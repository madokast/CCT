package cn.edu.hust.zrx.cct.study.超导机架AGCCT单独建模;

import cn.edu.hust.zrx.cct.Logger;
import cn.edu.hust.zrx.cct.base.BaseUtils;
import cn.edu.hust.zrx.cct.advanced.UniEquationSolver;
import cn.edu.hust.zrx.cct.base.cct.Cct;
import cn.edu.hust.zrx.cct.base.cct.CctFactory;
import cn.edu.hust.zrx.cct.base.cct.CctLine2;
import cn.edu.hust.zrx.cct.base.cct.SoleLayerCct;
import cn.edu.hust.zrx.cct.base.line.Trajectory;
import cn.edu.hust.zrx.cct.base.line.TrajectoryFactory;
import cn.edu.hust.zrx.cct.base.particle.ParticleFactory;
import cn.edu.hust.zrx.cct.base.particle.ParticleRunner;
import cn.edu.hust.zrx.cct.base.particle.RunningParticle;
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
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Description
 * 22.5度 AGCCT建模
 * <p>
 * <p>
 * 层间距：3 mm
 * 二极CCT 厚度 6 mm
 * 四级CCT 厚度 7 mm
 * 文献来源：？？？
 * 匝间距离：控制在5mm左右就行
 * <p>
 * dipole CCT 外层
 * -------------------------------- <-- 55
 * |||||||||||||||||||||||||||||||| }-- 6  -- mid = 52
 * -------------------------------- <-- 46 + 3 = 49
 * <p>
 * dipole CCT 内层
 * -------------------------------- <-- 46
 * |||||||||||||||||||||||||||||||| }-- 6mm -- mid = 43
 * -------------------------------- <-- 37 + 3 = 40
 * <p>
 * AG CCT 外层
 * -------------------------------- <-- 37
 * |||||||||||||||||||||||||||||||| }-- 7mm  -- mid = 33.5
 * -------------------------------- <-- 27mm + 3mm = 30
 * <p>
 * AG CCT 内层
 * -------------------------------- <-- 27 mm
 * |||||||||||||||||||||||||||||||| }-- 7 mm  -- mid = 23.5
 * -------------------------------- <-- 20 mm
 * <p>
 * 束流通道
 * ==================>>>>>>>>>>>
 *
 *
 * <p>
 * Data
 * 20:40
 *
 * @author zrx
 * @version 1.0
 */
@SuppressWarnings("all")
public class AGCCT225 {
    private final double bigR = 0.75;
    //孔径
    private final double agCctSmallRInner = 23.5 * MM;
    private final double agCctSmallROuter = 33.5 * MM;
    private final double dipoleCctSmallRInner = 43 * MM;
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
    private final int dipoleCctWindingNumber = 59;
    private final int agCctWindingNumber0 = 23;
    private final int agCctWindingNumber1 = 34; // sum = 57
    // 电流
    private final double dipoleCctIInner = 9.898121356964111 * kA; // 求解获得
    private final double dipoleCctIOuter = -dipoleCctIInner;
    private final double agCctIInner = 5000;
    private final double agCctIOuter = -agCctIInner;
    // CCT每匝分段
    private final int numberPerWinding = 360;

        @run
    //但是可惜 似乎粒子偏离了中平面？ 2020年2月12日
    public void 解方程找最佳电流() {
        Function<Double, Double> function = I -> {
            Cct dipoleCct = CctFactory.createDipoleCct(
                    dipoleCctSmallRInner, dipoleCctSmallROuter, bigR,
                    dipoleCctAngle, dipoleCctWindingNumber,
                    dipoleCctA0Inner, dipoleCctA1Inner, dipoleCctA2Inner, I * kA,
                    dipoleCctA0Outer, dipoleCctA1Outer, dipoleCctA2Outer, -I * kA,
                    numberPerWinding
            );

            Trajectory trajectory = getTrajectory();
            RunningParticle idealProtonAtTrajectory250MeV =
                    ParticleFactory.createIdealProtonAtTrajectory250MeV(trajectory);
            ParticleRunner.run(idealProtonAtTrajectory250MeV, dipoleCct, trajectory.getLength(), 1 * MM);
            Vector3 velocityLast = idealProtonAtTrajectory250MeV.getVelocity();

            Vector3 directEnd = trajectory.directAtEnd().toVector3();
            double angleIncluding = Vector3.angleIncluding(velocityLast, directEnd);
            Logger.getLogger().info("I = " + I);
            Logger.getLogger().info("angleIncluding = " + angleIncluding);

            directEnd.normalSelf();
            velocityLast.normalSelf();

            double sub = directEnd.x - velocityLast.x;
            Logger.getLogger().info("sub = " + sub);

            return sub;
        };

        double zeroPoint = UniEquationSolver.findZeroPoint(function, Point2.create(9, 10), 5000);

        Logger.getLogger().info("zeroPoint = " + zeroPoint);


    }

//        @run
    public void 中平面粒子轨迹() {
        Cct dipoleCct = creatDipoleCct();
        Trajectory trajectory = getTrajectory();

        RunningParticle idealProtonAtTrajectory250MeV =
                ParticleFactory.createIdealProtonAtTrajectory250MeV(trajectory);

        List<Point2> distanceWithZList = ParticleRunner.runGetPoint3WithDistance(
                idealProtonAtTrajectory250MeV, dipoleCct, trajectory.getLength(), 1 * MM)
                .stream()
                .map(ParticleRunner.Point3WithDistance::getDistanceWithZ)
                .collect(Collectors.toList());

        Plot2d.plot2(distanceWithZList);
        Logger.getLogger().info("trajectory.couplingPointLocation() = " + trajectory.couplingPointLocation());
        Plot2d.plotYLines(trajectory.couplingPointLocation(), distanceWithZList);//这包装无敌了
        Plot2d.yLabel("偏离中平面/m\n");
        Plot2d.xLabel("粒子运动距离/m\n");
        Plot2d.show();
    }


    //    @run
    public void 简单磁场分析() {
        Trajectory trajectory = getTrajectory();
        trajectory.plot3d(1 * MM, Plot2d.BLACK_LINE);
        Cct dipoleCct = creatDipoleCct();
        dipoleCct.plot3(Plot2d.YELLOW_LINE, Plot2d.RED_LINE);

        List<Point2> magnetBzAlongTrajectory = dipoleCct.magnetBzAlongTrajectory(trajectory, 1 * MM);
        Plot2d.plot2(magnetBzAlongTrajectory);

        Plot2d.plot2(
                dipoleCct.get(0).magnetBzAlongTrajectory(trajectory, 1 * MM), Plot2d.YELLOW_LINE
        );
        Plot2d.plot2(
                dipoleCct.get(1).magnetBzAlongTrajectory(trajectory, 1 * MM), Plot2d.RED_LINE
        );

        //入口处三维
        Vector3 magnetAtEnter = dipoleCct.magnetAt(trajectory.pointAt(1).toPoint3());
        Logger.getLogger().info("入口处三维");
        Logger.getLogger().info("magnetAtEnter = " + magnetAtEnter);

        //全程非z方向磁场大小
        if (false) {
            Logger.getLogger().info("全程非z方向磁场大小");
            List<Point2> BModExceptZAlongTrajectory = BaseUtils
                    .Python
                    .linspaceStream(0, trajectory.getLength(), 10000)
                    .parallel()
                    .mapToObj(s -> {
                        Point3 pointAtS = trajectory.pointAt(s).toPoint3();
                        Vector3 magnetAtS = dipoleCct.magnetAt(pointAtS);
                        double BModExceptZ = Point2.create(magnetAtS.x, magnetAtS.y).toVector2().length();
                        return Point2.create(s, BModExceptZ);
                    }).sorted(Point2::compareInX).collect(Collectors.toList());

            Plot2d.plot2(BModExceptZAlongTrajectory, Plot2d.YELLOW_LINE);
        }

        //计算四极场
        if (false) {
            Logger.getLogger().info("计算四极场");
            Plot2d.plot2(
                    dipoleCct.magnetGradientAlongTrajectoryFast(
                            trajectory, 1 * MM, 1 * MM)
            );
        }

        //计算倾角
        Point3 p0 = dipoleCct.get(0).getWindings().get(0);
        Point3 p1 = dipoleCct.get(0).getWindings().get(1);
        Vector3 t = Vector3.subtract(p1, p0);
        Vector3 z = Vector3.getYDirect();
        Vector3 theta = Vector3.getZDirect();
        double tanAlpha = (Vector3.dot(t, theta)) / (Vector3.dot(t, z));
        double alphaR = Math.atan(tanAlpha);
        double alpha = BaseUtils.Converter.radianToAngle(alphaR);
        Logger.getLogger().info("倾角1 = " + alpha);

        p0 = dipoleCct.get(1).getWindings().get(0);
        p1 = dipoleCct.get(1).getWindings().get(1);
        t = Vector3.subtract(p1, p0);
        z = Vector3.getYDirect();
        theta = Vector3.getZDirect();
        tanAlpha = (Vector3.dot(t, theta)) / (Vector3.dot(t, z));
        alphaR = Math.atan(tanAlpha);
        alpha = BaseUtils.Converter.radianToAngle(alphaR);
        Logger.getLogger().info("倾角2 = " + alpha);

        //粒子运动
        RunningParticle idealProtonAtTrajectory250MeV =
                ParticleFactory.createIdealProtonAtTrajectory250MeV(trajectory);
        List<Point3> traj = ParticleRunner.run(idealProtonAtTrajectory250MeV, dipoleCct, trajectory.getLength(), 1 * MM);
        Plot3d.plot3(traj, Plot2d.BLUE_LINE);

        //---------------plot-------------------
        Logger.getLogger().info("处理完毕，开始绘图");
        Plot3d.setCube(bigR * 1.1);
        Plot3d.showThread();
        Plot2d.plotYLine(1.0, 0.5, -3.5, Plot2d.GREY_DASH);
        Plot2d.plotYLine(1.294524311274043, 0.5, -3.5, Plot2d.GREY_DASH);
        Plot2d.xLabel("s/m");
        Plot2d.yLabel("二极场/T");
        Plot2d.showThread();
    }

    //    @run
    public void 轨道上右手侧磁场() {
        Cct cct = creatDipoleCct();
        Trajectory trajectory = getTrajectory();

//        List<Point2> Bx = cct.magnetAlongTrajectory(trajectory, 1 * MM)
//                .stream()
//                .map(CctFactory.MagnetAble.Point3WithDistance::getDistanceWithX)
//                .collect(Collectors.toList());
//
//        List<Point2> By = cct.magnetAlongTrajectory(trajectory, 1 * MM)
//                .stream()
//                .map(CctFactory.MagnetAble.Point3WithDistance::getDistanceWithY)
//                .collect(Collectors.toList());
//
//        Plot2d.plot2(Bx,Plot2d.YELLOW_LINE);
//        Plot2d.plot2(By,Plot2d.RED_LINE);

        List<Point2> B垂直于束流方向和z方向 = cct.magnetAlongTrajectory(trajectory, 1 * MM)
                .stream()
                .map(point3WithDistance -> {
                    double distance = point3WithDistance.getDistance();
                    Vector3 B = point3WithDistance.getPoint3().toVector3();
                    Vector3 rightHand = trajectory.directAt(distance).toVector3().cross(Vector3.getZDirect());
                    return Point2.create(distance, B.dot(rightHand));
                })
                .collect(Collectors.toList());

        Plot2d.plot2(B垂直于束流方向和z方向, Plot2d.BLACK_LINE);
        Plot2d.plotYLine(1, B垂直于束流方向和z方向);
        Plot2d.plotYLine(1.294524311274043, B垂直于束流方向和z方向);

        Plot2d.xLabel("s/m");
        Plot2d.yLabel("轨道上右手侧磁场/T");


        Plot2d.show();
    }

    //    @run
    public void 轨道上右手侧磁场_两层CCT分别绘图() {
        Trajectory trajectory = getTrajectory();
        Cct cct = creatDipoleCct();
        SoleLayerCct innerCct = cct.get(0);
        SoleLayerCct outerCct = cct.get(1);

        List<Point2> innerCctB垂直于束流方向和z方向 = innerCct.magnetAlongTrajectory(trajectory, 1 * MM)
                .stream()
                .map(point3WithDistance -> {
                    double distance = point3WithDistance.getDistance();
                    Vector3 B = point3WithDistance.getPoint3().toVector3();
                    Vector3 rightHand = trajectory.directAt(distance).toVector3().cross(Vector3.getZDirect());
                    return Point2.create(distance, B.dot(rightHand));
                })
                .collect(Collectors.toList());

        List<Point2> outerCctB垂直于束流方向和z方向 = outerCct.magnetAlongTrajectory(trajectory, 1 * MM)
                .stream()
                .map(point3WithDistance -> {
                    double distance = point3WithDistance.getDistance();
                    Vector3 B = point3WithDistance.getPoint3().toVector3();
                    Vector3 rightHand = trajectory.directAt(distance).toVector3().cross(Vector3.getZDirect());
                    return Point2.create(distance, B.dot(rightHand));
                })
                .collect(Collectors.toList());

        Plot2d.plot2(innerCctB垂直于束流方向和z方向, Plot2d.YELLOW_LINE);
        Plot2d.plot2(outerCctB垂直于束流方向和z方向, Plot2d.RED_LINE);
        Plot2d.plotYLines(trajectory.couplingPointLocation(),
                innerCctB垂直于束流方向和z方向, outerCctB垂直于束流方向和z方向);
        Plot2d.xLabel("s/m");
        Plot2d.yLabel("轨道上右手侧磁场/T");
        Plot2d.title("黄线-内侧CCT，红色-外侧CCT");
        Plot2d.showThread();

        trajectory.plot3d();
        cct.plot3(Plot2d.YELLOW_LINE, Plot2d.RED_LINE);
        Plot3d.setCube(0.8);
        Plot3d.showThread();
    }

    //    @run
    public void CCT起始theta对磁场的影响_内层() {
        Trajectory trajectory = getTrajectory();
        final int times = 10;
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


        List<List<Point2>> listList = BaseUtils.Python.linspaceStream(0, 360, times)
                .limit(times - 1)
//                .parallel()
                .mapToObj(theta -> {
                    theta = BaseUtils.Converter.angleToRadian(theta);
                    //底层代码。代码封装一时好，深入底层要哭了
                    CctLine2 cctLine2 = new CctLine2(dipoleCctSmallRInner, bigR, dipoleCctAngle, dipoleCctWindingNumber,
                            dipoleCctA0Inner, dipoleCctA1Inner, dipoleCctA2Inner, theta, 0.0, true);
                    SoleLayerCct innerCct =
                            new SoleLayerCct(cctLine2.disperseToPoint3(numberPerWinding), dipoleCctIInner);
                    List<Point2> inner右手侧 = innerCct.magnetAlongTrajectory(trajectory, 1 * MM)
                            .stream()
                            .map(point3WithDistance -> {
                                double distance = point3WithDistance.getDistance();
                                Vector3 B = point3WithDistance.getPoint3().toVector3();
                                Vector3 rightHand = trajectory.directAt(distance).toVector3().cross(Vector3.getZDirect());
                                return Point2.create(distance, B.dot(rightHand));
                            })
                            .collect(Collectors.toList());

                    String plot2Des = switcher.getAndSwitch();

                    Plot2d.plot2(inner右手侧, plot2Des);
//                    innerCct.plot3(Plot2d.YELLOW_LINE);

                    theta = BaseUtils.Converter.radianToAngle(theta);
                    Logger.getLogger().info("theta={}, plot2Des={}", theta, plot2Des);

                    return inner右手侧;
                }).collect(Collectors.toList());


        List<Point2> lists = BaseUtils.ListUtils.listListToList(listList);

        Plot2d.plotYLines(trajectory.couplingPointLocation(), lists);
        Plot2d.xLabel("s/m");
        Plot2d.yLabel("轨道上右手侧磁场/T");
        Plot2d.showThread();

//        Plot3d.setCube(0.8);
//        Plot3d.setCenter(Point3.create(bigR,0,0),bigR*1.1);
//        Plot3d.showThread();

    }

//    @run
    public void CCT起始theta对磁场的影响_外层() {

        Trajectory trajectory = getTrajectory();
        final int times = 10;
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


        List<List<Point2>> listList = BaseUtils.Python.linspaceStream(0, 360, times)
                .limit(times - 1)
//                .parallel()
                .mapToObj(theta -> {
                    theta = BaseUtils.Converter.angleToRadian(theta);
                    //底层代码。代码封装一时好，深入底层要哭了
                    CctLine2 cctLine2 = new CctLine2(dipoleCctSmallROuter, bigR, dipoleCctAngle, dipoleCctWindingNumber,
                            dipoleCctA0Outer, dipoleCctA1Outer, dipoleCctA2Outer, theta, 0.0, true);
                    SoleLayerCct innerCct =
                            new SoleLayerCct(cctLine2.disperseToPoint3(numberPerWinding), dipoleCctIOuter);
                    List<Point2> inner右手侧 = innerCct.magnetAlongTrajectory(trajectory, 1 * MM)
                            .stream()
                            .map(point3WithDistance -> {
                                double distance = point3WithDistance.getDistance();
                                Vector3 B = point3WithDistance.getPoint3().toVector3();
                                Vector3 rightHand = trajectory.directAt(distance).toVector3().cross(Vector3.getZDirect());
                                return Point2.create(distance, B.dot(rightHand));
                            })
                            .collect(Collectors.toList());

                    String plot2Des = switcher.getAndSwitch();

                    //inner右手侧
                    Plot2d.plot2(inner右手侧, plot2Des);


                    theta = BaseUtils.Converter.radianToAngle(theta);
                    Logger.getLogger().info("theta={}, plot2Des={}", theta, plot2Des);

                    return inner右手侧;
                }).collect(Collectors.toList());


        List<Point2> lists = BaseUtils.ListUtils.listListToList(listList);

        Plot2d.plotYLines(trajectory.couplingPointLocation(), lists);
        Plot2d.xLabel("s/m");
        Plot2d.yLabel("轨道上右手侧磁场/T");
        Plot2d.showThread();

//        Plot3d.setCube(0.8);
//        Plot3d.setCenter(Point3.create(bigR,0,0),bigR*1.1);
//        Plot3d.showThread();

    }

    public void 根据theta对右手场影响随孔径增大而减小_看来需要增大孔径(){}

    //    @run
    public void 轨迹二维绘图() {
        Trajectory trajectory = getTrajectory();
        trajectory.plot2d(1 * MM, Plot2d.BLACK_LINE);
        Logger.getLogger().info("trajectory = " + trajectory.toString());
        Logger.getLogger().info("trajectory.detailInfo() = " + trajectory.detailInfo());
        Plot2d.setCube(2);
        Plot2d.show();
    }

    public Trajectory getTrajectory() {
        return TrajectoryFactory.setStartingPoint(bigR, -1)
                .setStraightLine(1, Vector2.yDirect())
                .addArcLine(bigR, false, dipoleCctAngle)
                .addStraitLine(1.0);
    }

    public Cct creatDipoleCct() {
        return CctFactory.createDipoleCct(
                dipoleCctSmallRInner, dipoleCctSmallROuter, bigR,
                dipoleCctAngle, dipoleCctWindingNumber,
                dipoleCctA0Inner, dipoleCctA1Inner, dipoleCctA2Inner, dipoleCctIInner,
                dipoleCctA0Outer, dipoleCctA1Outer, dipoleCctA2Outer, dipoleCctIOuter,
                numberPerWinding
        );
    }

    public Cct createAgCct() {
        return null;
    }


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
                .filter(method -> method.isAnnotationPresent(AGCCT225.run.class))
                .limit(1)
                .forEach(method -> {
                    Logger.getLogger().info("运行{}", method.toString());
                    try {
                        method.invoke(newInstance);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                });

    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    private static @interface run {
    }
}
