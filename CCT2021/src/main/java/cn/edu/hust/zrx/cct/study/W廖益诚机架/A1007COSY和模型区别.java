package cn.edu.hust.zrx.cct.study.W廖益诚机架;

import cn.edu.hust.zrx.cct.Logger;
import cn.edu.hust.zrx.cct.advanced.CctUtils;
import cn.edu.hust.zrx.cct.advanced.CosyArbitraryOrder;
import cn.edu.hust.zrx.cct.advanced.combined.GantryAnalysor;
import cn.edu.hust.zrx.cct.advanced.combined.GantryData;
import cn.edu.hust.zrx.cct.base.BaseUtils;
import cn.edu.hust.zrx.cct.base.annotation.Run;
import cn.edu.hust.zrx.cct.base.cct.Elements;
import cn.edu.hust.zrx.cct.base.cct.MagnetAble;
import cn.edu.hust.zrx.cct.base.line.Line2;
import cn.edu.hust.zrx.cct.base.line.Trajectory;
import cn.edu.hust.zrx.cct.base.point.Point2;
import cn.edu.hust.zrx.cct.base.python.Plot2d;
import cn.edu.hust.zrx.cct.base.vector.Vector2;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static cn.edu.hust.zrx.cct.base.BaseUtils.Constant.MM;
import static cn.edu.hust.zrx.cct.base.BaseUtils.Constant.PRESENT;

/**
 * Description
 * A1007COSY和模型区别
 * <p>
 * Data
 * 19:05
 *
 * @author zrx
 * @version 1.0
 */

public class A1007COSY和模型区别 {

    @Run
    public void gantry验证() {
        GantryData.FirstPart firstPart = GantryAnalysor.defaultFirstPart();
        Line2 trajectoryFirstPart = GantryAnalysor.getTrajectoryFirstPart(firstPart);
        MagnetAble magnetAble = GantryAnalysor.firstPartMagnetAble(firstPart);
        List<Point2> g0 = magnetAble.magnetGradientAlongTrajectory(trajectoryFirstPart, MM, 10 * MM);

        A0906廖益诚机架后偏转段建模 a0906 = new A0906廖益诚机架后偏转段建模();
        Elements elementsOfAllPart1 = a0906.getElementsOfAllPart1();
        Trajectory trajectoryPart1 = a0906.getTrajectoryPart1();
        List<Point2> g1 = elementsOfAllPart1.magnetGradientAlongTrajectory(trajectoryPart1, MM, 10 * MM);


        Plot2d.plot2(g0, Plot2d.BLUE_LINE);

        Plot2d.plot2(g1, Plot2d.BLACK_LINE);

        Plot2d.showThread();
    }

    @Run(2)
    public void gentry验证2() {
        GantryData.FirstPart firstPart = GantryAnalysor.defaultFirstPart();
        Line2 trajectoryFirstPart = GantryAnalysor.getTrajectoryFirstPart(firstPart);
        Vector2 directAtEnd = trajectoryFirstPart.directAtEnd();
        Point2 pointAtEnd = trajectoryFirstPart.pointAtEnd();

        GantryData.SecondPart secondPart = GantryAnalysor.defaultSecondPart();
        MagnetAble secondPartMagnetAble = GantryAnalysor.secondPartMagnetAble(pointAtEnd, directAtEnd, secondPart);
        Line2 trajectorySecondPart = GantryAnalysor.getTrajectorySecondPart(pointAtEnd, directAtEnd, secondPart);

        List<Point2> g0 = secondPartMagnetAble.magnetGradientAlongTrajectory(trajectorySecondPart, MM, 10 * MM);

        A0906廖益诚机架后偏转段建模 a0906 = new A0906廖益诚机架后偏转段建模();
        MagnetAble elementsOfAllPart1 = a0906.getElementsOfAllPart2();
        Line2 trajectoryPart1 = a0906.getTrajectoryPart2();
        List<Point2> g1 = elementsOfAllPart1.magnetGradientAlongTrajectory(trajectoryPart1, MM, 10 * MM);

        Plot2d.plot2(g0, Plot2d.BLUE_LINE);

        Plot2d.plot2(g1, Plot2d.BLACK_LINE);

        Plot2d.showThread();
    }

    @Run(3)
    public void 后偏转段段COSY_模型对比() {
        A0906廖益诚机架后偏转段建模 a0906 = new A0906廖益诚机架后偏转段建模();
        MagnetAble elementsOfAllPart2 = a0906.getElementsOfAllPart2();
        Line2 trajectoryPart2 = a0906.getTrajectoryPart2();

        CosyArbitraryOrder.CosyMapArbitraryOrder part2Map = A0906廖益诚机架后偏转段建模.COSY_MAP.part2Optics.map;

        BaseUtils.Switcher<String> sw = CctUtils.createPlotDescribeSwitcher();

        final double DP_MAX = 10 * PRESENT;
        final double DP_MIN = -10 * PRESENT;
        final boolean xPlane = false;

        List<BaseUtils.Content.BiContent<Double, List<Point2>>> biContentList = CctUtils.multiDpPhaseEllipses(
                trajectoryPart2, trajectoryPart2.getLength(),
                elementsOfAllPart2, DP_MIN, DP_MAX, 16, 16, xPlane
        );

        List<Point2> dp2x = BaseUtils.Content.BiContent.map(biContentList, x -> x / PRESENT, point2s ->
                point2s.stream().mapToDouble(Point2::getX).max().orElseThrow() -
                        point2s.stream().mapToDouble(Point2::getX).min().orElseThrow())
                .stream()
                .peek(bi -> bi.setT2If(x -> x >= 15., 15.))
                .map(BaseUtils.Content.BiContent::doubleDoubleBiContentToPoint2)
                .collect(Collectors.toList());


        List<BaseUtils.Content.BiContent<Double, List<Point2>>> biContentList1 =
                CctUtils.multiDpPhaseEllipses(part2Map, 5, false,
                        1, DP_MIN, DP_MAX, 16, 512, xPlane);

        List<Point2> dp2x2 = BaseUtils.Content.BiContent.map(biContentList1, x -> x / PRESENT, point2s ->
                point2s.stream().mapToDouble(Point2::getX).max().orElseThrow() -
                        point2s.stream().mapToDouble(Point2::getX).min().orElseThrow())
                .stream()
                .peek(bi -> bi.setT2If(x -> x >= 15., 15.))
                .map(BaseUtils.Content.BiContent::doubleDoubleBiContentToPoint2)
                .collect(Collectors.toList());


        Plot2d.plot2(dp2x, sw.getAndSwitch());
        Plot2d.plot2(dp2x2, sw.getAndSwitch());

        if (xPlane) {
            Plot2d.info("dp/%", "x-size/mm", "dp-x方向束斑", 18);
        } else {
            Plot2d.info("dp/%", "y-size/mm", "dp-y方向束斑", 18);
        }

        Plot2d.legend(18, "track", "cosy");

        Plot2d.showThread();
    }


    @Run(4)
    public void 四极磁铁对比() {
        GantryData.FirstPart firstPart = GantryAnalysor.defaultFirstPart();
        Line2 trajectoryFirstPart = GantryAnalysor.getTrajectoryFirstPart(firstPart);
        Vector2 directAtEnd = trajectoryFirstPart.directAtEnd();
        Point2 pointAtEnd = trajectoryFirstPart.pointAtEnd();

        GantryData.SecondPart secondPart = GantryAnalysor.defaultSecondPart();
        secondPart.QS3_GRADIENT  = 0;
        MagnetAble secondPartMagnetAble = GantryAnalysor.secondPartMagnetAble(pointAtEnd, directAtEnd, secondPart);
        Line2 trajectorySecondPart = GantryAnalysor.getTrajectorySecondPart(pointAtEnd, directAtEnd, secondPart);

        List<Point2> g = secondPartMagnetAble.magnetGradientAlongTrajectoryFast(trajectorySecondPart.resetLength(secondPart.DL2 + secondPart.CCT345_LENGTH + secondPart.CCT345_LENGTH / 3)
                , 10 * MM, 10 * MM);

        g = Point2.convert(g, x -> BaseUtils.Converter.radianToAngle((x - secondPart.DL2) / 0.95), y -> y)
                .stream().filter(p->p.x>-20).collect(Collectors.toList());

        Plot2d.plot2(g, Plot2d.RED_LINE);

        Plot2d.plot2(List.of(
                Point2.create(-20, 0),
                Point2.create(0, 0),
                Point2.create(0, 17.466),
                Point2.create(8 + 4.386, 17.466),
                Point2.create(8 + 4.386, -17.466),
                Point2.create(8 + 4.386 + 8 + 19.344, -17.466),
                Point2.create(8 + 4.386 + 8 + 19.344, 17.466),
                Point2.create(8 + 4.386 + 8 + 19.344 + 8 + 19.770, 17.466),
                Point2.create(8 + 4.386 + 8 + 19.344 + 8 + 19.770, 0),
                Point2.create(67.5 + 20, 0)
        ), Plot2d.BLUE_DASH);

        Plot2d.info("s/deg","gradient/Tm-1","四极场",18);
        Plot2d.legend(18,"cct model","cut off");

        Plot2d.showThread();
    }

    @Run(5)
    public void 一层二极CCT(){
        GantryData.FirstPart firstPart = GantryAnalysor.defaultFirstPart();
        Line2 trajectoryFirstPart = GantryAnalysor.getTrajectoryFirstPart(firstPart);
        Vector2 directAtEnd = trajectoryFirstPart.directAtEnd();
        Point2 pointAtEnd = trajectoryFirstPart.pointAtEnd();

        GantryData.SecondPart secondPart = GantryAnalysor.defaultSecondPart();
        secondPart.QS3_GRADIENT  = 0;
        secondPart.agCct345IInner = 0;
        secondPart.agCct345IOuter = 0;
        secondPart.dipoleCct345IOuter = 0;
        MagnetAble secondPartMagnetAble = GantryAnalysor.secondPartMagnetAble(pointAtEnd, directAtEnd, secondPart);
        Line2 trajectorySecondPart = GantryAnalysor.getTrajectorySecondPart(pointAtEnd, directAtEnd, secondPart);

        List<Point2> g = secondPartMagnetAble.magnetBzAlongTrajectory(trajectorySecondPart.resetLength(secondPart.DL2 +
                secondPart.CCT345_LENGTH + secondPart.CCT345_LENGTH / 3));

        g = Point2.convert(g, x -> BaseUtils.Converter.radianToAngle((x - secondPart.DL2) / 0.95), y -> y)
                .stream().filter(p->p.x>-20).collect(Collectors.toList());

        Plot2d.plot2(g);



        Plot2d.showThread();
    }

    @Run(6)
    public void dp_x_size(){
        GantryData.FirstPart firstPart = GantryAnalysor.defaultFirstPart();
        Line2 trajectoryFirstPart = GantryAnalysor.getTrajectoryFirstPart(firstPart);
        Vector2 directAtEnd = trajectoryFirstPart.directAtEnd();
        Point2 pointAtEnd = trajectoryFirstPart.pointAtEnd();

        GantryData.SecondPart secondPart = GantryAnalysor.defaultSecondPart();
        MagnetAble secondPartMagnetAble = GantryAnalysor.secondPartMagnetAble(pointAtEnd, directAtEnd, secondPart);
        Line2 trajectorySecondPart = GantryAnalysor.getTrajectorySecondPart(pointAtEnd, directAtEnd, secondPart);


        List<Point2> x = CctUtils.trackingPhaseEllipse(trajectorySecondPart.getLength(), true, 0, 16,
                false, 1, secondPartMagnetAble, trajectorySecondPart);

        List<Point2> y = CctUtils.trackingPhaseEllipse(trajectorySecondPart.getLength(), false, 0, 16,
                false, 1, secondPartMagnetAble, trajectorySecondPart);

        Plot2d.plot2(x,Plot2d.BLACK_LINE);
        Plot2d.plot2(y,Plot2d.RED_LINE);

        Point2 point2 = CctUtils.beamSizeAtDp(trajectorySecondPart.getLength(), 0, 16, secondPartMagnetAble, trajectorySecondPart);
        System.out.println("point2 = " + point2);

        Plot2d.showThread();
    }

    @Run(7)
    public void dp_x_size1(){
        GantryData.FirstPart firstPart = GantryAnalysor.defaultFirstPart();
        Line2 trajectoryFirstPart = GantryAnalysor.getTrajectoryFirstPart(firstPart);
        Vector2 directAtEnd = trajectoryFirstPart.directAtEnd();
        Point2 pointAtEnd = trajectoryFirstPart.pointAtEnd();

        GantryData.SecondPart secondPart = GantryAnalysor.defaultSecondPart();
        MagnetAble secondPartMagnetAble = GantryAnalysor.secondPartMagnetAble(pointAtEnd, directAtEnd, secondPart);
        Line2 trajectorySecondPart = GantryAnalysor.getTrajectorySecondPart(pointAtEnd, directAtEnd, secondPart);


        List<Point2> x = CctUtils.trackingPhaseEllipse(trajectorySecondPart.getLength(), true, 5*PRESENT, 14,
                false, 1, secondPartMagnetAble, trajectorySecondPart);

        List<Point2> y = CctUtils.trackingPhaseEllipse(trajectorySecondPart.getLength(), false, 5*PRESENT, 14,
                false, 1, secondPartMagnetAble, trajectorySecondPart);

        Plot2d.plot2circle(x,Plot2d.BLACK_LINE);
        Plot2d.plot2circle(y,Plot2d.RED_LINE);

        Point2 point2 = CctUtils.beamSizeAtDp(trajectorySecondPart.getLength(), 5*PRESENT, 14, secondPartMagnetAble, trajectorySecondPart);
        System.out.println("point2 = " + point2);

        Plot2d.showThread();
    }

    public static void main(String[] args) throws Exception {
        BaseUtils.Timer.printPeriodPerSecondCall(Logger.getLogger());
        StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[1];
        Logger.getLogger().info("{}", stackTraceElement.getClassName());
        Class<?> currentClass = Class.forName(stackTraceElement.getClassName());

        Constructor<?> constructor = currentClass.getConstructor();
        Object newInstance = constructor.newInstance();

        Class<Run> runAnnotation = Run.class;

        Method[] methods = currentClass.getMethods();
        Arrays.stream(methods)
                .filter(method -> method.isAnnotationPresent(runAnnotation))
                .filter(method -> method.getAnnotation(runAnnotation).validate())
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
}
