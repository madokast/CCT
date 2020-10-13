package cn.edu.hust.zrx.cct.advanced;

import cn.edu.hust.zrx.cct.base.BaseUtils;
import cn.edu.hust.zrx.cct.base.cct.MagnetAble;
import cn.edu.hust.zrx.cct.base.line.Line2;
import cn.edu.hust.zrx.cct.base.line.Trajectory;
import cn.edu.hust.zrx.cct.base.particle.*;
import cn.edu.hust.zrx.cct.base.point.Point2;
import cn.edu.hust.zrx.cct.base.point.Point3;
import cn.edu.hust.zrx.cct.base.python.Plot2d;
import cn.edu.hust.zrx.cct.base.vector.Vector2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static cn.edu.hust.zrx.cct.base.BaseUtils.Constant.*;

/**
 * Description
 * CctUtils
 * cct 研究 工具类
 * 这里都是调用我的低级函数
 * <p>
 * Data
 * 22:56
 *
 * @author zrx
 * @version 1.0
 */

public class CctUtils {

    public static Point2 beamSizeAtDp(double distance, double dp, int number,
                                      MagnetAble magnetAble, Line2 trajectory) {
        List<Point2> xPlane = trackingPhaseEllipse(distance, true, dp, number, false,
                1., magnetAble, trajectory);
        List<Point2> yPlane = trackingPhaseEllipse(distance, false, dp, number, false,
                1., magnetAble, trajectory);

        double xMax = xPlane.stream().map(Point2::getX).mapToDouble(Double::doubleValue).max().orElseThrow();
        double xMin = xPlane.stream().map(Point2::getX).mapToDouble(Double::doubleValue).min().orElseThrow();

        double yMax = yPlane.stream().map(Point2::getX).mapToDouble(Double::doubleValue).max().orElseThrow();
        double yMin = yPlane.stream().map(Point2::getX).mapToDouble(Double::doubleValue).min().orElseThrow();

        return Point2.create(
                (xMax - xMin) / 2,
                (yMax - yMin) / 2
        );
    }

    // 画出不同动量分散下的相椭圆
    // 2020年9月17日
    public static void multiDpPhaseEllipsesAndPlot(Trajectory trajectory, double length, MagnetAble magnetAble,
                                                   double dpMin, double dpMax, int numberOfPart, int numberOfParticle, boolean xPlane) {
        BaseUtils.Switcher<String> switcher = createPlotDescribeSwitcher();

        List<String> des = multiDpPhaseEllipses(trajectory, length, magnetAble, dpMin, dpMax, numberOfPart, numberOfParticle, xPlane)
                .stream()
                .peek(bi -> {
                    Plot2d.plot2circle(bi.getT2(), switcher.getAndSwitch());
                })
                .mapToDouble(BaseUtils.Content.BiContent::getT1)
                .mapToInt(d -> (int) Math.round(d / PRESENT))
                .mapToObj(d -> "delta=" + d + "%")
                .collect(Collectors.toList());

        if (xPlane) {
            Plot2d.info("x/mm", "xp/mrad", "x-plane", 18);
        } else {
            Plot2d.info("y/mm", "yp/mrad", "y-plane", 18);
        }

        Plot2d.legend(18, des);

        Plot2d.equal();

        Plot2d.showThread();
    }

    // 单位 mm mrad
    public static List<BaseUtils.Content.BiContent<Double, List<Point2>>>
    multiDpPhaseEllipses(Line2 trajectory, double length, MagnetAble magnetAble,
                         double dpMin, double dpMax, int numberOfPart, int numberOfParticle, boolean xPlane) {

        return BaseUtils.Python.linspaceStream(dpMin, dpMax, numberOfPart)
                .mapToObj(dp -> {
                    List<Point2> x1 = trackingPhaseEllipse(
                            length,
                            xPlane,
                            dp,
                            numberOfParticle,
                            false,
                            1,
                            magnetAble,
                            trajectory
                    );
                    return BaseUtils.Content.BiContent.create(dp, x1);
                })
                .collect(Collectors.toList());
    }

    public static List<BaseUtils.Content.BiContent<Double, List<Point2>>>
    multiDpPhaseEllipses(CosyArbitraryOrder.CosyMapArbitraryOrder map, int order, boolean moveToCenter, double scaleForParticle,
                         double dpMin, double dpMax, int numberOfPart, int numberOfParticle, boolean xPlane) {
        return BaseUtils.Python.linspaceStream(dpMin, dpMax, numberOfPart)
                .mapToObj(delta -> {
                    return BaseUtils.Content.BiContent.create(delta, cosyPhaseEllipse(
                            xPlane, delta, numberOfParticle, order, moveToCenter, scaleForParticle, map));
                }).collect(Collectors.toList());

    }

    public static void analysePhaseEllipseAndPlot(
            double distance, boolean xPlane, double delta, int numberParticleForTrack,
            boolean moveToCenter, double scaleForParticle,
            MagnetAble magnetAble, Line2 trajectory,
            int numberParticleForCosyMap, int cosyOrder,
            List<BaseUtils.Content.BiContent<String, CosyArbitraryOrder.CosyMapArbitraryOrder>> mapInfo
    ) {
        CosyArbitraryOrder.CosyMapArbitraryOrder[] maps = mapInfo.stream().map(BaseUtils.Content.
                BiContent::getT2).collect(Collectors.toList()).toArray(CosyArbitraryOrder.CosyMapArbitraryOrder[]::new);

        List<List<Point2>> lists = analysePhaseEllipse(distance, xPlane, delta, numberParticleForTrack, moveToCenter,
                scaleForParticle, magnetAble, trajectory, numberParticleForCosyMap, cosyOrder, maps);

        List<Point2> list = lists.get(0);
        Plot2d.plot2(list, Plot2d.BLACK_POINT);

        BaseUtils.Switcher<String> switcher = createPlotDescribeSwitcher();

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

    public static void analysePhaseEllipseAndPlot(
            double distance, boolean xPlane, double delta, int numberParticleForTrack,
            boolean moveToCenter, double scaleForParticle,
            List<BaseUtils.Content.BiContent<String, MagnetAble>> magnetAblenfo, Line2 trajectory,
            int numberParticleForCosyMap, int cosyOrder,
            List<BaseUtils.Content.BiContent<String, CosyArbitraryOrder.CosyMapArbitraryOrder>> mapInfo, List<String> describes
    ) {
        List<CosyArbitraryOrder.CosyMapArbitraryOrder> maps = mapInfo.stream().map(BaseUtils.Content.
                BiContent::getT2).collect(Collectors.toList());

        List<MagnetAble> magnetAbles = magnetAblenfo.stream().map(BaseUtils.Content.BiContent::getT2).collect(Collectors.toList());

        List<List<Point2>> lists = analysePhaseEllipse(distance, xPlane, delta, numberParticleForTrack, moveToCenter,
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

    public static List<List<Point2>> analysePhaseEllipse(
            double distance, boolean xPlane, double delta, int numberParticleForTrack,
            boolean moveToCenter, double scaleForParticle,
            MagnetAble magnetAble, Line2 trajectory,
            int numberParticleForCosyMap, int cosyOrder, CosyArbitraryOrder.CosyMapArbitraryOrder... maps
    ) {
        List<List<Point2>> ret = new ArrayList<>(1 + maps.length);

        ret.add(
                trackingPhaseEllipse(distance, xPlane, delta, numberParticleForTrack, moveToCenter, scaleForParticle, magnetAble, trajectory)
        );

        for (CosyArbitraryOrder.CosyMapArbitraryOrder map : maps) {
            ret.add(
                    cosyPhaseEllipse(xPlane, delta, numberParticleForCosyMap, cosyOrder, moveToCenter, scaleForParticle, map)
            );
        }

        return ret;
    }

    public static List<List<Point2>> analysePhaseEllipse(
            double distance, boolean xPlane, double delta, int numberParticleForTrack,
            boolean moveToCenter, double scaleForParticle,
            List<MagnetAble> magnetAbles, Line2 trajectory,
            int numberParticleForCosyMap, int cosyOrder, List<CosyArbitraryOrder.CosyMapArbitraryOrder> maps
    ) {
        List<List<Point2>> ret = new ArrayList<>(magnetAbles.size() + maps.size());


        for (MagnetAble magnetAble : magnetAbles) {
            ret.add(
                    trackingPhaseEllipse(distance, xPlane, delta, numberParticleForTrack, moveToCenter, scaleForParticle, magnetAble, trajectory)
            );
        }


        for (CosyArbitraryOrder.CosyMapArbitraryOrder map : maps) {
            ret.add(
                    cosyPhaseEllipse(xPlane, delta, numberParticleForCosyMap, cosyOrder, moveToCenter, scaleForParticle, map)
            );
        }

        return ret;
    }


    public static List<Point2> cosyPhaseEllipse(
            boolean xPlane, double delta, int number, int order,
            boolean moveToCenter, double scaleForParticle,
            CosyArbitraryOrder.CosyMapArbitraryOrder map
    ) {

        /*----------------COSY-----------------*/

        // 相空间点
        List<PhaseSpaceParticle> pp = PhaseSpaceParticles.phaseSpaceParticlesAlongPositiveEllipseInPlane(
                // 注意 这里写死了
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

    // 单位 mm mrad
    public static List<Point2> trackingPhaseEllipse(
            double distance, boolean xPlane, double delta, int number,
            boolean moveToCenter, double scaleForParticle,
            MagnetAble magnetAble, Line2 trajectory) {

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


    public static List<Point2> trackingIdealParticle(
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

    public static List<Point3> trackingIdealParticle(Line2 trajectory, double distance, MagnetAble magnetAble) {
        RunningParticle ip = ParticleFactory.createIdealProtonAtTrajectory250MeV(trajectory);
        return ParticleRunner.run(ip, magnetAble, distance, MM);
    }

    // 单位 m-m
    public static List<List<Point2>> trackMultiParticlesRandom2d(int number, double delta, Line2 trajectory, double distance,
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

    public static List<List<Point3>> trackMultiParticlesRandom3d(int number, double delta, Line2 trajectory,
                                                                 double distance, MagnetAble magnetAble, boolean xPlane) {
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


        return ps.stream().parallel().map(p -> ParticleRunner.run(p, magnetAble, distance, MM)).collect(Collectors.toList());
    }


    // 画图时 单位转为 m-mm
    public static void trackMultiParticlesRandomAndDrawInMmm(int number, double delta, Line2 trajectory, double distance,
                                                             MagnetAble magnetAble, boolean xPlane) {
        List<List<Point2>> xs = trackMultiParticlesRandom2d(number, delta, trajectory, distance, magnetAble, xPlane);

        xs.stream().map(x -> Point2.convert(x, 1, 1 / MM))
                .forEach(x -> Plot2d.plot2(x, Plot2d.BLUE_LINE));

        Plot2d.info("m", "mm", (xPlane ? "x-plane" : "y-plane") + " dp=" + delta / PRESENT + "%", 18);

        Plot2d.showThread();
    }


    public static List<List<Point2>> trackMultiParticles2d(int number, double delta, Line2 trajectory, double distance,
                                                           MagnetAble magnetAble, boolean xPlane) {
        RunningParticle ip = ParticleFactory.createIdealProtonAtTrajectory250MeV(trajectory);

        List<PhaseSpaceParticle> pp = PhaseSpaceParticles.phaseSpaceParticlesAlongPositiveEllipseInPlane(
                xPlane, 3.5 * MM, 7.5 * MM, delta, number);
        List<RunningParticle> ps = ParticleFactory.createParticlesFromPhaseSpaceParticle(ip, ip.computeNaturalCoordinateSystem(), pp);


//        List<RunningParticle> ps = Arrays.stream(SR.sphericalUniformDistribution(number)).map(point3 -> {
//
//            PhaseSpaceParticle psp = PhaseSpaceParticle.create(
//                    xPlane ? 3.5 * MM * point3.x : 0,
//                    xPlane ? 7.5 * MM * point3.y : 0,
//                    xPlane ? 0 : 3.5 * MM * point3.x,
//                    xPlane ? 0 : 7.5 * MM * point3.y,
//                    0,
//                    delta * point3.z
//            );
//
//            return ParticleFactory.createParticleFromPhaseSpaceParticle(ip, ip.computeNaturalCoordinateSystem(), psp);
//        }).collect(Collectors.toList());


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

    public static List<List<Point3>> trackMultiParticles3d(int number, double delta, Line2 trajectory,
                                                           double distance, MagnetAble magnetAble, boolean xPlane) {
        RunningParticle ip = ParticleFactory.createIdealProtonAtTrajectory250MeV(trajectory);

        List<PhaseSpaceParticle> pp = PhaseSpaceParticles.phaseSpaceParticlesAlongPositiveEllipseInPlane(
                xPlane, 3.5 * MM, 7.5 * MM, delta, number);
        List<RunningParticle> ps = ParticleFactory.createParticlesFromPhaseSpaceParticle(ip, ip.computeNaturalCoordinateSystem(), pp);


//        List<RunningParticle> ps = Arrays.stream(SR.sphericalUniformDistribution(number)).map(point3 -> {
//
//            PhaseSpaceParticle psp = PhaseSpaceParticle.create(
//                    xPlane ? 3.5 * MM * point3.x : 0,
//                    xPlane ? 7.5 * MM * point3.y : 0,
//                    xPlane ? 0 : 3.5 * MM * point3.x,
//                    xPlane ? 0 : 7.5 * MM * point3.y,
//                    0,
//                    delta * point3.z
//            );
//
//            return ParticleFactory.createParticleFromPhaseSpaceParticle(ip, ip.computeNaturalCoordinateSystem(), psp);
//        }).collect(Collectors.toList());


        return ps.stream().parallel().map(p -> ParticleRunner.run(p, magnetAble, distance, MM)).collect(Collectors.toList());
    }


    // 画图时 单位转为 m-mm
    public static void trackMultiParticlesAndDrawInMmm(int number, double delta, Line2 trajectory, double distance,
                                                       MagnetAble magnetAble, boolean xPlane) {
        List<List<Point2>> xs = trackMultiParticles2d(number, delta, trajectory, distance, magnetAble, xPlane);

        xs.stream().map(x -> Point2.convert(x, 1, 1 / MM))
                .forEach(x -> Plot2d.plot2(x, Plot2d.BLUE_LINE));

        Plot2d.info("m", "mm", (xPlane ? "x-plane" : "y-plane") + " dp=" + delta / PRESENT + "%", 18);

        Plot2d.showThread();
    }

    // 单位 mm/%
    public static List<Point2> chromaticDispersion(Line2 trajectory, MagnetAble magnetAble) {
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
    public static List<Point2> chromaticDispersion(List<BaseUtils.Content.BiContent<Double,
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


    public static BaseUtils.Switcher<String> createPlotDescribeSwitcher() {
        return BaseUtils.Switcher.creat(
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
    }


}
