package cn.edu.hust.zrx.cct.study.V双极点坐标系机架;

import cn.edu.hust.zrx.cct.advanced.CctUtils;
import cn.edu.hust.zrx.cct.advanced.MathFunction;
import cn.edu.hust.zrx.cct.advanced.combined.GantryDataBipolarCo;
import cn.edu.hust.zrx.cct.advanced.combined.GantryDataBipolarCoUtils;
import cn.edu.hust.zrx.cct.base.BaseUtils;
import cn.edu.hust.zrx.cct.base.annotation.Run;
import cn.edu.hust.zrx.cct.base.cct.Cct;
import cn.edu.hust.zrx.cct.base.cct.CctFactory;
import cn.edu.hust.zrx.cct.base.cct.SoleLayerCct;
import cn.edu.hust.zrx.cct.base.line.Line2;
import cn.edu.hust.zrx.cct.base.line.Trajectory;
import cn.edu.hust.zrx.cct.base.magnet.Elements;
import cn.edu.hust.zrx.cct.base.magnet.FarIgnoreMagnet;
import cn.edu.hust.zrx.cct.base.magnet.qs.QsHardPlaneMagnet;
import cn.edu.hust.zrx.cct.base.point.Point2;
import cn.edu.hust.zrx.cct.base.python.Plot2d;
import cn.edu.hust.zrx.cct.base.vector.Vector2;
import io.jenetics.*;
import io.jenetics.engine.Codecs;
import io.jenetics.engine.Engine;
import io.jenetics.engine.Problem;
import io.jenetics.ext.SimulatedBinaryCrossover;
import io.jenetics.ext.moea.MOEA;
import io.jenetics.ext.moea.NSGA2Selector;
import io.jenetics.ext.moea.Vec;
import io.jenetics.util.DoubleRange;
import io.jenetics.util.IntRange;

import static cn.edu.hust.zrx.cct.base.BaseUtils.Constant.MM;
import static cn.edu.hust.zrx.cct.base.BaseUtils.Constant.PRESENT;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Description
 * C20201029准备充分遗传算法开始
 * <p>
 * Data
 * 10:52
 *
 * @author zrx
 * @version 1.0
 */

public class C20201029准备充分遗传算法开始 {
    void 变量数目分析() {
        GantryDataBipolarCo.SecondBend secondBend = GantryDataBipolarCo.SecondBend.getDefault();

        /*
         * QS 磁铁 梯度 六级梯度 孔径  -------  2个梯度
         *
         * 偏转半径  二极CCT-R  四极CCT-R   不变
         * 孔径  不变
         * 匝数 --- 可以改变
         * 角度 不变
         * 倾斜角 --- 2 * 4 = 8 个 - 2个 = 6 个
         * 长度  不变？
         * 电流 --- 两个
         * 分段数目 120
         *
         * public double[] dipoleCct345TiltAngles = {30, 90, 90, 90};
           public double[] agCct345TiltAngles = {90, 30, 90, 90};
         */

        // 10 个变量
        secondBend.QS3_GRADIENT = 0.0;
        secondBend.QS3_SECOND_GRADIENT = 0.0;
        secondBend.dipoleCct345TiltAngles[1] = 0.0;
        secondBend.dipoleCct345TiltAngles[2] = 0.0;
        secondBend.dipoleCct345TiltAngles[3] = 0.0;
        secondBend.agCct345TiltAngles[0] = 0.0;
        secondBend.agCct345TiltAngles[2] = 0.0;
        secondBend.agCct345TiltAngles[3] = 0.0;
        secondBend.dipoleCct345I0 = 0.0;
        secondBend.agCct345I0 = 0.0;

        secondBend.disperseNumberPerWinding = 120;
    }

    public static GantryDataBipolarCo.SecondBend create(double[] data) {
        GantryDataBipolarCo.SecondBend secondBend = GantryDataBipolarCo.SecondBend.getDefault();

        secondBend.QS3_GRADIENT = data[0]; // -7.3733
        secondBend.QS3_SECOND_GRADIENT = data[1]; // -45.31
        secondBend.dipoleCct345TiltAngles[1] = data[2]; // 80
        secondBend.dipoleCct345TiltAngles[2] = data[3]; // 90
        secondBend.dipoleCct345TiltAngles[3] = data[4]; // 90
        secondBend.agCct345TiltAngles[0] = data[5];
        secondBend.agCct345TiltAngles[2] = data[6];
        secondBend.agCct345TiltAngles[3] = data[7];
        secondBend.dipoleCct345I0 = data[8];
        secondBend.agCct345I0 = data[9];

        return secondBend;
    }

    public static double[] objectives(GantryDataBipolarCo.SecondBend secondBend) {
        GantryDataBipolarCo.FirstBend FIRST_BEND = GantryDataBipolarCo.FirstBend.getDefault();
        Line2 trajectoryPart2 = GantryDataBipolarCoUtils.getTrajectory2(FIRST_BEND, secondBend);
        double ignoreDistance = 3.5;
        // ---------------- create magnet ----------------------
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
        List<Double> size = new ArrayList<>();

        // x 方向 mm  == > 束斑中心 + 束斑大小（半径）
        CctUtils.multiDpPhaseEllipses(
                trajectoryPart2, trajectoryPart2.getLength(),
                elements, -5 * PRESENT, 5 * PRESENT, 3, 6,
                true, 215
        ).stream().sequential().map(BaseUtils.Content.BiContent::getT2).forEach(point2s -> {
            DoubleSummaryStatistics statistics = point2s.stream().mapToDouble(Point2::getX).summaryStatistics();
            result.add(statistics.getAverage());
            size.add((statistics.getMax() - statistics.getMin()) / 2);
        });

        // 束斑计算均值
        double avgX = size.stream().mapToDouble(Double::doubleValue).average().orElse(0);
        size.forEach(s -> result.add(Math.abs(s - avgX)));
        size.clear();

        // y 方向 mm
        CctUtils.multiDpPhaseEllipses(
                trajectoryPart2, trajectoryPart2.getLength(),
                elements, -5 * PRESENT, 5 * PRESENT, 3, 6,
                false, 215
        ).stream().sequential().map(BaseUtils.Content.BiContent::getT2).forEach(point2s -> {
            DoubleSummaryStatistics statistics = point2s.stream().mapToDouble(Point2::getX).summaryStatistics();
            result.add(statistics.getAverage());
            size.add((statistics.getMax() - statistics.getMin()) / 2);
        });

        double avgY = size.stream().mapToDouble(Double::doubleValue).average().orElse(0);
        size.forEach(s -> result.add(Math.abs(s - avgY)));
        size.clear();


        return result.stream().mapToDouble(Double::doubleValue).toArray();
    }

    public static double[] fun(double[] v) {
        GantryDataBipolarCo.SecondBend secondBend = create(v);
//        double[] objectives = objectives(secondBend);
        double[] objectives = objectivesTest(secondBend);
//        System.out.println("data = " + Arrays.toString(v) + "\nresult = " + Arrays.toString(objectives));
        return objectives;
    }

    private static double[] objectivesTest(GantryDataBipolarCo.SecondBend secondBend) {
        return new double[]{
                Math.abs(secondBend.QS3_GRADIENT),
                Math.abs(secondBend.QS3_SECOND_GRADIENT),
                Math.abs(secondBend.dipoleCct345TiltAngles[1]-80),
                Math.abs(secondBend.dipoleCct345TiltAngles[2]-80),
                Math.abs(secondBend.dipoleCct345TiltAngles[3]-80),
                Math.abs(secondBend.agCct345TiltAngles[0]-80),
                Math.abs(secondBend.agCct345TiltAngles[2]-80),
                Math.abs(secondBend.agCct345TiltAngles[3]-80),
                Math.abs(secondBend.dipoleCct345I0+9500)
        };
    }

    public static final Problem<double[], DoubleGene, Vec<double[]>> PROBLEM = Problem.of(
            v -> Vec.of(fun(v)),
            Codecs.ofVector(
                    //        secondBend.QS3_GRADIENT = data[0]; // -7.3733
                    //        secondBend.QS3_SECOND_GRADIENT = data[1]; // -45.31
                    //        secondBend.dipoleCct345TiltAngles[1] = data[2]; // 80
                    //        secondBend.dipoleCct345TiltAngles[2] = data[3]; // 90
                    //        secondBend.dipoleCct345TiltAngles[3] = data[4]; // 90
                    //        secondBend.agCct345TiltAngles[0] = data[5];
                    //        secondBend.agCct345TiltAngles[2] = data[6];
                    //        secondBend.agCct345TiltAngles[3] = data[7];
                    //        secondBend.dipoleCct345I0 = data[8];
                    //        secondBend.agCct345I0 = data[9];
                    DoubleRange.of(-7.3733, 0),//-7.3733
                    DoubleRange.of(-45.31, 0),//-87.25

                    DoubleRange.of(70, 90),// 80
                    DoubleRange.of(70, 90),// 90
                    DoubleRange.of(70, 90),// 90

                    DoubleRange.of(70, 90),// 90
                    DoubleRange.of(70, 90),// 90
                    DoubleRange.of(70, 90),// 90

                    DoubleRange.of(-10000, -9000), // -9664
                    DoubleRange.of(-7000, -5000) // -6000
            )
    );

    public static Engine<DoubleGene, Vec<double[]>> engine = Engine.builder(PROBLEM).populationSize(20000)
            .alterers(
                    new SimulatedBinaryCrossover<>(1),
                    new Mutator<>(1.0 / 10)
            ).offspringSelector(new TournamentSelector<>(5))
            .survivorsSelector(NSGA2Selector.ofVec())
            .minimizing().build();

    public static void main(String[] args) {
        List<Point2> point2s = engine.stream()
                .limit(25000)
                .peek(result -> {
                    long generation = result.generation();

                    Phenotype<DoubleGene, Vec<double[]>> best = result.bestPhenotype();
                    double[] fitnessData = best.fitness().data();
                    double[] individual = best.genotype().chromosome().as(DoubleChromosome.class).toArray();

                    System.out.println("fitnessData = " + Arrays.toString(fitnessData));

                    System.out.println("individual = " + Arrays.toString(individual));
                })
                .collect(MOEA.toParetoSet(IntRange.of(1000, 1100)))
                .map(Phenotype::fitness)
                .asList()
                .stream()
                .map(Vec::data)
                .map(v -> Point2.create(v[0], v[1]))
                .collect(Collectors.toList());

        Plot2d.plot2(point2s, Plot2d.BLUE_POINT);

        Plot2d.showThread();
    }


    //------------------ tools--------------------------
    // 这才是正确的绕线方法
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

    // 2020年10月26日 发现巨大错误 外层CCT却使用内层半径建模
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
