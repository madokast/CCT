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
import cn.edu.hust.zrx.cct.base.line.*;
import cn.edu.hust.zrx.cct.base.opticsMagnet.NoMagnet;
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
import java.util.*;
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

        Logger.getLogger().info("trajectoryPart2.getLength() = " + trajectoryPart2.getLength());

        MagnetAble elementsOfAllPart2 = getElementsOfAllPart2();

//         trackingIdealParticle(trajectoryPart2, trajectoryPart2.getLength()-DL2, elementsOfAllPart2);
        List<Point3> point3List = trackingIdealParticle(trajectoryPart2, trajectoryPart2.getLength(), elementsOfAllPart2);

//        Collections.reverse(point3List);

        Line3 trajectory3 = DiscretePointLine3.create(point3List);

        List<String> script = elementsOfAllPart2.sliceToCosyScript(Bp,
                60 * MM, trajectory3,
                20 * MM,
                1 * MM, 1000000 * MM,
                0.2);

//        Collections.reverse(script);

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
    public void CCT675_2出口相椭圆10() {
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

    @run(1001)
    public void CCT675_2出口相椭圆15() {
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
                trajectoryPart2.getLength() - DL2 + 1.5, false, 0.05,
                32, true, 1, elementsOfAllPart2, trajectoryPart2,
                512, 5, List.of(
                        BaseUtils.Content.BiContent.create("optics", CosyMap.optics_后偏转段CCT2出口加1点5米.map)
                )
        );
    }

    @run(1002)
    public void CCT675_2出口相椭圆20() {
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
                trajectoryPart2.getLength() - DL2 + 2.0, false, 0.05,
                32, true, 1, elementsOfAllPart2, trajectoryPart2,
                512, 5, List.of(
                        BaseUtils.Content.BiContent.create("optics", CosyMap.optics_后偏转段CCT2出口加2点0米.map)
                )
        );
    }

    @run(1003)
    public void CCT675_2出口相椭圆24() {
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
                trajectoryPart2.getLength(), false, 0.05,
                32, true, 1, elementsOfAllPart2, trajectoryPart2,
                512, 5, List.of(
                        BaseUtils.Content.BiContent.create("optics", CosyMap.optics_后偏转段CCT2出口加2点4米.map)
                )
        );
    }

    @run(1004)
    public void CCT675_2出口相椭圆XX() {
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
                0.1, false, 0.05,
                32, true, 1, elementsOfAllPart2, trajectoryPart2,
                512, 5, List.of(
                        BaseUtils.Content.BiContent.create("optics", CosyMap.optics_后偏转段CCT2出口加2点4米.map)
                )
        );
    }


    // -------- 继续像椭圆对比 加上 slice 原map
    @run(2000)
    public void 第二段出口前00() {
        MagnetAble elementsOfAllPart2 = getElementsOfAllPart2();

        Trajectory trajectoryPart2 = getTrajectoryPart2();

        phase相椭圆画图(
                trajectoryPart2.getLength(), false, 0.05,
                32, true, 1, elementsOfAllPart2, trajectoryPart2,
                512, 5, List.of(
                        BaseUtils.Content.BiContent.create("slice", CosyMap.slice_20200715_后偏转段全段.map),
                        BaseUtils.Content.BiContent.create("optic", CosyMap.optics_后偏转段CCT2出口加2点4米.map)
                )
        );
    }

    @run(2001)
    public void 第二段出口前05() {
        MagnetAble elementsOfAllPart2 = getElementsOfAllPart2();

        Trajectory trajectoryPart2 = getTrajectoryPart2();

        phase相椭圆画图(
                trajectoryPart2.getLength() - DL2 + 1.5, false, 0.05,
                32, true, 1, elementsOfAllPart2, trajectoryPart2,
                512, 5, List.of(
                        BaseUtils.Content.BiContent.create("slice", CosyMap.slice_20200715_后偏转段_最后偏移段15.map),
                        BaseUtils.Content.BiContent.create("optic", CosyMap.optics_后偏转段CCT2出口加1点5米.map)
                )
        );
    }

    @run(2002)
    public void 偏转段CCT2出口加1点0米() {
        MagnetAble elementsOfAllPart2 = getElementsOfAllPart2();

        Trajectory trajectoryPart2 = getTrajectoryPart2();

        phase相椭圆画图(
                trajectoryPart2.getLength() - DL2 + 1.0, false, 0.05,
                32, true, 1, elementsOfAllPart2, trajectoryPart2,
                512, 5, List.of(
                        BaseUtils.Content.BiContent.create("slice", CosyMap.slice_20200715_后偏转段_最后偏移段10.map),
                        BaseUtils.Content.BiContent.create("optic", CosyMap.optics_后偏转段CCT2出口加1点0米.map)
                )
        );
    }

    @run(2003)
    public void 偏转段CCT2出口加0点5米() {
        MagnetAble elementsOfAllPart2 = getElementsOfAllPart2();

        Trajectory trajectoryPart2 = getTrajectoryPart2();

        phase相椭圆画图(
                trajectoryPart2.getLength() - DL2 + 0.5, false, 0.05,
                32, true, 1, elementsOfAllPart2, trajectoryPart2,
                512, 5, List.of(
                        BaseUtils.Content.BiContent.create("slice", CosyMap.slice_20200715_后偏转段_最后偏移段05.map),
                        BaseUtils.Content.BiContent.create("optic", CosyMap.optics_后偏转段CCT2出口加0点5米.map)
                )
        );
    }

    @run(2004)
    public void 偏转段CCT2出口加0点0米() {
        MagnetAble elementsOfAllPart2 = getElementsOfAllPart2();

        Trajectory trajectoryPart2 = getTrajectoryPart2();

        phase相椭圆画图(
                trajectoryPart2.getLength() - DL2 + 0.0, true, 0.05,
                32, true, 1, elementsOfAllPart2, trajectoryPart2,
                512, 5, List.of(
                        BaseUtils.Content.BiContent.create("slice", CosyMap.slice_20200715_后偏转段_最后偏移段00.map),
                        BaseUtils.Content.BiContent.create("optic", CosyMap.optics_后偏转段CCT2出口.map)
                )
        );
    }

    @run(2010)
    public void 偏转段CCT2出口后无磁场() {
        MagnetAble elementsOfAllPart2 = getElementsOfAllPart2();

        NoMagnet noMagnet = NoMagnet.create();

        Trajectory trajectoryPart2 = getTrajectoryPart2();

        boolean xPlane = true;

        double delta = 0.00;

        int number = 32;

        double distancePre = trajectoryPart2.getLength() - DL2 + 0.4;
        double distancePost = DL2 - 0.4;

        double distance = trajectoryPart2.getLength();
        /*---------------------------------------------------*/


        RunningParticle ip = ParticleFactory.createIdealProtonAtTrajectory250MeV(trajectoryPart2);

        List<PhaseSpaceParticle> pp = PhaseSpaceParticles.phaseSpaceParticlesAlongPositiveEllipseInPlane(
                xPlane, 3.5 * MM * 1, 7.5 * MM * 1, delta, number);

        List<RunningParticle> ps = ParticleFactory.createParticlesFromPhaseSpaceParticle(ip, ip.computeNaturalCoordinateSystem(), pp);

        ParticleRunner.runThread(ps, elementsOfAllPart2, distancePre, MM);

        ParticleRunner.runThread(ps, noMagnet, distancePost, MM);

        RunningParticle ipEnd = ParticleFactory.createIdealProtonAtTrajectory250MeV(trajectoryPart2, distance);

        List<PhaseSpaceParticle> ppEnd = PhaseSpaceParticles.phaseSpaceParticlesFromRunningParticles(
                ipEnd, ipEnd.computeNaturalCoordinateSystem(), ps);

        List<Point2> projectionToPlaneCOSY = PhaseSpaceParticles.projectionToPlane(xPlane, ppEnd);

        Vector2 average = Point2.average(projectionToPlaneCOSY).toVector2().reverseSelf();

        projectionToPlaneCOSY.forEach(point2 -> point2.moveSelf(average));

        // 改单位
        List<Point2> tracked = Point2.convert(projectionToPlaneCOSY, 1 / MM, 1 / MRAD);

        /*----------------------------------------------*/

        List<Point2> cosyPhaseSlice = cosy相椭圆(xPlane, delta, 512, 5, false, 1, CosyMap.slice3d_后偏转段全部.map);
        List<Point2> cosyPhaseOptics = cosy相椭圆(xPlane, delta, 512, 5, false, 1, CosyMap.optics_后偏转段CCT2出口加2点4米.map);

        /*----------------------------------------------*/

        List<List<Point2>> lists = List.of(tracked, cosyPhaseSlice, cosyPhaseOptics);

        List<String> mapInfo = List.of("track noMagnet", "slice", "optics");

        Plot2d.plot2(tracked, Plot2d.BLACK_POINT);

        for (int i = 1; i < lists.size(); i++) {
            List<Point2> list1 = lists.get(i);
            Plot2d.plot2(list1, switcher.getAndSwitch());
        }

        if (xPlane)
            Plot2d.info("x/mm", "xp/mrad", "x/xp dp=" + delta / PRESENT + "%, s=" + distance + "m", 18);
        else
            Plot2d.info("y/mm", "yp/mrad", "y/yp dp=" + delta / PRESENT + "%, s=" + distance + "m", 18);

        List<String> info = mapInfo.stream().collect(Collectors.toList());

        Plot2d.legend(18, info.toArray(String[]::new));

        Plot2d.equal();

        Plot2d.showThread();
    }


    //----------------2020年7月7日--------------------
    @run(-10001)
    public void 磁场分布() {
        MagnetAble elementsOfAllPart2 = getElementsOfAllPart2();

        Trajectory trajectoryPart2 = getTrajectoryPart2();

        {
            //final Line2 trajectory, final double deltaLength, final double goodFieldAreaWidth,
            //            final int order, final int dotNumber
            List<List<Point2>> lists = elementsOfAllPart2.multiplePoleMagnetAlongTrajectoryBreak(
                    trajectoryPart2, MM, 20 * MM, 4, 12
            );

            List<Point2> bz = lists.get(3);

            Plot2d.plot2(bz, Plot2d.BLACK_LINE);
        }// 二极场 理想轨道

        {
            RunningParticle p = ParticleFactory.createIdealProtonAtTrajectory250MeV(trajectoryPart2);

            List<Point3> tra = ParticleRunner.run(p, elementsOfAllPart2, trajectoryPart2.getLength(), MM);

            Line3 tra3 = DiscretePointLine3.create(tra);

            //final Line3 trajectory, final double deltaLength, final double goodFieldAreaWidth,
            //            final int order, final int dotNumber
            List<List<Point2>> lists = elementsOfAllPart2.multiplePoleMagnetAlongTrajectoryBreak(
                    tra3, MM, 20 * MM, 4, 12
            );

            List<Point2> bz = lists.get(3);

            Plot2d.plot2(bz, Plot2d.RED_LINE);

        }// 二极场 实际轨道


//        Plot2d.info("s/m","B/T","Field along trajectory",18);
//        Plot2d.info("s/m", "G/(T/m)", "Field along trajectory", 18);
        Plot2d.info("s/m", "L/(T/m2)", "Field along trajectory", 18);

        Plot2d.legend(18, "indel traj", "real traj");

        Plot2d.showThread();
    }

    @run(-10002)
    public void 磁场分布_介绍0713() {
        Elements elementsOfAllPart1 = getElementsOfAllPart1();
        Trajectory trajectoryPart1 = getTrajectoryPart1();

        List<List<Point2>> lists = elementsOfAllPart1.multiplePoleMagnetAlongTrajectoryBreak(trajectoryPart1,
                trajectoryPart1.getLength(), 10 * MM, 1, 4);


        List<Point2> bx = lists.get(0);
        List<Point2> g = lists.get(1);
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
        optics_后偏转段CCT2出口加2点0米, optics_后偏转段CCT2出口加2点4米,
        slice_20200715_后偏转段全段, slice_20200715_后偏转段_最后偏移段20,
        slice_20200715_后偏转段_最后偏移段15, slice_20200715_后偏转段_最后偏移段10,
        slice_20200715_后偏转段_最后偏移段05, slice_20200715_后偏转段_最后偏移段00;

        static {
            slice_20200715_后偏转段_最后偏移段00.map = CosyArbitraryOrder.readMap(
                    " -0.5004004     0.7182201     0.0000000E+00 0.0000000E+00 0.1956850E-01 100000\n" +
                            "  -2.195638      1.152980     0.0000000E+00 0.0000000E+00 0.3404278E-01 010000\n" +
                            "  0.0000000E+00 0.0000000E+00 -1.321234     0.8248457E-01 0.0000000E+00 001000\n" +
                            "  0.0000000E+00 0.0000000E+00  2.465952    -0.9108178     0.0000000E+00 000100\n" +
                            "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00  1.000000     000010\n" +
                            "  0.2593034E-01 0.1888118E-02 0.0000000E+00 0.0000000E+00 0.8725335     000001\n" +
                            "  -6.359273     -1.104255     0.0000000E+00 0.0000000E+00 0.7817876     200000\n" +
                            "  -29.14138      2.830954     0.0000000E+00 0.0000000E+00  4.930617     110000\n" +
                            "  -31.44763      17.77472     0.0000000E+00 0.0000000E+00  6.807920     020000\n" +
                            "  0.0000000E+00 0.0000000E+00 -15.09395     0.9215201     0.0000000E+00 101000\n" +
                            "  0.0000000E+00 0.0000000E+00 -38.31162     0.6730656E-01 0.0000000E+00 011000\n" +
                            "  0.7382500     -1.087056     0.0000000E+00 0.0000000E+00-0.9553630     002000\n" +
                            "  0.0000000E+00 0.0000000E+00 -36.53639      10.96635     0.0000000E+00 100100\n" +
                            "  0.0000000E+00 0.0000000E+00 -86.51260      31.68621     0.0000000E+00 010100\n" +
                            "   7.817488      11.71210     0.0000000E+00 0.0000000E+00  1.165972     001100\n" +
                            "   1.103041      1.607916     0.0000000E+00 0.0000000E+00 0.3582441E-01 100001\n" +
                            "   4.251192      3.494992     0.0000000E+00 0.0000000E+00 0.3685511E-01 010001\n" +
                            "  0.0000000E+00 0.0000000E+00  3.407099     -1.614094     0.0000000E+00 001001\n" +
                            "   6.680200     -3.357508     0.0000000E+00 0.0000000E+00 -5.557957     000200\n" +
                            "  0.0000000E+00 0.0000000E+00  12.26061    -0.1016304     0.0000000E+00 000101\n" +
                            "  0.7288129E-02-0.1424429E-01 0.0000000E+00 0.0000000E+00-0.7713578     000002\n" +
                            "  -12.47249      11.91120     0.0000000E+00 0.0000000E+00  4.181033     300000\n" +
                            "  -145.1414      78.32789     0.0000000E+00 0.0000000E+00  34.89557     210000\n" +
                            "  -517.2974      161.1647     0.0000000E+00 0.0000000E+00  100.8812     120000\n" +
                            "  -582.5067      102.9999     0.0000000E+00 0.0000000E+00  100.8921     030000\n" +
                            "  0.0000000E+00 0.0000000E+00  15.57892     -44.76506     0.0000000E+00 201000\n" +
                            "  0.0000000E+00 0.0000000E+00  39.94885     -251.3069     0.0000000E+00 111000\n" +
                            "  0.0000000E+00 0.0000000E+00 -5.858421     -348.1983     0.0000000E+00 021000\n" +
                            "  -17.07968     -60.22557     0.0000000E+00 0.0000000E+00 -16.11851     102000\n" +
                            "  -48.91687     -164.8760     0.0000000E+00 0.0000000E+00 -40.81278     012000\n" +
                            "  0.0000000E+00 0.0000000E+00 -20.45137      19.64552     0.0000000E+00 003000\n" +
                            "  0.0000000E+00 0.0000000E+00  32.71876     -29.03069     0.0000000E+00 200100\n" +
                            "  0.0000000E+00 0.0000000E+00  40.46195     -178.8020     0.0000000E+00 110100\n" +
                            "  0.0000000E+00 0.0000000E+00 -136.5105     -251.9548     0.0000000E+00 020100\n" +
                            "  -57.25400     -60.01882     0.0000000E+00 0.0000000E+00 -10.99964     101100\n" +
                            "  -144.7160     -149.3246     0.0000000E+00 0.0000000E+00 -23.81362     011100\n" +
                            "  0.0000000E+00 0.0000000E+00 -211.1600     -28.56865     0.0000000E+00 002100\n" +
                            "  -5.500780     -5.031257     0.0000000E+00 0.0000000E+00-0.4384138E-01 200001\n" +
                            "  -12.19467     -22.18746     0.0000000E+00 0.0000000E+00 -2.278792     110001\n" +
                            " -0.4661035E-01 -36.22091     0.0000000E+00 0.0000000E+00 -5.544920     020001\n" +
                            "  0.0000000E+00 0.0000000E+00 -7.622536     -1.932934     0.0000000E+00 101001\n" +
                            "  0.0000000E+00 0.0000000E+00  7.748337      1.599533     0.0000000E+00 011001\n" +
                            "   2.066743      10.29154     0.0000000E+00 0.0000000E+00  4.965561     002001\n" +
                            "   25.02351      162.4119     0.0000000E+00 0.0000000E+00  74.71939     100200\n" +
                            "   27.52141      313.1695     0.0000000E+00 0.0000000E+00  178.4489     010200\n" +
                            "  0.0000000E+00 0.0000000E+00 -421.1866      232.4228     0.0000000E+00 001200\n" +
                            "  0.0000000E+00 0.0000000E+00 -3.377584     -14.67287     0.0000000E+00 100101\n" +
                            "  0.0000000E+00 0.0000000E+00  49.72464     -33.08744     0.0000000E+00 010101\n" +
                            "  -2.236688      7.794487     0.0000000E+00 0.0000000E+00  2.586724     001101\n" +
                            "  0.8937117E-01 0.4182831     0.0000000E+00 0.0000000E+00-0.3149350E-01 100002\n" +
                            "  -2.315173    -0.5919148     0.0000000E+00 0.0000000E+00-0.2748973E-01 010002\n" +
                            "  0.0000000E+00 0.0000000E+00-0.5150559E-01  3.558540     0.0000000E+00 001002\n" +
                            "  0.0000000E+00 0.0000000E+00 -438.8298     -73.13924     0.0000000E+00 000300\n" +
                            "  -27.40221     -80.94478     0.0000000E+00 0.0000000E+00 -14.68125     000201\n" +
                            "  0.0000000E+00 0.0000000E+00 -9.543930      8.705853     0.0000000E+00 000102\n" +
                            " -0.3956309E-01-0.7852002E-02 0.0000000E+00 0.0000000E+00 0.6865840     000003\n" +
                            "  -55.25781      45.67109     0.0000000E+00 0.0000000E+00  2.215719     400000\n" +
                            "  -694.0995      526.4122     0.0000000E+00 0.0000000E+00  56.11902     310000\n" +
                            "  -3323.845      2243.815     0.0000000E+00 0.0000000E+00  340.7058     220000\n" +
                            "  -7201.900      4142.119     0.0000000E+00 0.0000000E+00  785.3058     130000\n" +
                            "  -5938.059      2767.589     0.0000000E+00 0.0000000E+00  624.9948     040000\n" +
                            "  0.0000000E+00 0.0000000E+00 -41.41339     -267.4917     0.0000000E+00 301000\n" +
                            "  0.0000000E+00 0.0000000E+00 -295.6709     -2242.578     0.0000000E+00 211000\n" +
                            "  0.0000000E+00 0.0000000E+00 -744.3471     -6329.488     0.0000000E+00 121000\n" +
                            "  0.0000000E+00 0.0000000E+00 -702.1453     -6001.040     0.0000000E+00 031000\n" +
                            "   35.15835     -296.9841     0.0000000E+00 0.0000000E+00 -63.54913     202000\n" +
                            "   174.6419     -1667.670     0.0000000E+00 0.0000000E+00 -360.3461     112000\n" +
                            "   224.5056     -2335.853     0.0000000E+00 0.0000000E+00 -516.8502     022000\n" +
                            "  0.0000000E+00 0.0000000E+00  150.3260      401.1409     0.0000000E+00 103000\n" +
                            "  0.0000000E+00 0.0000000E+00  338.2178      968.2094     0.0000000E+00 013000\n" +
                            "  -15.67182     -59.73543     0.0000000E+00 0.0000000E+00 -29.46017     004000\n" +
                            "  0.0000000E+00 0.0000000E+00 -92.16265     -266.2128     0.0000000E+00 300100\n" +
                            "  0.0000000E+00 0.0000000E+00 -663.6503     -1890.030     0.0000000E+00 210100\n" +
                            "  0.0000000E+00 0.0000000E+00 -1783.425     -4580.561     0.0000000E+00 120100\n" +
                            "  0.0000000E+00 0.0000000E+00 -1882.210     -3825.309     0.0000000E+00 030100\n" +
                            "  -1.221602     -1017.501     0.0000000E+00 0.0000000E+00 -447.1526     201100\n" +
                            "   135.4860     -4941.477     0.0000000E+00 0.0000000E+00 -2321.054     111100\n" +
                            "   313.0971     -6080.364     0.0000000E+00 0.0000000E+00 -3048.393     021100\n" +
                            "  0.0000000E+00 0.0000000E+00 -68.60913     -709.8887     0.0000000E+00 102100\n" +
                            "  0.0000000E+00 0.0000000E+00 -455.8463     -2186.558     0.0000000E+00 012100\n" +
                            "  -68.18762     -360.8397     0.0000000E+00 0.0000000E+00 -112.0842     003100\n" +
                            "  -38.41365     -32.85391     0.0000000E+00 0.0000000E+00  4.099579     300001\n" +
                            "  -279.8869     -360.2841     0.0000000E+00 0.0000000E+00  7.632399     210001\n" +
                            "  -555.6402     -1192.677     0.0000000E+00 0.0000000E+00 -58.07709     120001\n" +
                            "  -209.6610     -1244.102     0.0000000E+00 0.0000000E+00 -138.1177     030001\n" +
                            "  0.0000000E+00 0.0000000E+00  19.65137      82.45150     0.0000000E+00 201001\n" +
                            "  0.0000000E+00 0.0000000E+00  57.36364      557.4906     0.0000000E+00 111001\n" +
                            "  0.0000000E+00 0.0000000E+00  73.65930      925.3016     0.0000000E+00 021001\n" +
                            "   28.41327      143.1158     0.0000000E+00 0.0000000E+00  57.80890     102001\n" +
                            "   119.7949      496.0571     0.0000000E+00 0.0000000E+00  171.7211     012001\n" +
                            "  0.0000000E+00 0.0000000E+00 -39.32817     -144.2605     0.0000000E+00 003001\n" +
                            "  -735.4382     -1669.801     0.0000000E+00 0.0000000E+00 -635.0864     200200\n" +
                            "  -2602.474     -6271.660     0.0000000E+00 0.0000000E+00 -2858.615     110200\n" +
                            "  -1788.960     -5078.153     0.0000000E+00 0.0000000E+00 -3145.168     020200\n" +
                            "  0.0000000E+00 0.0000000E+00 -482.6122     -2583.277     0.0000000E+00 101200\n" +
                            "  0.0000000E+00 0.0000000E+00 -1684.464     -6841.303     0.0000000E+00 011200\n" +
                            "  -188.9272     -78.19630     0.0000000E+00 0.0000000E+00 -193.4036     002200\n" +
                            "  0.0000000E+00 0.0000000E+00 -14.84617      266.0688     0.0000000E+00 200101\n" +
                            "  0.0000000E+00 0.0000000E+00 -139.1199      1414.086     0.0000000E+00 110101\n" +
                            "  0.0000000E+00 0.0000000E+00 -65.36672      1888.899     0.0000000E+00 020101\n" +
                            "   248.1422      830.9958     0.0000000E+00 0.0000000E+00  265.3821     101101\n" +
                            "   693.3043      2147.915     0.0000000E+00 0.0000000E+00  661.6197     011101\n" +
                            "  0.0000000E+00 0.0000000E+00  207.8116     -77.83777     0.0000000E+00 002101\n" +
                            "   2.488705     -1.931596     0.0000000E+00 0.0000000E+00 -1.214617     200002\n" +
                            "   16.21677    -0.1425359     0.0000000E+00 0.0000000E+00 -5.629503     110002\n" +
                            "   18.01188      17.73235     0.0000000E+00 0.0000000E+00 -6.187273     020002\n" +
                            "  0.0000000E+00 0.0000000E+00  5.880730     -4.428462     0.0000000E+00 101002\n" +
                            "  0.0000000E+00 0.0000000E+00 0.8718540     -12.07670     0.0000000E+00 011002\n" +
                            "  -4.511243     -18.40109     0.0000000E+00 0.0000000E+00 -13.83745     002002\n" +
                            "  0.0000000E+00 0.0000000E+00  1925.550      4508.635     0.0000000E+00 100300\n" +
                            "  0.0000000E+00 0.0000000E+00  4131.300      10055.59     0.0000000E+00 010300\n" +
                            "   408.7219      1943.738     0.0000000E+00 0.0000000E+00  733.1921     001300\n" +
                            "   523.9039      1090.707     0.0000000E+00 0.0000000E+00  217.6503     100201\n" +
                            "   1209.561      2229.222     0.0000000E+00 0.0000000E+00  400.5578     010201\n" +
                            "  0.0000000E+00 0.0000000E+00  821.9196      66.74381     0.0000000E+00 001201\n" +
                            "  0.0000000E+00 0.0000000E+00  12.39115     -14.82511     0.0000000E+00 100102\n" +
                            "  0.0000000E+00 0.0000000E+00 -25.72721     -36.65403     0.0000000E+00 010102\n" +
                            "  -28.32193     -124.4220     0.0000000E+00 0.0000000E+00 -44.62682     001102\n" +
                            " -0.4271942    -0.3889939     0.0000000E+00 0.0000000E+00 0.4949287E-01 100003\n" +
                            "  0.7134446    -0.5099879     0.0000000E+00 0.0000000E+00-0.3504251E-01 010003\n" +
                            "  0.0000000E+00 0.0000000E+00 -1.734620     -3.778753     0.0000000E+00 001003\n" +
                            "   502.4356      2897.954     0.0000000E+00 0.0000000E+00  752.6680     000400\n" +
                            "  0.0000000E+00 0.0000000E+00  360.0424     -1865.547     0.0000000E+00 000301\n" +
                            "  -12.30164     -86.80326     0.0000000E+00 0.0000000E+00 -9.359987     000202\n" +
                            "  0.0000000E+00 0.0000000E+00  4.008326     -14.22929     0.0000000E+00 000103\n" +
                            "  0.7187398E-01 0.4248500E-02 0.0000000E+00 0.0000000E+00-0.6455254     000004\n" +
                            "   406.9188      796.5244     0.0000000E+00 0.0000000E+00 -32.83620     500000\n" +
                            "   5483.983      11744.50     0.0000000E+00 0.0000000E+00 -467.9096     410000\n" +
                            "   29671.76      69295.90     0.0000000E+00 0.0000000E+00 -2508.709     320000\n" +
                            "   80511.33      204775.6     0.0000000E+00 0.0000000E+00 -6273.606     230000\n" +
                            "   109272.7      303283.1     0.0000000E+00 0.0000000E+00 -7150.850     140000\n" +
                            "   59064.65      180138.9     0.0000000E+00 0.0000000E+00 -2794.585     050000\n" +
                            "  0.0000000E+00 0.0000000E+00 -163.8867     -1326.178     0.0000000E+00 401000\n" +
                            "  0.0000000E+00 0.0000000E+00 -2084.615     -16440.21     0.0000000E+00 311000\n" +
                            "  0.0000000E+00 0.0000000E+00 -9650.004     -75450.38     0.0000000E+00 221000\n" +
                            "  0.0000000E+00 0.0000000E+00 -19411.24     -152596.6     0.0000000E+00 131000\n" +
                            "  0.0000000E+00 0.0000000E+00 -14463.09     -115130.9     0.0000000E+00 041000\n" +
                            "  -308.6878     -1121.733     0.0000000E+00 0.0000000E+00  289.4394     302000\n" +
                            "  -2396.656     -10581.83     0.0000000E+00 0.0000000E+00  1991.241     212000\n" +
                            "  -5905.405     -32400.17     0.0000000E+00 0.0000000E+00  4461.868     122000\n" +
                            "  -4581.334     -32551.16     0.0000000E+00 0.0000000E+00  3198.976     032000\n" +
                            "  0.0000000E+00 0.0000000E+00  1128.387      3803.526     0.0000000E+00 203000\n" +
                            "  0.0000000E+00 0.0000000E+00  5917.033      19965.95     0.0000000E+00 113000\n" +
                            "  0.0000000E+00 0.0000000E+00  7841.787      26309.54     0.0000000E+00 023000\n" +
                            "  -206.4819     -793.6952     0.0000000E+00 0.0000000E+00 -444.2374     104000\n" +
                            "  -681.1137     -2335.866     0.0000000E+00 0.0000000E+00 -1144.525     014000\n" +
                            "  0.0000000E+00 0.0000000E+00  269.4596      786.6171     0.0000000E+00 005000\n" +
                            "  0.0000000E+00 0.0000000E+00  329.0539     -1301.354     0.0000000E+00 400100\n" +
                            "  0.0000000E+00 0.0000000E+00  3486.059     -16686.36     0.0000000E+00 310100\n" +
                            "  0.0000000E+00 0.0000000E+00  14551.31     -76719.33     0.0000000E+00 220100\n" +
                            "  0.0000000E+00 0.0000000E+00  28274.09     -151851.3     0.0000000E+00 130100\n" +
                            "  0.0000000E+00 0.0000000E+00  21130.30     -110088.3     0.0000000E+00 040100\n" +
                            "   2562.273      993.3770     0.0000000E+00 0.0000000E+00  881.8541     301100\n" +
                            "   17131.07     -2457.997     0.0000000E+00 0.0000000E+00  4477.995     211100\n" +
                            "   38870.56     -31323.58     0.0000000E+00 0.0000000E+00  5296.393     121100\n" +
                            "   30316.68     -47401.62     0.0000000E+00 0.0000000E+00 -1050.831     031100\n" +
                            "  0.0000000E+00 0.0000000E+00  4003.559      13027.27     0.0000000E+00 202100\n" +
                            "  0.0000000E+00 0.0000000E+00  19340.93      62829.92     0.0000000E+00 112100\n" +
                            "  0.0000000E+00 0.0000000E+00  23552.22      75498.94     0.0000000E+00 022100\n" +
                            "  -1631.140     -9736.840     0.0000000E+00 0.0000000E+00 -2213.388     103100\n" +
                            "  -4730.523     -25814.59     0.0000000E+00 0.0000000E+00 -5562.370     013100\n" +
                            "  0.0000000E+00 0.0000000E+00  873.4411      2764.506     0.0000000E+00 004100\n" +
                            "  -109.5900     -47.90570     0.0000000E+00 0.0000000E+00  11.88967     400001\n" +
                            "  -884.0397     -867.7220     0.0000000E+00 0.0000000E+00  132.9418     310001\n" +
                            "  -1925.936     -5125.860     0.0000000E+00 0.0000000E+00  436.4514     220001\n" +
                            "   727.9975     -12374.86     0.0000000E+00 0.0000000E+00  423.6538     130001\n" +
                            "   4543.017     -10508.16     0.0000000E+00 0.0000000E+00 -55.40487     040001\n" +
                            "  0.0000000E+00 0.0000000E+00  118.8552      87.67140     0.0000000E+00 301001\n" +
                            "  0.0000000E+00 0.0000000E+00  1199.928      2369.554     0.0000000E+00 211001\n" +
                            "  0.0000000E+00 0.0000000E+00  3774.386      11122.82     0.0000000E+00 121001\n" +
                            "  0.0000000E+00 0.0000000E+00  3901.556      14551.07     0.0000000E+00 031001\n" +
                            "   26.64115      340.7551     0.0000000E+00 0.0000000E+00 -45.41368     202001\n" +
                            "   318.3824      3480.008     0.0000000E+00 0.0000000E+00  114.9702     112001\n" +
                            "   614.7737      6907.929     0.0000000E+00 0.0000000E+00  677.0812     022001\n" +
                            "  0.0000000E+00 0.0000000E+00 -929.5732     -2522.514     0.0000000E+00 103001\n" +
                            "  0.0000000E+00 0.0000000E+00 -2473.323     -6987.508     0.0000000E+00 013001\n" +
                            "   64.58380      165.0640     0.0000000E+00 0.0000000E+00  174.1254     004001\n" +
                            "   5821.705      6123.233     0.0000000E+00 0.0000000E+00  1423.939     300200\n" +
                            "   31152.42      25159.50     0.0000000E+00 0.0000000E+00  6641.436     210200\n" +
                            "   46415.67      11374.12     0.0000000E+00 0.0000000E+00  6638.388     120200\n" +
                            "   13365.10     -31208.99     0.0000000E+00 0.0000000E+00 -2239.528     030200\n" +
                            "  0.0000000E+00 0.0000000E+00 -9358.994     -25193.77     0.0000000E+00 201200\n" +
                            "  0.0000000E+00 0.0000000E+00 -49652.85     -135931.0     0.0000000E+00 111200\n" +
                            "  0.0000000E+00 0.0000000E+00 -66389.04     -185820.2     0.0000000E+00 021200\n" +
                            "  -8103.852     -34100.70     0.0000000E+00 0.0000000E+00 -10398.09     102200\n" +
                            "  -20815.90     -84257.56     0.0000000E+00 0.0000000E+00 -25740.72     012200\n" +
                            "  0.0000000E+00 0.0000000E+00 -508.4795     -997.2119     0.0000000E+00 003200\n" +
                            "  0.0000000E+00 0.0000000E+00  296.3829      764.0837     0.0000000E+00 300101\n" +
                            "  0.0000000E+00 0.0000000E+00  2903.900      8797.444     0.0000000E+00 210101\n" +
                            "  0.0000000E+00 0.0000000E+00  9097.526      30209.26     0.0000000E+00 120101\n" +
                            "  0.0000000E+00 0.0000000E+00  9690.938      32649.12     0.0000000E+00 030101\n" +
                            "  -1419.338      55.80517     0.0000000E+00 0.0000000E+00  208.4402     201101\n" +
                            "  -5221.005      8167.908     0.0000000E+00 0.0000000E+00  3321.081     111101\n" +
                            "  -4169.372      20430.78     0.0000000E+00 0.0000000E+00  7367.883     021101\n" +
                            "  0.0000000E+00 0.0000000E+00 -2645.716     -7973.776     0.0000000E+00 102101\n" +
                            "  0.0000000E+00 0.0000000E+00 -5365.244     -17720.63     0.0000000E+00 012101\n" +
                            "   404.9554      2584.844     0.0000000E+00 0.0000000E+00  925.2619     003101\n" +
                            "   26.77757     -11.73699     0.0000000E+00 0.0000000E+00 -3.628678     300002\n" +
                            "   372.4937      99.43899     0.0000000E+00 0.0000000E+00 -29.41388     210002\n" +
                            "   1347.473      976.4153     0.0000000E+00 0.0000000E+00 -22.34123     120002\n" +
                            "   1372.769      1674.462     0.0000000E+00 0.0000000E+00  99.29404     030002\n" +
                            "  0.0000000E+00 0.0000000E+00 -44.54118     0.3257381     0.0000000E+00 201002\n" +
                            "  0.0000000E+00 0.0000000E+00 -260.3993     -357.9652     0.0000000E+00 111002\n" +
                            "  0.0000000E+00 0.0000000E+00 -386.6975     -1076.426     0.0000000E+00 021002\n" +
                            "   4.653947     -46.09925     0.0000000E+00 0.0000000E+00 -79.52473     102002\n" +
                            "  -127.5512     -534.3623     0.0000000E+00 0.0000000E+00 -317.0775     012002\n" +
                            "  0.0000000E+00 0.0000000E+00  194.8568      476.2311     0.0000000E+00 003002\n" +
                            "  0.0000000E+00 0.0000000E+00 -28517.49     -75699.67     0.0000000E+00 200300\n" +
                            "  0.0000000E+00 0.0000000E+00 -133548.7     -352413.5     0.0000000E+00 110300\n" +
                            "  0.0000000E+00 0.0000000E+00 -154474.6     -404907.9     0.0000000E+00 020300\n" +
                            "  -27699.27     -63404.85     0.0000000E+00 0.0000000E+00 -23754.59     101300\n" +
                            "  -65700.94     -146975.7     0.0000000E+00 0.0000000E+00 -56660.62     011300\n" +
                            "  0.0000000E+00 0.0000000E+00 -6341.906     -13833.99     0.0000000E+00 002300\n" +
                            "  -3335.915     -2580.815     0.0000000E+00 0.0000000E+00  749.6491     200201\n" +
                            "  -12244.12     -2376.781     0.0000000E+00 0.0000000E+00  6586.700     110201\n" +
                            "  -10578.20      8383.693     0.0000000E+00 0.0000000E+00  11576.16     020201\n" +
                            "  0.0000000E+00 0.0000000E+00  5017.869      14488.33     0.0000000E+00 101201\n" +
                            "  0.0000000E+00 0.0000000E+00  15430.60      41143.33     0.0000000E+00 011201\n" +
                            "   2339.709      9902.502     0.0000000E+00 0.0000000E+00  3685.102     002201\n" +
                            "  0.0000000E+00 0.0000000E+00 -88.88649     -203.9243     0.0000000E+00 200102\n" +
                            "  0.0000000E+00 0.0000000E+00 -359.4272     -1913.242     0.0000000E+00 110102\n" +
                            "  0.0000000E+00 0.0000000E+00 -451.9281     -3633.481     0.0000000E+00 020102\n" +
                            "   12.45072     -562.9532     0.0000000E+00 0.0000000E+00 -505.8031     101102\n" +
                            "  -562.4855     -3167.954     0.0000000E+00 0.0000000E+00 -1674.108     011102\n" +
                            "  0.0000000E+00 0.0000000E+00  340.2511      1590.203     0.0000000E+00 002102\n" +
                            "  0.2622501      3.939609     0.0000000E+00 0.0000000E+00  1.346229     200003\n" +
                            "  -4.988946      21.83206     0.0000000E+00 0.0000000E+00  10.93450     110003\n" +
                            "  -12.12753      20.67116     0.0000000E+00 0.0000000E+00  20.97282     020003\n" +
                            "  0.0000000E+00 0.0000000E+00 -4.571318     -2.611859     0.0000000E+00 101003\n" +
                            "  0.0000000E+00 0.0000000E+00 -4.813270     -5.941312     0.0000000E+00 011003\n" +
                            "   1.272511      6.871723     0.0000000E+00 0.0000000E+00  25.21896     002003\n" +
                            "  -13567.15     -43322.62     0.0000000E+00 0.0000000E+00 -8072.518     100400\n" +
                            "  -30517.50     -94147.59     0.0000000E+00 0.0000000E+00 -18415.77     010400\n" +
                            "  0.0000000E+00 0.0000000E+00  7107.066      26832.38     0.0000000E+00 001400\n" +
                            "  0.0000000E+00 0.0000000E+00  10710.18      35845.77     0.0000000E+00 100301\n" +
                            "  0.0000000E+00 0.0000000E+00  25225.29      80886.62     0.0000000E+00 010301\n" +
                            "   5005.965      14821.69     0.0000000E+00 0.0000000E+00  4996.405     001301\n" +
                            "  -190.3556     -932.9794     0.0000000E+00 0.0000000E+00 -636.8717     100202\n" +
                            "  -1089.346     -3650.756     0.0000000E+00 0.0000000E+00 -1754.326     010202\n" +
                            "  0.0000000E+00 0.0000000E+00 -1507.372     -1432.858     0.0000000E+00 001202\n" +
                            "  0.0000000E+00 0.0000000E+00 -11.69084      12.65566     0.0000000E+00 100103\n" +
                            "  0.0000000E+00 0.0000000E+00  21.09788      52.48635     0.0000000E+00 010103\n" +
                            "   37.40633      143.2389     0.0000000E+00 0.0000000E+00  116.7498     001103\n" +
                            "  0.6133547     0.3201570     0.0000000E+00 0.0000000E+00-0.4025733E-01 100004\n" +
                            "  0.4445221     0.9139037     0.0000000E+00 0.0000000E+00 0.3055247     010004\n" +
                            "  0.0000000E+00 0.0000000E+00  2.979887      4.076410     0.0000000E+00 001004\n" +
                            "  0.0000000E+00 0.0000000E+00  16010.83      54119.28     0.0000000E+00 000500\n" +
                            "   888.8165      7093.299     0.0000000E+00 0.0000000E+00 -486.3017     000401\n" +
                            "  0.0000000E+00 0.0000000E+00 -1717.732     -2037.259     0.0000000E+00 000302\n" +
                            "   58.95050      246.1442     0.0000000E+00 0.0000000E+00  88.86714     000203\n" +
                            "  0.0000000E+00 0.0000000E+00  2.309938      18.96143     0.0000000E+00 000104\n" +
                            " -0.1189647     0.6672337E-03 0.0000000E+00 0.0000000E+00 0.6425721     000005"
            );

            slice_20200715_后偏转段_最后偏移段05.map = CosyArbitraryOrder.readMap(
                    " -0.3676851     0.7182200     0.0000000E+00 0.0000000E+00 0.1956855E-01 100000\n" +
                            "  -1.982587      1.152979     0.0000000E+00 0.0000000E+00 0.3404286E-01 010000\n" +
                            "  0.0000000E+00 0.0000000E+00 -1.305992     0.8248457E-01 0.0000000E+00 001000\n" +
                            "  0.0000000E+00 0.0000000E+00  2.297648    -0.9108178     0.0000000E+00 000100\n" +
                            "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00  1.000000     000010\n" +
                            "  0.2627930E-01 0.1888123E-02 0.0000000E+00 0.0000000E+00 0.9085061     000001\n" +
                            "  -6.563322     -1.104256     0.0000000E+00 0.0000000E+00 0.7551563     200000\n" +
                            "  -28.61827      2.830949     0.0000000E+00 0.0000000E+00  4.845114     110000\n" +
                            "  -28.16315      17.77472     0.0000000E+00 0.0000000E+00  6.739291     020000\n" +
                            "  0.0000000E+00 0.0000000E+00 -14.92367     0.9215201     0.0000000E+00 101000\n" +
                            "  0.0000000E+00 0.0000000E+00 -38.29919     0.6730656E-01 0.0000000E+00 011000\n" +
                            "  0.5373798     -1.087056     0.0000000E+00 0.0000000E+00-0.9557143     002000\n" +
                            "  0.0000000E+00 0.0000000E+00 -34.50999      10.96635     0.0000000E+00 100100\n" +
                            "  0.0000000E+00 0.0000000E+00 -80.65750      31.68621     0.0000000E+00 010100\n" +
                            "   9.981693      11.71210     0.0000000E+00 0.0000000E+00  1.173730     001100\n" +
                            "   1.325999      1.607916     0.0000000E+00 0.0000000E+00 0.3568443E-01 100001\n" +
                            "   4.777960      3.494993     0.0000000E+00 0.0000000E+00 0.3663045E-01 010001\n" +
                            "  0.0000000E+00 0.0000000E+00  3.100324     -1.614094     0.0000000E+00 001001\n" +
                            "   6.059787     -3.357507     0.0000000E+00 0.0000000E+00 -5.600786     000200\n" +
                            "  0.0000000E+00 0.0000000E+00  12.33587    -0.1016304     0.0000000E+00 000101\n" +
                            "  0.4461012E-02-0.1424428E-01 0.0000000E+00 0.0000000E+00-0.8015092     000002\n" +
                            "  -10.23726      11.91120     0.0000000E+00 0.0000000E+00  4.262924     300000\n" +
                            "  -130.5028      78.32786     0.0000000E+00 0.0000000E+00  34.81710     210000\n" +
                            "  -487.2521      161.1646     0.0000000E+00 0.0000000E+00  99.22602     120000\n" +
                            "  -563.3325      102.9998     0.0000000E+00 0.0000000E+00  98.77600     030000\n" +
                            "  0.0000000E+00 0.0000000E+00  7.310998     -44.76506     0.0000000E+00 201000\n" +
                            "  0.0000000E+00 0.0000000E+00 -6.475948     -251.3069     0.0000000E+00 111000\n" +
                            "  0.0000000E+00 0.0000000E+00 -70.18965     -348.1983     0.0000000E+00 021000\n" +
                            "  -28.20793     -60.22558     0.0000000E+00 0.0000000E+00 -16.04575     102000\n" +
                            "  -79.38254     -164.8760     0.0000000E+00 0.0000000E+00 -40.68396     012000\n" +
                            "  0.0000000E+00 0.0000000E+00 -16.82115      19.64552     0.0000000E+00 003000\n" +
                            "  0.0000000E+00 0.0000000E+00  27.31095     -29.03069     0.0000000E+00 200100\n" +
                            "  0.0000000E+00 0.0000000E+00  7.282878     -178.8020     0.0000000E+00 110100\n" +
                            "  0.0000000E+00 0.0000000E+00 -183.1795     -251.9548     0.0000000E+00 020100\n" +
                            "  -68.35448     -60.01883     0.0000000E+00 0.0000000E+00 -11.87494     101100\n" +
                            "  -172.3248     -149.3246     0.0000000E+00 0.0000000E+00 -25.47149     011100\n" +
                            "  0.0000000E+00 0.0000000E+00 -216.4408     -28.56865     0.0000000E+00 002100\n" +
                            "  -6.316187     -5.031258     0.0000000E+00 0.0000000E+00-0.1238274     200001\n" +
                            "  -16.58599     -22.18746     0.0000000E+00 0.0000000E+00 -2.604607     110001\n" +
                            "  -8.574252     -36.22091     0.0000000E+00 0.0000000E+00 -5.863856     020001\n" +
                            "  0.0000000E+00 0.0000000E+00 -8.074840     -1.932934     0.0000000E+00 101001\n" +
                            "  0.0000000E+00 0.0000000E+00  8.036989      1.599533     0.0000000E+00 011001\n" +
                            "   4.080694      10.29154     0.0000000E+00 0.0000000E+00  4.980036     002001\n" +
                            "   55.08963      162.4119     0.0000000E+00 0.0000000E+00  75.99973     100200\n" +
                            "   85.47840      313.1695     0.0000000E+00 0.0000000E+00  181.8286     010200\n" +
                            "  0.0000000E+00 0.0000000E+00 -378.2197      232.4228     0.0000000E+00 001200\n" +
                            "  0.0000000E+00 0.0000000E+00 -7.221434     -14.67287     0.0000000E+00 100101\n" +
                            "  0.0000000E+00 0.0000000E+00  40.33854     -33.08744     0.0000000E+00 010101\n" +
                            "  -2.005736      7.794487     0.0000000E+00 0.0000000E+00  2.422136     001101\n" +
                            "  0.5499727E-01 0.4182831     0.0000000E+00 0.0000000E+00-0.3054541E-01 100002\n" +
                            "  -2.698159    -0.5919151     0.0000000E+00 0.0000000E+00-0.2614606E-01 010002\n" +
                            "  0.0000000E+00 0.0000000E+00 0.7789583      3.558540     0.0000000E+00 001002\n" +
                            "  0.0000000E+00 0.0000000E+00 -452.4146     -73.13924     0.0000000E+00 000300\n" +
                            "  -42.01267     -80.94478     0.0000000E+00 0.0000000E+00 -14.62738     000201\n" +
                            "  0.0000000E+00 0.0000000E+00 -7.993670      8.705853     0.0000000E+00 000102\n" +
                            " -0.3940030E-01-0.7852009E-02 0.0000000E+00 0.0000000E+00 0.7125524     000003\n" +
                            "  -46.97643      45.67108     0.0000000E+00 0.0000000E+00  1.259146     400000\n" +
                            "  -596.9293      526.4121     0.0000000E+00 0.0000000E+00  49.14896     310000\n" +
                            "  -2905.791      2243.814     0.0000000E+00 0.0000000E+00  320.8829     220000\n" +
                            "  -6427.302      4142.118     0.0000000E+00 0.0000000E+00  753.1151     130000\n" +
                            "  -5420.105      2767.588     0.0000000E+00 0.0000000E+00  596.3535     040000\n" +
                            "  0.0000000E+00 0.0000000E+00 -90.80966     -267.4917     0.0000000E+00 301000\n" +
                            "  0.0000000E+00 0.0000000E+00 -709.9070     -2242.578     0.0000000E+00 211000\n" +
                            "  0.0000000E+00 0.0000000E+00 -1913.565     -6329.488     0.0000000E+00 121000\n" +
                            "  0.0000000E+00 0.0000000E+00 -1810.719     -6001.040     0.0000000E+00 031000\n" +
                            "  -19.86548     -296.9841     0.0000000E+00 0.0000000E+00 -58.86969     202000\n" +
                            "  -133.9965     -1667.670     0.0000000E+00 0.0000000E+00 -338.4985     112000\n" +
                            "  -207.5100     -2335.853     0.0000000E+00 0.0000000E+00 -492.2623     022000\n" +
                            "  0.0000000E+00 0.0000000E+00  224.4401      401.1409     0.0000000E+00 103000\n" +
                            "  0.0000000E+00 0.0000000E+00  517.1081      968.2094     0.0000000E+00 013000\n" +
                            "  -26.71063     -59.73544     0.0000000E+00 0.0000000E+00 -29.68850     004000\n" +
                            "  0.0000000E+00 0.0000000E+00 -140.6983     -266.2128     0.0000000E+00 300100\n" +
                            "  0.0000000E+00 0.0000000E+00 -1009.837     -1890.030     0.0000000E+00 210100\n" +
                            "  0.0000000E+00 0.0000000E+00 -2626.341     -4580.561     0.0000000E+00 120100\n" +
                            "  0.0000000E+00 0.0000000E+00 -2588.622     -3825.309     0.0000000E+00 030100\n" +
                            "  -187.5406     -1017.501     0.0000000E+00 0.0000000E+00 -446.3665     201100\n" +
                            "  -771.9284     -4941.477     0.0000000E+00 0.0000000E+00 -2331.443     111100\n" +
                            "  -805.8425     -6080.364     0.0000000E+00 0.0000000E+00 -3082.917     021100\n" +
                            "  0.0000000E+00 0.0000000E+00 -199.5431     -709.8887     0.0000000E+00 102100\n" +
                            "  0.0000000E+00 0.0000000E+00 -859.4127     -2186.558     0.0000000E+00 012100\n" +
                            "  -134.8425     -360.8397     0.0000000E+00 0.0000000E+00 -108.6787     003100\n" +
                            "  -45.54270     -32.85391     0.0000000E+00 0.0000000E+00  4.533542     300001\n" +
                            "  -353.5869     -360.2841     0.0000000E+00 0.0000000E+00  9.904474     210001\n" +
                            "  -790.8982     -1192.677     0.0000000E+00 0.0000000E+00 -54.32816     120001\n" +
                            "  -449.1140     -1244.102     0.0000000E+00 0.0000000E+00 -137.1385     030001\n" +
                            "  0.0000000E+00 0.0000000E+00  39.44350      82.45150     0.0000000E+00 201001\n" +
                            "  0.0000000E+00 0.0000000E+00  186.1260      557.4906     0.0000000E+00 111001\n" +
                            "  0.0000000E+00 0.0000000E+00  280.4394      925.3016     0.0000000E+00 021001\n" +
                            "   61.05903      143.1158     0.0000000E+00 0.0000000E+00  57.30130     102001\n" +
                            "   228.4536      496.0571     0.0000000E+00 0.0000000E+00  170.7291     012001\n" +
                            "  0.0000000E+00 0.0000000E+00 -68.01679     -144.2605     0.0000000E+00 003001\n" +
                            "  -1045.880     -1669.801     0.0000000E+00 0.0000000E+00 -656.4856     200200\n" +
                            "  -3768.657     -6271.660     0.0000000E+00 0.0000000E+00 -2952.994     110200\n" +
                            "  -2733.343     -5078.153     0.0000000E+00 0.0000000E+00 -3251.903     020200\n" +
                            "  0.0000000E+00 0.0000000E+00 -961.6571     -2583.277     0.0000000E+00 101200\n" +
                            "  0.0000000E+00 0.0000000E+00 -2952.261     -6841.303     0.0000000E+00 011200\n" +
                            "  -203.6247     -78.19633     0.0000000E+00 0.0000000E+00 -205.5299     002200\n" +
                            "  0.0000000E+00 0.0000000E+00  37.19318      266.0688     0.0000000E+00 200101\n" +
                            "  0.0000000E+00 0.0000000E+00  140.1371      1414.086     0.0000000E+00 110101\n" +
                            "  0.0000000E+00 0.0000000E+00  309.1902      1888.899     0.0000000E+00 020101\n" +
                            "   408.0911      830.9958     0.0000000E+00 0.0000000E+00  265.9351     101101\n" +
                            "   1105.927      2147.915     0.0000000E+00 0.0000000E+00  664.6387     011101\n" +
                            "  0.0000000E+00 0.0000000E+00  196.4489     -77.83777     0.0000000E+00 002101\n" +
                            "   2.566419     -1.931596     0.0000000E+00 0.0000000E+00 -1.253545     200002\n" +
                            "   18.69220    -0.1425333     0.0000000E+00 0.0000000E+00 -5.700783     110002\n" +
                            "   26.37152      17.73236     0.0000000E+00 0.0000000E+00 -6.223598     020002\n" +
                            "  0.0000000E+00 0.0000000E+00  5.331199     -4.428462     0.0000000E+00 101002\n" +
                            "  0.0000000E+00 0.0000000E+00 -1.520642     -12.07670     0.0000000E+00 011002\n" +
                            "  -9.056433     -18.40109     0.0000000E+00 0.0000000E+00 -14.02697     002002\n" +
                            "  0.0000000E+00 0.0000000E+00  2761.600      4508.635     0.0000000E+00 100300\n" +
                            "  0.0000000E+00 0.0000000E+00  5997.346      10055.59     0.0000000E+00 010300\n" +
                            "   768.8373      1943.738     0.0000000E+00 0.0000000E+00  759.7434     001300\n" +
                            "   708.7164      1090.707     0.0000000E+00 0.0000000E+00  221.0370     100201\n" +
                            "   1589.275      2229.222     0.0000000E+00 0.0000000E+00  403.6111     010201\n" +
                            "  0.0000000E+00 0.0000000E+00  809.8518      66.74381     0.0000000E+00 001201\n" +
                            "  0.0000000E+00 0.0000000E+00  11.99827     -14.82511     0.0000000E+00 100102\n" +
                            "  0.0000000E+00 0.0000000E+00 -26.68356     -36.65403     0.0000000E+00 010102\n" +
                            "  -51.23074     -124.4220     0.0000000E+00 0.0000000E+00 -44.12887     001102\n" +
                            " -0.4653927    -0.3889940     0.0000000E+00 0.0000000E+00 0.5101569E-01 100003\n" +
                            "  0.8728833    -0.5099878     0.0000000E+00 0.0000000E+00-0.3074624E-01 010003\n" +
                            "  0.0000000E+00 0.0000000E+00 -2.927610     -3.778753     0.0000000E+00 001003\n" +
                            "   1037.673      2897.954     0.0000000E+00 0.0000000E+00  745.1812     000400\n" +
                            "  0.0000000E+00 0.0000000E+00  22.96633     -1865.547     0.0000000E+00 000301\n" +
                            "  -20.23904     -86.80327     0.0000000E+00 0.0000000E+00 -8.595519     000202\n" +
                            "  0.0000000E+00 0.0000000E+00 0.5292150     -14.22929     0.0000000E+00 000103\n" +
                            "  0.7227389E-01 0.4248511E-02 0.0000000E+00 0.0000000E+00-0.6685663     000004\n" +
                            "   556.0625      796.5245     0.0000000E+00 0.0000000E+00 -34.80160     500000\n" +
                            "   7670.094      11744.50     0.0000000E+00 0.0000000E+00 -506.7921     410000\n" +
                            "   42532.03      69295.91     0.0000000E+00 0.0000000E+00 -2765.464     320000\n" +
                            "   118478.7      204775.6     0.0000000E+00 0.0000000E+00 -7032.917     230000\n" +
                            "   165516.6      303283.2     0.0000000E+00 0.0000000E+00 -8183.637     140000\n" +
                            "   92490.45      180138.9     0.0000000E+00 0.0000000E+00 -3317.307     050000\n" +
                            "  0.0000000E+00 0.0000000E+00 -411.0701     -1326.178     0.0000000E+00 401000\n" +
                            "  0.0000000E+00 0.0000000E+00 -5140.177     -16440.21     0.0000000E+00 311000\n" +
                            "  0.0000000E+00 0.0000000E+00 -23646.89     -75450.38     0.0000000E+00 221000\n" +
                            "  0.0000000E+00 0.0000000E+00 -47684.32     -152596.6     0.0000000E+00 131000\n" +
                            "  0.0000000E+00 0.0000000E+00 -35775.69     -115130.9     0.0000000E+00 041000\n" +
                            "  -524.5401     -1121.733     0.0000000E+00 0.0000000E+00  312.5281     302000\n" +
                            "  -4407.034     -10581.83     0.0000000E+00 0.0000000E+00  2201.468     212000\n" +
                            "  -12007.82     -32400.17     0.0000000E+00 0.0000000E+00  5099.673     122000\n" +
                            "  -10675.39     -32551.16     0.0000000E+00 0.0000000E+00  3844.978     032000\n" +
                            "  0.0000000E+00 0.0000000E+00  1831.314      3803.526     0.0000000E+00 203000\n" +
                            "  0.0000000E+00 0.0000000E+00  9605.817      19965.95     0.0000000E+00 113000\n" +
                            "  0.0000000E+00 0.0000000E+00  12701.91      26309.54     0.0000000E+00 023000\n" +
                            "  -352.7467     -793.6952     0.0000000E+00 0.0000000E+00 -451.8524     104000\n" +
                            "  -1112.126     -2335.866     0.0000000E+00 0.0000000E+00 -1164.301     014000\n" +
                            "  0.0000000E+00 0.0000000E+00  414.8596      786.6171     0.0000000E+00 005000\n" +
                            "  0.0000000E+00 0.0000000E+00  84.03500     -1301.354     0.0000000E+00 400100\n" +
                            "  0.0000000E+00 0.0000000E+00  375.2637     -16686.36     0.0000000E+00 310100\n" +
                            "  0.0000000E+00 0.0000000E+00  336.5201     -76719.33     0.0000000E+00 220100\n" +
                            "  0.0000000E+00 0.0000000E+00  236.8464     -151851.3     0.0000000E+00 130100\n" +
                            "  0.0000000E+00 0.0000000E+00  830.1393     -110088.3     0.0000000E+00 040100\n" +
                            "   2738.350      993.3774     0.0000000E+00 0.0000000E+00  965.9287     301100\n" +
                            "   16674.02     -2457.994     0.0000000E+00 0.0000000E+00  5121.362     211100\n" +
                            "   33186.77     -31323.57     0.0000000E+00 0.0000000E+00  6974.438     121100\n" +
                            "   21706.19     -47401.61     0.0000000E+00 0.0000000E+00  428.8637     031100\n" +
                            "  0.0000000E+00 0.0000000E+00  6417.555      13027.27     0.0000000E+00 202100\n" +
                            "  0.0000000E+00 0.0000000E+00  30983.21      62829.92     0.0000000E+00 112100\n" +
                            "  0.0000000E+00 0.0000000E+00  37542.42      75498.94     0.0000000E+00 022100\n" +
                            "  -3437.156     -9736.841     0.0000000E+00 0.0000000E+00 -2096.315     103100\n" +
                            "  -9511.396     -25814.59     0.0000000E+00 0.0000000E+00 -5291.231     013100\n" +
                            "  0.0000000E+00 0.0000000E+00  1383.111      2764.506     0.0000000E+00 004100\n" +
                            "  -124.3097     -47.90571     0.0000000E+00 0.0000000E+00  13.08801     400001\n" +
                            "  -1104.833     -867.7221     0.0000000E+00 0.0000000E+00  154.7540     310001\n" +
                            "  -3111.804     -5125.860     0.0000000E+00 0.0000000E+00  551.4664     220001\n" +
                            "  -1976.939     -12374.86     0.0000000E+00 0.0000000E+00  678.5615     130001\n" +
                            "   2331.189     -10508.16     0.0000000E+00 0.0000000E+00  162.6839     040001\n" +
                            "  0.0000000E+00 0.0000000E+00  162.8693      87.67140     0.0000000E+00 301001\n" +
                            "  0.0000000E+00 0.0000000E+00  1868.988      2369.554     0.0000000E+00 211001\n" +
                            "  0.0000000E+00 0.0000000E+00  6478.216      11122.82     0.0000000E+00 121001\n" +
                            "  0.0000000E+00 0.0000000E+00  7203.757      14551.07     0.0000000E+00 031001\n" +
                            "   121.0750      340.7551     0.0000000E+00 0.0000000E+00 -60.19005     202001\n" +
                            "   1135.936      3480.008     0.0000000E+00 0.0000000E+00  26.31370     112001\n" +
                            "   2133.765      6907.929     0.0000000E+00 0.0000000E+00  553.0848     022001\n" +
                            "  0.0000000E+00 0.0000000E+00 -1436.845     -2522.514     0.0000000E+00 103001\n" +
                            "  0.0000000E+00 0.0000000E+00 -3863.942     -6987.508     0.0000000E+00 013001\n" +
                            "   101.2884      165.0640     0.0000000E+00 0.0000000E+00  180.1299     004001\n" +
                            "   6992.360      6123.234     0.0000000E+00 0.0000000E+00  1579.345     300200\n" +
                            "   36012.45      25159.50     0.0000000E+00 0.0000000E+00  7446.103     210200\n" +
                            "   48861.78      11374.13     0.0000000E+00 0.0000000E+00  7876.262     120200\n" +
                            "   7744.559     -31208.99     0.0000000E+00 0.0000000E+00 -1705.355     030200\n" +
                            "  0.0000000E+00 0.0000000E+00 -13986.84     -25193.77     0.0000000E+00 201200\n" +
                            "  0.0000000E+00 0.0000000E+00 -74678.45     -135931.0     0.0000000E+00 111200\n" +
                            "  0.0000000E+00 0.0000000E+00 -100667.1     -185820.2     0.0000000E+00 021200\n" +
                            "  -14371.92     -34100.70     0.0000000E+00 0.0000000E+00 -10356.59     102200\n" +
                            "  -36328.57     -84257.56     0.0000000E+00 0.0000000E+00 -25627.80     012200\n" +
                            "  0.0000000E+00 0.0000000E+00 -683.3584     -997.2119     0.0000000E+00 003200\n" +
                            "  0.0000000E+00 0.0000000E+00  466.5079      764.0837     0.0000000E+00 300101\n" +
                            "  0.0000000E+00 0.0000000E+00  4734.773      8797.444     0.0000000E+00 210101\n" +
                            "  0.0000000E+00 0.0000000E+00  15175.47      30209.26     0.0000000E+00 120101\n" +
                            "  0.0000000E+00 0.0000000E+00  16133.98      32649.12     0.0000000E+00 030101\n" +
                            "  -1300.740      55.80493     0.0000000E+00 0.0000000E+00  166.4610     201101\n" +
                            "  -3188.320      8167.907     0.0000000E+00 0.0000000E+00  3165.460     111101\n" +
                            "   250.3287      20430.78     0.0000000E+00 0.0000000E+00  7267.402     021101\n" +
                            "  0.0000000E+00 0.0000000E+00 -4049.319     -7973.776     0.0000000E+00 102101\n" +
                            "  0.0000000E+00 0.0000000E+00 -8421.741     -17720.63     0.0000000E+00 012101\n" +
                            "   919.0727      2584.844     0.0000000E+00 0.0000000E+00  891.3053     003101\n" +
                            "   29.16777     -11.73698     0.0000000E+00 0.0000000E+00 -3.242008     300002\n" +
                            "   435.4208      99.43905     0.0000000E+00 0.0000000E+00 -26.98216     210002\n" +
                            "   1665.872      976.4155     0.0000000E+00 0.0000000E+00 -14.68288     120002\n" +
                            "   1820.093      1674.463     0.0000000E+00 0.0000000E+00  111.0106     030002\n" +
                            "  0.0000000E+00 0.0000000E+00 -56.42627     0.3257381     0.0000000E+00 201002\n" +
                            "  0.0000000E+00 0.0000000E+00 -403.4744     -357.9652     0.0000000E+00 111002\n" +
                            "  0.0000000E+00 0.0000000E+00 -707.9429     -1076.426     0.0000000E+00 021002\n" +
                            "  -22.98567     -46.09925     0.0000000E+00 0.0000000E+00 -79.84159     102002\n" +
                            "  -289.6710     -534.3624     0.0000000E+00 0.0000000E+00 -317.2507     012002\n" +
                            "  0.0000000E+00 0.0000000E+00  299.3115      476.2311     0.0000000E+00 003002\n" +
                            "  0.0000000E+00 0.0000000E+00 -42571.28     -75699.67     0.0000000E+00 200300\n" +
                            "  0.0000000E+00 0.0000000E+00 -198986.7     -352413.5     0.0000000E+00 110300\n" +
                            "  0.0000000E+00 0.0000000E+00 -229688.9     -404907.9     0.0000000E+00 020300\n" +
                            "  -39488.53     -63404.85     0.0000000E+00 0.0000000E+00 -24654.89     101300\n" +
                            "  -93011.03     -146975.7     0.0000000E+00 0.0000000E+00 -58814.29     011300\n" +
                            "  0.0000000E+00 0.0000000E+00 -8927.349     -13833.99     0.0000000E+00 002300\n" +
                            "  -3652.539     -2580.815     0.0000000E+00 0.0000000E+00  703.7411     200201\n" +
                            "  -12078.71     -2376.783     0.0000000E+00 0.0000000E+00  6552.619     110201\n" +
                            "  -8546.731      8383.692     0.0000000E+00 0.0000000E+00  11774.05     020201\n" +
                            "  0.0000000E+00 0.0000000E+00  7969.361      14488.33     0.0000000E+00 101201\n" +
                            "  0.0000000E+00 0.0000000E+00  23764.31      41143.33     0.0000000E+00 011201\n" +
                            "   4181.969      9902.503     0.0000000E+00 0.0000000E+00  3718.551     002201\n" +
                            "  0.0000000E+00 0.0000000E+00 -155.8975     -203.9243     0.0000000E+00 200102\n" +
                            "  0.0000000E+00 0.0000000E+00 -871.3362     -1913.242     0.0000000E+00 110102\n" +
                            "  0.0000000E+00 0.0000000E+00 -1336.473     -3633.481     0.0000000E+00 020102\n" +
                            "  -182.1753     -562.9532     0.0000000E+00 0.0000000E+00 -506.7571     101102\n" +
                            "  -1381.084     -3167.954     0.0000000E+00 0.0000000E+00 -1683.925     011102\n" +
                            "  0.0000000E+00 0.0000000E+00  639.0555      1590.203     0.0000000E+00 002102\n" +
                            "  0.8711989      3.939609     0.0000000E+00 0.0000000E+00  1.380099     200003\n" +
                            "  -2.822857      21.83205     0.0000000E+00 0.0000000E+00  11.14463     110003\n" +
                            "  -14.01516      20.67116     0.0000000E+00 0.0000000E+00  21.36126     020003\n" +
                            "  0.0000000E+00 0.0000000E+00 -4.796960     -2.611859     0.0000000E+00 101003\n" +
                            "  0.0000000E+00 0.0000000E+00 -4.542449     -5.941312     0.0000000E+00 011003\n" +
                            "   5.290010      6.871723     0.0000000E+00 0.0000000E+00  26.13499     002003\n" +
                            "  -21542.70     -43322.62     0.0000000E+00 0.0000000E+00 -7722.701     100400\n" +
                            "  -47854.68     -94147.59     0.0000000E+00 0.0000000E+00 -17463.04     010400\n" +
                            "  0.0000000E+00 0.0000000E+00  12128.47      26832.38     0.0000000E+00 001400\n" +
                            "  0.0000000E+00 0.0000000E+00  16871.30      35845.77     0.0000000E+00 100301\n" +
                            "  0.0000000E+00 0.0000000E+00  39131.84      80886.62     0.0000000E+00 010301\n" +
                            "   7543.388      14821.69     0.0000000E+00 0.0000000E+00  5069.910     001301\n" +
                            "  -464.1893     -932.9794     0.0000000E+00 0.0000000E+00 -633.6247     100202\n" +
                            "  -1972.354     -3650.756     0.0000000E+00 0.0000000E+00 -1752.849     010202\n" +
                            "  0.0000000E+00 0.0000000E+00 -1760.381     -1432.858     0.0000000E+00 001202\n" +
                            "  0.0000000E+00 0.0000000E+00 -9.611775      12.65566     0.0000000E+00 100103\n" +
                            "  0.0000000E+00 0.0000000E+00  30.11019      52.48635     0.0000000E+00 010103\n" +
                            "   76.57475      143.2389     0.0000000E+00 0.0000000E+00  117.3750     001103\n" +
                            "  0.6830707     0.3201571     0.0000000E+00 0.0000000E+00-0.4113724E-01 100004\n" +
                            "  0.4658792     0.9139037     0.0000000E+00 0.0000000E+00 0.3003181     010004\n" +
                            "  0.0000000E+00 0.0000000E+00  4.497829      4.076410     0.0000000E+00 001004\n" +
                            "  0.0000000E+00 0.0000000E+00  25993.38      54119.28     0.0000000E+00 000500\n" +
                            "   1894.517      7093.299     0.0000000E+00 0.0000000E+00 -680.1469     000401\n" +
                            "  0.0000000E+00 0.0000000E+00 -1905.192     -2037.259     0.0000000E+00 000302\n" +
                            "   107.4785      246.1442     0.0000000E+00 0.0000000E+00  86.37413     000203\n" +
                            "  0.0000000E+00 0.0000000E+00  7.898637      18.96143     0.0000000E+00 000104\n" +
                            " -0.1188828     0.6672139E-03 0.0000000E+00 0.0000000E+00 0.6634502     000005"
            );

            slice_20200715_后偏转段_最后偏移段10.map = CosyArbitraryOrder.readMap(
                    " -0.8575134E-02 0.7182200     0.0000000E+00 0.0000000E+00 0.1956855E-01 100000\n" +
                            "  -1.406097      1.152979     0.0000000E+00 0.0000000E+00 0.3404286E-01 010000\n" +
                            "  0.0000000E+00 0.0000000E+00 -1.264749     0.8248457E-01 0.0000000E+00 001000\n" +
                            "  0.0000000E+00 0.0000000E+00  1.842239    -0.9108178     0.0000000E+00 000100\n" +
                            "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00  1.000000     000010\n" +
                            "  0.2722336E-01 0.1888123E-02 0.0000000E+00 0.0000000E+00  1.005843     000001\n" +
                            "  -7.115450     -1.104256     0.0000000E+00 0.0000000E+00 0.6830960     200000\n" +
                            "  -27.20280      2.830949     0.0000000E+00 0.0000000E+00  4.613752     110000\n" +
                            "  -19.27579      17.77472     0.0000000E+00 0.0000000E+00  6.553585     020000\n" +
                            "  0.0000000E+00 0.0000000E+00 -14.46291     0.9215201     0.0000000E+00 101000\n" +
                            "  0.0000000E+00 0.0000000E+00 -38.26553     0.6730656E-01 0.0000000E+00 011000\n" +
                            " -0.6148234E-02 -1.087056     0.0000000E+00 0.0000000E+00-0.9566647     002000\n" +
                            "  0.0000000E+00 0.0000000E+00 -29.02682      10.96635     0.0000000E+00 100100\n" +
                            "  0.0000000E+00 0.0000000E+00 -64.81439      31.68621     0.0000000E+00 010100\n" +
                            "   15.83774      11.71210     0.0000000E+00 0.0000000E+00  1.194720     001100\n" +
                            "   1.929294      1.607916     0.0000000E+00 0.0000000E+00 0.3530555E-01 100001\n" +
                            "   6.203325      3.494993     0.0000000E+00 0.0000000E+00 0.3602223E-01 010001\n" +
                            "  0.0000000E+00 0.0000000E+00  2.270231     -1.614094     0.0000000E+00 001001\n" +
                            "   4.381034     -3.357507     0.0000000E+00 0.0000000E+00 -5.716676     000200\n" +
                            "  0.0000000E+00 0.0000000E+00  12.53953    -0.1016304     0.0000000E+00 000101\n" +
                            " -0.3188653E-02-0.1424428E-01 0.0000000E+00 0.0000000E+00-0.8830949     000002\n" +
                            "  -4.189038      11.91120     0.0000000E+00 0.0000000E+00  4.484508     300000\n" +
                            "  -90.89285      78.32786     0.0000000E+00 0.0000000E+00  34.60474     210000\n" +
                            "  -405.9537      161.1646     0.0000000E+00 0.0000000E+00  94.74734     120000\n" +
                            "  -511.4494      102.9998     0.0000000E+00 0.0000000E+00  93.05021     030000\n" +
                            "  0.0000000E+00 0.0000000E+00 -15.06090     -44.76506     0.0000000E+00 201000\n" +
                            "  0.0000000E+00 0.0000000E+00 -132.0952     -251.3069     0.0000000E+00 111000\n" +
                            "  0.0000000E+00 0.0000000E+00 -244.2614     -348.1983     0.0000000E+00 021000\n" +
                            "  -58.31950     -60.22558     0.0000000E+00 0.0000000E+00 -15.84885     102000\n" +
                            "  -161.8186     -164.8760     0.0000000E+00 0.0000000E+00 -40.33533     012000\n" +
                            "  0.0000000E+00 0.0000000E+00 -6.998247      19.64552     0.0000000E+00 003000\n" +
                            "  0.0000000E+00 0.0000000E+00  12.67815     -29.03069     0.0000000E+00 200100\n" +
                            "  0.0000000E+00 0.0000000E+00 -82.49526     -178.8020     0.0000000E+00 110100\n" +
                            "  0.0000000E+00 0.0000000E+00 -309.4596     -251.9548     0.0000000E+00 020100\n" +
                            "  -98.39087     -60.01883     0.0000000E+00 0.0000000E+00 -14.24335     101100\n" +
                            "  -247.0304     -149.3246     0.0000000E+00 0.0000000E+00 -29.95741     011100\n" +
                            "  0.0000000E+00 0.0000000E+00 -230.7298     -28.56865     0.0000000E+00 002100\n" +
                            "  -8.522566     -5.031258     0.0000000E+00 0.0000000E+00-0.3402584     200001\n" +
                            "  -28.46832     -22.18746     0.0000000E+00 0.0000000E+00 -3.486214     110001\n" +
                            "  -31.64891     -36.22091     0.0000000E+00 0.0000000E+00 -6.726844     020001\n" +
                            "  0.0000000E+00 0.0000000E+00 -9.298715     -1.932934     0.0000000E+00 101001\n" +
                            "  0.0000000E+00 0.0000000E+00  8.818040      1.599533     0.0000000E+00 011001\n" +
                            "   9.530179      10.29154     0.0000000E+00 0.0000000E+00  5.019200     002001\n" +
                            "   136.4445      162.4119     0.0000000E+00 0.0000000E+00  79.46410     100200\n" +
                            "   242.3023      313.1695     0.0000000E+00 0.0000000E+00  190.9735     010200\n" +
                            "  0.0000000E+00 0.0000000E+00 -261.9570      232.4228     0.0000000E+00 001200\n" +
                            "  0.0000000E+00 0.0000000E+00 -17.62238     -14.67287     0.0000000E+00 100101\n" +
                            "  0.0000000E+00 0.0000000E+00  14.94100     -33.08744     0.0000000E+00 010101\n" +
                            "  -1.380812      7.794487     0.0000000E+00 0.0000000E+00  1.976785     001101\n" +
                            " -0.3801389E-01 0.4182831     0.0000000E+00 0.0000000E+00-0.2797990E-01 100002\n" +
                            "  -3.734466    -0.5919151     0.0000000E+00 0.0000000E+00-0.2250959E-01 010002\n" +
                            "  0.0000000E+00 0.0000000E+00  3.026083      3.558540     0.0000000E+00 001002\n" +
                            "  0.0000000E+00 0.0000000E+00 -489.1731     -73.13924     0.0000000E+00 000300\n" +
                            "  -81.54661     -80.94478     0.0000000E+00 0.0000000E+00 -14.48158     000201\n" +
                            "  0.0000000E+00 0.0000000E+00 -3.798873      8.705853     0.0000000E+00 000102\n" +
                            " -0.3895993E-01-0.7852009E-02 0.0000000E+00 0.0000000E+00 0.7828195     000003\n" +
                            "  -24.56810      45.67108     0.0000000E+00 0.0000000E+00 -1.329221     400000\n" +
                            "  -333.9997      526.4121     0.0000000E+00 0.0000000E+00  30.28881     310000\n" +
                            "  -1774.591      2243.814     0.0000000E+00 0.0000000E+00  267.2444     220000\n" +
                            "  -4331.342      4142.118     0.0000000E+00 0.0000000E+00  666.0105     130000\n" +
                            "  -4018.589      2767.588     0.0000000E+00 0.0000000E+00  518.8535     040000\n" +
                            "  0.0000000E+00 0.0000000E+00 -224.4694     -267.4917     0.0000000E+00 301000\n" +
                            "  0.0000000E+00 0.0000000E+00 -1830.775     -2242.578     0.0000000E+00 211000\n" +
                            "  0.0000000E+00 0.0000000E+00 -5077.314     -6329.488     0.0000000E+00 121000\n" +
                            "  0.0000000E+00 0.0000000E+00 -4810.371     -6001.040     0.0000000E+00 031000\n" +
                            "  -168.7527     -296.9841     0.0000000E+00 0.0000000E+00 -46.20771     202000\n" +
                            "  -969.1311     -1667.670     0.0000000E+00 0.0000000E+00 -279.3818     112000\n" +
                            "  -1376.487     -2335.853     0.0000000E+00 0.0000000E+00 -425.7303     022000\n" +
                            "  0.0000000E+00 0.0000000E+00  424.9831      401.1409     0.0000000E+00 103000\n" +
                            "  0.0000000E+00 0.0000000E+00  1001.161      968.2094     0.0000000E+00 013000\n" +
                            "  -56.58020     -59.73544     0.0000000E+00 0.0000000E+00 -30.30632     004000\n" +
                            "  0.0000000E+00 0.0000000E+00 -272.0293     -266.2128     0.0000000E+00 300100\n" +
                            "  0.0000000E+00 0.0000000E+00 -1946.571     -1890.030     0.0000000E+00 210100\n" +
                            "  0.0000000E+00 0.0000000E+00 -4907.158     -4580.561     0.0000000E+00 120100\n" +
                            "  0.0000000E+00 0.0000000E+00 -4500.079     -3825.309     0.0000000E+00 030100\n" +
                            "  -691.6950     -1017.501     0.0000000E+00 0.0000000E+00 -444.2392     201100\n" +
                            "  -3227.271     -4941.477     0.0000000E+00 0.0000000E+00 -2359.551     111100\n" +
                            "  -3833.543     -6080.364     0.0000000E+00 0.0000000E+00 -3176.333     021100\n" +
                            "  0.0000000E+00 0.0000000E+00 -553.8328     -709.8887     0.0000000E+00 102100\n" +
                            "  0.0000000E+00 0.0000000E+00 -1951.410     -2186.558     0.0000000E+00 012100\n" +
                            "  -315.2015     -360.8397     0.0000000E+00 0.0000000E+00 -99.46373     003100\n" +
                            "  -64.83298     -32.85391     0.0000000E+00 0.0000000E+00  5.707796     300001\n" +
                            "  -553.0093     -360.2841     0.0000000E+00 0.0000000E+00  16.05249     210001\n" +
                            "  -1427.475     -1192.677     0.0000000E+00 0.0000000E+00 -44.18380     120001\n" +
                            "  -1097.042     -1244.102     0.0000000E+00 0.0000000E+00 -134.4889     030001\n" +
                            "  0.0000000E+00 0.0000000E+00  92.99836      82.45150     0.0000000E+00 201001\n" +
                            "  0.0000000E+00 0.0000000E+00  534.5397      557.4906     0.0000000E+00 111001\n" +
                            "  0.0000000E+00 0.0000000E+00  839.9587      925.3016     0.0000000E+00 021001\n" +
                            "   149.3941      143.1158     0.0000000E+00 0.0000000E+00  55.92775     102001\n" +
                            "   522.4692      496.0571     0.0000000E+00 0.0000000E+00  168.0447     012001\n" +
                            "  0.0000000E+00 0.0000000E+00 -145.6444     -144.2605     0.0000000E+00 003001\n" +
                            "  -1885.896     -1669.801     0.0000000E+00 0.0000000E+00 -714.3887     200200\n" +
                            "  -6924.193     -6271.660     0.0000000E+00 0.0000000E+00 -3208.370     110200\n" +
                            "  -5288.719     -5078.153     0.0000000E+00 0.0000000E+00 -3540.714     020200\n" +
                            "  0.0000000E+00 0.0000000E+00 -2257.888     -2583.277     0.0000000E+00 101200\n" +
                            "  0.0000000E+00 0.0000000E+00 -6382.751     -6841.303     0.0000000E+00 011200\n" +
                            "  -243.3940     -78.19633     0.0000000E+00 0.0000000E+00 -238.3418     002200\n" +
                            "  0.0000000E+00 0.0000000E+00  178.0047      266.0688     0.0000000E+00 200101\n" +
                            "  0.0000000E+00 0.0000000E+00  895.7692      1414.086     0.0000000E+00 110101\n" +
                            "  0.0000000E+00 0.0000000E+00  1322.691      1888.899     0.0000000E+00 020101\n" +
                            "   840.8913      830.9958     0.0000000E+00 0.0000000E+00  267.4311     101101\n" +
                            "   2222.429      2147.915     0.0000000E+00 0.0000000E+00  672.8072     011101\n" +
                            "  0.0000000E+00 0.0000000E+00  165.7028     -77.83777     0.0000000E+00 002101\n" +
                            "   2.776701     -1.931596     0.0000000E+00 0.0000000E+00 -1.358878     200002\n" +
                            "   25.39037    -0.1425333     0.0000000E+00 0.0000000E+00 -5.893664     110002\n" +
                            "   48.99160      17.73236     0.0000000E+00 0.0000000E+00 -6.321906     020002\n" +
                            "  0.0000000E+00 0.0000000E+00  3.844242     -4.428462     0.0000000E+00 101002\n" +
                            "  0.0000000E+00 0.0000000E+00 -7.994417     -12.07670     0.0000000E+00 011002\n" +
                            "  -21.35511     -18.40109     0.0000000E+00 0.0000000E+00 -14.53977     002002\n" +
                            "  0.0000000E+00 0.0000000E+00  5023.839      4508.635     0.0000000E+00 100300\n" +
                            "  0.0000000E+00 0.0000000E+00  11046.62      10055.59     0.0000000E+00 010300\n" +
                            "   1743.262      1943.738     0.0000000E+00 0.0000000E+00  831.5871     001300\n" +
                            "   1208.794      1090.707     0.0000000E+00 0.0000000E+00  230.2007     100201\n" +
                            "   2616.729      2229.222     0.0000000E+00 0.0000000E+00  411.8723     010201\n" +
                            "  0.0000000E+00 0.0000000E+00  777.1978      66.74381     0.0000000E+00 001201\n" +
                            "  0.0000000E+00 0.0000000E+00  10.93518     -14.82511     0.0000000E+00 100102\n" +
                            "  0.0000000E+00 0.0000000E+00 -29.27129     -36.65403     0.0000000E+00 010102\n" +
                            "  -113.2189     -124.4220     0.0000000E+00 0.0000000E+00 -42.78147     001102\n" +
                            " -0.5687527    -0.3889940     0.0000000E+00 0.0000000E+00 0.5513618E-01 100003\n" +
                            "   1.304303    -0.5099878     0.0000000E+00 0.0000000E+00-0.1912189E-01 010003\n" +
                            "  0.0000000E+00 0.0000000E+00 -6.155681     -3.778753     0.0000000E+00 001003\n" +
                            "   2485.953      2897.954     0.0000000E+00 0.0000000E+00  724.9223     000400\n" +
                            "  0.0000000E+00 0.0000000E+00 -889.1163     -1865.547     0.0000000E+00 000301\n" +
                            "  -41.71658     -86.80327     0.0000000E+00 0.0000000E+00 -6.526974     000202\n" +
                            "  0.0000000E+00 0.0000000E+00 -8.884795     -14.22929     0.0000000E+00 000103\n" +
                            "  0.7335608E-01 0.4248511E-02 0.0000000E+00 0.0000000E+00-0.7309120     000004\n" +
                            "   959.6257      796.5245     0.0000000E+00 0.0000000E+00 -40.11985     500000\n" +
                            "   13585.42      11744.50     0.0000000E+00 0.0000000E+00 -612.0053     410000\n" +
                            "   77330.19      69295.91     0.0000000E+00 0.0000000E+00 -3460.224     320000\n" +
                            "   221213.3      204775.6     0.0000000E+00 0.0000000E+00 -9087.551     230000\n" +
                            "   317705.2      303283.2     0.0000000E+00 0.0000000E+00 -10978.28     140000\n" +
                            "   182936.2      180138.9     0.0000000E+00 0.0000000E+00 -4731.758     050000\n" +
                            "  0.0000000E+00 0.0000000E+00 -1079.915     -1326.178     0.0000000E+00 401000\n" +
                            "  0.0000000E+00 0.0000000E+00 -13408.12     -16440.21     0.0000000E+00 311000\n" +
                            "  0.0000000E+00 0.0000000E+00 -61520.58     -75450.38     0.0000000E+00 221000\n" +
                            "  0.0000000E+00 0.0000000E+00 -124187.5     -152596.6     0.0000000E+00 131000\n" +
                            "  0.0000000E+00 0.0000000E+00 -93444.74     -115130.9     0.0000000E+00 041000\n" +
                            "  -1108.608     -1121.733     0.0000000E+00 0.0000000E+00  375.0034     302000\n" +
                            "  -9846.850     -10581.83     0.0000000E+00 0.0000000E+00  2770.315     212000\n" +
                            "  -28520.15     -32400.17     0.0000000E+00 0.0000000E+00  6825.496     122000\n" +
                            "  -27165.10     -32551.16     0.0000000E+00 0.0000000E+00  5592.981     032000\n" +
                            "  0.0000000E+00 0.0000000E+00  3733.341      3803.526     0.0000000E+00 203000\n" +
                            "  0.0000000E+00 0.0000000E+00  19587.17      19965.95     0.0000000E+00 113000\n" +
                            "  0.0000000E+00 0.0000000E+00  25852.75      26309.54     0.0000000E+00 023000\n" +
                            "  -748.5196     -793.6952     0.0000000E+00 0.0000000E+00 -472.4574     104000\n" +
                            "  -2278.386     -2335.866     0.0000000E+00 0.0000000E+00 -1217.812     014000\n" +
                            "  0.0000000E+00 0.0000000E+00  808.2928      786.6171     0.0000000E+00 005000\n" +
                            "  0.0000000E+00 0.0000000E+00 -578.9534     -1301.354     0.0000000E+00 400100\n" +
                            "  0.0000000E+00 0.0000000E+00 -8042.132     -16686.36     0.0000000E+00 310100\n" +
                            "  0.0000000E+00 0.0000000E+00 -38126.81     -76719.33     0.0000000E+00 220100\n" +
                            "  0.0000000E+00 0.0000000E+00 -75628.20     -151851.3     0.0000000E+00 130100\n" +
                            "  0.0000000E+00 0.0000000E+00 -54099.38     -110088.3     0.0000000E+00 040100\n" +
                            "   3214.793      993.3774     0.0000000E+00 0.0000000E+00  1193.423     301100\n" +
                            "   15437.30     -2457.994     0.0000000E+00 0.0000000E+00  6862.228     211100\n" +
                            "   17807.16     -31323.57     0.0000000E+00 0.0000000E+00  11515.01     121100\n" +
                            "  -1592.650     -47401.61     0.0000000E+00 0.0000000E+00  4432.728     031100\n" +
                            "  0.0000000E+00 0.0000000E+00  12949.51      13027.27     0.0000000E+00 202100\n" +
                            "  0.0000000E+00 0.0000000E+00  62485.66      62829.92     0.0000000E+00 112100\n" +
                            "  0.0000000E+00 0.0000000E+00  75398.06      75498.94     0.0000000E+00 022100\n" +
                            "  -8323.994     -9736.841     0.0000000E+00 0.0000000E+00 -1779.528     103100\n" +
                            "  -22447.80     -25814.59     0.0000000E+00 0.0000000E+00 -4557.562     013100\n" +
                            "  0.0000000E+00 0.0000000E+00  2762.210      2764.506     0.0000000E+00 004100\n" +
                            "  -164.1394     -47.90571     0.0000000E+00 0.0000000E+00  16.33061     400001\n" +
                            "  -1702.271     -867.7221     0.0000000E+00 0.0000000E+00  213.7751     310001\n" +
                            "  -6320.606     -5125.860     0.0000000E+00 0.0000000E+00  862.6831     220001\n" +
                            "  -9296.140     -12374.86     0.0000000E+00 0.0000000E+00  1368.311     130001\n" +
                            "  -3653.724     -10508.16     0.0000000E+00 0.0000000E+00  752.8056     040001\n" +
                            "  0.0000000E+00 0.0000000E+00  281.9655      87.67140     0.0000000E+00 301001\n" +
                            "  0.0000000E+00 0.0000000E+00  3679.377      2369.554     0.0000000E+00 211001\n" +
                            "  0.0000000E+00 0.0000000E+00  13794.42      11122.82     0.0000000E+00 121001\n" +
                            "  0.0000000E+00 0.0000000E+00  16139.07      14551.07     0.0000000E+00 031001\n" +
                            "   376.6004      340.7551     0.0000000E+00 0.0000000E+00 -100.1731     202001\n" +
                            "   3348.127      3480.008     0.0000000E+00 0.0000000E+00 -213.5800     112001\n" +
                            "   6243.953      6907.929     0.0000000E+00 0.0000000E+00  217.5650     022001\n" +
                            "  0.0000000E+00 0.0000000E+00 -2809.454     -2522.514     0.0000000E+00 103001\n" +
                            "  0.0000000E+00 0.0000000E+00 -7626.771     -6987.508     0.0000000E+00 013001\n" +
                            "   200.6061      165.0640     0.0000000E+00 0.0000000E+00  196.3771     004001\n" +
                            "   10159.99      6123.234     0.0000000E+00 0.0000000E+00  1999.853     300200\n" +
                            "   49163.04      25159.50     0.0000000E+00 0.0000000E+00  9623.418     210200\n" +
                            "   55480.62      11374.13     0.0000000E+00 0.0000000E+00  11225.78     120200\n" +
                            "  -7463.867     -31208.99     0.0000000E+00 0.0000000E+00 -259.9489     030200\n" +
                            "  0.0000000E+00 0.0000000E+00 -26509.16     -25193.77     0.0000000E+00 201200\n" +
                            "  0.0000000E+00 0.0000000E+00 -142394.4     -135931.0     0.0000000E+00 111200\n" +
                            "  0.0000000E+00 0.0000000E+00 -193419.1     -185820.2     0.0000000E+00 021200\n" +
                            "  -31332.49     -34100.70     0.0000000E+00 0.0000000E+00 -10244.28     102200\n" +
                            "  -78303.79     -84257.56     0.0000000E+00 0.0000000E+00 -25322.21     012200\n" +
                            "  0.0000000E+00 0.0000000E+00 -1156.557     -997.2119     0.0000000E+00 003200\n" +
                            "  0.0000000E+00 0.0000000E+00  926.8435      764.0837     0.0000000E+00 300101\n" +
                            "  0.0000000E+00 0.0000000E+00  9688.872      8797.444     0.0000000E+00 210101\n" +
                            "  0.0000000E+00 0.0000000E+00  31621.59      30209.26     0.0000000E+00 120101\n" +
                            "  0.0000000E+00 0.0000000E+00  33567.98      32649.12     0.0000000E+00 030101\n" +
                            "  -979.8296      55.80493     0.0000000E+00 0.0000000E+00  52.87069     201101\n" +
                            "   2311.855      8167.907     0.0000000E+00 0.0000000E+00  2744.369     111101\n" +
                            "   12209.45      20430.78     0.0000000E+00 0.0000000E+00  6995.509     021101\n" +
                            "  0.0000000E+00 0.0000000E+00 -7847.280     -7973.776     0.0000000E+00 102101\n" +
                            "  0.0000000E+00 0.0000000E+00 -16692.22     -17720.63     0.0000000E+00 012101\n" +
                            "   2310.205      2584.844     0.0000000E+00 0.0000000E+00  799.4225     003101\n" +
                            "   35.63533     -11.73698     0.0000000E+00 0.0000000E+00 -2.195742     300002\n" +
                            "   605.6932      99.43905     0.0000000E+00 0.0000000E+00 -20.40240     210002\n" +
                            "   2527.417      976.4155     0.0000000E+00 0.0000000E+00  6.039018     120002\n" +
                            "   3030.491      1674.463     0.0000000E+00 0.0000000E+00  142.7132     030002\n" +
                            "  0.0000000E+00 0.0000000E+00 -88.58574     0.3257381     0.0000000E+00 201002\n" +
                            "  0.0000000E+00 0.0000000E+00 -790.6166     -357.9652     0.0000000E+00 111002\n" +
                            "  0.0000000E+00 0.0000000E+00 -1577.190     -1076.426     0.0000000E+00 021002\n" +
                            "  -97.77477     -46.09925     0.0000000E+00 0.0000000E+00 -80.69892     102002\n" +
                            "  -728.3457     -534.3624     0.0000000E+00 0.0000000E+00 -317.7192     012002\n" +
                            "  0.0000000E+00 0.0000000E+00  581.9518      476.2311     0.0000000E+00 003002\n" +
                            "  0.0000000E+00 0.0000000E+00 -80598.94     -75699.67     0.0000000E+00 200300\n" +
                            "  0.0000000E+00 0.0000000E+00 -376053.0     -352413.5     0.0000000E+00 110300\n" +
                            "  0.0000000E+00 0.0000000E+00 -433208.7     -404907.9     0.0000000E+00 020300\n" +
                            "  -71388.70     -63404.85     0.0000000E+00 0.0000000E+00 -27090.98     101300\n" +
                            "  -166908.5     -146975.7     0.0000000E+00 0.0000000E+00 -64641.80     011300\n" +
                            "  0.0000000E+00 0.0000000E+00 -15923.21     -13833.99     0.0000000E+00 002300\n" +
                            "  -4509.280     -2580.815     0.0000000E+00 0.0000000E+00  579.5201     200201\n" +
                            "  -11631.12     -2376.783     0.0000000E+00 0.0000000E+00  6460.400     110201\n" +
                            "  -3049.833      8383.692     0.0000000E+00 0.0000000E+00  12309.51     020201\n" +
                            "  0.0000000E+00 0.0000000E+00  15955.71      14488.33     0.0000000E+00 101201\n" +
                            "  0.0000000E+00 0.0000000E+00  46314.19      41143.33     0.0000000E+00 011201\n" +
                            "   9166.877      9902.503     0.0000000E+00 0.0000000E+00  3809.059     002201\n" +
                            "  0.0000000E+00 0.0000000E+00 -337.2202     -203.9243     0.0000000E+00 200102\n" +
                            "  0.0000000E+00 0.0000000E+00 -2256.493     -1913.242     0.0000000E+00 110102\n" +
                            "  0.0000000E+00 0.0000000E+00 -3729.935     -3633.481     0.0000000E+00 020102\n" +
                            "  -708.8075     -562.9532     0.0000000E+00 0.0000000E+00 -509.3380     101102\n" +
                            "  -3596.104     -3167.954     0.0000000E+00 0.0000000E+00 -1710.487     011102\n" +
                            "  0.0000000E+00 0.0000000E+00  1447.580      1590.203     0.0000000E+00 002102\n" +
                            "   2.518933      3.939609     0.0000000E+00 0.0000000E+00  1.471746     200003\n" +
                            "   3.038288      21.83205     0.0000000E+00 0.0000000E+00  11.71324     110003\n" +
                            "  -19.12284      20.67116     0.0000000E+00 0.0000000E+00  22.41232     020003\n" +
                            "  0.0000000E+00 0.0000000E+00 -5.407518     -2.611859     0.0000000E+00 101003\n" +
                            "  0.0000000E+00 0.0000000E+00 -3.809641     -5.941312     0.0000000E+00 011003\n" +
                            "   16.16082      6.871723     0.0000000E+00 0.0000000E+00  28.61364     002003\n" +
                            "  -43123.47     -43322.62     0.0000000E+00 0.0000000E+00 -6776.133     100400\n" +
                            "  -94766.77     -94147.59     0.0000000E+00 0.0000000E+00 -14885.03     010400\n" +
                            "  0.0000000E+00 0.0000000E+00  25715.70      26832.38     0.0000000E+00 001400\n" +
                            "  0.0000000E+00 0.0000000E+00  33542.46      35845.77     0.0000000E+00 100301\n" +
                            "  0.0000000E+00 0.0000000E+00  76761.12      80886.62     0.0000000E+00 010301\n" +
                            "   14409.32      14821.69     0.0000000E+00 0.0000000E+00  5268.802     001301\n" +
                            "  -1205.147     -932.9794     0.0000000E+00 0.0000000E+00 -624.8384     100202\n" +
                            "  -4361.658     -3650.756     0.0000000E+00 0.0000000E+00 -1748.852     010202\n" +
                            "  0.0000000E+00 0.0000000E+00 -2444.989     -1432.858     0.0000000E+00 001202\n" +
                            "  0.0000000E+00 0.0000000E+00 -3.986093      12.65566     0.0000000E+00 100103\n" +
                            "  0.0000000E+00 0.0000000E+00  54.49631      52.48635     0.0000000E+00 010103\n" +
                            "   182.5593      143.2389     0.0000000E+00 0.0000000E+00  119.0665     001103\n" +
                            "  0.8717128     0.3201571     0.0000000E+00 0.0000000E+00-0.4351815E-01 100004\n" +
                            "  0.5236689     0.9139037     0.0000000E+00 0.0000000E+00 0.2862304     010004\n" +
                            "  0.0000000E+00 0.0000000E+00  8.605176      4.076410     0.0000000E+00 001004\n" +
                            "  0.0000000E+00 0.0000000E+00  53004.83      54119.28     0.0000000E+00 000500\n" +
                            "   4615.810      7093.299     0.0000000E+00 0.0000000E+00 -1204.667     000401\n" +
                            "  0.0000000E+00 0.0000000E+00 -2412.433     -2037.259     0.0000000E+00 000302\n" +
                            "   238.7889      246.1442     0.0000000E+00 0.0000000E+00  79.62834     000203\n" +
                            "  0.0000000E+00 0.0000000E+00  23.02091      18.96143     0.0000000E+00 000104\n" +
                            " -0.1186615     0.6672139E-03 0.0000000E+00 0.0000000E+00 0.7199433     000005"
            );

            slice_20200715_后偏转段_最后偏移段15.map = CosyArbitraryOrder.readMap(
                    "  0.3505348     0.7182200     0.0000000E+00 0.0000000E+00 0.1956855E-01 100000\n" +
                            " -0.8296074      1.152979     0.0000000E+00 0.0000000E+00 0.3404286E-01 010000\n" +
                            "  0.0000000E+00 0.0000000E+00 -1.223507     0.8248457E-01 0.0000000E+00 001000\n" +
                            "  0.0000000E+00 0.0000000E+00  1.386830    -0.9108178     0.0000000E+00 000100\n" +
                            "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00  1.000000     000010\n" +
                            "  0.2816742E-01 0.1888123E-02 0.0000000E+00 0.0000000E+00  1.103180     000001\n" +
                            "  -7.667578     -1.104256     0.0000000E+00 0.0000000E+00 0.6110356     200000\n" +
                            "  -25.78732      2.830949     0.0000000E+00 0.0000000E+00  4.382391     110000\n" +
                            "  -10.38843      17.77472     0.0000000E+00 0.0000000E+00  6.367880     020000\n" +
                            "  0.0000000E+00 0.0000000E+00 -14.00215     0.9215201     0.0000000E+00 101000\n" +
                            "  0.0000000E+00 0.0000000E+00 -38.23188     0.6730656E-01 0.0000000E+00 011000\n" +
                            " -0.5496763     -1.087056     0.0000000E+00 0.0000000E+00-0.9576152     002000\n" +
                            "  0.0000000E+00 0.0000000E+00 -23.54364      10.96635     0.0000000E+00 100100\n" +
                            "  0.0000000E+00 0.0000000E+00 -48.97129      31.68621     0.0000000E+00 010100\n" +
                            "   21.69379      11.71210     0.0000000E+00 0.0000000E+00  1.215710     001100\n" +
                            "   2.532588      1.607916     0.0000000E+00 0.0000000E+00 0.3492667E-01 100001\n" +
                            "   7.628691      3.494993     0.0000000E+00 0.0000000E+00 0.3541401E-01 010001\n" +
                            "  0.0000000E+00 0.0000000E+00  1.440139     -1.614094     0.0000000E+00 001001\n" +
                            "   2.702281     -3.357507     0.0000000E+00 0.0000000E+00 -5.832565     000200\n" +
                            "  0.0000000E+00 0.0000000E+00  12.74319    -0.1016304     0.0000000E+00 000101\n" +
                            " -0.1083832E-01-0.1424428E-01 0.0000000E+00 0.0000000E+00-0.9646806     000002\n" +
                            "   1.859184      11.91120     0.0000000E+00 0.0000000E+00  4.706092     300000\n" +
                            "  -51.28285      78.32786     0.0000000E+00 0.0000000E+00  34.39239     210000\n" +
                            "  -324.6553      161.1646     0.0000000E+00 0.0000000E+00  90.26866     120000\n" +
                            "  -459.5662      102.9998     0.0000000E+00 0.0000000E+00  87.32441     030000\n" +
                            "  0.0000000E+00 0.0000000E+00 -37.43279     -44.76506     0.0000000E+00 201000\n" +
                            "  0.0000000E+00 0.0000000E+00 -257.7145     -251.3069     0.0000000E+00 111000\n" +
                            "  0.0000000E+00 0.0000000E+00 -418.3331     -348.1983     0.0000000E+00 021000\n" +
                            "  -88.43106     -60.22558     0.0000000E+00 0.0000000E+00 -15.65196     102000\n" +
                            "  -244.2546     -164.8760     0.0000000E+00 0.0000000E+00 -39.98671     012000\n" +
                            "  0.0000000E+00 0.0000000E+00  2.824652      19.64552     0.0000000E+00 003000\n" +
                            "  0.0000000E+00 0.0000000E+00 -1.954657     -29.03069     0.0000000E+00 200100\n" +
                            "  0.0000000E+00 0.0000000E+00 -172.2734     -178.8020     0.0000000E+00 110100\n" +
                            "  0.0000000E+00 0.0000000E+00 -435.7397     -251.9548     0.0000000E+00 020100\n" +
                            "  -128.4273     -60.01883     0.0000000E+00 0.0000000E+00 -16.61177     101100\n" +
                            "  -321.7360     -149.3246     0.0000000E+00 0.0000000E+00 -34.44334     011100\n" +
                            "  0.0000000E+00 0.0000000E+00 -245.0187     -28.56865     0.0000000E+00 002100\n" +
                            "  -10.72895     -5.031258     0.0000000E+00 0.0000000E+00-0.5566894     200001\n" +
                            "  -40.35064     -22.18746     0.0000000E+00 0.0000000E+00 -4.367822     110001\n" +
                            "  -54.72357     -36.22091     0.0000000E+00 0.0000000E+00 -7.589832     020001\n" +
                            "  0.0000000E+00 0.0000000E+00 -10.52259     -1.932934     0.0000000E+00 101001\n" +
                            "  0.0000000E+00 0.0000000E+00  9.599092      1.599533     0.0000000E+00 011001\n" +
                            "   14.97966      10.29154     0.0000000E+00 0.0000000E+00  5.058365     002001\n" +
                            "   217.7995      162.4119     0.0000000E+00 0.0000000E+00  82.92848     100200\n" +
                            "   399.1261      313.1695     0.0000000E+00 0.0000000E+00  200.1183     010200\n" +
                            "  0.0000000E+00 0.0000000E+00 -145.6942      232.4228     0.0000000E+00 001200\n" +
                            "  0.0000000E+00 0.0000000E+00 -28.02333     -14.67287     0.0000000E+00 100101\n" +
                            "  0.0000000E+00 0.0000000E+00 -10.45654     -33.08744     0.0000000E+00 010101\n" +
                            " -0.7558871      7.794487     0.0000000E+00 0.0000000E+00  1.531433     001101\n" +
                            " -0.1310250     0.4182831     0.0000000E+00 0.0000000E+00-0.2541438E-01 100002\n" +
                            "  -4.770773    -0.5919151     0.0000000E+00 0.0000000E+00-0.1887312E-01 010002\n" +
                            "  0.0000000E+00 0.0000000E+00  5.273207      3.558540     0.0000000E+00 001002\n" +
                            "  0.0000000E+00 0.0000000E+00 -525.9316     -73.13924     0.0000000E+00 000300\n" +
                            "  -121.0806     -80.94478     0.0000000E+00 0.0000000E+00 -14.33578     000201\n" +
                            "  0.0000000E+00 0.0000000E+00 0.3959246      8.705853     0.0000000E+00 000102\n" +
                            " -0.3851955E-01-0.7852009E-02 0.0000000E+00 0.0000000E+00 0.8530867     000003\n" +
                            "  -2.159778      45.67108     0.0000000E+00 0.0000000E+00 -3.917588     400000\n" +
                            "  -71.07007      526.4121     0.0000000E+00 0.0000000E+00  11.42865     310000\n" +
                            "  -643.3921      2243.814     0.0000000E+00 0.0000000E+00  213.6058     220000\n" +
                            "  -2235.382      4142.118     0.0000000E+00 0.0000000E+00  578.9059     130000\n" +
                            "  -2617.073      2767.588     0.0000000E+00 0.0000000E+00  441.3535     040000\n" +
                            "  0.0000000E+00 0.0000000E+00 -358.1291     -267.4917     0.0000000E+00 301000\n" +
                            "  0.0000000E+00 0.0000000E+00 -2951.642     -2242.578     0.0000000E+00 211000\n" +
                            "  0.0000000E+00 0.0000000E+00 -8241.063     -6329.488     0.0000000E+00 121000\n" +
                            "  0.0000000E+00 0.0000000E+00 -7810.024     -6001.040     0.0000000E+00 031000\n" +
                            "  -317.6399     -296.9841     0.0000000E+00 0.0000000E+00 -33.54574     202000\n" +
                            "  -1804.266     -1667.670     0.0000000E+00 0.0000000E+00 -220.2650     112000\n" +
                            "  -2545.463     -2335.853     0.0000000E+00 0.0000000E+00 -359.1984     022000\n" +
                            "  0.0000000E+00 0.0000000E+00  625.5260      401.1409     0.0000000E+00 103000\n" +
                            "  0.0000000E+00 0.0000000E+00  1485.215      968.2094     0.0000000E+00 013000\n" +
                            "  -86.44977     -59.73544     0.0000000E+00 0.0000000E+00 -30.92414     004000\n" +
                            "  0.0000000E+00 0.0000000E+00 -403.3603     -266.2128     0.0000000E+00 300100\n" +
                            "  0.0000000E+00 0.0000000E+00 -2883.305     -1890.030     0.0000000E+00 210100\n" +
                            "  0.0000000E+00 0.0000000E+00 -7187.974     -4580.561     0.0000000E+00 120100\n" +
                            "  0.0000000E+00 0.0000000E+00 -6411.536     -3825.309     0.0000000E+00 030100\n" +
                            "  -1195.849     -1017.501     0.0000000E+00 0.0000000E+00 -442.1119     201100\n" +
                            "  -5682.613     -4941.477     0.0000000E+00 0.0000000E+00 -2387.659     111100\n" +
                            "  -6861.244     -6080.364     0.0000000E+00 0.0000000E+00 -3269.749     021100\n" +
                            "  0.0000000E+00 0.0000000E+00 -908.1226     -709.8887     0.0000000E+00 102100\n" +
                            "  0.0000000E+00 0.0000000E+00 -3043.407     -2186.558     0.0000000E+00 012100\n" +
                            "  -495.5606     -360.8397     0.0000000E+00 0.0000000E+00 -90.24875     003100\n" +
                            "  -84.12325     -32.85391     0.0000000E+00 0.0000000E+00  6.882051     300001\n" +
                            "  -752.4316     -360.2841     0.0000000E+00 0.0000000E+00  22.20051     210001\n" +
                            "  -2064.052     -1192.677     0.0000000E+00 0.0000000E+00 -34.03943     120001\n" +
                            "  -1744.970     -1244.102     0.0000000E+00 0.0000000E+00 -131.8392     030001\n" +
                            "  0.0000000E+00 0.0000000E+00  146.5532      82.45150     0.0000000E+00 201001\n" +
                            "  0.0000000E+00 0.0000000E+00  882.9535      557.4906     0.0000000E+00 111001\n" +
                            "  0.0000000E+00 0.0000000E+00  1399.478      925.3016     0.0000000E+00 021001\n" +
                            "   237.7292      143.1158     0.0000000E+00 0.0000000E+00  54.55420     102001\n" +
                            "   816.4849      496.0571     0.0000000E+00 0.0000000E+00  165.3604     012001\n" +
                            "  0.0000000E+00 0.0000000E+00 -223.2719     -144.2605     0.0000000E+00 003001\n" +
                            "  -2725.911     -1669.801     0.0000000E+00 0.0000000E+00 -772.2918     200200\n" +
                            "  -10079.73     -6271.660     0.0000000E+00 0.0000000E+00 -3463.745     110200\n" +
                            "  -7844.094     -5078.153     0.0000000E+00 0.0000000E+00 -3829.526     020200\n" +
                            "  0.0000000E+00 0.0000000E+00 -3554.120     -2583.277     0.0000000E+00 101200\n" +
                            "  0.0000000E+00 0.0000000E+00 -9813.241     -6841.303     0.0000000E+00 011200\n" +
                            "  -283.1632     -78.19633     0.0000000E+00 0.0000000E+00 -271.1538     002200\n" +
                            "  0.0000000E+00 0.0000000E+00  318.8163      266.0688     0.0000000E+00 200101\n" +
                            "  0.0000000E+00 0.0000000E+00  1651.401      1414.086     0.0000000E+00 110101\n" +
                            "  0.0000000E+00 0.0000000E+00  2336.192      1888.899     0.0000000E+00 020101\n" +
                            "   1273.692      830.9958     0.0000000E+00 0.0000000E+00  268.9271     101101\n" +
                            "   3338.932      2147.915     0.0000000E+00 0.0000000E+00  680.9757     011101\n" +
                            "  0.0000000E+00 0.0000000E+00  134.9567     -77.83777     0.0000000E+00 002101\n" +
                            "   2.986984     -1.931596     0.0000000E+00 0.0000000E+00 -1.464212     200002\n" +
                            "   32.08855    -0.1425333     0.0000000E+00 0.0000000E+00 -6.086545     110002\n" +
                            "   71.61168      17.73236     0.0000000E+00 0.0000000E+00 -6.420214     020002\n" +
                            "  0.0000000E+00 0.0000000E+00  2.357284     -4.428462     0.0000000E+00 101002\n" +
                            "  0.0000000E+00 0.0000000E+00 -14.46819     -12.07670     0.0000000E+00 011002\n" +
                            "  -33.65378     -18.40109     0.0000000E+00 0.0000000E+00 -15.05258     002002\n" +
                            "  0.0000000E+00 0.0000000E+00  7286.077      4508.635     0.0000000E+00 100300\n" +
                            "  0.0000000E+00 0.0000000E+00  16095.89      10055.59     0.0000000E+00 010300\n" +
                            "   2717.686      1943.738     0.0000000E+00 0.0000000E+00  903.4309     001300\n" +
                            "   1708.872      1090.707     0.0000000E+00 0.0000000E+00  239.3645     100201\n" +
                            "   3644.182      2229.222     0.0000000E+00 0.0000000E+00  420.1336     010201\n" +
                            "  0.0000000E+00 0.0000000E+00  744.5438      66.74381     0.0000000E+00 001201\n" +
                            "  0.0000000E+00 0.0000000E+00  9.872101     -14.82511     0.0000000E+00 100102\n" +
                            "  0.0000000E+00 0.0000000E+00 -31.85903     -36.65403     0.0000000E+00 010102\n" +
                            "  -175.2071     -124.4220     0.0000000E+00 0.0000000E+00 -41.43407     001102\n" +
                            " -0.6721127    -0.3889940     0.0000000E+00 0.0000000E+00 0.5925668E-01 100003\n" +
                            "   1.735723    -0.5099878     0.0000000E+00 0.0000000E+00-0.7497547E-02 010003\n" +
                            "  0.0000000E+00 0.0000000E+00 -9.383752     -3.778753     0.0000000E+00 001003\n" +
                            "   3934.234      2897.954     0.0000000E+00 0.0000000E+00  704.6634     000400\n" +
                            "  0.0000000E+00 0.0000000E+00 -1801.199     -1865.547     0.0000000E+00 000301\n" +
                            "  -63.19412     -86.80327     0.0000000E+00 0.0000000E+00 -4.458430     000202\n" +
                            "  0.0000000E+00 0.0000000E+00 -18.29881     -14.22929     0.0000000E+00 000103\n" +
                            "  0.7443828E-01 0.4248511E-02 0.0000000E+00 0.0000000E+00-0.7932578     000004\n" +
                            "   1363.189      796.5245     0.0000000E+00 0.0000000E+00 -45.43810     500000\n" +
                            "   19500.74      11744.50     0.0000000E+00 0.0000000E+00 -717.2185     410000\n" +
                            "   112128.4      69295.91     0.0000000E+00 0.0000000E+00 -4154.983     320000\n" +
                            "   323947.9      204775.6     0.0000000E+00 0.0000000E+00 -11142.18     230000\n" +
                            "   469893.8      303283.2     0.0000000E+00 0.0000000E+00 -13772.92     140000\n" +
                            "   273382.0      180138.9     0.0000000E+00 0.0000000E+00 -6146.208     050000\n" +
                            "  0.0000000E+00 0.0000000E+00 -1748.760     -1326.178     0.0000000E+00 401000\n" +
                            "  0.0000000E+00 0.0000000E+00 -21676.06     -16440.21     0.0000000E+00 311000\n" +
                            "  0.0000000E+00 0.0000000E+00 -99394.28     -75450.38     0.0000000E+00 221000\n" +
                            "  0.0000000E+00 0.0000000E+00 -200690.7     -152596.6     0.0000000E+00 131000\n" +
                            "  0.0000000E+00 0.0000000E+00 -151113.8     -115130.9     0.0000000E+00 041000\n" +
                            "  -1692.675     -1121.733     0.0000000E+00 0.0000000E+00  437.4787     302000\n" +
                            "  -15286.67     -10581.83     0.0000000E+00 0.0000000E+00  3339.163     212000\n" +
                            "  -45032.48     -32400.17     0.0000000E+00 0.0000000E+00  8551.318     122000\n" +
                            "  -43654.81     -32551.16     0.0000000E+00 0.0000000E+00  7340.984     032000\n" +
                            "  0.0000000E+00 0.0000000E+00  5635.367      3803.526     0.0000000E+00 203000\n" +
                            "  0.0000000E+00 0.0000000E+00  29568.53      19965.95     0.0000000E+00 113000\n" +
                            "  0.0000000E+00 0.0000000E+00  39003.59      26309.54     0.0000000E+00 023000\n" +
                            "  -1144.292     -793.6952     0.0000000E+00 0.0000000E+00 -493.0624     104000\n" +
                            "  -3444.646     -2335.866     0.0000000E+00 0.0000000E+00 -1271.323     014000\n" +
                            "  0.0000000E+00 0.0000000E+00  1201.726      786.6171     0.0000000E+00 005000\n" +
                            "  0.0000000E+00 0.0000000E+00 -1241.942     -1301.354     0.0000000E+00 400100\n" +
                            "  0.0000000E+00 0.0000000E+00 -16459.53     -16686.36     0.0000000E+00 310100\n" +
                            "  0.0000000E+00 0.0000000E+00 -76590.14     -76719.33     0.0000000E+00 220100\n" +
                            "  0.0000000E+00 0.0000000E+00 -151493.3     -151851.3     0.0000000E+00 130100\n" +
                            "  0.0000000E+00 0.0000000E+00 -109028.9     -110088.3     0.0000000E+00 040100\n" +
                            "   3691.235      993.3774     0.0000000E+00 0.0000000E+00  1420.918     301100\n" +
                            "   14200.57     -2457.994     0.0000000E+00 0.0000000E+00  8603.093     211100\n" +
                            "   2427.558     -31323.57     0.0000000E+00 0.0000000E+00  16055.58     121100\n" +
                            "  -24891.49     -47401.61     0.0000000E+00 0.0000000E+00  8436.592     031100\n" +
                            "  0.0000000E+00 0.0000000E+00  19481.46      13027.27     0.0000000E+00 202100\n" +
                            "  0.0000000E+00 0.0000000E+00  93988.12      62829.92     0.0000000E+00 112100\n" +
                            "  0.0000000E+00 0.0000000E+00  113253.7      75498.94     0.0000000E+00 022100\n" +
                            "  -13210.83     -9736.841     0.0000000E+00 0.0000000E+00 -1462.742     103100\n" +
                            "  -35384.21     -25814.59     0.0000000E+00 0.0000000E+00 -3823.892     013100\n" +
                            "  0.0000000E+00 0.0000000E+00  4141.309      2764.506     0.0000000E+00 004100\n" +
                            "  -203.9691     -47.90571     0.0000000E+00 0.0000000E+00  19.57320     400001\n" +
                            "  -2299.709     -867.7221     0.0000000E+00 0.0000000E+00  272.7963     310001\n" +
                            "  -9529.409     -5125.860     0.0000000E+00 0.0000000E+00  1173.900     220001\n" +
                            "  -16615.34     -12374.86     0.0000000E+00 0.0000000E+00  2058.061     130001\n" +
                            "  -9638.637     -10508.16     0.0000000E+00 0.0000000E+00  1342.927     040001\n" +
                            "  0.0000000E+00 0.0000000E+00  401.0618      87.67140     0.0000000E+00 301001\n" +
                            "  0.0000000E+00 0.0000000E+00  5489.766      2369.554     0.0000000E+00 211001\n" +
                            "  0.0000000E+00 0.0000000E+00  21110.62      11122.82     0.0000000E+00 121001\n" +
                            "  0.0000000E+00 0.0000000E+00  25074.38      14551.07     0.0000000E+00 031001\n" +
                            "   632.1259      340.7551     0.0000000E+00 0.0000000E+00 -140.1561     202001\n" +
                            "   5560.317      3480.008     0.0000000E+00 0.0000000E+00 -453.4738     112001\n" +
                            "   10354.14      6907.929     0.0000000E+00 0.0000000E+00 -117.9547     022001\n" +
                            "  0.0000000E+00 0.0000000E+00 -4182.063     -2522.514     0.0000000E+00 103001\n" +
                            "  0.0000000E+00 0.0000000E+00 -11389.60     -6987.508     0.0000000E+00 013001\n" +
                            "   299.9238      165.0640     0.0000000E+00 0.0000000E+00  212.6243     004001\n" +
                            "   13327.63      6123.234     0.0000000E+00 0.0000000E+00  2420.362     300200\n" +
                            "   62313.63      25159.50     0.0000000E+00 0.0000000E+00  11800.73     210200\n" +
                            "   62099.47      11374.13     0.0000000E+00 0.0000000E+00  14575.30     120200\n" +
                            "  -22672.29     -31208.99     0.0000000E+00 0.0000000E+00  1185.457     030200\n" +
                            "  0.0000000E+00 0.0000000E+00 -39031.48     -25193.77     0.0000000E+00 201200\n" +
                            "  0.0000000E+00 0.0000000E+00 -210110.3     -135931.0     0.0000000E+00 111200\n" +
                            "  0.0000000E+00 0.0000000E+00 -286171.1     -185820.2     0.0000000E+00 021200\n" +
                            "  -48293.06     -34100.70     0.0000000E+00 0.0000000E+00 -10131.97     102200\n" +
                            "  -120279.0     -84257.56     0.0000000E+00 0.0000000E+00 -25016.62     012200\n" +
                            "  0.0000000E+00 0.0000000E+00 -1629.756     -997.2119     0.0000000E+00 003200\n" +
                            "  0.0000000E+00 0.0000000E+00  1387.179      764.0837     0.0000000E+00 300101\n" +
                            "  0.0000000E+00 0.0000000E+00  14642.97      8797.444     0.0000000E+00 210101\n" +
                            "  0.0000000E+00 0.0000000E+00  48067.70      30209.26     0.0000000E+00 120101\n" +
                            "  0.0000000E+00 0.0000000E+00  51001.99      32649.12     0.0000000E+00 030101\n" +
                            "  -658.9191      55.80493     0.0000000E+00 0.0000000E+00 -60.71961     201101\n" +
                            "   7812.031      8167.907     0.0000000E+00 0.0000000E+00  2323.277     111101\n" +
                            "   24168.57      20430.78     0.0000000E+00 0.0000000E+00  6723.616     021101\n" +
                            "  0.0000000E+00 0.0000000E+00 -11645.24     -7973.776     0.0000000E+00 102101\n" +
                            "  0.0000000E+00 0.0000000E+00 -24962.69     -17720.63     0.0000000E+00 012101\n" +
                            "   3701.338      2584.844     0.0000000E+00 0.0000000E+00  707.5397     003101\n" +
                            "   42.10290     -11.73698     0.0000000E+00 0.0000000E+00 -1.149475     300002\n" +
                            "   775.9656      99.43905     0.0000000E+00 0.0000000E+00 -13.82264     210002\n" +
                            "   3388.963      976.4155     0.0000000E+00 0.0000000E+00  26.76092     120002\n" +
                            "   4240.889      1674.463     0.0000000E+00 0.0000000E+00  174.4159     030002\n" +
                            "  0.0000000E+00 0.0000000E+00 -120.7452     0.3257381     0.0000000E+00 201002\n" +
                            "  0.0000000E+00 0.0000000E+00 -1177.759     -357.9652     0.0000000E+00 111002\n" +
                            "  0.0000000E+00 0.0000000E+00 -2446.437     -1076.426     0.0000000E+00 021002\n" +
                            "  -172.5639     -46.09925     0.0000000E+00 0.0000000E+00 -81.55626     102002\n" +
                            "  -1167.020     -534.3624     0.0000000E+00 0.0000000E+00 -318.1877     012002\n" +
                            "  0.0000000E+00 0.0000000E+00  864.5922      476.2311     0.0000000E+00 003002\n" +
                            "  0.0000000E+00 0.0000000E+00 -118626.6     -75699.67     0.0000000E+00 200300\n" +
                            "  0.0000000E+00 0.0000000E+00 -553119.3     -352413.5     0.0000000E+00 110300\n" +
                            "  0.0000000E+00 0.0000000E+00 -636728.6     -404907.9     0.0000000E+00 020300\n" +
                            "  -103288.9     -63404.85     0.0000000E+00 0.0000000E+00 -29527.06     101300\n" +
                            "  -240806.0     -146975.7     0.0000000E+00 0.0000000E+00 -70469.32     011300\n" +
                            "  0.0000000E+00 0.0000000E+00 -22919.08     -13833.99     0.0000000E+00 002300\n" +
                            "  -5366.022     -2580.815     0.0000000E+00 0.0000000E+00  455.2991     200201\n" +
                            "  -11183.54     -2376.783     0.0000000E+00 0.0000000E+00  6368.180     110201\n" +
                            "   2447.065      8383.692     0.0000000E+00 0.0000000E+00  12844.96     020201\n" +
                            "  0.0000000E+00 0.0000000E+00  23942.05      14488.33     0.0000000E+00 101201\n" +
                            "  0.0000000E+00 0.0000000E+00  68864.08      41143.33     0.0000000E+00 011201\n" +
                            "   14151.79      9902.503     0.0000000E+00 0.0000000E+00  3899.567     002201\n" +
                            "  0.0000000E+00 0.0000000E+00 -518.5430     -203.9243     0.0000000E+00 200102\n" +
                            "  0.0000000E+00 0.0000000E+00 -3641.651     -1913.242     0.0000000E+00 110102\n" +
                            "  0.0000000E+00 0.0000000E+00 -6123.397     -3633.481     0.0000000E+00 020102\n" +
                            "  -1235.440     -562.9532     0.0000000E+00 0.0000000E+00 -511.9189     101102\n" +
                            "  -5811.123     -3167.954     0.0000000E+00 0.0000000E+00 -1737.049     011102\n" +
                            "  0.0000000E+00 0.0000000E+00  2256.105      1590.203     0.0000000E+00 002102\n" +
                            "   4.166667      3.939609     0.0000000E+00 0.0000000E+00  1.563392     200003\n" +
                            "   8.899433      21.83205     0.0000000E+00 0.0000000E+00  12.28184     110003\n" +
                            "  -24.23051      20.67116     0.0000000E+00 0.0000000E+00  23.46339     020003\n" +
                            "  0.0000000E+00 0.0000000E+00 -6.018076     -2.611859     0.0000000E+00 101003\n" +
                            "  0.0000000E+00 0.0000000E+00 -3.076833     -5.941312     0.0000000E+00 011003\n" +
                            "   27.03164      6.871723     0.0000000E+00 0.0000000E+00  31.09230     002003\n" +
                            "  -64704.24     -43322.62     0.0000000E+00 0.0000000E+00 -5829.565     100400\n" +
                            "  -141678.9     -94147.59     0.0000000E+00 0.0000000E+00 -12307.03     010400\n" +
                            "  0.0000000E+00 0.0000000E+00  39302.94      26832.38     0.0000000E+00 001400\n" +
                            "  0.0000000E+00 0.0000000E+00  50213.63      35845.77     0.0000000E+00 100301\n" +
                            "  0.0000000E+00 0.0000000E+00  114390.4      80886.62     0.0000000E+00 010301\n" +
                            "   21275.24      14821.69     0.0000000E+00 0.0000000E+00  5467.694     001301\n" +
                            "  -1946.105     -932.9794     0.0000000E+00 0.0000000E+00 -616.0522     100202\n" +
                            "  -6750.962     -3650.756     0.0000000E+00 0.0000000E+00 -1744.855     010202\n" +
                            "  0.0000000E+00 0.0000000E+00 -3129.597     -1432.858     0.0000000E+00 001202\n" +
                            "  0.0000000E+00 0.0000000E+00  1.639589      12.65566     0.0000000E+00 100103\n" +
                            "  0.0000000E+00 0.0000000E+00  78.88242      52.48635     0.0000000E+00 010103\n" +
                            "   288.5438      143.2389     0.0000000E+00 0.0000000E+00  120.7580     001103\n" +
                            "   1.060355     0.3201571     0.0000000E+00 0.0000000E+00-0.4589906E-01 100004\n" +
                            "  0.5814586     0.9139037     0.0000000E+00 0.0000000E+00 0.2721427     010004\n" +
                            "  0.0000000E+00 0.0000000E+00  12.71252      4.076410     0.0000000E+00 001004\n" +
                            "  0.0000000E+00 0.0000000E+00  80016.28      54119.28     0.0000000E+00 000500\n" +
                            "   7337.102      7093.299     0.0000000E+00 0.0000000E+00 -1729.186     000401\n" +
                            "  0.0000000E+00 0.0000000E+00 -2919.675     -2037.259     0.0000000E+00 000302\n" +
                            "   370.0992      246.1442     0.0000000E+00 0.0000000E+00  72.88255     000203\n" +
                            "  0.0000000E+00 0.0000000E+00  38.14318      18.96143     0.0000000E+00 000104\n" +
                            " -0.1184402     0.6672139E-03 0.0000000E+00 0.0000000E+00 0.7764365     000005"
            );

            slice_20200715_后偏转段_最后偏移段20.map = CosyArbitraryOrder.readMap(
                    "  0.7096448     0.7182200     0.0000000E+00 0.0000000E+00 0.1956855E-01 100000\n" +
                            " -0.2531178      1.152979     0.0000000E+00 0.0000000E+00 0.3404286E-01 010000\n" +
                            "  0.0000000E+00 0.0000000E+00 -1.182265     0.8248457E-01 0.0000000E+00 001000\n" +
                            "  0.0000000E+00 0.0000000E+00 0.9314213    -0.9108178     0.0000000E+00 000100\n" +
                            "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00  1.000000     000010\n" +
                            "  0.2911149E-01 0.1888123E-02 0.0000000E+00 0.0000000E+00  1.200518     000001\n" +
                            "  -8.219706     -1.104256     0.0000000E+00 0.0000000E+00 0.5389752     200000\n" +
                            "  -24.37185      2.830949     0.0000000E+00 0.0000000E+00  4.151030     110000\n" +
                            "  -1.501069      17.77472     0.0000000E+00 0.0000000E+00  6.182174     020000\n" +
                            "  0.0000000E+00 0.0000000E+00 -13.54139     0.9215201     0.0000000E+00 101000\n" +
                            "  0.0000000E+00 0.0000000E+00 -38.19823     0.6730656E-01 0.0000000E+00 011000\n" +
                            "  -1.093204     -1.087056     0.0000000E+00 0.0000000E+00-0.9585656     002000\n" +
                            "  0.0000000E+00 0.0000000E+00 -18.06047      10.96635     0.0000000E+00 100100\n" +
                            "  0.0000000E+00 0.0000000E+00 -33.12818      31.68621     0.0000000E+00 010100\n" +
                            "   27.54984      11.71210     0.0000000E+00 0.0000000E+00  1.236700     001100\n" +
                            "   3.135883      1.607916     0.0000000E+00 0.0000000E+00 0.3454779E-01 100001\n" +
                            "   9.054056      3.494993     0.0000000E+00 0.0000000E+00 0.3480578E-01 010001\n" +
                            "  0.0000000E+00 0.0000000E+00 0.6100459     -1.614094     0.0000000E+00 001001\n" +
                            "   1.023527     -3.357507     0.0000000E+00 0.0000000E+00 -5.948455     000200\n" +
                            "  0.0000000E+00 0.0000000E+00  12.94685    -0.1016304     0.0000000E+00 000101\n" +
                            " -0.1848798E-01-0.1424428E-01 0.0000000E+00 0.0000000E+00 -1.046266     000002\n" +
                            "   7.907405      11.91120     0.0000000E+00 0.0000000E+00  4.927677     300000\n" +
                            "  -11.67286      78.32786     0.0000000E+00 0.0000000E+00  34.18004     210000\n" +
                            "  -243.3569      161.1646     0.0000000E+00 0.0000000E+00  85.78998     120000\n" +
                            "  -407.6831      102.9998     0.0000000E+00 0.0000000E+00  81.59862     030000\n" +
                            "  0.0000000E+00 0.0000000E+00 -59.80468     -44.76506     0.0000000E+00 201000\n" +
                            "  0.0000000E+00 0.0000000E+00 -383.3338     -251.3069     0.0000000E+00 111000\n" +
                            "  0.0000000E+00 0.0000000E+00 -592.4048     -348.1983     0.0000000E+00 021000\n" +
                            "  -118.5426     -60.22558     0.0000000E+00 0.0000000E+00 -15.45506     102000\n" +
                            "  -326.6906     -164.8760     0.0000000E+00 0.0000000E+00 -39.63808     012000\n" +
                            "  0.0000000E+00 0.0000000E+00  12.64755      19.64552     0.0000000E+00 003000\n" +
                            "  0.0000000E+00 0.0000000E+00 -16.58746     -29.03069     0.0000000E+00 200100\n" +
                            "  0.0000000E+00 0.0000000E+00 -262.0515     -178.8020     0.0000000E+00 110100\n" +
                            "  0.0000000E+00 0.0000000E+00 -562.0197     -251.9548     0.0000000E+00 020100\n" +
                            "  -158.4637     -60.01883     0.0000000E+00 0.0000000E+00 -18.98018     101100\n" +
                            "  -396.4417     -149.3246     0.0000000E+00 0.0000000E+00 -38.92927     011100\n" +
                            "  0.0000000E+00 0.0000000E+00 -259.3077     -28.56865     0.0000000E+00 002100\n" +
                            "  -12.93533     -5.031258     0.0000000E+00 0.0000000E+00-0.7731203     200001\n" +
                            "  -52.23296     -22.18746     0.0000000E+00 0.0000000E+00 -5.249430     110001\n" +
                            "  -77.79823     -36.22091     0.0000000E+00 0.0000000E+00 -8.452820     020001\n" +
                            "  0.0000000E+00 0.0000000E+00 -11.74646     -1.932934     0.0000000E+00 101001\n" +
                            "  0.0000000E+00 0.0000000E+00  10.38014      1.599533     0.0000000E+00 011001\n" +
                            "   20.42915      10.29154     0.0000000E+00 0.0000000E+00  5.097529     002001\n" +
                            "   299.1544      162.4119     0.0000000E+00 0.0000000E+00  86.39286     100200\n" +
                            "   555.9500      313.1695     0.0000000E+00 0.0000000E+00  209.2632     010200\n" +
                            "  0.0000000E+00 0.0000000E+00 -29.43152      232.4228     0.0000000E+00 001200\n" +
                            "  0.0000000E+00 0.0000000E+00 -38.42427     -14.67287     0.0000000E+00 100101\n" +
                            "  0.0000000E+00 0.0000000E+00 -35.85408     -33.08744     0.0000000E+00 010101\n" +
                            " -0.1309625      7.794487     0.0000000E+00 0.0000000E+00  1.086082     001101\n" +
                            " -0.2240362     0.4182831     0.0000000E+00 0.0000000E+00-0.2284887E-01 100002\n" +
                            "  -5.807081    -0.5919151     0.0000000E+00 0.0000000E+00-0.1523665E-01 010002\n" +
                            "  0.0000000E+00 0.0000000E+00  7.520332      3.558540     0.0000000E+00 001002\n" +
                            "  0.0000000E+00 0.0000000E+00 -562.6902     -73.13924     0.0000000E+00 000300\n" +
                            "  -160.6145     -80.94478     0.0000000E+00 0.0000000E+00 -14.18999     000201\n" +
                            "  0.0000000E+00 0.0000000E+00  4.590722      8.705853     0.0000000E+00 000102\n" +
                            " -0.3807917E-01-0.7852009E-02 0.0000000E+00 0.0000000E+00 0.9233538     000003\n" +
                            "   20.24855      45.67108     0.0000000E+00 0.0000000E+00 -6.505955     400000\n" +
                            "   191.8596      526.4121     0.0000000E+00 0.0000000E+00 -7.431498     310000\n" +
                            "   487.8072      2243.814     0.0000000E+00 0.0000000E+00  159.9672     220000\n" +
                            "  -139.4216      4142.118     0.0000000E+00 0.0000000E+00  491.8013     130000\n" +
                            "  -1215.557      2767.588     0.0000000E+00 0.0000000E+00  363.8535     040000\n" +
                            "  0.0000000E+00 0.0000000E+00 -491.7889     -267.4917     0.0000000E+00 301000\n" +
                            "  0.0000000E+00 0.0000000E+00 -4072.510     -2242.578     0.0000000E+00 211000\n" +
                            "  0.0000000E+00 0.0000000E+00 -11404.81     -6329.488     0.0000000E+00 121000\n" +
                            "  0.0000000E+00 0.0000000E+00 -10809.68     -6001.040     0.0000000E+00 031000\n" +
                            "  -466.5271     -296.9841     0.0000000E+00 0.0000000E+00 -20.88376     202000\n" +
                            "  -2639.400     -1667.670     0.0000000E+00 0.0000000E+00 -161.1483     112000\n" +
                            "  -3714.440     -2335.853     0.0000000E+00 0.0000000E+00 -292.6664     022000\n" +
                            "  0.0000000E+00 0.0000000E+00  826.0690      401.1409     0.0000000E+00 103000\n" +
                            "  0.0000000E+00 0.0000000E+00  1969.268      968.2094     0.0000000E+00 013000\n" +
                            "  -116.3193     -59.73544     0.0000000E+00 0.0000000E+00 -31.54196     004000\n" +
                            "  0.0000000E+00 0.0000000E+00 -534.6913     -266.2128     0.0000000E+00 300100\n" +
                            "  0.0000000E+00 0.0000000E+00 -3820.040     -1890.030     0.0000000E+00 210100\n" +
                            "  0.0000000E+00 0.0000000E+00 -9468.791     -4580.561     0.0000000E+00 120100\n" +
                            "  0.0000000E+00 0.0000000E+00 -8322.993     -3825.309     0.0000000E+00 030100\n" +
                            "  -1700.004     -1017.501     0.0000000E+00 0.0000000E+00 -439.9846     201100\n" +
                            "  -8137.956     -4941.477     0.0000000E+00 0.0000000E+00 -2415.767     111100\n" +
                            "  -9888.945     -6080.364     0.0000000E+00 0.0000000E+00 -3363.165     021100\n" +
                            "  0.0000000E+00 0.0000000E+00 -1262.412     -709.8887     0.0000000E+00 102100\n" +
                            "  0.0000000E+00 0.0000000E+00 -4135.404     -2186.558     0.0000000E+00 012100\n" +
                            "  -675.9197     -360.8397     0.0000000E+00 0.0000000E+00 -81.03378     003100\n" +
                            "  -103.4135     -32.85391     0.0000000E+00 0.0000000E+00  8.056306     300001\n" +
                            "  -951.8539     -360.2841     0.0000000E+00 0.0000000E+00  28.34853     210001\n" +
                            "  -2700.629     -1192.677     0.0000000E+00 0.0000000E+00 -23.89506     120001\n" +
                            "  -2392.898     -1244.102     0.0000000E+00 0.0000000E+00 -129.1896     030001\n" +
                            "  0.0000000E+00 0.0000000E+00  200.1081      82.45150     0.0000000E+00 201001\n" +
                            "  0.0000000E+00 0.0000000E+00  1231.367      557.4906     0.0000000E+00 111001\n" +
                            "  0.0000000E+00 0.0000000E+00  1958.997      925.3016     0.0000000E+00 021001\n" +
                            "   326.0642      143.1158     0.0000000E+00 0.0000000E+00  53.18065     102001\n" +
                            "   1110.501      496.0571     0.0000000E+00 0.0000000E+00  162.6760     012001\n" +
                            "  0.0000000E+00 0.0000000E+00 -300.8995     -144.2605     0.0000000E+00 003001\n" +
                            "  -3565.926     -1669.801     0.0000000E+00 0.0000000E+00 -830.1949     200200\n" +
                            "  -13235.26     -6271.660     0.0000000E+00 0.0000000E+00 -3719.121     110200\n" +
                            "  -10399.47     -5078.153     0.0000000E+00 0.0000000E+00 -4118.337     020200\n" +
                            "  0.0000000E+00 0.0000000E+00 -4850.351     -2583.277     0.0000000E+00 101200\n" +
                            "  0.0000000E+00 0.0000000E+00 -13243.73     -6841.303     0.0000000E+00 011200\n" +
                            "  -322.9325     -78.19633     0.0000000E+00 0.0000000E+00 -303.9657     002200\n" +
                            "  0.0000000E+00 0.0000000E+00  459.6278      266.0688     0.0000000E+00 200101\n" +
                            "  0.0000000E+00 0.0000000E+00  2407.033      1414.086     0.0000000E+00 110101\n" +
                            "  0.0000000E+00 0.0000000E+00  3349.693      1888.899     0.0000000E+00 020101\n" +
                            "   1706.492      830.9958     0.0000000E+00 0.0000000E+00  270.4231     101101\n" +
                            "   4455.434      2147.915     0.0000000E+00 0.0000000E+00  689.1442     011101\n" +
                            "  0.0000000E+00 0.0000000E+00  104.2107     -77.83777     0.0000000E+00 002101\n" +
                            "   3.197267     -1.931596     0.0000000E+00 0.0000000E+00 -1.569546     200002\n" +
                            "   38.78673    -0.1425333     0.0000000E+00 0.0000000E+00 -6.279426     110002\n" +
                            "   94.23176      17.73236     0.0000000E+00 0.0000000E+00 -6.518523     020002\n" +
                            "  0.0000000E+00 0.0000000E+00 0.8703271     -4.428462     0.0000000E+00 101002\n" +
                            "  0.0000000E+00 0.0000000E+00 -20.94197     -12.07670     0.0000000E+00 011002\n" +
                            "  -45.95246     -18.40109     0.0000000E+00 0.0000000E+00 -15.56538     002002\n" +
                            "  0.0000000E+00 0.0000000E+00  9548.316      4508.635     0.0000000E+00 100300\n" +
                            "  0.0000000E+00 0.0000000E+00  21145.16      10055.59     0.0000000E+00 010300\n" +
                            "   3692.110      1943.738     0.0000000E+00 0.0000000E+00  975.2746     001300\n" +
                            "   2208.950      1090.707     0.0000000E+00 0.0000000E+00  248.5283     100201\n" +
                            "   4671.636      2229.222     0.0000000E+00 0.0000000E+00  428.3949     010201\n" +
                            "  0.0000000E+00 0.0000000E+00  711.8898      66.74381     0.0000000E+00 001201\n" +
                            "  0.0000000E+00 0.0000000E+00  8.809019     -14.82511     0.0000000E+00 100102\n" +
                            "  0.0000000E+00 0.0000000E+00 -34.44676     -36.65403     0.0000000E+00 010102\n" +
                            "  -237.1953     -124.4220     0.0000000E+00 0.0000000E+00 -40.08668     001102\n" +
                            " -0.7754727    -0.3889940     0.0000000E+00 0.0000000E+00 0.6337717E-01 100003\n" +
                            "   2.167143    -0.5099878     0.0000000E+00 0.0000000E+00 0.4126799E-02 010003\n" +
                            "  0.0000000E+00 0.0000000E+00 -12.61182     -3.778753     0.0000000E+00 001003\n" +
                            "   5382.514      2897.954     0.0000000E+00 0.0000000E+00  684.4045     000400\n" +
                            "  0.0000000E+00 0.0000000E+00 -2713.282     -1865.547     0.0000000E+00 000301\n" +
                            "  -84.67166     -86.80327     0.0000000E+00 0.0000000E+00 -2.389885     000202\n" +
                            "  0.0000000E+00 0.0000000E+00 -27.71282     -14.22929     0.0000000E+00 000103\n" +
                            "  0.7552048E-01 0.4248511E-02 0.0000000E+00 0.0000000E+00-0.8556035     000004\n" +
                            "   1766.752      796.5245     0.0000000E+00 0.0000000E+00 -50.75635     500000\n" +
                            "   25416.07      11744.50     0.0000000E+00 0.0000000E+00 -822.4317     410000\n" +
                            "   146926.5      69295.91     0.0000000E+00 0.0000000E+00 -4849.743     320000\n" +
                            "   426682.6      204775.6     0.0000000E+00 0.0000000E+00 -13196.82     230000\n" +
                            "   622082.4      303283.2     0.0000000E+00 0.0000000E+00 -16567.56     140000\n" +
                            "   363827.7      180138.9     0.0000000E+00 0.0000000E+00 -7560.658     050000\n" +
                            "  0.0000000E+00 0.0000000E+00 -2417.606     -1326.178     0.0000000E+00 401000\n" +
                            "  0.0000000E+00 0.0000000E+00 -29944.00     -16440.21     0.0000000E+00 311000\n" +
                            "  0.0000000E+00 0.0000000E+00 -137268.0     -75450.38     0.0000000E+00 221000\n" +
                            "  0.0000000E+00 0.0000000E+00 -277193.8     -152596.6     0.0000000E+00 131000\n" +
                            "  0.0000000E+00 0.0000000E+00 -208782.8     -115130.9     0.0000000E+00 041000\n" +
                            "  -2276.743     -1121.733     0.0000000E+00 0.0000000E+00  499.9540     302000\n" +
                            "  -20726.48     -10581.83     0.0000000E+00 0.0000000E+00  3908.011     212000\n" +
                            "  -61544.81     -32400.17     0.0000000E+00 0.0000000E+00  10277.14     122000\n" +
                            "  -60144.51     -32551.16     0.0000000E+00 0.0000000E+00  9088.987     032000\n" +
                            "  0.0000000E+00 0.0000000E+00  7537.394      3803.526     0.0000000E+00 203000\n" +
                            "  0.0000000E+00 0.0000000E+00  39549.89      19965.95     0.0000000E+00 113000\n" +
                            "  0.0000000E+00 0.0000000E+00  52154.44      26309.54     0.0000000E+00 023000\n" +
                            "  -1540.065     -793.6952     0.0000000E+00 0.0000000E+00 -513.6674     104000\n" +
                            "  -4610.906     -2335.866     0.0000000E+00 0.0000000E+00 -1324.834     014000\n" +
                            "  0.0000000E+00 0.0000000E+00  1595.159      786.6171     0.0000000E+00 005000\n" +
                            "  0.0000000E+00 0.0000000E+00 -1904.930     -1301.354     0.0000000E+00 400100\n" +
                            "  0.0000000E+00 0.0000000E+00 -24876.92     -16686.36     0.0000000E+00 310100\n" +
                            "  0.0000000E+00 0.0000000E+00 -115053.5     -76719.33     0.0000000E+00 220100\n" +
                            "  0.0000000E+00 0.0000000E+00 -227358.3     -151851.3     0.0000000E+00 130100\n" +
                            "  0.0000000E+00 0.0000000E+00 -163958.4     -110088.3     0.0000000E+00 040100\n" +
                            "   4167.677      993.3774     0.0000000E+00 0.0000000E+00  1648.413     301100\n" +
                            "   12963.85     -2457.994     0.0000000E+00 0.0000000E+00  10343.96     211100\n" +
                            "  -12952.05     -31323.57     0.0000000E+00 0.0000000E+00  20596.15     121100\n" +
                            "  -48190.33     -47401.61     0.0000000E+00 0.0000000E+00  12440.46     031100\n" +
                            "  0.0000000E+00 0.0000000E+00  26013.41      13027.27     0.0000000E+00 202100\n" +
                            "  0.0000000E+00 0.0000000E+00  125490.6      62829.92     0.0000000E+00 112100\n" +
                            "  0.0000000E+00 0.0000000E+00  151109.3      75498.94     0.0000000E+00 022100\n" +
                            "  -18097.67     -9736.841     0.0000000E+00 0.0000000E+00 -1145.955     103100\n" +
                            "  -48320.61     -25814.59     0.0000000E+00 0.0000000E+00 -3090.222     013100\n" +
                            "  0.0000000E+00 0.0000000E+00  5520.408      2764.506     0.0000000E+00 004100\n" +
                            "  -243.7988     -47.90571     0.0000000E+00 0.0000000E+00  22.81580     400001\n" +
                            "  -2897.147     -867.7221     0.0000000E+00 0.0000000E+00  331.8174     310001\n" +
                            "  -12738.21     -5125.860     0.0000000E+00 0.0000000E+00  1485.116     220001\n" +
                            "  -23934.54     -12374.86     0.0000000E+00 0.0000000E+00  2747.810     130001\n" +
                            "  -15623.55     -10508.16     0.0000000E+00 0.0000000E+00  1933.049     040001\n" +
                            "  0.0000000E+00 0.0000000E+00  520.1580      87.67140     0.0000000E+00 301001\n" +
                            "  0.0000000E+00 0.0000000E+00  7300.155      2369.554     0.0000000E+00 211001\n" +
                            "  0.0000000E+00 0.0000000E+00  28426.83      11122.82     0.0000000E+00 121001\n" +
                            "  0.0000000E+00 0.0000000E+00  34009.70      14551.07     0.0000000E+00 031001\n" +
                            "   887.6513      340.7551     0.0000000E+00 0.0000000E+00 -180.1391     202001\n" +
                            "   7772.508      3480.008     0.0000000E+00 0.0000000E+00 -693.3675     112001\n" +
                            "   14464.33      6907.929     0.0000000E+00 0.0000000E+00 -453.4744     022001\n" +
                            "  0.0000000E+00 0.0000000E+00 -5554.672     -2522.514     0.0000000E+00 103001\n" +
                            "  0.0000000E+00 0.0000000E+00 -15152.43     -6987.508     0.0000000E+00 013001\n" +
                            "   399.2415      165.0640     0.0000000E+00 0.0000000E+00  228.8715     004001\n" +
                            "   16495.26      6123.234     0.0000000E+00 0.0000000E+00  2840.870     300200\n" +
                            "   75464.22      25159.50     0.0000000E+00 0.0000000E+00  13978.05     210200\n" +
                            "   68718.32      11374.13     0.0000000E+00 0.0000000E+00  17924.81     120200\n" +
                            "  -37880.72     -31208.99     0.0000000E+00 0.0000000E+00  2630.863     030200\n" +
                            "  0.0000000E+00 0.0000000E+00 -51553.80     -25193.77     0.0000000E+00 201200\n" +
                            "  0.0000000E+00 0.0000000E+00 -277826.3     -135931.0     0.0000000E+00 111200\n" +
                            "  0.0000000E+00 0.0000000E+00 -378923.1     -185820.2     0.0000000E+00 021200\n" +
                            "  -65253.63     -34100.70     0.0000000E+00 0.0000000E+00 -10019.66     102200\n" +
                            "  -162254.2     -84257.56     0.0000000E+00 0.0000000E+00 -24711.03     012200\n" +
                            "  0.0000000E+00 0.0000000E+00 -2102.955     -997.2119     0.0000000E+00 003200\n" +
                            "  0.0000000E+00 0.0000000E+00  1847.515      764.0837     0.0000000E+00 300101\n" +
                            "  0.0000000E+00 0.0000000E+00  19597.07      8797.444     0.0000000E+00 210101\n" +
                            "  0.0000000E+00 0.0000000E+00  64513.82      30209.26     0.0000000E+00 120101\n" +
                            "  0.0000000E+00 0.0000000E+00  68435.99      32649.12     0.0000000E+00 030101\n" +
                            "  -338.0087      55.80493     0.0000000E+00 0.0000000E+00 -174.3099     201101\n" +
                            "   13312.21      8167.907     0.0000000E+00 0.0000000E+00  1902.186     111101\n" +
                            "   36127.69      20430.78     0.0000000E+00 0.0000000E+00  6451.723     021101\n" +
                            "  0.0000000E+00 0.0000000E+00 -15443.20     -7973.776     0.0000000E+00 102101\n" +
                            "  0.0000000E+00 0.0000000E+00 -33233.17     -17720.63     0.0000000E+00 012101\n" +
                            "   5092.471      2584.844     0.0000000E+00 0.0000000E+00  615.6569     003101\n" +
                            "   48.57046     -11.73698     0.0000000E+00 0.0000000E+00-0.1032081     300002\n" +
                            "   946.2380      99.43905     0.0000000E+00 0.0000000E+00 -7.242885     210002\n" +
                            "   4250.509      976.4155     0.0000000E+00 0.0000000E+00  47.48282     120002\n" +
                            "   5451.287      1674.463     0.0000000E+00 0.0000000E+00  206.1186     030002\n" +
                            "  0.0000000E+00 0.0000000E+00 -152.9047     0.3257381     0.0000000E+00 201002\n" +
                            "  0.0000000E+00 0.0000000E+00 -1564.901     -357.9652     0.0000000E+00 111002\n" +
                            "  0.0000000E+00 0.0000000E+00 -3315.684     -1076.426     0.0000000E+00 021002\n" +
                            "  -247.3530     -46.09925     0.0000000E+00 0.0000000E+00 -82.41359     102002\n" +
                            "  -1605.695     -534.3624     0.0000000E+00 0.0000000E+00 -318.6562     012002\n" +
                            "  0.0000000E+00 0.0000000E+00  1147.233      476.2311     0.0000000E+00 003002\n" +
                            "  0.0000000E+00 0.0000000E+00 -156654.3     -75699.67     0.0000000E+00 200300\n" +
                            "  0.0000000E+00 0.0000000E+00 -730185.6     -352413.5     0.0000000E+00 110300\n" +
                            "  0.0000000E+00 0.0000000E+00 -840248.5     -404907.9     0.0000000E+00 020300\n" +
                            "  -135189.0     -63404.85     0.0000000E+00 0.0000000E+00 -31963.15     101300\n" +
                            "  -314703.4     -146975.7     0.0000000E+00 0.0000000E+00 -76296.83     011300\n" +
                            "  0.0000000E+00 0.0000000E+00 -29914.94     -13833.99     0.0000000E+00 002300\n" +
                            "  -6222.763     -2580.815     0.0000000E+00 0.0000000E+00  331.0781     200201\n" +
                            "  -10735.95     -2376.783     0.0000000E+00 0.0000000E+00  6275.960     110201\n" +
                            "   7943.963      8383.692     0.0000000E+00 0.0000000E+00  13380.42     020201\n" +
                            "  0.0000000E+00 0.0000000E+00  31928.39      14488.33     0.0000000E+00 101201\n" +
                            "  0.0000000E+00 0.0000000E+00  91413.97      41143.33     0.0000000E+00 011201\n" +
                            "   19136.69      9902.503     0.0000000E+00 0.0000000E+00  3990.074     002201\n" +
                            "  0.0000000E+00 0.0000000E+00 -699.8658     -203.9243     0.0000000E+00 200102\n" +
                            "  0.0000000E+00 0.0000000E+00 -5026.808     -1913.242     0.0000000E+00 110102\n" +
                            "  0.0000000E+00 0.0000000E+00 -8516.859     -3633.481     0.0000000E+00 020102\n" +
                            "  -1762.072     -562.9532     0.0000000E+00 0.0000000E+00 -514.4998     101102\n" +
                            "  -8026.142     -3167.954     0.0000000E+00 0.0000000E+00 -1763.611     011102\n" +
                            "  0.0000000E+00 0.0000000E+00  3064.630      1590.203     0.0000000E+00 002102\n" +
                            "   5.814401      3.939609     0.0000000E+00 0.0000000E+00  1.655039     200003\n" +
                            "   14.76058      21.83205     0.0000000E+00 0.0000000E+00  12.85044     110003\n" +
                            "  -29.33819      20.67116     0.0000000E+00 0.0000000E+00  24.51446     020003\n" +
                            "  0.0000000E+00 0.0000000E+00 -6.628634     -2.611859     0.0000000E+00 101003\n" +
                            "  0.0000000E+00 0.0000000E+00 -2.344025     -5.941312     0.0000000E+00 011003\n" +
                            "   37.90245      6.871723     0.0000000E+00 0.0000000E+00  33.57095     002003\n" +
                            "  -86285.01     -43322.62     0.0000000E+00 0.0000000E+00 -4882.997     100400\n" +
                            "  -188591.0     -94147.59     0.0000000E+00 0.0000000E+00 -9729.033     010400\n" +
                            "  0.0000000E+00 0.0000000E+00  52890.17      26832.38     0.0000000E+00 001400\n" +
                            "  0.0000000E+00 0.0000000E+00  66884.79      35845.77     0.0000000E+00 100301\n" +
                            "  0.0000000E+00 0.0000000E+00  152019.7      80886.62     0.0000000E+00 010301\n" +
                            "   28141.17      14821.69     0.0000000E+00 0.0000000E+00  5666.585     001301\n" +
                            "  -2687.062     -932.9794     0.0000000E+00 0.0000000E+00 -607.2659     100202\n" +
                            "  -9140.265     -3650.756     0.0000000E+00 0.0000000E+00 -1740.858     010202\n" +
                            "  0.0000000E+00 0.0000000E+00 -3814.205     -1432.858     0.0000000E+00 001202\n" +
                            "  0.0000000E+00 0.0000000E+00  7.265270      12.65566     0.0000000E+00 100103\n" +
                            "  0.0000000E+00 0.0000000E+00  103.2685      52.48635     0.0000000E+00 010103\n" +
                            "   394.5283      143.2389     0.0000000E+00 0.0000000E+00  122.4495     001103\n" +
                            "   1.248997     0.3201571     0.0000000E+00 0.0000000E+00-0.4827996E-01 100004\n" +
                            "  0.6392483     0.9139037     0.0000000E+00 0.0000000E+00 0.2580551     010004\n" +
                            "  0.0000000E+00 0.0000000E+00  16.81987      4.076410     0.0000000E+00 001004\n" +
                            "  0.0000000E+00 0.0000000E+00  107027.7      54119.28     0.0000000E+00 000500\n" +
                            "   10058.39      7093.299     0.0000000E+00 0.0000000E+00 -2253.706     000401\n" +
                            "  0.0000000E+00 0.0000000E+00 -3426.917     -2037.259     0.0000000E+00 000302\n" +
                            "   501.4095      246.1442     0.0000000E+00 0.0000000E+00  66.13676     000203\n" +
                            "  0.0000000E+00 0.0000000E+00  53.26545      18.96143     0.0000000E+00 000104\n" +
                            " -0.1182189     0.6672139E-03 0.0000000E+00 0.0000000E+00 0.8329297     000005"
            );

            slice_20200715_后偏转段全段.map = CosyArbitraryOrder.readMap(
                    "  0.9969328     0.7182200     0.0000000E+00 0.0000000E+00 0.1956855E-01 100000\n" +
                            "  0.2080739      1.152979     0.0000000E+00 0.0000000E+00 0.3404286E-01 010000\n" +
                            "  0.0000000E+00 0.0000000E+00 -1.149271     0.8248457E-01 0.0000000E+00 001000\n" +
                            "  0.0000000E+00 0.0000000E+00 0.5670942    -0.9108178     0.0000000E+00 000100\n" +
                            "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00  1.000000     000010\n" +
                            "  0.2986673E-01 0.1888123E-02 0.0000000E+00 0.0000000E+00  1.278387     000001\n" +
                            "  -8.661409     -1.104256     0.0000000E+00 0.0000000E+00 0.4813269     200000\n" +
                            "  -23.23947      2.830949     0.0000000E+00 0.0000000E+00  3.965941     110000\n" +
                            "   5.608818      17.77472     0.0000000E+00 0.0000000E+00  6.033610     020000\n" +
                            "  0.0000000E+00 0.0000000E+00 -13.17278     0.9215201     0.0000000E+00 101000\n" +
                            "  0.0000000E+00 0.0000000E+00 -38.17130     0.6730656E-01 0.0000000E+00 011000\n" +
                            "  -1.528027     -1.087056     0.0000000E+00 0.0000000E+00-0.9593260     002000\n" +
                            "  0.0000000E+00 0.0000000E+00 -13.67393      10.96635     0.0000000E+00 100100\n" +
                            "  0.0000000E+00 0.0000000E+00 -20.45369      31.68621     0.0000000E+00 010100\n" +
                            "   32.23468      11.71210     0.0000000E+00 0.0000000E+00  1.253493     001100\n" +
                            "   3.618518      1.607916     0.0000000E+00 0.0000000E+00 0.3424469E-01 100001\n" +
                            "   10.19435      3.494993     0.0000000E+00 0.0000000E+00 0.3431921E-01 010001\n" +
                            "  0.0000000E+00 0.0000000E+00-0.5402818E-01 -1.614094     0.0000000E+00 001001\n" +
                            " -0.3194756     -3.357507     0.0000000E+00 0.0000000E+00 -6.041167     000200\n" +
                            "  0.0000000E+00 0.0000000E+00  13.10978    -0.1016304     0.0000000E+00 000101\n" +
                            " -0.2460772E-01-0.1424428E-01 0.0000000E+00 0.0000000E+00 -1.111535     000002\n" +
                            "   12.74598      11.91120     0.0000000E+00 0.0000000E+00  5.104944     300000\n" +
                            "   20.01514      78.32786     0.0000000E+00 0.0000000E+00  34.01015     210000\n" +
                            "  -178.3182      161.1646     0.0000000E+00 0.0000000E+00  82.20704     120000\n" +
                            "  -366.1767      102.9998     0.0000000E+00 0.0000000E+00  77.01798     030000\n" +
                            "  0.0000000E+00 0.0000000E+00 -77.70220     -44.76506     0.0000000E+00 201000\n" +
                            "  0.0000000E+00 0.0000000E+00 -483.8293     -251.3069     0.0000000E+00 111000\n" +
                            "  0.0000000E+00 0.0000000E+00 -731.6622     -348.1983     0.0000000E+00 021000\n" +
                            "  -142.6319     -60.22558     0.0000000E+00 0.0000000E+00 -15.29754     102000\n" +
                            "  -392.6395     -164.8760     0.0000000E+00 0.0000000E+00 -39.35919     012000\n" +
                            "  0.0000000E+00 0.0000000E+00  20.50587      19.64552     0.0000000E+00 003000\n" +
                            "  0.0000000E+00 0.0000000E+00 -28.29370     -29.03069     0.0000000E+00 200100\n" +
                            "  0.0000000E+00 0.0000000E+00 -333.8740     -178.8020     0.0000000E+00 110100\n" +
                            "  0.0000000E+00 0.0000000E+00 -663.0438     -251.9548     0.0000000E+00 020100\n" +
                            "  -182.4928     -60.01883     0.0000000E+00 0.0000000E+00 -20.87491     101100\n" +
                            "  -456.2062     -149.3246     0.0000000E+00 0.0000000E+00 -42.51801     011100\n" +
                            "  0.0000000E+00 0.0000000E+00 -270.7389     -28.56865     0.0000000E+00 002100\n" +
                            "  -14.70043     -5.031258     0.0000000E+00 0.0000000E+00-0.9462651     200001\n" +
                            "  -61.73882     -22.18746     0.0000000E+00 0.0000000E+00 -5.954716     110001\n" +
                            "  -96.25795     -36.22091     0.0000000E+00 0.0000000E+00 -9.143211     020001\n" +
                            "  0.0000000E+00 0.0000000E+00 -12.72556     -1.932934     0.0000000E+00 101001\n" +
                            "  0.0000000E+00 0.0000000E+00  11.00498      1.599533     0.0000000E+00 011001\n" +
                            "   24.78874      10.29154     0.0000000E+00 0.0000000E+00  5.128860     002001\n" +
                            "   364.2383      162.4119     0.0000000E+00 0.0000000E+00  89.16436     100200\n" +
                            "   681.4090      313.1695     0.0000000E+00 0.0000000E+00  216.5791     010200\n" +
                            "  0.0000000E+00 0.0000000E+00  63.57865      232.4228     0.0000000E+00 001200\n" +
                            "  0.0000000E+00 0.0000000E+00 -46.74503     -14.67287     0.0000000E+00 100101\n" +
                            "  0.0000000E+00 0.0000000E+00 -56.17211     -33.08744     0.0000000E+00 010101\n" +
                            "  0.3689772      7.794487     0.0000000E+00 0.0000000E+00 0.7298004     001101\n" +
                            " -0.2984451     0.4182831     0.0000000E+00 0.0000000E+00-0.2079646E-01 100002\n" +
                            "  -6.636126    -0.5919151     0.0000000E+00 0.0000000E+00-0.1232748E-01 010002\n" +
                            "  0.0000000E+00 0.0000000E+00  9.318031      3.558540     0.0000000E+00 001002\n" +
                            "  0.0000000E+00 0.0000000E+00 -592.0970     -73.13924     0.0000000E+00 000300\n" +
                            "  -192.2417     -80.94478     0.0000000E+00 0.0000000E+00 -14.07335     000201\n" +
                            "  0.0000000E+00 0.0000000E+00  7.946559      8.705853     0.0000000E+00 000102\n" +
                            " -0.3772687E-01-0.7852009E-02 0.0000000E+00 0.0000000E+00 0.9795675     000003\n" +
                            "   38.17521      45.67108     0.0000000E+00 0.0000000E+00 -8.576648     400000\n" +
                            "   402.2033      526.4121     0.0000000E+00 0.0000000E+00 -22.51962     310000\n" +
                            "   1392.767      2243.814     0.0000000E+00 0.0000000E+00  117.0564     220000\n" +
                            "   1537.346      4142.118     0.0000000E+00 0.0000000E+00  422.1176     130000\n" +
                            "  -94.34468      2767.588     0.0000000E+00 0.0000000E+00  301.8534     040000\n" +
                            "  0.0000000E+00 0.0000000E+00 -598.7166     -267.4917     0.0000000E+00 301000\n" +
                            "  0.0000000E+00 0.0000000E+00 -4969.204     -2242.578     0.0000000E+00 211000\n" +
                            "  0.0000000E+00 0.0000000E+00 -13935.81     -6329.488     0.0000000E+00 121000\n" +
                            "  0.0000000E+00 0.0000000E+00 -13209.40     -6001.040     0.0000000E+00 031000\n" +
                            "  -585.6368     -296.9841     0.0000000E+00 0.0000000E+00 -10.75418     202000\n" +
                            "  -3307.508     -1667.670     0.0000000E+00 0.0000000E+00 -113.8549     112000\n" +
                            "  -4649.621     -2335.853     0.0000000E+00 0.0000000E+00 -239.4408     022000\n" +
                            "  0.0000000E+00 0.0000000E+00  986.5034      401.1409     0.0000000E+00 103000\n" +
                            "  0.0000000E+00 0.0000000E+00  2356.511      968.2094     0.0000000E+00 013000\n" +
                            "  -140.2150     -59.73544     0.0000000E+00 0.0000000E+00 -32.03622     004000\n" +
                            "  0.0000000E+00 0.0000000E+00 -639.7561     -266.2128     0.0000000E+00 300100\n" +
                            "  0.0000000E+00 0.0000000E+00 -4569.427     -1890.030     0.0000000E+00 210100\n" +
                            "  0.0000000E+00 0.0000000E+00 -11293.44     -4580.561     0.0000000E+00 120100\n" +
                            "  0.0000000E+00 0.0000000E+00 -9852.159     -3825.309     0.0000000E+00 030100\n" +
                            "  -2103.327     -1017.501     0.0000000E+00 0.0000000E+00 -438.2828     201100\n" +
                            "  -10102.23     -4941.477     0.0000000E+00 0.0000000E+00 -2438.254     111100\n" +
                            "  -12311.11     -6080.364     0.0000000E+00 0.0000000E+00 -3437.898     021100\n" +
                            "  0.0000000E+00 0.0000000E+00 -1545.844     -709.8887     0.0000000E+00 102100\n" +
                            "  0.0000000E+00 0.0000000E+00 -5009.001     -2186.558     0.0000000E+00 012100\n" +
                            "  -820.2070     -360.8397     0.0000000E+00 0.0000000E+00 -73.66180     003100\n" +
                            "  -118.8457     -32.85391     0.0000000E+00 0.0000000E+00  8.995709     300001\n" +
                            "  -1111.392     -360.2841     0.0000000E+00 0.0000000E+00  33.26694     210001\n" +
                            "  -3209.890     -1192.677     0.0000000E+00 0.0000000E+00 -15.77957     120001\n" +
                            "  -2911.240     -1244.102     0.0000000E+00 0.0000000E+00 -127.0699     030001\n" +
                            "  0.0000000E+00 0.0000000E+00  242.9520      82.45150     0.0000000E+00 201001\n" +
                            "  0.0000000E+00 0.0000000E+00  1510.098      557.4906     0.0000000E+00 111001\n" +
                            "  0.0000000E+00 0.0000000E+00  2406.613      925.3016     0.0000000E+00 021001\n" +
                            "   396.7323      143.1158     0.0000000E+00 0.0000000E+00  52.08181     102001\n" +
                            "   1345.713      496.0571     0.0000000E+00 0.0000000E+00  160.5285     012001\n" +
                            "  0.0000000E+00 0.0000000E+00 -363.0015     -144.2605     0.0000000E+00 003001\n" +
                            "  -4237.939     -1669.801     0.0000000E+00 0.0000000E+00 -876.5174     200200\n" +
                            "  -15759.69     -6271.660     0.0000000E+00 0.0000000E+00 -3923.422     110200\n" +
                            "  -12443.77     -5078.153     0.0000000E+00 0.0000000E+00 -4349.386     020200\n" +
                            "  0.0000000E+00 0.0000000E+00 -5887.336     -2583.277     0.0000000E+00 101200\n" +
                            "  0.0000000E+00 0.0000000E+00 -15988.12     -6841.303     0.0000000E+00 011200\n" +
                            "  -354.7479     -78.19633     0.0000000E+00 0.0000000E+00 -330.2153     002200\n" +
                            "  0.0000000E+00 0.0000000E+00  572.2770      266.0688     0.0000000E+00 200101\n" +
                            "  0.0000000E+00 0.0000000E+00  3011.539      1414.086     0.0000000E+00 110101\n" +
                            "  0.0000000E+00 0.0000000E+00  4160.494      1888.899     0.0000000E+00 020101\n" +
                            "   2052.732      830.9958     0.0000000E+00 0.0000000E+00  271.6199     101101\n" +
                            "   5348.636      2147.915     0.0000000E+00 0.0000000E+00  695.6790     011101\n" +
                            "  0.0000000E+00 0.0000000E+00  79.61381     -77.83777     0.0000000E+00 002101\n" +
                            "   3.365493     -1.931596     0.0000000E+00 0.0000000E+00 -1.653812     200002\n" +
                            "   44.14528    -0.1425333     0.0000000E+00 0.0000000E+00 -6.433731     110002\n" +
                            "   112.3278      17.73236     0.0000000E+00 0.0000000E+00 -6.597170     020002\n" +
                            "  0.0000000E+00 0.0000000E+00-0.3192387     -4.428462     0.0000000E+00 101002\n" +
                            "  0.0000000E+00 0.0000000E+00 -26.12099     -12.07670     0.0000000E+00 011002\n" +
                            "  -55.79140     -18.40109     0.0000000E+00 0.0000000E+00 -15.97562     002002\n" +
                            "  0.0000000E+00 0.0000000E+00  11358.11      4508.635     0.0000000E+00 100300\n" +
                            "  0.0000000E+00 0.0000000E+00  25184.58      10055.59     0.0000000E+00 010300\n" +
                            "   4471.650      1943.738     0.0000000E+00 0.0000000E+00  1032.750     001300\n" +
                            "   2609.012      1090.707     0.0000000E+00 0.0000000E+00  255.8593     100201\n" +
                            "   5493.599      2229.222     0.0000000E+00 0.0000000E+00  435.0039     010201\n" +
                            "  0.0000000E+00 0.0000000E+00  685.7666      66.74381     0.0000000E+00 001201\n" +
                            "  0.0000000E+00 0.0000000E+00  7.958553     -14.82511     0.0000000E+00 100102\n" +
                            "  0.0000000E+00 0.0000000E+00 -36.51695     -36.65403     0.0000000E+00 010102\n" +
                            "  -286.7858     -124.4220     0.0000000E+00 0.0000000E+00 -39.00876     001102\n" +
                            " -0.8581607    -0.3889940     0.0000000E+00 0.0000000E+00 0.6667356E-01 100003\n" +
                            "   2.512279    -0.5099878     0.0000000E+00 0.0000000E+00 0.1342628E-01 010003\n" +
                            "  0.0000000E+00 0.0000000E+00 -15.19428     -3.778753     0.0000000E+00 001003\n" +
                            "   6541.139      2897.954     0.0000000E+00 0.0000000E+00  668.1974     000400\n" +
                            "  0.0000000E+00 0.0000000E+00 -3442.948     -1865.547     0.0000000E+00 000301\n" +
                            "  -101.8537     -86.80327     0.0000000E+00 0.0000000E+00-0.7350498     000202\n" +
                            "  0.0000000E+00 0.0000000E+00 -35.24402     -14.22929     0.0000000E+00 000103\n" +
                            "  0.7638624E-01 0.4248511E-02 0.0000000E+00 0.0000000E+00-0.9054801     000004\n" +
                            "   2089.602      796.5245     0.0000000E+00 0.0000000E+00 -55.01095     500000\n" +
                            "   30148.33      11744.50     0.0000000E+00 0.0000000E+00 -906.6022     410000\n" +
                            "   174765.0      69295.91     0.0000000E+00 0.0000000E+00 -5405.551     320000\n" +
                            "   508870.2      204775.6     0.0000000E+00 0.0000000E+00 -14840.52     230000\n" +
                            "   743833.3      303283.2     0.0000000E+00 0.0000000E+00 -18803.27     140000\n" +
                            "   436184.3      180138.9     0.0000000E+00 0.0000000E+00 -8692.219     050000\n" +
                            "  0.0000000E+00 0.0000000E+00 -2952.682     -1326.178     0.0000000E+00 401000\n" +
                            "  0.0000000E+00 0.0000000E+00 -36558.35     -16440.21     0.0000000E+00 311000\n" +
                            "  0.0000000E+00 0.0000000E+00 -167566.9     -75450.38     0.0000000E+00 221000\n" +
                            "  0.0000000E+00 0.0000000E+00 -338396.4     -152596.6     0.0000000E+00 131000\n" +
                            "  0.0000000E+00 0.0000000E+00 -254918.1     -115130.9     0.0000000E+00 041000\n" +
                            "  -2743.997     -1121.733     0.0000000E+00 0.0000000E+00  549.9342     302000\n" +
                            "  -25078.33     -10581.83     0.0000000E+00 0.0000000E+00  4363.089     212000\n" +
                            "  -74754.68     -32400.17     0.0000000E+00 0.0000000E+00  11657.80     122000\n" +
                            "  -73336.28     -32551.16     0.0000000E+00 0.0000000E+00  10487.39     032000\n" +
                            "  0.0000000E+00 0.0000000E+00  9059.015      3803.526     0.0000000E+00 203000\n" +
                            "  0.0000000E+00 0.0000000E+00  47534.98      19965.95     0.0000000E+00 113000\n" +
                            "  0.0000000E+00 0.0000000E+00  62675.11      26309.54     0.0000000E+00 023000\n" +
                            "  -1856.684     -793.6952     0.0000000E+00 0.0000000E+00 -530.1514     104000\n" +
                            "  -5543.914     -2335.866     0.0000000E+00 0.0000000E+00 -1367.643     014000\n" +
                            "  0.0000000E+00 0.0000000E+00  1909.906      786.6171     0.0000000E+00 005000\n" +
                            "  0.0000000E+00 0.0000000E+00 -2435.321     -1301.354     0.0000000E+00 400100\n" +
                            "  0.0000000E+00 0.0000000E+00 -31610.84     -16686.36     0.0000000E+00 310100\n" +
                            "  0.0000000E+00 0.0000000E+00 -145824.1     -76719.33     0.0000000E+00 220100\n" +
                            "  0.0000000E+00 0.0000000E+00 -288050.3     -151851.3     0.0000000E+00 130100\n" +
                            "  0.0000000E+00 0.0000000E+00 -207902.0     -110088.3     0.0000000E+00 040100\n" +
                            "   4548.830      993.3774     0.0000000E+00 0.0000000E+00  1830.409     301100\n" +
                            "   11974.47     -2457.994     0.0000000E+00 0.0000000E+00  11736.65     211100\n" +
                            "  -25255.73     -31323.57     0.0000000E+00 0.0000000E+00  24228.61     121100\n" +
                            "  -66829.40     -47401.61     0.0000000E+00 0.0000000E+00  15643.55     031100\n" +
                            "  0.0000000E+00 0.0000000E+00  31238.97      13027.27     0.0000000E+00 202100\n" +
                            "  0.0000000E+00 0.0000000E+00  150692.5      62829.92     0.0000000E+00 112100\n" +
                            "  0.0000000E+00 0.0000000E+00  181393.8      75498.94     0.0000000E+00 022100\n" +
                            "  -22007.14     -9736.841     0.0000000E+00 0.0000000E+00 -892.5258     103100\n" +
                            "  -58669.74     -25814.59     0.0000000E+00 0.0000000E+00 -2503.287     013100\n" +
                            "  0.0000000E+00 0.0000000E+00  6623.687      2764.506     0.0000000E+00 004100\n" +
                            "  -275.6625     -47.90571     0.0000000E+00 0.0000000E+00  25.40987     400001\n" +
                            "  -3375.097     -867.7221     0.0000000E+00 0.0000000E+00  379.0344     310001\n" +
                            "  -15305.25     -5125.860     0.0000000E+00 0.0000000E+00  1734.090     220001\n" +
                            "  -29789.90     -12374.86     0.0000000E+00 0.0000000E+00  3299.610     130001\n" +
                            "  -20411.48     -10508.16     0.0000000E+00 0.0000000E+00  2405.146     040001\n" +
                            "  0.0000000E+00 0.0000000E+00  615.4350      87.67140     0.0000000E+00 301001\n" +
                            "  0.0000000E+00 0.0000000E+00  8748.467      2369.554     0.0000000E+00 211001\n" +
                            "  0.0000000E+00 0.0000000E+00  34279.79      11122.82     0.0000000E+00 121001\n" +
                            "  0.0000000E+00 0.0000000E+00  41157.95      14551.07     0.0000000E+00 031001\n" +
                            "   1092.072      340.7551     0.0000000E+00 0.0000000E+00 -212.1255     202001\n" +
                            "   9542.261      3480.008     0.0000000E+00 0.0000000E+00 -885.2825     112001\n" +
                            "   17752.48      6907.929     0.0000000E+00 0.0000000E+00 -721.8902     022001\n" +
                            "  0.0000000E+00 0.0000000E+00 -6652.759     -2522.514     0.0000000E+00 103001\n" +
                            "  0.0000000E+00 0.0000000E+00 -18162.69     -6987.508     0.0000000E+00 013001\n" +
                            "   478.6956      165.0640     0.0000000E+00 0.0000000E+00  241.8692     004001\n" +
                            "   19029.37      6123.234     0.0000000E+00 0.0000000E+00  3177.276     300200\n" +
                            "   85984.70      25159.50     0.0000000E+00 0.0000000E+00  15719.90     210200\n" +
                            "   74013.39      11374.13     0.0000000E+00 0.0000000E+00  20604.43     120200\n" +
                            "  -50047.46     -31208.99     0.0000000E+00 0.0000000E+00  3787.188     030200\n" +
                            "  0.0000000E+00 0.0000000E+00 -61571.66     -25193.77     0.0000000E+00 201200\n" +
                            "  0.0000000E+00 0.0000000E+00 -331999.0     -135931.0     0.0000000E+00 111200\n" +
                            "  0.0000000E+00 0.0000000E+00 -453124.7     -185820.2     0.0000000E+00 021200\n" +
                            "  -78822.09     -34100.70     0.0000000E+00 0.0000000E+00 -9929.814     102200\n" +
                            "  -195834.4     -84257.56     0.0000000E+00 0.0000000E+00 -24466.56     012200\n" +
                            "  0.0000000E+00 0.0000000E+00 -2481.515     -997.2119     0.0000000E+00 003200\n" +
                            "  0.0000000E+00 0.0000000E+00  2215.783      764.0837     0.0000000E+00 300101\n" +
                            "  0.0000000E+00 0.0000000E+00  23560.35      8797.444     0.0000000E+00 210101\n" +
                            "  0.0000000E+00 0.0000000E+00  77670.71      30209.26     0.0000000E+00 120101\n" +
                            "  0.0000000E+00 0.0000000E+00  82383.20      32649.12     0.0000000E+00 030101\n" +
                            "  -81.28033      55.80493     0.0000000E+00 0.0000000E+00 -265.1822     201101\n" +
                            "   17712.35      8167.907     0.0000000E+00 0.0000000E+00  1565.313     111101\n" +
                            "   45694.99      20430.78     0.0000000E+00 0.0000000E+00  6234.209     021101\n" +
                            "  0.0000000E+00 0.0000000E+00 -18481.57     -7973.776     0.0000000E+00 102101\n" +
                            "  0.0000000E+00 0.0000000E+00 -39849.54     -17720.63     0.0000000E+00 012101\n" +
                            "   6205.377      2584.844     0.0000000E+00 0.0000000E+00  542.1507     003101\n" +
                            "   53.74451     -11.73698     0.0000000E+00 0.0000000E+00 0.7338053     300002\n" +
                            "   1082.456      99.43905     0.0000000E+00 0.0000000E+00 -1.979077     210002\n" +
                            "   4939.745      976.4155     0.0000000E+00 0.0000000E+00  64.06034     120002\n" +
                            "   6419.606      1674.463     0.0000000E+00 0.0000000E+00  231.4807     030002\n" +
                            "  0.0000000E+00 0.0000000E+00 -178.6323     0.3257381     0.0000000E+00 201002\n" +
                            "  0.0000000E+00 0.0000000E+00 -1874.615     -357.9652     0.0000000E+00 111002\n" +
                            "  0.0000000E+00 0.0000000E+00 -4011.081     -1076.426     0.0000000E+00 021002\n" +
                            "  -307.1843     -46.09925     0.0000000E+00 0.0000000E+00 -83.09945     102002\n" +
                            "  -1956.635     -534.3624     0.0000000E+00 0.0000000E+00 -319.0310     012002\n" +
                            "  0.0000000E+00 0.0000000E+00  1373.345      476.2311     0.0000000E+00 003002\n" +
                            "  0.0000000E+00 0.0000000E+00 -187076.4     -75699.67     0.0000000E+00 200300\n" +
                            "  0.0000000E+00 0.0000000E+00 -871838.6     -352413.5     0.0000000E+00 110300\n" +
                            "  0.0000000E+00 0.0000000E+00 -1003064.     -404907.9     0.0000000E+00 020300\n" +
                            "  -160709.2     -63404.85     0.0000000E+00 0.0000000E+00 -33912.02     101300\n" +
                            "  -373821.4     -146975.7     0.0000000E+00 0.0000000E+00 -80958.84     011300\n" +
                            "  0.0000000E+00 0.0000000E+00 -35511.63     -13833.99     0.0000000E+00 002300\n" +
                            "  -6908.156     -2580.815     0.0000000E+00 0.0000000E+00  231.7013     200201\n" +
                            "  -10377.88     -2376.783     0.0000000E+00 0.0000000E+00  6202.185     110201\n" +
                            "   12341.48      8383.692     0.0000000E+00 0.0000000E+00  13808.79     020201\n" +
                            "  0.0000000E+00 0.0000000E+00  38317.47      14488.33     0.0000000E+00 101201\n" +
                            "  0.0000000E+00 0.0000000E+00  109453.9      41143.33     0.0000000E+00 011201\n" +
                            "   23124.62      9902.503     0.0000000E+00 0.0000000E+00  4062.481     002201\n" +
                            "  0.0000000E+00 0.0000000E+00 -844.9240     -203.9243     0.0000000E+00 200102\n" +
                            "  0.0000000E+00 0.0000000E+00 -6134.934     -1913.242     0.0000000E+00 110102\n" +
                            "  0.0000000E+00 0.0000000E+00 -10431.63     -3633.481     0.0000000E+00 020102\n" +
                            "  -2183.378     -562.9532     0.0000000E+00 0.0000000E+00 -516.5645     101102\n" +
                            "  -9798.158     -3167.954     0.0000000E+00 0.0000000E+00 -1784.861     011102\n" +
                            "  0.0000000E+00 0.0000000E+00  3711.450      1590.203     0.0000000E+00 002102\n" +
                            "   7.132588      3.939609     0.0000000E+00 0.0000000E+00  1.728356     200003\n" +
                            "   19.44949      21.83205     0.0000000E+00 0.0000000E+00  13.30532     110003\n" +
                            "  -33.42433      20.67116     0.0000000E+00 0.0000000E+00  25.35531     020003\n" +
                            "  0.0000000E+00 0.0000000E+00 -7.117081     -2.611859     0.0000000E+00 101003\n" +
                            "  0.0000000E+00 0.0000000E+00 -1.757779     -5.941312     0.0000000E+00 011003\n" +
                            "   46.59910      6.871723     0.0000000E+00 0.0000000E+00  35.55387     002003\n" +
                            "  -103549.6     -43322.62     0.0000000E+00 0.0000000E+00 -4125.742     100400\n" +
                            "  -226120.6     -94147.59     0.0000000E+00 0.0000000E+00 -7666.632     010400\n" +
                            "  0.0000000E+00 0.0000000E+00  63759.96      26832.38     0.0000000E+00 001400\n" +
                            "  0.0000000E+00 0.0000000E+00  80221.72      35845.77     0.0000000E+00 100301\n" +
                            "  0.0000000E+00 0.0000000E+00  182123.1      80886.62     0.0000000E+00 010301\n" +
                            "   33633.91      14821.69     0.0000000E+00 0.0000000E+00  5825.699     001301\n" +
                            "  -3279.829     -932.9794     0.0000000E+00 0.0000000E+00 -600.2369     100202\n" +
                            "  -11051.71     -3650.756     0.0000000E+00 0.0000000E+00 -1737.660     010202\n" +
                            "  0.0000000E+00 0.0000000E+00 -4361.891     -1432.858     0.0000000E+00 001202\n" +
                            "  0.0000000E+00 0.0000000E+00  11.76582      12.65566     0.0000000E+00 100103\n" +
                            "  0.0000000E+00 0.0000000E+00  122.7774      52.48635     0.0000000E+00 010103\n" +
                            "   479.3159      143.2389     0.0000000E+00 0.0000000E+00  123.8027     001103\n" +
                            "   1.399911     0.3201571     0.0000000E+00 0.0000000E+00-0.5018469E-01 100004\n" +
                            "  0.6854801     0.9139037     0.0000000E+00 0.0000000E+00 0.2467850     010004\n" +
                            "  0.0000000E+00 0.0000000E+00  20.10575      4.076410     0.0000000E+00 001004\n" +
                            "  0.0000000E+00 0.0000000E+00  128636.9      54119.28     0.0000000E+00 000500\n" +
                            "   12235.43      7093.299     0.0000000E+00 0.0000000E+00 -2673.322     000401\n" +
                            "  0.0000000E+00 0.0000000E+00 -3832.710     -2037.259     0.0000000E+00 000302\n" +
                            "   606.4578      246.1442     0.0000000E+00 0.0000000E+00  60.74013     000203\n" +
                            "  0.0000000E+00 0.0000000E+00  65.36327      18.96143     0.0000000E+00 000104\n" +
                            " -0.1180419     0.6672139E-03 0.0000000E+00 0.0000000E+00 0.8781243     000005"
            );

            optics_后偏转段CCT2出口加2点4米.map = CosyArbitraryOrder.readMap(
                    "  0.8560639     0.6426617     0.0000000E+00 0.0000000E+00 0.9122470E-02 100000\n" +
                            " -0.4157002     0.8560639     0.0000000E+00 0.0000000E+00-0.2043148E-02 010000\n" +
                            "  0.0000000E+00 0.0000000E+00 -1.013995     0.1209793     0.0000000E+00 001000\n" +
                            "  0.0000000E+00 0.0000000E+00 0.2329814     -1.013995     0.0000000E+00 000100\n" +
                            "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00  1.000000     000010\n" +
                            "  0.2043148E-02-0.9122470E-02 0.0000000E+00 0.0000000E+00  1.274693     000001\n" +
                            "  -5.639219    -0.9384987     0.0000000E+00 0.0000000E+00-0.1807699     200000\n" +
                            "  -15.67769     0.4203887     0.0000000E+00 0.0000000E+00-0.3370804E-01 110000\n" +
                            "   1.755656      9.054778     0.0000000E+00 0.0000000E+00-0.3263133E-01 020000\n" +
                            "  0.0000000E+00 0.0000000E+00 -16.58085     0.7224144E-01 0.0000000E+00 101000\n" +
                            "  0.0000000E+00 0.0000000E+00 -48.47883    -0.1617982E-01 0.0000000E+00 011000\n" +
                            "  -2.919111     -1.062605     0.0000000E+00 0.0000000E+00-0.7957914     002000\n" +
                            "  0.0000000E+00 0.0000000E+00 -3.327242      16.96122     0.0000000E+00 100100\n" +
                            "  0.0000000E+00 0.0000000E+00 0.7451981      48.39364     0.0000000E+00 010100\n" +
                            "   49.06714      17.21543     0.0000000E+00 0.0000000E+00  1.114787     001100\n" +
                            " -0.1308558E-01 0.2878382     0.0000000E+00 0.0000000E+00-0.5877258E-02 100001\n" +
                            " -0.6988145E-01-0.1791484     0.0000000E+00 0.0000000E+00 0.1316322E-02 010001\n" +
                            "  0.0000000E+00 0.0000000E+00-0.2834559     -1.478991     0.0000000E+00 001001\n" +
                            "  -5.269520     -3.618418     0.0000000E+00 0.0000000E+00 -5.843334     000200\n" +
                            "  0.0000000E+00 0.0000000E+00  11.59050    -0.7595797     0.0000000E+00 000101\n" +
                            " -0.3260473E-03 0.3461719E-02 0.0000000E+00 0.0000000E+00 -1.085339     000002\n" +
                            "   16.16456      7.152302     0.0000000E+00 0.0000000E+00  1.770778     300000\n" +
                            "   91.45580      49.65767     0.0000000E+00 0.0000000E+00  7.028247     210000\n" +
                            "   159.4661      119.5897     0.0000000E+00 0.0000000E+00  5.194792     120000\n" +
                            "   87.31837      104.1643     0.0000000E+00 0.0000000E+00 -1.264830     030000\n" +
                            "  0.0000000E+00 0.0000000E+00 -126.0292     -49.36324     0.0000000E+00 201000\n" +
                            "  0.0000000E+00 0.0000000E+00 -770.5523     -283.7283     0.0000000E+00 111000\n" +
                            "  0.0000000E+00 0.0000000E+00 -1173.504     -408.8501     0.0000000E+00 021000\n" +
                            "  -173.7893     -61.78520     0.0000000E+00 0.0000000E+00 -12.46591     102000\n" +
                            "  -505.3297     -179.9722     0.0000000E+00 0.0000000E+00 -37.53233     012000\n" +
                            "  0.0000000E+00 0.0000000E+00  6.733567      7.991398     0.0000000E+00 003000\n" +
                            "  0.0000000E+00 0.0000000E+00 -290.3424     -105.1012     0.0000000E+00 200100\n" +
                            "  0.0000000E+00 0.0000000E+00 -1711.900     -562.3613     0.0000000E+00 110100\n" +
                            "  0.0000000E+00 0.0000000E+00 -2515.282     -746.1342     0.0000000E+00 020100\n" +
                            "  -270.6266     -81.13102     0.0000000E+00 0.0000000E+00 -25.70689     101100\n" +
                            "  -618.9886     -177.8524     0.0000000E+00 0.0000000E+00 -40.69442     011100\n" +
                            "  0.0000000E+00 0.0000000E+00 -109.7295     -8.898953     0.0000000E+00 002100\n" +
                            "   2.003650    -0.3124955     0.0000000E+00 0.0000000E+00 -1.181508     200001\n" +
                            "   13.45981     -1.071271     0.0000000E+00 0.0000000E+00 -6.961490     110001\n" +
                            "  -2.662812     -9.397693     0.0000000E+00 0.0000000E+00 -10.12061     020001\n" +
                            "  0.0000000E+00 0.0000000E+00 -1.328183      1.466717     0.0000000E+00 101001\n" +
                            "  0.0000000E+00 0.0000000E+00  28.32791      3.999954     0.0000000E+00 011001\n" +
                            "   39.23882      13.34967     0.0000000E+00 0.0000000E+00  3.843765     002001\n" +
                            "   1069.290      394.1066     0.0000000E+00 0.0000000E+00  109.4270     100200\n" +
                            "   2779.446      1028.766     0.0000000E+00 0.0000000E+00  294.7823     010200\n" +
                            "  0.0000000E+00 0.0000000E+00  24.94059      118.5879     0.0000000E+00 001200\n" +
                            "  0.0000000E+00 0.0000000E+00 -1.258815      3.141818     0.0000000E+00 100101\n" +
                            "  0.0000000E+00 0.0000000E+00  27.02191     -8.411894     0.0000000E+00 010101\n" +
                            "   12.13186      10.96079     0.0000000E+00 0.0000000E+00 0.5049545     001101\n" +
                            "  -3.602286     -1.257206     0.0000000E+00 0.0000000E+00 0.6506947E-01 100002\n" +
                            "  -10.10104     -3.396481     0.0000000E+00 0.0000000E+00 0.3911568E-01 010002\n" +
                            "  0.0000000E+00 0.0000000E+00  7.632859      2.479513     0.0000000E+00 001002\n" +
                            "  0.0000000E+00 0.0000000E+00 -308.1594     -37.71632     0.0000000E+00 000300\n" +
                            "  -291.3815     -107.5448     0.0000000E+00 0.0000000E+00 -12.90238     000201\n" +
                            "  0.0000000E+00 0.0000000E+00  8.098020      7.949245     0.0000000E+00 000102\n" +
                            "  0.5320448E-01 0.7711552E-03 0.0000000E+00 0.0000000E+00 0.9119250     000003\n" +
                            "  -36.41269     -5.141206     0.0000000E+00 0.0000000E+00 -3.276358     400000\n" +
                            "  -240.0839      12.33565     0.0000000E+00 0.0000000E+00  1.069799     310000\n" +
                            "  -420.6474      315.1893     0.0000000E+00 0.0000000E+00  107.6025     220000\n" +
                            "   64.61248      1009.924     0.0000000E+00 0.0000000E+00  297.9029     130000\n" +
                            "   460.3569      949.0020     0.0000000E+00 0.0000000E+00  217.8983     040000\n" +
                            "  0.0000000E+00 0.0000000E+00 -161.2827     -89.00278     0.0000000E+00 301000\n" +
                            "  0.0000000E+00 0.0000000E+00 -1796.676     -941.0648     0.0000000E+00 211000\n" +
                            "  0.0000000E+00 0.0000000E+00 -6354.001     -3206.121     0.0000000E+00 121000\n" +
                            "  0.0000000E+00 0.0000000E+00 -7260.207     -3550.247     0.0000000E+00 031000\n" +
                            "  -664.6231     -269.1820     0.0000000E+00 0.0000000E+00 -22.53895     202000\n" +
                            "  -4462.360     -1779.914     0.0000000E+00 0.0000000E+00 -245.0190     112000\n" +
                            "  -7319.792     -2883.765     0.0000000E+00 0.0000000E+00 -517.7190     022000\n" +
                            "  0.0000000E+00 0.0000000E+00  336.2990      152.4807     0.0000000E+00 103000\n" +
                            "  0.0000000E+00 0.0000000E+00  947.5792      436.9210     0.0000000E+00 013000\n" +
                            "  -144.6195     -51.60734     0.0000000E+00 0.0000000E+00 -14.54474     004000\n" +
                            "  0.0000000E+00 0.0000000E+00 -620.3584     -275.0352     0.0000000E+00 300100\n" +
                            "  0.0000000E+00 0.0000000E+00 -6170.616     -2711.648     0.0000000E+00 210100\n" +
                            "  0.0000000E+00 0.0000000E+00 -20241.19     -8792.925     0.0000000E+00 120100\n" +
                            "  0.0000000E+00 0.0000000E+00 -21928.52     -9381.292     0.0000000E+00 030100\n" +
                            "  -6315.386     -2290.130     0.0000000E+00 0.0000000E+00 -428.5574     201100\n" +
                            "  -36339.90     -13143.96     0.0000000E+00 0.0000000E+00 -2755.233     111100\n" +
                            "  -52190.18     -18843.52     0.0000000E+00 0.0000000E+00 -4297.324     021100\n" +
                            "  0.0000000E+00 0.0000000E+00 -1001.485     -446.2564     0.0000000E+00 102100\n" +
                            "  0.0000000E+00 0.0000000E+00 -3235.356     -1402.148     0.0000000E+00 012100\n" +
                            "  -504.8874     -167.2618     0.0000000E+00 0.0000000E+00  1.149484     003100\n" +
                            "   33.40340      13.64079     0.0000000E+00 0.0000000E+00 -3.943900     300001\n" +
                            "   278.1239      94.46927     0.0000000E+00 0.0000000E+00 -35.03786     210001\n" +
                            "   854.6251      226.4735     0.0000000E+00 0.0000000E+00 -93.61814     120001\n" +
                            "   892.7184      175.4815     0.0000000E+00 0.0000000E+00 -84.54098     030001\n" +
                            "  0.0000000E+00 0.0000000E+00  74.03675      11.62783     0.0000000E+00 201001\n" +
                            "  0.0000000E+00 0.0000000E+00  814.4964      209.7734     0.0000000E+00 111001\n" +
                            "  0.0000000E+00 0.0000000E+00  1813.577      514.6545     0.0000000E+00 021001\n" +
                            "   421.1781      128.1295     0.0000000E+00 0.0000000E+00  32.52388     102001\n" +
                            "   1589.861      502.9321     0.0000000E+00 0.0000000E+00  139.5210     012001\n" +
                            "  0.0000000E+00 0.0000000E+00 -131.0812     -53.50182     0.0000000E+00 003001\n" +
                            "  -11736.83     -4024.352     0.0000000E+00 0.0000000E+00 -1331.095     200200\n" +
                            "  -57843.99     -19785.78     0.0000000E+00 0.0000000E+00 -6405.461     110200\n" +
                            "  -70128.97     -23913.15     0.0000000E+00 0.0000000E+00 -7601.883     020200\n" +
                            "  0.0000000E+00 0.0000000E+00 -3718.446     -1687.788     0.0000000E+00 101200\n" +
                            "  0.0000000E+00 0.0000000E+00 -10109.04     -4497.940     0.0000000E+00 011200\n" +
                            "  -1386.773     -458.4595     0.0000000E+00 0.0000000E+00 -360.1933     002200\n" +
                            "  0.0000000E+00 0.0000000E+00  314.9102      81.57972     0.0000000E+00 200101\n" +
                            "  0.0000000E+00 0.0000000E+00  2612.832      714.1154     0.0000000E+00 110101\n" +
                            "  0.0000000E+00 0.0000000E+00  4977.412      1318.235     0.0000000E+00 020101\n" +
                            "   3408.924      1171.044     0.0000000E+00 0.0000000E+00  288.3788     101101\n" +
                            "   9674.152      3338.694     0.0000000E+00 0.0000000E+00  821.1018     011101\n" +
                            "  0.0000000E+00 0.0000000E+00  44.88871     -19.24578     0.0000000E+00 002101\n" +
                            "   4.712436      1.827216     0.0000000E+00 0.0000000E+00  2.710937     200002\n" +
                            "   15.60770      8.109118     0.0000000E+00 0.0000000E+00  17.43379     110002\n" +
                            "   30.33584      15.35664     0.0000000E+00 0.0000000E+00  28.36872     020002\n" +
                            "  0.0000000E+00 0.0000000E+00 0.4875794     -3.346234     0.0000000E+00 101002\n" +
                            "  0.0000000E+00 0.0000000E+00 -11.02085     -6.022921     0.0000000E+00 011002\n" +
                            "  -79.67103     -21.70561     0.0000000E+00 0.0000000E+00 -10.47816     002002\n" +
                            "  0.0000000E+00 0.0000000E+00  5937.135      2541.632     0.0000000E+00 100300\n" +
                            "  0.0000000E+00 0.0000000E+00  15369.89      6631.442     0.0000000E+00 010300\n" +
                            "   4783.491      1782.203     0.0000000E+00 0.0000000E+00  579.7876     001300\n" +
                            "   4591.474      1769.837     0.0000000E+00 0.0000000E+00  301.7717     100201\n" +
                            "   10274.00      3980.427     0.0000000E+00 0.0000000E+00  505.5914     010201\n" +
                            "  0.0000000E+00 0.0000000E+00  141.2155     -2.960204     0.0000000E+00 001201\n" +
                            "  0.0000000E+00 0.0000000E+00  1.000612     -13.07404     0.0000000E+00 100102\n" +
                            "  0.0000000E+00 0.0000000E+00 -38.54930     -25.63350     0.0000000E+00 010102\n" +
                            "  -445.3324     -156.6929     0.0000000E+00 0.0000000E+00 -30.21381     001102\n" +
                            "   4.691831     0.9201753     0.0000000E+00 0.0000000E+00-0.1700657     100003\n" +
                            "   18.01681      4.287405     0.0000000E+00 0.0000000E+00-0.1590967     010003\n" +
                            "  0.0000000E+00 0.0000000E+00 -9.852822     -1.435093     0.0000000E+00 001003\n" +
                            "   4515.257      1678.475     0.0000000E+00 0.0000000E+00  235.5919     000400\n" +
                            "  0.0000000E+00 0.0000000E+00 -1566.210     -919.2420     0.0000000E+00 000301\n" +
                            "  -138.1382     -95.61125     0.0000000E+00 0.0000000E+00 0.9847976     000202\n" +
                            "  0.0000000E+00 0.0000000E+00 -30.46606     -10.65300     0.0000000E+00 000103\n" +
                            " -0.1102275     0.9122754E-03 0.0000000E+00 0.0000000E+00-0.7743762     000004\n" +
                            "   71.82599      43.32422     0.0000000E+00 0.0000000E+00  40.28549     500000\n" +
                            "   488.9632      519.1536     0.0000000E+00 0.0000000E+00  438.0605     410000\n" +
                            "   601.2691      2657.028     0.0000000E+00 0.0000000E+00  2013.158     320000\n" +
                            "  -2279.503      7452.509     0.0000000E+00 0.0000000E+00  5037.216     230000\n" +
                            "  -6121.946      11492.56     0.0000000E+00 0.0000000E+00  6902.973     140000\n" +
                            "  -3709.672      7639.827     0.0000000E+00 0.0000000E+00  4040.029     050000\n" +
                            "  0.0000000E+00 0.0000000E+00  447.3288      52.31768     0.0000000E+00 401000\n" +
                            "  0.0000000E+00 0.0000000E+00  3931.153      50.22695     0.0000000E+00 311000\n" +
                            "  0.0000000E+00 0.0000000E+00  11721.79     -2209.912     0.0000000E+00 221000\n" +
                            "  0.0000000E+00 0.0000000E+00  12155.65     -9082.975     0.0000000E+00 131000\n" +
                            "  0.0000000E+00 0.0000000E+00  833.2368     -10236.52     0.0000000E+00 041000\n" +
                            "   3522.033      1075.329     0.0000000E+00 0.0000000E+00  191.7570     302000\n" +
                            "   26657.97      7792.566     0.0000000E+00 0.0000000E+00  1645.683     212000\n" +
                            "   65602.62      18080.12     0.0000000E+00 0.0000000E+00  4425.015     122000\n" +
                            "   51796.63      13063.34     0.0000000E+00 0.0000000E+00  3664.599     032000\n" +
                            "  0.0000000E+00 0.0000000E+00  4566.077      2066.746     0.0000000E+00 203000\n" +
                            "  0.0000000E+00 0.0000000E+00  27107.26      12288.51     0.0000000E+00 113000\n" +
                            "  0.0000000E+00 0.0000000E+00  40102.63      18198.93     0.0000000E+00 023000\n" +
                            "  -2314.057     -880.5830     0.0000000E+00 0.0000000E+00 -314.8972     104000\n" +
                            "  -6974.421     -2644.850     0.0000000E+00 0.0000000E+00 -992.7145     014000\n" +
                            "  0.0000000E+00 0.0000000E+00  375.1008      166.7971     0.0000000E+00 005000\n" +
                            "  0.0000000E+00 0.0000000E+00  555.3282     -45.30959     0.0000000E+00 400100\n" +
                            "  0.0000000E+00 0.0000000E+00  2830.684     -1949.615     0.0000000E+00 310100\n" +
                            "  0.0000000E+00 0.0000000E+00 -3417.094     -14706.56     0.0000000E+00 220100\n" +
                            "  0.0000000E+00 0.0000000E+00 -37710.27     -40756.77     0.0000000E+00 130100\n" +
                            "  0.0000000E+00 0.0000000E+00 -50910.92     -38802.34     0.0000000E+00 040100\n" +
                            "   14548.90      4119.674     0.0000000E+00 0.0000000E+00  2593.007     301100\n" +
                            "   81445.83      19785.50     0.0000000E+00 0.0000000E+00  17559.09     211100\n" +
                            "   110634.3      13193.57     0.0000000E+00 0.0000000E+00  36188.50     121100\n" +
                            "  -9610.160     -27871.57     0.0000000E+00 0.0000000E+00  20505.57     031100\n" +
                            "  0.0000000E+00 0.0000000E+00  16286.09      7478.098     0.0000000E+00 202100\n" +
                            "  0.0000000E+00 0.0000000E+00  86914.73      39893.66     0.0000000E+00 112100\n" +
                            "  0.0000000E+00 0.0000000E+00  115382.6      52927.86     0.0000000E+00 022100\n" +
                            "  -9788.161     -3516.772     0.0000000E+00 0.0000000E+00  755.1295     103100\n" +
                            "  -27009.57     -9799.116     0.0000000E+00 0.0000000E+00  2158.698     013100\n" +
                            "  0.0000000E+00 0.0000000E+00  678.1534      341.7025     0.0000000E+00 004100\n" +
                            "   9.109785    -0.4278971     0.0000000E+00 0.0000000E+00 -16.10315     400001\n" +
                            "   343.6918      48.04626     0.0000000E+00 0.0000000E+00 -221.5556     310001\n" +
                            "   2019.402      189.4998     0.0000000E+00 0.0000000E+00 -1144.947     220001\n" +
                            "   4480.256      18.10270     0.0000000E+00 0.0000000E+00 -2475.217     130001\n" +
                            "   3725.257     -343.0320     0.0000000E+00 0.0000000E+00 -1853.932     040001\n" +
                            "  0.0000000E+00 0.0000000E+00  67.17055      28.98270     0.0000000E+00 301001\n" +
                            "  0.0000000E+00 0.0000000E+00  2041.502      889.1035     0.0000000E+00 211001\n" +
                            "  0.0000000E+00 0.0000000E+00  11322.56      4919.217     0.0000000E+00 121001\n" +
                            "  0.0000000E+00 0.0000000E+00  17283.96      7465.877     0.0000000E+00 031001\n" +
                            "  -1977.060     -727.9282     0.0000000E+00 0.0000000E+00 -233.5265     202001\n" +
                            "  -6601.120     -2479.123     0.0000000E+00 0.0000000E+00 -951.4137     112001\n" +
                            "  -1670.706     -782.2825     0.0000000E+00 0.0000000E+00 -589.5354     022001\n" +
                            "  0.0000000E+00 0.0000000E+00 -2611.856     -1055.112     0.0000000E+00 103001\n" +
                            "  0.0000000E+00 0.0000000E+00 -8139.112     -3339.352     0.0000000E+00 013001\n" +
                            "   710.4590      235.7421     0.0000000E+00 0.0000000E+00  119.3089     004001\n" +
                            "   17704.33      4643.392     0.0000000E+00 0.0000000E+00  5903.826     300200\n" +
                            "   52186.15      6429.200     0.0000000E+00 0.0000000E+00  32738.87     210200\n" +
                            "  -108235.1     -67960.72     0.0000000E+00 0.0000000E+00  49239.31     120200\n" +
                            "  -321499.4     -137954.4     0.0000000E+00 0.0000000E+00  10909.88     030200\n" +
                            "  0.0000000E+00 0.0000000E+00 -31378.42     -13578.81     0.0000000E+00 201200\n" +
                            "  0.0000000E+00 0.0000000E+00 -191376.4     -82966.55     0.0000000E+00 111200\n" +
                            "  0.0000000E+00 0.0000000E+00 -289675.7     -125769.5     0.0000000E+00 021200\n" +
                            "  -61353.86     -21975.98     0.0000000E+00 0.0000000E+00 -1778.535     102200\n" +
                            "  -175505.7     -62673.38     0.0000000E+00 0.0000000E+00 -7586.939     012200\n" +
                            "  0.0000000E+00 0.0000000E+00  823.0558      407.8439     0.0000000E+00 003200\n" +
                            "  0.0000000E+00 0.0000000E+00  489.7238      203.9615     0.0000000E+00 300101\n" +
                            "  0.0000000E+00 0.0000000E+00  8997.842      3663.105     0.0000000E+00 210101\n" +
                            "  0.0000000E+00 0.0000000E+00  42270.86      17046.26     0.0000000E+00 120101\n" +
                            "  0.0000000E+00 0.0000000E+00  58984.46      23560.07     0.0000000E+00 030101\n" +
                            "  -5386.939     -2447.638     0.0000000E+00 0.0000000E+00 -838.6173     201101\n" +
                            "   6701.519     -641.6613     0.0000000E+00 0.0000000E+00 -304.5775     111101\n" +
                            "   62635.48      17898.49     0.0000000E+00 0.0000000E+00  6076.146     021101\n" +
                            "  0.0000000E+00 0.0000000E+00 -7334.352     -3541.639     0.0000000E+00 102101\n" +
                            "  0.0000000E+00 0.0000000E+00 -17288.58     -8729.309     0.0000000E+00 012101\n" +
                            "   3651.373      1203.745     0.0000000E+00 0.0000000E+00 -147.6211     003101\n" +
                            "  -40.07315     -8.445100     0.0000000E+00 0.0000000E+00 0.7917710     300002\n" +
                            "  -483.6455     -107.8521     0.0000000E+00 0.0000000E+00  44.45839     210002\n" +
                            "  -1872.436     -392.8324     0.0000000E+00 0.0000000E+00  226.2596     120002\n" +
                            "  -2295.102     -429.1439     0.0000000E+00 0.0000000E+00  306.9168     030002\n" +
                            "  0.0000000E+00 0.0000000E+00 -83.23378     -13.69250     0.0000000E+00 201002\n" +
                            "  0.0000000E+00 0.0000000E+00 -950.2565     -214.1229     0.0000000E+00 111002\n" +
                            "  0.0000000E+00 0.0000000E+00 -2449.292     -628.5572     0.0000000E+00 021002\n" +
                            "   120.5435      103.0616     0.0000000E+00 0.0000000E+00 -8.854317     102002\n" +
                            "  -940.9818     -106.0903     0.0000000E+00 0.0000000E+00 -174.4523     012002\n" +
                            "  0.0000000E+00 0.0000000E+00  475.6157      165.9634     0.0000000E+00 003002\n" +
                            "  0.0000000E+00 0.0000000E+00 -101010.9     -43846.10     0.0000000E+00 200300\n" +
                            "  0.0000000E+00 0.0000000E+00 -522665.4     -226718.0     0.0000000E+00 110300\n" +
                            "  0.0000000E+00 0.0000000E+00 -672358.2     -291243.8     0.0000000E+00 020300\n" +
                            "  -193879.2     -68306.67     0.0000000E+00 0.0000000E+00 -29974.11     101300\n" +
                            "  -509986.3     -179928.3     0.0000000E+00 0.0000000E+00 -77550.07     011300\n" +
                            "  0.0000000E+00 0.0000000E+00 -10679.58     -4092.945     0.0000000E+00 002300\n" +
                            "   2806.670     -442.0413     0.0000000E+00 0.0000000E+00  123.4596     200201\n" +
                            "   66926.99      16620.38     0.0000000E+00 0.0000000E+00  7921.702     110201\n" +
                            "   153527.0      45872.52     0.0000000E+00 0.0000000E+00  19279.43     020201\n" +
                            "  0.0000000E+00 0.0000000E+00  21175.54      8164.989     0.0000000E+00 101201\n" +
                            "  0.0000000E+00 0.0000000E+00  65983.18      25736.39     0.0000000E+00 011201\n" +
                            "   18597.83      6208.505     0.0000000E+00 0.0000000E+00  1505.757     002201\n" +
                            "  0.0000000E+00 0.0000000E+00 -375.8611     -86.99716     0.0000000E+00 200102\n" +
                            "  0.0000000E+00 0.0000000E+00 -3457.551     -873.2438     0.0000000E+00 110102\n" +
                            "  0.0000000E+00 0.0000000E+00 -7479.947     -1902.447     0.0000000E+00 020102\n" +
                            "  -2448.135     -344.7222     0.0000000E+00 0.0000000E+00 -324.4735     101102\n" +
                            "  -13234.98     -3242.857     0.0000000E+00 0.0000000E+00 -1614.410     011102\n" +
                            "  0.0000000E+00 0.0000000E+00  1272.423      568.3360     0.0000000E+00 002102\n" +
                            "  -10.99955     -2.526971     0.0000000E+00 0.0000000E+00 -3.458818     200003\n" +
                            "  -57.18196     -14.60326     0.0000000E+00 0.0000000E+00 -26.60908     110003\n" +
                            "  -80.44288     -23.18605     0.0000000E+00 0.0000000E+00 -50.56815     020003\n" +
                            "  0.0000000E+00 0.0000000E+00  15.25455      7.281695     0.0000000E+00 101003\n" +
                            "  0.0000000E+00 0.0000000E+00  27.18193      13.65670     0.0000000E+00 011003\n" +
                            "   55.21238      5.297607     0.0000000E+00 0.0000000E+00  19.16616     002003\n" +
                            "  -57156.40     -20003.51     0.0000000E+00 0.0000000E+00  2192.332     100400\n" +
                            "  -145176.2     -50667.76     0.0000000E+00 0.0000000E+00  7954.607     010400\n" +
                            "  0.0000000E+00 0.0000000E+00  22357.45      10087.31     0.0000000E+00 001400\n" +
                            "  0.0000000E+00 0.0000000E+00  39674.73      19492.13     0.0000000E+00 100301\n" +
                            "  0.0000000E+00 0.0000000E+00  98175.81      48303.36     0.0000000E+00 010301\n" +
                            "   36392.36      13730.97     0.0000000E+00 0.0000000E+00  4902.104     001301\n" +
                            "  -8317.495     -2191.539     0.0000000E+00 0.0000000E+00 -825.5049     100202\n" +
                            "  -27780.65     -8147.170     0.0000000E+00 0.0000000E+00 -2567.074     010202\n" +
                            "  0.0000000E+00 0.0000000E+00 -1920.034     -785.3102     0.0000000E+00 001202\n" +
                            "  0.0000000E+00 0.0000000E+00  48.09505      31.78482     0.0000000E+00 100103\n" +
                            "  0.0000000E+00 0.0000000E+00  146.2021      80.66497     0.0000000E+00 010103\n" +
                            "   747.0664      187.8787     0.0000000E+00 0.0000000E+00  84.24979     001103\n" +
                            "  -4.715054    -0.5831851     0.0000000E+00 0.0000000E+00 0.2976229     100004\n" +
                            "  -22.08529     -4.009389     0.0000000E+00 0.0000000E+00 0.2556717     010004\n" +
                            "  0.0000000E+00 0.0000000E+00  7.845325    -0.2450467     0.0000000E+00 001004\n" +
                            "  0.0000000E+00 0.0000000E+00  28351.40      13275.94     0.0000000E+00 000500\n" +
                            "  -3464.046     -554.2254     0.0000000E+00 0.0000000E+00 -2216.809     000401\n" +
                            "  0.0000000E+00 0.0000000E+00 -920.3368     -784.7085     0.0000000E+00 000302\n" +
                            "   995.6177      331.9975     0.0000000E+00 0.0000000E+00  47.25857     000203\n" +
                            "  0.0000000E+00 0.0000000E+00  44.85353      9.220976     0.0000000E+00 000104\n" +
                            "  0.2344651     0.1385446E-01 0.0000000E+00 0.0000000E+00 0.6519569     000005"
            );

            optics_后偏转段CCT2出口加2点0米.map = CosyArbitraryOrder.readMap(
                    "  0.5989992     0.6426617     0.0000000E+00 0.0000000E+00 0.9122470E-02 100000\n" +
                            " -0.7581258     0.8560639     0.0000000E+00 0.0000000E+00-0.2043148E-02 010000\n" +
                            "  0.0000000E+00 0.0000000E+00 -1.062387     0.1209793     0.0000000E+00 001000\n" +
                            "  0.0000000E+00 0.0000000E+00 0.6385794     -1.013995     0.0000000E+00 000100\n" +
                            "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00  1.000000     000010\n" +
                            "  0.5692136E-02-0.9122470E-02 0.0000000E+00 0.0000000E+00  1.196823     000001\n" +
                            "  -5.263820    -0.9384987     0.0000000E+00 0.0000000E+00-0.1346130     200000\n" +
                            "  -15.84585     0.4203887     0.0000000E+00 0.0000000E+00 0.8925939E-01 110000\n" +
                            "  -1.866255      9.054778     0.0000000E+00 0.0000000E+00 0.4926865E-01 020000\n" +
                            "  0.0000000E+00 0.0000000E+00 -16.60975     0.7224144E-01 0.0000000E+00 101000\n" +
                            "  0.0000000E+00 0.0000000E+00 -48.47236    -0.1617982E-01 0.0000000E+00 011000\n" +
                            "  -2.494069     -1.062605     0.0000000E+00 0.0000000E+00-0.7941557     002000\n" +
                            "  0.0000000E+00 0.0000000E+00 -10.11173      16.96122     0.0000000E+00 100100\n" +
                            "  0.0000000E+00 0.0000000E+00 -18.61226      48.39364     0.0000000E+00 010100\n" +
                            "   42.18097      17.21543     0.0000000E+00 0.0000000E+00  1.087369     001100\n" +
                            "  0.1542194E-01 0.2878382     0.0000000E+00 0.0000000E+00-0.7187636E-02 100001\n" +
                            "  0.1931187    -0.1791484     0.0000000E+00 0.0000000E+00-0.4291792E-03 010001\n" +
                            "  0.0000000E+00 0.0000000E+00 0.3351808     -1.478991     0.0000000E+00 001001\n" +
                            "  -3.822153     -3.618418     0.0000000E+00 0.0000000E+00 -5.728428     000200\n" +
                            "  0.0000000E+00 0.0000000E+00  11.66769    -0.7595797     0.0000000E+00 000101\n" +
                            " -0.3749720E-02 0.3461719E-02 0.0000000E+00 0.0000000E+00 -1.020061     000002\n" +
                            "   13.25055      7.152302     0.0000000E+00 0.0000000E+00  1.635970     300000\n" +
                            "   71.38059      49.65767     0.0000000E+00 0.0000000E+00  6.909059     210000\n" +
                            "   111.3477      119.5897     0.0000000E+00 0.0000000E+00  6.575883     120000\n" +
                            "   45.52716      104.1643     0.0000000E+00 0.0000000E+00 0.4677188     030000\n" +
                            "  0.0000000E+00 0.0000000E+00 -106.2939     -49.36324     0.0000000E+00 201000\n" +
                            "  0.0000000E+00 0.0000000E+00 -657.0877     -283.7283     0.0000000E+00 111000\n" +
                            "  0.0000000E+00 0.0000000E+00 -1009.982     -408.8501     0.0000000E+00 021000\n" +
                            "  -149.0771     -61.78520     0.0000000E+00 0.0000000E+00 -12.61659     102000\n" +
                            "  -433.3433     -179.9722     0.0000000E+00 0.0000000E+00 -37.73609     012000\n" +
                            "  0.0000000E+00 0.0000000E+00  3.536653      7.991398     0.0000000E+00 003000\n" +
                            "  0.0000000E+00 0.0000000E+00 -248.2182     -105.1012     0.0000000E+00 200100\n" +
                            "  0.0000000E+00 0.0000000E+00 -1486.732     -562.3613     0.0000000E+00 110100\n" +
                            "  0.0000000E+00 0.0000000E+00 -2216.679     -746.1342     0.0000000E+00 020100\n" +
                            "  -238.1427     -81.13102     0.0000000E+00 0.0000000E+00 -22.79175     101100\n" +
                            "  -547.8056     -177.8524     0.0000000E+00 0.0000000E+00 -36.08816     011100\n" +
                            "  0.0000000E+00 0.0000000E+00 -106.1610     -8.898953     0.0000000E+00 002100\n" +
                            "   1.921143    -0.3124955     0.0000000E+00 0.0000000E+00 -1.205913     200001\n" +
                            "   13.98830     -1.071271     0.0000000E+00 0.0000000E+00 -7.113270     110001\n" +
                            "   3.124130     -9.397693     0.0000000E+00 0.0000000E+00 -10.29341     020001\n" +
                            "  0.0000000E+00 0.0000000E+00 -1.898439      1.466717     0.0000000E+00 101001\n" +
                            "  0.0000000E+00 0.0000000E+00  26.72469      3.999954     0.0000000E+00 011001\n" +
                            "   33.66147      13.34967     0.0000000E+00 0.0000000E+00  3.803541     002001\n" +
                            "   911.5156      394.1066     0.0000000E+00 0.0000000E+00  105.0631     100200\n" +
                            "   2367.764      1028.766     0.0000000E+00 0.0000000E+00  283.1220     010200\n" +
                            "  0.0000000E+00 0.0000000E+00 -22.56919      118.5879     0.0000000E+00 001200\n" +
                            "  0.0000000E+00 0.0000000E+00  1.273122      3.141818     0.0000000E+00 100101\n" +
                            "  0.0000000E+00 0.0000000E+00  41.20007     -8.411894     0.0000000E+00 010101\n" +
                            "   11.59496      10.96079     0.0000000E+00 0.0000000E+00 0.8247069     001101\n" +
                            "  -3.140387     -1.257206     0.0000000E+00 0.0000000E+00 0.6690078E-01 100002\n" +
                            "  -8.922784     -3.396481     0.0000000E+00 0.0000000E+00 0.4270215E-01 010002\n" +
                            "  0.0000000E+00 0.0000000E+00  6.290659      2.479513     0.0000000E+00 001002\n" +
                            "  0.0000000E+00 0.0000000E+00 -292.8644     -37.71632     0.0000000E+00 000300\n" +
                            "  -249.1704     -107.5448     0.0000000E+00 0.0000000E+00 -12.89130     000201\n" +
                            "  0.0000000E+00 0.0000000E+00  4.914686      7.949245     0.0000000E+00 000102\n" +
                            "  0.5516444E-01 0.7711552E-03 0.0000000E+00 0.0000000E+00 0.8556972     000003\n" +
                            "  -34.12364     -5.141206     0.0000000E+00 0.0000000E+00 -2.136251     400000\n" +
                            "  -244.5028      12.33565     0.0000000E+00 0.0000000E+00  9.559292     310000\n" +
                            "  -548.8318      315.1893     0.0000000E+00 0.0000000E+00  132.5549     220000\n" +
                            "  -345.5198      1009.924     0.0000000E+00 0.0000000E+00  336.7337     130000\n" +
                            "   76.77469      949.0020     0.0000000E+00 0.0000000E+00  247.0369     040000\n" +
                            "  0.0000000E+00 0.0000000E+00 -125.6584     -89.00278     0.0000000E+00 301000\n" +
                            "  0.0000000E+00 0.0000000E+00 -1420.239     -941.0648     0.0000000E+00 211000\n" +
                            "  0.0000000E+00 0.0000000E+00 -5071.858     -3206.121     0.0000000E+00 121000\n" +
                            "  0.0000000E+00 0.0000000E+00 -5840.481     -3550.247     0.0000000E+00 031000\n" +
                            "  -556.6865     -269.1820     0.0000000E+00 0.0000000E+00 -32.52425     202000\n" +
                            "  -3749.697     -1779.914     0.0000000E+00 0.0000000E+00 -290.4623     112000\n" +
                            "  -6165.844     -2883.765     0.0000000E+00 0.0000000E+00 -565.3592     022000\n" +
                            "  0.0000000E+00 0.0000000E+00  275.3391      152.4807     0.0000000E+00 103000\n" +
                            "  0.0000000E+00 0.0000000E+00  772.8549      436.9210     0.0000000E+00 013000\n" +
                            "  -123.9735     -51.60734     0.0000000E+00 0.0000000E+00 -14.20244     004000\n" +
                            "  0.0000000E+00 0.0000000E+00 -511.9899     -275.0352     0.0000000E+00 300100\n" +
                            "  0.0000000E+00 0.0000000E+00 -5093.903     -2711.648     0.0000000E+00 210100\n" +
                            "  0.0000000E+00 0.0000000E+00 -16734.65     -8792.925     0.0000000E+00 120100\n" +
                            "  0.0000000E+00 0.0000000E+00 -18179.95     -9381.292     0.0000000E+00 030100\n" +
                            "  -5404.155     -2290.130     0.0000000E+00 0.0000000E+00 -435.2198     201100\n" +
                            "  -31095.85     -13143.96     0.0000000E+00 0.0000000E+00 -2744.914     111100\n" +
                            "  -44661.91     -18843.52     0.0000000E+00 0.0000000E+00 -4224.232     021100\n" +
                            "  0.0000000E+00 0.0000000E+00 -823.9329     -446.2564     0.0000000E+00 102100\n" +
                            "  0.0000000E+00 0.0000000E+00 -2676.006     -1402.148     0.0000000E+00 012100\n" +
                            "  -438.0852     -167.2618     0.0000000E+00 0.0000000E+00 -4.991676     003100\n" +
                            "   29.55677      13.64079     0.0000000E+00 0.0000000E+00 -3.866939     300001\n" +
                            "   251.6395      94.46927     0.0000000E+00 0.0000000E+00 -35.11668     210001\n" +
                            "   791.2985      226.4735     0.0000000E+00 0.0000000E+00 -96.88010     120001\n" +
                            "   846.1818      175.4815     0.0000000E+00 0.0000000E+00 -89.45586     030001\n" +
                            "  0.0000000E+00 0.0000000E+00  58.48205      11.62783     0.0000000E+00 201001\n" +
                            "  0.0000000E+00 0.0000000E+00  667.5343      209.7734     0.0000000E+00 111001\n" +
                            "  0.0000000E+00 0.0000000E+00  1516.590      514.6545     0.0000000E+00 021001\n" +
                            "   356.1574      128.1295     0.0000000E+00 0.0000000E+00  34.73573     102001\n" +
                            "   1348.518      502.9321     0.0000000E+00 0.0000000E+00  142.8970     012001\n" +
                            "  0.0000000E+00 0.0000000E+00 -107.8811     -53.50182     0.0000000E+00 003001\n" +
                            "  -10121.57     -4024.352     0.0000000E+00 0.0000000E+00 -1217.684     200200\n" +
                            "  -49908.88     -19785.78     0.0000000E+00 0.0000000E+00 -5871.512     110200\n" +
                            "  -60547.18     -23913.15     0.0000000E+00 0.0000000E+00 -6981.405     020200\n" +
                            "  0.0000000E+00 0.0000000E+00 -3036.279     -1687.788     0.0000000E+00 101200\n" +
                            "  0.0000000E+00 0.0000000E+00 -8296.607     -4497.940     0.0000000E+00 011200\n" +
                            "  -1202.315     -458.4595     0.0000000E+00 0.0000000E+00 -320.9815     002200\n" +
                            "  0.0000000E+00 0.0000000E+00  258.8276      81.57972     0.0000000E+00 200101\n" +
                            "  0.0000000E+00 0.0000000E+00  2201.502      714.1154     0.0000000E+00 110101\n" +
                            "  0.0000000E+00 0.0000000E+00  4283.265      1318.235     0.0000000E+00 020101\n" +
                            "   2922.100      1171.044     0.0000000E+00 0.0000000E+00  281.0867     101101\n" +
                            "   8298.544      3338.694     0.0000000E+00 0.0000000E+00  798.9916     011101\n" +
                            "  0.0000000E+00 0.0000000E+00  50.38355     -19.24578     0.0000000E+00 002101\n" +
                            "   4.062881      1.827216     0.0000000E+00 0.0000000E+00  2.559790     200002\n" +
                            "   12.04477      8.109118     0.0000000E+00 0.0000000E+00  16.87754     110002\n" +
                            "   20.59887      15.35664     0.0000000E+00 0.0000000E+00  27.97440     020002\n" +
                            "  0.0000000E+00 0.0000000E+00  2.138142     -3.346234     0.0000000E+00 101002\n" +
                            "  0.0000000E+00 0.0000000E+00 -7.720470     -6.022921     0.0000000E+00 011002\n" +
                            "  -67.83154     -21.70561     0.0000000E+00 0.0000000E+00 -10.13629     002002\n" +
                            "  0.0000000E+00 0.0000000E+00  4909.076      2541.632     0.0000000E+00 100300\n" +
                            "  0.0000000E+00 0.0000000E+00  12686.21      6631.442     0.0000000E+00 010300\n" +
                            "   4066.892      1782.203     0.0000000E+00 0.0000000E+00  537.9255     001300\n" +
                            "   3971.503      1769.837     0.0000000E+00 0.0000000E+00  288.0908     100201\n" +
                            "   8911.626      3980.427     0.0000000E+00 0.0000000E+00  493.8421     010201\n" +
                            "  0.0000000E+00 0.0000000E+00  169.7659     -2.960204     0.0000000E+00 001201\n" +
                            "  0.0000000E+00 0.0000000E+00  4.155479     -13.07404     0.0000000E+00 100102\n" +
                            "  0.0000000E+00 0.0000000E+00 -38.10036     -25.63350     0.0000000E+00 010102\n" +
                            "  -383.0206     -156.6929     0.0000000E+00 0.0000000E+00 -30.77827     001102\n" +
                            "   4.082463     0.9201753     0.0000000E+00 0.0000000E+00-0.1694129     100003\n" +
                            "   15.68778      4.287405     0.0000000E+00 0.0000000E+00-0.1568333     010003\n" +
                            "  0.0000000E+00 0.0000000E+00 -8.465912     -1.435093     0.0000000E+00 001003\n" +
                            "   3844.611      1678.475     0.0000000E+00 0.0000000E+00  245.6918     000400\n" +
                            "  0.0000000E+00 0.0000000E+00 -1206.811     -919.2420     0.0000000E+00 000301\n" +
                            "  -123.3394     -95.61125     0.0000000E+00 0.0000000E+00-0.5907330     000202\n" +
                            "  0.0000000E+00 0.0000000E+00 -24.44064     -10.65300     0.0000000E+00 000103\n" +
                            " -0.1122197     0.9122754E-03 0.0000000E+00 0.0000000E+00-0.7245015     000004\n" +
                            "   52.36783      43.32422     0.0000000E+00 0.0000000E+00  37.96316     500000\n" +
                            "   264.0166      519.1536     0.0000000E+00 0.0000000E+00  428.8079     410000\n" +
                            "  -520.5069      2657.028     0.0000000E+00 0.0000000E+00  2055.359     320000\n" +
                            "  -5381.792      7452.509     0.0000000E+00 0.0000000E+00  5335.702     230000\n" +
                            "  -10876.11      11492.56     0.0000000E+00 0.0000000E+00  7488.728     140000\n" +
                            "  -6853.586      7639.827     0.0000000E+00 0.0000000E+00  4434.329     050000\n" +
                            "  0.0000000E+00 0.0000000E+00  430.2498      52.31768     0.0000000E+00 401000\n" +
                            "  0.0000000E+00 0.0000000E+00  3943.536      50.22695     0.0000000E+00 311000\n" +
                            "  0.0000000E+00 0.0000000E+00  12703.61     -2209.912     0.0000000E+00 221000\n" +
                            "  0.0000000E+00 0.0000000E+00  15911.81     -9082.975     0.0000000E+00 131000\n" +
                            "  0.0000000E+00 0.0000000E+00  4981.513     -10236.52     0.0000000E+00 041000\n" +
                            "   3107.959      1075.329     0.0000000E+00 0.0000000E+00  161.0526     302000\n" +
                            "   23636.37      7792.566     0.0000000E+00 0.0000000E+00  1328.432     212000\n" +
                            "   58548.51      18080.12     0.0000000E+00 0.0000000E+00  3407.112     122000\n" +
                            "   46676.95      13063.34     0.0000000E+00 0.0000000E+00  2629.133     032000\n" +
                            "  0.0000000E+00 0.0000000E+00  3741.045      2066.746     0.0000000E+00 203000\n" +
                            "  0.0000000E+00 0.0000000E+00  22200.79      12288.51     0.0000000E+00 113000\n" +
                            "  0.0000000E+00 0.0000000E+00  32833.39      18198.93     0.0000000E+00 023000\n" +
                            "  -1962.324     -880.5830     0.0000000E+00 0.0000000E+00 -303.3870     104000\n" +
                            "  -5916.867     -2644.850     0.0000000E+00 0.0000000E+00 -948.0636     014000\n" +
                            "  0.0000000E+00 0.0000000E+00  308.2844      166.7971     0.0000000E+00 005000\n" +
                            "  0.0000000E+00 0.0000000E+00  588.2945     -45.30959     0.0000000E+00 400100\n" +
                            "  0.0000000E+00 0.0000000E+00  3710.811     -1949.615     0.0000000E+00 310100\n" +
                            "  0.0000000E+00 0.0000000E+00  2680.001     -14706.56     0.0000000E+00 220100\n" +
                            "  0.0000000E+00 0.0000000E+00 -21262.68     -40756.77     0.0000000E+00 130100\n" +
                            "  0.0000000E+00 0.0000000E+00 -35377.80     -38802.34     0.0000000E+00 040100\n" +
                            "   12924.79      4119.674     0.0000000E+00 0.0000000E+00  2134.341     301100\n" +
                            "   73574.17      19785.50     0.0000000E+00 0.0000000E+00  13983.02     211100\n" +
                            "   105220.6      13193.57     0.0000000E+00 0.0000000E+00  27116.26     121100\n" +
                            "   1329.647     -27871.57     0.0000000E+00 0.0000000E+00  13076.62     031100\n" +
                            "  0.0000000E+00 0.0000000E+00  13280.99      7478.098     0.0000000E+00 202100\n" +
                            "  0.0000000E+00 0.0000000E+00  70880.97      39893.66     0.0000000E+00 112100\n" +
                            "  0.0000000E+00 0.0000000E+00  94110.34      52927.86     0.0000000E+00 022100\n" +
                            "  -8366.998     -3516.772     0.0000000E+00 0.0000000E+00  496.2780     103100\n" +
                            "  -23073.79     -9799.116     0.0000000E+00 0.0000000E+00  1426.137     013100\n" +
                            "  0.0000000E+00 0.0000000E+00  543.8413      341.7025     0.0000000E+00 004100\n" +
                            "   8.084030    -0.4278971     0.0000000E+00 0.0000000E+00 -15.26572     400001\n" +
                            "   327.3110      48.04626     0.0000000E+00 0.0000000E+00 -214.7332     310001\n" +
                            "   2019.737      189.4998     0.0000000E+00 0.0000000E+00 -1124.747     220001\n" +
                            "   4716.308      18.10270     0.0000000E+00 0.0000000E+00 -2467.068     130001\n" +
                            "   4088.485     -343.0320     0.0000000E+00 0.0000000E+00 -1888.345     040001\n" +
                            "  0.0000000E+00 0.0000000E+00  35.07213      28.98270     0.0000000E+00 301001\n" +
                            "  0.0000000E+00 0.0000000E+00  1473.766      889.1035     0.0000000E+00 211001\n" +
                            "  0.0000000E+00 0.0000000E+00  8639.771      4919.217     0.0000000E+00 121001\n" +
                            "  0.0000000E+00 0.0000000E+00  13507.94      7465.877     0.0000000E+00 031001\n" +
                            "  -1750.100     -727.9282     0.0000000E+00 0.0000000E+00 -190.0005     202001\n" +
                            "  -6019.215     -2479.123     0.0000000E+00 0.0000000E+00 -692.5369     112001\n" +
                            "  -2010.626     -782.2825     0.0000000E+00 0.0000000E+00 -232.1128     022001\n" +
                            "  0.0000000E+00 0.0000000E+00 -2156.594     -1055.112     0.0000000E+00 103001\n" +
                            "  0.0000000E+00 0.0000000E+00 -6706.981     -3339.352     0.0000000E+00 013001\n" +
                            "   604.5167      235.7421     0.0000000E+00 0.0000000E+00  111.6525     004001\n" +
                            "   15674.31      4643.392     0.0000000E+00 0.0000000E+00  4898.282     300200\n" +
                            "   48627.62      6429.200     0.0000000E+00 0.0000000E+00  26239.02     210200\n" +
                            "  -82802.77     -67960.72     0.0000000E+00 0.0000000E+00  35875.57     120200\n" +
                            "  -267240.3     -137954.4     0.0000000E+00 0.0000000E+00  2377.565     030200\n" +
                            "  0.0000000E+00 0.0000000E+00 -26076.26     -13578.81     0.0000000E+00 201200\n" +
                            "  0.0000000E+00 0.0000000E+00 -158672.9     -82966.55     0.0000000E+00 111200\n" +
                            "  0.0000000E+00 0.0000000E+00 -239738.8     -125769.5     0.0000000E+00 021200\n" +
                            "  -52700.08     -21975.98     0.0000000E+00 0.0000000E+00 -2177.924     102200\n" +
                            "  -150636.5     -62673.38     0.0000000E+00 0.0000000E+00 -8360.588     012200\n" +
                            "  0.0000000E+00 0.0000000E+00  637.8574      407.8439     0.0000000E+00 003200\n" +
                            "  0.0000000E+00 0.0000000E+00  347.2028      203.9615     0.0000000E+00 300101\n" +
                            "  0.0000000E+00 0.0000000E+00  6932.957      3663.105     0.0000000E+00 210101\n" +
                            "  0.0000000E+00 0.0000000E+00  33501.50      17046.26     0.0000000E+00 120101\n" +
                            "  0.0000000E+00 0.0000000E+00  47471.73      23560.07     0.0000000E+00 030101\n" +
                            "  -4911.787     -2447.638     0.0000000E+00 0.0000000E+00 -616.2590     201101\n" +
                            "   4059.825     -641.6613     0.0000000E+00 0.0000000E+00  636.5295     111101\n" +
                            "   51300.31      17898.49     0.0000000E+00 0.0000000E+00  6918.007     021101\n" +
                            "  0.0000000E+00 0.0000000E+00 -6002.915     -3541.639     0.0000000E+00 102101\n" +
                            "  0.0000000E+00 0.0000000E+00 -14083.42     -8729.309     0.0000000E+00 012101\n" +
                            "   3134.882      1203.745     0.0000000E+00 0.0000000E+00 -76.30195     003101\n" +
                            "  -34.52574     -8.445100     0.0000000E+00 0.0000000E+00  1.208158     300002\n" +
                            "  -426.0693     -107.8521     0.0000000E+00 0.0000000E+00  46.51477     210002\n" +
                            "  -1682.231     -392.8324     0.0000000E+00 0.0000000E+00  230.5469     120002\n" +
                            "  -2100.500     -429.1439     0.0000000E+00 0.0000000E+00  309.7073     030002\n" +
                            "  0.0000000E+00 0.0000000E+00 -67.33062     -13.69250     0.0000000E+00 201002\n" +
                            "  0.0000000E+00 0.0000000E+00 -772.1227     -214.1229     0.0000000E+00 111002\n" +
                            "  0.0000000E+00 0.0000000E+00 -2016.626     -628.5572     0.0000000E+00 021002\n" +
                            "   117.7750      103.0616     0.0000000E+00 0.0000000E+00 -14.85020     102002\n" +
                            "  -757.0800     -106.0903     0.0000000E+00 0.0000000E+00 -185.8718     012002\n" +
                            "  0.0000000E+00 0.0000000E+00  395.7722      165.9634     0.0000000E+00 003002\n" +
                            "  0.0000000E+00 0.0000000E+00 -83109.43     -43846.10     0.0000000E+00 200300\n" +
                            "  0.0000000E+00 0.0000000E+00 -430153.4     -226718.0     0.0000000E+00 110300\n" +
                            "  0.0000000E+00 0.0000000E+00 -553565.9     -291243.8     0.0000000E+00 020300\n" +
                            "  -166319.0     -68306.67     0.0000000E+00 0.0000000E+00 -27229.71     101300\n" +
                            "  -437474.7     -179928.3     0.0000000E+00 0.0000000E+00 -70613.70     011300\n" +
                            "  0.0000000E+00 0.0000000E+00 -8954.419     -4092.945     0.0000000E+00 002300\n" +
                            "   2112.401     -442.0413     0.0000000E+00 0.0000000E+00  279.2412     200201\n" +
                            "   55926.44      16620.38     0.0000000E+00 0.0000000E+00  8067.026     110201\n" +
                            "   129873.6      45872.52     0.0000000E+00 0.0000000E+00  18666.14     020201\n" +
                            "  0.0000000E+00 0.0000000E+00  17503.38      8164.989     0.0000000E+00 101201\n" +
                            "  0.0000000E+00 0.0000000E+00  54589.76      25736.39     0.0000000E+00 011201\n" +
                            "   16000.39      6208.505     0.0000000E+00 0.0000000E+00  1472.555     002201\n" +
                            "  0.0000000E+00 0.0000000E+00 -306.6286     -86.99716     0.0000000E+00 200102\n" +
                            "  0.0000000E+00 0.0000000E+00 -2859.744     -873.2438     0.0000000E+00 110102\n" +
                            "  0.0000000E+00 0.0000000E+00 -6304.511     -1902.447     0.0000000E+00 020102\n" +
                            "  -2034.898     -344.7222     0.0000000E+00 0.0000000E+00 -335.2140     101102\n" +
                            "  -11162.04     -3242.857     0.0000000E+00 0.0000000E+00 -1604.423     011102\n" +
                            "  0.0000000E+00 0.0000000E+00  1043.923      568.3360     0.0000000E+00 002102\n" +
                            "  -9.662418     -2.526971     0.0000000E+00 0.0000000E+00 -3.179145     200003\n" +
                            "  -49.31861     -14.60326     0.0000000E+00 0.0000000E+00 -25.12891     110003\n" +
                            "  -64.98927     -23.18605     0.0000000E+00 0.0000000E+00 -49.00786     020003\n" +
                            "  0.0000000E+00 0.0000000E+00  11.37473      7.281695     0.0000000E+00 101003\n" +
                            "  0.0000000E+00 0.0000000E+00  19.73339      13.65670     0.0000000E+00 011003\n" +
                            "   45.91699      5.297607     0.0000000E+00 0.0000000E+00  17.87454     002003\n" +
                            "  -49275.91     -20003.51     0.0000000E+00 0.0000000E+00  1388.944     100400\n" +
                            "  -125211.7     -50667.76     0.0000000E+00 0.0000000E+00  5514.844     010400\n" +
                            "  0.0000000E+00 0.0000000E+00  18218.14      10087.31     0.0000000E+00 001400\n" +
                            "  0.0000000E+00 0.0000000E+00  32416.54      19492.13     0.0000000E+00 100301\n" +
                            "  0.0000000E+00 0.0000000E+00  80306.70      48303.36     0.0000000E+00 010301\n" +
                            "   31292.61      13730.97     0.0000000E+00 0.0000000E+00  4505.717     001301\n" +
                            "  -7108.218     -2191.539     0.0000000E+00 0.0000000E+00 -794.3577     100202\n" +
                            "  -23797.95     -8147.170     0.0000000E+00 0.0000000E+00 -2471.270     010202\n" +
                            "  0.0000000E+00 0.0000000E+00 -1626.521     -785.3102     0.0000000E+00 001202\n" +
                            "  0.0000000E+00 0.0000000E+00  34.25744      31.78482     0.0000000E+00 100103\n" +
                            "  0.0000000E+00 0.0000000E+00  116.1556      80.66497     0.0000000E+00 010103\n" +
                            "   637.4097      187.8787     0.0000000E+00 0.0000000E+00  82.28752     001103\n" +
                            "  -4.106870    -0.5831851     0.0000000E+00 0.0000000E+00 0.2933042     100004\n" +
                            "  -19.09169     -4.009389     0.0000000E+00 0.0000000E+00 0.2398180     010004\n" +
                            "  0.0000000E+00 0.0000000E+00  7.002280    -0.2450467     0.0000000E+00 001004\n" +
                            "  0.0000000E+00 0.0000000E+00  23067.11      13275.94     0.0000000E+00 000500\n" +
                            "  -2845.001     -554.2254     0.0000000E+00 0.0000000E+00 -1933.140     000401\n" +
                            "  0.0000000E+00 0.0000000E+00 -810.2422     -784.7085     0.0000000E+00 000302\n" +
                            "   858.5546      331.9975     0.0000000E+00 0.0000000E+00  50.74725     000203\n" +
                            "  0.0000000E+00 0.0000000E+00  37.49832      9.220976     0.0000000E+00 000104\n" +
                            "  0.2305421     0.1385446E-01 0.0000000E+00 0.0000000E+00 0.6067424     000005"
            );

            optics_后偏转段CCT2出口加1点5米.map = CosyArbitraryOrder.readMap(
                    "  0.2776683     0.6426617     0.0000000E+00 0.0000000E+00 0.9122470E-02 100000\n" +
                            "  -1.186158     0.8560639     0.0000000E+00 0.0000000E+00-0.2043148E-02 010000\n" +
                            "  0.0000000E+00 0.0000000E+00 -1.122876     0.1209793     0.0000000E+00 001000\n" +
                            "  0.0000000E+00 0.0000000E+00  1.145577     -1.013995     0.0000000E+00 000100\n" +
                            "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00  1.000000     000010\n" +
                            "  0.1025337E-01-0.9122470E-02 0.0000000E+00 0.0000000E+00  1.099486     000001\n" +
                            "  -4.794570    -0.9384987     0.0000000E+00 0.0000000E+00-0.7691690E-01 200000\n" +
                            "  -16.05604     0.4203887     0.0000000E+00 0.0000000E+00 0.2429687     110000\n" +
                            "  -6.393644      9.054778     0.0000000E+00 0.0000000E+00 0.1516436     020000\n" +
                            "  0.0000000E+00 0.0000000E+00 -16.64587     0.7224144E-01 0.0000000E+00 101000\n" +
                            "  0.0000000E+00 0.0000000E+00 -48.46427    -0.1617982E-01 0.0000000E+00 011000\n" +
                            "  -1.962767     -1.062605     0.0000000E+00 0.0000000E+00-0.7921112     002000\n" +
                            "  0.0000000E+00 0.0000000E+00 -18.59234      16.96122     0.0000000E+00 100100\n" +
                            "  0.0000000E+00 0.0000000E+00 -42.80908      48.39364     0.0000000E+00 010100\n" +
                            "   33.57326      17.21543     0.0000000E+00 0.0000000E+00  1.053095     001100\n" +
                            "  0.5105635E-01 0.2878382     0.0000000E+00 0.0000000E+00-0.8825607E-02 100001\n" +
                            "  0.5218690    -0.1791484     0.0000000E+00 0.0000000E+00-0.2611056E-02 010001\n" +
                            "  0.0000000E+00 0.0000000E+00  1.108477     -1.478991     0.0000000E+00 001001\n" +
                            "  -2.012944     -3.618418     0.0000000E+00 0.0000000E+00 -5.584795     000200\n" +
                            "  0.0000000E+00 0.0000000E+00  11.76418    -0.7595797     0.0000000E+00 000101\n" +
                            " -0.8029310E-02 0.3461719E-02 0.0000000E+00 0.0000000E+00-0.9384646     000002\n" +
                            "   9.608044      7.152302     0.0000000E+00 0.0000000E+00  1.467459     300000\n" +
                            "   46.28658      49.65767     0.0000000E+00 0.0000000E+00  6.760075     210000\n" +
                            "   51.19955      119.5897     0.0000000E+00 0.0000000E+00  8.302247     120000\n" +
                            "  -6.711845      104.1643     0.0000000E+00 0.0000000E+00  2.633405     030000\n" +
                            "  0.0000000E+00 0.0000000E+00 -81.62473     -49.36324     0.0000000E+00 201000\n" +
                            "  0.0000000E+00 0.0000000E+00 -515.2568     -283.7283     0.0000000E+00 111000\n" +
                            "  0.0000000E+00 0.0000000E+00 -805.5788     -408.8501     0.0000000E+00 021000\n" +
                            "  -118.1868     -61.78520     0.0000000E+00 0.0000000E+00 -12.80494     102000\n" +
                            "  -343.3604     -179.9722     0.0000000E+00 0.0000000E+00 -37.99079     012000\n" +
                            "  0.0000000E+00 0.0000000E+00-0.4594881      7.991398     0.0000000E+00 003000\n" +
                            "  0.0000000E+00 0.0000000E+00 -195.5629     -105.1012     0.0000000E+00 200100\n" +
                            "  0.0000000E+00 0.0000000E+00 -1205.273     -562.3613     0.0000000E+00 110100\n" +
                            "  0.0000000E+00 0.0000000E+00 -1843.427     -746.1342     0.0000000E+00 020100\n" +
                            "  -197.5378     -81.13102     0.0000000E+00 0.0000000E+00 -19.14783     101100\n" +
                            "  -458.8269     -177.8524     0.0000000E+00 0.0000000E+00 -30.33033     011100\n" +
                            "  0.0000000E+00 0.0000000E+00 -101.7004     -8.898953     0.0000000E+00 002100\n" +
                            "   1.818009    -0.3124955     0.0000000E+00 0.0000000E+00 -1.236418     200001\n" +
                            "   14.64892     -1.071271     0.0000000E+00 0.0000000E+00 -7.302995     110001\n" +
                            "   10.35781     -9.397693     0.0000000E+00 0.0000000E+00 -10.50941     020001\n" +
                            "  0.0000000E+00 0.0000000E+00 -2.611260      1.466717     0.0000000E+00 101001\n" +
                            "  0.0000000E+00 0.0000000E+00  24.72067      3.999954     0.0000000E+00 011001\n" +
                            "   26.68979      13.34967     0.0000000E+00 0.0000000E+00  3.753262     002001\n" +
                            "   714.2971      394.1066     0.0000000E+00 0.0000000E+00  99.60832     100200\n" +
                            "   1853.161      1028.766     0.0000000E+00 0.0000000E+00  268.5466     010200\n" +
                            "  0.0000000E+00 0.0000000E+00 -81.95641      118.5879     0.0000000E+00 001200\n" +
                            "  0.0000000E+00 0.0000000E+00  4.438044      3.141818     0.0000000E+00 100101\n" +
                            "  0.0000000E+00 0.0000000E+00  58.92278     -8.411894     0.0000000E+00 010101\n" +
                            "   10.92382      10.96079     0.0000000E+00 0.0000000E+00  1.224397     001101\n" +
                            "  -2.563014     -1.257206     0.0000000E+00 0.0000000E+00 0.6918991E-01 100002\n" +
                            "  -7.449959     -3.396481     0.0000000E+00 0.0000000E+00 0.4718524E-01 010002\n" +
                            "  0.0000000E+00 0.0000000E+00  4.612909      2.479513     0.0000000E+00 001002\n" +
                            "  0.0000000E+00 0.0000000E+00 -273.7456     -37.71632     0.0000000E+00 000300\n" +
                            "  -196.4067     -107.5448     0.0000000E+00 0.0000000E+00 -12.87745     000201\n" +
                            "  0.0000000E+00 0.0000000E+00 0.9355174      7.949245     0.0000000E+00 000102\n" +
                            "  0.5761438E-01 0.7711552E-03 0.0000000E+00 0.0000000E+00 0.7854125     000003\n" +
                            "  -31.26232     -5.141206     0.0000000E+00 0.0000000E+00-0.7111171     400000\n" +
                            "  -250.0263      12.33565     0.0000000E+00 0.0000000E+00  20.17116     310000\n" +
                            "  -709.0624      315.1893     0.0000000E+00 0.0000000E+00  163.7453     220000\n" +
                            "  -858.1851      1009.924     0.0000000E+00 0.0000000E+00  385.2723     130000\n" +
                            "  -402.7031      949.0020     0.0000000E+00 0.0000000E+00  283.4603     040000\n" +
                            "  0.0000000E+00 0.0000000E+00 -81.12796     -89.00278     0.0000000E+00 301000\n" +
                            "  0.0000000E+00 0.0000000E+00 -949.6926     -941.0648     0.0000000E+00 211000\n" +
                            "  0.0000000E+00 0.0000000E+00 -3469.180     -3206.121     0.0000000E+00 121000\n" +
                            "  0.0000000E+00 0.0000000E+00 -4065.823     -3550.247     0.0000000E+00 031000\n" +
                            "  -421.7657     -269.1820     0.0000000E+00 0.0000000E+00 -45.00588     202000\n" +
                            "  -2858.868     -1779.914     0.0000000E+00 0.0000000E+00 -347.2664     112000\n" +
                            "  -4723.410     -2883.765     0.0000000E+00 0.0000000E+00 -624.9095     022000\n" +
                            "  0.0000000E+00 0.0000000E+00  199.1393      152.4807     0.0000000E+00 103000\n" +
                            "  0.0000000E+00 0.0000000E+00  554.4497      436.9210     0.0000000E+00 013000\n" +
                            "  -98.16593     -51.60734     0.0000000E+00 0.0000000E+00 -13.77457     004000\n" +
                            "  0.0000000E+00 0.0000000E+00 -376.5294     -275.0352     0.0000000E+00 300100\n" +
                            "  0.0000000E+00 0.0000000E+00 -3748.012     -2711.648     0.0000000E+00 210100\n" +
                            "  0.0000000E+00 0.0000000E+00 -12351.47     -8792.925     0.0000000E+00 120100\n" +
                            "  0.0000000E+00 0.0000000E+00 -13494.24     -9381.292     0.0000000E+00 030100\n" +
                            "  -4265.116     -2290.130     0.0000000E+00 0.0000000E+00 -443.5480     201100\n" +
                            "  -24540.79     -13143.96     0.0000000E+00 0.0000000E+00 -2732.015     111100\n" +
                            "  -35251.57     -18843.52     0.0000000E+00 0.0000000E+00 -4132.867     021100\n" +
                            "  0.0000000E+00 0.0000000E+00 -601.9931     -446.2564     0.0000000E+00 102100\n" +
                            "  0.0000000E+00 0.0000000E+00 -1976.819     -1402.148     0.0000000E+00 012100\n" +
                            "  -354.5824     -167.2618     0.0000000E+00 0.0000000E+00 -12.66813     003100\n" +
                            "   24.74848      13.64079     0.0000000E+00 0.0000000E+00 -3.770737     300001\n" +
                            "   218.5339      94.46927     0.0000000E+00 0.0000000E+00 -35.21520     210001\n" +
                            "   712.1403      226.4735     0.0000000E+00 0.0000000E+00 -100.9575     120001\n" +
                            "   788.0110      175.4815     0.0000000E+00 0.0000000E+00 -95.59945     030001\n" +
                            "  0.0000000E+00 0.0000000E+00  39.03867      11.62783     0.0000000E+00 201001\n" +
                            "  0.0000000E+00 0.0000000E+00  483.8318      209.7734     0.0000000E+00 111001\n" +
                            "  0.0000000E+00 0.0000000E+00  1145.356      514.6545     0.0000000E+00 021001\n" +
                            "   274.8815      128.1295     0.0000000E+00 0.0000000E+00  37.50053     102001\n" +
                            "   1046.839      502.9321     0.0000000E+00 0.0000000E+00  147.1170     012001\n" +
                            "  0.0000000E+00 0.0000000E+00 -78.88113     -53.50182     0.0000000E+00 003001\n" +
                            "  -8102.510     -4024.352     0.0000000E+00 0.0000000E+00 -1075.920     200200\n" +
                            "  -39989.98     -19785.78     0.0000000E+00 0.0000000E+00 -5204.075     110200\n" +
                            "  -48569.94     -23913.15     0.0000000E+00 0.0000000E+00 -6205.806     020200\n" +
                            "  0.0000000E+00 0.0000000E+00 -2183.570     -1687.788     0.0000000E+00 101200\n" +
                            "  0.0000000E+00 0.0000000E+00 -6031.060     -4497.940     0.0000000E+00 011200\n" +
                            "  -971.7430     -458.4595     0.0000000E+00 0.0000000E+00 -271.9668     002200\n" +
                            "  0.0000000E+00 0.0000000E+00  188.7242      81.57972     0.0000000E+00 200101\n" +
                            "  0.0000000E+00 0.0000000E+00  1687.340      714.1154     0.0000000E+00 110101\n" +
                            "  0.0000000E+00 0.0000000E+00  3415.582      1318.235     0.0000000E+00 020101\n" +
                            "   2313.570      1171.044     0.0000000E+00 0.0000000E+00  271.9714     101101\n" +
                            "   6579.033      3338.694     0.0000000E+00 0.0000000E+00  771.3538     011101\n" +
                            "  0.0000000E+00 0.0000000E+00  57.25211     -19.24578     0.0000000E+00 002101\n" +
                            "   3.250937      1.827216     0.0000000E+00 0.0000000E+00  2.370856     200002\n" +
                            "   7.591117      8.109118     0.0000000E+00 0.0000000E+00  16.18222     110002\n" +
                            "   8.427655      15.35664     0.0000000E+00 0.0000000E+00  27.48150     020002\n" +
                            "  0.0000000E+00 0.0000000E+00  4.201344     -3.346234     0.0000000E+00 101002\n" +
                            "  0.0000000E+00 0.0000000E+00 -3.594992     -6.022921     0.0000000E+00 011002\n" +
                            "  -53.03218     -21.70561     0.0000000E+00 0.0000000E+00 -9.708939     002002\n" +
                            "  0.0000000E+00 0.0000000E+00  3624.002      2541.632     0.0000000E+00 100300\n" +
                            "  0.0000000E+00 0.0000000E+00  9331.596      6631.442     0.0000000E+00 010300\n" +
                            "   3171.143      1782.203     0.0000000E+00 0.0000000E+00  485.5978     001300\n" +
                            "   3196.539      1769.837     0.0000000E+00 0.0000000E+00  270.9897     100201\n" +
                            "   7208.659      3980.427     0.0000000E+00 0.0000000E+00  479.1555     010201\n" +
                            "  0.0000000E+00 0.0000000E+00  205.4538     -2.960204     0.0000000E+00 001201\n" +
                            "  0.0000000E+00 0.0000000E+00  8.099064     -13.07404     0.0000000E+00 100102\n" +
                            "  0.0000000E+00 0.0000000E+00 -37.53919     -25.63350     0.0000000E+00 010102\n" +
                            "  -305.1308     -156.6929     0.0000000E+00 0.0000000E+00 -31.48385     001102\n" +
                            "   3.320753     0.9201753     0.0000000E+00 0.0000000E+00-0.1685969     100003\n" +
                            "   12.77648      4.287405     0.0000000E+00 0.0000000E+00-0.1540039     010003\n" +
                            "  0.0000000E+00 0.0000000E+00 -6.732275     -1.435093     0.0000000E+00 001003\n" +
                            "   3006.303      1678.475     0.0000000E+00 0.0000000E+00  258.3166     000400\n" +
                            "  0.0000000E+00 0.0000000E+00 -757.5618     -919.2420     0.0000000E+00 000301\n" +
                            "  -104.8408     -95.61125     0.0000000E+00 0.0000000E+00 -2.560146     000202\n" +
                            "  0.0000000E+00 0.0000000E+00 -16.90886     -10.65300     0.0000000E+00 000103\n" +
                            " -0.1147099     0.9122754E-03 0.0000000E+00 0.0000000E+00-0.6621582     000004\n" +
                            "   28.04513      43.32422     0.0000000E+00 0.0000000E+00  35.06026     500000\n" +
                            "  -17.16662      519.1536     0.0000000E+00 0.0000000E+00  417.2422     410000\n" +
                            "  -1922.727      2657.028     0.0000000E+00 0.0000000E+00  2108.110     320000\n" +
                            "  -9259.653      7452.509     0.0000000E+00 0.0000000E+00  5708.808     230000\n" +
                            "  -16818.81      11492.56     0.0000000E+00 0.0000000E+00  8220.921     140000\n" +
                            "  -10783.48      7639.827     0.0000000E+00 0.0000000E+00  4927.205     050000\n" +
                            "  0.0000000E+00 0.0000000E+00  408.9012      52.31768     0.0000000E+00 401000\n" +
                            "  0.0000000E+00 0.0000000E+00  3959.014      50.22695     0.0000000E+00 311000\n" +
                            "  0.0000000E+00 0.0000000E+00  13930.90     -2209.912     0.0000000E+00 221000\n" +
                            "  0.0000000E+00 0.0000000E+00  20607.00     -9082.975     0.0000000E+00 131000\n" +
                            "  0.0000000E+00 0.0000000E+00  10166.86     -10236.52     0.0000000E+00 041000\n" +
                            "   2590.366      1075.329     0.0000000E+00 0.0000000E+00  122.6722     302000\n" +
                            "   19859.37      7792.566     0.0000000E+00 0.0000000E+00  931.8667     212000\n" +
                            "   49730.88      18080.12     0.0000000E+00 0.0000000E+00  2134.735     122000\n" +
                            "   40277.35      13063.34     0.0000000E+00 0.0000000E+00  1334.800     032000\n" +
                            "  0.0000000E+00 0.0000000E+00  2709.754      2066.746     0.0000000E+00 203000\n" +
                            "  0.0000000E+00 0.0000000E+00  16067.70      12288.51     0.0000000E+00 113000\n" +
                            "  0.0000000E+00 0.0000000E+00  23746.84      18198.93     0.0000000E+00 023000\n" +
                            "  -1522.656     -880.5830     0.0000000E+00 0.0000000E+00 -288.9993     104000\n" +
                            "  -4594.923     -2644.850     0.0000000E+00 0.0000000E+00 -892.2500     014000\n" +
                            "  0.0000000E+00 0.0000000E+00  224.7640      166.7971     0.0000000E+00 005000\n" +
                            "  0.0000000E+00 0.0000000E+00  629.5025     -45.30959     0.0000000E+00 400100\n" +
                            "  0.0000000E+00 0.0000000E+00  4810.968     -1949.615     0.0000000E+00 310100\n" +
                            "  0.0000000E+00 0.0000000E+00  10301.37     -14706.56     0.0000000E+00 220100\n" +
                            "  0.0000000E+00 0.0000000E+00 -703.1872     -40756.77     0.0000000E+00 130100\n" +
                            "  0.0000000E+00 0.0000000E+00 -15961.39     -38802.34     0.0000000E+00 040100\n" +
                            "   10894.66      4119.674     0.0000000E+00 0.0000000E+00  1561.008     301100\n" +
                            "   63734.58      19785.50     0.0000000E+00 0.0000000E+00  9512.936     211100\n" +
                            "   98453.53      13193.57     0.0000000E+00 0.0000000E+00  15775.97     121100\n" +
                            "   15004.41     -27871.57     0.0000000E+00 0.0000000E+00  3790.439     031100\n" +
                            "  0.0000000E+00 0.0000000E+00  9524.616      7478.098     0.0000000E+00 202100\n" +
                            "  0.0000000E+00 0.0000000E+00  50838.78      39893.66     0.0000000E+00 112100\n" +
                            "  0.0000000E+00 0.0000000E+00  67520.07      52927.86     0.0000000E+00 022100\n" +
                            "  -6590.543     -3516.772     0.0000000E+00 0.0000000E+00  172.7137     103100\n" +
                            "  -18154.07     -9799.116     0.0000000E+00 0.0000000E+00  510.4348     013100\n" +
                            "  0.0000000E+00 0.0000000E+00  375.9512      341.7025     0.0000000E+00 004100\n" +
                            "   6.801836    -0.4278971     0.0000000E+00 0.0000000E+00 -14.21893     400001\n" +
                            "   306.8351      48.04626     0.0000000E+00 0.0000000E+00 -206.2052     310001\n" +
                            "   2020.156      189.4998     0.0000000E+00 0.0000000E+00 -1099.498     220001\n" +
                            "   5011.374      18.10270     0.0000000E+00 0.0000000E+00 -2456.881     130001\n" +
                            "   4542.520     -343.0320     0.0000000E+00 0.0000000E+00 -1931.362     040001\n" +
                            "  0.0000000E+00 0.0000000E+00 -5.050889      28.98270     0.0000000E+00 301001\n" +
                            "  0.0000000E+00 0.0000000E+00  764.0966      889.1035     0.0000000E+00 211001\n" +
                            "  0.0000000E+00 0.0000000E+00  5286.282      4919.217     0.0000000E+00 121001\n" +
                            "  0.0000000E+00 0.0000000E+00  8787.922      7465.877     0.0000000E+00 031001\n" +
                            "  -1466.400     -727.9282     0.0000000E+00 0.0000000E+00 -135.5931     202001\n" +
                            "  -5291.834     -2479.123     0.0000000E+00 0.0000000E+00 -368.9408     112001\n" +
                            "  -2435.526     -782.2825     0.0000000E+00 0.0000000E+00  214.6655     022001\n" +
                            "  0.0000000E+00 0.0000000E+00 -1587.518     -1055.112     0.0000000E+00 103001\n" +
                            "  0.0000000E+00 0.0000000E+00 -4916.818     -3339.352     0.0000000E+00 013001\n" +
                            "   472.0888      235.7421     0.0000000E+00 0.0000000E+00  102.0819     004001\n" +
                            "   13136.79      4643.392     0.0000000E+00 0.0000000E+00  3641.351     300200\n" +
                            "   44179.47      6429.200     0.0000000E+00 0.0000000E+00  18114.21     210200\n" +
                            "  -51012.37     -67960.72     0.0000000E+00 0.0000000E+00  19170.88     120200\n" +
                            "  -199416.3     -137954.4     0.0000000E+00 0.0000000E+00 -8287.830     030200\n" +
                            "  0.0000000E+00 0.0000000E+00 -19448.57     -13578.81     0.0000000E+00 201200\n" +
                            "  0.0000000E+00 0.0000000E+00 -117793.6     -82966.55     0.0000000E+00 111200\n" +
                            "  0.0000000E+00 0.0000000E+00 -177317.6     -125769.5     0.0000000E+00 021200\n" +
                            "  -41882.86     -21975.98     0.0000000E+00 0.0000000E+00 -2677.161     102200\n" +
                            "  -119550.1     -62673.38     0.0000000E+00 0.0000000E+00 -9327.649     012200\n" +
                            "  0.0000000E+00 0.0000000E+00  406.3595      407.8439     0.0000000E+00 003200\n" +
                            "  0.0000000E+00 0.0000000E+00  169.0516      203.9615     0.0000000E+00 300101\n" +
                            "  0.0000000E+00 0.0000000E+00  4351.851      3663.105     0.0000000E+00 210101\n" +
                            "  0.0000000E+00 0.0000000E+00  22539.80      17046.26     0.0000000E+00 120101\n" +
                            "  0.0000000E+00 0.0000000E+00  33080.82      23560.07     0.0000000E+00 030101\n" +
                            "  -4317.848     -2447.638     0.0000000E+00 0.0000000E+00 -338.3112     201101\n" +
                            "   757.7083     -641.6613     0.0000000E+00 0.0000000E+00  1812.913     111101\n" +
                            "   37131.35      17898.49     0.0000000E+00 0.0000000E+00  7970.334     021101\n" +
                            "  0.0000000E+00 0.0000000E+00 -4338.619     -3541.639     0.0000000E+00 102101\n" +
                            "  0.0000000E+00 0.0000000E+00 -10076.97     -8729.309     0.0000000E+00 012101\n" +
                            "   2489.268      1203.745     0.0000000E+00 0.0000000E+00  12.84693     003101\n" +
                            "  -27.59147     -8.445100     0.0000000E+00 0.0000000E+00  1.728641     300002\n" +
                            "  -354.0990     -107.8521     0.0000000E+00 0.0000000E+00  49.08524     210002\n" +
                            "  -1444.476     -392.8324     0.0000000E+00 0.0000000E+00  235.9060     120002\n" +
                            "  -1857.247     -429.1439     0.0000000E+00 0.0000000E+00  313.1955     030002\n" +
                            "  0.0000000E+00 0.0000000E+00 -47.45168     -13.69250     0.0000000E+00 201002\n" +
                            "  0.0000000E+00 0.0000000E+00 -549.4554     -214.1229     0.0000000E+00 111002\n" +
                            "  0.0000000E+00 0.0000000E+00 -1475.794     -628.5572     0.0000000E+00 021002\n" +
                            "   114.3145      103.0616     0.0000000E+00 0.0000000E+00 -22.34505     102002\n" +
                            "  -527.2029     -106.0903     0.0000000E+00 0.0000000E+00 -200.1462     012002\n" +
                            "  0.0000000E+00 0.0000000E+00  295.9678      165.9634     0.0000000E+00 003002\n" +
                            "  0.0000000E+00 0.0000000E+00 -60732.64     -43846.10     0.0000000E+00 200300\n" +
                            "  0.0000000E+00 0.0000000E+00 -314513.5     -226718.0     0.0000000E+00 110300\n" +
                            "  0.0000000E+00 0.0000000E+00 -405075.5     -291243.8     0.0000000E+00 020300\n" +
                            "  -131868.8     -68306.67     0.0000000E+00 0.0000000E+00 -23799.22     101300\n" +
                            "  -346835.2     -179928.3     0.0000000E+00 0.0000000E+00 -61943.23     011300\n" +
                            "  0.0000000E+00 0.0000000E+00 -6797.973     -4092.945     0.0000000E+00 002300\n" +
                            "   1244.565     -442.0413     0.0000000E+00 0.0000000E+00  473.9682     200201\n" +
                            "   42175.74      16620.38     0.0000000E+00 0.0000000E+00  8248.681     110201\n" +
                            "   100306.9      45872.52     0.0000000E+00 0.0000000E+00  17899.52     020201\n" +
                            "  0.0000000E+00 0.0000000E+00  12913.19      8164.989     0.0000000E+00 101201\n" +
                            "  0.0000000E+00 0.0000000E+00  40347.97      25736.39     0.0000000E+00 011201\n" +
                            "   12753.59      6208.505     0.0000000E+00 0.0000000E+00  1431.054     002201\n" +
                            "  0.0000000E+00 0.0000000E+00 -220.0880     -86.99716     0.0000000E+00 200102\n" +
                            "  0.0000000E+00 0.0000000E+00 -2112.486     -873.2438     0.0000000E+00 110102\n" +
                            "  0.0000000E+00 0.0000000E+00 -4835.215     -1902.447     0.0000000E+00 020102\n" +
                            "  -1518.352     -344.7222     0.0000000E+00 0.0000000E+00 -348.6397     101102\n" +
                            "  -8570.872     -3242.857     0.0000000E+00 0.0000000E+00 -1591.941     011102\n" +
                            "  0.0000000E+00 0.0000000E+00  758.2972      568.3360     0.0000000E+00 002102\n" +
                            "  -7.991007     -2.526971     0.0000000E+00 0.0000000E+00 -2.829554     200003\n" +
                            "  -39.48943     -14.60326     0.0000000E+00 0.0000000E+00 -23.27869     110003\n" +
                            "  -45.67225     -23.18605     0.0000000E+00 0.0000000E+00 -47.05749     020003\n" +
                            "  0.0000000E+00 0.0000000E+00  6.524969      7.281695     0.0000000E+00 101003\n" +
                            "  0.0000000E+00 0.0000000E+00  10.42272      13.65670     0.0000000E+00 011003\n" +
                            "   34.29776      5.297607     0.0000000E+00 0.0000000E+00  16.26002     002003\n" +
                            "  -39425.31     -20003.51     0.0000000E+00 0.0000000E+00  384.7094     100400\n" +
                            "  -100256.0     -50667.76     0.0000000E+00 0.0000000E+00  2465.141     010400\n" +
                            "  0.0000000E+00 0.0000000E+00  13044.00      10087.31     0.0000000E+00 001400\n" +
                            "  0.0000000E+00 0.0000000E+00  23343.80      19492.13     0.0000000E+00 100301\n" +
                            "  0.0000000E+00 0.0000000E+00  57970.32      48303.36     0.0000000E+00 010301\n" +
                            "   24917.93      13730.97     0.0000000E+00 0.0000000E+00  4010.234     001301\n" +
                            "  -5596.621     -2191.539     0.0000000E+00 0.0000000E+00 -755.4236     100202\n" +
                            "  -18819.58     -8147.170     0.0000000E+00 0.0000000E+00 -2351.515     010202\n" +
                            "  0.0000000E+00 0.0000000E+00 -1259.631     -785.3102     0.0000000E+00 001202\n" +
                            "  0.0000000E+00 0.0000000E+00  16.96043      31.78482     0.0000000E+00 100103\n" +
                            "  0.0000000E+00 0.0000000E+00  78.59749      80.66497     0.0000000E+00 010103\n" +
                            "   500.3388      187.8787     0.0000000E+00 0.0000000E+00  79.83468     001103\n" +
                            "  -3.346642    -0.5831851     0.0000000E+00 0.0000000E+00 0.2879058     100004\n" +
                            "  -15.34969     -4.009389     0.0000000E+00 0.0000000E+00 0.2200009     010004\n" +
                            "  0.0000000E+00 0.0000000E+00  5.948474    -0.2450467     0.0000000E+00 001004\n" +
                            "  0.0000000E+00 0.0000000E+00  16461.74      13275.94     0.0000000E+00 000500\n" +
                            "  -2071.194     -554.2254     0.0000000E+00 0.0000000E+00 -1578.555     000401\n" +
                            "  0.0000000E+00 0.0000000E+00 -672.6239     -784.7085     0.0000000E+00 000302\n" +
                            "   687.2258      331.9975     0.0000000E+00 0.0000000E+00  55.10810     000203\n" +
                            "  0.0000000E+00 0.0000000E+00  28.30431      9.220976     0.0000000E+00 000104\n" +
                            "  0.2256383     0.1385446E-01 0.0000000E+00 0.0000000E+00 0.5502242     000005"
            );

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

/*
cosy code
-------------
{cosy script for HUST SC Gantry DownStream, B. Qin, 2019-12-25}

INCLUDE 'COSY' ;
PROCEDURE RUN ;

VARIABLE I 1 ; {number of iteration} VARIABLE refresh 1 ; {print picture per refresh}
VARIABLE DL1 1 ; VARIABLE DL2 1 ;
VARIABLE CCT12_APER 1 ; VARIABLE CCT345_APER 1 ; {half aperture}
VARIABLE CCT1_ANG 1 ; VARIABLE CCT2_ANG 1 ; VARIABLE CCT3_ANG 1 ;
VARIABLE CCT12_ANG_MIN 1 ; VARIABLE CCT345_ANG_MIN 1 ;
VARIABLE CCT4_ANG 1 ; VARIABLE CCT5_ANG 1 ;{angle}
VARIABLE CCT12_n 1 ; VARIABLE CCT345_n 1 ;
VARIABLE CCT12_R 1 ; VARIABLE CCT345_R 1 ;
VARIABLE QS1_LEN 1 ; VARIABLE QS1_Q 1 ; VARIABLE QS1_S 1 ; VARIABLE QS1_APER 1 ;
VARIABLE QS2_LEN 1 ; VARIABLE QS2_Q 1 ; VARIABLE QS2_S 1 ; VARIABLE QS2_APER 1 ;
VARIABLE QS3_LEN 1 ; VARIABLE QS3_Q 1 ; VARIABLE QS3_S 1 ; VARIABLE QS3_APER 1 ;
VARIABLE GAP1 1 ; VARIABLE GAP2 1 ; VARIABLE GAP3 1 ;
VARIABLE DP 1 ;
VARIABLE BEAM_X 1 ; VARIABLE BEAM_Y 1 ; {x y 方向束斑大小}
VARIABLE BEAM_X_MAX 1 ; VARIABLE BEAM_X_MIN 1 ;
VARIABLE BEAM_Y_MAX 1 ; VARIABLE BEAM_Y_MIN 1 ;
VARIABLE BEAM_X_AFTER_DL1 1 ;

VARIABLE OBJ_ANG45 1 ; VARIABLE OBJ_ANG135 1 ;
VARIABLE OBJ_R16_1 1 ; VARIABLE OBJ_R16_2 1;
VARIABLE OBJ_R26_1 1 ; VARIABLE OBJ_R26_2 1;
VARIABLE OBJ_R11_1 1 ; VARIABLE OBJ_R11_2 1;
VARIABLE OBJ_R12_1 1 ; VARIABLE OBJ_R12_2 1;
VARIABLE OBJ_R21_1 1 ; VARIABLE OBJ_R21_2 1;
VARIABLE OBJ_R33_1 1 ; VARIABLE OBJ_R33_2 1;
VARIABLE OBJ_R34_1 1 ; VARIABLE OBJ_R34_2 1;
VARIABLE OBJ_R43_1 1 ; VARIABLE OBJ_R43_2 1;
VARIABLE OBJ_T126_1 1 ; VARIABLE OBJ_T126_2 1;
VARIABLE OBJ_T226_1 1 ; VARIABLE OBJ_T226_2 1;
VARIABLE OBJ_T346_1 1 ; VARIABLE OBJ_T346_2 1;
VARIABLE OBJ_T446_1 1 ; VARIABLE OBJ_T446_2 1;
VARIABLE OBJ_SIGMA11_1 1 ; VARIABLE OBJ_SIGMA11_2 1 ;
VARIABLE OBJ_SIGMA33_1 1 ; VARIABLE OBJ_SIGMA33_2 1 ;
VARIABLE OBJ_ALIGN 1 ; VARIABLE IGNORE 1 ;
VARIABLE OBJ_BEAM_X_BEFORE_DL1 1 ;


FUNCTION SQUA X ; SQUA := X * X ; ENDFUNCTION ;

FUNCTION GET_ONE IGNORE ; GET_ONE := 1 ; ENDFUNCTION ; {COSY函数必须传参}

FUNCTION BEAM_SPOT_X RAY ;
BEAM_SPOT_X := 0.0 ; BEAM_X_MAX := -1E9 ; BEAM_X_MIN := 1E9 ;
LOOP I 1 LENGTH(RAY(1)) ;
BEAM_X_MAX := MAX((RAY(1)|I),BEAM_X_MAX);
BEAM_X_MIN := MIN((RAY(1)|I),BEAM_X_MIN);
ENDLOOP ;
BEAM_SPOT_X := BEAM_X_MAX - BEAM_X_MIN ;
ENDFUNCTION ;

FUNCTION BEAM_SPOT_Y RAY ;
BEAM_SPOT_Y := 0.0 ; BEAM_Y_MAX := -1E9 ; BEAM_Y_MIN := 1E9 ;
LOOP I 1 LENGTH(RAY(1)) ;
BEAM_Y_MAX := MAX((RAY(3)|I),BEAM_Y_MAX);
BEAM_Y_MIN := MIN((RAY(3)|I),BEAM_Y_MIN);
ENDLOOP ;
BEAM_SPOT_Y := BEAM_Y_MAX - BEAM_Y_MIN ;
ENDFUNCTION ;


BEAM_X_MAX := -1000. ; BEAM_X_MIN := 1000. ;
BEAM_Y_MAX := -1000. ; BEAM_Y_MIN := 1000. ;

I := 0 ; {number of iteration}
refresh := 1 ; {print picture per refresh}

{--------------PARAMETER SET--------------------}
CCT12_R := 0.75 ;
CCT345_R := 0.75 ;

CCT12_APER := 0.020 ;
CCT345_APER := 0.060 ;
QS1_APER := 0.030 ;
QS2_APER := 0.030 ;
QS3_APER := 0.060 ;

DL1 := 1.1759 ;
DL2 := 2.40 ;
QS1_LEN := 0.2;
QS2_LEN := 0.2;
QS3_LEN := 0.2;
GAP1 := 0.15 ;
GAP2 := 0.15 ;
GAP3:=0.2585751675857585;

CCT12_ANG_MIN := 5 ;
CCT345_ANG_MIN := 8 ;

CCT1_ANG:= 4.116696651312341;
CCT2_ANG:= 8.383304669429050;
CCT3_ANG:=0.3926893224425800E-005;
CCT4_ANG:= 21.99845340255313;
CCT5_ANG:= 21.50155751013952;

CCT12_n:=-13.63885007227421;
CCT345_n:=-4.216451945875646;

QS1_Q:= 1.596245173043834;
QS2_Q:=-1.133857472311528;
QS3_Q:=0.4569;

QS1_S:=-0.1924;
QS2_S:=.6062;
QS3_S:=.1470;


OV 5 3 0 ; {order 1, phase space MSm 3, # of parameters 0}
PTY 0.0 ; {picture type 0.0 for straight-line and nonzore for lab}
RPP 250 ; {particle type = proton, kinetic energy = 250MeV}
SB 3.5E-3 7.5E-3 0    3.5E-3 7.5E-3 0    0 0 0 0 0;
{<x><x'><r12>       <y><y'><r34>     <t'><dE/E><r56><dm/m><z'>}
{x=2.0mm x'=3.0mr y=2.0mm y'=3.0mm z=0.0mm DP/p=5.0% momentum=729.134GeV/c}
CR ;

ENVEL ;

SR  -2.720497e-03  -8.397257e-04  0.000000e+00  0.000000e+00  0  -8.865599e-02  0  0  2 ;
SR  -2.710292e-03  -1.541299e-03  0.000000e+00  0.000000e+00  0  8.569529e-02  0  0  2 ;
SR  1.454720e-03  -6.167730e-03  0.000000e+00  0.000000e+00  0  5.563935e-02  0  0  2 ;
SR  2.530787e-03  3.622343e-03  0.000000e+00  0.000000e+00  0  -7.071869e-02  0  0  2 ;
SR  -1.289776e-03  -3.349264e-03  0.000000e+00  0.000000e+00  0  -1.167566e-01  0  0  2 ;
SR  9.287557e-05  -1.203443e-03  0.000000e+00  0.000000e+00  0  -1.412934e-01  0  0  2 ;
SR  -1.698824e-03  6.207546e-03  0.000000e+00  0.000000e+00  0  -4.034219e-02  0  0  2 ;
SR  3.299369e-03  -2.409030e-03  0.000000e+00  0.000000e+00  0  1.295793e-02  0  0  2 ;
SR  7.277577e-04  -4.567211e-03  0.000000e+00  0.000000e+00  0  1.096140e-01  0  0  2 ;
SR  2.996202e-03  2.132803e-03  0.000000e+00  0.000000e+00  0  -6.180811e-02  0  0  2 ;
SR  6.169990e-04  -2.036978e-03  0.000000e+00  0.000000e+00  0  1.354856e-01  0  0  2 ;
SR  5.939528e-04  -3.266379e-03  0.000000e+00  0.000000e+00  0  1.265945e-01  0  0  2 ;
SR  -2.560700e-03  -6.558904e-04  0.000000e+00  0.000000e+00  0  -9.681336e-02  0  0  2 ;
SR  -4.469585e-05  7.495641e-03  0.000000e+00  0.000000e+00  0  4.526333e-03  0  0  2 ;
SR  -2.145509e-03  4.922801e-03  0.000000e+00  0.000000e+00  0  -6.297572e-02  0  0  2 ;
SR  -1.654091e-04  -6.494969e-03  0.000000e+00  0.000000e+00  0  7.128681e-02  0  0  2 ;
SR  3.056339e-03  -2.876077e-03  0.000000e+00  0.000000e+00  0  4.305480e-02  0  0  2 ;
SR  -2.444495e-04  -6.904690e-03  0.000000e+00  0.000000e+00  0  -5.501023e-02  0  0  2 ;
SR  -2.365166e-03  5.518938e-03  0.000000e+00  0.000000e+00  0  -6.173870e-03  0  0  2 ;
SR  5.585674e-04  -7.117425e-03  0.000000e+00  0.000000e+00  0  -3.894110e-02  0  0  2 ;
SR  3.065401e-03  -2.880084e-03  0.000000e+00  0.000000e+00  0  4.186217e-02  0  0  2 ;
SR  -6.542977e-04  -3.078570e-03  0.000000e+00  0.000000e+00  0  1.278065e-01  0  0  2 ;
SR  2.991888e-03  3.481236e-03  0.000000e+00  0.000000e+00  0  3.322273e-02  0  0  2 ;
SR  2.768994e-04  -6.851030e-03  0.000000e+00  0.000000e+00  0  5.715676e-02  0  0  2 ;
SR  -7.826101e-04  -5.254914e-03  0.000000e+00  0.000000e+00  0  -9.702621e-02  0  0  2 ;
SR  -1.868872e-03  -6.161874e-03  0.000000e+00  0.000000e+00  0  2.859851e-02  0  0  2 ;
SR  1.882646e-03  5.329345e-03  0.000000e+00  0.000000e+00  0  -6.495377e-02  0  0  2 ;
SR  -3.467490e-03  -1.002111e-03  0.000000e+00  0.000000e+00  0  3.616517e-03  0  0  2 ;
SR  3.239399e-03  -1.633336e-04  0.000000e+00  0.000000e+00  0  5.413195e-02  0  0  2 ;
SR  -1.221241e-03  -5.667483e-03  0.000000e+00  0.000000e+00  0  -7.937233e-02  0  0  2 ;

SR  0.000000e+00  0.000000e+00  -1.096077e-03  6.327117e-05  0  1.359915e-01  0  0  2 ;
SR  0.000000e+00  0.000000e+00  -1.675346e-03  -6.547495e-03  0  1.339282e-02  0  0  2 ;
SR  0.000000e+00  0.000000e+00  -6.832267e-04  -1.825517e-04  0  -1.404019e-01  0  0  2 ;
SR  0.000000e+00  0.000000e+00  5.063675e-04  -5.872317e-03  0  -8.663517e-02  0  0  2 ;
SR  0.000000e+00  0.000000e+00  -1.823738e-03  3.630495e-03  0  1.006654e-01  0  0  2 ;
SR  0.000000e+00  0.000000e+00  1.087291e-03  5.300814e-03  0  9.101511e-02  0  0  2 ;
SR  0.000000e+00  0.000000e+00  -1.067058e-03  7.142890e-03  0  5.400112e-04  0  0  2 ;
SR  0.000000e+00  0.000000e+00  1.816442e-03  -6.333762e-03  0  -1.892904e-02  0  0  2 ;
SR  0.000000e+00  0.000000e+00  -3.077618e-03  2.901837e-03  0  3.976131e-02  0  0  2 ;
SR  0.000000e+00  0.000000e+00  1.440804e-03  -4.751994e-03  0  9.380304e-02  0  0  2 ;
SR  0.000000e+00  0.000000e+00  1.624999e-04  -7.444588e-03  0  1.605272e-02  0  0  2 ;
SR  0.000000e+00  0.000000e+00  -1.361737e-03  -6.704299e-03  0  -3.187843e-02  0  0  2 ;
SR  0.000000e+00  0.000000e+00  8.676178e-04  -6.622504e-03  0  -5.707560e-02  0  0  2 ;
SR  0.000000e+00  0.000000e+00  -2.900130e-03  -3.053534e-03  0  5.502437e-02  0  0  2 ;
SR  0.000000e+00  0.000000e+00  -5.379993e-04  -5.607661e-03  0  -9.250924e-02  0  0  2 ;
SR  0.000000e+00  0.000000e+00  -7.458753e-04  6.561275e-03  0  -6.229548e-02  0  0  2 ;
SR  0.000000e+00  0.000000e+00  6.338758e-04  7.340164e-03  0  -1.386079e-02  0  0  2 ;
SR  0.000000e+00  0.000000e+00  1.273425e-03  -6.835749e-04  0  -1.327454e-01  0  0  2 ;
SR  0.000000e+00  0.000000e+00  1.931545e-03  -4.645982e-03  0  -7.994917e-02  0  0  2 ;
SR  0.000000e+00  0.000000e+00  -9.409321e-04  2.801209e-03  0  -1.271361e-01  0  0  2 ;
SR  0.000000e+00  0.000000e+00  1.098956e-05  -7.277099e-04  0  1.425236e-01  0  0  2 ;
SR  0.000000e+00  0.000000e+00  2.730249e-04  -5.897201e-03  0  -8.776856e-02  0  0  2 ;
SR  0.000000e+00  0.000000e+00  2.152974e-03  1.468675e-05  0  -1.129016e-01  0  0  2 ;
SR  0.000000e+00  0.000000e+00  1.313572e-03  -6.676160e-04  0  1.321187e-01  0  0  2 ;
SR  0.000000e+00  0.000000e+00  4.786816e-04  4.468924e-03  0  -1.133227e-01  0  0  2 ;
SR  0.000000e+00  0.000000e+00  -3.066289e-03  3.918676e-04  0  -6.863884e-02  0  0  2 ;

UM ;    {sets map to unity} BP ;

{-------------------------BEAMLINE START-------------------------}



{ DL ABS(DL1) ; }
{ CB ; MS CCT12_R ABS(CCT1_ANG)+CCT12_ANG_MIN CCT12_APER CCT12_n 0 0 0 0; CB ;  }
{ CB ; MS CCT12_R ABS(CCT2_ANG)+CCT12_ANG_MIN CCT12_APER -CCT12_n 0 0 0 0; CB ;  }
{ DL ABS(GAP1); }
{ M5 ABS(QS1_LEN) QS1_Q QS1_S 0 0 0 QS1_APER; }
{ DL ABS(GAP2); }
{ M5 ABS(QS2_LEN) QS2_Q QS2_S 0 0 0 QS2_APER; }
{ DL ABS(GAP2); }
{ M5 ABS(QS1_LEN) QS1_Q QS1_S 0 0 0 QS1_APER; }
{ DL ABS(GAP1); }
{ CB ; MS CCT12_R ABS(CCT2_ANG)+CCT12_ANG_MIN CCT12_APER -CCT12_n 0 0 0 0; CB ;  }
{ CB ; MS CCT12_R ABS(CCT1_ANG)+CCT12_ANG_MIN CCT12_APER CCT12_n 0 0 0 0; CB ;  }
{ DL ABS(DL1) ; }




DL ABS(DL2) ;
MS CCT345_R ABS(CCT3_ANG)+CCT345_ANG_MIN CCT345_APER CCT345_n 0 0 0 0;
MS CCT345_R ABS(CCT4_ANG)+CCT345_ANG_MIN CCT345_APER -CCT345_n 0 0 0 0;
MS CCT345_R ABS(CCT5_ANG)+CCT345_ANG_MIN CCT345_APER CCT345_n 0 0 0 0;
DL ABS(GAP3);
M5 ABS(QS3_LEN) QS3_Q QS3_S 0 0 0 QS3_APER;
DL ABS(GAP3);
MS CCT345_R ABS(CCT5_ANG)+CCT345_ANG_MIN CCT345_APER CCT345_n 0 0 0 0;
MS CCT345_R ABS(CCT4_ANG)+CCT345_ANG_MIN CCT345_APER -CCT345_n 0 0 0 0;
MS CCT345_R ABS(CCT3_ANG)+CCT345_ANG_MIN CCT345_APER CCT345_n 0 0 0 0;

DL ABS(2.0);
PM 6 ;
DL ABS(DL2-2.0);





WRITE 6 ' SIGMA11' (SIGMA(3,3));
WRITE 6 'SQRT SIGMA11' SQRT(SIGMA(3,3));

{ PRAY 6 ; }


WRITE 6 BEAM_SPOT_X(RAY);
WRITE 6 BEAM_SPOT_Y(RAY);



{----------------------------------------------------------------}

EP ; {end the picture} PG -1 -2 ;


ENDPROCEDURE ;
RUN ; END ;

{动量分散 - 能量分散对照表 250MeV}
{ dp[-0.100]-dE[-0.179] }
{ dp[-0.090]-dE[-0.161] }
{ dp[-0.080]-dE[-0.143] }
{ dp[-0.070]-dE[-0.125] }
{ dp[-0.060]-dE[-0.107] }
{ dp[-0.050]-dE[-0.089] }
{ dp[-0.040]-dE[-0.072] }
{ dp[-0.030]-dE[-0.054] }
{ dp[-0.020]-dE[-0.036] }
{ dp[-0.010]-dE[-0.018] }
{ dp[+0.000]-dE[+0.000] }
{ dp[+0.010]-dE[+0.018] }
{ dp[+0.020]-dE[+0.036] }
{ dp[+0.030]-dE[+0.054] }
{ dp[+0.040]-dE[+0.072] }
{ dp[+0.050]-dE[+0.089] }
{ dp[+0.060]-dE[+0.107] }
{ dp[+0.070]-dE[+0.125] }
{ dp[+0.080]-dE[+0.143] }
{ dp[+0.090]-dE[+0.161] }
{ dp[+0.100]-dE[+0.179] }

 */
