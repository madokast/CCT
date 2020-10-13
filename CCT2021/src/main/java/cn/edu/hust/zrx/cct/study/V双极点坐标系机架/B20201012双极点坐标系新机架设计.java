package cn.edu.hust.zrx.cct.study.V双极点坐标系机架;

import cn.edu.hust.zrx.cct.Logger;
import cn.edu.hust.zrx.cct.advanced.MathFunction;
import cn.edu.hust.zrx.cct.base.BaseUtils;
import cn.edu.hust.zrx.cct.base.annotation.Run;
import cn.edu.hust.zrx.cct.base.point.Point2;
import cn.edu.hust.zrx.cct.base.point.Point2Function;
import cn.edu.hust.zrx.cct.base.python.Plot2d;
import cn.edu.hust.zrx.cct.base.vector.Vector2;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * Description
 * B20201012双极点坐标系新机架设计
 * <p>
 * Data
 * 10:14
 *
 * @author zrx
 * @version 1.0
 */

public class B20201012双极点坐标系新机架设计 {

    @Run
    public void 双极点坐标系() {
        // 再次推到一下圆心


        final double a = 1;
        final double eta = 0.1;

        final Point2 F1 = Point2.create(a, 0);
        final Point2 F2 = Point2.create(-a, 0);
        Vector2 xDirect = Vector2.xDirect();

        MathFunction xf = ksi -> a * Math.sinh(eta) / (
                Math.cosh(eta) - Math.cos(ksi)
        );


        MathFunction yf = ksi -> a * Math.sin(ksi) / (
                Math.cosh(eta) - Math.cos(ksi)
        );

        Point2Function p2f = Point2Function.create(xf, yf);

        Plot2d.plot2(p2f.disperse(0, Math.PI, 10000));

        Plot2d.showThread();

        p2f.disperse(0, Math.PI, 50)
                .forEach(p2 -> {
                    Vector2 F1P = Vector2.from(F1).to(p2);
                    Vector2 F2P = Vector2.from(F2).to(p2);

                    Logger.getLogger().info("eta = {}", Math.log(F1P.length() / F2P.length()));

                    double t1 = xDirect.angleToInRadian(F1P);
                    double t2 = xDirect.angleToInRadian(F2P);

                    Logger.getLogger().info("ksi = " + BaseUtils.Converter.radianToAngle(t1 - t2));
                });
    }

    @Run(1)
    public void 前偏转段二极CCT建模(){

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
