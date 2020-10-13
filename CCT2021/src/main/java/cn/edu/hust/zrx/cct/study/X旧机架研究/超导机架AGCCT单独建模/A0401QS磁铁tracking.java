package cn.edu.hust.zrx.cct.study.X旧机架研究.超导机架AGCCT单独建模;

import cn.edu.hust.zrx.cct.Logger;
import cn.edu.hust.zrx.cct.advanced.COSY;
import cn.edu.hust.zrx.cct.base.BaseUtils;
import cn.edu.hust.zrx.cct.base.line.Trajectory;
import cn.edu.hust.zrx.cct.base.line.TrajectoryFactory;
import cn.edu.hust.zrx.cct.base.particle.*;
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
 * A0401QS磁铁tracking
 * <p>
 * Data
 * 20:26
 *
 * @author zrx
 * @version 1.0
 */
@SuppressWarnings("all")
public class A0401QS磁铁tracking {

    @run(value = 1, code = "0401001")
    public void QS_TRACKING对比() {
        boolean xPlane = false;
        double delta = 0.;
        double distance = 1.2;
        int number = 32;
        List<Point2> qs_true = tracking_true_qs(xPlane, delta, distance, number);
        List<Point2> cosy_1200mm = tracking_cosy_1200mm(xPlane, delta, number);

        Plot2d.plot2(qs_true, Plot2d.RED_POINT);
        Plot2d.plot2(cosy_1200mm, Plot2d.YELLOW_POINT);

        Plot2d.plot2circle(qs_true, Plot2d.RED_LINE);
        Plot2d.plot2circle(cosy_1200mm, Plot2d.YELLOW_LINE);

        if (xPlane) {
            Plot2d.info("x/mm", "xp/mrad", "x/xp plane", 18);
        } else {
            Plot2d.info("y/mm", "yp/mrad", "y/yp plane", 18);
        }
        Plot2d.legend(18, "code", "COSY-2nd");

        Plot2d.equal();
        Plot2d.showThread();
    }


    private List<Point2> tracking_true_qs(boolean xPlane, double delta, double distance, int number) {
        QsHardPlaneMagnet qsHardPlaneMagnet = QsHardPlaneMagnet.create(0.2, 53.3,
                221.1, 30, Point2.origin(), Vector2.yDirect());

        Trajectory trajectory = TrajectoryFactory.setStartingPoint(0, -0.5).setStraightLine(1.2, Vector2.yDirect());

        RunningParticle rp0 = ParticleFactory.createIdealProtonAtTrajectory250MeV(trajectory);
        RunningParticle rpEnd = ParticleFactory.createIdealProtonAtTrajectory250MeV(trajectory, distance);

        List<PhaseSpaceParticle> pp = PhaseSpaceParticles.phaseSpaceParticlesAlongPositiveEllipseInPlane(
                xPlane, 3.5 * MM, 7.2 * MM, delta, number);

        List<RunningParticle> ps = ParticleFactory.createParticlesFromPhaseSpaceParticle(rp0, rp0.computeNaturalCoordinateSystem(), pp);

        ParticleRunner.runThread(ps, qsHardPlaneMagnet, distance, 1 * MM);

        List<PhaseSpaceParticle> psEnd = PhaseSpaceParticles.phaseSpaceParticlesFromRunningParticles(rpEnd, rpEnd.computeNaturalCoordinateSystem(), ps);

        List<Point2> list = PhaseSpaceParticles.projectionToPlane(xPlane, psEnd);

        return Point2.convert(list, 1 / MM, 1 / MRAD);

    }

    private List<Point2> tracking_cosy_1200mm(boolean xPlane, double delta, int number) {
        List<PhaseSpaceParticle> pp = PhaseSpaceParticles.phaseSpaceParticlesAlongPositiveEllipseInPlane(
                xPlane, 3.5 * MM, 7.2 * MM, delta, number);

        pp = PhaseSpaceParticles.convertDeltaFromMomentumDispersionToEnergyDispersion(pp, 250);

        COSY.TransMatrix transMatrix = cosy_qs_matrix();

        List<PhaseSpaceParticle> apply = transMatrix.apply(pp);

        List<Point2> list = PhaseSpaceParticles.projectionToPlane(xPlane, apply);

        return Point2.convert(list, 1 / MM, 1 / MRAD);

    }

    private COSY.TransMatrix cosy_qs_matrix() {
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


    @run(value = 2, code = "0401001")
    public void QS_TRACKING对比_六极场10000() {
        boolean xPlane = false;
        double delta = 0.;
        double distance = 1.2;
        int number = 32;
        List<Point2> qs_true = tracking_true_qs_六极场10000(xPlane, delta, distance, number);
        List<Point2> cosy_1200mm = tracking_cosy_1200mm_六极场10000(xPlane, delta, number);

        Plot2d.plot2(qs_true, Plot2d.RED_POINT);
        Plot2d.plot2(cosy_1200mm, Plot2d.YELLOW_POINT);

        Plot2d.plot2circle(qs_true, Plot2d.RED_LINE);
        Plot2d.plot2circle(cosy_1200mm, Plot2d.YELLOW_LINE);

        if (xPlane) {
            Plot2d.info("x/mm", "xp/mrad", "x/xp plane", 18);
        } else {
            Plot2d.info("y/mm", "yp/mrad", "y/yp plane", 18);
        }
        Plot2d.legend(18, "code", "COSY-2nd");

        Plot2d.equal();
        Plot2d.showThread();
    }

    private List<Point2> tracking_true_qs_六极场10000(boolean xPlane, double delta, double distance, int number) {
        QsHardPlaneMagnet qsHardPlaneMagnet = QsHardPlaneMagnet.create(0.2, 0.0,
                10000, 30, Point2.origin(), Vector2.yDirect());

        Trajectory trajectory = TrajectoryFactory.setStartingPoint(0, -0.5).setStraightLine(1.2, Vector2.yDirect());

        RunningParticle rp0 = ParticleFactory.createIdealProtonAtTrajectory250MeV(trajectory);
        RunningParticle rpEnd = ParticleFactory.createIdealProtonAtTrajectory250MeV(trajectory, distance);

        List<PhaseSpaceParticle> pp = PhaseSpaceParticles.phaseSpaceParticlesAlongPositiveEllipseInPlane(
                xPlane, 3.5 * MM, 7.2 * MM, delta, number);

        List<RunningParticle> ps = ParticleFactory.createParticlesFromPhaseSpaceParticle(rp0, rp0.computeNaturalCoordinateSystem(), pp);

        ParticleRunner.runThread(ps, qsHardPlaneMagnet, distance, 1 * MM);

        List<PhaseSpaceParticle> psEnd = PhaseSpaceParticles.phaseSpaceParticlesFromRunningParticles(rpEnd, rpEnd.computeNaturalCoordinateSystem(), ps);

        List<Point2> list = PhaseSpaceParticles.projectionToPlane(xPlane, psEnd);

        return Point2.convert(list, 1 / MM, 1 / MRAD);

    }

    private List<Point2> tracking_cosy_1200mm_六极场10000(boolean xPlane, double delta, int number) {
        List<PhaseSpaceParticle> pp = PhaseSpaceParticles.phaseSpaceParticlesAlongPositiveEllipseInPlane(
                xPlane, 3.5 * MM, 7.2 * MM, delta, number);

        pp = PhaseSpaceParticles.convertDeltaFromMomentumDispersionToEnergyDispersion(pp, 250);

        COSY.TransMatrix transMatrix = cosy_qs_matrix_六极场10000();

        List<PhaseSpaceParticle> apply = transMatrix.apply(pp);

        List<Point2> list = PhaseSpaceParticles.projectionToPlane(xPlane, apply);

        return Point2.convert(list, 1 / MM, 1 / MRAD);

    }

    private COSY.TransMatrix cosy_qs_matrix_六极场10000() {
        //DL ABS(0.5) ;
        //M5 ABS(0.2) 0.0 9.0 0 0 0 QS1_APER;
        //DL ABS(0.5) ;
        //PM 6;


        return COSY.importMatrix(
                "   1.000000     0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00 100000\n" +
                        "   1.200000      1.000000     0.0000000E+00 0.0000000E+00 0.0000000E+00 010000\n" +
                        "  0.0000000E+00 0.0000000E+00  1.000000     0.0000000E+00 0.0000000E+00 001000\n" +
                        "  0.0000000E+00 0.0000000E+00  1.200000      1.000000     0.0000000E+00 000100\n" +
                        "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00  1.000000     000010\n" +
                        "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00 0.2336092     000001\n" +
                        "  -493.3950     -822.3250     0.0000000E+00 0.0000000E+00 0.0000000E+00 200000\n" +
                        "  -586.5919     -986.7900     0.0000000E+00 0.0000000E+00 0.0000000E+00 110000\n" +
                        "  -175.9776     -298.7781     0.0000000E+00 0.0000000E+00-0.3352685     020000\n" +
                        "  0.0000000E+00 0.0000000E+00  986.7900      1644.650     0.0000000E+00 101000\n" +
                        "  0.0000000E+00 0.0000000E+00  586.5919      986.7900     0.0000000E+00 011000\n" +
                        "   493.3950      822.3250     0.0000000E+00 0.0000000E+00 0.0000000E+00 002000\n" +
                        "  0.0000000E+00 0.0000000E+00  586.5919      986.7900     0.0000000E+00 100100\n" +
                        "  0.0000000E+00 0.0000000E+00  351.9551      597.5562     0.0000000E+00 010100\n" +
                        "   586.5919      986.7900     0.0000000E+00 0.0000000E+00 0.0000000E+00 001100\n" +
                        " -0.6705370     0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00 010001\n" +
                        "   175.9776      298.7781     0.0000000E+00 0.0000000E+00-0.3352685     000200\n" +
                        "  0.0000000E+00 0.0000000E+00-0.6705370     0.0000000E+00 0.0000000E+00 000101\n" +
                        "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00-0.1958045     000002"
        );
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
