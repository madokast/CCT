package cn.edu.hust.zrx.cct.study.U转Py;

import cn.edu.hust.zrx.cct.Logger;
import cn.edu.hust.zrx.cct.base.BaseUtils;
import cn.edu.hust.zrx.cct.base.annotation.Run;
import cn.edu.hust.zrx.cct.base.magnet.qs.QsHardPlaneMagnet;
import cn.edu.hust.zrx.cct.base.point.Point2;
import cn.edu.hust.zrx.cct.base.point.Point3;
import cn.edu.hust.zrx.cct.base.python.Plot2d;
import cn.edu.hust.zrx.cct.base.vector.Vector2;
import cn.edu.hust.zrx.cct.base.vector.Vector3;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

import static cn.edu.hust.zrx.cct.base.BaseUtils.Constant.MM;

/**
 * Description
 * A20201109QS磁铁验证
 * <p>
 * Data
 * 11:34
 *
 * @author zrx
 * @version 1.0
 */

public class A20201109QS磁铁验证 {

    @Run
    private void test_四极场() {
        QsHardPlaneMagnet qs = QsHardPlaneMagnet.create(
                0.2, -45.7, 0, 30,
                Point2.origin(), Vector2.yDirect()
        );
        Point3 p = Point3.create(10 * MM, 0.1, 0 * MM);
        Vector3 m = qs.magnetAt(p);
        Logger.getLogger().info("m = " + m);
    }

    @Run(2)
    private void test_六极场() {
        Point3 p = Point3.create(-8 * MM, 0.1, 1 * MM);

        double[] doubles = BaseUtils.Python.linspaceStream(-100, 100, 10)
                .map(k -> {

                    QsHardPlaneMagnet qs = QsHardPlaneMagnet.create(
                            0.2, 0, k / 2, 30,
                            Point2.origin(), Vector2.yDirect()
                    );

                    Vector3 m = qs.magnetAt(p);

                    return m.x;
                })
                .toArray();

        Logger.getLogger().info("doubles = " + Arrays.toString(doubles));

        doubles = BaseUtils.Python.linspaceStream(-100, 100, 10)
                .map(k -> {

                    QsHardPlaneMagnet qs = QsHardPlaneMagnet.create(
                            0.2, 0, k / 2, 30,
                            Point2.origin(), Vector2.yDirect()
                    );
                    Vector3 m = qs.magnetAt(p);

                    return m.y;
                })
                .toArray();

        Logger.getLogger().info("doubles = " + Arrays.toString(doubles));

        doubles = BaseUtils.Python.linspaceStream(-100, 100, 10)
                .map(k -> {

                    QsHardPlaneMagnet qs = QsHardPlaneMagnet.create(
                            0.2, 0, k / 2, 30,
                            Point2.origin(), Vector2.yDirect()
                    );
                    Vector3 m = qs.magnetAt(p);

                    return m.z;
                })
                .toArray();

        Logger.getLogger().info("doubles = " + Arrays.toString(doubles));
    }

    @Run(3)
    private void test_四六极场() {
        Point3 p = Point3.create(-8 * MM, 0.1, 1 * MM);

        double[] doubles = BaseUtils.Python.linspaceStream(-100, 100, 10)
                .map(k -> {

                    QsHardPlaneMagnet qs = QsHardPlaneMagnet.create(
                            0.2, Math.sin(k / 180) * 20, Math.pow(1.1, k / 2), 30,
                            Point2.origin(), Vector2.yDirect()
                    );

                    Vector3 m = qs.magnetAt(p);

                    return m.x;
                })
                .toArray();

        Logger.getLogger().info("doubles = " + Arrays.toString(doubles));

        doubles = BaseUtils.Python.linspaceStream(-100, 100, 10)
                .map(k -> {

                    QsHardPlaneMagnet qs = QsHardPlaneMagnet.create(
                            0.2, Math.sin(k / 180) * 20, Math.pow(1.1, k / 2), 30,
                            Point2.origin(), Vector2.yDirect()
                    );
                    Vector3 m = qs.magnetAt(p);

                    return m.y;
                })
                .toArray();

        Logger.getLogger().info("doubles = " + Arrays.toString(doubles));

        doubles = BaseUtils.Python.linspaceStream(-100, 100, 10)
                .map(k -> {

                    QsHardPlaneMagnet qs = QsHardPlaneMagnet.create(
                            0.2, Math.sin(k / 180) * 20, Math.pow(1.1, k / 2), 30,
                            Point2.origin(), Vector2.yDirect()
                    );
                    Vector3 m = qs.magnetAt(p);

                    return m.z;
                })
                .toArray();

        Logger.getLogger().info("doubles = " + Arrays.toString(doubles));
    }

    @Run(-4)
    private void power() {
        System.out.println(Math.pow(1.1, 50));
    }

    @Run(10)
    private void EllipseTest_point_at() {
        Point2 point2 = BaseUtils.Geometry.ellipsePointTheta(5, 6, 7, 8, 9);
        Logger.getLogger().info("point2 = " + point2);

        double c = BaseUtils.Geometry.ellipseCircumference(5, 6, 7, 8);
        Logger.getLogger().info("c = " + c);

        Point2 p1 = BaseUtils.Geometry.ellipseWalkPoint(51, 61, 71, 81, 10);
        Logger.getLogger().info("p1 = " + p1);

        Point2 p2 = BaseUtils.Geometry.ellipseWalkPoint(51, 61, 71, 81, 20);
        Logger.getLogger().info("p2 = " + p2);

        Point2 p3 = BaseUtils.Geometry.ellipseWalkPoint(51, 61, 71, 81, 30);
        Logger.getLogger().info("p3 = " + p3);
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
