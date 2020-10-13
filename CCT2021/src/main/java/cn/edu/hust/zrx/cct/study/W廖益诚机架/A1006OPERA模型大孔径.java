package cn.edu.hust.zrx.cct.study.W廖益诚机架;

import cn.edu.hust.zrx.cct.Logger;
import cn.edu.hust.zrx.cct.advanced.Opera.Brick8s;
import cn.edu.hust.zrx.cct.advanced.Opera.Brick8sSet;
import cn.edu.hust.zrx.cct.advanced.Opera.OperaCct;
import cn.edu.hust.zrx.cct.advanced.combined.GantryAnalysor;
import cn.edu.hust.zrx.cct.advanced.combined.GantryData;
import cn.edu.hust.zrx.cct.base.BaseUtils;
import cn.edu.hust.zrx.cct.base.annotation.Run;
import cn.edu.hust.zrx.cct.base.cct.Cct;
import cn.edu.hust.zrx.cct.base.cct.EntitySoleLayerCct;
import cn.edu.hust.zrx.cct.base.cct.MagnetAble;
import cn.edu.hust.zrx.cct.base.cct.SoleLayerCct;
import cn.edu.hust.zrx.cct.base.line.Line2;
import cn.edu.hust.zrx.cct.base.line.Trajectory;
import cn.edu.hust.zrx.cct.base.line.TrajectoryFactory;
import cn.edu.hust.zrx.cct.base.point.Point2;
import cn.edu.hust.zrx.cct.base.point.Point2Function;
import cn.edu.hust.zrx.cct.base.point.Point2To3;
import cn.edu.hust.zrx.cct.base.point.Point3Function;
import cn.edu.hust.zrx.cct.base.python.Plot2d;
import cn.edu.hust.zrx.cct.base.python.Plot3d;
import cn.edu.hust.zrx.cct.base.vector.Vector2;

import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import static cn.edu.hust.zrx.cct.base.BaseUtils.Constant.MM;
import static java.lang.Math.PI;
import static java.lang.Math.atan;

/**
 * Description
 * A1006OPERA模型大孔径
 * <p>
 * Data
 * 15:54
 *
 * @author zrx
 * @version 1.0
 */

public class A1006OPERA模型大孔径 {

    public final int numberPerWinding = 30;

    private GantryData.SecondPart secondPart() {
        GantryData.SecondPart secondPart = GantryAnalysor.defaultSecondPart();

        secondPart.agCct345SmallRInner = 69 * MM + 30 * MM; // +30 mm
        secondPart.agCct345SmallROuter = 79 * MM + 30 * MM;
        secondPart.dipoleCct345SmallRInner = 89 * MM + 30 * MM;
        secondPart.dipoleCct345SmallROuter = 99 * MM + 30 * MM;

        secondPart.dipoleCct345A0Inner = -secondPart.dipoleCct345SmallRInner * Math.sqrt(3) / secondPart.dipoleCct345BigR; // 2020年9月4日 调整后倾斜角为30度整
        secondPart.dipoleCct345A0Outer = secondPart.dipoleCct345SmallROuter * Math.sqrt(3) / secondPart.dipoleCct345BigR;
        secondPart.dipoleCct345A1Inner = Math.pow(secondPart.dipoleCct345SmallRInner, 2) * 0.25;
        secondPart.dipoleCct345A1Outer = -Math.pow(secondPart.dipoleCct345SmallROuter, 2) * 0.25;
        secondPart.dipoleCct345A2Inner = 0.0;
        secondPart.dipoleCct345A2Outer = 0.0;

        secondPart.agCct345IInner = 6500; // from 8171
        secondPart.agCct345IOuter = 6500; // from 8171

        //        public double agCct345A0Inner = 0.;
        //        public double agCct345A0Outer = 0.;
        //        public double agCct345A1Inner = -Math.pow(agCct345SmallRInner, 2) * Math.sqrt(3) * 20 * 0.19;
        //        public double agCct345A1Outer = Math.pow(agCct345SmallROuter, 2) * Math.sqrt(3) * 20 * 0.19;
        //        public double agCct345A2Inner = 0.;
        //        public double agCct345A2Outer = 0.;

        secondPart.agCct345A1Inner = -Math.pow(secondPart.agCct345SmallRInner, 2) * Math.sqrt(3) * 20 * 0.19 / 6500 * 8171 / 11.45 * 18.60;
        secondPart.agCct345A1Outer = Math.pow(secondPart.agCct345SmallROuter, 2) * Math.sqrt(3) * 20 * 0.19 / 6500 * 8171 / 11.45 * 18.60;

        Logger.getLogger().info("secondPart.agCct345A1Inner = " + secondPart.agCct345A1Inner);

        Logger.getLogger().info("secondPart.agCct345A1Outer = " + secondPart.agCct345A1Outer);


        double innerTiltTan = secondPart.agCct345SmallRInner / (2 * secondPart.agCct345BigR * secondPart.agCct345A1Inner);
        double outerTiltTan = secondPart.agCct345SmallROuter / (2 * secondPart.agCct345BigR * secondPart.agCct345A1Outer);

        Logger.getLogger().info("innerTilt = " + innerTiltTan);

        Logger.getLogger().info("outerTilt = " + outerTiltTan);

        double innerTilt = atan(innerTiltTan);
        Logger.getLogger().info("Math.atan(innerTiltTan) = " + innerTilt);
        double outerTilt = atan(outerTiltTan);
        Logger.getLogger().info("atan(outerTiltTan) = " + outerTilt);


        Logger.getLogger().info("BaseUtils.Converter.radianToAngle(innerTilt) = " + BaseUtils.Converter.radianToAngle(innerTilt));

        Logger.getLogger().info("BaseUtils.Converter.radianToAngle(outerTilt) = " + BaseUtils.Converter.radianToAngle(outerTilt));

        return secondPart;
    }

    @Run
    public void test() {
        GantryData.SecondPart secondPart = secondPart();
        Point3Function innerDipoleCctPath3d = secondPart.innerDipoleCctPath3d();
        Point3Function outerDipoleCctPath3d = secondPart.outerDipoleCctPath3d();

        Plot3d.plot(innerDipoleCctPath3d, 0, 201 * 2 * PI, PI / 20, Plot2d.RED_LINE);
        Plot3d.plot(outerDipoleCctPath3d, 0, 201 * 2 * PI, PI / 20, Plot2d.BLUE_LINE);

        Plot3d.showThread();
    }


    @Run(2) // 2020年10月7日 人工验证无误
    public void opera_dipole345Inner() throws IOException {
        GantryData.SecondPart secondPart = secondPart();

        Point3Function innerDipoleCctPath3d = secondPart.innerDipoleCctPath3d();
        Point3Function outerDipoleCctPath3d = secondPart.outerDipoleCctPath3d();

        Point2Function innerDipoleCctPath2d = secondPart.innerDipoleCctPath2d().convertToPoint2Function();
        Point2Function outerDipoleCctPath2d = secondPart.outerDipoleCctPath2d().convertToPoint2Function();

        OperaCct operaCct = new OperaCct(
                innerDipoleCctPath3d, innerDipoleCctPath2d,
                Point2To3.SimpleToroidalCoordinateSystemPoint2To3.create(secondPart.dipoleCct345BigR, secondPart.dipoleCct345SmallRInner),
                2 * MM, 8 * MM,
                secondPart.dipoleCct345IInner / (2 * MM * 8 * MM),
                0,
                2 * PI * secondPart.dipoleCct345WindingNumber,
                numberPerWinding * secondPart.dipoleCct345WindingNumber
        );

        Brick8s brick8s = operaCct.createBrick8s();

        FileOutputStream fileOutputStream = new FileOutputStream("opera_dipole345Inner.txt");
        fileOutputStream.write(brick8s.toString().getBytes());
        fileOutputStream.flush();
        fileOutputStream.close();
    }

    @Run(3) // 2020年10月7日 人工验证无误
    public void opera_dipole345Outer() throws IOException {
        GantryData.SecondPart secondPart = secondPart();

        Point3Function innerDipoleCctPath3d = secondPart.innerDipoleCctPath3d();
        Point3Function outerDipoleCctPath3d = secondPart.outerDipoleCctPath3d();

        Point2Function innerDipoleCctPath2d = secondPart.innerDipoleCctPath2d().convertToPoint2Function();
        Point2Function outerDipoleCctPath2d = secondPart.outerDipoleCctPath2d().convertToPoint2Function();

        OperaCct operaCct = new OperaCct(
                outerDipoleCctPath3d, outerDipoleCctPath2d,
                Point2To3.SimpleToroidalCoordinateSystemPoint2To3.create(secondPart.dipoleCct345BigR, secondPart.dipoleCct345SmallROuter),
                2 * MM, 8 * MM,
                secondPart.dipoleCct345IOuter / (2 * MM * 8 * MM),
                0,
                2 * PI * secondPart.dipoleCct345WindingNumber,
                numberPerWinding * secondPart.dipoleCct345WindingNumber
        );

        Brick8s brick8s = operaCct.createBrick8s();

//        brick8s.plot3(Plot2d.BLACK_SMALL_POINT);
//
//        Plot3d.plot(innerDipoleCctPath3d, 0, 1 * 2 * PI, PI / 20, Plot2d.RED_LINE);
//
//        Plot3d.showThread();

        FileOutputStream fileOutputStream = new FileOutputStream("opera_dipole345Outer.txt");
        fileOutputStream.write(brick8s.toString().getBytes());
        fileOutputStream.flush();
        fileOutputStream.close();
    }

    @Run(4)
    public void opera_agCctInner() throws IOException {
        GantryData.SecondPart secondPart = secondPart();

        List<EntitySoleLayerCct> entitySoleLayerCcts = secondPart.innerAgCctEntitySoleLayerCcts(
                2 * MM, 8 * MM, numberPerWinding);

//        BaseUtils.Switcher<String> stringSwitcher = CctUtils.createPlotDescribeSwitcher();
//        stringSwitcher.getAndSwitch();

        Brick8sSet brick8sSet = Brick8sSet.empty();

        entitySoleLayerCcts.stream()
                .peek(e -> e.setCurrentDensity(-e.getCurrentDensity())) // 不知道为什么反一下电流 TODO
                .map(OperaCct::create).map(OperaCct::createBrick8s).forEach(brick8sSet::add);


        FileOutputStream fileOutputStream = new FileOutputStream("opera_agCctInner.txt");
        fileOutputStream.write(brick8sSet.toString().getBytes());
        fileOutputStream.flush();
        fileOutputStream.close();

//        entitySoleLayerCcts.forEach(e->{
//            Logger.getLogger().info("e = " + e);
//
//            Point3Function centerCctPath3d = e.getCenterCctPath3d();
//
//            Point2Function centerCctPath2d = e.centerCctPath2d;
//
//            double startTheta = e.getStartTheta();
//
//            double endTheta = e.getEndTheta();
//
//            //Plot3d.plot3(BaseUtils.Python.linspaceFunction(centerCctPath3d,startTheta,endTheta,10000),stringSwitcher.getAndSwitch());
//
//            Plot2d.plot2(BaseUtils.Python.linspaceFunction(centerCctPath2d,startTheta,endTheta,10000),stringSwitcher.getAndSwitch());
//        });
//
//
//        Plot2d.showThread();
    }

    @Run(5)
    public void opera_agCctOuter() throws IOException {
        GantryData.SecondPart secondPart = secondPart();

        List<EntitySoleLayerCct> entitySoleLayerCcts = secondPart.outerAgCctEntitySoleLayerCcts(
                2 * MM, 8 * MM, numberPerWinding);

//        BaseUtils.Switcher<String> stringSwitcher = CctUtils.createPlotDescribeSwitcher();
//        stringSwitcher.getAndSwitch();

        Brick8sSet brick8sSet = Brick8sSet.empty();

        entitySoleLayerCcts.stream()
                .peek(e -> e.setCurrentDensity(-e.getCurrentDensity())) // 不知道为什么反一下电流
                .map(OperaCct::create).map(OperaCct::createBrick8s).forEach(brick8sSet::add);


        FileOutputStream fileOutputStream = new FileOutputStream("opera_agCctOuter.txt");
        fileOutputStream.write(brick8sSet.toString().getBytes());
        fileOutputStream.flush();
        fileOutputStream.close();
    }


    @Run(-100)
    public void 磁场() {
        GantryData.SecondPart secondPart = secondPart();


        GantryData.FirstPart firstPart = GantryAnalysor.defaultFirstPart();

        Line2 trajectoryFirstPart = GantryAnalysor.getTrajectoryFirstPart(firstPart);

        Line2 trajectorySecondPart = GantryAnalysor.getTrajectorySecondPart(trajectoryFirstPart.pointAtEnd(), trajectoryFirstPart.directAtEnd(), secondPart);


        MagnetAble secondPartMagnetAble = GantryAnalysor.secondPartMagnetAble(trajectoryFirstPart.pointAtEnd(), trajectoryFirstPart.directAtEnd(), secondPart);


        Trajectory trajectory = TrajectoryFactory.setStartingPoint(
                trajectorySecondPart.pointAt(secondPart.DL2 + secondPart.CCT345_LENGTH / 2)
        ).setStraightLine(0.95, trajectorySecondPart.directAt(secondPart.DL2 + secondPart.CCT345_LENGTH / 2).rotateSelf(PI / 2));


        List<Point2> b = secondPartMagnetAble.magnetBzAlongTrajectory(trajectory.resetLength(0.1), 0.01 * MM);

//        trajectorySecondPart.plot3d();
//
//        trajectory.plot3d();
//
//        Plot3d.showThread();

        Plot2d.plot2(b);


        Plot2d.showThread();
    }

    @Run(-101)
    public void 二极磁场验证() {
        GantryData.SecondPart secondPart = secondPart();

        Point3Function innerDipoleCctPath3d = secondPart.innerDipoleCctPath3d();
        Point3Function outerDipoleCctPath3d = secondPart.outerDipoleCctPath3d();

        Point2Function innerDipoleCctPath2d = secondPart.innerDipoleCctPath2d().convertToPoint2Function();
        Point2Function outerDipoleCctPath2d = secondPart.outerDipoleCctPath2d().convertToPoint2Function();

        OperaCct operaCct = new OperaCct(
                innerDipoleCctPath3d, innerDipoleCctPath2d,
                Point2To3.SimpleToroidalCoordinateSystemPoint2To3.create(secondPart.dipoleCct345BigR, secondPart.dipoleCct345SmallRInner),
                2 * MM, 8 * MM,
                secondPart.dipoleCct345IInner / (2 * MM * 8 * MM),
                0,
                2 * PI * secondPart.dipoleCct345WindingNumber,
                180 * secondPart.dipoleCct345WindingNumber
        );

        SoleLayerCct inner = SoleLayerCct.create(innerDipoleCctPath3d.disperse(0,
                2 * PI * secondPart.dipoleCct345WindingNumber,
                180 * secondPart.dipoleCct345WindingNumber), secondPart.dipoleCct345IInner);

        SoleLayerCct outer = SoleLayerCct.create(outerDipoleCctPath3d.disperse(0,
                2 * PI * secondPart.dipoleCct345WindingNumber,
                180 * secondPart.dipoleCct345WindingNumber), secondPart.dipoleCct345IOuter);


        /////////////////////////////////////////////////////////////////////////////

        GantryData.FirstPart firstPart = GantryAnalysor.defaultFirstPart();

        Line2 trajectoryFirstPart = GantryAnalysor.getTrajectoryFirstPart(firstPart);

        Line2 trajectorySecondPart = GantryAnalysor.getTrajectorySecondPart(trajectoryFirstPart.pointAtEnd(),
                trajectoryFirstPart.directAtEnd(), secondPart);


        MagnetAble secondPartMagnetAble = GantryAnalysor.secondPartMagnetAble(trajectoryFirstPart.pointAtEnd(),
                trajectoryFirstPart.directAtEnd(), secondPart);


        Trajectory trajectory = TrajectoryFactory.setStartingPoint(Point2.create(0.95, 0))
                .setStraightLine(1e-3, Vector2.yDirect())
                .addArcLine(0.95, false, 67.5);

        List<Point2> in = inner.magnetBzAlongTrajectory(trajectory);
        List<Point2> ou = outer.magnetBzAlongTrajectory(trajectory);

        Plot2d.plot2(in);
        Plot2d.plot2(ou);

        List<Point2> all = Point2.addOnY(in, ou);

        Plot2d.plot2(all);

        Plot2d.showThread();

    }

    @Run(-102)
    public void 四极磁场验证() {
        GantryData.SecondPart secondPart = secondPart();

        List<EntitySoleLayerCct> entitySoleLayerCcts = secondPart.innerAgCctEntitySoleLayerCcts(
                2 * MM, 8 * MM, 180);

        List<EntitySoleLayerCct> entitySoleLayerCcts2 = secondPart.outerAgCctEntitySoleLayerCcts(
                2 * MM, 8 * MM, 180);

        Cct innerCct = Cct.getEmptyCct();

        entitySoleLayerCcts.forEach(entitySoleLayerCct -> {
            Point3Function centerCctPath3d = entitySoleLayerCct.getCenterCctPath3d();

            double startTheta = entitySoleLayerCct.getStartTheta();

            double endTheta = entitySoleLayerCct.getEndTheta();

            double currentDensity = entitySoleLayerCct.getCurrentDensity();

            double width = entitySoleLayerCct.getWidth();

            double depth = entitySoleLayerCct.getDepth();

            double i = -currentDensity * width * depth;

            int windNumber = entitySoleLayerCct.getWindNumber();

            SoleLayerCct soleLayerCct = SoleLayerCct.create(BaseUtils.Python.linspaceFunction(centerCctPath3d, startTheta, endTheta, windNumber * 360), i);

            innerCct.addSoleLayerCct(soleLayerCct);

        });

        entitySoleLayerCcts2.forEach(entitySoleLayerCct -> {
            Point3Function centerCctPath3d = entitySoleLayerCct.getCenterCctPath3d();

            double startTheta = entitySoleLayerCct.getStartTheta();

            double endTheta = entitySoleLayerCct.getEndTheta();

            double currentDensity = entitySoleLayerCct.getCurrentDensity();

            double width = entitySoleLayerCct.getWidth();

            double depth = entitySoleLayerCct.getDepth();

            double i = -currentDensity * width * depth;

            int windNumber = entitySoleLayerCct.getWindNumber();

            SoleLayerCct soleLayerCct = SoleLayerCct.create(BaseUtils.Python.linspaceFunction(centerCctPath3d, startTheta, endTheta, windNumber * 360), i);

            innerCct.addSoleLayerCct(soleLayerCct);

        });

        /////////////////////////////////////////////////////////////////////////////

        GantryData.FirstPart firstPart = GantryAnalysor.defaultFirstPart();

        Line2 trajectoryFirstPart = GantryAnalysor.getTrajectoryFirstPart(firstPart);

        Line2 trajectorySecondPart = GantryAnalysor.getTrajectorySecondPart(trajectoryFirstPart.pointAtEnd(), trajectoryFirstPart.directAtEnd(), secondPart);


        MagnetAble secondPartMagnetAble = GantryAnalysor.secondPartMagnetAble(trajectoryFirstPart.pointAtEnd(), trajectoryFirstPart.directAtEnd(), secondPart);


        Trajectory trajectory = TrajectoryFactory.setStartingPoint(Point2.create(0.95, -1))
                .setStraightLine(1, Vector2.yDirect())
                .addArcLine(0.95, false, 67.5)
                .addStraitLine(1);

        List<Point2> in = innerCct.magnetGradientAlongTrajectoryFast(trajectory, 10*MM, 10 * MM);

        Plot2d.plot2(in);


        Plot2d.plot2(List.of(
                Point2.create(-20, 0),
                Point2.create(0, 0),
                Point2.create(0, 17.466),
                Point2.create(8 + 4.386, 17.466),
                Point2.create(8 + 4.386, -17.466),
                Point2.create(8 + 4.386 + 8 + 19.344, -17.466),
                Point2.create(8 + 4.386 + 8 + 19.344, 17.466),
                Point2.create(8 + 4.386 + 8 + 19.344 + 8 + 19.770, 17.466),
                Point2.create(8 + 4.386 + 8 + 19.344 + 8 + 19.770, 0),
                Point2.create(67.5 + 20, 0)
        ), Plot2d.BLUE_DASH);


        Plot2d.showThread();

    }

    public static void main(String[] args) throws Exception {
        A1006OPERA模型大孔径 a = new A1006OPERA模型大孔径();
        a.opera_agCctInner();
        a.opera_agCctOuter();
        a.opera_dipole345Inner();
        a.opera_dipole345Outer();

        if(false){
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
}
