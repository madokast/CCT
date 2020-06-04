package cn.edu.hust.zrx.cct.study.前45偏转段2;

import cn.edu.hust.zrx.cct.Logger;
import cn.edu.hust.zrx.cct.advanced.*;
import cn.edu.hust.zrx.cct.base.BaseUtils;
import cn.edu.hust.zrx.cct.base.cct.CctFactory;
import cn.edu.hust.zrx.cct.base.line.Arcs;
import cn.edu.hust.zrx.cct.base.line.Trajectory;
import cn.edu.hust.zrx.cct.base.line.TrajectoryFactory;
import cn.edu.hust.zrx.cct.base.particle.*;
import cn.edu.hust.zrx.cct.base.point.Point2;
import cn.edu.hust.zrx.cct.base.python.Plot2d;
import cn.edu.hust.zrx.cct.base.python.Plot3d;
import cn.edu.hust.zrx.cct.base.opticsMagnet.qs.QsHardPlaneMagnet;
import cn.edu.hust.zrx.cct.base.vector.Vector2;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Description
 * B20200506绕线方向问题详解
 * <p>
 * Data
 * 15:44
 *
 * @author zrx
 * @version 1.0
 */

@SuppressWarnings({"NonAsciiCharacters", "unchecked"})
public class B20200506绕线方向问题详解 {


    @run(-1)
    public void 回顾二极CCT的构建() {
        //传统二极CCT构建方法
        /*
          第一步
         */
        //return CctFactory.createDipoleCct(
        //                dipoleCctSmallRInner, dipoleCctSmallROuter, dipoleCctBigR,
        //                dipoleCctAngle, dipoleCctWindingNumber,
        //                dipoleCctA0Inner, dipoleCctA1Inner, dipoleCctA2Inner, dipoleCctIInner,
        //                dipoleCctA0Outer, dipoleCctA1Outer, dipoleCctA2Outer, dipoleCctIOuter,
        //                numberPerWinding
        //        );

        /*
         * 第二步
         */
        //return createDipoleCct(smallRInner, smallROuter,
        //                bigR, angle, windingNumber,
        //                a0BipolarInner, a1QuadrupleInner, a2SextupleInner, IInner,
        //                a0BipolarOuter, a1QuadrupleOuter, a2SextupleOuter, IOuter,
        //                numberPerWinding,
        //                0, 0, 0, 0, true);

        /*
         * 第三步
         * 首先构造  CctLine2s
         * 然后离散它 disperseToPoint3
         * 组成 SoleLayerCct
         *
         * directθ 应该相反 这个方法有问题
         */
        //        CctLine2s cctLine2sInner =
        //                CctLine2Factory.create(smallRInner, bigR, angle,
        //                        windingNumber, a0BipolarInner, a1QuadrupleInner, a2SextupleInner, startingθInner, startingφInner, directθ);
        //
        //        CctLine2s cctLine2sOuter =
        //                CctLine2Factory.create(smallROuter, bigR, angle,
        //                        windingNumber, a0BipolarOuter, a1QuadrupleOuter, a2SextupleOuter, startingθOuter, startingφOuter, directθ);
        //
        //        SoleLayerCct soleLayerCctInner = new SoleLayerCct(
        //                cctLine2sInner.disperseToPoint3(numberPerWinding), IInner);
        //        SoleLayerCct soleLayerCctOuter = new SoleLayerCct(
        //                cctLine2sOuter.disperseToPoint3(numberPerWinding), IOuter);
        //
        //        return Cct.getEmptyCct().
        //                addSoleLayerCct(soleLayerCctInner).
        //                addSoleLayerCct(soleLayerCctOuter);

        /*
         * 第四步
         * directθ 应该相反
         */
        //        return CctLine2s.create(
        //                new CctLine2(smallR, bigR, angle, windingNumber,
        //                        a0Bipolar, a1Quadruple, a2Sextuple,
        //                        startingθ, startingφ, directθ)
        //        );
        /*
         * 第五步，最底层
         * directθ 应该相反
         */
        //public CctLine2(double smallR,
        //                    double bigR,
        //                    double angle,
        //                    int windingNumber,
        //                    double a0Bipolar,
        //                    double a1Quadruple,
        //                    double a2Sextuple,
        //                    double startingθ,
        //                    double startingφ,
        //                    boolean directθ)


    }

    @run(1)
    public void 验证二极CCT构造方法正确() {
        Trajectory testTrajectory = createTestTrajectory();

        CctFactory.Cct dipoleCct = createDipoleCct(
                dipoleCctSmallRInner, dipoleCctSmallROuter, dipoleCctBigR,
                dipoleCctAngle, dipoleCctWindingNumber,
                dipoleCctA0Inner, dipoleCctA1Inner, dipoleCctA2Inner, dipoleCctIInner,
                dipoleCctA0Outer, dipoleCctA1Outer, dipoleCctA2Outer, dipoleCctIOuter,
                numberPerWinding,
                dipoleCctStartingθInner, dipoleCctStartingθOuter,
                dipoleCctStartingφInner, dipoleCctStartingφOuter,
                dipoleCctDirectθInner, dipoleCctDirectθOuter
        );


        RunningParticle idealProtonAtTrajectory250MeV = ParticleFactory.createIdealProtonAtTrajectory250MeV(testTrajectory);

        List<Point2> collect = ParticleRunner.runGetPoint3WithDistance(
                idealProtonAtTrajectory250MeV, dipoleCct, testTrajectory.getLength(), MM)
                .stream().map(ParticleRunner.Point3WithDistance::getDistanceWithZ).collect(Collectors.toList());

        Plot2d.plot2(collect);
        Plot2d.showThread();

        // 螺线管方向磁场
//        List<Point2> collect = BaseUtils.Python.linspaceStream(0, testTrajectory.getLength(), 1000)
//                .mapToObj(s -> {
//                    Point2 point2 = testTrajectory.pointAt(s);
//                    Vector2 vector2 = testTrajectory.directAt(s);
//
//                    Vector3 magnet = dipoleCct.magnetAt(point2.toPoint3());
//
//                    double magnetAxis = magnet.dot(vector2.toVector3());
//
//                    return Point2.create(s, magnetAxis);
//                }).collect(Collectors.toList());
//
//        Plot2d.plot2(collect);


//        List<Point2> list = dipoleCct.magnetBzAlongTrajectory(testTrajectory);
//        Plot2d.plot2(list);
//
//        Plot2d.showThread();

//        dipoleCct.plot3(Plot2d.RED_LINE,Plot2d.BLUE_LINE);
//
//        Plot3d.showThread();


//        // 验证 [7.268938341807897E-5, -3.654342079159943E-4, 0.0025707585428348875]
//
//
//        Vector3 vector3 = dipoleCct.magnetAt(Point3.origin());
//
//        Logger.getLogger().info("vector3 = " + vector3);
//        //[7.268938341807897E-5, -3.654342079159943E-4, 0.0025707585428348875]

    }

    @run(2)
    public void 创建绕线方向相反的四极CCT并验证() {
        Trajectory testTrajectory = createTestTrajectory();

        CctFactory.Cct agCct = createAgCct(
                agCctSmallRInner, agCctSmallROuter, agCCTBigR,
                new double[]{agCctAngle0, agCctAngle1},
                new int[]{agCctWindingNumber0, agCctWindingNumber1},
                agCctA0Inner, agCctA1Inner, agCctA2Inner, agCctIInner,
                agCctA0Outer, agCctA1Outer, agCctA2Outer, agCctIOuter,
                numberPerWinding,
                agCctStartingθInner, agCctStartingθOuter,
                agCctStartingφInner, agCctStartingφOuter,
                agCctDirectθInner, agCctDirectθOuter
        );

        CctFactory.Cct agCctInner = createAgCct(
                agCctSmallRInner, agCctSmallROuter, agCCTBigR,
                new double[]{agCctAngle0, agCctAngle1},
                new int[]{agCctWindingNumber0, agCctWindingNumber1},
                agCctA0Inner, agCctA1Inner, agCctA2Inner, agCctIInner,
                agCctA0Outer, agCctA1Outer, agCctA2Outer, 0,
                numberPerWinding,
                agCctStartingθInner, agCctStartingθOuter,
                agCctStartingφInner, agCctStartingφOuter,
                agCctDirectθInner, agCctDirectθOuter
        );

        CctFactory.Cct agCctOuter = createAgCct(
                agCctSmallRInner, agCctSmallROuter, agCCTBigR,
                new double[]{agCctAngle0, agCctAngle1},
                new int[]{agCctWindingNumber0, agCctWindingNumber1},
                agCctA0Inner, agCctA1Inner, agCctA2Inner, 0,
                agCctA0Outer, agCctA1Outer, agCctA2Outer, agCctIOuter,
                numberPerWinding,
                agCctStartingθInner, agCctStartingθOuter,
                agCctStartingφInner, agCctStartingφOuter,
                agCctDirectθInner, agCctDirectθOuter
        );


//        List<Point2> list = agCct.magnetGradientAlongTrajectoryFast(testTrajectory, MM, 2 * MM);
//

//        List<Point2> list = agCct.leftHandMagnetAlongTrajectory(testTrajectory, MM);

//        List<Point2> list = agCct.magnetBzAlongTrajectory(testTrajectory);
        List<Point2> listI = agCctInner.magnetBzAlongTrajectory(testTrajectory);
        List<Point2> listO = agCctOuter.magnetBzAlongTrajectory(testTrajectory);

//        Plot2d.plot2(list,Plot2d.BLACK_LINE);
        Plot2d.plot2(listI, Plot2d.RED_LINE);
        Plot2d.plot2(listO, Plot2d.BLUE_LINE);
        Plot2d.showThread();

//        Vector3 vector3 = agCct.magnetAt(Point3.origin());
//
//        Logger.getLogger().info("vector3 = " + vector3);
        //[-1.4705561346250172E-5, 1.984386025561039E-5, 5.1794020466365825E-6]
        //[-1.4705561346250172E-5, 1.984386025561039E-5, 5.1794020466365825E-6]


    }

    @run(103)
    public void 单粒子y方向位移() {
        CctFactory.Elements elementsOfAll = getElementBefore();
        Trajectory trajectory = getTrajectory();

        RunningParticle p = ParticleFactory.createIdealProtonAtTrajectory250MeV(trajectory);

        List<Point2> collect = ParticleRunner.runGetPoint3WithDistance(p, elementsOfAll, trajectory.getLength() + DL2, MM)
                .stream()
                .map(ParticleRunner.Point3WithDistance::getDistanceWithZ)
                .map(point2 -> Point2.convert(point2,1,1/MM))
                .collect(Collectors.toList());

        Plot2d.plot2(collect, Plot2d.BLUE_LINE);

        Plot2d.info("s/m", "y/mm", "y-displacement", 18);

//        Plot2d.plotYLines(keyDistance(), collect);

        plotElement(20,30,Plot2d.PINK_DASH, Plot2d.GREY_DASH);

        Plot2d.showThread();
    }

    @run(102)
    public void y方向位移() {
        boolean xPlane = false;

        CctFactory.Elements elementsOfAll = getElementsOfAll();
        Trajectory trajectory = getTrajectory();

        RunningParticle ip = ParticleFactory.createIdealProtonAtTrajectory250MeV(trajectory);

        List<PhaseSpaceParticle> pp = PhaseSpaceParticles.phaseSpaceParticlesAlongPositiveEllipseInPlane(
                xPlane, 3.5 * MM * 1, 7.5 * MM * 1, 5 * PRESENT, 32);

        List<RunningParticle> ps = ParticleFactory.createParticlesFromPhaseSpaceParticle(ip, ip.computeNaturalCoordinateSystem(), pp);

        List<List<Point2>> all = ps.stream().parallel().map(p -> {
            List<Point2> collect = ParticleRunner.runGetAllInfo(p, elementsOfAll, trajectory.getLength() + DL2, MM)
                    .stream()
                    .map(bi -> Point2.create(bi.getT1(), bi.getT2().position.z / MM))
                    .collect(Collectors.toList());

            Plot2d.plot2(collect, Plot2d.BLUE_LINE);

            return collect;
        }).collect(Collectors.toList());


        Plot2d.info("s/m", "y/mm", "y-displacement", 18);

        Plot2d.plotYLines(keyDistance(), all.toArray(List[]::new));

        Plot2d.showThread();
    }

    @run(101)
    public void 出口相椭圆分析() {
        double scaleForParticle = 1.;
        boolean moveToCenter = false;

        boolean xPlane = true;
        double delta = -5 * PRESENT;
        int number = 512;

        switcher.getAndSwitch();

        IntStream.rangeClosed(1, 5)
                .forEach(order -> {
                    List<Point2> list = cosy相椭圆(8, xPlane, delta, number, order, moveToCenter, scaleForParticle);
                    Plot2d.plot2circle(list, switcher.getAndSwitch());
                });


        //double distance,boolean xPlane, double delta, int number,boolean moveToCenter, double scaleForParticle
        List<Point2> tracking相椭圆 = tracking相椭圆(keyDistance().get(8), xPlane, delta, 32, moveToCenter, scaleForParticle);

        Plot2d.plot2(tracking相椭圆, Plot2d.BLACK_POINT);

//        Plot2d.plot2circle(tracking相椭圆, Plot2d.BLACK_LINE);

        Plot2d.legend(18, "COSY 1st", "COSY 2nd", "COSY 3rd", "COSY 4th", "COSY 5th", "Tracking");
//        Plot2d.legend(18, "COSY 1st", "COSY 2nd", "COSY 3rd", "COSY 4th", "COSY 5th", "COSY 6th", "COSY 7th");
        if (xPlane)
            Plot2d.info("x/mm", "xp/mrad", "x Plane dp = " + delta / PRESENT + "%", 18);
        else
            Plot2d.info("y/mm", "yp/mrad", "y Plane dp = " + delta / PRESENT + "%", 18);

        Plot2d.equal();
        Plot2d.showThread();
    }

    @run(105)
    public void cosy包络分析() {
        int order = 1;
        double delta = 5 * PRESENT;

        List<Point2>[] lists1 = BaseUtils.StreamTools.from(1).to(5)
                .parallel()
                .mapToObj(o -> {
                    List<Point2> list = cosy包络(true, o, delta);

                    return BaseUtils.Content.BiContent.create(o, list);

                }).collect(Collectors.toList())
                .stream()
                .sorted(Comparator.comparingInt(BaseUtils.Content.BiContent::getT1))
                .map(bi -> {
                    List<Point2> list = bi.getT2();
                    Plot2d.plot2(list, switcher.getAndSwitch());

                    return list;
                }).collect(Collectors.toList())
                .toArray(List[]::new);

        switcher.reset();
        List<Point2>[] lists2 = BaseUtils.StreamTools.from(1).to(5)
                .mapToObj(o -> {
                    List<Point2> list = cosy包络(false, o, delta);
                    list = Point2.convert(list, 1, -1);

                    return BaseUtils.Content.BiContent.create(o, list);

                }).collect(Collectors.toList())
                .stream()
                .sorted(Comparator.comparingInt(BaseUtils.Content.BiContent::getT1))
                .map(bi -> {
                    List<Point2> list = bi.getT2();
                    Plot2d.plot2(list, switcher.getAndSwitch());

                    return list;
                }).collect(Collectors.toList())
                .toArray(List[]::new);

        List<Point2>[] combine = BaseUtils.ListUtils.combine(lists1, lists2);

        Plot2d.plotYLines(keyDistance(), combine);

//        Plot2d.grid();

        Plot2d.info("s/m", "mm", "envelope dp = " + delta / PRESENT + "%", 18);
        Plot2d.legend(18, "cosy 1st", "cosy 2nd", "cosy 3rd", "cosy 4th", "cosy 5th");


        Plot2d.showThread();
    }

    @run(106)
    public void cosy包络分析2() {
        double delta = 5 * PRESENT;

        List<Point2> x0 = null;
        x0 = cosy包络(true, 5, delta);
        List<Point2> y0 = null;
        y0 = cosy包络(false, 5, delta);

        List<Point2> xx0 = track粒子跟踪包络(true, delta);
        List<Point2> yy0 = track粒子跟踪包络(false, delta);

        Plot2d.plot2(x0, Plot2d.BLACK_DOt_DASH);
        Plot2d.plot2(xx0, Plot2d.RED_LINE);

        Plot2d.plot2(Point2.convert(y0, 1, -1), Plot2d.BLACK_DOt_DASH);
        Plot2d.plot2(Point2.convert(yy0, 1, -1), Plot2d.RED_LINE);


//        Plot2d.plotYLines(keyDistance(), x0, Point2.convert(y0, 1, -1), xx0, Point2.convert(yy0, 1, -1));

        plotElement(20,30, Plot2d.PINK_DASH, Plot2d.GREY_DASH);
//        Plot2d.grid();

        Plot2d.info("s/m", "mm", "envelope dp = " + delta / PRESENT + "%", 18);
        Plot2d.legend(18, "cosy 5th", "tracking");

        Plot2d.showThread();
    }

    @run(107)
    public void 磁场Bx分布() {

//        dipoleCctDirectθInner = true;
//        dipoleCctDirectθOuter = false;
//        agCctDirectθInner = false;
//        agCctDirectθOuter = true;
//
//        // 积分场
//        // sum = 0.8995274771524209 t f f t
//        // sum = 0.9336838858554605 f t f t
//        // sum = -0.8997646180786371 f t t f
//        // sum = -0.9339210267816761 t f t f


        CctFactory.Elements elementsOfAll = getElementBefore();

//        double dii = dipoleCctIInner;
//        double dio = dipoleCctIOuter;
//
//        dipoleCctIInner = 0;
//        dipoleCctIOuter = 0;
//
//        CctFactory.Elements elementsOfAllAgCct = getElementsOfAll();
//
//        dipoleCctIInner = dii;
//        dipoleCctIOuter = dio;
//
//        agCctIInner = 0;
//        agCctIOuter = 0;
//
//        CctFactory.Elements elementsOfAllDiCct = getElementsOfAll();
//
//
        Trajectory trajectory = getTrajectory();


        List<Point2> leftHandMagnetAlongTrajectory = elementsOfAll.leftHandMagnetAlongTrajectory(trajectory, MM);

        Plot2d.plot2(Point2.convert(leftHandMagnetAlongTrajectory,1,1/Gs),Plot2d.BLACK_LINE);

        plotElement(20,30, Plot2d.PINK_DASH, Plot2d.GREY_DASH);
//        Plot2d.grid();

        Plot2d.info("s/m", "Bx/Gs", "", 18);

        Plot2d.showThread();
//        Plot2d.plot2(Point2.convert(leftHandMagnetAlongTrajectory, 1, 1 / Gs),Plot2d.BLACK_LINE);

//        List<Point2> leftHandMagnetAlongTrajectoryAg = elementsOfAllAgCct.leftHandMagnetAlongTrajectory(trajectory, MM);
//        Plot2d.plot2(Point2.convert(leftHandMagnetAlongTrajectoryAg, 1, 1 / Gs), Plot2d.BLUE_LINE);
//
//        List<Point2> leftHandMagnetAlongTrajectoryDi = elementsOfAllDiCct.leftHandMagnetAlongTrajectory(trajectory, MM);
//        Plot2d.plot2(Point2.convert(leftHandMagnetAlongTrajectoryDi, 1, 1 / Gs), Plot2d.RED_LINE);
//
//        Plot2d.legend(18,"all cct","di cct","ag cct");
//
//        Plot2d.info("s/m", "Bx/Gs", "", 18);
//
//        Plot2d.showThread();

//        double sum = leftHandMagnetAlongTrajectory.stream().mapToDouble(Point2::getY).sum();
//
//
//        Logger.getLogger().info("sum = " + sum);
    }

    @run(-108)
    public void 对称CCT绘图() {
        Trajectory trajectory = getTrajectory();
        trajectory.plot3d();

        CctFactory.Cct cct1 = getCct1();
        CctFactory.Cct cctSymmetryCct1 = getCctSymmetryCct1();

        cct1.plot3();
        cctSymmetryCct1.plot3();

        Plot3d.showThread();
    }

    @run(109)
    public void 磁场By分布() {
        CctFactory.Elements elementsOfAll = getElementsOfAll();

        Trajectory trajectory = getTrajectory();

        List<Point2> bz = elementsOfAll.magnetBzAlongTrajectory(trajectory, MM);

        Plot2d.plot2(bz);

        Plot2d.info("s/m", "By/T", "", 18);

        Plot2d.showThread();
    }

    @run(110)
    public void 磁场G分布() {
        CctFactory.Elements elementsOfAll = getElementsOfAll();

        Trajectory trajectory = getTrajectory();

        List<Point2> g = elementsOfAll.magnetGradientAlongTrajectory(trajectory, MM, 2 * MM);

        Plot2d.plot2(g);

        Plot2d.info("s/m", "G/(T/m)", "", 18);

        Plot2d.showThread();
    }


    @run(111)
    public void 单粒子跟踪QS_AGCCT有无() {
        agCctIInner = 0;
        agCctIOuter = 0;

        CctFactory.MagnetAble elementsOfAll = getCct1();
        Trajectory trajectory = getTrajectory();

        RunningParticle p = ParticleFactory.createIdealProtonAtTrajectory250MeV(trajectory);

        List<Point2> collect = ParticleRunner.runGetPoint3WithDistance(p, elementsOfAll, trajectory.getLength() + DL2, MM)
                .stream()
                .map(ParticleRunner.Point3WithDistance::getDistanceWithZ)
                .collect(Collectors.toList());

        Plot2d.plot2(collect, Plot2d.BLUE_LINE);

        Plot2d.info("s/m", "y/mm", "y-displacement", 18);

        Plot2d.plotYLines(keyDistance(), collect);

        Plot2d.showThread();

    }

    @run(112)
    public void 调整agcct绕线使得积分场为0() { // 没什么用

        Trajectory testTrajectory = createTestTrajectory();

//        BaseUtils.Python.linspaceStream(0,360,180)
//                .map(BaseUtils.Converter::angleToRadian)
//                .parallel()
//                .mapToObj(startingTheta->{
//                    CctFactory.Cct agCct = createAgCct(
//                            agCctSmallRInner, agCctSmallROuter, agCCTBigR,
//                            new double[]{agCctAngle0, agCctAngle1},
//                            new int[]{agCctWindingNumber0, agCctWindingNumber1},
//                            agCctA0Inner, agCctA1Inner, agCctA2Inner, agCctIInner,
//                            agCctA0Outer, agCctA1Outer, agCctA2Outer, agCctIOuter,
//                            numberPerWinding,
//                            startingTheta, agCctStartingθOuter,
//                            agCctStartingφInner, agCctStartingφOuter,
//                            agCctDirectθInner, agCctDirectθOuter
//                    );
//
//                    List<Point2> list = agCct.leftHandMagnetAlongTrajectory(testTrajectory, MM);
//
//                    double sum = list.stream().mapToDouble(Point2::getY).sum();
//
//                    return BaseUtils.Content.BiContent.create(startingTheta,sum);
//                }).collect(Collectors.toList())
//                .stream()
//                .sorted(Comparator.comparingDouble(BaseUtils.Content.BiContent::getT1))
//                .forEach(bi-> Logger.getLogger().info("{}" +bi.toStringWithInfo("startingTheta","inter") ));


        CctFactory.Cct agCct = createAgCct(
                agCctSmallRInner, agCctSmallROuter, agCCTBigR,
                new double[]{agCctAngle0, agCctAngle1},
                new int[]{agCctWindingNumber0, agCctWindingNumber1},
                agCctA0Inner, agCctA1Inner, agCctA2Inner, agCctIInner,
                agCctA0Outer, agCctA1Outer, agCctA2Outer, agCctIOuter,
                numberPerWinding,
                agCctStartingθInner, agCctStartingθOuter,
                agCctStartingφInner, agCctStartingφOuter,
                agCctDirectθInner, agCctDirectθOuter
        );

        List<Point2> list = agCct.leftHandMagnetAlongTrajectory(testTrajectory, MM);

        double sum = list.stream().mapToDouble(Point2::getY).sum();


        Logger.getLogger().info("sum = " + sum);
        // sum = -0.46013453414387623  true false
        // sum = 0.4601345284280684  false true
    }

    @run(113)
    public void 每段相椭圆绘图() {
        double scaleForParticle = 1.;
        boolean moveToCenter = false;

        boolean xPlane = false;
        double delta = 5 * PRESENT;
        int number = 512;

        int index = 3;

        switcher.getAndSwitch();

        IntStream.rangeClosed(1, 5)
                .forEach(order -> {
                    List<Point2> list = cosy相椭圆(index, xPlane, delta, number, order, moveToCenter, scaleForParticle);
                    Plot2d.plot2circle(list, switcher.getAndSwitch());
                });


        //double distance,boolean xPlane, double delta, int number,boolean moveToCenter, double scaleForParticle
        List<Point2> tracking相椭圆 = tracking相椭圆(cosyMap().get(index).getT1(), xPlane, delta, 32, moveToCenter, scaleForParticle);

        Plot2d.plot2(tracking相椭圆, Plot2d.BLACK_POINT);

//        Plot2d.plot2circle(tracking相椭圆, Plot2d.BLACK_LINE);

        Plot2d.legend(18, "COSY 1st", "COSY 2nd", "COSY 3rd", "COSY 4th", "COSY 5th", "Tracking");
//        Plot2d.legend(18, "COSY 1st", "COSY 2nd", "COSY 3rd", "COSY 4th", "COSY 5th", "COSY 6th", "COSY 7th");
        if (xPlane)
            Plot2d.info("x/mm", "xp/mrad", "x Plane, dp = " + delta / PRESENT + "%, " + "s = " + cosyMap().get(index).getT1() + "m", 18);
        else
            Plot2d.info("y/mm", "yp/mrad", "y Plane, dp = " + delta / PRESENT + "%, " + "s = " + cosyMap().get(index).getT1() + "m", 18);

        Plot2d.equal();
        Plot2d.showThread();
    }

    @run(114)
    public void 磁场2G分布() {
        CctFactory.Elements elementsOfAll = getElementsOfAll();

        Trajectory trajectory = getTrajectory();

        List<Point2> gg = elementsOfAll.magnetSecondGradientAlongTrajectory(trajectory, MM, 2 * MM);

        Plot2d.plot2(gg);

        Plot2d.info("s/m", "G/(T/m2)", "", 18);

        Plot2d.showThread();
    }

    @run(115)
    public void 调整agcct电流() {


        Trajectory testTrajectory = createTestTrajectory();
        boolean xPlane = true;
        double delta = 5 * PRESENT;

        RunningParticle ip = ParticleFactory.createIdealProtonAtTrajectory250MeV(testTrajectory);


        // cosy
        List<PhaseSpaceParticle> pp = PhaseSpaceParticles.phaseSpaceParticlesAlongPositiveEllipseInPlane(
                xPlane, 3.5 * MM, 7.5 * MM, delta, 512);

        // 转为COSY能量分散
        pp = PhaseSpaceParticles.convertDeltaFromMomentumDispersionToEnergyDispersion(pp, 250);

        // 运行
        List<PhaseSpaceParticle> ppEnd = cosyMapTest().apply(pp, 5);

        // 投影
        List<Point2> projectionToPlaneCOSY = PhaseSpaceParticles.projectionToPlane(xPlane, ppEnd);

        // 改单位
        List<Point2> convert = Point2.convert(projectionToPlaneCOSY, 1 / MM, 1 / MRAD);

        Point2 averageCosy = Point2.average(convert);
        Logger.getLogger().info("cosy 5th average = " + averageCosy);

        //cosy end


        double k0 = 0.02;

        final double agCctIInner0 = agCctIInner;
        final double agCctIOuter0 = agCctIOuter;

//        List<Point2>[] lists =
        BaseUtils.Python.linspaceStream(-k0, k0, 150)
                .mapToObj(k -> {

                    /**
                     * 我又犯错了
                     */
                    agCctIInner = (1 + k) * agCctIInner0;
                    agCctIOuter = (1 + k) * agCctIOuter0;

                    CctFactory.Cct cct = getCct();

                    return BaseUtils.Content.BiContent.create(k, cct);
                }).collect(Collectors.toList())
                .stream()
                .parallel()
                .map(bi -> {
                    CctFactory.Cct cct = bi.getT2();
                    Double k = bi.getT1();

                    RunningParticle ipT = ip.copy();

                    List<PhaseSpaceParticle> ppT = PhaseSpaceParticles.phaseSpaceParticlesAlongPositiveEllipseInPlane(
                            xPlane, 3.5 * MM, 7.5 * MM, delta, 8);

                    List<RunningParticle> ps = ParticleFactory.createParticlesFromPhaseSpaceParticle(ipT, ipT.computeNaturalCoordinateSystem(), ppT);

                    ParticleRunner.runThread(ps, cct, testTrajectory.getLength(), MM);
                    ParticleRunner.run(ipT, cct, testTrajectory.getLength(), MM);

                    List<PhaseSpaceParticle> ppEndT = PhaseSpaceParticles.phaseSpaceParticlesFromRunningParticles(ipT, ipT.computeNaturalCoordinateSystem(), ps);

                    List<Point2> projectionToPlane = PhaseSpaceParticles.projectionToPlane(xPlane, ppEndT);

                    projectionToPlane = Point2.convert(projectionToPlane, 1 / MM, 1 / MRAD);

                    Point2 average = Point2.average(projectionToPlane);

                    double length = Vector2.from(averageCosy).to(average).length();

                    Logger.getLogger().info("{}--{}--len={}", k, average, length);

//                    return BaseUtils.Content.BiContent.create(k, projectionToPlane);

                    return Point2.create(k, length);
                }).collect(Collectors.toList())
                .stream()
                .sorted(Comparator.comparingDouble(Point2::getX))
                .forEach(p -> Logger.getLogger().info(p.toString()));


//                .collect(Collectors.toList())
//                .stream()
//                .sorted(Comparator.comparingDouble(BaseUtils.Content.BiContent::getT1))
//                .map(BaseUtils.Content.BiContent::getT2)
//                //.peek(point2List -> Plot2d.plot2(point2List, Plot2d.YELLOW_POINT))
//                .collect(Collectors.toList())
//                .toArray(List[]::new);


        //  ----- cosy

        //Plot2d.plot2(convert, Plot2d.BLACK_LINE);

        //  ----- cosy

//        List<String> collect = BaseUtils.Python.linspaceStream(-k0, k0, switcher.getSize())
//                .mapToObj(String::valueOf)
//                .collect(Collectors.toList());
//        collect.add("cosy 5th");
//
//        Plot2d.legend(18, collect.toArray(String[]::new));
////        Plot2d.legend(18, "COSY 1st", "COSY 2nd", "COSY 3rd", "COSY 4th", "COSY 5th", "COSY 6th", "COSY 7th");
//        if (xPlane)
//            Plot2d.info("x/mm", "xp/mrad", "", 18);
//        else
//            Plot2d.info("y/mm", "yp/mrad", "", 18);
//
//        Plot2d.equal();
//
//        Plot2d.showThread();
    }


    @run(116)
    public void 单独分析CCT12() {
        CctFactory.Cct cct = getCct();
        Trajectory testTrajectory = createTestTrajectory();


    }


    @run(1000)
    public void 调整绕线方前向后Bx分布() {
        // 调整后

        CctFactory.Elements elementsOfAll = getElementsOfAll();

        Trajectory trajectory = getTrajectory();

        List<Point2> leftAfter = elementsOfAll.leftHandMagnetAlongTrajectory(trajectory, MM);

        Plot2d.plot2(Point2.convert(leftAfter, 1, 1 / Gs), Plot2d.BLACK_LINE);

        // 调整前

        agCctDirectθOuter = true;
        dipoleCctDirectθOuter = true;

        agCctIOuter *= -1;
        dipoleCctIOuter *= -1;

        CctFactory.Elements elementsOfAllBefore = getElementsOfAll();

        List<Point2> listBefore = elementsOfAllBefore.leftHandMagnetAlongTrajectory(trajectory, MM);

        Plot2d.plot2(Point2.convert(listBefore, 1, 1 / Gs), Plot2d.RED_LINE);


        plotElement(230, Plot2d.PINK_DASH, Plot2d.GREY_DASH);

        Plot2d.info("s/m", "Bx/Gs", "", 18);

        Plot2d.legend(18, "After modification", "Before modification");

        Plot2d.showThread();
    }

    @run(1001)
    public void 调整绕线前后单粒子跟踪() throws ExecutionException, InterruptedException {
        Trajectory trajectory = getTrajectory();

        // 调整后
        CctFactory.Elements elementsOfAll = getElementsOfAll();

        // 调整前
        agCctDirectθOuter = true;
        dipoleCctDirectθOuter = true;

        agCctIOuter *= -1;
        dipoleCctIOuter *= -1;

        CctFactory.Elements elementsOfAllBefore = getElementsOfAll();


        RunningParticle p0 = ParticleFactory.createIdealProtonAtTrajectory250MeV(trajectory);
        RunningParticle p1 = ParticleFactory.createIdealProtonAtTrajectory250MeV(trajectory);

        BaseUtils.Async async = new BaseUtils.Async();


        Future<List<Point2>> futureAfter = async.submit(() ->
                ParticleRunner.runGetPoint3WithDistance(p0, elementsOfAll, trajectory.getLength(), MM)
                        .stream().map(ParticleRunner.Point3WithDistance::getDistanceWithZ)
                        .map(point2 -> Point2.convert(point2, 1, 1 / MM))
                        .collect(Collectors.toList())
        );

        Future<List<Point2>> futureBefore = async.submit(() ->
                ParticleRunner.runGetPoint3WithDistance(p1, elementsOfAllBefore, trajectory.getLength(), MM)
                        .stream().map(ParticleRunner.Point3WithDistance::getDistanceWithZ)
                        .map(point2 -> Point2.convert(point2, 1, 1 / MM))
                        .collect(Collectors.toList())
        );


        List<Point2> afterL = futureAfter.get();

        Plot2d.plot2(afterL, Plot2d.BLACK_LINE);

        List<Point2> beforeL = futureBefore.get();

        Plot2d.plot2(beforeL, Plot2d.RED_LINE);

        plotElement(BaseUtils.ListUtils.max(beforeL.stream().map(Point2::getY).collect(Collectors.toList())), Plot2d.PINK_DASH, Plot2d.GREY_DASH);

        Plot2d.info("s/m", "y/mm", "", 18);

        Plot2d.legend(18, "After modification", "Before modification");


        Plot2d.showThread();
    }

    @run(1002)
    public void 调整前后多粒子跟踪() {
        Trajectory trajectory = getTrajectory();

        CctFactory.Elements eAfter = getElementsOfAll();
//        CctFactory.Elements eBefore = getElementBefore();

        List<PhaseSpaceParticle> ppA = PhaseSpaceParticles.phaseSpaceParticlesAlongPositiveEllipseInPlane(
                true, 3.5 * MM, 7.5 * MM, 0 * PRESENT, 32);
        List<PhaseSpaceParticle> ppB = PhaseSpaceParticles.phaseSpaceParticlesAlongPositiveEllipseInPlane(
                true, 3.5 * MM, 7.5 * MM, 0 * PRESENT, 32);

        RunningParticle ip = ParticleFactory.createIdealProtonAtTrajectory250MeV(trajectory);

        List<RunningParticle> psA = ParticleFactory.createParticlesFromPhaseSpaceParticle(ip, ip.computeNaturalCoordinateSystem(), ppA);
        List<RunningParticle> psB = ParticleFactory.createParticlesFromPhaseSpaceParticle(ip, ip.computeNaturalCoordinateSystem(), ppB);

        psA.stream().limit(1).forEach(runningParticle -> {
            List<Point2> collect = ParticleRunner.runGetPoint3WithDistance(runningParticle, eAfter, trajectory.getLength()+DL2, MM)
                    .stream()
//                    .map(ParticleRunner.Point3WithDistance::getDistanceWithZ)
                    .map(point3WithDistance -> {
                        double distance = point3WithDistance.getDistance();
                        Point2 p = point3WithDistance.getPoint3().toPoint2();

                        Point2 o = trajectory.pointAt(distance);
                        Vector2 x = trajectory.directAt(distance).rotateSelf(Math.PI / 2);

                        return Point2.create(distance, Vector2.from(o).to(p).dot(x));
                    })
                    .map(point2 -> Point2.convert(point2, 1, 1 / MM))
                    .collect(Collectors.toList());

            Plot2d.plot2(collect, Plot2d.BLACK_LINE);
        });

//        psB.stream().limit(1).forEach(runningParticle -> {
//            List<Point2> collect = ParticleRunner.runGetPoint3WithDistance(runningParticle, eBefore, trajectory.getLength(), MM)
//                    .stream()
//                    //                    .map(ParticleRunner.Point3WithDistance::getDistanceWithZ)
//                    .map(point3WithDistance -> {
//                        double distance = point3WithDistance.getDistance();
//                        Point2 p = point3WithDistance.getPoint3().toPoint2();
//
//                        Point2 o = trajectory.pointAt(distance);
//                        Vector2 x = trajectory.directAt(distance).rotateSelf(Math.PI / 2);
//
//                        return Point2.create(distance, Vector2.from(o).to(p).dot(x));
//                    })
//                    .map(point2 -> Point2.convert(point2, 1, 1 / MM))
//                    .collect(Collectors.toList());
//
//            Plot2d.plot2(collect, Plot2d.RED_LINE);
//        });

        psA.stream().skip(1).parallel().forEach(runningParticle -> {
            List<Point2> collect = ParticleRunner.runGetPoint3WithDistance(runningParticle, eAfter, trajectory.getLength()+DL2, MM)
                    .stream()
                    //                    .map(ParticleRunner.Point3WithDistance::getDistanceWithZ)
                    .map(point3WithDistance -> {
                        double distance = point3WithDistance.getDistance();
                        Point2 p = point3WithDistance.getPoint3().toPoint2();

                        Point2 o = trajectory.pointAt(distance);
                        Vector2 x = trajectory.directAt(distance).rotateSelf(Math.PI / 2);

                        return Point2.create(distance, Vector2.from(o).to(p).dot(x));
                    })
                    .map(point2 -> Point2.convert(point2, 1, 1 / MM))
                    .collect(Collectors.toList());

            Plot2d.plot2(collect, Plot2d.BLACK_LINE);
        });

//        psB.stream().skip(1).parallel().forEach(runningParticle -> {
//            List<Point2> collect = ParticleRunner.runGetPoint3WithDistance(runningParticle, eBefore, trajectory.getLength(), MM)
//                    .stream()
//                    //                    .map(ParticleRunner.Point3WithDistance::getDistanceWithZ)
//                    .map(point3WithDistance -> {
//                        double distance = point3WithDistance.getDistance();
//                        Point2 p = point3WithDistance.getPoint3().toPoint2();
//
//                        Point2 o = trajectory.pointAt(distance);
//                        Vector2 x = trajectory.directAt(distance).rotateSelf(Math.PI / 2);
//
//                        return Point2.create(distance, Vector2.from(o).to(p).dot(x));
//                    })
//                    .map(point2 -> Point2.convert(point2, 1, 1 / MM))
//                    .collect(Collectors.toList());
//
//            Plot2d.plot2(collect, Plot2d.RED_LINE);
//        });


        plotElement(20, 30, Plot2d.PINK_DASH, Plot2d.GREY_DASH);

        Plot2d.info("s/m", "x/mm", "", 18);

        plotElement(20,30,Plot2d.PINK_DASH, Plot2d.GREY_DASH);

//        Plot2d.legend(18, "After modification", "Before modification");

        Plot2d.showThread();

    }

    @run(1003)
    public void 六极场分布(){
        CctFactory.Elements elementsOfAll = getElementsOfAll();

        Trajectory trajectory = getTrajectory();

        List<Point2> secondGradientAlongTrajectory = elementsOfAll.magnetSecondGradientAlongTrajectory(trajectory, MM, 2 * MM);

        Plot2d.plot2(secondGradientAlongTrajectory,Plot2d.BLUE_LINE);

        plotElement(200,300,Plot2d.PINK_DASH, Plot2d.GREY_DASH);

        Plot2d.info("s/m","l/(T/m2)","",18);

        Plot2d.showThread();
    }


    private CctFactory.Elements getElementBefore() {
        agCctDirectθOuter = true;
        dipoleCctDirectθOuter = true;

        agCctIOuter *= -1;
        dipoleCctIOuter *= -1;

        CctFactory.Elements elementsOfAllBefore = getElementsOfAll();

        return elementsOfAllBefore;
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

    private void plotElement(double height, String cctDescribe, String QsDescribe) {
        Plot2d.plotBox(DL1, DL1 + CCT_LENGTH, -height, height, cctDescribe); // 1st cct1
        Plot2d.plotYLine(DL1 + CCT00_LENGTH, height, -height, cctDescribe);

        //Plot2d.plotBox(DL1 + CCT00_LENGTH, DL1 + CCT_LENGTH, -height, height, cctDescribe); // 1st cct2

        Plot2d.plotBox(DL1 + CCT_LENGTH + GAP1, DL1 + CCT_LENGTH + GAP1 + QS1_LEN, -height, height, QsDescribe); // qs1
        Plot2d.plotBox(DL1 + CCT_LENGTH + GAP1 + QS1_LEN + GAP2, DL1 + CCT_LENGTH + GAP1 + QS1_LEN + GAP2 + QS2_LEN, -height, height, QsDescribe); // qs2
        Plot2d.plotBox(DL1 + CCT_LENGTH + GAP1 + QS1_LEN + GAP2 + QS2_LEN + GAP2, DL1 + CCT_LENGTH + GAP1 + QS1_LEN + GAP2 + QS2_LEN + GAP2 + QS2_LEN, -height, height, QsDescribe); // qs1

        Plot2d.plotBox(DL1 + CCT_LENGTH + BETWEEN_CCT225, DL1 + CCT_LENGTH + BETWEEN_CCT225 + CCT_LENGTH, -height, height, cctDescribe); // 2 cct1
        //Plot2d.plotBox(DL1 + CCT_LENGTH + BETWEEN_CCT225 + CCT00_LENGTH, DL1 + CCT_LENGTH + BETWEEN_CCT225 + CCT_LENGTH, -height, height, cctDescribe); // 2 cct2

        Plot2d.plotYLine(DL1 + CCT_LENGTH + BETWEEN_CCT225 + CCT01_LENGTH, height, -height, cctDescribe);
    }

    private void plotElement(double cctHeight, double QsHeight, String cctDescribe, String QsDescribe) {
        Plot2d.plotBox(DL1, DL1 + CCT_LENGTH, -cctHeight, cctHeight, cctDescribe); // 1st cct1
        Plot2d.plotYLine(DL1 + CCT00_LENGTH, cctHeight, -cctHeight, cctDescribe);

        //Plot2d.plotBox(DL1 + CCT00_LENGTH, DL1 + CCT_LENGTH, -height, height, cctDescribe); // 1st cct2

        Plot2d.plotBox(DL1 + CCT_LENGTH + GAP1, DL1 + CCT_LENGTH + GAP1 + QS1_LEN, -QsHeight, QsHeight, QsDescribe); // qs1
        Plot2d.plotBox(DL1 + CCT_LENGTH + GAP1 + QS1_LEN + GAP2, DL1 + CCT_LENGTH + GAP1 + QS1_LEN + GAP2 + QS2_LEN, -QsHeight, QsHeight, QsDescribe); // qs2
        Plot2d.plotBox(DL1 + CCT_LENGTH + GAP1 + QS1_LEN + GAP2 + QS2_LEN + GAP2, DL1 + CCT_LENGTH + GAP1 + QS1_LEN + GAP2 + QS2_LEN + GAP2 + QS2_LEN, -QsHeight, QsHeight, QsDescribe); // qs1

        Plot2d.plotBox(DL1 + CCT_LENGTH + BETWEEN_CCT225, DL1 + CCT_LENGTH + BETWEEN_CCT225 + CCT_LENGTH, -cctHeight, cctHeight, cctDescribe); // 2 cct1
        //Plot2d.plotBox(DL1 + CCT_LENGTH + BETWEEN_CCT225 + CCT00_LENGTH, DL1 + CCT_LENGTH + BETWEEN_CCT225 + CCT_LENGTH, -height, height, cctDescribe); // 2 cct2

        Plot2d.plotYLine(DL1 + CCT_LENGTH + BETWEEN_CCT225 + CCT01_LENGTH, cctHeight, -cctHeight, cctDescribe);
    }

    private List<BaseUtils.Content.BiContent<Double, CosyArbitraryOrder.CosyMapArbitraryOrder>> cosyMap() {
        List<BaseUtils.Content.BiContent<Double, CosyArbitraryOrder.CosyMapArbitraryOrder>> list = new ArrayList<>();


        list.add(BaseUtils.Content.BiContent.create(keyDistance().get(0), CosyArbitraryOrder.readMap(
                "   1.000000     0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00 100000\n" +
                        "  0.0000000E+00  1.000000     0.0000000E+00 0.0000000E+00 0.0000000E+00 010000\n" +
                        "  0.0000000E+00 0.0000000E+00  1.000000     0.0000000E+00 0.0000000E+00 001000\n" +
                        "  0.0000000E+00 0.0000000E+00 0.0000000E+00  1.000000     0.0000000E+00 000100\n" +
                        "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00  1.000000     000010"
        )));

        list.add(BaseUtils.Content.BiContent.create(keyDistance().get(1), CosyArbitraryOrder.readMap(
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
                        "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00-0.1918721     000002\n" +
                        "  0.5879500     0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00 030000\n" +
                        "  0.0000000E+00 0.0000000E+00 0.5879500     0.0000000E+00 0.0000000E+00 020100\n" +
                        "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00 0.4816171     020001\n" +
                        "  0.5879500     0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00 010200\n" +
                        "  0.4816171     0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00 010002\n" +
                        "  0.0000000E+00 0.0000000E+00 0.5879500     0.0000000E+00 0.0000000E+00 000300\n" +
                        "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00 0.4816171     000201\n" +
                        "  0.0000000E+00 0.0000000E+00 0.4816171     0.0000000E+00 0.0000000E+00 000102\n" +
                        "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00 0.1652348     000003\n" +
                        "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00-0.2464014     040000\n" +
                        " -0.9856055     0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00 030001\n" +
                        "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00-0.4928028     020200\n" +
                        "  0.0000000E+00 0.0000000E+00-0.9856055     0.0000000E+00 0.0000000E+00 020101\n" +
                        "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00-0.5955497     020002\n" +
                        " -0.9856055     0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00 010201\n" +
                        " -0.3970332     0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00 010003\n" +
                        "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00-0.2464014     000400\n" +
                        "  0.0000000E+00 0.0000000E+00-0.9856055     0.0000000E+00 0.0000000E+00 000301\n" +
                        "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00-0.5955497     000202\n" +
                        "  0.0000000E+00 0.0000000E+00-0.3970332     0.0000000E+00 0.0000000E+00 000103\n" +
                        "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00-0.1465397     000004\n" +
                        "  0.4409625     0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00 050000\n" +
                        "  0.0000000E+00 0.0000000E+00 0.4409625     0.0000000E+00 0.0000000E+00 040100\n" +
                        "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00 0.6365816     040001\n" +
                        "  0.8819250     0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00 030200\n" +
                        "   1.273163     0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00 030002\n" +
                        "  0.0000000E+00 0.0000000E+00 0.8819250     0.0000000E+00 0.0000000E+00 020300\n" +
                        "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00  1.273163     020201\n" +
                        "  0.0000000E+00 0.0000000E+00  1.273163     0.0000000E+00 0.0000000E+00 020102\n" +
                        "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00 0.6915613     020003\n" +
                        "  0.4409625     0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00 010400\n" +
                        "   1.273163     0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00 010202\n" +
                        "  0.3457806     0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00 010004\n" +
                        "  0.0000000E+00 0.0000000E+00 0.4409625     0.0000000E+00 0.0000000E+00 000500\n" +
                        "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00 0.6365816     000401\n" +
                        "  0.0000000E+00 0.0000000E+00  1.273163     0.0000000E+00 0.0000000E+00 000302\n" +
                        "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00 0.6915613     000203\n" +
                        "  0.0000000E+00 0.0000000E+00 0.3457806     0.0000000E+00 0.0000000E+00 000104\n" +
                        "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00 0.1328215     000005"
        )));

        list.add(BaseUtils.Content.BiContent.create(keyDistance().get(2), CosyArbitraryOrder.readMap(
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
                        "  0.2711393E-01 0.8701740E-01 0.0000000E+00 0.0000000E+00-0.2389992     000002\n" +
                        "  -2.198037     -11.75455     0.0000000E+00 0.0000000E+00 0.8034181     300000\n" +
                        "  -6.796234     -38.61531     0.0000000E+00 0.0000000E+00  2.176722     210000\n" +
                        "  -7.534520     -43.34932     0.0000000E+00 0.0000000E+00  1.872581     120000\n" +
                        "  -2.498861     -16.53749     0.0000000E+00 0.0000000E+00 0.6277317     030000\n" +
                        "  0.0000000E+00 0.0000000E+00  1.094963    -0.2527649     0.0000000E+00 201000\n" +
                        "  0.0000000E+00 0.0000000E+00  2.021299    -0.7257698     0.0000000E+00 111000\n" +
                        "  0.0000000E+00 0.0000000E+00 0.8394323     -1.013929     0.0000000E+00 021000\n" +
                        "  -1.414597     -8.035215     0.0000000E+00 0.0000000E+00 0.6186335     102000\n" +
                        "  -1.206533     -8.391187     0.0000000E+00 0.0000000E+00 0.8716089     012000\n" +
                        "  0.0000000E+00 0.0000000E+00 0.3750358     -5.442975     0.0000000E+00 003000\n" +
                        "  0.0000000E+00 0.0000000E+00  1.491404     -1.017903     0.0000000E+00 200100\n" +
                        "  0.0000000E+00 0.0000000E+00  2.475722     -1.928664     0.0000000E+00 110100\n" +
                        "  0.0000000E+00 0.0000000E+00  1.667911     -3.253692     0.0000000E+00 020100\n" +
                        "  -4.689755     -25.70351     0.0000000E+00 0.0000000E+00  1.950267     101100\n" +
                        "  -4.196062     -26.90121     0.0000000E+00 0.0000000E+00  2.577834     011100\n" +
                        "  0.0000000E+00 0.0000000E+00  2.234997     -23.92563     0.0000000E+00 002100\n" +
                        " -0.9925480     -3.696445     0.0000000E+00 0.0000000E+00 0.3467838     200001\n" +
                        "  -3.780723     -13.90991     0.0000000E+00 0.0000000E+00 0.5614337     110001\n" +
                        "  -2.861782     -9.794896     0.0000000E+00 0.0000000E+00 0.6613196     020001\n" +
                        "  0.0000000E+00 0.0000000E+00 0.3145799    -0.3863809     0.0000000E+00 101001\n" +
                        "  0.0000000E+00 0.0000000E+00 0.6888564     -3.578169     0.0000000E+00 011001\n" +
                        " -0.1433462     -1.424276     0.0000000E+00 0.0000000E+00 0.3791432     002001\n" +
                        "  -3.893233     -20.34424     0.0000000E+00 0.0000000E+00  1.506935     100200\n" +
                        "  -3.220087     -21.41767     0.0000000E+00 0.0000000E+00  1.999127     010200\n" +
                        "  0.0000000E+00 0.0000000E+00  4.113271     -35.52018     0.0000000E+00 001200\n" +
                        "  0.0000000E+00 0.0000000E+00 0.8610822     -3.249888     0.0000000E+00 100101\n" +
                        "  0.0000000E+00 0.0000000E+00  1.487559     -9.523625     0.0000000E+00 010101\n" +
                        " -0.4995865     -8.341644     0.0000000E+00 0.0000000E+00  1.498149     001101\n" +
                        " -0.2702332    -0.6800157     0.0000000E+00 0.0000000E+00 0.6865829E-01 100002\n" +
                        " -0.1396788     -1.371671     0.0000000E+00 0.0000000E+00 0.1835329     010002\n" +
                        "  0.0000000E+00 0.0000000E+00 0.5970649E-01-0.2921996     0.0000000E+00 001002\n" +
                        "  0.0000000E+00 0.0000000E+00  3.190505     -19.31217     0.0000000E+00 000300\n" +
                        " -0.5553931     -9.410842     0.0000000E+00 0.0000000E+00  1.929034     000201\n" +
                        "  0.0000000E+00 0.0000000E+00 0.8201167     -2.051054     0.0000000E+00 000102\n" +
                        " -0.2357721E-01-0.6198957E-01 0.0000000E+00 0.0000000E+00 0.2070888     000003\n" +
                        "   5.729413      21.82772     0.0000000E+00 0.0000000E+00 -2.109991     400000\n" +
                        "   22.01099      87.66485     0.0000000E+00 0.0000000E+00 -7.881964     310000\n" +
                        "   32.47928      133.9056     0.0000000E+00 0.0000000E+00 -11.19060     220000\n" +
                        "   23.59749      98.59979     0.0000000E+00 0.0000000E+00 -7.076584     130000\n" +
                        "   7.394222      30.15222     0.0000000E+00 0.0000000E+00 -1.860081     040000\n" +
                        "  0.0000000E+00 0.0000000E+00 -3.330711      3.313601     0.0000000E+00 301000\n" +
                        "  0.0000000E+00 0.0000000E+00 -10.70912      11.39369     0.0000000E+00 211000\n" +
                        "  0.0000000E+00 0.0000000E+00 -11.37282      10.35227     0.0000000E+00 121000\n" +
                        "  0.0000000E+00 0.0000000E+00 -4.165731      4.523121     0.0000000E+00 031000\n" +
                        "   5.762657      30.89719     0.0000000E+00 0.0000000E+00 -2.430907     202000\n" +
                        "   12.92875      67.66467     0.0000000E+00 0.0000000E+00 -4.185215     112000\n" +
                        "   7.078798      36.44915     0.0000000E+00 0.0000000E+00 -2.041982     022000\n" +
                        "  0.0000000E+00 0.0000000E+00 -1.147125      9.949361     0.0000000E+00 103000\n" +
                        "  0.0000000E+00 0.0000000E+00 -1.428826      16.03552     0.0000000E+00 013000\n" +
                        "  0.2462387      5.306285     0.0000000E+00 0.0000000E+00 -1.434925     004000\n" +
                        "  0.0000000E+00 0.0000000E+00 -5.273616      7.136113     0.0000000E+00 300100\n" +
                        "  0.0000000E+00 0.0000000E+00 -16.57358      22.76451     0.0000000E+00 210100\n" +
                        "  0.0000000E+00 0.0000000E+00 -17.53116      22.36267     0.0000000E+00 120100\n" +
                        "  0.0000000E+00 0.0000000E+00 -6.587023      11.44500     0.0000000E+00 030100\n" +
                        "   18.34039      96.93344     0.0000000E+00 0.0000000E+00 -7.923840     201100\n" +
                        "   40.56225      210.1240     0.0000000E+00 0.0000000E+00 -13.61558     111100\n" +
                        "   22.28326      115.8941     0.0000000E+00 0.0000000E+00 -6.668503     021100\n" +
                        "  0.0000000E+00 0.0000000E+00 -7.004712      46.13486     0.0000000E+00 102100\n" +
                        "  0.0000000E+00 0.0000000E+00 -8.802276      74.14518     0.0000000E+00 012100\n" +
                        "   1.575718      33.34687     0.0000000E+00 0.0000000E+00 -8.383956     003100\n" +
                        "   3.211088      10.08565     0.0000000E+00 0.0000000E+00 -1.599495     300001\n" +
                        "   13.18461      53.72315     0.0000000E+00 0.0000000E+00 -5.967894     210001\n" +
                        "   17.92137      83.45477     0.0000000E+00 0.0000000E+00 -6.760097     120001\n" +
                        "   7.791627      41.48440     0.0000000E+00 0.0000000E+00 -2.560119     030001\n" +
                        "  0.0000000E+00 0.0000000E+00 -1.416914      1.193676     0.0000000E+00 201001\n" +
                        "  0.0000000E+00 0.0000000E+00 -4.176081      1.975907     0.0000000E+00 111001\n" +
                        "  0.0000000E+00 0.0000000E+00 -2.513708      1.245591     0.0000000E+00 021001\n" +
                        "   2.764889      10.73789     0.0000000E+00 0.0000000E+00 -1.190159     102001\n" +
                        "   3.328529      16.13911     0.0000000E+00 0.0000000E+00 -1.928453     012001\n" +
                        "  0.0000000E+00 0.0000000E+00-0.3806090      5.245353     0.0000000E+00 003001\n" +
                        "   14.56141      75.82653     0.0000000E+00 0.0000000E+00 -6.383481     200200\n" +
                        "   33.42680      168.6282     0.0000000E+00 0.0000000E+00 -10.95986     110200\n" +
                        "   19.28376      97.27382     0.0000000E+00 0.0000000E+00 -5.852506     020200\n" +
                        "  0.0000000E+00 0.0000000E+00 -12.99556      71.72674     0.0000000E+00 101200\n" +
                        "  0.0000000E+00 0.0000000E+00 -16.53896      116.7807     0.0000000E+00 011200\n" +
                        "   3.924377      79.24745     0.0000000E+00 0.0000000E+00 -18.77678     002200\n" +
                        "  0.0000000E+00 0.0000000E+00 -2.994456      2.838004     0.0000000E+00 200101\n" +
                        "  0.0000000E+00 0.0000000E+00 -7.306161      5.323218     0.0000000E+00 110101\n" +
                        "  0.0000000E+00 0.0000000E+00 -5.124392      6.094928     0.0000000E+00 020101\n" +
                        "   10.73215      45.01324     0.0000000E+00 0.0000000E+00 -4.625209     101101\n" +
                        "   12.55301      62.77717     0.0000000E+00 0.0000000E+00 -7.057710     011101\n" +
                        "  0.0000000E+00 0.0000000E+00 -3.474443      35.89377     0.0000000E+00 002101\n" +
                        "  0.9978295      2.922835     0.0000000E+00 0.0000000E+00-0.5542928     200002\n" +
                        "   4.529836      15.49678     0.0000000E+00 0.0000000E+00 -1.302784     110002\n" +
                        "   4.266280      14.62576     0.0000000E+00 0.0000000E+00 -1.225730     020002\n" +
                        "  0.0000000E+00 0.0000000E+00-0.2408000     0.1493449     0.0000000E+00 101002\n" +
                        "  0.0000000E+00 0.0000000E+00-0.7163003      2.750248     0.0000000E+00 011002\n" +
                        "  0.2592479      1.388173     0.0000000E+00 0.0000000E+00-0.5034520     002002\n" +
                        "  0.0000000E+00 0.0000000E+00 -7.928571      39.39580     0.0000000E+00 100300\n" +
                        "  0.0000000E+00 0.0000000E+00 -10.22937      65.81746     0.0000000E+00 010300\n" +
                        "   4.433485      87.46623     0.0000000E+00 0.0000000E+00 -19.34349     001300\n" +
                        "   10.28706      44.54552     0.0000000E+00 0.0000000E+00 -4.278304     100201\n" +
                        "   11.42185      59.83342     0.0000000E+00 0.0000000E+00 -6.367310     010201\n" +
                        "  0.0000000E+00 0.0000000E+00 -8.493322      71.88802     0.0000000E+00 001201\n" +
                        "  0.0000000E+00 0.0000000E+00-0.8727974      2.431830     0.0000000E+00 100102\n" +
                        "  0.0000000E+00 0.0000000E+00 -1.853078      9.777011     0.0000000E+00 010102\n" +
                        "   1.029889      9.133697     0.0000000E+00 0.0000000E+00 -2.299744     001102\n" +
                        "  0.2492491     0.5709543     0.0000000E+00 0.0000000E+00-0.8567038E-01 100003\n" +
                        "  0.2910751      1.536125     0.0000000E+00 0.0000000E+00-0.2081619     010003\n" +
                        "  0.0000000E+00 0.0000000E+00-0.3789942E-01 0.2232429     0.0000000E+00 001003\n" +
                        "   2.000774      38.67072     0.0000000E+00 0.0000000E+00 -8.067283     000400\n" +
                        "  0.0000000E+00 0.0000000E+00 -7.524692      47.87237     0.0000000E+00 000301\n" +
                        "   1.133232      12.20653     0.0000000E+00 0.0000000E+00 -3.100234     000202\n" +
                        "  0.0000000E+00 0.0000000E+00-0.6916586      1.830468     0.0000000E+00 000103\n" +
                        "  0.2189242E-01 0.4986079E-01 0.0000000E+00 0.0000000E+00-0.1851781     000004\n" +
                        "  -16.56309     -62.26760     0.0000000E+00 0.0000000E+00  6.914754     500000\n" +
                        "  -79.65974     -317.5984     0.0000000E+00 0.0000000E+00  32.80004     410000\n" +
                        "  -155.8989     -650.0408     0.0000000E+00 0.0000000E+00  62.85060     320000\n" +
                        "  -159.1327     -686.4660     0.0000000E+00 0.0000000E+00  61.80867     230000\n" +
                        "  -87.27619     -386.7781     0.0000000E+00 0.0000000E+00  31.28177     140000\n" +
                        "  -20.86953     -95.89548     0.0000000E+00 0.0000000E+00  6.521898     050000\n" +
                        "  0.0000000E+00 0.0000000E+00  7.741580     -6.167316     0.0000000E+00 401000\n" +
                        "  0.0000000E+00 0.0000000E+00  32.98993     -36.85141     0.0000000E+00 311000\n" +
                        "  0.0000000E+00 0.0000000E+00  54.66561     -77.09176     0.0000000E+00 221000\n" +
                        "  0.0000000E+00 0.0000000E+00  42.90119     -67.48980     0.0000000E+00 131000\n" +
                        "  0.0000000E+00 0.0000000E+00  13.13806     -21.12450     0.0000000E+00 041000\n" +
                        "  -21.17543     -100.5928     0.0000000E+00 0.0000000E+00  10.64556     302000\n" +
                        "  -62.67172     -320.1501     0.0000000E+00 0.0000000E+00  31.44224     212000\n" +
                        "  -65.38105     -348.9465     0.0000000E+00 0.0000000E+00  30.39015     122000\n" +
                        "  -24.40687     -133.7592     0.0000000E+00 0.0000000E+00  10.15456     032000\n" +
                        "  0.0000000E+00 0.0000000E+00  6.650744     -26.76976     0.0000000E+00 203000\n" +
                        "  0.0000000E+00 0.0000000E+00  13.63678     -53.16048     0.0000000E+00 113000\n" +
                        "  0.0000000E+00 0.0000000E+00  6.229036     -31.03021     0.0000000E+00 023000\n" +
                        "  -8.822429     -43.09193     0.0000000E+00 0.0000000E+00  5.670943     104000\n" +
                        "  -7.343775     -47.39731     0.0000000E+00 0.0000000E+00  7.936563     014000\n" +
                        "  0.0000000E+00 0.0000000E+00  1.226770     -20.51778     0.0000000E+00 005000\n" +
                        "  0.0000000E+00 0.0000000E+00  12.15679     -15.05888     0.0000000E+00 400100\n" +
                        "  0.0000000E+00 0.0000000E+00  50.60572     -77.36007     0.0000000E+00 310100\n" +
                        "  0.0000000E+00 0.0000000E+00  82.68471     -147.7549     0.0000000E+00 220100\n" +
                        "  0.0000000E+00 0.0000000E+00  63.94305     -122.9075     0.0000000E+00 130100\n" +
                        "  0.0000000E+00 0.0000000E+00  19.63650     -38.75213     0.0000000E+00 040100\n" +
                        "  -67.47945     -318.0339     0.0000000E+00 0.0000000E+00  34.27058     301100\n" +
                        "  -197.3146     -1004.848     0.0000000E+00 0.0000000E+00  101.6235     211100\n" +
                        "  -204.4989     -1096.247     0.0000000E+00 0.0000000E+00  99.78771     121100\n" +
                        "  -76.79736     -424.4039     0.0000000E+00 0.0000000E+00  34.10926     031100\n" +
                        "  0.0000000E+00 0.0000000E+00  34.00252     -125.1694     0.0000000E+00 202100\n" +
                        "  0.0000000E+00 0.0000000E+00  68.44931     -248.4567     0.0000000E+00 112100\n" +
                        "  0.0000000E+00 0.0000000E+00  33.05214     -154.0234     0.0000000E+00 022100\n" +
                        "  -53.91780     -269.6626     0.0000000E+00 0.0000000E+00  33.86892     103100\n" +
                        "  -46.12838     -296.5934     0.0000000E+00 0.0000000E+00  46.71403     013100\n" +
                        "  0.0000000E+00 0.0000000E+00  12.91116     -159.2404     0.0000000E+00 004100\n" +
                        "  -12.40751     -37.88662     0.0000000E+00 0.0000000E+00  5.623180     400001\n" +
                        "  -60.94136     -206.0555     0.0000000E+00 0.0000000E+00  25.93980     310001\n" +
                        "  -108.1719     -392.3951     0.0000000E+00 0.0000000E+00  44.06892     220001\n" +
                        "  -88.26287     -335.4854     0.0000000E+00 0.0000000E+00  33.03710     130001\n" +
                        "  -29.90828     -115.2219     0.0000000E+00 0.0000000E+00  9.646723     040001\n" +
                        "  0.0000000E+00 0.0000000E+00  4.903145     -6.098955     0.0000000E+00 301001\n" +
                        "  0.0000000E+00 0.0000000E+00  22.84442     -31.57339     0.0000000E+00 211001\n" +
                        "  0.0000000E+00 0.0000000E+00  32.23521     -43.19754     0.0000000E+00 121001\n" +
                        "  0.0000000E+00 0.0000000E+00  14.75003     -20.01483     0.0000000E+00 031001\n" +
                        "  -12.70742     -52.73160     0.0000000E+00 0.0000000E+00  7.109542     202001\n" +
                        "  -34.39382     -155.0162     0.0000000E+00 0.0000000E+00  15.20364     112001\n" +
                        "  -22.71267     -106.1487     0.0000000E+00 0.0000000E+00  8.388979     022001\n" +
                        "  0.0000000E+00 0.0000000E+00  1.590933     -12.01398     0.0000000E+00 103001\n" +
                        "  0.0000000E+00 0.0000000E+00  2.591095     -27.91773     0.0000000E+00 013001\n" +
                        "  -1.279850     -10.53477     0.0000000E+00 0.0000000E+00  3.667609     004001\n" +
                        "  -53.40064     -248.5973     0.0000000E+00 0.0000000E+00  27.43525     300200\n" +
                        "  -158.3409     -801.3949     0.0000000E+00 0.0000000E+00  82.93427     210200\n" +
                        "  -167.7956     -900.1749     0.0000000E+00 0.0000000E+00  83.53888     120200\n" +
                        "  -64.66276     -361.0117     0.0000000E+00 0.0000000E+00  29.35320     030200\n" +
                        "  0.0000000E+00 0.0000000E+00  57.43948     -196.5861     0.0000000E+00 201200\n" +
                        "  0.0000000E+00 0.0000000E+00  115.0609     -388.6692     0.0000000E+00 111200\n" +
                        "  0.0000000E+00 0.0000000E+00  58.56129     -252.3259     0.0000000E+00 021200\n" +
                        "  -125.0121     -635.1039     0.0000000E+00 0.0000000E+00  76.91968     102200\n" +
                        "  -109.8850     -701.9217     0.0000000E+00 0.0000000E+00  104.9582     012200\n" +
                        "  0.0000000E+00 0.0000000E+00  48.68964     -494.8538     0.0000000E+00 003200\n" +
                        "  0.0000000E+00 0.0000000E+00  10.45808     -15.50872     0.0000000E+00 300101\n" +
                        "  0.0000000E+00 0.0000000E+00  43.98153     -70.53208     0.0000000E+00 210101\n" +
                        "  0.0000000E+00 0.0000000E+00  58.69449     -92.79725     0.0000000E+00 120101\n" +
                        "  0.0000000E+00 0.0000000E+00  26.25626     -46.98102     0.0000000E+00 030101\n" +
                        "  -48.09719     -209.3409     0.0000000E+00 0.0000000E+00  26.17528     201101\n" +
                        "  -125.5339     -578.6513     0.0000000E+00 0.0000000E+00  55.01572     111101\n" +
                        "  -80.83816     -387.3321     0.0000000E+00 0.0000000E+00  30.59229     021101\n" +
                        "  0.0000000E+00 0.0000000E+00  12.80896     -78.97478     0.0000000E+00 102101\n" +
                        "  0.0000000E+00 0.0000000E+00  19.91122     -165.5050     0.0000000E+00 012101\n" +
                        "  -8.520296     -81.54603     0.0000000E+00 0.0000000E+00  25.50031     003101\n" +
                        "  -4.647938     -11.84751     0.0000000E+00 0.0000000E+00  2.581672     300002\n" +
                        "  -21.54808     -70.90071     0.0000000E+00 0.0000000E+00  11.29129     210002\n" +
                        "  -32.44425     -127.2091     0.0000000E+00 0.0000000E+00  15.15663     120002\n" +
                        "  -16.06673     -73.14233     0.0000000E+00 0.0000000E+00  6.546591     030002\n" +
                        "  0.0000000E+00 0.0000000E+00  1.491285     -1.914055     0.0000000E+00 201002\n" +
                        "  0.0000000E+00 0.0000000E+00  6.019538     -4.943257     0.0000000E+00 111002\n" +
                        "  0.0000000E+00 0.0000000E+00  4.689313     -2.923592     0.0000000E+00 021002\n" +
                        "  -4.077084     -13.10355     0.0000000E+00 0.0000000E+00  1.973366     102002\n" +
                        "  -5.974436     -24.03208     0.0000000E+00 0.0000000E+00  3.361465     012002\n" +
                        "  0.0000000E+00 0.0000000E+00 0.2489965     -5.611987     0.0000000E+00 003002\n" +
                        "  0.0000000E+00 0.0000000E+00  32.73306     -103.6200     0.0000000E+00 200300\n" +
                        "  0.0000000E+00 0.0000000E+00  65.45688     -204.1181     0.0000000E+00 110300\n" +
                        "  0.0000000E+00 0.0000000E+00  35.67982     -139.9738     0.0000000E+00 020300\n" +
                        "  -131.7906     -676.1702     0.0000000E+00 0.0000000E+00  79.28471     101300\n" +
                        "  -119.2735     -754.5272     0.0000000E+00 0.0000000E+00  107.4610     011300\n" +
                        "  0.0000000E+00 0.0000000E+00  87.81406     -780.2982     0.0000000E+00 002300\n" +
                        "  -44.30778     -197.8870     0.0000000E+00 0.0000000E+00  23.76268     200201\n" +
                        "  -116.1480     -537.3179     0.0000000E+00 0.0000000E+00  49.46418     110201\n" +
                        "  -76.35533     -364.2077     0.0000000E+00 0.0000000E+00  28.84494     020201\n" +
                        "  0.0000000E+00 0.0000000E+00  29.95367     -158.0793     0.0000000E+00 101201\n" +
                        "  0.0000000E+00 0.0000000E+00  45.39191     -315.1386     0.0000000E+00 011201\n" +
                        "  -21.68074     -229.2031     0.0000000E+00 0.0000000E+00  65.80215     002201\n" +
                        "  0.0000000E+00 0.0000000E+00  4.113924     -5.081762     0.0000000E+00 200102\n" +
                        "  0.0000000E+00 0.0000000E+00  13.14123     -12.51491     0.0000000E+00 110102\n" +
                        "  0.0000000E+00 0.0000000E+00  10.49198     -11.37045     0.0000000E+00 020102\n" +
                        "  -17.98923     -64.19993     0.0000000E+00 0.0000000E+00  8.481977     101102\n" +
                        "  -24.90439     -106.6383     0.0000000E+00 0.0000000E+00  13.88461     011102\n" +
                        "  0.0000000E+00 0.0000000E+00  3.805727     -46.07902     0.0000000E+00 002102\n" +
                        "  -1.129507     -2.759957     0.0000000E+00 0.0000000E+00 0.7761092     200003\n" +
                        "  -5.389407     -16.78500     0.0000000E+00 0.0000000E+00  2.233993     110003\n" +
                        "  -5.701842     -18.85624     0.0000000E+00 0.0000000E+00  2.053171     020003\n" +
                        "  0.0000000E+00 0.0000000E+00 0.1909742    -0.5633970E-01 0.0000000E+00 101003\n" +
                        "  0.0000000E+00 0.0000000E+00 0.7372220     -2.292229     0.0000000E+00 011003\n" +
                        " -0.3670588     -1.426647     0.0000000E+00 0.0000000E+00 0.6353959     002003\n" +
                        "  -54.01422     -278.2510     0.0000000E+00 0.0000000E+00  31.49969     100400\n" +
                        "  -50.29234     -314.6656     0.0000000E+00 0.0000000E+00  42.55242     010400\n" +
                        "  0.0000000E+00 0.0000000E+00  78.52213     -630.3181     0.0000000E+00 001400\n" +
                        "  0.0000000E+00 0.0000000E+00  22.00538     -103.9370     0.0000000E+00 100301\n" +
                        "  0.0000000E+00 0.0000000E+00  32.88496     -204.1988     0.0000000E+00 010301\n" +
                        "  -25.12833     -287.6018     0.0000000E+00 0.0000000E+00  76.17633     001301\n" +
                        "  -19.18516     -72.77780     0.0000000E+00 0.0000000E+00  8.719535     100202\n" +
                        "  -25.30165     -114.4946     0.0000000E+00 0.0000000E+00  13.91629     010202\n" +
                        "  0.0000000E+00 0.0000000E+00  12.02287     -109.3632     0.0000000E+00 001202\n" +
                        "  0.0000000E+00 0.0000000E+00 0.8806482     -2.006067     0.0000000E+00 100103\n" +
                        "  0.0000000E+00 0.0000000E+00  2.197044     -9.868828     0.0000000E+00 010103\n" +
                        "  -1.642463     -10.12404     0.0000000E+00 0.0000000E+00  3.198987     001103\n" +
                        " -0.2464652    -0.5166055     0.0000000E+00 0.0000000E+00 0.1067851     100004\n" +
                        " -0.4032778     -1.648552     0.0000000E+00 0.0000000E+00 0.2466871     010004\n" +
                        "  0.0000000E+00 0.0000000E+00 0.2308272E-01-0.1810007     0.0000000E+00 001004\n" +
                        "  0.0000000E+00 0.0000000E+00  29.02149     -210.9829     0.0000000E+00 000500\n" +
                        "  -11.43309     -140.1835     0.0000000E+00 0.0000000E+00  34.55617     000401\n" +
                        "  0.0000000E+00 0.0000000E+00  12.26460     -83.38621     0.0000000E+00 000302\n" +
                        "  -1.901666     -15.01012     0.0000000E+00 0.0000000E+00  4.496007     000203\n" +
                        "  0.0000000E+00 0.0000000E+00 0.6051801     -1.685591     0.0000000E+00 000104\n" +
                        " -0.2115865E-01-0.4304278E-01 0.0000000E+00 0.0000000E+00 0.1695897     000005"
        )));

        list.add(BaseUtils.Content.BiContent.create(keyDistance().get(3), CosyArbitraryOrder.readMap(
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
                        "  0.4627381E-01-0.3539130     0.0000000E+00 0.0000000E+00-0.3006609     000002\n" +
                        "  -7.548881     -14.43976     0.0000000E+00 0.0000000E+00 0.5206894     300000\n" +
                        "  -28.34533     -80.45964     0.0000000E+00 0.0000000E+00-0.5305555     210000\n" +
                        "  -34.35726     -102.1052     0.0000000E+00 0.0000000E+00 -3.541702     120000\n" +
                        "  -12.56501     -30.32597     0.0000000E+00 0.0000000E+00 -2.512751     030000\n" +
                        "  0.0000000E+00 0.0000000E+00  7.877202      73.91540     0.0000000E+00 201000\n" +
                        "  0.0000000E+00 0.0000000E+00  17.44805      171.3409     0.0000000E+00 111000\n" +
                        "  0.0000000E+00 0.0000000E+00  9.593542      111.8895     0.0000000E+00 021000\n" +
                        "  -3.084190      13.33711     0.0000000E+00 0.0000000E+00  2.847691     102000\n" +
                        "  -2.359442      16.61604     0.0000000E+00 0.0000000E+00  3.637161     012000\n" +
                        "  0.0000000E+00 0.0000000E+00 -1.894200      8.428171     0.0000000E+00 003000\n" +
                        "  0.0000000E+00 0.0000000E+00  12.39478      120.2163     0.0000000E+00 200100\n" +
                        "  0.0000000E+00 0.0000000E+00  27.02626      273.0272     0.0000000E+00 110100\n" +
                        "  0.0000000E+00 0.0000000E+00  15.34162      173.2842     0.0000000E+00 020100\n" +
                        "  -9.841612      43.95692     0.0000000E+00 0.0000000E+00  7.020336     101100\n" +
                        "  -8.583478      58.59828     0.0000000E+00 0.0000000E+00  7.115553     011100\n" +
                        "  0.0000000E+00 0.0000000E+00 -3.812296      64.07610     0.0000000E+00 002100\n" +
                        "  -1.784575      3.904018     0.0000000E+00 0.0000000E+00 0.7121954     200001\n" +
                        "  -6.280837      4.107539     0.0000000E+00 0.0000000E+00  1.512743     110001\n" +
                        "  -3.008046      8.409197     0.0000000E+00 0.0000000E+00  1.552517     020001\n" +
                        "  0.0000000E+00 0.0000000E+00 0.4003682      16.16305     0.0000000E+00 101001\n" +
                        "  0.0000000E+00 0.0000000E+00 -3.229214     -3.329124     0.0000000E+00 011001\n" +
                        " -0.5511356      10.91626     0.0000000E+00 0.0000000E+00  1.060297     002001\n" +
                        "  -8.037731      36.97358     0.0000000E+00 0.0000000E+00  3.959451     100200\n" +
                        "  -7.526691      49.57900     0.0000000E+00 0.0000000E+00  2.290994     010200\n" +
                        "  0.0000000E+00 0.0000000E+00-0.1378136      131.0349     0.0000000E+00 001200\n" +
                        "  0.0000000E+00 0.0000000E+00 -1.047298      12.20413     0.0000000E+00 100101\n" +
                        "  0.0000000E+00 0.0000000E+00 -9.281065     -42.39896     0.0000000E+00 010101\n" +
                        "  -4.355238      18.40395     0.0000000E+00 0.0000000E+00  3.589364     001101\n" +
                        " -0.4564555      2.720133     0.0000000E+00 0.0000000E+00 0.9640761E-01 100002\n" +
                        "  -1.044043    -0.5388188     0.0000000E+00 0.0000000E+00 0.1227174     010002\n" +
                        "  0.0000000E+00 0.0000000E+00-0.2691036    -0.4526466     0.0000000E+00 001002\n" +
                        "  0.0000000E+00 0.0000000E+00  2.845467      82.32793     0.0000000E+00 000300\n" +
                        "  -5.668256     0.3130095     0.0000000E+00 0.0000000E+00  3.586607     000201\n" +
                        "  0.0000000E+00 0.0000000E+00 0.1799044      2.039251     0.0000000E+00 000102\n" +
                        " -0.1606915E-01 0.3810118     0.0000000E+00 0.0000000E+00 0.2654777     000003\n" +
                        "   6.881595     -17.61107     0.0000000E+00 0.0000000E+00 -6.845914     400000\n" +
                        "   34.46042      111.4821     0.0000000E+00 0.0000000E+00 -31.54392     310000\n" +
                        "   64.90735      513.5184     0.0000000E+00 0.0000000E+00 -57.09625     220000\n" +
                        "   53.96508      619.4390     0.0000000E+00 0.0000000E+00 -48.68264     130000\n" +
                        "   13.76344      216.4894     0.0000000E+00 0.0000000E+00 -17.63290     040000\n" +
                        "  0.0000000E+00 0.0000000E+00 -7.651665     -73.41182     0.0000000E+00 301000\n" +
                        "  0.0000000E+00 0.0000000E+00 -17.07946     -146.5001     0.0000000E+00 211000\n" +
                        "  0.0000000E+00 0.0000000E+00 -12.03552     -134.4593     0.0000000E+00 121000\n" +
                        "  0.0000000E+00 0.0000000E+00-0.1388507E-01 -67.13041     0.0000000E+00 031000\n" +
                        "   22.96186      31.45551     0.0000000E+00 0.0000000E+00 -8.472958     202000\n" +
                        "   56.93330      196.4564     0.0000000E+00 0.0000000E+00 -27.88096     112000\n" +
                        "   32.43927      182.2794     0.0000000E+00 0.0000000E+00 -29.34026     022000\n" +
                        "  0.0000000E+00 0.0000000E+00  3.022800     -45.02632     0.0000000E+00 103000\n" +
                        "  0.0000000E+00 0.0000000E+00  8.970884     -5.126479     0.0000000E+00 013000\n" +
                        "  -2.937321     -66.41220     0.0000000E+00 0.0000000E+00 -6.936183     004000\n" +
                        "  0.0000000E+00 0.0000000E+00 -11.98341     -137.7616     0.0000000E+00 300100\n" +
                        "  0.0000000E+00 0.0000000E+00 -23.52706     -319.0470     0.0000000E+00 210100\n" +
                        "  0.0000000E+00 0.0000000E+00 -9.098532     -316.0968     0.0000000E+00 120100\n" +
                        "  0.0000000E+00 0.0000000E+00  9.497394     -117.5097     0.0000000E+00 030100\n" +
                        "   65.58180      115.5638     0.0000000E+00 0.0000000E+00 -32.67004     201100\n" +
                        "   160.2225      660.2617     0.0000000E+00 0.0000000E+00 -101.9836     111100\n" +
                        "   93.69884      629.1194     0.0000000E+00 0.0000000E+00 -100.7738     021100\n" +
                        "  0.0000000E+00 0.0000000E+00  1.958988     -223.3958     0.0000000E+00 102100\n" +
                        "  0.0000000E+00 0.0000000E+00  27.95113      6.732466     0.0000000E+00 012100\n" +
                        "  -12.73809     -340.7920     0.0000000E+00 0.0000000E+00 -39.09801     003100\n" +
                        "   5.156265     -50.50964     0.0000000E+00 0.0000000E+00 -2.058124     300001\n" +
                        "   39.82187     -62.80146     0.0000000E+00 0.0000000E+00 -3.661120     210001\n" +
                        "   74.83596      72.07360     0.0000000E+00 0.0000000E+00  4.116027     120001\n" +
                        "   40.20864      73.23746     0.0000000E+00 0.0000000E+00  7.406442     030001\n" +
                        "  0.0000000E+00 0.0000000E+00 -8.714155     -44.43929     0.0000000E+00 201001\n" +
                        "  0.0000000E+00 0.0000000E+00 -31.54528     -191.6994     0.0000000E+00 111001\n" +
                        "  0.0000000E+00 0.0000000E+00 -24.99017     -183.4629     0.0000000E+00 021001\n" +
                        "   5.033747     -32.88990     0.0000000E+00 0.0000000E+00 -3.991537     102001\n" +
                        "   7.610385     -22.76922     0.0000000E+00 0.0000000E+00 -6.979000     012001\n" +
                        "  0.0000000E+00 0.0000000E+00  6.173301      19.88835     0.0000000E+00 003001\n" +
                        "   46.31702      103.2264     0.0000000E+00 0.0000000E+00 -30.57083     200200\n" +
                        "   111.7733      540.8408     0.0000000E+00 0.0000000E+00 -91.80574     110200\n" +
                        "   65.63944      519.7165     0.0000000E+00 0.0000000E+00 -86.61238     020200\n" +
                        "  0.0000000E+00 0.0000000E+00 -9.383406     -375.8620     0.0000000E+00 101200\n" +
                        "  0.0000000E+00 0.0000000E+00  36.57101      40.09154     0.0000000E+00 011200\n" +
                        "  -17.82587     -651.2662     0.0000000E+00 0.0000000E+00 -86.62564     002200\n" +
                        "  0.0000000E+00 0.0000000E+00 -21.79922     -146.0187     0.0000000E+00 200101\n" +
                        "  0.0000000E+00 0.0000000E+00 -67.18451     -476.8809     0.0000000E+00 110101\n" +
                        "  0.0000000E+00 0.0000000E+00 -50.21681     -398.8238     0.0000000E+00 020101\n" +
                        "   18.77463     -128.6259     0.0000000E+00 0.0000000E+00 -14.69955     101101\n" +
                        "   25.97381     -127.7590     0.0000000E+00 0.0000000E+00 -19.43564     011101\n" +
                        "  0.0000000E+00 0.0000000E+00  22.07999      23.40256     0.0000000E+00 002101\n" +
                        "  0.6343076     -16.67091     0.0000000E+00 0.0000000E+00 -1.173920     200002\n" +
                        "   6.412447     -30.38510     0.0000000E+00 0.0000000E+00 -3.423297     110002\n" +
                        "   5.495860     -13.88244     0.0000000E+00 0.0000000E+00 -3.602460     020002\n" +
                        "  0.0000000E+00 0.0000000E+00-0.8691561     -10.70409     0.0000000E+00 101002\n" +
                        "  0.0000000E+00 0.0000000E+00  1.582919     -9.469867     0.0000000E+00 011002\n" +
                        "  0.1869392E-01 -12.80741     0.0000000E+00 0.0000000E+00 -1.463683     002002\n" +
                        "  0.0000000E+00 0.0000000E+00 -6.237555     -199.5083     0.0000000E+00 100300\n" +
                        "  0.0000000E+00 0.0000000E+00  27.25872      65.34665     0.0000000E+00 010300\n" +
                        "  -4.912281     -540.7832     0.0000000E+00 0.0000000E+00 -89.33362     001300\n" +
                        "   17.93654     -124.2899     0.0000000E+00 0.0000000E+00 -11.64471     100201\n" +
                        "   24.30520     -147.1294     0.0000000E+00 0.0000000E+00 -9.790409     010201\n" +
                        "  0.0000000E+00 0.0000000E+00  20.52485     -91.93103     0.0000000E+00 001201\n" +
                        "  0.0000000E+00 0.0000000E+00-0.6785895     -23.09798     0.0000000E+00 100102\n" +
                        "  0.0000000E+00 0.0000000E+00  8.484609      14.45492     0.0000000E+00 010102\n" +
                        "   2.660167     -44.78423     0.0000000E+00 0.0000000E+00 -5.917516     001102\n" +
                        "  0.2136797     -3.822094     0.0000000E+00 0.0000000E+00-0.1202773     100003\n" +
                        "   1.124010     -2.740793     0.0000000E+00 0.0000000E+00-0.7772533E-01 010003\n" +
                        "  0.0000000E+00 0.0000000E+00 0.4051230      1.578414     0.0000000E+00 001003\n" +
                        "   4.959555     -156.9821     0.0000000E+00 0.0000000E+00 -36.25029     000400\n" +
                        "  0.0000000E+00 0.0000000E+00  1.330944     -124.6699     0.0000000E+00 000301\n" +
                        "   5.378392     -29.89786     0.0000000E+00 0.0000000E+00 -6.534725     000202\n" +
                        "  0.0000000E+00 0.0000000E+00 0.1911543    -0.7024153E-01 0.0000000E+00 000103\n" +
                        " -0.6892498E-02-0.3860967     0.0000000E+00 0.0000000E+00-0.2449019     000004\n" +
                        "  -24.42808      319.6468     0.0000000E+00 0.0000000E+00  5.754759     500000\n" +
                        "  -174.6853      1442.650     0.0000000E+00 0.0000000E+00  11.91600     410000\n" +
                        "  -520.5634      2501.436     0.0000000E+00 0.0000000E+00 -28.55110     320000\n" +
                        "  -826.4123      1879.747     0.0000000E+00 0.0000000E+00 -114.3813     230000\n" +
                        "  -692.9366      405.8713     0.0000000E+00 0.0000000E+00 -125.8941     140000\n" +
                        "  -238.0900     -63.35094     0.0000000E+00 0.0000000E+00 -45.87135     050000\n" +
                        "  0.0000000E+00 0.0000000E+00  49.05982      290.5353     0.0000000E+00 401000\n" +
                        "  0.0000000E+00 0.0000000E+00  233.9693      725.7460     0.0000000E+00 311000\n" +
                        "  0.0000000E+00 0.0000000E+00  475.5203      307.7522     0.0000000E+00 221000\n" +
                        "  0.0000000E+00 0.0000000E+00  497.9232     -290.7973     0.0000000E+00 131000\n" +
                        "  0.0000000E+00 0.0000000E+00  218.0500     -115.2691     0.0000000E+00 041000\n" +
                        "  -41.61413      334.1570     0.0000000E+00 0.0000000E+00 -8.083983     302000\n" +
                        "  -179.8018      717.9827     0.0000000E+00 0.0000000E+00 -81.79377     212000\n" +
                        "  -262.1180      403.8976     0.0000000E+00 0.0000000E+00 -160.5711     122000\n" +
                        "  -128.0440      33.69410     0.0000000E+00 0.0000000E+00 -93.83841     032000\n" +
                        "  0.0000000E+00 0.0000000E+00 -5.944252      346.3638     0.0000000E+00 203000\n" +
                        "  0.0000000E+00 0.0000000E+00 -31.78754      818.6427     0.0000000E+00 113000\n" +
                        "  0.0000000E+00 0.0000000E+00 -38.07001      510.0002     0.0000000E+00 023000\n" +
                        "  -25.83569      271.2630     0.0000000E+00 0.0000000E+00  14.40276     104000\n" +
                        "  -31.09891      219.6662     0.0000000E+00 0.0000000E+00  17.00845     014000\n" +
                        "  0.0000000E+00 0.0000000E+00 -30.93472     -154.7555     0.0000000E+00 005000\n" +
                        "  0.0000000E+00 0.0000000E+00  87.94019      475.5705     0.0000000E+00 400100\n" +
                        "  0.0000000E+00 0.0000000E+00  425.8310      1242.935     0.0000000E+00 310100\n" +
                        "  0.0000000E+00 0.0000000E+00  879.5000      833.1267     0.0000000E+00 220100\n" +
                        "  0.0000000E+00 0.0000000E+00  922.5343      45.10871     0.0000000E+00 130100\n" +
                        "  0.0000000E+00 0.0000000E+00  400.6539      58.34018     0.0000000E+00 040100\n" +
                        "  -143.7902      1081.991     0.0000000E+00 0.0000000E+00 -27.69710     301100\n" +
                        "  -622.8652      2537.842     0.0000000E+00 0.0000000E+00 -273.3204     211100\n" +
                        "  -946.2614      1759.157     0.0000000E+00 0.0000000E+00 -527.4797     121100\n" +
                        "  -505.4710      337.9575     0.0000000E+00 0.0000000E+00 -306.1632     031100\n" +
                        "  0.0000000E+00 0.0000000E+00  35.68675      1803.418     0.0000000E+00 202100\n" +
                        "  0.0000000E+00 0.0000000E+00  71.93452      3907.918     0.0000000E+00 112100\n" +
                        "  0.0000000E+00 0.0000000E+00  67.35050      2287.899     0.0000000E+00 022100\n" +
                        "  -152.8167      1525.248     0.0000000E+00 0.0000000E+00  65.98159     103100\n" +
                        "  -167.0159      1290.292     0.0000000E+00 0.0000000E+00  52.28240     013100\n" +
                        "  0.0000000E+00 0.0000000E+00 -182.3228     -920.6513     0.0000000E+00 004100\n" +
                        "   1.500355      254.0550     0.0000000E+00 0.0000000E+00  14.37935     400001\n" +
                        "  -12.48848      870.9886     0.0000000E+00 0.0000000E+00  86.13389     310001\n" +
                        "  -49.36879      650.8508     0.0000000E+00 0.0000000E+00  194.3334     220001\n" +
                        "  -54.29150     -439.5737     0.0000000E+00 0.0000000E+00  193.1609     130001\n" +
                        "  -10.62839     -456.2568     0.0000000E+00 0.0000000E+00  75.09584     040001\n" +
                        "  0.0000000E+00 0.0000000E+00  9.807754      123.8279     0.0000000E+00 301001\n" +
                        "  0.0000000E+00 0.0000000E+00  18.59187      289.1963     0.0000000E+00 211001\n" +
                        "  0.0000000E+00 0.0000000E+00 -11.22096      141.4387     0.0000000E+00 121001\n" +
                        "  0.0000000E+00 0.0000000E+00 -27.28831      39.41050     0.0000000E+00 031001\n" +
                        "  -26.61536      217.9141     0.0000000E+00 0.0000000E+00  12.09516     202001\n" +
                        "  -115.5369      158.9150     0.0000000E+00 0.0000000E+00  44.99752     112001\n" +
                        "  -94.03141     -179.6183     0.0000000E+00 0.0000000E+00  65.67274     022001\n" +
                        "  0.0000000E+00 0.0000000E+00 -10.59660      53.96687     0.0000000E+00 103001\n" +
                        "  0.0000000E+00 0.0000000E+00 -22.53538      70.62906     0.0000000E+00 013001\n" +
                        "   7.353654      144.3638     0.0000000E+00 0.0000000E+00  13.16513     004001\n" +
                        "  -127.3499      881.7474     0.0000000E+00 0.0000000E+00 -24.02042     300200\n" +
                        "  -569.5277      2199.686     0.0000000E+00 0.0000000E+00 -232.0003     210200\n" +
                        "  -902.7958      1694.879     0.0000000E+00 0.0000000E+00 -447.2494     120200\n" +
                        "  -509.2620      422.4065     0.0000000E+00 0.0000000E+00 -263.6745     030200\n" +
                        "  0.0000000E+00 0.0000000E+00  173.5430      3043.154     0.0000000E+00 201200\n" +
                        "  0.0000000E+00 0.0000000E+00  529.3510      6353.293     0.0000000E+00 111200\n" +
                        "  0.0000000E+00 0.0000000E+00  557.8859      3745.720     0.0000000E+00 021200\n" +
                        "  -342.8181      3198.648     0.0000000E+00 0.0000000E+00  111.9578     102200\n" +
                        "  -329.9427      2812.506     0.0000000E+00 0.0000000E+00  19.48011     012200\n" +
                        "  0.0000000E+00 0.0000000E+00 -418.7941     -2122.706     0.0000000E+00 003200\n" +
                        "  0.0000000E+00 0.0000000E+00  23.14515      292.2665     0.0000000E+00 300101\n" +
                        "  0.0000000E+00 0.0000000E+00  36.78496      727.0551     0.0000000E+00 210101\n" +
                        "  0.0000000E+00 0.0000000E+00 -37.52266      569.5771     0.0000000E+00 120101\n" +
                        "  0.0000000E+00 0.0000000E+00 -77.77653      176.8648     0.0000000E+00 030101\n" +
                        "  -108.3835      557.0319     0.0000000E+00 0.0000000E+00  63.20539     201101\n" +
                        "  -397.0642      33.77497     0.0000000E+00 0.0000000E+00  225.7705     111101\n" +
                        "  -296.9434     -974.8448     0.0000000E+00 0.0000000E+00  286.5212     021101\n" +
                        "  0.0000000E+00 0.0000000E+00 -31.98186      458.5913     0.0000000E+00 102101\n" +
                        "  0.0000000E+00 0.0000000E+00 -101.6532      304.5057     0.0000000E+00 012101\n" +
                        "   43.71528      983.3877     0.0000000E+00 0.0000000E+00  89.72566     003101\n" +
                        " -0.6273099      109.0991     0.0000000E+00 0.0000000E+00  3.417292     300002\n" +
                        "  -36.30694      344.7132     0.0000000E+00 0.0000000E+00  9.552600     210002\n" +
                        "  -106.2273      236.7991     0.0000000E+00 0.0000000E+00 0.6226781     120002\n" +
                        "  -77.95580     -8.596724     0.0000000E+00 0.0000000E+00 -11.74059     030002\n" +
                        "  0.0000000E+00 0.0000000E+00  6.677016      22.88998     0.0000000E+00 201002\n" +
                        "  0.0000000E+00 0.0000000E+00  39.04891      170.2286     0.0000000E+00 111002\n" +
                        "  0.0000000E+00 0.0000000E+00  41.35653      210.5949     0.0000000E+00 021002\n" +
                        "  -3.674903      71.10966     0.0000000E+00 0.0000000E+00  5.565118     102002\n" +
                        "  -11.78625      54.53179     0.0000000E+00 0.0000000E+00  10.08557     012002\n" +
                        "  0.0000000E+00 0.0000000E+00 -12.96719     -56.15527     0.0000000E+00 003002\n" +
                        "  0.0000000E+00 0.0000000E+00  169.2379      1744.520     0.0000000E+00 200300\n" +
                        "  0.0000000E+00 0.0000000E+00  546.5990      3666.184     0.0000000E+00 110300\n" +
                        "  0.0000000E+00 0.0000000E+00  569.9260      2295.885     0.0000000E+00 020300\n" +
                        "  -346.2517      3000.213     0.0000000E+00 0.0000000E+00  84.52313     101300\n" +
                        "  -280.7341      2771.655     0.0000000E+00 0.0000000E+00 -70.92560     011300\n" +
                        "  0.0000000E+00 0.0000000E+00 -452.7179     -2288.361     0.0000000E+00 002300\n" +
                        "  -98.72749      326.2857     0.0000000E+00 0.0000000E+00  76.29482     200201\n" +
                        "  -326.4778     -360.0637     0.0000000E+00 0.0000000E+00  261.1958     110201\n" +
                        "  -226.9800     -1117.830     0.0000000E+00 0.0000000E+00  300.8756     020201\n" +
                        "  0.0000000E+00 0.0000000E+00 -17.14900      1027.608     0.0000000E+00 101201\n" +
                        "  0.0000000E+00 0.0000000E+00 -167.2867      410.4979     0.0000000E+00 011201\n" +
                        "   85.39280      2351.261     0.0000000E+00 0.0000000E+00  233.9783     002201\n" +
                        "  0.0000000E+00 0.0000000E+00  26.41318      145.0972     0.0000000E+00 200102\n" +
                        "  0.0000000E+00 0.0000000E+00  108.5382      602.3486     0.0000000E+00 110102\n" +
                        "  0.0000000E+00 0.0000000E+00  101.9599      603.8911     0.0000000E+00 020102\n" +
                        "  -20.22814      278.0248     0.0000000E+00 0.0000000E+00  24.07618     101102\n" +
                        "  -47.02052      262.8746     0.0000000E+00 0.0000000E+00  34.54968     011102\n" +
                        "  0.0000000E+00 0.0000000E+00 -58.62824     -206.1197     0.0000000E+00 002102\n" +
                        "  0.8102212      25.13924     0.0000000E+00 0.0000000E+00  1.625151     200003\n" +
                        "  -3.522164      66.11052     0.0000000E+00 0.0000000E+00  5.381152     110003\n" +
                        "  -5.530948      36.06161     0.0000000E+00 0.0000000E+00  6.492020     020003\n" +
                        "  0.0000000E+00 0.0000000E+00 0.4783199      4.514056     0.0000000E+00 101003\n" +
                        "  0.0000000E+00 0.0000000E+00-0.6200889      13.90195     0.0000000E+00 011003\n" +
                        "  0.4556549      15.28548     0.0000000E+00 0.0000000E+00  1.965045     002003\n" +
                        "  -133.7060      1083.052     0.0000000E+00 0.0000000E+00  23.09828     100400\n" +
                        "  -86.32352      1080.887     0.0000000E+00 0.0000000E+00 -63.81397     010400\n" +
                        "  0.0000000E+00 0.0000000E+00 -207.1941     -1022.600     0.0000000E+00 001400\n" +
                        "  0.0000000E+00 0.0000000E+00  3.249680      690.6662     0.0000000E+00 100301\n" +
                        "  0.0000000E+00 0.0000000E+00 -122.1412      110.1933     0.0000000E+00 010301\n" +
                        "   56.70143      2391.715     0.0000000E+00 0.0000000E+00  280.0276     001301\n" +
                        "  -24.45381      278.6433     0.0000000E+00 0.0000000E+00  22.98249     100202\n" +
                        "  -49.58317      318.0518     0.0000000E+00 0.0000000E+00  22.09323     010202\n" +
                        "  0.0000000E+00 0.0000000E+00 -76.74512     -149.7394     0.0000000E+00 001202\n" +
                        "  0.0000000E+00 0.0000000E+00  1.248298      23.48372     0.0000000E+00 100103\n" +
                        "  0.0000000E+00 0.0000000E+00 -7.279107      7.776491     0.0000000E+00 010103\n" +
                        " -0.4878640      68.35778     0.0000000E+00 0.0000000E+00  8.668349     001103\n" +
                        " -0.4454086E-02  4.494363     0.0000000E+00 0.0000000E+00 0.1693687     100004\n" +
                        "  -1.052456      5.725502     0.0000000E+00 0.0000000E+00 0.5495815E-04 010004\n" +
                        "  0.0000000E+00 0.0000000E+00-0.5841119     -2.709969     0.0000000E+00 001004\n" +
                        "  0.0000000E+00 0.0000000E+00 -17.35295     -64.51126     0.0000000E+00 000500\n" +
                        "  0.5935649      870.9293     0.0000000E+00 0.0000000E+00  130.6239     000401\n" +
                        "  0.0000000E+00 0.0000000E+00 -24.52231      61.60881     0.0000000E+00 000302\n" +
                        "  -3.786487      63.29171     0.0000000E+00 0.0000000E+00  10.27033     000203\n" +
                        "  0.0000000E+00 0.0000000E+00-0.6378330     -2.478363     0.0000000E+00 000104\n" +
                        "  0.2686283E-01 0.3835219     0.0000000E+00 0.0000000E+00 0.2360719     000005"
        )));

        list.add(BaseUtils.Content.BiContent.create(keyDistance().get(4), CosyArbitraryOrder.readMap(
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
                        " -0.8825391E-01-0.3774416     0.0000000E+00 0.0000000E+00-0.3586644     000002\n" +
                        "  -18.67415     -54.35598     0.0000000E+00 0.0000000E+00 -2.212960     300000\n" +
                        "  -77.91567     -184.1862     0.0000000E+00 0.0000000E+00 -15.91092     210000\n" +
                        "  -98.36533     -193.0432     0.0000000E+00 0.0000000E+00 -32.32622     120000\n" +
                        "  -40.81568     -102.7747     0.0000000E+00 0.0000000E+00 -20.44566     030000\n" +
                        "  0.0000000E+00 0.0000000E+00  32.95778      105.6218     0.0000000E+00 201000\n" +
                        "  0.0000000E+00 0.0000000E+00  88.16105      407.3126     0.0000000E+00 111000\n" +
                        "  0.0000000E+00 0.0000000E+00  57.30470      307.8995     0.0000000E+00 021000\n" +
                        "  -32.44173     -372.2135     0.0000000E+00 0.0000000E+00  4.320211     102000\n" +
                        "  -63.46779     -717.5965     0.0000000E+00 0.0000000E+00  3.962361     012000\n" +
                        "  0.0000000E+00 0.0000000E+00 -14.57485     -140.6916     0.0000000E+00 003000\n" +
                        "  0.0000000E+00 0.0000000E+00  53.68187      176.3085     0.0000000E+00 200100\n" +
                        "  0.0000000E+00 0.0000000E+00  150.5038      758.8950     0.0000000E+00 110100\n" +
                        "  0.0000000E+00 0.0000000E+00  107.6864      675.7605     0.0000000E+00 020100\n" +
                        "  -121.9924     -1398.907     0.0000000E+00 0.0000000E+00  5.008687     101100\n" +
                        "  -245.1880     -2766.868     0.0000000E+00 0.0000000E+00 -4.506792     011100\n" +
                        "  0.0000000E+00 0.0000000E+00 -68.00946     -755.0911     0.0000000E+00 002100\n" +
                        " -0.4751886     -15.60305     0.0000000E+00 0.0000000E+00  1.866843     200001\n" +
                        "  -1.459560     -32.05816     0.0000000E+00 0.0000000E+00  6.876686     110001\n" +
                        "   9.463800      41.95518     0.0000000E+00 0.0000000E+00  7.141820     020001\n" +
                        "  0.0000000E+00 0.0000000E+00 0.1969104     -12.10712     0.0000000E+00 101001\n" +
                        "  0.0000000E+00 0.0000000E+00 -11.60621     -25.50332     0.0000000E+00 011001\n" +
                        "   1.351508    -0.1759505     0.0000000E+00 0.0000000E+00  1.268457     002001\n" +
                        "  -113.2135     -1291.418     0.0000000E+00 0.0000000E+00 -4.377607     100200\n" +
                        "  -231.5838     -2593.315     0.0000000E+00 0.0000000E+00 -18.67564     010200\n" +
                        "  0.0000000E+00 0.0000000E+00 -108.8056     -1304.025     0.0000000E+00 001200\n" +
                        "  0.0000000E+00 0.0000000E+00 -9.592396     -67.81789     0.0000000E+00 100101\n" +
                        "  0.0000000E+00 0.0000000E+00 -37.12464     -106.3683     0.0000000E+00 010101\n" +
                        "   2.555020      34.49211     0.0000000E+00 0.0000000E+00  4.850977     001101\n" +
                        " -0.2832876    -0.5910927     0.0000000E+00 0.0000000E+00-0.8217138E-01 100002\n" +
                        "  -4.281375     -17.47141     0.0000000E+00 0.0000000E+00-0.2148643     010002\n" +
                        "  0.0000000E+00 0.0000000E+00-0.1719974      2.855892     0.0000000E+00 001002\n" +
                        "  0.0000000E+00 0.0000000E+00 -57.77728     -726.6247     0.0000000E+00 000300\n" +
                        "   4.734465      99.96056     0.0000000E+00 0.0000000E+00  5.500659     000201\n" +
                        "  0.0000000E+00 0.0000000E+00 0.6266790    -0.1243447     0.0000000E+00 000102\n" +
                        "  0.2514508      1.083454     0.0000000E+00 0.0000000E+00 0.3201977     000003\n" +
                        "  0.2981339      234.5276     0.0000000E+00 0.0000000E+00 -25.00367     400000\n" +
                        "   19.10739      1289.914     0.0000000E+00 0.0000000E+00 -143.7277     310000\n" +
                        "   11.69631      2523.271     0.0000000E+00 0.0000000E+00 -318.7455     220000\n" +
                        "  -23.00113      2673.110     0.0000000E+00 0.0000000E+00 -331.2237     130000\n" +
                        "  -13.78934      1403.286     0.0000000E+00 0.0000000E+00 -140.0643     040000\n" +
                        "  0.0000000E+00 0.0000000E+00  71.90639      1073.147     0.0000000E+00 301000\n" +
                        "  0.0000000E+00 0.0000000E+00  386.9661      4933.960     0.0000000E+00 211000\n" +
                        "  0.0000000E+00 0.0000000E+00  734.8717      8713.478     0.0000000E+00 121000\n" +
                        "  0.0000000E+00 0.0000000E+00  515.4491      5876.055     0.0000000E+00 031000\n" +
                        "  -147.8968     -2092.865     0.0000000E+00 0.0000000E+00 -69.41982     202000\n" +
                        "  -577.8422     -8002.567     0.0000000E+00 0.0000000E+00 -236.2205     112000\n" +
                        "  -665.3380     -8588.687     0.0000000E+00 0.0000000E+00 -219.7842     022000\n" +
                        "  0.0000000E+00 0.0000000E+00 -85.26396     -710.5321     0.0000000E+00 103000\n" +
                        "  0.0000000E+00 0.0000000E+00 -181.1863     -1560.929     0.0000000E+00 013000\n" +
                        "   14.04170      466.2863     0.0000000E+00 0.0000000E+00 -19.23180     004000\n" +
                        "  0.0000000E+00 0.0000000E+00  154.1731      2124.283     0.0000000E+00 300100\n" +
                        "  0.0000000E+00 0.0000000E+00  791.3676      9701.092     0.0000000E+00 210100\n" +
                        "  0.0000000E+00 0.0000000E+00  1412.965      16278.30     0.0000000E+00 120100\n" +
                        "  0.0000000E+00 0.0000000E+00  932.9875      10223.76     0.0000000E+00 030100\n" +
                        "  -601.2451     -7670.314     0.0000000E+00 0.0000000E+00 -283.5583     201100\n" +
                        "  -2190.445     -27926.74     0.0000000E+00 0.0000000E+00 -944.7612     111100\n" +
                        "  -2358.167     -28906.86     0.0000000E+00 0.0000000E+00 -855.0699     021100\n" +
                        "  0.0000000E+00 0.0000000E+00 -377.7554     -2503.730     0.0000000E+00 102100\n" +
                        "  0.0000000E+00 0.0000000E+00 -795.6590     -5741.461     0.0000000E+00 012100\n" +
                        "   93.74203      2807.829     0.0000000E+00 0.0000000E+00 -122.3588     003100\n" +
                        "   5.464880      6.400123     0.0000000E+00 0.0000000E+00  2.291900     300001\n" +
                        "   112.4118      201.4288     0.0000000E+00 0.0000000E+00  28.97600     210001\n" +
                        "   215.8722     -72.70201     0.0000000E+00 0.0000000E+00  88.59115     120001\n" +
                        "   105.6501     -376.5211     0.0000000E+00 0.0000000E+00  77.89440     030001\n" +
                        "  0.0000000E+00 0.0000000E+00 -45.41424     -159.7969     0.0000000E+00 201001\n" +
                        "  0.0000000E+00 0.0000000E+00 -214.0621     -1170.300     0.0000000E+00 111001\n" +
                        "  0.0000000E+00 0.0000000E+00 -228.4738     -1586.740     0.0000000E+00 021001\n" +
                        "   22.20979      125.1502     0.0000000E+00 0.0000000E+00 -4.652017     102001\n" +
                        "   127.6183      1054.198     0.0000000E+00 0.0000000E+00 -4.810782     012001\n" +
                        "  0.0000000E+00 0.0000000E+00  13.22833     -82.42069     0.0000000E+00 003001\n" +
                        "  -596.8705     -6942.496     0.0000000E+00 0.0000000E+00 -281.0154     200200\n" +
                        "  -2064.418     -24293.41     0.0000000E+00 0.0000000E+00 -925.5258     110200\n" +
                        "  -2110.119     -24435.60     0.0000000E+00 0.0000000E+00 -827.4172     020200\n" +
                        "  0.0000000E+00 0.0000000E+00 -530.5550     -2364.753     0.0000000E+00 101200\n" +
                        "  0.0000000E+00 0.0000000E+00 -1089.724     -5863.351     0.0000000E+00 011200\n" +
                        "   211.8512      6187.618     0.0000000E+00 0.0000000E+00 -311.5696     002200\n" +
                        "  0.0000000E+00 0.0000000E+00 -106.6790     -381.7028     0.0000000E+00 200101\n" +
                        "  0.0000000E+00 0.0000000E+00 -441.8763     -2332.152     0.0000000E+00 110101\n" +
                        "  0.0000000E+00 0.0000000E+00 -458.5711     -3077.567     0.0000000E+00 020101\n" +
                        "   151.8484      1214.708     0.0000000E+00 0.0000000E+00 -8.307813     101101\n" +
                        "   612.2893      5452.512     0.0000000E+00 0.0000000E+00  21.58905     011101\n" +
                        "  0.0000000E+00 0.0000000E+00  89.12440      37.40530     0.0000000E+00 002101\n" +
                        "  -5.915015     0.6474202     0.0000000E+00 0.0000000E+00 -2.687176     200002\n" +
                        "   2.618860      164.2154     0.0000000E+00 0.0000000E+00 -14.90298     110002\n" +
                        "  -1.447649      132.8562     0.0000000E+00 0.0000000E+00 -19.71706     020002\n" +
                        "  0.0000000E+00 0.0000000E+00-0.6579386      22.10615     0.0000000E+00 101002\n" +
                        "  0.0000000E+00 0.0000000E+00  13.94103      100.3185     0.0000000E+00 011002\n" +
                        "  -6.622787     -30.90950     0.0000000E+00 0.0000000E+00 -1.527725     002002\n" +
                        "  0.0000000E+00 0.0000000E+00 -220.4355     -227.8893     0.0000000E+00 100300\n" +
                        "  0.0000000E+00 0.0000000E+00 -431.4075     -1037.302     0.0000000E+00 010300\n" +
                        "   200.1041      5960.437     0.0000000E+00 0.0000000E+00 -378.4104     001300\n" +
                        "   205.4259      1828.934     0.0000000E+00 0.0000000E+00  11.53561     100201\n" +
                        "   700.9440      6496.481     0.0000000E+00 0.0000000E+00  71.85167     010201\n" +
                        "  0.0000000E+00 0.0000000E+00  186.5871      789.9620     0.0000000E+00 001201\n" +
                        "  0.0000000E+00 0.0000000E+00  8.781583      91.88236     0.0000000E+00 100102\n" +
                        "  0.0000000E+00 0.0000000E+00  57.27366      311.2467     0.0000000E+00 010102\n" +
                        "  -24.27686     -134.3708     0.0000000E+00 0.0000000E+00 -8.089436     001102\n" +
                        "  -1.695558     -12.91509     0.0000000E+00 0.0000000E+00 0.3878087     100003\n" +
                        "   3.370782      6.255555     0.0000000E+00 0.0000000E+00  1.122259     010003\n" +
                        "  0.0000000E+00 0.0000000E+00 0.6767812E-02 -8.055895     0.0000000E+00 001003\n" +
                        "   64.70615      2081.950     0.0000000E+00 0.0000000E+00 -185.2933     000400\n" +
                        "  0.0000000E+00 0.0000000E+00  122.2442      794.4162     0.0000000E+00 000301\n" +
                        "  -29.08815     -220.5458     0.0000000E+00 0.0000000E+00 -11.29918     000202\n" +
                        "  0.0000000E+00 0.0000000E+00 -1.296330     -14.86915     0.0000000E+00 000103\n" +
                        " -0.4145999     -1.679045     0.0000000E+00 0.0000000E+00-0.3119839     000004\n" +
                        "  -57.97705      364.5793     0.0000000E+00 0.0000000E+00 -28.21971     500000\n" +
                        "  -699.5618      3203.913     0.0000000E+00 0.0000000E+00 -272.9109     410000\n" +
                        "  -2414.390      14955.16     0.0000000E+00 0.0000000E+00 -1000.554     320000\n" +
                        "  -4096.431      30549.73     0.0000000E+00 0.0000000E+00 -1711.256     230000\n" +
                        "  -3786.556      26282.44     0.0000000E+00 0.0000000E+00 -1381.390     140000\n" +
                        "  -1532.655      7559.347     0.0000000E+00 0.0000000E+00 -441.8684     050000\n" +
                        "  0.0000000E+00 0.0000000E+00  613.3298      4811.134     0.0000000E+00 401000\n" +
                        "  0.0000000E+00 0.0000000E+00  4080.570      35284.39     0.0000000E+00 311000\n" +
                        "  0.0000000E+00 0.0000000E+00  9963.119      90099.53     0.0000000E+00 221000\n" +
                        "  0.0000000E+00 0.0000000E+00  10438.41      94957.81     0.0000000E+00 131000\n" +
                        "  0.0000000E+00 0.0000000E+00  3985.477      35073.52     0.0000000E+00 041000\n" +
                        "  -1284.555     -11984.13     0.0000000E+00 0.0000000E+00 -487.8633     302000\n" +
                        "  -6482.322     -57876.39     0.0000000E+00 0.0000000E+00 -2678.317     212000\n" +
                        "  -9960.644     -84234.62     0.0000000E+00 0.0000000E+00 -4832.865     122000\n" +
                        "  -5078.159     -40751.52     0.0000000E+00 0.0000000E+00 -2920.470     032000\n" +
                        "  0.0000000E+00 0.0000000E+00 -354.3794     -283.1194     0.0000000E+00 203000\n" +
                        "  0.0000000E+00 0.0000000E+00 -513.7532      6660.727     0.0000000E+00 113000\n" +
                        "  0.0000000E+00 0.0000000E+00  104.8473      13153.50     0.0000000E+00 023000\n" +
                        "   89.22817      1913.181     0.0000000E+00 0.0000000E+00  91.61110     104000\n" +
                        "   63.83669      3155.082     0.0000000E+00 0.0000000E+00 -1.012198     014000\n" +
                        "  0.0000000E+00 0.0000000E+00  17.05263      1310.546     0.0000000E+00 005000\n" +
                        "  0.0000000E+00 0.0000000E+00  1064.529      8290.906     0.0000000E+00 400100\n" +
                        "  0.0000000E+00 0.0000000E+00  6977.372      58585.54     0.0000000E+00 310100\n" +
                        "  0.0000000E+00 0.0000000E+00  16976.14      147748.1     0.0000000E+00 220100\n" +
                        "  0.0000000E+00 0.0000000E+00  17857.30      156288.7     0.0000000E+00 130100\n" +
                        "  0.0000000E+00 0.0000000E+00  6900.360      58895.52     0.0000000E+00 040100\n" +
                        "  -4582.613     -38931.56     0.0000000E+00 0.0000000E+00 -1877.800     301100\n" +
                        "  -23292.78     -190329.6     0.0000000E+00 0.0000000E+00 -10099.64     211100\n" +
                        "  -36433.64     -281249.5     0.0000000E+00 0.0000000E+00 -17992.70     121100\n" +
                        "  -19067.92     -139526.2     0.0000000E+00 0.0000000E+00 -10856.25     031100\n" +
                        "  0.0000000E+00 0.0000000E+00 -1228.514      4516.394     0.0000000E+00 202100\n" +
                        "  0.0000000E+00 0.0000000E+00 -649.3965      53627.58     0.0000000E+00 112100\n" +
                        "  0.0000000E+00 0.0000000E+00  2428.494      84039.61     0.0000000E+00 022100\n" +
                        "   313.8574      9970.006     0.0000000E+00 0.0000000E+00  341.1676     103100\n" +
                        "  -226.1934      13830.27     0.0000000E+00 0.0000000E+00 -687.1848     013100\n" +
                        "  0.0000000E+00 0.0000000E+00  51.65516      8171.378     0.0000000E+00 004100\n" +
                        "   77.47416     -317.1369     0.0000000E+00 0.0000000E+00  42.45340     400001\n" +
                        "   159.4993     -4938.835     0.0000000E+00 0.0000000E+00  352.9299     310001\n" +
                        "   306.2844     -13020.81     0.0000000E+00 0.0000000E+00  958.4912     220001\n" +
                        "   682.1526     -12662.44     0.0000000E+00 0.0000000E+00  1127.040     130001\n" +
                        "   489.8652     -5029.754     0.0000000E+00 0.0000000E+00  540.1389     040001\n" +
                        "  0.0000000E+00 0.0000000E+00 -183.7951     -1859.193     0.0000000E+00 301001\n" +
                        "  0.0000000E+00 0.0000000E+00 -1102.920     -10506.53     0.0000000E+00 211001\n" +
                        "  0.0000000E+00 0.0000000E+00 -2166.485     -19598.10     0.0000000E+00 121001\n" +
                        "  0.0000000E+00 0.0000000E+00 -1630.944     -14410.30     0.0000000E+00 031001\n" +
                        "   223.8301      946.3938     0.0000000E+00 0.0000000E+00  117.0180     202001\n" +
                        "   810.2450      5464.987     0.0000000E+00 0.0000000E+00  607.5134     112001\n" +
                        "   1352.898      12171.10     0.0000000E+00 0.0000000E+00  752.1039     022001\n" +
                        "  0.0000000E+00 0.0000000E+00  7.029848     -279.5146     0.0000000E+00 103001\n" +
                        "  0.0000000E+00 0.0000000E+00  113.1144     -250.8883     0.0000000E+00 013001\n" +
                        "   32.58260     -21.35449     0.0000000E+00 0.0000000E+00  30.64045     004001\n" +
                        "  -4113.337     -31831.74     0.0000000E+00 0.0000000E+00 -1804.857     300200\n" +
                        "  -21111.88     -157727.7     0.0000000E+00 0.0000000E+00 -9539.487     210200\n" +
                        "  -33613.17     -236723.6     0.0000000E+00 0.0000000E+00 -16781.69     120200\n" +
                        "  -18003.41     -120418.5     0.0000000E+00 0.0000000E+00 -10078.59     030200\n" +
                        "  0.0000000E+00 0.0000000E+00 -903.1901      19228.84     0.0000000E+00 201200\n" +
                        "  0.0000000E+00 0.0000000E+00  2726.823      127658.6     0.0000000E+00 111200\n" +
                        "  0.0000000E+00 0.0000000E+00  7529.452      172333.1     0.0000000E+00 021200\n" +
                        "   18.17731      19415.03     0.0000000E+00 0.0000000E+00 -48.56619     102200\n" +
                        "  -2303.700      18916.77     0.0000000E+00 0.0000000E+00 -3910.990     012200\n" +
                        "  0.0000000E+00 0.0000000E+00 -152.7911      19521.76     0.0000000E+00 003200\n" +
                        "  0.0000000E+00 0.0000000E+00 -436.8488     -4337.208     0.0000000E+00 300101\n" +
                        "  0.0000000E+00 0.0000000E+00 -2639.145     -25228.09     0.0000000E+00 210101\n" +
                        "  0.0000000E+00 0.0000000E+00 -5074.403     -47364.80     0.0000000E+00 120101\n" +
                        "  0.0000000E+00 0.0000000E+00 -3609.419     -32839.34     0.0000000E+00 030101\n" +
                        "   1331.973      9071.814     0.0000000E+00 0.0000000E+00  662.7707     201101\n" +
                        "   4904.983      39422.94     0.0000000E+00 0.0000000E+00  3037.744     111101\n" +
                        "   6666.742      60669.46     0.0000000E+00 0.0000000E+00  3509.642     021101\n" +
                        "  0.0000000E+00 0.0000000E+00  160.7179     -1114.658     0.0000000E+00 102101\n" +
                        "  0.0000000E+00 0.0000000E+00  723.8442     -1701.220     0.0000000E+00 012101\n" +
                        "   156.5726     -1385.608     0.0000000E+00 0.0000000E+00  285.6749     003101\n" +
                        "   42.14614      333.6864     0.0000000E+00 0.0000000E+00 -6.216438     300002\n" +
                        "  -80.89778      169.8426     0.0000000E+00 0.0000000E+00 -40.74521     210002\n" +
                        "  -415.4443     -208.5780     0.0000000E+00 0.0000000E+00 -141.6189     120002\n" +
                        "  -284.3649      664.6016     0.0000000E+00 0.0000000E+00 -170.0863     030002\n" +
                        "  0.0000000E+00 0.0000000E+00  38.50585      59.93633     0.0000000E+00 201002\n" +
                        "  0.0000000E+00 0.0000000E+00  268.2585      1015.718     0.0000000E+00 111002\n" +
                        "  0.0000000E+00 0.0000000E+00  429.8236      2566.685     0.0000000E+00 021002\n" +
                        "   45.40358      488.1055     0.0000000E+00 0.0000000E+00 -5.960499     102002\n" +
                        "  -123.8829     -674.2217     0.0000000E+00 0.0000000E+00 -12.93254     012002\n" +
                        "  0.0000000E+00 0.0000000E+00 -7.464595      270.4914     0.0000000E+00 003002\n" +
                        "  0.0000000E+00 0.0000000E+00  310.1849      18339.85     0.0000000E+00 200300\n" +
                        "  0.0000000E+00 0.0000000E+00  4088.219      95439.05     0.0000000E+00 110300\n" +
                        "  0.0000000E+00 0.0000000E+00  6401.273      115811.6     0.0000000E+00 020300\n" +
                        "  -1148.185      15249.12     0.0000000E+00 0.0000000E+00 -1344.068     101300\n" +
                        "  -5044.374      2438.203     0.0000000E+00 0.0000000E+00 -7422.193     011300\n" +
                        "  0.0000000E+00 0.0000000E+00 -826.8860      21455.06     0.0000000E+00 002300\n" +
                        "   1741.417      13151.84     0.0000000E+00 0.0000000E+00  840.8231     200201\n" +
                        "   6293.728      51619.65     0.0000000E+00 0.0000000E+00  3558.694     110201\n" +
                        "   7621.321      67852.13     0.0000000E+00 0.0000000E+00  3923.009     020201\n" +
                        "  0.0000000E+00 0.0000000E+00  384.6794     -3290.639     0.0000000E+00 101201\n" +
                        "  0.0000000E+00 0.0000000E+00  1188.246     -7916.455     0.0000000E+00 011201\n" +
                        "   341.9949     -5198.801     0.0000000E+00 0.0000000E+00  979.4607     002201\n" +
                        "  0.0000000E+00 0.0000000E+00  146.4054      534.6707     0.0000000E+00 200102\n" +
                        "  0.0000000E+00 0.0000000E+00  737.7622      3452.904     0.0000000E+00 110102\n" +
                        "  0.0000000E+00 0.0000000E+00  1022.897      6308.047     0.0000000E+00 020102\n" +
                        "   21.10155      711.8409     0.0000000E+00 0.0000000E+00 -27.35916     101102\n" +
                        "  -880.2219     -6150.790     0.0000000E+00 0.0000000E+00 -120.6819     011102\n" +
                        "  0.0000000E+00 0.0000000E+00 -63.88675      1207.646     0.0000000E+00 002102\n" +
                        "   22.73042      101.2570     0.0000000E+00 0.0000000E+00  1.805450     200003\n" +
                        "   27.20745     -83.20690     0.0000000E+00 0.0000000E+00  20.78187     110003\n" +
                        "   3.236378     -343.6760     0.0000000E+00 0.0000000E+00  40.35421     020003\n" +
                        "  0.0000000E+00 0.0000000E+00  1.923803      3.039468     0.0000000E+00 101003\n" +
                        "  0.0000000E+00 0.0000000E+00 -19.96624     -160.8450     0.0000000E+00 011003\n" +
                        "   12.43492      59.62134     0.0000000E+00 0.0000000E+00  1.553718     002003\n" +
                        "  -1224.670      2756.996     0.0000000E+00 0.0000000E+00 -1244.544     100400\n" +
                        "  -3589.709     -9038.572     0.0000000E+00 0.0000000E+00 -4695.438     010400\n" +
                        "  0.0000000E+00 0.0000000E+00 -1200.839      9538.548     0.0000000E+00 001400\n" +
                        "  0.0000000E+00 0.0000000E+00  182.9821     -3994.258     0.0000000E+00 100301\n" +
                        "  0.0000000E+00 0.0000000E+00  381.6024     -10230.39     0.0000000E+00 010301\n" +
                        "   423.5429     -6484.009     0.0000000E+00 0.0000000E+00  1505.279     001301\n" +
                        "  -146.9653     -699.5409     0.0000000E+00 0.0000000E+00 -54.54211     100202\n" +
                        "  -1282.425     -9873.479     0.0000000E+00 0.0000000E+00 -237.9822     010202\n" +
                        "  0.0000000E+00 0.0000000E+00 -168.7995      1451.653     0.0000000E+00 001202\n" +
                        "  0.0000000E+00 0.0000000E+00 -5.348709     -56.50345     0.0000000E+00 100103\n" +
                        "  0.0000000E+00 0.0000000E+00 -87.86766     -551.1870     0.0000000E+00 010103\n" +
                        "   53.92513      270.9374     0.0000000E+00 0.0000000E+00  11.30415     001103\n" +
                        "   4.210598      29.86132     0.0000000E+00 0.0000000E+00-0.8068085     100004\n" +
                        " -0.2054245      26.55314     0.0000000E+00 0.0000000E+00 -3.056959     010004\n" +
                        "  0.0000000E+00 0.0000000E+00 0.2101480      12.49994     0.0000000E+00 001004\n" +
                        "  0.0000000E+00 0.0000000E+00 -586.4944      545.3013     0.0000000E+00 000500\n" +
                        "   241.5520     -2450.991     0.0000000E+00 0.0000000E+00  887.3275     000401\n" +
                        "  0.0000000E+00 0.0000000E+00 -131.1021      425.0679     0.0000000E+00 000302\n" +
                        "   69.61474      417.8199     0.0000000E+00 0.0000000E+00  19.94041     000203\n" +
                        "  0.0000000E+00 0.0000000E+00  2.739846      34.02772     0.0000000E+00 000104\n" +
                        "  0.5588110      1.851028     0.0000000E+00 0.0000000E+00 0.3484203     000005"
        )));

        list.add(BaseUtils.Content.BiContent.create(keyDistance().get(5), CosyArbitraryOrder.readMap(
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
                        " -0.1689991     0.1967531     0.0000000E+00 0.0000000E+00-0.4173160     000002\n" +
                        "  -32.60783      7.300043     0.0000000E+00 0.0000000E+00 -3.068726     300000\n" +
                        "  -152.6679     -225.3018     0.0000000E+00 0.0000000E+00 -28.12940     210000\n" +
                        "  -222.5021     -663.2690     0.0000000E+00 0.0000000E+00 -59.65663     120000\n" +
                        "  -111.8614     -412.4301     0.0000000E+00 0.0000000E+00 -36.84884     030000\n" +
                        "  0.0000000E+00 0.0000000E+00  46.72184     -157.5806     0.0000000E+00 201000\n" +
                        "  0.0000000E+00 0.0000000E+00  183.1864     -94.38903     0.0000000E+00 111000\n" +
                        "  0.0000000E+00 0.0000000E+00  135.0279      11.64060     0.0000000E+00 021000\n" +
                        "  -93.75614      322.8443     0.0000000E+00 0.0000000E+00  8.057143     102000\n" +
                        "  -197.2405      475.2790     0.0000000E+00 0.0000000E+00  4.142565     012000\n" +
                        "  0.0000000E+00 0.0000000E+00 -81.63935     -351.3330     0.0000000E+00 003000\n" +
                        "  0.0000000E+00 0.0000000E+00  64.10730     -414.9696     0.0000000E+00 200100\n" +
                        "  0.0000000E+00 0.0000000E+00  314.7794     -334.2735     0.0000000E+00 110100\n" +
                        "  0.0000000E+00 0.0000000E+00  282.1677      56.21643     0.0000000E+00 020100\n" +
                        "  -368.8432      1085.156     0.0000000E+00 0.0000000E+00  4.417638     101100\n" +
                        "  -774.3013      1728.352     0.0000000E+00 0.0000000E+00 -20.86845     011100\n" +
                        "  0.0000000E+00 0.0000000E+00 -430.5620     -1912.223     0.0000000E+00 002100\n" +
                        "   4.804523      69.16702     0.0000000E+00 0.0000000E+00  4.230745     200001\n" +
                        "   18.04401      210.2683     0.0000000E+00 0.0000000E+00  13.76437     110001\n" +
                        "   32.49660      77.17095     0.0000000E+00 0.0000000E+00  11.80215     020001\n" +
                        "  0.0000000E+00 0.0000000E+00 -14.71990     -94.07807     0.0000000E+00 101001\n" +
                        "  0.0000000E+00 0.0000000E+00 -32.33814     -140.5994     0.0000000E+00 011001\n" +
                        "   3.595088      11.46408     0.0000000E+00 0.0000000E+00  2.379338     002001\n" +
                        "  -355.6816      885.6549     0.0000000E+00 0.0000000E+00 -18.25588     100200\n" +
                        "  -744.2408      1465.428     0.0000000E+00 0.0000000E+00 -47.02154     010200\n" +
                        "  0.0000000E+00 0.0000000E+00 -763.2116     -3614.259     0.0000000E+00 001200\n" +
                        "  0.0000000E+00 0.0000000E+00 -56.78608     -263.9953     0.0000000E+00 100101\n" +
                        "  0.0000000E+00 0.0000000E+00 -94.83549     -322.7636     0.0000000E+00 010101\n" +
                        "   21.20840      36.06396     0.0000000E+00 0.0000000E+00  8.410374     001101\n" +
                        " -0.2468316      2.884559     0.0000000E+00 0.0000000E+00-0.5125621E-01 100002\n" +
                        "  -6.486463      28.39041     0.0000000E+00 0.0000000E+00-0.1156542     010002\n" +
                        "  0.0000000E+00 0.0000000E+00-0.6004867     -3.382246     0.0000000E+00 001002\n" +
                        "  0.0000000E+00 0.0000000E+00 -454.3868     -2375.144     0.0000000E+00 000300\n" +
                        "   38.29778      3.940980     0.0000000E+00 0.0000000E+00  8.041436     000201\n" +
                        "  0.0000000E+00 0.0000000E+00 -2.958596     -17.99717     0.0000000E+00 000102\n" +
                        "  0.4209646     -1.457267     0.0000000E+00 0.0000000E+00 0.3677776     000003\n" +
                        "  -68.41515     -1133.019     0.0000000E+00 0.0000000E+00 -55.10012     400000\n" +
                        "  -361.9051     -6175.573     0.0000000E+00 0.0000000E+00 -273.8634     310000\n" +
                        "  -893.8779     -13176.52     0.0000000E+00 0.0000000E+00 -569.2508     220000\n" +
                        "  -1056.364     -14399.37     0.0000000E+00 0.0000000E+00 -620.5630     130000\n" +
                        "  -458.1448     -6778.789     0.0000000E+00 0.0000000E+00 -291.2125     040000\n" +
                        "  0.0000000E+00 0.0000000E+00  277.8348     -933.9430     0.0000000E+00 301000\n" +
                        "  0.0000000E+00 0.0000000E+00  1104.968     -6541.547     0.0000000E+00 211000\n" +
                        "  0.0000000E+00 0.0000000E+00  2147.548     -9286.620     0.0000000E+00 121000\n" +
                        "  0.0000000E+00 0.0000000E+00  1801.138     -2166.908     0.0000000E+00 031000\n" +
                        "  -745.5930     -306.3971     0.0000000E+00 0.0000000E+00 -252.7602     202000\n" +
                        "  -2670.290      132.0771     0.0000000E+00 0.0000000E+00 -671.9935     112000\n" +
                        "  -2648.401      2235.360     0.0000000E+00 0.0000000E+00 -506.9676     022000\n" +
                        "  0.0000000E+00 0.0000000E+00 -276.3770      55.57403     0.0000000E+00 103000\n" +
                        "  0.0000000E+00 0.0000000E+00 -938.5260     -3607.166     0.0000000E+00 013000\n" +
                        "   57.00214     -727.7536     0.0000000E+00 0.0000000E+00 -64.08237     004000\n" +
                        "  0.0000000E+00 0.0000000E+00  604.3450     -1345.376     0.0000000E+00 300100\n" +
                        "  0.0000000E+00 0.0000000E+00  2315.942     -11692.07     0.0000000E+00 210100\n" +
                        "  0.0000000E+00 0.0000000E+00  3981.861     -18539.51     0.0000000E+00 120100\n" +
                        "  0.0000000E+00 0.0000000E+00  3040.169     -5439.752     0.0000000E+00 030100\n" +
                        "  -2804.021     -968.3203     0.0000000E+00 0.0000000E+00 -965.1054     201100\n" +
                        "  -9949.071     -2966.622     0.0000000E+00 0.0000000E+00 -2644.060     111100\n" +
                        "  -9627.605      2423.381     0.0000000E+00 0.0000000E+00 -2027.592     021100\n" +
                        "  0.0000000E+00 0.0000000E+00 -1447.329     -3679.506     0.0000000E+00 102100\n" +
                        "  0.0000000E+00 0.0000000E+00 -4707.533     -24825.83     0.0000000E+00 012100\n" +
                        "   262.2859     -5492.024     0.0000000E+00 0.0000000E+00 -406.5632     003100\n" +
                        "   10.29197     -52.22614     0.0000000E+00 0.0000000E+00 -8.216212     300001\n" +
                        "   254.8366      268.5470     0.0000000E+00 0.0000000E+00  27.60206     210001\n" +
                        "   598.0295      2670.176     0.0000000E+00 0.0000000E+00  178.0612     120001\n" +
                        "   375.3978      2827.302     0.0000000E+00 0.0000000E+00  170.7406     030001\n" +
                        "  0.0000000E+00 0.0000000E+00 -52.88531      422.7181     0.0000000E+00 201001\n" +
                        "  0.0000000E+00 0.0000000E+00 -544.5285      77.43150     0.0000000E+00 111001\n" +
                        "  0.0000000E+00 0.0000000E+00 -776.8559     -1336.560     0.0000000E+00 021001\n" +
                        "   54.51121     -222.5193     0.0000000E+00 0.0000000E+00 -40.57449     102001\n" +
                        "   330.6012     -1223.507     0.0000000E+00 0.0000000E+00 -38.29272     012001\n" +
                        "  0.0000000E+00 0.0000000E+00  60.98007      325.4952     0.0000000E+00 003001\n" +
                        "  -2620.218     -823.5681     0.0000000E+00 0.0000000E+00 -897.1437     200200\n" +
                        "  -9241.614     -5540.114     0.0000000E+00 0.0000000E+00 -2550.140     110200\n" +
                        "  -8756.021     -1905.836     0.0000000E+00 0.0000000E+00 -2032.250     020200\n" +
                        "  0.0000000E+00 0.0000000E+00 -2698.237     -15374.29     0.0000000E+00 101200\n" +
                        "  0.0000000E+00 0.0000000E+00 -7965.775     -55415.77     0.0000000E+00 011200\n" +
                        "   219.5804     -16393.31     0.0000000E+00 0.0000000E+00 -1010.027     002200\n" +
                        "  0.0000000E+00 0.0000000E+00 -67.18869      1605.540     0.0000000E+00 200101\n" +
                        "  0.0000000E+00 0.0000000E+00 -949.1126      1848.124     0.0000000E+00 110101\n" +
                        "  0.0000000E+00 0.0000000E+00 -1478.798     -1912.913     0.0000000E+00 020101\n" +
                        "   495.3281     -741.5536     0.0000000E+00 0.0000000E+00 -106.6045     101101\n" +
                        "   1800.447     -4470.846     0.0000000E+00 0.0000000E+00 -24.43314     011101\n" +
                        "  0.0000000E+00 0.0000000E+00  547.7167      2621.568     0.0000000E+00 002101\n" +
                        "  -20.41345     -84.24761     0.0000000E+00 0.0000000E+00 -9.066295     200002\n" +
                        "  -48.02772     -704.4853     0.0000000E+00 0.0000000E+00 -42.22640     110002\n" +
                        "  -53.31656     -661.6199     0.0000000E+00 0.0000000E+00 -43.41275     020002\n" +
                        "  0.0000000E+00 0.0000000E+00  22.92609      110.7021     0.0000000E+00 101002\n" +
                        "  0.0000000E+00 0.0000000E+00  84.69095      407.3599     0.0000000E+00 011002\n" +
                        "  -10.00026      63.32070     0.0000000E+00 0.0000000E+00 -4.100208     002002\n" +
                        "  0.0000000E+00 0.0000000E+00 -1742.378     -15215.75     0.0000000E+00 100300\n" +
                        "  0.0000000E+00 0.0000000E+00 -4522.189     -39887.12     0.0000000E+00 010300\n" +
                        "  -407.4060     -22891.28     0.0000000E+00 0.0000000E+00 -1180.832     001300\n" +
                        "   739.6823     -561.2546     0.0000000E+00 0.0000000E+00 -22.22925     100201\n" +
                        "   2206.777     -4079.968     0.0000000E+00 0.0000000E+00  131.1272     010201\n" +
                        "  0.0000000E+00 0.0000000E+00  1346.076      6329.126     0.0000000E+00 001201\n" +
                        "  0.0000000E+00 0.0000000E+00  98.47259      473.6588     0.0000000E+00 100102\n" +
                        "  0.0000000E+00 0.0000000E+00  263.0663      1140.394     0.0000000E+00 010102\n" +
                        "  -50.32199      205.1849     0.0000000E+00 0.0000000E+00 -20.94277     001102\n" +
                        "  -1.965614      29.52537     0.0000000E+00 0.0000000E+00 0.8635211     100003\n" +
                        "   5.751793     -1.639701     0.0000000E+00 0.0000000E+00  1.470901     010003\n" +
                        "  0.0000000E+00 0.0000000E+00 -2.998356     -16.64061     0.0000000E+00 001003\n" +
                        "  -537.0258     -12440.57     0.0000000E+00 0.0000000E+00 -552.9066     000400\n" +
                        "  0.0000000E+00 0.0000000E+00  1016.703      5020.942     0.0000000E+00 000301\n" +
                        "  -83.32298      212.4627     0.0000000E+00 0.0000000E+00 -24.98175     000202\n" +
                        "  0.0000000E+00 0.0000000E+00 -4.019489     -16.14320     0.0000000E+00 000103\n" +
                        " -0.6381522      2.806015     0.0000000E+00 0.0000000E+00-0.3412016     000004\n" +
                        "  -107.8326     -1115.591     0.0000000E+00 0.0000000E+00  29.74863     500000\n" +
                        "  -1901.371     -14995.46     0.0000000E+00 0.0000000E+00 -337.9724     410000\n" +
                        "  -7471.752     -67060.74     0.0000000E+00 0.0000000E+00 -2092.239     320000\n" +
                        "  -12763.75     -124531.3     0.0000000E+00 0.0000000E+00 -3747.997     230000\n" +
                        "  -11002.22     -102691.5     0.0000000E+00 0.0000000E+00 -2749.087     140000\n" +
                        "  -4248.049     -32752.23     0.0000000E+00 0.0000000E+00 -805.1524     050000\n" +
                        "  0.0000000E+00 0.0000000E+00  394.0762     -17348.44     0.0000000E+00 401000\n" +
                        "  0.0000000E+00 0.0000000E+00  5990.009     -90363.69     0.0000000E+00 311000\n" +
                        "  0.0000000E+00 0.0000000E+00  17004.52     -206888.0     0.0000000E+00 221000\n" +
                        "  0.0000000E+00 0.0000000E+00  15443.53     -243645.8     0.0000000E+00 131000\n" +
                        "  0.0000000E+00 0.0000000E+00  3852.472     -111097.8     0.0000000E+00 041000\n" +
                        "  -4541.508      2797.355     0.0000000E+00 0.0000000E+00 -1036.181     302000\n" +
                        "  -25251.51     -12724.38     0.0000000E+00 0.0000000E+00 -7639.935     212000\n" +
                        "  -44544.47     -83933.38     0.0000000E+00 0.0000000E+00 -15072.22     122000\n" +
                        "  -25854.38     -78678.37     0.0000000E+00 0.0000000E+00 -9108.056     032000\n" +
                        "  0.0000000E+00 0.0000000E+00 -4701.846     -40437.21     0.0000000E+00 203000\n" +
                        "  0.0000000E+00 0.0000000E+00 -9513.828     -100809.5     0.0000000E+00 113000\n" +
                        "  0.0000000E+00 0.0000000E+00 -3756.463     -64036.55     0.0000000E+00 023000\n" +
                        "  -344.9323     -12276.31     0.0000000E+00 0.0000000E+00  1015.133     104000\n" +
                        "  -509.1169     -16757.54     0.0000000E+00 0.0000000E+00  284.3770     014000\n" +
                        "  0.0000000E+00 0.0000000E+00  74.32562     -1368.699     0.0000000E+00 005000\n" +
                        "  0.0000000E+00 0.0000000E+00  250.2231     -35597.31     0.0000000E+00 400100\n" +
                        "  0.0000000E+00 0.0000000E+00  7612.044     -183362.2     0.0000000E+00 310100\n" +
                        "  0.0000000E+00 0.0000000E+00  23297.41     -408403.2     0.0000000E+00 220100\n" +
                        "  0.0000000E+00 0.0000000E+00  20583.96     -473223.7     0.0000000E+00 130100\n" +
                        "  0.0000000E+00 0.0000000E+00  4238.287     -218065.4     0.0000000E+00 040100\n" +
                        "  -16961.49     -734.6840     0.0000000E+00 0.0000000E+00 -4114.194     301100\n" +
                        "  -92249.84     -79680.58     0.0000000E+00 0.0000000E+00 -28631.49     211100\n" +
                        "  -161764.0     -327053.6     0.0000000E+00 0.0000000E+00 -55729.64     121100\n" +
                        "  -94413.11     -284018.6     0.0000000E+00 0.0000000E+00 -34112.05     031100\n" +
                        "  0.0000000E+00 0.0000000E+00 -26222.09     -261162.1     0.0000000E+00 202100\n" +
                        "  0.0000000E+00 0.0000000E+00 -55920.98     -686747.2     0.0000000E+00 112100\n" +
                        "  0.0000000E+00 0.0000000E+00 -23343.12     -444674.3     0.0000000E+00 022100\n" +
                        "  -6799.697     -117049.8     0.0000000E+00 0.0000000E+00  5776.533     103100\n" +
                        "  -11427.80     -167480.9     0.0000000E+00 0.0000000E+00 -717.3557     013100\n" +
                        "  0.0000000E+00 0.0000000E+00  740.5829      9.950491     0.0000000E+00 004100\n" +
                        "   361.2148      2473.482     0.0000000E+00 0.0000000E+00  187.6479     400001\n" +
                        "   1636.657      21080.04     0.0000000E+00 0.0000000E+00  993.5748     310001\n" +
                        "   3521.951      50265.16     0.0000000E+00 0.0000000E+00  1783.796     220001\n" +
                        "   4747.529      53468.64     0.0000000E+00 0.0000000E+00  1694.589     130001\n" +
                        "   2886.626      26518.78     0.0000000E+00 0.0000000E+00  937.3394     040001\n" +
                        "  0.0000000E+00 0.0000000E+00 -867.8309      201.5877     0.0000000E+00 301001\n" +
                        "  0.0000000E+00 0.0000000E+00 -2847.082      19243.58     0.0000000E+00 211001\n" +
                        "  0.0000000E+00 0.0000000E+00 -3148.229      57997.20     0.0000000E+00 121001\n" +
                        "  0.0000000E+00 0.0000000E+00 -3003.612      34191.35     0.0000000E+00 031001\n" +
                        "   1451.404      4698.435     0.0000000E+00 0.0000000E+00  819.6005     202001\n" +
                        "   6213.344      23514.16     0.0000000E+00 0.0000000E+00  2641.557     112001\n" +
                        "   7482.333      16426.27     0.0000000E+00 0.0000000E+00  2228.368     022001\n" +
                        "  0.0000000E+00 0.0000000E+00 -792.5174     -8697.493     0.0000000E+00 103001\n" +
                        "  0.0000000E+00 0.0000000E+00 -614.7781     -13197.33     0.0000000E+00 013001\n" +
                        "  -108.2901     -1700.755     0.0000000E+00 0.0000000E+00  248.3081     004001\n" +
                        "  -15883.63     -9015.278     0.0000000E+00 0.0000000E+00 -4129.222     300200\n" +
                        "  -84426.20     -94575.93     0.0000000E+00 0.0000000E+00 -27102.92     210200\n" +
                        "  -147268.2     -307810.4     0.0000000E+00 0.0000000E+00 -51535.59     120200\n" +
                        "  -86782.57     -257020.1     0.0000000E+00 0.0000000E+00 -31457.41     030200\n" +
                        "  0.0000000E+00 0.0000000E+00 -48127.96     -540087.0     0.0000000E+00 201200\n" +
                        "  0.0000000E+00 0.0000000E+00 -108480.3     -1480107.     0.0000000E+00 111200\n" +
                        "  0.0000000E+00 0.0000000E+00 -48624.58     -975557.2     0.0000000E+00 021200\n" +
                        "  -29882.58     -398531.0     0.0000000E+00 0.0000000E+00  11368.51     102200\n" +
                        "  -51773.32     -591174.1     0.0000000E+00 0.0000000E+00 -10103.87     012200\n" +
                        "  0.0000000E+00 0.0000000E+00  3540.341      45366.34     0.0000000E+00 003200\n" +
                        "  0.0000000E+00 0.0000000E+00 -2036.895     -483.6797     0.0000000E+00 300101\n" +
                        "  0.0000000E+00 0.0000000E+00 -7273.968      37634.89     0.0000000E+00 210101\n" +
                        "  0.0000000E+00 0.0000000E+00 -8483.899      122472.7     0.0000000E+00 120101\n" +
                        "  0.0000000E+00 0.0000000E+00 -6468.594      77185.11     0.0000000E+00 030101\n" +
                        "   6947.327      13697.57     0.0000000E+00 0.0000000E+00  3644.407     201101\n" +
                        "   29455.51      79699.48     0.0000000E+00 0.0000000E+00  11786.17     111101\n" +
                        "   34145.96      64109.48     0.0000000E+00 0.0000000E+00  10255.61     021101\n" +
                        "  0.0000000E+00 0.0000000E+00 -3381.681     -42485.19     0.0000000E+00 102101\n" +
                        "  0.0000000E+00 0.0000000E+00 -1158.377     -51493.69     0.0000000E+00 012101\n" +
                        "  -848.1942     -9600.715     0.0000000E+00 0.0000000E+00  2022.895     003101\n" +
                        "   87.11454     -489.6208     0.0000000E+00 0.0000000E+00  39.49885     300002\n" +
                        "  -113.2351      171.7271     0.0000000E+00 0.0000000E+00  158.0411     210002\n" +
                        "  -1134.376     -2603.151     0.0000000E+00 0.0000000E+00 -91.23818     120002\n" +
                        "  -1133.737     -6650.722     0.0000000E+00 0.0000000E+00 -379.2329     030002\n" +
                        "  0.0000000E+00 0.0000000E+00 -68.87781     -1272.726     0.0000000E+00 201002\n" +
                        "  0.0000000E+00 0.0000000E+00  217.9569     -5115.864     0.0000000E+00 111002\n" +
                        "  0.0000000E+00 0.0000000E+00  1283.757     -591.6321     0.0000000E+00 021002\n" +
                        "   63.47629     -1049.320     0.0000000E+00 0.0000000E+00  59.15595     102002\n" +
                        "  -315.6323      583.8799     0.0000000E+00 0.0000000E+00  101.1400     012002\n" +
                        "  0.0000000E+00 0.0000000E+00  69.91970      627.8880     0.0000000E+00 003002\n" +
                        "  0.0000000E+00 0.0000000E+00 -29143.17     -362163.7     0.0000000E+00 200300\n" +
                        "  0.0000000E+00 0.0000000E+00 -69432.12     -1028287.     0.0000000E+00 110300\n" +
                        "  0.0000000E+00 0.0000000E+00 -33410.13     -691461.3     0.0000000E+00 020300\n" +
                        "  -50248.76     -580965.6     0.0000000E+00 0.0000000E+00  8680.622     101300\n" +
                        "  -88505.33     -886127.4     0.0000000E+00 0.0000000E+00 -22587.44     011300\n" +
                        "  0.0000000E+00 0.0000000E+00  8294.048      160792.3     0.0000000E+00 002300\n" +
                        "   7867.606      9188.018     0.0000000E+00 0.0000000E+00  3865.564     200201\n" +
                        "   33237.61      71761.82     0.0000000E+00 0.0000000E+00  12527.62     110201\n" +
                        "   37554.89      68257.93     0.0000000E+00 0.0000000E+00  11236.26     020201\n" +
                        "  0.0000000E+00 0.0000000E+00 -3397.297     -49927.47     0.0000000E+00 101201\n" +
                        "  0.0000000E+00 0.0000000E+00  2585.951     -32655.80     0.0000000E+00 011201\n" +
                        "  -1556.838     -15734.96     0.0000000E+00 0.0000000E+00  6169.877     002201\n" +
                        "  0.0000000E+00 0.0000000E+00 -162.8936     -4627.570     0.0000000E+00 200102\n" +
                        "  0.0000000E+00 0.0000000E+00  433.3873     -16237.40     0.0000000E+00 110102\n" +
                        "  0.0000000E+00 0.0000000E+00  2731.179     -4431.135     0.0000000E+00 020102\n" +
                        "  -357.1855     -3986.887     0.0000000E+00 0.0000000E+00  237.6381     101102\n" +
                        "  -2831.733      2151.115     0.0000000E+00 0.0000000E+00  167.7330     011102\n" +
                        "  0.0000000E+00 0.0000000E+00  5.564964      1583.614     0.0000000E+00 002102\n" +
                        "   46.99684     -164.9570     0.0000000E+00 0.0000000E+00  6.705499     200003\n" +
                        "   160.6494      887.3705     0.0000000E+00 0.0000000E+00  80.94301     110003\n" +
                        "   162.9183      1824.123     0.0000000E+00 0.0000000E+00  118.9829     020003\n" +
                        "  0.0000000E+00 0.0000000E+00  5.722525      205.4874     0.0000000E+00 101003\n" +
                        "  0.0000000E+00 0.0000000E+00 -142.0678     -499.1418     0.0000000E+00 011003\n" +
                        "   15.51228     -143.2096     0.0000000E+00 0.0000000E+00  1.514107     002003\n" +
                        "  -29663.48     -308437.6     0.0000000E+00 0.0000000E+00  1776.646     100400\n" +
                        "  -52831.29     -480559.9     0.0000000E+00 0.0000000E+00 -15138.40     010400\n" +
                        "  0.0000000E+00 0.0000000E+00  8828.220      210286.6     0.0000000E+00 001400\n" +
                        "  0.0000000E+00 0.0000000E+00  198.5313     -1895.886     0.0000000E+00 100301\n" +
                        "  0.0000000E+00 0.0000000E+00  4630.523      30604.16     0.0000000E+00 010301\n" +
                        "   194.9697      123.4447     0.0000000E+00 0.0000000E+00  8520.135     001301\n" +
                        "  -1095.750     -4439.640     0.0000000E+00 0.0000000E+00  132.0607     100202\n" +
                        "  -4537.917      1345.507     0.0000000E+00 0.0000000E+00 -245.6423     010202\n" +
                        "  0.0000000E+00 0.0000000E+00 -758.9953     -706.0734     0.0000000E+00 001202\n" +
                        "  0.0000000E+00 0.0000000E+00 -71.95437      10.89936     0.0000000E+00 100103\n" +
                        "  0.0000000E+00 0.0000000E+00 -505.4686     -1944.027     0.0000000E+00 010103\n" +
                        "   90.17519     -517.2784     0.0000000E+00 0.0000000E+00  27.83152     001103\n" +
                        "   2.220667     -88.00557     0.0000000E+00 0.0000000E+00 -3.221525     100004\n" +
                        "  -6.775278     -112.6988     0.0000000E+00 0.0000000E+00 -6.538780     010004\n" +
                        "  0.0000000E+00 0.0000000E+00  9.099102      50.86217     0.0000000E+00 001004\n" +
                        "  0.0000000E+00 0.0000000E+00  3272.802      95714.14     0.0000000E+00 000500\n" +
                        "   1675.946      13216.74     0.0000000E+00 0.0000000E+00  4543.661     000401\n" +
                        "  0.0000000E+00 0.0000000E+00 -974.1089     -3087.568     0.0000000E+00 000302\n" +
                        "   157.6069     -540.1028     0.0000000E+00 0.0000000E+00  52.04482     000203\n" +
                        "  0.0000000E+00 0.0000000E+00  21.04913      106.1641     0.0000000E+00 000104\n" +
                        "  0.8536353     -2.706101     0.0000000E+00 0.0000000E+00 0.3765722     000005"
        )));

        list.add(BaseUtils.Content.BiContent.create(keyDistance().get(6), CosyArbitraryOrder.readMap(
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
                        " -0.1623417     0.1967531     0.0000000E+00 0.0000000E+00-0.4449078     000002\n" +
                        "  -30.86519      7.300043     0.0000000E+00 0.0000000E+00 0.7853691E-01 300000\n" +
                        "  -185.3518     -225.3018     0.0000000E+00 0.0000000E+00 -23.74875     210000\n" +
                        "  -321.3569     -663.2690     0.0000000E+00 0.0000000E+00 -61.08503     120000\n" +
                        "  -173.6047     -412.4301     0.0000000E+00 0.0000000E+00 -38.51008     030000\n" +
                        "  0.0000000E+00 0.0000000E+00  23.03872     -157.5806     0.0000000E+00 201000\n" +
                        "  0.0000000E+00 0.0000000E+00  168.9754     -94.38903     0.0000000E+00 111000\n" +
                        "  0.0000000E+00 0.0000000E+00  136.7590      11.64060     0.0000000E+00 021000\n" +
                        "  -45.32622      322.8443     0.0000000E+00 0.0000000E+00  10.49585     102000\n" +
                        "  -125.9468      475.2790     0.0000000E+00 0.0000000E+00  5.879523     012000\n" +
                        "  0.0000000E+00 0.0000000E+00 -134.3395     -351.3330     0.0000000E+00 003000\n" +
                        "  0.0000000E+00 0.0000000E+00  2.669247     -414.9696     0.0000000E+00 200100\n" +
                        "  0.0000000E+00 0.0000000E+00  265.5620     -334.2735     0.0000000E+00 110100\n" +
                        "  0.0000000E+00 0.0000000E+00  290.8643      56.21643     0.0000000E+00 020100\n" +
                        "  -206.1845      1085.156     0.0000000E+00 0.0000000E+00 -1.967640     101100\n" +
                        "  -515.1142      1728.352     0.0000000E+00 0.0000000E+00 -30.24328     011100\n" +
                        "  0.0000000E+00 0.0000000E+00 -717.3832     -1912.223     0.0000000E+00 002100\n" +
                        "   16.97188      69.16702     0.0000000E+00 0.0000000E+00  6.475639     200001\n" +
                        "   51.13747      210.2683     0.0000000E+00 0.0000000E+00  18.01996     110001\n" +
                        "   42.74106      77.17095     0.0000000E+00 0.0000000E+00  13.05645     020001\n" +
                        "  0.0000000E+00 0.0000000E+00 -32.96670     -94.07807     0.0000000E+00 101001\n" +
                        "  0.0000000E+00 0.0000000E+00 -58.13871     -140.5994     0.0000000E+00 011001\n" +
                        "   6.210734      11.46408     0.0000000E+00 0.0000000E+00  2.704527     002001\n" +
                        "  -221.8268      885.6549     0.0000000E+00 0.0000000E+00 -35.40581     100200\n" +
                        "  -523.8509      1465.428     0.0000000E+00 0.0000000E+00 -61.67656     010200\n" +
                        "  0.0000000E+00 0.0000000E+00 -1305.565     -3614.259     0.0000000E+00 001200\n" +
                        "  0.0000000E+00 0.0000000E+00 -102.4390     -263.9953     0.0000000E+00 100101\n" +
                        "  0.0000000E+00 0.0000000E+00 -148.6071     -322.7636     0.0000000E+00 010101\n" +
                        "   28.18460      36.06396     0.0000000E+00 0.0000000E+00  7.556213     001101\n" +
                        "   1.110499      2.884559     0.0000000E+00 0.0000000E+00 0.1920571     100002\n" +
                        " -0.8104067      28.39041     0.0000000E+00 0.0000000E+00 0.2658274     010002\n" +
                        "  0.0000000E+00 0.0000000E+00 -1.663832     -3.382246     0.0000000E+00 001002\n" +
                        "  0.0000000E+00 0.0000000E+00 -809.4035     -2375.144     0.0000000E+00 000300\n" +
                        "   38.47779      3.940980     0.0000000E+00 0.0000000E+00  5.534413     000201\n" +
                        "  0.0000000E+00 0.0000000E+00 -6.566017     -17.99717     0.0000000E+00 000102\n" +
                        "  0.2041566     -1.457267     0.0000000E+00 0.0000000E+00 0.3889265     000003\n" +
                        "  -255.7010     -1133.019     0.0000000E+00 0.0000000E+00 -70.95078     400000\n" +
                        "  -1322.281     -6175.573     0.0000000E+00 0.0000000E+00 -260.1337     310000\n" +
                        "  -2876.288     -13176.52     0.0000000E+00 0.0000000E+00 -417.6566     220000\n" +
                        "  -3202.621     -14399.37     0.0000000E+00 0.0000000E+00 -463.5780     130000\n" +
                        "  -1469.730     -6778.789     0.0000000E+00 0.0000000E+00 -262.6624     040000\n" +
                        "  0.0000000E+00 0.0000000E+00  154.0926     -933.9430     0.0000000E+00 301000\n" +
                        "  0.0000000E+00 0.0000000E+00  160.3574     -6541.547     0.0000000E+00 211000\n" +
                        "  0.0000000E+00 0.0000000E+00  779.5276     -9286.620     0.0000000E+00 121000\n" +
                        "  0.0000000E+00 0.0000000E+00  1481.464     -2166.908     0.0000000E+00 031000\n" +
                        "  -803.9086     -306.3971     0.0000000E+00 0.0000000E+00 -427.9983     202000\n" +
                        "  -2665.859      132.0771     0.0000000E+00 0.0000000E+00 -1031.442     112000\n" +
                        "  -2317.821      2235.360     0.0000000E+00 0.0000000E+00 -670.4268     022000\n" +
                        "  0.0000000E+00 0.0000000E+00 -267.3259      55.57403     0.0000000E+00 103000\n" +
                        "  0.0000000E+00 0.0000000E+00 -1479.058     -3607.166     0.0000000E+00 013000\n" +
                        "  -52.17794     -727.7536     0.0000000E+00 0.0000000E+00 -73.16201     004000\n" +
                        "  0.0000000E+00 0.0000000E+00  411.7407     -1345.376     0.0000000E+00 300100\n" +
                        "  0.0000000E+00 0.0000000E+00  589.7249     -11692.07     0.0000000E+00 210100\n" +
                        "  0.0000000E+00 0.0000000E+00  1238.805     -18539.51     0.0000000E+00 120100\n" +
                        "  0.0000000E+00 0.0000000E+00  2238.563     -5439.752     0.0000000E+00 030100\n" +
                        "  -2930.758     -968.3203     0.0000000E+00 0.0000000E+00 -1460.080     201100\n" +
                        "  -10352.21     -2966.622     0.0000000E+00 0.0000000E+00 -3676.669     111100\n" +
                        "  -9247.305      2423.381     0.0000000E+00 0.0000000E+00 -2480.131     021100\n" +
                        "  0.0000000E+00 0.0000000E+00 -2014.717     -3679.506     0.0000000E+00 102100\n" +
                        "  0.0000000E+00 0.0000000E+00 -8444.841     -24825.83     0.0000000E+00 012100\n" +
                        "  -560.9499     -5492.024     0.0000000E+00 0.0000000E+00 -371.5233     003100\n" +
                        "  -12.48253     -52.22614     0.0000000E+00 0.0000000E+00 -39.17493     300001\n" +
                        "   280.8684      268.5470     0.0000000E+00 0.0000000E+00 -52.87283     210001\n" +
                        "   1035.214      2670.176     0.0000000E+00 0.0000000E+00  154.0861     120001\n" +
                        "   831.3900      2827.302     0.0000000E+00 0.0000000E+00  197.3489     030001\n" +
                        "  0.0000000E+00 0.0000000E+00  30.51093      422.7181     0.0000000E+00 201001\n" +
                        "  0.0000000E+00 0.0000000E+00 -514.4570      77.43150     0.0000000E+00 111001\n" +
                        "  0.0000000E+00 0.0000000E+00 -974.6185     -1336.560     0.0000000E+00 021001\n" +
                        "  -9.222481     -222.5193     0.0000000E+00 0.0000000E+00 -89.68621     102001\n" +
                        "   105.1697     -1223.507     0.0000000E+00 0.0000000E+00 -99.36862     012001\n" +
                        "  0.0000000E+00 0.0000000E+00  139.3474      325.4952     0.0000000E+00 003001\n" +
                        "  -2687.714     -823.5681     0.0000000E+00 0.0000000E+00 -1186.623     200200\n" +
                        "  -9987.824     -5540.114     0.0000000E+00 0.0000000E+00 -3221.195     110200\n" +
                        "  -9002.162     -1905.836     0.0000000E+00 0.0000000E+00 -2377.339     020200\n" +
                        "  0.0000000E+00 0.0000000E+00 -4959.682     -15374.29     0.0000000E+00 101200\n" +
                        "  0.0000000E+00 0.0000000E+00 -16215.18     -55415.77     0.0000000E+00 011200\n" +
                        "  -2243.592     -16393.31     0.0000000E+00 0.0000000E+00 -653.4071     002200\n" +
                        "  0.0000000E+00 0.0000000E+00  208.3190      1605.540     0.0000000E+00 200101\n" +
                        "  0.0000000E+00 0.0000000E+00 -649.8235      1848.124     0.0000000E+00 110101\n" +
                        "  0.0000000E+00 0.0000000E+00 -1771.776     -1912.913     0.0000000E+00 020101\n" +
                        "   298.3431     -741.5536     0.0000000E+00 0.0000000E+00 -219.6147     101101\n" +
                        "   991.3160     -4470.846     0.0000000E+00 0.0000000E+00 -148.8122     011101\n" +
                        "  0.0000000E+00 0.0000000E+00  1099.171      2621.568     0.0000000E+00 002101\n" +
                        "  -42.82071     -84.24761     0.0000000E+00 0.0000000E+00 -17.61649     200002\n" +
                        "  -178.0746     -704.4853     0.0000000E+00 0.0000000E+00 -70.27753     110002\n" +
                        "  -160.0658     -661.6199     0.0000000E+00 0.0000000E+00 -60.57122     020002\n" +
                        "  0.0000000E+00 0.0000000E+00  51.32597      110.7021     0.0000000E+00 101002\n" +
                        "  0.0000000E+00 0.0000000E+00  161.7540      407.3599     0.0000000E+00 011002\n" +
                        "  -2.337549      63.32070     0.0000000E+00 0.0000000E+00 -6.483772     002002\n" +
                        "  0.0000000E+00 0.0000000E+00 -3909.552     -15215.75     0.0000000E+00 100300\n" +
                        "  0.0000000E+00 0.0000000E+00 -10406.10     -39887.12     0.0000000E+00 010300\n" +
                        "  -3850.722     -22891.28     0.0000000E+00 0.0000000E+00 -424.4688     001300\n" +
                        "   594.6950     -561.2546     0.0000000E+00 0.0000000E+00 -37.61624     100201\n" +
                        "   1476.801     -4079.968     0.0000000E+00 0.0000000E+00  125.7189     010201\n" +
                        "  0.0000000E+00 0.0000000E+00  2604.159      6329.126     0.0000000E+00 001201\n" +
                        "  0.0000000E+00 0.0000000E+00  196.5819      473.6588     0.0000000E+00 100102\n" +
                        "  0.0000000E+00 0.0000000E+00  464.4058      1140.394     0.0000000E+00 010102\n" +
                        "  -23.42272      205.1849     0.0000000E+00 0.0000000E+00 -26.73899     001102\n" +
                        "   1.396750      29.52537     0.0000000E+00 0.0000000E+00 0.8524094     100003\n" +
                        "   1.825837     -1.639701     0.0000000E+00 0.0000000E+00 0.6729949     010003\n" +
                        "  0.0000000E+00 0.0000000E+00 -4.766660     -16.64061     0.0000000E+00 001003\n" +
                        "  -2399.921     -12440.57     0.0000000E+00 0.0000000E+00 -46.85158     000400\n" +
                        "  0.0000000E+00 0.0000000E+00  1986.450      5020.942     0.0000000E+00 000301\n" +
                        "  -50.05642      212.4627     0.0000000E+00 0.0000000E+00 -24.53233     000202\n" +
                        "  0.0000000E+00 0.0000000E+00 -4.202496     -16.14320     0.0000000E+00 000103\n" +
                        " -0.9608623E-01  2.806015     0.0000000E+00 0.0000000E+00-0.3274405     000004\n" +
                        "  -111.5840     -1115.591     0.0000000E+00 0.0000000E+00  255.6500     500000\n" +
                        "  -4008.292     -14995.46     0.0000000E+00 0.0000000E+00  549.3537     410000\n" +
                        "  -18430.54     -67060.74     0.0000000E+00 0.0000000E+00 -501.1105     320000\n" +
                        "  -32956.35     -124531.3     0.0000000E+00 0.0000000E+00 -1135.667     230000\n" +
                        "  -27059.32     -102691.5     0.0000000E+00 0.0000000E+00  238.1176     140000\n" +
                        "  -9213.220     -32752.23     0.0000000E+00 0.0000000E+00  442.0179     050000\n" +
                        "  0.0000000E+00 0.0000000E+00 -2539.120     -17348.44     0.0000000E+00 401000\n" +
                        "  0.0000000E+00 0.0000000E+00 -8349.363     -90363.69     0.0000000E+00 311000\n" +
                        "  0.0000000E+00 0.0000000E+00 -14349.99     -206888.0     0.0000000E+00 221000\n" +
                        "  0.0000000E+00 0.0000000E+00 -20778.21     -243645.8     0.0000000E+00 131000\n" +
                        "  0.0000000E+00 0.0000000E+00 -12636.70     -111097.8     0.0000000E+00 041000\n" +
                        "  -3236.522      2797.355     0.0000000E+00 0.0000000E+00  172.0677     302000\n" +
                        "  -25000.01     -12724.38     0.0000000E+00 0.0000000E+00 -5620.001     212000\n" +
                        "  -55629.93     -83933.38     0.0000000E+00 0.0000000E+00 -15622.86     122000\n" +
                        "  -37348.71     -78678.37     0.0000000E+00 0.0000000E+00 -10447.84     032000\n" +
                        "  0.0000000E+00 0.0000000E+00 -11139.00     -40437.21     0.0000000E+00 203000\n" +
                        "  0.0000000E+00 0.0000000E+00 -25253.92     -100809.5     0.0000000E+00 113000\n" +
                        "  0.0000000E+00 0.0000000E+00 -13615.32     -64036.55     0.0000000E+00 023000\n" +
                        "  -2105.894     -12276.31     0.0000000E+00 0.0000000E+00  2878.666     104000\n" +
                        "  -2969.707     -16757.54     0.0000000E+00 0.0000000E+00  2390.123     014000\n" +
                        "  0.0000000E+00 0.0000000E+00 -133.9095     -1368.699     0.0000000E+00 005000\n" +
                        "  0.0000000E+00 0.0000000E+00 -5569.034     -35597.31     0.0000000E+00 400100\n" +
                        "  0.0000000E+00 0.0000000E+00 -21165.03     -183362.2     0.0000000E+00 310100\n" +
                        "  0.0000000E+00 0.0000000E+00 -39124.15     -408403.2     0.0000000E+00 220100\n" +
                        "  0.0000000E+00 0.0000000E+00 -50742.91     -473223.7     0.0000000E+00 130100\n" +
                        "  0.0000000E+00 0.0000000E+00 -28402.02     -218065.4     0.0000000E+00 040100\n" +
                        "  -15015.07     -734.6840     0.0000000E+00 0.0000000E+00  567.1013     301100\n" +
                        "  -98751.70     -79680.58     0.0000000E+00 0.0000000E+00 -18431.79     211100\n" +
                        "  -206325.7     -327053.6     0.0000000E+00 0.0000000E+00 -52796.38     121100\n" +
                        "  -135636.7     -284018.6     0.0000000E+00 0.0000000E+00 -37397.28     031100\n" +
                        "  0.0000000E+00 0.0000000E+00 -65083.29     -261162.1     0.0000000E+00 202100\n" +
                        "  0.0000000E+00 0.0000000E+00 -157294.4     -686747.2     0.0000000E+00 112100\n" +
                        "  0.0000000E+00 0.0000000E+00 -88873.97     -444674.3     0.0000000E+00 022100\n" +
                        "  -24541.88     -117049.8     0.0000000E+00 0.0000000E+00  18229.71     103100\n" +
                        "  -36767.96     -167480.9     0.0000000E+00 0.0000000E+00  13509.80     013100\n" +
                        "  0.0000000E+00 0.0000000E+00  809.3880      9.950491     0.0000000E+00 004100\n" +
                        "   1099.587      2473.482     0.0000000E+00 0.0000000E+00  375.8914     400001\n" +
                        "   6110.276      21080.04     0.0000000E+00 0.0000000E+00  1371.742     310001\n" +
                        "   12579.61      50265.16     0.0000000E+00 0.0000000E+00  899.5872     220001\n" +
                        "   13668.56      53468.64     0.0000000E+00 0.0000000E+00 -337.4698     130001\n" +
                        "   7264.947      26518.78     0.0000000E+00 0.0000000E+00  117.1891     040001\n" +
                        "  0.0000000E+00 0.0000000E+00 -1048.075      201.5877     0.0000000E+00 301001\n" +
                        "  0.0000000E+00 0.0000000E+00 -185.9599      19243.58     0.0000000E+00 211001\n" +
                        "  0.0000000E+00 0.0000000E+00  5733.034      57997.20     0.0000000E+00 121001\n" +
                        "  0.0000000E+00 0.0000000E+00  2191.975      34191.35     0.0000000E+00 031001\n" +
                        "   2574.477      4698.435     0.0000000E+00 0.0000000E+00  1933.355     202001\n" +
                        "   10482.76      23514.16     0.0000000E+00 0.0000000E+00  5474.165     112001\n" +
                        "   10055.38      16426.27     0.0000000E+00 0.0000000E+00  3861.245     022001\n" +
                        "  0.0000000E+00 0.0000000E+00 -2201.469     -8697.493     0.0000000E+00 103001\n" +
                        "  0.0000000E+00 0.0000000E+00 -2378.095     -13197.33     0.0000000E+00 013001\n" +
                        "  -291.7340     -1700.755     0.0000000E+00 0.0000000E+00  484.4185     004001\n" +
                        "  -16488.27     -9015.278     0.0000000E+00 0.0000000E+00  179.2175     300200\n" +
                        "  -95860.53     -94575.93     0.0000000E+00 0.0000000E+00 -15882.08     210200\n" +
                        "  -190326.9     -307810.4     0.0000000E+00 0.0000000E+00 -44413.59     120200\n" +
                        "  -124199.6     -257020.1     0.0000000E+00 0.0000000E+00 -32308.26     030200\n" +
                        "  0.0000000E+00 0.0000000E+00 -125781.7     -540087.0     0.0000000E+00 201200\n" +
                        "  0.0000000E+00 0.0000000E+00 -322263.5     -1480107.     0.0000000E+00 111200\n" +
                        "  0.0000000E+00 0.0000000E+00 -190778.9     -975557.2     0.0000000E+00 021200\n" +
                        "  -91444.46     -398531.0     0.0000000E+00 0.0000000E+00  44040.02     102200\n" +
                        "  -141676.6     -591174.1     0.0000000E+00 0.0000000E+00  27446.59     012200\n" +
                        "  0.0000000E+00 0.0000000E+00  10206.85      45366.34     0.0000000E+00 003200\n" +
                        "  0.0000000E+00 0.0000000E+00 -2348.043     -483.6797     0.0000000E+00 300101\n" +
                        "  0.0000000E+00 0.0000000E+00 -1464.539      37634.89     0.0000000E+00 210101\n" +
                        "  0.0000000E+00 0.0000000E+00  10754.69      122472.7     0.0000000E+00 120101\n" +
                        "  0.0000000E+00 0.0000000E+00  5301.194      77185.11     0.0000000E+00 030101\n" +
                        "   9777.921      13697.57     0.0000000E+00 0.0000000E+00  7499.364     201101\n" +
                        "   42833.62      79699.48     0.0000000E+00 0.0000000E+00  21574.42     111101\n" +
                        "   43922.94      64109.48     0.0000000E+00 0.0000000E+00  16162.22     021101\n" +
                        "  0.0000000E+00 0.0000000E+00 -9353.182     -42485.19     0.0000000E+00 102101\n" +
                        "  0.0000000E+00 0.0000000E+00 -6519.066     -51493.69     0.0000000E+00 012101\n" +
                        "  -1851.129     -9600.715     0.0000000E+00 0.0000000E+00  3533.060     003101\n" +
                        "   118.2623     -489.6208     0.0000000E+00 0.0000000E+00  161.7908     300002\n" +
                        "   223.5530      171.7271     0.0000000E+00 0.0000000E+00  714.1611     210002\n" +
                        "  -1484.070     -2603.151     0.0000000E+00 0.0000000E+00  455.4949     120002\n" +
                        "  -2341.153     -6650.722     0.0000000E+00 0.0000000E+00 -346.5989     030002\n" +
                        "  0.0000000E+00 0.0000000E+00 -365.9985     -1272.726     0.0000000E+00 201002\n" +
                        "  0.0000000E+00 0.0000000E+00 -696.9629     -5115.864     0.0000000E+00 111002\n" +
                        "  0.0000000E+00 0.0000000E+00  1242.524     -591.6321     0.0000000E+00 021002\n" +
                        "  -10.15763     -1049.320     0.0000000E+00 0.0000000E+00  198.0590     102002\n" +
                        "  -41.92011      583.8799     0.0000000E+00 0.0000000E+00  342.8189     012002\n" +
                        "  0.0000000E+00 0.0000000E+00  108.8369      627.8880     0.0000000E+00 003002\n" +
                        "  0.0000000E+00 0.0000000E+00 -80800.82     -362163.7     0.0000000E+00 200300\n" +
                        "  0.0000000E+00 0.0000000E+00 -217657.3     -1028287.     0.0000000E+00 110300\n" +
                        "  0.0000000E+00 0.0000000E+00 -134052.8     -691461.3     0.0000000E+00 020300\n" +
                        "  -140192.7     -580965.6     0.0000000E+00 0.0000000E+00  48940.46     101300\n" +
                        "  -222632.8     -886127.4     0.0000000E+00 0.0000000E+00  23334.42     011300\n" +
                        "  0.0000000E+00 0.0000000E+00  30238.25      160792.3     0.0000000E+00 002300\n" +
                        "   9222.211      9188.018     0.0000000E+00 0.0000000E+00  6783.236     200201\n" +
                        "   44256.66      71761.82     0.0000000E+00 0.0000000E+00  19992.71     110201\n" +
                        "   47879.68      68257.93     0.0000000E+00 0.0000000E+00  16192.32     020201\n" +
                        "  0.0000000E+00 0.0000000E+00 -8746.455     -49927.47     0.0000000E+00 101201\n" +
                        "  0.0000000E+00 0.0000000E+00  3401.289     -32655.80     0.0000000E+00 011201\n" +
                        "  -2793.153     -15734.96     0.0000000E+00 0.0000000E+00  9633.471     002201\n" +
                        "  0.0000000E+00 0.0000000E+00 -1092.379     -4627.570     0.0000000E+00 200102\n" +
                        "  0.0000000E+00 0.0000000E+00 -2273.722     -16237.40     0.0000000E+00 110102\n" +
                        "  0.0000000E+00 0.0000000E+00  2218.540     -4431.135     0.0000000E+00 020102\n" +
                        "  -745.6166     -3986.887     0.0000000E+00 0.0000000E+00  722.2208     101102\n" +
                        "  -1954.831      2151.115     0.0000000E+00 0.0000000E+00  855.0483     011102\n" +
                        "  0.0000000E+00 0.0000000E+00 -92.30928      1583.614     0.0000000E+00 002102\n" +
                        "   42.65365     -164.9570     0.0000000E+00 0.0000000E+00  14.70321     200003\n" +
                        "   399.1323      887.3705     0.0000000E+00 0.0000000E+00  155.5494     110003\n" +
                        "   518.3605      1824.123     0.0000000E+00 0.0000000E+00  197.8949     020003\n" +
                        "  0.0000000E+00 0.0000000E+00  15.15386      205.4874     0.0000000E+00 101003\n" +
                        "  0.0000000E+00 0.0000000E+00 -268.3267     -499.1418     0.0000000E+00 011003\n" +
                        "  -8.873530     -143.2096     0.0000000E+00 0.0000000E+00  3.637648     002003\n" +
                        "  -77153.08     -308437.6     0.0000000E+00 0.0000000E+00  21414.88     100400\n" +
                        "  -125088.5     -480559.9     0.0000000E+00 0.0000000E+00  6709.134     010400\n" +
                        "  0.0000000E+00 0.0000000E+00  35400.95      210286.6     0.0000000E+00 001400\n" +
                        "  0.0000000E+00 0.0000000E+00  1637.571     -1895.886     0.0000000E+00 100301\n" +
                        "  0.0000000E+00 0.0000000E+00  12926.94      30604.16     0.0000000E+00 010301\n" +
                        "   1707.825      123.4447     0.0000000E+00 0.0000000E+00  11818.71     001301\n" +
                        "  -1688.228     -4439.640     0.0000000E+00 0.0000000E+00  415.1289     100202\n" +
                        "  -3951.368      1345.507     0.0000000E+00 0.0000000E+00  6.869279     010202\n" +
                        "  0.0000000E+00 0.0000000E+00 -1563.895     -706.0734     0.0000000E+00 001202\n" +
                        "  0.0000000E+00 0.0000000E+00 -138.6457      10.89936     0.0000000E+00 100103\n" +
                        "  0.0000000E+00 0.0000000E+00 -923.4421     -1944.027     0.0000000E+00 010103\n" +
                        "   1.907330     -517.2784     0.0000000E+00 0.0000000E+00  45.78994     001103\n" +
                        "  -12.99604     -88.00557     0.0000000E+00 0.0000000E+00 -5.590747     100004\n" +
                        "  -20.61169     -112.6988     0.0000000E+00 0.0000000E+00 -8.100482     010004\n" +
                        "  0.0000000E+00 0.0000000E+00  17.56000      50.86217     0.0000000E+00 001004\n" +
                        "  0.0000000E+00 0.0000000E+00  14148.32      95714.14     0.0000000E+00 000500\n" +
                        "   4484.666      13216.74     0.0000000E+00 0.0000000E+00  5543.945     000401\n" +
                        "  0.0000000E+00 0.0000000E+00 -1974.054     -3087.568     0.0000000E+00 000302\n" +
                        "   58.38652     -540.1028     0.0000000E+00 0.0000000E+00  70.63545     000203\n" +
                        "  0.0000000E+00 0.0000000E+00  36.41687      106.1641     0.0000000E+00 000104\n" +
                        "  0.1009167     -2.706101     0.0000000E+00 0.0000000E+00 0.3053216     000005"
        )));

        list.add(BaseUtils.Content.BiContent.create(keyDistance().get(7), CosyArbitraryOrder.readMap(
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
                        " -0.2176690     0.2299405     0.0000000E+00 0.0000000E+00-0.5364178     000002\n" +
                        "  -64.65172      60.53023     0.0000000E+00 0.0000000E+00 -9.176049     300000\n" +
                        "  -431.6029      136.2644     0.0000000E+00 0.0000000E+00 -96.40622     210000\n" +
                        "  -802.4168     0.4719218     0.0000000E+00 0.0000000E+00 -197.2146     120000\n" +
                        "  -442.6791     -62.17062     0.0000000E+00 0.0000000E+00 -111.4429     030000\n" +
                        "  0.0000000E+00 0.0000000E+00 -62.10333     -346.0352     0.0000000E+00 201000\n" +
                        "  0.0000000E+00 0.0000000E+00 -45.66775     -756.7844     0.0000000E+00 111000\n" +
                        "  0.0000000E+00 0.0000000E+00 -3.794667     -462.2035     0.0000000E+00 021000\n" +
                        "   31.01167      226.0838     0.0000000E+00 0.0000000E+00  7.220227     102000\n" +
                        "  -50.86616      360.9274     0.0000000E+00 0.0000000E+00 -18.52035     012000\n" +
                        "  0.0000000E+00 0.0000000E+00 -133.0934     -112.7277     0.0000000E+00 003000\n" +
                        "  0.0000000E+00 0.0000000E+00 -170.9604     -715.4766     0.0000000E+00 200100\n" +
                        "  0.0000000E+00 0.0000000E+00 -163.7052     -1524.238     0.0000000E+00 110100\n" +
                        "  0.0000000E+00 0.0000000E+00-0.2002780     -940.8534     0.0000000E+00 020100\n" +
                        "   48.48544      824.9893     0.0000000E+00 0.0000000E+00 -35.69290     101100\n" +
                        "  -245.0582      1379.006     0.0000000E+00 0.0000000E+00 -141.4301     011100\n" +
                        "  0.0000000E+00 0.0000000E+00 -724.6597     -654.6603     0.0000000E+00 002100\n" +
                        "   51.27954      29.59412     0.0000000E+00 0.0000000E+00  16.18169     200001\n" +
                        "   151.0453      107.1515     0.0000000E+00 0.0000000E+00  43.05174     110001\n" +
                        "   93.71316      38.17042     0.0000000E+00 0.0000000E+00  28.26135     020001\n" +
                        "  0.0000000E+00 0.0000000E+00 -35.12632     -41.35714     0.0000000E+00 101001\n" +
                        "  0.0000000E+00 0.0000000E+00 -53.12252     -33.85452     0.0000000E+00 011001\n" +
                        "   16.29166      2.186293     0.0000000E+00 0.0000000E+00  5.404244     002001\n" +
                        "  -15.68404      731.9230     0.0000000E+00 0.0000000E+00 -86.20422     100200\n" +
                        "  -328.8765      1221.012     0.0000000E+00 0.0000000E+00 -184.9584     010200\n" +
                        "  0.0000000E+00 0.0000000E+00 -1369.111     -1404.328     0.0000000E+00 001200\n" +
                        "  0.0000000E+00 0.0000000E+00 -99.46371     -82.02399     0.0000000E+00 100101\n" +
                        "  0.0000000E+00 0.0000000E+00 -123.0040     -25.54867     0.0000000E+00 010101\n" +
                        "   68.57363      8.045677     0.0000000E+00 0.0000000E+00  18.56566     001101\n" +
                        "   4.141547     -1.673647     0.0000000E+00 0.0000000E+00  1.049219     100002\n" +
                        "   9.212043      13.66011     0.0000000E+00 0.0000000E+00  1.357314     010002\n" +
                        "  0.0000000E+00 0.0000000E+00 -1.414869    -0.1995963E-01 0.0000000E+00 001002\n" +
                        "  0.0000000E+00 0.0000000E+00 -896.4927     -1088.442     0.0000000E+00 000300\n" +
                        "   73.97933     -7.164499     0.0000000E+00 0.0000000E+00  17.86696     000201\n" +
                        "  0.0000000E+00 0.0000000E+00 -7.332181     -7.551937     0.0000000E+00 000102\n" +
                        " -0.9239367E-01 -1.010236     0.0000000E+00 0.0000000E+00 0.4603707     000003\n" +
                        "  -976.2145      605.4623     0.0000000E+00 0.0000000E+00 -314.1625     400000\n" +
                        "  -5267.674      2638.539     0.0000000E+00 0.0000000E+00 -1385.702     310000\n" +
                        "  -11467.35      4988.322     0.0000000E+00 0.0000000E+00 -2623.994     220000\n" +
                        "  -12380.47      4007.226     0.0000000E+00 0.0000000E+00 -2745.812     130000\n" +
                        "  -5487.633      800.1206     0.0000000E+00 0.0000000E+00 -1265.851     040000\n" +
                        "  0.0000000E+00 0.0000000E+00 -554.5618     -2328.422     0.0000000E+00 301000\n" +
                        "  0.0000000E+00 0.0000000E+00 -3239.228     -12505.27     0.0000000E+00 211000\n" +
                        "  0.0000000E+00 0.0000000E+00 -4499.248     -19724.66     0.0000000E+00 121000\n" +
                        "  0.0000000E+00 0.0000000E+00 -1237.136     -9500.775     0.0000000E+00 031000\n" +
                        "  -1083.554      866.4891     0.0000000E+00 0.0000000E+00 -835.7767     202000\n" +
                        "  -3556.305      2829.060     0.0000000E+00 0.0000000E+00 -2123.701     112000\n" +
                        "  -2713.875      3409.813     0.0000000E+00 0.0000000E+00 -1433.286     022000\n" +
                        "  0.0000000E+00 0.0000000E+00  6.351472      1123.594     0.0000000E+00 103000\n" +
                        "  0.0000000E+00 0.0000000E+00 -1346.983     -606.2518     0.0000000E+00 013000\n" +
                        "  -356.9291     -418.9831     0.0000000E+00 0.0000000E+00 -120.3823     004000\n" +
                        "  0.0000000E+00 0.0000000E+00 -940.4839     -4239.492     0.0000000E+00 300100\n" +
                        "  0.0000000E+00 0.0000000E+00 -6081.010     -23941.38     0.0000000E+00 210100\n" +
                        "  0.0000000E+00 0.0000000E+00 -9241.427     -38663.59     0.0000000E+00 120100\n" +
                        "  0.0000000E+00 0.0000000E+00 -3076.684     -18522.58     0.0000000E+00 030100\n" +
                        "  -4127.879      3680.304     0.0000000E+00 0.0000000E+00 -2953.786     201100\n" +
                        "  -15645.00      9712.337     0.0000000E+00 0.0000000E+00 -8055.834     111100\n" +
                        "  -13381.82      10059.23     0.0000000E+00 0.0000000E+00 -5774.909     021100\n" +
                        "  0.0000000E+00 0.0000000E+00 -1457.608      1349.381     0.0000000E+00 102100\n" +
                        "  0.0000000E+00 0.0000000E+00 -9263.900     -10623.12     0.0000000E+00 012100\n" +
                        "  -3169.632     -3260.795     0.0000000E+00 0.0000000E+00 -742.7559     003100\n" +
                        "  -9.496893      167.8126     0.0000000E+00 0.0000000E+00 -97.37561     300001\n" +
                        "   804.5442      37.98719     0.0000000E+00 0.0000000E+00 -46.93642     210001\n" +
                        "   3009.967      267.4729     0.0000000E+00 0.0000000E+00  597.1292     120001\n" +
                        "   2487.538      682.1228     0.0000000E+00 0.0000000E+00  611.3944     030001\n" +
                        "  0.0000000E+00 0.0000000E+00  164.9862      640.7486     0.0000000E+00 201001\n" +
                        "  0.0000000E+00 0.0000000E+00  59.04465      2040.874     0.0000000E+00 111001\n" +
                        "  0.0000000E+00 0.0000000E+00 -473.3798      1248.980     0.0000000E+00 021001\n" +
                        "  -55.46071      70.24134     0.0000000E+00 0.0000000E+00 -135.3666     102001\n" +
                        "  -155.7370     -625.4864     0.0000000E+00 0.0000000E+00 -132.6064     012001\n" +
                        "  0.0000000E+00 0.0000000E+00  124.2456      58.36405     0.0000000E+00 003001\n" +
                        "  -3994.333      3380.367     0.0000000E+00 0.0000000E+00 -2508.839     200200\n" +
                        "  -16557.65      7833.584     0.0000000E+00 0.0000000E+00 -7467.714     110200\n" +
                        "  -14636.27      7679.374     0.0000000E+00 0.0000000E+00 -5785.990     020200\n" +
                        "  0.0000000E+00 0.0000000E+00 -5912.759     -7675.306     0.0000000E+00 101200\n" +
                        "  0.0000000E+00 0.0000000E+00 -20672.53     -33566.34     0.0000000E+00 011200\n" +
                        "  -10815.38     -9954.974     0.0000000E+00 0.0000000E+00 -1841.900     002200\n" +
                        "  0.0000000E+00 0.0000000E+00  654.8853      2058.504     0.0000000E+00 200101\n" +
                        "  0.0000000E+00 0.0000000E+00  884.0104      5664.456     0.0000000E+00 110101\n" +
                        "  0.0000000E+00 0.0000000E+00 -567.3952      3219.423     0.0000000E+00 020101\n" +
                        "   280.2117      5.681175     0.0000000E+00 0.0000000E+00 -242.5575     101101\n" +
                        "   258.1228     -2772.759     0.0000000E+00 0.0000000E+00 -29.77128     011101\n" +
                        "  0.0000000E+00 0.0000000E+00  1007.108      518.5363     0.0000000E+00 002101\n" +
                        "  -120.1762      64.26168     0.0000000E+00 0.0000000E+00 -51.89897     200002\n" +
                        "  -558.3579     -149.5773     0.0000000E+00 0.0000000E+00 -191.1136     110002\n" +
                        "  -481.6201     -270.9768     0.0000000E+00 0.0000000E+00 -148.9862     020002\n" +
                        "  0.0000000E+00 0.0000000E+00  39.88898      4.334830     0.0000000E+00 101002\n" +
                        "  0.0000000E+00 0.0000000E+00  151.0803      115.6646     0.0000000E+00 011002\n" +
                        "   12.77784      57.73805     0.0000000E+00 0.0000000E+00 -8.040228     002002\n" +
                        "  0.0000000E+00 0.0000000E+00 -5810.185     -11436.21     0.0000000E+00 100300\n" +
                        "  0.0000000E+00 0.0000000E+00 -14926.00     -28914.00     0.0000000E+00 010300\n" +
                        "  -16574.81     -14087.63     0.0000000E+00 0.0000000E+00 -2224.233     001300\n" +
                        "   691.6973     -356.5452     0.0000000E+00 0.0000000E+00  108.5426     100201\n" +
                        "   991.6478     -3171.627     0.0000000E+00 0.0000000E+00  491.8203     010201\n" +
                        "  0.0000000E+00 0.0000000E+00  2460.636      1392.488     0.0000000E+00 001201\n" +
                        "  0.0000000E+00 0.0000000E+00  175.8185      104.0501     0.0000000E+00 100102\n" +
                        "  0.0000000E+00 0.0000000E+00  427.1001      285.7451     0.0000000E+00 010102\n" +
                        "   7.920721      196.5680     0.0000000E+00 0.0000000E+00 -37.52124     001102\n" +
                        "   8.345490      25.39299     0.0000000E+00 0.0000000E+00  1.107429     100003\n" +
                        "  -4.397178      15.93764     0.0000000E+00 0.0000000E+00 -1.245474     010003\n" +
                        "  0.0000000E+00 0.0000000E+00 -6.546966     -11.58295     0.0000000E+00 001003\n" +
                        "  -9543.042     -7647.112     0.0000000E+00 0.0000000E+00 -1116.674     000400\n" +
                        "  0.0000000E+00 0.0000000E+00  1975.447      1374.590     0.0000000E+00 000301\n" +
                        "  -38.60622      188.6623     0.0000000E+00 0.0000000E+00 -41.06847     000202\n" +
                        "  0.0000000E+00 0.0000000E+00 -5.899186     -12.29988     0.0000000E+00 000103\n" +
                        "  0.8549353      1.434609     0.0000000E+00 0.0000000E+00-0.2784293     000004\n" +
                        "  -2447.547      10221.30     0.0000000E+00 0.0000000E+00 -716.4276     500000\n" +
                        "  -28020.78      86400.87     0.0000000E+00 0.0000000E+00 -9884.872     410000\n" +
                        "  -104603.8      263292.5     0.0000000E+00 0.0000000E+00 -36753.40     320000\n" +
                        "  -175649.3      384843.3     0.0000000E+00 0.0000000E+00 -57483.33     230000\n" +
                        "  -140762.4      281243.0     0.0000000E+00 0.0000000E+00 -41415.98     140000\n" +
                        "  -45696.89      84126.38     0.0000000E+00 0.0000000E+00 -12046.07     050000\n" +
                        "  0.0000000E+00 0.0000000E+00 -8986.679     -24693.15     0.0000000E+00 401000\n" +
                        "  0.0000000E+00 0.0000000E+00 -48835.49     -146886.2     0.0000000E+00 311000\n" +
                        "  0.0000000E+00 0.0000000E+00 -111183.8     -348946.1     0.0000000E+00 221000\n" +
                        "  0.0000000E+00 0.0000000E+00 -125212.1     -387016.8     0.0000000E+00 131000\n" +
                        "  0.0000000E+00 0.0000000E+00 -54702.03     -161574.1     0.0000000E+00 041000\n" +
                        "  -5008.988      4727.144     0.0000000E+00 0.0000000E+00 -527.8333     302000\n" +
                        "  -48212.45      36465.68     0.0000000E+00 0.0000000E+00 -16894.53     212000\n" +
                        "  -122369.3      47446.51     0.0000000E+00 0.0000000E+00 -44363.61     122000\n" +
                        "  -88172.35      11901.11     0.0000000E+00 0.0000000E+00 -30011.27     032000\n" +
                        "  0.0000000E+00 0.0000000E+00 -14353.92     -28331.00     0.0000000E+00 203000\n" +
                        "  0.0000000E+00 0.0000000E+00 -36587.05     -78474.78     0.0000000E+00 113000\n" +
                        "  0.0000000E+00 0.0000000E+00 -23351.06     -56799.71     0.0000000E+00 023000\n" +
                        "  -11516.47     -11150.76     0.0000000E+00 0.0000000E+00  2888.273     104000\n" +
                        "  -16213.67     -12050.19     0.0000000E+00 0.0000000E+00  1753.622     014000\n" +
                        "  0.0000000E+00 0.0000000E+00 -347.5874     -1757.734     0.0000000E+00 005000\n" +
                        "  0.0000000E+00 0.0000000E+00 -18614.39     -50059.07     0.0000000E+00 400100\n" +
                        "  0.0000000E+00 0.0000000E+00 -100583.5     -287457.7     0.0000000E+00 310100\n" +
                        "  0.0000000E+00 0.0000000E+00 -224595.2     -663081.1     0.0000000E+00 220100\n" +
                        "  0.0000000E+00 0.0000000E+00 -248951.5     -728367.7     0.0000000E+00 130100\n" +
                        "  0.0000000E+00 0.0000000E+00 -109027.9     -308454.2     0.0000000E+00 040100\n" +
                        "  -30373.25      23597.19     0.0000000E+00 0.0000000E+00 -4852.181     301100\n" +
                        "  -213347.2      160444.7     0.0000000E+00 0.0000000E+00 -67376.27     211100\n" +
                        "  -479616.1      231173.0     0.0000000E+00 0.0000000E+00 -165107.2     121100\n" +
                        "  -328597.0      87327.48     0.0000000E+00 0.0000000E+00 -112258.9     031100\n" +
                        "  0.0000000E+00 0.0000000E+00 -95173.78     -208934.5     0.0000000E+00 202100\n" +
                        "  0.0000000E+00 0.0000000E+00 -256645.0     -595619.5     0.0000000E+00 112100\n" +
                        "  0.0000000E+00 0.0000000E+00 -167724.3     -424801.4     0.0000000E+00 022100\n" +
                        "  -107631.6     -89980.87     0.0000000E+00 0.0000000E+00  13584.63     103100\n" +
                        "  -157495.0     -102303.2     0.0000000E+00 0.0000000E+00  1282.865     013100\n" +
                        "  0.0000000E+00 0.0000000E+00  1261.224     -1942.420     0.0000000E+00 004100\n" +
                        "   3818.439     -4800.678     0.0000000E+00 0.0000000E+00  1629.690     400001\n" +
                        "   23076.15     -17644.75     0.0000000E+00 0.0000000E+00  7263.929     310001\n" +
                        "   51403.97     -28886.83     0.0000000E+00 0.0000000E+00  10967.10     220001\n" +
                        "   56941.40     -29658.89     0.0000000E+00 0.0000000E+00  9018.872     130001\n" +
                        "   28366.83     -12809.88     0.0000000E+00 0.0000000E+00  4814.516     040001\n" +
                        "  0.0000000E+00 0.0000000E+00  750.1458      4293.661     0.0000000E+00 301001\n" +
                        "  0.0000000E+00 0.0000000E+00  10780.90      35837.09     0.0000000E+00 211001\n" +
                        "  0.0000000E+00 0.0000000E+00  27737.44      83789.30     0.0000000E+00 121001\n" +
                        "  0.0000000E+00 0.0000000E+00  16068.98      53930.72     0.0000000E+00 031001\n" +
                        "   5319.823     -4238.226     0.0000000E+00 0.0000000E+00  4137.742     202001\n" +
                        "   22508.32     -3347.534     0.0000000E+00 0.0000000E+00  12511.30     112001\n" +
                        "   19599.86     -813.7266     0.0000000E+00 0.0000000E+00  9060.389     022001\n" +
                        "  0.0000000E+00 0.0000000E+00 -3090.788     -7045.151     0.0000000E+00 103001\n" +
                        "  0.0000000E+00 0.0000000E+00 -4744.351     -14251.03     0.0000000E+00 013001\n" +
                        "  -1176.615     -1975.804     0.0000000E+00 0.0000000E+00  502.4148     004001\n" +
                        "  -37703.75      27452.82     0.0000000E+00 0.0000000E+00 -7281.715     300200\n" +
                        "  -222614.3      173058.2     0.0000000E+00 0.0000000E+00 -67175.47     210200\n" +
                        "  -461246.1      259336.2     0.0000000E+00 0.0000000E+00 -152437.4     120200\n" +
                        "  -308219.1      109243.3     0.0000000E+00 0.0000000E+00 -102800.7     030200\n" +
                        "  0.0000000E+00 0.0000000E+00 -201351.1     -466704.2     0.0000000E+00 201200\n" +
                        "  0.0000000E+00 0.0000000E+00 -567023.2     -1358283.     0.0000000E+00 111200\n" +
                        "  0.0000000E+00 0.0000000E+00 -379054.4     -963376.7     0.0000000E+00 021200\n" +
                        "  -361314.2     -267798.0     0.0000000E+00 0.0000000E+00  17824.07     102200\n" +
                        "  -543216.7     -317320.7     0.0000000E+00 0.0000000E+00 -28578.68     012200\n" +
                        "  0.0000000E+00 0.0000000E+00  19954.90      43899.13     0.0000000E+00 003200\n" +
                        "  0.0000000E+00 0.0000000E+00  1542.960      8502.013     0.0000000E+00 300101\n" +
                        "  0.0000000E+00 0.0000000E+00  22865.64      76937.54     0.0000000E+00 210101\n" +
                        "  0.0000000E+00 0.0000000E+00  60444.24      185371.3     0.0000000E+00 120101\n" +
                        "  0.0000000E+00 0.0000000E+00  36889.24      121473.1     0.0000000E+00 030101\n" +
                        "   18424.98     -19985.72     0.0000000E+00 0.0000000E+00  15666.24     201101\n" +
                        "   88244.04     -29716.36     0.0000000E+00 0.0000000E+00  49526.59     111101\n" +
                        "   86107.92     -15663.16     0.0000000E+00 0.0000000E+00  38824.95     021101\n" +
                        "  0.0000000E+00 0.0000000E+00 -14272.50     -38820.57     0.0000000E+00 102101\n" +
                        "  0.0000000E+00 0.0000000E+00 -17264.81     -65559.66     0.0000000E+00 012101\n" +
                        "  -6544.376     -12829.99     0.0000000E+00 0.0000000E+00  3781.155     003101\n" +
                        "   156.1958     -1654.437     0.0000000E+00 0.0000000E+00  476.6367     300002\n" +
                        "   24.58631     -3311.029     0.0000000E+00 0.0000000E+00  1784.511     210002\n" +
                        "  -5244.369     -278.7657     0.0000000E+00 0.0000000E+00  499.7118     120002\n" +
                        "  -7458.525      89.08329     0.0000000E+00 0.0000000E+00 -1490.220     030002\n" +
                        "  0.0000000E+00 0.0000000E+00 -486.9968     -960.0979     0.0000000E+00 201002\n" +
                        "  0.0000000E+00 0.0000000E+00 -2013.013     -6600.146     0.0000000E+00 111002\n" +
                        "  0.0000000E+00 0.0000000E+00 -322.1269     -5720.127     0.0000000E+00 021002\n" +
                        "  -350.9889     -1437.159     0.0000000E+00 0.0000000E+00  299.7642     102002\n" +
                        "   125.6407     -930.0224     0.0000000E+00 0.0000000E+00  584.1514     012002\n" +
                        "  0.0000000E+00 0.0000000E+00  267.7026      705.6175     0.0000000E+00 003002\n" +
                        "  0.0000000E+00 0.0000000E+00 -137939.6     -327610.1     0.0000000E+00 200300\n" +
                        "  0.0000000E+00 0.0000000E+00 -402706.9     -974655.6     0.0000000E+00 110300\n" +
                        "  0.0000000E+00 0.0000000E+00 -275280.5     -696068.8     0.0000000E+00 020300\n" +
                        "  -523324.6     -349121.9     0.0000000E+00 0.0000000E+00 -89.13825     101300\n" +
                        "  -802655.1     -425779.6     0.0000000E+00 0.0000000E+00 -74376.60     011300\n" +
                        "  0.0000000E+00 0.0000000E+00  61005.40      169975.1     0.0000000E+00 002300\n" +
                        "   16180.22     -21276.95     0.0000000E+00 0.0000000E+00  14090.05     200201\n" +
                        "   90242.74     -35697.38     0.0000000E+00 0.0000000E+00  47132.03     110201\n" +
                        "   96085.49     -19200.49     0.0000000E+00 0.0000000E+00  40213.06     020201\n" +
                        "  0.0000000E+00 0.0000000E+00 -14004.02     -52051.92     0.0000000E+00 101201\n" +
                        "  0.0000000E+00 0.0000000E+00 -6539.585     -69234.48     0.0000000E+00 011201\n" +
                        "  -8329.935     -29421.80     0.0000000E+00 0.0000000E+00  11143.10     002201\n" +
                        "  0.0000000E+00 0.0000000E+00 -1882.025     -4358.623     0.0000000E+00 200102\n" +
                        "  0.0000000E+00 0.0000000E+00 -6747.240     -20690.44     0.0000000E+00 110102\n" +
                        "  0.0000000E+00 0.0000000E+00 -2261.303     -16260.53     0.0000000E+00 020102\n" +
                        "  -2719.501     -4758.456     0.0000000E+00 0.0000000E+00  786.7382     101102\n" +
                        "  -2528.818     -1887.362     0.0000000E+00 0.0000000E+00  973.3086     011102\n" +
                        "  0.0000000E+00 0.0000000E+00  784.9696      3219.497     0.0000000E+00 002102\n" +
                        "   107.5216     -389.2406     0.0000000E+00 0.0000000E+00  66.21468     200003\n" +
                        "   1238.107     -712.9371     0.0000000E+00 0.0000000E+00  498.9451     110003\n" +
                        "   1614.375      132.6933     0.0000000E+00 0.0000000E+00  561.5546     020003\n" +
                        "  0.0000000E+00 0.0000000E+00  89.50555      313.9549     0.0000000E+00 101003\n" +
                        "  0.0000000E+00 0.0000000E+00 -176.4430      117.4945     0.0000000E+00 011003\n" +
                        "  -68.41333     -85.85492     0.0000000E+00 0.0000000E+00 -3.941304     002003\n" +
                        "  -278215.4     -168653.8     0.0000000E+00 0.0000000E+00 -9108.755     100400\n" +
                        "  -433246.5     -209880.1     0.0000000E+00 0.0000000E+00 -52041.50     010400\n" +
                        "  0.0000000E+00 0.0000000E+00  73284.02      233316.7     0.0000000E+00 001400\n" +
                        "  0.0000000E+00 0.0000000E+00  3351.977     -6947.452     0.0000000E+00 100301\n" +
                        "  0.0000000E+00 0.0000000E+00  16239.84      5360.086     0.0000000E+00 010301\n" +
                        "   8159.210     -25646.98     0.0000000E+00 0.0000000E+00  15600.95     001301\n" +
                        "  -4391.693     -3820.183     0.0000000E+00 0.0000000E+00 -29.11997     100202\n" +
                        "  -5780.512      212.2642     0.0000000E+00 0.0000000E+00 -923.6692     010202\n" +
                        "  0.0000000E+00 0.0000000E+00  77.81816      4898.812     0.0000000E+00 001202\n" +
                        "  0.0000000E+00 0.0000000E+00  28.97321      533.5254     0.0000000E+00 100103\n" +
                        "  0.0000000E+00 0.0000000E+00 -712.0355     -19.80528     0.0000000E+00 010103\n" +
                        "  -196.1004     -363.9689     0.0000000E+00 0.0000000E+00  35.57807     001103\n" +
                        "  -49.60941     -39.99326     0.0000000E+00 0.0000000E+00 -14.37040     100004\n" +
                        "  -58.55626     -82.15015     0.0000000E+00 0.0000000E+00 -15.77348     010004\n" +
                        "  0.0000000E+00 0.0000000E+00  20.52459      26.06689     0.0000000E+00 001004\n" +
                        "  0.0000000E+00 0.0000000E+00  30639.14      112047.4     0.0000000E+00 000500\n" +
                        "   16098.25     -4843.134     0.0000000E+00 0.0000000E+00  8779.124     000401\n" +
                        "  0.0000000E+00 0.0000000E+00 -1001.452      2237.298     0.0000000E+00 000302\n" +
                        "  -107.0609     -420.3566     0.0000000E+00 0.0000000E+00  82.83259     000203\n" +
                        "  0.0000000E+00 0.0000000E+00  42.18981      54.50892     0.0000000E+00 000104\n" +
                        "  -1.302596    -0.1251966     0.0000000E+00 0.0000000E+00 0.7758707E-01 000005"
        )));

        list.add(BaseUtils.Content.BiContent.create(keyDistance().get(8), CosyArbitraryOrder.readMap(
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
                        "  0.5830945E-01 0.2299405     0.0000000E+00 0.0000000E+00-0.7283137     000002\n" +
                        "   8.236240      60.53023     0.0000000E+00 0.0000000E+00 -1.902012     300000\n" +
                        "  -267.7877      136.2644     0.0000000E+00 0.0000000E+00 -91.36449     210000\n" +
                        "  -799.3616     0.4719218     0.0000000E+00 0.0000000E+00 -213.6769     120000\n" +
                        "  -515.2038     -62.17062     0.0000000E+00 0.0000000E+00 -122.9168     030000\n" +
                        "  0.0000000E+00 0.0000000E+00 -470.0683     -346.0352     0.0000000E+00 201000\n" +
                        "  0.0000000E+00 0.0000000E+00 -937.0534     -756.7844     0.0000000E+00 111000\n" +
                        "  0.0000000E+00 0.0000000E+00 -547.8174     -462.2035     0.0000000E+00 021000\n" +
                        "   297.5232      226.0838     0.0000000E+00 0.0000000E+00  15.69456     102000\n" +
                        "   374.0088      360.9274     0.0000000E+00 0.0000000E+00 -14.84043     012000\n" +
                        "  0.0000000E+00 0.0000000E+00 -266.0595     -112.7277     0.0000000E+00 003000\n" +
                        "  0.0000000E+00 0.0000000E+00 -1013.481     -715.4766     0.0000000E+00 200100\n" +
                        "  0.0000000E+00 0.0000000E+00 -1957.721     -1524.238     0.0000000E+00 110100\n" +
                        "  0.0000000E+00 0.0000000E+00 -1107.131     -940.8534     0.0000000E+00 020100\n" +
                        "   1020.071      824.9893     0.0000000E+00 0.0000000E+00 -29.86793     101100\n" +
                        "   1377.548      1379.006     0.0000000E+00 0.0000000E+00 -150.3101     011100\n" +
                        "  0.0000000E+00 0.0000000E+00 -1495.854     -654.6603     0.0000000E+00 002100\n" +
                        "   91.14417      29.59412     0.0000000E+00 0.0000000E+00  21.94581     200001\n" +
                        "   276.9769      107.1515     0.0000000E+00 0.0000000E+00  57.18331     110001\n" +
                        "   127.0685      38.17042     0.0000000E+00 0.0000000E+00  35.39383     020001\n" +
                        "  0.0000000E+00 0.0000000E+00 -87.34541     -41.35714     0.0000000E+00 101001\n" +
                        "  0.0000000E+00 0.0000000E+00 -92.91434     -33.85452     0.0000000E+00 011001\n" +
                        "   22.55939      2.186293     0.0000000E+00 0.0000000E+00  6.800823     002001\n" +
                        "   845.8152      731.9230     0.0000000E+00 0.0000000E+00 -100.6213     100200\n" +
                        "   1107.492      1221.012     0.0000000E+00 0.0000000E+00 -206.7367     010200\n" +
                        "  0.0000000E+00 0.0000000E+00 -3022.008     -1404.328     0.0000000E+00 001200\n" +
                        "  0.0000000E+00 0.0000000E+00 -191.5928     -82.02399     0.0000000E+00 100101\n" +
                        "  0.0000000E+00 0.0000000E+00 -138.2559     -25.54867     0.0000000E+00 010101\n" +
                        "   82.27262      8.045677     0.0000000E+00 0.0000000E+00  23.77564     001101\n" +
                        "   6.241839     -1.673647     0.0000000E+00 0.0000000E+00 0.7930654     100002\n" +
                        "   32.33441      13.66011     0.0000000E+00 0.0000000E+00  1.142601     010002\n" +
                        "  0.0000000E+00 0.0000000E+00 -3.049303    -0.1995963E-01 0.0000000E+00 001002\n" +
                        "  0.0000000E+00 0.0000000E+00 -2176.971     -1088.442     0.0000000E+00 000300\n" +
                        "   58.45367     -7.164499     0.0000000E+00 0.0000000E+00  22.01658     000201\n" +
                        "  0.0000000E+00 0.0000000E+00 -20.32226     -7.551937     0.0000000E+00 000102\n" +
                        "  -1.435516     -1.010236     0.0000000E+00 0.0000000E+00 0.6269261     000003\n" +
                        "  -292.1265      605.4623     0.0000000E+00 0.0000000E+00 -391.7205     400000\n" +
                        "  -2203.795      2638.539     0.0000000E+00 0.0000000E+00 -1555.814     310000\n" +
                        "  -5551.982      4988.322     0.0000000E+00 0.0000000E+00 -2627.357     220000\n" +
                        "  -7580.363      4007.226     0.0000000E+00 0.0000000E+00 -2689.637     130000\n" +
                        "  -4516.079      800.1206     0.0000000E+00 0.0000000E+00 -1326.275     040000\n" +
                        "  0.0000000E+00 0.0000000E+00 -3274.449     -2328.422     0.0000000E+00 301000\n" +
                        "  0.0000000E+00 0.0000000E+00 -17927.03     -12505.27     0.0000000E+00 211000\n" +
                        "  0.0000000E+00 0.0000000E+00 -27716.42     -19724.66     0.0000000E+00 121000\n" +
                        "  0.0000000E+00 0.0000000E+00 -12427.31     -9500.775     0.0000000E+00 031000\n" +
                        "  -96.63058      866.4891     0.0000000E+00 0.0000000E+00 -1288.754     202000\n" +
                        "  -263.5313      2829.060     0.0000000E+00 0.0000000E+00 -3052.010     112000\n" +
                        "   1293.970      3409.813     0.0000000E+00 0.0000000E+00 -1874.367     022000\n" +
                        "  0.0000000E+00 0.0000000E+00  1343.562      1123.594     0.0000000E+00 103000\n" +
                        "  0.0000000E+00 0.0000000E+00 -2054.043     -606.2518     0.0000000E+00 013000\n" +
                        "  -852.2137     -418.9831     0.0000000E+00 0.0000000E+00 -196.6189     004000\n" +
                        "  0.0000000E+00 0.0000000E+00 -5920.607     -4239.492     0.0000000E+00 300100\n" +
                        "  0.0000000E+00 0.0000000E+00 -34262.62     -23941.38     0.0000000E+00 210100\n" +
                        "  0.0000000E+00 0.0000000E+00 -54776.71     -38663.59     0.0000000E+00 120100\n" +
                        "  0.0000000E+00 0.0000000E+00 -24890.95     -18522.58     0.0000000E+00 030100\n" +
                        "   169.1203      3680.304     0.0000000E+00 0.0000000E+00 -4381.717     201100\n" +
                        "  -4222.741      9712.337     0.0000000E+00 0.0000000E+00 -11193.53     111100\n" +
                        "  -1522.933      10059.23     0.0000000E+00 0.0000000E+00 -7454.690     021100\n" +
                        "  0.0000000E+00 0.0000000E+00  156.1132      1349.381     0.0000000E+00 102100\n" +
                        "  0.0000000E+00 0.0000000E+00 -21773.57     -10623.12     0.0000000E+00 012100\n" +
                        "  -7012.830     -3260.795     0.0000000E+00 0.0000000E+00 -1222.391     003100\n" +
                        "   127.0325      167.8126     0.0000000E+00 0.0000000E+00 -161.6516     300001\n" +
                        "   692.0921      37.98719     0.0000000E+00 0.0000000E+00 -224.2924     210001\n" +
                        "   3259.975      267.4729     0.0000000E+00 0.0000000E+00  574.8309     120001\n" +
                        "   3311.460      682.1228     0.0000000E+00 0.0000000E+00  718.1789     030001\n" +
                        "  0.0000000E+00 0.0000000E+00  1157.261      640.7486     0.0000000E+00 201001\n" +
                        "  0.0000000E+00 0.0000000E+00  2981.861      2040.874     0.0000000E+00 111001\n" +
                        "  0.0000000E+00 0.0000000E+00  1311.473      1248.980     0.0000000E+00 021001\n" +
                        "  -127.2913      70.24134     0.0000000E+00 0.0000000E+00 -198.1840     102001\n" +
                        "  -1135.503     -625.4864     0.0000000E+00 0.0000000E+00 -194.1687     012001\n" +
                        "  0.0000000E+00 0.0000000E+00  270.0804      58.36405     0.0000000E+00 003001\n" +
                        "   25.89775      3380.367     0.0000000E+00 0.0000000E+00 -3623.252     200200\n" +
                        "  -7246.707      7833.584     0.0000000E+00 0.0000000E+00 -10187.38     110200\n" +
                        "  -5550.752      7679.374     0.0000000E+00 0.0000000E+00 -7491.669     020200\n" +
                        "  0.0000000E+00 0.0000000E+00 -14954.26     -7675.306     0.0000000E+00 101200\n" +
                        "  0.0000000E+00 0.0000000E+00 -60216.89     -33566.34     0.0000000E+00 011200\n" +
                        "  -22526.43     -9954.974     0.0000000E+00 0.0000000E+00 -3062.826     002200\n" +
                        "  0.0000000E+00 0.0000000E+00  3562.828      2058.504     0.0000000E+00 200101\n" +
                        "  0.0000000E+00 0.0000000E+00  8581.519      5664.456     0.0000000E+00 110101\n" +
                        "  0.0000000E+00 0.0000000E+00  3852.805      3219.423     0.0000000E+00 020101\n" +
                        "  -273.9610      5.681175     0.0000000E+00 0.0000000E+00 -362.8033     101101\n" +
                        "  -3928.434     -2772.759     0.0000000E+00 0.0000000E+00 -67.15472     011101\n" +
                        "  0.0000000E+00 0.0000000E+00  2062.477      518.5363     0.0000000E+00 002101\n" +
                        "  -66.69383      64.26168     0.0000000E+00 0.0000000E+00 -65.91359     200002\n" +
                        "  -802.8250     -149.5773     0.0000000E+00 0.0000000E+00 -256.4907     110002\n" +
                        "  -816.1731     -270.9768     0.0000000E+00 0.0000000E+00 -203.6512     020002\n" +
                        "  0.0000000E+00 0.0000000E+00  74.36494      4.334830     0.0000000E+00 101002\n" +
                        "  0.0000000E+00 0.0000000E+00  308.9682      115.6646     0.0000000E+00 011002\n" +
                        "   76.65097      57.73805     0.0000000E+00 0.0000000E+00 -10.21590     002002\n" +
                        "  0.0000000E+00 0.0000000E+00 -19287.51     -11436.21     0.0000000E+00 100300\n" +
                        "  0.0000000E+00 0.0000000E+00 -48977.85     -28914.00     0.0000000E+00 010300\n" +
                        "  -33133.02     -14087.63     0.0000000E+00 0.0000000E+00 -3731.379     001300\n" +
                        "  -222.6340     -356.5452     0.0000000E+00 0.0000000E+00  147.1905     100201\n" +
                        "  -3553.949     -3171.627     0.0000000E+00 0.0000000E+00  671.2907     010201\n" +
                        "  0.0000000E+00 0.0000000E+00  5043.771      1392.488     0.0000000E+00 001201\n" +
                        "  0.0000000E+00 0.0000000E+00  348.3701      104.0501     0.0000000E+00 100102\n" +
                        "  0.0000000E+00 0.0000000E+00  768.6204      285.7451     0.0000000E+00 010102\n" +
                        "   230.9850      196.5680     0.0000000E+00 0.0000000E+00 -54.89621     001102\n" +
                        "   36.24929      25.39299     0.0000000E+00 0.0000000E+00  3.195838     100003\n" +
                        "  0.1416955      15.93764     0.0000000E+00 0.0000000E+00  1.318122     010003\n" +
                        "  0.0000000E+00 0.0000000E+00 -18.93227     -11.58295     0.0000000E+00 001003\n" +
                        "  -18528.99     -7647.112     0.0000000E+00 0.0000000E+00 -1866.852     000400\n" +
                        "  0.0000000E+00 0.0000000E+00  4317.738      1374.590     0.0000000E+00 000301\n" +
                        "   193.3491      188.6623     0.0000000E+00 0.0000000E+00 -63.68115     000202\n" +
                        "  0.0000000E+00 0.0000000E+00 -12.34174     -12.29988     0.0000000E+00 000103\n" +
                        "   3.319841      1.434609     0.0000000E+00 0.0000000E+00-0.4499161     000004\n" +
                        "   9943.295      10221.30     0.0000000E+00 0.0000000E+00 -953.6843     500000\n" +
                        "   74484.90      86400.87     0.0000000E+00 0.0000000E+00 -12016.98     410000\n" +
                        "   205120.8      263292.5     0.0000000E+00 0.0000000E+00 -43876.01     320000\n" +
                        "   276440.0      384843.3     0.0000000E+00 0.0000000E+00 -66491.87     230000\n" +
                        "   190418.7      281243.0     0.0000000E+00 0.0000000E+00 -44867.62     140000\n" +
                        "   53658.60      84126.38     0.0000000E+00 0.0000000E+00 -11871.19     050000\n" +
                        "  0.0000000E+00 0.0000000E+00 -38632.33     -24693.15     0.0000000E+00 401000\n" +
                        "  0.0000000E+00 0.0000000E+00 -223363.7     -146886.2     0.0000000E+00 311000\n" +
                        "  0.0000000E+00 0.0000000E+00 -523375.0     -348946.1     0.0000000E+00 221000\n" +
                        "  0.0000000E+00 0.0000000E+00 -581318.9     -387016.8     0.0000000E+00 131000\n" +
                        "  0.0000000E+00 0.0000000E+00 -245062.9     -161574.1     0.0000000E+00 041000\n" +
                        "   2196.688      4727.144     0.0000000E+00 0.0000000E+00 -40.69207     302000\n" +
                        "  -1178.145      36465.68     0.0000000E+00 0.0000000E+00 -22287.35     212000\n" +
                        "  -63490.67      47446.51     0.0000000E+00 0.0000000E+00 -61848.17     122000\n" +
                        "  -73440.32      11901.11     0.0000000E+00 0.0000000E+00 -42176.04     032000\n" +
                        "  0.0000000E+00 0.0000000E+00 -48765.01     -28331.00     0.0000000E+00 203000\n" +
                        "  0.0000000E+00 0.0000000E+00 -130912.4     -78474.78     0.0000000E+00 113000\n" +
                        "  0.0000000E+00 0.0000000E+00 -91120.89     -56799.71     0.0000000E+00 023000\n" +
                        "  -24244.00     -11150.76     0.0000000E+00 0.0000000E+00  5188.245     104000\n" +
                        "  -30043.68     -12050.19     0.0000000E+00 0.0000000E+00  3013.880     014000\n" +
                        "  0.0000000E+00 0.0000000E+00 -2587.540     -1757.734     0.0000000E+00 005000\n" +
                        "  0.0000000E+00 0.0000000E+00 -78388.86     -50059.07     0.0000000E+00 400100\n" +
                        "  0.0000000E+00 0.0000000E+00 -441579.0     -287457.7     0.0000000E+00 310100\n" +
                        "  0.0000000E+00 0.0000000E+00 -1008403.     -663081.1     0.0000000E+00 220100\n" +
                        "  0.0000000E+00 0.0000000E+00 -1108600.     -728367.7     0.0000000E+00 130100\n" +
                        "  0.0000000E+00 0.0000000E+00 -472858.0     -308454.2     0.0000000E+00 040100\n" +
                        "   2238.941      23597.19     0.0000000E+00 0.0000000E+00 -6713.565     301100\n" +
                        "  -11009.44      160444.7     0.0000000E+00 0.0000000E+00 -96316.58     211100\n" +
                        "  -195476.4      231173.0     0.0000000E+00 0.0000000E+00 -235727.6     121100\n" +
                        "  -222026.7      87327.48     0.0000000E+00 0.0000000E+00 -158872.1     031100\n" +
                        "  0.0000000E+00 0.0000000E+00 -345363.4     -208934.5     0.0000000E+00 202100\n" +
                        "  0.0000000E+00 0.0000000E+00 -965777.6     -595619.5     0.0000000E+00 112100\n" +
                        "  0.0000000E+00 0.0000000E+00 -671847.1     -424801.4     0.0000000E+00 022100\n" +
                        "  -211442.3     -89980.87     0.0000000E+00 0.0000000E+00  24077.41     103100\n" +
                        "  -275973.4     -102303.2     0.0000000E+00 0.0000000E+00  1797.214     013100\n" +
                        "  0.0000000E+00 0.0000000E+00 -2338.962     -1942.420     0.0000000E+00 004100\n" +
                        "  -1874.051     -4800.678     0.0000000E+00 0.0000000E+00  1960.288     400001\n" +
                        "   1715.110     -17644.75     0.0000000E+00 0.0000000E+00  8848.948     310001\n" +
                        "   14617.93     -28886.83     0.0000000E+00 0.0000000E+00  11559.08     220001\n" +
                        "   18464.76     -29658.89     0.0000000E+00 0.0000000E+00  6751.954     130001\n" +
                        "   12173.77     -12809.88     0.0000000E+00 0.0000000E+00  3622.863     040001\n" +
                        "  0.0000000E+00 0.0000000E+00  7098.635      4293.661     0.0000000E+00 301001\n" +
                        "  0.0000000E+00 0.0000000E+00  60603.44      35837.09     0.0000000E+00 211001\n" +
                        "  0.0000000E+00 0.0000000E+00  139112.1      83789.30     0.0000000E+00 121001\n" +
                        "  0.0000000E+00 0.0000000E+00  85924.05      53930.72     0.0000000E+00 031001\n" +
                        "   95.63506     -4238.226     0.0000000E+00 0.0000000E+00  6566.839     202001\n" +
                        "   17349.20     -3347.534     0.0000000E+00 0.0000000E+00  19759.41     112001\n" +
                        "   16615.30     -813.7266     0.0000000E+00 0.0000000E+00  13910.52     022001\n" +
                        "  0.0000000E+00 0.0000000E+00 -12275.00     -7045.151     0.0000000E+00 103001\n" +
                        "  0.0000000E+00 0.0000000E+00 -21229.03     -14251.03     0.0000000E+00 013001\n" +
                        "  -3210.188     -1975.804     0.0000000E+00 0.0000000E+00  788.7558     004001\n" +
                        "  -2003.387      27452.82     0.0000000E+00 0.0000000E+00 -13040.34     300200\n" +
                        "  -7811.763      173058.2     0.0000000E+00 0.0000000E+00 -104416.6     210200\n" +
                        "  -143843.1      259336.2     0.0000000E+00 0.0000000E+00 -225231.8     120200\n" +
                        "  -175136.8      109243.3     0.0000000E+00 0.0000000E+00 -147513.0     030200\n" +
                        "  0.0000000E+00 0.0000000E+00 -756875.8     -466704.2     0.0000000E+00 201200\n" +
                        "  0.0000000E+00 0.0000000E+00 -2178157.     -1358283.     0.0000000E+00 111200\n" +
                        "  0.0000000E+00 0.0000000E+00 -1520009.     -963376.7     0.0000000E+00 021200\n" +
                        "  -672015.0     -267798.0     0.0000000E+00 0.0000000E+00  30427.03     102200\n" +
                        "  -912365.5     -317320.7     0.0000000E+00 0.0000000E+00 -50476.37     012200\n" +
                        "  0.0000000E+00 0.0000000E+00  67391.30      43899.13     0.0000000E+00 003200\n" +
                        "  0.0000000E+00 0.0000000E+00  14118.25      8502.013     0.0000000E+00 300101\n" +
                        "  0.0000000E+00 0.0000000E+00  128967.1      76937.54     0.0000000E+00 210101\n" +
                        "  0.0000000E+00 0.0000000E+00  304555.7      185371.3     0.0000000E+00 120101\n" +
                        "  0.0000000E+00 0.0000000E+00  192492.4      121473.1     0.0000000E+00 030101\n" +
                        "  -6981.378     -19985.72     0.0000000E+00 0.0000000E+00  24596.62     201101\n" +
                        "   47463.12     -29716.36     0.0000000E+00 0.0000000E+00  75897.78     111101\n" +
                        "   60905.24     -15663.16     0.0000000E+00 0.0000000E+00  57310.15     021101\n" +
                        "  0.0000000E+00 0.0000000E+00 -61325.27     -38820.57     0.0000000E+00 102101\n" +
                        "  0.0000000E+00 0.0000000E+00 -87533.23     -65559.66     0.0000000E+00 012101\n" +
                        "  -19419.39     -12829.99     0.0000000E+00 0.0000000E+00  6038.261     003101\n" +
                        "  -1787.020     -1654.437     0.0000000E+00 0.0000000E+00  590.5022     300002\n" +
                        "  -3384.533     -3311.029     0.0000000E+00 0.0000000E+00  2744.942     210002\n" +
                        "  -5124.290     -278.7657     0.0000000E+00 0.0000000E+00  1742.579     120002\n" +
                        "  -7581.941      89.08329     0.0000000E+00 0.0000000E+00 -1358.908     030002\n" +
                        "  0.0000000E+00 0.0000000E+00 -2243.816     -960.0979     0.0000000E+00 201002\n" +
                        "  0.0000000E+00 0.0000000E+00 -11631.37     -6600.146     0.0000000E+00 111002\n" +
                        "  0.0000000E+00 0.0000000E+00 -8203.746     -5720.127     0.0000000E+00 021002\n" +
                        "  -1966.379     -1437.159     0.0000000E+00 0.0000000E+00  353.5031     102002\n" +
                        "  -351.0656     -930.0224     0.0000000E+00 0.0000000E+00  749.4452     012002\n" +
                        "  0.0000000E+00 0.0000000E+00  996.1687      705.6175     0.0000000E+00 003002\n" +
                        "  0.0000000E+00 0.0000000E+00 -527050.6     -327610.1     0.0000000E+00 200300\n" +
                        "  0.0000000E+00 0.0000000E+00 -1557191.     -974655.6     0.0000000E+00 110300\n" +
                        "  0.0000000E+00 0.0000000E+00 -1098885.     -696068.8     0.0000000E+00 020300\n" +
                        "  -929046.6     -349121.9     0.0000000E+00 0.0000000E+00 -3462.275     101300\n" +
                        "  -1298653.     -425779.6     0.0000000E+00 0.0000000E+00 -129377.7     011300\n" +
                        "  0.0000000E+00 0.0000000E+00  253973.4      169975.1     0.0000000E+00 002300\n" +
                        "  -11374.09     -21276.95     0.0000000E+00 0.0000000E+00  21914.84     200201\n" +
                        "   41822.59     -35697.38     0.0000000E+00 0.0000000E+00  70506.40     110201\n" +
                        "   67451.69     -19200.49     0.0000000E+00 0.0000000E+00  57837.68     020201\n" +
                        "  0.0000000E+00 0.0000000E+00 -70397.83     -52051.92     0.0000000E+00 101201\n" +
                        "  0.0000000E+00 0.0000000E+00 -65327.63     -69234.48     0.0000000E+00 011201\n" +
                        "  -36322.04     -29421.80     0.0000000E+00 0.0000000E+00  18077.62     002201\n" +
                        "  0.0000000E+00 0.0000000E+00 -8800.988     -4358.623     0.0000000E+00 200102\n" +
                        "  0.0000000E+00 0.0000000E+00 -35821.07     -20690.44     0.0000000E+00 110102\n" +
                        "  0.0000000E+00 0.0000000E+00 -24134.71     -16260.53     0.0000000E+00 020102\n" +
                        "  -7834.069     -4758.456     0.0000000E+00 0.0000000E+00  1009.126     101102\n" +
                        "  -2118.820     -1887.362     0.0000000E+00 0.0000000E+00  1183.672     011102\n" +
                        "  0.0000000E+00 0.0000000E+00  3845.925      3219.497     0.0000000E+00 002102\n" +
                        "  -386.7560     -389.2406     0.0000000E+00 0.0000000E+00  46.05875     200003\n" +
                        "   523.7418     -712.9371     0.0000000E+00 0.0000000E+00  576.5902     110003\n" +
                        "   1946.779      132.6933     0.0000000E+00 0.0000000E+00  723.0710     020003\n" +
                        "  0.0000000E+00 0.0000000E+00  437.9596      313.9549     0.0000000E+00 101003\n" +
                        "  0.0000000E+00 0.0000000E+00 -125.7464      117.4945     0.0000000E+00 011003\n" +
                        "  -205.1071     -85.85492     0.0000000E+00 0.0000000E+00 -11.70465     002003\n" +
                        "  -473914.2     -168653.8     0.0000000E+00 0.0000000E+00 -19317.65     100400\n" +
                        "  -677575.0     -209880.1     0.0000000E+00 0.0000000E+00 -90723.09     010400\n" +
                        "  0.0000000E+00 0.0000000E+00  341820.8      233316.7     0.0000000E+00 001400\n" +
                        "  0.0000000E+00 0.0000000E+00  2930.943     -6947.452     0.0000000E+00 100301\n" +
                        "  0.0000000E+00 0.0000000E+00  42250.17      5360.086     0.0000000E+00 010301\n" +
                        "  -12822.47     -25646.98     0.0000000E+00 0.0000000E+00  25759.40     001301\n" +
                        "  -8187.479     -3820.183     0.0000000E+00 0.0000000E+00 -50.05075     100202\n" +
                        "  -2717.362      212.2642     0.0000000E+00 0.0000000E+00 -1481.715     010202\n" +
                        "  0.0000000E+00 0.0000000E+00  4102.572      4898.812     0.0000000E+00 001202\n" +
                        "  0.0000000E+00 0.0000000E+00  557.5617      533.5254     0.0000000E+00 100103\n" +
                        "  0.0000000E+00 0.0000000E+00 -919.5444     -19.80528     0.0000000E+00 010103\n" +
                        "  -750.3172     -363.9689     0.0000000E+00 0.0000000E+00  49.13594     001103\n" +
                        "  -111.3624     -39.99326     0.0000000E+00 0.0000000E+00 -21.71141     100004\n" +
                        "  -154.5217     -82.15015     0.0000000E+00 0.0000000E+00 -29.01993     010004\n" +
                        "  0.0000000E+00 0.0000000E+00  57.71160      26.06689     0.0000000E+00 001004\n" +
                        "  0.0000000E+00 0.0000000E+00  160426.2      112047.4     0.0000000E+00 000500\n" +
                        "   15330.79     -4843.134     0.0000000E+00 0.0000000E+00  14742.94     000401\n" +
                        "  0.0000000E+00 0.0000000E+00  113.9742      2237.298     0.0000000E+00 000302\n" +
                        "  -735.7686     -420.3566     0.0000000E+00 0.0000000E+00  143.2509     000203\n" +
                        "  0.0000000E+00 0.0000000E+00  108.1355      54.50892     0.0000000E+00 000104\n" +
                        "  -2.974210    -0.1251966     0.0000000E+00 0.0000000E+00 0.4071897     000005"
        )));

        list.add(BaseUtils.Content.BiContent.create(keyDistance().get(8) + DL2, CosyArbitraryOrder.readMap(
                "   4.422597      1.427547     0.0000000E+00 0.0000000E+00-0.8509655E-02 100000\n" +
                        "   2.386644     0.9964840     0.0000000E+00 0.0000000E+00 0.2095900E-04 010000\n" +
                        "  0.0000000E+00 0.0000000E+00 -3.122598    -0.8864910     0.0000000E+00 001000\n" +
                        "  0.0000000E+00 0.0000000E+00 -2.376836    -0.9950191     0.0000000E+00 000100\n" +
                        "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00  1.000000     000010\n" +
                        "  0.2040221E-01 0.8509655E-02 0.0000000E+00 0.0000000E+00  1.270425     000001\n" +
                        "  -32.06016     -5.556269     0.0000000E+00 0.0000000E+00 -1.652702     200000\n" +
                        "  -26.09522     0.2736982E-01 0.0000000E+00 0.0000000E+00 -1.632806     110000\n" +
                        "   31.53591      13.12654     0.0000000E+00 0.0000000E+00-0.5531668     020000\n" +
                        "  0.0000000E+00 0.0000000E+00  34.64195      5.880510     0.0000000E+00 101000\n" +
                        "  0.0000000E+00 0.0000000E+00  12.07812    -0.1448351E-01 0.0000000E+00 011000\n" +
                        "  -9.042372     -1.522805     0.0000000E+00 0.0000000E+00 -1.138838     002000\n" +
                        "  0.0000000E+00 0.0000000E+00  34.26424      3.164848     0.0000000E+00 100100\n" +
                        "  0.0000000E+00 0.0000000E+00 -29.27665     -12.17123     0.0000000E+00 010100\n" +
                        "  -4.394770      3.215001     0.0000000E+00 0.0000000E+00 -1.649339     001100\n" +
                        "  0.4441741     0.9628742     0.0000000E+00 0.0000000E+00 0.1167855     100001\n" +
                        " -0.4597674     0.2711272     0.0000000E+00 0.0000000E+00-0.1169972E-01 010001\n" +
                        "  0.0000000E+00 0.0000000E+00-0.1599347    -0.8042071     0.0000000E+00 001001\n" +
                        "   32.15427      13.38294     0.0000000E+00 0.0000000E+00 -1.253502     000200\n" +
                        "  0.0000000E+00 0.0000000E+00  3.659738     0.4778055     0.0000000E+00 000101\n" +
                        " -0.1671859    -0.6537472E-01 0.0000000E+00 0.0000000E+00 -1.092269     000002\n" +
                        "  -338.8866     -92.80341     0.0000000E+00 0.0000000E+00  19.94525     300000\n" +
                        "  -2009.357     -503.7975     0.0000000E+00 0.0000000E+00  24.78405     210000\n" +
                        "  -3402.960     -806.4819     0.0000000E+00 0.0000000E+00 -15.41919     120000\n" +
                        "  -1667.487     -375.9656     0.0000000E+00 0.0000000E+00 -12.79800     030000\n" +
                        "  0.0000000E+00 0.0000000E+00 -362.0468     -92.27420     0.0000000E+00 201000\n" +
                        "  0.0000000E+00 0.0000000E+00 -366.2968     -117.6130     0.0000000E+00 111000\n" +
                        "  0.0000000E+00 0.0000000E+00  2.398721     -28.34393     0.0000000E+00 021000\n" +
                        "   169.4003      47.90212     0.0000000E+00 0.0000000E+00  19.07166     102000\n" +
                        "   264.9609      89.38007     0.0000000E+00 0.0000000E+00  11.15235     012000\n" +
                        "  0.0000000E+00 0.0000000E+00 -377.0896     -78.33313     0.0000000E+00 003000\n" +
                        "  0.0000000E+00 0.0000000E+00 -737.4929     -193.6569     0.0000000E+00 200100\n" +
                        "  0.0000000E+00 0.0000000E+00 -748.5221     -252.7975     0.0000000E+00 110100\n" +
                        "  0.0000000E+00 0.0000000E+00 -64.13476     -87.76033     0.0000000E+00 020100\n" +
                        "   331.4492      121.6824     0.0000000E+00 0.0000000E+00  5.825194     101100\n" +
                        "   730.6966      292.0067     0.0000000E+00 0.0000000E+00 -14.54432     011100\n" +
                        "  0.0000000E+00 0.0000000E+00 -2248.428     -479.6198     0.0000000E+00 002100\n" +
                        "  -31.87623     -10.30287     0.0000000E+00 0.0000000E+00-0.5025227     200001\n" +
                        "  -33.27110     -8.092743     0.0000000E+00 0.0000000E+00-0.1355306     110001\n" +
                        "  -43.78755     -6.223280     0.0000000E+00 0.0000000E+00 0.8991022     020001\n" +
                        "  0.0000000E+00 0.0000000E+00  73.60442      19.64457     0.0000000E+00 101001\n" +
                        "  0.0000000E+00 0.0000000E+00 -6.744184     0.3744378     0.0000000E+00 011001\n" +
                        "   25.88135      10.02347     0.0000000E+00 0.0000000E+00  2.202025     002001\n" +
                        "   29.61342      60.94811     0.0000000E+00 0.0000000E+00 -49.74030     100200\n" +
                        "   410.3578      232.0219     0.0000000E+00 0.0000000E+00 -39.13866     010200\n" +
                        "  0.0000000E+00 0.0000000E+00 -4736.062     -1039.303     0.0000000E+00 001200\n" +
                        "  0.0000000E+00 0.0000000E+00  135.0307      36.90453     0.0000000E+00 100101\n" +
                        "  0.0000000E+00 0.0000000E+00  44.47410      10.00852     0.0000000E+00 010101\n" +
                        "   119.8112      44.51941     0.0000000E+00 0.0000000E+00  7.734544     001101\n" +
                        "   1.533797     0.9477599E-01 0.0000000E+00 0.0000000E+00-0.2860390E-01 100002\n" +
                        " -0.3862108    -0.5464363     0.0000000E+00 0.0000000E+00 0.1107322     010002\n" +
                        "  0.0000000E+00 0.0000000E+00 -3.478188    -0.6165215E-01 0.0000000E+00 001002\n" +
                        "  0.0000000E+00 0.0000000E+00 -3541.659     -800.7339     0.0000000E+00 000300\n" +
                        "   66.19485      31.75485     0.0000000E+00 0.0000000E+00  9.744105     000201\n" +
                        "  0.0000000E+00 0.0000000E+00 -16.31674     -2.652776     0.0000000E+00 000102\n" +
                        "  0.2010781     0.4633389E-01 0.0000000E+00 0.0000000E+00 0.9647097     000003\n" +
                        "   14.59717     -154.9667     0.0000000E+00 0.0000000E+00  150.3137     400000\n" +
                        "   251.0285     -872.9781     0.0000000E+00 0.0000000E+00  1394.860     310000\n" +
                        "  -6393.258     -3472.470     0.0000000E+00 0.0000000E+00  3274.661     220000\n" +
                        "  -19632.91     -6318.385     0.0000000E+00 0.0000000E+00  2651.951     130000\n" +
                        "  -14095.92     -3793.244     0.0000000E+00 0.0000000E+00  591.4953     040000\n" +
                        "  0.0000000E+00 0.0000000E+00 -2891.154     -687.9454     0.0000000E+00 301000\n" +
                        "  0.0000000E+00 0.0000000E+00 -16264.53     -3902.831     0.0000000E+00 211000\n" +
                        "  0.0000000E+00 0.0000000E+00 -25298.11     -6192.863     0.0000000E+00 121000\n" +
                        "  0.0000000E+00 0.0000000E+00 -10156.24     -2627.292     0.0000000E+00 031000\n" +
                        "  -3066.646     -763.2199     0.0000000E+00 0.0000000E+00 -559.9042     202000\n" +
                        "  -8469.788     -1901.479     0.0000000E+00 0.0000000E+00 -807.6638     112000\n" +
                        "  -4764.735     -929.5593     0.0000000E+00 0.0000000E+00 -235.6366     022000\n" +
                        "  0.0000000E+00 0.0000000E+00  646.9910      192.0657     0.0000000E+00 103000\n" +
                        "  0.0000000E+00 0.0000000E+00 -2026.269     -426.4908     0.0000000E+00 013000\n" +
                        "  -1084.394     -287.1761     0.0000000E+00 0.0000000E+00 -181.6968     004000\n" +
                        "  0.0000000E+00 0.0000000E+00 -5761.424     -1352.005     0.0000000E+00 300100\n" +
                        "  0.0000000E+00 0.0000000E+00 -33289.40     -7920.747     0.0000000E+00 210100\n" +
                        "  0.0000000E+00 0.0000000E+00 -52764.15     -12827.69     0.0000000E+00 120100\n" +
                        "  0.0000000E+00 0.0000000E+00 -21482.81     -5477.244     0.0000000E+00 030100\n" +
                        "  -11894.01     -3109.685     0.0000000E+00 0.0000000E+00 -1521.555     201100\n" +
                        "  -37652.87     -8672.323     0.0000000E+00 0.0000000E+00 -2322.867     111100\n" +
                        "  -25709.01     -5286.628     0.0000000E+00 0.0000000E+00 -998.1423     021100\n" +
                        "  0.0000000E+00 0.0000000E+00 -2742.068     -510.7057     0.0000000E+00 102100\n" +
                        "  0.0000000E+00 0.0000000E+00 -20359.78     -4657.659     0.0000000E+00 012100\n" +
                        "  -9082.842     -2292.997     0.0000000E+00 0.0000000E+00 -1090.783     003100\n" +
                        "  -273.3997     -103.0692     0.0000000E+00 0.0000000E+00  23.67359     300001\n" +
                        "  -371.3854     -309.2949     0.0000000E+00 0.0000000E+00  24.80261     210001\n" +
                        "   1145.520     -135.7156     0.0000000E+00 0.0000000E+00  1.590351     120001\n" +
                        "   1225.475      60.02462     0.0000000E+00 0.0000000E+00 -11.96381     030001\n" +
                        "  0.0000000E+00 0.0000000E+00 -129.3610     -114.8169     0.0000000E+00 201001\n" +
                        "  0.0000000E+00 0.0000000E+00  950.9521      108.7662     0.0000000E+00 111001\n" +
                        "  0.0000000E+00 0.0000000E+00  1333.424      278.8213     0.0000000E+00 021001\n" +
                        "  -331.3777      18.21707     0.0000000E+00 0.0000000E+00  18.67611     102001\n" +
                        "  -335.2815      51.60679     0.0000000E+00 0.0000000E+00 -9.778725     012001\n" +
                        "  0.0000000E+00 0.0000000E+00 -1437.962     -398.7901     0.0000000E+00 003001\n" +
                        "  -10607.16     -2903.053     0.0000000E+00 0.0000000E+00 -903.3059     200200\n" +
                        "  -39522.61     -9209.980     0.0000000E+00 0.0000000E+00 -1735.204     110200\n" +
                        "  -30810.76     -6504.505     0.0000000E+00 0.0000000E+00 -1376.053     020200\n" +
                        "  0.0000000E+00 0.0000000E+00 -20517.91     -4825.448     0.0000000E+00 101200\n" +
                        "  0.0000000E+00 0.0000000E+00 -56760.06     -13459.50     0.0000000E+00 011200\n" +
                        "  -28836.38     -6927.517     0.0000000E+00 0.0000000E+00 -2466.911     002200\n" +
                        "  0.0000000E+00 0.0000000E+00  321.8637     -67.90246     0.0000000E+00 200101\n" +
                        "  0.0000000E+00 0.0000000E+00  2457.998      436.5434     0.0000000E+00 110101\n" +
                        "  0.0000000E+00 0.0000000E+00  3040.423      740.7771     0.0000000E+00 020101\n" +
                        "  -1740.877     -110.0403     0.0000000E+00 0.0000000E+00  57.10959     101101\n" +
                        "  -2362.668     -186.2185     0.0000000E+00 0.0000000E+00  49.72569     011101\n" +
                        "  0.0000000E+00 0.0000000E+00 -7208.455     -2094.121     0.0000000E+00 002101\n" +
                        "   99.33748      20.08039     0.0000000E+00 0.0000000E+00 -2.425870     200002\n" +
                        "   247.6247      48.93629     0.0000000E+00 0.0000000E+00 -2.805736     110002\n" +
                        "   162.0528      24.65772     0.0000000E+00 0.0000000E+00 -2.284485     020002\n" +
                        "  0.0000000E+00 0.0000000E+00 -1.286857      15.99800     0.0000000E+00 101002\n" +
                        "  0.0000000E+00 0.0000000E+00  25.82036      7.680771     0.0000000E+00 011002\n" +
                        "  -21.44484     -3.470310     0.0000000E+00 0.0000000E+00 -5.007125     002002\n" +
                        "  0.0000000E+00 0.0000000E+00 -24287.06     -5918.027     0.0000000E+00 100300\n" +
                        "  0.0000000E+00 0.0000000E+00 -46986.17     -11349.78     0.0000000E+00 010300\n" +
                        "  -41479.63     -9498.001     0.0000000E+00 0.0000000E+00 -2477.423     001300\n" +
                        "  -1850.165     -231.3766     0.0000000E+00 0.0000000E+00  136.7869     100201\n" +
                        "  -2958.413     -523.4356     0.0000000E+00 0.0000000E+00  117.6191     010201\n" +
                        "  0.0000000E+00 0.0000000E+00 -10823.22     -3422.033     0.0000000E+00 001201\n" +
                        "  0.0000000E+00 0.0000000E+00 -128.7958     -4.402835     0.0000000E+00 100102\n" +
                        "  0.0000000E+00 0.0000000E+00  28.93797      17.94736     0.0000000E+00 010102\n" +
                        "  -246.7030     -60.72725     0.0000000E+00 0.0000000E+00 -13.59434     001102\n" +
                        "  -2.078767    -0.2052832     0.0000000E+00 0.0000000E+00 0.1013442     100003\n" +
                        " -0.9356685    -0.7750638E-01 0.0000000E+00 0.0000000E+00-0.2114617     010003\n" +
                        "  0.0000000E+00 0.0000000E+00 -8.602399     -3.911722     0.0000000E+00 001003\n" +
                        "  -22824.75     -5009.697     0.0000000E+00 0.0000000E+00 -899.8907     000400\n" +
                        "  0.0000000E+00 0.0000000E+00 -3496.944     -1456.590     0.0000000E+00 000301\n" +
                        "  -292.5362     -88.04819     0.0000000E+00 0.0000000E+00 -20.95204     000202\n" +
                        "  0.0000000E+00 0.0000000E+00  15.11643    -0.4712181     0.0000000E+00 000103\n" +
                        " -0.2804740    -0.5660992E-01 0.0000000E+00 0.0000000E+00-0.8913176     000004\n" +
                        "   2916.954      998.7156     0.0000000E+00 0.0000000E+00 -1215.995     500000\n" +
                        "  -5647.999     -1364.534     0.0000000E+00 0.0000000E+00 -8577.732     410000\n" +
                        "  -37259.62     -15849.80     0.0000000E+00 0.0000000E+00 -11473.68     320000\n" +
                        "  -41616.76     -30031.28     0.0000000E+00 0.0000000E+00  14145.90     230000\n" +
                        "  -30365.05     -28577.42     0.0000000E+00 0.0000000E+00  34645.44     140000\n" +
                        "  -26131.67     -13966.12     0.0000000E+00 0.0000000E+00  15817.07     050000\n" +
                        "  0.0000000E+00 0.0000000E+00  16287.02      4237.158     0.0000000E+00 401000\n" +
                        "  0.0000000E+00 0.0000000E+00  48760.15      12163.43     0.0000000E+00 311000\n" +
                        "  0.0000000E+00 0.0000000E+00 -33394.50     -8251.005     0.0000000E+00 221000\n" +
                        "  0.0000000E+00 0.0000000E+00 -205989.7     -49875.28     0.0000000E+00 131000\n" +
                        "  0.0000000E+00 0.0000000E+00 -144676.9     -34819.21     0.0000000E+00 041000\n" +
                        "   12854.23      1425.882     0.0000000E+00 0.0000000E+00  2559.698     302000\n" +
                        "  -34613.63     -14086.38     0.0000000E+00 0.0000000E+00 -1583.312     212000\n" +
                        "  -182687.3     -49079.90     0.0000000E+00 0.0000000E+00 -13773.67     122000\n" +
                        "  -149568.8     -36381.40     0.0000000E+00 0.0000000E+00 -8905.814     032000\n" +
                        "  0.0000000E+00 0.0000000E+00 -13765.55     -2629.263     0.0000000E+00 203000\n" +
                        "  0.0000000E+00 0.0000000E+00 -42995.25     -9281.583     0.0000000E+00 113000\n" +
                        "  0.0000000E+00 0.0000000E+00 -22230.86     -4993.120     0.0000000E+00 023000\n" +
                        "  -7579.997     -1971.844     0.0000000E+00 0.0000000E+00  4374.205     104000\n" +
                        "  -20456.11     -5339.733     0.0000000E+00 0.0000000E+00  1570.914     014000\n" +
                        "  0.0000000E+00 0.0000000E+00  11477.49      2783.976     0.0000000E+00 005000\n" +
                        "  0.0000000E+00 0.0000000E+00  32037.92      8260.317     0.0000000E+00 400100\n" +
                        "  0.0000000E+00 0.0000000E+00  104187.4      26152.31     0.0000000E+00 310100\n" +
                        "  0.0000000E+00 0.0000000E+00 -47857.05     -10945.09     0.0000000E+00 220100\n" +
                        "  0.0000000E+00 0.0000000E+00 -405546.0     -97656.76     0.0000000E+00 130100\n" +
                        "  0.0000000E+00 0.0000000E+00 -298847.6     -72224.37     0.0000000E+00 040100\n" +
                        "   35689.14      1763.921     0.0000000E+00 0.0000000E+00  10123.54     301100\n" +
                        "  -147710.0     -61893.20     0.0000000E+00 0.0000000E+00  7820.799     211100\n" +
                        "  -693180.2     -195238.1     0.0000000E+00 0.0000000E+00 -21608.11     121100\n" +
                        "  -576112.5     -144715.4     0.0000000E+00 0.0000000E+00 -18822.52     031100\n" +
                        "  0.0000000E+00 0.0000000E+00 -120130.2     -27003.06     0.0000000E+00 202100\n" +
                        "  0.0000000E+00 0.0000000E+00 -382584.1     -89834.37     0.0000000E+00 112100\n" +
                        "  0.0000000E+00 0.0000000E+00 -237843.0     -57055.41     0.0000000E+00 022100\n" +
                        "  -89957.19     -21245.92     0.0000000E+00 0.0000000E+00  28222.50     103100\n" +
                        "  -208647.0     -50852.10     0.0000000E+00 0.0000000E+00  4626.039     013100\n" +
                        "  0.0000000E+00 0.0000000E+00  121520.7      29693.94     0.0000000E+00 004100\n" +
                        "  -1453.018     -343.3659     0.0000000E+00 0.0000000E+00  204.2636     400001\n" +
                        "  -9861.443     -1799.336     0.0000000E+00 0.0000000E+00  727.9361     310001\n" +
                        "  -16482.37     -2464.762     0.0000000E+00 0.0000000E+00  52.97262     220001\n" +
                        "   7060.774      2069.449     0.0000000E+00 0.0000000E+00 -857.7493     130001\n" +
                        "   20135.86      4010.990     0.0000000E+00 0.0000000E+00 -236.0082     040001\n" +
                        "  0.0000000E+00 0.0000000E+00 -10268.87     -3131.516     0.0000000E+00 301001\n" +
                        "  0.0000000E+00 0.0000000E+00 -33287.41     -11075.18     0.0000000E+00 211001\n" +
                        "  0.0000000E+00 0.0000000E+00 -27326.74     -10945.54     0.0000000E+00 121001\n" +
                        "  0.0000000E+00 0.0000000E+00 -2818.376     -2142.118     0.0000000E+00 031001\n" +
                        "  -10759.21     -2890.931     0.0000000E+00 0.0000000E+00  8.932851     202001\n" +
                        "  -34643.38     -8626.066     0.0000000E+00 0.0000000E+00  1125.318     112001\n" +
                        "  -27889.36     -6563.285     0.0000000E+00 0.0000000E+00  817.8075     022001\n" +
                        "  0.0000000E+00 0.0000000E+00 -13362.55     -3294.902     0.0000000E+00 103001\n" +
                        "  0.0000000E+00 0.0000000E+00 -18856.36     -5068.956     0.0000000E+00 013001\n" +
                        "  -3831.579     -1001.535     0.0000000E+00 0.0000000E+00 -541.9239     004001\n" +
                        "   23039.86     -1351.540     0.0000000E+00 0.0000000E+00  9581.195     300200\n" +
                        "  -153452.2     -65871.09     0.0000000E+00 0.0000000E+00  22155.26     210200\n" +
                        "  -654585.2     -191618.9     0.0000000E+00 0.0000000E+00  14218.56     120200\n" +
                        "  -553169.1     -141542.6     0.0000000E+00 0.0000000E+00  311.9994     030200\n" +
                        "  0.0000000E+00 0.0000000E+00 -299519.5     -71057.05     0.0000000E+00 201200\n" +
                        "  0.0000000E+00 0.0000000E+00 -984667.4     -237654.2     0.0000000E+00 111200\n" +
                        "  0.0000000E+00 0.0000000E+00 -680056.3     -165980.9     0.0000000E+00 021200\n" +
                        "  -350327.6     -79933.54     0.0000000E+00 0.0000000E+00  69511.78     102200\n" +
                        "  -760381.5     -178749.4     0.0000000E+00 0.0000000E+00 -3998.790     012200\n" +
                        "  0.0000000E+00 0.0000000E+00  516404.5      127117.5     0.0000000E+00 003200\n" +
                        "  0.0000000E+00 0.0000000E+00 -15460.73     -4995.815     0.0000000E+00 300101\n" +
                        "  0.0000000E+00 0.0000000E+00 -40002.49     -15943.40     0.0000000E+00 210101\n" +
                        "  0.0000000E+00 0.0000000E+00 -13903.35     -12592.68     0.0000000E+00 120101\n" +
                        "  0.0000000E+00 0.0000000E+00  8191.519     -1229.207     0.0000000E+00 030101\n" +
                        "  -28471.27     -7815.702     0.0000000E+00 0.0000000E+00  1712.983     201101\n" +
                        "  -95105.00     -25091.67     0.0000000E+00 0.0000000E+00  6190.968     111101\n" +
                        "  -75579.62     -19704.58     0.0000000E+00 0.0000000E+00  3249.442     021101\n" +
                        "  0.0000000E+00 0.0000000E+00 -84497.49     -21714.57     0.0000000E+00 102101\n" +
                        "  0.0000000E+00 0.0000000E+00 -90411.09     -26086.43     0.0000000E+00 012101\n" +
                        "  -27067.92     -7002.207     0.0000000E+00 0.0000000E+00 -3411.484     003101\n" +
                        "   477.4788      104.4032     0.0000000E+00 0.0000000E+00 -96.76110     300002\n" +
                        "   2989.016      761.5318     0.0000000E+00 0.0000000E+00 -327.4027     210002\n" +
                        "   5340.517      1543.079     0.0000000E+00 0.0000000E+00 -307.2466     120002\n" +
                        "   3172.009      982.9198     0.0000000E+00 0.0000000E+00 -51.75024     030002\n" +
                        "  0.0000000E+00 0.0000000E+00  1823.956      497.1618     0.0000000E+00 201002\n" +
                        "  0.0000000E+00 0.0000000E+00  3177.341      1091.928     0.0000000E+00 111002\n" +
                        "  0.0000000E+00 0.0000000E+00  1009.911      625.7579     0.0000000E+00 021002\n" +
                        "  -654.0420     -221.5708     0.0000000E+00 0.0000000E+00  79.01258     102002\n" +
                        "  -1282.597     -472.8702     0.0000000E+00 0.0000000E+00  77.62765     012002\n" +
                        "  0.0000000E+00 0.0000000E+00  318.5514     -125.4784     0.0000000E+00 003002\n" +
                        "  0.0000000E+00 0.0000000E+00 -234347.9     -56762.93     0.0000000E+00 200300\n" +
                        "  0.0000000E+00 0.0000000E+00 -801440.1     -195599.6     0.0000000E+00 110300\n" +
                        "  0.0000000E+00 0.0000000E+00 -609654.3     -150203.9     0.0000000E+00 020300\n" +
                        "  -560622.4     -126502.1     0.0000000E+00 0.0000000E+00  79793.73     101300\n" +
                        "  -1185912.     -273404.9     0.0000000E+00 0.0000000E+00 -22382.17     011300\n" +
                        "  0.0000000E+00 0.0000000E+00  1093704.      271087.0     0.0000000E+00 002300\n" +
                        "  -20478.58     -5418.909     0.0000000E+00 0.0000000E+00  2712.820     200201\n" +
                        "  -57330.83     -16688.98     0.0000000E+00 0.0000000E+00  7859.661     110201\n" +
                        "  -36166.94     -11765.20     0.0000000E+00 0.0000000E+00  5097.155     020201\n" +
                        "  0.0000000E+00 0.0000000E+00 -144851.0     -39473.97     0.0000000E+00 101201\n" +
                        "  0.0000000E+00 0.0000000E+00 -102187.1     -35008.37     0.0000000E+00 011201\n" +
                        "  -70355.41     -18575.06     0.0000000E+00 0.0000000E+00 -8125.814     002201\n" +
                        "  0.0000000E+00 0.0000000E+00  2893.183      883.5420     0.0000000E+00 200102\n" +
                        "  0.0000000E+00 0.0000000E+00  3853.206      1588.835     0.0000000E+00 110102\n" +
                        "  0.0000000E+00 0.0000000E+00 -1906.402      195.9525     0.0000000E+00 020102\n" +
                        "  -1095.226     -773.4383     0.0000000E+00 0.0000000E+00  257.9446     101102\n" +
                        "  -1528.800     -1398.652     0.0000000E+00 0.0000000E+00  37.78866     011102\n" +
                        "  0.0000000E+00 0.0000000E+00  8687.834      1117.130     0.0000000E+00 002102\n" +
                        "  -73.13073     -5.104070     0.0000000E+00 0.0000000E+00  4.180856     200003\n" +
                        "  -239.5067     -18.75282     0.0000000E+00 0.0000000E+00  10.94674     110003\n" +
                        "  -145.0702      1.759629     0.0000000E+00 0.0000000E+00  9.567265     020003\n" +
                        "  0.0000000E+00 0.0000000E+00 -185.8249     -57.90591     0.0000000E+00 101003\n" +
                        "  0.0000000E+00 0.0000000E+00 -82.67459     -17.10572     0.0000000E+00 011003\n" +
                        "  -58.24218     -22.53623     0.0000000E+00 0.0000000E+00  3.918454     002003\n" +
                        "  -318974.8     -71791.26     0.0000000E+00 0.0000000E+00  36699.71     100400\n" +
                        "  -670822.9     -152655.7     0.0000000E+00 0.0000000E+00 -17625.35     010400\n" +
                        "  0.0000000E+00 0.0000000E+00  1153669.      287848.3     0.0000000E+00 001400\n" +
                        "  0.0000000E+00 0.0000000E+00 -58573.02     -18282.63     0.0000000E+00 100301\n" +
                        "  0.0000000E+00 0.0000000E+00 -5481.556     -9084.503     0.0000000E+00 010301\n" +
                        "  -75010.81     -21416.99     0.0000000E+00 0.0000000E+00 -8483.732     001301\n" +
                        "   900.4834     -453.5623     0.0000000E+00 0.0000000E+00 -2.113959     100202\n" +
                        "   3147.842     -337.0786     0.0000000E+00 0.0000000E+00 -196.9174     010202\n" +
                        "  0.0000000E+00 0.0000000E+00  28316.32      5541.744     0.0000000E+00 001202\n" +
                        "  0.0000000E+00 0.0000000E+00 -114.2262     -66.99477     0.0000000E+00 100103\n" +
                        "  0.0000000E+00 0.0000000E+00 -306.5134     -79.69288     0.0000000E+00 010103\n" +
                        "   174.3694     -4.332120     0.0000000E+00 0.0000000E+00 -3.748263     001103\n" +
                        " -0.1017632    -0.2713288     0.0000000E+00 0.0000000E+00-0.1197481     100004\n" +
                        "   2.092930     0.6861586     0.0000000E+00 0.0000000E+00 0.4213403     010004\n" +
                        "  0.0000000E+00 0.0000000E+00  39.60786      10.78395     0.0000000E+00 001004\n" +
                        "  0.0000000E+00 0.0000000E+00  484530.8      121723.9     0.0000000E+00 000500\n" +
                        "  -23456.52     -8378.232     0.0000000E+00 0.0000000E+00 -3283.599     000401\n" +
                        "  0.0000000E+00 0.0000000E+00  20444.95      4692.881     0.0000000E+00 000302\n" +
                        "   533.9486      102.6490     0.0000000E+00 0.0000000E+00  16.46230     000203\n" +
                        "  0.0000000E+00 0.0000000E+00  14.27853      9.459268     0.0000000E+00 000104\n" +
                        "  0.4824157     0.1003746     0.0000000E+00 0.0000000E+00 0.8600458     000005"
        )));

        return list;
    }

    // cosy 正确的坐标系
    private CosyArbitraryOrder.CosyMapArbitraryOrder cosyMapTest() {
        return CosyArbitraryOrder.readMap(
                "  0.1787251    -0.3692074     0.0000000E+00 0.0000000E+00 0.1680138     100000\n" +
                        "   2.106134      1.244367     0.0000000E+00 0.0000000E+00 0.1994597     010000\n" +
                        "  0.0000000E+00 0.0000000E+00 -1.173974     -2.412454     0.0000000E+00 001000\n" +
                        "  0.0000000E+00 0.0000000E+00-0.6166184     -2.118926     0.0000000E+00 000100\n" +
                        "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00  1.000000     000010\n" +
                        " -0.3182113    -0.2827129     0.0000000E+00 0.0000000E+00 0.4442537     000001\n" +
                        "   5.884125      4.590501     0.0000000E+00 0.0000000E+00-0.1537632     200000\n" +
                        "   9.295337      7.052685     0.0000000E+00 0.0000000E+00 0.2465018     110000\n" +
                        "   3.292079      2.395209     0.0000000E+00 0.0000000E+00-0.7059726     020000\n" +
                        "  0.0000000E+00 0.0000000E+00  2.885618      3.476974     0.0000000E+00 101000\n" +
                        "  0.0000000E+00 0.0000000E+00  4.288296      4.824977     0.0000000E+00 011000\n" +
                        "   2.614055      2.655721     0.0000000E+00 0.0000000E+00 -1.878795     002000\n" +
                        "  0.0000000E+00 0.0000000E+00  3.984002      4.804856     0.0000000E+00 100100\n" +
                        "  0.0000000E+00 0.0000000E+00  5.684783      6.476178     0.0000000E+00 010100\n" +
                        "   7.270030      7.197072     0.0000000E+00 0.0000000E+00 -3.530548     001100\n" +
                        "   1.477294     0.9379569     0.0000000E+00 0.0000000E+00-0.1128497     100001\n" +
                        " -0.2370026E-01 0.8164318     0.0000000E+00 0.0000000E+00 0.4416053E-01 010001\n" +
                        "  0.0000000E+00 0.0000000E+00  1.682843     0.4380811     0.0000000E+00 001001\n" +
                        "   5.050226      4.895456     0.0000000E+00 0.0000000E+00 -2.023583     000200\n" +
                        "  0.0000000E+00 0.0000000E+00  2.413792      2.152919     0.0000000E+00 000101\n" +
                        "  0.2721059     0.8701740E-01 0.0000000E+00 0.0000000E+00-0.3957987     000002\n" +
                        "  -13.97775     -11.75455     0.0000000E+00 0.0000000E+00  1.750466     300000\n" +
                        "  -37.79433     -32.41244     0.0000000E+00 0.0000000E+00 0.1586258E-01 210000\n" +
                        "  -37.06072     -30.85553     0.0000000E+00 0.0000000E+00 -3.228402     120000\n" +
                        "  -10.46732     -10.01069     0.0000000E+00 0.0000000E+00 -1.318912     030000\n" +
                        "  0.0000000E+00 0.0000000E+00 0.6777727    -0.2527649     0.0000000E+00 201000\n" +
                        "  0.0000000E+00 0.0000000E+00  2.107597    -0.6368471     0.0000000E+00 111000\n" +
                        "  0.0000000E+00 0.0000000E+00 -2.244104    -0.8940867     0.0000000E+00 021000\n" +
                        "  -10.52419     -8.035215     0.0000000E+00 0.0000000E+00  5.853600     102000\n" +
                        "  -4.314432     -6.977793     0.0000000E+00 0.0000000E+00  5.420419     012000\n" +
                        "  0.0000000E+00 0.0000000E+00 -12.08810     -5.442975     0.0000000E+00 003000\n" +
                        "  0.0000000E+00 0.0000000E+00 0.1809391    -0.9734414     0.0000000E+00 200100\n" +
                        "  0.0000000E+00 0.0000000E+00  1.178210     -1.458545     0.0000000E+00 110100\n" +
                        "  0.0000000E+00 0.0000000E+00 -3.138437     -2.576489     0.0000000E+00 020100\n" +
                        "  -28.95614     -22.87672     0.0000000E+00 0.0000000E+00  13.81132     101100\n" +
                        "  -16.59841     -19.92517     0.0000000E+00 0.0000000E+00  11.40505     011100\n" +
                        "  0.0000000E+00 0.0000000E+00 -37.51429     -21.05338     0.0000000E+00 002100\n" +
                        "  -7.311883     -3.696445     0.0000000E+00 0.0000000E+00  1.321303     200001\n" +
                        "  -18.43560     -11.70711     0.0000000E+00 0.0000000E+00 0.6707465     110001\n" +
                        "  -10.77139     -6.769308     0.0000000E+00 0.0000000E+00 0.9451422     020001\n" +
                        "  0.0000000E+00 0.0000000E+00 -2.266479    -0.3863809     0.0000000E+00 101001\n" +
                        "  0.0000000E+00 0.0000000E+00 -4.440463     -3.168454     0.0000000E+00 011001\n" +
                        "  -3.874273     -1.424276     0.0000000E+00 0.0000000E+00  3.772915     002001\n" +
                        "  -20.01253     -16.07161     0.0000000E+00 0.0000000E+00  7.882008     100200\n" +
                        "  -13.31251     -14.08591     0.0000000E+00 0.0000000E+00  5.613726     010200\n" +
                        "  0.0000000E+00 0.0000000E+00 -40.49389     -27.60838     0.0000000E+00 001200\n" +
                        "  0.0000000E+00 0.0000000E+00 -4.998584     -2.840173     0.0000000E+00 100101\n" +
                        "  0.0000000E+00 0.0000000E+00 -9.170114     -7.388013     0.0000000E+00 010101\n" +
                        "  -13.24263     -7.318524     0.0000000E+00 0.0000000E+00  10.06025     001101\n" +
                        "  -1.669843    -0.6800157     0.0000000E+00 0.0000000E+00 0.3202861     100002\n" +
                        "  -1.029512     -1.133265     0.0000000E+00 0.0000000E+00-0.6571259E-01 010002\n" +
                        "  0.0000000E+00 0.0000000E+00 -1.561770    -0.2921996     0.0000000E+00 001002\n" +
                        "  0.0000000E+00 0.0000000E+00 -15.89431     -13.56265     0.0000000E+00 000300\n" +
                        "  -11.11515     -7.280218     0.0000000E+00 0.0000000E+00  6.700301     000201\n" +
                        "  0.0000000E+00 0.0000000E+00 -3.228100     -1.782794     0.0000000E+00 000102\n" +
                        " -0.2612802    -0.6198957E-01 0.0000000E+00 0.0000000E+00 0.3693718     000003\n" +
                        "   28.49576      21.82772     0.0000000E+00 0.0000000E+00 -10.42643     400000\n" +
                        "   85.40168      72.30687     0.0000000E+00 0.0000000E+00 -22.94921     310000\n" +
                        "   115.0562      91.69709     0.0000000E+00 0.0000000E+00 -11.55766     220000\n" +
                        "   85.28832      58.34640     0.0000000E+00 0.0000000E+00  6.741389     130000\n" +
                        "   25.37526      15.87516     0.0000000E+00 0.0000000E+00  3.972507     040000\n" +
                        "  0.0000000E+00 0.0000000E+00  4.308610      3.313601     0.0000000E+00 301000\n" +
                        "  0.0000000E+00 0.0000000E+00 -8.073761      9.645098     0.0000000E+00 211000\n" +
                        "  0.0000000E+00 0.0000000E+00 -19.82635      6.651551     0.0000000E+00 121000\n" +
                        "  0.0000000E+00 0.0000000E+00 -3.150359      2.730852     0.0000000E+00 031000\n" +
                        "   53.65799      30.89719     0.0000000E+00 0.0000000E+00 -14.95163     202000\n" +
                        "   78.41899      56.79504     0.0000000E+00 0.0000000E+00 -17.64004     112000\n" +
                        "   29.13973      25.50292     0.0000000E+00 0.0000000E+00 -11.56989     022000\n" +
                        "  0.0000000E+00 0.0000000E+00  41.52131      9.949361     0.0000000E+00 103000\n" +
                        "  0.0000000E+00 0.0000000E+00  47.20752      14.28542     0.0000000E+00 013000\n" +
                        "   13.28058      5.306285     0.0000000E+00 0.0000000E+00 -17.84029     004000\n" +
                        "  0.0000000E+00 0.0000000E+00  5.784247      6.553251     0.0000000E+00 300100\n" +
                        "  0.0000000E+00 0.0000000E+00 -3.266406      17.30221     0.0000000E+00 210100\n" +
                        "  0.0000000E+00 0.0000000E+00 -13.18266      13.54070     0.0000000E+00 120100\n" +
                        "  0.0000000E+00 0.0000000E+00  2.062318      6.795831     0.0000000E+00 030100\n" +
                        "   134.3140      86.06381     0.0000000E+00 0.0000000E+00 -41.78009     201100\n" +
                        "   198.4008      156.0424     0.0000000E+00 0.0000000E+00 -50.54332     111100\n" +
                        "   83.26039      72.49339     0.0000000E+00 0.0000000E+00 -28.39309     021100\n" +
                        "  0.0000000E+00 0.0000000E+00  138.2400      40.88459     0.0000000E+00 102100\n" +
                        "  0.0000000E+00 0.0000000E+00  153.4906      58.49164     0.0000000E+00 012100\n" +
                        "   65.53461      29.61337     0.0000000E+00 0.0000000E+00 -77.81568     003100\n" +
                        "   21.53639      10.08565     0.0000000E+00 0.0000000E+00 -8.025206     300001\n" +
                        "   69.69965      44.93490     0.0000000E+00 0.0000000E+00 -12.97610     210001\n" +
                        "   84.63161      59.11954     0.0000000E+00 0.0000000E+00 -1.009869     120001\n" +
                        "   33.14512      25.24235     0.0000000E+00 0.0000000E+00  3.529540     030001\n" +
                        "  0.0000000E+00 0.0000000E+00  4.552722      1.193676     0.0000000E+00 201001\n" +
                        "  0.0000000E+00 0.0000000E+00 -1.657413      1.506283     0.0000000E+00 111001\n" +
                        "  0.0000000E+00 0.0000000E+00 0.6652931     0.8723669     0.0000000E+00 021001\n" +
                        "   26.11637      10.73789     0.0000000E+00 0.0000000E+00 -13.70063     102001\n" +
                        "   15.54122      13.46054     0.0000000E+00 0.0000000E+00 -13.54588     012001\n" +
                        "  0.0000000E+00 0.0000000E+00  25.31005      5.245353     0.0000000E+00 003001\n" +
                        "   86.31082      59.73192     0.0000000E+00 0.0000000E+00 -28.79706     200200\n" +
                        "   135.8365      111.9399     0.0000000E+00 0.0000000E+00 -35.30130     110200\n" +
                        "   65.79450      55.08214     0.0000000E+00 0.0000000E+00 -17.67598     020200\n" +
                        "  0.0000000E+00 0.0000000E+00  152.8742      56.42002     0.0000000E+00 101200\n" +
                        "  0.0000000E+00 0.0000000E+00  168.4404      81.95481     0.0000000E+00 011200\n" +
                        "   122.7715      62.63539     0.0000000E+00 0.0000000E+00 -131.3529     002200\n" +
                        "  0.0000000E+00 0.0000000E+00  4.883613      2.603192     0.0000000E+00 200101\n" +
                        "  0.0000000E+00 0.0000000E+00 -1.822439      3.797159     0.0000000E+00 110101\n" +
                        "  0.0000000E+00 0.0000000E+00  4.111747      4.456478     0.0000000E+00 020101\n" +
                        "   79.75294      39.65609     0.0000000E+00 0.0000000E+00 -41.22716     101101\n" +
                        "   52.02845      46.22585     0.0000000E+00 0.0000000E+00 -38.58389     011101\n" +
                        "  0.0000000E+00 0.0000000E+00  103.1435      31.52083     0.0000000E+00 002101\n" +
                        "   8.825076      2.922835     0.0000000E+00 0.0000000E+00 -3.177023     200002\n" +
                        "   25.64618      13.08045     0.0000000E+00 0.0000000E+00 -3.770586     110002\n" +
                        "   18.79313      10.28718     0.0000000E+00 0.0000000E+00 -2.128224     020002\n" +
                        "  0.0000000E+00 0.0000000E+00  2.872545     0.1493449     0.0000000E+00 101002\n" +
                        "  0.0000000E+00 0.0000000E+00  4.493343      2.435506     0.0000000E+00 011002\n" +
                        "   5.780491      1.388173     0.0000000E+00 0.0000000E+00 -5.928357     002002\n" +
                        "  0.0000000E+00 0.0000000E+00  58.23486      27.84656     0.0000000E+00 100300\n" +
                        "  0.0000000E+00 0.0000000E+00  65.95547      41.68363     0.0000000E+00 010300\n" +
                        "   106.4823      62.09966     0.0000000E+00 0.0000000E+00 -103.3365     001300\n" +
                        "   61.35532      34.71138     0.0000000E+00 0.0000000E+00 -29.29418     100201\n" +
                        "   44.44278      39.11399     0.0000000E+00 0.0000000E+00 -25.09214     010201\n" +
                        "  0.0000000E+00 0.0000000E+00  134.9826      55.60882     0.0000000E+00 001201\n" +
                        "  0.0000000E+00 0.0000000E+00  6.332603      2.117088     0.0000000E+00 100102\n" +
                        "  0.0000000E+00 0.0000000E+00  11.49691      7.552158     0.0000000E+00 010102\n" +
                        "   21.39746      7.982698     0.0000000E+00 0.0000000E+00 -18.99595     001102\n" +
                        "   1.922907     0.5709543     0.0000000E+00 0.0000000E+00-0.6077450     100003\n" +
                        "   1.812015      1.279354     0.0000000E+00 0.0000000E+00-0.6851891E-01 010003\n" +
                        "  0.0000000E+00 0.0000000E+00  1.581062     0.2232429     0.0000000E+00 001003\n" +
                        "   37.24552      24.92800     0.0000000E+00 0.0000000E+00 -32.08355     000400\n" +
                        "  0.0000000E+00 0.0000000E+00  60.57209      33.20148     0.0000000E+00 000301\n" +
                        "   19.49864      9.379369     0.0000000E+00 0.0000000E+00 -14.53078     000202\n" +
                        "  0.0000000E+00 0.0000000E+00  3.863036      1.587640     0.0000000E+00 000103\n" +
                        "  0.2668596     0.4986079E-01 0.0000000E+00 0.0000000E+00-0.3617554     000004\n" +
                        "  -92.90702     -62.26760     0.0000000E+00 0.0000000E+00  41.76300     500000\n" +
                        "  -314.8358     -262.8340     0.0000000E+00 0.0000000E+00  154.2622     410000\n" +
                        "  -458.9142     -445.8447     0.0000000E+00 0.0000000E+00  236.8582     320000\n" +
                        "  -404.7206     -395.9096     0.0000000E+00 0.0000000E+00  174.7817     230000\n" +
                        "  -236.0630     -193.3003     0.0000000E+00 0.0000000E+00  49.88848     140000\n" +
                        "  -63.77509     -43.11866     0.0000000E+00 0.0000000E+00 0.9965367     050000\n" +
                        "  0.0000000E+00 0.0000000E+00 -40.24089     -6.167316     0.0000000E+00 401000\n" +
                        "  0.0000000E+00 0.0000000E+00 -73.72643     -32.51209     0.0000000E+00 311000\n" +
                        "  0.0000000E+00 0.0000000E+00  4.526392     -58.79020     0.0000000E+00 221000\n" +
                        "  0.0000000E+00 0.0000000E+00  76.65150     -43.61083     0.0000000E+00 131000\n" +
                        "  0.0000000E+00 0.0000000E+00  30.23077     -11.38766     0.0000000E+00 041000\n" +
                        "  -212.3015     -100.5928     0.0000000E+00 0.0000000E+00  69.37015     302000\n" +
                        "  -487.3220     -267.0673     0.0000000E+00 0.0000000E+00  107.2164     212000\n" +
                        "  -418.8899     -245.6549     0.0000000E+00 0.0000000E+00  49.79197     122000\n" +
                        "  -119.5295     -81.03105     0.0000000E+00 0.0000000E+00  14.50182     032000\n" +
                        "  0.0000000E+00 0.0000000E+00 -107.8561     -26.76976     0.0000000E+00 203000\n" +
                        "  0.0000000E+00 0.0000000E+00 -167.7646     -43.74288     0.0000000E+00 113000\n" +
                        "  0.0000000E+00 0.0000000E+00 -109.4941     -22.50756     0.0000000E+00 023000\n" +
                        "  -111.0165     -43.09193     0.0000000E+00 0.0000000E+00  88.37741     104000\n" +
                        "  -51.51718     -39.81743     0.0000000E+00 0.0000000E+00  88.19691     014000\n" +
                        "  0.0000000E+00 0.0000000E+00 -105.9576     -20.51778     0.0000000E+00 005000\n" +
                        "  0.0000000E+00 0.0000000E+00 -42.92522     -13.97405     0.0000000E+00 400100\n" +
                        "  0.0000000E+00 0.0000000E+00 -82.29686     -61.04576     0.0000000E+00 310100\n" +
                        "  0.0000000E+00 0.0000000E+00 -5.918562     -99.36421     0.0000000E+00 220100\n" +
                        "  0.0000000E+00 0.0000000E+00  69.84524     -69.87403     0.0000000E+00 130100\n" +
                        "  0.0000000E+00 0.0000000E+00  25.95999     -18.91876     0.0000000E+00 040100\n" +
                        "  -538.6836     -282.6453     0.0000000E+00 0.0000000E+00  186.5540     301100\n" +
                        "  -1200.526     -743.0669     0.0000000E+00 0.0000000E+00  319.8905     211100\n" +
                        "  -1015.055     -684.4277     0.0000000E+00 0.0000000E+00  186.8378     121100\n" +
                        "  -301.0430     -228.9392     0.0000000E+00 0.0000000E+00  52.69277     031100\n" +
                        "  0.0000000E+00 0.0000000E+00 -403.6239     -111.0430     0.0000000E+00 202100\n" +
                        "  0.0000000E+00 0.0000000E+00 -623.1787     -181.3389     0.0000000E+00 112100\n" +
                        "  0.0000000E+00 0.0000000E+00 -388.8945     -100.8793     0.0000000E+00 022100\n" +
                        "  -566.0742     -239.3431     0.0000000E+00 0.0000000E+00  410.3886     103100\n" +
                        "  -295.3448     -221.1442     0.0000000E+00 0.0000000E+00  398.9599     013100\n" +
                        "  0.0000000E+00 0.0000000E+00 -584.7809     -141.1950     0.0000000E+00 004100\n" +
                        "  -82.21634     -37.88662     0.0000000E+00 0.0000000E+00  39.47479     400001\n" +
                        "  -270.7639     -170.8167     0.0000000E+00 0.0000000E+00  123.3060     310001\n" +
                        "  -366.5834     -269.3721     0.0000000E+00 0.0000000E+00  132.4186     220001\n" +
                        "  -274.5046     -195.7131     0.0000000E+00 0.0000000E+00  42.98195     130001\n" +
                        "  -94.55532     -59.38220     0.0000000E+00 0.0000000E+00 -2.101364     040001\n" +
                        "  0.0000000E+00 0.0000000E+00 -38.55860     -6.098955     0.0000000E+00 301001\n" +
                        "  0.0000000E+00 0.0000000E+00 -55.22160     -27.37790     0.0000000E+00 211001\n" +
                        "  0.0000000E+00 0.0000000E+00  7.020003     -30.76012     0.0000000E+00 121001\n" +
                        "  0.0000000E+00 0.0000000E+00  12.52204     -12.15971     0.0000000E+00 031001\n" +
                        "  -153.8727     -52.73160     0.0000000E+00 0.0000000E+00  58.56983     202001\n" +
                        "  -271.2322     -130.3915     0.0000000E+00 0.0000000E+00  74.71799     112001\n" +
                        "  -125.3780     -74.93057     0.0000000E+00 0.0000000E+00  40.28394     022001\n" +
                        "  0.0000000E+00 0.0000000E+00 -103.7926     -12.01398     0.0000000E+00 103001\n" +
                        "  0.0000000E+00 0.0000000E+00 -129.3794     -24.82655     0.0000000E+00 013001\n" +
                        "  -44.98000     -10.53477     0.0000000E+00 0.0000000E+00  55.82212     004001\n" +
                        "  -344.4485     -195.7675     0.0000000E+00 0.0000000E+00  125.5596     300200\n" +
                        "  -767.4954     -528.1399     0.0000000E+00 0.0000000E+00  231.9077     210200\n" +
                        "  -663.0069     -505.3660     0.0000000E+00 0.0000000E+00  148.4204     120200\n" +
                        "  -212.8075     -176.8161     0.0000000E+00 0.0000000E+00  37.41072     030200\n" +
                        "  0.0000000E+00 0.0000000E+00 -496.0010     -155.0363     0.0000000E+00 201200\n" +
                        "  0.0000000E+00 0.0000000E+00 -755.1991     -251.6104     0.0000000E+00 111200\n" +
                        "  0.0000000E+00 0.0000000E+00 -453.6376     -148.1984     0.0000000E+00 021200\n" +
                        "  -1091.153     -500.8027     0.0000000E+00 0.0000000E+00  724.4510     102200\n" +
                        "  -616.1084     -465.4106     0.0000000E+00 0.0000000E+00  691.4160     012200\n" +
                        "  0.0000000E+00 0.0000000E+00 -1320.454     -389.1606     0.0000000E+00 003200\n" +
                        "  0.0000000E+00 0.0000000E+00 -46.01868     -14.11023     0.0000000E+00 300101\n" +
                        "  0.0000000E+00 0.0000000E+00 -66.22352     -54.48013     0.0000000E+00 210101\n" +
                        "  0.0000000E+00 0.0000000E+00 -1.960720     -59.07777     0.0000000E+00 120101\n" +
                        "  0.0000000E+00 0.0000000E+00 -1.279292     -26.79819     0.0000000E+00 030101\n" +
                        "  -450.5760     -184.7162     0.0000000E+00 0.0000000E+00  172.9628     201101\n" +
                        "  -768.9938     -428.9137     0.0000000E+00 0.0000000E+00  231.7833     111101\n" +
                        "  -360.9623     -242.3158     0.0000000E+00 0.0000000E+00  124.7375     021101\n" +
                        "  0.0000000E+00 0.0000000E+00 -436.2166     -69.70125     0.0000000E+00 102101\n" +
                        "  0.0000000E+00 0.0000000E+00 -527.6195     -129.7655     0.0000000E+00 012101\n" +
                        "  -252.8720     -72.04755     0.0000000E+00 0.0000000E+00  290.4362     003101\n" +
                        "  -37.03351     -11.84751     0.0000000E+00 0.0000000E+00  18.58098     300002\n" +
                        "  -118.3106     -59.13430     0.0000000E+00 0.0000000E+00  43.14573     210002\n" +
                        "  -146.1187     -89.52178     0.0000000E+00 0.0000000E+00  24.23019     120002\n" +
                        "  -64.45412     -44.28016     0.0000000E+00 0.0000000E+00 -1.464092     030002\n" +
                        "  0.0000000E+00 0.0000000E+00 -15.89175     -1.914055     0.0000000E+00 201002\n" +
                        "  0.0000000E+00 0.0000000E+00 -12.98408     -3.998821     0.0000000E+00 111002\n" +
                        "  0.0000000E+00 0.0000000E+00 -2.743882     -1.916921     0.0000000E+00 021002\n" +
                        "  -48.25141     -13.10355     0.0000000E+00 0.0000000E+00  26.50856     102002\n" +
                        "  -36.05348     -20.09286     0.0000000E+00 0.0000000E+00  25.56394     012002\n" +
                        "  0.0000000E+00 0.0000000E+00 -41.33462     -5.611987     0.0000000E+00 003002\n" +
                        "  0.0000000E+00 0.0000000E+00 -199.9031     -72.74546     0.0000000E+00 200300\n" +
                        "  0.0000000E+00 0.0000000E+00 -297.8289     -117.3224     0.0000000E+00 110300\n" +
                        "  0.0000000E+00 0.0000000E+00 -176.6290     -74.30464     0.0000000E+00 020300\n" +
                        "  -952.8080     -475.4199     0.0000000E+00 0.0000000E+00  584.0541     101300\n" +
                        "  -569.0365     -446.9755     0.0000000E+00 0.0000000E+00  551.3264     011300\n" +
                        "  0.0000000E+00 0.0000000E+00 -1540.455     -546.1732     0.0000000E+00 002300\n" +
                        "  -327.2383     -154.2363     0.0000000E+00 0.0000000E+00  128.4101     200201\n" +
                        "  -566.8664     -354.0100     0.0000000E+00 0.0000000E+00  178.4571     110201\n" +
                        "  -287.5682     -204.9130     0.0000000E+00 0.0000000E+00  93.39159     020201\n" +
                        "  0.0000000E+00 0.0000000E+00 -583.4027     -123.3741     0.0000000E+00 101201\n" +
                        "  0.0000000E+00 0.0000000E+00 -690.6444     -218.7998     0.0000000E+00 011201\n" +
                        "  -531.1499     -179.3949     0.0000000E+00 0.0000000E+00  562.7935     002201\n" +
                        "  0.0000000E+00 0.0000000E+00 -20.07047     -4.609543     0.0000000E+00 200102\n" +
                        "  0.0000000E+00 0.0000000E+00 -13.47644     -9.125259     0.0000000E+00 110102\n" +
                        "  0.0000000E+00 0.0000000E+00 -7.837848     -7.760834     0.0000000E+00 020102\n" +
                        "  -162.3793     -56.32148     0.0000000E+00 0.0000000E+00  88.30765     101102\n" +
                        "  -121.7110     -78.34932     0.0000000E+00 0.0000000E+00  85.11635     011102\n" +
                        "  0.0000000E+00 0.0000000E+00 -197.7621     -40.39449     0.0000000E+00 002102\n" +
                        "  -11.63915     -2.759957     0.0000000E+00 0.0000000E+00  5.745891     200003\n" +
                        "  -34.10928     -14.16160     0.0000000E+00 0.0000000E+00  9.196623     110003\n" +
                        "  -27.57817     -13.34046     0.0000000E+00 0.0000000E+00  4.871976     020003\n" +
                        "  0.0000000E+00 0.0000000E+00 -4.346029    -0.5633970E-01 0.0000000E+00 101003\n" +
                        "  0.0000000E+00 0.0000000E+00 -5.274173     -2.033301     0.0000000E+00 011003\n" +
                        "  -8.254124     -1.426647     0.0000000E+00 0.0000000E+00  8.562119     002003\n" +
                        "  -322.1660     -175.5249     0.0000000E+00 0.0000000E+00  183.2094     100400\n" +
                        "  -202.4837     -167.6745     0.0000000E+00 0.0000000E+00  171.2374     010400\n" +
                        "  0.0000000E+00 0.0000000E+00 -931.8299     -394.6710     0.0000000E+00 001400\n" +
                        "  0.0000000E+00 0.0000000E+00 -256.0637     -72.41693     0.0000000E+00 100301\n" +
                        "  0.0000000E+00 0.0000000E+00 -303.5994     -127.1244     0.0000000E+00 010301\n" +
                        "  -504.1817     -200.9617     0.0000000E+00 0.0000000E+00  493.4682     001301\n" +
                        "  -135.1263     -56.26693     0.0000000E+00 0.0000000E+00  70.55117     100202\n" +
                        "  -106.6293     -74.48851     0.0000000E+00 0.0000000E+00  65.32797     010202\n" +
                        "  0.0000000E+00 0.0000000E+00 -297.0028     -84.28592     0.0000000E+00 001202\n" +
                        "  0.0000000E+00 0.0000000E+00 -8.896834     -1.747140     0.0000000E+00 100103\n" +
                        "  0.0000000E+00 0.0000000E+00 -14.09725     -7.609241     0.0000000E+00 010103\n" +
                        "  -32.47531     -8.828584     0.0000000E+00 0.0000000E+00  30.77801     001103\n" +
                        "  -2.294701    -0.5166055     0.0000000E+00 0.0000000E+00 0.9821117     100004\n" +
                        "  -2.579807     -1.377768     0.0000000E+00 0.0000000E+00 0.3540176     010004\n" +
                        "  0.0000000E+00 0.0000000E+00 -1.676901    -0.1810007     0.0000000E+00 001004\n" +
                        "  0.0000000E+00 0.0000000E+00 -234.5751     -119.1215     0.0000000E+00 000500\n" +
                        "  -187.1248     -88.39792     0.0000000E+00 0.0000000E+00  167.5472     000401\n" +
                        "  0.0000000E+00 0.0000000E+00 -147.9917     -57.33649     0.0000000E+00 000302\n" +
                        "  -31.27158     -11.48276     0.0000000E+00 0.0000000E+00  26.11000     000203\n" +
                        "  0.0000000E+00 0.0000000E+00 -4.493808     -1.459959     0.0000000E+00 000104\n" +
                        " -0.2832402    -0.4304278E-01 0.0000000E+00 0.0000000E+00 0.3680534     000005"
        );
    }

    // COSY切片后的map
    private CosyArbitraryOrder.CosyMapArbitraryOrder cosyMapCut(){
        return CosyArbitraryOrder.readMap(
                "  0.8047855      1.352366     0.0000000E+00 0.0000000E+00-0.3705115E-02 100000\n" +
                        " -0.1541378     0.9835530     0.0000000E+00 0.0000000E+00-0.5079161E-02 010000\n" +
                        "  0.0000000E+00 0.0000000E+00 -1.664560     -1.115462     0.0000000E+00 001000\n" +
                        "  0.0000000E+00 0.0000000E+00 0.2654553    -0.4228712     0.0000000E+00 000100\n" +
                        "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00  1.000000     000010\n" +
                        " -0.4658734E-02-0.3224710E-02 0.0000000E+00 0.0000000E+00 0.8037211     000001\n" +
                        "  -16.17386     -6.458773     0.0000000E+00 0.0000000E+00 -2.993678     200000\n" +
                        "  -36.52662     -19.37236     0.0000000E+00 0.0000000E+00 -5.482151     110000\n" +
                        "  -26.39477     -20.17863     0.0000000E+00 0.0000000E+00 -2.870772     020000\n" +
                        "  0.0000000E+00 0.0000000E+00  15.75563      7.307356     0.0000000E+00 101000\n" +
                        "  0.0000000E+00 0.0000000E+00 -23.48807     -13.74153     0.0000000E+00 011000\n" +
                        "  0.9214885      4.910408     0.0000000E+00 0.0000000E+00 -2.150729     002000\n" +
                        "  0.0000000E+00 0.0000000E+00 -16.99165     -16.55447     0.0000000E+00 100100\n" +
                        "  0.0000000E+00 0.0000000E+00 -123.7844     -74.79249     0.0000000E+00 010100\n" +
                        "   9.603188      26.82629     0.0000000E+00 0.0000000E+00 -6.378638     001100\n" +
                        "  -5.305869     -1.531162     0.0000000E+00 0.0000000E+00-0.3330793     100001\n" +
                        "  -5.475785     -2.423818     0.0000000E+00 0.0000000E+00-0.2106457     010001\n" +
                        "  0.0000000E+00 0.0000000E+00  11.59242      5.208134     0.0000000E+00 001001\n" +
                        "   29.94477      43.12507     0.0000000E+00 0.0000000E+00 -6.995124     000200\n" +
                        "  0.0000000E+00 0.0000000E+00  24.60852      12.71521     0.0000000E+00 000101\n" +
                        " -0.1071019     0.2076372E-01 0.0000000E+00 0.0000000E+00-0.7051163     000002\n" +
                        "   85.41944      48.63300     0.0000000E+00 0.0000000E+00  14.46918     300000\n" +
                        "   452.7396      227.2907     0.0000000E+00 0.0000000E+00  64.70478     210000\n" +
                        "   790.4894      344.1004     0.0000000E+00 0.0000000E+00  116.8537     120000\n" +
                        "   405.5899      150.5725     0.0000000E+00 0.0000000E+00  65.86299     030000\n" +
                        "  0.0000000E+00 0.0000000E+00 -354.4428     -211.0892     0.0000000E+00 201000\n" +
                        "  0.0000000E+00 0.0000000E+00 -816.1865     -429.3019     0.0000000E+00 111000\n" +
                        "  0.0000000E+00 0.0000000E+00 -584.1926     -269.9019     0.0000000E+00 021000\n" +
                        "  -48.99269      45.58285     0.0000000E+00 0.0000000E+00 -19.86404     102000\n" +
                        "  -29.07722      58.03871     0.0000000E+00 0.0000000E+00 -45.83673     012000\n" +
                        "  0.0000000E+00 0.0000000E+00 -95.97052     -44.91418     0.0000000E+00 003000\n" +
                        "  0.0000000E+00 0.0000000E+00 -675.4636     -411.0379     0.0000000E+00 200100\n" +
                        "  0.0000000E+00 0.0000000E+00 -947.4439     -430.2998     0.0000000E+00 110100\n" +
                        "  0.0000000E+00 0.0000000E+00 -271.6200      42.92196     0.0000000E+00 020100\n" +
                        "  -216.5941      218.2325     0.0000000E+00 0.0000000E+00 -152.8142     101100\n" +
                        "  -363.7950      144.9043     0.0000000E+00 0.0000000E+00 -231.6081     011100\n" +
                        "  0.0000000E+00 0.0000000E+00 -527.5722     -285.8651     0.0000000E+00 002100\n" +
                        "   17.77730    -0.2612315     0.0000000E+00 0.0000000E+00  7.184983     200001\n" +
                        "   78.94593      21.55131     0.0000000E+00 0.0000000E+00  14.55966     110001\n" +
                        "   97.84047      42.18950     0.0000000E+00 0.0000000E+00  9.595516     020001\n" +
                        "  0.0000000E+00 0.0000000E+00 -135.1035     -85.94259     0.0000000E+00 101001\n" +
                        "  0.0000000E+00 0.0000000E+00 -192.1812     -131.5888     0.0000000E+00 011001\n" +
                        "  -10.25740      10.28155     0.0000000E+00 0.0000000E+00  4.878218     002001\n" +
                        "  -375.0272      132.0400     0.0000000E+00 0.0000000E+00 -184.3861     100200\n" +
                        "  -949.9835     -196.1778     0.0000000E+00 0.0000000E+00 -204.5458     010200\n" +
                        "  0.0000000E+00 0.0000000E+00 -1210.432     -760.4499     0.0000000E+00 001200\n" +
                        "  0.0000000E+00 0.0000000E+00 -319.0393     -215.5701     0.0000000E+00 100101\n" +
                        "  0.0000000E+00 0.0000000E+00 -296.7805     -238.9131     0.0000000E+00 010101\n" +
                        "   21.63162      70.11605     0.0000000E+00 0.0000000E+00  9.211943     001101\n" +
                        "   2.497015     -2.105415     0.0000000E+00 0.0000000E+00 0.8604528     100002\n" +
                        "   4.284387     -1.010842     0.0000000E+00 0.0000000E+00 0.5782792     010002\n" +
                        "  0.0000000E+00 0.0000000E+00  1.408429      4.577988     0.0000000E+00 001002\n" +
                        "  0.0000000E+00 0.0000000E+00 -1108.344     -755.6672     0.0000000E+00 000300\n" +
                        "   53.01144      78.18715     0.0000000E+00 0.0000000E+00 0.8832860     000201\n" +
                        "  0.0000000E+00 0.0000000E+00 -23.86395     -5.523713     0.0000000E+00 000102\n" +
                        " -0.3500050E-01-0.1806994     0.0000000E+00 0.0000000E+00 0.6505747     000003\n" +
                        "   35.85027      124.8954     0.0000000E+00 0.0000000E+00 -153.8693     400000\n" +
                        "  -874.4372     -324.8379     0.0000000E+00 0.0000000E+00 -746.2166     310000\n" +
                        "  -4577.512     -2921.772     0.0000000E+00 0.0000000E+00 -1422.117     220000\n" +
                        "  -7600.106     -5067.265     0.0000000E+00 0.0000000E+00 -1238.861     130000\n" +
                        "  -4396.257     -2752.334     0.0000000E+00 0.0000000E+00 -495.5265     040000\n" +
                        "  0.0000000E+00 0.0000000E+00  942.5075      629.2137     0.0000000E+00 301000\n" +
                        "  0.0000000E+00 0.0000000E+00  9484.523      6522.422     0.0000000E+00 211000\n" +
                        "  0.0000000E+00 0.0000000E+00  18217.80      12393.54     0.0000000E+00 121000\n" +
                        "  0.0000000E+00 0.0000000E+00  8593.393      5735.928     0.0000000E+00 031000\n" +
                        "  -110.3712      586.8191     0.0000000E+00 0.0000000E+00 -452.6989     202000\n" +
                        "   185.0675      1118.511     0.0000000E+00 0.0000000E+00 -677.6042     112000\n" +
                        "   1945.176      1268.325     0.0000000E+00 0.0000000E+00 -396.9497     022000\n" +
                        "  0.0000000E+00 0.0000000E+00  892.0291      536.4904     0.0000000E+00 103000\n" +
                        "  0.0000000E+00 0.0000000E+00  3174.295      2098.477     0.0000000E+00 013000\n" +
                        "  -274.2763     -247.7202     0.0000000E+00 0.0000000E+00 -71.60910     004000\n" +
                        "  0.0000000E+00 0.0000000E+00  3160.081      2222.755     0.0000000E+00 300100\n" +
                        "  0.0000000E+00 0.0000000E+00  22550.30      15935.25     0.0000000E+00 210100\n" +
                        "  0.0000000E+00 0.0000000E+00  39029.72      27202.22     0.0000000E+00 120100\n" +
                        "  0.0000000E+00 0.0000000E+00  16310.80      11162.39     0.0000000E+00 030100\n" +
                        "  -1563.793      1173.428     0.0000000E+00 0.0000000E+00 -1074.611     201100\n" +
                        "  -3841.277      990.7035     0.0000000E+00 0.0000000E+00 -1022.115     111100\n" +
                        "   5884.688      3511.696     0.0000000E+00 0.0000000E+00 -611.5734     021100\n" +
                        "  0.0000000E+00 0.0000000E+00 -550.3750     -792.7901     0.0000000E+00 102100\n" +
                        "  0.0000000E+00 0.0000000E+00  15252.22      10092.48     0.0000000E+00 012100\n" +
                        "  -3485.802     -2228.659     0.0000000E+00 0.0000000E+00 -601.7588     003100\n" +
                        "   98.96394      158.5642     0.0000000E+00 0.0000000E+00 -70.45477     300001\n" +
                        "  -47.06872      361.8259     0.0000000E+00 0.0000000E+00 -292.2982     210001\n" +
                        "  -875.8179      132.1510     0.0000000E+00 0.0000000E+00 -500.8715     120001\n" +
                        "  -729.3947     -88.25712     0.0000000E+00 0.0000000E+00 -267.4305     030001\n" +
                        "  0.0000000E+00 0.0000000E+00 -639.0256     -552.1769     0.0000000E+00 201001\n" +
                        "  0.0000000E+00 0.0000000E+00  491.6633      76.59750     0.0000000E+00 111001\n" +
                        "  0.0000000E+00 0.0000000E+00  2020.714      1134.847     0.0000000E+00 021001\n" +
                        "  -38.21205      199.4139     0.0000000E+00 0.0000000E+00 -190.3464     102001\n" +
                        "  -722.7476     -193.1344     0.0000000E+00 0.0000000E+00 -81.29355     012001\n" +
                        "  0.0000000E+00 0.0000000E+00 -536.6704     -422.9075     0.0000000E+00 003001\n" +
                        "  -2014.895      620.2440     0.0000000E+00 0.0000000E+00 -517.9900     200200\n" +
                        "  -4470.523      817.4325     0.0000000E+00 0.0000000E+00 -522.4854     110200\n" +
                        "   8292.098      5396.241     0.0000000E+00 0.0000000E+00 -1001.004     020200\n" +
                        "  0.0000000E+00 0.0000000E+00 -13417.99     -10100.66     0.0000000E+00 101200\n" +
                        "  0.0000000E+00 0.0000000E+00  24418.86      15856.91     0.0000000E+00 011200\n" +
                        "  -13111.95     -6770.026     0.0000000E+00 0.0000000E+00 -2200.755     002200\n" +
                        "  0.0000000E+00 0.0000000E+00  295.5517     -61.04298     0.0000000E+00 200101\n" +
                        "  0.0000000E+00 0.0000000E+00  3362.618      1892.955     0.0000000E+00 110101\n" +
                        "  0.0000000E+00 0.0000000E+00  4426.570      2719.227     0.0000000E+00 020101\n" +
                        "  -388.5990      91.40350     0.0000000E+00 0.0000000E+00 -333.3757     101101\n" +
                        "  -4059.425     -2189.234     0.0000000E+00 0.0000000E+00  364.7933     011101\n" +
                        "  0.0000000E+00 0.0000000E+00 -4073.456     -3165.062     0.0000000E+00 002101\n" +
                        "   26.68544      42.41710     0.0000000E+00 0.0000000E+00 -17.32726     200002\n" +
                        "   3.708099      73.54540     0.0000000E+00 0.0000000E+00 -37.07500     110002\n" +
                        "  -104.5938      9.653978     0.0000000E+00 0.0000000E+00 -28.84159     020002\n" +
                        "  0.0000000E+00 0.0000000E+00 -147.3968     -145.5565     0.0000000E+00 101002\n" +
                        "  0.0000000E+00 0.0000000E+00  54.64910     -35.22294     0.0000000E+00 011002\n" +
                        "   92.50442      53.24573     0.0000000E+00 0.0000000E+00 -21.68646     002002\n" +
                        "  0.0000000E+00 0.0000000E+00 -16228.86     -11885.15     0.0000000E+00 100300\n" +
                        "  0.0000000E+00 0.0000000E+00  14458.81      9001.537     0.0000000E+00 010300\n" +
                        "  -20119.09     -8800.038     0.0000000E+00 0.0000000E+00 -3696.829     001300\n" +
                        "  -470.1535     -600.3406     0.0000000E+00 0.0000000E+00  235.3737     100201\n" +
                        "  -4011.883     -2998.214     0.0000000E+00 0.0000000E+00  1050.428     010201\n" +
                        "  0.0000000E+00 0.0000000E+00 -10008.09     -7607.246     0.0000000E+00 001201\n" +
                        "  0.0000000E+00 0.0000000E+00  155.9294     -14.17986     0.0000000E+00 100102\n" +
                        "  0.0000000E+00 0.0000000E+00  486.5271      217.9401     0.0000000E+00 010102\n" +
                        "   353.8458      179.9959     0.0000000E+00 0.0000000E+00 -88.83720     001102\n" +
                        "   3.163746      6.100501     0.0000000E+00 0.0000000E+00 -1.733011     100003\n" +
                        "   2.322744      5.898215     0.0000000E+00 0.0000000E+00 -1.179370     010003\n" +
                        "  0.0000000E+00 0.0000000E+00 -30.27596     -19.11793     0.0000000E+00 001003\n" +
                        "  -11209.58     -4310.946     0.0000000E+00 0.0000000E+00 -2286.047     000400\n" +
                        "  0.0000000E+00 0.0000000E+00 -7285.098     -5495.625     0.0000000E+00 000301\n" +
                        "   273.1501      111.7461     0.0000000E+00 0.0000000E+00 -70.54683     000202\n" +
                        "  0.0000000E+00 0.0000000E+00 -17.88259     -21.96688     0.0000000E+00 000103\n" +
                        "  0.3301776     0.3518461     0.0000000E+00 0.0000000E+00-0.6350031     000004\n" +
                        "  -534.1636     -1315.084     0.0000000E+00 0.0000000E+00  703.4617     500000\n" +
                        "  -3165.492     -9336.090     0.0000000E+00 0.0000000E+00  6905.857     410000\n" +
                        "  -3848.629     -24474.34     0.0000000E+00 0.0000000E+00  24817.58     320000\n" +
                        "   8973.189     -24498.55     0.0000000E+00 0.0000000E+00  41237.13     230000\n" +
                        "   22356.27     -3337.711     0.0000000E+00 0.0000000E+00  32116.86     140000\n" +
                        "   12296.72      5615.477     0.0000000E+00 0.0000000E+00  8941.483     050000\n" +
                        "  0.0000000E+00 0.0000000E+00  11754.54      9075.542     0.0000000E+00 401000\n" +
                        "  0.0000000E+00 0.0000000E+00  48447.44      37473.75     0.0000000E+00 311000\n" +
                        "  0.0000000E+00 0.0000000E+00  35102.70      29632.88     0.0000000E+00 221000\n" +
                        "  0.0000000E+00 0.0000000E+00 -74305.84     -49876.51     0.0000000E+00 131000\n" +
                        "  0.0000000E+00 0.0000000E+00 -83126.15     -57628.33     0.0000000E+00 041000\n" +
                        "   581.2302      1438.367     0.0000000E+00 0.0000000E+00  962.2492     302000\n" +
                        "   1940.815      1064.966     0.0000000E+00 0.0000000E+00  3943.010     212000\n" +
                        "   25146.67      3356.399     0.0000000E+00 0.0000000E+00  6853.554     122000\n" +
                        "   28760.95      6781.932     0.0000000E+00 0.0000000E+00  2417.824     032000\n" +
                        "  0.0000000E+00 0.0000000E+00 -1298.496     -1348.847     0.0000000E+00 203000\n" +
                        "  0.0000000E+00 0.0000000E+00  40912.87      26512.65     0.0000000E+00 113000\n" +
                        "  0.0000000E+00 0.0000000E+00  40259.87      26609.71     0.0000000E+00 023000\n" +
                        "  -2062.381     -294.6341     0.0000000E+00 0.0000000E+00  908.6607     104000\n" +
                        "   7259.286      3974.640     0.0000000E+00 0.0000000E+00  1413.155     014000\n" +
                        "  0.0000000E+00 0.0000000E+00  3755.877      3108.131     0.0000000E+00 005000\n" +
                        "  0.0000000E+00 0.0000000E+00  11056.00      9057.550     0.0000000E+00 400100\n" +
                        "  0.0000000E+00 0.0000000E+00  29232.85      23377.69     0.0000000E+00 310100\n" +
                        "  0.0000000E+00 0.0000000E+00 -72145.95     -52553.25     0.0000000E+00 220100\n" +
                        "  0.0000000E+00 0.0000000E+00 -283583.6     -208333.9     0.0000000E+00 130100\n" +
                        "  0.0000000E+00 0.0000000E+00 -217563.6     -157020.4     0.0000000E+00 040100\n" +
                        "   12138.04      17533.22     0.0000000E+00 0.0000000E+00 -1876.015     301100\n" +
                        "   74262.09      67902.22     0.0000000E+00 0.0000000E+00 -14369.10     211100\n" +
                        "   221556.5      113477.4     0.0000000E+00 0.0000000E+00 -17452.43     121100\n" +
                        "   186945.0      76175.32     0.0000000E+00 0.0000000E+00 -8062.336     031100\n" +
                        "  0.0000000E+00 0.0000000E+00 -28075.85     -22853.82     0.0000000E+00 202100\n" +
                        "  0.0000000E+00 0.0000000E+00  270855.6      179389.2     0.0000000E+00 112100\n" +
                        "  0.0000000E+00 0.0000000E+00  317488.0      216026.1     0.0000000E+00 022100\n" +
                        "  -10852.71      10760.93     0.0000000E+00 0.0000000E+00 -1238.540     103100\n" +
                        "   63466.53      47840.02     0.0000000E+00 0.0000000E+00  1136.157     013100\n" +
                        "  0.0000000E+00 0.0000000E+00  38441.13      31384.18     0.0000000E+00 004100\n" +
                        "  -1334.055     -1514.828     0.0000000E+00 0.0000000E+00  426.8312     400001\n" +
                        "  -5547.621     -7115.798     0.0000000E+00 0.0000000E+00  2951.233     310001\n" +
                        "  -5126.174     -11479.90     0.0000000E+00 0.0000000E+00  7731.181     220001\n" +
                        "   6212.326     -4926.671     0.0000000E+00 0.0000000E+00  8635.276     130001\n" +
                        "   9285.802      1338.892     0.0000000E+00 0.0000000E+00  4008.798     040001\n" +
                        "  0.0000000E+00 0.0000000E+00  9780.419      7606.427     0.0000000E+00 301001\n" +
                        "  0.0000000E+00 0.0000000E+00  29521.96      25626.53     0.0000000E+00 211001\n" +
                        "  0.0000000E+00 0.0000000E+00  25411.20      26475.79     0.0000000E+00 121001\n" +
                        "  0.0000000E+00 0.0000000E+00  4789.265      7259.585     0.0000000E+00 031001\n" +
                        "  -2113.735     -1756.522     0.0000000E+00 0.0000000E+00  1511.202     202001\n" +
                        "  -13408.85     -7844.386     0.0000000E+00 0.0000000E+00  3244.680     112001\n" +
                        "  -10654.17     -5314.555     0.0000000E+00 0.0000000E+00  1934.080     022001\n" +
                        "  0.0000000E+00 0.0000000E+00 -11032.71     -7497.528     0.0000000E+00 103001\n" +
                        "  0.0000000E+00 0.0000000E+00 -3382.580     -1444.978     0.0000000E+00 013001\n" +
                        "  -3371.532     -1394.628     0.0000000E+00 0.0000000E+00 -221.3822     004001\n" +
                        "   20717.77      28624.62     0.0000000E+00 0.0000000E+00 -10653.28     300200\n" +
                        "   128584.9      118886.5     0.0000000E+00 0.0000000E+00 -48427.00     210200\n" +
                        "   308347.5      178103.4     0.0000000E+00 0.0000000E+00 -53531.58     120200\n" +
                        "   229522.8      96941.02     0.0000000E+00 0.0000000E+00 -13542.04     030200\n" +
                        "  0.0000000E+00 0.0000000E+00 -53315.50     -43061.18     0.0000000E+00 201200\n" +
                        "  0.0000000E+00 0.0000000E+00  657375.0      450973.4     0.0000000E+00 111200\n" +
                        "  0.0000000E+00 0.0000000E+00  813392.5      568687.5     0.0000000E+00 021200\n" +
                        "  -21356.12      59165.77     0.0000000E+00 0.0000000E+00 -21275.67     102200\n" +
                        "   188067.2      174775.3     0.0000000E+00 0.0000000E+00 -21155.40     012200\n" +
                        "  0.0000000E+00 0.0000000E+00  129908.9      107009.4     0.0000000E+00 003200\n" +
                        "  0.0000000E+00 0.0000000E+00  8977.402      8582.466     0.0000000E+00 300101\n" +
                        "  0.0000000E+00 0.0000000E+00  7140.743      16364.95     0.0000000E+00 210101\n" +
                        "  0.0000000E+00 0.0000000E+00 -29280.38     -2784.147     0.0000000E+00 120101\n" +
                        "  0.0000000E+00 0.0000000E+00 -22578.12     -8887.426     0.0000000E+00 030101\n" +
                        "  -7620.065     -6801.265     0.0000000E+00 0.0000000E+00  6320.556     201101\n" +
                        "  -35872.75     -18133.91     0.0000000E+00 0.0000000E+00  6113.053     111101\n" +
                        "  -30956.11     -8200.479     0.0000000E+00 0.0000000E+00 -1351.465     021101\n" +
                        "  0.0000000E+00 0.0000000E+00 -75910.46     -54081.24     0.0000000E+00 102101\n" +
                        "  0.0000000E+00 0.0000000E+00 -35745.38     -21612.53     0.0000000E+00 012101\n" +
                        "  -21763.73     -8118.643     0.0000000E+00 0.0000000E+00 -2157.872     003101\n" +
                        "  -581.6573     -558.9224     0.0000000E+00 0.0000000E+00  127.4396     300002\n" +
                        "  -1837.678     -1965.218     0.0000000E+00 0.0000000E+00  612.5055     210002\n" +
                        "  -1416.342     -2485.504     0.0000000E+00 0.0000000E+00  1244.336     120002\n" +
                        "  -48.53750     -893.5269     0.0000000E+00 0.0000000E+00  705.8111     030002\n" +
                        "  0.0000000E+00 0.0000000E+00  3666.999      2349.566     0.0000000E+00 201002\n" +
                        "  0.0000000E+00 0.0000000E+00  6249.509      4596.326     0.0000000E+00 111002\n" +
                        "  0.0000000E+00 0.0000000E+00  1205.287      1674.058     0.0000000E+00 021002\n" +
                        "   29.47932     -545.5920     0.0000000E+00 0.0000000E+00  531.4709     102002\n" +
                        "  -984.3003     -1529.007     0.0000000E+00 0.0000000E+00  804.8500     012002\n" +
                        "  0.0000000E+00 0.0000000E+00 -1660.405     -1349.417     0.0000000E+00 003002\n" +
                        "  0.0000000E+00 0.0000000E+00 -16136.91     -13748.23     0.0000000E+00 200300\n" +
                        "  0.0000000E+00 0.0000000E+00  530402.3      375075.0     0.0000000E+00 110300\n" +
                        "  0.0000000E+00 0.0000000E+00  659833.1      471368.8     0.0000000E+00 020300\n" +
                        "  -19043.37      102005.1     0.0000000E+00 0.0000000E+00 -42637.86     101300\n" +
                        "   238290.3      265507.9     0.0000000E+00 0.0000000E+00 -56826.80     011300\n" +
                        "  0.0000000E+00 0.0000000E+00  157042.8      137826.4     0.0000000E+00 002300\n" +
                        "  -5482.961     -3886.517     0.0000000E+00 0.0000000E+00  3262.718     200201\n" +
                        "  -19428.62     -2524.936     0.0000000E+00 0.0000000E+00 -6893.535     110201\n" +
                        "  -28624.63      574.6143     0.0000000E+00 0.0000000E+00 -12037.92     020201\n" +
                        "  0.0000000E+00 0.0000000E+00 -128771.9     -97882.49     0.0000000E+00 101201\n" +
                        "  0.0000000E+00 0.0000000E+00 -75297.01     -49777.06     0.0000000E+00 011201\n" +
                        "  -47593.42     -17542.27     0.0000000E+00 0.0000000E+00 -5058.733     002201\n" +
                        "  0.0000000E+00 0.0000000E+00  4211.042      3293.276     0.0000000E+00 200102\n" +
                        "  0.0000000E+00 0.0000000E+00  3149.594      3916.347     0.0000000E+00 110102\n" +
                        "  0.0000000E+00 0.0000000E+00 -5196.056     -1468.342     0.0000000E+00 020102\n" +
                        "  -394.1959     -2657.632     0.0000000E+00 0.0000000E+00  2778.820     101102\n" +
                        "   1008.946     -3477.343     0.0000000E+00 0.0000000E+00  2825.910     011102\n" +
                        "  0.0000000E+00 0.0000000E+00 -7771.389     -6555.594     0.0000000E+00 002102\n" +
                        "  -116.7527     -109.6363     0.0000000E+00 0.0000000E+00  27.50862     200003\n" +
                        "  -236.4551     -241.0788     0.0000000E+00 0.0000000E+00  58.47422     110003\n" +
                        "  -25.67496     -152.3207     0.0000000E+00 0.0000000E+00  57.63344     020003\n" +
                        "  0.0000000E+00 0.0000000E+00  672.6718      401.0092     0.0000000E+00 101003\n" +
                        "  0.0000000E+00 0.0000000E+00  498.8256      370.0204     0.0000000E+00 011003\n" +
                        "   14.75354      21.28229     0.0000000E+00 0.0000000E+00  4.391579     002003\n" +
                        "  -7181.160      58329.59     0.0000000E+00 0.0000000E+00 -25479.25     100400\n" +
                        "   110796.2      148977.4     0.0000000E+00 0.0000000E+00 -42342.45     010400\n" +
                        "  0.0000000E+00 0.0000000E+00  3992.859      25667.77     0.0000000E+00 001400\n" +
                        "  0.0000000E+00 0.0000000E+00 -50554.68     -43926.64     0.0000000E+00 100301\n" +
                        "  0.0000000E+00 0.0000000E+00 -48078.42     -32413.21     0.0000000E+00 010301\n" +
                        "  -39319.77     -18455.16     0.0000000E+00 0.0000000E+00 -724.8699     001301\n" +
                        "  -386.0593     -1910.524     0.0000000E+00 0.0000000E+00  2290.886     100202\n" +
                        "   5754.960      418.6744     0.0000000E+00 0.0000000E+00  1102.420     010202\n" +
                        "  0.0000000E+00 0.0000000E+00 -4700.660     -6010.974     0.0000000E+00 001202\n" +
                        "  0.0000000E+00 0.0000000E+00  723.8608      577.3426     0.0000000E+00 100103\n" +
                        "  0.0000000E+00 0.0000000E+00  123.5639      297.0817     0.0000000E+00 010103\n" +
                        "  -343.4479     -117.9220     0.0000000E+00 0.0000000E+00  129.9708     001103\n" +
                        "  -12.09279     -11.71418     0.0000000E+00 0.0000000E+00  2.814847     100004\n" +
                        "  -13.91037     -11.93705     0.0000000E+00 0.0000000E+00  1.633702     010004\n" +
                        "  0.0000000E+00 0.0000000E+00  64.11503      30.10875     0.0000000E+00 001004\n" +
                        "  0.0000000E+00 0.0000000E+00 -78724.72     -48425.51     0.0000000E+00 000500\n" +
                        "  -6958.808     -8457.034     0.0000000E+00 0.0000000E+00  4693.654     000401\n" +
                        "  0.0000000E+00 0.0000000E+00  6573.808      2398.922     0.0000000E+00 000302\n" +
                        "  -605.7224     -234.5880     0.0000000E+00 0.0000000E+00  197.0671     000203\n" +
                        "  0.0000000E+00 0.0000000E+00  98.54379      60.23837     0.0000000E+00 000104\n" +
                        " -0.7217579    -0.5213644     0.0000000E+00 0.0000000E+00 0.6492402     000005"
        );
    }

    //----------------- 相椭圆---------------------------------
    private List<Point2> cosy相椭圆(int keyDistanceIndex, boolean xPlane, double delta, int number, int order,
                                 boolean moveToCenter, double scaleForParticle) {

        /*----------------COSY-----------------*/

        // 相空间点
        List<PhaseSpaceParticle> pp = PhaseSpaceParticles.phaseSpaceParticlesAlongPositiveEllipseInPlane(
                xPlane, 3.5 * MM * scaleForParticle, 7.5 * MM * scaleForParticle, delta, number);

        // 转为COSY能量分散
        pp = PhaseSpaceParticles.convertDeltaFromMomentumDispersionToEnergyDispersion(pp, 250);

        // 运行
        List<PhaseSpaceParticle> ppEnd = cosyMap().get(keyDistanceIndex).getT2().apply(pp, order);

        // 投影
        List<Point2> projectionToPlaneCOSY = PhaseSpaceParticles.projectionToPlane(xPlane, ppEnd);

        if (moveToCenter) {
            Vector2 average = Point2.average(projectionToPlaneCOSY).toVector2().reverseSelf();

            projectionToPlaneCOSY.forEach(point2 -> point2.moveSelf(average));
        }

        // 改单位
        return Point2.convert(projectionToPlaneCOSY, 1 / MM, 1 / MRAD);
    }

    private List<Point2> tracking相椭圆(double distance, boolean xPlane, double delta, int number,
                                     boolean moveToCenter, double scaleForParticle) {
        CctFactory.Elements elementsOfAll = getElementsOfAll();
        Trajectory trajectory = getTrajectory();

        RunningParticle ip = ParticleFactory.createIdealProtonAtTrajectory250MeV(trajectory);

        List<PhaseSpaceParticle> pp = PhaseSpaceParticles.phaseSpaceParticlesAlongPositiveEllipseInPlane(
                xPlane, 3.5 * MM * scaleForParticle, 7.5 * MM * scaleForParticle, delta, number);

        List<RunningParticle> ps = ParticleFactory.createParticlesFromPhaseSpaceParticle(ip, ip.computeNaturalCoordinateSystem(), pp);

        ParticleRunner.runThread(ps, elementsOfAll, distance, MM);
        ParticleRunner.run(ip, elementsOfAll, distance, MM);

        List<PhaseSpaceParticle> ppEnd = PhaseSpaceParticles.phaseSpaceParticlesFromRunningParticles(ip, ip.computeNaturalCoordinateSystem(), ps);

        List<Point2> projectionToPlaneCOSY = PhaseSpaceParticles.projectionToPlane(xPlane, ppEnd);

        if (moveToCenter) {
            Vector2 average = Point2.average(projectionToPlaneCOSY).toVector2().reverseSelf();

            projectionToPlaneCOSY.forEach(point2 -> point2.moveSelf(average));
        }

        // 改单位
        return Point2.convert(projectionToPlaneCOSY, 1 / MM, 1 / MRAD);
    }

    //----------------- 粒子跟踪包络 ------------------------------

    private List<Point2> cosy包络0(boolean xxPlane, int order, double delta) {
        return cosyMap().stream().map(bi -> {
            Double s = bi.getT1();
            CosyArbitraryOrder.CosyMapArbitraryOrder map = bi.getT2();

            // 相空间点
            List<PhaseSpaceParticle> pp = PhaseSpaceParticles.phaseSpaceParticlesAlongPositiveEllipseInPlane(
                    xxPlane, 3.5 * MM * 1, 7.5 * MM * 1, delta, 512);

            // 转为COSY能量分散
            pp = PhaseSpaceParticles.convertDeltaFromMomentumDispersionToEnergyDispersion(pp, 250);

            // 运行
            List<PhaseSpaceParticle> ppEnd = map.apply(pp, order);

            // 投影
            List<Point2> projectionToPlaneCOSY = PhaseSpaceParticles.projectionToPlane(xxPlane, ppEnd);

            // 改单位
            projectionToPlaneCOSY = Point2.convert(projectionToPlaneCOSY, 1 / MM, 1 / MRAD);

            double max = projectionToPlaneCOSY.stream().mapToDouble(Point2::getX).map(Math::abs).max().orElse(0);

            return Point2.create(s, max);
        }).collect(Collectors.toList());
    }

    private List<Point2> cosy包络(boolean xxPlane, int order, double delta) {
        if (BaseUtils.Equal.isEqual(delta, 0.0))
            return cosy包络0(xxPlane, order, delta);

        List<Point2> point2s = cosy包络0(xxPlane, order, delta);
        List<Point2> point2s1 = cosy包络0(xxPlane, order, -delta);

        List<Point2> ret = new ArrayList<>();
        for (int i = 0; i < point2s.size(); i++) {
            Point2 point2 = point2s.get(i);
            Point2 point21 = point2s1.get(i);

            ret.add(Point2.create(point2.x, Math.max(point2.y, point21.y)));
        }

        return ret;
    }

    private List<Point2> track粒子跟踪包络0(boolean xxPlane, double delta) {
        CctFactory.MagnetAble cct = getElementsOfAll();
        Trajectory trajectory = getTrajectory();

        RunningParticle ip = ParticleFactory.createIdealProtonAtTrajectory250MeV(trajectory);

        List<PhaseSpaceParticle> pp =
                PhaseSpaceParticles.phaseSpaceParticlesAlongPositiveEllipseInPlane(xxPlane, 3.5 * MM, 7.5 * MRAD, delta, 64);

        List<List<BaseUtils.Content.BiContent<Double, RunningParticle>>> runs = ParticleFactory.createParticlesFromPhaseSpaceParticle(ip, ip.computeNaturalCoordinateSystem(), pp)
                .stream()
                .parallel()
                .map(p -> ParticleRunner.runGetAllInfo(p, cct, trajectory.getLength() + DL2, MM))
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

                    return Point2.create(distance, max / MM);
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

    //------------------ 色散 -----------------------
    private List<Point2> track色散函数R16() {
        CctFactory.MagnetAble cct = getElementsOfAll();
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


    private CctFactory.Elements getElementsOfAll() {
        List<QsHardPlaneMagnet> qs = get3QS();
        CctFactory.Cct allCctIn45 = getAllCctIn45();

        CctFactory.Elements elements = CctFactory.Elements.empty();
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

    private CctFactory.Cct getAllCctIn45() {
        return CctFactory.combineCct(getCct1(), getCctSymmetryCct1());
    }


    private CctFactory.Cct getCct1() {
        Trajectory trajectory = getTrajectory();

        CctFactory.Cct cct = getCct();


        return CctFactory.positionInXYPlane(cct, Point2.create(DL1, trajectoryBigR), BaseUtils.Converter.angleToRadian(-90));

    }

    private CctFactory.Cct getCct2() {

        Trajectory trajectory = getTrajectory();

        CctFactory.Cct dipoleCct = createDipoleCct(
                dipoleCctSmallRInner, dipoleCctSmallROuter, dipoleCctBigR,
                dipoleCctAngle, dipoleCctWindingNumber,
                dipoleCctA0Inner, dipoleCctA1Inner, dipoleCctA2Inner, dipoleCctIInner,
                dipoleCctA0Outer, dipoleCctA1Outer, dipoleCctA2Outer, dipoleCctIOuter,
                numberPerWinding,
                dipoleCctStartingθInner, dipoleCctStartingθOuter,
                dipoleCctStartingφInner, dipoleCctStartingφOuter,
                dipoleCctDirectθInner, dipoleCctDirectθOuter
        );

        CctFactory.Cct agCct = CctFactory.createAgCct(agCctSmallRInner, agCctSmallROuter, agCCTBigR,
                new double[]{agCctAngle1, agCctAngle0},
                new int[]{agCctWindingNumber1, agCctWindingNumber0},
                agCctA0Inner, agCctA1Inner, agCctA2Inner, -agCctIInner,
                agCctA0Outer, agCctA1Outer, agCctA2Outer, -agCctIOuter,
                numberPerWinding, agCctStartingθInner, agCctStartingθOuter,
                agCctStartingφInner, agCctStartingφOuter,
                agCctDirectθInner, agCctDirectθOuter
        );

        CctFactory.Cct cct = CctFactory.combineCct(agCct, dipoleCct);

        //CctFactory.Cct cct1 = CctFactory.positionInXYPlane(cct, Point2.create(DL1, trajectoryBigR), BaseUtils.Converter.angleToRadian(-90));

        Point2 center = Arcs.center(
                trajectory.pointAt(DL1 + CCT_LENGTH + BETWEEN_CCT225),
                trajectory.pointAt(DL1 + CCT_LENGTH + BETWEEN_CCT225 + CCT_LENGTH / 2),
                trajectory.pointAt(DL1 + CCT_LENGTH + BETWEEN_CCT225 + CCT_LENGTH)
        );


        return CctFactory.positionInXYPlane(cct, center, BaseUtils.Converter.angleToRadian(-90 + 22.5));


    }

    private CctFactory.Cct getCctSymmetryCct1() {
        Trajectory trajectory = getTrajectory();
        return CctFactory.symmetryInXYPlaneByLine(
                getCct1(),
                trajectory.pointAt(DL1 + CCT_LENGTH + GAP1 + QS1_LEN + GAP2 + QS2_LEN / 2),
                trajectory.directAt(DL1 + CCT_LENGTH + GAP1 + QS1_LEN + GAP2 + QS2_LEN / 2).rotateSelf(Math.PI / 2)
        );
    }

    private CctFactory.Cct getCct() {
        CctFactory.Cct dipoleCct = createDipoleCct(
                dipoleCctSmallRInner, dipoleCctSmallROuter, dipoleCctBigR,
                dipoleCctAngle, dipoleCctWindingNumber,
                dipoleCctA0Inner, dipoleCctA1Inner, dipoleCctA2Inner, dipoleCctIInner,
                dipoleCctA0Outer, dipoleCctA1Outer, dipoleCctA2Outer, dipoleCctIOuter,
                numberPerWinding,
                dipoleCctStartingθInner, dipoleCctStartingθOuter,
                dipoleCctStartingφInner, dipoleCctStartingφOuter,
                dipoleCctDirectθInner, dipoleCctDirectθOuter
        );
        CctFactory.Cct agCct = createAgCct(
                agCctSmallRInner, agCctSmallROuter, agCCTBigR,
                new double[]{agCctAngle0, agCctAngle1},
                new int[]{agCctWindingNumber0, agCctWindingNumber1},
                agCctA0Inner, agCctA1Inner, agCctA2Inner, agCctIInner,
                agCctA0Outer, agCctA1Outer, agCctA2Outer, agCctIOuter,
                numberPerWinding,
                agCctStartingθInner, agCctStartingθOuter,
                agCctStartingφInner, agCctStartingφOuter,
                agCctDirectθInner, agCctDirectθOuter
        );

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

    private CctFactory.Cct createDipoleCct(
            double smallRInner, double smallROuter, double bigR, double angle, int windingNumber,
            double a0BipolarInner, double a1QuadrupleInner, double a2SextupleInner, double IInner,
            double a0BipolarOuter, double a1QuadrupleOuter, double a2SextupleOuter, double IOuter,
            int numberPerWinding,
            double startingθInner, double startingθOuter,
            double startingφInner, double startingφOuter,
            boolean directθInner, boolean directθOuter
    ) {
        return CctFactory.createDipoleCctDetailed(
                smallRInner, smallROuter, bigR, angle, windingNumber,
                a0BipolarInner, a1QuadrupleInner, a2SextupleInner, IInner,
                a0BipolarOuter, a1QuadrupleOuter, a2SextupleOuter, IOuter,
                numberPerWinding,
                startingθInner, startingθOuter,
                startingφInner, startingφOuter,
                directθInner, directθOuter
        );
    }

    private CctFactory.Cct createAgCct(
            double smallRInner, double smallROuter, double bigR, double[] angles, int[] windingNumbers,
            double a0BipolarInners, double a1QuadrupleInners, double a2SextupleInners, double IInner,
            double a0BipolarOuters, double a1QuadrupleOuters, double a2SextupleOuters, double IOuter,
            int numberPerWinding,
            double startingθInner, double startingθOuter,
            double startingφInner, double startingφOuter,
            boolean directθInner, boolean directθOuter
    ) {
        return CctFactory.createAgCct(
                smallRInner, smallROuter, bigR, angles, windingNumbers,
                a0BipolarInners, a1QuadrupleInners, a2SextupleInners, IInner,
                a0BipolarOuters, a1QuadrupleOuters, a2SextupleOuters, IOuter,
                numberPerWinding,
                startingθInner, startingθOuter,
                startingφInner, startingφOuter,
                directθInner, directθOuter
        );
    }


    private Trajectory createTestTrajectory() {
        return TrajectoryFactory.setStartingPoint(trajectoryBigR, -1)
                .setStraightLine(1, Vector2.yDirect())
                .addArcLine(trajectoryBigR, false, dipoleCctAngle)
                .addStraitLine(1.0);
    }


    //--------------parameter------------------

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
    private double QS2_SECOND_GRADIENT = -355.6; //T m-2
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
    private boolean dipoleCctDirectθInner = true;
    private boolean dipoleCctDirectθOuter = false;
    private double agCctStartingθInner = 0.0; // 起始绕线方向
    private double agCctStartingθOuter = 0.0;
    private double agCctStartingφInner = 0.0;
    private double agCctStartingφOuter = 0.0;
    private boolean agCctDirectθInner = false;
    private boolean agCctDirectθOuter = true;

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
    private double DL2 = 2.40;
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
    private double dipoleCctIOuter = dipoleCctIInner;
    private double agCctIInner = -4618.911905272398 ;//* (1 - 0.010335570469798657);
    private double agCctIOuter = agCctIInner;

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
