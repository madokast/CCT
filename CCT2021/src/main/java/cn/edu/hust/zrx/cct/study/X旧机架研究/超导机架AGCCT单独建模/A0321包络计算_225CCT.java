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
import cn.edu.hust.zrx.cct.base.python.Plot2d;
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
 * A0321包络计算_225CCT
 * <p>
 * Data
 * 20:54
 *
 * @author zrx
 * @version 1.0
 */
@SuppressWarnings("all")
public class A0321包络计算_225CCT {

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
    // |                                                                                         |
    // -------------------------------------------------------------------------------------------

    @run(1)
    public void cosy包络绘制() {
        List<Point2> xe1 = cosy包络0(true, 1, 0.0).get(0);
        List<Point2> xe2 = cosy包络0(true, 2, 0.0).get(0);

        List<Point2> ye1 = cosy包络0(false, 1, 0.0).get(0);
        List<Point2> ye2 = cosy包络0(false, 2, 0.0).get(0);

        xe1 = Point2.convert(xe1, 1, -1);
        xe2 = Point2.convert(xe2, 1, -1);

        Plot2d.plot2(ye2, Plot2d.YELLOW_POINT);
        Plot2d.plot2(ye1, Plot2d.RED_POINT);
        Plot2d.plot2(xe1, Plot2d.BLUE_POINT);
        Plot2d.plot2(xe2, Plot2d.GREEN_POINT);

        Plot2d.showThread();
    }

    @run(-2) // 测不出名堂来
    public void 粒子跟踪包络测试() {
        List<Point2> xe = track粒子跟踪包络(true, 0.0);
        List<Point2> ye = track粒子跟踪包络(false, 0.0);

        xe = Point2.convert(xe, 1, -1);

        Plot2d.plot2(xe, Plot2d.BLACK_LINE);
        Plot2d.plot2(ye, Plot2d.BLACK_LINE);

        Plot2d.showThread();
    }

    @run(value = 3, code = "a0321-2254")
    public void 包络画图对比() {
        double delta = +0 * PRESENT;

        List<Point2> xe2 = cosy包络0(true, 2, delta).get(0);
        List<Point2> ye2 = cosy包络0(false, 2, delta).get(0);
        xe2 = Point2.convert(xe2, 1, -1 / MM);
        ye2 = Point2.convert(ye2, 1, 1 / MM);

        Plot2d.plot2(xe2, Plot2d.RED_POINT);
        Plot2d.plot2(ye2, Plot2d.BLUE_POINT);

        List<Point2> ye = track粒子跟踪包络(false, delta);
        List<Point2> xe = track粒子跟踪包络(true, delta);
        xe = Point2.convert(xe, 1, -1 / MM);
        ye = Point2.convert(ye, 1, 1 / MM);

        Plot2d.plot2(xe, Plot2d.RED_LINE);
        Plot2d.plot2(ye, Plot2d.BLUE_LINE);


        Plot2d.legend(18, "cosy-x", "cosy-y", "track-x", "track-y");


        Plot2d.plotXLine(0, getTrajectory().getLength(), 0, Plot2d.BLACK_LINE);
        Plot2d.plotYLines(keyDistance(), xe, xe2, ye, ye2);

        Plot2d.info("s/m", "envelope/mm", "", 18);


        Plot2d.showThread();
    }


    @run(value = 4, code = "")
    public void 包络画图对比_包含动量分散() {
        {
            double delta = 0;

            List<Point2> xsCosy = cosy包络(true, 2, delta);
            List<Point2> ysCosy = cosy包络(false, 2, delta);
            xsCosy = Point2.convert(xsCosy, 1, -1 / MM);
            ysCosy = Point2.convert(ysCosy, 1, 1 / MM);

            Plot2d.plot2(xsCosy, Plot2d.BLACK_POINT);
            Plot2d.plot2(ysCosy, Plot2d.BLACK_UP_TRI);

            // dp = 0
            List<Point2> xeTrack = track粒子跟踪包络(true, delta);
            List<Point2> yeTrack = track粒子跟踪包络(false, delta);
            xeTrack = Point2.convert(xeTrack, 1, -1 / MM);
            yeTrack = Point2.convert(yeTrack, 1, 1 / MM);

            Plot2d.plot2(xeTrack, Plot2d.BLACK_LINE);
            Plot2d.plot2(yeTrack, Plot2d.BLACK_DASH);

        }

        {
            double delta = 5 * PRESENT;

            List<Point2> xsCosy = cosy包络(true, 2, delta);
            List<Point2> ysCosy = cosy包络(false, 2, delta);
            xsCosy = Point2.convert(xsCosy, 1, -1 / MM);
            ysCosy = Point2.convert(ysCosy, 1, 1 / MM);

            Plot2d.plot2(xsCosy, Plot2d.RED_POINT);
            Plot2d.plot2(ysCosy, Plot2d.RED_UP_TRI);

            // dp = 0
            List<Point2> xeTrack = track粒子跟踪包络(true, delta);
            List<Point2> yeTrack = track粒子跟踪包络(false, delta);
            xeTrack = Point2.convert(xeTrack, 1, -1 / MM);
            yeTrack = Point2.convert(yeTrack, 1, 1 / MM);

            Plot2d.plot2(xeTrack, Plot2d.RED_LINE);
            Plot2d.plot2(yeTrack, Plot2d.RED_DASH);
        }


        Plot2d.legend(12, "cosy-x dp0", "cosy-y dp0", "track-x dp0", "track-y dp0",
                "cosy-x dp5%", "cosy-y dp5%", "track-x dp5%", "track-y dp5%");


        Plot2d.plotXLine(0, getTrajectory().getLength(), 0, Plot2d.BLACK_LINE);
        Plot2d.plotYLines(keyDistance(), -30, 30, Plot2d.GREY_DASH);

        Plot2d.info("s/m", "envelope/mm", "", 18);


        Plot2d.showThread();
    }


    private List<Point2> track粒子跟踪包络0(boolean xxPlane, double delta) {
        Cct cct = getCct();
        Trajectory trajectory = getTrajectory();

        RunningParticle ip = ParticleFactory.createIdealProtonAtTrajectory250MeV(trajectory);

        List<PhaseSpaceParticle> pp =
                PhaseSpaceParticles.phaseSpaceParticlesAlongPositiveEllipseInPlane(xxPlane, 3.5 * MM, 7.5 * MRAD, delta, 64);

        List<List<BaseUtils.Content.BiContent<Double, RunningParticle>>> runs = ParticleFactory.createParticlesFromPhaseSpaceParticle(ip, ip.computeNaturalCoordinateSystem(), pp)
                .stream()
                .parallel()
                .map(p -> ParticleRunner.runGetAllInfo(p, cct, trajectory.getLength(), MM))
                .collect(Collectors.toList());

        List<List<BaseUtils.Content.BiContent<Double, RunningParticle>>> states = BaseUtils.ListUtils.listList2ListList(runs);

        return states.stream()
                .parallel()
                .map(bis -> {
                    Double distance = bis.get(0).getT1();
                    RunningParticle ipp = ParticleFactory.createIdealProtonAtTrajectory250MeV(trajectory, distance);
                    double max = bis.stream()
                            .map(BaseUtils.Content.BiContent::getT2)
                            .map(rp -> PhaseSpaceParticles.phaseSpaceParticleFromRunningParticle(ipp, ipp.computeNaturalCoordinateSystem(), rp))
                            .map(ppp -> PhaseSpaceParticles.projectionToPlane(xxPlane, ppp))
                            .mapToDouble(Point2::getX)
                            .map(Math::abs)
                            .max()
                            .orElse(10000);

                    return Point2.create(distance, max);
                })
                .collect(Collectors.toList())
                .stream()
                .sorted(Comparator.comparingDouble(Point2::getX))
                .collect(Collectors.toList());
    }


    private List<Point2> track粒子跟踪包络(boolean xxPlane, double delta) {
        if (BaseUtils.Equal.isEqual(delta, 0.0))
            return track粒子跟踪包络0(xxPlane, delta);

        List<Point2> point2s = track粒子跟踪包络0(xxPlane, delta);
        List<Point2> point2s1 = track粒子跟踪包络0(xxPlane, -delta);

        List<Point2> ret = new ArrayList<>();
        for (int i = 0; i < point2s.size(); i++) {
            Point2 point2 = point2s.get(i);
            Point2 point21 = point2s1.get(i);

            ret.add(Point2.create(point2.x, Math.max(point2.y, point21.y)));
        }

        return ret;
    }

    @run(-10000)
    public void listList2ListListTest() {
        List<List<Integer>> listList = new ArrayList<>();
        listList.add(List.of(1, 2, 3, 4));
        listList.add(List.of(5, 6, 7, 8));
        listList.add(List.of(9, 0, 1, 2));

        for (List<Integer> integers : listList) {
            Logger.getLogger().info("integers = " + integers);
        }

        List<List<Integer>> listList1 = BaseUtils.ListUtils.listList2ListList(listList);

        for (List<Integer> integers : listList1) {
            Logger.getLogger().info("integers = " + integers);
        }
    }

    @run(-1000)
    public void dotDash() {
        Plot2d.plot2(Math::sin, Point2.create(0, 10), 100, Plot2d.BLACK_DOt_DASH);

        Plot2d.showThread();


    }


    private List<List<Point2>> cosy包络0(boolean xxPlane, int order, double delta) {

        List<PhaseSpaceParticle> ppStart;


        // START
        ppStart = phaseSpaceParticlesStart(xxPlane, delta, 64);
        ppStart = PhaseSpaceParticles.convertDeltaFromMomentumDispersionToEnergyDispersion(ppStart,250.0);
        double xStart = PhaseSpaceParticles.projectionToPlane(xxPlane, ppStart).stream().mapToDouble(Point2::getX).map(Math::abs).max().orElse(10000);
        double xpStart = PhaseSpaceParticles.projectionToPlane(xxPlane, ppStart).stream().mapToDouble(Point2::getY).map(Math::abs).max().orElse(10000);


        //ENTRY FIRST
        ppStart = phaseSpaceParticlesStart(xxPlane, delta, 64);
        ppStart = PhaseSpaceParticles.convertDeltaFromMomentumDispersionToEnergyDispersion(ppStart,250.0);
        ppStart = entryMatrixFirst().apply(ppStart);
        double xEntry1 = PhaseSpaceParticles.projectionToPlane(xxPlane, ppStart).stream().mapToDouble(Point2::getX).map(Math::abs).max().orElse(10000);
        double xpEntry1 = PhaseSpaceParticles.projectionToPlane(xxPlane, ppStart).stream().mapToDouble(Point2::getY).map(Math::abs).max().orElse(10000);

        //ENTRY Second
        ppStart = phaseSpaceParticlesStart(xxPlane, delta, 64);
        ppStart = PhaseSpaceParticles.convertDeltaFromMomentumDispersionToEnergyDispersion(ppStart,250.0);
        ppStart = entryMatrixSecond().apply(ppStart);
        double xEntry2 = PhaseSpaceParticles.projectionToPlane(xxPlane, ppStart).stream().mapToDouble(Point2::getX).map(Math::abs).max().orElse(10000);
        double xpEntry2 = PhaseSpaceParticles.projectionToPlane(xxPlane, ppStart).stream().mapToDouble(Point2::getY).map(Math::abs).max().orElse(10000);

        // AFTER 1
        ppStart = phaseSpaceParticlesStart(xxPlane, delta, 64);
        ppStart = PhaseSpaceParticles.convertDeltaFromMomentumDispersionToEnergyDispersion(ppStart,250.0);
        ppStart = afterAgCct0MatrixFirst().apply(ppStart);
        double xAfter1 = PhaseSpaceParticles.projectionToPlane(xxPlane, ppStart).stream().mapToDouble(Point2::getX).map(Math::abs).max().orElse(10000);
        double xpAfter1 = PhaseSpaceParticles.projectionToPlane(xxPlane, ppStart).stream().mapToDouble(Point2::getY).map(Math::abs).max().orElse(10000);

        // AFTER 2
        ppStart = phaseSpaceParticlesStart(xxPlane, delta, 64);
        ppStart = PhaseSpaceParticles.convertDeltaFromMomentumDispersionToEnergyDispersion(ppStart,250.0);
        ppStart = afterAgCct0MatrixSecond().apply(ppStart);
        double xAfter2 = PhaseSpaceParticles.projectionToPlane(xxPlane, ppStart).stream().mapToDouble(Point2::getX).map(Math::abs).max().orElse(10000);
        double xpAfter2 = PhaseSpaceParticles.projectionToPlane(xxPlane, ppStart).stream().mapToDouble(Point2::getY).map(Math::abs).max().orElse(10000);

        // exit 1
        ppStart = phaseSpaceParticlesStart(xxPlane, delta, 64);
        ppStart = PhaseSpaceParticles.convertDeltaFromMomentumDispersionToEnergyDispersion(ppStart,250.0);
        ppStart = exitMatrixFirst().apply(ppStart);
        double xExit1 = PhaseSpaceParticles.projectionToPlane(xxPlane, ppStart).stream().mapToDouble(Point2::getX).map(Math::abs).max().orElse(10000);
        double xpExit1 = PhaseSpaceParticles.projectionToPlane(xxPlane, ppStart).stream().mapToDouble(Point2::getY).map(Math::abs).max().orElse(10000);

        // exit 2
        ppStart = phaseSpaceParticlesStart(xxPlane, delta, 64);
        ppStart = PhaseSpaceParticles.convertDeltaFromMomentumDispersionToEnergyDispersion(ppStart,250.0);
        ppStart = exitMatrixSecond().apply(ppStart);
        double xExit2 = PhaseSpaceParticles.projectionToPlane(xxPlane, ppStart).stream().mapToDouble(Point2::getX).map(Math::abs).max().orElse(10000);
        double xpExit2 = PhaseSpaceParticles.projectionToPlane(xxPlane, ppStart).stream().mapToDouble(Point2::getY).map(Math::abs).max().orElse(10000);

        // end 1
        ppStart = phaseSpaceParticlesStart(xxPlane, delta, 64);
        ppStart = PhaseSpaceParticles.convertDeltaFromMomentumDispersionToEnergyDispersion(ppStart,250.0);
        ppStart = endMatrixFirst().apply(ppStart);
        double xEnd1 = PhaseSpaceParticles.projectionToPlane(xxPlane, ppStart).stream().mapToDouble(Point2::getX).map(Math::abs).max().orElse(10000);
        double xpEnd1 = PhaseSpaceParticles.projectionToPlane(xxPlane, ppStart).stream().mapToDouble(Point2::getY).map(Math::abs).max().orElse(10000);

        // end 2
        ppStart = phaseSpaceParticlesStart(xxPlane, delta, 64);
        ppStart = PhaseSpaceParticles.convertDeltaFromMomentumDispersionToEnergyDispersion(ppStart,250.0);
        ppStart = endMatrixSecond().apply(ppStart);
        double xEnd2 = PhaseSpaceParticles.projectionToPlane(xxPlane, ppStart).stream().mapToDouble(Point2::getX).map(Math::abs).max().orElse(10000);
        double xpEnd2 = PhaseSpaceParticles.projectionToPlane(xxPlane, ppStart).stream().mapToDouble(Point2::getY).map(Math::abs).max().orElse(10000);


        List<List<Point2>> pll = new ArrayList<>(2);

        if (order == 1) {
            //                0.,//start
            //                0.5,//entry
            //                0.5 + trajectoryBigR * BaseUtils.Converter.angleToRadian(agCctAngle0),//after ag cct 0
            //                0.5 + trajectoryBigR * BaseUtils.Converter.angleToRadian(dipoleCctAngle),//exit
            //                0.5 + trajectoryBigR * BaseUtils.Converter.angleToRadian(dipoleCctAngle) + 0.5//edn

            pll.add(List.of(
                    Point2.create(keyDistance().get(0), xStart),
                    Point2.create(keyDistance().get(1), xEntry1),
                    Point2.create(keyDistance().get(2), xAfter1),
                    Point2.create(keyDistance().get(3), xExit1),
                    Point2.create(keyDistance().get(4), xEnd1)
            ));

            pll.add(List.of(
                    Point2.create(keyDistance().get(0), xpStart),
                    Point2.create(keyDistance().get(1), xpEntry1),
                    Point2.create(keyDistance().get(2), xpAfter1),
                    Point2.create(keyDistance().get(3), xpExit1),
                    Point2.create(keyDistance().get(4), xpEnd1)
            ));

        } else if (order == 2) {
            pll.add(List.of(
                    Point2.create(keyDistance().get(0), xStart),
                    Point2.create(keyDistance().get(1), xEntry2),
                    Point2.create(keyDistance().get(2), xAfter2),
                    Point2.create(keyDistance().get(3), xExit2),
                    Point2.create(keyDistance().get(4), xEnd2)
            ));

            pll.add(List.of(
                    Point2.create(keyDistance().get(0), xpStart),
                    Point2.create(keyDistance().get(1), xpEntry2),
                    Point2.create(keyDistance().get(2), xpAfter2),
                    Point2.create(keyDistance().get(3), xpExit2),
                    Point2.create(keyDistance().get(4), xpEnd2)
            ));
        } else {
            throw new RuntimeException("错误");
        }

        return pll;
    }

    private List<Point2> cosy包络(boolean xxPlane, int order, double delta) {
        if (BaseUtils.Equal.isEqual(delta, 0.0))
            return cosy包络0(xxPlane, order, delta).get(0);

        List<List<Point2>> lists = cosy包络0(xxPlane, order, delta);
        List<List<Point2>> lists1 = cosy包络0(xxPlane, order, -delta);

        List<Point2> point2s = lists.get(0);
        List<Point2> point2s1 = lists1.get(0);

        List<Point2> ret = new ArrayList<>();
        for (int i = 0; i < point2s.size(); i++) {
            Point2 point2 = point2s.get(i);
            Point2 point21 = point2s1.get(i);

            ret.add(Point2.create(point2.x, Math.max(point2.y, point21.y)));
        }

        return ret;
    }

    private List<Double> keyDistance() {
        return List.of(
                0.,//start
                0.5,//entry
                0.5 + trajectoryBigR * BaseUtils.Converter.angleToRadian(agCctAngle0),//after ag cct 0
                0.5 + trajectoryBigR * BaseUtils.Converter.angleToRadian(dipoleCctAngle),//exit
                0.5 + trajectoryBigR * BaseUtils.Converter.angleToRadian(dipoleCctAngle) + 0.5//edn
        );
    }

    private List<PhaseSpaceParticle> phaseSpaceParticlesStart(boolean xxPlane, double delta, int num) {
        return xxPlane ? PhaseSpaceParticles.phaseSpaceParticlesAlongPositiveEllipseInXXpPlane(
                3.5 * MM, 7.5 * MRAD, delta, num) :
                PhaseSpaceParticles.phaseSpaceParticlesAlongPositiveEllipseInYYpPlane(
                        3.5 * MM, 7.5 * MRAD, delta, num);
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
