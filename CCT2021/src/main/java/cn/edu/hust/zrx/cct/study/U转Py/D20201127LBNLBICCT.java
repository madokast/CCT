package cn.edu.hust.zrx.cct.study.U转Py;

import cn.edu.hust.zrx.cct.Logger;
import cn.edu.hust.zrx.cct.advanced.CctUtils;
import cn.edu.hust.zrx.cct.advanced.MathFunction;
import cn.edu.hust.zrx.cct.base.BaseUtils;
import cn.edu.hust.zrx.cct.base.annotation.Run;
import cn.edu.hust.zrx.cct.base.cct.Cct;
import cn.edu.hust.zrx.cct.base.cct.SoleLayerCct;
import cn.edu.hust.zrx.cct.base.line.Trajectory;
import cn.edu.hust.zrx.cct.base.line.TrajectoryFactory;
import cn.edu.hust.zrx.cct.base.magnet.Elements;
import cn.edu.hust.zrx.cct.base.point.*;
import cn.edu.hust.zrx.cct.base.python.Plot2d;
import cn.edu.hust.zrx.cct.base.python.Plot3d;
import cn.edu.hust.zrx.cct.base.vector.Vector2;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static cn.edu.hust.zrx.cct.base.BaseUtils.Constant.MM;

/**
 * Description
 * D20201127LBNLBICCT
 * <p>
 * Data
 * 9:38
 *
 * @author zrx
 * @version 1.0
 */

public class D20201127LBNLBICCT {

    @Run
    public void lbnl_cct() {
        int wingdingNumber = 78;
        double tiltRadian = BaseUtils.Converter.angleToRadian(15.);
        double z0 = 7.604 * MM;
        double smallR1 = 30 * MM;
        double smallR2 = 36.59 * MM;
        double current = 4050;

        MathFunction thetaFun = MathFunction.identity();
        MathFunction zFun = t -> (z0 / (2 * Math.PI)) * t + (smallR1 / Math.tan(tiltRadian)) * Math.sin(t);
        MathFunction zFun_2 = t -> (z0 / (2 * Math.PI)) * t + (smallR2 / Math.tan(tiltRadian)) * Math.sin(t);


        Point2Function p2fun = Point2Function.create(thetaFun, zFun);
        Point2Function p2fun_2 = Point2Function.create(thetaFun, zFun_2);
        p2fun_2 = p2fun_2.yAxisSymmetry();

        Point2To3 p23 = p2 -> Point3.create(smallR1 * Math.cos(p2.x), p2.y, smallR1 * Math.sin(p2.x));
        Point2To3 p23_2 = p2 -> Point3.create(smallR2 * Math.cos(p2.x), p2.y, smallR2 * Math.sin(p2.x));

        Point3Function p3fun = p23.convert(p2fun);
        Point3Function p3fun_2 = p23_2.convert(p2fun_2);

        SoleLayerCct cct1 = SoleLayerCct.create(p3fun.disperse(0, wingdingNumber*Math.PI * 2, wingdingNumber * 360), current);
        SoleLayerCct cct2 = SoleLayerCct.create(p3fun_2.disperse(0, wingdingNumber*Math.PI * 2, wingdingNumber * 360), current);

        Elements e = Elements.of(cct1, cct2);

        Trajectory t = TrajectoryFactory.setStartingPoint(0, -1).setStraightLine(3, Vector2.yDirect());

        BaseUtils.Switcher<String> sw = CctUtils.createPlotDescribeSwitcher();
        List<List<Point2>> ll = e.multiplePoleMagnetAlongTrajectoryBreak(t, MM, 10 * MM, sw.getSize() - 1, 64);

        ll.stream().limit(6)
                .peek(l-> System.out.println(l.get(1296)))
                .forEach(l-> Plot2d.plot2(l,sw.getAndSwitch()));

        List<String> des = BaseUtils.StreamTools.from(0).toExcluding(sw.getSize()).map(i -> i * 2).mapToObj(i -> i + " pole").collect(Collectors.toList());

        Plot2d.info("s/m","val","t",18);

        Plot2d.legend(18,des);

        Plot2d.showThread();

    }

    @Run(12)
    public void t(){
        int wingdingNumber = 78;
        double tiltRadian = BaseUtils.Converter.angleToRadian(15.);
        double z0 = 7.604 * MM;
        double smallR1 = 30 * MM;
        double smallR2 = 36.59 * MM;
        double current = -4050;

        MathFunction thetaFun = MathFunction.identity();
        MathFunction zFun = t -> (z0 / (2 * Math.PI)) * t + (smallR1 / Math.tan(tiltRadian)) * Math.sin(t);
        MathFunction zFun_2 = t -> (z0 / (2 * Math.PI)) * t + (smallR2 / Math.tan(tiltRadian)) * Math.sin(t);


        Point2Function p2fun = Point2Function.create(thetaFun, zFun);
        Point2Function p2fun_2 = Point2Function.create(thetaFun, zFun_2);
        p2fun_2 = p2fun_2.yAxisSymmetry();

        Point2To3 p23 = p2 -> Point3.create(smallR1 * Math.cos(p2.x), p2.y, smallR1 * Math.sin(p2.x));
        Point2To3 p23_2 = p2 -> Point3.create(smallR2 * Math.cos(p2.x), p2.y, smallR2 * Math.sin(p2.x));

        Point3Function p3fun = p23.convert(p2fun);
        Point3Function p3fun_2 = p23_2.convert(p2fun_2);

        SoleLayerCct cct1 = SoleLayerCct.create(p3fun.disperse(0, wingdingNumber*Math.PI * 2, wingdingNumber * 360), current);
        SoleLayerCct cct2 = SoleLayerCct.create(p3fun_2.disperse(0, wingdingNumber*Math.PI * 2, wingdingNumber * 360), current);

        Elements e = Elements.of(cct1, cct2);

        Trajectory t = TrajectoryFactory.setStartingPoint(0, -1).setStraightLine(3, Vector2.yDirect());

        Point2 p0 = t.pointAt(1.296);
        Vector2 d = t.directAt(1.296).rotateSelf(Math.PI / 2);

        Point2 left = p0.copy().moveSelf(d.normal().changeLengthSelf(5 * MM));
        Point2 right = p0.copy().moveSelf(d.normal().changeLengthSelf(-5 * MM));

        Trajectory tt = TrajectoryFactory.setStartingPoint(right).setStraightLine(10 * MM, d);

        List<Point2> bz = e.magnetBzAlongTrajectoryParallel(tt);

        Plot2d.plot2(bz);

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

        Method[] methods = currentClass.getDeclaredMethods();
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
