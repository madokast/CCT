package cn.edu.hust.zrx.cct.study.验证性研究;

import cn.edu.hust.zrx.cct.Logger;
import cn.edu.hust.zrx.cct.base.BaseUtils;
import cn.edu.hust.zrx.cct.advanced.FirstOrderTransportMatrixEquation;
import cn.edu.hust.zrx.cct.advanced.FirstOrderTransportMatrixSolver;
import cn.edu.hust.zrx.cct.base.cct.Cct;
import cn.edu.hust.zrx.cct.base.cct.CctFactory;
import cn.edu.hust.zrx.cct.base.line.Trajectory;
import cn.edu.hust.zrx.cct.base.line.TrajectoryFactory;
import cn.edu.hust.zrx.cct.base.point.Point2;
import cn.edu.hust.zrx.cct.base.point.Point3;
import cn.edu.hust.zrx.cct.base.python.Plot2d;
import cn.edu.hust.zrx.cct.base.vector.Vector2;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.FirstOrderIntegrator;
import org.apache.commons.math3.ode.nonstiff.DormandPrince853Integrator;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.ode.sampling.StepInterpolator;

import java.io.*;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * Description
 * 回顾常微分方程
 * <p>
 * Data
 * 11:14
 *
 * @author zrx
 * @version 1.0
 */
@SuppressWarnings("all")
public class A20200317常微分方程 {

    //    @run
    public void 一阶常微分方程() {
        // * 练习一阶一元常微分方程求解。
        // * 函数 y^2 = 4t  即 y = 2sqrt(t)
        // * 方程化为 y' = 2/y ,y0=0

        FirstOrderDifferentialEquations firstOrderDifferentialEquations = new FirstOrderDifferentialEquations() {
            //维度
            @Override
            public int getDimension() {
                return 1;
            }

            //方程
            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) throws MaxCountExceededException, DimensionMismatchException {
                yDot[0] = (8 * t + 1) / (2 * y[0]);
                Logger.getLogger().info("t = " + t);
            }
        };

        final List<Point2> stepList = new ArrayList<>();

        FirstOrderIntegrator dp853 = new DormandPrince853Integrator(1.0e-8, 1.0, 1.0e-10, 1.0e-10);
        double[] y0 = new double[]{Math.sqrt(5)};
        double[] y = new double[]{Math.sqrt(5)};
        dp853.addStepHandler(new StepHandler() {
            @Override
            public void init(double t0, double[] y0, double t) {
                // do nothing
            }

            @Override
            public void handleStep(StepInterpolator interpolator, boolean isLast) throws MaxCountExceededException {
                double t = interpolator.getCurrentTime();
                double[] y = interpolator.getInterpolatedState();
                stepList.add(Point2.create(t, y[0]));
            }
        });
        dp853.integrate(firstOrderDifferentialEquations, 1.0, y0, 2.0, y);

        Logger.getLogger().info("Arrays.toString(y0) = " + Arrays.toString(y0));
        Logger.getLogger().info("Arrays.toString(y) = " + Arrays.toString(y));


        // plot


        Plot2d.plot2(x -> Math.sqrt(4 * x * x + x), Point2.create(0, 3), 100, Plot2d.BLACK_LINE);

        Plot2d.plot2(stepList, Plot2d.RED_LINE);

        Plot2d.equal();

        Plot2d.showThread();
    }


    //    @run
    public void 绘制函数曲线() {

        Plot2d.plot2(x -> Math.sqrt(4 * x * x + x), Point2.create(0, 3), 100, Plot2d.BLACK_LINE);

        Plot2d.equal();

        Plot2d.showThread();
    }

//    @run
    public void 传输矩阵计算_复杂写法() {
        Trajectory trajectory = getTrajectory();
        Cct cct = getCct();

        FirstOrderTransportMatrixEquation transportMatrixFirstOrderEquation
                = new FirstOrderTransportMatrixEquation() {
            @Override
            public HKdP getHKdp(double s) {
                double h = cct.magnetBzAlongTrajectoryAt(trajectory, s);
                double k = cct.magnetGradientAlongTrajectoryAt(trajectory, 5 * MM, s);

                return HKdP.valueOf(h, k, 0.0);
            }

            @Override
            public boolean loggerDistance() {
                return false;
            }
        };

        final double sStart = 0.0;
        final double sEnd = trajectory.getLength();
        Logger.getLogger().info("sEnd = " + sEnd);

        final double minStep = 1e-10;
        final double maxStep = 0.1;
        final double scaleAbsoluteTolerance = 1e-6;
        final double scaleRelativeTolerance = 1e-6;

        final Map<String, Double> matrix = new ConcurrentHashMap<>();

        BaseUtils.Async async = new BaseUtils.Async();

        async.execute(() -> {
            double[] y0 = new double[]{1.0, 0, 0, 0};
            final FirstOrderIntegrator integrator = new DormandPrince853Integrator(
                    minStep, maxStep, scaleAbsoluteTolerance, scaleRelativeTolerance);
            integrator.integrate(transportMatrixFirstOrderEquation, sStart, y0, sEnd, y0);
            matrix.put("r11", y0[0]);
            matrix.put("r21", y0[1]);
        });

        async.execute(() -> {
            double[] y0 = new double[]{0.0, 1.0, 0, 0};
            final FirstOrderIntegrator integrator = new DormandPrince853Integrator(
                    minStep, maxStep, scaleAbsoluteTolerance, scaleRelativeTolerance);
            integrator.integrate(transportMatrixFirstOrderEquation, sStart, y0, sEnd, y0);
            matrix.put("r12", y0[0]);
            matrix.put("r22", y0[1]);
        });

        async.execute(() -> {
            double[] y0 = new double[]{0.0, 0.0, 1.0, 0};
            final FirstOrderIntegrator integrator = new DormandPrince853Integrator(
                    minStep, maxStep, scaleAbsoluteTolerance, scaleRelativeTolerance);
            integrator.integrate(transportMatrixFirstOrderEquation, sStart, y0, sEnd, y0);
            matrix.put("r33", y0[2]);
            matrix.put("r43", y0[3]);
        });

        async.execute(() -> {
            double[] y0 = new double[]{0.0, 0.0, 0.0, 1.0};
            final FirstOrderIntegrator integrator = new DormandPrince853Integrator(
                    minStep, maxStep, scaleAbsoluteTolerance, scaleRelativeTolerance);
            integrator.integrate(transportMatrixFirstOrderEquation, sStart, y0, sEnd, y0);
            matrix.put("r34", y0[2]);
            matrix.put("r44", y0[3]);
        });

        async.execute(() -> {
            double[] y0 = new double[]{0.0, 0.0, 0.0, 0.0};
            final FirstOrderIntegrator integrator = new DormandPrince853Integrator(
                    minStep, maxStep, scaleAbsoluteTolerance, scaleRelativeTolerance);
            integrator.integrate(transportMatrixFirstOrderEquation, sStart, y0, sEnd, y0);
            matrix.put("r16", y0[0]);
            matrix.put("r26", y0[1]);
        });


        async.waitForAllFinish(1, TimeUnit.HOURS);

        Logger.getLogger().info("matrix = " + matrix);


        StringBuilder stringBuilder = new StringBuilder("\n");
        for (int i = 1; i <= 6; i++) {
            for (int j = 1; j <= 6; j++) {
                String key = "r" + i + j;
                Double value = matrix.getOrDefault(key, 0.);
                stringBuilder.append(value).append("  ");
            }
            stringBuilder.append("\n");
        }

        Logger.getLogger().info("R = " + stringBuilder.toString());

    }

//    @run
    public void 传输矩阵计算_新API()throws Exception{
        Cct cct = getCct();
        Trajectory trajectory = getTrajectory();

        String solveAndFormMatrix = FirstOrderTransportMatrixSolver
                .build()
                .setHKFunction(
                        s -> cct.magnetBzAlongTrajectoryAt(trajectory, s),
                        s -> cct.magnetGradientAlongTrajectoryAt(trajectory, 5 * MM, s))
                .isLogger(true)
                .initiate(
                        0.0,
                        trajectory.getLength(),
                        1e-8,
                        0.1,
                        1e-5,
                        1e-5,
                        new BaseUtils.Async())
                .solveAndFormMatrix();

        Logger.getLogger().info("solveAndFormMatrix = " + solveAndFormMatrix);
    }


    /**
     * 验证成功
     * @throws Exception ？
     */
    @run
    public void 验证()throws Exception{
        List<Point3> dataForValid = getDataForValid();

        String formMatrix = FirstOrderTransportMatrixSolver
                .build()
                .setHKFunction(s ->
                {
                    Point3 pre = null;
                    Point3 after = null;
                    for (int i = 1; i < dataForValid.size(); i++) {
                        Point3 point3 = dataForValid.get(i);
                        if(point3.x>s){
                            pre = dataForValid.get(i-1);
                            after = point3;
                            break;
                        }
                    }

                    if(pre==null)
                        return 0.;

                    double delta = s -pre.x;
                    double h = pre.y + (after.y-pre.y)*(delta)/(after.x-pre.x);

                    return h;

                },s->
                {

                        Point3 pre = null;
                        Point3 after = null;
                        for (int i = 1; i < dataForValid.size(); i++) {
                            Point3 point3 = dataForValid.get(i);
                            if(point3.x>s){
                                pre = dataForValid.get(i-1);
                                after = point3;
                                break;
                            }
                        }

                        if(pre==null)
                            return 0.;

                        double delta = s -pre.x;
                        double k = pre.z + (after.z-pre.z)*(delta)/(after.x-pre.x);

                        return k;


                })
                .isLogger(false)
                .initiate(
                        0.0,
                        dataForValid.get(dataForValid.size() - 1).x,
                        1e-10,
                        1 * MM,
                        1e-7,
                        1e-7,
                        new BaseUtils.Async()
                        )
                .solveAndFormMatrix();

        Logger.getLogger().info("formMatrix = " + formMatrix);
    }

    private List<Point3> getDataForValid()throws Exception{
        List<Point3> list = new ArrayList<>();

        InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream("shkl.txt");

        Objects.requireNonNull(resourceAsStream);

        InputStreamReader inputStreamReader = new InputStreamReader(resourceAsStream);

        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

        String line = bufferedReader.readLine();

        while (line!=null){
            String[] split = line.trim().split("\\s+");

            double s = Double.parseDouble(split[0])/1000.0;
            double h = Double.parseDouble(split[1]);
            double k = Double.parseDouble(split[2]);

            list.add(Point3.create(s,h,k));



            line = bufferedReader.readLine();
        }


        return list;
    }

    //---------------------------------------------------------

    private Cct getCct() {
        Cct dipoleCct = createDipoleCct();
        Cct agCct = createAgCct();

        return CctFactory.combineCct(dipoleCct, agCct);
    }

    private Trajectory getTrajectory() {
        return TrajectoryFactory.setStartingPoint(bigR, -1)
                .setStraightLine(1, Vector2.yDirect())
                .addArcLine(bigR, false, dipoleCctAngle)
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
        return CctFactory.createAgCct(agCctSmallRInner, agCctSmallROuter, bigR,
                new double[]{agCctAngle0, agCctAngle1},
                new int[]{agCctWindingNumber0, agCctWindingNumber1},
                agCctA0Inner, agCctA1Inner, agCctA2Inner, agCctIInner,
                agCctA0Outer, agCctA1Outer, agCctA2Outer, agCctIOuter,
                numberPerWinding);
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
    ;
    private final double agCctA1Outer = Math.pow(agCctSmallROuter, 2) * Math.sqrt(3) * 20;
    ;
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

        Method[] methods = currentClass.getMethods();
        Arrays.stream(methods)
                .filter(method -> method.isAnnotationPresent(A20200317常微分方程.run.class))
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
    }


}
