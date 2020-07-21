package cn.edu.hust.zrx.cct.study.验证性研究;

import cn.edu.hust.zrx.cct.advanced.CosyArbitraryOrder;
import cn.edu.hust.zrx.cct.base.BaseUtils;
import cn.edu.hust.zrx.cct.base.particle.PhaseSpaceParticle;
import cn.edu.hust.zrx.cct.base.particle.PhaseSpaceParticles;
import cn.edu.hust.zrx.cct.base.particle.Protons;
import cn.edu.hust.zrx.cct.base.point.Point2;
import cn.edu.hust.zrx.cct.base.python.Plot2d;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Description
 * F5CosyeTracking绘图
 * <p>
 * Data
 * 22:02
 *
 * @author zrx
 * @version 1.0
 */

public class F5CosyeTracking绘图 {
    public static void main(String[] args) {
//        new F5CosyeTracking绘图().test();
//        new F5CosyeTracking绘图().一阶dp0X();
//        new F5CosyeTracking绘图().一阶dp0y();
//        new F5CosyeTracking绘图().一阶dp1432X();
//        new F5CosyeTracking绘图().一阶dp1432y();


//        new F5CosyeTracking绘图().五阶dp0X();
//        new F5CosyeTracking绘图().五阶dp0y();

//        new F5CosyeTracking绘图().五阶dp1432X();
//        new F5CosyeTracking绘图().五阶dp1432y();

//        new F5CosyeTracking绘图().二阶dp1432X();
//        new F5CosyeTracking绘图().二阶dp1432y();

//        new F5CosyeTracking绘图().九阶dp1432X();
//        new F5CosyeTracking绘图().九阶dp1432y();


//        new F5CosyeTracking绘图().fiveCosyMap();


//        new F5CosyeTracking绘图().cosyTrack自身一致性x方向();
        new F5CosyeTracking绘图().cosyTrack自身一致性y方向();

    }


    public void 一阶dp0X() {
        List<PhaseSpaceParticle> phaseSpaceParticles = a一阶dp0xCOSY();
        System.out.println("phaseSpaceParticles.size() = " + phaseSpaceParticles.size());

        boolean xxPlane = true;
        List<PhaseSpaceParticle> pp = PhaseSpaceParticles.phaseSpaceParticlesAlongPositiveEllipseInPlane(
                xxPlane, 3.5 * MM, 7.5 * MM, 0.0, 32);

        CosyArbitraryOrder.CosyMapArbitraryOrder map = map();

        List<PhaseSpaceParticle> apply = map.apply(pp, 1);


        List<Point2> cosyTrack = PhaseSpaceParticles.projectionToPlane(xxPlane, phaseSpaceParticles);
        List<Point2> cosyMap = PhaseSpaceParticles.projectionToPlane(xxPlane, apply);

        Plot2d.plot2circle(Point2.convert(cosyMap, 1 / MM, 1 / MM), Plot2d.RED_LINE);
        Plot2d.plot2circle(Point2.convert(cosyTrack, 1 / MM, 1 / MM), Plot2d.BLUE_LINE);

        Plot2d.info("x/mm", "xp/mrad", "", 18);
        Plot2d.legend(18, "cosy map", "cosy track");

        Plot2d.equal();

        Plot2d.showThread();
    }

    public void 一阶dp0y() {
        List<PhaseSpaceParticle> phaseSpaceParticles = a一阶dp0yCOSY();
        System.out.println("phaseSpaceParticles.size() = " + phaseSpaceParticles.size());

        boolean xxPlane = false;
        List<PhaseSpaceParticle> pp = PhaseSpaceParticles.phaseSpaceParticlesAlongPositiveEllipseInPlane(
                xxPlane, 3.5 * MM, 7.5 * MM, 0.0, 32);

        CosyArbitraryOrder.CosyMapArbitraryOrder map = map();

        List<PhaseSpaceParticle> apply = map.apply(pp, 1);


        List<Point2> cosyTrack = PhaseSpaceParticles.projectionToPlane(xxPlane, phaseSpaceParticles);
        List<Point2> cosyMap = PhaseSpaceParticles.projectionToPlane(xxPlane, apply);

        Plot2d.plot2circle(Point2.convert(cosyMap, 1 / MM, 1 / MM), Plot2d.RED_LINE);
        Plot2d.plot2circle(Point2.convert(cosyTrack, 1 / MM, 1 / MM), Plot2d.BLUE_LINE);

        if (xxPlane) {
            Plot2d.info("x/mm", "xp/mrad", "", 18);
        }
        {
            Plot2d.info("y/mm", "yp/mrad", "", 18);
        }

        Plot2d.legend(18, "cosy map", "cosy track");

        Plot2d.equal();

        Plot2d.showThread();
    }

    public void 一阶dp1432X() {
        List<PhaseSpaceParticle> phaseSpaceParticles = a一阶dp1432xCOSY();
        System.out.println("phaseSpaceParticles.size() = " + phaseSpaceParticles.size());

        boolean xxPlane = true;
        List<PhaseSpaceParticle> pp = PhaseSpaceParticles.phaseSpaceParticlesAlongPositiveEllipseInPlane(
                xxPlane, 3.5 * MM, 7.5 * MM, 0.1432, 32);

        CosyArbitraryOrder.CosyMapArbitraryOrder map = map();

        List<PhaseSpaceParticle> apply = map.apply(pp, 1);


        List<Point2> cosyTrack = PhaseSpaceParticles.projectionToPlane(xxPlane, phaseSpaceParticles);
        List<Point2> cosyMap = PhaseSpaceParticles.projectionToPlane(xxPlane, apply);

        Plot2d.plot2circle(Point2.convert(cosyMap, 1 / MM, 1 / MM), Plot2d.RED_LINE);
        Plot2d.plot2circle(Point2.convert(cosyTrack, 1 / MM, 1 / MM), Plot2d.BLUE_LINE);

        Plot2d.info("x/mm", "xp/mrad", "", 18);
        Plot2d.legend(18, "cosy map", "cosy track");

        Plot2d.equal();

        Plot2d.showThread();
    }

    public void 一阶dp1432y() {
        List<PhaseSpaceParticle> phaseSpaceParticles = a一阶dp1432yCOSY();
        System.out.println("phaseSpaceParticles.size() = " + phaseSpaceParticles.size());

        boolean xxPlane = false;
        List<PhaseSpaceParticle> pp = PhaseSpaceParticles.phaseSpaceParticlesAlongPositiveEllipseInPlane(
                xxPlane, 3.5 * MM, 7.5 * MM, 0.1432, 32);

        CosyArbitraryOrder.CosyMapArbitraryOrder map = map();

        List<PhaseSpaceParticle> apply = map.apply(pp, 1);


        List<Point2> cosyTrack = PhaseSpaceParticles.projectionToPlane(xxPlane, phaseSpaceParticles);
        List<Point2> cosyMap = PhaseSpaceParticles.projectionToPlane(xxPlane, apply);

        Plot2d.plot2circle(Point2.convert(cosyMap, 1 / MM, 1 / MM), Plot2d.RED_LINE);
        Plot2d.plot2circle(Point2.convert(cosyTrack, 1 / MM, 1 / MM), Plot2d.BLUE_LINE);

        if (xxPlane) {
            Plot2d.info("x/mm", "xp/mrad", "", 18);
        }
        {
            Plot2d.info("y/mm", "yp/mrad", "", 18);
        }

        Plot2d.legend(18, "cosy map", "cosy track");

        Plot2d.equal();

        Plot2d.showThread();
    }

    public void 五阶dp0X() {
        List<PhaseSpaceParticle> phaseSpaceParticles = a五阶dp0xCOSY();
        System.out.println("phaseSpaceParticles.size() = " + phaseSpaceParticles.size());

        boolean xxPlane = true;
        List<PhaseSpaceParticle> pp = PhaseSpaceParticles.phaseSpaceParticlesAlongPositiveEllipseInPlane(
                xxPlane, 3.5 * MM, 7.5 * MM, 0.0, 32);

        CosyArbitraryOrder.CosyMapArbitraryOrder map = map();

        List<PhaseSpaceParticle> apply = map.apply(pp);


        List<Point2> cosyTrack = PhaseSpaceParticles.projectionToPlane(xxPlane, phaseSpaceParticles);
        List<Point2> cosyMap = PhaseSpaceParticles.projectionToPlane(xxPlane, apply);

        Plot2d.plot2circle(Point2.convert(cosyMap, 1 / MM, 1 / MM), Plot2d.RED_LINE);
        Plot2d.plot2circle(Point2.convert(cosyTrack, 1 / MM, 1 / MM), Plot2d.BLUE_LINE);

        Plot2d.info("x/mm", "xp/mrad", "", 18);
        Plot2d.legend(18, "cosy map", "cosy track");

        Plot2d.equal();

        Plot2d.showThread();
    }

    public void 五阶dp0y() {
        List<PhaseSpaceParticle> phaseSpaceParticles = a五阶dp0yCOSY();
        System.out.println("phaseSpaceParticles.size() = " + phaseSpaceParticles.size());

        boolean xxPlane = false;
        List<PhaseSpaceParticle> pp = PhaseSpaceParticles.phaseSpaceParticlesAlongPositiveEllipseInPlane(
                xxPlane, 3.5 * MM, 7.5 * MM, 0.0, 32);

        CosyArbitraryOrder.CosyMapArbitraryOrder map = map();

        List<PhaseSpaceParticle> apply = map.apply(pp);


        List<Point2> cosyTrack = PhaseSpaceParticles.projectionToPlane(xxPlane, phaseSpaceParticles);
        List<Point2> cosyMap = PhaseSpaceParticles.projectionToPlane(xxPlane, apply);

        Plot2d.plot2circle(Point2.convert(cosyMap, 1 / MM, 1 / MM), Plot2d.RED_LINE);
        Plot2d.plot2circle(Point2.convert(cosyTrack, 1 / MM, 1 / MM), Plot2d.BLUE_LINE);

        if (xxPlane) {
            Plot2d.info("x/mm", "xp/mrad", "", 18);
        }
        {
            Plot2d.info("y/mm", "yp/mrad", "", 18);
        }

        Plot2d.legend(18, "cosy map", "cosy track");

        Plot2d.equal();

        Plot2d.showThread();
    }

    public void 五阶dp1432X() {
        List<PhaseSpaceParticle> phaseSpaceParticles = a五阶dp1432xCOSY();
        System.out.println("phaseSpaceParticles.size() = " + phaseSpaceParticles.size());

        boolean xxPlane = true;
        List<PhaseSpaceParticle> pp = PhaseSpaceParticles.phaseSpaceParticlesAlongPositiveEllipseInPlane(
                xxPlane, 3.5 * MM, 7.5 * MM, 0.1432, 32);

        CosyArbitraryOrder.CosyMapArbitraryOrder map = map();

        List<PhaseSpaceParticle> apply = map.apply(pp);


        List<Point2> cosyTrack = PhaseSpaceParticles.projectionToPlane(xxPlane, phaseSpaceParticles);
        List<Point2> cosyMap = PhaseSpaceParticles.projectionToPlane(xxPlane, apply);

        Plot2d.plot2circle(Point2.convert(cosyMap, 1 / MM, 1 / MM), Plot2d.RED_LINE);
        Plot2d.plot2circle(Point2.convert(cosyTrack, 1 / MM, 1 / MM), Plot2d.BLUE_LINE);

        Plot2d.info("x/mm", "xp/mrad", "", 18);
        Plot2d.legend(18, "cosy map", "cosy track");

        Plot2d.equal();

        Plot2d.showThread();
    }

    public void 五阶dp1432y() {
        List<PhaseSpaceParticle> phaseSpaceParticles = a五阶dp1432yCOSY();
        System.out.println("phaseSpaceParticles.size() = " + phaseSpaceParticles.size());

        boolean xxPlane = false;
        List<PhaseSpaceParticle> pp = PhaseSpaceParticles.phaseSpaceParticlesAlongPositiveEllipseInPlane(
                xxPlane, 3.5 * MM, 7.5 * MM, 0.1432, 32);

        CosyArbitraryOrder.CosyMapArbitraryOrder map = map();

        List<PhaseSpaceParticle> apply = map.apply(pp);


        List<Point2> cosyTrack = PhaseSpaceParticles.projectionToPlane(xxPlane, phaseSpaceParticles);
        List<Point2> cosyMap = PhaseSpaceParticles.projectionToPlane(xxPlane, apply);

        Plot2d.plot2circle(Point2.convert(cosyMap, 1 / MM, 1 / MM), Plot2d.RED_LINE);
        Plot2d.plot2circle(Point2.convert(cosyTrack, 1 / MM, 1 / MM), Plot2d.BLUE_LINE);

        if (xxPlane) {
            Plot2d.info("x/mm", "xp/mrad", "", 18);
        }
        {
            Plot2d.info("y/mm", "yp/mrad", "", 18);
        }

        Plot2d.legend(18, "cosy map", "cosy track");

        Plot2d.equal();

        Plot2d.showThread();
    }

    public void 二阶dp1432X() {
        List<PhaseSpaceParticle> phaseSpaceParticles = a二阶dp1432xCOSY();
        System.out.println("phaseSpaceParticles.size() = " + phaseSpaceParticles.size());

        boolean xxPlane = true;
        List<PhaseSpaceParticle> pp = PhaseSpaceParticles.phaseSpaceParticlesAlongPositiveEllipseInPlane(
                xxPlane, 3.5 * MM, 7.5 * MM, 0.1432, 32);

        CosyArbitraryOrder.CosyMapArbitraryOrder map = map();

        List<PhaseSpaceParticle> apply = map.apply(pp, 2);


        List<Point2> cosyTrack = PhaseSpaceParticles.projectionToPlane(xxPlane, phaseSpaceParticles);
        List<Point2> cosyMap = PhaseSpaceParticles.projectionToPlane(xxPlane, apply);

        Plot2d.plot2circle(Point2.convert(cosyMap, 1 / MM, 1 / MM), Plot2d.RED_LINE);
        Plot2d.plot2circle(Point2.convert(cosyTrack, 1 / MM, 1 / MM), Plot2d.BLUE_LINE);

        Plot2d.info("x/mm", "xp/mrad", "", 18);
        Plot2d.legend(18, "cosy map", "cosy track");

        Plot2d.equal();

        Plot2d.showThread();
    }

    public void 二阶dp1432y() {
        List<PhaseSpaceParticle> phaseSpaceParticles = a二阶dp1432yCOSY();
        System.out.println("phaseSpaceParticles.size() = " + phaseSpaceParticles.size());

        boolean xxPlane = false;
        List<PhaseSpaceParticle> pp = PhaseSpaceParticles.phaseSpaceParticlesAlongPositiveEllipseInPlane(
                xxPlane, 3.5 * MM, 7.5 * MM, 0.1432, 32);

        CosyArbitraryOrder.CosyMapArbitraryOrder map = map();

        List<PhaseSpaceParticle> apply = map.apply(pp, 2);


        List<Point2> cosyTrack = PhaseSpaceParticles.projectionToPlane(xxPlane, phaseSpaceParticles);
        List<Point2> cosyMap = PhaseSpaceParticles.projectionToPlane(xxPlane, apply);

        Plot2d.plot2circle(Point2.convert(cosyMap, 1 / MM, 1 / MM), Plot2d.RED_LINE);
        Plot2d.plot2circle(Point2.convert(cosyTrack, 1 / MM, 1 / MM), Plot2d.BLUE_LINE);

        if (xxPlane) {
            Plot2d.info("x/mm", "xp/mrad", "", 18);
        }
        {
            Plot2d.info("y/mm", "yp/mrad", "", 18);
        }

        Plot2d.legend(18, "cosy map", "cosy track");

        Plot2d.equal();

        Plot2d.showThread();
    }

    public void 九阶dp1432X() {
        List<PhaseSpaceParticle> phaseSpaceParticles = a九阶dp1432xCOSY();
        System.out.println("phaseSpaceParticles.size() = " + phaseSpaceParticles.size());

        boolean xxPlane = true;
        List<PhaseSpaceParticle> pp = PhaseSpaceParticles.phaseSpaceParticlesAlongPositiveEllipseInPlane(
                xxPlane, 3.5 * MM, 7.5 * MM, 0.1432, 32);

        CosyArbitraryOrder.CosyMapArbitraryOrder map = map9();

        List<PhaseSpaceParticle> apply = map.apply(pp);


        List<Point2> cosyTrack = PhaseSpaceParticles.projectionToPlane(xxPlane, phaseSpaceParticles);
        List<Point2> cosyMap = PhaseSpaceParticles.projectionToPlane(xxPlane, apply);

        Plot2d.plot2circle(Point2.convert(cosyMap, 1 / MM, 1 / MM), Plot2d.RED_LINE);
        Plot2d.plot2circle(Point2.convert(cosyTrack, 1 / MM, 1 / MM), Plot2d.BLUE_LINE);

        Plot2d.info("x/mm", "xp/mrad", "", 18);
        Plot2d.legend(18, "cosy map", "cosy track");

        Plot2d.equal();

        Plot2d.showThread();
    }

    public void 九阶dp1432y() {
        List<PhaseSpaceParticle> phaseSpaceParticles = a九阶dp1432yCOSY();
        System.out.println("phaseSpaceParticles.size() = " + phaseSpaceParticles.size());

        boolean xxPlane = false;
        List<PhaseSpaceParticle> pp = PhaseSpaceParticles.phaseSpaceParticlesAlongPositiveEllipseInPlane(
                xxPlane, 3.5 * MM, 7.5 * MM, 0.1432, 32);

        CosyArbitraryOrder.CosyMapArbitraryOrder map = map9();

        List<PhaseSpaceParticle> apply = map.apply(pp);


        List<Point2> cosyTrack = PhaseSpaceParticles.projectionToPlane(xxPlane, phaseSpaceParticles);
        List<Point2> cosyMap = PhaseSpaceParticles.projectionToPlane(xxPlane, apply);

        Plot2d.plot2circle(Point2.convert(cosyMap, 1 / MM, 1 / MM), Plot2d.RED_LINE);
        Plot2d.plot2circle(Point2.convert(cosyTrack, 1 / MM, 1 / MM), Plot2d.BLUE_LINE);

        if (xxPlane) {
            Plot2d.info("x/mm", "xp/mrad", "", 18);
        }
        {
            Plot2d.info("y/mm", "yp/mrad", "", 18);
        }

        Plot2d.legend(18, "cosy map", "cosy track");

        Plot2d.equal();

        Plot2d.showThread();
    }

    public void fiveCosyMap() {

        CosyArbitraryOrder.CosyMapArbitraryOrder map = map();

        boolean xxPlane = false;
        String[] le = BaseUtils.Python.linspaceStream(0, 0.08, switcher.getSize())
                .mapToObj(dp -> {

                    double de = Protons.convert动量分散_TO_能量分散(dp, 250);

                    List<PhaseSpaceParticle> pp = PhaseSpaceParticles.phaseSpaceParticlesAlongPositiveEllipseInPlane(
                            xxPlane, 3.5 * MM, 7.5 * MM, de, 512);

                    List<PhaseSpaceParticle> apply = map.apply(pp);

                    List<Point2> cosyMap = PhaseSpaceParticles.projectionToPlane(xxPlane, apply);

                    Plot2d.plot2circle(Point2.convert(cosyMap, 1 / MM, 1 / MM), switcher.getAndSwitch());

                    return String.format("dp=%4.3f", dp);
                }).collect(Collectors.toList())
                .toArray(String[]::new);

        if (xxPlane) {
            Plot2d.info("x/mm", "xp/mrad", "", 18);
        }
        {
            Plot2d.info("y/mm", "yp/mrad", "", 18);
        }

        Plot2d.legend(18, le);

        Plot2d.equal();

        Plot2d.showThread();

    }

    public void cosyTrack自身一致性x方向(){
        { // 1
            List<PhaseSpaceParticle> phaseSpaceParticles = a一阶dp1432xCOSY();
            System.out.println("phaseSpaceParticles.size() = " + phaseSpaceParticles.size());

            boolean xxPlane = true;

            List<Point2> cosyTrack = PhaseSpaceParticles.projectionToPlane(xxPlane, phaseSpaceParticles);


            Plot2d.plot2circle(Point2.convert(cosyTrack, 1 / MM, 1 / MM), Plot2d.BLUE_LINE);
        }

        { // 2
            List<PhaseSpaceParticle> phaseSpaceParticles = a二阶dp1432xCOSY();
            System.out.println("phaseSpaceParticles.size() = " + phaseSpaceParticles.size());

            boolean xxPlane = true;

            List<Point2> cosyTrack = PhaseSpaceParticles.projectionToPlane(xxPlane, phaseSpaceParticles);


            Plot2d.plot2circle(Point2.convert(cosyTrack, 1 / MM, 1 / MM), Plot2d.RED_LINE);
        }

        { // 3
            List<PhaseSpaceParticle> phaseSpaceParticles = a三阶dp1432xCOSY();
            System.out.println("phaseSpaceParticles.size() = " + phaseSpaceParticles.size());

            boolean xxPlane = true;

            List<Point2> cosyTrack = PhaseSpaceParticles.projectionToPlane(xxPlane, phaseSpaceParticles);


            Plot2d.plot2circle(Point2.convert(cosyTrack, 1 / MM, 1 / MM), Plot2d.GREEN_LINE);
        }

        { // 5
            List<PhaseSpaceParticle> phaseSpaceParticles = a五阶dp1432xCOSY();
            System.out.println("phaseSpaceParticles.size() = " + phaseSpaceParticles.size());

            boolean xxPlane = true;

            List<Point2> cosyTrack = PhaseSpaceParticles.projectionToPlane(xxPlane, phaseSpaceParticles);


            Plot2d.plot2circle(Point2.convert(cosyTrack, 1 / MM, 1 / MM), Plot2d.BLACK_LINE);
        }

        { // 9
            List<PhaseSpaceParticle> phaseSpaceParticles = a九阶dp1432xCOSY();
            System.out.println("phaseSpaceParticles.size() = " + phaseSpaceParticles.size());

            boolean xxPlane = true;

            List<Point2> cosyTrack = PhaseSpaceParticles.projectionToPlane(xxPlane, phaseSpaceParticles);


            Plot2d.plot2circle(Point2.convert(cosyTrack, 1 / MM, 1 / MM), Plot2d.YELLOW_DASH);
        }




        Plot2d.info("x/mm", "xp/mrad", "", 18);
        Plot2d.legend(18, "cosy track 1st","cosy track 2ed","cosy track 3rd","cosy track 5th","cosy track 9th");

        Plot2d.equal();

        Plot2d.showThread();
    }

    public void cosyTrack自身一致性y方向(){
        boolean xxPlane = false;

        { // 1
            List<PhaseSpaceParticle> phaseSpaceParticles = a一阶dp1432yCOSY();
            System.out.println("phaseSpaceParticles.size() = " + phaseSpaceParticles.size());

//            boolean xxPlane = true;

            List<Point2> cosyTrack = PhaseSpaceParticles.projectionToPlane(xxPlane, phaseSpaceParticles);


            Plot2d.plot2circle(Point2.convert(cosyTrack, 1 / MM, 1 / MM), Plot2d.BLUE_LINE);
        }

        { // 2
            List<PhaseSpaceParticle> phaseSpaceParticles = a二阶dp1432yCOSY();
            System.out.println("phaseSpaceParticles.size() = " + phaseSpaceParticles.size());

//            boolean xxPlane = true;

            List<Point2> cosyTrack = PhaseSpaceParticles.projectionToPlane(xxPlane, phaseSpaceParticles);


            Plot2d.plot2circle(Point2.convert(cosyTrack, 1 / MM, 1 / MM), Plot2d.RED_LINE);
        }

        { // 3
            List<PhaseSpaceParticle> phaseSpaceParticles = a三阶dp1432yCOSY();
            System.out.println("phaseSpaceParticles.size() = " + phaseSpaceParticles.size());

//            boolean xxPlane = true;

            List<Point2> cosyTrack = PhaseSpaceParticles.projectionToPlane(xxPlane, phaseSpaceParticles);


            Plot2d.plot2circle(Point2.convert(cosyTrack, 1 / MM, 1 / MM), Plot2d.GREEN_LINE);
        }

        { // 5
            List<PhaseSpaceParticle> phaseSpaceParticles = a五阶dp1432yCOSY();
            System.out.println("phaseSpaceParticles.size() = " + phaseSpaceParticles.size());

//            boolean xxPlane = true;

            List<Point2> cosyTrack = PhaseSpaceParticles.projectionToPlane(xxPlane, phaseSpaceParticles);


            Plot2d.plot2circle(Point2.convert(cosyTrack, 1 / MM, 1 / MM), Plot2d.BLACK_LINE);
        }

        { // 9
            List<PhaseSpaceParticle> phaseSpaceParticles = a九阶dp1432yCOSY();
            System.out.println("phaseSpaceParticles.size() = " + phaseSpaceParticles.size());

//            boolean xxPlane = true;

            List<Point2> cosyTrack = PhaseSpaceParticles.projectionToPlane(xxPlane, phaseSpaceParticles);


            Plot2d.plot2circle(Point2.convert(cosyTrack, 1 / MM, 1 / MM), Plot2d.YELLOW_DASH);
        }




        Plot2d.info("y/mm", "yp/mrad", "", 18);
        Plot2d.legend(18, "cosy track 1st","cosy track 2nd","cosy track 3rd","cosy track 5th","cosy track 9th");

        Plot2d.equal();

        Plot2d.showThread();
    }

    private List<PhaseSpaceParticle> a一阶dp1432xCOSY() {
        return PhaseSpaceParticles.readFromCosy_PRAY(
                //"  0.0000000E+00  0.0000000E+00  0.0000000E+00  0.0000000E+00  0.0000000E+00  0.0000000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                //   "  0.0000000E+00  0.0000000E+00  0.0000000E+00  0.0000000E+00  0.0000000E+00  0.0000000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                "  0.1710391E-02  0.4171064E-02  0.0000000E+00  0.0000000E+00  0.2976091E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.1232483E-02  0.5048299E-02  0.0000000E+00  0.0000000E+00  0.2976062E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.7384722E-03  0.5768569E-02  0.0000000E+00  0.0000000E+00  0.2976021E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.2255063E-03  0.6320789E-02  0.0000000E+00  0.0000000E+00  0.2975968E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        " -0.3029634E-03  0.6670664E-02  0.0000000E+00  0.0000000E+00  0.2975901E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        " -0.8450466E-03  0.6763739E-02  0.0000000E+00  0.0000000E+00  0.2975817E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        " -0.1392943E-02  0.6499213E-02  0.0000000E+00  0.0000000E+00  0.2975713E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        " -0.1922575E-02  0.5681238E-02  0.0000000E+00  0.0000000E+00  0.2975580E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        " -0.2338342E-02  0.4014409E-02  0.0000000E+00  0.0000000E+00  0.2975419E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        " -0.2464693E-02  0.1777163E-02  0.0000000E+00  0.0000000E+00  0.2975273E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        " -0.2345358E-02 -0.3554577E-03  0.0000000E+00  0.0000000E+00  0.2975173E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        " -0.2096240E-02 -0.2235444E-02  0.0000000E+00  0.0000000E+00  0.2975109E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        " -0.1773097E-02 -0.3897047E-02  0.0000000E+00  0.0000000E+00  0.2975068E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        " -0.1401978E-02 -0.5372404E-02  0.0000000E+00  0.0000000E+00  0.2975047E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        " -0.9956131E-03 -0.6686159E-02  0.0000000E+00  0.0000000E+00  0.2975040E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        " -0.5632696E-03 -0.7846589E-02  0.0000000E+00  0.0000000E+00  0.2975046E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        " -0.1056870E-03 -0.8868847E-02  0.0000000E+00  0.0000000E+00  0.2975064E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.3709435E-03 -0.9743486E-02  0.0000000E+00  0.0000000E+00  0.2975093E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.8649542E-03 -0.1046376E-01  0.0000000E+00  0.0000000E+00  0.2975133E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.1377920E-02 -0.1101598E-01  0.0000000E+00  0.0000000E+00  0.2975187E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.1906390E-02 -0.1136585E-01  0.0000000E+00  0.0000000E+00  0.2975254E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.2448473E-02 -0.1145893E-01  0.0000000E+00  0.0000000E+00  0.2975337E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.2996369E-02 -0.1119440E-01  0.0000000E+00  0.0000000E+00  0.2975442E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.3526001E-02 -0.1037642E-01  0.0000000E+00  0.0000000E+00  0.2975574E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.3941768E-02 -0.8709596E-02  0.0000000E+00  0.0000000E+00  0.2975736E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.4068120E-02 -0.6472350E-02  0.0000000E+00  0.0000000E+00  0.2975882E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.3948785E-02 -0.4339729E-02  0.0000000E+00  0.0000000E+00  0.2975982E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.3699667E-02 -0.2459743E-02  0.0000000E+00  0.0000000E+00  0.2976046E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.3376523E-02 -0.7981403E-03  0.0000000E+00  0.0000000E+00  0.2976086E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.3005404E-02  0.6772174E-03  0.0000000E+00  0.0000000E+00  0.2976108E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.2599039E-02  0.1990972E-02  0.0000000E+00  0.0000000E+00  0.2976115E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.2166696E-02  0.3151402E-02  0.0000000E+00  0.0000000E+00  0.2976109E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0"
        );
    }

    private List<PhaseSpaceParticle> a一阶dp1432yCOSY() {
        return PhaseSpaceParticles.readFromCosy_PRAY(
                "  0.8017132E-03 -0.2347593E-02  0.2808430E-02  0.2724823E-02  0.2975577E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.8017132E-03 -0.2347593E-02  0.2505215E-02  0.3823744E-02  0.2975577E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.8017132E-03 -0.2347593E-02  0.2140934E-02  0.4850003E-02  0.2975577E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.8017132E-03 -0.2347593E-02  0.1709324E-02  0.5800563E-02  0.2975577E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.8017132E-03 -0.2347593E-02  0.1204955E-02  0.6651044E-02  0.2975577E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.8017132E-03 -0.2347593E-02  0.6151239E-03  0.7367119E-02  0.2975577E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.8017132E-03 -0.2347593E-02 -0.7879674E-04  0.7879993E-02  0.2975577E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.8017132E-03 -0.2347593E-02 -0.9028755E-03  0.8045118E-02  0.2975577E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.8017132E-03 -0.2347593E-02 -0.1829153E-02  0.7572145E-02  0.2975577E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.8017132E-03 -0.2347593E-02 -0.2582487E-02  0.6410644E-02  0.2975577E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.8017132E-03 -0.2347593E-02 -0.3028470E-02  0.5010930E-02  0.2975577E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.8017132E-03 -0.2347593E-02 -0.3258288E-02  0.3601449E-02  0.2975577E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.8017132E-03 -0.2347593E-02 -0.3344557E-02  0.2230081E-02  0.2975577E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.8017132E-03 -0.2347593E-02 -0.3325604E-02  0.9097068E-03  0.2975577E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.8017132E-03 -0.2347593E-02 -0.3222592E-02 -0.3586335E-03  0.2975577E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.8017132E-03 -0.2347593E-02 -0.3048296E-02 -0.1568503E-02  0.2975577E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.8017132E-03 -0.2347593E-02 -0.2807687E-02 -0.2727909E-02  0.2975577E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.8017132E-03 -0.2347593E-02 -0.2505215E-02 -0.3823744E-02  0.2975577E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.8017132E-03 -0.2347593E-02 -0.2140934E-02 -0.4850003E-02  0.2975577E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.8017132E-03 -0.2347593E-02 -0.1709324E-02 -0.5800563E-02  0.2975577E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.8017132E-03 -0.2347593E-02 -0.1204955E-02 -0.6651044E-02  0.2975577E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.8017132E-03 -0.2347593E-02 -0.6151239E-03 -0.7367119E-02  0.2975577E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.8017132E-03 -0.2347593E-02  0.7879674E-04 -0.7879993E-02  0.2975577E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.8017132E-03 -0.2347593E-02  0.9028755E-03 -0.8045118E-02  0.2975577E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.8017132E-03 -0.2347593E-02  0.1829153E-02 -0.7572145E-02  0.2975577E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.8017132E-03 -0.2347593E-02  0.2582487E-02 -0.6410644E-02  0.2975577E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.8017132E-03 -0.2347593E-02  0.3028470E-02 -0.5010930E-02  0.2975577E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.8017132E-03 -0.2347593E-02  0.3258288E-02 -0.3601449E-02  0.2975577E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.8017132E-03 -0.2347593E-02  0.3344557E-02 -0.2230081E-02  0.2975577E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.8017132E-03 -0.2347593E-02  0.3325604E-02 -0.9097068E-03  0.2975577E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.8017132E-03 -0.2347593E-02  0.3222592E-02  0.3586335E-03  0.2975577E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.8017132E-03 -0.2347593E-02  0.3048296E-02  0.1568503E-02  0.2975577E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0"
        );
    }

    private List<PhaseSpaceParticle> a一阶dp0xCOSY() {
        return PhaseSpaceParticles.readFromCosy_PRAY(
                "  0.9086782E-03  0.6518657E-02  0.0000000E+00  0.0000000E+00  0.5139176E-04  0.0000000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.4307697E-03  0.7395893E-02  0.0000000E+00  0.0000000E+00  0.4846827E-04  0.0000000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        " -0.6324101E-04  0.8116163E-02  0.0000000E+00  0.0000000E+00  0.4440203E-04  0.0000000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        " -0.5762069E-03  0.8668383E-02  0.0000000E+00  0.0000000E+00  0.3908420E-04  0.0000000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        " -0.1104677E-02  0.9018257E-02  0.0000000E+00  0.0000000E+00  0.3237936E-04  0.0000000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        " -0.1646760E-02  0.9111332E-02  0.0000000E+00  0.0000000E+00  0.2401364E-04  0.0000000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        " -0.2194656E-02  0.8846807E-02  0.0000000E+00  0.0000000E+00  0.1355056E-04  0.0000000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        " -0.2724288E-02  0.8028831E-02  0.0000000E+00  0.0000000E+00  0.2884032E-06  0.0000000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        " -0.3140055E-02  0.6362003E-02  0.0000000E+00  0.0000000E+00 -0.1585943E-04  0.0000000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        " -0.3266407E-02  0.4124757E-02  0.0000000E+00  0.0000000E+00 -0.3045617E-04  0.0000000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        " -0.3147071E-02  0.1992136E-02  0.0000000E+00  0.0000000E+00 -0.4043940E-04  0.0000000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        " -0.2897953E-02  0.1121494E-03  0.0000000E+00  0.0000000E+00 -0.4688062E-04  0.0000000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        " -0.2574810E-02 -0.1549453E-02  0.0000000E+00  0.0000000E+00 -0.5088565E-04  0.0000000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        " -0.2203691E-02 -0.3024811E-02  0.0000000E+00  0.0000000E+00 -0.5306147E-04  0.0000000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        " -0.1797326E-02 -0.4338565E-02  0.0000000E+00  0.0000000E+00 -0.5375472E-04  0.0000000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        " -0.1364983E-02 -0.5498996E-02  0.0000000E+00  0.0000000E+00 -0.5316370E-04  0.0000000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        " -0.9074002E-03 -0.6521253E-02  0.0000000E+00  0.0000000E+00 -0.5138534E-04  0.0000000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        " -0.4307697E-03 -0.7395893E-02  0.0000000E+00  0.0000000E+00 -0.4846827E-04  0.0000000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.6324101E-04 -0.8116163E-02  0.0000000E+00  0.0000000E+00 -0.4440203E-04  0.0000000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.5762069E-03 -0.8668383E-02  0.0000000E+00  0.0000000E+00 -0.3908420E-04  0.0000000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.1104677E-02 -0.9018257E-02  0.0000000E+00  0.0000000E+00 -0.3237936E-04  0.0000000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.1646760E-02 -0.9111332E-02  0.0000000E+00  0.0000000E+00 -0.2401364E-04  0.0000000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.2194656E-02 -0.8846807E-02  0.0000000E+00  0.0000000E+00 -0.1355056E-04  0.0000000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.2724288E-02 -0.8028831E-02  0.0000000E+00  0.0000000E+00 -0.2884032E-06  0.0000000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.3140055E-02 -0.6362003E-02  0.0000000E+00  0.0000000E+00  0.1585943E-04  0.0000000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.3266407E-02 -0.4124757E-02  0.0000000E+00  0.0000000E+00  0.3045617E-04  0.0000000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.3147071E-02 -0.1992136E-02  0.0000000E+00  0.0000000E+00  0.4043940E-04  0.0000000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.2897953E-02 -0.1121494E-03  0.0000000E+00  0.0000000E+00  0.4688062E-04  0.0000000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.2574810E-02  0.1549453E-02  0.0000000E+00  0.0000000E+00  0.5088565E-04  0.0000000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.2203691E-02  0.3024811E-02  0.0000000E+00  0.0000000E+00  0.5306147E-04  0.0000000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.1797326E-02  0.4338565E-02  0.0000000E+00  0.0000000E+00  0.5375472E-04  0.0000000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.1364983E-02  0.5498996E-02  0.0000000E+00  0.0000000E+00  0.5316370E-04  0.0000000E+00  0.0000000E+00  0.0000000E+00         1         0"
        );
    }

    private List<PhaseSpaceParticle> a一阶dp0yCOSY() {
        return PhaseSpaceParticles.readFromCosy_PRAY(
                "  0.0000000E+00  0.0000000E+00  0.2808430E-02  0.2724823E-02  0.0000000E+00  0.0000000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.0000000E+00  0.0000000E+00  0.2505215E-02  0.3823744E-02  0.0000000E+00  0.0000000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.0000000E+00  0.0000000E+00  0.2140934E-02  0.4850003E-02  0.0000000E+00  0.0000000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.0000000E+00  0.0000000E+00  0.1709324E-02  0.5800563E-02  0.0000000E+00  0.0000000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.0000000E+00  0.0000000E+00  0.1204955E-02  0.6651044E-02  0.0000000E+00  0.0000000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.0000000E+00  0.0000000E+00  0.6151239E-03  0.7367119E-02  0.0000000E+00  0.0000000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.0000000E+00  0.0000000E+00 -0.7879674E-04  0.7879993E-02  0.0000000E+00  0.0000000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.0000000E+00  0.0000000E+00 -0.9028755E-03  0.8045118E-02  0.0000000E+00  0.0000000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.0000000E+00  0.0000000E+00 -0.1829153E-02  0.7572145E-02  0.0000000E+00  0.0000000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.0000000E+00  0.0000000E+00 -0.2582487E-02  0.6410644E-02  0.0000000E+00  0.0000000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.0000000E+00  0.0000000E+00 -0.3028470E-02  0.5010930E-02  0.0000000E+00  0.0000000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.0000000E+00  0.0000000E+00 -0.3258288E-02  0.3601449E-02  0.0000000E+00  0.0000000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.0000000E+00  0.0000000E+00 -0.3344557E-02  0.2230081E-02  0.0000000E+00  0.0000000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.0000000E+00  0.0000000E+00 -0.3325604E-02  0.9097068E-03  0.0000000E+00  0.0000000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.0000000E+00  0.0000000E+00 -0.3222592E-02 -0.3586335E-03  0.0000000E+00  0.0000000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.0000000E+00  0.0000000E+00 -0.3048296E-02 -0.1568503E-02  0.0000000E+00  0.0000000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.0000000E+00  0.0000000E+00 -0.2807687E-02 -0.2727909E-02  0.0000000E+00  0.0000000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.0000000E+00  0.0000000E+00 -0.2505215E-02 -0.3823744E-02  0.0000000E+00  0.0000000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.0000000E+00  0.0000000E+00 -0.2140934E-02 -0.4850003E-02  0.0000000E+00  0.0000000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.0000000E+00  0.0000000E+00 -0.1709324E-02 -0.5800563E-02  0.0000000E+00  0.0000000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.0000000E+00  0.0000000E+00 -0.1204955E-02 -0.6651044E-02  0.0000000E+00  0.0000000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.0000000E+00  0.0000000E+00 -0.6151239E-03 -0.7367119E-02  0.0000000E+00  0.0000000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.0000000E+00  0.0000000E+00  0.7879674E-04 -0.7879993E-02  0.0000000E+00  0.0000000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.0000000E+00  0.0000000E+00  0.9028755E-03 -0.8045118E-02  0.0000000E+00  0.0000000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.0000000E+00  0.0000000E+00  0.1829153E-02 -0.7572145E-02  0.0000000E+00  0.0000000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.0000000E+00  0.0000000E+00  0.2582487E-02 -0.6410644E-02  0.0000000E+00  0.0000000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.0000000E+00  0.0000000E+00  0.3028470E-02 -0.5010930E-02  0.0000000E+00  0.0000000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.0000000E+00  0.0000000E+00  0.3258288E-02 -0.3601449E-02  0.0000000E+00  0.0000000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.0000000E+00  0.0000000E+00  0.3344557E-02 -0.2230081E-02  0.0000000E+00  0.0000000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.0000000E+00  0.0000000E+00  0.3325604E-02 -0.9097068E-03  0.0000000E+00  0.0000000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.0000000E+00  0.0000000E+00  0.3222592E-02  0.3586335E-03  0.0000000E+00  0.0000000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.0000000E+00  0.0000000E+00  0.3048296E-02  0.1568503E-02  0.0000000E+00  0.0000000E+00  0.0000000E+00  0.0000000E+00         1         0"
        );
    }

    private List<PhaseSpaceParticle> a五阶dp0xCOSY() {
        return PhaseSpaceParticles.readFromCosy_PRAY(
                "  0.7782205E-03  0.6985505E-02  0.0000000E+00  0.0000000E+00  0.4487612E-04  0.0000000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.3524324E-03  0.8010771E-02  0.0000000E+00  0.0000000E+00  0.4407327E-04  0.0000000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        " -0.7537403E-04  0.8817871E-02  0.0000000E+00  0.0000000E+00  0.4256290E-04  0.0000000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        " -0.5135190E-03  0.9379202E-02  0.0000000E+00  0.0000000E+00  0.3998127E-04  0.0000000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        " -0.9655292E-03  0.9646088E-02  0.0000000E+00  0.0000000E+00  0.3584625E-04  0.0000000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        " -0.1437533E-02  0.9554923E-02  0.0000000E+00  0.0000000E+00  0.2948842E-04  0.0000000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        " -0.1932802E-02  0.9005368E-02  0.0000000E+00  0.0000000E+00  0.1999473E-04  0.0000000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        " -0.2444456E-02  0.7825198E-02  0.0000000E+00  0.0000000E+00  0.6078955E-05  0.0000000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        " -0.2903032E-02  0.5819668E-02  0.0000000E+00  0.0000000E+00 -0.1287610E-04  0.0000000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        " -0.3126943E-02  0.3453586E-02  0.0000000E+00  0.0000000E+00 -0.3137250E-04  0.0000000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        " -0.3111279E-02  0.1378186E-02  0.0000000E+00  0.0000000E+00 -0.4467605E-04  0.0000000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        " -0.2950589E-02 -0.3628625E-03  0.0000000E+00  0.0000000E+00 -0.5357491E-04  0.0000000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        " -0.2695214E-02 -0.1853559E-02  0.0000000E+00  0.0000000E+00 -0.5923758E-04  0.0000000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        " -0.2368115E-02 -0.3147162E-02  0.0000000E+00  0.0000000E+00 -0.6228828E-04  0.0000000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        " -0.1979439E-02 -0.4277627E-02  0.0000000E+00  0.0000000E+00 -0.6305243E-04  0.0000000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        " -0.1536067E-02 -0.5259061E-02  0.0000000E+00  0.0000000E+00 -0.6169373E-04  0.0000000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        " -0.1036591E-02 -0.6108771E-02  0.0000000E+00  0.0000000E+00 -0.5827012E-04  0.0000000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        " -0.4862964E-03 -0.6822576E-02  0.0000000E+00  0.0000000E+00 -0.5283766E-04  0.0000000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.1125926E-03 -0.7398962E-02  0.0000000E+00  0.0000000E+00 -0.4544002E-04  0.0000000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.7595151E-03 -0.7831725E-02  0.0000000E+00  0.0000000E+00 -0.3608491E-04  0.0000000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.1443761E-02 -0.8100131E-02  0.0000000E+00  0.0000000E+00 -0.2490887E-04  0.0000000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.2149921E-02 -0.8170266E-02  0.0000000E+00  0.0000000E+00 -0.1209598E-04  0.0000000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.2843631E-02 -0.7974693E-02  0.0000000E+00  0.0000000E+00  0.1965305E-05  0.0000000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.3449754E-02 -0.7369514E-02  0.0000000E+00  0.0000000E+00  0.1650767E-04  0.0000000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.3787926E-02 -0.6095678E-02  0.0000000E+00  0.0000000E+00  0.2944872E-04  0.0000000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.3692482E-02 -0.4253857E-02  0.0000000E+00  0.0000000E+00  0.3728831E-04  0.0000000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.3350982E-02 -0.2327619E-02  0.0000000E+00  0.0000000E+00  0.4106756E-04  0.0000000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.2934037E-02 -0.4811483E-03  0.0000000E+00  0.0000000E+00  0.4305342E-04  0.0000000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.2498102E-02  0.1265740E-02  0.0000000E+00  0.0000000E+00  0.4419922E-04  0.0000000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.2061563E-02  0.2900465E-02  0.0000000E+00  0.0000000E+00  0.4485931E-04  0.0000000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.1629027E-02  0.4411976E-02  0.0000000E+00  0.0000000E+00  0.4517919E-04  0.0000000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.1202664E-02  0.5777705E-02  0.0000000E+00  0.0000000E+00  0.4519911E-04  0.0000000E+00  0.0000000E+00  0.0000000E+00         1         0"
        );
    }

    private List<PhaseSpaceParticle> a五阶dp0yCOSY() {
        return PhaseSpaceParticles.readFromCosy_PRAY(
                "  0.5260926E-03  0.1348972E-03  0.2828652E-02  0.2731458E-02 -0.4420345E-04  0.0000000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.7778869E-03  0.5638561E-04  0.2555972E-02  0.3878803E-02 -0.5713878E-04  0.0000000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.1027641E-02 -0.1190618E-03  0.2259564E-02  0.5012975E-02 -0.6890427E-04  0.0000000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.1259436E-02 -0.3830668E-03  0.1949730E-02  0.6145435E-02 -0.8064843E-04  0.0000000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.1447550E-02 -0.7222317E-03  0.1630098E-02  0.7254669E-02 -0.9281380E-04  0.0000000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.1556213E-02 -0.1119448E-02  0.1284862E-02  0.8292626E-02 -0.1042175E-03  0.0000000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.1531046E-02 -0.1545055E-02  0.8663828E-03  0.9143422E-02 -0.1099483E-03  0.0000000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.1293053E-02 -0.1934854E-02  0.2661797E-03  0.9548698E-02 -0.9825853E-04  0.0000000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.7882167E-03 -0.2119332E-02 -0.6620921E-03  0.8996764E-02 -0.5351569E-04  0.0000000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.2510601E-03 -0.1916505E-02 -0.1699980E-02  0.7405085E-02  0.2329817E-05  0.0000000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        " -0.7035759E-04 -0.1489874E-02 -0.2473075E-02  0.5566125E-02  0.3139177E-04  0.0000000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        " -0.1964659E-03 -0.1025600E-02 -0.2952443E-02  0.3855809E-02  0.3488581E-04  0.0000000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        " -0.1875126E-03 -0.6048242E-03 -0.3201374E-02  0.2316738E-02  0.2379278E-04  0.0000000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        " -0.8584418E-04 -0.2653390E-03 -0.3274210E-02  0.9247840E-03  0.6435164E-05  0.0000000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.8003555E-04 -0.2474221E-04 -0.3213927E-02 -0.3593699E-03 -0.1214855E-04  0.0000000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.2888977E-03  0.1087301E-03 -0.3055950E-02 -0.1565930E-02 -0.2928845E-04  0.0000000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.5267675E-03  0.1348232E-03 -0.2827959E-02 -0.2734612E-02 -0.4424123E-04  0.0000000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.7778869E-03  0.5638561E-04 -0.2555972E-02 -0.3878803E-02 -0.5713878E-04  0.0000000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.1027641E-02 -0.1190618E-03 -0.2259564E-02 -0.5012975E-02 -0.6890427E-04  0.0000000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.1259436E-02 -0.3830668E-03 -0.1949730E-02 -0.6145435E-02 -0.8064843E-04  0.0000000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.1447550E-02 -0.7222317E-03 -0.1630098E-02 -0.7254669E-02 -0.9281380E-04  0.0000000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.1556213E-02 -0.1119448E-02 -0.1284862E-02 -0.8292626E-02 -0.1042175E-03  0.0000000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.1531046E-02 -0.1545055E-02 -0.8663828E-03 -0.9143422E-02 -0.1099483E-03  0.0000000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.1293053E-02 -0.1934854E-02 -0.2661797E-03 -0.9548698E-02 -0.9825853E-04  0.0000000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.7882167E-03 -0.2119332E-02  0.6620921E-03 -0.8996764E-02 -0.5351569E-04  0.0000000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.2510601E-03 -0.1916505E-02  0.1699980E-02 -0.7405085E-02  0.2329817E-05  0.0000000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        " -0.7035759E-04 -0.1489874E-02  0.2473075E-02 -0.5566125E-02  0.3139177E-04  0.0000000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        " -0.1964659E-03 -0.1025600E-02  0.2952443E-02 -0.3855809E-02  0.3488581E-04  0.0000000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        " -0.1875126E-03 -0.6048242E-03  0.3201374E-02 -0.2316738E-02  0.2379278E-04  0.0000000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        " -0.8584418E-04 -0.2653390E-03  0.3274210E-02 -0.9247840E-03  0.6435164E-05  0.0000000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.8003555E-04 -0.2474221E-04  0.3213927E-02  0.3593699E-03 -0.1214855E-04  0.0000000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.2888977E-03  0.1087301E-03  0.3055950E-02  0.1565930E-02 -0.2928845E-04  0.0000000E+00  0.0000000E+00  0.0000000E+00         1         0"
        );
    }

    private List<PhaseSpaceParticle> a五阶dp1432xCOSY() {
        return PhaseSpaceParticles.readFromCosy_PRAY(
                " -0.4735763E-03  0.6806958E-02  0.0000000E+00  0.0000000E+00  0.2647040E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        " -0.1009342E-02  0.7864039E-02  0.0000000E+00  0.0000000E+00  0.2646848E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        " -0.1507626E-02  0.8689069E-02  0.0000000E+00  0.0000000E+00  0.2646695E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        " -0.1978221E-02  0.9251591E-02  0.0000000E+00  0.0000000E+00  0.2646597E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        " -0.2423460E-02  0.9499015E-02  0.0000000E+00  0.0000000E+00  0.2646574E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        " -0.2844961E-02  0.9362748E-02  0.0000000E+00  0.0000000E+00  0.2646642E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        " -0.3234186E-02  0.8735754E-02  0.0000000E+00  0.0000000E+00  0.2646821E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        " -0.3557232E-02  0.7436529E-02  0.0000000E+00  0.0000000E+00  0.2647132E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        " -0.3703429E-02  0.5268156E-02  0.0000000E+00  0.0000000E+00  0.2647557E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        " -0.3541711E-02  0.2746172E-02  0.0000000E+00  0.0000000E+00  0.2647926E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        " -0.3167782E-02  0.5611686E-03  0.0000000E+00  0.0000000E+00  0.2648137E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        " -0.2693344E-02 -0.1253148E-02  0.0000000E+00  0.0000000E+00  0.2648232E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        " -0.2162067E-02 -0.2793454E-02  0.0000000E+00  0.0000000E+00  0.2648252E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        " -0.1592549E-02 -0.4120668E-02  0.0000000E+00  0.0000000E+00  0.2648222E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        " -0.9922018E-03 -0.5273603E-02  0.0000000E+00  0.0000000E+00  0.2648160E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        " -0.3677261E-03 -0.6269444E-02  0.0000000E+00  0.0000000E+00  0.2648078E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.2837667E-03 -0.7127679E-02  0.0000000E+00  0.0000000E+00  0.2647984E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.9550514E-03 -0.7845334E-02  0.0000000E+00  0.0000000E+00  0.2647889E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.1642883E-02 -0.8421544E-02  0.0000000E+00  0.0000000E+00  0.2647798E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.2344997E-02 -0.8850007E-02  0.0000000E+00  0.0000000E+00  0.2647720E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.3046778E-02 -0.9108987E-02  0.0000000E+00  0.0000000E+00  0.2647662E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.3726622E-02 -0.9162104E-02  0.0000000E+00  0.0000000E+00  0.2647632E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.4338100E-02 -0.8936796E-02  0.0000000E+00  0.0000000E+00  0.2647641E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.4780090E-02 -0.8278481E-02  0.0000000E+00  0.0000000E+00  0.2647703E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.4827321E-02 -0.6911447E-02  0.0000000E+00  0.0000000E+00  0.2647827E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.4345380E-02 -0.4950088E-02  0.0000000E+00  0.0000000E+00  0.2647948E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.3618625E-02 -0.2913098E-02  0.0000000E+00  0.0000000E+00  0.2647989E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.2848738E-02 -0.9715938E-03  0.0000000E+00  0.0000000E+00  0.2647945E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.2097087E-02  0.8573887E-03  0.0000000E+00  0.0000000E+00  0.2647832E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.1385084E-02  0.2563590E-02  0.0000000E+00  0.0000000E+00  0.2647667E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.7185620E-03  0.4137321E-02  0.0000000E+00  0.0000000E+00  0.2647468E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.1009856E-03  0.5555972E-02  0.0000000E+00  0.0000000E+00  0.2647253E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0"
        );
    }

    private List<PhaseSpaceParticle> a五阶dp1432yCOSY() {
        return PhaseSpaceParticles.readFromCosy_PRAY(
                "  0.5710445E-03 -0.6179928E-03 -0.3878037E-02  0.5516544E-02  0.2646119E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.9013113E-03 -0.7380436E-03 -0.6071818E-02  0.7822048E-02  0.2644374E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.1229945E-02 -0.9625413E-03 -0.8105016E-02  0.1008840E-01  0.2642196E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.1507626E-02 -0.1291504E-02 -0.9969539E-02  0.1233040E-01  0.2639641E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.1677318E-02 -0.1716962E-02 -0.1162386E-01  0.1449803E-01  0.2636845E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.1685573E-02 -0.2219896E-02 -0.1303033E-01  0.1649083E-01  0.2634050E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.1490715E-02 -0.2751693E-02 -0.1413403E-01  0.1808009E-01  0.2631744E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.1089379E-02 -0.3194464E-02 -0.1482130E-01  0.1877142E-01  0.2630905E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.6017911E-03 -0.3275877E-02 -0.1472288E-01  0.1760997E-01  0.2633139E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.2861122E-03 -0.2831499E-02 -0.1338999E-01  0.1449072E-01  0.2637962E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.1192874E-03 -0.2211638E-02 -0.1120698E-01  0.1092207E-01  0.2642376E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.6522466E-05 -0.1654463E-02 -0.8673819E-02  0.7592698E-02  0.2645427E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        " -0.5353085E-04 -0.1214814E-02 -0.6036137E-02  0.4572926E-02  0.2647233E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        " -0.3533213E-04 -0.8974200E-03 -0.3417982E-02  0.1816544E-02  0.2648032E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.7824501E-04 -0.6955948E-03 -0.8739637E-03 -0.7475239E-03  0.2648025E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.2859736E-03 -0.6037861E-03  0.1558331E-02 -0.3170587E-02  0.2647356E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.5719037E-03 -0.6181765E-03  0.3884215E-02 -0.5522890E-02  0.2646114E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.9013113E-03 -0.7380436E-03  0.6071818E-02 -0.7822048E-02  0.2644374E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.1229945E-02 -0.9625413E-03  0.8105016E-02 -0.1008840E-01  0.2642196E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.1507626E-02 -0.1291504E-02  0.9969539E-02 -0.1233040E-01  0.2639641E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.1677318E-02 -0.1716962E-02  0.1162386E-01 -0.1449803E-01  0.2636845E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.1685573E-02 -0.2219896E-02  0.1303033E-01 -0.1649083E-01  0.2634050E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.1490715E-02 -0.2751693E-02  0.1413403E-01 -0.1808009E-01  0.2631744E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.1089379E-02 -0.3194464E-02  0.1482130E-01 -0.1877142E-01  0.2630905E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.6017911E-03 -0.3275877E-02  0.1472288E-01 -0.1760997E-01  0.2633139E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.2861122E-03 -0.2831499E-02  0.1338999E-01 -0.1449072E-01  0.2637962E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.1192874E-03 -0.2211638E-02  0.1120698E-01 -0.1092207E-01  0.2642376E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.6522466E-05 -0.1654463E-02  0.8673819E-02 -0.7592698E-02  0.2645427E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        " -0.5353085E-04 -0.1214814E-02  0.6036137E-02 -0.4572926E-02  0.2647233E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        " -0.3533213E-04 -0.8974200E-03  0.3417982E-02 -0.1816544E-02  0.2648032E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.7824501E-04 -0.6955948E-03  0.8739637E-03  0.7475239E-03  0.2648025E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.2859736E-03 -0.6037861E-03 -0.1558331E-02  0.3170587E-02  0.2647356E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0"
        );
    }

    private List<PhaseSpaceParticle> a二阶dp1432xCOSY() {
        return PhaseSpaceParticles.readFromCosy_PRAY(
                " -0.2117920E-02  0.6143631E-02  0.0000000E+00  0.0000000E+00  0.2602282E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        " -0.3123502E-02  0.6928030E-02  0.0000000E+00  0.0000000E+00  0.2601717E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        " -0.4021705E-02  0.7504857E-02  0.0000000E+00  0.0000000E+00  0.2601241E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        " -0.4792246E-02  0.7856346E-02  0.0000000E+00  0.0000000E+00  0.2600894E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        " -0.5398698E-02  0.7948281E-02  0.0000000E+00  0.0000000E+00  0.2600723E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        " -0.5802584E-02  0.7732258E-02  0.0000000E+00  0.0000000E+00  0.2600776E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        " -0.5950238E-02  0.7126932E-02  0.0000000E+00  0.0000000E+00  0.2601108E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        " -0.5755544E-02  0.5986323E-02  0.0000000E+00  0.0000000E+00  0.2601792E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        " -0.5095352E-02  0.4146966E-02  0.0000000E+00  0.0000000E+00  0.2602839E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        " -0.4097487E-02  0.2030526E-02  0.0000000E+00  0.0000000E+00  0.2603858E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        " -0.3065379E-02  0.2036600E-03  0.0000000E+00  0.0000000E+00  0.2604526E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        " -0.2065105E-02 -0.1305293E-02  0.0000000E+00  0.0000000E+00  0.2604895E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        " -0.1079452E-02 -0.2574217E-02  0.0000000E+00  0.0000000E+00  0.2605053E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        " -0.9329306E-04 -0.3651726E-02  0.0000000E+00  0.0000000E+00  0.2605054E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.9046487E-03 -0.4568886E-02  0.0000000E+00  0.0000000E+00  0.2604938E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.1914242E-02 -0.5340009E-02  0.0000000E+00  0.0000000E+00  0.2604737E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.2942834E-02 -0.5981541E-02  0.0000000E+00  0.0000000E+00  0.2604475E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.3975801E-02 -0.6493284E-02  0.0000000E+00  0.0000000E+00  0.2604177E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.5000388E-02 -0.6877433E-02  0.0000000E+00  0.0000000E+00  0.2603870E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.6000470E-02 -0.7132295E-02  0.0000000E+00  0.0000000E+00  0.2603577E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.6936711E-02 -0.7245814E-02  0.0000000E+00  0.0000000E+00  0.2603331E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.7753163E-02 -0.7195210E-02  0.0000000E+00  0.0000000E+00  0.2603172E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.8349057E-02 -0.6930614E-02  0.0000000E+00  0.0000000E+00  0.2603157E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.8533487E-02 -0.6337089E-02  0.0000000E+00  0.0000000E+00  0.2603370E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.7971915E-02 -0.5195100E-02  0.0000000E+00  0.0000000E+00  0.2603881E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.6688778E-02 -0.3570993E-02  0.0000000E+00  0.0000000E+00  0.2604445E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.5245132E-02 -0.1862374E-02  0.0000000E+00  0.0000000E+00  0.2604728E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.3861735E-02 -0.2198853E-03  0.0000000E+00  0.0000000E+00  0.2604711E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.2551784E-02  0.1325132E-02  0.0000000E+00  0.0000000E+00  0.2604455E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.1303360E-02  0.2750928E-02  0.0000000E+00  0.0000000E+00  0.2604026E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.1057455E-03  0.4041612E-02  0.0000000E+00  0.0000000E+00  0.2603485E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        " -0.1035716E-02  0.5175658E-02  0.0000000E+00  0.0000000E+00  0.2602887E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0"
        );
    }

    private List<PhaseSpaceParticle> a二阶dp1432yCOSY() {
        return PhaseSpaceParticles.readFromCosy_PRAY(
                "  0.1238150E-02 -0.3480271E-03 -0.4912710E-02  0.5561665E-02  0.2602344E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.1539134E-02 -0.5293594E-03 -0.7553742E-02  0.7905089E-02  0.2600068E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.1829740E-02 -0.8349852E-03 -0.1005196E-01  0.1026908E-01  0.2597125E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.2054489E-02 -0.1264394E-02 -0.1239174E-01  0.1267990E-01  0.2593524E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.2153856E-02 -0.1805435E-02 -0.1450419E-01  0.1508702E-01  0.2589396E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.2080069E-02 -0.2429283E-02 -0.1631293E-01  0.1737087E-01  0.2585066E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.1813933E-02 -0.3064836E-02 -0.1770801E-01  0.1924663E-01  0.2581301E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.1402974E-02 -0.3548194E-02 -0.1849283E-01  0.2009534E-01  0.2579735E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.1041322E-02 -0.3533905E-02 -0.1814400E-01  0.1875434E-01  0.2583065E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.9265406E-03 -0.2886146E-02 -0.1617005E-01  0.1516638E-01  0.2590438E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.8801935E-03 -0.2091920E-02 -0.1323515E-01  0.1119559E-01  0.2596964E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.8005306E-03 -0.1422230E-02 -0.1001731E-01  0.7629406E-02  0.2601273E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.7275292E-03 -0.9181235E-03 -0.6796347E-02  0.4503517E-02  0.2603717E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.7166123E-03 -0.5708324E-03 -0.3680153E-02  0.1723784E-02  0.2604766E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.7994579E-03 -0.3657086E-03 -0.6905266E-03 -0.8225134E-03  0.2604762E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.9793898E-03 -0.2932864E-03  0.2165021E-02 -0.3220584E-02  0.2603920E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.1238935E-02 -0.3483501E-03  0.4920080E-02 -0.5568048E-02  0.2602339E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.1539134E-02 -0.5293594E-03  0.7553742E-02 -0.7905089E-02  0.2600068E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.1829740E-02 -0.8349852E-03  0.1005196E-01 -0.1026908E-01  0.2597125E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.2054489E-02 -0.1264394E-02  0.1239174E-01 -0.1267990E-01  0.2593524E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.2153856E-02 -0.1805435E-02  0.1450419E-01 -0.1508702E-01  0.2589396E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.2080069E-02 -0.2429283E-02  0.1631293E-01 -0.1737087E-01  0.2585066E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.1813933E-02 -0.3064836E-02  0.1770801E-01 -0.1924663E-01  0.2581301E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.1402974E-02 -0.3548194E-02  0.1849283E-01 -0.2009534E-01  0.2579735E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.1041322E-02 -0.3533905E-02  0.1814400E-01 -0.1875434E-01  0.2583065E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.9265406E-03 -0.2886146E-02  0.1617005E-01 -0.1516638E-01  0.2590438E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.8801935E-03 -0.2091920E-02  0.1323515E-01 -0.1119559E-01  0.2596964E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.8005306E-03 -0.1422230E-02  0.1001731E-01 -0.7629406E-02  0.2601273E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.7275292E-03 -0.9181235E-03  0.6796347E-02 -0.4503517E-02  0.2603717E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.7166123E-03 -0.5708324E-03  0.3680153E-02 -0.1723784E-02  0.2604766E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.7994579E-03 -0.3657086E-03  0.6905266E-03  0.8225134E-03  0.2604762E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.9793898E-03 -0.2932864E-03 -0.2165021E-02  0.3220584E-02  0.2603920E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0"
        );
    }

    private List<PhaseSpaceParticle> a九阶dp1432xCOSY() {
        return PhaseSpaceParticles.readFromCosy_PRAY(
                " -0.4785722E-03  0.6804909E-02  0.0000000E+00  0.0000000E+00  0.2646931E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        " -0.1016247E-02  0.7861083E-02  0.0000000E+00  0.0000000E+00  0.2646736E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        " -0.1516386E-02  0.8685223E-02  0.0000000E+00  0.0000000E+00  0.2646580E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        " -0.1988545E-02  0.9246961E-02  0.0000000E+00  0.0000000E+00  0.2646480E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        " -0.2434749E-02  0.9493829E-02  0.0000000E+00  0.0000000E+00  0.2646454E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        " -0.2856313E-02  0.9357360E-02  0.0000000E+00  0.0000000E+00  0.2646522E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        " -0.3244440E-02  0.8730630E-02  0.0000000E+00  0.0000000E+00  0.2646703E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        " -0.3565107E-02  0.7432217E-02  0.0000000E+00  0.0000000E+00  0.2647017E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        " -0.3708025E-02  0.5265124E-02  0.0000000E+00  0.0000000E+00  0.2647448E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        " -0.3543584E-02  0.2744348E-02  0.0000000E+00  0.0000000E+00  0.2647821E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        " -0.3167959E-02  0.5602050E-03  0.0000000E+00  0.0000000E+00  0.2648034E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        " -0.2692321E-02 -0.1253445E-02  0.0000000E+00  0.0000000E+00  0.2648130E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        " -0.2159976E-02 -0.2793151E-02  0.0000000E+00  0.0000000E+00  0.2648150E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        " -0.1589337E-02 -0.4119760E-02  0.0000000E+00  0.0000000E+00  0.2648121E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        " -0.9877162E-03 -0.5272045E-02  0.0000000E+00  0.0000000E+00  0.2648058E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        " -0.3617737E-03 -0.6267176E-02  0.0000000E+00  0.0000000E+00  0.2647974E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.2913974E-03 -0.7124628E-02  0.0000000E+00  0.0000000E+00  0.2647879E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.9645240E-03 -0.7841445E-02  0.0000000E+00  0.0000000E+00  0.2647781E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.1654278E-02 -0.8416791E-02  0.0000000E+00  0.0000000E+00  0.2647688E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.2358265E-02 -0.8844408E-02  0.0000000E+00  0.0000000E+00  0.2647607E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.3061649E-02 -0.9102644E-02  0.0000000E+00  0.0000000E+00  0.2647547E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.3742514E-02 -0.9155234E-02  0.0000000E+00  0.0000000E+00  0.2647515E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.4353980E-02 -0.8929790E-02  0.0000000E+00  0.0000000E+00  0.2647524E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.4794317E-02 -0.8271980E-02  0.0000000E+00  0.0000000E+00  0.2647588E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.4837861E-02 -0.6906292E-02  0.0000000E+00  0.0000000E+00  0.2647716E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.4351904E-02 -0.4946535E-02  0.0000000E+00  0.0000000E+00  0.2647842E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.3622448E-02 -0.2910725E-02  0.0000000E+00  0.0000000E+00  0.2647885E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.2850846E-02 -0.9700525E-03  0.0000000E+00  0.0000000E+00  0.2647843E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.2097917E-02  0.8582621E-03  0.0000000E+00  0.0000000E+00  0.2647730E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.1384720E-02  0.2563830E-02  0.0000000E+00  0.0000000E+00  0.2647564E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.7168767E-03  0.4136882E-02  0.0000000E+00  0.0000000E+00  0.2647363E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.9775966E-04  0.5554773E-02  0.0000000E+00  0.0000000E+00  0.2647146E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0"
        );
    }

    private List<PhaseSpaceParticle> a九阶dp1432yCOSY() {
        return PhaseSpaceParticles.readFromCosy_PRAY(
                "  0.5721110E-03 -0.6176929E-03 -0.3881110E-02  0.5516584E-02  0.2646014E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.9020679E-03 -0.7380305E-03 -0.6076533E-02  0.7822206E-02  0.2644266E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.1230195E-02 -0.9629596E-03 -0.8111696E-02  0.1008893E-01  0.2642082E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.1507087E-02 -0.1292534E-02 -0.9978531E-02  0.1233164E-01  0.2639519E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.1675641E-02 -0.1718804E-02 -0.1163541E-01  0.1450034E-01  0.2636713E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.1682430E-02 -0.2222722E-02 -0.1304446E-01  0.1649451E-01  0.2633906E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.1486081E-02 -0.2755492E-02 -0.1415024E-01  0.1808519E-01  0.2631589E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.1084115E-02 -0.3198724E-02 -0.1483822E-01  0.1877730E-01  0.2630744E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.5980697E-03 -0.3279303E-02 -0.1473777E-01  0.1761490E-01  0.2632987E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.2850260E-03 -0.2833285E-02 -0.1340071E-01  0.1449340E-01  0.2637830E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.1197730E-03 -0.2212263E-02 -0.1121392E-01  0.1092302E-01  0.2642260E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.7654663E-05 -0.1654458E-02 -0.8678013E-02  0.7592724E-02  0.2645319E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        " -0.5218326E-04 -0.1214481E-02 -0.6038380E-02  0.4572590E-02  0.2647129E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        " -0.3394558E-04 -0.8969319E-03 -0.3418758E-02  0.1816176E-02  0.2647930E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.7959147E-04 -0.6950677E-03 -0.8734929E-03 -0.7477702E-03  0.2647923E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.2872206E-03 -0.6033205E-03  0.1560029E-02 -0.3170689E-02  0.2647254E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.5729696E-03 -0.6178771E-03  0.3887292E-02 -0.5522931E-02  0.2646010E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.9020679E-03 -0.7380305E-03  0.6076533E-02 -0.7822206E-02  0.2644266E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.1230195E-02 -0.9629596E-03  0.8111696E-02 -0.1008893E-01  0.2642082E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.1507087E-02 -0.1292534E-02  0.9978531E-02 -0.1233164E-01  0.2639519E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.1675641E-02 -0.1718804E-02  0.1163541E-01 -0.1450034E-01  0.2636713E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.1682430E-02 -0.2222722E-02  0.1304446E-01 -0.1649451E-01  0.2633906E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.1486081E-02 -0.2755492E-02  0.1415024E-01 -0.1808519E-01  0.2631589E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.1084115E-02 -0.3198724E-02  0.1483822E-01 -0.1877730E-01  0.2630744E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.5980697E-03 -0.3279303E-02  0.1473777E-01 -0.1761490E-01  0.2632987E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.2850260E-03 -0.2833285E-02  0.1340071E-01 -0.1449340E-01  0.2637830E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.1197730E-03 -0.2212263E-02  0.1121392E-01 -0.1092302E-01  0.2642260E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.7654663E-05 -0.1654458E-02  0.8678013E-02 -0.7592724E-02  0.2645319E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        " -0.5218326E-04 -0.1214481E-02  0.6038380E-02 -0.4572590E-02  0.2647129E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        " -0.3394558E-04 -0.8969319E-03  0.3418758E-02 -0.1816176E-02  0.2647930E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.7959147E-04 -0.6950677E-03  0.8734929E-03  0.7477702E-03  0.2647923E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.2872206E-03 -0.6033205E-03 -0.1560029E-02  0.3170689E-02  0.2647254E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0"
        );
    }

    private List<PhaseSpaceParticle> a三阶dp1432yCOSY() {
        return PhaseSpaceParticles.readFromCosy_PRAY(
                "  0.4937896E-03 -0.6466225E-03 -0.3744121E-02  0.5514620E-02  0.2651733E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.8317616E-03 -0.7571072E-03 -0.5877371E-02  0.7816551E-02  0.2650083E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.1170299E-02 -0.9688724E-03 -0.7847079E-02  0.1006956E-01  0.2648040E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.1460304E-02 -0.1282247E-02 -0.9647226E-02  0.1228663E-01  0.2645669E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.1644347E-02 -0.1690134E-02 -0.1124117E-01  0.1441775E-01  0.2643105E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.1667680E-02 -0.2175088E-02 -0.1259730E-01  0.1636549E-01  0.2640575E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.1485480E-02 -0.2691601E-02 -0.1366766E-01  0.1790982E-01  0.2638518E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.1087654E-02 -0.3127891E-02 -0.1434699E-01  0.1857575E-01  0.2637802E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.5828676E-03 -0.3221960E-02 -0.1428151E-01  0.1744116E-01  0.2639848E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.2344673E-03 -0.2806792E-02 -0.1302917E-01  0.1439434E-01  0.2644235E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.4346500E-04 -0.2213346E-02 -0.1094310E-01  0.1088780E-01  0.2648285E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        " -0.8074029E-04 -0.1674086E-02 -0.8499296E-02  0.7594182E-02  0.2651116E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        " -0.1442245E-03 -0.1245299E-02 -0.5937365E-02  0.4589118E-02  0.2652807E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        " -0.1253693E-03 -0.9334406E-03 -0.3383194E-02  0.1834007E-02  0.2653560E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        " -0.9028063E-05 -0.7328654E-03 -0.8962490E-03 -0.7355265E-03  0.2653550E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.2029298E-03 -0.6385229E-03  0.1481166E-02 -0.3165258E-02  0.2652911E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.4946671E-03 -0.6467846E-03  0.3750139E-02 -0.5520968E-02  0.2651729E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.8317616E-03 -0.7571072E-03  0.5877371E-02 -0.7816551E-02  0.2650083E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.1170299E-02 -0.9688724E-03  0.7847079E-02 -0.1006956E-01  0.2648040E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.1460304E-02 -0.1282247E-02  0.9647226E-02 -0.1228663E-01  0.2645669E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.1644347E-02 -0.1690134E-02  0.1124117E-01 -0.1441775E-01  0.2643105E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.1667680E-02 -0.2175088E-02  0.1259730E-01 -0.1636549E-01  0.2640575E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.1485480E-02 -0.2691601E-02  0.1366766E-01 -0.1790982E-01  0.2638518E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.1087654E-02 -0.3127891E-02  0.1434699E-01 -0.1857575E-01  0.2637802E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.5828676E-03 -0.3221960E-02  0.1428151E-01 -0.1744116E-01  0.2639848E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.2344673E-03 -0.2806792E-02  0.1302917E-01 -0.1439434E-01  0.2644235E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.4346500E-04 -0.2213346E-02  0.1094310E-01 -0.1088780E-01  0.2648285E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        " -0.8074029E-04 -0.1674086E-02  0.8499296E-02 -0.7594182E-02  0.2651116E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        " -0.1442245E-03 -0.1245299E-02  0.5937365E-02 -0.4589118E-02  0.2652807E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        " -0.1253693E-03 -0.9334406E-03  0.3383194E-02 -0.1834007E-02  0.2653560E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        " -0.9028063E-05 -0.7328654E-03  0.8962490E-03  0.7355265E-03  0.2653550E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.2029298E-03 -0.6385229E-03 -0.1481166E-02  0.3165258E-02  0.2652911E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0"
        );
    }

    private List<PhaseSpaceParticle> a三阶dp1432xCOSY() {
        return PhaseSpaceParticles.readFromCosy_PRAY(
                " -0.2442646E-03  0.6899969E-02  0.0000000E+00  0.0000000E+00  0.2652836E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        " -0.7050888E-03  0.7995966E-02  0.0000000E+00  0.0000000E+00  0.2652728E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        " -0.1135947E-02  0.8857331E-02  0.0000000E+00  0.0000000E+00  0.2652653E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        " -0.1552703E-02  0.9451266E-02  0.0000000E+00  0.0000000E+00  0.2652621E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        " -0.1965362E-02  0.9721947E-02  0.0000000E+00  0.0000000E+00  0.2652639E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        " -0.2383463E-02  0.9597266E-02  0.0000000E+00  0.0000000E+00  0.2652716E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        " -0.2807060E-02  0.8965935E-02  0.0000000E+00  0.0000000E+00  0.2652860E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        " -0.3211119E-02  0.7641149E-02  0.0000000E+00  0.0000000E+00  0.2653080E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        " -0.3484216E-02  0.5422617E-02  0.0000000E+00  0.0000000E+00  0.2653361E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        " -0.3447666E-02  0.2843086E-02  0.0000000E+00  0.0000000E+00  0.2653592E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        " -0.3165018E-02  0.6107998E-03  0.0000000E+00  0.0000000E+00  0.2653714E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        " -0.2759797E-02 -0.1242156E-02  0.0000000E+00  0.0000000E+00  0.2653761E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        " -0.2288183E-02 -0.2816645E-02  0.0000000E+00  0.0000000E+00  0.2653764E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        " -0.1775726E-02 -0.4176162E-02  0.0000000E+00  0.0000000E+00  0.2653741E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        " -0.1233605E-02 -0.5361037E-02  0.0000000E+00  0.0000000E+00  0.2653703E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        " -0.6698267E-03 -0.6389012E-02  0.0000000E+00  0.0000000E+00  0.2653659E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        " -0.8204783E-04 -0.7279958E-02  0.0000000E+00  0.0000000E+00  0.2653616E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.5242494E-03 -0.8030264E-02  0.0000000E+00  0.0000000E+00  0.2653579E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.1148271E-02 -0.8638226E-02  0.0000000E+00  0.0000000E+00  0.2653551E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.1791203E-02 -0.9096335E-02  0.0000000E+00  0.0000000E+00  0.2653536E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.2444178E-02 -0.9380668E-02  0.0000000E+00  0.0000000E+00  0.2653533E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.3093426E-02 -0.9451832E-02  0.0000000E+00  0.0000000E+00  0.2653542E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.3704414E-02 -0.9232524E-02  0.0000000E+00  0.0000000E+00  0.2653562E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.4194445E-02 -0.8560474E-02  0.0000000E+00  0.0000000E+00  0.2653588E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.4356999E-02 -0.7150874E-02  0.0000000E+00  0.0000000E+00  0.2653609E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.4017221E-02 -0.5131567E-02  0.0000000E+00  0.0000000E+00  0.2653605E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.3403087E-02 -0.3044683E-02  0.0000000E+00  0.0000000E+00  0.2653565E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.2717995E-02 -0.1062629E-02  0.0000000E+00  0.0000000E+00  0.2653490E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.2037682E-02  0.8023323E-03  0.0000000E+00  0.0000000E+00  0.2653383E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.1393731E-02  0.2543565E-02  0.0000000E+00  0.0000000E+00  0.2653253E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.7975808E-03  0.4153477E-02  0.0000000E+00  0.0000000E+00  0.2653110E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                        "  0.2539380E-03  0.5609917E-02  0.0000000E+00  0.0000000E+00  0.2652968E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0"
        );
    }

    private List<PhaseSpaceParticle> a() {
        return PhaseSpaceParticles.readFromCosy_PRAY(
                ""
        );
    }

    private CosyArbitraryOrder.CosyMapArbitraryOrder map() {
        return CosyArbitraryOrder.readMap(
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
    }

    private CosyArbitraryOrder.CosyMapArbitraryOrder map9() {
        String string =
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
                        "   3.004751    -0.1682839     0.0000000E+00 0.0000000E+00  1.095465     000005\n" +
                        "  -7369559.      3987389.     0.0000000E+00 0.0000000E+00  3868583.     600000\n" +
                        " -0.4181371E+08-0.1009460E+08 0.0000000E+00 0.0000000E+00  8084637.     510000\n" +
                        " -0.9770714E+08-0.6678179E+08 0.0000000E+00 0.0000000E+00 -3176933.     420000\n" +
                        " -0.1112910E+09-0.8366094E+08 0.0000000E+00 0.0000000E+00-0.1889723E+08 330000\n" +
                        " -0.5925044E+08-0.2145826E+08 0.0000000E+00 0.0000000E+00-0.1639452E+08 240000\n" +
                        " -0.1129968E+08 0.1483450E+08 0.0000000E+00 0.0000000E+00 -5539354.     150000\n" +
                        "   105709.7      5581478.     0.0000000E+00 0.0000000E+00 -666920.8     060000\n" +
                        "  0.0000000E+00 0.0000000E+00 0.1423440E+08 0.3935788E+08 0.0000000E+00 501000\n" +
                        "  0.0000000E+00 0.0000000E+00-0.6604187E+09-0.1583953E+09 0.0000000E+00 411000\n" +
                        "  0.0000000E+00 0.0000000E+00-0.1988044E+10-0.7082701E+09 0.0000000E+00 321000\n" +
                        "  0.0000000E+00 0.0000000E+00-0.1668744E+10-0.7363905E+09 0.0000000E+00 231000\n" +
                        "  0.0000000E+00 0.0000000E+00-0.2569660E+09-0.2330366E+09 0.0000000E+00 141000\n" +
                        "  0.0000000E+00 0.0000000E+00 0.9722526E+08 -4363734.     0.0000000E+00 051000\n" +
                        " -0.4923441E+09-0.2025102E+09 0.0000000E+00 0.0000000E+00-0.2417697E+08 402000\n" +
                        " -0.1151618E+09-0.9383397E+08 0.0000000E+00 0.0000000E+00 0.2150099E+08 312000\n" +
                        "  0.1443450E+10 0.4801594E+09 0.0000000E+00 0.0000000E+00 0.1349055E+09 222000\n" +
                        "  0.1316201E+10 0.4607451E+09 0.0000000E+00 0.0000000E+00 0.1062389E+09 132000\n" +
                        "  0.2993632E+09 0.1062889E+09 0.0000000E+00 0.0000000E+00 0.2179893E+08 042000\n" +
                        "  0.0000000E+00 0.0000000E+00 0.5378777E+08  6024394.     0.0000000E+00 303000\n" +
                        "  0.0000000E+00 0.0000000E+00 0.1263765E+09 0.2378120E+08 0.0000000E+00 213000\n" +
                        "  0.0000000E+00 0.0000000E+00 0.8707855E+08 0.3049515E+08 0.0000000E+00 123000\n" +
                        "  0.0000000E+00 0.0000000E+00 0.1411553E+08 0.1352982E+08 0.0000000E+00 033000\n" +
                        " -0.1771394E+09-0.6005181E+08 0.0000000E+00 0.0000000E+00-0.1600761E+08 204000\n" +
                        " -0.1727102E+09-0.5960552E+08 0.0000000E+00 0.0000000E+00-0.1499041E+08 114000\n" +
                        " -0.4077024E+08-0.1429789E+08 0.0000000E+00 0.0000000E+00 -3024772.     024000\n" +
                        "  0.0000000E+00 0.0000000E+00 0.2250155E+08  3857079.     0.0000000E+00 105000\n" +
                        "  0.0000000E+00 0.0000000E+00  6250619.      6790154.     0.0000000E+00 015000\n" +
                        "   5270519.      1875975.     0.0000000E+00 0.0000000E+00  204879.4     006000\n" +
                        "  0.0000000E+00 0.0000000E+00 0.6807014E+08 0.7505209E+08 0.0000000E+00 500100\n" +
                        "  0.0000000E+00 0.0000000E+00-0.9506540E+09-0.2179901E+09 0.0000000E+00 410100\n" +
                        "  0.0000000E+00 0.0000000E+00-0.2928034E+10-0.1022418E+10 0.0000000E+00 320100\n" +
                        "  0.0000000E+00 0.0000000E+00-0.2204936E+10-0.9939235E+09 0.0000000E+00 230100\n" +
                        "  0.0000000E+00 0.0000000E+00-0.1117773E+09-0.2730837E+09 0.0000000E+00 140100\n" +
                        "  0.0000000E+00 0.0000000E+00 0.1913688E+09  743599.8     0.0000000E+00 050100\n" +
                        " -0.1765179E+10-0.7044283E+09 0.0000000E+00 0.0000000E+00-0.1058421E+09 401100\n" +
                        " -0.8833821E+09-0.4433830E+09 0.0000000E+00 0.0000000E+00 -3402047.     311100\n" +
                        "  0.3320980E+10 0.1130882E+10 0.0000000E+00 0.0000000E+00 0.3085357E+09 221100\n" +
                        "  0.2996940E+10 0.1076223E+10 0.0000000E+00 0.0000000E+00 0.2367204E+09 131100\n" +
                        "  0.6616913E+09 0.2397620E+09 0.0000000E+00 0.0000000E+00 0.4433047E+08 041100\n" +
                        "  0.0000000E+00 0.0000000E+00 0.6081509E+08-0.5609704E+08 0.0000000E+00 302100\n" +
                        "  0.0000000E+00 0.0000000E+00 0.5450701E+09 0.3049542E+08 0.0000000E+00 212100\n" +
                        "  0.0000000E+00 0.0000000E+00 0.8748862E+09 0.2330807E+09 0.0000000E+00 122100\n" +
                        "  0.0000000E+00 0.0000000E+00 0.3023934E+09 0.1284308E+09 0.0000000E+00 032100\n" +
                        " -0.4953514E+09-0.1464226E+09 0.0000000E+00 0.0000000E+00-0.6216422E+08 203100\n" +
                        " -0.5726383E+09-0.1837819E+09 0.0000000E+00 0.0000000E+00-0.6776386E+08 113100\n" +
                        " -0.2261327E+09-0.7776216E+08 0.0000000E+00 0.0000000E+00-0.2221186E+08 023100\n" +
                        "  0.0000000E+00 0.0000000E+00 0.1520455E+09 0.1740944E+08 0.0000000E+00 104100\n" +
                        "  0.0000000E+00 0.0000000E+00 0.1247992E+08 0.5559892E+08 0.0000000E+00 014100\n" +
                        "  0.5949230E+08 0.1991201E+08 0.0000000E+00 0.0000000E+00  1824553.     005100\n" +
                        "   773661.2      819854.2     0.0000000E+00 0.0000000E+00 -398822.7     500001\n" +
                        "  -5255282.     -1242331.     0.0000000E+00 0.0000000E+00 -392011.7     410001\n" +
                        " -0.2325014E+08 -8293116.     0.0000000E+00 0.0000000E+00  1477232.     320001\n" +
                        " -0.2782859E+08 -9967769.     0.0000000E+00 0.0000000E+00  2188986.     230001\n" +
                        " -0.1210879E+08 -4651220.     0.0000000E+00 0.0000000E+00  616411.5     140001\n" +
                        "  -1547638.     -842405.9     0.0000000E+00 0.0000000E+00 -94685.52     050001\n" +
                        "  0.0000000E+00 0.0000000E+00 0.3890707E+08 0.2067227E+08 0.0000000E+00 401001\n" +
                        "  0.0000000E+00 0.0000000E+00 -1341770.     0.2028401E+08 0.0000000E+00 311001\n" +
                        "  0.0000000E+00 0.0000000E+00-0.1267842E+09-0.2362497E+08 0.0000000E+00 221001\n" +
                        "  0.0000000E+00 0.0000000E+00-0.1006831E+09-0.3027336E+08 0.0000000E+00 131001\n" +
                        "  0.0000000E+00 0.0000000E+00-0.1836266E+08 -8362302.     0.0000000E+00 041001\n" +
                        " -0.1807706E+09-0.6866407E+08 0.0000000E+00 0.0000000E+00-0.1276806E+08 302001\n" +
                        " -0.2958083E+09-0.1129850E+09 0.0000000E+00 0.0000000E+00-0.2033246E+08 212001\n" +
                        " -0.1662898E+09-0.6391337E+08 0.0000000E+00 0.0000000E+00-0.1124607E+08 122001\n" +
                        " -0.2674295E+08-0.1041501E+08 0.0000000E+00 0.0000000E+00 -2111662.     032001\n" +
                        "  0.0000000E+00 0.0000000E+00  9045466.      3303024.     0.0000000E+00 203001\n" +
                        "  0.0000000E+00 0.0000000E+00  5994627.      3389609.     0.0000000E+00 113001\n" +
                        "  0.0000000E+00 0.0000000E+00  4573475.      1258880.     0.0000000E+00 023001\n" +
                        "  0.1974390E+08  7430817.     0.0000000E+00 0.0000000E+00  1287628.     104001\n" +
                        "   6320189.      2185913.     0.0000000E+00 0.0000000E+00  655169.1     014001\n" +
                        "  0.0000000E+00 0.0000000E+00  2998678.     -831647.6     0.0000000E+00 005001\n" +
                        " -0.1420411E+10-0.5684678E+09 0.0000000E+00 0.0000000E+00-0.1024253E+09 400200\n" +
                        " -0.8343195E+09-0.3928828E+09 0.0000000E+00 0.0000000E+00-0.4619234E+08 310200\n" +
                        "  0.1778245E+10 0.6319416E+09 0.0000000E+00 0.0000000E+00 0.1549484E+09 220200\n" +
                        "  0.1628810E+10 0.6259927E+09 0.0000000E+00 0.0000000E+00 0.1190472E+09 130200\n" +
                        "  0.3854604E+09 0.1478521E+09 0.0000000E+00 0.0000000E+00 0.2092014E+08 040200\n" +
                        "  0.0000000E+00 0.0000000E+00-0.6180464E+09-0.3828966E+09 0.0000000E+00 301200\n" +
                        "  0.0000000E+00 0.0000000E+00 0.3512913E+09-0.2746990E+09 0.0000000E+00 211200\n" +
                        "  0.0000000E+00 0.0000000E+00 0.2046655E+10 0.4277924E+09 0.0000000E+00 121200\n" +
                        "  0.0000000E+00 0.0000000E+00 0.8117189E+09 0.2769358E+09 0.0000000E+00 031200\n" +
                        " -0.5659668E+08 0.7565931E+08 0.0000000E+00 0.0000000E+00-0.6096206E+08 202200\n" +
                        " -0.8225259E+09-0.2290086E+09 0.0000000E+00 0.0000000E+00-0.1333879E+09 112200\n" +
                        " -0.6435972E+09-0.2239089E+09 0.0000000E+00 0.0000000E+00-0.7063708E+08 022200\n" +
                        "  0.0000000E+00 0.0000000E+00 0.4787749E+09 0.3343579E+08 0.0000000E+00 103200\n" +
                        "  0.0000000E+00 0.0000000E+00-0.5281368E+08 0.1972840E+09 0.0000000E+00 013200\n" +
                        "  0.2136778E+09 0.6381190E+08 0.0000000E+00 0.0000000E+00 -509804.5     004200\n" +
                        "  0.0000000E+00 0.0000000E+00 0.5950432E+08 0.3271902E+08 0.0000000E+00 400101\n" +
                        "  0.0000000E+00 0.0000000E+00 -7466444.     0.3245703E+08 0.0000000E+00 310101\n" +
                        "  0.0000000E+00 0.0000000E+00-0.2213050E+09-0.4243807E+08 0.0000000E+00 220101\n" +
                        "  0.0000000E+00 0.0000000E+00-0.1941800E+09-0.5818065E+08 0.0000000E+00 130101\n" +
                        "  0.0000000E+00 0.0000000E+00-0.4279394E+08-0.1715205E+08 0.0000000E+00 040101\n" +
                        " -0.4994726E+09-0.1895666E+09 0.0000000E+00 0.0000000E+00-0.3344818E+08 301101\n" +
                        " -0.8350154E+09-0.3192098E+09 0.0000000E+00 0.0000000E+00-0.5397309E+08 211101\n" +
                        " -0.4563987E+09-0.1777923E+09 0.0000000E+00 0.0000000E+00-0.2968596E+08 121101\n" +
                        " -0.5159113E+08-0.2171312E+08 0.0000000E+00 0.0000000E+00 -4576105.     031101\n" +
                        "  0.0000000E+00 0.0000000E+00-0.6520872E+08-0.2516982E+08 0.0000000E+00 202101\n" +
                        "  0.0000000E+00 0.0000000E+00-0.7909517E+08-0.2808176E+08 0.0000000E+00 112101\n" +
                        "  0.0000000E+00 0.0000000E+00  3801157.     -7385289.     0.0000000E+00 022101\n" +
                        "  0.1676771E+09 0.6302416E+08 0.0000000E+00 0.0000000E+00 0.1276810E+08 103101\n" +
                        "  0.7066846E+08 0.2539939E+08 0.0000000E+00 0.0000000E+00  7167586.     013101\n" +
                        "  0.0000000E+00 0.0000000E+00 0.3122580E+08 -4781856.     0.0000000E+00 004101\n" +
                        "   198233.8      176465.2     0.0000000E+00 0.0000000E+00  43026.74     400002\n" +
                        "   128645.0      185318.3     0.0000000E+00 0.0000000E+00  83960.26     310002\n" +
                        "  -263878.7     -144357.3     0.0000000E+00 0.0000000E+00 -34653.06     220002\n" +
                        "  -334518.5     -219047.0     0.0000000E+00 0.0000000E+00 -111232.5     130002\n" +
                        "  -115981.5     -58580.90     0.0000000E+00 0.0000000E+00 -36124.74     040002\n" +
                        "  0.0000000E+00 0.0000000E+00  5131009.      2454519.     0.0000000E+00 301002\n" +
                        "  0.0000000E+00 0.0000000E+00  4920470.      3460605.     0.0000000E+00 211002\n" +
                        "  0.0000000E+00 0.0000000E+00 -1549475.      830929.5     0.0000000E+00 121002\n" +
                        "  0.0000000E+00 0.0000000E+00 -1502049.     -188038.3     0.0000000E+00 031002\n" +
                        "  -1345341.     -219524.6     0.0000000E+00 0.0000000E+00 -154054.9     202002\n" +
                        "  -6768385.     -2351341.     0.0000000E+00 0.0000000E+00 -412899.1     112002\n" +
                        "  -5033056.     -1948600.     0.0000000E+00 0.0000000E+00 -265444.0     022002\n" +
                        "  0.0000000E+00 0.0000000E+00 -874911.5     -772642.5     0.0000000E+00 103002\n" +
                        "  0.0000000E+00 0.0000000E+00 -1566791.     -414892.4     0.0000000E+00 013002\n" +
                        "   5588.763      19464.12     0.0000000E+00 0.0000000E+00 -43942.51     004002\n" +
                        "  0.0000000E+00 0.0000000E+00-0.8449793E+09-0.4009226E+09 0.0000000E+00 300300\n" +
                        "  0.0000000E+00 0.0000000E+00-0.1188985E+09-0.3224968E+09 0.0000000E+00 210300\n" +
                        "  0.0000000E+00 0.0000000E+00 0.1473831E+10 0.2537778E+09 0.0000000E+00 120300\n" +
                        "  0.0000000E+00 0.0000000E+00 0.5502117E+09 0.1636390E+09 0.0000000E+00 030300\n" +
                        "  0.5891508E+09 0.3475658E+09 0.0000000E+00 0.0000000E+00  8378187.     201300\n" +
                        " -0.9680750E+09-0.2504963E+09 0.0000000E+00 0.0000000E+00-0.1470840E+09 111300\n" +
                        " -0.9059463E+09-0.3224627E+09 0.0000000E+00 0.0000000E+00-0.9796681E+08 021300\n" +
                        "  0.0000000E+00 0.0000000E+00 0.8626921E+09 0.3365983E+08 0.0000000E+00 102300\n" +
                        "  0.0000000E+00 0.0000000E+00-0.2423605E+09 0.3516608E+09 0.0000000E+00 012300\n" +
                        "  0.3144635E+09 0.6342931E+08 0.0000000E+00 0.0000000E+00-0.3192191E+08 003300\n" +
                        " -0.3589140E+09-0.1338094E+09 0.0000000E+00 0.0000000E+00-0.2180732E+08 300201\n" +
                        " -0.5915653E+09-0.2239557E+09 0.0000000E+00 0.0000000E+00-0.3424260E+08 210201\n" +
                        " -0.2850393E+09-0.1144162E+09 0.0000000E+00 0.0000000E+00-0.1660868E+08 120201\n" +
                        " -0.1853345E+08-0.1031191E+08 0.0000000E+00 0.0000000E+00 -2018880.     030201\n" +
                        "  0.0000000E+00 0.0000000E+00-0.2444234E+09-0.9221445E+08 0.0000000E+00 201201\n" +
                        "  0.0000000E+00 0.0000000E+00-0.2795480E+09-0.1078208E+09 0.0000000E+00 111201\n" +
                        "  0.0000000E+00 0.0000000E+00-0.1949711E+08-0.3324757E+08 0.0000000E+00 021201\n" +
                        "  0.4820948E+09 0.1804340E+09 0.0000000E+00 0.0000000E+00 0.3872498E+08 102201\n" +
                        "  0.2544905E+09 0.9276986E+08 0.0000000E+00 0.0000000E+00 0.2419412E+08 012201\n" +
                        "  0.0000000E+00 0.0000000E+00 0.1267912E+09-0.1037663E+08 0.0000000E+00 003201\n" +
                        "  0.0000000E+00 0.0000000E+00  6961088.      3511760.     0.0000000E+00 300102\n" +
                        "  0.0000000E+00 0.0000000E+00  5932757.      4733215.     0.0000000E+00 210102\n" +
                        "  0.0000000E+00 0.0000000E+00 -2195877.      1119184.     0.0000000E+00 120102\n" +
                        "  0.0000000E+00 0.0000000E+00 -1380950.     -29479.93     0.0000000E+00 030102\n" +
                        "  -2326238.      66688.99     0.0000000E+00 0.0000000E+00 -332834.0     201102\n" +
                        " -0.1812528E+08 -6307706.     0.0000000E+00 0.0000000E+00 -1079595.     111102\n" +
                        " -0.1512767E+08 -5851911.     0.0000000E+00 0.0000000E+00 -747180.2     021102\n" +
                        "  0.0000000E+00 0.0000000E+00 -3819672.     -2690086.     0.0000000E+00 102102\n" +
                        "  0.0000000E+00 0.0000000E+00 -7864590.     -1285367.     0.0000000E+00 012102\n" +
                        "  -3675019.     -1400920.     0.0000000E+00 0.0000000E+00 -481523.5     003102\n" +
                        "   34524.15      12058.67     0.0000000E+00 0.0000000E+00 -5000.186     300003\n" +
                        "   47179.94      16435.66     0.0000000E+00 0.0000000E+00 -5523.844     210003\n" +
                        "  -4596.424      1960.651     0.0000000E+00 0.0000000E+00  3733.601     120003\n" +
                        "  -17551.44     -4389.288     0.0000000E+00 0.0000000E+00  3929.301     030003\n" +
                        "  0.0000000E+00 0.0000000E+00  102752.7      59512.35     0.0000000E+00 201003\n" +
                        "  0.0000000E+00 0.0000000E+00 -17899.96      63261.05     0.0000000E+00 111003\n" +
                        "  0.0000000E+00 0.0000000E+00 -41649.27      31178.13     0.0000000E+00 021003\n" +
                        "   840858.4      346197.8     0.0000000E+00 0.0000000E+00  61244.53     102003\n" +
                        "   511826.3      212991.2     0.0000000E+00 0.0000000E+00  35059.70     012003\n" +
                        "  0.0000000E+00 0.0000000E+00  182286.9      29734.53     0.0000000E+00 003003\n" +
                        "  0.2631842E+09 0.1767137E+09 0.0000000E+00 0.0000000E+00 0.2268010E+08 200400\n" +
                        " -0.5327372E+09-0.1266891E+09 0.0000000E+00 0.0000000E+00-0.6323231E+08 110400\n" +
                        " -0.4216915E+09-0.1523140E+09 0.0000000E+00 0.0000000E+00-0.4356464E+08 020400\n" +
                        "  0.0000000E+00 0.0000000E+00 0.7477160E+09-0.2645551E+08 0.0000000E+00 101400\n" +
                        "  0.0000000E+00 0.0000000E+00-0.4563126E+09 0.2670960E+09 0.0000000E+00 011400\n" +
                        "  0.1490206E+09-0.4512111E+08 0.0000000E+00 0.0000000E+00-0.9065046E+08 002400\n" +
                        "  0.0000000E+00 0.0000000E+00-0.2210487E+09-0.8442678E+08 0.0000000E+00 200301\n" +
                        "  0.0000000E+00 0.0000000E+00-0.2790777E+09-0.1099170E+09 0.0000000E+00 110301\n" +
                        "  0.0000000E+00 0.0000000E+00-0.3962178E+08-0.3678075E+08 0.0000000E+00 020301\n" +
                        "  0.5721041E+09 0.2119201E+09 0.0000000E+00 0.0000000E+00 0.4734818E+08 101301\n" +
                        "  0.3552361E+09 0.1301441E+09 0.0000000E+00 0.0000000E+00 0.3154150E+08 011301\n" +
                        "  0.0000000E+00 0.0000000E+00 0.2573432E+09-0.1160213E+08 0.0000000E+00 002301\n" +
                        "   95285.90      623822.0     0.0000000E+00 0.0000000E+00 -122508.3     200202\n" +
                        " -0.1722780E+08 -6071193.     0.0000000E+00 0.0000000E+00 -1228889.     110202\n" +
                        " -0.1462175E+08 -5489519.     0.0000000E+00 0.0000000E+00 -827383.0     020202\n" +
                        "  0.0000000E+00 0.0000000E+00 -7045932.     -3495694.     0.0000000E+00 101202\n" +
                        "  0.0000000E+00 0.0000000E+00-0.1365471E+08 -1047100.     0.0000000E+00 011202\n" +
                        " -0.1692956E+08 -6711564.     0.0000000E+00 0.0000000E+00 -1560093.     002202\n" +
                        "  0.0000000E+00 0.0000000E+00  122288.0      99544.80     0.0000000E+00 200103\n" +
                        "  0.0000000E+00 0.0000000E+00  49874.05      166775.1     0.0000000E+00 110103\n" +
                        "  0.0000000E+00 0.0000000E+00 -48600.00      60720.49     0.0000000E+00 020103\n" +
                        "   2196551.      922290.1     0.0000000E+00 0.0000000E+00  169480.1     101103\n" +
                        "   1355294.      574911.8     0.0000000E+00 0.0000000E+00  84098.41     011103\n" +
                        "  0.0000000E+00 0.0000000E+00  857655.7      101330.5     0.0000000E+00 002103\n" +
                        "  -288.4746      413.7350     0.0000000E+00 0.0000000E+00  468.4172     200004\n" +
                        "  -130.6450      758.2537     0.0000000E+00 0.0000000E+00  731.6028     110004\n" +
                        "   442.8031      484.7668     0.0000000E+00 0.0000000E+00  98.23027     020004\n" +
                        "  0.0000000E+00 0.0000000E+00  16239.76      6079.204     0.0000000E+00 101004\n" +
                        "  0.0000000E+00 0.0000000E+00  9089.928      2329.025     0.0000000E+00 011004\n" +
                        "  -14917.69     -6164.384     0.0000000E+00 0.0000000E+00 -444.2431     002004\n" +
                        "  0.0000000E+00 0.0000000E+00 0.1649366E+09-0.7439931E+08 0.0000000E+00 100500\n" +
                        "  0.0000000E+00 0.0000000E+00-0.3613530E+09 0.3454508E+08 0.0000000E+00 010500\n" +
                        " -0.7178391E+08-0.1269174E+09 0.0000000E+00 0.0000000E+00-0.1014804E+09 001500\n" +
                        "  0.2186075E+09 0.7915474E+08 0.0000000E+00 0.0000000E+00 0.1910054E+08 100401\n" +
                        "  0.1484606E+09 0.5438573E+08 0.0000000E+00 0.0000000E+00 0.1214357E+08 010401\n" +
                        "  0.0000000E+00 0.0000000E+00 0.2736371E+09 -7026986.     0.0000000E+00 001401\n" +
                        "  0.0000000E+00 0.0000000E+00 -1157196.     -612108.3     0.0000000E+00 100302\n" +
                        "  0.0000000E+00 0.0000000E+00 -4819608.      894158.0     0.0000000E+00 010302\n" +
                        " -0.2493697E+08-0.1004682E+08 0.0000000E+00 0.0000000E+00 -1879437.     001302\n" +
                        "   1517137.      643793.0     0.0000000E+00 0.0000000E+00  144052.3     100203\n" +
                        "   1296457.      516530.6     0.0000000E+00 0.0000000E+00  92794.14     010203\n" +
                        "  0.0000000E+00 0.0000000E+00  1430895.      111462.5     0.0000000E+00 001203\n" +
                        "  0.0000000E+00 0.0000000E+00  16973.51      5858.470     0.0000000E+00 100104\n" +
                        "  0.0000000E+00 0.0000000E+00  7370.630      682.7873     0.0000000E+00 010104\n" +
                        "  -37942.16     -15686.80     0.0000000E+00 0.0000000E+00  203.7287     001104\n" +
                        "   90.23237      1.447987     0.0000000E+00 0.0000000E+00 -42.51237     100005\n" +
                        "   91.60704     -11.42497     0.0000000E+00 0.0000000E+00 -27.05117     010005\n" +
                        "  0.0000000E+00 0.0000000E+00  300.3189      185.9216     0.0000000E+00 001005\n" +
                        " -0.7704369E+08-0.7004316E+08 0.0000000E+00 0.0000000E+00-0.4231637E+08 000600\n" +
                        "  0.0000000E+00 0.0000000E+00 0.1286782E+09 -575213.0     0.0000000E+00 000501\n" +
                        " -0.1044713E+08 -4304121.     0.0000000E+00 0.0000000E+00 -531658.9     000402\n" +
                        "  0.0000000E+00 0.0000000E+00  645301.3      4289.628     0.0000000E+00 000303\n" +
                        "  -48232.55     -17330.19     0.0000000E+00 0.0000000E+00 -1370.022     000204\n" +
                        "  0.0000000E+00 0.0000000E+00  594.7082      621.0282     0.0000000E+00 000105\n" +
                        "  -6.656338     0.4969532     0.0000000E+00 0.0000000E+00-0.4693674     000006\n" +
                        " -0.4746345E+08 0.1216495E+09 0.0000000E+00 0.0000000E+00 0.8482277E+08 700000\n" +
                        "  0.2732375E+08 0.4425616E+08 0.0000000E+00 0.0000000E+00 0.1040390E+09 610000\n" +
                        "  0.1178618E+10-0.7038791E+09 0.0000000E+00 0.0000000E+00-0.4656065E+09 520000\n" +
                        "  0.3837879E+10-0.3371098E+09 0.0000000E+00 0.0000000E+00-0.1114983E+10 430000\n" +
                        "  0.5756110E+10 0.1882307E+10 0.0000000E+00 0.0000000E+00-0.8038309E+09 340000\n" +
                        "  0.4538454E+10 0.2559918E+10 0.0000000E+00 0.0000000E+00-0.1127915E+09 250000\n" +
                        "  0.1788375E+10 0.1093188E+10 0.0000000E+00 0.0000000E+00 0.8716291E+08 160000\n" +
                        "  0.2731578E+09 0.1264994E+09 0.0000000E+00 0.0000000E+00 0.2516067E+08 070000\n" +
                        "  0.0000000E+00 0.0000000E+00-0.3160177E+10-0.6061925E+09 0.0000000E+00 601000\n" +
                        "  0.0000000E+00 0.0000000E+00-0.2098838E+11-0.7606780E+10 0.0000000E+00 511000\n" +
                        "  0.0000000E+00 0.0000000E+00-0.4010562E+11-0.1937880E+11 0.0000000E+00 421000\n" +
                        "  0.0000000E+00 0.0000000E+00-0.1157649E+11-0.1334550E+11 0.0000000E+00 331000\n" +
                        "  0.0000000E+00 0.0000000E+00 0.3417038E+11 0.5820465E+10 0.0000000E+00 241000\n" +
                        "  0.0000000E+00 0.0000000E+00 0.2865000E+11 0.8752808E+10 0.0000000E+00 151000\n" +
                        "  0.0000000E+00 0.0000000E+00 0.5539744E+10 0.2079988E+10 0.0000000E+00 061000\n" +
                        "  0.8065460E+10 0.1925347E+10 0.0000000E+00 0.0000000E+00 0.4797290E+09 502000\n" +
                        "  0.5803707E+11 0.1926572E+11 0.0000000E+00 0.0000000E+00 0.3567760E+10 412000\n" +
                        "  0.1203301E+12 0.4362539E+11 0.0000000E+00 0.0000000E+00 0.7151251E+10 322000\n" +
                        "  0.8646426E+11 0.3286783E+11 0.0000000E+00 0.0000000E+00 0.4494434E+10 232000\n" +
                        "  0.1337012E+11 0.5774747E+10 0.0000000E+00 0.0000000E+00 0.1322082E+08 142000\n" +
                        " -0.3690370E+10-0.1178245E+10 0.0000000E+00 0.0000000E+00-0.4601246E+09 052000\n" +
                        "  0.0000000E+00 0.0000000E+00-0.7817595E+10-0.3803531E+10 0.0000000E+00 403000\n" +
                        "  0.0000000E+00 0.0000000E+00-0.1508791E+11-0.6983979E+10 0.0000000E+00 313000\n" +
                        "  0.0000000E+00 0.0000000E+00-0.1303804E+11-0.4866739E+10 0.0000000E+00 223000\n" +
                        "  0.0000000E+00 0.0000000E+00-0.7561890E+10-0.2088843E+10 0.0000000E+00 133000\n" +
                        "  0.0000000E+00 0.0000000E+00-0.2180351E+10-0.5939749E+09 0.0000000E+00 043000\n" +
                        " -0.6872804E+10-0.2494906E+10 0.0000000E+00 0.0000000E+00-0.7950435E+08 304000\n" +
                        " -0.8491390E+10-0.3177046E+10 0.0000000E+00 0.0000000E+00 0.1060041E+09 214000\n" +
                        " -0.2364790E+10-0.1027993E+10 0.0000000E+00 0.0000000E+00 0.2613249E+09 124000\n" +
                        " -0.1691302E+09-0.1280188E+09 0.0000000E+00 0.0000000E+00 0.6552953E+08 034000\n" +
                        "  0.0000000E+00 0.0000000E+00 0.1483046E+10 0.5194528E+09 0.0000000E+00 205000\n" +
                        "  0.0000000E+00 0.0000000E+00 0.5744506E+09 0.2401187E+09 0.0000000E+00 115000\n" +
                        "  0.0000000E+00 0.0000000E+00-0.2880838E+09-0.1577967E+09 0.0000000E+00 025000\n" +
                        " -0.1237828E+10-0.4313226E+09 0.0000000E+00 0.0000000E+00-0.1123279E+09 106000\n" +
                        " -0.8443046E+09-0.3021238E+09 0.0000000E+00 0.0000000E+00-0.7715850E+08 016000\n" +
                        "  0.0000000E+00 0.0000000E+00 -3261810.    -0.3915838E+08 0.0000000E+00 007000\n" +
                        "  0.0000000E+00 0.0000000E+00-0.4229468E+10-0.7542959E+09 0.0000000E+00 600100\n" +
                        "  0.0000000E+00 0.0000000E+00-0.3219066E+11-0.1188309E+11 0.0000000E+00 510100";

        string = string +
                "  0.0000000E+00 0.0000000E+00-0.6282665E+11-0.3039560E+11 0.0000000E+00 420100\n" +
                "  0.0000000E+00 0.0000000E+00-0.1394592E+11-0.1923878E+11 0.0000000E+00 330100\n" +
                "  0.0000000E+00 0.0000000E+00 0.5878048E+11 0.1079249E+11 0.0000000E+00 240100\n" +
                "  0.0000000E+00 0.0000000E+00 0.4453892E+11 0.1331402E+11 0.0000000E+00 150100\n" +
                "  0.0000000E+00 0.0000000E+00 0.7415700E+10 0.2777876E+10 0.0000000E+00 060100\n" +
                "  0.1022230E+11 0.4987589E+09 0.0000000E+00 0.0000000E+00 0.9576539E+09 501100\n" +
                "  0.1426365E+12 0.4628878E+11 0.0000000E+00 0.0000000E+00 0.1004398E+11 411100\n" +
                "  0.3291439E+12 0.1195215E+12 0.0000000E+00 0.0000000E+00 0.2169302E+11 321100\n" +
                "  0.2312640E+12 0.8821086E+11 0.0000000E+00 0.0000000E+00 0.1360847E+11 231100\n" +
                "  0.2945013E+11 0.1288590E+11 0.0000000E+00 0.0000000E+00 0.1329997E+09 141100\n" +
                " -0.1054586E+11-0.3612287E+10 0.0000000E+00 0.0000000E+00-0.1194623E+10 051100\n" +
                "  0.0000000E+00 0.0000000E+00-0.1770941E+11-0.1053628E+11 0.0000000E+00 402100\n" +
                "  0.0000000E+00 0.0000000E+00-0.1561773E+11-0.1184054E+11 0.0000000E+00 312100\n" +
                "  0.0000000E+00 0.0000000E+00 0.5145192E+10 0.2544272E+10 0.0000000E+00 222100\n" +
                "  0.0000000E+00 0.0000000E+00-0.3032248E+10 0.3396985E+10 0.0000000E+00 132100\n" +
                "  0.0000000E+00 0.0000000E+00-0.6743365E+10-0.8990790E+09 0.0000000E+00 042100\n" +
                " -0.3097628E+11-0.1031372E+11 0.0000000E+00 0.0000000E+00-0.1027381E+10 303100\n" +
                " -0.4379865E+11-0.1523863E+11 0.0000000E+00 0.0000000E+00-0.7044341E+09 213100\n" +
                " -0.2868296E+11-0.1106993E+11 0.0000000E+00 0.0000000E+00-0.2954459E+09 123100\n" +
                " -0.9224870E+10-0.3768282E+10 0.0000000E+00 0.0000000E+00-0.2957285E+09 033100\n" +
                "  0.0000000E+00 0.0000000E+00 0.1392001E+11 0.5153579E+10 0.0000000E+00 204100\n" +
                "  0.0000000E+00 0.0000000E+00 0.4296936E+10 0.2404366E+10 0.0000000E+00 114100\n" +
                "  0.0000000E+00 0.0000000E+00-0.3002582E+10-0.1538405E+10 0.0000000E+00 024100\n" +
                " -0.8092570E+10-0.2829229E+10 0.0000000E+00 0.0000000E+00-0.7154311E+09 105100\n" +
                " -0.6880617E+10-0.2488613E+10 0.0000000E+00 0.0000000E+00-0.6095923E+09 015100\n" +
                "  0.0000000E+00 0.0000000E+00-0.5669519E+08-0.5849645E+09 0.0000000E+00 006100\n" +
                " -0.5986744E+08 -6806969.     0.0000000E+00 0.0000000E+00 -4261977.     600001\n" +
                " -0.3715424E+09-0.1165803E+09 0.0000000E+00 0.0000000E+00  6211536.     510001\n" +
                " -0.8426352E+09-0.3190865E+09 0.0000000E+00 0.0000000E+00 0.7056722E+08 420001\n" +
                " -0.7553025E+09-0.3002504E+09 0.0000000E+00 0.0000000E+00 0.1055040E+09 330001\n" +
                " -0.1080606E+09-0.7014758E+08 0.0000000E+00 0.0000000E+00 0.4017181E+08 240001\n" +
                "  0.1731190E+09 0.3382591E+08 0.0000000E+00 0.0000000E+00 -9964806.     150001\n" +
                "  0.5884088E+08 0.1332816E+08 0.0000000E+00 0.0000000E+00 -5262490.     060001\n" +
                "  0.0000000E+00 0.0000000E+00 0.2351461E+08 0.1400524E+09 0.0000000E+00 501001\n" +
                "  0.0000000E+00 0.0000000E+00-0.2517330E+10-0.7479480E+09 0.0000000E+00 411001\n" +
                "  0.0000000E+00 0.0000000E+00-0.6455278E+10-0.2664889E+10 0.0000000E+00 321001\n" +
                "  0.0000000E+00 0.0000000E+00-0.4628512E+10-0.2405819E+10 0.0000000E+00 231001\n" +
                "  0.0000000E+00 0.0000000E+00-0.4956828E+09-0.6525716E+09 0.0000000E+00 141001\n" +
                "  0.0000000E+00 0.0000000E+00 0.2656211E+09 -5492693.     0.0000000E+00 051001\n" +
                " -0.4518298E+10-0.1868135E+10 0.0000000E+00 0.0000000E+00-0.2328350E+09 402001\n" +
                " -0.3446772E+10-0.1657565E+10 0.0000000E+00 0.0000000E+00-0.5202988E+08 312001\n" +
                "  0.7128375E+10 0.2420165E+10 0.0000000E+00 0.0000000E+00 0.6744302E+09 222001\n" +
                "  0.8248238E+10 0.3043998E+10 0.0000000E+00 0.0000000E+00 0.6550083E+09 132001\n" +
                "  0.2325235E+10 0.8771538E+09 0.0000000E+00 0.0000000E+00 0.1771259E+09 042001\n" +
                "  0.0000000E+00 0.0000000E+00 0.1344037E+10 0.5146794E+09 0.0000000E+00 303001\n" +
                "  0.0000000E+00 0.0000000E+00 0.1946926E+10 0.7245049E+09 0.0000000E+00 213001\n" +
                "  0.0000000E+00 0.0000000E+00 0.1136974E+10 0.4058468E+09 0.0000000E+00 123001\n" +
                "  0.0000000E+00 0.0000000E+00 0.3140833E+09 0.1095825E+09 0.0000000E+00 033001\n" +
                " -0.8311685E+09-0.2936558E+09 0.0000000E+00 0.0000000E+00-0.1308289E+09 204001\n" +
                " -0.5253259E+09-0.1888074E+09 0.0000000E+00 0.0000000E+00-0.9331161E+08 114001\n" +
                " -0.6147704E+08-0.2035656E+08 0.0000000E+00 0.0000000E+00 -8371759.     024001\n" +
                "  0.0000000E+00 0.0000000E+00 0.1393701E+09 0.1590815E+08 0.0000000E+00 105001\n" +
                "  0.0000000E+00 0.0000000E+00 0.1352061E+08 0.5436798E+08 0.0000000E+00 015001\n" +
                "  0.1201911E+09 0.4482340E+08 0.0000000E+00 0.0000000E+00  8067676.     006001\n" +
                " -0.2521864E+10-0.3663462E+10 0.0000000E+00 0.0000000E+00-0.3873401E+08 500200\n" +
                "  0.8520012E+11 0.2642002E+11 0.0000000E+00 0.0000000E+00 0.5964756E+10 410200\n" +
                "  0.2213580E+12 0.8091756E+11 0.0000000E+00 0.0000000E+00 0.1521726E+11 320200\n" +
                "  0.1498398E+12 0.5849541E+11 0.0000000E+00 0.0000000E+00 0.9629269E+10 230200\n" +
                "  0.1759233E+11 0.8083002E+10 0.0000000E+00 0.0000000E+00 0.3372301E+09 140200\n" +
                " -0.5959196E+10-0.2193804E+10 0.0000000E+00 0.0000000E+00-0.6467207E+09 050200\n" +
                "  0.0000000E+00 0.0000000E+00-0.1118495E+11-0.1100056E+11 0.0000000E+00 401200\n" +
                "  0.0000000E+00 0.0000000E+00 0.3598137E+11 0.1612165E+10 0.0000000E+00 311200\n" +
                "  0.0000000E+00 0.0000000E+00 0.1025734E+12 0.3740737E+11 0.0000000E+00 221200\n" +
                "  0.0000000E+00 0.0000000E+00 0.4467690E+11 0.2455063E+11 0.0000000E+00 131200\n" +
                "  0.0000000E+00 0.0000000E+00-0.7134358E+10 0.9398159E+09 0.0000000E+00 041200\n" +
                " -0.3582263E+11-0.9026413E+10 0.0000000E+00 0.0000000E+00-0.2397727E+10 302200\n" +
                " -0.7547149E+11-0.2400757E+11 0.0000000E+00 0.0000000E+00-0.3079650E+10 212200\n" +
                " -0.9196390E+11-0.3500981E+11 0.0000000E+00 0.0000000E+00-0.3945944E+10 122200\n" +
                " -0.3350203E+11-0.1350389E+11 0.0000000E+00 0.0000000E+00-0.1669594E+10 032200\n" +
                "  0.0000000E+00 0.0000000E+00 0.4807456E+11 0.1799376E+11 0.0000000E+00 203200\n" +
                "  0.0000000E+00 0.0000000E+00 0.7549892E+10 0.7190971E+10 0.0000000E+00 113200\n" +
                "  0.0000000E+00 0.0000000E+00-0.1456240E+11-0.6964252E+10 0.0000000E+00 023200\n" +
                " -0.1871863E+11-0.6772312E+10 0.0000000E+00 0.0000000E+00-0.1180815E+10 104200\n" +
                " -0.2341162E+11-0.8644034E+10 0.0000000E+00 0.0000000E+00-0.1770121E+10 014200\n" +
                "  0.0000000E+00 0.0000000E+00 0.5827663E+08-0.3573341E+10 0.0000000E+00 005200\n" +
                "  0.0000000E+00 0.0000000E+00-0.5145133E+08 0.1979686E+09 0.0000000E+00 500101\n" +
                "  0.0000000E+00 0.0000000E+00-0.4334356E+10-0.1302297E+10 0.0000000E+00 410101\n" +
                "  0.0000000E+00 0.0000000E+00-0.1096417E+11-0.4541598E+10 0.0000000E+00 320101\n" +
                "  0.0000000E+00 0.0000000E+00-0.8508644E+10-0.4318242E+10 0.0000000E+00 230101\n" +
                "  0.0000000E+00 0.0000000E+00-0.1542031E+10-0.1337874E+10 0.0000000E+00 140101\n" +
                "  0.0000000E+00 0.0000000E+00 0.3460077E+09-0.4469947E+08 0.0000000E+00 050101\n" +
                " -0.1359675E+11-0.5665074E+10 0.0000000E+00 0.0000000E+00-0.7069400E+09 401101\n" +
                " -0.9681466E+10-0.4844233E+10 0.0000000E+00 0.0000000E+00-0.1666011E+09 311101\n" +
                "  0.2205315E+11 0.7343646E+10 0.0000000E+00 0.0000000E+00 0.1919478E+10 221101\n" +
                "  0.2559040E+11 0.9339521E+10 0.0000000E+00 0.0000000E+00 0.1901749E+10 131101\n" +
                "  0.7324559E+10 0.2746686E+10 0.0000000E+00 0.0000000E+00 0.5281452E+09 041101\n" +
                "  0.0000000E+00 0.0000000E+00 0.2786638E+10 0.8882314E+09 0.0000000E+00 302101\n" +
                "  0.0000000E+00 0.0000000E+00 0.4890702E+10 0.1361328E+10 0.0000000E+00 212101\n" +
                "  0.0000000E+00 0.0000000E+00 0.5854993E+10 0.1735372E+10 0.0000000E+00 122101\n" +
                "  0.0000000E+00 0.0000000E+00 0.2451715E+10 0.7646615E+09 0.0000000E+00 032101\n" +
                "  0.2269526E+09 0.2182774E+09 0.0000000E+00 0.0000000E+00-0.3725524E+09 203101\n" +
                "  0.1187770E+10 0.5404147E+09 0.0000000E+00 0.0000000E+00-0.2209212E+09 113101\n" +
                "  0.2768997E+09 0.1425490E+09 0.0000000E+00 0.0000000E+00 -1716649.     023101\n" +
                "  0.0000000E+00 0.0000000E+00 0.9005980E+09-0.2542205E+08 0.0000000E+00 104101\n" +
                "  0.0000000E+00 0.0000000E+00-0.1661125E+09 0.3833324E+09 0.0000000E+00 014101\n" +
                "  0.1027623E+10 0.3829264E+09 0.0000000E+00 0.0000000E+00 0.6862549E+08 005101\n" +
                "   2428377.      3090712.     0.0000000E+00 0.0000000E+00  861975.1     500002\n" +
                "  -2377968.      565433.1     0.0000000E+00 0.0000000E+00  1123837.     410002\n" +
                "  -9717634.    -0.1053141E+08 0.0000000E+00 0.0000000E+00 -3952381.     320002\n" +
                "  -662500.3     -8982282.     0.0000000E+00 0.0000000E+00 -7358758.     230002\n" +
                "   5279791.      22150.55     0.0000000E+00 0.0000000E+00 -2996460.     140002\n" +
                "   1993984.      1236924.     0.0000000E+00 0.0000000E+00  38814.96     050002\n" +
                "  0.0000000E+00 0.0000000E+00 0.3582657E+08 0.3196564E+08 0.0000000E+00 401002\n" +
                "  0.0000000E+00 0.0000000E+00-0.1311776E+09-0.1886092E+08 0.0000000E+00 311002\n" +
                "  0.0000000E+00 0.0000000E+00-0.3520566E+09-0.1392390E+09 0.0000000E+00 221002\n" +
                "  0.0000000E+00 0.0000000E+00-0.2172969E+09-0.1094207E+09 0.0000000E+00 131002\n" +
                "  0.0000000E+00 0.0000000E+00-0.3156746E+08-0.2032593E+08 0.0000000E+00 041002\n" +
                " -0.6264903E+09-0.2522474E+09 0.0000000E+00 0.0000000E+00-0.3937267E+08 302002\n" +
                " -0.1116541E+10-0.4535490E+09 0.0000000E+00 0.0000000E+00-0.7012678E+08 212002\n" +
                " -0.6977274E+09-0.2895259E+09 0.0000000E+00 0.0000000E+00-0.4474169E+08 122002\n" +
                " -0.1351449E+09-0.5774425E+08 0.0000000E+00 0.0000000E+00 -9410566.     032002\n" +
                "  0.0000000E+00 0.0000000E+00 0.6623421E+08 0.2785673E+08 0.0000000E+00 203002\n" +
                "  0.0000000E+00 0.0000000E+00 0.7778939E+08 0.4545447E+08 0.0000000E+00 113002\n" +
                "  0.0000000E+00 0.0000000E+00 0.3682237E+08 0.2231133E+08 0.0000000E+00 023002\n" +
                "  0.4096597E+08 0.1849198E+08 0.0000000E+00 0.0000000E+00  2383190.     104002\n" +
                " -0.1536350E+08 -4796004.     0.0000000E+00 0.0000000E+00 -1861985.     014002\n" +
                "  0.0000000E+00 0.0000000E+00 0.1743303E+08 -1899588.     0.0000000E+00 005002\n" +
                "  0.0000000E+00 0.0000000E+00-0.4808449E+10-0.6064766E+10 0.0000000E+00 400300\n" +
                "  0.0000000E+00 0.0000000E+00 0.3903166E+11 0.6230237E+10 0.0000000E+00 310300\n" +
                "  0.0000000E+00 0.0000000E+00 0.1002050E+12 0.3482820E+11 0.0000000E+00 220300\n" +
                "  0.0000000E+00 0.0000000E+00 0.4570930E+11 0.2105508E+11 0.0000000E+00 130300\n" +
                "  0.0000000E+00 0.0000000E+00-0.4853001E+10 0.7605314E+09 0.0000000E+00 040300\n" +
                "  0.3796904E+10 0.7205545E+10 0.0000000E+00 0.0000000E+00-0.1183182E+10 301300\n" +
                " -0.5217381E+11-0.1414374E+11 0.0000000E+00 0.0000000E+00-0.2997566E+10 211300\n" +
                " -0.1064358E+12-0.4147979E+11 0.0000000E+00 0.0000000E+00-0.5501675E+10 121300\n" +
                " -0.3651110E+11-0.1552839E+11 0.0000000E+00 0.0000000E+00-0.1750374E+10 031300\n" +
                "  0.0000000E+00 0.0000000E+00 0.8699428E+11 0.3203337E+11 0.0000000E+00 202300\n" +
                "  0.0000000E+00 0.0000000E+00 0.5336453E+09 0.1024837E+11 0.0000000E+00 112300\n" +
                "  0.0000000E+00 0.0000000E+00-0.3480962E+11-0.1592727E+11 0.0000000E+00 022300\n" +
                " -0.1142749E+11-0.5369989E+10 0.0000000E+00 0.0000000E+00 0.1589879E+10 103300\n" +
                " -0.3882823E+11-0.1492403E+11 0.0000000E+00 0.0000000E+00-0.1766913E+10 013300\n" +
                "  0.0000000E+00 0.0000000E+00 0.2604937E+10-0.1178340E+11 0.0000000E+00 004300\n" +
                " -0.1062295E+11-0.4391121E+10 0.0000000E+00 0.0000000E+00-0.5051896E+09 400201\n" +
                " -0.7884154E+10-0.3835048E+10 0.0000000E+00 0.0000000E+00-0.1049459E+09 310201\n" +
                "  0.1669901E+11 0.5450502E+10 0.0000000E+00 0.0000000E+00 0.1383535E+10 220201\n" +
                "  0.1969495E+11 0.7056612E+10 0.0000000E+00 0.0000000E+00 0.1366224E+10 130201\n" +
                "  0.5340172E+10 0.1987263E+10 0.0000000E+00 0.0000000E+00 0.3596352E+09 040201\n" +
                "  0.0000000E+00 0.0000000E+00 0.2609848E+09-0.5236769E+09 0.0000000E+00 301201\n" +
                "  0.0000000E+00 0.0000000E+00 0.4727425E+10 0.3249073E+09 0.0000000E+00 211201\n" +
                "  0.0000000E+00 0.0000000E+00 0.1251454E+11 0.3398624E+10 0.0000000E+00 121201\n" +
                "  0.0000000E+00 0.0000000E+00 0.6108850E+10 0.1824580E+10 0.0000000E+00 031201\n" +
                "  0.1251834E+11 0.5036442E+10 0.0000000E+00 0.0000000E+00 0.7966401E+08 202201\n" +
                "  0.1200266E+11 0.4820674E+10 0.0000000E+00 0.0000000E+00 0.1648187E+09 112201\n" +
                "  0.1800473E+10 0.8345395E+09 0.0000000E+00 0.0000000E+00 0.7659890E+08 022201\n" +
                "  0.0000000E+00 0.0000000E+00 0.4085812E+10 0.1559043E+09 0.0000000E+00 103201\n" +
                "  0.0000000E+00 0.0000000E+00-0.4779432E+09 0.1457761E+10 0.0000000E+00 013201\n" +
                "  0.2991347E+10 0.1115090E+10 0.0000000E+00 0.0000000E+00 0.1885096E+09 004201\n" +
                "  0.0000000E+00 0.0000000E+00 0.4571215E+08 0.4234595E+08 0.0000000E+00 400102\n" +
                "  0.0000000E+00 0.0000000E+00-0.1818690E+09-0.3850215E+08 0.0000000E+00 310102\n" +
                "  0.0000000E+00 0.0000000E+00-0.4182860E+09-0.1966796E+09 0.0000000E+00 220102\n" +
                "  0.0000000E+00 0.0000000E+00-0.1949476E+09-0.1336168E+09 0.0000000E+00 130102\n" +
                "  0.0000000E+00 0.0000000E+00 -4928877.    -0.1628349E+08 0.0000000E+00 040102\n" +
                " -0.1924951E+10-0.7618141E+09 0.0000000E+00 0.0000000E+00-0.1256986E+09 301102\n" +
                " -0.3418319E+10-0.1367371E+10 0.0000000E+00 0.0000000E+00-0.2225712E+09 211102\n" +
                " -0.2147329E+10-0.8764358E+09 0.0000000E+00 0.0000000E+00-0.1414538E+09 121102\n" +
                " -0.4428278E+09-0.1818753E+09 0.0000000E+00 0.0000000E+00-0.3022412E+08 031102\n" +
                "  0.0000000E+00 0.0000000E+00 0.1757299E+09 0.4529968E+08 0.0000000E+00 202102\n" +
                "  0.0000000E+00 0.0000000E+00 0.2070100E+09 0.1185680E+09 0.0000000E+00 112102\n" +
                "  0.0000000E+00 0.0000000E+00 0.1192943E+09 0.7700641E+08 0.0000000E+00 022102\n" +
                "  0.3809848E+09 0.1625164E+09 0.0000000E+00 0.0000000E+00 0.2763774E+08 103102\n" +
                "  0.3227901E+08 0.1876499E+08 0.0000000E+00 0.0000000E+00 -3701020.     013102\n" +
                "  0.0000000E+00 0.0000000E+00 0.1809680E+09 -620637.3     0.0000000E+00 004102\n" +
                "  -205343.0     -27394.87     0.0000000E+00 0.0000000E+00 -143513.8     400003\n" +
                "  -1882332.     -667609.2     0.0000000E+00 0.0000000E+00 -238564.7     310003\n" +
                "  -3780140.     -1303353.     0.0000000E+00 0.0000000E+00  101302.4     220003\n" +
                "  -2535726.     -823375.6     0.0000000E+00 0.0000000E+00  255210.7     130003\n" +
                "  -451078.4     -152839.3     0.0000000E+00 0.0000000E+00  65833.92     040003\n" +
                "  0.0000000E+00 0.0000000E+00  2321713.      2157851.     0.0000000E+00 301003\n" +
                "  0.0000000E+00 0.0000000E+00 -6249422.     -367210.7     0.0000000E+00 211003\n" +
                "  0.0000000E+00 0.0000000E+00 -8721390.     -3283907.     0.0000000E+00 121003\n" +
                "  0.0000000E+00 0.0000000E+00 -1459206.     -1218551.     0.0000000E+00 031003\n" +
                "  -757103.9      381848.6     0.0000000E+00 0.0000000E+00 -310037.4     202003\n" +
                "  -7620120.     -3018826.     0.0000000E+00 0.0000000E+00 -766195.7     112003\n" +
                "  -8013786.     -3483752.     0.0000000E+00 0.0000000E+00 -655043.4     022003\n" +
                "  0.0000000E+00 0.0000000E+00 -7830429.     -4960655.     0.0000000E+00 103003\n" +
                "  0.0000000E+00 0.0000000E+00 -8984645.     -3892408.     0.0000000E+00 013003\n" +
                "   2563045.      959754.1     0.0000000E+00 0.0000000E+00  223290.2     004003\n" +
                "  0.1330800E+11 0.7971391E+10 0.0000000E+00 0.0000000E+00 0.3282609E+09 300400\n" +
                " -0.1651656E+11-0.3278680E+10 0.0000000E+00 0.0000000E+00-0.7459725E+09 210400\n" +
                " -0.3736306E+11-0.1535487E+11 0.0000000E+00 0.0000000E+00-0.1667150E+10 120400\n" +
                " -0.1011087E+11-0.5146692E+10 0.0000000E+00 0.0000000E+00-0.2219601E+09 030400\n" +
                "  0.0000000E+00 0.0000000E+00 0.8840030E+11 0.3131578E+11 0.0000000E+00 201400\n" +
                "  0.0000000E+00 0.0000000E+00-0.7802604E+10 0.7559238E+10 0.0000000E+00 111400\n" +
                "  0.0000000E+00 0.0000000E+00-0.4058108E+11-0.1812616E+11 0.0000000E+00 021400\n" +
                "  0.2347411E+11 0.4865108E+10 0.0000000E+00 0.0000000E+00 0.7570923E+10 102400\n" +
                " -0.2628099E+11-0.1139377E+11 0.0000000E+00 0.0000000E+00 0.1317163E+10 012400\n" +
                "  0.0000000E+00 0.0000000E+00 0.1196100E+11-0.2272985E+11 0.0000000E+00 003400\n" +
                "  0.0000000E+00 0.0000000E+00-0.1547243E+10-0.1194720E+10 0.0000000E+00 300301\n" +
                "  0.0000000E+00 0.0000000E+00 0.1348953E+10-0.7964434E+09 0.0000000E+00 210301\n" +
                "  0.0000000E+00 0.0000000E+00 0.8788271E+10 0.2211814E+10 0.0000000E+00 120301\n" +
                "  0.0000000E+00 0.0000000E+00 0.4988281E+10 0.1431238E+10 0.0000000E+00 030301\n" +
                "  0.2543886E+11 0.9812808E+10 0.0000000E+00 0.0000000E+00 0.9594153E+09 201301\n" +
                "  0.2062472E+11 0.8100587E+10 0.0000000E+00 0.0000000E+00 0.6491949E+09 111301\n" +
                "  0.1929572E+10 0.9989189E+09 0.0000000E+00 0.0000000E+00 0.5697612E+08 021301\n" +
                "  0.0000000E+00 0.0000000E+00 0.1134893E+11 0.1701782E+10 0.0000000E+00 102301\n" +
                "  0.0000000E+00 0.0000000E+00 0.9168875E+09 0.3373856E+10 0.0000000E+00 012301\n" +
                "  0.2789623E+10 0.1053580E+10 0.0000000E+00 0.0000000E+00 0.1428776E+09 003301\n" +
                " -0.1415111E+10-0.5598285E+09 0.0000000E+00 0.0000000E+00-0.1010396E+09 300202\n" +
                " -0.2475396E+10-0.9924725E+09 0.0000000E+00 0.0000000E+00-0.1722040E+09 210202\n" +
                " -0.1654266E+10-0.6635697E+09 0.0000000E+00 0.0000000E+00-0.1142750E+09 120202\n" +
                " -0.3467565E+09-0.1363377E+09 0.0000000E+00 0.0000000E+00-0.2208839E+08 030202\n" +
                "  0.0000000E+00 0.0000000E+00-0.2652520E+08-0.1035586E+09 0.0000000E+00 201202\n" +
                "  0.0000000E+00 0.0000000E+00-0.1214631E+09-0.4129638E+08 0.0000000E+00 111202\n" +
                "  0.0000000E+00 0.0000000E+00 0.3124524E+08 0.6052865E+08 0.0000000E+00 021202\n" +
                "  0.1161188E+10 0.4760940E+09 0.0000000E+00 0.0000000E+00 0.9711156E+08 102202\n" +
                "  0.3947257E+09 0.1638497E+09 0.0000000E+00 0.0000000E+00 0.1106671E+08 012202\n" +
                "  0.0000000E+00 0.0000000E+00 0.6781088E+09 0.2255576E+08 0.0000000E+00 003202\n" +
                "  0.0000000E+00 0.0000000E+00 -138843.7      1980940.     0.0000000E+00 300103\n" +
                "  0.0000000E+00 0.0000000E+00-0.1535603E+08 -2982661.     0.0000000E+00 210103\n" +
                "  0.0000000E+00 0.0000000E+00-0.1620315E+08 -6825440.     0.0000000E+00 120103\n" +
                "  0.0000000E+00 0.0000000E+00 -4005473.     -3204757.     0.0000000E+00 030103\n" +
                "   8746008.      4706262.     0.0000000E+00 0.0000000E+00 -15042.15     201103\n" +
                " -0.1910021E+08 -8179618.     0.0000000E+00 0.0000000E+00 -2211852.     111103\n" +
                " -0.2215128E+08 -9888699.     0.0000000E+00 0.0000000E+00 -2235673.     021103\n" +
                "  0.0000000E+00 0.0000000E+00-0.3011623E+08-0.2059266E+08 0.0000000E+00 102103\n" +
                "  0.0000000E+00 0.0000000E+00-0.3772769E+08-0.1786154E+08 0.0000000E+00 012103\n" +
                "   2870281.      650904.8     0.0000000E+00 0.0000000E+00  825134.3     003103\n" +
                "  -22790.79      7547.320     0.0000000E+00 0.0000000E+00  13494.33     300004\n" +
                "  -94916.07     -9991.082     0.0000000E+00 0.0000000E+00  19363.71     210004\n" +
                "  -70096.14     -22368.05     0.0000000E+00 0.0000000E+00 -4940.648     120004\n" +
                "  -8528.008     -6369.174     0.0000000E+00 0.0000000E+00 -10320.08     030004\n" +
                "  0.0000000E+00 0.0000000E+00  303545.6      195987.8     0.0000000E+00 201004\n" +
                "  0.0000000E+00 0.0000000E+00  308567.8      236546.0     0.0000000E+00 111004\n" +
                "  0.0000000E+00 0.0000000E+00 -47270.03     -1045.971     0.0000000E+00 021004\n" +
                "   546336.7      341108.8     0.0000000E+00 0.0000000E+00  30622.41     102004\n" +
                "   143992.3      132778.4     0.0000000E+00 0.0000000E+00 -4521.035     012004\n" +
                "  0.0000000E+00 0.0000000E+00  579426.8      214387.2     0.0000000E+00 003004\n" +
                "  0.0000000E+00 0.0000000E+00 0.3892856E+11 0.1295286E+11 0.0000000E+00 200500\n" +
                "  0.0000000E+00 0.0000000E+00-0.6778621E+10 0.1274760E+10 0.0000000E+00 110500\n" +
                "  0.0000000E+00 0.0000000E+00-0.1927101E+11-0.8556791E+10 0.0000000E+00 020500\n" +
                "  0.4209197E+11 0.1083300E+11 0.0000000E+00 0.0000000E+00 0.8969869E+10 101500\n" +
                "  0.1061812E+10-0.1627252E+10 0.0000000E+00 0.0000000E+00 0.3779802E+10 011500\n" +
                "  0.0000000E+00 0.0000000E+00 0.2542247E+11-0.2532786E+11 0.0000000E+00 002500\n" +
                "  0.1506412E+11 0.5595861E+10 0.0000000E+00 0.0000000E+00 0.6779957E+09 200401\n" +
                "  0.9803375E+10 0.3748796E+10 0.0000000E+00 0.0000000E+00 0.2694292E+09 110401\n" +
                " -0.3153094E+09 0.5218449E+08 0.0000000E+00 0.0000000E+00-0.8828789E+08 020401\n" +
                "  0.0000000E+00 0.0000000E+00 0.1588586E+11 0.3605835E+10 0.0000000E+00 101401\n" +
                "  0.0000000E+00 0.0000000E+00 0.4220289E+10 0.4394303E+10 0.0000000E+00 011401\n" +
                " -0.2131943E+10-0.7283533E+09 0.0000000E+00 0.0000000E+00-0.1723736E+09 002401\n" +
                "  0.0000000E+00 0.0000000E+00-0.7299241E+08-0.1082622E+09 0.0000000E+00 200302\n" +
                "  0.0000000E+00 0.0000000E+00-0.1105036E+09-0.6666608E+08 0.0000000E+00 110302\n" +
                "  0.0000000E+00 0.0000000E+00 -6525132.     0.3473270E+08 0.0000000E+00 020302\n" +
                "  0.1516546E+10 0.6006191E+09 0.0000000E+00 0.0000000E+00 0.1432685E+09 101302\n" +
                "  0.8761713E+09 0.3398321E+09 0.0000000E+00 0.0000000E+00 0.4403112E+08 011302\n" +
                "  0.0000000E+00 0.0000000E+00 0.1190821E+10 0.4508509E+08 0.0000000E+00 002302\n" +
                "   6985526.      4013731.     0.0000000E+00 0.0000000E+00  162962.7     200203\n" +
                "  -9713831.     -4766531.     0.0000000E+00 0.0000000E+00 -1001961.     110203\n" +
                "  -5392162.     -3892000.     0.0000000E+00 0.0000000E+00 -1056890.     020203\n" +
                "  0.0000000E+00 0.0000000E+00-0.2042485E+08-0.2414509E+08 0.0000000E+00 101203\n" +
                "  0.0000000E+00 0.0000000E+00-0.4107730E+08-0.2584008E+08 0.0000000E+00 011203\n" +
                " -0.2496539E+08-0.1085042E+08 0.0000000E+00 0.0000000E+00  660278.1     002203\n" +
                "  0.0000000E+00 0.0000000E+00  500723.3      298983.5     0.0000000E+00 200104\n" +
                "  0.0000000E+00 0.0000000E+00  239607.3      208870.9     0.0000000E+00 110104\n" +
                "  0.0000000E+00 0.0000000E+00 -211763.3     -77150.24     0.0000000E+00 020104\n" +
                "   1798533.      1050097.     0.0000000E+00 0.0000000E+00  131872.3     101104\n" +
                "   851425.2      522082.6     0.0000000E+00 0.0000000E+00  52336.12     011104\n" +
                "  0.0000000E+00 0.0000000E+00  2363323.      981503.3     0.0000000E+00 002104\n" +
                "   3433.901      347.2556     0.0000000E+00 0.0000000E+00 -1125.370     200005\n" +
                "   3269.224     -437.0824     0.0000000E+00 0.0000000E+00 -1810.020     110005\n" +
                "  -72.00864     -927.9301     0.0000000E+00 0.0000000E+00 -252.6332     020005\n" +
                "  0.0000000E+00 0.0000000E+00 -26014.25     -7934.343     0.0000000E+00 101005\n" +
                "  0.0000000E+00 0.0000000E+00 -18829.08     -3331.974     0.0000000E+00 011005\n" +
                "   20045.49      5081.682     0.0000000E+00 0.0000000E+00  2620.856     002005\n" +
                "  0.1871324E+11 0.4704194E+10 0.0000000E+00 0.0000000E+00 0.3578112E+10 100600\n" +
                "  0.5054243E+10 0.9297236E+09 0.0000000E+00 0.0000000E+00 0.1914666E+10 010600\n" +
                "  0.0000000E+00 0.0000000E+00 0.2740143E+11-0.1453142E+11 0.0000000E+00 001600\n" +
                "  0.0000000E+00 0.0000000E+00 0.9040960E+10 0.2537535E+10 0.0000000E+00 100501\n" +
                "  0.0000000E+00 0.0000000E+00 0.3988533E+10 0.2547372E+10 0.0000000E+00 010501\n" +
                " -0.5096411E+10-0.1785834E+10 0.0000000E+00 0.0000000E+00-0.2805840E+09 001501\n" +
                "  0.8426489E+09 0.3218326E+09 0.0000000E+00 0.0000000E+00 0.8386634E+08 100402\n" +
                "  0.7313744E+09 0.2707087E+09 0.0000000E+00 0.0000000E+00 0.4886403E+08 010402\n" +
                "  0.0000000E+00 0.0000000E+00 0.9390680E+09 0.1132663E+08 0.0000000E+00 001402\n" +
                "  0.0000000E+00 0.0000000E+00 -2892097.    -0.1181557E+08 0.0000000E+00 100303\n" +
                "  0.0000000E+00 0.0000000E+00-0.2084251E+08-0.1689938E+08 0.0000000E+00 010303\n" +
                " -0.6580714E+08-0.2592768E+08 0.0000000E+00 0.0000000E+00 -1116718.     001303\n" +
                "   2230607.      1053761.     0.0000000E+00 0.0000000E+00  153627.8     100204\n" +
                "   150165.9      251968.6     0.0000000E+00 0.0000000E+00 -9458.014     010204\n" +
                "  0.0000000E+00 0.0000000E+00  2584391.      1438972.     0.0000000E+00 001204\n" +
                "  0.0000000E+00 0.0000000E+00 -40517.99     -9205.164     0.0000000E+00 100105\n" +
                "  0.0000000E+00 0.0000000E+00 -16352.96      3555.525     0.0000000E+00 010105\n" +
                "   19276.27      931.9248     0.0000000E+00 0.0000000E+00  3466.100     001105\n" +
                "  -153.9435      20.99764     0.0000000E+00 0.0000000E+00  108.5565     100006\n" +
                "  -208.4684      47.25825     0.0000000E+00 0.0000000E+00  81.47311     010006\n" +
                "  0.0000000E+00 0.0000000E+00 -178.6988      81.84245     0.0000000E+00 001006\n" +
                "  0.0000000E+00 0.0000000E+00 0.1219632E+11-0.2978220E+10 0.0000000E+00 000700\n" +
                " -0.2047195E+10-0.6966626E+09 0.0000000E+00 0.0000000E+00-0.6786665E+08 000601\n" +
                "  0.0000000E+00 0.0000000E+00 0.1982523E+09-0.2937883E+08 0.0000000E+00 000502\n" +
                " -0.5314867E+08-0.1970646E+08 0.0000000E+00 0.0000000E+00 -2405023.     000403\n" +
                "  0.0000000E+00 0.0000000E+00  1123695.      847529.7     0.0000000E+00 000304\n" +
                "   10609.13     -4500.144     0.0000000E+00 0.0000000E+00  4417.434     000205\n" +
                "  0.0000000E+00 0.0000000E+00 -1563.917     -853.2807     0.0000000E+00 000106\n" +
                "   15.06921     -1.156998     0.0000000E+00 0.0000000E+00 -1.278347     000007\n" +
                "  0.4540004E+10 0.4474526E+10 0.0000000E+00 0.0000000E+00 0.1499230E+10 800000\n" +
                "  0.2909137E+11 0.9672184E+10 0.0000000E+00 0.0000000E+00-0.7289187E+09 710000\n" +
                "  0.8499773E+11-0.1826197E+10 0.0000000E+00 0.0000000E+00-0.2115038E+11 620000\n" +
                "  0.1358022E+12 0.2100836E+10 0.0000000E+00 0.0000000E+00-0.4020582E+11 530000\n" +
                "  0.1120987E+12 0.5234695E+11 0.0000000E+00 0.0000000E+00-0.1949366E+11 440000\n" +
                "  0.2287709E+11 0.7028615E+11 0.0000000E+00 0.0000000E+00 0.1412606E+11 350000\n" +
                " -0.3652333E+11 0.2476156E+11 0.0000000E+00 0.0000000E+00 0.1741577E+11 260000\n" +
                " -0.2926236E+11-0.5460028E+10 0.0000000E+00 0.0000000E+00 0.5534689E+10 170000\n" +
                " -0.6651446E+10-0.3200766E+10 0.0000000E+00 0.0000000E+00 0.4530673E+09 080000\n" +
                "  0.0000000E+00 0.0000000E+00-0.8324559E+11-0.2248941E+11 0.0000000E+00 701000\n" +
                "  0.0000000E+00 0.0000000E+00-0.2837795E+12-0.1239445E+12 0.0000000E+00 611000\n" +
                "  0.0000000E+00 0.0000000E+00 0.4602459E+11-0.1306595E+12 0.0000000E+00 521000\n" +
                "  0.0000000E+00 0.0000000E+00 0.1546794E+13 0.3625317E+12 0.0000000E+00 431000\n" +
                "  0.0000000E+00 0.0000000E+00 0.2680871E+13 0.9404759E+12 0.0000000E+00 341000\n" +
                "  0.0000000E+00 0.0000000E+00 0.1762336E+13 0.7489165E+12 0.0000000E+00 251000\n" +
                "  0.0000000E+00 0.0000000E+00 0.3563609E+12 0.2065948E+12 0.0000000E+00 161000\n" +
                "  0.0000000E+00 0.0000000E+00-0.2595404E+11 0.5821118E+10 0.0000000E+00 071000\n" +
                "  0.5189225E+12 0.1730796E+12 0.0000000E+00 0.0000000E+00-0.1460044E+10 602000\n" +
                "  0.1843657E+13 0.6837685E+12 0.0000000E+00 0.0000000E+00-0.1479715E+10 512000\n" +
                "  0.1877485E+13 0.8377338E+12 0.0000000E+00 0.0000000E+00-0.5790820E+11 422000\n" +
                " -0.1000908E+13-0.1412698E+12 0.0000000E+00 0.0000000E+00-0.2125954E+12 332000\n" +
                " -0.3077830E+13-0.1002074E+13 0.0000000E+00 0.0000000E+00-0.2671154E+12 242000\n" +
                " -0.1799761E+13-0.6352120E+12 0.0000000E+00 0.0000000E+00-0.1295532E+12 152000\n" +
                " -0.2952255E+12-0.1081142E+12 0.0000000E+00 0.0000000E+00-0.1860747E+11 062000\n" +
                "  0.0000000E+00 0.0000000E+00-0.5529171E+12-0.2503874E+12 0.0000000E+00 503000\n" +
                "  0.0000000E+00 0.0000000E+00-0.1078551E+13-0.4653809E+12 0.0000000E+00 413000\n" +
                "  0.0000000E+00 0.0000000E+00-0.5334027E+12-0.1682146E+12 0.0000000E+00 323000\n" +
                "  0.0000000E+00 0.0000000E+00 0.6281829E+11 0.1066779E+12 0.0000000E+00 233000\n" +
                "  0.0000000E+00 0.0000000E+00 0.9923593E+11 0.7380523E+11 0.0000000E+00 143000\n" +
                "  0.0000000E+00 0.0000000E+00 0.2551692E+11 0.1261610E+11 0.0000000E+00 053000\n" +
                " -0.9495727E+11-0.3999366E+11 0.0000000E+00 0.0000000E+00 0.1684470E+11 404000\n" +
                " -0.8035920E+11-0.4515255E+11 0.0000000E+00 0.0000000E+00 0.2743473E+11 314000\n" +
                "  0.2219198E+12 0.6232911E+11 0.0000000E+00 0.0000000E+00 0.2054178E+11 224000\n" +
                "  0.2706090E+12 0.9014580E+11 0.0000000E+00 0.0000000E+00 0.1073160E+11 134000\n" +
                "  0.8021117E+11 0.2847118E+11 0.0000000E+00 0.0000000E+00 0.2998861E+10 044000\n" +
                "  0.0000000E+00 0.0000000E+00-0.3358794E+11-0.1465830E+11 0.0000000E+00 305000\n" +
                "  0.0000000E+00 0.0000000E+00-0.9282581E+11-0.3661438E+11 0.0000000E+00 215000\n" +
                "  0.0000000E+00 0.0000000E+00-0.7348568E+11-0.2589539E+11 0.0000000E+00 125000\n" +
                "  0.0000000E+00 0.0000000E+00-0.1038049E+11-0.2511941E+10 0.0000000E+00 035000\n" +
                " -0.8047001E+11-0.2947252E+11 0.0000000E+00 0.0000000E+00-0.5153210E+10 206000\n" +
                " -0.2008280E+11-0.8066259E+10 0.0000000E+00 0.0000000E+00 0.6105412E+09 116000\n" +
                "  0.2533006E+11 0.9019099E+10 0.0000000E+00 0.0000000E+00 0.3106942E+10 026000\n" +
                "  0.0000000E+00 0.0000000E+00 0.6392346E+10 0.2952476E+10 0.0000000E+00 107000\n" +
                "  0.0000000E+00 0.0000000E+00 0.5149167E+10 0.2963940E+10 0.0000000E+00 017000\n" +
                "  0.2836864E+09 0.1991169E+09 0.0000000E+00 0.0000000E+00 0.6095606E+08 008000\n" +
                "  0.0000000E+00 0.0000000E+00-0.1181260E+12-0.3227419E+11 0.0000000E+00 700100\n" +
                "  0.0000000E+00 0.0000000E+00-0.4276692E+12-0.1942896E+12 0.0000000E+00 610100\n" +
                "  0.0000000E+00 0.0000000E+00 0.6490071E+11-0.2058659E+12 0.0000000E+00 520100\n" +
                "  0.0000000E+00 0.0000000E+00 0.2477971E+13 0.6155559E+12 0.0000000E+00 430100\n" +
                "  0.0000000E+00 0.0000000E+00 0.4297301E+13 0.1541335E+13 0.0000000E+00 340100\n" +
                "  0.0000000E+00 0.0000000E+00 0.2705048E+13 0.1165637E+13 0.0000000E+00 250100\n" +
                "  0.0000000E+00 0.0000000E+00 0.4394728E+12 0.2834112E+12 0.0000000E+00 160100\n" +
                "  0.0000000E+00 0.0000000E+00-0.7416816E+11-0.1933840E+10 0.0000000E+00 070100\n" +
                "  0.1407522E+13 0.4474332E+12 0.0000000E+00 0.0000000E+00 0.1651502E+11 601100\n" +
                "  0.5446724E+13 0.1983534E+13 0.0000000E+00 0.0000000E+00 0.1014862E+12 511100\n" +
                "  0.6434837E+13 0.2776458E+13 0.0000000E+00 0.0000000E+00 0.3908633E+11 421100\n" +
                " -0.1754839E+13-0.1887558E+10 0.0000000E+00 0.0000000E+00-0.4379278E+12 331100\n" +
                " -0.8379282E+13-0.2735924E+13 0.0000000E+00 0.0000000E+00-0.7042828E+12 241100\n" +
                " -0.4906035E+13-0.1748514E+13 0.0000000E+00 0.0000000E+00-0.3522479E+12 151100\n" +
                " -0.7468843E+12-0.2775444E+12 0.0000000E+00 0.0000000E+00-0.4686178E+11 061100\n" +
                "  0.0000000E+00 0.0000000E+00-0.1902644E+13-0.9068756E+12 0.0000000E+00 502100\n" +
                "  0.0000000E+00 0.0000000E+00-0.3154427E+13-0.1418125E+13 0.0000000E+00 412100\n" +
                "  0.0000000E+00 0.0000000E+00-0.1164064E+13-0.1959540E+12 0.0000000E+00 322100\n" +
                "  0.0000000E+00 0.0000000E+00-0.1492669E+12 0.4191602E+12 0.0000000E+00 232100\n" +
                "  0.0000000E+00 0.0000000E+00-0.4218776E+12 0.5471724E+11 0.0000000E+00 142100\n" +
                "  0.0000000E+00 0.0000000E+00-0.1497977E+12-0.3494387E+11 0.0000000E+00 052100\n" +
                " -0.7536696E+12-0.2612123E+12 0.0000000E+00 0.0000000E+00 0.1203890E+12 403100\n" +
                " -0.1498656E+13-0.5679561E+12 0.0000000E+00 0.0000000E+00 0.1811502E+12 313100\n" +
                " -0.4096195E+12-0.2487560E+12 0.0000000E+00 0.0000000E+00 0.1233696E+12 223100\n" +
                "  0.6899370E+12 0.1810773E+12 0.0000000E+00 0.0000000E+00 0.7482327E+11 133100\n" +
                "  0.4140590E+12 0.1411696E+12 0.0000000E+00 0.0000000E+00 0.3014484E+11 043100\n" +
                "  0.0000000E+00 0.0000000E+00 0.2354545E+12 0.1195777E+12 0.0000000E+00 304100\n" +
                "  0.0000000E+00 0.0000000E+00-0.1415558E+12 0.1751629E+10 0.0000000E+00 214100\n" +
                "  0.0000000E+00 0.0000000E+00-0.3719092E+12-0.9781920E+11 0.0000000E+00 124100\n" +
                "  0.0000000E+00 0.0000000E+00-0.3410603E+11-0.5656977E+09 0.0000000E+00 034100\n" +
                " -0.7080506E+12-0.2586846E+12 0.0000000E+00 0.0000000E+00-0.6671056E+11 205100\n" +
                " -0.4775194E+11-0.2393795E+11 0.0000000E+00 0.0000000E+00-0.4733105E+10 115100\n" +
                "  0.3045627E+12 0.1103900E+12 0.0000000E+00 0.0000000E+00 0.2928191E+11 025100\n" +
                "  0.0000000E+00 0.0000000E+00 0.5815270E+11 0.2964909E+11 0.0000000E+00 106100\n" +
                "  0.0000000E+00 0.0000000E+00 0.5871184E+11 0.3859437E+11 0.0000000E+00 016100\n" +
                "  0.2236939E+11 0.9520217E+10 0.0000000E+00 0.0000000E+00 0.2631354E+10 007100\n" +
                " -0.1691233E+10-0.2513266E+09 0.0000000E+00 0.0000000E+00 0.4531072E+08 700001\n" +
                " -0.5870418E+10-0.1850067E+10 0.0000000E+00 0.0000000E+00 0.6730187E+09 610001\n" +
                " -0.2971143E+10-0.2364820E+10 0.0000000E+00 0.0000000E+00 0.2739080E+10 520001\n" +
                "  0.1681290E+11 0.3925077E+10 0.0000000E+00 0.0000000E+00 0.3872297E+10 430001\n" +
                "  0.3570821E+11 0.1112498E+11 0.0000000E+00 0.0000000E+00 0.1215481E+10 340001\n" +
                "  0.2880440E+11 0.9093894E+10 0.0000000E+00 0.0000000E+00-0.1363616E+10 250001\n" +
                "  0.9541357E+10 0.2993969E+10 0.0000000E+00 0.0000000E+00-0.9522696E+09 160001\n" +
                "  0.8332757E+09 0.3193261E+09 0.0000000E+00 0.0000000E+00-0.1317683E+09 070001\n" +
                "  0.0000000E+00 0.0000000E+00-0.1151990E+11-0.2379283E+10 0.0000000E+00 601001\n" +
                "  0.0000000E+00 0.0000000E+00-0.7012654E+11-0.2827431E+11 0.0000000E+00 511001\n" +
                "  0.0000000E+00 0.0000000E+00-0.1120322E+12-0.6167690E+11 0.0000000E+00 421001\n" +
                "  0.0000000E+00 0.0000000E+00-0.6323721E+10-0.2921974E+11 0.0000000E+00 331001\n" +
                "  0.0000000E+00 0.0000000E+00 0.1060483E+12 0.2804833E+11 0.0000000E+00 241001\n" +
                "  0.0000000E+00 0.0000000E+00 0.7106020E+11 0.2685939E+11 0.0000000E+00 151001\n" +
                "  0.0000000E+00 0.0000000E+00 0.1215136E+11 0.5386943E+10 0.0000000E+00 061001\n" +
                "  0.3112792E+11 0.5104200E+10 0.0000000E+00 0.0000000E+00 0.2415761E+10 502001\n" +
                "  0.3730364E+12 0.1291890E+12 0.0000000E+00 0.0000000E+00 0.2372538E+11 412001\n" +
                "  0.8669620E+12 0.3276104E+12 0.0000000E+00 0.0000000E+00 0.5183801E+11 322001\n" +
                "  0.6927306E+12 0.2721856E+12 0.0000000E+00 0.0000000E+00 0.3850934E+11 232001\n" +
                "  0.1717232E+12 0.7138063E+11 0.0000000E+00 0.0000000E+00 0.7191262E+10 142001\n" +
                " -0.4660145E+10-0.6528456E+09 0.0000000E+00 0.0000000E+00-0.1226549E+10 052001\n" +
                "  0.0000000E+00 0.0000000E+00-0.3203963E+11-0.1996355E+11 0.0000000E+00 403001\n" +
                "  0.0000000E+00 0.0000000E+00-0.1159514E+12-0.6195997E+11 0.0000000E+00 313001\n" +
                "  0.0000000E+00 0.0000000E+00-0.1626152E+12-0.7775624E+11 0.0000000E+00 223001\n" +
                "  0.0000000E+00 0.0000000E+00-0.9303611E+11-0.4175309E+11 0.0000000E+00 133001\n" +
                "  0.0000000E+00 0.0000000E+00-0.1996030E+11-0.8371478E+10 0.0000000E+00 043001\n" +
                " -0.4215091E+11-0.1542769E+11 0.0000000E+00 0.0000000E+00 0.1661761E+10 304001\n" +
                " -0.3792323E+11-0.1393136E+11 0.0000000E+00 0.0000000E+00 0.7107467E+10 214001\n" +
                " -0.2090315E+11-0.7890599E+10 0.0000000E+00 0.0000000E+00 0.5578802E+10 124001\n" +
                " -0.1322989E+11-0.4979012E+10 0.0000000E+00 0.0000000E+00 0.5474364E+09 034001\n" +
                "  0.0000000E+00 0.0000000E+00 0.2137235E+11 0.9130704E+10 0.0000000E+00 205001\n" +
                "  0.0000000E+00 0.0000000E+00 0.1187474E+11 0.6243492E+10 0.0000000E+00 115001\n" +
                "  0.0000000E+00 0.0000000E+00-0.1922551E+10-0.3809140E+09 0.0000000E+00 025001\n" +
                " -0.1279704E+11-0.4641962E+10 0.0000000E+00 0.0000000E+00-0.1303112E+10 106001\n" +
                " -0.1182811E+11-0.4417024E+10 0.0000000E+00 0.0000000E+00-0.1107472E+10 016001\n" +
                "  0.0000000E+00 0.0000000E+00-0.6579956E+09-0.5726629E+09 0.0000000E+00 007001\n" +
                "  0.9172088E+12 0.2666437E+12 0.0000000E+00 0.0000000E+00 0.1969784E+11 600200\n" +
                "  0.3972597E+13 0.1410921E+13 0.0000000E+00 0.0000000E+00 0.1302104E+12 510200\n" +
                "  0.5289753E+13 0.2247679E+13 0.0000000E+00 0.0000000E+00 0.1681235E+12 420200\n" +
                " -0.6658147E+12 0.2589520E+12 0.0000000E+00 0.0000000E+00-0.1766362E+12 330200\n" +
                " -0.5768635E+13-0.1862046E+13 0.0000000E+00 0.0000000E+00-0.4400193E+12 240200\n" +
                " -0.3262053E+13-0.1172870E+13 0.0000000E+00 0.0000000E+00-0.2245341E+12 150200\n" +
                " -0.4511595E+12-0.1728216E+12 0.0000000E+00 0.0000000E+00-0.2702188E+11 060200\n" +
                "  0.0000000E+00 0.0000000E+00-0.1871283E+13-0.9971772E+12 0.0000000E+00 501200\n" +
                "  0.0000000E+00 0.0000000E+00-0.1865536E+13-0.1000936E+13 0.0000000E+00 411200\n" +
                "  0.0000000E+00 0.0000000E+00 0.9085224E+12 0.8961338E+12 0.0000000E+00 321200\n" +
                "  0.0000000E+00 0.0000000E+00-0.8035725E+11 0.9357876E+12 0.0000000E+00 231200\n" +
                "  0.0000000E+00 0.0000000E+00-0.1783809E+13-0.2088539E+12 0.0000000E+00 141200\n" +
                "  0.0000000E+00 0.0000000E+00-0.6489869E+12-0.1843151E+12 0.0000000E+00 051200\n" +
                " -0.1773492E+13-0.5030091E+12 0.0000000E+00 0.0000000E+00 0.2517849E+12 402200\n" +
                " -0.5270057E+13-0.1810080E+13 0.0000000E+00 0.0000000E+00 0.3073181E+12 312200\n" +
                " -0.4883398E+13-0.1998879E+13 0.0000000E+00 0.0000000E+00 0.1383613E+12 222200\n" +
                " -0.3722112E+12-0.3439964E+12 0.0000000E+00 0.0000000E+00 0.1410431E+12 132200\n" +
                "  0.9190886E+12 0.3041206E+12 0.0000000E+00 0.0000000E+00 0.9366829E+11 042200\n" +
                "  0.0000000E+00 0.0000000E+00 0.1651817E+13 0.8388066E+12 0.0000000E+00 303200\n" +
                "  0.0000000E+00 0.0000000E+00 0.9665997E+11 0.3807608E+12 0.0000000E+00 213200\n" +
                "  0.0000000E+00 0.0000000E+00-0.1307547E+13-0.2875023E+12 0.0000000E+00 123200\n" +
                "  0.0000000E+00 0.0000000E+00-0.7951840E+11 0.8199649E+10 0.0000000E+00 033200\n" +
                " -0.2582331E+13-0.9531842E+12 0.0000000E+00 0.0000000E+00-0.3089558E+12 204200\n" +
                "  0.2946545E+12 0.7372720E+11 0.0000000E+00 0.0000000E+00-0.4391220E+11 114200\n" +
                "  0.1465804E+13 0.5380315E+12 0.0000000E+00 0.0000000E+00 0.1182610E+12 024200\n" +
                "  0.0000000E+00 0.0000000E+00 0.1693556E+12 0.1078437E+12 0.0000000E+00 105200\n" +
                "  0.0000000E+00 0.0000000E+00 0.2768234E+12 0.2127691E+12 0.0000000E+00 015200\n" +
                "  0.2225572E+12 0.9022328E+11 0.0000000E+00 0.0000000E+00 0.2482313E+11 006200\n" +
                "  0.0000000E+00 0.0000000E+00-0.1971639E+11-0.4240013E+10 0.0000000E+00 600101\n" +
                "  0.0000000E+00 0.0000000E+00-0.1155238E+12-0.4656094E+11 0.0000000E+00 510101\n" +
                "  0.0000000E+00 0.0000000E+00-0.1791770E+12-0.9991282E+11 0.0000000E+00 420101\n" +
                "  0.0000000E+00 0.0000000E+00-0.8857522E+10-0.4833299E+11 0.0000000E+00 330101\n" +
                "  0.0000000E+00 0.0000000E+00 0.1734398E+12 0.4566516E+11 0.0000000E+00 240101\n" +
                "  0.0000000E+00 0.0000000E+00 0.1258310E+12 0.4760691E+11 0.0000000E+00 150101\n" +
                "  0.0000000E+00 0.0000000E+00 0.2587137E+11 0.1088235E+11 0.0000000E+00 060101\n" +
                "  0.7723165E+11 0.6467517E+10 0.0000000E+00 0.0000000E+00 0.5894141E+10 501101\n" +
                "  0.1129181E+13 0.3843910E+12 0.0000000E+00 0.0000000E+00 0.6863741E+11 411101\n" +
                "  0.2638223E+13 0.9906856E+12 0.0000000E+00 0.0000000E+00 0.1504121E+12 321101\n" +
                "  0.2071969E+13 0.8127070E+12 0.0000000E+00 0.0000000E+00 0.1080136E+12 231101\n" +
                "  0.4898630E+12 0.2062438E+12 0.0000000E+00 0.0000000E+00 0.1748824E+11 141101\n" +
                " -0.2573127E+11-0.5416192E+10 0.0000000E+00 0.0000000E+00-0.4624165E+10 051101\n" +
                "  0.0000000E+00 0.0000000E+00-0.1081485E+12-0.7140922E+11 0.0000000E+00 402101\n" +
                "  0.0000000E+00 0.0000000E+00-0.3823556E+12-0.2132428E+12 0.0000000E+00 312101\n" +
                "  0.0000000E+00 0.0000000E+00-0.4405818E+12-0.2207783E+12 0.0000000E+00 222101\n" +
                "  0.0000000E+00 0.0000000E+00-0.2033560E+12-0.9327422E+11 0.0000000E+00 132101\n" +
                "  0.0000000E+00 0.0000000E+00-0.4818966E+11-0.1824349E+11 0.0000000E+00 042101\n" +
                " -0.1645970E+12-0.5349826E+11 0.0000000E+00 0.0000000E+00-0.1508477E+10 303101\n" +
                " -0.2038111E+12-0.6504248E+11 0.0000000E+00 0.0000000E+00 0.1704934E+11 213101\n" +
                " -0.3020805E+12-0.1084717E+12 0.0000000E+00 0.0000000E+00 0.4912219E+10 123101\n" +
                " -0.1670492E+12-0.6208186E+11 0.0000000E+00 0.0000000E+00-0.6504110E+10 033101\n" +
                "  0.0000000E+00 0.0000000E+00 0.1415768E+12 0.6240111E+11 0.0000000E+00 204101\n" +
                "  0.0000000E+00 0.0000000E+00 0.6824207E+11 0.4308852E+11 0.0000000E+00 114101\n" +
                "  0.0000000E+00 0.0000000E+00-0.2171590E+11-0.3557475E+10 0.0000000E+00 024101\n" +
                " -0.9783227E+11-0.3552002E+11 0.0000000E+00 0.0000000E+00-0.8921887E+10 105101\n" +
                " -0.1156321E+12-0.4341350E+11 0.0000000E+00 0.0000000E+00-0.1010916E+11 015101\n" +
                "  0.0000000E+00 0.0000000E+00-0.7769161E+10-0.7015820E+10 0.0000000E+00 006101\n" +
                "  0.6246179E+08 0.6451797E+08 0.0000000E+00 0.0000000E+00  9191135.     600002\n" +
                "  0.1785994E+09 0.3786784E+08 0.0000000E+00 0.0000000E+00-0.2421533E+08 510002\n" +
                "  0.4753775E+09-0.1697585E+09 0.0000000E+00 0.0000000E+00-0.2104738E+09 420002\n" +
                "  0.9513030E+09-0.7359609E+08 0.0000000E+00 0.0000000E+00-0.3210440E+09 330002\n" +
                "  0.7862122E+09 0.1853979E+09 0.0000000E+00 0.0000000E+00-0.1147908E+09 240002\n" +
                "  0.1880092E+09 0.1338721E+09 0.0000000E+00 0.0000000E+00 0.5258032E+08 150002\n" +
                "  -7125494.     0.1773485E+08 0.0000000E+00 0.0000000E+00 0.2519051E+08 060002\n" +
                "  0.0000000E+00 0.0000000E+00-0.1051269E+10-0.1081550E+09 0.0000000E+00 501002\n" +
                "  0.0000000E+00 0.0000000E+00-0.6856571E+10-0.2670829E+10 0.0000000E+00 411002\n" +
                "  0.0000000E+00 0.0000000E+00-0.9998948E+10-0.5445429E+10 0.0000000E+00 321002\n" +
                "  0.0000000E+00 0.0000000E+00-0.2970348E+10-0.2932103E+10 0.0000000E+00 231002\n" +
                "  0.0000000E+00 0.0000000E+00 0.1877858E+10 0.3041021E+09 0.0000000E+00 141002\n" +
                "  0.0000000E+00 0.0000000E+00 0.7949614E+09 0.3894961E+09 0.0000000E+00 051002\n" +
                " -0.1239933E+11-0.5814358E+10 0.0000000E+00 0.0000000E+00-0.5084589E+09 402002\n" +
                " -0.6077225E+10-0.4322216E+10 0.0000000E+00 0.0000000E+00 0.3180456E+09 312002\n" +
                "  0.2617812E+11 0.8975321E+10 0.0000000E+00 0.0000000E+00 0.2336771E+10 222002\n" +
                "  0.2803801E+11 0.1073036E+11 0.0000000E+00 0.0000000E+00 0.2073746E+10 132002\n" +
                "  0.8070095E+10 0.3190911E+10 0.0000000E+00 0.0000000E+00 0.5627032E+09 042002\n" +
                "  0.0000000E+00 0.0000000E+00 0.1001819E+11 0.4795588E+10 0.0000000E+00 303002\n" +
                "  0.0000000E+00 0.0000000E+00 0.1410245E+11 0.7015793E+10 0.0000000E+00 213002\n" +
                "  0.0000000E+00 0.0000000E+00 0.6145883E+10 0.3172394E+10 0.0000000E+00 123002\n" +
                "  0.0000000E+00 0.0000000E+00 0.8523442E+09 0.3109682E+09 0.0000000E+00 033002\n" +
                " -0.5876554E+10-0.2195490E+10 0.0000000E+00 0.0000000E+00-0.1188611E+10 204002\n" +
                " -0.2979506E+10-0.1146221E+10 0.0000000E+00 0.0000000E+00-0.9388716E+09 114002\n" +
                "  0.1049886E+10 0.3704857E+09 0.0000000E+00 0.0000000E+00-0.7897803E+08 024002\n" +
                "  0.0000000E+00 0.0000000E+00 0.5795246E+08-0.1347037E+09 0.0000000E+00 105002\n" +
                "  0.0000000E+00 0.0000000E+00 0.7914383E+08 0.1231476E+09 0.0000000E+00 015002\n" +
                "  0.9817484E+09 0.3855372E+09 0.0000000E+00 0.0000000E+00 0.7297482E+08 006002\n" +
                "  0.0000000E+00 0.0000000E+00-0.4597952E+12-0.3258221E+12 0.0000000E+00 500300\n" +
                "  0.0000000E+00 0.0000000E+00 0.5417509E+12 0.7919600E+11 0.0000000E+00 410300\n" +
                "  0.0000000E+00 0.0000000E+00 0.2196556E+13 0.1229786E+13 0.0000000E+00 320300\n" +
                "  0.0000000E+00 0.0000000E+00 0.3864756E+12 0.8010100E+12 0.0000000E+00 230300\n" +
                "  0.0000000E+00 0.0000000E+00-0.1506944E+13-0.2342099E+12 0.0000000E+00 140300\n" +
                "  0.0000000E+00 0.0000000E+00-0.5807827E+12-0.1654881E+12 0.0000000E+00 050300\n" +
                " -0.1158384E+13-0.1538486E+12 0.0000000E+00 0.0000000E+00 0.2287981E+12 401300";

        string = string +
                " -0.6143260E+13-0.1970075E+13 0.0000000E+00 0.0000000E+00 0.1902878E+12 311300\n" +
                " -0.8183693E+13-0.3219193E+13 0.0000000E+00 0.0000000E+00-0.4920199E+11 221300\n" +
                " -0.1635927E+13-0.8683446E+12 0.0000000E+00 0.0000000E+00 0.1030687E+12 131300\n" +
                "  0.1082386E+13 0.3556118E+12 0.0000000E+00 0.0000000E+00 0.1201114E+12 041300\n" +
                "  0.0000000E+00 0.0000000E+00 0.3317572E+13 0.1759284E+13 0.0000000E+00 302300\n" +
                "  0.0000000E+00 0.0000000E+00-0.3501087E+12 0.6749430E+12 0.0000000E+00 212300\n" +
                "  0.0000000E+00 0.0000000E+00-0.3182368E+13-0.7435964E+12 0.0000000E+00 122300\n" +
                "  0.0000000E+00 0.0000000E+00-0.2111943E+12-0.1966953E+11 0.0000000E+00 032300\n" +
                " -0.5025910E+13-0.1902607E+13 0.0000000E+00 0.0000000E+00-0.7036835E+12 203300\n" +
                "  0.1379627E+13 0.4022365E+12 0.0000000E+00 0.0000000E+00-0.1404537E+12 113300\n" +
                "  0.3613329E+13 0.1345179E+13 0.0000000E+00 0.0000000E+00 0.2496475E+12 023300\n" +
                "  0.0000000E+00 0.0000000E+00 0.1265460E+12 0.1887957E+12 0.0000000E+00 104300\n" +
                "  0.0000000E+00 0.0000000E+00 0.7576198E+12 0.6760365E+12 0.0000000E+00 014300\n" +
                "  0.9760979E+12 0.3928208E+12 0.0000000E+00 0.0000000E+00 0.1073512E+12 005300\n" +
                "  0.4221997E+11-0.2712372E+10 0.0000000E+00 0.0000000E+00 0.5300862E+10 500201\n" +
                "  0.8366582E+12 0.2800565E+12 0.0000000E+00 0.0000000E+00 0.5452271E+11 410201\n" +
                "  0.1997049E+13 0.7426428E+12 0.0000000E+00 0.0000000E+00 0.1128585E+12 320201\n" +
                "  0.1558760E+13 0.6043500E+12 0.0000000E+00 0.0000000E+00 0.7620049E+11 230201\n" +
                "  0.3369566E+12 0.1422710E+12 0.0000000E+00 0.0000000E+00 0.8613652E+10 140201\n" +
                " -0.3873684E+11-0.1046147E+11 0.0000000E+00 0.0000000E+00-0.5070449E+10 050201\n" +
                "  0.0000000E+00 0.0000000E+00-0.1116619E+12-0.8453828E+11 0.0000000E+00 401201\n" +
                "  0.0000000E+00 0.0000000E+00-0.3054205E+12-0.2031128E+12 0.0000000E+00 311201\n" +
                "  0.0000000E+00 0.0000000E+00-0.1409151E+12-0.1034200E+12 0.0000000E+00 221201\n" +
                "  0.0000000E+00 0.0000000E+00 0.4040582E+11 0.1219363E+11 0.0000000E+00 131201\n" +
                "  0.0000000E+00 0.0000000E+00-0.9967343E+10 0.1658246E+10 0.0000000E+00 041201\n" +
                " -0.9029408E+11 0.1050282E+09 0.0000000E+00 0.0000000E+00-0.1107038E+11 302201\n" +
                " -0.3636316E+12-0.8438906E+11 0.0000000E+00 0.0000000E+00 0.1907163E+10 212201\n" +
                " -0.1114908E+13-0.3914080E+12 0.0000000E+00 0.0000000E+00-0.4680155E+11 122201\n" +
                " -0.5910512E+12-0.2175881E+12 0.0000000E+00 0.0000000E+00-0.3601688E+11 032201\n" +
                "  0.0000000E+00 0.0000000E+00 0.4208606E+12 0.1901692E+12 0.0000000E+00 203201\n" +
                "  0.0000000E+00 0.0000000E+00 0.2063303E+12 0.1468331E+12 0.0000000E+00 113201\n" +
                "  0.0000000E+00 0.0000000E+00-0.8497642E+11-0.8638585E+10 0.0000000E+00 023201\n" +
                " -0.3306742E+12-0.1207119E+12 0.0000000E+00 0.0000000E+00-0.2488043E+11 104201\n" +
                " -0.4828653E+12-0.1825982E+12 0.0000000E+00 0.0000000E+00-0.3972919E+11 014201\n" +
                "  0.0000000E+00 0.0000000E+00-0.3591874E+11-0.3467441E+11 0.0000000E+00 005201\n" +
                "  0.0000000E+00 0.0000000E+00-0.1378511E+10-0.1823074E+09 0.0000000E+00 500102\n" +
                "  0.0000000E+00 0.0000000E+00-0.8365217E+10-0.3638110E+10 0.0000000E+00 410102\n" +
                "  0.0000000E+00 0.0000000E+00-0.9772973E+10-0.6499122E+10 0.0000000E+00 320102\n" +
                "  0.0000000E+00 0.0000000E+00 0.9065714E+09-0.2110782E+10 0.0000000E+00 230102\n" +
                "  0.0000000E+00 0.0000000E+00 0.4380875E+10 0.1563347E+10 0.0000000E+00 140102\n" +
                "  0.0000000E+00 0.0000000E+00 0.9550909E+09 0.7113687E+09 0.0000000E+00 050102\n" +
                " -0.4077623E+11-0.1868716E+11 0.0000000E+00 0.0000000E+00-0.1497102E+10 401102\n" +
                " -0.2913206E+11-0.1662510E+11 0.0000000E+00 0.0000000E+00 0.8405836E+09 311102\n" +
                "  0.7020989E+11 0.2401250E+11 0.0000000E+00 0.0000000E+00 0.7241897E+10 221102\n" +
                "  0.8266943E+11 0.3154512E+11 0.0000000E+00 0.0000000E+00 0.6647554E+10 131102\n" +
                "  0.2441681E+11 0.9583879E+10 0.0000000E+00 0.0000000E+00 0.1831767E+10 041102\n" +
                "  0.0000000E+00 0.0000000E+00 0.4261275E+11 0.1962956E+11 0.0000000E+00 302102\n" +
                "  0.0000000E+00 0.0000000E+00 0.6063474E+11 0.2935066E+11 0.0000000E+00 212102\n" +
                "  0.0000000E+00 0.0000000E+00 0.2522133E+11 0.1352646E+11 0.0000000E+00 122102\n" +
                "  0.0000000E+00 0.0000000E+00 0.2912570E+10 0.1137476E+10 0.0000000E+00 032102\n" +
                " -0.1375528E+11-0.4601038E+10 0.0000000E+00 0.0000000E+00-0.4848268E+10 203102\n" +
                "  0.7993012E+10 0.3200918E+10 0.0000000E+00 0.0000000E+00-0.2939415E+10 113102\n" +
                "  0.1536966E+11 0.5681470E+10 0.0000000E+00 0.0000000E+00 0.4016296E+09 023102\n" +
                "  0.0000000E+00 0.0000000E+00-0.1626458E+10-0.2115208E+10 0.0000000E+00 104102\n" +
                "  0.0000000E+00 0.0000000E+00-0.1258415E+10-0.7193513E+08 0.0000000E+00 014102\n" +
                "  0.9378587E+10 0.3686107E+10 0.0000000E+00 0.0000000E+00 0.7204280E+09 005102\n" +
                " -0.1990702E+08 -5291477.     0.0000000E+00 0.0000000E+00 -1874520.     500003\n" +
                " -0.8087605E+08-0.2774847E+08 0.0000000E+00 0.0000000E+00 -127933.2     410003\n" +
                " -0.1066228E+09-0.3587865E+08 0.0000000E+00 0.0000000E+00 0.1532549E+08 320003\n" +
                " -0.4006840E+08 -8735917.     0.0000000E+00 0.0000000E+00 0.2252205E+08 230003\n" +
                "  0.1778443E+08  7884656.     0.0000000E+00 0.0000000E+00  8589113.     140003\n" +
                "  0.1070688E+08  3080177.     0.0000000E+00 0.0000000E+00  19709.38     050003\n" +
                "  0.0000000E+00 0.0000000E+00 -8298608.      8868727.     0.0000000E+00 401003\n" +
                "  0.0000000E+00 0.0000000E+00-0.2235868E+09-0.1211050E+09 0.0000000E+00 311003\n" +
                "  0.0000000E+00 0.0000000E+00-0.1743326E+09-0.1835541E+09 0.0000000E+00 221003\n" +
                "  0.0000000E+00 0.0000000E+00 0.1453668E+09-0.2978277E+08 0.0000000E+00 131003\n" +
                "  0.0000000E+00 0.0000000E+00 0.9868817E+08 0.2320484E+08 0.0000000E+00 041003\n" +
                " -0.1403804E+10-0.6006117E+09 0.0000000E+00 0.0000000E+00-0.8239675E+08 302003\n" +
                " -0.2199049E+10-0.9769404E+09 0.0000000E+00 0.0000000E+00-0.1290388E+09 212003\n" +
                " -0.1242710E+10-0.5771837E+09 0.0000000E+00 0.0000000E+00-0.6838323E+08 122003\n" +
                " -0.2723057E+09-0.1254457E+09 0.0000000E+00 0.0000000E+00-0.1218071E+08 032003\n" +
                "  0.0000000E+00 0.0000000E+00 0.3313112E+09 0.1554517E+09 0.0000000E+00 203003\n" +
                "  0.0000000E+00 0.0000000E+00 0.5171211E+09 0.2919778E+09 0.0000000E+00 113003\n" +
                "  0.0000000E+00 0.0000000E+00 0.2393589E+09 0.1618983E+09 0.0000000E+00 023003\n" +
                "   2928698.     0.1143772E+08 0.0000000E+00 0.0000000E+00 0.1085291E+08 104003\n" +
                " -0.2410893E+09-0.9066487E+08 0.0000000E+00 0.0000000E+00-0.1092376E+08 014003\n" +
                "  0.0000000E+00 0.0000000E+00 0.2314070E+08 -1148384.     0.0000000E+00 005003\n" +
                "  0.1240856E+12 0.2009469E+12 0.0000000E+00 0.0000000E+00 0.1019125E+12 400400\n" +
                " -0.2224954E+13-0.6403652E+12 0.0000000E+00 0.0000000E+00 0.6688304E+11 310400\n" +
                " -0.3925124E+13-0.1530292E+13 0.0000000E+00 0.0000000E+00-0.7173851E+11 220400\n" +
                " -0.7099400E+12-0.3974357E+12 0.0000000E+00 0.0000000E+00 0.3567394E+11 130400\n" +
                "  0.5077829E+12 0.1652739E+12 0.0000000E+00 0.0000000E+00 0.5350428E+11 040400\n" +
                "  0.0000000E+00 0.0000000E+00 0.3043297E+13 0.1687686E+13 0.0000000E+00 301400\n" +
                "  0.0000000E+00 0.0000000E+00-0.1015583E+13 0.5052313E+12 0.0000000E+00 211400\n" +
                "  0.0000000E+00 0.0000000E+00-0.3831461E+13-0.9317230E+12 0.0000000E+00 121400\n" +
                "  0.0000000E+00 0.0000000E+00-0.2668909E+12-0.4699957E+11 0.0000000E+00 031400\n" +
                " -0.5441734E+13-0.2154434E+13 0.0000000E+00 0.0000000E+00-0.8628385E+12 202400\n" +
                "  0.2135900E+13 0.6129456E+12 0.0000000E+00 0.0000000E+00-0.2624049E+12 112400\n" +
                "  0.4726558E+13 0.1795667E+13 0.0000000E+00 0.0000000E+00 0.2668360E+12 022400\n" +
                "  0.0000000E+00 0.0000000E+00-0.2511806E+12 0.1912978E+12 0.0000000E+00 103400\n" +
                "  0.0000000E+00 0.0000000E+00 0.1399978E+13 0.1368173E+13 0.0000000E+00 013400\n" +
                "  0.2324852E+13 0.9468916E+12 0.0000000E+00 0.0000000E+00 0.2539116E+12 004400\n" +
                "  0.0000000E+00 0.0000000E+00-0.1875186E+11-0.2934255E+11 0.0000000E+00 400301\n" +
                "  0.0000000E+00 0.0000000E+00 0.2235527E+11-0.3185807E+11 0.0000000E+00 310301\n" +
                "  0.0000000E+00 0.0000000E+00 0.2263532E+12 0.7642534E+11 0.0000000E+00 220301\n" +
                "  0.0000000E+00 0.0000000E+00 0.2278616E+12 0.9699349E+11 0.0000000E+00 130301\n" +
                "  0.0000000E+00 0.0000000E+00 0.4173240E+11 0.2103213E+11 0.0000000E+00 040301\n" +
                "  0.2595636E+12 0.1473866E+12 0.0000000E+00 0.0000000E+00-0.9346794E+10 301301\n" +
                " -0.2520296E+12-0.1432926E+11 0.0000000E+00 0.0000000E+00-0.2774941E+11 211301\n" +
                " -0.1640741E+13-0.5668003E+12 0.0000000E+00 0.0000000E+00-0.1073272E+12 121301\n" +
                " -0.8447002E+12-0.3072419E+12 0.0000000E+00 0.0000000E+00-0.5901728E+11 031301\n" +
                "  0.0000000E+00 0.0000000E+00 0.6933467E+12 0.3200771E+12 0.0000000E+00 202301\n" +
                "  0.0000000E+00 0.0000000E+00 0.3575533E+12 0.2713103E+12 0.0000000E+00 112301\n" +
                "  0.0000000E+00 0.0000000E+00-0.1832185E+12-0.1275388E+11 0.0000000E+00 022301\n" +
                " -0.6489631E+12-0.2391815E+12 0.0000000E+00 0.0000000E+00-0.3761491E+11 103301\n" +
                " -0.1074681E+13-0.4102355E+12 0.0000000E+00 0.0000000E+00-0.8455050E+11 013301\n" +
                "  0.0000000E+00 0.0000000E+00-0.9518001E+11-0.9372103E+11 0.0000000E+00 004301\n" +
                " -0.3252277E+11-0.1480969E+11 0.0000000E+00 0.0000000E+00-0.1359179E+10 400202\n" +
                " -0.2659093E+11-0.1430595E+11 0.0000000E+00 0.0000000E+00 0.6834596E+08 310202\n" +
                "  0.4961286E+11 0.1694182E+11 0.0000000E+00 0.0000000E+00 0.5451172E+10 220202\n" +
                "  0.6141696E+11 0.2356358E+11 0.0000000E+00 0.0000000E+00 0.5323433E+10 130202\n" +
                "  0.1964085E+11 0.7635525E+10 0.0000000E+00 0.0000000E+00 0.1605199E+10 040202\n" +
                "  0.0000000E+00 0.0000000E+00 0.5814332E+11 0.2582006E+11 0.0000000E+00 301202\n" +
                "  0.0000000E+00 0.0000000E+00 0.7481635E+11 0.3685397E+11 0.0000000E+00 211202\n" +
                "  0.0000000E+00 0.0000000E+00 0.2168554E+11 0.1552181E+11 0.0000000E+00 121202\n" +
                "  0.0000000E+00 0.0000000E+00 0.5967334E+08 0.6151532E+09 0.0000000E+00 031202\n" +
                "  0.1611302E+11 0.8262993E+10 0.0000000E+00 0.0000000E+00-0.6843511E+10 202202\n" +
                "  0.7758215E+11 0.2995930E+11 0.0000000E+00 0.0000000E+00-0.1325109E+10 112202\n" +
                "  0.5683184E+11 0.2109688E+11 0.0000000E+00 0.0000000E+00 0.2744125E+10 022202\n" +
                "  0.0000000E+00 0.0000000E+00-0.9170729E+10-0.8365310E+10 0.0000000E+00 103202\n" +
                "  0.0000000E+00 0.0000000E+00-0.8846409E+10-0.3249685E+10 0.0000000E+00 013202\n" +
                "  0.3550885E+11 0.1397713E+11 0.0000000E+00 0.0000000E+00 0.2852013E+10 004202\n" +
                "  0.0000000E+00 0.0000000E+00-0.7846828E+08-0.1724866E+08 0.0000000E+00 400103\n" +
                "  0.0000000E+00 0.0000000E+00-0.4375982E+09-0.2334735E+09 0.0000000E+00 310103\n" +
                "  0.0000000E+00 0.0000000E+00-0.2771304E+09-0.2861499E+09 0.0000000E+00 220103\n" +
                "  0.0000000E+00 0.0000000E+00 0.2307227E+09-0.3956937E+08 0.0000000E+00 130103\n" +
                "  0.0000000E+00 0.0000000E+00 0.1370284E+09 0.2572346E+08 0.0000000E+00 040103\n" +
                " -0.3622861E+10-0.1605596E+10 0.0000000E+00 0.0000000E+00-0.2184674E+09 301103\n" +
                " -0.5705505E+10-0.2641747E+10 0.0000000E+00 0.0000000E+00-0.3554797E+09 211103\n" +
                " -0.3733290E+10-0.1741036E+10 0.0000000E+00 0.0000000E+00-0.2368121E+09 121103\n" +
                " -0.9289389E+09-0.4145700E+09 0.0000000E+00 0.0000000E+00-0.5420546E+08 031103\n" +
                "  0.0000000E+00 0.0000000E+00 0.1183526E+10 0.5298441E+09 0.0000000E+00 202103\n" +
                "  0.0000000E+00 0.0000000E+00 0.2284434E+10 0.1260728E+10 0.0000000E+00 112103\n" +
                "  0.0000000E+00 0.0000000E+00 0.1119781E+10 0.7730900E+09 0.0000000E+00 022103\n" +
                " -0.5533955E+08 0.4954697E+08 0.0000000E+00 0.0000000E+00 0.3408950E+08 103103\n" +
                " -0.1732057E+10-0.6372382E+09 0.0000000E+00 0.0000000E+00-0.1019220E+09 013103\n" +
                "  0.0000000E+00 0.0000000E+00 0.3254025E+09 0.9795292E+08 0.0000000E+00 004103\n" +
                "   1063901.      603265.9     0.0000000E+00 0.0000000E+00  332514.6     400004\n" +
                "   3324612.      1269344.     0.0000000E+00 0.0000000E+00  644646.6     310004\n" +
                "   6100179.      1477310.     0.0000000E+00 0.0000000E+00 -169158.2     220004\n" +
                "   5448916.      1253638.     0.0000000E+00 0.0000000E+00 -610646.0     130004\n" +
                "   1506751.      416584.7     0.0000000E+00 0.0000000E+00 -163014.2     040004\n" +
                "  0.0000000E+00 0.0000000E+00 -3078630.     -109898.8     0.0000000E+00 301004\n" +
                "  0.0000000E+00 0.0000000E+00-0.1241685E+08 -5763380.     0.0000000E+00 211004\n" +
                "  0.0000000E+00 0.0000000E+00-0.1128982E+08 -7683323.     0.0000000E+00 121004\n" +
                "  0.0000000E+00 0.0000000E+00 -4340557.     -3182547.     0.0000000E+00 031004\n" +
                " -0.2721763E+08 -9284887.     0.0000000E+00 0.0000000E+00 -1591893.     202004\n" +
                " -0.4132182E+08-0.1587579E+08 0.0000000E+00 0.0000000E+00 -2514214.     112004\n" +
                " -0.1278010E+08 -6015203.     0.0000000E+00 0.0000000E+00 -974267.4     022004\n" +
                "  0.0000000E+00 0.0000000E+00-0.3426153E+08-0.1998332E+08 0.0000000E+00 103004\n" +
                "  0.0000000E+00 0.0000000E+00-0.2436658E+08-0.1527457E+08 0.0000000E+00 013004\n" +
                "  0.1806439E+08  7309412.     0.0000000E+00 0.0000000E+00  988829.4     004004\n" +
                "  0.0000000E+00 0.0000000E+00 0.1223357E+13 0.6816753E+12 0.0000000E+00 300500\n" +
                "  0.0000000E+00 0.0000000E+00-0.4644662E+12 0.2008077E+12 0.0000000E+00 210500\n" +
                "  0.0000000E+00 0.0000000E+00-0.1728567E+13-0.4230827E+12 0.0000000E+00 120500\n" +
                "  0.0000000E+00 0.0000000E+00-0.9831980E+11-0.1531452E+11 0.0000000E+00 030500\n" +
                " -0.2913549E+13-0.1256824E+13 0.0000000E+00 0.0000000E+00-0.5509226E+12 201500\n" +
                "  0.1376208E+13 0.3510041E+12 0.0000000E+00 0.0000000E+00-0.2844971E+12 111500\n" +
                "  0.2976101E+13 0.1167894E+13 0.0000000E+00 0.0000000E+00 0.1081034E+12 021500\n" +
                "  0.0000000E+00 0.0000000E+00-0.5144443E+12 0.1756905E+12 0.0000000E+00 102500\n" +
                "  0.0000000E+00 0.0000000E+00 0.1812245E+13 0.1770414E+13 0.0000000E+00 012500\n" +
                "  0.3166255E+13 0.1331446E+13 0.0000000E+00 0.0000000E+00 0.3431612E+12 003500\n" +
                "  0.2817871E+12 0.1277584E+12 0.0000000E+00 0.0000000E+00-0.6513993E+09 300401\n" +
                " -0.5764160E+10 0.3343775E+11 0.0000000E+00 0.0000000E+00-0.2005445E+11 210401\n" +
                " -0.8504001E+12-0.2914576E+12 0.0000000E+00 0.0000000E+00-0.6575854E+11 120401\n" +
                " -0.4254315E+12-0.1525026E+12 0.0000000E+00 0.0000000E+00-0.3161087E+11 030401\n" +
                "  0.0000000E+00 0.0000000E+00 0.5969419E+12 0.2855743E+12 0.0000000E+00 201401\n" +
                "  0.0000000E+00 0.0000000E+00 0.3079270E+12 0.2520437E+12 0.0000000E+00 111401\n" +
                "  0.0000000E+00 0.0000000E+00-0.2172515E+12-0.1516877E+11 0.0000000E+00 021401\n" +
                " -0.7758310E+12-0.2882702E+12 0.0000000E+00 0.0000000E+00-0.3409372E+11 102401\n" +
                " -0.1298670E+13-0.5022713E+12 0.0000000E+00 0.0000000E+00-0.9946132E+11 012401\n" +
                "  0.0000000E+00 0.0000000E+00-0.1824535E+12-0.1571595E+12 0.0000000E+00 003401\n" +
                "  0.0000000E+00 0.0000000E+00 0.2562823E+11 0.1112897E+11 0.0000000E+00 300302\n" +
                "  0.0000000E+00 0.0000000E+00 0.2908112E+11 0.1572316E+11 0.0000000E+00 210302\n" +
                "  0.0000000E+00 0.0000000E+00 0.3417652E+09 0.5702022E+10 0.0000000E+00 120302\n" +
                "  0.0000000E+00 0.0000000E+00-0.4496856E+10-0.6147421E+09 0.0000000E+00 030302\n" +
                "  0.6134450E+11 0.2623586E+11 0.0000000E+00 0.0000000E+00-0.3117259E+10 201302\n" +
                "  0.1395214E+12 0.5328142E+11 0.0000000E+00 0.0000000E+00 0.4007399E+10 111302\n" +
                "  0.8347155E+11 0.3075541E+11 0.0000000E+00 0.0000000E+00 0.4791666E+10 021302\n" +
                "  0.0000000E+00 0.0000000E+00-0.1587456E+11-0.1280915E+11 0.0000000E+00 102302\n" +
                "  0.0000000E+00 0.0000000E+00-0.1796836E+11-0.8902219E+10 0.0000000E+00 012302\n" +
                "  0.6716804E+11 0.2657742E+11 0.0000000E+00 0.0000000E+00 0.5724691E+10 003302\n" +
                " -0.2477788E+10-0.1112503E+10 0.0000000E+00 0.0000000E+00-0.1331057E+09 300203\n" +
                " -0.4374118E+10-0.1991347E+10 0.0000000E+00 0.0000000E+00-0.2736426E+09 210203\n" +
                " -0.3205178E+10-0.1446001E+10 0.0000000E+00 0.0000000E+00-0.2273776E+09 120203\n" +
                " -0.8126301E+09-0.3549043E+09 0.0000000E+00 0.0000000E+00-0.6332527E+08 030203\n" +
                "  0.0000000E+00 0.0000000E+00 0.2158745E+10 0.8595944E+09 0.0000000E+00 201203\n" +
                "  0.0000000E+00 0.0000000E+00 0.4999376E+10 0.2410345E+10 0.0000000E+00 111203\n" +
                "  0.0000000E+00 0.0000000E+00 0.2289935E+10 0.1440209E+10 0.0000000E+00 021203\n" +
                " -0.2947665E+09 0.7813326E+08 0.0000000E+00 0.0000000E+00 0.1591460E+08 102203\n" +
                " -0.4660650E+10-0.1672599E+10 0.0000000E+00 0.0000000E+00-0.3074428E+09 012203\n" +
                "  0.0000000E+00 0.0000000E+00 0.1361976E+10 0.6383285E+09 0.0000000E+00 003203\n" +
                "  0.0000000E+00 0.0000000E+00 -1248010.     -311638.3     0.0000000E+00 300104\n" +
                "  0.0000000E+00 0.0000000E+00 -6256656.     -7506123.     0.0000000E+00 210104\n" +
                "  0.0000000E+00 0.0000000E+00 -9386310.    -0.1035080E+08 0.0000000E+00 120104\n" +
                "  0.0000000E+00 0.0000000E+00 -3811871.     -2753298.     0.0000000E+00 030104\n" +
                " -0.1189371E+09-0.3842567E+08 0.0000000E+00 0.0000000E+00 -9021393.     201104\n" +
                " -0.1047286E+09-0.3832091E+08 0.0000000E+00 0.0000000E+00 -6626159.     111104\n" +
                "  -4259722.     -5428625.     0.0000000E+00 0.0000000E+00 -164119.0     021104\n" +
                "  0.0000000E+00 0.0000000E+00-0.1415317E+09-0.8434428E+08 0.0000000E+00 102104\n" +
                "  0.0000000E+00 0.0000000E+00-0.1017091E+09-0.6917861E+08 0.0000000E+00 012104\n" +
                "  0.1179868E+09 0.4557365E+08 0.0000000E+00 0.0000000E+00  7110242.     003104\n" +
                "  -32494.68     -31394.59     0.0000000E+00 0.0000000E+00 -38866.56     300005\n" +
                "  -37442.33     -67637.93     0.0000000E+00 0.0000000E+00 -67340.46     210005\n" +
                "  -35621.46     -42429.10     0.0000000E+00 0.0000000E+00 -11634.21     120005\n" +
                "  -5088.772     -6070.601     0.0000000E+00 0.0000000E+00  17522.35     030005\n" +
                "  0.0000000E+00 0.0000000E+00 -340883.3     -142117.4     0.0000000E+00 201005\n" +
                "  0.0000000E+00 0.0000000E+00 -1112389.     -588427.8     0.0000000E+00 111005\n" +
                "  0.0000000E+00 0.0000000E+00 -270834.3     -199825.0     0.0000000E+00 021005\n" +
                "   375954.6      245477.8     0.0000000E+00 0.0000000E+00 -45102.01     102005\n" +
                "  -237989.9     -46135.23     0.0000000E+00 0.0000000E+00 -32702.36     012005\n" +
                "  0.0000000E+00 0.0000000E+00  446431.0      429888.7     0.0000000E+00 003005\n" +
                " -0.4557074E+12-0.2567294E+12 0.0000000E+00 0.0000000E+00-0.1417345E+12 200600\n" +
                "  0.2944438E+12 0.4087678E+11 0.0000000E+00 0.0000000E+00-0.1302109E+12 110600\n" +
                "  0.6240956E+12 0.2611303E+12 0.0000000E+00 0.0000000E+00-0.7745502E+10 020600\n" +
                "  0.0000000E+00 0.0000000E+00-0.2626629E+12 0.1816665E+12 0.0000000E+00 101600\n" +
                "  0.0000000E+00 0.0000000E+00 0.1499999E+13 0.1351927E+13 0.0000000E+00 011600\n" +
                "  0.2398535E+13 0.1075944E+13 0.0000000E+00 0.0000000E+00 0.2557032E+12 002600\n" +
                "  0.0000000E+00 0.0000000E+00 0.2149195E+12 0.1102594E+12 0.0000000E+00 200501\n" +
                "  0.0000000E+00 0.0000000E+00 0.1283604E+12 0.1061414E+12 0.0000000E+00 110501\n" +
                "  0.0000000E+00 0.0000000E+00-0.9600910E+11-0.3576712E+10 0.0000000E+00 020501\n" +
                " -0.5048623E+12-0.1866696E+12 0.0000000E+00 0.0000000E+00-0.1705675E+11 101501\n" +
                " -0.7501681E+12-0.2966542E+12 0.0000000E+00 0.0000000E+00-0.5709308E+11 011501\n" +
                "  0.0000000E+00 0.0000000E+00-0.2747656E+12-0.1742573E+12 0.0000000E+00 002501\n" +
                "  0.3631115E+11 0.1562714E+11 0.0000000E+00 0.0000000E+00 0.6602960E+08 200402\n" +
                "  0.7711306E+11 0.2949339E+11 0.0000000E+00 0.0000000E+00 0.3684448E+10 110402\n" +
                "  0.4488927E+11 0.1635613E+11 0.0000000E+00 0.0000000E+00 0.2841231E+10 020402\n" +
                "  0.0000000E+00 0.0000000E+00-0.1191750E+11-0.8111145E+10 0.0000000E+00 101402\n" +
                "  0.0000000E+00 0.0000000E+00-0.1638343E+11-0.1020097E+11 0.0000000E+00 011402\n" +
                "  0.6286899E+11 0.2533556E+11 0.0000000E+00 0.0000000E+00 0.5770846E+10 002402\n" +
                "  0.0000000E+00 0.0000000E+00 0.1373651E+10 0.4979195E+09 0.0000000E+00 200303\n" +
                "  0.0000000E+00 0.0000000E+00 0.3170710E+10 0.1391770E+10 0.0000000E+00 110303\n" +
                "  0.0000000E+00 0.0000000E+00 0.1325281E+10 0.7982312E+09 0.0000000E+00 020303\n" +
                " -0.3519840E+09 0.9801639E+08 0.0000000E+00 0.0000000E+00-0.7101047E+08 101303\n" +
                " -0.5727067E+10-0.1990672E+10 0.0000000E+00 0.0000000E+00-0.4157452E+09 011303\n" +
                "  0.0000000E+00 0.0000000E+00 0.2365514E+10 0.1505786E+10 0.0000000E+00 002303\n" +
                " -0.8832134E+08-0.2843881E+08 0.0000000E+00 0.0000000E+00 -8997231.     200204\n" +
                "  -5300099.     -2736655.     0.0000000E+00 0.0000000E+00 -1321430.     110204\n" +
                "  0.1513526E+08  3894484.     0.0000000E+00 0.0000000E+00  964794.4     020204\n" +
                "  0.0000000E+00 0.0000000E+00-0.2550625E+09-0.1369467E+09 0.0000000E+00 101204\n" +
                "  0.0000000E+00 0.0000000E+00-0.1919025E+09-0.1190223E+09 0.0000000E+00 011204\n" +
                "  0.3034326E+09 0.1112073E+09 0.0000000E+00 0.0000000E+00 0.1864949E+08 002204\n" +
                "  0.0000000E+00 0.0000000E+00 -1272404.     -540085.7     0.0000000E+00 200105\n" +
                "  0.0000000E+00 0.0000000E+00 -1898038.     -1034011.     0.0000000E+00 110105\n" +
                "  0.0000000E+00 0.0000000E+00  94191.08     -175209.6     0.0000000E+00 020105\n" +
                "   595342.2      454270.6     0.0000000E+00 0.0000000E+00 -317750.9     101105\n" +
                "  -4119285.     -1313606.     0.0000000E+00 0.0000000E+00 -462206.0     011105\n" +
                "  0.0000000E+00 0.0000000E+00  1487373.      1833012.     0.0000000E+00 002105\n" +
                "  -9240.665     -577.2847     0.0000000E+00 0.0000000E+00  2783.345     200006\n" +
                "  -15131.13     -720.2461     0.0000000E+00 0.0000000E+00  4791.648     110006\n" +
                "  -5563.748      766.8645     0.0000000E+00 0.0000000E+00  977.1711     020006\n" +
                "  0.0000000E+00 0.0000000E+00  52989.00      23226.73     0.0000000E+00 101006\n" +
                "  0.0000000E+00 0.0000000E+00  44276.38      16077.47     0.0000000E+00 011006\n" +
                "   33768.12      15117.06     0.0000000E+00 0.0000000E+00 -857.6848     002006\n" +
                "  0.0000000E+00 0.0000000E+00 0.1489594E+11 0.1019360E+12 0.0000000E+00 100700\n" +
                "  0.0000000E+00 0.0000000E+00 0.5891883E+12 0.4690930E+12 0.0000000E+00 010700\n" +
                "  0.9052683E+12 0.4625568E+12 0.0000000E+00 0.0000000E+00 0.9135193E+11 001700\n" +
                " -0.1204594E+12-0.4241593E+11 0.0000000E+00 0.0000000E+00-0.2535958E+10 100601\n" +
                " -0.1227485E+12-0.5189314E+11 0.0000000E+00 0.0000000E+00-0.9775724E+10 010601\n" +
                "  0.0000000E+00 0.0000000E+00-0.2775602E+12-0.1262502E+12 0.0000000E+00 001601\n" +
                "  0.0000000E+00 0.0000000E+00-0.7785026E+10-0.3231102E+10 0.0000000E+00 100502\n" +
                "  0.0000000E+00 0.0000000E+00-0.1000171E+11-0.6164256E+10 0.0000000E+00 010502\n" +
                "  0.1964902E+11 0.8739551E+10 0.0000000E+00 0.0000000E+00 0.2044216E+10 001502\n" +
                " -0.2683708E+09  7184711.     0.0000000E+00 0.0000000E+00-0.8445967E+08 100403\n" +
                " -0.3123226E+10-0.1048869E+10 0.0000000E+00 0.0000000E+00-0.2545495E+09 010403\n" +
                "  0.0000000E+00 0.0000000E+00 0.1878528E+10 0.1614853E+10 0.0000000E+00 001403\n" +
                "  0.0000000E+00 0.0000000E+00-0.1408236E+09-0.6787146E+08 0.0000000E+00 100304\n" +
                "  0.0000000E+00 0.0000000E+00-0.9188895E+08-0.5555924E+08 0.0000000E+00 010304\n" +
                "  0.3652356E+09 0.1259549E+09 0.0000000E+00 0.0000000E+00 0.2173815E+08 001304\n" +
                "  -4131658.     -1141287.     0.0000000E+00 0.0000000E+00 -725955.5     100205\n" +
                "  -6172319.     -2127974.     0.0000000E+00 0.0000000E+00 -584130.1     010205\n" +
                "  0.0000000E+00 0.0000000E+00  4021004.      3090365.     0.0000000E+00 001205\n" +
                "  0.0000000E+00 0.0000000E+00  107860.1      37538.59     0.0000000E+00 100106\n" +
                "  0.0000000E+00 0.0000000E+00  37743.90      1560.817     0.0000000E+00 010106\n" +
                "   324936.5      119855.3     0.0000000E+00 0.0000000E+00  16564.21     001106\n" +
                "   380.8219     -31.37872     0.0000000E+00 0.0000000E+00 -264.7419     100007\n" +
                "   531.2300     -117.8954     0.0000000E+00 0.0000000E+00 -230.2314     010007\n" +
                "  0.0000000E+00 0.0000000E+00 -1215.488     -937.1642     0.0000000E+00 001007\n" +
                "  0.1310502E+12 0.8626544E+11 0.0000000E+00 0.0000000E+00 0.1037242E+11 000800\n" +
                "  0.0000000E+00 0.0000000E+00-0.1291750E+12-0.4707459E+11 0.0000000E+00 000701\n" +
                " -0.5815834E+10-0.1646563E+10 0.0000000E+00 0.0000000E+00-0.4654190E+09 000602\n" +
                "  0.0000000E+00 0.0000000E+00 0.8264748E+09 0.7503859E+09 0.0000000E+00 000503\n" +
                "  0.1965972E+09 0.6367714E+08 0.0000000E+00 0.0000000E+00 0.1229455E+08 000404\n" +
                "  0.0000000E+00 0.0000000E+00  2519446.      1384140.     0.0000000E+00 000305\n" +
                "   537769.7      194014.2     0.0000000E+00 0.0000000E+00  27975.15     000206\n" +
                "  0.0000000E+00 0.0000000E+00  1399.074      505.9130     0.0000000E+00 000107\n" +
                "  -33.11657      3.134926     0.0000000E+00 0.0000000E+00  6.118988     000008\n" +
                "  0.2348319E+12 0.1453350E+12 0.0000000E+00 0.0000000E+00 0.2138534E+11 900000\n" +
                "  0.1112019E+13 0.3744219E+12 0.0000000E+00 0.0000000E+00-0.7827278E+11 810000\n" +
                "  0.1866964E+13-0.1038596E+12 0.0000000E+00 0.0000000E+00-0.6101313E+12 720000\n" +
                "  0.3582034E+12-0.8359675E+12 0.0000000E+00 0.0000000E+00-0.8938650E+12 630000\n" +
                " -0.3655783E+13-0.1639308E+12 0.0000000E+00 0.0000000E+00 0.2266741E+12 540000\n" +
                " -0.6639257E+13 0.6272652E+12 0.0000000E+00 0.0000000E+00 0.1588464E+13 450000\n" +
                " -0.5679354E+13-0.1701532E+12 0.0000000E+00 0.0000000E+00 0.1380756E+13 360000\n" +
                " -0.2576235E+13-0.8328353E+12 0.0000000E+00 0.0000000E+00 0.4166093E+12 270000\n" +
                " -0.5328424E+12-0.4316200E+12 0.0000000E+00 0.0000000E+00 0.9455530E+09 180000\n" +
                " -0.2148810E+11-0.5699648E+11 0.0000000E+00 0.0000000E+00-0.1310857E+11 090000\n" +
                "  0.0000000E+00 0.0000000E+00-0.1641615E+13-0.4808063E+12 0.0000000E+00 801000\n" +
                "  0.0000000E+00 0.0000000E+00-0.1554913E+13-0.1126871E+13 0.0000000E+00 711000\n" +
                "  0.0000000E+00 0.0000000E+00 0.1900465E+14 0.3994819E+13 0.0000000E+00 621000\n" +
                "  0.0000000E+00 0.0000000E+00 0.5945306E+14 0.1955871E+14 0.0000000E+00 531000\n" +
                "  0.0000000E+00 0.0000000E+00 0.6782757E+14 0.2949815E+14 0.0000000E+00 441000\n" +
                "  0.0000000E+00 0.0000000E+00 0.2109499E+14 0.1623639E+14 0.0000000E+00 351000\n" +
                "  0.0000000E+00 0.0000000E+00-0.1910871E+14-0.2161469E+13 0.0000000E+00 261000\n" +
                "  0.0000000E+00 0.0000000E+00-0.1584732E+14-0.4928102E+13 0.0000000E+00 171000\n" +
                "  0.0000000E+00 0.0000000E+00-0.2988951E+13-0.1128518E+13 0.0000000E+00 081000\n" +
                "  0.9946175E+13 0.3664164E+13 0.0000000E+00 0.0000000E+00-0.9123998E+12 702000\n" +
                "  0.9057710E+13 0.5377802E+13 0.0000000E+00 0.0000000E+00-0.3809584E+13 612000\n" +
                " -0.7995623E+14-0.2230733E+14 0.0000000E+00 0.0000000E+00-0.7713944E+13 522000\n" +
                " -0.2177799E+15-0.7253631E+14 0.0000000E+00 0.0000000E+00-0.9817556E+13 432000\n" +
                " -0.2256454E+15-0.8223862E+14 0.0000000E+00 0.0000000E+00-0.6910812E+13 342000\n" +
                " -0.9667737E+14-0.3887141E+14 0.0000000E+00 0.0000000E+00-0.1083775E+13 252000\n" +
                " -0.5701408E+13-0.3957569E+13 0.0000000E+00 0.0000000E+00 0.1322674E+13 162000\n" +
                "  0.4250202E+13 0.1277458E+13 0.0000000E+00 0.0000000E+00 0.4963720E+12 072000\n" +
                "  0.0000000E+00 0.0000000E+00-0.1193190E+14-0.5274491E+13 0.0000000E+00 603000\n" +
                "  0.0000000E+00 0.0000000E+00-0.3604027E+13-0.6589754E+12 0.0000000E+00 513000\n" +
                "  0.0000000E+00 0.0000000E+00 0.6226363E+14 0.3023868E+14 0.0000000E+00 423000\n" +
                "  0.0000000E+00 0.0000000E+00 0.9445004E+14 0.4394160E+14 0.0000000E+00 333000\n" +
                "  0.0000000E+00 0.0000000E+00 0.5134289E+14 0.2239257E+14 0.0000000E+00 243000\n" +
                "  0.0000000E+00 0.0000000E+00 0.1139033E+14 0.4004907E+13 0.0000000E+00 153000\n" +
                "  0.0000000E+00 0.0000000E+00 0.9642426E+12 0.1151105E+12 0.0000000E+00 063000\n" +
                " -0.5051115E+13-0.2119618E+13 0.0000000E+00 0.0000000E+00-0.1573266E+13 504000\n" +
                " -0.7670778E+13-0.3639369E+13 0.0000000E+00 0.0000000E+00-0.5261473E+13 414000\n" +
                "  0.5964298E+13 0.1332569E+13 0.0000000E+00 0.0000000E+00-0.6906791E+13 324000\n" +
                "  0.1492039E+14 0.5254455E+13 0.0000000E+00 0.0000000E+00-0.4170748E+13 234000\n" +
                "  0.6639645E+13 0.2580204E+13 0.0000000E+00 0.0000000E+00-0.1172051E+13 144000\n" +
                "  0.4055540E+12 0.2114128E+12 0.0000000E+00 0.0000000E+00-0.1472104E+12 054000\n" +
                "  0.0000000E+00 0.0000000E+00-0.6883564E+13-0.3061761E+13 0.0000000E+00 405000\n" +
                "  0.0000000E+00 0.0000000E+00-0.1349496E+14-0.6035090E+13 0.0000000E+00 315000\n" +
                "  0.0000000E+00 0.0000000E+00-0.9293104E+13-0.4270400E+13 0.0000000E+00 225000\n" +
                "  0.0000000E+00 0.0000000E+00-0.2221557E+13-0.1207424E+13 0.0000000E+00 135000\n" +
                "  0.0000000E+00 0.0000000E+00-0.1533089E+11-0.1087405E+12 0.0000000E+00 045000\n" +
                " -0.5989541E+12-0.2964604E+12 0.0000000E+00 0.0000000E+00 0.3384446E+12 306000\n" +
                "  0.1741909E+13 0.5426325E+12 0.0000000E+00 0.0000000E+00 0.6451362E+12 216000\n" +
                "  0.2267796E+13 0.8086844E+12 0.0000000E+00 0.0000000E+00 0.3530124E+12 126000\n" +
                "  0.2185561E+12 0.8241081E+11 0.0000000E+00 0.0000000E+00 0.1594020E+11 036000\n" +
                "  0.0000000E+00 0.0000000E+00-0.1709468E+12-0.9300904E+11 0.0000000E+00 207000\n" +
                "  0.0000000E+00 0.0000000E+00-0.4318219E+12-0.2422797E+12 0.0000000E+00 117000\n" +
                "  0.0000000E+00 0.0000000E+00-0.2488846E+12-0.1366527E+12 0.0000000E+00 027000\n" +
                " -0.5543329E+12-0.1948997E+12 0.0000000E+00 0.0000000E+00-0.5193995E+11 108000\n" +
                " -0.2629642E+12-0.9610476E+11 0.0000000E+00 0.0000000E+00-0.2639727E+11 018000\n" +
                "  0.0000000E+00 0.0000000E+00-0.5307236E+10 0.9713648E+09 0.0000000E+00 009000\n" +
                "  0.0000000E+00 0.0000000E+00-0.2198351E+13-0.6439706E+12 0.0000000E+00 800100\n" +
                "  0.0000000E+00 0.0000000E+00-0.1144192E+13-0.1386335E+13 0.0000000E+00 710100\n" +
                "  0.0000000E+00 0.0000000E+00 0.3118348E+14 0.7060072E+13 0.0000000E+00 620100\n" +
                "  0.0000000E+00 0.0000000E+00 0.9388489E+14 0.3229731E+14 0.0000000E+00 530100\n" +
                "  0.0000000E+00 0.0000000E+00 0.1052782E+15 0.4749567E+14 0.0000000E+00 440100\n" +
                "  0.0000000E+00 0.0000000E+00 0.2751700E+14 0.2379622E+14 0.0000000E+00 350100\n" +
                "  0.0000000E+00 0.0000000E+00-0.3706454E+14-0.6432095E+13 0.0000000E+00 260100\n" +
                "  0.0000000E+00 0.0000000E+00-0.2791444E+14-0.9091470E+13 0.0000000E+00 170100\n" +
                "  0.0000000E+00 0.0000000E+00-0.4961746E+13-0.1898755E+13 0.0000000E+00 080100\n" +
                "  0.3040412E+14 0.1074871E+14 0.0000000E+00 0.0000000E+00-0.2241233E+13 701100\n" +
                "  0.3630551E+14 0.1882255E+14 0.0000000E+00 0.0000000E+00-0.9465945E+13 611100\n" +
                " -0.2037916E+15-0.5276152E+14 0.0000000E+00 0.0000000E+00-0.2126056E+14 521100\n" +
                " -0.6020246E+15-0.1969459E+15 0.0000000E+00 0.0000000E+00-0.3157393E+14 431100\n" +
                " -0.6455623E+15-0.2346244E+15 0.0000000E+00 0.0000000E+00-0.2631155E+14 341100\n" +
                " -0.2765726E+15-0.1118013E+15 0.0000000E+00 0.0000000E+00-0.7304984E+13 251100\n" +
                " -0.1081664E+14-0.9517287E+13 0.0000000E+00 0.0000000E+00 0.2812309E+13 161100\n" +
                "  0.1366374E+14 0.4223212E+13 0.0000000E+00 0.0000000E+00 0.1354661E+13 071100\n" +
                "  0.0000000E+00 0.0000000E+00-0.5528931E+14-0.2567960E+14 0.0000000E+00 602100\n" +
                "  0.0000000E+00 0.0000000E+00-0.3396487E+14-0.1264816E+14 0.0000000E+00 512100\n" +
                "  0.0000000E+00 0.0000000E+00 0.1882742E+15 0.9818733E+14 0.0000000E+00 422100\n" +
                "  0.0000000E+00 0.0000000E+00 0.2589957E+15 0.1311587E+15 0.0000000E+00 332100\n" +
                "  0.0000000E+00 0.0000000E+00 0.1041470E+15 0.4874699E+14 0.0000000E+00 242100\n" +
                "  0.0000000E+00 0.0000000E+00 0.1122108E+14 0.6093565E+12 0.0000000E+00 152100\n" +
                "  0.0000000E+00 0.0000000E+00 0.1153999E+13-0.1192879E+13 0.0000000E+00 062100\n" +
                " -0.3442719E+14-0.1296794E+14 0.0000000E+00 0.0000000E+00-0.2854294E+13 503100\n" +
                " -0.6212171E+14-0.2652403E+14 0.0000000E+00 0.0000000E+00-0.1461398E+14 413100\n" +
                "  0.2744455E+14 0.1899575E+13 0.0000000E+00 0.0000000E+00-0.2311721E+14 323100\n" +
                "  0.1145042E+15 0.3611745E+14 0.0000000E+00 0.0000000E+00-0.1375069E+14 233100\n" +
                "  0.7049452E+14 0.2495132E+14 0.0000000E+00 0.0000000E+00-0.3039189E+13 143100\n" +
                "  0.1108577E+14 0.4290143E+13 0.0000000E+00 0.0000000E+00-0.3139715E+12 053100\n" +
                "  0.0000000E+00 0.0000000E+00-0.2639032E+14-0.1127699E+14 0.0000000E+00 404100\n" +
                "  0.0000000E+00 0.0000000E+00-0.6299453E+14-0.2786244E+14 0.0000000E+00 314100\n" +
                "  0.0000000E+00 0.0000000E+00-0.6179633E+14-0.2942398E+14 0.0000000E+00 224100\n" +
                "  0.0000000E+00 0.0000000E+00-0.2219466E+14-0.1292535E+14 0.0000000E+00 134100\n" +
                "  0.0000000E+00 0.0000000E+00-0.1429951E+13-0.1903925E+13 0.0000000E+00 044100\n" +
                " -0.5041271E+13-0.2478429E+13 0.0000000E+00 0.0000000E+00 0.2629347E+13 305100\n" +
                "  0.2314728E+14 0.7861108E+13 0.0000000E+00 0.0000000E+00 0.6160044E+13 215100\n" +
                "  0.2242493E+14 0.8314885E+13 0.0000000E+00 0.0000000E+00 0.3675330E+13 125100\n" +
                " -0.5710544E+12-0.9243288E+11 0.0000000E+00 0.0000000E+00 0.9048725E+11 035100\n" +
                "  0.0000000E+00 0.0000000E+00 0.8396401E+12 0.1907661E+12 0.0000000E+00 206100\n" +
                "  0.0000000E+00 0.0000000E+00-0.2124876E+13-0.1628541E+13 0.0000000E+00 116100\n" +
                "  0.0000000E+00 0.0000000E+00-0.2279326E+13-0.1411852E+13 0.0000000E+00 026100\n" +
                " -0.7221828E+13-0.2513899E+13 0.0000000E+00 0.0000000E+00-0.7617841E+12 107100\n" +
                " -0.4188822E+13-0.1525658E+13 0.0000000E+00 0.0000000E+00-0.4623261E+12 017100\n" +
                "  0.0000000E+00 0.0000000E+00-0.1921330E+12-0.2532797E+11 0.0000000E+00 008100\n" +
                " -0.3842998E+11-0.4206776E+10 0.0000000E+00 0.0000000E+00 0.3695723E+10 800001\n" +
                " -0.8821861E+11-0.2154497E+11 0.0000000E+00 0.0000000E+00 0.2073572E+11 710001\n" +
                "  0.1530393E+12 0.2416959E+11 0.0000000E+00 0.0000000E+00 0.5418804E+11 620001\n" +
                "  0.7464986E+12 0.2212378E+12 0.0000000E+00 0.0000000E+00 0.3934418E+11 530001\n" +
                "  0.1007280E+13 0.3474425E+12 0.0000000E+00 0.0000000E+00-0.8600192E+11 440001\n" +
                "  0.5333883E+12 0.1893237E+12 0.0000000E+00 0.0000000E+00-0.1786054E+12 350001\n" +
                " -0.2535586E+11-0.6285077E+10 0.0000000E+00 0.0000000E+00-0.1156921E+12 260001\n" +
                " -0.1265127E+12-0.3572172E+11 0.0000000E+00 0.0000000E+00-0.2591152E+11 170001\n" +
                " -0.3188779E+11-0.7789691E+10 0.0000000E+00 0.0000000E+00-0.5120404E+09 080001\n" +
                "  0.0000000E+00 0.0000000E+00-0.2888445E+12-0.7875014E+11 0.0000000E+00 701001\n" +
                "  0.0000000E+00 0.0000000E+00-0.8312567E+12-0.4021806E+12 0.0000000E+00 611001\n" +
                "  0.0000000E+00 0.0000000E+00 0.5255736E+12-0.2655178E+12 0.0000000E+00 521001\n" +
                "  0.0000000E+00 0.0000000E+00 0.4718109E+13 0.1377432E+13 0.0000000E+00 431001\n" +
                "  0.0000000E+00 0.0000000E+00 0.6614937E+13 0.2709587E+13 0.0000000E+00 341001\n" +
                "  0.0000000E+00 0.0000000E+00 0.3574232E+13 0.1771811E+13 0.0000000E+00 251001\n" +
                "  0.0000000E+00 0.0000000E+00 0.5681255E+12 0.3815217E+12 0.0000000E+00 161001\n" +
                "  0.0000000E+00 0.0000000E+00-0.5141968E+11-0.3739514E+10 0.0000000E+00 071001\n" +
                "  0.3384572E+13 0.1181895E+13 0.0000000E+00 0.0000000E+00 0.1605194E+11 602001\n" +
                "  0.1330024E+14 0.5144137E+13 0.0000000E+00 0.0000000E+00 0.1339448E+12 512001\n" +
                "  0.1654473E+14 0.7271754E+13 0.0000000E+00 0.0000000E+00-0.6504972E+11 422001\n" +
                " -0.1289385E+13 0.9120813E+12 0.0000000E+00 0.0000000E+00-0.1030239E+13 332001\n" +
                " -0.1723816E+14-0.5797084E+13 0.0000000E+00 0.0000000E+00-0.1528599E+13 242001\n" +
                " -0.1135707E+14-0.4179205E+13 0.0000000E+00 0.0000000E+00-0.8145139E+12 152001\n" +
                " -0.2094115E+13-0.7982159E+12 0.0000000E+00 0.0000000E+00-0.1382249E+12 062001\n" +
                "  0.0000000E+00 0.0000000E+00-0.5078491E+13-0.2532601E+13 0.0000000E+00 503001\n" +
                "  0.0000000E+00 0.0000000E+00-0.1254159E+14-0.6037062E+13 0.0000000E+00 413001\n" +
                "  0.0000000E+00 0.0000000E+00-0.1116731E+14-0.4945516E+13 0.0000000E+00 323001\n" +
                "  0.0000000E+00 0.0000000E+00-0.3117538E+13-0.9821799E+12 0.0000000E+00 233001\n" +
                "  0.0000000E+00 0.0000000E+00 0.8942075E+12 0.6518463E+12 0.0000000E+00 143001\n" +
                "  0.0000000E+00 0.0000000E+00 0.4483157E+12 0.2520556E+12 0.0000000E+00 053001\n" +
                "  0.3547944E+12 0.1233375E+12 0.0000000E+00 0.0000000E+00 0.6110338E+12 404001\n" +
                "  0.1286726E+13 0.4462120E+12 0.0000000E+00 0.0000000E+00 0.1158952E+13 314001\n" +
                "  0.1583410E+13 0.5325704E+12 0.0000000E+00 0.0000000E+00 0.7156725E+12 224001\n" +
                "  0.6312463E+12 0.1995769E+12 0.0000000E+00 0.0000000E+00 0.1078081E+12 134001\n" +
                "  0.1765971E+12 0.5931704E+11 0.0000000E+00 0.0000000E+00-0.1262685E+11 044001\n" +
                "  0.0000000E+00 0.0000000E+00 0.4069008E+12 0.1822001E+12 0.0000000E+00 305001\n" +
                "  0.0000000E+00 0.0000000E+00 0.6551545E+11 0.7273547E+11 0.0000000E+00 215001\n" +
                "  0.0000000E+00 0.0000000E+00-0.3744604E+12-0.1053678E+12 0.0000000E+00 125001\n" +
                "  0.0000000E+00 0.0000000E+00-0.6906834E+11-0.1825497E+11 0.0000000E+00 035001\n" +
                " -0.9978228E+12-0.3758274E+12 0.0000000E+00 0.0000000E+00-0.9446867E+11 206001\n" +
                " -0.1740850E+12-0.7732802E+11 0.0000000E+00 0.0000000E+00-0.1378023E+11 116001\n" +
                "  0.3667241E+12 0.1325496E+12 0.0000000E+00 0.0000000E+00 0.3653385E+11 026001\n" +
                "  0.0000000E+00 0.0000000E+00 0.1014575E+12 0.5087700E+11 0.0000000E+00 107001\n" +
                "  0.0000000E+00 0.0000000E+00 0.9859171E+11 0.4994976E+11 0.0000000E+00 017001\n" +
                "  0.2792340E+11 0.1093708E+11 0.0000000E+00 0.0000000E+00 0.2293857E+10 008001\n" +
                "  0.2295691E+14 0.7608444E+13 0.0000000E+00 0.0000000E+00-0.1332624E+13 700200\n" +
                "  0.3504476E+14 0.1619486E+14 0.0000000E+00 0.0000000E+00-0.5439868E+13 610200\n" +
                " -0.1217651E+15-0.2698657E+14 0.0000000E+00 0.0000000E+00-0.1314953E+14 520200\n" +
                " -0.4104331E+15-0.1298184E+15 0.0000000E+00 0.0000000E+00-0.2237963E+14 430200\n" +
                " -0.4588902E+15-0.1653558E+15 0.0000000E+00 0.0000000E+00-0.2113620E+14 340200\n" +
                " -0.1922762E+15-0.7862584E+14 0.0000000E+00 0.0000000E+00-0.7078161E+13 250200\n" +
                " -0.4568120E+12-0.4681950E+13 0.0000000E+00 0.0000000E+00 0.1610730E+13 160200\n" +
                "  0.1096078E+14 0.3391564E+13 0.0000000E+00 0.0000000E+00 0.9151816E+12 070200\n" +
                "  0.0000000E+00 0.0000000E+00-0.7615365E+14-0.3765031E+14 0.0000000E+00 601200\n" +
                "  0.0000000E+00 0.0000000E+00-0.5015808E+14-0.2121061E+14 0.0000000E+00 511200\n" +
                "  0.0000000E+00 0.0000000E+00 0.1768652E+15 0.1070672E+15 0.0000000E+00 421200\n" +
                "  0.0000000E+00 0.0000000E+00 0.1621162E+15 0.1104349E+15 0.0000000E+00 331200\n" +
                "  0.0000000E+00 0.0000000E+00-0.2906086E+14-0.5381079E+12 0.0000000E+00 241200\n" +
                "  0.0000000E+00 0.0000000E+00-0.4622594E+14-0.2617084E+14 0.0000000E+00 151200\n" +
                "  0.0000000E+00 0.0000000E+00-0.3564787E+13-0.4691485E+13 0.0000000E+00 061200\n" +
                " -0.9879557E+14-0.3340683E+14 0.0000000E+00 0.0000000E+00 0.3726240E+13 502200\n" +
                " -0.2212210E+15-0.8612409E+14 0.0000000E+00 0.0000000E+00-0.9171867E+13 412200\n" +
                " -0.2481639E+14-0.3500171E+14 0.0000000E+00 0.0000000E+00-0.2641921E+14 322200\n" +
                "  0.2758299E+15 0.7739684E+14 0.0000000E+00 0.0000000E+00-0.1312398E+14 232200\n" +
                "  0.2232693E+15 0.7635472E+14 0.0000000E+00 0.0000000E+00 0.7371523E+12 142200\n" +
                "  0.4384355E+14 0.1644046E+14 0.0000000E+00 0.0000000E+00 0.4599020E+12 052200\n" +
                "  0.0000000E+00 0.0000000E+00-0.2011649E+14-0.5112416E+13 0.0000000E+00 403200\n" +
                "  0.0000000E+00 0.0000000E+00-0.1256951E+15-0.5252906E+14 0.0000000E+00 313200\n" +
                "  0.0000000E+00 0.0000000E+00-0.1947280E+15-0.9528407E+14 0.0000000E+00 223200\n" +
                "  0.0000000E+00 0.0000000E+00-0.7830838E+14-0.4974791E+14 0.0000000E+00 133200\n" +
                "  0.0000000E+00 0.0000000E+00-0.2590259E+13-0.7167551E+13 0.0000000E+00 043200\n" +
                " -0.2075538E+14-0.1038796E+14 0.0000000E+00 0.0000000E+00 0.6041088E+13 304200\n" +
                "  0.1181047E+15 0.4102740E+14 0.0000000E+00 0.0000000E+00 0.2193757E+14 214200\n" +
                "  0.9134511E+14 0.3501586E+14 0.0000000E+00 0.0000000E+00 0.1386533E+14 124200\n" +
                " -0.1426519E+14-0.4285150E+13 0.0000000E+00 0.0000000E+00-0.5555304E+12 034200\n" +
                "  0.0000000E+00 0.0000000E+00 0.8749717E+13 0.3044421E+13 0.0000000E+00 205200\n" +
                "  0.0000000E+00 0.0000000E+00-0.3581989E+13-0.5695446E+13 0.0000000E+00 115200\n" +
                "  0.0000000E+00 0.0000000E+00-0.9533446E+13-0.6844471E+13 0.0000000E+00 025200\n" +
                " -0.4113192E+14-0.1416842E+14 0.0000000E+00 0.0000000E+00-0.4563543E+13 106200\n" +
                " -0.2993275E+14-0.1088476E+14 0.0000000E+00 0.0000000E+00-0.3341064E+13 016200\n" +
                "  0.0000000E+00 0.0000000E+00-0.2106275E+13-0.4585505E+12 0.0000000E+00 007200\n" +
                "  0.0000000E+00 0.0000000E+00-0.4841571E+12-0.1333225E+12 0.0000000E+00 700101\n" +
                "  0.0000000E+00 0.0000000E+00-0.1376233E+13-0.6570219E+12 0.0000000E+00 610101\n" +
                "  0.0000000E+00 0.0000000E+00 0.9964059E+12-0.3774480E+12 0.0000000E+00 520101\n" +
                "  0.0000000E+00 0.0000000E+00 0.7994797E+13 0.2324646E+13 0.0000000E+00 430101\n" +
                "  0.0000000E+00 0.0000000E+00 0.1110518E+14 0.4493209E+13 0.0000000E+00 340101\n" +
                "  0.0000000E+00 0.0000000E+00 0.6342480E+13 0.3064686E+13 0.0000000E+00 250101\n" +
                "  0.0000000E+00 0.0000000E+00 0.1375447E+13 0.7800890E+12 0.0000000E+00 160101\n" +
                "  0.0000000E+00 0.0000000E+00 0.1882558E+11 0.2700662E+11 0.0000000E+00 070101\n" +
                "  0.1023903E+14 0.3506181E+13 0.0000000E+00 0.0000000E+00 0.3789989E+11 601101\n" +
                "  0.4150526E+14 0.1592490E+14 0.0000000E+00 0.0000000E+00 0.3756273E+12 511101\n" +
                "  0.5175063E+14 0.2274926E+14 0.0000000E+00 0.0000000E+00-0.3173998E+12 421101\n" +
                " -0.5947849E+13 0.2375120E+13 0.0000000E+00 0.0000000E+00-0.3371268E+13 331101\n" +
                " -0.5615577E+14-0.1868252E+14 0.0000000E+00 0.0000000E+00-0.4803416E+13 241101\n" +
                " -0.3624154E+14-0.1319470E+14 0.0000000E+00 0.0000000E+00-0.2486961E+13 151101\n" +
                " -0.6614065E+13-0.2496272E+13 0.0000000E+00 0.0000000E+00-0.4122064E+12 061101\n" +
                "  0.0000000E+00 0.0000000E+00-0.1954001E+14-0.9912323E+13 0.0000000E+00 502101\n" +
                "  0.0000000E+00 0.0000000E+00-0.4698060E+14-0.2286162E+14 0.0000000E+00 412101\n" +
                "  0.0000000E+00 0.0000000E+00-0.3841964E+14-0.1695063E+14 0.0000000E+00 322101\n" +
                "  0.0000000E+00 0.0000000E+00-0.6568050E+13-0.1179888E+13 0.0000000E+00 232101\n" +
                "  0.0000000E+00 0.0000000E+00 0.4893086E+13 0.3297020E+13 0.0000000E+00 142101\n" +
                "  0.0000000E+00 0.0000000E+00 0.1406781E+13 0.8824217E+12 0.0000000E+00 052101\n" +
                " -0.1382982E+13-0.2799268E+12 0.0000000E+00 0.0000000E+00 0.2812611E+13 403101\n" +
                " -0.5496779E+13-0.1812116E+13 0.0000000E+00 0.0000000E+00 0.5021820E+13 313101\n" +
                " -0.1003930E+14-0.4058662E+13 0.0000000E+00 0.0000000E+00 0.2735584E+13 223101\n" +
                " -0.6357260E+13-0.2758334E+13 0.0000000E+00 0.0000000E+00 0.2097463E+12 133101\n" +
                "  0.1933455E+12-0.2725551E+11 0.0000000E+00 0.0000000E+00-0.2694237E+11 043101\n" +
                "  0.0000000E+00 0.0000000E+00 0.5905741E+13 0.2913302E+13 0.0000000E+00 304101\n" +
                "  0.0000000E+00 0.0000000E+00 0.4195495E+13 0.2646373E+13 0.0000000E+00 214101\n" +
                "  0.0000000E+00 0.0000000E+00-0.8796596E+12 0.3110935E+12 0.0000000E+00 124101\n" +
                "  0.0000000E+00 0.0000000E+00-0.7605347E+11 0.7944091E+11 0.0000000E+00 034101\n" +
                " -0.9644008E+13-0.3642124E+13 0.0000000E+00 0.0000000E+00-0.1158029E+13 205101\n" +
                " -0.1377257E+13-0.6509010E+12 0.0000000E+00 0.0000000E+00-0.3766526E+12 115101";

        string = string +
                "  0.3996139E+13 0.1447414E+13 0.0000000E+00 0.0000000E+00 0.3058812E+12 025101\n" +
                "  0.0000000E+00 0.0000000E+00 0.5799455E+12 0.3479186E+12 0.0000000E+00 106101\n" +
                "  0.0000000E+00 0.0000000E+00 0.9523989E+12 0.5087193E+12 0.0000000E+00 016101\n" +
                "  0.6411413E+12 0.2458957E+12 0.0000000E+00 0.0000000E+00 0.5539959E+11 007101\n" +
                "  0.2961630E+10 0.1958477E+10 0.0000000E+00 0.0000000E+00-0.1164098E+09 700002\n" +
                "  0.1102531E+11 0.3141801E+10 0.0000000E+00 0.0000000E+00-0.2097282E+10 610002\n" +
                "  0.1117336E+11-0.3758304E+10 0.0000000E+00 0.0000000E+00-0.7853042E+10 520002\n" +
                " -0.1229183E+11-0.1033126E+11 0.0000000E+00 0.0000000E+00-0.9897967E+10 430002\n" +
                " -0.4720007E+11-0.9026866E+10 0.0000000E+00 0.0000000E+00-0.7792083E+09 340002\n" +
                " -0.5346749E+11-0.6897527E+10 0.0000000E+00 0.0000000E+00 0.6621570E+10 250002\n" +
                " -0.2552276E+11-0.4256077E+10 0.0000000E+00 0.0000000E+00 0.3808865E+10 160002\n" +
                " -0.3976517E+10-0.1007017E+10 0.0000000E+00 0.0000000E+00 0.4551236E+09 070002\n" +
                "  0.0000000E+00 0.0000000E+00-0.4069845E+11-0.1065711E+11 0.0000000E+00 601002\n" +
                "  0.0000000E+00 0.0000000E+00-0.1263553E+12-0.5526677E+11 0.0000000E+00 511002\n" +
                "  0.0000000E+00 0.0000000E+00-0.2082014E+11-0.4921102E+11 0.0000000E+00 421002\n" +
                "  0.0000000E+00 0.0000000E+00 0.2615653E+12 0.7136589E+11 0.0000000E+00 331002\n" +
                "  0.0000000E+00 0.0000000E+00 0.2712838E+12 0.1222243E+12 0.0000000E+00 241002\n" +
                "  0.0000000E+00 0.0000000E+00 0.7772954E+11 0.5187031E+11 0.0000000E+00 151002\n" +
                "  0.0000000E+00 0.0000000E+00 0.1578716E+10 0.4921436E+10 0.0000000E+00 061002\n" +
                "  0.1945616E+12 0.5075744E+11 0.0000000E+00 0.0000000E+00 0.2193587E+10 502002\n" +
                "  0.1401282E+13 0.5210866E+12 0.0000000E+00 0.0000000E+00 0.5229268E+11 412002\n" +
                "  0.2713528E+13 0.1112253E+13 0.0000000E+00 0.0000000E+00 0.1115553E+12 322002\n" +
                "  0.1968556E+13 0.8562011E+12 0.0000000E+00 0.0000000E+00 0.7287787E+11 232002\n" +
                "  0.4751971E+12 0.2262891E+12 0.0000000E+00 0.0000000E+00 0.8382872E+10 142002\n" +
                " -0.4970682E+10 0.3920211E+10 0.0000000E+00 0.0000000E+00-0.3743450E+10 052002\n" +
                "  0.0000000E+00 0.0000000E+00-0.6768546E+11-0.4789810E+11 0.0000000E+00 403002\n" +
                "  0.0000000E+00 0.0000000E+00-0.6181899E+12-0.3241757E+12 0.0000000E+00 313002\n" +
                "  0.0000000E+00 0.0000000E+00-0.1140182E+13-0.5722693E+12 0.0000000E+00 223002\n" +
                "  0.0000000E+00 0.0000000E+00-0.7171158E+12-0.3632865E+12 0.0000000E+00 133002\n" +
                "  0.0000000E+00 0.0000000E+00-0.1461445E+12-0.7654198E+11 0.0000000E+00 043002\n" +
                " -0.1609513E+12-0.6491396E+11 0.0000000E+00 0.0000000E+00 0.5053945E+10 304002\n" +
                "  0.5591464E+11 0.1574836E+11 0.0000000E+00 0.0000000E+00 0.6013547E+11 214002\n" +
                "  0.1752016E+12 0.6313075E+11 0.0000000E+00 0.0000000E+00 0.6881617E+11 124002\n" +
                "  0.2034202E+11 0.6927859E+10 0.0000000E+00 0.0000000E+00 0.1761730E+11 034002\n" +
                "  0.0000000E+00 0.0000000E+00 0.9309177E+11 0.4399552E+11 0.0000000E+00 205002\n" +
                "  0.0000000E+00 0.0000000E+00 0.5509652E+11 0.2923014E+11 0.0000000E+00 115002\n" +
                "  0.0000000E+00 0.0000000E+00-0.1077197E+11-0.8567758E+09 0.0000000E+00 025002\n" +
                " -0.8666644E+11-0.3225620E+11 0.0000000E+00 0.0000000E+00-0.8155453E+10 106002\n" +
                " -0.8999100E+11-0.3428655E+11 0.0000000E+00 0.0000000E+00-0.7630899E+10 016002\n" +
                "  0.0000000E+00 0.0000000E+00-0.9207061E+10-0.4482686E+10 0.0000000E+00 007002\n" +
                "  0.0000000E+00 0.0000000E+00-0.3098019E+14-0.1679017E+14 0.0000000E+00 600300\n" +
                "  0.0000000E+00 0.0000000E+00-0.1402816E+14-0.6611526E+13 0.0000000E+00 510300\n" +
                "  0.0000000E+00 0.0000000E+00 0.5085997E+14 0.4139467E+14 0.0000000E+00 420300\n" +
                "  0.0000000E+00 0.0000000E+00-0.2009739E+14 0.1857679E+14 0.0000000E+00 330300\n" +
                "  0.0000000E+00 0.0000000E+00-0.1085937E+15-0.3724082E+14 0.0000000E+00 240300\n" +
                "  0.0000000E+00 0.0000000E+00-0.5913353E+14-0.2858413E+14 0.0000000E+00 150300\n" +
                "  0.0000000E+00 0.0000000E+00-0.4889883E+13-0.4061621E+13 0.0000000E+00 060300\n" +
                " -0.1215733E+15-0.3693229E+14 0.0000000E+00 0.0000000E+00 0.9127306E+13 501300\n" +
                " -0.3176793E+15-0.1163720E+15 0.0000000E+00 0.0000000E+00 0.1765794E+13 411300\n" +
                " -0.1522275E+15-0.8912782E+14 0.0000000E+00 0.0000000E+00-0.1467864E+14 321300\n" +
                "  0.2698618E+15 0.6458011E+14 0.0000000E+00 0.0000000E+00-0.2776874E+13 231300\n" +
                "  0.2844152E+15 0.9565323E+14 0.0000000E+00 0.0000000E+00 0.6473978E+13 141300\n" +
                "  0.5931411E+14 0.2216130E+14 0.0000000E+00 0.0000000E+00 0.1247916E+13 051300\n" +
                "  0.0000000E+00 0.0000000E+00 0.2273476E+14 0.2172886E+14 0.0000000E+00 402300\n" +
                "  0.0000000E+00 0.0000000E+00-0.1849985E+15-0.6915600E+14 0.0000000E+00 312300\n" +
                "  0.0000000E+00 0.0000000E+00-0.3615513E+15-0.1778041E+15 0.0000000E+00 222300\n" +
                "  0.0000000E+00 0.0000000E+00-0.1302501E+15-0.9172577E+14 0.0000000E+00 132300\n" +
                "  0.0000000E+00 0.0000000E+00 0.7228469E+13-0.1056617E+14 0.0000000E+00 042300\n" +
                " -0.5079030E+14-0.2654239E+14 0.0000000E+00 0.0000000E+00 0.2308985E+13 303300\n" +
                "  0.3088029E+15 0.1072055E+15 0.0000000E+00 0.0000000E+00 0.4009853E+14 213300\n" +
                "  0.1966260E+15 0.7803761E+14 0.0000000E+00 0.0000000E+00 0.2577712E+14 123300\n" +
                " -0.5969739E+14-0.1857269E+14 0.0000000E+00 0.0000000E+00-0.4123366E+13 033300\n" +
                "  0.0000000E+00 0.0000000E+00 0.2010042E+14 0.6805055E+13 0.0000000E+00 204300\n" +
                "  0.0000000E+00 0.0000000E+00 0.2535684E+13-0.1280295E+14 0.0000000E+00 114300\n" +
                "  0.0000000E+00 0.0000000E+00-0.2119941E+14-0.1883729E+14 0.0000000E+00 024300\n" +
                " -0.1340068E+15-0.4559283E+14 0.0000000E+00 0.0000000E+00-0.1489651E+14 105300\n" +
                " -0.1228215E+15-0.4462990E+14 0.0000000E+00 0.0000000E+00-0.1337821E+14 015300\n" +
                "  0.0000000E+00 0.0000000E+00-0.1161689E+14-0.2951692E+13 0.0000000E+00 006300\n" +
                "  0.7856022E+13 0.2641915E+13 0.0000000E+00 0.0000000E+00 0.6357198E+11 600201\n" +
                "  0.3272457E+14 0.1243981E+14 0.0000000E+00 0.0000000E+00 0.4033685E+12 510201\n" +
                "  0.4145232E+14 0.1801922E+14 0.0000000E+00 0.0000000E+00-0.2272900E+12 420201\n" +
                " -0.3944847E+13 0.2015376E+13 0.0000000E+00 0.0000000E+00-0.2780090E+13 330201\n" +
                " -0.4392870E+14-0.1458724E+14 0.0000000E+00 0.0000000E+00-0.3834001E+13 240201\n" +
                " -0.2862533E+14-0.1032767E+14 0.0000000E+00 0.0000000E+00-0.1931315E+13 150201\n" +
                " -0.5232805E+13-0.1946364E+13 0.0000000E+00 0.0000000E+00-0.3115861E+12 060201\n" +
                "  0.0000000E+00 0.0000000E+00-0.2493627E+14-0.1291059E+14 0.0000000E+00 501201\n" +
                "  0.0000000E+00 0.0000000E+00-0.5758130E+14-0.2843463E+14 0.0000000E+00 411201\n" +
                "  0.0000000E+00 0.0000000E+00-0.4148052E+14-0.1807180E+14 0.0000000E+00 321201\n" +
                "  0.0000000E+00 0.0000000E+00-0.2120840E+13 0.1766178E+13 0.0000000E+00 231201\n" +
                "  0.0000000E+00 0.0000000E+00 0.5664725E+13 0.4246069E+13 0.0000000E+00 141201\n" +
                "  0.0000000E+00 0.0000000E+00 0.4447679E+12 0.6238887E+12 0.0000000E+00 051201\n" +
                " -0.8866144E+13-0.1950618E+13 0.0000000E+00 0.0000000E+00 0.4805442E+13 402201\n" +
                " -0.4010026E+14-0.1310175E+14 0.0000000E+00 0.0000000E+00 0.8068825E+13 312201\n" +
                " -0.6864266E+14-0.2605436E+14 0.0000000E+00 0.0000000E+00 0.3380034E+13 222201\n" +
                " -0.3783393E+14-0.1537841E+14 0.0000000E+00 0.0000000E+00-0.2464713E+12 132201\n" +
                " -0.9573305E+12-0.7476778E+12 0.0000000E+00 0.0000000E+00 0.1393533E+12 042201\n" +
                "  0.0000000E+00 0.0000000E+00 0.2512199E+14 0.1279920E+14 0.0000000E+00 303201\n" +
                "  0.0000000E+00 0.0000000E+00 0.2200894E+14 0.1337151E+14 0.0000000E+00 213201\n" +
                "  0.0000000E+00 0.0000000E+00 0.2859417E+12 0.2989250E+13 0.0000000E+00 123201\n" +
                "  0.0000000E+00 0.0000000E+00-0.4216716E+12 0.2383976E+12 0.0000000E+00 033201\n" +
                " -0.3868991E+14-0.1471733E+14 0.0000000E+00 0.0000000E+00-0.4995197E+13 204201\n" +
                " -0.3644801E+13-0.2069689E+13 0.0000000E+00 0.0000000E+00-0.1802598E+13 114201\n" +
                "  0.1917470E+14 0.6939075E+13 0.0000000E+00 0.0000000E+00 0.1312231E+13 024201\n" +
                "  0.0000000E+00 0.0000000E+00-0.9058794E+11 0.5085698E+12 0.0000000E+00 105201\n" +
                "  0.0000000E+00 0.0000000E+00 0.3688503E+13 0.2100997E+13 0.0000000E+00 015201\n" +
                "  0.5162184E+13 0.1966471E+13 0.0000000E+00 0.0000000E+00 0.4446845E+12 006201\n" +
                "  0.0000000E+00 0.0000000E+00-0.4958451E+11-0.1316956E+11 0.0000000E+00 600102\n" +
                "  0.0000000E+00 0.0000000E+00-0.1287458E+12-0.6213094E+11 0.0000000E+00 510102\n" +
                "  0.0000000E+00 0.0000000E+00 0.7234690E+11-0.2230413E+11 0.0000000E+00 420102\n" +
                "  0.0000000E+00 0.0000000E+00 0.4147391E+12 0.1507853E+12 0.0000000E+00 330102\n" +
                "  0.0000000E+00 0.0000000E+00 0.2902301E+12 0.1728399E+12 0.0000000E+00 240102\n" +
                "  0.0000000E+00 0.0000000E+00-0.2254393E+10 0.4619187E+11 0.0000000E+00 150102\n" +
                "  0.0000000E+00 0.0000000E+00-0.2981400E+11-0.3047897E+10 0.0000000E+00 060102\n" +
                "  0.5572033E+12 0.1377138E+12 0.0000000E+00 0.0000000E+00 0.2111563E+11 501102\n" +
                "  0.4180973E+13 0.1549963E+13 0.0000000E+00 0.0000000E+00 0.2074024E+12 411102\n" +
                "  0.8332836E+13 0.3400776E+13 0.0000000E+00 0.0000000E+00 0.4084595E+12 321102\n" +
                "  0.6363910E+13 0.2717978E+13 0.0000000E+00 0.0000000E+00 0.2840225E+12 231102\n" +
                "  0.1725942E+13 0.7748822E+12 0.0000000E+00 0.0000000E+00 0.5291042E+11 141102\n" +
                "  0.4245833E+11 0.2985243E+11 0.0000000E+00 0.0000000E+00-0.7601497E+10 051102\n" +
                "  0.0000000E+00 0.0000000E+00-0.1516205E+12-0.1387872E+12 0.0000000E+00 402102\n" +
                "  0.0000000E+00 0.0000000E+00-0.2421374E+13-0.1266113E+13 0.0000000E+00 312102\n" +
                "  0.0000000E+00 0.0000000E+00-0.4811247E+13-0.2364646E+13 0.0000000E+00 222102\n" +
                "  0.0000000E+00 0.0000000E+00-0.3095471E+13-0.1532878E+13 0.0000000E+00 132102\n" +
                "  0.0000000E+00 0.0000000E+00-0.6133908E+12-0.3229804E+12 0.0000000E+00 042102\n" +
                " -0.6587781E+12-0.2416702E+12 0.0000000E+00 0.0000000E+00 0.1295729E+11 303102\n" +
                "  0.5123888E+12 0.2068067E+12 0.0000000E+00 0.0000000E+00 0.3094301E+12 213102\n" +
                "  0.5757409E+12 0.2125807E+12 0.0000000E+00 0.0000000E+00 0.3307501E+12 123102\n" +
                " -0.1781468E+12-0.7143743E+11 0.0000000E+00 0.0000000E+00 0.7095936E+11 033102\n" +
                "  0.0000000E+00 0.0000000E+00 0.4865615E+12 0.2380570E+12 0.0000000E+00 204102\n" +
                "  0.0000000E+00 0.0000000E+00 0.3334881E+12 0.1839525E+12 0.0000000E+00 114102\n" +
                "  0.0000000E+00 0.0000000E+00-0.6085614E+11 0.9941952E+10 0.0000000E+00 024102\n" +
                " -0.7120245E+12-0.2622773E+12 0.0000000E+00 0.0000000E+00-0.5305039E+11 105102\n" +
                " -0.9035548E+12-0.3440418E+12 0.0000000E+00 0.0000000E+00-0.6805187E+11 015102\n" +
                "  0.0000000E+00 0.0000000E+00-0.1013629E+12-0.4639284E+11 0.0000000E+00 006102\n" +
                " -0.5804966E+09-0.1598627E+09 0.0000000E+00 0.0000000E+00 -6270534.     600003\n" +
                " -0.1437451E+10-0.5185153E+09 0.0000000E+00 0.0000000E+00 0.1515411E+09 510003\n" +
                "  0.4249710E+09 0.8798701E+08 0.0000000E+00 0.0000000E+00 0.6890393E+09 420003\n" +
                "  0.4270579E+10 0.1591583E+10 0.0000000E+00 0.0000000E+00 0.9044601E+09 330003\n" +
                "  0.4779908E+10 0.1723255E+10 0.0000000E+00 0.0000000E+00 0.2693929E+09 240003\n" +
                "  0.2086341E+10 0.6669925E+09 0.0000000E+00 0.0000000E+00-0.1742535E+09 150003\n" +
                "  0.2846640E+09 0.8036988E+08 0.0000000E+00 0.0000000E+00-0.7301526E+08 060003\n" +
                "  0.0000000E+00 0.0000000E+00-0.1034203E+10-0.2678818E+09 0.0000000E+00 501003\n" +
                "  0.0000000E+00 0.0000000E+00-0.3050943E+10-0.2240155E+10 0.0000000E+00 411003\n" +
                "  0.0000000E+00 0.0000000E+00 0.4418285E+10-0.4014097E+09 0.0000000E+00 321003\n" +
                "  0.0000000E+00 0.0000000E+00 0.1447854E+11 0.5419379E+10 0.0000000E+00 231003\n" +
                "  0.0000000E+00 0.0000000E+00 0.8980123E+10 0.4628571E+10 0.0000000E+00 141003\n" +
                "  0.0000000E+00 0.0000000E+00 0.1087369E+10 0.8926359E+09 0.0000000E+00 051003\n" +
                " -0.1723908E+11-0.9798609E+10 0.0000000E+00 0.0000000E+00-0.4611974E+09 402003\n" +
                "  0.1220737E+11-0.1320043E+09 0.0000000E+00 0.0000000E+00 0.1866912E+10 312003\n" +
                "  0.7107330E+11 0.2676086E+11 0.0000000E+00 0.0000000E+00 0.5013126E+10 222003\n" +
                "  0.5084234E+11 0.2170271E+11 0.0000000E+00 0.0000000E+00 0.3178258E+10 132003\n" +
                "  0.9897331E+10 0.4733852E+10 0.0000000E+00 0.0000000E+00 0.5452984E+09 042003\n" +
                "  0.0000000E+00 0.0000000E+00 0.4706144E+11 0.2418024E+11 0.0000000E+00 303003\n" +
                "  0.0000000E+00 0.0000000E+00 0.7107233E+11 0.3720896E+11 0.0000000E+00 213003\n" +
                "  0.0000000E+00 0.0000000E+00 0.3169538E+11 0.1733592E+11 0.0000000E+00 123003\n" +
                "  0.0000000E+00 0.0000000E+00 0.3190790E+10 0.1838834E+10 0.0000000E+00 033003\n" +
                " -0.4062913E+11-0.1557600E+11 0.0000000E+00 0.0000000E+00-0.7674851E+10 204003\n" +
                " -0.3210631E+11-0.1237242E+11 0.0000000E+00 0.0000000E+00-0.7800596E+10 114003\n" +
                " -0.1567110E+10-0.5943114E+09 0.0000000E+00 0.0000000E+00-0.1899001E+10 024003\n" +
                "  0.0000000E+00 0.0000000E+00-0.1313711E+10-0.7611560E+09 0.0000000E+00 105003\n" +
                "  0.0000000E+00 0.0000000E+00 0.1829650E+10 0.5689580E+09 0.0000000E+00 015003\n" +
                "  0.5301793E+10 0.2115135E+10 0.0000000E+00 0.0000000E+00 0.3570195E+09 006003\n" +
                " -0.4977749E+14-0.1288136E+14 0.0000000E+00 0.0000000E+00 0.4144541E+13 500400\n" +
                " -0.1497796E+15-0.5185550E+14 0.0000000E+00 0.0000000E+00 0.1770354E+13 410400\n" +
                " -0.1101372E+15-0.5547451E+14 0.0000000E+00 0.0000000E+00-0.4752875E+13 320400\n" +
                "  0.9894479E+14 0.1889009E+14 0.0000000E+00 0.0000000E+00 0.5416302E+12 230400\n" +
                "  0.1299844E+15 0.4296935E+14 0.0000000E+00 0.0000000E+00 0.3827745E+13 140400\n" +
                "  0.2540685E+14 0.9498190E+13 0.0000000E+00 0.0000000E+00 0.4912536E+12 050400\n" +
                "  0.0000000E+00 0.0000000E+00 0.2623756E+14 0.2678578E+14 0.0000000E+00 401400\n" +
                "  0.0000000E+00 0.0000000E+00-0.2060853E+15-0.7235713E+14 0.0000000E+00 311400\n" +
                "  0.0000000E+00 0.0000000E+00-0.3708633E+15-0.1801552E+15 0.0000000E+00 221400\n" +
                "  0.0000000E+00 0.0000000E+00-0.1106201E+15-0.8553457E+14 0.0000000E+00 131400\n" +
                "  0.0000000E+00 0.0000000E+00 0.1972638E+14-0.6562248E+13 0.0000000E+00 041400\n" +
                " -0.7926726E+14-0.4263615E+14 0.0000000E+00 0.0000000E+00-0.8770509E+13 302400\n" +
                "  0.4356925E+15 0.1495336E+15 0.0000000E+00 0.0000000E+00 0.4200216E+14 212400\n" +
                "  0.2289534E+15 0.9499951E+14 0.0000000E+00 0.0000000E+00 0.2555113E+14 122400\n" +
                " -0.1148235E+15-0.3614524E+14 0.0000000E+00 0.0000000E+00-0.9278736E+13 032400\n" +
                "  0.0000000E+00 0.0000000E+00 0.8677795E+13 0.1010490E+13 0.0000000E+00 203400\n" +
                "  0.0000000E+00 0.0000000E+00 0.2576765E+14-0.1703986E+14 0.0000000E+00 113400\n" +
                "  0.0000000E+00 0.0000000E+00-0.2376612E+14-0.3078951E+14 0.0000000E+00 023400\n" +
                " -0.2728826E+15-0.9136549E+14 0.0000000E+00 0.0000000E+00-0.2923071E+14 104400\n" +
                " -0.3112777E+15-0.1130835E+15 0.0000000E+00 0.0000000E+00-0.3266905E+14 014400\n" +
                "  0.0000000E+00 0.0000000E+00-0.3864998E+14-0.1056953E+14 0.0000000E+00 005400\n" +
                "  0.0000000E+00 0.0000000E+00-0.1013144E+14-0.5457528E+13 0.0000000E+00 500301\n" +
                "  0.0000000E+00 0.0000000E+00-0.2175369E+14-0.1113905E+14 0.0000000E+00 410301\n" +
                "  0.0000000E+00 0.0000000E+00-0.1291985E+14-0.5404862E+13 0.0000000E+00 320301\n" +
                "  0.0000000E+00 0.0000000E+00 0.1332849E+13 0.2195008E+13 0.0000000E+00 230301\n" +
                "  0.0000000E+00 0.0000000E+00 0.1298665E+13 0.1516586E+13 0.0000000E+00 140301\n" +
                "  0.0000000E+00 0.0000000E+00-0.7710211E+12-0.1147847E+12 0.0000000E+00 050301\n" +
                " -0.1353872E+14-0.2651079E+13 0.0000000E+00 0.0000000E+00 0.3528318E+13 401301\n" +
                " -0.6969243E+14-0.2240269E+14 0.0000000E+00 0.0000000E+00 0.5143948E+13 311301\n" +
                " -0.1208141E+15-0.4495025E+14 0.0000000E+00 0.0000000E+00 0.6045725E+12 221301\n" +
                " -0.6351440E+14-0.2524317E+14 0.0000000E+00 0.0000000E+00-0.8487748E+12 131301\n" +
                " -0.1573215E+13-0.1162899E+13 0.0000000E+00 0.0000000E+00 0.4628900E+12 041301\n" +
                "  0.0000000E+00 0.0000000E+00 0.4678824E+14 0.2437386E+14 0.0000000E+00 302301\n" +
                "  0.0000000E+00 0.0000000E+00 0.4204860E+14 0.2595730E+14 0.0000000E+00 212301\n" +
                "  0.0000000E+00 0.0000000E+00-0.3083949E+13 0.4233137E+13 0.0000000E+00 122301\n" +
                "  0.0000000E+00 0.0000000E+00-0.4327462E+13-0.1069370E+13 0.0000000E+00 032301\n" +
                " -0.8462949E+14-0.3252271E+14 0.0000000E+00 0.0000000E+00-0.1065044E+14 203301\n" +
                " -0.3106349E+13-0.3227596E+13 0.0000000E+00 0.0000000E+00-0.3477947E+13 113301\n" +
                "  0.5079678E+14 0.1831560E+14 0.0000000E+00 0.0000000E+00 0.3510564E+13 023301\n" +
                "  0.0000000E+00 0.0000000E+00-0.8769105E+13-0.2094221E+13 0.0000000E+00 104301\n" +
                "  0.0000000E+00 0.0000000E+00 0.6634164E+13 0.4220746E+13 0.0000000E+00 014301\n" +
                "  0.2143453E+14 0.8145151E+13 0.0000000E+00 0.0000000E+00 0.1805596E+13 005301\n" +
                "  0.4104594E+12 0.9517386E+11 0.0000000E+00 0.0000000E+00 0.2108791E+11 500202\n" +
                "  0.3173099E+13 0.1169675E+13 0.0000000E+00 0.0000000E+00 0.1765527E+12 410202\n" +
                "  0.6440367E+13 0.2625358E+13 0.0000000E+00 0.0000000E+00 0.3521268E+12 320202\n" +
                "  0.5063734E+13 0.2153583E+13 0.0000000E+00 0.0000000E+00 0.2663472E+12 230202\n" +
                "  0.1470980E+13 0.6464946E+12 0.0000000E+00 0.0000000E+00 0.6373331E+11 140202\n" +
                "  0.8397191E+11 0.3946148E+11 0.0000000E+00 0.0000000E+00-0.1087386E+10 050202\n" +
                "  0.0000000E+00 0.0000000E+00 0.6885153E+11-0.6218190E+11 0.0000000E+00 401202\n" +
                "  0.0000000E+00 0.0000000E+00-0.3149316E+13-0.1605299E+13 0.0000000E+00 311202\n" +
                "  0.0000000E+00 0.0000000E+00-0.6969479E+13-0.3291130E+13 0.0000000E+00 221202\n" +
                "  0.0000000E+00 0.0000000E+00-0.4436425E+13-0.2137552E+13 0.0000000E+00 131202\n" +
                "  0.0000000E+00 0.0000000E+00-0.7681147E+12-0.4254980E+12 0.0000000E+00 041202\n" +
                " -0.6423753E+12-0.1501414E+12 0.0000000E+00 0.0000000E+00-0.2708016E+11 302202\n" +
                "  0.1718272E+13 0.7745357E+12 0.0000000E+00 0.0000000E+00 0.5157016E+12 212202\n" +
                "  0.1802748E+12 0.8306377E+11 0.0000000E+00 0.0000000E+00 0.5116713E+12 122202\n" +
                " -0.1179590E+13-0.4499184E+12 0.0000000E+00 0.0000000E+00 0.6598991E+11 032202\n" +
                "  0.0000000E+00 0.0000000E+00 0.8165836E+12 0.4516786E+12 0.0000000E+00 203202\n" +
                "  0.0000000E+00 0.0000000E+00 0.8802902E+12 0.5055644E+12 0.0000000E+00 113202\n" +
                "  0.0000000E+00 0.0000000E+00-0.4040961E+11 0.1166906E+12 0.0000000E+00 023202\n" +
                " -0.2709774E+13-0.9879392E+12 0.0000000E+00 0.0000000E+00-0.1645192E+12 104202\n" +
                " -0.4004896E+13-0.1520924E+13 0.0000000E+00 0.0000000E+00-0.2832355E+12 014202\n" +
                "  0.0000000E+00 0.0000000E+00-0.4525947E+12-0.1853868E+12 0.0000000E+00 005202\n" +
                "  0.0000000E+00 0.0000000E+00-0.2291635E+10-0.7014186E+09 0.0000000E+00 500103\n" +
                "  0.0000000E+00 0.0000000E+00-0.5023905E+10-0.3180710E+10 0.0000000E+00 410103\n" +
                "  0.0000000E+00 0.0000000E+00 0.9242364E+10 0.1496561E+10 0.0000000E+00 320103\n" +
                "  0.0000000E+00 0.0000000E+00 0.2588659E+11 0.1051100E+11 0.0000000E+00 230103\n" +
                "  0.0000000E+00 0.0000000E+00 0.1660868E+11 0.7865291E+10 0.0000000E+00 140103\n" +
                "  0.0000000E+00 0.0000000E+00 0.2640023E+10 0.1409558E+10 0.0000000E+00 050103\n" +
                " -0.4056104E+11-0.2570407E+11 0.0000000E+00 0.0000000E+00-0.1441745E+10 401103\n" +
                "  0.5692779E+11 0.5563643E+10 0.0000000E+00 0.0000000E+00 0.4803625E+10 311103\n" +
                "  0.2012492E+12 0.7522915E+11 0.0000000E+00 0.0000000E+00 0.1219586E+11 221103\n" +
                "  0.1219140E+12 0.5469706E+11 0.0000000E+00 0.0000000E+00 0.6572544E+10 131103\n" +
                "  0.2173938E+11 0.1166706E+11 0.0000000E+00 0.0000000E+00 0.9900925E+09 041103\n" +
                "  0.0000000E+00 0.0000000E+00 0.2040540E+12 0.1055388E+12 0.0000000E+00 302103\n" +
                "  0.0000000E+00 0.0000000E+00 0.3010604E+12 0.1606528E+12 0.0000000E+00 212103\n" +
                "  0.0000000E+00 0.0000000E+00 0.1275002E+12 0.7351567E+11 0.0000000E+00 122103\n" +
                "  0.0000000E+00 0.0000000E+00 0.7948769E+10 0.6741211E+10 0.0000000E+00 032103\n" +
                " -0.2028275E+12-0.7557958E+11 0.0000000E+00 0.0000000E+00-0.3992167E+11 203103\n" +
                " -0.1440974E+12-0.5337929E+11 0.0000000E+00 0.0000000E+00-0.3988352E+11 113103\n" +
                "  0.9204329E+10 0.4259425E+10 0.0000000E+00 0.0000000E+00-0.9178662E+10 023103\n" +
                "  0.0000000E+00 0.0000000E+00-0.3287454E+11-0.1556631E+11 0.0000000E+00 104103\n" +
                "  0.0000000E+00 0.0000000E+00-0.3503566E+09-0.3595167E+10 0.0000000E+00 014103\n" +
                "  0.5554536E+11 0.2198654E+11 0.0000000E+00 0.0000000E+00 0.3577748E+10 005103\n" +
                "  0.5134878E+08 0.2130938E+08 0.0000000E+00 0.0000000E+00  3080364.     500004\n" +
                "  0.1775179E+09 0.5521143E+08 0.0000000E+00 0.0000000E+00 -2038344.     410004\n" +
                "  0.2391573E+09 0.4837369E+08 0.0000000E+00 0.0000000E+00-0.3898578E+08 320004\n" +
                "  0.1302670E+09  8671169.     0.0000000E+00 0.0000000E+00-0.5711086E+08 230004\n" +
                "  -7588859.    -0.1279413E+08 0.0000000E+00 0.0000000E+00-0.2253069E+08 140004\n" +
                " -0.2220197E+08 -6680788.     0.0000000E+00 0.0000000E+00 -606532.2     050004\n" +
                "  0.0000000E+00 0.0000000E+00-0.1767155E+09-0.4968680E+08 0.0000000E+00 401004\n" +
                "  0.0000000E+00 0.0000000E+00-0.4774921E+09-0.1923694E+09 0.0000000E+00 311004\n" +
                "  0.0000000E+00 0.0000000E+00-0.3199370E+09-0.1310735E+09 0.0000000E+00 221004\n" +
                "  0.0000000E+00 0.0000000E+00-0.9689910E+08 0.1068780E+08 0.0000000E+00 131004\n" +
                "  0.0000000E+00 0.0000000E+00-0.4833168E+08  1589746.     0.0000000E+00 041004\n" +
                " -0.9756971E+09-0.6119299E+09 0.0000000E+00 0.0000000E+00 0.3862160E+08 302004\n" +
                " -0.2271308E+09-0.5308183E+09 0.0000000E+00 0.0000000E+00 0.1605002E+09 212004\n" +
                "  0.1238337E+10 0.1719747E+09 0.0000000E+00 0.0000000E+00 0.1834614E+09 122004\n" +
                "  0.7259099E+09 0.2022411E+09 0.0000000E+00 0.0000000E+00 0.6585705E+08 032004\n" +
                "  0.0000000E+00 0.0000000E+00 0.4256485E+09 0.3372574E+09 0.0000000E+00 203004\n" +
                "  0.0000000E+00 0.0000000E+00 0.1198315E+10 0.7798357E+09 0.0000000E+00 113004\n" +
                "  0.0000000E+00 0.0000000E+00 0.7324236E+09 0.4423614E+09 0.0000000E+00 023004\n" +
                "  0.3671255E+08 0.2928810E+08 0.0000000E+00 0.0000000E+00 0.1273370E+09 104004\n" +
                " -0.7656285E+09-0.3147606E+09 0.0000000E+00 0.0000000E+00 0.4017955E+08 014004\n" +
                "  0.0000000E+00 0.0000000E+00-0.1686226E+09-0.5022114E+08 0.0000000E+00 005004\n" +
                "  0.0000000E+00 0.0000000E+00 0.3231945E+13 0.8492268E+13 0.0000000E+00 400500\n" +
                "  0.0000000E+00 0.0000000E+00-0.9906027E+14-0.3451689E+14 0.0000000E+00 310500\n" +
                "  0.0000000E+00 0.0000000E+00-0.1587751E+15-0.7583076E+14 0.0000000E+00 220500\n" +
                "  0.0000000E+00 0.0000000E+00-0.4183866E+14-0.3361449E+14 0.0000000E+00 130500\n" +
                "  0.0000000E+00 0.0000000E+00 0.1213886E+14-0.1254351E+13 0.0000000E+00 040500\n" +
                " -0.7429501E+14-0.3966104E+14 0.0000000E+00 0.0000000E+00-0.1211565E+14 301500\n" +
                "  0.3139783E+15 0.1054768E+15 0.0000000E+00 0.0000000E+00 0.2428709E+14 211500\n" +
                "  0.1293809E+15 0.5742731E+14 0.0000000E+00 0.0000000E+00 0.1262922E+14 121500\n" +
                " -0.1095232E+15-0.3477618E+14 0.0000000E+00 0.0000000E+00-0.9093496E+13 031500\n" +
                "  0.0000000E+00 0.0000000E+00-0.3267735E+14-0.1568730E+14 0.0000000E+00 202500\n" +
                "  0.0000000E+00 0.0000000E+00 0.4846046E+14-0.1293781E+14 0.0000000E+00 112500\n" +
                "  0.0000000E+00 0.0000000E+00-0.1027939E+14-0.3029858E+14 0.0000000E+00 022500\n" +
                " -0.3539620E+15-0.1158403E+15 0.0000000E+00 0.0000000E+00-0.3517573E+14 103500\n" +
                " -0.4919464E+15-0.1787344E+15 0.0000000E+00 0.0000000E+00-0.4955294E+14 013500\n" +
                "  0.0000000E+00 0.0000000E+00-0.8440977E+14-0.2424026E+14 0.0000000E+00 004500\n" +
                " -0.6365411E+13-0.1015295E+13 0.0000000E+00 0.0000000E+00 0.7804192E+12 400401\n" +
                " -0.3721232E+14-0.1179109E+14 0.0000000E+00 0.0000000E+00 0.5865453E+12 310401\n" +
                " -0.6670128E+14-0.2449639E+14 0.0000000E+00 0.0000000E+00-0.1028811E+13 220401\n" +
                " -0.3399299E+14-0.1323343E+14 0.0000000E+00 0.0000000E+00-0.5120092E+12 130401\n" +
                " -0.5652678E+11-0.2940394E+12 0.0000000E+00 0.0000000E+00 0.3805616E+12 040401\n" +
                "  0.0000000E+00 0.0000000E+00 0.3890442E+14 0.2098745E+14 0.0000000E+00 301401\n" +
                "  0.0000000E+00 0.0000000E+00 0.3060468E+14 0.2083791E+14 0.0000000E+00 211401\n" +
                "  0.0000000E+00 0.0000000E+00-0.1401710E+14-0.7263400E+12 0.0000000E+00 121401\n" +
                "  0.0000000E+00 0.0000000E+00-0.9614720E+13-0.3343224E+13 0.0000000E+00 031401\n" +
                " -0.1087212E+15-0.4217221E+14 0.0000000E+00 0.0000000E+00-0.1234143E+14 202401\n" +
                "  0.2600287E+13-0.2397828E+13 0.0000000E+00 0.0000000E+00-0.2776844E+13 112401\n" +
                "  0.7744505E+14 0.2777174E+14 0.0000000E+00 0.0000000E+00 0.5731974E+13 022401\n" +
                "  0.0000000E+00 0.0000000E+00-0.2965783E+14-0.9637303E+13 0.0000000E+00 103401\n" +
                "  0.0000000E+00 0.0000000E+00 0.3547912E+13 0.3456301E+13 0.0000000E+00 013401\n" +
                "  0.5216394E+14 0.1981263E+14 0.0000000E+00 0.0000000E+00 0.4250500E+13 004401\n" +
                "  0.0000000E+00 0.0000000E+00 0.1496261E+12 0.3768008E+11 0.0000000E+00 400302\n" +
                "  0.0000000E+00 0.0000000E+00-0.1503797E+13-0.6957707E+12 0.0000000E+00 310302\n" +
                "  0.0000000E+00 0.0000000E+00-0.3529110E+13-0.1561996E+13 0.0000000E+00 220302\n" +
                "  0.0000000E+00 0.0000000E+00-0.2211733E+13-0.1024860E+13 0.0000000E+00 130302\n" +
                "  0.0000000E+00 0.0000000E+00-0.3310260E+12-0.1970170E+12 0.0000000E+00 040302\n" +
                "  0.5311070E+12 0.3706852E+12 0.0000000E+00 0.0000000E+00-0.5694716E+11 301302\n" +
                "  0.2832844E+13 0.1279396E+13 0.0000000E+00 0.0000000E+00 0.3835914E+12 211302\n" +
                " -0.8060882E+12-0.2740904E+12 0.0000000E+00 0.0000000E+00 0.2908586E+12 121302\n" +
                " -0.2060948E+13-0.7762163E+12 0.0000000E+00 0.0000000E+00-0.3745723E+11 031302\n" +
                "  0.0000000E+00 0.0000000E+00 0.3170442E+12 0.3375607E+12 0.0000000E+00 202302\n" +
                "  0.0000000E+00 0.0000000E+00 0.1656967E+13 0.9027527E+12 0.0000000E+00 112302\n" +
                "  0.0000000E+00 0.0000000E+00 0.4933857E+12 0.4385102E+12 0.0000000E+00 022302\n" +
                " -0.6146663E+13-0.2221984E+13 0.0000000E+00 0.0000000E+00-0.3361112E+12 103302\n" +
                " -0.9910284E+13-0.3742064E+13 0.0000000E+00 0.0000000E+00-0.6888644E+12 013302\n" +
                "  0.0000000E+00 0.0000000E+00-0.1064868E+13-0.3515236E+12 0.0000000E+00 004302\n" +
                " -0.2146756E+11-0.1614409E+11 0.0000000E+00 0.0000000E+00-0.4273806E+08 400203\n" +
                "  0.5156356E+11 0.6799072E+10 0.0000000E+00 0.0000000E+00 0.5504445E+10 310203\n" +
                "  0.1298381E+12 0.4825898E+11 0.0000000E+00 0.0000000E+00 0.8170543E+10 220203\n" +
                "  0.6919887E+11 0.3273789E+11 0.0000000E+00 0.0000000E+00 0.3142762E+10 130203\n" +
                "  0.1172914E+11 0.7202827E+10 0.0000000E+00 0.0000000E+00 0.3058944E+09 040203\n" +
                "  0.0000000E+00 0.0000000E+00 0.2975426E+12 0.1548212E+12 0.0000000E+00 301203\n" +
                "  0.0000000E+00 0.0000000E+00 0.4543855E+12 0.2425953E+12 0.0000000E+00 211203\n" +
                "  0.0000000E+00 0.0000000E+00 0.1930463E+12 0.1123162E+12 0.0000000E+00 121203\n" +
                "  0.0000000E+00 0.0000000E+00 0.2132646E+09 0.7560102E+10 0.0000000E+00 031203\n" +
                " -0.3854144E+12-0.1370600E+12 0.0000000E+00 0.0000000E+00-0.7452545E+11 202203\n" +
                " -0.2218544E+12-0.7470100E+11 0.0000000E+00 0.0000000E+00-0.7023976E+11 112203\n" +
                "  0.7221576E+11 0.3092652E+11 0.0000000E+00 0.0000000E+00-0.1358063E+11 022203\n" +
                "  0.0000000E+00 0.0000000E+00-0.1745527E+12-0.7730071E+11 0.0000000E+00 103203\n" +
                "  0.0000000E+00 0.0000000E+00-0.5002367E+11-0.3767629E+11 0.0000000E+00 013203\n" +
                "  0.2463707E+12 0.9635757E+11 0.0000000E+00 0.0000000E+00 0.1550310E+11 004203\n" +
                "  0.0000000E+00 0.0000000E+00-0.1282155E+09-0.3669575E+08 0.0000000E+00 400104\n" +
                "  0.0000000E+00 0.0000000E+00-0.2348339E+09-0.1074303E+09 0.0000000E+00 310104\n" +
                "  0.0000000E+00 0.0000000E+00-0.1553232E+09  8958571.     0.0000000E+00 220104\n" +
                "  0.0000000E+00 0.0000000E+00-0.3180984E+09 0.7016827E+08 0.0000000E+00 130104\n" +
                "  0.0000000E+00 0.0000000E+00-0.1770514E+09 0.1352524E+08 0.0000000E+00 040104\n" +
                " -0.3092543E+10-0.1798372E+10 0.0000000E+00 0.0000000E+00 0.1913966E+09 301104\n" +
                " -0.1484146E+10-0.1672158E+10 0.0000000E+00 0.0000000E+00 0.5668466E+09 211104\n" +
                "  0.4963602E+10 0.1003256E+10 0.0000000E+00 0.0000000E+00 0.7766792E+09 121104\n" +
                "  0.2948541E+10 0.8527894E+09 0.0000000E+00 0.0000000E+00 0.2892041E+09 031104\n" +
                "  0.0000000E+00 0.0000000E+00 0.2308149E+10 0.1629806E+10 0.0000000E+00 202104\n" +
                "  0.0000000E+00 0.0000000E+00 0.6699536E+10 0.3969152E+10 0.0000000E+00 112104\n" +
                "  0.0000000E+00 0.0000000E+00 0.3713393E+10 0.2056847E+10 0.0000000E+00 022104\n" +
                " -0.6871934E+09-0.1467185E+09 0.0000000E+00 0.0000000E+00 0.5472004E+09 103104\n" +
                " -0.5533251E+10-0.2249460E+10 0.0000000E+00 0.0000000E+00 0.1296304E+09 013104\n" +
                "  0.0000000E+00 0.0000000E+00-0.7897334E+09 0.1270845E+08 0.0000000E+00 004104\n" +
                "  -4850315.     -1827006.     0.0000000E+00 0.0000000E+00 -658099.2     400005\n" +
                " -0.1372705E+08 -5091346.     0.0000000E+00 0.0000000E+00 -1265566.     310005\n" +
                " -0.1500442E+08 -4482500.     0.0000000E+00 0.0000000E+00  636114.7     220005\n" +
                "  -8832452.     -1809790.     0.0000000E+00 0.0000000E+00  1679549.     130005\n" +
                "  -2077647.     -423089.4     0.0000000E+00 0.0000000E+00  512142.5     040005\n" +
                "  0.0000000E+00 0.0000000E+00  6522182.      200215.5     0.0000000E+00 301005\n" +
                "  0.0000000E+00 0.0000000E+00 0.1853596E+08  621960.2     0.0000000E+00 211005\n" +
                "  0.0000000E+00 0.0000000E+00 0.2264177E+08  5976925.     0.0000000E+00 121005\n" +
                "  0.0000000E+00 0.0000000E+00 0.1385555E+08  7507822.     0.0000000E+00 031005\n" +
                " -0.1198630E+09-0.4602863E+08 0.0000000E+00 0.0000000E+00 -6416896.     202005\n" +
                " -0.1094482E+09-0.4492013E+08 0.0000000E+00 0.0000000E+00 -4498242.     112005\n" +
                " -0.2619566E+08-0.1158517E+08 0.0000000E+00 0.0000000E+00  293997.8     022005\n" +
                "  0.0000000E+00 0.0000000E+00-0.7043761E+08-0.3928595E+08 0.0000000E+00 103005\n" +
                "  0.0000000E+00 0.0000000E+00-0.3649223E+08-0.2164893E+08 0.0000000E+00 013005\n" +
                "  0.5265708E+08 0.2255338E+08 0.0000000E+00 0.0000000E+00  865675.5     004005\n" +
                " -0.2943206E+14-0.1550167E+14 0.0000000E+00 0.0000000E+00-0.5054561E+13 300600\n" +
                "  0.9397714E+14 0.3040849E+14 0.0000000E+00 0.0000000E+00 0.5833921E+13 210600\n" +
                "  0.2625257E+14 0.1298437E+14 0.0000000E+00 0.0000000E+00 0.2266567E+13 120600\n" +
                " -0.4156963E+14-0.1330412E+14 0.0000000E+00 0.0000000E+00-0.3331264E+13 030600\n" +
                "  0.0000000E+00 0.0000000E+00-0.5528107E+14-0.2301522E+14 0.0000000E+00 201600\n" +
                "  0.0000000E+00 0.0000000E+00 0.3482355E+14-0.6784743E+13 0.0000000E+00 111600\n" +
                "  0.0000000E+00 0.0000000E+00 0.1190993E+13-0.1727402E+14 0.0000000E+00 021600\n" +
                " -0.2829241E+15-0.8927719E+14 0.0000000E+00 0.0000000E+00-0.2475005E+14 102600\n" +
                " -0.4664584E+15-0.1694567E+15 0.0000000E+00 0.0000000E+00-0.4494022E+14 012600\n" +
                "  0.0000000E+00 0.0000000E+00-0.1249341E+15-0.3754204E+14 0.0000000E+00 003600\n" +
                "  0.0000000E+00 0.0000000E+00 0.1096802E+14 0.6468810E+13 0.0000000E+00 300501\n" +
                "  0.0000000E+00 0.0000000E+00 0.5473304E+13 0.5485750E+13 0.0000000E+00 210501\n" +
                "  0.0000000E+00 0.0000000E+00-0.1081056E+14-0.2559743E+13 0.0000000E+00 120501\n" +
                "  0.0000000E+00 0.0000000E+00-0.6265734E+13-0.2336884E+13 0.0000000E+00 030501\n" +
                " -0.7932707E+14-0.3079488E+14 0.0000000E+00 0.0000000E+00-0.7536518E+13 201501\n" +
                "  0.6214526E+13-0.3873907E+12 0.0000000E+00 0.0000000E+00-0.2408833E+12 111501\n" +
                "  0.6405967E+14 0.2285532E+14 0.0000000E+00 0.0000000E+00 0.5168593E+13 021501\n" +
                "  0.0000000E+00 0.0000000E+00-0.4610363E+14-0.1629665E+14 0.0000000E+00 102501\n" +
                "  0.0000000E+00 0.0000000E+00-0.5925248E+13-0.1307168E+13 0.0000000E+00 012501\n" +
                "  0.7707962E+14 0.2929909E+14 0.0000000E+00 0.0000000E+00 0.6024188E+13 003501\n" +
                "  0.6869025E+12 0.3638693E+12 0.0000000E+00 0.0000000E+00-0.9067867E+10 300402\n" +
                "  0.1452002E+13 0.6795953E+12 0.0000000E+00 0.0000000E+00 0.1242654E+12 210402\n" +
                " -0.7945644E+12-0.2771196E+12 0.0000000E+00 0.0000000E+00 0.2680437E+11 120402\n" +
                " -0.1228989E+13-0.4592769E+12 0.0000000E+00 0.0000000E+00-0.6199476E+11 030402\n" +
                "  0.0000000E+00 0.0000000E+00-0.2873501E+12 0.8097661E+11 0.0000000E+00 201402\n" +
                "  0.0000000E+00 0.0000000E+00 0.2143033E+13 0.1028989E+13 0.0000000E+00 111402\n" +
                "  0.0000000E+00 0.0000000E+00 0.1205303E+13 0.6734886E+12 0.0000000E+00 021402\n" +
                " -0.8592013E+13-0.3087518E+13 0.0000000E+00 0.0000000E+00-0.4726433E+12 102402\n" +
                " -0.1432037E+14-0.5357743E+13 0.0000000E+00 0.0000000E+00-0.1006914E+13 012402\n" +
                "  0.0000000E+00 0.0000000E+00-0.1398419E+13-0.2701685E+12 0.0000000E+00 003402\n" +
                "  0.0000000E+00 0.0000000E+00 0.1502703E+12 0.7789185E+11 0.0000000E+00 300303\n" +
                "  0.0000000E+00 0.0000000E+00 0.2353774E+12 0.1242880E+12 0.0000000E+00 210303\n" +
                "  0.0000000E+00 0.0000000E+00 0.9317779E+11 0.5417129E+11 0.0000000E+00 120303\n" +
                "  0.0000000E+00 0.0000000E+00-0.7216055E+10 0.1727996E+10 0.0000000E+00 030303\n" +
                " -0.3425517E+12-0.1136561E+12 0.0000000E+00 0.0000000E+00-0.6201660E+11 201303\n" +
                " -0.1201001E+12-0.2860909E+11 0.0000000E+00 0.0000000E+00-0.5145593E+11 111303\n" +
                "  0.1384658E+12 0.5808962E+11 0.0000000E+00 0.0000000E+00-0.5156296E+10 021303\n" +
                "  0.0000000E+00 0.0000000E+00-0.4076010E+12-0.1709324E+12 0.0000000E+00 102303\n" +
                "  0.0000000E+00 0.0000000E+00-0.1812434E+12-0.1094962E+12 0.0000000E+00 012303\n" +
                "  0.5939493E+12 0.2286060E+12 0.0000000E+00 0.0000000E+00 0.3688297E+11 003303\n" +
                " -0.2374975E+10-0.1327214E+10 0.0000000E+00 0.0000000E+00 0.1161769E+09 300204\n" +
                " -0.1061490E+09-0.8700103E+09 0.0000000E+00 0.0000000E+00 0.4724106E+09 210204\n" +
                "  0.6233598E+10 0.1603794E+10 0.0000000E+00 0.0000000E+00 0.8469806E+09 120204\n" +
                "  0.2541018E+10 0.7467897E+09 0.0000000E+00 0.0000000E+00 0.2814979E+09 030204\n" +
                "  0.0000000E+00 0.0000000E+00 0.2558166E+10 0.2237861E+10 0.0000000E+00 201204\n" +
                "  0.0000000E+00 0.0000000E+00 0.7662865E+10 0.5277664E+10 0.0000000E+00 111204\n" +
                "  0.0000000E+00 0.0000000E+00 0.4397549E+10 0.2484762E+10 0.0000000E+00 021204\n" +
                " -0.4772319E+10-0.1493684E+10 0.0000000E+00 0.0000000E+00 0.5364822E+09 102204\n" +
                " -0.1641277E+11-0.6557633E+10 0.0000000E+00 0.0000000E+00-0.1667440E+09 012204\n" +
                "  0.0000000E+00 0.0000000E+00-0.9704371E+09 0.1196803E+10 0.0000000E+00 003204\n" +
                "  0.0000000E+00 0.0000000E+00  428075.9     -2526467.     0.0000000E+00 300105\n" +
                "  0.0000000E+00 0.0000000E+00 0.1278184E+08  447716.0     0.0000000E+00 210105\n" +
                "  0.0000000E+00 0.0000000E+00 0.4923350E+08 0.2060530E+08 0.0000000E+00 120105\n" +
                "  0.0000000E+00 0.0000000E+00 0.3444486E+08 0.1687017E+08 0.0000000E+00 030105\n" +
                " -0.2609468E+09-0.1130825E+09 0.0000000E+00 0.0000000E+00 -8826170.     201105\n" +
                " -0.2992909E+09-0.1294573E+09 0.0000000E+00 0.0000000E+00-0.1130664E+08 111105\n" +
                " -0.1789690E+09-0.6644012E+08 0.0000000E+00 0.0000000E+00 -6976558.     021105\n" +
                "  0.0000000E+00 0.0000000E+00-0.4009743E+09-0.1946211E+09 0.0000000E+00 102105\n" +
                "  0.0000000E+00 0.0000000E+00-0.2029704E+09-0.9556307E+08 0.0000000E+00 012105\n" +
                "  0.3810418E+09 0.1582971E+09 0.0000000E+00 0.0000000E+00  8588062.     003105\n" +
                "   253952.9      130240.5     0.0000000E+00 0.0000000E+00  102486.7     300006\n" +
                "   477678.5      274438.4     0.0000000E+00 0.0000000E+00  209033.3     210006\n" +
                "   412650.2      211928.2     0.0000000E+00 0.0000000E+00  90485.61     120006\n" +
                "   121578.2      64017.26     0.0000000E+00 0.0000000E+00 -17345.90     030006\n" +
                "  0.0000000E+00 0.0000000E+00 -249935.0     -110636.0     0.0000000E+00 201006\n" +
                "  0.0000000E+00 0.0000000E+00  1378063.      559240.1     0.0000000E+00 111006\n" +
                "  0.0000000E+00 0.0000000E+00  853310.5      318135.6     0.0000000E+00 021006\n" +
                "  -3808867.     -1332571.     0.0000000E+00 0.0000000E+00 -342356.5     102006\n" +
                "  -3493964.     -1305163.     0.0000000E+00 0.0000000E+00 -282372.0     012006\n" +
                "  0.0000000E+00 0.0000000E+00 -427667.9     -52537.18     0.0000000E+00 003006\n" +
                "  0.0000000E+00 0.0000000E+00-0.2766975E+14-0.1077442E+14 0.0000000E+00 200700\n" +
                "  0.0000000E+00 0.0000000E+00 0.7542745E+13-0.2299656E+13 0.0000000E+00 110700\n" +
                "  0.0000000E+00 0.0000000E+00 0.1543376E+13-0.4416012E+13 0.0000000E+00 020700\n" +
                " -0.1255173E+15-0.3701063E+14 0.0000000E+00 0.0000000E+00-0.8658873E+13 101700\n" +
                " -0.2375147E+15-0.8612210E+14 0.0000000E+00 0.0000000E+00-0.2168484E+14 011700\n" +
                "  0.0000000E+00 0.0000000E+00-0.1231321E+15-0.3885840E+14 0.0000000E+00 002700\n" +
                " -0.2638867E+14-0.1005617E+14 0.0000000E+00 0.0000000E+00-0.1954188E+13 200601\n" +
                "  0.2633737E+13 0.2551600E+12 0.0000000E+00 0.0000000E+00 0.5272106E+12 110601\n" +
                "  0.2230649E+14 0.7950236E+13 0.0000000E+00 0.0000000E+00 0.1959128E+13 020601\n" +
                "  0.0000000E+00 0.0000000E+00-0.3627023E+14-0.1336679E+14 0.0000000E+00 101601\n" +
                "  0.0000000E+00 0.0000000E+00-0.1042647E+14-0.4456075E+13 0.0000000E+00 011601\n" +
                "  0.6708042E+14 0.2555216E+14 0.0000000E+00 0.0000000E+00 0.4977556E+13 002601\n" +
                "  0.0000000E+00 0.0000000E+00-0.2314635E+12-0.2510222E+11 0.0000000E+00 200502\n" +
                "  0.0000000E+00 0.0000000E+00 0.9641334E+12 0.4113638E+12 0.0000000E+00 110502\n" +
                "  0.0000000E+00 0.0000000E+00 0.7117590E+12 0.3241861E+12 0.0000000E+00 020502\n" +
                " -0.6841691E+13-0.2454223E+13 0.0000000E+00 0.0000000E+00-0.4030495E+12 101502\n" +
                " -0.1146757E+14-0.4239658E+13 0.0000000E+00 0.0000000E+00-0.8271817E+12 011502\n" +
                "  0.0000000E+00 0.0000000E+00-0.9097250E+12 0.9863274E+11 0.0000000E+00 002502\n" +
                " -0.1057986E+12-0.3251273E+11 0.0000000E+00 0.0000000E+00-0.1907535E+11 200403\n" +
                "  0.1624537E+11 0.1388139E+11 0.0000000E+00 0.0000000E+00-0.1068040E+11 110403\n" +
                "  0.8966992E+11 0.3665168E+11 0.0000000E+00 0.0000000E+00 0.2452652E+10 020403\n" +
                "  0.0000000E+00 0.0000000E+00-0.4544674E+12-0.1812380E+12 0.0000000E+00 101403\n" +
                "  0.0000000E+00 0.0000000E+00-0.2517465E+12-0.1342187E+12 0.0000000E+00 011403\n" +
                "  0.8258731E+12 0.3115114E+12 0.0000000E+00 0.0000000E+00 0.5135044E+11 002403\n" +
                "  0.0000000E+00 0.0000000E+00 0.1822824E+09 0.8803308E+09 0.0000000E+00 200304\n" +
                "  0.0000000E+00 0.0000000E+00 0.2701958E+10 0.2491056E+10 0.0000000E+00 110304\n" +
                "  0.0000000E+00 0.0000000E+00 0.2140648E+10 0.1133650E+10 0.0000000E+00 020304\n" +
                " -0.1079948E+11-0.3656376E+10 0.0000000E+00 0.0000000E+00-0.2108633E+09 101304\n" +
                " -0.2387594E+11-0.9347743E+10 0.0000000E+00 0.0000000E+00-0.8181030E+09 011304\n" +
                "  0.0000000E+00 0.0000000E+00 0.1456881E+10 0.3684234E+10 0.0000000E+00 002304\n" +
                " -0.1901254E+09-0.8646913E+08 0.0000000E+00 0.0000000E+00  2740075.     200205\n" +
                " -0.4846273E+09-0.1811724E+09 0.0000000E+00 0.0000000E+00-0.2384676E+08 110205\n" +
                " -0.2712221E+09-0.9555747E+08 0.0000000E+00 0.0000000E+00-0.1364698E+08 020205\n" +
                "  0.0000000E+00 0.0000000E+00-0.5697417E+09-0.2623692E+09 0.0000000E+00 101205\n" +
                "  0.0000000E+00 0.0000000E+00-0.1805140E+09-0.7248664E+08 0.0000000E+00 011205\n" +
                "  0.1095819E+10 0.4435324E+09 0.0000000E+00 0.0000000E+00 0.3519295E+08 002205\n" +
                "  0.0000000E+00 0.0000000E+00  1873225.      631259.4     0.0000000E+00 200106\n" +
                "  0.0000000E+00 0.0000000E+00  4186083.      1669811.     0.0000000E+00 110106\n" +
                "  0.0000000E+00 0.0000000E+00  124717.5      198642.2     0.0000000E+00 020106\n" +
                " -0.2052634E+08 -6969254.     0.0000000E+00 0.0000000E+00 -1570126.     101106\n" +
                "  -4666108.     -2255855.     0.0000000E+00 0.0000000E+00 -104698.7     011106\n" +
                "  0.0000000E+00 0.0000000E+00 -415959.1     -297425.0     0.0000000E+00 002106\n" +
                "   15253.83     -1107.916     0.0000000E+00 0.0000000E+00 -7225.336     200007\n" +
                "   35254.26     -383.9428     0.0000000E+00 0.0000000E+00 -13473.28     110007\n" +
                "   19186.13     -1646.384     0.0000000E+00 0.0000000E+00 -4151.452     020007\n" +
                "  0.0000000E+00 0.0000000E+00 -94036.47     -47771.18     0.0000000E+00 101007\n" +
                "  0.0000000E+00 0.0000000E+00 -123526.9     -57158.80     0.0000000E+00 011007\n" +
                "   109943.0      46131.96     0.0000000E+00 0.0000000E+00  5782.487     002007\n" +
                " -0.2316747E+14-0.5862153E+13 0.0000000E+00 0.0000000E+00-0.8664542E+12 100800\n" +
                " -0.4775599E+14-0.1716625E+14 0.0000000E+00 0.0000000E+00-0.4021755E+13 010800\n" +
                "  0.0000000E+00 0.0000000E+00-0.7379218E+14-0.2452752E+14 0.0000000E+00 001800\n" +
                "  0.0000000E+00 0.0000000E+00-0.1207502E+14-0.4601758E+13 0.0000000E+00 100701\n" +
                "  0.0000000E+00 0.0000000E+00-0.5198346E+13-0.2489208E+13 0.0000000E+00 010701\n" +
                "  0.3036823E+14 0.1161805E+14 0.0000000E+00 0.0000000E+00 0.2095234E+13 001701\n" +
                " -0.2387794E+13-0.8625081E+12 0.0000000E+00 0.0000000E+00-0.1509878E+12 100602\n" +
                " -0.4018844E+13-0.1468160E+13 0.0000000E+00 0.0000000E+00-0.2971174E+12 010602\n" +
                "  0.0000000E+00 0.0000000E+00-0.3448132E+11 0.3326771E+12 0.0000000E+00 001602\n" +
                "  0.0000000E+00 0.0000000E+00-0.1806194E+12-0.6839596E+11 0.0000000E+00 100503\n" +
                "  0.0000000E+00 0.0000000E+00-0.1064965E+12-0.5270523E+11 0.0000000E+00 010503\n" +
                "  0.6394944E+12 0.2353527E+12 0.0000000E+00 0.0000000E+00 0.4107174E+11 001503\n" +
                " -0.8549326E+10-0.2951946E+10 0.0000000E+00 0.0000000E+00-0.4583164E+09 100404\n" +
                " -0.1337843E+11-0.5172309E+10 0.0000000E+00 0.0000000E+00-0.6335763E+09 010404\n" +
                "  0.0000000E+00 0.0000000E+00 0.4307298E+10 0.4303244E+10 0.0000000E+00 001404\n" +
                "  0.0000000E+00 0.0000000E+00-0.2523220E+09-0.1063669E+09 0.0000000E+00 100305\n" +
                "  0.0000000E+00 0.0000000E+00-0.7202390E+08-0.1181818E+08 0.0000000E+00 010305\n" +
                "  0.1506910E+10 0.5976200E+09 0.0000000E+00 0.0000000E+00 0.6469239E+08 001305\n" +
                " -0.1040865E+08 -4009239.     0.0000000E+00 0.0000000E+00 -417425.3     100206\n" +
                "  0.1048577E+08  2758920.     0.0000000E+00 0.0000000E+00  1137996.     010206\n" +
                "  0.0000000E+00 0.0000000E+00 -6240148.     -2604639.     0.0000000E+00 001206\n" +
                "  0.0000000E+00 0.0000000E+00 -276036.2     -112143.6     0.0000000E+00 100107\n" +
                "  0.0000000E+00 0.0000000E+00 -141414.2     -48720.50     0.0000000E+00 010107\n" +
                "  -70824.44      27141.76     0.0000000E+00 0.0000000E+00 -34033.65     001107\n" +
                "  -1058.797      17.33670     0.0000000E+00 0.0000000E+00  632.8699     100008\n" +
                "  -1511.838      232.2458     0.0000000E+00 0.0000000E+00  630.8640     010008\n" +
                "  0.0000000E+00 0.0000000E+00  5523.404      3132.004     0.0000000E+00 001008\n" +
                "  0.0000000E+00 0.0000000E+00-0.2042534E+14-0.7143399E+13 0.0000000E+00 000900\n" +
                "  0.4872189E+13 0.1883813E+13 0.0000000E+00 0.0000000E+00 0.2892475E+12 000801\n" +
                "  0.0000000E+00 0.0000000E+00 0.2436058E+12 0.1958134E+12 0.0000000E+00 000702\n" +
                "  0.2237313E+12 0.8014164E+11 0.0000000E+00 0.0000000E+00 0.1547986E+11 000603\n" +
                "  0.0000000E+00 0.0000000E+00 0.1673276E+10 0.1430376E+10 0.0000000E+00 000504\n" +
                "  0.7754740E+09 0.3072222E+09 0.0000000E+00 0.0000000E+00 0.4028157E+08 000405\n" +
                "  0.0000000E+00 0.0000000E+00 -6749087.     -2319088.     0.0000000E+00 000306\n" +
                "  -1093853.     -302368.5     0.0000000E+00 0.0000000E+00 -112957.4     000207\n" +
                "  0.0000000E+00 0.0000000E+00  3178.384      1626.510     0.0000000E+00 000108\n" +
                "   73.26814     -7.923222     0.0000000E+00 0.0000000E+00 -19.08913     000009";

        return CosyArbitraryOrder.readMap(string);
    }


    private void test() {
        String data = data();
        List<PhaseSpaceParticle> analyse = analyse(data);
        analyse.forEach(System.out::println);

        List<Point2> list = PhaseSpaceParticles.projectionToPlane(true, analyse);

        Plot2d.plot2(list);

        Plot2d.showThread();
    }

    private List<PhaseSpaceParticle> analyse(String data) {
        String[] lines = data.split("\n");
        System.out.println(lines.length);
        return Arrays.stream(lines).map(line -> {
            String[] split = line.trim().split(" +");

            return PhaseSpaceParticle.create(
                    Double.parseDouble(split[0]),
                    Double.parseDouble(split[1]),
                    Double.parseDouble(split[2]),
                    Double.parseDouble(split[3]),
                    0,
                    Double.parseDouble(split[5])
            );
        }).collect(Collectors.toList());
    }

    private String data() {
        return "  0.0000000E+00  0.0000000E+00  0.0000000E+00  0.0000000E+00  0.0000000E+00  0.0000000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                "  0.0000000E+00  0.0000000E+00  0.0000000E+00  0.0000000E+00  0.0000000E+00  0.0000000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                "  0.1710391E-02  0.4171064E-02  0.0000000E+00  0.0000000E+00  0.2976091E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                "  0.1232483E-02  0.5048299E-02  0.0000000E+00  0.0000000E+00  0.2976062E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                "  0.7384722E-03  0.5768569E-02  0.0000000E+00  0.0000000E+00  0.2976021E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                "  0.2255063E-03  0.6320789E-02  0.0000000E+00  0.0000000E+00  0.2975968E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                " -0.3029634E-03  0.6670664E-02  0.0000000E+00  0.0000000E+00  0.2975901E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                " -0.8450466E-03  0.6763739E-02  0.0000000E+00  0.0000000E+00  0.2975817E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                " -0.1392943E-02  0.6499213E-02  0.0000000E+00  0.0000000E+00  0.2975713E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                " -0.1922575E-02  0.5681238E-02  0.0000000E+00  0.0000000E+00  0.2975580E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                " -0.2338342E-02  0.4014409E-02  0.0000000E+00  0.0000000E+00  0.2975419E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                " -0.2464693E-02  0.1777163E-02  0.0000000E+00  0.0000000E+00  0.2975273E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                " -0.2345358E-02 -0.3554577E-03  0.0000000E+00  0.0000000E+00  0.2975173E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                " -0.2096240E-02 -0.2235444E-02  0.0000000E+00  0.0000000E+00  0.2975109E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                " -0.1773097E-02 -0.3897047E-02  0.0000000E+00  0.0000000E+00  0.2975068E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                " -0.1401978E-02 -0.5372404E-02  0.0000000E+00  0.0000000E+00  0.2975047E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                " -0.9956131E-03 -0.6686159E-02  0.0000000E+00  0.0000000E+00  0.2975040E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                " -0.5632696E-03 -0.7846589E-02  0.0000000E+00  0.0000000E+00  0.2975046E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                " -0.1056870E-03 -0.8868847E-02  0.0000000E+00  0.0000000E+00  0.2975064E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                "  0.3709435E-03 -0.9743486E-02  0.0000000E+00  0.0000000E+00  0.2975093E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                "  0.8649542E-03 -0.1046376E-01  0.0000000E+00  0.0000000E+00  0.2975133E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                "  0.1377920E-02 -0.1101598E-01  0.0000000E+00  0.0000000E+00  0.2975187E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                "  0.1906390E-02 -0.1136585E-01  0.0000000E+00  0.0000000E+00  0.2975254E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                "  0.2448473E-02 -0.1145893E-01  0.0000000E+00  0.0000000E+00  0.2975337E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                "  0.2996369E-02 -0.1119440E-01  0.0000000E+00  0.0000000E+00  0.2975442E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                "  0.3526001E-02 -0.1037642E-01  0.0000000E+00  0.0000000E+00  0.2975574E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                "  0.3941768E-02 -0.8709596E-02  0.0000000E+00  0.0000000E+00  0.2975736E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                "  0.4068120E-02 -0.6472350E-02  0.0000000E+00  0.0000000E+00  0.2975882E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                "  0.3948785E-02 -0.4339729E-02  0.0000000E+00  0.0000000E+00  0.2975982E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                "  0.3699667E-02 -0.2459743E-02  0.0000000E+00  0.0000000E+00  0.2976046E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                "  0.3376523E-02 -0.7981403E-03  0.0000000E+00  0.0000000E+00  0.2976086E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                "  0.3005404E-02  0.6772174E-03  0.0000000E+00  0.0000000E+00  0.2976108E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                "  0.2599039E-02  0.1990972E-02  0.0000000E+00  0.0000000E+00  0.2976115E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                "  0.2166696E-02  0.3151402E-02  0.0000000E+00  0.0000000E+00  0.2976109E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                "  0.8017132E-03 -0.2347593E-02  0.2808430E-02  0.2724823E-02  0.2975577E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                "  0.8017132E-03 -0.2347593E-02  0.2505215E-02  0.3823744E-02  0.2975577E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                "  0.8017132E-03 -0.2347593E-02  0.2140934E-02  0.4850003E-02  0.2975577E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                "  0.8017132E-03 -0.2347593E-02  0.1709324E-02  0.5800563E-02  0.2975577E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                "  0.8017132E-03 -0.2347593E-02  0.1204955E-02  0.6651044E-02  0.2975577E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                "  0.8017132E-03 -0.2347593E-02  0.6151239E-03  0.7367119E-02  0.2975577E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                "  0.8017132E-03 -0.2347593E-02 -0.7879674E-04  0.7879993E-02  0.2975577E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                "  0.8017132E-03 -0.2347593E-02 -0.9028755E-03  0.8045118E-02  0.2975577E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                "  0.8017132E-03 -0.2347593E-02 -0.1829153E-02  0.7572145E-02  0.2975577E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                "  0.8017132E-03 -0.2347593E-02 -0.2582487E-02  0.6410644E-02  0.2975577E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                "  0.8017132E-03 -0.2347593E-02 -0.3028470E-02  0.5010930E-02  0.2975577E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                "  0.8017132E-03 -0.2347593E-02 -0.3258288E-02  0.3601449E-02  0.2975577E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                "  0.8017132E-03 -0.2347593E-02 -0.3344557E-02  0.2230081E-02  0.2975577E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                "  0.8017132E-03 -0.2347593E-02 -0.3325604E-02  0.9097068E-03  0.2975577E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                "  0.8017132E-03 -0.2347593E-02 -0.3222592E-02 -0.3586335E-03  0.2975577E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                "  0.8017132E-03 -0.2347593E-02 -0.3048296E-02 -0.1568503E-02  0.2975577E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                "  0.8017132E-03 -0.2347593E-02 -0.2807687E-02 -0.2727909E-02  0.2975577E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                "  0.8017132E-03 -0.2347593E-02 -0.2505215E-02 -0.3823744E-02  0.2975577E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                "  0.8017132E-03 -0.2347593E-02 -0.2140934E-02 -0.4850003E-02  0.2975577E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                "  0.8017132E-03 -0.2347593E-02 -0.1709324E-02 -0.5800563E-02  0.2975577E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                "  0.8017132E-03 -0.2347593E-02 -0.1204955E-02 -0.6651044E-02  0.2975577E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                "  0.8017132E-03 -0.2347593E-02 -0.6151239E-03 -0.7367119E-02  0.2975577E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                "  0.8017132E-03 -0.2347593E-02  0.7879674E-04 -0.7879993E-02  0.2975577E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                "  0.8017132E-03 -0.2347593E-02  0.9028755E-03 -0.8045118E-02  0.2975577E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                "  0.8017132E-03 -0.2347593E-02  0.1829153E-02 -0.7572145E-02  0.2975577E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                "  0.8017132E-03 -0.2347593E-02  0.2582487E-02 -0.6410644E-02  0.2975577E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                "  0.8017132E-03 -0.2347593E-02  0.3028470E-02 -0.5010930E-02  0.2975577E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                "  0.8017132E-03 -0.2347593E-02  0.3258288E-02 -0.3601449E-02  0.2975577E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                "  0.8017132E-03 -0.2347593E-02  0.3344557E-02 -0.2230081E-02  0.2975577E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                "  0.8017132E-03 -0.2347593E-02  0.3325604E-02 -0.9097068E-03  0.2975577E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                "  0.8017132E-03 -0.2347593E-02  0.3222592E-02  0.3586335E-03  0.2975577E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0\n" +
                "  0.8017132E-03 -0.2347593E-02  0.3048296E-02  0.1568503E-02  0.2975577E+00  0.1432000E+00  0.0000000E+00  0.0000000E+00         1         0";
    }

    private static final double MM = 0.001;


    private static BaseUtils.Switcher<String> switcher = BaseUtils.Switcher.creat(
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
}
