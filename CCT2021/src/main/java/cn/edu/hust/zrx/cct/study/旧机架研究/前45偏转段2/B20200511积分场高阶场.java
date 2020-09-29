package cn.edu.hust.zrx.cct.study.旧机架研究.前45偏转段2;

import cn.edu.hust.zrx.cct.Logger;
import cn.edu.hust.zrx.cct.advanced.PolynomialFitter;
import cn.edu.hust.zrx.cct.base.BaseUtils;
import cn.edu.hust.zrx.cct.base.cct.Cct;
import cn.edu.hust.zrx.cct.base.cct.CctFactory;
import cn.edu.hust.zrx.cct.base.cct.Elements;
import cn.edu.hust.zrx.cct.base.line.Arcs;
import cn.edu.hust.zrx.cct.base.line.Line2;
import cn.edu.hust.zrx.cct.base.line.Trajectory;
import cn.edu.hust.zrx.cct.base.line.TrajectoryFactory;
import cn.edu.hust.zrx.cct.base.point.Point2;
import cn.edu.hust.zrx.cct.base.python.Plot2d;
import cn.edu.hust.zrx.cct.base.opticsMagnet.qs.QsHardPlaneMagnet;
import cn.edu.hust.zrx.cct.base.vector.Vector2;
import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.FastFourierTransformer;

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
 * B20200511积分场高阶场
 * <p>
 * Data
 * 20:17
 *
 * @author zrx
 * @version 1.0
 */

public class B20200511积分场高阶场 {

    @run(1)
    public void 积分场0() {
        Trajectory testTrajectory = createTestTrajectory();

        int order = 10;

        {
            Cct dipoleCct = createDipoleCct(
                    dipoleCctSmallRInner, dipoleCctSmallROuter, dipoleCctBigR,
                    dipoleCctAngle, dipoleCctWindingNumber,
                    dipoleCctA0Inner, dipoleCctA1Inner, dipoleCctA2Inner, dipoleCctIInner,
                    dipoleCctA0Outer, dipoleCctA1Outer, dipoleCctA2Outer, dipoleCctIOuter,
                    numberPerWinding,
                    dipoleCctStartingθInner, dipoleCctStartingθOuter,
                    dipoleCctStartingφInner, dipoleCctStartingφOuter,
                    dipoleCctDirectθInner, dipoleCctDirectθOuter
            );


            final double RANGE = 6 * MM;

            List<Point2> collect = BaseUtils.Python.linspaceStream(-RANGE, RANGE, 64)
                    .parallel()
                    .mapToObj(d -> {
                        Line2 rightHandSideLine2 = testTrajectory.rightHandSideLine2(d);

                        double integrationField = dipoleCct.integrationField(rightHandSideLine2, MM);

                        return Point2.create(d / MM, integrationField);
                    })
                    .collect(Collectors.toList())
                    .stream()
                    .sorted()
                    .collect(Collectors.toList());

            Logger.getLogger().info("collect = " + collect);

//        Plot2d.plot2(collect);

            double[] fit = PolynomialFitter.build(order).fit(collect);

            Logger.getLogger().info("fit = " + Arrays.toString(fit));

            fit = BaseUtils.ArrayUtils.multiple(fit, 1 / fit[0]);


            Logger.getLogger().info("fit = " + Arrays.toString(fit));

            fit = BaseUtils.ArrayUtils.apply(fit, x -> -1 / Math.log10(Math.abs(x)));

            Logger.getLogger().info("fit = " + Arrays.toString(fit));

            Plot2d.harmonicAnalysisWithOut0(fit, Plot2d.BLACK_LINE, 0.15, 0.2);
        }

        {
            Cct dipoleCct = createDipoleCct(
                    dipoleCctSmallRInner, dipoleCctSmallROuter, dipoleCctBigR,
                    dipoleCctAngle, dipoleCctWindingNumber,
                    dipoleCctA0Inner, dipoleCctA1Inner * 0, dipoleCctA2Inner, dipoleCctIInner,
                    dipoleCctA0Outer, dipoleCctA1Outer * 0, dipoleCctA2Outer, dipoleCctIOuter,
                    numberPerWinding,
                    dipoleCctStartingθInner, dipoleCctStartingθOuter,
                    dipoleCctStartingφInner, dipoleCctStartingφOuter,
                    dipoleCctDirectθInner, dipoleCctDirectθOuter
            );


            final double RANGE = 6 * MM;

            List<Point2> collect = BaseUtils.Python.linspaceStream(-RANGE, RANGE, 64)
                    .parallel()
                    .mapToObj(d -> {
                        Line2 rightHandSideLine2 = testTrajectory.rightHandSideLine2(d);

                        double integrationField = dipoleCct.integrationField(rightHandSideLine2, MM);

                        return Point2.create(d / MM, integrationField);
                    })
                    .collect(Collectors.toList())
                    .stream()
                    .sorted()
                    .collect(Collectors.toList());

            Logger.getLogger().info("collect = " + collect);

//        Plot2d.plot2(collect);

            double[] fit = PolynomialFitter.build(order).fit(collect);

            Logger.getLogger().info("fit = " + Arrays.toString(fit));

            fit = BaseUtils.ArrayUtils.multiple(fit, 1 / fit[0]);


            Logger.getLogger().info("fit = " + Arrays.toString(fit));

            fit = BaseUtils.ArrayUtils.apply(fit, x -> -1 / Math.log10(Math.abs(x)));

            Logger.getLogger().info("fit = " + Arrays.toString(fit));

            Plot2d.harmonicAnalysisWithOut0(fit, Plot2d.RED_LINE, -0.15, 0.2);
        }

        Plot2d.showThread();
    }


    @run(5)
    public void 积分场1() {
        Trajectory testTrajectory = createTestTrajectory();

        Cct dipoleCct = createDipoleCct(
                dipoleCctSmallRInner, dipoleCctSmallROuter, dipoleCctBigR,
                dipoleCctAngle, dipoleCctWindingNumber,
                dipoleCctA0Inner, dipoleCctA1Inner, dipoleCctA2Inner, dipoleCctIInner,
                dipoleCctA0Outer, dipoleCctA1Outer, dipoleCctA2Outer, dipoleCctIOuter,
                numberPerWinding,
                dipoleCctStartingθInner, dipoleCctStartingθOuter,
                dipoleCctStartingφInner, dipoleCctStartingφOuter,
                dipoleCctDirectθInner, dipoleCctDirectθOuter
        );


        final double RANGE = 6 * MM;

        List<BaseUtils.Content.BiContent<Double, double[]>> multiplePoleMagnetAlongTrajectory =
                dipoleCct.multiplePoleMagnetAlongTrajectory(testTrajectory, MM, RANGE, 5, 10);


        final double delta = multiplePoleMagnetAlongTrajectory.get(1).getT1() - multiplePoleMagnetAlongTrajectory.get(0).getT1();

        double[] sum = BaseUtils.ListUtils.averageSum(
                multiplePoleMagnetAlongTrajectory,
                (bi1, bi2) -> {
                    double[] bi1T2 = bi1.getT2();
                    double[] bi2T2 = bi2.getT2();

                    Double d = bi1.getT1();

                    return BaseUtils.Content.BiContent.create(d, BaseUtils.ArrayUtils.add(bi1T2, bi2T2));
                },
                (bi, a) -> {
                    Double t1 = bi.getT1();
                    double[] t2 = bi.getT2();

                    return BaseUtils.Content.BiContent.create(t1, BaseUtils.ArrayUtils.multiple(t2, a));
                }
        ).getT2();

        for (int i = 1; i < sum.length; i++) {
            sum[i] *= Math.pow(1 / MM, i);
        }

        double[] normal = BaseUtils.ArrayUtils.multiple(sum, 1 / sum[0]);

        Logger.getLogger().info("normal = " + Arrays.toString(normal));

        Plot2d.harmonicAnalysisWithOut0(normal, Plot2d.BLACK_LINE, 0, 0.5);

        Plot2d.showThread();
    }


    @run(3)
    public void test() {

        List<Point2> collect = BaseUtils.Python.linspaceStream(0, 5, 10)
                .mapToObj(s -> Point2.create(s, s * 2 + s * s))
                .collect(Collectors.toList());

        double[] fit = PolynomialFitter.build(5).fit(collect);

        Logger.getLogger().info("fit = " + Arrays.toString(fit));

    }

    @run(-20)
    public void 四极场分析() {
        Trajectory testTrajectory = createTestTrajectory();

        Cct dipoleCct = createDipoleCct(
                dipoleCctSmallRInner, dipoleCctSmallROuter, dipoleCctBigR,
                dipoleCctAngle, dipoleCctWindingNumber,
                dipoleCctA0Inner, dipoleCctA1Inner, dipoleCctA2Inner, dipoleCctIInner,
                dipoleCctA0Outer, dipoleCctA1Outer, dipoleCctA2Outer, dipoleCctIOuter,
                numberPerWinding,
                dipoleCctStartingθInner, dipoleCctStartingθOuter,
                dipoleCctStartingφInner, dipoleCctStartingφOuter,
                dipoleCctDirectθInner, dipoleCctDirectθOuter
        );


        List<Point2> after = dipoleCct.magnetGradientAlongTrajectoryFast(testTrajectory, MM, MM);

        Cct dipoleCctBefore = createDipoleCct(
                dipoleCctSmallRInner, dipoleCctSmallROuter, dipoleCctBigR,
                dipoleCctAngle, dipoleCctWindingNumber,
                dipoleCctA0Inner, 0, dipoleCctA2Inner, dipoleCctIInner,
                dipoleCctA0Outer, 0, dipoleCctA2Outer, dipoleCctIOuter,
                numberPerWinding,
                dipoleCctStartingθInner, dipoleCctStartingθOuter,
                dipoleCctStartingφInner, dipoleCctStartingφOuter,
                dipoleCctDirectθInner, dipoleCctDirectθOuter
        );

        List<Point2> before = dipoleCctBefore.magnetGradientAlongTrajectoryFast(testTrajectory, MM, MM);

        Plot2d.plot2(after, Plot2d.BLACK_LINE);
        Plot2d.plot2(before, Plot2d.RED_LINE);


        Plot2d.showThread();

    }

    @run(100)
    public void 新的谐波分析(){
        FastFourierTransformer transformer = new FastFourierTransformer(DftNormalization.STANDARD);


    }


    // ------------------------------------------

    private Elements getElementsOfAll() {
        List<QsHardPlaneMagnet> qs = get3QS();
        Cct allCctIn45 = getAllCctIn45();

        Elements elements = Elements.empty();
        qs.forEach(elements::addElement);
        allCctIn45.getSoleLayerCctList().forEach(elements::addElement);

        return elements;
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
        return CctFactory.combineCct(getCct1(), getCctSymmetryCct1());
    }


    private Cct getCct1() {
        Trajectory trajectory = getTrajectory();

        Cct cct = getCct();


        return CctFactory.positionInXYPlane(cct, Point2.create(DL1, trajectoryBigR), BaseUtils.Converter.angleToRadian(-90));

    }

    private Cct getCct2() {

        Trajectory trajectory = getTrajectory();

        Cct dipoleCct = createDipoleCct(
                dipoleCctSmallRInner, dipoleCctSmallROuter, dipoleCctBigR,
                dipoleCctAngle, dipoleCctWindingNumber,
                dipoleCctA0Inner, dipoleCctA1Inner, dipoleCctA2Inner, dipoleCctIInner,
                dipoleCctA0Outer, dipoleCctA1Outer, dipoleCctA2Outer, dipoleCctIOuter,
                numberPerWinding,
                dipoleCctStartingθInner, dipoleCctStartingθOuter,
                dipoleCctStartingφInner, dipoleCctStartingφOuter,
                dipoleCctDirectθInner, dipoleCctDirectθOuter
        );

        Cct agCct = CctFactory.createAgCct(agCctSmallRInner, agCctSmallROuter, agCCTBigR,
                new double[]{agCctAngle1, agCctAngle0},
                new int[]{agCctWindingNumber1, agCctWindingNumber0},
                agCctA0Inner, agCctA1Inner, agCctA2Inner, -agCctIInner,
                agCctA0Outer, agCctA1Outer, agCctA2Outer, -agCctIOuter,
                numberPerWinding, agCctStartingθInner, agCctStartingθOuter,
                agCctStartingφInner, agCctStartingφOuter,
                agCctDirectθInner, agCctDirectθOuter
        );

        Cct cct = CctFactory.combineCct(agCct, dipoleCct);

        //CctFactory.Cct cct1 = CctFactory.positionInXYPlane(cct, Point2.create(DL1, trajectoryBigR), BaseUtils.Converter.angleToRadian(-90));

        Point2 center = Arcs.center(
                trajectory.pointAt(DL1 + CCT_LENGTH + BETWEEN_CCT225),
                trajectory.pointAt(DL1 + CCT_LENGTH + BETWEEN_CCT225 + CCT_LENGTH / 2),
                trajectory.pointAt(DL1 + CCT_LENGTH + BETWEEN_CCT225 + CCT_LENGTH)
        );


        return CctFactory.positionInXYPlane(cct, center, BaseUtils.Converter.angleToRadian(-90 + 22.5));


    }

    private Cct getCctSymmetryCct1() {
        Trajectory trajectory = getTrajectory();
        return CctFactory.symmetryInXYPlaneByLine(
                getCct1(),
                trajectory.pointAt(DL1 + CCT_LENGTH + GAP1 + QS1_LEN + GAP2 + QS2_LEN / 2),
                trajectory.directAt(DL1 + CCT_LENGTH + GAP1 + QS1_LEN + GAP2 + QS2_LEN / 2).rotateSelf(Math.PI / 2)
        );
    }


    private Cct getCct() {
        Cct dipoleCct = createDipoleCct(
                dipoleCctSmallRInner, dipoleCctSmallROuter, dipoleCctBigR,
                dipoleCctAngle, dipoleCctWindingNumber,
                dipoleCctA0Inner, dipoleCctA1Inner, dipoleCctA2Inner, dipoleCctIInner,
                dipoleCctA0Outer, dipoleCctA1Outer, dipoleCctA2Outer, dipoleCctIOuter,
                numberPerWinding,
                dipoleCctStartingθInner, dipoleCctStartingθOuter,
                dipoleCctStartingφInner, dipoleCctStartingφOuter,
                dipoleCctDirectθInner, dipoleCctDirectθOuter
        );
        Cct agCct = createAgCct(
                agCctSmallRInner, agCctSmallROuter, agCCTBigR,
                new double[]{agCctAngle0, agCctAngle1},
                new int[]{agCctWindingNumber0, agCctWindingNumber1},
                agCctA0Inner, agCctA1Inner, agCctA2Inner, agCctIInner,
                agCctA0Outer, agCctA1Outer, agCctA2Outer, agCctIOuter,
                numberPerWinding,
                agCctStartingθInner, agCctStartingθOuter,
                agCctStartingφInner, agCctStartingφOuter,
                agCctDirectθInner, agCctDirectθOuter
        );

        return CctFactory.combineCct(agCct, dipoleCct);
    }

    private Trajectory getTrajectory() {
        return TrajectoryFactory.setStartingPoint(Point2.origin())
                .setStraightLine(DL1, Vector2.xDirect())
                .addArcLine(trajectoryBigR, false, dipoleCctAngle)
                .addStraitLine(GAP1)//gap1
                .addStraitLine(QS1_LEN)//QS1_LEN
                .addStraitLine(GAP2)//GAP2
                .addStraitLine(QS2_LEN)//QS2_LEN
                .addStraitLine(GAP2)//GAP2
                .addStraitLine(QS1_LEN)//QS1_LEN
                .addStraitLine(GAP1)//gap1
                .addArcLine(trajectoryBigR, false, dipoleCctAngle)
                .addStraitLine(DL1);

    }

    private Cct createDipoleCct(
            double smallRInner, double smallROuter, double bigR, double angle, int windingNumber,
            double a0BipolarInner, double a1QuadrupleInner, double a2SextupleInner, double IInner,
            double a0BipolarOuter, double a1QuadrupleOuter, double a2SextupleOuter, double IOuter,
            int numberPerWinding,
            double startingθInner, double startingθOuter,
            double startingφInner, double startingφOuter,
            boolean directθInner, boolean directθOuter
    ) {
        return CctFactory.createDipoleCctDetailed(
                smallRInner, smallROuter, bigR, angle, windingNumber,
                a0BipolarInner, a1QuadrupleInner, a2SextupleInner, IInner,
                a0BipolarOuter, a1QuadrupleOuter, a2SextupleOuter, IOuter,
                numberPerWinding,
                startingθInner, startingθOuter,
                startingφInner, startingφOuter,
                directθInner, directθOuter
        );
    }

    private Cct createAgCct(
            double smallRInner, double smallROuter, double bigR, double[] angles, int[] windingNumbers,
            double a0BipolarInners, double a1QuadrupleInners, double a2SextupleInners, double IInner,
            double a0BipolarOuters, double a1QuadrupleOuters, double a2SextupleOuters, double IOuter,
            int numberPerWinding,
            double startingθInner, double startingθOuter,
            double startingφInner, double startingφOuter,
            boolean directθInner, boolean directθOuter
    ) {
        return CctFactory.createAgCct(
                smallRInner, smallROuter, bigR, angles, windingNumbers,
                a0BipolarInners, a1QuadrupleInners, a2SextupleInners, IInner,
                a0BipolarOuters, a1QuadrupleOuters, a2SextupleOuters, IOuter,
                numberPerWinding,
                startingθInner, startingθOuter,
                startingφInner, startingφOuter,
                directθInner, directθOuter
        );
    }


    private Trajectory createTestTrajectory() {
        return TrajectoryFactory.setStartingPoint(trajectoryBigR, -1)
                .setStraightLine(1, Vector2.yDirect())
                .addArcLine(trajectoryBigR, false, dipoleCctAngle)
                .addStraitLine(1.0);
    }


    //--------------parameter------------------

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
    private double QS2_SECOND_GRADIENT = -355.6; //T m-2
    private double QS1_APERTURE_MM = 30;
    private double QS2_APERTURE_MM = 30;

    // 偏转半径
    private final double trajectoryBigR = 0.75;
    private final double dipoleCctBigR = 0.75;
    private double agCCTBigR = 750.2 * MM;

    // 初始绕线位置
    private double dipoleCctStartingθInner = 0.0;
    private double dipoleCctStartingθOuter = 0.0;
    private double dipoleCctStartingφInner = 0.0;
    private double dipoleCctStartingφOuter = 0.0;
    private boolean dipoleCctDirectθInner = true;
    private boolean dipoleCctDirectθOuter = false;
    private double agCctStartingθInner = 0.0; // 起始绕线方向
    private double agCctStartingθOuter = 0.0;
    private double agCctStartingφInner = 0.0;
    private double agCctStartingφOuter = 0.0;
    private boolean agCctDirectθInner = false;
    private boolean agCctDirectθOuter = true;

    // CCT孔径
    private final double agCctSmallRInner = 23.5 * MM;
    private final double agCctSmallROuter = 33.5 * MM;
    private final double dipoleCctSmallRInner = 43 * MM;
    private final double dipoleCctSmallROuter = 52 * MM;
    // CCT角度
    private final double dipoleCctAngle = 22.5;
    private final double agCctAngle0 = 9.1;
    private final double agCctAngle1 = 13.4;
    // CCT长度
    private double DL1 = 1.1759;
    private double GAP1 = 0.15;
    private double QS1_LEN = 0.2;
    private double QS2_LEN = 0.2;
    private double GAP2 = 0.15;
    private double BETWEEN_CCT225 = GAP1 * 2 + QS1_LEN * 2 + GAP2 * 2 + QS2_LEN;
    private final double CCT_LENGTH = trajectoryBigR * BaseUtils.Converter.angleToRadian(dipoleCctAngle);
    private final double CCT00_LENGTH = trajectoryBigR * BaseUtils.Converter.angleToRadian(agCctAngle0);
    private final double CCT01_LENGTH = trajectoryBigR * BaseUtils.Converter.angleToRadian(agCctAngle1);
    private double DL2 = 2.40;
    // CCT a0 a1 a2
    private double dipoleCctA0Inner = -dipoleCctSmallRInner * Math.sqrt(3);
    private double dipoleCctA0Outer = dipoleCctSmallROuter * Math.sqrt(3);
    private double dipoleCctA1Inner = Math.pow(dipoleCctSmallRInner, 2) * 0.225;
    private double dipoleCctA1Outer = -Math.pow(dipoleCctSmallROuter, 2) * 0.225;
    private double dipoleCctA2Inner = 0.0;
    private double dipoleCctA2Outer = 0.0;

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
    private double dipoleCctIInner = 9.898121356964111 * kA; // 求解获得
    private double dipoleCctIOuter = dipoleCctIInner;
    private double agCctIInner = -4618.911905272398;//* (1 - 0.010335570469798657);
    private double agCctIOuter = agCctIInner;

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
