package cn.edu.hust.zrx.cct.study;

import cn.edu.hust.zrx.cct.Logger;
import cn.edu.hust.zrx.cct.base.BaseUtils;
import cn.edu.hust.zrx.cct.base.annotation.Run;
import cn.edu.hust.zrx.cct.base.cct.*;
import cn.edu.hust.zrx.cct.base.magnet.MagnetAble;
import cn.edu.hust.zrx.cct.base.point.Point3;
import cn.edu.hust.zrx.cct.base.python.Plot3d;
import cn.edu.hust.zrx.cct.base.vector.Vector3;
import cn.edu.hust.zrx.cct.study.X旧机架研究.前45偏转段.A20200402对比研究;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

public class C20210715CCT速度测试 {
    @run(1)
    public void hello() {
        Logger.getLogger().info("hello world");
    }

    @run(2)
    public void plotCct(){
        SoleLayerCct cct = createCct();

        cct.plot3d();

        Plot3d.showThread();

        System.out.println(cct.magnetAt(Point3.origin()));
        System.out.println(cct.magnetAt(Point3.create(1,0,0)));
    }

    @run(3)
    public void cost(){
        SoleLayerCct cct = createCct();
        BaseUtils.Timer.printPeriodPerSecondCall(Logger.getLogger());
        for (int times = 0; times < 10000; times++) {
            BaseUtils.Timer.printPeriodPerSecondCall(Logger.getLogger());
            Vector3 sum = Vector3.getZero();
            for (int i = 0; i < 30*1000; i++) {
                Vector3 m = cct.magnetAt(Point3.create(i / 10000., 0, 0));
                sum.addSelf(m);
            }
            BaseUtils.Timer.printPeriodPerSecondCall(Logger.getLogger());
        }

//        System.out.println("sum = " + sum);


    }

    private SoleLayerCct createCct() {
        double smallR = 100 * MM;
        double bigR = 1.0;
        double angle = 45.0;
        int windingNumber = 15;
        CctLine2s cctLine2sInner = CctLine2Factory.create(
                smallR,
                bigR,
                angle,
                windingNumber,
                0,
                0,
                0,
                0,
                0,
                true
        );


        return new SoleLayerCct(cctLine2sInner.disperseToPoint3(360), 10000);
    }

    private static final double MM = 0.001;

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
