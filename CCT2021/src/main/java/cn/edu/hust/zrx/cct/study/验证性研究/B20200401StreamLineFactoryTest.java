package cn.edu.hust.zrx.cct.study.验证性研究;

import cn.edu.hust.zrx.cct.Logger;
import cn.edu.hust.zrx.cct.base.BaseUtils;
import cn.edu.hust.zrx.cct.base.line.StreamLineFactory;
import cn.edu.hust.zrx.cct.base.particle.PhaseSpaceParticle;
import cn.edu.hust.zrx.cct.base.particle.PhaseSpaceParticles;
import cn.edu.hust.zrx.cct.base.point.Point2;
import cn.edu.hust.zrx.cct.base.python.Plot2d;
import cn.edu.hust.zrx.cct.study.超导机架AGCCT单独建模.A0401QS磁铁tracking;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

/**
 * Description
 * StreamLineFactory
 * <p>
 * Data
 * 22:46
 *
 * @author zrx
 * @version 1.0
 */
@SuppressWarnings("all")
public class B20200401StreamLineFactoryTest {

    @run(1)
    public void test(){
        List<Point2> list = StreamLineFactory.create(
                p -> Point2.convert(p, (x, y) -> -y, (x, y) -> -x),
                Point2.create(1, 0),
                MM,
                5, -5, 5, -5
        );

        Plot2d.plot2(list);
        Plot2d.equal();
        Plot2d.showThread();
    }

    @run(2)
    public void test2(){
        BaseUtils.StreamTools.from(-5).to(5)
                .filter(i->Math.abs(i)>0)
                .forEach(i->{
                    List<Point2> list = StreamLineFactory.create(
                            p -> Point2.convert(p, (x, y) -> -y, (x, y) -> -x),
                            Point2.create(i, 0),
                            MM,
                            5, -5, 5, -5
                    );

                    Plot2d.plot2(list,Plot2d.BLACK_LINE);

                    list = StreamLineFactory.create(
                            p -> Point2.convert(p, (x, y) -> -y, (x, y) -> -x),
                            Point2.create(0, i),
                            MM,
                            5, -5, 5, -5
                    );

                    Plot2d.plot2(list,Plot2d.BLACK_LINE);
                });

        Plot2d.equal();
        Plot2d.showThread();
    }


    @run(value = 300,code = "六极场01")
    public void test3(){


        for (int i = -5; i <=5; i++) {
            if(i==0)
                continue;//break
            for (int j = -5; j <=5 ; j++) {
                if(j==0)
                    continue;

                List<Point2> list = StreamLineFactory.create(
                        pp -> Point2.convert(pp, (x, y) -> 2*x*y, (x, y) -> x*x-y*y),
//                        pp -> Point2.convert(pp, (x, y) -> 2*x*y, (x, y) -> x*x),
                        Point2.create(i,j),
                        MM,
                        5, -5, 5, -5
                );

                Plot2d.plot2(list,Plot2d.BLACK_LINE);
            }
        }

        Plot2d.equal();
        Plot2d.showThread();
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
