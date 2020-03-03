package cn.edu.hust.zrx.cct.study.小论文画图三月三日;

import cn.edu.hust.zrx.cct.Logger;
import cn.edu.hust.zrx.cct.base.BaseUtils;
import cn.edu.hust.zrx.cct.base.cct.CctFactory;
import cn.edu.hust.zrx.cct.base.line.Trajectory;
import cn.edu.hust.zrx.cct.base.line.TrajectoryFactory;
import cn.edu.hust.zrx.cct.base.point.Point2;
import cn.edu.hust.zrx.cct.base.point.Point3;
import cn.edu.hust.zrx.cct.base.python.Plot2d;
import cn.edu.hust.zrx.cct.base.python.Plot3d;
import cn.edu.hust.zrx.cct.base.vector.Vector2;
import cn.edu.hust.zrx.cct.base.vector.Vector3;
import cn.edu.hust.zrx.cct.study.超导机架AGCCT单独建模.左手侧磁场分析20200220;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Description
 * 小论文 2020年3月3日
 * 画图 67.5度 AG-CCT
 * <p>
 * Data
 * 11:55
 *
 * @author zrx
 * @version 1.0
 */

public class 六十七点五三段AGCCT {
    //    @run
    public void 二极CCT() {
        Trajectory trajectory = getTrajectory();
        CctFactory.Cct dipoleCct = createDipoleCct();

        List<Point2> magnetBzAlongTrajectory = dipoleCct.magnetBzAlongTrajectory(trajectory, 1 * MM);
        magnetBzAlongTrajectory = Point2.convert(
                magnetBzAlongTrajectory,
                (x, y) -> BaseUtils.Converter.radianToAngle((x - 1) / bigR),
                (x, y) -> y);
        Plot2d.plot2(magnetBzAlongTrajectory);

        trajectory.plot3d();
        dipoleCct.plot3(Plot2d.RED_LINE, Plot2d.YELLOW_LINE);

        Plot3d.setCube(0.8);
        Plot3d.showThread();

        Plot2d.showThread();
    }

    //    @run
    public void 四极AgCct() {
        Trajectory trajectory = getTrajectory();
        CctFactory.Cct agCct = createAgCct();

        BaseUtils.Async async = new BaseUtils.Async();
        BaseUtils.Timer timer = new BaseUtils.Timer();

        async.execute(() -> {
            timer.printPeriodAfterInitial(Logger.getLogger());
            List<Point2> point2s = agCct.magnetGradientAlongTrajectoryFast(
                    trajectory, 1 * MM, 2 * MM);
            point2s = Point2.convert(
                    point2s,
                    (x, y) -> BaseUtils.Converter.radianToAngle((x - 1) / bigR),
                    (x, y) -> y);
            Logger.getLogger().info("agCct = " + agCct);
            Plot2d.plot2(point2s, Plot2d.BLACK_LINE);
            timer.printPeriodAfterInitial(Logger.getLogger());
        });

        //分开求梯度
        if (false) {
            //只有内层，外层电流为 0
            double temp = this.agCctIOuter;
            this.agCctIOuter = 0;
            CctFactory.Cct agCctInnerOnly = createAgCct();
            this.agCctIOuter = temp;

            //只有外层，内层电流 0
            temp = this.agCctIInner;
            this.agCctIInner = 0;
            CctFactory.Cct agCctOuterOnly = createAgCct();
            this.agCctIInner = temp;

            async.execute(() -> {
                timer.printPeriodAfterInitial(Logger.getLogger());
                List<Point2> point2s = agCctInnerOnly.magnetGradientAlongTrajectoryFast(
                        trajectory, 1 * MM, 2 * MM);
                point2s = Point2.convert(
                        point2s,
                        (x, y) -> BaseUtils.Converter.radianToAngle((x - 1) / bigR),
                        (x, y) -> y);
                Logger.getLogger().info("agCctInnerOnly = " + agCctInnerOnly);
                Plot2d.plot2(point2s, Plot2d.YELLOW_LINE);
                timer.printPeriodAfterInitial(Logger.getLogger());
            });

            async.execute(() -> {
                timer.printPeriodAfterInitial(Logger.getLogger());
                List<Point2> point2s = agCctOuterOnly.magnetGradientAlongTrajectoryFast(
                        trajectory, 1 * MM, 2 * MM);
                point2s = Point2.convert(
                        point2s,
                        (x, y) -> BaseUtils.Converter.radianToAngle((x - 1) / bigR),
                        (x, y) -> y);
                Logger.getLogger().info("agCctOuterOnly = " + agCctOuterOnly);
                Plot2d.plot2(point2s, Plot2d.RED_LINE);
                timer.printPeriodAfterInitial(Logger.getLogger());
            });

            //验证一下全层 对不对
            async.execute(() -> {
                timer.printPeriodAfterInitial(Logger.getLogger());
                CctFactory.Cct agCctBoth = createAgCct();
                Vector3 agCctBothOrigin = agCctBoth.magnetAt(Point3.origin());
                Vector3 agCctOrigin = agCct.magnetAt(Point3.origin());
                Logger.getLogger().info("agCctBothOrigin = " + agCctBothOrigin);
                Logger.getLogger().info("agCctOrigin = " + agCctOrigin);
                timer.printPeriodAfterInitial(Logger.getLogger());
            });
        }

        async.waitForAllFinish(10, TimeUnit.MINUTES);


        trajectory.plot3d();
        agCct.plot3(Plot2d.RED_LINE, Plot2d.YELLOW_LINE, Plot2d.RED_LINE);

        Plot3d.setCube(0.8);
        Plot3d.showThread();

        Plot2d.plot2(new Point2[]{
                Point2.create(-50, 0),
                Point2.create(0, 0),
                Point2.create(0, 18.14),
                Point2.create(agCctAngle0, 18.14),
                Point2.create(agCctAngle0, -18.14),
                Point2.create(agCctAngle0 + agCctAngle1, -18.14),
                Point2.create(agCctAngle0 + agCctAngle1, 18.14),
                Point2.create(agCctAngle0 + agCctAngle1 + agCctAngle2, 18.14),
                Point2.create(agCctAngle0 + agCctAngle1 + agCctAngle2, 0),
                Point2.create(agCctAngle0 + agCctAngle1 + agCctAngle2 + 50, 0),
        }, Plot2d.GREY_DASH);
        Plot2d.showThread();

    }

    @run// 论文图 不要改动
    public void 全部之二极场() {
        double delta = 10 * MM;

        BaseUtils.Timer timer = new BaseUtils.Timer();
        Trajectory trajectory = getTrajectory();
        trajectory.plot3d();

        CctFactory.Cct agCct = createAgCct();
        CctFactory.Cct dipoleCct = createDipoleCct();

        Plot2d.plot2(
                Point2.convert(dipoleCct.magnetBzAlongTrajectory(trajectory, delta),
                        (x, y) -> BaseUtils.Converter.radianToAngle((x - 1) / bigR),
                        (x, y) -> y),
                Plot2d.RED_LINE);
        timer.printPeriodAfterInitial(Logger.getLogger());


        Plot2d.plot2(
                Point2.convert(agCct.magnetBzAlongTrajectory(trajectory, delta),
                        (x, y) -> BaseUtils.Converter.radianToAngle((x - 1) / bigR),
                        (x, y) -> y),
                Plot2d.BLUE_LINE);
        timer.printPeriodAfterInitial(Logger.getLogger());

        CctFactory.Cct combineCct = CctFactory.combineCct(dipoleCct, agCct);

        Plot2d.plot2(
                Point2.convert(combineCct.magnetBzAlongTrajectory(trajectory, delta),
                        (x, y) -> BaseUtils.Converter.radianToAngle((x - 1) / bigR),
                        (x, y) -> y),
                Plot2d.BLACK_LINE);
        timer.printPeriodAfterInitial(Logger.getLogger());

        Plot2d.plot2(new Point2[]{
                Point2.create(-50, 0),
                Point2.create(0, 0),
                Point2.create(0, 3.24),
                Point2.create(dipoleCctAngle, 3.24),
                Point2.create(dipoleCctAngle, 0),
                Point2.create(dipoleCctAngle + 50, 0),
        }, Plot2d.GREY_DASH);

//        plt.legend(labels=['up', 'down'])
        Plot2d.info("Bending angle(degree)", "Dipole field(T)", "", 30);
        Plot2d.legend(20,"Dipole CCT","AG-CCT","Dipole CCT + AG-CCT","SCOFF");


        Plot2d.showThread();
        timer.printPeriodAfterInitial(Logger.getLogger());


//        dipoleCct.plot3(Plot2d.BLACK_LINE,Plot2d.BLACK_LINE);
//
//        agCct.plot3(Plot2d.RED_LINE, Plot2d.YELLOW_LINE, Plot2d.RED_LINE,
//                Plot2d.RED_LINE, Plot2d.YELLOW_LINE, Plot2d.RED_LINE);
//
//        Plot3d.setCube(0.8);
//        Plot3d.showThread();
    }

//    @run// 论文图 不要改动
    public void 全部之四极场() {
        double delta = 10 * MM;

        BaseUtils.Timer timer = new BaseUtils.Timer();
        Trajectory trajectory = getTrajectory();
        trajectory.plot3d();

        CctFactory.Cct agCct = createAgCct();
        CctFactory.Cct dipoleCct = createDipoleCct();

        Plot2d.plot2(
                Point2.convert(dipoleCct.magnetGradientAlongTrajectoryFast(trajectory, delta,5*MM),
                        (x, y) -> BaseUtils.Converter.radianToAngle((x - 1) / bigR),
                        (x, y) -> y),
                Plot2d.RED_LINE);
        timer.printPeriodAfterInitial(Logger.getLogger());


        Plot2d.plot2(
                Point2.convert(agCct.magnetGradientAlongTrajectoryFast(trajectory, delta,5*MM),
                        (x, y) -> BaseUtils.Converter.radianToAngle((x - 1) / bigR),
                        (x, y) -> y),
                Plot2d.BLUE_LINE);
        timer.printPeriodAfterInitial(Logger.getLogger());

        CctFactory.Cct combineCct = CctFactory.combineCct(dipoleCct, agCct);

        Plot2d.plot2(
                Point2.convert(combineCct.magnetGradientAlongTrajectoryFast(trajectory, delta,5*MM),
                        (x, y) -> BaseUtils.Converter.radianToAngle((x - 1) / bigR),
                        (x, y) -> y),
                Plot2d.BLACK_LINE);
        timer.printPeriodAfterInitial(Logger.getLogger());

        Plot2d.plot2(new Point2[]{
                Point2.create(-50, 0),
                Point2.create(0, 0),
                Point2.create(0, 18.14),
                Point2.create(agCctAngle0, 18.14),
                Point2.create(agCctAngle0, -18.14),
                Point2.create(agCctAngle0 + agCctAngle1, -18.14),
                Point2.create(agCctAngle0 + agCctAngle1, 18.14),
                Point2.create(agCctAngle0 + agCctAngle1 + agCctAngle2, 18.14),
                Point2.create(agCctAngle0 + agCctAngle1 + agCctAngle2, 0),
                Point2.create(agCctAngle0 + agCctAngle1 + agCctAngle2 + 50, 0),
        }, Plot2d.GREY_DASH);

//        plt.legend(labels=['up', 'down'])
        Plot2d.info("Bending angle(degree)", "Quadrupole field(T/m)", "", 30);
        Plot2d.legend(20,"Dipole CCT","AG-CCT","Dipole CCT + AG-CCT","SCOFF");


        Plot2d.showThread();
        timer.printPeriodAfterInitial(Logger.getLogger());


//        dipoleCct.plot3(Plot2d.BLACK_LINE,Plot2d.BLACK_LINE);
//
//        agCct.plot3(Plot2d.RED_LINE, Plot2d.YELLOW_LINE, Plot2d.RED_LINE,
//                Plot2d.RED_LINE, Plot2d.YELLOW_LINE, Plot2d.RED_LINE);
//
//        Plot3d.setCube(0.8);
//        Plot3d.showThread();
    }

    //    @run
    public void asyncTest() {
        BaseUtils.Async async = new BaseUtils.Async();
        BaseUtils.Timer timer = new BaseUtils.Timer();

        BaseUtils.Python.linspaceStream(1000, 1003, 4)
                .forEach(t -> {
                    long time = (int) t;
                    async.execute(() -> {
                        timer.printPeriodAfterInitial(Logger.getLogger());
                        try {
                            Thread.sleep(time);
                        } catch (InterruptedException ignored) {
                        }
                        timer.printPeriodAfterInitial(Logger.getLogger());
                    });
                });

        async.waitForAllFinish(10, TimeUnit.SECONDS);
    }


    /*-----------------系数----------------------*/

    private double bigR = 0.75;
    //孔径
    private double agCctSmallRInner = 23.5 * MM + 40 * MM;
    private double agCctSmallROuter = 33.5 * MM + 40 * MM;
    private double dipoleCctSmallRInner = 43 * MM + 40 * MM;
    private double dipoleCctSmallROuter = 52 * MM + 40 * MM;
    //角度
    private double dipoleCctAngle = 67.5;
    private double agCctAngle0 = 8.0;
    private double agCctAngle1 = 30.0;
    private double agCctAngle2 = 29.5;
    // a0 a1 a2
    private double dipoleCctA0Inner = -dipoleCctSmallRInner * Math.sqrt(3);
    private double dipoleCctA0Outer = dipoleCctSmallROuter * Math.sqrt(3);
    private double dipoleCctA1Inner = 0.0;
    private double dipoleCctA1Outer = 0.0;
    private double dipoleCctA2Inner = 0.0;
    private double dipoleCctA2Outer = 0.0;

    private double agCctA0Inner = 0.0;
    private double agCctA0Outer = 0.0;
    private double agCctA1Inner = -Math.pow(agCctSmallRInner, 2) * Math.sqrt(3) * 6;
    private double agCctA1Outer = Math.pow(agCctSmallROuter, 2) * Math.sqrt(3) * 6;
    private double agCctA2Inner = 0.0;
    private double agCctA2Outer = 0.0;
    // 匝数 5mm匝间距 则匝数 = 58.85
    private int dipoleCctWindingNumber = 176;
    private int agCctWindingNumber0 = 16;
    private int agCctWindingNumber1 = 60;
    private int agCctWindingNumber2 = 59; // sum = 135
    // 电流
    private double dipoleCctIInner = -9.898121356964111 * kA * 3.24 / 3.27; // 求解获得
    private double dipoleCctIOuter = -dipoleCctIInner;
    private double agCctIInner = 6000;
    private double agCctIOuter = -agCctIInner;
    // CCT每匝分段
    private int numberPerWinding = 360;

    /*-----------------create methods----------------------*/

    public Trajectory getTrajectory() {
        return TrajectoryFactory.setStartingPoint(bigR, -1)
                .setStraightLine(1, Vector2.yDirect())
                .addArcLine(bigR, false, dipoleCctAngle)
                .addStraitLine(1.0);
    }

    public CctFactory.Cct createDipoleCct() {
        return CctFactory.createDipoleCct(
                dipoleCctSmallRInner, dipoleCctSmallROuter, bigR,
                dipoleCctAngle, dipoleCctWindingNumber,
                dipoleCctA0Inner, dipoleCctA1Inner, dipoleCctA2Inner, dipoleCctIInner,
                dipoleCctA0Outer, dipoleCctA1Outer, dipoleCctA2Outer, dipoleCctIOuter,
                numberPerWinding
        );
    }

    public CctFactory.Cct createAgCct() {
        //public static Cct createAgCct(double smallRInner,
        //                                  double smallROuter,
        //                                  double bigR,
        //                                  double[] angles,
        //                                  int[] windingNumbers,
        //                                  double a0BipolarInners,
        //                                  double a1QuadrupleInners,
        //                                  double a2SextupleInners,
        //                                  double IInner,
        //                                  double a0BipolarOuters,
        //                                  double a1QuadrupleOuters,
        //                                  double a2SextupleOuters,
        //                                  double IOuter,
        //                                  int numberPerWinding)
        return CctFactory.createAgCct(agCctSmallRInner, agCctSmallROuter, bigR,
                new double[]{agCctAngle0, agCctAngle1, agCctAngle2},
                new int[]{agCctWindingNumber0, agCctWindingNumber1, agCctWindingNumber2},
                agCctA0Inner, agCctA1Inner, agCctA2Inner, agCctIInner,
                agCctA0Outer, agCctA1Outer, agCctA2Outer, agCctIOuter,
                numberPerWinding);
    }

    /*-----------------辅助量----------------------*/
    final static double MM = 1e-3;
    final static double kA = 1e3;

    public static void main(String[] args) throws Exception {
        StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[1];
        Logger.getLogger().info("{}", stackTraceElement.getClassName());
        Class<?> currentClass = Class.forName(stackTraceElement.getClassName());

        Constructor<?> constructor = currentClass.getConstructor(null);
        Object newInstance = constructor.newInstance();

        Method[] methods = currentClass.getMethods();
        Arrays.stream(methods)
                .filter(method -> method.isAnnotationPresent(六十七点五三段AGCCT.run.class))
                .limit(1)
                .forEach(method -> {
                    Logger.getLogger().info("运行{}", method.toString());
                    BaseUtils.Timer.printPeriodPerSecondCall(null);
                    try {
                        method.invoke(newInstance);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                    BaseUtils.Timer.printPeriodPerSecondCall(Logger.getLogger());
                });

    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    private static @interface run {
    }
}
