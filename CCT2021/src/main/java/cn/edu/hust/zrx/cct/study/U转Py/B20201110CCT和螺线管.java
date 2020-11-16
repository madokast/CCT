package cn.edu.hust.zrx.cct.study.U转Py;

import cn.edu.hust.zrx.cct.Logger;
import cn.edu.hust.zrx.cct.base.BaseUtils;
import cn.edu.hust.zrx.cct.base.CoordinateSystem3d;
import cn.edu.hust.zrx.cct.base.annotation.Run;
import cn.edu.hust.zrx.cct.base.cct.SoleLayerCct;
import cn.edu.hust.zrx.cct.base.point.Point3;
import cn.edu.hust.zrx.cct.base.point.Point3Function;
import cn.edu.hust.zrx.cct.base.python.Plot2d;
import cn.edu.hust.zrx.cct.base.vector.Vector3;
import org.apache.commons.math3.analysis.function.Sin;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static cn.edu.hust.zrx.cct.base.BaseUtils.Constant.MM;

/**
 * Description
 * B20201110CCT和螺线管
 * <p>
 * Data
 * 15:55
 *
 * @author zrx
 * @version 1.0
 */

public class B20201110CCT和螺线管 {

    @Run
    public void 螺线管() {
        int wn = 20;

        double r = 10 * MM;

        double len = 0.1;

        Point3Function pathFun = t -> Point3.create(r * Math.sin(t), r * Math.cos(t), t / (wn * 2 * Math.PI) * len);

        List<Point3> path = pathFun.disperse(0, wn * 2 * Math.PI, wn * 360);

        Vector3 p0 = BaseUtils.Magnet.magnetAtPoint(path, 1000, Point3.create(0, 0, 0));
        Vector3 p1 = BaseUtils.Magnet.magnetAtPoint(path, 1000, Point3.create(0, 0, 0.05));
        Vector3 p2 = BaseUtils.Magnet.magnetAtPoint(path, 1000, Point3.create(0, 0.001, 0.05));

        System.out.println("p0 = " + p0);
        System.out.println("p1 = " + p1);
        System.out.println("p2 = " + p2);

    }

    @Run(2)
    public void 螺线管2() {
        int wn = 1000;

        double r = 10 * MM;

        double len = 0.1;

        Point3Function pathFun = t -> Point3.create(r * Math.sin(t), r * Math.cos(t), t / (wn * 2 * Math.PI) * len);

        List<Point3> path = pathFun.disperse(0, wn * 2 * Math.PI, wn * 360);

        SoleLayerCct cct = SoleLayerCct.create(path, 1000);

        List<Point3> line = BaseUtils.Python.linspaceStream(
                Point3.create(0, 0, -0.1), Point3.create(0, 0, 0.2), 7105)
                .collect(Collectors.toList());

        List<Vector3> b = new ArrayList<>(line.size());

        for (Point3 p : line) {
            b.add(cct.magnetAt(p));
        }

        Plot2d.plot2(
                line.stream().mapToDouble(Point3::getZ).toArray(),
                b.stream().mapToDouble(Vector3::getZ).toArray(),Plot2d.RED_LINE
        );

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
