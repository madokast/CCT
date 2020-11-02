package cn.edu.hust.zrx.cct.study.Z验证性研究;

import cn.edu.hust.zrx.cct.Logger;
import cn.edu.hust.zrx.cct.base.BaseUtils;
import cn.edu.hust.zrx.cct.base.cct.Cct;
import cn.edu.hust.zrx.cct.base.cct.CctFactory;
import cn.edu.hust.zrx.cct.base.magnet.Elements;
import cn.edu.hust.zrx.cct.base.cct.SoleLayerCct;
import cn.edu.hust.zrx.cct.base.line.Arcs;
import cn.edu.hust.zrx.cct.base.line.Trajectory;
import cn.edu.hust.zrx.cct.base.line.TrajectoryFactory;
import cn.edu.hust.zrx.cct.base.particle.ParticleFactory;
import cn.edu.hust.zrx.cct.base.particle.ParticleRunner;
import cn.edu.hust.zrx.cct.base.particle.RunningParticle;
import cn.edu.hust.zrx.cct.base.point.Point2;
import cn.edu.hust.zrx.cct.base.python.Plot2d;
import cn.edu.hust.zrx.cct.base.python.Plot3d;
import cn.edu.hust.zrx.cct.base.magnet.qs.QsHardPlaneMagnet;
import cn.edu.hust.zrx.cct.base.vector.Vector2;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Description
 * Ddirectθ问题
 * <p>
 * Data
 * 7:11
 *
 * @author zrx
 * @version 1.0
 */
@SuppressWarnings("all")
public class Ddirectθ问题 {


    @run(1)
    public void 画图() {
        Cct dipoleCct = createDipoleCct();

        dipoleCct.plot3(Plot2d.RED_LINE, Plot2d.BLUE_LINE);

        Plot3d.showThread();
    }

    @run(2)
    public void 画图2() {

        SoleLayerCct innerCct = CctFactory.createSoleLaterCct(dipoleCctSmallRInner, dipoleCctBigR,
                dipoleCctAngle, dipoleCctWindingNumber, dipoleCctA0Inner, dipoleCctA1Inner,
                dipoleCctA2Inner, dipoleCctIInner, numberPerWinding,
                0.0, 0.0, false);
        SoleLayerCct outerCct = CctFactory.createSoleLaterCct(dipoleCctSmallROuter, dipoleCctBigR,
                dipoleCctAngle, dipoleCctWindingNumber, dipoleCctA0Outer, dipoleCctA1Outer,
                dipoleCctA2Outer, dipoleCctIOuter, numberPerWinding,
                0.0, 0.0, true);

        Cct dipoleCct = Cct.getEmptyCct().
                addSoleLayerCct(innerCct).
                addSoleLayerCct(outerCct);


        dipoleCct.plot3(Plot2d.RED_LINE, Plot2d.BLUE_LINE);

        Plot3d.showThread();
    }

    @run(3)
    public void 对比之传统CCT() {
        SoleLayerCct innerCct = CctFactory.createSoleLaterCct(dipoleCctSmallRInner, dipoleCctBigR,
                dipoleCctAngle, dipoleCctWindingNumber, dipoleCctA0Inner, dipoleCctA1Inner,
                dipoleCctA2Inner, dipoleCctIInner, numberPerWinding,
                0.0, 0.0, true);
        SoleLayerCct outerCct = CctFactory.createSoleLaterCct(dipoleCctSmallROuter, dipoleCctBigR,
                dipoleCctAngle, dipoleCctWindingNumber, dipoleCctA0Outer, dipoleCctA1Outer,
                dipoleCctA2Outer, dipoleCctIOuter, numberPerWinding,
                0.0, 0.0, true);

        Cct dipoleCct_inTouT = Cct.getEmptyCct().
                addSoleLayerCct(innerCct).
                addSoleLayerCct(outerCct);

        innerCct = CctFactory.createSoleLaterCct(dipoleCctSmallRInner, dipoleCctBigR,
                dipoleCctAngle, dipoleCctWindingNumber, dipoleCctA0Inner, dipoleCctA1Inner,
                dipoleCctA2Inner, -dipoleCctIInner, numberPerWinding,
                0.0, 0.0, false);
        outerCct = CctFactory.createSoleLaterCct(dipoleCctSmallROuter, dipoleCctBigR,
                dipoleCctAngle, dipoleCctWindingNumber, dipoleCctA0Outer, dipoleCctA1Outer,
                dipoleCctA2Outer, dipoleCctIOuter, numberPerWinding,
                0.0, 0.0, true);

        Cct dipoleCct_inFouT = Cct.getEmptyCct().
                addSoleLayerCct(innerCct).
                addSoleLayerCct(outerCct);

        innerCct = CctFactory.createSoleLaterCct(dipoleCctSmallRInner, dipoleCctBigR,
                dipoleCctAngle, dipoleCctWindingNumber, dipoleCctA0Inner, dipoleCctA1Inner,
                dipoleCctA2Inner, dipoleCctIInner, numberPerWinding,
                0.0, 0.0, true);
        outerCct = CctFactory.createSoleLaterCct(dipoleCctSmallROuter, dipoleCctBigR,
                dipoleCctAngle, dipoleCctWindingNumber, dipoleCctA0Outer, dipoleCctA1Outer,
                dipoleCctA2Outer, -dipoleCctIOuter, numberPerWinding,
                0.0, 0.0, false);

        Cct dipoleCct_inTouF = Cct.getEmptyCct().
                addSoleLayerCct(innerCct).
                addSoleLayerCct(outerCct);


        innerCct = CctFactory.createSoleLaterCct(dipoleCctSmallRInner, dipoleCctBigR,
                dipoleCctAngle, dipoleCctWindingNumber, dipoleCctA0Inner, dipoleCctA1Inner,
                dipoleCctA2Inner, -dipoleCctIInner, numberPerWinding,
                0.0, 0.0, false);
        outerCct = CctFactory.createSoleLaterCct(dipoleCctSmallROuter, dipoleCctBigR,
                dipoleCctAngle, dipoleCctWindingNumber, dipoleCctA0Outer, dipoleCctA1Outer,
                dipoleCctA2Outer, -dipoleCctIOuter, numberPerWinding,
                0.0, 0.0, false);

        Cct dipoleCct_inFouF = Cct.getEmptyCct().
                addSoleLayerCct(innerCct).
                addSoleLayerCct(outerCct);


//        dipoleCct_inTouT.plot3(Plot2d.BLUE_LINE,Plot2d.RED_LINE);
//        dipoleCct_inFouT.plot3(Plot2d.BLUE_LINE,Plot2d.RED_LINE);
//        dipoleCct_inFouF.plot3(Plot2d.BLUE_LINE,Plot2d.RED_LINE);
//        dipoleCct_inTouF.plot3(Plot2d.BLUE_LINE,Plot2d.RED_LINE);


//        Plot3d.offAxisAndBgColor();
//
//        Plot3d.showThread();


        Trajectory trajectory = TrajectoryFactory.setStartingPoint(trajectoryBigR, -1)
                .setStraightLine(1, Vector2.yDirect())
                .addArcLine(trajectoryBigR, false, dipoleCctAngle)
                .addStraitLine(1.0);


        List<Point2> list_1 = dipoleCct_inTouT.magnetBzAlongTrajectory(trajectory, MM);
        List<Point2> list_2 = dipoleCct_inFouT.magnetBzAlongTrajectory(trajectory, MM);
        List<Point2> list_3 = dipoleCct_inTouF.magnetBzAlongTrajectory(trajectory, MM);
        List<Point2> list_4 = dipoleCct_inFouF.magnetBzAlongTrajectory(trajectory, MM);

        Plot2d.plot2(list_1, Plot2d.BLACK_LINE);
        Plot2d.plot2(list_2, Plot2d.RED_LINE);
        Plot2d.plot2(list_3, Plot2d.GREEN_LINE);
        Plot2d.plot2(list_4, Plot2d.BLUE_LINE);

        Plot2d.info("s/m", "By/T", "", 18);

        Plot2d.legend(18, "A/A", "C/A", "A/C", "C/C");

        Plot2d.showThread();
    }


    @run(4)
    public void 对比左手侧磁场() {
        SoleLayerCct innerCct = CctFactory.createSoleLaterCct(dipoleCctSmallRInner, dipoleCctBigR,
                dipoleCctAngle, dipoleCctWindingNumber, dipoleCctA0Inner, dipoleCctA1Inner,
                dipoleCctA2Inner, dipoleCctIInner, numberPerWinding,
                0.0, 0.0, true);
        SoleLayerCct outerCct = CctFactory.createSoleLaterCct(dipoleCctSmallROuter, dipoleCctBigR,
                dipoleCctAngle, dipoleCctWindingNumber, dipoleCctA0Outer, dipoleCctA1Outer,
                dipoleCctA2Outer, dipoleCctIOuter, numberPerWinding,
                0.0, 0.0, true);

        Cct dipoleCct_inTouT = Cct.getEmptyCct().
                addSoleLayerCct(innerCct).
                addSoleLayerCct(outerCct);

        innerCct = CctFactory.createSoleLaterCct(dipoleCctSmallRInner, dipoleCctBigR,
                dipoleCctAngle, dipoleCctWindingNumber, dipoleCctA0Inner, dipoleCctA1Inner,
                dipoleCctA2Inner, -dipoleCctIInner, numberPerWinding,
                0.0, 0.0, false);
        outerCct = CctFactory.createSoleLaterCct(dipoleCctSmallROuter, dipoleCctBigR,
                dipoleCctAngle, dipoleCctWindingNumber, dipoleCctA0Outer, dipoleCctA1Outer,
                dipoleCctA2Outer, dipoleCctIOuter, numberPerWinding,
                0.0, 0.0, true);

        Cct dipoleCct_inFouT = Cct.getEmptyCct().
                addSoleLayerCct(innerCct).
                addSoleLayerCct(outerCct);

        innerCct = CctFactory.createSoleLaterCct(dipoleCctSmallRInner, dipoleCctBigR,
                dipoleCctAngle, dipoleCctWindingNumber, dipoleCctA0Inner, dipoleCctA1Inner,
                dipoleCctA2Inner, dipoleCctIInner, numberPerWinding,
                0.0, 0.0, true);
        outerCct = CctFactory.createSoleLaterCct(dipoleCctSmallROuter, dipoleCctBigR,
                dipoleCctAngle, dipoleCctWindingNumber, dipoleCctA0Outer, dipoleCctA1Outer,
                dipoleCctA2Outer, -dipoleCctIOuter, numberPerWinding,
                0.0, 0.0, false);

        Cct dipoleCct_inTouF = Cct.getEmptyCct().
                addSoleLayerCct(innerCct).
                addSoleLayerCct(outerCct);


        innerCct = CctFactory.createSoleLaterCct(dipoleCctSmallRInner, dipoleCctBigR,
                dipoleCctAngle, dipoleCctWindingNumber, dipoleCctA0Inner, dipoleCctA1Inner,
                dipoleCctA2Inner, -dipoleCctIInner, numberPerWinding,
                0.0, 0.0, false);
        outerCct = CctFactory.createSoleLaterCct(dipoleCctSmallROuter, dipoleCctBigR,
                dipoleCctAngle, dipoleCctWindingNumber, dipoleCctA0Outer, dipoleCctA1Outer,
                dipoleCctA2Outer, -dipoleCctIOuter, numberPerWinding,
                0.0, 0.0, false);

        Cct dipoleCct_inFouF = Cct.getEmptyCct().
                addSoleLayerCct(innerCct).
                addSoleLayerCct(outerCct);


//        dipoleCct_inTouT.plot3(Plot2d.BLUE_LINE,Plot2d.RED_LINE);
//        dipoleCct_inFouT.plot3(Plot2d.BLUE_LINE,Plot2d.RED_LINE);
//        dipoleCct_inFouF.plot3(Plot2d.BLUE_LINE,Plot2d.RED_LINE);
//        dipoleCct_inTouF.plot3(Plot2d.BLUE_LINE,Plot2d.RED_LINE);


//        Plot3d.offAxisAndBgColor();
//
//        Plot3d.showThread();


        Trajectory trajectory = TrajectoryFactory.setStartingPoint(trajectoryBigR, -1)
                .setStraightLine(1, Vector2.yDirect())
                .addArcLine(trajectoryBigR, false, dipoleCctAngle)
                .addStraitLine(1.0);


        List<Point2> list_1 = dipoleCct_inTouT.leftHandMagnetAlongTrajectory(trajectory, MM);
        List<Point2> list_2 = dipoleCct_inFouT.leftHandMagnetAlongTrajectory(trajectory, MM);
        List<Point2> list_3 = dipoleCct_inTouF.leftHandMagnetAlongTrajectory(trajectory, MM);
        List<Point2> list_4 = dipoleCct_inFouF.leftHandMagnetAlongTrajectory(trajectory, MM);

        Plot2d.plot2(Point2.convert(list_1, 1, 1 / Gs), Plot2d.BLACK_LINE);
        Plot2d.plot2(Point2.convert(list_2, 1, 1 / Gs), Plot2d.RED_LINE);
        Plot2d.plot2(Point2.convert(list_3, 1, 1 / Gs), Plot2d.GREEN_LINE);
        Plot2d.plot2(Point2.convert(list_4, 1, 1 / Gs), Plot2d.BLUE_LINE);

        Plot2d.info("s/m", "Bx/Gs", "", 18);

        Plot2d.legend(18, "A/A", "C/A", "A/C", "C/C");

        Plot2d.showThread();
    }


    @run(5)
    public void 对比六极场分量() {
        SoleLayerCct innerCct = CctFactory.createSoleLaterCct(dipoleCctSmallRInner, dipoleCctBigR,
                dipoleCctAngle, dipoleCctWindingNumber, dipoleCctA0Inner, dipoleCctA1Inner,
                dipoleCctA2Inner, dipoleCctIInner, numberPerWinding,
                0.0, 0.0, true);
        SoleLayerCct outerCct = CctFactory.createSoleLaterCct(dipoleCctSmallROuter, dipoleCctBigR,
                dipoleCctAngle, dipoleCctWindingNumber, dipoleCctA0Outer, dipoleCctA1Outer,
                dipoleCctA2Outer, dipoleCctIOuter, numberPerWinding,
                0.0, 0.0, true);

        Cct dipoleCct_inTouT = Cct.getEmptyCct().
                addSoleLayerCct(innerCct).
                addSoleLayerCct(outerCct);

        innerCct = CctFactory.createSoleLaterCct(dipoleCctSmallRInner, dipoleCctBigR,
                dipoleCctAngle, dipoleCctWindingNumber, dipoleCctA0Inner, dipoleCctA1Inner,
                dipoleCctA2Inner, -dipoleCctIInner, numberPerWinding,
                0.0, 0.0, false);
        outerCct = CctFactory.createSoleLaterCct(dipoleCctSmallROuter, dipoleCctBigR,
                dipoleCctAngle, dipoleCctWindingNumber, dipoleCctA0Outer, dipoleCctA1Outer,
                dipoleCctA2Outer, dipoleCctIOuter, numberPerWinding,
                0.0, 0.0, true);

        Cct dipoleCct_inFouT = Cct.getEmptyCct().
                addSoleLayerCct(innerCct).
                addSoleLayerCct(outerCct);

        innerCct = CctFactory.createSoleLaterCct(dipoleCctSmallRInner, dipoleCctBigR,
                dipoleCctAngle, dipoleCctWindingNumber, dipoleCctA0Inner, dipoleCctA1Inner,
                dipoleCctA2Inner, dipoleCctIInner, numberPerWinding,
                0.0, 0.0, true);
        outerCct = CctFactory.createSoleLaterCct(dipoleCctSmallROuter, dipoleCctBigR,
                dipoleCctAngle, dipoleCctWindingNumber, dipoleCctA0Outer, dipoleCctA1Outer,
                dipoleCctA2Outer, -dipoleCctIOuter, numberPerWinding,
                0.0, 0.0, false);

        Cct dipoleCct_inTouF = Cct.getEmptyCct().
                addSoleLayerCct(innerCct).
                addSoleLayerCct(outerCct);


        innerCct = CctFactory.createSoleLaterCct(dipoleCctSmallRInner, dipoleCctBigR,
                dipoleCctAngle, dipoleCctWindingNumber, dipoleCctA0Inner, dipoleCctA1Inner,
                dipoleCctA2Inner, -dipoleCctIInner, numberPerWinding,
                0.0, 0.0, false);
        outerCct = CctFactory.createSoleLaterCct(dipoleCctSmallROuter, dipoleCctBigR,
                dipoleCctAngle, dipoleCctWindingNumber, dipoleCctA0Outer, dipoleCctA1Outer,
                dipoleCctA2Outer, -dipoleCctIOuter, numberPerWinding,
                0.0, 0.0, false);

        Cct dipoleCct_inFouF = Cct.getEmptyCct().
                addSoleLayerCct(innerCct).
                addSoleLayerCct(outerCct);


//        dipoleCct_inTouT.plot3(Plot2d.BLUE_LINE,Plot2d.RED_LINE);
//        dipoleCct_inFouT.plot3(Plot2d.BLUE_LINE,Plot2d.RED_LINE);
//        dipoleCct_inFouF.plot3(Plot2d.BLUE_LINE,Plot2d.RED_LINE);
//        dipoleCct_inTouF.plot3(Plot2d.BLUE_LINE,Plot2d.RED_LINE);


//        Plot3d.offAxisAndBgColor();
//
//        Plot3d.showThread();


        Trajectory trajectory = TrajectoryFactory.setStartingPoint(trajectoryBigR, -1)
                .setStraightLine(1, Vector2.yDirect())
                .addArcLine(trajectoryBigR, false, dipoleCctAngle)
                .addStraitLine(1.0);


        List<Point2> list_1 = dipoleCct_inTouT.magnetSecondGradientAlongTrajectory(trajectory, MM, 5 * MM);
        List<Point2> list_2 = dipoleCct_inFouT.magnetSecondGradientAlongTrajectory(trajectory, MM, 5 * MM);
        List<Point2> list_3 = dipoleCct_inTouF.magnetSecondGradientAlongTrajectory(trajectory, MM, 5 * MM);
        List<Point2> list_4 = dipoleCct_inFouF.magnetSecondGradientAlongTrajectory(trajectory, MM, 5 * MM);

        Plot2d.plot2(Point2.convert(list_1, 1, 1), Plot2d.BLACK_LINE);
        Plot2d.plot2(Point2.convert(list_2, 1, 1), Plot2d.RED_LINE);
        Plot2d.plot2(Point2.convert(list_3, 1, 1), Plot2d.GREEN_LINE);
        Plot2d.plot2(Point2.convert(list_4, 1, 1), Plot2d.BLUE_LINE);

        Plot2d.info("s/m", "L/(B T-2)", "sext", 18);

        Plot2d.legend(18, "A/A", "C/A", "A/C", "C/C");

        Plot2d.showThread();
    }


    @run(6)
    public void 对比单粒子跟踪() {
        SoleLayerCct innerCct = CctFactory.createSoleLaterCct(dipoleCctSmallRInner, dipoleCctBigR,
                dipoleCctAngle, dipoleCctWindingNumber, dipoleCctA0Inner, dipoleCctA1Inner,
                dipoleCctA2Inner, dipoleCctIInner, numberPerWinding,
                0.0, 0.0, true);
        SoleLayerCct outerCct = CctFactory.createSoleLaterCct(dipoleCctSmallROuter, dipoleCctBigR,
                dipoleCctAngle, dipoleCctWindingNumber, dipoleCctA0Outer, dipoleCctA1Outer,
                dipoleCctA2Outer, dipoleCctIOuter, numberPerWinding,
                0.0, 0.0, true);

        Cct dipoleCct_inTouT = Cct.getEmptyCct().
                addSoleLayerCct(innerCct).
                addSoleLayerCct(outerCct);

        innerCct = CctFactory.createSoleLaterCct(dipoleCctSmallRInner, dipoleCctBigR,
                dipoleCctAngle, dipoleCctWindingNumber, dipoleCctA0Inner, dipoleCctA1Inner,
                dipoleCctA2Inner, -dipoleCctIInner, numberPerWinding,
                0.0, 0.0, false);
        outerCct = CctFactory.createSoleLaterCct(dipoleCctSmallROuter, dipoleCctBigR,
                dipoleCctAngle, dipoleCctWindingNumber, dipoleCctA0Outer, dipoleCctA1Outer,
                dipoleCctA2Outer, dipoleCctIOuter, numberPerWinding,
                0.0, 0.0, true);

        Cct dipoleCct_inFouT = Cct.getEmptyCct().
                addSoleLayerCct(innerCct).
                addSoleLayerCct(outerCct);

        innerCct = CctFactory.createSoleLaterCct(dipoleCctSmallRInner, dipoleCctBigR,
                dipoleCctAngle, dipoleCctWindingNumber, dipoleCctA0Inner, dipoleCctA1Inner,
                dipoleCctA2Inner, dipoleCctIInner, numberPerWinding,
                0.0, 0.0, true);
        outerCct = CctFactory.createSoleLaterCct(dipoleCctSmallROuter, dipoleCctBigR,
                dipoleCctAngle, dipoleCctWindingNumber, dipoleCctA0Outer, dipoleCctA1Outer,
                dipoleCctA2Outer, -dipoleCctIOuter, numberPerWinding,
                0.0, 0.0, false);

        Cct dipoleCct_inTouF = Cct.getEmptyCct().
                addSoleLayerCct(innerCct).
                addSoleLayerCct(outerCct);


        innerCct = CctFactory.createSoleLaterCct(dipoleCctSmallRInner, dipoleCctBigR,
                dipoleCctAngle, dipoleCctWindingNumber, dipoleCctA0Inner, dipoleCctA1Inner,
                dipoleCctA2Inner, -dipoleCctIInner, numberPerWinding,
                0.0, 0.0, false);
        outerCct = CctFactory.createSoleLaterCct(dipoleCctSmallROuter, dipoleCctBigR,
                dipoleCctAngle, dipoleCctWindingNumber, dipoleCctA0Outer, dipoleCctA1Outer,
                dipoleCctA2Outer, -dipoleCctIOuter, numberPerWinding,
                0.0, 0.0, false);

        Cct dipoleCct_inFouF = Cct.getEmptyCct().
                addSoleLayerCct(innerCct).
                addSoleLayerCct(outerCct);


        Trajectory trajectory = TrajectoryFactory.setStartingPoint(trajectoryBigR, -1)
                .setStraightLine(1, Vector2.yDirect())
                .addArcLine(trajectoryBigR, false, dipoleCctAngle)
                .addStraitLine(1.0);

        RunningParticle ip1 = ParticleFactory.createIdealProtonAtTrajectory250MeV(trajectory);
        RunningParticle ip2 = ParticleFactory.createIdealProtonAtTrajectory250MeV(trajectory);
        RunningParticle ip3 = ParticleFactory.createIdealProtonAtTrajectory250MeV(trajectory);
        RunningParticle ip4 = ParticleFactory.createIdealProtonAtTrajectory250MeV(trajectory);

        List<Point2> collect1 = ParticleRunner.runGetPoint3WithDistance(ip1, dipoleCct_inTouT, trajectory.getLength(), MM).stream().map(ParticleRunner.Point3WithDistance::getDistanceWithZ).collect(Collectors.toList());
        List<Point2> collect2 = ParticleRunner.runGetPoint3WithDistance(ip2, dipoleCct_inFouT, trajectory.getLength(), MM).stream().map(ParticleRunner.Point3WithDistance::getDistanceWithZ).collect(Collectors.toList());
        List<Point2> collect3 = ParticleRunner.runGetPoint3WithDistance(ip3, dipoleCct_inTouF, trajectory.getLength(), MM).stream().map(ParticleRunner.Point3WithDistance::getDistanceWithZ).collect(Collectors.toList());
        List<Point2> collect4 = ParticleRunner.runGetPoint3WithDistance(ip4, dipoleCct_inFouF, trajectory.getLength(), MM).stream().map(ParticleRunner.Point3WithDistance::getDistanceWithZ).collect(Collectors.toList());


//        List<Point2> list_1 = dipoleCct_inTouT.magnetSecondGradientAlongTrajectory(trajectory, MM, 5 * MM);
//        List<Point2> list_2 = dipoleCct_inFouT.magnetSecondGradientAlongTrajectory(trajectory, MM, 5 * MM);
//        List<Point2> list_3 = dipoleCct_inTouF.magnetSecondGradientAlongTrajectory(trajectory, MM, 5 * MM);
//        List<Point2> list_4 = dipoleCct_inFouF.magnetSecondGradientAlongTrajectory(trajectory, MM, 5 * MM);

        Plot2d.plot2(Point2.convert(collect1, 1, 1 / MM), Plot2d.BLACK_LINE);
        Plot2d.plot2(Point2.convert(collect2, 1, 1 / MM), Plot2d.RED_LINE);
        Plot2d.plot2(Point2.convert(collect3, 1, 1 / MM), Plot2d.GREEN_LINE);
        Plot2d.plot2(Point2.convert(collect4, 1, 1 / MM), Plot2d.BLUE_LINE);

        Plot2d.info("s/m", "y/mm", "sext", 18);

        Plot2d.legend(18, "A/A", "C/A", "A/C", "C/C");

        Plot2d.grid();

        Plot2d.showThread();
    }

    @run(7)
    public void 对比四极场() {
        SoleLayerCct innerCct = CctFactory.createSoleLaterCct(dipoleCctSmallRInner, dipoleCctBigR,
                dipoleCctAngle, dipoleCctWindingNumber, dipoleCctA0Inner, dipoleCctA1Inner,
                dipoleCctA2Inner, dipoleCctIInner, numberPerWinding,
                0.0, 0.0, true);
        SoleLayerCct outerCct = CctFactory.createSoleLaterCct(dipoleCctSmallROuter, dipoleCctBigR,
                dipoleCctAngle, dipoleCctWindingNumber, dipoleCctA0Outer, dipoleCctA1Outer,
                dipoleCctA2Outer, dipoleCctIOuter, numberPerWinding,
                0.0, 0.0, true);

        Cct dipoleCct_inTouT = Cct.getEmptyCct().
                addSoleLayerCct(innerCct).
                addSoleLayerCct(outerCct);

        innerCct = CctFactory.createSoleLaterCct(dipoleCctSmallRInner, dipoleCctBigR,
                dipoleCctAngle, dipoleCctWindingNumber, dipoleCctA0Inner, dipoleCctA1Inner,
                dipoleCctA2Inner, -dipoleCctIInner, numberPerWinding,
                0.0, 0.0, false);
        outerCct = CctFactory.createSoleLaterCct(dipoleCctSmallROuter, dipoleCctBigR,
                dipoleCctAngle, dipoleCctWindingNumber, dipoleCctA0Outer, dipoleCctA1Outer,
                dipoleCctA2Outer, dipoleCctIOuter, numberPerWinding,
                0.0, 0.0, true);

        Cct dipoleCct_inFouT = Cct.getEmptyCct().
                addSoleLayerCct(innerCct).
                addSoleLayerCct(outerCct);

        innerCct = CctFactory.createSoleLaterCct(dipoleCctSmallRInner, dipoleCctBigR,
                dipoleCctAngle, dipoleCctWindingNumber, dipoleCctA0Inner, dipoleCctA1Inner,
                dipoleCctA2Inner, dipoleCctIInner, numberPerWinding,
                0.0, 0.0, true);
        outerCct = CctFactory.createSoleLaterCct(dipoleCctSmallROuter, dipoleCctBigR,
                dipoleCctAngle, dipoleCctWindingNumber, dipoleCctA0Outer, dipoleCctA1Outer,
                dipoleCctA2Outer, -dipoleCctIOuter, numberPerWinding,
                0.0, 0.0, false);

        Cct dipoleCct_inTouF = Cct.getEmptyCct().
                addSoleLayerCct(innerCct).
                addSoleLayerCct(outerCct);


        innerCct = CctFactory.createSoleLaterCct(dipoleCctSmallRInner, dipoleCctBigR,
                dipoleCctAngle, dipoleCctWindingNumber, dipoleCctA0Inner, dipoleCctA1Inner,
                dipoleCctA2Inner, -dipoleCctIInner, numberPerWinding,
                0.0, 0.0, false);
        outerCct = CctFactory.createSoleLaterCct(dipoleCctSmallROuter, dipoleCctBigR,
                dipoleCctAngle, dipoleCctWindingNumber, dipoleCctA0Outer, dipoleCctA1Outer,
                dipoleCctA2Outer, -dipoleCctIOuter, numberPerWinding,
                0.0, 0.0, false);

        Cct dipoleCct_inFouF = Cct.getEmptyCct().
                addSoleLayerCct(innerCct).
                addSoleLayerCct(outerCct);


//        dipoleCct_inTouT.plot3(Plot2d.BLUE_LINE,Plot2d.RED_LINE);
//        dipoleCct_inFouT.plot3(Plot2d.BLUE_LINE,Plot2d.RED_LINE);
//        dipoleCct_inFouF.plot3(Plot2d.BLUE_LINE,Plot2d.RED_LINE);
//        dipoleCct_inTouF.plot3(Plot2d.BLUE_LINE,Plot2d.RED_LINE);


//        Plot3d.offAxisAndBgColor();
//
//        Plot3d.showThread();


        Trajectory trajectory = TrajectoryFactory.setStartingPoint(trajectoryBigR, -1)
                .setStraightLine(1, Vector2.yDirect())
                .addArcLine(trajectoryBigR, false, dipoleCctAngle)
                .addStraitLine(1.0);


        List<Point2> list_1 = dipoleCct_inTouT.magnetGradientAlongTrajectory(trajectory, MM, 5 * MM);
        List<Point2> list_2 = dipoleCct_inFouT.magnetGradientAlongTrajectory(trajectory, MM, 5 * MM);
        List<Point2> list_3 = dipoleCct_inTouF.magnetGradientAlongTrajectory(trajectory, MM, 5 * MM);
        List<Point2> list_4 = dipoleCct_inFouF.magnetGradientAlongTrajectory(trajectory, MM, 5 * MM);

        Plot2d.plot2(Point2.convert(list_1, 1, 1 / 1), Plot2d.BLACK_LINE);
        Plot2d.plot2(Point2.convert(list_2, 1, 1 / 1), Plot2d.RED_LINE);
        Plot2d.plot2(Point2.convert(list_3, 1, 1 / 1), Plot2d.GREEN_LINE);
        Plot2d.plot2(Point2.convert(list_4, 1, 1 / 1), Plot2d.BLUE_LINE);

        Plot2d.info("s/m", "G/(T/m)", "", 18);

        Plot2d.legend(18, "A/A", "C/A", "A/C", "C/C");

        Plot2d.showThread();
    }


    //---------------------elements------------------------------

    private Elements getElementsOfAll() {
        List<QsHardPlaneMagnet> qs = get3QS();
        Cct allCctIn45 = getAllCctIn45();

        Elements elements = Elements.empty();
        qs.forEach(elements::addElement);
        allCctIn45.getSoleLayerCctList().forEach(elements::addElement);

        return elements;
    }

    private List<QsHardPlaneMagnet> get3QS() {
        //double length_m, double gradient_T_per_m,
        //double second_gradient_T_per_m2, double aperture_radius_mm,
        //Point2 location, Vector2 direct
        QsHardPlaneMagnet QS11 = QsHardPlaneMagnet.create(QS1_LEN, QS1_GRADIENT, QS1_SECOND_GRADIENT, QS1_APERTURE_MM,
                getTrajectory().pointAt(DL1 + CCT_LENGTH + GAP1), getTrajectory().directAt(DL1 + CCT_LENGTH + GAP1));

        QsHardPlaneMagnet QS2 = QsHardPlaneMagnet.create(QS2_LEN, QS2_GRADIENT, QS2_SECOND_GRADIENT, QS2_APERTURE_MM,
                getTrajectory().pointAt(DL1 + CCT_LENGTH + GAP1 + QS1_LEN + GAP2),
                getTrajectory().directAt(DL1 + CCT_LENGTH + GAP1 + QS1_LEN + GAP2));


        QsHardPlaneMagnet QS12 = QsHardPlaneMagnet.create(QS1_LEN, QS1_GRADIENT, QS1_SECOND_GRADIENT, QS1_APERTURE_MM,
                getTrajectory().pointAt(DL1 + CCT_LENGTH + GAP1 + QS1_LEN + GAP2 + QS2_LEN + GAP2),
                getTrajectory().directAt(DL1 + CCT_LENGTH + GAP1 + QS1_LEN + GAP2 + QS2_LEN + GAP2));

        return List.of(QS11, QS2, QS12);
    }

    private Cct getAllCctIn45() {
        return CctFactory.combineCct(getCct1(), getCct2());
    }

    private Cct getCct1() {
        Trajectory trajectory = getTrajectory();

        Cct cct = getCct();

        //        Point2 center = Arcs.center(
//                trajectory.pointAt(DL1 + CCT_LENGTH + BETWEEN_CCT225),
//                trajectory.pointAt(DL1 + CCT_LENGTH + BETWEEN_CCT225 + CCT_LENGTH / 2),
//                trajectory.pointAt(DL1 + CCT_LENGTH + BETWEEN_CCT225 + CCT_LENGTH)
//        );
//
//
//        CctFactory.Cct cct2 = CctFactory.positionInXYPlane(cct,center,BaseUtils.Converter.angleToRadian(-90+22.5));

        return CctFactory.positionInXYPlane(cct, Point2.create(DL1, trajectoryBigR), BaseUtils.Converter.angleToRadian(-90));

    }

    private Cct getCct2() {
        Trajectory trajectory = getTrajectory();

        Cct dipoleCct = createDipoleCct();
        //public static Cct createAgCct(double smallRInner,
        //                                  double smallROuter,
        //                                  double bigR,
        //                                  double[] angles,
        //                                  int[] windingNumbers,
        //                                  double a0BipolarInners,
        //                                  double a1QuadrupleInners,
        //                                  double a2SextupleInners,
        //                                  double IInner,
        //                                  double a0BipolarOuters,
        //                                  double a1QuadrupleOuters,
        //                                  double a2SextupleOuters,
        //                                  double IOuter,
        //                                  int numberPerWinding)
        Cct agCct = CctFactory.createAgCct(agCctSmallRInner, agCctSmallROuter, agCCTBigR,
                new double[]{agCctAngle1, agCctAngle0},
                new int[]{agCctWindingNumber1, agCctWindingNumber0},
                agCctA0Inner, agCctA1Inner, agCctA2Inner, -agCctIInner,
                agCctA0Outer, agCctA1Outer, agCctA2Outer, -agCctIOuter,
                numberPerWinding);

        Cct cct = CctFactory.combineCct(agCct, dipoleCct);

        //CctFactory.Cct cct1 = CctFactory.positionInXYPlane(cct, Point2.create(DL1, trajectoryBigR), BaseUtils.Converter.angleToRadian(-90));

        Point2 center = Arcs.center(
                trajectory.pointAt(DL1 + CCT_LENGTH + BETWEEN_CCT225),
                trajectory.pointAt(DL1 + CCT_LENGTH + BETWEEN_CCT225 + CCT_LENGTH / 2),
                trajectory.pointAt(DL1 + CCT_LENGTH + BETWEEN_CCT225 + CCT_LENGTH)
        );


        return CctFactory.positionInXYPlane(cct, center, BaseUtils.Converter.angleToRadian(-90 + 22.5));


    }

    private Cct getCct() {
        Cct dipoleCct = createDipoleCct();
        Cct agCct = createAgCct();

        return CctFactory.combineCct(agCct, dipoleCct);
    }

    private Trajectory getTrajectory() {
        return TrajectoryFactory.setStartingPoint(Point2.origin())
                .setStraightLine(DL1, Vector2.xDirect())
                .addArcLine(trajectoryBigR, false, dipoleCctAngle)
                .addStraitLine(GAP1)//gap1
                .addStraitLine(QS1_LEN)//QS1_LEN
                .addStraitLine(GAP2)//GAP2
                .addStraitLine(QS2_LEN)//QS2_LEN
                .addStraitLine(GAP2)//GAP2
                .addStraitLine(QS1_LEN)//QS1_LEN
                .addStraitLine(GAP1)//gap1
                .addArcLine(trajectoryBigR, false, dipoleCctAngle)
                .addStraitLine(DL1);

    }

    private Cct createAgCct() {
        //public static Cct createAgCct(double smallRInner,
        //                                  double smallROuter,
        //                                  double bigR,
        //                                  double[] angles,
        //                                  int[] windingNumbers,
        //                                  double a0BipolarInners,
        //                                  double a1QuadrupleInners,
        //                                  double a2SextupleInners,
        //                                  double IInner,
        //                                  double a0BipolarOuters,
        //                                  double a1QuadrupleOuters,
        //                                  double a2SextupleOuters,
        //                                  double IOuter,
        //                                  int numberPerWinding)
        return CctFactory.createAgCct(agCctSmallRInner, agCctSmallROuter, agCCTBigR,
                new double[]{agCctAngle0, agCctAngle1},
                new int[]{agCctWindingNumber0, agCctWindingNumber1},
                agCctA0Inner, agCctA1Inner, agCctA2Inner, agCctIInner,
                agCctA0Outer, agCctA1Outer, agCctA2Outer, agCctIOuter,
                numberPerWinding);
    }

    private Cct createDipoleCct() {
        return CctFactory.createDipoleCct(
                dipoleCctSmallRInner, dipoleCctSmallROuter, dipoleCctBigR,
                dipoleCctAngle, dipoleCctWindingNumber,
                dipoleCctA0Inner, dipoleCctA1Inner, dipoleCctA2Inner, dipoleCctIInner,
                dipoleCctA0Outer, dipoleCctA1Outer, dipoleCctA2Outer, dipoleCctIOuter,
                numberPerWinding
        );
    }

    private final BaseUtils.Timer timer = new BaseUtils.Timer();

    private void logPastTime(String msg) {
        timer.printPeriodAfterInitial(Logger.getLogger(), msg);
    }

    BaseUtils.Switcher<String> switcher = BaseUtils.Switcher.creat(
            Plot2d.BLACK_LINE,
            Plot2d.YELLOW_LINE,
            Plot2d.RED_LINE,
            Plot2d.BLUE_LINE,
            Plot2d.GREEN_LINE,
            Plot2d.RED_DASH,
            Plot2d.PINK_DASH,
            Plot2d.BLUE_DASH,
            Plot2d.BLACK_DASH
    );

    // QS 123
    private double QS1_GRADIENT = 53.3; //T m-1
    private double QS1_SECOND_GRADIENT = 211.1; //T m-2
    private double QS2_GRADIENT = -37.7; //T m-1
    private double QS2_SECOND_GRADIENT = 355.6; //T m-2
    private double QS1_APERTURE_MM = 30;
    private double QS2_APERTURE_MM = 30;

    // 偏转半径
    private final double trajectoryBigR = 0.75;
    private final double dipoleCctBigR = 0.75;
    private double agCCTBigR = 750.2 * MM;

    // 初始绕线位置
    private double dipoleCctStartingθInner = 0.0;
    private double dipoleCctStartingθOuter = 0.0;
    private double dipoleCctStartingφInner = 0.0;
    private double dipoleCctStartingφOuter = 0.0;
    private boolean dipoleCctDirectθ = true;
    private double agCctStartingθInner = 0.0; // 起始绕线方向
    private double agCctStartingθOuter = 0.0;
    private double agCctStartingφInner = 0.0;
    private double agCctStartingφOuter = 0.0;
    private boolean agCctDirectθ = true;

    // CCT孔径
    private final double agCctSmallRInner = 23.5 * MM;
    private final double agCctSmallROuter = 33.5 * MM;
    private final double dipoleCctSmallRInner = 43 * MM;
    private final double dipoleCctSmallROuter = 52 * MM;
    // CCT角度
    private final double dipoleCctAngle = 22.5;
    private final double agCctAngle0 = 9.1;
    private final double agCctAngle1 = 13.4;
    // CCT长度
    private double DL1 = 1.1759;
    private double GAP1 = 0.15;
    private double QS1_LEN = 0.2;
    private double QS2_LEN = 0.2;
    private double GAP2 = 0.15;
    private double BETWEEN_CCT225 = GAP1 * 2 + QS1_LEN * 2 + GAP2 * 2 + QS2_LEN;
    private final double CCT_LENGTH = trajectoryBigR * BaseUtils.Converter.angleToRadian(dipoleCctAngle);
    private final double CCT00_LENGTH = trajectoryBigR * BaseUtils.Converter.angleToRadian(agCctAngle0);
    private final double CCT01_LENGTH = trajectoryBigR * BaseUtils.Converter.angleToRadian(agCctAngle1);
    // CCT a0 a1 a2
    private double dipoleCctA0Inner = -dipoleCctSmallRInner * Math.sqrt(3);
    private double dipoleCctA0Outer = dipoleCctSmallROuter * Math.sqrt(3);
    private double dipoleCctA1Inner = Math.pow(dipoleCctSmallRInner, 2) * 0.225;
    private double dipoleCctA1Outer = -Math.pow(dipoleCctSmallROuter, 2) * 0.225;
    private double dipoleCctA2Inner = 0.0;
    private double dipoleCctA2Outer = 0.0;

    private final double agCctA0Inner = 0.0;
    private final double agCctA0Outer = 0.0;
    private final double agCctA1Inner = -Math.pow(agCctSmallRInner, 2) * Math.sqrt(3) * 20;
    private final double agCctA1Outer = Math.pow(agCctSmallROuter, 2) * Math.sqrt(3) * 20;
    private final double agCctA2Inner = 0.0;
    private final double agCctA2Outer = 0.0;

    // 匝数 5mm匝间距 则匝数 = 58.85
    private final int dipoleCctWindingNumber = 59;
    private final int agCctWindingNumber0 = 23;
    private final int agCctWindingNumber1 = 34; // sum = 57

    // 电流
    private double dipoleCctIInner = 9.898121356964111 * kA; // 求解获得
    private double dipoleCctIOuter = -dipoleCctIInner;
    private double agCctIInner = 4618.911905272398;
    private double agCctIOuter = -agCctIInner;

    // CCT每匝分段
    private final int numberPerWinding = 360;


    /*-----------------辅助量----------------------*/
    final static double MM = 1e-3;
    final static double MRAD = 1e-3; // m rad
    final static double kA = 1e3;
    final static double PRESENT = 0.01; // %
    final static double Gs = 1e-4; // 1T = 10000 Gs

    public static void main(String[] args) throws Exception {
        BaseUtils.Timer.printPeriodPerSecondCall(Logger.getLogger());
        StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[1];
        Logger.getLogger().info("{}", stackTraceElement.getClassName());
        Class<?> currentClass = Class.forName(stackTraceElement.getClassName());

        Constructor<?> constructor = currentClass.getConstructor();
        Object newInstance = constructor.newInstance();

        Class<run> runAnnotation = run.class;

        Method[] methods = currentClass.getMethods();
        Arrays.stream(methods)
                .filter(method -> method.isAnnotationPresent(runAnnotation))
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

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    private @interface run {
        int value() default 0;

        String code() default "";
    }
}
