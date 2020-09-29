package cn.edu.hust.zrx.cct.study.旧机架研究.超导机架AGCCT单独建模;

import cn.edu.hust.zrx.cct.Logger;
import cn.edu.hust.zrx.cct.base.BaseUtils;
import cn.edu.hust.zrx.cct.base.cct.Cct;
import cn.edu.hust.zrx.cct.base.cct.CctFactory;
import cn.edu.hust.zrx.cct.base.cct.SoleLayerCct;
import cn.edu.hust.zrx.cct.base.line.Trajectory;
import cn.edu.hust.zrx.cct.base.line.TrajectoryFactory;
import cn.edu.hust.zrx.cct.base.particle.ParticleFactory;
import cn.edu.hust.zrx.cct.base.particle.ParticleRunner;
import cn.edu.hust.zrx.cct.base.particle.RunningParticle;
import cn.edu.hust.zrx.cct.base.point.Point2;
import cn.edu.hust.zrx.cct.base.point.Point3;
import cn.edu.hust.zrx.cct.base.python.Plot2d;
import cn.edu.hust.zrx.cct.base.python.Plot3d;
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
 * 四极CCT建模225度0308
 * <p>
 * Data
 * 12:27
 * 2020年3月8日
 *
 * @author zrx
 * @version 1.0
 */
@SuppressWarnings("all")
public class 四极CCT建模225度0308 {
//    @run // 验证通过
    public void 四极CCT分开建模_验证公式正确性() {
        Trajectory trajectory = getTrajectory();

        //     * @param smallR           CCT半孔径
        //     * @param bigR             弯曲半径、即理想粒子的半径
        //     * @param angle            弯曲角度。典型值66.7度
        //     * @param windingNumber    匝数
        //     * @param a0Bipolar        a0 产生二极场
        //     * @param a1Quadruple      a1 产生四极场
        //     * @param a2Sextuple       a2 产生六极场
        //     * @param I                电流
        //     * @param numberPerWinding 每匝分段
        //     * @param startingθ        起始 theta 类似于直线cct中的z
        //     * @param startingφ        起始 φ 绕线点
        //     * @param directθ          绕线方法，z 正方向？还是负方向
        //     * @return 单层CCT

        //r = 23.5mm 内层agcct
        SoleLayerCct agCctInner0 = CctFactory.createSoleLaterCct(agCctSmallRInner, bigR, agCctAngle0, agCctWindingNumber0,
                agCctA0Inner, agCctA1Inner , agCctA2Inner, agCctIInner, numberPerWinding, 0, 0, true);
        SoleLayerCct agCctInner1 = CctFactory.createSoleLaterCct(agCctSmallRInner, bigR, agCctAngle1, agCctWindingNumber1,
                agCctA0Inner, agCctA1Inner , agCctA2Inner, agCctIInner, numberPerWinding,
                agCctWindingNumber0*2*Math.PI, BaseUtils.Converter.angleToRadian(agCctAngle0), false);
        //(agCctWindingNumber0+agCctWindingNumber1)*2* Math.PI
        //            startingφ += windingNumbers[i - 1] * 2 * Math.PI
        //                    / Math.abs(cctLine2s.cctLine2s.get(cctLine2s.cctLine2s.size() - 1).getN());
//        agCctInner0.plot3(Plot2d.RED_LINE);
//        agCctInner1.plot3(Plot2d.BLUE_LINE);

        Cct agCctInner = CctFactory.combineCct(agCctInner0.toCct(), agCctInner1.toCct());
        List<Point2> gradientAlongTrajectoryFastInner = agCctInner.magnetGradientAlongTrajectoryFast(trajectory, MM, 2 * MM);
        Plot2d.plot2(gradientAlongTrajectoryFastInner,Plot2d.RED_LINE);

        //r = 33.5mm 外层agcct
        SoleLayerCct agCctOuter0 = CctFactory.createSoleLaterCct(agCctSmallROuter, bigR, agCctAngle0, agCctWindingNumber0,
                agCctA0Outer, agCctA1Outer , agCctA2Outer, agCctIOuter, numberPerWinding, 0, 0, true);
        SoleLayerCct agCctOuter1 = CctFactory.createSoleLaterCct(agCctSmallROuter, bigR, agCctAngle1, agCctWindingNumber1,
                agCctA0Outer, agCctA1Outer , agCctA2Outer, agCctIOuter, numberPerWinding,
                agCctWindingNumber0*2*Math.PI, BaseUtils.Converter.angleToRadian(agCctAngle0), false);

        Cct agCctOuter = CctFactory.combineCct(agCctOuter0.toCct(), agCctOuter1.toCct());
        List<Point2> gradientAlongTrajectoryFastOuter = agCctOuter.magnetGradientAlongTrajectoryFast(trajectory, MM, 2 * MM);
        Plot2d.plot2(gradientAlongTrajectoryFastOuter,Plot2d.YELLOW_LINE);

        // 内外层合起来
        List<Point2> gradientSum = BaseUtils.StreamTools.combine(gradientAlongTrajectoryFastInner.stream(),
                gradientAlongTrajectoryFastOuter.stream())
                .map(ps -> Point2.create(ps.get(0).x, ps.get(0).y + ps.get(1).y))
                .collect(Collectors.toList());
        Plot2d.plot2(gradientSum, Plot2d.BLUE_LINE);

        // api 建模
        Cct agCct = createAgCct();
        List<Point2> gradientAgCct = agCct.magnetGradientAlongTrajectoryFast(trajectory, MM, 2 * MM);
        Plot2d.plot2(gradientAgCct, Plot2d.BLACK_LINE);



//        Plot3d.setCube(bigR*1.1);
//        Plot3d.showThread();

        Plot2d.showThread();

        //文件写入
        if(true){
            agCctInner0.printToCad("agCctInner0");
            agCctInner1.printToCad("agCctInner1");

            agCctOuter0.printToCad("agCctOuter0");
            agCctOuter1.printToCad("agCctOuter1");
        }
    }

//    @run
    public void 修改电流和A1使得梯度为58点75T每m(){
        Trajectory trajectory = getTrajectory();

        //可变参数
        // agCctIInner 注意 outer的电流为 - inner 不要改变! ->>> 脑残了，又不是响应式了
        // agCctA1Inner
        // agCctA1Outer 注意以上两个等比变化


//        double zeroPoint = Solver.findZeroPoint(I -> {
//            agCctIInner = I;
//            agCctIOuter = -agCctIInner;
//            CctFactory.Cct agCct = createAgCct();
//            return agCct.magnetGradientAtTrajectoryOfDistanceFast(trajectory,1.208,2*MM)+58.75;
//        }, Point2.create(0, 10000), 1000);
//        Logger.getLogger().info("zeroPoint = " + zeroPoint);
//        // zeroPoint = 4618.911905272398


        Cct agCct = createAgCct();
        List<Point2> point2s = agCct.magnetGradientAlongTrajectoryFast(trajectory, MM, 2 * MM);
        point2s = Point2.convert(point2s,
                (x, y) -> BaseUtils.Converter.radianToAngle((x - 1) / bigR),
                (x, y) -> y);
        Plot2d.plot2(point2s,Plot2d.BLUE_LINE);

        Plot2d.plot2(new Point2[]{
                Point2.create(-50,0),
                Point2.create(0,0),
                Point2.create(0,58.75),
                Point2.create(agCctAngle0,58.75),
                Point2.create(agCctAngle0,-58.75),
                Point2.create(agCctAngle0+agCctAngle1,-58.75),
                Point2.create(agCctAngle0+agCctAngle1,0),
                Point2.create(agCctAngle0+agCctAngle1+50,0),
        },Plot2d.GREY_DASH);

        Plot2d.showThread();


//        agCct.plot3(Plot2d.BLUE_LINE,Plot2d.YELLOW_LINE,Plot2d.BLUE_LINE,Plot2d.YELLOW_LINE);
//        Plot3d.setCenter(Point3.create(bigR,0,0),bigR*1.1);
//        Plot3d.showThread();
    }

//    @run
    public void 二极CCT(){
        Cct dipoleCct = createDipoleCct();
        SoleLayerCct soleLayerCct = dipoleCct.get(0);
        SoleLayerCct soleLayerCct1 = dipoleCct.get(1);

        soleLayerCct.printToCad("cct0");
        soleLayerCct1.printToCad("cct1");
    }

//    @run
    public void tracking(){
        Trajectory trajectory = getTrajectory();
        Cct agCct = createAgCct();
        Cct dipoleCct = createDipoleCct();

        Cct cct = CctFactory.combineCct(agCct, dipoleCct);

        List<Point2> magnetBzAlongTrajectory = cct.magnetBzAlongTrajectory(trajectory, MM);
        List<Point2> gradientAlongTrajectoryFast = cct.magnetGradientAlongTrajectoryFast(trajectory, MM, 2 * MM);

        Plot2d.plot2(magnetBzAlongTrajectory,Plot2d.BLACK_LINE);
        Plot2d.plot2(gradientAlongTrajectoryFast,Plot2d.RED_LINE);

//        Plot2d.showThread();


        dipoleCct.plot3(Plot2d.RED_LINE,Plot2d.BLUE_LINE);
        agCct.plot3(Plot2d.YELLOW_LINE,Plot2d.GREEN_LINE,Plot2d.YELLOW_LINE,Plot2d.GREEN_LINE);


        RunningParticle idealProtonAtTrajectory250MeV = ParticleFactory.createIdealProtonAtTrajectory250MeV(trajectory);
        List<Point3> run = ParticleRunner.run(idealProtonAtTrajectory250MeV, cct, trajectory.getLength(), MM);
        trajectory.plot3d(MM);
        Plot3d.plot3(run,Plot2d.RED_LINE);

        Plot3d.setCube(bigR*1.1);
        Plot3d.showThread();
    }


    public Trajectory getTrajectory() {
        return TrajectoryFactory.setStartingPoint(bigR, -1)
                .setStraightLine(1, Vector2.yDirect())
                .addArcLine(bigR, false, dipoleCctAngle)
                .addStraitLine(1.0);
    }

    public Cct createAgCct() {
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

    public Cct createDipoleCct() {
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
    private final double agCctA1Inner = -Math.pow(agCctSmallRInner, 2) * Math.sqrt(3) * 20;;
    private final double agCctA1Outer = Math.pow(agCctSmallROuter, 2) * Math.sqrt(3) * 20;;
    private final double agCctA2Inner = 0.0;
    private final double agCctA2Outer = 0.0;
    // 匝数 5mm匝间距 则匝数 = 58.85
    private final int dipoleCctWindingNumber = 59;
    private final int agCctWindingNumber0 = 23;
    private final int agCctWindingNumber1 = 34; // sum = 57
    // 电流
    private final double dipoleCctIInner = 9.898121356964111 * kA; // 求解获得
    private final double dipoleCctIOuter = -dipoleCctIInner;
    public  double agCctIInner = 4618.911905272398;
    public  double agCctIOuter = -agCctIInner;
    // CCT每匝分段
    private final int numberPerWinding = 360;

    /*-----------------辅助量----------------------*/
    final static double MM = 1e-3;
    final static double kA = 1e3;

    public static void main(String[] args) throws Exception {
        BaseUtils.Timer.printPeriodPerSecondCall(Logger.getLogger());
        StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[1];
        Logger.getLogger().info("{}", stackTraceElement.getClassName());
        Class<?> currentClass = Class.forName(stackTraceElement.getClassName());

        Constructor<?> constructor = currentClass.getConstructor(null);
        Object newInstance = constructor.newInstance();

        Method[] methods = currentClass.getMethods();
        Arrays.stream(methods)
                .filter(method -> method.isAnnotationPresent(四极CCT建模225度0308.run.class))
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
    private static @interface run {
    }
}
