package cn.edu.hust.zrx.cct.study.V双极点坐标系机架;

import cn.edu.hust.zrx.cct.Logger;
import cn.edu.hust.zrx.cct.advanced.CctUtils;
import cn.edu.hust.zrx.cct.advanced.MathFunction;
import cn.edu.hust.zrx.cct.advanced.opera.Brick8s;
import cn.edu.hust.zrx.cct.advanced.opera.OperaCct;
import cn.edu.hust.zrx.cct.base.BaseUtils;
import cn.edu.hust.zrx.cct.base.annotation.Run;
import cn.edu.hust.zrx.cct.base.cct.Elements;
import cn.edu.hust.zrx.cct.base.cct.SoleLayerCct;
import cn.edu.hust.zrx.cct.base.line.Trajectory;
import cn.edu.hust.zrx.cct.base.line.TrajectoryFactory;
import cn.edu.hust.zrx.cct.base.point.*;
import cn.edu.hust.zrx.cct.base.python.Plot2d;
import cn.edu.hust.zrx.cct.base.python.Plot3d;
import cn.edu.hust.zrx.cct.base.vector.Vector2;
import org.apache.commons.math3.util.FastMath;

import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static cn.edu.hust.zrx.cct.base.BaseUtils.Constant.MM;

/**
 * Description
 * 匝数	159
 * 偏转半径	0.9mm
 * 光学孔径/直径	290mm
 * 内层线模型CCT半径	150mm
 * 外层线模型CCT半径	160mm
 * 磁铁偏转角度	50度
 * 二极场大小	2.4T
 * 电流	922A
 * 倾斜角	未知
 * <p>
 * Data
 * 16:14
 *
 * @author zrx
 * @version 1.0
 */


public class B20201013LBNL2020二极CCT复现 {

    private double tiltAngle0 = 34.5;

    @Run
    public SoleLayerCct in内层CCT() {
        /**
         * 建立一个 单层cct线圈 不考虑位置，需要什么呢？
         * bigR smallR bendingAngle
         * tiltAngle（X） -> φ(ξ)
         * windingNumber I0
         */

        final double bigR = 900 * MM;
        final double smallR = 150 * MM;
        final double bendingAngle = 50;
        final double bendingRadian = BaseUtils.Converter.angleToRadian(bendingAngle);
        final double tiltAngle = 31.5; // 待定
        final double tiltRadian = BaseUtils.Converter.angleToRadian(tiltAngle);
        final int windingNumber = 159;
        final double endKsi = windingNumber * Math.PI * 2;
        final double I0 = 922 * 6;


        final double a = Math.sqrt(bigR * bigR - smallR * smallR);

        final double eta = 0.5 * Math.log((bigR + a) / (bigR - a));
        final double eta_1 = FastMath.asinh(Math.sqrt((bigR * bigR) / (smallR * smallR) - 1));
        Logger.getLogger().info("eta = " + eta); // eta = 2.4778887302884756
        Logger.getLogger().info("eta_1 = " + eta_1); // eta_1 = 2.477888730288475

        final double C1 = (1 / Math.tan(tiltRadian)) / (1 * Math.sinh(eta));


        final double phi0 = bendingRadian / windingNumber;
        System.out.println(phi0 * bigR);

        MathFunction path2MF = ksi -> C1 * Math.sin(1 * ksi) + phi0 * ksi / (2 * Math.PI);
        Point2Function path2P2F = Point2Function.create(MathFunction.identity(), path2MF);


        BipolarToroidalCoordinateSystemPoint2To3 bipolarToroidalCoordinateSystemPoint2To3 =
                new BipolarToroidalCoordinateSystemPoint2To3(a, eta);

        Point3Function path3P3F = bipolarToroidalCoordinateSystemPoint2To3.convert(path2P2F);

        List<Point3> dispersedPath3 = path3P3F.disperse(0, endKsi, windingNumber * 360);

//        Plot3d.plot3(dispersedPath3, Plot2d.BLACK_LINE);
//        Plot3d.showThread();

        SoleLayerCct soleLayerCct = SoleLayerCct.create(dispersedPath3, I0);

        return soleLayerCct;
    }

    @Run
    public SoleLayerCct out外层CCT() {
        final double bigR = 900 * MM;
        final double smallR = 160 * MM;
        final double bendingAngle = 50;
        final double bendingRadian = BaseUtils.Converter.angleToRadian(bendingAngle);
        final double tiltAngle = -tiltAngle0; // 待定
        final double tiltRadian = BaseUtils.Converter.angleToRadian(tiltAngle);
        final int windingNumber = 159;
        final double endKsi = windingNumber * Math.PI * 2;
        final double I0 = -922 * 6;


        final double a = Math.sqrt(bigR * bigR - smallR * smallR);

        final double eta = 0.5 * Math.log((bigR + a) / (bigR - a));
        final double eta_1 = FastMath.asinh(Math.sqrt((bigR * bigR) / (smallR * smallR) - 1));
        Logger.getLogger().info("eta = " + eta); // eta = 2.4778887302884756
        Logger.getLogger().info("eta_1 = " + eta_1); // eta_1 = 2.477888730288475

        final double C1 = (1 / Math.tan(tiltRadian)) / (1 * Math.sinh(eta));


        final double phi0 = bendingRadian / windingNumber;

        MathFunction path2MF = ksi -> C1 * Math.sin(1 * ksi) + phi0 * ksi / (2 * Math.PI);
        Point2Function path2P2F = Point2Function.create(MathFunction.identity(), path2MF);


        BipolarToroidalCoordinateSystemPoint2To3 bipolarToroidalCoordinateSystemPoint2To3 =
                new BipolarToroidalCoordinateSystemPoint2To3(a, eta);

        Point3Function path3P3F = bipolarToroidalCoordinateSystemPoint2To3.convert(path2P2F);

        List<Point3> dispersedPath3 = path3P3F.disperse(0, endKsi, windingNumber * 360);

        SoleLayerCct soleLayerCct = SoleLayerCct.create(dispersedPath3, I0);

        return soleLayerCct;
    }

    @Run(-1)
    public void 磁场分布() {
        SoleLayerCct in = in内层CCT();
        SoleLayerCct out = out外层CCT();

        Trajectory trajectory = TrajectoryFactory.setStartingPoint(900 * MM, -1)
                .setStraightLine(1, Vector2.yDirect())
                .addArcLine(900 * MM, false, 50)
                .addStraitLine(1);

        Elements elements = Elements.of(in, out);

        List<Point2> bz = elements.magnetBzAlongTrajectory(trajectory);
        List<Point2> bz1 = in.magnetBzAlongTrajectory(trajectory);
        List<Point2> bz2 = out.magnetBzAlongTrajectory(trajectory);

//        in.plot3(Plot2d.BLUE_LINE);
//        out.plot3(Plot2d.BLUE_LINE);
//
//        trajectory.plot3d();

        Plot2d.plot2(bz);
        Plot2d.plot2(bz1);
        Plot2d.plot2(bz2);

        Plot2d.plotXLine(2.4, bz.stream().map(Point2::getX).mapToDouble(Double::doubleValue).max().orElse(0),
                bz.stream().map(Point2::getX).mapToDouble(Double::doubleValue).min().orElse(0), Plot2d.GREY_DASH);

        Plot2d.info("s/m", "B/T", "bipole field", 18);

        Plot2d.legend(18, "total field", "inner layer", "outer layer");

        Plot2d.showThread();
//        Plot3d.showThread();
    }

    @Run(-2)
    public void 磁场分布_tiltAngle0() {
        Trajectory trajectory = TrajectoryFactory.setStartingPoint(900 * MM, -1)
                .setStraightLine(1, Vector2.yDirect())
                .addArcLine(900 * MM, false, 50)
                .addStraitLine(1);

        BaseUtils.Switcher<String> switcher = CctUtils.createPlotDescribeSwitcher();
        List<String> des = BaseUtils.Python.linspaceStream(25, 35, switcher.getSize())
                .sequential()
                .mapToObj(t -> {
                    tiltAngle0 = t;
                    SoleLayerCct in = in内层CCT();
                    SoleLayerCct out = out外层CCT();

                    Elements elements = Elements.of(in, out);

                    return BaseUtils.Content.BiContent.create(t, elements);
                })
                .collect(Collectors.toList())
                .stream()
                .parallel()
                .map(bi -> {
                    Elements e = bi.getT2();
                    Double t = bi.getT1();

                    List<Point2> bz = e.magnetBzAlongTrajectory(trajectory);

                    return BaseUtils.Content.BiContent.create(t, bz);
                })
                .collect(Collectors.toList())
                .stream()
                .sequential()
                .sorted(Comparator.comparingDouble(BaseUtils.Content.BiContent::getT1))
                .map(bi -> {
                    Plot2d.plot2(bi.getT2(), switcher.getAndSwitch());

                    return bi.getT1();
                })
                .map(t -> "tilt = " + t)
                .collect(Collectors.toList());


        Plot2d.info("s/m", "B/T", "B vs tilt", 18);

        Plot2d.legend(18, des);

        Plot2d.showThread();
    }

    @Run(-3)
    public void opera_in() throws IOException {
        final double bigR = 900 * MM;
        final double smallR = 150 * MM;
        final double bendingAngle = 50;
        final double bendingRadian = BaseUtils.Converter.angleToRadian(bendingAngle);
        final double tiltAngle = tiltAngle0; // 待定
        final double tiltRadian = BaseUtils.Converter.angleToRadian(tiltAngle);
        final int windingNumber = 159;
        final double endKsi = windingNumber * Math.PI * 2;
        final double I0 = 922 * 6;


        final double a = Math.sqrt(bigR * bigR - smallR * smallR);

        final double eta = 0.5 * Math.log((bigR + a) / (bigR - a));
        final double eta_1 = FastMath.asinh(Math.sqrt((bigR * bigR) / (smallR * smallR) - 1));
        Logger.getLogger().info("eta = " + eta); // eta = 2.4778887302884756
        Logger.getLogger().info("eta_1 = " + eta_1); // eta_1 = 2.477888730288475

        final double C1 = (1 / Math.tan(tiltRadian)) / (1 * Math.sinh(eta));


        final double phi0 = bendingRadian / windingNumber;

        MathFunction path2MF = ksi -> C1 * Math.sin(1 * ksi) + phi0 * ksi / (2 * Math.PI);
        Point2Function path2P2F = Point2Function.create(MathFunction.identity(), path2MF);


        BipolarToroidalCoordinateSystemPoint2To3 bipolarToroidalCoordinateSystemPoint2To3 =
                new BipolarToroidalCoordinateSystemPoint2To3(a, eta);

        Point3Function path3P3F = bipolarToroidalCoordinateSystemPoint2To3.convert(path2P2F);

        OperaCct operaCct = new OperaCct(
                path3P3F, path2P2F,
                bipolarToroidalCoordinateSystemPoint2To3,
                2 * MM, 8 * MM,
                I0 / (2 * MM * 8 * MM),
                0,
                endKsi,
                360 * windingNumber
        );


        Brick8s brick8s = operaCct.createBrick8s();

//        brick8s.plot3(Plot2d.BLACK_SMALL_POINT);
//
//        Plot3d.plot(innerDipoleCctPath3d, 0, 1 * 2 * PI, PI / 20, Plot2d.RED_LINE);
//
//        Plot3d.showThread();

        FileOutputStream fileOutputStream = new FileOutputStream("LBNL_CCT_IN.txt");
        fileOutputStream.write(brick8s.toString().getBytes());
        fileOutputStream.flush();
        fileOutputStream.close();
    }

    @Run(-4)
    public void opera_out() throws IOException {
        final double bigR = 900 * MM;
        final double smallR = 160 * MM;
        final double bendingAngle = 50;
        final double bendingRadian = BaseUtils.Converter.angleToRadian(bendingAngle);
        final double tiltAngle = -tiltAngle0; // 待定
        final double tiltRadian = BaseUtils.Converter.angleToRadian(tiltAngle);
        final int windingNumber = 159;
        final double endKsi = windingNumber * Math.PI * 2;
        final double I0 = -922 * 6;


        final double a = Math.sqrt(bigR * bigR - smallR * smallR);

        final double eta = 0.5 * Math.log((bigR + a) / (bigR - a));
        final double eta_1 = FastMath.asinh(Math.sqrt((bigR * bigR) / (smallR * smallR) - 1));
        Logger.getLogger().info("eta = " + eta); // eta = 2.4778887302884756
        Logger.getLogger().info("eta_1 = " + eta_1); // eta_1 = 2.477888730288475

        final double C1 = (1 / Math.tan(tiltRadian)) / (1 * Math.sinh(eta));


        final double phi0 = bendingRadian / windingNumber;

        MathFunction path2MF = ksi -> C1 * Math.sin(1 * ksi) + phi0 * ksi / (2 * Math.PI);
        Point2Function path2P2F = Point2Function.create(MathFunction.identity(), path2MF);


        BipolarToroidalCoordinateSystemPoint2To3 bipolarToroidalCoordinateSystemPoint2To3 =
                new BipolarToroidalCoordinateSystemPoint2To3(a, eta);

        Point3Function path3P3F = bipolarToroidalCoordinateSystemPoint2To3.convert(path2P2F);

        OperaCct operaCct = new OperaCct(
                path3P3F, path2P2F,
                bipolarToroidalCoordinateSystemPoint2To3,
                2 * MM, 8 * MM,
                I0 / (2 * MM * 8 * MM),
                0,
                endKsi,
                360 * windingNumber
        );


        Brick8s brick8s = operaCct.createBrick8s();

//        brick8s.plot3(Plot2d.BLACK_SMALL_POINT);
//
//        Plot3d.plot(innerDipoleCctPath3d, 0, 1 * 2 * PI, PI / 20, Plot2d.RED_LINE);
//
//        Plot3d.showThread();

        FileOutputStream fileOutputStream = new FileOutputStream("LBNL_CCT_OUT.txt");
        fileOutputStream.write(brick8s.toString().getBytes());
        fileOutputStream.flush();
        fileOutputStream.close();
    }


    @Run(-10)
    public void opera_in李纯漪() throws IOException {
        final double bigR = 900 * MM;
        final double smallR = 150 * MM;
//        final double bendingAngle = 50;
//        final double bendingRadian = BaseUtils.Converter.angleToRadian(bendingAngle);
        final double tiltAngle = tiltAngle0; // 待定
        final double tiltRadian = BaseUtils.Converter.angleToRadian(tiltAngle);
        final int windingNumber = 159;
        final double endKsi = windingNumber * Math.PI * 2;
        final double I0 = 922 * 6;


        final double a = Math.sqrt(bigR * bigR - smallR * smallR);

        final double eta = 0.5 * Math.log((bigR + a) / (bigR - a));
        final double eta_1 = FastMath.asinh(Math.sqrt((bigR * bigR) / (smallR * smallR) - 1));
        Logger.getLogger().info("eta = " + eta); // eta = 2.4778887302884756
        Logger.getLogger().info("eta_1 = " + eta_1); // eta_1 = 2.477888730288475

        final double C1 = (1 / Math.tan(tiltRadian)) / (1 * Math.sinh(eta));


        final double phi0 = 0.00497;

        MathFunction path2MF = ksi -> C1 * Math.sin(1 * ksi) + phi0 * ksi / (2 * Math.PI);
        Point2Function path2P2F = Point2Function.create(MathFunction.identity(), path2MF);


        BipolarToroidalCoordinateSystemPoint2To3 bipolarToroidalCoordinateSystemPoint2To3 =
                new BipolarToroidalCoordinateSystemPoint2To3(a, eta);

        Point3Function path3P3F = bipolarToroidalCoordinateSystemPoint2To3.convert(path2P2F);

        OperaCct operaCct = new OperaCct(
                path3P3F, path2P2F,
                bipolarToroidalCoordinateSystemPoint2To3,
                2 * MM, 8 * MM,
                I0 / (2 * MM * 8 * MM),
                0,
                endKsi,
                10 * windingNumber
        );


        Brick8s brick8s = operaCct.createBrick8s();

//        brick8s.plot3(Plot2d.BLACK_SMALL_POINT);
//
//        Plot3d.plot(innerDipoleCctPath3d, 0, 1 * 2 * PI, PI / 20, Plot2d.RED_LINE);
//
//        Plot3d.showThread();

        FileOutputStream fileOutputStream = new FileOutputStream("LBNL_CCT_IN_LICHUNYI.txt");
        fileOutputStream.write(brick8s.toString().getBytes());
        fileOutputStream.flush();
        fileOutputStream.close();
    }

    @Run(-11)
    public void opera_out李纯漪() throws IOException {
        final double bigR = 900 * MM;
        final double smallR = 160 * MM;
//        final double bendingAngle = 50;
//        final double bendingRadian = BaseUtils.Converter.angleToRadian(bendingAngle);
        final double tiltAngle = -tiltAngle0; // 待定
        final double tiltRadian = BaseUtils.Converter.angleToRadian(tiltAngle);
        final int windingNumber = 159;
        final double endKsi = windingNumber * Math.PI * 2;
        final double I0 = -922 * 6;


        final double a = Math.sqrt(bigR * bigR - smallR * smallR);

        final double eta = 0.5 * Math.log((bigR + a) / (bigR - a));
        final double eta_1 = FastMath.asinh(Math.sqrt((bigR * bigR) / (smallR * smallR) - 1));
        Logger.getLogger().info("eta = " + eta); // eta = 2.4778887302884756
        Logger.getLogger().info("eta_1 = " + eta_1); // eta_1 = 2.477888730288475

        final double C1 = (1 / Math.tan(tiltRadian)) / (1 * Math.sinh(eta));


        final double phi0 = 0.00497;

        MathFunction path2MF = ksi -> C1 * Math.sin(1 * ksi) + phi0 * ksi / (2 * Math.PI);
        Point2Function path2P2F = Point2Function.create(MathFunction.identity(), path2MF);


        BipolarToroidalCoordinateSystemPoint2To3 bipolarToroidalCoordinateSystemPoint2To3 =
                new BipolarToroidalCoordinateSystemPoint2To3(a, eta);

        Point3Function path3P3F = bipolarToroidalCoordinateSystemPoint2To3.convert(path2P2F);

        OperaCct operaCct = new OperaCct(
                path3P3F, path2P2F,
                bipolarToroidalCoordinateSystemPoint2To3,
                2 * MM, 8 * MM,
                I0 / (2 * MM * 8 * MM),
                0,
                endKsi,
                10 * windingNumber
        );


        Brick8s brick8s = operaCct.createBrick8s();

//        brick8s.plot3(Plot2d.BLACK_SMALL_POINT);
//
//        Plot3d.plot(innerDipoleCctPath3d, 0, 1 * 2 * PI, PI / 20, Plot2d.RED_LINE);
//
//        Plot3d.showThread();

        FileOutputStream fileOutputStream = new FileOutputStream("LBNL_CCT_OUT_LICHUNYI.txt");
        fileOutputStream.write(brick8s.toString().getBytes());
        fileOutputStream.flush();
        fileOutputStream.close();
    }

    @Run(5)
    public void 画图() {
        in内层CCT().plot3(Plot2d.RED_LINE);
        out外层CCT().plot3(Plot2d.BLUE_LINE);

        Plot3d.showThread();
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
