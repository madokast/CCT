package cn.edu.hust.zrx.cct.study.超导机架AGCCT单独建模;

import cn.edu.hust.zrx.cct.Logger;
import cn.edu.hust.zrx.cct.base.BaseUtils;
import cn.edu.hust.zrx.cct.advanced.COSY;
import cn.edu.hust.zrx.cct.advanced.MathFunction;
import cn.edu.hust.zrx.cct.advanced.PolynomialFitter;
import cn.edu.hust.zrx.cct.base.cct.Cct;
import cn.edu.hust.zrx.cct.base.cct.CctFactory;
import cn.edu.hust.zrx.cct.base.line.Trajectory;
import cn.edu.hust.zrx.cct.base.line.TrajectoryFactory;
import cn.edu.hust.zrx.cct.base.particle.*;
import cn.edu.hust.zrx.cct.base.point.Point2;
import cn.edu.hust.zrx.cct.base.python.Plot2d;
import cn.edu.hust.zrx.cct.base.vector.Vector2;
import org.apache.commons.math3.fitting.PolynomialCurveFitter;
import org.apache.commons.math3.fitting.WeightedObservedPoints;

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
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Description
 * A0322色散函数计算225CCT
 * <p>
 * Data
 * 9:12
 *
 * @author zrx
 * @version 1.0
 */
@SuppressWarnings("all")
public class A0322色散函数计算225CCT {

    // -------------------------------------------------------------------------------------------
    // |                                                                                         |
    // |                                                                                         |
    // |       COSY MAP 中运动的粒子。注意                                                         |
    // |       由 PhaseSpaceParticles 工厂方法建立后，需要修改 delta                                |
    // |       原 delta 为 动量分散                                                               |
    // |       需要修改为 能量分散                                                                 |
    // |       调用 convertDeltaFromMomentumDispersionToEnergyDispersion 方法                     |
    // |                                                                                         |
    // |                                                                                         |
    // |        另外 如果需要研究色散 注意 COSY MAP 中的 r16 单位是                                  |
    // |        m / 100% 能量分散                                                                 |
    // |        最好改为通用的：                                                                   |
    // |        mm / 1% 动量分散                                                                  |
    // |        调用 COSY 的静态方法 convertR16InCosyToTransportPronTonOnly                        |
    // |                                                                                         |
    // |                                                                                         |
    // -------------------------------------------------------------------------------------------


    @run(value = 1, code = "a0322-1222")
    public void 色散函数对比() {
        List<Point2> track色散函数R16 = track色散函数R16(); // r16 m/1

        track色散函数R16 = Point2.convert(track色散函数R16, 1, 1 / MM * PRESENT);

        List<Point2> cosy色散函数 = cosy色散函数();

        // !!!!!!!
        // cosy色散函数 中的是动量分散，注意转为能量分散
        cosy色散函数 = Point2.convert(cosy色散函数, x -> x, r16Cosy -> COSY.convertR16InCosyToTransportPronTonOnly(r16Cosy, 250));


        Plot2d.plot2(cosy色散函数, Plot2d.RED_POINT);
        Plot2d.plot2(track色散函数R16, Plot2d.BLACK_LINE);
        Plot2d.plotYLines(keyDistance(), cosy色散函数, track色散函数R16);

        Plot2d.info("s/m", "R16(mm/%)", "", 18);
        Plot2d.legend(18, "cosy R16", "track R16");

        Plot2d.showThread();
    }

    @run(-100)
    public List<Point2> track色散函数R16() {
        Cct cct = getCct();
        Trajectory trajectory = getTrajectory();
        double length = trajectory.getLength() + 0.1;

        List<RunningParticle> particles = BaseUtils.Python.linspaceStream(-5 * PRESENT, 5 * PRESENT, 2)
                .mapToObj(delta -> {
                    double k = Protons.getKineticEnergy_MeV_AfterMomentumDispersion(250., delta);
                    RunningParticle ipk = ParticleFactory.createIdealProtonAtTrajectory(trajectory, k);
                    return ipk;
                })
                .collect(Collectors.toList());

        PolynomialFitter fitter = PolynomialFitter.build(1);

        List<Point2> distance_R16 = IntStream.range(0, (int) (length / MM) + 1)
                .mapToObj(i -> {
                    double distance = particles.get(0).getDistance();
                    RunningParticle ip = ParticleFactory.createIdealProtonAtTrajectory250MeV(trajectory, distance);
                    List<Point2> delta_x = particles.stream().map(p -> PhaseSpaceParticles.phaseSpaceParticleFromRunningParticle(ip, ip.computeNaturalCoordinateSystem(), p))
                            .map(pp -> Point2.create(pp.getDelta(), pp.getX()))
                            .sorted(Comparator.comparingDouble(Point2::getX))
                            .collect(Collectors.toList());
                    double[] fit = fitter.fit(delta_x);


                    particles.forEach(p -> p.runSelf(cct.magnetAt(p.position), MM));

                    return Point2.create(distance, fit[1]);
                })
                .collect(Collectors.toList());


//        Plot2d.plot2(distance_R16);
//        Plot2d.showThread();


        return distance_R16;
    }


    @run(-10000)
    public void track色散函数0() {
        Cct cct = getCct();
        Trajectory trajectory = getTrajectory();
        double length = trajectory.getLength();
        RunningParticle ip = ParticleFactory.createIdealProtonAtTrajectory250MeV(trajectory);
        ParticleRunner.run(ip, cct, length, MM);


        List<Point2> x_vs_dp = BaseUtils.Python.linspaceStream(-5 * PRESENT, 5 * PRESENT, 16)
                .parallel()
                .mapToObj(delta -> {
                    double k = Protons.getKineticEnergy_MeV_AfterMomentumDispersion(250., delta);
                    RunningParticle ipk = ParticleFactory.createIdealProtonAtTrajectory(trajectory, k);
                    return BaseUtils.Content.BiContent.create(delta, ipk);
                })
                .peek(bi -> ParticleRunner.run(bi.getT2(), cct, length, MM))
                .map(bi -> {
//                    Double delta = bi.getT1();
                    RunningParticle ipk = bi.getT2();
                    PhaseSpaceParticle phaseSpaceParticle =
                            PhaseSpaceParticles.phaseSpaceParticleFromRunningParticle(ip, ip.computeNaturalCoordinateSystem(), ipk);
                    Point2 point2 = PhaseSpaceParticles.projectionToXXpPlane(phaseSpaceParticle);

                    return Point2.create(phaseSpaceParticle.getDelta(), point2.x);
                })
                .map(p -> Point2.convert(p, 1 / PRESENT, 1 / MM))
                .collect(Collectors.toList());


        Plot2d.plot2(x_vs_dp, Plot2d.RED_POINT);

        Plot2d.showThread();

    }

    @run(-1000)
    public void fittingTest() {

        //测试数据
        double[] xs = {1, 3, 4, 5, 6, 7, 8, 9, 10};
        double[] ys = {10, 5, 4, 2, 1, 1, 2, 3, 4};
        WeightedObservedPoints points = new WeightedObservedPoints();
        for (int i = 0; i < xs.length; i++) {
            points.add(xs[i], ys[i]);
        }

        PolynomialCurveFitter fitter = PolynomialCurveFitter.create(2);
        final double[] results = fitter.fit(points.toList());

        Logger.getLogger().info("results = " + Arrays.toString(results));

        Plot2d.plot2(xs, ys, Plot2d.BLACK_POINT);

        Plot2d.plot2(MathFunction.createPolynomialFunction(results), Point2.create(0, 11), 1000, Plot2d.RED_LINE);


        List<Point2> list = new ArrayList<>();
        for (int i = 0; i < xs.length; i++) {
            list.add(Point2.create(xs[i], ys[i]));
        }

        double[] fit = PolynomialFitter.build(3).fit(list);
        Function<Double, Double> polynomialFunction = MathFunction.createPolynomialFunction(fit);
        Plot2d.plot2(polynomialFunction, Point2.create(0, 11), 1000, Plot2d.YELLOW_LINE);


        Plot2d.showThread();

        //results.length = 3
        //13.45966386554622
        //-3.6053093964858682
        //0.26757066462948825
        //从0次项开始

        //啊哈哈哈完美。和书本答案一致

    }


    @run(-10000)
    public void createPolynomialFunctionTest() {
        Function<Double, Double> polynomialFunction = MathFunction.createPolynomialFunction(new double[]{10, 0, -2});

        Function<Double, Double> function = x -> 10 - 2 * x * x;

        Plot2d.plot2(polynomialFunction, Point2.create(-10, 10), 1000, Plot2d.BLACK_LINE);
        Plot2d.plot2(function, Point2.create(-10, 10), 10, Plot2d.RED_POINT);

        Plot2d.showThread();
    }

    @run(-1000)
    public void protonReport() {
        Logger.getLogger().info("质子报表");

        Logger.getLogger().info("\n" + Protons.report(250.0));

        double momentum_kg_m_per_s = Protons.getMomentum_KG_M_PER_S(250);
        momentum_kg_m_per_s = momentum_kg_m_per_s * 1.05;
        double kineticEnergy_meV = Protons.getKineticEnergy_MeV(momentum_kg_m_per_s);

        Logger.getLogger().info("kineticEnergy_meV = " + kineticEnergy_meV);

        double newK = Protons.get动量分散后的动能(250, 5 * PRESENT);
        Logger.getLogger().info("newK = " + newK);
    }

    @run(-100)
    public void cosy色散函数画图() {
        List<Point2> cosy色散函数 = cosy色散函数();

        Plot2d.plot2(cosy色散函数, Plot2d.RED_POINT);

        Plot2d.info("s/m", "R16(m/%)", "dispersion function", 18);
        Plot2d.legend(18, "cosy");

        Plot2d.plotYLines(keyDistance(), cosy色散函数);

        Plot2d.showThread();
    }

    private List<Point2> cosy色散函数() {
        //        return List.of(
        //                0.,//start
        //                0.5,//entry
        //                0.5 + trajectoryBigR * BaseUtils.Converter.angleToRadian(agCctAngle0),//after ag cct 0
        //                0.5 + trajectoryBigR * BaseUtils.Converter.angleToRadian(dipoleCctAngle),//exit
        //                0.5 + trajectoryBigR * BaseUtils.Converter.angleToRadian(dipoleCctAngle) + 0.5//edn
        //        );

        return List.of(
                Point2.create(keyDistance().get(0), 0.0),
                Point2.create(keyDistance().get(1), entryMatrixFirst().getR(1, 6)),
                Point2.create(keyDistance().get(2), afterAgCct0MatrixFirst().getR(1, 6)),
                Point2.create(keyDistance().get(3), exitMatrixFirst().getR(1, 6)),
                Point2.create(keyDistance().get(4), endMatrixFirst().getR(1, 6)));
    }

    @run(-10000)
    public void convertR16InCosyToTransportPronTonOnlyTest() {
        double r16Cosy = 0.1768;

        double r16Trans = COSY.convertR16InCosyToTransportPronTonOnly(r16Cosy, 250);

        Logger.getLogger().info("r16Trans = " + r16Trans);
    }

    //---------------------------------------------------

    private List<Double> keyDistance() {
        return List.of(
                0.,//start
                0.5,//entry
                0.5 + trajectoryBigR * BaseUtils.Converter.angleToRadian(agCctAngle0),//after ag cct 0
                0.5 + trajectoryBigR * BaseUtils.Converter.angleToRadian(dipoleCctAngle),//exit
                0.5 + trajectoryBigR * BaseUtils.Converter.angleToRadian(dipoleCctAngle) + 0.5//edn
        );
    }

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
