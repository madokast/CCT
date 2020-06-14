package cn.edu.hust.zrx.cct.study.验证性研究;

import cn.edu.hust.zrx.cct.Logger;
import cn.edu.hust.zrx.cct.base.BaseUtils;
import cn.edu.hust.zrx.cct.base.CoordinateSystem3d;
import cn.edu.hust.zrx.cct.base.cct.Cct;
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
import java.util.Arrays;
import java.util.List;

/**
 * Description
 * 研究 粒子 像空间 绘图等
 * <p>
 * Data
 * 16:22
 *
 * @author zrx
 * @version 1.0
 */
@SuppressWarnings("all")
public class A20200315粒子相椭圆画图 {

//    @run //美妙
    public void 单粒子运动() {
        //拿到 轨道 和 CCT
        Trajectory trajectory = getTrajectory();
        Cct dipoleCct = createDipoleCct();
        Cct agCct = createAgCct();

        // 组合为 CCT
        Cct cct = CctFactory.combineCct(dipoleCct, agCct);

        // 拿到理想粒子
        RunningParticle idealProton = ParticleFactory.createIdealProtonAtTrajectory250MeV(trajectory);

        // 运行
        List<Point3> run = ParticleRunner.run(idealProton, cct, trajectory.getLength(), 1 * MM);

        //------------plot----------------
        trajectory.plot3d(Plot2d.BLACK_LINE);
//        dipoleCct.plot3(Plot2d.RED_LINE,Plot2d.RED_LINE);
//        agCct.plot3(Plot2d.GREEN_LINE,Plot2d.GREEN_LINE,Plot2d.GREEN_LINE,Plot2d.GREEN_LINE);
        Plot3d.plot3(run, Plot2d.YELLOW_LINE);

        Plot3d.setCube(bigR * 1.1);
        Plot3d.showThread();

        // length
        double length = trajectory.getLength();
        double distance = idealProton.getDistance();
        Logger.getLogger().info("length = " + length);
        Logger.getLogger().info("distance = " + distance);
    }

    //    @run //成功 2020年3月15日
    public void 多粒子运动() {
        List<PhaseSpaceParticle> phaseSpaceParticles =
                PhaseSpaceParticles.phaseSpaceParticlesAlongPositiveEllipseInXXpPlane(
                        3.5 * MM, 7.5 * MRAD, 0.0, 16);

        Trajectory trajectory = getTrajectory();
        RunningParticle idealProton = ParticleFactory.createIdealProtonAtTrajectory250MeV(trajectory);
        List<RunningParticle> runningParticles = ParticleFactory.createParticlesFromPhaseSpaceParticle(
                idealProton,
                idealProton.computeNaturalCoordinateSystem(Vector3.getZDirect()),
                phaseSpaceParticles
        );

        List<PhaseSpaceParticle> phaseSpaceParticleList = PhaseSpaceParticles.phaseSpaceParticlesFromRunningParticles(
                idealProton,
                idealProton.computeNaturalCoordinateSystem(Vector3.getZDirect()),
                runningParticles
        );

        List<Point2> projectionToXXpPlane = PhaseSpaceParticles.projectionToXXpPlane(phaseSpaceParticles);

        List<Point2> projectionToXXpPlane2 = PhaseSpaceParticles.projectionToXXpPlane(phaseSpaceParticleList);

        Plot2d.plotPoints(projectionToXXpPlane, Plot2d.BLACK_POINT);
        Plot2d.plotPoints(projectionToXXpPlane2, Plot2d.RED_POINT);

//        Plot2d.equal();

        Plot2d.showThread();
    }


    @run // 美妙!!
    public void 相空间变化() {
        //拿到 轨道 和 CCT
        Trajectory trajectory = getTrajectory();
        Cct dipoleCct = createDipoleCct();
        Cct agCct = createAgCct();

        // 组合为 CCT
        Cct cct = CctFactory.combineCct(dipoleCct, agCct);

        // 拿到理想粒子
        RunningParticle idealProton = ParticleFactory.createIdealProtonAtTrajectory250MeV(trajectory);

        // 拿到起点处自然坐标系
        CoordinateSystem3d naturalCoordinateSystemStartingPoint =
                idealProton.computeNaturalCoordinateSystem(Vector3.getZDirect());

        // 计算相空间椭圆粒子
        List<PhaseSpaceParticle> phaseSpaceParticles =
                PhaseSpaceParticles.phaseSpaceParticlesAlongPositiveEllipseInXXpPlane(
                        3.5 * MM, 7.5 * MRAD, 0.0, 16);

        // 相空间 -> 实际粒子
        List<RunningParticle> particlesFromPhaseSpaceParticle = ParticleFactory.createParticlesFromPhaseSpaceParticle(
                idealProton, naturalCoordinateSystemStartingPoint, phaseSpaceParticles);

        // run
        List<Point3> idealRun = ParticleRunner.run(idealProton, cct, trajectory.getLength(), 1 * MM);
        // runs
        List<List<Point3>> runListList = ParticleRunner.runThread(
                particlesFromPhaseSpaceParticle, cct, trajectory.getLength(), 1 * MM);

        // 拿到终点处自然坐标系
        CoordinateSystem3d naturalCoordinateSystemEndingPoint =
                idealProton.computeNaturalCoordinateSystem(Vector3.getZDirect());

        // 实际粒子 -> 相空间
        List<PhaseSpaceParticle> phaseSpaceParticlesEnd = PhaseSpaceParticles.phaseSpaceParticlesFromRunningParticles(
                idealProton, naturalCoordinateSystemEndingPoint, particlesFromPhaseSpaceParticle);

        // plot2d
        List<Point2> xXpPlane = PhaseSpaceParticles.projectionToXXpPlane(phaseSpaceParticlesEnd);

        Plot2d.plot2(xXpPlane, Plot2d.RED_POINT);

        Plot2d.equal();

        Plot2d.showThread();

        // plot3d
        trajectory.plot3d(Plot2d.BLACK_LINE);
        Plot3d.plot3(idealRun,Plot2d.RED_LINE);
        runListList.forEach(runList->Plot3d.plot3(runList,Plot2d.YELLOW_LINE));

        Plot3d.setCube(bigR*1.1);
        Plot3d.showThread();


    }

    //---------------------------------------------------------

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

        Constructor<?> constructor = currentClass.getConstructor((Class<?>) null);
        Object newInstance = constructor.newInstance();

        Method[] methods = currentClass.getMethods();
        Arrays.stream(methods)
                .filter(method -> method.isAnnotationPresent(A20200315粒子相椭圆画图.run.class))
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
