package cn.edu.hust.zrx.cct.study.超导机架AGCCT单独建模;

import cn.edu.hust.zrx.cct.Logger;
import cn.edu.hust.zrx.cct.base.BaseUtils;
import cn.edu.hust.zrx.cct.base.CoordinateSystem3d;
import cn.edu.hust.zrx.cct.base.cct.CctFactory;
import cn.edu.hust.zrx.cct.base.line.Trajectory;
import cn.edu.hust.zrx.cct.base.line.TrajectoryFactory;
import cn.edu.hust.zrx.cct.base.particle.*;
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
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Description
 * 22.5 度 CCT 建模
 * 2020年3月19日
 * <p>
 * Data
 * 22:10
 *
 * @author zrx
 * @version 1.0
 */

public class A0319AGCCT225修改四极CCT孔径以补充二极场 {

    //    @run
    public void 二机场分布画图() {
        CctFactory.Cct cct = getCct();
        Trajectory trajectory = getTrajectory();

        List<Point2> Bz = cct.magnetBzAlongTrajectory(trajectory, 1 * MM);
        List<Point2> Gz = cct.magnetGradientAlongTrajectoryFast(trajectory, 1 * MM, 5 * MM);

        Plot2d.plot2(Bz, Plot2d.RED_LINE);

        List<Point2> scoffMagnetDistribution = CctFactory.scoffMagnetDistribution(
                0,
                Point2.create(0, 1),
                Point2.create(-3.24, BaseUtils.Converter.angleToRadian(dipoleCctAngle) * trajectoryBigR),
                Point2.create(0, 1)
        );

        Logger.getLogger().info("scoffMagnetDistribution = " + scoffMagnetDistribution);

        Plot2d.plot2(scoffMagnetDistribution, Plot2d.GREY_DASH);

        Plot2d.info("s/m", "Bz/T", "", 20);

        Plot2d.showThread();
    }

    //    @run
    public void 单粒子跟踪() {
        Trajectory trajectory = getTrajectory();
        CctFactory.Cct cct = getCct();

        RunningParticle idealParticle = ParticleFactory.createIdealProtonAtTrajectory250MeV(trajectory);

        List<Point3> runTraj = ParticleRunner.run(idealParticle, cct, trajectory.getLength(), 1 * MM);

        trajectory.plot3d(Plot2d.BLACK_LINE);
        Plot3d.plot3(runTraj, Plot2d.RED_LINE);

        Plot3d.setCube(trajectoryBigR * 1.1);
        Plot3d.showThread();

        Vector3 velocityIdealParticle = idealParticle.getVelocity().copy().normalSelf();
        Logger.getLogger().info("velocityIdealParticle = " + velocityIdealParticle);

        Vector3 velocityIdealTrajectory = trajectory.directAt(trajectory.getLength()).toVector3().normalSelf();
        Logger.getLogger().info("velocityIdealTrajectory = " + velocityIdealTrajectory);
    }

    //        @run
    public void 多粒子跟踪() {

        CctFactory.Cct cct = getCct();
        logPastTime("cct");
        Trajectory trajectory = getTrajectory();
        logPastTime("traj");

        RunningParticle idealP = ParticleFactory.createIdealProtonAtTrajectory250MeV(trajectory);
        CoordinateSystem3d coordinateSystem3dStart = idealP.computeNaturalCoordinateSystem();

        boolean xPlane = false;
        List<PhaseSpaceParticle> spaceParticles;
        if (xPlane) {
            spaceParticles = PhaseSpaceParticles.phaseSpaceParticlesAlongPositiveEllipseInXXpPlane(
                    3.5 * MM, 7.5 * MM, 0.0, 160);
            Plot2d.plotPoints(PhaseSpaceParticles.projectionToXXpPlane(spaceParticles), Plot2d.RED_POINT);
        } else {
            spaceParticles = PhaseSpaceParticles.phaseSpaceParticlesAlongPositiveEllipseInYYpPlane(
                    3.5 * MM, 7.5 * MM, 0.0, 160);
            Plot2d.plotPoints(PhaseSpaceParticles.projectionToYYpPlane(spaceParticles), Plot2d.RED_POINT);
        }

        List<RunningParticle> particles =
                ParticleFactory.createParticlesFromPhaseSpaceParticle(idealP, coordinateSystem3dStart, spaceParticles);

        logPastTime("运行理想粒子");
        List<Point3> idealTraj = ParticleRunner.run(idealP, cct, trajectory.getLength(), 1 * MM);

        logPastTime("理想粒子运行完毕，开始运行粒子群");
        List<List<Point3>> particlesTrajs = ParticleRunner.runThread(particles, cct, trajectory.getLength(), 1 * MM);


        logPastTime("粒子群运行完毕，开始绘图");
        List<PhaseSpaceParticle> spaceParticlesEnd =
                PhaseSpaceParticles.phaseSpaceParticlesFromRunningParticles(
                        idealP, idealP.computeNaturalCoordinateSystem(), particles);

        if (xPlane) {
            Plot2d.plotPoints(PhaseSpaceParticles.projectionToXXpPlane(spaceParticlesEnd), Plot2d.BLACK_POINT);
        } else {
            Plot2d.plotPoints(PhaseSpaceParticles.projectionToYYpPlane(spaceParticlesEnd), Plot2d.BLACK_POINT);
        }


        Plot3d.plot3(idealTraj, Plot2d.RED_LINE);
        particlesTrajs.forEach(particlesTraj -> Plot3d.plot3(particlesTraj, Plot2d.YELLOW_LINE));
        trajectory.plot3d(Plot2d.BLACK_LINE);
        logPastTime("绘图结束");

        Plot2d.equal();
        Plot2d.showThread();

        Plot3d.setCenter(trajectory.pointAt(trajectory.getLength() / 2).toPoint3(), trajectory.getLength() / 1.5);
        Plot3d.showThread();
    }

    @run(value = 2)
    public void AGCCT画图() {
        CctFactory.Cct agCct = createAgCct();

        agCct.plot3(Plot2d.RED_LINE, Plot2d.RED_LINE, Plot2d.RED_LINE, Plot2d.RED_LINE);

        Trajectory arc75 = TrajectoryFactory.setStartingPoint(
                Point2.create(trajectoryBigR, 0).toVector2().rotateSelf(-BaseUtils.Converter.angleToRadian(20)).toPoint2())
                .setStraightLine(0.001 * MM, Vector2.yDirect().rotateSelf(-BaseUtils.Converter.angleToRadian(20)))
                .addArcLine(trajectoryBigR, false, 40 + dipoleCctAngle);


        Trajectory arc74 = TrajectoryFactory.setStartingPoint(
                Point2.create(trajectoryBigR - 10 * MM, 0).toVector2().rotateSelf(-BaseUtils.Converter.angleToRadian(20)).toPoint2())
                .setStraightLine(0.001 * MM, Vector2.yDirect().rotateSelf(-BaseUtils.Converter.angleToRadian(20)))
                .addArcLine(trajectoryBigR - 10 * MM, false, 40 + dipoleCctAngle);

        arc75.plot3d(Plot2d.BLACK_LINE);
        arc74.plot3d(Plot2d.YELLOW_LINE);

        Plot3d.setCenter(arc75.pointAt(arc75.getLength() / 2).toPoint3(), trajectoryBigR);

        Plot3d.showThread();
    }

    @run(value = 3)
    public void 四极场CCT孔径扩大_使得二机场最小() {
        Trajectory trajectory = getTrajectory();

        // 只检查四极CCT的二机场
        boolean justHaveALook = true;

        // 只检查四极CCT的二机场
        if (justHaveALook) {
            CctFactory.Cct agCct = createAgCct();
            List<Point2> magnetBzAlongTrajectory = agCct.magnetBzAlongTrajectory(trajectory, 1 * MM);
            DoubleSummaryStatistics statistics = magnetBzAlongTrajectory.stream().
                    mapToDouble(Point2::getY).summaryStatistics();
            Logger.getLogger().info("最大磁场 = " + statistics.getMax());
            Logger.getLogger().info("最小磁场 = " + statistics.getMin());

//            magnetBzAlongTrajectory = Point2.convert(magnetBzAlongTrajectory,
//                    (x,y)->BaseUtils.Converter.radianToAngle((x-1)/trajectoryBigR),
//                    (x,y)->y);

            magnetBzAlongTrajectory = Point2.convert(magnetBzAlongTrajectory,
                    (x, y) -> x,
                    (x, y) -> y * 10000);//T Gs

            Plot2d.plot2(magnetBzAlongTrajectory, Plot2d.BLACK_LINE);

//            agCCTBigR = 0.7503030516431924;
            agCCTBigR = 0.750294;
            agCct = createAgCct();
            magnetBzAlongTrajectory = agCct.magnetBzAlongTrajectory(trajectory, 1 * MM);
            statistics = magnetBzAlongTrajectory.stream().
                    mapToDouble(Point2::getY).summaryStatistics();
            Logger.getLogger().info("最大磁场 = " + statistics.getMax());
            Logger.getLogger().info("最小磁场 = " + statistics.getMin());
            magnetBzAlongTrajectory = Point2.convert(magnetBzAlongTrajectory,
                    (x, y) -> x,
                    (x, y) -> y * 10000);//T Gs
            Plot2d.plot2(magnetBzAlongTrajectory, Plot2d.RED_LINE);


            Plot2d.info("s/m", "magnetic field/Gs", "", 18);

            Plot2d.showThread();
        } else {
            // 修改 AG-CCT 的半径，使得 轨道处 二机场幅值最小
            // 变动 +/- 10 MM
            final double bigR0 = agCCTBigR; // 0.75m
            List<BaseUtils.Content.BiContent<Double, Point3>> biContents = BaseUtils.Python
                    .linspaceStream(-10 * MM, 10 * MM, 16)
                    .parallel()
                    .map(delta -> delta + bigR0)
                    .mapToObj(bigR -> {

                        A0319AGCCT225修改四极CCT孔径以补充二极场 a0319AGCCT225 = new A0319AGCCT225修改四极CCT孔径以补充二极场();
                        a0319AGCCT225.agCCTBigR = bigR;
                        CctFactory.Cct agCct = a0319AGCCT225.createAgCct();

                        List<Point2> magnetBzAlongTrajectory = agCct.magnetBzAlongTrajectory(trajectory, 1 * MM);
                        DoubleSummaryStatistics statistics = magnetBzAlongTrajectory.stream().
                                mapToDouble(Point2::getY).summaryStatistics();
                        double min = statistics.getMin();
                        double max = statistics.getMax();
//                        double amplitude = Math.max(Math.abs(max), Math.abs(min));
                        double j积分场 = magnetBzAlongTrajectory.stream().mapToDouble(p -> p.y * MM).sum();
                        Logger.getLogger().info("bigR = " + bigR);
                        Logger.getLogger().info("j积分场 = " + j积分场);

                        return BaseUtils.Content.BiContent.create(bigR, Point3.create(max, min, j积分场));
                    })
                    .sorted(Comparator.comparingDouble(BaseUtils.Content.BiContent::getT1))
                    .collect(Collectors.toList());


            List<Point2> maxList = biContents.stream().map(bi -> Point2.create(bi.getT1(), bi.getT2().x)).collect(Collectors.toList());
            List<Point2> minList = biContents.stream().map(bi -> Point2.create(bi.getT1(), bi.getT2().y)).collect(Collectors.toList());
            List<Point2> 积分场List = biContents.stream().map(bi -> Point2.create(bi.getT1(), bi.getT2().z)).collect(Collectors.toList());

            Optional<Point2> min = 积分场List.stream().min(Comparator.comparingDouble(Point2::getY));
            min.ifPresent(m -> Logger.getLogger().info("m = {}", m));


//            amplitudeList = amplitudeList
//                    .stream()
//                    .map(p -> Point2.create(p.x * 1000, p.y * 10000))
//                    .collect(Collectors.toList());

//            Plot2d.plot2(maxList,Plot2d.BLACK_LINE);
//            Plot2d.plot2(minList,Plot2d.YELLOW_LINE);
            Plot2d.plot2(积分场List, Plot2d.RED_LINE);
            Logger.getLogger().info("积分场List = " + 积分场List);


            Plot2d.info("radius of AG-CCT/mm", "max magnetic field intensity along ideal trajectory/Gs",
                    "", 18);

            Plot2d.showThread();

            // 0.7503030516431924
            // 最小幅值二机场 的半径
        }


    }

    @run(value = 6,code = "a0319agcct225-0320-2007")
    public void 四极场CCT孔径扩大_使得二机场最小02() {
        Trajectory trajectory = getTrajectory();
        String[] strings = BaseUtils.Python.linspaceStream(0.0 * MM, 0.4 * MM, switcher.getSize())
                .parallel()
                .map(delta -> trajectoryBigR + delta)
                .mapToObj(bigR -> {
                    A0319AGCCT225修改四极CCT孔径以补充二极场 obj = new A0319AGCCT225修改四极CCT孔径以补充二极场();
                    obj.agCCTBigR = bigR;
                    CctFactory.Cct agCct = obj.createAgCct();
                    List<Point2> magnetBzAlongTrajectory = agCct.magnetBzAlongTrajectory(trajectory, 10 * MM);
                    return BaseUtils.Content.BiContent.create(bigR, magnetBzAlongTrajectory);
                })
                .collect(Collectors.toList())
                .stream()
                .sorted(Comparator.comparingDouble(BaseUtils.Content.BiContent::getT1))
                .peek(bi -> Logger.getLogger().info("R = {}", bi.getT1()))
                .peek(bi -> {
                    List<Point2> point2s = bi.getT2();
                    point2s = Point2.convert(point2s, (x, y) -> x, (x, y) -> y * 10000);
                    Plot2d.plot2(point2s, switcher.getAndSwitch());
                })
                .mapToDouble(BaseUtils.Content.BiContent::getT1)
                .mapToObj(r -> "cct radius " + ((int) (r * 100000)) / 100. + "mm")
                .collect(Collectors.toList())
                .toArray(new String[]{});

        Plot2d.info("s/m", "magnetic field/Gs", "", 18);

        Plot2d.legend(18, strings);
        Plot2d.showThread();
    }

    @run(5)
    public void reservedDecimalTest() {
        IntStream.range(0, 8).forEach(n ->
                Logger.getLogger().info("PI保留{}位小数为{}", n, BaseUtils.Doubles.reservedDecimal(Math.PI, n)));

        IntStream.range(0, 8).forEach(n ->
                Logger.getLogger().info("PI/10保留{}位小数为{}", n, BaseUtils.Doubles.reservedDecimal(Math.PI / 10, n)));

        IntStream.range(0, 8).forEach(n ->
                Logger.getLogger().info("PI/100保留{}位小数为{}", n, BaseUtils.Doubles.reservedDecimal(Math.PI / 100, n)));

        IntStream.range(0, 8).forEach(n ->
                Logger.getLogger().info("PI*10保留{}位小数为{}", n, BaseUtils.Doubles.reservedDecimal(Math.PI * 10, n)));
    }


    //    @run //没有发现性能问题...
    public void 性能测试() {

        final int THREAD_NUM = 16;
        final int LENGTH = 100 * 100 * 100;

        timer.printPeriodAfterInitial(Logger.getLogger());
        IntStream.range(0, THREAD_NUM).parallel().forEach(ignore -> {
            List<String> list = new ArrayList<>();
            for (int i = 0; i < LENGTH; i++) {
                list.add(fun(i));
            }
            System.out.println(list.contains("1.1"));
        });

        timer.printPeriodAfterInitial(Logger.getLogger());
        IntStream.range(0, THREAD_NUM).parallel().forEach(ignore -> {

            List<String> list = IntStream.range(0, LENGTH).mapToObj(this::fun).collect(Collectors.toList());
            System.out.println(list.contains("1.1"));
        });
        timer.printPeriodAfterInitial(Logger.getLogger());

    }

    private String fun(int num) {
        double temp = num * 1.1;
        for (int i = 0; i < 100 * 100; i++) {
            temp *= i * 1.1;
        }
        return String.valueOf(temp);
    }

    //---------------------------------------------------------

    private CctFactory.Cct getCct() {
        CctFactory.Cct dipoleCct = createDipoleCct();
        CctFactory.Cct agCct = createAgCct();

        return CctFactory.combineCct(dipoleCct, agCct);
    }

    private Trajectory getTrajectory() {
        return TrajectoryFactory.setStartingPoint(trajectoryBigR, -1)
                .setStraightLine(1, Vector2.yDirect())
                .addArcLine(trajectoryBigR, false, dipoleCctAngle)
                .addStraitLine(1.0);
    }

    private CctFactory.Cct createAgCct() {
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

    private CctFactory.Cct createDipoleCct() {
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
    private double agCCTBigR = 750.2*MM;
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
