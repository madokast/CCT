package cn.edu.hust.zrx.cct.study.X旧机架研究.前45偏转段2;

import cn.edu.hust.zrx.cct.Logger;
import cn.edu.hust.zrx.cct.advanced.CosyArbitraryOrder;
import cn.edu.hust.zrx.cct.base.BaseUtils;
import cn.edu.hust.zrx.cct.base.cct.Cct;
import cn.edu.hust.zrx.cct.base.cct.CctFactory;
import cn.edu.hust.zrx.cct.base.cct.Elements;
import cn.edu.hust.zrx.cct.base.cct.MagnetAble;
import cn.edu.hust.zrx.cct.base.line.Arcs;
import cn.edu.hust.zrx.cct.base.line.Trajectory;
import cn.edu.hust.zrx.cct.base.line.TrajectoryFactory;
import cn.edu.hust.zrx.cct.base.opticsMagnet.qs.QsHardPlaneMagnet;
import cn.edu.hust.zrx.cct.base.particle.*;
import cn.edu.hust.zrx.cct.base.point.Point2;
import cn.edu.hust.zrx.cct.base.python.Plot2d;
import cn.edu.hust.zrx.cct.base.vector.Vector2;

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
import java.util.stream.Collectors;

/**
 * Description
 * C20200521六极场改变
 * <p>
 * Data
 * 2:54
 *
 * @author zrx
 * @version 1.0
 */

public class C20200521六极场改变 {


    @run(10)
    public void 美味的相椭圆(){
        Elements elementsOfAll = getElementsOfAll();
        Trajectory trajectory = getTrajectory();

        CosyArbitraryOrder.CosyMapArbitraryOrder cosyMap更新六极场后 = cosyMap更新六极场后();

        phase相椭圆画图(trajectory.getLength(),false,5*PRESENT,16,false,
                1,elementsOfAll,trajectory,512,5,
                List.of(BaseUtils.Content.BiContent.create("COSY 5th",cosyMap更新六极场后)));
    }

    @run(2)
    public void 切片(){
        Elements elementsOfAll = getElementsOfAll();

        Trajectory trajectory = getTrajectory();

        List<String> list = elementsOfAll.sliceToCosyScript(Bp, 30 * MM, trajectory, 5 * MM, MM, 0.5);

        list.forEach(System.out::println);
    }


    @run(3)
    public void 单粒子跟踪(){
        Elements elementsOfAll = getElementsOfAll();
        Trajectory trajectory = getTrajectory();

        //    private double QS1_GRADIENT = 53.3; //T m-1
        //    private double QS2_GRADIENT = -37.7; //T m-1
        //    private double QS3_GRADIENT = 7.6; //T m-1
        //    private double QS1_SECOND_GRADIENT = -213.78; //T m-2
        //    private double QS2_SECOND_GRADIENT = 673.56; //T m-2


        //    private double QS1_GRADIENT = 53.3; //T m-1
        //    private double QS1_SECOND_GRADIENT = -213.78; //T m-2
        //    private double QS2_GRADIENT = -37.7; //T m-1
        //    private double QS2_SECOND_GRADIENT = 673.56; //T m-2
        //    private double QS1_APERTURE_MM = 30;
        //    private double QS2_APERTURE_MM = 30;

        List<Point2> x = trackingIdealParticle(trajectory, trajectory.getLength(), elementsOfAll, true);
        Plot2d.plot2(x);


//        List<Point2> y = trackingIdealParticle(trajectory, trajectory.getLength(), elementsOfAll, false);
//        Plot2d.plot2(y);

        Plot2d.showThread();
    }

    private List<Point2> trackingIdealParticle(
            Trajectory trajectory, double distance, MagnetAble magnetAble, boolean xPlane) {
        RunningParticle ip = ParticleFactory.createIdealProtonAtTrajectory250MeV(trajectory);

        return ParticleRunner.runGetPoint3WithDistance(ip, magnetAble, distance, MM)
                .stream().map(point3WithDistance -> {

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


    private void phase相椭圆画图(
            double distance, boolean xPlane, double delta, int numberParticleForTrack,
            boolean moveToCenter, double scaleForParticle,
            MagnetAble magnetAble, Trajectory trajectory,
            int numberParticleForCosyMap, int cosyOrder,
            List<BaseUtils.Content.BiContent<String,CosyArbitraryOrder.CosyMapArbitraryOrder>> mapInfo
    ) {
        CosyArbitraryOrder.CosyMapArbitraryOrder[] maps = mapInfo.stream().map(BaseUtils.Content.
                BiContent::getT2).collect(Collectors.toList()).toArray(CosyArbitraryOrder.CosyMapArbitraryOrder[]::new);

        List<List<Point2>> lists = phase相椭圆研究(distance, xPlane, delta, numberParticleForTrack, moveToCenter,
                scaleForParticle, magnetAble, trajectory, numberParticleForCosyMap, cosyOrder, maps);

        List<Point2> list = lists.get(0);
        Plot2d.plot2(list,Plot2d.BLACK_POINT);

        for (int i = 0; i < lists.size(); i++) {
            List<Point2> list1 = lists.get(i);
            Plot2d.plot2(list1,switcher.getAndSwitch());
        }

        if(xPlane)
            Plot2d.info("x/mm","xp/mrad","x/xp dp="+delta/PRESENT,18);
        else
            Plot2d.info("y/mm","yp/mrad","y/yp dp="+delta/PRESENT,18);

        List<String> info = mapInfo.stream().map(BaseUtils.Content.BiContent::getT1).collect(Collectors.toList());
        info.add(0,"Track");

        Plot2d.legend(18,info.toArray(String[]::new));

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

        ParticleRunner.runThread(ps, magnetAble, distance, MM * 0.1);
        ParticleRunner.run(ip, magnetAble, distance, MM * 0.1);

        List<PhaseSpaceParticle> ppEnd = PhaseSpaceParticles.phaseSpaceParticlesFromRunningParticles(ip, ip.computeNaturalCoordinateSystem(), ps);

        List<Point2> projectionToPlaneCOSY = PhaseSpaceParticles.projectionToPlane(xPlane, ppEnd);

        if (moveToCenter) {
            Vector2 average = Point2.average(projectionToPlaneCOSY).toVector2().reverseSelf();

            projectionToPlaneCOSY.forEach(point2 -> point2.moveSelf(average));
        }

        // 改单位
        return Point2.convert(projectionToPlaneCOSY, 1 / MM, 1 / MRAD);
    }

    // ---------------- map ---------------------

    private CosyArbitraryOrder.CosyMapArbitraryOrder cosyMap更新六极场后(){
        return CosyArbitraryOrder.readMap(
                "  0.9964840      1.427547     0.0000000E+00 0.0000000E+00 0.8509655E-02 100000\n" +
                        " -0.4917276E-02 0.9964840     0.0000000E+00 0.0000000E+00-0.2095900E-04 010000\n" +
                        "  0.0000000E+00 0.0000000E+00-0.9950191    -0.8864910     0.0000000E+00 001000\n" +
                        "  0.0000000E+00 0.0000000E+00 0.1120935E-01-0.9950191     0.0000000E+00 000100\n" +
                        "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00  1.000000     000010\n" +
                        "  0.2095900E-04-0.8509655E-02 0.0000000E+00 0.0000000E+00 0.8032067     000001\n" +
                        "   18.83240      6.331251     0.0000000E+00 0.0000000E+00-0.4456768     200000\n" +
                        "   26.31336    -0.3118733E-01 0.0000000E+00 0.0000000E+00 0.3013971     110000\n" +
                        " -0.3240448E-01 -13.20303     0.0000000E+00 0.0000000E+00 0.1301480     020000\n" +
                        "  0.0000000E+00 0.0000000E+00 -47.27325     -17.80921     0.0000000E+00 101000\n" +
                        "  0.0000000E+00 0.0000000E+00 -11.93804     0.4386352E-01 0.0000000E+00 011000\n" +
                        "   5.354092     -4.465888     0.0000000E+00 0.0000000E+00 0.7539683     002000\n" +
                        "  0.0000000E+00 0.0000000E+00 -87.03144     -30.06492     0.0000000E+00 100100\n" +
                        "  0.0000000E+00 0.0000000E+00 0.2143556      12.12853     0.0000000E+00 010100\n" +
                        "   12.06861     -30.11480     0.0000000E+00 0.0000000E+00  5.686917     001100\n" +
                        "  0.7124728E-01  1.318478     0.0000000E+00 0.0000000E+00-0.2105623     100001\n" +
                        "  0.2608628     0.2959543     0.0000000E+00 0.0000000E+00 0.5186081E-03 010001\n" +
                        "  0.0000000E+00 0.0000000E+00 -5.572915     -3.540976     0.0000000E+00 001001\n" +
                        "  0.3973511E-01 -43.56392     0.0000000E+00 0.0000000E+00  6.334811     000200\n" +
                        "  0.0000000E+00 0.0000000E+00 -12.67026     -5.675494     0.0000000E+00 000101\n" +
                        " -0.1369977E-02 0.1040081     0.0000000E+00 0.0000000E+00-0.7095005     000002\n" +
                        "  -85.18509     -87.93142     0.0000000E+00 0.0000000E+00 -6.764007     300000\n" +
                        "  -781.0224     -526.4756     0.0000000E+00 0.0000000E+00 -9.841772     210000\n" +
                        "  -1490.506     -853.0228     0.0000000E+00 0.0000000E+00 -1.767337     120000\n" +
                        "  -775.6403     -380.5027     0.0000000E+00 0.0000000E+00 -5.043569     030000\n" +
                        "  0.0000000E+00 0.0000000E+00 -59.46117     -80.58674     0.0000000E+00 201000\n" +
                        "  0.0000000E+00 0.0000000E+00  579.6886      153.9762     0.0000000E+00 111000\n" +
                        "  0.0000000E+00 0.0000000E+00  744.5257      271.9173     0.0000000E+00 021000\n" +
                        "  -190.9712     -9.176141     0.0000000E+00 0.0000000E+00 -31.49387     102000\n" +
                        "  -248.9030      49.88027     0.0000000E+00 0.0000000E+00 -32.85226     012000\n" +
                        "  0.0000000E+00 0.0000000E+00 -322.9085     -149.0415     0.0000000E+00 003000\n" +
                        "  0.0000000E+00 0.0000000E+00 -125.8473     -181.0911     0.0000000E+00 200100\n" +
                        "  0.0000000E+00 0.0000000E+00  1225.935      302.9590     0.0000000E+00 110100\n" +
                        "  0.0000000E+00 0.0000000E+00  1660.252      586.0520     0.0000000E+00 020100\n" +
                        "  -1010.666     -147.6474     0.0000000E+00 0.0000000E+00 -62.63279     101100\n" +
                        "  -1316.822      60.49662     0.0000000E+00 0.0000000E+00 -99.51641     011100\n" +
                        "  0.0000000E+00 0.0000000E+00 -1871.931     -909.7498     0.0000000E+00 002100\n" +
                        "   31.55121      14.44538     0.0000000E+00 0.0000000E+00 0.2624962     200001\n" +
                        "   31.52732      8.867219     0.0000000E+00 0.0000000E+00 0.5861139     110001\n" +
                        "   11.61513     -2.156366     0.0000000E+00 0.0000000E+00  2.357478     020001\n" +
                        "  0.0000000E+00 0.0000000E+00 -104.3938     -69.72959     0.0000000E+00 101001\n" +
                        "  0.0000000E+00 0.0000000E+00  10.15292     -4.476409     0.0000000E+00 011001\n" +
                        "   11.92833     -21.32234     0.0000000E+00 0.0000000E+00  3.628027     002001\n" +
                        "  -1236.474     -251.9312     0.0000000E+00 0.0000000E+00 0.8688341     100200\n" +
                        "  -1660.085     -87.67372     0.0000000E+00 0.0000000E+00 -89.02235     010200\n" +
                        "  0.0000000E+00 0.0000000E+00 -3749.493     -1926.820     0.0000000E+00 001200\n" +
                        "  0.0000000E+00 0.0000000E+00 -141.4876     -119.3417     0.0000000E+00 100101\n" +
                        "  0.0000000E+00 0.0000000E+00 -25.19671     -31.06572     0.0000000E+00 010101\n" +
                        "   47.30121     -61.94243     0.0000000E+00 0.0000000E+00  12.79034     001101\n" +
                        "   3.065577     0.4493407     0.0000000E+00 0.0000000E+00 0.3329000     100002\n" +
                        "   2.221420     0.4385717     0.0000000E+00 0.0000000E+00-0.2463673E-01 010002\n" +
                        "  0.0000000E+00 0.0000000E+00 -14.00858     -7.829004     0.0000000E+00 001002\n" +
                        "  0.0000000E+00 0.0000000E+00 -2616.329     -1425.353     0.0000000E+00 000300\n" +
                        "   77.90033     -13.25229     0.0000000E+00 0.0000000E+00  15.83368     000201\n" +
                        "  0.0000000E+00 0.0000000E+00 -14.99380     -12.56393     0.0000000E+00 000102\n" +
                        "  0.4361296E-02-0.1025468     0.0000000E+00 0.0000000E+00 0.6438668     000003\n" +
                        "  -988.5611     -144.8476     0.0000000E+00 0.0000000E+00 -33.75032     400000\n" +
                        "  -5103.995     -353.6271     0.0000000E+00 0.0000000E+00  239.8718     310000\n" +
                        "  -5968.721      2069.318     0.0000000E+00 0.0000000E+00  831.2919     220000\n" +
                        "   2861.422      6346.577     0.0000000E+00 0.0000000E+00  684.3052     130000\n" +
                        "   5133.201      4324.043     0.0000000E+00 0.0000000E+00  102.5290     040000\n" +
                        "  0.0000000E+00 0.0000000E+00  3190.964      1810.375     0.0000000E+00 301000\n" +
                        "  0.0000000E+00 0.0000000E+00  15529.47      8973.302     0.0000000E+00 211000\n" +
                        "  0.0000000E+00 0.0000000E+00  19436.90      11871.05     0.0000000E+00 121000\n" +
                        "  0.0000000E+00 0.0000000E+00  4101.437      3323.460     0.0000000E+00 031000\n" +
                        "   2599.453      2008.111     0.0000000E+00 0.0000000E+00 -688.3128     202000\n" +
                        "   12897.72      6842.382     0.0000000E+00 0.0000000E+00 -107.6773     112000\n" +
                        "   11088.59      4893.396     0.0000000E+00 0.0000000E+00  363.3973     022000\n" +
                        "  0.0000000E+00 0.0000000E+00  2406.074      1327.030     0.0000000E+00 103000\n" +
                        "  0.0000000E+00 0.0000000E+00  2029.023      1214.051     0.0000000E+00 013000\n" +
                        "   1619.949      1097.355     0.0000000E+00 0.0000000E+00 -185.8557     004000\n" +
                        "  0.0000000E+00 0.0000000E+00  6462.412      3645.303     0.0000000E+00 300100\n" +
                        "  0.0000000E+00 0.0000000E+00  33252.74      18967.08     0.0000000E+00 210100\n" +
                        "  0.0000000E+00 0.0000000E+00  44196.00      26308.94     0.0000000E+00 120100\n" +
                        "  0.0000000E+00 0.0000000E+00  11884.18      8361.178     0.0000000E+00 030100\n" +
                        "   8536.290      7815.692     0.0000000E+00 0.0000000E+00 -2584.790     201100\n" +
                        "   52219.82      28892.92     0.0000000E+00 0.0000000E+00 -411.3454     111100\n" +
                        "   47654.11      21986.08     0.0000000E+00 0.0000000E+00  790.9720     021100\n" +
                        "  0.0000000E+00 0.0000000E+00  20113.09      11656.74     0.0000000E+00 102100\n" +
                        "  0.0000000E+00 0.0000000E+00  16056.97      10150.32     0.0000000E+00 012100\n" +
                        "   15812.86      9484.888     0.0000000E+00 0.0000000E+00 -1002.772     003100\n" +
                        "  -328.0471     -194.0607     0.0000000E+00 0.0000000E+00 -28.00217     300001\n" +
                        "  -1072.351     -707.7945     0.0000000E+00 0.0000000E+00 -26.04961     210001\n" +
                        "  -668.0702     -633.9828     0.0000000E+00 0.0000000E+00  29.52012     120001\n" +
                        "   152.3520     -167.9445     0.0000000E+00 0.0000000E+00  50.92893     030001\n" +
                        "  0.0000000E+00 0.0000000E+00  489.4265      146.4883     0.0000000E+00 201001\n" +
                        "  0.0000000E+00 0.0000000E+00  2504.014      1431.484     0.0000000E+00 111001\n" +
                        "  0.0000000E+00 0.0000000E+00  2428.599      1537.780     0.0000000E+00 021001\n" +
                        "  -1470.579     -434.6847     0.0000000E+00 0.0000000E+00 -227.8163     102001\n" +
                        "  -1424.441     -400.3356     0.0000000E+00 0.0000000E+00 -82.77755     012001\n" +
                        "  0.0000000E+00 0.0000000E+00 -520.4567     -445.5917     0.0000000E+00 003001\n" +
                        "   6212.214      7277.568     0.0000000E+00 0.0000000E+00 -2436.561     200200\n" +
                        "   51794.51      29689.19     0.0000000E+00 0.0000000E+00 -525.6590     110200\n" +
                        "   49769.82      23803.28     0.0000000E+00 0.0000000E+00 -261.0261     020200\n" +
                        "  0.0000000E+00 0.0000000E+00  54074.46      32265.81     0.0000000E+00 101200\n" +
                        "  0.0000000E+00 0.0000000E+00  40058.78      26125.37     0.0000000E+00 011200\n" +
                        "   56727.15      30972.37     0.0000000E+00 0.0000000E+00 -1832.088     002200\n" +
                        "  0.0000000E+00 0.0000000E+00  901.3880      384.1680     0.0000000E+00 200101\n" +
                        "  0.0000000E+00 0.0000000E+00  3624.208      2536.139     0.0000000E+00 110101\n" +
                        "  0.0000000E+00 0.0000000E+00  2979.791      2549.190     0.0000000E+00 020101\n" +
                        "  -5545.008     -1910.895     0.0000000E+00 0.0000000E+00 -900.7574     101101\n" +
                        "  -4928.354     -2020.139     0.0000000E+00 0.0000000E+00 -266.2448     011101\n" +
                        "  0.0000000E+00 0.0000000E+00 -1245.640     -1714.489     0.0000000E+00 002101\n" +
                        "  -60.91843     -20.67725     0.0000000E+00 0.0000000E+00 -5.332660     200002\n" +
                        "  -169.4354     -67.17469     0.0000000E+00 0.0000000E+00 -7.625248     110002\n" +
                        "  -108.7616     -24.14741     0.0000000E+00 0.0000000E+00 -9.226852     020002\n" +
                        "  0.0000000E+00 0.0000000E+00  77.85440     -16.04619     0.0000000E+00 101002\n" +
                        "  0.0000000E+00 0.0000000E+00 -21.65518     -13.84549     0.0000000E+00 011002\n" +
                        "  -2.176781      21.96444     0.0000000E+00 0.0000000E+00 -20.47603     002002\n" +
                        "  0.0000000E+00 0.0000000E+00  46616.28      28380.30     0.0000000E+00 100300\n" +
                        "  0.0000000E+00 0.0000000E+00  31430.62      20901.01     0.0000000E+00 010300\n" +
                        "   89138.85      45252.66     0.0000000E+00 0.0000000E+00 -1160.639     001300\n" +
                        "  -4932.408     -1943.266     0.0000000E+00 0.0000000E+00 -1015.479     100201\n" +
                        "  -3477.814     -2185.828     0.0000000E+00 0.0000000E+00 -114.9853     010201\n" +
                        "  0.0000000E+00 0.0000000E+00  1782.373     -1091.937     0.0000000E+00 001201\n" +
                        "  0.0000000E+00 0.0000000E+00  218.3612      56.34811     0.0000000E+00 100102\n" +
                        "  0.0000000E+00 0.0000000E+00  31.79258      1.882543     0.0000000E+00 010102\n" +
                        "  -45.19577      141.8831     0.0000000E+00 0.0000000E+00 -84.43107     001102\n" +
                        "  -2.138041      1.013061     0.0000000E+00 0.0000000E+00-0.7583419     100003\n" +
                        "  -3.993064     -1.472329     0.0000000E+00 0.0000000E+00 0.8574304E-01 010003\n" +
                        "  0.0000000E+00 0.0000000E+00  18.49181      1.325671     0.0000000E+00 001003\n" +
                        "   51914.70      24892.82     0.0000000E+00 0.0000000E+00 -37.79529     000400\n" +
                        "  0.0000000E+00 0.0000000E+00  4774.446      1372.138     0.0000000E+00 000301\n" +
                        "  -176.1053      128.4307     0.0000000E+00 0.0000000E+00 -116.0002     000202\n" +
                        "  0.0000000E+00 0.0000000E+00  40.52426      14.81875     0.0000000E+00 000103\n" +
                        "  0.1241194     0.2130398     0.0000000E+00 0.0000000E+00-0.6236291     000004\n" +
                        "   3212.909      3667.409     0.0000000E+00 0.0000000E+00  962.0259     500000\n" +
                        "   18458.56      15111.86     0.0000000E+00 0.0000000E+00  8059.990     410000\n" +
                        "   76156.54      31912.92     0.0000000E+00 0.0000000E+00  17805.42     320000\n" +
                        "   160425.9      41407.52     0.0000000E+00 0.0000000E+00  10325.06     230000\n" +
                        "   136591.5      21513.83     0.0000000E+00 0.0000000E+00 -5022.621     140000\n" +
                        "   31570.82     -1604.579     0.0000000E+00 0.0000000E+00 -4555.199     050000\n" +
                        "  0.0000000E+00 0.0000000E+00  10892.91      7563.191     0.0000000E+00 401000\n" +
                        "  0.0000000E+00 0.0000000E+00  13901.86      10160.26     0.0000000E+00 311000\n" +
                        "  0.0000000E+00 0.0000000E+00 -114717.1     -64490.31     0.0000000E+00 221000\n" +
                        "  0.0000000E+00 0.0000000E+00 -277512.6     -157723.7     0.0000000E+00 131000\n" +
                        "  0.0000000E+00 0.0000000E+00 -159471.9     -90687.69     0.0000000E+00 041000\n" +
                        "   27385.68     -849.2452     0.0000000E+00 0.0000000E+00  3565.680     302000\n" +
                        "   20697.55     -48752.55     0.0000000E+00 0.0000000E+00  25194.95     212000\n" +
                        "  -131264.9     -143329.3     0.0000000E+00 0.0000000E+00  32246.42     122000\n" +
                        "  -149890.9     -105310.7     0.0000000E+00 0.0000000E+00  7009.098     032000\n" +
                        "  0.0000000E+00 0.0000000E+00 -52909.43     -29372.59     0.0000000E+00 203000\n" +
                        "  0.0000000E+00 0.0000000E+00 -147851.8     -85050.25     0.0000000E+00 113000\n" +
                        "  0.0000000E+00 0.0000000E+00 -77952.93     -46418.97     0.0000000E+00 023000\n" +
                        "  -34847.89     -21438.37     0.0000000E+00 0.0000000E+00 -7608.121     104000\n" +
                        "  -63620.69     -39009.09     0.0000000E+00 0.0000000E+00 -1365.116     014000\n" +
                        "  0.0000000E+00 0.0000000E+00  17003.45      10144.18     0.0000000E+00 005000\n" +
                        "  0.0000000E+00 0.0000000E+00  25660.28      16923.32     0.0000000E+00 400100\n" +
                        "  0.0000000E+00 0.0000000E+00  55005.69      34316.41     0.0000000E+00 310100\n" +
                        "  0.0000000E+00 0.0000000E+00 -164769.3     -95767.69     0.0000000E+00 220100\n" +
                        "  0.0000000E+00 0.0000000E+00 -495736.3     -287197.4     0.0000000E+00 130100\n" +
                        "  0.0000000E+00 0.0000000E+00 -301613.3     -174980.8     0.0000000E+00 040100\n" +
                        "   118257.3      140.4126     0.0000000E+00 0.0000000E+00  14586.82     301100\n" +
                        "   144590.1     -177456.5     0.0000000E+00 0.0000000E+00  92176.14     211100\n" +
                        "  -416636.0     -549237.0     0.0000000E+00 0.0000000E+00  109024.0     121100\n" +
                        "  -558717.1     -414421.2     0.0000000E+00 0.0000000E+00  16064.09     031100\n" +
                        "  0.0000000E+00 0.0000000E+00 -368260.7     -216856.9     0.0000000E+00 202100\n" +
                        "  0.0000000E+00 0.0000000E+00 -1029331.     -608165.9     0.0000000E+00 112100\n" +
                        "  0.0000000E+00 0.0000000E+00 -586992.1     -354066.7     0.0000000E+00 022100\n" +
                        "  -320392.6     -191688.9     0.0000000E+00 0.0000000E+00 -59691.23     103100\n" +
                        "  -596776.9     -352563.1     0.0000000E+00 0.0000000E+00 -8630.652     013100\n" +
                        "  0.0000000E+00 0.0000000E+00  162182.6      98102.05     0.0000000E+00 004100\n" +
                        "   1410.287      1040.099     0.0000000E+00 0.0000000E+00  217.1481     400001\n" +
                        "   10001.08      5306.049     0.0000000E+00 0.0000000E+00  1413.117     310001\n" +
                        "   24846.05      10056.74     0.0000000E+00 0.0000000E+00  2620.272     220001\n" +
                        "   18295.68      5192.586     0.0000000E+00 0.0000000E+00  1882.068     130001\n" +
                        "  -234.6264     -2055.627     0.0000000E+00 0.0000000E+00  640.2002     040001\n" +
                        "  0.0000000E+00 0.0000000E+00  8577.410      7242.039     0.0000000E+00 301001\n" +
                        "  0.0000000E+00 0.0000000E+00  26415.98      23706.49     0.0000000E+00 211001\n" +
                        "  0.0000000E+00 0.0000000E+00  19479.09      20431.70     0.0000000E+00 121001\n" +
                        "  0.0000000E+00 0.0000000E+00  4085.012      3138.246     0.0000000E+00 031001\n" +
                        "   16731.05      9347.293     0.0000000E+00 0.0000000E+00  311.4718     202001\n" +
                        "   54348.37      29923.68     0.0000000E+00 0.0000000E+00  5619.168     112001\n" +
                        "   35841.73      20214.08     0.0000000E+00 0.0000000E+00  4153.697     022001\n" +
                        "  0.0000000E+00 0.0000000E+00  10537.31      8235.889     0.0000000E+00 103001\n" +
                        "  0.0000000E+00 0.0000000E+00 -1854.050      1219.885     0.0000000E+00 013001\n" +
                        "   8818.798      5065.556     0.0000000E+00 0.0000000E+00 -1117.378     004001\n" +
                        "   127470.2      4526.825     0.0000000E+00 0.0000000E+00  15036.57     300200\n" +
                        "   202864.3     -159358.4     0.0000000E+00 0.0000000E+00  81425.77     210200\n" +
                        "  -317866.7     -525561.3     0.0000000E+00 0.0000000E+00  82069.03     120200\n" +
                        "  -526846.2     -407088.8     0.0000000E+00 0.0000000E+00 -3315.201     030200\n" +
                        "  0.0000000E+00 0.0000000E+00 -840413.7     -513989.4     0.0000000E+00 201200\n" +
                        "  0.0000000E+00 0.0000000E+00 -2352002.     -1411551.     0.0000000E+00 111200\n" +
                        "  0.0000000E+00 0.0000000E+00 -1445065.     -877929.7     0.0000000E+00 021200\n" +
                        "  -1060661.     -630241.1     0.0000000E+00 0.0000000E+00 -180388.0     102200\n" +
                        "  -2036749.     -1179977.     0.0000000E+00 0.0000000E+00 -22713.70     012200\n" +
                        "  0.0000000E+00 0.0000000E+00  613416.8      375983.1     0.0000000E+00 003200\n" +
                        "  0.0000000E+00 0.0000000E+00  12534.40      11700.30     0.0000000E+00 300101\n" +
                        "  0.0000000E+00 0.0000000E+00  25197.56      32660.93     0.0000000E+00 210101\n" +
                        "  0.0000000E+00 0.0000000E+00 -2987.323      19462.21     0.0000000E+00 120101\n" +
                        "  0.0000000E+00 0.0000000E+00 -7962.333     -1180.417     0.0000000E+00 030101\n" +
                        "   61428.17      31292.31     0.0000000E+00 0.0000000E+00  4076.176     201101\n" +
                        "   175207.3      97114.33     0.0000000E+00 0.0000000E+00  22849.92     111101\n" +
                        "   100561.4      60749.70     0.0000000E+00 0.0000000E+00  16239.52     021101\n" +
                        "  0.0000000E+00 0.0000000E+00  49921.44      41700.38     0.0000000E+00 102101\n" +
                        "  0.0000000E+00 0.0000000E+00 -29092.26     -4133.364     0.0000000E+00 012101\n" +
                        "   67231.37      38599.39     0.0000000E+00 0.0000000E+00 -8433.004     003101\n" +
                        "   279.2909      133.2571     0.0000000E+00 0.0000000E+00  55.89761     300002\n" +
                        "   1911.514      1104.779     0.0000000E+00 0.0000000E+00  177.5674     210002\n" +
                        "   3726.737      2229.723     0.0000000E+00 0.0000000E+00  149.7101     120002\n" +
                        "   2177.536      1550.184     0.0000000E+00 0.0000000E+00 -49.76450     030002\n" +
                        "  0.0000000E+00 0.0000000E+00  847.4317      1009.307     0.0000000E+00 201002\n" +
                        "  0.0000000E+00 0.0000000E+00  80.23721      1730.773     0.0000000E+00 111002\n" +
                        "  0.0000000E+00 0.0000000E+00 -2918.820     -38.03287     0.0000000E+00 021002\n" +
                        "  -133.0355     -761.4696     0.0000000E+00 0.0000000E+00 -267.5137     102002\n" +
                        "   1772.385     -319.7556     0.0000000E+00 0.0000000E+00  210.8632     012002\n" +
                        "  0.0000000E+00 0.0000000E+00  2094.208      1052.659     0.0000000E+00 003002\n" +
                        "  0.0000000E+00 0.0000000E+00 -632798.6     -396978.4     0.0000000E+00 200300\n" +
                        "  0.0000000E+00 0.0000000E+00 -1773128.     -1074844.     0.0000000E+00 110300\n" +
                        "  0.0000000E+00 0.0000000E+00 -1175825.     -720045.1     0.0000000E+00 020300\n" +
                        "  -1513395.     -905405.1     0.0000000E+00 0.0000000E+00 -250622.7     101300\n" +
                        "  -3020039.     -1733876.     0.0000000E+00 0.0000000E+00 -31311.41     011300\n" +
                        "  0.0000000E+00 0.0000000E+00  1139172.      707787.0     0.0000000E+00 002300\n" +
                        "   62124.25      27306.52     0.0000000E+00 0.0000000E+00  7035.946     200201\n" +
                        "   138537.5      75773.87     0.0000000E+00 0.0000000E+00  24271.93     110201\n" +
                        "   59508.59      38764.15     0.0000000E+00 0.0000000E+00  20107.45     020201\n" +
                        "  0.0000000E+00 0.0000000E+00  40934.73      48621.52     0.0000000E+00 101201\n" +
                        "  0.0000000E+00 0.0000000E+00 -108541.8     -40991.64     0.0000000E+00 011201\n" +
                        "   178226.0      106941.7     0.0000000E+00 0.0000000E+00 -25384.38     002201\n" +
                        "  0.0000000E+00 0.0000000E+00  1339.821      1641.431     0.0000000E+00 200102\n" +
                        "  0.0000000E+00 0.0000000E+00 -1305.313      1585.964     0.0000000E+00 110102\n" +
                        "  0.0000000E+00 0.0000000E+00 -6552.670     -2069.433     0.0000000E+00 020102\n" +
                        "   4323.796     -1423.742     0.0000000E+00 0.0000000E+00 -195.4364     101102\n" +
                        "   12320.38      1425.794     0.0000000E+00 0.0000000E+00  1249.453     011102\n" +
                        "  0.0000000E+00 0.0000000E+00  14129.56      8159.645     0.0000000E+00 002102\n" +
                        "   20.85404     -17.51918     0.0000000E+00 0.0000000E+00  9.134037     200003\n" +
                        "   133.2277      10.46593     0.0000000E+00 0.0000000E+00  17.50286     110003\n" +
                        "   111.5633     -26.66882     0.0000000E+00 0.0000000E+00  25.09856     020003\n" +
                        "  0.0000000E+00 0.0000000E+00  40.05845      111.2871     0.0000000E+00 101003\n" +
                        "  0.0000000E+00 0.0000000E+00  193.0363      105.0913     0.0000000E+00 011003\n" +
                        "  -133.7280     -37.49885     0.0000000E+00 0.0000000E+00 -21.50850     002003\n" +
                        "  -790580.9     -480768.1     0.0000000E+00 0.0000000E+00 -134999.5     100400\n" +
                        "  -1650079.     -944116.5     0.0000000E+00 0.0000000E+00 -18628.47     010400\n" +
                        "  0.0000000E+00 0.0000000E+00  1027992.      648785.2     0.0000000E+00 001400\n" +
                        "  0.0000000E+00 0.0000000E+00 -36757.14     -6202.519     0.0000000E+00 100301\n" +
                        "  0.0000000E+00 0.0000000E+00 -109442.0     -51648.26     0.0000000E+00 010301\n" +
                        "   184581.1      124464.4     0.0000000E+00 0.0000000E+00 -35807.20     001301\n" +
                        "   9199.348      430.1448     0.0000000E+00 0.0000000E+00  1060.349     100202\n" +
                        "   15731.31      4515.839     0.0000000E+00 0.0000000E+00  1091.066     010202\n" +
                        "  0.0000000E+00 0.0000000E+00  25668.71      16943.58     0.0000000E+00 001202\n" +
                        "  0.0000000E+00 0.0000000E+00 -78.21961      114.9232     0.0000000E+00 100103\n" +
                        "  0.0000000E+00 0.0000000E+00  312.2081      222.7418     0.0000000E+00 010103\n" +
                        "  -589.4062     -354.3483     0.0000000E+00 0.0000000E+00 -47.84715     001103\n" +
                        "  -2.385894     -4.486176     0.0000000E+00 0.0000000E+00  1.320619     100004\n" +
                        "   2.621372      1.537968     0.0000000E+00 0.0000000E+00-0.4386217     010004\n" +
                        "  0.0000000E+00 0.0000000E+00 -11.66445      10.06869     0.0000000E+00 001004\n" +
                        "  0.0000000E+00 0.0000000E+00  354943.9      228701.8     0.0000000E+00 000500\n" +
                        "   53032.31      48988.94     0.0000000E+00 0.0000000E+00 -19745.64     000401\n" +
                        "  0.0000000E+00 0.0000000E+00  9162.688      8023.569     0.0000000E+00 000302\n" +
                        "  -356.3888     -503.3922     0.0000000E+00 0.0000000E+00  102.4324     000203\n" +
                        "  0.0000000E+00 0.0000000E+00 -54.85984     -5.793614     0.0000000E+00 000104\n" +
                        " -0.3811689    -0.4168399     0.0000000E+00 0.0000000E+00 0.6493005     000005"
        );
    }


    // ---------------- pram ---------------------

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
        return CctFactory.combineCct(getCct1(), getCctSymmetryCct1());
    }


    private Cct getCct1() {
        Trajectory trajectory = getTrajectory();

        Cct cct = getCct();


        return CctFactory.positionInXYPlane(cct, Point2.create(DL1, trajectoryBigR), BaseUtils.Converter.angleToRadian(-90));

    }

    private Cct getCct2() {

        Trajectory trajectory = getTrajectory();

        Cct dipoleCct = createDipoleCct(
                dipoleCctSmallRInner, dipoleCctSmallROuter, dipoleCctBigR,
                dipoleCctAngle, dipoleCctWindingNumber,
                dipoleCctA0Inner, dipoleCctA1Inner, dipoleCctA2Inner, dipoleCctIInner,
                dipoleCctA0Outer, dipoleCctA1Outer, dipoleCctA2Outer, dipoleCctIOuter,
                numberPerWinding,
                dipoleCctStartingθInner, dipoleCctStartingθOuter,
                dipoleCctStartingφInner, dipoleCctStartingφOuter,
                dipoleCctDirectθInner, dipoleCctDirectθOuter
        );

        Cct agCct = CctFactory.createAgCct(agCctSmallRInner, agCctSmallROuter, agCCTBigR,
                new double[]{agCctAngle1, agCctAngle0},
                new int[]{agCctWindingNumber1, agCctWindingNumber0},
                agCctA0Inner, agCctA1Inner, agCctA2Inner, -agCctIInner,
                agCctA0Outer, agCctA1Outer, agCctA2Outer, -agCctIOuter,
                numberPerWinding, agCctStartingθInner, agCctStartingθOuter,
                agCctStartingφInner, agCctStartingφOuter,
                agCctDirectθInner, agCctDirectθOuter
        );

        Cct cct = CctFactory.combineCct(agCct, dipoleCct);

        //CctFactory.Cct cct1 = CctFactory.positionInXYPlane(cct, Point2.create(DL1, trajectoryBigR), BaseUtils.Converter.angleToRadian(-90));

        Point2 center = Arcs.center(
                trajectory.pointAt(DL1 + CCT_LENGTH + BETWEEN_CCT225),
                trajectory.pointAt(DL1 + CCT_LENGTH + BETWEEN_CCT225 + CCT_LENGTH / 2),
                trajectory.pointAt(DL1 + CCT_LENGTH + BETWEEN_CCT225 + CCT_LENGTH)
        );


        return CctFactory.positionInXYPlane(cct, center, BaseUtils.Converter.angleToRadian(-90 + 22.5));


    }

    private Cct getCctSymmetryCct1() {
        Trajectory trajectory = getTrajectory();
        return CctFactory.symmetryInXYPlaneByLine(
                getCct1(),
                trajectory.pointAt(DL1 + CCT_LENGTH + GAP1 + QS1_LEN + GAP2 + QS2_LEN / 2),
                trajectory.directAt(DL1 + CCT_LENGTH + GAP1 + QS1_LEN + GAP2 + QS2_LEN / 2).rotateSelf(Math.PI / 2)
        );
    }


    private Cct getCct() {
        Cct dipoleCct = createDipoleCct(
                dipoleCctSmallRInner, dipoleCctSmallROuter, dipoleCctBigR,
                dipoleCctAngle, dipoleCctWindingNumber,
                dipoleCctA0Inner, dipoleCctA1Inner, dipoleCctA2Inner, dipoleCctIInner,
                dipoleCctA0Outer, dipoleCctA1Outer, dipoleCctA2Outer, dipoleCctIOuter,
                numberPerWinding,
                dipoleCctStartingθInner, dipoleCctStartingθOuter,
                dipoleCctStartingφInner, dipoleCctStartingφOuter,
                dipoleCctDirectθInner, dipoleCctDirectθOuter
        );
        Cct agCct = createAgCct(
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

    final double Bp = 2.43213; // 磁钢度

    // QS 123
    private double QS1_GRADIENT = 53.3; //T m-1
    private double QS1_SECOND_GRADIENT = -213.78; //T m-2
    private double QS2_GRADIENT = -37.7; //T m-1
    private double QS2_SECOND_GRADIENT = 673.56; //T m-2
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
    private double agCctIInner = -4618.911905272398;//* (1 - 0.010335570469798657);
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
