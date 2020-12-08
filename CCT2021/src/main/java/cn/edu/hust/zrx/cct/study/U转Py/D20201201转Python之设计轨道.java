package cn.edu.hust.zrx.cct.study.U转Py;

import cn.edu.hust.zrx.cct.Logger;
import cn.edu.hust.zrx.cct.advanced.MathFunction;
import cn.edu.hust.zrx.cct.advanced.combined.GantryDataBipolarCo;
import cn.edu.hust.zrx.cct.advanced.combined.GantryDataBipolarCoUtils;
import cn.edu.hust.zrx.cct.base.BaseUtils;
import cn.edu.hust.zrx.cct.base.annotation.Run;
import cn.edu.hust.zrx.cct.base.cct.Cct;
import cn.edu.hust.zrx.cct.base.cct.CctFactory;
import cn.edu.hust.zrx.cct.base.cct.SoleLayerCct;
import cn.edu.hust.zrx.cct.base.line.Trajectory;
import cn.edu.hust.zrx.cct.base.magnet.Elements;
import cn.edu.hust.zrx.cct.base.magnet.qs.QsHardPlaneMagnet;
import cn.edu.hust.zrx.cct.base.point.Point2;
import cn.edu.hust.zrx.cct.base.point.Point3;
import cn.edu.hust.zrx.cct.base.python.Plot2d;
import cn.edu.hust.zrx.cct.base.python.Plot3d;
import cn.edu.hust.zrx.cct.base.vector.Vector2;
import cn.edu.hust.zrx.cct.base.vector.Vector3;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

/**
 * Description
 * D20201201转Python之设计轨道
 * <p>
 * Data
 * 19:30
 *
 * @author zrx
 * @version 1.0
 */

public class D20201201转Python之设计轨道 {

    static GantryDataBipolarCo.SecondBend defaultSecond = GantryDataBipolarCo.SecondBend.getDefault();

    GantryDataBipolarCo.FirstBend defaultFirstBend = GantryDataBipolarCo.FirstBend.getDefault();

    @Run
    public void 设计轨道(){
        Trajectory trajectory2 = GantryDataBipolarCoUtils.getTrajectory2();

        Point2 pointAtStart = trajectory2.pointAtStart();

        Logger.getLogger().info("pointAtStart = " + pointAtStart);
    }

    @Run(3)
    public void 二极磁铁(){
        Trajectory trajectory2 = GantryDataBipolarCoUtils.getTrajectory2();

        Cct dicct = dipoleCct345_local_goodWindingMethod(defaultSecond);
        Vector3 bb = dicct.magnetAt(Point3.create(0.95, 0, 0));
        Logger.getLogger().info("bb = " + bb);

        Cct cct = moveCCT345_1(dicct, defaultFirstBend, defaultSecond);

        Vector3 b = cct.magnetAt(trajectory2.pointAt(defaultSecond.DL2).toPoint3());
        Logger.getLogger().info("b = " + b);

        List<Point2> bz = cct.magnetBzAlongTrajectoryParallel(trajectory2.resetLength(defaultSecond.DL2+defaultSecond.CCT345_LENGTH+defaultSecond.GAP3*2+defaultSecond.QS3_LEN));

        Plot2d.plot2(bz);

        Plot2d.showThread();
    }

    @Run(2)
    public void 四极磁铁(){
        Trajectory trajectory2 = GantryDataBipolarCoUtils.getTrajectory2();

        Cct agcct = agCct345_local_goodWindingMethod(defaultSecond);
        Vector3 bb = agcct.magnetAt(Point3.create(0.95, 0, 0));
        Logger.getLogger().info("bb = " + bb);

        Cct cct = moveCCT345_1(agcct, defaultFirstBend, defaultSecond);

        List<Point2> g = cct.magnetGradientAlongTrajectoryFast(
                trajectory2.resetLength(defaultSecond.DL2+defaultSecond.CCT345_LENGTH+defaultSecond.GAP3*2+defaultSecond.QS3_LEN));

        Plot2d.plot2(g);

        Plot2d.showThread();
    }


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

    public static Elements allElementPart2(GantryDataBipolarCo.FirstBend firstBend, GantryDataBipolarCo.SecondBend secondBend) {
        Cct all = dipoleCct345_local_goodWindingMethod(secondBend).addCct(agCct345_local_goodWindingMethod(secondBend));

        Cct move = moveCCT345_1(all, firstBend, secondBend);
        Cct moved2 = moveCCT345_2(move, firstBend, secondBend);

        Cct cct = move.addCct(moved2);

        return Elements.empty().addElement(cct).addElement(getQs3(firstBend, secondBend));
    }


    public static void main(String[] args) throws Exception {
        BaseUtils.Timer.printPeriodPerSecondCall(Logger.getLogger());
        StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[1];
        Logger.getLogger().info("{}", stackTraceElement.getClassName());
        Class<?> currentClass = Class.forName(stackTraceElement.getClassName());

        Constructor<?> constructor = currentClass.getConstructor();
        Object newInstance = constructor.newInstance();

        Class<Run> runAnnotation = Run.class;

        Method[] methods = currentClass.getDeclaredMethods();
        Arrays.stream(methods)
                .filter(method -> method.isAnnotationPresent(runAnnotation))
                .filter(method -> method.getAnnotation(runAnnotation).validate())
                .sorted((m1, m2) -> Integer.compare(
                        m2.getAnnotation(runAnnotation).value(),
                        m1.getAnnotation(runAnnotation).value()
                ))
                .limit(1)
                .forEach(method -> {
                    Logger.getLogger().info("运行{}", method.toString());
                    try {
                        method.invoke(newInstance);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                });

        BaseUtils.Timer.printPeriodPerSecondCall(Logger.getLogger());
    }
}
