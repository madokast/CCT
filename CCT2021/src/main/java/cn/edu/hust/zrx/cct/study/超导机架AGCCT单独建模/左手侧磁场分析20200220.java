package cn.edu.hust.zrx.cct.study.超导机架AGCCT单独建模;

import cn.edu.hust.zrx.cct.Logger;
import cn.edu.hust.zrx.cct.base.BaseUtils;
import cn.edu.hust.zrx.cct.base.cct.CctFactory;
import cn.edu.hust.zrx.cct.base.line.Trajectory;
import cn.edu.hust.zrx.cct.base.line.TrajectoryFactory;
import cn.edu.hust.zrx.cct.base.point.Point2;
import cn.edu.hust.zrx.cct.base.point.Point3;
import cn.edu.hust.zrx.cct.base.python.Plot2d;
import cn.edu.hust.zrx.cct.base.python.Plot3d;
import cn.edu.hust.zrx.cct.base.vector.Vector2;
import cn.edu.hust.zrx.cct.base.vector.Vector3;
import cn.hutool.core.collection.CollUtil;

import java.awt.geom.Point2D;
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

/**
 * Description
 * 右手侧磁场分析20200220
 * 2020年2月20日
 * 发现在CCT的入口和出口位置，存在左手侧磁场
 * 这里进一步验证真伪
 * <p>
 * Data
 * 18:55
 *
 * @author zrx
 * @version 1.0
 */
@SuppressWarnings("all")
public class 左手侧磁场分析20200220 {
    @run
    public void 保持By不变时_分析r和左手侧磁场关系() {
        final double K = dipoleCctA0Inner / dipoleCctSmallRInner;
        List<String> legends = new ArrayList<>();

        ArrayList<Point2> data = BaseUtils.Python.linspaceStream(10, 90, switcher.getSize())
                .parallel()
                .mapToObj(smallR -> {
                    CctFactory.SoleLayerCct soleLaterCctInner = CctFactory.createSoleLaterCct(smallR * MM, bigR,
                            dipoleCctAngle, dipoleCctWindingNumber, K * (smallR * MM), 0.0,
                            0.0, dipoleCctIInner, numberPerWinding,
                            0.0, 0.0, true
                    );

                    List<Point2> leftHandMagnetAlongTrajectory =
                            soleLaterCctInner.leftHandMagnetAlongTrajectory(trajectory, MM);

                    return BaseUtils.Content.BiContent.create(smallR, leftHandMagnetAlongTrajectory);
                })
                .sorted(Comparator.comparingDouble(BaseUtils.Content.BiContent::getT1))
                .collect(Collectors.toList())//关闭并行流
                .stream()
                .peek(bi -> {
                    Double smallR = bi.getT1();
                    List<Point2> leftHandMagnetAlongTrajectory = bi.getT2();
                    String andSwitch = switcher.getAndSwitch();
                    String legend = switcherLegend.getAndSwitch();
                    legends.add(legend);

                    BaseUtils.Content.BiContent<Double, String> info =
                            BaseUtils.Content.BiContent.create(smallR, andSwitch);
                    Logger.getLogger().info(info.toStringWithInfo("smallR", "des"));

                    Plot2d.plot2(leftHandMagnetAlongTrajectory, andSwitch);
                })
                .map(BaseUtils.Content.BiContent::getT2)
                .collect(ArrayList<Point2>::new, ArrayList::addAll, ArrayList::addAll);

        Plot2d.plotYLines(trajectory.couplingPointLocation(), data);


        Plot2d.info("s/m", "左手侧磁场/T", "轨道上左手侧磁场分布(内层CCT)", 20);
        Plot2d.legend(20,legends.toArray(new String[0]));
        Plot2d.showThread();
    }

    //    @run
    public void 证明a0不变的情况下_By和孔径成反比() {
        List<Point2> data = BaseUtils.Python.linspaceStream(1 * MM, 150 * MM, 1000)
                .parallel()
                .mapToObj(smallR -> Point2.create(
                        smallR / MM,
                        CctFactory.createSoleLaterCct(smallR, bigR, dipoleCctAngle, dipoleCctWindingNumber,
                                dipoleCctA0Inner, dipoleCctA1Inner, dipoleCctA2Inner, dipoleCctIInner, numberPerWinding)
                                .magnetAt(trajectory.pointAt(trajectory.getLength() / 2).toPoint3())
                                .z
                        )
                ).sorted(Point2::compareInX)
                .collect(Collectors.toList());

        double avg = data.stream().mapToDouble(p -> p.x * p.y).average().getAsDouble();
        Logger.getLogger().info("avg = " + avg);

        List<Point2> multi = BaseUtils.Python.linspaceStream(1 * MM, 150 * MM, 1000)
                .mapToObj(smallR -> Point2.create(smallR / MM, avg * MM / smallR))
                .collect(Collectors.toList());

        Plot2d.plot2(data, Plot2d.BLACK_LINE);
        Plot2d.plot2(multi, Plot2d.RED_DASH);

        Plot2d.info("二极CCT半孔径r/mm", "二极场(CCT中心处)/T", "", 20);

        Plot2d.showThread();

        createDipoleCct().get(0).plot3(Plot2d.YELLOW_LINE);
        Plot3d.setCube(0.8);
        Plot3d.showThread();
    }

    //    @run
    public void 存在对称性吗_存在的呢() {
        CctFactory.SoleLayerCct soleLaterCctInner = CctFactory.createSoleLaterCct(dipoleCctSmallRInner, bigR,
                dipoleCctAngle, dipoleCctWindingNumber, dipoleCctA0Inner, 0.0,
                0.0, dipoleCctIInner, numberPerWinding,
                0.0, 0.0, true
        );

        CctFactory.SoleLayerCct soleLaterCctOuter = CctFactory.createSoleLaterCct(
                /*这里外层的孔径也和内层相同*/dipoleCctSmallRInner, bigR,
                dipoleCctAngle, dipoleCctWindingNumber, /*这里外层的a0也和内层相同*/dipoleCctA0Inner, 0.0,
                0.0, dipoleCctIOuter, numberPerWinding,
                BaseUtils.Converter.angleToRadian(180), 0.0, true
        );

        //拿到了内外层CCT。他们的不同点：
        // a0 一正一负
        // 电流一正一负
        // 起始绕线方法 0 和 180 度

        //拿到左手侧磁场
        List<Point2> leftHandMagnetAlongTrajectoryInner =
                soleLaterCctInner.leftHandMagnetAlongTrajectory(trajectory, 1 * MM);
        List<Point2> leftHandMagnetAlongTrajectoryOuter =
                soleLaterCctOuter.leftHandMagnetAlongTrajectory(trajectory, 1 * MM);

        //画图
        Plot2d.plot2(leftHandMagnetAlongTrajectoryInner, Plot2d.RED_LINE);
        Plot2d.plot2(leftHandMagnetAlongTrajectoryOuter, Plot2d.YELLOW_LINE);

        List<Point2> sum = BaseUtils.StreamTools.combine(
                leftHandMagnetAlongTrajectoryInner.stream(),
                leftHandMagnetAlongTrajectoryOuter.stream()
        ).map(point2s -> Point2.create(point2s.get(0).x, point2s.get(0).y + point2s.get(1).y)
        ).collect(Collectors.toList());

        Plot2d.plot2(sum, Plot2d.BLACK_LINE);

        //画辅助线
        Plot2d.plotYLines(trajectory.couplingPointLocation(),
                leftHandMagnetAlongTrajectoryInner, leftHandMagnetAlongTrajectoryOuter);

        Plot2d.info("s/m", "左手侧磁场/T", "轨道上左手侧磁场分布(内层CCT)", 20);
        Plot2d.showThread();


//        soleLaterCctInner.plot3(Plot2d.RED_LINE);
//        soleLaterCctOuter.plot3(Plot2d.YELLOW_LINE);
//
//        trajectory.plot3d(1*MM);
//        Plot3d.setCube(0.8);
//        Plot3d.showThread();

    }

    //    @run
    public void 内层CCT起点绕线和左手侧磁场() {
        List<Point2> collect = BaseUtils.Python
                .linspaceStream(0, 320, switcher.getSize())
                .parallel()
                .map(BaseUtils.Converter::angleToRadian)
                .mapToObj(startPhi -> {
                    CctFactory.SoleLayerCct soleLaterCctInner = CctFactory.createSoleLaterCct(dipoleCctSmallRInner, bigR,
                            dipoleCctAngle, dipoleCctWindingNumber, dipoleCctA0Inner, dipoleCctA1Inner,
                            dipoleCctA2Inner, dipoleCctIInner, numberPerWinding,
                            startPhi, 0.0, true);
//                    soleLaterCctInner.plot3(Plot2d.YELLOW_LINE);

//                    List<Point2> point2s = soleLaterCctInner.magnetAlongTrajectory(trajectory, 1 * MM)
//                            .stream()
//                            .map(point3WithDistance -> {
//                                double distance = point3WithDistance.getDistance();
//                                Point3 point3 = point3WithDistance.getPoint3();
//
//                                Vector3 leftHandDirect = trajectory.directAt(distance)
//                                        .rotateSelf(BaseUtils.Converter.angleToRadian(90))
//                                        .toVector3()
//                                        .normalSelf();
//
//                                return Point2.create(distance, point3.toVector3().dot(leftHandDirect));
//                            }).collect(Collectors.toList());

                    //简单方法
                    List<Point2> point2s = soleLaterCctInner.leftHandMagnetAlongTrajectory(trajectory, 1 * MM);

                    return BaseUtils.Content.BiContent.create(startPhi, point2s);
                })
                .sorted(Comparator.comparing(BaseUtils.Content.BiContent::getT1))
                .collect(Collectors.toList())
                .stream()
                .peek(bi -> {
                    String andSwitch = switcher.getAndSwitch();
                    BaseUtils.Content.BiContent<Double, String> doubleSwitcherBiContent =
                            BaseUtils.Content.BiContent.create(BaseUtils.Converter.radianToAngle(bi.getT1()), andSwitch);
                    Logger.getLogger().info(doubleSwitcherBiContent.toStringWithInfo("startPhi", "des"));
                    Plot2d.plot2(bi.getT2(), andSwitch);
                })
                .map(BaseUtils.Content.BiContent::getT2)
                .collect(ArrayList::new, ArrayList::addAll, ArrayList::addAll);


        Plot2d.plotYLines(trajectory.couplingPointLocation(), collect);

        Plot2d.info("s/m", "左手侧磁场/T", "轨道上左手侧磁场分布(内层CCT)", 20);
        Plot2d.showThread();

//        trajectory.plot3d(1*MM);
//        Plot3d.setCube(0.8);
//        Plot3d.showThread();
    }

    //    @run
    public void 内层CCT孔径和左手侧磁场() {

        ArrayList<Point2> collect = BaseUtils.Python.linspaceStream(10 * MM, 90 * MM, switcher.getSize())
                .parallel()
                .mapToObj(smallR -> {

                    List<Point2> point2s = null;

                    //单CCT层方法
                    if (true) {
                        CctFactory.SoleLayerCct soleLaterCctInner = CctFactory.createSoleLaterCct(smallR, bigR,
                                dipoleCctAngle, dipoleCctWindingNumber, dipoleCctA0Inner, dipoleCctA1Inner,
                                dipoleCctA2Inner, dipoleCctIInner, numberPerWinding);
                        //Bz方向磁场
//                        point2s = soleLaterCctInner.magnetBzAlongTrajectory(trajectory, 1 * MM);

                        //左手侧磁场
                        point2s = soleLaterCctInner.magnetAlongTrajectory(trajectory, 1 * MM)
                                .stream()
                                .map(point3WithDistance -> {
                                    double distance = point3WithDistance.getDistance();
                                    Point3 point3 = point3WithDistance.getPoint3();

                                    Vector3 leftHandDirect = trajectory.directAt(distance)
                                            .rotateSelf(BaseUtils.Converter.angleToRadian(90))
                                            .toVector3()
                                            .normalSelf();

                                    return Point2.create(distance, point3.toVector3().dot(leftHandDirect));
                                }).collect(Collectors.toList());
                    }

                    //dipole双层法，但是外层电流设为零
                    //验证成功
                    if (false) {
                        dipoleCctIOuter = 0;
                        dipoleCctSmallRInner = smallR;
                        CctFactory.Cct dipoleCct = createDipoleCct();
                        point2s = dipoleCct.magnetBzAlongTrajectory(trajectory, 1 * MM);
                    }

                    return BaseUtils.Content.BiContent.create(smallR, point2s);
                }).sorted(Comparator.comparing(BaseUtils.Content.BiContent::getT1))
                .collect(Collectors.toList())
                .stream()
                .peek(bi -> {
                    String andSwitch = switcher.getAndSwitch();
                    BaseUtils.Content.BiContent<Double, String> doubleSwitcherBiContent =
                            BaseUtils.Content.BiContent.create(bi.getT1(), andSwitch);
                    Logger.getLogger().info(doubleSwitcherBiContent.toStringWithInfo("smallR", "des"));
                    Plot2d.plot2(bi.getT2(), andSwitch);
                    Logger.getLogger().info("bi.getT2().size() = " + bi.getT2().size());
                })
                .map(BaseUtils.Content.BiContent::getT2)
                .collect(ArrayList::new, ArrayList::addAll, ArrayList::addAll);

        Logger.getLogger().info("collect.size() = " + collect.size());


        Plot2d.plotYLines(trajectory.couplingPointLocation(), collect);

        Plot2d.info("s/m", "左手侧磁场/T", "轨道上左手侧磁场分布(内层CCT)", 20);
        Plot2d.showThread();
    }


    //    @run//2020年2月20日 验证成功
    public void 验证性分析() {
        Trajectory trajectory = getTrajectory();
        CctFactory.Cct dipoleCct = createDipoleCct();
        //        return CctFactory.createDipoleCct(
        //                dipoleCctSmallRInner, dipoleCctSmallROuter, bigR,
        //                dipoleCctAngle, dipoleCctWindingNumber,
        //                dipoleCctA0Inner, dipoleCctA1Inner, dipoleCctA2Inner, dipoleCctIInner,
        //                dipoleCctA0Outer, dipoleCctA1Outer, dipoleCctA2Outer, dipoleCctIOuter,
        //                numberPerWinding
        //        );

        //createDipoleCct
        List<Point2> magnetBzAlongTrajectory = dipoleCct.magnetBzAlongTrajectory(trajectory, 1 * MM);
        Plot2d.plot2(magnetBzAlongTrajectory, Plot2d.RED_LINE);

        //单层分开建模
        CctFactory.SoleLayerCct soleLaterCctInner = CctFactory.createSoleLaterCct(dipoleCctSmallRInner, bigR,
                dipoleCctAngle, dipoleCctWindingNumber, dipoleCctA0Inner, dipoleCctA1Inner,
                dipoleCctA2Inner, dipoleCctIInner, numberPerWinding);
        CctFactory.SoleLayerCct soleLaterCctOuter = CctFactory.createSoleLaterCct(dipoleCctSmallROuter, bigR,
                dipoleCctAngle, dipoleCctWindingNumber, dipoleCctA0Outer, dipoleCctA1Outer,
                dipoleCctA2Outer, dipoleCctIOuter, numberPerWinding);

        //分别计算磁场
        List<Point2> magnetBzAlongTrajectory分开算的 = BaseUtils.StreamTools.combine(
                soleLaterCctInner.magnetBzAlongTrajectory(trajectory, 1 * MM).stream(),
                soleLaterCctOuter.magnetBzAlongTrajectory(trajectory, 1 * MM).stream()
        ).map(point2s -> {
            Point2 p0 = point2s.get(0);
            Point2 p1 = point2s.get(1);
            return Point2.create(p0.x, p0.y + p1.y);
        }).collect(Collectors.toList());

        Plot2d.plot2(magnetBzAlongTrajectory分开算的, Plot2d.YELLOW_LINE);

        Plot2d.showThread();
    }


    public Trajectory getTrajectory() {
        return TrajectoryFactory.setStartingPoint(bigR, -1)
                .setStraightLine(1, Vector2.yDirect())
                .addArcLine(bigR, false, dipoleCctAngle)
                .addStraitLine(1.0);
    }

    public CctFactory.Cct createDipoleCct() {
        return CctFactory.createDipoleCct(
                dipoleCctSmallRInner, dipoleCctSmallROuter, bigR,
                dipoleCctAngle, dipoleCctWindingNumber,
                dipoleCctA0Inner, dipoleCctA1Inner, dipoleCctA2Inner, dipoleCctIInner,
                dipoleCctA0Outer, dipoleCctA1Outer, dipoleCctA2Outer, dipoleCctIOuter,
                numberPerWinding
        );
    }

    //--------------------------------参数-------------------------------------------

    Trajectory trajectory = getTrajectory();

    BaseUtils.Switcher<String> switcherLegend = BaseUtils.Switcher.creat(
            "BLACK_LINE",
            "YELLOW_LINE",
            "RED_LINE",
            "BLUE_LINE",
            "GREEN_LINE",
            "RED_DASH",
            "PINK_DASH",
            "BLUE_DASH",
            "BLACK_DASH"
    );

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

    private final double bigR = 0.75;
    //孔径
    private final double agCctSmallRInner = 23.5 * MM;
    private final double agCctSmallROuter = 33.5 * MM;
    private double dipoleCctSmallRInner = 43 * MM;
    private final double dipoleCctSmallROuter = 52 * MM;
    //角度
    private final double dipoleCctAngle = 22.5;
    private final double agCctAngle0 = 9.1;
    private final double agCctAngle1 = 13.4;
    // a0 a1 a2
    private final double dipoleCctA0Inner = -dipoleCctSmallRInner * Math.sqrt(3);
    private final double dipoleCctA0Outer = dipoleCctSmallROuter * Math.sqrt(3);
    private final double dipoleCctA1Inner = 0.0;
    private final double dipoleCctA1Outer = 0.0;
    private final double dipoleCctA2Inner = 0.0;
    private final double dipoleCctA2Outer = 0.0;

    private final double agCctA0Inner = 0.0;
    private final double agCctA0Outer = 0.0;
    private final double agCctA1Inner = 0.1;
    private final double agCctA1Outer = 0.1;
    private final double agCctA2Inner = 0.0;
    private final double agCctA2Outer = 0.0;
    // 匝数 5mm匝间距 则匝数 = 58.85
    private int dipoleCctWindingNumber = 59;
    private final int agCctWindingNumber0 = 23;
    private final int agCctWindingNumber1 = 34; // sum = 57
    // 电流
    private final double dipoleCctIInner = 9.898121356964111 * kA; // 求解获得
    private double dipoleCctIOuter = -dipoleCctIInner;
    private double agCctIInner = 5000;
    private final double agCctIOuter = -agCctIInner;
    // CCT每匝分段
    private final int numberPerWinding = 360;

    /*-----------------辅助量----------------------*/
    final static double MM = 1e-3;
    final static double kA = 1e3;

    public static void main(String[] args) throws Exception {
        StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[1];
        Logger.getLogger().info("{}", stackTraceElement.getClassName());
        Class<?> currentClass = Class.forName(stackTraceElement.getClassName());

        Constructor<?> constructor = currentClass.getConstructor(null);
        Object newInstance = constructor.newInstance();

        Method[] methods = currentClass.getMethods();
        Arrays.stream(methods)
                .filter(method -> method.isAnnotationPresent(左手侧磁场分析20200220.run.class))
                .limit(1)
                .forEach(method -> {
                    Logger.getLogger().info("运行{}", method.toString());
                    BaseUtils.Timer.printPeriodPerSecondCall(null);
                    try {
                        method.invoke(newInstance);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                    BaseUtils.Timer.printPeriodPerSecondCall(Logger.getLogger());
                });

    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    private static @interface run {
    }
}
