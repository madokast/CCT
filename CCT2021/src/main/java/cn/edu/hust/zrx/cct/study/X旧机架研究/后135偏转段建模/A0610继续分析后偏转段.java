package cn.edu.hust.zrx.cct.study.X旧机架研究.后135偏转段建模;

import cn.edu.hust.zrx.cct.Logger;
import cn.edu.hust.zrx.cct.advanced.COSY;
import cn.edu.hust.zrx.cct.advanced.CosyArbitraryOrder;
import cn.edu.hust.zrx.cct.advanced.MathFunction;
import cn.edu.hust.zrx.cct.advanced.PolynomialFitter;
import cn.edu.hust.zrx.cct.base.BaseUtils;
import cn.edu.hust.zrx.cct.base.cct.Cct;
import cn.edu.hust.zrx.cct.base.cct.CctFactory;
import cn.edu.hust.zrx.cct.base.magnet.Elements;
import cn.edu.hust.zrx.cct.base.magnet.MagnetAble;
import cn.edu.hust.zrx.cct.base.line.Arcs;
import cn.edu.hust.zrx.cct.base.line.Line2;
import cn.edu.hust.zrx.cct.base.line.Trajectory;
import cn.edu.hust.zrx.cct.base.line.TrajectoryFactory;
import cn.edu.hust.zrx.cct.base.magnet.MovedMagnetable;
import cn.edu.hust.zrx.cct.base.magnet.qs.QsHardPlaneMagnet;
import cn.edu.hust.zrx.cct.base.particle.*;
import cn.edu.hust.zrx.cct.base.point.Point2;
import cn.edu.hust.zrx.cct.base.point.Point3;
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
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Description
 * A0610继续分析后偏转段
 * <p>
 * Data
 * 15:49
 *
 * @author zrx
 * @version 1.0
 */

@SuppressWarnings("all")
public class A0610继续分析后偏转段 {

    @run(1) // 研究只有二极场时，和COSY比较、切片比较
    public void 只有二极场() {
        agCct345IInner = 0;
        agCct345IOuter = 0;

        Trajectory trajectoryPart2 = getTrajectoryPart2();

        MagnetAble elementsOfAllPart2 = getElementsOfAllPart2();

        phase相椭圆画图(CosyMap第二段.ISOC.distance, false, 0.03, 10, false,
                1, elementsOfAllPart2, trajectoryPart2, 256, 5,
                List.of(BaseUtils.Content.BiContent.create("OPTICS", CosyMap第二段只有二极CCT.ISOC.getCosyMapArbitraryOrder())));
    }

    @run(2) // 如果我把四极场弄到最好
    public void 如果我把四极场弄到最好() {
        agCct345IInner = 4618.911905272398 / 45 * 18.14 / 0.05; // 9799
        agCct345IOuter = agCct345IInner;

        agCct345A1Inners = BaseUtils.ArrayUtils.repeat(-Math.pow(agCct345SmallRInner, 2) * Math.sqrt(3) * 20 * 0.05, 3);
        agCct345A1Outers = BaseUtils.ArrayUtils.repeat(Math.pow(agCct345SmallROuter, 2) * Math.sqrt(3) * 20 * 0.05, 3);

        Trajectory trajectoryPart2 = getTrajectoryPart2();

        MagnetAble elementsOfAllPart2 = getElementsOfAllPart2();

        phase相椭圆画图(CosyMap第二段.ISOC.distance, true, 0.00, 10, false,
                1, elementsOfAllPart2, trajectoryPart2, 256, 5,
                List.of(BaseUtils.Content.BiContent.create("OPTICS", CosyMap第二段.ISOC.map)));
    }


    @run(3)
    public void 四极场分布() {
        Trajectory trajectoryPart2 = getTrajectoryPart2();

        MagnetAble elementsOfAllPart2 = getElementsOfAllPart2();

//        List<Point2> g = elementsOfAllPart2.magnetGradientAlongTrajectoryFast(trajectoryPart2, MM, 10 * MM);

        List<Point2> g = elementsOfAllPart2.multiplePoleMagnetAlongTrajectoryBreak(trajectoryPart2, MM, 10 * MM, 2, 6).get(1);

        Plot2d.plot2(g);
        Plot2d.showThread();
    }

    @run(4)
    public void 不同电流大小下终点相椭圆() {
        agCct345IInner = 4618.911905272398 / 45 * 18.14 / 0.19; // 9799
        agCct345IOuter = agCct345IInner;

        agCct345A1Inners = BaseUtils.ArrayUtils.repeat(-Math.pow(agCct345SmallRInner, 2) * Math.sqrt(3) * 20 * 0.19, 3);
        agCct345A1Outers = BaseUtils.ArrayUtils.repeat(Math.pow(agCct345SmallROuter, 2) * Math.sqrt(3) * 20 * 0.19, 3);

        agCct345A2Inners = BaseUtils.ArrayUtils.repeat(-Math.pow(agCct345SmallRInner, 3) * Math.sqrt(3) * -6.25, 3);
        agCct345A2Outers = BaseUtils.ArrayUtils.repeat(Math.pow(agCct345SmallRInner, 3) * Math.sqrt(3) * -6.25, 3);

        Trajectory trajectoryPart2 = getTrajectoryPart2();


        List<BaseUtils.Content.BiContent<String, MagnetAble>> eleList = BaseUtils.Python.linspaceStream(0.9, 1.1, switcher.getSize() - 1)
                .mapToObj(k -> {
                    agCct345IInner = 4618.911905272398 / 45 * 18.14 / 0.19 * k; // 9799
                    agCct345IOuter = agCct345IInner;

                    MagnetAble elementsOfAllPart2 = getElementsOfAllPart2();

                    return BaseUtils.Content.BiContent.create("k=" + k, elementsOfAllPart2);
                }).collect(Collectors.toList());


        phase相椭圆画图(trajectoryPart2.getLength(), false, 0, 16, false, 1, eleList, trajectoryPart2, 512, 5,
                List.of(BaseUtils.Content.BiContent.create("OPTICS", CosyMap第二段.cosyMapIsoc())), Arrays.asList(switcher.getObjs()));

    }

    @run(5)
    public void 相椭圆() {
        Trajectory trajectoryPart2 = getTrajectoryPart2();


        agCct345IInner = 4618.911905272398 / 45 * 18.14 / 0.19 * 0.995; // 9799
        agCct345IOuter = agCct345IInner;

        agCct345A2Inners = BaseUtils.ArrayUtils.repeat(-Math.pow(agCct345SmallRInner, 3) * Math.sqrt(3) * -6.25, 3);
        agCct345A2Outers = BaseUtils.ArrayUtils.repeat(Math.pow(agCct345SmallRInner, 3) * Math.sqrt(3) * -6.25, 3);


        MagnetAble elementsOfAllPart2 = getElementsOfAllPart2();

        phase相椭圆画图(CosyMap第二段.ISOC.distance, true, 0.05, 10, false,
                1, elementsOfAllPart2, trajectoryPart2, 256, 5,
                List.of(BaseUtils.Content.BiContent.create("OPTICS", CosyMap第二段.ISOC.map)));

//        elementsOfAllPart2.sliceToCosyScript(Bp,60*MM,trajectoryPart2,20*MM,MM,0.1)
//                .forEach(System.out::println);
    }


    @run(-6)
    public void 纯二极CCT切片() {
        agCct345IInner = 0;
        agCct345IOuter = 0;

        Trajectory trajectoryPart2 = getTrajectoryPart2();

        MagnetAble elementsOfAllPart2 = getElementsOfAllPart2();

        List<String> list = elementsOfAllPart2.sliceToCosyScript(Bp, 60 * MM, trajectoryPart2, 10 * MM, MM, 0.1);

        list.forEach(System.out::println);
    }

    @run(7)
    public void 四极CCT中引入六极场之场分布() {
        Trajectory trajectoryPart2 = getTrajectoryPart2();

        String[] le = BaseUtils.Python.linspaceStream(-10, 10, 5)
                .mapToObj(k -> {
                    agCct345A2Inners = BaseUtils.ArrayUtils.repeat(-Math.pow(agCct345SmallRInner, 3) * Math.sqrt(3) * k, 3);
                    agCct345A2Outers = BaseUtils.ArrayUtils.repeat(Math.pow(agCct345SmallRInner, 3) * Math.sqrt(3) * k, 3);

                    MagnetAble elementsOfAllPart2 = getElementsOfAllPart2();

                    return BaseUtils.Content.BiContent.create(k, elementsOfAllPart2);
                }).map(bi -> {
                    MagnetAble t2 = bi.getT2();

                    List<Point2> L = t2.multiplePoleMagnetAlongTrajectoryBreak(
                            trajectoryPart2, 50 * MM, 15 * MM, 3, 12
                    ).get(2);

                    Plot2d.plot2(L, switcher.getAndSwitch());

                    return bi.getT1();
                }).map(k -> "k=" + k).collect(Collectors.toList()).toArray(String[]::new);


        Plot2d.info("s/m", "B2/(Tm-2)", "六极场", 18);

        Plot2d.legend(18, le);

        Plot2d.showThread();
    }

    @run(8)
    public void 四极CCT中引入六极场之相椭圆() {
        Trajectory trajectoryPart2 = getTrajectoryPart2();

        agCct345IInner = 4618.911905272398 / 45 * 18.14 / 0.19 * 1.005; // 9799
        agCct345IOuter = agCct345IInner;

        List<BaseUtils.Content.BiContent<String, MagnetAble>> mList = BaseUtils.Python.linspaceStream(-15, 0, switcher.getSize() - 1)
//        List<BaseUtils.Content.BiContent<String, CctFactory.MagnetAble>> mList = BaseUtils.Python.linspaceStream(-10, -5, 5)
                .mapToObj(k -> {
                    agCct345A2Inners = BaseUtils.ArrayUtils.repeat(-Math.pow(agCct345SmallRInner, 3) * Math.sqrt(3) * k, 3);
                    agCct345A2Outers = BaseUtils.ArrayUtils.repeat(Math.pow(agCct345SmallRInner, 3) * Math.sqrt(3) * k, 3);

                    MagnetAble elementsOfAllPart2 = getElementsOfAllPart2();

                    return BaseUtils.Content.BiContent.create(k, elementsOfAllPart2);
                }).map(bi -> {
                    Double t1 = bi.getT1();
                    MagnetAble t2 = bi.getT2();

                    return BaseUtils.Content.BiContent.create("k=" + t1, t2);
                }).collect(Collectors.toList());


        phase相椭圆画图(
                trajectoryPart2.getLength(),
                true, 0, 8, false, 1,
                mList, trajectoryPart2, 128, 5,
                List.of(BaseUtils.Content.BiContent.create("OPTICS", CosyMap第二段.ISOC.map)),
                List.of(switcher.getObjs())
        );
    }

    @run(9)
    public void CCT345二极电流单粒子跟踪() {
        agCct345IInner = 4618.911905272398 / 45 * 18.14 / 0.19 * 1.005; // 9799
        agCct345IOuter = agCct345IInner;

        agCct345A2Inners = BaseUtils.ArrayUtils.repeat(-Math.pow(agCct345SmallRInner, 3) * Math.sqrt(3) * -6.25, 3);
        agCct345A2Outers = BaseUtils.ArrayUtils.repeat(Math.pow(agCct345SmallRInner, 3) * Math.sqrt(3) * -6.25, 3);

        Trajectory trajectory = CosyMapOthers.CCT345ONLY前后偏移DL2.trajectory;

        double distance = CosyMapOthers.CCT345ONLY前后偏移DL2.distance;

        CosyArbitraryOrder.CosyMapArbitraryOrder map = CosyMapOthers.CCT345ONLY前后偏移DL2.map;
//        phase相椭圆画图(distance, false, 0.05, 10, false,
//                1, cct345, trajectory, 256, 5,
//                List.of(BaseUtils.Content.BiContent.create("OPTICS", map)));

        String[] le = BaseUtils.Python.linspaceStream(0.90, 0.96, switcher.getSize())
                .sequential()
                .mapToObj(k -> {
                    dipoleCct345IInner = -10.017 * kA * k;
                    dipoleCct345IOuter = dipoleCct345IInner;

                    Cct cct345 = getCct345_1();

                    return BaseUtils.Content.BiContent.create(k, cct345);
                })
                .collect(Collectors.toList())
                .stream()
                .parallel()
                .map(bi -> {
                    Cct cct = bi.getT2();
                    Double k = bi.getT1();
                    List<Point2> x = trackingIdealParticle(trajectory, distance, cct, true);
                    return BaseUtils.Content.BiContent.create(k, Point2.convert(x, 1, 1 / MM));
                })
                .collect(Collectors.toList())
                .stream()
                .sequential()
                .sorted(Comparator.comparingDouble(BaseUtils.Content.BiContent::getT1))
                .peek(bi -> {
                    List<Point2> x = bi.getT2();
                    Plot2d.plot2(x, switcher.getAndSwitch());
                })
                .map(BaseUtils.Content.BiContent::getT1)
                .map(k -> "k=" + k)
                .collect(Collectors.toList())
                .toArray(String[]::new);


        Plot2d.info("x/m", "x/mm", "二极CCT电流", 18);

        Plot2d.legend(18, le);

        CosyMap第二段.drawElement(60, 60);


        Plot2d.showThread();
    }

    @run(10)
    public void CCT345相椭圆() {

        dipoleCct345IInner = -10.017 * kA * 0.925;
        dipoleCct345IOuter = dipoleCct345IInner;

        agCct345IInner = 4618.911905272398 / 45 * 18.14 / 0.19 * 1.005; // 9799
        agCct345IOuter = agCct345IInner;

        agCct345A2Inners = BaseUtils.ArrayUtils.repeat(-Math.pow(agCct345SmallRInner, 3) * Math.sqrt(3) * -6.25, 3);
        agCct345A2Outers = BaseUtils.ArrayUtils.repeat(Math.pow(agCct345SmallRInner, 3) * Math.sqrt(3) * -6.25, 3);

        Trajectory trajectory = CosyMapOthers.CCT345ONLY前后偏移DL2.trajectory;

        double distance = CosyMapOthers.CCT345ONLY前后偏移DL2.distance;

        CosyArbitraryOrder.CosyMapArbitraryOrder map = CosyMapOthers.CCT345ONLY前后偏移DL2.map;

        Cct cct345 = getCct345_1();


//        List<Point2> x = trackingIdealParticle(trajectory, distance, cct345, true);
//        Plot2d.plot2(Point2.convert(x, 1, 1 / MM));
//        Plot2d.showThread();


        phase相椭圆画图(distance, true, 0.00, 10, false,
                1, cct345, trajectory, 256, 5,
                List.of(BaseUtils.Content.BiContent.create("OPTICS", map)));


    }

    @run(11)
    public void 四极场不均匀() {
        //Point3 rightPoint = rightHandSideLine2.pointAt(distance).toPoint3();
        //                    Point3 leftPoint = leftHandSideLine2.pointAt(distance).toPoint3();
        //
        //                    List<Double> bzList = BaseUtils.Python.linspaceStream(rightPoint, leftPoint, dotNumber)
        //                            .mapToDouble(p -> magnetAt(p).z)
        //                            .boxed()
        //                            .collect(Collectors.toList());
        //                    List<Point2> fitted = Point2.create(xList, bzList);


        Trajectory trajectoryPart2 = getTrajectoryPart2();

        MagnetAble elementsOfAllPart2 = getElementsOfAllPart2();

        PolynomialFitter fitter = PolynomialFitter.build(5);

        double good = 30 * MM;
        int number = 12;
        List<Double> xList = BaseUtils.Python.linspaceStream(-good, good, number)
                .boxed().collect(Collectors.toList());

        Point3 leftHandSidePoint = trajectoryPart2.leftHandSidePoint(DL2 + CCT345_LENGTH / 2, good).toPoint3(20 * MM);
        Point3 rightHandSidePoint = trajectoryPart2.rightHandSidePoint(DL2 + CCT345_LENGTH / 2, good).toPoint3(20 * MM);

        List<Double> bzList = BaseUtils.Python.linspaceStream(rightHandSidePoint, leftHandSidePoint, number)
                .mapToDouble(p -> elementsOfAllPart2.magnetAt(p).z)
                .boxed()
                .collect(Collectors.toList());
        List<Point2> fitted = Point2.create(xList, bzList);

        double[] fit = fitter.fit(fitted);
        Function<Double, Double> polynomialFunction = MathFunction.createPolynomialFunction(fit);

        Logger.getLogger().info("fit = " + Arrays.toString(fit));

        Plot2d.plot2(fitted, Plot2d.RED_POINT);

        Plot2d.plot2(polynomialFunction, Point2.create(-good, good), 20, Plot2d.BLACK_LINE);

        Plot2d.info("x/mm","B/T","横截面磁场分布",18);

        Plot2d.showThread();

    }


    @run(20)
    public void CCT345_2二极电流单粒子跟踪() {
        agCct345IInner = 4618.911905272398 / 45 * 18.14 / 0.19; // 9799
        agCct345IOuter = agCct345IInner;

//        agCct345A2Inners = BaseUtils.ArrayUtils.repeat(-Math.pow(agCct345SmallRInner, 3) * Math.sqrt(3) * -6.25, 3);
//        agCct345A2Outers = BaseUtils.ArrayUtils.repeat(Math.pow(agCct345SmallRInner, 3) * Math.sqrt(3) * -6.25, 3);


        final double beforeWalked = DL2 + CCT345_LENGTH;
        final double distance = GAP3 + QS3_LEN + GAP3 + CCT345_LENGTH + DL2;

        Line2 trajectory = new Line2() {
            private Line2 origin = getTrajectoryPart2();

            private double walked = beforeWalked;

            @Override
            public double getLength() {
                return origin.getLength() - walked;
            }

            @Override
            public Point2 pointAt(double s) {
                return origin.pointAt(s + walked);
            }

            @Override
            public Vector2 directAt(double s) {
                return origin.directAt(s + walked);
            }
        };

        String[] le = BaseUtils.Python.linspaceStream(1.0, 1.01, switcher.getSize())
                .sequential()
                .mapToObj(k -> {
                    dipoleCct345IInner = -10.017 * kA * k;
                    dipoleCct345IOuter = dipoleCct345IInner;

                    Cct cct345 = getCct345_2();

                    return BaseUtils.Content.BiContent.create(k, cct345);
                })
                .collect(Collectors.toList())
                .stream()
                .parallel()
                .map(bi -> {
                    Cct cct = bi.getT2();
                    Double k = bi.getT1();
                    List<Point2> x = trackingIdealParticle(trajectory, distance, cct, true);
                    x = Point2.convert(x, sx -> sx + beforeWalked, sy -> sy / MM);
                    return BaseUtils.Content.BiContent.create(k, x);

                })
                .collect(Collectors.toList())
                .stream()
                .sequential()
                .sorted(Comparator.comparingDouble(BaseUtils.Content.BiContent::getT1))
                .peek(bi -> {
                    List<Point2> x = bi.getT2();
                    Plot2d.plot2(x, switcher.getAndSwitch());
                })
                .map(BaseUtils.Content.BiContent::getT1)
                .map(k -> "k=" + k)
                .collect(Collectors.toList())
                .toArray(String[]::new);


        Plot2d.info("x/m", "x/mm", "二极CCT电流", 18);

        Plot2d.legend(18, le);

        CosyMap第二段.drawElement(60, 60);


        Plot2d.showThread();
    }

    @run(-100)
    public void 后面的移动393MM二极电流() {
        dipoleCct345IInner = -10.017 * kA * 0.9225;
        dipoleCct345IOuter = dipoleCct345IInner;

        agCct345IInner = 4618.911905272398 / 45 * 18.14 / 0.19 * 1.005; // 9799
        agCct345IOuter = agCct345IInner;

        agCct345A2Inners = BaseUtils.ArrayUtils.repeat(-Math.pow(agCct345SmallRInner, 3) * Math.sqrt(3) * -6.25, 3);
        agCct345A2Outers = BaseUtils.ArrayUtils.repeat(Math.pow(agCct345SmallRInner, 3) * Math.sqrt(3) * -6.25, 3);


        Trajectory trajectoryPart2 = getTrajectoryPart2();
        double length = trajectoryPart2.getLength();

        Cct cct345_1 = getCct345_1();
        QsHardPlaneMagnet QS3 = getQs3();


        String[] le = BaseUtils.Python.linspaceStream(1.0, 1.01, switcher.getSize())
                .sequential()
                .mapToObj(k -> {
                    dipoleCct345IInner = -10.017 * kA * k;
                    dipoleCct345IOuter = dipoleCct345IInner;
                    Cct cct345_2 = getCct345_2();


                    Elements elements = Elements.empty();

                    elements.addElement(cct345_1);
                    elements.addElement(
                            MovedMagnetable.create(
                                    cct345_2,
                                    trajectoryPart2.directAt(DL2 + CCT345_LENGTH + GAP3 + QS3_LEN + GAP3).rotateSelf(
                                            BaseUtils.Converter.angleToRadian(90)
                                    ).toVector3(),
                                    39.3 * MM
                            )
                    );
                    elements.addElement(
                            MovedMagnetable.create(
                                    QS3,
                                    trajectoryPart2.directAt(DL2 + CCT345_LENGTH + GAP3).rotateSelf(
                                            BaseUtils.Converter.angleToRadian(90)
                                    ).toVector3(),
                                    39.3 * MM
                            )
                    );

                    return BaseUtils.Content.BiContent.create(k, elements);
                })
                .collect(Collectors.toList())
                .stream()
                .parallel()
                .map(bi -> {
                    MagnetAble m = bi.getT2();
                    Double k = bi.getT1();
                    List<Point2> x = trackingIdealParticle(trajectoryPart2, length, m, true);
                    x = Point2.convert(x, 1, 1 / MM);
                    return BaseUtils.Content.BiContent.create(k, x);

                })
                .collect(Collectors.toList())
                .stream()
                .sequential()
                .sorted(Comparator.comparingDouble(BaseUtils.Content.BiContent::getT1))
                .peek(bi -> {
                    List<Point2> x = bi.getT2();
                    Plot2d.plot2(x, switcher.getAndSwitch());
                })
                .map(BaseUtils.Content.BiContent::getT1)
                .map(k -> "k=" + k)
                .collect(Collectors.toList())
                .toArray(String[]::new);


        Plot2d.info("x/m", "x/mm", "二极CCT电流", 18);

        Plot2d.legend(18, le);

        //CosyMap第二段.drawElement(60, 60);


        Plot2d.showThread();
    }


    @run(101)
    public void 后面的移动393MM相椭圆() {
        dipoleCct345IInner = -10.017 * kA * 0.9225;
        dipoleCct345IOuter = dipoleCct345IInner;

        agCct345IInner = 4618.911905272398 / 45 * 18.14 / 0.19 * 1.005; // 9799
        agCct345IOuter = agCct345IInner;

        agCct345A2Inners = BaseUtils.ArrayUtils.repeat(-Math.pow(agCct345SmallRInner, 3) * Math.sqrt(3) * -6.25, 3);
        agCct345A2Outers = BaseUtils.ArrayUtils.repeat(Math.pow(agCct345SmallRInner, 3) * Math.sqrt(3) * -6.25, 3);


        Trajectory trajectoryPart2 = getTrajectoryPart2();
        double length = trajectoryPart2.getLength();

        Cct cct345_1 = getCct345_1();
        QsHardPlaneMagnet QS3 = getQs3();


        ///////////////

        dipoleCct345IInner = -10.017 * kA * 1.005;
        dipoleCct345IOuter = dipoleCct345IInner;
        Cct cct345_2 = getCct345_2();


        Elements elements = Elements.empty();

        elements.addElement(cct345_1);
        elements.addElement(
                MovedMagnetable.create(
                        cct345_2,
                        trajectoryPart2.directAt(DL2 + CCT345_LENGTH + GAP3 + QS3_LEN + GAP3).rotateSelf(
                                BaseUtils.Converter.angleToRadian(90)
                        ).toVector3(),
                        39.3 * MM
                )
        );
        elements.addElement(
                MovedMagnetable.create(
                        QS3,
                        trajectoryPart2.directAt(DL2 + CCT345_LENGTH + GAP3).rotateSelf(
                                BaseUtils.Converter.angleToRadian(90)
                        ).toVector3(),
                        39.3 * MM
                )
        );

        phase相椭圆画图(
                trajectoryPart2.getLength(),
                true,0.05,8,true,
                0.001,elements,trajectoryPart2,128,5,
                List.of(BaseUtils.Content.BiContent.create("OPTICS",CosyMap第二段.ISOC.map))
        );


//        List<Point2> list = track色散(trajectoryPart2, elements);
//        Plot2d.plot2(list); //单位 mm/%
//        Plot2d.showThread();

//        List<Point2> x = trackingIdealParticle(trajectoryPart2, trajectoryPart2.getLength(), elements, true);
//        Plot2d.plot2(Point2.convert(x,1,1/MM));
//        Plot2d.info("s/m","x/mm","",18);
//        Plot2d.showThread();

    }


    @run(102)
    public void 束流(){

        double convert动量分散_to_能量分散 = Protons.convert动量分散_TO_能量分散(0.08, 250);
        Logger.getLogger().info("convert动量分散_to_能量分散 = " + convert动量分散_to_能量分散);

        Trajectory trajectoryPart2 = getTrajectoryPart2();
        MagnetAble elementsOfAllPart2 = getElementsOfAllPart2();

        List<List<Point2>> x = track多粒子(32, 0.08, trajectoryPart2, trajectoryPart2.getLength(), elementsOfAllPart2, false);

        x.forEach(xx->Plot2d.plot2(xx,Plot2d.BLACK_LINE));

        Plot2d.info("s/m","x/mm","",18);

        Plot2d.showThread();

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
            ParticleRunner.run(ip, magnetAble, distance, MM);
        }

        List<PhaseSpaceParticle> ppEnd = PhaseSpaceParticles.phaseSpaceParticlesFromRunningParticles(ip, ip.computeNaturalCoordinateSystem(), ps);

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

    private List<List<Point2>> track多粒子(int number, double delta, Trajectory trajectory, double distance,
                                        MagnetAble magnetAble, boolean xPlane) {
        RunningParticle ip = ParticleFactory.createIdealProtonAtTrajectory250MeV(trajectory);

        List<PhaseSpaceParticle> pp = PhaseSpaceParticles.phaseSpaceParticlesAlongPositiveEllipseInPlane(
                xPlane, 3.5 * MM, 7.5 * MM, delta, number);
        List<RunningParticle> ps = ParticleFactory.createParticlesFromPhaseSpaceParticle(ip, ip.computeNaturalCoordinateSystem(), pp);

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

    // ------------ MAP ---------------------------

    private static enum CosyMap第一段 {
        //        COUPLING_POINT("COUPLING_POINT",0.0,),
        //        BEFORE_CCT12_1,
        //        AFTER_CCT12_1,
        //        AFTER_QS1_1,
        //        AFTER_QS2,
        //        AFTER_QS1_2,
        //        BEFORE_CCT12_2,
        //        AFTER_CCT12_2,
        //        END;

        //                0., // start
        //                DL1, // BEFORE CCT1
        //                DL1 + CCT_LENGTH, // AFTER CCT1
        //                DL1 + CCT_LENGTH + GAP1 + QS1_LEN, // QS1
        //                DL1 + CCT_LENGTH + GAP1 + QS1_LEN + GAP2 + QS2_LEN, // QS2
        //                DL1 + CCT_LENGTH + GAP1 + QS1_LEN + GAP2 + QS2_LEN + GAP2 + QS1_LEN,  // 2ND QS1
        //                DL1 + CCT_LENGTH + BETWEEN_CCT225, // BEFORE CCT2
        //                DL1 + CCT_LENGTH + BETWEEN_CCT225 + CCT_LENGTH, // AFTER CCT2
        //                DL1 + CCT_LENGTH + BETWEEN_CCT225 + CCT_LENGTH + DL1 // END

        COUPLING_POINT("COUPLING_POINT", 0.0, CosyArbitraryOrder.readMap(
                "   1.000000     0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00 100000\n" +
                        "  0.0000000E+00  1.000000     0.0000000E+00 0.0000000E+00 0.0000000E+00 010000\n" +
                        "  0.0000000E+00 0.0000000E+00  1.000000     0.0000000E+00 0.0000000E+00 001000\n" +
                        "  0.0000000E+00 0.0000000E+00 0.0000000E+00  1.000000     0.0000000E+00 000100\n" +
                        "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00  1.000000     000010"
        )),
        BEFORE_CCT12_1("BEFORE_CCT12_1", DL1, CosyArbitraryOrder.readMap(
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
        )),
        AFTER_CCT12_1("AFTER_CCT12_1", DL1 + CCT12_LENGTH, CosyArbitraryOrder.readMap(
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
        )),
        AFTER_QS1_1("AFTER_QS1_1", DL1 + CCT12_LENGTH + GAP1 + QS1_LEN, CosyArbitraryOrder.readMap(
                "  0.2287698     -2.073383     0.0000000E+00 0.0000000E+00 0.1680138     100000\n" +
                        "  0.8765549     -3.573173     0.0000000E+00 0.0000000E+00 0.2290133     010000\n" +
                        "  0.0000000E+00 0.0000000E+00 0.7329625     0.8727016     0.0000000E+00 001000\n" +
                        "  0.0000000E+00 0.0000000E+00  1.382212      3.010057     0.0000000E+00 000100\n" +
                        "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00  1.000000     000010\n" +
                        " -0.9488203E-01 0.1255101     0.0000000E+00 0.0000000E+00 0.3519586     000001\n" +
                        "   2.276214     -2.470118     0.0000000E+00 0.0000000E+00-0.2326305     200000\n" +
                        "   5.464847      2.646777     0.0000000E+00 0.0000000E+00-0.2828416     110000\n" +
                        "   3.861137      12.75443     0.0000000E+00 0.0000000E+00-0.5879686     020000\n" +
                        "  0.0000000E+00 0.0000000E+00-0.5911760     -7.562095     0.0000000E+00 101000\n" +
                        "  0.0000000E+00 0.0000000E+00 -1.816050     -24.74386     0.0000000E+00 011000\n" +
                        " -0.3099023     -8.177915     0.0000000E+00 0.0000000E+00-0.5743553     002000\n" +
                        "  0.0000000E+00 0.0000000E+00 -1.001912     -13.02566     0.0000000E+00 100100\n" +
                        "  0.0000000E+00 0.0000000E+00 -3.208687     -43.02414     0.0000000E+00 010100\n" +
                        "  -1.028648     -28.58842     0.0000000E+00 0.0000000E+00 -1.424952     001100\n" +
                        "  0.4675180     -2.652094     0.0000000E+00 0.0000000E+00-0.6912452E-01 100001\n" +
                        " -0.6701876E-01 -2.252169     0.0000000E+00 0.0000000E+00-0.1442374     010001\n" +
                        "  0.0000000E+00 0.0000000E+00 0.7130596      4.639616     0.0000000E+00 001001\n" +
                        " -0.8715524     -24.85969     0.0000000E+00 0.0000000E+00 -1.357984     000200\n" +
                        "  0.0000000E+00 0.0000000E+00 0.2885367      6.164560     0.0000000E+00 000101\n" +
                        "  0.7296110E-01-0.8845191E-01 0.0000000E+00 0.0000000E+00-0.3006609     000002\n" +
                        "  -1.598379      34.10319     0.0000000E+00 0.0000000E+00  1.048268     300000\n" +
                        " -0.8542174      154.1739     0.0000000E+00 0.0000000E+00  2.876592     210000\n" +
                        "   2.430592      218.1737     0.0000000E+00 0.0000000E+00  3.544300     120000\n" +
                        "   2.118867      98.76538     0.0000000E+00 0.0000000E+00  2.111188     030000\n" +
                        "  0.0000000E+00 0.0000000E+00 -4.913666     -62.01456     0.0000000E+00 201000\n" +
                        "  0.0000000E+00 0.0000000E+00 -10.20090     -128.3190     0.0000000E+00 111000\n" +
                        "  0.0000000E+00 0.0000000E+00 -6.560570     -70.01883     0.0000000E+00 021000\n" +
                        "  -2.507561      12.21012     0.0000000E+00 0.0000000E+00 0.8577292     102000\n" +
                        "  -1.464508      16.98014     0.0000000E+00 0.0000000E+00 0.8436001     012000\n" +
                        "  0.0000000E+00 0.0000000E+00 -4.721632     -23.60051     0.0000000E+00 003000\n" +
                        "  0.0000000E+00 0.0000000E+00 -8.386053     -106.9213     0.0000000E+00 200100\n" +
                        "  0.0000000E+00 0.0000000E+00 -17.42783     -220.8880     0.0000000E+00 110100\n" +
                        "  0.0000000E+00 0.0000000E+00 -9.810989     -115.0591     0.0000000E+00 020100\n" +
                        "  -7.742960      40.67373     0.0000000E+00 0.0000000E+00  2.560204     101100\n" +
                        "  -4.566565      66.21371     0.0000000E+00 0.0000000E+00  3.227180     011100\n" +
                        "  0.0000000E+00 0.0000000E+00 -18.11326     -98.13314     0.0000000E+00 002100\n" +
                        "  -1.798181      5.428296     0.0000000E+00 0.0000000E+00 0.5022149     200001\n" +
                        "  -6.389821      20.84079     0.0000000E+00 0.0000000E+00 0.6922144     110001\n" +
                        "  -6.411342      3.817861     0.0000000E+00 0.0000000E+00 0.8446485     020001\n" +
                        "  0.0000000E+00 0.0000000E+00 -2.017529     -23.33485     0.0000000E+00 101001\n" +
                        "  0.0000000E+00 0.0000000E+00 -1.014813     -20.39021     0.0000000E+00 011001\n" +
                        "  -1.185491     -8.274448     0.0000000E+00 0.0000000E+00  1.073926     002001\n" +
                        "  -6.221048      34.22983     0.0000000E+00 0.0000000E+00  1.939754     100200\n" +
                        "  -3.538602      60.32777     0.0000000E+00 0.0000000E+00  3.333992     010200\n" +
                        "  0.0000000E+00 0.0000000E+00 -24.04456     -140.8746     0.0000000E+00 001200\n" +
                        "  0.0000000E+00 0.0000000E+00 -2.639685     -30.53706     0.0000000E+00 100101\n" +
                        "  0.0000000E+00 0.0000000E+00 0.3630352     -6.773592     0.0000000E+00 010101\n" +
                        "  -2.830826     -12.04437     0.0000000E+00 0.0000000E+00  3.221235     001101\n" +
                        " -0.4572034     0.8681693     0.0000000E+00 0.0000000E+00 0.1172493     100002\n" +
                        " -0.1762171      3.161984     0.0000000E+00 0.0000000E+00 0.1476931     010002\n" +
                        "  0.0000000E+00 0.0000000E+00-0.8120888     -2.860872     0.0000000E+00 001002\n" +
                        "  0.0000000E+00 0.0000000E+00 -10.37387     -68.58657     0.0000000E+00 000300\n" +
                        "  -1.457818      4.519118     0.0000000E+00 0.0000000E+00  2.948832     000201\n" +
                        "  0.0000000E+00 0.0000000E+00 -1.198982     -7.658309     0.0000000E+00 000102\n" +
                        " -0.7205092E-01 0.2381383E-02 0.0000000E+00 0.0000000E+00 0.2656699     000003\n" +
                        "   11.54250      14.08523     0.0000000E+00 0.0000000E+00 -1.199388     400000\n" +
                        "   39.99903      24.38325     0.0000000E+00 0.0000000E+00  2.716212     310000\n" +
                        "   58.31973     -4.862614     0.0000000E+00 0.0000000E+00  15.86034     220000\n" +
                        "   47.66771     -43.19860     0.0000000E+00 0.0000000E+00  16.66714     130000\n" +
                        "   20.00695     -14.27427     0.0000000E+00 0.0000000E+00  3.105314     040000\n" +
                        "  0.0000000E+00 0.0000000E+00  8.910154      124.3176     0.0000000E+00 301000\n" +
                        "  0.0000000E+00 0.0000000E+00  17.76312      373.3005     0.0000000E+00 211000\n" +
                        "  0.0000000E+00 0.0000000E+00  10.93054      425.9141     0.0000000E+00 121000\n" +
                        "  0.0000000E+00 0.0000000E+00  2.946668      193.6446     0.0000000E+00 031000\n" +
                        "   8.244674     -28.16790     0.0000000E+00 0.0000000E+00 -5.422215     202000\n" +
                        "   9.468514     -102.4541     0.0000000E+00 0.0000000E+00 -10.03637     112000\n" +
                        "   5.029265     -39.74298     0.0000000E+00 0.0000000E+00 -8.454286     022000\n" +
                        "  0.0000000E+00 0.0000000E+00  23.29700      176.7991     0.0000000E+00 103000\n" +
                        "  0.0000000E+00 0.0000000E+00  29.66975      240.5368     0.0000000E+00 013000\n" +
                        "   8.274773      62.62594     0.0000000E+00 0.0000000E+00 -5.880030     004000\n" +
                        "  0.0000000E+00 0.0000000E+00  15.12900      213.5888     0.0000000E+00 300100\n" +
                        "  0.0000000E+00 0.0000000E+00  33.58589      668.0137     0.0000000E+00 210100\n" +
                        "  0.0000000E+00 0.0000000E+00  22.24446      776.3315     0.0000000E+00 120100\n" +
                        "  0.0000000E+00 0.0000000E+00  2.323172      323.3863     0.0000000E+00 030100\n" +
                        "   26.58317     -56.16884     0.0000000E+00 0.0000000E+00 -12.63736     201100\n" +
                        "   33.75643     -264.7839     0.0000000E+00 0.0000000E+00 -22.09109     111100\n" +
                        "   18.00179     -137.1351     0.0000000E+00 0.0000000E+00 -20.74159     021100\n" +
                        "  0.0000000E+00 0.0000000E+00  96.34046      757.9793     0.0000000E+00 102100\n" +
                        "  0.0000000E+00 0.0000000E+00  117.8259      978.3690     0.0000000E+00 012100\n" +
                        "   44.28668      337.1883     0.0000000E+00 0.0000000E+00 -30.83310     003100\n" +
                        "   5.548299      6.290963     0.0000000E+00 0.0000000E+00 -2.470858     300001\n" +
                        "   8.405173     -102.0208     0.0000000E+00 0.0000000E+00 -6.752402     210001\n" +
                        "  -4.252158     -302.4817     0.0000000E+00 0.0000000E+00 -6.549817     120001\n" +
                        "  -8.115702     -212.9007     0.0000000E+00 0.0000000E+00 -3.233426     030001\n" +
                        "  0.0000000E+00 0.0000000E+00  8.481616      44.27615     0.0000000E+00 201001\n" +
                        "  0.0000000E+00 0.0000000E+00  22.87869      175.5931     0.0000000E+00 111001\n" +
                        "  0.0000000E+00 0.0000000E+00  17.79471      144.1623     0.0000000E+00 021001\n" +
                        "   7.005758      3.473812     0.0000000E+00 0.0000000E+00 -2.880094     102001\n" +
                        "   4.592105     -16.99511     0.0000000E+00 0.0000000E+00-0.8890328     012001\n" +
                        "  0.0000000E+00 0.0000000E+00  10.69706      46.97104     0.0000000E+00 003001\n" +
                        "   21.79830     -22.68830     0.0000000E+00 0.0000000E+00 -6.172502     200200\n" +
                        "   34.98578     -174.4921     0.0000000E+00 0.0000000E+00 -9.901457     110200\n" +
                        "   22.75409     -118.7821     0.0000000E+00 0.0000000E+00 -13.04671     020200\n" +
                        "  0.0000000E+00 0.0000000E+00  134.3361      1109.021     0.0000000E+00 101200\n" +
                        "  0.0000000E+00 0.0000000E+00  154.6751      1364.576     0.0000000E+00 011200\n" +
                        "   89.07109      686.0456     0.0000000E+00 0.0000000E+00 -62.50651     002200\n" +
                        "  0.0000000E+00 0.0000000E+00  18.68164      137.4031     0.0000000E+00 200101\n" +
                        "  0.0000000E+00 0.0000000E+00  49.21229      433.4294     0.0000000E+00 110101\n" +
                        "  0.0000000E+00 0.0000000E+00  35.52182      319.1771     0.0000000E+00 020101\n" +
                        "   24.28516      4.990655     0.0000000E+00 0.0000000E+00 -8.121221     101101\n" +
                        "   15.86818     -72.16127     0.0000000E+00 0.0000000E+00 -2.910084     011101\n" +
                        "  0.0000000E+00 0.0000000E+00  52.18073      251.8781     0.0000000E+00 002101\n" +
                        "   2.623589      2.439214     0.0000000E+00 0.0000000E+00 -1.021754     200002\n" +
                        "   8.299363     -14.75871     0.0000000E+00 0.0000000E+00 -1.823562     110002\n" +
                        "   8.849960     -10.29464     0.0000000E+00 0.0000000E+00 -1.389031     020002\n" +
                        "  0.0000000E+00 0.0000000E+00  2.848359      14.93727     0.0000000E+00 101002\n" +
                        "  0.0000000E+00 0.0000000E+00  3.606114      33.99396     0.0000000E+00 011002\n" +
                        "   2.102986      6.386930     0.0000000E+00 0.0000000E+00 -2.033773     002002\n" +
                        "  0.0000000E+00 0.0000000E+00  62.52800      547.6168     0.0000000E+00 100300\n" +
                        "  0.0000000E+00 0.0000000E+00  63.78223      630.0638     0.0000000E+00 010300\n" +
                        "   79.22811      618.6832     0.0000000E+00 0.0000000E+00 -58.43330     001300\n" +
                        "   21.12481     -4.408445     0.0000000E+00 0.0000000E+00 -5.648789     100201\n" +
                        "   13.71540     -83.25011     0.0000000E+00 0.0000000E+00 -3.688625     010201\n" +
                        "  0.0000000E+00 0.0000000E+00  83.99478      434.8794     0.0000000E+00 001201\n" +
                        "  0.0000000E+00 0.0000000E+00  5.689407      39.69850     0.0000000E+00 100102\n" +
                        "  0.0000000E+00 0.0000000E+00  4.782152      50.74248     0.0000000E+00 010102\n" +
                        "   7.504420      25.92853     0.0000000E+00 0.0000000E+00 -6.825131     001102\n" +
                        "  0.5760844     0.1875539     0.0000000E+00 0.0000000E+00-0.1922770     100003\n" +
                        "  0.3835655     -2.934176     0.0000000E+00 0.0000000E+00-0.2130338     010003\n" +
                        "  0.0000000E+00 0.0000000E+00 0.8232190      1.573799     0.0000000E+00 001003\n" +
                        "   25.85717      201.6396     0.0000000E+00 0.0000000E+00 -21.62426     000400\n" +
                        "  0.0000000E+00 0.0000000E+00  44.81277      249.3956     0.0000000E+00 000301\n" +
                        "   6.032317      16.17250     0.0000000E+00 0.0000000E+00 -6.106551     000202\n" +
                        "  0.0000000E+00 0.0000000E+00  1.796217      7.290505     0.0000000E+00 000103\n" +
                        "  0.7949109E-01 0.6407839E-01 0.0000000E+00 0.0000000E+00-0.2437245     000004\n" +
                        "  -30.00942     -47.85341     0.0000000E+00 0.0000000E+00  14.47410     500000\n" +
                        "  -93.73450     -98.57786     0.0000000E+00 0.0000000E+00  54.00891     410000\n" +
                        "  -78.67055      23.41404     0.0000000E+00 0.0000000E+00  52.71134     320000\n" +
                        "   15.48651      384.3139     0.0000000E+00 0.0000000E+00 -34.79165     230000\n" +
                        "   25.47383      631.6477     0.0000000E+00 0.0000000E+00 -82.92115     140000\n" +
                        "  -9.459804      335.4776     0.0000000E+00 0.0000000E+00 -33.19463     050000\n" +
                        "  0.0000000E+00 0.0000000E+00 -42.19293     -294.7184     0.0000000E+00 401000\n" +
                        "  0.0000000E+00 0.0000000E+00 -137.3980     -923.7317     0.0000000E+00 311000\n" +
                        "  0.0000000E+00 0.0000000E+00 -127.1548     -838.5234     0.0000000E+00 221000\n" +
                        "  0.0000000E+00 0.0000000E+00  7.802440     -174.3913     0.0000000E+00 131000\n" +
                        "  0.0000000E+00 0.0000000E+00  41.96684      2.897267     0.0000000E+00 041000\n" +
                        "  -54.71923     -6.268511     0.0000000E+00 0.0000000E+00  11.50247     302000\n" +
                        "  -103.7821      273.9389     0.0000000E+00 0.0000000E+00 -9.490681     212000\n" +
                        "  -45.97928      501.2971     0.0000000E+00 0.0000000E+00 -54.73870     122000\n" +
                        "   6.280528      222.7908     0.0000000E+00 0.0000000E+00 -35.45286     032000\n" +
                        "  0.0000000E+00 0.0000000E+00 -74.24604     -448.0409     0.0000000E+00 203000\n" +
                        "  0.0000000E+00 0.0000000E+00 -182.2449     -1161.248     0.0000000E+00 113000\n" +
                        "  0.0000000E+00 0.0000000E+00 -133.4722     -861.0076     0.0000000E+00 023000\n" +
                        "  -53.72644     -178.3086     0.0000000E+00 0.0000000E+00  22.36687     104000\n" +
                        "  -49.12122     -212.0532     0.0000000E+00 0.0000000E+00  13.09682     014000\n" +
                        "  0.0000000E+00 0.0000000E+00 -39.37494     -230.2995     0.0000000E+00 005000\n" +
                        "  0.0000000E+00 0.0000000E+00 -70.34947     -468.9266     0.0000000E+00 400100\n" +
                        "  0.0000000E+00 0.0000000E+00 -254.0979     -1511.502     0.0000000E+00 310100\n" +
                        "  0.0000000E+00 0.0000000E+00 -301.5111     -1599.858     0.0000000E+00 220100\n" +
                        "  0.0000000E+00 0.0000000E+00 -84.29016     -683.0686     0.0000000E+00 130100\n" +
                        "  0.0000000E+00 0.0000000E+00  42.62385     -160.6965     0.0000000E+00 040100\n" +
                        "  -159.1643     -115.7683     0.0000000E+00 0.0000000E+00  28.93449     301100\n" +
                        "  -290.5368      443.6087     0.0000000E+00 0.0000000E+00 -41.87315     211100\n" +
                        "  -121.9370      1057.126     0.0000000E+00 0.0000000E+00 -177.0053     121100\n" +
                        "   9.968879      543.3338     0.0000000E+00 0.0000000E+00 -114.1247     031100\n" +
                        "  0.0000000E+00 0.0000000E+00 -331.9639     -2160.585     0.0000000E+00 202100\n" +
                        "  0.0000000E+00 0.0000000E+00 -782.0186     -5306.517     0.0000000E+00 112100\n" +
                        "  0.0000000E+00 0.0000000E+00 -525.0996     -3724.335     0.0000000E+00 022100\n" +
                        "  -310.5481     -1034.485     0.0000000E+00 0.0000000E+00  108.7818     103100\n" +
                        "  -276.7329     -1207.384     0.0000000E+00 0.0000000E+00  48.93040     013100\n" +
                        "  0.0000000E+00 0.0000000E+00 -253.5800     -1575.378     0.0000000E+00 004100\n" +
                        "  -36.66805     -87.47732     0.0000000E+00 0.0000000E+00  11.41311     400001\n" +
                        "  -144.8435     -325.0919     0.0000000E+00 0.0000000E+00  31.56763     310001\n" +
                        "  -214.6388     -524.6542     0.0000000E+00 0.0000000E+00  8.344860     220001\n" +
                        "  -151.5077     -382.9646     0.0000000E+00 0.0000000E+00 -32.28775     130001\n" +
                        "  -52.88638     -115.3136     0.0000000E+00 0.0000000E+00 -14.97590     040001\n" +
                        "  0.0000000E+00 0.0000000E+00 -28.86579     -135.9075     0.0000000E+00 301001\n" +
                        "  0.0000000E+00 0.0000000E+00 -88.97768     -570.2345     0.0000000E+00 211001\n" +
                        "  0.0000000E+00 0.0000000E+00 -92.49712     -755.0441     0.0000000E+00 121001\n" +
                        "  0.0000000E+00 0.0000000E+00 -40.32625     -413.0327     0.0000000E+00 031001\n" +
                        "  -41.02127     -19.83901     0.0000000E+00 0.0000000E+00  25.03635     202001\n" +
                        "  -61.34344      139.1670     0.0000000E+00 0.0000000E+00  40.77452     112001\n" +
                        "  -27.26621      93.23234     0.0000000E+00 0.0000000E+00  23.98953     022001\n" +
                        "  0.0000000E+00 0.0000000E+00 -52.85760     -280.7933     0.0000000E+00 103001\n" +
                        "  0.0000000E+00 0.0000000E+00 -84.13028     -556.4062     0.0000000E+00 013001\n" +
                        "  -22.89538     -94.43138     0.0000000E+00 0.0000000E+00  18.16104     004001\n" +
                        "  -110.8698     -165.7876     0.0000000E+00 0.0000000E+00  15.57959     300200\n" +
                        "  -184.9527      108.9156     0.0000000E+00 0.0000000E+00 -46.23518     210200\n" +
                        "  -58.80090      690.1745     0.0000000E+00 0.0000000E+00 -142.0409     120200\n" +
                        "   6.342971      506.2811     0.0000000E+00 0.0000000E+00 -84.21753     030200\n" +
                        "  0.0000000E+00 0.0000000E+00 -506.0926     -3411.167     0.0000000E+00 201200\n" +
                        "  0.0000000E+00 0.0000000E+00 -1144.944     -8123.027     0.0000000E+00 111200\n" +
                        "  0.0000000E+00 0.0000000E+00 -686.7898     -5481.222     0.0000000E+00 021200\n" +
                        "  -675.5077     -2237.274     0.0000000E+00 0.0000000E+00  195.1016     102200\n" +
                        "  -575.9243     -2556.482     0.0000000E+00 0.0000000E+00  56.90308     012200\n" +
                        "  0.0000000E+00 0.0000000E+00 -667.1568     -4381.695     0.0000000E+00 003200\n" +
                        "  0.0000000E+00 0.0000000E+00 -53.27042     -339.6148     0.0000000E+00 300101\n" +
                        "  0.0000000E+00 0.0000000E+00 -167.6164     -1333.919     0.0000000E+00 210101\n" +
                        "  0.0000000E+00 0.0000000E+00 -182.4312     -1807.331     0.0000000E+00 120101\n" +
                        "  0.0000000E+00 0.0000000E+00 -73.59036     -925.9566     0.0000000E+00 030101\n" +
                        "  -133.2517     -87.33272     0.0000000E+00 0.0000000E+00  73.98322     201101\n" +
                        "  -191.1328      375.8465     0.0000000E+00 0.0000000E+00  112.7354     111101\n" +
                        "  -78.95683      253.5486     0.0000000E+00 0.0000000E+00  67.76906     021101\n" +
                        "  0.0000000E+00 0.0000000E+00 -276.6208     -1630.702     0.0000000E+00 102101\n" +
                        "  0.0000000E+00 0.0000000E+00 -425.9767     -2828.776     0.0000000E+00 012101\n" +
                        "  -154.1644     -726.1907     0.0000000E+00 0.0000000E+00  108.3831     003101\n" +
                        "  -14.36790     -35.23235     0.0000000E+00 0.0000000E+00  5.914630     300002\n" +
                        "  -36.10691      5.436422     0.0000000E+00 0.0000000E+00  18.76273     210002\n" +
                        "  -15.37034      273.8639     0.0000000E+00 0.0000000E+00  18.21893     120002\n" +
                        "   10.69477      295.1751     0.0000000E+00 0.0000000E+00  5.906729     030002\n" +
                        "  0.0000000E+00 0.0000000E+00 -10.60883     -17.75347     0.0000000E+00 201002\n" +
                        "  0.0000000E+00 0.0000000E+00 -32.97993     -153.8991     0.0000000E+00 111002\n" +
                        "  0.0000000E+00 0.0000000E+00 -28.81432     -174.1777     0.0000000E+00 021002\n" +
                        "  -14.92924     -7.733271     0.0000000E+00 0.0000000E+00  9.234119     102002\n" +
                        "  -11.72864      28.40338     0.0000000E+00 0.0000000E+00  5.603209     012002\n" +
                        "  0.0000000E+00 0.0000000E+00 -16.74791     -48.66251     0.0000000E+00 003002\n" +
                        "  0.0000000E+00 0.0000000E+00 -269.9888     -1831.398     0.0000000E+00 200300\n" +
                        "  0.0000000E+00 0.0000000E+00 -587.0406     -4312.609     0.0000000E+00 110300\n" +
                        "  0.0000000E+00 0.0000000E+00 -302.9893     -2825.089     0.0000000E+00 020300\n" +
                        "  -657.2978     -2133.551     0.0000000E+00 0.0000000E+00  152.0376     101300\n" +
                        "  -522.1491     -2364.550     0.0000000E+00 0.0000000E+00  16.92723     011300\n" +
                        "  0.0000000E+00 0.0000000E+00 -902.7621     -6226.088     0.0000000E+00 002300\n" +
                        "  -108.9965     -110.2881     0.0000000E+00 0.0000000E+00  49.52450     200201\n" +
                        "  -162.4909      228.2905     0.0000000E+00 0.0000000E+00  65.91310     110201\n" +
                        "  -76.41590      197.0987     0.0000000E+00 0.0000000E+00  43.27550     020201\n" +
                        "  0.0000000E+00 0.0000000E+00 -472.1410     -2972.708     0.0000000E+00 101201\n" +
                        "  0.0000000E+00 0.0000000E+00 -706.2175     -4719.118     0.0000000E+00 011201\n" +
                        "  -377.3629     -1904.195     0.0000000E+00 0.0000000E+00  242.7119     002201\n" +
                        "  0.0000000E+00 0.0000000E+00 -27.95897     -124.6487     0.0000000E+00 200102\n" +
                        "  0.0000000E+00 0.0000000E+00 -84.73915     -544.2563     0.0000000E+00 110102\n" +
                        "  0.0000000E+00 0.0000000E+00 -70.94237     -508.6980     0.0000000E+00 020102\n" +
                        "  -57.08531     -38.86906     0.0000000E+00 0.0000000E+00  28.84569     101102\n" +
                        "  -44.10089      97.39494     0.0000000E+00 0.0000000E+00  13.98841     011102\n" +
                        "  0.0000000E+00 0.0000000E+00 -94.29106     -355.0697     0.0000000E+00 002102\n" +
                        "  -4.245600     -8.946738     0.0000000E+00 0.0000000E+00  1.791599     200003\n" +
                        "  -12.33972      1.495558     0.0000000E+00 0.0000000E+00  3.959025     110003\n" +
                        "  -12.43299      8.380338     0.0000000E+00 0.0000000E+00  2.691565     020003\n" +
                        "  0.0000000E+00 0.0000000E+00 -3.168434     -6.814101     0.0000000E+00 101003\n" +
                        "  0.0000000E+00 0.0000000E+00 -5.710079     -36.59949     0.0000000E+00 011003\n" +
                        "  -2.959907     -4.549193     0.0000000E+00 0.0000000E+00  3.357511     002003\n" +
                        "  -242.7930     -751.1531     0.0000000E+00 0.0000000E+00  43.34521     100400\n" +
                        "  -173.2657     -782.4457     0.0000000E+00 0.0000000E+00 -1.560887     010400\n" +
                        "  0.0000000E+00 0.0000000E+00 -631.7626     -4540.325     0.0000000E+00 001400\n" +
                        "  0.0000000E+00 0.0000000E+00 -266.4788     -1766.823     0.0000000E+00 100301\n" +
                        "  0.0000000E+00 0.0000000E+00 -379.4969     -2598.486     0.0000000E+00 010301\n" +
                        "  -403.4837     -2116.666     0.0000000E+00 0.0000000E+00  244.7793     001301\n" +
                        "  -53.47430     -40.77450     0.0000000E+00 0.0000000E+00  21.37117     100202\n" +
                        "  -40.67914      100.8932     0.0000000E+00 0.0000000E+00  9.589109     010202\n" +
                        "  0.0000000E+00 0.0000000E+00 -171.9529     -755.0938     0.0000000E+00 001202\n" +
                        "  0.0000000E+00 0.0000000E+00 -8.091414     -36.76754     0.0000000E+00 100103\n" +
                        "  0.0000000E+00 0.0000000E+00 -10.74781     -81.71967     0.0000000E+00 010103\n" +
                        "  -12.83250     -30.51987     0.0000000E+00 0.0000000E+00  12.63698     001103\n" +
                        " -0.7760376     -1.129056     0.0000000E+00 0.0000000E+00 0.2915186     100004\n" +
                        " -0.6551271      2.307188     0.0000000E+00 0.0000000E+00 0.3375034     010004\n" +
                        "  0.0000000E+00 0.0000000E+00-0.7981733    -0.4571323     0.0000000E+00 001004\n" +
                        "  0.0000000E+00 0.0000000E+00 -183.2095     -1365.173     0.0000000E+00 000500\n" +
                        "  -159.4007     -848.8156     0.0000000E+00 0.0000000E+00  95.16744     000401\n" +
                        "  0.0000000E+00 0.0000000E+00 -104.0322     -507.9210     0.0000000E+00 000302\n" +
                        "  -12.55561     -32.59678     0.0000000E+00 0.0000000E+00  11.80255     000203\n" +
                        "  0.0000000E+00 0.0000000E+00 -2.220377     -6.059815     0.0000000E+00 000104\n" +
                        " -0.9238018E-01-0.1289569     0.0000000E+00 0.0000000E+00 0.2303171     000005"
        )),
        AFTER_QS2("AFTER_QS2", DL1 + CCT12_LENGTH + GAP1 + QS1_LEN + GAP2 + QS2_LEN, CosyArbitraryOrder.readMap(
                " -0.5681410     -3.034779     0.0000000E+00 0.0000000E+00 0.1680138     100000\n" +
                        " -0.3389771     -3.570805     0.0000000E+00 0.0000000E+00 0.2290133     010000\n" +
                        "  0.0000000E+00 0.0000000E+00 0.7660123     -1.800034     0.0000000E+00 001000\n" +
                        "  0.0000000E+00 0.0000000E+00  1.834271     -3.004847     0.0000000E+00 000100\n" +
                        "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00  1.000000     000010\n" +
                        " -0.7315902E-01-0.9506020E-01 0.0000000E+00 0.0000000E+00 0.4200946     000001\n" +
                        "   1.616026     -3.350119     0.0000000E+00 0.0000000E+00-0.7470038     200000\n" +
                        "   8.476638      20.96745     0.0000000E+00 0.0000000E+00 -1.826808     110000\n" +
                        "   10.26188      34.43169     0.0000000E+00 0.0000000E+00 -1.768483     020000\n" +
                        "  0.0000000E+00 0.0000000E+00 -4.704814     -27.64305     0.0000000E+00 101000\n" +
                        "  0.0000000E+00 0.0000000E+00 -7.334209     -2.941764     0.0000000E+00 011000\n" +
                        "  0.6338938      29.47437     0.0000000E+00 0.0000000E+00-0.6536260     002000\n" +
                        "  0.0000000E+00 0.0000000E+00 -9.189800     -63.05398     0.0000000E+00 100100\n" +
                        "  0.0000000E+00 0.0000000E+00 -12.39848     -6.679371     0.0000000E+00 010100\n" +
                        "   6.393508      148.2818     0.0000000E+00 0.0000000E+00 -1.797871     001100\n" +
                        " -0.1525592     -4.848116     0.0000000E+00 0.0000000E+00-0.4531336E-01 100001\n" +
                        " -0.1167624     -2.557445     0.0000000E+00 0.0000000E+00-0.1011411     010001\n" +
                        "  0.0000000E+00 0.0000000E+00  1.175447     -6.528234     0.0000000E+00 001001\n" +
                        "   10.21940      180.6763     0.0000000E+00 0.0000000E+00 -1.909733     000200\n" +
                        "  0.0000000E+00 0.0000000E+00 0.3505535     -11.84514     0.0000000E+00 000101\n" +
                        "  0.1339090E-01-0.2677307     0.0000000E+00 0.0000000E+00-0.3586644     000002\n" +
                        "   14.41858      116.6605     0.0000000E+00 0.0000000E+00 0.2187746     300000\n" +
                        "   67.41175      516.2822     0.0000000E+00 0.0000000E+00  5.758823     210000\n" +
                        "   89.35131      678.5236     0.0000000E+00 0.0000000E+00  17.80567     120000\n" +
                        "   27.73386      185.8961     0.0000000E+00 0.0000000E+00  14.45520     030000\n" +
                        "  0.0000000E+00 0.0000000E+00  1.812620      252.2287     0.0000000E+00 201000\n" +
                        "  0.0000000E+00 0.0000000E+00  29.68035      824.8767     0.0000000E+00 111000\n" +
                        "  0.0000000E+00 0.0000000E+00  32.50163      676.2295     0.0000000E+00 021000\n" +
                        "  -26.25311     -310.9009     0.0000000E+00 0.0000000E+00  1.944978     102000\n" +
                        "  -59.32575     -669.8751     0.0000000E+00 0.0000000E+00  4.380330     012000\n" +
                        "  0.0000000E+00 0.0000000E+00 -23.48620     -84.39985     0.0000000E+00 003000\n" +
                        "  0.0000000E+00 0.0000000E+00  12.43525      526.2256     0.0000000E+00 200100\n" +
                        "  0.0000000E+00 0.0000000E+00  82.72096      1735.248     0.0000000E+00 110100\n" +
                        "  0.0000000E+00 0.0000000E+00  91.84160      1514.621     0.0000000E+00 020100\n" +
                        "  -100.9623     -1193.168     0.0000000E+00 0.0000000E+00  13.81094     101100\n" +
                        "  -233.6064     -2639.498     0.0000000E+00 0.0000000E+00  27.70760     011100\n" +
                        "  0.0000000E+00 0.0000000E+00 -110.5019     -376.4264     0.0000000E+00 002100\n" +
                        "   2.290509      21.84031     0.0000000E+00 0.0000000E+00-0.1032741     200001\n" +
                        "   3.754808      68.10392     0.0000000E+00 0.0000000E+00-0.4369798E-01 110001\n" +
                        "  -4.607043      45.30629     0.0000000E+00 0.0000000E+00  1.031210     020001\n" +
                        "  0.0000000E+00 0.0000000E+00 -6.629670     -22.85996     0.0000000E+00 101001\n" +
                        "  0.0000000E+00 0.0000000E+00  1.251279      27.08150     0.0000000E+00 011001\n" +
                        "   8.204502      122.8591     0.0000000E+00 0.0000000E+00 0.6542722     002001\n" +
                        "  -96.94816     -1134.217     0.0000000E+00 0.0000000E+00  20.02200     100200\n" +
                        "  -228.8934     -2558.032     0.0000000E+00 0.0000000E+00  36.97421     010200\n" +
                        "  0.0000000E+00 0.0000000E+00 -172.9254     -441.0843     0.0000000E+00 001200\n" +
                        "  0.0000000E+00 0.0000000E+00 -3.149005      16.32615     0.0000000E+00 100101\n" +
                        "  0.0000000E+00 0.0000000E+00  9.481282      48.63309     0.0000000E+00 010101\n" +
                        "   26.94357      382.6770     0.0000000E+00 0.0000000E+00  1.454346     001101\n" +
                        "  0.2672653      2.309812     0.0000000E+00 0.0000000E+00 0.4800571E-01 100002\n" +
                        "  0.8101973      3.716463     0.0000000E+00 0.0000000E+00 0.1937258E-01 010002\n" +
                        "  0.0000000E+00 0.0000000E+00 -2.263110     -4.778155     0.0000000E+00 001002\n" +
                        "  0.0000000E+00 0.0000000E+00 -86.93618     -60.41096     0.0000000E+00 000300\n" +
                        "   14.36183      212.6284     0.0000000E+00 0.0000000E+00  1.738550     000201\n" +
                        "  0.0000000E+00 0.0000000E+00 -2.904996      1.696043     0.0000000E+00 000102\n" +
                        " -0.1983716E-01 0.1305484     0.0000000E+00 0.0000000E+00 0.3170167     000003\n" +
                        "   13.44313      178.0549     0.0000000E+00 0.0000000E+00  20.65248     400000\n" +
                        "   27.27809      339.3869     0.0000000E+00 0.0000000E+00  132.2513     310000\n" +
                        "  -75.25647     -1749.160     0.0000000E+00 0.0000000E+00  268.2954     220000\n" +
                        "  -224.5582     -4906.332     0.0000000E+00 0.0000000E+00  187.7028     130000\n" +
                        "  -146.8220     -3271.015     0.0000000E+00 0.0000000E+00  19.33435     040000\n" +
                        "  0.0000000E+00 0.0000000E+00  67.25802      442.5345     0.0000000E+00 301000\n" +
                        "  0.0000000E+00 0.0000000E+00  101.0491      254.5396     0.0000000E+00 211000\n" +
                        "  0.0000000E+00 0.0000000E+00 -161.8430     -2743.453     0.0000000E+00 121000\n" +
                        "  0.0000000E+00 0.0000000E+00 -308.8338     -3784.635     0.0000000E+00 031000\n" +
                        "  -81.27357     -873.2014     0.0000000E+00 0.0000000E+00 -41.13996     202000\n" +
                        "   4.390068      268.9775     0.0000000E+00 0.0000000E+00 -139.3102     112000\n" +
                        "   244.3696      2722.596     0.0000000E+00 0.0000000E+00 -140.2558     022000\n" +
                        "  0.0000000E+00 0.0000000E+00  100.4482      59.42166     0.0000000E+00 103000\n" +
                        "  0.0000000E+00 0.0000000E+00  116.0675     -784.5024     0.0000000E+00 013000\n" +
                        "  -94.93401     -1447.091     0.0000000E+00 0.0000000E+00 -26.40127     004000\n" +
                        "  0.0000000E+00 0.0000000E+00  130.2151      924.0630     0.0000000E+00 300100\n" +
                        "  0.0000000E+00 0.0000000E+00  279.1272      1493.692     0.0000000E+00 210100\n" +
                        "  0.0000000E+00 0.0000000E+00 -127.4964     -3149.462     0.0000000E+00 120100\n" +
                        "  0.0000000E+00 0.0000000E+00 -472.3228     -5676.942     0.0000000E+00 030100\n" +
                        "  -274.6273     -3442.997     0.0000000E+00 0.0000000E+00 -152.1338     201100\n" +
                        "   95.40226      188.6924     0.0000000E+00 0.0000000E+00 -556.2898     111100\n" +
                        "   930.0151      9183.937     0.0000000E+00 0.0000000E+00 -581.1284     021100\n" +
                        "  0.0000000E+00 0.0000000E+00  388.1143     -1210.272     0.0000000E+00 102100\n" +
                        "  0.0000000E+00 0.0000000E+00  397.4154     -6722.359     0.0000000E+00 012100\n" +
                        "  -658.7596     -9854.453     0.0000000E+00 0.0000000E+00 -207.1639     003100\n" +
                        "  -4.659868      79.35000     0.0000000E+00 0.0000000E+00  2.332874     300001\n" +
                        "  -82.68664      51.10389     0.0000000E+00 0.0000000E+00  15.58803     210001\n" +
                        "  -193.9435     -410.8004     0.0000000E+00 0.0000000E+00  15.89893     120001\n" +
                        "  -94.94979     -187.7821     0.0000000E+00 0.0000000E+00 -4.658712     030001\n" +
                        "  0.0000000E+00 0.0000000E+00  62.82526      539.3801     0.0000000E+00 201001\n" +
                        "  0.0000000E+00 0.0000000E+00  138.6909      1045.614     0.0000000E+00 111001\n" +
                        "  0.0000000E+00 0.0000000E+00  95.34918      630.4528     0.0000000E+00 021001\n" +
                        "  -74.65428     -1023.668     0.0000000E+00 0.0000000E+00  12.61064     102001\n" +
                        "  -100.2170     -1373.118     0.0000000E+00 0.0000000E+00  26.63807     012001\n" +
                        "  0.0000000E+00 0.0000000E+00  14.39910      48.76237     0.0000000E+00 003001\n" +
                        "  -216.7950     -3235.359     0.0000000E+00 0.0000000E+00 -146.1585     200200\n" +
                        "   200.9862     -181.2491     0.0000000E+00 0.0000000E+00 -570.4939     110200\n" +
                        "   943.5431      8247.530     0.0000000E+00 0.0000000E+00 -620.5195     020200\n" +
                        "  0.0000000E+00 0.0000000E+00  455.2935     -4988.770     0.0000000E+00 101200\n" +
                        "  0.0000000E+00 0.0000000E+00  316.0020     -16979.36     0.0000000E+00 011200\n" +
                        "  -1690.199     -25167.85     0.0000000E+00 0.0000000E+00 -653.8662     002200\n" +
                        "  0.0000000E+00 0.0000000E+00  85.02280      510.6662     0.0000000E+00 200101\n" +
                        "  0.0000000E+00 0.0000000E+00  138.6793      405.9548     0.0000000E+00 110101\n" +
                        "  0.0000000E+00 0.0000000E+00  31.54219     -483.4680     0.0000000E+00 020101\n" +
                        "  -202.6576     -2881.486     0.0000000E+00 0.0000000E+00  42.86972     101101\n" +
                        "  -167.7093     -2851.207     0.0000000E+00 0.0000000E+00  72.68466     011101\n" +
                        "  0.0000000E+00 0.0000000E+00  130.8188      840.8923     0.0000000E+00 002101\n" +
                        "  0.4678095      5.973119     0.0000000E+00 0.0000000E+00-0.3925032     200002\n" +
                        "  -4.445206     -14.28953     0.0000000E+00 0.0000000E+00 0.8768904     110002\n" +
                        "  -2.382351     -68.69952     0.0000000E+00 0.0000000E+00 0.9445274     020002\n" +
                        "  0.0000000E+00 0.0000000E+00  16.77761      99.91689     0.0000000E+00 101002\n" +
                        "  0.0000000E+00 0.0000000E+00  9.265577      25.07139     0.0000000E+00 011002\n" +
                        "  -6.197104     -52.26429     0.0000000E+00 0.0000000E+00 -2.526381     002002\n" +
                        "  0.0000000E+00 0.0000000E+00  127.6073     -4837.617     0.0000000E+00 100300\n" +
                        "  0.0000000E+00 0.0000000E+00 -64.98409     -13401.99     0.0000000E+00 010300\n" +
                        "  -1917.220     -28719.18     0.0000000E+00 0.0000000E+00 -963.2062     001300\n" +
                        "  -112.3497     -1755.092     0.0000000E+00 0.0000000E+00  21.76341     100201\n" +
                        "   43.61023     -512.1924     0.0000000E+00 0.0000000E+00  25.76985     010201\n" +
                        "  0.0000000E+00 0.0000000E+00  318.1999      2209.708     0.0000000E+00 001201\n" +
                        "  0.0000000E+00 0.0000000E+00  24.11794      123.4893     0.0000000E+00 100102\n" +
                        "  0.0000000E+00 0.0000000E+00  9.121747      36.25792     0.0000000E+00 010102\n" +
                        "  -47.72352     -498.3820     0.0000000E+00 0.0000000E+00 -5.689002     001102\n" +
                        "  0.8802553E-01-0.5931249     0.0000000E+00 0.0000000E+00-0.1702844     100003\n" +
                        " -0.7989283     0.1264547E-01 0.0000000E+00 0.0000000E+00-0.9840278E-01 010003\n" +
                        "  0.0000000E+00 0.0000000E+00  3.235340      11.22859     0.0000000E+00 001003\n" +
                        "  -806.4796     -12290.02     0.0000000E+00 0.0000000E+00 -549.3763     000400\n" +
                        "  0.0000000E+00 0.0000000E+00  223.6463      1366.373     0.0000000E+00 000301\n" +
                        "  -51.47629     -576.3151     0.0000000E+00 0.0000000E+00 -3.565986     000202\n" +
                        "  0.0000000E+00 0.0000000E+00  5.708805      11.13685     0.0000000E+00 000103\n" +
                        "  0.4122631E-01-0.1209349     0.0000000E+00 0.0000000E+00-0.2922251     000004\n" +
                        "   14.95228     -1113.910     0.0000000E+00 0.0000000E+00  36.47752     500000\n" +
                        "   75.08506     -12000.21     0.0000000E+00 0.0000000E+00  7.130777     410000\n" +
                        "  -392.7888     -45243.81     0.0000000E+00 0.0000000E+00 -768.9777     320000\n" +
                        "  -2183.446     -79319.66     0.0000000E+00 0.0000000E+00 -2209.260     230000\n" +
                        "  -3192.552     -64336.64     0.0000000E+00 0.0000000E+00 -2206.500     140000\n" +
                        "  -1508.760     -18759.04     0.0000000E+00 0.0000000E+00 -697.2497     050000\n" +
                        "  0.0000000E+00 0.0000000E+00 -479.3273     -5021.285     0.0000000E+00 401000\n" +
                        "  0.0000000E+00 0.0000000E+00 -2359.447     -28015.48     0.0000000E+00 311000\n" +
                        "  0.0000000E+00 0.0000000E+00 -4710.625     -58193.01     0.0000000E+00 221000\n" +
                        "  0.0000000E+00 0.0000000E+00 -4056.858     -48490.76     0.0000000E+00 131000\n" +
                        "  0.0000000E+00 0.0000000E+00 -1196.887     -13121.58     0.0000000E+00 041000\n" +
                        "   490.7841      8161.885     0.0000000E+00 0.0000000E+00 -52.57078     302000\n" +
                        "   1788.611      32937.53     0.0000000E+00 0.0000000E+00  154.9870     212000\n" +
                        "   1604.229      42075.26     0.0000000E+00 0.0000000E+00  1034.844     122000\n" +
                        "   74.65935      15380.40     0.0000000E+00 0.0000000E+00  1013.665     032000\n" +
                        "  0.0000000E+00 0.0000000E+00 -455.7358     -3292.822     0.0000000E+00 203000\n" +
                        "  0.0000000E+00 0.0000000E+00 -995.2771     -859.9355     0.0000000E+00 113000\n" +
                        "  0.0000000E+00 0.0000000E+00 -461.8982      7823.970     0.0000000E+00 023000\n" +
                        "   608.8476      11033.99     0.0000000E+00 0.0000000E+00 -9.403560     104000\n" +
                        "   1211.880      20110.45     0.0000000E+00 0.0000000E+00  313.9570     014000\n" +
                        "  0.0000000E+00 0.0000000E+00  52.36462     -65.03772     0.0000000E+00 005000\n" +
                        "  0.0000000E+00 0.0000000E+00 -755.2495     -8076.190     0.0000000E+00 400100\n" +
                        "  0.0000000E+00 0.0000000E+00 -3733.564     -45297.28     0.0000000E+00 310100\n" +
                        "  0.0000000E+00 0.0000000E+00 -7178.219     -88969.02     0.0000000E+00 220100\n" +
                        "  0.0000000E+00 0.0000000E+00 -5554.069     -63876.53     0.0000000E+00 130100\n" +
                        "  0.0000000E+00 0.0000000E+00 -1187.860     -10488.03     0.0000000E+00 040100\n" +
                        "   1759.164      29009.25     0.0000000E+00 0.0000000E+00 -226.2083     301100\n" +
                        "   5229.242      111550.1     0.0000000E+00 0.0000000E+00  546.0290     211100\n" +
                        "   2300.951      132459.9     0.0000000E+00 0.0000000E+00  4036.262     121100\n" +
                        "  -2425.883      40083.77     0.0000000E+00 0.0000000E+00  4073.586     031100\n" +
                        "  0.0000000E+00 0.0000000E+00 -1747.116     -13264.90     0.0000000E+00 202100\n" +
                        "  0.0000000E+00 0.0000000E+00 -3197.548      15703.42     0.0000000E+00 112100\n" +
                        "  0.0000000E+00 0.0000000E+00 -725.4316      62034.73     0.0000000E+00 022100\n" +
                        "   3316.051      71514.32     0.0000000E+00 0.0000000E+00  348.1840     103100\n" +
                        "   6695.496      129232.1     0.0000000E+00 0.0000000E+00  3423.291     013100\n" +
                        "  0.0000000E+00 0.0000000E+00  51.88876     -7162.180     0.0000000E+00 004100\n" +
                        "  -63.15181     -365.3136     0.0000000E+00 0.0000000E+00  14.55286     400001\n" +
                        "  -210.6705     -3263.442     0.0000000E+00 0.0000000E+00 -79.76145     310001\n" +
                        "   73.86121     -5247.045     0.0000000E+00 0.0000000E+00 -494.3912     220001\n" +
                        "   804.3224      1570.619     0.0000000E+00 0.0000000E+00 -664.9600     130001\n" +
                        "   686.9536      5367.698     0.0000000E+00 0.0000000E+00 -219.0419     040001\n" +
                        "  0.0000000E+00 0.0000000E+00 -270.0773     -2029.389     0.0000000E+00 301001\n" +
                        "  0.0000000E+00 0.0000000E+00 -904.4303     -8219.070     0.0000000E+00 211001\n" +
                        "  0.0000000E+00 0.0000000E+00 -893.4931     -9966.676     0.0000000E+00 121001\n" +
                        "  0.0000000E+00 0.0000000E+00  117.3564      225.4841     0.0000000E+00 031001\n" +
                        "   325.9665      2394.153     0.0000000E+00 0.0000000E+00 -80.37462     202001\n" +
                        "   1172.655      8948.505     0.0000000E+00 0.0000000E+00 -283.1233     112001\n" +
                        "   611.7041      4946.700     0.0000000E+00 0.0000000E+00 -187.9941     022001\n" +
                        "  0.0000000E+00 0.0000000E+00 -279.2713     -4037.308     0.0000000E+00 103001\n" +
                        "  0.0000000E+00 0.0000000E+00 -476.2401     -6900.949     0.0000000E+00 013001\n" +
                        "  -11.24934     -925.4913     0.0000000E+00 0.0000000E+00 -92.51376     004001\n" +
                        "   1591.313      25827.29     0.0000000E+00 0.0000000E+00 -228.0815     300200\n" +
                        "   3727.128      96183.59     0.0000000E+00 0.0000000E+00  451.5975     210200\n" +
                        "  -937.0321      106956.1     0.0000000E+00 0.0000000E+00  3873.301     120200\n" +
                        "  -4624.262      25332.72     0.0000000E+00 0.0000000E+00  4041.105     030200\n" +
                        "  0.0000000E+00 0.0000000E+00 -1887.604     -16217.85     0.0000000E+00 201200\n" +
                        "  0.0000000E+00 0.0000000E+00 -1953.579      66539.36     0.0000000E+00 111200\n" +
                        "  0.0000000E+00 0.0000000E+00  1708.098      150029.6     0.0000000E+00 021200\n" +
                        "   5661.651      173651.9     0.0000000E+00 0.0000000E+00  2408.513     102200\n" +
                        "   12240.32      311771.2     0.0000000E+00 0.0000000E+00  12969.33     012200\n" +
                        "  0.0000000E+00 0.0000000E+00 -1255.357     -45157.51     0.0000000E+00 003200\n" +
                        "  0.0000000E+00 0.0000000E+00 -542.5853     -4052.557     0.0000000E+00 300101\n" +
                        "  0.0000000E+00 0.0000000E+00 -1993.514     -17682.01     0.0000000E+00 210101\n" +
                        "  0.0000000E+00 0.0000000E+00 -2074.130     -21207.86     0.0000000E+00 120101\n" +
                        "  0.0000000E+00 0.0000000E+00  127.0068     -112.8755     0.0000000E+00 030101\n" +
                        "   1573.113      12140.95     0.0000000E+00 0.0000000E+00 -244.0782     201101\n" +
                        "   4563.888      35335.63     0.0000000E+00 0.0000000E+00 -720.5385     111101\n" +
                        "   1904.784      15355.03     0.0000000E+00 0.0000000E+00 -274.0022     021101\n" +
                        "  0.0000000E+00 0.0000000E+00 -1539.373     -22033.76     0.0000000E+00 102101\n" +
                        "  0.0000000E+00 0.0000000E+00 -2509.878     -32992.32     0.0000000E+00 012101\n" +
                        "   461.2511      313.7436     0.0000000E+00 0.0000000E+00 -735.8754     003101\n" +
                        "  -21.56455     -67.24655     0.0000000E+00 0.0000000E+00  8.260472     300002\n" +
                        "  -10.40327     -641.0969     0.0000000E+00 0.0000000E+00  8.243524     210002\n" +
                        "   156.8276     -839.1837     0.0000000E+00 0.0000000E+00 -22.69010     120002\n" +
                        "   118.3832     -577.5172     0.0000000E+00 0.0000000E+00 -16.62124     030002\n" +
                        "  0.0000000E+00 0.0000000E+00 -116.7790     -789.5297     0.0000000E+00 201002\n" +
                        "  0.0000000E+00 0.0000000E+00 -345.8120     -2466.705     0.0000000E+00 111002\n" +
                        "  0.0000000E+00 0.0000000E+00 -361.9698     -2885.636     0.0000000E+00 021002\n" +
                        "   64.30727      552.5941     0.0000000E+00 0.0000000E+00 -1.312197     102002\n" +
                        "   249.4210      2275.232     0.0000000E+00 0.0000000E+00 -37.88937     012002\n" +
                        "  0.0000000E+00 0.0000000E+00 -4.538422      307.9831     0.0000000E+00 003002\n" +
                        "  0.0000000E+00 0.0000000E+00 -303.8584     -4247.816     0.0000000E+00 200300\n" +
                        "  0.0000000E+00 0.0000000E+00  1329.807      67537.67     0.0000000E+00 110300\n" +
                        "  0.0000000E+00 0.0000000E+00  3052.753      118485.1     0.0000000E+00 020300\n" +
                        "   2098.306      186279.7     0.0000000E+00 0.0000000E+00  5053.566     101300\n" +
                        "   6808.329      332442.7     0.0000000E+00 0.0000000E+00  20742.56     011300\n" +
                        "  0.0000000E+00 0.0000000E+00 -4712.665     -109643.3     0.0000000E+00 002300\n" +
                        "   1691.331      13717.93     0.0000000E+00 0.0000000E+00 -143.3665     200201\n" +
                        "   4166.741      33923.21     0.0000000E+00 0.0000000E+00 -235.6174     110201\n" +
                        "   1168.865      10201.57     0.0000000E+00 0.0000000E+00  330.3364     020201\n" +
                        "  0.0000000E+00 0.0000000E+00 -2667.633     -37017.17     0.0000000E+00 101201\n" +
                        "  0.0000000E+00 0.0000000E+00 -3986.286     -44590.46     0.0000000E+00 011201\n" +
                        "   2423.219      16471.08     0.0000000E+00 0.0000000E+00 -2006.222     002201\n" +
                        "  0.0000000E+00 0.0000000E+00 -213.4449     -1314.687     0.0000000E+00 200102\n" +
                        "  0.0000000E+00 0.0000000E+00 -579.0879     -3693.235     0.0000000E+00 110102\n" +
                        "  0.0000000E+00 0.0000000E+00 -494.0950     -3424.371     0.0000000E+00 020102\n" +
                        "   380.9955      3880.533     0.0000000E+00 0.0000000E+00 -61.46245     101102\n" +
                        "   911.5188      9040.147     0.0000000E+00 0.0000000E+00 -198.1595     011102\n" +
                        "  0.0000000E+00 0.0000000E+00 -83.09228      501.8253     0.0000000E+00 002102\n" +
                        "  -4.307403      5.992555     0.0000000E+00 0.0000000E+00  2.086470     200003\n" +
                        "  -4.866831     -13.35701     0.0000000E+00 0.0000000E+00  2.626028     110003\n" +
                        "   4.952267      79.06608     0.0000000E+00 0.0000000E+00-0.6122795     020003\n" +
                        "  0.0000000E+00 0.0000000E+00 -24.60156     -129.6239     0.0000000E+00 101003\n" +
                        "  0.0000000E+00 0.0000000E+00 -14.50580      1.111220     0.0000000E+00 011003\n" +
                        "  -10.04950     -121.6910     0.0000000E+00 0.0000000E+00  6.064956     002003\n" +
                        "  -1671.896      73561.53     0.0000000E+00 0.0000000E+00  3451.063     100400\n" +
                        "  -1159.729      130250.4     0.0000000E+00 0.0000000E+00  12019.98     010400\n" +
                        "  0.0000000E+00 0.0000000E+00 -6323.966     -119186.0     0.0000000E+00 001400\n" +
                        "  0.0000000E+00 0.0000000E+00 -1392.563     -18137.07     0.0000000E+00 100301\n" +
                        "  0.0000000E+00 0.0000000E+00 -1740.461     -12985.11     0.0000000E+00 010301\n" +
                        "   4110.677      36620.53     0.0000000E+00 0.0000000E+00 -2096.783     001301\n" +
                        "   411.4052      4391.517     0.0000000E+00 0.0000000E+00 -77.16576     100202\n" +
                        "   673.8558      7294.345     0.0000000E+00 0.0000000E+00 -186.5119     010202\n" +
                        "  0.0000000E+00 0.0000000E+00 -340.9529     -1996.811     0.0000000E+00 001202\n" +
                        "  0.0000000E+00 0.0000000E+00 -46.25562     -224.8313     0.0000000E+00 100103\n" +
                        "  0.0000000E+00 0.0000000E+00 -28.95335     -54.58901     0.0000000E+00 010103\n" +
                        "   21.53218      127.7590     0.0000000E+00 0.0000000E+00  17.42551     001103\n" +
                        " -0.4797453      1.845328     0.0000000E+00 0.0000000E+00 0.3480766     100004\n" +
                        "  0.2116761     -3.637710     0.0000000E+00 0.0000000E+00 0.3509684     010004\n" +
                        "  0.0000000E+00 0.0000000E+00 -4.277387     -17.69811     0.0000000E+00 001004\n" +
                        "  0.0000000E+00 0.0000000E+00 -2969.932     -48413.31     0.0000000E+00 000500\n" +
                        "   2311.064      23603.57     0.0000000E+00 0.0000000E+00 -566.4010     000401\n" +
                        "  0.0000000E+00 0.0000000E+00 -340.7355     -2782.869     0.0000000E+00 000302\n" +
                        "   65.09981      603.0312     0.0000000E+00 0.0000000E+00  11.90341     000203\n" +
                        "  0.0000000E+00 0.0000000E+00 -8.832800     -25.84522     0.0000000E+00 000104\n" +
                        " -0.6210687E-01 0.2360131     0.0000000E+00 0.0000000E+00 0.2770816     000005"
        )),
        AFTER_QS1_2("AFTER_QS1_2", DL1 + CCT12_LENGTH + GAP1 + QS1_LEN + GAP2 + QS2_LEN + GAP2 + QS1_LEN, CosyArbitraryOrder.readMap(
                "  -1.129535      2.051592     0.0000000E+00 0.0000000E+00 0.1680138     100000\n" +
                        "  -1.133493      1.173461     0.0000000E+00 0.0000000E+00 0.2290133     010000\n" +
                        "  0.0000000E+00 0.0000000E+00 0.3144687    -0.1458455     0.0000000E+00 001000\n" +
                        "  0.0000000E+00 0.0000000E+00  1.341823      2.557651     0.0000000E+00 000100\n" +
                        "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00  1.000000     000010\n" +
                        " -0.6823598E-01 0.2726842     0.0000000E+00 0.0000000E+00 0.4882306     000001\n" +
                        "   2.238751      14.49318     0.0000000E+00 0.0000000E+00 -1.272839     200000\n" +
                        "   14.43495      7.521148     0.0000000E+00 0.0000000E+00 -3.043487     110000\n" +
                        "   16.87465     -19.37780     0.0000000E+00 0.0000000E+00 -2.504545     020000\n" +
                        "  0.0000000E+00 0.0000000E+00 -17.62599     -67.33492     0.0000000E+00 101000\n" +
                        "  0.0000000E+00 0.0000000E+00 -10.52093     -26.98316     0.0000000E+00 011000\n" +
                        "   7.798772     -3.653894     0.0000000E+00 0.0000000E+00-0.8478815     002000\n" +
                        "  0.0000000E+00 0.0000000E+00 -36.43535     -127.0604     0.0000000E+00 100100\n" +
                        "  0.0000000E+00 0.0000000E+00 -16.24105     -22.03420     0.0000000E+00 010100\n" +
                        "   40.81161     -34.04794     0.0000000E+00 0.0000000E+00 -2.355674     001100\n" +
                        " -0.8476481      2.117869     0.0000000E+00 0.0000000E+00-0.8174119E-01 100001\n" +
                        " -0.1563852      1.528724     0.0000000E+00 0.0000000E+00-0.1324392     010001\n" +
                        "  0.0000000E+00 0.0000000E+00-0.7342948     -6.206318     0.0000000E+00 001001\n" +
                        "   50.57352     -57.63823     0.0000000E+00 0.0000000E+00 -2.430567     000200\n" +
                        "  0.0000000E+00 0.0000000E+00 -3.983237     -18.66251     0.0000000E+00 000101\n" +
                        " -0.5545314E-01 0.2619129E-01 0.0000000E+00 0.0000000E+00-0.4173160     000002\n" +
                        "   33.11185     -91.22242     0.0000000E+00 0.0000000E+00 -1.204503     300000\n" +
                        "   117.2085     -724.9342     0.0000000E+00 0.0000000E+00  11.48699     210000\n" +
                        "   116.4544     -1327.081     0.0000000E+00 0.0000000E+00  38.45385     120000\n" +
                        "   4.652673     -652.0347     0.0000000E+00 0.0000000E+00  28.22996     030000\n" +
                        "  0.0000000E+00 0.0000000E+00  64.24856     -37.26528     0.0000000E+00 201000\n" +
                        "  0.0000000E+00 0.0000000E+00  310.9170      814.7464     0.0000000E+00 111000\n" +
                        "  0.0000000E+00 0.0000000E+00  291.0779      1015.780     0.0000000E+00 021000\n" +
                        "  -107.4679     -17.34307     0.0000000E+00 0.0000000E+00  3.836157     102000\n" +
                        "  -222.4759      68.91371     0.0000000E+00 0.0000000E+00  15.21254     012000\n" +
                        "  0.0000000E+00 0.0000000E+00 -83.32103     -414.4297     0.0000000E+00 003000\n" +
                        "  0.0000000E+00 0.0000000E+00  144.6726     -68.75618     0.0000000E+00 200100\n" +
                        "  0.0000000E+00 0.0000000E+00  680.4031      1750.116     0.0000000E+00 110100\n" +
                        "  0.0000000E+00 0.0000000E+00  676.6474      2309.449     0.0000000E+00 020100\n" +
                        "  -431.1444     -208.9772     0.0000000E+00 0.0000000E+00  40.44013     101100\n" +
                        "  -900.3696      40.29714     0.0000000E+00 0.0000000E+00  87.10419     011100\n" +
                        "  0.0000000E+00 0.0000000E+00 -421.8110     -2336.051     0.0000000E+00 002100\n" +
                        "   9.105184      17.85926     0.0000000E+00 0.0000000E+00 -1.144562     200001\n" +
                        "   17.21036     0.8662847     0.0000000E+00 0.0000000E+00 -1.058103     110001\n" +
                        "   2.661108     -2.268047     0.0000000E+00 0.0000000E+00  1.468342     020001\n" +
                        "  0.0000000E+00 0.0000000E+00 -13.79165     -100.0809     0.0000000E+00 101001\n" +
                        "  0.0000000E+00 0.0000000E+00  13.47311      36.09981     0.0000000E+00 011001\n" +
                        "   32.39669     -29.36437     0.0000000E+00 0.0000000E+00-0.4472332     002001\n" +
                        "  -429.3493     -337.2074     0.0000000E+00 0.0000000E+00  66.76707     100200\n" +
                        "  -904.5904     -257.5989     0.0000000E+00 0.0000000E+00  110.2114     010200\n" +
                        "  0.0000000E+00 0.0000000E+00 -700.9234     -4528.468     0.0000000E+00 001200\n" +
                        "  0.0000000E+00 0.0000000E+00  9.074071     -89.81714     0.0000000E+00 100101\n" +
                        "  0.0000000E+00 0.0000000E+00  26.85040      18.49093     0.0000000E+00 010101\n" +
                        "   95.68780     -83.33915     0.0000000E+00 0.0000000E+00 -1.480893     001101\n" +
                        "   1.323965      1.385829     0.0000000E+00 0.0000000E+00-0.3708277E-01 100002\n" +
                        "   1.573851    -0.9464673     0.0000000E+00 0.0000000E+00-0.7670390E-01 010002\n" +
                        "  0.0000000E+00 0.0000000E+00 -4.119055     -18.39820     0.0000000E+00 001002\n" +
                        "  0.0000000E+00 0.0000000E+00 -377.9607     -3028.965     0.0000000E+00 000300\n" +
                        "   44.72817      7.773215     0.0000000E+00 0.0000000E+00  1.095515     000201\n" +
                        "  0.0000000E+00 0.0000000E+00 -1.036423     -9.487189     0.0000000E+00 000102\n" +
                        "  0.6095408E-01 0.6900462E-01 0.0000000E+00 0.0000000E+00 0.3666782     000003\n" +
                        "  -74.00986     -1223.558     0.0000000E+00 0.0000000E+00  54.38463     400000\n" +
                        "  -503.4532     -6027.208     0.0000000E+00 0.0000000E+00  350.1470     310000\n" +
                        "  -1283.812     -8367.613     0.0000000E+00 0.0000000E+00  676.7645     220000\n" +
                        "  -1382.964     -831.7940     0.0000000E+00 0.0000000E+00  413.0376     130000\n" +
                        "  -571.4865      3111.395     0.0000000E+00 0.0000000E+00  7.643090     040000\n" +
                        "  0.0000000E+00 0.0000000E+00  474.7440      3944.014     0.0000000E+00 301000\n" +
                        "  0.0000000E+00 0.0000000E+00  1620.513      18715.22     0.0000000E+00 211000\n" +
                        "  0.0000000E+00 0.0000000E+00  935.1956      22843.37     0.0000000E+00 121000\n" +
                        "  0.0000000E+00 0.0000000E+00 -950.8527      4349.519     0.0000000E+00 031000\n" +
                        "  -162.2044      607.1251     0.0000000E+00 0.0000000E+00 -231.9481     202000\n" +
                        "   905.3042      6782.633     0.0000000E+00 0.0000000E+00 -452.8381     112000\n" +
                        "   1609.840      5815.153     0.0000000E+00 0.0000000E+00 -370.3577     022000\n" +
                        "  0.0000000E+00 0.0000000E+00  386.6757      3102.062     0.0000000E+00 103000\n" +
                        "  0.0000000E+00 0.0000000E+00  178.7889      2190.090     0.0000000E+00 013000\n" +
                        "  -295.6811      1667.364     0.0000000E+00 0.0000000E+00 -110.4189     004000\n" +
                        "  0.0000000E+00 0.0000000E+00  961.8181      7967.303     0.0000000E+00 300100\n" +
                        "  0.0000000E+00 0.0000000E+00  3800.687      40257.04     0.0000000E+00 210100\n" +
                        "  0.0000000E+00 0.0000000E+00  3238.083      52717.54     0.0000000E+00 120100\n" +
                        "  0.0000000E+00 0.0000000E+00 -849.8126      13826.49     0.0000000E+00 030100\n" +
                        "  -721.5404      447.7109     0.0000000E+00 0.0000000E+00 -924.0246     201100\n" +
                        "   3560.950      26169.83     0.0000000E+00 0.0000000E+00 -1930.114     111100\n" +
                        "   6238.277      24547.92     0.0000000E+00 0.0000000E+00 -1719.357     021100\n" +
                        "  0.0000000E+00 0.0000000E+00  2202.030      24302.44     0.0000000E+00 102100\n" +
                        "  0.0000000E+00 0.0000000E+00  568.2025      16780.07     0.0000000E+00 012100\n" +
                        "  -1650.957      15363.05     0.0000000E+00 0.0000000E+00 -892.6146     003100\n" +
                        "  -32.49202     -297.5067     0.0000000E+00 0.0000000E+00  10.65280     300001\n" +
                        "  -213.5859     -961.6012     0.0000000E+00 0.0000000E+00  58.04102     210001\n" +
                        "  -381.6880     -581.0954     0.0000000E+00 0.0000000E+00  68.25452     120001\n" +
                        "  -115.1503      117.8764     0.0000000E+00 0.0000000E+00  10.41294     030001\n" +
                        "  0.0000000E+00 0.0000000E+00  206.4459      539.4694     0.0000000E+00 201001\n" +
                        "  0.0000000E+00 0.0000000E+00  454.5187      2607.354     0.0000000E+00 111001\n" +
                        "  0.0000000E+00 0.0000000E+00  312.3338      2464.736     0.0000000E+00 021001\n" +
                        "  -374.4121     -912.8200     0.0000000E+00 0.0000000E+00  14.15251     102001\n" +
                        "  -420.6478     -686.2673     0.0000000E+00 0.0000000E+00  61.80672     012001\n" +
                        "  0.0000000E+00 0.0000000E+00  16.23030     -351.1444     0.0000000E+00 003001\n" +
                        "  -769.9358     -1912.223     0.0000000E+00 0.0000000E+00 -930.2305     200200\n" +
                        "   3588.512      24150.65     0.0000000E+00 0.0000000E+00 -2076.457     110200\n" +
                        "   6155.762      24135.27     0.0000000E+00 0.0000000E+00 -2032.340     020200\n" +
                        "  0.0000000E+00 0.0000000E+00  4539.336      61905.27     0.0000000E+00 101200\n" +
                        "  0.0000000E+00 0.0000000E+00  356.1279      40952.90     0.0000000E+00 011200\n" +
                        "  -2947.362      52778.61     0.0000000E+00 0.0000000E+00 -2833.789     002200\n" +
                        "  0.0000000E+00 0.0000000E+00  199.5700      845.3875     0.0000000E+00 200101\n" +
                        "  0.0000000E+00 0.0000000E+00  135.0876      3123.166     0.0000000E+00 110101\n" +
                        "  0.0000000E+00 0.0000000E+00 -277.2755      1945.897     0.0000000E+00 020101\n" +
                        "  -1121.550     -4090.177     0.0000000E+00 0.0000000E+00  15.70560     101101\n" +
                        "  -816.7827     -3300.786     0.0000000E+00 0.0000000E+00  149.4781     011101\n" +
                        "  0.0000000E+00 0.0000000E+00  449.4985     -12.10612     0.0000000E+00 002101\n" +
                        "  -6.477559     -37.19008     0.0000000E+00 0.0000000E+00 0.6132035     200002\n" +
                        "  -26.52641     -96.28226     0.0000000E+00 0.0000000E+00  4.688444     110002\n" +
                        "  -29.62759     -54.56148     0.0000000E+00 0.0000000E+00  2.990371     020002\n" +
                        "  0.0000000E+00 0.0000000E+00  53.32032      89.24570     0.0000000E+00 101002\n" +
                        "  0.0000000E+00 0.0000000E+00  2.978589     -66.11645     0.0000000E+00 011002\n" +
                        "  -31.02558      14.06130     0.0000000E+00 0.0000000E+00 -4.259844     002002\n" +
                        "  0.0000000E+00 0.0000000E+00  3245.349      50963.93     0.0000000E+00 100300\n" +
                        "  0.0000000E+00 0.0000000E+00 -197.0859      31939.84     0.0000000E+00 010300\n" +
                        "  -1440.037      80361.97     0.0000000E+00 0.0000000E+00 -4146.149     001300\n" +
                        "  -752.9649     -4404.971     0.0000000E+00 0.0000000E+00 -78.20701     100201\n" +
                        "   26.40633     -3033.509     0.0000000E+00 0.0000000E+00  30.17496     010201\n" +
                        "  0.0000000E+00 0.0000000E+00  1443.122      4445.238     0.0000000E+00 001201\n" +
                        "  0.0000000E+00 0.0000000E+00  58.36439      138.0049     0.0000000E+00 100102\n" +
                        "  0.0000000E+00 0.0000000E+00  5.429394     -20.17698     0.0000000E+00 010102\n" +
                        "  -196.6445      126.2318     0.0000000E+00 0.0000000E+00 -9.981239     001102\n" +
                        " -0.9042965     -1.550162     0.0000000E+00 0.0000000E+00-0.1398675     100003\n" +
                        "  -1.252314    -0.4774147     0.0000000E+00 0.0000000E+00 0.6508917E-01 010003\n" +
                        "  0.0000000E+00 0.0000000E+00  9.787870      31.63760     0.0000000E+00 001003\n" +
                        "   479.9402      45724.89     0.0000000E+00 0.0000000E+00 -2334.737     000400\n" +
                        "  0.0000000E+00 0.0000000E+00  1207.935      6346.908     0.0000000E+00 000301\n" +
                        "  -207.4395      2.967336     0.0000000E+00 0.0000000E+00 -11.85971     000202\n" +
                        "  0.0000000E+00 0.0000000E+00  10.54924      37.39019     0.0000000E+00 000103\n" +
                        " -0.5405670E-01-0.8594821E-01 0.0000000E+00 0.0000000E+00-0.3403591     000004\n" +
                        "  -52.58436      450.2472     0.0000000E+00 0.0000000E+00  163.4655     500000\n" +
                        "  -73.28287      16365.87     0.0000000E+00 0.0000000E+00  483.3917     410000\n" +
                        "   978.3223      94068.37     0.0000000E+00 0.0000000E+00 -1452.279     320000\n" +
                        "   2012.351      202130.5     0.0000000E+00 0.0000000E+00 -6638.979     230000\n" +
                        "   135.5294      178528.9     0.0000000E+00 0.0000000E+00 -7556.616     140000\n" +
                        "  -1242.548      50376.01     0.0000000E+00 0.0000000E+00 -2551.833     050000\n" +
                        "  0.0000000E+00 0.0000000E+00 -695.9625      12466.84     0.0000000E+00 401000\n" +
                        "  0.0000000E+00 0.0000000E+00 -6985.435      14582.30     0.0000000E+00 311000\n" +
                        "  0.0000000E+00 0.0000000E+00 -27302.35     -139780.1     0.0000000E+00 221000\n" +
                        "  0.0000000E+00 0.0000000E+00 -40157.79     -331607.6     0.0000000E+00 131000\n" +
                        "  0.0000000E+00 0.0000000E+00 -19089.30     -189470.2     0.0000000E+00 041000\n" +
                        "   5009.079      31139.73     0.0000000E+00 0.0000000E+00  600.3768     302000\n" +
                        "   14660.40      85584.20     0.0000000E+00 0.0000000E+00  5834.219     212000\n" +
                        "   7529.932      18678.45     0.0000000E+00 0.0000000E+00  10667.98     122000\n" +
                        "  -4921.943     -55013.28     0.0000000E+00 0.0000000E+00  5435.100     032000\n" +
                        "  0.0000000E+00 0.0000000E+00 -6489.165     -58970.31     0.0000000E+00 203000\n" +
                        "  0.0000000E+00 0.0000000E+00 -15749.45     -174355.6     0.0000000E+00 113000\n" +
                        "  0.0000000E+00 0.0000000E+00 -6913.596     -94142.86     0.0000000E+00 023000\n" +
                        "   253.8051     -33463.73     0.0000000E+00 0.0000000E+00 -749.0010     104000\n" +
                        "   870.3169     -48731.51     0.0000000E+00 0.0000000E+00  1612.167     014000\n" +
                        "  0.0000000E+00 0.0000000E+00  1384.757      19608.02     0.0000000E+00 005000\n" +
                        "  0.0000000E+00 0.0000000E+00 -178.6904      31315.79     0.0000000E+00 400100\n" +
                        "  0.0000000E+00 0.0000000E+00 -6999.643      69450.45     0.0000000E+00 310100\n" +
                        "  0.0000000E+00 0.0000000E+00 -37602.42     -185184.7     0.0000000E+00 220100\n" +
                        "  0.0000000E+00 0.0000000E+00 -61539.63     -571757.6     0.0000000E+00 130100\n" +
                        "  0.0000000E+00 0.0000000E+00 -30108.63     -346996.1     0.0000000E+00 040100\n" +
                        "   19949.67      137623.6     0.0000000E+00 0.0000000E+00  2216.455     301100\n" +
                        "   57429.89      410844.4     0.0000000E+00 0.0000000E+00  21539.02     211100\n" +
                        "   26669.01      189938.9     0.0000000E+00 0.0000000E+00  39453.06     121100\n" +
                        "  -24201.07     -171389.5     0.0000000E+00 0.0000000E+00  20208.09     031100\n" +
                        "  0.0000000E+00 0.0000000E+00 -36483.88     -376135.4     0.0000000E+00 202100\n" +
                        "  0.0000000E+00 0.0000000E+00 -94745.72     -1168733.     0.0000000E+00 112100\n" +
                        "  0.0000000E+00 0.0000000E+00 -45385.87     -686340.8     0.0000000E+00 022100\n" +
                        "  -6064.932     -280808.1     0.0000000E+00 0.0000000E+00 -3620.736     103100\n" +
                        "  -8775.789     -424966.6     0.0000000E+00 0.0000000E+00  16990.55     013100\n" +
                        "  0.0000000E+00 0.0000000E+00  10433.62      181860.8     0.0000000E+00 004100\n" +
                        "  -55.88325      100.8632     0.0000000E+00 0.0000000E+00  49.52715     400001\n" +
                        "   178.8927      5514.862     0.0000000E+00 0.0000000E+00 -85.87718     310001\n" +
                        "   2287.907      19703.35     0.0000000E+00 0.0000000E+00 -947.0471     220001\n" +
                        "   4458.752      18322.61     0.0000000E+00 0.0000000E+00 -1346.248     130001\n" +
                        "   2718.896      2368.326     0.0000000E+00 0.0000000E+00 -427.0312     040001\n" +
                        "  0.0000000E+00 0.0000000E+00 -485.2951      8105.514     0.0000000E+00 301001\n" +
                        "  0.0000000E+00 0.0000000E+00 -1819.858      25680.85     0.0000000E+00 211001\n" +
                        "  0.0000000E+00 0.0000000E+00 -2109.425      19924.17     0.0000000E+00 121001\n" +
                        "  0.0000000E+00 0.0000000E+00  1412.296      7383.494     0.0000000E+00 031001\n" +
                        "   2090.013      8529.451     0.0000000E+00 0.0000000E+00 -362.3381     202001\n" +
                        "   6930.700      36580.04     0.0000000E+00 0.0000000E+00 -401.0175     112001\n" +
                        "   3949.557      26792.04     0.0000000E+00 0.0000000E+00 -144.5526     022001\n" +
                        "  0.0000000E+00 0.0000000E+00 -533.3483      11241.91     0.0000000E+00 103001\n" +
                        "  0.0000000E+00 0.0000000E+00 -2219.551     -3771.517     0.0000000E+00 013001\n" +
                        "   549.8919      6008.082     0.0000000E+00 0.0000000E+00 -626.6200     004001\n" +
                        "   19950.87      150096.0     0.0000000E+00 0.0000000E+00  2084.926     300200\n" +
                        "   56938.97      469121.3     0.0000000E+00 0.0000000E+00  19492.37     210200\n" +
                        "   24493.58      281005.2     0.0000000E+00 0.0000000E+00  35337.98     120200\n" +
                        "  -28492.10     -144413.9     0.0000000E+00 0.0000000E+00  17604.00     030200\n" +
                        "  0.0000000E+00 0.0000000E+00 -69172.05     -797589.3     0.0000000E+00 201200\n" +
                        "  0.0000000E+00 0.0000000E+00 -194074.9     -2595980.     0.0000000E+00 111200\n" +
                        "  0.0000000E+00 0.0000000E+00 -102420.5     -1640960.     0.0000000E+00 021200\n" +
                        "  -38928.48     -868675.4     0.0000000E+00 0.0000000E+00 -4334.431     102200\n" +
                        "  -67632.81     -1373806.     0.0000000E+00 0.0000000E+00  62455.69     012200\n" +
                        "  0.0000000E+00 0.0000000E+00  29903.13      661583.2     0.0000000E+00 003200\n" +
                        "  0.0000000E+00 0.0000000E+00 -1387.267      10792.87     0.0000000E+00 300101\n" +
                        "  0.0000000E+00 0.0000000E+00 -7199.253      16387.06     0.0000000E+00 210101\n" +
                        "  0.0000000E+00 0.0000000E+00 -10017.41     -15891.95     0.0000000E+00 120101\n" +
                        "  0.0000000E+00 0.0000000E+00 -21.73854     -10606.66     0.0000000E+00 030101\n" +
                        "   9218.906      35257.22     0.0000000E+00 0.0000000E+00 -667.2240     201101\n" +
                        "   24948.02      126073.3     0.0000000E+00 0.0000000E+00  61.49150     111101\n" +
                        "   11589.51      85589.17     0.0000000E+00 0.0000000E+00  1103.836     021101\n" +
                        "  0.0000000E+00 0.0000000E+00 -3029.410      58889.97     0.0000000E+00 102101\n" +
                        "  0.0000000E+00 0.0000000E+00 -12014.08     -35576.40     0.0000000E+00 012101\n" +
                        "   5984.756      42484.23     0.0000000E+00 0.0000000E+00 -4719.759     003101\n" +
                        "  -19.17142      37.85242     0.0000000E+00 0.0000000E+00  15.00047     300002\n" +
                        "   10.68676      839.0311     0.0000000E+00 0.0000000E+00 -4.604883     210002\n" +
                        "   304.5983      1999.616     0.0000000E+00 0.0000000E+00 -97.74548     120002\n" +
                        "   124.9170      1194.358     0.0000000E+00 0.0000000E+00 -75.79396     030002\n" +
                        "  0.0000000E+00 0.0000000E+00 -398.7065      407.3850     0.0000000E+00 201002\n" +
                        "  0.0000000E+00 0.0000000E+00 -1243.591     -679.4321     0.0000000E+00 111002\n" +
                        "  0.0000000E+00 0.0000000E+00 -1555.074     -4002.806     0.0000000E+00 021002\n" +
                        "   289.9375     -1146.508     0.0000000E+00 0.0000000E+00 -64.66491     102002\n" +
                        "   1000.988     -9.086782     0.0000000E+00 0.0000000E+00 -97.82251     012002\n" +
                        "  0.0000000E+00 0.0000000E+00  213.8863      2072.181     0.0000000E+00 003002\n" +
                        "  0.0000000E+00 0.0000000E+00 -43868.02     -563444.2     0.0000000E+00 200300\n" +
                        "  0.0000000E+00 0.0000000E+00 -133515.0     -1911454.     0.0000000E+00 110300\n" +
                        "  0.0000000E+00 0.0000000E+00 -76697.02     -1288583.     0.0000000E+00 020300\n" +
                        "  -75970.99     -1175919.     0.0000000E+00 0.0000000E+00  1751.394     101300\n" +
                        "  -139613.6     -1950914.     0.0000000E+00 0.0000000E+00  97302.38     011300\n" +
                        "  0.0000000E+00 0.0000000E+00  37705.94      1165304.     0.0000000E+00 002300\n" +
                        "   10082.00      42322.07     0.0000000E+00 0.0000000E+00  228.1592     200201\n" +
                        "   21928.12      113578.4     0.0000000E+00 0.0000000E+00  2134.891     110201\n" +
                        "   7423.177      72939.37     0.0000000E+00 0.0000000E+00  3756.928     020201\n" +
                        "  0.0000000E+00 0.0000000E+00 -7346.734      72069.86     0.0000000E+00 101201\n" +
                        "  0.0000000E+00 0.0000000E+00 -21092.33     -111093.7     0.0000000E+00 011201\n" +
                        "   19391.73      101597.7     0.0000000E+00 0.0000000E+00 -12812.72     002201\n" +
                        "  0.0000000E+00 0.0000000E+00 -581.2549      1015.602     0.0000000E+00 200102\n" +
                        "  0.0000000E+00 0.0000000E+00 -1647.143     -1173.234     0.0000000E+00 110102\n" +
                        "  0.0000000E+00 0.0000000E+00 -1619.307     -5480.129     0.0000000E+00 020102\n" +
                        "   1925.376     -616.5375     0.0000000E+00 0.0000000E+00 -278.2450     101102\n" +
                        "   3982.767      4631.410     0.0000000E+00 0.0000000E+00 -448.0094     011102\n" +
                        "  0.0000000E+00 0.0000000E+00  867.4064      13415.39     0.0000000E+00 002102\n" +
                        "   1.811512      5.065905     0.0000000E+00 0.0000000E+00  3.041414     200003\n" +
                        "   10.31767      48.25623     0.0000000E+00 0.0000000E+00  1.990899     110003\n" +
                        "   46.44585      43.14636     0.0000000E+00 0.0000000E+00 -1.813501     020003\n" +
                        "  0.0000000E+00 0.0000000E+00 -90.82856     -104.9880     0.0000000E+00 101003\n" +
                        "  0.0000000E+00 0.0000000E+00  11.96605      292.3355     0.0000000E+00 011003\n" +
                        "  -30.36499     -80.02962     0.0000000E+00 0.0000000E+00  5.606376     002003\n" +
                        "  -48849.46     -588305.9     0.0000000E+00 0.0000000E+00  4013.128     100400\n" +
                        "  -92848.12     -1027586.     0.0000000E+00 0.0000000E+00  55038.23     010400\n" +
                        "  0.0000000E+00 0.0000000E+00  15539.07      974780.4     0.0000000E+00 001400\n" +
                        "  0.0000000E+00 0.0000000E+00 -6772.555     -2676.177     0.0000000E+00 100301\n" +
                        "  0.0000000E+00 0.0000000E+00 -11925.98     -107989.6     0.0000000E+00 010301\n" +
                        "   24217.08      84256.13     0.0000000E+00 0.0000000E+00 -14369.84     001301\n" +
                        "   2373.149      4458.982     0.0000000E+00 0.0000000E+00 -120.6990     100202\n" +
                        "   3224.417      7709.996     0.0000000E+00 0.0000000E+00 -422.0267     010202\n" +
                        "  0.0000000E+00 0.0000000E+00  344.3113      23289.86     0.0000000E+00 001202\n" +
                        "  0.0000000E+00 0.0000000E+00 -136.1567     -109.3708     0.0000000E+00 100103\n" +
                        "  0.0000000E+00 0.0000000E+00 -12.21779      372.0994     0.0000000E+00 010103\n" +
                        "   94.39039     -607.0637     0.0000000E+00 0.0000000E+00  11.88213     001103\n" +
                        "  0.7063149     0.3237769     0.0000000E+00 0.0000000E+00 0.4546820     100004\n" +
                        " -0.4371515     0.8019732E-01 0.0000000E+00 0.0000000E+00 0.2389261     010004\n" +
                        "  0.0000000E+00 0.0000000E+00 -17.15046     -52.33803     0.0000000E+00 001004\n" +
                        "  0.0000000E+00 0.0000000E+00 -3139.716      298053.4     0.0000000E+00 000500\n" +
                        "   9627.198      6380.619     0.0000000E+00 0.0000000E+00 -5198.978     000401\n" +
                        "  0.0000000E+00 0.0000000E+00 -882.2442      8468.204     0.0000000E+00 000302\n" +
                        "   269.3947     -449.8365     0.0000000E+00 0.0000000E+00  21.59957     000203\n" +
                        "  0.0000000E+00 0.0000000E+00 -24.68681     -76.77508     0.0000000E+00 000104\n" +
                        "  0.7557376E-01 0.7877055E-01 0.0000000E+00 0.0000000E+00 0.3279051     000005"
        )),
        BEFORE_CCT12_2("BEFORE_CCT12_2", DL1 + CCT12_LENGTH + BETWEEN_CCT225, CosyArbitraryOrder.readMap(
                " -0.8217959      2.051592     0.0000000E+00 0.0000000E+00 0.1680138     100000\n" +
                        " -0.9574738      1.173461     0.0000000E+00 0.0000000E+00 0.2290133     010000\n" +
                        "  0.0000000E+00 0.0000000E+00 0.2925919    -0.1458455     0.0000000E+00 001000\n" +
                        "  0.0000000E+00 0.0000000E+00  1.725471      2.557651     0.0000000E+00 000100\n" +
                        "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00  1.000000     000010\n" +
                        " -0.2733335E-01 0.2726842     0.0000000E+00 0.0000000E+00 0.5174318     000001\n" +
                        "   4.412728      14.49318     0.0000000E+00 0.0000000E+00 -1.449234     200000\n" +
                        "   15.56312      7.521148     0.0000000E+00 0.0000000E+00 -3.245274     110000\n" +
                        "   13.96798     -19.37780     0.0000000E+00 0.0000000E+00 -2.562254     020000\n" +
                        "  0.0000000E+00 0.0000000E+00 -27.72622     -67.33492     0.0000000E+00 101000\n" +
                        "  0.0000000E+00 0.0000000E+00 -14.56841     -26.98316     0.0000000E+00 011000\n" +
                        "   7.250688     -3.653894     0.0000000E+00 0.0000000E+00-0.8487730     002000\n" +
                        "  0.0000000E+00 0.0000000E+00 -55.49441     -127.0604     0.0000000E+00 100100\n" +
                        "  0.0000000E+00 0.0000000E+00 -19.54618     -22.03420     0.0000000E+00 010100\n" +
                        "   35.70442     -34.04794     0.0000000E+00 0.0000000E+00 -2.324409     001100\n" +
                        " -0.7019262      2.117869     0.0000000E+00 0.0000000E+00-0.1286316     100001\n" +
                        " -0.2543280E-01  1.528724     0.0000000E+00 0.0000000E+00-0.1592593     010001\n" +
                        "  0.0000000E+00 0.0000000E+00 -1.653018     -6.206318     0.0000000E+00 001001\n" +
                        "   41.92778     -57.63823     0.0000000E+00 0.0000000E+00 -2.704715     000200\n" +
                        "  0.0000000E+00 0.0000000E+00 -6.996988     -18.66251     0.0000000E+00 000101\n" +
                        " -0.7438005E-01 0.2619129E-01 0.0000000E+00 0.0000000E+00-0.4449078     000002\n" +
                        "   20.07613     -91.22242     0.0000000E+00 0.0000000E+00 -3.696730     300000\n" +
                        "   9.579710     -724.9342     0.0000000E+00 0.0000000E+00  8.768169     210000\n" +
                        "  -81.97217     -1327.081     0.0000000E+00 0.0000000E+00  41.04628     120000\n" +
                        "  -93.03134     -652.0347     0.0000000E+00 0.0000000E+00  30.13588     030000\n" +
                        "  0.0000000E+00 0.0000000E+00  58.61272     -37.26528     0.0000000E+00 201000\n" +
                        "  0.0000000E+00 0.0000000E+00  433.0763      814.7464     0.0000000E+00 111000\n" +
                        "  0.0000000E+00 0.0000000E+00  443.4298      1015.780     0.0000000E+00 021000\n" +
                        "  -110.0661     -17.34307     0.0000000E+00 0.0000000E+00  3.641350     102000\n" +
                        "  -212.1370      68.91371     0.0000000E+00 0.0000000E+00  15.24208     012000\n" +
                        "  0.0000000E+00 0.0000000E+00 -145.4857     -414.4297     0.0000000E+00 003000\n" +
                        "  0.0000000E+00 0.0000000E+00  135.1666     -68.75618     0.0000000E+00 200100\n" +
                        "  0.0000000E+00 0.0000000E+00  943.8442      1750.116     0.0000000E+00 110100\n" +
                        "  0.0000000E+00 0.0000000E+00  1023.329      2309.449     0.0000000E+00 020100\n" +
                        "  -462.6058     -208.9772     0.0000000E+00 0.0000000E+00  59.17666     101100\n" +
                        "  -894.3907      40.29714     0.0000000E+00 0.0000000E+00  95.96818     011100\n" +
                        "  0.0000000E+00 0.0000000E+00 -772.2064     -2336.051     0.0000000E+00 002100\n" +
                        "   10.82754      17.85926     0.0000000E+00 0.0000000E+00 -1.581412     200001\n" +
                        "   17.00532     0.8662847     0.0000000E+00 0.0000000E+00 -1.405377     110001\n" +
                        "   4.029578     -2.268047     0.0000000E+00 0.0000000E+00  1.845472     020001\n" +
                        "  0.0000000E+00 0.0000000E+00 -23.17221     -100.0809     0.0000000E+00 101001\n" +
                        "  0.0000000E+00 0.0000000E+00  21.14274      36.09981     0.0000000E+00 011001\n" +
                        "   28.29873     -29.36437     0.0000000E+00 0.0000000E+00-0.4382827     002001\n" +
                        "  -478.9239     -337.2074     0.0000000E+00 0.0000000E+00  103.9170     100200\n" +
                        "  -942.6545     -257.5989     0.0000000E+00 0.0000000E+00  120.6040     010200\n" +
                        "  0.0000000E+00 0.0000000E+00 -1380.408     -4528.468     0.0000000E+00 001200\n" +
                        "  0.0000000E+00 0.0000000E+00  6.465963     -89.81714     0.0000000E+00 100101\n" +
                        "  0.0000000E+00 0.0000000E+00  31.59364      18.49093     0.0000000E+00 010101\n" +
                        "   86.02547     -83.33915     0.0000000E+00 0.0000000E+00 0.3538009     001101\n" +
                        "   1.514691      1.385829     0.0000000E+00 0.0000000E+00-0.2125269E-01 100002\n" +
                        "   1.395473    -0.9464673     0.0000000E+00 0.0000000E+00-0.7490278E-01 010002\n" +
                        "  0.0000000E+00 0.0000000E+00 -6.368363     -18.39820     0.0000000E+00 001002\n" +
                        "  0.0000000E+00 0.0000000E+00 -831.0506     -3028.965     0.0000000E+00 000300\n" +
                        "   50.85901      7.773215     0.0000000E+00 0.0000000E+00  6.815534     000201\n" +
                        "  0.0000000E+00 0.0000000E+00-0.7238686     -9.487189     0.0000000E+00 000102\n" +
                        "  0.8738281E-01 0.6900462E-01 0.0000000E+00 0.0000000E+00 0.3917254     000003\n" +
                        "  -243.8180     -1223.558     0.0000000E+00 0.0000000E+00  60.71127     400000\n" +
                        "  -1384.710     -6027.208     0.0000000E+00 0.0000000E+00  473.3674     310000\n" +
                        "  -2544.667     -8367.613     0.0000000E+00 0.0000000E+00  996.3452     220000\n" +
                        "  -1526.396     -831.7940     0.0000000E+00 0.0000000E+00  667.4861     130000\n" +
                        "  -110.7810      3111.395     0.0000000E+00 0.0000000E+00  55.97849     040000\n" +
                        "  0.0000000E+00 0.0000000E+00  1044.439      3944.014     0.0000000E+00 301000\n" +
                        "  0.0000000E+00 0.0000000E+00  4394.252      18715.22     0.0000000E+00 211000\n" +
                        "  0.0000000E+00 0.0000000E+00  4345.680      22843.37     0.0000000E+00 121000\n" +
                        "  0.0000000E+00 0.0000000E+00 -300.7141      4349.519     0.0000000E+00 031000\n" +
                        "  -71.55076      607.1251     0.0000000E+00 0.0000000E+00 -415.0014     202000\n" +
                        "   1921.692      6782.633     0.0000000E+00 0.0000000E+00 -603.0140     112000\n" +
                        "   2481.643      5815.153     0.0000000E+00 0.0000000E+00 -401.1683     022000\n" +
                        "  0.0000000E+00 0.0000000E+00  851.8268      3102.062     0.0000000E+00 103000\n" +
                        "  0.0000000E+00 0.0000000E+00  507.2670      2190.090     0.0000000E+00 013000\n" +
                        "  -45.58230      1667.364     0.0000000E+00 0.0000000E+00 -116.0446     004000\n" +
                        "  0.0000000E+00 0.0000000E+00  2128.211      7967.303     0.0000000E+00 300100\n" +
                        "  0.0000000E+00 0.0000000E+00  9798.848      40257.04     0.0000000E+00 210100\n" +
                        "  0.0000000E+00 0.0000000E+00  11112.77      52717.54     0.0000000E+00 120100\n" +
                        "  0.0000000E+00 0.0000000E+00  1213.162      13826.49     0.0000000E+00 030100\n" +
                        "  -734.7350      447.7109     0.0000000E+00 0.0000000E+00 -1556.489     201100\n" +
                        "   7401.817      26169.83     0.0000000E+00 0.0000000E+00 -2459.790     111100\n" +
                        "   9899.419      24547.92     0.0000000E+00 0.0000000E+00 -2017.916     021100\n" +
                        "  0.0000000E+00 0.0000000E+00  5856.743      24302.44     0.0000000E+00 102100\n" +
                        "  0.0000000E+00 0.0000000E+00  3088.867      16780.07     0.0000000E+00 012100\n" +
                        "   653.6509      15363.05     0.0000000E+00 0.0000000E+00 -842.7544     003100\n" +
                        "  -64.90340     -297.5067     0.0000000E+00 0.0000000E+00  10.45139     300001\n" +
                        "  -291.2046     -961.6012     0.0000000E+00 0.0000000E+00  72.98980     210001\n" +
                        "  -360.1687     -581.0954     0.0000000E+00 0.0000000E+00  97.27574     120001\n" +
                        "  -45.33699      117.8764     0.0000000E+00 0.0000000E+00  25.17220     030001\n" +
                        "  0.0000000E+00 0.0000000E+00  282.7758      539.4694     0.0000000E+00 201001\n" +
                        "  0.0000000E+00 0.0000000E+00  769.5151      2607.354     0.0000000E+00 111001\n" +
                        "  0.0000000E+00 0.0000000E+00  595.0699      2464.736     0.0000000E+00 021001\n" +
                        "  -510.1232     -912.8200     0.0000000E+00 0.0000000E+00 -15.71972     102001\n" +
                        "  -529.5706     -686.2673     0.0000000E+00 0.0000000E+00  49.94864     012001\n" +
                        "  0.0000000E+00 0.0000000E+00 -1.712570     -351.1444     0.0000000E+00 003001\n" +
                        "  -1204.252     -1912.223     0.0000000E+00 0.0000000E+00 -1465.805     200200\n" +
                        "   7077.812      24150.65     0.0000000E+00 0.0000000E+00 -2574.480     110200\n" +
                        "   9738.768      24135.27     0.0000000E+00 0.0000000E+00 -2616.620     020200\n" +
                        "  0.0000000E+00 0.0000000E+00  13723.14      61905.27     0.0000000E+00 101200\n" +
                        "  0.0000000E+00 0.0000000E+00  6449.197      40952.90     0.0000000E+00 011200\n" +
                        "   4969.450      52778.61     0.0000000E+00 0.0000000E+00 -2454.617     002200\n" +
                        "  0.0000000E+00 0.0000000E+00  317.4170      845.3875     0.0000000E+00 200101\n" +
                        "  0.0000000E+00 0.0000000E+00  443.5810      3123.166     0.0000000E+00 110101\n" +
                        "  0.0000000E+00 0.0000000E+00 -183.7291      1945.897     0.0000000E+00 020101\n" +
                        "  -1736.391     -4090.177     0.0000000E+00 0.0000000E+00 -137.6249     101101\n" +
                        "  -1325.163     -3300.786     0.0000000E+00 0.0000000E+00  86.96946     011101\n" +
                        "  0.0000000E+00 0.0000000E+00  644.2365     -12.10612     0.0000000E+00 002101\n" +
                        "  -12.29503     -37.19008     0.0000000E+00 0.0000000E+00 0.3876189     200002\n" +
                        "  -40.23049     -96.28226     0.0000000E+00 0.0000000E+00  4.916636     110002\n" +
                        "  -39.04978     -54.56148     0.0000000E+00 0.0000000E+00  2.527141     020002\n" +
                        "  0.0000000E+00 0.0000000E+00  70.06927      89.24570     0.0000000E+00 101002\n" +
                        "  0.0000000E+00 0.0000000E+00 -12.06882     -66.11645     0.0000000E+00 011002\n" +
                        "  -26.70442      14.06130     0.0000000E+00 0.0000000E+00 -5.432759     002002\n" +
                        "  0.0000000E+00 0.0000000E+00  10657.56      50963.93     0.0000000E+00 100300\n" +
                        "  0.0000000E+00 0.0000000E+00  4535.510      31939.84     0.0000000E+00 010300\n" +
                        "   10600.78      80361.97     0.0000000E+00 0.0000000E+00 -3376.567     001300\n" +
                        "  -1428.587     -4404.971     0.0000000E+00 0.0000000E+00 -296.0233     100201\n" +
                        "  -426.2502     -3033.509     0.0000000E+00 0.0000000E+00 -11.24566     010201\n" +
                        "  0.0000000E+00 0.0000000E+00  2480.611      4445.238     0.0000000E+00 001201\n" +
                        "  0.0000000E+00 0.0000000E+00  76.39499      138.0049     0.0000000E+00 100102\n" +
                        "  0.0000000E+00 0.0000000E+00 -1.553410     -20.17698     0.0000000E+00 010102\n" +
                        "  -173.8997      126.2318     0.0000000E+00 0.0000000E+00 -16.57835     001102\n" +
                        "  -1.242281     -1.550162     0.0000000E+00 0.0000000E+00-0.2007247     100003\n" +
                        "  -1.213672    -0.4774147     0.0000000E+00 0.0000000E+00 0.7996546E-01 010003\n" +
                        "  0.0000000E+00 0.0000000E+00  15.66829      31.63760     0.0000000E+00 001003\n" +
                        "   7310.396      45724.89     0.0000000E+00 0.0000000E+00 -1825.975     000400\n" +
                        "  0.0000000E+00 0.0000000E+00  2378.249      6346.908     0.0000000E+00 000301\n" +
                        "  -214.3150      2.967336     0.0000000E+00 0.0000000E+00 -32.79699     000202\n" +
                        "  0.0000000E+00 0.0000000E+00  15.55163      37.39019     0.0000000E+00 000103\n" +
                        " -0.8704507E-01-0.8594821E-01 0.0000000E+00 0.0000000E+00-0.3656029     000004\n" +
                        "   27.56854      450.2472     0.0000000E+00 0.0000000E+00  468.9468     500000\n" +
                        "   1758.178      16365.87     0.0000000E+00 0.0000000E+00  2543.141     410000\n" +
                        "   12849.24      94068.37     0.0000000E+00 0.0000000E+00  2491.944     320000\n" +
                        "   29788.02      202130.5     0.0000000E+00 0.0000000E+00 -5196.560     230000\n" +
                        "   25894.78      178528.9     0.0000000E+00 0.0000000E+00 -9735.105     140000\n" +
                        "   6211.102      50376.01     0.0000000E+00 0.0000000E+00 -3912.950     050000\n" +
                        "  0.0000000E+00 0.0000000E+00  863.6289      12466.84     0.0000000E+00 401000\n" +
                        "  0.0000000E+00 0.0000000E+00 -4970.164      14582.30     0.0000000E+00 311000\n" +
                        "  0.0000000E+00 0.0000000E+00 -47393.81     -139780.1     0.0000000E+00 221000\n" +
                        "  0.0000000E+00 0.0000000E+00 -89026.70     -331607.6     0.0000000E+00 131000\n" +
                        "  0.0000000E+00 0.0000000E+00 -47300.28     -189470.2     0.0000000E+00 041000\n" +
                        "   10335.26      31139.73     0.0000000E+00 0.0000000E+00  325.6918     302000\n" +
                        "   28432.24      85584.20     0.0000000E+00 0.0000000E+00  9054.848     212000\n" +
                        "   10790.97      18678.45     0.0000000E+00 0.0000000E+00  16377.55     122000\n" +
                        "  -13089.67     -55013.28     0.0000000E+00 0.0000000E+00  7126.012     032000\n" +
                        "  0.0000000E+00 0.0000000E+00 -15536.85     -58970.31     0.0000000E+00 203000\n" +
                        "  0.0000000E+00 0.0000000E+00 -42096.20     -174355.6     0.0000000E+00 113000\n" +
                        "  0.0000000E+00 0.0000000E+00 -21082.82     -94142.86     0.0000000E+00 023000\n" +
                        "  -4746.401     -33463.73     0.0000000E+00 0.0000000E+00 -3342.082     104000\n" +
                        "  -6427.292     -48731.51     0.0000000E+00 0.0000000E+00  558.7556     014000\n" +
                        "  0.0000000E+00 0.0000000E+00  4323.830      19608.02     0.0000000E+00 005000\n" +
                        "  0.0000000E+00 0.0000000E+00  3901.311      31315.79     0.0000000E+00 400100\n" +
                        "  0.0000000E+00 0.0000000E+00  2665.059      69450.45     0.0000000E+00 310100\n" +
                        "  0.0000000E+00 0.0000000E+00 -65006.69     -185184.7     0.0000000E+00 220100\n" +
                        "  0.0000000E+00 0.0000000E+00 -146917.6     -571757.6     0.0000000E+00 130100\n" +
                        "  0.0000000E+00 0.0000000E+00 -82065.62     -346996.1     0.0000000E+00 040100\n" +
                        "   42216.64      137623.6     0.0000000E+00 0.0000000E+00  721.0607     301100\n" +
                        "   121659.5      410844.4     0.0000000E+00 0.0000000E+00  30013.13     211100\n" +
                        "   58124.00      189938.9     0.0000000E+00 0.0000000E+00  53667.90     121100\n" +
                        "  -48818.34     -171389.5     0.0000000E+00 0.0000000E+00  22350.25     031100\n" +
                        "  0.0000000E+00 0.0000000E+00 -90753.34     -376135.4     0.0000000E+00 202100\n" +
                        "  0.0000000E+00 0.0000000E+00 -268421.0     -1168733.     0.0000000E+00 112100\n" +
                        "  0.0000000E+00 0.0000000E+00 -148139.1     -686340.8     0.0000000E+00 022100\n" +
                        "  -48257.92     -280808.1     0.0000000E+00 0.0000000E+00 -24341.00     103100\n" +
                        "  -72569.51     -424966.6     0.0000000E+00 0.0000000E+00  9375.664     013100\n" +
                        "  0.0000000E+00 0.0000000E+00  37770.96      181860.8     0.0000000E+00 004100\n" +
                        "   75.32506      100.8632     0.0000000E+00 0.0000000E+00  105.9237     400001\n" +
                        "   1365.431      5514.862     0.0000000E+00 0.0000000E+00  179.3092     310001\n" +
                        "   5484.699      19703.35     0.0000000E+00 0.0000000E+00 -671.3792     220001\n" +
                        "   6890.906      18322.61     0.0000000E+00 0.0000000E+00 -1367.799     130001\n" +
                        "   2736.371      2368.326     0.0000000E+00 0.0000000E+00 -498.2314     040001\n" +
                        "  0.0000000E+00 0.0000000E+00  289.4692      8105.514     0.0000000E+00 301001\n" +
                        "  0.0000000E+00 0.0000000E+00  423.2676      25680.85     0.0000000E+00 211001\n" +
                        "  0.0000000E+00 0.0000000E+00 -840.0039      19924.17     0.0000000E+00 121001\n" +
                        "  0.0000000E+00 0.0000000E+00  2251.453      7383.494     0.0000000E+00 031001\n" +
                        "   3504.591      8529.451     0.0000000E+00 0.0000000E+00 -484.6042     202001\n" +
                        "   12021.35      36580.04     0.0000000E+00 0.0000000E+00  313.6667     112001\n" +
                        "   7523.957      26792.04     0.0000000E+00 0.0000000E+00  418.2206     022001\n" +
                        "  0.0000000E+00 0.0000000E+00  849.1470      11241.91     0.0000000E+00 103001\n" +
                        "  0.0000000E+00 0.0000000E+00 -2991.007     -3771.517     0.0000000E+00 013001\n" +
                        "   1314.109      6008.082     0.0000000E+00 0.0000000E+00 -885.3516     004001\n" +
                        "   43059.82      150096.0     0.0000000E+00 0.0000000E+00  152.5400     300200\n" +
                        "   128651.6      469121.3     0.0000000E+00 0.0000000E+00  22739.45     210200\n" +
                        "   70472.33      281005.2     0.0000000E+00 0.0000000E+00  38719.18     120200\n" +
                        "  -48716.69     -144413.9     0.0000000E+00 0.0000000E+00  13003.03     030200\n" +
                        "  0.0000000E+00 0.0000000E+00 -178762.1     -797589.3     0.0000000E+00 201200\n" +
                        "  0.0000000E+00 0.0000000E+00 -576710.3     -2595980.     0.0000000E+00 111200\n" +
                        "  0.0000000E+00 0.0000000E+00 -346597.6     -1640960.     0.0000000E+00 021200\n" +
                        "  -169254.8     -868675.4     0.0000000E+00 0.0000000E+00 -69104.79     102200\n" +
                        "  -273857.8     -1373806.     0.0000000E+00 0.0000000E+00  39971.59     012200\n" +
                        "  0.0000000E+00 0.0000000E+00  128931.5      661583.2     0.0000000E+00 003200\n" +
                        "  0.0000000E+00 0.0000000E+00 -645.8675      10792.87     0.0000000E+00 300101\n" +
                        "  0.0000000E+00 0.0000000E+00 -8245.382      16387.06     0.0000000E+00 210101\n" +
                        "  0.0000000E+00 0.0000000E+00 -16511.90     -15891.95     0.0000000E+00 120101\n" +
                        "  0.0000000E+00 0.0000000E+00 -2645.592     -10606.66     0.0000000E+00 030101\n" +
                        "   15168.41      35257.22     0.0000000E+00 0.0000000E+00 -585.9054     201101\n" +
                        "   42325.82      126073.3     0.0000000E+00 0.0000000E+00  3096.217     111101\n" +
                        "   22707.55      85589.17     0.0000000E+00 0.0000000E+00  3564.290     021101\n" +
                        "  0.0000000E+00 0.0000000E+00  4096.015      58889.97     0.0000000E+00 102101\n" +
                        "  0.0000000E+00 0.0000000E+00 -18645.53     -35576.40     0.0000000E+00 012101\n" +
                        "   11059.39      42484.23     0.0000000E+00 0.0000000E+00 -7041.598     003101\n" +
                        "   8.592980      37.85242     0.0000000E+00 0.0000000E+00  23.22684     300002\n" +
                        "   163.1790      839.0311     0.0000000E+00 0.0000000E+00  13.91160     210002\n" +
                        "   551.4870      1999.616     0.0000000E+00 0.0000000E+00 -102.8678     120002\n" +
                        "   243.5312      1194.358     0.0000000E+00 0.0000000E+00 -94.73636     030002\n" +
                        "  0.0000000E+00 0.0000000E+00 -400.8995      407.3850     0.0000000E+00 201002\n" +
                        "  0.0000000E+00 0.0000000E+00 -1519.038     -679.4321     0.0000000E+00 111002\n" +
                        "  0.0000000E+00 0.0000000E+00 -2289.638     -4002.806     0.0000000E+00 021002\n" +
                        "   209.8607     -1146.508     0.0000000E+00 0.0000000E+00 -151.5037     102002\n" +
                        "   1068.632     -9.086782     0.0000000E+00 0.0000000E+00 -86.45479     012002\n" +
                        "  0.0000000E+00 0.0000000E+00  526.1388      2072.181     0.0000000E+00 003002\n" +
                        "  0.0000000E+00 0.0000000E+00 -118475.6     -563444.2     0.0000000E+00 200300\n" +
                        "  0.0000000E+00 0.0000000E+00 -414360.6     -1911454.     0.0000000E+00 110300\n" +
                        "  0.0000000E+00 0.0000000E+00 -266080.1     -1288583.     0.0000000E+00 020300\n" +
                        "  -251071.8     -1175919.     0.0000000E+00 0.0000000E+00 -91867.47     101300\n" +
                        "  -432285.2     -1950914.     0.0000000E+00 0.0000000E+00  65330.51     011300\n" +
                        "  0.0000000E+00 0.0000000E+00  210069.4      1165304.     0.0000000E+00 002300\n" +
                        "   17331.39      42322.07     0.0000000E+00 0.0000000E+00  796.8759     200201\n" +
                        "   37532.82      113578.4     0.0000000E+00 0.0000000E+00  5542.639     110201\n" +
                        "   16917.25      72939.37     0.0000000E+00 0.0000000E+00  7648.859     020201\n" +
                        "  0.0000000E+00 0.0000000E+00  662.2328      72069.86     0.0000000E+00 101201\n" +
                        "  0.0000000E+00 0.0000000E+00 -40343.79     -111093.7     0.0000000E+00 011201\n" +
                        "   30162.99      101597.7     0.0000000E+00 0.0000000E+00 -20906.89     002201\n" +
                        "  0.0000000E+00 0.0000000E+00 -521.7723      1015.602     0.0000000E+00 200102\n" +
                        "  0.0000000E+00 0.0000000E+00 -1982.621     -1173.234     0.0000000E+00 110102\n" +
                        "  0.0000000E+00 0.0000000E+00 -2433.965     -5480.129     0.0000000E+00 020102\n" +
                        "   2250.788     -616.5375     0.0000000E+00 0.0000000E+00 -433.7393     101102\n" +
                        "   4994.007      4631.410     0.0000000E+00 0.0000000E+00 -261.0590     011102\n" +
                        "  0.0000000E+00 0.0000000E+00  2749.364      13415.39     0.0000000E+00 002102\n" +
                        "   6.403126      5.065905     0.0000000E+00 0.0000000E+00  4.151875     200003\n" +
                        "   25.11651      48.25623     0.0000000E+00 0.0000000E+00  4.027009     110003\n" +
                        "   58.64196      43.14636     0.0000000E+00 0.0000000E+00 0.2846014     020003\n" +
                        "  0.0000000E+00 0.0000000E+00 -118.0720     -104.9880     0.0000000E+00 101003\n" +
                        "  0.0000000E+00 0.0000000E+00  64.57024      292.3355     0.0000000E+00 011003\n" +
                        "  -44.72749     -80.02962     0.0000000E+00 0.0000000E+00 -2.096004     002003\n" +
                        "  -135296.7     -588305.9     0.0000000E+00 0.0000000E+00 -48297.57     100400\n" +
                        "  -247108.8     -1027586.     0.0000000E+00 0.0000000E+00  36957.28     010400\n" +
                        "  0.0000000E+00 0.0000000E+00  156314.1      974780.4     0.0000000E+00 001400\n" +
                        "  0.0000000E+00 0.0000000E+00 -8156.571     -2676.177     0.0000000E+00 100301\n" +
                        "  0.0000000E+00 0.0000000E+00 -30163.30     -107989.6     0.0000000E+00 010301\n" +
                        "   30243.54      84256.13     0.0000000E+00 0.0000000E+00 -27237.62     001301\n" +
                        "   3560.398      4458.982     0.0000000E+00 0.0000000E+00  34.28718     100202\n" +
                        "   4670.099      7709.996     0.0000000E+00 0.0000000E+00 -278.8444     010202\n" +
                        "  0.0000000E+00 0.0000000E+00  3304.646      23289.86     0.0000000E+00 001202\n" +
                        "  0.0000000E+00 0.0000000E+00 -162.2962     -109.3708     0.0000000E+00 100103\n" +
                        "  0.0000000E+00 0.0000000E+00  47.55808      372.0994     0.0000000E+00 010103\n" +
                        "  -7.510584     -607.0637     0.0000000E+00 0.0000000E+00 -21.40023     001103\n" +
                        "   1.007927     0.3237769     0.0000000E+00 0.0000000E+00 0.5696418     100004\n" +
                        " -0.4745814     0.8019732E-01 0.0000000E+00 0.0000000E+00 0.2261033     010004\n" +
                        "  0.0000000E+00 0.0000000E+00 -28.52855     -52.33803     0.0000000E+00 001004\n" +
                        "  0.0000000E+00 0.0000000E+00  37753.53      298053.4     0.0000000E+00 000500\n" +
                        "   7103.270      6380.619     0.0000000E+00 0.0000000E+00 -12997.27     000401\n" +
                        "  0.0000000E+00 0.0000000E+00 -57.26182      8468.204     0.0000000E+00 000302\n" +
                        "   216.1613     -449.8365     0.0000000E+00 0.0000000E+00  28.36704     000203\n" +
                        "  0.0000000E+00 0.0000000E+00 -38.72586     -76.77508     0.0000000E+00 000104\n" +
                        "  0.1133736     0.7877055E-01 0.0000000E+00 0.0000000E+00 0.3548717     000005"
        )),
        AFTER_CCT12_2("AFTER_CCT12_2", DL1 + CCT12_LENGTH + BETWEEN_CCT225 + CCT12_LENGTH, CosyArbitraryOrder.readMap(
                " -0.6821685      1.427547     0.0000000E+00 0.0000000E+00 0.8509655E-02 100000\n" +
                        "  -1.176683     0.9964840     0.0000000E+00 0.0000000E+00-0.2095900E-04 010000\n" +
                        "  0.0000000E+00 0.0000000E+00 0.4740570E-01-0.8864910     0.0000000E+00 001000\n" +
                        "  0.0000000E+00 0.0000000E+00  1.181252    -0.9950191     0.0000000E+00 000100\n" +
                        "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00  1.000000     000010\n" +
                        "  0.1002746E-01-0.8509655E-02 0.0000000E+00 0.0000000E+00 0.5742891     000001\n" +
                        "   11.38748      6.331251     0.0000000E+00 0.0000000E+00 0.2238419     200000\n" +
                        "   26.35003    -0.3118733E-01 0.0000000E+00 0.0000000E+00  1.236098     110000\n" +
                        "   15.49303     -13.20303     0.0000000E+00 0.0000000E+00 0.4563769     020000\n" +
                        "  0.0000000E+00 0.0000000E+00 -26.33140     -17.80921     0.0000000E+00 101000\n" +
                        "  0.0000000E+00 0.0000000E+00 -11.98962     0.4386352E-01 0.0000000E+00 011000\n" +
                        "   10.60553     -4.465888     0.0000000E+00 0.0000000E+00  1.012153     002000\n" +
                        "  0.0000000E+00 0.0000000E+00 -51.67810     -30.06492     0.0000000E+00 100100\n" +
                        "  0.0000000E+00 0.0000000E+00 -14.04758      12.12853     0.0000000E+00 010100\n" +
                        "   47.48060     -30.11480     0.0000000E+00 0.0000000E+00  6.266503     001100\n" +
                        " -0.5411523      1.318478     0.0000000E+00 0.0000000E+00-0.2185443     100001\n" +
                        "  0.5676102     0.2959543     0.0000000E+00 0.0000000E+00-0.5053174E-02 010001\n" +
                        "  0.0000000E+00 0.0000000E+00 -1.991569     -3.540976     0.0000000E+00 001001\n" +
                        "   51.26655     -43.56392     0.0000000E+00 0.0000000E+00  6.660082     000200\n" +
                        "  0.0000000E+00 0.0000000E+00 -6.650246     -5.675494     0.0000000E+00 000101\n" +
                        " -0.1292645     0.1040081     0.0000000E+00 0.0000000E+00-0.5176045     000002\n" +
                        "   16.50301     -87.93142     0.0000000E+00 0.0000000E+00-0.8253009     300000\n" +
                        "  -165.5216     -526.4756     0.0000000E+00 0.0000000E+00 -5.725575     210000\n" +
                        "  -489.9371     -853.0228     0.0000000E+00 0.0000000E+00 -14.17218     120000\n" +
                        "  -328.7889     -380.5027     0.0000000E+00 0.0000000E+00 -13.68838     030000\n" +
                        "  0.0000000E+00 0.0000000E+00  36.36296     -80.58674     0.0000000E+00 201000\n" +
                        "  0.0000000E+00 0.0000000E+00  400.1109      153.9762     0.0000000E+00 111000\n" +
                        "  0.0000000E+00 0.0000000E+00  425.2957      271.9173     0.0000000E+00 021000\n" +
                        "  -180.8406     -9.176141     0.0000000E+00 0.0000000E+00 -25.30923     102000\n" +
                        "  -308.0177      49.88027     0.0000000E+00 0.0000000E+00 -35.80190     012000\n" +
                        "  0.0000000E+00 0.0000000E+00 -147.2409     -149.0415     0.0000000E+00 003000\n" +
                        "  0.0000000E+00 0.0000000E+00  88.28991     -181.0911     0.0000000E+00 200100\n" +
                        "  0.0000000E+00 0.0000000E+00  871.3502      302.9590     0.0000000E+00 110100\n" +
                        "  0.0000000E+00 0.0000000E+00  971.6944      586.0520     0.0000000E+00 020100\n" +
                        "  -838.5282     -147.6474     0.0000000E+00 0.0000000E+00 -61.72439     101100\n" +
                        "  -1388.994      60.49662     0.0000000E+00 0.0000000E+00 -126.3278     011100\n" +
                        "  0.0000000E+00 0.0000000E+00 -800.7768     -909.7498     0.0000000E+00 002100\n" +
                        "   18.75555      14.44538     0.0000000E+00 0.0000000E+00 0.4823433     200001\n" +
                        "   21.12256      8.867219     0.0000000E+00 0.0000000E+00 0.3569525     110001\n" +
                        "   5.490385     -2.156366     0.0000000E+00 0.0000000E+00  2.146845     020001\n" +
                        "  0.0000000E+00 0.0000000E+00 -34.11336     -69.72959     0.0000000E+00 101001\n" +
                        "  0.0000000E+00 0.0000000E+00  15.43671     -4.476409     0.0000000E+00 011001\n" +
                        "   34.07079     -21.32234     0.0000000E+00 0.0000000E+00  5.337083     002001\n" +
                        "  -941.0590     -251.9312     0.0000000E+00 0.0000000E+00 -20.33770     100200\n" +
                        "  -1557.569     -87.67372     0.0000000E+00 0.0000000E+00 -125.4759     010200\n" +
                        "  0.0000000E+00 0.0000000E+00 -1482.197     -1926.820     0.0000000E+00 001200\n" +
                        "  0.0000000E+00 0.0000000E+00 -20.92263     -119.3417     0.0000000E+00 100101\n" +
                        "  0.0000000E+00 0.0000000E+00  19.29283     -31.06572     0.0000000E+00 010101\n" +
                        "   100.3606     -61.94243     0.0000000E+00 0.0000000E+00  17.73006     001101\n" +
                        "   2.715817     0.4493407     0.0000000E+00 0.0000000E+00 0.4347885     100002\n" +
                        "   1.420116     0.4385717     0.0000000E+00 0.0000000E+00 0.4997678E-01 010002\n" +
                        "  0.0000000E+00 0.0000000E+00 -6.702134     -7.829004     0.0000000E+00 001002\n" +
                        "  0.0000000E+00 0.0000000E+00 -939.6774     -1425.353     0.0000000E+00 000300\n" +
                        "   64.86409     -13.25229     0.0000000E+00 0.0000000E+00  19.31105     000201\n" +
                        "  0.0000000E+00 0.0000000E+00 -3.469810     -12.56393     0.0000000E+00 000102\n" +
                        "  0.1973855    -0.1025468     0.0000000E+00 0.0000000E+00 0.4780155     000003\n" +
                        "  -840.9927     -144.8476     0.0000000E+00 0.0000000E+00 -102.0373     400000\n" +
                        "  -4719.825     -353.6271     0.0000000E+00 0.0000000E+00 -308.8082     310000\n" +
                        "  -8365.506      2069.318     0.0000000E+00 0.0000000E+00 -365.4913     220000\n" +
                        "  -4535.207      6346.577     0.0000000E+00 0.0000000E+00 -229.4684     130000\n" +
                        "   71.68367      4324.043     0.0000000E+00 0.0000000E+00 -89.09583     040000\n" +
                        "  0.0000000E+00 0.0000000E+00  1092.904      1810.375     0.0000000E+00 301000\n" +
                        "  0.0000000E+00 0.0000000E+00  5014.032      8973.302     0.0000000E+00 211000\n" +
                        "  0.0000000E+00 0.0000000E+00  5468.381      11871.05     0.0000000E+00 121000\n" +
                        "  0.0000000E+00 0.0000000E+00  179.6402      3323.460     0.0000000E+00 031000\n" +
                        "   224.7405      2008.111     0.0000000E+00 0.0000000E+00 -563.5676     202000\n" +
                        "   4855.759      6842.382     0.0000000E+00 0.0000000E+00 -155.9069     112000\n" +
                        "   5348.414      4893.396     0.0000000E+00 0.0000000E+00  276.7968     022000\n" +
                        "  0.0000000E+00 0.0000000E+00  863.6593      1327.030     0.0000000E+00 103000\n" +
                        "  0.0000000E+00 0.0000000E+00  596.7210      1214.051     0.0000000E+00 013000\n" +
                        "   331.6323      1097.355     0.0000000E+00 0.0000000E+00 -92.33640     004000\n" +
                        "  0.0000000E+00 0.0000000E+00  2222.498      3645.303     0.0000000E+00 300100\n" +
                        "  0.0000000E+00 0.0000000E+00  10992.44      18967.08     0.0000000E+00 210100\n" +
                        "  0.0000000E+00 0.0000000E+00  13234.49      26308.94     0.0000000E+00 120100\n" +
                        "  0.0000000E+00 0.0000000E+00  2029.798      8361.178     0.0000000E+00 030100\n" +
                        "  -626.9874      7815.692     0.0000000E+00 0.0000000E+00 -2336.804     201100\n" +
                        "   18361.92      28892.92     0.0000000E+00 0.0000000E+00 -868.1150     111100\n" +
                        "   21879.77      21986.08     0.0000000E+00 0.0000000E+00  573.9045     021100\n" +
                        "  0.0000000E+00 0.0000000E+00  6450.741      11656.74     0.0000000E+00 102100\n" +
                        "  0.0000000E+00 0.0000000E+00  4067.767      10150.32     0.0000000E+00 012100\n" +
                        "   4678.122      9484.888     0.0000000E+00 0.0000000E+00 -286.3592     003100\n" +
                        "  -159.2289     -194.0607     0.0000000E+00 0.0000000E+00 -17.20605     300001\n" +
                        "  -587.4744     -707.7945     0.0000000E+00 0.0000000E+00 -10.21122     210001\n" +
                        "  -483.2356     -633.9828     0.0000000E+00 0.0000000E+00  44.77795     120001\n" +
                        "   99.88279     -167.9445     0.0000000E+00 0.0000000E+00  61.74168     030001\n" +
                        "  0.0000000E+00 0.0000000E+00  268.3334      146.4883     0.0000000E+00 201001\n" +
                        "  0.0000000E+00 0.0000000E+00  926.9760      1431.484     0.0000000E+00 111001\n" +
                        "  0.0000000E+00 0.0000000E+00  800.6168      1537.780     0.0000000E+00 021001\n" +
                        "  -970.2692     -434.6847     0.0000000E+00 0.0000000E+00 -178.6573     102001\n" +
                        "  -924.0886     -400.3356     0.0000000E+00 0.0000000E+00 -91.06306     012001\n" +
                        "  0.0000000E+00 0.0000000E+00 -90.15489     -445.5917     0.0000000E+00 003001\n" +
                        "  -2242.790      7277.568     0.0000000E+00 0.0000000E+00 -2437.747     200200\n" +
                        "   17086.82      29689.19     0.0000000E+00 0.0000000E+00 -1208.241     110200\n" +
                        "   21877.67      23803.28     0.0000000E+00 0.0000000E+00 -274.8475     020200\n" +
                        "  0.0000000E+00 0.0000000E+00  16142.61      32265.81     0.0000000E+00 101200\n" +
                        "  0.0000000E+00 0.0000000E+00  9219.773      26125.37     0.0000000E+00 011200\n" +
                        "   20360.71      30972.37     0.0000000E+00 0.0000000E+00  311.9851     002200\n" +
                        "  0.0000000E+00 0.0000000E+00  337.1668      384.1680     0.0000000E+00 200101\n" +
                        "  0.0000000E+00 0.0000000E+00  849.6364      2536.139     0.0000000E+00 110101\n" +
                        "  0.0000000E+00 0.0000000E+00  370.2131      2549.190     0.0000000E+00 020101\n" +
                        "  -3409.093     -1910.895     0.0000000E+00 0.0000000E+00 -734.0067     101101\n" +
                        "  -2522.730     -2020.139     0.0000000E+00 0.0000000E+00 -281.0679     011101\n" +
                        "  0.0000000E+00 0.0000000E+00  189.5425     -1714.489     0.0000000E+00 002101\n" +
                        "  -30.53110     -20.67725     0.0000000E+00 0.0000000E+00 -4.535358     200002\n" +
                        "  -85.14471     -67.17469     0.0000000E+00 0.0000000E+00 -6.693048     110002\n" +
                        "  -75.62131     -24.14741     0.0000000E+00 0.0000000E+00 -9.601980     020002\n" +
                        "  0.0000000E+00 0.0000000E+00  59.59757     -16.04619     0.0000000E+00 101002\n" +
                        "  0.0000000E+00 0.0000000E+00 -8.251791     -13.84549     0.0000000E+00 011002\n" +
                        "  -39.88686      21.96444     0.0000000E+00 0.0000000E+00 -14.57456     002002\n" +
                        "  0.0000000E+00 0.0000000E+00  13223.63      28380.30     0.0000000E+00 100300\n" +
                        "  0.0000000E+00 0.0000000E+00  6781.151      20901.01     0.0000000E+00 010300\n" +
                        "   35988.97      45252.66     0.0000000E+00 0.0000000E+00  1792.245     001300\n" +
                        "  -2823.280     -1943.266     0.0000000E+00 0.0000000E+00 -843.0224     100201\n" +
                        "  -972.3473     -2185.828     0.0000000E+00 0.0000000E+00 -103.1319     010201\n" +
                        "  0.0000000E+00 0.0000000E+00  1822.261     -1091.937     0.0000000E+00 001201\n" +
                        "  0.0000000E+00 0.0000000E+00  88.27002      56.34811     0.0000000E+00 100102\n" +
                        "  0.0000000E+00 0.0000000E+00  3.403076      1.882543     0.0000000E+00 010102\n" +
                        "  -238.2660      141.8831     0.0000000E+00 0.0000000E+00 -67.93711     001102\n" +
                        "  -3.097674      1.013061     0.0000000E+00 0.0000000E+00-0.9136181     100003\n" +
                        "  -1.717193     -1.472329     0.0000000E+00 0.0000000E+00-0.7113392E-01 010003\n" +
                        "  0.0000000E+00 0.0000000E+00  13.14134      1.325671     0.0000000E+00 001003\n" +
                        "   22668.59      24892.82     0.0000000E+00 0.0000000E+00  1517.838     000400\n" +
                        "  0.0000000E+00 0.0000000E+00  2233.766      1372.138     0.0000000E+00 000301\n" +
                        "  -314.8602      128.4307     0.0000000E+00 0.0000000E+00 -105.3135     000202\n" +
                        "  0.0000000E+00 0.0000000E+00  17.18100      14.81875     0.0000000E+00 000103\n" +
                        " -0.2472591     0.2130398     0.0000000E+00 0.0000000E+00-0.4720663     000004\n" +
                        "  -887.0719      3667.409     0.0000000E+00 0.0000000E+00  478.5112     500000\n" +
                        "   2943.635      15111.86     0.0000000E+00 0.0000000E+00  5482.997     410000\n" +
                        "   44901.27      31912.92     0.0000000E+00 0.0000000E+00  16728.37     320000\n" +
                        "   118588.2      41407.52     0.0000000E+00 0.0000000E+00  20561.72     230000\n" +
                        "   114253.4      21513.83     0.0000000E+00 0.0000000E+00  10541.48     140000\n" +
                        "   33817.26     -1604.579     0.0000000E+00 0.0000000E+00  1564.118     050000\n" +
                        "  0.0000000E+00 0.0000000E+00  2176.856      7563.191     0.0000000E+00 401000\n" +
                        "  0.0000000E+00 0.0000000E+00  1164.970      10160.26     0.0000000E+00 311000\n" +
                        "  0.0000000E+00 0.0000000E+00 -41713.65     -64490.31     0.0000000E+00 221000\n" +
                        "  0.0000000E+00 0.0000000E+00 -94314.25     -157723.7     0.0000000E+00 131000\n" +
                        "  0.0000000E+00 0.0000000E+00 -53294.28     -90687.69     0.0000000E+00 041000\n" +
                        "   28094.62     -849.2452     0.0000000E+00 0.0000000E+00  5583.543     302000\n" +
                        "   78191.94     -48752.55     0.0000000E+00 0.0000000E+00  27671.77     212000\n" +
                        "   37946.21     -143329.3     0.0000000E+00 0.0000000E+00  33788.00     122000\n" +
                        "  -25893.70     -105310.7     0.0000000E+00 0.0000000E+00  8954.301     032000\n" +
                        "  0.0000000E+00 0.0000000E+00 -17759.37     -29372.59     0.0000000E+00 203000\n" +
                        "  0.0000000E+00 0.0000000E+00 -47834.09     -85050.25     0.0000000E+00 113000\n" +
                        "  0.0000000E+00 0.0000000E+00 -23544.65     -46418.97     0.0000000E+00 023000\n" +
                        "  -9823.769     -21438.37     0.0000000E+00 0.0000000E+00 -5573.494     104000\n" +
                        "  -17963.30     -39009.09     0.0000000E+00 0.0000000E+00 -1507.922     014000\n" +
                        "  0.0000000E+00 0.0000000E+00  5292.137      10144.18     0.0000000E+00 005000\n" +
                        "  0.0000000E+00 0.0000000E+00  6175.056      16923.32     0.0000000E+00 400100\n" +
                        "  0.0000000E+00 0.0000000E+00  13708.48      34316.41     0.0000000E+00 310100\n" +
                        "  0.0000000E+00 0.0000000E+00 -56147.17     -95767.69     0.0000000E+00 220100\n" +
                        "  0.0000000E+00 0.0000000E+00 -161001.2     -287197.4     0.0000000E+00 130100\n" +
                        "  0.0000000E+00 0.0000000E+00 -96349.09     -174980.8     0.0000000E+00 040100\n" +
                        "   118036.5      140.4126     0.0000000E+00 0.0000000E+00  23466.27     301100\n" +
                        "   355232.2     -177456.5     0.0000000E+00 0.0000000E+00  110851.0     211100\n" +
                        "   230802.0     -549237.0     0.0000000E+00 0.0000000E+00  132498.1     121100\n" +
                        "  -71752.22     -414421.2     0.0000000E+00 0.0000000E+00  32542.00     031100\n" +
                        "  0.0000000E+00 0.0000000E+00 -111024.2     -216856.9     0.0000000E+00 202100\n" +
                        "  0.0000000E+00 0.0000000E+00 -314924.7     -608165.9     0.0000000E+00 112100\n" +
                        "  0.0000000E+00 0.0000000E+00 -171099.3     -354066.7     0.0000000E+00 022100\n" +
                        "  -96397.47     -191688.9     0.0000000E+00 0.0000000E+00 -44229.57     103100\n" +
                        "  -183928.5     -352563.1     0.0000000E+00 0.0000000E+00 -11544.42     013100\n" +
                        "  0.0000000E+00 0.0000000E+00  48702.46      98102.05     0.0000000E+00 004100\n" +
                        "   33.15832      1040.099     0.0000000E+00 0.0000000E+00  122.2631     400001\n" +
                        "   3414.495      5306.049     0.0000000E+00 0.0000000E+00  996.9696     310001\n" +
                        "   14283.42      10056.74     0.0000000E+00 0.0000000E+00  2333.647     220001\n" +
                        "   16283.20      5192.586     0.0000000E+00 0.0000000E+00  2043.003     130001\n" +
                        "   4993.786     -2055.627     0.0000000E+00 0.0000000E+00  732.0327     040001\n" +
                        "  0.0000000E+00 0.0000000E+00  1388.734      7242.039     0.0000000E+00 301001\n" +
                        "  0.0000000E+00 0.0000000E+00  4595.272      23706.49     0.0000000E+00 211001\n" +
                        "  0.0000000E+00 0.0000000E+00  3248.062      20431.70     0.0000000E+00 121001\n" +
                        "  0.0000000E+00 0.0000000E+00  2549.100      3138.246     0.0000000E+00 031001\n" +
                        "   6924.702      9347.293     0.0000000E+00 0.0000000E+00  495.4290     202001\n" +
                        "   23623.11      29923.68     0.0000000E+00 0.0000000E+00  3874.601     112001\n" +
                        "   15352.02      20214.08     0.0000000E+00 0.0000000E+00  2666.712     022001\n" +
                        "  0.0000000E+00 0.0000000E+00  1922.626      8235.889     0.0000000E+00 103001\n" +
                        "  0.0000000E+00 0.0000000E+00 -2521.195      1219.885     0.0000000E+00 013001\n" +
                        "   3607.751      5065.556     0.0000000E+00 0.0000000E+00 -589.4277     004001\n" +
                        "   123206.5      4526.825     0.0000000E+00 0.0000000E+00  24469.98     300200\n" +
                        "   393565.5     -159358.4     0.0000000E+00 0.0000000E+00  108725.3     210200\n" +
                        "   300713.4     -525561.3     0.0000000E+00 0.0000000E+00  123871.3     120200\n" +
                        "  -49388.46     -407088.8     0.0000000E+00 0.0000000E+00  23059.33     030200\n" +
                        "  0.0000000E+00 0.0000000E+00 -233657.5     -513989.4     0.0000000E+00 201200\n" +
                        "  0.0000000E+00 0.0000000E+00 -693858.6     -1411551.     0.0000000E+00 111200\n" +
                        "  0.0000000E+00 0.0000000E+00 -412172.4     -877929.7     0.0000000E+00 021200\n" +
                        "  -324401.5     -630241.1     0.0000000E+00 0.0000000E+00 -133322.1     102200\n" +
                        "  -655054.3     -1179977.     0.0000000E+00 0.0000000E+00 -34079.68     012200\n" +
                        "  0.0000000E+00 0.0000000E+00  177896.1      375983.1     0.0000000E+00 003200\n" +
                        "  0.0000000E+00 0.0000000E+00  1393.214      11700.30     0.0000000E+00 300101\n" +
                        "  0.0000000E+00 0.0000000E+00 -462.5062      32660.93     0.0000000E+00 210101\n" +
                        "  0.0000000E+00 0.0000000E+00 -8553.042      19462.21     0.0000000E+00 120101\n" +
                        "  0.0000000E+00 0.0000000E+00 -1115.668     -1180.417     0.0000000E+00 030101\n" +
                        "   29349.91      31292.31     0.0000000E+00 0.0000000E+00  4400.283     201101\n" +
                        "   79848.58      97114.33     0.0000000E+00 0.0000000E+00  16287.89     111101\n" +
                        "   43737.31      60749.70     0.0000000E+00 0.0000000E+00  10808.53     021101\n" +
                        "  0.0000000E+00 0.0000000E+00  9320.347      41700.38     0.0000000E+00 102101\n" +
                        "  0.0000000E+00 0.0000000E+00 -17816.81     -4133.364     0.0000000E+00 012101\n" +
                        "   28263.91      38599.39     0.0000000E+00 0.0000000E+00 -4958.512     003101\n" +
                        "   32.78899      133.2571     0.0000000E+00 0.0000000E+00  28.39814     300002\n" +
                        "   395.4785      1104.779     0.0000000E+00 0.0000000E+00  57.27409     210002\n" +
                        "   1103.067      2229.723     0.0000000E+00 0.0000000E+00  6.496163     120002\n" +
                        "   431.6224      1550.184     0.0000000E+00 0.0000000E+00 -107.7075     030002\n" +
                        "  0.0000000E+00 0.0000000E+00 -190.9194      1009.307     0.0000000E+00 201002\n" +
                        "  0.0000000E+00 0.0000000E+00 -1074.890      1730.773     0.0000000E+00 111002\n" +
                        "  0.0000000E+00 0.0000000E+00 -1992.390     -38.03287     0.0000000E+00 021002\n" +
                        "   462.9793     -761.4696     0.0000000E+00 0.0000000E+00 -75.80277     102002\n" +
                        "   1851.155     -319.7556     0.0000000E+00 0.0000000E+00  256.8832     012002\n" +
                        "  0.0000000E+00 0.0000000E+00  658.3019      1052.659     0.0000000E+00 003002\n" +
                        "  0.0000000E+00 0.0000000E+00 -165321.6     -396978.4     0.0000000E+00 200300\n" +
                        "  0.0000000E+00 0.0000000E+00 -509727.1     -1074844.     0.0000000E+00 110300\n" +
                        "  0.0000000E+00 0.0000000E+00 -327865.9     -720045.1     0.0000000E+00 020300\n" +
                        "  -456998.2     -905405.1     0.0000000E+00 0.0000000E+00 -181902.3     101300\n" +
                        "  -990438.9     -1733876.     0.0000000E+00 0.0000000E+00 -46466.64     011300\n" +
                        "  0.0000000E+00 0.0000000E+00  318574.3      707787.0     0.0000000E+00 002300\n" +
                        "   34365.84      27306.52     0.0000000E+00 0.0000000E+00  7282.591     200201\n" +
                        "   68644.60      75773.87     0.0000000E+00 0.0000000E+00  18298.99     110201\n" +
                        "   29617.44      38764.15     0.0000000E+00 0.0000000E+00  14630.01     020201\n" +
                        "  0.0000000E+00 0.0000000E+00  5918.424      48621.52     0.0000000E+00 101201\n" +
                        "  0.0000000E+00 0.0000000E+00 -43751.47     -40991.64     0.0000000E+00 011201\n" +
                        "   73355.66      106941.7     0.0000000E+00 0.0000000E+00 -17171.28     002201\n" +
                        "  0.0000000E+00 0.0000000E+00 -229.5741      1641.431     0.0000000E+00 200102\n" +
                        "  0.0000000E+00 0.0000000E+00 -1631.043      1585.964     0.0000000E+00 110102\n" +
                        "  0.0000000E+00 0.0000000E+00 -2724.523     -2069.433     0.0000000E+00 020102\n" +
                        "   4769.180     -1423.742     0.0000000E+00 0.0000000E+00  448.0426     101102\n" +
                        "   9264.978      1425.794     0.0000000E+00 0.0000000E+00  1402.021     011102\n" +
                        "  0.0000000E+00 0.0000000E+00  3941.992      8159.645     0.0000000E+00 002102\n" +
                        "   23.76589     -17.51918     0.0000000E+00 0.0000000E+00  9.941671     200003\n" +
                        "   73.29960      10.46593     0.0000000E+00 0.0000000E+00  16.99481     110003\n" +
                        "   123.2180     -26.66882     0.0000000E+00 0.0000000E+00  25.73460     020003\n" +
                        "  0.0000000E+00 0.0000000E+00 -74.58871      111.2871     0.0000000E+00 101003\n" +
                        "  0.0000000E+00 0.0000000E+00  62.66365      105.0913     0.0000000E+00 011003\n" +
                        "  -66.88495     -37.49885     0.0000000E+00 0.0000000E+00 -14.55539     002003\n" +
                        "  -230726.6     -480768.1     0.0000000E+00 0.0000000E+00 -94867.57     100400\n" +
                        "  -545457.7     -944116.5     0.0000000E+00 0.0000000E+00 -24898.24     010400\n" +
                        "  0.0000000E+00 0.0000000E+00  275412.2      648785.2     0.0000000E+00 001400\n" +
                        "  0.0000000E+00 0.0000000E+00 -10483.58     -6202.519     0.0000000E+00 100301\n" +
                        "  0.0000000E+00 0.0000000E+00 -35381.03     -51648.26     0.0000000E+00 010301\n" +
                        "   68611.76      124464.4     0.0000000E+00 0.0000000E+00 -27919.14     001301\n" +
                        "   7518.775      430.1448     0.0000000E+00 0.0000000E+00  1560.847     100202\n" +
                        "   9020.135      4515.839     0.0000000E+00 0.0000000E+00  1175.635     010202\n" +
                        "  0.0000000E+00 0.0000000E+00  6086.851      16943.58     0.0000000E+00 001202\n" +
                        "  0.0000000E+00 0.0000000E+00 -130.2641      114.9232     0.0000000E+00 100103\n" +
                        "  0.0000000E+00 0.0000000E+00  71.65165      222.7418     0.0000000E+00 010103\n" +
                        "  -62.21043     -354.3483     0.0000000E+00 0.0000000E+00 -27.72192     001103\n" +
                        "   3.333346     -4.486176     0.0000000E+00 0.0000000E+00  1.649577     100004\n" +
                        " -0.6194738      1.537968     0.0000000E+00 0.0000000E+00-0.7629358E-01 010004\n" +
                        "  0.0000000E+00 0.0000000E+00 -19.95736      10.06869     0.0000000E+00 001004\n" +
                        "  0.0000000E+00 0.0000000E+00  89613.22      228701.8     0.0000000E+00 000500\n" +
                        "   12079.72      48988.94     0.0000000E+00 0.0000000E+00 -17362.04     000401\n" +
                        "  0.0000000E+00 0.0000000E+00  1375.589      8023.569     0.0000000E+00 000302\n" +
                        "   308.5534     -503.3922     0.0000000E+00 0.0000000E+00  124.0922     000203\n" +
                        "  0.0000000E+00 0.0000000E+00 -34.16507     -5.793614     0.0000000E+00 000104\n" +
                        "  0.3427993    -0.4168399     0.0000000E+00 0.0000000E+00 0.5011248     000005"
        )),
        END("END", DL1 + CCT12_LENGTH + BETWEEN_CCT225 + CCT12_LENGTH + DL1, CosyArbitraryOrder.readMap(
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
                        " -0.3811689    -0.4168399     0.0000000E+00 0.0000000E+00 0.6493005     000005\n"
        ));


        public static CosyMap第一段[] getAll() {
            return new CosyMap第一段[]{
                    COUPLING_POINT,
                    BEFORE_CCT12_1,
                    AFTER_CCT12_1,
                    AFTER_QS1_1,
                    AFTER_QS2,
                    AFTER_QS1_2,
                    BEFORE_CCT12_2,
                    AFTER_CCT12_2,
                    END
            };
        }

        String name;
        double distance;
        CosyArbitraryOrder.CosyMapArbitraryOrder map;

        CosyMap第一段(String name, double distance, CosyArbitraryOrder.CosyMapArbitraryOrder map) {
            this.name = name;
            this.distance = distance;
            this.map = map;
        }

        public String getName() {
            return name;
        }

        public double getDistance() {
            return distance;
        }

        public CosyArbitraryOrder.CosyMapArbitraryOrder getMap() {
            return map;
        }

        public static void drawElement(double cctHeight, double QsHeight, String cctDescribe, String QsDescribe) {
            Plot2d.plotBox(DL1, DL1 + CCT12_LENGTH, -cctHeight, cctHeight, cctDescribe); // 1st cct1
            Plot2d.plotYLine(DL1 + CCT12_LENGTH_PART1, cctHeight, -cctHeight, cctDescribe);

            //Plot2d.plotBox(DL1 + CCT00_LENGTH, DL1 + CCT_LENGTH, -height, height, cctDescribe); // 1st cct2

            Plot2d.plotBox(DL1 + CCT12_LENGTH + GAP1, DL1 + CCT12_LENGTH + GAP1 + QS1_LEN, -QsHeight, QsHeight, QsDescribe); // qs1
            Plot2d.plotBox(DL1 + CCT12_LENGTH + GAP1 + QS1_LEN + GAP2, DL1 + CCT12_LENGTH + GAP1 + QS1_LEN + GAP2 + QS2_LEN, -QsHeight, QsHeight, QsDescribe); // qs2
            Plot2d.plotBox(DL1 + CCT12_LENGTH + GAP1 + QS1_LEN + GAP2 + QS2_LEN + GAP2, DL1 + CCT12_LENGTH + GAP1 + QS1_LEN + GAP2 + QS2_LEN + GAP2 + QS2_LEN, -QsHeight, QsHeight, QsDescribe); // qs1

            Plot2d.plotBox(DL1 + CCT12_LENGTH + BETWEEN_CCT225, DL1 + CCT12_LENGTH + BETWEEN_CCT225 + CCT12_LENGTH, -cctHeight, cctHeight, cctDescribe); // 2 cct1
            //Plot2d.plotBox(DL1 + CCT_LENGTH + BETWEEN_CCT225 + CCT00_LENGTH, DL1 + CCT_LENGTH + BETWEEN_CCT225 + CCT_LENGTH, -height, height, cctDescribe); // 2 cct2

            Plot2d.plotYLine(DL1 + CCT12_LENGTH + BETWEEN_CCT225 + CCT12_LENGTH_PART2, cctHeight, -cctHeight, cctDescribe);
        }

        public static void drawElement() {
            drawElement(20, 30, Plot2d.GREY_DASH, Plot2d.GREY_DASH);
        }
    }

    private static enum CosyMap第二段 {
        EnterCCT345_0("EnterCCT345_0", DL2, cosyMapEnterCCT345_0()),
        ExitCCT345_0("ExitCCT345_0", DL2 + CCT345_LENGTH, cosyMapExitCCT345_0()),
        EndQs3("EndQs3", DL2 + CCT345_LENGTH + GAP3 + QS3_LEN, cosyMapEndQs3()),
        EnterCct345_1("EnterCct345_1", DL2 + CCT345_LENGTH + GAP3 + QS3_LEN + GAP3, cosyMapEnterCct345_1()),
        ExitCct345_1("ExitCct345_1", DL2 + CCT345_LENGTH + GAP3 + QS3_LEN + GAP3 + CCT345_LENGTH, cosyMapExitCct345_1()),
        ISOC("ISOC", DL2 + CCT345_LENGTH + GAP3 + QS3_LEN + GAP3 + CCT345_LENGTH + DL2, cosyMapIsoc());

        private String name;
        private double distance;
        private CosyArbitraryOrder.CosyMapArbitraryOrder map;

        CosyMap第二段(String name, double distance, CosyArbitraryOrder.CosyMapArbitraryOrder map) {
            this.name = name;
            this.distance = distance;
            this.map = map;
        }

        public String getName() {
            return name;
        }

        public double getDistance() {
            return distance;
        }

        public CosyArbitraryOrder.CosyMapArbitraryOrder getMap() {
            return map;
        }

        // --------- 画元件分布 -------------
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

        //------------data-------------
        private static CosyArbitraryOrder.CosyMapArbitraryOrder cosyMap第二段ISOC() {
            return CosyArbitraryOrder.readMap(
                    "  0.8560639     0.6426617     0.0000000E+00 0.0000000E+00 0.9122470E-02 100000\n" +
                            " -0.4157002     0.8560639     0.0000000E+00 0.0000000E+00-0.2043148E-02 010000\n" +
                            "  0.0000000E+00 0.0000000E+00 -1.013995     0.1209793     0.0000000E+00 001000\n" +
                            "  0.0000000E+00 0.0000000E+00 0.2329814     -1.013995     0.0000000E+00 000100\n" +
                            "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00  1.000000     000010\n" +
                            "  0.2043148E-02-0.9122470E-02 0.0000000E+00 0.0000000E+00  1.274693     000001\n" +
                            "  -5.639226    -0.9384986     0.0000000E+00 0.0000000E+00-0.1807654     200000\n" +
                            "  -15.67771     0.4203887     0.0000000E+00 0.0000000E+00-0.3369669E-01 110000\n" +
                            "   1.755658      9.054789     0.0000000E+00 0.0000000E+00-0.3261315E-01 020000\n" +
                            "  0.0000000E+00 0.0000000E+00 -16.58083     0.7224107E-01 0.0000000E+00 101000\n" +
                            "  0.0000000E+00 0.0000000E+00 -48.47881    -0.1617974E-01 0.0000000E+00 011000\n" +
                            "  -2.919110     -1.062605     0.0000000E+00 0.0000000E+00-0.7957923     002000\n" +
                            "  0.0000000E+00 0.0000000E+00 -3.327428      16.96123     0.0000000E+00 100100\n" +
                            "  0.0000000E+00 0.0000000E+00 0.7452398      48.39362     0.0000000E+00 010100\n" +
                            "   49.06712      17.21543     0.0000000E+00 0.0000000E+00  1.114809     001100\n" +
                            " -0.1307193E-01 0.2878378     0.0000000E+00 0.0000000E+00-0.5886283E-02 100001\n" +
                            " -0.6984560E-01-0.1791349     0.0000000E+00 0.0000000E+00 0.1318343E-02 010001\n" +
                            "  0.0000000E+00 0.0000000E+00-0.2834773     -1.478990     0.0000000E+00 001001\n" +
                            "  -5.269497     -3.618511     0.0000000E+00 0.0000000E+00 -5.843512     000200\n" +
                            "  0.0000000E+00 0.0000000E+00  11.59086    -0.7596009     0.0000000E+00 000101\n" +
                            " -0.3272354E-03 0.3466170E-02 0.0000000E+00 0.0000000E+00 -1.085330     000002\n" +
                            "   16.16432      7.152255     0.0000000E+00 0.0000000E+00  1.770827     300000\n" +
                            "   91.45431      49.65737     0.0000000E+00 0.0000000E+00  7.028567     210000\n" +
                            "   159.4623      119.5888     0.0000000E+00 0.0000000E+00  5.195335     120000\n" +
                            "   87.31451      104.1631     0.0000000E+00 0.0000000E+00 -1.264801     030000\n" +
                            "  0.0000000E+00 0.0000000E+00 -126.0283     -49.36323     0.0000000E+00 201000\n" +
                            "  0.0000000E+00 0.0000000E+00 -770.5492     -283.7281     0.0000000E+00 111000\n" +
                            "  0.0000000E+00 0.0000000E+00 -1173.503     -408.8499     0.0000000E+00 021000\n" +
                            "  -173.7890     -61.78512     0.0000000E+00 0.0000000E+00 -12.46595     102000\n" +
                            "  -505.3292     -179.9721     0.0000000E+00 0.0000000E+00 -37.53256     012000\n" +
                            "  0.0000000E+00 0.0000000E+00  6.733636      7.991402     0.0000000E+00 003000\n" +
                            "  0.0000000E+00 0.0000000E+00 -290.3437     -105.1017     0.0000000E+00 200100\n" +
                            "  0.0000000E+00 0.0000000E+00 -1711.910     -562.3619     0.0000000E+00 110100\n" +
                            "  0.0000000E+00 0.0000000E+00 -2515.298     -746.1316     0.0000000E+00 020100\n" +
                            "  -270.6305     -81.13163     0.0000000E+00 0.0000000E+00 -25.70584     101100\n" +
                            "  -618.9907     -177.8512     0.0000000E+00 0.0000000E+00 -40.69118     011100\n" +
                            "  0.0000000E+00 0.0000000E+00 -109.7306     -8.899062     0.0000000E+00 002100\n" +
                            "   2.004007    -0.3124385     0.0000000E+00 0.0000000E+00 -1.181603     200001\n" +
                            "   13.46064     -1.071326     0.0000000E+00 0.0000000E+00 -6.962070     110001\n" +
                            "  -2.663009     -9.398257     0.0000000E+00 0.0000000E+00 -10.12157     020001\n" +
                            "  0.0000000E+00 0.0000000E+00 -1.329634      1.466792     0.0000000E+00 101001\n" +
                            "  0.0000000E+00 0.0000000E+00  28.32370      3.999936     0.0000000E+00 011001\n" +
                            "   39.23853      13.34955     0.0000000E+00 0.0000000E+00  3.843719     002001\n" +
                            "   1069.301      394.1117     0.0000000E+00 0.0000000E+00  109.4303     100200\n" +
                            "   2779.460      1028.775     0.0000000E+00 0.0000000E+00  294.7902     010200\n" +
                            "  0.0000000E+00 0.0000000E+00  24.93993      118.5896     0.0000000E+00 001200\n" +
                            "  0.0000000E+00 0.0000000E+00 -1.259687      3.143039     0.0000000E+00 100101\n" +
                            "  0.0000000E+00 0.0000000E+00  27.02222     -8.407664     0.0000000E+00 010101\n" +
                            "   12.13720      10.96243     0.0000000E+00 0.0000000E+00 0.5048720     001101\n" +
                            "  -3.602654     -1.257306     0.0000000E+00 0.0000000E+00 0.6508856E-01 100002\n" +
                            "  -10.10198     -3.396756     0.0000000E+00 0.0000000E+00 0.3911656E-01 010002\n" +
                            "  0.0000000E+00 0.0000000E+00  7.632976      2.479428     0.0000000E+00 001002\n" +
                            "  0.0000000E+00 0.0000000E+00 -308.1684     -37.71834     0.0000000E+00 000300\n" +
                            "  -291.3905     -107.5484     0.0000000E+00 0.0000000E+00 -12.90355     000201\n" +
                            "  0.0000000E+00 0.0000000E+00  8.099066      7.949556     0.0000000E+00 000102\n" +
                            "  0.5321035E-01 0.7664841E-03 0.0000000E+00 0.0000000E+00 0.9119077     000003\n" +
                            "  -36.41423     -5.141470     0.0000000E+00 0.0000000E+00 -3.275935     400000\n" +
                            "  -240.0996      12.33315     0.0000000E+00 0.0000000E+00  1.074435     310000\n" +
                            "  -420.6995      315.1830     0.0000000E+00 0.0000000E+00  107.6221     220000\n" +
                            "   64.55341      1009.924     0.0000000E+00 0.0000000E+00  297.9399     130000\n" +
                            "   460.3484      949.0121     0.0000000E+00 0.0000000E+00  217.9246     040000\n" +
                            "  0.0000000E+00 0.0000000E+00 -161.2702     -89.00101     0.0000000E+00 301000\n" +
                            "  0.0000000E+00 0.0000000E+00 -1796.576     -941.0548     0.0000000E+00 211000\n" +
                            "  0.0000000E+00 0.0000000E+00 -6353.740     -3206.110     0.0000000E+00 121000\n" +
                            "  0.0000000E+00 0.0000000E+00 -7259.987     -3550.254     0.0000000E+00 031000\n" +
                            "  -664.6075     -269.1779     0.0000000E+00 0.0000000E+00 -22.54046     202000\n" +
                            "  -4462.299     -1779.901     0.0000000E+00 0.0000000E+00 -245.0296     112000\n" +
                            "  -7319.744     -2883.760     0.0000000E+00 0.0000000E+00 -517.7367     022000\n" +
                            "  0.0000000E+00 0.0000000E+00  336.3040      152.4810     0.0000000E+00 103000\n" +
                            "  0.0000000E+00 0.0000000E+00  947.5939      436.9227     0.0000000E+00 013000\n" +
                            "  -144.6188     -51.60708     0.0000000E+00 0.0000000E+00 -14.54469     004000\n" +
                            "  0.0000000E+00 0.0000000E+00 -620.3497     -275.0373     0.0000000E+00 300100\n" +
                            "  0.0000000E+00 0.0000000E+00 -6170.583     -2711.685     0.0000000E+00 210100\n" +
                            "  0.0000000E+00 0.0000000E+00 -20241.24     -8793.095     0.0000000E+00 120100\n" +
                            "  0.0000000E+00 0.0000000E+00 -21928.72     -9381.519     0.0000000E+00 030100\n" +
                            "  -6315.465     -2290.160     0.0000000E+00 0.0000000E+00 -428.5664     201100\n" +
                            "  -36340.32     -13144.12     0.0000000E+00 0.0000000E+00 -2755.286     111100\n" +
                            "  -52190.72     -18843.72     0.0000000E+00 0.0000000E+00 -4297.396     021100\n" +
                            "  0.0000000E+00 0.0000000E+00 -1001.481     -446.2677     0.0000000E+00 102100\n" +
                            "  0.0000000E+00 0.0000000E+00 -3235.374     -1402.188     0.0000000E+00 012100\n" +
                            "  -504.8971     -167.2647     0.0000000E+00 0.0000000E+00  1.149901     003100\n" +
                            "   33.40615      13.64138     0.0000000E+00 0.0000000E+00 -3.944353     300001\n" +
                            "   278.1537      94.47620     0.0000000E+00 0.0000000E+00 -35.04142     210001\n" +
                            "   854.7247      226.4977     0.0000000E+00 0.0000000E+00 -93.62782     120001\n" +
                            "   892.8239      175.5080     0.0000000E+00 0.0000000E+00 -84.54943     030001\n" +
                            "  0.0000000E+00 0.0000000E+00  74.02566      11.62474     0.0000000E+00 201001\n" +
                            "  0.0000000E+00 0.0000000E+00  814.4235      209.7534     0.0000000E+00 111001\n" +
                            "  0.0000000E+00 0.0000000E+00  1813.465      514.6235     0.0000000E+00 021001\n" +
                            "   421.1564      128.1223     0.0000000E+00 0.0000000E+00  32.52335     102001\n" +
                            "   1589.805      502.9141     0.0000000E+00 0.0000000E+00  139.5203     012001\n" +
                            "  0.0000000E+00 0.0000000E+00 -131.0818     -53.50127     0.0000000E+00 003001\n" +
                            "  -11737.05     -4024.428     0.0000000E+00 0.0000000E+00 -1331.111     200200\n" +
                            "  -57844.72     -19786.01     0.0000000E+00 0.0000000E+00 -6405.500     110200\n" +
                            "  -70129.25     -23913.22     0.0000000E+00 0.0000000E+00 -7601.880     020200\n" +
                            "  0.0000000E+00 0.0000000E+00 -3718.433     -1687.808     0.0000000E+00 101200\n" +
                            "  0.0000000E+00 0.0000000E+00 -10108.96     -4497.967     0.0000000E+00 011200\n" +
                            "  -1386.818     -458.4714     0.0000000E+00 0.0000000E+00 -360.1903     002200\n" +
                            "  0.0000000E+00 0.0000000E+00  314.8816      81.57111     0.0000000E+00 200101\n" +
                            "  0.0000000E+00 0.0000000E+00  2612.651      714.0580     0.0000000E+00 110101\n" +
                            "  0.0000000E+00 0.0000000E+00  4977.134      1318.139     0.0000000E+00 020101\n" +
                            "   3408.974      1171.063     0.0000000E+00 0.0000000E+00  288.3815     101101\n" +
                            "   9674.273      3338.741     0.0000000E+00 0.0000000E+00  821.1095     011101\n" +
                            "  0.0000000E+00 0.0000000E+00  44.87209     -19.24840     0.0000000E+00 002101\n" +
                            "   4.712806      1.827457     0.0000000E+00 0.0000000E+00  2.711218     200002\n" +
                            "   15.60972      8.110417     0.0000000E+00 0.0000000E+00  17.43549     110002\n" +
                            "   30.33918      15.35879     0.0000000E+00 0.0000000E+00  28.37169     020002\n" +
                            "  0.0000000E+00 0.0000000E+00 0.4871138     -3.347223     0.0000000E+00 101002\n" +
                            "  0.0000000E+00 0.0000000E+00 -11.01725     -6.024555     0.0000000E+00 011002\n" +
                            "  -79.66628     -21.70399     0.0000000E+00 0.0000000E+00 -10.47781     002002\n" +
                            "  0.0000000E+00 0.0000000E+00  5937.325      2541.704     0.0000000E+00 100300\n" +
                            "  0.0000000E+00 0.0000000E+00  15370.30      6631.594     0.0000000E+00 010300\n" +
                            "   4783.588      1782.245     0.0000000E+00 0.0000000E+00  579.8117     001300\n" +
                            "   4591.787      1769.948     0.0000000E+00 0.0000000E+00  301.7917     100201\n" +
                            "   10274.76      3980.690     0.0000000E+00 0.0000000E+00  505.6356     010201\n" +
                            "  0.0000000E+00 0.0000000E+00  141.2262     -2.946112     0.0000000E+00 001201\n" +
                            "  0.0000000E+00 0.0000000E+00 0.9977426     -13.07779     0.0000000E+00 100102\n" +
                            "  0.0000000E+00 0.0000000E+00 -38.55618     -25.64447     0.0000000E+00 010102\n" +
                            "  -445.3514     -156.6984     0.0000000E+00 0.0000000E+00 -30.21464     001102\n" +
                            "   4.692432     0.9202861     0.0000000E+00 0.0000000E+00-0.1701047     100003\n" +
                            "   18.01866      4.287810     0.0000000E+00 0.0000000E+00-0.1591082     010003\n" +
                            "  0.0000000E+00 0.0000000E+00 -9.852122     -1.434638     0.0000000E+00 001003\n" +
                            "   4515.316      1678.504     0.0000000E+00 0.0000000E+00  235.6050     000400\n" +
                            "  0.0000000E+00 0.0000000E+00 -1566.313     -919.2864     0.0000000E+00 000301\n" +
                            "  -138.1797     -95.62678     0.0000000E+00 0.0000000E+00 0.9844651     000202\n" +
                            "  0.0000000E+00 0.0000000E+00 -30.46682     -10.65260     0.0000000E+00 000103\n" +
                            " -0.1102648     0.9097688E-03 0.0000000E+00 0.0000000E+00-0.7743478     000004\n" +
                            "   71.81583      43.32215     0.0000000E+00 0.0000000E+00  40.28738     500000\n" +
                            "   488.8054      519.1212     0.0000000E+00 0.0000000E+00  438.0894     410000\n" +
                            "   600.2871      2656.822     0.0000000E+00 0.0000000E+00  2013.330     320000\n" +
                            "  -2282.531      7451.866     0.0000000E+00 0.0000000E+00  5037.724     230000\n" +
                            "  -6126.536      11491.58     0.0000000E+00 0.0000000E+00  6903.726     140000\n" +
                            "  -3712.388      7639.247     0.0000000E+00 0.0000000E+00  4040.481     050000\n" +
                            "  0.0000000E+00 0.0000000E+00  447.4095      52.33914     0.0000000E+00 401000\n" +
                            "  0.0000000E+00 0.0000000E+00  3932.162      50.49063     0.0000000E+00 311000\n" +
                            "  0.0000000E+00 0.0000000E+00  11726.49     -2208.704     0.0000000E+00 221000\n" +
                            "  0.0000000E+00 0.0000000E+00  12165.34     -9080.527     0.0000000E+00 131000\n" +
                            "  0.0000000E+00 0.0000000E+00  840.7026     -10234.67     0.0000000E+00 041000\n" +
                            "   3522.312      1075.420     0.0000000E+00 0.0000000E+00  191.7591     302000\n" +
                            "   26660.19      7793.277     0.0000000E+00 0.0000000E+00  1645.697     212000\n" +
                            "   65608.40      18081.94     0.0000000E+00 0.0000000E+00  4425.028     122000\n" +
                            "   51801.58      13064.86     0.0000000E+00 0.0000000E+00  3664.573     032000\n" +
                            "  0.0000000E+00 0.0000000E+00  4566.135      2066.764     0.0000000E+00 203000\n" +
                            "  0.0000000E+00 0.0000000E+00  27107.67      12288.65     0.0000000E+00 113000\n" +
                            "  0.0000000E+00 0.0000000E+00  40103.34      18199.19     0.0000000E+00 023000\n" +
                            "  -2313.997     -880.5636     0.0000000E+00 0.0000000E+00 -314.8966     104000\n" +
                            "  -6974.276     -2644.804     0.0000000E+00 0.0000000E+00 -992.7155     014000\n" +
                            "  0.0000000E+00 0.0000000E+00  375.1020      166.7964     0.0000000E+00 005000\n" +
                            "  0.0000000E+00 0.0000000E+00  555.4647     -45.27870     0.0000000E+00 400100\n" +
                            "  0.0000000E+00 0.0000000E+00  2832.316     -1949.253     0.0000000E+00 310100\n" +
                            "  0.0000000E+00 0.0000000E+00 -3409.816     -14704.98     0.0000000E+00 220100\n" +
                            "  0.0000000E+00 0.0000000E+00 -37695.90     -40753.78     0.0000000E+00 130100\n" +
                            "  0.0000000E+00 0.0000000E+00 -50900.33     -38800.23     0.0000000E+00 040100\n" +
                            "   14549.77      4119.940     0.0000000E+00 0.0000000E+00  2593.031     301100\n" +
                            "   81450.68      19786.86     0.0000000E+00 0.0000000E+00  17559.09     211100\n" +
                            "   110640.9      13194.94     0.0000000E+00 0.0000000E+00  36187.92     121100\n" +
                            "  -9610.399     -27872.55     0.0000000E+00 0.0000000E+00  20504.50     031100\n" +
                            "  0.0000000E+00 0.0000000E+00  16286.42      7478.198     0.0000000E+00 202100\n" +
                            "  0.0000000E+00 0.0000000E+00  86916.24      39894.06     0.0000000E+00 112100\n" +
                            "  0.0000000E+00 0.0000000E+00  115384.2      52928.16     0.0000000E+00 022100\n" +
                            "  -9787.973     -3516.721     0.0000000E+00 0.0000000E+00  755.1214     103100\n" +
                            "  -27009.01     -9798.964     0.0000000E+00 0.0000000E+00  2158.680     013100\n" +
                            "  0.0000000E+00 0.0000000E+00  678.1850      341.7039     0.0000000E+00 004100\n" +
                            "   9.113978    -0.4275915     0.0000000E+00 0.0000000E+00 -16.10490     400001\n" +
                            "   343.7481      48.05047     0.0000000E+00 0.0000000E+00 -221.5818     310001\n" +
                            "   2019.688      189.5257     0.0000000E+00 0.0000000E+00 -1145.077     220001\n" +
                            "   4480.887      18.17159     0.0000000E+00 0.0000000E+00 -2475.485     130001\n" +
                            "   3725.769     -342.9678     0.0000000E+00 0.0000000E+00 -1854.132     040001\n" +
                            "  0.0000000E+00 0.0000000E+00  67.14839      28.97999     0.0000000E+00 301001\n" +
                            "  0.0000000E+00 0.0000000E+00  2041.247      889.0741     0.0000000E+00 211001\n" +
                            "  0.0000000E+00 0.0000000E+00  11321.67      4919.130     0.0000000E+00 121001\n" +
                            "  0.0000000E+00 0.0000000E+00  17282.97      7465.805     0.0000000E+00 031001\n" +
                            "  -1977.358     -728.0282     0.0000000E+00 0.0000000E+00 -233.5329     202001\n" +
                            "  -6602.743     -2479.668     0.0000000E+00 0.0000000E+00 -951.4451     112001\n" +
                            "  -1672.862     -783.0062     0.0000000E+00 0.0000000E+00 -589.5685     022001\n" +
                            "  0.0000000E+00 0.0000000E+00 -2611.827     -1055.094     0.0000000E+00 103001\n" +
                            "  0.0000000E+00 0.0000000E+00 -8139.070     -3339.315     0.0000000E+00 013001\n" +
                            "   710.4301      235.7323     0.0000000E+00 0.0000000E+00  119.3071     004001\n" +
                            "   17704.79      4643.499     0.0000000E+00 0.0000000E+00  5903.869     300200\n" +
                            "   52184.44      6428.225     0.0000000E+00 0.0000000E+00  32738.55     210200\n" +
                            "  -108253.7     -67968.09     0.0000000E+00 0.0000000E+00  49236.91     120200\n" +
                            "  -321527.9     -137965.0     0.0000000E+00 0.0000000E+00  10906.57     030200\n" +
                            "  0.0000000E+00 0.0000000E+00 -31379.02     -13579.21     0.0000000E+00 201200\n" +
                            "  0.0000000E+00 0.0000000E+00 -191380.5     -82969.16     0.0000000E+00 111200\n" +
                            "  0.0000000E+00 0.0000000E+00 -289682.3     -125773.7     0.0000000E+00 021200\n" +
                            "  -61355.29     -21976.50     0.0000000E+00 0.0000000E+00 -1778.637     102200\n" +
                            "  -175509.7     -62674.85     0.0000000E+00 0.0000000E+00 -7587.377     012200\n" +
                            "  0.0000000E+00 0.0000000E+00  823.1474      407.8332     0.0000000E+00 003200\n" +
                            "  0.0000000E+00 0.0000000E+00  489.6557      203.9511     0.0000000E+00 300101\n" +
                            "  0.0000000E+00 0.0000000E+00  8997.230      3663.035     0.0000000E+00 210101\n" +
                            "  0.0000000E+00 0.0000000E+00  42269.19      17046.17     0.0000000E+00 120101\n" +
                            "  0.0000000E+00 0.0000000E+00  58983.09      23560.16     0.0000000E+00 030101\n" +
                            "  -5388.471     -2448.169     0.0000000E+00 0.0000000E+00 -838.6991     201101\n" +
                            "   6694.252     -644.1579     0.0000000E+00 0.0000000E+00 -304.8974     111101\n" +
                            "   62627.08      17895.63     0.0000000E+00 0.0000000E+00  6075.869     021101\n" +
                            "  0.0000000E+00 0.0000000E+00 -7334.638     -3541.731     0.0000000E+00 102101\n" +
                            "  0.0000000E+00 0.0000000E+00 -17289.30     -8729.534     0.0000000E+00 012101\n" +
                            "   3651.366      1203.742     0.0000000E+00 0.0000000E+00 -147.6138     003101\n" +
                            "  -40.07766     -8.445786     0.0000000E+00 0.0000000E+00 0.7920398     300002\n" +
                            "  -483.7009     -107.8606     0.0000000E+00 0.0000000E+00  44.46418     210002\n" +
                            "  -1872.650     -392.8671     0.0000000E+00 0.0000000E+00  226.2858     120002\n" +
                            "  -2295.360     -429.1882     0.0000000E+00 0.0000000E+00  306.9492     030002\n" +
                            "  0.0000000E+00 0.0000000E+00 -83.22290     -13.69080     0.0000000E+00 201002\n" +
                            "  0.0000000E+00 0.0000000E+00 -950.1486     -214.1031     0.0000000E+00 111002\n" +
                            "  0.0000000E+00 0.0000000E+00 -2449.073     -628.5141     0.0000000E+00 021002\n" +
                            "   120.6712      103.1023     0.0000000E+00 0.0000000E+00 -8.848013     102002\n" +
                            "  -940.6101     -105.9714     0.0000000E+00 0.0000000E+00 -174.4354     012002\n" +
                            "  0.0000000E+00 0.0000000E+00  475.6016      165.9557     0.0000000E+00 003002\n" +
                            "  0.0000000E+00 0.0000000E+00 -101012.5     -43847.02     0.0000000E+00 200300\n" +
                            "  0.0000000E+00 0.0000000E+00 -522670.4     -226721.3     0.0000000E+00 110300\n" +
                            "  0.0000000E+00 0.0000000E+00 -672359.4     -291245.8     0.0000000E+00 020300\n" +
                            "  -193885.0     -68308.70     0.0000000E+00 0.0000000E+00 -29974.73     101300\n" +
                            "  -509999.3     -179932.9     0.0000000E+00 0.0000000E+00 -77551.39     011300\n" +
                            "  0.0000000E+00 0.0000000E+00 -10679.62     -4093.061     0.0000000E+00 002300\n" +
                            "   2804.801     -442.6974     0.0000000E+00 0.0000000E+00  123.2518     200201\n" +
                            "   66918.78      16617.59     0.0000000E+00 0.0000000E+00  7920.820     110201\n" +
                            "   153517.9      45869.57     0.0000000E+00 0.0000000E+00  19278.48     020201\n" +
                            "  0.0000000E+00 0.0000000E+00  21175.55      8165.100     0.0000000E+00 101201\n" +
                            "  0.0000000E+00 0.0000000E+00  65983.20      25736.71     0.0000000E+00 011201\n" +
                            "   18598.26      6208.636     0.0000000E+00 0.0000000E+00  1505.716     002201\n" +
                            "  0.0000000E+00 0.0000000E+00 -375.8193     -86.98937     0.0000000E+00 200102\n" +
                            "  0.0000000E+00 0.0000000E+00 -3457.173     -873.1619     0.0000000E+00 110102\n" +
                            "  0.0000000E+00 0.0000000E+00 -7479.202     -1902.270     0.0000000E+00 020102\n" +
                            "  -2447.610     -344.5330     0.0000000E+00 0.0000000E+00 -324.4347     101102\n" +
                            "  -13233.67     -3242.395     0.0000000E+00 0.0000000E+00 -1614.323     011102\n" +
                            "  0.0000000E+00 0.0000000E+00  1272.493      568.3512     0.0000000E+00 002102\n" +
                            "  -11.00051     -2.527247     0.0000000E+00 0.0000000E+00 -3.459269     200003\n" +
                            "  -57.18659     -14.60486     0.0000000E+00 0.0000000E+00 -26.61210     110003\n" +
                            "  -80.44698     -23.18837     0.0000000E+00 0.0000000E+00 -50.57407     020003\n" +
                            "  0.0000000E+00 0.0000000E+00  15.25855      7.283361     0.0000000E+00 101003\n" +
                            "  0.0000000E+00 0.0000000E+00  27.18486      13.66047     0.0000000E+00 011003\n" +
                            "   55.19225      5.291407     0.0000000E+00 0.0000000E+00  19.16435     002003\n" +
                            "  -57157.60     -20003.99     0.0000000E+00 0.0000000E+00  2192.396     100400\n" +
                            "  -145178.4     -50668.70     0.0000000E+00 0.0000000E+00  7954.759     010400\n" +
                            "  0.0000000E+00 0.0000000E+00  22358.66      10087.77     0.0000000E+00 001400\n" +
                            "  0.0000000E+00 0.0000000E+00  39677.32      19493.43     0.0000000E+00 100301\n" +
                            "  0.0000000E+00 0.0000000E+00  98182.04      48306.51     0.0000000E+00 010301\n" +
                            "   36394.76      13731.82     0.0000000E+00 0.0000000E+00  4902.335     001301\n" +
                            "  -8317.421     -2191.460     0.0000000E+00 0.0000000E+00 -825.5113     100202\n" +
                            "  -27780.78     -8147.091     0.0000000E+00 0.0000000E+00 -2567.132     010202\n" +
                            "  0.0000000E+00 0.0000000E+00 -1920.100     -785.3652     0.0000000E+00 001202\n" +
                            "  0.0000000E+00 0.0000000E+00  48.11060      31.79242     0.0000000E+00 100103\n" +
                            "  0.0000000E+00 0.0000000E+00  146.2367      80.68702     0.0000000E+00 010103\n" +
                            "   747.0386      187.8650     0.0000000E+00 0.0000000E+00  84.24881     001103\n" +
                            "  -4.715653    -0.5832457     0.0000000E+00 0.0000000E+00 0.2977024     100004\n" +
                            "  -22.08730     -4.009692     0.0000000E+00 0.0000000E+00 0.2557065     010004\n" +
                            "  0.0000000E+00 0.0000000E+00  7.843121    -0.2459141     0.0000000E+00 001004\n" +
                            "  0.0000000E+00 0.0000000E+00  28352.63      13276.50     0.0000000E+00 000500\n" +
                            "  -3463.577     -554.0543     0.0000000E+00 0.0000000E+00 -2216.909     000401\n" +
                            "  0.0000000E+00 0.0000000E+00 -920.6223     -784.8926     0.0000000E+00 000302\n" +
                            "   995.6954      332.0163     0.0000000E+00 0.0000000E+00  47.26148     000203\n" +
                            "  0.0000000E+00 0.0000000E+00  44.85100      9.219070     0.0000000E+00 000104\n" +
                            "  0.2345659     0.1386849E-01 0.0000000E+00 0.0000000E+00 0.6519129     000005"
            );
        }

        private static CosyArbitraryOrder.CosyMapArbitraryOrder cosyMapEnterCCT345_0() {
            return CosyArbitraryOrder.readMap(
                    "   1.000000     0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00 100000\n" +
                            "   2.400000      1.000000     0.0000000E+00 0.0000000E+00 0.0000000E+00 010000\n" +
                            "  0.0000000E+00 0.0000000E+00  1.000000     0.0000000E+00 0.0000000E+00 001000\n" +
                            "  0.0000000E+00 0.0000000E+00  2.400000      1.000000     0.0000000E+00 000100\n" +
                            "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00  1.000000     000010\n" +
                            "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00 0.4672185     000001\n" +
                            "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00-0.6705370     020000\n" +
                            "  -1.341074     0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00 010001\n" +
                            "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00-0.6705370     000200\n" +
                            "  0.0000000E+00 0.0000000E+00 -1.341074     0.0000000E+00 0.0000000E+00 000101\n" +
                            "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00-0.3916091     000002\n" +
                            "   1.200000     0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00 030000\n" +
                            "  0.0000000E+00 0.0000000E+00  1.200000     0.0000000E+00 0.0000000E+00 020100\n" +
                            "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00 0.9829756     020001\n" +
                            "   1.200000     0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00 010200\n" +
                            "  0.9829756     0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00 010002\n" +
                            "  0.0000000E+00 0.0000000E+00  1.200000     0.0000000E+00 0.0000000E+00 000300\n" +
                            "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00 0.9829756     000201\n" +
                            "  0.0000000E+00 0.0000000E+00 0.9829756     0.0000000E+00 0.0000000E+00 000102\n" +
                            "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00 0.3372426     000003\n" +
                            "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00-0.5029027     040000\n" +
                            "  -2.011611     0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00 030001\n" +
                            "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00 -1.005805     020200\n" +
                            "  0.0000000E+00 0.0000000E+00 -2.011611     0.0000000E+00 0.0000000E+00 020101\n" +
                            "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00 -1.215511     020002\n" +
                            "  -2.011611     0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00 010201\n" +
                            " -0.8103407     0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00 010003\n" +
                            "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00-0.5029027     000400\n" +
                            "  0.0000000E+00 0.0000000E+00 -2.011611     0.0000000E+00 0.0000000E+00 000301\n" +
                            "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00 -1.215511     000202\n" +
                            "  0.0000000E+00 0.0000000E+00-0.8103407     0.0000000E+00 0.0000000E+00 000103\n" +
                            "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00-0.2990861     000004\n" +
                            "  0.9000000     0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00 050000\n" +
                            "  0.0000000E+00 0.0000000E+00 0.9000000     0.0000000E+00 0.0000000E+00 040100\n" +
                            "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00  1.299257     040001\n" +
                            "   1.800000     0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00 030200\n" +
                            "   2.598513     0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00 030002\n" +
                            "  0.0000000E+00 0.0000000E+00  1.800000     0.0000000E+00 0.0000000E+00 020300\n" +
                            "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00  2.598513     020201\n" +
                            "  0.0000000E+00 0.0000000E+00  2.598513     0.0000000E+00 0.0000000E+00 020102\n" +
                            "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00  1.411470     020003\n" +
                            "  0.9000000     0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00 010400\n" +
                            "   2.598513     0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00 010202\n" +
                            "  0.7057348     0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00 010004\n" +
                            "  0.0000000E+00 0.0000000E+00 0.9000000     0.0000000E+00 0.0000000E+00 000500\n" +
                            "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00  1.299257     000401\n" +
                            "  0.0000000E+00 0.0000000E+00  2.598513     0.0000000E+00 0.0000000E+00 000302\n" +
                            "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00  1.411470     000203\n" +
                            "  0.0000000E+00 0.0000000E+00 0.7057348     0.0000000E+00 0.0000000E+00 000104\n" +
                            "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00 0.2710873     000005"
            );
        }

        private static CosyArbitraryOrder.CosyMapArbitraryOrder cosyMapExitCCT345_0() {
            return CosyArbitraryOrder.readMap(
                    "  0.6909822     -2.314735     0.0000000E+00 0.0000000E+00-0.6086743     100000\n" +
                            "   2.387337     -6.550172     0.0000000E+00 0.0000000E+00 -1.772100     010000\n" +
                            "  0.0000000E+00 0.0000000E+00 0.2269755    -0.8326336     0.0000000E+00 001000\n" +
                            "  0.0000000E+00 0.0000000E+00  1.276645    -0.2774643     0.0000000E+00 000100\n" +
                            "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00  1.000000     000010\n" +
                            "  0.2286209     0.1150208     0.0000000E+00 0.0000000E+00 0.5762458     000001\n" +
                            " -0.5212457     -2.252022     0.0000000E+00 0.0000000E+00-0.1403853     200000\n" +
                            "  -2.249674     -17.90751     0.0000000E+00 0.0000000E+00 -1.319464     110000\n" +
                            "  -2.624972     -32.80459     0.0000000E+00 0.0000000E+00 -3.371839     020000\n" +
                            "  0.0000000E+00 0.0000000E+00 -2.474977     -5.107612     0.0000000E+00 101000\n" +
                            "  0.0000000E+00 0.0000000E+00 -7.228067     -14.26538     0.0000000E+00 011000\n" +
                            " -0.6457313    -0.1669061     0.0000000E+00 0.0000000E+00-0.2180008     002000\n" +
                            "  0.0000000E+00 0.0000000E+00 -5.519268     -11.50695     0.0000000E+00 100100\n" +
                            "  0.0000000E+00 0.0000000E+00 -15.83607     -30.98007     0.0000000E+00 010100\n" +
                            "  -3.236477     0.4113887     0.0000000E+00 0.0000000E+00-0.6846109     001100\n" +
                            "  0.2366482     -1.703109     0.0000000E+00 0.0000000E+00 0.1042845     100001\n" +
                            " -0.7186152     -1.233613     0.0000000E+00 0.0000000E+00  1.211593     010001\n" +
                            "  0.0000000E+00 0.0000000E+00 0.3875978     0.8172833     0.0000000E+00 001001\n" +
                            "  -4.180893      2.267513     0.0000000E+00 0.0000000E+00 -1.203017     000200\n" +
                            "  0.0000000E+00 0.0000000E+00 0.4695833      3.348089     0.0000000E+00 000101\n" +
                            " -0.1342700     0.7288792E-01 0.0000000E+00 0.0000000E+00-0.4921432     000002\n" +
                            "  -1.664478    -0.4122981     0.0000000E+00 0.0000000E+00-0.3977869     300000\n" +
                            "  -14.75874     -14.52130     0.0000000E+00 0.0000000E+00 -4.825479     210000\n" +
                            "  -44.69506     -72.73386     0.0000000E+00 0.0000000E+00 -17.96978     120000\n" +
                            "  -44.60918     -101.4069     0.0000000E+00 0.0000000E+00 -21.93730     030000\n" +
                            "  0.0000000E+00 0.0000000E+00 -3.084682     -8.206529     0.0000000E+00 201000\n" +
                            "  0.0000000E+00 0.0000000E+00 -21.16415     -54.50684     0.0000000E+00 111000\n" +
                            "  0.0000000E+00 0.0000000E+00 -34.97008     -88.56111     0.0000000E+00 021000\n" +
                            " -0.8618666     0.4317126     0.0000000E+00 0.0000000E+00 -1.434201     102000\n" +
                            "  -1.828849      1.780421     0.0000000E+00 0.0000000E+00 -3.905607     012000\n" +
                            "  0.0000000E+00 0.0000000E+00 -1.466784     -3.873640     0.0000000E+00 003000\n" +
                            "  0.0000000E+00 0.0000000E+00 -8.113500     -21.43981     0.0000000E+00 200100\n" +
                            "  0.0000000E+00 0.0000000E+00 -53.59157     -136.8655     0.0000000E+00 110100\n" +
                            "  0.0000000E+00 0.0000000E+00 -85.85510     -217.1896     0.0000000E+00 020100\n" +
                            "  -4.455411      3.422094     0.0000000E+00 0.0000000E+00 -5.707372     101100\n" +
                            "  -9.452616      13.76934     0.0000000E+00 0.0000000E+00 -15.02881     011100\n" +
                            "  0.0000000E+00 0.0000000E+00 -9.719945     -27.98499     0.0000000E+00 002100\n" +
                            "  0.1241217     -2.378831     0.0000000E+00 0.0000000E+00-0.1776603     200001\n" +
                            "   1.661130     -5.148206     0.0000000E+00 0.0000000E+00-0.1464717     110001\n" +
                            "   3.415761      12.54585     0.0000000E+00 0.0000000E+00  2.962893     020001\n" +
                            "  0.0000000E+00 0.0000000E+00 0.9413539      1.078627     0.0000000E+00 101001\n" +
                            "  0.0000000E+00 0.0000000E+00  6.692332      11.00829     0.0000000E+00 011001\n" +
                            "  0.6123958     0.3727395     0.0000000E+00 0.0000000E+00 0.4648109     002001\n" +
                            "  -5.990334      7.870824     0.0000000E+00 0.0000000E+00 -5.370045     100200\n" +
                            "  -11.70071      25.86782     0.0000000E+00 0.0000000E+00 -14.32782     010200\n" +
                            "  0.0000000E+00 0.0000000E+00 -22.19991     -68.79775     0.0000000E+00 001200\n" +
                            "  0.0000000E+00 0.0000000E+00  5.997107      10.93546     0.0000000E+00 100101\n" +
                            "  0.0000000E+00 0.0000000E+00  26.01083      47.66929     0.0000000E+00 010101\n" +
                            "   4.844200      2.500631     0.0000000E+00 0.0000000E+00  2.449720     001101\n" +
                            " -0.1121243     0.8101713     0.0000000E+00 0.0000000E+00-0.6591342E-01 100002\n" +
                            "  0.4682529      2.361348     0.0000000E+00 0.0000000E+00-0.9675896     010002\n" +
                            "  0.0000000E+00 0.0000000E+00-0.2654985    -0.3965939     0.0000000E+00 001002\n" +
                            "  0.0000000E+00 0.0000000E+00 -17.13215     -58.68184     0.0000000E+00 000300\n" +
                            "   8.475885      1.902502     0.0000000E+00 0.0000000E+00  3.831700     000201\n" +
                            "  0.0000000E+00 0.0000000E+00-0.9150552     -3.225016     0.0000000E+00 000102\n" +
                            "  0.1113803    -0.4175706E-01 0.0000000E+00 0.0000000E+00 0.4274883     000003\n" +
                            "  -2.319941      6.758755     0.0000000E+00 0.0000000E+00-0.7304778     400000\n" +
                            "  -31.37214      59.44315     0.0000000E+00 0.0000000E+00 -12.53326     310000\n" +
                            "  -158.1657      173.4486     0.0000000E+00 0.0000000E+00 -73.02490     220000\n" +
                            "  -351.1067      160.4069     0.0000000E+00 0.0000000E+00 -178.7773     130000\n" +
                            "  -288.9691     -28.85472     0.0000000E+00 0.0000000E+00 -159.6422     040000\n" +
                            "  0.0000000E+00 0.0000000E+00 -3.530407     -7.029463     0.0000000E+00 301000\n" +
                            "  0.0000000E+00 0.0000000E+00 -43.56614     -92.07621     0.0000000E+00 211000\n" +
                            "  0.0000000E+00 0.0000000E+00 -161.7451     -353.8701     0.0000000E+00 121000\n" +
                            "  0.0000000E+00 0.0000000E+00 -192.1617     -429.5248     0.0000000E+00 031000\n" +
                            " -0.8733206      2.596222     0.0000000E+00 0.0000000E+00 -3.411619     202000\n" +
                            "  -3.775570      10.42631     0.0000000E+00 0.0000000E+00 -21.25583     112000\n" +
                            "  -4.701711      7.806082     0.0000000E+00 0.0000000E+00 -33.09981     022000\n" +
                            "  0.0000000E+00 0.0000000E+00 -6.900417     -12.42596     0.0000000E+00 103000\n" +
                            "  0.0000000E+00 0.0000000E+00 -19.13231     -34.17644     0.0000000E+00 013000\n" +
                            "  -1.107800     -1.968322     0.0000000E+00 0.0000000E+00 -1.158139     004000\n" +
                            "  0.0000000E+00 0.0000000E+00 -8.493632     -20.73040     0.0000000E+00 300100\n" +
                            "  0.0000000E+00 0.0000000E+00 -102.5239     -250.3069     0.0000000E+00 210100\n" +
                            "  0.0000000E+00 0.0000000E+00 -379.1091     -929.0825     0.0000000E+00 120100\n" +
                            "  0.0000000E+00 0.0000000E+00 -451.9836     -1107.403     0.0000000E+00 030100\n" +
                            "  -1.397724      15.97962     0.0000000E+00 0.0000000E+00 -14.36540     201100\n" +
                            "  0.3839408      74.48820     0.0000000E+00 0.0000000E+00 -87.04089     111100\n" +
                            "   5.124527      78.16855     0.0000000E+00 0.0000000E+00 -133.1699     021100\n" +
                            "  0.0000000E+00 0.0000000E+00 -43.68556     -84.81600     0.0000000E+00 102100\n" +
                            "  0.0000000E+00 0.0000000E+00 -119.7007     -229.7450     0.0000000E+00 012100\n" +
                            "  -10.52523     -20.36841     0.0000000E+00 0.0000000E+00 -10.29261     003100\n" +
                            "  0.4552655     -3.936138     0.0000000E+00 0.0000000E+00-0.3223382     300001\n" +
                            "   11.48194     -29.26443     0.0000000E+00 0.0000000E+00 0.2856357     210001\n" +
                            "   58.39185     -37.52488     0.0000000E+00 0.0000000E+00  14.16592     120001\n" +
                            "   82.74894      41.21657     0.0000000E+00 0.0000000E+00  31.98634     030001\n" +
                            "  0.0000000E+00 0.0000000E+00 0.8506009E-01 -2.215362     0.0000000E+00 201001\n" +
                            "  0.0000000E+00 0.0000000E+00  14.21806      20.15043     0.0000000E+00 111001\n" +
                            "  0.0000000E+00 0.0000000E+00  44.89907      87.28525     0.0000000E+00 021001\n" +
                            "   1.192749     -2.522591     0.0000000E+00 0.0000000E+00  2.141728     102001\n" +
                            "   3.136134     -7.891820     0.0000000E+00 0.0000000E+00  7.933386     012001\n" +
                            "  0.0000000E+00 0.0000000E+00  2.235248      3.745527     0.0000000E+00 003001\n" +
                            "   1.303048      25.92818     0.0000000E+00 0.0000000E+00 -14.30452     200200\n" +
                            "   19.10334      128.7268     0.0000000E+00 0.0000000E+00 -84.21783     110200\n" +
                            "   32.53454      145.2677     0.0000000E+00 0.0000000E+00 -128.1548     020200\n" +
                            "  0.0000000E+00 0.0000000E+00 -93.24567     -196.3783     0.0000000E+00 101200\n" +
                            "  0.0000000E+00 0.0000000E+00 -256.5534     -531.8548     0.0000000E+00 011200\n" +
                            "  -38.06288     -79.24409     0.0000000E+00 0.0000000E+00 -34.98681     002200\n" +
                            "  0.0000000E+00 0.0000000E+00  5.644170      8.644063     0.0000000E+00 200101\n" +
                            "  0.0000000E+00 0.0000000E+00  70.94656      141.2564     0.0000000E+00 110101\n" +
                            "  0.0000000E+00 0.0000000E+00  166.3125      358.7455     0.0000000E+00 020101\n" +
                            "   8.495527     -11.79524     0.0000000E+00 0.0000000E+00  13.41194     101101\n" +
                            "   21.02730     -41.72673     0.0000000E+00 0.0000000E+00  43.97594     011101\n" +
                            "  0.0000000E+00 0.0000000E+00  20.76423      43.13821     0.0000000E+00 002101\n" +
                            "  0.2808622E-01  1.004359     0.0000000E+00 0.0000000E+00 0.2668840     200002\n" +
                            " -0.5392814      7.305942     0.0000000E+00 0.0000000E+00  1.400251     110002\n" +
                            "  -2.429761    -0.9760138     0.0000000E+00 0.0000000E+00 -1.267599     020002\n" +
                            "  0.0000000E+00 0.0000000E+00-0.3675863     0.2183381     0.0000000E+00 101002\n" +
                            "  0.0000000E+00 0.0000000E+00 -5.393708     -6.684218     0.0000000E+00 011002\n" +
                            " -0.7629185    -0.3435709E-01 0.0000000E+00 0.0000000E+00-0.6538647     002002\n" +
                            "  0.0000000E+00 0.0000000E+00 -70.98868     -162.3983     0.0000000E+00 100300\n" +
                            "  0.0000000E+00 0.0000000E+00 -199.6048     -445.8388     0.0000000E+00 010300\n" +
                            "  -63.81376     -138.2457     0.0000000E+00 0.0000000E+00 -54.24767     001300\n" +
                            "   14.14443     -16.73947     0.0000000E+00 0.0000000E+00  17.98854     100201\n" +
                            "   32.50056     -65.66600     0.0000000E+00 0.0000000E+00  55.60482     010201\n" +
                            "  0.0000000E+00 0.0000000E+00  59.85347      143.8717     0.0000000E+00 001201\n" +
                            "  0.0000000E+00 0.0000000E+00 -5.182633     -7.334445     0.0000000E+00 100102\n" +
                            "  0.0000000E+00 0.0000000E+00 -30.63898     -49.23921     0.0000000E+00 010102\n" +
                            "  -6.819148     -2.040574     0.0000000E+00 0.0000000E+00 -4.512396     001102\n" +
                            "  0.8094979E-01-0.5007542     0.0000000E+00 0.0000000E+00 0.4087829E-01 100003\n" +
                            " -0.4316335     -2.522396     0.0000000E+00 0.0000000E+00 0.7836439     010003\n" +
                            "  0.0000000E+00 0.0000000E+00 0.2234755     0.2848699     0.0000000E+00 001003\n" +
                            "  -42.86862     -90.08102     0.0000000E+00 0.0000000E+00 -32.69467     000400\n" +
                            "  0.0000000E+00 0.0000000E+00  56.14205      153.7665     0.0000000E+00 000301\n" +
                            "  -13.73073     -3.970130     0.0000000E+00 0.0000000E+00 -7.938964     000202\n" +
                            "  0.0000000E+00 0.0000000E+00  1.124742      3.057663     0.0000000E+00 000103\n" +
                            " -0.1058267     0.2696139E-01 0.0000000E+00 0.0000000E+00-0.3846946     000004\n" +
                            "  -2.292232      10.84787     0.0000000E+00 0.0000000E+00-0.8226484     500000\n" +
                            "  -52.33115      141.1971     0.0000000E+00 0.0000000E+00 -23.80253     410000\n" +
                            "  -425.7426      706.9372     0.0000000E+00 0.0000000E+00 -212.3940     320000\n" +
                            "  -1620.723      1668.904     0.0000000E+00 0.0000000E+00 -848.2663     230000\n" +
                            "  -2958.123      1750.906     0.0000000E+00 0.0000000E+00 -1596.525     140000\n" +
                            "  -2100.029      522.9508     0.0000000E+00 0.0000000E+00 -1161.621     050000\n" +
                            "  0.0000000E+00 0.0000000E+00 -5.396599    -0.6720233     0.0000000E+00 401000\n" +
                            "  0.0000000E+00 0.0000000E+00 -98.51976     -81.44198     0.0000000E+00 311000\n" +
                            "  0.0000000E+00 0.0000000E+00 -584.9679     -682.5008     0.0000000E+00 221000\n" +
                            "  0.0000000E+00 0.0000000E+00 -1442.680     -1989.260     0.0000000E+00 131000\n" +
                            "  0.0000000E+00 0.0000000E+00 -1289.314     -1977.435     0.0000000E+00 041000\n" +
                            "  -8.335075     0.3227693     0.0000000E+00 0.0000000E+00 -7.453873     302000\n" +
                            "  -74.78166     -5.973137     0.0000000E+00 0.0000000E+00 -75.84960     212000\n" +
                            "  -225.0305     -46.32799     0.0000000E+00 0.0000000E+00 -251.4493     122000\n" +
                            "  -226.0144     -73.38591     0.0000000E+00 0.0000000E+00 -274.3085     032000\n" +
                            "  0.0000000E+00 0.0000000E+00 -20.61141     -29.77177     0.0000000E+00 203000\n" +
                            "  0.0000000E+00 0.0000000E+00 -124.7380     -183.9075     0.0000000E+00 113000\n" +
                            "  0.0000000E+00 0.0000000E+00 -188.9250     -284.6998     0.0000000E+00 023000\n" +
                            "  -4.052440      1.322543     0.0000000E+00 0.0000000E+00 -6.864642     104000\n" +
                            "  -8.846675      3.399382     0.0000000E+00 0.0000000E+00 -18.74717     014000\n" +
                            "  0.0000000E+00 0.0000000E+00 -4.547643     -7.973936     0.0000000E+00 005000\n" +
                            "  0.0000000E+00 0.0000000E+00 -12.39351     -5.900120     0.0000000E+00 400100\n" +
                            "  0.0000000E+00 0.0000000E+00 -222.8591     -243.8534     0.0000000E+00 310100\n" +
                            "  0.0000000E+00 0.0000000E+00 -1315.288     -1850.664     0.0000000E+00 220100\n" +
                            "  0.0000000E+00 0.0000000E+00 -3242.883     -5215.635     0.0000000E+00 130100\n" +
                            "  0.0000000E+00 0.0000000E+00 -2908.632     -5110.561     0.0000000E+00 040100\n" +
                            "  -27.34910      4.702056     0.0000000E+00 0.0000000E+00 -29.71506     301100\n" +
                            "  -231.0701     -3.295927     0.0000000E+00 0.0000000E+00 -298.6435     211100\n" +
                            "  -665.6976     -148.2879     0.0000000E+00 0.0000000E+00 -984.3897     121100\n" +
                            "  -646.9076     -269.5861     0.0000000E+00 0.0000000E+00 -1071.228     031100\n" +
                            "  0.0000000E+00 0.0000000E+00 -128.6736     -188.7371     0.0000000E+00 202100\n" +
                            "  0.0000000E+00 0.0000000E+00 -767.2667     -1145.631     0.0000000E+00 112100\n" +
                            "  0.0000000E+00 0.0000000E+00 -1153.741     -1767.030     0.0000000E+00 022100\n" +
                            "  -38.93648      4.428888     0.0000000E+00 0.0000000E+00 -60.49950     103100\n" +
                            "  -84.54281      11.36296     0.0000000E+00 0.0000000E+00 -163.6428     013100\n" +
                            "  0.0000000E+00 0.0000000E+00 -50.23227     -92.78303     0.0000000E+00 004100\n" +
                            "  -1.758101     -8.236603     0.0000000E+00 0.0000000E+00 -1.178552     400001\n" +
                            "  -3.691895     -134.3366     0.0000000E+00 0.0000000E+00 -5.336994     310001\n" +
                            "   84.86121     -664.3989     0.0000000E+00 0.0000000E+00  33.43949     220001\n" +
                            "   416.0705     -1258.747     0.0000000E+00 0.0000000E+00  215.4678     130001\n" +
                            "   526.2938     -732.8327     0.0000000E+00 0.0000000E+00  300.8443     040001\n" +
                            "  0.0000000E+00 0.0000000E+00 -2.328679     -9.770295     0.0000000E+00 301001\n" +
                            "  0.0000000E+00 0.0000000E+00  13.19205     -24.02750     0.0000000E+00 211001\n" +
                            "  0.0000000E+00 0.0000000E+00  171.2332      198.7215     0.0000000E+00 121001\n" +
                            "  0.0000000E+00 0.0000000E+00  332.7449      546.7082     0.0000000E+00 031001\n" +
                            "   2.246996     -6.677633     0.0000000E+00 0.0000000E+00  4.327677     202001\n" +
                            "   13.52285     -40.80102     0.0000000E+00 0.0000000E+00  39.01076     112001\n" +
                            "   22.14524     -52.44851     0.0000000E+00 0.0000000E+00  79.94625     022001\n" +
                            "  0.0000000E+00 0.0000000E+00  11.47723      14.14966     0.0000000E+00 103001\n" +
                            "  0.0000000E+00 0.0000000E+00  41.84256      56.61265     0.0000000E+00 013001\n" +
                            "   2.717997     0.8740335     0.0000000E+00 0.0000000E+00  2.906077     004001\n" +
                            "  -17.08280      14.71084     0.0000000E+00 0.0000000E+00 -27.77053     300200\n" +
                            "  -126.7346      75.89983     0.0000000E+00 0.0000000E+00 -276.9955     210200\n" +
                            "  -335.9764      38.41512     0.0000000E+00 0.0000000E+00 -915.1092     120200\n" +
                            "  -311.9297     -139.1483     0.0000000E+00 0.0000000E+00 -1003.283     030200\n" +
                            "  0.0000000E+00 0.0000000E+00 -264.9695     -387.8847     0.0000000E+00 201200\n" +
                            "  0.0000000E+00 0.0000000E+00 -1567.962     -2332.966     0.0000000E+00 111200\n" +
                            "  0.0000000E+00 0.0000000E+00 -2362.874     -3637.375     0.0000000E+00 021200\n" +
                            "  -143.6448     -12.33390     0.0000000E+00 0.0000000E+00 -204.5459     102200\n" +
                            "  -312.5699     -31.66429     0.0000000E+00 0.0000000E+00 -550.9294     012200\n" +
                            "  0.0000000E+00 0.0000000E+00 -224.5380     -436.1193     0.0000000E+00 003200\n" +
                            "  0.0000000E+00 0.0000000E+00 0.6406289     -10.22398     0.0000000E+00 300101\n" +
                            "  0.0000000E+00 0.0000000E+00  101.9937      117.0576     0.0000000E+00 210101\n" +
                            "  0.0000000E+00 0.0000000E+00  658.9969      1154.453     0.0000000E+00 120101\n" +
                            "  0.0000000E+00 0.0000000E+00  1087.785      2146.220     0.0000000E+00 030101\n" +
                            "   11.52291     -41.23618     0.0000000E+00 0.0000000E+00  29.46605     201101\n" +
                            "   49.83773     -245.3861     0.0000000E+00 0.0000000E+00  228.9287     111101\n" +
                            "   62.78900     -319.8422     0.0000000E+00 0.0000000E+00  429.8707     021101\n" +
                            "  0.0000000E+00 0.0000000E+00  103.9233      152.4082     0.0000000E+00 102101\n" +
                            "  0.0000000E+00 0.0000000E+00  347.7059      531.5031     0.0000000E+00 012101\n" +
                            "   32.14808      22.19806     0.0000000E+00 0.0000000E+00  32.32322     003101\n" +
                            " -0.1780253E-01  1.493037     0.0000000E+00 0.0000000E+00 0.5424308     300002\n" +
                            "  -6.370384      29.18160     0.0000000E+00 0.0000000E+00  3.988436     210002\n" +
                            "  -52.80275      94.94584     0.0000000E+00 0.0000000E+00-0.8038055     120002\n" +
                            "  -101.0135      43.51107     0.0000000E+00 0.0000000E+00 -27.37597     030002\n" +
                            "  0.0000000E+00 0.0000000E+00  1.479331      4.334467     0.0000000E+00 201002\n" +
                            "  0.0000000E+00 0.0000000E+00 -3.114656      6.780564     0.0000000E+00 111002\n" +
                            "  0.0000000E+00 0.0000000E+00 -38.35322     -55.96403     0.0000000E+00 021002\n" +
                            "  -1.457671      3.819581     0.0000000E+00 0.0000000E+00 -2.554939     102002\n" +
                            "  -4.035541      15.51019     0.0000000E+00 0.0000000E+00 -11.31831     012002\n" +
                            "  0.0000000E+00 0.0000000E+00 -3.033654     -4.213239     0.0000000E+00 003002\n" +
                            "  0.0000000E+00 0.0000000E+00 -184.6945     -268.4397     0.0000000E+00 200300\n" +
                            "  0.0000000E+00 0.0000000E+00 -1101.427     -1630.438     0.0000000E+00 110300\n" +
                            "  0.0000000E+00 0.0000000E+00 -1690.172     -2629.931     0.0000000E+00 020300\n" +
                            "  -242.8628     -62.85606     0.0000000E+00 0.0000000E+00 -318.0780     101300\n" +
                            "  -532.8280     -158.0263     0.0000000E+00 0.0000000E+00 -856.1953     011300\n" +
                            "  0.0000000E+00 0.0000000E+00 -512.4256     -1049.332     0.0000000E+00 002300\n" +
                            "   10.85714     -65.66580     0.0000000E+00 0.0000000E+00  41.99840     200201\n" +
                            "   19.17867     -395.1136     0.0000000E+00 0.0000000E+00  297.4687     110201\n" +
                            "  -4.688168     -522.0619     0.0000000E+00 0.0000000E+00  529.8371     020201\n" +
                            "  0.0000000E+00 0.0000000E+00  285.9878      475.6252     0.0000000E+00 101201\n" +
                            "  0.0000000E+00 0.0000000E+00  915.8786      1552.196     0.0000000E+00 011201\n" +
                            "   138.5758      135.4722     0.0000000E+00 0.0000000E+00  131.1371     002201\n" +
                            "  0.0000000E+00 0.0000000E+00-0.9175191      3.481044     0.0000000E+00 200102\n" +
                            "  0.0000000E+00 0.0000000E+00 -59.49015     -85.91984     0.0000000E+00 110102\n" +
                            "  0.0000000E+00 0.0000000E+00 -209.7279     -387.8660     0.0000000E+00 020102\n" +
                            "  -13.13420      25.05348     0.0000000E+00 0.0000000E+00 -20.87915     101102\n" +
                            "  -34.50609      97.05586     0.0000000E+00 0.0000000E+00 -79.84410     011102\n" +
                            "  0.0000000E+00 0.0000000E+00 -34.05413     -58.70627     0.0000000E+00 002102\n" +
                            " -0.1789293E-01-0.3382549     0.0000000E+00 0.0000000E+00-0.2879815     200003\n" +
                            " -0.9281772E-01 -5.751303     0.0000000E+00 0.0000000E+00 -2.346908     110003\n" +
                            "   1.126195     -3.414939     0.0000000E+00 0.0000000E+00-0.8451584     020003\n" +
                            "  0.0000000E+00 0.0000000E+00 0.9917627E-01-0.5455678     0.0000000E+00 101003\n" +
                            "  0.0000000E+00 0.0000000E+00  4.181409      3.723892     0.0000000E+00 011003\n" +
                            "  0.9755758    -0.1139596     0.0000000E+00 0.0000000E+00 0.8628723     002003\n" +
                            "  -159.8773     -60.81285     0.0000000E+00 0.0000000E+00 -192.2299     100400\n" +
                            "  -354.3811     -145.5730     0.0000000E+00 0.0000000E+00 -517.7052     010400\n" +
                            "  0.0000000E+00 0.0000000E+00 -602.0881     -1309.581     0.0000000E+00 001400\n" +
                            "  0.0000000E+00 0.0000000E+00  257.7400      479.0817     0.0000000E+00 100301\n" +
                            "  0.0000000E+00 0.0000000E+00  816.6717      1524.621     0.0000000E+00 010301\n" +
                            "   266.0814      319.1501     0.0000000E+00 0.0000000E+00  235.3330     001301\n" +
                            "  -25.75139      38.63250     0.0000000E+00 0.0000000E+00 -35.38350     100202\n" +
                            "  -63.69827      154.6563     0.0000000E+00 0.0000000E+00 -123.9890     010202\n" +
                            "  0.0000000E+00 0.0000000E+00 -114.9115     -232.7833     0.0000000E+00 001202\n" +
                            "  0.0000000E+00 0.0000000E+00  4.310837      4.749452     0.0000000E+00 100103\n" +
                            "  0.0000000E+00 0.0000000E+00  32.26315      45.52587     0.0000000E+00 010103\n" +
                            "   9.344549      1.150430     0.0000000E+00 0.0000000E+00  6.960381     001103\n" +
                            " -0.7534498E-01 0.3607478     0.0000000E+00 0.0000000E+00-0.2684866E-01 100004\n" +
                            "  0.4533524      2.538694     0.0000000E+00 0.0000000E+00-0.6207504     010004\n" +
                            "  0.0000000E+00 0.0000000E+00-0.2119646    -0.2509868     0.0000000E+00 001004\n" +
                            "  0.0000000E+00 0.0000000E+00 -293.7224     -687.0272     0.0000000E+00 000500\n" +
                            "   197.6030      262.6665     0.0000000E+00 0.0000000E+00  160.2710     000401\n" +
                            "  0.0000000E+00 0.0000000E+00 -123.0945     -287.2806     0.0000000E+00 000302\n" +
                            "   20.49992      4.458075     0.0000000E+00 0.0000000E+00  13.60323     000203\n" +
                            "  0.0000000E+00 0.0000000E+00 -1.289583     -2.992268     0.0000000E+00 000104\n" +
                            "  0.1083802    -0.1783390E-01 0.0000000E+00 0.0000000E+00 0.3565332     000005"
            );
        }

        private static CosyArbitraryOrder.CosyMapArbitraryOrder cosyMapEndQs3() {
            return CosyArbitraryOrder.readMap(
                    " -0.3666239     -2.227984     0.0000000E+00 0.0000000E+00-0.6086743     100000\n" +
                            " -0.6322186     -6.569601     0.0000000E+00 0.0000000E+00 -1.772100     010000\n" +
                            "  0.0000000E+00 0.0000000E+00-0.1576085    -0.8778538     0.0000000E+00 001000\n" +
                            "  0.0000000E+00 0.0000000E+00  1.224483     0.4753356     0.0000000E+00 000100\n" +
                            "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00  1.000000     000010\n" +
                            "  0.2648789    -0.5053771E-01 0.0000000E+00 0.0000000E+00 0.6655187     000001\n" +
                            "  -1.481481     -1.558832     0.0000000E+00 0.0000000E+00-0.8219121     200000\n" +
                            "  -9.973249     -12.89005     0.0000000E+00 0.0000000E+00 -5.210718     110000\n" +
                            "  -16.89744     -24.44400     0.0000000E+00 0.0000000E+00 -8.927129     020000\n" +
                            "  0.0000000E+00 0.0000000E+00 -5.074094     -7.746385     0.0000000E+00 101000\n" +
                            "  0.0000000E+00 0.0000000E+00 -14.51968     -22.03748     0.0000000E+00 011000\n" +
                            " -0.6777135     0.2912347     0.0000000E+00 0.0000000E+00-0.3081293     002000\n" +
                            "  0.0000000E+00 0.0000000E+00 -11.43182     -18.79369     0.0000000E+00 100100\n" +
                            "  0.0000000E+00 0.0000000E+00 -31.47513     -47.88932     0.0000000E+00 010100\n" +
                            "  -2.890887      1.725390     0.0000000E+00 0.0000000E+00-0.7084130     001100\n" +
                            "  0.5132264E-01 -1.510904     0.0000000E+00 0.0000000E+00 0.1512456     100001\n" +
                            "  0.3549999     -1.367769     0.0000000E+00 0.0000000E+00  1.344559     010001\n" +
                            "  0.0000000E+00 0.0000000E+00  1.020294      1.228532     0.0000000E+00 001001\n" +
                            "  -2.450013      9.084826     0.0000000E+00 0.0000000E+00 -1.211742     000200\n" +
                            "  0.0000000E+00 0.0000000E+00  2.349620      6.619500     0.0000000E+00 000101\n" +
                            " -0.1360984    -0.7854294E-01 0.0000000E+00 0.0000000E+00-0.5681123     000002\n" +
                            "  -4.488639     0.7584133     0.0000000E+00 0.0000000E+00 -1.636266     300000\n" +
                            "  -43.43793     0.6127313     0.0000000E+00 0.0000000E+00 -18.31722     210000\n" +
                            "  -139.0520     -11.73467     0.0000000E+00 0.0000000E+00 -64.77960     120000\n" +
                            "  -147.0154     -22.35895     0.0000000E+00 0.0000000E+00 -74.43081     030000\n" +
                            "  0.0000000E+00 0.0000000E+00 -8.046291     -7.460795     0.0000000E+00 201000\n" +
                            "  0.0000000E+00 0.0000000E+00 -54.56373     -66.42130     0.0000000E+00 111000\n" +
                            "  0.0000000E+00 0.0000000E+00 -89.79033     -128.3391     0.0000000E+00 021000\n" +
                            " -0.8729723      2.637074     0.0000000E+00 0.0000000E+00 -2.696300     102000\n" +
                            "  -1.461185      9.524759     0.0000000E+00 0.0000000E+00 -7.445673     012000\n" +
                            "  0.0000000E+00 0.0000000E+00 -3.536281     -5.426337     0.0000000E+00 003000\n" +
                            "  0.0000000E+00 0.0000000E+00 -19.62895     -32.45211     0.0000000E+00 200100\n" +
                            "  0.0000000E+00 0.0000000E+00 -130.1999     -246.0177     0.0000000E+00 110100\n" +
                            "  0.0000000E+00 0.0000000E+00 -211.4256     -440.5044     0.0000000E+00 020100\n" +
                            "  -5.931711     -27.03939     0.0000000E+00 0.0000000E+00 -8.156894     101100\n" +
                            "  -11.30280     -68.41288     0.0000000E+00 0.0000000E+00 -21.41543     011100\n" +
                            "  0.0000000E+00 0.0000000E+00 -24.27449     -44.88106     0.0000000E+00 002100\n" +
                            "  0.8038948E-01-0.4107879E-01 0.0000000E+00 0.0000000E+00-0.1338010     200001\n" +
                            "   6.365101      6.825501     0.0000000E+00 0.0000000E+00  2.273069     110001\n" +
                            "   20.83931      27.28339     0.0000000E+00 0.0000000E+00  9.511180     020001\n" +
                            "  0.0000000E+00 0.0000000E+00  2.316332     -6.218187     0.0000000E+00 101001\n" +
                            "  0.0000000E+00 0.0000000E+00  14.82103     -3.076009     0.0000000E+00 011001\n" +
                            "  0.8613866     0.6501935     0.0000000E+00 0.0000000E+00 0.7963566     002001\n" +
                            "  -9.782837     -70.76899     0.0000000E+00 0.0000000E+00 -3.398892     100200\n" +
                            "  -19.95155     -188.1924     0.0000000E+00 0.0000000E+00 -8.647318     010200\n" +
                            "  0.0000000E+00 0.0000000E+00 -59.01766     -122.3162     0.0000000E+00 001200\n" +
                            "  0.0000000E+00 0.0000000E+00  13.40589    -0.2166101     0.0000000E+00 100101\n" +
                            "  0.0000000E+00 0.0000000E+00  55.28662      30.57402     0.0000000E+00 010101\n" +
                            "   6.591505      10.18663     0.0000000E+00 0.0000000E+00  3.364177     001101\n" +
                            "  0.2137368     0.4299508     0.0000000E+00 0.0000000E+00-0.7606924E-01 100002\n" +
                            "  0.5936556      1.790623     0.0000000E+00 0.0000000E+00 -1.069438     010002\n" +
                            "  0.0000000E+00 0.0000000E+00-0.7326317     0.7816230     0.0000000E+00 001002\n" +
                            "  0.0000000E+00 0.0000000E+00 -49.04225     -108.9341     0.0000000E+00 000300\n" +
                            "   9.748419      16.48875     0.0000000E+00 0.0000000E+00  3.853968     000201\n" +
                            "  0.0000000E+00 0.0000000E+00 -3.428847     -2.843125     0.0000000E+00 000102\n" +
                            "  0.1139610     0.1287683     0.0000000E+00 0.0000000E+00 0.4921628     000003\n" +
                            "  -7.220570     0.9469449     0.0000000E+00 0.0000000E+00 -4.034445     400000\n" +
                            "  -111.7244      6.129308     0.0000000E+00 0.0000000E+00 -58.68242     310000\n" +
                            "  -608.7336      6.583704     0.0000000E+00 0.0000000E+00 -309.3630     220000\n" +
                            "  -1417.664     -29.12765     0.0000000E+00 0.0000000E+00 -708.8009     130000\n" +
                            "  -1209.159     -65.19072     0.0000000E+00 0.0000000E+00 -600.8904     040000\n" +
                            "  0.0000000E+00 0.0000000E+00 -12.28447      33.42438     0.0000000E+00 301000\n" +
                            "  0.0000000E+00 0.0000000E+00 -137.6836      258.8123     0.0000000E+00 211000\n" +
                            "  0.0000000E+00 0.0000000E+00 -485.6000      655.8904     0.0000000E+00 121000\n" +
                            "  0.0000000E+00 0.0000000E+00 -556.3857      532.1378     0.0000000E+00 031000\n" +
                            "  0.3608731      66.01699     0.0000000E+00 0.0000000E+00 -9.760549     202000\n" +
                            "   1.019158      376.7316     0.0000000E+00 0.0000000E+00 -59.07417     112000\n" +
                            "  -1.841511      532.6788     0.0000000E+00 0.0000000E+00 -89.27615     022000\n" +
                            "  0.0000000E+00 0.0000000E+00 -14.09600      3.283606     0.0000000E+00 103000\n" +
                            "  0.0000000E+00 0.0000000E+00 -39.69681      2.413269     0.0000000E+00 013000\n" +
                            "  -1.981351    -0.8721455     0.0000000E+00 0.0000000E+00 -2.116612     004000\n" +
                            "  0.0000000E+00 0.0000000E+00 -30.04381      34.26080     0.0000000E+00 300100\n" +
                            "  0.0000000E+00 0.0000000E+00 -323.0206      184.4922     0.0000000E+00 210100\n" +
                            "  0.0000000E+00 0.0000000E+00 -1116.324      184.1330     0.0000000E+00 120100\n" +
                            "  0.0000000E+00 0.0000000E+00 -1267.027     -197.4945     0.0000000E+00 030100\n" +
                            "   15.50662      228.2582     0.0000000E+00 0.0000000E+00 -40.65150     201100\n" +
                            "   84.89932      1215.537     0.0000000E+00 0.0000000E+00 -233.4450     111100\n" +
                            "   105.2422      1590.372     0.0000000E+00 0.0000000E+00 -337.3395     021100\n" +
                            "  0.0000000E+00 0.0000000E+00 -80.16605      17.60103     0.0000000E+00 102100\n" +
                            "  0.0000000E+00 0.0000000E+00 -221.3450      12.27557     0.0000000E+00 012100\n" +
                            "  -21.77516     -36.62429     0.0000000E+00 0.0000000E+00 -17.22341     003100\n" +
                            "  -1.430354      4.727002     0.0000000E+00 0.0000000E+00-0.1591701     300001\n" +
                            "   13.37589      42.81637     0.0000000E+00 0.0000000E+00  10.67524     210001\n" +
                            "   128.3605      143.1744     0.0000000E+00 0.0000000E+00  78.56902     120001\n" +
                            "   223.6268      162.7531     0.0000000E+00 0.0000000E+00  135.1119     030001\n" +
                            "  0.0000000E+00 0.0000000E+00 0.9888116     -25.78850     0.0000000E+00 201001\n" +
                            "  0.0000000E+00 0.0000000E+00  41.44259     -113.9989     0.0000000E+00 111001\n" +
                            "  0.0000000E+00 0.0000000E+00  122.7928     -80.78758     0.0000000E+00 021001\n" +
                            " -0.9769577     -26.77282     0.0000000E+00 0.0000000E+00  5.557837     102001\n" +
                            "  -3.742578     -85.16174     0.0000000E+00 0.0000000E+00  19.59972     012001\n" +
                            "  0.0000000E+00 0.0000000E+00  5.085370     -3.074645     0.0000000E+00 003001\n" +
                            "   38.71083      172.6025     0.0000000E+00 0.0000000E+00 -39.53125     200200\n" +
                            "   209.9687      806.7927     0.0000000E+00 0.0000000E+00 -211.1641     110200\n" +
                            "   266.1287      871.7481     0.0000000E+00 0.0000000E+00 -285.8751     020200\n" +
                            "  0.0000000E+00 0.0000000E+00 -163.2845      14.52011     0.0000000E+00 101200\n" +
                            "  0.0000000E+00 0.0000000E+00 -452.7181     -35.50850     0.0000000E+00 011200\n" +
                            "  -88.37044     -219.5304     0.0000000E+00 0.0000000E+00 -53.01190     002200\n" +
                            "  0.0000000E+00 0.0000000E+00  17.61488     -43.70789     0.0000000E+00 200101\n" +
                            "  0.0000000E+00 0.0000000E+00  197.0552     -79.56355     0.0000000E+00 110101\n" +
                            "  0.0000000E+00 0.0000000E+00  447.6402      225.8732     0.0000000E+00 020101\n" +
                            "  0.2975182     -99.06785     0.0000000E+00 0.0000000E+00  30.94674     101101\n" +
                            "  -4.672379     -262.5423     0.0000000E+00 0.0000000E+00  95.81106     011101\n" +
                            "  0.0000000E+00 0.0000000E+00  47.96080      11.34695     0.0000000E+00 002101\n" +
                            "  0.5090623    -0.4751554     0.0000000E+00 0.0000000E+00 0.5469398     200002\n" +
                            "  -1.707978     -8.837419     0.0000000E+00 0.0000000E+00  1.335025     110002\n" +
                            "  -18.06937     -35.95541     0.0000000E+00 0.0000000E+00 -5.459274     020002\n" +
                            "  0.0000000E+00 0.0000000E+00-0.5862596      8.601513     0.0000000E+00 101002\n" +
                            "  0.0000000E+00 0.0000000E+00 -11.45628      21.79765     0.0000000E+00 011002\n" +
                            " -0.8551584      1.344025     0.0000000E+00 0.0000000E+00 -1.258836     002002\n" +
                            "  0.0000000E+00 0.0000000E+00 -134.9068     -39.41091     0.0000000E+00 100300\n" +
                            "  0.0000000E+00 0.0000000E+00 -379.9333     -162.4127     0.0000000E+00 010300\n" +
                            "  -162.3620     -496.0132     0.0000000E+00 0.0000000E+00 -71.57495     001300\n" +
                            "   8.136526     -25.31207     0.0000000E+00 0.0000000E+00  33.76046     100201\n" +
                            "   14.26637      4.732903     0.0000000E+00 0.0000000E+00  94.50820     010201\n" +
                            "  0.0000000E+00 0.0000000E+00  145.2504      124.9710     0.0000000E+00 001201\n" +
                            "  0.0000000E+00 0.0000000E+00 -11.01810      21.18384     0.0000000E+00 100102\n" +
                            "  0.0000000E+00 0.0000000E+00 -64.88873      31.17963     0.0000000E+00 010102\n" +
                            "  -9.102685     -3.244760     0.0000000E+00 0.0000000E+00 -7.585479     001102\n" +
                            " -0.3014566    -0.4565319     0.0000000E+00 0.0000000E+00 0.3917342E-01 100003\n" +
                            "  -1.442274     -2.666655     0.0000000E+00 0.0000000E+00 0.8472244     010003\n" +
                            "  0.0000000E+00 0.0000000E+00 0.5450120     -1.374251     0.0000000E+00 001003\n" +
                            "  -112.8826     -392.4338     0.0000000E+00 0.0000000E+00 -36.19792     000400\n" +
                            "  0.0000000E+00 0.0000000E+00  148.9512      196.7766     0.0000000E+00 000301\n" +
                            "  -18.18708     -24.22964     0.0000000E+00 0.0000000E+00 -10.33572     000202\n" +
                            "  0.0000000E+00 0.0000000E+00  3.792047    -0.6590756     0.0000000E+00 000103\n" +
                            " -0.1149495    -0.1563274     0.0000000E+00 0.0000000E+00-0.4415054     000004\n" +
                            "  -18.30271     -21.23075     0.0000000E+00 0.0000000E+00 -7.181396     500000\n" +
                            "  -355.8153     -339.5426     0.0000000E+00 0.0000000E+00 -151.4333     410000\n" +
                            "  -2626.688     -2122.320     0.0000000E+00 0.0000000E+00 -1166.118     320000\n" +
                            "  -9367.094     -6516.409     0.0000000E+00 0.0000000E+00 -4258.579     230000\n" +
                            "  -16314.43     -9885.515     0.0000000E+00 0.0000000E+00 -7523.370     140000\n" +
                            "  -11183.11     -5956.656     0.0000000E+00 0.0000000E+00 -5206.034     050000\n" +
                            "  0.0000000E+00 0.0000000E+00 -18.08192      178.6928     0.0000000E+00 401000\n" +
                            "  0.0000000E+00 0.0000000E+00 -318.4306      2148.243     0.0000000E+00 311000\n" +
                            "  0.0000000E+00 0.0000000E+00 -1859.354      9655.272     0.0000000E+00 221000\n" +
                            "  0.0000000E+00 0.0000000E+00 -4535.281      19212.05     0.0000000E+00 131000\n" +
                            "  0.0000000E+00 0.0000000E+00 -4012.883      14237.73     0.0000000E+00 041000\n" +
                            "  -20.50569      199.9222     0.0000000E+00 0.0000000E+00 -22.18214     302000\n" +
                            "  -182.2890      1919.471     0.0000000E+00 0.0000000E+00 -228.8049     212000\n" +
                            "  -543.5095      6059.271     0.0000000E+00 0.0000000E+00 -763.7803     122000\n" +
                            "  -540.3190      6319.838     0.0000000E+00 0.0000000E+00 -834.5055     032000\n" +
                            "  0.0000000E+00 0.0000000E+00 -57.52442      38.93266     0.0000000E+00 203000\n" +
                            "  0.0000000E+00 0.0000000E+00 -346.9682      189.3926     0.0000000E+00 113000\n" +
                            "  0.0000000E+00 0.0000000E+00 -522.5824      231.1951     0.0000000E+00 023000\n" +
                            "  0.8231747      93.81328     0.0000000E+00 0.0000000E+00 -17.58787     104000\n" +
                            "   5.841934      275.5470     0.0000000E+00 0.0000000E+00 -49.07840     014000\n" +
                            "  0.0000000E+00 0.0000000E+00 -9.474308      1.472426     0.0000000E+00 005000\n" +
                            "  0.0000000E+00 0.0000000E+00 -43.94696      349.5353     0.0000000E+00 400100\n" +
                            "  0.0000000E+00 0.0000000E+00 -748.1560      3966.369     0.0000000E+00 310100\n" +
                            "  0.0000000E+00 0.0000000E+00 -4289.253      16815.83     0.0000000E+00 220100\n" +
                            "  0.0000000E+00 0.0000000E+00 -10349.84      31459.23     0.0000000E+00 130100\n" +
                            "  0.0000000E+00 0.0000000E+00 -9102.246      21741.19     0.0000000E+00 040100\n" +
                            "  -73.40603      816.3132     0.0000000E+00 0.0000000E+00 -85.97598     301100\n" +
                            "  -595.3138      7503.970     0.0000000E+00 0.0000000E+00 -879.9659     211100\n" +
                            "  -1628.513      22809.73     0.0000000E+00 0.0000000E+00 -2920.306     121100\n" +
                            "  -1489.753      22979.00     0.0000000E+00 0.0000000E+00 -3171.713     031100\n" +
                            "  0.0000000E+00 0.0000000E+00 -314.8810      275.1177     0.0000000E+00 202100\n" +
                            "  0.0000000E+00 0.0000000E+00 -1858.142      1383.292     0.0000000E+00 112100\n" +
                            "  0.0000000E+00 0.0000000E+00 -2762.362      1733.470     0.0000000E+00 022100\n" +
                            "  -2.064221      710.1859     0.0000000E+00 0.0000000E+00 -153.8125     103100\n" +
                            "   28.04989      2096.536     0.0000000E+00 0.0000000E+00 -423.8050     013100\n" +
                            "  0.0000000E+00 0.0000000E+00 -100.2970    -0.2628678     0.0000000E+00 004100\n" +
                            "  -8.386452      9.546119     0.0000000E+00 0.0000000E+00 -2.671651     400001\n" +
                            "  -27.78197      147.3229     0.0000000E+00 0.0000000E+00  8.929457     310001\n" +
                            "   318.3137      814.7417     0.0000000E+00 0.0000000E+00  273.1269     220001\n" +
                            "   1729.960      1940.581     0.0000000E+00 0.0000000E+00  1106.654     130001\n" +
                            "   2268.215      1725.549     0.0000000E+00 0.0000000E+00  1325.067     040001\n" +
                            "  0.0000000E+00 0.0000000E+00 -8.618634     -82.02310     0.0000000E+00 301001\n" +
                            "  0.0000000E+00 0.0000000E+00  25.76835     -918.6918     0.0000000E+00 211001\n" +
                            "  0.0000000E+00 0.0000000E+00  455.7949     -3157.545     0.0000000E+00 121001\n" +
                            "  0.0000000E+00 0.0000000E+00  903.6633     -3419.453     0.0000000E+00 031001\n" +
                            "  0.5817332E-01 -106.6251     0.0000000E+00 0.0000000E+00  11.51862     202001\n" +
                            " -0.1969059     -855.3062     0.0000000E+00 0.0000000E+00  107.9119     112001\n" +
                            "   7.660549     -1553.940     0.0000000E+00 0.0000000E+00  220.2351     022001\n" +
                            "  0.0000000E+00 0.0000000E+00  27.07036     -39.02708     0.0000000E+00 103001\n" +
                            "  0.0000000E+00 0.0000000E+00  98.28168     -94.29020     0.0000000E+00 013001\n" +
                            "   2.595961     -14.20356     0.0000000E+00 0.0000000E+00  6.129167     004001\n" +
                            "  -39.79049      821.7017     0.0000000E+00 0.0000000E+00 -69.35177     300200\n" +
                            "  -220.5249      7198.323     0.0000000E+00 0.0000000E+00 -726.8186     210200\n" +
                            "  -342.5412      20858.33     0.0000000E+00 0.0000000E+00 -2456.025     120200\n" +
                            "  -84.74443      19995.93     0.0000000E+00 0.0000000E+00 -2700.607     030200\n" +
                            "  0.0000000E+00 0.0000000E+00 -514.7881      826.0321     0.0000000E+00 201200\n" +
                            "  0.0000000E+00 0.0000000E+00 -3026.542      4324.099     0.0000000E+00 111200\n" +
                            "  0.0000000E+00 0.0000000E+00 -4553.098      5553.639     0.0000000E+00 021200\n" +
                            "  -23.80065      2133.061     0.0000000E+00 0.0000000E+00 -531.8533     102200\n" +
                            "   58.36755      6357.426     0.0000000E+00 0.0000000E+00 -1456.917     012200\n" +
                            "  0.0000000E+00 0.0000000E+00 -436.0422     -98.77097     0.0000000E+00 003200\n" +
                            "  0.0000000E+00 0.0000000E+00  4.329849     -244.1095     0.0000000E+00 300101\n" +
                            "  0.0000000E+00 0.0000000E+00  317.7211     -2275.052     0.0000000E+00 210101\n" +
                            "  0.0000000E+00 0.0000000E+00  1922.194     -6651.761     0.0000000E+00 120101\n" +
                            "  0.0000000E+00 0.0000000E+00  3047.257     -6049.684     0.0000000E+00 030101\n" +
                            "  -2.702213     -689.0553     0.0000000E+00 0.0000000E+00  85.54604     201101\n" +
                            "  -62.87681     -4680.673     0.0000000E+00 0.0000000E+00  655.2568     111101\n" +
                            "  -105.8475     -7511.637     0.0000000E+00 0.0000000E+00  1185.472     021101\n" +
                            "  0.0000000E+00 0.0000000E+00  216.1843     -272.5002     0.0000000E+00 102101\n" +
                            "  0.0000000E+00 0.0000000E+00  718.8489     -643.8310     0.0000000E+00 012101\n" +
                            "   43.11175     -69.30547     0.0000000E+00 0.0000000E+00  63.95095     003101\n" +
                            "   3.654396     0.2910153E-01 0.0000000E+00 0.0000000E+00  1.333654     300002\n" +
                            "   20.38828     -27.49053     0.0000000E+00 0.0000000E+00  2.981510     210002\n" +
                            "  -34.57530     -180.2276     0.0000000E+00 0.0000000E+00 -51.62232     120002\n" +
                            "  -190.1832     -284.9096     0.0000000E+00 0.0000000E+00 -153.1012     030002\n" +
                            "  0.0000000E+00 0.0000000E+00  4.565679      18.84388     0.0000000E+00 201002\n" +
                            "  0.0000000E+00 0.0000000E+00 -6.275703      170.5205     0.0000000E+00 111002\n" +
                            "  0.0000000E+00 0.0000000E+00 -99.91445      278.3962     0.0000000E+00 021002\n" +
                            "   3.048492      34.89582     0.0000000E+00 0.0000000E+00 -6.467133     102002\n" +
                            "   13.72750      154.0197     0.0000000E+00 0.0000000E+00 -29.42124     012002\n" +
                            "  0.0000000E+00 0.0000000E+00 -6.597422      10.90575     0.0000000E+00 003002\n" +
                            "  0.0000000E+00 0.0000000E+00 -241.4798      915.3075     0.0000000E+00 200300\n" +
                            "  0.0000000E+00 0.0000000E+00 -1517.582      4794.416     0.0000000E+00 110300\n" +
                            "  0.0000000E+00 0.0000000E+00 -2493.940      6039.262     0.0000000E+00 020300\n" +
                            "  -66.11330      2964.018     0.0000000E+00 0.0000000E+00 -848.9464     101300\n" +
                            "   19.81077      8877.326     0.0000000E+00 0.0000000E+00 -2312.909     011300\n" +
                            "  0.0000000E+00 0.0000000E+00 -987.2348     -529.8105     0.0000000E+00 002300\n" +
                            "  -31.44605     -890.0244     0.0000000E+00 0.0000000E+00  123.8161     200201\n" +
                            "  -292.5301     -5380.623     0.0000000E+00 0.0000000E+00  837.3456     110201\n" +
                            "  -481.4293     -7676.616     0.0000000E+00 0.0000000E+00  1381.233     020201\n" +
                            "  0.0000000E+00 0.0000000E+00  514.6438     -641.6391     0.0000000E+00 101201\n" +
                            "  0.0000000E+00 0.0000000E+00  1654.455     -1474.077     0.0000000E+00 011201\n" +
                            "   228.0609      54.63728     0.0000000E+00 0.0000000E+00  242.9126     002201\n" +
                            "  0.0000000E+00 0.0000000E+00 -3.490451      80.68238     0.0000000E+00 200102\n" +
                            "  0.0000000E+00 0.0000000E+00 -167.5420      470.2943     0.0000000E+00 110102\n" +
                            "  0.0000000E+00 0.0000000E+00 -574.8339      435.1748     0.0000000E+00 020102\n" +
                            "   10.65837      231.1194     0.0000000E+00 0.0000000E+00 -52.45124     101102\n" +
                            "   54.23853      838.0805     0.0000000E+00 0.0000000E+00 -197.4095     011102\n" +
                            "  0.0000000E+00 0.0000000E+00 -75.44701      59.08220     0.0000000E+00 002102\n" +
                            " -0.5504130    -0.3021322     0.0000000E+00 0.0000000E+00-0.7657963     200003\n" +
                            "  -1.115005      5.653220     0.0000000E+00 0.0000000E+00 -4.694278     110003\n" +
                            "   14.06804      39.78002     0.0000000E+00 0.0000000E+00 -1.100402     020003\n" +
                            "  0.0000000E+00 0.0000000E+00-0.3890028     -7.207598     0.0000000E+00 101003\n" +
                            "  0.0000000E+00 0.0000000E+00  7.654837     -28.71709     0.0000000E+00 011003\n" +
                            "  0.8902920     -2.150982     0.0000000E+00 0.0000000E+00  1.644543     002003\n" +
                            "  -90.69505      1481.694     0.0000000E+00 0.0000000E+00 -505.4809     100400\n" +
                            "  -122.6473      4432.028     0.0000000E+00 0.0000000E+00 -1366.794     010400\n" +
                            "  0.0000000E+00 0.0000000E+00 -1191.915     -1127.480     0.0000000E+00 001400\n" +
                            "  0.0000000E+00 0.0000000E+00  417.7312     -478.2677     0.0000000E+00 100301\n" +
                            "  0.0000000E+00 0.0000000E+00  1363.530     -1001.549     0.0000000E+00 010301\n" +
                            "   510.6992      697.5410     0.0000000E+00 0.0000000E+00  400.4282     001301\n" +
                            "   2.255565      256.2382     0.0000000E+00 0.0000000E+00 -82.09817     100202\n" +
                            "   37.46156      773.1926     0.0000000E+00 0.0000000E+00 -272.2496     010202\n" +
                            "  0.0000000E+00 0.0000000E+00 -261.0673      22.65799     0.0000000E+00 001202\n" +
                            "  0.0000000E+00 0.0000000E+00  7.685230     -31.81691     0.0000000E+00 100103\n" +
                            "  0.0000000E+00 0.0000000E+00  64.82563     -90.90478     0.0000000E+00 010103\n" +
                            "   11.25475     -5.399056     0.0000000E+00 0.0000000E+00  12.53671     001103\n" +
                            "  0.3423937     0.5922626     0.0000000E+00 0.0000000E+00-0.2521321E-01 100004\n" +
                            "   2.224184      3.999654     0.0000000E+00 0.0000000E+00-0.6646898     010004\n" +
                            "  0.0000000E+00 0.0000000E+00-0.4166674      1.695271     0.0000000E+00 001004\n" +
                            "  0.0000000E+00 0.0000000E+00 -623.7112     -883.2641     0.0000000E+00 000500\n" +
                            "   425.8445      909.3832     0.0000000E+00 0.0000000E+00  240.0863     000401\n" +
                            "  0.0000000E+00 0.0000000E+00 -298.3357     -154.8156     0.0000000E+00 000302\n" +
                            "   27.19129      19.79919     0.0000000E+00 0.0000000E+00  20.57394     000203\n" +
                            "  0.0000000E+00 0.0000000E+00 -3.898911      3.629507     0.0000000E+00 000104\n" +
                            "  0.1267531     0.1879224     0.0000000E+00 0.0000000E+00 0.4078678     000005"
            );
        }

        private static CosyArbitraryOrder.CosyMapArbitraryOrder cosyMapEnterCct345_1() {
            return CosyArbitraryOrder.readMap(
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
        }

        private static CosyArbitraryOrder.CosyMapArbitraryOrder cosyMapExitCct345_1() {
            return CosyArbitraryOrder.readMap(
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
        }

        private static CosyArbitraryOrder.CosyMapArbitraryOrder cosyMapIsoc() {
            return CosyArbitraryOrder.readMap(
                    "  0.8560639     0.6426617     0.0000000E+00 0.0000000E+00 0.9122470E-02 100000\n" +
                            " -0.4157002     0.8560639     0.0000000E+00 0.0000000E+00-0.2043148E-02 010000\n" +
                            "  0.0000000E+00 0.0000000E+00 -1.013995     0.1209793     0.0000000E+00 001000\n" +
                            "  0.0000000E+00 0.0000000E+00 0.2329814     -1.013995     0.0000000E+00 000100\n" +
                            "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00  1.000000     000010\n" +
                            "  0.2043148E-02-0.9122470E-02 0.0000000E+00 0.0000000E+00  1.274693     000001\n" +
                            "  -5.639226    -0.9384986     0.0000000E+00 0.0000000E+00-0.1807654     200000\n" +
                            "  -15.67771     0.4203887     0.0000000E+00 0.0000000E+00-0.3369669E-01 110000\n" +
                            "   1.755658      9.054789     0.0000000E+00 0.0000000E+00-0.3261315E-01 020000\n" +
                            "  0.0000000E+00 0.0000000E+00 -16.58083     0.7224107E-01 0.0000000E+00 101000\n" +
                            "  0.0000000E+00 0.0000000E+00 -48.47881    -0.1617974E-01 0.0000000E+00 011000\n" +
                            "  -2.919110     -1.062605     0.0000000E+00 0.0000000E+00-0.7957923     002000\n" +
                            "  0.0000000E+00 0.0000000E+00 -3.327428      16.96123     0.0000000E+00 100100\n" +
                            "  0.0000000E+00 0.0000000E+00 0.7452398      48.39362     0.0000000E+00 010100\n" +
                            "   49.06712      17.21543     0.0000000E+00 0.0000000E+00  1.114809     001100\n" +
                            " -0.1307193E-01 0.2878378     0.0000000E+00 0.0000000E+00-0.5886283E-02 100001\n" +
                            " -0.6984560E-01-0.1791349     0.0000000E+00 0.0000000E+00 0.1318343E-02 010001\n" +
                            "  0.0000000E+00 0.0000000E+00-0.2834773     -1.478990     0.0000000E+00 001001\n" +
                            "  -5.269497     -3.618511     0.0000000E+00 0.0000000E+00 -5.843512     000200\n" +
                            "  0.0000000E+00 0.0000000E+00  11.59086    -0.7596009     0.0000000E+00 000101\n" +
                            " -0.3272354E-03 0.3466170E-02 0.0000000E+00 0.0000000E+00 -1.085330     000002\n" +
                            "   16.16432      7.152255     0.0000000E+00 0.0000000E+00  1.770827     300000\n" +
                            "   91.45431      49.65737     0.0000000E+00 0.0000000E+00  7.028567     210000\n" +
                            "   159.4623      119.5888     0.0000000E+00 0.0000000E+00  5.195335     120000\n" +
                            "   87.31451      104.1631     0.0000000E+00 0.0000000E+00 -1.264801     030000\n" +
                            "  0.0000000E+00 0.0000000E+00 -126.0283     -49.36323     0.0000000E+00 201000\n" +
                            "  0.0000000E+00 0.0000000E+00 -770.5492     -283.7281     0.0000000E+00 111000\n" +
                            "  0.0000000E+00 0.0000000E+00 -1173.503     -408.8499     0.0000000E+00 021000\n" +
                            "  -173.7890     -61.78512     0.0000000E+00 0.0000000E+00 -12.46595     102000\n" +
                            "  -505.3292     -179.9721     0.0000000E+00 0.0000000E+00 -37.53256     012000\n" +
                            "  0.0000000E+00 0.0000000E+00  6.733636      7.991402     0.0000000E+00 003000\n" +
                            "  0.0000000E+00 0.0000000E+00 -290.3437     -105.1017     0.0000000E+00 200100\n" +
                            "  0.0000000E+00 0.0000000E+00 -1711.910     -562.3619     0.0000000E+00 110100\n" +
                            "  0.0000000E+00 0.0000000E+00 -2515.298     -746.1316     0.0000000E+00 020100\n" +
                            "  -270.6305     -81.13163     0.0000000E+00 0.0000000E+00 -25.70584     101100\n" +
                            "  -618.9907     -177.8512     0.0000000E+00 0.0000000E+00 -40.69118     011100\n" +
                            "  0.0000000E+00 0.0000000E+00 -109.7306     -8.899062     0.0000000E+00 002100\n" +
                            "   2.004007    -0.3124385     0.0000000E+00 0.0000000E+00 -1.181603     200001\n" +
                            "   13.46064     -1.071326     0.0000000E+00 0.0000000E+00 -6.962070     110001\n" +
                            "  -2.663009     -9.398257     0.0000000E+00 0.0000000E+00 -10.12157     020001\n" +
                            "  0.0000000E+00 0.0000000E+00 -1.329634      1.466792     0.0000000E+00 101001\n" +
                            "  0.0000000E+00 0.0000000E+00  28.32370      3.999936     0.0000000E+00 011001\n" +
                            "   39.23853      13.34955     0.0000000E+00 0.0000000E+00  3.843719     002001\n" +
                            "   1069.301      394.1117     0.0000000E+00 0.0000000E+00  109.4303     100200\n" +
                            "   2779.460      1028.775     0.0000000E+00 0.0000000E+00  294.7902     010200\n" +
                            "  0.0000000E+00 0.0000000E+00  24.93993      118.5896     0.0000000E+00 001200\n" +
                            "  0.0000000E+00 0.0000000E+00 -1.259687      3.143039     0.0000000E+00 100101\n" +
                            "  0.0000000E+00 0.0000000E+00  27.02222     -8.407664     0.0000000E+00 010101\n" +
                            "   12.13720      10.96243     0.0000000E+00 0.0000000E+00 0.5048720     001101\n" +
                            "  -3.602654     -1.257306     0.0000000E+00 0.0000000E+00 0.6508856E-01 100002\n" +
                            "  -10.10198     -3.396756     0.0000000E+00 0.0000000E+00 0.3911656E-01 010002\n" +
                            "  0.0000000E+00 0.0000000E+00  7.632976      2.479428     0.0000000E+00 001002\n" +
                            "  0.0000000E+00 0.0000000E+00 -308.1684     -37.71834     0.0000000E+00 000300\n" +
                            "  -291.3905     -107.5484     0.0000000E+00 0.0000000E+00 -12.90355     000201\n" +
                            "  0.0000000E+00 0.0000000E+00  8.099066      7.949556     0.0000000E+00 000102\n" +
                            "  0.5321035E-01 0.7664841E-03 0.0000000E+00 0.0000000E+00 0.9119077     000003\n" +
                            "  -36.41423     -5.141470     0.0000000E+00 0.0000000E+00 -3.275935     400000\n" +
                            "  -240.0996      12.33315     0.0000000E+00 0.0000000E+00  1.074435     310000\n" +
                            "  -420.6995      315.1830     0.0000000E+00 0.0000000E+00  107.6221     220000\n" +
                            "   64.55341      1009.924     0.0000000E+00 0.0000000E+00  297.9399     130000\n" +
                            "   460.3484      949.0121     0.0000000E+00 0.0000000E+00  217.9246     040000\n" +
                            "  0.0000000E+00 0.0000000E+00 -161.2702     -89.00101     0.0000000E+00 301000\n" +
                            "  0.0000000E+00 0.0000000E+00 -1796.576     -941.0548     0.0000000E+00 211000\n" +
                            "  0.0000000E+00 0.0000000E+00 -6353.740     -3206.110     0.0000000E+00 121000\n" +
                            "  0.0000000E+00 0.0000000E+00 -7259.987     -3550.254     0.0000000E+00 031000\n" +
                            "  -664.6075     -269.1779     0.0000000E+00 0.0000000E+00 -22.54046     202000\n" +
                            "  -4462.299     -1779.901     0.0000000E+00 0.0000000E+00 -245.0296     112000\n" +
                            "  -7319.744     -2883.760     0.0000000E+00 0.0000000E+00 -517.7367     022000\n" +
                            "  0.0000000E+00 0.0000000E+00  336.3040      152.4810     0.0000000E+00 103000\n" +
                            "  0.0000000E+00 0.0000000E+00  947.5939      436.9227     0.0000000E+00 013000\n" +
                            "  -144.6188     -51.60708     0.0000000E+00 0.0000000E+00 -14.54469     004000\n" +
                            "  0.0000000E+00 0.0000000E+00 -620.3497     -275.0373     0.0000000E+00 300100\n" +
                            "  0.0000000E+00 0.0000000E+00 -6170.583     -2711.685     0.0000000E+00 210100\n" +
                            "  0.0000000E+00 0.0000000E+00 -20241.24     -8793.095     0.0000000E+00 120100\n" +
                            "  0.0000000E+00 0.0000000E+00 -21928.72     -9381.519     0.0000000E+00 030100\n" +
                            "  -6315.465     -2290.160     0.0000000E+00 0.0000000E+00 -428.5664     201100\n" +
                            "  -36340.32     -13144.12     0.0000000E+00 0.0000000E+00 -2755.286     111100\n" +
                            "  -52190.72     -18843.72     0.0000000E+00 0.0000000E+00 -4297.396     021100\n" +
                            "  0.0000000E+00 0.0000000E+00 -1001.481     -446.2677     0.0000000E+00 102100\n" +
                            "  0.0000000E+00 0.0000000E+00 -3235.374     -1402.188     0.0000000E+00 012100\n" +
                            "  -504.8971     -167.2647     0.0000000E+00 0.0000000E+00  1.149901     003100\n" +
                            "   33.40615      13.64138     0.0000000E+00 0.0000000E+00 -3.944353     300001\n" +
                            "   278.1537      94.47620     0.0000000E+00 0.0000000E+00 -35.04142     210001\n" +
                            "   854.7247      226.4977     0.0000000E+00 0.0000000E+00 -93.62782     120001\n" +
                            "   892.8239      175.5080     0.0000000E+00 0.0000000E+00 -84.54943     030001\n" +
                            "  0.0000000E+00 0.0000000E+00  74.02566      11.62474     0.0000000E+00 201001\n" +
                            "  0.0000000E+00 0.0000000E+00  814.4235      209.7534     0.0000000E+00 111001\n" +
                            "  0.0000000E+00 0.0000000E+00  1813.465      514.6235     0.0000000E+00 021001\n" +
                            "   421.1564      128.1223     0.0000000E+00 0.0000000E+00  32.52335     102001\n" +
                            "   1589.805      502.9141     0.0000000E+00 0.0000000E+00  139.5203     012001\n" +
                            "  0.0000000E+00 0.0000000E+00 -131.0818     -53.50127     0.0000000E+00 003001\n" +
                            "  -11737.05     -4024.428     0.0000000E+00 0.0000000E+00 -1331.111     200200\n" +
                            "  -57844.72     -19786.01     0.0000000E+00 0.0000000E+00 -6405.500     110200\n" +
                            "  -70129.25     -23913.22     0.0000000E+00 0.0000000E+00 -7601.880     020200\n" +
                            "  0.0000000E+00 0.0000000E+00 -3718.433     -1687.808     0.0000000E+00 101200\n" +
                            "  0.0000000E+00 0.0000000E+00 -10108.96     -4497.967     0.0000000E+00 011200\n" +
                            "  -1386.818     -458.4714     0.0000000E+00 0.0000000E+00 -360.1903     002200\n" +
                            "  0.0000000E+00 0.0000000E+00  314.8816      81.57111     0.0000000E+00 200101\n" +
                            "  0.0000000E+00 0.0000000E+00  2612.651      714.0580     0.0000000E+00 110101\n" +
                            "  0.0000000E+00 0.0000000E+00  4977.134      1318.139     0.0000000E+00 020101\n" +
                            "   3408.974      1171.063     0.0000000E+00 0.0000000E+00  288.3815     101101\n" +
                            "   9674.273      3338.741     0.0000000E+00 0.0000000E+00  821.1095     011101\n" +
                            "  0.0000000E+00 0.0000000E+00  44.87209     -19.24840     0.0000000E+00 002101\n" +
                            "   4.712806      1.827457     0.0000000E+00 0.0000000E+00  2.711218     200002\n" +
                            "   15.60972      8.110417     0.0000000E+00 0.0000000E+00  17.43549     110002\n" +
                            "   30.33918      15.35879     0.0000000E+00 0.0000000E+00  28.37169     020002\n" +
                            "  0.0000000E+00 0.0000000E+00 0.4871138     -3.347223     0.0000000E+00 101002\n" +
                            "  0.0000000E+00 0.0000000E+00 -11.01725     -6.024555     0.0000000E+00 011002\n" +
                            "  -79.66628     -21.70399     0.0000000E+00 0.0000000E+00 -10.47781     002002\n" +
                            "  0.0000000E+00 0.0000000E+00  5937.325      2541.704     0.0000000E+00 100300\n" +
                            "  0.0000000E+00 0.0000000E+00  15370.30      6631.594     0.0000000E+00 010300\n" +
                            "   4783.588      1782.245     0.0000000E+00 0.0000000E+00  579.8117     001300\n" +
                            "   4591.787      1769.948     0.0000000E+00 0.0000000E+00  301.7917     100201\n" +
                            "   10274.76      3980.690     0.0000000E+00 0.0000000E+00  505.6356     010201\n" +
                            "  0.0000000E+00 0.0000000E+00  141.2262     -2.946112     0.0000000E+00 001201\n" +
                            "  0.0000000E+00 0.0000000E+00 0.9977426     -13.07779     0.0000000E+00 100102\n" +
                            "  0.0000000E+00 0.0000000E+00 -38.55618     -25.64447     0.0000000E+00 010102\n" +
                            "  -445.3514     -156.6984     0.0000000E+00 0.0000000E+00 -30.21464     001102\n" +
                            "   4.692432     0.9202861     0.0000000E+00 0.0000000E+00-0.1701047     100003\n" +
                            "   18.01866      4.287810     0.0000000E+00 0.0000000E+00-0.1591082     010003\n" +
                            "  0.0000000E+00 0.0000000E+00 -9.852122     -1.434638     0.0000000E+00 001003\n" +
                            "   4515.316      1678.504     0.0000000E+00 0.0000000E+00  235.6050     000400\n" +
                            "  0.0000000E+00 0.0000000E+00 -1566.313     -919.2864     0.0000000E+00 000301\n" +
                            "  -138.1797     -95.62678     0.0000000E+00 0.0000000E+00 0.9844651     000202\n" +
                            "  0.0000000E+00 0.0000000E+00 -30.46682     -10.65260     0.0000000E+00 000103\n" +
                            " -0.1102648     0.9097688E-03 0.0000000E+00 0.0000000E+00-0.7743478     000004\n" +
                            "   71.81583      43.32215     0.0000000E+00 0.0000000E+00  40.28738     500000\n" +
                            "   488.8054      519.1212     0.0000000E+00 0.0000000E+00  438.0894     410000\n" +
                            "   600.2871      2656.822     0.0000000E+00 0.0000000E+00  2013.330     320000\n" +
                            "  -2282.531      7451.866     0.0000000E+00 0.0000000E+00  5037.724     230000\n" +
                            "  -6126.536      11491.58     0.0000000E+00 0.0000000E+00  6903.726     140000\n" +
                            "  -3712.388      7639.247     0.0000000E+00 0.0000000E+00  4040.481     050000\n" +
                            "  0.0000000E+00 0.0000000E+00  447.4095      52.33914     0.0000000E+00 401000\n" +
                            "  0.0000000E+00 0.0000000E+00  3932.162      50.49063     0.0000000E+00 311000\n" +
                            "  0.0000000E+00 0.0000000E+00  11726.49     -2208.704     0.0000000E+00 221000\n" +
                            "  0.0000000E+00 0.0000000E+00  12165.34     -9080.527     0.0000000E+00 131000\n" +
                            "  0.0000000E+00 0.0000000E+00  840.7026     -10234.67     0.0000000E+00 041000\n" +
                            "   3522.312      1075.420     0.0000000E+00 0.0000000E+00  191.7591     302000\n" +
                            "   26660.19      7793.277     0.0000000E+00 0.0000000E+00  1645.697     212000\n" +
                            "   65608.40      18081.94     0.0000000E+00 0.0000000E+00  4425.028     122000\n" +
                            "   51801.58      13064.86     0.0000000E+00 0.0000000E+00  3664.573     032000\n" +
                            "  0.0000000E+00 0.0000000E+00  4566.135      2066.764     0.0000000E+00 203000\n" +
                            "  0.0000000E+00 0.0000000E+00  27107.67      12288.65     0.0000000E+00 113000\n" +
                            "  0.0000000E+00 0.0000000E+00  40103.34      18199.19     0.0000000E+00 023000\n" +
                            "  -2313.997     -880.5636     0.0000000E+00 0.0000000E+00 -314.8966     104000\n" +
                            "  -6974.276     -2644.804     0.0000000E+00 0.0000000E+00 -992.7155     014000\n" +
                            "  0.0000000E+00 0.0000000E+00  375.1020      166.7964     0.0000000E+00 005000\n" +
                            "  0.0000000E+00 0.0000000E+00  555.4647     -45.27870     0.0000000E+00 400100\n" +
                            "  0.0000000E+00 0.0000000E+00  2832.316     -1949.253     0.0000000E+00 310100\n" +
                            "  0.0000000E+00 0.0000000E+00 -3409.816     -14704.98     0.0000000E+00 220100\n" +
                            "  0.0000000E+00 0.0000000E+00 -37695.90     -40753.78     0.0000000E+00 130100\n" +
                            "  0.0000000E+00 0.0000000E+00 -50900.33     -38800.23     0.0000000E+00 040100\n" +
                            "   14549.77      4119.940     0.0000000E+00 0.0000000E+00  2593.031     301100\n" +
                            "   81450.68      19786.86     0.0000000E+00 0.0000000E+00  17559.09     211100\n" +
                            "   110640.9      13194.94     0.0000000E+00 0.0000000E+00  36187.92     121100\n" +
                            "  -9610.399     -27872.55     0.0000000E+00 0.0000000E+00  20504.50     031100\n" +
                            "  0.0000000E+00 0.0000000E+00  16286.42      7478.198     0.0000000E+00 202100\n" +
                            "  0.0000000E+00 0.0000000E+00  86916.24      39894.06     0.0000000E+00 112100\n" +
                            "  0.0000000E+00 0.0000000E+00  115384.2      52928.16     0.0000000E+00 022100\n" +
                            "  -9787.973     -3516.721     0.0000000E+00 0.0000000E+00  755.1214     103100\n" +
                            "  -27009.01     -9798.964     0.0000000E+00 0.0000000E+00  2158.680     013100\n" +
                            "  0.0000000E+00 0.0000000E+00  678.1850      341.7039     0.0000000E+00 004100\n" +
                            "   9.113978    -0.4275915     0.0000000E+00 0.0000000E+00 -16.10490     400001\n" +
                            "   343.7481      48.05047     0.0000000E+00 0.0000000E+00 -221.5818     310001\n" +
                            "   2019.688      189.5257     0.0000000E+00 0.0000000E+00 -1145.077     220001\n" +
                            "   4480.887      18.17159     0.0000000E+00 0.0000000E+00 -2475.485     130001\n" +
                            "   3725.769     -342.9678     0.0000000E+00 0.0000000E+00 -1854.132     040001\n" +
                            "  0.0000000E+00 0.0000000E+00  67.14839      28.97999     0.0000000E+00 301001\n" +
                            "  0.0000000E+00 0.0000000E+00  2041.247      889.0741     0.0000000E+00 211001\n" +
                            "  0.0000000E+00 0.0000000E+00  11321.67      4919.130     0.0000000E+00 121001\n" +
                            "  0.0000000E+00 0.0000000E+00  17282.97      7465.805     0.0000000E+00 031001\n" +
                            "  -1977.358     -728.0282     0.0000000E+00 0.0000000E+00 -233.5329     202001\n" +
                            "  -6602.743     -2479.668     0.0000000E+00 0.0000000E+00 -951.4451     112001\n" +
                            "  -1672.862     -783.0062     0.0000000E+00 0.0000000E+00 -589.5685     022001\n" +
                            "  0.0000000E+00 0.0000000E+00 -2611.827     -1055.094     0.0000000E+00 103001\n" +
                            "  0.0000000E+00 0.0000000E+00 -8139.070     -3339.315     0.0000000E+00 013001\n" +
                            "   710.4301      235.7323     0.0000000E+00 0.0000000E+00  119.3071     004001\n" +
                            "   17704.79      4643.499     0.0000000E+00 0.0000000E+00  5903.869     300200\n" +
                            "   52184.44      6428.225     0.0000000E+00 0.0000000E+00  32738.55     210200\n" +
                            "  -108253.7     -67968.09     0.0000000E+00 0.0000000E+00  49236.91     120200\n" +
                            "  -321527.9     -137965.0     0.0000000E+00 0.0000000E+00  10906.57     030200\n" +
                            "  0.0000000E+00 0.0000000E+00 -31379.02     -13579.21     0.0000000E+00 201200\n" +
                            "  0.0000000E+00 0.0000000E+00 -191380.5     -82969.16     0.0000000E+00 111200\n" +
                            "  0.0000000E+00 0.0000000E+00 -289682.3     -125773.7     0.0000000E+00 021200\n" +
                            "  -61355.29     -21976.50     0.0000000E+00 0.0000000E+00 -1778.637     102200\n" +
                            "  -175509.7     -62674.85     0.0000000E+00 0.0000000E+00 -7587.377     012200\n" +
                            "  0.0000000E+00 0.0000000E+00  823.1474      407.8332     0.0000000E+00 003200\n" +
                            "  0.0000000E+00 0.0000000E+00  489.6557      203.9511     0.0000000E+00 300101\n" +
                            "  0.0000000E+00 0.0000000E+00  8997.230      3663.035     0.0000000E+00 210101\n" +
                            "  0.0000000E+00 0.0000000E+00  42269.19      17046.17     0.0000000E+00 120101\n" +
                            "  0.0000000E+00 0.0000000E+00  58983.09      23560.16     0.0000000E+00 030101\n" +
                            "  -5388.471     -2448.169     0.0000000E+00 0.0000000E+00 -838.6991     201101\n" +
                            "   6694.252     -644.1579     0.0000000E+00 0.0000000E+00 -304.8974     111101\n" +
                            "   62627.08      17895.63     0.0000000E+00 0.0000000E+00  6075.869     021101\n" +
                            "  0.0000000E+00 0.0000000E+00 -7334.638     -3541.731     0.0000000E+00 102101\n" +
                            "  0.0000000E+00 0.0000000E+00 -17289.30     -8729.534     0.0000000E+00 012101\n" +
                            "   3651.366      1203.742     0.0000000E+00 0.0000000E+00 -147.6138     003101\n" +
                            "  -40.07766     -8.445786     0.0000000E+00 0.0000000E+00 0.7920398     300002\n" +
                            "  -483.7009     -107.8606     0.0000000E+00 0.0000000E+00  44.46418     210002\n" +
                            "  -1872.650     -392.8671     0.0000000E+00 0.0000000E+00  226.2858     120002\n" +
                            "  -2295.360     -429.1882     0.0000000E+00 0.0000000E+00  306.9492     030002\n" +
                            "  0.0000000E+00 0.0000000E+00 -83.22290     -13.69080     0.0000000E+00 201002\n" +
                            "  0.0000000E+00 0.0000000E+00 -950.1486     -214.1031     0.0000000E+00 111002\n" +
                            "  0.0000000E+00 0.0000000E+00 -2449.073     -628.5141     0.0000000E+00 021002\n" +
                            "   120.6712      103.1023     0.0000000E+00 0.0000000E+00 -8.848013     102002\n" +
                            "  -940.6101     -105.9714     0.0000000E+00 0.0000000E+00 -174.4354     012002\n" +
                            "  0.0000000E+00 0.0000000E+00  475.6016      165.9557     0.0000000E+00 003002\n" +
                            "  0.0000000E+00 0.0000000E+00 -101012.5     -43847.02     0.0000000E+00 200300\n" +
                            "  0.0000000E+00 0.0000000E+00 -522670.4     -226721.3     0.0000000E+00 110300\n" +
                            "  0.0000000E+00 0.0000000E+00 -672359.4     -291245.8     0.0000000E+00 020300\n" +
                            "  -193885.0     -68308.70     0.0000000E+00 0.0000000E+00 -29974.73     101300\n" +
                            "  -509999.3     -179932.9     0.0000000E+00 0.0000000E+00 -77551.39     011300\n" +
                            "  0.0000000E+00 0.0000000E+00 -10679.62     -4093.061     0.0000000E+00 002300\n" +
                            "   2804.801     -442.6974     0.0000000E+00 0.0000000E+00  123.2518     200201\n" +
                            "   66918.78      16617.59     0.0000000E+00 0.0000000E+00  7920.820     110201\n" +
                            "   153517.9      45869.57     0.0000000E+00 0.0000000E+00  19278.48     020201\n" +
                            "  0.0000000E+00 0.0000000E+00  21175.55      8165.100     0.0000000E+00 101201\n" +
                            "  0.0000000E+00 0.0000000E+00  65983.20      25736.71     0.0000000E+00 011201\n" +
                            "   18598.26      6208.636     0.0000000E+00 0.0000000E+00  1505.716     002201\n" +
                            "  0.0000000E+00 0.0000000E+00 -375.8193     -86.98937     0.0000000E+00 200102\n" +
                            "  0.0000000E+00 0.0000000E+00 -3457.173     -873.1619     0.0000000E+00 110102\n" +
                            "  0.0000000E+00 0.0000000E+00 -7479.202     -1902.270     0.0000000E+00 020102\n" +
                            "  -2447.610     -344.5330     0.0000000E+00 0.0000000E+00 -324.4347     101102\n" +
                            "  -13233.67     -3242.395     0.0000000E+00 0.0000000E+00 -1614.323     011102\n" +
                            "  0.0000000E+00 0.0000000E+00  1272.493      568.3512     0.0000000E+00 002102\n" +
                            "  -11.00051     -2.527247     0.0000000E+00 0.0000000E+00 -3.459269     200003\n" +
                            "  -57.18659     -14.60486     0.0000000E+00 0.0000000E+00 -26.61210     110003\n" +
                            "  -80.44698     -23.18837     0.0000000E+00 0.0000000E+00 -50.57407     020003\n" +
                            "  0.0000000E+00 0.0000000E+00  15.25855      7.283361     0.0000000E+00 101003\n" +
                            "  0.0000000E+00 0.0000000E+00  27.18486      13.66047     0.0000000E+00 011003\n" +
                            "   55.19225      5.291407     0.0000000E+00 0.0000000E+00  19.16435     002003\n" +
                            "  -57157.60     -20003.99     0.0000000E+00 0.0000000E+00  2192.396     100400\n" +
                            "  -145178.4     -50668.70     0.0000000E+00 0.0000000E+00  7954.759     010400\n" +
                            "  0.0000000E+00 0.0000000E+00  22358.66      10087.77     0.0000000E+00 001400\n" +
                            "  0.0000000E+00 0.0000000E+00  39677.32      19493.43     0.0000000E+00 100301\n" +
                            "  0.0000000E+00 0.0000000E+00  98182.04      48306.51     0.0000000E+00 010301\n" +
                            "   36394.76      13731.82     0.0000000E+00 0.0000000E+00  4902.335     001301\n" +
                            "  -8317.421     -2191.460     0.0000000E+00 0.0000000E+00 -825.5113     100202\n" +
                            "  -27780.78     -8147.091     0.0000000E+00 0.0000000E+00 -2567.132     010202\n" +
                            "  0.0000000E+00 0.0000000E+00 -1920.100     -785.3652     0.0000000E+00 001202\n" +
                            "  0.0000000E+00 0.0000000E+00  48.11060      31.79242     0.0000000E+00 100103\n" +
                            "  0.0000000E+00 0.0000000E+00  146.2367      80.68702     0.0000000E+00 010103\n" +
                            "   747.0386      187.8650     0.0000000E+00 0.0000000E+00  84.24881     001103\n" +
                            "  -4.715653    -0.5832457     0.0000000E+00 0.0000000E+00 0.2977024     100004\n" +
                            "  -22.08730     -4.009692     0.0000000E+00 0.0000000E+00 0.2557065     010004\n" +
                            "  0.0000000E+00 0.0000000E+00  7.843121    -0.2459141     0.0000000E+00 001004\n" +
                            "  0.0000000E+00 0.0000000E+00  28352.63      13276.50     0.0000000E+00 000500\n" +
                            "  -3463.577     -554.0543     0.0000000E+00 0.0000000E+00 -2216.909     000401\n" +
                            "  0.0000000E+00 0.0000000E+00 -920.6223     -784.8926     0.0000000E+00 000302\n" +
                            "   995.6954      332.0163     0.0000000E+00 0.0000000E+00  47.26148     000203\n" +
                            "  0.0000000E+00 0.0000000E+00  44.85100      9.219070     0.0000000E+00 000104\n" +
                            "  0.2345659     0.1386849E-01 0.0000000E+00 0.0000000E+00 0.6519129     000005"
            );
        }
    }

    private static enum CosyMap第二段只有二极CCT {
        EXIT_CCT0(CosyArbitraryOrder.readMap(
                "  0.3826832     -1.231840     0.0000000E+00 0.0000000E+00-0.5162462     100000\n" +
                        "   1.611349     -2.573732     0.0000000E+00 0.0000000E+00 -1.497700     010000\n" +
                        "  0.0000000E+00 0.0000000E+00  1.000000     0.0000000E+00 0.0000000E+00 001000\n" +
                        "  0.0000000E+00 0.0000000E+00  3.283573      1.000000     0.0000000E+00 000100\n" +
                        "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00  1.000000     000010\n" +
                        "  0.2587086     0.5162462     0.0000000E+00 0.0000000E+00 0.5796955     000001\n" +
                        " -0.5690357     0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00 200000\n" +
                        "  -2.377818     0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00 110000\n" +
                        "  -2.340529    -0.4619398     0.0000000E+00 0.0000000E+00-0.8641293     020000\n" +
                        "  0.0000000E+00 0.0000000E+00 0.9238796     0.0000000E+00 0.0000000E+00 100100\n" +
                        "  0.0000000E+00 0.0000000E+00  2.680299     0.0000000E+00 0.0000000E+00 010100\n" +
                        "  0.4769494    -0.5252777E-15 0.0000000E+00 0.0000000E+00 0.1798557     100001\n" +
                        "  0.4833029      1.651988     0.0000000E+00 0.0000000E+00  1.214110     010001\n" +
                        " -0.2314938    -0.4619398     0.0000000E+00 0.0000000E+00-0.8641293     000200\n" +
                        "  0.0000000E+00 0.0000000E+00 -1.728259     0.6661338E-15 0.0000000E+00 000101\n" +
                        " -0.1450072    -0.8992784E-01 0.0000000E+00 0.0000000E+00-0.5046715     000002\n" +
                        "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00-0.1305611     300000\n" +
                        "  0.3330669E-15 0.1124729E-14 0.0000000E+00 0.0000000E+00-0.8183598     210000\n" +
                        " -0.4267768     0.2650657E-14 0.0000000E+00 0.0000000E+00 -1.709832     120000\n" +
                        " -0.4324620     -1.478207     0.0000000E+00 0.0000000E+00 -1.880150     030000\n" +
                        "  0.0000000E+00 0.0000000E+00 0.1110223E-15-0.5332916E-15 0.0000000E+00 110100\n" +
                        "  0.0000000E+00 0.0000000E+00  1.546455    -0.3138070E-15 0.0000000E+00 020100\n" +
                        "  0.3179662     0.0000000E+00 0.0000000E+00 0.0000000E+00 0.1641489     200001\n" +
                        "   2.854917    -0.9436896E-15 0.0000000E+00 0.0000000E+00 0.6859256     110001\n" +
                        "   4.675529     0.2581231     0.0000000E+00 0.0000000E+00  1.875163     020001\n" +
                        " -0.4267768     0.1249001E-15 0.0000000E+00 0.0000000E+00-0.2581231     100200\n" +
                        " -0.4324620     -1.478207     0.0000000E+00 0.0000000E+00 -1.368345     010200\n" +
                        "  0.0000000E+00 0.0000000E+00-0.5162462     0.0000000E+00 0.0000000E+00 100101\n" +
                        "  0.0000000E+00 0.0000000E+00 -2.736690     0.0000000E+00 0.0000000E+00 010101\n" +
                        " -0.3495927     0.8285107E-15 0.0000000E+00 0.0000000E+00-0.2195423     100002\n" +
                        " -0.9938741     -1.210868     0.0000000E+00 0.0000000E+00 -1.329734     010002\n" +
                        "  0.0000000E+00 0.0000000E+00  1.546455     0.0000000E+00 0.0000000E+00 000300\n" +
                        "  0.3082103     0.2581231     0.0000000E+00 0.0000000E+00  1.266773     000201\n" +
                        "  0.0000000E+00 0.0000000E+00  1.266773    -0.7216450E-15 0.0000000E+00 000102\n" +
                        "  0.1158459     0.5024995E-01 0.0000000E+00 0.0000000E+00 0.4442187     000003\n" +
                        " -0.2158678     0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00 400000\n" +
                        "  -1.804085     0.3885781E-15 0.0000000E+00 0.0000000E+00 0.2220446E-15 310000\n" +
                        "  -5.654020     0.1443290E-14 0.0000000E+00 0.0000000E+00-0.1468813     220000\n" +
                        "  -9.241132     0.2109424E-14 0.0000000E+00 0.0000000E+00-0.6137698     130000\n" +
                        "  -7.011147    -0.1154850     0.0000000E+00 0.0000000E+00 -1.192488     040000\n" +
                        "  0.0000000E+00 0.0000000E+00 0.2336536     0.0000000E+00 0.0000000E+00 300100\n" +
                        "  0.0000000E+00 0.0000000E+00  1.464545     0.0000000E+00 0.0000000E+00 210100\n" +
                        "  0.0000000E+00 0.0000000E+00  3.059933     0.0000000E+00 0.0000000E+00 120100\n" +
                        "  0.0000000E+00 0.0000000E+00  3.364736     0.0000000E+00 0.0000000E+00 030100\n" +
                        "  0.3618683     0.2359224E-15 0.0000000E+00 0.0000000E+00 0.1913965     300001\n" +
                        "   2.268198    -0.2652701E-15 0.0000000E+00 0.0000000E+00  1.724953     210001\n" +
                        "   5.215986    -0.2081668E-14 0.0000000E+00 0.0000000E+00  4.824607     120001\n" +
                        "   4.671864      2.477982     0.0000000E+00 0.0000000E+00  5.652606     030001\n" +
                        " -0.2845179     0.0000000E+00 0.0000000E+00 0.0000000E+00-0.1468813     200200\n" +
                        "  -2.554595     0.0000000E+00 0.0000000E+00 0.0000000E+00-0.6137698     110200\n" +
                        "  -4.183688    -0.2309699     0.0000000E+00 0.0000000E+00 -1.840585     020200\n" +
                        "  0.0000000E+00 0.0000000E+00-0.2937625    -0.1436307E-15 0.0000000E+00 200101\n" +
                        "  0.0000000E+00 0.0000000E+00 -1.227540    -0.5412563E-15 0.0000000E+00 110101\n" +
                        "  0.0000000E+00 0.0000000E+00 -3.681170    -0.3442872E-15 0.0000000E+00 020101\n" +
                        " -0.4605425     0.2081668E-15 0.0000000E+00 0.0000000E+00-0.2692285     200002\n" +
                        "  -3.895989     0.1165734E-14 0.0000000E+00 0.0000000E+00 -1.565291     110002\n" +
                        "  -7.325281    -0.1891982     0.0000000E+00 0.0000000E+00 -3.509969     020002\n" +
                        "  0.0000000E+00 0.0000000E+00 0.4619398     0.0000000E+00 0.0000000E+00 100300\n" +
                        "  0.0000000E+00 0.0000000E+00  2.448805     0.0000000E+00 0.0000000E+00 010300\n" +
                        "  0.7154240    -0.8900333E-15 0.0000000E+00 0.0000000E+00 0.5015080     100201\n" +
                        "   1.869633      2.477982     0.0000000E+00 0.0000000E+00  2.955476     010201\n" +
                        "  0.0000000E+00 0.0000000E+00 0.5015080     0.0000000E+00 0.0000000E+00 100102\n" +
                        "  0.0000000E+00 0.0000000E+00  2.955476     0.1564182E-15 0.0000000E+00 010102\n" +
                        "  0.3517514    -0.6589715E-15 0.0000000E+00 0.0000000E+00 0.2546346     100003\n" +
                        "   1.362484     0.9982096     0.0000000E+00 0.0000000E+00  1.526958     010003\n" +
                        " -0.1378941    -0.1154850     0.0000000E+00 0.0000000E+00-0.6480970     000400\n" +
                        "  0.0000000E+00 0.0000000E+00 -2.592388     0.5551115E-15 0.0000000E+00 000301\n" +
                        " -0.3758229    -0.1891982     0.0000000E+00 0.0000000E+00 -1.592241     000202\n" +
                        "  0.0000000E+00 0.0000000E+00 -1.061494     0.2775558E-15 0.0000000E+00 000103\n" +
                        " -0.1058013    -0.3245537E-01 0.0000000E+00 0.0000000E+00-0.4045457     000004\n" +
                        "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00-0.8915273E-01 500000\n" +
                        "  0.9992007E-15 0.2356791E-15 0.0000000E+00 0.0000000E+00-0.9313519     410000\n" +
                        " -0.3238016     0.7910339E-15 0.0000000E+00 0.0000000E+00 -3.891822     320000\n" +
                        "  -2.029595     0.2553513E-14 0.0000000E+00 0.0000000E+00 -8.601359     230000\n" +
                        "  -4.347209     0.5939693E-14 0.0000000E+00 0.0000000E+00 -10.51371     140000\n" +
                        "  -3.343930     -1.108656     0.0000000E+00 0.0000000E+00 -6.212540     050000\n" +
                        "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.1393624E-15 0.0000000E+00 400100\n" +
                        "  0.0000000E+00 0.0000000E+00-0.5702940E-15 0.1940668E-14 0.0000000E+00 310100\n" +
                        "  0.0000000E+00 0.0000000E+00 0.2628603     0.5778767E-14 0.0000000E+00 220100\n" +
                        "  0.0000000E+00 0.0000000E+00  1.098409     0.7211249E-14 0.0000000E+00 130100\n" +
                        "  0.0000000E+00 0.0000000E+00  2.134089     0.2407503E-14 0.0000000E+00 040100\n" +
                        "  0.3618683     0.0000000E+00 0.0000000E+00 0.0000000E+00 0.1868131     400001\n" +
                        "   4.182242    -0.1050769E-14 0.0000000E+00 0.0000000E+00  1.561265     310001\n" +
                        "   17.14341    -0.2568051E-14 0.0000000E+00 0.0000000E+00  5.190415     220001\n" +
                        "   33.12049    -0.4191092E-14 0.0000000E+00 0.0000000E+00  8.846089     130001\n" +
                        "   27.56239     0.1935923     0.0000000E+00 0.0000000E+00  7.924738     040001\n" +
                        " -0.3238016     0.0000000E+00 0.0000000E+00 0.0000000E+00-0.1958417     300200\n" +
                        "  -2.029595     0.1538842E-14 0.0000000E+00 0.0000000E+00 -1.697560     210200\n" +
                        "  -4.667291     0.5204170E-14 0.0000000E+00 0.0000000E+00 -4.638972     120200\n" +
                        "  -4.180409     -2.217311     0.0000000E+00 0.0000000E+00 -5.411934     030200\n" +
                        "  0.0000000E+00 0.0000000E+00-0.3916834    -0.1107655E-15 0.0000000E+00 300101\n" +
                        "  0.0000000E+00 0.0000000E+00 -3.395119    -0.1032076E-14 0.0000000E+00 210101\n" +
                        "  0.0000000E+00 0.0000000E+00 -9.277944    -0.2463395E-14 0.0000000E+00 120101\n" +
                        "  0.0000000E+00 0.0000000E+00 -10.82387    -0.1300245E-14 0.0000000E+00 030101\n" +
                        " -0.6696511     0.5454118E-15 0.0000000E+00 0.0000000E+00-0.3932551     300002\n" +
                        "  -5.653263     0.3271991E-14 0.0000000E+00 0.0000000E+00 -3.619977     210002\n" +
                        "  -15.50687     0.7827072E-14 0.0000000E+00 0.0000000E+00 -10.95179     120002\n" +
                        "  -15.27205     -3.200951     0.0000000E+00 0.0000000E+00 -12.92478     030002\n" +
                        "  0.0000000E+00 0.0000000E+00 0.2628603     0.0000000E+00 0.0000000E+00 200300\n" +
                        "  0.0000000E+00 0.0000000E+00  1.098409    -0.1795366E-15 0.0000000E+00 110300\n" +
                        "  0.0000000E+00 0.0000000E+00  3.293930    -0.5812890E-15 0.0000000E+00 020300\n" +
                        "  0.8840512     0.1387779E-15 0.0000000E+00 0.0000000E+00 0.5436188     200201\n" +
                        "   7.509762    -0.5273559E-15 0.0000000E+00 0.0000000E+00  3.059524     110201\n" +
                        "   13.98960     0.3871847     0.0000000E+00 0.0000000E+00  7.055956     020201\n" +
                        "  0.0000000E+00 0.0000000E+00 0.5436188     0.6546190E-15 0.0000000E+00 200102\n" +
                        "  0.0000000E+00 0.0000000E+00  3.059524     0.2209974E-14 0.0000000E+00 110102\n" +
                        "  0.0000000E+00 0.0000000E+00  7.055956     0.2801850E-14 0.0000000E+00 020102\n" +
                        "  0.6527182    -0.4996004E-15 0.0000000E+00 0.0000000E+00 0.4210759     200003\n" +
                        "   5.510075    -0.3941292E-14 0.0000000E+00 0.0000000E+00  2.804361     110003\n" +
                        "   10.96516     0.1559703     0.0000000E+00 0.0000000E+00  5.962292     020003\n" +
                        " -0.3200826     0.0000000E+00 0.0000000E+00 0.0000000E+00-0.2486728     100400\n" +
                        " -0.8364786     -1.108656     0.0000000E+00 0.0000000E+00 -1.451088     010400\n" +
                        "  0.0000000E+00 0.0000000E+00-0.9946912    -0.3572433E-15 0.0000000E+00 100301\n" +
                        "  0.0000000E+00 0.0000000E+00 -5.804354    -0.7292685E-15 0.0000000E+00 010301\n" +
                        "  -1.094765     0.0000000E+00 0.0000000E+00 0.0000000E+00-0.8418132     100202\n" +
                        "  -4.050824     -3.200951     0.0000000E+00 0.0000000E+00 -5.031692     010202\n" +
                        "  0.0000000E+00 0.0000000E+00-0.5612088     0.0000000E+00 0.0000000E+00 100103\n" +
                        "  0.0000000E+00 0.0000000E+00 -3.354462     0.0000000E+00 0.0000000E+00 010103\n" +
                        " -0.3907477     0.7581943E-15 0.0000000E+00 0.0000000E+00-0.3042001     100004\n" +
                        "  -1.748189    -0.8693520     0.0000000E+00 0.0000000E+00 -1.796408     010004\n" +
                        "  0.0000000E+00 0.0000000E+00  1.159841     0.0000000E+00 0.0000000E+00 000500\n" +
                        "  0.3652997     0.1935923     0.0000000E+00 0.0000000E+00  1.697451     000401\n" +
                        "  0.0000000E+00 0.0000000E+00  3.394903     0.2220446E-15 0.0000000E+00 000302\n" +
                        "  0.4577553     0.1559703     0.0000000E+00 0.0000000E+00  1.894613     000203\n" +
                        "  0.0000000E+00 0.0000000E+00 0.9473066    -0.4440892E-15 0.0000000E+00 000104\n" +
                        "  0.1038685     0.2302663E-01 0.0000000E+00 0.0000000E+00 0.3789696     000005"
        )),
        EXIT_QS3(CosyArbitraryOrder.readMap(
                " -0.1810734     -1.194847     0.0000000E+00 0.0000000E+00-0.5162462     100000\n" +
                        "  0.3831640     -2.994243     0.0000000E+00 0.0000000E+00 -1.497700     010000\n" +
                        "  0.0000000E+00 0.0000000E+00  1.063276     0.6393535     0.0000000E+00 001000\n" +
                        "  0.0000000E+00 0.0000000E+00  3.970483      3.327961     0.0000000E+00 000100\n" +
                        "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00  1.000000     000010\n" +
                        "  0.4690004     0.2437552     0.0000000E+00 0.0000000E+00 0.6689684     000001\n" +
                        " -0.5350104     0.3204778     0.0000000E+00 0.0000000E+00-0.1936444     200000\n" +
                        "  -2.226149      1.647970     0.0000000E+00 0.0000000E+00-0.8434742     110000\n" +
                        "  -2.598072    -0.4815985     0.0000000E+00 0.0000000E+00 -1.785804     020000\n" +
                        "  0.0000000E+00 0.0000000E+00-0.1265575E-01-0.4134817     0.0000000E+00 101000\n" +
                        "  0.0000000E+00 0.0000000E+00 0.5244255      4.700248     0.0000000E+00 011000\n" +
                        "  0.3394375      3.431498     0.0000000E+00 0.0000000E+00-0.7488912E-02 002000\n" +
                        "  0.0000000E+00 0.0000000E+00 0.9348267    -0.9422945     0.0000000E+00 100100\n" +
                        "  0.0000000E+00 0.0000000E+00  4.738002      18.75923     0.0000000E+00 010100\n" +
                        "   2.450305      25.00282     0.0000000E+00 0.0000000E+00-0.8952992E-01 001100\n" +
                        "  0.7544544    -0.2610147     0.0000000E+00 0.0000000E+00 0.3244575     100001\n" +
                        "   1.621524     -1.223452     0.0000000E+00 0.0000000E+00  1.525759     010001\n" +
                        "  0.0000000E+00 0.0000000E+00 0.2539613      3.038361     0.0000000E+00 001001\n" +
                        "   4.003089      45.33684     0.0000000E+00 0.0000000E+00 -1.217809     000200\n" +
                        "  0.0000000E+00 0.0000000E+00 -1.188372      9.836388     0.0000000E+00 000101\n" +
                        " -0.3467174    -0.5461123     0.0000000E+00 0.0000000E+00-0.6073387     000002\n" +
                        " -0.4138563    -0.1049582E-01 0.0000000E+00 0.0000000E+00-0.1073587     300000\n" +
                        "  -2.404474      2.872573     0.0000000E+00 0.0000000E+00-0.6602586     210000\n" +
                        "  -5.060671      12.33066     0.0000000E+00 0.0000000E+00 -1.641734     120000\n" +
                        "  -4.049343      12.18748     0.0000000E+00 0.0000000E+00 -2.269138     030000\n" +
                        "  0.0000000E+00 0.0000000E+00-0.3386501     -3.891309     0.0000000E+00 201000\n" +
                        "  0.0000000E+00 0.0000000E+00 -1.394327     -16.40516     0.0000000E+00 111000\n" +
                        "  0.0000000E+00 0.0000000E+00 -1.412107     -16.98534     0.0000000E+00 021000\n" +
                        " -0.1192027E-01 0.9630986E-01 0.0000000E+00 0.0000000E+00 0.2360605     102000\n" +
                        "  0.2882778E-02 0.8813518     0.0000000E+00 0.0000000E+00 0.4264362     012000\n" +
                        "  0.0000000E+00 0.0000000E+00 0.4529186E-01 0.7909767     0.0000000E+00 003000\n" +
                        "  0.0000000E+00 0.0000000E+00-0.8712743     -14.36962     0.0000000E+00 200100\n" +
                        "  0.0000000E+00 0.0000000E+00 -3.004117     -55.72439     0.0000000E+00 110100\n" +
                        "  0.0000000E+00 0.0000000E+00-0.3282923     -47.51212     0.0000000E+00 020100\n" +
                        "  0.4642776      7.046081     0.0000000E+00 0.0000000E+00  1.697787     101100\n" +
                        "   1.661691      24.88079     0.0000000E+00 0.0000000E+00  2.745420     011100\n" +
                        "  0.0000000E+00 0.0000000E+00 0.5310090      8.607018     0.0000000E+00 002100\n" +
                        "  0.9015324      1.452259     0.0000000E+00 0.0000000E+00 0.4159280     200001\n" +
                        "   4.887064      1.433827     0.0000000E+00 0.0000000E+00  2.146088     110001\n" +
                        "   6.737802     -3.072673     0.0000000E+00 0.0000000E+00  3.932261     020001\n" +
                        "  0.0000000E+00 0.0000000E+00 0.4541573      4.893228     0.0000000E+00 101001\n" +
                        "  0.0000000E+00 0.0000000E+00 0.6746719      11.25122     0.0000000E+00 011001\n" +
                        " -0.1724043     0.2680921     0.0000000E+00 0.0000000E+00-0.1223185     002001\n" +
                        "   1.155999      24.62451     0.0000000E+00 0.0000000E+00  2.633897     100200\n" +
                        "   4.508978      80.03495     0.0000000E+00 0.0000000E+00  2.615342     010200\n" +
                        "  0.0000000E+00 0.0000000E+00  1.900382      28.51280     0.0000000E+00 001200\n" +
                        "  0.0000000E+00 0.0000000E+00  1.080030      20.97582     0.0000000E+00 100101\n" +
                        "  0.0000000E+00 0.0000000E+00 -1.374235      38.03003     0.0000000E+00 010101\n" +
                        "  -2.518725     -11.26261     0.0000000E+00 0.0000000E+00-0.9645796     001101\n" +
                        " -0.9017624     -1.746888     0.0000000E+00 0.0000000E+00-0.4817455     100002\n" +
                        "  -2.823261     -2.781866     0.0000000E+00 0.0000000E+00 -2.053550     010002\n" +
                        "  0.0000000E+00 0.0000000E+00-0.3080908     -1.801675     0.0000000E+00 001002\n" +
                        "  0.0000000E+00 0.0000000E+00  3.954640      28.98877     0.0000000E+00 000300\n" +
                        "  -6.137595     -43.58009     0.0000000E+00 0.0000000E+00-0.3162237     000201\n" +
                        "  0.0000000E+00 0.0000000E+00-0.2765323E-02 -11.51040     0.0000000E+00 000102\n" +
                        "  0.3581672     0.7300381     0.0000000E+00 0.0000000E+00 0.5680891     000003\n" +
                        " -0.2379837     -1.059561     0.0000000E+00 0.0000000E+00-0.2132726     400000\n" +
                        "  -1.765739     -7.092359     0.0000000E+00 0.0000000E+00 -1.653317     310000\n" +
                        "  -5.780250     -17.21822     0.0000000E+00 0.0000000E+00 -4.847507     220000\n" +
                        "  -11.10721     -14.66478     0.0000000E+00 0.0000000E+00 -6.822229     130000\n" +
                        "  -9.677859     0.2691242     0.0000000E+00 0.0000000E+00 -4.862485     040000\n" +
                        "  0.0000000E+00 0.0000000E+00-0.2301634     -2.215751     0.0000000E+00 301000\n" +
                        "  0.0000000E+00 0.0000000E+00 -1.091426     -14.63274     0.0000000E+00 211000\n" +
                        "  0.0000000E+00 0.0000000E+00 -1.469770     -35.02089     0.0000000E+00 121000\n" +
                        "  0.0000000E+00 0.0000000E+00-0.3478497     -30.15253     0.0000000E+00 031000\n" +
                        "  0.7456668    -0.5280708     0.0000000E+00 0.0000000E+00 0.3369290E-01 202000\n" +
                        "   3.254920     -2.169010     0.0000000E+00 0.0000000E+00 0.1769839     112000\n" +
                        "   3.506469     -1.873913     0.0000000E+00 0.0000000E+00 0.1277527E-01 022000\n" +
                        "  0.0000000E+00 0.0000000E+00-0.1843021    -0.6078426E-01 0.0000000E+00 103000\n" +
                        "  0.0000000E+00 0.0000000E+00-0.2605605     0.2224823     0.0000000E+00 013000\n" +
                        "  0.3370817E-01-0.1814123E-01 0.0000000E+00 0.0000000E+00-0.2279816     004000\n" +
                        "  0.0000000E+00 0.0000000E+00-0.9441992     -11.67497     0.0000000E+00 300100\n" +
                        "  0.0000000E+00 0.0000000E+00 -4.772115     -77.37824     0.0000000E+00 210100\n" +
                        "  0.0000000E+00 0.0000000E+00 -6.743018     -181.1497     0.0000000E+00 120100\n" +
                        "  0.0000000E+00 0.0000000E+00 0.6152707     -139.9280     0.0000000E+00 030100\n" +
                        "   5.556467     -1.841098     0.0000000E+00 0.0000000E+00 0.8127477     201100\n" +
                        "   23.62091     -5.605520     0.0000000E+00 0.0000000E+00  3.853949     111100\n" +
                        "   25.60416      10.58475     0.0000000E+00 0.0000000E+00  2.847008     021100\n" +
                        "  0.0000000E+00 0.0000000E+00 -2.328013      1.254860     0.0000000E+00 102100\n" +
                        "  0.0000000E+00 0.0000000E+00 -2.819988      8.288459     0.0000000E+00 012100\n" +
                        "  0.6343475    -0.2068433     0.0000000E+00 0.0000000E+00 -3.331660     003100\n" +
                        "   1.245908      3.353966     0.0000000E+00 0.0000000E+00 0.5726379     300001\n" +
                        "   8.534178      16.73562     0.0000000E+00 0.0000000E+00  3.810055     210001\n" +
                        "   19.81230      19.98958     0.0000000E+00 0.0000000E+00  8.940730     120001\n" +
                        "   17.23834    -0.6690772     0.0000000E+00 0.0000000E+00  9.036494     030001\n" +
                        "  0.0000000E+00 0.0000000E+00 0.8302936      4.570652     0.0000000E+00 201001\n" +
                        "  0.0000000E+00 0.0000000E+00  4.404710      30.58853     0.0000000E+00 111001\n" +
                        "  0.0000000E+00 0.0000000E+00  5.653578      45.67229     0.0000000E+00 021001\n" +
                        " -0.5258828     0.5325844     0.0000000E+00 0.0000000E+00-0.3760932     102001\n" +
                        "  -1.206258     0.8751040     0.0000000E+00 0.0000000E+00 -1.326012     012001\n" +
                        "  0.0000000E+00 0.0000000E+00 0.8642464E-01-0.2587610     0.0000000E+00 003001\n" +
                        "   9.758283      1.953639     0.0000000E+00 0.0000000E+00  2.077435     200200\n" +
                        "   39.81436      24.22038     0.0000000E+00 0.0000000E+00  9.199458     110200\n" +
                        "   42.69163      91.07304     0.0000000E+00 0.0000000E+00  5.322351     020200\n" +
                        "  0.0000000E+00 0.0000000E+00 -9.950481      7.108364     0.0000000E+00 101200\n" +
                        "  0.0000000E+00 0.0000000E+00 -10.02474      41.46529     0.0000000E+00 011200\n" +
                        "   4.399249    -0.9099184     0.0000000E+00 0.0000000E+00 -18.17183     002200\n" +
                        "  0.0000000E+00 0.0000000E+00  3.277685      29.39854     0.0000000E+00 200101\n" +
                        "  0.0000000E+00 0.0000000E+00  15.96277      165.6926     0.0000000E+00 110101\n" +
                        "  0.0000000E+00 0.0000000E+00  15.80194      218.2139     0.0000000E+00 020101\n" +
                        "  -4.787509    -0.8497153     0.0000000E+00 0.0000000E+00 -4.061741     101101\n" +
                        "  -12.15129     -17.29033     0.0000000E+00 0.0000000E+00 -11.85315     011101\n" +
                        "  0.0000000E+00 0.0000000E+00  1.105915     -7.215427     0.0000000E+00 002101\n" +
                        "  -1.851558     -4.465133     0.0000000E+00 0.0000000E+00-0.8891399     200002\n" +
                        "  -10.48390     -17.47304     0.0000000E+00 0.0000000E+00 -4.936872     110002\n" +
                        "  -15.00397     -12.57679     0.0000000E+00 0.0000000E+00 -8.333966     020002\n" +
                        "  0.0000000E+00 0.0000000E+00-0.7514460     -4.276595     0.0000000E+00 101002\n" +
                        "  0.0000000E+00 0.0000000E+00 -2.019535     -16.58619     0.0000000E+00 011002\n" +
                        "  0.2033387    -0.2845564     0.0000000E+00 0.0000000E+00 0.1330396     002002\n" +
                        "  0.0000000E+00 0.0000000E+00 -13.69556      3.497250     0.0000000E+00 100300\n" +
                        "  0.0000000E+00 0.0000000E+00 -8.122706      54.48628     0.0000000E+00 010300\n" +
                        "   14.44584      9.922103     0.0000000E+00 0.0000000E+00 -43.78197     001300\n" +
                        "  -9.706398     -19.32444     0.0000000E+00 0.0000000E+00 -8.803628     100201\n" +
                        "  -25.86855     -105.4353     0.0000000E+00 0.0000000E+00 -20.69609     010201\n" +
                        "  0.0000000E+00 0.0000000E+00  5.241678     -38.23508     0.0000000E+00 001201\n" +
                        "  0.0000000E+00 0.0000000E+00 -2.989099     -29.00217     0.0000000E+00 100102\n" +
                        "  0.0000000E+00 0.0000000E+00 -4.840367     -87.28017     0.0000000E+00 010102\n" +
                        "   3.136268      6.808045     0.0000000E+00 0.0000000E+00  1.670584     001102\n" +
                        "   1.312312      3.075452     0.0000000E+00 0.0000000E+00 0.7202863     100003\n" +
                        "   4.521797      7.705461     0.0000000E+00 0.0000000E+00  2.902193     010003\n" +
                        "  0.0000000E+00 0.0000000E+00 0.3490698      1.472789     0.0000000E+00 001003\n" +
                        "   18.62001      40.97290     0.0000000E+00 0.0000000E+00 -40.08403     000400\n" +
                        "  0.0000000E+00 0.0000000E+00  6.071549     -47.45076     0.0000000E+00 000301\n" +
                        "   8.757532      39.15002     0.0000000E+00 0.0000000E+00  2.430360     000202\n" +
                        "  0.0000000E+00 0.0000000E+00 0.8043342      12.49591     0.0000000E+00 000103\n" +
                        " -0.4151444    -0.9388177     0.0000000E+00 0.0000000E+00-0.5686698     000004\n" +
                        " -0.5633828     -1.068229     0.0000000E+00 0.0000000E+00-0.1053009     500000\n" +
                        "  -5.300939     -9.818976     0.0000000E+00 0.0000000E+00-0.9908559     410000\n" +
                        "  -19.67818     -37.74483     0.0000000E+00 0.0000000E+00 -4.295421     320000\n" +
                        "  -36.88932     -74.03300     0.0000000E+00 0.0000000E+00 -10.84124     230000\n" +
                        "  -36.82051     -64.35241     0.0000000E+00 0.0000000E+00 -15.26231     140000\n" +
                        "  -17.70254     -11.25763     0.0000000E+00 0.0000000E+00 -9.878383     050000\n" +
                        "  0.0000000E+00 0.0000000E+00-0.3615709     -1.125405     0.0000000E+00 401000\n" +
                        "  0.0000000E+00 0.0000000E+00 -3.450952     -9.988568     0.0000000E+00 311000\n" +
                        "  0.0000000E+00 0.0000000E+00 -12.63418     -35.98743     0.0000000E+00 221000\n" +
                        "  0.0000000E+00 0.0000000E+00 -20.42779     -70.43276     0.0000000E+00 131000\n" +
                        "  0.0000000E+00 0.0000000E+00 -11.64683     -60.40706     0.0000000E+00 041000\n" +
                        " -0.1128488     0.1322029E-01 0.0000000E+00 0.0000000E+00 0.5253824     302000\n" +
                        " -0.6635059    -0.6397545     0.0000000E+00 0.0000000E+00  3.855175     212000\n" +
                        " -0.1349322     -2.912579     0.0000000E+00 0.0000000E+00  9.192060     122000\n" +
                        "   1.421805     -2.870201     0.0000000E+00 0.0000000E+00  7.382703     032000\n" +
                        "  0.0000000E+00 0.0000000E+00 0.9932289E-01  1.617072     0.0000000E+00 203000\n" +
                        "  0.0000000E+00 0.0000000E+00 -1.198435      6.499554     0.0000000E+00 113000\n" +
                        "  0.0000000E+00 0.0000000E+00 -2.240079      6.677898     0.0000000E+00 023000\n" +
                        "  -1.461948    -0.7782069E-01 0.0000000E+00 0.0000000E+00 0.7026489E-01 104000\n" +
                        "  -2.893879    -0.3435933E-01 0.0000000E+00 0.0000000E+00-0.2811951E-01 014000\n" +
                        "  0.0000000E+00 0.0000000E+00 0.2040315     0.8611704E-01 0.0000000E+00 005000\n" +
                        "  0.0000000E+00 0.0000000E+00 -1.203671     -7.106456     0.0000000E+00 400100\n" +
                        "  0.0000000E+00 0.0000000E+00 -11.56324     -62.73155     0.0000000E+00 310100\n" +
                        "  0.0000000E+00 0.0000000E+00 -42.72732     -226.8590     0.0000000E+00 220100\n" +
                        "  0.0000000E+00 0.0000000E+00 -68.63717     -422.9091     0.0000000E+00 130100\n" +
                        "  0.0000000E+00 0.0000000E+00 -35.54607     -324.8367     0.0000000E+00 040100\n" +
                        "  0.9970119     0.4661860     0.0000000E+00 0.0000000E+00  4.123535     301100\n" +
                        "   8.209182     -2.761558     0.0000000E+00 0.0000000E+00  29.37724     211100\n" +
                        "   29.99159     -13.98669     0.0000000E+00 0.0000000E+00  68.43359     121100\n" +
                        "   34.50947      1.144876     0.0000000E+00 0.0000000E+00  53.24946     031100\n" +
                        "  0.0000000E+00 0.0000000E+00 0.2819291      18.09185     0.0000000E+00 202100\n" +
                        "  0.0000000E+00 0.0000000E+00 -16.70935      71.20690     0.0000000E+00 112100\n" +
                        "  0.0000000E+00 0.0000000E+00 -25.62720      76.51258     0.0000000E+00 022100\n" +
                        "  -21.19355     -2.134568     0.0000000E+00 0.0000000E+00 0.4871437     103100\n" +
                        "  -40.58190     -1.979982     0.0000000E+00 0.0000000E+00 -2.399265     013100\n" +
                        "  0.0000000E+00 0.0000000E+00  4.165777      1.893671     0.0000000E+00 004100\n" +
                        "   1.897848      4.658064     0.0000000E+00 0.0000000E+00 0.8537632     400001\n" +
                        "   15.81173      38.47416     0.0000000E+00 0.0000000E+00  7.603423     310001\n" +
                        "   50.61022      118.5355     0.0000000E+00 0.0000000E+00  25.74967     220001\n" +
                        "   78.89111      160.4465     0.0000000E+00 0.0000000E+00  40.70581     130001\n" +
                        "   53.61717      71.38543     0.0000000E+00 0.0000000E+00  28.00395     040001\n" +
                        "  0.0000000E+00 0.0000000E+00 0.9926800      5.141420     0.0000000E+00 301001\n" +
                        "  0.0000000E+00 0.0000000E+00  7.066764      42.38129     0.0000000E+00 211001\n" +
                        "  0.0000000E+00 0.0000000E+00  17.44953      117.1481     0.0000000E+00 121001\n" +
                        "  0.0000000E+00 0.0000000E+00  15.40107      117.8464     0.0000000E+00 031001\n" +
                        "  -1.050535     0.2657336     0.0000000E+00 0.0000000E+00-0.4229063     202001\n" +
                        "  -6.057356      3.576686     0.0000000E+00 0.0000000E+00 -2.621284     112001\n" +
                        "  -8.955820      6.206957     0.0000000E+00 0.0000000E+00 -3.520802     022001\n" +
                        "  0.0000000E+00 0.0000000E+00-0.5676118     -1.292386     0.0000000E+00 103001\n" +
                        "  0.0000000E+00 0.0000000E+00 0.3163760     -2.727653     0.0000000E+00 013001\n" +
                        "  0.6381571     0.1147834     0.0000000E+00 0.0000000E+00 0.2257176     004001\n" +
                        "   4.103793     0.3006572     0.0000000E+00 0.0000000E+00  7.830552     300200\n" +
                        "   31.13935     -2.920854     0.0000000E+00 0.0000000E+00  54.57374     210200\n" +
                        "   92.55650      14.59804     0.0000000E+00 0.0000000E+00  124.2702     120200\n" +
                        "   90.91027      101.6382     0.0000000E+00 0.0000000E+00  90.97389     030200\n" +
                        "  0.0000000E+00 0.0000000E+00 -3.552923      64.71865     0.0000000E+00 201200\n" +
                        "  0.0000000E+00 0.0000000E+00 -81.11485      241.3686     0.0000000E+00 111200\n" +
                        "  0.0000000E+00 0.0000000E+00 -104.1921      265.0888     0.0000000E+00 021200\n" +
                        "  -114.3977     -17.39507     0.0000000E+00 0.0000000E+00 0.8809041E-01 102200\n" +
                        "  -211.0118     -17.77260     0.0000000E+00 0.0000000E+00 -23.16603     012200\n" +
                        "  0.0000000E+00 0.0000000E+00  33.58195      16.41679     0.0000000E+00 003200\n" +
                        "  0.0000000E+00 0.0000000E+00  4.100509      31.93966     0.0000000E+00 300101\n" +
                        "  0.0000000E+00 0.0000000E+00  29.23358      255.1448     0.0000000E+00 210101\n" +
                        "  0.0000000E+00 0.0000000E+00  71.72358      695.7849     0.0000000E+00 120101\n" +
                        "  0.0000000E+00 0.0000000E+00  57.09504      663.4183     0.0000000E+00 030101\n" +
                        "  -12.40539     0.3419597     0.0000000E+00 0.0000000E+00 -5.022682     201101\n" +
                        "  -65.10311      14.59411     0.0000000E+00 0.0000000E+00 -29.87846     111101\n" +
                        "  -90.58400      7.680077     0.0000000E+00 0.0000000E+00 -39.76461     021101\n" +
                        "  0.0000000E+00 0.0000000E+00 -3.912169     -16.96729     0.0000000E+00 102101\n" +
                        "  0.0000000E+00 0.0000000E+00  9.829534     -40.36115     0.0000000E+00 012101\n" +
                        "   9.452405      2.449136     0.0000000E+00 0.0000000E+00  4.850343     003101\n" +
                        "  -3.508277     -8.993928     0.0000000E+00 0.0000000E+00 -1.684055     300002\n" +
                        "  -26.12149     -60.72172     0.0000000E+00 0.0000000E+00 -12.31899     210002\n" +
                        "  -65.35143     -124.5086     0.0000000E+00 0.0000000E+00 -30.87056     120002\n" +
                        "  -58.61140     -75.99272     0.0000000E+00 0.0000000E+00 -29.32693     030002\n" +
                        "  0.0000000E+00 0.0000000E+00 -1.561646     -6.868943     0.0000000E+00 201002\n" +
                        "  0.0000000E+00 0.0000000E+00 -8.898177     -49.97025     0.0000000E+00 111002\n" +
                        "  0.0000000E+00 0.0000000E+00 -12.74201     -85.28593     0.0000000E+00 021002\n" +
                        "   1.027865    -0.2998490     0.0000000E+00 0.0000000E+00 0.4183740     102002\n" +
                        "   2.638613     -1.520938     0.0000000E+00 0.0000000E+00  2.162181     012002\n" +
                        "  0.0000000E+00 0.0000000E+00 0.3499980     0.4155091     0.0000000E+00 003002\n" +
                        "  0.0000000E+00 0.0000000E+00 -12.04183      62.41295     0.0000000E+00 200300\n" +
                        "  0.0000000E+00 0.0000000E+00 -130.7803      200.0092     0.0000000E+00 110300\n" +
                        "  0.0000000E+00 0.0000000E+00 -141.5249      227.0733     0.0000000E+00 020300\n" +
                        "  -271.5721     -52.30608     0.0000000E+00 0.0000000E+00 -4.025377     101300\n" +
                        "  -478.3456     -35.36920     0.0000000E+00 0.0000000E+00 -76.90682     011300\n" +
                        "  0.0000000E+00 0.0000000E+00  133.8414      74.19298     0.0000000E+00 002300\n" +
                        "  -28.69949     -2.196339     0.0000000E+00 0.0000000E+00 -12.26362     200201\n" +
                        "  -141.3854     -14.50392     0.0000000E+00 0.0000000E+00 -69.91974     110201\n" +
                        "  -190.1383     -135.9997     0.0000000E+00 0.0000000E+00 -86.40742     020201\n" +
                        "  0.0000000E+00 0.0000000E+00 -2.200598     -70.43539     0.0000000E+00 101201\n" +
                        "  0.0000000E+00 0.0000000E+00  64.40464     -174.1222     0.0000000E+00 011201\n" +
                        "   51.27163      18.27388     0.0000000E+00 0.0000000E+00  34.66441     002201\n" +
                        "  0.0000000E+00 0.0000000E+00 -7.159590     -49.45342     0.0000000E+00 200102\n" +
                        "  0.0000000E+00 0.0000000E+00 -38.99236     -321.8846     0.0000000E+00 110102\n" +
                        "  0.0000000E+00 0.0000000E+00 -49.44197     -502.3880     0.0000000E+00 020102\n" +
                        "   11.25622      1.244322     0.0000000E+00 0.0000000E+00  6.218663     101102\n" +
                        "   29.57723      12.49633     0.0000000E+00 0.0000000E+00  24.54928     011102\n" +
                        "  0.0000000E+00 0.0000000E+00  3.298554      9.862945     0.0000000E+00 002102\n" +
                        "   3.578573      8.987530     0.0000000E+00 0.0000000E+00  1.814295     200003\n" +
                        "   21.03722      45.31360     0.0000000E+00 0.0000000E+00  10.37964     110003\n" +
                        "   31.13720      49.84500     0.0000000E+00 0.0000000E+00  16.82188     020003\n" +
                        "  0.0000000E+00 0.0000000E+00  1.196470      4.716501     0.0000000E+00 101003\n" +
                        "  0.0000000E+00 0.0000000E+00  3.764638      21.53136     0.0000000E+00 011003\n" +
                        " -0.3379092     0.1931002     0.0000000E+00 0.0000000E+00-0.9910545E-01 002003\n" +
                        "  -238.1729     -39.61576     0.0000000E+00 0.0000000E+00 -5.136503     100400\n" +
                        "  -394.2675      47.05037     0.0000000E+00 0.0000000E+00 -86.35790     010400\n" +
                        "  0.0000000E+00 0.0000000E+00  263.7771      175.4154     0.0000000E+00 001400\n" +
                        "  0.0000000E+00 0.0000000E+00  16.75041     -69.07097     0.0000000E+00 100301\n" +
                        "  0.0000000E+00 0.0000000E+00  113.0729     -182.8849     0.0000000E+00 010301\n" +
                        "   117.4531      38.88198     0.0000000E+00 0.0000000E+00  101.9131     001301\n" +
                        "   26.09739      17.57326     0.0000000E+00 0.0000000E+00  16.74047     100202\n" +
                        "   68.08387      113.1140     0.0000000E+00 0.0000000E+00  54.52680     010202\n" +
                        "  0.0000000E+00 0.0000000E+00  7.527602      58.56361     0.0000000E+00 001202\n" +
                        "  0.0000000E+00 0.0000000E+00  5.624558      37.95826     0.0000000E+00 100103\n" +
                        "  0.0000000E+00 0.0000000E+00  14.26558      139.8517     0.0000000E+00 010103\n" +
                        "  -4.771534     -5.371058     0.0000000E+00 0.0000000E+00 -1.998863     001103\n" +
                        "  -2.002743     -4.888509     0.0000000E+00 0.0000000E+00 -1.128287     100004\n" +
                        "  -7.106292     -14.59777     0.0000000E+00 0.0000000E+00 -4.283316     010004\n" +
                        "  0.0000000E+00 0.0000000E+00-0.4206037     -1.372056     0.0000000E+00 001004\n" +
                        "  0.0000000E+00 0.0000000E+00  206.6637      165.9984     0.0000000E+00 000500\n" +
                        "   91.06211     -23.45111     0.0000000E+00 0.0000000E+00  107.9104     000401\n" +
                        "  0.0000000E+00 0.0000000E+00  2.250894      81.28743     0.0000000E+00 000302\n" +
                        "  -13.62927     -36.43852     0.0000000E+00 0.0000000E+00 -4.355218     000203\n" +
                        "  0.0000000E+00 0.0000000E+00 -1.580134     -13.64926     0.0000000E+00 000104\n" +
                        "  0.5174595      1.217642     0.0000000E+00 0.0000000E+00 0.6112002     000005"
        )),
        ENTER_CCT1(CosyArbitraryOrder.readMap(
                " -0.4900311     -1.194847     0.0000000E+00 0.0000000E+00-0.5162462     100000\n" +
                        " -0.3910729     -2.994243     0.0000000E+00 0.0000000E+00 -1.497700     010000\n" +
                        "  0.0000000E+00 0.0000000E+00  1.228597     0.6393535     0.0000000E+00 001000\n" +
                        "  0.0000000E+00 0.0000000E+00  4.831011      3.327961     0.0000000E+00 000100\n" +
                        "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00  1.000000     000010\n" +
                        "  0.5320295     0.2437552     0.0000000E+00 0.0000000E+00 0.7193063     000001\n" +
                        " -0.4521427     0.3204778     0.0000000E+00 0.0000000E+00-0.2967834     200000\n" +
                        "  -1.800025      1.647970     0.0000000E+00 0.0000000E+00 -1.360399     110000\n" +
                        "  -2.722601    -0.4815985     0.0000000E+00 0.0000000E+00 -2.433502     020000\n" +
                        "  0.0000000E+00 0.0000000E+00-0.1195718    -0.4134817     0.0000000E+00 101000\n" +
                        "  0.0000000E+00 0.0000000E+00  1.739793      4.700248     0.0000000E+00 011000\n" +
                        "   1.226738      3.431498     0.0000000E+00 0.0000000E+00-0.3702007E-01 002000\n" +
                        "  0.0000000E+00 0.0000000E+00 0.6911728    -0.9422945     0.0000000E+00 100100\n" +
                        "  0.0000000E+00 0.0000000E+00  9.588674      18.75923     0.0000000E+00 010100\n" +
                        "   8.915414      25.00282     0.0000000E+00 0.0000000E+00-0.3969609     001100\n" +
                        "  0.8596021    -0.2610147     0.0000000E+00 0.0000000E+00 0.3665393     100001\n" +
                        "   1.737799     -1.223452     0.0000000E+00 0.0000000E+00  1.631214     010001\n" +
                        "  0.0000000E+00 0.0000000E+00 0.9472279      3.038361     0.0000000E+00 001001\n" +
                        "   15.72607      45.33684     0.0000000E+00 0.0000000E+00 -2.017928     000200\n" +
                        "  0.0000000E+00 0.0000000E+00 0.8742274      9.836388     0.0000000E+00 000101\n" +
                        " -0.5231479    -0.5461123     0.0000000E+00 0.0000000E+00-0.6538230     000002\n" +
                        " -0.6371135    -0.1049582E-01 0.0000000E+00 0.0000000E+00-0.5203151E-01 300000\n" +
                        "  -3.319718      2.872573     0.0000000E+00 0.0000000E+00-0.2371056     210000\n" +
                        "  -6.027205      12.33066     0.0000000E+00 0.0000000E+00 -1.011918     120000\n" +
                        "  -4.368671      12.18748     0.0000000E+00 0.0000000E+00 -2.477491     030000\n" +
                        "  0.0000000E+00 0.0000000E+00 -1.226835     -3.891309     0.0000000E+00 201000\n" +
                        "  0.0000000E+00 0.0000000E+00 -5.044832     -16.40516     0.0000000E+00 111000\n" +
                        "  0.0000000E+00 0.0000000E+00 -5.063003     -16.98534     0.0000000E+00 021000\n" +
                        " -0.5016371E-01 0.9630986E-01 0.0000000E+00 0.0000000E+00 0.8666698     102000\n" +
                        "  0.7253493E-01 0.8813518     0.0000000E+00 0.0000000E+00  1.476800     012000\n" +
                        "  0.0000000E+00 0.0000000E+00 0.2836082     0.7909767     0.0000000E+00 003000\n" +
                        "  0.0000000E+00 0.0000000E+00 -3.972630     -14.36962     0.0000000E+00 200100\n" +
                        "  0.0000000E+00 0.0000000E+00 -14.33438     -55.72439     0.0000000E+00 110100\n" +
                        "  0.0000000E+00 0.0000000E+00 -8.756217     -47.51212     0.0000000E+00 020100\n" +
                        "   1.628836      7.046081     0.0000000E+00 0.0000000E+00  6.300134     101100\n" +
                        "   6.447866      24.88079     0.0000000E+00 0.0000000E+00  9.569316     011100\n" +
                        "  0.0000000E+00 0.0000000E+00  3.284211      8.607018     0.0000000E+00 002100\n" +
                        "   1.365722      1.452259     0.0000000E+00 0.0000000E+00 0.5107764     200001\n" +
                        "   5.696196      1.433827     0.0000000E+00 0.0000000E+00  2.521697     110001\n" +
                        "   6.860499     -3.072673     0.0000000E+00 0.0000000E+00  4.369416     020001\n" +
                        "  0.0000000E+00 0.0000000E+00  1.731017      4.893228     0.0000000E+00 101001\n" +
                        "  0.0000000E+00 0.0000000E+00  2.784173      11.25122     0.0000000E+00 011001\n" +
                        " -0.5860063     0.2680921     0.0000000E+00 0.0000000E+00-0.4805608     002001\n" +
                        "   5.812382      24.62451     0.0000000E+00 0.0000000E+00  10.91393     100200\n" +
                        "   20.91656      80.03495     0.0000000E+00 0.0000000E+00  13.20905     010200\n" +
                        "  0.0000000E+00 0.0000000E+00  12.01956      28.51280     0.0000000E+00 001200\n" +
                        "  0.0000000E+00 0.0000000E+00  6.389375      20.97582     0.0000000E+00 100101\n" +
                        "  0.0000000E+00 0.0000000E+00  5.120857      38.03003     0.0000000E+00 010101\n" +
                        "  -8.909425     -11.26261     0.0000000E+00 0.0000000E+00 -3.764138     001101\n" +
                        "  -1.469828     -1.746888     0.0000000E+00 0.0000000E+00-0.6285234     100002\n" +
                        "  -3.751920     -2.781866     0.0000000E+00 0.0000000E+00 -2.401318     010002\n" +
                        "  0.0000000E+00 0.0000000E+00 -1.140340     -1.801675     0.0000000E+00 001002\n" +
                        "  0.0000000E+00 0.0000000E+00  16.21573      28.98877     0.0000000E+00 000300\n" +
                        "  -23.60787     -43.58009     0.0000000E+00 0.0000000E+00 -5.469818     000201\n" +
                        "  0.0000000E+00 0.0000000E+00 -4.022284     -11.51040     0.0000000E+00 000102\n" +
                        "  0.6535304     0.7300381     0.0000000E+00 0.0000000E+00 0.6299498     000003\n" +
                        " -0.3344999     -1.059561     0.0000000E+00 0.0000000E+00-0.3329399     400000\n" +
                        "  -1.797690     -7.092359     0.0000000E+00 0.0000000E+00 -2.345236     310000\n" +
                        "  -4.811126     -17.21822     0.0000000E+00 0.0000000E+00 -5.811000     220000\n" +
                        "  -10.50511     -14.66478     0.0000000E+00 0.0000000E+00 -6.220646     130000\n" +
                        "  -11.28297     0.2691242     0.0000000E+00 0.0000000E+00 -3.961783     040000\n" +
                        "  0.0000000E+00 0.0000000E+00-0.9427265     -2.215751     0.0000000E+00 301000\n" +
                        "  0.0000000E+00 0.0000000E+00 -4.874202     -14.63274     0.0000000E+00 211000\n" +
                        "  0.0000000E+00 0.0000000E+00 -7.377036     -35.02089     0.0000000E+00 121000\n" +
                        "  0.0000000E+00 0.0000000E+00 -2.457964     -30.15253     0.0000000E+00 031000\n" +
                        "   2.607878    -0.5280708     0.0000000E+00 0.0000000E+00 0.1753050     202000\n" +
                        "   11.58076     -2.169010     0.0000000E+00 0.0000000E+00  1.033059     112000\n" +
                        "   12.60242     -1.873913     0.0000000E+00 0.0000000E+00 0.2087600     022000\n" +
                        "  0.0000000E+00 0.0000000E+00-0.9434109    -0.6078426E-01 0.0000000E+00 103000\n" +
                        "  0.0000000E+00 0.0000000E+00 -1.156448     0.2224823     0.0000000E+00 013000\n" +
                        "  0.2103694    -0.1814123E-01 0.0000000E+00 0.0000000E+00 -1.160783     004000\n" +
                        "  0.0000000E+00 0.0000000E+00 -4.466498     -11.67497     0.0000000E+00 300100\n" +
                        "  0.0000000E+00 0.0000000E+00 -24.70956     -77.37824     0.0000000E+00 210100\n" +
                        "  0.0000000E+00 0.0000000E+00 -41.07304     -181.1497     0.0000000E+00 120100\n" +
                        "  0.0000000E+00 0.0000000E+00 -12.58141     -139.9280     0.0000000E+00 030100\n" +
                        "   19.71296     -1.841098     0.0000000E+00 0.0000000E+00  3.355336     201100\n" +
                        "   85.46154     -5.605520     0.0000000E+00 0.0000000E+00  16.74137     111100\n" +
                        "   93.62362      10.58475     0.0000000E+00 0.0000000E+00  11.03314     021100\n" +
                        "  0.0000000E+00 0.0000000E+00 -11.30256      1.254860     0.0000000E+00 102100\n" +
                        "  0.0000000E+00 0.0000000E+00 -11.16296      8.288459     0.0000000E+00 012100\n" +
                        "   3.790191    -0.2068433     0.0000000E+00 0.0000000E+00 -17.09216     003100\n" +
                        "   2.267444      3.353966     0.0000000E+00 0.0000000E+00 0.8448219     300001\n" +
                        "   13.27032      16.73562     0.0000000E+00 0.0000000E+00  4.760684     210001\n" +
                        "   25.03728      19.98958     0.0000000E+00 0.0000000E+00  9.643952     120001\n" +
                        "   17.14075    -0.6690772     0.0000000E+00 0.0000000E+00  8.916424     030001\n" +
                        "  0.0000000E+00 0.0000000E+00  3.032996      4.570652     0.0000000E+00 201001\n" +
                        "  0.0000000E+00 0.0000000E+00  16.66510      30.58853     0.0000000E+00 111001\n" +
                        "  0.0000000E+00 0.0000000E+00  21.89614      45.67229     0.0000000E+00 021001\n" +
                        "  -1.702143     0.5325844     0.0000000E+00 0.0000000E+00 -1.372938     102001\n" +
                        "  -4.164143     0.8751040     0.0000000E+00 0.0000000E+00 -5.212388     012001\n" +
                        "  0.0000000E+00 0.0000000E+00 0.4685946    -0.2587610     0.0000000E+00 003001\n" +
                        "   36.79584      1.953639     0.0000000E+00 0.0000000E+00  9.361278     200200\n" +
                        "   157.3992      24.22038     0.0000000E+00 0.0000000E+00  43.63605     110200\n" +
                        "   174.8691      91.07304     0.0000000E+00 0.0000000E+00  29.76526     020200\n" +
                        "  0.0000000E+00 0.0000000E+00 -46.10735      7.108364     0.0000000E+00 101200\n" +
                        "  0.0000000E+00 0.0000000E+00 -35.01426      41.46529     0.0000000E+00 011200\n" +
                        "   25.22965    -0.9099184     0.0000000E+00 0.0000000E+00 -94.05687     002200\n" +
                        "  0.0000000E+00 0.0000000E+00  14.14806      29.39854     0.0000000E+00 200101\n" +
                        "  0.0000000E+00 0.0000000E+00  71.83812      165.6926     0.0000000E+00 110101\n" +
                        "  0.0000000E+00 0.0000000E+00  83.53765      218.2139     0.0000000E+00 020101\n" +
                        "  -15.90755    -0.8497153     0.0000000E+00 0.0000000E+00 -15.07882     101101\n" +
                        "  -43.24054     -17.29033     0.0000000E+00 0.0000000E+00 -46.35126     011101\n" +
                        "  0.0000000E+00 0.0000000E+00  5.413911     -7.215427     0.0000000E+00 002101\n" +
                        "  -3.644333     -4.465133     0.0000000E+00 0.0000000E+00 -1.353441     200002\n" +
                        "  -17.22211     -17.47304     0.0000000E+00 0.0000000E+00 -6.654498     110002\n" +
                        "  -20.50143     -12.57679     0.0000000E+00 0.0000000E+00 -10.17162     020002\n" +
                        "  0.0000000E+00 0.0000000E+00 -2.661987     -4.276595     0.0000000E+00 101002\n" +
                        "  0.0000000E+00 0.0000000E+00 -7.550190     -16.58619     0.0000000E+00 011002\n" +
                        "  0.6055012    -0.2845564     0.0000000E+00 0.0000000E+00 0.4263388     002002\n" +
                        "  0.0000000E+00 0.0000000E+00 -63.45440      3.497250     0.0000000E+00 100300\n" +
                        "  0.0000000E+00 0.0000000E+00 -30.26604      54.48628     0.0000000E+00 010300\n" +
                        "   77.75654      9.922103     0.0000000E+00 0.0000000E+00 -229.0604     001300\n" +
                        "  -36.32121     -19.32444     0.0000000E+00 0.0000000E+00 -35.67072     100201\n" +
                        "  -106.3384     -105.4353     0.0000000E+00 0.0000000E+00 -93.08028     010201\n" +
                        "  0.0000000E+00 0.0000000E+00  22.99011     -38.23508     0.0000000E+00 001201\n" +
                        "  0.0000000E+00 0.0000000E+00 -13.43998     -29.00217     0.0000000E+00 100102\n" +
                        "  0.0000000E+00 0.0000000E+00 -30.42589     -87.28017     0.0000000E+00 010102\n" +
                        "   10.25653      6.808045     0.0000000E+00 0.0000000E+00  5.831557     001102\n" +
                        "   2.600152      3.075452     0.0000000E+00 0.0000000E+00  1.092017     100003\n" +
                        "   7.444699      7.705461     0.0000000E+00 0.0000000E+00  3.703214     010003\n" +
                        "  0.0000000E+00 0.0000000E+00  1.249274      1.472789     0.0000000E+00 001003\n" +
                        "   94.13251      40.97290     0.0000000E+00 0.0000000E+00 -209.1606     000400\n" +
                        "  0.0000000E+00 0.0000000E+00  33.38918     -47.45076     0.0000000E+00 000301\n" +
                        "   31.71992      39.15002     0.0000000E+00 0.0000000E+00  13.84011     000202\n" +
                        "  0.0000000E+00 0.0000000E+00  6.367899      12.49591     0.0000000E+00 000103\n" +
                        " -0.8582221    -0.9388177     0.0000000E+00 0.0000000E+00-0.6843187     000004\n" +
                        "  -1.129156     -1.068229     0.0000000E+00 0.0000000E+00-0.1692547     500000\n" +
                        "  -9.846019     -9.818976     0.0000000E+00 0.0000000E+00 -1.304187     410000\n" +
                        "  -31.84627     -37.74483     0.0000000E+00 0.0000000E+00 -4.957523     320000\n" +
                        "  -44.29596     -74.03300     0.0000000E+00 0.0000000E+00 -12.11829     230000\n" +
                        "  -21.58619     -64.35241     0.0000000E+00 0.0000000E+00 -17.37053     140000\n" +
                        "  -1.839784     -11.25763     0.0000000E+00 0.0000000E+00 -11.71588     050000\n" +
                        "  0.0000000E+00 0.0000000E+00 -1.192962     -1.125405     0.0000000E+00 401000\n" +
                        "  0.0000000E+00 0.0000000E+00 -12.02220     -9.988568     0.0000000E+00 311000\n" +
                        "  0.0000000E+00 0.0000000E+00 -46.75255     -35.98743     0.0000000E+00 221000\n" +
                        "  0.0000000E+00 0.0000000E+00 -79.50894     -70.43276     0.0000000E+00 131000\n" +
                        "  0.0000000E+00 0.0000000E+00 -46.23275     -60.40706     0.0000000E+00 041000\n" +
                        " -0.4908380     0.1322029E-01 0.0000000E+00 0.0000000E+00  1.791538     302000\n" +
                        "  -2.896780    -0.6397545     0.0000000E+00 0.0000000E+00  14.05574     212000\n" +
                        " -0.7250194     -2.912579     0.0000000E+00 0.0000000E+00  35.11526     122000\n" +
                        "   5.580317     -2.870201     0.0000000E+00 0.0000000E+00  28.90907     032000\n" +
                        "  0.0000000E+00 0.0000000E+00 0.7624031      1.617072     0.0000000E+00 203000\n" +
                        "  0.0000000E+00 0.0000000E+00 -5.159803      6.499554     0.0000000E+00 113000\n" +
                        "  0.0000000E+00 0.0000000E+00 -9.553663      6.677898     0.0000000E+00 023000\n" +
                        "  -7.344200    -0.7782069E-01 0.0000000E+00 0.0000000E+00 0.4589162     104000\n" +
                        "  -14.30496    -0.3435933E-01 0.0000000E+00 0.0000000E+00-0.3866309     014000\n" +
                        "  0.0000000E+00 0.0000000E+00  1.335408     0.8611704E-01 0.0000000E+00 005000\n" +
                        "  0.0000000E+00 0.0000000E+00 -4.887539     -7.106456     0.0000000E+00 400100\n" +
                        "  0.0000000E+00 0.0000000E+00 -48.38580     -62.73155     0.0000000E+00 310100\n" +
                        "  0.0000000E+00 0.0000000E+00 -185.7693     -226.8590     0.0000000E+00 220100\n" +
                        "  0.0000000E+00 0.0000000E+00 -311.6140     -422.9091     0.0000000E+00 130100\n" +
                        "  0.0000000E+00 0.0000000E+00 -172.9829     -324.8367     0.0000000E+00 040100\n" +
                        "   2.733964     0.4661860     0.0000000E+00 0.0000000E+00  14.48205     301100\n" +
                        "   24.94697     -2.761558     0.0000000E+00 0.0000000E+00  110.0446     211100\n" +
                        "   106.5657     -13.98669     0.0000000E+00 0.0000000E+00  267.7430     121100\n" +
                        "   129.4206      1.144876     0.0000000E+00 0.0000000E+00  211.7010     031100\n" +
                        "  0.0000000E+00 0.0000000E+00  4.364758      18.09185     0.0000000E+00 202100\n" +
                        "  0.0000000E+00 0.0000000E+00 -75.21285      71.20690     0.0000000E+00 112100\n" +
                        "  0.0000000E+00 0.0000000E+00 -110.9186      76.51258     0.0000000E+00 022100\n" +
                        "  -107.2244     -2.134568     0.0000000E+00 0.0000000E+00  3.992517     103100\n" +
                        "  -201.9348     -1.979982     0.0000000E+00 0.0000000E+00 -15.98170     013100\n" +
                        "  0.0000000E+00 0.0000000E+00  26.84570      1.893671     0.0000000E+00 004100\n" +
                        "   4.092573      4.658064     0.0000000E+00 0.0000000E+00  1.532299     400001\n" +
                        "   31.41634      38.47416     0.0000000E+00 0.0000000E+00  12.22511     310001\n" +
                        "   90.54159      118.5355     0.0000000E+00 0.0000000E+00  36.55116     220001\n" +
                        "   120.7127      160.4465     0.0000000E+00 0.0000000E+00  48.27715     130001\n" +
                        "   65.41171      71.38543     0.0000000E+00 0.0000000E+00  26.75858     040001\n" +
                        "  0.0000000E+00 0.0000000E+00  3.325799      5.141420     0.0000000E+00 301001\n" +
                        "  0.0000000E+00 0.0000000E+00  24.80438      42.38129     0.0000000E+00 211001\n" +
                        "  0.0000000E+00 0.0000000E+00  65.52912      117.1481     0.0000000E+00 121001\n" +
                        "  0.0000000E+00 0.0000000E+00  62.87372      117.8464     0.0000000E+00 031001\n" +
                        "  -3.482836     0.2657336     0.0000000E+00 0.0000000E+00 -1.045127     202001\n" +
                        "  -19.85362      3.576686     0.0000000E+00 0.0000000E+00 -8.905870     112001\n" +
                        "  -30.71803      6.206957     0.0000000E+00 0.0000000E+00 -13.63918     022001\n" +
                        "  0.0000000E+00 0.0000000E+00 -3.091005     -1.292386     0.0000000E+00 103001\n" +
                        "  0.0000000E+00 0.0000000E+00  1.973684     -2.727653     0.0000000E+00 013001\n" +
                        "   3.253367     0.1147834     0.0000000E+00 0.0000000E+00 0.9014546     004001\n" +
                        "   15.04894     0.3006572     0.0000000E+00 0.0000000E+00  29.40457     300200\n" +
                        "   119.8887     -2.920854     0.0000000E+00 0.0000000E+00  218.3682     210200\n" +
                        "   378.1591      14.59804     0.0000000E+00 0.0000000E+00  519.5473     120200\n" +
                        "   384.4266      101.6382     0.0000000E+00 0.0000000E+00  391.1779     030200\n" +
                        "  0.0000000E+00 0.0000000E+00 -4.622358      64.71865     0.0000000E+00 201200\n" +
                        "  0.0000000E+00 0.0000000E+00 -367.4422      241.3686     0.0000000E+00 111200\n" +
                        "  0.0000000E+00 0.0000000E+00 -447.9805      265.0888     0.0000000E+00 021200\n" +
                        "  -583.7917     -17.39507     0.0000000E+00 0.0000000E+00  8.182962     102200\n" +
                        "  -1058.674     -17.77260     0.0000000E+00 0.0000000E+00 -141.9035     012200\n" +
                        "  0.0000000E+00 0.0000000E+00  213.9831      16.41679     0.0000000E+00 003200\n" +
                        "  0.0000000E+00 0.0000000E+00  16.67092      31.93966     0.0000000E+00 300101\n" +
                        "  0.0000000E+00 0.0000000E+00  124.0442      255.1448     0.0000000E+00 210101\n" +
                        "  0.0000000E+00 0.0000000E+00  321.6199      695.7849     0.0000000E+00 120101\n" +
                        "  0.0000000E+00 0.0000000E+00  286.7755      663.4183     0.0000000E+00 030101\n" +
                        "  -42.09561     0.3419597     0.0000000E+00 0.0000000E+00 -15.05204     201101\n" +
                        "  -220.3794      14.59411     0.0000000E+00 0.0000000E+00 -107.8569     111101\n" +
                        "  -318.7659      7.680077     0.0000000E+00 0.0000000E+00 -155.6494     021101\n" +
                        "  0.0000000E+00 0.0000000E+00 -23.63093     -16.96729     0.0000000E+00 102101\n" +
                        "  0.0000000E+00 0.0000000E+00  49.57721     -40.36115     0.0000000E+00 012101\n" +
                        "   48.21722      2.449136     0.0000000E+00 0.0000000E+00  21.18841     003101\n" +
                        "  -7.712623     -8.993928     0.0000000E+00 0.0000000E+00 -3.084584     300002\n" +
                        "  -51.74576     -60.72172     0.0000000E+00 0.0000000E+00 -19.89939     210002\n" +
                        "  -114.9570     -124.5086     0.0000000E+00 0.0000000E+00 -44.67087     120002\n" +
                        "  -89.15218     -75.99272     0.0000000E+00 0.0000000E+00 -37.35297     030002\n" +
                        "  0.0000000E+00 0.0000000E+00 -5.330830     -6.868943     0.0000000E+00 201002\n" +
                        "  0.0000000E+00 0.0000000E+00 -31.05629     -49.97025     0.0000000E+00 111002\n" +
                        "  0.0000000E+00 0.0000000E+00 -45.30034     -85.28593     0.0000000E+00 021002\n" +
                        "   3.457683    -0.2998490     0.0000000E+00 0.0000000E+00 0.9489067     102002\n" +
                        "   8.309341     -1.520938     0.0000000E+00 0.0000000E+00  7.447025     012002\n" +
                        "  0.0000000E+00 0.0000000E+00  1.983273     0.4155091     0.0000000E+00 003002\n" +
                        "  0.0000000E+00 0.0000000E+00 -40.54639      62.41295     0.0000000E+00 200300\n" +
                        "  0.0000000E+00 0.0000000E+00 -597.2437      200.0092     0.0000000E+00 110300\n" +
                        "  0.0000000E+00 0.0000000E+00 -618.4654      227.0733     0.0000000E+00 020300\n" +
                        "  -1401.548     -52.30608     0.0000000E+00 0.0000000E+00 -6.241413     101300\n" +
                        "  -2428.328     -35.36920     0.0000000E+00 0.0000000E+00 -465.6845     011300\n" +
                        "  0.0000000E+00 0.0000000E+00  846.5166      74.19298     0.0000000E+00 002300\n" +
                        "  -104.4504     -2.196339     0.0000000E+00 0.0000000E+00 -43.57482     200201\n" +
                        "  -526.9620     -14.50392     0.0000000E+00 0.0000000E+00 -283.6547     110201\n" +
                        "  -746.5876     -135.9997     0.0000000E+00 0.0000000E+00 -385.0618     020201\n" +
                        "  0.0000000E+00 0.0000000E+00 -35.26436     -70.43539     0.0000000E+00 101201\n" +
                        "  0.0000000E+00 0.0000000E+00  300.4497     -174.1222     0.0000000E+00 011201\n" +
                        "   262.6652      18.27388     0.0000000E+00 0.0000000E+00  159.0783     002201\n" +
                        "  0.0000000E+00 0.0000000E+00 -28.93396     -49.45342     0.0000000E+00 200102\n" +
                        "  0.0000000E+00 0.0000000E+00 -163.0534     -321.8846     0.0000000E+00 110102\n" +
                        "  0.0000000E+00 0.0000000E+00 -222.8031     -502.3880     0.0000000E+00 020102\n" +
                        "   37.92860      1.244322     0.0000000E+00 0.0000000E+00  18.51529     101102\n" +
                        "   97.01792      12.49633     0.0000000E+00 0.0000000E+00  88.19571     011102\n" +
                        "  0.0000000E+00 0.0000000E+00  19.72998      9.862945     0.0000000E+00 002102\n" +
                        "   8.055143      8.987530     0.0000000E+00 0.0000000E+00  3.301354     200003\n" +
                        "   41.23459      45.31360     0.0000000E+00 0.0000000E+00  16.22427     110003\n" +
                        "   52.24412      49.84500     0.0000000E+00 0.0000000E+00  23.25294     020003\n" +
                        "  0.0000000E+00 0.0000000E+00  4.164547      4.716501     0.0000000E+00 101003\n" +
                        "  0.0000000E+00 0.0000000E+00  13.71998      21.53136     0.0000000E+00 011003\n" +
                        "  -1.144025     0.1931002     0.0000000E+00 0.0000000E+00-0.1073016     002003\n" +
                        "  -1246.498     -39.61576     0.0000000E+00 0.0000000E+00 -19.54641     100400\n" +
                        "  -2033.025      47.05037     0.0000000E+00 0.0000000E+00 -521.6863     010400\n" +
                        "  0.0000000E+00 0.0000000E+00  1662.841      175.4154     0.0000000E+00 001400\n" +
                        "  0.0000000E+00 0.0000000E+00  42.46330     -69.07097     0.0000000E+00 100301\n" +
                        "  0.0000000E+00 0.0000000E+00  532.1513     -182.8849     0.0000000E+00 010301\n" +
                        "   611.7549      38.88198     0.0000000E+00 0.0000000E+00  485.2334     001301\n" +
                        "   94.52245      17.57326     0.0000000E+00 0.0000000E+00  59.16199     100202\n" +
                        "   254.6611      113.1140     0.0000000E+00 0.0000000E+00  223.3084     010202\n" +
                        "  0.0000000E+00 0.0000000E+00  55.30771      58.56361     0.0000000E+00 001202\n" +
                        "  0.0000000E+00 0.0000000E+00  23.33700      37.95826     0.0000000E+00 100103\n" +
                        "  0.0000000E+00 0.0000000E+00  68.28371      139.8517     0.0000000E+00 010103\n" +
                        "  -16.12615     -5.371058     0.0000000E+00 0.0000000E+00 -5.326411     001103\n" +
                        "  -4.539099     -4.888509     0.0000000E+00 0.0000000E+00 -2.030649     100004\n" +
                        "  -13.73278     -14.59777     0.0000000E+00 0.0000000E+00 -6.172190     010004\n" +
                        "  0.0000000E+00 0.0000000E+00 -1.451375     -1.372056     0.0000000E+00 001004\n" +
                        "  0.0000000E+00 0.0000000E+00  1298.075      165.9984     0.0000000E+00 000500\n" +
                        "   494.9118     -23.45111     0.0000000E+00 0.0000000E+00  523.7422     000401\n" +
                        "  0.0000000E+00 0.0000000E+00  30.49345      81.28743     0.0000000E+00 000302\n" +
                        "  -49.02237     -36.43852     0.0000000E+00 0.0000000E+00 -18.80999     000203\n" +
                        "  0.0000000E+00 0.0000000E+00 -8.763419     -13.64926     0.0000000E+00 000104\n" +
                        "   1.181743      1.217642     0.0000000E+00 0.0000000E+00 0.8464628     000005"
        )),
        EXIT_CCT1(CosyArbitraryOrder.readMap(
                "  -1.015448     0.1463918     0.0000000E+00 0.0000000E+00 0.4584770E-01 100000\n" +
                        "  -2.224397    -0.6641075     0.0000000E+00 0.0000000E+00-0.5211732     010000\n" +
                        "  0.0000000E+00 0.0000000E+00  1.793513     0.6393535     0.0000000E+00 001000\n" +
                        "  0.0000000E+00 0.0000000E+00  7.771509      3.327961     0.0000000E+00 000100\n" +
                        "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00  1.000000     000010\n" +
                        "  0.6312077    -0.4584770E-01 0.0000000E+00 0.0000000E+00 0.4940636     000001\n" +
                        "  0.2458763     0.2011602E-01 0.0000000E+00 0.0000000E+00-0.4226607     200000\n" +
                        "   1.552804    -0.4573374     0.0000000E+00 0.0000000E+00 -2.242703     110000\n" +
                        " -0.2543840    -0.9720090     0.0000000E+00 0.0000000E+00 -2.639026     020000\n" +
                        "  0.0000000E+00 0.0000000E+00 -1.128057    -0.4134817     0.0000000E+00 101000\n" +
                        "  0.0000000E+00 0.0000000E+00  4.775471      4.700248     0.0000000E+00 011000\n" +
                        "   2.752542    -0.3867958     0.0000000E+00 0.0000000E+00 -1.637212     002000\n" +
                        "  0.0000000E+00 0.0000000E+00 -3.489107    -0.9422945     0.0000000E+00 100100\n" +
                        "  0.0000000E+00 0.0000000E+00  20.34788      18.75923     0.0000000E+00 010100\n" +
                        "   19.75136     -3.379979     0.0000000E+00 0.0000000E+00 -12.29178     001100\n" +
                        "  0.6953705E-01-0.8896977     0.0000000E+00 0.0000000E+00-0.9276130E-01 100001\n" +
                        " -0.4150309     -1.934579     0.0000000E+00 0.0000000E+00 0.9929779     010001\n" +
                        "  0.0000000E+00 0.0000000E+00  3.770711      3.038361     0.0000000E+00 001001\n" +
                        "   34.86857     -7.138481     0.0000000E+00 0.0000000E+00 -24.00958     000200\n" +
                        "  0.0000000E+00 0.0000000E+00  10.28824      9.836388     0.0000000E+00 000101\n" +
                        " -0.6159339     0.3180715     0.0000000E+00 0.0000000E+00-0.2493723     000002\n" +
                        " -0.3631973      1.134579     0.0000000E+00 0.0000000E+00 0.5472204     300000\n" +
                        " -0.6029567E-01  7.894375     0.0000000E+00 0.0000000E+00  2.760222     210000\n" +
                        "   4.865372      16.17046     0.0000000E+00 0.0000000E+00  2.855522     120000\n" +
                        "   6.702747      8.713189     0.0000000E+00 0.0000000E+00 -2.078925     030000\n" +
                        "  0.0000000E+00 0.0000000E+00 -4.105130     -3.891309     0.0000000E+00 201000\n" +
                        "  0.0000000E+00 0.0000000E+00 -22.53598     -16.40516     0.0000000E+00 111000\n" +
                        "  0.0000000E+00 0.0000000E+00 -28.04980     -16.98534     0.0000000E+00 021000\n" +
                        " -0.9643829      4.130900     0.0000000E+00 0.0000000E+00  2.672393     102000\n" +
                        "  -3.894511      6.964179     0.0000000E+00 0.0000000E+00  4.225614     012000\n" +
                        "  0.0000000E+00 0.0000000E+00  2.813423     0.7909767     0.0000000E+00 003000\n" +
                        "  0.0000000E+00 0.0000000E+00 -14.97166     -14.36962     0.0000000E+00 200100\n" +
                        "  0.0000000E+00 0.0000000E+00 -75.53987     -55.72439     0.0000000E+00 110100\n" +
                        "  0.0000000E+00 0.0000000E+00 -82.29626     -47.51212     0.0000000E+00 020100\n" +
                        "  -1.781612      30.11834     0.0000000E+00 0.0000000E+00  17.16538     101100\n" +
                        "  -16.25805      45.21216     0.0000000E+00 0.0000000E+00  20.16770     011100\n" +
                        "  0.0000000E+00 0.0000000E+00  34.02947      8.607018     0.0000000E+00 002100\n" +
                        "   1.629239     -1.118388     0.0000000E+00 0.0000000E+00-0.5958165     200001\n" +
                        "   3.071542     -7.064880     0.0000000E+00 0.0000000E+00 -1.096208     110001\n" +
                        " -0.1094765     -10.58870     0.0000000E+00 0.0000000E+00 0.8289925     020001\n" +
                        "  0.0000000E+00 0.0000000E+00  3.569197      4.893228     0.0000000E+00 101001\n" +
                        "  0.0000000E+00 0.0000000E+00  9.401851      11.25122     0.0000000E+00 011001\n" +
                        " -0.6582383     -1.637517     0.0000000E+00 0.0000000E+00-0.7464875     002001\n" +
                        "   5.974886      55.20779     0.0000000E+00 0.0000000E+00  26.84363     100200\n" +
                        "  -7.960243      72.60060     0.0000000E+00 0.0000000E+00  15.50110     010200\n" +
                        "  0.0000000E+00 0.0000000E+00  133.2181      28.51280     0.0000000E+00 001200\n" +
                        "  0.0000000E+00 0.0000000E+00  18.26346      20.97582     0.0000000E+00 100101\n" +
                        "  0.0000000E+00 0.0000000E+00  30.63242      38.03003     0.0000000E+00 010101\n" +
                        "  -16.62322     -13.01930     0.0000000E+00 0.0000000E+00-0.6145315     001101\n" +
                        "  -1.625512     0.4476605     0.0000000E+00 0.0000000E+00 0.5620505     100002\n" +
                        "  -2.757935      1.945202     0.0000000E+00 0.0000000E+00 0.5701084E-02 010002\n" +
                        "  0.0000000E+00 0.0000000E+00 -2.564299     -1.801675     0.0000000E+00 001002\n" +
                        "  0.0000000E+00 0.0000000E+00  172.8063      28.98877     0.0000000E+00 000300\n" +
                        "  -50.02838     -25.19066     0.0000000E+00 0.0000000E+00  9.226535     000201\n" +
                        "  0.0000000E+00 0.0000000E+00 -14.61697     -11.51040     0.0000000E+00 000102\n" +
                        "  0.7495342    -0.3370998     0.0000000E+00 0.0000000E+00 0.2009258E-01 000003\n" +
                        " -0.8954901    -0.2878393     0.0000000E+00 0.0000000E+00-0.1053047     400000\n" +
                        "  -5.987794    -0.2050553     0.0000000E+00 0.0000000E+00-0.4352469     310000\n" +
                        "  -15.28087      10.91466     0.0000000E+00 0.0000000E+00  3.066343     220000\n" +
                        "  -17.80235      40.80920     0.0000000E+00 0.0000000E+00  14.65115     130000\n" +
                        "  -7.720684      38.32645     0.0000000E+00 0.0000000E+00  12.80242     040000\n" +
                        "  0.0000000E+00 0.0000000E+00 0.2350709     -2.215751     0.0000000E+00 301000\n" +
                        "  0.0000000E+00 0.0000000E+00  2.476020     -14.63274     0.0000000E+00 211000\n" +
                        "  0.0000000E+00 0.0000000E+00  10.27960     -35.02089     0.0000000E+00 121000\n" +
                        "  0.0000000E+00 0.0000000E+00  1.856439     -30.15253     0.0000000E+00 031000\n" +
                        "   1.619510     -2.239490     0.0000000E+00 0.0000000E+00 -1.744539     202000\n" +
                        "   11.30781     -8.270630     0.0000000E+00 0.0000000E+00 -5.402615     112000\n" +
                        "   5.622088     -13.29516     0.0000000E+00 0.0000000E+00 -8.167492     022000\n" +
                        "  0.0000000E+00 0.0000000E+00 -5.042978    -0.6078426E-01 0.0000000E+00 103000\n" +
                        "  0.0000000E+00 0.0000000E+00  7.972840     0.2224823     0.0000000E+00 013000\n" +
                        "   1.457832     -6.192022     0.0000000E+00 0.0000000E+00 -4.075225     004000\n" +
                        "  0.0000000E+00 0.0000000E+00 -4.108636     -11.67497     0.0000000E+00 300100\n" +
                        "  0.0000000E+00 0.0000000E+00 -27.02503     -77.37824     0.0000000E+00 210100\n" +
                        "  0.0000000E+00 0.0000000E+00 -49.71383     -181.1497     0.0000000E+00 120100\n" +
                        "  0.0000000E+00 0.0000000E+00 -48.65973     -139.9280     0.0000000E+00 030100\n" +
                        "   13.38350     -5.923447     0.0000000E+00 0.0000000E+00 -7.230377     201100\n" +
                        "   84.69084     -10.94936     0.0000000E+00 0.0000000E+00 -11.12369     111100\n" +
                        "   40.17747     -41.31063     0.0000000E+00 0.0000000E+00 -31.44948     021100\n" +
                        "  0.0000000E+00 0.0000000E+00 -53.53853      1.254860     0.0000000E+00 102100\n" +
                        "  0.0000000E+00 0.0000000E+00  106.3968      8.288459     0.0000000E+00 012100\n" +
                        "   20.98356     -91.93206     0.0000000E+00 0.0000000E+00 -61.36881     003100\n" +
                        "   2.847192     0.1675610     0.0000000E+00 0.0000000E+00-0.7195800     300001\n" +
                        "   15.10063     -4.297414     0.0000000E+00 0.0000000E+00 -5.010284     210001\n" +
                        "   20.10737     -22.27965     0.0000000E+00 0.0000000E+00 -9.178049     120001\n" +
                        "   2.468679     -29.39251     0.0000000E+00 0.0000000E+00 -3.372686     030001\n" +
                        "  0.0000000E+00 0.0000000E+00  2.796993      4.570652     0.0000000E+00 201001\n" +
                        "  0.0000000E+00 0.0000000E+00  33.21832      30.58853     0.0000000E+00 111001\n" +
                        "  0.0000000E+00 0.0000000E+00  51.11480      45.67229     0.0000000E+00 021001\n" +
                        " -0.8783010    -0.5255049     0.0000000E+00 0.0000000E+00 -1.083773     102001\n" +
                        "  -10.95523     -13.56871     0.0000000E+00 0.0000000E+00 -9.930277     012001\n" +
                        "  0.0000000E+00 0.0000000E+00  9.031841    -0.2587610     0.0000000E+00 003001\n" +
                        "   28.23185      9.299585     0.0000000E+00 0.0000000E+00 -3.614724     200200\n" +
                        "   167.0644      72.18064     0.0000000E+00 0.0000000E+00  23.19401     110200\n" +
                        "   83.88541      21.60331     0.0000000E+00 0.0000000E+00 -25.35405     020200\n" +
                        "  0.0000000E+00 0.0000000E+00 -187.7777      7.108364     0.0000000E+00 101200\n" +
                        "  0.0000000E+00 0.0000000E+00  470.3653      41.46529     0.0000000E+00 011200\n" +
                        "   113.7586     -510.3781     0.0000000E+00 0.0000000E+00 -346.2329     002200\n" +
                        "  0.0000000E+00 0.0000000E+00  23.57729      29.39854     0.0000000E+00 200101\n" +
                        "  0.0000000E+00 0.0000000E+00  185.1635      165.6926     0.0000000E+00 110101\n" +
                        "  0.0000000E+00 0.0000000E+00  252.3348      218.2139     0.0000000E+00 020101\n" +
                        "  -10.61959     -25.62364     0.0000000E+00 0.0000000E+00 -18.62371     101101\n" +
                        "  -92.50750     -137.2260     0.0000000E+00 0.0000000E+00 -82.99720     011101\n" +
                        "  0.0000000E+00 0.0000000E+00  86.86612     -7.215427     0.0000000E+00 002101\n" +
                        "  -4.236244     0.5277390     0.0000000E+00 0.0000000E+00  1.316235     200002\n" +
                        "  -17.46858      6.555587     0.0000000E+00 0.0000000E+00  5.481852     110002\n" +
                        "  -14.09916      12.26874     0.0000000E+00 0.0000000E+00  2.617678     020002\n" +
                        "  0.0000000E+00 0.0000000E+00 -1.608130     -4.276595     0.0000000E+00 101002\n" +
                        "  0.0000000E+00 0.0000000E+00 -18.59943     -16.58619     0.0000000E+00 011002\n" +
                        "  -1.713668     -1.032336     0.0000000E+00 0.0000000E+00-0.2443653     002002\n" +
                        "  0.0000000E+00 0.0000000E+00 -221.4837      3.497250     0.0000000E+00 100300\n" +
                        "  0.0000000E+00 0.0000000E+00  691.8625      54.48628     0.0000000E+00 010300\n" +
                        "   285.9089     -1254.925     0.0000000E+00 0.0000000E+00 -871.8017     001300\n" +
                        "  -35.14210     -89.39768     0.0000000E+00 0.0000000E+00 -51.19016     100201\n" +
                        "  -212.4337     -318.2141     0.0000000E+00 0.0000000E+00 -152.5791     010201\n" +
                        "  0.0000000E+00 0.0000000E+00  268.5948     -38.23508     0.0000000E+00 001201\n" +
                        "  0.0000000E+00 0.0000000E+00 -19.85328     -29.00217     0.0000000E+00 100102\n" +
                        "  0.0000000E+00 0.0000000E+00 -94.14494     -87.28017     0.0000000E+00 010102\n" +
                        " -0.4162958     0.5986947     0.0000000E+00 0.0000000E+00 -1.141745     001102\n" +
                        "   2.966541    -0.5362359     0.0000000E+00 0.0000000E+00 -1.046890     100003\n" +
                        "   7.551225     -3.206795     0.0000000E+00 0.0000000E+00 -2.095554     010003\n" +
                        "  0.0000000E+00 0.0000000E+00 0.6225298      1.472789     0.0000000E+00 001003\n" +
                        "   288.5061     -1153.057     0.0000000E+00 0.0000000E+00 -831.3437     000400\n" +
                        "  0.0000000E+00 0.0000000E+00  269.8011     -47.45076     0.0000000E+00 000301\n" +
                        "   31.11142      19.64937     0.0000000E+00 0.0000000E+00 -4.988899     000202\n" +
                        "  0.0000000E+00 0.0000000E+00  11.51065      12.49591     0.0000000E+00 000103\n" +
                        " -0.9323312     0.2829229     0.0000000E+00 0.0000000E+00 0.1282134     000004\n" +
                        " -0.8738895     0.6814250E-01 0.0000000E+00 0.0000000E+00 0.4174930     500000\n" +
                        "  -7.812663    -0.2657215E-01 0.0000000E+00 0.0000000E+00  3.232630     410000\n" +
                        "  -26.25681     -7.730018     0.0000000E+00 0.0000000E+00  5.581652     320000\n" +
                        "  -37.66991     -33.10332     0.0000000E+00 0.0000000E+00 -9.225167     230000\n" +
                        "  -9.492473     -38.08915     0.0000000E+00 0.0000000E+00 -32.59732     140000\n" +
                        "   16.76971     -1.846781     0.0000000E+00 0.0000000E+00 -22.28793     050000\n" +
                        "  0.0000000E+00 0.0000000E+00-0.6520814     -1.125405     0.0000000E+00 401000\n" +
                        "  0.0000000E+00 0.0000000E+00 -17.10413     -9.988568     0.0000000E+00 311000\n" +
                        "  0.0000000E+00 0.0000000E+00 -81.41831     -35.98743     0.0000000E+00 221000\n" +
                        "  0.0000000E+00 0.0000000E+00 -139.1789     -70.43276     0.0000000E+00 131000\n" +
                        "  0.0000000E+00 0.0000000E+00 -75.69361     -60.40706     0.0000000E+00 041000\n" +
                        " -0.4899310      2.804468     0.0000000E+00 0.0000000E+00  2.355402     302000\n" +
                        "   9.475109      29.00152     0.0000000E+00 0.0000000E+00  21.65601     212000\n" +
                        "   49.93598      79.68268     0.0000000E+00 0.0000000E+00  61.64880     122000\n" +
                        "   50.60239      70.00186     0.0000000E+00 0.0000000E+00  56.66225     032000\n" +
                        "  0.0000000E+00 0.0000000E+00 -5.180231      1.617072     0.0000000E+00 203000\n" +
                        "  0.0000000E+00 0.0000000E+00 -51.05674      6.499554     0.0000000E+00 113000\n" +
                        "  0.0000000E+00 0.0000000E+00 -65.93031      6.677898     0.0000000E+00 023000\n" +
                        " -0.9225501      9.853940     0.0000000E+00 0.0000000E+00  8.662026     104000\n" +
                        "  -8.391921      12.57045     0.0000000E+00 0.0000000E+00  8.530339     014000\n" +
                        "  0.0000000E+00 0.0000000E+00  7.011321     0.8611704E-01 0.0000000E+00 005000\n" +
                        "  0.0000000E+00 0.0000000E+00 -3.004683     -7.106456     0.0000000E+00 400100\n" +
                        "  0.0000000E+00 0.0000000E+00 -67.25479     -62.73155     0.0000000E+00 310100\n" +
                        "  0.0000000E+00 0.0000000E+00 -319.7167     -226.8590     0.0000000E+00 220100\n" +
                        "  0.0000000E+00 0.0000000E+00 -576.9384     -422.9091     0.0000000E+00 130100\n" +
                        "  0.0000000E+00 0.0000000E+00 -346.1608     -324.8367     0.0000000E+00 040100\n" +
                        "   1.322315      19.52596     0.0000000E+00 0.0000000E+00  14.31309     301100\n" +
                        "   113.5149      199.6390     0.0000000E+00 0.0000000E+00  136.2800     211100\n" +
                        "   474.1117      544.6137     0.0000000E+00 0.0000000E+00  392.2605     121100\n" +
                        "   432.6557      468.6597     0.0000000E+00 0.0000000E+00  353.3414     031100\n" +
                        "  0.0000000E+00 0.0000000E+00 -68.32715      18.09185     0.0000000E+00 202100\n" +
                        "  0.0000000E+00 0.0000000E+00 -587.3592      71.20690     0.0000000E+00 112100\n" +
                        "  0.0000000E+00 0.0000000E+00 -635.2001      76.51258     0.0000000E+00 022100\n" +
                        "  -7.435362      124.4917     0.0000000E+00 0.0000000E+00  117.2929     103100\n" +
                        "  -108.0537      114.7275     0.0000000E+00 0.0000000E+00  76.64246     013100\n" +
                        "  0.0000000E+00 0.0000000E+00  138.0234      1.893671     0.0000000E+00 004100\n" +
                        "   4.499388     0.3170235     0.0000000E+00 0.0000000E+00-0.8294957     400001\n" +
                        "   38.00634      1.810944     0.0000000E+00 0.0000000E+00 -6.292537     310001\n" +
                        "   115.1798    -0.7395511     0.0000000E+00 0.0000000E+00 -19.69637     220001\n" +
                        "   151.2675     -34.62001     0.0000000E+00 0.0000000E+00 -38.24515     130001\n" +
                        "   65.66444     -59.69289     0.0000000E+00 0.0000000E+00 -29.17825     040001\n" +
                        "  0.0000000E+00 0.0000000E+00 -2.253612      5.141420     0.0000000E+00 301001\n" +
                        "  0.0000000E+00 0.0000000E+00  9.893730      42.38129     0.0000000E+00 211001\n" +
                        "  0.0000000E+00 0.0000000E+00  66.99216      117.1481     0.0000000E+00 121001\n" +
                        "  0.0000000E+00 0.0000000E+00  100.5329      117.8464     0.0000000E+00 031001\n" +
                        "   7.937731      6.944476     0.0000000E+00 0.0000000E+00  4.043715     202001\n" +
                        "   9.625584      17.50943     0.0000000E+00 0.0000000E+00  11.51760     112001\n" +
                        "  -23.92151      7.976624     0.0000000E+00 0.0000000E+00  7.146898     022001\n" +
                        "  0.0000000E+00 0.0000000E+00  1.182144     -1.292386     0.0000000E+00 103001\n" +
                        "  0.0000000E+00 0.0000000E+00  23.99701     -2.727653     0.0000000E+00 013001\n" +
                        "  -1.040047     -4.068928     0.0000000E+00 0.0000000E+00 -3.414605     004001\n" +
                        "   11.03789      40.02138     0.0000000E+00 0.0000000E+00  24.60360     300200\n" +
                        "   287.7579      393.0272     0.0000000E+00 0.0000000E+00  238.3554     210200\n" +
                        "   1074.332      1063.277     0.0000000E+00 0.0000000E+00  680.9122     120200\n" +
                        "   919.5590      892.3586     0.0000000E+00 0.0000000E+00  576.9131     030200\n" +
                        "  0.0000000E+00 0.0000000E+00 -299.1989      64.71865     0.0000000E+00 201200\n" +
                        "  0.0000000E+00 0.0000000E+00 -2284.992      241.3686     0.0000000E+00 111200\n" +
                        "  0.0000000E+00 0.0000000E+00 -2014.614      265.0888     0.0000000E+00 021200\n" +
                        "  -7.215786      572.7083     0.0000000E+00 0.0000000E+00  589.4220     102200\n" +
                        "  -507.1530      250.3265     0.0000000E+00 0.0000000E+00  148.1592     012200\n" +
                        "  0.0000000E+00 0.0000000E+00  1076.729      16.41679     0.0000000E+00 003200\n" +
                        "  0.0000000E+00 0.0000000E+00 0.3538172      31.93966     0.0000000E+00 300101\n" +
                        "  0.0000000E+00 0.0000000E+00  116.6782      255.1448     0.0000000E+00 210101\n" +
                        "  0.0000000E+00 0.0000000E+00  480.6919      695.7849     0.0000000E+00 120101\n" +
                        "  0.0000000E+00 0.0000000E+00  587.5417      663.4183     0.0000000E+00 030101\n" +
                        "   44.68663      46.24607     0.0000000E+00 0.0000000E+00  29.11084     201101\n" +
                        "  -30.55560      57.10292     0.0000000E+00 0.0000000E+00  39.32699     111101\n" +
                        "  -307.5934     -63.48304     0.0000000E+00 0.0000000E+00 -18.69340     021101\n" +
                        "  0.0000000E+00 0.0000000E+00  62.68322     -16.96729     0.0000000E+00 102101\n" +
                        "  0.0000000E+00 0.0000000E+00  309.8229     -40.36115     0.0000000E+00 012101\n" +
                        "  -26.71761     -15.73509     0.0000000E+00 0.0000000E+00 -25.62043     003101\n" +
                        "  -8.429620    -0.1536528     0.0000000E+00 0.0000000E+00  1.976605     300002\n" +
                        "  -59.85842      5.738107     0.0000000E+00 0.0000000E+00  15.53327     210002\n" +
                        "  -127.6507      29.63667     0.0000000E+00 0.0000000E+00  34.07965     120002\n" +
                        "  -83.32822      45.32655     0.0000000E+00 0.0000000E+00  23.16981     030002\n" +
                        "  0.0000000E+00 0.0000000E+00  5.070202     -6.868943     0.0000000E+00 201002\n" +
                        "  0.0000000E+00 0.0000000E+00 -20.78384     -49.97025     0.0000000E+00 111002\n" +
                        "  0.0000000E+00 0.0000000E+00 -70.55440     -85.28593     0.0000000E+00 021002\n" +
                        "  -8.306550     -8.387982     0.0000000E+00 0.0000000E+00 -3.282322     102002\n" +
                        "  -7.403449     -3.241105     0.0000000E+00 0.0000000E+00  6.116490     012002\n" +
                        "  0.0000000E+00 0.0000000E+00 -3.074139     0.4155091     0.0000000E+00 003002\n" +
                        "  0.0000000E+00 0.0000000E+00 -443.7807      62.41295     0.0000000E+00 200300\n" +
                        "  0.0000000E+00 0.0000000E+00 -3037.960      200.0092     0.0000000E+00 110300\n" +
                        "  0.0000000E+00 0.0000000E+00 -2120.969      227.0733     0.0000000E+00 020300\n" +
                        "   68.70417      1141.063     0.0000000E+00 0.0000000E+00  1308.232     101300\n" +
                        "  -1005.050     -279.6667     0.0000000E+00 0.0000000E+00 -316.8753     011300\n" +
                        "  0.0000000E+00 0.0000000E+00  4178.103      74.19298     0.0000000E+00 002300\n" +
                        "   55.24536      50.20608     0.0000000E+00 0.0000000E+00  38.90910     200201\n" +
                        "  -254.3319     -151.4339     0.0000000E+00 0.0000000E+00 -67.21216     110201\n" +
                        "  -838.9935     -479.2904     0.0000000E+00 0.0000000E+00 -195.8984     020201\n" +
                        "  0.0000000E+00 0.0000000E+00  413.3394     -70.43539     0.0000000E+00 101201\n" +
                        "  0.0000000E+00 0.0000000E+00  1266.678     -174.1222     0.0000000E+00 011201\n" +
                        "  -206.3110      161.6734     0.0000000E+00 0.0000000E+00 0.7330876     002201\n" +
                        "  0.0000000E+00 0.0000000E+00 -2.952055     -49.45342     0.0000000E+00 200102\n" +
                        "  0.0000000E+00 0.0000000E+00 -233.1599     -321.8846     0.0000000E+00 110102\n" +
                        "  0.0000000E+00 0.0000000E+00 -482.7812     -502.3880     0.0000000E+00 020102\n" +
                        "  -55.27043     -43.30146     0.0000000E+00 0.0000000E+00 -13.23959     101102\n" +
                        "   5.301490      66.06774     0.0000000E+00 0.0000000E+00  93.58798     011102\n" +
                        "  0.0000000E+00 0.0000000E+00 -69.61683      9.862945     0.0000000E+00 002102\n" +
                        "   8.326228    -0.3907195     0.0000000E+00 0.0000000E+00 -2.364914     200003\n" +
                        "   44.22780     -9.508343     0.0000000E+00 0.0000000E+00 -13.96732     110003\n" +
                        "   50.27435     -19.03688     0.0000000E+00 0.0000000E+00 -13.27806     020003\n" +
                        "  0.0000000E+00 0.0000000E+00 -4.595564      4.716501     0.0000000E+00 101003\n" +
                        "  0.0000000E+00 0.0000000E+00  14.28454      21.53136     0.0000000E+00 011003\n" +
                        "   3.645414      3.380531     0.0000000E+00 0.0000000E+00  2.142751     002003\n" +
                        "   157.6193      841.8098     0.0000000E+00 0.0000000E+00  1086.054     100400\n" +
                        "  -656.0400     -1011.442     0.0000000E+00 0.0000000E+00 -933.8194     010400\n" +
                        "  0.0000000E+00 0.0000000E+00  8096.991      175.4154     0.0000000E+00 001400\n" +
                        "  0.0000000E+00 0.0000000E+00  749.9838     -69.07097     0.0000000E+00 100301\n" +
                        "  0.0000000E+00 0.0000000E+00  1673.990     -182.8849     0.0000000E+00 010301\n" +
                        "  -664.1500      1003.418     0.0000000E+00 0.0000000E+00  367.2413     001301\n" +
                        "  -78.25777     -13.87493     0.0000000E+00 0.0000000E+00  8.977302     100202\n" +
                        "   159.6767      347.0940     0.0000000E+00 0.0000000E+00  264.6617     010202\n" +
                        "  0.0000000E+00 0.0000000E+00 -368.8800      58.56361     0.0000000E+00 001202\n" +
                        "  0.0000000E+00 0.0000000E+00 0.7318400E-01  37.95826     0.0000000E+00 100103\n" +
                        "  0.0000000E+00 0.0000000E+00  120.2034      139.8517     0.0000000E+00 010103\n" +
                        "   22.97936      22.46618     0.0000000E+00 0.0000000E+00  16.32657     001103\n" +
                        "  -4.622805     0.3107864     0.0000000E+00 0.0000000E+00  1.439044     100004\n" +
                        "  -14.29300      4.392301     0.0000000E+00 0.0000000E+00  4.908401     010004\n" +
                        "  0.0000000E+00 0.0000000E+00  1.752314     -1.372056     0.0000000E+00 001004\n" +
                        "  0.0000000E+00 0.0000000E+00  6291.212      165.9984     0.0000000E+00 000500\n" +
                        "  -809.4939      1460.076     0.0000000E+00 0.0000000E+00  698.3408     000401\n" +
                        "  0.0000000E+00 0.0000000E+00 -587.1038      81.28743     0.0000000E+00 000302\n" +
                        "   16.90037      23.06880     0.0000000E+00 0.0000000E+00  30.49870     000203\n" +
                        "  0.0000000E+00 0.0000000E+00 -2.640859     -13.64926     0.0000000E+00 000104\n" +
                        "   1.177631    -0.1541380     0.0000000E+00 0.0000000E+00-0.1930368     000005"
        )),
        ISOC(CosyArbitraryOrder.readMap(
                " -0.6641075     0.1463918     0.0000000E+00 0.0000000E+00 0.4584770E-01 100000\n" +
                        "  -3.818255    -0.6641075     0.0000000E+00 0.0000000E+00-0.5211732     010000\n" +
                        "  0.0000000E+00 0.0000000E+00  3.327961     0.6393535     0.0000000E+00 001000\n" +
                        "  0.0000000E+00 0.0000000E+00  15.75862      3.327961     0.0000000E+00 000100\n" +
                        "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00  1.000000     000010\n" +
                        "  0.5211732    -0.4584770E-01 0.0000000E+00 0.0000000E+00 0.9612821     000001\n" +
                        "  0.2941547     0.2011602E-01 0.0000000E+00 0.0000000E+00-0.4370307     200000\n" +
                        "  0.4551939    -0.4573374     0.0000000E+00 0.0000000E+00 -2.112324     110000\n" +
                        "  -2.587206    -0.9720090     0.0000000E+00 0.0000000E+00 -2.934759     020000\n" +
                        "  0.0000000E+00 0.0000000E+00 -2.120413    -0.4134817     0.0000000E+00 101000\n" +
                        "  0.0000000E+00 0.0000000E+00  16.05607      4.700248     0.0000000E+00 011000\n" +
                        "   1.824232    -0.3867958     0.0000000E+00 0.0000000E+00 -1.911309     002000\n" +
                        "  0.0000000E+00 0.0000000E+00 -5.750613    -0.9422945     0.0000000E+00 100100\n" +
                        "  0.0000000E+00 0.0000000E+00  65.37004      18.75923     0.0000000E+00 010100\n" +
                        "   11.63941     -3.379979     0.0000000E+00 0.0000000E+00 -15.14525     001100\n" +
                        "  -2.262060    -0.8896977     0.0000000E+00 0.0000000E+00-0.8376037E-01 100001\n" +
                        "  -4.167403     -1.934579     0.0000000E+00 0.0000000E+00 0.9521452     010001\n" +
                        "  0.0000000E+00 0.0000000E+00  10.20536      3.038361     0.0000000E+00 001001\n" +
                        "   17.73622     -7.138481     0.0000000E+00 0.0000000E+00 -31.43600     000200\n" +
                        "  0.0000000E+00 0.0000000E+00  29.43253      9.836388     0.0000000E+00 000101\n" +
                        "  0.2089228     0.3180715     0.0000000E+00 0.0000000E+00-0.6423908     000002\n" +
                        "   2.363557      1.134579     0.0000000E+00 0.0000000E+00 0.5432712     300000\n" +
                        "   18.83497      7.894375     0.0000000E+00 0.0000000E+00  2.867923     210000\n" +
                        "   43.90692      16.17046     0.0000000E+00 0.0000000E+00  2.639036     120000\n" +
                        "   27.26292      8.713189     0.0000000E+00 0.0000000E+00 -2.944613     030000\n" +
                        "  0.0000000E+00 0.0000000E+00 -13.42783     -3.891309     0.0000000E+00 201000\n" +
                        "  0.0000000E+00 0.0000000E+00 -62.05754     -16.40516     0.0000000E+00 111000\n" +
                        "  0.0000000E+00 0.0000000E+00 -68.47624     -16.98534     0.0000000E+00 021000\n" +
                        "   9.021587      4.130900     0.0000000E+00 0.0000000E+00  3.102857     102000\n" +
                        "   12.49376      6.964179     0.0000000E+00 0.0000000E+00-0.1489612     012000\n" +
                        "  0.0000000E+00 0.0000000E+00  5.025388     0.7909767     0.0000000E+00 003000\n" +
                        "  0.0000000E+00 0.0000000E+00 -49.37317     -14.36962     0.0000000E+00 200100\n" +
                        "  0.0000000E+00 0.0000000E+00 -210.0549     -55.72439     0.0000000E+00 110100\n" +
                        "  0.0000000E+00 0.0000000E+00 -194.5640     -47.51212     0.0000000E+00 020100\n" +
                        "   71.24997      30.11834     0.0000000E+00 0.0000000E+00  20.48227     101100\n" +
                        "   88.85982      45.21216     0.0000000E+00 0.0000000E+00 -19.90452     011100\n" +
                        "  0.0000000E+00 0.0000000E+00  59.58368      8.607018     0.0000000E+00 002100\n" +
                        "  -1.085406     -1.118388     0.0000000E+00 0.0000000E+00-0.3988465     200001\n" +
                        "  -13.23875     -7.064880     0.0000000E+00 0.0000000E+00 -1.728036     110001\n" +
                        "  -24.29161     -10.58870     0.0000000E+00 0.0000000E+00-0.5202106     020001\n" +
                        "  0.0000000E+00 0.0000000E+00  15.85715      4.893228     0.0000000E+00 101001\n" +
                        "  0.0000000E+00 0.0000000E+00  30.14812      11.25122     0.0000000E+00 011001\n" +
                        "  -4.092048     -1.637517     0.0000000E+00 0.0000000E+00 -2.973609     002001\n" +
                        "   140.4192      55.20779     0.0000000E+00 0.0000000E+00  32.45057     100200\n" +
                        "   157.4549      72.60060     0.0000000E+00 0.0000000E+00 -74.57981     010200\n" +
                        "  0.0000000E+00 0.0000000E+00  227.1406      28.51280     0.0000000E+00 001200\n" +
                        "  0.0000000E+00 0.0000000E+00  69.81550      20.97582     0.0000000E+00 100101\n" +
                        "  0.0000000E+00 0.0000000E+00  96.99017      38.03003     0.0000000E+00 010101\n" +
                        "  -43.57087     -13.01930     0.0000000E+00 0.0000000E+00 -18.63356     001101\n" +
                        "  0.7870310     0.4476605     0.0000000E+00 0.0000000E+00 0.4317078     100002\n" +
                        "   3.847137      1.945202     0.0000000E+00 0.0000000E+00 0.2298920     010002\n" +
                        "  0.0000000E+00 0.0000000E+00 -10.33290     -1.801675     0.0000000E+00 001002\n" +
                        "  0.0000000E+00 0.0000000E+00  286.6093      28.98877     0.0000000E+00 000300\n" +
                        "  -101.5221     -25.19066     0.0000000E+00 0.0000000E+00 -24.22582     000201\n" +
                        "  0.0000000E+00 0.0000000E+00 -52.15356     -11.51040     0.0000000E+00 000102\n" +
                        " -0.5312455    -0.3370998     0.0000000E+00 0.0000000E+00 0.3789581     000003\n" +
                        "  -1.584752    -0.2878393     0.0000000E+00 0.0000000E+00-0.3285501     400000\n" +
                        "  -6.529291    -0.2050553     0.0000000E+00 0.0000000E+00-0.9580841     310000\n" +
                        "   11.19140      10.91466     0.0000000E+00 0.0000000E+00  6.780041     220000\n" +
                        "   80.09399      40.80920     0.0000000E+00 0.0000000E+00  26.83235     130000\n" +
                        "   82.71949      38.32645     0.0000000E+00 0.0000000E+00  19.83119     040000\n" +
                        "  0.0000000E+00 0.0000000E+00 -5.088847     -2.215751     0.0000000E+00 301000\n" +
                        "  0.0000000E+00 0.0000000E+00 -32.54843     -14.63274     0.0000000E+00 211000\n" +
                        "  0.0000000E+00 0.0000000E+00 -74.83836     -35.02089     0.0000000E+00 121000\n" +
                        "  0.0000000E+00 0.0000000E+00 -67.03152     -30.15253     0.0000000E+00 031000\n" +
                        "  -3.868120     -2.239490     0.0000000E+00 0.0000000E+00 0.6679453     202000\n" +
                        "  -7.018111     -8.270630     0.0000000E+00 0.0000000E+00  13.42437     112000\n" +
                        "  -32.16696     -13.29516     0.0000000E+00 0.0000000E+00 -2.900758     022000\n" +
                        "  0.0000000E+00 0.0000000E+00 -5.884218    -0.6078426E-01 0.0000000E+00 103000\n" +
                        "  0.0000000E+00 0.0000000E+00  15.81776     0.2224823     0.0000000E+00 013000\n" +
                        "  -13.59276     -6.192022     0.0000000E+00 0.0000000E+00 -4.937777     004000\n" +
                        "  0.0000000E+00 0.0000000E+00 -32.12927     -11.67497     0.0000000E+00 300100\n" +
                        "  0.0000000E+00 0.0000000E+00 -212.6719     -77.37824     0.0000000E+00 210100\n" +
                        "  0.0000000E+00 0.0000000E+00 -488.0595     -181.1497     0.0000000E+00 120100\n" +
                        "  0.0000000E+00 0.0000000E+00 -369.4029     -139.9280     0.0000000E+00 030100\n" +
                        "  -1.685944     -5.923447     0.0000000E+00 0.0000000E+00  16.02155     201100\n" +
                        "   71.30600     -10.94936     0.0000000E+00 0.0000000E+00  142.9212     111100\n" +
                        "  -113.3461     -41.31063     0.0000000E+00 0.0000000E+00 0.8211646     021100\n" +
                        "  0.0000000E+00 0.0000000E+00 -59.45947      1.254860     0.0000000E+00 102100\n" +
                        "  0.0000000E+00 0.0000000E+00  231.3976      8.288459     0.0000000E+00 012100\n" +
                        "  -203.2866     -91.93206     0.0000000E+00 0.0000000E+00 -75.78170     003100\n" +
                        "   1.651860     0.1675610     0.0000000E+00 0.0000000E+00-0.4001757     300001\n" +
                        "  -5.214187     -4.297414     0.0000000E+00 0.0000000E+00 -4.789265     210001\n" +
                        "  -55.55094     -22.27965     0.0000000E+00 0.0000000E+00 -14.40817     120001\n" +
                        "  -82.45388     -29.39251     0.0000000E+00 0.0000000E+00 -13.54718     030001\n" +
                        "  0.0000000E+00 0.0000000E+00  18.84106      4.570652     0.0000000E+00 201001\n" +
                        "  0.0000000E+00 0.0000000E+00  128.5708      30.58853     0.0000000E+00 111001\n" +
                        "  0.0000000E+00 0.0000000E+00  186.9310      45.67229     0.0000000E+00 021001\n" +
                        "  -7.505864    -0.5255049     0.0000000E+00 0.0000000E+00 -4.106083     102001\n" +
                        "  -56.77417     -13.56871     0.0000000E+00 0.0000000E+00 -34.37508     012001\n" +
                        "  0.0000000E+00 0.0000000E+00  11.32273    -0.2587610     0.0000000E+00 003001\n" +
                        "   49.16569      9.299585     0.0000000E+00 0.0000000E+00  49.03744     200200\n" +
                        "   366.1490      72.18064     0.0000000E+00 0.0000000E+00  328.3038     110200\n" +
                        "   11.97639      21.60331     0.0000000E+00 0.0000000E+00  1.167728     020200\n" +
                        "  0.0000000E+00 0.0000000E+00 -207.1949      7.108364     0.0000000E+00 101200\n" +
                        "  0.0000000E+00 0.0000000E+00  1069.876      41.46529     0.0000000E+00 011200\n" +
                        "  -1137.051     -510.3781     0.0000000E+00 0.0000000E+00 -434.1179     002200\n" +
                        "  0.0000000E+00 0.0000000E+00  112.4815      29.39854     0.0000000E+00 200101\n" +
                        "  0.0000000E+00 0.0000000E+00  658.8166      165.6926     0.0000000E+00 110101\n" +
                        "  0.0000000E+00 0.0000000E+00  854.0072      218.2139     0.0000000E+00 020101\n" +
                        "  -112.1605     -25.62364     0.0000000E+00 0.0000000E+00 -53.58355     101101\n" +
                        "  -516.5988     -137.2260     0.0000000E+00 0.0000000E+00 -263.3608     011101\n" +
                        "  0.0000000E+00 0.0000000E+00  111.1982     -7.215427     0.0000000E+00 002101\n" +
                        "  -1.376441     0.5277390     0.0000000E+00 0.0000000E+00 0.3361800     200002\n" +
                        "   6.908340      6.555587     0.0000000E+00 0.0000000E+00  3.834732     110002\n" +
                        "   28.78618      12.26874     0.0000000E+00 0.0000000E+00  3.678648     020002\n" +
                        "  0.0000000E+00 0.0000000E+00 -18.73926     -4.276595     0.0000000E+00 101002\n" +
                        "  0.0000000E+00 0.0000000E+00 -68.90723     -16.58619     0.0000000E+00 011002\n" +
                        "  -2.398409     -1.032336     0.0000000E+00 0.0000000E+00 -1.469257     002002\n" +
                        "  0.0000000E+00 0.0000000E+00 -259.0074      3.497250     0.0000000E+00 100300\n" +
                        "  0.0000000E+00 0.0000000E+00  1608.447      54.48628     0.0000000E+00 010300\n" +
                        "  -2807.285     -1254.925     0.0000000E+00 0.0000000E+00 -1103.673     001300\n" +
                        "  -326.6290     -89.39768     0.0000000E+00 0.0000000E+00 -140.6233     100201\n" +
                        "  -1145.035     -318.2141     0.0000000E+00 0.0000000E+00 -474.8834     010201\n" +
                        "  0.0000000E+00 0.0000000E+00  369.4339     -38.23508     0.0000000E+00 001201\n" +
                        "  0.0000000E+00 0.0000000E+00 -117.8881     -29.00217     0.0000000E+00 100102\n" +
                        "  0.0000000E+00 0.0000000E+00 -336.7988     -87.28017     0.0000000E+00 010102\n" +
                        "   15.34452     0.5986947     0.0000000E+00 0.0000000E+00  4.695318     001102\n" +
                        "  0.6209074E-01-0.5362359     0.0000000E+00 0.0000000E+00-0.3856009     100003\n" +
                        "  -4.053719     -3.206795     0.0000000E+00 0.0000000E+00 -1.766022     010003\n" +
                        "  0.0000000E+00 0.0000000E+00  9.024529      1.472789     0.0000000E+00 001003\n" +
                        "  -2573.703     -1153.057     0.0000000E+00 0.0000000E+00 -1056.578     000400\n" +
                        "  0.0000000E+00 0.0000000E+00  437.7011     -47.45076     0.0000000E+00 000301\n" +
                        "   106.6282      19.64937     0.0000000E+00 0.0000000E+00  34.51462     000202\n" +
                        "  0.0000000E+00 0.0000000E+00  63.80354      12.49591     0.0000000E+00 000103\n" +
                        "  0.5511690     0.2829229     0.0000000E+00 0.0000000E+00-0.2906636     000004\n" +
                        " -0.6225409     0.6814250E-01 0.0000000E+00 0.0000000E+00 0.4432677     500000\n" +
                        "  -8.073608    -0.2657215E-01 0.0000000E+00 0.0000000E+00  3.504043     410000\n" +
                        "  -47.13976     -7.730018     0.0000000E+00 0.0000000E+00  9.099787     320000\n" +
                        "  -115.2250     -33.10332     0.0000000E+00 0.0000000E+00  12.56369     230000\n" +
                        "  -82.83056     -38.08915     0.0000000E+00 0.0000000E+00  22.75577     140000\n" +
                        "   23.79663     -1.846781     0.0000000E+00 0.0000000E+00  22.63152     050000\n" +
                        "  0.0000000E+00 0.0000000E+00 -3.200612     -1.125405     0.0000000E+00 401000\n" +
                        "  0.0000000E+00 0.0000000E+00 -39.87949     -9.988568     0.0000000E+00 311000\n" +
                        "  0.0000000E+00 0.0000000E+00 -171.7720     -35.98743     0.0000000E+00 221000\n" +
                        "  0.0000000E+00 0.0000000E+00 -325.6940     -70.43276     0.0000000E+00 131000\n" +
                        "  0.0000000E+00 0.0000000E+00 -230.4204     -60.40706     0.0000000E+00 041000\n" +
                        "   6.253304      2.804468     0.0000000E+00 0.0000000E+00  3.025633     302000\n" +
                        "   80.68562      29.00152     0.0000000E+00 0.0000000E+00  55.50374     212000\n" +
                        "   267.6823      79.68268     0.0000000E+00 0.0000000E+00  200.3820     122000\n" +
                        "   224.6173      70.00186     0.0000000E+00 0.0000000E+00  187.9128     032000\n" +
                        "  0.0000000E+00 0.0000000E+00 -5.629529      1.617072     0.0000000E+00 203000\n" +
                        "  0.0000000E+00 0.0000000E+00 -72.08923      6.499554     0.0000000E+00 113000\n" +
                        "  0.0000000E+00 0.0000000E+00 -27.04543      6.677898     0.0000000E+00 023000\n" +
                        "   25.27717      9.853940     0.0000000E+00 0.0000000E+00  12.77511     104000\n" +
                        "   21.14000      12.57045     0.0000000E+00 0.0000000E+00 -1.230811     014000\n" +
                        "  0.0000000E+00 0.0000000E+00  8.592924     0.8611704E-01 0.0000000E+00 005000\n" +
                        "  0.0000000E+00 0.0000000E+00 -19.10678     -7.106456     0.0000000E+00 400100\n" +
                        "  0.0000000E+00 0.0000000E+00 -212.4626     -62.73155     0.0000000E+00 310100\n" +
                        "  0.0000000E+00 0.0000000E+00 -886.1020     -226.8590     0.0000000E+00 220100\n" +
                        "  0.0000000E+00 0.0000000E+00 -1677.067     -422.9091     0.0000000E+00 130100\n" +
                        "  0.0000000E+00 0.0000000E+00 -1163.713     -324.8367     0.0000000E+00 040100\n" +
                        "   48.51806      19.52596     0.0000000E+00 0.0000000E+00  26.90159     301100\n" +
                        "   619.1832      199.6390     0.0000000E+00 0.0000000E+00  453.1166     211100\n" +
                        "   2015.441      544.6137     0.0000000E+00 0.0000000E+00  1571.183     121100\n" +
                        "   1589.273      468.6597     0.0000000E+00 0.0000000E+00  1364.289     031100\n" +
                        "  0.0000000E+00 0.0000000E+00 -89.39791      18.09185     0.0000000E+00 202100\n" +
                        "  0.0000000E+00 0.0000000E+00 -897.3469      71.20690     0.0000000E+00 112100\n" +
                        "  0.0000000E+00 0.0000000E+00 -141.4149      76.51258     0.0000000E+00 022100\n" +
                        "   335.8893      124.4917     0.0000000E+00 0.0000000E+00  179.4379     103100\n" +
                        "   153.7038      114.7275     0.0000000E+00 0.0000000E+00 -84.96058     013100\n" +
                        "  0.0000000E+00 0.0000000E+00  172.4576      1.893671     0.0000000E+00 004100\n" +
                        "   5.483519     0.3170235     0.0000000E+00 0.0000000E+00 0.8370640     400001\n" +
                        "   43.29357      1.810944     0.0000000E+00 0.0000000E+00  7.248936     310001\n" +
                        "   101.6497    -0.7395511     0.0000000E+00 0.0000000E+00  10.58073     220001\n" +
                        "   6.351623     -34.62001     0.0000000E+00 0.0000000E+00 -30.89022     130001\n" +
                        "  -150.4994     -59.69289     0.0000000E+00 0.0000000E+00 -55.64933     040001\n" +
                        "  0.0000000E+00 0.0000000E+00  13.04885      5.141420     0.0000000E+00 301001\n" +
                        "  0.0000000E+00 0.0000000E+00  127.3772      42.38129     0.0000000E+00 211001\n" +
                        "  0.0000000E+00 0.0000000E+00  405.8548      117.1481     0.0000000E+00 121001\n" +
                        "  0.0000000E+00 0.0000000E+00  455.4804      117.8464     0.0000000E+00 031001\n" +
                        "   28.86594      6.944476     0.0000000E+00 0.0000000E+00  19.54012     202001\n" +
                        "   56.14727      17.50943     0.0000000E+00 0.0000000E+00  16.51091     112001\n" +
                        "  -39.83620      7.976624     0.0000000E+00 0.0000000E+00 -46.93577     022001\n" +
                        "  0.0000000E+00 0.0000000E+00 0.1652118     -1.292386     0.0000000E+00 103001\n" +
                        "  0.0000000E+00 0.0000000E+00  91.68092     -2.727653     0.0000000E+00 013001\n" +
                        "  -4.877260     -4.068928     0.0000000E+00 0.0000000E+00 -7.899422     004001\n" +
                        "   109.5416      40.02138     0.0000000E+00 0.0000000E+00  66.21193     300200\n" +
                        "   1316.779      393.0272     0.0000000E+00 0.0000000E+00  973.6298     210200\n" +
                        "   4142.320      1063.277     0.0000000E+00 0.0000000E+00  3188.964     120200\n" +
                        "   3079.209      892.3586     0.0000000E+00 0.0000000E+00  2520.020     030200\n" +
                        "  0.0000000E+00 0.0000000E+00 -457.2223      64.71865     0.0000000E+00 201200\n" +
                        "  0.0000000E+00 0.0000000E+00 -3774.261      241.3686     0.0000000E+00 111200\n" +
                        "  0.0000000E+00 0.0000000E+00  48.79978      265.0888     0.0000000E+00 021200\n" +
                        "   1655.537      572.7083     0.0000000E+00 0.0000000E+00  941.0782     102200\n" +
                        "  -12.40265      250.3265     0.0000000E+00 0.0000000E+00 -849.6276     012200\n" +
                        "  0.0000000E+00 0.0000000E+00  1370.977      16.41679     0.0000000E+00 003200\n" +
                        "  0.0000000E+00 0.0000000E+00  91.93642      31.93966     0.0000000E+00 300101\n" +
                        "  0.0000000E+00 0.0000000E+00  817.8239      255.1448     0.0000000E+00 210101\n" +
                        "  0.0000000E+00 0.0000000E+00  2447.083      695.7849     0.0000000E+00 120101\n" +
                        "  0.0000000E+00 0.0000000E+00  2502.012      663.4183     0.0000000E+00 030101\n" +
                        "   175.4574      46.24607     0.0000000E+00 0.0000000E+00  113.4451     201101\n" +
                        "   34.34825      57.10292     0.0000000E+00 0.0000000E+00 -142.2675     111101\n" +
                        "  -840.7246     -63.48304     0.0000000E+00 0.0000000E+00 -662.0971     021101\n" +
                        "  0.0000000E+00 0.0000000E+00  72.59501     -16.96729     0.0000000E+00 102101\n" +
                        "  0.0000000E+00 0.0000000E+00  1129.661     -40.36115     0.0000000E+00 012101\n" +
                        "   17.69470     -15.73509     0.0000000E+00 0.0000000E+00 -90.92265     003101\n" +
                        "  -7.255947    -0.1536528     0.0000000E+00 0.0000000E+00-0.4216909     300002\n" +
                        "  -33.90480      5.738107     0.0000000E+00 0.0000000E+00-0.3153798     210002\n" +
                        "  -18.08561      29.63667     0.0000000E+00 0.0000000E+00  7.644963     120002\n" +
                        "   70.92045      45.32655     0.0000000E+00 0.0000000E+00  18.81664     030002\n" +
                        "  0.0000000E+00 0.0000000E+00 -21.52067     -6.868943     0.0000000E+00 201002\n" +
                        "  0.0000000E+00 0.0000000E+00 -190.7093     -49.97025     0.0000000E+00 111002\n" +
                        "  0.0000000E+00 0.0000000E+00 -349.8448     -85.28593     0.0000000E+00 021002\n" +
                        "  -27.26089     -8.387982     0.0000000E+00 0.0000000E+00 -19.60400     102002\n" +
                        "   3.009054     -3.241105     0.0000000E+00 0.0000000E+00  15.06660     012002\n" +
                        "  0.0000000E+00 0.0000000E+00  10.84147     0.4155091     0.0000000E+00 003002\n" +
                        "  0.0000000E+00 0.0000000E+00 -788.3501      62.41295     0.0000000E+00 200300\n" +
                        "  0.0000000E+00 0.0000000E+00 -5462.601      200.0092     0.0000000E+00 110300\n" +
                        "  0.0000000E+00 0.0000000E+00  674.1083      227.0733     0.0000000E+00 020300\n" +
                        "   3626.500      1141.063     0.0000000E+00 0.0000000E+00  2201.453     101300\n" +
                        "  -2037.011     -279.6667     0.0000000E+00 0.0000000E+00 -3045.167     011300\n" +
                        "  0.0000000E+00 0.0000000E+00  5419.107      74.19298     0.0000000E+00 002300\n" +
                        "   187.5572      50.20608     0.0000000E+00 0.0000000E+00  122.1953     200201\n" +
                        "  -961.4490     -151.4339     0.0000000E+00 0.0000000E+00 -858.9661     110201\n" +
                        "  -2909.160     -479.2904     0.0000000E+00 0.0000000E+00 -1828.284     020201\n" +
                        "  0.0000000E+00 0.0000000E+00  580.6496     -70.43539     0.0000000E+00 101201\n" +
                        "  0.0000000E+00 0.0000000E+00  4558.318     -174.1222     0.0000000E+00 011201\n" +
                        "   603.1923      161.6734     0.0000000E+00 0.0000000E+00 -351.6171     002201\n" +
                        "  0.0000000E+00 0.0000000E+00 -173.5170     -49.45342     0.0000000E+00 200102\n" +
                        "  0.0000000E+00 0.0000000E+00 -1253.778     -321.8846     0.0000000E+00 110102\n" +
                        "  0.0000000E+00 0.0000000E+00 -2024.381     -502.3880     0.0000000E+00 020102\n" +
                        "  -128.4906     -43.30146     0.0000000E+00 0.0000000E+00 -106.2284     101102\n" +
                        "   362.5307      66.06774     0.0000000E+00 0.0000000E+00  281.8663     011102\n" +
                        "  0.0000000E+00 0.0000000E+00  83.97458      9.862945     0.0000000E+00 002102\n" +
                        "   4.956835    -0.3907195     0.0000000E+00 0.0000000E+00 0.1664028     200003\n" +
                        "   7.032215     -9.508343     0.0000000E+00 0.0000000E+00 -4.510761     110003\n" +
                        "  -19.53186     -19.03688     0.0000000E+00 0.0000000E+00 -7.349569     020003\n" +
                        "  0.0000000E+00 0.0000000E+00  17.59239      4.716501     0.0000000E+00 101003\n" +
                        "  0.0000000E+00 0.0000000E+00  93.57956      21.53136     0.0000000E+00 011003\n" +
                        "   12.86446      3.380531     0.0000000E+00 0.0000000E+00  11.21427     002003\n" +
                        "   3042.333      841.8098     0.0000000E+00 0.0000000E+00  1955.114     100400\n" +
                        "  -3537.094     -1011.442     0.0000000E+00 0.0000000E+00 -3734.702     010400\n" +
                        "  0.0000000E+00 0.0000000E+00  10683.65      175.4154     0.0000000E+00 001400\n" +
                        "  0.0000000E+00 0.0000000E+00  1245.023     -69.07097     0.0000000E+00 100301\n" +
                        "  0.0000000E+00 0.0000000E+00  6132.560     -182.8849     0.0000000E+00 010301\n" +
                        "   2690.883      1003.418     0.0000000E+00 0.0000000E+00 -490.6300     001301\n" +
                        "  -6.104392     -13.87493     0.0000000E+00 0.0000000E+00 -83.48684     100202\n" +
                        "   1488.887      347.0940     0.0000000E+00 0.0000000E+00  864.2656     010202\n" +
                        "  0.0000000E+00 0.0000000E+00  138.8850      58.56361     0.0000000E+00 001202\n" +
                        "  0.0000000E+00 0.0000000E+00  149.9483      37.95826     0.0000000E+00 100103\n" +
                        "  0.0000000E+00 0.0000000E+00  587.8282      139.8517     0.0000000E+00 010103\n" +
                        "   75.10966      22.46618     0.0000000E+00 0.0000000E+00  71.16678     001103\n" +
                        "  -1.687610     0.3107864     0.0000000E+00 0.0000000E+00-0.1684820     100004\n" +
                        "   3.358968      4.392301     0.0000000E+00 0.0000000E+00  2.700443     010004\n" +
                        "  0.0000000E+00 0.0000000E+00 -7.279027     -1.372056     0.0000000E+00 001004\n" +
                        "  0.0000000E+00 0.0000000E+00  8416.323      165.9984     0.0000000E+00 000500\n" +
                        "   3480.352      1460.076     0.0000000E+00 0.0000000E+00 -121.8188     000401\n" +
                        "  0.0000000E+00 0.0000000E+00 -166.6528      81.28743     0.0000000E+00 000302\n" +
                        "   44.61758      23.06880     0.0000000E+00 0.0000000E+00  88.40134     000203\n" +
                        "  0.0000000E+00 0.0000000E+00 -68.76760     -13.64926     0.0000000E+00 000104\n" +
                        " -0.2167182    -0.1541380     0.0000000E+00 0.0000000E+00 0.4075554     000005"
        ));

        private CosyArbitraryOrder.CosyMapArbitraryOrder cosyMapArbitraryOrder;

        CosyMap第二段只有二极CCT(CosyArbitraryOrder.CosyMapArbitraryOrder cosyMapArbitraryOrder) {
            this.cosyMapArbitraryOrder = cosyMapArbitraryOrder;
        }

        public CosyArbitraryOrder.CosyMapArbitraryOrder getCosyMapArbitraryOrder() {
            return cosyMapArbitraryOrder;
        }
    }

    private enum CosyMapOthers {
        CCT345ONLY前后偏移DL2(
                TrajectoryFactory.setStartingPoint(getTrajectoryPart1().pointAtEnd())
                        .setStraightLine(DL2, getTrajectoryPart1().directAtEnd())
                        .addArcLine(trajectoryBigRPart2, true, dipoleCct345Angle)
                        .addStraitLine(DL2), DL2 * 2 + CCT345_LENGTH, CosyArbitraryOrder.readMap(
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
        ));


        private final Trajectory trajectory;
        private final double distance;
        private final CosyArbitraryOrder.CosyMapArbitraryOrder map;

        CosyMapOthers(Trajectory trajectory, double distance, CosyArbitraryOrder.CosyMapArbitraryOrder map) {
            this.trajectory = trajectory;
            this.distance = distance;
            this.map = map;
        }

        public Trajectory getTrajectory() {
            return trajectory;
        }

        public double getDistance() {
            return distance;
        }

        public CosyArbitraryOrder.CosyMapArbitraryOrder getMap() {
            return map;
        }
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
