package cn.edu.hust.zrx.cct.study.Z验证性研究;

import cn.edu.hust.zrx.cct.Logger;
import cn.edu.hust.zrx.cct.advanced.MathFunction;
import cn.edu.hust.zrx.cct.base.BaseUtils;
import cn.edu.hust.zrx.cct.base.annotation.Run;
import cn.edu.hust.zrx.cct.base.point.Point2;
import cn.edu.hust.zrx.cct.base.point.Point2Function;
import cn.edu.hust.zrx.cct.base.python.Plot2d;
import cn.edu.hust.zrx.cct.base.vector.Vector2;
import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.FastFourierTransformer;
import org.apache.commons.math3.transform.TransformType;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Description
 * G02快速傅里叶变换
 * <p>
 * Data
 * 15:44
 *
 * @author zrx
 * @version 1.0
 */

public class G02快速傅里叶变换 {

    @Run(1)
    public void test01() {
        FastFourierTransformer fft = new FastFourierTransformer(DftNormalization.STANDARD);

        Complex[] transformed = fft.transform(
                ((MathFunction) Math::sin)
                        .disperse(0, 2 * Math.PI, 256),
                TransformType.FORWARD
        );

        Point2[] point2s = new Point2[transformed.length];
        for (int i = 0; i < transformed.length; i++) {
            point2s[i] = Point2.create(i, transformed[i].abs());
        }

        Plot2d.plot2(point2s, Plot2d.BLACK_POINT);

        Plot2d.showThread();
    }

    @Run(2)
    public void test02() {
        FastFourierTransformer fft = new FastFourierTransformer(DftNormalization.STANDARD);

        Complex[] transformed = fft.transform(
                ((MathFunction) Math::sin)
                        .add(x -> Math.sin(2 * x))
                        .disperse(0, 2 * Math.PI, 256),
                TransformType.FORWARD
        );

        Point2[] point2s = new Point2[transformed.length];
        for (int i = 0; i < transformed.length; i++) {
            point2s[i] = Point2.create(i, transformed[i].abs());
        }

        Plot2d.plot2(point2s, Plot2d.BLACK_POINT);

        Plot2d.showThread();
    }

    @Run(3)
    public void test03() {
        FastFourierTransformer fft = new FastFourierTransformer(DftNormalization.STANDARD);

        Complex[] transformed = fft.transform(
                ((MathFunction) Math::sin)
                        .add(x -> Math.sin(2 * x) * 10)
                        .disperse(0, 2 * Math.PI, 256),
                TransformType.FORWARD
        );

        Point2[] point2s = new Point2[transformed.length];
        for (int i = 0; i < transformed.length; i++) {
            point2s[i] = Point2.create(i, transformed[i].abs());
        }

        Plot2d.plot2(point2s, Plot2d.BLACK_POINT);

        Plot2d.showThread();
    }

    @Run(4)
    public void test04() {
        FastFourierTransformer fft = new FastFourierTransformer(DftNormalization.STANDARD);

        Complex[] transformed = fft.transform(
                ((MathFunction) Math::sin)
                        .add(x -> Math.sin(2 * x))
                        .add(x -> Math.sin(5 * x))
                        .disperse(0, 2 * Math.PI, 256),
                TransformType.FORWARD
        );

        Point2[] point2s = new Point2[transformed.length];
        for (int i = 0; i < transformed.length; i++) {
            point2s[i] = Point2.create(i, transformed[i].abs());
        }

        Plot2d.plot2(point2s, Plot2d.BLACK_POINT);

        Plot2d.showThread();
    }

    @Run(5)
    public void test05() {
        FastFourierTransformer fft = new FastFourierTransformer(DftNormalization.STANDARD);

        Complex[] transformed = fft.transform(
                ((MathFunction) Math::sin)
                        .add(x -> Math.sin(2 * x))
                        .add(x -> Math.sin(5 * x))
                        .add(512)
                        .disperse(0, 2 * Math.PI, 256),
                TransformType.FORWARD
        );

        Point2[] point2s = new Point2[transformed.length];
        for (int i = 0; i < transformed.length; i++) {
            point2s[i] = Point2.create(i, transformed[i].abs());
        }

        Plot2d.plot2(point2s, Plot2d.BLACK_POINT);

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
