package cn.edu.hust.zrx.cct.study.Z验证性研究;

import cn.edu.hust.zrx.cct.Logger;
import cn.edu.hust.zrx.cct.base.BaseUtils;
import cn.edu.hust.zrx.cct.base.cct.Cct;
import cn.edu.hust.zrx.cct.base.cct.CctFactory;
import cn.edu.hust.zrx.cct.base.cct.SoleLayerCct;
import cn.edu.hust.zrx.cct.base.line.Trajectory;
import cn.edu.hust.zrx.cct.base.line.TrajectoryFactory;
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
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Description
 * A20200211CCT系数a0a1a2的研究
 * <p>
 * Data
 * 21:02
 *
 * @author zrx
 * @version 1.0
 */

@SuppressWarnings("all")
public class A20200211CCT系数a0a1a2的研究 {
    /**
     * 研究 CCT 方程中 a0 的意义
     * 单层CCT 参数如代码
     */
    //@run
    public void a系数a0研究() {
        final Trajectory trajectory = TrajectoryFactory.setStartingPoint(Point2.create(0.75, -1.0))
                .setStraightLine(1.0, Vector2.yDirect())
                .addArcLine(0.75, false, 67.5)
                .addStraitLine(1.0);

        // CCT 中点
        final Point3 midPoint = trajectory.pointAt(trajectory.getLength() / 2).toPoint3();

        final List<Point2> magnetAtMidPointBx = new ArrayList<>();
        final List<Point2> magnetAtMidPointBy = new ArrayList<>();
        final List<Point2> magnetAtMidPointBz = new ArrayList<>();

        // 研究 a0 = -0.2 ~ 0.2
        BaseUtils.Python.linspaceStream(-0.2, 0.2, 100).parallel()
                .forEach(a0 -> {
                    Cct dipoleCct = CctFactory.createDipoleCct(60 * MM, 75 * MM,
                            0.75, 67.5, 135,
                            a0, 0, 0, 18000,
                            0.1, 0, 0, 0,
                            360);
                    Vector3 magnetAtMidPoint = dipoleCct.magnetAt(midPoint);
                    magnetAtMidPointBx.add(Point2.create(a0, magnetAtMidPoint.x));
                    magnetAtMidPointBy.add(Point2.create(a0, magnetAtMidPoint.y));
                    magnetAtMidPointBz.add(Point2.create(a0, magnetAtMidPoint.z));
                });

        magnetAtMidPointBx.sort(Point2::compareInX);
        magnetAtMidPointBy.sort(Point2::compareInX);
        magnetAtMidPointBz.sort(Point2::compareInX);

        Plot2d.plot2(magnetAtMidPointBx, Plot2d.BLACK_LINE);
        Plot2d.plot2(magnetAtMidPointBy, Plot2d.YELLOW_LINE);
        Plot2d.plot2(magnetAtMidPointBz, Plot2d.RED_LINE);

        Plot2d.show();
    }

    /**
     * 内层 a0
     * 外层-a0
     * 电流相反
     */
//    @run
    public void a系数a0双层CCT() {
        final Trajectory trajectory = TrajectoryFactory.setStartingPoint(Point2.create(0.75, -1.0))
                .setStraightLine(1.0, Vector2.yDirect())
                .addArcLine(0.75, false, 67.5)
                .addStraitLine(1.0);

        // CCT 中点
        final Point3 midPoint = trajectory.pointAt(trajectory.getLength() / 2).toPoint3();

        final List<Point2> magnetAtMidPointBx = new ArrayList<>();
        final List<Point2> magnetAtMidPointBy = new ArrayList<>();
        final List<Point2> magnetAtMidPointBz = new ArrayList<>();

        // 研究 a0 = -0.2 ~ 0.2
        BaseUtils.Python.linspaceStream(-0.2, 0.2, 100).parallel()
                .forEach(a0 -> {
                    Cct dipoleCct = CctFactory.createDipoleCct(60 * MM, 75 * MM,
                            0.75, 67.5, 135,
                            a0, 0, 0, 18000,
                            -a0, 0, 0, -18000,
                            360);
                    Vector3 magnetAtMidPoint = dipoleCct.magnetAt(midPoint);
                    magnetAtMidPointBx.add(Point2.create(a0, magnetAtMidPoint.x));
                    magnetAtMidPointBy.add(Point2.create(a0, magnetAtMidPoint.y));
                    magnetAtMidPointBz.add(Point2.create(a0, magnetAtMidPoint.z));
                });

        magnetAtMidPointBx.sort(Point2::compareInX);
        magnetAtMidPointBy.sort(Point2::compareInX);
        magnetAtMidPointBz.sort(Point2::compareInX);

        Plot2d.plot2(magnetAtMidPointBx, Plot2d.BLACK_LINE);
        Plot2d.plot2(magnetAtMidPointBy, Plot2d.YELLOW_LINE);
        Plot2d.plot2(magnetAtMidPointBz, Plot2d.RED_LINE);

        Plot2d.show();
    }

    //    @run
    public void a系数a1单层研究() {
        final Trajectory trajectory = TrajectoryFactory.setStartingPoint(Point2.create(0.75, -1.0))
                .setStraightLine(1.0, Vector2.yDirect())
                .addArcLine(0.75, false, 67.5)
                .addStraitLine(1.0);

        // CCT 中点
        double midDistance = trajectory.getLength() / 2;
        final Point3 midPoint = trajectory.pointAt(midDistance).toPoint3();
        double goodFieldAreaWidth = 10 * MM;

        List<Point2> g = BaseUtils.Python.linspaceStream(-0.2, 0.2, 100)
                .parallel()
                .mapToObj(
                        a1 -> {
                            Cct dipoleCct = CctFactory.createDipoleCct(60 * MM, 75 * MM,
                                    0.75, 67.5, 135,
                                    0.0, a1, 0, 5000,
                                    0.0, 0, 0, 0.0,
                                    360);

                            double bzLeft = dipoleCct.magnetAt(
                                    trajectory.leftHandSidePoint(midDistance, goodFieldAreaWidth).toPoint3()).z;

                            double bzRight = dipoleCct.magnetAt(
                                    trajectory.rightHandSidePoint(midDistance, goodFieldAreaWidth).toPoint3()).z;

                            return Point2.create(a1, (bzLeft - bzRight) / (2 * goodFieldAreaWidth));

//                            return Point2.create(a1,dipoleCct.magnetAt(midPoint).z);
                        }
                ).collect(Collectors.toList());

        g.sort(Point2::compareInX);

        Plot2d.plot2(g);
        Plot2d.show();


//        trajectory.plot3d(1*MM,Plot2d.BLACK_LINE);
//        dipoleCct.plot3(Plot2d.RED_LINE);//这里会报error，不管
//        Plot3d.setCube(0.8);
//        Plot3d.showThread();
//
//        List<Point2> gradientAlongTrajectoryFast = dipoleCct.magnetGradientAlongTrajectoryFast(trajectory, 1 * MM, 10 * MM);
//
//        Plot2d.plot2(gradientAlongTrajectoryFast);
//        Plot2d.show();
    }

    //    @run
    public void a系数a1双层研究画图() {
        final Trajectory trajectory = TrajectoryFactory.setStartingPoint(Point2.create(0.75, -1.0))
                .setStraightLine(1.0, Vector2.yDirect())
                .addArcLine(0.75, false, 67.5)
                .addStraitLine(1.0);

        Cct dipoleCct = CctFactory.createDipoleCct(60 * MM, 75 * MM,
                0.75, 67.5, 135,
                0.0, 0.1, 0, 5000,
                0.0, -0.1, 0, -5000,
                360);

        trajectory.plot3d(1 * MM, Plot2d.BLACK_LINE);
        dipoleCct.plot3(Plot2d.RED_LINE, Plot2d.YELLOW_LINE);
        Plot3d.setCube(0.8);
        Plot3d.showThread();

    }

    //    @run
    public void a系数a1双层研究() {
        final Trajectory trajectory = TrajectoryFactory.setStartingPoint(Point2.create(0.75, -1.0))
                .setStraightLine(1.0, Vector2.yDirect())
                .addArcLine(0.75, false, 67.5)
                .addStraitLine(1.0);

        // CCT 中点
        double midDistance = trajectory.getLength() / 2;
        final Point3 midPoint = trajectory.pointAt(midDistance).toPoint3();
        double goodFieldAreaWidth = 10 * MM;

        List<Point2> g = BaseUtils.Python.linspaceStream(-0.2, 0.2, 100)
                .parallel()
                .mapToObj(
                        a1 -> {
                            Cct dipoleCct = CctFactory.createDipoleCct(60 * MM, 75 * MM,
                                    0.75, 67.5, 135,
                                    0.0, a1, 0, 5000,
                                    0.0, -a1, 0, -5000,
                                    360);

                            double bzLeft = dipoleCct.magnetAt(
                                    trajectory.leftHandSidePoint(midDistance, goodFieldAreaWidth).toPoint3()).z;

                            double bzRight = dipoleCct.magnetAt(
                                    trajectory.rightHandSidePoint(midDistance, goodFieldAreaWidth).toPoint3()).z;


                            return Point2.create(a1, (bzLeft - bzRight) / (2 * goodFieldAreaWidth));

//                            return Point2.create(a1,dipoleCct.magnetAt(midPoint).x);
                        }
                ).collect(Collectors.toList());

        g.sort(Point2::compareInX);

        Plot2d.plot2(g);
        Plot2d.show();
    }

//    @run
    public void a系数a1双层研究2() {
        final Trajectory trajectory = TrajectoryFactory.setStartingPoint(Point2.create(0.75, -1.0))
                .setStraightLine(1.0, Vector2.yDirect())
                .addArcLine(0.75, false, 67.5)
                .addStraitLine(1.0);

        // CCT 中点
        double midDistance = trajectory.getLength() / 2;
        final Point3 midPoint = trajectory.pointAt(midDistance).toPoint3();
        double goodFieldAreaWidth = 10 * MM;

        final List<Point2> innerGList = new ArrayList<>();
        final List<Point2> outerGList = new ArrayList<>();
        final List<Point2> totalGList = new ArrayList<>();

        BaseUtils.Python.linspaceStream(-0.2, 0.2, 100)
                .parallel()
                .forEach(
                        a1 -> {
                            Cct dipoleCct = CctFactory.createDipoleCct(60 * MM, 60 * MM,
                                    0.75, 67.5, 135,
                                    0.0, a1, 0, 5000,
                                    0.0, -a1, 0, -5000,
                                    360);

                            double totalG = dipoleCct.magnetGradientAtTrajectoryOfDistanceFast(
                                    trajectory, midDistance, goodFieldAreaWidth);

                            double innerG = dipoleCct.get(0).magnetGradientAtTrajectoryOfDistanceFast(
                                    trajectory, midDistance, goodFieldAreaWidth);

                            double outerG = dipoleCct.get(1).magnetGradientAtTrajectoryOfDistanceFast(
                                    trajectory, midDistance, goodFieldAreaWidth);

                            totalGList.add(Point2.create(a1,totalG));
                            outerGList.add(Point2.create(a1,outerG));
                            innerGList.add(Point2.create(a1,innerG));
                        }
                );

        totalGList.sort(Point2::compareInX);
        outerGList.sort(Point2::compareInX);
        innerGList.sort(Point2::compareInX);

        Plot2d.plot2(totalGList,Plot2d.BLACK_LINE);
        Plot2d.plot2(outerGList,Plot2d.RED_LINE);
        Plot2d.plot2(innerGList,Plot2d.YELLOW_LINE);

        Plot2d.show();
    }

//    @run
    public void 孔径对二极场CCT的影响(){
        final Trajectory trajectory = TrajectoryFactory.setStartingPoint(Point2.create(0.75, -1.0))
                .setStraightLine(1.0, Vector2.yDirect())
                .addArcLine(0.75, false, 67.5)
                .addStraitLine(1.0);

        // CCT 中点
        double midDistance = trajectory.getLength() / 2;
        final Point3 midPoint = trajectory.pointAt(midDistance).toPoint3();
        double goodFieldAreaWidth = 10 * MM;

        List<Point2> list孔径_二极场 = BaseUtils.Python.linspaceStream(10, 100, 100)
                .parallel()
                .mapToObj(smallR -> {
                    Cct dipoleCct = CctFactory.createDipoleCct(smallR * MM, 75 * MM,
                            0.75, 67.5, 135,
                            smallR/50*0.1, 0, 0, 1000,
                            0.0, 0.0, 0.0, 0.0,
                            360);

                    double Bz = dipoleCct.magnetAt(midPoint).z;
//                    double Bx = dipoleCct.magnetAt(midPoint).x;

                    return Point2.create(smallR, Bz);
//                    return Point2.create(smallR, Bx);

                }).collect(Collectors.toList());

        list孔径_二极场.sort(Point2::compareInX);

        Plot2d.plot2(list孔径_二极场);
        Plot2d.show();
    }

    @run
    public void a0和倾斜角的关系(){
        // 倾斜角 tan(alpha) = (t*theta)/(t*z)
        // 新建的CCT z = yDirect
        // t 即 初始绕线方向
        // theta 方向 大致是 t 方向中 z=0 ？

        boolean 查看初始点和方向 = false;
        if(查看初始点和方向){
            Cct dipoleCct = CctFactory.createDipoleCct(70 * MM, 79 * MM, 1.0, 67.5,
                    241, 0.00001, 0, 0, 5000,
                    0, 0, 0, 0, 360);

            SoleLayerCct soleLayerCct = dipoleCct.get(1);

            Logger.getLogger().info("电流 = " + soleLayerCct.getI());

            Point3 p0 = soleLayerCct.getWindings().get(0);
            Point3 p1 = soleLayerCct.getWindings().get(1);

            Logger.getLogger().info("p0 = " + p0);
            Logger.getLogger().info("p1 = " + p1);

            Vector3 v01 = Vector3.subtract(p1, p0);

            Logger.getLogger().info("v01 = " + v01);

            Vector3 t = v01.copy();

            Vector3 theta = v01.copy();
            theta.z = 0;
            theta.normalSelf();

            Vector3 z = Vector3.getYDirect().normalSelf();

            double tanAlpha = (Vector3.dot(t,theta))/(Vector3.dot(t,z));
            double alpgha = Math.atan(tanAlpha);
            double alphaRadian = BaseUtils.Converter.angleToRadian(alpgha);

            Logger.getLogger().info("t = " + t);
            Logger.getLogger().info("z = " + z);
            Logger.getLogger().info("theta = " + theta);
            Logger.getLogger().info("倾斜角？ alpgha = " + alpgha);
            Logger.getLogger().info("倾斜角 alphaRadian = " + alphaRadian);
        }

        // dipole cct
        boolean 计算a0和倾斜角 = false;
        if(计算a0和倾斜角){
            BaseUtils.Python.linspaceStream(-0.1,0.1,20)
                    .forEach(a0->{
                        Cct dipoleCct = CctFactory.createDipoleCct(
                                70 * MM, 79 * MM, 1.0,
                                67.5, 241,
                                a0, 0, 0, 5000,
                                0, 0, 0, 0,
                                360
                        );

                        SoleLayerCct soleLayerCct = dipoleCct.get(0);

                        Logger.getLogger().info("电流 = " + soleLayerCct.getI());

                        Point3 p0 = soleLayerCct.getWindings().get(0);
                        Point3 p1 = soleLayerCct.getWindings().get(1);

//                        Logger.getLogger().("p0 = " + p0);
//                        Logger.getLogger().info("p1 = " + p1);

                        Vector3 v01 = Vector3.subtract(p1, p0);

//                        Logger.getLogger().info("v01 = " + v01);

                        Vector3 t = v01.copy();

                        Vector3 theta = Vector3.getZDirect();

                        Vector3 z = Vector3.getYDirect().normalSelf();

                        double tanAlpha = (Vector3.dot(t,theta))/(Vector3.dot(t,z));
                        double alphaRadian = Math.atan(tanAlpha);
                        double alphaAngle = BaseUtils.Converter.radianToAngle(alphaRadian);

                        Logger.getLogger().info("a0 = " + a0);

                        Logger.getLogger().info("t = " + t);
                        Logger.getLogger().info("z = " + z);
                        Logger.getLogger().info("theta = " + theta);
//                        Logger.getLogger().info("倾斜角 alphaRadian = " + alphaRadian);
                        Logger.getLogger().info("倾斜角度 alphaAngle = " + alphaAngle);

                        // 推算倾斜角
                        double tanAlphaCalc = 70*MM / (1.0 * a0);
                        double alphaRadianCalc = Math.atan(tanAlphaCalc);
                        double alphaAngleCalc  = BaseUtils.Converter.radianToAngle(alphaRadianCalc);

                        Logger.getLogger().info("推测倾斜角度 alphaAngleCalc = " + alphaAngleCalc);
                        Logger.getLogger().info("推算误差 = " + Math.abs(alphaAngle - alphaAngleCalc));
                        Logger.getLogger().info("--------------------------");
                    });

            // 推算误差 < 2 度
        }


        // 四极场cct
        boolean 计算a1和倾斜角 = true;
        if(计算a1和倾斜角){
            BaseUtils.Python.linspaceStream(-0.1,0.1,20)
                    .forEach(a1->{
                        Cct dipoleCct = CctFactory.createDipoleCct(
                                70 * MM, 79 * MM, 1.0,
                                67.5, 241,
                                0, a1, 0, 5000,
                                0, 0, 0, 0,
                                360
                        );

                        SoleLayerCct soleLayerCct = dipoleCct.get(0);

                        Logger.getLogger().info("电流 = " + soleLayerCct.getI());

                        Point3 p0 = soleLayerCct.getWindings().get(0);
                        Point3 p1 = soleLayerCct.getWindings().get(1);

//                        Logger.getLogger().("p0 = " + p0);
//                        Logger.getLogger().info("p1 = " + p1);

                        Vector3 v01 = Vector3.subtract(p1, p0);

//                        Logger.getLogger().info("v01 = " + v01);

                        Vector3 t = v01.copy();

                        Vector3 theta = Vector3.getZDirect();

                        Vector3 z = Vector3.getYDirect().normalSelf();

                        double tanAlpha = (Vector3.dot(t,theta))/(Vector3.dot(t,z));
                        double alphaRadian = Math.atan(tanAlpha);
                        double alphaAngle = BaseUtils.Converter.radianToAngle(alphaRadian);

                        Logger.getLogger().info("a1 = " + a1);

                        Logger.getLogger().info("t = " + t);
                        Logger.getLogger().info("z = " + z);
                        Logger.getLogger().info("theta = " + theta);
//                        Logger.getLogger().info("倾斜角 alphaRadian = " + alphaRadian);
                        Logger.getLogger().info("倾斜角度 alphaAngle = " + alphaAngle);

                        // 推算倾斜角
                        double tanAlphaCalc = 70*MM / (2* 1.0 * a1);
                        double alphaRadianCalc = Math.atan(tanAlphaCalc);
                        double alphaAngleCalc  = BaseUtils.Converter.radianToAngle(alphaRadianCalc);

                        Logger.getLogger().info("推测倾斜角度 alphaAngleCalc = " + alphaAngleCalc);
                        Logger.getLogger().info("推算误差 = " + Math.abs(alphaAngle - alphaAngleCalc));
                        Logger.getLogger().info("--------------------------");
                    });

            // 推算误差也 < 2 度
        }
    }

    /*-----------------辅助量----------------------*/
    final static double MM = 1e-3;

    public static void main(String[] args) throws Exception {
        StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[1];
        Logger.getLogger().info("{}", stackTraceElement.getClassName());
        Class<?> currentClass = Class.forName(stackTraceElement.getClassName());

        Constructor<?> constructor = currentClass.getConstructor(null);
        Object newInstance = constructor.newInstance();

        Method[] methods = currentClass.getMethods();
        Arrays.stream(methods)
                .filter(method -> method.isAnnotationPresent(run.class))
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


        Logger.getLogger().trace("进程号 ManagementFactory.getRuntimeMXBean() = " + ManagementFactory.getRuntimeMXBean());

        MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();

        MemoryUsage memoryUsage = memoryMXBean.getHeapMemoryUsage(); //椎内存使用情况

        long totalMemorySize = memoryUsage.getInit(); //初始的总内存

        long maxMemorySize = memoryUsage.getMax(); //最大可用内存

        long usedMemorySize = memoryUsage.getUsed(); //已使用的内存

        Logger.getLogger().trace("TotalMemory = {}", totalMemorySize/(1024*1024)+"M");
        Logger.getLogger().trace("FreeMemory = {}", (totalMemorySize-usedMemorySize)/(1024*1024)+"M");
        Logger.getLogger().trace("MaxMemory = {}", maxMemorySize/(1024*1024)+"M");
        Logger.getLogger().trace("UsedMemory = {}", usedMemorySize/(1024*1024)+"M");



    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    private static @interface run {
    }
}
