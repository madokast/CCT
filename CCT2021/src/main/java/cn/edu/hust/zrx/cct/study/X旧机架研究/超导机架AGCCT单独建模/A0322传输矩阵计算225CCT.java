package cn.edu.hust.zrx.cct.study.X旧机架研究.超导机架AGCCT单独建模;

import cn.edu.hust.zrx.cct.Logger;
import cn.edu.hust.zrx.cct.base.BaseUtils;
import cn.edu.hust.zrx.cct.advanced.COSY;
import cn.edu.hust.zrx.cct.advanced.FirstOrderTransportMatrixSolver;
import cn.edu.hust.zrx.cct.advanced.TransportCode;
import cn.edu.hust.zrx.cct.base.cct.Cct;
import cn.edu.hust.zrx.cct.base.cct.CctFactory;
import cn.edu.hust.zrx.cct.base.line.Trajectory;
import cn.edu.hust.zrx.cct.base.line.TrajectoryFactory;
import cn.edu.hust.zrx.cct.base.particle.*;
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
import java.util.List;

/**
 * Description
 * A0322传输矩阵计算225CCT
 * <p>
 * Data
 * 14:46
 *
 * @author zrx
 * @version 1.0
 */
@SuppressWarnings("all")
public class A0322传输矩阵计算225CCT {

    // -------------------------------------------------------
    // |                                                     |
    // |                                                     |
    // |                                                     |
    // |                                                     |
    // |        ODE 方法有大问题                              |
    // |        不过不怕                                      |
    // |        不用 trajectory                               |
    // |        用 参考粒子轨迹来试试？                        |
    // |                                                     |
    // |        能不能把 真实粒子轨迹                          |
    // |        包装成 Trajectory 对象？                      |
    // |        我要试试                                      |
    // |                                                     |
    // |                                                     |
    // -------------------------------------------------------

    @run(code = "这代码如此简单，但实际上耗费心血")
    public TransportCode.TransMatrix computeMatrix(double length){
        Cct cct = getCct();
        Trajectory trajectory = getTrajectory();

        TransportCode.TransMatrix transMatrix = FirstOrderTransportMatrixSolver.build()
                .setHKFunction(
                        s -> cct.magnetBzAlongTrajectoryAt(trajectory, s)/Protons.getMagneticStiffness(250.),
                        s -> cct.magnetGradientAlongTrajectoryAt(trajectory, 5 * MM, s)/Protons.getMagneticStiffness(250.)
                )
                .isLogger(false)
                .initiate(
                        0.0,
                        length,
                        1e-8,
                        0.1,
                        1e-6,
                        1e-6,
                        new BaseUtils.Async()
                )
                .solveToMatrix();

        Logger.getLogger().info("matrix = " + transMatrix);

        return transMatrix;
    }


    public List<Point2> matrixApply(boolean xxPlane,double delta,double length,int number){

        List<PhaseSpaceParticle> phaseSpaceParticles =
                PhaseSpaceParticles.phaseSpaceParticlesAlongPositiveEllipseInPlane(xxPlane, 3.5 * MM, 7.5 * MRAD, delta, number);

        TransportCode.TransMatrix matrix = computeMatrix(length);

        List<PhaseSpaceParticle> applied = matrix.apply(phaseSpaceParticles);

        List<Point2> plane = PhaseSpaceParticles.projectionToPlane(xxPlane, applied);
        plane = Point2.convert(plane,1/MM,1/MRAD);

//        Plot2d.plot2(plane,Plot2d.RED_POINT);
//
//        Plot2d.equal();
//
//        Plot2d.showThread();

        return plane;
    }


    public List<Point2> trackOnEnd(boolean xxPlane,double delta,double length,int number){
        Cct cct = getCct();
        Trajectory trajectory = getTrajectory();

        RunningParticle ip = ParticleFactory.createIdealProtonAtTrajectory250MeV(trajectory);
        RunningParticle ipEnd = ParticleFactory.createIdealProtonAtTrajectory250MeV(trajectory,length);

        List<PhaseSpaceParticle> phaseSpaceParticles =
                PhaseSpaceParticles.phaseSpaceParticlesAlongPositiveEllipseInPlane(xxPlane, 3.5 * MM, 7.5 * MRAD, delta, number);


        List<RunningParticle> ps = ParticleFactory.createParticlesFromPhaseSpaceParticle(ip, ip.computeNaturalCoordinateSystem(), phaseSpaceParticles);

        ParticleRunner.runThread(ps,cct,length,MM);

        List<PhaseSpaceParticle> ppEnd = PhaseSpaceParticles.phaseSpaceParticlesFromRunningParticles(ipEnd, ipEnd.computeNaturalCoordinateSystem(), ps);

        List<Point2> plane = PhaseSpaceParticles.projectionToPlane(xxPlane, ppEnd);

        plane = Point2.convert(plane,1/MM,1/MRAD);

//        Plot2d.plot2(plane);
//
//        Plot2d.equal();
//
//        Plot2d.showThread();


        return plane;
    }

    public List<Point2> cosyResult(boolean xxPlane,double delta,COSY.TransMatrix matrix,int number){
        List<PhaseSpaceParticle> phaseSpaceParticles =
                PhaseSpaceParticles.phaseSpaceParticlesAlongPositiveEllipseInPlane(xxPlane, 3.5 * MM, 7.5 * MRAD, delta, number);

        List<PhaseSpaceParticle> convertDeltaFromMomentumDispersionToEnergyDispersion =
                PhaseSpaceParticles.convertDeltaFromMomentumDispersionToEnergyDispersion(phaseSpaceParticles, 250);

        List<PhaseSpaceParticle> apply = matrix.apply(convertDeltaFromMomentumDispersionToEnergyDispersion);

        List<Point2> list = PhaseSpaceParticles.projectionToPlane(xxPlane, apply);

        list = Point2.convert(list,1/MM,1/MRAD);

        return list;
    }

    @run(value = 3,code = "A0322-ODE-2205")
    public void 三大对比(){
        boolean xxPlane = false;
        double delta = 0.0;
        double length = getTrajectory().getLength();
        int number = 64;

        List<Point2> applyOnEnd = matrixApply(xxPlane, delta,length,number);
        List<Point2> track = trackOnEnd(xxPlane, delta,length,number);
        List<Point2> cosyResult = cosyResult(xxPlane, delta, endMatrixSecond(), number);

        Plot2d.plot2(applyOnEnd,Plot2d.BLACK_POINT);
        Plot2d.plot2(track,Plot2d.RED_POINT);
        Plot2d.plot2(cosyResult,Plot2d.BLUE_POINT);
        Plot2d.plotPoint(Point2.origin(),Plot2d.GREEN_UP_TRI);

        Plot2d.plot2circle(applyOnEnd,Plot2d.BLACK_LINE);
        Plot2d.plot2circle(track,Plot2d.RED_LINE);
        Plot2d.plot2circle(cosyResult,Plot2d.BLACK_LINE);

        Plot2d.legend(18,"ODE","track","cosy","center");

        Plot2d.info(
                xxPlane?"x/mm":"y/mm",
                xxPlane?"xp/mrad":"yp/mrad",
                xxPlane?"x-plane":"y-plane",
                18
        );

        Plot2d.equal();

        Plot2d.showThread();

    }


    @run(-100)
    public void 打印COSY_MAP(){
        COSY.TransMatrix matrix = endMatrixFirst();

        Logger.getLogger().info("matrix = " + matrix);

        double r16 = matrix.getR(1, 6);

        r16 = COSY.convertR16InCosyToTransportPronTonOnly(r16, 250);

        Logger.getLogger().info("r16 = " + r16);

    }

    //--------------------------------

    @run(code = "new")
    private COSY.TransMatrix endMatrixFirst() {
        return COSY.importMatrix(//
                "  0.3633288    -0.3692074     0.0000000E+00 0.0000000E+00 0.1680138     100000\n" +
                        "   1.302286      1.428971     0.0000000E+00 0.0000000E+00 0.1154528     010000\n" +
                        "  0.0000000E+00 0.0000000E+00 0.3225315E-01 -2.412454     0.0000000E+00 001000\n" +
                        "  0.0000000E+00 0.0000000E+00 0.4267180    -0.9126992     0.0000000E+00 000100\n" +
                        "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00  1.000000     000010\n" +
                        " -0.1768548    -0.2827129     0.0000000E+00 0.0000000E+00 0.2495793     000001"
        );
    }

    @run(code = "new")
    private COSY.TransMatrix endMatrixSecond() {
        return COSY.importMatrix(
                "  0.3633288    -0.3692074     0.0000000E+00 0.0000000E+00 0.1680138     100000\n" +
                        "   1.302286      1.428971     0.0000000E+00 0.0000000E+00 0.1154528     010000\n" +
                        "  0.0000000E+00 0.0000000E+00 0.3225315E-01 -2.412454     0.0000000E+00 001000\n" +
                        "  0.0000000E+00 0.0000000E+00 0.4267180    -0.9126992     0.0000000E+00 000100\n" +
                        "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00  1.000000     000010\n" +
                        " -0.1768548    -0.2827129     0.0000000E+00 0.0000000E+00 0.2495793     000001\n" +
                        "   3.588874      4.590501     0.0000000E+00 0.0000000E+00-0.1347208     200000\n" +
                        "   2.180121      2.462184     0.0000000E+00 0.0000000E+00 0.2528623     110000\n" +
                        "  0.1071956     0.1649161E-01 0.0000000E+00 0.0000000E+00-0.4427173     020000\n" +
                        "  0.0000000E+00 0.0000000E+00  1.147131      3.476974     0.0000000E+00 101000\n" +
                        "  0.0000000E+00 0.0000000E+00  1.302242      3.086489     0.0000000E+00 011000\n" +
                        "   1.286195      2.655721     0.0000000E+00 0.0000000E+00 -1.065778     002000\n" +
                        "  0.0000000E+00 0.0000000E+00  1.008008      3.066368     0.0000000E+00 100100\n" +
                        "  0.0000000E+00 0.0000000E+00  1.004786      2.530505     0.0000000E+00 010100\n" +
                        "   2.385299      4.541351     0.0000000E+00 0.0000000E+00 -1.036579     001100\n" +
                        "  0.9051628     0.9379569     0.0000000E+00 0.0000000E+00-0.8368698E-01 100001\n" +
                        " -0.4353227     0.2443004     0.0000000E+00 0.0000000E+00 0.3465628E-01 010001\n" +
                        "  0.0000000E+00 0.0000000E+00 0.7897856     0.4380811     0.0000000E+00 001001\n" +
                        "   1.088300      1.960850     0.0000000E+00 0.0000000E+00-0.4719440     000200\n" +
                        "  0.0000000E+00 0.0000000E+00 0.3594429      1.259862     0.0000000E+00 000101\n" +
                        "  0.1496099     0.8701740E-01 0.0000000E+00 0.0000000E+00-0.2214629     000002"
        );
    }

    //---------------------------------------------------------

    private Cct getCct() {
        Cct dipoleCct = createDipoleCct();
        Cct agCct = createAgCct();

        return CctFactory.combineCct(dipoleCct, agCct);
    }

    private Trajectory getTrajectory() {
        return TrajectoryFactory.setStartingPoint(trajectoryBigR, -0.5)
                .setStraightLine(0.5, Vector2.yDirect())
                .addArcLine(trajectoryBigR, false, dipoleCctAngle)
                .addStraitLine(0.5);
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
    private double dipoleCctA1Inner = Math.pow(dipoleCctSmallRInner, 2) * 0.225;
    private double dipoleCctA1Outer = -Math.pow(dipoleCctSmallROuter, 2) * 0.225;
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
