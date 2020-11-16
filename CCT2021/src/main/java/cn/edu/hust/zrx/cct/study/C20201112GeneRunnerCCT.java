package cn.edu.hust.zrx.cct.study;

import cn.edu.hust.zrx.cct.advanced.CctUtils;
import cn.edu.hust.zrx.cct.advanced.CosyArbitraryOrder;
import cn.edu.hust.zrx.cct.advanced.MathFunction;
import cn.edu.hust.zrx.cct.advanced.combined.GantryDataBipolarCo;
import cn.edu.hust.zrx.cct.advanced.combined.GantryDataBipolarCoUtils;
import cn.edu.hust.zrx.cct.base.BaseUtils;
import cn.edu.hust.zrx.cct.base.cct.Cct;
import cn.edu.hust.zrx.cct.base.cct.CctFactory;
import cn.edu.hust.zrx.cct.base.cct.SoleLayerCct;
import cn.edu.hust.zrx.cct.base.line.Line2;
import cn.edu.hust.zrx.cct.base.line.Trajectory;
import cn.edu.hust.zrx.cct.base.magnet.Elements;
import cn.edu.hust.zrx.cct.base.magnet.FarIgnoreMagnet;
import cn.edu.hust.zrx.cct.base.magnet.qs.QsHardPlaneMagnet;
import cn.edu.hust.zrx.cct.base.particle.*;
import cn.edu.hust.zrx.cct.base.point.Point2;
import cn.edu.hust.zrx.cct.base.python.Plot2d;
import cn.edu.hust.zrx.cct.base.vector.Vector2;
import cn.hutool.core.io.file.FileReader;
import cn.hutool.core.io.file.FileWriter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static cn.edu.hust.zrx.cct.base.BaseUtils.Constant.MM;
import static cn.edu.hust.zrx.cct.base.BaseUtils.Constant.PRESENT;

/**
 * Description
 * C20201112GeneRunnerCCT
 * <p>
 * Data
 * 8:46
 *
 * @author zrx
 * @version 1.0
 */

public class C20201112GeneRunnerCCT {

    static final int VAR_NUMBER = 8; // 变量数目

    static double STEP_LENGTH = 1 * MM;

    static int NUMBER_PER_WINDING = 120;

    static double IGNORE_DISTANCE = 5.0;

    static int PARTICLE_NUMBER_PER_PLANE_PER_DP = 5;

//    static double RUN_LENGTH = GantryDataBipolarCoUtils.getTrajectory2().getLength();

    public static void main(String[] args) throws IOException {
        readMetaInfo("meta.txt");

        File inputFile = new File("input.txt");
        if (!inputFile.exists()) System.err.println("input.txt不存在");
        BufferedReader in = FileReader.create(inputFile).getReader();

        // 组数
        int number = Integer.parseInt(in.readLine());
        // 全部数据
        List<BaseUtils.Content.BiContent<Integer, double[]>> datas = new ArrayList<>(number);
        for (int i = 1; i <= number; i++) {
            int index = Integer.parseInt(in.readLine());
            if (i != index) System.err.println("组数不正确，输入格式有误？");
            datas.add(BaseUtils.Content.BiContent.create(i, readInput(in)));
        }

        in.close();

        // 计算
        List<double[]> results = datas.stream()
//                .parallel() // TODO
                .map(bi -> {
                    double[] data = bi.getT2();
                    double[] result = compute(data);
                    Integer index = bi.getT1();
                    System.out.println("[" + new Date().toString() + "]" + " index = " + index + '\n' +
                            "data = " + Arrays.toString(data) + '\n' +
                            "result = " + Arrays.toString(result)
                    );
                    return BaseUtils.Content.BiContent.create(index, result);
                })
                .collect(Collectors.toList())
                .stream().sequential()
                .sorted(Comparator.comparingInt(BaseUtils.Content.BiContent::getT1))
                .map(BaseUtils.Content.BiContent::getT2)
                .collect(Collectors.toList());

        // 输出
        File outputFile = new File("output.txt");
        if (outputFile.exists()) outputFile.delete();
        BufferedWriter out = FileWriter.create(outputFile).getWriter(false);
        for (int i = 0; i < results.size(); i++) {
            double[] result = results.get(i);
            out.append(String.valueOf(i + 1)).append(" "); // index from 1
            out.append(Arrays.stream(result).mapToObj(String::valueOf).collect(Collectors.joining(" ")));
            out.append('\n');
        }
        out.flush();
        out.close();
    }

    // 2020年11月12日 OK
    private static void readMetaInfo(String fileName) throws IOException {
        File inputFile = new File(fileName);
        if (!inputFile.exists()) System.err.println(fileName + "不存在");
        BufferedReader in = FileReader.create(inputFile).getReader();

        if (!in.readLine().startsWith("STEP_LENGTH")) System.err.println(fileName + "STEP_LENGTH 格式不正确");

        STEP_LENGTH = Double.parseDouble(in.readLine()) * MM;

        if (!in.readLine().startsWith("NUMBER_PER_WINDING")) System.err.println(fileName + "NUMBER_PER_WINDING 格式不正确");

        NUMBER_PER_WINDING = Integer.parseInt(in.readLine());

        if (!in.readLine().startsWith("IGNORE_DISTANCE")) System.err.println(fileName + "IGNORE_DISTANCE 格式不正确");

        IGNORE_DISTANCE = Double.parseDouble(in.readLine());

        if (!in.readLine().startsWith("PARTICLE_NUMBER_PER_PLANE_PER_DP"))
            System.err.println(fileName + "PARTICLE_NUMBER_PER_PLANE_PER_DP 格式不正确");

        PARTICLE_NUMBER_PER_PLANE_PER_DP = Integer.parseInt(in.readLine());


        Map<String, Object> map = new HashMap<>();
        map.put("STEP_LENGTH", STEP_LENGTH);
        map.put("NUMBER_PER_WINDING", NUMBER_PER_WINDING);
        map.put("IGNORE_DISTANCE", IGNORE_DISTANCE);
        map.put("PARTICLE_NUMBER_PER_PLANE_PER_DP", PARTICLE_NUMBER_PER_PLANE_PER_DP);

        System.out.println("读取元信息成功 = " + map);
    }

    // 2020年11月12日 OK
    private static double[] readInput(BufferedReader in) throws IOException {
        double[] data = new double[VAR_NUMBER];
        for (int i = 0; i < data.length; i++) {
            data[i] = Double.parseDouble(in.readLine());
        }
        return data;
    }

    // 8个变量 2020年11月12日 OK
    public static GantryDataBipolarCo.SecondBend create(double[] data) {
        GantryDataBipolarCo.SecondBend secondBend = GantryDataBipolarCo.SecondBend.getDefault();

//        secondBend.QS3_GRADIENT = data[0]; // -5.7
//        secondBend.QS3_SECOND_GRADIENT = data[1]; // -10
        secondBend.dipoleCct345TiltAngles[1] = data[2 - 2]; // 80
        secondBend.dipoleCct345TiltAngles[2] = data[3 - 2]; // 90
        secondBend.dipoleCct345TiltAngles[3] = data[4 - 2]; // 90
        secondBend.agCct345TiltAngles[0] = data[5 - 2]; // 90
        secondBend.agCct345TiltAngles[2] = data[6 - 2]; // 90
        secondBend.agCct345TiltAngles[3] = data[7 - 2]; // 90
        secondBend.dipoleCct345I0 = data[8 - 2]; // -9457
        secondBend.agCct345I0 = data[9 - 2]; // -6700

        return secondBend;
    }

    public static double[] objectives(GantryDataBipolarCo.SecondBend secondBend) {
        GantryDataBipolarCo.FirstBend FIRST_BEND = GantryDataBipolarCo.FirstBend.getDefault();
        Line2 trajectoryPart2 = GantryDataBipolarCoUtils.getTrajectory2(FIRST_BEND, secondBend);
        final double stepLen = STEP_LENGTH;
        final int numberPerWinding = NUMBER_PER_WINDING;

        final double LENGTH = secondBend.DL2 + secondBend.CCT345_LENGTH + secondBend.GAP3;
        // ---------------- create magnet ----------------------
        secondBend.disperseNumberPerWinding = numberPerWinding;

        Cct e = dipoleCct345_local_goodWindingMethod(secondBend)
                .addCct(agCct345_local_goodWindingMethod(secondBend));

        e = moveCCT345_1(e, FIRST_BEND, secondBend);

        // ------------------- run ------------------------
        List<Double> result = new ArrayList<>();

        List<RunningParticle> particles = particlesAtEntry(secondBend); // 存在结构 x-dp0 x-dp5 y-dp0 y-dp5

        ParticleRunner.runThread(particles, e, LENGTH, stepLen); // run

        RunningParticle ip_end = ParticleFactory.createIdealProtonAtTrajectory(trajectoryPart2, LENGTH, 215);

        List<Point2> ppx_track = PhaseSpaceParticles.phaseSpaceParticlesFromRunningParticles(
                ip_end, ip_end.computeNaturalCoordinateSystem(), particles)
                .stream().map(PhaseSpaceParticle::projectionToXXpPlane).collect(Collectors.toList());

        List<Point2> ppy_track = PhaseSpaceParticles.phaseSpaceParticlesFromRunningParticles(
                ip_end, ip_end.computeNaturalCoordinateSystem(), particles)
                .stream().map(PhaseSpaceParticle::projectionToYYpPlane).collect(Collectors.toList());

        // 拿到 COSY
        List<PhaseSpaceParticle> cosyBeforeQs3 = cosyParticleBeforeQs3();

        List<Point2> ppx_cosy = cosyBeforeQs3.stream().map(PhaseSpaceParticle::projectionToXXpPlane)
                .collect(Collectors.toList());

        List<Point2> ppy_cosy = cosyBeforeQs3.stream().map(PhaseSpaceParticle::projectionToYYpPlane)
                .collect(Collectors.toList());

        if (true) { // plot
            Plot2d.plot2(Point2.convert(ppx_track, 1 / MM, 1 / MM), Plot2d.BLACK_DASH);
            Plot2d.plot2(Point2.convert(ppy_track, 1 / MM, 1 / MM), Plot2d.RED_DASH);
            Plot2d.plot2(Point2.convert(ppx_cosy, 1 / MM, 1 / MM), Plot2d.YELLOW_LINE);
            Plot2d.plot2(Point2.convert(ppy_cosy, 1 / MM, 1 / MM), Plot2d.BLUE_LINE);

            Plot2d.info("X-Y/mm", "XP-YP/mm", "TRACK-COSY", 18);

            Plot2d.legend(18, "TRACK-X", "TRACK-Y", "COSY-X", "COSY-Y");

            Plot2d.show();
        }


        BaseUtils.StreamTools.Average avg_x = new BaseUtils.StreamTools.Average();
        BaseUtils.StreamTools.Average avg_xp = new BaseUtils.StreamTools.Average();
        BaseUtils.StreamTools.Average avg_y = new BaseUtils.StreamTools.Average();
        BaseUtils.StreamTools.Average avg_yp = new BaseUtils.StreamTools.Average();

        // 获取数据
        int number = PARTICLE_NUMBER_PER_PLANE_PER_DP;
        BaseUtils.StreamTools.from(0).to(number * 2 - 1)
                .forEach(i -> {
                    Point2 pc = ppx_cosy.get(i);
                    Point2 pt = ppx_track.get(i);
                    avg_x.add(Math.abs(pc.x - pt.x) / MM);
                    avg_xp.add(Math.abs(pc.y - pt.y) / MM);
                });

        BaseUtils.StreamTools.from(number * 2).to(number * 4 - 1)
                .forEach(i -> {
                    Point2 pc = ppy_cosy.get(i);
                    Point2 pt = ppy_track.get(i);
                    avg_y.add(Math.abs(pc.x - pt.x) / MM);
                    avg_yp.add(Math.abs(pc.y - pt.y) / MM);
                });

        result.add(avg_x.average());
        result.add(avg_xp.average());
        result.add(avg_y.average());
        result.add(avg_yp.average());


        return result.stream().mapToDouble(Double::doubleValue).toArray();
    }

    public static double[] compute(double[] v) {
        GantryDataBipolarCo.SecondBend secondBend = create(v);
        return objectives(secondBend);
    }


    // full by particlesAtEntry
    private static volatile List<RunningParticle> PARTICLES_AT_ENTRY = null;

    // 利用 COSY 值得到的后偏转段入口粒子 2020年11月12日
    public static synchronized List<RunningParticle> particlesAtEntry(GantryDataBipolarCo.SecondBend secondBend) {
        // x 平面，动量分散 0
        // x 平面，动量分散 5%

        // y 平面，动量分散 0
        // y 平面，动量分散 5%

        if (PARTICLES_AT_ENTRY == null) {
            int number = PARTICLE_NUMBER_PER_PLANE_PER_DP;

            List<PhaseSpaceParticle> pp = PhaseSpaceParticles.phaseSpaceParticlesAlongPositiveEllipseInPlane(
                    true, 3.5 * MM, 7.5 * MM, 0, number);

            pp.addAll(PhaseSpaceParticles.phaseSpaceParticlesAlongPositiveEllipseInPlane(
                    true, 3.5 * MM, 7.5 * MM, 5 * PRESENT, number));

            pp.addAll(PhaseSpaceParticles.phaseSpaceParticlesAlongPositiveEllipseInPlane(
                    false, 3.5 * MM, 7.5 * MM, 0, number));

            pp.addAll(PhaseSpaceParticles.phaseSpaceParticlesAlongPositiveEllipseInPlane(
                    false, 3.5 * MM, 7.5 * MM, 5 * PRESENT, number));

            // 转为COSY能量分散
            pp = PhaseSpaceParticles.convertDeltaFromMomentumDispersionToEnergyDispersion(pp, 215);

            // 运行
            List<PhaseSpaceParticle> ppEnd = COSY_MAP1021.part1.map.apply(pp, 5);

            // 能量分散转回去
            ppEnd = PhaseSpaceParticles.convertDeltaFromEnergyDispersionToEnergyDispersionMomentumDispersion(
                    ppEnd, 215);

            // 变成实际粒子
            GantryDataBipolarCo.FirstBend FIRST_BEND = GantryDataBipolarCo.FirstBend.getDefault();
            Line2 trajectoryPart2 = GantryDataBipolarCoUtils.getTrajectory2(FIRST_BEND, secondBend);
            RunningParticle ip = ParticleFactory.createIdealProtonAtTrajectory(trajectoryPart2, 215);

            PARTICLES_AT_ENTRY = ParticleFactory.createParticlesFromPhaseSpaceParticle(
                    ip, ip.computeNaturalCoordinateSystem(), ppEnd);
        }

        return ParticleFactory.copy(PARTICLES_AT_ENTRY);
    }

    // full by cosyParticleAfterQs3
    private static volatile List<PhaseSpaceParticle> COSY_PARTICLES_BEFORE_QS3 = null;

    public static synchronized List<PhaseSpaceParticle> cosyParticleBeforeQs3() {

        if (COSY_PARTICLES_BEFORE_QS3 == null) {
            int number = PARTICLE_NUMBER_PER_PLANE_PER_DP;

            List<PhaseSpaceParticle> pp = PhaseSpaceParticles.phaseSpaceParticlesAlongPositiveEllipseInPlane(
                    true, 3.5 * MM, 7.5 * MM, 0, number);

            pp.addAll(PhaseSpaceParticles.phaseSpaceParticlesAlongPositiveEllipseInPlane(
                    true, 3.5 * MM, 7.5 * MM, 5 * PRESENT, number));

            pp.addAll(PhaseSpaceParticles.phaseSpaceParticlesAlongPositiveEllipseInPlane(
                    false, 3.5 * MM, 7.5 * MM, 0, number));

            pp.addAll(PhaseSpaceParticles.phaseSpaceParticlesAlongPositiveEllipseInPlane(
                    false, 3.5 * MM, 7.5 * MM, 5 * PRESENT, number));

            // 转为COSY能量分散
            pp = PhaseSpaceParticles.convertDeltaFromMomentumDispersionToEnergyDispersion(pp, 215);

            // 运行 all beforeQS3WithPart1
            List<PhaseSpaceParticle> ppEnd = COSY_MAP1021.beforeQS3WithPart1.map.apply(pp, 5);

            // 能量分散转回去
            COSY_PARTICLES_BEFORE_QS3 = PhaseSpaceParticles.convertDeltaFromEnergyDispersionToEnergyDispersionMomentumDispersion(
                    ppEnd, 215);


        }


        return PhaseSpaceParticles.copy(COSY_PARTICLES_BEFORE_QS3);
    }

    //--------------- cons

    public static Cct dipoleCct345_local_goodWindingMethod(GantryDataBipolarCo.SecondBend secondBend) {
        Cct cct = Cct.getEmptyCct();

        final double bigR = secondBend.dipoleCct345BigR;
        final double innerSmallR = secondBend.dipoleCct345SmallRInner;
        final double outerSmall = secondBend.dipoleCct345SmallROuter;
        final double bendingAngle = secondBend.dipoleCct345Angle;
        final double bendingRadian = BaseUtils.Converter.angleToRadian(bendingAngle);
        final double[] tiltAngles = secondBend.dipoleCct345TiltAngles;
        final int windingNumber = secondBend.dipoleCct345WindingNumber;
        final double endKsi = windingNumber * Math.PI * 2;
        final double I0 = secondBend.dipoleCct345I0; // 2.355 T
        final double phi0 = bendingRadian / windingNumber;
        final int disperseNumberPerWinding = secondBend.disperseNumberPerWinding;

        MathFunction phiKsiFun = MathFunction.createPhiKsiFun(bigR, innerSmallR, phi0, tiltAngles);
        SoleLayerCct soleLayerCct = SoleLayerCct.createSoleLayerCct(bigR, innerSmallR, phiKsiFun, I0,
                0, endKsi, windingNumber * disperseNumberPerWinding);
        cct.addSoleLayerCct(soleLayerCct);


        MathFunction phiKsiFun1 = MathFunction.createPhiKsiFun(bigR, outerSmall, phi0, tiltAngles);
        phiKsiFun1 = phiKsiFun1.yAxisSymmetry();
        SoleLayerCct soleLayerCct1 = SoleLayerCct.createSoleLayerCct(bigR, outerSmall, phiKsiFun1, I0,
                0, -endKsi, windingNumber * disperseNumberPerWinding);
        cct.addSoleLayerCct(soleLayerCct1);

        return cct;
    }

    public static Cct agCct345_local_goodWindingMethod(GantryDataBipolarCo.SecondBend secondBend) {
        Cct cct = Cct.getEmptyCct();

        final double bigR = secondBend.agCct345BigR;

        final double innerSmallR = secondBend.agCct345SmallRInner;
        final double outerSmallR = secondBend.agCct345SmallROuter;
        final double[] bendingAngles = {secondBend.agCctAngle3, secondBend.agCctAngle4, secondBend.agCctAngle5};
        BaseUtils.Equal.requireEqual(Arrays.stream(bendingAngles).sum(), 67.5);
        final double[] bendingRadians = BaseUtils.Converter.angleToRadian(bendingAngles);
        final double[] tiltAngles = secondBend.agCct345TiltAngles;
        final int[] windingNumbers = {secondBend.agCctWindingNumber3, secondBend.agCctWindingNumber4, secondBend.agCctWindingNumber5};
        final double[] endKsis = BaseUtils.ArrayUtils.multiple(windingNumbers, Math.PI * 2);
        final double I0 = secondBend.agCct345I0; // 2.355 T
        final double[] phi0 = BaseUtils.ArrayUtils.div(bendingRadians, windingNumbers);

        MathFunction phiKsiFunInner1 = MathFunction.createPhiKsiFun(bigR, innerSmallR, phi0[0], tiltAngles);
        SoleLayerCct soleLayerCctInner1 = SoleLayerCct.createSoleLayerCct(bigR, innerSmallR, phiKsiFunInner1, I0,
                0, endKsis[0], windingNumbers[0] * 360);
        cct.addSoleLayerCct(soleLayerCctInner1);

        MathFunction phiKsiFunInner2 = MathFunction.createPhiKsiFun(bigR, innerSmallR, phi0[1], BaseUtils.ArrayUtils.dot(tiltAngles, -1))
                .yAxisSymmetry().move(Vector2.create(endKsis[0], bendingRadians[0] + phi0[0]));
        SoleLayerCct soleLayerCctInner2 = SoleLayerCct.createSoleLayerCct(bigR, innerSmallR, phiKsiFunInner2, I0,
                endKsis[0], endKsis[0] - endKsis[1], windingNumbers[1] * 360);
        cct.addSoleLayerCct(soleLayerCctInner2);

        MathFunction phiKsiFunInner3 = MathFunction.createPhiKsiFun(bigR, innerSmallR, phi0[2], tiltAngles)
                .move(Vector2.create(endKsis[0] - endKsis[1],
                        bendingRadians[0] + bendingRadians[1] + phi0[0] + phi0[1]));
        SoleLayerCct soleLayerCctInner3 = SoleLayerCct.createSoleLayerCct(bigR, innerSmallR, phiKsiFunInner3, I0,
                endKsis[0] - endKsis[1], endKsis[0] - endKsis[1] + endKsis[2], windingNumbers[2] * 360);
        cct.addSoleLayerCct(soleLayerCctInner3);


        MathFunction phiKsiFunOuter1 = MathFunction.createPhiKsiFun(bigR, outerSmallR, phi0[0], tiltAngles)
                .yAxisSymmetry();
        SoleLayerCct soleLayerCctOuter1 = SoleLayerCct.createSoleLayerCct(bigR, outerSmallR, phiKsiFunOuter1, I0,
                0, -endKsis[0], windingNumbers[0] * 360);
        cct.addSoleLayerCct(soleLayerCctOuter1);
        MathFunction phiKsiFunOuter2 = MathFunction.createPhiKsiFun(bigR, outerSmallR, phi0[1], BaseUtils.ArrayUtils.dot(tiltAngles, -1))
                .yAxisSymmetry().move(Vector2.create(endKsis[0], bendingRadians[0] + phi0[0]))
                .yAxisSymmetry();
        SoleLayerCct soleLayerCctOuter2 = SoleLayerCct.createSoleLayerCct(bigR, outerSmallR, phiKsiFunOuter2, I0,
                -endKsis[0], -(endKsis[0] - endKsis[1]), windingNumbers[1] * 360);
        cct.addSoleLayerCct(soleLayerCctOuter2);
        MathFunction phiKsiFunOuter3 = MathFunction.createPhiKsiFun(bigR, outerSmallR, phi0[2], tiltAngles)
                .move(Vector2.create(endKsis[0] - endKsis[1],
                        bendingRadians[0] + bendingRadians[1] + phi0[0] + phi0[1]))
                .yAxisSymmetry();
        SoleLayerCct soleLayerCctOuter3 = SoleLayerCct.createSoleLayerCct(bigR, outerSmallR, phiKsiFunOuter3, I0,
                -(endKsis[0] - endKsis[1]), -(endKsis[0] - endKsis[1] + endKsis[2]), windingNumbers[2] * 360);
        cct.addSoleLayerCct(soleLayerCctOuter3);

        return cct;
    }

    private static Cct moveCCT345_1(Cct local, GantryDataBipolarCo.FirstBend firstBend, GantryDataBipolarCo.SecondBend secondBend) {
        Trajectory trajectory2 = GantryDataBipolarCoUtils.getTrajectory2(firstBend, secondBend);
        Point2 afterDl2 = trajectory2.pointAt(secondBend.DL2).copy();
        Vector2 directDl2 = trajectory2.directAt(secondBend.DL2).copy();

        return local.symmetricXZPlane().rotateInXYPlane(Point2.origin(), BaseUtils.Converter.angleToRadian(
                135
        )).move(
                afterDl2.moveSelf(
                        directDl2.rotateSelf(BaseUtils.Converter.angleToRadian(-90))
                                .changeLengthSelf(secondBend.trajectoryBigRPart2))
                        .toVector3()
        );
    }

    private static QsHardPlaneMagnet getQs3(GantryDataBipolarCo.FirstBend firstBend, GantryDataBipolarCo.SecondBend secondBend) {
        Trajectory trajectoryPart2 = GantryDataBipolarCoUtils.getTrajectory2(firstBend, secondBend);


        return QsHardPlaneMagnet.create(secondBend.QS3_LEN, secondBend.QS3_GRADIENT, secondBend.QS3_SECOND_GRADIENT,
                secondBend.QS3_APERTURE_MM,
                trajectoryPart2.pointAt(secondBend.DL2 + secondBend.CCT345_LENGTH + secondBend.GAP3),
                trajectoryPart2.directAt(secondBend.DL2 + secondBend.CCT345_LENGTH + secondBend.GAP3));
    }

    private static Cct moveCCT345_2(Cct moved1, GantryDataBipolarCo.FirstBend firstBend, GantryDataBipolarCo.SecondBend secondBend) {
        Trajectory trajectory2 = GantryDataBipolarCoUtils.getTrajectory2(firstBend, secondBend);

        //Cct cct12_1 = getCct12_1();
        //
        //        Trajectory trajectoryPart1 = getTrajectoryPart1();
        //        return CctFactory.symmetryInXYPlaneByLine(
        //                cct12_1,
        //                trajectoryPart1.pointAt(DL1 + CCT12_LENGTH + GAP1 + QS1_LEN + GAP2 + QS2_LEN / 2),
        //                trajectoryPart1.directAt(DL1 + CCT12_LENGTH + GAP1 + QS1_LEN + GAP2 + QS2_LEN / 2).rotateSelf(Math.PI / 2)
        //        );

        double mid = secondBend.DL2 + secondBend.CCT345_LENGTH + secondBend.GAP3 + secondBend.QS3_LEN / 2;

        return CctFactory.symmetryInXYPlaneByLine(
                moved1,
                trajectory2.pointAt(mid),
                trajectory2.directAt(mid).rotateSelf(Math.PI / 2)
        );
    }


    public static enum COSY_MAP1021 {
        part2,
        cct345WithDl2TwoSides,
        beforeQS3,
        part1,
        beforeQS3WithPart1,
        all;

        CosyArbitraryOrder.CosyMapArbitraryOrder map;

        static {
            all.map = CosyArbitraryOrder.readMap(
                    "  0.9204298      2.342948     0.0000000E+00 0.0000000E+00 0.2875828E-01 100000\n" +
                            " -0.1511535     0.7016887     0.0000000E+00 0.0000000E+00-0.5728590E-02 010000\n" +
                            "  0.0000000E+00 0.0000000E+00 0.7342880     0.8951463     0.0000000E+00 001000\n" +
                            "  0.0000000E+00 0.0000000E+00-0.3362275     0.9519796     0.0000000E+00 000100\n" +
                            "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00  1.000000     000010\n" +
                            " -0.9258508E-03-0.3360115E-01 0.0000000E+00 0.0000000E+00  2.053380     000001\n" +
                            "   4.885144      28.34025     0.0000000E+00 0.0000000E+00-0.3071154     200000\n" +
                            "   15.06786      21.59868     0.0000000E+00 0.0000000E+00 0.8201890     110000\n" +
                            " -0.3743387     -8.469834     0.0000000E+00 0.0000000E+00 0.2712384E-01 020000\n" +
                            "  0.0000000E+00 0.0000000E+00  47.42200     -18.29999     0.0000000E+00 101000\n" +
                            "  0.0000000E+00 0.0000000E+00  20.27668     -8.717686     0.0000000E+00 011000\n" +
                            "   15.52291      9.154235     0.0000000E+00 0.0000000E+00 -1.067533     002000\n" +
                            "  0.0000000E+00 0.0000000E+00  18.80651     -30.17513     0.0000000E+00 100100\n" +
                            "  0.0000000E+00 0.0000000E+00 0.4577091     -21.73826     0.0000000E+00 010100\n" +
                            "   20.96289      10.99831     0.0000000E+00 0.0000000E+00 0.7893651E-02 001100\n" +
                            "  0.1727875      1.406819     0.0000000E+00 0.0000000E+00-0.7117329E-01 100001\n" +
                            "  0.1091187    -0.8499203E-01 0.0000000E+00 0.0000000E+00-0.2705320E-02 010001\n" +
                            "  0.0000000E+00 0.0000000E+00 -1.566856      2.395144     0.0000000E+00 001001\n" +
                            "  -2.576880     -10.77361     0.0000000E+00 0.0000000E+00-0.3607443     000200\n" +
                            "  0.0000000E+00 0.0000000E+00-0.6480240     0.1446635     0.0000000E+00 000101\n" +
                            " -0.8688021E-02 0.1899410E-01 0.0000000E+00 0.0000000E+00 -1.892372     000002\n" +
                            "  -797.0295     -96.55657     0.0000000E+00 0.0000000E+00 -51.13967     300000\n" +
                            "  -1179.201     -432.6189     0.0000000E+00 0.0000000E+00 -52.18501     210000\n" +
                            "  -685.8243     -708.8992     0.0000000E+00 0.0000000E+00 -16.96513     120000\n" +
                            "  -217.8797     -254.9463     0.0000000E+00 0.0000000E+00 -6.652174     030000\n" +
                            "  0.0000000E+00 0.0000000E+00  2906.779      652.1254     0.0000000E+00 201000\n" +
                            "  0.0000000E+00 0.0000000E+00  2029.305      575.5792     0.0000000E+00 111000\n" +
                            "  0.0000000E+00 0.0000000E+00  126.3083      237.6345     0.0000000E+00 021000\n" +
                            "   680.7857      284.9838     0.0000000E+00 0.0000000E+00 -67.97312     102000\n" +
                            "   251.4978      120.3974     0.0000000E+00 0.0000000E+00 -25.05416     012000\n" +
                            "  0.0000000E+00 0.0000000E+00  103.1423     -41.66030     0.0000000E+00 003000\n" +
                            "  0.0000000E+00 0.0000000E+00  2606.710      590.6888     0.0000000E+00 200100\n" +
                            "  0.0000000E+00 0.0000000E+00  1336.176      736.8877     0.0000000E+00 110100\n" +
                            "  0.0000000E+00 0.0000000E+00  34.35928      364.1676     0.0000000E+00 020100\n" +
                            "   1854.990      587.5165     0.0000000E+00 0.0000000E+00 -41.59704     101100\n" +
                            "   408.9860      154.7060     0.0000000E+00 0.0000000E+00 -1.290977     011100\n" +
                            "  0.0000000E+00 0.0000000E+00  208.8665     -31.64033     0.0000000E+00 002100\n" +
                            "  -6.344459      59.24166     0.0000000E+00 0.0000000E+00  18.79133     200001\n" +
                            "   6.020431      32.13839     0.0000000E+00 0.0000000E+00  14.57900     110001\n" +
                            "   3.224877      6.268984     0.0000000E+00 0.0000000E+00  3.044752     020001\n" +
                            "  0.0000000E+00 0.0000000E+00 -76.03886     -16.77605     0.0000000E+00 101001\n" +
                            "  0.0000000E+00 0.0000000E+00 -49.60650     -10.74272     0.0000000E+00 011001\n" +
                            "  0.5346831      7.527031     0.0000000E+00 0.0000000E+00  12.38693     002001\n" +
                            "   1194.301      83.92642     0.0000000E+00 0.0000000E+00  64.57250     100200\n" +
                            "   178.9343     -39.62327     0.0000000E+00 0.0000000E+00  13.67199     010200\n" +
                            "  0.0000000E+00 0.0000000E+00  130.3576      293.8730     0.0000000E+00 001200\n" +
                            "  0.0000000E+00 0.0000000E+00 -6.104951      102.5838     0.0000000E+00 100101\n" +
                            "  0.0000000E+00 0.0000000E+00 -3.974531      23.19271     0.0000000E+00 010101\n" +
                            "  -63.26682     -40.86532     0.0000000E+00 0.0000000E+00  21.01019     001101\n" +
                            "   10.12700      4.334550     0.0000000E+00 0.0000000E+00-0.3924230E-01 100002\n" +
                            "   3.707241      1.187368     0.0000000E+00 0.0000000E+00 0.9770301E-02 010002\n" +
                            "  0.0000000E+00 0.0000000E+00  12.76915     -2.307577     0.0000000E+00 001002\n" +
                            "  0.0000000E+00 0.0000000E+00  307.3727      452.8350     0.0000000E+00 000300\n" +
                            "  -4.726842     -20.38336     0.0000000E+00 0.0000000E+00  16.63024     000201\n" +
                            "  0.0000000E+00 0.0000000E+00  16.37677      2.661159     0.0000000E+00 000102\n" +
                            " -0.5142238E-01-0.6135132E-02 0.0000000E+00 0.0000000E+00  1.762705     000003\n" +
                            "  -6244.844     -4188.357     0.0000000E+00 0.0000000E+00 -1228.809     400000\n" +
                            "  -5451.411     -8738.913     0.0000000E+00 0.0000000E+00 -1521.005     310000\n" +
                            "   12470.42     -6169.308     0.0000000E+00 0.0000000E+00  140.9155     220000\n" +
                            "   12575.26     -92.73620     0.0000000E+00 0.0000000E+00  412.4666     130000\n" +
                            "   3002.432      1534.098     0.0000000E+00 0.0000000E+00  56.95713     040000\n" +
                            "  0.0000000E+00 0.0000000E+00  71635.62      22230.88     0.0000000E+00 301000\n" +
                            "  0.0000000E+00 0.0000000E+00  61206.18      23348.60     0.0000000E+00 211000\n" +
                            "  0.0000000E+00 0.0000000E+00 -15333.03      1854.025     0.0000000E+00 121000\n" +
                            "  0.0000000E+00 0.0000000E+00 -10380.97     -1475.190     0.0000000E+00 031000\n" +
                            "  -11477.08     -3624.832     0.0000000E+00 0.0000000E+00 -6946.682     202000\n" +
                            "  -15520.59     -4125.748     0.0000000E+00 0.0000000E+00 -5066.296     112000\n" +
                            "  -6965.047     -2165.210     0.0000000E+00 0.0000000E+00 -600.1663     022000\n" +
                            "  0.0000000E+00 0.0000000E+00  14118.17      1664.035     0.0000000E+00 103000\n" +
                            "  0.0000000E+00 0.0000000E+00  5520.337      600.2018     0.0000000E+00 013000\n" +
                            "   1830.741      648.9129     0.0000000E+00 0.0000000E+00 -247.9355     004000\n" +
                            "  0.0000000E+00 0.0000000E+00  72756.79      18684.66     0.0000000E+00 300100\n" +
                            "  0.0000000E+00 0.0000000E+00  37632.34      12279.35     0.0000000E+00 210100\n" +
                            "  0.0000000E+00 0.0000000E+00 -28249.39     -4282.864     0.0000000E+00 120100\n" +
                            "  0.0000000E+00 0.0000000E+00 -8958.878     -3364.064     0.0000000E+00 030100\n" +
                            "  -8395.211     -3057.664     0.0000000E+00 0.0000000E+00 -14504.10     201100\n" +
                            "  -31625.56     -6532.423     0.0000000E+00 0.0000000E+00 -9557.451     111100\n" +
                            "  -18420.88     -3826.847     0.0000000E+00 0.0000000E+00 -1340.012     021100\n" +
                            "  0.0000000E+00 0.0000000E+00  26640.77     -1078.209     0.0000000E+00 102100\n" +
                            "  0.0000000E+00 0.0000000E+00  9261.612     -531.3176     0.0000000E+00 012100\n" +
                            "   9026.984      3601.400     0.0000000E+00 0.0000000E+00 -710.7056     003100\n" +
                            "  -2726.676      85.08376     0.0000000E+00 0.0000000E+00  223.9421     300001\n" +
                            "  -2653.320     -269.1689     0.0000000E+00 0.0000000E+00  167.7071     210001\n" +
                            "  -541.9313     -420.7670     0.0000000E+00 0.0000000E+00 -174.1346     120001\n" +
                            "   40.12787     -61.13530     0.0000000E+00 0.0000000E+00 -75.09170     030001\n" +
                            "  0.0000000E+00 0.0000000E+00 -5489.523     -1844.479     0.0000000E+00 201001\n" +
                            "  0.0000000E+00 0.0000000E+00 -6297.592     -1712.865     0.0000000E+00 111001\n" +
                            "  0.0000000E+00 0.0000000E+00 -1032.462     -380.0584     0.0000000E+00 021001\n" +
                            "   836.6016      387.1534     0.0000000E+00 0.0000000E+00  133.2199     102001\n" +
                            "  -91.32184     -25.37364     0.0000000E+00 0.0000000E+00  52.12548     012001\n" +
                            "  0.0000000E+00 0.0000000E+00 -456.4088      78.52101     0.0000000E+00 003001\n" +
                            "   11762.09      1067.726     0.0000000E+00 0.0000000E+00 -7049.794     200200\n" +
                            "  -13003.56     -1742.409     0.0000000E+00 0.0000000E+00 -4945.996     110200\n" +
                            "  -9872.036      744.6892     0.0000000E+00 0.0000000E+00 -1325.793     020200\n" +
                            "  0.0000000E+00 0.0000000E+00 -10616.77     -17235.75     0.0000000E+00 101200\n" +
                            "  0.0000000E+00 0.0000000E+00 -5208.763     -8941.145     0.0000000E+00 011200\n" +
                            "   15359.39      7462.531     0.0000000E+00 0.0000000E+00 -572.4739     002200\n" +
                            "  0.0000000E+00 0.0000000E+00 -8588.121     -3920.342     0.0000000E+00 200101\n" +
                            "  0.0000000E+00 0.0000000E+00 -7064.726     -3374.686     0.0000000E+00 110101\n" +
                            "  0.0000000E+00 0.0000000E+00 -1350.612     -1547.889     0.0000000E+00 020101\n" +
                            "  -2362.907     -1030.490     0.0000000E+00 0.0000000E+00  124.4395     101101\n" +
                            "  -1964.314     -1064.810     0.0000000E+00 0.0000000E+00 -269.9114     011101\n" +
                            "  0.0000000E+00 0.0000000E+00 -1299.355      1310.482     0.0000000E+00 002101\n" +
                            "  -60.60866     -28.56919     0.0000000E+00 0.0000000E+00 -31.64450     200002\n" +
                            "  -101.5502     -91.64918     0.0000000E+00 0.0000000E+00 -31.24417     110002\n" +
                            "  -91.29221     -55.01080     0.0000000E+00 0.0000000E+00 -7.695720     020002\n" +
                            "  0.0000000E+00 0.0000000E+00  442.0123      254.0249     0.0000000E+00 101002\n" +
                            "  0.0000000E+00 0.0000000E+00  174.9425      77.80741     0.0000000E+00 011002\n" +
                            "   41.48541      9.498526     0.0000000E+00 0.0000000E+00 -1.188670     002002\n" +
                            "  0.0000000E+00 0.0000000E+00 -15731.38     -17471.45     0.0000000E+00 100300\n" +
                            "  0.0000000E+00 0.0000000E+00 -4030.052     -9924.188     0.0000000E+00 010300\n" +
                            "   11655.01      8391.371     0.0000000E+00 0.0000000E+00 -366.1533     001300\n" +
                            "  -5058.711     -2133.199     0.0000000E+00 0.0000000E+00 -547.6290     100201\n" +
                            "  -2104.874     -1186.718     0.0000000E+00 0.0000000E+00 -444.6354     010201\n" +
                            "  0.0000000E+00 0.0000000E+00  19.22801      2853.822     0.0000000E+00 001201\n" +
                            "  0.0000000E+00 0.0000000E+00  438.7239     -40.13488     0.0000000E+00 100102\n" +
                            "  0.0000000E+00 0.0000000E+00  71.70591     -10.57024     0.0000000E+00 010102\n" +
                            "   504.0647      182.8720     0.0000000E+00 0.0000000E+00 -4.418250     001102\n" +
                            "  -8.186259      1.841879     0.0000000E+00 0.0000000E+00  2.452925     100003\n" +
                            "  -5.175391    -0.3432384     0.0000000E+00 0.0000000E+00 0.8866104     010003\n" +
                            "  0.0000000E+00 0.0000000E+00  1.509245      15.25913     0.0000000E+00 001003\n" +
                            "   2443.741      4258.919     0.0000000E+00 0.0000000E+00 -212.0163     000400\n" +
                            "  0.0000000E+00 0.0000000E+00 -413.5822      743.3074     0.0000000E+00 000301\n" +
                            "   226.7695      129.4281     0.0000000E+00 0.0000000E+00 -30.26036     000202\n" +
                            "  0.0000000E+00 0.0000000E+00 -32.80219      9.686172     0.0000000E+00 000103\n" +
                            "  0.4062480     0.1096512     0.0000000E+00 0.0000000E+00 -1.754385     000004\n" +
                            "   205859.5     -628.8931     0.0000000E+00 0.0000000E+00 -10632.59     500000\n" +
                            "   599148.2      57176.92     0.0000000E+00 0.0000000E+00  3206.590     410000\n" +
                            "   847886.2      195486.7     0.0000000E+00 0.0000000E+00  51656.24     320000\n" +
                            "   541621.1      202780.5     0.0000000E+00 0.0000000E+00  46859.86     230000\n" +
                            "   82690.90      85237.13     0.0000000E+00 0.0000000E+00  10399.25     140000\n" +
                            "  -16890.45      10878.99     0.0000000E+00 0.0000000E+00 -24.86644     050000\n" +
                            "  0.0000000E+00 0.0000000E+00  1271227.      409089.5     0.0000000E+00 401000\n" +
                            "  0.0000000E+00 0.0000000E+00  959344.6      335909.1     0.0000000E+00 311000\n" +
                            "  0.0000000E+00 0.0000000E+00 -1282037.     -373120.6     0.0000000E+00 221000\n" +
                            "  0.0000000E+00 0.0000000E+00 -997567.2     -337905.0     0.0000000E+00 131000\n" +
                            "  0.0000000E+00 0.0000000E+00 -88990.12     -48086.55     0.0000000E+00 041000\n" +
                            "  -270622.6     -115453.5     0.0000000E+00 0.0000000E+00 -86279.80     302000\n" +
                            "  -324337.5     -128411.9     0.0000000E+00 0.0000000E+00 -38951.94     212000\n" +
                            "   1719.837     -12018.23     0.0000000E+00 0.0000000E+00  79964.82     122000\n" +
                            "   59938.60      10794.44     0.0000000E+00 0.0000000E+00  31244.77     032000\n" +
                            "  0.0000000E+00 0.0000000E+00  839569.8      223778.9     0.0000000E+00 203000\n" +
                            "  0.0000000E+00 0.0000000E+00  626768.6      179147.5     0.0000000E+00 113000\n" +
                            "  0.0000000E+00 0.0000000E+00  46305.24      25047.76     0.0000000E+00 023000\n" +
                            "   14965.43      1714.429     0.0000000E+00 0.0000000E+00 -30932.01     104000\n" +
                            "  -3347.388     -1695.947     0.0000000E+00 0.0000000E+00 -11241.07     014000\n" +
                            "  0.0000000E+00 0.0000000E+00  18036.43     -948.1405     0.0000000E+00 005000\n" +
                            "  0.0000000E+00 0.0000000E+00  1523946.      492331.0     0.0000000E+00 400100\n" +
                            "  0.0000000E+00 0.0000000E+00  728923.2      308655.0     0.0000000E+00 310100\n" +
                            "  0.0000000E+00 0.0000000E+00 -1649729.     -397693.9     0.0000000E+00 220100\n" +
                            "  0.0000000E+00 0.0000000E+00 -826383.7     -256176.7     0.0000000E+00 130100\n" +
                            "  0.0000000E+00 0.0000000E+00  1443.127     -21357.35     0.0000000E+00 040100\n" +
                            "  -232545.5     -112276.8     0.0000000E+00 0.0000000E+00 -185875.1     301100\n" +
                            "  -227189.4     -53115.57     0.0000000E+00 0.0000000E+00  27706.14     211100\n" +
                            "   86225.25      48401.78     0.0000000E+00 0.0000000E+00  248092.7     121100\n" +
                            "   151737.1      28560.08     0.0000000E+00 0.0000000E+00  74197.74     031100\n" +
                            "  0.0000000E+00 0.0000000E+00  2356229.      599069.9     0.0000000E+00 202100\n" +
                            "  0.0000000E+00 0.0000000E+00  1654999.      501055.8     0.0000000E+00 112100\n" +
                            "  0.0000000E+00 0.0000000E+00  157430.9      102923.7     0.0000000E+00 022100\n" +
                            "   273796.7      81915.83     0.0000000E+00 0.0000000E+00 -100182.0     103100\n" +
                            "   33937.57      7128.896     0.0000000E+00 0.0000000E+00 -28355.68     013100\n" +
                            "  0.0000000E+00 0.0000000E+00  60403.56     -26103.54     0.0000000E+00 004100\n" +
                            "  -48978.95     -7222.500     0.0000000E+00 0.0000000E+00  2085.186     400001\n" +
                            "  -44840.52     -11594.32     0.0000000E+00 0.0000000E+00  474.1186     310001\n" +
                            "   22560.33     -9499.592     0.0000000E+00 0.0000000E+00 -7149.748     220001\n" +
                            "   18549.21     -4232.982     0.0000000E+00 0.0000000E+00 -4778.293     130001\n" +
                            "   583.6716     -1599.545     0.0000000E+00 0.0000000E+00 -213.2903     040001\n" +
                            "  0.0000000E+00 0.0000000E+00 -39038.37     -7442.306     0.0000000E+00 301001\n" +
                            "  0.0000000E+00 0.0000000E+00 -97766.97     -21574.07     0.0000000E+00 211001\n" +
                            "  0.0000000E+00 0.0000000E+00  38858.26      13204.53     0.0000000E+00 121001\n" +
                            "  0.0000000E+00 0.0000000E+00  38111.68      10465.22     0.0000000E+00 031001\n" +
                            "   71685.34      18759.40     0.0000000E+00 0.0000000E+00 -2071.606     202001\n" +
                            "   49908.57      15463.77     0.0000000E+00 0.0000000E+00  3774.717     112001\n" +
                            "   9284.254      4254.941     0.0000000E+00 0.0000000E+00  1262.833     022001\n" +
                            "  0.0000000E+00 0.0000000E+00 -36409.30     -18116.41     0.0000000E+00 103001\n" +
                            "  0.0000000E+00 0.0000000E+00 -17806.48     -7669.094     0.0000000E+00 013001\n" +
                            "   3083.374      2055.374     0.0000000E+00 0.0000000E+00  266.4293     004001\n" +
                            "   208627.6      95678.92     0.0000000E+00 0.0000000E+00 -73598.87     300200\n" +
                            "   182604.3      175215.2     0.0000000E+00 0.0000000E+00  102231.4     210200\n" +
                            "   24679.31      106972.5     0.0000000E+00 0.0000000E+00  169487.4     120200\n" +
                            "   87746.18      29763.05     0.0000000E+00 0.0000000E+00  43331.76     030200\n" +
                            "  0.0000000E+00 0.0000000E+00  1549900.      385527.3     0.0000000E+00 201200\n" +
                            "  0.0000000E+00 0.0000000E+00  1232162.      483584.2     0.0000000E+00 111200\n" +
                            "  0.0000000E+00 0.0000000E+00  295665.8      197370.5     0.0000000E+00 021200\n" +
                            "   923304.0      302378.2     0.0000000E+00 0.0000000E+00 -60999.99     102200\n" +
                            "   182229.0      30866.85     0.0000000E+00 0.0000000E+00  310.7293     012200\n" +
                            "  0.0000000E+00 0.0000000E+00  20921.09     -119847.6     0.0000000E+00 003200\n" +
                            "  0.0000000E+00 0.0000000E+00 -82039.73     -18042.53     0.0000000E+00 300101\n" +
                            "  0.0000000E+00 0.0000000E+00 -51982.80     -8631.905     0.0000000E+00 210101\n" +
                            "  0.0000000E+00 0.0000000E+00  126363.0      49069.19     0.0000000E+00 120101\n" +
                            "  0.0000000E+00 0.0000000E+00  50165.60      20698.49     0.0000000E+00 030101\n" +
                            "   240251.8      59605.97     0.0000000E+00 0.0000000E+00  19939.47     201101\n" +
                            "   195087.3      54901.73     0.0000000E+00 0.0000000E+00  25750.98     111101\n" +
                            "   64450.39      25344.73     0.0000000E+00 0.0000000E+00  6410.015     021101\n" +
                            "  0.0000000E+00 0.0000000E+00 -177701.0     -85470.01     0.0000000E+00 102101\n" +
                            "  0.0000000E+00 0.0000000E+00 -62405.75     -34765.38     0.0000000E+00 012101\n" +
                            "  -3293.618      6974.506     0.0000000E+00 0.0000000E+00  2713.380     003101\n" +
                            "  -858.7979     -777.1925     0.0000000E+00 0.0000000E+00 -381.7598     300002\n" +
                            "   856.1090     -1232.763     0.0000000E+00 0.0000000E+00 -403.4953     210002\n" +
                            "   1134.943      132.6262     0.0000000E+00 0.0000000E+00  422.0336     120002\n" +
                            "   411.8027      536.0182     0.0000000E+00 0.0000000E+00  232.1183     030002\n" +
                            "  0.0000000E+00 0.0000000E+00  935.0290     -1148.719     0.0000000E+00 201002\n" +
                            "  0.0000000E+00 0.0000000E+00  3856.895      197.5799     0.0000000E+00 111002\n" +
                            "  0.0000000E+00 0.0000000E+00 -1001.325     -1339.492     0.0000000E+00 021002\n" +
                            "  -16112.78     -4936.678     0.0000000E+00 0.0000000E+00 -1468.759     102002\n" +
                            "  -7126.358     -2615.991     0.0000000E+00 0.0000000E+00 -916.3055     012002\n" +
                            "  0.0000000E+00 0.0000000E+00  371.2409      536.7394     0.0000000E+00 003002\n" +
                            "  0.0000000E+00 0.0000000E+00  309694.5      187762.0     0.0000000E+00 200300\n" +
                            "  0.0000000E+00 0.0000000E+00  655125.0      379032.9     0.0000000E+00 110300\n" +
                            "  0.0000000E+00 0.0000000E+00  264725.2      193932.6     0.0000000E+00 020300\n" +
                            "   1138214.      387603.4     0.0000000E+00 0.0000000E+00  51673.23     101300\n" +
                            "   306787.0      31867.20     0.0000000E+00 0.0000000E+00  39451.00     011300\n" +
                            "  0.0000000E+00 0.0000000E+00 -57710.78     -199521.0     0.0000000E+00 002300\n" +
                            "   210374.1      43610.22     0.0000000E+00 0.0000000E+00  24047.06     200201\n" +
                            "   214473.6      50178.10     0.0000000E+00 0.0000000E+00  25876.37     110201\n" +
                            "   84022.88      26283.00     0.0000000E+00 0.0000000E+00  11004.25     020201\n" +
                            "  0.0000000E+00 0.0000000E+00 -95065.52     -83775.89     0.0000000E+00 101201\n" +
                            "  0.0000000E+00 0.0000000E+00  4740.763     -28653.71     0.0000000E+00 011201\n" +
                            "  -19514.89      17814.90     0.0000000E+00 0.0000000E+00  5910.406     002201\n" +
                            "  0.0000000E+00 0.0000000E+00  25605.38      9495.849     0.0000000E+00 200102\n" +
                            "  0.0000000E+00 0.0000000E+00  20366.87      7447.307     0.0000000E+00 110102\n" +
                            "  0.0000000E+00 0.0000000E+00  2271.753      2205.847     0.0000000E+00 020102\n" +
                            "  -22757.78     -7299.670     0.0000000E+00 0.0000000E+00 -3545.028     101102\n" +
                            "  -8246.798     -3370.550     0.0000000E+00 0.0000000E+00 -1393.158     011102\n" +
                            "  0.0000000E+00 0.0000000E+00  4011.778      212.6521     0.0000000E+00 002102\n" +
                            "  -239.8164      42.93402     0.0000000E+00 0.0000000E+00  77.01081     200003\n" +
                            "  -83.14475      59.45467     0.0000000E+00 0.0000000E+00  54.88567     110003\n" +
                            "   104.2765      25.97647     0.0000000E+00 0.0000000E+00 -1.675278     020003\n" +
                            "  0.0000000E+00 0.0000000E+00 -655.0510     -753.9361     0.0000000E+00 101003\n" +
                            "  0.0000000E+00 0.0000000E+00 -414.2486     -266.2238     0.0000000E+00 011003\n" +
                            "   603.9015      237.7388     0.0000000E+00 0.0000000E+00  7.717392     002003\n" +
                            "   524516.9      189231.5     0.0000000E+00 0.0000000E+00  49444.20     100400\n" +
                            "   162186.5      4302.691     0.0000000E+00 0.0000000E+00  29475.16     010400\n" +
                            "  0.0000000E+00 0.0000000E+00 -24174.47     -111923.8     0.0000000E+00 001400\n" +
                            "  0.0000000E+00 0.0000000E+00  53160.72      17466.83     0.0000000E+00 100301\n" +
                            "  0.0000000E+00 0.0000000E+00  41118.21      13296.00     0.0000000E+00 010301\n" +
                            "  -15425.06      26542.71     0.0000000E+00 0.0000000E+00  8715.126     001301\n" +
                            "   6604.432      1754.657     0.0000000E+00 0.0000000E+00  195.4265     100202\n" +
                            "   625.2787      547.8311     0.0000000E+00 0.0000000E+00  520.6480     010202\n" +
                            "  0.0000000E+00 0.0000000E+00 -676.8423     -6589.112     0.0000000E+00 001202\n" +
                            "  0.0000000E+00 0.0000000E+00 -1204.814     -458.6519     0.0000000E+00 100103\n" +
                            "  0.0000000E+00 0.0000000E+00 -575.8461     -209.3115     0.0000000E+00 010103\n" +
                            "  -727.2853     -44.67296     0.0000000E+00 0.0000000E+00 -17.81994     001103\n" +
                            "   10.94512     -4.455945     0.0000000E+00 0.0000000E+00 -6.412213     100004\n" +
                            "   3.662316     -2.278804     0.0000000E+00 0.0000000E+00 -2.859713     010004\n" +
                            "  0.0000000E+00 0.0000000E+00 -48.35760     -28.27328     0.0000000E+00 001004\n" +
                            "  0.0000000E+00 0.0000000E+00 -23492.97      2088.657     0.0000000E+00 000500\n" +
                            "   3557.700      13456.98     0.0000000E+00 0.0000000E+00  6442.822     000401\n" +
                            "  0.0000000E+00 0.0000000E+00 -883.1863     -3887.335     0.0000000E+00 000302\n" +
                            "  -1115.623     -369.8261     0.0000000E+00 0.0000000E+00  46.68054     000203\n" +
                            "  0.0000000E+00 0.0000000E+00  69.26004     -37.76118     0.0000000E+00 000104\n" +
                            " -0.7971318    -0.1188820     0.0000000E+00 0.0000000E+00  1.923537     000005"
            );

            beforeQS3WithPart1.map = CosyArbitraryOrder.readMap(
                    "  0.9476600     -9.626604     0.0000000E+00 0.0000000E+00 -2.626454     100000\n" +
                            "  0.4602665     -3.620290     0.0000000E+00 0.0000000E+00-0.9842866     010000\n" +
                            "  0.0000000E+00 0.0000000E+00 -1.487866    -0.1068188     0.0000000E+00 001000\n" +
                            "  0.0000000E+00 0.0000000E+00 -1.751707    -0.7978641     0.0000000E+00 000100\n" +
                            "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00  1.000000     000010\n" +
                            "  0.2760997    -0.3318547E-01 0.0000000E+00 0.0000000E+00  1.438152     000001\n" +
                            "  -23.24870     -67.12672     0.0000000E+00 0.0000000E+00 -19.61382     200000\n" +
                            "  -20.02545     -39.41220     0.0000000E+00 0.0000000E+00 -10.63193     110000\n" +
                            "  -9.202651      45.66115     0.0000000E+00 0.0000000E+00  12.56329     020000\n" +
                            "  0.0000000E+00 0.0000000E+00  20.11279      51.08815     0.0000000E+00 101000\n" +
                            "  0.0000000E+00 0.0000000E+00  11.37838      20.58481     0.0000000E+00 011000\n" +
                            "  -3.062245     -7.864574     0.0000000E+00 0.0000000E+00 -5.521324     002000\n" +
                            "  0.0000000E+00 0.0000000E+00  16.74212      50.56406     0.0000000E+00 100100\n" +
                            "  0.0000000E+00 0.0000000E+00  24.23313      19.87323     0.0000000E+00 010100\n" +
                            "  -8.235890      6.161972     0.0000000E+00 0.0000000E+00 -3.201663     001100\n" +
                            "   1.150573     -11.03649     0.0000000E+00 0.0000000E+00-0.6831106     100001\n" +
                            "  0.7508746E-01 -1.727579     0.0000000E+00 0.0000000E+00 0.4898083     010001\n" +
                            "  0.0000000E+00 0.0000000E+00 -4.491797     -4.688862     0.0000000E+00 001001\n" +
                            "  -9.975961      61.65384     0.0000000E+00 0.0000000E+00  14.29420     000200\n" +
                            "  0.0000000E+00 0.0000000E+00-0.9759514     -3.181683     0.0000000E+00 000101\n" +
                            " -0.7834740E-01-0.4312934     0.0000000E+00 0.0000000E+00 -1.386480     000002\n" +
                            "  -420.5667      596.5778     0.0000000E+00 0.0000000E+00 -3.615176     300000\n" +
                            "  -469.5103      1721.868     0.0000000E+00 0.0000000E+00  289.1139     210000\n" +
                            "   37.55296      2205.285     0.0000000E+00 0.0000000E+00  548.8572     120000\n" +
                            "   57.13775      722.8357     0.0000000E+00 0.0000000E+00  189.7423     030000\n" +
                            "  0.0000000E+00 0.0000000E+00  585.6150      1101.367     0.0000000E+00 201000\n" +
                            "  0.0000000E+00 0.0000000E+00  386.3395      645.7902     0.0000000E+00 111000\n" +
                            "  0.0000000E+00 0.0000000E+00 -70.55218     -175.5648     0.0000000E+00 021000\n" +
                            "  -154.6646     -44.11142     0.0000000E+00 0.0000000E+00 -108.0208     102000\n" +
                            "  -28.28266     -148.4554     0.0000000E+00 0.0000000E+00 -68.01260     012000\n" +
                            "  0.0000000E+00 0.0000000E+00  57.00864      152.5691     0.0000000E+00 003000\n" +
                            "  0.0000000E+00 0.0000000E+00  455.0272      1123.956     0.0000000E+00 200100\n" +
                            "  0.0000000E+00 0.0000000E+00  26.73269      146.6723     0.0000000E+00 110100\n" +
                            "  0.0000000E+00 0.0000000E+00 -164.0788     -363.9324     0.0000000E+00 020100\n" +
                            "  -205.4667      300.1454     0.0000000E+00 0.0000000E+00 -93.02044     101100\n" +
                            "   83.89990     -363.3684     0.0000000E+00 0.0000000E+00 -90.85722     011100\n" +
                            "  0.0000000E+00 0.0000000E+00  5.060191      335.0919     0.0000000E+00 002100\n" +
                            "  -3.493067     -180.3282     0.0000000E+00 0.0000000E+00 -27.21994     200001\n" +
                            "   5.331412     -70.34665     0.0000000E+00 0.0000000E+00 -6.610042     110001\n" +
                            "   1.204065      12.79096     0.0000000E+00 0.0000000E+00 -7.538336     020001\n" +
                            "  0.0000000E+00 0.0000000E+00 -25.17053      24.70326     0.0000000E+00 101001\n" +
                            "  0.0000000E+00 0.0000000E+00  3.542331    -0.6183011     0.0000000E+00 011001\n" +
                            "  -8.120752      35.57477     0.0000000E+00 0.0000000E+00  11.38451     002001\n" +
                            "   188.7680      826.3126     0.0000000E+00 0.0000000E+00  182.9008     100200\n" +
                            "   208.3353     -51.95665     0.0000000E+00 0.0000000E+00  15.06763     010200\n" +
                            "  0.0000000E+00 0.0000000E+00 -416.9948      2.615179     0.0000000E+00 001200\n" +
                            "  0.0000000E+00 0.0000000E+00 -131.5484     -76.25099     0.0000000E+00 100101\n" +
                            "  0.0000000E+00 0.0000000E+00 -11.00239      2.125189     0.0000000E+00 010101\n" +
                            "  -16.10167      200.0584     0.0000000E+00 0.0000000E+00  57.54803     001101\n" +
                            "  -3.034935     -3.169941     0.0000000E+00 0.0000000E+00 -1.002447     100002\n" +
                            "  -1.420592      2.520012     0.0000000E+00 0.0000000E+00-0.8727265E-01 010002\n" +
                            "  0.0000000E+00 0.0000000E+00  4.783947      2.521695     0.0000000E+00 001002\n" +
                            "  0.0000000E+00 0.0000000E+00 -518.3596     -161.6253     0.0000000E+00 000300\n" +
                            "  -7.853152      138.2642     0.0000000E+00 0.0000000E+00  32.73431     000201\n" +
                            "  0.0000000E+00 0.0000000E+00 -1.400429      6.832679     0.0000000E+00 000102\n" +
                            "  0.8322284E-01 0.1213094     0.0000000E+00 0.0000000E+00  1.288096     000003\n" +
                            "  -1079.280      12971.86     0.0000000E+00 0.0000000E+00  441.5053     400000\n" +
                            "   4837.131      20278.19     0.0000000E+00 0.0000000E+00  1943.080     310000\n" +
                            "   15401.54      4378.447     0.0000000E+00 0.0000000E+00  2533.462     220000\n" +
                            "   9800.345     -8658.557     0.0000000E+00 0.0000000E+00 -541.9537     130000\n" +
                            "   1225.730     -6081.403     0.0000000E+00 0.0000000E+00 -1343.262     040000\n" +
                            "  0.0000000E+00 0.0000000E+00  7313.353      11590.85     0.0000000E+00 301000\n" +
                            "  0.0000000E+00 0.0000000E+00  3282.523      2758.412     0.0000000E+00 211000\n" +
                            "  0.0000000E+00 0.0000000E+00 -7317.464     -15539.28     0.0000000E+00 121000\n" +
                            "  0.0000000E+00 0.0000000E+00 -3003.252     -5486.981     0.0000000E+00 031000\n" +
                            "  -2872.116      2510.106     0.0000000E+00 0.0000000E+00 -2055.587     202000\n" +
                            "  -1875.317     -1105.420     0.0000000E+00 0.0000000E+00 -1810.028     112000\n" +
                            "   319.2247     -589.5099     0.0000000E+00 0.0000000E+00  35.45658     022000\n" +
                            "  0.0000000E+00 0.0000000E+00  3754.763      6766.116     0.0000000E+00 103000\n" +
                            "  0.0000000E+00 0.0000000E+00  1427.094      2455.823     0.0000000E+00 013000\n" +
                            "  -295.6460     -240.7746     0.0000000E+00 0.0000000E+00 -289.2101     004000\n" +
                            "  0.0000000E+00 0.0000000E+00  11274.64      17612.00     0.0000000E+00 300100\n" +
                            "  0.0000000E+00 0.0000000E+00  4622.611      930.6059     0.0000000E+00 210100\n" +
                            "  0.0000000E+00 0.0000000E+00 -5681.814     -19092.60     0.0000000E+00 120100\n" +
                            "  0.0000000E+00 0.0000000E+00 -822.6854     -3504.564     0.0000000E+00 030100\n" +
                            "  -3120.083     -525.8562     0.0000000E+00 0.0000000E+00 -5173.462     201100\n" +
                            "  -556.2396     -16780.81     0.0000000E+00 0.0000000E+00 -5791.538     111100\n" +
                            "   856.3098     -9422.887     0.0000000E+00 0.0000000E+00 -1659.972     021100\n" +
                            "  0.0000000E+00 0.0000000E+00  9539.174      18383.05     0.0000000E+00 102100\n" +
                            "  0.0000000E+00 0.0000000E+00  3162.084      4432.234     0.0000000E+00 012100\n" +
                            "  -1265.113     -1944.270     0.0000000E+00 0.0000000E+00 -1589.094     003100\n" +
                            "  -681.3826     -849.6079     0.0000000E+00 0.0000000E+00 -327.5016     300001\n" +
                            "  -476.8326      139.6224     0.0000000E+00 0.0000000E+00 -244.4795     210001\n" +
                            "  -258.7007      1335.783     0.0000000E+00 0.0000000E+00 -163.2663     120001\n" +
                            "  -142.5473      303.8639     0.0000000E+00 0.0000000E+00 -95.37805     030001\n" +
                            "  0.0000000E+00 0.0000000E+00  1058.835      2255.164     0.0000000E+00 201001\n" +
                            "  0.0000000E+00 0.0000000E+00  453.6975      591.4306     0.0000000E+00 111001\n" +
                            "  0.0000000E+00 0.0000000E+00  220.1500     -52.66615     0.0000000E+00 021001\n" +
                            "  -48.47153      606.3821     0.0000000E+00 0.0000000E+00  67.67170     102001\n" +
                            "   153.6857     -180.2247     0.0000000E+00 0.0000000E+00  44.73045     012001\n" +
                            "  0.0000000E+00 0.0000000E+00 -240.1634      20.87793     0.0000000E+00 003001\n" +
                            "   4890.103     -9434.356     0.0000000E+00 0.0000000E+00 -2625.388     200200\n" +
                            "   4979.225     -26823.16     0.0000000E+00 0.0000000E+00 -5399.952     110200\n" +
                            "  -374.9658     -15924.94     0.0000000E+00 0.0000000E+00 -3883.100     020200\n" +
                            "  0.0000000E+00 0.0000000E+00  7795.596      13026.87     0.0000000E+00 101200\n" +
                            "  0.0000000E+00 0.0000000E+00  5139.341      2372.187     0.0000000E+00 011200\n" +
                            "  -2189.792     -6200.083     0.0000000E+00 0.0000000E+00 -3705.887     002200\n" +
                            "  0.0000000E+00 0.0000000E+00  1188.214      2036.306     0.0000000E+00 200101\n" +
                            "  0.0000000E+00 0.0000000E+00  1011.328      796.6820     0.0000000E+00 110101\n" +
                            "  0.0000000E+00 0.0000000E+00  1172.222      747.2808     0.0000000E+00 020101\n" +
                            "   567.6366      2954.732     0.0000000E+00 0.0000000E+00  593.1631     101101\n" +
                            "   487.3274      429.6245     0.0000000E+00 0.0000000E+00  256.0424     011101\n" +
                            "  0.0000000E+00 0.0000000E+00 -2033.627     -1414.219     0.0000000E+00 002101\n" +
                            "  -62.24853      166.0599     0.0000000E+00 0.0000000E+00  36.47056     200002\n" +
                            "  -37.86087      317.9709     0.0000000E+00 0.0000000E+00  68.26878     110002\n" +
                            "   12.23740      86.28940     0.0000000E+00 0.0000000E+00  29.81664     020002\n" +
                            "  0.0000000E+00 0.0000000E+00 -98.20820     -104.4762     0.0000000E+00 101002\n" +
                            "  0.0000000E+00 0.0000000E+00 0.6022503     -3.774267     0.0000000E+00 011002\n" +
                            "   3.323739      60.53265     0.0000000E+00 0.0000000E+00-0.7593625     002002\n" +
                            "  0.0000000E+00 0.0000000E+00  8322.409      7291.695     0.0000000E+00 100300\n" +
                            "  0.0000000E+00 0.0000000E+00  6989.848      4888.929     0.0000000E+00 010300\n" +
                            "  -3066.641     -10062.76     0.0000000E+00 0.0000000E+00 -5050.838     001300\n" +
                            "   344.3564      3054.523     0.0000000E+00 0.0000000E+00  381.3354     100201\n" +
                            "  -50.59966      1543.697     0.0000000E+00 0.0000000E+00  341.3348     010201\n" +
                            "  0.0000000E+00 0.0000000E+00 -3876.170     -2813.908     0.0000000E+00 001201\n" +
                            "  0.0000000E+00 0.0000000E+00  169.8287      102.1722     0.0000000E+00 100102\n" +
                            "  0.0000000E+00 0.0000000E+00  30.82499      42.83213     0.0000000E+00 010102\n" +
                            "  -17.22992     -53.08889     0.0000000E+00 0.0000000E+00 -82.39157     001102\n" +
                            "   3.816984     -8.644536     0.0000000E+00 0.0000000E+00-0.7939091     100003\n" +
                            "   1.993400     -2.165440     0.0000000E+00 0.0000000E+00-0.8367213E-02 010003\n" +
                            "  0.0000000E+00 0.0000000E+00 -16.37919    -0.5496090     0.0000000E+00 001003\n" +
                            "  -2308.575     -7258.438     0.0000000E+00 0.0000000E+00 -3083.681     000400\n" +
                            "  0.0000000E+00 0.0000000E+00 -1408.336     -1399.469     0.0000000E+00 000301\n" +
                            "   6.441810     -213.2516     0.0000000E+00 0.0000000E+00 -113.4653     000202\n" +
                            "  0.0000000E+00 0.0000000E+00 -15.87573     -26.62039     0.0000000E+00 000103\n" +
                            " -0.1615133    -0.1430493     0.0000000E+00 0.0000000E+00 -1.299586     000004\n" +
                            "   26489.14      120654.8     0.0000000E+00 0.0000000E+00  19174.54     500000\n" +
                            "   120928.3      10001.18     0.0000000E+00 0.0000000E+00  37752.27     410000\n" +
                            "   190700.5     -486800.8     0.0000000E+00 0.0000000E+00  24886.90     320000\n" +
                            "   54433.06     -502964.0     0.0000000E+00 0.0000000E+00 -4491.484     230000\n" +
                            "  -71694.95     -148882.5     0.0000000E+00 0.0000000E+00 -14097.23     140000\n" +
                            "  -29881.22     -1945.978     0.0000000E+00 0.0000000E+00 -1800.091     050000\n" +
                            "  0.0000000E+00 0.0000000E+00  28866.32      21285.10     0.0000000E+00 401000\n" +
                            "  0.0000000E+00 0.0000000E+00 -90652.72     -200208.2     0.0000000E+00 311000\n" +
                            "  0.0000000E+00 0.0000000E+00 -265579.1     -464659.9     0.0000000E+00 221000\n" +
                            "  0.0000000E+00 0.0000000E+00 -126010.3     -193309.1     0.0000000E+00 131000\n" +
                            "  0.0000000E+00 0.0000000E+00  1282.341      14095.48     0.0000000E+00 041000\n" +
                            "  -31487.01      86989.56     0.0000000E+00 0.0000000E+00 -25456.96     302000\n" +
                            "  -23827.71      85407.03     0.0000000E+00 0.0000000E+00 -11045.25     212000\n" +
                            "   25998.62      23584.54     0.0000000E+00 0.0000000E+00  29387.13     122000\n" +
                            "   12189.42      19678.66     0.0000000E+00 0.0000000E+00  14566.83     032000\n" +
                            "  0.0000000E+00 0.0000000E+00  95884.85      154057.7     0.0000000E+00 203000\n" +
                            "  0.0000000E+00 0.0000000E+00  65661.54      101408.1     0.0000000E+00 113000\n" +
                            "  0.0000000E+00 0.0000000E+00 -4714.909     -12033.43     0.0000000E+00 023000\n" +
                            "  -12931.48      1319.526     0.0000000E+00 0.0000000E+00 -10184.15     104000\n" +
                            "  -4881.097      2403.097     0.0000000E+00 0.0000000E+00 -3169.922     014000\n" +
                            "  0.0000000E+00 0.0000000E+00  7780.704      11991.71     0.0000000E+00 005000\n" +
                            "  0.0000000E+00 0.0000000E+00  69693.81      89198.68     0.0000000E+00 400100\n" +
                            "  0.0000000E+00 0.0000000E+00 -111101.8     -240813.4     0.0000000E+00 310100\n" +
                            "  0.0000000E+00 0.0000000E+00 -358066.6     -619997.0     0.0000000E+00 220100\n" +
                            "  0.0000000E+00 0.0000000E+00 -127112.2     -167720.6     0.0000000E+00 130100\n" +
                            "  0.0000000E+00 0.0000000E+00  9515.620      48320.99     0.0000000E+00 040100\n" +
                            "  -74855.25      51386.58     0.0000000E+00 0.0000000E+00 -67605.66     301100\n" +
                            "  -100304.6      15088.33     0.0000000E+00 0.0000000E+00 -17011.29     211100\n" +
                            "  -29299.11      76564.43     0.0000000E+00 0.0000000E+00  86005.06     121100\n" +
                            "  -5719.450      118437.8     0.0000000E+00 0.0000000E+00  46387.12     031100\n" +
                            "  0.0000000E+00 0.0000000E+00  303266.5      502965.6     0.0000000E+00 202100\n" +
                            "  0.0000000E+00 0.0000000E+00  159018.2      263330.1     0.0000000E+00 112100\n" +
                            "  0.0000000E+00 0.0000000E+00 -21816.24     -41281.86     0.0000000E+00 022100\n" +
                            "  -62305.93      1850.877     0.0000000E+00 0.0000000E+00 -44220.51     103100\n" +
                            "  -14208.07      19802.16     0.0000000E+00 0.0000000E+00 -5053.218     013100\n" +
                            "  0.0000000E+00 0.0000000E+00  49316.51      74630.90     0.0000000E+00 004100\n" +
                            "  -10303.99      4785.224     0.0000000E+00 0.0000000E+00 -4665.861     400001\n" +
                            "  -9158.898      11205.22     0.0000000E+00 0.0000000E+00 -3690.197     310001\n" +
                            "  -2493.114      34690.61     0.0000000E+00 0.0000000E+00  4494.929     220001\n" +
                            "  -3808.124      25703.76     0.0000000E+00 0.0000000E+00  5399.150     130001\n" +
                            "  -586.4370      6770.131     0.0000000E+00 0.0000000E+00  2717.512     040001\n" +
                            "  0.0000000E+00 0.0000000E+00  26646.06      43545.46     0.0000000E+00 301001\n" +
                            "  0.0000000E+00 0.0000000E+00  13500.95      14389.24     0.0000000E+00 211001\n" +
                            "  0.0000000E+00 0.0000000E+00 -3784.836     -16839.34     0.0000000E+00 121001\n" +
                            "  0.0000000E+00 0.0000000E+00 -659.5430     -2101.012     0.0000000E+00 031001\n" +
                            "   639.2693     -10684.86     0.0000000E+00 0.0000000E+00 -4227.657     202001\n" +
                            "   4707.721     -31430.45     0.0000000E+00 0.0000000E+00 -4757.290     112001\n" +
                            "   1435.705     -16694.15     0.0000000E+00 0.0000000E+00 -2880.106     022001\n" +
                            "  0.0000000E+00 0.0000000E+00  8509.909      16929.49     0.0000000E+00 103001\n" +
                            "  0.0000000E+00 0.0000000E+00  2243.544      2177.573     0.0000000E+00 013001\n" +
                            "  -923.4307     -640.1640     0.0000000E+00 0.0000000E+00 -568.4322     004001\n" +
                            "  -19292.02     -271240.0     0.0000000E+00 0.0000000E+00 -53351.26     300200\n" +
                            "  -113174.2     -316218.5     0.0000000E+00 0.0000000E+00 -20636.43     210200\n" +
                            "  -177659.3      70072.71     0.0000000E+00 0.0000000E+00  45546.95     120200\n" +
                            "  -60393.50      151401.2     0.0000000E+00 0.0000000E+00  34779.55     030200\n" +
                            "  0.0000000E+00 0.0000000E+00  215795.0      407381.8     0.0000000E+00 201200\n" +
                            "  0.0000000E+00 0.0000000E+00  43457.73      159203.3     0.0000000E+00 111200\n" +
                            "  0.0000000E+00 0.0000000E+00 -26263.78     -11629.14     0.0000000E+00 021200\n" +
                            "  -116308.1      1316.738     0.0000000E+00 0.0000000E+00 -64857.18     102200\n" +
                            "  -1819.532      83565.66     0.0000000E+00 0.0000000E+00  23916.61     012200\n" +
                            "  0.0000000E+00 0.0000000E+00  135371.8      192045.5     0.0000000E+00 003200\n" +
                            "  0.0000000E+00 0.0000000E+00  31717.57      51710.96     0.0000000E+00 300101\n" +
                            "  0.0000000E+00 0.0000000E+00  21825.99      25387.92     0.0000000E+00 210101\n" +
                            "  0.0000000E+00 0.0000000E+00  623.3400     -3446.412     0.0000000E+00 120101\n" +
                            "  0.0000000E+00 0.0000000E+00 -1319.442     -442.4566     0.0000000E+00 030101\n" +
                            "   16226.30     -30108.55     0.0000000E+00 0.0000000E+00 -2403.845     201101\n" +
                            "   22801.88     -92943.21     0.0000000E+00 0.0000000E+00 -8642.890     111101\n" +
                            "   3635.692     -56149.89     0.0000000E+00 0.0000000E+00 -10179.30     021101\n" +
                            "  0.0000000E+00 0.0000000E+00  21283.98      27045.68     0.0000000E+00 102101\n" +
                            "  0.0000000E+00 0.0000000E+00  11362.20      61.46124     0.0000000E+00 012101\n" +
                            "  -3899.201     -3926.824     0.0000000E+00 0.0000000E+00 -3097.870     003101\n" +
                            "   218.7003      1485.017     0.0000000E+00 0.0000000E+00  144.1727     300002\n" +
                            "   1875.236      1055.011     0.0000000E+00 0.0000000E+00  248.8762     210002\n" +
                            "   2207.738     -3703.762     0.0000000E+00 0.0000000E+00 -403.3203     120002\n" +
                            "   684.7533     -2659.954     0.0000000E+00 0.0000000E+00 -463.2372     030002\n" +
                            "  0.0000000E+00 0.0000000E+00  1005.507      635.6830     0.0000000E+00 201002\n" +
                            "  0.0000000E+00 0.0000000E+00  20.26125     -952.1009     0.0000000E+00 111002\n" +
                            "  0.0000000E+00 0.0000000E+00  683.6409      466.7252     0.0000000E+00 021002\n" +
                            "  -28.13445      1902.379     0.0000000E+00 0.0000000E+00  151.2154     102002\n" +
                            "  -66.04076      1039.901     0.0000000E+00 0.0000000E+00  179.7351     012002\n" +
                            "  0.0000000E+00 0.0000000E+00 -616.5606     -813.4502     0.0000000E+00 003002\n" +
                            "  0.0000000E+00 0.0000000E+00 -32152.75      17380.60     0.0000000E+00 200300\n" +
                            "  0.0000000E+00 0.0000000E+00 -50427.18      33824.23     0.0000000E+00 110300\n" +
                            "  0.0000000E+00 0.0000000E+00 -45454.45      1452.098     0.0000000E+00 020300\n" +
                            "  -139259.2      55218.09     0.0000000E+00 0.0000000E+00 -43706.99     101300\n" +
                            "   7272.984      202970.8     0.0000000E+00 0.0000000E+00  68157.42     011300\n" +
                            "  0.0000000E+00 0.0000000E+00  213869.4      281750.9     0.0000000E+00 002300\n" +
                            "   16901.80      57.52004     0.0000000E+00 0.0000000E+00  5726.377     200201\n" +
                            "   21475.68     -42109.98     0.0000000E+00 0.0000000E+00  965.8874     110201\n" +
                            "   5817.860     -28375.50     0.0000000E+00 0.0000000E+00 -1330.635     020201\n" +
                            "  0.0000000E+00 0.0000000E+00  41986.07      26369.98     0.0000000E+00 101201\n" +
                            "  0.0000000E+00 0.0000000E+00  24925.16      18457.53     0.0000000E+00 011201\n" +
                            "  -11210.23     -17177.90     0.0000000E+00 0.0000000E+00 -10822.28     002201\n" +
                            "  0.0000000E+00 0.0000000E+00  1140.275      533.6995     0.0000000E+00 200102\n" +
                            "  0.0000000E+00 0.0000000E+00  812.7970      250.2084     0.0000000E+00 110102\n" +
                            "  0.0000000E+00 0.0000000E+00 -1228.559     -382.7145     0.0000000E+00 020102\n" +
                            "  -1281.556      4865.838     0.0000000E+00 0.0000000E+00 -555.1128     101102\n" +
                            "  -1532.451      3934.452     0.0000000E+00 0.0000000E+00  523.6152     011102\n" +
                            "  0.0000000E+00 0.0000000E+00 -521.1169     -870.8828     0.0000000E+00 002102\n" +
                            "  -3.483008     -236.5540     0.0000000E+00 0.0000000E+00 -74.69569     200003\n" +
                            "   12.55237     -260.8090     0.0000000E+00 0.0000000E+00 -104.7948     110003\n" +
                            "  -39.10469      6.957029     0.0000000E+00 0.0000000E+00 -23.85008     020003\n" +
                            "  0.0000000E+00 0.0000000E+00  618.9443      582.6499     0.0000000E+00 101003\n" +
                            "  0.0000000E+00 0.0000000E+00  112.7002      182.7283     0.0000000E+00 011003\n" +
                            "  -55.59833     -104.4039     0.0000000E+00 0.0000000E+00 -47.88601     002003\n" +
                            "  -100435.1      94571.35     0.0000000E+00 0.0000000E+00 -13350.58     100400\n" +
                            "  -1192.270      164880.7     0.0000000E+00 0.0000000E+00  52460.24     010400\n" +
                            "  0.0000000E+00 0.0000000E+00  169716.2      202350.2     0.0000000E+00 001400\n" +
                            "  0.0000000E+00 0.0000000E+00  7199.758      1601.783     0.0000000E+00 100301\n" +
                            "  0.0000000E+00 0.0000000E+00  4985.653      8723.464     0.0000000E+00 010301\n" +
                            "  -11284.80     -38388.52     0.0000000E+00 0.0000000E+00 -15584.79     001301\n" +
                            "  -1130.270     -62.89569     0.0000000E+00 0.0000000E+00 -547.9360     100202\n" +
                            "  -491.4314     -1255.900     0.0000000E+00 0.0000000E+00 -425.6789     010202\n" +
                            "  0.0000000E+00 0.0000000E+00  5505.645      1303.321     0.0000000E+00 001202\n" +
                            "  0.0000000E+00 0.0000000E+00  215.5181      258.0215     0.0000000E+00 100103\n" +
                            "  0.0000000E+00 0.0000000E+00  47.13540     -30.44256     0.0000000E+00 010103\n" +
                            "   26.99781     -188.1920     0.0000000E+00 0.0000000E+00 -38.36501     001103\n" +
                            "  -8.428342      24.24836     0.0000000E+00 0.0000000E+00  4.720539     100004\n" +
                            "  -3.026388      8.831420     0.0000000E+00 0.0000000E+00  1.227052     010004\n" +
                            "  0.0000000E+00 0.0000000E+00  23.98735     -31.91461     0.0000000E+00 001004\n" +
                            "  0.0000000E+00 0.0000000E+00  32133.34      21450.33     0.0000000E+00 000500\n" +
                            "  -97.63897     -25260.69     0.0000000E+00 0.0000000E+00 -5991.688     000401\n" +
                            "  0.0000000E+00 0.0000000E+00  4024.639      1234.374     0.0000000E+00 000302\n" +
                            "   66.24876      249.5788     0.0000000E+00 0.0000000E+00  191.9424     000203\n" +
                            "  0.0000000E+00 0.0000000E+00  52.67692      42.68738     0.0000000E+00 000104\n" +
                            "  0.2918890     0.8311784E-01 0.0000000E+00 0.0000000E+00  1.391062     000005"
            );

            part1.map = CosyArbitraryOrder.readMap(
                    "  0.8184267      1.746312     0.0000000E+00 0.0000000E+00-0.1109671E-02 100000\n" +
                            " -0.1890715     0.8184267     0.0000000E+00 0.0000000E+00 0.1153783E-03 010000\n" +
                            "  0.0000000E+00 0.0000000E+00-0.9946401    -0.6855043     0.0000000E+00 001000\n" +
                            "  0.0000000E+00 0.0000000E+00 0.1559595E-01-0.9946401     0.0000000E+00 000100\n" +
                            "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00  1.000000     000010\n" +
                            " -0.1153783E-03 0.1109671E-02 0.0000000E+00 0.0000000E+00 0.8289198     000001\n" +
                            "   17.39312      5.049923     0.0000000E+00 0.0000000E+00-0.4869064     200000\n" +
                            "   16.90427     -1.050135     0.0000000E+00 0.0000000E+00 0.2252698     110000\n" +
                            " -0.8788132     -10.20599     0.0000000E+00 0.0000000E+00 0.5937807E-01 020000\n" +
                            "  0.0000000E+00 0.0000000E+00 -14.88396     -4.574182     0.0000000E+00 101000\n" +
                            "  0.0000000E+00 0.0000000E+00 -5.712058     0.4756019     0.0000000E+00 011000\n" +
                            "   2.330356      1.518584     0.0000000E+00 0.0000000E+00-0.8892349     002000\n" +
                            "  0.0000000E+00 0.0000000E+00 -24.90152     -2.206399     0.0000000E+00 100100\n" +
                            "  0.0000000E+00 0.0000000E+00  2.589143      7.489034     0.0000000E+00 010100\n" +
                            "   7.456311     -2.265912     0.0000000E+00 0.0000000E+00 0.4793966E-01 001100\n" +
                            "  0.2301840E-01  1.190386     0.0000000E+00 0.0000000E+00-0.2381189     100001\n" +
                            "  0.1397853     0.2466495E-03 0.0000000E+00 0.0000000E+00 0.2475848E-01 010001\n" +
                            "  0.0000000E+00 0.0000000E+00-0.2800157E-01 -1.801800     0.0000000E+00 001001\n" +
                            "   1.243090     -12.50009     0.0000000E+00 0.0000000E+00 0.5520336E-01 000200\n" +
                            "  0.0000000E+00 0.0000000E+00-0.1105626    -0.1994578E-01 0.0000000E+00 000101\n" +
                            " -0.1230036E-01 0.1191282     0.0000000E+00 0.0000000E+00-0.7236049     000002\n" +
                            "   9.692919     -34.07860     0.0000000E+00 0.0000000E+00 -10.31145     300000\n" +
                            "  -202.4616     -184.2185     0.0000000E+00 0.0000000E+00 -10.26628     210000\n" +
                            "  -426.9733     -243.0831     0.0000000E+00 0.0000000E+00 0.3358878     120000\n" +
                            "  -173.8178     -68.55358     0.0000000E+00 0.0000000E+00-0.5801077     030000\n" +
                            "  0.0000000E+00 0.0000000E+00 -79.61479     -43.33373     0.0000000E+00 201000\n" +
                            "  0.0000000E+00 0.0000000E+00  23.03844     -17.47395     0.0000000E+00 111000\n" +
                            "  0.0000000E+00 0.0000000E+00  65.09414      13.99491     0.0000000E+00 021000\n" +
                            "   30.73345      15.76899     0.0000000E+00 0.0000000E+00 -10.63085     102000\n" +
                            "   10.65910      28.94140     0.0000000E+00 0.0000000E+00 -7.453961     012000\n" +
                            "  0.0000000E+00 0.0000000E+00 -42.50385     -7.744677     0.0000000E+00 003000\n" +
                            "  0.0000000E+00 0.0000000E+00 -169.1385     -117.7767     0.0000000E+00 200100\n" +
                            "  0.0000000E+00 0.0000000E+00  63.30382     -54.71769     0.0000000E+00 110100\n" +
                            "  0.0000000E+00 0.0000000E+00  157.6663    -0.8965831     0.0000000E+00 020100\n" +
                            "  -37.73252      11.69774     0.0000000E+00 0.0000000E+00 -15.19210     101100\n" +
                            "  -43.41016      93.99928     0.0000000E+00 0.0000000E+00 -4.922929     011100\n" +
                            "  0.0000000E+00 0.0000000E+00 -211.2609     -51.11054     0.0000000E+00 002100\n" +
                            "   9.825568      8.425699     0.0000000E+00 0.0000000E+00 -1.043232     200001\n" +
                            "  -1.907162     -1.282499     0.0000000E+00 0.0000000E+00 -1.173505     110001\n" +
                            "  0.4147007     0.9031871     0.0000000E+00 0.0000000E+00 0.7361609     020001\n" +
                            "  0.0000000E+00 0.0000000E+00 -38.46605     -15.47346     0.0000000E+00 101001\n" +
                            "  0.0000000E+00 0.0000000E+00  9.126241      1.267185     0.0000000E+00 011001\n" +
                            "   5.389118     -8.166685     0.0000000E+00 0.0000000E+00  3.130539     002001\n" +
                            "  -175.7429     -36.14794     0.0000000E+00 0.0000000E+00  23.27409     100200\n" +
                            "  -152.2537      80.10548     0.0000000E+00 0.0000000E+00 -3.651857     010200\n" +
                            "  0.0000000E+00 0.0000000E+00 -398.0275     -124.6267     0.0000000E+00 001200\n" +
                            "  0.0000000E+00 0.0000000E+00 -49.56828     -40.71092     0.0000000E+00 100101\n" +
                            "  0.0000000E+00 0.0000000E+00  3.403328     -2.153707     0.0000000E+00 010101\n" +
                            "  -2.017804     -40.18443     0.0000000E+00 0.0000000E+00  3.234582     001101\n" +
                            "   1.836511    -0.3638466E-01 0.0000000E+00 0.0000000E+00 0.3037416     100002\n" +
                            "  0.4775278    -0.6058761     0.0000000E+00 0.0000000E+00-0.4047861E-01 010002\n" +
                            "  0.0000000E+00 0.0000000E+00 -2.617916      1.943985     0.0000000E+00 001002\n" +
                            "  0.0000000E+00 0.0000000E+00 -342.6651     -163.4203     0.0000000E+00 000300\n" +
                            "   1.926501     -24.39237     0.0000000E+00 0.0000000E+00  6.025338     000201\n" +
                            "  0.0000000E+00 0.0000000E+00 -6.018133     -1.759977     0.0000000E+00 000102\n" +
                            "  0.1963244E-01-0.9680805E-01 0.0000000E+00 0.0000000E+00 0.6479229     000003\n" +
                            "  -27.46096      273.2328     0.0000000E+00 0.0000000E+00 -88.97688     400000\n" +
                            "  -1203.429      546.7172     0.0000000E+00 0.0000000E+00  15.20157     310000\n" +
                            "  -1939.754      979.6497     0.0000000E+00 0.0000000E+00  322.7610     220000\n" +
                            "   397.3867      1512.618     0.0000000E+00 0.0000000E+00  220.5648     130000\n" +
                            "   1064.059      781.2263     0.0000000E+00 0.0000000E+00  22.71245     040000\n" +
                            "  0.0000000E+00 0.0000000E+00  167.5404      63.95372     0.0000000E+00 301000\n" +
                            "  0.0000000E+00 0.0000000E+00  1453.659      782.2175     0.0000000E+00 211000\n" +
                            "  0.0000000E+00 0.0000000E+00  1947.051      1139.949     0.0000000E+00 121000\n" +
                            "  0.0000000E+00 0.0000000E+00  170.6182      279.6459     0.0000000E+00 031000\n" +
                            "   279.4699      225.7466     0.0000000E+00 0.0000000E+00 -201.3402     202000\n" +
                            "   705.8999      360.3887     0.0000000E+00 0.0000000E+00 -113.7631     112000\n" +
                            "   184.0442      3.707477     0.0000000E+00 0.0000000E+00  20.28481     022000\n" +
                            "  0.0000000E+00 0.0000000E+00 -138.3495     -162.8990     0.0000000E+00 103000\n" +
                            "  0.0000000E+00 0.0000000E+00 -25.57060     -82.86411     0.0000000E+00 013000\n" +
                            "  -8.150907      69.59977     0.0000000E+00 0.0000000E+00 -45.70114     004000\n" +
                            "  0.0000000E+00 0.0000000E+00  264.4046      89.99262     0.0000000E+00 300100\n" +
                            "  0.0000000E+00 0.0000000E+00  3291.968      1694.844     0.0000000E+00 210100\n" +
                            "  0.0000000E+00 0.0000000E+00  4771.206      2770.771     0.0000000E+00 120100\n" +
                            "  0.0000000E+00 0.0000000E+00  669.6260      669.9497     0.0000000E+00 030100\n" +
                            "   683.5546      1270.374     0.0000000E+00 0.0000000E+00 -599.8401     201100\n" +
                            "   3518.478      2296.381     0.0000000E+00 0.0000000E+00 -310.2776     111100\n" +
                            "   2243.695      729.2983     0.0000000E+00 0.0000000E+00  83.92303     021100\n" +
                            "  0.0000000E+00 0.0000000E+00 -788.6249     -832.7788     0.0000000E+00 102100\n" +
                            "  0.0000000E+00 0.0000000E+00  336.8235     -101.7834     0.0000000E+00 012100\n" +
                            "   98.88588      519.7447     0.0000000E+00 0.0000000E+00 -198.0280     003100\n" +
                            "  -81.01080     -20.03439     0.0000000E+00 0.0000000E+00 -13.33901     300001\n" +
                            "  -198.9843     -70.18060     0.0000000E+00 0.0000000E+00  3.627201     210001\n" +
                            "   113.8227      6.163725     0.0000000E+00 0.0000000E+00  29.14858     120001\n" +
                            "   158.7588      7.923333     0.0000000E+00 0.0000000E+00  18.18548     030001\n" +
                            "  0.0000000E+00 0.0000000E+00 -40.70846     -152.2666     0.0000000E+00 201001\n" +
                            "  0.0000000E+00 0.0000000E+00  254.9662      56.23586     0.0000000E+00 111001\n" +
                            "  0.0000000E+00 0.0000000E+00  268.1608      91.68640     0.0000000E+00 021001\n" +
                            "  -207.1110      4.636604     0.0000000E+00 0.0000000E+00 -20.20046     102001\n" +
                            "  -119.2243      71.40552     0.0000000E+00 0.0000000E+00  12.51531     012001\n" +
                            "  0.0000000E+00 0.0000000E+00 -131.8402     -99.04700     0.0000000E+00 003001\n" +
                            "  -228.1554      1218.002     0.0000000E+00 0.0000000E+00 -403.4867     200200\n" +
                            "   4006.873      2702.101     0.0000000E+00 0.0000000E+00 -146.8492     110200\n" +
                            "   3471.366      1380.594     0.0000000E+00 0.0000000E+00 -164.9222     020200\n" +
                            "  0.0000000E+00 0.0000000E+00  93.88311     -354.7342     0.0000000E+00 101200\n" +
                            "  0.0000000E+00 0.0000000E+00  2268.063      1176.347     0.0000000E+00 011200\n" +
                            "   890.9443      1373.632     0.0000000E+00 0.0000000E+00 -280.3095     002200\n" +
                            "  0.0000000E+00 0.0000000E+00  65.12015     -139.1352     0.0000000E+00 200101\n" +
                            "  0.0000000E+00 0.0000000E+00  370.7251      180.9778     0.0000000E+00 110101\n" +
                            "  0.0000000E+00 0.0000000E+00  362.5909      342.3457     0.0000000E+00 020101\n" +
                            "  -790.4271     -43.91728     0.0000000E+00 0.0000000E+00 -6.202705     101101\n" +
                            "  -556.7121      120.6548     0.0000000E+00 0.0000000E+00 -35.39298     011101\n" +
                            "  0.0000000E+00 0.0000000E+00 -673.1689     -529.3021     0.0000000E+00 002101\n" +
                            "  -25.61529     -15.55300     0.0000000E+00 0.0000000E+00 -1.455105     200002\n" +
                            "  -55.24977     -26.08781     0.0000000E+00 0.0000000E+00 0.3654690     110002\n" +
                            "  -30.85512     -5.838548     0.0000000E+00 0.0000000E+00 -2.047505     020002\n" +
                            "  0.0000000E+00 0.0000000E+00  15.11448     -42.64016     0.0000000E+00 101002\n" +
                            "  0.0000000E+00 0.0000000E+00  1.029978      3.376676     0.0000000E+00 011002\n" +
                            "  -11.90007     -5.679672     0.0000000E+00 0.0000000E+00 -9.300710     002002\n" +
                            "  0.0000000E+00 0.0000000E+00  2250.517      1504.300     0.0000000E+00 100300\n" +
                            "  0.0000000E+00 0.0000000E+00  2255.121      1458.062     0.0000000E+00 010300\n" +
                            "   2428.924      1704.766     0.0000000E+00 0.0000000E+00 -73.12463     001300\n" +
                            "  -579.1571     -15.81601     0.0000000E+00 0.0000000E+00 -108.2017     100201\n" +
                            "  -329.3635     -99.70963     0.0000000E+00 0.0000000E+00 -20.54934     010201\n" +
                            "  0.0000000E+00 0.0000000E+00 -1277.137     -1152.922     0.0000000E+00 001201\n" +
                            "  0.0000000E+00 0.0000000E+00  72.78392    -0.7427346     0.0000000E+00 100102\n" +
                            "  0.0000000E+00 0.0000000E+00 0.9748285     -6.014196     0.0000000E+00 010102\n" +
                            "   4.266028      32.38838     0.0000000E+00 0.0000000E+00-0.5621338     001102\n" +
                            "  -1.895066     0.3382776     0.0000000E+00 0.0000000E+00-0.6169000     100003\n" +
                            "  -1.016295     0.3532332     0.0000000E+00 0.0000000E+00 0.7871267E-01 010003\n" +
                            "  0.0000000E+00 0.0000000E+00 -1.938849     -8.966089     0.0000000E+00 001003\n" +
                            "   2164.807      904.1983     0.0000000E+00 0.0000000E+00  82.33128     000400\n" +
                            "  0.0000000E+00 0.0000000E+00 -287.6438     -453.1947     0.0000000E+00 000301\n" +
                            " -0.1234913      58.92268     0.0000000E+00 0.0000000E+00 -11.28549     000202\n" +
                            "  0.0000000E+00 0.0000000E+00  7.430874     -3.382987     0.0000000E+00 000103\n" +
                            "  0.1555580E-01 0.1123360     0.0000000E+00 0.0000000E+00-0.6113525     000004\n" +
                            "   1716.190      2415.364     0.0000000E+00 0.0000000E+00 -216.1982     500000\n" +
                            "  -1266.763      2106.983     0.0000000E+00 0.0000000E+00  1943.125     410000\n" +
                            "  -6053.793     -7755.786     0.0000000E+00 0.0000000E+00  5748.165     320000\n" +
                            "   4851.921     -12256.29     0.0000000E+00 0.0000000E+00  2957.958     230000\n" +
                            "   9712.562     -8228.982     0.0000000E+00 0.0000000E+00 -1276.896     140000\n" +
                            "   636.5841     -3355.444     0.0000000E+00 0.0000000E+00 -729.8153     050000\n" +
                            "  0.0000000E+00 0.0000000E+00  2078.022      2252.844     0.0000000E+00 401000\n" +
                            "  0.0000000E+00 0.0000000E+00  8496.816      5930.656     0.0000000E+00 311000\n" +
                            "  0.0000000E+00 0.0000000E+00  5150.158      2956.293     0.0000000E+00 221000\n" +
                            "  0.0000000E+00 0.0000000E+00 -10087.68     -5828.656     0.0000000E+00 131000\n" +
                            "  0.0000000E+00 0.0000000E+00 -6973.870     -3812.554     0.0000000E+00 041000\n" +
                            "   5221.981      1691.028     0.0000000E+00 0.0000000E+00 -886.8280     302000\n" +
                            "   6299.073     -673.8698     0.0000000E+00 0.0000000E+00  880.7762     212000\n" +
                            "  -6421.335     -6209.451     0.0000000E+00 0.0000000E+00  2817.726     122000\n" +
                            "  -6889.654     -3563.472     0.0000000E+00 0.0000000E+00  684.9215     032000\n" +
                            "  0.0000000E+00 0.0000000E+00  161.0667      1616.286     0.0000000E+00 203000\n" +
                            "  0.0000000E+00 0.0000000E+00 -87.43673      1738.289     0.0000000E+00 113000\n" +
                            "  0.0000000E+00 0.0000000E+00  794.4928      1169.810     0.0000000E+00 023000\n" +
                            "   1356.963      197.1170     0.0000000E+00 0.0000000E+00 -610.1758     104000\n" +
                            "   924.7945     -457.3952     0.0000000E+00 0.0000000E+00 -235.3306     014000\n" +
                            "  0.0000000E+00 0.0000000E+00  572.7311      257.8445     0.0000000E+00 005000\n" +
                            "  0.0000000E+00 0.0000000E+00  3355.190      3356.201     0.0000000E+00 400100\n" +
                            "  0.0000000E+00 0.0000000E+00  17645.45      11597.50     0.0000000E+00 310100\n" +
                            "  0.0000000E+00 0.0000000E+00  13895.49      8139.428     0.0000000E+00 220100\n" +
                            "  0.0000000E+00 0.0000000E+00 -19755.82     -11983.07     0.0000000E+00 130100\n" +
                            "  0.0000000E+00 0.0000000E+00 -15882.68     -9800.661     0.0000000E+00 040100\n" +
                            "   15337.00      2062.422     0.0000000E+00 0.0000000E+00 -2595.585     301100\n" +
                            "   24117.01     -10472.78     0.0000000E+00 0.0000000E+00  3043.137     211100\n" +
                            "  -17092.12     -32674.13     0.0000000E+00 0.0000000E+00  7996.321     121100\n" +
                            "  -26575.69     -19314.94     0.0000000E+00 0.0000000E+00  2424.174     031100\n" +
                            "  0.0000000E+00 0.0000000E+00 -3641.365      1613.826     0.0000000E+00 202100\n" +
                            "  0.0000000E+00 0.0000000E+00 -10465.59     -2079.847     0.0000000E+00 112100\n" +
                            "  0.0000000E+00 0.0000000E+00 -161.3446      1581.734     0.0000000E+00 022100\n" +
                            "   4877.776      758.4664     0.0000000E+00 0.0000000E+00 -4555.362     103100\n" +
                            "   1556.843     -3775.386     0.0000000E+00 0.0000000E+00 -1057.424     013100\n" +
                            "  0.0000000E+00 0.0000000E+00  4701.115      2094.285     0.0000000E+00 004100\n" +
                            "   172.4595      191.0357     0.0000000E+00 0.0000000E+00  138.9236     400001\n" +
                            "   1450.334      161.7205     0.0000000E+00 0.0000000E+00  558.1844     310001\n" +
                            "   4831.602     -288.9683     0.0000000E+00 0.0000000E+00  503.2429     220001\n" +
                            "   2237.404     -1157.108     0.0000000E+00 0.0000000E+00  61.90064     130001\n" +
                            "  -1293.242     -972.3758     0.0000000E+00 0.0000000E+00 -32.69425     040001\n" +
                            "  0.0000000E+00 0.0000000E+00  1228.944      1449.270     0.0000000E+00 301001\n" +
                            "  0.0000000E+00 0.0000000E+00  4160.832      4006.619     0.0000000E+00 211001\n" +
                            "  0.0000000E+00 0.0000000E+00  3022.923      3519.930     0.0000000E+00 121001\n" +
                            "  0.0000000E+00 0.0000000E+00  119.8045     -11.22768     0.0000000E+00 031001\n" +
                            "   1247.940      1449.513     0.0000000E+00 0.0000000E+00 -151.3200     202001\n" +
                            "   4704.027      2618.514     0.0000000E+00 0.0000000E+00  235.7324     112001\n" +
                            "   3604.115      1294.977     0.0000000E+00 0.0000000E+00  277.4179     022001\n" +
                            "  0.0000000E+00 0.0000000E+00 -243.9058      16.40467     0.0000000E+00 103001\n" +
                            "  0.0000000E+00 0.0000000E+00  694.2251      521.3873     0.0000000E+00 013001\n" +
                            "   191.8432      200.1020     0.0000000E+00 0.0000000E+00  22.10159     004001\n" +
                            "   11277.46     -1044.251     0.0000000E+00 0.0000000E+00 -1051.695     300200\n" +
                            "   23670.04     -17266.17     0.0000000E+00 0.0000000E+00  2048.459     210200\n" +
                            "  -7771.847     -38734.18     0.0000000E+00 0.0000000E+00  2867.291     120200\n" +
                            "  -27446.74     -21667.01     0.0000000E+00 0.0000000E+00 -301.4361     030200\n" +
                            "  0.0000000E+00 0.0000000E+00 -15347.97     -9208.647     0.0000000E+00 201200\n" +
                            "  0.0000000E+00 0.0000000E+00 -42520.07     -25043.22     0.0000000E+00 111200\n" +
                            "  0.0000000E+00 0.0000000E+00 -12000.22     -5625.225     0.0000000E+00 021200\n" +
                            "  -949.7879     -2385.285     0.0000000E+00 0.0000000E+00 -12280.96     102200\n" +
                            "  -14942.88     -15152.89     0.0000000E+00 0.0000000E+00 -1771.005     012200\n" +
                            "  0.0000000E+00 0.0000000E+00  19261.43      10142.74     0.0000000E+00 003200\n" +
                            "  0.0000000E+00 0.0000000E+00  2316.598      2507.493     0.0000000E+00 300101\n" +
                            "  0.0000000E+00 0.0000000E+00  4620.716      6373.865     0.0000000E+00 210101\n" +
                            "  0.0000000E+00 0.0000000E+00 -256.6659      4147.433     0.0000000E+00 120101\n" +
                            "  0.0000000E+00 0.0000000E+00 -492.0422      6.167935     0.0000000E+00 030101\n" +
                            "   1974.798      2378.719     0.0000000E+00 0.0000000E+00  65.15661     201101\n" +
                            "   13007.07      6070.487     0.0000000E+00 0.0000000E+00  1486.286     111101\n" +
                            "   9387.256      4432.055     0.0000000E+00 0.0000000E+00 -61.83463     021101\n" +
                            "  0.0000000E+00 0.0000000E+00  4900.162      3841.003     0.0000000E+00 102101\n" +
                            "  0.0000000E+00 0.0000000E+00  6079.282      5282.231     0.0000000E+00 012101\n" +
                            "   2075.617      947.9107     0.0000000E+00 0.0000000E+00  6.223560     003101\n" +
                            "   80.61482      65.66301     0.0000000E+00 0.0000000E+00  33.29708     300002\n" +
                            "   354.5622      266.1352     0.0000000E+00 0.0000000E+00  78.11257     210002\n" +
                            "   527.4155      418.6450     0.0000000E+00 0.0000000E+00  19.63841     120002\n" +
                            "   267.9720      270.8019     0.0000000E+00 0.0000000E+00 -28.77228     030002\n" +
                            "  0.0000000E+00 0.0000000E+00  333.7124      413.0777     0.0000000E+00 201002\n" +
                            "  0.0000000E+00 0.0000000E+00  206.9099      472.6280     0.0000000E+00 111002\n" +
                            "  0.0000000E+00 0.0000000E+00 -117.6942      414.6389     0.0000000E+00 021002\n" +
                            "  -57.27247     -31.42795     0.0000000E+00 0.0000000E+00  6.458659     102002\n" +
                            "  -103.6174     -133.1062     0.0000000E+00 0.0000000E+00 -81.18579     012002\n" +
                            "  0.0000000E+00 0.0000000E+00 -127.4121     -107.9626     0.0000000E+00 003002\n" +
                            "  0.0000000E+00 0.0000000E+00 -17082.66     -13754.69     0.0000000E+00 200300\n" +
                            "  0.0000000E+00 0.0000000E+00 -43881.17     -28791.09     0.0000000E+00 110300\n" +
                            "  0.0000000E+00 0.0000000E+00 -25555.62     -17504.71     0.0000000E+00 020300\n" +
                            "  -16154.93     -9810.570     0.0000000E+00 0.0000000E+00 -16498.18     101300\n" +
                            "  -47870.48     -30301.28     0.0000000E+00 0.0000000E+00 -1379.350     011300\n" +
                            "  0.0000000E+00 0.0000000E+00  41193.35      24720.19     0.0000000E+00 002300\n" +
                            "   3617.961      801.5090     0.0000000E+00 0.0000000E+00  682.8452     200201\n" +
                            "   7864.085      3298.351     0.0000000E+00 0.0000000E+00  1370.138     110201\n" +
                            "   2905.107      1651.381     0.0000000E+00 0.0000000E+00  773.0133     020201\n" +
                            "  0.0000000E+00 0.0000000E+00  16434.73      12860.60     0.0000000E+00 101201\n" +
                            "  0.0000000E+00 0.0000000E+00  6202.368      6796.531     0.0000000E+00 011201\n" +
                            "   8515.851      2450.859     0.0000000E+00 0.0000000E+00 -237.1627     002201\n" +
                            "  0.0000000E+00 0.0000000E+00  528.0844      696.8674     0.0000000E+00 200102\n" +
                            "  0.0000000E+00 0.0000000E+00  343.8764      760.9363     0.0000000E+00 110102\n" +
                            "  0.0000000E+00 0.0000000E+00 -682.6301     -34.61808     0.0000000E+00 020102\n" +
                            "   390.3704     -63.47619     0.0000000E+00 0.0000000E+00 -313.6958     101102\n" +
                            "   716.6923     -764.9205     0.0000000E+00 0.0000000E+00  91.86826     011102\n" +
                            "  0.0000000E+00 0.0000000E+00 -199.6198     -537.9105     0.0000000E+00 002102\n" +
                            "   20.42571      9.532662     0.0000000E+00 0.0000000E+00  3.332393     200003\n" +
                            "   70.44838      24.48204     0.0000000E+00 0.0000000E+00  3.578342     110003\n" +
                            "   52.22343     -3.670599     0.0000000E+00 0.0000000E+00  6.768826     020003\n" +
                            "  0.0000000E+00 0.0000000E+00  61.85800      121.2655     0.0000000E+00 101003\n" +
                            "  0.0000000E+00 0.0000000E+00  3.811649     -6.750276     0.0000000E+00 011003\n" +
                            "   27.05860      27.05866     0.0000000E+00 0.0000000E+00  26.87699     002003\n" +
                            "  -12854.20     -8131.111     0.0000000E+00 0.0000000E+00 -9539.993     100400\n" +
                            "  -38371.23     -20766.68     0.0000000E+00 0.0000000E+00 -686.7269     010400\n" +
                            "  0.0000000E+00 0.0000000E+00  45059.19      29787.03     0.0000000E+00 001400\n" +
                            "  0.0000000E+00 0.0000000E+00  5190.538      4801.387     0.0000000E+00 100301\n" +
                            "  0.0000000E+00 0.0000000E+00 -444.6287      1509.319     0.0000000E+00 010301\n" +
                            "   12599.26      4111.728     0.0000000E+00 0.0000000E+00 -998.9830     001301\n" +
                            "   991.2578     -170.4594     0.0000000E+00 0.0000000E+00  23.20588     100202\n" +
                            "   1281.181     -174.8293     0.0000000E+00 0.0000000E+00  110.2792     010202\n" +
                            "  0.0000000E+00 0.0000000E+00  2651.314      1236.524     0.0000000E+00 001202\n" +
                            "  0.0000000E+00 0.0000000E+00 -21.58245      91.29053     0.0000000E+00 100103\n" +
                            "  0.0000000E+00 0.0000000E+00  39.28143      40.80841     0.0000000E+00 010103\n" +
                            "  -26.23134      49.04353     0.0000000E+00 0.0000000E+00 -20.20516     001103\n" +
                            "  0.1593085     -1.667353     0.0000000E+00 0.0000000E+00  1.020198     100004\n" +
                            " -0.2469181    -0.3924189     0.0000000E+00 0.0000000E+00-0.1874837     010004\n" +
                            "  0.0000000E+00 0.0000000E+00  13.56824      19.68726     0.0000000E+00 001004\n" +
                            "  0.0000000E+00 0.0000000E+00  19880.19      14378.32     0.0000000E+00 000500\n" +
                            "   4265.917      2292.667     0.0000000E+00 0.0000000E+00 -992.1038     000401\n" +
                            "  0.0000000E+00 0.0000000E+00  1930.448      1297.434     0.0000000E+00 000302\n" +
                            "  -49.86380     -58.42061     0.0000000E+00 0.0000000E+00 -3.629270     000203\n" +
                            "  0.0000000E+00 0.0000000E+00-0.4655091      14.79530     0.0000000E+00 000104\n" +
                            " -0.7621282E-01-0.1582300     0.0000000E+00 0.0000000E+00 0.6058916     000005"
            );

            beforeQS3.map = CosyArbitraryOrder.readMap(
                    " -0.2817860E-01 -1.556516     0.0000000E+00 0.0000000E+00-0.4295789     100000\n" +
                            "  0.5558699     -4.783058     0.0000000E+00 0.0000000E+00 -1.302039     010000\n" +
                            "  0.0000000E+00 0.0000000E+00 0.2790888    -0.4406930     0.0000000E+00 001000\n" +
                            "  0.0000000E+00 0.0000000E+00  1.765523     0.7952536     0.0000000E+00 000100\n" +
                            "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00  1.000000     000010\n" +
                            "  0.2754796    -0.2805744E-01 0.0000000E+00 0.0000000E+00 0.6106278     000001\n" +
                            " -0.8575551      1.460035     0.0000000E+00 0.0000000E+00 0.1460028     200000\n" +
                            "  -4.194062      1.851399     0.0000000E+00 0.0000000E+00 0.6435851E-01 110000\n" +
                            "  -6.229335     -6.402148     0.0000000E+00 0.0000000E+00 -1.728021     020000\n" +
                            "  0.0000000E+00 0.0000000E+00 -2.092854     -3.314779     0.0000000E+00 101000\n" +
                            "  0.0000000E+00 0.0000000E+00 -6.611159     -9.650868     0.0000000E+00 011000\n" +
                            " -0.5924616     0.5452265E-01 0.0000000E+00 0.0000000E+00-0.2988466     002000\n" +
                            "  0.0000000E+00 0.0000000E+00 -4.668986     -7.633336     0.0000000E+00 100100\n" +
                            "  0.0000000E+00 0.0000000E+00 -13.81347     -20.40132     0.0000000E+00 010100\n" +
                            "  -2.659946      1.693548     0.0000000E+00 0.0000000E+00-0.9350274     001100\n" +
                            "  0.2673284     -1.710216     0.0000000E+00 0.0000000E+00-0.5182287E-02 100001\n" +
                            "  0.1704608     -2.223835     0.0000000E+00 0.0000000E+00 0.6446416     010001\n" +
                            "  0.0000000E+00 0.0000000E+00 0.6792479      1.057539     0.0000000E+00 001001\n" +
                            "  -3.066410      3.867683     0.0000000E+00 0.0000000E+00 -1.533425     000200\n" +
                            "  0.0000000E+00 0.0000000E+00 0.9401121      3.270046     0.0000000E+00 000101\n" +
                            " -0.1450650     0.1216367     0.0000000E+00 0.0000000E+00-0.5137635     000002\n" +
                            " -0.2397216      5.115339     0.0000000E+00 0.0000000E+00 0.5966889     300000\n" +
                            "  -1.913055      35.60571     0.0000000E+00 0.0000000E+00  3.366482     210000\n" +
                            "  -10.55057      74.75393     0.0000000E+00 0.0000000E+00  3.353841     120000\n" +
                            "  -17.58316      42.26789     0.0000000E+00 0.0000000E+00 -4.139806     030000\n" +
                            "  0.0000000E+00 0.0000000E+00 0.1151877    -0.7750223     0.0000000E+00 201000\n" +
                            "  0.0000000E+00 0.0000000E+00 -6.701672     -13.70272     0.0000000E+00 111000\n" +
                            "  0.0000000E+00 0.0000000E+00 -19.58689     -33.42009     0.0000000E+00 021000\n" +
                            "  -1.145852     0.6571701     0.0000000E+00 0.0000000E+00 -1.128962     102000\n" +
                            "  -2.162391      1.167892     0.0000000E+00 0.0000000E+00 -3.181197     012000\n" +
                            "  0.0000000E+00 0.0000000E+00 -2.982051     -5.040151     0.0000000E+00 003000\n" +
                            "  0.0000000E+00 0.0000000E+00 -1.189900     -5.139384     0.0000000E+00 200100\n" +
                            "  0.0000000E+00 0.0000000E+00 -20.03084     -44.24857     0.0000000E+00 110100\n" +
                            "  0.0000000E+00 0.0000000E+00 -46.52220     -87.74879     0.0000000E+00 020100\n" +
                            "  -5.225768      5.226062     0.0000000E+00 0.0000000E+00 -3.469246     101100\n" +
                            "  -8.493235      13.24520     0.0000000E+00 0.0000000E+00 -8.900560     011100\n" +
                            "  0.0000000E+00 0.0000000E+00 -19.07582     -33.88812     0.0000000E+00 002100\n" +
                            "  0.1389734     -1.716321     0.0000000E+00 0.0000000E+00-0.3984334     200001\n" +
                            "   1.550966     -13.08400     0.0000000E+00 0.0000000E+00 -2.382953     110001\n" +
                            "   4.859532     -13.06213     0.0000000E+00 0.0000000E+00 -1.086769     020001\n" +
                            "  0.0000000E+00 0.0000000E+00 0.8013936     0.5981035     0.0000000E+00 101001\n" +
                            "  0.0000000E+00 0.0000000E+00  6.230386      7.017703     0.0000000E+00 011001\n" +
                            "  0.7983282     0.1079928     0.0000000E+00 0.0000000E+00 0.6224684     002001\n" +
                            "  -6.530447      10.05418     0.0000000E+00 0.0000000E+00 -1.797822     100200\n" +
                            "  -9.451004      26.49639     0.0000000E+00 0.0000000E+00 -4.259087     010200\n" +
                            "  0.0000000E+00 0.0000000E+00 -42.35092     -77.92453     0.0000000E+00 001200\n" +
                            "  0.0000000E+00 0.0000000E+00  5.401256      7.213184     0.0000000E+00 100101\n" +
                            "  0.0000000E+00 0.0000000E+00  23.83251      31.27149     0.0000000E+00 010101\n" +
                            "   5.303329     0.4345304     0.0000000E+00 0.0000000E+00  2.884729     001101\n" +
                            " -0.1727348     0.5888810     0.0000000E+00 0.0000000E+00 0.1228923E-01 100002\n" +
                            " -0.8077684E-01  2.489811     0.0000000E+00 0.0000000E+00-0.3518858     010002\n" +
                            "  0.0000000E+00 0.0000000E+00-0.6082453    -0.6233091     0.0000000E+00 001002\n" +
                            "  0.0000000E+00 0.0000000E+00 -31.67511     -61.90549     0.0000000E+00 000300\n" +
                            "   7.985610     -1.639468     0.0000000E+00 0.0000000E+00  4.047733     000201\n" +
                            "  0.0000000E+00 0.0000000E+00 -1.956964     -3.516100     0.0000000E+00 000102\n" +
                            "  0.1221687    -0.6822697E-01 0.0000000E+00 0.0000000E+00 0.4465517     000003\n" +
                            "   1.584907      3.317920     0.0000000E+00 0.0000000E+00  1.051608     400000\n" +
                            "   20.17118      44.21263     0.0000000E+00 0.0000000E+00  12.14772     310000\n" +
                            "   71.55030      194.3595     0.0000000E+00 0.0000000E+00  44.11442     220000\n" +
                            "   75.26756      351.0987     0.0000000E+00 0.0000000E+00  55.31443     130000\n" +
                            "  -8.503263      214.2477     0.0000000E+00 0.0000000E+00  8.971808     040000\n" +
                            "  0.0000000E+00 0.0000000E+00  5.558590      9.114681     0.0000000E+00 301000\n" +
                            "  0.0000000E+00 0.0000000E+00  29.49688      55.42981     0.0000000E+00 211000\n" +
                            "  0.0000000E+00 0.0000000E+00  28.85294      82.90388     0.0000000E+00 121000\n" +
                            "  0.0000000E+00 0.0000000E+00 -30.49231     -3.398815     0.0000000E+00 031000\n" +
                            "  -1.714321      3.943575     0.0000000E+00 0.0000000E+00 -1.381083     202000\n" +
                            "  -6.731303      16.89803     0.0000000E+00 0.0000000E+00 -10.22531     112000\n" +
                            "  -9.033118      13.62187     0.0000000E+00 0.0000000E+00 -19.01913     022000\n" +
                            "  0.0000000E+00 0.0000000E+00 -9.180952     -13.85001     0.0000000E+00 103000\n" +
                            "  0.0000000E+00 0.0000000E+00 -25.91834     -38.26539     0.0000000E+00 013000\n" +
                            "  -1.726643     -1.727285     0.0000000E+00 0.0000000E+00 -1.638381     004000\n" +
                            "  0.0000000E+00 0.0000000E+00  10.18427      17.44843     0.0000000E+00 300100\n" +
                            "  0.0000000E+00 0.0000000E+00  52.49741      101.9230     0.0000000E+00 210100\n" +
                            "  0.0000000E+00 0.0000000E+00  42.07716      126.5196     0.0000000E+00 120100\n" +
                            "  0.0000000E+00 0.0000000E+00 -75.33820     -63.21580     0.0000000E+00 030100\n" +
                            "  -5.243874      19.04921     0.0000000E+00 0.0000000E+00 -6.317251     201100\n" +
                            "  -5.598132      89.79439     0.0000000E+00 0.0000000E+00 -36.98448     111100\n" +
                            "   5.073909      88.58888     0.0000000E+00 0.0000000E+00 -60.01856     021100\n" +
                            "  0.0000000E+00 0.0000000E+00 -56.72107     -92.55951     0.0000000E+00 102100\n" +
                            "  0.0000000E+00 0.0000000E+00 -156.5308     -249.2671     0.0000000E+00 012100\n" +
                            "  -16.13471     -16.47787     0.0000000E+00 0.0000000E+00 -12.82996     003100\n" +
                            " -0.2375790E-01 0.3088420E-01 0.0000000E+00 0.0000000E+00-0.5245561     300001\n" +
                            "  -6.621115     -26.86134     0.0000000E+00 0.0000000E+00 -8.022422     210001\n" +
                            "  -22.61865     -121.7366     0.0000000E+00 0.0000000E+00 -24.03716     120001\n" +
                            "  -10.64113     -126.8492     0.0000000E+00 0.0000000E+00 -14.04318     030001\n" +
                            "  0.0000000E+00 0.0000000E+00 -3.267544     -4.875687     0.0000000E+00 201001\n" +
                            "  0.0000000E+00 0.0000000E+00 -10.16005     -18.21549     0.0000000E+00 111001\n" +
                            "  0.0000000E+00 0.0000000E+00  8.854483      4.468398     0.0000000E+00 021001\n" +
                            "   1.799221     -2.463744     0.0000000E+00 0.0000000E+00  2.282758     102001\n" +
                            "   3.725697     -7.154235     0.0000000E+00 0.0000000E+00  7.896573     012001\n" +
                            "  0.0000000E+00 0.0000000E+00  4.967126      5.993866     0.0000000E+00 003001\n" +
                            "  -2.523696      23.30294     0.0000000E+00 0.0000000E+00 -6.481339     200200\n" +
                            "   21.06777      124.8326     0.0000000E+00 0.0000000E+00 -28.38215     110200\n" +
                            "   50.24903      143.7786     0.0000000E+00 0.0000000E+00 -37.63048     020200\n" +
                            "  0.0000000E+00 0.0000000E+00 -122.2301     -209.6013     0.0000000E+00 101200\n" +
                            "  0.0000000E+00 0.0000000E+00 -335.6033     -556.3974     0.0000000E+00 011200\n" +
                            "  -57.78364     -59.33136     0.0000000E+00 0.0000000E+00 -38.22006     002200\n" +
                            "  0.0000000E+00 0.0000000E+00 -5.127257     -7.545440     0.0000000E+00 200101\n" +
                            "  0.0000000E+00 0.0000000E+00-0.7661558E-01 -4.488641     0.0000000E+00 110101\n" +
                            "  0.0000000E+00 0.0000000E+00  62.55023      83.21059     0.0000000E+00 020101\n" +
                            "   11.80990     -11.93357     0.0000000E+00 0.0000000E+00  12.20321     101101\n" +
                            "   21.34353     -37.88037     0.0000000E+00 0.0000000E+00  36.40672     011101\n" +
                            "  0.0000000E+00 0.0000000E+00  42.82901      59.91357     0.0000000E+00 002101\n" +
                            " -0.6097278    -0.1412055     0.0000000E+00 0.0000000E+00 0.1991884     200002\n" +
                            "  -1.923307      8.283359     0.0000000E+00 0.0000000E+00  3.095387     110002\n" +
                            "  -3.619612      17.77179     0.0000000E+00 0.0000000E+00  4.117145     020002\n" +
                            "  0.0000000E+00 0.0000000E+00-0.2124336     0.1990435     0.0000000E+00 101002\n" +
                            "  0.0000000E+00 0.0000000E+00 -4.652578     -3.729389     0.0000000E+00 011002\n" +
                            "  -1.035663     0.1927880     0.0000000E+00 0.0000000E+00 -1.025784     002002\n" +
                            "  0.0000000E+00 0.0000000E+00 -96.20744     -166.0849     0.0000000E+00 100300\n" +
                            "  0.0000000E+00 0.0000000E+00 -266.1812     -440.0582     0.0000000E+00 010300\n" +
                            "  -94.87845     -95.66543     0.0000000E+00 0.0000000E+00 -51.27282     001300\n" +
                            "   17.83389     -17.55875     0.0000000E+00 0.0000000E+00  12.99774     100201\n" +
                            "   28.72640     -61.62317     0.0000000E+00 0.0000000E+00  34.27211     010201\n" +
                            "  0.0000000E+00 0.0000000E+00  118.2803      180.3961     0.0000000E+00 001201\n" +
                            "  0.0000000E+00 0.0000000E+00 -4.582181     -4.696088     0.0000000E+00 100102\n" +
                            "  0.0000000E+00 0.0000000E+00 -27.79268     -30.84270     0.0000000E+00 010102\n" +
                            "  -8.263310     0.2706859     0.0000000E+00 0.0000000E+00 -5.941958     001102\n" +
                            "  0.2688162    -0.2344258     0.0000000E+00 0.0000000E+00 0.2525767E-01 100003\n" +
                            "  0.1827304     -2.123127     0.0000000E+00 0.0000000E+00 0.2292262     010003\n" +
                            "  0.0000000E+00 0.0000000E+00 0.5988075     0.5189320     0.0000000E+00 001003\n" +
                            "  -60.39069     -56.65944     0.0000000E+00 0.0000000E+00 -26.51955     000400\n" +
                            "  0.0000000E+00 0.0000000E+00  107.9221      175.8980     0.0000000E+00 000301\n" +
                            "  -14.66740      1.057828     0.0000000E+00 0.0000000E+00 -8.812464     000202\n" +
                            "  0.0000000E+00 0.0000000E+00  2.613100      3.619479     0.0000000E+00 000103\n" +
                            " -0.1249864     0.4471147E-01 0.0000000E+00 0.0000000E+00-0.4065146     000004\n" +
                            " -0.5503752     -5.024978     0.0000000E+00 0.0000000E+00 0.6164814     500000\n" +
                            "   22.67216     -41.23177     0.0000000E+00 0.0000000E+00  17.97641     410000\n" +
                            "   216.7648     -103.5113     0.0000000E+00 0.0000000E+00  127.9965     320000\n" +
                            "   666.1394      9.781696     0.0000000E+00 0.0000000E+00  366.2715     230000\n" +
                            "   760.7986      364.5065     0.0000000E+00 0.0000000E+00  420.3875     140000\n" +
                            "   180.5882      343.7526     0.0000000E+00 0.0000000E+00  116.0098     050000\n" +
                            "  0.0000000E+00 0.0000000E+00  11.52145      13.29929     0.0000000E+00 401000\n" +
                            "  0.0000000E+00 0.0000000E+00  114.0468      153.3587     0.0000000E+00 311000\n" +
                            "  0.0000000E+00 0.0000000E+00  373.2818      592.3759     0.0000000E+00 221000\n" +
                            "  0.0000000E+00 0.0000000E+00  420.3484      890.4996     0.0000000E+00 131000\n" +
                            "  0.0000000E+00 0.0000000E+00  35.06259      379.7353     0.0000000E+00 041000\n" +
                            "  -4.142587      6.619283     0.0000000E+00 0.0000000E+00  1.574296     302000\n" +
                            "  -28.98624      55.33411     0.0000000E+00 0.0000000E+00  2.257205     212000\n" +
                            "  -90.00354      134.3316     0.0000000E+00 0.0000000E+00 -36.09307     122000\n" +
                            "  -107.3913      92.85485     0.0000000E+00 0.0000000E+00 -83.37474     032000\n" +
                            "  0.0000000E+00 0.0000000E+00 -10.97309     -10.76533     0.0000000E+00 203000\n" +
                            "  0.0000000E+00 0.0000000E+00 -79.68760     -80.58828     0.0000000E+00 113000\n" +
                            "  0.0000000E+00 0.0000000E+00 -144.1693     -153.6222     0.0000000E+00 023000\n" +
                            "  -5.850702    -0.7254777E-02 0.0000000E+00 0.0000000E+00 -9.253106     104000\n" +
                            "  -12.41425     -1.489841     0.0000000E+00 0.0000000E+00 -25.75999     014000\n" +
                            "  0.0000000E+00 0.0000000E+00 -9.055220     -12.53483     0.0000000E+00 005000\n" +
                            "  0.0000000E+00 0.0000000E+00  22.72237      30.28079     0.0000000E+00 400100\n" +
                            "  0.0000000E+00 0.0000000E+00  218.6738      345.3865     0.0000000E+00 310100\n" +
                            "  0.0000000E+00 0.0000000E+00  695.6738      1316.364     0.0000000E+00 220100\n" +
                            "  0.0000000E+00 0.0000000E+00  741.0988      1927.060     0.0000000E+00 130100\n" +
                            "  0.0000000E+00 0.0000000E+00  3.760043      755.8765     0.0000000E+00 040100\n" +
                            "  -26.75283      28.17626     0.0000000E+00 0.0000000E+00  2.177011     301100\n" +
                            "  -138.6723      239.8741     0.0000000E+00 0.0000000E+00 -2.926412     211100\n" +
                            "  -268.1736      589.7922     0.0000000E+00 0.0000000E+00 -111.6303     121100\n" +
                            "  -202.7072      421.1232     0.0000000E+00 0.0000000E+00 -235.4577     031100\n" +
                            "  0.0000000E+00 0.0000000E+00 -65.11528     -76.85820     0.0000000E+00 202100\n" +
                            "  0.0000000E+00 0.0000000E+00 -444.3603     -516.5276     0.0000000E+00 112100\n" +
                            "  0.0000000E+00 0.0000000E+00 -791.3583     -951.5061     0.0000000E+00 022100\n" +
                            "  -52.94709     -4.332644     0.0000000E+00 0.0000000E+00 -77.74403     103100\n" +
                            "  -107.2673     -21.40360     0.0000000E+00 0.0000000E+00 -212.0673     013100\n" +
                            "  0.0000000E+00 0.0000000E+00 -94.77772     -139.1031     0.0000000E+00 004100\n" +
                            "   3.431274      5.317738     0.0000000E+00 0.0000000E+00 0.3084630     400001\n" +
                            "  -4.198994      19.41725     0.0000000E+00 0.0000000E+00 -13.12183     310001\n" +
                            "  -147.6290     -85.82171     0.0000000E+00 0.0000000E+00 -108.4861     220001\n" +
                            "  -399.9289     -450.4159     0.0000000E+00 0.0000000E+00 -255.5684     130001\n" +
                            "  -264.0932     -475.9165     0.0000000E+00 0.0000000E+00 -167.7046     040001\n" +
                            "  0.0000000E+00 0.0000000E+00 -10.21886     -8.788657     0.0000000E+00 301001\n" +
                            "  0.0000000E+00 0.0000000E+00 -96.88054     -115.0922     0.0000000E+00 211001\n" +
                            "  0.0000000E+00 0.0000000E+00 -235.3469     -344.4674     0.0000000E+00 121001\n" +
                            "  0.0000000E+00 0.0000000E+00 -113.8299     -239.4648     0.0000000E+00 031001\n" +
                            "   5.240736     -5.439589     0.0000000E+00 0.0000000E+00  1.457711     202001\n" +
                            "   22.49813     -41.56493     0.0000000E+00 0.0000000E+00  16.47940     112001\n" +
                            "   32.28047     -57.95535     0.0000000E+00 0.0000000E+00  43.78756     022001\n" +
                            "  0.0000000E+00 0.0000000E+00  15.87816      16.06227     0.0000000E+00 103001\n" +
                            "  0.0000000E+00 0.0000000E+00  56.82245      61.65120     0.0000000E+00 013001\n" +
                            "   3.943003     0.7462321     0.0000000E+00 0.0000000E+00  5.162090     004001\n" +
                            "  -37.10900      27.93332     0.0000000E+00 0.0000000E+00 -1.151675     300200\n" +
                            "  -144.8996      268.8112     0.0000000E+00 0.0000000E+00 -2.902205     210200\n" +
                            "  -99.03423      728.3037     0.0000000E+00 0.0000000E+00 -36.29437     120200\n" +
                            "   109.3361      588.4950     0.0000000E+00 0.0000000E+00 -94.86219     030200\n" +
                            "  0.0000000E+00 0.0000000E+00 -122.1186     -177.1870     0.0000000E+00 201200\n" +
                            "  0.0000000E+00 0.0000000E+00 -808.0189     -1082.802     0.0000000E+00 111200\n" +
                            "  0.0000000E+00 0.0000000E+00 -1469.016     -1958.188     0.0000000E+00 021200\n" +
                            "  -188.1511     -27.85343     0.0000000E+00 0.0000000E+00 -252.1781     102200\n" +
                            "  -370.5518     -95.79749     0.0000000E+00 0.0000000E+00 -678.5563     012200\n" +
                            "  0.0000000E+00 0.0000000E+00 -407.8314     -627.0988     0.0000000E+00 003200\n" +
                            "  0.0000000E+00 0.0000000E+00 -27.45382     -30.68690     0.0000000E+00 300101\n" +
                            "  0.0000000E+00 0.0000000E+00 -224.0066     -313.3389     0.0000000E+00 210101\n" +
                            "  0.0000000E+00 0.0000000E+00 -466.0924     -799.4862     0.0000000E+00 120101\n" +
                            "  0.0000000E+00 0.0000000E+00 -124.9014     -414.9288     0.0000000E+00 030101\n" +
                            "   34.82629     -34.47794     0.0000000E+00 0.0000000E+00  15.88104     201101\n" +
                            "   122.5936     -237.8026     0.0000000E+00 0.0000000E+00  113.1128     111101\n" +
                            "   123.4910     -320.3512     0.0000000E+00 0.0000000E+00  224.4644     021101\n" +
                            "  0.0000000E+00 0.0000000E+00  133.6267      167.2288     0.0000000E+00 102101\n" +
                            "  0.0000000E+00 0.0000000E+00  439.1482      561.7547     0.0000000E+00 012101\n" +
                            "   46.19373      18.10406     0.0000000E+00 0.0000000E+00  50.14889     003101\n" +
                            "  -2.916022     -2.329052     0.0000000E+00 0.0000000E+00-0.5840785     300002\n" +
                            "  -7.265592      4.807826     0.0000000E+00 0.0000000E+00  3.987468     210002\n" +
                            "   20.57793      98.88446     0.0000000E+00 0.0000000E+00  32.00291     120002\n" +
                            "   39.61230      166.0370     0.0000000E+00 0.0000000E+00  37.00354     030002\n" +
                            "  0.0000000E+00 0.0000000E+00  4.188099      3.491127     0.0000000E+00 201002\n" +
                            "  0.0000000E+00 0.0000000E+00  26.98911      31.11892     0.0000000E+00 111002\n" +
                            "  0.0000000E+00 0.0000000E+00  19.32421      30.48470     0.0000000E+00 021002\n" +
                            "  -2.967211      2.993715     0.0000000E+00 0.0000000E+00 -3.513978     102002\n" +
                            "  -5.759350      12.77274     0.0000000E+00 0.0000000E+00 -13.36054     012002\n" +
                            "  0.0000000E+00 0.0000000E+00 -7.336845     -7.386499     0.0000000E+00 003002\n" +
                            "  0.0000000E+00 0.0000000E+00 -73.63423     -134.6385     0.0000000E+00 200300\n" +
                            "  0.0000000E+00 0.0000000E+00 -500.9206     -770.6867     0.0000000E+00 110300\n" +
                            "  0.0000000E+00 0.0000000E+00 -979.4319     -1405.684     0.0000000E+00 020300\n" +
                            "  -310.7796     -59.36062     0.0000000E+00 0.0000000E+00 -373.3613     101300\n" +
                            "  -602.3786     -171.2565     0.0000000E+00 0.0000000E+00 -993.3387     011300\n" +
                            "  0.0000000E+00 0.0000000E+00 -911.0767     -1452.282     0.0000000E+00 002300\n" +
                            "   47.67972     -49.86510     0.0000000E+00 0.0000000E+00  26.65167     200201\n" +
                            "   125.3504     -346.6137     0.0000000E+00 0.0000000E+00  147.1325     110201\n" +
                            "   58.33841     -484.4955     0.0000000E+00 0.0000000E+00  235.5137     020201\n" +
                            "  0.0000000E+00 0.0000000E+00  351.2240      507.7769     0.0000000E+00 101201\n" +
                            "  0.0000000E+00 0.0000000E+00  1104.849      1587.460     0.0000000E+00 011201\n" +
                            "   197.7775      102.8103     0.0000000E+00 0.0000000E+00  178.7133     002201\n" +
                            "  0.0000000E+00 0.0000000E+00  11.64064      11.84016     0.0000000E+00 200102\n" +
                            "  0.0000000E+00 0.0000000E+00  48.77490      63.05481     0.0000000E+00 110102\n" +
                            "  0.0000000E+00 0.0000000E+00 -20.11185     -8.313009     0.0000000E+00 020102\n" +
                            "  -23.01319      20.00290     0.0000000E+00 0.0000000E+00 -24.34083     101102\n" +
                            "  -43.27219      78.81428     0.0000000E+00 0.0000000E+00 -81.00043     011102\n" +
                            "  0.0000000E+00 0.0000000E+00 -73.66060     -88.20741     0.0000000E+00 002102\n" +
                            "   1.477486     0.6740243     0.0000000E+00 0.0000000E+00 0.1633249     200003\n" +
                            "   5.592054     -3.908173     0.0000000E+00 0.0000000E+00 -2.301742     110003\n" +
                            "   6.378613     -16.57382     0.0000000E+00 0.0000000E+00 -5.690062     020003\n" +
                            "  0.0000000E+00 0.0000000E+00 0.6225096E-01-0.7780217E-01 0.0000000E+00 101003\n" +
                            "  0.0000000E+00 0.0000000E+00  3.204544      2.003389     0.0000000E+00 011003\n" +
                            "   1.364458    -0.3581956     0.0000000E+00 0.0000000E+00  1.542473     002003\n" +
                            "  -200.9225     -39.32494     0.0000000E+00 0.0000000E+00 -208.9401     100400\n" +
                            "  -384.7847     -96.77417     0.0000000E+00 0.0000000E+00 -548.7183     010400\n" +
                            "  0.0000000E+00 0.0000000E+00 -1066.746     -1744.055     0.0000000E+00 001400\n" +
                            "  0.0000000E+00 0.0000000E+00  311.8240      492.0711     0.0000000E+00 100301\n" +
                            "  0.0000000E+00 0.0000000E+00  969.5319      1484.199     0.0000000E+00 010301\n" +
                            "   376.0195      223.6695     0.0000000E+00 0.0000000E+00  280.2211     001301\n" +
                            "  -39.97415      31.59027     0.0000000E+00 0.0000000E+00 -34.76376     100202\n" +
                            "  -70.05645      127.3143     0.0000000E+00 0.0000000E+00 -102.8606     010202\n" +
                            "  0.0000000E+00 0.0000000E+00 -232.0283     -311.7429     0.0000000E+00 001202\n" +
                            "  0.0000000E+00 0.0000000E+00  3.891263      3.774058     0.0000000E+00 100103\n" +
                            "  0.0000000E+00 0.0000000E+00  28.36477      28.13443     0.0000000E+00 010103\n" +
                            "   11.97623     -1.430829     0.0000000E+00 0.0000000E+00  10.29354     001103\n" +
                            " -0.4228413     0.8523766E-01 0.0000000E+00 0.0000000E+00-0.9377915E-01 100004\n" +
                            " -0.4685473      1.787786     0.0000000E+00 0.0000000E+00-0.2352380     010004\n" +
                            "  0.0000000E+00 0.0000000E+00-0.6335773    -0.5123128     0.0000000E+00 001004\n" +
                            "  0.0000000E+00 0.0000000E+00 -526.8825     -875.4576     0.0000000E+00 000500\n" +
                            "   271.5670      167.6050     0.0000000E+00 0.0000000E+00  164.4453     000401\n" +
                            "  0.0000000E+00 0.0000000E+00 -238.6496     -347.7893     0.0000000E+00 000302\n" +
                            "   23.66203     -1.871920     0.0000000E+00 0.0000000E+00  16.60543     000203\n" +
                            "  0.0000000E+00 0.0000000E+00 -3.214144     -3.828155     0.0000000E+00 000104\n" +
                            "  0.1402043    -0.3223699E-01 0.0000000E+00 0.0000000E+00 0.3844893     000005"
            );

            cct345WithDl2TwoSides.map = CosyArbitraryOrder.readMap(
                    "  -3.014213     -1.556516     0.0000000E+00 0.0000000E+00-0.4295789     100000\n" +
                            "  -8.619995     -4.783058     0.0000000E+00 0.0000000E+00 -1.302039     010000\n" +
                            "  0.0000000E+00 0.0000000E+00-0.5663410    -0.4406930     0.0000000E+00 001000\n" +
                            "  0.0000000E+00 0.0000000E+00  3.291145     0.7952536     0.0000000E+00 000100\n" +
                            "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00  1.000000     000010\n" +
                            "  0.2216539    -0.2805744E-01 0.0000000E+00 0.0000000E+00 0.9966968     000001\n" +
                            "   1.943390      1.460035     0.0000000E+00 0.0000000E+00 -1.135392     200000\n" +
                            " -0.6423206      1.851399     0.0000000E+00 0.0000000E+00 -7.810907     110000\n" +
                            "  -18.51128     -6.402148     0.0000000E+00 0.0000000E+00 -13.82808     020000\n" +
                            "  0.0000000E+00 0.0000000E+00 -8.451959     -3.314779     0.0000000E+00 101000\n" +
                            "  0.0000000E+00 0.0000000E+00 -25.12548     -9.650868     0.0000000E+00 011000\n" +
                            " -0.4878648     0.5452265E-01 0.0000000E+00 0.0000000E+00-0.4015650     002000\n" +
                            "  0.0000000E+00 0.0000000E+00 -19.31285     -7.633336     0.0000000E+00 100100\n" +
                            "  0.0000000E+00 0.0000000E+00 -52.95156     -20.40132     0.0000000E+00 010100\n" +
                            "  0.5889737      1.693548     0.0000000E+00 0.0000000E+00-0.5643058     001100\n" +
                            "  -1.367076     -1.710216     0.0000000E+00 0.0000000E+00-0.5137863E-01 100001\n" +
                            "  0.9637825     -2.223835     0.0000000E+00 0.0000000E+00 0.5026836     010001\n" +
                            "  0.0000000E+00 0.0000000E+00  3.174208      1.057539     0.0000000E+00 001001\n" +
                            "   4.353390      3.867683     0.0000000E+00 0.0000000E+00 -1.867918     000200\n" +
                            "  0.0000000E+00 0.0000000E+00  6.372176      3.270046     0.0000000E+00 000101\n" +
                            "  0.1179634     0.1216367     0.0000000E+00 0.0000000E+00-0.8334960     000002\n" +
                            "   5.956401      5.115339     0.0000000E+00 0.0000000E+00  3.000624     300000\n" +
                            "   33.04718      35.60571     0.0000000E+00 0.0000000E+00  13.80191     210000\n" +
                            "   30.38796      74.75393     0.0000000E+00 0.0000000E+00  2.179999     120000\n" +
                            "  -41.45709      42.26789     0.0000000E+00 0.0000000E+00 -36.53178     030000\n" +
                            "  0.0000000E+00 0.0000000E+00 -2.395751    -0.7750223     0.0000000E+00 201000\n" +
                            "  0.0000000E+00 0.0000000E+00 -39.28325     -13.70272     0.0000000E+00 111000\n" +
                            "  0.0000000E+00 0.0000000E+00 -93.37104     -33.42009     0.0000000E+00 021000\n" +
                            " -0.1750897     0.6571701     0.0000000E+00 0.0000000E+00 -2.584435     102000\n" +
                            " -0.8129196      1.167892     0.0000000E+00 0.0000000E+00 -7.404261     012000\n" +
                            "  0.0000000E+00 0.0000000E+00 -12.73322     -5.040151     0.0000000E+00 003000\n" +
                            "  0.0000000E+00 0.0000000E+00 -9.201250     -5.139384     0.0000000E+00 200100\n" +
                            "  0.0000000E+00 0.0000000E+00 -93.55962     -44.24857     0.0000000E+00 110100\n" +
                            "  0.0000000E+00 0.0000000E+00 -197.4090     -87.74879     0.0000000E+00 020100\n" +
                            "   5.846453      5.226062     0.0000000E+00 0.0000000E+00 -1.450776     101100\n" +
                            "   20.13229      13.24520     0.0000000E+00 0.0000000E+00 -1.723865     011100\n" +
                            "  0.0000000E+00 0.0000000E+00 -83.64269     -33.88812     0.0000000E+00 002100\n" +
                            "  -4.893675     -1.716321     0.0000000E+00 0.0000000E+00 -1.290169     200001\n" +
                            "  -26.71012     -13.08400     0.0000000E+00 0.0000000E+00 -3.083405     110001\n" +
                            "  -15.27386     -13.06213     0.0000000E+00 0.0000000E+00  5.231673     020001\n" +
                            "  0.0000000E+00 0.0000000E+00  5.418274     0.5981035     0.0000000E+00 101001\n" +
                            "  0.0000000E+00 0.0000000E+00  29.78851      7.017703     0.0000000E+00 011001\n" +
                            "  0.9426015     0.1079928     0.0000000E+00 0.0000000E+00  1.267844     002001\n" +
                            "   11.81336      10.05418     0.0000000E+00 0.0000000E+00  10.99163     100200\n" +
                            "   38.47839      26.49639     0.0000000E+00 0.0000000E+00  32.47172     010200\n" +
                            "  0.0000000E+00 0.0000000E+00 -192.6441     -77.92453     0.0000000E+00 001200\n" +
                            "  0.0000000E+00 0.0000000E+00  27.38032      7.213184     0.0000000E+00 100101\n" +
                            "  0.0000000E+00 0.0000000E+00  105.6094      31.27149     0.0000000E+00 010101\n" +
                            "   4.364354     0.4345304     0.0000000E+00 0.0000000E+00  3.025623     001101\n" +
                            "   1.554199     0.5888810     0.0000000E+00 0.0000000E+00 0.2296104     100002\n" +
                            "   3.324134      2.489811     0.0000000E+00 0.0000000E+00 0.4059001     010002\n" +
                            "  0.0000000E+00 0.0000000E+00 -3.265124    -0.6233091     0.0000000E+00 001002\n" +
                            "  0.0000000E+00 0.0000000E+00 -149.9528     -61.90549     0.0000000E+00 000300\n" +
                            "  0.7321604     -1.639468     0.0000000E+00 0.0000000E+00  1.902640     000201\n" +
                            "  0.0000000E+00 0.0000000E+00 -11.54340     -3.516100     0.0000000E+00 000102\n" +
                            " -0.1591890    -0.6822697E-01 0.0000000E+00 0.0000000E+00 0.7243801     000003\n" +
                            "   18.12898      3.317920     0.0000000E+00 0.0000000E+00  6.018142     400000\n" +
                            "   180.4548      44.21263     0.0000000E+00 0.0000000E+00  65.17458     310000\n" +
                            "   575.2233      194.3595     0.0000000E+00 0.0000000E+00  223.5009     220000\n" +
                            "   596.3884      351.0987     0.0000000E+00 0.0000000E+00  245.4161     130000\n" +
                            "  -18.96124      214.2477     0.0000000E+00 0.0000000E+00 -6.465718     040000\n" +
                            "  0.0000000E+00 0.0000000E+00  17.26235      9.114681     0.0000000E+00 301000\n" +
                            "  0.0000000E+00 0.0000000E+00  74.40365      55.42981     0.0000000E+00 211000\n" +
                            "  0.0000000E+00 0.0000000E+00 -23.61959      82.90388     0.0000000E+00 121000\n" +
                            "  0.0000000E+00 0.0000000E+00 -274.6832     -3.398815     0.0000000E+00 031000\n" +
                            "   2.141175      3.943575     0.0000000E+00 0.0000000E+00 -6.929305     202000\n" +
                            "   2.263162      16.89803     0.0000000E+00 0.0000000E+00 -47.60588     112000\n" +
                            "  -19.52964      13.62187     0.0000000E+00 0.0000000E+00 -81.10684     022000\n" +
                            "  0.0000000E+00 0.0000000E+00 -37.53170     -13.85001     0.0000000E+00 103000\n" +
                            "  0.0000000E+00 0.0000000E+00 -104.5001     -38.26539     0.0000000E+00 013000\n" +
                            "  -5.030125     -1.727285     0.0000000E+00 0.0000000E+00 -4.004471     004000\n" +
                            "  0.0000000E+00 0.0000000E+00  22.45129      17.44843     0.0000000E+00 300100\n" +
                            "  0.0000000E+00 0.0000000E+00  76.54417      101.9230     0.0000000E+00 210100\n" +
                            "  0.0000000E+00 0.0000000E+00 -172.4013      126.5196     0.0000000E+00 120100\n" +
                            "  0.0000000E+00 0.0000000E+00 -597.5882     -63.21580     0.0000000E+00 030100\n" +
                            "   39.95218      19.04921     0.0000000E+00 0.0000000E+00 -27.49024     201100\n" +
                            "   227.3753      89.79439     0.0000000E+00 0.0000000E+00 -142.3336     111100\n" +
                            "   278.7456      88.58888     0.0000000E+00 0.0000000E+00 -189.8766     021100\n" +
                            "  0.0000000E+00 0.0000000E+00 -229.7691     -92.55951     0.0000000E+00 102100\n" +
                            "  0.0000000E+00 0.0000000E+00 -620.2126     -249.2671     0.0000000E+00 012100\n" +
                            "  -47.46719     -16.47787     0.0000000E+00 0.0000000E+00 -24.37730     003100\n" +
                            "  -10.94819     0.3088420E-01 0.0000000E+00 0.0000000E+00 -4.253626     300001\n" +
                            "  -127.8440     -26.86134     0.0000000E+00 0.0000000E+00 -47.27273     210001\n" +
                            "  -373.7820     -121.7366     0.0000000E+00 0.0000000E+00 -119.7841     120001\n" +
                            "  -276.4219     -126.8492     0.0000000E+00 0.0000000E+00 -51.26515     030001\n" +
                            "  0.0000000E+00 0.0000000E+00 -10.14316     -4.875687     0.0000000E+00 201001\n" +
                            "  0.0000000E+00 0.0000000E+00 -16.55421     -18.21549     0.0000000E+00 111001\n" +
                            "  0.0000000E+00 0.0000000E+00  80.35380      4.468398     0.0000000E+00 021001\n" +
                            "  -2.134636     -2.463744     0.0000000E+00 0.0000000E+00  8.688506     102001\n" +
                            "  -6.085212     -7.154235     0.0000000E+00 0.0000000E+00  28.83058     012001\n" +
                            "  0.0000000E+00 0.0000000E+00  22.52543      5.993866     0.0000000E+00 003001\n" +
                            "   88.15750      23.30294     0.0000000E+00 0.0000000E+00 -23.61084     200200\n" +
                            "   531.5376      124.8326     0.0000000E+00 0.0000000E+00 -76.44111     110200\n" +
                            "   725.6841      143.7786     0.0000000E+00 0.0000000E+00 -35.17678     020200\n" +
                            "  0.0000000E+00 0.0000000E+00 -513.8995     -209.6013     0.0000000E+00 101200\n" +
                            "  0.0000000E+00 0.0000000E+00 -1376.134     -556.3974     0.0000000E+00 011200\n" +
                            "  -171.9905     -59.33136     0.0000000E+00 0.0000000E+00 -48.07080     002200\n" +
                            "  0.0000000E+00 0.0000000E+00 -6.264705     -7.545440     0.0000000E+00 200101\n" +
                            "  0.0000000E+00 0.0000000E+00  80.04111     -4.488641     0.0000000E+00 110101\n" +
                            "  0.0000000E+00 0.0000000E+00  369.1440      83.21059     0.0000000E+00 020101\n" +
                            "  -15.01468     -11.93357     0.0000000E+00 0.0000000E+00  36.08813     101101\n" +
                            "  -62.41840     -37.88037     0.0000000E+00 0.0000000E+00  97.47751     011101\n" +
                            "  0.0000000E+00 0.0000000E+00  192.6120      59.91357     0.0000000E+00 002101\n" +
                            "   2.813400    -0.1412055     0.0000000E+00 0.0000000E+00  1.118685     200002\n" +
                            "   34.56994      8.283359     0.0000000E+00 0.0000000E+00  9.175863     110002\n" +
                            "   48.65223      17.77179     0.0000000E+00 0.0000000E+00  9.291153     020002\n" +
                            "  0.0000000E+00 0.0000000E+00 -2.769877     0.1990435     0.0000000E+00 101002\n" +
                            "  0.0000000E+00 0.0000000E+00 -25.83070     -3.729389     0.0000000E+00 011002\n" +
                            " -0.6812118     0.1927880     0.0000000E+00 0.0000000E+00 -2.824484     002002\n" +
                            "  0.0000000E+00 0.0000000E+00 -437.9026     -166.0849     0.0000000E+00 100300\n" +
                            "  0.0000000E+00 0.0000000E+00 -1175.744     -440.0582     0.0000000E+00 010300\n" +
                            "  -279.9770     -95.66543     0.0000000E+00 0.0000000E+00 -21.15623     001300\n" +
                            "  -32.42826     -17.55875     0.0000000E+00 0.0000000E+00  19.11433     100201\n" +
                            "  -134.0710     -61.62317     0.0000000E+00 0.0000000E+00  26.07827     010201\n" +
                            "  0.0000000E+00 0.0000000E+00  543.4579      180.3961     0.0000000E+00 001201\n" +
                            "  0.0000000E+00 0.0000000E+00 -27.20481     -4.696088     0.0000000E+00 100102\n" +
                            "  0.0000000E+00 0.0000000E+00 -136.1831     -30.84270     0.0000000E+00 010102\n" +
                            "  -6.965809     0.2706859     0.0000000E+00 0.0000000E+00 -11.25098     001102\n" +
                            "  -1.101333    -0.2344258     0.0000000E+00 0.0000000E+00-0.1530302     100003\n" +
                            "  -5.077970     -2.123127     0.0000000E+00 0.0000000E+00-0.8205732     010003\n" +
                            "  0.0000000E+00 0.0000000E+00  3.361334     0.5189320     0.0000000E+00 001003\n" +
                            "  -166.7405     -56.65944     0.0000000E+00 0.0000000E+00  17.48634     000400\n" +
                            "  0.0000000E+00 0.0000000E+00  515.8381      175.8980     0.0000000E+00 000301\n" +
                            "  -7.930581      1.057828     0.0000000E+00 0.0000000E+00 -8.795910     000202\n" +
                            "  0.0000000E+00 0.0000000E+00  15.30122      3.619479     0.0000000E+00 000103\n" +
                            "  0.1456802     0.4471147E-01 0.0000000E+00 0.0000000E+00-0.6645542     000004\n" +
                            "   9.351695     -5.024978     0.0000000E+00 0.0000000E+00  6.915273     500000\n" +
                            "   256.4419     -41.23177     0.0000000E+00 0.0000000E+00  134.1654     410000\n" +
                            "   1775.037     -103.5113     0.0000000E+00 0.0000000E+00  832.4636     320000\n" +
                            "   4935.719      9.781696     0.0000000E+00 0.0000000E+00  2171.348     230000\n" +
                            "   5404.761      364.5065     0.0000000E+00 0.0000000E+00  2209.271     140000\n" +
                            "   1257.580      343.7526     0.0000000E+00 0.0000000E+00  374.6762     050000\n" +
                            "  0.0000000E+00 0.0000000E+00  53.65476      13.29929     0.0000000E+00 401000\n" +
                            "  0.0000000E+00 0.0000000E+00  512.5278      153.3587     0.0000000E+00 311000\n" +
                            "  0.0000000E+00 0.0000000E+00  1538.283      592.3759     0.0000000E+00 221000\n" +
                            "  0.0000000E+00 0.0000000E+00  1287.338      890.4996     0.0000000E+00 131000\n" +
                            "  0.0000000E+00 0.0000000E+00 -549.1122      379.7353     0.0000000E+00 041000\n" +
                            "  -1.009584      6.619283     0.0000000E+00 0.0000000E+00  3.699774     302000\n" +
                            "  -42.71018      55.33411     0.0000000E+00 0.0000000E+00 -30.04609     212000\n" +
                            "  -289.3966      134.3316     0.0000000E+00 0.0000000E+00 -293.3505     122000\n" +
                            "  -480.1605      92.85485     0.0000000E+00 0.0000000E+00 -506.1014     032000\n" +
                            "  0.0000000E+00 0.0000000E+00 -56.66662     -10.76533     0.0000000E+00 203000\n" +
                            "  0.0000000E+00 0.0000000E+00 -389.5644     -80.58828     0.0000000E+00 113000\n" +
                            "  0.0000000E+00 0.0000000E+00 -679.2450     -153.6222     0.0000000E+00 023000\n" +
                            "  -12.27741    -0.7254777E-02 0.0000000E+00 0.0000000E+00 -36.68815     104000\n" +
                            "  -35.16171     -1.489841     0.0000000E+00 0.0000000E+00 -105.0886     014000\n" +
                            "  0.0000000E+00 0.0000000E+00 -35.93213     -12.53483     0.0000000E+00 005000\n" +
                            "  0.0000000E+00 0.0000000E+00  94.98606      30.28079     0.0000000E+00 400100\n" +
                            "  0.0000000E+00 0.0000000E+00  861.9646      345.3865     0.0000000E+00 310100\n" +
                            "  0.0000000E+00 0.0000000E+00  2383.458      1316.364     0.0000000E+00 220100\n" +
                            "  0.0000000E+00 0.0000000E+00  1447.695      1927.060     0.0000000E+00 130100\n" +
                            "  0.0000000E+00 0.0000000E+00 -1647.964      755.8765     0.0000000E+00 040100\n" +
                            "  -36.48991      28.17626     0.0000000E+00 0.0000000E+00 -2.698188     301100\n" +
                            "  -135.8936      239.8741     0.0000000E+00 0.0000000E+00 -127.0906     211100\n" +
                            "  -160.5842      589.7922     0.0000000E+00 0.0000000E+00 -740.8581     121100\n" +
                            "  -76.21091      421.1232     0.0000000E+00 0.0000000E+00 -1136.566     031100\n" +
                            "  0.0000000E+00 0.0000000E+00 -308.6848     -76.85820     0.0000000E+00 202100\n" +
                            "  0.0000000E+00 0.0000000E+00 -1982.433     -516.5276     0.0000000E+00 112100\n" +
                            "  0.0000000E+00 0.0000000E+00 -3399.147     -951.5061     0.0000000E+00 022100\n" +
                            "  -89.05291     -4.332644     0.0000000E+00 0.0000000E+00 -295.2547     103100\n" +
                            "  -234.5515     -21.40360     0.0000000E+00 0.0000000E+00 -830.4988     013100\n" +
                            "  0.0000000E+00 0.0000000E+00 -370.3752     -139.1031     0.0000000E+00 004100\n" +
                            "   4.208898      5.317738     0.0000000E+00 0.0000000E+00 -2.120134     400001\n" +
                            "  -171.9416      19.41725     0.0000000E+00 0.0000000E+00 -106.2753     310001\n" +
                            "  -1369.623     -85.82171     0.0000000E+00 0.0000000E+00 -710.2000     220001\n" +
                            "  -3118.378     -450.4159     0.0000000E+00 0.0000000E+00 -1542.604     130001\n" +
                            "  -1981.856     -475.9165     0.0000000E+00 0.0000000E+00 -946.2356     040001\n" +
                            "  0.0000000E+00 0.0000000E+00 -47.26947     -8.788657     0.0000000E+00 301001\n" +
                            "  0.0000000E+00 0.0000000E+00 -412.5560     -115.0922     0.0000000E+00 211001\n" +
                            "  0.0000000E+00 0.0000000E+00 -885.9622     -344.4674     0.0000000E+00 121001\n" +
                            "  0.0000000E+00 0.0000000E+00 -236.9579     -239.4648     0.0000000E+00 031001\n" +
                            "   2.976938     -5.439589     0.0000000E+00 0.0000000E+00  8.684833     202001\n" +
                            "   20.31519     -41.56493     0.0000000E+00 0.0000000E+00  98.22027     112001\n" +
                            "   82.02262     -57.95535     0.0000000E+00 0.0000000E+00  230.2952     022001\n" +
                            "  0.0000000E+00 0.0000000E+00  73.15483      16.06227     0.0000000E+00 103001\n" +
                            "  0.0000000E+00 0.0000000E+00  252.6725      61.65120     0.0000000E+00 013001\n" +
                            "   7.035726     0.7462321     0.0000000E+00 0.0000000E+00  17.17047     004001\n" +
                            "  -56.13444      27.93332     0.0000000E+00 0.0000000E+00 -6.657459     300200\n" +
                            "   99.91151      268.8112     0.0000000E+00 0.0000000E+00 -10.65677     210200\n" +
                            "   1566.005      728.3037     0.0000000E+00 0.0000000E+00  30.66076     120200\n" +
                            "   2520.363      588.4950     0.0000000E+00 0.0000000E+00  28.35008     030200\n" +
                            "  0.0000000E+00 0.0000000E+00 -518.4654     -177.1870     0.0000000E+00 201200\n" +
                            "  0.0000000E+00 0.0000000E+00 -3244.564     -1082.802     0.0000000E+00 111200\n" +
                            "  0.0000000E+00 0.0000000E+00 -5799.239     -1958.188     0.0000000E+00 021200\n" +
                            "  -267.8896     -27.85343     0.0000000E+00 0.0000000E+00 -928.9875     102200\n" +
                            "  -639.0933     -95.79749     0.0000000E+00 0.0000000E+00 -2586.156     012200\n" +
                            "  0.0000000E+00 0.0000000E+00 -1596.873     -627.0988     0.0000000E+00 003200\n" +
                            "  0.0000000E+00 0.0000000E+00 -105.7184     -30.68690     0.0000000E+00 300101\n" +
                            "  0.0000000E+00 0.0000000E+00 -778.2704     -313.3389     0.0000000E+00 210101\n" +
                            "  0.0000000E+00 0.0000000E+00 -1255.652     -799.4862     0.0000000E+00 120101\n" +
                            "  0.0000000E+00 0.0000000E+00  363.9199     -414.9288     0.0000000E+00 030101\n" +
                            "   24.49435     -34.47794     0.0000000E+00 0.0000000E+00  83.05195     201101\n" +
                            "  -16.15464     -237.8026     0.0000000E+00 0.0000000E+00  590.3017     111101\n" +
                            "  -48.07230     -320.3512     0.0000000E+00 0.0000000E+00  1027.420     021101\n" +
                            "  0.0000000E+00 0.0000000E+00  574.0025      167.2288     0.0000000E+00 102101\n" +
                            "  0.0000000E+00 0.0000000E+00  1826.419      561.7547     0.0000000E+00 012101\n" +
                            "   95.72779      18.10406     0.0000000E+00 0.0000000E+00  144.1820     003101\n" +
                            "  -2.643939     -2.329052     0.0000000E+00 0.0000000E+00-0.4560027     300002\n" +
                            "   89.00516      4.807826     0.0000000E+00 0.0000000E+00  37.25716     210002\n" +
                            "   527.8356      98.88446     0.0000000E+00 0.0000000E+00  191.7855     120002\n" +
                            "   656.7013      166.0370     0.0000000E+00 0.0000000E+00  188.9707     030002\n" +
                            "  0.0000000E+00 0.0000000E+00  17.48490      3.491127     0.0000000E+00 201002\n" +
                            "  0.0000000E+00 0.0000000E+00  99.09008      31.11892     0.0000000E+00 111002\n" +
                            "  0.0000000E+00 0.0000000E+00  35.37821      30.48470     0.0000000E+00 021002\n" +
                            "   3.283940      2.993715     0.0000000E+00 0.0000000E+00 -14.74541     102002\n" +
                            "   15.67303      12.77274     0.0000000E+00 0.0000000E+00 -57.49435     012002\n" +
                            "  0.0000000E+00 0.0000000E+00 -34.69124     -7.386499     0.0000000E+00 003002\n" +
                            "  0.0000000E+00 0.0000000E+00 -277.1488     -134.6385     0.0000000E+00 200300\n" +
                            "  0.0000000E+00 0.0000000E+00 -1839.412     -770.6867     0.0000000E+00 110300\n" +
                            "  0.0000000E+00 0.0000000E+00 -3732.341     -1405.684     0.0000000E+00 020300\n" +
                            "  -396.6717     -59.36062     0.0000000E+00 0.0000000E+00 -1329.174     101300\n" +
                            "  -842.6078     -171.2565     0.0000000E+00 0.0000000E+00 -3661.554     011300\n" +
                            "  0.0000000E+00 0.0000000E+00 -3638.570     -1452.282     0.0000000E+00 002300\n" +
                            "  -17.21466     -49.86510     0.0000000E+00 0.0000000E+00  124.4926     200201\n" +
                            "  -543.6056     -346.6137     0.0000000E+00 0.0000000E+00  672.1010     110201\n" +
                            "  -1126.828     -484.4955     0.0000000E+00 0.0000000E+00  863.5836     020201\n" +
                            "  0.0000000E+00 0.0000000E+00  1457.102      507.7769     0.0000000E+00 101201\n" +
                            "  0.0000000E+00 0.0000000E+00  4457.001      1587.460     0.0000000E+00 011201\n" +
                            "   452.0618      102.8103     0.0000000E+00 0.0000000E+00  427.6763     002201\n" +
                            "  0.0000000E+00 0.0000000E+00  36.27394      11.84016     0.0000000E+00 200102\n" +
                            "  0.0000000E+00 0.0000000E+00  100.4631      63.05481     0.0000000E+00 110102\n" +
                            "  0.0000000E+00 0.0000000E+00 -288.3633     -8.313009     0.0000000E+00 020102\n" +
                            "   16.94073      20.00290     0.0000000E+00 0.0000000E+00 -90.90264     101102\n" +
                            "   109.8945      78.81428     0.0000000E+00 0.0000000E+00 -298.2533     011102\n" +
                            "  0.0000000E+00 0.0000000E+00 -338.3842     -88.20741     0.0000000E+00 002102\n" +
                            "  0.8054174     0.6740243     0.0000000E+00 0.0000000E+00 0.7082526E-01 200003\n" +
                            "  -24.46162     -3.908173     0.0000000E+00 0.0000000E+00 -7.975659     110003\n" +
                            "  -59.99010     -16.57382     0.0000000E+00 0.0000000E+00 -18.91620     020003\n" +
                            "  0.0000000E+00 0.0000000E+00  1.655894    -0.7780217E-01 0.0000000E+00 101003\n" +
                            "  0.0000000E+00 0.0000000E+00  20.08631      2.003389     0.0000000E+00 011003\n" +
                            "  0.2647752    -0.3581956     0.0000000E+00 0.0000000E+00  4.895692     002003\n" +
                            "  -235.7519     -39.32494     0.0000000E+00 0.0000000E+00 -691.3778     100400\n" +
                            "  -430.2797     -96.77417     0.0000000E+00 0.0000000E+00 -1874.721     010400\n" +
                            "  0.0000000E+00 0.0000000E+00 -4426.474     -1744.055     0.0000000E+00 001400\n" +
                            "  0.0000000E+00 0.0000000E+00  1320.630      492.0711     0.0000000E+00 100301\n" +
                            "  0.0000000E+00 0.0000000E+00  4012.165      1484.199     0.0000000E+00 010301\n" +
                            "   915.0964      223.6695     0.0000000E+00 0.0000000E+00  506.4135     001301\n" +
                            "   37.70205      31.59027     0.0000000E+00 0.0000000E+00 -103.0266     100202\n" +
                            "   241.7902      127.3143     0.0000000E+00 0.0000000E+00 -275.5801     010202\n" +
                            "  0.0000000E+00 0.0000000E+00 -1067.901     -311.7429     0.0000000E+00 001202\n" +
                            "  0.0000000E+00 0.0000000E+00  25.33945      3.774058     0.0000000E+00 100103\n" +
                            "  0.0000000E+00 0.0000000E+00  148.6672      28.13443     0.0000000E+00 010103\n" +
                            "   7.943199     -1.430829     0.0000000E+00 0.0000000E+00  25.47239     001103\n" +
                            "  0.5722921     0.8523766E-01 0.0000000E+00 0.0000000E+00-0.3992170E-01 100004\n" +
                            "   5.516179      1.787786     0.0000000E+00 0.0000000E+00 0.7264355     010004\n" +
                            "  0.0000000E+00 0.0000000E+00 -3.595266    -0.5123128     0.0000000E+00 001004\n" +
                            "  0.0000000E+00 0.0000000E+00 -2307.390     -875.4576     0.0000000E+00 000500\n" +
                            "   668.8906      167.6050     0.0000000E+00 0.0000000E+00  168.7212     000401\n" +
                            "  0.0000000E+00 0.0000000E+00 -1130.388     -347.7893     0.0000000E+00 000302\n" +
                            "   15.60757     -1.871920     0.0000000E+00 0.0000000E+00  26.92251     000203\n" +
                            "  0.0000000E+00 0.0000000E+00 -18.77208     -3.828155     0.0000000E+00 000104\n" +
                            " -0.1173845    -0.3223699E-01 0.0000000E+00 0.0000000E+00 0.6357264     000005"
            );

            part2.map = CosyArbitraryOrder.readMap(
                    "   1.017265     0.6921644     0.0000000E+00 0.0000000E+00 0.3465012E-01 100000\n" +
                            "  0.5031897E-01  1.017265     0.0000000E+00 0.0000000E+00 0.8643179E-03 010000\n" +
                            "  0.0000000E+00 0.0000000E+00-0.9608377    -0.2377623     0.0000000E+00 001000\n" +
                            "  0.0000000E+00 0.0000000E+00 0.3229734    -0.9608377     0.0000000E+00 000100\n" +
                            "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00  1.000000     000010\n" +
                            " -0.8643179E-03-0.3465012E-01 0.0000000E+00 0.0000000E+00  1.224463     000001\n" +
                            "  -2.632360    -0.5084484     0.0000000E+00 0.0000000E+00-0.2792015     200000\n" +
                            "  -7.700847    -0.2536563E-01 0.0000000E+00 0.0000000E+00-0.1488584     110000\n" +
                            " -0.9604555E-01  3.784445     0.0000000E+00 0.0000000E+00-0.9010819E-02 020000\n" +
                            "  0.0000000E+00 0.0000000E+00 -5.847688    -0.1204154     0.0000000E+00 101000\n" +
                            "  0.0000000E+00 0.0000000E+00 -18.69544    -0.3003660E-02 0.0000000E+00 011000\n" +
                            "   2.227371     0.8890276     0.0000000E+00 0.0000000E+00-0.2660706     002000\n" +
                            "  0.0000000E+00 0.0000000E+00  4.459451      6.991669     0.0000000E+00 100100\n" +
                            "  0.0000000E+00 0.0000000E+00 0.1112372      18.72398     0.0000000E+00 010100\n" +
                            "   17.98973      6.678968     0.0000000E+00 0.0000000E+00 0.3130270     001100\n" +
                            "  0.1389546     0.4650095     0.0000000E+00 0.0000000E+00 0.1916578     100001\n" +
                            " -0.1084238E-01-0.1233303     0.0000000E+00 0.0000000E+00 0.4780741E-02 010001\n" +
                            "  0.0000000E+00 0.0000000E+00 0.5138444    -0.5857273     0.0000000E+00 001001\n" +
                            "  -2.965625      1.198081     0.0000000E+00 0.0000000E+00-0.4480868     000200\n" +
                            "  0.0000000E+00 0.0000000E+00 0.7599781    -0.1289006     0.0000000E+00 000101\n" +
                            " -0.2142475E-02-0.9349123E-01 0.0000000E+00 0.0000000E+00 -1.168427     000002\n" +
                            "   7.303700     0.2972869     0.0000000E+00 0.0000000E+00-0.8163623E-01 300000\n" +
                            "   10.48050     -7.169097     0.0000000E+00 0.0000000E+00 -2.372058     210000\n" +
                            "  -52.23958     -26.69790     0.0000000E+00 0.0000000E+00 -7.253035     120000\n" +
                            "  -65.47398     -8.475802     0.0000000E+00 0.0000000E+00 -1.308431     030000\n" +
                            "  0.0000000E+00 0.0000000E+00 -28.10651     -11.21845     0.0000000E+00 201000\n" +
                            "  0.0000000E+00 0.0000000E+00 -171.3050     -67.05745     0.0000000E+00 111000\n" +
                            "  0.0000000E+00 0.0000000E+00 -250.5792     -98.39637     0.0000000E+00 021000\n" +
                            "  -17.49854     -5.504906     0.0000000E+00 0.0000000E+00 -4.228376     102000\n" +
                            "  -51.89770     -15.89008     0.0000000E+00 0.0000000E+00 -10.93537     012000\n" +
                            "  0.0000000E+00 0.0000000E+00 -5.298245      1.052880     0.0000000E+00 003000\n" +
                            "  0.0000000E+00 0.0000000E+00 -50.57365     -22.62980     0.0000000E+00 200100\n" +
                            "  0.0000000E+00 0.0000000E+00 -290.2912     -127.9547     0.0000000E+00 110100\n" +
                            "  0.0000000E+00 0.0000000E+00 -398.5765     -179.2955     0.0000000E+00 020100\n" +
                            "   4.453472    -0.9419992     0.0000000E+00 0.0000000E+00 -15.01121     101100\n" +
                            "   57.28884      13.62001     0.0000000E+00 0.0000000E+00 -32.28938     011100\n" +
                            "  0.0000000E+00 0.0000000E+00 -32.27073      4.522809     0.0000000E+00 002100\n" +
                            "  -6.261379     -1.006326     0.0000000E+00 0.0000000E+00 0.4936947     200001\n" +
                            "  -10.89186     0.8680335     0.0000000E+00 0.0000000E+00  2.190324     110001\n" +
                            "   3.242980      6.646452     0.0000000E+00 0.0000000E+00  4.231188     020001\n" +
                            "  0.0000000E+00 0.0000000E+00  14.01526      1.446800     0.0000000E+00 101001\n" +
                            "  0.0000000E+00 0.0000000E+00  46.83508      3.388477     0.0000000E+00 011001\n" +
                            "   1.839261      1.388873     0.0000000E+00 0.0000000E+00  1.613606     002001\n" +
                            "   109.0086      28.38320     0.0000000E+00 0.0000000E+00  3.483178     100200\n" +
                            "   461.4163      125.0338     0.0000000E+00 0.0000000E+00  21.14550     010200\n" +
                            "  0.0000000E+00 0.0000000E+00  15.15311      45.54277     0.0000000E+00 001200\n" +
                            "  0.0000000E+00 0.0000000E+00  11.36879     -4.351057     0.0000000E+00 100101\n" +
                            "  0.0000000E+00 0.0000000E+00  13.82010     -31.96263     0.0000000E+00 010101\n" +
                            "  -41.22122     -7.870274     0.0000000E+00 0.0000000E+00  3.909036     001101\n" +
                            "   1.984528     0.1708608     0.0000000E+00 0.0000000E+00-0.3454763     100002\n" +
                            "   4.129998     0.8459336     0.0000000E+00 0.0000000E+00-0.8150627E-01 010002\n" +
                            "  0.0000000E+00 0.0000000E+00 -1.144273     0.9016516     0.0000000E+00 001002\n" +
                            "  0.0000000E+00 0.0000000E+00 -31.77360      10.68112     0.0000000E+00 000300\n" +
                            "  -8.823879     0.4936454E-01 0.0000000E+00 0.0000000E+00  10.51266     000201\n" +
                            "  0.0000000E+00 0.0000000E+00 -11.31594     -1.564621     0.0000000E+00 000102\n" +
                            " -0.6805895E-01 0.9724053E-01 0.0000000E+00 0.0000000E+00  1.116018     000003\n" +
                            "  -1.587395     -3.205867     0.0000000E+00 0.0000000E+00 -2.821251     400000\n" +
                            "   50.28359     -13.85549     0.0000000E+00 0.0000000E+00 -10.85323     310000\n" +
                            "   281.0847     -25.48142     0.0000000E+00 0.0000000E+00 -10.82708     220000\n" +
                            "   317.8258     -68.40979     0.0000000E+00 0.0000000E+00 -15.44002     130000\n" +
                            "  -115.9443     -107.6592     0.0000000E+00 0.0000000E+00 -27.94380     040000\n" +
                            "  0.0000000E+00 0.0000000E+00 -14.07864     -4.450054     0.0000000E+00 301000\n" +
                            "  0.0000000E+00 0.0000000E+00 -257.8399     -72.49559     0.0000000E+00 211000\n" +
                            "  0.0000000E+00 0.0000000E+00 -1074.912     -299.3150     0.0000000E+00 121000\n" +
                            "  0.0000000E+00 0.0000000E+00 -1315.633     -369.6722     0.0000000E+00 031000\n" +
                            "  -17.98481     -8.481272     0.0000000E+00 0.0000000E+00 -2.291793     202000\n" +
                            "  -296.9986     -109.0892     0.0000000E+00 0.0000000E+00 -32.32367     112000\n" +
                            "  -705.4176     -248.0782     0.0000000E+00 0.0000000E+00 -76.95117     022000\n" +
                            "  0.0000000E+00 0.0000000E+00 -65.43069     -8.487714     0.0000000E+00 103000\n" +
                            "  0.0000000E+00 0.0000000E+00 -162.6362     -20.18191     0.0000000E+00 013000\n" +
                            "  -15.34679     -5.631887     0.0000000E+00 0.0000000E+00 -5.283869     004000\n" +
                            "  0.0000000E+00 0.0000000E+00 -93.82918     -28.70995     0.0000000E+00 300100\n" +
                            "  0.0000000E+00 0.0000000E+00 -799.4718     -214.8211     0.0000000E+00 210100\n" +
                            "  0.0000000E+00 0.0000000E+00 -2110.010     -513.7719     0.0000000E+00 120100\n" +
                            "  0.0000000E+00 0.0000000E+00 -1676.001     -359.1322     0.0000000E+00 030100\n" +
                            "  0.8727969     -17.05790     0.0000000E+00 0.0000000E+00 -42.24453     201100\n" +
                            "  -1053.297     -420.5431     0.0000000E+00 0.0000000E+00 -352.2178     111100\n" +
                            "  -3153.122     -1120.978     0.0000000E+00 0.0000000E+00 -665.6986     021100\n" +
                            "  0.0000000E+00 0.0000000E+00 -631.1138     -162.9541     0.0000000E+00 102100\n" +
                            "  0.0000000E+00 0.0000000E+00 -1751.036     -492.6020     0.0000000E+00 012100\n" +
                            "   16.79003     -5.715161     0.0000000E+00 0.0000000E+00 -39.76487     003100\n" +
                            "  0.9723234      2.706560     0.0000000E+00 0.0000000E+00  2.876897     300001\n" +
                            "  -41.97846      7.642963     0.0000000E+00 0.0000000E+00  12.54475     210001\n" +
                            "  -120.8102      19.20285     0.0000000E+00 0.0000000E+00  23.25398     120001\n" +
                            "  -96.04626      4.895323     0.0000000E+00 0.0000000E+00  10.25057     030001\n" +
                            "  0.0000000E+00 0.0000000E+00  62.75404      19.70072     0.0000000E+00 201001\n" +
                            "  0.0000000E+00 0.0000000E+00  510.4805      160.6025     0.0000000E+00 111001\n" +
                            "  0.0000000E+00 0.0000000E+00  956.5669      304.3299     0.0000000E+00 021001\n" +
                            "   52.92190      14.85797     0.0000000E+00 0.0000000E+00  9.686531     102001\n" +
                            "   217.2882      59.43039     0.0000000E+00 0.0000000E+00  31.81921     012001\n" +
                            "  0.0000000E+00 0.0000000E+00  13.35982     -5.461604     0.0000000E+00 003001\n" +
                            "  -64.78524     -56.97721     0.0000000E+00 0.0000000E+00 -147.2986     200200\n" +
                            "  -1864.529     -748.4855     0.0000000E+00 0.0000000E+00 -919.5582     110200\n" +
                            "  -4958.540     -1689.673     0.0000000E+00 0.0000000E+00 -1421.979     020200\n" +
                            "  0.0000000E+00 0.0000000E+00 -1213.432     -293.2180     0.0000000E+00 101200\n" +
                            "  0.0000000E+00 0.0000000E+00 -3518.966     -971.3277     0.0000000E+00 011200\n" +
                            "   328.2211      52.30814     0.0000000E+00 0.0000000E+00 -118.0013     002200\n" +
                            "  0.0000000E+00 0.0000000E+00  233.7926      81.36306     0.0000000E+00 200101\n" +
                            "  0.0000000E+00 0.0000000E+00  1530.728      523.9273     0.0000000E+00 110101\n" +
                            "  0.0000000E+00 0.0000000E+00  2552.123      879.4967     0.0000000E+00 020101\n" +
                            "  -103.5728     -30.37779     0.0000000E+00 0.0000000E+00  34.86870     101101\n" +
                            "  -222.0690     -80.29114     0.0000000E+00 0.0000000E+00  80.17028     011101\n" +
                            "  0.0000000E+00 0.0000000E+00  205.5689      12.03162     0.0000000E+00 002101\n" +
                            "   1.701157    -0.7637098     0.0000000E+00 0.0000000E+00 -1.630169     200002\n" +
                            "   15.94524    -0.2170977     0.0000000E+00 0.0000000E+00 -5.536559     110002\n" +
                            "   6.589599     -7.833222     0.0000000E+00 0.0000000E+00 -10.03418     020002\n" +
                            "  0.0000000E+00 0.0000000E+00 -11.57935     0.8797316     0.0000000E+00 101002\n" +
                            "  0.0000000E+00 0.0000000E+00 -60.70702     -1.148373     0.0000000E+00 011002\n" +
                            "  -14.36578     -5.193016     0.0000000E+00 0.0000000E+00 -4.132907     002002\n" +
                            "  0.0000000E+00 0.0000000E+00  60.35552      200.4579     0.0000000E+00 100300\n" +
                            "  0.0000000E+00 0.0000000E+00  1005.809      917.5140     0.0000000E+00 010300\n" +
                            "   1192.091      326.6177     0.0000000E+00 0.0000000E+00 -52.21062     001300\n" +
                            "  -735.1379     -201.2135     0.0000000E+00 0.0000000E+00 -32.09662     100201\n" +
                            "  -2371.145     -656.0558     0.0000000E+00 0.0000000E+00 -168.3564     010201\n" +
                            "  0.0000000E+00 0.0000000E+00  149.1212     -108.6497     0.0000000E+00 001201\n" +
                            "  0.0000000E+00 0.0000000E+00 -36.44760    -0.5635066     0.0000000E+00 100102\n" +
                            "  0.0000000E+00 0.0000000E+00 -40.35399      41.48070     0.0000000E+00 010102\n" +
                            "   85.95658      15.25476     0.0000000E+00 0.0000000E+00 -7.679723     001102\n" +
                            "  -1.298506     0.3253296     0.0000000E+00 0.0000000E+00 0.7073630     100003\n" +
                            "  -6.511441     -1.104776     0.0000000E+00 0.0000000E+00 0.2442499     010003\n" +
                            "  0.0000000E+00 0.0000000E+00 -3.191083     -3.044445     0.0000000E+00 001003\n" +
                            "   589.4090      126.7390     0.0000000E+00 0.0000000E+00 -72.85167     000400\n" +
                            "  0.0000000E+00 0.0000000E+00  372.2260      19.27965     0.0000000E+00 000301\n" +
                            "   135.4156      31.59821     0.0000000E+00 0.0000000E+00 -23.61625     000202\n" +
                            "  0.0000000E+00 0.0000000E+00  19.57036     0.7625845     0.0000000E+00 000103\n" +
                            " -0.8993929E-01-0.1884437     0.0000000E+00 0.0000000E+00 -1.142792     000004\n" +
                            "   65.41260      24.85872     0.0000000E+00 0.0000000E+00  5.779673     500000\n" +
                            "   548.2795      190.6935     0.0000000E+00 0.0000000E+00  19.57359     410000\n" +
                            "   2042.382      559.3452     0.0000000E+00 0.0000000E+00 -42.22815     320000\n" +
                            "   4770.836      956.5431     0.0000000E+00 0.0000000E+00 -179.0900     230000\n" +
                            "   6633.297      1227.303     0.0000000E+00 0.0000000E+00 -80.70200     140000\n" +
                            "   3694.124      848.8295     0.0000000E+00 0.0000000E+00  65.74558     050000\n" +
                            "  0.0000000E+00 0.0000000E+00  142.8545      41.86649     0.0000000E+00 401000\n" +
                            "  0.0000000E+00 0.0000000E+00  873.7961      222.3999     0.0000000E+00 311000\n" +
                            "  0.0000000E+00 0.0000000E+00  366.7237     -140.8600     0.0000000E+00 221000\n" +
                            "  0.0000000E+00 0.0000000E+00 -6194.973     -2428.050     0.0000000E+00 131000\n" +
                            "  0.0000000E+00 0.0000000E+00 -9851.602     -3398.783     0.0000000E+00 041000\n" +
                            "  -175.7482     -53.19220     0.0000000E+00 0.0000000E+00 -3.315118     302000\n" +
                            "  -1109.376     -327.1279     0.0000000E+00 0.0000000E+00  85.45912     212000\n" +
                            "  -2120.176     -612.0880     0.0000000E+00 0.0000000E+00  461.7952     122000\n" +
                            "  -1014.747     -271.3034     0.0000000E+00 0.0000000E+00  559.2145     032000\n" +
                            "  0.0000000E+00 0.0000000E+00 -90.68665      6.832053     0.0000000E+00 203000\n" +
                            "  0.0000000E+00 0.0000000E+00 -341.9564      141.3257     0.0000000E+00 113000\n" +
                            "  0.0000000E+00 0.0000000E+00 -226.3475      368.2760     0.0000000E+00 023000\n" +
                            "  -216.5736     -67.08658     0.0000000E+00 0.0000000E+00 -23.63100     104000\n" +
                            "  -741.3276     -224.6152     0.0000000E+00 0.0000000E+00 -68.61275     014000\n" +
                            "  0.0000000E+00 0.0000000E+00 -21.63766      9.504961     0.0000000E+00 005000\n" +
                            "  0.0000000E+00 0.0000000E+00  139.2973      30.20764     0.0000000E+00 400100\n" +
                            "  0.0000000E+00 0.0000000E+00  183.5810     -92.53095     0.0000000E+00 310100\n" +
                            "  0.0000000E+00 0.0000000E+00 -4843.538     -2060.102     0.0000000E+00 220100\n" +
                            "  0.0000000E+00 0.0000000E+00 -19561.29     -6842.601     0.0000000E+00 130100\n" +
                            "  0.0000000E+00 0.0000000E+00 -21658.13     -7035.947     0.0000000E+00 040100\n" +
                            "  -626.8536     -140.8858     0.0000000E+00 0.0000000E+00  129.3247     301100\n" +
                            "  -1147.708      41.56112     0.0000000E+00 0.0000000E+00  1716.515     211100\n" +
                            "   6581.918      2749.223     0.0000000E+00 0.0000000E+00  5841.468     121100\n" +
                            "   14094.64      4427.257     0.0000000E+00 0.0000000E+00  5769.987     031100\n" +
                            "  0.0000000E+00 0.0000000E+00 -1525.783     -406.1823     0.0000000E+00 202100\n" +
                            "  0.0000000E+00 0.0000000E+00 -7880.568     -1833.816     0.0000000E+00 112100\n" +
                            "  0.0000000E+00 0.0000000E+00 -9894.721     -1726.613     0.0000000E+00 022100\n" +
                            "  -514.3460     -155.2563     0.0000000E+00 0.0000000E+00 -202.4951     103100\n" +
                            "  -2669.113     -724.9666     0.0000000E+00 0.0000000E+00 -567.3012     013100\n" +
                            "  0.0000000E+00 0.0000000E+00 -614.9744     -91.19575     0.0000000E+00 004100\n" +
                            "  -50.40670     -21.29831     0.0000000E+00 0.0000000E+00 -3.177107     400001\n" +
                            "  -329.1895     -119.4390     0.0000000E+00 0.0000000E+00 -8.184038     310001\n" +
                            "  -997.3972     -205.0203     0.0000000E+00 0.0000000E+00  17.64387     220001\n" +
                            "  -1587.991     -73.44429     0.0000000E+00 0.0000000E+00  91.72950     130001\n" +
                            "  -732.3783      140.8289     0.0000000E+00 0.0000000E+00  116.3877     040001\n" +
                            "  0.0000000E+00 0.0000000E+00 -138.3592     -47.61239     0.0000000E+00 301001\n" +
                            "  0.0000000E+00 0.0000000E+00 -534.5806     -200.8986     0.0000000E+00 211001\n" +
                            "  0.0000000E+00 0.0000000E+00  615.6989      64.00236     0.0000000E+00 121001\n" +
                            "  0.0000000E+00 0.0000000E+00  2821.984      693.9822     0.0000000E+00 031001\n" +
                            "   379.7976      122.3466     0.0000000E+00 0.0000000E+00  24.86356     202001\n" +
                            "   2552.302      797.7599     0.0000000E+00 0.0000000E+00  163.3765     112001\n" +
                            "   4482.363      1403.041     0.0000000E+00 0.0000000E+00  335.7185     022001\n" +
                            "  0.0000000E+00 0.0000000E+00  44.59644     -43.16895     0.0000000E+00 103001\n" +
                            "  0.0000000E+00 0.0000000E+00  144.6430     -125.7511     0.0000000E+00 013001\n" +
                            "   121.5998      37.64766     0.0000000E+00 0.0000000E+00  20.84717     004001\n" +
                            "   78.42087      153.6765     0.0000000E+00 0.0000000E+00  369.0323     300200\n" +
                            "   7053.992      3020.344     0.0000000E+00 0.0000000E+00  3660.377     210200\n" +
                            "   32529.84      11145.15     0.0000000E+00 0.0000000E+00  10774.40     120200\n" +
                            "   37910.00      10841.96     0.0000000E+00 0.0000000E+00  9780.572     030200\n" +
                            "  0.0000000E+00 0.0000000E+00 -7061.565     -2472.124     0.0000000E+00 201200\n" +
                            "  0.0000000E+00 0.0000000E+00 -43554.38     -15403.56     0.0000000E+00 111200\n" +
                            "  0.0000000E+00 0.0000000E+00 -67785.55     -24097.05     0.0000000E+00 021200\n" +
                            "   310.4427     -150.2472     0.0000000E+00 0.0000000E+00 -1064.831     102200\n" +
                            "  -4936.014     -1939.233     0.0000000E+00 0.0000000E+00 -3267.676     012200\n" +
                            "  0.0000000E+00 0.0000000E+00 -2740.659     -477.3615     0.0000000E+00 003200\n" +
                            "  0.0000000E+00 0.0000000E+00 -41.52976     -17.65320     0.0000000E+00 300101\n" +
                            "  0.0000000E+00 0.0000000E+00  952.0737      249.5026     0.0000000E+00 210101\n" +
                            "  0.0000000E+00 0.0000000E+00  6155.724      1694.592     0.0000000E+00 120101\n" +
                            "  0.0000000E+00 0.0000000E+00  7956.540      2086.414     0.0000000E+00 030101\n" +
                            "   2056.993      632.1559     0.0000000E+00 0.0000000E+00  257.2357     201101\n" +
                            "   13028.48      3838.614     0.0000000E+00 0.0000000E+00  1685.915     111101\n" +
                            "   22455.76      6621.104     0.0000000E+00 0.0000000E+00  3090.180     021101\n" +
                            "  0.0000000E+00 0.0000000E+00  2316.688      555.0497     0.0000000E+00 102101\n" +
                            "  0.0000000E+00 0.0000000E+00  7190.835      1829.189     0.0000000E+00 012101\n" +
                            "   173.5156      79.90023     0.0000000E+00 0.0000000E+00  142.8385     003101\n" +
                            "   16.73673      6.011954     0.0000000E+00 0.0000000E+00 -2.289608     300002\n" +
                            "   94.32291      22.86085     0.0000000E+00 0.0000000E+00 -15.85807     210002\n" +
                            "   192.9676     -3.660952     0.0000000E+00 0.0000000E+00 -41.61090     120002\n" +
                            "   224.3532     -7.252282     0.0000000E+00 0.0000000E+00 -22.97372     030002\n" +
                            "  0.0000000E+00 0.0000000E+00 -21.29687      4.357583     0.0000000E+00 201002\n" +
                            "  0.0000000E+00 0.0000000E+00 -572.6578     -123.5443     0.0000000E+00 111002\n" +
                            "  0.0000000E+00 0.0000000E+00 -1620.506     -428.4577     0.0000000E+00 021002\n" +
                            "  -258.8528     -77.01837     0.0000000E+00 0.0000000E+00 -31.08382     102002\n" +
                            "  -933.4035     -263.8008     0.0000000E+00 0.0000000E+00 -105.9544     012002\n" +
                            "  0.0000000E+00 0.0000000E+00  30.54268      34.30231     0.0000000E+00 003002\n" +
                            "  0.0000000E+00 0.0000000E+00 -10919.76     -4063.045     0.0000000E+00 200300\n" +
                            "  0.0000000E+00 0.0000000E+00 -71672.22     -27618.12     0.0000000E+00 110300\n" +
                            "  0.0000000E+00 0.0000000E+00 -122176.3     -48722.44     0.0000000E+00 020300\n" +
                            "   4657.556      953.7959     0.0000000E+00 0.0000000E+00 -2179.918     101300\n" +
                            "   1060.741     -878.2088     0.0000000E+00 0.0000000E+00 -6614.776     011300\n" +
                            "  0.0000000E+00 0.0000000E+00 -5070.631     -711.8977     0.0000000E+00 002300\n" +
                            "   3048.809      867.0173     0.0000000E+00 0.0000000E+00  721.9098     200201\n" +
                            "   18522.72      5049.925     0.0000000E+00 0.0000000E+00  4483.887     110201\n" +
                            "   31213.53      8406.953     0.0000000E+00 0.0000000E+00  7413.404     020201\n" +
                            "  0.0000000E+00 0.0000000E+00  5856.299      1458.808     0.0000000E+00 101201\n" +
                            "  0.0000000E+00 0.0000000E+00  18882.44      5223.242     0.0000000E+00 011201\n" +
                            "  -1360.670     -175.2322     0.0000000E+00 0.0000000E+00  538.4340     002201\n" +
                            "  0.0000000E+00 0.0000000E+00 -311.8489     -77.31480     0.0000000E+00 200102\n" +
                            "  0.0000000E+00 0.0000000E+00 -2843.187     -805.5982     0.0000000E+00 110102\n" +
                            "  0.0000000E+00 0.0000000E+00 -6254.774     -1886.226     0.0000000E+00 020102\n" +
                            "  -643.0809     -218.1944     0.0000000E+00 0.0000000E+00 -138.3856     101102\n" +
                            "  -2017.486     -591.3749     0.0000000E+00 0.0000000E+00 -362.2681     011102\n" +
                            "  0.0000000E+00 0.0000000E+00 -490.3856     -24.81728     0.0000000E+00 002102\n" +
                            "  -5.070647    -0.5510451     0.0000000E+00 0.0000000E+00  2.573985     200003\n" +
                            "  -30.22644     -5.716594     0.0000000E+00 0.0000000E+00  9.575686     110003\n" +
                            "  -8.219862      8.997631     0.0000000E+00 0.0000000E+00  19.24076     020003\n" +
                            "  0.0000000E+00 0.0000000E+00 -17.92173     -13.68920     0.0000000E+00 101003\n" +
                            "  0.0000000E+00 0.0000000E+00  16.91681     -22.81687     0.0000000E+00 011003\n" +
                            "   55.64568      17.12399     0.0000000E+00 0.0000000E+00  10.47568     002003\n" +
                            "   10505.16      3279.797     0.0000000E+00 0.0000000E+00  130.7339     100400\n" +
                            "   26829.34      9189.742     0.0000000E+00 0.0000000E+00  1380.390     010400\n" +
                            "  0.0000000E+00 0.0000000E+00 -1352.625      988.4889     0.0000000E+00 001400\n" +
                            "  0.0000000E+00 0.0000000E+00 -334.4589     -938.0627     0.0000000E+00 100301\n" +
                            "  0.0000000E+00 0.0000000E+00 -5331.475     -4357.791     0.0000000E+00 010301\n" +
                            "  -8007.246     -2115.155     0.0000000E+00 0.0000000E+00  93.66766     001301\n" +
                            "   676.5517      106.6505     0.0000000E+00 0.0000000E+00  20.14485     100202\n" +
                            "   3095.920      721.8469     0.0000000E+00 0.0000000E+00  381.3614     010202\n" +
                            "  0.0000000E+00 0.0000000E+00 -358.6814      296.7597     0.0000000E+00 001202\n" +
                            "  0.0000000E+00 0.0000000E+00  11.91411     -18.73973     0.0000000E+00 100103\n" +
                            "  0.0000000E+00 0.0000000E+00 -67.30976     -107.1400     0.0000000E+00 010103\n" +
                            "  -58.85002      5.320432     0.0000000E+00 0.0000000E+00  12.73874     001103\n" +
                            "   1.230359    -0.5617472     0.0000000E+00 0.0000000E+00 -1.204188     100004\n" +
                            "   10.52367      1.959247     0.0000000E+00 0.0000000E+00-0.4293422     010004\n" +
                            "  0.0000000E+00 0.0000000E+00  15.65062      7.647521     0.0000000E+00 001004\n" +
                            "  0.0000000E+00 0.0000000E+00  523.9417      956.8980     0.0000000E+00 000500\n" +
                            "  -6183.396     -1601.051     0.0000000E+00 0.0000000E+00  254.5651     000401\n" +
                            "  0.0000000E+00 0.0000000E+00 -736.8262      170.9571     0.0000000E+00 000302\n" +
                            "  -323.0757     -64.58157     0.0000000E+00 0.0000000E+00  34.70359     000203\n" +
                            "  0.0000000E+00 0.0000000E+00 -14.39169      5.873751     0.0000000E+00 000104\n" +
                            "  0.3617380     0.3103731     0.0000000E+00 0.0000000E+00  1.242357     000005"
            );
        }
    }
}
