package cn.edu.hust.zrx.cct.study.X旧机架研究.前45偏转段2;

import cn.edu.hust.zrx.cct.Logger;
import cn.edu.hust.zrx.cct.advanced.MathFunction;
import cn.edu.hust.zrx.cct.advanced.PolynomialFitter;
import cn.edu.hust.zrx.cct.advanced.UniEquationSolver;
import cn.edu.hust.zrx.cct.advanced.geneticAlgorithm.GeneticAlgorithm;
import cn.edu.hust.zrx.cct.advanced.geneticAlgorithm.base.Gene;
import cn.edu.hust.zrx.cct.base.BaseUtils;
import cn.edu.hust.zrx.cct.base.cct.Cct;
import cn.edu.hust.zrx.cct.base.cct.CctFactory;
import cn.edu.hust.zrx.cct.base.cct.Elements;
import cn.edu.hust.zrx.cct.base.line.Arcs;
import cn.edu.hust.zrx.cct.base.line.Trajectory;
import cn.edu.hust.zrx.cct.base.line.TrajectoryFactory;
import cn.edu.hust.zrx.cct.base.particle.*;
import cn.edu.hust.zrx.cct.base.point.Point2;
import cn.edu.hust.zrx.cct.base.point.Point3;
import cn.edu.hust.zrx.cct.base.python.Plot2d;
import cn.edu.hust.zrx.cct.base.opticsMagnet.qs.QsHardPlaneMagnet;
import cn.edu.hust.zrx.cct.base.vector.Vector2;
import cn.edu.hust.zrx.cct.base.vector.Vector3;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Description
 * A0423月报
 * 修复磁场计算问题
 * <p>
 * Data
 * 21:48
 *
 * @author zrx
 * @version 1.0
 */

@SuppressWarnings("all")
public class A0423月报 {

    @run(-100)
    public void 回顾建模() {

        agCctIInner = 0;
        agCctIOuter = 0;

        dipoleCctA1Inner = 0.0;
        dipoleCctA1Outer = 0.0;

        Trajectory trajectory = getTrajectory();

        Cct cct1 = getCct1();

        double mid = DL1 + dipoleCctBigR * BaseUtils.Converter.angleToRadian(dipoleCctAngle / 2);
        Logger.getLogger().info("mid = " + mid);

        List<Point2> list = cct1.magnetBzAlongTrajectory(trajectory, MM);

        Plot2d.plotYLine(mid, list);


        Plot2d.plot2(list);

        Plot2d.info("s/m", "B1/T", "dipole cct only", 18);

        Plot2d.showThread();
    }


    @run(value = 110, code = "A0423-1954-001")
    public void 好场区研究() {
        /**
         * 好场区要求
         * 1. 万分之5，即+/-5e4
         * 2. 亦有一说是万分之1.5
         */


        agCctIInner = 0;
        agCctIOuter = 0;

        dipoleCctA1Inner = Math.pow(dipoleCctSmallRInner, 2) * 0.217;
        dipoleCctA1Outer = -Math.pow(dipoleCctSmallROuter, 2) * 0.217;

        //    private double dipoleCctA1Inner = Math.pow(dipoleCctSmallRInner, 2) * 0.225;
        //    private double dipoleCctA1Outer = -Math.pow(dipoleCctSmallROuter, 2) * 0.225;

        Trajectory trajectory = getTrajectory();

        Cct cct1 = getCct1();

        double mid = DL1 + dipoleCctBigR * BaseUtils.Converter.angleToRadian(dipoleCctAngle / 2);

        Point2 midPoint = trajectory.pointAt(mid);
        Point2 rightHandSidePoint = trajectory.rightHandSidePoint(mid, 1);

        Vector3 right = Vector2.from(midPoint).to(rightHandSidePoint).toVector3();
        Vector3 top = Vector3.getZDirect();

        Point3 orign = midPoint.toPoint3();

        double bz0 = cct1.magnetAt(orign).z;

        int length = 200;

        double maxInR = bz0;
        double minInR = bz0;

        BaseUtils.Content.BiContent<Double, Double> maxMinInR = BaseUtils.Content.BiContent.create(maxInR, minInR);

        double[] linspace = BaseUtils.Python.linspace(-dipoleCctSmallRInner, dipoleCctSmallRInner, length);

        double[][] data = (double[][]) Array.newInstance(double.class, length, length);


        // get max min
        BaseUtils.StreamTools.from(0).to(length - 1)
                .parallel()
                .forEach(numZ -> {
                    BaseUtils.StreamTools.from(0).to(length - 1)
                            .forEach(numX -> {

                                double z = linspace[numZ];
                                double x = linspace[numX];

                                Point3 point3 = orign.copy().moveSelf(top.dot(z).addSelf(right.dot(x)));

                                double bz = cct1.magnetAt(point3).z;

                                if (x * x + z * z < dipoleCctSmallRInner * dipoleCctSmallRInner * 0.81) {

                                    maxMinInR.setT1(Math.max(maxMinInR.getT1(), bz));

                                    maxMinInR.setT1(Math.min(maxMinInR.getT2(), bz));
                                }

                            });
                });

        BaseUtils.StreamTools.from(0).to(length - 1)
                .parallel()
                .forEach(numZ -> {
                    BaseUtils.StreamTools.from(0).to(length - 1)
                            .forEach(numX -> {

                                double z = linspace[numZ];
                                double x = linspace[numX];

                                Point3 point3 = orign.copy().moveSelf(top.dot(z).addSelf(right.dot(x)));

                                double bz = cct1.magnetAt(point3).z;

                                if (x * x + z * z > dipoleCctSmallRInner * dipoleCctSmallRInner * 0.81) {
                                    bz = maxMinInR.getT2();//min
                                }


                                data[numZ][numX] = bz;

                            });
                });


        Arrays.stream(data).forEach(arr -> System.out.println(Arrays.toString(arr) + ","));


        Logger.getLogger().info("dipoleCctSmallRInner = " + dipoleCctSmallRInner);

        Logger.getLogger().info("maxMinInR = " + maxMinInR.toStringWithInfo("max", "min"));

//        trajectory.plot3d();

//        Plot3d.plotPoint(midPoint.toPoint3(),Plot2d.GREEN_POINT);
//        Plot3d.plotPoint(rightHandSidePoint.toPoint3(),Plot2d.GREEN_POINT);
//
//        Plot3d.setCenter(trajectory.midPoint().toPoint3(),trajectory.getLength()/1.5);
//
//        Plot3d.showThread();

    }

    @run(value = 105, code = "A0423-1954-001-2")
    public void 好场区研究2() {
        agCctIInner = 0;
        agCctIOuter = 0;

        dipoleCctA1Inner = 0.0;
        dipoleCctA1Outer = 0.0;

        //    private double dipoleCctA1Inner = Math.pow(dipoleCctSmallRInner, 2) * 0.225;
        //    private double dipoleCctA1Outer = -Math.pow(dipoleCctSmallROuter, 2) * 0.225;

        Trajectory trajectory = getTrajectory();

        double mid = DL1 + dipoleCctBigR * BaseUtils.Converter.angleToRadian(dipoleCctAngle / 2);

        Point2 midPoint = trajectory.pointAt(mid);
        Point2 rightHandSidePoint = trajectory.rightHandSidePoint(mid, 1);

        Vector3 right = Vector2.from(midPoint).to(rightHandSidePoint).toVector3();
        Vector3 top = Vector3.getZDirect();

        Point3 orign = midPoint.toPoint3();

        int length = 200;

        double[] linspace = BaseUtils.Python.linspace(-dipoleCctSmallRInner, dipoleCctSmallRInner, length);

        List<Point2> k2Diff = BaseUtils.Python.linspaceStream(0.1, 0.3, 100)
                .mapToObj(k -> {
                    Logger.getLogger().info("k = " + k);

                    dipoleCctA1Inner = Math.pow(dipoleCctSmallRInner, 2) * k;
                    dipoleCctA1Outer = -Math.pow(dipoleCctSmallROuter, 2) * k;

                    Cct cct1 = getCct1();

                    double bz0 = cct1.magnetAt(orign).z;

                    BaseUtils.Content.BiContent<Double, Double> maxMinInR = BaseUtils.Content.BiContent.create(bz0, bz0);

                    // get max min
                    BaseUtils.StreamTools.from(0).to(length - 1)
                            .parallel()
                            .forEach(numZ -> {
                                BaseUtils.StreamTools.from(0).to(length - 1)
                                        .forEach(numX -> {

                                            double z = linspace[numZ];
                                            double x = linspace[numX];

                                            if (x * x + z * z < dipoleCctSmallRInner * dipoleCctSmallRInner * 0.81) {

                                                Point3 point3 = orign.copy().moveSelf(top.dot(z).addSelf(right.dot(x)));

                                                double bz = cct1.magnetAt(point3).z;

                                                maxMinInR.setT1(Math.max(maxMinInR.getT1(), bz));

                                                maxMinInR.setT2(Math.min(maxMinInR.getT2(), bz));
                                            }

                                        });
                            });

                    Double max = maxMinInR.getT1();
                    Double min = maxMinInR.getT2();

                    return Point2.create(k, (max - min) / Gs);
                }).collect(Collectors.toList());

        Plot2d.plot2(k2Diff, Plot2d.BLACK_LINE);

        Plot2d.info("k", "range/Gs", "(max - min) in 90% aperture", 18);

        Plot2d.showThread();
    }

    @run(value = 115, code = "A0423-0915-021")
    public void 好场区引入六极场() {
        agCctIInner = 0;
        agCctIOuter = 0;


        dipoleCctA1Inner = Math.pow(dipoleCctSmallRInner, 2) * 0.217;
        dipoleCctA1Outer = -Math.pow(dipoleCctSmallROuter, 2) * 0.217;

        //    private double dipoleCctA1Inner = Math.pow(dipoleCctSmallRInner, 2) * 0.225;
        //    private double dipoleCctA1Outer = -Math.pow(dipoleCctSmallROuter, 2) * 0.225;

        Trajectory trajectory = getTrajectory();

        double mid = DL1 + dipoleCctBigR * BaseUtils.Converter.angleToRadian(dipoleCctAngle / 2);

        Point2 midPoint = trajectory.pointAt(mid);
        Point2 rightHandSidePoint = trajectory.rightHandSidePoint(mid, 1);

        Vector3 right = Vector2.from(midPoint).to(rightHandSidePoint).toVector3();
        Vector3 top = Vector3.getZDirect();

        Point3 orign = midPoint.toPoint3();

        int length = 200;

        double[] linspace = BaseUtils.Python.linspace(-dipoleCctSmallRInner, dipoleCctSmallRInner, length);

        List<Point2> k2Diff = BaseUtils.Python.linspaceStream(-1, 1, 100)
                .mapToObj(k -> {
                    Logger.getLogger().info("k = " + k);

                    dipoleCctA2Inner = Math.pow(dipoleCctSmallRInner, 3) * k;
                    dipoleCctA2Outer = -Math.pow(dipoleCctSmallROuter, 3) * k;


                    Cct cct1 = getCct1();

                    double bz0 = cct1.magnetAt(orign).z;

                    BaseUtils.Content.BiContent<Double, Double> maxMinInR = BaseUtils.Content.BiContent.create(bz0, bz0);

                    // get max min
                    BaseUtils.StreamTools.from(0).to(length - 1)
                            .parallel()
                            .forEach(numZ -> {
                                BaseUtils.StreamTools.from(0).to(length - 1)
                                        .forEach(numX -> {

                                            double z = linspace[numZ];
                                            double x = linspace[numX];

                                            if (x * x + z * z < dipoleCctSmallRInner * dipoleCctSmallRInner * 0.81) {

                                                Point3 point3 = orign.copy().moveSelf(top.dot(z).addSelf(right.dot(x)));

                                                double bz = cct1.magnetAt(point3).z;

                                                maxMinInR.setT1(Math.max(maxMinInR.getT1(), bz));

                                                maxMinInR.setT2(Math.min(maxMinInR.getT2(), bz));
                                            }

                                        });
                            });

                    Double max = maxMinInR.getT1();
                    Double min = maxMinInR.getT2();

                    return Point2.create(k, (max - min) / Gs);
                }).collect(Collectors.toList());

        Plot2d.plot2(k2Diff, Plot2d.BLACK_LINE);

        Plot2d.info("k", "range/Gs", "(max - min) in 90% aperture", 18);

        Plot2d.showThread();
    }

    // 由此可见好场区范围是 60%
    @run(value = 120, code = "A0423-1003-022")
    public void 求好场区范围() {
        /**
         * 然后现在查看好场区范围。认为磁场均匀度1e-3是好场区
         */
        agCctIInner = 0;
        agCctIOuter = 0;

        dipoleCctA1Inner = Math.pow(dipoleCctSmallRInner, 2) * 0.217;
        dipoleCctA1Outer = -Math.pow(dipoleCctSmallROuter, 2) * 0.217;

        //    private double dipoleCctA1Inner = Math.pow(dipoleCctSmallRInner, 2) * 0.225;
        //    private double dipoleCctA1Outer = -Math.pow(dipoleCctSmallROuter, 2) * 0.225;

        Trajectory trajectory = getTrajectory();

        double mid = DL1 + dipoleCctBigR * BaseUtils.Converter.angleToRadian(dipoleCctAngle / 2);

        Point2 midPoint = trajectory.pointAt(mid);
        Point2 rightHandSidePoint = trajectory.rightHandSidePoint(mid, 1);

        Vector3 right = Vector2.from(midPoint).to(rightHandSidePoint).toVector3();
        Vector3 top = Vector3.getZDirect();

        Point3 orign = midPoint.toPoint3();

        Cct cct1 = getCct1();
        double bz0 = cct1.magnetAt(orign).z;

        Logger.getLogger().info("bz0 = " + bz0);

        int length = 200;

        double[] linspace = BaseUtils.Python.linspace(-dipoleCctSmallRInner, dipoleCctSmallRInner, length);

        List<Point2> k2Diff = BaseUtils.Python.linspaceStream(0.1, 0.9, 100)
                .mapToObj(range -> {

                    Logger.getLogger().info("range = " + range);

                    BaseUtils.Content.BiContent<Double, Double> maxMinInR = BaseUtils.Content.BiContent.create(bz0, bz0);

                    // get max min
                    BaseUtils.StreamTools.from(0).to(length - 1)
                            .parallel()
                            .forEach(numZ -> {
                                BaseUtils.StreamTools.from(0).to(length - 1)
                                        .forEach(numX -> {

                                            double z = linspace[numZ];
                                            double x = linspace[numX];

                                            if (x * x + z * z < dipoleCctSmallRInner * dipoleCctSmallRInner * range * range) {

                                                Point3 point3 = orign.copy().moveSelf(top.dot(z).addSelf(right.dot(x)));

                                                double bz = cct1.magnetAt(point3).z;

                                                maxMinInR.setT1(Math.max(maxMinInR.getT1(), bz));

                                                maxMinInR.setT2(Math.min(maxMinInR.getT2(), bz));
                                            }

                                        });
                            });

                    Double max = maxMinInR.getT1();
                    Double min = maxMinInR.getT2();

                    return Point2.create(range / PRESENT, (max - min) / -bz0);
                }).collect(Collectors.toList());

        Plot2d.plot2(k2Diff, Plot2d.BLACK_LINE);

        Plot2d.plotXLine(1e-3, 100, 0, Plot2d.GREY_DASH);

        Plot2d.info("range/%", "uniformity/1", "(max - min) in 90% aperture", 18);

        Plot2d.showThread();

    }

    @run(value = 125, code = "A0423-1022-023")
    public void 其他位置的60_PERSNET下均匀度() {
        agCctIInner = 0;
        agCctIOuter = 0;

        dipoleCctA1Inner = Math.pow(dipoleCctSmallRInner, 2) * 0.217;
        dipoleCctA1Outer = -Math.pow(dipoleCctSmallROuter, 2) * 0.217;

        //    private double dipoleCctA1Inner = Math.pow(dipoleCctSmallRInner, 2) * 0.225;
        //    private double dipoleCctA1Outer = -Math.pow(dipoleCctSmallROuter, 2) * 0.225;


        int length = 200;

        double[] linspace = BaseUtils.Python.linspace(-dipoleCctSmallRInner, dipoleCctSmallRInner, length);

        Trajectory trajectory = getTrajectory();

        Cct cct1 = getCct1();

        Vector3 top = Vector3.getZDirect().changeLengthSelf(1);

        List<Point2> collect = BaseUtils.Python.linspaceStream(DL1, DL1 + dipoleCctBigR * BaseUtils.Converter.angleToRadian(dipoleCctAngle), 300)
                .mapToObj(location -> {
                    Point2 pointAtLocal = trajectory.pointAt(location);
                    Point2 rightHandSidePoint = trajectory.rightHandSidePoint(location, 1);
                    Vector3 right = Vector2.from(pointAtLocal).to(rightHandSidePoint).toVector3().changeLengthSelf(1);

                    Point3 orign = pointAtLocal.toPoint3();

                    double bz0 = cct1.magnetAt(orign).z;

                    Logger.getLogger().info("bz0 = " + bz0);

                    BaseUtils.Content.BiContent<Double, Double> maxMinInR = BaseUtils.Content.BiContent.create(bz0, bz0);

                    //
                    BaseUtils.StreamTools.from(0).to(length - 1)
                            .parallel()
                            .forEach(numZ -> {
                                BaseUtils.StreamTools.from(0).to(length - 1)
                                        .forEach(numX -> {

                                            double z = linspace[numZ];
                                            double x = linspace[numX];

                                            if (x * x + z * z < dipoleCctSmallRInner * dipoleCctSmallRInner * 0.6 * 0.6) {

                                                Point3 point3 = orign.copy().moveSelf(top.dot(z).addSelf(right.dot(x)));

                                                double bz = cct1.magnetAt(point3).z;

                                                maxMinInR.setT1(Math.max(maxMinInR.getT1(), bz));

                                                maxMinInR.setT2(Math.min(maxMinInR.getT2(), bz));
                                            }

                                        });
                            });

                    Double max = maxMinInR.getT1();
                    Double min = maxMinInR.getT2();

                    return Point2.create(BaseUtils.Converter.radianToAngle((location - DL1) / dipoleCctBigR), (max - min) / -bz0);
                }).collect(Collectors.toList());

        Plot2d.plot2(collect, Plot2d.BLACK_LINE);

        Plot2d.plotXLine(1e-3, 25, -5, Plot2d.GREY_DASH);

        Plot2d.info("φ/degree", "uniformity/1", "", 18);

        Plot2d.showThread();

//        trajectory.plot3d();
//
//        Plot3d.plot3(collect,Plot2d.RED_POINT);
//
//        Plot3d.showThread();


    }


    @run(value = 130000, code = "A0423-1306-053")
    public void _DEGREE处热图绘制() {
        double phi = 0.0;

        /**
         * 好场区要求
         * 1. 万分之5，即+/-5e4
         * 2. 亦有一说是万分之1.5
         */


        agCctIInner = 0;
        agCctIOuter = 0;

        dipoleCctA1Inner = Math.pow(dipoleCctSmallRInner, 2) * 0.217;
        dipoleCctA1Outer = -Math.pow(dipoleCctSmallROuter, 2) * 0.217;

        //    private double dipoleCctA1Inner = Math.pow(dipoleCctSmallRInner, 2) * 0.225;
        //    private double dipoleCctA1Outer = -Math.pow(dipoleCctSmallROuter, 2) * 0.225;

        Trajectory trajectory = getTrajectory();

        Cct cct1 = getCct1();

        double mid = DL1 + dipoleCctBigR * BaseUtils.Converter.angleToRadian(phi);

        Point2 midPoint = trajectory.pointAt(mid);
        Point2 rightHandSidePoint = trajectory.rightHandSidePoint(mid, 1);

        Vector3 right = Vector2.from(midPoint).to(rightHandSidePoint).toVector3();
        Vector3 top = Vector3.getZDirect();

        Point3 orign = midPoint.toPoint3();

        double bz0 = cct1.magnetAt(orign).z;

        int length = 200;

        double maxInR = bz0;
        double minInR = bz0;

        BaseUtils.Content.BiContent<Double, Double> maxMinInR = BaseUtils.Content.BiContent.create(maxInR, minInR);

        double[] linspace = BaseUtils.Python.linspace(-dipoleCctSmallRInner, dipoleCctSmallRInner, length);

        double[][] data = (double[][]) Array.newInstance(double.class, length, length);


        // get max min
        BaseUtils.StreamTools.from(0).to(length - 1)
                .parallel()
                .forEach(numZ -> {
                    BaseUtils.StreamTools.from(0).to(length - 1)
                            .forEach(numX -> {

                                double z = linspace[numZ];
                                double x = linspace[numX];

                                Point3 point3 = orign.copy().moveSelf(top.dot(z).addSelf(right.dot(x)));

                                double bz = cct1.magnetAt(point3).z;

                                if (x * x + z * z < dipoleCctSmallRInner * dipoleCctSmallRInner * 0.81) {

                                    maxMinInR.setT1(Math.max(maxMinInR.getT1(), bz));

                                    maxMinInR.setT1(Math.min(maxMinInR.getT2(), bz));
                                }

                            });
                });

        BaseUtils.StreamTools.from(0).to(length - 1)
                .parallel()
                .forEach(numZ -> {
                    BaseUtils.StreamTools.from(0).to(length - 1)
                            .forEach(numX -> {

                                double z = linspace[numZ];
                                double x = linspace[numX];

                                Point3 point3 = orign.copy().moveSelf(top.dot(z).addSelf(right.dot(x)));

                                double bz = cct1.magnetAt(point3).z;

                                if (x * x + z * z > dipoleCctSmallRInner * dipoleCctSmallRInner * 0.81) {
                                    bz = maxMinInR.getT2();//min
                                }


                                data[numZ][numX] = bz;

                            });
                });


        Arrays.stream(data).forEach(arr -> System.out.println(Arrays.toString(arr) + ","));


        Logger.getLogger().info("dipoleCctSmallRInner = " + dipoleCctSmallRInner);

        Logger.getLogger().info("maxMinInR = " + maxMinInR.toStringWithInfo("max", "min"));

    }


    @run(value = 140, code = "A0423-1327-055")
    public void 中轴线积分场计算() {


        agCctIInner = 0;
        agCctIOuter = 0;

        dipoleCctA1Inner = Math.pow(dipoleCctSmallRInner, 2) * 0.217;
        dipoleCctA1Outer = -Math.pow(dipoleCctSmallROuter, 2) * 0.217;

        //    private double dipoleCctA1Inner = Math.pow(dipoleCctSmallRInner, 2) * 0.225;
        //    private double dipoleCctA1Outer = -Math.pow(dipoleCctSmallROuter, 2) * 0.225;

        Cct cct1 = getCct1();


        Trajectory trajectory300 = TrajectoryFactory.setStartingPoint(Point2.origin())
                .setStraightLine(DL1, Vector2.xDirect())
                .addArcLine(trajectoryBigR, false, dipoleCctAngle)
                .addStraitLine(3.00);


//        List<Point2> list = cct1.magnetBzAlongTrajectory(trajectory300, MM);
//
//        Plot2d.plot2(list);
//
//        Plot2d.showThread();

        int part = 100000;

        double[] linspace = BaseUtils.Python.linspace(0, trajectory300.getLength(), part);
        final double distance = linspace[1] - linspace[0];


        double sum = Arrays.stream(linspace)
                .limit(linspace.length - 1)
                .parallel()
                .map(s -> {
                    double next = s + distance;

                    Point2 sPoint = trajectory300.pointAt(s);
                    Point2 nextPoint = trajectory300.pointAt(next);


                    Point2 midPoint = Point2.midPoint(sPoint, nextPoint);

                    return cct1.magnetAt(midPoint.toPoint3()).z;
                })
                .map(m -> -m * distance)
                .sum();

        Logger.getLogger().info("sum = " + sum);

    }

    @run(value = 145, code = "A0423-1544-056")
    public void 粒子跟踪() {
        dipoleCctIInner = 9919.082986140875;
        dipoleCctIOuter = -dipoleCctIInner;

        agCctIInner = 0;
        agCctIOuter = 0;

        dipoleCctA1Inner = Math.pow(dipoleCctSmallRInner, 2) * 0.217;
        dipoleCctA1Outer = -Math.pow(dipoleCctSmallROuter, 2) * 0.217;

        //    private double dipoleCctA1Inner = Math.pow(dipoleCctSmallRInner, 2) * 0.225;
        //    private double dipoleCctA1Outer = -Math.pow(dipoleCctSmallROuter, 2) * 0.225;

        Cct cct1 = getCct1();


        Trajectory trajectory300 = TrajectoryFactory.setStartingPoint(Point2.origin())
                .setStraightLine(DL1, Vector2.xDirect())
                .addArcLine(trajectoryBigR, false, dipoleCctAngle)
                .addStraitLine(3.00);

        RunningParticle p = ParticleFactory.createIdealProtonAtTrajectory250MeV(trajectory300);


        if (true) {// xy


            List<Point3> run3 = ParticleRunner.run(p, cct1, trajectory300.getLength(), MM);

            List<Point2> run2 = run3.stream().map(Point3::toPoint2).collect(Collectors.toList());

            trajectory300.plot2d(MM, Plot2d.BLACK_LINE);

            Plot2d.plot2(run2, Plot2d.RED_LINE);

            Plot2d.info("global_x/m", "global_y/m", "", 18);


            Plot2d.showThread();
        } else { //z
            List<Point2> collect = ParticleRunner.runGetPoint3WithDistance(p, cct1, trajectory300.getLength(), MM)
                    .stream()
                    .map(ParticleRunner.Point3WithDistance::getDistanceWithZ)
                    .map(pp -> Point2.convert(pp, 1, 1 / MM))
                    .collect(Collectors.toList());

            Plot2d.plot2(collect, Plot2d.RED_LINE);

            Plot2d.grid();

            Plot2d.info("s/m", "y/mm", "", 18);

            Plot2d.showThread();

            Logger.getLogger().info("p.position.z = " + p.position.z);


            {
                RunningParticle ipEnd =
                        ParticleFactory.createIdealProtonAtTrajectory250MeV(trajectory300, trajectory300.getLength());

                PhaseSpaceParticle phaseSpaceParticle = PhaseSpaceParticles.phaseSpaceParticleFromRunningParticle(ipEnd, ipEnd.computeNaturalCoordinateSystem(), p);

                double x = phaseSpaceParticle.getX();
                double y = phaseSpaceParticle.getY();

                Logger.getLogger().info("x = " + x);
                Logger.getLogger().info("y = " + y);
            }

        }


//        Plot3d.plot3(run, Plot2d.RED_LINE);
//
//
//        Plot3d.setCenter(trajectory300.pointAtEnd().toPoint3(),trajectory300.getLength());
//        Plot3d.showThread();


    }

    @run(value = 146, code = "")
    public void 左手侧磁场分析() {
        agCctIInner = 0;
        agCctIOuter = 0;

        dipoleCctA1Inner = Math.pow(dipoleCctSmallRInner, 2) * 0.217;
        dipoleCctA1Outer = -Math.pow(dipoleCctSmallROuter, 2) * 0.217;

        //    private double dipoleCctA1Inner = Math.pow(dipoleCctSmallRInner, 2) * 0.225;
        //    private double dipoleCctA1Outer = -Math.pow(dipoleCctSmallROuter, 2) * 0.225;

        Cct cct1 = getCct1();


        Trajectory trajectory300 = TrajectoryFactory.setStartingPoint(Point2.origin())
                .setStraightLine(DL1, Vector2.xDirect())
                .addArcLine(trajectoryBigR, false, dipoleCctAngle)
                .addStraitLine(3.00);

        List<Point2> leftHandMagnetAlongTrajectory = cct1.leftHandMagnetAlongTrajectory(trajectory300, MM);


        Plot2d.plot2(leftHandMagnetAlongTrajectory);

        Plot2d.showThread();
    }

    @run(value = 150, code = "A0423-1606-057")
    public void 调整电流使得积分场和理论值一致() {
        /**
         * 理论值 0.9551 Tm
         */

        double target = 22.5 / 180 * Math.PI * 0.75 * 2.4321282996 / 0.75;

        Logger.getLogger().info("target = " + target);


        //     * @param function 函数 y = f(x)
        //     * @param interval 区间
        //     * @param maxEval  最大迭代次数
        //     * @return 零点

        double zeroPoint = UniEquationSolver.findZeroPoint(x -> {
                    double v = 积分场VS电流(x) - target;

                    Logger.getLogger().info("x = " + x);

                    Logger.getLogger().info("v = " + v);

                    return v;

                },
                Point2.create(9898.121444999295 * 0.95, 9898.121444999295 * 1.05),
                100
        );

        // 9898.121357425029

        //    private double dipoleCctIInner = 9.898121356964111 * kA; // 求解获得
        //    private double dipoleCctIOuter = -dipoleCctIInner;

        Logger.getLogger().info("zeroPoint = " + zeroPoint);


    }

    @run(value = 166, code = "")
    public void 中轴线积分场计算2() {
        agCctIInner = 0;
        agCctIOuter = 0;

        dipoleCctA1Inner = Math.pow(dipoleCctSmallRInner, 2) * 0.217;
        dipoleCctA1Outer = -Math.pow(dipoleCctSmallROuter, 2) * 0.217;

        dipoleCctIInner = 9919.082986140875;
        dipoleCctIOuter = -dipoleCctIInner;

        Cct cct1 = getCct1();


        Trajectory trajectory300 = TrajectoryFactory.setStartingPoint(Point2.origin())
                .setStraightLine(DL1, Vector2.xDirect())
                .addArcLine(trajectoryBigR, false, dipoleCctAngle)
                .addStraitLine(3.00);


//        int part = 100000;
        int part = (int) (trajectory300.getLength() / MM) * 10;

        double[] linspace = BaseUtils.Python.linspace(0, trajectory300.getLength(), part);
        final double distance = linspace[1] - linspace[0];


        double sum = Arrays.stream(linspace)
                .limit(linspace.length - 1)
                .parallel()
                .map(s -> {
                    double next = s + distance;

                    Point2 sPoint = trajectory300.pointAt(s);
                    Point2 nextPoint = trajectory300.pointAt(next);


                    Point2 midPoint = Point2.midPoint(sPoint, nextPoint);

                    return cct1.magnetAt(midPoint.toPoint3()).z;
                })
                .map(m -> -m * distance)
                .sum();

        Logger.getLogger().info("sum = " + sum);
    }

    public double 积分场VS电流(double I) {

        // 电流
        dipoleCctIInner = I; // 求解获得
        dipoleCctIOuter = -dipoleCctIInner;

        agCctIInner = 0;
        agCctIOuter = 0;

        dipoleCctA1Inner = Math.pow(dipoleCctSmallRInner, 2) * 0.217;
        dipoleCctA1Outer = -Math.pow(dipoleCctSmallROuter, 2) * 0.217;

        //    private double dipoleCctA1Inner = Math.pow(dipoleCctSmallRInner, 2) * 0.225;
        //    private double dipoleCctA1Outer = -Math.pow(dipoleCctSmallROuter, 2) * 0.225;


        Cct cct1 = getCct1();


        Trajectory trajectory300 = TrajectoryFactory.setStartingPoint(Point2.origin())
                .setStraightLine(DL1, Vector2.xDirect())
                .addArcLine(trajectoryBigR, false, dipoleCctAngle)
                .addStraitLine(3.00);


//        List<Point2> list = cct1.magnetBzAlongTrajectory(trajectory300, MM);
//
//        Plot2d.plot2(list);
//
//        Plot2d.showThread();

//        int part = 100000;
        int part = (int) (trajectory300.getLength() / MM) * 10;

        double[] linspace = BaseUtils.Python.linspace(0, trajectory300.getLength(), part);
        final double distance = linspace[1] - linspace[0];


        double sum = Arrays.stream(linspace)
                .limit(linspace.length - 1)
                .parallel()
                .map(s -> {
                    double next = s + distance;

                    Point2 sPoint = trajectory300.pointAt(s);
                    Point2 nextPoint = trajectory300.pointAt(next);


                    Point2 midPoint = Point2.midPoint(sPoint, nextPoint);

                    return cct1.magnetAt(midPoint.toPoint3()).z;
                })
                .map(m -> -m * distance)
                .sum();

        Logger.getLogger().info("sum = " + sum);

        return sum;
    }

    @run(value = 1700)
    public void 积分场均匀度() {
        agCctIInner = 0;
        agCctIOuter = 0;

        dipoleCctA1Inner = Math.pow(dipoleCctSmallRInner, 2) * 0.217;
        dipoleCctA1Outer = -Math.pow(dipoleCctSmallROuter, 2) * 0.217;

        dipoleCctIInner = 9919.082986140875;
        dipoleCctIOuter = -dipoleCctIInner;

        Cct cct1 = getCct1();


        Trajectory trajectory300 = TrajectoryFactory.setStartingPoint(Point2.origin())
                .setStraightLine(DL1, Vector2.xDirect())
                .addArcLine(trajectoryBigR, false, dipoleCctAngle)
                .addStraitLine(3.00);

        Point2 mid = trajectory300.pointAt(DL1 + dipoleCctBigR * BaseUtils.Converter.angleToRadian(dipoleCctAngle / 2));
        Point2 rightHandSidePoint = trajectory300.rightHandSidePoint(DL1 + dipoleCctBigR * BaseUtils.Converter.angleToRadian(dipoleCctAngle / 2), 1);

        Vector3 right = Vector2.from(mid).to(rightHandSidePoint).toVector3().changeLengthSelf(1);


        List<Point2> collect = BaseUtils.Python.linspaceStream(-40 * MM, 40 * MM, 10)
                .mapToObj(dispalce -> {


                    int part = (int) (trajectory300.getLength() / MM) * 10;

                    double[] linspace = BaseUtils.Python.linspace(0, trajectory300.getLength(), part);
                    final double distance = linspace[1] - linspace[0];


                    double sum = Arrays.stream(linspace)
                            .limit(linspace.length - 1)
                            .parallel()
                            .map(s -> {
                                double next = s + distance;

                                Point2 sPoint = trajectory300.pointAt(s);
                                Point2 nextPoint = trajectory300.pointAt(next);


                                Point2 midPoint = Point2.midPoint(sPoint, nextPoint);

                                return cct1.magnetAt(midPoint.toPoint3().moveSelf(right.dot(dispalce))).z;
                            })
                            .map(m -> -m * distance)
                            .sum();

                    Logger.getLogger().info("sum = " + sum);


                    return Point2.create(dispalce / MM, sum);

                }).collect(Collectors.toList());

        double[] fit1 = PolynomialFitter.build(5).fit(collect);

        Logger.getLogger().info("fit1 = " + Arrays.toString(fit1));

        Plot2d.plot2(collect, Plot2d.BLACK_POINT);

        Plot2d.plot2(MathFunction.createPolynomialFunction(fit1), Point2.create(-45, 45), 100, Plot2d.RED_LINE);

        Plot2d.info("d/mm", "B_int/Tm", "", 18);

        Plot2d.showThread();
    }

    @run(value = -200, code = "A0429-2217-000")
    public void 遗传算法求解最佳DIPOLE() {
        Trajectory trajectory300 = TrajectoryFactory.setStartingPoint(Point2.origin())
                .setStraightLine(DL1, Vector2.xDirect())
                .addArcLine(trajectoryBigR, false, dipoleCctAngle)
                .addStraitLine(3.00);

        //dipoleCctIInner = I; // 求解获得
        //dipoleCctIOuter = -dipoleCctIInner;

        agCctIInner = 0;
        agCctIOuter = 0;

        //dipoleCctA1Inner = Math.pow(dipoleCctSmallRInner, 2) * 0.217;
        //dipoleCctA1Outer = -Math.pow(dipoleCctSmallROuter, 2) * 0.217;

        //                    dipoleCctA2Inner = Math.pow(dipoleCctSmallRInner, 3) * k;
        //                    dipoleCctA2Outer = -Math.pow(dipoleCctSmallROuter, 3) * k;

//        dipoleCctA0Inner
//        dipoleCctA0Outer


        //     * @param populationSize  种群大小
        //     * @param maxGeneration   最大代数
        //     * @param keepRate        种群中最优个体，有多少直接进入下一代，比例
        //     * @param crossRate       杂交比例
        //     * @param mutateRate      变异比例
        //     * @param fitnessFunction 适应性函数 / 目标函数
        //     * @param genes           基因 / 变量
        GeneticAlgorithm.solve(
                100, 100, 0.2, 1, 1,
                X -> {
                    double I = X[0];
                    double a0k = X[1];
                    double a1k = X[2];
                    double a2k = X[3];

                    dipoleCctIInner = I;
                    dipoleCctIOuter = -dipoleCctIInner;

                    dipoleCctA0Inner = -dipoleCctSmallRInner * Math.sqrt(3) * a0k;
                    dipoleCctA0Outer = dipoleCctSmallROuter * Math.sqrt(3) * a0k;

                    dipoleCctA1Inner = Math.pow(dipoleCctSmallRInner, 2) * a1k;
                    dipoleCctA1Outer = -Math.pow(dipoleCctSmallROuter, 2) * a1k;

                    dipoleCctA2Inner = Math.pow(dipoleCctSmallRInner, 3) * a2k;
                    dipoleCctA2Outer = -Math.pow(dipoleCctSmallROuter, 3) * a2k;

                    Cct cct1 = getCct1();

                    RunningParticle ip = ParticleFactory.createIdealProtonAtTrajectory250MeV(trajectory300);

                    List<Point3> run = ParticleRunner.run(ip, cct1, trajectoryBigR, MM);


                    return 0;
                },
                Gene.create("I", 10000 * 90 * PRESENT, 10000 * 110 * PRESENT, 1000 * 1000),
                Gene.create("a0k", 90 * PRESENT, 110 * PRESENT, 1000 * 1000),
                Gene.create("a1k", 0, 0.4, 10 * 1000),
                Gene.create("a2k", -1, 1, 10 * 1000)
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

        //        Point2 center = Arcs.center(
//                trajectory.pointAt(DL1 + CCT_LENGTH + BETWEEN_CCT225),
//                trajectory.pointAt(DL1 + CCT_LENGTH + BETWEEN_CCT225 + CCT_LENGTH / 2),
//                trajectory.pointAt(DL1 + CCT_LENGTH + BETWEEN_CCT225 + CCT_LENGTH)
//        );
//
//
//        CctFactory.Cct cct2 = CctFactory.positionInXYPlane(cct,center,BaseUtils.Converter.angleToRadian(-90+22.5));

        return CctFactory.positionInXYPlane(cct, Point2.create(DL1, trajectoryBigR), BaseUtils.Converter.angleToRadian(-90));

    }

    private Cct getCct2() {
        Trajectory trajectory = getTrajectory();

        Cct dipoleCct = createDipoleCct();
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
