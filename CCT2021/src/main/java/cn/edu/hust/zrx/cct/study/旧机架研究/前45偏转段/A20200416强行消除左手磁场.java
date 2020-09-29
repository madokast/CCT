package cn.edu.hust.zrx.cct.study.旧机架研究.前45偏转段;

import cn.edu.hust.zrx.cct.Logger;
import cn.edu.hust.zrx.cct.advanced.COSY;
import cn.edu.hust.zrx.cct.advanced.CosyArbitraryOrder;
import cn.edu.hust.zrx.cct.base.BaseUtils;
import cn.edu.hust.zrx.cct.base.cct.Cct;
import cn.edu.hust.zrx.cct.base.cct.CctFactory;
import cn.edu.hust.zrx.cct.base.cct.Elements;
import cn.edu.hust.zrx.cct.base.cct.MagnetAble;
import cn.edu.hust.zrx.cct.base.line.Trajectory;
import cn.edu.hust.zrx.cct.base.line.TrajectoryFactory;
import cn.edu.hust.zrx.cct.base.particle.*;
import cn.edu.hust.zrx.cct.base.point.Point2;
import cn.edu.hust.zrx.cct.base.python.Plot2d;
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
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Description
 * A20200416强行消除左手磁场
 * <p>
 * Data
 * 11:31
 *
 * @author zrx
 * @version 1.0
 */
@SuppressWarnings("all")
public class A20200416强行消除左手磁场 {
    double scaleForParticle = 0.01;
    boolean moveToCenter = false;

    @run(1)
    public void 回顾() {
        List<Point2> list = tracking相椭圆(true, getTrajectory().getLength(), 0, 16);

        Plot2d.plot2(list, Plot2d.BLACK_POINT);

        Plot2d.plot2circle(list, Plot2d.BLACK_LINE);

        Plot2d.equal();

        Plot2d.showThread();
    }

    @run(20)
    public void 尝试强行消除左手磁场() {
        //private double dipoleCctIInner = 9.898121356964111 * kA;


        Elements elementsOfAll = getElementsOfAll();

        Trajectory trajectory = getTrajectory();

        RunningParticle rp = ParticleFactory.createIdealProtonAtTrajectory(trajectory, 250 * 0.95);

        List<Point2> collect = runNoLeft(rp, elementsOfAll, trajectory.getLength(), MM, trajectory)
                .stream()
                .map(bi -> {
                    Double d = bi.getT1();
                    RunningParticle runningParticle = bi.getT2();
                    double z = runningParticle.position.z;

                    return Point2.create(d, z);
                })
                .collect(Collectors.toList());

        rp = ParticleFactory.createIdealProtonAtTrajectory(trajectory, 250 * 0.95);
        List<Point2> collect1 = ParticleRunner.runGetPoint3WithDistance(rp, elementsOfAll, trajectory.getLength(), MM)
                .stream()
                .map(ParticleRunner.Point3WithDistance::getDistanceWithZ)
                .collect(Collectors.toList());

        Plot2d.plot2(Point2.convert(collect, 1, 1 / MM), Plot2d.BLACK_LINE);
        Plot2d.plot2(Point2.convert(collect1, 1, 1 / MM), Plot2d.BLUE_LINE);

        Plot2d.plotYLines(keyDistance(), Point2.convert(collect1, 1, 1 / MM));

        Plot2d.info("s/m", "y/mm", "", 18);
        Plot2d.legend(18, "Bx->0", "Default");

        Plot2d.plotYLines(keyDistance(), collect);

        Plot2d.showThread();
    }

    @run(value = 3, code = "20200416COSYHIGH")
    public void cosy相椭圆高阶() {
        scaleForParticle = 0.001;
        moveToCenter = true;

        boolean xPlane = false;
        double delta = -5 * PRESENT;
        int number = 512;

        switcher.getAndSwitch();

        IntStream.rangeClosed(1, 5)
                .forEach(order -> {
                    List<Point2> list = cosy相椭圆End(xPlane, delta, number, order);
                    Plot2d.plot2circle(list, switcher.getAndSwitch());
                });


        List<Point2> tracking相椭圆 = tracking相椭圆(xPlane, getTrajectory().getLength(), delta, 32);

        Plot2d.plot2(tracking相椭圆, Plot2d.BLACK_POINT);

//        Plot2d.plot2circle(tracking相椭圆, Plot2d.BLACK_LINE);

        Plot2d.legend(18, "COSY 1st", "COSY 2nd", "COSY 3rd", "COSY 4th", "COSY 5th", "Tracking");
//        Plot2d.legend(18, "COSY 1st", "COSY 2nd", "COSY 3rd", "COSY 4th", "COSY 5th", "COSY 6th", "COSY 7th");
        if (xPlane)
            Plot2d.info("x/mm", "xp/mrad", "x Plane", 18);
        else
            Plot2d.info("y/mm", "yp/mrad", "y Plane", 18);

        Plot2d.equal();
        Plot2d.showThread();

    }

    @run(4)
    public void track包络() {
        scaleForParticle = 1;


        boolean xPlane = false;
        double delta = 5 * PRESENT;
        int number = 16;

        Elements elementsOfAll = getElementsOfAll();

        Trajectory trajectory = getTrajectory();

        RunningParticle ip = ParticleFactory.createIdealProtonAtTrajectory250MeV(trajectory);
        //RunningParticle ipRun = ParticleFactory.createIdealProtonAtTrajectory(trajectory, 250 * (1 + delta));

        List<PhaseSpaceParticle> phaseSpaceParticlesAlongPositiveEllipseInPlane =
                PhaseSpaceParticles.phaseSpaceParticlesAlongPositiveEllipseInPlane(
                        xPlane, 3.5 * MM * scaleForParticle, 7.5 * MRAD * scaleForParticle, delta, number);

        List<RunningParticle> particles = ParticleFactory.createParticlesFromPhaseSpaceParticle(
                ip, ip.computeNaturalCoordinateSystem(), phaseSpaceParticlesAlongPositiveEllipseInPlane);

        particles.stream().parallel().forEach(p -> {
            p = p.copy();
            List<Point2> collect = ParticleRunner.runGetPoint3WithDistance(p, elementsOfAll, trajectory.getLength(), MM)
                    .stream()
                    .map(ParticleRunner.Point3WithDistance::getDistanceWithZ)
                    .map(point2 -> Point2.convert(point2, 1, 1 / MM))
                    .collect(Collectors.toList());

            Plot2d.plot2(collect, Plot2d.BLUE_LINE);
        });

        particles.stream().parallel().forEach(p -> {
            p = p.copy();
            List<Point2> collect = runNoLeft(p, elementsOfAll, trajectory.getLength(), MM, trajectory)
                    .stream()
                    .map(bi -> Point2.create(bi.getT1(), bi.getT2().position.z / MM))
                    .collect(Collectors.toList());

            Plot2d.plot2(collect, Plot2d.RED_LINE);
        });

        Plot2d.info("s/m", "y/mm", "", 18);

        Plot2d.plotYLines(keyDistance(), 20, -20, Plot2d.GREY_DASH);

        Plot2d.showThread();
    }


    @run(1000)
    public void convertR16InCosyToTransportPronTonOnlyUse(){
        double v = COSY.convertR16InCosyToTransportPronTonOnly(0.397, 250);
        Logger.getLogger().info("v = " + v);
    }


    private List<BaseUtils.Content.BiContent<Double, RunningParticle>> runNoLeft(
            RunningParticle particle, MagnetAble elements,
            double distance, double footStep, Trajectory trajectory) {
        int count = (int) (distance / footStep) + 1;
        footStep = distance / (count - 1);

        List<BaseUtils.Content.BiContent<Double, RunningParticle>> ret = new ArrayList<>(count);//提前开辟空间

        RunningParticle ip = ParticleFactory.createIdealProtonAtTrajectory250MeV(trajectory);

        for (long i = 0; i < count - 1/*让粒子不要多走哪怕一步*/; i++) {
            ret.add(BaseUtils.Content.BiContent.create(particle.distance, particle.copy()));

            // 粒子所在磁场
            Vector3 magnet = elements.magnetAt(particle.position);

            // 参考粒子所在位置的磁场
            Vector3 magnetAtIp = elements.magnetAt(ip.position);

            Vector3 left = ip.velocity.copy().toPoint3().toPoint2().toVector2()
                    .rotateSelf(BaseUtils.Converter.angleToRadian(90))
                    .toVector3()
                    .changeLengthSelf(1);
//                    .changeLengthSelf(1 + 14e-2);


            particle.runSelf(magnet.subtract(left.dot(left.dot(magnetAtIp))), footStep);
            ip.runSelf(magnetAtIp.subtract(left.dot(left.dot(magnetAtIp))), footStep);
        }

        ret.add(BaseUtils.Content.BiContent.create(particle.distance, particle.copy()));

        return ret;

    }


    private List<Point2> tracking相椭圆(boolean xPlane, double distance, double delta, int number) {
        Elements elementsOfAll = getElementsOfAll();

        Trajectory trajectory = getTrajectory();

        RunningParticle ip = ParticleFactory.createIdealProtonAtTrajectory250MeV(trajectory);
        RunningParticle ipRun = ParticleFactory.createIdealProtonAtTrajectory(trajectory, 250 * (1 + delta));

        List<PhaseSpaceParticle> phaseSpaceParticlesAlongPositiveEllipseInPlane =
                PhaseSpaceParticles.phaseSpaceParticlesAlongPositiveEllipseInPlane(
                        xPlane, 3.5 * MM * scaleForParticle, 7.5 * MRAD * scaleForParticle, delta, number);


        List<RunningParticle> particles = ParticleFactory.createParticlesFromPhaseSpaceParticle(
                ip, ip.computeNaturalCoordinateSystem(), phaseSpaceParticlesAlongPositiveEllipseInPlane);

//        ParticleRunner.runThread(particles, elementsOfAll, distance, MM);
        BaseUtils.Async async = new BaseUtils.Async();
        particles.forEach(particle -> async.execute(() -> runNoLeft(particle, elementsOfAll, distance, MM, trajectory)));


        async.execute(() -> runNoLeft(ipRun, elementsOfAll, distance, MM, trajectory));
        async.waitForAllFinish(1, TimeUnit.MINUTES);


        List<PhaseSpaceParticle> ppEnd = PhaseSpaceParticles.phaseSpaceParticlesFromRunningParticles(
                ipRun, ipRun.computeNaturalCoordinateSystem(), particles);

        List<Point2> projectionToPlane = PhaseSpaceParticles.projectionToPlane(xPlane, ppEnd);

        projectionToPlane = Point2.convert(projectionToPlane, 1 / MM, 1 / MRAD);

        if (moveToCenter) {
            Vector2 average = Point2.average(projectionToPlane).toVector2().reverseSelf();

            projectionToPlane.forEach(point2 -> point2.moveSelf(average));
        }

        return projectionToPlane;
    }

    private List<Point2> cosy相椭圆End(boolean xPlane, double delta, int number, int order) {

        Elements elementsOfAll = getElementsOfAll();
        Trajectory trajectory = getTrajectory();


        /*----------------COSY-----------------*/

        // 相空间点
        List<PhaseSpaceParticle> pp = PhaseSpaceParticles.phaseSpaceParticlesAlongPositiveEllipseInPlane(
                xPlane, 3.5 * MM * scaleForParticle, 7.5 * MM * scaleForParticle, delta, number);

        // 转为COSY能量分散
        pp = PhaseSpaceParticles.convertDeltaFromMomentumDispersionToEnergyDispersion(pp, 250);

        // 运行
        List<PhaseSpaceParticle> ppEnd = cosyMapEnd().apply(pp, order);

        // 投影
        List<Point2> projectionToPlaneCOSY = PhaseSpaceParticles.projectionToPlane(xPlane, ppEnd);

        if (moveToCenter) {
            Vector2 average = Point2.average(projectionToPlaneCOSY).toVector2().reverseSelf();

            projectionToPlaneCOSY.forEach(point2 -> point2.moveSelf(average));
        }

        // 改单位
        return Point2.convert(projectionToPlaneCOSY, 1 / MM, 1 / MRAD);
    }


    // ------------- COSY高阶 五阶 --------------
    private CosyArbitraryOrder.CosyMapArbitraryOrder cosyMapEnd() {
        return CosyArbitraryOrder.readMap(
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
                        "  -2.974210    -0.1251966     0.0000000E+00 0.0000000E+00 0.4071897     000005\n" +
                        "   143318.4      157171.5     0.0000000E+00 0.0000000E+00 -32386.93     600000\n" +
                        "   1210353.      1293683.     0.0000000E+00 0.0000000E+00 -264917.2     510000\n" +
                        "   4527245.      4760628.     0.0000000E+00 0.0000000E+00 -916659.4     420000\n" +
                        "   9320446.      9784064.     0.0000000E+00 0.0000000E+00 -1765740.     330000\n" +
                        "  0.1088005E+08 0.1151661E+08 0.0000000E+00 0.0000000E+00 -1979074.     240000\n" +
                        "   6764713.      7232083.     0.0000000E+00 0.0000000E+00 -1180484.     150000\n" +
                        "   1757331.      1884799.     0.0000000E+00 0.0000000E+00 -280405.8     060000\n" +
                        "  0.0000000E+00 0.0000000E+00 -243272.4     -162487.3     0.0000000E+00 501000\n" +
                        "  0.0000000E+00 0.0000000E+00 -2045848.     -1317998.     0.0000000E+00 411000\n" +
                        "  0.0000000E+00 0.0000000E+00 -6693145.     -4237883.     0.0000000E+00 321000\n" +
                        "  0.0000000E+00 0.0000000E+00-0.1079716E+08 -6795520.     0.0000000E+00 231000\n" +
                        "  0.0000000E+00 0.0000000E+00 -8867411.     -5583676.     0.0000000E+00 141000\n" +
                        "  0.0000000E+00 0.0000000E+00 -3060680.     -1934849.     0.0000000E+00 051000\n" +
                        "   28100.62      175391.2     0.0000000E+00 0.0000000E+00 -118711.3     402000\n" +
                        "   69997.77      897737.5     0.0000000E+00 0.0000000E+00 -582589.7     312000\n" +
                        "   273512.5      2059212.     0.0000000E+00 0.0000000E+00 -1159976.     222000\n" +
                        "   499275.6      2388532.     0.0000000E+00 0.0000000E+00 -1206595.     132000\n" +
                        "   189989.9      1062130.     0.0000000E+00 0.0000000E+00 -557739.1     042000\n" +
                        "  0.0000000E+00 0.0000000E+00 -477978.5     -328350.2     0.0000000E+00 303000\n" +
                        "  0.0000000E+00 0.0000000E+00 -2784068.     -1803573.     0.0000000E+00 213000\n" +
                        "  0.0000000E+00 0.0000000E+00 -4999071.     -3153891.     0.0000000E+00 123000\n" +
                        "  0.0000000E+00 0.0000000E+00 -2669126.     -1665392.     0.0000000E+00 033000\n" +
                        "  -355362.5     -61616.06     0.0000000E+00 0.0000000E+00 -117995.3     204000\n" +
                        "  -1381720.     -362157.2     0.0000000E+00 0.0000000E+00 -290673.8     114000\n" +
                        "  -1162019.     -332070.6     0.0000000E+00 0.0000000E+00 -207725.1     024000\n" +
                        "  0.0000000E+00 0.0000000E+00  319117.4      190330.8     0.0000000E+00 105000\n" +
                        "  0.0000000E+00 0.0000000E+00  599341.2      369128.5     0.0000000E+00 015000\n" +
                        "   91949.02      45091.81     0.0000000E+00 0.0000000E+00 -15982.13     006000\n" +
                        "  0.0000000E+00 0.0000000E+00 -451419.4     -299430.1     0.0000000E+00 500100\n" +
                        "  0.0000000E+00 0.0000000E+00 -3830812.     -2462304.     0.0000000E+00 410100\n" +
                        "  0.0000000E+00 0.0000000E+00-0.1263830E+08 -8011419.     0.0000000E+00 320100\n" +
                        "  0.0000000E+00 0.0000000E+00-0.2043436E+08-0.1289458E+08 0.0000000E+00 230100\n" +
                        "  0.0000000E+00 0.0000000E+00-0.1666880E+08-0.1052006E+08 0.0000000E+00 140100\n" +
                        "  0.0000000E+00 0.0000000E+00 -5673142.     -3590415.     0.0000000E+00 050100\n" +
                        "   288278.1      776382.3     0.0000000E+00 0.0000000E+00 -478226.2     401100\n" +
                        "   1503017.      4391413.     0.0000000E+00 0.0000000E+00 -2463219.     311100\n" +
                        "   3811892.     0.1026541E+08 0.0000000E+00 0.0000000E+00 -5034235.     221100\n" +
                        "   4446898.     0.1140921E+08 0.0000000E+00 0.0000000E+00 -5084214.     131100\n" +
                        "   1527902.      4786778.     0.0000000E+00 0.0000000E+00 -2182275.     041100\n" +
                        "  0.0000000E+00 0.0000000E+00 -2993787.     -1979428.     0.0000000E+00 302100\n" +
                        "  0.0000000E+00 0.0000000E+00-0.1706102E+08-0.1086985E+08 0.0000000E+00 212100\n" +
                        "  0.0000000E+00 0.0000000E+00-0.3062083E+08-0.1917867E+08 0.0000000E+00 122100\n" +
                        "  0.0000000E+00 0.0000000E+00-0.1674112E+08-0.1040464E+08 0.0000000E+00 032100\n" +
                        "  -2618055.     -393016.0     0.0000000E+00 0.0000000E+00 -862252.2     203100\n" +
                        " -0.1022919E+08 -2339611.     0.0000000E+00 0.0000000E+00 -2303100.     113100\n" +
                        "  -8798999.     -2172298.     0.0000000E+00 0.0000000E+00 -1814216.     023100\n" +
                        "  0.0000000E+00 0.0000000E+00  3005178.      1858190.     0.0000000E+00 104100\n" +
                        "  0.0000000E+00 0.0000000E+00  5667515.      3574493.     0.0000000E+00 014100\n" +
                        "   1030861.      506322.4     0.0000000E+00 0.0000000E+00 -159844.3     005100\n" +
                        "  -19358.07     -3395.257     0.0000000E+00 0.0000000E+00  1999.026     500001\n" +
                        "  -356171.5     -278830.3     0.0000000E+00 0.0000000E+00  38137.81     410001\n" +
                        "  -1438898.     -1394987.     0.0000000E+00 0.0000000E+00  220503.0     320001\n" +
                        "  -2415814.     -2614463.     0.0000000E+00 0.0000000E+00  447705.4     230001\n" +
                        "  -1915213.     -2194962.     0.0000000E+00 0.0000000E+00  346673.0     140001\n" +
                        "  -618144.1     -719604.4     0.0000000E+00 0.0000000E+00  83368.52     050001\n" +
                        "  0.0000000E+00 0.0000000E+00  114252.1      50506.11     0.0000000E+00 401001\n" +
                        "  0.0000000E+00 0.0000000E+00  869753.1      449894.8     0.0000000E+00 311001\n" +
                        "  0.0000000E+00 0.0000000E+00  2419652.      1357198.     0.0000000E+00 221001\n" +
                        "  0.0000000E+00 0.0000000E+00  3201971.      1877091.     0.0000000E+00 131001\n" +
                        "  0.0000000E+00 0.0000000E+00  1666971.      999344.1     0.0000000E+00 041001\n" +
                        "   12415.13      37276.22     0.0000000E+00 0.0000000E+00 -20776.43     302001\n" +
                        "  -108852.0     -53558.09     0.0000000E+00 0.0000000E+00 -575.8226     212001\n" +
                        "  -122370.3     -333077.0     0.0000000E+00 0.0000000E+00  220766.9     122001\n" +
                        "   165963.5     -202170.4     0.0000000E+00 0.0000000E+00  247783.0     032001\n" +
                        "  0.0000000E+00 0.0000000E+00  42086.84     -9303.262     0.0000000E+00 203001\n" +
                        "  0.0000000E+00 0.0000000E+00  226767.6      52998.39     0.0000000E+00 113001\n" +
                        "  0.0000000E+00 0.0000000E+00  132410.7      29927.38     0.0000000E+00 023001\n" +
                        "   41612.74      25734.23     0.0000000E+00 0.0000000E+00 -28424.04     104001\n" +
                        "   35335.65      21998.51     0.0000000E+00 0.0000000E+00 -36893.93     014001\n" +
                        "  0.0000000E+00 0.0000000E+00 -8701.743     -6455.678     0.0000000E+00 005001\n" +
                        "   409368.7      803441.7     0.0000000E+00 0.0000000E+00 -477429.8     400200\n" +
                        "   2307929.      4833963.     0.0000000E+00 0.0000000E+00 -2551329.     310200\n" +
                        "   5520272.     0.1145219E+08 0.0000000E+00 0.0000000E+00 -5323730.     220200\n" +
                        "   5986897.     0.1250980E+08 0.0000000E+00 0.0000000E+00 -5284912.     130200\n" +
                        "   2079105.      5121180.     0.0000000E+00 0.0000000E+00 -2150724.     040200\n" +
                        "  0.0000000E+00 0.0000000E+00 -6167068.     -3950628.     0.0000000E+00 301200\n" +
                        "  0.0000000E+00 0.0000000E+00-0.3441075E+08-0.2161139E+08 0.0000000E+00 211200\n" +
                        "  0.0000000E+00 0.0000000E+00-0.6166168E+08-0.3838666E+08 0.0000000E+00 121200\n" +
                        "  0.0000000E+00 0.0000000E+00-0.3443045E+08-0.2135728E+08 0.0000000E+00 031200\n" +
                        "  -7199932.     -823288.7     0.0000000E+00 0.0000000E+00 -2453463.     202200\n" +
                        " -0.2829128E+08 -5368481.     0.0000000E+00 0.0000000E+00 -6934879.     112200\n" +
                        " -0.2480877E+08 -5100019.     0.0000000E+00 0.0000000E+00 -5804856.     022200\n" +
                        "  0.0000000E+00 0.0000000E+00 0.1095148E+08  6985582.     0.0000000E+00 103200\n" +
                        "  0.0000000E+00 0.0000000E+00 0.2078071E+08 0.1338262E+08 0.0000000E+00 013200\n" +
                        "   4780114.      2369424.     0.0000000E+00 0.0000000E+00 -692683.6     004200\n" +
                        "  0.0000000E+00 0.0000000E+00  295806.1      147179.5     0.0000000E+00 400101\n" +
                        "  0.0000000E+00 0.0000000E+00  2082268.      1134530.     0.0000000E+00 310101\n" +
                        "  0.0000000E+00 0.0000000E+00  5412891.      3097689.     0.0000000E+00 220101\n" +
                        "  0.0000000E+00 0.0000000E+00  6764836.      3979751.     0.0000000E+00 130101\n" +
                        "  0.0000000E+00 0.0000000E+00  3414458.      2040729.     0.0000000E+00 040101\n" +
                        "   8994.579      32201.41     0.0000000E+00 0.0000000E+00 -37705.69     301101\n" +
                        "  -406702.9     -524605.1     0.0000000E+00 0.0000000E+00  164306.2     211101\n" +
                        "  -352431.4     -1584930.     0.0000000E+00 0.0000000E+00  993330.7     121101\n" +
                        "   637426.1     -925105.8     0.0000000E+00 0.0000000E+00  970672.6     031101\n" +
                        "  0.0000000E+00 0.0000000E+00  726381.6      252471.3     0.0000000E+00 202101\n" +
                        "  0.0000000E+00 0.0000000E+00  2987185.      1353014.     0.0000000E+00 112101\n" +
                        "  0.0000000E+00 0.0000000E+00  2252386.      1098794.     0.0000000E+00 022101\n" +
                        "   490373.3      222602.1     0.0000000E+00 0.0000000E+00 -173858.0     103101\n" +
                        "   616073.0      229000.6     0.0000000E+00 0.0000000E+00 -167088.5     013101\n" +
                        "  0.0000000E+00 0.0000000E+00 -91865.61     -56262.04     0.0000000E+00 004101\n" +
                        "   28102.51      32098.16     0.0000000E+00 0.0000000E+00 -4691.716     400002\n" +
                        "   94656.06      148792.8     0.0000000E+00 0.0000000E+00 -37694.85     310002\n" +
                        "   87470.36      229285.9     0.0000000E+00 0.0000000E+00 -64097.26     220002\n" +
                        "   35825.63      171309.6     0.0000000E+00 0.0000000E+00 -20131.12     130002\n" +
                        "   12631.17      69438.60     0.0000000E+00 0.0000000E+00  5808.256     040002\n" +
                        "  0.0000000E+00 0.0000000E+00 -25632.14     -16417.85     0.0000000E+00 301002\n" +
                        "  0.0000000E+00 0.0000000E+00 -159482.8     -92537.12     0.0000000E+00 211002\n" +
                        "  0.0000000E+00 0.0000000E+00 -441378.5     -247621.0     0.0000000E+00 121002\n" +
                        "  0.0000000E+00 0.0000000E+00 -362684.4     -208321.9     0.0000000E+00 031002\n" +
                        "   19542.78      24157.04     0.0000000E+00 0.0000000E+00 -13458.03     202002\n" +
                        "  -10450.23      56181.41     0.0000000E+00 0.0000000E+00 -66820.63     112002\n" +
                        "  -76463.51      14329.85     0.0000000E+00 0.0000000E+00 -63788.82     022002\n" +
                        "  0.0000000E+00 0.0000000E+00  1956.606     -4683.185     0.0000000E+00 103002\n" +
                        "  0.0000000E+00 0.0000000E+00  51177.61      22767.10     0.0000000E+00 013002\n" +
                        "   7287.071      2713.965     0.0000000E+00 0.0000000E+00 -336.0502     004002\n" +
                        "  0.0000000E+00 0.0000000E+00 -4203623.     -2627506.     0.0000000E+00 300300\n" +
                        "  0.0000000E+00 0.0000000E+00-0.2297486E+08-0.1426617E+08 0.0000000E+00 210300\n" +
                        "  0.0000000E+00 0.0000000E+00-0.4099944E+08-0.2539665E+08 0.0000000E+00 120300\n" +
                        "  0.0000000E+00 0.0000000E+00-0.2326937E+08-0.1441064E+08 0.0000000E+00 030300\n" +
                        "  -8765080.     -571848.9     0.0000000E+00 0.0000000E+00 -3250858.     201300\n" +
                        " -0.3470738E+08 -5056420.     0.0000000E+00 0.0000000E+00 -9446777.     111300\n" +
                        " -0.3098882E+08 -5060611.     0.0000000E+00 0.0000000E+00 -8141309.     021300\n" +
                        "  0.0000000E+00 0.0000000E+00 0.1945924E+08 0.1281157E+08 0.0000000E+00 102300\n" +
                        "  0.0000000E+00 0.0000000E+00 0.3710760E+08 0.2441349E+08 0.0000000E+00 012300\n" +
                        "  0.1172563E+08  5904017.     0.0000000E+00 0.0000000E+00 -1672532.     003300\n" +
                        "  -4639.790     -67379.11     0.0000000E+00 0.0000000E+00  12085.48     300201\n" +
                        "  -370503.3     -812997.3     0.0000000E+00 0.0000000E+00  342930.7     210201\n" +
                        "  -327969.1     -1870613.     0.0000000E+00 0.0000000E+00  1121075.     120201\n" +
                        "   551585.2     -1059512.     0.0000000E+00 0.0000000E+00  956776.1     030201\n" +
                        "  0.0000000E+00 0.0000000E+00  2329273.      1087604.     0.0000000E+00 201201\n" +
                        "  0.0000000E+00 0.0000000E+00  8986341.      4641641.     0.0000000E+00 111201\n" +
                        "  0.0000000E+00 0.0000000E+00  7184573.      3890750.     0.0000000E+00 021201\n" +
                        "   1986235.      752220.8     0.0000000E+00 0.0000000E+00 -354745.2     102201\n" +
                        "   2875270.      868815.9     0.0000000E+00 0.0000000E+00 -119375.3     012201\n" +
                        "  0.0000000E+00 0.0000000E+00 -479518.1     -261250.0     0.0000000E+00 003201\n" +
                        "  0.0000000E+00 0.0000000E+00 -49943.73     -31604.76     0.0000000E+00 300102\n" +
                        "  0.0000000E+00 0.0000000E+00 -338803.0     -191623.8     0.0000000E+00 210102\n" +
                        "  0.0000000E+00 0.0000000E+00 -990890.2     -551232.0     0.0000000E+00 120102\n" +
                        "  0.0000000E+00 0.0000000E+00 -833128.5     -478268.6     0.0000000E+00 030102\n" +
                        "   71938.46      86247.16     0.0000000E+00 0.0000000E+00 -61610.73     201102\n" +
                        "  -24284.80      221091.2     0.0000000E+00 0.0000000E+00 -272097.0     111102\n" +
                        "  -272627.9      87226.57     0.0000000E+00 0.0000000E+00 -259219.6     021102\n" +
                        "  0.0000000E+00 0.0000000E+00  51625.14      4832.829     0.0000000E+00 102102\n" +
                        "  0.0000000E+00 0.0000000E+00  304619.9      156014.7     0.0000000E+00 012102\n" +
                        "   60288.48      25428.36     0.0000000E+00 0.0000000E+00 -7648.733     003102\n" +
                        "   7915.976      5306.325     0.0000000E+00 0.0000000E+00 -155.0717     300003\n" +
                        "   24937.98      22600.91     0.0000000E+00 0.0000000E+00 -7779.450     210003\n" +
                        "   18421.30      17912.72     0.0000000E+00 0.0000000E+00 -15353.79     120003\n" +
                        "   9271.161     -714.8539     0.0000000E+00 0.0000000E+00 -4018.221     030003\n" +
                        "  0.0000000E+00 0.0000000E+00  74.02473     -885.4035     0.0000000E+00 201003\n" +
                        "  0.0000000E+00 0.0000000E+00  27387.41      12763.59     0.0000000E+00 111003\n" +
                        "  0.0000000E+00 0.0000000E+00  35411.08      20358.22     0.0000000E+00 021003\n" +
                        "   5864.956      2206.164     0.0000000E+00 0.0000000E+00  1072.591     102003\n" +
                        "   10036.97      5708.743     0.0000000E+00 0.0000000E+00  148.8084     012003\n" +
                        "  0.0000000E+00 0.0000000E+00 -4146.881     -2209.996     0.0000000E+00 003003\n" +
                        "  -3987730.     -6024.043     0.0000000E+00 0.0000000E+00 -1689319.     200400\n" +
                        " -0.1595780E+08 -1542349.     0.0000000E+00 0.0000000E+00 -4906942.     110400\n" +
                        " -0.1448441E+08 -1740305.     0.0000000E+00 0.0000000E+00 -4248030.     020400\n" +
                        "  0.0000000E+00 0.0000000E+00 0.1692154E+08 0.1155811E+08 0.0000000E+00 101400\n" +
                        "  0.0000000E+00 0.0000000E+00 0.3227321E+08 0.2176470E+08 0.0000000E+00 011400\n" +
                        "  0.1601908E+08  8237194.     0.0000000E+00 0.0000000E+00 -2370517.     002400\n" +
                        "  0.0000000E+00 0.0000000E+00  2062960.      1065764.     0.0000000E+00 200301\n" +
                        "  0.0000000E+00 0.0000000E+00  7771311.      4231807.     0.0000000E+00 110301\n" +
                        "  0.0000000E+00 0.0000000E+00  6403562.      3603987.     0.0000000E+00 020301\n" +
                        "   3369287.      1153226.     0.0000000E+00 0.0000000E+00 -264474.8     101301\n" +
                        "   5200072.      1417300.     0.0000000E+00 0.0000000E+00  325614.1     011301\n" +
                        "  0.0000000E+00 0.0000000E+00 -1270146.     -671350.6     0.0000000E+00 002301\n" +
                        "   75598.52      80334.01     0.0000000E+00 0.0000000E+00 -64772.51     200202\n" +
                        "  -12016.75      217216.9     0.0000000E+00 0.0000000E+00 -265820.2     110202\n" +
                        "  -274561.5      100927.1     0.0000000E+00 0.0000000E+00 -258100.7     020202\n" +
                        "  0.0000000E+00 0.0000000E+00  91197.67      29813.70     0.0000000E+00 101202\n" +
                        "  0.0000000E+00 0.0000000E+00  440460.0      257842.2     0.0000000E+00 011202\n" +
                        "   173035.6      85096.45     0.0000000E+00 0.0000000E+00 -37215.52     002202\n" +
                        "  0.0000000E+00 0.0000000E+00  9072.183      2366.858     0.0000000E+00 200103\n" +
                        "  0.0000000E+00 0.0000000E+00  95024.01      47280.23     0.0000000E+00 110103\n" +
                        "  0.0000000E+00 0.0000000E+00  104790.5      60723.46     0.0000000E+00 020103\n" +
                        "   23248.68      7911.027     0.0000000E+00 0.0000000E+00  2604.029     101103\n" +
                        "   39008.62      17949.28     0.0000000E+00 0.0000000E+00  1644.809     011103\n" +
                        "  0.0000000E+00 0.0000000E+00 -21951.14     -12351.12     0.0000000E+00 002103\n" +
                        "   1140.596      455.0980     0.0000000E+00 0.0000000E+00  321.1213     200004\n" +
                        "   2386.748      2705.117     0.0000000E+00 0.0000000E+00 -152.4786     110004\n" +
                        "  -1476.071      1791.881     0.0000000E+00 0.0000000E+00 -1348.207     020004\n" +
                        "  0.0000000E+00 0.0000000E+00 -1861.318     -1002.906     0.0000000E+00 101004\n" +
                        "  0.0000000E+00 0.0000000E+00 -1951.432     -1400.586     0.0000000E+00 011004\n" +
                        "   68.48920     -84.02398     0.0000000E+00 0.0000000E+00  92.91288     002004\n" +
                        "  0.0000000E+00 0.0000000E+00  5759198.      4121854.     0.0000000E+00 100500\n" +
                        "  0.0000000E+00 0.0000000E+00 0.1088957E+08  7576283.     0.0000000E+00 010500\n" +
                        "  0.1151080E+08  6069811.     0.0000000E+00 0.0000000E+00 -1861180.     001500\n" +
                        "   2051421.      665668.6     0.0000000E+00 0.0000000E+00 -39325.22     100401\n" +
                        "   3260790.      836444.4     0.0000000E+00 0.0000000E+00  390353.1     010401\n" +
                        "  0.0000000E+00 0.0000000E+00 -1594190.     -849276.4     0.0000000E+00 001401\n" +
                        "  0.0000000E+00 0.0000000E+00 -21825.04     -8591.958     0.0000000E+00 100302\n" +
                        "  0.0000000E+00 0.0000000E+00  67554.70      65505.10     0.0000000E+00 010302\n" +
                        "   190885.4      118349.4     0.0000000E+00 0.0000000E+00 -72306.75     001302\n" +
                        "   25557.72      7243.034     0.0000000E+00 0.0000000E+00  2547.001     100203\n" +
                        "   41388.50      13289.86     0.0000000E+00 0.0000000E+00  6050.725     010203\n" +
                        "  0.0000000E+00 0.0000000E+00 -36972.58     -22019.38     0.0000000E+00 001203\n" +
                        "  0.0000000E+00 0.0000000E+00 -4263.406     -2522.843     0.0000000E+00 100104\n" +
                        "  0.0000000E+00 0.0000000E+00 -3410.561     -2962.204     0.0000000E+00 010104\n" +
                        "   353.3260     -153.9674     0.0000000E+00 0.0000000E+00  136.4570     001104\n" +
                        "   130.5069     -22.22063     0.0000000E+00 0.0000000E+00  56.05008     100005\n" +
                        "   388.3664      84.23591     0.0000000E+00 0.0000000E+00  116.5044     010005\n" +
                        "  0.0000000E+00 0.0000000E+00 -73.17767     -18.04058     0.0000000E+00 001005\n" +
                        "   3372043.      1829083.     0.0000000E+00 0.0000000E+00 -629378.7     000600\n" +
                        "  0.0000000E+00 0.0000000E+00 -749746.7     -408509.6     0.0000000E+00 000501\n" +
                        "   54935.70      56087.89     0.0000000E+00 0.0000000E+00 -51523.08     000402\n" +
                        "  0.0000000E+00 0.0000000E+00 -19180.25     -12470.26     0.0000000E+00 000303\n" +
                        "   498.6294      85.02630     0.0000000E+00 0.0000000E+00 -184.6004     000204\n" +
                        "  0.0000000E+00 0.0000000E+00 -208.2477     -70.65276     0.0000000E+00 000105\n" +
                        "  -2.694354     -3.107070     0.0000000E+00 0.0000000E+00 -1.426607     000006\n" +
                        "   1299821.      1175678.     0.0000000E+00 0.0000000E+00 -218477.2     700000\n" +
                        "  0.1514061E+08 0.1416957E+08 0.0000000E+00 0.0000000E+00 -2470435.     610000\n" +
                        "  0.7186537E+08 0.6862733E+08 0.0000000E+00 0.0000000E+00-0.1213032E+08 520000\n" +
                        "  0.1856427E+09 0.1793276E+09 0.0000000E+00 0.0000000E+00-0.3269879E+08 430000\n" +
                        "  0.2870993E+09 0.2795146E+09 0.0000000E+00 0.0000000E+00-0.5227678E+08 340000\n" +
                        "  0.2683052E+09 0.2635154E+09 0.0000000E+00 0.0000000E+00-0.5012194E+08 250000\n" +
                        "  0.1404726E+09 0.1397250E+09 0.0000000E+00 0.0000000E+00-0.2696816E+08 160000\n" +
                        "  0.3163093E+08 0.3202245E+08 0.0000000E+00 0.0000000E+00 -6296555.     070000\n" +
                        "  0.0000000E+00 0.0000000E+00 -2205289.     -1349526.     0.0000000E+00 601000\n" +
                        "  0.0000000E+00 0.0000000E+00-0.1994686E+08-0.1247045E+08 0.0000000E+00 511000\n" +
                        "  0.0000000E+00 0.0000000E+00-0.7610067E+08-0.4800010E+08 0.0000000E+00 421000\n" +
                        "  0.0000000E+00 0.0000000E+00-0.1579003E+09-0.9974522E+08 0.0000000E+00 331000\n" +
                        "  0.0000000E+00 0.0000000E+00-0.1866133E+09-0.1177652E+09 0.0000000E+00 241000\n" +
                        "  0.0000000E+00 0.0000000E+00-0.1179637E+09-0.7442149E+08 0.0000000E+00 151000\n" +
                        "  0.0000000E+00 0.0000000E+00-0.3107972E+08-0.1964465E+08 0.0000000E+00 061000\n" +
                        "   1392181.      1554331.     0.0000000E+00 0.0000000E+00 -875146.0     502000\n" +
                        "  0.1492377E+08 0.1826641E+08 0.0000000E+00 0.0000000E+00 -7877194.     412000\n" +
                        "  0.5409041E+08 0.6959713E+08 0.0000000E+00 0.0000000E+00-0.2656498E+08 322000\n" +
                        "  0.9069503E+08 0.1200923E+09 0.0000000E+00 0.0000000E+00-0.4211173E+08 232000\n" +
                        "  0.7398304E+08 0.9921347E+08 0.0000000E+00 0.0000000E+00-0.3170626E+08 142000\n" +
                        "  0.2405962E+08 0.3253097E+08 0.0000000E+00 0.0000000E+00 -9256683.     052000\n" +
                        "  0.0000000E+00 0.0000000E+00 -5358290.     -2849839.     0.0000000E+00 403000\n" +
                        "  0.0000000E+00 0.0000000E+00-0.3598701E+08-0.2083491E+08 0.0000000E+00 313000\n" +
                        "  0.0000000E+00 0.0000000E+00-0.9236214E+08-0.5563242E+08 0.0000000E+00 223000\n" +
                        "  0.0000000E+00 0.0000000E+00-0.1104469E+09-0.6760759E+08 0.0000000E+00 133000\n" +
                        "  0.0000000E+00 0.0000000E+00-0.5151456E+08-0.3168229E+08 0.0000000E+00 043000\n" +
                        "  -2069608.     -84250.00     0.0000000E+00 0.0000000E+00 -225690.5     304000\n" +
                        " -0.1098430E+08  1106930.     0.0000000E+00 0.0000000E+00 -3403750.     214000\n" +
                        " -0.2122184E+08  3185202.     0.0000000E+00 0.0000000E+00 -8560446.     124000\n" +
                        " -0.1488471E+08  1510617.     0.0000000E+00 0.0000000E+00 -6050493.     034000\n" +
                        "  0.0000000E+00 0.0000000E+00  3121252.      2553307.     0.0000000E+00 205000\n" +
                        "  0.0000000E+00 0.0000000E+00 0.1271425E+08  9234983.     0.0000000E+00 115000\n" +
                        "  0.0000000E+00 0.0000000E+00 0.1366939E+08  9344942.     0.0000000E+00 025000\n" +
                        "   1604740.      587574.6     0.0000000E+00 0.0000000E+00  302680.9     106000\n" +
                        "   3568805.      1496004.     0.0000000E+00 0.0000000E+00  257690.2     016000\n" +
                        "  0.0000000E+00 0.0000000E+00 -217270.2     -103850.9     0.0000000E+00 007000\n" +
                        "  0.0000000E+00 0.0000000E+00 -4447946.     -2740815.     0.0000000E+00 600100\n" +
                        "  0.0000000E+00 0.0000000E+00-0.3973883E+08-0.2500290E+08 0.0000000E+00 510100\n" +
                        "  0.0000000E+00 0.0000000E+00-0.1495715E+09-0.9483983E+08 0.0000000E+00 420100\n" +
                        "  0.0000000E+00 0.0000000E+00-0.3065334E+09-0.1944918E+09 0.0000000E+00 330100\n" +
                        "  0.0000000E+00 0.0000000E+00-0.3588962E+09-0.2274127E+09 0.0000000E+00 240100\n" +
                        "  0.0000000E+00 0.0000000E+00-0.2252738E+09-0.1426976E+09 0.0000000E+00 150100\n" +
                        "  0.0000000E+00 0.0000000E+00-0.5886429E+08-0.3734325E+08 0.0000000E+00 060100\n" +
                        "   7524571.      8207003.     0.0000000E+00 0.0000000E+00 -4212123.     501100\n" +
                        "  0.7068094E+08 0.8268840E+08 0.0000000E+00 0.0000000E+00-0.3583616E+08 411100\n" +
                        "  0.2441972E+09 0.2997122E+09 0.0000000E+00 0.0000000E+00-0.1169924E+09 321100\n" +
                        "  0.4027619E+09 0.5102763E+09 0.0000000E+00 0.0000000E+00-0.1836032E+09 231100\n" +
                        "  0.3277297E+09 0.4218417E+09 0.0000000E+00 0.0000000E+00-0.1393383E+09 141100\n" +
                        "  0.1074694E+09 0.1389839E+09 0.0000000E+00 0.0000000E+00-0.4144830E+08 051100\n" +
                        "  0.0000000E+00 0.0000000E+00-0.3327865E+08-0.1810731E+08 0.0000000E+00 402100\n" +
                        "  0.0000000E+00 0.0000000E+00-0.2213591E+09-0.1284843E+09 0.0000000E+00 312100\n" +
                        "  0.0000000E+00 0.0000000E+00-0.5613040E+09-0.3364542E+09 0.0000000E+00 222100\n" +
                        "  0.0000000E+00 0.0000000E+00-0.6614706E+09-0.4027470E+09 0.0000000E+00 132100\n" +
                        "  0.0000000E+00 0.0000000E+00-0.3052410E+09-0.1872573E+09 0.0000000E+00 042100\n" +
                        " -0.1321135E+08  3987218.     0.0000000E+00 0.0000000E+00 -4413879.     303100\n" +
                        " -0.7303610E+08 0.2582560E+08 0.0000000E+00 0.0000000E+00-0.3495794E+08 213100\n" +
                        " -0.1435932E+09 0.4838676E+08 0.0000000E+00 0.0000000E+00-0.7430835E+08 123100\n" +
                        " -0.1019316E+09 0.2436525E+08 0.0000000E+00 0.0000000E+00-0.4893606E+08 033100\n" +
                        "  0.0000000E+00 0.0000000E+00 0.2432057E+08 0.2027274E+08 0.0000000E+00 204100\n" +
                        "  0.0000000E+00 0.0000000E+00 0.9983510E+08 0.7394569E+08 0.0000000E+00 114100\n" +
                        "  0.0000000E+00 0.0000000E+00 0.1079902E+09 0.7518667E+08 0.0000000E+00 024100\n" +
                        "  0.1684679E+08  6810828.     0.0000000E+00 0.0000000E+00  2457170.     105100\n" +
                        "  0.3742553E+08 0.1678681E+08 0.0000000E+00 0.0000000E+00  1514934.     015100\n" +
                        "  0.0000000E+00 0.0000000E+00 -2793396.     -1505146.     0.0000000E+00 006100\n" +
                        "  -972979.6     -923272.9     0.0000000E+00 0.0000000E+00  173338.2     600001\n" +
                        "  -8092325.     -7601833.     0.0000000E+00 0.0000000E+00  1862951.     510001\n" +
                        " -0.3121948E+08-0.2875619E+08 0.0000000E+00 0.0000000E+00  7496554.     420001\n" +
                        " -0.7008116E+08-0.6452772E+08 0.0000000E+00 0.0000000E+00 0.1596679E+08 330001\n" +
                        " -0.9174218E+08-0.8639531E+08 0.0000000E+00 0.0000000E+00 0.1984291E+08 240001\n" +
                        " -0.6375218E+08-0.6203919E+08 0.0000000E+00 0.0000000E+00 0.1351962E+08 150001\n" +
                        " -0.1804015E+08-0.1811568E+08 0.0000000E+00 0.0000000E+00  3788169.     060001\n" +
                        "  0.0000000E+00 0.0000000E+00  815096.4      495444.9     0.0000000E+00 501001\n" +
                        "  0.0000000E+00 0.0000000E+00  7852257.      4485295.     0.0000000E+00 411001\n" +
                        "  0.0000000E+00 0.0000000E+00 0.3095643E+08 0.1730916E+08 0.0000000E+00 321001\n" +
                        "  0.0000000E+00 0.0000000E+00 0.5832715E+08 0.3267867E+08 0.0000000E+00 231001\n" +
                        "  0.0000000E+00 0.0000000E+00 0.5332490E+08 0.3021624E+08 0.0000000E+00 141001\n" +
                        "  0.0000000E+00 0.0000000E+00 0.1981581E+08 0.1139784E+08 0.0000000E+00 051001\n" +
                        "  -1085431.     -1628205.     0.0000000E+00 0.0000000E+00  542391.1     402001\n" +
                        "  -4353040.     -7989922.     0.0000000E+00 0.0000000E+00  3263199.     312001\n" +
                        "  -7145741.    -0.1512261E+08 0.0000000E+00 0.0000000E+00  6591529.     222001\n" +
                        "  -7733755.    -0.1547202E+08 0.0000000E+00 0.0000000E+00  6135489.     132001\n" +
                        "  -3939152.     -7437631.     0.0000000E+00 0.0000000E+00  2689519.     042001\n" +
                        "  0.0000000E+00 0.0000000E+00  1473835.      1055225.     0.0000000E+00 303001\n" +
                        "  0.0000000E+00 0.0000000E+00  9535029.      5821813.     0.0000000E+00 213001\n" +
                        "  0.0000000E+00 0.0000000E+00 0.2284907E+08 0.1311967E+08 0.0000000E+00 123001\n" +
                        "  0.0000000E+00 0.0000000E+00 0.1667740E+08  9539584.     0.0000000E+00 033001\n" +
                        "   438791.0     -308752.3     0.0000000E+00 0.0000000E+00  424320.8     204001\n" +
                        "   3420887.     -326580.0     0.0000000E+00 0.0000000E+00  1458107.     114001\n" +
                        "   4744086.      643562.3     0.0000000E+00 0.0000000E+00  1161507.     024001\n" +
                        "  0.0000000E+00 0.0000000E+00 -299981.1      77824.67     0.0000000E+00 105001\n" +
                        "  0.0000000E+00 0.0000000E+00 -1554155.     -598689.7     0.0000000E+00 015001\n" +
                        "  -137214.1     -38856.30     0.0000000E+00 0.0000000E+00  28101.31     006001\n" +
                        "   8780143.      9533511.     0.0000000E+00 0.0000000E+00 -4788180.     500200\n" +
                        "  0.7732125E+08 0.8822983E+08 0.0000000E+00 0.0000000E+00-0.3930648E+08 410200\n" +
                        "  0.2610085E+09 0.3102460E+09 0.0000000E+00 0.0000000E+00-0.1255178E+09 320200\n" +
                        "  0.4279016E+09 0.5246048E+09 0.0000000E+00 0.0000000E+00-0.1956072E+09 230200\n" +
                        "  0.3481919E+09 0.4346075E+09 0.0000000E+00 0.0000000E+00-0.1493219E+09 140200\n" +
                        "  0.1145005E+09 0.1437211E+09 0.0000000E+00 0.0000000E+00-0.4500961E+08 050200\n" +
                        "  0.0000000E+00 0.0000000E+00-0.6772724E+08-0.3755289E+08 0.0000000E+00 401200\n" +
                        "  0.0000000E+00 0.0000000E+00-0.4482531E+09-0.2610583E+09 0.0000000E+00 311200\n" +
                        "  0.0000000E+00 0.0000000E+00-0.1126817E+10-0.6734441E+09 0.0000000E+00 221200\n" +
                        "  0.0000000E+00 0.0000000E+00-0.1310245E+10-0.7943071E+09 0.0000000E+00 131200\n" +
                        "  0.0000000E+00 0.0000000E+00-0.5968464E+09-0.3649044E+09 0.0000000E+00 041200\n" +
                        " -0.2981500E+08 0.2437478E+08 0.0000000E+00 0.0000000E+00-0.2114625E+08 302200\n" +
                        " -0.1748038E+09 0.1250282E+09 0.0000000E+00 0.0000000E+00-0.1299089E+09 212200\n" +
                        " -0.3538245E+09 0.2107113E+09 0.0000000E+00 0.0000000E+00-0.2450317E+09 122200\n" +
                        " -0.2568797E+09 0.1073703E+09 0.0000000E+00 0.0000000E+00-0.1509722E+09 032200\n" +
                        "  0.0000000E+00 0.0000000E+00 0.7342181E+08 0.6386400E+08 0.0000000E+00 203200\n" +
                        "  0.0000000E+00 0.0000000E+00 0.3033196E+09 0.2332902E+09 0.0000000E+00 113200\n" +
                        "  0.0000000E+00 0.0000000E+00 0.3295449E+09 0.2366551E+09 0.0000000E+00 023200\n" +
                        "  0.7312030E+08 0.3249425E+08 0.0000000E+00 0.0000000E+00  7590892.     104200\n" +
                        "  0.1627323E+09 0.7855548E+08 0.0000000E+00 0.0000000E+00  953453.3     014200\n" +
                        "  0.0000000E+00 0.0000000E+00-0.1509342E+08 -8968399.     0.0000000E+00 005200\n" +
                        "  0.0000000E+00 0.0000000E+00  1762765.      1093365.     0.0000000E+00 500101\n" +
                        "  0.0000000E+00 0.0000000E+00 0.1678104E+08  9726544.     0.0000000E+00 410101\n" +
                        "  0.0000000E+00 0.0000000E+00 0.6569336E+08 0.3729099E+08 0.0000000E+00 320101\n" +
                        "  0.0000000E+00 0.0000000E+00 0.1230284E+09 0.7006209E+08 0.0000000E+00 230101\n" +
                        "  0.0000000E+00 0.0000000E+00 0.1110928E+09 0.6392958E+08 0.0000000E+00 140101\n" +
                        "  0.0000000E+00 0.0000000E+00 0.4029542E+08 0.2345047E+08 0.0000000E+00 050101\n" +
                        "  -4270341.     -6205233.     0.0000000E+00 0.0000000E+00  2497752.     401101\n" +
                        " -0.2155259E+08-0.3512317E+08 0.0000000E+00 0.0000000E+00 0.1525179E+08 311101\n" +
                        " -0.4537822E+08-0.7698781E+08 0.0000000E+00 0.0000000E+00 0.3306423E+08 221101\n" +
                        " -0.5206696E+08-0.8434630E+08 0.0000000E+00 0.0000000E+00 0.3282927E+08 131101\n" +
                        " -0.2426871E+08-0.3896130E+08 0.0000000E+00 0.0000000E+00 0.1388240E+08 041101\n" +
                        "  0.0000000E+00 0.0000000E+00 0.1095784E+08  7223249.     0.0000000E+00 302101\n" +
                        "  0.0000000E+00 0.0000000E+00 0.6985001E+08 0.4140702E+08 0.0000000E+00 212101\n" +
                        "  0.0000000E+00 0.0000000E+00 0.1593504E+09 0.9126902E+08 0.0000000E+00 122101\n" +
                        "  0.0000000E+00 0.0000000E+00 0.1134197E+09 0.6509932E+08 0.0000000E+00 032101\n" +
                        "   5574607.     -1187755.     0.0000000E+00 0.0000000E+00  3274918.     203101\n" +
                        "  0.3231417E+08 -282404.8     0.0000000E+00 0.0000000E+00 0.1150029E+08 113101\n" +
                        "  0.4074443E+08  5166555.     0.0000000E+00 0.0000000E+00 0.1007531E+08 023101\n" +
                        "  0.0000000E+00 0.0000000E+00 -5293428.     -1062986.     0.0000000E+00 104101\n" +
                        "  0.0000000E+00 0.0000000E+00-0.1825641E+08 -8306188.     0.0000000E+00 014101\n" +
                        "  -2163098.     -736153.0     0.0000000E+00 0.0000000E+00  359196.9     005101\n" +
                        "  -86938.91     -126520.3     0.0000000E+00 0.0000000E+00 -41618.99     500002\n" +
                        "   644325.3      149766.9     0.0000000E+00 0.0000000E+00 -225607.7     410002\n" +
                        "   5593459.      4331907.     0.0000000E+00 0.0000000E+00 -963101.7     320002\n" +
                        "  0.1253387E+08 0.1175600E+08 0.0000000E+00 0.0000000E+00 -2327480.     230002\n" +
                        "  0.1137083E+08 0.1172350E+08 0.0000000E+00 0.0000000E+00 -2316422.     140002\n" +
                        "   3793877.      4085420.     0.0000000E+00 0.0000000E+00 -718359.4     050002\n" +
                        "  0.0000000E+00 0.0000000E+00 -133976.3     -4710.127     0.0000000E+00 401002\n" +
                        "  0.0000000E+00 0.0000000E+00 -2052844.     -783097.9     0.0000000E+00 311002\n" +
                        "  0.0000000E+00 0.0000000E+00 -6920607.     -3248210.     0.0000000E+00 221002\n" +
                        "  0.0000000E+00 0.0000000E+00 -9973509.     -5137676.     0.0000000E+00 131002\n" +
                        "  0.0000000E+00 0.0000000E+00 -5805019.     -3148304.     0.0000000E+00 041002\n" +
                        "  -287692.5     -319714.6     0.0000000E+00 0.0000000E+00  30328.37     302002\n" +
                        "  -470204.8     -981390.1     0.0000000E+00 0.0000000E+00  258901.0     212002\n" +
                        "   674174.0      65387.07     0.0000000E+00 0.0000000E+00 -30335.51     122002\n" +
                        "   485044.7      912778.5     0.0000000E+00 0.0000000E+00 -559546.3     032002\n" +
                        "  0.0000000E+00 0.0000000E+00  209606.0      220015.0     0.0000000E+00 203002\n" +
                        "  0.0000000E+00 0.0000000E+00 -375243.5      62835.84     0.0000000E+00 113002\n" +
                        "  0.0000000E+00 0.0000000E+00 -675834.0     -236606.1     0.0000000E+00 023002\n" +
                        "  -91815.49     -33700.46     0.0000000E+00 0.0000000E+00  18693.10     104002\n" +
                        "  -234139.4     -149968.8     0.0000000E+00 0.0000000E+00  100108.7     014002\n" +
                        "  0.0000000E+00 0.0000000E+00  84118.74      55578.05     0.0000000E+00 005002\n" +
                        "  0.0000000E+00 0.0000000E+00-0.4536183E+08-0.2554099E+08 0.0000000E+00 400300\n" +
                        "  0.0000000E+00 0.0000000E+00-0.2994641E+09-0.1749233E+09 0.0000000E+00 310300\n" +
                        "  0.0000000E+00 0.0000000E+00-0.7480523E+09-0.4460819E+09 0.0000000E+00 220300\n" +
                        "  0.0000000E+00 0.0000000E+00-0.8596856E+09-0.5192278E+09 0.0000000E+00 130300\n" +
                        "  0.0000000E+00 0.0000000E+00-0.3863541E+09-0.2353508E+09 0.0000000E+00 040300\n" +
                        " -0.2737731E+08 0.4672516E+08 0.0000000E+00 0.0000000E+00-0.3841125E+08 301300\n" +
                        " -0.1762532E+09 0.2241315E+09 0.0000000E+00 0.0000000E+00-0.2071857E+09 211300\n" +
                        " -0.3730243E+09 0.3627250E+09 0.0000000E+00 0.0000000E+00-0.3600816E+09 121300\n" +
                        " -0.2801453E+09 0.1860190E+09 0.0000000E+00 0.0000000E+00-0.2096755E+09 031300\n" +
                        "  0.0000000E+00 0.0000000E+00 0.1045565E+09 0.9903008E+08 0.0000000E+00 202300\n" +
                        "  0.0000000E+00 0.0000000E+00 0.4362775E+09 0.3595115E+09 0.0000000E+00 112300\n" +
                        "  0.0000000E+00 0.0000000E+00 0.4762989E+09 0.3606187E+09 0.0000000E+00 022300\n" +
                        "  0.1673392E+09 0.8169293E+08 0.0000000E+00 0.0000000E+00 0.1012740E+08 103300\n" +
                        "  0.3748809E+09 0.1960177E+09 0.0000000E+00 0.0000000E+00-0.1243073E+08 013300\n" +
                        "  0.0000000E+00 0.0000000E+00-0.4546255E+08-0.2953195E+08 0.0000000E+00 004300\n" +
                        "  -4381610.     -5961079.     0.0000000E+00 0.0000000E+00  2824377.     400201\n" +
                        " -0.2584634E+08-0.3804600E+08 0.0000000E+00 0.0000000E+00 0.1743237E+08 310201\n" +
                        " -0.6017514E+08-0.9128210E+08 0.0000000E+00 0.0000000E+00 0.3945850E+08 220201\n" +
                        " -0.6926005E+08-0.1032539E+09 0.0000000E+00 0.0000000E+00 0.4054004E+08 130201\n" +
                        " -0.3096153E+08-0.4661319E+08 0.0000000E+00 0.0000000E+00 0.1678408E+08 040201\n" +
                        "  0.0000000E+00 0.0000000E+00 0.2636867E+08 0.1628289E+08 0.0000000E+00 301201\n" +
                        "  0.0000000E+00 0.0000000E+00 0.1639255E+09 0.9494693E+08 0.0000000E+00 211201\n" +
                        "  0.0000000E+00 0.0000000E+00 0.3587746E+09 0.2046581E+09 0.0000000E+00 121201\n" +
                        "  0.0000000E+00 0.0000000E+00 0.2502665E+09 0.1437310E+09 0.0000000E+00 031201\n" +
                        "  0.2149578E+08 -1301826.     0.0000000E+00 0.0000000E+00  9764239.     202201\n" +
                        "  0.1083615E+09  2411666.     0.0000000E+00 0.0000000E+00 0.3511828E+08 112201\n" +
                        "  0.1287352E+09 0.1380518E+08 0.0000000E+00 0.0000000E+00 0.3300528E+08 022201\n" +
                        "  0.0000000E+00 0.0000000E+00-0.2682210E+08 -9447984.     0.0000000E+00 103201\n" +
                        "  0.0000000E+00 0.0000000E+00-0.7816685E+08-0.3893717E+08 0.0000000E+00 013201\n" +
                        " -0.1306645E+08 -5035249.     0.0000000E+00 0.0000000E+00  1908974.     004201\n" +
                        "  0.0000000E+00 0.0000000E+00 -529631.6     -146779.5     0.0000000E+00 400102\n" +
                        "  0.0000000E+00 0.0000000E+00 -5920432.     -2632801.     0.0000000E+00 310102\n" +
                        "  0.0000000E+00 0.0000000E+00-0.1792741E+08 -9004232.     0.0000000E+00 220102\n" +
                        "  0.0000000E+00 0.0000000E+00-0.2367118E+08-0.1256359E+08 0.0000000E+00 130102\n" +
                        "  0.0000000E+00 0.0000000E+00-0.1284547E+08 -7026112.     0.0000000E+00 040102\n" +
                        "  -513840.8     -574356.4     0.0000000E+00 0.0000000E+00  38665.38     301102\n" +
                        "   48452.56     -1056623.     0.0000000E+00 0.0000000E+00  208544.2     211102\n" +
                        "   3947633.      3482642.     0.0000000E+00 0.0000000E+00 -1505391.     121102\n" +
                        "   1913297.      4675178.     0.0000000E+00 0.0000000E+00 -2747160.     031102\n" +
                        "  0.0000000E+00 0.0000000E+00 -102359.9      609342.1     0.0000000E+00 202102\n" +
                        "  0.0000000E+00 0.0000000E+00 -7243311.     -2357176.     0.0000000E+00 112102\n" +
                        "  0.0000000E+00 0.0000000E+00 -8859771.     -4081749.     0.0000000E+00 022102\n" +
                        "  -844187.5     -169096.9     0.0000000E+00 0.0000000E+00  196663.0     103102\n" +
                        "  -2198657.     -934065.5     0.0000000E+00 0.0000000E+00  600062.6     013102\n" +
                        "  0.0000000E+00 0.0000000E+00  721135.2      471037.7     0.0000000E+00 004102\n" +
                        "  -129751.3     -108990.0     0.0000000E+00 0.0000000E+00 -2170.146     400003\n" +
                        "  -693146.5     -740277.0     0.0000000E+00 0.0000000E+00  89856.00     310003\n" +
                        "  -1097814.     -1472670.     0.0000000E+00 0.0000000E+00  302880.4     220003\n" +
                        "  -645317.0     -1099559.     0.0000000E+00 0.0000000E+00  221929.0     130003\n" +
                        "  -152133.0     -305995.9     0.0000000E+00 0.0000000E+00  431.3895     040003\n" +
                        "  0.0000000E+00 0.0000000E+00  107633.0      62161.96     0.0000000E+00 301003\n" +
                        "  0.0000000E+00 0.0000000E+00  440574.8      255135.1     0.0000000E+00 211003\n" +
                        "  0.0000000E+00 0.0000000E+00  1027705.      551356.8     0.0000000E+00 121003\n" +
                        "  0.0000000E+00 0.0000000E+00  994913.9      529673.2     0.0000000E+00 031003\n" +
                        "  -78051.38     -46057.67     0.0000000E+00 0.0000000E+00 -6854.799     202003\n" +
                        "  -221998.1     -261245.5     0.0000000E+00 0.0000000E+00  101897.4     112003\n" +
                        "   2834.587     -206971.4     0.0000000E+00 0.0000000E+00  175707.1     022003\n" +
                        "  0.0000000E+00 0.0000000E+00  87046.58      58440.90     0.0000000E+00 103003\n" +
                        "  0.0000000E+00 0.0000000E+00  27002.59      42735.02     0.0000000E+00 013003\n" +
                        "   4149.507      7689.161     0.0000000E+00 0.0000000E+00 -6282.236     004003\n" +
                        "  -7958919.     0.2932132E+08 0.0000000E+00 0.0000000E+00-0.2400002E+08 300400\n" +
                        " -0.6121186E+08 0.1376146E+09 0.0000000E+00 0.0000000E+00-0.1202430E+09 210400\n" +
                        " -0.1394153E+09 0.2192178E+09 0.0000000E+00 0.0000000E+00-0.1978706E+09 120400\n" +
                        " -0.1105678E+09 0.1129804E+09 0.0000000E+00 0.0000000E+00-0.1100960E+09 030400\n" +
                        "  0.0000000E+00 0.0000000E+00 0.6581745E+08 0.7425174E+08 0.0000000E+00 201400\n" +
                        "  0.0000000E+00 0.0000000E+00 0.2829711E+09 0.2660846E+09 0.0000000E+00 111400\n" +
                        "  0.0000000E+00 0.0000000E+00 0.3130287E+09 0.2612437E+09 0.0000000E+00 021400\n" +
                        "  0.2113922E+09 0.1138677E+09 0.0000000E+00 0.0000000E+00  2351614.     102400\n" +
                        "  0.4808384E+09 0.2745287E+09 0.0000000E+00 0.0000000E+00-0.3840887E+08 012400\n" +
                        "  0.0000000E+00 0.0000000E+00-0.8402101E+08-0.5926767E+08 0.0000000E+00 003400\n" +
                        "  0.0000000E+00 0.0000000E+00 0.2065470E+08 0.1212739E+08 0.0000000E+00 300301\n" +
                        "  0.0000000E+00 0.0000000E+00 0.1249305E+09 0.7113519E+08 0.0000000E+00 210301\n" +
                        "  0.0000000E+00 0.0000000E+00 0.2636368E+09 0.1498928E+09 0.0000000E+00 120301\n" +
                        "  0.0000000E+00 0.0000000E+00 0.1805547E+09 0.1036757E+09 0.0000000E+00 030301\n" +
                        "  0.3328732E+08 -780796.4     0.0000000E+00 0.0000000E+00 0.1377835E+08 201301\n" +
                        "  0.1553586E+09  2769983.     0.0000000E+00 0.0000000E+00 0.4995407E+08 111301\n" +
                        "  0.1778214E+09 0.1392753E+08 0.0000000E+00 0.0000000E+00 0.4862647E+08 021301\n" +
                        "  0.0000000E+00 0.0000000E+00-0.5926409E+08-0.2572466E+08 0.0000000E+00 102301\n" +
                        "  0.0000000E+00 0.0000000E+00-0.1570741E+09-0.8331740E+08 0.0000000E+00 012301\n" +
                        " -0.3989411E+08-0.1697071E+08 0.0000000E+00 0.0000000E+00  5521966.     003301\n" +
                        "  -28456.52      115776.1     0.0000000E+00 0.0000000E+00 -88779.59     300202\n" +
                        "   1405266.      1655589.     0.0000000E+00 0.0000000E+00 -739176.9     210202\n" +
                        "   4866894.      6607994.     0.0000000E+00 0.0000000E+00 -2972674.     120202\n" +
                        "   2027576.      5784703.     0.0000000E+00 0.0000000E+00 -3271180.     030202\n" +
                        "  0.0000000E+00 0.0000000E+00 -3348039.     -569876.4     0.0000000E+00 201202\n" +
                        "  0.0000000E+00 0.0000000E+00-0.2577356E+08-0.1118628E+08 0.0000000E+00 111202\n" +
                        "  0.0000000E+00 0.0000000E+00-0.2843412E+08-0.1426325E+08 0.0000000E+00 021202\n" +
                        "  -3462777.     -431800.8     0.0000000E+00 0.0000000E+00  516334.2     102202\n" +
                        "  -8667320.     -2434716.     0.0000000E+00 0.0000000E+00  893485.0     012202\n" +
                        "  0.0000000E+00 0.0000000E+00  2680253.      1676748.     0.0000000E+00 003202\n" +
                        "  0.0000000E+00 0.0000000E+00  238453.6      143766.2     0.0000000E+00 300103\n" +
                        "  0.0000000E+00 0.0000000E+00  950754.0      551598.9     0.0000000E+00 210103\n" +
                        "  0.0000000E+00 0.0000000E+00  2338762.      1230971.     0.0000000E+00 120103\n" +
                        "  0.0000000E+00 0.0000000E+00  2342210.      1234909.     0.0000000E+00 030103\n" +
                        "  -251684.6     -165088.2     0.0000000E+00 0.0000000E+00  34840.43     201103\n" +
                        "  -695523.9     -934319.6     0.0000000E+00 0.0000000E+00  533168.7     111103\n" +
                        "   129667.1     -768323.2     0.0000000E+00 0.0000000E+00  759046.7     021103\n" +
                        "  0.0000000E+00 0.0000000E+00  407883.3      297818.0     0.0000000E+00 102103\n" +
                        "  0.0000000E+00 0.0000000E+00 -7866.780      142951.7     0.0000000E+00 012103\n" +
                        "   6289.615      42373.60     0.0000000E+00 0.0000000E+00 -32023.29     003103\n" +
                        "  -14606.34     -2007.822     0.0000000E+00 0.0000000E+00 -5047.266     300004\n" +
                        "  -90470.83     -55538.21     0.0000000E+00 0.0000000E+00 -16.52709     210004\n" +
                        "  -115032.3     -103178.6     0.0000000E+00 0.0000000E+00  37836.23     120004\n" +
                        "  -37625.06     -33581.61     0.0000000E+00 0.0000000E+00  28125.05     030004\n" +
                        "  0.0000000E+00 0.0000000E+00  11651.89      6903.934     0.0000000E+00 201004\n" +
                        "  0.0000000E+00 0.0000000E+00 -26106.40     -6065.299     0.0000000E+00 111004\n" +
                        "  0.0000000E+00 0.0000000E+00 -86222.15     -41992.82     0.0000000E+00 021004\n" +
                        "  -3667.162      4551.902     0.0000000E+00 0.0000000E+00 -6507.715     102004\n" +
                        "  -23559.51     -4030.886     0.0000000E+00 0.0000000E+00 -8352.672     012004\n" +
                        "  0.0000000E+00 0.0000000E+00  7255.699      2810.422     0.0000000E+00 003004\n" +
                        "  0.0000000E+00 0.0000000E+00 0.1160502E+08 0.2071459E+08 0.0000000E+00 200500\n" +
                        "  0.0000000E+00 0.0000000E+00 0.5702664E+08 0.7300379E+08 0.0000000E+00 110500\n" +
                        "  0.0000000E+00 0.0000000E+00 0.6672198E+08 0.6929252E+08 0.0000000E+00 020500\n" +
                        "  0.1379067E+09 0.8303185E+08 0.0000000E+00 0.0000000E+00 -7521077.     101500\n" +
                        "  0.3237328E+09 0.2041677E+09 0.0000000E+00 0.0000000E+00-0.4544478E+08 011500\n" +
                        "  0.0000000E+00 0.0000000E+00-0.9658390E+08-0.7321656E+08 0.0000000E+00 002500\n" +
                        "  0.1815543E+08 -903572.5     0.0000000E+00 0.0000000E+00  7860492.     200401\n" +
                        "  0.8122479E+08 -1020699.     0.0000000E+00 0.0000000E+00 0.2794351E+08 110401\n" +
                        "  0.9100070E+08  3811552.     0.0000000E+00 0.0000000E+00 0.2714432E+08 020401\n" +
                        "  0.0000000E+00 0.0000000E+00-0.6060124E+08-0.2994153E+08 0.0000000E+00 101401\n" +
                        "  0.0000000E+00 0.0000000E+00-0.1499730E+09-0.8397190E+08 0.0000000E+00 011401\n" +
                        " -0.6591343E+08-0.3052885E+08 0.0000000E+00 0.0000000E+00  9187297.     002401\n" +
                        "  0.0000000E+00 0.0000000E+00 -4469964.     -1714544.     0.0000000E+00 200302\n" +
                        "  0.0000000E+00 0.0000000E+00-0.2513451E+08-0.1210424E+08 0.0000000E+00 110302\n" +
                        "  0.0000000E+00 0.0000000E+00-0.2651838E+08-0.1383691E+08 0.0000000E+00 020302\n" +
                        "  -6646773.     -823311.8     0.0000000E+00 0.0000000E+00  414519.3     101302\n" +
                        " -0.1567064E+08 -3331311.     0.0000000E+00 0.0000000E+00 -273216.8     011302\n" +
                        "  0.0000000E+00 0.0000000E+00  5385880.      3179313.     0.0000000E+00 002302\n" +
                        "  -218762.4     -144375.0     0.0000000E+00 0.0000000E+00  89244.11     200203\n" +
                        "  -580158.7     -842778.6     0.0000000E+00 0.0000000E+00  624180.6     110203\n" +
                        "   249403.5     -715754.9     0.0000000E+00 0.0000000E+00  793916.5     020203\n" +
                        "  0.0000000E+00 0.0000000E+00  685889.0      500739.8     0.0000000E+00 101203\n" +
                        "  0.0000000E+00 0.0000000E+00  2298.509      203764.6     0.0000000E+00 011203\n" +
                        "  -51275.59      63785.06     0.0000000E+00 0.0000000E+00 -40456.95     002203\n" +
                        "  0.0000000E+00 0.0000000E+00  16857.99      13708.93     0.0000000E+00 200104\n" +
                        "  0.0000000E+00 0.0000000E+00 -136347.6     -49417.79     0.0000000E+00 110104\n" +
                        "  0.0000000E+00 0.0000000E+00 -278677.9     -140080.5     0.0000000E+00 020104\n" +
                        "  -18328.98      14734.13     0.0000000E+00 0.0000000E+00 -20663.29     101104\n" +
                        "  -97768.95     -14120.98     0.0000000E+00 0.0000000E+00 -28438.42     011104\n" +
                        "  0.0000000E+00 0.0000000E+00  47120.85      20819.19     0.0000000E+00 002104\n" +
                        "  -305.7858      1475.982     0.0000000E+00 0.0000000E+00 -1221.960     200005\n" +
                        "  -5504.807     -1184.115     0.0000000E+00 0.0000000E+00 -3011.978     110005\n" +
                        "  -2358.295     -4582.917     0.0000000E+00 0.0000000E+00  244.2397     020005\n" +
                        "  0.0000000E+00 0.0000000E+00  3608.899      1486.766     0.0000000E+00 101005\n" +
                        "  0.0000000E+00 0.0000000E+00  7380.075      4034.468     0.0000000E+00 011005\n" +
                        "   585.7068      452.2891     0.0000000E+00 0.0000000E+00 -171.9747     002005\n" +
                        "  0.3541498E+08 0.2456207E+08 0.0000000E+00 0.0000000E+00 -5643901.     100600\n" +
                        "  0.8863887E+08 0.6290050E+08 0.0000000E+00 0.0000000E+00-0.1998828E+08 010600\n" +
                        "  0.0000000E+00 0.0000000E+00-0.6432120E+08-0.5161070E+08 0.0000000E+00 001600\n" +
                        "  0.0000000E+00 0.0000000E+00-0.2350149E+08-0.1296143E+08 0.0000000E+00 100501\n" +
                        "  0.0000000E+00 0.0000000E+00-0.5449371E+08-0.3230401E+08 0.0000000E+00 010501\n" +
                        " -0.5626258E+08-0.2812370E+08 0.0000000E+00 0.0000000E+00  8279583.     001501\n" +
                        "  -4729155.     -731136.4     0.0000000E+00 0.0000000E+00  31815.56     100402\n" +
                        " -0.1049854E+08 -1984746.     0.0000000E+00 0.0000000E+00 -940179.4     010402\n" +
                        "  0.0000000E+00 0.0000000E+00  5651157.      3160994.     0.0000000E+00 001402\n" +
                        "  0.0000000E+00 0.0000000E+00  523178.3      329218.3     0.0000000E+00 100303\n" +
                        "  0.0000000E+00 0.0000000E+00  364974.8      254600.6     0.0000000E+00 010303\n" +
                        "  -116212.1     -2062.548     0.0000000E+00 0.0000000E+00  31472.73     001303\n" +
                        "  -26100.81      12281.76     0.0000000E+00 0.0000000E+00 -16555.14     100204\n" +
                        "  -111724.6     -10959.53     0.0000000E+00 0.0000000E+00 -30247.51     010204\n" +
                        "  0.0000000E+00 0.0000000E+00  90965.62      43289.14     0.0000000E+00 001204\n" +
                        "  0.0000000E+00 0.0000000E+00  11162.57      5335.737     0.0000000E+00 100105\n" +
                        "  0.0000000E+00 0.0000000E+00  18648.80      11147.30     0.0000000E+00 010105\n" +
                        "   2283.358      1554.248     0.0000000E+00 0.0000000E+00 -457.3686     001105\n" +
                        "   37.38939      196.0002     0.0000000E+00 0.0000000E+00 -65.36556     100006\n" +
                        "  -454.9581      171.6253     0.0000000E+00 0.0000000E+00 -258.8341     010006\n" +
                        "  0.0000000E+00 0.0000000E+00 -17.98347     -51.07227     0.0000000E+00 001006\n" +
                        "  0.0000000E+00 0.0000000E+00-0.1910319E+08-0.1594085E+08 0.0000000E+00 000700\n" +
                        " -0.1939387E+08-0.1040129E+08 0.0000000E+00 0.0000000E+00  3137494.     000601\n" +
                        "  0.0000000E+00 0.0000000E+00  2367911.      1271556.     0.0000000E+00 000502\n" +
                        "  -37704.18     -41265.37     0.0000000E+00 0.0000000E+00  72200.73     000403\n" +
                        "  0.0000000E+00 0.0000000E+00  51991.76      25991.09     0.0000000E+00 000304\n" +
                        "   2224.663      1188.448     0.0000000E+00 0.0000000E+00  143.2728     000205\n" +
                        "  0.0000000E+00 0.0000000E+00  107.2047     -52.63014     0.0000000E+00 000106\n" +
                        "   13.67965      4.953325     0.0000000E+00 0.0000000E+00  5.178695     000007"
        );
    }


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


    // ------------------   调整 初始绕线 ------------

    private Cct createDipoleCct() {
        return CctFactory.createDipoleCct(
                dipoleCctSmallRInner, dipoleCctSmallROuter, dipoleCctBigR,
                dipoleCctAngle, dipoleCctWindingNumber,
                dipoleCctA0Inner, dipoleCctA1Inner, dipoleCctA2Inner, dipoleCctIInner,
                dipoleCctA0Outer, dipoleCctA1Outer, dipoleCctA2Outer, dipoleCctIOuter,
                numberPerWinding,
                dipoleCctStartingθInner, dipoleCctStartingθOuter,
                dipoleCctStartingφInner, dipoleCctStartingφOuter, dipoleCctDirectθ
        );
    }

    private Cct createAgCct() {
        return CctFactory.createAgCct(agCctSmallRInner, agCctSmallROuter, agCCTBigR,
                new double[]{agCctAngle0, agCctAngle1},
                new int[]{agCctWindingNumber0, agCctWindingNumber1},
                agCctA0Inner, agCctA1Inner, agCctA2Inner, agCctIInner,
                agCctA0Outer, agCctA1Outer, agCctA2Outer, agCctIOuter,
                numberPerWinding,
                agCctStartingθInner, agCctStartingθOuter,
                agCctStartingφInner, agCctStartingφOuter, agCctDirectθ,agCctDirectθ
        );
    }

    //---------------------elements 初始绕线位置不可调整------------------------------

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

        Cct cct = getCct();

        return CctFactory.positionInXYPlane(cct, Point2.create(DL1, trajectoryBigR), BaseUtils.Converter.angleToRadian(-90));

    }

    private Cct getCct2() {
        Trajectory trajectory = getTrajectory();

        Cct cct1 = getCct1();

        // 关于面对称

        return CctFactory.symmetryInXYPlaneByLine(cct1, trajectory.midPoint(),
                trajectory.directAt(trajectory.getLength() / 2).rotateSelf(BaseUtils.Converter.angleToRadian(90)));

//        CctFactory.Cct dipoleCct = createDipoleCct();
//
//        //        return CctFactory.createAgCct(agCctSmallRInner, agCctSmallROuter, agCCTBigR,
//        //                new double[]{agCctAngle0, agCctAngle1},
//        //                new int[]{agCctWindingNumber0, agCctWindingNumber1},
//        //                agCctA0Inner, agCctA1Inner, agCctA2Inner, agCctIInner,
//        //                agCctA0Outer, agCctA1Outer, agCctA2Outer, agCctIOuter,
//        //                numberPerWinding,
//        //                agCctStartingθInner, agCctStartingθOuter,
//        //                agCctStartingφInner, agCctStartingφOuter, agCctDirectθ
//        //        );
//
//        CctFactory.Cct agCct = CctFactory.createAgCct(agCctSmallRInner, agCctSmallROuter, agCCTBigR,
//                new double[]{agCctAngle0, agCctAngle1},
//                new int[]{agCctWindingNumber0, agCctWindingNumber1},
//                agCctA0Inner, agCctA1Inner, agCctA2Inner, agCctIInner,
//                agCctA0Outer, agCctA1Outer, agCctA2Outer, agCctIOuter,
//                numberPerWinding,
//                agCctStartingθInner, agCctStartingθOuter,
//                agCctStartingφInner, agCctStartingφOuter, !agCctDirectθ
//        );
//
//
//        Point2 center = Arcs.center(
//                trajectory.pointAt(DL1 + CCT_LENGTH + BETWEEN_CCT225),
//                trajectory.pointAt(DL1 + CCT_LENGTH + BETWEEN_CCT225 + CCT_LENGTH / 2),
//                trajectory.pointAt(DL1 + CCT_LENGTH + BETWEEN_CCT225 + CCT_LENGTH)
//        );
//
//
//        dipoleCct = CctFactory.positionInXYPlane(dipoleCct, center, BaseUtils.Converter.angleToRadian(-90 + 22.5));
//
//        agCct = CctFactory.positionInXYPlane(agCct, center, BaseUtils.Converter.angleToRadian(0.0));
//
//        return CctFactory.combineCct(agCct, dipoleCct);
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
