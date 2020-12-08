package cn.edu.hust.zrx.cct.study.U转Py;

import cn.edu.hust.zrx.cct.Logger;
import cn.edu.hust.zrx.cct.advanced.combined.GantryDataBipolarCo;
import cn.edu.hust.zrx.cct.advanced.combined.GantryDataBipolarCoUtils;
import cn.edu.hust.zrx.cct.base.BaseUtils;
import cn.edu.hust.zrx.cct.base.annotation.Run;
import cn.edu.hust.zrx.cct.base.line.Trajectory;
import cn.edu.hust.zrx.cct.base.magnet.qs.QsHardPlaneMagnet;
import cn.edu.hust.zrx.cct.base.point.Point2;
import cn.edu.hust.zrx.cct.base.point.Point3;
import cn.edu.hust.zrx.cct.base.vector.Vector2;
import cn.edu.hust.zrx.cct.base.vector.Vector3;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * Description
 * C20201116QS磁铁
 * <p>
 * Data
 * 16:49
 *
 * @author zrx
 * @version 1.0
 */

public class C20201116QS磁铁 {

    @Run(10)
    public void variables() {
        QsHardPlaneMagnet qs3 = getQs3();

        Vector2 d = qs3.getDirect().copy().normalSelf();
        Point2 p0 = qs3.getLocation();

        Vector2 right = d.copy().rotateSelf(-Math.PI / 2);

        double len = qs3.getLength_m();

        Logger.getLogger().info("len = " + len);
        Logger.getLogger().info("d = " + d);
        Logger.getLogger().info("p0 = " + p0);
        Logger.getLogger().info("right = " + right);
    }

    @Run(1)
    public void test1() {
        GantryDataBipolarCo.SecondBend secondBend = GantryDataBipolarCo.SecondBend.getDefault();

        secondBend.QS3_GRADIENT = 10;
        secondBend.QS3_SECOND_GRADIENT = 10000.;

        QsHardPlaneMagnet qs3 = getQs3(GantryDataBipolarCo.FirstBend.getDefault(), secondBend);

        Trajectory t = GantryDataBipolarCoUtils.getTrajectory2();


        double len = secondBend.DL2 + secondBend.CCT345_LENGTH + secondBend.GAP3 + secondBend.QS3_LEN / 2;
        Point3 p0 = t.pointAt(len).toPoint3();
        p0.moveSelf(Vector3.create(Math.random() * 0.1, Math.random() * 0.1, Math.random() * 0.1));

        Vector3 m = qs3.magnetAt(p0);

        Logger.getLogger().info("p0 = " + p0);
        Logger.getLogger().info("m = " + m);
    }


    private static QsHardPlaneMagnet getQs3() {
        return getQs3(GantryDataBipolarCo.FirstBend.getDefault(), GantryDataBipolarCo.SecondBend.getDefault());
    }

    private static QsHardPlaneMagnet getQs3(GantryDataBipolarCo.FirstBend firstBend, GantryDataBipolarCo.SecondBend secondBend) {
        Trajectory trajectoryPart2 = GantryDataBipolarCoUtils.getTrajectory2(firstBend, secondBend);


        return QsHardPlaneMagnet.create(secondBend.QS3_LEN, secondBend.QS3_GRADIENT, secondBend.QS3_SECOND_GRADIENT,
                secondBend.QS3_APERTURE_MM,
                trajectoryPart2.pointAt(secondBend.DL2 + secondBend.CCT345_LENGTH + secondBend.GAP3),
                trajectoryPart2.directAt(secondBend.DL2 + secondBend.CCT345_LENGTH + secondBend.GAP3));
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
