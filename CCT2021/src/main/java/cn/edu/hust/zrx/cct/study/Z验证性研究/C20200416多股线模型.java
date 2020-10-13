package cn.edu.hust.zrx.cct.study.Z验证性研究;

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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Description
 * C20200416多股线模型
 * <p>
 * Data
 * 22:42
 *
 * @author zrx
 * @version 1.0
 */
@SuppressWarnings("all")
public class C20200416多股线模型 {

    @run(1)
    public void 单股线() {
        Trajectory trajectory = getTrajectory();


        Cct dipoleCct = createDipoleCct();


        List<Point2> list = dipoleCct.magnetBzAlongTrajectory(trajectory, MM);

        Plot2d.plot2(list, Plot2d.BLACK_LINE);

        Plot2d.info("s/x", "By/T", "", 18);

        Plot2d.plotYLines(List.of(1., trajectory.getLength() - 1), list);

        Plot2d.showThread();
    }

    @run(2)
    public void 多股线dipole() {
        Cct dipoleCctIn = CctFactory.createDipoleCct(
                dipoleCctSmallRInner - MM, dipoleCctSmallROuter - MM, bigR,
                dipoleCctAngle, dipoleCctWindingNumber,
                -(dipoleCctSmallRInner - MM) * Math.sqrt(3), dipoleCctA1Inner, dipoleCctA2Inner, dipoleCctIInner / 6.0,
                (dipoleCctSmallROuter - MM) * Math.sqrt(3), dipoleCctA1Outer, dipoleCctA2Outer, dipoleCctIOuter / 6.0,
                numberPerWinding
        );

        Cct dipoleCctOut = CctFactory.createDipoleCct(
                dipoleCctSmallRInner + MM, dipoleCctSmallROuter + MM, bigR,
                dipoleCctAngle, dipoleCctWindingNumber,
                -(dipoleCctSmallRInner + MM) * Math.sqrt(3), dipoleCctA1Inner, dipoleCctA2Inner, dipoleCctIInner / 6.0,
                (dipoleCctSmallROuter + MM) * Math.sqrt(3), dipoleCctA1Outer, dipoleCctA2Outer, dipoleCctIOuter / 6.0,
                numberPerWinding
        );

        Cct cctInPlus = CctFactory.rotateInXYPlane(dipoleCctIn, Point2.origin(), 2 * MM / bigR);
        Cct cctInSub = CctFactory.rotateInXYPlane(dipoleCctIn, Point2.origin(), -2 * MM / bigR);

        Cct cctOutPlus = CctFactory.rotateInXYPlane(dipoleCctOut, Point2.origin(), 2 * MM / bigR);
        Cct cctOutSub = CctFactory.rotateInXYPlane(dipoleCctOut, Point2.origin(), -2 * MM / bigR);

        Cct cct = CctFactory.combineCct(dipoleCctIn, dipoleCctOut, cctInPlus, cctInSub, cctOutPlus, cctOutSub);


        Cct dipoleCct = createDipoleCct();


        Trajectory trajectory = getTrajectory();

        double length = trajectory.getLength();
        Logger.getLogger().info("length = " + length);


        List<Point2> one = dipoleCct.magnetBzAlongTrajectory(trajectory, MM);

        List<Point2> six = cct.magnetBzAlongTrajectory(trajectory, MM);

        List<Point2> offset = new ArrayList<>(one.size());

        for (int i = 0; i < one.size(); i++) {
            Point2 oneP = one.get(i);
            Point2 sixP = six.get(i);

            if (oneP.x > 1 && oneP.x < (trajectory.getLength() - 1)) {
            offset.add(Point2.create(oneP.x, (oneP.y - sixP.y)/sixP.y));
            }
        }

        Plot2d.plot2(offset, Plot2d.BLACK_LINE);

        Plot2d.plotYLines(List.of(1., length - 1), offset);

        Plot2d.info("s/x", "(ΔBy)/By", "", 18);
//        Plot2d.info("s/x", "(ΔBy)/Gs", "", 18);


        Plot2d.showThread();

    }


    //---------------------------------------------------------

    private Trajectory getTrajectory() {
        return TrajectoryFactory.setStartingPoint(bigR, -1)
                .setStraightLine(1, Vector2.yDirect())
                .addArcLine(bigR, false, dipoleCctAngle)
                .addStraitLine(1.0);
    }

    private Cct createAgCct() {
        return CctFactory.createAgCct(agCctSmallRInner, agCctSmallROuter, bigR,
                new double[]{agCctAngle0, agCctAngle1},
                new int[]{agCctWindingNumber0, agCctWindingNumber1},
                agCctA0Inner, agCctA1Inner, agCctA2Inner, agCctIInner,
                agCctA0Outer, agCctA1Outer, agCctA2Outer, agCctIOuter,
                numberPerWinding
        );
    }

    private Cct createDipoleCct() {
        return CctFactory.createDipoleCct(
                dipoleCctSmallRInner, dipoleCctSmallROuter, bigR,
                dipoleCctAngle, dipoleCctWindingNumber,
                dipoleCctA0Inner, dipoleCctA1Inner, dipoleCctA2Inner, dipoleCctIInner,
                dipoleCctA0Outer, dipoleCctA1Outer, dipoleCctA2Outer, dipoleCctIOuter,
                numberPerWinding
        );
    }

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
