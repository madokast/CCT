package cn.edu.hust.zrx.cct.study.X旧机架研究.前45偏转段;

import cn.edu.hust.zrx.cct.Logger;
import cn.edu.hust.zrx.cct.advanced.COSY;
import cn.edu.hust.zrx.cct.advanced.FirstOrderTransportMatrixSolver;
import cn.edu.hust.zrx.cct.advanced.PolynomialFitter;
import cn.edu.hust.zrx.cct.advanced.TransportCode;
import cn.edu.hust.zrx.cct.base.BaseUtils;
import cn.edu.hust.zrx.cct.base.cct.Cct;
import cn.edu.hust.zrx.cct.base.cct.CctFactory;
import cn.edu.hust.zrx.cct.base.cct.Elements;
import cn.edu.hust.zrx.cct.base.cct.MagnetAble;
import cn.edu.hust.zrx.cct.base.line.Arcs;
import cn.edu.hust.zrx.cct.base.line.Trajectory;
import cn.edu.hust.zrx.cct.base.line.TrajectoryFactory;
import cn.edu.hust.zrx.cct.base.particle.*;
import cn.edu.hust.zrx.cct.base.point.Point2;
import cn.edu.hust.zrx.cct.base.point.Point3;
import cn.edu.hust.zrx.cct.base.python.Plot2d;
import cn.edu.hust.zrx.cct.base.python.Plot3d;
import cn.edu.hust.zrx.cct.base.opticsMagnet.qs.QsHardPlaneMagnet;
import cn.edu.hust.zrx.cct.base.vector.Vector2;
import cn.edu.hust.zrx.cct.base.vector.Vector3;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Description
 * A20200402对比研究
 * y方向有问题
 * <p>
 * 但是我的代码，完美
 * <p>
 * Data
 * 19:24
 *
 * @author zrx
 * @version 1.0
 */

@SuppressWarnings("all")
public class A20200402对比研究 {

    @run(1)
    public void 画图() {
        List<QsHardPlaneMagnet> qs = get3QS();
        Cct allCctIn45 = getAllCctIn45();
        Trajectory trajectory = getTrajectory();

        trajectory.plot3d();
        allCctIn45.plot3();
        qs.forEach(q -> q.plot3d(Plot2d.RED_LINE));

        Plot3d.setCenter(
                trajectory.pointAt(trajectory.getLength() / 2).toPoint3(),
                trajectory.getLength()
        );

        Plot3d.offAxisAndBgColor();

        Plot3d.showThread();
    }

    @run(2)
    public void 二极场分布() {
        List<QsHardPlaneMagnet> qs = get3QS();
        Cct allCctIn45 = getAllCctIn45();
        Trajectory trajectory = getTrajectory();

        List<List<Point2>> collect = allCctIn45.getSoleLayerCctList()
                .stream()
                .parallel()
                .map(soleLayerCct -> soleLayerCct.magnetBzAlongTrajectory(trajectory, MM))
                .collect(Collectors.toList());

        List<List<Point2>> collect1 = qs.stream()
                .map(qsHardPlaneMagnet -> qsHardPlaneMagnet.magnetBzAlongTrajectory(trajectory, MM))
                .collect(Collectors.toList());

        List<Point2> cctAdd = Point2.addOnY(collect);
        List<Point2> qsAdd = Point2.addOnY(collect1);
        List<Point2> sum = Point2.addOnY(cctAdd, qsAdd);

        Plot2d.plot2(sum, Plot2d.BLACK_LINE);
        Plot2d.plot2(List.of(
                Point2.create(0, 0),
                Point2.create(DL1, 0),
                Point2.create(DL1, -Protons.getMagneticStiffness(250) / trajectoryBigR),
                Point2.create(DL1 + CCT_LENGTH, -Protons.getMagneticStiffness(250) / trajectoryBigR),
                Point2.create(DL1 + CCT_LENGTH, 0),
                Point2.create(DL1 + CCT_LENGTH + BETWEEN_CCT225, 0),
                Point2.create(DL1 + CCT_LENGTH + BETWEEN_CCT225, -Protons.getMagneticStiffness(250) / trajectoryBigR),
                Point2.create(DL1 + CCT_LENGTH + BETWEEN_CCT225 + CCT_LENGTH, -Protons.getMagneticStiffness(250) / trajectoryBigR),
                Point2.create(DL1 + CCT_LENGTH + BETWEEN_CCT225 + CCT_LENGTH, 0),
                Point2.create(DL1 + CCT_LENGTH + BETWEEN_CCT225 + CCT_LENGTH + DL1, 0)
        ), Plot2d.RED_DASH);

        Plot2d.legend(18, "magnetic distribution", "SCOFF");
        Plot2d.info("s/m", "T", "", 18);
        Plot2d.showThread();
    }

    @run(3)
    public void 四极场分布() {
        List<QsHardPlaneMagnet> qs = get3QS();
        Cct allCctIn45 = getAllCctIn45();
        Trajectory trajectory = getTrajectory();

        List<List<Point2>> collect = allCctIn45.getSoleLayerCctList()
                .stream()
                .parallel()
                .map(soleLayerCct -> soleLayerCct.magnetGradientAlongTrajectoryFast(trajectory, MM, 3 * MM))
                .collect(Collectors.toList());

        List<List<Point2>> collect1 = qs.stream()
                .map(qsHardPlaneMagnet -> qsHardPlaneMagnet.magnetGradientAlongTrajectoryFast(trajectory, MM, 3 * MM))
                .collect(Collectors.toList());

        List<Point2> cctAdd = Point2.addOnY(collect);
        List<Point2> qsAdd = Point2.addOnY(collect1);
        List<Point2> sum = Point2.addOnY(cctAdd, qsAdd);

        Plot2d.plot2(sum, Plot2d.BLACK_LINE);
        Plot2d.plot2(List.of(
                Point2.create(0, 0),
                Point2.create(DL1, 0),
                Point2.create(DL1, 58.75),
                Point2.create(DL1 + CCT00_LENGTH, 58.75),
                Point2.create(DL1 + CCT00_LENGTH, -58.75),
                Point2.create(DL1 + CCT_LENGTH, -58.75),
                Point2.create(DL1 + CCT_LENGTH, 0),
                Point2.create(DL1 + CCT_LENGTH + GAP1, 0),
                Point2.create(DL1 + CCT_LENGTH + GAP1, 53.3),
                Point2.create(DL1 + CCT_LENGTH + GAP1 + QS1_LEN, 53.3),
                Point2.create(DL1 + CCT_LENGTH + GAP1 + QS1_LEN, 0),
                Point2.create(DL1 + CCT_LENGTH + GAP1 + QS1_LEN + GAP2, 0),
                Point2.create(DL1 + CCT_LENGTH + GAP1 + QS1_LEN + GAP2, -37.7),
                Point2.create(DL1 + CCT_LENGTH + GAP1 + QS1_LEN + GAP2 + QS2_LEN, -37.7),
                Point2.create(DL1 + CCT_LENGTH + GAP1 + QS1_LEN + GAP2 + QS2_LEN, 0),
                Point2.create(DL1 + CCT_LENGTH + GAP1 + QS1_LEN + GAP2 + QS2_LEN + GAP2, 0),
                Point2.create(DL1 + CCT_LENGTH + GAP1 + QS1_LEN + GAP2 + QS2_LEN + GAP2, 53.3),
                Point2.create(DL1 + CCT_LENGTH + GAP1 + QS1_LEN + GAP2 + QS2_LEN + GAP2 + QS1_LEN, 53.3),
                Point2.create(DL1 + CCT_LENGTH + GAP1 + QS1_LEN + GAP2 + QS2_LEN + GAP2 + QS1_LEN, 0),
                Point2.create(DL1 + CCT_LENGTH + GAP1 + QS1_LEN + GAP2 + QS2_LEN + GAP2 + QS1_LEN + GAP1, 0),
                Point2.create(DL1 + CCT_LENGTH + BETWEEN_CCT225, -58.7),
                Point2.create(DL1 + CCT_LENGTH + BETWEEN_CCT225 + CCT01_LENGTH, -58.7),
                Point2.create(DL1 + CCT_LENGTH + BETWEEN_CCT225 + CCT01_LENGTH, 58.7),
                Point2.create(DL1 + CCT_LENGTH + BETWEEN_CCT225 + CCT_LENGTH, 58.7),
                Point2.create(DL1 + CCT_LENGTH + BETWEEN_CCT225 + CCT_LENGTH, 0),
                Point2.create(DL1 + CCT_LENGTH + BETWEEN_CCT225 + CCT_LENGTH + DL1, 0)
        ), Plot2d.RED_DASH);

        Plot2d.legend(18, "magnetic distribution", "SCOFF");
        Plot2d.info("s/m", "T/m", "", 18);
        Plot2d.showThread();
    }

    @run(4)
    public void 六极场分布() {
        List<QsHardPlaneMagnet> qs = get3QS();
        Cct allCctIn45 = getAllCctIn45();
        Trajectory trajectory = getTrajectory();

        List<List<Point2>> collect = allCctIn45.getSoleLayerCctList()
                .stream()
                .parallel()
                .map(soleLayerCct -> soleLayerCct.magnetSecondGradientAlongTrajectory(trajectory, MM, 3 * MM))
                .collect(Collectors.toList());

        List<List<Point2>> collect1 = qs.stream()
                .map(qsHardPlaneMagnet -> qsHardPlaneMagnet.magnetSecondGradientAlongTrajectory(trajectory, MM, 3 * MM))
                .collect(Collectors.toList());

        List<Point2> cctAdd = Point2.addOnY(collect);
        List<Point2> qsAdd = Point2.addOnY(collect1);
        List<Point2> sum = Point2.addOnY(cctAdd, qsAdd);

        Plot2d.plot2(sum, Plot2d.BLACK_LINE);
        Plot2d.plot2(List.of(
                Point2.create(0, 0),
                Point2.create(DL1 + CCT_LENGTH + GAP1, 0),
                Point2.create(DL1 + CCT_LENGTH + GAP1, 211.1),
                Point2.create(DL1 + CCT_LENGTH + GAP1 + QS1_LEN, 211.1),
                Point2.create(DL1 + CCT_LENGTH + GAP1 + QS1_LEN, 0),
                Point2.create(DL1 + CCT_LENGTH + GAP1 + QS1_LEN + GAP2, 0),
                Point2.create(DL1 + CCT_LENGTH + GAP1 + QS1_LEN + GAP2, 355.6),
                Point2.create(DL1 + CCT_LENGTH + GAP1 + QS1_LEN + GAP2 + QS2_LEN, 355.6),
                Point2.create(DL1 + CCT_LENGTH + GAP1 + QS1_LEN + GAP2 + QS2_LEN, 0),
                Point2.create(DL1 + CCT_LENGTH + GAP1 + QS1_LEN + GAP2 * 2 + QS2_LEN, 0),
                Point2.create(DL1 + CCT_LENGTH + GAP1 + QS1_LEN + GAP2 * 2 + QS2_LEN, 211.1),
                Point2.create(DL1 + CCT_LENGTH + GAP1 + QS1_LEN * 2 + GAP2 * 2 + QS2_LEN, 211.1),
                Point2.create(DL1 + CCT_LENGTH + GAP1 + QS1_LEN * 2 + GAP2 * 2 + QS2_LEN, 0),
                Point2.create(DL1 + CCT_LENGTH + BETWEEN_CCT225 + CCT_LENGTH + DL1, 0)
        ), Plot2d.RED_DASH);

        Plot2d.info("s/m", "T/(m2)", "", 18);
        Plot2d.legend(18, "magnetic distribution", "SCOFF");
        Plot2d.showThread();
    }

    @run(5)
    public void 单粒子跟踪() {
        Elements elementsOfAll = getElementsOfAll();
        Trajectory trajectory = getTrajectory();

        RunningParticle rp0 = ParticleFactory.createIdealProtonAtTrajectory250MeV(trajectory);

        List<ParticleRunner.Point3WithDistance> point3WithDistances =
                ParticleRunner.runGetPoint3WithDistance(rp0, elementsOfAll, trajectory.getLength(), MM);


        List<Point2> collect = point3WithDistances.stream()
                .map(ParticleRunner.Point3WithDistance::getDistanceWithZ).collect(Collectors.toList());


        Plot2d.plot2(collect);


        Plot2d.showThread();


//        trajectory.plot3d();
//
//        Plot3d.plot3(run, Plot2d.RED_LINE);
//
//        Plot3d.setCenter(
//                trajectory.pointAt(trajectory.getLength() / 2).toPoint3(),
//                trajectory.getLength()
//        );
//
//        Plot3d.showThread();
    }

    @run(6)
    public void 包络() {
        {
            double delta = 0;

            List<Point2> xsCosy = cosy包络(true, 2, delta);
            List<Point2> ysCosy = cosy包络(false, 2, delta);
            xsCosy = Point2.convert(xsCosy, 1, -1 / MM);
            ysCosy = Point2.convert(ysCosy, 1, 1 / MM);

            Plot2d.plot2(xsCosy, Plot2d.BLACK_POINT);
            Plot2d.plot2(ysCosy, Plot2d.BLACK_UP_TRI);

            // dp = 0
            List<Point2> xeTrack = track粒子跟踪包络(true, delta);
            List<Point2> yeTrack = track粒子跟踪包络(false, delta);
            xeTrack = Point2.convert(xeTrack, 1, -1 / MM);
            yeTrack = Point2.convert(yeTrack, 1, 1 / MM);

            Plot2d.plot2(xeTrack, Plot2d.BLACK_LINE);
            Plot2d.plot2(yeTrack, Plot2d.BLACK_DASH);

        }

        {
            double delta = 5 * PRESENT;

            List<Point2> xsCosy = cosy包络(true, 2, delta);
            List<Point2> ysCosy = cosy包络(false, 2, delta);
            xsCosy = Point2.convert(xsCosy, 1, -1 / MM);
            ysCosy = Point2.convert(ysCosy, 1, 1 / MM);

            Plot2d.plot2(xsCosy, Plot2d.RED_POINT);
            Plot2d.plot2(ysCosy, Plot2d.RED_UP_TRI);

            // dp = 0
            List<Point2> xeTrack = track粒子跟踪包络(true, delta);
            List<Point2> yeTrack = track粒子跟踪包络(false, delta);
            xeTrack = Point2.convert(xeTrack, 1, -1 / MM);
            yeTrack = Point2.convert(yeTrack, 1, 1 / MM);

            Plot2d.plot2(xeTrack, Plot2d.RED_LINE);
            Plot2d.plot2(yeTrack, Plot2d.RED_DASH);
        }


        Plot2d.legend(12, "cosy-x dp0", "cosy-y dp0", "track-x dp0", "track-y dp0",
                "cosy-x dp5%", "cosy-y dp5%", "track-x dp5%", "track-y dp5%");


        Plot2d.plotXLine(0, getTrajectory().getLength(), 0, Plot2d.BLACK_LINE);
        Plot2d.plotYLines(keyDistance(), -30, 30, Plot2d.GREY_DASH);

        Plot2d.info("s/m", "envelope/mm", "", 18);


        Plot2d.showThread();
    }

    @run(7)
    public void ode() {
        Elements elementsOfAll = getElementsOfAll();
        Trajectory trajectory = getTrajectory();

        TransportCode.TransMatrix transMatrix = FirstOrderTransportMatrixSolver.build()
                .setHKFunction(
                        s -> elementsOfAll.magnetBzAlongTrajectoryAt(trajectory, s) / Protons.getMagneticStiffness(250.0),
                        s -> elementsOfAll.magnetGradientAlongTrajectoryAt(trajectory, 3 * MM, s) / Protons.getMagneticStiffness(250)
                ).isLogger(false)
                .initiate(
                        0.0,
                        trajectory.getLength(),
                        1e-8,
                        MM,
                        1e-3,
                        1e-3,
                        new BaseUtils.Async()
                ).solveToMatrix();

        Logger.getLogger().info("transMatrix = " + transMatrix);
        // ODE
        //+0.927	-0.102	+0.000	+0.000	+0.000	+0.001
        //+1.377	+0.927	+0.000	+0.000	+0.000	-0.015
        //+0.000	+0.000	-0.805	+0.430	+0.000	+0.000
        //+0.000	+0.000	-0.826	-0.801	+0.000	+0.000
        //+0.000	+0.000	+0.000	+0.000	+1.000	+0.000
        //+0.000	+0.000	+0.000	+0.000	+0.000	+1.000


        // TRANSPORT
        //     0.82718  -0.12169   0.00000   0.00000   0.00000   0.01844
        //     1.42912   0.99868   0.00000   0.00000   0.00000  -0.15251
        //     0.00000   0.00000  -0.88625   0.13582   0.00000   0.00000
        //     0.00000   0.00000  -0.88599  -0.99257   0.00000   0.00000
        //     0.00153   0.00000   0.00000   0.00000   1.00000  -0.00932
        //     0.00000   0.00000   0.00000   0.00000   0.00000   1.00000

        // COSY
        // +0.9965   -0.0049   +0.0000   +0.0000   +0.0000   +0.0000
        // +1.4275   +0.9965   +0.0000   +0.0000   +0.0000   -0.0085
        // +0.0000   +0.0000   -0.9950   +0.0112   +0.0000   +0.0000
        // +0.0000   +0.0000   -0.8865   -0.9950   +0.0000   +0.0000
        // +0.0000   +0.0000   +0.0000   +0.0000   +0.0000   +0.0000
        // +0.0085   -0.0000   +0.0000   +0.0000   +1.0000   +0.8032
    }

    @run(8)
    public void 出口相椭圆() {
        boolean xPlane = true;
        double delta = -5 * PRESENT;
        List<PhaseSpaceParticle> pp0;
        List<PhaseSpaceParticle> pp1;

        Elements elementsOfAll = getElementsOfAll();
        Trajectory trajectory = getTrajectory();


        /*----------------COSY-----------------*/

        // 相空间点
        pp0 = PhaseSpaceParticles.phaseSpaceParticlesAlongPositiveEllipseInPlane(xPlane, 3.5 * MM, 7.5 * MM, delta, 128);
        pp1 = PhaseSpaceParticles.phaseSpaceParticlesAlongPositiveEllipseInPlane(xPlane, 3.5 * MM, 7.5 * MM, delta, 128);

        // 转为COSY能量分散
        pp0 = PhaseSpaceParticles.convertDeltaFromMomentumDispersionToEnergyDispersion(pp0, 250);
        pp1 = PhaseSpaceParticles.convertDeltaFromMomentumDispersionToEnergyDispersion(pp1, 250);

        // 运行
        List<PhaseSpaceParticle> pp_cosy1st = cosyMatrixEnd().toFirstOrder().apply(pp0);
        List<PhaseSpaceParticle> pp_cosy2nd = cosyMatrixEnd().apply(pp1);

        // 投影
        List<Point2> projectionToPlaneCOSY1st = PhaseSpaceParticles.projectionToPlane(xPlane, pp_cosy1st);
        List<Point2> projectionToPlaneCOSY2nd = PhaseSpaceParticles.projectionToPlane(xPlane, pp_cosy2nd);

        // 改单位
        projectionToPlaneCOSY1st = Point2.convert(projectionToPlaneCOSY1st, 1 / MM, 1 / MRAD);
        projectionToPlaneCOSY2nd = Point2.convert(projectionToPlaneCOSY2nd, 1 / MM, 1 / MRAD);

        /*----------------TRACKING-----------------*/
        RunningParticle rp = ParticleFactory.createIdealProtonAtTrajectory250MeV(trajectory);
        RunningParticle rpEnd = ParticleFactory.createIdealProtonAtTrajectory250MeV(trajectory, trajectory.getLength());

        pp0 = PhaseSpaceParticles.phaseSpaceParticlesAlongPositiveEllipseInPlane(xPlane, 3.5 * MM, 7.5 * MM, delta, 32);

        List<RunningParticle> ps = ParticleFactory.createParticlesFromPhaseSpaceParticle(rp, rp.computeNaturalCoordinateSystem(), pp0);

        ParticleRunner.runThread(ps, elementsOfAll, trajectory.getLength(), MM);

        List<PhaseSpaceParticle> ppEnd = PhaseSpaceParticles.phaseSpaceParticlesFromRunningParticles(rpEnd, rpEnd.computeNaturalCoordinateSystem(), ps);

        List<Point2> projectionToPlaneTrack = PhaseSpaceParticles.projectionToPlane(xPlane, ppEnd);

        projectionToPlaneTrack = Point2.convert(projectionToPlaneTrack, 1 / MM, 1 / MRAD);

        // 绘图
//        Plot2d.plot2(projectionToPlaneCOSY1st,Plot2d.BLUE_POINT);
//        Plot2d.plot2(projectionToPlaneCOSY2nd,Plot2d.RED_POINT);
        Plot2d.plot2circle(projectionToPlaneCOSY1st, Plot2d.BLUE_LINE);
        Plot2d.plot2circle(projectionToPlaneCOSY2nd, Plot2d.RED_LINE);

        Plot2d.plot2(projectionToPlaneTrack, Plot2d.BLACK_POINT);
        Plot2d.plotPoint(Point2.origin(), Plot2d.GREEN_UP_TRI);


        Plot2d.plot2circle(projectionToPlaneTrack, Plot2d.BLACK_LINE);

        Plot2d.equal();

        Plot2d.legend(18, "COSY 1st", "COSY 2nd", "Tracking", "Center");

        if (xPlane)
            Plot2d.info("x/mm", "xp/mrad", "x Plane", 18);
        else
            Plot2d.info("y/mm", "yp/mrad", "y Plane", 18);

        Plot2d.showThread();

    }

    @run(9)
    public void 色散分析() {

        List<Point2> track色散函数R16 = track色散函数R16(); // r16 m/1

        track色散函数R16 = Point2.convert(track色散函数R16, 1, 1 / MM * PRESENT);

        List<Point2> cosy色散函数 = cosy色散函数();

        // !!!!!!!
        // cosy色散函数 中的是动量分散，注意转为能量分散
        cosy色散函数 = Point2.convert(cosy色散函数, x -> x, r16Cosy -> COSY.convertR16InCosyToTransportPronTonOnly(r16Cosy, 250));


        Plot2d.plot2(cosy色散函数, Plot2d.RED_POINT);
        Plot2d.plot2(track色散函数R16, Plot2d.BLACK_LINE);
        Plot2d.plotYLines(keyDistance(), cosy色散函数, track色散函数R16);

        Plot2d.info("s/m", "R16(mm/%)", "", 18);
        Plot2d.legend(18, "cosy R16", "track R16");

        Plot2d.showThread();
    }


    @run(10)
    public void 其他小测试(){
        Cct dipoleCct = createDipoleCct();
        Vector3 vector3 = dipoleCct.magnetAt(Point3.origin());

        Logger.getLogger().info("vector3 = " + vector3);
        //[7.268938341807897E-5, -3.654342079159943E-4, 0.0025707585428348875]


        Cct agCct = createAgCct();

        Vector3 vector31 = agCct.magnetAt(Point3.origin());

        Logger.getLogger().info("vector31 = " + vector31);
        //[-1.4705561346250172E-5, 1.984386025561039E-5, 5.1794020466365825E-6]
    }

    //--------------------  COSY  ---------------------------
    private List<Double> keyDistance() {
        return List.of(
                0., // start
                DL1, // BEFORE CCT1
                DL1 + CCT_LENGTH, // AFTER CCT1
                DL1 + CCT_LENGTH + GAP1 + QS1_LEN, // QS1
                DL1 + CCT_LENGTH + GAP1 + QS1_LEN + GAP2 + QS2_LEN, // QS2
                DL1 + CCT_LENGTH + GAP1 + QS1_LEN + GAP2 + QS2_LEN + GAP2 + QS1_LEN,  // 2ND QS1
                DL1 + CCT_LENGTH + BETWEEN_CCT225, // BEFORE CCT2
                DL1 + CCT_LENGTH + BETWEEN_CCT225 + CCT_LENGTH, // AFTER CCT2
                DL1 + CCT_LENGTH + BETWEEN_CCT225 + CCT_LENGTH + DL1 // END
        );
    }

    private COSY.TransMatrix cosyMatrixBeforeCCT1() {
        return COSY.importMatrix(
                "   1.000000     0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00 100000\n" +
                        "   1.175900      1.000000     0.0000000E+00 0.0000000E+00 0.0000000E+00 010000\n" +
                        "  0.0000000E+00 0.0000000E+00  1.000000     0.0000000E+00 0.0000000E+00 001000\n" +
                        "  0.0000000E+00 0.0000000E+00  1.175900      1.000000     0.0000000E+00 000100\n" +
                        "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00  1.000000     000010\n" +
                        "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00 0.2289176     000001\n" +
                        "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00-0.3285352     020000\n" +
                        " -0.6570704     0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00 010001\n" +
                        "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00-0.3285352     000200\n" +
                        "  0.0000000E+00 0.0000000E+00-0.6570704     0.0000000E+00 0.0000000E+00 000101\n" +
                        "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00-0.1918721     000002"
        );
    }

    private COSY.TransMatrix cosyMatrixAfterCCT1() {
        return COSY.importMatrix(
                "  0.5479324    -0.3692074     0.0000000E+00 0.0000000E+00 0.1680138     100000\n" +
                        "  0.9581484      1.179424     0.0000000E+00 0.0000000E+00 0.2290133     010000\n" +
                        "  0.0000000E+00 0.0000000E+00  1.238480     -2.412454     0.0000000E+00 001000\n" +
                        "  0.0000000E+00 0.0000000E+00  1.720156     -2.543277     0.0000000E+00 000100\n" +
                        "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00  1.000000     000010\n" +
                        " -0.3549835E-01-0.2827129     0.0000000E+00 0.0000000E+00 0.2838225     000001\n" +
                        "   1.293624      4.590501     0.0000000E+00 0.0000000E+00-0.1156784     200000\n" +
                        "   2.697750      8.667623     0.0000000E+00 0.0000000E+00-0.5091435E-01 110000\n" +
                        "   1.331379      3.777810     0.0000000E+00 0.0000000E+00-0.3278719     020000\n" +
                        "  0.0000000E+00 0.0000000E+00-0.5913558      3.476974     0.0000000E+00 101000\n" +
                        "  0.0000000E+00 0.0000000E+00-0.6407000      5.436576     0.0000000E+00 011000\n" +
                        " -0.4166564E-01  2.655721     0.0000000E+00 0.0000000E+00-0.2527618     002000\n" +
                        "  0.0000000E+00 0.0000000E+00-0.9248732      5.416455     0.0000000E+00 100100\n" +
                        "  0.0000000E+00 0.0000000E+00 -1.048482      8.277645     0.0000000E+00 010100\n" +
                        "  0.5830007E-01  8.131354     0.0000000E+00 0.0000000E+00-0.7630878     001100\n" +
                        "  0.3330313     0.9379569     0.0000000E+00 0.0000000E+00-0.5452429E-01 100001\n" +
                        " -0.1400793      1.017708     0.0000000E+00 0.0000000E+00-0.1785227     010001\n" +
                        "  0.0000000E+00 0.0000000E+00-0.1032713     0.4380811     0.0000000E+00 001001\n" +
                        "  0.1663142      6.243591     0.0000000E+00 0.0000000E+00-0.9447142     000200\n" +
                        "  0.0000000E+00 0.0000000E+00 -1.063038      2.467097     0.0000000E+00 000101\n" +
                        "  0.2711393E-01 0.8701740E-01 0.0000000E+00 0.0000000E+00-0.2389992     000002"
        );
    }

    private COSY.TransMatrix cosyMatrixAfterQS1() {
        return COSY.importMatrix(
                "  0.2287698     -2.073383     0.0000000E+00 0.0000000E+00 0.1680138     100000\n" +
                        "  0.8765549     -3.573173     0.0000000E+00 0.0000000E+00 0.2290133     010000\n" +
                        "  0.0000000E+00 0.0000000E+00 0.7329625     0.8727016     0.0000000E+00 001000\n" +
                        "  0.0000000E+00 0.0000000E+00  1.382212      3.010057     0.0000000E+00 000100\n" +
                        "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00  1.000000     000010\n" +
                        " -0.9488203E-01 0.1255101     0.0000000E+00 0.0000000E+00 0.3519586     000001\n" +
                        "   1.663907     -6.950427     0.0000000E+00 0.0000000E+00-0.2326305     200000\n" +
                        "   2.302488     -22.15303     0.0000000E+00 0.0000000E+00-0.2828416     110000\n" +
                        " -0.2571249     -22.12907     0.0000000E+00 0.0000000E+00-0.5879686     020000\n" +
                        "  0.0000000E+00 0.0000000E+00  1.956987      16.78140     0.0000000E+00 101000\n" +
                        "  0.0000000E+00 0.0000000E+00  4.754572      41.67096     0.0000000E+00 011000\n" +
                        "   1.623288      8.438349     0.0000000E+00 0.0000000E+00-0.5743553     002000\n" +
                        "  0.0000000E+00 0.0000000E+00  3.116982      27.32066     0.0000000E+00 100100\n" +
                        "  0.0000000E+00 0.0000000E+00  7.460030      67.93931     0.0000000E+00 010100\n" +
                        "   5.265963      27.91655     0.0000000E+00 0.0000000E+00 -1.424952     001100\n" +
                        "  0.7183854    -0.5374840     0.0000000E+00 0.0000000E+00-0.6912452E-01 100001\n" +
                        "  0.5926088      3.790168     0.0000000E+00 0.0000000E+00-0.1442374     010001\n" +
                        "  0.0000000E+00 0.0000000E+00 0.1926675    -0.9440069     0.0000000E+00 001001\n" +
                        "   4.265637      23.36861     0.0000000E+00 0.0000000E+00 -1.357984     000200\n" +
                        "  0.0000000E+00 0.0000000E+00-0.5606737     -3.238991     0.0000000E+00 000101\n" +
                        "  0.4627381E-01-0.3539130     0.0000000E+00 0.0000000E+00-0.3006609     000002"
        );
    }

    private COSY.TransMatrix cosyMatrixAfterQS2() {
        return COSY.importMatrix(
                " -0.5681410     -3.034779     0.0000000E+00 0.0000000E+00 0.1680138     100000\n" +
                        " -0.3389771     -3.570805     0.0000000E+00 0.0000000E+00 0.2290133     010000\n" +
                        "  0.0000000E+00 0.0000000E+00 0.7660123     -1.800034     0.0000000E+00 001000\n" +
                        "  0.0000000E+00 0.0000000E+00  1.834271     -3.004847     0.0000000E+00 000100\n" +
                        "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00  1.000000     000010\n" +
                        " -0.7315902E-01-0.9506020E-01 0.0000000E+00 0.0000000E+00 0.4200946     000001\n" +
                        " -0.5230252     -3.634073     0.0000000E+00 0.0000000E+00-0.7470038     200000\n" +
                        "  -6.316142     -31.50026     0.0000000E+00 0.0000000E+00 -1.826808     110000\n" +
                        "  -9.528147     -40.44400     0.0000000E+00 0.0000000E+00 -1.768483     020000\n" +
                        "  0.0000000E+00 0.0000000E+00  7.282113      13.46040     0.0000000E+00 101000\n" +
                        "  0.0000000E+00 0.0000000E+00  14.72701    -0.8960265     0.0000000E+00 011000\n" +
                        "   3.366250     -2.628960     0.0000000E+00 0.0000000E+00-0.6536260     002000\n" +
                        "  0.0000000E+00 0.0000000E+00  12.48333      31.46326     0.0000000E+00 100100\n" +
                        "  0.0000000E+00 0.0000000E+00  23.54439     0.2978533     0.0000000E+00 010100\n" +
                        "   8.421114     -36.98984     0.0000000E+00 0.0000000E+00 -1.797871     001100\n" +
                        "   1.353090      3.837127     0.0000000E+00 0.0000000E+00-0.4531336E-01 100001\n" +
                        "   3.145489      10.58706     0.0000000E+00 0.0000000E+00-0.1011411     010001\n" +
                        "  0.0000000E+00 0.0000000E+00 0.2140223      2.541293     0.0000000E+00 001001\n" +
                        "   4.113271     -61.83051     0.0000000E+00 0.0000000E+00 -1.909733     000200\n" +
                        "  0.0000000E+00 0.0000000E+00-0.7249110      8.628305     0.0000000E+00 000101\n" +
                        " -0.8825391E-01-0.3774416     0.0000000E+00 0.0000000E+00-0.3586644     000002"
        );
    }

    private COSY.TransMatrix cosyMatrixAfterSecondQS1() {
        return COSY.importMatrix(
                "  -1.129535      2.051592     0.0000000E+00 0.0000000E+00 0.1680138     100000\n" +
                        "  -1.133493      1.173461     0.0000000E+00 0.0000000E+00 0.2290133     010000\n" +
                        "  0.0000000E+00 0.0000000E+00 0.3144687    -0.1458455     0.0000000E+00 001000\n" +
                        "  0.0000000E+00 0.0000000E+00  1.341823      2.557651     0.0000000E+00 000100\n" +
                        "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00  1.000000     000010\n" +
                        " -0.6823598E-01 0.2726842     0.0000000E+00 0.0000000E+00 0.4882306     000001\n" +
                        "  -3.360906     -18.30245     0.0000000E+00 0.0000000E+00 -1.272839     200000\n" +
                        "  -15.80665     -15.00652     0.0000000E+00 0.0000000E+00 -3.043487     110000\n" +
                        "  -17.96507      16.89002     0.0000000E+00 0.0000000E+00 -2.504545     020000\n" +
                        "  0.0000000E+00 0.0000000E+00  15.06191      49.18862     0.0000000E+00 101000\n" +
                        "  0.0000000E+00 0.0000000E+00  19.69927      56.11801     0.0000000E+00 011000\n" +
                        "   1.577682     -10.68516     0.0000000E+00 0.0000000E+00-0.8478815     002000\n" +
                        "  0.0000000E+00 0.0000000E+00  27.16481      74.78479     0.0000000E+00 100100\n" +
                        "  0.0000000E+00 0.0000000E+00  29.87420      65.37881     0.0000000E+00 010100\n" +
                        "  -3.003468     -18.87277     0.0000000E+00 0.0000000E+00 -2.355674     001100\n" +
                        "   1.668567     -9.118445     0.0000000E+00 0.0000000E+00-0.8174119E-01 100001\n" +
                        "   4.624998     -15.81740     0.0000000E+00 0.0000000E+00-0.1324392     010001\n" +
                        "  0.0000000E+00 0.0000000E+00  1.674257      6.516982     0.0000000E+00 001001\n" +
                        "  -11.10276      6.501263     0.0000000E+00 0.0000000E+00 -2.430567     000200\n" +
                        "  0.0000000E+00 0.0000000E+00  2.834050      12.87613     0.0000000E+00 000101\n" +
                        " -0.1689991     0.1967531     0.0000000E+00 0.0000000E+00-0.4173160     000002"
        );
    }

    private COSY.TransMatrix cosyMatrixBeforeCct2() {
        return COSY.importMatrix(
                " -0.8217959      2.051592     0.0000000E+00 0.0000000E+00 0.1680138     100000\n" +
                        " -0.9574738      1.173461     0.0000000E+00 0.0000000E+00 0.2290133     010000\n" +
                        "  0.0000000E+00 0.0000000E+00 0.2925919    -0.1458455     0.0000000E+00 001000\n" +
                        "  0.0000000E+00 0.0000000E+00  1.725471      2.557651     0.0000000E+00 000100\n" +
                        "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00  1.000000     000010\n" +
                        " -0.2733335E-01 0.2726842     0.0000000E+00 0.0000000E+00 0.5174318     000001\n" +
                        "  -6.106273     -18.30245     0.0000000E+00 0.0000000E+00 -1.449234     200000\n" +
                        "  -18.05763     -15.00652     0.0000000E+00 0.0000000E+00 -3.245274     110000\n" +
                        "  -15.43157      16.89002     0.0000000E+00 0.0000000E+00 -2.562254     020000\n" +
                        "  0.0000000E+00 0.0000000E+00  22.44020      49.18862     0.0000000E+00 101000\n" +
                        "  0.0000000E+00 0.0000000E+00  28.11697      56.11801     0.0000000E+00 011000\n" +
                        " -0.2509179E-01 -10.68516     0.0000000E+00 0.0000000E+00-0.8487730     002000\n" +
                        "  0.0000000E+00 0.0000000E+00  38.38253      74.78479     0.0000000E+00 100100\n" +
                        "  0.0000000E+00 0.0000000E+00  39.68102      65.37881     0.0000000E+00 010100\n" +
                        "  -5.834384     -18.87277     0.0000000E+00 0.0000000E+00 -2.324409     001100\n" +
                        "  0.1288416     -9.118445     0.0000000E+00 0.0000000E+00-0.1286316     100001\n" +
                        "   2.154032     -15.81740     0.0000000E+00 0.0000000E+00-0.1592593     010001\n" +
                        "  0.0000000E+00 0.0000000E+00  2.664028      6.516982     0.0000000E+00 001001\n" +
                        "  -10.12757      6.501263     0.0000000E+00 0.0000000E+00 -2.704715     000200\n" +
                        "  0.0000000E+00 0.0000000E+00  4.551095      12.87613     0.0000000E+00 000101\n" +
                        " -0.1623417     0.1967531     0.0000000E+00 0.0000000E+00-0.4449078     000002"
        );
    }

    private COSY.TransMatrix cosyMatrixAfterCct2() {
        return COSY.importMatrix(
                " -0.6821685      1.427547     0.0000000E+00 0.0000000E+00 0.8509655E-02 100000\n" +
                        "  -1.176683     0.9964840     0.0000000E+00 0.0000000E+00-0.2095900E-04 010000\n" +
                        "  0.0000000E+00 0.0000000E+00 0.4740570E-01-0.8864910     0.0000000E+00 001000\n" +
                        "  0.0000000E+00 0.0000000E+00  1.181252    -0.9950191     0.0000000E+00 000100\n" +
                        "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00  1.000000     000010\n" +
                        "  0.1002746E-01-0.8509655E-02 0.0000000E+00 0.0000000E+00 0.5742891     000001\n" +
                        "  -15.87812     -7.754847     0.0000000E+00 0.0000000E+00 -3.914207     200000\n" +
                        "  -34.96952     0.3819988E-01 0.0000000E+00 0.0000000E+00 -9.068618     110000\n" +
                        "  -20.56325      17.52382     0.0000000E+00 0.0000000E+00 -6.567810     020000\n" +
                        "  0.0000000E+00 0.0000000E+00  19.13597      5.478693     0.0000000E+00 101000\n" +
                        "  0.0000000E+00 0.0000000E+00  22.46410    -0.1349384E-01 0.0000000E+00 011000\n" +
                        "  -3.341139     -5.632274     0.0000000E+00 0.0000000E+00 -1.294402     002000\n" +
                        "  0.0000000E+00 0.0000000E+00  29.12971     -6.557476     0.0000000E+00 100100\n" +
                        "  0.0000000E+00 0.0000000E+00  26.39921     -22.49508     0.0000000E+00 010100\n" +
                        "  -14.78287     -6.463401     0.0000000E+00 0.0000000E+00 -4.938361     001100\n" +
                        "  -2.726991     -5.144989     0.0000000E+00 0.0000000E+00-0.3825461     100001\n" +
                        "  -1.359476     -10.01323     0.0000000E+00 0.0000000E+00-0.4649243E-02 010001\n" +
                        "  0.0000000E+00 0.0000000E+00  2.632362      1.801903     0.0000000E+00 001001\n" +
                        "  -12.59945      10.79941     0.0000000E+00 0.0000000E+00 -5.779793     000200\n" +
                        "  0.0000000E+00 0.0000000E+00  5.060201      5.525269     0.0000000E+00 000101\n" +
                        " -0.2176690     0.2299405     0.0000000E+00 0.0000000E+00-0.5364178     000002"
        );
    }

    private COSY.TransMatrix cosyMatrixEnd() {
        return COSY.importMatrix(
                "  0.9964840      1.427547     0.0000000E+00 0.0000000E+00 0.8509655E-02 100000\n" +
                        " -0.4917276E-02 0.9964840     0.0000000E+00 0.0000000E+00-0.2095900E-04 010000\n" +
                        "  0.0000000E+00 0.0000000E+00-0.9950191    -0.8864910     0.0000000E+00 001000\n" +
                        "  0.0000000E+00 0.0000000E+00 0.1120935E-01-0.9950191     0.0000000E+00 000100\n" +
                        "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00  1.000000     000010\n" +
                        "  0.2095900E-04-0.8509655E-02 0.0000000E+00 0.0000000E+00 0.8032067     000001\n" +
                        "  -24.99704     -7.754847     0.0000000E+00 0.0000000E+00 -4.583726     200000\n" +
                        "  -34.92460     0.3819988E-01 0.0000000E+00 0.0000000E+00 -10.00332     110000\n" +
                        "  0.4300908E-01  17.52382     0.0000000E+00 0.0000000E+00 -6.894039     020000\n" +
                        "  0.0000000E+00 0.0000000E+00  25.57836      5.478693     0.0000000E+00 101000\n" +
                        "  0.0000000E+00 0.0000000E+00  22.44823    -0.1349384E-01 0.0000000E+00 011000\n" +
                        "  -9.964129     -5.632274     0.0000000E+00 0.0000000E+00 -1.552587     002000\n" +
                        "  0.0000000E+00 0.0000000E+00  21.41878     -6.557476     0.0000000E+00 100100\n" +
                        "  0.0000000E+00 0.0000000E+00-0.5275376E-01 -22.49508     0.0000000E+00 010100\n" +
                        "  -22.38318     -6.463401     0.0000000E+00 0.0000000E+00 -5.517946     001100\n" +
                        "  -9.714982     -5.144989     0.0000000E+00 0.0000000E+00-0.3745641     100001\n" +
                        "  -13.78879     -10.01323     0.0000000E+00 0.0000000E+00 0.9225392E-03 010001\n" +
                        "  0.0000000E+00 0.0000000E+00  5.333706      1.801903     0.0000000E+00 001001\n" +
                        "  0.9956954E-01  10.79941     0.0000000E+00 0.0000000E+00 -6.105064     000200\n" +
                        "  0.0000000E+00 0.0000000E+00  12.21116      5.525269     0.0000000E+00 000101\n" +
                        "  0.5830945E-01 0.2299405     0.0000000E+00 0.0000000E+00-0.7283137     000002"
        );
    }

    //----------------- 粒子跟踪包络 ------------------------------
    private List<Point2> track粒子跟踪包络0(boolean xxPlane, double delta) {
        MagnetAble cct = getElementsOfAll();
        Trajectory trajectory = getTrajectory();

        RunningParticle ip = ParticleFactory.createIdealProtonAtTrajectory250MeV(trajectory);

        List<PhaseSpaceParticle> pp =
                PhaseSpaceParticles.phaseSpaceParticlesAlongPositiveEllipseInPlane(xxPlane, 3.5 * MM, 7.5 * MRAD, delta, 64);

        List<List<BaseUtils.Content.BiContent<Double, RunningParticle>>> runs = ParticleFactory.createParticlesFromPhaseSpaceParticle(ip, ip.computeNaturalCoordinateSystem(), pp)
                .stream()
                .parallel()
                .map(p -> ParticleRunner.runGetAllInfo(p, cct, trajectory.getLength(), MM))
                .collect(Collectors.toList());

        List<List<BaseUtils.Content.BiContent<Double, RunningParticle>>> states = BaseUtils.ListUtils.listList2ListList(runs);

        return states.stream()
                .parallel()
                .map(bis -> {
                    Double distance = bis.get(0).getT1();
                    RunningParticle ipp = ParticleFactory.createIdealProtonAtTrajectory250MeV(trajectory, distance);
                    double max = bis.stream()
                            .map(BaseUtils.Content.BiContent::getT2)
                            .map(rp -> PhaseSpaceParticles.phaseSpaceParticleFromRunningParticle(ipp, ipp.computeNaturalCoordinateSystem(), rp))
                            .map(ppp -> PhaseSpaceParticles.projectionToPlane(xxPlane, ppp))
                            .mapToDouble(Point2::getX)
                            .map(Math::abs)
                            .max()
                            .orElse(10000);

                    return Point2.create(distance, max);
                })
                .collect(Collectors.toList())
                .stream()
                .sorted(Comparator.comparingDouble(Point2::getX))
                .collect(Collectors.toList());
    }

    private List<Point2> track粒子跟踪包络(boolean xxPlane, double delta) {
        if (BaseUtils.Equal.isEqual(delta, 0.0))
            return track粒子跟踪包络0(xxPlane, delta);

        List<Point2> point2s = track粒子跟踪包络0(xxPlane, delta);
        List<Point2> point2s1 = track粒子跟踪包络0(xxPlane, -delta);

        List<Point2> ret = new ArrayList<>();
        for (int i = 0; i < point2s.size(); i++) {
            Point2 point2 = point2s.get(i);
            Point2 point21 = point2s1.get(i);

            ret.add(Point2.create(point2.x, Math.max(point2.y, point21.y)));
        }

        return ret;
    }

    private List<Point2> cosy包络(boolean xxPlane, int order, double delta) {
        if (BaseUtils.Equal.isEqual(delta, 0.0))
            return cosy包络0(xxPlane, order, delta).get(0);

        List<List<Point2>> lists = cosy包络0(xxPlane, order, delta);
        List<List<Point2>> lists1 = cosy包络0(xxPlane, order, -delta);

        List<Point2> point2s = lists.get(0);
        List<Point2> point2s1 = lists1.get(0);

        List<Point2> ret = new ArrayList<>();
        for (int i = 0; i < point2s.size(); i++) {
            Point2 point2 = point2s.get(i);
            Point2 point21 = point2s1.get(i);

            ret.add(Point2.create(point2.x, Math.max(point2.y, point21.y)));
        }

        return ret;
    }

    private List<List<Point2>> cosy包络0(boolean xxPlane, int order, double delta) {

        boolean firstOrder = order == 1;

        List<PhaseSpaceParticle> ppStart;


        // START
        ppStart = phaseSpaceParticlesStart(xxPlane, delta, 64);
        ppStart = PhaseSpaceParticles.convertDeltaFromMomentumDispersionToEnergyDispersion(ppStart, 250.0);
        double xStart = PhaseSpaceParticles.projectionToPlane(xxPlane, ppStart).stream().mapToDouble(Point2::getX).map(Math::abs).max().orElse(10000);
        double xpStart = PhaseSpaceParticles.projectionToPlane(xxPlane, ppStart).stream().mapToDouble(Point2::getY).map(Math::abs).max().orElse(10000);


        //BeforeCct1
        ppStart = phaseSpaceParticlesStart(xxPlane, delta, 64);
        ppStart = PhaseSpaceParticles.convertDeltaFromMomentumDispersionToEnergyDispersion(ppStart, 250.0);
        ppStart = cosyMatrixBeforeCCT1().toFirstOrder(firstOrder).apply(ppStart);
        double xBeforeCct1 = PhaseSpaceParticles.projectionToPlane(xxPlane, ppStart).stream().mapToDouble(Point2::getX).map(Math::abs).max().orElse(10000);
        double xpBeforeCct1 = PhaseSpaceParticles.projectionToPlane(xxPlane, ppStart).stream().mapToDouble(Point2::getY).map(Math::abs).max().orElse(10000);

        //AfterCct1
        ppStart = phaseSpaceParticlesStart(xxPlane, delta, 64);
        ppStart = PhaseSpaceParticles.convertDeltaFromMomentumDispersionToEnergyDispersion(ppStart, 250.0);
        ppStart = cosyMatrixAfterCCT1().toFirstOrder(firstOrder).apply(ppStart);
        double xAfterCct1 = PhaseSpaceParticles.projectionToPlane(xxPlane, ppStart).stream().mapToDouble(Point2::getX).map(Math::abs).max().orElse(10000);
        double xpAfterCct1 = PhaseSpaceParticles.projectionToPlane(xxPlane, ppStart).stream().mapToDouble(Point2::getY).map(Math::abs).max().orElse(10000);

        // AfterQs1
        ppStart = phaseSpaceParticlesStart(xxPlane, delta, 64);
        ppStart = PhaseSpaceParticles.convertDeltaFromMomentumDispersionToEnergyDispersion(ppStart, 250.0);
        ppStart = cosyMatrixAfterQS1().toFirstOrder(firstOrder).apply(ppStart);
        double xAfterQs1 = PhaseSpaceParticles.projectionToPlane(xxPlane, ppStart).stream().mapToDouble(Point2::getX).map(Math::abs).max().orElse(10000);
        double xpAfterQs1 = PhaseSpaceParticles.projectionToPlane(xxPlane, ppStart).stream().mapToDouble(Point2::getY).map(Math::abs).max().orElse(10000);

        // AfterQs2
        ppStart = phaseSpaceParticlesStart(xxPlane, delta, 64);
        ppStart = PhaseSpaceParticles.convertDeltaFromMomentumDispersionToEnergyDispersion(ppStart, 250.0);
        ppStart = cosyMatrixAfterQS2().toFirstOrder(firstOrder).apply(ppStart);
        double xAfterQs2 = PhaseSpaceParticles.projectionToPlane(xxPlane, ppStart).stream().mapToDouble(Point2::getX).map(Math::abs).max().orElse(10000);
        double xpAfterQs2 = PhaseSpaceParticles.projectionToPlane(xxPlane, ppStart).stream().mapToDouble(Point2::getY).map(Math::abs).max().orElse(10000);

        // AfterSecondQs1
        ppStart = phaseSpaceParticlesStart(xxPlane, delta, 64);
        ppStart = PhaseSpaceParticles.convertDeltaFromMomentumDispersionToEnergyDispersion(ppStart, 250.0);
        ppStart = cosyMatrixAfterSecondQS1().toFirstOrder(firstOrder).apply(ppStart);
        double xAfterSecondQs1 = PhaseSpaceParticles.projectionToPlane(xxPlane, ppStart).stream().mapToDouble(Point2::getX).map(Math::abs).max().orElse(10000);
        double xpAfterSecondQs1 = PhaseSpaceParticles.projectionToPlane(xxPlane, ppStart).stream().mapToDouble(Point2::getY).map(Math::abs).max().orElse(10000);

        // BeforeCct2
        ppStart = phaseSpaceParticlesStart(xxPlane, delta, 64);
        ppStart = PhaseSpaceParticles.convertDeltaFromMomentumDispersionToEnergyDispersion(ppStart, 250.0);
        ppStart = cosyMatrixBeforeCct2().toFirstOrder(firstOrder).apply(ppStart);
        double xBeforeCct2 = PhaseSpaceParticles.projectionToPlane(xxPlane, ppStart).stream().mapToDouble(Point2::getX).map(Math::abs).max().orElse(10000);
        double xpBeforeCct2 = PhaseSpaceParticles.projectionToPlane(xxPlane, ppStart).stream().mapToDouble(Point2::getY).map(Math::abs).max().orElse(10000);

        // AfterCct2
        ppStart = phaseSpaceParticlesStart(xxPlane, delta, 64);
        ppStart = PhaseSpaceParticles.convertDeltaFromMomentumDispersionToEnergyDispersion(ppStart, 250.0);
        ppStart = cosyMatrixAfterCct2().toFirstOrder(firstOrder).apply(ppStart);
        double xAfterCct2 = PhaseSpaceParticles.projectionToPlane(xxPlane, ppStart).stream().mapToDouble(Point2::getX).map(Math::abs).max().orElse(10000);
        double xpAfterCct2 = PhaseSpaceParticles.projectionToPlane(xxPlane, ppStart).stream().mapToDouble(Point2::getY).map(Math::abs).max().orElse(10000);

        // end
        ppStart = phaseSpaceParticlesStart(xxPlane, delta, 64);
        ppStart = PhaseSpaceParticles.convertDeltaFromMomentumDispersionToEnergyDispersion(ppStart, 250.0);
        ppStart = cosyMatrixEnd().toFirstOrder(firstOrder).apply(ppStart);
        double xEnd = PhaseSpaceParticles.projectionToPlane(xxPlane, ppStart).stream().mapToDouble(Point2::getX).map(Math::abs).max().orElse(10000);
        double xpEnd = PhaseSpaceParticles.projectionToPlane(xxPlane, ppStart).stream().mapToDouble(Point2::getY).map(Math::abs).max().orElse(10000);


        List<List<Point2>> pll = new ArrayList<>(keyDistance().size());


        //                DL1, // BEFORE CCT1
        //                DL1+CCT_LENGTH, // AFTER CCT1
        //                DL1+CCT_LENGTH+GAP1+QS1_LEN, // QS1
        //                DL1+CCT_LENGTH+GAP1+QS1_LEN+GAP2+QS2_LEN, // QS2

        //                DL1+CCT_LENGTH+GAP1+QS1_LEN+GAP2+QS2_LEN+GAP2+QS1_LEN,  // 2ND QS1
        //                DL1+CCT_LENGTH+BETWEEN_CCT225, // BEFORE CCT2
        //                DL1+CCT_LENGTH+BETWEEN_CCT225+CCT_LENGTH, // AFTER CCT2
        //                DL1+CCT_LENGTH+BETWEEN_CCT225+CCT_LENGTH+DL1 // END

        pll.add(List.of(
                Point2.create(keyDistance().get(0), xStart),
                Point2.create(keyDistance().get(1), xBeforeCct1),
                Point2.create(keyDistance().get(2), xAfterCct1),
                Point2.create(keyDistance().get(3), xAfterQs1),
                Point2.create(keyDistance().get(4), xAfterQs2),
                Point2.create(keyDistance().get(5), xAfterSecondQs1),
                Point2.create(keyDistance().get(6), xBeforeCct2),
                Point2.create(keyDistance().get(7), xAfterCct2),
                Point2.create(keyDistance().get(8), xEnd)
        ));

        pll.add(List.of(
                Point2.create(keyDistance().get(0), xpStart),
                Point2.create(keyDistance().get(1), xpBeforeCct1),
                Point2.create(keyDistance().get(2), xpAfterCct1),
                Point2.create(keyDistance().get(3), xpAfterQs1),
                Point2.create(keyDistance().get(4), xpAfterQs2),
                Point2.create(keyDistance().get(5), xpAfterSecondQs1),
                Point2.create(keyDistance().get(6), xpBeforeCct2),
                Point2.create(keyDistance().get(7), xpAfterCct2),
                Point2.create(keyDistance().get(8), xpEnd)
        ));


        return pll;
    }

    private List<PhaseSpaceParticle> phaseSpaceParticlesStart(boolean xxPlane, double delta, int num) {
        return xxPlane ? PhaseSpaceParticles.phaseSpaceParticlesAlongPositiveEllipseInXXpPlane(
                3.5 * MM, 7.5 * MRAD, delta, num) :
                PhaseSpaceParticles.phaseSpaceParticlesAlongPositiveEllipseInYYpPlane(
                        3.5 * MM, 7.5 * MRAD, delta, num);
    }

    //------------------ 色散 -----------------------
    private List<Point2> track色散函数R16() {
        MagnetAble cct = getElementsOfAll();
        Trajectory trajectory = getTrajectory();
        double length = trajectory.getLength() + 0.1;

        List<RunningParticle> particles = BaseUtils.Python.linspaceStream(-5 * PRESENT, 5 * PRESENT, 2)
                .mapToObj(delta -> {
                    double k = Protons.getKineticEnergy_MeV_AfterMomentumDispersion(250., delta);
                    RunningParticle ipk = ParticleFactory.createIdealProtonAtTrajectory(trajectory, k);
                    return ipk;
                })
                .collect(Collectors.toList());

        PolynomialFitter fitter = PolynomialFitter.build(1);

        List<Point2> distance_R16 = IntStream.range(0, (int) (length / MM) + 1)
                .mapToObj(i -> {
                    double distance = particles.get(0).getDistance();
                    RunningParticle ip = ParticleFactory.createIdealProtonAtTrajectory250MeV(trajectory, distance);
                    List<Point2> delta_x = particles.stream().map(p -> PhaseSpaceParticles.phaseSpaceParticleFromRunningParticle(ip, ip.computeNaturalCoordinateSystem(), p))
                            .map(pp -> Point2.create(pp.getDelta(), pp.getX()))
                            .sorted(Comparator.comparingDouble(Point2::getX))
                            .collect(Collectors.toList());
                    double[] fit = fitter.fit(delta_x);


                    particles.forEach(p -> p.runSelf(cct.magnetAt(p.position), MM));

                    return Point2.create(distance, fit[1]);
                })
                .collect(Collectors.toList());


//        Plot2d.plot2(distance_R16);
//        Plot2d.showThread();


        return distance_R16;
    }

    private List<Point2> cosy色散函数() {
//        return List.of(
//                0., // start

//                DL1, // BEFORE CCT1
//                DL1 + CCT_LENGTH, // AFTER CCT1
//                DL1 + CCT_LENGTH + GAP1 + QS1_LEN, // QS1
//                DL1 + CCT_LENGTH + GAP1 + QS1_LEN + GAP2 + QS2_LEN, // QS2

//                DL1 + CCT_LENGTH + GAP1 + QS1_LEN + GAP2 + QS2_LEN + GAP2 + QS1_LEN,  // 2ND QS1
//                DL1 + CCT_LENGTH + BETWEEN_CCT225, // BEFORE CCT2
//                DL1 + CCT_LENGTH + BETWEEN_CCT225 + CCT_LENGTH, // AFTER CCT2
//                DL1 + CCT_LENGTH + BETWEEN_CCT225 + CCT_LENGTH + DL1 // END
//        );

        return List.of(
                Point2.create(keyDistance().get(0), 0.0),

                Point2.create(keyDistance().get(1), cosyMatrixBeforeCCT1().getR(1, 6)),
                Point2.create(keyDistance().get(2), cosyMatrixAfterCCT1().getR(1, 6)),
                Point2.create(keyDistance().get(3), cosyMatrixAfterQS1().getR(1, 6)),
                Point2.create(keyDistance().get(4), cosyMatrixAfterQS2().getR(1, 6)),

                Point2.create(keyDistance().get(5), cosyMatrixAfterSecondQS1().getR(1, 6)),
                Point2.create(keyDistance().get(6), cosyMatrixBeforeCct2().getR(1, 6)),
                Point2.create(keyDistance().get(7), cosyMatrixAfterCct2().getR(1, 6)),
                Point2.create(keyDistance().get(8), cosyMatrixEnd().getR(1, 6))
        );
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


        return CctFactory.positionInXYPlane(cct, Point2.create(DL1, trajectoryBigR), BaseUtils.Converter.angleToRadian(-90));

    }

    private Cct getCct2() {
        Trajectory trajectory = getTrajectory();

        Cct dipoleCct = createDipoleCct();

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
    //孔径
    private final double agCctSmallRInner = 23.5 * MM;
    private final double agCctSmallROuter = 33.5 * MM;
    private final double dipoleCctSmallRInner = 43 * MM;
    private final double dipoleCctSmallROuter = 52 * MM;
    //角度
    private final double dipoleCctAngle = 22.5;
    private final double agCctAngle0 = 9.1;
    private final double agCctAngle1 = 13.4;
    // 长度
    private double DL1 = 1.1759;
    private double GAP1 = 0.15;
    private double QS1_LEN = 0.2;
    private double QS2_LEN = 0.2;
    private double GAP2 = 0.15;
    private double BETWEEN_CCT225 = GAP1 * 2 + QS1_LEN * 2 + GAP2 * 2 + QS2_LEN;
    private final double CCT_LENGTH = trajectoryBigR * BaseUtils.Converter.angleToRadian(dipoleCctAngle);
    private final double CCT00_LENGTH = trajectoryBigR * BaseUtils.Converter.angleToRadian(agCctAngle0);
    private final double CCT01_LENGTH = trajectoryBigR * BaseUtils.Converter.angleToRadian(agCctAngle1);
    // a0 a1 a2
    private final double dipoleCctA0Inner = -dipoleCctSmallRInner * Math.sqrt(3);
    private final double dipoleCctA0Outer = dipoleCctSmallROuter * Math.sqrt(3);
    private double dipoleCctA1Inner = Math.pow(dipoleCctSmallRInner, 2) * 0.225;
    private double dipoleCctA1Outer = -Math.pow(dipoleCctSmallROuter, 2) * 0.225;
    private final double dipoleCctA2Inner = 0.0;
    private final double dipoleCctA2Outer = 0.0;

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
    private final double dipoleCctIInner = 9.898121356964111 * kA; // 求解获得
    private final double dipoleCctIOuter = -dipoleCctIInner;
    public double agCctIInner = 4618.911905272398;
    public double agCctIOuter = -agCctIInner;
    // CCT每匝分段
    private final int numberPerWinding = 360;

    /*-----------------辅助量----------------------*/
    final static double MM = 1e-3;
    final static double MRAD = 1e-3; // m rad
    final static double kA = 1e3;
    final static double PRESENT = 0.01; // %

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
