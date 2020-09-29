package cn.edu.hust.zrx.cct.study.旧机架研究.超导机架AGCCT单独建模;

import cn.edu.hust.zrx.cct.Logger;
import cn.edu.hust.zrx.cct.base.BaseUtils;
import cn.edu.hust.zrx.cct.base.cct.Cct;
import cn.edu.hust.zrx.cct.base.cct.CctFactory;
import cn.edu.hust.zrx.cct.base.line.Arcs;
import cn.edu.hust.zrx.cct.base.line.Trajectory;
import cn.edu.hust.zrx.cct.base.line.TrajectoryFactory;
import cn.edu.hust.zrx.cct.base.particle.ParticleFactory;
import cn.edu.hust.zrx.cct.base.particle.ParticleRunner;
import cn.edu.hust.zrx.cct.base.particle.RunningParticle;
import cn.edu.hust.zrx.cct.base.point.Point2;
import cn.edu.hust.zrx.cct.base.point.Point3;
import cn.edu.hust.zrx.cct.base.python.Plot2d;
import cn.edu.hust.zrx.cct.base.python.Plot3d;
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
import java.util.stream.Collectors;

/**
 * Description
 * B20200402完整前45度偏转段模型
 * <p>
 * Data
 * 0:37
 *
 * @author zrx
 * @version 1.0
 */
@SuppressWarnings("all")
public class B20200402完整前45度偏转段模型 {

    @run(1)
    public void 磁场分析() {
        Cct allCctIn45 = getAllCctIn45();
//        CctFactory.Cct allCctIn45 = getCct2();

        Logger.getLogger().info("allCctIn45.cctLayerNumber() = " + allCctIn45.cctLayerNumber());


        Trajectory trajectory = getTrajectory();

//        double length = trajectory.getLength();
//        Logger.getLogger().info("length = " + length);
//
//        Point3 point3 = trajectory.pointAt(length).toPoint3();
//
//        Vector3 magnetAt = allCctIn45.magnetAt(point3);
//        Logger.getLogger().info("magnetAt = " + magnetAt);

//        trajectory.plot3d();
//        Plot3d.plotPoint(point3,Plot2d.RED_POINT);


//        List<Point2> magnetBzAlongTrajectory = allCctIn45.magnetBzAlongTrajectory(trajectory, 100*MM);
//        List<Point2> gradientAlongTrajectory = allCctIn45.magnetGradientAlongTrajectoryFast(trajectory, 100*MM, 3 * MM);

        List<List<Point2>> magnetBz = allCctIn45.getSoleLayerCctList()
                .stream()
                .parallel()
                .map(soleLayerCct -> soleLayerCct.magnetBzAlongTrajectory(trajectory, MM))
                .collect(Collectors.toList());

        List<Point2> magnetBzAlongTrajectory = Point2.addOnY(magnetBz);


        List<List<Point2>> magnetGra = allCctIn45.getSoleLayerCctList()
                .stream()
                .parallel()
                .map(soleLayerCct -> soleLayerCct.magnetGradientAlongTrajectoryFast(trajectory, MM, 3 * MM))
                .collect(Collectors.toList());

        List<Point2> gradientAlongTrajectory = Point2.addOnY(magnetGra);

        Plot2d.plot2(magnetBzAlongTrajectory, Plot2d.RED_LINE);
        Plot2d.plot2(gradientAlongTrajectory, Plot2d.BLUE_LINE);

//        trajectory.plot3d();
//        allCctIn45.plot3();
//
//        Plot3d.setCenter(trajectory.midPoint().toPoint3(),trajectory.getLength()/1.5);
//        Plot3d.showThread();

        Plot2d.showThread();
//        Plot3d.showThread();
    }

    @run(-2)
    public void 粒子跟踪() {
        Cct allCctIn45 = getAllCctIn45();
        Trajectory trajectory = getTrajectory();

        RunningParticle rp = ParticleFactory.createIdealProtonAtTrajectory250MeV(trajectory);

        List<Point3> run = ParticleRunner.run(rp, allCctIn45, trajectory.getLength(), MM);

        Logger.getLogger().info("run.size() = " + run.size());
    }

    @run(-1000)
    public void ArcsTest() {
        Point2 p1 = Point2.create(0, 0);
        Point2 p2 = Point2.create(1, 0);
        Point2 p3 = Point2.create(1, 1);

        Point2 center = Arcs.center(p1, p2, p3);
        Logger.getLogger().info("center = " + center);

        double r = Vector2.from(center).to(p1).length();
        Logger.getLogger().info("r = " + r);

        Plot2d.plotPoint(p1, Plot2d.RED_POINT);
        Plot2d.plotPoint(p2, Plot2d.RED_POINT);
        Plot2d.plotPoint(p3, Plot2d.RED_POINT);
        Plot2d.plotPoint(center, Plot2d.RED_LINE);

        Plot2d.plot2(Arcs.circle(center, r, 360), Plot2d.BLACK_LINE);


        Plot2d.showThread();
    }

    @run(value = 3,code = "20200402003")
    public void 画图() {
        Cct allCctIn45 = getAllCctIn45();

        List<QsHardPlaneMagnet> qs = get3QS();
        Trajectory trajectory = getTrajectory();
        double length = trajectory.getLength();

        trajectory.plot2d(MM, Plot2d.BLACK_LINE);

        qs.forEach(q -> q.plot2d(Plot2d.RED_LINE));

        Plot2d.equal();

        Plot2d.showThread();


        trajectory.plot3d();
        qs.forEach(q -> q.plot3d(Plot2d.RED_LINE));

        Plot3d.setCenter(trajectory.pointAt(length / 2).toPoint3(), length);
        allCctIn45.plot3(
                Plot2d.RED_LINE,
                Plot2d.RED_LINE,
                Plot2d.RED_LINE,
                Plot2d.RED_LINE,
                Plot2d.BLUE_LINE,
                Plot2d.BLUE_LINE,

                Plot2d.RED_LINE,
                Plot2d.RED_LINE,
                Plot2d.RED_LINE,
                Plot2d.RED_LINE,
                Plot2d.BLUE_LINE,
                Plot2d.BLUE_LINE
        );

        Plot3d.offAxisAndBgColor();

        Plot3d.showThread();
    }

    private List<QsHardPlaneMagnet> get3QS() {
        //double length_m, double gradient_T_per_m,
        //double second_gradient_T_per_m2, double aperture_radius_mm,
        //Point2 location, Vector2 direct
        QsHardPlaneMagnet QS11 = QsHardPlaneMagnet.create(QS1_LEN, QS1_GRADIENT, QS1_SECOND_GRADIENT, QS1_APERTURE_MM,
                getTrajectory().pointAt(DL1 + CCT_LENGTH + GAP1), getTrajectory().directAt(DL1 + CCT_LENGTH + GAP1));

        QsHardPlaneMagnet QS2 = QsHardPlaneMagnet.create(QS2_LEN, QS2_GRADIENT, QS2_SECOND_GRADIENT, QS2_APERTURE_MM,
                getTrajectory().pointAt(DL1 + CCT_LENGTH + GAP1 + QS1_LEN + GAP2),
                getTrajectory().directAt(DL1 + CCT_LENGTH + GAP1 + QS1_LEN + GAP2));


        QsHardPlaneMagnet QS12 = QsHardPlaneMagnet.create(QS1_LEN, QS1_GRADIENT, QS1_SECOND_GRADIENT, QS1_APERTURE_MM,
                getTrajectory().pointAt(DL1 + CCT_LENGTH + GAP1 + QS1_LEN + GAP2 + QS2_LEN + GAP2),
                getTrajectory().directAt(DL1 + CCT_LENGTH + GAP1 + QS1_LEN + GAP2 + QS2_LEN + GAP2));

        return List.of(QS11, QS2, QS12);
    }


    private Cct getAllCctIn45() {
        return CctFactory.combineCct(getCct1(), getCct2());
    }

    private Cct getCct1() {
        Trajectory trajectory = getTrajectory();

        Cct cct = getCct();

        //        Point2 center = Arcs.center(
//                trajectory.pointAt(DL1 + CCT_LENGTH + BETWEEN_CCT225),
//                trajectory.pointAt(DL1 + CCT_LENGTH + BETWEEN_CCT225 + CCT_LENGTH / 2),
//                trajectory.pointAt(DL1 + CCT_LENGTH + BETWEEN_CCT225 + CCT_LENGTH)
//        );
//
//
//        CctFactory.Cct cct2 = CctFactory.positionInXYPlane(cct,center,BaseUtils.Converter.angleToRadian(-90+22.5));

        return CctFactory.positionInXYPlane(cct, Point2.create(DL1, trajectoryBigR), BaseUtils.Converter.angleToRadian(-90));

    }


    private Cct getCct2() {
        Trajectory trajectory = getTrajectory();

        Cct dipoleCct = createDipoleCct();
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
        Cct agCct = CctFactory.createAgCct(agCctSmallRInner, agCctSmallROuter, agCCTBigR,
                new double[]{agCctAngle1, agCctAngle0},
                new int[]{agCctWindingNumber1, agCctWindingNumber0},
                agCctA0Inner, agCctA1Inner, agCctA2Inner, -agCctIInner,
                agCctA0Outer, agCctA1Outer, agCctA2Outer, -agCctIOuter,
                numberPerWinding);

        Cct cct = CctFactory.combineCct( agCct,dipoleCct);

        //CctFactory.Cct cct1 = CctFactory.positionInXYPlane(cct, Point2.create(DL1, trajectoryBigR), BaseUtils.Converter.angleToRadian(-90));

        Point2 center = Arcs.center(
                trajectory.pointAt(DL1 + CCT_LENGTH + BETWEEN_CCT225),
                trajectory.pointAt(DL1 + CCT_LENGTH + BETWEEN_CCT225 + CCT_LENGTH / 2),
                trajectory.pointAt(DL1 + CCT_LENGTH + BETWEEN_CCT225 + CCT_LENGTH)
        );


        return CctFactory.positionInXYPlane(cct, center, BaseUtils.Converter.angleToRadian(-90 + 22.5));


    }


    //---------------------------------------------------------

    private Cct getCct() {
        Cct dipoleCct = createDipoleCct();
        Cct agCct = createAgCct();

        return CctFactory.combineCct(agCct, dipoleCct);
    }

    private Trajectory getTrajectory() {
        return TrajectoryFactory.setStartingPoint(Point2.origin())
                .setStraightLine(DL1, Vector2.xDirect())
                .addArcLine(0.75, false, 22.5)
                .addStraitLine(0.15)//gap1
                .addStraitLine(0.2)//QS1_LEN
                .addStraitLine(0.15)//GAP2
                .addStraitLine(0.2)//QS2_LEN
                .addStraitLine(0.15)//GAP2
                .addStraitLine(0.2)//QS1_LEN
                .addStraitLine(0.15)//gap1
                .addArcLine(0.75, false, 22.5)
                .addStraitLine(DL1);

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

    // QS 123
    private double QS1_GRADIENT = 53.3; //T m-1
    private double QS1_SECOND_GRADIENT = 211.1; //T m-2
    private double QS2_GRADIENT = -37.7; //T m-1
    private double QS2_SECOND_GRADIENT = 355.6; //T m-2
    private double QS1_APERTURE_MM = 30;
    private double QS2_APERTURE_MM = 30;

    // 偏转半径
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
    // 长度
    private double DL1 = 1.1759;
    private double GAP1 = 0.15;
    private double QS1_LEN = 0.2;
    private double QS2_LEN = 0.2;
    private double GAP2 = 0.15;
    private double BETWEEN_CCT225 = GAP1 * 2 + QS1_LEN * 2 + GAP2 * 2 + QS2_LEN;
    private final double CCT_LENGTH = trajectoryBigR * BaseUtils.Converter.angleToRadian(dipoleCctAngle);
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
