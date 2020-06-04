package cn.edu.hust.zrx.cct.study.验证性研究;

import cn.edu.hust.zrx.cct.Logger;
import cn.edu.hust.zrx.cct.base.cct.CctFactory;
import cn.edu.hust.zrx.cct.base.line.Trajectory;
import cn.edu.hust.zrx.cct.base.line.TrajectoryFactory;
import cn.edu.hust.zrx.cct.base.point.Point2;
import cn.edu.hust.zrx.cct.base.python.Plot2d;
import cn.edu.hust.zrx.cct.base.python.Plot3d;
import cn.edu.hust.zrx.cct.base.vector.Vector2;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

/**
 * Description
 * A20200212AGCCT建模
 * <p>
 * Data
 * 0:01
 *
 * @author zrx
 * @version 1.0
 */
@SuppressWarnings("all")
public class A20200212AGCCT建模 {

    //public static Cct createAgCct(double smallRInner,
    //                                  double smallROuter,
    //                                  double bigR,
    //                                  double[] angles,
    //                                  int[] windingNumbers,
    //                                  double[] a0BipolarInners,
    //                                  double[] a1QuadrupleInners,
    //                                  double[] a2SextupleInners,
    //                                  double IInner,
    //                                  double[] a0BipolarOuters,
    //                                  double[] a1QuadrupleOuters,
    //                                  double[] a2SextupleOuters,
    //                                  double IOuter,
    //                                  int numberPerWinding)

    @run
    public void 建模测试() {
//        CctFactory.Cct agCct = CctFactory.createAgCct(60 * MM, 75 * MM, 0.75,
//                new double[]{22.5, 22.5, 22.5},
//                new int[]{45, 45, 45},
//                new double[]{0, 0, 0},
//                new double[]{0.1, 0.1, 0.1},
//                new double[]{0, 0, 0},
//                5000,
//                new double[]{0, 0, 0},
//                new double[]{-0.1, -0.1, -0.1},
//                new double[]{0, 0, 0},
//                -5000,
//                360);

        CctFactory.Cct agCct = CctFactory.createAgCct(60 * MM, 75 * MM, 0.75,
                new double[]{22.5, 22.5, 22.5},
                new int[]{45, 45, 45},
                0,
                0.1,
                0,
                5000,
                0,
                -0.1,
                0,
                -5000,
                360);

        final Trajectory trajectory = TrajectoryFactory.setStartingPoint(Point2.create(0.75, -1.0))
                .setStraightLine(1.0, Vector2.yDirect())
                .addArcLine(0.75, false, 67.5)
                .addStraitLine(1.0);
        trajectory.plot3d(1*MM,Plot2d.BLACK_LINE);

        agCct.plot3(Plot2d.YELLOW_LINE,Plot2d.RED_LINE,Plot2d.YELLOW_LINE);
        Plot3d.setCube(0.8);
        Plot3d.showThread();

        List<Point2> magnetGradientAlongTrajectoryFast = agCct.magnetGradientAlongTrajectoryFast(trajectory, 1 * MM, 10 * MM);
        Plot2d.plot2(magnetGradientAlongTrajectoryFast);
        Plot2d.show();

    }


    /*-----------------辅助量----------------------*/
    final static double MM = 1e-3;

    public static void main(String[] args) throws Exception {
        StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[1];
        Logger.getLogger().info("{}", stackTraceElement.getClassName());
        Class<?> currentClass = Class.forName(stackTraceElement.getClassName());

        Constructor<?> constructor = currentClass.getConstructor(null);
        Object newInstance = constructor.newInstance();

        Method[] methods = currentClass.getMethods();
        Arrays.stream(methods)
                .filter(method -> method.isAnnotationPresent(A20200212AGCCT建模.run.class))
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

    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    private static @interface run {
    }
}
