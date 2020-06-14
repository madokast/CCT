package cn.edu.hust.zrx.cct.study.超导机架AGCCT单独建模;

import cn.edu.hust.zrx.cct.Logger;
import cn.edu.hust.zrx.cct.base.BaseUtils;
import cn.edu.hust.zrx.cct.base.cct.Cct;
import cn.edu.hust.zrx.cct.base.cct.CctFactory;
import cn.edu.hust.zrx.cct.base.cct.CctLine2;
import cn.edu.hust.zrx.cct.base.cct.SoleLayerCct;
import cn.edu.hust.zrx.cct.base.line.Trajectory;
import cn.edu.hust.zrx.cct.base.line.TrajectoryFactory;
import cn.edu.hust.zrx.cct.base.point.Point2;
import cn.edu.hust.zrx.cct.base.python.Plot2d;
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
import java.util.stream.Collectors;

/**
 * Description
 * AGCCT225增大孔径 研究
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
public class AGCCT225增大孔径 {
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
    public void 孔径和右手侧磁场的关系() {
        Trajectory trajectory = getTrajectory();
        double startTheta = 0.0;
        double smallRInner = 43 * MM;
        final Vector3 zDirect = Vector3.getZDirect();
        List<Point2> all = new ArrayList<>();

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

        BaseUtils.Python.linspaceStream(20 * MM, 60 * MM, switcher.getSize())
                .forEach(smallR -> {
                    SoleLayerCct innerDipoleCct = getInnerDipoleCct(startTheta, smallR);
                    SoleLayerCct outerDipoleCct = getOuterDipoleCct(startTheta, smallR);


                    List<Point2> rightHandB = outerDipoleCct.magnetAlongTrajectory(trajectory, 1 * MM)
                            .stream()
                            .map(point3WithDistance -> {
                                Vector3 m = point3WithDistance.getPoint3().toVector3();
                                double distance = point3WithDistance.getDistance();

                                Vector3 directAt = trajectory.directAt(distance).toVector3();
                                Vector3 rightHandDirect = directAt.cross(zDirect);

                                return Point2.create(distance, m.dot(rightHandDirect));

                            }).collect(Collectors.toList());

                    String plotDes = switcher.getAndSwitch();

                    Logger.getLogger().info("smallR = {}, color = {}", smallR, plotDes);
                    Plot2d.plot2(rightHandB, plotDes);
                    all.addAll(rightHandB);
                });


        Plot2d.xLabel("s/m");
        Plot2d.yLabel("右手侧磁场/T");
        Plot2d.title("轨道右手侧磁场分布(非0导致参考粒子脱离中平面)");
        Plot2d.plotYLines(trajectory.couplingPointLocation(), all);
        Plot2d.showThread();

//        creatDipoleCct().plot3(Plot2d.RED_LINE,Plot2d.YELLOW_LINE);
//        trajectory.plot3d(1*MM);
//        Plot3d.setCenter(trajectory.pointAt(1).toPoint3(),0.8);
//        Plot3d.showThread();
    }



    public SoleLayerCct getOuterDipoleCct(double startTheta, double smallROuter) {
        CctLine2 cctLine2 = new CctLine2(smallROuter, bigR, dipoleCctAngle, dipoleCctWindingNumber,
                dipoleCctA0Outer, dipoleCctA1Outer, dipoleCctA2Outer, startTheta, 0.0, true);
        SoleLayerCct outerCct =
                new SoleLayerCct(cctLine2.disperseToPoint3(numberPerWinding), dipoleCctIOuter);

        return outerCct;
    }

    public SoleLayerCct getInnerDipoleCct(double startTheta, double smallRInner) {
        CctLine2 cctLine2 = new CctLine2(smallRInner, bigR, dipoleCctAngle, dipoleCctWindingNumber,
                dipoleCctA0Inner, dipoleCctA1Inner, dipoleCctA2Inner, startTheta, 0.0, true);
        SoleLayerCct innerCct =
                new SoleLayerCct(cctLine2.disperseToPoint3(numberPerWinding), dipoleCctIInner);

        return innerCct;
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
                .filter(method -> method.isAnnotationPresent(AGCCT225增大孔径.run.class))
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
