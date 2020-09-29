package cn.edu.hust.zrx.cct.study.旧机架研究.后135偏转段建模;

import cn.edu.hust.zrx.cct.Logger;
import cn.edu.hust.zrx.cct.advanced.COSY;
import cn.edu.hust.zrx.cct.advanced.CosyArbitraryOrder;
import cn.edu.hust.zrx.cct.advanced.PolynomialFitter;
import cn.edu.hust.zrx.cct.advanced.SR;
import cn.edu.hust.zrx.cct.base.BaseUtils;
import cn.edu.hust.zrx.cct.base.cct.Cct;
import cn.edu.hust.zrx.cct.base.cct.CctFactory;
import cn.edu.hust.zrx.cct.base.cct.Elements;
import cn.edu.hust.zrx.cct.base.cct.MagnetAble;
import cn.edu.hust.zrx.cct.base.line.Arcs;
import cn.edu.hust.zrx.cct.base.line.Line2;
import cn.edu.hust.zrx.cct.base.line.Trajectory;
import cn.edu.hust.zrx.cct.base.line.TrajectoryFactory;
import cn.edu.hust.zrx.cct.base.opticsMagnet.qs.QsHardPlaneMagnet;
import cn.edu.hust.zrx.cct.base.particle.*;
import cn.edu.hust.zrx.cct.base.point.Point2;
import cn.edu.hust.zrx.cct.base.point.Point3;
import cn.edu.hust.zrx.cct.base.python.Plot2d;
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
import java.util.stream.Stream;

/**
 * Description
 * A0619增大CCT孔径
 * <p>
 * Data
 * 11:51
 *
 * @author zrx
 * @version 1.0
 */

@SuppressWarnings("all")
public class A0619增大CCT孔径 {

    @run(-100)
    public void 调整孔径重新匹配二极CCT电流磁场() {
        Trajectory trajectoryPart2 = getTrajectoryPart2();


        Vector2 yDirect = Vector2.yDirect(-1);


        dipoleCct345IInner = -10000;
        dipoleCct345IOuter = dipoleCct345IInner;

        MagnetAble elementsOfAllPart2 = getElementsOfAllPart2();

        List<Point2> list = elementsOfAllPart2.magnetBzAlongTrajectory(trajectoryPart2);

        Plot2d.plot2(list);

        Plot2d.showThread();
    }

    @run(-100)
    public void 调整孔径重新匹配二极CCT电流Pre() {

        Trajectory trajectoryPart2 = getTrajectoryPart2();


        Vector2 yDirect = Vector2.yDirect(-1);


        dipoleCct345IInner = -10000;
        dipoleCct345IOuter = dipoleCct345IInner;

        MagnetAble elementsOfAllPart2 = getElementsOfAllPart2();

        List<Point3> point3List = trackingIdealParticle(trajectoryPart2, trajectoryPart2.getLength(), elementsOfAllPart2);

        List<Point2> collect = point3List.stream().map(Point3::toPoint2).collect(Collectors.toList());

        Plot2d.plot2(collect);

        Plot2d.showThread();


    }

    @run(1) // 9953.93
    public void 调整孔径重新匹配二极CCT电流() {
        Trajectory trajectoryPart2 = getTrajectoryPart2();

        Vector2 yDirect = Vector2.yDirect(-1);

//        dipoleCct345IInner = 10000;
//        dipoleCct345IOuter = dipoleCct345IInner;

        List<Point2> collect = BaseUtils.Python.linspaceStream(-9954.2, -9953.18, 160)
                .sequential()
                .mapToObj(i -> {
                    dipoleCct345IInner = i;
                    dipoleCct345IOuter = i;

                    MagnetAble elementsOfAllPart2 = getElementsOfAllPart2();

                    return BaseUtils.Content.BiContent.create(i, elementsOfAllPart2);
                }).collect(Collectors.toList())
                .stream()
                .parallel()
                .map(bi -> {
                    Double i = bi.getT1();
                    MagnetAble elementsOfAllPart2 = bi.getT2();

                    RunningParticle ip = ParticleFactory.createIdealProtonAtTrajectory250MeV(trajectoryPart2);

                    ParticleRunner.run(ip, elementsOfAllPart2, trajectoryPart2.getLength(), MM);

                    Vector3 v3 = ip.getVelocity();

                    Vector2 v2 = v3.toVector2().changeLengthSelf(1);


                    double angle = yDirect.angleToInRadian(v2);

                    return Point2.create(i, angle);
                })
                .collect(Collectors.toList())
                .stream()
                .sorted()
                .collect(Collectors.toList());

        collect.forEach(System.out::println);

        Plot2d.plot2(collect);

        Plot2d.showThread();
    }

    @run(2)
    public void 四极场确定() {
        //private double[] agCct345A1Inners = BaseUtils.ArrayUtils.repeat(-Math.pow(agCct345SmallRInner, 2) * Math.sqrt(3) * 20, 3);
        //    private double[] agCct345A1Outers = BaseUtils.ArrayUtils.repeat(Math.pow(agCct345SmallROuter, 2) * Math.sqrt(3) * 20, 3);

        //    private double agCct345IInner = 0; // 9799 // 2020年6月11日 *0.995
        //    private double agCct345IOuter = 0;

        Trajectory trajectoryPart2 = getTrajectoryPart2();

        agCct345IInner = 10000;
        agCct345IOuter = 10000;

        agCct345A1Inners = BaseUtils.ArrayUtils.repeat(-Math.pow(agCct345SmallRInner, 2) * Math.sqrt(3) * 20 * 0.3028 / 29.7 * 18.14, 3);
        agCct345A1Outers = BaseUtils.ArrayUtils.repeat(Math.pow(agCct345SmallROuter, 2) * Math.sqrt(3) * 20 * 0.3028 / 29.7 * 18.14, 3);

        MagnetAble elementsOfAllPart2 = getElementsOfAllPart2();


        List<List<Point2>> lists = elementsOfAllPart2.multiplePoleMagnetAlongTrajectoryBreak(
                trajectoryPart2, 10 * MM, 40 * MM, 1, 16
        );

        List<Point2> g = lists.get(1);

        Plot2d.plot2(g, Plot2d.BLACK_LINE);


        Plot2d.plot2(List.of(
                Point2.create(0, 0),
                Point2.create(DL2, 0),
                Point2.create(DL2, 18.14),
                Point2.create(DL2 + 0.75 * BaseUtils.Converter.angleToRadian(8), 18.14),
                Point2.create(DL2 + 0.75 * BaseUtils.Converter.angleToRadian(8), -18.14),
                Point2.create(DL2 + 0.75 * BaseUtils.Converter.angleToRadian(8 + 30), -18.14),
                Point2.create(DL2 + 0.75 * BaseUtils.Converter.angleToRadian(8 + 30), 18.14),
                Point2.create(DL2 + CCT345_LENGTH, 18.14),
                Point2.create(DL2 + CCT345_LENGTH, 0),
                Point2.create(DL2 + CCT345_LENGTH + GAP3, 0),
                Point2.create(DL2 + CCT345_LENGTH + GAP3, 7.6),
                Point2.create(DL2 + CCT345_LENGTH + GAP3 + QS3_LEN, 7.6),
                Point2.create(DL2 + CCT345_LENGTH + GAP3 + QS3_LEN, 0.0),
                Point2.create(DL2 + CCT345_LENGTH + GAP3 + QS3_LEN + GAP3, 0.0)
        ), Plot2d.GREY_DASH);

        Plot2d.info("s/m", "B1/(T/m))", "", 18);

        Plot2d.showThread();
    }

    @run(3)
    public void 单粒子跟踪() {
        Trajectory trajectoryPart2 = getTrajectoryPart2();
        MagnetAble elementsOfAllPart2 = getElementsOfAllPart2();


//        List<List<Point2>> lists = elementsOfAllPart2.multiplePoleMagnetAlongTrajectoryBreak(
//                trajectoryPart2, 10 * MM, 40 * MM, 1, 16
//        );
//
//        List<Point2> g = lists.get(0);
//
//        Plot2d.plot2(g, Plot2d.BLACK_LINE);
//
//        Plot2d.showThread();

        List<Point2> x = trackingIdealParticle(trajectoryPart2, trajectoryPart2.getLength(), elementsOfAllPart2, false);

        Plot2d.plot2(mm2mmm(x), Plot2d.BLACK_LINE);

        Plot2d.info("s/m", "y/mm", "", 18);

        Plot2d.showThread();


    }


    @run(4)
    public void 通过色散细致条件四极CCT电流() {
        Trajectory trajectoryPart2 = getTrajectoryPart2();


        String[] le = BaseUtils.Python.linspaceStream(0.9, 1.1, switcher.getSize())
                .sequential()
                .mapToObj(k -> {
                    agCct345IInner = 10000 * k;
                    agCct345IOuter = 10000 * k;
                    MagnetAble elementsOfAllPart2 = getElementsOfAllPart2();

                    return BaseUtils.Content.BiContent.create(k, elementsOfAllPart2);
                }).collect(Collectors.toList())
                .stream()
                .parallel()
                .map(bi -> {
                    Double k = bi.getT1();
                    MagnetAble elementsOfAllPart2 = bi.getT2();

                    List<Point2> list = track色散(trajectoryPart2, elementsOfAllPart2);

                    return BaseUtils.Content.BiContent.create(k, list);

                }).collect(Collectors.toList())
                .stream()
                .sequential()
                .sorted(Comparator.comparingDouble(BaseUtils.Content.BiContent::getT1))
                .collect(Collectors.toList())
                .stream()
                .peek(bi -> {
                    List<Point2> t2 = bi.getT2();

                    Plot2d.plot2(t2, switcher.getAndSwitch());
                }).map(BaseUtils.Content.BiContent::getT1)
                .map(k -> "k=" + k)
                .collect(Collectors.toList())
                .toArray(String[]::new);


        Plot2d.info("s/m", "R16/(mm/%)", "", 18);

        Plot2d.legend(18, le);

        Plot2d.showThread();
    }

    @run(-1000)
    public void 其他事情_束流斑点2() {

        boolean xPlane = false;

        List<PhaseSpaceParticle> pp = PhaseSpaceParticles.phaseSpaceParticlesAlongPositiveEllipseInPlane(
                xPlane, 4 * MM, 6 * MM, 0, 64);


        // 转为COSY能量分散
        pp = PhaseSpaceParticles.convertDeltaFromMomentumDispersionToEnergyDispersion(pp, 250);

        // 运行
        List<PhaseSpaceParticle> ppEnd = mapQS31437788.apply(pp, 5);

        // 投影
        List<Point2> projectionToPlaneCOSY = PhaseSpaceParticles.projectionToPlane(xPlane, ppEnd);

        projectionToPlaneCOSY = Point2.convert(projectionToPlaneCOSY, 1 / MM, 1 / MRAD);

        Plot2d.plot2(projectionToPlaneCOSY, Plot2d.RED_POINT);

        if (xPlane)
            Plot2d.info("x/mm", "xp/mrad", "", 18);
        else
            Plot2d.info("y/mm", "yp/mrad", "", 18);

        Plot2d.showThread();
    }


    @run(1001)
    public void 其他事情_束流斑点1() {

        boolean xPlane = false;

        List<PhaseSpaceParticle> pp = PhaseSpaceParticles.phaseSpaceParticlesAlongPositiveEllipseInPlane(
                xPlane, 3.5 * MM, 7.5 * MM, 0.05, 64);


        // 转为COSY能量分散
        pp = PhaseSpaceParticles.convertDeltaFromMomentumDispersionToEnergyDispersion(pp, 250);

        // 运行
        List<PhaseSpaceParticle> ppEnd = mapQS3S150000.apply(pp, 5);

        // 投影
        List<Point2> projectionToPlaneCOSY = PhaseSpaceParticles.projectionToPlane(xPlane, ppEnd);

        projectionToPlaneCOSY = Point2.convert(projectionToPlaneCOSY, 1 / MM, 1 / MRAD);

        Plot2d.plot2(projectionToPlaneCOSY, Plot2d.RED_POINT);

        if (xPlane)
            Plot2d.info("x/mm", "xp/mrad", "", 18);
        else
            Plot2d.info("y/mm", "yp/mrad", "", 18);

        Plot2d.showThread();
    }


    @run(-1002)
    public void 其他事情_束流斑点12() {

        boolean xPlane = false;

        //1437 - 46
        //1500 - 3575

        {

            List<PhaseSpaceParticle> pp1500 = PhaseSpaceParticles.phaseSpaceParticlesAlongPositiveEllipseInPlane(
                    xPlane, 3.5 * MM, 7.5 * MM, 0, 64);

            // 转为COSY能量分散
            pp1500 = PhaseSpaceParticles.convertDeltaFromMomentumDispersionToEnergyDispersion(pp1500, 250);

            // 运行
            List<PhaseSpaceParticle> ppEnd1500 = mapQS3S150000.apply(pp1500, 5);

            // 投影
            List<Point2> projectionToPlaneCOSY1500 = PhaseSpaceParticles.projectionToPlane(xPlane, ppEnd1500);

            projectionToPlaneCOSY1500 = Point2.convert(projectionToPlaneCOSY1500, 1 / MM, 1 / MRAD);

            Plot2d.plot2(projectionToPlaneCOSY1500, Plot2d.RED_POINT);
        }


        {
            List<PhaseSpaceParticle> pp1437 = PhaseSpaceParticles.phaseSpaceParticlesAlongPositiveEllipseInPlane(
                    xPlane, 4 * MM, 6 * MM, 0, 64);

            // 转为COSY能量分散
            pp1437 = PhaseSpaceParticles.convertDeltaFromMomentumDispersionToEnergyDispersion(pp1437, 250);

            // 运行
            List<PhaseSpaceParticle> ppEnd1437 = mapQS31437788.apply(pp1437, 5);

            // 投影
            List<Point2> projectionToPlaneCOSY1437 = PhaseSpaceParticles.projectionToPlane(xPlane, ppEnd1437);

            projectionToPlaneCOSY1437 = Point2.convert(projectionToPlaneCOSY1437, 1 / MM, 1 / MRAD);

            Plot2d.plot2(projectionToPlaneCOSY1437, Plot2d.BLUE_POINT);
        }


        if (xPlane)
            Plot2d.info("x/mm", "xp/mrad", "", 18);
        else
            Plot2d.info("y/mm", "yp/mrad", "", 18);

        Plot2d.legend(18, "QS1500-x", "QS1437-x", "QS1500-y", "QS1437-y");

        Plot2d.showThread();
    }


    @run(-1005)
    public void 其他事情_束流斑点1500() {

        //1500 - 3575

        final double xM = 3.5 * MM;
        final double xpM = 7.5 * MM;
        final double dE = -5 * PRESENT;

        double[][] dss = sphericalUniformDistribution球面(3000, 5);

        List<PhaseSpaceParticle> pps = Arrays.stream(dss)
//                .peek(d -> System.out.println(Arrays.toString(d)))
                .map(p ->
                        PhaseSpaceParticle.create(xM * p[0], xpM * p[1], xM * p[2], xpM * p[3], 0 * p[4], dE * 1)
                ).map(pp -> PhaseSpaceParticles.convertDeltaFromMomentumDispersionToEnergyDispersion(pp, 250))
                .map(pp -> map完整机架.apply(pp, 5))
                .collect(Collectors.toList());

        List<PhaseSpaceParticle> apply = map完整机架.apply(pps);

        List<Point2> ans = apply.stream().map(p -> Point2.create(p.getX(), p.getY())).collect(Collectors.toList());

        Plot2d.plot2(Point2.convert(ans, 1 / MM, 1 / MM), Plot2d.BLACK_POINT);

        Plot2d.info("x/mm", "y/mm", "完整机架 束斑 动量=95%", 18);

        Plot2d.showThread();

    }

    @run(1006)
    public void 其他事情_束流椭圆1500() {
        boolean xPlane = false;


        String[] le = BaseUtils.Python.linspaceStream(-0.08, 0.08, switcher.getSize())
                .sequential().peek(dp -> {
                    List<Point2> list = cosy相椭圆(xPlane, dp, 128, 5,
                            false, 1, mapQS3S150000);
                    Plot2d.plot2(list, switcher.getAndSwitch());
                }).mapToObj(dp -> "dp=" + BaseUtils.Doubles.reservedDecimal(dp,3))
                .collect(Collectors.toList())
                .toArray(String[]::new);


        if (xPlane)
            Plot2d.info("x/mm", "xp/mrad", "QS3_S=-.1500 5阶", 18);
        else
            Plot2d.info("y/mm", "yp/mrad", "QS3_S=-.1500 5阶", 18);

        Plot2d.legend(18, le);

        Plot2d.showThread();
    }

    private Point3[] sphericalUniformDistribution(int i) {
        return BaseUtils.StreamTools.repeat(i)
                .mapToObj(k -> {
                    double u = Math.random();
                    double v = Math.random();
                    while (u * u + v * v > 1) {
                        u = Math.random();
                        v = Math.random();
                    }

                    double r = Math.sqrt(u * u + v * v);

                    return Point3.create(
                            2 * u * Math.sqrt(1 - r * r),
                            2 * v * Math.sqrt(1 - r * r),
                            1 - 2 * r * r
                    );
                }).collect(Collectors.toList())
                .toArray(Point3[]::new);


        // step1：
        //　　　　随机抽样产生一对均匀分布的随机数 u ，v   ；这里u，v 在[-1,1] 范围内
        //　step2 ：
        //　　　　计算  r^2 = u^2+v^2;
        //　　　　如果 r^2 > 1 则重新抽样，直到满足   r^2 < 1  .
        //  step3 ：
        //　　　　计算
        //　　
        //　　　　x=2*u*sqrt(1-r2);
        //
        //　　　　y=2*v*sqrt(1-r2);
        //
        //　　　　z=1-2*r2;　　


    }

    private double[][] sphericalUniformDistribution(int number, int dimenstion) { // n 维球面均匀分布
        return BaseUtils.StreamTools.repeat(number)
                .mapToObj(ii -> {
                    double[] ans = new double[dimenstion];
                    do {
                        for (int i = 0; i < ans.length; i++) {
                            ans[i] = (Math.random() - 0.5) * 2;
                        }
                    } while (Arrays.stream(ans).map(a -> a * a).sum() > 1.0);
//                    System.out.println("找到一个点");
                    return ans;
                }).collect(Collectors.toList())
                .toArray(double[][]::new);
    }

    public double[][] sphericalUniformDistribution球面(int number, int dimenstion) { // n 维球面均匀分布
        return BaseUtils.StreamTools.repeat(number)
                .mapToObj(ii -> {
                    double[] ans = new double[dimenstion];
                    double r;
                    do {
                        for (int i = 0; i < ans.length; i++) {
                            ans[i] = (Math.random() - 0.5) * 2;
                        }

                        r = Arrays.stream(ans).map(a -> a * a).sum();
                    } while (r > 1.0 || r < 0.9);
//                    System.out.println("找到一个点");

                    r = 0;

                    for (int i = 0; i < ans.length; i++) {
                        if (i == ii % dimenstion) continue;


                        double ai = ans[i];

                        r += ai * ai;
                    }

                    ans[ii % dimenstion] = Math.sqrt(1 - r) * (Math.random() > 0.5 ? 1 : -1);

                    return ans;
                }).collect(Collectors.toList())
                .toArray(double[][]::new);
    }

    // ------------------- method ----------------
    private void phase相椭圆画图(
            double distance, boolean xPlane, double delta, int numberParticleForTrack,
            boolean moveToCenter, double scaleForParticle,
            MagnetAble magnetAble, Trajectory trajectory,
            int numberParticleForCosyMap, int cosyOrder,
            List<BaseUtils.Content.BiContent<String, CosyArbitraryOrder.CosyMapArbitraryOrder>> mapInfo
    ) {
        CosyArbitraryOrder.CosyMapArbitraryOrder[] maps = mapInfo.stream().map(BaseUtils.Content.
                BiContent::getT2).collect(Collectors.toList()).toArray(CosyArbitraryOrder.CosyMapArbitraryOrder[]::new);

        List<List<Point2>> lists = phase相椭圆研究(distance, xPlane, delta, numberParticleForTrack, moveToCenter,
                scaleForParticle, magnetAble, trajectory, numberParticleForCosyMap, cosyOrder, maps);

        List<Point2> list = lists.get(0);
        Plot2d.plot2(list, Plot2d.BLACK_POINT);

        for (int i = 1; i < lists.size(); i++) {
            List<Point2> list1 = lists.get(i);
            Plot2d.plot2(list1, switcher.getAndSwitch());
        }

        if (xPlane)
            Plot2d.info("x/mm", "xp/mrad", "x/xp dp=" + delta / PRESENT + "%, s=" + distance + "m", 18);
        else
            Plot2d.info("y/mm", "yp/mrad", "y/yp dp=" + delta / PRESENT + "%, s=" + distance + "m", 18);

        List<String> info = mapInfo.stream().map(BaseUtils.Content.BiContent::getT1).collect(Collectors.toList());
        info.add(0, "Track");

        Plot2d.legend(18, info.toArray(String[]::new));

        Plot2d.equal();

        Plot2d.showThread();
    }

    private void phase相椭圆画图(
            double distance, boolean xPlane, double delta, int numberParticleForTrack,
            boolean moveToCenter, double scaleForParticle,
            List<BaseUtils.Content.BiContent<String, MagnetAble>> magnetAblenfo, Trajectory trajectory,
            int numberParticleForCosyMap, int cosyOrder,
            List<BaseUtils.Content.BiContent<String, CosyArbitraryOrder.CosyMapArbitraryOrder>> mapInfo, List<String> describes
    ) {
        List<CosyArbitraryOrder.CosyMapArbitraryOrder> maps = mapInfo.stream().map(BaseUtils.Content.
                BiContent::getT2).collect(Collectors.toList());

        List<MagnetAble> magnetAbles = magnetAblenfo.stream().map(BaseUtils.Content.BiContent::getT2).collect(Collectors.toList());

        List<List<Point2>> lists = phase相椭圆研究(distance, xPlane, delta, numberParticleForTrack, moveToCenter,
                scaleForParticle, magnetAbles, trajectory, numberParticleForCosyMap, cosyOrder, maps);

        for (int i = 0; i < lists.size(); i++) {
            List<Point2> list1 = lists.get(i);
            Plot2d.plot2circle(list1, describes.get(i));
        }

        if (xPlane)
            Plot2d.info("x/mm", "xp/mrad", "x/xp dp=" + delta / PRESENT + "%, s=" + distance + "m", 18);
        else
            Plot2d.info("y/mm", "yp/mrad", "y/yp dp=" + delta / PRESENT + "%, s=" + distance + "m", 18);

        Stream<String> trakcinfo = magnetAblenfo.stream().map(BaseUtils.Content.BiContent::getT1).collect(Collectors.toList()).stream();
        Stream<String> mapinfo = mapInfo.stream().map(BaseUtils.Content.BiContent::getT1).collect(Collectors.toList()).stream();

        String[] le = Stream.concat(trakcinfo, mapinfo).collect(Collectors.toList()).toArray(String[]::new);

        Plot2d.legend(18, le);

        Plot2d.equal();

        Plot2d.showThread();
    }

    private List<List<Point2>> phase相椭圆研究(
            double distance, boolean xPlane, double delta, int numberParticleForTrack,
            boolean moveToCenter, double scaleForParticle,
            MagnetAble magnetAble, Trajectory trajectory,
            int numberParticleForCosyMap, int cosyOrder, CosyArbitraryOrder.CosyMapArbitraryOrder... maps
    ) {
        List<List<Point2>> ret = new ArrayList<>(1 + maps.length);

        ret.add(
                tracking相椭圆(distance, xPlane, delta, numberParticleForTrack, moveToCenter, scaleForParticle, magnetAble, trajectory)
        );

        for (CosyArbitraryOrder.CosyMapArbitraryOrder map : maps) {
            ret.add(
                    cosy相椭圆(xPlane, delta, numberParticleForCosyMap, cosyOrder, moveToCenter, scaleForParticle, map)
            );
        }

        return ret;
    }

    private List<List<Point2>> phase相椭圆研究(
            double distance, boolean xPlane, double delta, int numberParticleForTrack,
            boolean moveToCenter, double scaleForParticle,
            List<MagnetAble> magnetAbles, Trajectory trajectory,
            int numberParticleForCosyMap, int cosyOrder, List<CosyArbitraryOrder.CosyMapArbitraryOrder> maps
    ) {
        List<List<Point2>> ret = new ArrayList<>(magnetAbles.size() + maps.size());


        for (MagnetAble magnetAble : magnetAbles) {
            ret.add(
                    tracking相椭圆(distance, xPlane, delta, numberParticleForTrack, moveToCenter, scaleForParticle, magnetAble, trajectory)
            );
        }


        for (CosyArbitraryOrder.CosyMapArbitraryOrder map : maps) {
            ret.add(
                    cosy相椭圆(xPlane, delta, numberParticleForCosyMap, cosyOrder, moveToCenter, scaleForParticle, map)
            );
        }

        return ret;
    }


    private List<Point2> cosy相椭圆(
            boolean xPlane, double delta, int number, int order,
            boolean moveToCenter, double scaleForParticle,
            CosyArbitraryOrder.CosyMapArbitraryOrder map
    ) {

        /*----------------COSY-----------------*/

        // 相空间点
        List<PhaseSpaceParticle> pp = PhaseSpaceParticles.phaseSpaceParticlesAlongPositiveEllipseInPlane(
                xPlane, 3.5 * MM * scaleForParticle, 7.5 * MM * scaleForParticle, delta, number);

        // 转为COSY能量分散
        pp = PhaseSpaceParticles.convertDeltaFromMomentumDispersionToEnergyDispersion(pp, 250);

        // 运行
        List<PhaseSpaceParticle> ppEnd = map.apply(pp, order);

        // 投影
        List<Point2> projectionToPlaneCOSY = PhaseSpaceParticles.projectionToPlane(xPlane, ppEnd);

        if (moveToCenter) {
            Vector2 average = Point2.average(projectionToPlaneCOSY).toVector2().reverseSelf();

            projectionToPlaneCOSY.forEach(point2 -> point2.moveSelf(average));
        }

        // 改单位
        return Point2.convert(projectionToPlaneCOSY, 1 / MM, 1 / MRAD);
    }

    private List<Point2> tracking相椭圆(
            double distance, boolean xPlane, double delta, int number,
            boolean moveToCenter, double scaleForParticle,
            MagnetAble magnetAble, Trajectory trajectory) {

        RunningParticle ip = ParticleFactory.createIdealProtonAtTrajectory250MeV(trajectory);

        List<PhaseSpaceParticle> pp = PhaseSpaceParticles.phaseSpaceParticlesAlongPositiveEllipseInPlane(
                xPlane, 3.5 * MM * scaleForParticle, 7.5 * MM * scaleForParticle, delta, number);

        List<RunningParticle> ps = ParticleFactory.createParticlesFromPhaseSpaceParticle(ip, ip.computeNaturalCoordinateSystem(), pp);

        if (distance > MM) {
            ParticleRunner.runThread(ps, magnetAble, distance, MM);
            //ParticleRunner.run(ip, magnetAble, distance, MM);
        }

        RunningParticle ipEnd = ParticleFactory.createIdealProtonAtTrajectory250MeV(trajectory, distance);

        List<PhaseSpaceParticle> ppEnd = PhaseSpaceParticles.phaseSpaceParticlesFromRunningParticles(
                ipEnd, ipEnd.computeNaturalCoordinateSystem(), ps);

        List<Point2> projectionToPlaneCOSY = PhaseSpaceParticles.projectionToPlane(xPlane, ppEnd);

        if (moveToCenter) {
            Vector2 average = Point2.average(projectionToPlaneCOSY).toVector2().reverseSelf();

            projectionToPlaneCOSY.forEach(point2 -> point2.moveSelf(average));
        }

        // 改单位
        return Point2.convert(projectionToPlaneCOSY, 1 / MM, 1 / MRAD);
    }


    private List<Point2> trackingIdealParticle(
            Line2 trajectory, double distance, MagnetAble magnetAble, boolean xPlane) {
        RunningParticle ip = ParticleFactory.createIdealProtonAtTrajectory250MeV(trajectory);

        return ParticleRunner.runGetPoint3WithDistance(ip, magnetAble, distance, MM)
                .stream()
                .map(point3WithDistance -> {

                    if (xPlane) {
                        double d = point3WithDistance.getDistance();

                        Point2 p = point3WithDistance.getPoint3().toPoint2();

                        Point2 o = trajectory.pointAt(d);
                        Vector2 x = trajectory.directAt(d).rotateSelf(Math.PI / 2);

                        return Point2.create(d, Vector2.from(o).to(p).dot(x));


                    } else {
                        return point3WithDistance.getDistanceWithZ();
                    }
                }).collect(Collectors.toList());
    }

    private List<Point3> trackingIdealParticle(Line2 trajectory, double distance, MagnetAble magnetAble) {
        RunningParticle ip = ParticleFactory.createIdealProtonAtTrajectory250MeV(trajectory);
        return ParticleRunner.run(ip, magnetAble, distance, MM);
    }

    private List<List<Point2>> track多粒子(int number, double delta, Trajectory trajectory, double distance,
                                        MagnetAble magnetAble, boolean xPlane) {
        RunningParticle ip = ParticleFactory.createIdealProtonAtTrajectory250MeV(trajectory);

//        List<PhaseSpaceParticle> pp = PhaseSpaceParticles.phaseSpaceParticlesAlongPositiveEllipseInPlane(
//                xPlane, 3.5 * MM, 7.5 * MM, delta, number);
//        List<RunningParticle> ps = ParticleFactory.createParticlesFromPhaseSpaceParticle(ip, ip.computeNaturalCoordinateSystem(), pp);


        List<RunningParticle> ps = Arrays.stream(SR.sphericalUniformDistribution(number)).map(point3 -> {

            PhaseSpaceParticle psp = PhaseSpaceParticle.create(
                    xPlane ? 3.5 * MM * point3.x : 0,
                    xPlane ? 7.5 * MM * point3.y : 0,
                    xPlane ? 0 : 3.5 * MM * point3.x,
                    xPlane ? 0 : 7.5 * MM * point3.y,
                    0,
                    delta * point3.z
            );

            return ParticleFactory.createParticleFromPhaseSpaceParticle(ip, ip.computeNaturalCoordinateSystem(), psp);
        }).collect(Collectors.toList());


        return ps.stream().parallel().map(p ->
                ParticleRunner.runGetPoint3WithDistance(p, magnetAble, distance, MM)
                        .stream()
                        .map(point3WithDistance -> {
                            if (xPlane) {
                                double d = point3WithDistance.getDistance();

                                Point2 p2 = point3WithDistance.getPoint3().toPoint2();

                                Point2 o = trajectory.pointAt(d);
                                Vector2 x = trajectory.directAt(d).rotateSelf(Math.PI / 2);

                                return Point2.create(d, Vector2.from(o).to(p2).dot(x));
                            } else {
                                return point3WithDistance.getDistanceWithZ();
                            }
                        })
                        .collect(Collectors.toList())
        ).collect(Collectors.toList());
    }

    // 单位 mm/%
    private List<Point2> track色散(Trajectory trajectory, MagnetAble magnetAble) {
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
                    List<Point2> delta_x = particles
                            .stream()
                            .map(p -> PhaseSpaceParticles.phaseSpaceParticleFromRunningParticle(ip, ip.computeNaturalCoordinateSystem(), p))
                            .map(pp -> Point2.create(pp.getDelta(), pp.getX()))
                            .sorted(Comparator.comparingDouble(Point2::getX))
                            .collect(Collectors.toList());
                    double[] fit = fitter.fit(delta_x);


                    particles.forEach(p -> p.runSelf(magnetAble.magnetAt(p.position), MM));

                    return Point2.create(distance, fit[1]);
                })
                .map(p -> Point2.convert(p, 1, 1 / MM * PRESENT))
                .collect(Collectors.toList());

        return distance_R16;
    }

    // 单位 mm/%
    private List<Point2> cosy色散(List<BaseUtils.Content.BiContent<Double,
            CosyArbitraryOrder.CosyMapArbitraryOrder>> distance_cosyMap) {
        return distance_cosyMap.stream()
                .map(bi -> {
                    Double d = bi.getT1();
                    CosyArbitraryOrder.CosyMapArbitraryOrder m = bi.getT2();

                    double r16Cosy = m.getR(1, 6);

                    double r16Trans = COSY.convertR16InCosyToTransportPronTonOnly(r16Cosy, 250);

                    return Point2.create(d, r16Trans);
                })
                .collect(Collectors.toList());
    }


    /**
     * 神方法
     *
     * @param dependentVariable   应变量
     * @param independentVariable 自变量
     * @param xMin                自变量最小值
     * @param xMax                自变量最大值
     * @param number              分点数目
     * @param trajectory          轨迹
     * @param distance            粒子运动距离
     * @param magnetAble          磁场
     * @return 函数关系 y=f(x)
     */
    private List<Point2> trackFunction(
            PhaseSpaceParticle.VARIABLE dependentVariable,
            PhaseSpaceParticle.VARIABLE independentVariable,
            double xMin, double xMax, int number,
            Trajectory trajectory,
            double distance,
            MagnetAble magnetAble
    ) {
        RunningParticle ip = ParticleFactory.createIdealProtonAtTrajectory250MeV(trajectory);
        RunningParticle ipEnd = ParticleFactory.createIdealProtonAtTrajectory250MeV(trajectory, distance);
        PhaseSpaceParticle origin = PhaseSpaceParticle.origin();

        return BaseUtils.Python.linspaceStream(xMin, xMax, number)
                .mapToObj(x -> {
                    PhaseSpaceParticle p = origin.copy();
                    p.set(independentVariable, x);
                    RunningParticle pp = ParticleFactory.createParticleFromPhaseSpaceParticle(ip,
                            ip.computeNaturalCoordinateSystem(), p);
                    return BaseUtils.Content.BiContent.create(x, pp);
                })
                .collect(Collectors.toList())
                .stream().parallel()
                .map(bi -> {
                    Double x = bi.getT1();
                    RunningParticle p = bi.getT2();
                    ParticleRunner.run(p, magnetAble, distance, MM);
                    PhaseSpaceParticle ppEnd = PhaseSpaceParticles.phaseSpaceParticleFromRunningParticle(
                            ipEnd, ipEnd.computeNaturalCoordinateSystem(), p);
                    double y = ppEnd.get(dependentVariable);
                    return Point2.create(x, y);
                })
                .collect(Collectors.toList())
                .stream()
                .sorted(Point2::compareTo)
                .collect(Collectors.toList());
    }

    private List<Point2> mm2mmm(List<Point2> mm) {
        return Point2.convert(mm, 1, 1 / MM);
    }

    // ---------- element --------------

    private static Trajectory getTrajectoryPart2() {
        Trajectory trajectoryPart1 = getTrajectoryPart1();

        return TrajectoryFactory.setStartingPoint(trajectoryPart1.pointAtEnd())
                .setStraightLine(DL2, trajectoryPart1.directAtEnd())
                .addArcLine(trajectoryBigRPart2, true, dipoleCct345Angle)
                .addStraitLine(GAP3 + QS3_LEN + GAP3)
                .addArcLine(trajectoryBigRPart2, true, dipoleCct345Angle)
                .addStraitLine(DL2);
    }

    private static Trajectory getTrajectoryPart1() {
        return TrajectoryFactory.setStartingPoint(Point2.origin())
                .setStraightLine(DL1, Vector2.xDirect())
                .addArcLine(trajectoryBigRPart1, false, dipoleCct12Angle)
                .addStraitLine(GAP1)//gap1
                .addStraitLine(QS1_LEN)//QS1_LEN
                .addStraitLine(GAP2)//GAP2
                .addStraitLine(QS2_LEN)//QS2_LEN
                .addStraitLine(GAP2)//GAP2
                .addStraitLine(QS1_LEN)//QS1_LEN
                .addStraitLine(GAP1)//gap1
                .addArcLine(trajectoryBigRPart1, false, dipoleCct12Angle)
                .addStraitLine(DL1);

    }

    private static Trajectory getTestTrajectory(double BigR, double angle, double driftLength) {
        return TrajectoryFactory.setStartingPoint(BigR, -driftLength)
                .setStraightLine(driftLength, Vector2.yDirect())
                .addArcLine(BigR, false, angle)
                .addStraitLine(driftLength);
    }

    private Cct getCct12_1() {
        Cct agCct12 = createAgCct12();
        Cct dipoleCct12 = createDipoleCct12();

        Cct cct = CctFactory.combineCct(agCct12, dipoleCct12);

        return CctFactory.positionInXYPlane(cct, Point2.create(DL1, trajectoryBigRPart1), BaseUtils.Converter.angleToRadian(-90));
    }

    private Cct getCct12_2() {
        Cct cct12_1 = getCct12_1();

        Trajectory trajectoryPart1 = getTrajectoryPart1();
        return CctFactory.symmetryInXYPlaneByLine(
                cct12_1,
                trajectoryPart1.pointAt(DL1 + CCT12_LENGTH + GAP1 + QS1_LEN + GAP2 + QS2_LEN / 2),
                trajectoryPart1.directAt(DL1 + CCT12_LENGTH + GAP1 + QS1_LEN + GAP2 + QS2_LEN / 2).rotateSelf(Math.PI / 2)
        );
    }

    private Cct getCct345_1() {
        Trajectory trajectoryPart2 = getTrajectoryPart2();

        Point2 p1 = trajectoryPart2.pointAt(DL2);
        Point2 p2 = trajectoryPart2.pointAt(DL2 + CCT345_LENGTH / 2);
        Point2 p3 = trajectoryPart2.pointAt(DL2 + CCT345_LENGTH);

        Point2 center3 = Arcs.center(p1, p2, p3);

        Cct dipoleCct345 = createDipoleCct345();
        Cct agCct345 = createAgCct345();


        dipoleCct345 = CctFactory.positionInXYPlane(dipoleCct345, center3, BaseUtils.Converter.angleToRadian(180 - dipoleCct345Angle - 45));
        agCct345 = CctFactory.positionInXYPlane(agCct345, center3, BaseUtils.Converter.angleToRadian(180 - dipoleCct345Angle - 45));


        return CctFactory.combineCct(dipoleCct345, agCct345);
    }

    private Cct getCct345_2() {
        Trajectory trajectoryPart2 = getTrajectoryPart2();

        Point2 p = trajectoryPart2.pointAt(DL2 + CCT345_LENGTH + GAP3 + QS3_LEN / 2);
        Vector2 d = trajectoryPart2.directAt(DL2 + CCT345_LENGTH + GAP3 + QS3_LEN / 2).rotateSelf(Math.PI / 2);

        Cct cct345_1 = getCct345_1();

        return CctFactory.symmetryInXYPlaneByLine(cct345_1, p, d);
    }

    private Elements getElementsOfAllPart1() {
        List<QsHardPlaneMagnet> qs = getQs12();
        Cct allCctIn45 = CctFactory.combineCct(getCct12_1(), getCct12_2());

        Elements elements = Elements.empty();
        qs.forEach(elements::addElement);
        allCctIn45.getSoleLayerCctList().forEach(elements::addElement);

        return elements;
    }

    private MagnetAble getElementsOfAllPart2() {
        Trajectory trajectoryPart2 = getTrajectoryPart2();

        Cct cct345_1 = getCct345_1();
        Cct cct345_2 = getCct345_2();

        QsHardPlaneMagnet QS3 = getQs3();

        Elements elements = Elements.empty();

        elements.addElement(cct345_1);
        elements.addElement(cct345_2);
        elements.addElement(QS3);

        return elements;
    }

    private List<QsHardPlaneMagnet> getQs12() {
        Trajectory trajectoryPart1 = getTrajectoryPart1();

        QsHardPlaneMagnet QS11 = QsHardPlaneMagnet.create(QS1_LEN, QS1_GRADIENT, QS1_SECOND_GRADIENT, QS1_APERTURE_MM,
                trajectoryPart1.pointAt(DL1 + CCT12_LENGTH + GAP1), trajectoryPart1.directAt(DL1 + CCT12_LENGTH + GAP1));

        QsHardPlaneMagnet QS2 = QsHardPlaneMagnet.create(QS2_LEN, QS2_GRADIENT, QS2_SECOND_GRADIENT, QS2_APERTURE_MM,
                trajectoryPart1.pointAt(DL1 + CCT12_LENGTH + GAP1 + QS1_LEN + GAP2),
                trajectoryPart1.directAt(DL1 + CCT12_LENGTH + GAP1 + QS1_LEN + GAP2));


        QsHardPlaneMagnet QS12 = QsHardPlaneMagnet.create(QS1_LEN, QS1_GRADIENT, QS1_SECOND_GRADIENT, QS1_APERTURE_MM,
                trajectoryPart1.pointAt(DL1 + CCT12_LENGTH + GAP1 + QS1_LEN + GAP2 + QS2_LEN + GAP2),
                trajectoryPart1.directAt(DL1 + CCT12_LENGTH + GAP1 + QS1_LEN + GAP2 + QS2_LEN + GAP2));

        return List.of(QS11, QS2, QS12);
    }

    private QsHardPlaneMagnet getQs3() {
        Trajectory trajectoryPart2 = getTrajectoryPart2();

        return QsHardPlaneMagnet.create(QS3_LEN, QS3_GRADIENT, QS3_SECOND_GRADIENT, QS3_APERTURE_MM,
                trajectoryPart2.pointAt(DL2 + CCT345_LENGTH + GAP3),
                trajectoryPart2.directAt(DL2 + CCT345_LENGTH + GAP3));
    }

    private Cct createDipoleCct345() {
        return createDipoleCct(
                dipoleCct345SmallRInner, dipoleCct345SmallROuter, dipoleCct345BigR,
                dipoleCct345Angle, dipoleCct345WindingNumber,
                dipoleCct345A0Inner, dipoleCct345A1Inner, dipoleCct345A2Inner, dipoleCct345IInner,
                dipoleCct345A0Outer, dipoleCct345A1Outer, dipoleCct345A2Outer, dipoleCct345IOuter,
                numberPerWinding,
                dipoleCct345StartingθInner, dipoleCct345StartingθOuter,
                dipoleCct345StartingφInner, dipoleCct345StartingφOuter,
                dipoleCct345DirectθInner, dipoleCct345DirectθOuter
        );
    }

    private Cct createDipoleCct12() {
        return createDipoleCct(
                dipoleCct12SmallRInner, dipoleCct12SmallROuter, dipoleCct12BigR,
                dipoleCct12Angle, dipoleCct12WindingNumber,
                dipoleCct12A0Inner, dipoleCct12A1Inner, dipoleCct12A2Inner, dipoleCct12IInner,
                dipoleCct12A0Outer, dipoleCct12A1Outer, dipoleCct12A2Outer, dipoleCct12IOuter,
                numberPerWinding,
                dipoleCct12StartingθInner, dipoleCct12StartingθOuter,
                dipoleCct12StartingφInner, dipoleCct12StartingφOuter,
                dipoleCct12DirectθInner, dipoleCct12DirectθOuter
        );
    }

    private Cct createAgCct12() {
        return createAgCct(
                agCct12SmallRInner, agCct12SmallROuter, agCct12BigR,
                new double[]{agCctAngle1, agCctAngle2},
                new int[]{agCctWindingNumber1, agCctWindingNumber2},
                agCct12A0Inner, agCct12A1Inner, agCct12A2Inner, agCct12IInner,
                agCct12A0Outer, agCct12A1Outer, agCct12A2Outer, agCct12IOuter,
                numberPerWinding,
                agCct12StartingθInner, agCct12StartingθOuter,
                agCct12StartingφInner, agCct12StartingφOuter,
                agCct12DirectθInner, agCct12DirectθOuter
        );
    }

    private Cct createAgCct345() {
        return createAgCct(
                agCct345SmallRInner, agCct345SmallROuter, agCct345BigR,
                new double[]{agCctAngle5, agCctAngle4, agCctAngle3},
                new int[]{agCctWindingNumber5, agCctWindingNumber4, agCctWindingNumber3},
                agCct345A0Inners, agCct345A1Inners, agCct345A2Inners, agCct345IInner,
                agCct345A0Outers, agCct345A1Outers, agCct345A2Outers, agCct345IOuter,
                numberPerWinding,
                agCct345StartingθInner, agCct345StartingθOuter,
                agCct345StartingφInner, agCct345StartingφOuter,
                agCct345DirectθInner, agCct345DirectθOuter
        );
    }

    private Cct createDipoleCct(
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

    private Cct createAgCct(
            double smallRInner, double smallROuter, double bigR, double[] angles, int[] windingNumbers,
            double a0BipolarInners, double a1QuadrupleInners, double a2SextupleInners, double IInner,
            double a0BipolarOuters, double a1QuadrupleOuters, double a2SextupleOuters, double IOuter,
            int numberPerWinding,
            double startingθInner, double startingθOuter,
            double startingφInner, double startingφOuter,
            boolean directθInner, boolean directθOuter
    ) {
//        double smallRInner, double smallROuter, double bigR, double[] angles, int[] windingNumbers,
//        double a0BipolarInners, double a1QuadrupleInners, double a2SextupleInners, double IInner,
//        double a0BipolarOuters, double a1QuadrupleOuters, double a2SextupleOuters, double IOuter,
//        int numberPerWinding,
//        double startingθInner, double startingθOuter,
//        double startingφInner, double startingφOuter,
//        boolean directθInner, boolean directθOuter
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

    private Cct createAgCct(
            double smallRInner, double smallROuter, double bigR, double[] angles, int[] windingNumbers,
            double[] a0BipolarInners, double[] a1QuadrupleInners, double[] a2SextupleInners, double IInner,
            double[] a0BipolarOuters, double[] a1QuadrupleOuters, double[] a2SextupleOuters, double IOuter,
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

    public static class BendingPart2 {
        public static void drawElement(double cctHeight, double QsHeight, String cctDescribe, String QsDescribe) {

            Plot2d.plotBox(DL2, DL2 + CCT345_LENGTH, -cctHeight, cctHeight, cctDescribe);
            Plot2d.plotYLine(DL2 + CCT345_LENGTH_PART3, cctHeight, -cctHeight, cctDescribe);
            Plot2d.plotYLine(DL2 + CCT345_LENGTH_PART3 + CCT345_LENGTH_PART4, cctHeight, -cctHeight, cctDescribe);

            Plot2d.plotBox(DL2 + CCT345_LENGTH + GAP3, DL2 + CCT345_LENGTH + GAP3 + QS3_LEN, -QsHeight, QsHeight, QsDescribe); // qs3

            Plot2d.plotBox(DL2 + CCT345_LENGTH + GAP3 + QS3_LEN + GAP3, DL2 + CCT345_LENGTH + GAP3 + QS3_LEN + GAP3 + CCT345_LENGTH, -cctHeight, cctHeight, cctDescribe);
            Plot2d.plotYLine(DL2 + CCT345_LENGTH + GAP3 + QS3_LEN + GAP3 + CCT345_LENGTH_PART5, cctHeight, -cctHeight, cctDescribe);
            Plot2d.plotYLine(DL2 + CCT345_LENGTH + GAP3 + QS3_LEN + GAP3 + CCT345_LENGTH_PART5 + CCT345_LENGTH_PART4, cctHeight, -cctHeight, cctDescribe);
        }

        public static void drawElement() {
            drawElement(60, 60, Plot2d.GREY_DASH, Plot2d.GREY_DASH);
        }

        public static void drawElement(double cctHeight, double QsHeight) {
            drawElement(cctHeight, QsHeight, Plot2d.GREY_DASH, Plot2d.GREY_DASH);
        }
    }

    //--------------parameter------------------

    private final BaseUtils.Timer timer = new BaseUtils.Timer();

    private void logPastTime(String msg) {
        timer.printPeriodAfterInitial(Logger.getLogger(), msg);
    }

    BaseUtils.Switcher<String> switcher = BaseUtils.Switcher.creat(
            Plot2d.RED_LINE,
            Plot2d.YELLOW_LINE,
            Plot2d.BLUE_LINE,
            Plot2d.GREEN_LINE,
            Plot2d.RED_DASH,
            Plot2d.PINK_DASH,
            Plot2d.BLUE_DASH,
            Plot2d.BLACK_DASH,
            Plot2d.BLACK_LINE
    );

    private final double Bp = 2.43213; // 磁钢度

    // QS 123
    private double QS1_GRADIENT = 53.3; //T m-1
    private double QS2_GRADIENT = -37.7; //T m-1
    private double QS3_GRADIENT = 7.6; //T m-1
    // 注意，六极场梯度一直存在错误，实际值应该乘二。由陈庚发现 2020年5月26日
    private double QS1_SECOND_GRADIENT = -213.78 * 2.0 / 2.0; //T m-2
    private double QS2_SECOND_GRADIENT = 673.56 * 2.0 / 2.0; //T m-2
    private double QS3_SECOND_GRADIENT = 40.83 * 2.0 / 2.0; //T m-2
    private double QS1_APERTURE_MM = 30;
    private double QS2_APERTURE_MM = 30;
    private double QS3_APERTURE_MM = 60;

    // 偏转半径
    private static final double trajectoryBigRPart1 = 0.75;
    private static final double trajectoryBigRPart2 = 0.75;
    private final double dipoleCct12BigR = 0.75;
    private final double dipoleCct345BigR = 0.75;
    private double agCct12BigR = 750.2 * MM;
    private double agCct345BigR = 0.75; //2020年5月28日

    // 初始绕线位置
    private double dipoleCct12StartingθInner = 0.0;
    private double dipoleCct12StartingθOuter = 0.0;
    private double dipoleCct12StartingφInner = 0.0;
    private double dipoleCct12StartingφOuter = 0.0;
    private boolean dipoleCct12DirectθInner = true;
    private boolean dipoleCct12DirectθOuter = false;
    private double agCct12StartingθInner = 0.0; // 起始绕线方向
    private double agCct12StartingθOuter = 0.0;
    private double agCct12StartingφInner = 0.0;
    private double agCct12StartingφOuter = 0.0;
    private boolean agCct12DirectθInner = false;
    private boolean agCct12DirectθOuter = true;

    private double dipoleCct345StartingθInner = 0.0;
    private double dipoleCct345StartingθOuter = 0.0;
    private double dipoleCct345StartingφInner = 0.0;
    private double dipoleCct345StartingφOuter = 0.0;
    private boolean dipoleCct345DirectθInner = true;
    private boolean dipoleCct345DirectθOuter = false;
    private double agCct345StartingθInner = 0.0; // 起始绕线方向
    private double agCct345StartingθOuter = 0.0;
    private double agCct345StartingφInner = 0.0;
    private double agCct345StartingφOuter = 0.0;
    private boolean agCct345DirectθInner = false;
    private boolean agCct345DirectθOuter = true;

    // CCT孔径
    private final double agCct12SmallRInner = 23.5 * MM;
    private final double agCct12SmallROuter = 33.5 * MM;
    private final double dipoleCct12SmallRInner = 43 * MM;
    private final double dipoleCct12SmallROuter = 52 * MM;

    private double agCct345SmallRInner = 23.5 * MM + 80 * MM;
    private double agCct345SmallROuter = 33.5 * MM + 80 * MM;
    private final double dipoleCct345SmallRInner = 43 * MM + 80 * MM;
    private final double dipoleCct345SmallROuter = 52 * MM + 80 * MM;

    // CCT角度
    private static final double dipoleCct12Angle = 22.5;
    private static final double agCctAngle1 = 9.1;
    private static final double agCctAngle2 = 13.4;

    private static final double dipoleCct345Angle = 67.5;
    private static final double agCctAngle3 = 8.0;
    private static final double agCctAngle4 = 30.0;
    private static final double agCctAngle5 = 29.5;


    // CCT长度
    private static final double DL1 = 1.1759;
    private static final double DL2 = 2.40;
    private static final double QS1_LEN = 0.2;
    private static final double QS2_LEN = 0.2;
    private static final double QS3_LEN = 0.2;
    private static final double GAP1 = 0.15;
    private static final double GAP2 = 0.15;
    private static final double GAP3 = 0.2585751675857585;
    private static final double BETWEEN_CCT225 = GAP1 * 2 + QS1_LEN * 2 + GAP2 * 2 + QS2_LEN;
    private static final double CCT12_LENGTH = trajectoryBigRPart1 * BaseUtils.Converter.angleToRadian(dipoleCct12Angle);
    private static final double CCT12_LENGTH_PART1 = trajectoryBigRPart1 * BaseUtils.Converter.angleToRadian(agCctAngle1);
    private static final double CCT12_LENGTH_PART2 = trajectoryBigRPart1 * BaseUtils.Converter.angleToRadian(agCctAngle2);
    private static final double CCT345_LENGTH = trajectoryBigRPart2 * BaseUtils.Converter.angleToRadian(dipoleCct345Angle);
    private static final double CCT345_LENGTH_PART3 = trajectoryBigRPart2 * BaseUtils.Converter.angleToRadian(agCctAngle3);
    private static final double CCT345_LENGTH_PART4 = trajectoryBigRPart2 * BaseUtils.Converter.angleToRadian(agCctAngle4);
    private static final double CCT345_LENGTH_PART5 = trajectoryBigRPart2 * BaseUtils.Converter.angleToRadian(agCctAngle5);


    // CCT a0 a1 a2
    private double dipoleCct12A0Inner = -dipoleCct12SmallRInner * Math.sqrt(3);
    private double dipoleCct12A0Outer = dipoleCct12SmallROuter * Math.sqrt(3);
    private double dipoleCct12A1Inner = Math.pow(dipoleCct12SmallRInner, 2) * 0.225;
    private double dipoleCct12A1Outer = -Math.pow(dipoleCct12SmallROuter, 2) * 0.225;
    private double dipoleCct12A2Inner = 0.0;
    private double dipoleCct12A2Outer = 0.0;

    private final double agCct12A0Inner = 0.0;
    private final double agCct12A0Outer = 0.0;
    private final double agCct12A1Inner = -Math.pow(agCct12SmallRInner, 2) * Math.sqrt(3) * 20;
    private final double agCct12A1Outer = Math.pow(agCct12SmallROuter, 2) * Math.sqrt(3) * 20;
    private final double agCct12A2Inner = 0.0;
    private final double agCct12A2Outer = 0.0;

    private double dipoleCct345A0Inner = -dipoleCct345SmallRInner * Math.sqrt(3);
    private double dipoleCct345A0Outer = dipoleCct345SmallROuter * Math.sqrt(3);
    private double dipoleCct345A1Inner = Math.pow(dipoleCct345SmallRInner, 2) * 0;
    private double dipoleCct345A1Outer = -Math.pow(dipoleCct345SmallROuter, 2) * 0;
    private double dipoleCct345A2Inner = 0.0;
    private double dipoleCct345A2Outer = 0.0;

    // 单值 作废
//    private double agCct345A0Inner = 0.0;
//    private double agCct345A0Outer = 0.0;
//    private double agCct345A1Inner = -Math.pow(agCct345SmallRInner, 2) * Math.sqrt(3) * 20 * 0.19;
//    private double agCct345A1Outer = Math.pow(agCct345SmallROuter, 2) * Math.sqrt(3) * 20 * 0.19;
//    private final double agCct345A2Inner = 0.0;
//    private final double agCct345A2Outer = 0.0;

    // 数组
    private double[] agCct345A0Inners = BaseUtils.ArrayUtils.repeat(0.0, 3);
    private double[] agCct345A0Outers = BaseUtils.ArrayUtils.repeat(0.0, 3);
    private double[] agCct345A1Inners = BaseUtils.ArrayUtils.repeat(-Math.pow(agCct345SmallRInner, 2) * Math.sqrt(3) * 20 * 0.3028 / 29.7 * 18.14, 3);
    private double[] agCct345A1Outers = BaseUtils.ArrayUtils.repeat(Math.pow(agCct345SmallROuter, 2) * Math.sqrt(3) * 20 * 0.3028 / 29.7 * 18.14, 3);
    private double[] agCct345A2Inners = BaseUtils.ArrayUtils.repeat(0.0, 3);
    private double[] agCct345A2Outers = BaseUtils.ArrayUtils.repeat(0.0, 3);

    // 匝数 5mm匝间距 则匝数 = 58.85
    private final int dipoleCct12WindingNumber = 59;
    private final int agCctWindingNumber1 = 23;
    private final int agCctWindingNumber2 = 34; // sum = 57

    private final int dipoleCct345WindingNumber = 174; // 原 180，让最小匝间距>5mm
    private final int agCctWindingNumber3 = 16;
    private final int agCctWindingNumber4 = 60;
    private final int agCctWindingNumber5 = 59;  // sum 135

    // 电流
    private double dipoleCct12IInner = 9.898121356964111 * kA; // 求解获得
    private double dipoleCct12IOuter = dipoleCct12IInner;
    private double agCct12IInner = -4618.911905272398;//* (1 - 0.010335570469798657);
    private double agCct12IOuter = agCct12IInner;


    private double dipoleCct345IInner = -9953.93;
    private double dipoleCct345IOuter = dipoleCct345IInner;
    private double agCct345IInner = 10000; // 9799 // 2020年6月11日 *0.995
    private double agCct345IOuter = agCct345IInner;


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

    CosyArbitraryOrder.CosyMapArbitraryOrder map完整机架 = CosyArbitraryOrder.readMap(
            "  0.2596224      1.862474     0.0000000E+00 0.0000000E+00 0.1468336E-01 100000\n" +
                    " -0.4184481     0.8498938     0.0000000E+00 0.0000000E+00-0.2101781E-02 010000\n" +
                    "  0.0000000E+00 0.0000000E+00 0.8024085     0.7785208     0.0000000E+00 001000\n" +
                    "  0.0000000E+00 0.0000000E+00-0.2431872      1.010301     0.0000000E+00 000100\n" +
                    "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00  1.000000     000010\n" +
                    "  0.5598556E-02-0.1639381E-01 0.0000000E+00 0.0000000E+00  2.077917     000001\n" +
                    "  -10.83394      35.64159     0.0000000E+00 0.0000000E+00-0.5807074     200000\n" +
                    "   12.13149      43.06895     0.0000000E+00 0.0000000E+00 0.4172653     110000\n" +
                    "   7.280779     -2.334314     0.0000000E+00 0.0000000E+00 0.1246046     020000\n" +
                    "  0.0000000E+00 0.0000000E+00  131.0832     -63.93487     0.0000000E+00 101000\n" +
                    "  0.0000000E+00 0.0000000E+00  59.42912     -44.14807     0.0000000E+00 011000\n" +
                    "   42.68960      10.90738     0.0000000E+00 0.0000000E+00 -3.584823     002000\n" +
                    "  0.0000000E+00 0.0000000E+00  82.52457     -65.60024     0.0000000E+00 100100\n" +
                    "  0.0000000E+00 0.0000000E+00  1.312573     -60.17280     0.0000000E+00 010100\n" +
                    "   61.71111     -7.510766     0.0000000E+00 0.0000000E+00 -3.339891     001100\n" +
                    " -0.5102503     0.9820063     0.0000000E+00 0.0000000E+00-0.2155202     100001\n" +
                    " -0.6163846E-05 0.8754399E-01 0.0000000E+00 0.0000000E+00 0.4187320E-02 010001\n" +
                    "  0.0000000E+00 0.0000000E+00 -5.571651      5.425942     0.0000000E+00 001001\n" +
                    "   12.37878     -41.04267     0.0000000E+00 0.0000000E+00 0.6262018     000200\n" +
                    "  0.0000000E+00 0.0000000E+00-0.1095823E-05  5.370732     0.0000000E+00 000101\n" +
                    " -0.4401219E-01 0.9380933E-01 0.0000000E+00 0.0000000E+00 -1.795069     000002\n" +
                    "  -13.20922      635.7364     0.0000000E+00 0.0000000E+00  2.732125     300000\n" +
                    "  -533.1864      156.1866     0.0000000E+00 0.0000000E+00 -13.83974     210000\n" +
                    "  -663.9496     -1465.110     0.0000000E+00 0.0000000E+00 -13.14928     120000\n" +
                    "  -466.9126     -959.9705     0.0000000E+00 0.0000000E+00 -11.76161     030000\n" +
                    "  0.0000000E+00 0.0000000E+00  15354.96      1413.885     0.0000000E+00 201000\n" +
                    "  0.0000000E+00 0.0000000E+00  15162.39      2472.152     0.0000000E+00 111000\n" +
                    "  0.0000000E+00 0.0000000E+00  2617.802      1440.282     0.0000000E+00 021000\n" +
                    "   4245.033      1409.774     0.0000000E+00 0.0000000E+00  49.48966     102000\n" +
                    "   1246.931      431.8094     0.0000000E+00 0.0000000E+00  115.9975     012000\n" +
                    "  0.0000000E+00 0.0000000E+00  469.2207      156.1738     0.0000000E+00 003000\n" +
                    "  0.0000000E+00 0.0000000E+00  15434.75     -645.3084     0.0000000E+00 200100\n" +
                    "  0.0000000E+00 0.0000000E+00  11789.73      1642.274     0.0000000E+00 110100\n" +
                    "  0.0000000E+00 0.0000000E+00  932.6257      1556.705     0.0000000E+00 020100\n" +
                    "   13460.70      3525.846     0.0000000E+00 0.0000000E+00  297.5480     101100\n" +
                    "   2934.036      375.4320     0.0000000E+00 0.0000000E+00  494.7040     011100\n" +
                    "  0.0000000E+00 0.0000000E+00  1296.104      1820.046     0.0000000E+00 002100\n" +
                    "   12.79734      40.72083     0.0000000E+00 0.0000000E+00 -31.59138     200001\n" +
                    "   14.84136      29.25541     0.0000000E+00 0.0000000E+00 -35.16706     110001\n" +
                    "   4.247241      3.505801     0.0000000E+00 0.0000000E+00 -7.568016     020001\n" +
                    "  0.0000000E+00 0.0000000E+00  253.2319     -245.3414     0.0000000E+00 101001\n" +
                    "  0.0000000E+00 0.0000000E+00  163.4219     -178.2194     0.0000000E+00 011001\n" +
                    "   180.8828      37.96947     0.0000000E+00 0.0000000E+00 -39.57254     002001\n" +
                    "   8426.187      962.3133     0.0000000E+00 0.0000000E+00  269.2370     100200\n" +
                    "   1326.040     -826.9683     0.0000000E+00 0.0000000E+00  330.5342     010200\n" +
                    "  0.0000000E+00 0.0000000E+00  2142.529      4598.863     0.0000000E+00 001200\n" +
                    "  0.0000000E+00 0.0000000E+00  742.1934     -299.2294     0.0000000E+00 100101\n" +
                    "  0.0000000E+00 0.0000000E+00  697.4162     -281.8132     0.0000000E+00 010101\n" +
                    "   466.0038      94.36379     0.0000000E+00 0.0000000E+00 -108.2092     001101\n" +
                    "  -16.76482     -1.153843     0.0000000E+00 0.0000000E+00 0.7747687     100002\n" +
                    "  -7.869607     0.4578623     0.0000000E+00 0.0000000E+00 0.1967256     010002\n" +
                    "  0.0000000E+00 0.0000000E+00 -38.52057      4.725679     0.0000000E+00 001002\n" +
                    "  0.0000000E+00 0.0000000E+00  2680.843      3264.306     0.0000000E+00 000300\n" +
                    "   321.2698      113.3458     0.0000000E+00 0.0000000E+00 -50.56564     000201\n" +
                    "  0.0000000E+00 0.0000000E+00 -62.80678      23.42396     0.0000000E+00 000102\n" +
                    "  0.1746417    -0.9113106E-01 0.0000000E+00 0.0000000E+00  1.555164     000003\n" +
                    "   18838.23      18787.84     0.0000000E+00 0.0000000E+00  2682.974     400000\n" +
                    "   60024.02      25133.61     0.0000000E+00 0.0000000E+00  6515.431     310000\n" +
                    "   75633.04     -16499.90     0.0000000E+00 0.0000000E+00  6077.518     220000\n" +
                    "   48337.70     -23329.39     0.0000000E+00 0.0000000E+00  2836.201     130000\n" +
                    "   10644.88     -1773.023     0.0000000E+00 0.0000000E+00  447.8361     040000\n" +
                    "  0.0000000E+00 0.0000000E+00  626946.9      201853.4     0.0000000E+00 301000\n" +
                    "  0.0000000E+00 0.0000000E+00  751460.8      330872.8     0.0000000E+00 211000\n" +
                    "  0.0000000E+00 0.0000000E+00 -17631.47      171842.0     0.0000000E+00 121000\n" +
                    "  0.0000000E+00 0.0000000E+00 -129221.4      26594.51     0.0000000E+00 031000\n" +
                    "  -286921.2     -96459.73     0.0000000E+00 0.0000000E+00 -29815.66     202000\n" +
                    "  -411242.2     -133301.6     0.0000000E+00 0.0000000E+00 -33321.26     112000\n" +
                    "  -167638.8     -50774.00     0.0000000E+00 0.0000000E+00 -10252.17     022000\n" +
                    "  0.0000000E+00 0.0000000E+00  3544.856     -25417.82     0.0000000E+00 103000\n" +
                    "  0.0000000E+00 0.0000000E+00 -7416.278     -13381.05     0.0000000E+00 013000\n" +
                    "   20893.49      8518.194     0.0000000E+00 0.0000000E+00 -1930.880     004000\n" +
                    "  0.0000000E+00 0.0000000E+00  836711.7      237590.1     0.0000000E+00 300100\n" +
                    "  0.0000000E+00 0.0000000E+00  771806.4      340929.3     0.0000000E+00 210100\n" +
                    "  0.0000000E+00 0.0000000E+00 -193661.3      186527.5     0.0000000E+00 120100\n" +
                    "  0.0000000E+00 0.0000000E+00 -166124.7      25519.61     0.0000000E+00 030100\n" +
                    "  -256071.8     -97753.54     0.0000000E+00 0.0000000E+00 -43804.38     201100\n" +
                    "  -656625.2     -213886.0     0.0000000E+00 0.0000000E+00 -51277.76     111100\n" +
                    "  -364677.7     -95697.16     0.0000000E+00 0.0000000E+00 -20117.31     021100\n" +
                    "  0.0000000E+00 0.0000000E+00 -241473.2     -174793.6     0.0000000E+00 102100\n" +
                    "  0.0000000E+00 0.0000000E+00 -175223.1     -100028.8     0.0000000E+00 012100\n" +
                    "   114471.2      50989.09     0.0000000E+00 0.0000000E+00 -15832.87     003100\n" +
                    "   4707.222      2124.505     0.0000000E+00 0.0000000E+00 -1018.095     300001\n" +
                    "   7579.043      2249.179     0.0000000E+00 0.0000000E+00 -1506.291     210001\n" +
                    "   4240.607      747.2284     0.0000000E+00 0.0000000E+00 -173.4316     120001\n" +
                    "   1107.504      334.5291     0.0000000E+00 0.0000000E+00  231.3878     030001\n" +
                    "  0.0000000E+00 0.0000000E+00  53698.19      3430.195     0.0000000E+00 201001\n" +
                    "  0.0000000E+00 0.0000000E+00  49080.90      5408.109     0.0000000E+00 111001\n" +
                    "  0.0000000E+00 0.0000000E+00  9389.487      3080.874     0.0000000E+00 021001\n" +
                    "   61195.53      21306.53     0.0000000E+00 0.0000000E+00  2974.895     102001\n" +
                    "   28372.44      9641.707     0.0000000E+00 0.0000000E+00  2547.112     012001\n" +
                    "  0.0000000E+00 0.0000000E+00  2374.132      1979.626     0.0000000E+00 003001\n" +
                    "   125329.1      7211.397     0.0000000E+00 0.0000000E+00 -9795.906     200200\n" +
                    "  -257901.7     -104604.8     0.0000000E+00 0.0000000E+00 -17341.25     110200\n" +
                    "  -197581.9     -38985.61     0.0000000E+00 0.0000000E+00 -13905.89     020200\n" +
                    "  0.0000000E+00 0.0000000E+00 -696423.9     -343084.3     0.0000000E+00 101200\n" +
                    "  0.0000000E+00 0.0000000E+00 -394904.1     -225601.6     0.0000000E+00 011200\n" +
                    "   211236.1      116511.6     0.0000000E+00 0.0000000E+00 -47788.28     002200\n" +
                    "  0.0000000E+00 0.0000000E+00  99249.77      9976.247     0.0000000E+00 200101\n" +
                    "  0.0000000E+00 0.0000000E+00  105674.6      14148.28     0.0000000E+00 110101\n" +
                    "  0.0000000E+00 0.0000000E+00  24812.15      5375.793     0.0000000E+00 020101\n" +
                    "   130423.3      44063.93     0.0000000E+00 0.0000000E+00  4461.435     101101\n" +
                    "   56821.21      18010.02     0.0000000E+00 0.0000000E+00  5793.421     011101\n" +
                    "  0.0000000E+00 0.0000000E+00 -333.5896      12743.91     0.0000000E+00 002101\n" +
                    "  -180.8479      26.83199     0.0000000E+00 0.0000000E+00  42.05315     200002\n" +
                    "  -260.0203     -58.07276     0.0000000E+00 0.0000000E+00  57.84099     110002\n" +
                    "   45.95255     -24.03485     0.0000000E+00 0.0000000E+00  9.084118     020002\n" +
                    "  0.0000000E+00 0.0000000E+00  1041.008     -726.9788     0.0000000E+00 101002\n" +
                    "  0.0000000E+00 0.0000000E+00  1086.250     -325.4255     0.0000000E+00 011002\n" +
                    "  -316.8159     -147.1490     0.0000000E+00 0.0000000E+00 -252.3156     002002\n" +
                    "  0.0000000E+00 0.0000000E+00 -385038.5     -184109.7     0.0000000E+00 100300\n" +
                    "  0.0000000E+00 0.0000000E+00 -148919.1     -151119.9     0.0000000E+00 010300\n" +
                    "   149757.5      129232.8     0.0000000E+00 0.0000000E+00 -60514.23     001300\n" +
                    "   84194.12      28489.62     0.0000000E+00 0.0000000E+00  2201.303     100201\n" +
                    "   34583.20      10970.91     0.0000000E+00 0.0000000E+00  4960.300     010201\n" +
                    "  0.0000000E+00 0.0000000E+00 -25826.12      25595.54     0.0000000E+00 001201\n" +
                    "  0.0000000E+00 0.0000000E+00 -455.7208     -1771.902     0.0000000E+00 100102\n" +
                    "  0.0000000E+00 0.0000000E+00  271.7504     -500.2582     0.0000000E+00 010102\n" +
                    "   541.8056      107.1581     0.0000000E+00 0.0000000E+00 -686.3633     001102\n" +
                    "   15.41385     -1.555334     0.0000000E+00 0.0000000E+00 -5.483692     100003\n" +
                    "   9.314564     -4.344351     0.0000000E+00 0.0000000E+00 -2.595303     010003\n" +
                    "  0.0000000E+00 0.0000000E+00 -131.4402     -15.31155     0.0000000E+00 001003\n" +
                    "   33866.23      63380.83     0.0000000E+00 0.0000000E+00 -25826.38     000400\n" +
                    "  0.0000000E+00 0.0000000E+00 -38145.33      16302.52     0.0000000E+00 000301\n" +
                    "   422.1543      12.94548     0.0000000E+00 0.0000000E+00 -630.1081     000202\n" +
                    "  0.0000000E+00 0.0000000E+00 -235.6885     -62.76468     0.0000000E+00 000103\n" +
                    "  -1.251060     0.2828440E-01 0.0000000E+00 0.0000000E+00 -1.372619     000004\n" +
                    "   23381.87      292847.9     0.0000000E+00 0.0000000E+00  132349.5     500000\n" +
                    "  -408765.3      21297.08     0.0000000E+00 0.0000000E+00  333217.7     410000\n" +
                    "  -1770139.     -1800910.     0.0000000E+00 0.0000000E+00  246048.9     320000\n" +
                    "  -2488166.     -2323234.     0.0000000E+00 0.0000000E+00  13425.69     230000\n" +
                    "  -1518024.     -777196.9     0.0000000E+00 0.0000000E+00 -48347.45     140000\n" +
                    "  -382804.6     -2673.511     0.0000000E+00 0.0000000E+00 -15947.51     050000\n" +
                    "  0.0000000E+00 0.0000000E+00 0.1076761E+08  5136053.     0.0000000E+00 401000\n" +
                    "  0.0000000E+00 0.0000000E+00  3272748.      6359659.     0.0000000E+00 311000\n" +
                    "  0.0000000E+00 0.0000000E+00-0.3454217E+08 -4943053.     0.0000000E+00 221000\n" +
                    "  0.0000000E+00 0.0000000E+00-0.3172660E+08 -8170613.     0.0000000E+00 131000\n" +
                    "  0.0000000E+00 0.0000000E+00 -6215432.     -2438725.     0.0000000E+00 041000\n" +
                    " -0.2601973E+08 -9418704.     0.0000000E+00 0.0000000E+00 -1910197.     302000\n" +
                    " -0.3612576E+08-0.1310536E+08 0.0000000E+00 0.0000000E+00 -2387193.     212000\n" +
                    " -0.1173336E+08 -4389167.     0.0000000E+00 0.0000000E+00 -444422.6     122000\n" +
                    "   644906.0      104440.3     0.0000000E+00 0.0000000E+00  152662.7     032000\n" +
                    "  0.0000000E+00 0.0000000E+00  2424155.      531088.4     0.0000000E+00 203000\n" +
                    "  0.0000000E+00 0.0000000E+00  2974677.      697158.3     0.0000000E+00 113000\n" +
                    "  0.0000000E+00 0.0000000E+00  1110985.      278482.9     0.0000000E+00 023000\n" +
                    "   627927.9      289965.7     0.0000000E+00 0.0000000E+00  11484.23     104000\n" +
                    "  -81128.86     -27478.07     0.0000000E+00 0.0000000E+00  34852.42     014000\n" +
                    "  0.0000000E+00 0.0000000E+00  199367.9     -115014.1     0.0000000E+00 005000\n" +
                    "  0.0000000E+00 0.0000000E+00 0.1746715E+08  7912735.     0.0000000E+00 400100\n" +
                    "  0.0000000E+00 0.0000000E+00  4699490.      9591336.     0.0000000E+00 310100\n" +
                    "  0.0000000E+00 0.0000000E+00-0.4901295E+08 -5239990.     0.0000000E+00 220100\n" +
                    "  0.0000000E+00 0.0000000E+00-0.3860546E+08 -8733949.     0.0000000E+00 130100\n" +
                    "  0.0000000E+00 0.0000000E+00 -5601875.     -2671687.     0.0000000E+00 040100\n" +
                    " -0.6465121E+08-0.2348019E+08 0.0000000E+00 0.0000000E+00 -5044978.     301100\n" +
                    " -0.8351331E+08-0.3012094E+08 0.0000000E+00 0.0000000E+00 -5547417.     211100\n" +
                    " -0.2978935E+08-0.1026326E+08 0.0000000E+00 0.0000000E+00 -930502.2     121100\n" +
                    "   288406.7      188634.8     0.0000000E+00 0.0000000E+00  304037.1     031100\n" +
                    "  0.0000000E+00 0.0000000E+00 -4204381.     -2072899.     0.0000000E+00 202100\n" +
                    "  0.0000000E+00 0.0000000E+00 -865578.3     -1275047.     0.0000000E+00 112100\n" +
                    "  0.0000000E+00 0.0000000E+00  4160339.      415276.3     0.0000000E+00 022100\n" +
                    "  0.1353190E+08  5248000.     0.0000000E+00 0.0000000E+00  924231.9     103100\n" +
                    "   4248256.      1461662.     0.0000000E+00 0.0000000E+00  794771.7     013100\n" +
                    "  0.0000000E+00 0.0000000E+00  1717888.     -760803.4     0.0000000E+00 004100\n" +
                    "   203878.1      89906.21     0.0000000E+00 0.0000000E+00 -19890.52     400001\n" +
                    "   419665.2      166992.3     0.0000000E+00 0.0000000E+00 -18678.25     310001\n" +
                    "   190751.8      85960.48     0.0000000E+00 0.0000000E+00  43997.34     220001\n" +
                    "  -40685.54      13425.43     0.0000000E+00 0.0000000E+00  55495.43     130001\n" +
                    "  -29917.67     -4223.878     0.0000000E+00 0.0000000E+00  14053.08     040001\n" +
                    "  0.0000000E+00 0.0000000E+00  2333907.      832992.9     0.0000000E+00 301001\n" +
                    "  0.0000000E+00 0.0000000E+00  2550483.      1333844.     0.0000000E+00 211001\n" +
                    "  0.0000000E+00 0.0000000E+00 -15557.27      754939.3     0.0000000E+00 121001\n" +
                    "  0.0000000E+00 0.0000000E+00 -342020.1      159348.1     0.0000000E+00 031001\n" +
                    "  -427610.8     -145746.4     0.0000000E+00 0.0000000E+00 -56465.47     202001\n" +
                    "  -1784410.     -629184.0     0.0000000E+00 0.0000000E+00 -108397.3     112001\n" +
                    "  -1332052.     -465350.5     0.0000000E+00 0.0000000E+00 -66674.42     022001\n" +
                    "  0.0000000E+00 0.0000000E+00 -234624.0     -195703.2     0.0000000E+00 103001\n" +
                    "  0.0000000E+00 0.0000000E+00 -288183.9     -87730.51     0.0000000E+00 013001\n" +
                    "   110848.6      45480.19     0.0000000E+00 0.0000000E+00 -15195.24     004001\n" +
                    " -0.3660253E+08-0.1385361E+08 0.0000000E+00 0.0000000E+00 -3091446.     300200\n" +
                    " -0.4948932E+08-0.1833953E+08 0.0000000E+00 0.0000000E+00 -3364866.     210200\n" +
                    " -0.2353023E+08 -7371983.     0.0000000E+00 0.0000000E+00 -866354.0     120200\n" +
                    "  -1056665.      166043.5     0.0000000E+00 0.0000000E+00  12822.87     030200\n" +
                    "  0.0000000E+00 0.0000000E+00-0.3156753E+08 -9325532.     0.0000000E+00 201200\n" +
                    "  0.0000000E+00 0.0000000E+00-0.1846358E+08 -6183944.     0.0000000E+00 111200\n" +
                    "  0.0000000E+00 0.0000000E+00  8185348.      211559.2     0.0000000E+00 021200\n" +
                    "  0.4447226E+08 0.1717219E+08 0.0000000E+00 0.0000000E+00  3300455.     102200\n" +
                    "  0.1584687E+08  5548305.     0.0000000E+00 0.0000000E+00  2877402.     012200\n" +
                    "  0.0000000E+00 0.0000000E+00  6309750.     -1420777.     0.0000000E+00 003200\n" +
                    "  0.0000000E+00 0.0000000E+00  3841753.      1370908.     0.0000000E+00 300101\n" +
                    "  0.0000000E+00 0.0000000E+00  4740930.      2354014.     0.0000000E+00 210101\n" +
                    "  0.0000000E+00 0.0000000E+00  345087.2      1338725.     0.0000000E+00 120101\n" +
                    "  0.0000000E+00 0.0000000E+00 -656926.5      256629.2     0.0000000E+00 030101\n" +
                    "  -1227534.     -378058.0     0.0000000E+00 0.0000000E+00 -124919.2     201101\n" +
                    "  -6388552.     -2227286.     0.0000000E+00 0.0000000E+00 -345631.4     111101\n" +
                    "  -4138083.     -1451585.     0.0000000E+00 0.0000000E+00 -189062.4     021101\n" +
                    "  0.0000000E+00 0.0000000E+00 -1867151.     -936351.5     0.0000000E+00 102101\n" +
                    "  0.0000000E+00 0.0000000E+00 -1711960.     -402522.3     0.0000000E+00 012101\n" +
                    "   1577.409      37565.21     0.0000000E+00 0.0000000E+00 -158086.6     003101\n" +
                    "   7762.513      5843.796     0.0000000E+00 0.0000000E+00  1308.448     300002\n" +
                    "   20005.96      10110.42     0.0000000E+00 0.0000000E+00  2661.505     210002\n" +
                    "   22297.98      7526.452     0.0000000E+00 0.0000000E+00  472.2260     120002\n" +
                    "   9559.296      3222.871     0.0000000E+00 0.0000000E+00 -570.4420     030002\n" +
                    "  0.0000000E+00 0.0000000E+00  190532.5      49167.27     0.0000000E+00 201002\n" +
                    "  0.0000000E+00 0.0000000E+00  202600.3      55526.23     0.0000000E+00 111002\n" +
                    "  0.0000000E+00 0.0000000E+00  53348.16      19164.81     0.0000000E+00 021002\n" +
                    "   262866.4      104479.4     0.0000000E+00 0.0000000E+00  15541.32     102002\n" +
                    "   151304.0      61083.01     0.0000000E+00 0.0000000E+00  11761.89     012002\n" +
                    "  0.0000000E+00 0.0000000E+00  26089.21      3173.183     0.0000000E+00 003002\n" +
                    "  0.0000000E+00 0.0000000E+00-0.2614538E+08 -5808839.     0.0000000E+00 200300\n" +
                    "  0.0000000E+00 0.0000000E+00 -8633604.     -1284236.     0.0000000E+00 110300\n" +
                    "  0.0000000E+00 0.0000000E+00  8667653.      1144818.     0.0000000E+00 020300\n" +
                    "  0.5039472E+08 0.2024415E+08 0.0000000E+00 0.0000000E+00  3647571.     101300\n" +
                    "  0.1826963E+08  6740532.     0.0000000E+00 0.0000000E+00  3783614.     011300\n" +
                    "  0.0000000E+00 0.0000000E+00 0.1136780E+08  389622.4     0.0000000E+00 002300\n" +
                    "  -749734.0     -151036.2     0.0000000E+00 0.0000000E+00 -37386.19     200201\n" +
                    "  -4118857.     -1373225.     0.0000000E+00 0.0000000E+00 -138264.6     110201\n" +
                    "  -2730173.     -966221.4     0.0000000E+00 0.0000000E+00 -72030.45     020201\n" +
                    "  0.0000000E+00 0.0000000E+00 -4513595.     -1531265.     0.0000000E+00 101201\n" +
                    "  0.0000000E+00 0.0000000E+00 -3527980.     -599183.6     0.0000000E+00 011201\n" +
                    "  -1105603.     -316711.3     0.0000000E+00 0.0000000E+00 -489154.8     002201\n" +
                    "  0.0000000E+00 0.0000000E+00  235444.9      59253.73     0.0000000E+00 200102\n" +
                    "  0.0000000E+00 0.0000000E+00  214329.3      52872.72     0.0000000E+00 110102\n" +
                    "  0.0000000E+00 0.0000000E+00  61219.48      25272.06     0.0000000E+00 020102\n" +
                    "   821866.5      318347.1     0.0000000E+00 0.0000000E+00  49363.50     101102\n" +
                    "   503370.3      200545.6     0.0000000E+00 0.0000000E+00  39928.36     011102\n" +
                    "  0.0000000E+00 0.0000000E+00  73754.07      2252.185     0.0000000E+00 002102\n" +
                    "   821.6285      147.2082     0.0000000E+00 0.0000000E+00 -211.9918     200003\n" +
                    "   1130.539      155.2821     0.0000000E+00 0.0000000E+00 -264.8578     110003\n" +
                    "   152.2549      7.167064     0.0000000E+00 0.0000000E+00 -25.43908     020003\n" +
                    "  0.0000000E+00 0.0000000E+00  193.3138     -916.3047     0.0000000E+00 101003\n" +
                    "  0.0000000E+00 0.0000000E+00 -482.5110      144.5095     0.0000000E+00 011003\n" +
                    "  -4518.487     -1995.037     0.0000000E+00 0.0000000E+00 -531.3272     002003\n" +
                    "  0.2001841E+08  8756858.     0.0000000E+00 0.0000000E+00  1232099.     100400\n" +
                    "   7748086.      3184528.     0.0000000E+00 0.0000000E+00  1808419.     010400\n" +
                    "  0.0000000E+00 0.0000000E+00  8494084.      3346130.     0.0000000E+00 001400\n" +
                    "  0.0000000E+00 0.0000000E+00 -4148702.     -1044220.     0.0000000E+00 100301\n" +
                    "  0.0000000E+00 0.0000000E+00 -3032336.     -400915.6     0.0000000E+00 010301\n" +
                    "  -1854380.     -625478.8     0.0000000E+00 0.0000000E+00 -625606.9     001301\n" +
                    "   546400.4      209542.4     0.0000000E+00 0.0000000E+00  27538.71     100202\n" +
                    "   314653.9      129086.8     0.0000000E+00 0.0000000E+00  21328.00     010202\n" +
                    "  0.0000000E+00 0.0000000E+00  65255.60     -19362.79     0.0000000E+00 001202\n" +
                    "  0.0000000E+00 0.0000000E+00  4902.726      967.8473     0.0000000E+00 100103\n" +
                    "  0.0000000E+00 0.0000000E+00  1912.279      1004.407     0.0000000E+00 010103\n" +
                    "  -16470.71     -6992.344     0.0000000E+00 0.0000000E+00 -1835.512     001103\n" +
                    "  -31.85166      2.410040     0.0000000E+00 0.0000000E+00  14.89172     100004\n" +
                    "  -24.94398      7.478883     0.0000000E+00 0.0000000E+00  8.334573     010004\n" +
                    "  0.0000000E+00 0.0000000E+00  48.39722     -121.3425     0.0000000E+00 001004\n" +
                    "  0.0000000E+00 0.0000000E+00  939522.2      2267291.     0.0000000E+00 000500\n" +
                    "  -979561.8     -386291.6     0.0000000E+00 0.0000000E+00 -315832.5     000401\n" +
                    "  0.0000000E+00 0.0000000E+00  66165.54     -19116.15     0.0000000E+00 000302\n" +
                    "  -7034.074     -3399.347     0.0000000E+00 0.0000000E+00 -764.5798     000203\n" +
                    "  0.0000000E+00 0.0000000E+00  228.1602     -197.9849     0.0000000E+00 000104\n" +
                    "   3.004751    -0.1682839     0.0000000E+00 0.0000000E+00  1.095465     000005"
    );

    CosyArbitraryOrder.CosyMapArbitraryOrder mapQS3S150000 = CosyArbitraryOrder.readMap(
            "   1.024549      2.107310     0.0000000E+00 0.0000000E+00-0.3644321E-02 100000\n" +
                    " -0.7802118E-02 0.9599918     0.0000000E+00 0.0000000E+00 0.1072333E-03 010000\n" +
                    "  0.0000000E+00 0.0000000E+00  1.000733     0.5102768     0.0000000E+00 001000\n" +
                    "  0.0000000E+00 0.0000000E+00-0.1753608E-02 0.9983737     0.0000000E+00 000100\n" +
                    "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00  1.000000     000010\n" +
                    "  0.8143233E-04 0.3724492E-02 0.0000000E+00 0.0000000E+00  2.148024     000001\n" +
                    "  -15.55683      2.989026     0.0000000E+00 0.0000000E+00 -6.163693     200000\n" +
                    "  -9.824239      8.900999     0.0000000E+00 0.0000000E+00 -7.104304     110000\n" +
                    "  0.2775077      5.139497     0.0000000E+00 0.0000000E+00 -2.849306     020000\n" +
                    "  0.0000000E+00 0.0000000E+00  51.40475     -30.48574     0.0000000E+00 101000\n" +
                    "  0.0000000E+00 0.0000000E+00  25.74279     -15.68466     0.0000000E+00 011000\n" +
                    "   14.99129      3.144726     0.0000000E+00 0.0000000E+00 -3.113676     002000\n" +
                    "  0.0000000E+00 0.0000000E+00 -10.44601     -56.55661     0.0000000E+00 100100\n" +
                    "  0.0000000E+00 0.0000000E+00 0.9567487E-02 -25.64974     0.0000000E+00 010100\n" +
                    "   26.70367      4.885288     0.0000000E+00 0.0000000E+00 -5.697841     001100\n" +
                    "  -7.414026     -3.103702     0.0000000E+00 0.0000000E+00 0.8289901E-02 100001\n" +
                    "  -5.892965     -5.150252     0.0000000E+00 0.0000000E+00 0.5677702E-01 010001\n" +
                    "  0.0000000E+00 0.0000000E+00 -5.613608      3.249319     0.0000000E+00 001001\n" +
                    " -0.5921980E-01  5.016162     0.0000000E+00 0.0000000E+00 -7.721957     000200\n" +
                    "  0.0000000E+00 0.0000000E+00 -15.46631     -2.291637     0.0000000E+00 000101\n" +
                    "  0.1798231E-01 0.4629328E-01 0.0000000E+00 0.0000000E+00 -2.027027     000002\n" +
                    "   133.8347      21.56321     0.0000000E+00 0.0000000E+00 -16.20036     300000\n" +
                    "   135.3615      114.7003     0.0000000E+00 0.0000000E+00 -42.62608     210000\n" +
                    "   60.72109      154.8827     0.0000000E+00 0.0000000E+00 -40.66687     120000\n" +
                    "   19.31005      54.82091     0.0000000E+00 0.0000000E+00 -11.60931     030000\n" +
                    "  0.0000000E+00 0.0000000E+00  3495.155      1611.011     0.0000000E+00 201000\n" +
                    "  0.0000000E+00 0.0000000E+00  3779.750      1641.822     0.0000000E+00 111000\n" +
                    "  0.0000000E+00 0.0000000E+00  1071.324      408.5695     0.0000000E+00 021000\n" +
                    "  -11.13599     -7.134253     0.0000000E+00 0.0000000E+00 -17.27072     102000\n" +
                    "  -15.15540     0.2800745     0.0000000E+00 0.0000000E+00 -3.052146     012000\n" +
                    "  0.0000000E+00 0.0000000E+00  32.44856     -6.892223     0.0000000E+00 003000\n" +
                    "  0.0000000E+00 0.0000000E+00  3682.041      1611.116     0.0000000E+00 200100\n" +
                    "  0.0000000E+00 0.0000000E+00  4349.762      1380.083     0.0000000E+00 110100\n" +
                    "  0.0000000E+00 0.0000000E+00  1307.274      256.7308     0.0000000E+00 020100\n" +
                    "   1943.859      633.5359     0.0000000E+00 0.0000000E+00  118.5958     101100\n" +
                    "   1255.298      461.4039     0.0000000E+00 0.0000000E+00  29.00065     011100\n" +
                    "  0.0000000E+00 0.0000000E+00  188.3554      20.42528     0.0000000E+00 002100\n" +
                    "   56.41845      24.51137     0.0000000E+00 0.0000000E+00  31.18102     200001\n" +
                    "   100.6227      4.245974     0.0000000E+00 0.0000000E+00  50.46459     110001\n" +
                    "   24.14496     -11.43562     0.0000000E+00 0.0000000E+00  19.63316     020001\n" +
                    "  0.0000000E+00 0.0000000E+00 -272.6543     -53.93504     0.0000000E+00 101001\n" +
                    "  0.0000000E+00 0.0000000E+00 -219.4448      15.98320     0.0000000E+00 011001\n" +
                    "  -7.861043      4.734298     0.0000000E+00 0.0000000E+00  10.92980     002001\n" +
                    "   2065.891      810.4177     0.0000000E+00 0.0000000E+00  108.5755     100200\n" +
                    "   1358.743      590.7497     0.0000000E+00 0.0000000E+00  14.11622     010200\n" +
                    "  0.0000000E+00 0.0000000E+00  492.6878     -87.38364     0.0000000E+00 001200\n" +
                    "  0.0000000E+00 0.0000000E+00 -544.9210      231.9488     0.0000000E+00 100101\n" +
                    "  0.0000000E+00 0.0000000E+00 -299.6206      223.6270     0.0000000E+00 010101\n" +
                    "  -428.3648     -85.64719     0.0000000E+00 0.0000000E+00  21.06667     001101\n" +
                    "   15.45517      2.662171     0.0000000E+00 0.0000000E+00 -1.514668     100002\n" +
                    "   20.31630      7.867901     0.0000000E+00 0.0000000E+00 -1.255315     010002\n" +
                    "  0.0000000E+00 0.0000000E+00 -20.66600     -22.70378     0.0000000E+00 001002\n" +
                    "  0.0000000E+00 0.0000000E+00  286.0800     -107.3453     0.0000000E+00 000300\n" +
                    "  -392.6009     -90.52995     0.0000000E+00 0.0000000E+00  22.97537     000201\n" +
                    "  0.0000000E+00 0.0000000E+00  21.42468     -31.49152     0.0000000E+00 000102\n" +
                    " -0.5314488    -0.4319445     0.0000000E+00 0.0000000E+00  1.947447     000003\n" +
                    "   212.1544     -253.7712     0.0000000E+00 0.0000000E+00 -265.1630     400000\n" +
                    "   256.4573     -95.34188     0.0000000E+00 0.0000000E+00 -940.1610     310000\n" +
                    "  -399.4302      742.4105     0.0000000E+00 0.0000000E+00 -1310.206     220000\n" +
                    "  -509.1836      782.8763     0.0000000E+00 0.0000000E+00 -753.2202     130000\n" +
                    "  -150.5213      196.9384     0.0000000E+00 0.0000000E+00 -149.6086     040000\n" +
                    "  0.0000000E+00 0.0000000E+00 -6627.222     -7037.838     0.0000000E+00 301000\n" +
                    "  0.0000000E+00 0.0000000E+00  6782.134     -6364.274     0.0000000E+00 211000\n" +
                    "  0.0000000E+00 0.0000000E+00  18799.20     -122.0563     0.0000000E+00 121000\n" +
                    "  0.0000000E+00 0.0000000E+00  7038.466      649.9903     0.0000000E+00 031000\n" +
                    "  -49743.26     -15826.26     0.0000000E+00 0.0000000E+00 -5865.340     202000\n" +
                    "  -49837.16     -15834.55     0.0000000E+00 0.0000000E+00 -6299.156     112000\n" +
                    "  -11820.06     -3732.280     0.0000000E+00 0.0000000E+00 -1799.669     022000\n" +
                    "  0.0000000E+00 0.0000000E+00 -668.4003     -1127.545     0.0000000E+00 103000\n" +
                    "  0.0000000E+00 0.0000000E+00 -302.1985     -680.0133     0.0000000E+00 013000\n" +
                    "   368.5160      65.97253     0.0000000E+00 0.0000000E+00 -101.4052     004000\n" +
                    "  0.0000000E+00 0.0000000E+00  3221.489     -5946.259     0.0000000E+00 300100\n" +
                    "  0.0000000E+00 0.0000000E+00  54676.04      4646.476     0.0000000E+00 210100\n" +
                    "  0.0000000E+00 0.0000000E+00  62559.81      9633.126     0.0000000E+00 120100\n" +
                    "  0.0000000E+00 0.0000000E+00  18441.38      2750.026     0.0000000E+00 030100\n" +
                    "  -121120.4     -36729.86     0.0000000E+00 0.0000000E+00 -18394.51     201100\n" +
                    "  -115700.5     -33913.09     0.0000000E+00 0.0000000E+00 -20319.57     111100\n" +
                    "  -24450.68     -6669.285     0.0000000E+00 0.0000000E+00 -5686.435     021100\n" +
                    "  0.0000000E+00 0.0000000E+00 -1065.495     -6642.986     0.0000000E+00 102100\n" +
                    "  0.0000000E+00 0.0000000E+00  1401.384     -5131.752     0.0000000E+00 012100\n" +
                    "   3220.505      515.0310     0.0000000E+00 0.0000000E+00 -376.6148     003100\n" +
                    "  -1305.461      168.7071     0.0000000E+00 0.0000000E+00  91.14254     300001\n" +
                    "  -2323.379      218.8601     0.0000000E+00 0.0000000E+00  371.9162     210001\n" +
                    "  -1208.005     -110.9201     0.0000000E+00 0.0000000E+00  394.3186     120001\n" +
                    "  -190.9946     -141.2583     0.0000000E+00 0.0000000E+00  129.3085     030001\n" +
                    "  0.0000000E+00 0.0000000E+00 -13662.79     -4529.133     0.0000000E+00 201001\n" +
                    "  0.0000000E+00 0.0000000E+00 -25594.78     -9674.566     0.0000000E+00 111001\n" +
                    "  0.0000000E+00 0.0000000E+00 -9895.448     -3768.720     0.0000000E+00 021001\n" +
                    "   9790.157      3056.799     0.0000000E+00 0.0000000E+00  837.8091     102001\n" +
                    "   5189.430      1708.671     0.0000000E+00 0.0000000E+00  395.5949     012001\n" +
                    "  0.0000000E+00 0.0000000E+00  527.2220      277.3536     0.0000000E+00 003001\n" +
                    "  -69562.78     -19662.14     0.0000000E+00 0.0000000E+00 -13794.13     200200\n" +
                    "  -48696.42     -11003.10     0.0000000E+00 0.0000000E+00 -14117.47     110200\n" +
                    "  -2592.555      1023.305     0.0000000E+00 0.0000000E+00 -3645.351     020200\n" +
                    "  0.0000000E+00 0.0000000E+00  16786.76     -1638.646     0.0000000E+00 101200\n" +
                    "  0.0000000E+00 0.0000000E+00  12963.52     -4432.990     0.0000000E+00 011200\n" +
                    "   5640.878      433.4293     0.0000000E+00 0.0000000E+00 -400.9904     002200\n" +
                    "  0.0000000E+00 0.0000000E+00 -46287.93     -16025.81     0.0000000E+00 200101\n" +
                    "  0.0000000E+00 0.0000000E+00 -62954.35     -20746.41     0.0000000E+00 110101\n" +
                    "  0.0000000E+00 0.0000000E+00 -21316.84     -5848.164     0.0000000E+00 020101\n" +
                    "   12955.21      4697.053     0.0000000E+00 0.0000000E+00  1767.497     101101\n" +
                    "   3725.388      1782.744     0.0000000E+00 0.0000000E+00  714.8984     011101\n" +
                    "  0.0000000E+00 0.0000000E+00  1229.938      519.6303     0.0000000E+00 002101\n" +
                    "  -46.54963     -71.25803     0.0000000E+00 0.0000000E+00 -84.27778     200002\n" +
                    "  -192.2859     -98.34819     0.0000000E+00 0.0000000E+00 -192.1848     110002\n" +
                    "  -112.1360     -6.217256     0.0000000E+00 0.0000000E+00 -94.24447     020002\n" +
                    "  0.0000000E+00 0.0000000E+00  266.5423      1017.003     0.0000000E+00 101002\n" +
                    "  0.0000000E+00 0.0000000E+00  611.1605      630.1196     0.0000000E+00 011002\n" +
                    "  -1088.972     -302.7877     0.0000000E+00 0.0000000E+00 -8.692000     002002\n" +
                    "  0.0000000E+00 0.0000000E+00  23371.29     -1493.979     0.0000000E+00 100300\n" +
                    "  0.0000000E+00 0.0000000E+00  15298.79     -4320.935     0.0000000E+00 010300\n" +
                    "   16252.69      3513.402     0.0000000E+00 0.0000000E+00  216.9848     001300\n" +
                    "  -12836.96     -3344.765     0.0000000E+00 0.0000000E+00 -270.7259     100201\n" +
                    "  -13068.25     -3763.531     0.0000000E+00 0.0000000E+00 -123.9337     010201\n" +
                    "  0.0000000E+00 0.0000000E+00  659.5262      656.6921     0.0000000E+00 001201\n" +
                    "  0.0000000E+00 0.0000000E+00  1778.655      414.2408     0.0000000E+00 100102\n" +
                    "  0.0000000E+00 0.0000000E+00  1768.528     -232.6140     0.0000000E+00 010102\n" +
                    "  -960.7863     -417.1942     0.0000000E+00 0.0000000E+00 -11.50578     001102\n" +
                    "  -11.72631      2.600712     0.0000000E+00 0.0000000E+00  8.354117     100003\n" +
                    "  -38.14759     -9.726171     0.0000000E+00 0.0000000E+00  7.650642     010003\n" +
                    "  0.0000000E+00 0.0000000E+00  140.8368      25.66924     0.0000000E+00 001003\n" +
                    "   13244.77      3803.603     0.0000000E+00 0.0000000E+00  184.7381     000400\n" +
                    "  0.0000000E+00 0.0000000E+00 -2069.276      1645.736     0.0000000E+00 000301\n" +
                    "   1917.169      422.6803     0.0000000E+00 0.0000000E+00 -81.16094     000202\n" +
                    "  0.0000000E+00 0.0000000E+00  112.6532      113.3677     0.0000000E+00 000103\n" +
                    "   2.213372      1.062088     0.0000000E+00 0.0000000E+00 -2.111290     000004\n" +
                    "   79810.64      16677.33     0.0000000E+00 0.0000000E+00 -1250.798     500000\n" +
                    "   220864.8      44294.20     0.0000000E+00 0.0000000E+00 -7476.252     410000\n" +
                    "   246831.1      50622.90     0.0000000E+00 0.0000000E+00 -15632.15     320000\n" +
                    "   136047.8      32411.53     0.0000000E+00 0.0000000E+00 -14699.66     230000\n" +
                    "   35997.75      11406.91     0.0000000E+00 0.0000000E+00 -6416.964     140000\n" +
                    "   3420.092      1644.387     0.0000000E+00 0.0000000E+00 -1063.055     050000\n" +
                    "  0.0000000E+00 0.0000000E+00  252068.3      118347.8     0.0000000E+00 401000\n" +
                    "  0.0000000E+00 0.0000000E+00  856761.3      349437.1     0.0000000E+00 311000\n" +
                    "  0.0000000E+00 0.0000000E+00  1162509.      431014.0     0.0000000E+00 221000\n" +
                    "  0.0000000E+00 0.0000000E+00  671085.9      235229.0     0.0000000E+00 131000\n" +
                    "  0.0000000E+00 0.0000000E+00  137928.5      46707.61     0.0000000E+00 041000\n" +
                    "   766525.0      220183.5     0.0000000E+00 0.0000000E+00  186980.4     302000\n" +
                    "   1012497.      285646.7     0.0000000E+00 0.0000000E+00  261988.9     212000\n" +
                    "   375775.2      101922.8     0.0000000E+00 0.0000000E+00  111218.8     122000\n" +
                    "   29511.89      6783.488     0.0000000E+00 0.0000000E+00  13495.60     032000\n" +
                    "  0.0000000E+00 0.0000000E+00  130299.6      50234.52     0.0000000E+00 203000\n" +
                    "  0.0000000E+00 0.0000000E+00  153714.1      58710.55     0.0000000E+00 113000\n" +
                    "  0.0000000E+00 0.0000000E+00  47045.86      14737.38     0.0000000E+00 023000\n" +
                    "  -38148.05     -11082.35     0.0000000E+00 0.0000000E+00 -1656.034     104000\n" +
                    "  -23300.19     -7439.698     0.0000000E+00 0.0000000E+00  17.47236     014000\n" +
                    "  0.0000000E+00 0.0000000E+00 -1533.289     -815.5271     0.0000000E+00 005000\n" +
                    "  0.0000000E+00 0.0000000E+00  487958.0      203847.6     0.0000000E+00 400100\n" +
                    "  0.0000000E+00 0.0000000E+00  1713848.      634497.8     0.0000000E+00 310100\n" +
                    "  0.0000000E+00 0.0000000E+00  2325645.      795826.0     0.0000000E+00 220100\n" +
                    "  0.0000000E+00 0.0000000E+00  1363505.      440330.7     0.0000000E+00 130100\n" +
                    "  0.0000000E+00 0.0000000E+00  288299.2      88832.63     0.0000000E+00 040100\n" +
                    "   1718269.      479929.7     0.0000000E+00 0.0000000E+00  457647.8     301100\n" +
                    "   1509953.      385454.2     0.0000000E+00 0.0000000E+00  529859.9     211100\n" +
                    "   100644.7     -5485.718     0.0000000E+00 0.0000000E+00  154836.6     121100\n" +
                    "  -108708.1     -37364.26     0.0000000E+00 0.0000000E+00  1350.105     031100\n" +
                    "  0.0000000E+00 0.0000000E+00  1391429.      580668.5     0.0000000E+00 202100\n" +
                    "  0.0000000E+00 0.0000000E+00  1723069.      696179.8     0.0000000E+00 112100\n" +
                    "  0.0000000E+00 0.0000000E+00  531627.7      197079.0     0.0000000E+00 022100\n" +
                    "  -125396.4     -42271.47     0.0000000E+00 0.0000000E+00 -4611.514     103100\n" +
                    "  -77719.81     -30108.48     0.0000000E+00 0.0000000E+00 -179.8128     013100\n" +
                    "  0.0000000E+00 0.0000000E+00 -10471.30      1093.058     0.0000000E+00 004100\n" +
                    "  -1432.353      3093.144     0.0000000E+00 0.0000000E+00  498.2754     400001\n" +
                    "  -11806.07      10423.89     0.0000000E+00 0.0000000E+00  4553.484     310001\n" +
                    "  -15241.33      10079.06     0.0000000E+00 0.0000000E+00  9233.343     220001\n" +
                    "  -5694.512      2659.492     0.0000000E+00 0.0000000E+00  6979.577     130001\n" +
                    "  -267.1443     -164.7574     0.0000000E+00 0.0000000E+00  1746.591     040001\n" +
                    "  0.0000000E+00 0.0000000E+00  82838.67      34621.67     0.0000000E+00 301001\n" +
                    "  0.0000000E+00 0.0000000E+00  77782.55      55009.80     0.0000000E+00 211001\n" +
                    "  0.0000000E+00 0.0000000E+00 -45747.88      13165.71     0.0000000E+00 121001\n" +
                    "  0.0000000E+00 0.0000000E+00 -42080.76     -4819.116     0.0000000E+00 031001\n" +
                    "   89588.10      26663.24     0.0000000E+00 0.0000000E+00 -2678.656     202001\n" +
                    "   255434.6      81669.52     0.0000000E+00 0.0000000E+00  15827.15     112001\n" +
                    "   113050.9      36829.33     0.0000000E+00 0.0000000E+00  10025.83     022001\n" +
                    "  0.0000000E+00 0.0000000E+00  18346.36     -4623.630     0.0000000E+00 103001\n" +
                    "  0.0000000E+00 0.0000000E+00  12619.29     -3728.869     0.0000000E+00 013001\n" +
                    "   10884.14      2941.459     0.0000000E+00 0.0000000E+00  4.916576     004001\n" +
                    "   1065498.      284075.6     0.0000000E+00 0.0000000E+00  265212.8     300200\n" +
                    "   697575.8      162308.4     0.0000000E+00 0.0000000E+00  216506.1     210200\n" +
                    "  -82008.53     -30730.46     0.0000000E+00 0.0000000E+00 -7593.275     120200\n" +
                    "  -64543.10     -13691.66     0.0000000E+00 0.0000000E+00 -25120.71     030200\n" +
                    "  0.0000000E+00 0.0000000E+00  2716177.      1089016.     0.0000000E+00 201200\n" +
                    "  0.0000000E+00 0.0000000E+00  3527334.      1307575.     0.0000000E+00 111200\n" +
                    "  0.0000000E+00 0.0000000E+00  1149390.      365778.7     0.0000000E+00 021200\n" +
                    "  -438205.5     -157437.1     0.0000000E+00 0.0000000E+00 -31656.69     102200\n" +
                    "  -123072.6     -60050.62     0.0000000E+00 0.0000000E+00 -13286.93     012200\n" +
                    "  0.0000000E+00 0.0000000E+00 -41225.96      11272.12     0.0000000E+00 003200\n" +
                    "  0.0000000E+00 0.0000000E+00 -116551.5     -9850.681     0.0000000E+00 300101\n" +
                    "  0.0000000E+00 0.0000000E+00 -578506.3     -109495.8     0.0000000E+00 210101\n" +
                    "  0.0000000E+00 0.0000000E+00 -719609.7     -160458.3     0.0000000E+00 120101\n" +
                    "  0.0000000E+00 0.0000000E+00 -247220.6     -56369.27     0.0000000E+00 030101\n" +
                    "   1078238.      334635.3     0.0000000E+00 0.0000000E+00  121767.7     201101\n" +
                    "   1617536.      508662.6     0.0000000E+00 0.0000000E+00  201613.8     111101\n" +
                    "   546867.6      172811.9     0.0000000E+00 0.0000000E+00  75920.87     021101\n" +
                    "  0.0000000E+00 0.0000000E+00  14188.67      757.9581     0.0000000E+00 102101\n" +
                    "  0.0000000E+00 0.0000000E+00  18064.39      3322.536     0.0000000E+00 012101\n" +
                    "   23515.28      7887.507     0.0000000E+00 0.0000000E+00  2560.678     003101\n" +
                    "   3800.326     -879.8697     0.0000000E+00 0.0000000E+00 -219.9361     300002\n" +
                    "   11128.06     -2515.512     0.0000000E+00 0.0000000E+00 -1528.395     210002\n" +
                    "   8668.125     -1603.881     0.0000000E+00 0.0000000E+00 -2068.988     120002\n" +
                    "   1942.789     -119.3353     0.0000000E+00 0.0000000E+00 -775.5883     030002\n" +
                    "  0.0000000E+00 0.0000000E+00 -54277.56     -25773.83     0.0000000E+00 201002\n" +
                    "  0.0000000E+00 0.0000000E+00 -10062.34     -9283.832     0.0000000E+00 111002\n" +
                    "  0.0000000E+00 0.0000000E+00  18214.06      6291.743     0.0000000E+00 021002\n" +
                    "  -50340.46     -14284.79     0.0000000E+00 0.0000000E+00 -3535.720     102002\n" +
                    "  -40375.31     -11952.19     0.0000000E+00 0.0000000E+00 -2963.670     012002\n" +
                    "  0.0000000E+00 0.0000000E+00 -4596.758     -902.2127     0.0000000E+00 003002\n" +
                    "  0.0000000E+00 0.0000000E+00  2226638.      734098.4     0.0000000E+00 200300\n" +
                    "  0.0000000E+00 0.0000000E+00  2961706.      880173.6     0.0000000E+00 110300\n" +
                    "  0.0000000E+00 0.0000000E+00  992051.8      236014.2     0.0000000E+00 020300\n" +
                    "  -679487.5     -226667.6     0.0000000E+00 0.0000000E+00 -107056.5     101300\n" +
                    "  -101046.0     -46231.38     0.0000000E+00 0.0000000E+00 -50329.15     011300\n" +
                    "  0.0000000E+00 0.0000000E+00 -60105.42      6030.777     0.0000000E+00 002300\n" +
                    "   1027234.      318949.1     0.0000000E+00 0.0000000E+00  174840.9     200201\n" +
                    "   1257791.      390726.2     0.0000000E+00 0.0000000E+00  242227.2     110201\n" +
                    "   329352.6      100769.0     0.0000000E+00 0.0000000E+00  79239.03     020201\n" +
                    "  0.0000000E+00 0.0000000E+00 -41154.16     -27591.74     0.0000000E+00 101201\n" +
                    "  0.0000000E+00 0.0000000E+00 -66781.31     -9958.509     0.0000000E+00 011201\n" +
                    "   85599.35      30609.56     0.0000000E+00 0.0000000E+00  8698.592     002201\n" +
                    "  0.0000000E+00 0.0000000E+00  115797.3      27522.89     0.0000000E+00 200102\n" +
                    "  0.0000000E+00 0.0000000E+00  281608.3      81260.09     0.0000000E+00 110102\n" +
                    "  0.0000000E+00 0.0000000E+00  129321.5      37616.63     0.0000000E+00 020102\n" +
                    "  -252191.8     -76306.36     0.0000000E+00 0.0000000E+00 -23493.35     101102\n" +
                    "  -160950.9     -50344.47     0.0000000E+00 0.0000000E+00 -14575.08     011102\n" +
                    "  0.0000000E+00 0.0000000E+00 -15079.24     -1951.138     0.0000000E+00 002102\n" +
                    "  -271.6562      135.3563     0.0000000E+00 0.0000000E+00  177.3123     200003\n" +
                    "  -147.6483      287.8406     0.0000000E+00 0.0000000E+00  526.4685     110003\n" +
                    "   93.47141      92.74794     0.0000000E+00 0.0000000E+00  320.3097     020003\n" +
                    "  0.0000000E+00 0.0000000E+00  433.5029     -3723.855     0.0000000E+00 101003\n" +
                    "  0.0000000E+00 0.0000000E+00 -629.0683     -3622.327     0.0000000E+00 011003\n" +
                    "   5057.862      1141.385     0.0000000E+00 0.0000000E+00  39.23907     002003\n" +
                    "  -23000.32      2476.385     0.0000000E+00 0.0000000E+00 -60173.74     100400\n" +
                    "   232654.2      80161.16     0.0000000E+00 0.0000000E+00 -25823.87     010400\n" +
                    "  0.0000000E+00 0.0000000E+00 -20059.57     -7409.913     0.0000000E+00 001400\n" +
                    "  0.0000000E+00 0.0000000E+00 -242119.7     -60047.72     0.0000000E+00 100301\n" +
                    "  0.0000000E+00 0.0000000E+00 -197244.0     -13420.62     0.0000000E+00 010301\n" +
                    "   111620.0      45018.43     0.0000000E+00 0.0000000E+00  7267.380     001301\n" +
                    "  -113224.0     -37016.10     0.0000000E+00 0.0000000E+00 -16961.30     100202\n" +
                    "  -38269.56     -14811.13     0.0000000E+00 0.0000000E+00 -8631.231     010202\n" +
                    "  0.0000000E+00 0.0000000E+00 -22755.01      2638.531     0.0000000E+00 001202\n" +
                    "  0.0000000E+00 0.0000000E+00 -6038.384     -4659.234     0.0000000E+00 100103\n" +
                    "  0.0000000E+00 0.0000000E+00 -6331.758     -2715.796     0.0000000E+00 010103\n" +
                    "   18892.34      5331.554     0.0000000E+00 0.0000000E+00 -207.9676     001103\n" +
                    "  -12.43512     -21.81031     0.0000000E+00 0.0000000E+00 -29.04215     100004\n" +
                    "   40.36019     0.2091570     0.0000000E+00 0.0000000E+00 -31.28460     010004\n" +
                    "  0.0000000E+00 0.0000000E+00 -305.2424      134.1885     0.0000000E+00 001004\n" +
                    "  0.0000000E+00 0.0000000E+00  34906.96     -23615.26     0.0000000E+00 000500\n" +
                    "  -73825.82     -15199.37     0.0000000E+00 0.0000000E+00 -331.5161     000401\n" +
                    "  0.0000000E+00 0.0000000E+00 -8256.264     -2660.383     0.0000000E+00 000302\n" +
                    "   5420.208      2129.856     0.0000000E+00 0.0000000E+00 -38.50538     000203\n" +
                    "  0.0000000E+00 0.0000000E+00 -516.3754     -167.8993     0.0000000E+00 000104\n" +
                    "  -5.720635     -1.992706     0.0000000E+00 0.0000000E+00  2.890770     000005"
    );

    CosyArbitraryOrder.CosyMapArbitraryOrder mapQS31437788 = CosyArbitraryOrder.readMap(//QS3_S:=-.1437788
            "   1.200486      2.208411     0.0000000E+00 0.0000000E+00-0.1718336E-01 100000\n" +
                    "  0.8538834E-01 0.9900764     0.0000000E+00 0.0000000E+00-0.7131852E-03 010000\n" +
                    "  0.0000000E+00 0.0000000E+00 0.9600702    -0.6001315     0.0000000E+00 001000\n" +
                    "  0.0000000E+00 0.0000000E+00 0.6126101     0.6586533     0.0000000E+00 000100\n" +
                    "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00  1.000000     000010\n" +
                    "  0.6110899E-03 0.1543783E-01 0.0000000E+00 0.0000000E+00  2.177852     000001\n" +
                    "  -32.61375      11.21730     0.0000000E+00 0.0000000E+00 -9.881091     200000\n" +
                    "  -18.91656      20.59195     0.0000000E+00 0.0000000E+00 -11.13338     110000\n" +
                    "  0.3085996      9.100557     0.0000000E+00 0.0000000E+00 -3.947787     020000\n" +
                    "  0.0000000E+00 0.0000000E+00  99.93870     -48.54674     0.0000000E+00 101000\n" +
                    "  0.0000000E+00 0.0000000E+00  52.48328     -22.76422     0.0000000E+00 011000\n" +
                    "  -5.216574     -4.028616     0.0000000E+00 0.0000000E+00 -5.547853     002000\n" +
                    "  0.0000000E+00 0.0000000E+00 -5.163745     -96.31197     0.0000000E+00 100100\n" +
                    "  0.0000000E+00 0.0000000E+00  3.312126     -52.60198     0.0000000E+00 010100\n" +
                    "   50.08006      12.52175     0.0000000E+00 0.0000000E+00 -4.985759     001100\n" +
                    "  -11.95653     -4.683159     0.0000000E+00 0.0000000E+00 0.3370714E-01 100001\n" +
                    "  -8.503767     -6.115709     0.0000000E+00 0.0000000E+00 0.1231384     010001\n" +
                    "  0.0000000E+00 0.0000000E+00  2.841227      9.943791     0.0000000E+00 001001\n" +
                    "   18.27816      10.46694     0.0000000E+00 0.0000000E+00 -7.156458     000200\n" +
                    "  0.0000000E+00 0.0000000E+00 -10.62648      11.03833     0.0000000E+00 000101\n" +
                    "  0.3675825E-02 0.6840955E-01 0.0000000E+00 0.0000000E+00 -2.035555     000002\n" +
                    "   723.0906      471.1818     0.0000000E+00 0.0000000E+00 -73.34618     300000\n" +
                    "   953.6773      831.1991     0.0000000E+00 0.0000000E+00 -161.8188     210000\n" +
                    "   421.3857      548.0149     0.0000000E+00 0.0000000E+00 -122.3148     120000\n" +
                    "   72.91219      130.4089     0.0000000E+00 0.0000000E+00 -29.99983     030000\n" +
                    "  0.0000000E+00 0.0000000E+00  7387.363      3070.730     0.0000000E+00 201000\n" +
                    "  0.0000000E+00 0.0000000E+00  7848.218      3070.345     0.0000000E+00 111000\n" +
                    "  0.0000000E+00 0.0000000E+00  2154.709      720.0946     0.0000000E+00 021000\n" +
                    "  -3941.966     -1419.246     0.0000000E+00 0.0000000E+00 -307.9244     102000\n" +
                    "  -1971.696     -704.3138     0.0000000E+00 0.0000000E+00 -145.7666     012000\n" +
                    "  0.0000000E+00 0.0000000E+00 -149.7628     -72.56371     0.0000000E+00 003000\n" +
                    "  0.0000000E+00 0.0000000E+00  7037.086      2779.222     0.0000000E+00 200100\n" +
                    "  0.0000000E+00 0.0000000E+00  8119.547      2195.018     0.0000000E+00 110100\n" +
                    "  0.0000000E+00 0.0000000E+00  2355.801      305.6721     0.0000000E+00 020100\n" +
                    "  -3284.773     -1171.996     0.0000000E+00 0.0000000E+00 -348.0881     101100\n" +
                    "  -1697.709     -560.8352     0.0000000E+00 0.0000000E+00 -241.5127     011100\n" +
                    "  0.0000000E+00 0.0000000E+00 -79.78869     -65.01223     0.0000000E+00 002100\n" +
                    "   191.7378      30.93619     0.0000000E+00 0.0000000E+00  52.66783     200001\n" +
                    "   274.2876     -19.60936     0.0000000E+00 0.0000000E+00  84.98434     110001\n" +
                    "   62.08785     -31.26848     0.0000000E+00 0.0000000E+00  31.96569     020001\n" +
                    "  0.0000000E+00 0.0000000E+00 -463.6288     -73.08022     0.0000000E+00 101001\n" +
                    "  0.0000000E+00 0.0000000E+00 -414.4960      19.21887     0.0000000E+00 011001\n" +
                    "   427.9243      150.9749     0.0000000E+00 0.0000000E+00  49.76301     002001\n" +
                    "   1201.803      678.4336     0.0000000E+00 0.0000000E+00  21.88760     100200\n" +
                    "   1099.228      587.2457     0.0000000E+00 0.0000000E+00 -28.27417     010200\n" +
                    "  0.0000000E+00 0.0000000E+00  352.9332     -247.0118     0.0000000E+00 001200\n" +
                    "  0.0000000E+00 0.0000000E+00 -948.4615      484.1567     0.0000000E+00 100101\n" +
                    "  0.0000000E+00 0.0000000E+00 -488.1963      440.3115     0.0000000E+00 010101\n" +
                    "   267.9074      150.0719     0.0000000E+00 0.0000000E+00  80.19087     001101\n" +
                    "   27.56844      8.178484     0.0000000E+00 0.0000000E+00 -2.015146     100002\n" +
                    "   34.40402      13.39752     0.0000000E+00 0.0000000E+00 -1.649441     010002\n" +
                    "  0.0000000E+00 0.0000000E+00 -56.15516     -22.80290     0.0000000E+00 001002\n" +
                    "  0.0000000E+00 0.0000000E+00  159.2819     -437.8754     0.0000000E+00 000300\n" +
                    "  -552.3111     -149.3558     0.0000000E+00 0.0000000E+00  13.20400     000201\n" +
                    "  0.0000000E+00 0.0000000E+00 -62.93972     -79.41117     0.0000000E+00 000102\n" +
                    " -0.8074050    -0.5978820     0.0000000E+00 0.0000000E+00  1.915331     000003\n" +
                    "  -1372.605      2113.405     0.0000000E+00 0.0000000E+00 -2725.358     400000\n" +
                    "  -319.1746      8043.731     0.0000000E+00 0.0000000E+00 -6512.853     310000\n" +
                    "   1381.077      10635.21     0.0000000E+00 0.0000000E+00 -6326.880     220000\n" +
                    "   929.7129      5836.757     0.0000000E+00 0.0000000E+00 -2863.769     130000\n" +
                    "   139.3958      1120.941     0.0000000E+00 0.0000000E+00 -494.9112     040000\n" +
                    "  0.0000000E+00 0.0000000E+00  55142.78      7250.738     0.0000000E+00 301000\n" +
                    "  0.0000000E+00 0.0000000E+00  119336.5      19604.65     0.0000000E+00 211000\n" +
                    "  0.0000000E+00 0.0000000E+00  91953.24      15976.70     0.0000000E+00 121000\n" +
                    "  0.0000000E+00 0.0000000E+00  23312.25      4018.403     0.0000000E+00 031000\n" +
                    "  -121012.2     -45023.28     0.0000000E+00 0.0000000E+00 -4407.154     202000\n" +
                    "  -125190.9     -46553.49     0.0000000E+00 0.0000000E+00 -5198.489     112000\n" +
                    "  -35038.60     -12984.87     0.0000000E+00 0.0000000E+00 -1908.306     022000\n" +
                    "  0.0000000E+00 0.0000000E+00 -22476.48     -10919.56     0.0000000E+00 103000\n" +
                    "  0.0000000E+00 0.0000000E+00 -10827.27     -5381.347     0.0000000E+00 013000\n" +
                    "  -378.9206     -147.1320     0.0000000E+00 0.0000000E+00 -212.9371     004000\n" +
                    "  0.0000000E+00 0.0000000E+00  64273.93      6046.106     0.0000000E+00 300100\n" +
                    "  0.0000000E+00 0.0000000E+00  198196.4      35046.21     0.0000000E+00 210100\n" +
                    "  0.0000000E+00 0.0000000E+00  168647.0      31109.17     0.0000000E+00 120100\n" +
                    "  0.0000000E+00 0.0000000E+00  43555.96      7316.188     0.0000000E+00 030100\n" +
                    "  -320199.4     -114442.9     0.0000000E+00 0.0000000E+00 -27611.90     201100\n" +
                    "  -384098.1     -135394.9     0.0000000E+00 0.0000000E+00 -35278.57     111100\n" +
                    "  -113926.6     -39724.29     0.0000000E+00 0.0000000E+00 -11403.33     021100\n" +
                    "  0.0000000E+00 0.0000000E+00 -28298.31     -20459.60     0.0000000E+00 102100\n" +
                    "  0.0000000E+00 0.0000000E+00 -13705.17     -13265.07     0.0000000E+00 012100\n" +
                    "   1548.775      158.4440     0.0000000E+00 0.0000000E+00 -358.3978     003100\n" +
                    "  -5801.211     -1868.159     0.0000000E+00 0.0000000E+00  436.7683     300001\n" +
                    "  -11533.10     -5157.670     0.0000000E+00 0.0000000E+00  1373.324     210001\n" +
                    "  -6848.837     -4099.309     0.0000000E+00 0.0000000E+00  1258.710     120001\n" +
                    "  -1236.918     -1061.854     0.0000000E+00 0.0000000E+00  362.6181     030001\n" +
                    "  0.0000000E+00 0.0000000E+00 -29913.03     -9041.528     0.0000000E+00 201001\n" +
                    "  0.0000000E+00 0.0000000E+00 -54372.68     -18756.68     0.0000000E+00 111001\n" +
                    "  0.0000000E+00 0.0000000E+00 -20349.87     -7106.061     0.0000000E+00 021001\n" +
                    "   38472.76      13127.52     0.0000000E+00 0.0000000E+00  3011.526     102001\n" +
                    "   24810.39      8669.173     0.0000000E+00 0.0000000E+00  1848.163     012001\n" +
                    "  0.0000000E+00 0.0000000E+00  2955.576      1122.242     0.0000000E+00 003001\n" +
                    "  -191054.9     -61496.05     0.0000000E+00 0.0000000E+00 -26250.78     200200\n" +
                    "  -207184.9     -63544.99     0.0000000E+00 0.0000000E+00 -30411.63     110200\n" +
                    "  -52463.04     -14986.05     0.0000000E+00 0.0000000E+00 -8875.721     020200\n" +
                    "  0.0000000E+00 0.0000000E+00  47414.02      6131.800     0.0000000E+00 101200\n" +
                    "  0.0000000E+00 0.0000000E+00  28584.93     -3060.740     0.0000000E+00 011200\n" +
                    "  -12551.59     -5749.341     0.0000000E+00 0.0000000E+00 -1732.842     002200\n" +
                    "  0.0000000E+00 0.0000000E+00 -92962.48     -29956.24     0.0000000E+00 200101\n" +
                    "  0.0000000E+00 0.0000000E+00 -122243.3     -37335.12     0.0000000E+00 110101\n" +
                    "  0.0000000E+00 0.0000000E+00 -39977.02     -9970.988     0.0000000E+00 020101\n" +
                    "   129977.7      46539.31     0.0000000E+00 0.0000000E+00  11682.76     101101\n" +
                    "   75891.54      27417.28     0.0000000E+00 0.0000000E+00  6881.326     011101\n" +
                    "  0.0000000E+00 0.0000000E+00  7681.496      2175.423     0.0000000E+00 002101\n" +
                    "  -374.9837     -64.86963     0.0000000E+00 0.0000000E+00 -168.6280     200002\n" +
                    "  -930.4775     -83.31183     0.0000000E+00 0.0000000E+00 -357.1938     110002\n" +
                    "  -412.4606      30.18558     0.0000000E+00 0.0000000E+00 -167.2755     020002\n" +
                    "  0.0000000E+00 0.0000000E+00  453.7032      1798.126     0.0000000E+00 101002\n" +
                    "  0.0000000E+00 0.0000000E+00  1111.068      1086.169     0.0000000E+00 011002\n" +
                    "  -3024.759     -922.3692     0.0000000E+00 0.0000000E+00 -226.1296     002002\n" +
                    "  0.0000000E+00 0.0000000E+00  57011.32      451.3772     0.0000000E+00 100300\n" +
                    "  0.0000000E+00 0.0000000E+00  32375.97     -6746.796     0.0000000E+00 010300\n" +
                    "  -11462.77     -5836.585     0.0000000E+00 0.0000000E+00 -2199.860     001300\n" +
                    "   49203.55      18156.63     0.0000000E+00 0.0000000E+00  5609.201     100201\n" +
                    "   24158.77      8869.095     0.0000000E+00 0.0000000E+00  3381.635     010201\n" +
                    "  0.0000000E+00 0.0000000E+00  4015.719     -938.4705     0.0000000E+00 001201\n" +
                    "  0.0000000E+00 0.0000000E+00  2307.355      215.0613     0.0000000E+00 100102\n" +
                    "  0.0000000E+00 0.0000000E+00  2582.815     -812.1666     0.0000000E+00 010102\n" +
                    "  -10182.50     -3493.859     0.0000000E+00 0.0000000E+00 -834.2492     001102\n" +
                    "  -18.47225     -7.857342     0.0000000E+00 0.0000000E+00  12.26688     100003\n" +
                    "  -66.31523     -25.58317     0.0000000E+00 0.0000000E+00  10.76484     010003\n" +
                    "  0.0000000E+00 0.0000000E+00  86.58529     -76.05170     0.0000000E+00 001003\n" +
                    "   12411.03      3997.880     0.0000000E+00 0.0000000E+00  52.47257     000400\n" +
                    "  0.0000000E+00 0.0000000E+00 -530.7388      2067.236     0.0000000E+00 000301\n" +
                    "  -3177.540     -1300.665     0.0000000E+00 0.0000000E+00 -468.4990     000202\n" +
                    "  0.0000000E+00 0.0000000E+00  328.7321      167.7348     0.0000000E+00 000103\n" +
                    "   3.892393      1.787520     0.0000000E+00 0.0000000E+00 -2.017648     000004\n" +
                    "   493258.5      173944.5     0.0000000E+00 0.0000000E+00 -37041.43     500000\n" +
                    "   1344194.      513629.3     0.0000000E+00 0.0000000E+00 -128840.9     410000\n" +
                    "   1496723.      641341.4     0.0000000E+00 0.0000000E+00 -182034.2     320000\n" +
                    "   846008.8      417643.4     0.0000000E+00 0.0000000E+00 -129060.4     230000\n" +
                    "   241150.5      139671.2     0.0000000E+00 0.0000000E+00 -45654.37     140000\n" +
                    "   27356.04      18863.87     0.0000000E+00 0.0000000E+00 -6432.607     050000\n" +
                    "  0.0000000E+00 0.0000000E+00  1621078.      567908.6     0.0000000E+00 401000\n" +
                    "  0.0000000E+00 0.0000000E+00  4411431.      1488365.     0.0000000E+00 311000\n" +
                    "  0.0000000E+00 0.0000000E+00  4846845.      1583457.     0.0000000E+00 221000\n" +
                    "  0.0000000E+00 0.0000000E+00  2399890.      765287.1     0.0000000E+00 131000\n" +
                    "  0.0000000E+00 0.0000000E+00  441833.5      138479.8     0.0000000E+00 041000\n" +
                    "   955726.1      280886.0     0.0000000E+00 0.0000000E+00  392094.5     302000\n" +
                    "   650229.8      138094.8     0.0000000E+00 0.0000000E+00  541931.2     212000\n" +
                    "  -473159.0     -217841.2     0.0000000E+00 0.0000000E+00  219682.6     122000\n" +
                    "  -281715.7     -109035.8     0.0000000E+00 0.0000000E+00  23336.84     032000\n" +
                    "  0.0000000E+00 0.0000000E+00 -1117722.     -507449.7     0.0000000E+00 203000\n" +
                    "  0.0000000E+00 0.0000000E+00 -1109143.     -501681.6     0.0000000E+00 113000\n" +
                    "  0.0000000E+00 0.0000000E+00 -294740.9     -136404.9     0.0000000E+00 023000\n" +
                    "  -198719.9     -69219.99     0.0000000E+00 0.0000000E+00 -35851.34     104000\n" +
                    "  -107386.9     -38066.28     0.0000000E+00 0.0000000E+00 -16866.61     014000\n" +
                    "  0.0000000E+00 0.0000000E+00 -6003.641     -2042.957     0.0000000E+00 005000\n" +
                    "  0.0000000E+00 0.0000000E+00  2044778.      708120.6     0.0000000E+00 400100\n" +
                    "  0.0000000E+00 0.0000000E+00  6494325.      2117370.     0.0000000E+00 310100\n" +
                    "  0.0000000E+00 0.0000000E+00  7779226.      2432496.     0.0000000E+00 220100\n" +
                    "  0.0000000E+00 0.0000000E+00  4086302.      1233250.     0.0000000E+00 130100\n" +
                    "  0.0000000E+00 0.0000000E+00  790042.8      230753.2     0.0000000E+00 040100\n" +
                    "   2646295.      799144.2     0.0000000E+00 0.0000000E+00  1017245.     301100\n" +
                    "  -297877.8     -308597.5     0.0000000E+00 0.0000000E+00  1224442.     211100\n" +
                    "  -3274104.     -1253106.     0.0000000E+00 0.0000000E+00  366857.3     121100\n" +
                    "  -1273222.     -463274.2     0.0000000E+00 0.0000000E+00 -20.22333     031100\n" +
                    "  0.0000000E+00 0.0000000E+00 -1643618.     -730597.1     0.0000000E+00 202100\n" +
                    "  0.0000000E+00 0.0000000E+00 -1598717.     -751165.8     0.0000000E+00 112100\n" +
                    "  0.0000000E+00 0.0000000E+00 -405667.0     -220147.8     0.0000000E+00 022100\n" +
                    "  -692752.9     -242027.6     0.0000000E+00 0.0000000E+00 -39768.92     103100\n" +
                    "  -523348.7     -187409.5     0.0000000E+00 0.0000000E+00 -29482.15     013100\n" +
                    "  0.0000000E+00 0.0000000E+00 -32420.09     -197.9231     0.0000000E+00 004100\n" +
                    "   14717.48      4411.555     0.0000000E+00 0.0000000E+00  21207.15     400001\n" +
                    "   67.18567     -20557.34     0.0000000E+00 0.0000000E+00  66151.57     310001\n" +
                    "  -35121.91     -55921.79     0.0000000E+00 0.0000000E+00  75224.02     220001\n" +
                    "  -27495.95     -43138.90     0.0000000E+00 0.0000000E+00  38261.34     130001\n" +
                    "  -5621.592     -10581.11     0.0000000E+00 0.0000000E+00  7361.052     040001\n" +
                    "  0.0000000E+00 0.0000000E+00 -263131.9     -42797.35     0.0000000E+00 301001\n" +
                    "  0.0000000E+00 0.0000000E+00 -826203.5     -156151.5     0.0000000E+00 211001\n" +
                    "  0.0000000E+00 0.0000000E+00 -759411.7     -155275.6     0.0000000E+00 121001\n" +
                    "  0.0000000E+00 0.0000000E+00 -226503.1     -47774.89     0.0000000E+00 031001\n" +
                    "   238323.5      84196.87     0.0000000E+00 0.0000000E+00 -74763.51     202001\n" +
                    "   654650.9      241620.7     0.0000000E+00 0.0000000E+00 -60040.35     112001\n" +
                    "   307146.3      113355.1     0.0000000E+00 0.0000000E+00 -7243.114     022001\n" +
                    "  0.0000000E+00 0.0000000E+00  363780.3      134998.9     0.0000000E+00 103001\n" +
                    "  0.0000000E+00 0.0000000E+00  214118.8      77245.93     0.0000000E+00 013001\n" +
                    "   19719.74      5890.967     0.0000000E+00 0.0000000E+00  3509.637     004001\n" +
                    "   1970004.      687484.8     0.0000000E+00 0.0000000E+00  569754.7     300200\n" +
                    "  -622729.3     -155254.3     0.0000000E+00 0.0000000E+00  455650.2     210200\n" +
                    "  -2805611.     -895258.5     0.0000000E+00 0.0000000E+00 -66127.40     120200\n" +
                    "  -1027854.     -330490.3     0.0000000E+00 0.0000000E+00 -80456.99     030200\n" +
                    "  0.0000000E+00 0.0000000E+00  1669870.      514323.2     0.0000000E+00 201200\n" +
                    "  0.0000000E+00 0.0000000E+00  2773191.      754780.0     0.0000000E+00 111200\n" +
                    "  0.0000000E+00 0.0000000E+00  1040540.      211506.9     0.0000000E+00 021200\n" +
                    "  -1847841.     -691361.4     0.0000000E+00 0.0000000E+00 -54968.06     102200\n" +
                    "  -1251300.     -472565.6     0.0000000E+00 0.0000000E+00 -52969.90     012200\n" +
                    "  0.0000000E+00 0.0000000E+00 -170995.9     -8258.788     0.0000000E+00 003200\n" +
                    "  0.0000000E+00 0.0000000E+00 -1159893.     -273874.2     0.0000000E+00 300101\n" +
                    "  0.0000000E+00 0.0000000E+00 -2840371.     -669336.7     0.0000000E+00 210101\n" +
                    "  0.0000000E+00 0.0000000E+00 -2422765.     -571304.8     0.0000000E+00 120101\n" +
                    "  0.0000000E+00 0.0000000E+00 -677907.5     -156124.3     0.0000000E+00 030101\n" +
                    "   3189677.      1151121.     0.0000000E+00 0.0000000E+00  54686.81     201101\n" +
                    "   5050045.      1816869.     0.0000000E+00 0.0000000E+00  218303.8     111101\n" +
                    "   1944161.      695459.9     0.0000000E+00 0.0000000E+00  118614.7     021101\n" +
                    "  0.0000000E+00 0.0000000E+00  1276836.      522566.3     0.0000000E+00 102101\n" +
                    "  0.0000000E+00 0.0000000E+00  731295.3      290571.4     0.0000000E+00 012101\n" +
                    "   12163.99      950.9316     0.0000000E+00 0.0000000E+00  1344.589     003101\n" +
                    "   19940.19      4441.696     0.0000000E+00 0.0000000E+00 -1732.789     300002\n" +
                    "   56165.27      16698.63     0.0000000E+00 0.0000000E+00 -6491.666     210002\n" +
                    "   45117.08      17770.40     0.0000000E+00 0.0000000E+00 -7102.882     120002\n" +
                    "   10693.19      5532.831     0.0000000E+00 0.0000000E+00 -2356.719     030002\n" +
                    "  0.0000000E+00 0.0000000E+00 -83458.34     -42466.17     0.0000000E+00 201002\n" +
                    "  0.0000000E+00 0.0000000E+00  15569.10     -9487.514     0.0000000E+00 111002\n" +
                    "  0.0000000E+00 0.0000000E+00  47953.98      14231.24     0.0000000E+00 021002\n" +
                    "   4838.684      7906.500     0.0000000E+00 0.0000000E+00  2896.691     102002\n" +
                    "  -51222.74     -14519.62     0.0000000E+00 0.0000000E+00 -2332.371     012002\n" +
                    "  0.0000000E+00 0.0000000E+00 -18414.34     -6189.239     0.0000000E+00 003002\n" +
                    "  0.0000000E+00 0.0000000E+00  3564311.      1000291.     0.0000000E+00 200300\n" +
                    "  0.0000000E+00 0.0000000E+00  5010092.      1299374.     0.0000000E+00 110300\n" +
                    "  0.0000000E+00 0.0000000E+00  1721607.      366301.9     0.0000000E+00 020300\n" +
                    "  -4008087.     -1473676.     0.0000000E+00 0.0000000E+00 -342427.7     101300\n" +
                    "  -2363913.     -870959.8     0.0000000E+00 0.0000000E+00 -216814.2     011300\n" +
                    "  0.0000000E+00 0.0000000E+00 -258002.4     -7106.511     0.0000000E+00 002300\n" +
                    "   3252460.      1149883.     0.0000000E+00 0.0000000E+00  283611.2     200201\n" +
                    "   4923384.      1723752.     0.0000000E+00 0.0000000E+00  483073.5     110201\n" +
                    "   1759030.      609703.8     0.0000000E+00 0.0000000E+00  187152.0     020201\n" +
                    "  0.0000000E+00 0.0000000E+00  759212.5      244471.6     0.0000000E+00 101201\n" +
                    "  0.0000000E+00 0.0000000E+00  348835.4      131565.6     0.0000000E+00 011201\n" +
                    "   101284.8      29721.99     0.0000000E+00 0.0000000E+00  1365.224     002201\n" +
                    "  0.0000000E+00 0.0000000E+00  247648.9      53092.60     0.0000000E+00 200102\n" +
                    "  0.0000000E+00 0.0000000E+00  571595.4      150488.6     0.0000000E+00 110102\n" +
                    "  0.0000000E+00 0.0000000E+00  252714.0      67335.25     0.0000000E+00 020102\n" +
                    "  -744128.3     -243814.6     0.0000000E+00 0.0000000E+00 -64972.46     101102\n" +
                    "  -609083.8     -205742.2     0.0000000E+00 0.0000000E+00 -53070.27     011102\n" +
                    "  0.0000000E+00 0.0000000E+00 -83217.97     -32175.70     0.0000000E+00 002102\n" +
                    "  -476.0956     -275.1319     0.0000000E+00 0.0000000E+00  465.1153     200003\n" +
                    "   810.5669     -240.4128     0.0000000E+00 0.0000000E+00  1165.817     110003\n" +
                    "   927.7655     -104.2145     0.0000000E+00 0.0000000E+00  644.1502     020003\n" +
                    "  0.0000000E+00 0.0000000E+00 -1209.875     -7518.032     0.0000000E+00 101003\n" +
                    "  0.0000000E+00 0.0000000E+00 -2043.576     -6800.212     0.0000000E+00 011003\n" +
                    "   1028.635     -413.1115     0.0000000E+00 0.0000000E+00  109.8451     002003\n" +
                    "  -1723564.     -597286.0     0.0000000E+00 0.0000000E+00 -231609.5     100400\n" +
                    "  -857307.4     -291930.2     0.0000000E+00 0.0000000E+00 -133479.7     010400\n" +
                    "  0.0000000E+00 0.0000000E+00 -16129.08      69696.22     0.0000000E+00 001400\n" +
                    "  0.0000000E+00 0.0000000E+00 -472796.2     -144961.9     0.0000000E+00 100301\n" +
                    "  0.0000000E+00 0.0000000E+00 -365302.9     -50158.26     0.0000000E+00 010301\n" +
                    "   653605.6      231859.4     0.0000000E+00 0.0000000E+00  51151.90     001301\n" +
                    "  -807029.3     -278404.5     0.0000000E+00 0.0000000E+00 -80232.16     100202\n" +
                    "  -539277.2     -188131.6     0.0000000E+00 0.0000000E+00 -54861.60     010202\n" +
                    "  0.0000000E+00 0.0000000E+00 -73277.23     -8027.847     0.0000000E+00 001202\n" +
                    "  0.0000000E+00 0.0000000E+00 -9582.926     -7533.848     0.0000000E+00 100103\n" +
                    "  0.0000000E+00 0.0000000E+00 -9143.189     -3541.260     0.0000000E+00 010103\n" +
                    "   48218.49      14515.28     0.0000000E+00 0.0000000E+00  3718.469     001103\n" +
                    "  -64.81958     -10.00634     0.0000000E+00 0.0000000E+00 -47.75554     100004\n" +
                    "   46.74430      30.90531     0.0000000E+00 0.0000000E+00 -48.28315     010004\n" +
                    "  0.0000000E+00 0.0000000E+00  395.4162      656.1891     0.0000000E+00 001004\n" +
                    "  0.0000000E+00 0.0000000E+00  84863.41     -1742.878     0.0000000E+00 000500\n" +
                    "   290504.1      109855.3     0.0000000E+00 0.0000000E+00  30764.47     000401\n" +
                    "  0.0000000E+00 0.0000000E+00 -14467.46      3779.461     0.0000000E+00 000302\n" +
                    "   45598.30      15291.67     0.0000000E+00 0.0000000E+00  3908.250     000203\n" +
                    "  0.0000000E+00 0.0000000E+00 -451.4470      34.38421     0.0000000E+00 000104\n" +
                    "  -10.72283     -4.159829     0.0000000E+00 0.0000000E+00  2.821462     000005"
    );
}
