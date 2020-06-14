package cn.edu.hust.zrx.cct.study.超导机架AGCCT单独建模;

import cn.edu.hust.zrx.cct.Logger;
import cn.edu.hust.zrx.cct.base.BaseUtils;
import cn.edu.hust.zrx.cct.base.cct.Cct;
import cn.edu.hust.zrx.cct.base.cct.CctFactory;
import cn.edu.hust.zrx.cct.base.line.Trajectory;
import cn.edu.hust.zrx.cct.base.line.TrajectoryFactory;
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
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Description
 * A0320AGCCT225修改二极CCTa0抵消四极场
 * <p>
 * Data
 * 20:13
 *
 * @author zrx
 * @version 1.0
 */
@SuppressWarnings("all")
public class A0320AGCCT225修改二极CCTa0抵消四极场 {

    @run(0)
    public void 回顾四极CCT的二极场() {
        Trajectory trajectory = getTrajectory();
        Cct agCct = createAgCct();

        List<Point2> list = agCct.magnetBzAlongTrajectory(trajectory, MM);

        list = Point2.convert(list, null, (x, y) -> y * 10000);

        Plot2d.plot2(list, Plot2d.RED_LINE);

        Plot2d.showThread();
    }

    @run(1)
    public void 二极CCT的四极场画图() {
        Trajectory trajectory = getTrajectory();
        Cct dipoleCct = createDipoleCct();

        List<Point2> gradientAlongTrajectoryFast =
                dipoleCct.magnetGradientAlongTrajectoryFast(trajectory, 1 * MM, 3 * MM);


        Plot2d.plot2(gradientAlongTrajectoryFast, Plot2d.RED_LINE);

        Plot2d.showThread();
    }

    @run(value = 2,code = "a0320-a0-vary-0320-2238")
    public void 二极CCT改变a1() {
        //    private double dipoleCctA1Inner = 0.0;
        //    private double dipoleCctA1Outer = 0.0;

        //    private final double agCctA1Inner = -Math.pow(agCctSmallRInner, 2) * Math.sqrt(3) * 20;
        //    private final double agCctA1Outer = Math.pow(agCctSmallROuter, 2) * Math.sqrt(3) * 20;

        // 可以看到  a0in 和 a0out 需要成比例
        // in / out = - ( rin / rout ) ^ 2

        Trajectory trajectory = getTrajectory();

//        String[] describes = BaseUtils.Python.linspaceStream(0, 0, 1)
        String[] describes = BaseUtils.Python.linspaceStream(-0.3, 0, switcher.getSize())
                .parallel()
                .mapToObj(k -> {
                    A0320AGCCT225修改二极CCTa0抵消四极场 obj = new A0320AGCCT225修改二极CCTa0抵消四极场();
                    obj.dipoleCctA1Inner = -Math.pow(obj.dipoleCctSmallRInner, 2) * k;
                    obj.dipoleCctA1Outer = Math.pow(obj.dipoleCctSmallROuter, 2) * k;
                    Cct dipoleCct = obj.createDipoleCct();

                    List<Point2> list = dipoleCct.magnetGradientAlongTrajectoryFast(trajectory, 1*MM, 3 * MM);

                    return BaseUtils.Content.BiContent.create(k, list);
                })
                .collect(Collectors.toList())
                .stream()
                .sorted(Comparator.comparingDouble(BaseUtils.Content.BiContent::getT1))
                .peek(bi -> Plot2d.plot2(bi.getT2(), switcher.getAndSwitch()))
                .map(BaseUtils.Content.BiContent::getT1)
                .map(k-> BaseUtils.Doubles.reservedDecimal(k,3))
                .map(k -> "k = " + k)
                .collect(Collectors.toList())
                .toArray(new String[]{});

        Plot2d.info("s/m","gradient (T/m)","",18);

        Plot2d.legend(18,describes);

        Plot2d.showThread();
    }


    //---------------------------------------------------------

    private Cct getCct() {
        Cct dipoleCct = createDipoleCct();
        Cct agCct = createAgCct();

        return CctFactory.combineCct(dipoleCct, agCct);
    }

    private Trajectory getTrajectory() {
        return TrajectoryFactory.setStartingPoint(trajectoryBigR, -1)
                .setStraightLine(1, Vector2.yDirect())
                .addArcLine(trajectoryBigR, false, dipoleCctAngle)
                .addStraitLine(1.0);
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

    private final double trajectoryBigR = 0.75;
    private final double dipoleCctBigR = 0.75;
    private double agCCTBigR = 750.2 * MM;
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
    private double dipoleCctA1Inner = Math.pow(dipoleCctSmallRInner, 2) * 0.025;
    private double dipoleCctA1Outer = -Math.pow(dipoleCctSmallROuter, 2) * 0.025;
    private final double dipoleCctA2Inner = 0.0;
    private final double dipoleCctA2Outer = 0.0;

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
    private final double dipoleCctIInner = 9.898121356964111 * kA; // 求解获得
    private final double dipoleCctIOuter = -dipoleCctIInner;
    public double agCctIInner = 4618.911905272398;
    public double agCctIOuter = -agCctIInner;
    // CCT每匝分段
    private final int numberPerWinding = 360;

    /*-----------------辅助量----------------------*/
    final static double MM = 1e-3;
    final static double MRAD = 1e-3;
    final static double kA = 1e3;

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
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
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
