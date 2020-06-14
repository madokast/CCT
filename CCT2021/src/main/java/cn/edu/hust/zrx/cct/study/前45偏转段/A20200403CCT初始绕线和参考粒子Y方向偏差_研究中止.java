package cn.edu.hust.zrx.cct.study.前45偏转段;

import cn.edu.hust.zrx.cct.Logger;
import cn.edu.hust.zrx.cct.base.BaseUtils;
import cn.edu.hust.zrx.cct.base.cct.*;
import cn.edu.hust.zrx.cct.base.line.Arcs;
import cn.edu.hust.zrx.cct.base.line.Trajectory;
import cn.edu.hust.zrx.cct.base.line.TrajectoryFactory;
import cn.edu.hust.zrx.cct.base.particle.ParticleFactory;
import cn.edu.hust.zrx.cct.base.particle.ParticleRunner;
import cn.edu.hust.zrx.cct.base.particle.RunningParticle;
import cn.edu.hust.zrx.cct.base.point.Point2;
import cn.edu.hust.zrx.cct.base.point.Point3;
import cn.edu.hust.zrx.cct.base.python.Plot2d;
import cn.edu.hust.zrx.cct.base.opticsMagnet.qs.QsHardPlaneMagnet;
import cn.edu.hust.zrx.cct.base.vector.Vector2;
import cn.edu.hust.zrx.cct.base.vector.Vector3;

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
 * A20200403CCT初始绕线和参考粒子Y方向偏差
 * 完成了前45度建模
 * 主要问题是Y方向偏差
 * <p>
 * Data
 * 9:49
 *
 * @author zrx
 * @version 1.0
 */
@SuppressWarnings("all")
public class A20200403CCT初始绕线和参考粒子Y方向偏差_研究中止 {

    @run(1)
    public void 参考粒子Y方向() {
        Cct cct1 = getCct1();
        List<Point2> yDirectDisplacement = referenceParticleYDirectDisplacement(cct1, DL1 + CCT_LENGTH + BETWEEN_CCT225);

        Plot2d.plot2(yDirectDisplacement, Plot2d.BLACK_LINE);

        Plot2d.showThread();
    }

    @run(value = 2, code = "验证通过")
    public void 新CCT建模增加startPhi验证一下() {
        Cct cct1_old = getCct1();

        Cct cct1_new = getCct1(0, 0);

        List<Point2> yDirectDisplacement_old = referenceParticleYDirectDisplacement(cct1_old, DL1 + CCT_LENGTH + BETWEEN_CCT225);
        List<Point2> yDirectDisplacement_new = referenceParticleYDirectDisplacement(cct1_new, DL1 + CCT_LENGTH + BETWEEN_CCT225);

        Plot2d.plot2(yDirectDisplacement_old, Plot2d.BLACK_LINE);
        Plot2d.plot2(yDirectDisplacement_new, Plot2d.YELLOW_LINE);

        Plot2d.showThread();
    }

    @run(value = 3,code = "没什么影响？")
    public void 开始调整startPhi同时调整() {


        String[] le = BaseUtils.Python.linspaceStream(0, 360, switcher.getSize() + 1)
                .limit(switcher.getSize())
                .map(BaseUtils.Converter::angleToRadian)
                .parallel()
                // 构建 cct 并tracking
                .mapToObj(phi -> {
                    Cct cct1 = getCct1(0, phi);
                    List<Point2> yDirectDisplacement = referenceParticleYDirectDisplacement(cct1, DL1 + CCT_LENGTH + BETWEEN_CCT225);
                    yDirectDisplacement = Point2.convert(yDirectDisplacement, 1, 1 / MM);


                    return BaseUtils.Content.BiContent.create(phi, yDirectDisplacement);
                })
                // 停止平行并排序
                .collect(Collectors.toList())
                .stream()
                .sorted(Comparator.comparingDouble(BaseUtils.Content.BiContent::getT1))
                // 画图
                .peek(bi -> Plot2d.plot2(bi.getT2(), switcher.getAndSwitch()))
                // 拿到 legend
                .mapToDouble(BaseUtils.Content.BiContent::getT1)
                .map(BaseUtils.Converter::radianToAngle)
                .mapToObj(phi -> "startPhi = " + phi)
                .collect(Collectors.toList())
                .toArray(new String[]{});

        Plot2d.info("s/m","y/mm","",18);

        Plot2d.plotYLines(List.of(DL1,DL1+CCT_LENGTH,DL1),3.0,-0.5,Plot2d.GREY_DASH);

        Plot2d.legend(18, le);

        Plot2d.showThread();
    }

    @run(4)
    public void 左手侧磁场分析(){
        List<Point2> list = leftHandMagneticFieldAlongCct(
                getCct1(0, 0),
                getTrajectory(),
                DL1 + CCT_LENGTH + BETWEEN_CCT225
        );

        Plot2d.plot2(list,Plot2d.BLACK_LINE);

        Plot2d.showThread();
    }

    @run(5)
    public void 四层一层层分析(){

    }


    private List<Point2> referenceParticleYDirectDisplacement(
            MagnetAble element, double distance) {
        Trajectory trajectory = getTrajectory();
        RunningParticle rp = ParticleFactory.createIdealProtonAtTrajectory250MeV(trajectory);

        return ParticleRunner.runGetPoint3WithDistance(rp, element, distance, MM)
                .stream()
                .map(ParticleRunner.Point3WithDistance::getDistanceWithZ)
                .collect(Collectors.toList());
    }

    private List<Point2> leftHandMagneticFieldAlongCct(
            MagnetAble elements, Trajectory trajectory, double distance){
        final Vector3 Y = Vector3.getZDirect();

        return BaseUtils.Python.linspaceStream(0, distance, (int) (distance / MM))
                .mapToObj(s -> {
                    Point3 p = trajectory.pointAt(s).toPoint3();
                    Vector3 m = elements.magnetAt(p);

                    Vector3 z = trajectory.directAt(s).toVector3();
                    Vector3 left = Y.cross(z).changeLengthSelf(1);

                    return Point2.create(s, m.dot(left));
                }).collect(Collectors.toList());
    }

    //---------------------elements------------------------------

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
        return CctFactory.combineCct(getCct1(), getCct2());
    }

    private Cct getCct1() {
        Cct cct = getCct();

        return CctFactory.positionInXYPlane(cct, Point2.create(DL1, trajectoryBigR), BaseUtils.Converter.angleToRadian(-90));
    }

    private Cct getCct1(double dipoleCctStartPhiInner, double dipoleCctStartPhiOuter) {
        Cct cct = getCct(dipoleCctStartPhiInner, dipoleCctStartPhiOuter);

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

        Cct cct = CctFactory.combineCct(agCct, dipoleCct);

        //CctFactory.Cct cct1 = CctFactory.positionInXYPlane(cct, Point2.create(DL1, trajectoryBigR), BaseUtils.Converter.angleToRadian(-90));

        Point2 center = Arcs.center(
                trajectory.pointAt(DL1 + CCT_LENGTH + BETWEEN_CCT225),
                trajectory.pointAt(DL1 + CCT_LENGTH + BETWEEN_CCT225 + CCT_LENGTH / 2),
                trajectory.pointAt(DL1 + CCT_LENGTH + BETWEEN_CCT225 + CCT_LENGTH)
        );


        return CctFactory.positionInXYPlane(cct, center, BaseUtils.Converter.angleToRadian(-90 + 22.5));


    }


    private Cct getCct() {
        Cct dipoleCct = createDipoleCct();
        Cct agCct = createAgCct();

        return CctFactory.combineCct(agCct, dipoleCct);
    }


    private Cct getCct(double dipoleCctStartPhiInner, double dipoleCctStartPhiOuter) {
        Cct dipoleCct = createDipoleCct(dipoleCctStartPhiInner, dipoleCctStartPhiOuter);
        Cct agCct = createAgCct();

        return CctFactory.combineCct(agCct, dipoleCct);
    }

    // 22.5度 二极CCT 有绕线变量
    private Cct createDipoleCct(double dipoleCctStartPhiInner, double dipoleCctStartPhiOuter) {
        SoleLayerCct innerCct = CctFactory.createSoleLaterCct(dipoleCctSmallRInner, dipoleCctBigR,
                dipoleCctAngle, dipoleCctWindingNumber, dipoleCctA0Inner, dipoleCctA1Inner,
                dipoleCctA2Inner, dipoleCctIInner, numberPerWinding,
                dipoleCctStartPhiInner, 0.0, true);
        SoleLayerCct outerCct = CctFactory.createSoleLaterCct(dipoleCctSmallROuter, dipoleCctBigR,
                dipoleCctAngle, dipoleCctWindingNumber, dipoleCctA0Outer, dipoleCctA1Outer,
                dipoleCctA2Outer, dipoleCctIOuter, numberPerWinding,
                dipoleCctStartPhiOuter, 0.0, true);

        return Cct.getEmptyCct().
                addSoleLayerCct(innerCct).
                addSoleLayerCct(outerCct);
    }

    // 前45度偏转段完整轨迹
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

    // 22.5度 四极CCT 没有绕线变量
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

    // 22.5度 二极CCT 没有绕线变量
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


    private final BaseUtils.Switcher<String> switcher = BaseUtils.Switcher.creat(
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
    private final double CCT00_LENGTH = trajectoryBigR * BaseUtils.Converter.angleToRadian(agCctAngle0);
    private final double CCT01_LENGTH = trajectoryBigR * BaseUtils.Converter.angleToRadian(agCctAngle1);
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
