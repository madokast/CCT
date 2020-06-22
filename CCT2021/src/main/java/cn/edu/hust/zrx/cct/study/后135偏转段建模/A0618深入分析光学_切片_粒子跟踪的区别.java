package cn.edu.hust.zrx.cct.study.后135偏转段建模;

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
import cn.edu.hust.zrx.cct.base.line.*;
import cn.edu.hust.zrx.cct.base.opticsMagnet.qs.QsHardPlaneMagnet;
import cn.edu.hust.zrx.cct.base.particle.*;
import cn.edu.hust.zrx.cct.base.point.Point2;
import cn.edu.hust.zrx.cct.base.point.Point3;
import cn.edu.hust.zrx.cct.base.python.Plot2d;
import cn.edu.hust.zrx.cct.base.python.Plot3d;
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
 * A0618深入分析光学_切片_粒子跟踪的区别
 * <p>
 * Data
 * 13:02
 *
 * @author zrx
 * @version 1.0
 */

@SuppressWarnings("all")
public class A0618深入分析光学_切片_粒子跟踪的区别 {

    @run(-1) // pass
    public void 回顾_画一下单粒子跟踪() {
        Trajectory trajectoryPart2 = getTrajectoryPart2();

        MagnetAble elementsOfAllPart2 = getElementsOfAllPart2();

        List<Point2> x = trackingIdealParticle(trajectoryPart2, trajectoryPart2.getLength(), elementsOfAllPart2, true);

        Plot2d.plot2(x);

        Plot2d.showThread();
    }

    @run(0)
    public void 第一个CCT前后2点4米漂移段() {
        Trajectory trajectoryPart2 = getTrajectoryPart2();
        Cct cct345_1 = getCct345_1();

        List<Point2> x_cct345only = trackingIdealParticle(trajectoryPart2, DL2 + CCT345_LENGTH + GAP3 * 2 + QS3_LEN, cct345_1, true);

        MagnetAble elementsOfAllPart2 = getElementsOfAllPart2();

        List<Point2> x = trackingIdealParticle(trajectoryPart2, trajectoryPart2.getLength(), elementsOfAllPart2, true);

        Plot2d.plot2(x);


        Plot2d.plot2(mm2mmm(x_cct345only), Plot2d.RED_LINE);
        Plot2d.plot2(mm2mmm(x), Plot2d.BLUE_LINE);

        BendingPart2.drawElement(13, 13);

        Plot2d.info("s/m", "x/mm", "", 18);
        Plot2d.legend(18, "67.5-CCT-1", "67.5-CCT-1 + QS3 + 67.5-CCT-2");

        Plot2d.showThread();
    }

    @run(0)
    public void 第一个CCT前后2点4米漂移段_切片() {
        Trajectory trajectoryPart1 = getTrajectoryPart1();

        Trajectory trajectoryPart2 = TrajectoryFactory.setStartingPoint(trajectoryPart1.pointAtEnd())
                .setStraightLine(DL2, trajectoryPart1.directAtEnd())
                .addArcLine(trajectoryBigRPart2, true, dipoleCct345Angle)
                .addStraitLine(DL2);

        Cct cct345_1 = getCct345_1();

        List<String> slice = cct345_1.sliceToCosyScript(Bp, 60 * MM, trajectoryPart2, 20 * MM, MM, 0.1);

        slice.forEach(System.out::println);
    }

    @run(1)
    public void 第一个CCT前后2点4米漂移段_多粒子跟踪() {
        Trajectory trajectoryPart1 = getTrajectoryPart1();

        Trajectory trajectoryPart2 = TrajectoryFactory.setStartingPoint(trajectoryPart1.pointAtEnd())
                .setStraightLine(DL2, trajectoryPart1.directAtEnd())
                .addArcLine(trajectoryBigRPart2, true, dipoleCct345Angle)
                .addStraitLine(DL2);

        Cct cct345_1 = getCct345_1();

        List<List<Point2>> xs = track多粒子(64, 0.08, trajectoryPart2, DL2 + CCT345_LENGTH + DL2, cct345_1, true);

        xs.forEach(x -> {
            Plot2d.plot2(mm2mmm(x), Plot2d.BLUE_LINE);
        });


        BendingPart2.drawElement(60, 60);

        Plot2d.info("s/m", "x/mm", "", 18);

        Plot2d.showThread();
    }

    @run(2)
    public void 第一个CCT前后2点4米漂移段_相椭圆() {
        Trajectory trajectoryPart1 = getTrajectoryPart1();

        Trajectory trajectoryPart2 = TrajectoryFactory.setStartingPoint(trajectoryPart1.pointAtEnd())
                .setStraightLine(DL2, trajectoryPart1.directAtEnd())
                .addArcLine(trajectoryBigRPart2, true, dipoleCct345Angle)
                .addStraitLine(DL2);

        Cct cct345_1 = getCct345_1();

        phase相椭圆画图(
                trajectoryPart2.getLength(), true, 0.08, 16, false, 1, cct345_1, trajectoryPart2,
                128, 5,
                List.of(
                        BaseUtils.Content.BiContent.create("OPTICS", CosyMap.optics_后偏转段仅CCT1和前后2点4米漂移段.map),
                        BaseUtils.Content.BiContent.create("SLICE", CosyMap.slice_后偏转段仅CCT1和前后2点4米漂移段.map)
                )
        );
    }

    @run(4)
    public void 后偏转段CCT2入口_切片() {
        Trajectory trajectoryPart1 = getTrajectoryPart1();

        Trajectory trajectoryPart2 = TrajectoryFactory.setStartingPoint(trajectoryPart1.pointAtEnd())
                .setStraightLine(DL2, trajectoryPart1.directAtEnd())
                .addArcLine(trajectoryBigRPart2, true, dipoleCct345Angle)
                .addStraitLine(GAP3 * 2 + QS3_LEN);

        Cct cct345_1 = getCct345_1();
        QsHardPlaneMagnet qs3 = getQs3();

        Elements elements = Elements.empty();

        elements.addElement(cct345_1);
        elements.addElement(qs3);

        List<String> slice = elements.sliceToCosyScript(Bp, 60 * MM, trajectoryPart2, 20 * MM, MM, 0.1);

        slice.forEach(System.out::println);
    }

    @run(5)
    public void 后偏转段CCT2入口_多粒子跟踪() {
        Trajectory trajectoryPart1 = getTrajectoryPart1();

        Trajectory trajectoryPart2 = TrajectoryFactory.setStartingPoint(trajectoryPart1.pointAtEnd())
                .setStraightLine(DL2, trajectoryPart1.directAtEnd())
                .addArcLine(trajectoryBigRPart2, true, dipoleCct345Angle)
                .addStraitLine(GAP3 * 2 + QS3_LEN);

        Cct cct345_1 = getCct345_1();
        QsHardPlaneMagnet qs3 = getQs3();

        Elements elements = Elements.empty();

        elements.addElement(cct345_1);
        elements.addElement(qs3);

        List<List<Point2>> xs = track多粒子(64, 0.08, trajectoryPart2, trajectoryPart2.getLength(), elements, true);

        xs.forEach(x -> {
            Plot2d.plot2(mm2mmm(x), Plot2d.BLUE_LINE);
        });


        BendingPart2.drawElement(60, 60);

        Plot2d.info("s/m", "x/mm", "", 18);

        Plot2d.showThread();
    }

    // ----------------- 不能用参考轨迹切片了

    @run(10) // PASS
    public void FDiscretePointLine3Test() {
        Trajectory trajectoryPart2 = getTrajectoryPart2();


        MagnetAble elementsOfAllPart2 = getElementsOfAllPart2();

        List<Point3> point3List = trackingIdealParticle(trajectoryPart2, trajectoryPart2.getLength(), elementsOfAllPart2);

        DiscretePointLine3 discretePointLine3 = DiscretePointLine3.create(point3List);

        Plot3d.plot3(point3List, Plot2d.BLACK_LINE);

        double length = discretePointLine3.getLength();
        List<Point3> dis = BaseUtils.Python.linspaceStream(0, length, 1000)
                .mapToObj(discretePointLine3::pointAt).collect(Collectors.toList());

        Plot3d.plot3(dis, Plot2d.RED_LINE);

        Plot3d.showThread();
    }

    @run(11) // PASS 2020年6月18日
    public void FDiscretePointLine3Test2左右手() {
        Trajectory trajectoryPart2 = getTrajectoryPart2();


        MagnetAble elementsOfAllPart2 = getElementsOfAllPart2();

        List<Point3> point3List = trackingIdealParticle(trajectoryPart2, trajectoryPart2.getLength(), elementsOfAllPart2);

        Line3 discretePointLine3 = DiscretePointLine3.create(point3List);

        Plot3d.plot3(point3List, Plot2d.BLACK_LINE);

        double length = discretePointLine3.getLength();

        discretePointLine3.plot3(10 * MM, Plot2d.RED_LINE);

        discretePointLine3.rightHandSideLine3(0.1, Vector3.getZDirect()).plot3(10 * MM, Plot2d.BLUE_LINE);
        discretePointLine3.rightHandSideLine3(-0.1, Vector3.getZDirect()).plot3(10 * MM, Plot2d.YELLOW_LINE);

        Plot3d.showThread();
    }

    @run(12)
    public void 三维切片() {

        Trajectory trajectoryPart2 = getTrajectoryPart2();

        MagnetAble elementsOfAllPart2 = getElementsOfAllPart2();

        List<Point3> point3List = trackingIdealParticle(trajectoryPart2, trajectoryPart2.getLength(), elementsOfAllPart2);

        Line3 trajectory3 = DiscretePointLine3.create(point3List);

        List<String> script = elementsOfAllPart2.sliceToCosyScript(Bp, 60 * MM, trajectory3, 20 * MM, MM, 0.1);

        script.forEach(System.out::println);

    }

    @run(13)
    public void 相椭圆() {

        Trajectory trajectoryPart2 = getTrajectoryPart2();

        MagnetAble elementsOfAllPart2 = getElementsOfAllPart2();

        phase相椭圆画图(
                trajectoryPart2.getLength(), false, 0.05, 32, true,
                1, elementsOfAllPart2, trajectoryPart2, 512, 5,
                List.of(BaseUtils.Content.BiContent.create("3d slice", CosyMap.slice3d_后偏转段全部.map))
        );


//        List<Point2> cosy相椭圆 = cosy相椭圆(true, 0,
//                128, 5, false, 1, CosyMap.slice3d_后偏转段全部.map);
//
//        Plot2d.plot2(cosy相椭圆);
//
//        Plot2d.showThread();
    }

    @run(14)
    public void 完整后段_多粒子跟踪() {
        Trajectory trajectoryPart2 = getTrajectoryPart2();

        MagnetAble elementsOfAllPart2 = getElementsOfAllPart2();

        List<List<Point2>> xs = track多粒子(64, 0.08, trajectoryPart2, trajectoryPart2.getLength(), elementsOfAllPart2, false);

        xs.forEach(x -> {
            Plot2d.plot2(mm2mmm(x), Plot2d.BLUE_LINE);
        });

        BendingPart2.drawElement(60, 60);

        Plot2d.info("s/m", "x/mm", "", 18);
        //Antegrade and retrograde

        Plot2d.legend(18, "Antegrade", "Retrograde");

        Plot2d.showThread();
    }


    // ---------------  一些老师的疑惑
    @run(100)
    public void 后段的两个CCT是不是完全相同的_镜像的() {
        Line2 trajectoryPart2 = getTrajectoryPart2();
        Line2 reverse = trajectoryPart2.reverse();

        MagnetAble elementsOfAllPart2 = getElementsOfAllPart2();

        if (false) {
            List<Point2> bz = elementsOfAllPart2.magnetBzAlongTrajectory(trajectoryPart2);
            List<Point2> bz_r = elementsOfAllPart2.magnetBzAlongTrajectory(reverse);

            Plot2d.plot2(bz, Plot2d.BLACK_LINE);
            Plot2d.plot2(bz_r, Plot2d.RED_DASH);

            Plot2d.info("s/m", "B0/T", "", 18);
        }

        if (false) {
            List<Point2> bz = elementsOfAllPart2.magnetGradientAlongTrajectoryFast(trajectoryPart2, 10 * MM, 20 * MM);
            List<Point2> bz_r = elementsOfAllPart2.magnetGradientAlongTrajectoryFast(reverse, 10 * MM, 20 * MM);

            Plot2d.plot2(bz, Plot2d.BLACK_LINE);
            Plot2d.plot2(Point2.convert(bz_r, 1, -1), Plot2d.RED_DASH);

            Plot2d.info("s/m", "B1/(T/m)", "", 18);
        }

        if (true) {
            List<Point2> bz = elementsOfAllPart2.magnetSecondGradientAlongTrajectory(trajectoryPart2, 10 * MM, 20 * MM);
            List<Point2> bz_r = elementsOfAllPart2.magnetSecondGradientAlongTrajectory(reverse, 10 * MM, 20 * MM);

            Plot2d.plot2(bz, Plot2d.BLACK_LINE);
            Plot2d.plot2(bz_r, Plot2d.RED_DASH);

            Plot2d.info("s/m", "B1/(T/m2)", "", 18);
        }

        Plot2d.legend(18, "trajectory", "trajectory_reverse");

        Plot2d.showThread();
    }

    @run(101)
    @Deprecated
    public void 后段的两个CCT是不是完全相同的_粒子跟踪() {
        Line2 trajectoryPart2 = getTrajectoryPart2();
        Line2 reverse = trajectoryPart2.reverse();

        MagnetAble elementsOfAllPart2 = getElementsOfAllPart2();

        List<Point2> x = trackingIdealParticle(trajectoryPart2, trajectoryPart2.getLength(), elementsOfAllPart2, true);
        List<Point2> x_r = trackingIdealParticle(reverse, reverse.getLength(), elementsOfAllPart2, true);

        Plot2d.plot2(x, Plot2d.BLACK_LINE);
        Plot2d.plot2(x_r, Plot2d.RED_DASH);

        BendingPart2.drawElement();

        Plot2d.info("s/m", "x/mm", "", 18);

        //Antegrade and retrograde
        Plot2d.legend(18, "Antegrade", "Retrograde");

        Plot2d.showThread();
    }

    @run(102)
    public void reverseBug() {
        Line2 trajectoryPart2 = getTrajectoryPart2();
        Line2 reverse = trajectoryPart2.reverse();

        trajectoryPart2.plot3d(Plot2d.BLUE_LINE);
        reverse.plot3d(Plot2d.RED_DASH);

        Plot3d.showThread();
    }

    @run(103)
    public void 后段的两个CCT是不是完全相同的_粒子跟踪3d() {
        Line2 trajectoryPart2 = getTrajectoryPart2();
        Line2 reverse = trajectoryPart2.reverse();

        MagnetAble elementsOfAllPart2 = getElementsOfAllPart2();

        List<Point3> x = trackingIdealParticle(trajectoryPart2, trajectoryPart2.getLength(), elementsOfAllPart2);
        List<Point3> x_r = trackingIdealParticle(reverse, reverse.getLength(), elementsOfAllPart2);

        Plot3d.plot3(x, Plot2d.BLUE_LINE);
        Plot3d.plot3(x_r, Plot2d.RED_DASH);

        Plot3d.showThread();

    }


    @run(104) // 失败了
    public void 后段的两个CCT是不是完全相同的_粒子跟踪2() {
        Trajectory trajectoryPart2 = getTrajectoryPart2();

        Trajectory reverse = TrajectoryFactory
                .setStartingPoint(trajectoryPart2.pointAtEnd())
                .setStraightLine(DL2, trajectoryPart2.directAtEnd().copy().reverseSelf())
                .addArcLine(trajectoryBigRPart2, false, dipoleCct345Angle)
                .addStraitLine(GAP3 + QS3_LEN + GAP3)
                .addArcLine(trajectoryBigRPart2, false, dipoleCct345Angle)
                .addStraitLine(DL2);


        MagnetAble elementsOfAllPart2 = getElementsOfAllPart2();

        List<Point2> x = trackingIdealParticle(trajectoryPart2, trajectoryPart2.getLength(), elementsOfAllPart2, true);
        List<Point2> x_r = trackingIdealParticle(reverse, reverse.getLength(), elementsOfAllPart2, true);

        Plot2d.plot2(x, Plot2d.BLACK_LINE);
        Plot2d.plot2(x_r, Plot2d.RED_DASH);

        // BendingPart2.drawElement();

        Plot2d.info("s/m", "x/mm", "", 18);

        //Antegrade and retrograde
        Plot2d.legend(18, "675-CCT-1", "675-CCT-2");

        Plot2d.showThread();
    }


    // ------------- 继续像椭圆对比

    @run(1000)
    public void CCT675_2出口相椭圆() {
        MagnetAble elementsOfAllPart2 = getElementsOfAllPart2();
        Trajectory trajectoryPart2 = getTrajectoryPart2();

//        phase相椭圆画图(
//                trajectoryPart2.getLength() - DL2, true, 0.05,
//                32, true, 1, elementsOfAllPart2, trajectoryPart2,
//                512, 5, List.of(
//                        BaseUtils.Content.BiContent.create("optics", CosyMap.optics_后偏转段CCT2出口.map)
//                )
//        );

//        phase相椭圆画图(
//                trajectoryPart2.getLength() - DL2 + 0.5, true, 0.05,
//                32, true, 1, elementsOfAllPart2, trajectoryPart2,
//                512, 5, List.of(
//                        BaseUtils.Content.BiContent.create("optics", CosyMap.optics_后偏转段CCT2出口加0点5米.map)
//                )
//        );

        phase相椭圆画图(
                trajectoryPart2.getLength() - DL2 + 1.0, true, 0.05,
                32, true, 1, elementsOfAllPart2, trajectoryPart2,
                512, 5, List.of(
                        BaseUtils.Content.BiContent.create("optics", CosyMap.optics_后偏转段CCT2出口加1点0米.map)
                )
        );
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

    public static enum CosyMap {
        slice_后偏转段仅CCT1和前后2点4米漂移段, optics_后偏转段仅CCT1和前后2点4米漂移段,
        slice_后偏转段CCT2入口, optics_后偏转段CCT2入口, slice3d_后偏转段全部,
        optics_后偏转段CCT2出口, optics_后偏转段CCT2出口加0点5米,
        optics_后偏转段CCT2出口加1点0米, optics_后偏转段CCT2出口加1点5米,
        optics_后偏转段CCT2出口加2点0米, optics_后偏转段CCT2出口加2点4米;

        static {
            optics_后偏转段CCT2出口加1点0米.map = CosyArbitraryOrder.readMap(
                    " -0.4366255E-01 0.6426617     0.0000000E+00 0.0000000E+00 0.9122470E-02 100000\n" +
                            "  -1.614190     0.8560639     0.0000000E+00 0.0000000E+00-0.2043148E-02 010000\n" +
                            "  0.0000000E+00 0.0000000E+00 -1.183366     0.1209793     0.0000000E+00 001000\n" +
                            "  0.0000000E+00 0.0000000E+00  1.652574     -1.013995     0.0000000E+00 000100\n" +
                            "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00  1.000000     000010\n" +
                            "  0.1481461E-01-0.9122470E-02 0.0000000E+00 0.0000000E+00  1.002149     000001\n" +
                            "  -4.325328    -0.9384986     0.0000000E+00 0.0000000E+00-0.1921631E-01 200000\n" +
                            "  -16.26625     0.4203887     0.0000000E+00 0.0000000E+00 0.3966893     110000\n" +
                            "  -10.92105      9.054789     0.0000000E+00 0.0000000E+00 0.2540368     020000\n" +
                            "  0.0000000E+00 0.0000000E+00 -16.68197     0.7224107E-01 0.0000000E+00 101000\n" +
                            "  0.0000000E+00 0.0000000E+00 -48.45616    -0.1617974E-01 0.0000000E+00 011000\n" +
                            "  -1.431463     -1.062605     0.0000000E+00 0.0000000E+00-0.7900675     002000\n" +
                            "  0.0000000E+00 0.0000000E+00 -27.07315      16.96123     0.0000000E+00 100100\n" +
                            "  0.0000000E+00 0.0000000E+00 -67.00582      48.39362     0.0000000E+00 010100\n" +
                            "   24.96551      17.21543     0.0000000E+00 0.0000000E+00  1.018843     001100\n" +
                            "  0.8670498E-01 0.2878378     0.0000000E+00 0.0000000E+00-0.1047260E-01 100001\n" +
                            "  0.8506362    -0.1791349     0.0000000E+00 0.0000000E+00-0.4790911E-02 010001\n" +
                            "  0.0000000E+00 0.0000000E+00  1.881750     -1.478990     0.0000000E+00 001001\n" +
                            " -0.2035817     -3.618511     0.0000000E+00 0.0000000E+00 -5.441341     000200\n" +
                            "  0.0000000E+00 0.0000000E+00  11.86105    -0.7596009     0.0000000E+00 000101\n" +
                            " -0.1231632E-01 0.3466170E-02 0.0000000E+00 0.0000000E+00-0.8568591     000002\n" +
                            "   5.965366      7.152255     0.0000000E+00 0.0000000E+00  1.298997     300000\n" +
                            "   21.19151      49.65737     0.0000000E+00 0.0000000E+00  6.611411     210000\n" +
                            "  -8.951051      119.5888     0.0000000E+00 0.0000000E+00  10.02916     120000\n" +
                            "  -58.95301      104.1631     0.0000000E+00 0.0000000E+00  4.799126     030000\n" +
                            "  0.0000000E+00 0.0000000E+00 -56.95471     -49.36323     0.0000000E+00 201000\n" +
                            "  0.0000000E+00 0.0000000E+00 -373.4230     -283.7281     0.0000000E+00 111000\n" +
                            "  0.0000000E+00 0.0000000E+00 -601.1749     -408.8499     0.0000000E+00 021000\n" +
                            "  -87.29637     -61.78512     0.0000000E+00 0.0000000E+00 -12.99334     102000\n" +
                            "  -253.3770     -179.9721     0.0000000E+00 0.0000000E+00 -38.24571     012000\n" +
                            "  0.0000000E+00 0.0000000E+00 -4.455566      7.991402     0.0000000E+00 003000\n" +
                            "  0.0000000E+00 0.0000000E+00 -142.9082     -105.1017     0.0000000E+00 200100\n" +
                            "  0.0000000E+00 0.0000000E+00 -923.8224     -562.3619     0.0000000E+00 110100\n" +
                            "  0.0000000E+00 0.0000000E+00 -1470.194     -746.1316     0.0000000E+00 020100\n" +
                            "  -156.9358     -81.13163     0.0000000E+00 0.0000000E+00 -15.50286     101100\n" +
                            "  -369.8520     -177.8512     0.0000000E+00 0.0000000E+00 -24.56926     011100\n" +
                            "  0.0000000E+00 0.0000000E+00 -97.24072     -8.899062     0.0000000E+00 002100\n" +
                            "   1.715152    -0.3124385     0.0000000E+00 0.0000000E+00 -1.267018     200001\n" +
                            "   15.31044     -1.071326     0.0000000E+00 0.0000000E+00 -7.493292     110001\n" +
                            "   17.59209     -9.398257     0.0000000E+00 0.0000000E+00 -10.72637     020001\n" +
                            "  0.0000000E+00 0.0000000E+00 -3.325637      1.466792     0.0000000E+00 101001\n" +
                            "  0.0000000E+00 0.0000000E+00  22.71245      3.999936     0.0000000E+00 011001\n" +
                            "   19.71799      13.34955     0.0000000E+00 0.0000000E+00  3.702937     002001\n" +
                            "   517.0823      394.1117     0.0000000E+00 0.0000000E+00  94.15673     100200\n" +
                            "   1338.560      1028.775     0.0000000E+00 0.0000000E+00  253.9791     010200\n" +
                            "  0.0000000E+00 0.0000000E+00 -141.3467      118.5896     0.0000000E+00 001200\n" +
                            "  0.0000000E+00 0.0000000E+00  7.600387      3.143039     0.0000000E+00 100101\n" +
                            "  0.0000000E+00 0.0000000E+00  76.63985     -8.407664     0.0000000E+00 010101\n" +
                            "   10.25575      10.96243     0.0000000E+00 0.0000000E+00  1.624003     001101\n" +
                            "  -1.985868     -1.257306     0.0000000E+00 0.0000000E+00 0.7150037E-01 100002\n" +
                            "  -5.977681     -3.396756     0.0000000E+00 0.0000000E+00 0.5167209E-01 010002\n" +
                            "  0.0000000E+00 0.0000000E+00  2.935395      2.479428     0.0000000E+00 001002\n" +
                            "  0.0000000E+00 0.0000000E+00 -254.6329     -37.71834     0.0000000E+00 000300\n" +
                            "  -143.6469     -107.5484     0.0000000E+00 0.0000000E+00 -12.86475     000201\n" +
                            "  0.0000000E+00 0.0000000E+00 -3.043057      7.949556     0.0000000E+00 000102\n" +
                            "  0.6008021E-01 0.7664841E-03 0.0000000E+00 0.0000000E+00 0.7151104     000003\n" +
                            "  -28.40218     -5.141470     0.0000000E+00 0.0000000E+00 0.7144161     400000\n" +
                            "  -255.5621      12.33315     0.0000000E+00 0.0000000E+00  30.78748     310000\n" +
                            "  -869.3362      315.1830     0.0000000E+00 0.0000000E+00  194.9546     220000\n" +
                            "  -1370.909      1009.924     0.0000000E+00 0.0000000E+00  433.8466     130000\n" +
                            "  -882.2036      949.0121     0.0000000E+00 0.0000000E+00  319.9093     040000\n" +
                            "  0.0000000E+00 0.0000000E+00 -36.58756     -89.00101     0.0000000E+00 301000\n" +
                            "  0.0000000E+00 0.0000000E+00 -479.0598     -941.0548     0.0000000E+00 211000\n" +
                            "  0.0000000E+00 0.0000000E+00 -1866.257     -3206.110     0.0000000E+00 121000\n" +
                            "  0.0000000E+00 0.0000000E+00 -2290.937     -3550.254     0.0000000E+00 031000\n" +
                            "  -286.8350     -269.1779     0.0000000E+00 0.0000000E+00 -57.48899     202000\n" +
                            "  -1967.996     -1779.901     0.0000000E+00 0.0000000E+00 -404.0809     112000\n" +
                            "  -3280.935     -2883.760     0.0000000E+00 0.0000000E+00 -684.4774     022000\n" +
                            "  0.0000000E+00 0.0000000E+00  122.9440      152.4810     0.0000000E+00 103000\n" +
                            "  0.0000000E+00 0.0000000E+00  336.0567      436.9227     0.0000000E+00 013000\n" +
                            "  -72.35800     -51.60708     0.0000000E+00 0.0000000E+00 -13.34666     004000\n" +
                            "  0.0000000E+00 0.0000000E+00 -241.0574     -275.0373     0.0000000E+00 300100\n" +
                            "  0.0000000E+00 0.0000000E+00 -2402.036     -2711.685     0.0000000E+00 210100\n" +
                            "  0.0000000E+00 0.0000000E+00 -7968.109     -8793.095     0.0000000E+00 120100\n" +
                            "  0.0000000E+00 0.0000000E+00 -8808.420     -9381.519     0.0000000E+00 030100\n" +
                            "  -3126.114     -2290.160     0.0000000E+00 0.0000000E+00 -451.8854     201100\n" +
                            "  -17985.92     -13144.12     0.0000000E+00 0.0000000E+00 -2719.168     111100\n" +
                            "  -25841.49     -18843.72     0.0000000E+00 0.0000000E+00 -4041.573     021100\n" +
                            "  0.0000000E+00 0.0000000E+00 -380.0340     -446.2677     0.0000000E+00 102100\n" +
                            "  0.0000000E+00 0.0000000E+00 -1277.594     -1402.188     0.0000000E+00 012100\n" +
                            "  -271.0854     -167.2647     0.0000000E+00 0.0000000E+00 -20.34418     003100\n" +
                            "   19.94208      13.64138     0.0000000E+00 0.0000000E+00 -3.674959     300001\n" +
                            "   185.4482      94.47620     0.0000000E+00 0.0000000E+00 -35.31729     210001\n" +
                            "   633.0472      226.4977     0.0000000E+00 0.0000000E+00 -105.0450     120001\n" +
                            "   729.9076      175.5080     0.0000000E+00 0.0000000E+00 -101.7518     030001\n" +
                            "  0.0000000E+00 0.0000000E+00  19.58854      11.62474     0.0000000E+00 201001\n" +
                            "  0.0000000E+00 0.0000000E+00  300.0847      209.7534     0.0000000E+00 111001\n" +
                            "  0.0000000E+00 0.0000000E+00  774.0545      514.6235     0.0000000E+00 021001\n" +
                            "   193.5941      128.1223     0.0000000E+00 0.0000000E+00  40.26475     102001\n" +
                            "   745.1306      502.9141     0.0000000E+00 0.0000000E+00  151.3363     012001\n" +
                            "  0.0000000E+00 0.0000000E+00 -49.88251     -53.50127     0.0000000E+00 003001\n" +
                            "  -6083.566     -4024.428     0.0000000E+00 0.0000000E+00 -934.1678     200200\n" +
                            "  -30071.49     -19786.01     0.0000000E+00 0.0000000E+00 -4536.669     110200\n" +
                            "  -36592.88     -23913.22     0.0000000E+00 0.0000000E+00 -5430.201     020200\n" +
                            "  0.0000000E+00 0.0000000E+00 -1330.819     -1687.808     0.0000000E+00 101200\n" +
                            "  0.0000000E+00 0.0000000E+00 -3765.396     -4497.967     0.0000000E+00 011200\n" +
                            "  -741.2000     -458.4714     0.0000000E+00 0.0000000E+00 -222.9487     002200\n" +
                            "  0.0000000E+00 0.0000000E+00  118.6039      81.57111     0.0000000E+00 200101\n" +
                            "  0.0000000E+00 0.0000000E+00  1173.077      714.0580     0.0000000E+00 110101\n" +
                            "  0.0000000E+00 0.0000000E+00  2547.757      1318.139     0.0000000E+00 020101\n" +
                            "   1705.065      1171.063     0.0000000E+00 0.0000000E+00  262.8597     101101\n" +
                            "   4859.579      3338.741     0.0000000E+00 0.0000000E+00  743.7254     011101\n" +
                            "  0.0000000E+00 0.0000000E+00  64.10762     -19.24840     0.0000000E+00 002101\n" +
                            "   2.439067      1.827457     0.0000000E+00 0.0000000E+00  2.182150     200002\n" +
                            "   3.137608      8.110417     0.0000000E+00 0.0000000E+00  15.48839     110002\n" +
                            "  -3.743688      15.35879     0.0000000E+00 0.0000000E+00  26.99140     020002\n" +
                            "  0.0000000E+00 0.0000000E+00  6.265524     -3.347223     0.0000000E+00 101002\n" +
                            "  0.0000000E+00 0.0000000E+00 0.5363667     -6.024555     0.0000000E+00 011002\n" +
                            "  -38.23042     -21.70399     0.0000000E+00 0.0000000E+00 -9.281246     002002\n" +
                            "  0.0000000E+00 0.0000000E+00  2339.016      2541.704     0.0000000E+00 100300\n" +
                            "  0.0000000E+00 0.0000000E+00  5977.180      6631.594     0.0000000E+00 010300\n" +
                            "   2275.433      1782.245     0.0000000E+00 0.0000000E+00  433.2914     001300\n" +
                            "   2421.738      1769.948     0.0000000E+00 0.0000000E+00  253.9056     100201\n" +
                            "   5506.090      3980.690     0.0000000E+00 0.0000000E+00  464.5065     010201\n" +
                            "  0.0000000E+00 0.0000000E+00  241.1340     -2.946112     0.0000000E+00 001201\n" +
                            "  0.0000000E+00 0.0000000E+00  12.04599     -13.07779     0.0000000E+00 100102\n" +
                            "  0.0000000E+00 0.0000000E+00 -36.96620     -25.64447     0.0000000E+00 010102\n" +
                            "  -227.2511     -156.6984     0.0000000E+00 0.0000000E+00 -32.19010     001102\n" +
                            "   2.559411     0.9202861     0.0000000E+00 0.0000000E+00-0.1678239     100003\n" +
                            "   9.866239      4.287810     0.0000000E+00 0.0000000E+00-0.1511920     010003\n" +
                            "  0.0000000E+00 0.0000000E+00 -4.998642     -1.434638     0.0000000E+00 001003\n" +
                            "   2168.014      1678.504     0.0000000E+00 0.0000000E+00  270.9564     000400\n" +
                            "  0.0000000E+00 0.0000000E+00 -308.3548     -919.2864     0.0000000E+00 000301\n" +
                            "  -86.36469     -95.62678     0.0000000E+00 0.0000000E+00 -4.530139     000202\n" +
                            "  0.0000000E+00 0.0000000E+00 -9.378148     -10.65260     0.0000000E+00 000103\n" +
                            " -0.1172400     0.9097688E-03 0.0000000E+00 0.0000000E+00-0.5997865     000004\n" +
                            "   3.715208      43.32215     0.0000000E+00 0.0000000E+00  32.15915     500000\n" +
                            "  -298.4618      519.1212     0.0000000E+00 0.0000000E+00  405.7042     410000\n" +
                            "  -3325.640      2656.822     0.0000000E+00 0.0000000E+00  2161.028     320000\n" +
                            "  -13139.64      7451.866     0.0000000E+00 0.0000000E+00  6082.417     230000\n" +
                            "  -22764.72      11491.58     0.0000000E+00 0.0000000E+00  8953.866     140000\n" +
                            "  -14715.27      7639.247     0.0000000E+00 0.0000000E+00  5420.533     050000\n" +
                            "  0.0000000E+00 0.0000000E+00  387.6032      52.33914     0.0000000E+00 401000\n" +
                            "  0.0000000E+00 0.0000000E+00  3975.131      50.49063     0.0000000E+00 311000\n" +
                            "  0.0000000E+00 0.0000000E+00  15161.19     -2208.704     0.0000000E+00 221000\n" +
                            "  0.0000000E+00 0.0000000E+00  25308.47     -9080.527     0.0000000E+00 131000\n" +
                            "  0.0000000E+00 0.0000000E+00  15357.08     -10234.67     0.0000000E+00 041000\n" +
                            "   2072.926      1075.420     0.0000000E+00 0.0000000E+00  84.29603     302000\n" +
                            "   16083.59      7793.277     0.0000000E+00 0.0000000E+00  535.3264     212000\n" +
                            "   40916.49      18081.94     0.0000000E+00 0.0000000E+00  862.3832     122000\n" +
                            "   33880.56      13064.86     0.0000000E+00 0.0000000E+00  40.44279     032000\n" +
                            "  0.0000000E+00 0.0000000E+00  1678.495      2066.764     0.0000000E+00 203000\n" +
                            "  0.0000000E+00 0.0000000E+00  9934.830      12288.65     0.0000000E+00 113000\n" +
                            "  0.0000000E+00 0.0000000E+00  14660.65      18199.19     0.0000000E+00 023000\n" +
                            "  -1082.956     -880.5636     0.0000000E+00 0.0000000E+00 -274.6110     104000\n" +
                            "  -3272.898     -2644.804     0.0000000E+00 0.0000000E+00 -836.4372     014000\n" +
                            "  0.0000000E+00 0.0000000E+00  141.2458      166.7964     0.0000000E+00 005000\n" +
                            "  0.0000000E+00 0.0000000E+00  670.8039     -45.27870     0.0000000E+00 400100\n" +
                            "  0.0000000E+00 0.0000000E+00  5912.251     -1949.253     0.0000000E+00 310100\n" +
                            "  0.0000000E+00 0.0000000E+00  17927.82     -14704.98     0.0000000E+00 220100\n" +
                            "  0.0000000E+00 0.0000000E+00  19866.48     -40753.78     0.0000000E+00 130100\n" +
                            "  0.0000000E+00 0.0000000E+00  3462.654     -38800.23     0.0000000E+00 040100\n" +
                            "   8865.033      4119.940     0.0000000E+00 0.0000000E+00  987.6831     301100\n" +
                            "   53897.95      19786.86     0.0000000E+00 0.0000000E+00  5042.735     211100\n" +
                            "   91691.17      13194.94     0.0000000E+00 0.0000000E+00  4434.864     121100\n" +
                            "   28680.30     -27872.55     0.0000000E+00 0.0000000E+00 -5496.967     031100\n" +
                            "  0.0000000E+00 0.0000000E+00  5768.430      7478.198     0.0000000E+00 202100\n" +
                            "  0.0000000E+00 0.0000000E+00  30797.54      39894.06     0.0000000E+00 112100\n" +
                            "  0.0000000E+00 0.0000000E+00  40930.99      52928.16     0.0000000E+00 022100\n" +
                            "  -4813.972     -3516.721     0.0000000E+00 0.0000000E+00 -150.8600     103100\n" +
                            "  -13233.99     -9798.964     0.0000000E+00 0.0000000E+00 -405.2937     013100\n" +
                            "  0.0000000E+00 0.0000000E+00  208.0908      341.7039     0.0000000E+00 004100\n" +
                            "   5.523148    -0.4275915     0.0000000E+00 0.0000000E+00 -13.17362     400001\n" +
                            "   286.4075      48.05047     0.0000000E+00 0.0000000E+00 -197.6992     310001\n" +
                            "   2020.821      189.5257     0.0000000E+00 0.0000000E+00 -1074.360     220001\n" +
                            "   5306.976      18.17159     0.0000000E+00 0.0000000E+00 -2446.932     130001\n" +
                            "   4996.986     -342.9678     0.0000000E+00 0.0000000E+00 -1974.564     040001\n" +
                            "  0.0000000E+00 0.0000000E+00 -45.19091      28.97999     0.0000000E+00 301001\n" +
                            "  0.0000000E+00 0.0000000E+00  54.22150      889.0741     0.0000000E+00 211001\n" +
                            "  0.0000000E+00 0.0000000E+00  1932.035      4919.130     0.0000000E+00 121001\n" +
                            "  0.0000000E+00 0.0000000E+00  4067.017      7465.805     0.0000000E+00 031001\n" +
                            "  -1182.853     -728.0282     0.0000000E+00 0.0000000E+00 -81.19601     202001\n" +
                            "  -4565.301     -2479.668     0.0000000E+00 0.0000000E+00 -45.39304     112001\n" +
                            "  -2861.565     -783.0062     0.0000000E+00 0.0000000E+00  661.3928     022001\n" +
                            "  0.0000000E+00 0.0000000E+00 -1018.438     -1055.094     0.0000000E+00 103001\n" +
                            "  0.0000000E+00 0.0000000E+00 -3126.664     -3339.315     0.0000000E+00 013001\n" +
                            "   339.6458      235.7323     0.0000000E+00 0.0000000E+00  92.50960     004001\n" +
                            "   10599.56      4643.499     0.0000000E+00 0.0000000E+00  2384.416     300200\n" +
                            "   39730.95      6428.225     0.0000000E+00 0.0000000E+00  9988.905     210200\n" +
                            "  -19230.23     -67968.09     0.0000000E+00 0.0000000E+00  2463.790     120200\n" +
                            "  -131606.0     -137965.0     0.0000000E+00 0.0000000E+00 -18956.22     030200\n" +
                            "  0.0000000E+00 0.0000000E+00 -12820.92     -13579.21     0.0000000E+00 201200\n" +
                            "  0.0000000E+00 0.0000000E+00 -76914.60     -82969.16     0.0000000E+00 111200\n" +
                            "  0.0000000E+00 0.0000000E+00 -114897.2     -125773.7     0.0000000E+00 021200\n" +
                            "  -31066.34     -21976.50     0.0000000E+00 0.0000000E+00 -3176.509     102200\n" +
                            "  -88465.62     -62674.85     0.0000000E+00 0.0000000E+00 -10295.11     012200\n" +
                            "  0.0000000E+00 0.0000000E+00  174.9679      407.8332     0.0000000E+00 003200\n" +
                            "  0.0000000E+00 0.0000000E+00 -9.154957      203.9511     0.0000000E+00 300101\n" +
                            "  0.0000000E+00 0.0000000E+00  1770.199      3663.035     0.0000000E+00 210101\n" +
                            "  0.0000000E+00 0.0000000E+00  11576.40      17046.17     0.0000000E+00 120101\n" +
                            "  0.0000000E+00 0.0000000E+00  18688.25      23560.16     0.0000000E+00 030101\n" +
                            "  -3724.722     -2448.169     0.0000000E+00 0.0000000E+00 -60.43166     201101\n" +
                            "  -2548.308     -644.1579     0.0000000E+00 0.0000000E+00  2989.032     111101\n" +
                            "   22957.83      17895.63     0.0000000E+00 0.0000000E+00  9022.434     021101\n" +
                            "  0.0000000E+00 0.0000000E+00 -2674.489     -3541.731     0.0000000E+00 102101\n" +
                            "  0.0000000E+00 0.0000000E+00 -6070.969     -8729.534     0.0000000E+00 012101\n" +
                            "   1843.647      1203.742     0.0000000E+00 0.0000000E+00  101.9995     003101\n" +
                            "  -20.66016     -8.445786     0.0000000E+00 0.0000000E+00  2.249580     300002\n" +
                            "  -282.1661     -107.8606     0.0000000E+00 0.0000000E+00  51.66259     210002\n" +
                            "  -1206.866     -392.8671     0.0000000E+00 0.0000000E+00  241.2930     120002\n" +
                            "  -1614.168     -429.1882     0.0000000E+00 0.0000000E+00  316.7161     030002\n" +
                            "  0.0000000E+00 0.0000000E+00 -27.56662     -13.69080     0.0000000E+00 201002\n" +
                            "  0.0000000E+00 0.0000000E+00 -326.7235     -214.1031     0.0000000E+00 111002\n" +
                            "  0.0000000E+00 0.0000000E+00 -934.8267     -628.5141     0.0000000E+00 021002\n" +
                            "   110.9191      103.1023     0.0000000E+00 0.0000000E+00 -29.83300     102002\n" +
                            "  -297.1345     -105.9714     0.0000000E+00 0.0000000E+00 -214.4026     012002\n" +
                            "  0.0000000E+00 0.0000000E+00  196.1605      165.9557     0.0000000E+00 003002\n" +
                            "  0.0000000E+00 0.0000000E+00 -38356.22     -43847.02     0.0000000E+00 200300\n" +
                            "  0.0000000E+00 0.0000000E+00 -198874.0     -226721.3     0.0000000E+00 110300\n" +
                            "  0.0000000E+00 0.0000000E+00 -256583.6     -291245.8     0.0000000E+00 020300\n" +
                            "  -97421.57     -68308.70     0.0000000E+00 0.0000000E+00 -20369.20     101300\n" +
                            "  -256202.4     -179932.9     0.0000000E+00 0.0000000E+00 -53273.83     011300\n" +
                            "  0.0000000E+00 0.0000000E+00 -4641.411     -4093.061     0.0000000E+00 002300\n" +
                            "   375.7247     -442.6974     0.0000000E+00 0.0000000E+00  668.5674     200201\n" +
                            "   28420.57      16617.59     0.0000000E+00 0.0000000E+00  8429.812     110201\n" +
                            "   70735.22      45869.57     0.0000000E+00 0.0000000E+00  17132.35     020201\n" +
                            "  0.0000000E+00 0.0000000E+00  8322.839      8165.100     0.0000000E+00 101201\n" +
                            "  0.0000000E+00 0.0000000E+00  26105.74      25736.71     0.0000000E+00 011201\n" +
                            "   9507.021      6208.636     0.0000000E+00 0.0000000E+00  1389.537     002201\n" +
                            "  0.0000000E+00 0.0000000E+00 -133.5232     -86.98937     0.0000000E+00 200102\n" +
                            "  0.0000000E+00 0.0000000E+00 -1365.009     -873.1619     0.0000000E+00 110102\n" +
                            "  0.0000000E+00 0.0000000E+00 -3365.500     -1902.270     0.0000000E+00 020102\n" +
                            "  -1001.531     -344.5330     0.0000000E+00 0.0000000E+00 -362.0341     101102\n" +
                            "  -5979.006     -3242.395     0.0000000E+00 0.0000000E+00 -1579.390     011102\n" +
                            "  0.0000000E+00 0.0000000E+00  472.7187      568.3512     0.0000000E+00 002102\n" +
                            "  -6.320008     -2.527247     0.0000000E+00 0.0000000E+00 -2.480301     200003\n" +
                            "  -29.66157     -14.60486     0.0000000E+00 0.0000000E+00 -21.43098     110003\n" +
                            "  -26.35406     -23.18837     0.0000000E+00 0.0000000E+00 -45.11260     020003\n" +
                            "  0.0000000E+00 0.0000000E+00  1.676058      7.283361     0.0000000E+00 101003\n" +
                            "  0.0000000E+00 0.0000000E+00  1.108442      13.66047     0.0000000E+00 011003\n" +
                            "   22.66840      5.291407     0.0000000E+00 0.0000000E+00  14.64388     002003\n" +
                            "  -29575.23     -20003.99     0.0000000E+00 0.0000000E+00 -619.5744     100400\n" +
                            "  -75301.13     -50668.70     0.0000000E+00 0.0000000E+00 -584.6861     010400\n" +
                            "  0.0000000E+00 0.0000000E+00  7870.415      10087.77     0.0000000E+00 001400\n" +
                            "  0.0000000E+00 0.0000000E+00  14271.86      19493.43     0.0000000E+00 100301\n" +
                            "  0.0000000E+00 0.0000000E+00  35635.85      48306.51     0.0000000E+00 010301\n" +
                            "   18544.48      13731.82     0.0000000E+00 0.0000000E+00  3514.916     001301\n" +
                            "  -4084.976     -2191.460     0.0000000E+00 0.0000000E+00 -716.4927     100202\n" +
                            "  -13841.24     -8147.091     0.0000000E+00 0.0000000E+00 -2231.799     010202\n" +
                            "  0.0000000E+00 0.0000000E+00 -892.7197     -785.3652     0.0000000E+00 001202\n" +
                            "  0.0000000E+00 0.0000000E+00-0.3353055      31.79242     0.0000000E+00 100103\n" +
                            "  0.0000000E+00 0.0000000E+00  41.03217      80.68702     0.0000000E+00 010103\n" +
                            "   363.2540      187.8650     0.0000000E+00 0.0000000E+00  77.37997     001103\n" +
                            "  -2.586784    -0.5832457     0.0000000E+00 0.0000000E+00 0.2825842     100004\n" +
                            "  -11.60879     -4.009692     0.0000000E+00 0.0000000E+00 0.2002100     010004\n" +
                            "  0.0000000E+00 0.0000000E+00  4.894083    -0.2459141     0.0000000E+00 001004\n" +
                            "  0.0000000E+00 0.0000000E+00  9856.834      13276.50     0.0000000E+00 000500\n" +
                            "  -1297.132     -554.0543     0.0000000E+00 0.0000000E+00 -1224.018     000401\n" +
                            "  0.0000000E+00 0.0000000E+00 -535.0675     -784.8926     0.0000000E+00 000302\n" +
                            "   515.9380      332.0163     0.0000000E+00 0.0000000E+00  59.47134     000203\n" +
                            "  0.0000000E+00 0.0000000E+00  19.11055      9.219070     0.0000000E+00 000104\n" +
                            "  0.2208185     0.1386849E-01 0.0000000E+00 0.0000000E+00 0.4936620     000005"
            );
            optics_后偏转段CCT2出口加0点5米.map = CosyArbitraryOrder.readMap(
                    " -0.3649934     0.6426617     0.0000000E+00 0.0000000E+00 0.9122470E-02 100000\n" +
                            "  -2.042222     0.8560639     0.0000000E+00 0.0000000E+00-0.2043148E-02 010000\n" +
                            "  0.0000000E+00 0.0000000E+00 -1.243856     0.1209793     0.0000000E+00 001000\n" +
                            "  0.0000000E+00 0.0000000E+00  2.159572     -1.013995     0.0000000E+00 000100\n" +
                            "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00  1.000000     000010\n" +
                            "  0.1937584E-01-0.9122470E-02 0.0000000E+00 0.0000000E+00 0.9048116     000001\n" +
                            "  -3.856079    -0.9384986     0.0000000E+00 0.0000000E+00 0.3847978E-01 200000\n" +
                            "  -16.47645     0.4203887     0.0000000E+00 0.0000000E+00 0.5503986     110000\n" +
                            "  -15.44844      9.054789     0.0000000E+00 0.0000000E+00 0.3564118     020000\n" +
                            "  0.0000000E+00 0.0000000E+00 -16.71809     0.7224107E-01 0.0000000E+00 101000\n" +
                            "  0.0000000E+00 0.0000000E+00 -48.44807    -0.1617974E-01 0.0000000E+00 011000\n" +
                            " -0.9001608     -1.062605     0.0000000E+00 0.0000000E+00-0.7880229     002000\n" +
                            "  0.0000000E+00 0.0000000E+00 -35.55376      16.96123     0.0000000E+00 100100\n" +
                            "  0.0000000E+00 0.0000000E+00 -91.20263      48.39362     0.0000000E+00 010100\n" +
                            "   16.35780      17.21543     0.0000000E+00 0.0000000E+00 0.9845696     001100\n" +
                            "  0.1223396     0.2878378     0.0000000E+00 0.0000000E+00-0.1211058E-01 100001\n" +
                            "   1.179380    -0.1791349     0.0000000E+00 0.0000000E+00-0.6972787E-02 010001\n" +
                            "  0.0000000E+00 0.0000000E+00  2.655046     -1.478990     0.0000000E+00 001001\n" +
                            "   1.605674     -3.618511     0.0000000E+00 0.0000000E+00 -5.297708     000200\n" +
                            "  0.0000000E+00 0.0000000E+00  11.95755    -0.7596009     0.0000000E+00 000101\n" +
                            " -0.1659814E-01 0.3466170E-02 0.0000000E+00 0.0000000E+00-0.7752622     000002\n" +
                            "   2.322881      7.152255     0.0000000E+00 0.0000000E+00  1.130487     300000\n" +
                            "  -3.902353      49.65737     0.0000000E+00 0.0000000E+00  6.462427     210000\n" +
                            "  -69.09869      119.5888     0.0000000E+00 0.0000000E+00  11.75553     120000\n" +
                            "  -111.1914      104.1631     0.0000000E+00 0.0000000E+00  6.964815     030000\n" +
                            "  0.0000000E+00 0.0000000E+00 -32.28559     -49.36323     0.0000000E+00 201000\n" +
                            "  0.0000000E+00 0.0000000E+00 -231.5922     -283.7281     0.0000000E+00 111000\n" +
                            "  0.0000000E+00 0.0000000E+00 -396.7722     -408.8499     0.0000000E+00 021000\n" +
                            "  -56.40616     -61.78512     0.0000000E+00 0.0000000E+00 -13.18169     102000\n" +
                            "  -163.3941     -179.9721     0.0000000E+00 0.0000000E+00 -38.50041     012000\n" +
                            "  0.0000000E+00 0.0000000E+00 -8.451710      7.991402     0.0000000E+00 003000\n" +
                            "  0.0000000E+00 0.0000000E+00 -90.25269     -105.1017     0.0000000E+00 200100\n" +
                            "  0.0000000E+00 0.0000000E+00 -642.3625     -562.3619     0.0000000E+00 110100\n" +
                            "  0.0000000E+00 0.0000000E+00 -1096.943     -746.1316     0.0000000E+00 020100\n" +
                            "  -116.3306     -81.13163     0.0000000E+00 0.0000000E+00 -11.85894     101100\n" +
                            "  -280.8739     -177.8512     0.0000000E+00 0.0000000E+00 -18.81143     011100\n" +
                            "  0.0000000E+00 0.0000000E+00 -92.78006     -8.899062     0.0000000E+00 002100\n" +
                            "   1.611990    -0.3124385     0.0000000E+00 0.0000000E+00 -1.297524     200001\n" +
                            "   15.97109     -1.071326     0.0000000E+00 0.0000000E+00 -7.683015     110001\n" +
                            "   24.82605     -9.398257     0.0000000E+00 0.0000000E+00 -10.94237     020001\n" +
                            "  0.0000000E+00 0.0000000E+00 -4.038495      1.466792     0.0000000E+00 101001\n" +
                            "  0.0000000E+00 0.0000000E+00  20.70844      3.999936     0.0000000E+00 011001\n" +
                            "   12.74636      13.34955     0.0000000E+00 0.0000000E+00  3.652657     002001\n" +
                            "   319.8612      394.1117     0.0000000E+00 0.0000000E+00  88.70189     100200\n" +
                            "   823.9524      1028.775     0.0000000E+00 0.0000000E+00  239.4037     010200\n" +
                            "  0.0000000E+00 0.0000000E+00 -200.7347      118.5896     0.0000000E+00 001200\n" +
                            "  0.0000000E+00 0.0000000E+00  10.76470      3.143039     0.0000000E+00 100101\n" +
                            "  0.0000000E+00 0.0000000E+00  94.36044     -8.407664     0.0000000E+00 010101\n" +
                            "   9.583801      10.96243     0.0000000E+00 0.0000000E+00  2.023693     001101\n" +
                            "  -1.408444     -1.257306     0.0000000E+00 0.0000000E+00 0.7379030E-01 100002\n" +
                            "  -4.504715     -3.396756     0.0000000E+00 0.0000000E+00 0.5615621E-01 010002\n" +
                            "  0.0000000E+00 0.0000000E+00  1.257688      2.479428     0.0000000E+00 001002\n" +
                            "  0.0000000E+00 0.0000000E+00 -235.5131     -37.71834     0.0000000E+00 000300\n" +
                            "  -90.88135     -107.5484     0.0000000E+00 0.0000000E+00 -12.85089     000201\n" +
                            "  0.0000000E+00 0.0000000E+00 -7.022387      7.949556     0.0000000E+00 000102\n" +
                            "  0.6253374E-01 0.7664841E-03 0.0000000E+00 0.0000000E+00 0.6448256     000003\n" +
                            "  -25.54074     -5.141470     0.0000000E+00 0.0000000E+00  2.139541     400000\n" +
                            "  -261.0844      12.33315     0.0000000E+00 0.0000000E+00  41.39928     310000\n" +
                            "  -1029.564      315.1830     0.0000000E+00 0.0000000E+00  226.1447     220000\n" +
                            "  -1883.575      1009.924     0.0000000E+00 0.0000000E+00  482.3847     130000\n" +
                            "  -1361.687      949.0121     0.0000000E+00 0.0000000E+00  356.3324     040000\n" +
                            "  0.0000000E+00 0.0000000E+00  7.941965     -89.00101     0.0000000E+00 301000\n" +
                            "  0.0000000E+00 0.0000000E+00 -8.518370     -941.0548     0.0000000E+00 211000\n" +
                            "  0.0000000E+00 0.0000000E+00 -263.5848     -3206.110     0.0000000E+00 121000\n" +
                            "  0.0000000E+00 0.0000000E+00 -516.2759     -3550.254     0.0000000E+00 031000\n" +
                            "  -151.9163     -269.1779     0.0000000E+00 0.0000000E+00 -69.97060     202000\n" +
                            "  -1077.173     -1779.901     0.0000000E+00 0.0000000E+00 -460.8850     112000\n" +
                            "  -1838.503     -2883.760     0.0000000E+00 0.0000000E+00 -744.0277     022000\n" +
                            "  0.0000000E+00 0.0000000E+00  46.74408      152.4810     0.0000000E+00 103000\n" +
                            "  0.0000000E+00 0.0000000E+00  117.6506      436.9227     0.0000000E+00 013000\n" +
                            "  -46.55057     -51.60708     0.0000000E+00 0.0000000E+00 -12.91879     004000\n" +
                            "  0.0000000E+00 0.0000000E+00 -105.5958     -275.0373     0.0000000E+00 300100\n" +
                            "  0.0000000E+00 0.0000000E+00 -1056.126     -2711.685     0.0000000E+00 210100\n" +
                            "  0.0000000E+00 0.0000000E+00 -3584.848     -8793.095     0.0000000E+00 120100\n" +
                            "  0.0000000E+00 0.0000000E+00 -4122.597     -9381.519     0.0000000E+00 030100\n" +
                            "  -1987.060     -2290.160     0.0000000E+00 0.0000000E+00 -460.2137     201100\n" +
                            "  -11430.77     -13144.12     0.0000000E+00 0.0000000E+00 -2706.269     111100\n" +
                            "  -16431.05     -18843.72     0.0000000E+00 0.0000000E+00 -3950.207     021100\n" +
                            "  0.0000000E+00 0.0000000E+00 -158.0885     -446.2677     0.0000000E+00 102100\n" +
                            "  0.0000000E+00 0.0000000E+00 -578.3865     -1402.188     0.0000000E+00 012100\n" +
                            "  -187.5813     -167.2647     0.0000000E+00 0.0000000E+00 -28.02063     003100\n" +
                            "   15.13349      13.64138     0.0000000E+00 0.0000000E+00 -3.578746     300001\n" +
                            "   152.3390      94.47620     0.0000000E+00 0.0000000E+00 -35.41582     210001\n" +
                            "   553.8766      226.4977     0.0000000E+00 0.0000000E+00 -109.1225     120001\n" +
                            "   671.7232      175.5080     0.0000000E+00 0.0000000E+00 -107.8955     030001\n" +
                            "  0.0000000E+00 0.0000000E+00 0.1467100      11.62474     0.0000000E+00 201001\n" +
                            "  0.0000000E+00 0.0000000E+00  116.3923      209.7534     0.0000000E+00 111001\n" +
                            "  0.0000000E+00 0.0000000E+00  402.8363      514.6235     0.0000000E+00 021001\n" +
                            "   112.3218      128.1223     0.0000000E+00 0.0000000E+00  43.02954     102001\n" +
                            "   443.4611      502.9141     0.0000000E+00 0.0000000E+00  155.5563     012001\n" +
                            "  0.0000000E+00 0.0000000E+00 -20.88276     -53.50127     0.0000000E+00 003001\n" +
                            "  -4064.464     -4024.428     0.0000000E+00 0.0000000E+00 -792.4026     200200\n" +
                            "  -20152.47     -19786.01     0.0000000E+00 0.0000000E+00 -3869.230     110200\n" +
                            "  -24615.61     -23913.22     0.0000000E+00 0.0000000E+00 -4654.601     020200\n" +
                            "  0.0000000E+00 0.0000000E+00 -478.1001     -1687.808     0.0000000E+00 101200\n" +
                            "  0.0000000E+00 0.0000000E+00 -1499.836     -4497.967     0.0000000E+00 011200\n" +
                            "  -510.6220     -458.4714     0.0000000E+00 0.0000000E+00 -173.9339     002200\n" +
                            "  0.0000000E+00 0.0000000E+00  48.50473      81.57111     0.0000000E+00 200101\n" +
                            "  0.0000000E+00 0.0000000E+00  658.9439      714.0580     0.0000000E+00 110101\n" +
                            "  0.0000000E+00 0.0000000E+00  1680.123      1318.139     0.0000000E+00 020101\n" +
                            "   1096.525      1171.063     0.0000000E+00 0.0000000E+00  253.7448     101101\n" +
                            "   3140.045      3338.741     0.0000000E+00 0.0000000E+00  716.0882     011101\n" +
                            "  0.0000000E+00 0.0000000E+00  70.97745     -19.24840     0.0000000E+00 002101\n" +
                            "   1.627017      1.827457     0.0000000E+00 0.0000000E+00  1.993197     200002\n" +
                            "  -1.316719      8.110417     0.0000000E+00 0.0000000E+00  14.79300     110002\n" +
                            "  -15.91614      15.35879     0.0000000E+00 0.0000000E+00  26.49844     020002\n" +
                            "  0.0000000E+00 0.0000000E+00  8.329241     -3.347223     0.0000000E+00 101002\n" +
                            "  0.0000000E+00 0.0000000E+00  4.662657     -6.024555     0.0000000E+00 011002\n" +
                            "  -23.43189     -21.70399     0.0000000E+00 0.0000000E+00 -8.853902     002002\n" +
                            "  0.0000000E+00 0.0000000E+00  1053.906      2541.704     0.0000000E+00 100300\n" +
                            "  0.0000000E+00 0.0000000E+00  2622.494      6631.594     0.0000000E+00 010300\n" +
                            "   1379.663      1782.245     0.0000000E+00 0.0000000E+00  380.9628     001300\n" +
                            "   1646.720      1769.948     0.0000000E+00 0.0000000E+00  236.8034     100201\n" +
                            "   3802.993      3980.690     0.0000000E+00 0.0000000E+00  449.8176     010201\n" +
                            "  0.0000000E+00 0.0000000E+00  276.8154     -2.946112     0.0000000E+00 001201\n" +
                            "  0.0000000E+00 0.0000000E+00  15.99179     -13.07779     0.0000000E+00 100102\n" +
                            "  0.0000000E+00 0.0000000E+00 -36.39835     -25.64447     0.0000000E+00 010102\n" +
                            "  -149.3581     -156.6984     0.0000000E+00 0.0000000E+00 -32.89562     001102\n" +
                            "   1.797618     0.9202861     0.0000000E+00 0.0000000E+00-0.1670093     100003\n" +
                            "   6.954660      4.287810     0.0000000E+00 0.0000000E+00-0.1483648     010003\n" +
                            "  0.0000000E+00 0.0000000E+00 -3.265257     -1.434638     0.0000000E+00 001003\n" +
                            "   1329.692      1678.504     0.0000000E+00 0.0000000E+00  283.5819     000400\n" +
                            "  0.0000000E+00 0.0000000E+00  140.9158     -919.2864     0.0000000E+00 000301\n" +
                            "  -67.85935     -95.62678     0.0000000E+00 0.0000000E+00 -6.499641     000202\n" +
                            "  0.0000000E+00 0.0000000E+00 -1.846479     -10.65260     0.0000000E+00 000103\n" +
                            " -0.1197312     0.9097688E-03 0.0000000E+00 0.0000000E+00-0.5374431     000004\n" +
                            "  -20.60644      43.32215     0.0000000E+00 0.0000000E+00  29.25621     500000\n" +
                            "  -579.6287      519.1212     0.0000000E+00 0.0000000E+00  394.1380     410000\n" +
                            "  -4727.756      2656.822     0.0000000E+00 0.0000000E+00  2213.778     320000\n" +
                            "  -17017.18      7451.866     0.0000000E+00 0.0000000E+00  6455.522     230000\n" +
                            "  -28706.94      11491.58     0.0000000E+00 0.0000000E+00  9686.058     140000\n" +
                            "  -18644.88      7639.247     0.0000000E+00 0.0000000E+00  5913.409     050000\n" +
                            "  0.0000000E+00 0.0000000E+00  366.2438      52.33914     0.0000000E+00 401000\n" +
                            "  0.0000000E+00 0.0000000E+00  3990.478      50.49063     0.0000000E+00 311000\n" +
                            "  0.0000000E+00 0.0000000E+00  16387.87     -2208.704     0.0000000E+00 221000\n" +
                            "  0.0000000E+00 0.0000000E+00  30002.44     -9080.527     0.0000000E+00 131000\n" +
                            "  0.0000000E+00 0.0000000E+00  20541.50     -10234.67     0.0000000E+00 041000\n" +
                            "   1555.288      1075.420     0.0000000E+00 0.0000000E+00  45.91635     302000\n" +
                            "   12306.23      7793.277     0.0000000E+00 0.0000000E+00  138.7654     212000\n" +
                            "   32097.95      18081.94     0.0000000E+00 0.0000000E+00 -409.9899     122000\n" +
                            "   27480.20      13064.86     0.0000000E+00 0.0000000E+00 -1253.889     032000\n" +
                            "  0.0000000E+00 0.0000000E+00  647.1957      2066.764     0.0000000E+00 203000\n" +
                            "  0.0000000E+00 0.0000000E+00  3801.671      12288.65     0.0000000E+00 113000\n" +
                            "  0.0000000E+00 0.0000000E+00  5573.969      18199.19     0.0000000E+00 023000\n" +
                            "  -643.2982     -880.5636     0.0000000E+00 0.0000000E+00 -260.2233     104000\n" +
                            "  -1950.977     -2644.804     0.0000000E+00 0.0000000E+00 -780.6235     014000\n" +
                            "  0.0000000E+00 0.0000000E+00  57.72568      166.7964     0.0000000E+00 005000\n" +
                            "  0.0000000E+00 0.0000000E+00  711.9964     -45.27870     0.0000000E+00 400100\n" +
                            "  0.0000000E+00 0.0000000E+00  7012.227     -1949.253     0.0000000E+00 310100\n" +
                            "  0.0000000E+00 0.0000000E+00  25548.40     -14704.98     0.0000000E+00 220100\n" +
                            "  0.0000000E+00 0.0000000E+00  40424.48     -40753.78     0.0000000E+00 130100\n" +
                            "  0.0000000E+00 0.0000000E+00  22878.01     -38800.23     0.0000000E+00 040100\n" +
                            "   6834.769      4119.940     0.0000000E+00 0.0000000E+00  414.3444     301100\n" +
                            "   44057.69      19786.86     0.0000000E+00 0.0000000E+00  572.6076     211100\n" +
                            "   84923.41      13194.94     0.0000000E+00 0.0000000E+00 -6905.514     121100\n" +
                            "   42355.54     -27872.55     0.0000000E+00 0.0000000E+00 -14783.21     031100\n" +
                            "  0.0000000E+00 0.0000000E+00  2012.006      7478.198     0.0000000E+00 202100\n" +
                            "  0.0000000E+00 0.0000000E+00  10755.14      39894.06     0.0000000E+00 112100\n" +
                            "  0.0000000E+00 0.0000000E+00  14340.57      52928.16     0.0000000E+00 022100\n" +
                            "  -3037.543     -3516.721     0.0000000E+00 0.0000000E+00 -474.4247     103100\n" +
                            "  -8314.339     -9798.964     0.0000000E+00 0.0000000E+00 -1320.999     013100\n" +
                            "  0.0000000E+00 0.0000000E+00  40.20002      341.7039     0.0000000E+00 004100\n" +
                            "   4.240708    -0.4275915     0.0000000E+00 0.0000000E+00 -12.12673     400001\n" +
                            "   265.9288      48.05047     0.0000000E+00 0.0000000E+00 -189.1697     310001\n" +
                            "   2021.225      189.5257     0.0000000E+00 0.0000000E+00 -1049.104     220001\n" +
                            "   5602.008      18.17159     0.0000000E+00 0.0000000E+00 -2436.735     130001\n" +
                            "   5450.991     -342.9678     0.0000000E+00 0.0000000E+00 -2017.575     040001\n" +
                            "  0.0000000E+00 0.0000000E+00 -85.31210      28.97999     0.0000000E+00 301001\n" +
                            "  0.0000000E+00 0.0000000E+00 -655.4305      889.0741     0.0000000E+00 211001\n" +
                            "  0.0000000E+00 0.0000000E+00 -1421.407      4919.130     0.0000000E+00 121001\n" +
                            "  0.0000000E+00 0.0000000E+00 -652.9677      7465.805     0.0000000E+00 031001\n" +
                            "  -899.1015     -728.0282     0.0000000E+00 0.0000000E+00 -26.79000     202001\n" +
                            "  -3837.644     -2479.668     0.0000000E+00 0.0000000E+00  278.1970     112001\n" +
                            "  -3286.102     -783.0062     0.0000000E+00 0.0000000E+00  1108.165     022001\n" +
                            "  0.0000000E+00 0.0000000E+00 -449.3709     -1055.094     0.0000000E+00 103001\n" +
                            "  0.0000000E+00 0.0000000E+00 -1336.519     -3339.315     0.0000000E+00 013001\n" +
                            "   207.2229      235.7323     0.0000000E+00 0.0000000E+00  82.93906     004001\n" +
                            "   8061.982      4643.499     0.0000000E+00 0.0000000E+00  1127.469     300200\n" +
                            "   35283.28      6428.225     0.0000000E+00 0.0000000E+00  1864.032     210200\n" +
                            "   12563.86     -67968.09     0.0000000E+00 0.0000000E+00 -14240.90     120200\n" +
                            "  -63776.81     -137965.0     0.0000000E+00 0.0000000E+00 -29621.50     030200\n" +
                            "  0.0000000E+00 0.0000000E+00 -6193.025     -13579.21     0.0000000E+00 201200\n" +
                            "  0.0000000E+00 0.0000000E+00 -36033.93     -82969.16     0.0000000E+00 111200\n" +
                            "  0.0000000E+00 0.0000000E+00 -52474.00     -125773.7     0.0000000E+00 021200\n" +
                            "  -20248.86     -21976.50     0.0000000E+00 0.0000000E+00 -3675.749     102200\n" +
                            "  -57378.46     -62674.85     0.0000000E+00 0.0000000E+00 -11262.16     012200\n" +
                            "  0.0000000E+00 0.0000000E+00 -56.52471      407.8332     0.0000000E+00 003200\n" +
                            "  0.0000000E+00 0.0000000E+00 -187.3016      203.9511     0.0000000E+00 300101\n" +
                            "  0.0000000E+00 0.0000000E+00 -810.8836      3663.035     0.0000000E+00 210101\n" +
                            "  0.0000000E+00 0.0000000E+00  614.6921      17046.17     0.0000000E+00 120101\n" +
                            "  0.0000000E+00 0.0000000E+00  4297.228      23560.16     0.0000000E+00 030101\n" +
                            "  -3130.526     -2448.169     0.0000000E+00 0.0000000E+00  217.5210     201101\n" +
                            "  -5849.223     -644.1579     0.0000000E+00 0.0000000E+00  4165.435     111101\n" +
                            "   8790.237      17895.63     0.0000000E+00 0.0000000E+00  10074.78     021101\n" +
                            "  0.0000000E+00 0.0000000E+00 -1010.150     -3541.731     0.0000000E+00 102101\n" +
                            "  0.0000000E+00 0.0000000E+00 -2064.420     -8729.534     0.0000000E+00 012101\n" +
                            "   1198.033      1203.742     0.0000000E+00 0.0000000E+00  191.1471     003101\n" +
                            "  -13.72534     -8.445786     0.0000000E+00 0.0000000E+00  2.770130     300002\n" +
                            "  -210.1893     -107.8606     0.0000000E+00 0.0000000E+00  54.23345     210002\n" +
                            "  -969.0860     -392.8671     0.0000000E+00 0.0000000E+00  246.6527     120002\n" +
                            "  -1370.885     -429.1882     0.0000000E+00 0.0000000E+00  320.2043     030002\n" +
                            "  0.0000000E+00 0.0000000E+00 -7.689378     -13.69080     0.0000000E+00 201002\n" +
                            "  0.0000000E+00 0.0000000E+00 -104.0717     -214.1031     0.0000000E+00 111002\n" +
                            "  0.0000000E+00 0.0000000E+00 -394.0246     -628.5141     0.0000000E+00 021002\n" +
                            "   107.4362      103.1023     0.0000000E+00 0.0000000E+00 -37.32764     102002\n" +
                            "  -67.32177     -105.9714     0.0000000E+00 0.0000000E+00 -228.6766     012002\n" +
                            "  0.0000000E+00 0.0000000E+00  96.36006      165.9557     0.0000000E+00 003002\n" +
                            "  0.0000000E+00 0.0000000E+00 -15978.97     -43847.02     0.0000000E+00 200300\n" +
                            "  0.0000000E+00 0.0000000E+00 -83232.42     -226721.3     0.0000000E+00 110300\n" +
                            "  0.0000000E+00 0.0000000E+00 -108092.2     -291245.8     0.0000000E+00 020300\n" +
                            "  -62970.35     -68308.70     0.0000000E+00 0.0000000E+00 -16938.66     101300\n" +
                            "  -165560.6     -179932.9     0.0000000E+00 0.0000000E+00 -44603.27     011300\n" +
                            "  0.0000000E+00 0.0000000E+00 -2484.906     -4093.061     0.0000000E+00 002300\n" +
                            "  -491.8026     -442.6974     0.0000000E+00 0.0000000E+00  863.3229     200201\n" +
                            "   14671.21      16617.59     0.0000000E+00 0.0000000E+00  8611.594     110201\n" +
                            "   41169.97      45869.57     0.0000000E+00 0.0000000E+00  16365.88     020201\n" +
                            "  0.0000000E+00 0.0000000E+00  3732.584      8165.100     0.0000000E+00 101201\n" +
                            "  0.0000000E+00 0.0000000E+00  11863.78      25736.71     0.0000000E+00 011201\n" +
                            "   6260.149      6208.636     0.0000000E+00 0.0000000E+00  1348.044     002201\n" +
                            "  0.0000000E+00 0.0000000E+00 -46.98884     -86.98937     0.0000000E+00 200102\n" +
                            "  0.0000000E+00 0.0000000E+00 -617.8078     -873.1619     0.0000000E+00 110102\n" +
                            "  0.0000000E+00 0.0000000E+00 -1896.320     -1902.270     0.0000000E+00 020102\n" +
                            "  -485.0741     -344.5330     0.0000000E+00 0.0000000E+00 -375.4624     101102\n" +
                            "  -3388.053     -3242.395     0.0000000E+00 0.0000000E+00 -1566.914     011102\n" +
                            "  0.0000000E+00 0.0000000E+00  187.0850      568.3512     0.0000000E+00 002102\n" +
                            "  -4.648402     -2.527247     0.0000000E+00 0.0000000E+00 -2.130670     200003\n" +
                            "  -19.83120     -14.60486     0.0000000E+00 0.0000000E+00 -19.58058     110003\n" +
                            "  -7.035165     -23.18837     0.0000000E+00 0.0000000E+00 -43.16208     020003\n" +
                            "  0.0000000E+00 0.0000000E+00 -3.174830      7.283361     0.0000000E+00 101003\n" +
                            "  0.0000000E+00 0.0000000E+00 -8.204565      13.66047     0.0000000E+00 011003\n" +
                            "   11.05274      5.291407     0.0000000E+00 0.0000000E+00  13.02943     002003\n" +
                            "  -19724.39     -20003.99     0.0000000E+00 0.0000000E+00 -1623.850     100400\n" +
                            "  -50344.95     -50668.70     0.0000000E+00 0.0000000E+00 -3634.488     010400\n" +
                            "  0.0000000E+00 0.0000000E+00  2696.041      10087.77     0.0000000E+00 001400\n" +
                            "  0.0000000E+00 0.0000000E+00  5198.486      19493.43     0.0000000E+00 100301\n" +
                            "  0.0000000E+00 0.0000000E+00  13297.93      48306.51     0.0000000E+00 010301\n" +
                            "   12169.39      13731.82     0.0000000E+00 0.0000000E+00  3019.409     001301\n" +
                            "  -2573.388     -2191.460     0.0000000E+00 0.0000000E+00 -677.5575     100202\n" +
                            "  -8862.841     -8147.091     0.0000000E+00 0.0000000E+00 -2112.037     010202\n" +
                            "  0.0000000E+00 0.0000000E+00 -525.7981     -785.3652     0.0000000E+00 001202\n" +
                            "  0.0000000E+00 0.0000000E+00 -17.63741      31.79242     0.0000000E+00 100103\n" +
                            "  0.0000000E+00 0.0000000E+00  3.459104      80.68702     0.0000000E+00 010103\n" +
                            "   226.1881      187.8650     0.0000000E+00 0.0000000E+00  74.92682     001103\n" +
                            "  -1.826473    -0.5832457     0.0000000E+00 0.0000000E+00 0.2771848     100004\n" +
                            "  -7.866465     -4.009692     0.0000000E+00 0.0000000E+00 0.1803898     010004\n" +
                            "  0.0000000E+00 0.0000000E+00  3.840855    -0.2459141     0.0000000E+00 001004\n" +
                            "  0.0000000E+00 0.0000000E+00  3251.192      13276.50     0.0000000E+00 000500\n" +
                            "  -523.4017     -554.0543     0.0000000E+00 0.0000000E+00 -869.4137     000401\n" +
                            "  0.0000000E+00 0.0000000E+00 -397.3694     -784.8926     0.0000000E+00 000302\n" +
                            "   344.5960      332.0163     0.0000000E+00 0.0000000E+00  63.83200     000203\n" +
                            "  0.0000000E+00 0.0000000E+00  9.917536      9.219070     0.0000000E+00 000104\n" +
                            "  0.2159087     0.1386849E-01 0.0000000E+00 0.0000000E+00 0.4371438     000005"
            );
            optics_后偏转段CCT2出口.map = CosyArbitraryOrder.readMap(
                    " -0.6863243     0.6426617     0.0000000E+00 0.0000000E+00 0.9122470E-02 100000\n" +
                            "  -2.470254     0.8560639     0.0000000E+00 0.0000000E+00-0.2043148E-02 010000\n" +
                            "  0.0000000E+00 0.0000000E+00 -1.304345     0.1209793     0.0000000E+00 001000\n" +
                            "  0.0000000E+00 0.0000000E+00  2.666570     -1.013995     0.0000000E+00 000100\n" +
                            "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00  1.000000     000010\n" +
                            "  0.2393708E-01-0.9122470E-02 0.0000000E+00 0.0000000E+00 0.8074744     000001\n" +
                            "  -3.386830    -0.9384986     0.0000000E+00 0.0000000E+00 0.9617587E-01 200000\n" +
                            "  -16.68664     0.4203887     0.0000000E+00 0.0000000E+00 0.7041079     110000\n" +
                            "  -19.97583      9.054789     0.0000000E+00 0.0000000E+00 0.4587868     020000\n" +
                            "  0.0000000E+00 0.0000000E+00 -16.75421     0.7224107E-01 0.0000000E+00 101000\n" +
                            "  0.0000000E+00 0.0000000E+00 -48.43998    -0.1617974E-01 0.0000000E+00 011000\n" +
                            " -0.3688584     -1.062605     0.0000000E+00 0.0000000E+00-0.7859784     002000\n" +
                            "  0.0000000E+00 0.0000000E+00 -44.03437      16.96123     0.0000000E+00 100100\n" +
                            "  0.0000000E+00 0.0000000E+00 -115.3994      48.39362     0.0000000E+00 010100\n" +
                            "   7.750083      17.21543     0.0000000E+00 0.0000000E+00 0.9502961     001100\n" +
                            "  0.1579742     0.2878378     0.0000000E+00 0.0000000E+00-0.1374855E-01 100001\n" +
                            "   1.508123    -0.1791349     0.0000000E+00 0.0000000E+00-0.9154663E-02 010001\n" +
                            "  0.0000000E+00 0.0000000E+00  3.428341     -1.478990     0.0000000E+00 001001\n" +
                            "   3.414929     -3.618511     0.0000000E+00 0.0000000E+00 -5.154076     000200\n" +
                            "  0.0000000E+00 0.0000000E+00  12.05405    -0.7596009     0.0000000E+00 000101\n" +
                            " -0.2087995E-01 0.3466170E-02 0.0000000E+00 0.0000000E+00-0.6936654     000002\n" +
                            "  -1.319603      7.152255     0.0000000E+00 0.0000000E+00 0.9619759     300000\n" +
                            "  -28.99621      49.65737     0.0000000E+00 0.0000000E+00  6.313443     210000\n" +
                            "  -129.2463      119.5888     0.0000000E+00 0.0000000E+00  13.48189     120000\n" +
                            "  -163.4298      104.1631     0.0000000E+00 0.0000000E+00  9.130503     030000\n" +
                            "  0.0000000E+00 0.0000000E+00 -7.616465     -49.36323     0.0000000E+00 201000\n" +
                            "  0.0000000E+00 0.0000000E+00 -89.76146     -283.7281     0.0000000E+00 111000\n" +
                            "  0.0000000E+00 0.0000000E+00 -192.3694     -408.8499     0.0000000E+00 021000\n" +
                            "  -25.51595     -61.78512     0.0000000E+00 0.0000000E+00 -13.37004     102000\n" +
                            "  -73.41112     -179.9721     0.0000000E+00 0.0000000E+00 -38.75510     012000\n" +
                            "  0.0000000E+00 0.0000000E+00 -12.44785      7.991402     0.0000000E+00 003000\n" +
                            "  0.0000000E+00 0.0000000E+00 -37.59716     -105.1017     0.0000000E+00 200100\n" +
                            "  0.0000000E+00 0.0000000E+00 -360.9026     -562.3619     0.0000000E+00 110100\n" +
                            "  0.0000000E+00 0.0000000E+00 -723.6909     -746.1316     0.0000000E+00 020100\n" +
                            "  -75.72534     -81.13163     0.0000000E+00 0.0000000E+00 -8.215015     101100\n" +
                            "  -191.8957     -177.8512     0.0000000E+00 0.0000000E+00 -13.05360     011100\n" +
                            "  0.0000000E+00 0.0000000E+00 -88.31940     -8.899062     0.0000000E+00 002100\n" +
                            "   1.508827    -0.3124385     0.0000000E+00 0.0000000E+00 -1.328029     200001\n" +
                            "   16.63173     -1.071326     0.0000000E+00 0.0000000E+00 -7.872737     110001\n" +
                            "   32.06002     -9.398257     0.0000000E+00 0.0000000E+00 -11.15837     020001\n" +
                            "  0.0000000E+00 0.0000000E+00 -4.751353      1.466792     0.0000000E+00 101001\n" +
                            "  0.0000000E+00 0.0000000E+00  18.70442      3.999936     0.0000000E+00 011001\n" +
                            "   5.774737      13.34955     0.0000000E+00 0.0000000E+00  3.602378     002001\n" +
                            "   122.6402      394.1117     0.0000000E+00 0.0000000E+00  83.24705     100200\n" +
                            "   309.3450      1028.775     0.0000000E+00 0.0000000E+00  224.8283     010200\n" +
                            "  0.0000000E+00 0.0000000E+00 -260.1228      118.5896     0.0000000E+00 001200\n" +
                            "  0.0000000E+00 0.0000000E+00  13.92901      3.143039     0.0000000E+00 100101\n" +
                            "  0.0000000E+00 0.0000000E+00  112.0810     -8.407664     0.0000000E+00 010101\n" +
                            "   8.911855      10.96243     0.0000000E+00 0.0000000E+00  2.423382     001101\n" +
                            " -0.8310205     -1.257306     0.0000000E+00 0.0000000E+00 0.7608023E-01 100002\n" +
                            "  -3.031749     -3.396756     0.0000000E+00 0.0000000E+00 0.6064033E-01 010002\n" +
                            "  0.0000000E+00 0.0000000E+00-0.4200194      2.479428     0.0000000E+00 001002\n" +
                            "  0.0000000E+00 0.0000000E+00 -216.3933     -37.71834     0.0000000E+00 000300\n" +
                            "  -38.11579     -107.5484     0.0000000E+00 0.0000000E+00 -12.83703     000201\n" +
                            "  0.0000000E+00 0.0000000E+00 -11.00172      7.949556     0.0000000E+00 000102\n" +
                            "  0.6498726E-01 0.7664841E-03 0.0000000E+00 0.0000000E+00 0.5745409     000003\n" +
                            "  -22.67929     -5.141470     0.0000000E+00 0.0000000E+00  3.564667     400000\n" +
                            "  -266.6067      12.33315     0.0000000E+00 0.0000000E+00  52.01108     310000\n" +
                            "  -1189.791      315.1830     0.0000000E+00 0.0000000E+00  257.3349     220000\n" +
                            "  -2396.240      1009.924     0.0000000E+00 0.0000000E+00  530.9228     130000\n" +
                            "  -1841.169      949.0121     0.0000000E+00 0.0000000E+00  392.7554     040000\n" +
                            "  0.0000000E+00 0.0000000E+00  52.47149     -89.00101     0.0000000E+00 301000\n" +
                            "  0.0000000E+00 0.0000000E+00  462.0231     -941.0548     0.0000000E+00 211000\n" +
                            "  0.0000000E+00 0.0000000E+00  1339.088     -3206.110     0.0000000E+00 121000\n" +
                            "  0.0000000E+00 0.0000000E+00  1258.385     -3550.254     0.0000000E+00 031000\n" +
                            "  -16.99754     -269.1779     0.0000000E+00 0.0000000E+00 -82.45222     202000\n" +
                            "  -186.3506     -1779.901     0.0000000E+00 0.0000000E+00 -517.6890     112000\n" +
                            "  -396.0709     -2883.760     0.0000000E+00 0.0000000E+00 -803.5779     022000\n" +
                            "  0.0000000E+00 0.0000000E+00 -29.45589      152.4810     0.0000000E+00 103000\n" +
                            "  0.0000000E+00 0.0000000E+00 -100.7556      436.9227     0.0000000E+00 013000\n" +
                            "  -20.74315     -51.60708     0.0000000E+00 0.0000000E+00 -12.49092     004000\n" +
                            "  0.0000000E+00 0.0000000E+00  29.86571     -275.0373     0.0000000E+00 300100\n" +
                            "  0.0000000E+00 0.0000000E+00  289.7836     -2711.685     0.0000000E+00 210100\n" +
                            "  0.0000000E+00 0.0000000E+00  798.4121     -8793.095     0.0000000E+00 120100\n" +
                            "  0.0000000E+00 0.0000000E+00  563.2265     -9381.519     0.0000000E+00 030100\n" +
                            "  -848.0060     -2290.160     0.0000000E+00 0.0000000E+00 -468.5419     201100\n" +
                            "  -4875.629     -13144.12     0.0000000E+00 0.0000000E+00 -2693.370     111100\n" +
                            "  -7020.608     -18843.72     0.0000000E+00 0.0000000E+00 -3858.842     021100\n" +
                            "  0.0000000E+00 0.0000000E+00  63.85699     -446.2677     0.0000000E+00 102100\n" +
                            "  0.0000000E+00 0.0000000E+00  120.8207     -1402.188     0.0000000E+00 012100\n" +
                            "  -104.0771     -167.2647     0.0000000E+00 0.0000000E+00 -35.69709     003100\n" +
                            "   10.32489      13.64138     0.0000000E+00 0.0000000E+00 -3.482534     300001\n" +
                            "   119.2299      94.47620     0.0000000E+00 0.0000000E+00 -35.51434     210001\n" +
                            "   474.7060      226.4977     0.0000000E+00 0.0000000E+00 -113.2001     120001\n" +
                            "   613.5388      175.5080     0.0000000E+00 0.0000000E+00 -114.0392     030001\n" +
                            "  0.0000000E+00 0.0000000E+00 -19.29512      11.62474     0.0000000E+00 201001\n" +
                            "  0.0000000E+00 0.0000000E+00 -67.30019      209.7534     0.0000000E+00 111001\n" +
                            "  0.0000000E+00 0.0000000E+00  31.61820      514.6235     0.0000000E+00 021001\n" +
                            "   31.04956      128.1223     0.0000000E+00 0.0000000E+00  45.79432     102001\n" +
                            "   141.7916      502.9141     0.0000000E+00 0.0000000E+00  159.7762     012001\n" +
                            "  0.0000000E+00 0.0000000E+00  8.116982     -53.50127     0.0000000E+00 003001\n" +
                            "  -2045.361     -4024.428     0.0000000E+00 0.0000000E+00 -650.6373     200200\n" +
                            "  -10233.46     -19786.01     0.0000000E+00 0.0000000E+00 -3201.790     110200\n" +
                            "  -12638.34     -23913.22     0.0000000E+00 0.0000000E+00 -3879.002     020200\n" +
                            "  0.0000000E+00 0.0000000E+00  374.6192     -1687.808     0.0000000E+00 101200\n" +
                            "  0.0000000E+00 0.0000000E+00  765.7245     -4497.967     0.0000000E+00 011200\n" +
                            "  -280.0440     -458.4714     0.0000000E+00 0.0000000E+00 -124.9191     002200\n" +
                            "  0.0000000E+00 0.0000000E+00 -21.59446      81.57111     0.0000000E+00 200101\n" +
                            "  0.0000000E+00 0.0000000E+00  144.8105      714.0580     0.0000000E+00 110101\n" +
                            "  0.0000000E+00 0.0000000E+00  812.4886      1318.139     0.0000000E+00 020101\n" +
                            "   487.9860      1171.063     0.0000000E+00 0.0000000E+00  244.6298     101101\n" +
                            "   1420.512      3338.741     0.0000000E+00 0.0000000E+00  688.4510     011101\n" +
                            "  0.0000000E+00 0.0000000E+00  77.84729     -19.24840     0.0000000E+00 002101\n" +
                            "  0.8149678      1.827457     0.0000000E+00 0.0000000E+00  1.804244     200002\n" +
                            "  -5.771046      8.110417     0.0000000E+00 0.0000000E+00  14.09761     110002\n" +
                            "  -28.08859      15.35879     0.0000000E+00 0.0000000E+00  26.00548     020002\n" +
                            "  0.0000000E+00 0.0000000E+00  10.39296     -3.347223     0.0000000E+00 101002\n" +
                            "  0.0000000E+00 0.0000000E+00  8.788947     -6.024555     0.0000000E+00 011002\n" +
                            "  -8.633372     -21.70399     0.0000000E+00 0.0000000E+00 -8.426559     002002\n" +
                            "  0.0000000E+00 0.0000000E+00 -231.2041      2541.704     0.0000000E+00 100300\n" +
                            "  0.0000000E+00 0.0000000E+00 -732.1918      6631.594     0.0000000E+00 010300\n" +
                            "   483.8936      1782.245     0.0000000E+00 0.0000000E+00  328.6341     001300\n" +
                            "   871.7027      1769.948     0.0000000E+00 0.0000000E+00  219.7012     100201\n" +
                            "   2099.897      3980.690     0.0000000E+00 0.0000000E+00  435.1287     010201\n" +
                            "  0.0000000E+00 0.0000000E+00  312.4967     -2.946112     0.0000000E+00 001201\n" +
                            "  0.0000000E+00 0.0000000E+00  19.93759     -13.07779     0.0000000E+00 100102\n" +
                            "  0.0000000E+00 0.0000000E+00 -35.83051     -25.64447     0.0000000E+00 010102\n" +
                            "  -71.46508     -156.6984     0.0000000E+00 0.0000000E+00 -33.60114     001102\n" +
                            "   1.035825     0.9202861     0.0000000E+00 0.0000000E+00-0.1661947     100003\n" +
                            "   4.043081      4.287810     0.0000000E+00 0.0000000E+00-0.1455376     010003\n" +
                            "  0.0000000E+00 0.0000000E+00 -1.531871     -1.434638     0.0000000E+00 001003\n" +
                            "   491.3705      1678.504     0.0000000E+00 0.0000000E+00  296.2074     000400\n" +
                            "  0.0000000E+00 0.0000000E+00  590.1864     -919.2864     0.0000000E+00 000301\n" +
                            "  -49.35400     -95.62678     0.0000000E+00 0.0000000E+00 -8.469142     000202\n" +
                            "  0.0000000E+00 0.0000000E+00  5.685190     -10.65260     0.0000000E+00 000103\n" +
                            " -0.1222224     0.9097688E-03 0.0000000E+00 0.0000000E+00-0.4750998     000004\n" +
                            "  -44.92809      43.32215     0.0000000E+00 0.0000000E+00  26.35327     500000\n" +
                            "  -860.7955      519.1212     0.0000000E+00 0.0000000E+00  382.5719     410000\n" +
                            "  -6129.873      2656.822     0.0000000E+00 0.0000000E+00  2266.528     320000\n" +
                            "  -20894.71      7451.866     0.0000000E+00 0.0000000E+00  6828.626     230000\n" +
                            "  -34649.15      11491.58     0.0000000E+00 0.0000000E+00  10418.25     140000\n" +
                            "  -22574.48      7639.247     0.0000000E+00 0.0000000E+00  6406.284     050000\n" +
                            "  0.0000000E+00 0.0000000E+00  344.8844      52.33914     0.0000000E+00 401000\n" +
                            "  0.0000000E+00 0.0000000E+00  4005.824      50.49063     0.0000000E+00 311000\n" +
                            "  0.0000000E+00 0.0000000E+00  17614.54     -2208.704     0.0000000E+00 221000\n" +
                            "  0.0000000E+00 0.0000000E+00  34696.41     -9080.527     0.0000000E+00 131000\n" +
                            "  0.0000000E+00 0.0000000E+00  25725.92     -10234.67     0.0000000E+00 041000\n" +
                            "   1037.650      1075.420     0.0000000E+00 0.0000000E+00  7.536667     302000\n" +
                            "   8528.871      7793.277     0.0000000E+00 0.0000000E+00 -257.7955     212000\n" +
                            "   23279.41      18081.94     0.0000000E+00 0.0000000E+00 -1682.363     122000\n" +
                            "   21079.84      13064.86     0.0000000E+00 0.0000000E+00 -2548.222     032000\n" +
                            "  0.0000000E+00 0.0000000E+00 -384.1040      2066.764     0.0000000E+00 203000\n" +
                            "  0.0000000E+00 0.0000000E+00 -2331.487      12288.65     0.0000000E+00 113000\n" +
                            "  0.0000000E+00 0.0000000E+00 -3512.707      18199.19     0.0000000E+00 023000\n" +
                            "  -203.6406     -880.5636     0.0000000E+00 0.0000000E+00 -245.8356     104000\n" +
                            "  -629.0568     -2644.804     0.0000000E+00 0.0000000E+00 -724.8098     014000\n" +
                            "  0.0000000E+00 0.0000000E+00 -25.79441      166.7964     0.0000000E+00 005000\n" +
                            "  0.0000000E+00 0.0000000E+00  753.1890     -45.27870     0.0000000E+00 400100\n" +
                            "  0.0000000E+00 0.0000000E+00  8112.204     -1949.253     0.0000000E+00 310100\n" +
                            "  0.0000000E+00 0.0000000E+00  33168.98     -14704.98     0.0000000E+00 220100\n" +
                            "  0.0000000E+00 0.0000000E+00  60982.47     -40753.78     0.0000000E+00 130100\n" +
                            "  0.0000000E+00 0.0000000E+00  42293.36     -38800.23     0.0000000E+00 040100\n" +
                            "   4804.505      4119.940     0.0000000E+00 0.0000000E+00 -158.9943     301100\n" +
                            "   34217.42      19786.86     0.0000000E+00 0.0000000E+00 -3897.520     211100\n" +
                            "   78155.65      13194.94     0.0000000E+00 0.0000000E+00 -18245.89     121100\n" +
                            "   56030.79     -27872.55     0.0000000E+00 0.0000000E+00 -24069.44     031100\n" +
                            "  0.0000000E+00 0.0000000E+00 -1744.418      7478.198     0.0000000E+00 202100\n" +
                            "  0.0000000E+00 0.0000000E+00 -9287.251      39894.06     0.0000000E+00 112100\n" +
                            "  0.0000000E+00 0.0000000E+00 -12249.85      52928.16     0.0000000E+00 022100\n" +
                            "  -1261.114     -3516.721     0.0000000E+00 0.0000000E+00 -797.9895     103100\n" +
                            "  -3394.688     -9798.964     0.0000000E+00 0.0000000E+00 -2236.703     013100\n" +
                            "  0.0000000E+00 0.0000000E+00 -127.6908      341.7039     0.0000000E+00 004100\n" +
                            "   2.958269    -0.4275915     0.0000000E+00 0.0000000E+00 -11.07984     400001\n" +
                            "   245.4500      48.05047     0.0000000E+00 0.0000000E+00 -180.6402     310001\n" +
                            "   2021.630      189.5257     0.0000000E+00 0.0000000E+00 -1023.848     220001\n" +
                            "   5897.040      18.17159     0.0000000E+00 0.0000000E+00 -2426.537     130001\n" +
                            "   5904.997     -342.9678     0.0000000E+00 0.0000000E+00 -2060.586     040001\n" +
                            "  0.0000000E+00 0.0000000E+00 -125.4333      28.97999     0.0000000E+00 301001\n" +
                            "  0.0000000E+00 0.0000000E+00 -1365.082      889.0741     0.0000000E+00 211001\n" +
                            "  0.0000000E+00 0.0000000E+00 -4774.848      4919.130     0.0000000E+00 121001\n" +
                            "  0.0000000E+00 0.0000000E+00 -5372.952      7465.805     0.0000000E+00 031001\n" +
                            "  -615.3499     -728.0282     0.0000000E+00 0.0000000E+00  27.61602     202001\n" +
                            "  -3109.986     -2479.668     0.0000000E+00 0.0000000E+00  601.7870     112001\n" +
                            "  -3710.639     -783.0062     0.0000000E+00 0.0000000E+00  1554.937     022001\n" +
                            "  0.0000000E+00 0.0000000E+00  119.6966     -1055.094     0.0000000E+00 103001\n" +
                            "  0.0000000E+00 0.0000000E+00  453.6265     -3339.315     0.0000000E+00 013001\n" +
                            "   74.79994      235.7323     0.0000000E+00 0.0000000E+00  73.36852     004001\n" +
                            "   5524.402      4643.499     0.0000000E+00 0.0000000E+00 -129.4786     300200\n" +
                            "   30835.60      6428.225     0.0000000E+00 0.0000000E+00 -6260.841     210200\n" +
                            "   44357.94     -67968.09     0.0000000E+00 0.0000000E+00 -30945.58     120200\n" +
                            "   4052.428     -137965.0     0.0000000E+00 0.0000000E+00 -40286.79     030200\n" +
                            "  0.0000000E+00 0.0000000E+00  434.8683     -13579.21     0.0000000E+00 201200\n" +
                            "  0.0000000E+00 0.0000000E+00  4846.737     -82969.16     0.0000000E+00 111200\n" +
                            "  0.0000000E+00 0.0000000E+00  9949.241     -125773.7     0.0000000E+00 021200\n" +
                            "  -9431.373     -21976.50     0.0000000E+00 0.0000000E+00 -4174.988     102200\n" +
                            "  -26291.30     -62674.85     0.0000000E+00 0.0000000E+00 -12229.21     012200\n" +
                            "  0.0000000E+00 0.0000000E+00 -288.0174      407.8332     0.0000000E+00 003200\n" +
                            "  0.0000000E+00 0.0000000E+00 -365.4483      203.9511     0.0000000E+00 300101\n" +
                            "  0.0000000E+00 0.0000000E+00 -3391.966      3663.035     0.0000000E+00 210101\n" +
                            "  0.0000000E+00 0.0000000E+00 -10347.02      17046.17     0.0000000E+00 120101\n" +
                            "  0.0000000E+00 0.0000000E+00 -10093.79      23560.16     0.0000000E+00 030101\n" +
                            "  -2536.331     -2448.169     0.0000000E+00 0.0000000E+00  495.4737     201101\n" +
                            "  -9150.137     -644.1579     0.0000000E+00 0.0000000E+00  5341.838     111101\n" +
                            "  -5377.353      17895.63     0.0000000E+00 0.0000000E+00  11127.12     021101\n" +
                            "  0.0000000E+00 0.0000000E+00  654.1887     -3541.731     0.0000000E+00 102101\n" +
                            "  0.0000000E+00 0.0000000E+00  1942.128     -8729.534     0.0000000E+00 012101\n" +
                            "   552.4195      1203.742     0.0000000E+00 0.0000000E+00  280.2948     003101\n" +
                            "  -6.790525     -8.445786     0.0000000E+00 0.0000000E+00  3.290680     300002\n" +
                            "  -138.2126     -107.8606     0.0000000E+00 0.0000000E+00  56.80431     210002\n" +
                            "  -731.3060     -392.8671     0.0000000E+00 0.0000000E+00  252.0125     120002\n" +
                            "  -1127.602     -429.1882     0.0000000E+00 0.0000000E+00  323.6925     030002\n" +
                            "  0.0000000E+00 0.0000000E+00  12.18786     -13.69080     0.0000000E+00 201002\n" +
                            "  0.0000000E+00 0.0000000E+00  118.5801     -214.1031     0.0000000E+00 111002\n" +
                            "  0.0000000E+00 0.0000000E+00  146.7775     -628.5141     0.0000000E+00 021002\n" +
                            "   103.9533      103.1023     0.0000000E+00 0.0000000E+00 -44.82228     102002\n" +
                            "   162.4910     -105.9714     0.0000000E+00 0.0000000E+00 -242.9506     012002\n" +
                            "  0.0000000E+00 0.0000000E+00 -3.440347      165.9557     0.0000000E+00 003002\n" +
                            "  0.0000000E+00 0.0000000E+00  6398.279     -43847.02     0.0000000E+00 200300\n" +
                            "  0.0000000E+00 0.0000000E+00  32409.15     -226721.3     0.0000000E+00 110300\n" +
                            "  0.0000000E+00 0.0000000E+00  40399.12     -291245.8     0.0000000E+00 020300\n" +
                            "  -28519.13     -68308.70     0.0000000E+00 0.0000000E+00 -13508.11     101300\n" +
                            "  -74918.89     -179932.9     0.0000000E+00 0.0000000E+00 -35932.71     011300\n" +
                            "  0.0000000E+00 0.0000000E+00 -328.4016     -4093.061     0.0000000E+00 002300\n" +
                            "  -1359.330     -442.6974     0.0000000E+00 0.0000000E+00  1058.079     200201\n" +
                            "   921.8547      16617.59     0.0000000E+00 0.0000000E+00  8793.377     110201\n" +
                            "   11604.72      45869.57     0.0000000E+00 0.0000000E+00  15599.41     020201\n" +
                            "  0.0000000E+00 0.0000000E+00 -857.6703      8165.100     0.0000000E+00 101201\n" +
                            "  0.0000000E+00 0.0000000E+00 -2378.169      25736.71     0.0000000E+00 011201\n" +
                            "   3013.278      6208.636     0.0000000E+00 0.0000000E+00  1306.552     002201\n" +
                            "  0.0000000E+00 0.0000000E+00  39.54548     -86.98937     0.0000000E+00 200102\n" +
                            "  0.0000000E+00 0.0000000E+00  129.3937     -873.1619     0.0000000E+00 110102\n" +
                            "  0.0000000E+00 0.0000000E+00 -427.1408     -1902.270     0.0000000E+00 020102\n" +
                            "   31.38276     -344.5330     0.0000000E+00 0.0000000E+00 -388.8908     101102\n" +
                            "  -797.1001     -3242.395     0.0000000E+00 0.0000000E+00 -1554.438     011102\n" +
                            "  0.0000000E+00 0.0000000E+00 -98.54862      568.3512     0.0000000E+00 002102\n" +
                            "  -2.976796     -2.527247     0.0000000E+00 0.0000000E+00 -1.781038     200003\n" +
                            "  -10.00083     -14.60486     0.0000000E+00 0.0000000E+00 -17.73018     110003\n" +
                            "   12.28373     -23.18837     0.0000000E+00 0.0000000E+00 -41.21156     020003\n" +
                            "  0.0000000E+00 0.0000000E+00 -8.025718      7.283361     0.0000000E+00 101003\n" +
                            "  0.0000000E+00 0.0000000E+00 -17.51757      13.66047     0.0000000E+00 011003\n" +
                            " -0.5629155      5.291407     0.0000000E+00 0.0000000E+00  11.41497     002003\n" +
                            "  -9873.542     -20003.99     0.0000000E+00 0.0000000E+00 -2628.125     100400\n" +
                            "  -25388.77     -50668.70     0.0000000E+00 0.0000000E+00 -6684.290     010400\n" +
                            "  0.0000000E+00 0.0000000E+00 -2478.333      10087.77     0.0000000E+00 001400\n" +
                            "  0.0000000E+00 0.0000000E+00 -3874.891      19493.43     0.0000000E+00 100301\n" +
                            "  0.0000000E+00 0.0000000E+00 -9039.992      48306.51     0.0000000E+00 010301\n" +
                            "   5794.292      13731.82     0.0000000E+00 0.0000000E+00  2523.901     001301\n" +
                            "  -1061.801     -2191.460     0.0000000E+00 0.0000000E+00 -638.6222     100202\n" +
                            "  -3884.436     -8147.091     0.0000000E+00 0.0000000E+00 -1992.275     010202\n" +
                            "  0.0000000E+00 0.0000000E+00 -158.8765     -785.3652     0.0000000E+00 001202\n" +
                            "  0.0000000E+00 0.0000000E+00 -34.93952      31.79242     0.0000000E+00 100103\n" +
                            "  0.0000000E+00 0.0000000E+00 -34.11396      80.68702     0.0000000E+00 010103\n" +
                            "   89.12220      187.8650     0.0000000E+00 0.0000000E+00  72.47366     001103\n" +
                            "  -1.066163    -0.5832457     0.0000000E+00 0.0000000E+00 0.2717855     100004\n" +
                            "  -4.124141     -4.009692     0.0000000E+00 0.0000000E+00 0.1605696     010004\n" +
                            "  0.0000000E+00 0.0000000E+00  2.787627    -0.2459141     0.0000000E+00 001004\n" +
                            "  0.0000000E+00 0.0000000E+00 -3354.450      13276.50     0.0000000E+00 000500\n" +
                            "   250.3285     -554.0543     0.0000000E+00 0.0000000E+00 -514.8098     000401\n" +
                            "  0.0000000E+00 0.0000000E+00 -259.6713     -784.8926     0.0000000E+00 000302\n" +
                            "   173.2541      332.0163     0.0000000E+00 0.0000000E+00  68.19266     000203\n" +
                            "  0.0000000E+00 0.0000000E+00 0.7245205      9.219070     0.0000000E+00 000104\n" +
                            "  0.2109989     0.1386849E-01 0.0000000E+00 0.0000000E+00 0.3806256     000005"
            );
            slice3d_后偏转段全部.map = CosyArbitraryOrder.readMap(
                    "   1.001505     0.7197942     0.0000000E+00 0.0000000E+00 0.1958641E-01 100000\n" +
                            "  0.2214660      1.157667     0.0000000E+00 0.0000000E+00 0.3413686E-01 010000\n" +
                            "  0.0000000E+00 0.0000000E+00 -1.148943     0.8236633E-01 0.0000000E+00 001000\n" +
                            "  0.0000000E+00 0.0000000E+00 0.5674790    -0.9110468     0.0000000E+00 000100\n" +
                            "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00  1.000000     000010\n" +
                            "  0.2985053E-01 0.1896972E-02 0.0000000E+00 0.0000000E+00  1.278428     000001\n" +
                            "  -8.682380     -1.110426     0.0000000E+00 0.0000000E+00 0.4807971     200000\n" +
                            "  -23.35244      2.797604     0.0000000E+00 0.0000000E+00  3.964751     110000\n" +
                            "   5.461528      17.73145     0.0000000E+00 0.0000000E+00  6.034806     020000\n" +
                            "  0.0000000E+00 0.0000000E+00 -13.15046     0.9246427     0.0000000E+00 101000\n" +
                            "  0.0000000E+00 0.0000000E+00 -38.10718     0.7678874E-01 0.0000000E+00 011000\n" +
                            "  -1.525255     -1.085838     0.0000000E+00 0.0000000E+00-0.9591463     002000\n" +
                            "  0.0000000E+00 0.0000000E+00 -13.68086      10.95164     0.0000000E+00 100100\n" +
                            "  0.0000000E+00 0.0000000E+00 -20.47106      31.64644     0.0000000E+00 010100\n" +
                            "   32.18894      11.69583     0.0000000E+00 0.0000000E+00  1.253436     001100\n" +
                            "   3.622371      1.609991     0.0000000E+00 0.0000000E+00 0.3408407E-01 100001\n" +
                            "   10.19860      3.498687     0.0000000E+00 0.0000000E+00 0.3390956E-01 010001\n" +
                            "  0.0000000E+00 0.0000000E+00-0.5406912E-01 -1.614353     0.0000000E+00 001001\n" +
                            " -0.3457591     -3.368345     0.0000000E+00 0.0000000E+00 -6.046219     000200\n" +
                            "  0.0000000E+00 0.0000000E+00  13.11616    -0.1000564     0.0000000E+00 000101\n" +
                            " -0.2484274E-01-0.1430839E-01 0.0000000E+00 0.0000000E+00 -1.111531     000002\n" +
                            "   12.69027      11.89160     0.0000000E+00 0.0000000E+00  5.118624     300000\n" +
                            "   19.42668      78.13505     0.0000000E+00 0.0000000E+00  34.09812     210000\n" +
                            "  -180.3515      160.5301     0.0000000E+00 0.0000000E+00  82.37404     120000\n" +
                            "  -368.4620      102.3100     0.0000000E+00 0.0000000E+00  77.09792     030000\n" +
                            "  0.0000000E+00 0.0000000E+00 -77.44613     -44.68365     0.0000000E+00 201000\n" +
                            "  0.0000000E+00 0.0000000E+00 -482.3866     -250.8475     0.0000000E+00 111000\n" +
                            "  0.0000000E+00 0.0000000E+00 -729.6286     -347.5465     0.0000000E+00 021000\n" +
                            "  -142.3309     -60.10848     0.0000000E+00 0.0000000E+00 -15.28240     102000\n" +
                            "  -391.8435     -164.5649     0.0000000E+00 0.0000000E+00 -39.31641     012000\n" +
                            "  0.0000000E+00 0.0000000E+00  20.47143      19.65001     0.0000000E+00 003000\n" +
                            "  0.0000000E+00 0.0000000E+00 -27.78602     -28.86696     0.0000000E+00 200100\n" +
                            "  0.0000000E+00 0.0000000E+00 -331.1403     -177.9202     0.0000000E+00 110100\n" +
                            "  0.0000000E+00 0.0000000E+00 -659.3989     -250.7706     0.0000000E+00 020100\n" +
                            "  -182.1766     -59.88994     0.0000000E+00 0.0000000E+00 -20.84472     101100\n" +
                            "  -455.4340     -149.0068     0.0000000E+00 0.0000000E+00 -42.46873     011100\n" +
                            "  0.0000000E+00 0.0000000E+00 -271.1276     -28.58861     0.0000000E+00 002100\n" +
                            "  -14.72360     -5.041197     0.0000000E+00 0.0000000E+00-0.9497370     200001\n" +
                            "  -61.80534     -22.22342     0.0000000E+00 0.0000000E+00 -5.973685     110001\n" +
                            "  -96.26932     -36.24501     0.0000000E+00 0.0000000E+00 -9.172220     020001\n" +
                            "  0.0000000E+00 0.0000000E+00 -12.76685     -1.940814     0.0000000E+00 101001\n" +
                            "  0.0000000E+00 0.0000000E+00  10.86116      1.574074     0.0000000E+00 011001\n" +
                            "   24.74919      10.27749     0.0000000E+00 0.0000000E+00  5.128764     002001\n" +
                            "   363.8933      162.2657     0.0000000E+00 0.0000000E+00  89.16960     100200\n" +
                            "   680.4963      312.7745     0.0000000E+00 0.0000000E+00  216.5580     010200\n" +
                            "  0.0000000E+00 0.0000000E+00  63.75195      232.8284     0.0000000E+00 001200\n" +
                            "  0.0000000E+00 0.0000000E+00 -46.87101     -14.70629     0.0000000E+00 100101\n" +
                            "  0.0000000E+00 0.0000000E+00 -56.49149     -33.15064     0.0000000E+00 010101\n" +
                            "  0.3466824      7.774483     0.0000000E+00 0.0000000E+00 0.7216933     001101\n" +
                            " -0.2973518     0.4192319     0.0000000E+00 0.0000000E+00-0.2065997E-01 100002\n" +
                            "  -6.633132    -0.5904059     0.0000000E+00 0.0000000E+00-0.1194543E-01 010002\n" +
                            "  0.0000000E+00 0.0000000E+00  9.323558      3.560380     0.0000000E+00 001002\n" +
                            "  0.0000000E+00 0.0000000E+00 -592.9973     -73.23722     0.0000000E+00 000300\n" +
                            "  -192.1463     -80.92551     0.0000000E+00 0.0000000E+00 -14.08787     000201\n" +
                            "  0.0000000E+00 0.0000000E+00  7.958299      8.711822     0.0000000E+00 000102\n" +
                            " -0.3746540E-01-0.7820357E-02 0.0000000E+00 0.0000000E+00 0.9795391     000003\n" +
                            "   38.24589      45.70379     0.0000000E+00 0.0000000E+00 -8.602248     400000\n" +
                            "   403.0947      526.7877     0.0000000E+00 0.0000000E+00 -22.61659     310000\n" +
                            "   1396.833      2245.468     0.0000000E+00 0.0000000E+00  117.4128     220000\n" +
                            "   1545.004      4145.321     0.0000000E+00 0.0000000E+00  424.0911     130000\n" +
                            "  -89.56320      2769.824     0.0000000E+00 0.0000000E+00  304.0343     040000\n" +
                            "  0.0000000E+00 0.0000000E+00 -598.0761     -267.2335     0.0000000E+00 301000\n" +
                            "  0.0000000E+00 0.0000000E+00 -4963.304     -2240.251     0.0000000E+00 211000\n" +
                            "  0.0000000E+00 0.0000000E+00 -13917.87     -6322.549     0.0000000E+00 121000\n" +
                            "  0.0000000E+00 0.0000000E+00 -13191.38     -5994.196     0.0000000E+00 031000\n" +
                            "  -583.0676     -295.9346     0.0000000E+00 0.0000000E+00 -10.60418     202000\n" +
                            "  -3293.010     -1661.775     0.0000000E+00 0.0000000E+00 -112.8700     112000\n" +
                            "  -4629.141     -2327.571     0.0000000E+00 0.0000000E+00 -237.8877     022000\n" +
                            "  0.0000000E+00 0.0000000E+00  986.0576      400.9150     0.0000000E+00 103000\n" +
                            "  0.0000000E+00 0.0000000E+00  2354.943      967.4784     0.0000000E+00 013000\n" +
                            "  -140.2806     -59.77699     0.0000000E+00 0.0000000E+00 -32.07407     004000\n" +
                            "  0.0000000E+00 0.0000000E+00 -638.0191     -265.5404     0.0000000E+00 300100\n" +
                            "  0.0000000E+00 0.0000000E+00 -4552.845     -1883.610     0.0000000E+00 210100\n" +
                            "  0.0000000E+00 0.0000000E+00 -11241.56     -4560.456     0.0000000E+00 120100\n" +
                            "  0.0000000E+00 0.0000000E+00 -9798.964     -3804.669     0.0000000E+00 030100\n" +
                            "  -2093.511     -1013.629     0.0000000E+00 0.0000000E+00 -437.5920     201100\n" +
                            "  -10049.21     -4920.606     0.0000000E+00 0.0000000E+00 -2434.196     111100\n" +
                            "  -12239.51     -6052.234     0.0000000E+00 0.0000000E+00 -3432.034     021100\n" +
                            "  0.0000000E+00 0.0000000E+00 -1548.139     -711.0547     0.0000000E+00 102100\n" +
                            "  0.0000000E+00 0.0000000E+00 -5015.619     -2189.685     0.0000000E+00 012100\n" +
                            "  -820.5860     -361.0536     0.0000000E+00 0.0000000E+00 -73.87526     003100\n" +
                            "  -118.9510     -32.90138     0.0000000E+00 0.0000000E+00  9.032559     300001\n" +
                            "  -1112.129     -360.6396     0.0000000E+00 0.0000000E+00  33.50547     210001\n" +
                            "  -3211.214     -1193.484     0.0000000E+00 0.0000000E+00 -15.23484     120001\n" +
                            "  -2911.438     -1244.602     0.0000000E+00 0.0000000E+00 -126.6021     030001\n" +
                            "  0.0000000E+00 0.0000000E+00  242.8709      82.46749     0.0000000E+00 201001\n" +
                            "  0.0000000E+00 0.0000000E+00  1508.665      557.2639     0.0000000E+00 111001\n" +
                            "  0.0000000E+00 0.0000000E+00  2403.204      924.5316     0.0000000E+00 021001\n" +
                            "   395.8359      142.8303     0.0000000E+00 0.0000000E+00  52.03933     102001\n" +
                            "   1342.878      495.1171     0.0000000E+00 0.0000000E+00  160.3639     012001\n" +
                            "  0.0000000E+00 0.0000000E+00 -363.2433     -144.3635     0.0000000E+00 003001\n" +
                            "  -4234.385     -1668.239     0.0000000E+00 0.0000000E+00 -875.9793     200200\n" +
                            "  -15742.06     -6263.972     0.0000000E+00 0.0000000E+00 -3920.170     110200\n" +
                            "  -12421.53     -5068.590     0.0000000E+00 0.0000000E+00 -4344.403     020200\n" +
                            "  0.0000000E+00 0.0000000E+00 -5889.422     -2584.629     0.0000000E+00 101200\n" +
                            "  0.0000000E+00 0.0000000E+00 -15993.42     -6844.361     0.0000000E+00 011200\n" +
                            "  -352.2773     -77.05466     0.0000000E+00 0.0000000E+00 -329.8565     002200\n" +
                            "  0.0000000E+00 0.0000000E+00  571.9358      266.0890     0.0000000E+00 200101\n" +
                            "  0.0000000E+00 0.0000000E+00  3007.447      1413.450     0.0000000E+00 110101\n" +
                            "  0.0000000E+00 0.0000000E+00  4151.831      1886.983     0.0000000E+00 020101\n" +
                            "   2049.403      829.7536     0.0000000E+00 0.0000000E+00  271.4584     101101\n" +
                            "   5339.286      2144.365     0.0000000E+00 0.0000000E+00  695.1705     011101\n" +
                            "  0.0000000E+00 0.0000000E+00  80.41580     -77.66574     0.0000000E+00 002101\n" +
                            "   3.373505     -1.931820     0.0000000E+00 0.0000000E+00 -1.656644     200002\n" +
                            "   44.20674    -0.1301155     0.0000000E+00 0.0000000E+00 -6.437828     110002\n" +
                            "   112.3904      17.75345     0.0000000E+00 0.0000000E+00 -6.582877     020002\n" +
                            "  0.0000000E+00 0.0000000E+00-0.3364877     -4.446043     0.0000000E+00 101002\n" +
                            "  0.0000000E+00 0.0000000E+00 -26.07397     -12.10344     0.0000000E+00 011002\n" +
                            "  -55.70593     -18.37824     0.0000000E+00 0.0000000E+00 -15.97992     002002\n" +
                            "  0.0000000E+00 0.0000000E+00  11373.10      4514.236     0.0000000E+00 100300\n" +
                            "  0.0000000E+00 0.0000000E+00  25215.52      10067.19     0.0000000E+00 010300\n" +
                            "   4476.153      1946.223     0.0000000E+00 0.0000000E+00  1035.495     001300\n" +
                            "   2608.255      1090.274     0.0000000E+00 0.0000000E+00  255.9546     100201\n" +
                            "   5491.957      2228.184     0.0000000E+00 0.0000000E+00  435.3214     010201\n" +
                            "  0.0000000E+00 0.0000000E+00  686.5163      66.72168     0.0000000E+00 001201\n" +
                            "  0.0000000E+00 0.0000000E+00  7.982144     -14.85835     0.0000000E+00 100102\n" +
                            "  0.0000000E+00 0.0000000E+00 -36.28820     -36.70957     0.0000000E+00 010102\n" +
                            "  -286.5609     -124.3492     0.0000000E+00 0.0000000E+00 -39.01193     001102\n" +
                            " -0.8612442    -0.3899004     0.0000000E+00 0.0000000E+00 0.6685468E-01 100003\n" +
                            "   2.500203    -0.5136013     0.0000000E+00 0.0000000E+00 0.1398462E-01 010003\n" +
                            "  0.0000000E+00 0.0000000E+00 -15.20022     -3.779337     0.0000000E+00 001003\n" +
                            "   6546.108      2900.899     0.0000000E+00 0.0000000E+00  669.9226     000400\n" +
                            "  0.0000000E+00 0.0000000E+00 -3450.442     -1869.282     0.0000000E+00 000301\n" +
                            "  -101.9995     -86.84487     0.0000000E+00 0.0000000E+00-0.7178140     000202\n" +
                            "  0.0000000E+00 0.0000000E+00 -35.27149     -14.23651     0.0000000E+00 000103\n" +
                            "  0.7606564E-01 0.4202587E-02 0.0000000E+00 0.0000000E+00-0.9054385     000004\n" +
                            "   2093.580      797.7516     0.0000000E+00 0.0000000E+00 -55.20697     500000\n" +
                            "   30205.73      11762.77     0.0000000E+00 0.0000000E+00 -909.5154     410000\n" +
                            "   175098.0      69404.51     0.0000000E+00 0.0000000E+00 -5421.985     320000\n" +
                            "   509841.9      205098.4     0.0000000E+00 0.0000000E+00 -14883.93     230000\n" +
                            "   745260.6      303763.4     0.0000000E+00 0.0000000E+00 -18855.57     140000\n" +
                            "   437028.4      180425.5     0.0000000E+00 0.0000000E+00 -8713.841     050000\n" +
                            "  0.0000000E+00 0.0000000E+00 -2950.386     -1324.958     0.0000000E+00 401000\n" +
                            "  0.0000000E+00 0.0000000E+00 -36532.46     -16426.77     0.0000000E+00 311000\n" +
                            "  0.0000000E+00 0.0000000E+00 -167457.3     -75394.84     0.0000000E+00 221000\n" +
                            "  0.0000000E+00 0.0000000E+00 -338189.1     -152494.3     0.0000000E+00 131000\n" +
                            "  0.0000000E+00 0.0000000E+00 -254770.1     -115060.0     0.0000000E+00 041000\n" +
                            "  -2742.132     -1120.102     0.0000000E+00 0.0000000E+00  549.5473     302000\n" +
                            "  -25055.83     -10565.50     0.0000000E+00 0.0000000E+00  4360.304     212000\n" +
                            "  -74673.69     -32347.69     0.0000000E+00 0.0000000E+00  11651.38     122000\n" +
                            "  -73244.58     -32496.35     0.0000000E+00 0.0000000E+00  10482.81     032000\n" +
                            "  0.0000000E+00 0.0000000E+00  9043.683      3796.876     0.0000000E+00 203000\n" +
                            "  0.0000000E+00 0.0000000E+00  47451.45      19929.68     0.0000000E+00 113000\n" +
                            "  0.0000000E+00 0.0000000E+00  62562.40      26260.53     0.0000000E+00 023000\n" +
                            "  -1855.251     -793.4584     0.0000000E+00 0.0000000E+00 -529.9216     104000\n" +
                            "  -5541.002     -2335.560     0.0000000E+00 0.0000000E+00 -1366.971     014000\n" +
                            "  0.0000000E+00 0.0000000E+00  1913.787      788.2060     0.0000000E+00 005000\n" +
                            "  0.0000000E+00 0.0000000E+00 -2425.252     -1297.141     0.0000000E+00 400100\n" +
                            "  0.0000000E+00 0.0000000E+00 -31496.63     -16639.45     0.0000000E+00 310100\n" +
                            "  0.0000000E+00 0.0000000E+00 -145331.1     -76520.75     0.0000000E+00 220100\n" +
                            "  0.0000000E+00 0.0000000E+00 -287088.1     -151471.2     0.0000000E+00 130100\n" +
                            "  0.0000000E+00 0.0000000E+00 -207185.3     -109810.3     0.0000000E+00 040100\n" +
                            "   4580.009      1007.776     0.0000000E+00 0.0000000E+00  1829.045     301100\n" +
                            "   12260.12     -2328.418     0.0000000E+00 0.0000000E+00  11728.93     211100\n" +
                            "  -24391.18     -30938.28     0.0000000E+00 0.0000000E+00  24217.28     121100\n" +
                            "  -65962.58     -47022.08     0.0000000E+00 0.0000000E+00  15642.60     031100\n" +
                            "  0.0000000E+00 0.0000000E+00  31208.61      13013.49     0.0000000E+00 202100\n" +
                            "  0.0000000E+00 0.0000000E+00  150522.6      62752.65     0.0000000E+00 112100\n" +
                            "  0.0000000E+00 0.0000000E+00  181159.5      75392.20     0.0000000E+00 022100\n" +
                            "  -22014.87     -9741.741     0.0000000E+00 0.0000000E+00 -897.7962     103100\n" +
                            "  -58691.17     -25827.25     0.0000000E+00 0.0000000E+00 -2516.712     013100\n" +
                            "  0.0000000E+00 0.0000000E+00  6631.497      2767.769     0.0000000E+00 004100\n" +
                            "  -276.0776     -47.97149     0.0000000E+00 0.0000000E+00  25.53094     400001\n" +
                            "  -3379.880     -868.6751     0.0000000E+00 0.0000000E+00  380.7799     310001\n" +
                            "  -15326.27     -5130.807     0.0000000E+00 0.0000000E+00  1742.309     220001\n" +
                            "  -29830.87     -12385.97     0.0000000E+00 0.0000000E+00  3315.335     130001\n" +
                            "  -20440.39     -10517.21     0.0000000E+00 0.0000000E+00  2415.679     040001\n" +
                            "  0.0000000E+00 0.0000000E+00  614.5269      87.33514     0.0000000E+00 301001\n" +
                            "  0.0000000E+00 0.0000000E+00  8738.393      2365.966     0.0000000E+00 211001\n" +
                            "  0.0000000E+00 0.0000000E+00  34242.81      11110.04     0.0000000E+00 121001\n" +
                            "  0.0000000E+00 0.0000000E+00  41113.35      14536.01     0.0000000E+00 031001\n" +
                            "   1088.857      339.8656     0.0000000E+00 0.0000000E+00 -212.4226     202001\n" +
                            "   9514.931      3471.324     0.0000000E+00 0.0000000E+00 -887.8367     112001\n" +
                            "   17700.86      6890.544     0.0000000E+00 0.0000000E+00 -726.8422     022001\n" +
                            "  0.0000000E+00 0.0000000E+00 -6651.444     -2521.988     0.0000000E+00 103001\n" +
                            "  0.0000000E+00 0.0000000E+00 -18156.60     -6985.124     0.0000000E+00 013001\n" +
                            "   478.7800      165.1497     0.0000000E+00 0.0000000E+00  242.0812     004001\n" +
                            "   19074.64      6141.200     0.0000000E+00 0.0000000E+00  3182.328     300200\n" +
                            "   86328.98      25299.91     0.0000000E+00 0.0000000E+00  15758.05     210200\n" +
                            "   74874.41      11736.12     0.0000000E+00 0.0000000E+00  20699.19     120200\n" +
                            "  -49336.13     -30899.80     0.0000000E+00 0.0000000E+00  3865.502     030200\n" +
                            "  0.0000000E+00 0.0000000E+00 -61615.66     -25213.59     0.0000000E+00 201200\n" +
                            "  0.0000000E+00 0.0000000E+00 -332219.9     -136032.1     0.0000000E+00 111200\n" +
                            "  0.0000000E+00 0.0000000E+00 -453416.9     -185955.4     0.0000000E+00 021200\n" +
                            "  -78831.05     -34108.76     0.0000000E+00 0.0000000E+00 -9953.955     102200\n" +
                            "  -195831.8     -84266.47     0.0000000E+00 0.0000000E+00 -24521.97     012200\n" +
                            "  0.0000000E+00 0.0000000E+00 -2539.522     -1020.616     0.0000000E+00 003200\n" +
                            "  0.0000000E+00 0.0000000E+00  2212.943      763.1488     0.0000000E+00 300101\n" +
                            "  0.0000000E+00 0.0000000E+00  23531.12      8788.253     0.0000000E+00 210101\n" +
                            "  0.0000000E+00 0.0000000E+00  77566.96      30177.22     0.0000000E+00 120101\n" +
                            "  0.0000000E+00 0.0000000E+00  82259.57      32610.88     0.0000000E+00 030101\n" +
                            "  -111.7304      45.50616     0.0000000E+00 0.0000000E+00 -267.7489     201101\n" +
                            "   17516.38      8099.729     0.0000000E+00 0.0000000E+00  1548.213     111101\n" +
                            "   45387.20      20321.60     0.0000000E+00 0.0000000E+00  6206.575     021101\n" +
                            "  0.0000000E+00 0.0000000E+00 -18476.33     -7971.552     0.0000000E+00 102101\n" +
                            "  0.0000000E+00 0.0000000E+00 -39822.55     -17709.62     0.0000000E+00 012101\n" +
                            "   6211.873      2588.155     0.0000000E+00 0.0000000E+00  544.4840     003101\n" +
                            "   53.85417     -11.72659     0.0000000E+00 0.0000000E+00 0.7377553     300002\n" +
                            "   1083.756      99.69188     0.0000000E+00 0.0000000E+00 -2.066386     210002\n" +
                            "   4944.209      977.5507     0.0000000E+00 0.0000000E+00  63.53728     120002\n" +
                            "   6424.091      1675.841     0.0000000E+00 0.0000000E+00  230.7317     030002\n" +
                            "  0.0000000E+00 0.0000000E+00 -178.5194     0.3764266     0.0000000E+00 201002\n" +
                            "  0.0000000E+00 0.0000000E+00 -1873.092     -357.5739     0.0000000E+00 111002\n" +
                            "  0.0000000E+00 0.0000000E+00 -4006.806     -1075.445     0.0000000E+00 021002\n" +
                            "  -305.8485     -45.78270     0.0000000E+00 0.0000000E+00 -83.01254     102002\n" +
                            "  -1951.510     -533.0044     0.0000000E+00 0.0000000E+00 -318.6548     012002\n" +
                            "  0.0000000E+00 0.0000000E+00  1374.659      476.7002     0.0000000E+00 003002\n" +
                            "  0.0000000E+00 0.0000000E+00 -187219.6     -75757.44     0.0000000E+00 200300\n" +
                            "  0.0000000E+00 0.0000000E+00 -872408.6     -352644.3     0.0000000E+00 110300\n" +
                            "  0.0000000E+00 0.0000000E+00 -1003577.     -405117.0     0.0000000E+00 020300\n" +
                            "  -160723.4     -63411.17     0.0000000E+00 0.0000000E+00 -33943.12     101300\n" +
                            "  -373796.0     -146963.7     0.0000000E+00 0.0000000E+00 -81028.80     011300\n" +
                            "  0.0000000E+00 0.0000000E+00 -35642.84     -13885.37     0.0000000E+00 002300\n" +
                            "  -6962.316     -2600.557     0.0000000E+00 0.0000000E+00  225.9354     200201\n" +
                            "  -10667.33     -2483.547     0.0000000E+00 0.0000000E+00  6170.254     110201\n" +
                            "   11958.94      8241.065     0.0000000E+00 0.0000000E+00  13764.44     020201\n" +
                            "  0.0000000E+00 0.0000000E+00  38373.47      14511.95     0.0000000E+00 101201\n" +
                            "  0.0000000E+00 0.0000000E+00  109589.9      41200.28     0.0000000E+00 011201\n" +
                            "   23142.60      9912.624     0.0000000E+00 0.0000000E+00  4071.508     002201\n" +
                            "  0.0000000E+00 0.0000000E+00 -844.3509     -203.7384     0.0000000E+00 200102\n" +
                            "  0.0000000E+00 0.0000000E+00 -6128.843     -1911.783     0.0000000E+00 110102\n" +
                            "  0.0000000E+00 0.0000000E+00 -10417.01     -3630.138     0.0000000E+00 020102\n" +
                            "  -2173.862     -559.9920     0.0000000E+00 0.0000000E+00 -515.9284     101102\n" +
                            "  -9768.056     -3158.282     0.0000000E+00 0.0000000E+00 -1782.661     011102\n" +
                            "  0.0000000E+00 0.0000000E+00  3713.912      1591.388     0.0000000E+00 002102\n" +
                            "   7.126510      3.939395     0.0000000E+00 0.0000000E+00  1.730769     200003\n" +
                            "   19.40026      21.82684     0.0000000E+00 0.0000000E+00  13.31566     110003\n" +
                            "  -33.48483      20.66149     0.0000000E+00 0.0000000E+00  25.35082     020003\n" +
                            "  0.0000000E+00 0.0000000E+00 -7.064431     -2.594406     0.0000000E+00 101003\n" +
                            "  0.0000000E+00 0.0000000E+00 -1.693728     -5.896173     0.0000000E+00 011003\n" +
                            "   46.44968      6.834231     0.0000000E+00 0.0000000E+00  35.56760     002003\n" +
                            "  -103578.8     -43338.09     0.0000000E+00 0.0000000E+00 -4135.184     100400\n" +
                            "  -226123.3     -94154.08     0.0000000E+00 0.0000000E+00 -7689.462     010400\n" +
                            "  0.0000000E+00 0.0000000E+00  63980.84      26925.28     0.0000000E+00 001400\n" +
                            "  0.0000000E+00 0.0000000E+00  80341.19      35898.73     0.0000000E+00 100301\n" +
                            "  0.0000000E+00 0.0000000E+00  182378.4      80999.25     0.0000000E+00 010301\n" +
                            "   33651.31      14832.27     0.0000000E+00 0.0000000E+00  5830.185     001301\n" +
                            "  -3267.842     -928.5028     0.0000000E+00 0.0000000E+00 -599.5259     100202\n" +
                            "  -11019.12     -3638.376     0.0000000E+00 0.0000000E+00 -1735.884     010202\n" +
                            "  0.0000000E+00 0.0000000E+00 -4374.631     -1437.699     0.0000000E+00 001202\n" +
                            "  0.0000000E+00 0.0000000E+00  11.88049      12.71833     0.0000000E+00 100103\n" +
                            "  0.0000000E+00 0.0000000E+00  122.8817      52.65225     0.0000000E+00 010103\n" +
                            "   478.5885      143.0177     0.0000000E+00 0.0000000E+00  123.8432     001103\n" +
                            "   1.404921     0.3212047     0.0000000E+00 0.0000000E+00-0.5060332E-01 100004\n" +
                            "  0.7081767     0.9192264     0.0000000E+00 0.0000000E+00 0.2450951     010004\n" +
                            "  0.0000000E+00 0.0000000E+00  20.10894      4.075557     0.0000000E+00 001004\n" +
                            "  0.0000000E+00 0.0000000E+00  129063.2      54297.10     0.0000000E+00 000500\n" +
                            "   12256.62      7105.079     0.0000000E+00 0.0000000E+00 -2680.744     000401\n" +
                            "  0.0000000E+00 0.0000000E+00 -3843.269     -2042.838     0.0000000E+00 000302\n" +
                            "   606.1394      246.0089     0.0000000E+00 0.0000000E+00  60.72829     000203\n" +
                            "  0.0000000E+00 0.0000000E+00  65.39807      18.96649     0.0000000E+00 000104\n" +
                            " -0.1176634     0.7301057E-03 0.0000000E+00 0.0000000E+00 0.8780834     000005"
            );
            slice_后偏转段CCT2入口.map = CosyArbitraryOrder.readMap(
                    " -0.9548833     -2.268299     0.0000000E+00 0.0000000E+00-0.6158582     100000\n" +
                            "  -2.372421     -6.682870     0.0000000E+00 0.0000000E+00 -1.791893     010000\n" +
                            "  0.0000000E+00 0.0000000E+00-0.3344774    -0.8177879     0.0000000E+00 001000\n" +
                            "  0.0000000E+00 0.0000000E+00  1.494161     0.6634435     0.0000000E+00 000100\n" +
                            "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00  1.000000     000010\n" +
                            "  0.2499738    -0.5115048E-01 0.0000000E+00 0.0000000E+00 0.7159973     000001\n" +
                            "  -3.394786     -4.249518     0.0000000E+00 0.0000000E+00 -1.683203     200000\n" +
                            "  -21.99770     -25.85324     0.0000000E+00 0.0000000E+00 -9.510588     110000\n" +
                            "  -35.82353     -40.23737     0.0000000E+00 0.0000000E+00 -14.40575     020000\n" +
                            "  0.0000000E+00 0.0000000E+00 -6.901176     -7.035772     0.0000000E+00 101000\n" +
                            "  0.0000000E+00 0.0000000E+00 -17.11853     -17.15625     0.0000000E+00 011000\n" +
                            " -0.4102407E-01  1.625477     0.0000000E+00 0.0000000E+00-0.2359793E-01 002000\n" +
                            "  0.0000000E+00 0.0000000E+00 -12.97929     -13.99279     0.0000000E+00 100100\n" +
                            "  0.0000000E+00 0.0000000E+00 -29.40331     -29.20569     0.0000000E+00 010100\n" +
                            "  0.4451384      7.271823     0.0000000E+00 0.0000000E+00 0.6560471     001100\n" +
                            " -0.2863030     -1.616951     0.0000000E+00 0.0000000E+00 0.1448662     100001\n" +
                            "  0.2416052     -1.439684     0.0000000E+00 0.0000000E+00  1.336233     010001\n" +
                            "  0.0000000E+00 0.0000000E+00  1.674667      1.536337     0.0000000E+00 001001\n" +
                            "   3.065243      13.72012     0.0000000E+00 0.0000000E+00-0.9181370E-01 000200\n" +
                            "  0.0000000E+00 0.0000000E+00  4.230414      6.801936     0.0000000E+00 000101\n" +
                            " -0.1352223    -0.2604661E-01 0.0000000E+00 0.0000000E+00-0.6100946     000002\n" +
                            "  -15.78817     -6.989118     0.0000000E+00 0.0000000E+00 -4.946630     300000\n" +
                            "  -140.4321     -66.16647     0.0000000E+00 0.0000000E+00 -46.80274     210000\n" +
                            "  -416.0898     -202.7187     0.0000000E+00 0.0000000E+00 -146.5461     120000\n" +
                            "  -412.4895     -205.6999     0.0000000E+00 0.0000000E+00 -153.0810     030000\n" +
                            "  0.0000000E+00 0.0000000E+00 -3.340322      2.270087     0.0000000E+00 201000\n" +
                            "  0.0000000E+00 0.0000000E+00 -31.90811     -9.034200     0.0000000E+00 111000\n" +
                            "  0.0000000E+00 0.0000000E+00 -62.09644     -39.62098     0.0000000E+00 021000\n" +
                            "   4.436939      2.272859     0.0000000E+00 0.0000000E+00 -1.972708     102000\n" +
                            "   16.12063      10.74826     0.0000000E+00 0.0000000E+00 -3.927866     012000\n" +
                            "  0.0000000E+00 0.0000000E+00 -9.523363     -11.18416     0.0000000E+00 003000\n" +
                            "  0.0000000E+00 0.0000000E+00 0.3059597    -0.3300899     0.0000000E+00 200100\n" +
                            "  0.0000000E+00 0.0000000E+00 -30.36889     -62.68675     0.0000000E+00 110100\n" +
                            "  0.0000000E+00 0.0000000E+00 -84.79067     -166.9174     0.0000000E+00 020100\n" +
                            "   10.66544     -28.65850     0.0000000E+00 0.0000000E+00 -2.428567     101100\n" +
                            "   50.18045     -49.87294     0.0000000E+00 0.0000000E+00-0.8113534     011100\n" +
                            "  0.0000000E+00 0.0000000E+00 -72.10311     -89.18579     0.0000000E+00 002100\n" +
                            "  -1.609742     -2.389980     0.0000000E+00 0.0000000E+00-0.4304688E-03 200001\n" +
                            "   2.995856     0.6615683     0.0000000E+00 0.0000000E+00  4.857482     110001\n" +
                            "   26.71902      25.06509     0.0000000E+00 0.0000000E+00  15.19329     020001\n" +
                            "  0.0000000E+00 0.0000000E+00  1.893786     -5.212440     0.0000000E+00 101001\n" +
                            "  0.0000000E+00 0.0000000E+00  16.10756     0.1733246     0.0000000E+00 011001\n" +
                            "   1.015241     0.7488878     0.0000000E+00 0.0000000E+00 0.8526016     002001\n" +
                            "   4.428298     -59.02423     0.0000000E+00 0.0000000E+00  4.741711     100200\n" +
                            "   39.47407     -117.7632     0.0000000E+00 0.0000000E+00  15.81730     010200\n" +
                            "  0.0000000E+00 0.0000000E+00 -185.5085     -231.3331     0.0000000E+00 001200\n" +
                            "  0.0000000E+00 0.0000000E+00  14.89680      1.665979     0.0000000E+00 100101\n" +
                            "  0.0000000E+00 0.0000000E+00  57.35462      29.10171     0.0000000E+00 010101\n" +
                            "   7.110278      6.947002     0.0000000E+00 0.0000000E+00  2.204077     001101\n" +
                            "  0.3120710     0.4343965     0.0000000E+00 0.0000000E+00-0.6389737E-01 100002\n" +
                            "  0.7910813      1.615172     0.0000000E+00 0.0000000E+00 -1.030200     010002\n" +
                            "  0.0000000E+00 0.0000000E+00 -1.100958     0.4629822     0.0000000E+00 001002\n" +
                            "  0.0000000E+00 0.0000000E+00 -158.2765     -197.4229     0.0000000E+00 000300\n" +
                            "   8.417427      9.712119     0.0000000E+00 0.0000000E+00 0.8634180     000201\n" +
                            "  0.0000000E+00 0.0000000E+00 -5.704335     -3.574209     0.0000000E+00 000102\n" +
                            "  0.1141859     0.5801571E-01 0.0000000E+00 0.0000000E+00 0.5285943     000003\n" +
                            "  -35.50821     -1.421237     0.0000000E+00 0.0000000E+00 -19.33469     400000\n" +
                            "  -437.8425     -40.27759     0.0000000E+00 0.0000000E+00 -239.2198     310000\n" +
                            "  -2027.185     -283.0329     0.0000000E+00 0.0000000E+00 -1107.186     220000\n" +
                            "  -4183.668     -775.5638     0.0000000E+00 0.0000000E+00 -2278.107     130000\n" +
                            "  -3255.882     -753.9114     0.0000000E+00 0.0000000E+00 -1762.743     040000\n" +
                            "  0.0000000E+00 0.0000000E+00 -26.53974      19.61823     0.0000000E+00 301000\n" +
                            "  0.0000000E+00 0.0000000E+00 -240.7770      174.6104     0.0000000E+00 211000\n" +
                            "  0.0000000E+00 0.0000000E+00 -740.7764      487.3691     0.0000000E+00 121000\n" +
                            "  0.0000000E+00 0.0000000E+00 -779.7428      413.2141     0.0000000E+00 031000\n" +
                            "   20.58074      55.60235     0.0000000E+00 0.0000000E+00 -2.679025     202000\n" +
                            "   102.9800      263.7427     0.0000000E+00 0.0000000E+00 -10.79601     112000\n" +
                            "   132.8477      310.0588     0.0000000E+00 0.0000000E+00 -8.006220     022000\n" +
                            "  0.0000000E+00 0.0000000E+00 -3.972086      12.32294     0.0000000E+00 103000\n" +
                            "  0.0000000E+00 0.0000000E+00 -22.41829      7.775804     0.0000000E+00 013000\n" +
                            "  -6.672466     -2.099936     0.0000000E+00 0.0000000E+00 -4.932736     004000\n" +
                            "  0.0000000E+00 0.0000000E+00 -63.41644     -54.25293     0.0000000E+00 300100\n" +
                            "  0.0000000E+00 0.0000000E+00 -562.6712     -470.6135     0.0000000E+00 210100\n" +
                            "  0.0000000E+00 0.0000000E+00 -1707.566     -1458.206     0.0000000E+00 120100\n" +
                            "  0.0000000E+00 0.0000000E+00 -1779.154     -1609.742     0.0000000E+00 030100\n" +
                            "   78.44210      150.9510     0.0000000E+00 0.0000000E+00 -17.10733     201100\n" +
                            "   371.5785      592.8732     0.0000000E+00 0.0000000E+00 -54.68647     111100\n" +
                            "   455.7657      518.0595     0.0000000E+00 0.0000000E+00 -16.95424     021100\n" +
                            "  0.0000000E+00 0.0000000E+00 -10.01720      93.64703     0.0000000E+00 102100\n" +
                            "  0.0000000E+00 0.0000000E+00 -120.2049      76.62821     0.0000000E+00 012100\n" +
                            "  -77.83906     -60.91976     0.0000000E+00 0.0000000E+00 -38.89469     003100\n" +
                            "  -1.153911      1.946574     0.0000000E+00 0.0000000E+00 -1.590871     300001\n" +
                            "   64.62493      50.01761     0.0000000E+00 0.0000000E+00  12.39832     210001\n" +
                            "   412.1378      251.6055     0.0000000E+00 0.0000000E+00  123.1066     120001\n" +
                            "   626.0796      353.2484     0.0000000E+00 0.0000000E+00  215.8418     030001\n" +
                            "  0.0000000E+00 0.0000000E+00 -3.435237     -15.92397     0.0000000E+00 201001\n" +
                            "  0.0000000E+00 0.0000000E+00  2.544831     -94.44182     0.0000000E+00 111001\n" +
                            "  0.0000000E+00 0.0000000E+00  53.35600     -111.1664     0.0000000E+00 021001\n" +
                            "  -9.113781     -28.78358     0.0000000E+00 0.0000000E+00  6.340639     102001\n" +
                            "  -33.59069     -80.75205     0.0000000E+00 0.0000000E+00  18.35618     012001\n" +
                            "  0.0000000E+00 0.0000000E+00  6.992412     -4.824542     0.0000000E+00 003001\n" +
                            "   95.58016      129.6644     0.0000000E+00 0.0000000E+00 -22.55248     200200\n" +
                            "   429.6894      404.2102     0.0000000E+00 0.0000000E+00 -53.28973     110200\n" +
                            "   488.7778      175.0639     0.0000000E+00 0.0000000E+00  24.97255     020200\n" +
                            "  0.0000000E+00 0.0000000E+00 -11.12042      254.8150     0.0000000E+00 101200\n" +
                            "  0.0000000E+00 0.0000000E+00 -288.4137      264.9092     0.0000000E+00 011200\n" +
                            "  -332.2961     -379.4905     0.0000000E+00 0.0000000E+00 -108.5308     002200\n" +
                            "  0.0000000E+00 0.0000000E+00  1.544128     -34.11343     0.0000000E+00 200101\n" +
                            "  0.0000000E+00 0.0000000E+00  57.10979     -148.2858     0.0000000E+00 110101\n" +
                            "  0.0000000E+00 0.0000000E+00  196.7806     -63.16566     0.0000000E+00 020101\n" +
                            "  -39.45570     -98.99383     0.0000000E+00 0.0000000E+00  33.51650     101101\n" +
                            "  -144.6396     -232.5568     0.0000000E+00 0.0000000E+00  79.98723     011101\n" +
                            "  0.0000000E+00 0.0000000E+00  88.30289      21.51634     0.0000000E+00 002101\n" +
                            "   2.292754      1.291956     0.0000000E+00 0.0000000E+00 0.3364815     200002\n" +
                            "   8.910092      3.545944     0.0000000E+00 0.0000000E+00 -1.360010     110002\n" +
                            "  -8.181836     -15.57127     0.0000000E+00 0.0000000E+00 -11.90531     020002\n" +
                            "  0.0000000E+00 0.0000000E+00  1.791404      7.270804     0.0000000E+00 101002\n" +
                            "  0.0000000E+00 0.0000000E+00 -7.729082      14.48767     0.0000000E+00 011002\n" +
                            " -0.4595611      1.824312     0.0000000E+00 0.0000000E+00 -1.753237     002002\n" +
                            "  0.0000000E+00 0.0000000E+00 -30.58706      227.2673     0.0000000E+00 100300\n" +
                            "  0.0000000E+00 0.0000000E+00 -306.8253      271.3416     0.0000000E+00 010300\n" +
                            "  -628.6086     -900.2245     0.0000000E+00 0.0000000E+00 -117.1077     001300\n" +
                            "  -35.00859     -24.92344     0.0000000E+00 0.0000000E+00  31.23422     100201\n" +
                            "  -144.3706     -8.872540     0.0000000E+00 0.0000000E+00  58.50705     010201\n" +
                            "  0.0000000E+00 0.0000000E+00  321.0479      204.9161     0.0000000E+00 001201\n" +
                            "  0.0000000E+00 0.0000000E+00 -7.532062      14.79110     0.0000000E+00 100102\n" +
                            "  0.0000000E+00 0.0000000E+00 -59.42241      9.468758     0.0000000E+00 010102\n" +
                            "  -8.806460      1.463094     0.0000000E+00 0.0000000E+00 -9.106043     001102\n" +
                            " -0.2255067    -0.2642939     0.0000000E+00 0.0000000E+00 0.2925322E-01 100003\n" +
                            "  -1.176319     -1.597843     0.0000000E+00 0.0000000E+00 0.7985079     010003\n" +
                            "  0.0000000E+00 0.0000000E+00 0.6712408     -1.082852     0.0000000E+00 001003\n" +
                            "  -440.3927     -740.7289     0.0000000E+00 0.0000000E+00 -35.86602     000400\n" +
                            "  0.0000000E+00 0.0000000E+00  368.5107      303.0788     0.0000000E+00 000301\n" +
                            "  -19.25785     -13.64143     0.0000000E+00 0.0000000E+00 -9.432167     000202\n" +
                            "  0.0000000E+00 0.0000000E+00  5.805994     0.4253504     0.0000000E+00 000103\n" +
                            " -0.1001642    -0.5148865E-01 0.0000000E+00 0.0000000E+00-0.4738670     000004\n" +
                            "  -93.49235    -0.2905301     0.0000000E+00 0.0000000E+00 -50.97849     500000\n" +
                            "  -1589.407     -205.3398     0.0000000E+00 0.0000000E+00 -828.4219     410000\n" +
                            "  -10560.68     -2273.124     0.0000000E+00 0.0000000E+00 -5323.713     320000\n" +
                            "  -34560.03     -9551.392     0.0000000E+00 0.0000000E+00 -16978.04     230000\n" +
                            "  -56008.04     -17987.70     0.0000000E+00 0.0000000E+00 -26955.17     140000\n" +
                            "  -36101.70     -12811.37     0.0000000E+00 0.0000000E+00 -17089.93     050000\n" +
                            "  0.0000000E+00 0.0000000E+00  6.382250      298.4630     0.0000000E+00 401000\n" +
                            "  0.0000000E+00 0.0000000E+00 -67.42065      3327.507     0.0000000E+00 311000\n" +
                            "  0.0000000E+00 0.0000000E+00 -883.1078      13940.34     0.0000000E+00 221000\n" +
                            "  0.0000000E+00 0.0000000E+00 -2826.595      25992.57     0.0000000E+00 131000\n" +
                            "  0.0000000E+00 0.0000000E+00 -2884.912      18151.93     0.0000000E+00 041000\n" +
                            " -0.1443058      65.64425     0.0000000E+00 0.0000000E+00  19.62606     302000\n" +
                            "   94.14648      716.7646     0.0000000E+00 0.0000000E+00  129.7901     212000\n" +
                            "   536.7031      2445.122     0.0000000E+00 0.0000000E+00  291.4696     122000\n" +
                            "   780.3914      2679.948     0.0000000E+00 0.0000000E+00  229.4223     032000\n" +
                            "  0.0000000E+00 0.0000000E+00 -83.30547     -79.26988     0.0000000E+00 203000\n" +
                            "  0.0000000E+00 0.0000000E+00 -436.5451     -386.6606     0.0000000E+00 113000\n" +
                            "  0.0000000E+00 0.0000000E+00 -574.8426     -451.8513     0.0000000E+00 023000\n" +
                            "   51.18386      169.8226     0.0000000E+00 0.0000000E+00 -31.16273     104000\n" +
                            "   122.4966      437.1724     0.0000000E+00 0.0000000E+00 -86.51827     014000\n" +
                            "  0.0000000E+00 0.0000000E+00 0.3966351      21.12803     0.0000000E+00 005000\n" +
                            "  0.0000000E+00 0.0000000E+00 -20.79732      286.7274     0.0000000E+00 400100\n" +
                            "  0.0000000E+00 0.0000000E+00 -507.5293      2921.375     0.0000000E+00 310100\n" +
                            "  0.0000000E+00 0.0000000E+00 -3332.677      10995.30     0.0000000E+00 220100\n" +
                            "  0.0000000E+00 0.0000000E+00 -8603.217      17935.07     0.0000000E+00 130100\n" +
                            "  0.0000000E+00 0.0000000E+00 -7873.878      10422.31     0.0000000E+00 040100\n" +
                            "  -159.0596     -134.1863     0.0000000E+00 0.0000000E+00  64.43591     301100\n" +
                            "  -839.8031     -577.9290     0.0000000E+00 0.0000000E+00  374.3009     211100\n" +
                            "  -944.5157     -260.6174     0.0000000E+00 0.0000000E+00  734.1865     121100\n" +
                            "   525.7560      869.3473     0.0000000E+00 0.0000000E+00  549.6118     031100\n" +
                            "  0.0000000E+00 0.0000000E+00 -585.8068     -594.4051     0.0000000E+00 202100\n" +
                            "  0.0000000E+00 0.0000000E+00 -3037.296     -2814.687     0.0000000E+00 112100\n" +
                            "  0.0000000E+00 0.0000000E+00 -4012.142     -3208.768     0.0000000E+00 022100\n" +
                            "   464.1864      1520.283     0.0000000E+00 0.0000000E+00 -299.9535     103100\n" +
                            "   1082.407      3838.468     0.0000000E+00 0.0000000E+00 -813.2222     013100\n" +
                            "  0.0000000E+00 0.0000000E+00  38.75964      214.3092     0.0000000E+00 004100\n" +
                            "  -22.29458      3.422439     0.0000000E+00 0.0000000E+00 -5.588584     400001\n" +
                            "  -36.49056      58.58569     0.0000000E+00 0.0000000E+00  66.38990     310001\n" +
                            "   946.1669      451.3039     0.0000000E+00 0.0000000E+00  929.4278     220001\n" +
                            "   4270.615      1496.703     0.0000000E+00 0.0000000E+00  3185.390     130001\n" +
                            "   5140.197      1758.136     0.0000000E+00 0.0000000E+00  3446.825     040001\n" +
                            "  0.0000000E+00 0.0000000E+00 -17.23159     -82.07452     0.0000000E+00 301001\n" +
                            "  0.0000000E+00 0.0000000E+00 -13.10251     -850.1703     0.0000000E+00 211001\n" +
                            "  0.0000000E+00 0.0000000E+00  404.6627     -2854.353     0.0000000E+00 121001\n" +
                            "  0.0000000E+00 0.0000000E+00  908.1276     -3065.079     0.0000000E+00 031001\n" +
                            "  -19.25491     -84.63438     0.0000000E+00 0.0000000E+00 -5.410492     202001\n" +
                            "  -150.6796     -620.6952     0.0000000E+00 0.0000000E+00 -2.906986     112001\n" +
                            "  -256.6208     -1005.724     0.0000000E+00 0.0000000E+00  24.15812     022001\n" +
                            "  0.0000000E+00 0.0000000E+00  13.60430     -12.84178     0.0000000E+00 103001\n" +
                            "  0.0000000E+00 0.0000000E+00  63.17750     -6.985633     0.0000000E+00 013001\n" +
                            "  -2.427350     -26.14973     0.0000000E+00 0.0000000E+00  13.45218     004001\n" +
                            "  -255.5907     -410.2937     0.0000000E+00 0.0000000E+00  108.7028     300200\n" +
                            "  -1523.483     -2943.634     0.0000000E+00 0.0000000E+00  708.4372     210200\n" +
                            "  -2530.221     -7136.899     0.0000000E+00 0.0000000E+00  1631.791     120200\n" +
                            "  -667.6018     -5895.380     0.0000000E+00 0.0000000E+00  1429.769     030200\n" +
                            "  0.0000000E+00 0.0000000E+00 -1326.715     -1384.458     0.0000000E+00 201200\n" +
                            "  0.0000000E+00 0.0000000E+00 -6957.692     -6319.844     0.0000000E+00 111200\n" +
                            "  0.0000000E+00 0.0000000E+00 -9449.010     -6966.024     0.0000000E+00 021200\n" +
                            "   1635.422      5148.778     0.0000000E+00 0.0000000E+00 -1124.612     102200\n" +
                            "   3717.542      12646.85     0.0000000E+00 0.0000000E+00 -2974.916     012200\n" +
                            "  0.0000000E+00 0.0000000E+00  307.7054      836.0456     0.0000000E+00 003200\n" +
                            "  0.0000000E+00 0.0000000E+00  39.06680     -192.9318     0.0000000E+00 300101\n" +
                            "  0.0000000E+00 0.0000000E+00  674.8080     -1520.811     0.0000000E+00 210101\n" +
                            "  0.0000000E+00 0.0000000E+00  3044.386     -3839.833     0.0000000E+00 120101\n" +
                            "  0.0000000E+00 0.0000000E+00  4228.407     -2847.172     0.0000000E+00 030101\n" +
                            "  -83.48515     -459.8690     0.0000000E+00 0.0000000E+00  16.34167     201101\n" +
                            "  -562.9950     -2720.924     0.0000000E+00 0.0000000E+00  198.8091     111101\n" +
                            "  -857.9659     -3659.555     0.0000000E+00 0.0000000E+00  354.4722     021101\n" +
                            "  0.0000000E+00 0.0000000E+00  74.63673     -107.7043     0.0000000E+00 102101\n" +
                            "  0.0000000E+00 0.0000000E+00  409.9249     -18.58547     0.0000000E+00 012101\n" +
                            "   37.20169     -194.4855     0.0000000E+00 0.0000000E+00  146.9865     003101\n" +
                            "   3.983749     0.4874336     0.0000000E+00 0.0000000E+00  3.432941     300002\n" +
                            "  -13.78749     -32.83552     0.0000000E+00 0.0000000E+00  17.52397     210002\n" +
                            "  -302.6516     -260.5034     0.0000000E+00 0.0000000E+00 -37.45996     120002\n" +
                            "  -672.4778     -469.5051     0.0000000E+00 0.0000000E+00 -183.0389     030002\n" +
                            "  0.0000000E+00 0.0000000E+00  9.870614      6.873524     0.0000000E+00 201002\n" +
                            "  0.0000000E+00 0.0000000E+00  51.34404      100.3704     0.0000000E+00 111002\n" +
                            "  0.0000000E+00 0.0000000E+00  27.40687      213.1947     0.0000000E+00 021002\n" +
                            "   14.31378      36.68805     0.0000000E+00 0.0000000E+00 -4.751059     102002\n" +
                            "   58.43373      144.7144     0.0000000E+00 0.0000000E+00 -24.27522     012002\n" +
                            "  0.0000000E+00 0.0000000E+00 -7.323827      11.34721     0.0000000E+00 003002\n" +
                            "  0.0000000E+00 0.0000000E+00 -1016.911     -1055.266     0.0000000E+00 200300\n" +
                            "  0.0000000E+00 0.0000000E+00 -5566.577     -4709.524     0.0000000E+00 110300\n" +
                            "  0.0000000E+00 0.0000000E+00 -7996.872     -5165.889     0.0000000E+00 020300\n" +
                            "   2549.083      7689.827     0.0000000E+00 0.0000000E+00 -1876.934     101300\n" +
                            "   5617.683      18111.36     0.0000000E+00 0.0000000E+00 -4831.208     011300\n" +
                            "  0.0000000E+00 0.0000000E+00  879.4369      1477.124     0.0000000E+00 002300\n" +
                            "  -106.1319     -471.5818     0.0000000E+00 0.0000000E+00  56.65368     200201\n" +
                            "  -630.5918     -2377.960     0.0000000E+00 0.0000000E+00  375.1770     110201\n" +
                            "  -833.5963     -2520.531     0.0000000E+00 0.0000000E+00  491.9897     020201\n" +
                            "  0.0000000E+00 0.0000000E+00  41.80642     -360.1149     0.0000000E+00 101201\n" +
                            "  0.0000000E+00 0.0000000E+00  795.5259     -151.8006     0.0000000E+00 011201\n" +
                            "   397.0849     -352.5835     0.0000000E+00 0.0000000E+00  565.9173     002201\n" +
                            "  0.0000000E+00 0.0000000E+00  18.56007      48.13927     0.0000000E+00 200102\n" +
                            "  0.0000000E+00 0.0000000E+00  51.18643      366.0601     0.0000000E+00 110102\n" +
                            "  0.0000000E+00 0.0000000E+00 -115.7439      532.2362     0.0000000E+00 020102\n" +
                            "   83.62203      235.6845     0.0000000E+00 0.0000000E+00 -51.44771     101102\n" +
                            "   313.1997      754.8460     0.0000000E+00 0.0000000E+00 -177.3761     011102\n" +
                            "  0.0000000E+00 0.0000000E+00 -106.8234      52.13054     0.0000000E+00 002102\n" +
                            "  -2.283277     -2.005019     0.0000000E+00 0.0000000E+00-0.3898580     200003\n" +
                            "  -16.07157     -11.44773     0.0000000E+00 0.0000000E+00-0.6535741     110003\n" +
                            "  -11.17337    -0.5110447     0.0000000E+00 0.0000000E+00  8.264012     020003\n" +
                            "  0.0000000E+00 0.0000000E+00 -3.472969     -5.667968     0.0000000E+00 101003\n" +
                            "  0.0000000E+00 0.0000000E+00-0.3146872     -19.07482     0.0000000E+00 011003\n" +
                            " -0.4728457     -3.660757     0.0000000E+00 0.0000000E+00  2.323663     002003\n" +
                            "   1408.796      4177.461     0.0000000E+00 0.0000000E+00 -1142.876     100400\n" +
                            "   2972.130      9222.675     0.0000000E+00 0.0000000E+00 -2864.760     010400\n" +
                            "  0.0000000E+00 0.0000000E+00  949.8824      971.4891     0.0000000E+00 001400\n" +
                            "  0.0000000E+00 0.0000000E+00 -53.28581     -423.2025     0.0000000E+00 100301\n" +
                            "  0.0000000E+00 0.0000000E+00  663.6348     -319.0253     0.0000000E+00 010301\n" +
                            "   1177.303      302.4455     0.0000000E+00 0.0000000E+00  900.5806     001301\n" +
                            "   98.91498      251.0116     0.0000000E+00 0.0000000E+00 -84.08238     100202\n" +
                            "   368.5016      664.2543     0.0000000E+00 0.0000000E+00 -235.4631     010202\n" +
                            "  0.0000000E+00 0.0000000E+00 -442.6441     -43.74053     0.0000000E+00 001202\n" +
                            "  0.0000000E+00 0.0000000E+00-0.5235688     -21.70953     0.0000000E+00 100103\n" +
                            "  0.0000000E+00 0.0000000E+00  46.03499     -45.04216     0.0000000E+00 010103\n" +
                            "   5.892405     -15.42859     0.0000000E+00 0.0000000E+00  16.88902     001103\n" +
                            "  0.1384932     0.1741222     0.0000000E+00 0.0000000E+00 0.3079557E-02 100004\n" +
                            "   1.233491      1.455486     0.0000000E+00 0.0000000E+00-0.5531957     010004\n" +
                            "  0.0000000E+00 0.0000000E+00-0.3456455      1.420321     0.0000000E+00 001004\n" +
                            "  0.0000000E+00 0.0000000E+00  235.2628     -42.89843     0.0000000E+00 000500\n" +
                            "   1138.167      958.8725     0.0000000E+00 0.0000000E+00  484.4593     000401\n" +
                            "  0.0000000E+00 0.0000000E+00 -583.0950     -263.0701     0.0000000E+00 000302\n" +
                            "   24.50969     0.6127764     0.0000000E+00 0.0000000E+00  24.68007     000203\n" +
                            "  0.0000000E+00 0.0000000E+00 -5.356865      2.340325     0.0000000E+00 000104\n" +
                            "  0.8189644E-01 0.2894607E-01 0.0000000E+00 0.0000000E+00 0.4375355     000005"
            );
            optics_后偏转段CCT2入口.map = CosyArbitraryOrder.readMap(
                    " -0.9427251     -2.227984     0.0000000E+00 0.0000000E+00-0.6086743     100000\n" +
                            "  -2.330954     -6.569601     0.0000000E+00 0.0000000E+00 -1.772100     010000\n" +
                            "  0.0000000E+00 0.0000000E+00-0.3845997    -0.8778538     0.0000000E+00 001000\n" +
                            "  0.0000000E+00 0.0000000E+00  1.347393     0.4753356     0.0000000E+00 000100\n" +
                            "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00  1.000000     000010\n" +
                            "  0.2518111    -0.5053771E-01 0.0000000E+00 0.0000000E+00 0.7158566     000001\n" +
                            "  -1.884556     -1.558832     0.0000000E+00 0.0000000E+00 -1.180522     200000\n" +
                            "  -13.30630     -12.89005     0.0000000E+00 0.0000000E+00 -7.325567     110000\n" +
                            "  -23.21806     -24.44400     0.0000000E+00 0.0000000E+00 -12.04513     020000\n" +
                            "  0.0000000E+00 0.0000000E+00 -7.077117     -7.746385     0.0000000E+00 101000\n" +
                            "  0.0000000E+00 0.0000000E+00 -20.21803     -22.03748     0.0000000E+00 011000\n" +
                            " -0.6024075     0.2912347     0.0000000E+00 0.0000000E+00-0.3638021     002000\n" +
                            "  0.0000000E+00 0.0000000E+00 -16.29140     -18.79369     0.0000000E+00 100100\n" +
                            "  0.0000000E+00 0.0000000E+00 -43.85812     -47.88932     0.0000000E+00 010100\n" +
                            "  -2.444744      1.725390     0.0000000E+00 0.0000000E+00-0.6481222     001100\n" +
                            " -0.1744529E-01 -1.510904     0.0000000E+00 0.0000000E+00 0.1349768     100001\n" +
                            "  0.9505497     -1.367769     0.0000000E+00 0.0000000E+00  1.296588     010001\n" +
                            "  0.0000000E+00 0.0000000E+00  1.464801      1.228532     0.0000000E+00 001001\n" +
                            " -0.1009022      9.084826     0.0000000E+00 0.0000000E+00 -1.228065     000200\n" +
                            "  0.0000000E+00 0.0000000E+00  3.992579      6.619500     0.0000000E+00 000101\n" +
                            " -0.1491056    -0.7854294E-01 0.0000000E+00 0.0000000E+00-0.6104887     000002\n" +
                            "  -5.722389     0.7584133     0.0000000E+00 0.0000000E+00 -2.138077     300000\n" +
                            "  -55.92805     0.6127313     0.0000000E+00 0.0000000E+00 -23.94639     210000\n" +
                            "  -179.3828     -11.73467     0.0000000E+00 0.0000000E+00 -84.88398     120000\n" +
                            "  -189.4553     -22.35895     0.0000000E+00 0.0000000E+00 -97.63358     030000\n" +
                            "  0.0000000E+00 0.0000000E+00 -10.53885     -7.460795     0.0000000E+00 201000\n" +
                            "  0.0000000E+00 0.0000000E+00 -75.06109     -66.42130     0.0000000E+00 111000\n" +
                            "  0.0000000E+00 0.0000000E+00 -127.8741     -128.3391     0.0000000E+00 021000\n" +
                            " -0.4130700      2.637074     0.0000000E+00 0.0000000E+00 -3.585086     102000\n" +
                            "  0.3471350      9.524759     0.0000000E+00 0.0000000E+00 -9.964424     012000\n" +
                            "  0.0000000E+00 0.0000000E+00 -5.026860     -5.426337     0.0000000E+00 003000\n" +
                            "  0.0000000E+00 0.0000000E+00 -27.71520     -32.45211     0.0000000E+00 200100\n" +
                            "  0.0000000E+00 0.0000000E+00 -192.0149     -246.0177     0.0000000E+00 110100\n" +
                            "  0.0000000E+00 0.0000000E+00 -322.6767     -440.5044     0.0000000E+00 020100\n" +
                            "  -12.68303     -27.03939     0.0000000E+00 0.0000000E+00 -9.453207     101100\n" +
                            "  -28.28383     -68.41288     0.0000000E+00 0.0000000E+00 -24.33833     011100\n" +
                            "  0.0000000E+00 0.0000000E+00 -35.73755     -44.88106     0.0000000E+00 002100\n" +
                            "  0.1976971    -0.4107879E-01 0.0000000E+00 0.0000000E+00-0.1058600     200001\n" +
                            "   9.418630      6.825501     0.0000000E+00 0.0000000E+00  3.404728     110001\n" +
                            "   30.57995      27.28339     0.0000000E+00 0.0000000E+00  12.60522     020001\n" +
                            "  0.0000000E+00 0.0000000E+00  1.802156     -6.218187     0.0000000E+00 101001\n" +
                            "  0.0000000E+00 0.0000000E+00  17.13441     -3.076009     0.0000000E+00 011001\n" +
                            "  0.9823957     0.6501935     0.0000000E+00 0.0000000E+00  1.035922     002001\n" +
                            "  -28.14703     -70.76899     0.0000000E+00 0.0000000E+00 0.8163895     100200\n" +
                            "  -68.80533     -188.1924     0.0000000E+00 0.0000000E+00  3.265216     010200\n" +
                            "  0.0000000E+00 0.0000000E+00 -90.72253     -122.3162     0.0000000E+00 001200\n" +
                            "  0.0000000E+00 0.0000000E+00  16.07916    -0.2166101     0.0000000E+00 100101\n" +
                            "  0.0000000E+00 0.0000000E+00  70.15248      30.57402     0.0000000E+00 010101\n" +
                            "   8.981672      10.18663     0.0000000E+00 0.0000000E+00  4.043623     001101\n" +
                            "  0.3050545     0.4299508     0.0000000E+00 0.0000000E+00-0.8853669E-01 100002\n" +
                            "  0.5520263      1.790623     0.0000000E+00 0.0000000E+00 -1.083656     010002\n" +
                            "  0.0000000E+00 0.0000000E+00-0.8012895     0.7816230     0.0000000E+00 001002\n" +
                            "  0.0000000E+00 0.0000000E+00 -77.19602     -108.9341     0.0000000E+00 000300\n" +
                            "   12.69789      16.48875     0.0000000E+00 0.0000000E+00  3.489609     000201\n" +
                            "  0.0000000E+00 0.0000000E+00 -5.069942     -2.843125     0.0000000E+00 000102\n" +
                            "  0.1532368     0.1287683     0.0000000E+00 0.0000000E+00 0.5281941     000003\n" +
                            "  -9.976957     0.9469449     0.0000000E+00 0.0000000E+00 -5.300930     400000\n" +
                            "  -152.6563      6.129308     0.0000000E+00 0.0000000E+00 -76.41539     310000\n" +
                            "  -826.5456      6.583704     0.0000000E+00 0.0000000E+00 -399.7167     220000\n" +
                            "  -1918.519     -29.12765     0.0000000E+00 0.0000000E+00 -909.5772     130000\n" +
                            "  -1635.209     -65.19072     0.0000000E+00 0.0000000E+00 -766.2090     040000\n" +
                            "  0.0000000E+00 0.0000000E+00 -9.401521      33.42438     0.0000000E+00 301000\n" +
                            "  0.0000000E+00 0.0000000E+00 -123.0659      258.8123     0.0000000E+00 211000\n" +
                            "  0.0000000E+00 0.0000000E+00 -474.2187      655.8904     0.0000000E+00 121000\n" +
                            "  0.0000000E+00 0.0000000E+00 -578.2094      532.1378     0.0000000E+00 031000\n" +
                            "   13.91904      66.01699     0.0000000E+00 0.0000000E+00 -14.54196     202000\n" +
                            "   77.75827      376.7316     0.0000000E+00 0.0000000E+00 -88.49732     112000\n" +
                            "   105.4726      532.6788     0.0000000E+00 0.0000000E+00 -134.1741     022000\n" +
                            "  0.0000000E+00 0.0000000E+00 -15.41503      3.283606     0.0000000E+00 103000\n" +
                            "  0.0000000E+00 0.0000000E+00 -45.22545      2.413269     0.0000000E+00 013000\n" +
                            "  -2.177850    -0.8721455     0.0000000E+00 0.0000000E+00 -2.843184     004000\n" +
                            "  0.0000000E+00 0.0000000E+00 -32.81921      34.26080     0.0000000E+00 300100\n" +
                            "  0.0000000E+00 0.0000000E+00 -372.3905      184.4922     0.0000000E+00 210100\n" +
                            "  0.0000000E+00 0.0000000E+00 -1337.728      184.1330     0.0000000E+00 120100\n" +
                            "  0.0000000E+00 0.0000000E+00 -1565.580     -197.4945     0.0000000E+00 030100\n" +
                            "   70.63534      228.2582     0.0000000E+00 0.0000000E+00 -73.15696     201100\n" +
                            "   380.2329      1215.537     0.0000000E+00 0.0000000E+00 -415.3579     111100\n" +
                            "   494.3735      1590.372     0.0000000E+00 0.0000000E+00 -591.8256     021100\n" +
                            "  0.0000000E+00 0.0000000E+00 -77.93198      17.60103     0.0000000E+00 102100\n" +
                            "  0.0000000E+00 0.0000000E+00 -223.0137      12.27557     0.0000000E+00 012100\n" +
                            "  -31.10481     -36.62429     0.0000000E+00 0.0000000E+00 -22.54628     003100\n" +
                            " -0.9658420      4.727002     0.0000000E+00 0.0000000E+00 0.1073369     300001\n" +
                            "   24.24598      42.81637     0.0000000E+00 0.0000000E+00  16.89644     210001\n" +
                            "   183.3216      143.1744     0.0000000E+00 0.0000000E+00  112.1739     120001\n" +
                            "   301.2013      162.7531     0.0000000E+00 0.0000000E+00  186.9243     030001\n" +
                            "  0.0000000E+00 0.0000000E+00 -3.876139     -25.78850     0.0000000E+00 201001\n" +
                            "  0.0000000E+00 0.0000000E+00  27.38221     -113.9989     0.0000000E+00 111001\n" +
                            "  0.0000000E+00 0.0000000E+00  131.3011     -80.78758     0.0000000E+00 021001\n" +
                            "  -7.501306     -26.77282     0.0000000E+00 0.0000000E+00  7.720421     102001\n" +
                            "  -24.52428     -85.16174     0.0000000E+00 0.0000000E+00  27.50257     012001\n" +
                            "  0.0000000E+00 0.0000000E+00  5.591541     -3.074645     0.0000000E+00 003001\n" +
                            "   105.9337      172.6025     0.0000000E+00 0.0000000E+00 -83.67594     200200\n" +
                            "   549.6496      806.7927     0.0000000E+00 0.0000000E+00 -435.8623     110200\n" +
                            "   681.5764      871.7481     0.0000000E+00 0.0000000E+00 -568.9103     020200\n" +
                            "  0.0000000E+00 0.0000000E+00 -150.0034      14.52011     0.0000000E+00 101200\n" +
                            "  0.0000000E+00 0.0000000E+00 -436.1752     -35.50850     0.0000000E+00 011200\n" +
                            "  -144.4081     -219.5304     0.0000000E+00 0.0000000E+00 -66.09782     002200\n" +
                            "  0.0000000E+00 0.0000000E+00  14.61510     -43.70789     0.0000000E+00 200101\n" +
                            "  0.0000000E+00 0.0000000E+00  232.7327     -79.56355     0.0000000E+00 110101\n" +
                            "  0.0000000E+00 0.0000000E+00  599.3280      225.8732     0.0000000E+00 020101\n" +
                            "  -18.65759     -99.06785     0.0000000E+00 0.0000000E+00  47.47036     101101\n" +
                            "  -54.80405     -262.5423     0.0000000E+00 0.0000000E+00  143.3339     011101\n" +
                            "  0.0000000E+00 0.0000000E+00  58.74026      11.34695     0.0000000E+00 002101\n" +
                            "  0.1054220    -0.4751554     0.0000000E+00 0.0000000E+00 0.5779473     200002\n" +
                            "  -6.795688     -8.837419     0.0000000E+00 0.0000000E+00 0.9524430     110002\n" +
                            "  -34.17050     -35.95541     0.0000000E+00 0.0000000E+00 -7.495948     020002\n" +
                            "  0.0000000E+00 0.0000000E+00  1.734941      8.601513     0.0000000E+00 101002\n" +
                            "  0.0000000E+00 0.0000000E+00 -7.617681      21.79765     0.0000000E+00 011002\n" +
                            " -0.5557310      1.344025     0.0000000E+00 0.0000000E+00 -1.593362     002002\n" +
                            "  0.0000000E+00 0.0000000E+00 -149.2323     -39.41091     0.0000000E+00 100300\n" +
                            "  0.0000000E+00 0.0000000E+00 -433.4617     -162.4127     0.0000000E+00 010300\n" +
                            "  -291.5486     -496.0132     0.0000000E+00 0.0000000E+00 -79.23570     001300\n" +
                            "   10.97916     -25.31207     0.0000000E+00 0.0000000E+00  52.33976     100201\n" +
                            "   40.25548      4.732903     0.0000000E+00 0.0000000E+00  136.8044     010201\n" +
                            "  0.0000000E+00 0.0000000E+00  193.4254      124.9710     0.0000000E+00 001201\n" +
                            "  0.0000000E+00 0.0000000E+00 -7.305327      21.18384     0.0000000E+00 100102\n" +
                            "  0.0000000E+00 0.0000000E+00 -65.75976      31.17963     0.0000000E+00 010102\n" +
                            "  -11.16146     -3.244760     0.0000000E+00 0.0000000E+00 -10.09690     001102\n" +
                            " -0.4517809    -0.4565319     0.0000000E+00 0.0000000E+00 0.9030411E-01 100003\n" +
                            "  -1.972490     -2.666655     0.0000000E+00 0.0000000E+00  1.003798     010003\n" +
                            "  0.0000000E+00 0.0000000E+00 0.2834711     -1.374251     0.0000000E+00 001003\n" +
                            "  -214.0908     -392.4338     0.0000000E+00 0.0000000E+00 -34.68166     000400\n" +
                            "  0.0000000E+00 0.0000000E+00  216.0727      196.7766     0.0000000E+00 000301\n" +
                            "  -25.90448     -24.22964     0.0000000E+00 0.0000000E+00 -12.54294     000202\n" +
                            "  0.0000000E+00 0.0000000E+00  4.694373    -0.6590756     0.0000000E+00 000103\n" +
                            " -0.1779329    -0.1563274     0.0000000E+00 0.0000000E+00-0.4727283     000004\n" +
                            "  -29.75538     -21.23075     0.0000000E+00 0.0000000E+00 -10.44215     500000\n" +
                            "  -553.2239     -339.5426     0.0000000E+00 0.0000000E+00 -210.9598     410000\n" +
                            "  -3953.086     -2122.320     0.0000000E+00 0.0000000E+00 -1586.367     320000\n" +
                            "  -13745.00     -6516.409     0.0000000E+00 0.0000000E+00 -5706.428     230000\n" +
                            "  -23455.09     -9885.515     0.0000000E+00 0.0000000E+00 -9975.493     140000\n" +
                            "  -15806.79     -5956.656     0.0000000E+00 0.0000000E+00 -6849.017     050000\n" +
                            "  0.0000000E+00 0.0000000E+00  14.38922      178.6928     0.0000000E+00 401000\n" +
                            "  0.0000000E+00 0.0000000E+00  40.50056      2148.243     0.0000000E+00 311000\n" +
                            "  0.0000000E+00 0.0000000E+00 -380.7851      9655.272     0.0000000E+00 221000\n" +
                            "  0.0000000E+00 0.0000000E+00 -1853.856      19212.05     0.0000000E+00 131000\n" +
                            "  0.0000000E+00 0.0000000E+00 -2222.295      14237.73     0.0000000E+00 041000\n" +
                            "   11.67471      199.9222     0.0000000E+00 0.0000000E+00 -11.67714     302000\n" +
                            "   132.0658      1919.471     0.0000000E+00 0.0000000E+00 -167.3936     212000\n" +
                            "   461.4107      6059.271     0.0000000E+00 0.0000000E+00 -670.4801     122000\n" +
                            "   518.7984      6319.838     0.0000000E+00 0.0000000E+00 -825.3312     032000\n" +
                            "  0.0000000E+00 0.0000000E+00 -71.51614      38.93266     0.0000000E+00 203000\n" +
                            "  0.0000000E+00 0.0000000E+00 -441.3452      189.3926     0.0000000E+00 113000\n" +
                            "  0.0000000E+00 0.0000000E+00 -675.7380      231.1951     0.0000000E+00 023000\n" +
                            "   22.90992      93.81328     0.0000000E+00 0.0000000E+00 -24.66392     104000\n" +
                            "   70.81096      275.5470     0.0000000E+00 0.0000000E+00 -70.19064     014000\n" +
                            "  0.0000000E+00 0.0000000E+00 -10.77567      1.472426     0.0000000E+00 005000\n" +
                            "  0.0000000E+00 0.0000000E+00  9.807117      349.5353     0.0000000E+00 400100\n" +
                            "  0.0000000E+00 0.0000000E+00 -220.5119      3966.369     0.0000000E+00 310100\n" +
                            "  0.0000000E+00 0.0000000E+00 -2417.754      16815.83     0.0000000E+00 220100\n" +
                            "  0.0000000E+00 0.0000000E+00 -7587.853      31459.23     0.0000000E+00 130100\n" +
                            "  0.0000000E+00 0.0000000E+00 -7786.454      21741.19     0.0000000E+00 040100\n" +
                            "  -11.43603      816.3132     0.0000000E+00 0.0000000E+00 -82.33095     301100\n" +
                            "   70.54641      7503.970     0.0000000E+00 0.0000000E+00 -1014.608     211100\n" +
                            "   645.4732      22809.73     0.0000000E+00 0.0000000E+00 -3767.789     121100\n" +
                            "   1024.496      22979.00     0.0000000E+00 0.0000000E+00 -4410.574     031100\n" +
                            "  0.0000000E+00 0.0000000E+00 -369.8370      275.1177     0.0000000E+00 202100\n" +
                            "  0.0000000E+00 0.0000000E+00 -2200.955      1383.292     0.0000000E+00 112100\n" +
                            "  0.0000000E+00 0.0000000E+00 -3287.356      1733.470     0.0000000E+00 022100\n" +
                            "   160.7902      710.1859     0.0000000E+00 0.0000000E+00 -228.4690     103100\n" +
                            "   509.0378      2096.536     0.0000000E+00 0.0000000E+00 -637.3535     013100\n" +
                            "  0.0000000E+00 0.0000000E+00 -111.9953    -0.2628678     0.0000000E+00 004100\n" +
                            "  -5.758570      9.546119     0.0000000E+00 0.0000000E+00 -1.513884     400001\n" +
                            "   36.36660      147.3229     0.0000000E+00 0.0000000E+00  33.66605     310001\n" +
                            "   783.8224      814.7417     0.0000000E+00 0.0000000E+00  444.4069     220001\n" +
                            "   3071.884      1940.581     0.0000000E+00 0.0000000E+00  1589.639     130001\n" +
                            "   3632.994      1725.549     0.0000000E+00 0.0000000E+00  1807.446     040001\n" +
                            "  0.0000000E+00 0.0000000E+00 -35.74393     -82.02310     0.0000000E+00 301001\n" +
                            "  0.0000000E+00 0.0000000E+00 -227.3159     -918.6918     0.0000000E+00 211001\n" +
                            "  0.0000000E+00 0.0000000E+00 -293.4080     -3157.545     0.0000000E+00 121001\n" +
                            "  0.0000000E+00 0.0000000E+00  202.3850     -3419.453     0.0000000E+00 031001\n" +
                            "  -29.38134     -106.6251     0.0000000E+00 0.0000000E+00  2.972707     202001\n" +
                            "  -217.5711     -855.3062     0.0000000E+00 0.0000000E+00  84.23649     112001\n" +
                            "  -365.9947     -1553.940     0.0000000E+00 0.0000000E+00  224.9137     022001\n" +
                            "  0.0000000E+00 0.0000000E+00  24.85574     -39.02708     0.0000000E+00 103001\n" +
                            "  0.0000000E+00 0.0000000E+00  101.4753     -94.29020     0.0000000E+00 013001\n" +
                            "  -1.082611     -14.20356     0.0000000E+00 0.0000000E+00  7.952330     004001\n" +
                            "  -28.81101      821.7017     0.0000000E+00 0.0000000E+00 -89.98187     300200\n" +
                            "   59.24037      7198.323     0.0000000E+00 0.0000000E+00 -1111.634     210200\n" +
                            "   959.1618      20858.33     0.0000000E+00 0.0000000E+00 -4116.539     120200\n" +
                            "   1605.641      19995.93     0.0000000E+00 0.0000000E+00 -4779.545     030200\n" +
                            "  0.0000000E+00 0.0000000E+00 -403.2759      826.0321     0.0000000E+00 201200\n" +
                            "  0.0000000E+00 0.0000000E+00 -2423.044      4324.099     0.0000000E+00 111200\n" +
                            "  0.0000000E+00 0.0000000E+00 -3764.670      5553.639     0.0000000E+00 021200\n" +
                            "   487.7791      2133.061     0.0000000E+00 0.0000000E+00 -848.2134     102200\n" +
                            "   1581.065      6357.426     0.0000000E+00 0.0000000E+00 -2341.619     012200\n" +
                            "  0.0000000E+00 0.0000000E+00 -485.1148     -98.77097     0.0000000E+00 003200\n" +
                            "  0.0000000E+00 0.0000000E+00 -55.71949     -244.1095     0.0000000E+00 300101\n" +
                            "  0.0000000E+00 0.0000000E+00 -166.0822     -2275.052     0.0000000E+00 210101\n" +
                            "  0.0000000E+00 0.0000000E+00  723.3946     -6651.761     0.0000000E+00 120101\n" +
                            "  0.0000000E+00 0.0000000E+00  2192.310     -6049.684     0.0000000E+00 030101\n" +
                            "  -146.0338     -689.0553     0.0000000E+00 0.0000000E+00  96.77127     201101\n" +
                            "  -1042.912     -4680.673     0.0000000E+00 0.0000000E+00  867.2767     111101\n" +
                            "  -1676.558     -7511.637     0.0000000E+00 0.0000000E+00  1692.084     021101\n" +
                            "  0.0000000E+00 0.0000000E+00  197.9843     -272.5002     0.0000000E+00 102101\n" +
                            "  0.0000000E+00 0.0000000E+00  702.6690     -643.8310     0.0000000E+00 012101\n" +
                            "   29.82275     -69.30547     0.0000000E+00 0.0000000E+00  86.31481     003101\n" +
                            "   3.588581     0.2910153E-01 0.0000000E+00 0.0000000E+00 0.7846522     300002\n" +
                            "   11.56552     -27.49053     0.0000000E+00 0.0000000E+00 -6.744840     210002\n" +
                            "  -98.11480     -180.2276     0.0000000E+00 0.0000000E+00 -106.0863     120002\n" +
                            "  -299.7670     -284.9096     0.0000000E+00 0.0000000E+00 -246.2945     030002\n" +
                            "  0.0000000E+00 0.0000000E+00  12.35416      18.84388     0.0000000E+00 201002\n" +
                            "  0.0000000E+00 0.0000000E+00  44.56422      170.5205     0.0000000E+00 111002\n" +
                            "  0.0000000E+00 0.0000000E+00 -38.63879      278.3962     0.0000000E+00 021002\n" +
                            "   15.50903      34.89582     0.0000000E+00 0.0000000E+00 -5.848973     102002\n" +
                            "   63.57530      154.0197     0.0000000E+00 0.0000000E+00 -33.43279     012002\n" +
                            "  0.0000000E+00 0.0000000E+00 -4.992876      10.90575     0.0000000E+00 003002\n" +
                            "  0.0000000E+00 0.0000000E+00  103.6637      915.3075     0.0000000E+00 200300\n" +
                            "  0.0000000E+00 0.0000000E+00  255.7248      4794.416     0.0000000E+00 110300\n" +
                            "  0.0000000E+00 0.0000000E+00 -291.3475      6039.262     0.0000000E+00 020300\n" +
                            "   684.6689      2964.018     0.0000000E+00 0.0000000E+00 -1421.264     101300\n" +
                            "   2254.244      8877.326     0.0000000E+00 0.0000000E+00 -3874.295     011300\n" +
                            "  0.0000000E+00 0.0000000E+00 -1124.101     -529.8105     0.0000000E+00 002300\n" +
                            "  -202.7415     -890.0244     0.0000000E+00 0.0000000E+00  201.9123     200201\n" +
                            "  -1394.436     -5380.623     0.0000000E+00 0.0000000E+00  1430.404     110201\n" +
                            "  -2121.106     -7676.616     0.0000000E+00 0.0000000E+00  2396.999     020201\n" +
                            "  0.0000000E+00 0.0000000E+00  379.9323     -641.6391     0.0000000E+00 101201\n" +
                            "  0.0000000E+00 0.0000000E+00  1337.442     -1474.077     0.0000000E+00 011201\n" +
                            "   267.3311      54.63728     0.0000000E+00 0.0000000E+00  334.8223     002201\n" +
                            "  0.0000000E+00 0.0000000E+00  16.87186      80.68238     0.0000000E+00 200102\n" +
                            "  0.0000000E+00 0.0000000E+00 -89.44774      470.2943     0.0000000E+00 110102\n" +
                            "  0.0000000E+00 0.0000000E+00 -597.3554      435.1748     0.0000000E+00 020102\n" +
                            "   74.70653      231.1194     0.0000000E+00 0.0000000E+00 -68.89706     101102\n" +
                            "   276.7043      838.0805     0.0000000E+00 0.0000000E+00 -273.1792     011102\n" +
                            "  0.0000000E+00 0.0000000E+00 -75.22386      59.08220     0.0000000E+00 002102\n" +
                            " -0.1313612    -0.3021322     0.0000000E+00 0.0000000E+00-0.8838416     200003\n" +
                            "   5.419448      5.653220     0.0000000E+00 0.0000000E+00 -5.273157     110003\n" +
                            "   37.55502      39.78002     0.0000000E+00 0.0000000E+00 -1.102995     020003\n" +
                            "  0.0000000E+00 0.0000000E+00 -3.357506     -7.207598     0.0000000E+00 101003\n" +
                            "  0.0000000E+00 0.0000000E+00 -1.009850     -28.71709     0.0000000E+00 011003\n" +
                            "  0.1968051     -2.150982     0.0000000E+00 0.0000000E+00  1.749978     002003\n" +
                            "   227.8792      1481.694     0.0000000E+00 0.0000000E+00 -830.5857     100400\n" +
                            "   842.0167      4432.028     0.0000000E+00 0.0000000E+00 -2230.836     010400\n" +
                            "  0.0000000E+00 0.0000000E+00 -1466.374     -1127.480     0.0000000E+00 001400\n" +
                            "  0.0000000E+00 0.0000000E+00  219.4544     -478.2677     0.0000000E+00 100301\n" +
                            "  0.0000000E+00 0.0000000E+00  913.6617     -1001.549     0.0000000E+00 010301\n" +
                            "   750.8330      697.5410     0.0000000E+00 0.0000000E+00  543.9775     001301\n" +
                            "   58.00998      256.2382     0.0000000E+00 0.0000000E+00 -125.5939     100202\n" +
                            "   197.5804      773.1926     0.0000000E+00 0.0000000E+00 -413.8596     010202\n" +
                            "  0.0000000E+00 0.0000000E+00 -294.1276      22.65799     0.0000000E+00 001202\n" +
                            "  0.0000000E+00 0.0000000E+00 -2.014462     -31.81691     0.0000000E+00 100103\n" +
                            "  0.0000000E+00 0.0000000E+00  43.89215     -90.90478     0.0000000E+00 010103\n" +
                            "   11.09987     -5.399056     0.0000000E+00 0.0000000E+00  15.82454     001103\n" +
                            "  0.5804525     0.5922626     0.0000000E+00 0.0000000E+00-0.1178738     100004\n" +
                            "   3.490294      3.999654     0.0000000E+00 0.0000000E+00 -1.015073     010004\n" +
                            "  0.0000000E+00 0.0000000E+00 0.1315190      1.695271     0.0000000E+00 001004\n" +
                            "  0.0000000E+00 0.0000000E+00 -856.5734     -883.2641     0.0000000E+00 000500\n" +
                            "   724.1768      909.3832     0.0000000E+00 0.0000000E+00  303.8991     000401\n" +
                            "  0.0000000E+00 0.0000000E+00 -372.3658     -154.8156     0.0000000E+00 000302\n" +
                            "   36.53483      19.79919     0.0000000E+00 0.0000000E+00  26.53526     000203\n" +
                            "  0.0000000E+00 0.0000000E+00 -3.706771      3.629507     0.0000000E+00 000104\n" +
                            "  0.2146852     0.1879224     0.0000000E+00 0.0000000E+00 0.4360169     000005"
            );
            optics_后偏转段仅CCT1和前后2点4米漂移段.map = CosyArbitraryOrder.readMap(
                    "  -4.864382     -2.314735     0.0000000E+00 0.0000000E+00-0.6086743     100000\n" +
                            "  -13.33308     -6.550172     0.0000000E+00 0.0000000E+00 -1.772100     010000\n" +
                            "  0.0000000E+00 0.0000000E+00 -1.771345    -0.8326336     0.0000000E+00 001000\n" +
                            "  0.0000000E+00 0.0000000E+00 0.6107307    -0.2774643     0.0000000E+00 000100\n" +
                            "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00  1.000000     000010\n" +
                            "  0.5046709     0.1150208     0.0000000E+00 0.0000000E+00  1.043464     000001\n" +
                            "  -5.926099     -2.252022     0.0000000E+00 0.0000000E+00 -3.733121     200000\n" +
                            "  -45.22770     -17.90751     0.0000000E+00 0.0000000E+00 -21.65271     110000\n" +
                            "  -81.35599     -32.80459     0.0000000E+00 0.0000000E+00 -32.14106     020000\n" +
                            "  0.0000000E+00 0.0000000E+00 -14.73325     -5.107612     0.0000000E+00 101000\n" +
                            "  0.0000000E+00 0.0000000E+00 -41.46498     -14.26538     0.0000000E+00 011000\n" +
                            "  -1.046306    -0.1669061     0.0000000E+00 0.0000000E+00-0.6828699     002000\n" +
                            "  0.0000000E+00 0.0000000E+00 -33.13596     -11.50695     0.0000000E+00 100100\n" +
                            "  0.0000000E+00 0.0000000E+00 -90.18824     -30.98007     0.0000000E+00 010100\n" +
                            "  -2.249144     0.4113887     0.0000000E+00 0.0000000E+00-0.9944340     001100\n" +
                            " -0.7465824     -1.703109     0.0000000E+00 0.0000000E+00 0.4613356     100001\n" +
                            "   5.104978     -1.233613     0.0000000E+00 0.0000000E+00  2.221966     010001\n" +
                            "  0.0000000E+00 0.0000000E+00  3.465701     0.8172833     0.0000000E+00 001001\n" +
                            "   1.261138      2.267513     0.0000000E+00 0.0000000E+00 -1.254639     000200\n" +
                            "  0.0000000E+00 0.0000000E+00  8.877098      3.348089     0.0000000E+00 000101\n" +
                            " -0.1135904     0.7288792E-01 0.0000000E+00 0.0000000E+00-0.8926233     000002\n" +
                            "  -17.53681    -0.4122981     0.0000000E+00 0.0000000E+00 -7.388584     300000\n" +
                            "  -175.9548     -14.52130     0.0000000E+00 0.0000000E+00 -80.19689     210000\n" +
                            "  -576.7836     -72.73386     0.0000000E+00 0.0000000E+00 -277.1071     120000\n" +
                            "  -625.2258     -101.4069     0.0000000E+00 0.0000000E+00 -310.1015     030000\n" +
                            "  0.0000000E+00 0.0000000E+00 -28.13385     -8.206529     0.0000000E+00 201000\n" +
                            "  0.0000000E+00 0.0000000E+00 -182.2789     -54.50684     0.0000000E+00 111000\n" +
                            "  0.0000000E+00 0.0000000E+00 -290.3855     -88.56111     0.0000000E+00 021000\n" +
                            "  -1.751464     0.4317126     0.0000000E+00 0.0000000E+00 -7.655594     102000\n" +
                            "  -3.005152      1.780421     0.0000000E+00 0.0000000E+00 -21.30081     012000\n" +
                            "  0.0000000E+00 0.0000000E+00 -11.45622     -3.873640     0.0000000E+00 003000\n" +
                            "  0.0000000E+00 0.0000000E+00 -61.35302     -21.43981     0.0000000E+00 200100\n" +
                            "  0.0000000E+00 0.0000000E+00 -392.1652     -136.8655     0.0000000E+00 110100\n" +
                            "  0.0000000E+00 0.0000000E+00 -621.3956     -217.1896     0.0000000E+00 020100\n" +
                            "   2.474181      3.422094     0.0000000E+00 0.0000000E+00 -19.17980     101100\n" +
                            "   19.96196      13.76934     0.0000000E+00 0.0000000E+00 -51.31628     011100\n" +
                            "  0.0000000E+00 0.0000000E+00 -77.57642     -27.98499     0.0000000E+00 002100\n" +
                            " -0.3463323     -2.378831     0.0000000E+00 0.0000000E+00 0.1496557     200001\n" +
                            "   25.87707     -5.148206     0.0000000E+00 0.0000000E+00  13.63339     110001\n" +
                            "   95.28497      12.54585     0.0000000E+00 0.0000000E+00  39.36099     020001\n" +
                            "  0.0000000E+00 0.0000000E+00  10.91178      1.078627     0.0000000E+00 101001\n" +
                            "  0.0000000E+00 0.0000000E+00  53.74872      11.00829     0.0000000E+00 011001\n" +
                            "   1.826494     0.3727395     0.0000000E+00 0.0000000E+00  2.084630     002001\n" +
                            "   12.68580      7.870824     0.0000000E+00 0.0000000E+00 -2.612901     100200\n" +
                            "   49.77692      25.86782     0.0000000E+00 0.0000000E+00 -5.937081     010200\n" +
                            "  0.0000000E+00 0.0000000E+00 -187.5453     -68.79775     0.0000000E+00 001200\n" +
                            "  0.0000000E+00 0.0000000E+00  47.85119      10.93546     0.0000000E+00 100101\n" +
                            "  0.0000000E+00 0.0000000E+00  182.4654      47.66929     0.0000000E+00 010101\n" +
                            "   10.35779      2.500631     0.0000000E+00 0.0000000E+00  6.883115     001101\n" +
                            "   1.730709     0.8101713     0.0000000E+00 0.0000000E+00-0.1003657     100002\n" +
                            "   1.039229      2.361348     0.0000000E+00 0.0000000E+00 -1.618196     010002\n" +
                            "  0.0000000E+00 0.0000000E+00 -3.145038    -0.3965939     0.0000000E+00 001002\n" +
                            "  0.0000000E+00 0.0000000E+00 -157.9942     -58.68184     0.0000000E+00 000300\n" +
                            "   10.01161      1.902502     0.0000000E+00 0.0000000E+00  4.803433     000201\n" +
                            "  0.0000000E+00 0.0000000E+00 -13.42227     -3.225016     0.0000000E+00 000102\n" +
                            "  0.2830395E-01-0.4175706E-01 0.0000000E+00 0.0000000E+00 0.7664924     000003\n" +
                            "  -29.53772      6.758755     0.0000000E+00 0.0000000E+00 -19.84845     400000\n" +
                            "  -479.9666      59.44315     0.0000000E+00 0.0000000E+00 -278.7337     310000\n" +
                            "  -2677.378      173.4486     0.0000000E+00 0.0000000E+00 -1434.122     220000\n" +
                            "  -6313.210      160.4069     0.0000000E+00 0.0000000E+00 -3228.882     130000\n" +
                            "  -5425.122     -28.85472     0.0000000E+00 0.0000000E+00 -2697.772     040000\n" +
                            "  0.0000000E+00 0.0000000E+00 -63.65793     -7.029463     0.0000000E+00 301000\n" +
                            "  0.0000000E+00 0.0000000E+00 -654.4387     -92.07621     0.0000000E+00 211000\n" +
                            "  0.0000000E+00 0.0000000E+00 -2179.237     -353.8701     0.0000000E+00 121000\n" +
                            "  0.0000000E+00 0.0000000E+00 -2386.875     -429.5248     0.0000000E+00 031000\n" +
                            "  -23.36102      2.596222     0.0000000E+00 0.0000000E+00 -32.96808     202000\n" +
                            "  -144.7117      10.42631     0.0000000E+00 0.0000000E+00 -195.6669     112000\n" +
                            "  -225.7627      7.806082     0.0000000E+00 0.0000000E+00 -290.0649     022000\n" +
                            "  0.0000000E+00 0.0000000E+00 -50.24235     -12.42596     0.0000000E+00 103000\n" +
                            "  0.0000000E+00 0.0000000E+00 -138.9441     -34.17644     0.0000000E+00 013000\n" +
                            "  -5.970629     -1.968322     0.0000000E+00 0.0000000E+00 -5.743928     004000\n" +
                            "  0.0000000E+00 0.0000000E+00 -135.7030     -20.73040     0.0000000E+00 300100\n" +
                            "  0.0000000E+00 0.0000000E+00 -1358.598     -250.3069     0.0000000E+00 210100\n" +
                            "  0.0000000E+00 0.0000000E+00 -4457.347     -929.0825     0.0000000E+00 120100\n" +
                            "  0.0000000E+00 0.0000000E+00 -4847.869     -1107.403     0.0000000E+00 030100\n" +
                            "  -17.45944      15.97962     0.0000000E+00 0.0000000E+00 -110.8029     201100\n" +
                            "  -124.0508      74.48820     0.0000000E+00 0.0000000E+00 -623.9017     111100\n" +
                            "  -229.6503      78.16855     0.0000000E+00 0.0000000E+00 -882.2076     021100\n" +
                            "  0.0000000E+00 0.0000000E+00 -282.8134     -84.81600     0.0000000E+00 102100\n" +
                            "  0.0000000E+00 0.0000000E+00 -767.4810     -229.7450     0.0000000E+00 012100\n" +
                            "  -59.15971     -20.36841     0.0000000E+00 0.0000000E+00 -43.21279     003100\n" +
                            "  -12.02376     -3.936138     0.0000000E+00 0.0000000E+00 0.3310085     300001\n" +
                            "   9.346599     -29.26443     0.0000000E+00 0.0000000E+00  55.87376     210001\n" +
                            "   427.5106     -37.52488     0.0000000E+00 0.0000000E+00  363.3756     120001\n" +
                            "   870.4018      41.21657     0.0000000E+00 0.0000000E+00  591.0237     030001\n" +
                            "  0.0000000E+00 0.0000000E+00  15.90628     -2.215362     0.0000000E+00 201001\n" +
                            "  0.0000000E+00 0.0000000E+00  210.6751      20.15043     0.0000000E+00 111001\n" +
                            "  0.0000000E+00 0.0000000E+00  504.2789      87.28525     0.0000000E+00 021001\n" +
                            "   1.645181     -2.522591     0.0000000E+00 0.0000000E+00  19.14512     102001\n" +
                            "   4.798685     -7.891820     0.0000000E+00 0.0000000E+00  65.13564     012001\n" +
                            "  0.0000000E+00 0.0000000E+00  19.65869      3.745527     0.0000000E+00 003001\n" +
                            "   89.32322      25.92818     0.0000000E+00 0.0000000E+00 -80.20193     200200\n" +
                            "   475.9832      128.7268     0.0000000E+00 0.0000000E+00 -411.6740     110200\n" +
                            "   593.2493      145.2677     0.0000000E+00 0.0000000E+00 -528.8659     020200\n" +
                            "  0.0000000E+00 0.0000000E+00 -573.9869     -196.3783     0.0000000E+00 101200\n" +
                            "  0.0000000E+00 0.0000000E+00 -1557.016     -531.8548     0.0000000E+00 011200\n" +
                            "  -226.1496     -79.24409     0.0000000E+00 0.0000000E+00 -121.9882     002200\n" +
                            "  0.0000000E+00 0.0000000E+00  84.55976      8.644063     0.0000000E+00 200101\n" +
                            "  0.0000000E+00 0.0000000E+00  764.9098      141.2564     0.0000000E+00 110101\n" +
                            "  0.0000000E+00 0.0000000E+00  1568.044      358.7455     0.0000000E+00 020101\n" +
                            "  -4.201068     -11.79524     0.0000000E+00 0.0000000E+00  89.74120     101101\n" +
                            "  -38.80742     -41.72673     0.0000000E+00 0.0000000E+00  273.7270     011101\n" +
                            "  0.0000000E+00 0.0000000E+00  172.6206      43.13821     0.0000000E+00 002101\n" +
                            "   4.259331      1.004359     0.0000000E+00 0.0000000E+00  1.938394     200002\n" +
                            "   3.955046      7.305942     0.0000000E+00 0.0000000E+00  1.000580     110002\n" +
                            "  -67.23733    -0.9760138     0.0000000E+00 0.0000000E+00 -25.67021     020002\n" +
                            "  0.0000000E+00 0.0000000E+00 -7.077341     0.2183381     0.0000000E+00 101002\n" +
                            "  0.0000000E+00 0.0000000E+00 -53.21177     -6.684218     0.0000000E+00 011002\n" +
                            "  -1.804883    -0.3435709E-01 0.0000000E+00 0.0000000E+00 -3.813258     002002\n" +
                            "  0.0000000E+00 0.0000000E+00 -460.4385     -162.3983     0.0000000E+00 100300\n" +
                            "  0.0000000E+00 0.0000000E+00 -1268.314     -445.8388     0.0000000E+00 010300\n" +
                            "  -394.3083     -138.2457     0.0000000E+00 0.0000000E+00 -146.6596     001300\n" +
                            "  -34.68905     -16.73947     0.0000000E+00 0.0000000E+00  79.59436     100201\n" +
                            "  -154.2115     -65.66600     0.0000000E+00 0.0000000E+00  216.7337     010201\n" +
                            "  0.0000000E+00 0.0000000E+00  503.0383      143.8717     0.0000000E+00 001201\n" +
                            "  0.0000000E+00 0.0000000E+00 -51.13808     -7.334445     0.0000000E+00 100102\n" +
                            "  0.0000000E+00 0.0000000E+00 -250.1682     -49.23921     0.0000000E+00 010102\n" +
                            "  -15.54473     -2.040574     0.0000000E+00 0.0000000E+00 -18.75778     001102\n" +
                            "  -2.041780    -0.5007542     0.0000000E+00 0.0000000E+00-0.1097162     100003\n" +
                            "  -5.488040     -2.522396     0.0000000E+00 0.0000000E+00 0.8072260     010003\n" +
                            "  0.0000000E+00 0.0000000E+00  2.935492     0.2848699     0.0000000E+00 001003\n" +
                            "  -258.8536     -90.08102     0.0000000E+00 0.0000000E+00 -57.98082     000400\n" +
                            "  0.0000000E+00 0.0000000E+00  504.6756      153.7665     0.0000000E+00 000301\n" +
                            "  -23.74106     -3.970130     0.0000000E+00 0.0000000E+00 -18.57880     000202\n" +
                            "  0.0000000E+00 0.0000000E+00  16.35900      3.057663     0.0000000E+00 000103\n" +
                            " -0.6268642E-02 0.2696139E-01 0.0000000E+00 0.0000000E+00-0.6805890     000004\n" +
                            "  -86.27857      10.84787     0.0000000E+00 0.0000000E+00 -37.27212     500000\n" +
                            "  -1676.464      141.1971     0.0000000E+00 0.0000000E+00 -757.4056     410000\n" +
                            "  -12375.42      706.9372     0.0000000E+00 0.0000000E+00 -5699.260     320000\n" +
                            "  -44142.72      1668.904     0.0000000E+00 0.0000000E+00 -20472.25     230000\n" +
                            "  -76907.22      1750.906     0.0000000E+00 0.0000000E+00 -35696.86     140000\n" +
                            "  -52735.93      522.9508     0.0000000E+00 0.0000000E+00 -24421.74     050000\n" +
                            "  0.0000000E+00 0.0000000E+00 -152.1620    -0.6720233     0.0000000E+00 401000\n" +
                            "  0.0000000E+00 0.0000000E+00 -2207.138     -81.44198     0.0000000E+00 311000\n" +
                            "  0.0000000E+00 0.0000000E+00 -11519.36     -682.5008     0.0000000E+00 221000\n" +
                            "  0.0000000E+00 0.0000000E+00 -26040.41     -1989.260     0.0000000E+00 131000\n" +
                            "  0.0000000E+00 0.0000000E+00 -21733.51     -1977.435     0.0000000E+00 041000\n" +
                            "  -154.7264     0.3227693     0.0000000E+00 0.0000000E+00 -119.5156     302000\n" +
                            "  -1435.003     -5.973137     0.0000000E+00 0.0000000E+00 -1142.521     212000\n" +
                            "  -4418.794     -46.32799     0.0000000E+00 0.0000000E+00 -3595.789     122000\n" +
                            "  -4514.351     -73.38591     0.0000000E+00 0.0000000E+00 -3743.156     032000\n" +
                            "  0.0000000E+00 0.0000000E+00 -224.7062     -29.77177     0.0000000E+00 203000\n" +
                            "  0.0000000E+00 0.0000000E+00 -1330.139     -183.9075     0.0000000E+00 113000\n" +
                            "  0.0000000E+00 0.0000000E+00 -1972.310     -284.6998     0.0000000E+00 023000\n" +
                            "  -21.37400      1.322543     0.0000000E+00 0.0000000E+00 -59.75620     104000\n" +
                            "  -58.15864      3.399382     0.0000000E+00 0.0000000E+00 -165.9971     014000\n" +
                            "  0.0000000E+00 0.0000000E+00 -33.74094     -7.973936     0.0000000E+00 005000\n" +
                            "  0.0000000E+00 0.0000000E+00 -317.8575     -5.900120     0.0000000E+00 400100\n" +
                            "  0.0000000E+00 0.0000000E+00 -4540.150     -243.8534     0.0000000E+00 310100\n" +
                            "  0.0000000E+00 0.0000000E+00 -23428.53     -1850.664     0.0000000E+00 220100\n" +
                            "  0.0000000E+00 0.0000000E+00 -52526.10     -5215.635     0.0000000E+00 130100\n" +
                            "  0.0000000E+00 0.0000000E+00 -43592.93     -5110.561     0.0000000E+00 040100\n" +
                            "  -442.9302      4.702056     0.0000000E+00 0.0000000E+00 -381.9312     301100\n" +
                            "  -3910.886     -3.295927     0.0000000E+00 0.0000000E+00 -3595.445     211100\n" +
                            "  -11543.84     -148.2879     0.0000000E+00 0.0000000E+00 -11169.65     121100\n" +
                            "  -11339.82     -269.5861     0.0000000E+00 0.0000000E+00 -11488.77     031100\n" +
                            "  0.0000000E+00 0.0000000E+00 -1193.602     -188.7371     0.0000000E+00 202100\n" +
                            "  0.0000000E+00 0.0000000E+00 -6944.557     -1145.631     0.0000000E+00 112100\n" +
                            "  0.0000000E+00 0.0000000E+00 -10199.79     -1767.030     0.0000000E+00 022100\n" +
                            "  -161.0357      4.428888     0.0000000E+00 0.0000000E+00 -492.3185     103100\n" +
                            "  -428.7603      11.36296     0.0000000E+00 0.0000000E+00 -1355.828     013100\n" +
                            "  0.0000000E+00 0.0000000E+00 -349.6721     -92.78303     0.0000000E+00 004100\n" +
                            "  -49.82869     -8.236603     0.0000000E+00 0.0000000E+00 -12.51983     400001\n" +
                            "  -277.4675     -134.3366     0.0000000E+00 0.0000000E+00  48.92820     310001\n" +
                            "   748.5573     -664.3989     0.0000000E+00 0.0000000E+00  1307.083     220001\n" +
                            "   6465.320     -1258.747     0.0000000E+00 0.0000000E+00  5167.283     130001\n" +
                            "   9277.804     -732.8327     0.0000000E+00 0.0000000E+00  6086.459     040001\n" +
                            "  0.0000000E+00 0.0000000E+00  19.11968     -9.770295     0.0000000E+00 301001\n" +
                            "  0.0000000E+00 0.0000000E+00  648.1160     -24.02750     0.0000000E+00 211001\n" +
                            "  0.0000000E+00 0.0000000E+00  3563.633      198.7215     0.0000000E+00 121001\n" +
                            "  0.0000000E+00 0.0000000E+00  5376.350      546.7082     0.0000000E+00 031001\n" +
                            "   53.10665     -6.677633     0.0000000E+00 0.0000000E+00  66.46277     202001\n" +
                            "   405.3478     -40.80102     0.0000000E+00 0.0000000E+00  514.8842     112001\n" +
                            "   753.6575     -52.44851     0.0000000E+00 0.0000000E+00  940.5328     022001\n" +
                            "  0.0000000E+00 0.0000000E+00  117.5597      14.14966     0.0000000E+00 103001\n" +
                            "  0.0000000E+00 0.0000000E+00  399.6909      56.61265     0.0000000E+00 013001\n" +
                            "   9.222437     0.8740335     0.0000000E+00 0.0000000E+00  19.68960     004001\n" +
                            "  -164.7066      14.71084     0.0000000E+00 0.0000000E+00 -239.4729     300200\n" +
                            "  -1194.103      75.89983     0.0000000E+00 0.0000000E+00 -2250.146     210200\n" +
                            "  -2920.094      38.41512     0.0000000E+00 0.0000000E+00 -6999.156     120200\n" +
                            "  -2358.710     -139.1483     0.0000000E+00 0.0000000E+00 -7209.653     030200\n" +
                            "  0.0000000E+00 0.0000000E+00 -2049.195     -387.8847     0.0000000E+00 201200\n" +
                            "  0.0000000E+00 0.0000000E+00 -11853.10     -2332.966     0.0000000E+00 111200\n" +
                            "  0.0000000E+00 0.0000000E+00 -17529.96     -3637.375     0.0000000E+00 021200\n" +
                            "  -489.1734     -12.33390     0.0000000E+00 0.0000000E+00 -1613.105     102200\n" +
                            "  -1279.286     -31.66429     0.0000000E+00 0.0000000E+00 -4430.899     012200\n" +
                            "  0.0000000E+00 0.0000000E+00 -1490.320     -436.1193     0.0000000E+00 003200\n" +
                            "  0.0000000E+00 0.0000000E+00  153.1625     -10.22398     0.0000000E+00 300101\n" +
                            "  0.0000000E+00 0.0000000E+00  2468.256      117.0576     0.0000000E+00 210101\n" +
                            "  0.0000000E+00 0.0000000E+00  10988.07      1154.453     0.0000000E+00 120101\n" +
                            "  0.0000000E+00 0.0000000E+00  14958.71      2146.220     0.0000000E+00 030101\n" +
                            "   227.7535     -41.23618     0.0000000E+00 0.0000000E+00  351.2643     201101\n" +
                            "   1446.550     -245.3861     0.0000000E+00 0.0000000E+00  2426.777     111101\n" +
                            "   2390.757     -319.8422     0.0000000E+00 0.0000000E+00  4069.653     021101\n" +
                            "  0.0000000E+00 0.0000000E+00  871.5523      152.4082     0.0000000E+00 102101\n" +
                            "  0.0000000E+00 0.0000000E+00  2779.107      531.5031     0.0000000E+00 012101\n" +
                            "   121.8815      22.19806     0.0000000E+00 0.0000000E+00  186.8553     003101\n" +
                            "   24.47607      1.493037     0.0000000E+00 0.0000000E+00  7.243705     300002\n" +
                            "   185.7543      29.18160     0.0000000E+00 0.0000000E+00  20.33546     210002\n" +
                            "   180.2209      94.94584     0.0000000E+00 0.0000000E+00 -211.1500     120002\n" +
                            "  -491.9303      43.51107     0.0000000E+00 0.0000000E+00 -642.2558     030002\n" +
                            "  0.0000000E+00 0.0000000E+00  2.982019      4.334467     0.0000000E+00 201002\n" +
                            "  0.0000000E+00 0.0000000E+00 -135.0803      6.780564     0.0000000E+00 111002\n" +
                            "  0.0000000E+00 0.0000000E+00 -552.3024     -55.96403     0.0000000E+00 021002\n" +
                            " -0.3808206      3.819581     0.0000000E+00 0.0000000E+00 -28.55044     102002\n" +
                            "  -1.154767      15.51019     0.0000000E+00 0.0000000E+00 -117.3309     012002\n" +
                            "  0.0000000E+00 0.0000000E+00 -30.12607     -4.213239     0.0000000E+00 003002\n" +
                            "  0.0000000E+00 0.0000000E+00 -1184.175     -268.4397     0.0000000E+00 200300\n" +
                            "  0.0000000E+00 0.0000000E+00 -6999.419     -1630.438     0.0000000E+00 110300\n" +
                            "  0.0000000E+00 0.0000000E+00 -10777.12     -2629.931     0.0000000E+00 020300\n" +
                            "  -719.2104     -62.85606     0.0000000E+00 0.0000000E+00 -2479.645     101300\n" +
                            "  -1838.677     -158.0263     0.0000000E+00 0.0000000E+00 -6801.786     011300\n" +
                            "  0.0000000E+00 0.0000000E+00 -3301.275     -1049.332     0.0000000E+00 002300\n" +
                            "   99.80095     -65.66580     0.0000000E+00 0.0000000E+00  386.7219     200201\n" +
                            "   369.0628     -395.1136     0.0000000E+00 0.0000000E+00  2425.847     110201\n" +
                            "   454.4147     -522.0619     0.0000000E+00 0.0000000E+00  3737.289     020201\n" +
                            "  0.0000000E+00 0.0000000E+00  2049.524      475.6252     0.0000000E+00 101201\n" +
                            "  0.0000000E+00 0.0000000E+00  6341.242      1552.196     0.0000000E+00 011201\n" +
                            "   580.9050      135.4722     0.0000000E+00 0.0000000E+00  650.5522     002201\n" +
                            "  0.0000000E+00 0.0000000E+00 -61.32346      3.481044     0.0000000E+00 200102\n" +
                            "  0.0000000E+00 0.0000000E+00 -893.5615     -85.91984     0.0000000E+00 110102\n" +
                            "  0.0000000E+00 0.0000000E+00 -2409.652     -387.8660     0.0000000E+00 020102\n" +
                            "   1.404173      25.05348     0.0000000E+00 0.0000000E+00 -184.3599     101102\n" +
                            "   53.28155      97.05586     0.0000000E+00 0.0000000E+00 -661.6854     011102\n" +
                            "  0.0000000E+00 0.0000000E+00 -304.9132     -58.70627     0.0000000E+00 002102\n" +
                            "  -4.800116    -0.3382549     0.0000000E+00 0.0000000E+00 -3.154423     200003\n" +
                            "  -23.71093     -5.751303     0.0000000E+00 0.0000000E+00 -15.97056     110003\n" +
                            "   28.97075     -3.414939     0.0000000E+00 0.0000000E+00-0.9871308     020003\n" +
                            "  0.0000000E+00 0.0000000E+00  3.970091    -0.5455678     0.0000000E+00 101003\n" +
                            "  0.0000000E+00 0.0000000E+00  47.01623      3.723892     0.0000000E+00 011003\n" +
                            "   1.722729    -0.1139596     0.0000000E+00 0.0000000E+00  5.651585     002003\n" +
                            "  -421.0366     -60.81285     0.0000000E+00 0.0000000E+00 -1461.466     100400\n" +
                            "  -1031.827     -145.5730     0.0000000E+00 0.0000000E+00 -3990.619     010400\n" +
                            "  0.0000000E+00 0.0000000E+00 -3867.541     -1309.581     0.0000000E+00 001400\n" +
                            "  0.0000000E+00 0.0000000E+00  1697.848      479.0817     0.0000000E+00 100301\n" +
                            "  0.0000000E+00 0.0000000E+00  5267.511      1524.621     0.0000000E+00 010301\n" +
                            "   1218.769      319.1501     0.0000000E+00 0.0000000E+00  983.0517     001301\n" +
                            "   41.72299      38.63250     0.0000000E+00 0.0000000E+00 -237.4995     100202\n" +
                            "   262.7834      154.6563     0.0000000E+00 0.0000000E+00 -762.5656     010202\n" +
                            "  0.0000000E+00 0.0000000E+00 -989.1749     -232.7833     0.0000000E+00 001202\n" +
                            "  0.0000000E+00 0.0000000E+00  48.52165      4.749452     0.0000000E+00 100103\n" +
                            "  0.0000000E+00 0.0000000E+00  291.5840      45.52587     0.0000000E+00 010103\n" +
                            "   19.52104      1.150430     0.0000000E+00 0.0000000E+00  35.34131     001103\n" +
                            "   2.106093     0.3607478     0.0000000E+00 0.0000000E+00 0.2238629     100004\n" +
                            "   8.846872      2.538694     0.0000000E+00 0.0000000E+00-0.3096682E-01 010004\n" +
                            "  0.0000000E+00 0.0000000E+00 -2.844087    -0.2509868     0.0000000E+00 001004\n" +
                            "  0.0000000E+00 0.0000000E+00 -1960.565     -687.0272     0.0000000E+00 000500\n" +
                            "   950.2013      262.6665     0.0000000E+00 0.0000000E+00  526.2560     000401\n" +
                            "  0.0000000E+00 0.0000000E+00 -1088.966     -287.2806     0.0000000E+00 000302\n" +
                            "   38.67233      4.458075     0.0000000E+00 0.0000000E+00  45.06013     000203\n" +
                            "  0.0000000E+00 0.0000000E+00 -18.72233     -2.992268     0.0000000E+00 000104\n" +
                            "  0.8849592E-02-0.1783390E-01 0.0000000E+00 0.0000000E+00 0.6216201     000005"
            );
            slice_后偏转段仅CCT1和前后2点4米漂移段.map = CosyArbitraryOrder.readMap(
                    "  -4.940276     -2.353096     0.0000000E+00 0.0000000E+00-0.6158405     100000\n" +
                            "  -13.56320     -6.662687     0.0000000E+00 0.0000000E+00 -1.791823     010000\n" +
                            "  0.0000000E+00 0.0000000E+00 -1.657770    -0.7910455     0.0000000E+00 001000\n" +
                            "  0.0000000E+00 0.0000000E+00 0.9720287    -0.1393927     0.0000000E+00 000100\n" +
                            "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00  1.000000     000010\n" +
                            "  0.4993329     0.1131794     0.0000000E+00 0.0000000E+00  1.043625     000001\n" +
                            "  -12.63262     -5.272850     0.0000000E+00 0.0000000E+00 -4.319618     200000\n" +
                            "  -80.68983     -33.31190     0.0000000E+00 0.0000000E+00 -24.32088     110000\n" +
                            "  -129.0806     -52.79899     0.0000000E+00 0.0000000E+00 -35.19782     020000\n" +
                            "  0.0000000E+00 0.0000000E+00 -13.20174     -4.319297     0.0000000E+00 101000\n" +
                            "  0.0000000E+00 0.0000000E+00 -31.92383     -10.25671     0.0000000E+00 011000\n" +
                            "   1.884019      1.229621     0.0000000E+00 0.0000000E+00-0.3142549     002000\n" +
                            "  0.0000000E+00 0.0000000E+00 -23.69157     -7.662341     0.0000000E+00 100100\n" +
                            "  0.0000000E+00 0.0000000E+00 -54.41971     -17.26940     0.0000000E+00 010100\n" +
                            "   10.66657      6.302922     0.0000000E+00 0.0000000E+00 0.4223457     001100\n" +
                            "  -1.445038     -1.949940     0.0000000E+00 0.0000000E+00 0.4720452     100001\n" +
                            "   3.500561     -1.737237     0.0000000E+00 0.0000000E+00  2.264439     010001\n" +
                            "  0.0000000E+00 0.0000000E+00  3.905980      1.001214     0.0000000E+00 001001\n" +
                            "   12.70288      7.138532     0.0000000E+00 0.0000000E+00-0.6491709E-01 000200\n" +
                            "  0.0000000E+00 0.0000000E+00  8.741946      3.255942     0.0000000E+00 000101\n" +
                            " -0.1973452E-01 0.1203669     0.0000000E+00 0.0000000E+00-0.8919995     000002\n" +
                            "  -48.92311     -11.94843     0.0000000E+00 0.0000000E+00 -17.15447     300000\n" +
                            "  -442.9121     -113.3157     0.0000000E+00 0.0000000E+00 -158.6864     210000\n" +
                            "  -1338.605     -356.8771     0.0000000E+00 0.0000000E+00 -487.9202     120000\n" +
                            "  -1360.348     -378.1125     0.0000000E+00 0.0000000E+00 -499.7437     030000\n" +
                            "  0.0000000E+00 0.0000000E+00 -9.139704    -0.7648476     0.0000000E+00 201000\n" +
                            "  0.0000000E+00 0.0000000E+00 -67.11970     -9.370279     0.0000000E+00 111000\n" +
                            "  0.0000000E+00 0.0000000E+00 -117.0248     -20.53150     0.0000000E+00 021000\n" +
                            "   10.17696      3.787435     0.0000000E+00 0.0000000E+00 -2.171893     102000\n" +
                            "   39.61048      15.03743     0.0000000E+00 0.0000000E+00 -3.245960     012000\n" +
                            "  0.0000000E+00 0.0000000E+00 -22.01174     -7.876387     0.0000000E+00 003000\n" +
                            "  0.0000000E+00 0.0000000E+00  14.99241      7.662382     0.0000000E+00 200100\n" +
                            "  0.0000000E+00 0.0000000E+00  57.13842      35.11235     0.0000000E+00 110100\n" +
                            "  0.0000000E+00 0.0000000E+00  41.93894      37.75149     0.0000000E+00 020100\n" +
                            "   62.54752      20.90616     0.0000000E+00 0.0000000E+00  4.864597     101100\n" +
                            "   225.5268      78.70484     0.0000000E+00 0.0000000E+00  22.43342     011100\n" +
                            "  0.0000000E+00 0.0000000E+00 -160.1552     -58.51176     0.0000000E+00 002100\n" +
                            "  -6.458103     -6.344937     0.0000000E+00 0.0000000E+00 0.1166561     200001\n" +
                            "   3.083093     -21.69911     0.0000000E+00 0.0000000E+00  14.61448     110001\n" +
                            "   75.62601     -5.359897     0.0000000E+00 0.0000000E+00  41.81132     020001\n" +
                            "  0.0000000E+00 0.0000000E+00  12.39081      2.315609     0.0000000E+00 101001\n" +
                            "  0.0000000E+00 0.0000000E+00  50.49339      12.27232     0.0000000E+00 011001\n" +
                            "   1.032000     0.6417567     0.0000000E+00 0.0000000E+00  1.757262     002001\n" +
                            "   81.08500      27.29947     0.0000000E+00 0.0000000E+00  14.64306     100200\n" +
                            "   275.8924      95.41620     0.0000000E+00 0.0000000E+00  45.16110     010200\n" +
                            "  0.0000000E+00 0.0000000E+00 -403.1616     -147.2223     0.0000000E+00 001200\n" +
                            "  0.0000000E+00 0.0000000E+00  45.95771      12.63320     0.0000000E+00 100101\n" +
                            "  0.0000000E+00 0.0000000E+00  149.1103      43.27436     0.0000000E+00 010101\n" +
                            "  -2.554842    -0.7132496     0.0000000E+00 0.0000000E+00  3.852945     001101\n" +
                            "   1.135781     0.4002522     0.0000000E+00 0.0000000E+00 0.6025543E-01 100002\n" +
                            " -0.6346286      1.154644     0.0000000E+00 0.0000000E+00 -1.203214     010002\n" +
                            "  0.0000000E+00 0.0000000E+00 -4.295385    -0.8096137     0.0000000E+00 001002\n" +
                            "  0.0000000E+00 0.0000000E+00 -346.9927     -127.0788     0.0000000E+00 000300\n" +
                            "  -12.00064     -5.709105     0.0000000E+00 0.0000000E+00  1.148553     000201\n" +
                            "  0.0000000E+00 0.0000000E+00 -15.11598     -3.985250     0.0000000E+00 000102\n" +
                            " -0.1963685    -0.1229425     0.0000000E+00 0.0000000E+00 0.7600605     000003\n" +
                            "  -100.0954      6.439522     0.0000000E+00 0.0000000E+00 -73.44612     400000\n" +
                            "  -1288.051      35.42720     0.0000000E+00 0.0000000E+00 -898.6519     310000\n" +
                            "  -6153.005     -11.58340     0.0000000E+00 0.0000000E+00 -4123.878     220000\n" +
                            "  -12988.24     -347.5093     0.0000000E+00 0.0000000E+00 -8428.564     130000\n" +
                            "  -10270.44     -508.1583     0.0000000E+00 0.0000000E+00 -6483.497     040000\n" +
                            "  0.0000000E+00 0.0000000E+00 -126.6697     -28.92991     0.0000000E+00 301000\n" +
                            "  0.0000000E+00 0.0000000E+00 -1144.366     -269.8927     0.0000000E+00 211000\n" +
                            "  0.0000000E+00 0.0000000E+00 -3444.562     -834.0798     0.0000000E+00 121000\n" +
                            "  0.0000000E+00 0.0000000E+00 -3470.824     -860.0242     0.0000000E+00 031000\n" +
                            "  -11.29990     -7.052452     0.0000000E+00 0.0000000E+00  3.369374     202000\n" +
                            "  -38.18969     -31.12371     0.0000000E+00 0.0000000E+00  42.04716     112000\n" +
                            "   7.014135     -20.74586     0.0000000E+00 0.0000000E+00  91.35412     022000\n" +
                            "  0.0000000E+00 0.0000000E+00 -23.50854     -6.419484     0.0000000E+00 103000\n" +
                            "  0.0000000E+00 0.0000000E+00 -71.41350     -21.09310     0.0000000E+00 013000\n" +
                            "  -18.00113     -6.275083     0.0000000E+00 0.0000000E+00 -10.95513     004000\n" +
                            "  0.0000000E+00 0.0000000E+00 -162.4228     -40.53357     0.0000000E+00 300100\n" +
                            "  0.0000000E+00 0.0000000E+00 -1478.331     -390.0672     0.0000000E+00 210100\n" +
                            "  0.0000000E+00 0.0000000E+00 -4488.756     -1238.152     0.0000000E+00 120100\n" +
                            "  0.0000000E+00 0.0000000E+00 -4565.157     -1304.679     0.0000000E+00 030100\n" +
                            "  -20.47237     -51.56392     0.0000000E+00 0.0000000E+00  70.23710     201100\n" +
                            "   19.56825     -239.1875     0.0000000E+00 0.0000000E+00  516.9284     111100\n" +
                            "   335.5046     -213.7738     0.0000000E+00 0.0000000E+00  898.1464     021100\n" +
                            "  0.0000000E+00 0.0000000E+00 -136.1756     -48.16821     0.0000000E+00 102100\n" +
                            "  0.0000000E+00 0.0000000E+00 -465.5943     -166.9216     0.0000000E+00 012100\n" +
                            "  -158.5861     -52.46803     0.0000000E+00 0.0000000E+00 -86.55495     003100\n" +
                            "  -21.07631     -15.17349     0.0000000E+00 0.0000000E+00 -5.582165     300001\n" +
                            "   65.22672     -75.91320     0.0000000E+00 0.0000000E+00  37.01660     210001\n" +
                            "   966.7339     -28.51775     0.0000000E+00 0.0000000E+00  384.6710     120001\n" +
                            "   1763.469      192.5087     0.0000000E+00 0.0000000E+00  671.3779     030001\n" +
                            "  0.0000000E+00 0.0000000E+00  8.341534     -1.303046     0.0000000E+00 201001\n" +
                            "  0.0000000E+00 0.0000000E+00  101.4496      1.924350     0.0000000E+00 111001\n" +
                            "  0.0000000E+00 0.0000000E+00  249.4113      25.55951     0.0000000E+00 021001\n" +
                            "   14.67384      7.423346     0.0000000E+00 0.0000000E+00  15.65544     102001\n" +
                            "   21.87982      16.57536     0.0000000E+00 0.0000000E+00  39.33098     012001\n" +
                            "  0.0000000E+00 0.0000000E+00  29.19522      5.862142     0.0000000E+00 003001\n" +
                            "  -58.95117     -88.92980     0.0000000E+00 0.0000000E+00  95.03491     200200\n" +
                            "  -210.7471     -445.3497     0.0000000E+00 0.0000000E+00  666.3500     110200\n" +
                            "  -2.718852     -495.3578     0.0000000E+00 0.0000000E+00  1120.752     020200\n" +
                            "  0.0000000E+00 0.0000000E+00 -312.2429     -111.9082     0.0000000E+00 101200\n" +
                            "  0.0000000E+00 0.0000000E+00 -1182.515     -420.6618     0.0000000E+00 011200\n" +
                            "  -513.8410     -158.7581     0.0000000E+00 0.0000000E+00 -247.1627     002200\n" +
                            "  0.0000000E+00 0.0000000E+00  26.07557      2.545410     0.0000000E+00 200101\n" +
                            "  0.0000000E+00 0.0000000E+00  172.4777      12.34098     0.0000000E+00 110101\n" +
                            "  0.0000000E+00 0.0000000E+00  339.1250      33.98734     0.0000000E+00 020101\n" +
                            "   9.824501      21.37596     0.0000000E+00 0.0000000E+00  48.88235     101101\n" +
                            "  -115.3814      22.26882     0.0000000E+00 0.0000000E+00  94.13659     011101\n" +
                            "  0.0000000E+00 0.0000000E+00  281.1455      72.00721     0.0000000E+00 002101\n" +
                            "   9.997835      1.648317     0.0000000E+00 0.0000000E+00  1.449935     200002\n" +
                            "   51.36740      19.26710     0.0000000E+00 0.0000000E+00 -2.719419     110002\n" +
                            "   24.72808      28.90165     0.0000000E+00 0.0000000E+00 -32.38001     020002\n" +
                            "  0.0000000E+00 0.0000000E+00 -7.862351    -0.1479454     0.0000000E+00 101002\n" +
                            "  0.0000000E+00 0.0000000E+00 -51.28059     -7.989005     0.0000000E+00 011002\n" +
                            " -0.9249010     0.5797290E-01 0.0000000E+00 0.0000000E+00 -4.392771     002002\n" +
                            "  0.0000000E+00 0.0000000E+00 -281.1615     -91.47066     0.0000000E+00 100300\n" +
                            "  0.0000000E+00 0.0000000E+00 -1106.883     -368.1667     0.0000000E+00 010300\n" +
                            "  -706.9570     -197.1786     0.0000000E+00 0.0000000E+00 -283.7198     001300\n" +
                            "  -68.36154      4.089673     0.0000000E+00 0.0000000E+00  17.75436     100201\n" +
                            "  -392.2246     -48.01456     0.0000000E+00 0.0000000E+00 -5.373941     010201\n" +
                            "  0.0000000E+00 0.0000000E+00  884.8687      250.8627     0.0000000E+00 001201\n" +
                            "  0.0000000E+00 0.0000000E+00 -51.97681     -9.283768     0.0000000E+00 100102\n" +
                            "  0.0000000E+00 0.0000000E+00 -223.1575     -49.73737     0.0000000E+00 010102\n" +
                            " -0.4881319      1.306026     0.0000000E+00 0.0000000E+00 -17.06162     001102\n" +
                            " -0.6371072     0.1021643E-01 0.0000000E+00 0.0000000E+00-0.3928138     100003\n" +
                            "  0.7683551    -0.1797351E-01 0.0000000E+00 0.0000000E+00-0.1726558     010003\n" +
                            "  0.0000000E+00 0.0000000E+00  4.791195     0.8167914     0.0000000E+00 001003\n" +
                            "  -336.3456     -76.81058     0.0000000E+00 0.0000000E+00 -91.74418     000400\n" +
                            "  0.0000000E+00 0.0000000E+00  925.2062      274.8792     0.0000000E+00 000301\n" +
                            "   11.12120      6.289455     0.0000000E+00 0.0000000E+00 -12.88773     000202\n" +
                            "  0.0000000E+00 0.0000000E+00  20.90560      4.666252     0.0000000E+00 000103\n" +
                            "  0.3847913     0.1429702     0.0000000E+00 0.0000000E+00-0.6664107     000004\n" +
                            "  -236.7262      131.4766     0.0000000E+00 0.0000000E+00 -202.5972     500000\n" +
                            "  -4253.455      1717.714     0.0000000E+00 0.0000000E+00 -3240.138     410000\n" +
                            "  -29341.97      8959.279     0.0000000E+00 0.0000000E+00 -20566.46     320000\n" +
                            "  -98683.92      23254.74     0.0000000E+00 0.0000000E+00 -64940.82     230000\n" +
                            "  -163333.3      29895.89     0.0000000E+00 0.0000000E+00 -102251.8     140000\n" +
                            "  -107096.4      15100.40     0.0000000E+00 0.0000000E+00 -64361.92     050000\n" +
                            "  0.0000000E+00 0.0000000E+00 -408.7751     -71.73285     0.0000000E+00 401000\n" +
                            "  0.0000000E+00 0.0000000E+00 -4956.741     -869.6741     0.0000000E+00 311000\n" +
                            "  0.0000000E+00 0.0000000E+00 -22520.20     -3965.432     0.0000000E+00 221000\n" +
                            "  0.0000000E+00 0.0000000E+00 -45487.16     -8063.348     0.0000000E+00 131000\n" +
                            "  0.0000000E+00 0.0000000E+00 -34517.01     -6178.457     0.0000000E+00 041000\n" +
                            "  -67.57325     -52.19030     0.0000000E+00 0.0000000E+00 -36.15887     302000\n" +
                            "  -466.1860     -469.3457     0.0000000E+00 0.0000000E+00 -260.8512     212000\n" +
                            "  -1014.733     -1408.773     0.0000000E+00 0.0000000E+00 -534.1833     122000\n" +
                            "  -611.2855     -1388.351     0.0000000E+00 0.0000000E+00 -235.4091     032000\n" +
                            "  0.0000000E+00 0.0000000E+00 -131.5156     -25.05040     0.0000000E+00 203000\n" +
                            "  0.0000000E+00 0.0000000E+00 -707.2923     -147.3822     0.0000000E+00 113000\n" +
                            "  0.0000000E+00 0.0000000E+00 -986.8299     -227.6550     0.0000000E+00 023000\n" +
                            "  -47.79169     -7.848410     0.0000000E+00 0.0000000E+00 -82.62968     104000\n" +
                            "  -179.5238     -40.73790     0.0000000E+00 0.0000000E+00 -222.2461     014000\n" +
                            "  0.0000000E+00 0.0000000E+00 -23.06142     -1.855938     0.0000000E+00 005000\n" +
                            "  0.0000000E+00 0.0000000E+00 -487.1490     -119.7165     0.0000000E+00 400100\n" +
                            "  0.0000000E+00 0.0000000E+00 -5834.083     -1417.565     0.0000000E+00 310100\n" +
                            "  0.0000000E+00 0.0000000E+00 -26218.36     -6342.563     0.0000000E+00 220100\n" +
                            "  0.0000000E+00 0.0000000E+00 -52403.81     -12694.93     0.0000000E+00 130100\n" +
                            "  0.0000000E+00 0.0000000E+00 -39344.87     -9591.007     0.0000000E+00 040100\n" +
                            "   95.21142     -249.9702     0.0000000E+00 0.0000000E+00  138.4060     301100\n" +
                            "   1531.779     -2213.410     0.0000000E+00 0.0000000E+00  1530.658     211100\n" +
                            "   6441.302     -6529.754     0.0000000E+00 0.0000000E+00  5688.161     121100\n" +
                            "   8427.167     -6301.774     0.0000000E+00 0.0000000E+00  7030.882     031100\n" +
                            "  0.0000000E+00 0.0000000E+00 -974.0414     -261.9590     0.0000000E+00 202100\n" +
                            "  0.0000000E+00 0.0000000E+00 -5325.661     -1496.847     0.0000000E+00 112100\n" +
                            "  0.0000000E+00 0.0000000E+00 -7644.536     -2262.820     0.0000000E+00 022100\n" +
                            "  -379.1295     -54.51903     0.0000000E+00 0.0000000E+00 -728.9339     103100\n" +
                            "  -1463.045     -310.9290     0.0000000E+00 0.0000000E+00 -1963.607     013100\n" +
                            "  0.0000000E+00 0.0000000E+00 -134.1416      2.823218     0.0000000E+00 004100\n" +
                            "  -125.1169     -18.38458     0.0000000E+00 0.0000000E+00 -36.30516     400001\n" +
                            "  -750.4225     -244.6712     0.0000000E+00 0.0000000E+00  77.81744     310001\n" +
                            "   372.3452     -1028.978     0.0000000E+00 0.0000000E+00  2732.756     220001\n" +
                            "   8918.299     -1599.507     0.0000000E+00 0.0000000E+00  10412.43     130001\n" +
                            "   13364.51     -619.9373     0.0000000E+00 0.0000000E+00  11709.40     040001\n" +
                            "  0.0000000E+00 0.0000000E+00  102.8174      7.045756     0.0000000E+00 301001\n" +
                            "  0.0000000E+00 0.0000000E+00  1598.246      222.6019     0.0000000E+00 211001\n" +
                            "  0.0000000E+00 0.0000000E+00  6797.055      1164.973     0.0000000E+00 121001\n" +
                            "  0.0000000E+00 0.0000000E+00  8835.420      1691.340     0.0000000E+00 031001\n" +
                            "   73.72921     0.4470194     0.0000000E+00 0.0000000E+00  62.63903     202001\n" +
                            "   417.9901      24.51758     0.0000000E+00 0.0000000E+00  337.6449     112001\n" +
                            "   518.4754      42.06673     0.0000000E+00 0.0000000E+00  423.1717     022001\n" +
                            "  0.0000000E+00 0.0000000E+00  65.99449      6.144492     0.0000000E+00 103001\n" +
                            "  0.0000000E+00 0.0000000E+00  203.2862      24.73603     0.0000000E+00 013001\n" +
                            "   16.49490      1.531589     0.0000000E+00 0.0000000E+00  34.71355     004001\n" +
                            "   144.9622     -337.3311     0.0000000E+00 0.0000000E+00  250.1824     300200\n" +
                            "   1870.541     -3024.046     0.0000000E+00 0.0000000E+00  2520.415     210200\n" +
                            "   7129.512     -9029.780     0.0000000E+00 0.0000000E+00  8622.222     120200\n" +
                            "   8836.517     -8844.833     0.0000000E+00 0.0000000E+00  9951.592     030200\n" +
                            "  0.0000000E+00 0.0000000E+00 -2737.969     -776.9783     0.0000000E+00 201200\n" +
                            "  0.0000000E+00 0.0000000E+00 -15272.91     -4367.045     0.0000000E+00 111200\n" +
                            "  0.0000000E+00 0.0000000E+00 -22478.19     -6557.696     0.0000000E+00 021200\n" +
                            "  -1108.813     -134.3800     0.0000000E+00 0.0000000E+00 -2444.975     102200\n" +
                            "  -4354.882     -853.6118     0.0000000E+00 0.0000000E+00 -6545.000     012200\n" +
                            "  0.0000000E+00 0.0000000E+00 -38.70848      137.0330     0.0000000E+00 003200\n" +
                            "  0.0000000E+00 0.0000000E+00  398.1854      61.96527     0.0000000E+00 300101\n" +
                            "  0.0000000E+00 0.0000000E+00  4508.258      811.5974     0.0000000E+00 210101\n" +
                            "  0.0000000E+00 0.0000000E+00  16316.19      3257.262     0.0000000E+00 120101\n" +
                            "  0.0000000E+00 0.0000000E+00  19237.76      4162.406     0.0000000E+00 030101\n" +
                            "   258.0502      15.40298     0.0000000E+00 0.0000000E+00  178.8392     201101\n" +
                            "   1253.337      212.3305     0.0000000E+00 0.0000000E+00  658.7388     111101\n" +
                            "   1049.811      323.5212     0.0000000E+00 0.0000000E+00  267.0204     021101\n" +
                            "  0.0000000E+00 0.0000000E+00  424.0639      81.06678     0.0000000E+00 102101\n" +
                            "  0.0000000E+00 0.0000000E+00  1395.738      305.2581     0.0000000E+00 012101\n" +
                            "   241.4504      45.43237     0.0000000E+00 0.0000000E+00  341.9385     003101\n" +
                            "   45.17297      8.292714     0.0000000E+00 0.0000000E+00  15.23833     300002\n" +
                            "   311.1791      101.8972     0.0000000E+00 0.0000000E+00  97.88487     210002\n" +
                            "   205.9284      272.9458     0.0000000E+00 0.0000000E+00  22.67507     120002\n" +
                            "  -989.8235      107.9145     0.0000000E+00 0.0000000E+00 -412.9893     030002\n" +
                            "  0.0000000E+00 0.0000000E+00  12.52834      6.428837     0.0000000E+00 201002\n" +
                            "  0.0000000E+00 0.0000000E+00 -11.78777      31.77783     0.0000000E+00 111002\n" +
                            "  0.0000000E+00 0.0000000E+00 -225.0524      16.80804     0.0000000E+00 021002\n" +
                            "  -38.54251     -8.564795     0.0000000E+00 0.0000000E+00 -29.93314     102002\n" +
                            "  -127.2680     -36.67739     0.0000000E+00 0.0000000E+00 -96.59752     012002\n" +
                            "  0.0000000E+00 0.0000000E+00 -48.64463     -9.179904     0.0000000E+00 003002\n" +
                            "  0.0000000E+00 0.0000000E+00 -2587.754     -721.6289     0.0000000E+00 200300\n" +
                            "  0.0000000E+00 0.0000000E+00 -14531.03     -3983.924     0.0000000E+00 110300\n" +
                            "  0.0000000E+00 0.0000000E+00 -21617.51     -5945.855     0.0000000E+00 020300\n" +
                            "  -1224.861     -97.79028     0.0000000E+00 0.0000000E+00 -3620.904     101300\n" +
                            "  -4982.740     -853.0701     0.0000000E+00 0.0000000E+00 -9518.988     011300\n" +
                            "  0.0000000E+00 0.0000000E+00  1133.745      612.1163     0.0000000E+00 002300\n" +
                            "   175.9024      46.09913     0.0000000E+00 0.0000000E+00  55.62103     200201\n" +
                            "   767.1070      443.4292     0.0000000E+00 0.0000000E+00 -234.0445     110201\n" +
                            "   263.4643      686.3872     0.0000000E+00 0.0000000E+00 -1286.344     020201\n" +
                            "  0.0000000E+00 0.0000000E+00  987.1296      280.2816     0.0000000E+00 101201\n" +
                            "  0.0000000E+00 0.0000000E+00  3650.120      1052.897     0.0000000E+00 011201\n" +
                            "   1117.518      244.3020     0.0000000E+00 0.0000000E+00  1222.698     002201\n" +
                            "  0.0000000E+00 0.0000000E+00 -11.27448      8.730250     0.0000000E+00 200102\n" +
                            "  0.0000000E+00 0.0000000E+00 -230.2964      30.80361     0.0000000E+00 110102\n" +
                            "  0.0000000E+00 0.0000000E+00 -716.9305     -22.63312     0.0000000E+00 020102\n" +
                            "  -164.0930     -51.39082     0.0000000E+00 0.0000000E+00 -147.4360     101102\n" +
                            "  -402.3835     -170.9295     0.0000000E+00 0.0000000E+00 -406.2856     011102\n" +
                            "  0.0000000E+00 0.0000000E+00 -485.5465     -110.4395     0.0000000E+00 002102\n" +
                            "  -7.498694     0.3122058     0.0000000E+00 0.0000000E+00 -1.931524     200003\n" +
                            "  -70.65772     -10.13506     0.0000000E+00 0.0000000E+00 -5.662802     110003\n" +
                            "  -110.9820     -32.25890     0.0000000E+00 0.0000000E+00  18.88181     020003\n" +
                            "  0.0000000E+00 0.0000000E+00  3.448305    -0.5986794     0.0000000E+00 101003\n" +
                            "  0.0000000E+00 0.0000000E+00  42.56556      4.118809     0.0000000E+00 011003\n" +
                            " -0.4181775    -0.7999837     0.0000000E+00 0.0000000E+00  7.910736     002003\n" +
                            "  -249.7497      36.96090     0.0000000E+00 0.0000000E+00 -1954.776     100400\n" +
                            "  -1297.515     -114.1515     0.0000000E+00 0.0000000E+00 -4967.786     010400\n" +
                            "  0.0000000E+00 0.0000000E+00  2456.861      1027.536     0.0000000E+00 001400\n" +
                            "  0.0000000E+00 0.0000000E+00  932.9547      296.1695     0.0000000E+00 100301\n" +
                            "  0.0000000E+00 0.0000000E+00  3757.508      1144.993     0.0000000E+00 010301\n" +
                            "   2049.057      457.1106     0.0000000E+00 0.0000000E+00  1844.842     001301\n" +
                            "  -99.95002     -57.35469     0.0000000E+00 0.0000000E+00 -133.7254     100202\n" +
                            "  -28.99686     -140.9674     0.0000000E+00 0.0000000E+00 -293.4404     010202\n" +
                            "  0.0000000E+00 0.0000000E+00 -1628.781     -415.3273     0.0000000E+00 001202\n" +
                            "  0.0000000E+00 0.0000000E+00  49.08659      6.214677     0.0000000E+00 100103\n" +
                            "  0.0000000E+00 0.0000000E+00  266.2592      47.65559     0.0000000E+00 010103\n" +
                            "  -2.546709     -4.581039     0.0000000E+00 0.0000000E+00  39.98308     001103\n" +
                            "  0.1625616    -0.1437932     0.0000000E+00 0.0000000E+00 0.6751266     100004\n" +
                            "  -2.474114    -0.9502553     0.0000000E+00 0.0000000E+00  1.767729     010004\n" +
                            "  0.0000000E+00 0.0000000E+00 -5.384274    -0.8747520     0.0000000E+00 001004\n" +
                            "  0.0000000E+00 0.0000000E+00  1430.023      588.5295     0.0000000E+00 000500\n" +
                            "   1253.375      257.8018     0.0000000E+00 0.0000000E+00  945.3283     000401\n" +
                            "  0.0000000E+00 0.0000000E+00 -1851.813     -501.0403     0.0000000E+00 000302\n" +
                            "  -14.88868     -10.23277     0.0000000E+00 0.0000000E+00  41.57358     000203\n" +
                            "  0.0000000E+00 0.0000000E+00 -26.80370     -5.409215     0.0000000E+00 000104\n" +
                            " -0.5800612    -0.1708637     0.0000000E+00 0.0000000E+00 0.6019997     000005"
            );
        }

        public CosyArbitraryOrder.CosyMapArbitraryOrder map;
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
    private double agCct345BigR = 0.7512 * 0 + 0.75; //2020年5月28日

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

    private double agCct345SmallRInner = 23.5 * MM + 40 * MM;
    private double agCct345SmallROuter = 33.5 * MM + 40 * MM;
    private final double dipoleCct345SmallRInner = 43 * MM + 40 * MM;
    private final double dipoleCct345SmallROuter = 52 * MM + 40 * MM;

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
    private double dipoleCct345A1Inner = Math.pow(dipoleCct345SmallRInner, 2) * 0.250;
    private double dipoleCct345A1Outer = -Math.pow(dipoleCct345SmallROuter, 2) * 0.250;
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
    private double[] agCct345A1Inners = BaseUtils.ArrayUtils.repeat(-Math.pow(agCct345SmallRInner, 2) * Math.sqrt(3) * 20 * 0.19, 3);
    private double[] agCct345A1Outers = BaseUtils.ArrayUtils.repeat(Math.pow(agCct345SmallROuter, 2) * Math.sqrt(3) * 20 * 0.19, 3);
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


    private double dipoleCct345IInner = -10.017 * kA;
    private double dipoleCct345IOuter = dipoleCct345IInner;
    private double agCct345IInner = 4618.911905272398 / 45 * 18.14 / 0.19; // 9799 // 2020年6月11日 *0.995
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
}
