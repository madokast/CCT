package cn.edu.hust.zrx.cct.study.U转Py;

import cn.edu.hust.zrx.cct.Logger;
import cn.edu.hust.zrx.cct.advanced.MathFunction;
import cn.edu.hust.zrx.cct.advanced.combined.GantryDataBipolarCo;
import cn.edu.hust.zrx.cct.advanced.combined.GantryDataBipolarCoUtils;
import cn.edu.hust.zrx.cct.base.BaseUtils;
import cn.edu.hust.zrx.cct.base.annotation.Run;
import cn.edu.hust.zrx.cct.base.cct.SoleLayerCct;
import cn.edu.hust.zrx.cct.base.line.Trajectory;
import cn.edu.hust.zrx.cct.base.particle.ParticleFactory;
import cn.edu.hust.zrx.cct.base.particle.RunningParticle;
import cn.edu.hust.zrx.cct.base.point.Point2;
import cn.edu.hust.zrx.cct.base.point.Point3;
import cn.edu.hust.zrx.cct.base.vector.Vector2;
import cn.edu.hust.zrx.cct.base.vector.Vector3;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

/**
 * Description
 * <p>
 * Data
 * 21:08
 *
 * @author zrx
 * @version 1.0
 */

public class C20201113测试CCT磁场计算 {

    @Run(100)
    public void test01() {
        GantryDataBipolarCo.SecondBend secondBend = GantryDataBipolarCo.SecondBend.getDefault();

        final double bigR = secondBend.dipoleCct345BigR;
        final double innerSmallR = secondBend.dipoleCct345SmallRInner;
        final double outerSmall = secondBend.dipoleCct345SmallROuter;
        final double bendingAngle = secondBend.dipoleCct345Angle;
        final double bendingRadian = BaseUtils.Converter.angleToRadian(bendingAngle);
        final double[] tiltAngles = secondBend.dipoleCct345TiltAngles;
        final int windingNumber = 128;
        final double endKsi = windingNumber * Math.PI * 2;
        final double I0 = secondBend.dipoleCct345I0; // 2.355 T
        final double phi0 = bendingRadian / windingNumber;
        final int disperseNumberPerWinding = secondBend.disperseNumberPerWinding;

        MathFunction phiKsiFun = MathFunction.createPhiKsiFun(bigR, innerSmallR, phi0, tiltAngles);
        SoleLayerCct soleLayerCct = SoleLayerCct.createSoleLayerCct(bigR, innerSmallR, phiKsiFun, I0,
                0, endKsi, windingNumber * disperseNumberPerWinding);

        List<Point3> windings = soleLayerCct.getWindings();


        Vector3 m = soleLayerCct.magnetAt(Point3.origin());

        Logger.getLogger().info("m = " + m);
    }

    @Run(2)
    public void test_run() {
        GantryDataBipolarCo.SecondBend secondBend = GantryDataBipolarCo.SecondBend.getDefault();

        final double bigR = secondBend.dipoleCct345BigR;
        final double innerSmallR = secondBend.dipoleCct345SmallRInner;
        final double outerSmall = secondBend.dipoleCct345SmallROuter;
        final double bendingAngle = secondBend.dipoleCct345Angle;
        final double bendingRadian = BaseUtils.Converter.angleToRadian(bendingAngle);
        final double[] tiltAngles = secondBend.dipoleCct345TiltAngles;
        final int windingNumber = 128;
        final double endKsi = windingNumber * Math.PI * 2;
        final double I0 = secondBend.dipoleCct345I0; // 2.355 T
        final double phi0 = bendingRadian / windingNumber;
        final int disperseNumberPerWinding = secondBend.disperseNumberPerWinding;

        MathFunction phiKsiFun = MathFunction.createPhiKsiFun(bigR, innerSmallR, phi0, tiltAngles);
        SoleLayerCct soleLayerCct = SoleLayerCct.createSoleLayerCct(bigR, innerSmallR, phiKsiFun, I0,
                0, endKsi, windingNumber * disperseNumberPerWinding);


        RunningParticle p = ParticleFactory.createProton(
                Point3.origin(), Vector3.getZDirect(), 250
        );


        float distance = 0.0f;
        float LENGTH = 100.0f;

        while (distance < LENGTH) {
            Vector3 m = soleLayerCct.magnetAt(p.position);

            p.runSelf(m,0.001f);

            distance += 0.001f;
        }

        Logger.getLogger().info("p = " + p);
    }

    @Run(3)
    public void test_run_相对位置() {
        GantryDataBipolarCo.SecondBend secondBend = GantryDataBipolarCo.SecondBend.getDefault();

        final double bigR = secondBend.dipoleCct345BigR;
        final double innerSmallR = secondBend.dipoleCct345SmallRInner;
        final double outerSmall = secondBend.dipoleCct345SmallROuter;
        final double bendingAngle = secondBend.dipoleCct345Angle;
        final double bendingRadian = BaseUtils.Converter.angleToRadian(bendingAngle);
        final double[] tiltAngles = secondBend.dipoleCct345TiltAngles;
        final int windingNumber = 128;
        final double endKsi = windingNumber * Math.PI * 2;
        final double I0 = secondBend.dipoleCct345I0; // 2.355 T
        final double phi0 = bendingRadian / windingNumber;
        final int disperseNumberPerWinding = secondBend.disperseNumberPerWinding;

        MathFunction phiKsiFun = MathFunction.createPhiKsiFun(bigR, innerSmallR, phi0, tiltAngles);
        SoleLayerCct soleLayerCct = SoleLayerCct.createSoleLayerCct(bigR, innerSmallR, phiKsiFun, I0,
                0, endKsi, windingNumber * disperseNumberPerWinding);

        Trajectory trajectory2 = GantryDataBipolarCoUtils.getTrajectory2(GantryDataBipolarCo.FirstBend.getDefault(), secondBend);
        Point2 afterDl2 = trajectory2.pointAt(secondBend.DL2).copy();
        Vector2 directDl2 = trajectory2.directAt(secondBend.DL2).copy();
        Logger.getLogger().info("afterDl2 = " + afterDl2);
        Logger.getLogger().info("directDl2 = " + directDl2);

        Vector3 moving = afterDl2.moveSelf(
                directDl2.rotateSelf(BaseUtils.Converter.angleToRadian(-90))
                        .changeLengthSelf(secondBend.trajectoryBigRPart2))
                .toVector3();

        Logger.getLogger().info("moving = " + moving);;

        soleLayerCct =  soleLayerCct.symmetricXZPlane().rotateInXYPlane(Point2.origin(), BaseUtils.Converter.angleToRadian(
                        135
                )).move(moving);


        Point3 o = trajectory2.pointAt(secondBend.DL2).toPoint3();
        Logger.getLogger().info("o = " + o);
        Vector3 m = soleLayerCct.magnetAt(o);
        Logger.getLogger().info("m = " + m);


//        RunningParticle p = ParticleFactory.createProton(
//                Point3.origin(), Vector3.getZDirect(), 250
//        );
//
//
//        float distance = 0.0f;
//        float LENGTH = 100.0f;
//
//        while (distance < LENGTH) {
//            Vector3 m = soleLayerCct.magnetAt(p.position);
//
//            p.runSelf(m,0.001f);
//
//            distance += 0.001f;
//        }
//
//        Logger.getLogger().info("p = " + p);
    }

    @Run(4)
    public void test_run_相对位置_粒子运动() {
        GantryDataBipolarCo.SecondBend secondBend = GantryDataBipolarCo.SecondBend.getDefault();

        final double bigR = secondBend.dipoleCct345BigR;
        final double innerSmallR = secondBend.dipoleCct345SmallRInner;
        final double outerSmall = secondBend.dipoleCct345SmallROuter;
        final double bendingAngle = secondBend.dipoleCct345Angle;
        final double bendingRadian = BaseUtils.Converter.angleToRadian(bendingAngle);
        final double[] tiltAngles = secondBend.dipoleCct345TiltAngles;
        final int windingNumber = 128;
        final double endKsi = windingNumber * Math.PI * 2;
        final double I0 = secondBend.dipoleCct345I0; // 2.355 T
        final double phi0 = bendingRadian / windingNumber;
        final int disperseNumberPerWinding = secondBend.disperseNumberPerWinding;

        MathFunction phiKsiFun = MathFunction.createPhiKsiFun(bigR, innerSmallR, phi0, tiltAngles);
        SoleLayerCct soleLayerCct = SoleLayerCct.createSoleLayerCct(bigR, innerSmallR, phiKsiFun, I0,
                0, endKsi, windingNumber * disperseNumberPerWinding);

        Trajectory trajectory2 = GantryDataBipolarCoUtils.getTrajectory2(GantryDataBipolarCo.FirstBend.getDefault(), secondBend);
        Point2 afterDl2 = trajectory2.pointAt(secondBend.DL2).copy();
        Vector2 directDl2 = trajectory2.directAt(secondBend.DL2).copy();
        Logger.getLogger().info("afterDl2 = " + afterDl2);
        Logger.getLogger().info("directDl2 = " + directDl2);

        Vector3 moving = afterDl2.moveSelf(
                directDl2.rotateSelf(BaseUtils.Converter.angleToRadian(-90))
                        .changeLengthSelf(secondBend.trajectoryBigRPart2))
                .toVector3();

        Logger.getLogger().info("moving = " + moving);;

        soleLayerCct =  soleLayerCct.symmetricXZPlane().rotateInXYPlane(Point2.origin(), BaseUtils.Converter.angleToRadian(
                135
        )).move(moving);

        RunningParticle p = ParticleFactory.createIdealProtonAtTrajectory250MeV(trajectory2);
        Logger.getLogger().info("p = " + p);


        float distance = 0.0f;
        float LENGTH = 2.0f;

        while (distance < LENGTH) {
            Vector3 m = soleLayerCct.magnetAt(p.position);

            p.runSelf(m,0.001f);

            distance += 0.001f;
        }

        Logger.getLogger().info("p = " + p);
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
