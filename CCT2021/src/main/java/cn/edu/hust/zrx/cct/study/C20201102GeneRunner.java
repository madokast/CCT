package cn.edu.hust.zrx.cct.study;

import cn.edu.hust.zrx.cct.advanced.CctUtils;
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
import cn.edu.hust.zrx.cct.base.point.Point2;
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
 * C20201102外部算法
 * <p>
 * Data
 * 8:46
 *
 * @author zrx
 * @version 1.0
 */

public class C20201102GeneRunner {

    public static void main(String[] args) throws IOException {
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
        List<double[]> results = datas.stream().parallel()
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

    private static double[] readInput(BufferedReader in) throws IOException {
        double[] data = new double[10];
        for (int i = 0; i < data.length; i++) {
            data[i] = Double.parseDouble(in.readLine());
        }
        return data;
    }

    // 10个变量
    public static GantryDataBipolarCo.SecondBend create(double[] data) {
        GantryDataBipolarCo.SecondBend secondBend = GantryDataBipolarCo.SecondBend.getDefault();

        secondBend.QS3_GRADIENT = data[0]; // -5.7
        secondBend.QS3_SECOND_GRADIENT = data[1]; // -10
        secondBend.dipoleCct345TiltAngles[1] = data[2]; // 80
        secondBend.dipoleCct345TiltAngles[2] = data[3]; // 90
        secondBend.dipoleCct345TiltAngles[3] = data[4]; // 90
        secondBend.agCct345TiltAngles[0] = data[5]; // 90
        secondBend.agCct345TiltAngles[2] = data[6]; // 90
        secondBend.agCct345TiltAngles[3] = data[7]; // 90
        secondBend.dipoleCct345I0 = data[8]; // -9457
        secondBend.agCct345I0 = data[9]; // -6700

        return secondBend;
    }

    public static double[] objectives(GantryDataBipolarCo.SecondBend secondBend) {
        GantryDataBipolarCo.FirstBend FIRST_BEND = GantryDataBipolarCo.FirstBend.getDefault();
        Line2 trajectoryPart2 = GantryDataBipolarCoUtils.getTrajectory2(FIRST_BEND, secondBend);
        final double ignoreDistance = 1.5;
        final double stepLen = 5 * MM;
        final int numberPerWinding = 12;
        // ---------------- create magnet ----------------------
        secondBend.disperseNumberPerWinding = numberPerWinding;

        Cct cct = dipoleCct345_local_goodWindingMethod(secondBend)
                .addCct(agCct345_local_goodWindingMethod(secondBend));

        QsHardPlaneMagnet qs3 = getQs3(FIRST_BEND, secondBend);

        Cct moved1 = moveCCT345_1(cct, FIRST_BEND, secondBend);
        Cct moved2 = moveCCT345_2(moved1, FIRST_BEND, secondBend);

        Elements elements = Elements.empty();
        elements.addElement(FarIgnoreMagnet.create(
                moved1,
                trajectoryPart2.pointAt(secondBend.DL2 + secondBend.CCT345_LENGTH / 2).toPoint3(),
                ignoreDistance
        ));
        elements.addElement(FarIgnoreMagnet.create(
                moved2,
                trajectoryPart2.pointAt(secondBend.DL2 + secondBend.CCT345_LENGTH +
                        secondBend.GAP3 * 2 + secondBend.QS3_LEN + secondBend.CCT345_LENGTH / 2).toPoint3(),
                ignoreDistance
        ));
        elements.addElement(qs3);

        // ------------------- run ------------------------
        List<Double> result = new ArrayList<>();

        // x 方向 mm  == > 束斑中心 + 束斑大小（半径）
        CctUtils.multiDpPhaseEllipses(
                trajectoryPart2, trajectoryPart2.getLength(),
                elements, -5 * PRESENT, 5 * PRESENT, 3, 6,
                true, 215, stepLen
        ).stream().sequential().map(BaseUtils.Content.BiContent::getT2).forEach(point2s -> {
            DoubleSummaryStatistics statistics = point2s.stream().mapToDouble(Point2::getX).summaryStatistics();
            result.add(statistics.getAverage()); // x 方向束斑中心 单位 mm
            result.add((statistics.getMax() - statistics.getMin()) / 2);
        });

        // y 方向 mm
        CctUtils.multiDpPhaseEllipses(
                trajectoryPart2, trajectoryPart2.getLength(),
                elements, -5 * PRESENT, 5 * PRESENT, 3, 6,
                false, 215
        ).stream().sequential().map(BaseUtils.Content.BiContent::getT2).forEach(point2s -> {
            DoubleSummaryStatistics statistics = point2s.stream().mapToDouble(Point2::getX).summaryStatistics();
            result.add(statistics.getAverage());
            result.add((statistics.getMax() - statistics.getMin()) / 2);
        });


        return result.stream().mapToDouble(Double::doubleValue).toArray();
    }

    public static double[] compute(double[] v) {
        GantryDataBipolarCo.SecondBend secondBend = create(v);
        double[] objectives = objectives(secondBend);
        return objectives;
    }

    private static double[] objectivesTest(GantryDataBipolarCo.SecondBend secondBend) {
        return new double[]{
                Math.abs(secondBend.QS3_GRADIENT),
                Math.abs(secondBend.QS3_SECOND_GRADIENT),
                Math.abs(secondBend.dipoleCct345TiltAngles[1] - 80),
                Math.abs(secondBend.dipoleCct345TiltAngles[2] - 80),
                Math.abs(secondBend.dipoleCct345TiltAngles[3] - 80),
                Math.abs(secondBend.agCct345TiltAngles[0] - 80),
                Math.abs(secondBend.agCct345TiltAngles[2] - 80),
                Math.abs(secondBend.agCct345TiltAngles[3] - 80),
                Math.abs(secondBend.dipoleCct345I0 + 9500)
        };
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

    private static QsHardPlaneMagnet getQs3(GantryDataBipolarCo.FirstBend firstBend, GantryDataBipolarCo.SecondBend secondBend) {
        Trajectory trajectoryPart2 = GantryDataBipolarCoUtils.getTrajectory2(firstBend, secondBend);


        return QsHardPlaneMagnet.create(secondBend.QS3_LEN, secondBend.QS3_GRADIENT, secondBend.QS3_SECOND_GRADIENT,
                secondBend.QS3_APERTURE_MM,
                trajectoryPart2.pointAt(secondBend.DL2 + secondBend.CCT345_LENGTH + secondBend.GAP3),
                trajectoryPart2.directAt(secondBend.DL2 + secondBend.CCT345_LENGTH + secondBend.GAP3));
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

}
