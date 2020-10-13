package cn.edu.hust.zrx.cct.study.X旧机架研究.超导机架AGCCT单独建模;

import cn.edu.hust.zrx.cct.Logger;
import cn.edu.hust.zrx.cct.base.BaseUtils;
import cn.edu.hust.zrx.cct.advanced.COSY;
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

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Description
 * A0320粒子跟踪_在修复二极CCT四极场和四极CCT二极场后
 * <p>
 * Data
 * 22:39
 *
 * @author zrx
 * @version 1.0
 */
@SuppressWarnings("all")
public class A0320粒子跟踪_在修复二极CCT四极场和四极CCT二极场后 {

    // -------------------------------------------------------------------------------------------
    // |                                                                                         |
    // |                                                                                         |
    // |                                                                                         |
    // |                                                                                         |
    // |                                                                                         |
    // |       COSY MAP 中运动的粒子。注意                                                         |
    // |       由 PhaseSpaceParticles 工厂方法建立后，需要修改 delta                                |
    // |       原 delta 为 动量分散                                                               |
    // |       需要修改为 能量分散                                                                 |
    // |       调用 convertDeltaFromMomentumDispersionToEnergyDispersion 方法                     |
    // |                                                                                         |
    // |                                                                                         |
    // |                                                                                         |
    // |       track 后的粒子 应和理想粒子对比 而不是参考粒子                                        |
    // |       使用 ip = ParticleFactory.createIdealProtonAtTrajectory250MeV(trajectory, s);     |
    // |       获得该点处的理想粒子                                                                |
    // |                                                                                         |
    // |                                                                                         |
    // -------------------------------------------------------------------------------------------


    // ----------- xxp ----------- 对比

    @run(value = 1, code = "A0320-0321-1720")
    public void 起始处对比XXP平面() {
        Trajectory trajectory = getTrajectory();
        Cct cct = getCct();

        // XX or YY
        List<PhaseSpaceParticle> phaseStart = PhaseSpaceParticles.phaseSpaceParticlesAlongPositiveEllipseInXXpPlane(
                3.5 * MM, 7.5 * MRAD, 0.0, 64);

        // COSY 一阶-red 和 二阶-blus
        List<Point2> first, second;
        {
            // XX or YY
            first = PhaseSpaceParticles.projectionToXXpPlane(phaseStart);
            second = PhaseSpaceParticles.projectionToXXpPlane(phaseStart);

            first = Point2.convert(first, 1 / MM, 1 / MRAD);
            second = Point2.convert(second, 1 / MM, 1 / MRAD);
        }

        // TRACKING black
        List<Point2> tracking;
        {
            RunningParticle ip = ParticleFactory.createIdealProtonAtTrajectory250MeV(trajectory);

            List<RunningParticle> rp = ParticleFactory.createParticlesFromPhaseSpaceParticle(ip, ip.computeNaturalCoordinateSystem(), phaseStart);

            // 长度？
            double length = 0;

//            ParticleRunner.run(ip,cct,length,MM);

//            ParticleRunner.runThread(rp,cct,length,MM);

            List<PhaseSpaceParticle> rppTrack = PhaseSpaceParticles.phaseSpaceParticlesFromRunningParticles(ip, ip.computeNaturalCoordinateSystem(), rp);

            // XX or YY
            tracking = PhaseSpaceParticles.projectionToXXpPlane(rppTrack);

            tracking = Point2.convert(tracking, 1 / MM, 1 / MRAD);
        }


        Plot2d.plot2(first, Plot2d.RED_POINT);
        Plot2d.plot2(second, Plot2d.BLUE_POINT);
        Plot2d.plot2(tracking, Plot2d.BLACK_POINT);
        Plot2d.plotPoint(Point2.origin(), Plot2d.GREEN_UP_TRI);
        Plot2d.plot2circle(first, Plot2d.RED_LINE);
        Plot2d.plot2circle(second, Plot2d.BLUE_LINE);
        Plot2d.plot2circle(tracking, Plot2d.BLACK_LINE);

        Plot2d.legend(18, "cosy 1st", "cosy 2nd", "tracking", "center");
        Plot2d.info("x/mm", "xp/mrad", "x-plane", 18);

        Plot2d.equal();
        Plot2d.showThread();

    }

    @run(value = 2, code = "A0320-0321-1652")
    public void 入口处对比XXP平面() {
        Trajectory trajectory = getTrajectory();
        Cct cct = getCct();

        // XX or YY
        List<PhaseSpaceParticle> phaseStart = PhaseSpaceParticles.phaseSpaceParticlesAlongPositiveEllipseInXXpPlane(
                3.5 * MM, 7.5 * MRAD, 0.0, 64);

        // COSY 一阶-red 和 二阶-blus
        List<Point2> first, second;
        {
            List<PhaseSpaceParticle> phaseEndCosyFirst = COSY.importMatrix(//
                    "   1.000000     0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00 100000\n" +
                            "  0.5000000      1.000000     0.0000000E+00 0.0000000E+00 0.0000000E+00 010000\n" +
                            "  0.0000000E+00 0.0000000E+00  1.000000     0.0000000E+00 0.0000000E+00 001000\n" +
                            "  0.0000000E+00 0.0000000E+00 0.5000000      1.000000     0.0000000E+00 000100\n" +
                            "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00  1.000000     000010\n" +
                            "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00 0.9733718E-01 000001"
            ).apply(phaseStart);

            List<PhaseSpaceParticle> phaseEndCosySecond = COSY.importMatrix(
                    "   1.000000     0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00 100000\n" +
                            "  0.5000000      1.000000     0.0000000E+00 0.0000000E+00 0.0000000E+00 010000\n" +
                            "  0.0000000E+00 0.0000000E+00  1.000000     0.0000000E+00 0.0000000E+00 001000\n" +
                            "  0.0000000E+00 0.0000000E+00 0.5000000      1.000000     0.0000000E+00 000100\n" +
                            "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00  1.000000     000010\n" +
                            "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00 0.9733718E-01 000001\n" +
                            "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00-0.1396952     020000\n" +
                            " -0.2793904     0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00 010001\n" +
                            "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00-0.1396952     000200\n" +
                            "  0.0000000E+00 0.0000000E+00-0.2793904     0.0000000E+00 0.0000000E+00 000101\n" +
                            "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00-0.8158523E-01 000002"
            ).apply(phaseStart);

            // XX or YY
            first = PhaseSpaceParticles.projectionToXXpPlane(phaseEndCosyFirst);
            second = PhaseSpaceParticles.projectionToXXpPlane(phaseEndCosySecond);

            first = Point2.convert(first, 1 / MM, 1 / MRAD);
            second = Point2.convert(second, 1 / MM, 1 / MRAD);
        }

        // TRACKING black
        List<Point2> tracking;
        {
            RunningParticle ip = ParticleFactory.createIdealProtonAtTrajectory250MeV(trajectory);

            List<RunningParticle> rp = ParticleFactory.createParticlesFromPhaseSpaceParticle(ip, ip.computeNaturalCoordinateSystem(), phaseStart);

            // 长度？
            double length = 0.5;

            // ParticleRunner.run(ip,cct,length,MM);
            ip = ParticleFactory.createIdealProtonAtTrajectory250MeV(trajectory, 0.5);

            ParticleRunner.runThread(rp, cct, length, MM);

            List<PhaseSpaceParticle> rppTrack =
                    PhaseSpaceParticles.phaseSpaceParticlesFromRunningParticles(ip, ip.computeNaturalCoordinateSystem(), rp);

            // XX or YY
            tracking = PhaseSpaceParticles.projectionToXXpPlane(rppTrack);

            tracking = Point2.convert(tracking, 1 / MM, 1 / MRAD);
        }


        Plot2d.plot2(first, Plot2d.RED_POINT);
        Plot2d.plot2(second, Plot2d.BLUE_POINT);
        Plot2d.plot2(tracking, Plot2d.BLACK_POINT);
        Plot2d.plotPoint(Point2.origin(), Plot2d.GREEN_UP_TRI);
        Plot2d.plot2circle(first, Plot2d.RED_LINE);
        Plot2d.plot2circle(second, Plot2d.BLUE_LINE);
        Plot2d.plot2circle(tracking, Plot2d.BLACK_LINE);

        Plot2d.legend(18, "cosy 1st", "cosy 2nd", "tracking", "center");
        Plot2d.info("x/mm", "xp/mrad", "x-plane", 18);

        Plot2d.setCube(20);

        Plot2d.equal();
        Plot2d.showThread();

    }

    @run(value = 3, code = "A0320-0321-1720")
    public void 第二CCT处对比XXP平面() {
        Trajectory trajectory = getTrajectory();
        Cct cct = getCct();

        // XX or YY
        List<PhaseSpaceParticle> phaseStart = PhaseSpaceParticles.phaseSpaceParticlesAlongPositiveEllipseInXXpPlane(
                3.5 * MM, 7.5 * MRAD, 0.0, 64);

        // COSY 一阶-red 和 二阶-blus
        List<Point2> first, second;
        {
            List<PhaseSpaceParticle> phaseEndCosyFirst = COSY.importMatrix(//
                    "  0.8203399     -2.917389     0.0000000E+00 0.0000000E+00-0.8351992E-01 100000\n" +
                            "  0.5222710    -0.6383543     0.0000000E+00 0.0000000E+00-0.4690332E-01 010000\n" +
                            "  0.0000000E+00 0.0000000E+00  1.177680      3.062980     0.0000000E+00 001000\n" +
                            "  0.0000000E+00 0.0000000E+00 0.7151649      2.709170     0.0000000E+00 000100\n" +
                            "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00  1.000000     000010\n" +
                            "  0.5143366E-02 0.8351992E-01 0.0000000E+00 0.0000000E+00 0.1204147     000001"
            ).apply(phaseStart);

            List<PhaseSpaceParticle> phaseEndCosySecond = COSY.importMatrix(
                    "  0.8203399     -2.917389     0.0000000E+00 0.0000000E+00-0.8351992E-01 100000\n" +
                            "  0.5222710    -0.6383543     0.0000000E+00 0.0000000E+00-0.4690332E-01 010000\n" +
                            "  0.0000000E+00 0.0000000E+00  1.177680      3.062980     0.0000000E+00 001000\n" +
                            "  0.0000000E+00 0.0000000E+00 0.7151649      2.709170     0.0000000E+00 000100\n" +
                            "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00  1.000000     000010\n" +
                            "  0.5143366E-02 0.8351992E-01 0.0000000E+00 0.0000000E+00 0.1204147     000001\n" +
                            " -0.4278456     -3.190056     0.0000000E+00 0.0000000E+00-0.8627271E-01 200000\n" +
                            " -0.3219103     -3.582959     0.0000000E+00 0.0000000E+00-0.7593380E-03 110000\n" +
                            " -0.5073188E-01 -1.085379     0.0000000E+00 0.0000000E+00-0.1481379     020000\n" +
                            "  0.0000000E+00 0.0000000E+00 0.4578178      4.268066     0.0000000E+00 101000\n" +
                            "  0.0000000E+00 0.0000000E+00 0.2571025      2.396871     0.0000000E+00 011000\n" +
                            "  0.1115915      1.812065     0.0000000E+00 0.0000000E+00-0.1030432     002000\n" +
                            "  0.0000000E+00 0.0000000E+00 0.4049345      2.591851     0.0000000E+00 100100\n" +
                            "  0.0000000E+00 0.0000000E+00 0.2274041      1.455538     0.0000000E+00 010100\n" +
                            "  0.1115915      1.812065     0.0000000E+00 0.0000000E+00-0.2111479     001100\n" +
                            "  0.1027414    -0.1079298     0.0000000E+00 0.0000000E+00 0.3165896E-01 100001\n" +
                            " -0.2360259     0.8506180     0.0000000E+00 0.0000000E+00 0.4111378E-01 010001\n" +
                            "  0.0000000E+00 0.0000000E+00-0.1012789    -0.8841852E-01 0.0000000E+00 001001\n" +
                            "  0.2329557E-01 0.3782822     0.0000000E+00 0.0000000E+00-0.2568236     000200\n" +
                            "  0.0000000E+00 0.0000000E+00-0.4539065     -1.001255     0.0000000E+00 000101\n" +
                            " -0.3669528E-02-0.1181472E-01 0.0000000E+00 0.0000000E+00-0.1009740     000002"
            ).apply(phaseStart);

            // XX or YY
            first = PhaseSpaceParticles.projectionToXXpPlane(phaseEndCosyFirst);
            second = PhaseSpaceParticles.projectionToXXpPlane(phaseEndCosySecond);

            first = Point2.convert(first, 1 / MM, 1 / MRAD);
            second = Point2.convert(second, 1 / MM, 1 / MRAD);
        }

        // TRACKING black
        List<Point2> tracking;
        {
            RunningParticle ip = ParticleFactory.createIdealProtonAtTrajectory250MeV(trajectory);

            List<RunningParticle> rp = ParticleFactory.createParticlesFromPhaseSpaceParticle(ip, ip.computeNaturalCoordinateSystem(), phaseStart);

            // 长度？
            double length = 0.5 + trajectoryBigR * BaseUtils.Converter.angleToRadian(agCctAngle0);

//            ParticleRunner.run(ip,cct,length,MM);
            ip = ParticleFactory.createIdealProtonAtTrajectory250MeV(trajectory, length);

            ParticleRunner.runThread(rp, cct, length, MM);

            List<PhaseSpaceParticle> rppTrack = PhaseSpaceParticles.phaseSpaceParticlesFromRunningParticles(ip, ip.computeNaturalCoordinateSystem(), rp);

            // XX or YY
            tracking = PhaseSpaceParticles.projectionToXXpPlane(rppTrack);

            tracking = Point2.convert(tracking, 1 / MM, 1 / MRAD);
        }


        Plot2d.plot2(first, Plot2d.RED_POINT);
        Plot2d.plot2(second, Plot2d.BLUE_POINT);
        Plot2d.plot2(tracking, Plot2d.BLACK_POINT);
        Plot2d.plotPoint(Point2.origin(), Plot2d.GREEN_UP_TRI);
        Plot2d.plot2circle(first, Plot2d.RED_LINE);
        Plot2d.plot2circle(second, Plot2d.BLUE_LINE);
        Plot2d.plot2circle(tracking, Plot2d.BLACK_LINE);

        Plot2d.legend(18, "cosy 1st", "cosy 2nd", "tracking", "center");
        Plot2d.info("x/mm", "xp/mrad", "x-plane", 18);

        Plot2d.equal();
        Plot2d.showThread();

    }

    @run(value = 4, code = "A0320-0321-2027")
    public void 出口处对比XXP平面() {
        Trajectory trajectory = getTrajectory();
        Cct cct = getCct();

        // XX or YY
        List<PhaseSpaceParticle> phaseStart = PhaseSpaceParticles.phaseSpaceParticlesAlongPositiveEllipseInXXpPlane(
                3.5 * MM, 7.5 * MRAD, 0.0, 64);

        // COSY 一阶-red 和 二阶-blus
        List<Point2> first, second;
        {
            List<PhaseSpaceParticle> phaseEndCosyFirst = COSY.importMatrix(//
                    "  0.5479324    -0.3692074     0.0000000E+00 0.0000000E+00-0.1680138     100000\n" +
                            "  0.5878008      1.428971     0.0000000E+00 0.0000000E+00-0.1154528     010000\n" +
                            "  0.0000000E+00 0.0000000E+00  1.238480     -2.412454     0.0000000E+00 001000\n" +
                            "  0.0000000E+00 0.0000000E+00 0.8830676    -0.9126992     0.0000000E+00 000100\n" +
                            "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00  1.000000     000010\n" +
                            "  0.3549835E-01 0.2827129     0.0000000E+00 0.0000000E+00 0.1522421     000001"
            ).apply(phaseStart);

            List<PhaseSpaceParticle> phaseEndCosySecond = COSY.importMatrix(
                    "  0.5479324    -0.3692074     0.0000000E+00 0.0000000E+00-0.1680138     100000\n" +
                            "  0.5878008      1.428971     0.0000000E+00 0.0000000E+00-0.1154528     010000\n" +
                            "  0.0000000E+00 0.0000000E+00  1.238480     -2.412454     0.0000000E+00 001000\n" +
                            "  0.0000000E+00 0.0000000E+00 0.8830676    -0.9126992     0.0000000E+00 000100\n" +
                            "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00  1.000000     000010\n" +
                            "  0.3549835E-01 0.2827129     0.0000000E+00 0.0000000E+00 0.1522421     000001\n" +
                            "  -1.293624     -4.590501     0.0000000E+00 0.0000000E+00-0.1156784     200000\n" +
                            " -0.9490291     -2.462184     0.0000000E+00 0.0000000E+00 0.1054597     110000\n" +
                            " -0.9894984E-01-0.1649161E-01 0.0000000E+00 0.0000000E+00-0.1574655     020000\n" +
                            "  0.0000000E+00 0.0000000E+00 0.5913558     -3.476974     0.0000000E+00 101000\n" +
                            "  0.0000000E+00 0.0000000E+00 0.2410026     -3.086489     0.0000000E+00 011000\n" +
                            "  0.4166564E-01 -2.655721     0.0000000E+00 0.0000000E+00-0.2527618     002000\n" +
                            "  0.0000000E+00 0.0000000E+00 0.5251758     -3.066368     0.0000000E+00 100100\n" +
                            "  0.0000000E+00 0.0000000E+00 0.2604667     -2.530505     0.0000000E+00 010100\n" +
                            " -0.1146237     -4.541351     0.0000000E+00 0.0000000E+00-0.4214044     001100\n" +
                            "  0.3330313     0.9379569     0.0000000E+00 0.0000000E+00 0.5452429E-01 100001\n" +
                            " -0.1582321     0.2443004     0.0000000E+00 0.0000000E+00 0.7821427E-01 010001\n" +
                            "  0.0000000E+00 0.0000000E+00-0.1032713     0.4380811     0.0000000E+00 001001\n" +
                            " -0.1078746     -1.960850     0.0000000E+00 0.0000000E+00-0.3555751     000200\n" +
                            "  0.0000000E+00 0.0000000E+00-0.5254877      1.259862     0.0000000E+00 000101\n" +
                            " -0.2711393E-01-0.8701740E-01 0.0000000E+00 0.0000000E+00-0.1287123     000002"
            ).apply(phaseStart);

            // XX or YY
            first = PhaseSpaceParticles.projectionToXXpPlane(phaseEndCosyFirst);
            second = PhaseSpaceParticles.projectionToXXpPlane(phaseEndCosySecond);

            first = Point2.convert(first, 1 / MM, 1 / MRAD);
            second = Point2.convert(second, 1 / MM, 1 / MRAD);
        }

        // TRACKING black
        List<Point2> tracking;
        {
            RunningParticle ip = ParticleFactory.createIdealProtonAtTrajectory250MeV(trajectory);

            List<RunningParticle> rp = ParticleFactory.createParticlesFromPhaseSpaceParticle(ip, ip.computeNaturalCoordinateSystem(), phaseStart);

            // 长度？
            double length = 0.5 + trajectoryBigR * BaseUtils.Converter.angleToRadian(dipoleCctAngle);

//            ParticleRunner.run(ip,cct,length,MM);
            ip = ParticleFactory.createIdealProtonAtTrajectory250MeV(trajectory, length);

            ParticleRunner.runThread(rp, cct, length, MM);

            List<PhaseSpaceParticle> rppTrack = PhaseSpaceParticles.phaseSpaceParticlesFromRunningParticles(ip, ip.computeNaturalCoordinateSystem(), rp);

            // XX or YY
            tracking = PhaseSpaceParticles.projectionToXXpPlane(rppTrack);

            tracking = Point2.convert(tracking, 1 / MM, 1 / MRAD);
        }


        Plot2d.plot2(first, Plot2d.RED_POINT);
        Plot2d.plot2(second, Plot2d.BLUE_POINT);
        Plot2d.plot2(tracking, Plot2d.BLACK_POINT);
        Plot2d.plotPoint(Point2.origin(), Plot2d.GREEN_UP_TRI);
        Plot2d.plot2circle(first, Plot2d.RED_LINE);
        Plot2d.plot2circle(second, Plot2d.BLUE_LINE);
        Plot2d.plot2circle(tracking, Plot2d.BLACK_LINE);

        Plot2d.legend(18, "cosy 1st", "cosy 2nd", "tracking", "center");
        Plot2d.info("x/mm", "xp/mrad", "x-plane", 18);

        Plot2d.equal();
        Plot2d.showThread();

    }

    @run(value = 5, code = "A0320-0321-2033")
    public void 终点对比XXP平面() {
        Trajectory trajectory = getTrajectory();
        Cct cct = getCct();

        // XX or YY
        List<PhaseSpaceParticle> phaseStart = PhaseSpaceParticles.phaseSpaceParticlesAlongPositiveEllipseInXXpPlane(
                3.5 * MM, 7.5 * MRAD, 0.0, 64);

        // COSY 一阶-red 和 二阶-blus
        List<Point2> first, second;
        {
            List<PhaseSpaceParticle> phaseEndCosyFirst = COSY.importMatrix(//
                    "  0.3633288    -0.3692074     0.0000000E+00 0.0000000E+00-0.1680138     100000\n" +
                            "   1.302286      1.428971     0.0000000E+00 0.0000000E+00-0.1154528     010000\n" +
                            "  0.0000000E+00 0.0000000E+00 0.3225315E-01 -2.412454     0.0000000E+00 001000\n" +
                            "  0.0000000E+00 0.0000000E+00 0.4267180    -0.9126992     0.0000000E+00 000100\n" +
                            "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00  1.000000     000010\n" +
                            "  0.1768548     0.2827129     0.0000000E+00 0.0000000E+00 0.2495793     000001"
            ).apply(phaseStart);

            List<PhaseSpaceParticle> phaseEndCosySecond = COSY.importMatrix(
                    "  0.3633288    -0.3692074     0.0000000E+00 0.0000000E+00-0.1680138     100000\n" +
                            "   1.302286      1.428971     0.0000000E+00 0.0000000E+00-0.1154528     010000\n" +
                            "  0.0000000E+00 0.0000000E+00 0.3225315E-01 -2.412454     0.0000000E+00 001000\n" +
                            "  0.0000000E+00 0.0000000E+00 0.4267180    -0.9126992     0.0000000E+00 000100\n" +
                            "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00  1.000000     000010\n" +
                            "  0.1768548     0.2827129     0.0000000E+00 0.0000000E+00 0.2495793     000001\n" +
                            "  -3.588874     -4.590501     0.0000000E+00 0.0000000E+00-0.1347208     200000\n" +
                            "  -2.180121     -2.462184     0.0000000E+00 0.0000000E+00 0.2528623     110000\n" +
                            " -0.1071956    -0.1649161E-01 0.0000000E+00 0.0000000E+00-0.4427173     020000\n" +
                            "  0.0000000E+00 0.0000000E+00 -1.147131     -3.476974     0.0000000E+00 101000\n" +
                            "  0.0000000E+00 0.0000000E+00 -1.302242     -3.086489     0.0000000E+00 011000\n" +
                            "  -1.286195     -2.655721     0.0000000E+00 0.0000000E+00 -1.065778     002000\n" +
                            "  0.0000000E+00 0.0000000E+00 -1.008008     -3.066368     0.0000000E+00 100100\n" +
                            "  0.0000000E+00 0.0000000E+00 -1.004786     -2.530505     0.0000000E+00 010100\n" +
                            "  -2.385299     -4.541351     0.0000000E+00 0.0000000E+00 -1.036579     001100\n" +
                            "  0.9051628     0.9379569     0.0000000E+00 0.0000000E+00 0.8368698E-01 100001\n" +
                            " -0.4353227     0.2443004     0.0000000E+00 0.0000000E+00-0.3465628E-01 010001\n" +
                            "  0.0000000E+00 0.0000000E+00 0.7897856     0.4380811     0.0000000E+00 001001\n" +
                            "  -1.088300     -1.960850     0.0000000E+00 0.0000000E+00-0.4719440     000200\n" +
                            "  0.0000000E+00 0.0000000E+00 0.3594429      1.259862     0.0000000E+00 000101\n" +
                            " -0.1496099    -0.8701740E-01 0.0000000E+00 0.0000000E+00-0.2214629     000002"
            ).apply(phaseStart);

            // XX or YY
            first = PhaseSpaceParticles.projectionToXXpPlane(phaseEndCosyFirst);
            second = PhaseSpaceParticles.projectionToXXpPlane(phaseEndCosySecond);

            first = Point2.convert(first, 1 / MM, 1 / MRAD);
            second = Point2.convert(second, 1 / MM, 1 / MRAD);
        }

        // TRACKING black
        List<Point2> tracking;
        {
            RunningParticle ip = ParticleFactory.createIdealProtonAtTrajectory250MeV(trajectory);

            List<RunningParticle> rp = ParticleFactory.createParticlesFromPhaseSpaceParticle(ip, ip.computeNaturalCoordinateSystem(), phaseStart);

            // 长度？
            double length = 0.5 + trajectoryBigR * BaseUtils.Converter.angleToRadian(dipoleCctAngle) + 0.5;
            BaseUtils.Equal.requireEqual(length,trajectory.getLength(),"不应该");

            ParticleRunner.run(ip,cct,length,MM);
//            ip = ParticleFactory.createIdealProtonAtTrajectory250MeV(trajectory, length);

            ParticleRunner.runThread(rp, cct, length, MM);

            List<PhaseSpaceParticle> rppTrack = PhaseSpaceParticles.phaseSpaceParticlesFromRunningParticles(ip, ip.computeNaturalCoordinateSystem(), rp);

            // XX or YY
            tracking = PhaseSpaceParticles.projectionToXXpPlane(rppTrack);

            tracking = Point2.convert(tracking, 1 / MM, 1 / MRAD);
        }


        Plot2d.plot2(first, Plot2d.RED_POINT);
        Plot2d.plot2(second, Plot2d.BLUE_POINT);
        Plot2d.plot2(tracking, Plot2d.BLACK_POINT);
        Plot2d.plotPoint(Point2.origin(), Plot2d.GREEN_UP_TRI);
        Plot2d.plot2circle(first, Plot2d.RED_LINE);
        Plot2d.plot2circle(second, Plot2d.BLUE_LINE);
        Plot2d.plot2circle(tracking, Plot2d.BLACK_LINE);

        Plot2d.legend(18, "cosy 1st", "cosy 2nd", "tracking", "center");
        Plot2d.info("x/mm", "xp/mrad", "x-plane", 18);

        Plot2d.equal();
        Plot2d.showThread();

    }

    @run(value = 11, code = "A0320-0321-2033")
    public void 终点对比XXP平面_动量分散() {
        Trajectory trajectory = getTrajectory();
        Cct cct = getCct();

        double delta = -5*PRESENT;

        // XX or YY
        List<PhaseSpaceParticle> phaseStart = PhaseSpaceParticles.phaseSpaceParticlesAlongPositiveEllipseInXXpPlane(
                3.5 * MM, 7.5 * MRAD, delta, 64);

        // COSY 一阶-red 和 二阶-blus
        List<Point2> first, second;
        {
            phaseStart = PhaseSpaceParticles.convertDeltaFromMomentumDispersionToEnergyDispersion(phaseStart,250);

            List<PhaseSpaceParticle> phaseEndCosyFirst = endMatrixFirst().apply(phaseStart);

            List<PhaseSpaceParticle> phaseEndCosySecond = endMatrixSecond().apply(phaseStart);

            // XX or YY
            first = PhaseSpaceParticles.projectionToXXpPlane(phaseEndCosyFirst);
            second = PhaseSpaceParticles.projectionToXXpPlane(phaseEndCosySecond);

            first = Point2.convert(first, 1 / MM, 1 / MRAD);
            second = Point2.convert(second, 1 / MM, 1 / MRAD);
        }

        phaseStart = PhaseSpaceParticles.phaseSpaceParticlesAlongPositiveEllipseInXXpPlane(
                3.5 * MM, 7.5 * MRAD, delta, 64);

        // TRACKING black
        List<Point2> tracking;
        {
            RunningParticle ip = ParticleFactory.createIdealProtonAtTrajectory250MeV(trajectory);

            List<RunningParticle> rp = ParticleFactory.createParticlesFromPhaseSpaceParticle(ip, ip.computeNaturalCoordinateSystem(), phaseStart);

            // 长度？
            double length = 0.5 + trajectoryBigR * BaseUtils.Converter.angleToRadian(dipoleCctAngle) + 0.5;
            BaseUtils.Equal.requireEqual(length,trajectory.getLength(),"不应该");

//            ParticleRunner.run(ip,cct,length,MM);
            ip = ParticleFactory.createIdealProtonAtTrajectory250MeV(trajectory, length);

            ParticleRunner.runThread(rp, cct, length, MM);

            List<PhaseSpaceParticle> rppTrack = PhaseSpaceParticles.phaseSpaceParticlesFromRunningParticles(ip, ip.computeNaturalCoordinateSystem(), rp);

            // XX or YY
            tracking = PhaseSpaceParticles.projectionToXXpPlane(rppTrack);

            tracking = Point2.convert(tracking, 1 / MM, 1 / MRAD);
        }


        Plot2d.plot2(first, Plot2d.RED_POINT);
        Plot2d.plot2(second, Plot2d.BLUE_POINT);
        Plot2d.plot2(tracking, Plot2d.BLACK_POINT);
        Plot2d.plotPoint(Point2.origin(), Plot2d.GREEN_UP_TRI);
        Plot2d.plot2circle(first, Plot2d.RED_LINE);
        Plot2d.plot2circle(second, Plot2d.BLUE_LINE);
        Plot2d.plot2circle(tracking, Plot2d.BLACK_LINE);

        Plot2d.legend(18, "cosy 1st", "cosy 2nd", "tracking", "center");
        Plot2d.info("x/mm", "xp/mrad", "x-plane", 18);

        Plot2d.equal();
        Plot2d.showThread();

    }


    // ----------- yyp ----------- 对比

    @run(value = 6, code = "A0320-0321-1720")
    public void 起始处对比yyP平面() {
        Trajectory trajectory = getTrajectory();
        Cct cct = getCct();

        // XX or YY
        List<PhaseSpaceParticle> phaseStart = PhaseSpaceParticles.phaseSpaceParticlesAlongPositiveEllipseInYYpPlane(
                3.5 * MM, 7.5 * MRAD, 0.0, 64);

        // COSY 一阶-red 和 二阶-blus
        List<Point2> first, second;
        {
            // XX or YY
            first = PhaseSpaceParticles.projectionToYYpPlane(phaseStart);
            second = PhaseSpaceParticles.projectionToYYpPlane(phaseStart);

            first = Point2.convert(first, 1 / MM, 1 / MRAD);
            second = Point2.convert(second, 1 / MM, 1 / MRAD);
        }

        // TRACKING black
        List<Point2> tracking;
        {
            RunningParticle ip = ParticleFactory.createIdealProtonAtTrajectory250MeV(trajectory);

            List<RunningParticle> rp = ParticleFactory.createParticlesFromPhaseSpaceParticle(ip, ip.computeNaturalCoordinateSystem(), phaseStart);

            // 长度？
            double length = 0;

//            ParticleRunner.run(ip,cct,length,MM);

//            ParticleRunner.runThread(rp,cct,length,MM);

            List<PhaseSpaceParticle> rppTrack = PhaseSpaceParticles.phaseSpaceParticlesFromRunningParticles(ip, ip.computeNaturalCoordinateSystem(), rp);

            // XX or YY
            tracking = PhaseSpaceParticles.projectionToYYpPlane(rppTrack);

            tracking = Point2.convert(tracking, 1 / MM, 1 / MRAD);
        }


        Plot2d.plot2(first, Plot2d.RED_POINT);
        Plot2d.plot2(second, Plot2d.BLUE_POINT);
        Plot2d.plot2(tracking, Plot2d.BLACK_POINT);
        Plot2d.plotPoint(Point2.origin(), Plot2d.GREEN_UP_TRI);
        Plot2d.plot2circle(first, Plot2d.RED_LINE);
        Plot2d.plot2circle(second, Plot2d.BLUE_LINE);
        Plot2d.plot2circle(tracking, Plot2d.BLACK_LINE);

        Plot2d.legend(18, "cosy 1st", "cosy 2nd", "tracking", "center");
        Plot2d.info("Y/mm", "Yp/mrad", "Y-plane", 18);

        Plot2d.equal();
        Plot2d.showThread();

    }

    @run(value = 7, code = "A0320-0321-1652")
    public void 入口处对比yyP平面() {
        Trajectory trajectory = getTrajectory();
        Cct cct = getCct();

        // XX or YY
        List<PhaseSpaceParticle> phaseStart = PhaseSpaceParticles.phaseSpaceParticlesAlongPositiveEllipseInYYpPlane(
                3.5 * MM, 7.5 * MRAD, 0.0, 64);

        // COSY 一阶-red 和 二阶-blus
        List<Point2> first, second;
        {
            List<PhaseSpaceParticle> phaseEndCosyFirst = COSY.importMatrix(//
                    "   1.000000     0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00 100000\n" +
                            "  0.5000000      1.000000     0.0000000E+00 0.0000000E+00 0.0000000E+00 010000\n" +
                            "  0.0000000E+00 0.0000000E+00  1.000000     0.0000000E+00 0.0000000E+00 001000\n" +
                            "  0.0000000E+00 0.0000000E+00 0.5000000      1.000000     0.0000000E+00 000100\n" +
                            "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00  1.000000     000010\n" +
                            "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00 0.9733718E-01 000001"
            ).apply(phaseStart);

            List<PhaseSpaceParticle> phaseEndCosySecond = COSY.importMatrix(
                    "   1.000000     0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00 100000\n" +
                            "  0.5000000      1.000000     0.0000000E+00 0.0000000E+00 0.0000000E+00 010000\n" +
                            "  0.0000000E+00 0.0000000E+00  1.000000     0.0000000E+00 0.0000000E+00 001000\n" +
                            "  0.0000000E+00 0.0000000E+00 0.5000000      1.000000     0.0000000E+00 000100\n" +
                            "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00  1.000000     000010\n" +
                            "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00 0.9733718E-01 000001\n" +
                            "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00-0.1396952     020000\n" +
                            " -0.2793904     0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00 010001\n" +
                            "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00-0.1396952     000200\n" +
                            "  0.0000000E+00 0.0000000E+00-0.2793904     0.0000000E+00 0.0000000E+00 000101\n" +
                            "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00-0.8158523E-01 000002"
            ).apply(phaseStart);

            // XX or YY
            first = PhaseSpaceParticles.projectionToYYpPlane(phaseEndCosyFirst);
            second = PhaseSpaceParticles.projectionToYYpPlane(phaseEndCosySecond);

            first = Point2.convert(first, 1 / MM, 1 / MRAD);
            second = Point2.convert(second, 1 / MM, 1 / MRAD);
        }

        // TRACKING black
        List<Point2> tracking;
        {
            RunningParticle ip = ParticleFactory.createIdealProtonAtTrajectory250MeV(trajectory);

            List<RunningParticle> rp = ParticleFactory.createParticlesFromPhaseSpaceParticle(ip, ip.computeNaturalCoordinateSystem(), phaseStart);

            // 长度？
            double length = 0.5;

            // ParticleRunner.run(ip,cct,length,MM);
            ip = ParticleFactory.createIdealProtonAtTrajectory250MeV(trajectory, 0.5);

            ParticleRunner.runThread(rp, cct, length, MM);

            List<PhaseSpaceParticle> rppTrack =
                    PhaseSpaceParticles.phaseSpaceParticlesFromRunningParticles(ip, ip.computeNaturalCoordinateSystem(), rp);

            // XX or YY
            tracking = PhaseSpaceParticles.projectionToYYpPlane(rppTrack);

            tracking = Point2.convert(tracking, 1 / MM, 1 / MRAD);
        }


        Plot2d.plot2(first, Plot2d.RED_POINT);
        Plot2d.plot2(second, Plot2d.BLUE_POINT);
        Plot2d.plot2(tracking, Plot2d.BLACK_POINT);
        Plot2d.plotPoint(Point2.origin(), Plot2d.GREEN_UP_TRI);
        Plot2d.plot2circle(first, Plot2d.RED_LINE);
        Plot2d.plot2circle(second, Plot2d.BLUE_LINE);
        Plot2d.plot2circle(tracking, Plot2d.BLACK_LINE);

        Plot2d.legend(18, "cosy 1st", "cosy 2nd", "tracking", "center");
        Plot2d.info("y/mm", "yp/myrad", "y-plane", 18);

        Plot2d.setCube(10);

        Plot2d.equal();
        Plot2d.showThread();

    }

    @run(value = 8, code = "A0320-0321-1720")
    public void 第二CCT处对比yyP平面() {
        Trajectory trajectory = getTrajectory();
        Cct cct = getCct();

        // XX or YY
        List<PhaseSpaceParticle> phaseStart = PhaseSpaceParticles.phaseSpaceParticlesAlongPositiveEllipseInYYpPlane(
                3.5 * MM, 7.5 * MRAD, 0.0, 64);

        // COSY 一阶-red 和 二阶-blus
        List<Point2> first, second;
        {
            List<PhaseSpaceParticle> phaseEndCosyFirst = COSY.importMatrix(//
                    "  0.8203399     -2.917389     0.0000000E+00 0.0000000E+00-0.8351992E-01 100000\n" +
                            "  0.5222710    -0.6383543     0.0000000E+00 0.0000000E+00-0.4690332E-01 010000\n" +
                            "  0.0000000E+00 0.0000000E+00  1.177680      3.062980     0.0000000E+00 001000\n" +
                            "  0.0000000E+00 0.0000000E+00 0.7151649      2.709170     0.0000000E+00 000100\n" +
                            "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00  1.000000     000010\n" +
                            "  0.5143366E-02 0.8351992E-01 0.0000000E+00 0.0000000E+00 0.1204147     000001"
            ).apply(phaseStart);

            List<PhaseSpaceParticle> phaseEndCosySecond = COSY.importMatrix(
                    "  0.8203399     -2.917389     0.0000000E+00 0.0000000E+00-0.8351992E-01 100000\n" +
                            "  0.5222710    -0.6383543     0.0000000E+00 0.0000000E+00-0.4690332E-01 010000\n" +
                            "  0.0000000E+00 0.0000000E+00  1.177680      3.062980     0.0000000E+00 001000\n" +
                            "  0.0000000E+00 0.0000000E+00 0.7151649      2.709170     0.0000000E+00 000100\n" +
                            "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00  1.000000     000010\n" +
                            "  0.5143366E-02 0.8351992E-01 0.0000000E+00 0.0000000E+00 0.1204147     000001\n" +
                            " -0.4278456     -3.190056     0.0000000E+00 0.0000000E+00-0.8627271E-01 200000\n" +
                            " -0.3219103     -3.582959     0.0000000E+00 0.0000000E+00-0.7593380E-03 110000\n" +
                            " -0.5073188E-01 -1.085379     0.0000000E+00 0.0000000E+00-0.1481379     020000\n" +
                            "  0.0000000E+00 0.0000000E+00 0.4578178      4.268066     0.0000000E+00 101000\n" +
                            "  0.0000000E+00 0.0000000E+00 0.2571025      2.396871     0.0000000E+00 011000\n" +
                            "  0.1115915      1.812065     0.0000000E+00 0.0000000E+00-0.1030432     002000\n" +
                            "  0.0000000E+00 0.0000000E+00 0.4049345      2.591851     0.0000000E+00 100100\n" +
                            "  0.0000000E+00 0.0000000E+00 0.2274041      1.455538     0.0000000E+00 010100\n" +
                            "  0.1115915      1.812065     0.0000000E+00 0.0000000E+00-0.2111479     001100\n" +
                            "  0.1027414    -0.1079298     0.0000000E+00 0.0000000E+00 0.3165896E-01 100001\n" +
                            " -0.2360259     0.8506180     0.0000000E+00 0.0000000E+00 0.4111378E-01 010001\n" +
                            "  0.0000000E+00 0.0000000E+00-0.1012789    -0.8841852E-01 0.0000000E+00 001001\n" +
                            "  0.2329557E-01 0.3782822     0.0000000E+00 0.0000000E+00-0.2568236     000200\n" +
                            "  0.0000000E+00 0.0000000E+00-0.4539065     -1.001255     0.0000000E+00 000101\n" +
                            " -0.3669528E-02-0.1181472E-01 0.0000000E+00 0.0000000E+00-0.1009740     000002"
            ).apply(phaseStart);

            // XX or YY
            first = PhaseSpaceParticles.projectionToYYpPlane(phaseEndCosyFirst);
            second = PhaseSpaceParticles.projectionToYYpPlane(phaseEndCosySecond);

            first = Point2.convert(first, 1 / MM, 1 / MRAD);
            second = Point2.convert(second, 1 / MM, 1 / MRAD);
        }

        // TRACKING black
        List<Point2> tracking;
        {
            RunningParticle ip = ParticleFactory.createIdealProtonAtTrajectory250MeV(trajectory);

            List<RunningParticle> rp = ParticleFactory.createParticlesFromPhaseSpaceParticle(ip, ip.computeNaturalCoordinateSystem(), phaseStart);

            // 长度？
            double length = 0.5 + trajectoryBigR * BaseUtils.Converter.angleToRadian(agCctAngle0);

//            ParticleRunner.run(ip,cct,length,MM);
            ip = ParticleFactory.createIdealProtonAtTrajectory250MeV(trajectory, length);

            ParticleRunner.runThread(rp, cct, length, MM);

            List<PhaseSpaceParticle> rppTrack = PhaseSpaceParticles.phaseSpaceParticlesFromRunningParticles(ip, ip.computeNaturalCoordinateSystem(), rp);

            // XX or YY
            tracking = PhaseSpaceParticles.projectionToYYpPlane(rppTrack);

            tracking = Point2.convert(tracking, 1 / MM, 1 / MRAD);
        }


        Plot2d.plot2(first, Plot2d.RED_POINT);
        Plot2d.plot2(second, Plot2d.BLUE_POINT);
        Plot2d.plot2(tracking, Plot2d.BLACK_POINT);
        Plot2d.plotPoint(Point2.origin(), Plot2d.GREEN_UP_TRI);
        Plot2d.plot2circle(first, Plot2d.RED_LINE);
        Plot2d.plot2circle(second, Plot2d.BLUE_LINE);
        Plot2d.plot2circle(tracking, Plot2d.BLACK_LINE);

        Plot2d.legend(18, "cosy 1st", "cosy 2nd", "tracking", "center");
        Plot2d.info("y/mm", "yp/mrad", "y-plane", 18);

        Plot2d.equal();
        Plot2d.showThread();

    }

    @run(value = 9, code = "A0320-0321-2027")
    public void 出口处对比yyP平面() {
        Trajectory trajectory = getTrajectory();
        Cct cct = getCct();

        // XX or YY
        List<PhaseSpaceParticle> phaseStart = PhaseSpaceParticles.phaseSpaceParticlesAlongPositiveEllipseInYYpPlane(
                3.5 * MM, 7.5 * MRAD, 0.0, 64);

        // COSY 一阶-red 和 二阶-blus
        List<Point2> first, second;
        {
            List<PhaseSpaceParticle> phaseEndCosyFirst = COSY.importMatrix(//
                    "  0.5479324    -0.3692074     0.0000000E+00 0.0000000E+00-0.1680138     100000\n" +
                            "  0.5878008      1.428971     0.0000000E+00 0.0000000E+00-0.1154528     010000\n" +
                            "  0.0000000E+00 0.0000000E+00  1.238480     -2.412454     0.0000000E+00 001000\n" +
                            "  0.0000000E+00 0.0000000E+00 0.8830676    -0.9126992     0.0000000E+00 000100\n" +
                            "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00  1.000000     000010\n" +
                            "  0.3549835E-01 0.2827129     0.0000000E+00 0.0000000E+00 0.1522421     000001"
            ).apply(phaseStart);

            List<PhaseSpaceParticle> phaseEndCosySecond = COSY.importMatrix(
                    "  0.5479324    -0.3692074     0.0000000E+00 0.0000000E+00-0.1680138     100000\n" +
                            "  0.5878008      1.428971     0.0000000E+00 0.0000000E+00-0.1154528     010000\n" +
                            "  0.0000000E+00 0.0000000E+00  1.238480     -2.412454     0.0000000E+00 001000\n" +
                            "  0.0000000E+00 0.0000000E+00 0.8830676    -0.9126992     0.0000000E+00 000100\n" +
                            "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00  1.000000     000010\n" +
                            "  0.3549835E-01 0.2827129     0.0000000E+00 0.0000000E+00 0.1522421     000001\n" +
                            "  -1.293624     -4.590501     0.0000000E+00 0.0000000E+00-0.1156784     200000\n" +
                            " -0.9490291     -2.462184     0.0000000E+00 0.0000000E+00 0.1054597     110000\n" +
                            " -0.9894984E-01-0.1649161E-01 0.0000000E+00 0.0000000E+00-0.1574655     020000\n" +
                            "  0.0000000E+00 0.0000000E+00 0.5913558     -3.476974     0.0000000E+00 101000\n" +
                            "  0.0000000E+00 0.0000000E+00 0.2410026     -3.086489     0.0000000E+00 011000\n" +
                            "  0.4166564E-01 -2.655721     0.0000000E+00 0.0000000E+00-0.2527618     002000\n" +
                            "  0.0000000E+00 0.0000000E+00 0.5251758     -3.066368     0.0000000E+00 100100\n" +
                            "  0.0000000E+00 0.0000000E+00 0.2604667     -2.530505     0.0000000E+00 010100\n" +
                            " -0.1146237     -4.541351     0.0000000E+00 0.0000000E+00-0.4214044     001100\n" +
                            "  0.3330313     0.9379569     0.0000000E+00 0.0000000E+00 0.5452429E-01 100001\n" +
                            " -0.1582321     0.2443004     0.0000000E+00 0.0000000E+00 0.7821427E-01 010001\n" +
                            "  0.0000000E+00 0.0000000E+00-0.1032713     0.4380811     0.0000000E+00 001001\n" +
                            " -0.1078746     -1.960850     0.0000000E+00 0.0000000E+00-0.3555751     000200\n" +
                            "  0.0000000E+00 0.0000000E+00-0.5254877      1.259862     0.0000000E+00 000101\n" +
                            " -0.2711393E-01-0.8701740E-01 0.0000000E+00 0.0000000E+00-0.1287123     000002"
            ).apply(PhaseSpaceParticles.convertDeltaFromMomentumDispersionToEnergyDispersion(phaseStart,250));

            // XX or YY
            first = PhaseSpaceParticles.projectionToYYpPlane(phaseEndCosyFirst);
            second = PhaseSpaceParticles.projectionToYYpPlane(phaseEndCosySecond);

            first = Point2.convert(first, 1 / MM, 1 / MRAD);
            second = Point2.convert(second, 1 / MM, 1 / MRAD);
        }

        // TRACKING black
        List<Point2> tracking;
        {
            RunningParticle ip = ParticleFactory.createIdealProtonAtTrajectory250MeV(trajectory);

            List<RunningParticle> rp = ParticleFactory.createParticlesFromPhaseSpaceParticle(ip, ip.computeNaturalCoordinateSystem(), phaseStart);

            // 长度？
            double length = 0.5 + trajectoryBigR * BaseUtils.Converter.angleToRadian(dipoleCctAngle);

//            ParticleRunner.run(ip,cct,length,MM);
            ip = ParticleFactory.createIdealProtonAtTrajectory250MeV(trajectory, length);

            ParticleRunner.runThread(rp, cct, length, MM);

            List<PhaseSpaceParticle> rppTrack = PhaseSpaceParticles.phaseSpaceParticlesFromRunningParticles(ip, ip.computeNaturalCoordinateSystem(), rp);

            // XX or YY
            tracking = PhaseSpaceParticles.projectionToYYpPlane(rppTrack);

            tracking = Point2.convert(tracking, 1 / MM, 1 / MRAD);
        }


        Plot2d.plot2(first, Plot2d.RED_POINT);
        Plot2d.plot2(second, Plot2d.BLUE_POINT);
        Plot2d.plot2(tracking, Plot2d.BLACK_POINT);
        Plot2d.plotPoint(Point2.origin(), Plot2d.GREEN_UP_TRI);
        Plot2d.plot2circle(first, Plot2d.RED_LINE);
        Plot2d.plot2circle(second, Plot2d.BLUE_LINE);
        Plot2d.plot2circle(tracking, Plot2d.BLACK_LINE);

        Plot2d.legend(18, "cosy 1st", "cosy 2nd", "tracking", "center");
        Plot2d.info("y/mm", "yp/mrad", "y-plane", 18);

        Plot2d.equal();
        Plot2d.showThread();

    }

    @run(value = 10, code = "A0320-0321-2033")
    public void 终点对比yyP平面() {
        Trajectory trajectory = getTrajectory();
        Cct cct = getCct();

        // XX or YY
        List<PhaseSpaceParticle> phaseStart = PhaseSpaceParticles.phaseSpaceParticlesAlongPositiveEllipseInYYpPlane(
                3.5 * MM, 7.5 * MRAD, 0.0, 64);

        // COSY 一阶-red 和 二阶-blus
        List<Point2> first, second;
        {
            List<PhaseSpaceParticle> phaseEndCosyFirst = COSY.importMatrix(//
                    "  0.3633288    -0.3692074     0.0000000E+00 0.0000000E+00-0.1680138     100000\n" +
                            "   1.302286      1.428971     0.0000000E+00 0.0000000E+00-0.1154528     010000\n" +
                            "  0.0000000E+00 0.0000000E+00 0.3225315E-01 -2.412454     0.0000000E+00 001000\n" +
                            "  0.0000000E+00 0.0000000E+00 0.4267180    -0.9126992     0.0000000E+00 000100\n" +
                            "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00  1.000000     000010\n" +
                            "  0.1768548     0.2827129     0.0000000E+00 0.0000000E+00 0.2495793     000001"
            ).apply(phaseStart);

            List<PhaseSpaceParticle> phaseEndCosySecond = COSY.importMatrix(
                    "  0.3633288    -0.3692074     0.0000000E+00 0.0000000E+00-0.1680138     100000\n" +
                            "   1.302286      1.428971     0.0000000E+00 0.0000000E+00-0.1154528     010000\n" +
                            "  0.0000000E+00 0.0000000E+00 0.3225315E-01 -2.412454     0.0000000E+00 001000\n" +
                            "  0.0000000E+00 0.0000000E+00 0.4267180    -0.9126992     0.0000000E+00 000100\n" +
                            "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00  1.000000     000010\n" +
                            "  0.1768548     0.2827129     0.0000000E+00 0.0000000E+00 0.2495793     000001\n" +
                            "  -3.588874     -4.590501     0.0000000E+00 0.0000000E+00-0.1347208     200000\n" +
                            "  -2.180121     -2.462184     0.0000000E+00 0.0000000E+00 0.2528623     110000\n" +
                            " -0.1071956    -0.1649161E-01 0.0000000E+00 0.0000000E+00-0.4427173     020000\n" +
                            "  0.0000000E+00 0.0000000E+00 -1.147131     -3.476974     0.0000000E+00 101000\n" +
                            "  0.0000000E+00 0.0000000E+00 -1.302242     -3.086489     0.0000000E+00 011000\n" +
                            "  -1.286195     -2.655721     0.0000000E+00 0.0000000E+00 -1.065778     002000\n" +
                            "  0.0000000E+00 0.0000000E+00 -1.008008     -3.066368     0.0000000E+00 100100\n" +
                            "  0.0000000E+00 0.0000000E+00 -1.004786     -2.530505     0.0000000E+00 010100\n" +
                            "  -2.385299     -4.541351     0.0000000E+00 0.0000000E+00 -1.036579     001100\n" +
                            "  0.9051628     0.9379569     0.0000000E+00 0.0000000E+00 0.8368698E-01 100001\n" +
                            " -0.4353227     0.2443004     0.0000000E+00 0.0000000E+00-0.3465628E-01 010001\n" +
                            "  0.0000000E+00 0.0000000E+00 0.7897856     0.4380811     0.0000000E+00 001001\n" +
                            "  -1.088300     -1.960850     0.0000000E+00 0.0000000E+00-0.4719440     000200\n" +
                            "  0.0000000E+00 0.0000000E+00 0.3594429      1.259862     0.0000000E+00 000101\n" +
                            " -0.1496099    -0.8701740E-01 0.0000000E+00 0.0000000E+00-0.2214629     000002"
            ).apply(PhaseSpaceParticles.convertDeltaFromMomentumDispersionToEnergyDispersion(phaseStart,250));

            // XX or YY
            first = PhaseSpaceParticles.projectionToYYpPlane(phaseEndCosyFirst);
            second = PhaseSpaceParticles.projectionToYYpPlane(phaseEndCosySecond);

            first = Point2.convert(first, 1 / MM, 1 / MRAD);
            second = Point2.convert(second, 1 / MM, 1 / MRAD);
        }

        // TRACKING black
        List<Point2> tracking;
        {
            RunningParticle ip = ParticleFactory.createIdealProtonAtTrajectory250MeV(trajectory);

            List<RunningParticle> rp = ParticleFactory.createParticlesFromPhaseSpaceParticle(ip, ip.computeNaturalCoordinateSystem(), phaseStart);

            // 长度？
            double length = 0.5 + trajectoryBigR * BaseUtils.Converter.angleToRadian(dipoleCctAngle) + 0.5;
            BaseUtils.Equal.requireEqual(length,trajectory.getLength(),"不应该");

//            ParticleRunner.run(ip,cct,length,MM);
            ip = ParticleFactory.createIdealProtonAtTrajectory250MeV(trajectory, length);

            ParticleRunner.runThread(rp, cct, length, MM);

            List<PhaseSpaceParticle> rppTrack = PhaseSpaceParticles.phaseSpaceParticlesFromRunningParticles(ip, ip.computeNaturalCoordinateSystem(), rp);

            // XX or YY
            tracking = PhaseSpaceParticles.projectionToYYpPlane(rppTrack);

            tracking = Point2.convert(tracking, 1 / MM, 1 / MRAD);
        }


        Plot2d.plot2(first, Plot2d.RED_POINT);
        Plot2d.plot2(second, Plot2d.BLUE_POINT);
        Plot2d.plot2(tracking, Plot2d.BLACK_POINT);
        Plot2d.plotPoint(Point2.origin(), Plot2d.GREEN_UP_TRI);
        Plot2d.plot2circle(first, Plot2d.RED_LINE);
        Plot2d.plot2circle(second, Plot2d.BLUE_LINE);
        Plot2d.plot2circle(tracking, Plot2d.BLACK_LINE);

        Plot2d.legend(18, "cosy 1st", "cosy 2nd", "tracking", "center");
        Plot2d.info("y/mm", "yp/mrad", "y-plane", 18);

        Plot2d.equal();
        Plot2d.showThread();

    }


    // -------------COSY map
    @run(code = "new")
    private COSY.TransMatrix entryMatrixFirst() {
        return COSY.importMatrix(
                "   1.000000     0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00 100000\n" +
                        "  0.5000000      1.000000     0.0000000E+00 0.0000000E+00 0.0000000E+00 010000\n" +
                        "  0.0000000E+00 0.0000000E+00  1.000000     0.0000000E+00 0.0000000E+00 001000\n" +
                        "  0.0000000E+00 0.0000000E+00 0.5000000      1.000000     0.0000000E+00 000100\n" +
                        "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00  1.000000     000010\n" +
                        "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00 0.9733718E-01 000001"
        );
    }

    @run(code = "new")
    private COSY.TransMatrix entryMatrixSecond() {
        return COSY.importMatrix(
                "   1.000000     0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00 100000\n" +
                        "  0.5000000      1.000000     0.0000000E+00 0.0000000E+00 0.0000000E+00 010000\n" +
                        "  0.0000000E+00 0.0000000E+00  1.000000     0.0000000E+00 0.0000000E+00 001000\n" +
                        "  0.0000000E+00 0.0000000E+00 0.5000000      1.000000     0.0000000E+00 000100\n" +
                        "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00  1.000000     000010\n" +
                        "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00 0.9733718E-01 000001\n" +
                        "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00-0.1396952     020000\n" +
                        " -0.2793904     0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00 010001\n" +
                        "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00-0.1396952     000200\n" +
                        "  0.0000000E+00 0.0000000E+00-0.2793904     0.0000000E+00 0.0000000E+00 000101\n" +
                        "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00-0.8158523E-01 000002"
        );
    }

    @run(code = "new")
    private COSY.TransMatrix afterAgCct0MatrixFirst() {
        return COSY.importMatrix(//
                "  0.8203399     -2.917389     0.0000000E+00 0.0000000E+00 0.8351992E-01 100000\n" +
                        "  0.5222710    -0.6383543     0.0000000E+00 0.0000000E+00 0.4690332E-01 010000\n" +
                        "  0.0000000E+00 0.0000000E+00  1.177680      3.062980     0.0000000E+00 001000\n" +
                        "  0.0000000E+00 0.0000000E+00 0.7151649      2.709170     0.0000000E+00 000100\n" +
                        "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00  1.000000     000010\n" +
                        " -0.5143366E-02-0.8351992E-01 0.0000000E+00 0.0000000E+00 0.1204147     000001"
        );
    }

    @run(code = "new")
    private COSY.TransMatrix afterAgCct0MatrixSecond() {
        return COSY.importMatrix(
                "  0.8203399     -2.917389     0.0000000E+00 0.0000000E+00 0.8351992E-01 100000\n" +
                        "  0.5222710    -0.6383543     0.0000000E+00 0.0000000E+00 0.4690332E-01 010000\n" +
                        "  0.0000000E+00 0.0000000E+00  1.177680      3.062980     0.0000000E+00 001000\n" +
                        "  0.0000000E+00 0.0000000E+00 0.7151649      2.709170     0.0000000E+00 000100\n" +
                        "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00  1.000000     000010\n" +
                        " -0.5143366E-02-0.8351992E-01 0.0000000E+00 0.0000000E+00 0.1204147     000001\n" +
                        "  0.4278456      3.190056     0.0000000E+00 0.0000000E+00-0.8627271E-01 200000\n" +
                        "  0.3219103      3.582959     0.0000000E+00 0.0000000E+00-0.7593380E-03 110000\n" +
                        "  0.5073188E-01  1.085379     0.0000000E+00 0.0000000E+00-0.1481379     020000\n" +
                        "  0.0000000E+00 0.0000000E+00-0.4578178     -4.268066     0.0000000E+00 101000\n" +
                        "  0.0000000E+00 0.0000000E+00-0.2571025     -2.396871     0.0000000E+00 011000\n" +
                        " -0.1115915     -1.812065     0.0000000E+00 0.0000000E+00-0.1030432     002000\n" +
                        "  0.0000000E+00 0.0000000E+00-0.4049345     -2.591851     0.0000000E+00 100100\n" +
                        "  0.0000000E+00 0.0000000E+00-0.2274041     -1.455538     0.0000000E+00 010100\n" +
                        " -0.1115915     -1.812065     0.0000000E+00 0.0000000E+00-0.2111479     001100\n" +
                        "  0.1027414    -0.1079298     0.0000000E+00 0.0000000E+00-0.3165896E-01 100001\n" +
                        " -0.2360259     0.8506180     0.0000000E+00 0.0000000E+00-0.4111378E-01 010001\n" +
                        "  0.0000000E+00 0.0000000E+00-0.1012789    -0.8841852E-01 0.0000000E+00 001001\n" +
                        " -0.2329557E-01-0.3782822     0.0000000E+00 0.0000000E+00-0.2568236     000200\n" +
                        "  0.0000000E+00 0.0000000E+00-0.4539065     -1.001255     0.0000000E+00 000101\n" +
                        "  0.3669528E-02 0.1181472E-01 0.0000000E+00 0.0000000E+00-0.1009740     000002"
        );
    }

    @run(code = "new")
    private COSY.TransMatrix exitMatrixFirst() {
        return COSY.importMatrix(//
                "  0.5479324    -0.3692074     0.0000000E+00 0.0000000E+00 0.1680138     100000\n" +
                        "  0.5878008      1.428971     0.0000000E+00 0.0000000E+00 0.1154528     010000\n" +
                        "  0.0000000E+00 0.0000000E+00  1.238480     -2.412454     0.0000000E+00 001000\n" +
                        "  0.0000000E+00 0.0000000E+00 0.8830676    -0.9126992     0.0000000E+00 000100\n" +
                        "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00  1.000000     000010\n" +
                        " -0.3549835E-01-0.2827129     0.0000000E+00 0.0000000E+00 0.1522421     000001"
        );
    }

    @run(code = "new")
    private COSY.TransMatrix exitMatrixSecond() {
        return COSY.importMatrix(
                "  0.5479324    -0.3692074     0.0000000E+00 0.0000000E+00 0.1680138     100000\n" +
                        "  0.5878008      1.428971     0.0000000E+00 0.0000000E+00 0.1154528     010000\n" +
                        "  0.0000000E+00 0.0000000E+00  1.238480     -2.412454     0.0000000E+00 001000\n" +
                        "  0.0000000E+00 0.0000000E+00 0.8830676    -0.9126992     0.0000000E+00 000100\n" +
                        "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00  1.000000     000010\n" +
                        " -0.3549835E-01-0.2827129     0.0000000E+00 0.0000000E+00 0.1522421     000001\n" +
                        "   1.293624      4.590501     0.0000000E+00 0.0000000E+00-0.1156784     200000\n" +
                        "  0.9490291      2.462184     0.0000000E+00 0.0000000E+00 0.1054597     110000\n" +
                        "  0.9894984E-01 0.1649161E-01 0.0000000E+00 0.0000000E+00-0.1574655     020000\n" +
                        "  0.0000000E+00 0.0000000E+00-0.5913558      3.476974     0.0000000E+00 101000\n" +
                        "  0.0000000E+00 0.0000000E+00-0.2410026      3.086489     0.0000000E+00 011000\n" +
                        " -0.4166564E-01  2.655721     0.0000000E+00 0.0000000E+00-0.2527618     002000\n" +
                        "  0.0000000E+00 0.0000000E+00-0.5251758      3.066368     0.0000000E+00 100100\n" +
                        "  0.0000000E+00 0.0000000E+00-0.2604667      2.530505     0.0000000E+00 010100\n" +
                        "  0.1146237      4.541351     0.0000000E+00 0.0000000E+00-0.4214044     001100\n" +
                        "  0.3330313     0.9379569     0.0000000E+00 0.0000000E+00-0.5452429E-01 100001\n" +
                        " -0.1582321     0.2443004     0.0000000E+00 0.0000000E+00-0.7821427E-01 010001\n" +
                        "  0.0000000E+00 0.0000000E+00-0.1032713     0.4380811     0.0000000E+00 001001\n" +
                        "  0.1078746      1.960850     0.0000000E+00 0.0000000E+00-0.3555751     000200\n" +
                        "  0.0000000E+00 0.0000000E+00-0.5254877      1.259862     0.0000000E+00 000101\n" +
                        "  0.2711393E-01 0.8701740E-01 0.0000000E+00 0.0000000E+00-0.1287123     000002"
        );
    }

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

    //---------------------- 以下 全部丢弃

    @run(-1000)
    public void x变化() {
        Cct cct = getCct();
        Trajectory trajectory = getTrajectory();
        RunningParticle ip = ParticleFactory.createIdealProtonAtTrajectory250MeV(trajectory);

        List<Point2>[] collect = ParticleRunner
                .runGetAllInfo(ip, cct, trajectory.getLength(), MM)
                .stream()
                .map(bi -> {
                    Double distance = bi.getT1();
                    RunningParticle particle = bi.getT2();

                    RunningParticle refer = ParticleFactory.createIdealProtonAtTrajectory250MeV(trajectory, distance);

                    PhaseSpaceParticle phaseSpaceParticle =

                            PhaseSpaceParticles.phaseSpaceParticleFromRunningParticle(refer, refer.computeNaturalCoordinateSystem(), particle);

                    return BaseUtils.Content.BiContent.create(distance, phaseSpaceParticle);
                })
                .map(bi -> {
                    Double distance = bi.getT1();
                    PhaseSpaceParticle phaseSpaceParticle = bi.getT2();

                    Point2 point2 = PhaseSpaceParticles.projectionToXXpPlane(phaseSpaceParticle);

                    double x = point2.x;
                    double xp = point2.y;

                    return BaseUtils.Content.TriContent.create(distance, x, xp);
                })
                .collect(
                        () -> {
                            List<Point2>[] lists = new ArrayList[2];
                            lists[0] = new ArrayList<>();
                            lists[1] = new ArrayList<>();

                            return lists;
                        },
                        (List<Point2>[] lists, BaseUtils.Content.TriContent<Double, Double, Double> tri) -> {
                            lists[0].add(Point2.create(tri.getT1(), tri.getT2()));
                            lists[1].add(Point2.create(tri.getT1(), tri.getT3()));
                        },
                        (List<Point2>[] lists1, List<Point2>[] lists2) -> {
                            lists1[0].addAll(lists2[0]);
                            lists1[1].addAll(lists2[1]);
                        }
                );

        Plot2d.plot2(collect[0], Plot2d.RED_LINE);
//        Plot2d.plot2(collect[1],Plot2d.BLUE_LINE);

        Plot2d.legend(18, "x");
//        Plot2d.legend(18,"x","xp");

        Plot2d.showThread();

    }

    @run(-1)
    public void 回顾二极CCT四极场() {
        Trajectory trajectory = getTrajectory();
        Cct dipoleCct = createDipoleCct();

        List<Point2> list = dipoleCct.magnetGradientAlongTrajectoryFast(trajectory, MM, 3 * MM);

        Plot2d.plot2(list, Plot2d.RED_LINE);

        Plot2d.showThread();
    }

    @run(-2)
    public void 简单单粒子跟踪() {
        Trajectory trajectory = getTrajectory();
        Cct cct = getCct();

        RunningParticle ip = ParticleFactory.createIdealProtonAtTrajectory250MeV(trajectory);

        List<Point3> ipT = ParticleRunner.run(ip, cct, trajectory.getLength(), MM);

        trajectory.plot3d(Plot2d.BLACK_LINE);

        Plot3d.plot3(ipT, Plot2d.RED_LINE);

        Plot3d.setCenter(trajectory.pointAt(trajectory.getLength()).toPoint3(), trajectory.getLength() / 2 * 1.1);
        Plot3d.showThread();
    }

    @run(-3)
    public void 悄悄试一试不同动量的粒子() {
        Cct cct = getCct();
        Cct dipoleCct = createDipoleCct();
        Trajectory trajectory = getTrajectory();

        double dpp0 = 5 * PRESENT;
        BaseUtils.Python.linspaceStream(-dpp0, dpp0, switcher.getSize())
                .parallel()
                .map(dpp -> 250 * (1 + dpp))
                .mapToObj(dpp -> {
                    RunningParticle p = ParticleFactory.createIdealProtonAtTrajectory(trajectory, dpp);
                    List<Point3> pT = ParticleRunner.run(p, cct, trajectory.getLength(), MM);

                    return BaseUtils.Content.BiContent.create(dpp, pT);
                })
                .collect(Collectors.toList())
                .stream()
                .sorted(Comparator.comparingDouble(BaseUtils.Content.BiContent::getT1))
                .peek(bi -> Plot3d.plot3(bi.getT2(), switcher.getAndSwitch()))
                .map(BaseUtils.Content.BiContent::getT1)
                .map(dpp -> BaseUtils.Doubles.reservedDecimal(dpp, 3))
                .map(dpp -> "momentum = " + dpp + "MeV")
                .forEach(dpp -> Logger.getLogger().info(dpp));

        trajectory.plot3d(Plot2d.GREY_DASH);

        Plot3d.setCenter(trajectory.pointAt(trajectory.getLength()).toPoint3(), trajectory.getLength() / 2 * 1.1);


        Plot3d.showThread();

    }

    @run(-4) //奇怪啊
    public void 有没有四极场CCT没有区别吗() {
        Trajectory trajectory = getTrajectory();
        Cct dipoleCct = createDipoleCct();
        Cct cct = getCct();

        RunningParticle p = ParticleFactory.createIdealProtonAtTrajectory(trajectory, 220);
        RunningParticle p2 = ParticleFactory.createIdealProtonAtTrajectory(trajectory, 220);

        List<Point3> pT = ParticleRunner.run(p, cct, trajectory.getLength(), MM);
        List<Point3> p2T = ParticleRunner.run(p2, dipoleCct, trajectory.getLength(), MM);

        trajectory.plot3d(Plot2d.BLACK_LINE);

        Plot3d.plot3(pT, Plot2d.RED_LINE);
        Plot3d.plot3(p2T, Plot2d.YELLOW_LINE);

        Plot3d.setCenter(trajectory.pointAt(trajectory.getLength()).toPoint3(), trajectory.getLength() / 2 * 1.1);
        Plot3d.showThread();
    }

    @run(-15)
    public void 相椭圆() {
        Cct cct = getCct();
        Trajectory trajectory = getTrajectory();
        trajectory.plot3d();

        RunningParticle ip = ParticleFactory.createIdealProtonAtTrajectory250MeV(trajectory);

        boolean xxPlane = true;
        List<PhaseSpaceParticle> phaseSpacePsStart
                = xxPlane ?
                PhaseSpaceParticles.phaseSpaceParticlesAlongPositiveEllipseInXXpPlane(3.5 * MM, 7.5 * MRAD, 0.0, 16) :
                PhaseSpaceParticles.phaseSpaceParticlesAlongPositiveEllipseInYYpPlane(3.5 * MM, 7.5 * MRAD, 0.0, 1);

        List<RunningParticle> ps = ParticleFactory.createParticlesFromPhaseSpaceParticle(ip, ip.computeNaturalCoordinateSystem(), phaseSpacePsStart);

        List<Point3> ipT = ParticleRunner.run(ip, cct, trajectory.getLength(), MM);
        List<List<Point3>> psTs = ParticleRunner.runThread(ps, cct, trajectory.getLength(), MM);

        Plot3d.plot3(ipT, Plot2d.RED_LINE);
        psTs.forEach(psT -> Plot3d.plot3(psT, Plot2d.YELLOW_LINE));

        // 使用 参考粒子ip 获取出口相椭圆
//        List<PhaseSpaceParticle> phaseSpacePsEnd = PhaseSpaceParticles.phaseSpaceParticlesFromRunningParticles(ip, ip.computeNaturalCoordinateSystem(), ps);
        // 使用 轨道理想粒子tp 获取出口相椭圆
        RunningParticle tp = ParticleFactory.createIdealProtonAtTrajectory250MeV(trajectory, trajectory.getLength());
        List<PhaseSpaceParticle> phaseSpacePsEnd = PhaseSpaceParticles.phaseSpaceParticlesFromRunningParticles(tp, tp.computeNaturalCoordinateSystem(), ps);

        List<Point2> phaseStart;
        List<Point2> phaseEnd;

        if (xxPlane) {
            phaseStart = PhaseSpaceParticles.projectionToXXpPlane(phaseSpacePsStart);
            phaseStart = Point2.convert(phaseStart, 1000, 1000);
            phaseEnd = PhaseSpaceParticles.projectionToXXpPlane(phaseSpacePsEnd);
            phaseEnd = Point2.convert(phaseEnd, 1000, 1000);
        } else {
            phaseStart = PhaseSpaceParticles.projectionToYYpPlane(phaseSpacePsStart);
            phaseStart = Point2.convert(phaseStart, 1000, 1000);
            phaseEnd = PhaseSpaceParticles.projectionToYYpPlane(phaseSpacePsEnd);
            phaseEnd = Point2.convert(phaseEnd, 1000, 1000);
        }

//        Plot2d.plot2(phaseStart, Plot2d.BLACK_POINT);
        Plot2d.plot2circle(excel_map_computer160(), Plot2d.BLACK_LINE);
        Plot2d.plot2circle(phaseEnd, Plot2d.RED_LINE);


        Plot2d.legend(18, "cosy", "track");
        Plot2d.info(xxPlane ? "x/mm" : "y/mm", xxPlane ? "yp/mrad" : "yp/mrad", xxPlane ? "x-plane" : "y-plane", 18);

        Plot2d.equal();
        Plot2d.showThread();
        Plot3d.setCenter(trajectory.pointAt(trajectory.getLength()).toPoint3(), trajectory.getLength() / 2 * 1.1);
        Plot3d.showThread();
    }

    @run(-17)
    public void 相椭圆上粒子toExcel160() {
        List<PhaseSpaceParticle> phaseSpaceParticles = PhaseSpaceParticles.phaseSpaceParticlesAlongPositiveEllipseInXXpPlane(3.5 * MM, 7.5 * MRAD, 0.0, 16);

        phaseSpaceParticles.forEach(phaseSpaceParticle -> System.out.println(phaseSpaceParticle.toExcelMapComputer()));
    }

    @run(-7) //more
    public List<Point2> excel_map_computer160() {
        List<Point2> list = new ArrayList<>();
        list.add(Point2.create(0.00055345731875, -0.00134845953725));
        list.add(Point2.create(0.00102218760740573, -0.00107200914746141));
        list.add(Point2.create(0.00148002903889594, -0.000800533921267074));
        list.add(Point2.create(0.00194211245798313, -0.000525076219923338));
        list.add(Point2.create(0.00240438934105184, -0.000248009181452447));
        list.add(Point2.create(0.00286232154324102, 2.79511337753647E-05));
        list.add(Point2.create(0.00331746811072354, 0.000303737897739009));
        list.add(Point2.create(0.003771117238331, 0.00058014198144659));
        list.add(Point2.create(0.00423161746022106, 0.000862315887482037));
        list.add(Point2.create(0.00468539008311454, 0.00114198231510964));
        list.add(Point2.create(0.00513220245807651, 0.00141897378351851));
        list.add(Point2.create(0.00558772271188228, 0.0017030673885478));
        list.add(Point2.create(0.0060359302577667, 0.00198434156728882));
        list.add(Point2.create(0.00648434383980028, 0.00226754365126791));
        list.add(Point2.create(0.00692323945259507, 0.00254655306365592));
        list.add(Point2.create(0.00736964114756845, 0.00283226821266335));
        list.add(Point2.create(0.00781360649936275, 0.00311845974179763));
        list.add(Point2.create(0.00824353744404952, 0.00339765003557789));
        list.add(Point2.create(0.00867776275671405, 0.0036817977602168));
        list.add(Point2.create(0.00911540058461164, 0.00397053257632498));
        list.add(Point2.create(0.00954416080947594, 0.00425587170074666));
        list.add(Point2.create(0.00996176362508899, 0.00453631532738054));
        list.add(Point2.create(0.0103892007941523, 0.00482617553959379));
        list.add(Point2.create(0.0108015873303173, 0.0051087855794893));
        list.add(Point2.create(0.0112084061502129, 0.00539072473992216));
        list.add(Point2.create(0.0116196821974713, 0.00567930064926007));
        list.add(Point2.create(0.01200892691595, 0.0059561264161806));
        list.add(Point2.create(0.0123979892373893, 0.00623694657036789));
        list.add(Point2.create(0.0127841464216248, 0.00652040645618544));
        list.add(Point2.create(0.0131524065823771, 0.00679593355309817));
        list.add(Point2.create(0.0135114628018033, 0.00707049545971851));
        list.add(Point2.create(0.0138574062744541, 0.00734190288951887));
        list.add(Point2.create(0.0141859002104593, 0.00760764643788833));
        list.add(Point2.create(0.014482753524517, 0.00785677803923734));
        list.add(Point2.create(0.0147714710890533, 0.00811064910948179));
        list.add(Point2.create(0.0150185471212786, 0.00834162391176684));
        list.add(Point2.create(0.015240686811308, 0.00856759060845006));
        list.add(Point2.create(0.0154107147739131, 0.00876351873874968));
        list.add(Point2.create(0.0155374834419485, 0.00894298597331973));
        list.add(Point2.create(0.0156036795899064, 0.00908577619931706));
        list.add(Point2.create(0.0156100850974162, 0.00920076873735775));
        list.add(Point2.create(0.0155526038912965, 0.00927491028772526));
        list.add(Point2.create(0.0154377252982948, 0.00931093796814804));
        list.add(Point2.create(0.0152611551596384, 0.00931155276759131));
        list.add(Point2.create(0.0150447481842013, 0.00927962957307476));
        list.add(Point2.create(0.014775440115792, 0.00921581993349775));
        list.add(Point2.create(0.0144848082369691, 0.00912973216815771));
        list.add(Point2.create(0.0141520347131134, 0.00901712547888086));
        list.add(Point2.create(0.0138148245457015, 0.00889235293403367));
        list.add(Point2.create(0.0134456944882814, 0.00874669442075872));
        list.add(Point2.create(0.0130602874465285, 0.00858679320811145));
        list.add(Point2.create(0.0126630171930334, 0.0084152976091183));
        list.add(Point2.create(0.012257843422655, 0.00823465660628144));
        list.add(Point2.create(0.0118349925237932, 0.00804094467074823));
        list.add(Point2.create(0.0114106893391873, 0.00784199889081746));
        list.add(Point2.create(0.0109876648188724, 0.00763968186598056));
        list.add(Point2.create(0.0105420904128848, 0.00742279038230132));
        list.add(Point2.create(0.0101025865177454, 0.00720544636845172));
        list.add(Point2.create(0.00965819874786525, 0.0069825792473578));
        list.add(Point2.create(0.00919868267613395, 0.00674912862896843));
        list.add(Point2.create(0.00875072579117132, 0.00651885481566328));
        list.add(Point2.create(0.00829175198555217, 0.00628036230818697));
        list.add(Point2.create(0.00782420715097421, 0.00603493828908654));
        list.add(Point2.create(0.0073611983769256, 0.0057895852383842));
        list.add(Point2.create(0.00690360704207323, 0.00554497389966884));
        list.add(Point2.create(0.00643192500796325, 0.00529073204874549));
        list.add(Point2.create(0.00595850808598661, 0.00503351732272269));
        list.add(Point2.create(0.005493868186558, 0.00477917366427911));
        list.add(Point2.create(0.00501997778199303, 0.0045179087070089));
        list.add(Point2.create(0.00454714245243406, 0.00425542478860085));
        list.add(Point2.create(0.0040674415324925, 0.00398735319662718));
        list.add(Point2.create(0.00359775146840443, 0.00372319404994463));
        list.add(Point2.create(0.00312160427496623, 0.00345375130478859));
        list.add(Point2.create(0.00263929088487528, 0.0031791630155739));
        list.add(Point2.create(0.00216504665602094, 0.00290757813691958));
        list.add(Point2.create(0.00169014780268706, 0.00263406573544486));
        list.add(Point2.create(0.0012132758081965, 0.00235787771648632));
        list.add(Point2.create(0.00073285558094528, 0.00207809391881907));
        list.add(Point2.create(0.000253640053391918, 0.00179748590568968));
        list.add(Point2.create(-0.000220160163036562, 0.00151856111375171));
        list.add(Point2.create(-0.000704150602399044, 0.00123211614245753));
        list.add(Point2.create(-0.00118126806737487, 0.000948235336347593));
        list.add(Point2.create(-0.00165406538623506, 0.000665451361231126));
        list.add(Point2.create(-0.00213158927015207, 0.000378343566333862));
        list.add(Point2.create(-0.00260962272578416, 8.94128323531533E-05));
        list.add(Point2.create(-0.00308343008429618, -0.000198476046443835));
        list.add(Point2.create(-0.00355457910311266, -0.000486262946614191));
        list.add(Point2.create(-0.0040243605647562, -0.00077474055940061));
        list.add(Point2.create(-0.00450138092894214, -0.00106925345160516));
        list.add(Point2.create(-0.00497153145010646, -0.00136112780164436));
        list.add(Point2.create(-0.00543452291875849, -0.00165015438904349));
        list.add(Point2.create(-0.00590654474147132, -0.0019464971392398));
        list.add(Point2.create(-0.0063709476623945, -0.00223976681166038));
        list.add(Point2.create(-0.00683547208991212, -0.00253487452271849));
        list.add(Point2.create(-0.00728999544432073, -0.00282540487796288));
        list.add(Point2.create(-0.00775209221441034, -0.00312266094412145));
        list.add(Point2.create(-0.00821140586656725, -0.00342010681466237));
        list.add(Point2.create(-0.00865588866788868, -0.00370993520442931));
        list.add(Point2.create(-0.00910443212240715, -0.0040045152850624));
        list.add(Point2.create(-0.00955603855532996, -0.00430338894813261));
        list.add(Point2.create(-0.00999795091413306, -0.00459823238263934));
        list.add(Point2.create(-0.0104277681951322, -0.00488745791530666));
        list.add(Point2.create(-0.0108669969655065, -0.00518574379207901));
        list.add(Point2.create(-0.0112899668157871, -0.0054758579471491));
        list.add(Point2.create(-0.0117063385225077, -0.00576450408841543));
        list.add(Point2.create(-0.0121262275981245, -0.00605904512518272));
        list.add(Point2.create(-0.0125224940816618, -0.0063406378601382));
        list.add(Point2.create(-0.0129172838137347, -0.00662522218699611));
        list.add(Point2.create(-0.013307612603675, -0.00691124909214736));
        list.add(Point2.create(-0.0136781656697687, -0.00718792559107463));
        list.add(Point2.create(-0.0140375261810117, -0.00746212365845949));
        list.add(Point2.create(-0.0143815189270425, -0.00773145951826273));
        list.add(Point2.create(-0.0147055530092321, -0.00799323999967807));
        list.add(Point2.create(-0.0149954875068966, -0.00823658292769826));
        list.add(Point2.create(-0.0152738148833903, -0.00848203049946781));
        list.add(Point2.create(-0.0155077425591842, -0.00870255265653396));
        list.add(Point2.create(-0.0157125135263378, -0.00891489818720267));
        list.add(Point2.create(-0.0158624535542456, -0.00909523185175853));
        list.add(Point2.create(-0.0159646022387377, -0.00925573925564347));
        list.add(Point2.create(-0.0160040386255268, -0.00937805827097414));
        list.add(Point2.create(-0.015979530640477, -0.00946953681550589));
        list.add(Point2.create(-0.0158901839463862, -0.00951956333831849));
        list.add(Point2.create(-0.0157433398368773, -0.00953150860376656));
        list.add(Point2.create(-0.0155334973316662, -0.00950716141293872));
        list.add(Point2.create(-0.0152861036466611, -0.00945208316985112));
        list.add(Point2.create(-0.0149857668135876, -0.00936517610946185));
        list.add(Point2.create(-0.0146672979099621, -0.00925844567926548));
        list.add(Point2.create(-0.0143074914576902, -0.00912586962592675));
        list.add(Point2.create(-0.0139467526879673, -0.00898378514104313));
        list.add(Point2.create(-0.0135553196486146, -0.00882178438076928));
        list.add(Point2.create(-0.0131497412850273, -0.00864717023449935));
        list.add(Point2.create(-0.0127344546895428, -0.0084625997136629));
        list.add(Point2.create(-0.0123133879993968, -0.00827049026776336));
        list.add(Point2.create(-0.0118762802371992, -0.00806655883951617));
        list.add(Point2.create(-0.0114397887622513, -0.00785894620718614));
        list.add(Point2.create(-0.0110065065512268, -0.00764940631162664));
        list.add(Point2.create(-0.010551980278304, -0.00742629179106348));
        list.add(Point2.create(-0.01010534766555, -0.00720407922870068));
        list.add(Point2.create(-0.00965533135561395, -0.0069774664295114));
        list.add(Point2.create(-0.00919153952467724, -0.00674129170357477));
        list.add(Point2.create(-0.00874081892316647, -0.00650940564197752));
        list.add(Point2.create(-0.00828035473625163, -0.00627025602707983));
        list.add(Point2.create(-0.0078125978874142, -0.00602512979331786));
        list.add(Point2.create(-0.0073505977312832, -0.0057809723938466));
        list.add(Point2.create(-0.00689512359968857, -0.00553836945793596));
        list.add(Point2.create(-0.00642672226095475, -0.00528700685085051));
        list.add(Point2.create(-0.00595765555870319, -0.00503345942584611));
        list.add(Point2.create(-0.0054982576999216, -0.00478342866934648));
        list.add(Point2.create(-0.00503065124051858, -0.0045272490393447));
        list.add(Point2.create(-0.00456497785604794, -0.00427049093314515));
        list.add(Point2.create(-0.0040934031499693, -0.00400885548584362));
        list.add(Point2.create(-0.00363246289381497, -0.00375157503409377));
        list.add(Point2.create(-0.00316594223584097, -0.00348965426058861));
        list.add(Point2.create(-0.00269411236760112, -0.0032232150606065));
        list.add(Point2.create(-0.00223085318902606, -0.00296013105890042));
        list.add(Point2.create(-0.00176758689131194, -0.00269558569254114));
        list.add(Point2.create(-0.0013029933596717, -0.00242882979821348));
        list.add(Point2.create(-0.00083550939178052, -0.00215895212973553));
        list.add(Point2.create(-0.000369725623590082, -0.00188859997575652));
        list.add(Point2.create(9.03134306346381E-05, -0.00162015467022769));


//        Plot2d.plot2(list,Plot2d.BLACK_POINT);
//
//        Plot2d.equal();
//
//        Plot2d.showThread();

        return Point2.convert(list, 1000, 1000);
    }

    @run(-16) //完成
    public void 修改粒子run方法让他跑的距离更准确() {
        Trajectory trajectory = getTrajectory();
        Cct cct = getCct();

        double trajectoryLength = trajectory.getLength();

        Logger.getLogger().info("trajectoryLength = " + trajectoryLength);

        RunningParticle ip0 = ParticleFactory.createIdealProtonAtTrajectory250MeV(trajectory);
        RunningParticle ip1 = ParticleFactory.createIdealProtonAtTrajectory250MeV(trajectory, trajectoryLength);

        ParticleRunner.run(ip0, cct, trajectoryLength, 100 * MM);

        Logger.getLogger().info("ip0.getDistance() = " + ip0.getDistance());
    }

    @run(-10000)
    public void 先画个图() {
        Trajectory trajectory = getTrajectory();
        Cct agCct = createAgCct();
        Cct dipoleCct = createDipoleCct();

        trajectory.plot3d();

        agCct.plot3(Plot2d.BLUE_LINE, Plot2d.GREEN_LINE, Plot2d.BLUE_LINE, Plot2d.GREEN_LINE);
        dipoleCct.plot3(Plot2d.RED_LINE, Plot2d.RED_LINE);

        Plot3d.setCenter(trajectory.pointAt(trajectory.getLength() / 2).toPoint3(), trajectoryBigR);
        Plot3d.showThread();
    }

    @run(-18)
    public void 入口处相椭圆() {
        Trajectory trajectory = getTrajectory();

        RunningParticle ip = ParticleFactory.createIdealProtonAtTrajectory250MeV(trajectory);

        List<PhaseSpaceParticle> pp0 = PhaseSpaceParticles.phaseSpaceParticlesAlongPositiveEllipseInXXpPlane(3.5 * MM, 7.5 * MRAD, 0.0, 16);

        List<Point2> ppoXX = PhaseSpaceParticles.projectionToXXpPlane(pp0);

        ppoXX = Point2.convert(ppoXX, 1 / MM, 1 / MRAD);

        Plot2d.plot2(ppoXX, Plot2d.BLACK_POINT);

        Plot2d.info("y/mm", "yp/mrad", "", 18);
        Plot2d.legend(18, "starting point");

        Plot2d.equal();

        Plot2d.showThread();
    }

    @run(-10000)
    public void cosy矩阵导入测试() {
        String s = "  0.1027073     0.8598542     0.0000000E+00 0.0000000E+00-0.1064929     100000\n" +
                " -0.7719973      3.273330     0.0000000E+00 0.0000000E+00 0.3404825     010000\n" +
                "  0.0000000E+00 0.0000000E+00  7.045588    -0.3133395     0.0000000E+00 001000\n" +
                "  0.0000000E+00 0.0000000E+00  5.088447    -0.8436644E-01 0.0000000E+00 000100\n" +
                "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00  1.000000     000010\n" +
                " -0.4724219E-01 0.6413517     0.0000000E+00 0.0000000E+00  2.282058     000001\n" +
                "  -5.062997     -8.199716     0.0000000E+00 0.0000000E+00 -2.257327     200000\n" +
                "  -55.52304     -18.84660     0.0000000E+00 0.0000000E+00 -6.707541     110000\n" +
                "  -109.9309      35.27381     0.0000000E+00 0.0000000E+00 -8.903395     020000\n" +
                "  0.0000000E+00 0.0000000E+00  322.0610     -28.23742     0.0000000E+00 101000\n" +
                "  0.0000000E+00 0.0000000E+00  1218.509     -215.4256     0.0000000E+00 011000\n" +
                "   96.17872      327.9441     0.0000000E+00 0.0000000E+00 -56.40707     002000\n" +
                "  0.0000000E+00 0.0000000E+00  240.4671     -27.23141     0.0000000E+00 100100\n" +
                "  0.0000000E+00 0.0000000E+00  888.2583     -180.4969     0.0000000E+00 010100\n" +
                "   191.9755      472.7754     0.0000000E+00 0.0000000E+00 -83.62065     001100\n" +
                "  -13.53475      1.419701     0.0000000E+00 0.0000000E+00-0.3130132     100001\n" +
                "  -49.32543      7.740638     0.0000000E+00 0.0000000E+00 -3.430807     010001\n" +
                "  0.0000000E+00 0.0000000E+00  224.3108     -15.87173     0.0000000E+00 001001\n" +
                "   88.97256      169.0659     0.0000000E+00 0.0000000E+00 -31.67421     000200\n" +
                "  0.0000000E+00 0.0000000E+00  155.6404     -15.69868     0.0000000E+00 000101\n" +
                "  -5.227584    -0.3088457     0.0000000E+00 0.0000000E+00 -2.650967     000002";

        COSY.TransMatrix transMatrix = COSY.importMatrix(s);

        Logger.getLogger().info("transMatrix = " + transMatrix);

    }

    @run(-1000)
    public void COSY计算测试() {
        COSY.TransMatrix matrix = COSY.importMatrix(
                "  0.1027073     0.8598542     0.0000000E+00 0.0000000E+00-0.1064929     100000\n" +
                        " -0.7719973      3.273330     0.0000000E+00 0.0000000E+00 0.3404825     010000\n" +
                        "  0.0000000E+00 0.0000000E+00  7.045588    -0.3133395     0.0000000E+00 001000\n" +
                        "  0.0000000E+00 0.0000000E+00  5.088447    -0.8436644E-01 0.0000000E+00 000100\n" +
                        "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00  1.000000     000010\n" +
                        " -0.4724219E-01 0.6413517     0.0000000E+00 0.0000000E+00  2.282058     000001\n" +
                        "  -5.062997     -8.199716     0.0000000E+00 0.0000000E+00 -2.257327     200000\n" +
                        "  -55.52304     -18.84660     0.0000000E+00 0.0000000E+00 -6.707541     110000\n" +
                        "  -109.9309      35.27381     0.0000000E+00 0.0000000E+00 -8.903395     020000\n" +
                        "  0.0000000E+00 0.0000000E+00  322.0610     -28.23742     0.0000000E+00 101000\n" +
                        "  0.0000000E+00 0.0000000E+00  1218.509     -215.4256     0.0000000E+00 011000\n" +
                        "   96.17872      327.9441     0.0000000E+00 0.0000000E+00 -56.40707     002000\n" +
                        "  0.0000000E+00 0.0000000E+00  240.4671     -27.23141     0.0000000E+00 100100\n" +
                        "  0.0000000E+00 0.0000000E+00  888.2583     -180.4969     0.0000000E+00 010100\n" +
                        "   191.9755      472.7754     0.0000000E+00 0.0000000E+00 -83.62065     001100\n" +
                        "  -13.53475      1.419701     0.0000000E+00 0.0000000E+00-0.3130132     100001\n" +
                        "  -49.32543      7.740638     0.0000000E+00 0.0000000E+00 -3.430807     010001\n" +
                        "  0.0000000E+00 0.0000000E+00  224.3108     -15.87173     0.0000000E+00 001001\n" +
                        "   88.97256      169.0659     0.0000000E+00 0.0000000E+00 -31.67421     000200\n" +
                        "  0.0000000E+00 0.0000000E+00  155.6404     -15.69868     0.0000000E+00 000101\n" +
                        "  -5.227584    -0.3088457     0.0000000E+00 0.0000000E+00 -2.650967     000002"
        );

        Logger.getLogger().info("matrix = " + matrix);

        //3.34E-03	-2.22E-03	0.00E+00	0.00E+00	0.00E+00

        PhaseSpaceParticle p0 = PhaseSpaceParticles.create(1, 0, 0, 0, 0, 0);

        PhaseSpaceParticle p1 = COSY.transport(matrix, p0);

        Logger.getLogger().info("p0 = " + p0);

        Logger.getLogger().info("p1 = " + p1);

        p0 = PhaseSpaceParticles.create(0, 1, 0, 0, 0, 0);
        p1 = COSY.transport(matrix, p0);
        Logger.getLogger().info("p0 = " + p0);
        Logger.getLogger().info("p1 = " + p1);

        p0 = PhaseSpaceParticles.create(0, 0, 1, 0, 0, 0);
        p1 = COSY.transport(matrix, p0);
        Logger.getLogger().info("p0 = " + p0);
        Logger.getLogger().info("p1 = " + p1);

        p0 = PhaseSpaceParticles.create(0, 0, 0, 1, 0, 0);
        p1 = COSY.transport(matrix, p0);
        Logger.getLogger().info("p0 = " + p0);
        Logger.getLogger().info("p1 = " + p1);

        p0 = PhaseSpaceParticles.create(0, 0, 0, 0, 1, 0);
        p1 = COSY.transport(matrix, p0);
        Logger.getLogger().info("p0 = " + p0);
        Logger.getLogger().info("p1 = " + p1);

        p0 = PhaseSpaceParticles.create(0, 0, 0, 0, 0, 1);
        p1 = COSY.transport(matrix, p0);
        Logger.getLogger().info("p0 = " + p0);
        Logger.getLogger().info("p1 = " + p1);
    }

    @run(-1000)
    public void COSY计算测试2() {
        COSY.TransMatrix matrix = COSY.importMatrix(
                "  0.1787251    -0.3692074     0.0000000E+00 0.0000000E+00-0.1680138     100000\n" +
                        "   2.106134      1.244367     0.0000000E+00 0.0000000E+00-0.1994597     010000\n" +
                        "  0.0000000E+00 0.0000000E+00 -1.173974     -2.412454     0.0000000E+00 001000\n" +
                        "  0.0000000E+00 0.0000000E+00-0.6166184     -2.118926     0.0000000E+00 000100\n" +
                        "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00  1.000000     000010\n" +
                        "  0.3182113     0.2827129     0.0000000E+00 0.0000000E+00 0.4442537     000001\n" +
                        "  -5.884125     -4.590501     0.0000000E+00 0.0000000E+00-0.1537632     200000\n" +
                        "  -9.295337     -7.052685     0.0000000E+00 0.0000000E+00 0.2465018     110000\n" +
                        "  -3.292079     -2.395209     0.0000000E+00 0.0000000E+00-0.7059726     020000\n" +
                        "  0.0000000E+00 0.0000000E+00 -2.885618     -3.476974     0.0000000E+00 101000\n" +
                        "  0.0000000E+00 0.0000000E+00 -4.288296     -4.824977     0.0000000E+00 011000\n" +
                        "  -2.614055     -2.655721     0.0000000E+00 0.0000000E+00 -1.878795     002000\n" +
                        "  0.0000000E+00 0.0000000E+00 -3.984002     -4.804856     0.0000000E+00 100100\n" +
                        "  0.0000000E+00 0.0000000E+00 -5.684783     -6.476178     0.0000000E+00 010100\n" +
                        "  -7.270030     -7.197072     0.0000000E+00 0.0000000E+00 -3.530548     001100\n" +
                        "   1.477294     0.9379569     0.0000000E+00 0.0000000E+00 0.1128497     100001\n" +
                        " -0.2370026E-01 0.8164318     0.0000000E+00 0.0000000E+00-0.4416053E-01 010001\n" +
                        "  0.0000000E+00 0.0000000E+00  1.682843     0.4380811     0.0000000E+00 001001\n" +
                        "  -5.050226     -4.895456     0.0000000E+00 0.0000000E+00 -2.023583     000200\n" +
                        "  0.0000000E+00 0.0000000E+00  2.413792      2.152919     0.0000000E+00 000101\n" +
                        " -0.2721059    -0.8701740E-01 0.0000000E+00 0.0000000E+00-0.3957987     000002"
        );

        List<PhaseSpaceParticle> phaseSpaceParticles = PhaseSpaceParticles.phaseSpaceParticlesAlongPositiveEllipseInXXpPlane(3.5 * MM, 7.5 * MRAD, 0.0, 80);

        List<PhaseSpaceParticle> apply = matrix.apply(phaseSpaceParticles);

        List<Point2> list = PhaseSpaceParticles.projectionToXXpPlane(apply);

        list = Point2.convert(list, 1 / MM, 1 / MRAD);

        Plot2d.plot2(list, Plot2d.BLACK_POINT);

        List<Point2> list1 = excel_map_computer160();

        Plot2d.plot2(list1, Plot2d.RED_LINE);

        Plot2d.equal();

        Plot2d.showThread();
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
