package cn.edu.hust.zrx.cct.study.验证性研究;

import cn.edu.hust.zrx.cct.Logger;
import cn.edu.hust.zrx.cct.advanced.COSY;
import cn.edu.hust.zrx.cct.advanced.FirstOrderTransportMatrixSolver;
import cn.edu.hust.zrx.cct.advanced.MathFunction;
import cn.edu.hust.zrx.cct.advanced.TransportCode;
import cn.edu.hust.zrx.cct.base.BaseUtils;
import cn.edu.hust.zrx.cct.base.line.Trajectory;
import cn.edu.hust.zrx.cct.base.line.TrajectoryFactory;
import cn.edu.hust.zrx.cct.base.particle.Protons;
import cn.edu.hust.zrx.cct.base.point.Point2;
import cn.edu.hust.zrx.cct.base.python.Plot2d;
import cn.edu.hust.zrx.cct.base.opticsMagnet.qs.QsHardPlaneMagnet;
import cn.edu.hust.zrx.cct.base.vector.Vector2;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

/**
 * Description
 * B0322QS铁引进代码
 * <p>
 * Data
 * 23:12
 *
 * @author zrx
 * @version 1.0
 */
@SuppressWarnings("all")
public class B0322QS铁引进代码 {

    // -------------------------------------------------------------------------------------------
    // |                                                                                         |
    // |                                                                                         |
    // |                                                                                         |
    // |                                                                                         |
    // |              ODE 踩坑                                                                   |
    // |              传入的 HK 不是二极场、四极场                                                 |
    // |              需要归一化                                                                  |
    // |              也就是 / 磁钢度                                                             |
    // |                                                                                         |
    // |                                                                                         |
    // |                                                                                         |
    // -------------------------------------------------------------------------------------------

//    @run(1)
//    public void 简单QS磁场分布(){
//        //		长度 0.2m
//        //		半孔径 30mm
//        //		四极场 梯度53.3T/m
//        //		六极场 二阶梯度 211.1T/m^2
//
//        QsHardPlaneMagnet qsHardMagnet = QsHardPlaneMagnet.create(0.2, 30,53.3,
//                211.1, Point3.origin(), Vector3.create(1,2,0));
//
//        Trajectory trajectory = TrajectoryFactory.setStartingPoint(-1, -2).setStraightLine(5, Vector2.create(1,2));
//
//        List<Point2> magnetBzAlongTrajectory = qsHardMagnet.magnetBzAlongTrajectory(trajectory, MM);
//        List<Point2> gradientAlongTrajectoryFast = qsHardMagnet.magnetGradientAlongTrajectory(trajectory, MM, 3 * MM);
//        List<Point2> secondGradientAlongTrajectory = qsHardMagnet.magnetSecondGradientAlongTrajectory(trajectory, MM, 3 * MM);
//
//
//        Plot2d.plot2(gradientAlongTrajectoryFast,Plot2d.RED_LINE);
//        Plot2d.plot2(secondGradientAlongTrajectory,Plot2d.YELLOW_LINE);
//
//        Plot2d.info("s/m","gradient(T/m)/second gradient(T/m^2)","",18);
//
//        Plot2d.legend(18,"gradient","second gradient");
//
//        Plot2d.showThread();
//
//
//    }

//    @run(1)
//    public void 简单QS_ODE(){
//        QsHardPlaneMagnet qsHardMagnet = QsHardPlaneMagnet.create(0.2, 30,53.3,
//                211.1, Point3.origin(), Vector3.getYDirect());
//
//        Trajectory trajectory = TrajectoryFactory.setStartingPoint(0, -0.5).setStraightLine(1.2, Vector2.yDirect());
//
//        //double sStart, double sEnd, double minStep, double maxStep,
//        //                    double scaleAbsoluteTolerance, double scaleRelativeTolerance,
//        //                    BaseUtils.Async async
//        TransportCode.TransMatrix transMatrix = FirstOrderTransportMatrixSolver.build()
//                .setHKFunction(
//                        s -> qsHardMagnet.magnetBzAlongTrajectoryAt(trajectory,s),
//                        s -> qsHardMagnet.magnetGradientAlongTrajectoryAt(trajectory,3*MM,s)
//                ).isLogger(true)
//                .initiate(
//                        0.0,
//                        trajectory.getLength(),
//                        1e-8,
//                        MM,
//                        1e-6,
//                        1e-6,
//                        new BaseUtils.Async()
//                ).solveToMatrix();
//
//        Logger.getLogger().info("transMatrix = " + transMatrix);
//        //---  R  ---
//        //-1.777	-0.991	+0.000	+0.000	+0.000	+0.000
//        //-1.603	-1.456	+0.000	+0.000	+0.000	+0.000
//        //+0.000	+0.000	+43.051	+18.832	+0.000	+0.000
//        //+0.000	+0.000	+67.502	+29.551	+0.000	+0.000
//        //+0.000	+0.000	+0.000	+0.000	+1.000	+0.000
//        //+0.000	+0.000	+0.000	+0.000	+0.000	+1.000
//
//        //COSY
//
//    }

    @run(1)
    public void QS磁铁四极铁测试(){
        ////        //		长度 0.2m
        ////        //		半孔径 30mm
        ////        //		四极场 梯度53.3T/m
        ////        //		六极场 二阶梯度 211.1T/m^2
        QsHardPlaneMagnet qsHardPlaneMagnet = QsHardPlaneMagnet.create(0.2, 53.3,
                221.1, 30, Point2.origin(), Vector2.yDirect());

        Trajectory trajectory = TrajectoryFactory.setStartingPoint(0, -0.5).setStraightLine(1.2, Vector2.yDirect());
//
        List<Point2> list = qsHardPlaneMagnet.magnetGradientAlongTrajectory(trajectory,MM,3*MM);
        List<Point2> list2 = qsHardPlaneMagnet.magnetSecondGradientAlongTrajectory(trajectory,MM,3*MM);
//
//        List<Point2> list = qsHardPlaneMagnet.magnetBzAlongTrajectory(trajectory, MM);

        Plot2d.plot2(list,Plot2d.BLACK_LINE);
        Plot2d.plot2(list2,Plot2d.YELLOW_LINE);

        Plot2d.showThread();


    }

    @run(-3)
    public void QS磁场分布(){
        QsHardPlaneMagnet qsHardPlaneMagnet = QsHardPlaneMagnet.create(0.2, 53.3,
                221.1*0, 30, Point2.origin(), Vector2.yDirect());

        Trajectory trajectory = TrajectoryFactory.setStartingPoint(-1, 0.21).setStraightLine(2, Vector2.xDirect());


        List<Point2> list = qsHardPlaneMagnet.magnetBzAlongTrajectory(trajectory, MM);

        Plot2d.plot2(list);

        Plot2d.showThread();
    }

    @run(2)
    public void QS_ode(){
        QsHardPlaneMagnet qsHardPlaneMagnet = QsHardPlaneMagnet.create(0.2, 53.3,
                221.1, 30, Point2.origin(), Vector2.yDirect());

        Trajectory trajectory = TrajectoryFactory.setStartingPoint(0, -0.5).setStraightLine(1.2, Vector2.yDirect());

        TransportCode.TransMatrix transMatrix = FirstOrderTransportMatrixSolver.build()
                .setHKFunction(
                        s -> qsHardPlaneMagnet.magnetBzAlongTrajectoryAt(trajectory, s)/ Protons.getMagneticStiffness(250.0),
                        s -> qsHardPlaneMagnet.magnetGradientAlongTrajectoryAt(trajectory, 3 * MM, s)/ Protons.getMagneticStiffness(250.0)
                ).isLogger(true)
                .initiate(
                        0.0,
                        trajectory.getLength(),
                        1e-8,
                        MM,
                        1e-6,
                        1e-6,
                        new BaseUtils.Async()
                ).solveToMatrix();

        Logger.getLogger().info("transMatrix = " + transMatrix);

        // ODE
        // everything is under control
        //---  R  ---
        //-1.292	-0.178	+0.000	+0.000	+0.000	+0.000
        //-3.770	-1.292	+0.000	+0.000	+0.000	+0.000
        //+0.000	+0.000	+3.997	+2.965	+0.000	+0.000
        //+0.000	+0.000	+5.052	+3.997	+0.000	+0.000
        //+0.000	+0.000	+0.000	+0.000	+1.000	+0.000
        //+0.000	+0.000	+0.000	+0.000	+0.000	+1.000

        // TRANSPORT RESULT
        //    -1.28887  -0.17563   0.00000   0.00000   0.00000   0.00000
        //    -3.76464  -1.28887   0.00000   0.00000   0.00000   0.00000
        //     0.00000   0.00000   3.99145   2.96139   0.00000   0.00000
        //     0.00000   0.00000   5.04210   3.99145   0.00000   0.00000
        //     0.00000   0.00000   0.00000   0.00000   1.00000   0.00000
        //     0.00000   0.00000   0.00000   0.00000   0.00000   1.00000


        // COSY RESULT
        // -1.2889   -0.1756   +0.0000   +0.0000   +0.0000   +0.0000
        // -3.7646   -1.2889   +0.0000   +0.0000   +0.0000   +0.0000
        // +0.0000   +0.0000   +3.9914   +2.9614   +0.0000   +0.0000
        // +0.0000   +0.0000   +5.0421   +3.9914   +0.0000   +0.0000
        // +0.0000   +0.0000   +0.0000   +0.0000   +0.0000   +0.0000
        // +0.0000   +0.0000   +0.0000   +0.0000   +1.0000   +0.2336
    }


    @run(-5)
    public void cosy_qs(){
        COSY.TransMatrix transMatrix = cosy_qs_matrix();
        Logger.getLogger().info("transMatrix = " + transMatrix);

        //--- R ---
        // -1.2889   -0.1756   +0.0000   +0.0000   +0.0000   +0.0000
        // -3.7646   -1.2889   +0.0000   +0.0000   +0.0000   +0.0000
        // +0.0000   +0.0000   +3.9914   +2.9614   +0.0000   +0.0000
        // +0.0000   +0.0000   +5.0421   +3.9914   +0.0000   +0.0000
        // +0.0000   +0.0000   +0.0000   +0.0000   +0.0000   +0.0000
        // +0.0000   +0.0000   +0.0000   +0.0000   +1.0000   +0.2336
    }

    private COSY.TransMatrix cosy_qs_matrix(){
        return COSY.importMatrix(
                "  -1.288872     -3.764639     0.0000000E+00 0.0000000E+00 0.0000000E+00 100000\n" +
                        " -0.1756322     -1.288872     0.0000000E+00 0.0000000E+00 0.0000000E+00 010000\n" +
                        "  0.0000000E+00 0.0000000E+00  3.991448      5.042101     0.0000000E+00 001000\n" +
                        "  0.0000000E+00 0.0000000E+00  2.961395      3.991448     0.0000000E+00 000100\n" +
                        "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00  1.000000     000010\n" +
                        "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00 0.2336092     000001\n" +
                        "  -6.756905     -10.75822     0.0000000E+00 0.0000000E+00 -2.278964     200000\n" +
                        "  -8.101552     -13.08184     0.0000000E+00 0.0000000E+00 -1.473779     110000\n" +
                        "  -2.462842     -4.034203     0.0000000E+00 0.0000000E+00-0.3982464     020000\n" +
                        "  0.0000000E+00 0.0000000E+00  22.97803      38.63907     0.0000000E+00 101000\n" +
                        "  0.0000000E+00 0.0000000E+00  13.83387      23.49227     0.0000000E+00 011000\n" +
                        "   12.10767      20.48058     0.0000000E+00 0.0000000E+00 -3.975898     002000\n" +
                        "  0.0000000E+00 0.0000000E+00  13.27880      22.46378     0.0000000E+00 100100\n" +
                        "  0.0000000E+00 0.0000000E+00  8.073411      13.79441     0.0000000E+00 010100\n" +
                        "   14.51022      24.76254     0.0000000E+00 0.0000000E+00 -6.371939     001100\n" +
                        "   1.098994    -0.3263409     0.0000000E+00 0.0000000E+00 0.0000000E+00 100001\n" +
                        "  0.7677344      1.098994     0.0000000E+00 0.0000000E+00 0.0000000E+00 010001\n" +
                        "  0.0000000E+00 0.0000000E+00 -1.884848    -0.3887813     0.0000000E+00 001001\n" +
                        "   4.371481      7.522692     0.0000000E+00 0.0000000E+00 -2.709000     000200\n" +
                        "  0.0000000E+00 0.0000000E+00 -2.755838     -1.884848     0.0000000E+00 000101\n" +
                        "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00-0.1958045     000002"
        );
    }



    @run(-1000)
    public void enge函数01(){
        MathFunction mathFunction = COSY.defaultQuadrupoleEngeFunction(1);

        //Function<Double, Double> function, Point2 interval, int number, String describe
        Plot2d.plot2(mathFunction.toDoubleDoubleFunction(),Point2.create(-3.5/2,2.7),200,Plot2d.BLACK_LINE);

        double big = mathFunction.apply(-3.5 / 2);
        Logger.getLogger().info("big = " + big);

        Plot2d.plotXLine(0,2.7,-3.5, Plot2d.GREY_DASH);
        Plot2d.plotXLine(1,2.7,-3.5, Plot2d.GREY_DASH);

        Plot2d.showThread();
    }


    /*-----------------辅助量----------------------*/
    final static double MM = 1e-3;
    final static double MRAD = 1e-3; // m rad
    final static double kA = 1e3;
    final static double PRESENT = 0.01; // %

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
