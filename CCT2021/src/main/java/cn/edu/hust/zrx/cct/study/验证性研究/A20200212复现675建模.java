package cn.edu.hust.zrx.cct.study.验证性研究;

import cn.edu.hust.zrx.cct.Logger;
import cn.edu.hust.zrx.cct.base.BaseUtils;
import cn.edu.hust.zrx.cct.base.cct.Cct;
import cn.edu.hust.zrx.cct.base.cct.CctFactory;
import cn.edu.hust.zrx.cct.base.cct.SoleLayerCct;
import cn.edu.hust.zrx.cct.base.line.Trajectory;
import cn.edu.hust.zrx.cct.base.line.TrajectoryFactory;
import cn.edu.hust.zrx.cct.base.point.Point2;
import cn.edu.hust.zrx.cct.base.point.Point3;
import cn.edu.hust.zrx.cct.base.python.Plot2d;
import cn.edu.hust.zrx.cct.base.python.Plot3d;
import cn.edu.hust.zrx.cct.base.vector.Vector2;
import cn.edu.hust.zrx.cct.base.vector.Vector3;

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
 * A20200212复现675建模
 * <p>
 * Data
 * 20:20
 *
 * @author zrx
 * @version 1.0
 */

@SuppressWarnings("all")
public class A20200212复现675建模 {
    /**
     * 恭喜！
     * 2020年2月12日
     * 复现成功
     */

//    @run
    public Cct 复现675建模() {
        double alphaAngle = 30;
        double alphaR = BaseUtils.Converter.angleToRadian(alphaAngle);
        double tanAlpha = Math.tan(alphaR);
        double a0 = 78 * MM / tanAlpha;
        Logger.getLogger().info("a0 = " + a0);

        Cct dipoleCct = CctFactory.createDipoleCct(73 * MM, 82 * MM,
                1.00254, 67.5, 241,
                a0, 0, 0, 5450.5,
                -a0, 0, 0, -5450.5,
                360);

        dipoleCct.plot3(Plot2d.RED_LINE,Plot2d.YELLOW_LINE);

        Trajectory trajectory = TrajectoryFactory.setStartingPoint(Point2.create(1, -1))
                .setStraightLine(1, Vector2.yDirect())
                .addArcLine(1, false, 67.5)
                .addStraitLine(1);

        trajectory.plot3d(1*MM, Plot2d.BLACK_LINE);

        List<Point2> magnetBzAlongTrajectory = dipoleCct.magnetBzAlongTrajectory(trajectory, 1 * MM);

        Plot2d.plot2(magnetBzAlongTrajectory);
        Plot2d.showThread();

        Plot3d.setCube(1.1);
        Plot3d.showThread();


        //--------------------------------------------------------------


        return dipoleCct;

    }

    @run
    public void a0ToAngle(){
        double alphaAngleXX = 30;
        double alphaRXX = BaseUtils.Converter.angleToRadian(alphaAngleXX);
        double tanAlphaXX = Math.tan(alphaRXX);
        double a0 = 78 * MM / tanAlphaXX;
        Cct cct = 复现675建模();

        SoleLayerCct soleLayerCct = cct.get(0);

        Logger.getLogger().info("电流 = " + soleLayerCct.getI());

        Point3 p0 = soleLayerCct.getWindings().get(0);
        Point3 p1 = soleLayerCct.getWindings().get(1);

//                        Logger.getLogger().("p0 = " + p0);
//                        Logger.getLogger().info("p1 = " + p1);

        Vector3 v01 = Vector3.subtract(p1, p0);

//                        Logger.getLogger().info("v01 = " + v01);

        Vector3 t = v01.copy();

        Vector3 theta = Vector3.getZDirect();

        Vector3 z = Vector3.getYDirect().normalSelf();

        double tanAlpha = (Vector3.dot(t,theta))/(Vector3.dot(t,z));
        double alphaRadian = Math.atan(tanAlpha);
        double alphaAngle = BaseUtils.Converter.radianToAngle(alphaRadian);

        Logger.getLogger().info("a0 = " + a0);

        Logger.getLogger().info("t = " + t);
        Logger.getLogger().info("z = " + z);
        Logger.getLogger().info("theta = " + theta);
//                        Logger.getLogger().info("倾斜角 alphaRadian = " + alphaRadian);
        Logger.getLogger().info("倾斜角度 alphaAngle = " + alphaAngle);

        // 推算倾斜角
        double tanAlphaCalc = 70*MM / (1.0 * a0);
        double alphaRadianCalc = Math.atan(tanAlphaCalc);
        double alphaAngleCalc  = BaseUtils.Converter.radianToAngle(alphaRadianCalc);

        Logger.getLogger().info("推测倾斜角度 alphaAngleCalc = " + alphaAngleCalc);
        Logger.getLogger().info("推算误差 = " + Math.abs(alphaAngle - alphaAngleCalc));
        Logger.getLogger().info("--------------------------");

        //20:36:30.513 [main] INFO cn.edu.hust.zrx.cct.stydy.验证性研究.A20200212复现675建模 - cn.edu.hust.zrx.cct.stydy.验证性研究.A20200212复现675建模
        //20:36:30.556 [main] INFO cn.edu.hust.zrx.cct.stydy.验证性研究.A20200212复现675建模 - 运行public void cn.edu.hust.zrx.cct.stydy.验证性研究.A20200212复现675建模.a0ToAngle()
        //20:36:30.589 [main] INFO cn.edu.hust.zrx.cct.stydy.验证性研究.A20200212复现675建模 - a0 = 0.13509996299037244
        //20:36:40.326 [main] INFO cn.edu.hust.zrx.cct.stydy.验证性研究.A20200212复现675建模 - 电流 = 5450.5
        //20:36:40.327 [main] INFO cn.edu.hust.zrx.cct.stydy.验证性研究.A20200212复现675建模 - a0 = 0.13509996299037244
        //20:36:40.327 [main] INFO cn.edu.hust.zrx.cct.stydy.验证性研究.A20200212复现675建模 - t = [-1.4142386501214688E-5, 0.0025505049392390037, 0.0012740256699216963]
        //20:36:40.327 [main] INFO cn.edu.hust.zrx.cct.stydy.验证性研究.A20200212复现675建模 - z = [0.0, 1.0, 0.0]
        //20:36:40.327 [main] INFO cn.edu.hust.zrx.cct.stydy.验证性研究.A20200212复现675建模 - theta = [0.0, 0.0, 1.0]
        //20:36:40.328 [main] INFO cn.edu.hust.zrx.cct.stydy.验证性研究.A20200212复现675建模 - 倾斜角度 alphaAngle = 26.54299939682113
        //20:36:40.328 [main] INFO cn.edu.hust.zrx.cct.stydy.验证性研究.A20200212复现675建模 - 推测倾斜角度 alphaAngleCalc = 27.390248381342904
        //20:36:40.328 [main] INFO cn.edu.hust.zrx.cct.stydy.验证性研究.A20200212复现675建模 - 推算误差 = 0.847248984521773
        //20:36:40.328 [main] INFO cn.edu.hust.zrx.cct.stydy.验证性研究.A20200212复现675建模 - --------------------------
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
                .filter(method -> method.isAnnotationPresent(A20200212复现675建模.run.class))
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
