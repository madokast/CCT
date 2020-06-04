package cn.edu.hust.zrx.cct.study.验证性研究;

import cn.edu.hust.zrx.cct.Logger;
import cn.edu.hust.zrx.cct.advanced.CosyArbitraryOrder;
import cn.edu.hust.zrx.cct.base.BaseUtils;
import cn.edu.hust.zrx.cct.base.cct.CctFactory;
import cn.edu.hust.zrx.cct.base.line.Trajectory;
import cn.edu.hust.zrx.cct.base.line.TrajectoryFactory;
import cn.edu.hust.zrx.cct.base.opticsMagnet.NoMagnet;
import cn.edu.hust.zrx.cct.base.opticsMagnet.bendingMagnet.HardBendingMagnet;
import cn.edu.hust.zrx.cct.base.particle.*;
import cn.edu.hust.zrx.cct.base.point.Point2;
import cn.edu.hust.zrx.cct.base.point.Point3;
import cn.edu.hust.zrx.cct.base.python.Plot2d;
import cn.edu.hust.zrx.cct.base.opticsMagnet.qs.QsHardPlaneMagnet;
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
import java.util.stream.Collectors;

/**
 * Description
 * E20200512COSY语法学习
 * <p>
 * Data
 * 10:49
 *
 * @author zrx
 * @version 1.0
 */

public class E20200512COSY语法学习 {


    @run(1)
    public void 纯四极铁() {
        final double LENGTH = 0.2; // M

        final double APERTURE = 30 * MM; // 孔径 半径
        final double B_POLE = 3;// T

        final double T = B_POLE / APERTURE; // 梯度 100 T

        Logger.getLogger().info("T = " + T);

        QsHardPlaneMagnet Q = QsHardPlaneMagnet.create(LENGTH, T, 0,

                APERTURE / MM, Point2.origin(), Vector2.yDirect());

        Trajectory trajectory = TrajectoryFactory.setStartingPoint(0, -1).setStraightLine(2 + LENGTH, Vector2.yDirect());


        List<Point2> gradientAlongTrajectoryFast = Q.magnetGradientAlongTrajectoryFast(trajectory, MM, MM);

        Plot2d.plot2(gradientAlongTrajectoryFast);

        Plot2d.info("s/m", "G/(T/m)", "四极场", 18);

        Plot2d.showThread();

//        trajectory.plot3d();
//
//        Q.plot3d(Plot2d.BLACK_LINE);
//
//        Plot3d.setCube(1.3);
//
//        Plot3d.showThread();

    }

    @run(2)
    public void 纯四极场相椭圆对比() {
        final double LENGTH = 0.2; // M

        final double APERTURE = 30 * MM; // 孔径 半径
        final double B_POLE = 3;// T

        final double T = B_POLE / APERTURE; // 梯度 100 T

        Logger.getLogger().info("T = " + T);

        QsHardPlaneMagnet Q = QsHardPlaneMagnet.create(LENGTH, T, 0,
                APERTURE / MM, Point2.origin(), Vector2.yDirect());

        Trajectory trajectory = TrajectoryFactory.setStartingPoint(0, -1).setStraightLine(2 + LENGTH, Vector2.yDirect());

        CosyArbitraryOrder.CosyMapArbitraryOrder cosyMap纯四极场 = cosyMap纯四极场();

        CosyArbitraryOrder.CosyMapArbitraryOrder cosyMap纯四机场切片 = cosyMap纯四机场切片();

        List<Point2> cosy相椭圆 = cosy相椭圆(false, 0.05, 512, 5, false, 1, cosyMap纯四极场);
        List<Point2> cosy相椭圆切片 = cosy相椭圆(false, 0.05, 512, 5, false, 1, cosyMap纯四机场切片);

        List<Point2> tracking相椭圆 = tracking相椭圆(trajectory.getLength(), false, 0.05, 16, false, 1, Q, trajectory);

        Plot2d.plot2(cosy相椭圆, Plot2d.RED_LINE);

        Plot2d.plot2(cosy相椭圆切片, Plot2d.BLUE_LINE);

        Plot2d.plot2(tracking相椭圆, Plot2d.BLACK_POINT);

        Plot2d.info("y/mm", "yp/mrad", "纯四极场 y/yp dp=5%", 18);

        Plot2d.legend(18, "cosy 5th", "cosy slice", "tracking");

        Plot2d.equal();

        Plot2d.showThread();
    }

    @run(3)
    public void 纯四极场切片() {
        final double LENGTH = 0.2; // M

        final double APERTURE = 30 * MM; // 孔径 半径
        final double B_POLE = 3;// T

        final double T = B_POLE / APERTURE; // 梯度 100 T

        Logger.getLogger().info("T = " + T);

        QsHardPlaneMagnet Q = QsHardPlaneMagnet.create(LENGTH, T, 0,
                APERTURE / MM, Point2.origin(), Vector2.yDirect());

        Trajectory trajectory = TrajectoryFactory.setStartingPoint(0, -1).setStraightLine(2 + LENGTH, Vector2.yDirect());


        List<String> list = Q.sliceToCosyScript(Bp, APERTURE, trajectory, 5 * MM, MM, 0.5);

        list.forEach(System.out::println);

        //M5 1.000455e+00 0.000000e+00 0.000000e+00 0 0 0 3.000000e-02 ;
        //M5 2.000910e-01 3.000000e+00 -8.884110e-16 0 0 0 3.000000e-02 ;
        //M5 9.994543e-01 0.000000e+00 0.000000e+00 0 0 0 3.000000e-02 ;

    }


    @run(4)
    public void 四六极场() {
        final double LENGTH = 0.2; // M

        final double APERTURE = 30 * MM; // 孔径 半径
        final double B_POLE_Quad = 0.3;// T
        final double B_POLE_Sext = 9;// T

        final double T = B_POLE_Quad / APERTURE; // 梯度 100 T

        Logger.getLogger().info("T = " + T);

        final double L = B_POLE_Sext / APERTURE / APERTURE; // 六极场梯度

        Logger.getLogger().info("L = " + L);

        QsHardPlaneMagnet Q = QsHardPlaneMagnet.create(LENGTH, T, L,
                APERTURE / MM, Point2.origin(), Vector2.yDirect());

        Trajectory trajectory = TrajectoryFactory.setStartingPoint(0, -1).setStraightLine(2 + LENGTH, Vector2.yDirect());

        CosyArbitraryOrder.CosyMapArbitraryOrder cosyMap四六极场场 = cosyMap四六极场场();

        List<Point2> cosy相椭圆 = cosy相椭圆(false, 0.05, 512, 5, false, 1, cosyMap四六极场场);

        //List<Point2> cosy相椭圆切片 = cosy相椭圆(false, 0.05, 512, 5, false, 1, cosyMap纯四机场切片);

        List<Point2> tracking相椭圆 = tracking相椭圆(trajectory.getLength(), false, 0.05, 16, false, 1, Q, trajectory);

        Plot2d.plot2(cosy相椭圆, Plot2d.RED_LINE);

        //Plot2d.plot2(cosy相椭圆切片, Plot2d.BLUE_LINE);

        Plot2d.plot2(tracking相椭圆, Plot2d.BLACK_POINT);

        Plot2d.info("y/mm", "yp/mrad", "纯四极场 y/yp dp=5%", 18);

        Plot2d.legend(18, "cosy 5th", "tracking");
        //Plot2d.legend(18, "cosy 5th", "cosy slice", "tracking");

        Plot2d.equal();

//        Plot2d.showThread();

        List<String> list = Q.sliceToCosyScript(Bp, APERTURE, trajectory, 5 * MM, MM, 0.5);

        list.forEach(System.out::println);
    }


    @run(101)
    public void 简单弯铁_左偏(){
        HardBendingMagnet bendingMagnet = HardBendingMagnet.create(Point2.origin(), Vector2.yDirect(),
                false, 90, 0.85, Bp, 0, 0);

        Trajectory trajectory = TrajectoryFactory.setStartingPoint(0, -1)
                .setStraightLine(1, Vector2.yDirect())
                .addArcLine(0.85, false, 90)
                .addStraitLine(1);




        if(false){ //test
            trajectory.dispersePoint3sWithDistance(MM).forEach(point3WithDistance -> {
                double distance = point3WithDistance.getDistance();
                Point3 p = point3WithDistance.getPoint3();

                boolean inTheMagnet = bendingMagnet.isInTheMagnet(p.toPoint2());

                Logger.getLogger().info("{}-{}",distance,inTheMagnet);
            });
        }


        if(false){ // 磁场分布
            List<Point2> magnetBzAlongTrajectory = bendingMagnet.magnetBzAlongTrajectory(trajectory);

            Plot2d.plot2(magnetBzAlongTrajectory);

            Plot2d.info("s/m","By/T","简单弯铁_左偏",18);

            Plot2d.showThread();
        }


        if(false){ //画图
            trajectory.plot2d(MM, Plot2d.BLACK_LINE);

            bendingMagnet.plot2(Plot2d.RED_LINE);

            Plot2d.info("x/m","y/m","简单弯铁_左偏",18);

            Plot2d.equal();

            Plot2d.showThread();
        }

        // 相椭圆
        if(true){
            boolean xPlane = false;

            double delta = 0.05;

            List<Point2> tracking相椭圆 = tracking相椭圆(trajectory.getLength(), xPlane,
                    delta, 16, false, 1, bendingMagnet, trajectory);

            CosyArbitraryOrder.CosyMapArbitraryOrder cosyMap左偏简单弯铁 = cosyMap左偏简单弯铁();

            CosyArbitraryOrder.CosyMapArbitraryOrder cosyMap左偏转弯铁切片 = cosyMap左偏转弯铁切片();

            List<Point2> cosy相椭圆 = cosy相椭圆(xPlane, delta, 512, 5,
                    false, 1, cosyMap左偏简单弯铁);


            List<Point2> cosy相椭圆切片 = cosy相椭圆(xPlane, delta, 512, 5,
                    false, 1, cosyMap左偏转弯铁切片);


            Plot2d.plot2(cosy相椭圆, Plot2d.RED_LINE);

            Plot2d.plot2(cosy相椭圆切片, Plot2d.BLUE_LINE);

            Plot2d.plot2(tracking相椭圆, Plot2d.BLACK_POINT);

            if(xPlane){
                Plot2d.info("x/mm", "xp/mrad", "纯弯铁 x/xp dp=5%", 18);
            }else {
                Plot2d.info("y/mm", "yp/mrad", "纯弯铁 y/yp dp=5%", 18);
            }

//            Plot2d.legend(18, "cosy 5th", "tracking");
            Plot2d.legend(18, "cosy 5th", "cosy slice", "tracking");

            Plot2d.equal();

            Plot2d.showThread();
        }

        // 束流轨迹
        if(false){

//            RunningParticle p = ParticleFactory.createIdealProtonAtTrajectory250MeV(trajectory);
//
//            List<Point3> run = ParticleRunner.run(p, bendingMagnet, trajectory.getLength(), MM);
//
//            Plot3d.plot3(run,Plot2d.RED_LINE);
//
//            trajectory.plot3d();
//
//            Plot3d.showThread();

//            tracking束流轨迹(bendingMagnet,trajectory,true,0,16,true, Plot2d.RED_LINE);


            RunningParticle ip = ParticleFactory.createIdealProtonAtTrajectory250MeV(trajectory);

            List<PhaseSpaceParticle> pp = PhaseSpaceParticles.phaseSpaceParticlesAlongPositiveEllipseInPlane(
                    true, 3.5 * MM, 7.5 * MM, 0, 16);

            List<RunningParticle> ps = ParticleFactory.createParticlesFromPhaseSpaceParticle(ip, ip.computeNaturalCoordinateSystem(), pp);

            ps.stream().parallel().forEach(p->{
                List<Point3> run = ParticleRunner.run(p, bendingMagnet, trajectory.getLength(), MM);

               Plot3d.plot3(run,Plot2d.RED_LINE);
            });

            Plot3d.showThread();
        }

        // 切片
        if(false){
            List<String> list = bendingMagnet.sliceToCosyScript(Bp, 30 * MM, trajectory, 5 * MM, MM, 0.1);
            list.forEach(System.out::println);
        }
    }

    @run(102)
    public void n1弯铁_右偏(){
        //前后1m漂移段，弯铁半径0.95m，偏转45度，右手侧偏移，孔径30mm，n1=10

        HardBendingMagnet bendingMagnet = HardBendingMagnet.create(Point2.origin(), Vector2.yDirect(),
                true, 45, 0.95, Bp, 10, 0);

        Trajectory trajectory = TrajectoryFactory.setStartingPoint(0, -1)
                .setStraightLine(1, Vector2.yDirect())
                .addArcLine(0.95, true, 45)
                .addStraitLine(1);



        //test
        if(false){
            trajectory.dispersePoint3sWithDistance(MM).forEach(point3WithDistance -> {
                double distance = point3WithDistance.getDistance();
                Point3 p = point3WithDistance.getPoint3();

                boolean inTheMagnet = bendingMagnet.isInTheMagnet(p.toPoint2());

                Logger.getLogger().info("{}-{}",distance,inTheMagnet);
            });
        }

        // 磁场分布
        if(false){
            List<Point2> magnetBzAlongTrajectory = bendingMagnet.magnetBzAlongTrajectory(trajectory);

            List<Point2> gradientAlongTrajectory = bendingMagnet.magnetGradientAlongTrajectory(trajectory, MM, 5 * MM);

            Plot2d.plot2(magnetBzAlongTrajectory,Plot2d.BLACK_LINE);
            Plot2d.plot2(gradientAlongTrajectory,Plot2d.RED_LINE);

            Plot2d.info("s/m","By/T & G/Tm-1","n1弯铁_右偏",18);

            Plot2d.legend(18,"By","G");

            Plot2d.showThread();
        }


        //画图
        if(false){ //画图
            trajectory.plot2d(MM, Plot2d.BLACK_LINE);

            bendingMagnet.plot2(Plot2d.RED_LINE);

            Plot2d.info("x/m","y/m","简单弯铁_左偏",18);

            Plot2d.equal();

            Plot2d.showThread();
        }

        // 相椭圆
        if(true){
            boolean xPlane = false;

            double delta = 0.05;

            List<Point2> tracking相椭圆 = tracking相椭圆(trajectory.getLength(), xPlane,
                    delta, 16, false, 1, bendingMagnet, trajectory);

            CosyArbitraryOrder.CosyMapArbitraryOrder cosyMap右偏n1弯铁 = cosyMap右偏n1弯铁();

            CosyArbitraryOrder.CosyMapArbitraryOrder cosyMap右偏n1弯铁切片 = cosyMap右偏n1弯铁切片();

            List<Point2> cosy相椭圆 = cosy相椭圆(xPlane, delta, 512, 5,
                    false, 1, cosyMap右偏n1弯铁);


            List<Point2> cosy相椭圆切片 = cosy相椭圆(xPlane, delta, 512, 5,
                    false, 1, cosyMap右偏n1弯铁切片);


            Plot2d.plot2(cosy相椭圆, Plot2d.RED_LINE);

            Plot2d.plot2(cosy相椭圆切片, Plot2d.BLUE_LINE);

            Plot2d.plot2(tracking相椭圆, Plot2d.BLACK_POINT);

            if(xPlane){
                Plot2d.info("x/mm", "xp/mrad", "纯弯铁 x/xp dp=5%", 18);
            }else {
                Plot2d.info("y/mm", "yp/mrad", "纯弯铁 y/yp dp=5%", 18);
            }

//            Plot2d.legend(18, "cosy 5th", "tracking");
            Plot2d.legend(18, "cosy 5th", "cosy slice", "tracking");

            Plot2d.equal();

            Plot2d.showThread();
        }

        // 束流轨迹
        if(false){

//            RunningParticle p = ParticleFactory.createIdealProtonAtTrajectory250MeV(trajectory);
//
//            List<Point3> run = ParticleRunner.run(p, bendingMagnet, trajectory.getLength(), MM);
//
//            Plot3d.plot3(run,Plot2d.RED_LINE);
//
//            trajectory.plot3d();
//
//            Plot3d.showThread();

//            tracking束流轨迹(bendingMagnet,trajectory,true,0,16,true, Plot2d.RED_LINE);


            RunningParticle ip = ParticleFactory.createIdealProtonAtTrajectory250MeV(trajectory);

            List<PhaseSpaceParticle> pp = PhaseSpaceParticles.phaseSpaceParticlesAlongPositiveEllipseInPlane(
                    true, 3.5 * MM, 7.5 * MM, 0, 16);

            List<RunningParticle> ps = ParticleFactory.createParticlesFromPhaseSpaceParticle(ip, ip.computeNaturalCoordinateSystem(), pp);

            ps.stream().parallel().forEach(p->{
                List<Point3> run = ParticleRunner.run(p, bendingMagnet, trajectory.getLength(), MM);

                Plot3d.plot3(run,Plot2d.RED_LINE);
            });

            Plot3d.showThread();
        }

        // 切片
        if(false){
            List<String> list = bendingMagnet.sliceToCosyScript(Bp, 30 * MM, trajectory, 5 * MM, MM, 0.1);
            list.forEach(System.out::println);
        }
    }

    @run(103)
    public void n1弯铁_左偏(){
        //前后1m漂移段，弯铁半径1.1m，偏转65度，左手侧偏移，孔径30mm，n1=8

        HardBendingMagnet bendingMagnet = HardBendingMagnet.create(Point2.origin(), Vector2.yDirect(),
                false, 65, 1.1, Bp, 8, 0);

        Trajectory trajectory = TrajectoryFactory.setStartingPoint(0, -1)
                .setStraightLine(1, Vector2.yDirect())
                .addArcLine(1.1, false, 65)
                .addStraitLine(1);

        // 磁场分布
        if(false){
            List<Point2> magnetBzAlongTrajectory = bendingMagnet.magnetBzAlongTrajectory(trajectory);

            List<Point2> gradientAlongTrajectory = bendingMagnet.magnetGradientAlongTrajectory(trajectory, MM, 5 * MM);

            Plot2d.plot2(magnetBzAlongTrajectory,Plot2d.BLACK_LINE);
            Plot2d.plot2(gradientAlongTrajectory,Plot2d.RED_LINE);

            Plot2d.info("s/m","By/T & G/Tm-1","n1弯铁_左偏",18);

            Plot2d.legend(18,"By","G");

            Plot2d.showThread();
        }


        //画图
        if(false){ //画图
            trajectory.plot2d(MM, Plot2d.BLACK_LINE);

            bendingMagnet.plot2(Plot2d.RED_LINE);

            Plot2d.info("x/m","y/m","n1弯铁_左偏",18);

            Plot2d.equal();

            Plot2d.showThread();
        }

        // 相椭圆
        if(false){
            boolean xPlane = true;

            double delta = 0.05;

            List<Point2> tracking相椭圆 = tracking相椭圆(trajectory.getLength(), xPlane,
                    delta, 16, false, 1, bendingMagnet, trajectory);

            CosyArbitraryOrder.CosyMapArbitraryOrder cosyMap左偏n1弯铁 = cosyMap左偏n1弯铁();

//            CosyArbitraryOrder.CosyMapArbitraryOrder cosyMap右偏n1弯铁切片 = cosyMap右偏n1弯铁切片();

            List<Point2> cosy相椭圆 = cosy相椭圆(xPlane, delta, 512, 5,
                    false, 1, cosyMap左偏n1弯铁);


//            List<Point2> cosy相椭圆切片 = cosy相椭圆(xPlane, delta, 512, 5,
//                    false, 1, cosyMap右偏n1弯铁切片);


            Plot2d.plot2(cosy相椭圆, Plot2d.RED_LINE);

//            Plot2d.plot2(cosy相椭圆切片, Plot2d.BLUE_LINE);

            Plot2d.plot2(tracking相椭圆, Plot2d.BLACK_POINT);

            if(xPlane){
                Plot2d.info("x/mm", "xp/mrad", "纯弯铁 x/xp dp=5%", 18);
            }else {
                Plot2d.info("y/mm", "yp/mrad", "纯弯铁 y/yp dp=5%", 18);
            }

            Plot2d.legend(18, "cosy 5th", "tracking");
//            Plot2d.legend(18, "cosy 5th", "cosy slice", "tracking");

            Plot2d.equal();

            Plot2d.showThread();
        }

        // 束流轨迹
        if(false){

//            RunningParticle p = ParticleFactory.createIdealProtonAtTrajectory250MeV(trajectory);
//
//            List<Point3> run = ParticleRunner.run(p, bendingMagnet, trajectory.getLength(), MM);
//
//            Plot3d.plot3(run,Plot2d.RED_LINE);
//
//            trajectory.plot3d();
//
//            Plot3d.showThread();

//            tracking束流轨迹(bendingMagnet,trajectory,true,0,16,true, Plot2d.RED_LINE);


            RunningParticle ip = ParticleFactory.createIdealProtonAtTrajectory250MeV(trajectory);

            List<PhaseSpaceParticle> pp = PhaseSpaceParticles.phaseSpaceParticlesAlongPositiveEllipseInPlane(
                    true, 3.5 * MM, 7.5 * MM, 0, 16);

            List<RunningParticle> ps = ParticleFactory.createParticlesFromPhaseSpaceParticle(ip, ip.computeNaturalCoordinateSystem(), pp);

            ps.stream().parallel().forEach(p->{
                List<Point3> run = ParticleRunner.run(p, bendingMagnet, trajectory.getLength(), MM);

                Plot3d.plot3(run,Plot2d.RED_LINE);
            });

            Plot3d.showThread();
        }

        // 切片
        if(true){
            List<String> list = bendingMagnet.sliceToCosyScript(Bp, 30 * MM, trajectory, 5 * MM, MM, 0.1);
            list.forEach(System.out::println);
        }
    }

    @run(104)
    public void n1弯铁_n1负数右偏(){
        //前后1m漂移段，弯铁半径1.1m，偏转65度，右手侧偏移，孔径30mm，n1=-8

        HardBendingMagnet bendingMagnet = HardBendingMagnet.create(Point2.origin(), Vector2.yDirect(),
                true, 65, 1.1, Bp, -8, 0);

        Trajectory trajectory = TrajectoryFactory.setStartingPoint(0, -1)
                .setStraightLine(1, Vector2.yDirect())
                .addArcLine(1.1, true, 65)
                .addStraitLine(1);

        // 磁场分布
        if(false){
            List<Point2> magnetBzAlongTrajectory = bendingMagnet.magnetBzAlongTrajectory(trajectory);

            List<Point2> gradientAlongTrajectory = bendingMagnet.magnetGradientAlongTrajectory(trajectory, MM, 5 * MM);

            Plot2d.plot2(magnetBzAlongTrajectory,Plot2d.BLACK_LINE);
            Plot2d.plot2(gradientAlongTrajectory,Plot2d.RED_LINE);

            Plot2d.info("s/m","By/T & G/Tm-1","n1弯铁_n1负数右偏",18);

            Plot2d.legend(18,"By","G");

            Plot2d.showThread();
        }


        //画图
        if(false){ //画图
            trajectory.plot2d(MM, Plot2d.BLACK_LINE);

            bendingMagnet.plot2(Plot2d.RED_LINE);

            Plot2d.info("x/m","y/m","n1弯铁_n1负数右偏",18);

            Plot2d.equal();

            Plot2d.showThread();
        }

        // 相椭圆
        if(false){
            boolean xPlane = false;

            double delta = 0.05;

            List<Point2> tracking相椭圆 = tracking相椭圆(trajectory.getLength(), xPlane,
                    delta, 16, false, 1, bendingMagnet, trajectory);

            CosyArbitraryOrder.CosyMapArbitraryOrder cosyMap右偏n1负数弯铁 = cosyMap右偏n1负数弯铁();

//            CosyArbitraryOrder.CosyMapArbitraryOrder cosyMap右偏n1弯铁切片 = cosyMap右偏n1弯铁切片();

            List<Point2> cosy相椭圆 = cosy相椭圆(xPlane, delta, 512, 5,
                    false, 1, cosyMap右偏n1负数弯铁);


//            List<Point2> cosy相椭圆切片 = cosy相椭圆(xPlane, delta, 512, 5,
//                    false, 1, cosyMap右偏n1弯铁切片);


            Plot2d.plot2(cosy相椭圆, Plot2d.RED_LINE);

//            Plot2d.plot2(cosy相椭圆切片, Plot2d.BLUE_LINE);

            Plot2d.plot2(tracking相椭圆, Plot2d.BLACK_POINT);

            if(xPlane){
                Plot2d.info("x/mm", "xp/mrad", "n1弯铁_n1负数右偏 x/xp dp=5%", 18);
            }else {
                Plot2d.info("y/mm", "yp/mrad", "n1弯铁_n1负数右偏 y/yp dp=5%", 18);
            }

            Plot2d.legend(18, "cosy 5th", "tracking");
//            Plot2d.legend(18, "cosy 5th", "cosy slice", "tracking");

            Plot2d.equal();

            Plot2d.showThread();
        }

        // 束流轨迹
        if(false){

//            RunningParticle p = ParticleFactory.createIdealProtonAtTrajectory250MeV(trajectory);
//
//            List<Point3> run = ParticleRunner.run(p, bendingMagnet, trajectory.getLength(), MM);
//
//            Plot3d.plot3(run,Plot2d.RED_LINE);
//
//            trajectory.plot3d();
//
//            Plot3d.showThread();

//            tracking束流轨迹(bendingMagnet,trajectory,true,0,16,true, Plot2d.RED_LINE);


            RunningParticle ip = ParticleFactory.createIdealProtonAtTrajectory250MeV(trajectory);

            List<PhaseSpaceParticle> pp = PhaseSpaceParticles.phaseSpaceParticlesAlongPositiveEllipseInPlane(
                    true, 3.5 * MM, 7.5 * MM, 0, 16);

            List<RunningParticle> ps = ParticleFactory.createParticlesFromPhaseSpaceParticle(ip, ip.computeNaturalCoordinateSystem(), pp);

            ps.stream().parallel().forEach(p->{
                List<Point3> run = ParticleRunner.run(p, bendingMagnet, trajectory.getLength(), MM);

                Plot3d.plot3(run,Plot2d.RED_LINE);
            });

            Plot3d.showThread();
        }

        // 切片
        if(true){
            List<String> list = bendingMagnet.sliceToCosyScript(Bp, 30 * MM, trajectory, 5 * MM, MM, 0.1);
            list.forEach(System.out::println);
        }
    }

    @run(105)
    public void n1弯铁_n1负数左偏(){
        //前后1m漂移段，弯铁半径1.3m，偏转35度，左手侧偏移，孔径60mm，n1=-5

        HardBendingMagnet bendingMagnet = HardBendingMagnet.create(Point2.origin(), Vector2.yDirect(),
                false, 35, 1.3, Bp, -5, 0);

        Trajectory trajectory = TrajectoryFactory.setStartingPoint(0, -1)
                .setStraightLine(1, Vector2.yDirect())
                .addArcLine(1.3, false, 35)
                .addStraitLine(1);

        // 磁场分布
        if(false){
            List<Point2> magnetBzAlongTrajectory = bendingMagnet.magnetBzAlongTrajectory(trajectory);

            List<Point2> gradientAlongTrajectory = bendingMagnet.magnetGradientAlongTrajectory(trajectory, MM, 5 * MM);

            Plot2d.plot2(magnetBzAlongTrajectory,Plot2d.BLACK_LINE);
            Plot2d.plot2(gradientAlongTrajectory,Plot2d.RED_LINE);

            Plot2d.info("s/m","By/T & G/Tm-1","n1弯铁_n1负数左偏",18);

            Plot2d.legend(18,"By","G");

            Plot2d.showThread();
        }


        //画图
        if(false){ //画图
            trajectory.plot2d(MM, Plot2d.BLACK_LINE);

            bendingMagnet.plot2(Plot2d.RED_LINE);

            Plot2d.info("x/m","y/m","n1弯铁_n1负数左偏",18);

            Plot2d.equal();

            Plot2d.showThread();
        }

        // 相椭圆
        if(false){
            boolean xPlane = true;

            double delta = 0.05;

            List<Point2> tracking相椭圆 = tracking相椭圆(trajectory.getLength(), xPlane,
                    delta, 16, false, 1, bendingMagnet, trajectory);

            CosyArbitraryOrder.CosyMapArbitraryOrder cosyMap左偏n1负数弯铁 = cosyMap左偏n1负数弯铁();

//            CosyArbitraryOrder.CosyMapArbitraryOrder cosyMap右偏n1弯铁切片 = cosyMap右偏n1弯铁切片();

            List<Point2> cosy相椭圆 = cosy相椭圆(xPlane, delta, 512, 5,
                    false, 1, cosyMap左偏n1负数弯铁);


//            List<Point2> cosy相椭圆切片 = cosy相椭圆(xPlane, delta, 512, 5,
//                    false, 1, cosyMap右偏n1弯铁切片);


            Plot2d.plot2(cosy相椭圆, Plot2d.RED_LINE);

//            Plot2d.plot2(cosy相椭圆切片, Plot2d.BLUE_LINE);

            Plot2d.plot2(tracking相椭圆, Plot2d.BLACK_POINT);

            if(xPlane){
                Plot2d.info("x/mm", "xp/mrad", "n1弯铁_n1负数左偏 x/xp dp=5%", 18);
            }else {
                Plot2d.info("y/mm", "yp/mrad", "n1弯铁_n1负数左偏 y/yp dp=5%", 18);
            }

            Plot2d.legend(18, "cosy 5th", "tracking");
//            Plot2d.legend(18, "cosy 5th", "cosy slice", "tracking");

            Plot2d.equal();

            Plot2d.showThread();
        }

        // 束流轨迹
        if(false){

//            RunningParticle p = ParticleFactory.createIdealProtonAtTrajectory250MeV(trajectory);
//
//            List<Point3> run = ParticleRunner.run(p, bendingMagnet, trajectory.getLength(), MM);
//
//            Plot3d.plot3(run,Plot2d.RED_LINE);
//
//            trajectory.plot3d();
//
//            Plot3d.showThread();

//            tracking束流轨迹(bendingMagnet,trajectory,true,0,16,true, Plot2d.RED_LINE);


            RunningParticle ip = ParticleFactory.createIdealProtonAtTrajectory250MeV(trajectory);

            List<PhaseSpaceParticle> pp = PhaseSpaceParticles.phaseSpaceParticlesAlongPositiveEllipseInPlane(
                    true, 3.5 * MM, 7.5 * MM, 0, 16);

            List<RunningParticle> ps = ParticleFactory.createParticlesFromPhaseSpaceParticle(ip, ip.computeNaturalCoordinateSystem(), pp);

            ps.stream().parallel().forEach(p->{
                List<Point3> run = ParticleRunner.run(p, bendingMagnet, trajectory.getLength(), MM);

                Plot3d.plot3(run,Plot2d.RED_LINE);
            });

            Plot3d.showThread();
        }

        // 切片
        if(true){
            List<String> list = bendingMagnet.sliceToCosyScript(Bp, 60 * MM, trajectory, 5 * MM, MM, 0.1);
            list.forEach(System.out::println);
        }
    }

    @run(201)
    public void n2弯铁_n2正数左偏(){
        //前后1m漂移段，弯铁半径0.84m，偏转66度，左手侧偏移，孔径50mm，n2=3

        HardBendingMagnet bendingMagnet = HardBendingMagnet.create(Point2.origin(), Vector2.yDirect(),
                false, 66, 0.84, Bp, 0, 100);

        Trajectory trajectory = TrajectoryFactory.setStartingPoint(0, -1)
                .setStraightLine(1, Vector2.yDirect())
                .addArcLine(0.84, false, 66)
                .addStraitLine(1);

        // 磁场分布
        if(true){
            List<Point2> magnetBzAlongTrajectory = bendingMagnet.magnetBzAlongTrajectory(trajectory);

            List<Point2> gradientAlongTrajectory = bendingMagnet.magnetGradientAlongTrajectory(trajectory, MM, 5 * MM);

            List<Point2> secondGradientAlongTrajectory = bendingMagnet.magnetSecondGradientAlongTrajectory(trajectory, MM, 5 * MM);

            Plot2d.plot2(magnetBzAlongTrajectory,Plot2d.BLACK_LINE);
            Plot2d.plot2(gradientAlongTrajectory,Plot2d.RED_LINE);
            Plot2d.plot2(secondGradientAlongTrajectory,Plot2d.YELLOW_LINE);

            Plot2d.info("s/m","By/T & G/Tm-1 & L/Tm-2","n2弯铁_n2正数左偏",18);

            Plot2d.legend(18,"By","G","L");


            Plot2d.showThread();
        }

        //画图
        if(false){ //画图
            trajectory.plot2d(MM, Plot2d.BLACK_LINE);

            bendingMagnet.plot2(Plot2d.RED_LINE);

            Plot2d.info("x/m","y/m","n2弯铁_n2正数左偏",18);

            Plot2d.equal();

            Plot2d.showThread();
        }

        // 相椭圆
        if(false){
            boolean xPlane = false;

            double delta = 0.05;

            List<Point2> tracking相椭圆 = tracking相椭圆(trajectory.getLength(), xPlane,
                    delta, 16, false, 1, bendingMagnet, trajectory);

            CosyArbitraryOrder.CosyMapArbitraryOrder cosyMap左偏n2正数弯铁 = cosyMap左偏n2正数弯铁();

//            CosyArbitraryOrder.CosyMapArbitraryOrder cosyMap右偏n1弯铁切片 = cosyMap右偏n1弯铁切片();

            List<Point2> cosy相椭圆 = cosy相椭圆(xPlane, delta, 512, 5,
                    false, 1, cosyMap左偏n2正数弯铁);


//            List<Point2> cosy相椭圆切片 = cosy相椭圆(xPlane, delta, 512, 5,
//                    false, 1, cosyMap右偏n1弯铁切片);


            Plot2d.plot2(cosy相椭圆, Plot2d.RED_LINE);

//            Plot2d.plot2(cosy相椭圆切片, Plot2d.BLUE_LINE);

            Plot2d.plot2(tracking相椭圆, Plot2d.BLACK_POINT);

            if(xPlane){
                Plot2d.info("x/mm", "xp/mrad", "n2弯铁_n2正数左偏 x/xp dp=5%", 18);
            }else {
                Plot2d.info("y/mm", "yp/mrad", "n2弯铁_n2正数左偏 y/yp dp=5%", 18);
            }

            Plot2d.legend(18, "cosy 5th", "tracking");
//            Plot2d.legend(18, "cosy 5th", "cosy slice", "tracking");

            Plot2d.equal();

            Plot2d.showThread();
        }

        // 束流轨迹
        if(false){

//            RunningParticle p = ParticleFactory.createIdealProtonAtTrajectory250MeV(trajectory);
//
//            List<Point3> run = ParticleRunner.run(p, bendingMagnet, trajectory.getLength(), MM);
//
//            Plot3d.plot3(run,Plot2d.RED_LINE);
//
//            trajectory.plot3d();
//
//            Plot3d.showThread();

//            tracking束流轨迹(bendingMagnet,trajectory,true,0,16,true, Plot2d.RED_LINE);


            RunningParticle ip = ParticleFactory.createIdealProtonAtTrajectory250MeV(trajectory);

            List<PhaseSpaceParticle> pp = PhaseSpaceParticles.phaseSpaceParticlesAlongPositiveEllipseInPlane(
                    true, 3.5 * MM, 7.5 * MM, 0, 16);

            List<RunningParticle> ps = ParticleFactory.createParticlesFromPhaseSpaceParticle(ip, ip.computeNaturalCoordinateSystem(), pp);

            ps.stream().parallel().forEach(p->{
                List<Point3> run = ParticleRunner.run(p, bendingMagnet, trajectory.getLength(), MM);

                Plot3d.plot3(run,Plot2d.RED_LINE);
            });

            Plot3d.showThread();
        }

        // 切片
        if(true){
            List<String> list = bendingMagnet.sliceToCosyScript(Bp, 50 * MM, trajectory, 5 * MM, MM, 0.1);
            list.forEach(System.out::println);
        }
    }

    @run(202)
    public void n2弯铁_n2负数左偏(){
        //前后1m漂移段，弯铁半径0.84m，偏转66度，左手侧偏移，孔径50mm，n2=-200

        HardBendingMagnet bendingMagnet = HardBendingMagnet.create(Point2.origin(), Vector2.yDirect(),
                false, 66, 0.84, Bp, 0, -120);

        Trajectory trajectory = TrajectoryFactory.setStartingPoint(0, -1)
                .setStraightLine(1, Vector2.yDirect())
                .addArcLine(0.84, false, 66)
                .addStraitLine(1);

        // 磁场分布
        if(true){
            List<Point2> magnetBzAlongTrajectory = bendingMagnet.magnetBzAlongTrajectory(trajectory);

            List<Point2> gradientAlongTrajectory = bendingMagnet.magnetGradientAlongTrajectory(trajectory, MM, 5 * MM);

            List<Point2> secondGradientAlongTrajectory = bendingMagnet.magnetSecondGradientAlongTrajectory(trajectory, MM, 5 * MM);

            Plot2d.plot2(magnetBzAlongTrajectory,Plot2d.BLACK_LINE);
            Plot2d.plot2(gradientAlongTrajectory,Plot2d.RED_LINE);
            Plot2d.plot2(secondGradientAlongTrajectory,Plot2d.YELLOW_LINE);

            Plot2d.info("s/m","By/T & G/Tm-1 & L/Tm-2","n2弯铁_n2正数左偏",18);

            Plot2d.legend(18,"By","G","L");


            Plot2d.showThread();
        }

        //画图
        if(false){ //画图
            trajectory.plot2d(MM, Plot2d.BLACK_LINE);

            bendingMagnet.plot2(Plot2d.RED_LINE);

            Plot2d.info("x/m","y/m","n2弯铁_n2正数左偏",18);

            Plot2d.equal();

            Plot2d.showThread();
        }

        // 相椭圆
        if(false){
            boolean xPlane = false;

            double delta = 0.04;

            List<Point2> tracking相椭圆 = tracking相椭圆(trajectory.getLength(), xPlane,
                    delta, 16, false, 1, bendingMagnet, trajectory);

            CosyArbitraryOrder.CosyMapArbitraryOrder cosyMap左偏n2负数弯铁 = cosyMap左偏n2负数弯铁();

//            CosyArbitraryOrder.CosyMapArbitraryOrder cosyMap右偏n1弯铁切片 = cosyMap右偏n1弯铁切片();

            List<Point2> cosy相椭圆 = cosy相椭圆(xPlane, delta, 512, 5,
                    false, 1, cosyMap左偏n2负数弯铁);


//            List<Point2> cosy相椭圆切片 = cosy相椭圆(xPlane, delta, 512, 5,
//                    false, 1, cosyMap右偏n1弯铁切片);


            Plot2d.plot2(cosy相椭圆, Plot2d.RED_LINE);

//            Plot2d.plot2(cosy相椭圆切片, Plot2d.BLUE_LINE);

            Plot2d.plot2(tracking相椭圆, Plot2d.BLACK_POINT);

            if(xPlane){
                Plot2d.info("x/mm", "xp/mrad", "n2弯铁_n2正数左偏 x/xp dp=5%", 18);
            }else {
                Plot2d.info("y/mm", "yp/mrad", "n2弯铁_n2正数左偏 y/yp dp=5%", 18);
            }

            Plot2d.legend(18, "cosy 5th", "tracking");
//            Plot2d.legend(18, "cosy 5th", "cosy slice", "tracking");

            Plot2d.equal();

            Plot2d.showThread();
        }

        // 束流轨迹
        if(false){

//            RunningParticle p = ParticleFactory.createIdealProtonAtTrajectory250MeV(trajectory);
//
//            List<Point3> run = ParticleRunner.run(p, bendingMagnet, trajectory.getLength(), MM);
//
//            Plot3d.plot3(run,Plot2d.RED_LINE);
//
//            trajectory.plot3d();
//
//            Plot3d.showThread();

//            tracking束流轨迹(bendingMagnet,trajectory,true,0,16,true, Plot2d.RED_LINE);


            RunningParticle ip = ParticleFactory.createIdealProtonAtTrajectory250MeV(trajectory);

            List<PhaseSpaceParticle> pp = PhaseSpaceParticles.phaseSpaceParticlesAlongPositiveEllipseInPlane(
                    true, 3.5 * MM, 7.5 * MM, 0, 16);

            List<RunningParticle> ps = ParticleFactory.createParticlesFromPhaseSpaceParticle(ip, ip.computeNaturalCoordinateSystem(), pp);

            ps.stream().parallel().forEach(p->{
                List<Point3> run = ParticleRunner.run(p, bendingMagnet, trajectory.getLength(), MM);

                Plot3d.plot3(run,Plot2d.RED_LINE);
            });

            Plot3d.showThread();
        }

        // 切片
        if(true){
            List<String> list = bendingMagnet.sliceToCosyScript(Bp, 50 * MM, trajectory, 5 * MM, MM, 0.1);
            list.forEach(System.out::println);
        }
    }


    private void tracking束流轨迹(
            CctFactory.MagnetAble magnetAble, Trajectory trajectory, boolean xPlane, double delta, int number,
            boolean toMM_MRAD, String plotDescribing
    ) {
        RunningParticle ip = ParticleFactory.createIdealProtonAtTrajectory250MeV(trajectory);

        List<PhaseSpaceParticle> pp = PhaseSpaceParticles.phaseSpaceParticlesAlongPositiveEllipseInPlane(
                xPlane, 3.5 * MM, 7.5 * MM, delta, number);

        List<RunningParticle> ps = ParticleFactory.createParticlesFromPhaseSpaceParticle(ip, ip.computeNaturalCoordinateSystem(), pp);

        ps.stream().parallel().forEach(runningParticle -> {
            List<Point2> collect = ParticleRunner.runGetPoint3WithDistance(runningParticle, magnetAble, trajectory.getLength(), MM)
                    .stream()
                    .map(point3WithDistance -> {
                        double distance = point3WithDistance.getDistance();
                        Point3 point3 = point3WithDistance.getPoint3();

                        double y;

                        if (xPlane) {
                            Vector2 v = trajectory.directAt(distance);
                            v.rotateSelf(BaseUtils.Converter.angleToRadian(-90));
                            v.normalSelf();

                            y = point3.toPoint2().toVector2().dot(v);
                        } else {
                            y = point3.z;
                        }

                        if (toMM_MRAD)
                            y /= MM;

                        return Point2.create(distance, y);
                    }).collect(Collectors.toList());

            Plot2d.plot2(collect, plotDescribing);
        });

        Plot2d.showThread();
    }


    private List<Point2> cosy相椭圆(boolean xPlane, double delta, int number, int order,
                                 boolean moveToCenter, double scaleForParticle,
                                 CosyArbitraryOrder.CosyMapArbitraryOrder map
    ) {

        /*----------------COSY-----------------*/

        // 相空间点
        List<PhaseSpaceParticle> pp = PhaseSpaceParticles.phaseSpaceParticlesAlongPositiveEllipseInPlane(
                xPlane, 3.5 * MM * scaleForParticle, 7.5 * MM * scaleForParticle, delta, number);

        // 转为COSY能量分散
        pp = PhaseSpaceParticles.convertDeltaFromMomentumDispersionToEnergyDispersion(pp, 250);

        // 运行
        List<PhaseSpaceParticle> ppEnd = map.apply(pp, order);

        // 投影
        List<Point2> projectionToPlaneCOSY = PhaseSpaceParticles.projectionToPlane(xPlane, ppEnd);

        if (moveToCenter) {
            Vector2 average = Point2.average(projectionToPlaneCOSY).toVector2().reverseSelf();

            projectionToPlaneCOSY.forEach(point2 -> point2.moveSelf(average));
        }

        // 改单位
        return Point2.convert(projectionToPlaneCOSY, 1 / MM, 1 / MRAD);
    }

    private List<Point2> tracking相椭圆(double distance, boolean xPlane, double delta, int number,
                                     boolean moveToCenter, double scaleForParticle,
                                     CctFactory.MagnetAble magnetAble, Trajectory trajectory) {

        RunningParticle ip = ParticleFactory.createIdealProtonAtTrajectory250MeV(trajectory);

        List<PhaseSpaceParticle> pp = PhaseSpaceParticles.phaseSpaceParticlesAlongPositiveEllipseInPlane(
                xPlane, 3.5 * MM * scaleForParticle, 7.5 * MM * scaleForParticle, delta, number);

        List<RunningParticle> ps = ParticleFactory.createParticlesFromPhaseSpaceParticle(ip, ip.computeNaturalCoordinateSystem(), pp);

        ParticleRunner.runThread(ps, magnetAble, distance, MM*0.1);
        ParticleRunner.run(ip, magnetAble, distance, MM*0.1);

        List<PhaseSpaceParticle> ppEnd = PhaseSpaceParticles.phaseSpaceParticlesFromRunningParticles(ip, ip.computeNaturalCoordinateSystem(), ps);

        List<Point2> projectionToPlaneCOSY = PhaseSpaceParticles.projectionToPlane(xPlane, ppEnd);

        if (moveToCenter) {
            Vector2 average = Point2.average(projectionToPlaneCOSY).toVector2().reverseSelf();

            projectionToPlaneCOSY.forEach(point2 -> point2.moveSelf(average));
        }

        // 改单位
        return Point2.convert(projectionToPlaneCOSY, 1 / MM, 1 / MRAD);
    }


    private CosyArbitraryOrder.CosyMapArbitraryOrder cosyMap纯四极场() {
        return CosyArbitraryOrder.readMap(
                "  -5.863073     -6.147451     0.0000000E+00 0.0000000E+00 0.0000000E+00 100000\n" +
                        "  -5.429182     -5.863073     0.0000000E+00 0.0000000E+00 0.0000000E+00 010000\n" +
                        "  0.0000000E+00 0.0000000E+00  12.61149      10.67010     0.0000000E+00 001000\n" +
                        "  0.0000000E+00 0.0000000E+00  14.81239      12.61149     0.0000000E+00 000100\n" +
                        "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00  1.000000     000010\n" +
                        "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00 0.4282836     000001\n" +
                        "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00 -11.46302     200000\n" +
                        "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00 -21.69238     110000\n" +
                        "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00 -10.56523     020000\n" +
                        "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00 -33.55387     002000\n" +
                        "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00 -79.45643     001100\n" +
                        "   2.714404     -1.064182     0.0000000E+00 0.0000000E+00 0.0000000E+00 100001\n" +
                        "   6.117516      2.714404     0.0000000E+00 0.0000000E+00 0.0000000E+00 010001\n" +
                        "  0.0000000E+00 0.0000000E+00 -8.037688     -1.479219     0.0000000E+00 001001\n" +
                        "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00 -47.33329     000200\n" +
                        "  0.0000000E+00 0.0000000E+00 -16.94677     -8.037688     0.0000000E+00 000101\n" +
                        "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00-0.3589750     000002\n" +
                        "  -108.7788      14.84431     0.0000000E+00 0.0000000E+00 0.0000000E+00 300000\n" +
                        "  -315.2152      37.06949     0.0000000E+00 0.0000000E+00 0.0000000E+00 210000\n" +
                        "  -303.4438      31.37965     0.0000000E+00 0.0000000E+00 0.0000000E+00 120000\n" +
                        "  -100.1426      5.833881     0.0000000E+00 0.0000000E+00 0.0000000E+00 030000\n" +
                        "  0.0000000E+00 0.0000000E+00  234.3802      20.70683     0.0000000E+00 201000\n" +
                        "  0.0000000E+00 0.0000000E+00  440.4996      34.57441     0.0000000E+00 111000\n" +
                        "  0.0000000E+00 0.0000000E+00  207.6111      14.68758     0.0000000E+00 021000\n" +
                        "  -340.6543      24.82658     0.0000000E+00 0.0000000E+00 0.0000000E+00 102000\n" +
                        "  -326.6376      21.16308     0.0000000E+00 0.0000000E+00 0.0000000E+00 012000\n" +
                        "  0.0000000E+00 0.0000000E+00  667.4999      35.10851     0.0000000E+00 003000\n" +
                        "  0.0000000E+00 0.0000000E+00  278.9195      25.92338     0.0000000E+00 200100\n" +
                        "  0.0000000E+00 0.0000000E+00  523.3230      42.87210     0.0000000E+00 110100\n" +
                        "  0.0000000E+00 0.0000000E+00  252.7838      23.51039     0.0000000E+00 020100\n" +
                        "  -803.1966      61.52400     0.0000000E+00 0.0000000E+00 0.0000000E+00 101100\n" +
                        "  -770.6856      52.10129     0.0000000E+00 0.0000000E+00 0.0000000E+00 011100\n" +
                        "  0.0000000E+00 0.0000000E+00  2374.661      130.3167     0.0000000E+00 002100\n" +
                        "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00  12.98053     200001\n" +
                        "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00  38.40120     110001\n" +
                        "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00  25.16090     020001\n" +
                        "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00  58.32266     002001\n" +
                        "  -473.2563      38.25347     0.0000000E+00 0.0000000E+00 0.0000000E+00 100200\n" +
                        "  -457.4662      29.02406     0.0000000E+00 0.0000000E+00 0.0000000E+00 010200\n" +
                        "  0.0000000E+00 0.0000000E+00  2817.103      161.8404     0.0000000E+00 001200\n" +
                        "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00  177.6205     001101\n" +
                        "  -1.416596     0.7286411     0.0000000E+00 0.0000000E+00 0.0000000E+00 100002\n" +
                        "  -5.603181     -1.416596     0.0000000E+00 0.0000000E+00 0.0000000E+00 010002\n" +
                        "  0.0000000E+00 0.0000000E+00  6.824316      1.149225     0.0000000E+00 001002\n" +
                        "  0.0000000E+00 0.0000000E+00  1120.930      72.70336     0.0000000E+00 000300\n" +
                        "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00  128.9735     000201\n" +
                        "  0.0000000E+00 0.0000000E+00  18.24194      6.824316     0.0000000E+00 000102\n" +
                        "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00 0.3091390     000003\n" +
                        "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00 -264.0002     400000\n" +
                        "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00 -1022.584     310000\n" +
                        "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00 -1481.413     220000\n" +
                        "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00 -963.4297     130000\n" +
                        "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00 -240.1683     040000\n" +
                        "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00 -1916.293     202000\n" +
                        "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00 -3627.194     112000\n" +
                        "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00 -1720.037     022000\n" +
                        "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00 -3013.793     004000\n" +
                        "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00 -4531.989     201100\n" +
                        "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00 -8575.068     111100\n" +
                        "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00 -4099.837     021100\n" +
                        "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00 -14290.16     003100\n" +
                        "   114.6407     -21.29462     0.0000000E+00 0.0000000E+00 0.0000000E+00 300001\n" +
                        "   535.7559     -78.93626     0.0000000E+00 0.0000000E+00 0.0000000E+00 210001\n" +
                        "   711.4999     -88.00183     0.0000000E+00 0.0000000E+00 0.0000000E+00 120001\n" +
                        "   296.7707     -26.38218     0.0000000E+00 0.0000000E+00 0.0000000E+00 030001\n" +
                        "  0.0000000E+00 0.0000000E+00 -360.3407     -33.48346     0.0000000E+00 201001\n" +
                        "  0.0000000E+00 0.0000000E+00 -956.7734     -79.92656     0.0000000E+00 111001\n" +
                        "  0.0000000E+00 0.0000000E+00 -582.4179     -43.86082     0.0000000E+00 021001\n" +
                        "   593.4272     -42.62642     0.0000000E+00 0.0000000E+00 0.0000000E+00 102001\n" +
                        "   777.2785     -51.02432     0.0000000E+00 0.0000000E+00 0.0000000E+00 012001\n" +
                        "  0.0000000E+00 0.0000000E+00 -1407.537     -67.22619     0.0000000E+00 003001\n" +
                        "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00 -2678.837     200200\n" +
                        "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00 -5078.671     110200\n" +
                        "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00 -2454.024     020200\n" +
                        "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00 -25415.29     002200\n" +
                        "  0.0000000E+00 0.0000000E+00 -567.8140     -54.03213     0.0000000E+00 200101\n" +
                        "  0.0000000E+00 0.0000000E+00 -1397.888     -119.5746     0.0000000E+00 110101\n" +
                        "  0.0000000E+00 0.0000000E+00 -829.0233     -72.58716     0.0000000E+00 020101\n" +
                        "   1799.570     -134.3707     0.0000000E+00 0.0000000E+00 0.0000000E+00 101101\n" +
                        "   2217.852     -150.3811     0.0000000E+00 0.0000000E+00 0.0000000E+00 011101\n" +
                        "  0.0000000E+00 0.0000000E+00 -6187.950     -310.3992     0.0000000E+00 002101\n" +
                        "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00 -12.87965     200002\n" +
                        "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00 -50.25492     110002\n" +
                        "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00 -40.75251     020002\n" +
                        "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00 -81.93550     002002\n" +
                        "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00 -20128.92     001300\n" +
                        "   1296.360     -101.2560     0.0000000E+00 0.0000000E+00 0.0000000E+00 100201\n" +
                        "   1540.703     -103.3013     0.0000000E+00 0.0000000E+00 0.0000000E+00 010201\n" +
                        "  0.0000000E+00 0.0000000E+00 -8740.293     -460.3910     0.0000000E+00 001201\n" +
                        "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00 -291.5718     001102\n" +
                        "  0.8286963    -0.5688646     0.0000000E+00 0.0000000E+00 0.0000000E+00 100003\n" +
                        "   5.029172     0.8286963     0.0000000E+00 0.0000000E+00 0.0000000E+00 010003\n" +
                        "  0.0000000E+00 0.0000000E+00 -6.263225    -0.9904771     0.0000000E+00 001003\n" +
                        "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00 -6000.344     000400\n" +
                        "  0.0000000E+00 0.0000000E+00 -4025.791     -232.0369     0.0000000E+00 000301\n" +
                        "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00 -242.5228     000202\n" +
                        "  0.0000000E+00 0.0000000E+00 -19.35782     -6.263225     0.0000000E+00 000103\n" +
                        "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00-0.2741622     000004\n" +
                        "  -2368.458      216.8160     0.0000000E+00 0.0000000E+00 0.0000000E+00 500000\n" +
                        "  -11661.77      949.6850     0.0000000E+00 0.0000000E+00 0.0000000E+00 410000\n" +
                        "  -22860.82      1670.931     0.0000000E+00 0.0000000E+00 0.0000000E+00 320000\n" +
                        "  -22487.27      1497.516     0.0000000E+00 0.0000000E+00 0.0000000E+00 230000\n" +
                        "  -11183.30      690.5213     0.0000000E+00 0.0000000E+00 0.0000000E+00 140000\n" +
                        "  -2265.556      129.5245     0.0000000E+00 0.0000000E+00 0.0000000E+00 050000\n" +
                        "  0.0000000E+00 0.0000000E+00  5703.459      331.7087     0.0000000E+00 401000\n" +
                        "  0.0000000E+00 0.0000000E+00  21879.05      1163.052     0.0000000E+00 311000\n" +
                        "  0.0000000E+00 0.0000000E+00  31427.88      1535.400     0.0000000E+00 221000\n" +
                        "  0.0000000E+00 0.0000000E+00  20279.38      924.8946     0.0000000E+00 131000\n" +
                        "  0.0000000E+00 0.0000000E+00  5012.595      217.7115     0.0000000E+00 041000\n" +
                        "  -18758.07      869.9244     0.0000000E+00 0.0000000E+00 0.0000000E+00 302000\n" +
                        "  -53910.62      2302.207     0.0000000E+00 0.0000000E+00 0.0000000E+00 212000\n" +
                        "  -51598.02      2038.679     0.0000000E+00 0.0000000E+00 0.0000000E+00 122000\n" +
                        "  -16627.82      616.4823     0.0000000E+00 0.0000000E+00 0.0000000E+00 032000\n" +
                        "  0.0000000E+00 0.0000000E+00  39537.16      1338.912     0.0000000E+00 203000\n" +
                        "  0.0000000E+00 0.0000000E+00  74437.90      2355.622     0.0000000E+00 113000\n" +
                        "  0.0000000E+00 0.0000000E+00  35144.48      1042.501     0.0000000E+00 023000\n" +
                        "  -30734.74      880.0673     0.0000000E+00 0.0000000E+00 0.0000000E+00 104000\n" +
                        "  -29484.68      788.3965     0.0000000E+00 0.0000000E+00 0.0000000E+00 014000\n" +
                        "  0.0000000E+00 0.0000000E+00  60672.60      1360.696     0.0000000E+00 005000\n" +
                        "  0.0000000E+00 0.0000000E+00  6790.598      407.5837     0.0000000E+00 400100\n" +
                        "  0.0000000E+00 0.0000000E+00  26021.57      1424.504     0.0000000E+00 310100\n" +
                        "  0.0000000E+00 0.0000000E+00  37464.60      1886.256     0.0000000E+00 220100\n" +
                        "  0.0000000E+00 0.0000000E+00  24304.62      1145.670     0.0000000E+00 130100\n" +
                        "  0.0000000E+00 0.0000000E+00  6059.016      276.7784     0.0000000E+00 040100\n" +
                        "  -44236.59      2117.070     0.0000000E+00 0.0000000E+00 0.0000000E+00 301100\n" +
                        "  -127141.7      5591.213     0.0000000E+00 0.0000000E+00 0.0000000E+00 211100\n" +
                        "  -122043.9      4968.439     0.0000000E+00 0.0000000E+00 0.0000000E+00 121100\n" +
                        "  -39554.79      1515.009     0.0000000E+00 0.0000000E+00 0.0000000E+00 031100\n" +
                        "  0.0000000E+00 0.0000000E+00  140571.5      4890.087     0.0000000E+00 202100\n" +
                        "  0.0000000E+00 0.0000000E+00  264526.2      8585.105     0.0000000E+00 112100\n" +
                        "  0.0000000E+00 0.0000000E+00  125873.0      3846.814     0.0000000E+00 022100\n" +
                        "  -145467.8      4272.459     0.0000000E+00 0.0000000E+00 0.0000000E+00 103100\n" +
                        "  -139594.9      3822.922     0.0000000E+00 0.0000000E+00 0.0000000E+00 013100\n" +
                        "  0.0000000E+00 0.0000000E+00  359887.7      8254.870     0.0000000E+00 004100\n" +
                        "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00  459.3927     400001\n" +
                        "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00  2440.758     310001\n" +
                        "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00  4493.105     220001\n" +
                        "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00  3533.792     130001\n" +
                        "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00  1028.236     040001\n" +
                        "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00  4867.013     202001\n" +
                        "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00  11513.81     112001\n" +
                        "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00  6550.106     022001\n" +
                        "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00  9528.264     004001\n" +
                        "  -26066.30      1288.605     0.0000000E+00 0.0000000E+00 0.0000000E+00 300200\n" +
                        "  -75093.00      3418.365     0.0000000E+00 0.0000000E+00 0.0000000E+00 210200\n" +
                        "  -72458.18      3066.134     0.0000000E+00 0.0000000E+00 0.0000000E+00 120200\n" +
                        "  -23672.93      942.5988     0.0000000E+00 0.0000000E+00 0.0000000E+00 030200\n" +
                        "  0.0000000E+00 0.0000000E+00  166613.5      5959.159     0.0000000E+00 201200\n" +
                        "  0.0000000E+00 0.0000000E+00  313613.5      10458.85     0.0000000E+00 111200\n" +
                        "  0.0000000E+00 0.0000000E+00  150507.9      4754.086     0.0000000E+00 021200\n" +
                        "  -258227.3      7785.504     0.0000000E+00 0.0000000E+00 0.0000000E+00 102200\n" +
                        "  -248058.4      6969.929     0.0000000E+00 0.0000000E+00 0.0000000E+00 012200\n" +
                        "  0.0000000E+00 0.0000000E+00  854080.5      20049.95     0.0000000E+00 003200\n" +
                        "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00  13771.57     201101\n" +
                        "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00  31499.00     111101\n" +
                        "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00  17627.64     021101\n" +
                        "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00  52287.49     003101\n" +
                        "  -87.77641      23.92789     0.0000000E+00 0.0000000E+00 0.0000000E+00 300002\n" +
                        "  -622.2366      115.5855     0.0000000E+00 0.0000000E+00 0.0000000E+00 210002\n" +
                        "  -1078.793      158.6640     0.0000000E+00 0.0000000E+00 0.0000000E+00 120002\n" +
                        "  -548.5644      60.92965     0.0000000E+00 0.0000000E+00 0.0000000E+00 030002\n" +
                        "  0.0000000E+00 0.0000000E+00  429.3552      41.87469     0.0000000E+00 201002\n" +
                        "  0.0000000E+00 0.0000000E+00  1442.159      126.3666     0.0000000E+00 111002\n" +
                        "  0.0000000E+00 0.0000000E+00  1061.998      83.93731     0.0000000E+00 021002\n" +
                        "  -788.1082      56.13034     0.0000000E+00 0.0000000E+00 0.0000000E+00 102002\n" +
                        "  -1272.675      83.86419     0.0000000E+00 0.0000000E+00 0.0000000E+00 012002\n" +
                        "  0.0000000E+00 0.0000000E+00  2193.629      97.16840     0.0000000E+00 003002\n" +
                        "  0.0000000E+00 0.0000000E+00  65954.41      2433.891     0.0000000E+00 200300\n" +
                        "  0.0000000E+00 0.0000000E+00  124271.5      4277.727     0.0000000E+00 110300\n" +
                        "  0.0000000E+00 0.0000000E+00  60196.46      1983.718     0.0000000E+00 020300\n" +
                        "  -204111.4      6337.336     0.0000000E+00 0.0000000E+00 0.0000000E+00 101300\n" +
                        "  -196414.9      5685.244     0.0000000E+00 0.0000000E+00 0.0000000E+00 011300\n" +
                        "  0.0000000E+00 0.0000000E+00  1014707.      24427.07     0.0000000E+00 002300\n" +
                        "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00  9477.385     200201\n" +
                        "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00  21174.76     110201\n" +
                        "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00  11741.98     020201\n" +
                        "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00  105630.7     002201\n" +
                        "  0.0000000E+00 0.0000000E+00  826.2234      80.87686     0.0000000E+00 200102\n" +
                        "  0.0000000E+00 0.0000000E+00  2471.504      218.8140     0.0000000E+00 110102\n" +
                        "  0.0000000E+00 0.0000000E+00  1719.515      149.3403     0.0000000E+00 020102\n" +
                        "  -2848.416      209.4899     0.0000000E+00 0.0000000E+00 0.0000000E+00 101102\n" +
                        "  -4197.031      284.1255     0.0000000E+00 0.0000000E+00 0.0000000E+00 011102\n" +
                        "  0.0000000E+00 0.0000000E+00  11155.13      521.8210     0.0000000E+00 002102\n" +
                        "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00  12.23371     200003\n" +
                        "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00  58.73332     110003\n" +
                        "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00  56.16386     020003\n" +
                        "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00  105.5922     002003\n" +
                        "  -60715.81      1952.620     0.0000000E+00 0.0000000E+00 0.0000000E+00 100400\n" +
                        "  -58569.83      1755.234     0.0000000E+00 0.0000000E+00 0.0000000E+00 010400\n" +
                        "  0.0000000E+00 0.0000000E+00  604131.5      14963.54     0.0000000E+00 001400\n" +
                        "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00  93620.00     001301\n" +
                        "  -2379.730      181.9512     0.0000000E+00 0.0000000E+00 0.0000000E+00 100202\n" +
                        "  -3301.715      224.7428     0.0000000E+00 0.0000000E+00 0.0000000E+00 010202\n" +
                        "  0.0000000E+00 0.0000000E+00  17901.85      881.5541     0.0000000E+00 001202\n" +
                        "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00  420.4540     001103\n" +
                        " -0.4901830     0.4728506     0.0000000E+00 0.0000000E+00 0.0000000E+00 100004\n" +
                        "  -4.511696    -0.4901830     0.0000000E+00 0.0000000E+00 0.0000000E+00 010004\n" +
                        "  0.0000000E+00 0.0000000E+00  5.944756     0.8949600     0.0000000E+00 001004\n" +
                        "  0.0000000E+00 0.0000000E+00  144345.6      3700.601     0.0000000E+00 000500\n" +
                        "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00  30849.69     000401\n" +
                        "  0.0000000E+00 0.0000000E+00  9226.502      490.4519     0.0000000E+00 000302\n" +
                        "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00  387.6250     000203\n" +
                        "  0.0000000E+00 0.0000000E+00  20.38960      5.944756     0.0000000E+00 000104\n" +
                        "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00 0.2484967     000005"
        );
    }

    private CosyArbitraryOrder.CosyMapArbitraryOrder cosyMap纯四机场切片() {
        return CosyArbitraryOrder.readMap(
                "  -5.861340     -6.148514     0.0000000E+00 0.0000000E+00 0.0000000E+00 100000\n" +
                        "  -5.430804     -5.867493     0.0000000E+00 0.0000000E+00 0.0000000E+00 010000\n" +
                        "  0.0000000E+00 0.0000000E+00  12.61390      10.67736     0.0000000E+00 001000\n" +
                        "  0.0000000E+00 0.0000000E+00  14.82063      12.62458     0.0000000E+00 000100\n" +
                        "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00  1.000000     000010\n" +
                        "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00 0.4282837     000001\n" +
                        "  0.3702974E-13 0.3159314E-13 0.0000000E+00 0.0000000E+00 -11.46187     200000\n" +
                        "  0.8228334E-13 0.7073306E-13 0.0000000E+00 0.0000000E+00 -21.70271     110000\n" +
                        "  0.4596814E-13 0.3985455E-13 0.0000000E+00 0.0000000E+00 -10.57617     020000\n" +
                        "  0.0000000E+00 0.0000000E+00-0.2135531E-12-0.1955481E-12 0.0000000E+00 101000\n" +
                        "  0.0000000E+00 0.0000000E+00-0.2377824E-12-0.2184408E-12 0.0000000E+00 011000\n" +
                        " -0.1277282E-12-0.1181253E-12 0.0000000E+00 0.0000000E+00 -33.58272     002000\n" +
                        "  0.0000000E+00 0.0000000E+00-0.2280251E-12-0.2090352E-12 0.0000000E+00 100100\n" +
                        "  0.0000000E+00 0.0000000E+00-0.2547041E-12-0.2342693E-12 0.0000000E+00 010100\n" +
                        " -0.2815519E-12-0.2609749E-12 0.0000000E+00 0.0000000E+00 -79.55310     001100\n" +
                        "   2.712634     -1.065468     0.0000000E+00 0.0000000E+00 0.0000000E+00 100001\n" +
                        "   6.117947      2.715006     0.0000000E+00 0.0000000E+00 0.0000000E+00 010001\n" +
                        "  0.0000000E+00 0.0000000E+00 -8.040594     -1.481451     0.0000000E+00 001001\n" +
                        " -0.1552316E-12-0.1442576E-12 0.0000000E+00 0.0000000E+00 -47.40744     000200\n" +
                        "  0.0000000E+00 0.0000000E+00 -16.95834     -8.048047     0.0000000E+00 000101\n" +
                        "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00-0.3589750     000002\n" +
                        "  -108.7650      14.87226     0.0000000E+00 0.0000000E+00 0.1210077E-12 300000\n" +
                        "  -315.3617      37.16440     0.0000000E+00 0.0000000E+00 0.3841367E-12 210000\n" +
                        "  -303.7642      31.48026     0.0000000E+00 0.0000000E+00 0.4063528E-12 120000\n" +
                        "  -100.3031      5.866282     0.0000000E+00 0.0000000E+00 0.1431116E-12 030000\n" +
                        "  0.0000000E+00 0.0000000E+00  234.5312      20.75197     0.0000000E+00 201000\n" +
                        "  0.0000000E+00 0.0000000E+00  441.0371      34.67322     0.0000000E+00 111000\n" +
                        "  0.0000000E+00 0.0000000E+00  207.9837      14.73903     0.0000000E+00 021000\n" +
                        "  -340.9854      24.88476     0.0000000E+00 0.0000000E+00 0.8011898E-12 102000\n" +
                        "  -327.1449      21.22699     0.0000000E+00 0.0000000E+00 0.9651417E-12 012000\n" +
                        "  0.0000000E+00 0.0000000E+00  668.5423      35.20213     0.0000000E+00 003000\n" +
                        "  0.0000000E+00 0.0000000E+00  279.1991      25.98785     0.0000000E+00 200100\n" +
                        "  0.0000000E+00 0.0000000E+00  524.1496      43.00849     0.0000000E+00 110100\n" +
                        "  0.0000000E+00 0.0000000E+00  253.3188      23.58593     0.0000000E+00 020100\n" +
                        "  -804.2650      61.68703     0.0000000E+00 0.0000000E+00 0.1825250E-11 101100\n" +
                        "  -772.1583      52.27532     0.0000000E+00 0.0000000E+00 0.2197937E-11 011100\n" +
                        "  0.0000000E+00 0.0000000E+00  2379.215      130.7042     0.0000000E+00 002100\n" +
                        " -0.1340490E-15 0.1935906E-13 0.0000000E+00 0.0000000E+00  12.97546     200001\n" +
                        " -0.4669330E-13 0.3137128E-14 0.0000000E+00 0.0000000E+00  38.41083     110001\n" +
                        " -0.5216634E-13-0.2105149E-13 0.0000000E+00 0.0000000E+00  25.18232     020001\n" +
                        "  0.0000000E+00 0.0000000E+00 0.1320388E-12 0.1188040E-13 0.0000000E+00 101001\n" +
                        "  0.0000000E+00 0.0000000E+00 0.2821695E-12 0.1375283E-12 0.0000000E+00 011001\n" +
                        "  0.1026437E-12 0.3018686E-13 0.0000000E+00 0.0000000E+00  58.38026     002001\n" +
                        "  -474.0557      38.36630     0.0000000E+00 0.0000000E+00 0.1034573E-11 100200\n" +
                        "  -458.4979      29.13966     0.0000000E+00 0.0000000E+00 0.1246191E-11 010200\n" +
                        "  0.0000000E+00 0.0000000E+00  2823.507      162.3701     0.0000000E+00 001200\n" +
                        "  0.0000000E+00 0.0000000E+00 0.2655397E-12 0.1267435E-12 0.0000000E+00 100101\n" +
                        "  0.0000000E+00 0.0000000E+00 0.4414928E-12 0.2754135E-12 0.0000000E+00 010101\n" +
                        "  0.3832854E-12 0.2121174E-12 0.0000000E+00 0.0000000E+00  177.8577     001101\n" +
                        "  -1.414940     0.7294721     0.0000000E+00 0.0000000E+00 0.0000000E+00 100002\n" +
                        "  -5.602574     -1.416134     0.0000000E+00 0.0000000E+00 0.0000000E+00 010002\n" +
                        "  0.0000000E+00 0.0000000E+00  6.827442      1.151016     0.0000000E+00 001002\n" +
                        "  0.0000000E+00 0.0000000E+00  1123.864      72.94932     0.0000000E+00 000300\n" +
                        "  0.2978974E-12 0.1975226E-12 0.0000000E+00 0.0000000E+00  129.1907     000201\n" +
                        "  0.0000000E+00 0.0000000E+00  18.25580      6.833798     0.0000000E+00 000102\n" +
                        "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00 0.3091391     000003\n" +
                        "  0.1458108E-11-0.4769312E-12 0.0000000E+00 0.0000000E+00 -263.9895     400000\n" +
                        "  0.6201973E-11-0.1795551E-11 0.0000000E+00 0.0000000E+00 -1023.150     310000\n" +
                        "  0.9855287E-11-0.2530493E-11 0.0000000E+00 0.0000000E+00 -1483.113     220000\n" +
                        "  0.6969979E-11-0.1552130E-11 0.0000000E+00 0.0000000E+00 -965.0915     130000\n" +
                        "  0.1863155E-11-0.3360820E-12 0.0000000E+00 0.0000000E+00 -240.7132     040000\n" +
                        "  0.0000000E+00 0.0000000E+00-0.6581199E-11-0.4288762E-12 0.0000000E+00 301000\n" +
                        "  0.0000000E+00 0.0000000E+00-0.2032309E-10-0.1389303E-11 0.0000000E+00 211000\n" +
                        "  0.0000000E+00 0.0000000E+00-0.2083262E-10-0.1432993E-11 0.0000000E+00 121000\n" +
                        "  0.0000000E+00 0.0000000E+00-0.7207221E-11-0.5805512E-12 0.0000000E+00 031000\n" +
                        "  0.6832075E-11-0.1429524E-11 0.0000000E+00 0.0000000E+00 -1918.674     202000\n" +
                        "  0.1574835E-10-0.2823368E-11 0.0000000E+00 0.0000000E+00 -3633.794     112000\n" +
                        "  0.8879275E-11-0.1380075E-11 0.0000000E+00 0.0000000E+00 -1724.158     022000\n" +
                        "  0.0000000E+00 0.0000000E+00-0.2770023E-10-0.9777659E-12 0.0000000E+00 103000\n" +
                        "  0.0000000E+00 0.0000000E+00-0.3259566E-10-0.1432074E-11 0.0000000E+00 013000\n" +
                        " -0.7797506E-11-0.7965233E-12 0.0000000E+00 0.0000000E+00 -3020.566     004000\n" +
                        "  0.0000000E+00 0.0000000E+00-0.7365381E-11-0.5287257E-12 0.0000000E+00 300100\n" +
                        "  0.0000000E+00 0.0000000E+00-0.2277199E-10-0.1692097E-11 0.0000000E+00 210100\n" +
                        "  0.0000000E+00 0.0000000E+00-0.2347846E-10-0.1830309E-11 0.0000000E+00 120100\n" +
                        "  0.0000000E+00 0.0000000E+00-0.8209127E-11-0.7948335E-12 0.0000000E+00 030100\n" +
                        "  0.1563450E-10-0.3501000E-11 0.0000000E+00 0.0000000E+00 -4539.245     201100\n" +
                        "  0.3609144E-10-0.6862777E-11 0.0000000E+00 0.0000000E+00 -8593.750     111100\n" +
                        "  0.2024142E-10-0.3456539E-11 0.0000000E+00 0.0000000E+00 -4111.084     021100\n" +
                        "  0.0000000E+00 0.0000000E+00-0.9572667E-10-0.3556512E-11 0.0000000E+00 102100\n" +
                        "  0.0000000E+00 0.0000000E+00-0.1127292E-09-0.5185605E-11 0.0000000E+00 012100\n" +
                        " -0.3594357E-10-0.3910387E-11 0.0000000E+00 0.0000000E+00 -14327.36     003100\n" +
                        "   114.5479     -21.33142     0.0000000E+00 0.0000000E+00-0.8793498E-13 300001\n" +
                        "   535.7625     -79.12444     0.0000000E+00 0.0000000E+00-0.5040427E-12 210001\n" +
                        "   712.0019     -88.26879     0.0000000E+00 0.0000000E+00-0.7727160E-12 120001\n" +
                        "   297.1642     -26.49381     0.0000000E+00 0.0000000E+00-0.3571947E-12 030001\n" +
                        "  0.0000000E+00 0.0000000E+00 -360.5373     -33.55521     0.0000000E+00 201001\n" +
                        "  0.0000000E+00 0.0000000E+00 -957.8418     -80.14758     0.0000000E+00 111001\n" +
                        "  0.0000000E+00 0.0000000E+00 -583.4017     -44.00975     0.0000000E+00 021001\n" +
                        "   594.0105     -42.72697     0.0000000E+00 0.0000000E+00-0.1382226E-11 102001\n" +
                        "   778.4706     -51.17585     0.0000000E+00 0.0000000E+00-0.2172591E-11 012001\n" +
                        "  0.0000000E+00 0.0000000E+00 -1409.965     -67.41300     0.0000000E+00 003001\n" +
                        "  0.8896402E-11-0.2126843E-11 0.0000000E+00 0.0000000E+00 -2684.088     200200\n" +
                        "  0.2061797E-10-0.4112898E-11 0.0000000E+00 0.0000000E+00 -5091.535     110200\n" +
                        "  0.1153380E-10-0.2103394E-11 0.0000000E+00 0.0000000E+00 -2461.584     020200\n" +
                        "  0.0000000E+00 0.0000000E+00-0.1101343E-09-0.4310290E-11 0.0000000E+00 101200\n" +
                        "  0.0000000E+00 0.0000000E+00-0.1299208E-09-0.6357488E-11 0.0000000E+00 011200\n" +
                        " -0.6215359E-10-0.7204500E-11 0.0000000E+00 0.0000000E+00 -25490.50     002200\n" +
                        "  0.0000000E+00 0.0000000E+00 -568.3533     -54.16670     0.0000000E+00 200101\n" +
                        "  0.0000000E+00 0.0000000E+00 -1400.002     -119.9479     0.0000000E+00 110101\n" +
                        "  0.0000000E+00 0.0000000E+00 -830.7256     -72.83461     0.0000000E+00 020101\n" +
                        "   1802.015     -134.7327     0.0000000E+00 0.0000000E+00-0.4060099E-11 101101\n" +
                        "   2222.090     -150.8798     0.0000000E+00 0.0000000E+00-0.6057930E-11 011101\n" +
                        "  0.0000000E+00 0.0000000E+00 -6200.738     -311.3591     0.0000000E+00 002101\n" +
                        " -0.7276807E-14-0.1025977E-13 0.0000000E+00 0.0000000E+00 -12.87108     200002\n" +
                        "  0.1815397E-13-0.1769214E-13 0.0000000E+00 0.0000000E+00 -50.25775     110002\n" +
                        "  0.4429863E-13 0.6161318E-14 0.0000000E+00 0.0000000E+00 -40.78075     020002\n" +
                        "  0.0000000E+00 0.0000000E+00-0.9995521E-13-0.4880030E-14 0.0000000E+00 101002\n" +
                        "  0.0000000E+00 0.0000000E+00-0.2954515E-12-0.1055700E-12 0.0000000E+00 011002\n" +
                        " -0.9893033E-13-0.2813475E-13 0.0000000E+00 0.0000000E+00 -82.02403     002002\n" +
                        "  0.0000000E+00 0.0000000E+00-0.4228708E-10-0.1838248E-11 0.0000000E+00 100300\n" +
                        "  0.0000000E+00 0.0000000E+00-0.5000448E-10-0.2732765E-11 0.0000000E+00 010300\n" +
                        " -0.4791561E-10-0.6023538E-11 0.0000000E+00 0.0000000E+00 -20195.58     001300\n" +
                        "   1298.601     -101.5612     0.0000000E+00 0.0000000E+00-0.2823504E-11 100201\n" +
                        "   1544.205     -103.6934     0.0000000E+00 0.0000000E+00-0.4069978E-11 010201\n" +
                        "  0.0000000E+00 0.0000000E+00 -8761.381     -461.9561     0.0000000E+00 001201\n" +
                        "  0.0000000E+00 0.0000000E+00-0.2736644E-12-0.9442275E-13 0.0000000E+00 100102\n" +
                        "  0.0000000E+00 0.0000000E+00-0.5823251E-12-0.2861336E-12 0.0000000E+00 010102\n" +
                        " -0.4586590E-12-0.2051927E-12 0.0000000E+00 0.0000000E+00 -291.9868     001102\n" +
                        "  0.8271458    -0.5694819     0.0000000E+00 0.0000000E+00 0.0000000E+00 100003\n" +
                        "   5.027775     0.8278088     0.0000000E+00 0.0000000E+00 0.0000000E+00 010003\n" +
                        "  0.0000000E+00 0.0000000E+00 -6.266514    -0.9920573     0.0000000E+00 001003\n" +
                        " -0.1392630E-10-0.1947602E-11 0.0000000E+00 0.0000000E+00 -6022.288     000400\n" +
                        "  0.0000000E+00 0.0000000E+00 -4036.875     -232.8707     0.0000000E+00 000301\n" +
                        " -0.4337176E-12-0.2370955E-12 0.0000000E+00 0.0000000E+00 -242.9520     000202\n" +
                        "  0.0000000E+00 0.0000000E+00 -19.37362     -6.272366     0.0000000E+00 000103\n" +
                        "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00-0.2741623     000004\n" +
                        "  -2367.911      217.3192     0.0000000E+00 0.0000000E+00 0.4645190E-11 500000\n" +
                        "  -11666.33      952.4923     0.0000000E+00 0.0000000E+00 0.2433612E-10 410000\n" +
                        "  -22883.89      1676.920     0.0000000E+00 0.0000000E+00 0.5082714E-10 320000\n" +
                        "  -22523.58      1503.774     0.0000000E+00 0.0000000E+00 0.5308243E-10 230000\n" +
                        "  -11207.89      693.7858     0.0000000E+00 0.0000000E+00 0.2782603E-10 140000\n" +
                        "  -2271.793      130.2122     0.0000000E+00 0.0000000E+00 0.5880463E-11 050000\n" +
                        "  0.0000000E+00 0.0000000E+00  5708.039      332.6063     0.0000000E+00 401000\n" +
                        "  0.0000000E+00 0.0000000E+00  21909.44      1166.938     0.0000000E+00 311000\n" +
                        "  0.0000000E+00 0.0000000E+00  31489.98      1541.495     0.0000000E+00 221000\n" +
                        "  0.0000000E+00 0.0000000E+00  20331.00      929.1015     0.0000000E+00 131000\n" +
                        "  0.0000000E+00 0.0000000E+00  5028.052      218.8129     0.0000000E+00 041000\n" +
                        "  -18782.20      872.4897     0.0000000E+00 0.0000000E+00 0.6484491E-10 302000\n" +
                        "  -54011.43      2310.459     0.0000000E+00 0.0000000E+00 0.2030343E-09 212000\n" +
                        "  -51724.65      2047.269     0.0000000E+00 0.0000000E+00 0.2107947E-09 122000\n" +
                        "  -16678.06      619.4387     0.0000000E+00 0.0000000E+00 0.7306496E-10 032000\n" +
                        "  0.0000000E+00 0.0000000E+00  39615.80      1343.391     0.0000000E+00 203000\n" +
                        "  0.0000000E+00 0.0000000E+00  74628.90      2364.999     0.0000000E+00 113000\n" +
                        "  0.0000000E+00 0.0000000E+00  35254.88      1047.304     0.0000000E+00 023000\n" +
                        "  -30807.46      883.2297     0.0000000E+00 0.0000000E+00 0.1428012E-09 104000\n" +
                        "  -29571.61      791.7308     0.0000000E+00 0.0000000E+00 0.1732454E-09 014000\n" +
                        "  0.0000000E+00 0.0000000E+00  60852.01      1366.137     0.0000000E+00 005000\n" +
                        "  0.0000000E+00 0.0000000E+00  6798.498      408.8203     0.0000000E+00 400100\n" +
                        "  0.0000000E+00 0.0000000E+00  26067.11      1429.738     0.0000000E+00 310100\n" +
                        "  0.0000000E+00 0.0000000E+00  37552.00      1894.360     0.0000000E+00 220100\n" +
                        "  0.0000000E+00 0.0000000E+00  24374.99      1151.239     0.0000000E+00 130100\n" +
                        "  0.0000000E+00 0.0000000E+00  6079.748      278.2439     0.0000000E+00 040100\n" +
                        "  -44309.37      2124.006     0.0000000E+00 0.0000000E+00 0.1487910E-09 301100\n" +
                        "  -127425.1      5613.107     0.0000000E+00 0.0000000E+00 0.4661921E-09 211100\n" +
                        "  -122386.7      4990.988     0.0000000E+00 0.0000000E+00 0.4851194E-09 121100\n" +
                        "  -39688.04      1522.746     0.0000000E+00 0.0000000E+00 0.1689384E-09 031100\n" +
                        "  0.0000000E+00 0.0000000E+00  140901.4      4908.049     0.0000000E+00 202100\n" +
                        "  0.0000000E+00 0.0000000E+00  265299.7      8622.123     0.0000000E+00 112100\n" +
                        "  0.0000000E+00 0.0000000E+00  126312.0      3865.722     0.0000000E+00 022100\n" +
                        "  -145864.0      4289.210     0.0000000E+00 0.0000000E+00 0.6636638E-09 103100\n" +
                        "  -140056.4      3840.352     0.0000000E+00 0.0000000E+00 0.8053920E-09 013100\n" +
                        "  0.0000000E+00 0.0000000E+00  361080.0      8290.579     0.0000000E+00 004100\n" +
                        " -0.6101276E-12 0.5625364E-12 0.0000000E+00 0.0000000E+00  459.1101     400001\n" +
                        " -0.6476404E-11 0.3255604E-11 0.0000000E+00 0.0000000E+00  2441.041     310001\n" +
                        " -0.1646493E-10 0.6193378E-11 0.0000000E+00 0.0000000E+00  4496.644     220001\n" +
                        " -0.1599085E-10 0.4847064E-11 0.0000000E+00 0.0000000E+00  3538.810     130001\n" +
                        " -0.5420985E-11 0.1322917E-11 0.0000000E+00 0.0000000E+00  1030.305     040001\n" +
                        "  0.0000000E+00 0.0000000E+00 0.8576747E-11 0.5044782E-12 0.0000000E+00 301001\n" +
                        "  0.0000000E+00 0.0000000E+00 0.3871761E-10 0.2509168E-11 0.0000000E+00 211001\n" +
                        "  0.0000000E+00 0.0000000E+00 0.5226512E-10 0.3513987E-11 0.0000000E+00 121001\n" +
                        "  0.0000000E+00 0.0000000E+00 0.2235178E-10 0.1663737E-11 0.0000000E+00 031001\n" +
                        " -0.1098293E-10 0.2410822E-11 0.0000000E+00 0.0000000E+00  4872.910     202001\n" +
                        " -0.3411700E-10 0.6451931E-11 0.0000000E+00 0.0000000E+00  11534.22     112001\n" +
                        " -0.2434800E-10 0.4000420E-11 0.0000000E+00 0.0000000E+00  6565.424     022001\n" +
                        "  0.0000000E+00 0.0000000E+00 0.5688832E-10 0.1638730E-11 0.0000000E+00 103001\n" +
                        "  0.0000000E+00 0.0000000E+00 0.8473038E-10 0.3142030E-11 0.0000000E+00 013001\n" +
                        "  0.1768740E-10 0.1800975E-11 0.0000000E+00 0.0000000E+00  9551.065     004001\n" +
                        "  -26118.57      1293.247     0.0000000E+00 0.0000000E+00 0.8512920E-10 300200\n" +
                        "  -75287.03      3432.838     0.0000000E+00 0.0000000E+00 0.2671337E-09 210200\n" +
                        "  -72686.78      3080.973     0.0000000E+00 0.0000000E+00 0.2788469E-09 120200\n" +
                        "  -23760.56      947.6983     0.0000000E+00 0.0000000E+00 0.9763343E-10 030200\n" +
                        "  0.0000000E+00 0.0000000E+00  167064.2      5982.987     0.0000000E+00 201200\n" +
                        "  0.0000000E+00 0.0000000E+00  314642.4      10507.35     0.0000000E+00 111200\n" +
                        "  0.0000000E+00 0.0000000E+00  151084.5      4778.877     0.0000000E+00 021200\n" +
                        "  -259022.8      7818.564     0.0000000E+00 0.0000000E+00 0.1155739E-08 102200\n" +
                        "  -248966.5      7003.973     0.0000000E+00 0.0000000E+00 0.1403541E-08 012200\n" +
                        "  0.0000000E+00 0.0000000E+00  857213.9      20143.22     0.0000000E+00 003200\n" +
                        "  0.0000000E+00 0.0000000E+00 0.1341263E-10 0.8647605E-12 0.0000000E+00 300101\n" +
                        "  0.0000000E+00 0.0000000E+00 0.5516572E-10 0.3852848E-11 0.0000000E+00 210101\n" +
                        "  0.0000000E+00 0.0000000E+00 0.7095946E-10 0.5244272E-11 0.0000000E+00 120101\n" +
                        "  0.0000000E+00 0.0000000E+00 0.2961431E-10 0.2567049E-11 0.0000000E+00 030101\n" +
                        " -0.3254635E-10 0.7539829E-11 0.0000000E+00 0.0000000E+00  13793.47     201101\n" +
                        " -0.9556201E-10 0.1894799E-10 0.0000000E+00 0.0000000E+00  31566.73     111101\n" +
                        " -0.6545178E-10 0.1150850E-10 0.0000000E+00 0.0000000E+00  17675.34     021101\n" +
                        "  0.0000000E+00 0.0000000E+00 0.2448305E-09 0.7600817E-11 0.0000000E+00 102101\n" +
                        "  0.0000000E+00 0.0000000E+00 0.3500502E-09 0.1382292E-10 0.0000000E+00 012101\n" +
                        "  0.1003900E-09 0.1067899E-10 0.0000000E+00 0.0000000E+00  52430.84     003101\n" +
                        "  -87.61020      23.96583     0.0000000E+00 0.0000000E+00 0.4889767E-13 300002\n" +
                        "  -621.9106      115.8456     0.0000000E+00 0.0000000E+00 0.4847502E-12 210002\n" +
                        "  -1079.158      159.1241     0.0000000E+00 0.0000000E+00 0.1004290E-11 120002\n" +
                        "  -549.1383      61.16241     0.0000000E+00 0.0000000E+00 0.5783041E-12 030002\n" +
                        "  0.0000000E+00 0.0000000E+00  429.5480      41.96297     0.0000000E+00 201002\n" +
                        "  0.0000000E+00 0.0000000E+00  1443.646      126.7088     0.0000000E+00 111002\n" +
                        "  0.0000000E+00 0.0000000E+00  1063.703      84.21613     0.0000000E+00 021002\n" +
                        "  -788.8817      56.26333     0.0000000E+00 0.0000000E+00 0.1896950E-11 102002\n" +
                        "  -1274.612      84.11145     0.0000000E+00 0.0000000E+00 0.3543432E-11 012002\n" +
                        "  0.0000000E+00 0.0000000E+00  2197.693      97.44745     0.0000000E+00 003002\n" +
                        "  0.0000000E+00 0.0000000E+00  66156.15      2444.392     0.0000000E+00 200300\n" +
                        "  0.0000000E+00 0.0000000E+00  124722.9      4298.906     0.0000000E+00 110300\n" +
                        "  0.0000000E+00 0.0000000E+00  60447.32      1994.591     0.0000000E+00 020300\n" +
                        "  -204812.4      6366.252     0.0000000E+00 0.0000000E+00 0.8945887E-09 101300\n" +
                        "  -197202.7      5714.793     0.0000000E+00 0.0000000E+00 0.1087591E-08 011300\n" +
                        "  0.0000000E+00 0.0000000E+00  1018789.      24548.51     0.0000000E+00 002300\n" +
                        " -0.2279555E-10 0.5592354E-11 0.0000000E+00 0.0000000E+00  9495.997     200201\n" +
                        " -0.6457611E-10 0.1340649E-10 0.0000000E+00 0.0000000E+00  21228.12     110201\n" +
                        " -0.4294294E-10 0.8027528E-11 0.0000000E+00 0.0000000E+00  11777.93     020201\n" +
                        "  0.0000000E+00 0.0000000E+00 0.3372978E-09 0.1119822E-10 0.0000000E+00 101201\n" +
                        "  0.0000000E+00 0.0000000E+00 0.4691369E-09 0.1980318E-10 0.0000000E+00 011201\n" +
                        "  0.2061973E-09 0.2305512E-10 0.0000000E+00 0.0000000E+00  105957.3     002201\n" +
                        "  0.0000000E+00 0.0000000E+00  826.9589      81.07762     0.0000000E+00 200102\n" +
                        "  0.0000000E+00 0.0000000E+00  2475.091      219.4890     0.0000000E+00 110102\n" +
                        "  0.0000000E+00 0.0000000E+00  1722.949      149.8594     0.0000000E+00 020102\n" +
                        "  -2852.320      210.0597     0.0000000E+00 0.0000000E+00 0.6559923E-11 101102\n" +
                        "  -4205.048      285.0657     0.0000000E+00 0.0000000E+00 0.1137553E-10 011102\n" +
                        "  0.0000000E+00 0.0000000E+00  11179.49      523.4835     0.0000000E+00 002102\n" +
                        "  0.8962521E-14 0.6279641E-14 0.0000000E+00 0.0000000E+00  12.22217     200003\n" +
                        "  0.6041813E-15 0.2049868E-13 0.0000000E+00 0.0000000E+00  58.72591     110003\n" +
                        " -0.3258252E-13 0.3549737E-14 0.0000000E+00 0.0000000E+00  56.19468     020003\n" +
                        "  0.0000000E+00 0.0000000E+00 0.8229913E-13 0.1632426E-14 0.0000000E+00 101003\n" +
                        "  0.0000000E+00 0.0000000E+00 0.3010581E-12 0.8787957E-13 0.0000000E+00 011003\n" +
                        "  0.1006113E-12 0.2748013E-13 0.0000000E+00 0.0000000E+00  105.7141     002003\n" +
                        "  -60945.36      1962.115     0.0000000E+00 0.0000000E+00 0.2598912E-09 100400\n" +
                        "  -58824.76      1764.884     0.0000000E+00 0.0000000E+00 0.3164256E-09 010400\n" +
                        "  0.0000000E+00 0.0000000E+00  606772.3      15042.56     0.0000000E+00 001400\n" +
                        "  0.0000000E+00 0.0000000E+00 0.1507798E-09 0.5492743E-11 0.0000000E+00 100301\n" +
                        "  0.0000000E+00 0.0000000E+00 0.2057193E-09 0.9574945E-11 0.0000000E+00 010301\n" +
                        "  0.1839112E-09 0.2189017E-10 0.0000000E+00 0.0000000E+00  93942.13     001301\n" +
                        "  -2383.896      182.5072     0.0000000E+00 0.0000000E+00 0.5259079E-11 100202\n" +
                        "  -3309.258      225.5826     0.0000000E+00 0.0000000E+00 0.8659971E-11 010202\n" +
                        "  0.0000000E+00 0.0000000E+00  17947.04      884.6367     0.0000000E+00 001202\n" +
                        "  0.0000000E+00 0.0000000E+00 0.2751881E-12 0.7673138E-13 0.0000000E+00 100103\n" +
                        "  0.0000000E+00 0.0000000E+00 0.6970998E-12 0.2896831E-12 0.0000000E+00 010103\n" +
                        "  0.5290134E-12 0.2090509E-12 0.0000000E+00 0.0000000E+00  421.0822     001103\n" +
                        " -0.4887234     0.4733407     0.0000000E+00 0.0000000E+00 0.0000000E+00 100004\n" +
                        "  -4.509672    -0.4890816     0.0000000E+00 0.0000000E+00 0.0000000E+00 010004\n" +
                        "  0.0000000E+00 0.0000000E+00  5.948188     0.8964149     0.0000000E+00 001004\n" +
                        "  0.0000000E+00 0.0000000E+00  145026.0      3721.211     0.0000000E+00 000500\n" +
                        "  0.6061284E-10 0.7839133E-11 0.0000000E+00 0.0000000E+00  30966.44     000401\n" +
                        "  0.0000000E+00 0.0000000E+00  9252.916      492.2820     0.0000000E+00 000302\n" +
                        "  0.5700181E-12 0.2739729E-12 0.0000000E+00 0.0000000E+00  388.3380     000203\n" +
                        "  0.0000000E+00 0.0000000E+00  20.40716      5.953754     0.0000000E+00 000104\n" +
                        "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00 0.2484967     000005"
        );
    }

    private CosyArbitraryOrder.CosyMapArbitraryOrder cosyMap四六极场场() {
        return CosyArbitraryOrder.readMap(
                "  0.1189193    -0.7999690     0.0000000E+00 0.0000000E+00 0.0000000E+00 100000\n" +
                        "   1.232370     0.1189193     0.0000000E+00 0.0000000E+00 0.0000000E+00 010000\n" +
                        "  0.0000000E+00 0.0000000E+00  1.928417     0.8450517     0.0000000E+00 001000\n" +
                        "  0.0000000E+00 0.0000000E+00  3.217311      1.928417     0.0000000E+00 000100\n" +
                        "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00  1.000000     000010\n" +
                        "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00 0.4282836     000001\n" +
                        "  -835.6298     -756.7112     0.0000000E+00 0.0000000E+00-0.1909834     200000\n" +
                        "  -1835.232     -1666.873     0.0000000E+00 0.0000000E+00 0.7226850E-01 110000\n" +
                        "  -1010.656     -920.6832     0.0000000E+00 0.0000000E+00-0.3049574     020000\n" +
                        "  0.0000000E+00 0.0000000E+00  1855.297      1688.615     0.0000000E+00 101000\n" +
                        "  0.0000000E+00 0.0000000E+00  2037.746      1859.829     0.0000000E+00 011000\n" +
                        "   929.5847      846.1815     0.0000000E+00 0.0000000E+00-0.2125323     002000\n" +
                        "  0.0000000E+00 0.0000000E+00  2028.318      1850.764     0.0000000E+00 100100\n" +
                        "  0.0000000E+00 0.0000000E+00  2233.977      2044.085     0.0000000E+00 010100\n" +
                        "   2041.799      1863.753     0.0000000E+00 0.0000000E+00-0.9851533     001100\n" +
                        "  0.4793188    -0.1238933E-01 0.0000000E+00 0.0000000E+00 0.0000000E+00 100001\n" +
                        " -0.1615922     0.4793188     0.0000000E+00 0.0000000E+00 0.0000000E+00 010001\n" +
                        "  0.0000000E+00 0.0000000E+00-0.5322222    -0.1280363E-01 0.0000000E+00 001001\n" +
                        "   1124.011      1028.811     0.0000000E+00 0.0000000E+00 -1.438971     000200\n" +
                        "  0.0000000E+00 0.0000000E+00 -2.380328    -0.5322222     0.0000000E+00 000101\n" +
                        "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00-0.3589750     000002\n" +
                        "   43622.60      41489.48     0.0000000E+00 0.0000000E+00 -361.8413     300000\n" +
                        "   141848.6      135018.2     0.0000000E+00 0.0000000E+00 -726.7963     210000\n" +
                        "   153707.0      146417.9     0.0000000E+00 0.0000000E+00 -288.5790     120000\n" +
                        "   55511.08      52917.52     0.0000000E+00 0.0000000E+00  82.05993     030000\n" +
                        "  0.0000000E+00 0.0000000E+00  42740.76      40634.65     0.0000000E+00 201000\n" +
                        "  0.0000000E+00 0.0000000E+00  99188.21      94445.62     0.0000000E+00 111000\n" +
                        "  0.0000000E+00 0.0000000E+00  57000.23      54345.47     0.0000000E+00 021000\n" +
                        "   54273.83      51796.58     0.0000000E+00 0.0000000E+00 -445.9551     102000\n" +
                        "   52341.46      49942.62     0.0000000E+00 0.0000000E+00 -1009.930     012000\n" +
                        "  0.0000000E+00 0.0000000E+00  50468.39      48113.49     0.0000000E+00 003000\n" +
                        "  0.0000000E+00 0.0000000E+00  40171.49      38161.46     0.0000000E+00 200100\n" +
                        "  0.0000000E+00 0.0000000E+00  94391.32      89830.47     0.0000000E+00 110100\n" +
                        "  0.0000000E+00 0.0000000E+00  54804.16      52233.13     0.0000000E+00 020100\n" +
                        "   123326.3      117811.8     0.0000000E+00 0.0000000E+00 -2011.110     101100\n" +
                        "   119886.3      114514.5     0.0000000E+00 0.0000000E+00 -3354.125     011100\n" +
                        "  0.0000000E+00 0.0000000E+00  163726.3      156193.0     0.0000000E+00 002100\n" +
                        "   429.5604     -35.55885     0.0000000E+00 0.0000000E+00 0.2742102     200001\n" +
                        "   1969.981      854.2865     0.0000000E+00 0.0000000E+00 0.1236585     110001\n" +
                        "   1650.324      987.1049     0.0000000E+00 0.0000000E+00 0.4040446     020001\n" +
                        "  0.0000000E+00 0.0000000E+00 -1061.863     -23.94043     0.0000000E+00 101001\n" +
                        "  0.0000000E+00 0.0000000E+00 -2306.380     -1066.942     0.0000000E+00 011001\n" +
                        "  -534.1891     -14.07610     0.0000000E+00 0.0000000E+00 0.3178470     002001\n" +
                        "   69627.50      66572.31     0.0000000E+00 0.0000000E+00 -1664.467     100200\n" +
                        "   68153.19      65160.22     0.0000000E+00 0.0000000E+00 -2463.605     010200\n" +
                        "  0.0000000E+00 0.0000000E+00  177025.8      168991.1     0.0000000E+00 001200\n" +
                        "  0.0000000E+00 0.0000000E+00 -2290.548     -1056.784     0.0000000E+00 100101\n" +
                        "  0.0000000E+00 0.0000000E+00 -3772.704     -2310.866     0.0000000E+00 010101\n" +
                        "  -2315.463     -1073.543     0.0000000E+00 0.0000000E+00  1.725794     001101\n" +
                        " -0.3441175     0.9023888E-02 0.0000000E+00 0.0000000E+00 0.0000000E+00 100002\n" +
                        " -0.1684806    -0.3441175     0.0000000E+00 0.0000000E+00 0.0000000E+00 010002\n" +
                        "  0.0000000E+00 0.0000000E+00 0.3976770     0.9443319E-02 0.0000000E+00 001002\n" +
                        "  0.0000000E+00 0.0000000E+00  63801.69      60943.26     0.0000000E+00 000300\n" +
                        "  -1903.261     -1167.953     0.0000000E+00 0.0000000E+00  2.728163     000201\n" +
                        "  0.0000000E+00 0.0000000E+00  2.078188     0.3976770     0.0000000E+00 000102\n" +
                        "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00 0.3091390     000003\n" +
                        "  -1772559.     -1713535.     0.0000000E+00 0.0000000E+00 -152083.9     400000\n" +
                        "  -7561170.     -7313371.     0.0000000E+00 0.0000000E+00 -694190.6     310000\n" +
                        " -0.1209960E+08-0.1170838E+08 0.0000000E+00 0.0000000E+00 -1186807.     220000\n" +
                        "  -8609565.     -8334089.     0.0000000E+00 0.0000000E+00 -900749.3     130000\n" +
                        "  -2298737.     -2225692.     0.0000000E+00 0.0000000E+00 -256096.0     040000\n" +
                        "  0.0000000E+00 0.0000000E+00 -3049774.     -2952348.     0.0000000E+00 301000\n" +
                        "  0.0000000E+00 0.0000000E+00 -9511154.     -9207107.     0.0000000E+00 211000\n" +
                        "  0.0000000E+00 0.0000000E+00 -9885932.     -9570329.     0.0000000E+00 121000\n" +
                        "  0.0000000E+00 0.0000000E+00 -3423801.     -3314968.     0.0000000E+00 031000\n" +
                        "   6913687.      6691428.     0.0000000E+00 0.0000000E+00 -462428.5     202000\n" +
                        "  0.1464397E+08 0.1417755E+08 0.0000000E+00 0.0000000E+00 -1053270.     112000\n" +
                        "   7765721.      7519401.     0.0000000E+00 0.0000000E+00 -593443.9     022000\n" +
                        "  0.0000000E+00 0.0000000E+00  6325990.      6122234.     0.0000000E+00 103000\n" +
                        "  0.0000000E+00 0.0000000E+00  6693761.      6478989.     0.0000000E+00 013000\n" +
                        "  -333705.5     -322854.6     0.0000000E+00 0.0000000E+00 -236685.8     004000\n" +
                        "  0.0000000E+00 0.0000000E+00 -3195050.     -3094283.     0.0000000E+00 300100\n" +
                        "  0.0000000E+00 0.0000000E+00 -9977418.     -9661809.     0.0000000E+00 210100\n" +
                        "  0.0000000E+00 0.0000000E+00-0.1038354E+08-0.1005479E+08 0.0000000E+00 120100\n" +
                        "  0.0000000E+00 0.0000000E+00 -3600268.     -3486517.     0.0000000E+00 030100\n" +
                        "  0.1453235E+08 0.1407040E+08 0.0000000E+00 0.0000000E+00 -1027400.     201100\n" +
                        "  0.3080077E+08 0.2982931E+08 0.0000000E+00 0.0000000E+00 -2350009.     111100\n" +
                        "  0.1634499E+08 0.1583057E+08 0.0000000E+00 0.0000000E+00 -1329525.     021100\n" +
                        "  0.0000000E+00 0.0000000E+00 0.2019247E+08 0.1954363E+08 0.0000000E+00 102100\n" +
                        "  0.0000000E+00 0.0000000E+00 0.2137092E+08 0.2068566E+08 0.0000000E+00 012100\n" +
                        "  -1322914.     -1280636.     0.0000000E+00 0.0000000E+00 -1068035.     003100\n" +
                        "  -46741.23     -21239.79     0.0000000E+00 0.0000000E+00  508.4407     300001\n" +
                        "  -231355.1     -144668.8     0.0000000E+00 0.0000000E+00  1681.545     210001\n" +
                        "  -336691.6     -238804.9     0.0000000E+00 0.0000000E+00  1285.677     120001\n" +
                        "  -152650.6     -115913.7     0.0000000E+00 0.0000000E+00  53.87159     030001\n" +
                        "  0.0000000E+00 0.0000000E+00 -45199.53     -20222.61     0.0000000E+00 201001\n" +
                        "  0.0000000E+00 0.0000000E+00 -160940.5     -100386.7     0.0000000E+00 111001\n" +
                        "  0.0000000E+00 0.0000000E+00 -124631.5     -88419.09     0.0000000E+00 021001\n" +
                        "  -64556.04     -32719.31     0.0000000E+00 0.0000000E+00  677.6212     102001\n" +
                        "  -91876.53     -59816.55     0.0000000E+00 0.0000000E+00  1789.454     012001\n" +
                        "  0.0000000E+00 0.0000000E+00 -58191.08     -28617.15     0.0000000E+00 003001\n" +
                        "   7634170.      7394703.     0.0000000E+00 0.0000000E+00 -565857.5     200200\n" +
                        "  0.1619023E+08 0.1568563E+08 0.0000000E+00 0.0000000E+00 -1300073.     110200\n" +
                        "   8597399.      8329478.     0.0000000E+00 0.0000000E+00 -738637.1     020200\n" +
                        "  0.0000000E+00 0.0000000E+00 0.2150144E+08 0.2080925E+08 0.0000000E+00 101200\n" +
                        "  0.0000000E+00 0.0000000E+00 0.2276257E+08 0.2202982E+08 0.0000000E+00 011200\n" +
                        "  -1961170.     -1900262.     0.0000000E+00 0.0000000E+00 -1805954.     002200\n" +
                        "  0.0000000E+00 0.0000000E+00 -64651.20     -40043.92     0.0000000E+00 200101\n" +
                        "  0.0000000E+00 0.0000000E+00 -205379.0     -145167.0     0.0000000E+00 110101\n" +
                        "  0.0000000E+00 0.0000000E+00 -150204.2     -113927.3     0.0000000E+00 020101\n" +
                        "  -214940.7     -139603.8     0.0000000E+00 0.0000000E+00  3556.195     101101\n" +
                        "  -276658.0     -200388.0     0.0000000E+00 0.0000000E+00  7481.976     011101\n" +
                        "  0.0000000E+00 0.0000000E+00 -280172.6     -180085.2     0.0000000E+00 002101\n" +
                        "  -294.6025      25.45588     0.0000000E+00 0.0000000E+00-0.3336030     200002\n" +
                        "  -1927.786     -584.3669     0.0000000E+00 0.0000000E+00-0.3670623     110002\n" +
                        "  -2083.488     -964.9522     0.0000000E+00 0.0000000E+00-0.5272274     020002\n" +
                        "  0.0000000E+00 0.0000000E+00  792.0153      17.19653     0.0000000E+00 101002\n" +
                        "  0.0000000E+00 0.0000000E+00  2358.904      797.1685     0.0000000E+00 011002\n" +
                        "   400.2292      10.73990     0.0000000E+00 0.0000000E+00-0.3991615     002002\n" +
                        "  0.0000000E+00 0.0000000E+00  7638501.      7390978.     0.0000000E+00 100300\n" +
                        "  0.0000000E+00 0.0000000E+00  8089394.      7826629.     0.0000000E+00 010300\n" +
                        "  -1287048.     -1248874.     0.0000000E+00 0.0000000E+00 -1356265.     001300\n" +
                        "  -159937.4     -115772.5     0.0000000E+00 0.0000000E+00  3705.743     100201\n" +
                        "  -194985.8     -150071.2     0.0000000E+00 0.0000000E+00  6747.740     010201\n" +
                        "  0.0000000E+00 0.0000000E+00 -401753.3     -289175.2     0.0000000E+00 001201\n" +
                        "  0.0000000E+00 0.0000000E+00  2338.422      786.8622     0.0000000E+00 100102\n" +
                        "  0.0000000E+00 0.0000000E+00  4904.092      2361.259     0.0000000E+00 010102\n" +
                        "   2373.210      805.7227     0.0000000E+00 0.0000000E+00 -2.412795     001102\n" +
                        "  0.2790878    -0.7402497E-02 0.0000000E+00 0.0000000E+00 0.0000000E+00 100003\n" +
                        "  0.3187781     0.2790878     0.0000000E+00 0.0000000E+00 0.0000000E+00 010003\n" +
                        "  0.0000000E+00 0.0000000E+00-0.3327262    -0.7822534E-02 0.0000000E+00 001003\n" +
                        "  -314971.5     -306291.4     0.0000000E+00 0.0000000E+00 -381716.5     000400\n" +
                        "  0.0000000E+00 0.0000000E+00 -180412.0     -138306.6     0.0000000E+00 000301\n" +
                        "   2480.314      1198.122     0.0000000E+00 0.0000000E+00 -4.061465     000202\n" +
                        "  0.0000000E+00 0.0000000E+00 -1.931623    -0.3327262     0.0000000E+00 000103\n" +
                        "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00-0.2741622     000004\n" +
                        "  0.5397824E+08 0.5330480E+08 0.0000000E+00 0.0000000E+00 0.1749104E+08 500000\n" +
                        "  0.2874265E+09 0.2832119E+09 0.0000000E+00 0.0000000E+00 0.9645226E+08 410000\n" +
                        "  0.6124965E+09 0.6020053E+09 0.0000000E+00 0.0000000E+00 0.2126700E+09 320000\n" +
                        "  0.6530128E+09 0.6400214E+09 0.0000000E+00 0.0000000E+00 0.2343822E+09 230000\n" +
                        "  0.3483751E+09 0.3403683E+09 0.0000000E+00 0.0000000E+00 0.1291177E+09 140000\n" +
                        "  0.7441130E+08 0.7244569E+08 0.0000000E+00 0.0000000E+00 0.2844453E+08 050000\n" +
                        "  0.0000000E+00 0.0000000E+00  9370697.      7869957.     0.0000000E+00 401000\n" +
                        "  0.0000000E+00 0.0000000E+00 0.3416530E+08 0.2905034E+08 0.0000000E+00 311000\n" +
                        "  0.0000000E+00 0.0000000E+00 0.4552372E+08 0.3949896E+08 0.0000000E+00 221000\n" +
                        "  0.0000000E+00 0.0000000E+00 0.2602733E+08 0.2335097E+08 0.0000000E+00 131000\n" +
                        "  0.0000000E+00 0.0000000E+00  5303813.      5039116.     0.0000000E+00 041000\n" +
                        "  0.1269709E+08 0.1305433E+08 0.0000000E+00 0.0000000E+00-0.3305071E+08 302000\n" +
                        "  0.4148395E+08 0.4330857E+08 0.0000000E+00 0.0000000E+00-0.1204181E+09 212000\n" +
                        "  0.4508311E+08 0.4769892E+08 0.0000000E+00 0.0000000E+00-0.1441612E+09 122000\n" +
                        "  0.1628593E+08 0.1744884E+08 0.0000000E+00 0.0000000E+00-0.5688965E+08 032000\n" +
                        "  0.0000000E+00 0.0000000E+00 0.2002929E+09 0.1934479E+09 0.0000000E+00 203000\n" +
                        "  0.0000000E+00 0.0000000E+00 0.4249351E+09 0.4088125E+09 0.0000000E+00 113000\n" +
                        "  0.0000000E+00 0.0000000E+00 0.2253224E+09 0.2158423E+09 0.0000000E+00 023000\n" +
                        "  0.9907308E+08 0.9633211E+08 0.0000000E+00 0.0000000E+00-0.7571617E+08 104000\n" +
                        "  0.1099079E+09 0.1057400E+09 0.0000000E+00 0.0000000E+00-0.7956315E+08 014000\n" +
                        "  0.0000000E+00 0.0000000E+00 0.4422535E+08 0.4279591E+08 0.0000000E+00 005000\n" +
                        "  0.0000000E+00 0.0000000E+00 0.1298001E+08 0.1101313E+08 0.0000000E+00 400100\n" +
                        "  0.0000000E+00 0.0000000E+00 0.4920763E+08 0.4213510E+08 0.0000000E+00 310100\n" +
                        "  0.0000000E+00 0.0000000E+00 0.6902877E+08 0.5993174E+08 0.0000000E+00 220100\n" +
                        "  0.0000000E+00 0.0000000E+00 0.4234989E+08 0.3753887E+08 0.0000000E+00 130100\n" +
                        "  0.0000000E+00 0.0000000E+00  9554679.      8736262.     0.0000000E+00 040100\n" +
                        "  0.2080376E+08 0.2305250E+08 0.0000000E+00 0.0000000E+00-0.6316598E+08 301100\n" +
                        "  0.6888849E+08 0.7771101E+08 0.0000000E+00 0.0000000E+00-0.2335430E+09 211100\n" +
                        "  0.7586811E+08 0.8687399E+08 0.0000000E+00 0.0000000E+00-0.2828236E+09 121100\n" +
                        "  0.2776556E+08 0.3222904E+08 0.0000000E+00 0.0000000E+00-0.1126517E+09 031100\n" +
                        "  0.0000000E+00 0.0000000E+00 0.6466571E+09 0.6207266E+09 0.0000000E+00 202100\n" +
                        "  0.0000000E+00 0.0000000E+00 0.1373140E+10 0.1312421E+10 0.0000000E+00 112100\n" +
                        "  0.0000000E+00 0.0000000E+00 0.7288528E+09 0.6933511E+09 0.0000000E+00 022100\n" +
                        "  0.4201488E+09 0.4070427E+09 0.0000000E+00 0.0000000E+00-0.3353719E+09 103100\n" +
                        "  0.4663668E+09 0.4468246E+09 0.0000000E+00 0.0000000E+00-0.3528172E+09 013100\n" +
                        "  0.0000000E+00 0.0000000E+00 0.2350173E+09 0.2268956E+09 0.0000000E+00 004100\n" +
                        "   2894003.      1839541.     0.0000000E+00 0.0000000E+00  197683.9     400001\n" +
                        "  0.1657228E+08 0.1193898E+08 0.0000000E+00 0.0000000E+00  1294136.     310001\n" +
                        "  0.3328309E+08 0.2565833E+08 0.0000000E+00 0.0000000E+00  2881942.     220001\n" +
                        "  0.2849363E+08 0.2292136E+08 0.0000000E+00 0.0000000E+00  2695126.     130001\n" +
                        "   8891367.      7364700.     0.0000000E+00 0.0000000E+00  910574.0     040001\n" +
                        "  0.0000000E+00 0.0000000E+00  5103875.      3291007.     0.0000000E+00 301001\n" +
                        "  0.0000000E+00 0.0000000E+00 0.2123377E+08 0.1541108E+08 0.0000000E+00 211001\n" +
                        "  0.0000000E+00 0.0000000E+00 0.2759745E+08 0.2136999E+08 0.0000000E+00 121001\n" +
                        "  0.0000000E+00 0.0000000E+00 0.1147349E+08  9256616.     0.0000000E+00 031001\n" +
                        " -0.1160092E+08 -7489055.     0.0000000E+00 0.0000000E+00  708148.1     202001\n" +
                        " -0.3275631E+08-0.2379072E+08 0.0000000E+00 0.0000000E+00  2205444.     112001\n" +
                        " -0.2170927E+08-0.1682019E+08 0.0000000E+00 0.0000000E+00  1576493.     022001\n" +
                        "  0.0000000E+00 0.0000000E+00-0.1071676E+08 -6951422.     0.0000000E+00 103001\n" +
                        "  0.0000000E+00 0.0000000E+00-0.1507951E+08-0.1097661E+08 0.0000000E+00 013001\n" +
                        "   531166.1      333266.4     0.0000000E+00 0.0000000E+00  368266.6     004001\n" +
                        "   7662882.      9661488.     0.0000000E+00 0.0000000E+00-0.2981580E+08 300200\n" +
                        "  0.2604227E+08 0.3341409E+08 0.0000000E+00 0.0000000E+00-0.1122198E+09 210200\n" +
                        "  0.2940478E+08 0.3822627E+08 0.0000000E+00 0.0000000E+00-0.1377575E+09 120200\n" +
                        "  0.1101857E+08 0.1448401E+08 0.0000000E+00 0.0000000E+00-0.5546337E+08 030200\n" +
                        "  0.0000000E+00 0.0000000E+00 0.6958573E+09 0.6636481E+09 0.0000000E+00 201200\n" +
                        "  0.0000000E+00 0.0000000E+00 0.1479088E+10 0.1403964E+10 0.0000000E+00 111200\n" +
                        "  0.0000000E+00 0.0000000E+00 0.7859855E+09 0.7422310E+09 0.0000000E+00 021200\n" +
                        "  0.6685498E+09 0.6451345E+09 0.0000000E+00 0.0000000E+00-0.5566222E+09 102200\n" +
                        "  0.7425744E+09 0.7082660E+09 0.0000000E+00 0.0000000E+00-0.5862144E+09 012200\n" +
                        "  0.0000000E+00 0.0000000E+00 0.4998849E+09 0.4813483E+09 0.0000000E+00 003200\n" +
                        "  0.0000000E+00 0.0000000E+00  7129560.      5175146.     0.0000000E+00 300101\n" +
                        "  0.0000000E+00 0.0000000E+00 0.2784010E+08 0.2156114E+08 0.0000000E+00 210101\n" +
                        "  0.0000000E+00 0.0000000E+00 0.3477833E+08 0.2806071E+08 0.0000000E+00 120101\n" +
                        "  0.0000000E+00 0.0000000E+00 0.1407344E+08 0.1168130E+08 0.0000000E+00 030101\n" +
                        " -0.3250258E+08-0.2360615E+08 0.0000000E+00 0.0000000E+00  2138506.     201101\n" +
                        " -0.8610053E+08-0.6671587E+08 0.0000000E+00 0.0000000E+00  6213489.     111101\n" +
                        " -0.5482032E+08-0.4425259E+08 0.0000000E+00 0.0000000E+00  4263189.     021101\n" +
                        "  0.0000000E+00 0.0000000E+00-0.4549068E+08-0.3311297E+08 0.0000000E+00 102101\n" +
                        "  0.0000000E+00 0.0000000E+00-0.6008409E+08-0.4660603E+08 0.0000000E+00 012101\n" +
                        "   2837991.      2030421.     0.0000000E+00 0.0000000E+00  2256869.     003101\n" +
                        "   45670.37      14516.84     0.0000000E+00 0.0000000E+00 -608.1030     300002\n" +
                        "   291780.0      141352.8     0.0000000E+00 0.0000000E+00 -2633.562     210002\n" +
                        "   519525.5      301172.5     0.0000000E+00 0.0000000E+00 -2769.420     120002\n" +
                        "   278435.0      178857.0     0.0000000E+00 0.0000000E+00 -490.1884     030002\n" +
                        "  0.0000000E+00 0.0000000E+00  43608.82      13436.06     0.0000000E+00 201002\n" +
                        "  0.0000000E+00 0.0000000E+00  201937.5      97302.77     0.0000000E+00 111002\n" +
                        "  0.0000000E+00 0.0000000E+00  191943.9      111218.4     0.0000000E+00 021002\n" +
                        "   68638.52      26106.16     0.0000000E+00 0.0000000E+00 -860.8354     102002\n" +
                        "   123317.9      63926.16     0.0000000E+00 0.0000000E+00 -2525.164     012002\n" +
                        "  0.0000000E+00 0.0000000E+00  60432.77      21965.21     0.0000000E+00 003002\n" +
                        "  0.0000000E+00 0.0000000E+00 0.2495785E+09 0.2364247E+09 0.0000000E+00 200300\n" +
                        "  0.0000000E+00 0.0000000E+00 0.5310821E+09 0.5004802E+09 0.0000000E+00 110300\n" +
                        "  0.0000000E+00 0.0000000E+00 0.2825709E+09 0.2647938E+09 0.0000000E+00 020300\n" +
                        "  0.4731723E+09 0.4546330E+09 0.0000000E+00 0.0000000E+00-0.4103163E+09 101300\n" +
                        "  0.5259371E+09 0.4991986E+09 0.0000000E+00 0.0000000E+00-0.4325786E+09 011300\n" +
                        "  0.0000000E+00 0.0000000E+00 0.5320657E+09 0.5108325E+09 0.0000000E+00 002300\n" +
                        " -0.2133917E+08-0.1653611E+08 0.0000000E+00 0.0000000E+00  1489700.     200201\n" +
                        " -0.5430316E+08-0.4384383E+08 0.0000000E+00 0.0000000E+00  4154071.     110201\n" +
                        " -0.3363778E+08-0.2793683E+08 0.0000000E+00 0.0000000E+00  2775612.     020201\n" +
                        "  0.0000000E+00 0.0000000E+00-0.6045016E+08-0.4688655E+08 0.0000000E+00 101201\n" +
                        "  0.0000000E+00 0.0000000E+00-0.7670920E+08-0.6194482E+08 0.0000000E+00 011201\n" +
                        "   5292220.      4062990.     0.0000000E+00 0.0000000E+00  4822439.     002201\n" +
                        "  0.0000000E+00 0.0000000E+00  80509.46      38358.62     0.0000000E+00 200102\n" +
                        "  0.0000000E+00 0.0000000E+00  314864.8      181467.3     0.0000000E+00 110102\n" +
                        "  0.0000000E+00 0.0000000E+00  273077.5      175058.6     0.0000000E+00 020102\n" +
                        "   286847.5      148014.6     0.0000000E+00 0.0000000E+00 -5009.877     101102\n" +
                        "   446852.2      268296.3     0.0000000E+00 0.0000000E+00 -12137.48     011102\n" +
                        "  0.0000000E+00 0.0000000E+00  368368.5      187016.4     0.0000000E+00 002102\n" +
                        "   230.2135     -20.60042     0.0000000E+00 0.0000000E+00 0.3821034     200003\n" +
                        "   1875.828      455.6284     0.0000000E+00 0.0000000E+00 0.6296447     110003\n" +
                        "   2414.624      938.0844     0.0000000E+00 0.0000000E+00 0.6920296     020003\n" +
                        "  0.0000000E+00 0.0000000E+00 -661.5007     -13.95030     0.0000000E+00 101003\n" +
                        "  0.0000000E+00 0.0000000E+00 -2382.107     -666.6706     0.0000000E+00 011003\n" +
                        "  -335.7558     -9.128024     0.0000000E+00 0.0000000E+00 0.4695420     002003\n" +
                        "  0.1257109E+09 0.1202193E+09 0.0000000E+00 0.0000000E+00-0.1133583E+09 100400\n" +
                        "  0.1398356E+09 0.1320281E+09 0.0000000E+00 0.0000000E+00-0.1196279E+09 010400\n" +
                        "  0.0000000E+00 0.0000000E+00 0.2834415E+09 0.2712406E+09 0.0000000E+00 001400\n" +
                        "  0.0000000E+00 0.0000000E+00-0.2573950E+08-0.2078226E+08 0.0000000E+00 100301\n" +
                        "  0.0000000E+00 0.0000000E+00-0.3177551E+08-0.2637921E+08 0.0000000E+00 010301\n" +
                        "   4185789.      3360503.     0.0000000E+00 0.0000000E+00  4377374.     001301\n" +
                        "   257322.3      154229.3     0.0000000E+00 0.0000000E+00 -6002.021     100202\n" +
                        "   368510.9      241988.4     0.0000000E+00 0.0000000E+00 -12575.44     010202\n" +
                        "  0.0000000E+00 0.0000000E+00  639781.9      380198.3     0.0000000E+00 001202\n" +
                        "  0.0000000E+00 0.0000000E+00 -2357.856     -656.3307     0.0000000E+00 100103\n" +
                        "  0.0000000E+00 0.0000000E+00 -5830.161     -2382.630     0.0000000E+00 010103\n" +
                        "  -2401.683     -676.8138     0.0000000E+00 0.0000000E+00  3.079726     001103\n" +
                        " -0.2397217     0.6420256E-02 0.0000000E+00 0.0000000E+00 0.0000000E+00 100004\n" +
                        " -0.4066499    -0.2397217     0.0000000E+00 0.0000000E+00 0.0000000E+00 010004\n" +
                        "  0.0000000E+00 0.0000000E+00 0.2933710     0.6840369E-02 0.0000000E+00 001004\n" +
                        "  0.0000000E+00 0.0000000E+00 0.6046944E+08 0.5765727E+08 0.0000000E+00 000500\n" +
                        "   1199296.      993782.9     0.0000000E+00 0.0000000E+00  1444709.     000401\n" +
                        "  0.0000000E+00 0.0000000E+00  337502.5      220247.3     0.0000000E+00 000302\n" +
                        "  -2955.758     -1213.364     0.0000000E+00 0.0000000E+00  5.462789     000203\n" +
                        "  0.0000000E+00 0.0000000E+00  1.844638     0.2933710     0.0000000E+00 000104\n" +
                        "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00 0.2484967     000005"
        );
    }

    private CosyArbitraryOrder.CosyMapArbitraryOrder cosyMap左偏简单弯铁(){
        return CosyArbitraryOrder.readMap(
                "  -1.176471     -1.176471     0.0000000E+00 0.0000000E+00 0.5587808     100000\n" +
                        " -0.3264706     -1.176471     0.0000000E+00 0.0000000E+00  1.033745     010000\n" +
                        "  0.0000000E+00 0.0000000E+00  1.000000     0.0000000E+00 0.0000000E+00 001000\n" +
                        "  0.0000000E+00 0.0000000E+00  3.335177      1.000000     0.0000000E+00 000100\n" +
                        "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00  1.000000     000010\n" +
                        "  -1.033745    -0.5587808     0.0000000E+00 0.0000000E+00 0.4977838     000001\n" +
                        "  0.5882353     0.0000000E+00 0.0000000E+00 0.0000000E+00-0.3866995     200000\n" +
                        "   1.176471     0.0000000E+00 0.0000000E+00 0.0000000E+00-0.7733991     110000\n" +
                        "   1.088235     0.5000000     0.0000000E+00 0.0000000E+00-0.9035718     020000\n" +
                        "  0.0000000E+00 0.0000000E+00 -1.000000     0.0000000E+00 0.0000000E+00 100100\n" +
                        "  0.0000000E+00 0.0000000E+00 -1.850000     0.0000000E+00 0.0000000E+00 010100\n" +
                        "   1.216170    -0.2220446E-15 0.0000000E+00 0.0000000E+00-0.5620108     100001\n" +
                        "   1.873559     0.6573892     0.0000000E+00 0.0000000E+00 -1.039720     010001\n" +
                        "  0.9250000     0.5000000     0.0000000E+00 0.0000000E+00-0.7962627     000200\n" +
                        "  0.0000000E+00 0.0000000E+00 -1.592525     0.2220446E-15 0.0000000E+00 000101\n" +
                        "  0.6250101     0.9733718E-01 0.0000000E+00 0.0000000E+00-0.5522715     000002\n" +
                        " -0.8141665    -0.2220446E-15 0.0000000E+00 0.0000000E+00 0.1288998     300000\n" +
                        "  -2.442499    -0.6661338E-15 0.0000000E+00 0.0000000E+00 0.3866995     210000\n" +
                        "  -2.942499    -0.6661338E-15 0.0000000E+00 0.0000000E+00 0.7153941     120000\n" +
                        "  -1.902402    -0.5882353     0.0000000E+00 0.0000000E+00 0.8161455     030000\n" +
                        "  0.0000000E+00 0.0000000E+00 0.6920415     0.0000000E+00 0.0000000E+00 200100\n" +
                        "  0.0000000E+00 0.0000000E+00  1.384083     0.0000000E+00 0.0000000E+00 110100\n" +
                        "  0.0000000E+00 0.0000000E+00  1.617042     0.0000000E+00 0.0000000E+00 020100\n" +
                        "  -1.488793     0.2220446E-15 0.0000000E+00 0.0000000E+00 0.7505515     200001\n" +
                        "  -3.634976     0.4440892E-15 0.0000000E+00 0.0000000E+00  1.933264     110001\n" +
                        "  -2.942445    -0.2793904     0.0000000E+00 0.0000000E+00  1.963840     020001\n" +
                        "  -1.088235     0.0000000E+00 0.0000000E+00 0.0000000E+00 0.6080850     100200\n" +
                        "  -1.676471    -0.5882353     0.0000000E+00 0.0000000E+00  1.124957     010200\n" +
                        "  0.0000000E+00 0.0000000E+00  1.216170    -0.1110223E-15 0.0000000E+00 100101\n" +
                        "  0.0000000E+00 0.0000000E+00  2.249915    -0.1110223E-15 0.0000000E+00 010101\n" +
                        "  -1.442429     0.0000000E+00 0.0000000E+00 0.0000000E+00 0.8528926     100002\n" +
                        "  -2.603852    -0.4818508     0.0000000E+00 0.0000000E+00  1.534490     010002\n" +
                        "  0.0000000E+00 0.0000000E+00  1.425000     0.0000000E+00 0.0000000E+00 000300\n" +
                        "  -1.313135    -0.2793904     0.0000000E+00 0.0000000E+00  1.323402     000201\n" +
                        "  0.0000000E+00 0.0000000E+00  1.323402    -0.3330669E-15 0.0000000E+00 000102\n" +
                        " -0.5914913    -0.5439015E-01 0.0000000E+00 0.0000000E+00 0.5725625     000003\n" +
                        "  0.2035416     0.0000000E+00 0.0000000E+00 0.0000000E+00-0.4014182     400000\n" +
                        "  0.8141665     0.0000000E+00 0.0000000E+00 0.0000000E+00 -1.605673     310000\n" +
                        "   2.259312     0.0000000E+00 0.0000000E+00 0.0000000E+00 -2.572856     220000\n" +
                        "   3.478526     0.0000000E+00 0.0000000E+00 0.0000000E+00 -2.321067     130000\n" +
                        "   2.061089     0.1250000     0.0000000E+00 0.0000000E+00 -1.291226     040000\n" +
                        "  0.0000000E+00 0.0000000E+00-0.2306805     0.0000000E+00 0.0000000E+00 300100\n" +
                        "  0.0000000E+00 0.0000000E+00-0.6920415     0.0000000E+00 0.0000000E+00 210100\n" +
                        "  0.0000000E+00 0.0000000E+00 -1.280277     0.1110223E-15 0.0000000E+00 120100\n" +
                        "  0.0000000E+00 0.0000000E+00 -1.460582     0.1110223E-15 0.0000000E+00 030100\n" +
                        "   1.751521    -0.2220446E-15 0.0000000E+00 0.0000000E+00-0.9515974     300001\n" +
                        "   6.619386    -0.2939110E-15 0.0000000E+00 0.0000000E+00 -3.070872     210001\n" +
                        "   9.529073    -0.1110223E-15 0.0000000E+00 0.0000000E+00 -4.108590     120001\n" +
                        "   6.534767     0.9860838     0.0000000E+00 0.0000000E+00 -3.038388     030001\n" +
                        "   1.332180     0.0000000E+00 0.0000000E+00 0.0000000E+00-0.7443966     200200\n" +
                        "   3.252595     0.0000000E+00 0.0000000E+00 0.0000000E+00 -1.875493     110200\n" +
                        "   2.632915     0.2500000     0.0000000E+00 0.0000000E+00 -1.927359     020200\n" +
                        "  0.0000000E+00 0.0000000E+00 -1.488793     0.0000000E+00 0.0000000E+00 200101\n" +
                        "  0.0000000E+00 0.0000000E+00 -3.750985     0.0000000E+00 0.0000000E+00 110101\n" +
                        "  0.0000000E+00 0.0000000E+00 -3.854718     0.0000000E+00 0.0000000E+00 020101\n" +
                        "   2.663234     0.4440892E-15 0.0000000E+00 0.0000000E+00 -1.545567     200002\n" +
                        "   7.472138     0.4350647E-15 0.0000000E+00 0.0000000E+00 -4.246685     110002\n" +
                        "   6.099209     0.2047866     0.0000000E+00 0.0000000E+00 -3.921535     020002\n" +
                        "  0.0000000E+00 0.0000000E+00 -1.088235     0.1110223E-15 0.0000000E+00 100300\n" +
                        "  0.0000000E+00 0.0000000E+00 -2.013235     0.1110223E-15 0.0000000E+00 010300\n" +
                        "   2.810339    -0.6661338E-15 0.0000000E+00 0.0000000E+00 -1.782215     100201\n" +
                        "   5.012593     0.9860838     0.0000000E+00 0.0000000E+00 -3.219498     010201\n" +
                        "  0.0000000E+00 0.0000000E+00 -1.782215     0.0000000E+00 0.0000000E+00 100102\n" +
                        "  0.0000000E+00 0.0000000E+00 -3.219498     0.0000000E+00 0.0000000E+00 010102\n" +
                        "   1.937741     0.0000000E+00 0.0000000E+00 0.0000000E+00 -1.266283     100003\n" +
                        "   3.639079     0.3972258     0.0000000E+00 0.0000000E+00 -2.281155     010003\n" +
                        "  0.5875000     0.1250000     0.0000000E+00 0.0000000E+00-0.6670446     000400\n" +
                        "  0.0000000E+00 0.0000000E+00 -2.668178     0.2220446E-15 0.0000000E+00 000301\n" +
                        "   1.864078     0.2047866     0.0000000E+00 0.0000000E+00 -1.954641     000202\n" +
                        "  0.0000000E+00 0.0000000E+00 -1.303094     0.1110223E-15 0.0000000E+00 000103\n" +
                        "  0.6361437     0.3512944E-01 0.0000000E+00 0.0000000E+00-0.6253767     000004\n" +
                        " -0.8451555     0.2220446E-15 0.0000000E+00 0.0000000E+00 0.8028364E-01 500000\n" +
                        "  -4.225778     0.1110223E-14 0.0000000E+00 0.0000000E+00 0.4014182     410000\n" +
                        "  -8.797576     0.2220446E-14 0.0000000E+00 0.0000000E+00  1.485247     320000\n" +
                        "  -10.71087     0.1887379E-14 0.0000000E+00 0.0000000E+00  3.043419     230000\n" +
                        "  -8.272516     0.4440892E-15 0.0000000E+00 0.0000000E+00  2.987372     140000\n" +
                        "  -3.669779    -0.4411765     0.0000000E+00 0.0000000E+00  1.517578     050000\n" +
                        "  0.0000000E+00 0.0000000E+00 0.7183822    -0.4440892E-15 0.0000000E+00 400100\n" +
                        "  0.0000000E+00 0.0000000E+00  2.873529    -0.1776357E-14 0.0000000E+00 310100\n" +
                        "  0.0000000E+00 0.0000000E+00  4.604411    -0.2664535E-14 0.0000000E+00 220100\n" +
                        "  0.0000000E+00 0.0000000E+00  4.153806    -0.1776357E-14 0.0000000E+00 130100\n" +
                        "  0.0000000E+00 0.0000000E+00  2.310791    -0.4440892E-15 0.0000000E+00 040100\n" +
                        "  -2.348296    -0.4440892E-15 0.0000000E+00 0.0000000E+00  1.227729     400001\n" +
                        "  -9.848126    -0.1776357E-14 0.0000000E+00 0.0000000E+00  5.808134     310001\n" +
                        "  -18.26784    -0.2220446E-14 0.0000000E+00 0.0000000E+00  11.36315     220001\n" +
                        "  -20.01946    -0.6661338E-15 0.0000000E+00 0.0000000E+00  11.79527     130001\n" +
                        "  -10.30656    -0.2095428     0.0000000E+00 0.0000000E+00  6.142833     040001\n" +
                        "  -1.567271     0.4440892E-15 0.0000000E+00 0.0000000E+00 0.8757607     300200\n" +
                        "  -5.923061     0.9992007E-15 0.0000000E+00 0.0000000E+00  2.820632     210200\n" +
                        "  -8.526664     0.7771561E-15 0.0000000E+00 0.0000000E+00  3.811066     120200\n" +
                        "  -5.847344    -0.8823529     0.0000000E+00 0.0000000E+00  2.872411     030200\n" +
                        "  0.0000000E+00 0.0000000E+00  1.751521     0.4440892E-15 0.0000000E+00 300101\n" +
                        "  0.0000000E+00 0.0000000E+00  5.641264     0.1332268E-14 0.0000000E+00 210101\n" +
                        "  0.0000000E+00 0.0000000E+00  7.622132     0.1332268E-14 0.0000000E+00 120101\n" +
                        "  0.0000000E+00 0.0000000E+00  5.744822     0.4440892E-15 0.0000000E+00 030101\n" +
                        "  -4.385212    -0.1776357E-14 0.0000000E+00 0.0000000E+00  2.517904     300002\n" +
                        "  -17.09217    -0.5147648E-14 0.0000000E+00 0.0000000E+00  9.307298     210002\n" +
                        "  -24.96699    -0.5854403E-14 0.0000000E+00 0.0000000E+00  12.98647     120002\n" +
                        "  -16.34242     -1.273781     0.0000000E+00 0.0000000E+00  8.436816     030002\n" +
                        "  0.0000000E+00 0.0000000E+00  1.332180    -0.2220446E-15 0.0000000E+00 200300\n" +
                        "  0.0000000E+00 0.0000000E+00  3.356401    -0.4440892E-15 0.0000000E+00 110300\n" +
                        "  0.0000000E+00 0.0000000E+00  3.449221    -0.3330669E-15 0.0000000E+00 020300\n" +
                        "  -5.046429     0.8881784E-15 0.0000000E+00 0.0000000E+00  3.079189     200201\n" +
                        "  -14.05653     0.1998401E-14 0.0000000E+00 0.0000000E+00  8.389079     110201\n" +
                        "  -11.46915    -0.4190856     0.0000000E+00 0.0000000E+00  7.829013     020201\n" +
                        "  0.0000000E+00 0.0000000E+00  3.079189     0.8881784E-15 0.0000000E+00 200102\n" +
                        "  0.0000000E+00 0.0000000E+00  8.389079     0.1776357E-14 0.0000000E+00 110102\n" +
                        "  0.0000000E+00 0.0000000E+00  7.829013     0.7771561E-15 0.0000000E+00 020102\n" +
                        "  -4.625786     0.4440892E-15 0.0000000E+00 0.0000000E+00  2.890753     200003\n" +
                        "  -13.84467     0.2717819E-15 0.0000000E+00 0.0000000E+00  8.384716     110003\n" +
                        "  -11.49959    -0.1688210     0.0000000E+00 0.0000000E+00  7.412155     020003\n" +
                        "  -1.257353     0.0000000E+00 0.0000000E+00 0.0000000E+00 0.8546060     100400\n" +
                        "  -2.242647    -0.4411765     0.0000000E+00 0.0000000E+00  1.546303     010400\n" +
                        "  0.0000000E+00 0.0000000E+00  3.418424    -0.4440892E-15 0.0000000E+00 100301\n" +
                        "  0.0000000E+00 0.0000000E+00  6.185211    -0.4440892E-15 0.0000000E+00 010301\n" +
                        "  -5.792969     0.0000000E+00 0.0000000E+00 0.0000000E+00  3.961669     100202\n" +
                        "  -10.82340     -1.273781     0.0000000E+00 0.0000000E+00  7.139592     010202\n" +
                        "  0.0000000E+00 0.0000000E+00  2.641113     0.0000000E+00 0.0000000E+00 100103\n" +
                        "  0.0000000E+00 0.0000000E+00  4.759728     0.0000000E+00 0.0000000E+00 010103\n" +
                        "  -2.685831     0.1776357E-14 0.0000000E+00 0.0000000E+00  1.866493     100004\n" +
                        "  -5.115962    -0.3459484     0.0000000E+00 0.0000000E+00  3.383387     010004\n" +
                        "  0.0000000E+00 0.0000000E+00  1.193750     0.0000000E+00 0.0000000E+00 000500\n" +
                        "  -1.791591    -0.2095428     0.0000000E+00 0.0000000E+00  2.029701     000401\n" +
                        "  0.0000000E+00 0.0000000E+00  4.059402     0.0000000E+00 0.0000000E+00 000302\n" +
                        "  -2.669082    -0.1688210     0.0000000E+00 0.0000000E+00  2.786676     000203\n" +
                        "  0.0000000E+00 0.0000000E+00  1.393338    -0.3330669E-15 0.0000000E+00 000104\n" +
                        " -0.7305543    -0.2492384E-01 0.0000000E+00 0.0000000E+00 0.7132020     000005"
        );
    }

    private CosyArbitraryOrder.CosyMapArbitraryOrder cosyMap左偏转弯铁切片(){
        return CosyArbitraryOrder.readMap(
                "  -1.176055     -1.176471     0.0000000E+00 0.0000000E+00 0.5587808     100000\n" +
                        " -0.3268164     -1.177232     0.0000000E+00 0.0000000E+00  1.034106     010000\n" +
                        "  0.0000000E+00 0.0000000E+00  1.000000     0.0000000E+00 0.0000000E+00 001000\n" +
                        "  0.0000000E+00 0.0000000E+00  3.335177      1.000000     0.0000000E+00 000100\n" +
                        "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00  1.000000     000010\n" +
                        "  -1.033547    -0.5587808     0.0000000E+00 0.0000000E+00 0.4976918     000001\n" +
                        "  0.5882352     0.0000000E+00 0.0000000E+00 0.0000000E+00-0.3864491     200000\n" +
                        "   1.177232     0.0000000E+00 0.0000000E+00 0.0000000E+00-0.7733986     110000\n" +
                        "   1.088820     0.5000000     0.0000000E+00 0.0000000E+00-0.9039205     020000\n" +
                        "  0.0000000E+00 0.0000000E+00-0.9999999     0.0000000E+00 0.0000000E+00 100100\n" +
                        "  0.0000000E+00 0.0000000E+00 -1.850647     0.0000000E+00 0.0000000E+00 010100\n" +
                        "   1.215744    -0.2220446E-15 0.0000000E+00 0.0000000E+00-0.5617730     100001\n" +
                        "   1.873920     0.6576212     0.0000000E+00 0.0000000E+00 -1.039956     010001\n" +
                        "  0.9248234     0.5000000     0.0000000E+00 0.0000000E+00-0.7961804     000200\n" +
                        "  0.0000000E+00 0.0000000E+00 -1.592361     0.2220446E-15 0.0000000E+00 000101\n" +
                        "  0.6247735     0.9733718E-01 0.0000000E+00 0.0000000E+00-0.5521670     000002\n" +
                        " -0.8136393    -0.3330669E-15 0.0000000E+00 0.0000000E+00 0.1288998     300000\n" +
                        "  -2.442498    -0.9995534E-15 0.0000000E+00 0.0000000E+00 0.3869498     210000\n" +
                        "  -2.944079    -0.9999063E-15 0.0000000E+00 0.0000000E+00 0.7156820     120000\n" +
                        "  -1.903779    -0.5884429     0.0000000E+00 0.0000000E+00 0.8164943     030000\n" +
                        "  0.0000000E+00 0.0000000E+00 0.6915934     0.0000000E+00 0.0000000E+00 200100\n" +
                        "  0.0000000E+00 0.0000000E+00  1.384082     0.0000000E+00 0.0000000E+00 110100\n" +
                        "  0.0000000E+00 0.0000000E+00  1.617666     0.0000000E+00 0.0000000E+00 020100\n" +
                        "  -1.488042     0.2220446E-15 0.0000000E+00 0.0000000E+00 0.7501844     200001\n" +
                        "  -3.635631     0.3186954E-15 0.0000000E+00 0.0000000E+00  1.933373     110001\n" +
                        "  -2.943999    -0.2793904     0.0000000E+00 0.0000000E+00  1.964640     020001\n" +
                        "  -1.087854     0.0000000E+00 0.0000000E+00 0.0000000E+00 0.6078722     100200\n" +
                        "  -1.676794    -0.5884429     0.0000000E+00 0.0000000E+00  1.125237     010200\n" +
                        "  0.0000000E+00 0.0000000E+00  1.215744    -0.2220446E-15 0.0000000E+00 100101\n" +
                        "  0.0000000E+00 0.0000000E+00  2.250473    -0.2221230E-15 0.0000000E+00 010101\n" +
                        "  -1.441760     0.0000000E+00 0.0000000E+00 0.0000000E+00 0.8525024     100002\n" +
                        "  -2.604118    -0.4820209     0.0000000E+00 0.0000000E+00  1.534710     010002\n" +
                        "  0.0000000E+00 0.0000000E+00  1.424853     0.0000000E+00 0.0000000E+00 000300\n" +
                        "  -1.312674    -0.2793904     0.0000000E+00 0.0000000E+00  1.323180     000201\n" +
                        "  0.0000000E+00 0.0000000E+00  1.323180    -0.2220446E-15 0.0000000E+00 000102\n" +
                        " -0.5912322    -0.5439015E-01 0.0000000E+00 0.0000000E+00 0.5724187     000003\n" +
                        "  0.2035416     0.0000000E+00 0.0000000E+00 0.0000000E+00-0.4011582     400000\n" +
                        "  0.8146933     0.0000000E+00 0.0000000E+00 0.0000000E+00 -1.605672     310000\n" +
                        "   2.260221     0.4440892E-15 0.0000000E+00 0.0000000E+00 -2.574414     220000\n" +
                        "   3.480314     0.8884919E-15 0.0000000E+00 0.0000000E+00 -2.323244     130000\n" +
                        "   2.062832     0.1250000     0.0000000E+00 0.0000000E+00 -1.292383     040000\n" +
                        "  0.0000000E+00 0.0000000E+00-0.2306805     0.0000000E+00 0.0000000E+00 300100\n" +
                        "  0.0000000E+00 0.0000000E+00-0.6924894     0.0000000E+00 0.0000000E+00 210100\n" +
                        "  0.0000000E+00 0.0000000E+00 -1.280792     0.2220446E-15 0.0000000E+00 120100\n" +
                        "  0.0000000E+00 0.0000000E+00 -1.461207     0.2221230E-15 0.0000000E+00 030100\n" +
                        "   1.750637    -0.4440892E-15 0.0000000E+00 0.0000000E+00-0.9511034     300001\n" +
                        "   6.619732    -0.6397656E-15 0.0000000E+00 0.0000000E+00 -3.071314     210001\n" +
                        "   9.533546    -0.2220446E-15 0.0000000E+00 0.0000000E+00 -4.110805     120001\n" +
                        "   6.539111     0.9864318     0.0000000E+00 0.0000000E+00 -3.040291     030001\n" +
                        "   1.331508     0.4440892E-15 0.0000000E+00 0.0000000E+00-0.7440210     200200\n" +
                        "   3.253182     0.8884919E-15 0.0000000E+00 0.0000000E+00 -1.875591     110200\n" +
                        "   2.634306     0.2500000     0.0000000E+00 0.0000000E+00 -1.928140     020200\n" +
                        "  0.0000000E+00 0.0000000E+00 -1.488042     0.0000000E+00 0.0000000E+00 200101\n" +
                        "  0.0000000E+00 0.0000000E+00 -3.751182     0.0000000E+00 0.0000000E+00 110101\n" +
                        "  0.0000000E+00 0.0000000E+00 -3.856281     0.1665335E-15 0.0000000E+00 020101\n" +
                        "   2.661844     0.0000000E+00 0.0000000E+00 0.0000000E+00 -1.544761     200002\n" +
                        "   7.472721    -0.2482361E-15 0.0000000E+00 0.0000000E+00 -4.246865     110002\n" +
                        "   6.102244     0.2047866     0.0000000E+00 0.0000000E+00 -3.923234     020002\n" +
                        "  0.0000000E+00 0.0000000E+00 -1.087854     0.2220446E-15 0.0000000E+00 100300\n" +
                        "  0.0000000E+00 0.0000000E+00 -2.013735     0.2221230E-15 0.0000000E+00 010300\n" +
                        "   2.809062     0.0000000E+00 0.0000000E+00 0.0000000E+00 -1.781427     100201\n" +
                        "   5.013137     0.9864318     0.0000000E+00 0.0000000E+00 -3.220010     010201\n" +
                        "  0.0000000E+00 0.0000000E+00 -1.781427     0.2220446E-15 0.0000000E+00 100102\n" +
                        "  0.0000000E+00 0.0000000E+00 -3.220010     0.3462411E-15 0.0000000E+00 010102\n" +
                        "   1.936762     0.0000000E+00 0.0000000E+00 0.0000000E+00 -1.265656     100003\n" +
                        "   3.639265     0.3973660     0.0000000E+00 0.0000000E+00 -2.281349     010003\n" +
                        "  0.5872940     0.1250000     0.0000000E+00 0.0000000E+00-0.6669377     000400\n" +
                        "  0.0000000E+00 0.0000000E+00 -2.667751     0.2220446E-15 0.0000000E+00 000301\n" +
                        "   1.863286     0.2047866     0.0000000E+00 0.0000000E+00 -1.954184     000202\n" +
                        "  0.0000000E+00 0.0000000E+00 -1.302790     0.1110223E-15 0.0000000E+00 000103\n" +
                        "  0.6358394     0.3512943E-01 0.0000000E+00 0.0000000E+00-0.6251828     000004\n" +
                        " -0.8446081     0.3330669E-15 0.0000000E+00 0.0000000E+00 0.8028362E-01 500000\n" +
                        "  -4.225774     0.1665922E-14 0.0000000E+00 0.0000000E+00 0.4016779     410000\n" +
                        "  -8.803040     0.3450959E-14 0.0000000E+00 0.0000000E+00  1.485845     320000\n" +
                        "  -10.72212     0.3188359E-14 0.0000000E+00 0.0000000E+00  3.045046     230000\n" +
                        "  -8.282643     0.1132866E-14 0.0000000E+00 0.0000000E+00  2.990069     140000\n" +
                        "  -3.674031    -0.4413322     0.0000000E+00 0.0000000E+00  1.519111     050000\n" +
                        "  0.0000000E+00 0.0000000E+00 0.7179170    -0.1110223E-15 0.0000000E+00 400100\n" +
                        "  0.0000000E+00 0.0000000E+00  2.873527    -0.4442460E-15 0.0000000E+00 310100\n" +
                        "  0.0000000E+00 0.0000000E+00  4.607198    -0.6666042E-15 0.0000000E+00 220100\n" +
                        "  0.0000000E+00 0.0000000E+00  4.157702    -0.4445597E-15 0.0000000E+00 130100\n" +
                        "  0.0000000E+00 0.0000000E+00  2.312863    -0.1111791E-15 0.0000000E+00 040100\n" +
                        "  -2.346996    -0.4440892E-15 0.0000000E+00 0.0000000E+00  1.227057     400001\n" +
                        "  -9.849164    -0.1897547E-14 0.0000000E+00 0.0000000E+00  5.808360     310001\n" +
                        "  -18.27814    -0.3472323E-14 0.0000000E+00 0.0000000E+00  11.36897     220001\n" +
                        "  -20.03517    -0.3500784E-14 0.0000000E+00 0.0000000E+00  11.80502     130001\n" +
                        "  -10.31664    -0.2095428     0.0000000E+00 0.0000000E+00  6.148737     040001\n" +
                        "  -1.566480     0.2220446E-15 0.0000000E+00 0.0000000E+00 0.8753187     300200\n" +
                        "  -5.923371     0.1665922E-15 0.0000000E+00 0.0000000E+00  2.821074     210200\n" +
                        "  -8.530666     0.1107871E-15 0.0000000E+00 0.0000000E+00  3.813102     120200\n" +
                        "  -5.851231    -0.8826644     0.0000000E+00 0.0000000E+00  2.874179     030200\n" +
                        "  0.0000000E+00 0.0000000E+00  1.750637    -0.2220446E-15 0.0000000E+00 300101\n" +
                        "  0.0000000E+00 0.0000000E+00  5.642148    -0.6663690E-15 0.0000000E+00 210101\n" +
                        "  0.0000000E+00 0.0000000E+00  7.626204    -0.4445596E-15 0.0000000E+00 120101\n" +
                        "  0.0000000E+00 0.0000000E+00  5.748358    -0.2353364E-15 0.0000000E+00 030101\n" +
                        "  -4.382835     0.0000000E+00 0.0000000E+00 0.0000000E+00  2.516542     300002\n" +
                        "  -17.09281     0.3353181E-15 0.0000000E+00 0.0000000E+00  9.307889     210002\n" +
                        "  -24.97901     0.6524562E-15 0.0000000E+00 0.0000000E+00  12.99315     120002\n" +
                        "  -16.35391     -1.274230     0.0000000E+00 0.0000000E+00  8.442808     030002\n" +
                        "  0.0000000E+00 0.0000000E+00  1.331508     0.2220446E-15 0.0000000E+00 200300\n" +
                        "  0.0000000E+00 0.0000000E+00  3.356577     0.4442460E-15 0.0000000E+00 110300\n" +
                        "  0.0000000E+00 0.0000000E+00  3.450620     0.1111791E-15 0.0000000E+00 020300\n" +
                        "  -5.043799    -0.8881784E-15 0.0000000E+00 0.0000000E+00  3.077589     200201\n" +
                        "  -14.05769    -0.2252127E-14 0.0000000E+00 0.0000000E+00  8.389444     110201\n" +
                        "  -11.47487    -0.4190856     0.0000000E+00 0.0000000E+00  7.832381     020201\n" +
                        "  0.0000000E+00 0.0000000E+00  3.077589     0.0000000E+00 0.0000000E+00 200102\n" +
                        "  0.0000000E+00 0.0000000E+00  8.389444     0.0000000E+00 0.0000000E+00 110102\n" +
                        "  0.0000000E+00 0.0000000E+00  7.832381    -0.1110223E-15 0.0000000E+00 020102\n" +
                        "  -4.623280    -0.4440892E-15 0.0000000E+00 0.0000000E+00  2.889195     200003\n" +
                        "  -13.84509    -0.1108563E-14 0.0000000E+00 0.0000000E+00  8.384887     110003\n" +
                        "  -11.50507    -0.1688210     0.0000000E+00 0.0000000E+00  7.415439     020003\n" +
                        "  -1.256782     0.0000000E+00 0.0000000E+00 0.0000000E+00 0.8542335     100400\n" +
                        "  -2.242891    -0.4413322     0.0000000E+00 0.0000000E+00  1.546558     010400\n" +
                        "  0.0000000E+00 0.0000000E+00  3.416934     0.0000000E+00 0.0000000E+00 100301\n" +
                        "  0.0000000E+00 0.0000000E+00  6.186232    -0.2351796E-15 0.0000000E+00 010301\n" +
                        "  -5.790070     0.1776357E-14 0.0000000E+00 0.0000000E+00  3.959738     100202\n" +
                        "  -10.82401     -1.274230     0.0000000E+00 0.0000000E+00  7.140273     010202\n" +
                        "  0.0000000E+00 0.0000000E+00  2.639826     0.4440892E-15 0.0000000E+00 100103\n" +
                        "  0.0000000E+00 0.0000000E+00  4.760182     0.3201279E-15 0.0000000E+00 010103\n" +
                        "  -2.684406     0.4440892E-15 0.0000000E+00 0.0000000E+00  1.865522     100004\n" +
                        "  -5.116045    -0.3460705     0.0000000E+00 0.0000000E+00  3.383532     010004\n" +
                        "  0.0000000E+00 0.0000000E+00  1.193559     0.0000000E+00 0.0000000E+00 000500\n" +
                        "  -1.790839    -0.2095428     0.0000000E+00 0.0000000E+00  2.029248     000401\n" +
                        "  0.0000000E+00 0.0000000E+00  4.058496    -0.4440892E-15 0.0000000E+00 000302\n" +
                        "  -2.667826    -0.1688210     0.0000000E+00 0.0000000E+00  2.785854     000203\n" +
                        "  0.0000000E+00 0.0000000E+00  1.392927    -0.2220446E-15 0.0000000E+00 000104\n" +
                        " -0.7301840    -0.2492384E-01 0.0000000E+00 0.0000000E+00 0.7129431     000005"
        );
    }

    private CosyArbitraryOrder.CosyMapArbitraryOrder cosyMap右偏n1弯铁(){
        return CosyArbitraryOrder.readMap(
                "   21.83214      16.50939     0.0000000E+00 0.0000000E+00-0.9737635     100000\n" +
                        "   28.81041      21.83214     0.0000000E+00 0.0000000E+00 -1.228730     010000\n" +
                        "  0.0000000E+00 0.0000000E+00 -2.826734     -2.035484     0.0000000E+00 001000\n" +
                        "  0.0000000E+00 0.0000000E+00 -3.434281     -2.826734     0.0000000E+00 000100\n" +
                        "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00  1.000000     000010\n" +
                        "   1.228730     0.9737635     0.0000000E+00 0.0000000E+00 0.5030512     000001\n" +
                        "   101.6812      74.95521     0.0000000E+00 0.0000000E+00 -89.76876     200000\n" +
                        "   257.4702      189.1624     0.0000000E+00 0.0000000E+00 -237.5550     110000\n" +
                        "   162.4426      118.9159     0.0000000E+00 0.0000000E+00 -157.4643     020000\n" +
                        "  0.0000000E+00 0.0000000E+00  11.73127      15.27843     0.0000000E+00 101000\n" +
                        "  0.0000000E+00 0.0000000E+00  14.80295      19.27887     0.0000000E+00 011000\n" +
                        "  -12.18254     -9.654611     0.0000000E+00 0.0000000E+00 -2.224661     002000\n" +
                        "  0.0000000E+00 0.0000000E+00  6.805247      11.73127     0.0000000E+00 100100\n" +
                        "  0.0000000E+00 0.0000000E+00  8.587106      14.80295     0.0000000E+00 010100\n" +
                        "  -24.36507     -19.30922     0.0000000E+00 0.0000000E+00 -5.244812     001100\n" +
                        "  -12.94460     -2.198474     0.0000000E+00 0.0000000E+00 -9.733459     100001\n" +
                        "  -31.50713     -13.78216     0.0000000E+00 0.0000000E+00 -12.28203     010001\n" +
                        "  0.0000000E+00 0.0000000E+00-0.4545412     -1.901323     0.0000000E+00 001001\n" +
                        "  -13.28201     -10.52594     0.0000000E+00 0.0000000E+00 -3.530155     000200\n" +
                        "  0.0000000E+00 0.0000000E+00  1.945457    -0.4545412     0.0000000E+00 000101\n" +
                        "  -1.246520    -0.4928849     0.0000000E+00 0.0000000E+00-0.7155900     000002\n" +
                        "   3208.605      580.8172     0.0000000E+00 0.0000000E+00 -831.8253     300000\n" +
                        "   12691.26      2276.574     0.0000000E+00 0.0000000E+00 -3204.503     210000\n" +
                        "   16736.07      2976.474     0.0000000E+00 0.0000000E+00 -4109.685     120000\n" +
                        "   7369.893      1307.063     0.0000000E+00 0.0000000E+00 -1754.943     030000\n" +
                        "  0.0000000E+00 0.0000000E+00 -168.5572      149.4468     0.0000000E+00 201000\n" +
                        "  0.0000000E+00 0.0000000E+00 -450.5008      391.9470     0.0000000E+00 111000\n" +
                        "  0.0000000E+00 0.0000000E+00 -300.7038      257.1171     0.0000000E+00 021000\n" +
                        "   40.25147      1.652680     0.0000000E+00 0.0000000E+00  116.4167     102000\n" +
                        "   61.24754      8.244628     0.0000000E+00 0.0000000E+00  152.9730     012000\n" +
                        "  0.0000000E+00 0.0000000E+00  5.709533      12.65506     0.0000000E+00 003000\n" +
                        "  0.0000000E+00 0.0000000E+00 -282.7890      158.3969     0.0000000E+00 200100\n" +
                        "  0.0000000E+00 0.0000000E+00 -753.3826      414.2322     0.0000000E+00 110100\n" +
                        "  0.0000000E+00 0.0000000E+00 -502.9631      269.8614     0.0000000E+00 020100\n" +
                        "   102.4253     -2.193817     0.0000000E+00 0.0000000E+00  234.3102     101100\n" +
                        "   148.6279      7.071560     0.0000000E+00 0.0000000E+00  307.9555     011100\n" +
                        "  0.0000000E+00 0.0000000E+00  8.741475      35.29098     0.0000000E+00 002100\n" +
                        "   381.1119      2.825721     0.0000000E+00 0.0000000E+00  99.47514     200001\n" +
                        "   889.8002     -83.63747     0.0000000E+00 0.0000000E+00  387.0270     110001\n" +
                        "   518.1261     -109.3586     0.0000000E+00 0.0000000E+00  338.4676     020001\n" +
                        "  0.0000000E+00 0.0000000E+00 -47.56344    -0.4939096     0.0000000E+00 101001\n" +
                        "  0.0000000E+00 0.0000000E+00 -69.32089     -9.938023     0.0000000E+00 011001\n" +
                        "   15.27775      5.221891     0.0000000E+00 0.0000000E+00  6.169106     002001\n" +
                        "   64.59891     -5.835030     0.0000000E+00 0.0000000E+00  125.2667     100200\n" +
                        "   103.0665      5.639533     0.0000000E+00 0.0000000E+00  164.3176     010200\n" +
                        "  0.0000000E+00 0.0000000E+00-0.2367493      33.96409     0.0000000E+00 001200\n" +
                        "  0.0000000E+00 0.0000000E+00 -64.22474     -8.994364     0.0000000E+00 100101\n" +
                        "  0.0000000E+00 0.0000000E+00 -87.92120     -18.31685     0.0000000E+00 010101\n" +
                        "   45.46327      20.90905     0.0000000E+00 0.0000000E+00  15.71612     001101\n" +
                        "   37.19441      2.762382     0.0000000E+00 0.0000000E+00  20.59562     100002\n" +
                        "   68.11329      12.95904     0.0000000E+00 0.0000000E+00  33.24365     010002\n" +
                        "  0.0000000E+00 0.0000000E+00-0.3598293E-01 0.8831114     0.0000000E+00 001002\n" +
                        "  0.0000000E+00 0.0000000E+00 -5.135412      10.13565     0.0000000E+00 000300\n" +
                        "   32.10747      16.51923     0.0000000E+00 0.0000000E+00  11.41686     000201\n" +
                        "  0.0000000E+00 0.0000000E+00 -1.929058      1.101466     0.0000000E+00 000102\n" +
                        "   1.782875     0.4214437     0.0000000E+00 0.0000000E+00  1.084488     000003\n" +
                        "   41598.25      6159.777     0.0000000E+00 0.0000000E+00 -24911.53     400000\n" +
                        "   215357.3      32059.81     0.0000000E+00 0.0000000E+00 -131017.0     310000\n" +
                        "   417832.3      62558.65     0.0000000E+00 0.0000000E+00 -258432.6     220000\n" +
                        "   360167.0      54319.85     0.0000000E+00 0.0000000E+00 -226691.3     130000\n" +
                        "   116407.8      17730.94     0.0000000E+00 0.0000000E+00 -74644.67     040000\n" +
                        "  0.0000000E+00 0.0000000E+00  517.6548      1235.982     0.0000000E+00 301000\n" +
                        "  0.0000000E+00 0.0000000E+00  2181.936      4822.709     0.0000000E+00 211000\n" +
                        "  0.0000000E+00 0.0000000E+00  3058.597      6268.257     0.0000000E+00 121000\n" +
                        "  0.0000000E+00 0.0000000E+00  1432.480      2721.970     0.0000000E+00 031000\n" +
                        "  -4942.157     -260.3438     0.0000000E+00 0.0000000E+00  37.01462     202000\n" +
                        "  -13056.67     -687.2411     0.0000000E+00 0.0000000E+00 -20.28608     112000\n" +
                        "  -8627.983     -456.6050     0.0000000E+00 0.0000000E+00 -92.37624     022000\n" +
                        "  0.0000000E+00 0.0000000E+00  463.3471     -19.68200     0.0000000E+00 103000\n" +
                        "  0.0000000E+00 0.0000000E+00  603.7431     -27.74116     0.0000000E+00 013000\n" +
                        "  -50.09932     -20.11243     0.0000000E+00 0.0000000E+00 -20.94021     004000\n" +
                        "  0.0000000E+00 0.0000000E+00 -980.9743      1404.107     0.0000000E+00 300100\n" +
                        "  0.0000000E+00 0.0000000E+00 -3624.699      5466.603     0.0000000E+00 210100\n" +
                        "  0.0000000E+00 0.0000000E+00 -4428.763      7096.938     0.0000000E+00 120100\n" +
                        "  0.0000000E+00 0.0000000E+00 -1783.127      3078.237     0.0000000E+00 030100\n" +
                        "  -9819.646     -523.8969     0.0000000E+00 0.0000000E+00 -163.8082     201100\n" +
                        "  -25979.36     -1401.241     0.0000000E+00 0.0000000E+00 -652.6159     111100\n" +
                        "  -17203.38     -951.8817     0.0000000E+00 0.0000000E+00 -580.3057     021100\n" +
                        "  0.0000000E+00 0.0000000E+00  1565.340     -58.55203     0.0000000E+00 102100\n" +
                        "  0.0000000E+00 0.0000000E+00  2037.409     -85.98880     0.0000000E+00 012100\n" +
                        "  -212.8540     -76.96140     0.0000000E+00 0.0000000E+00 -87.99621     003100\n" +
                        "  -1548.089     -114.4972     0.0000000E+00 0.0000000E+00 -3027.168     300001\n" +
                        "  -12931.34     -1602.571     0.0000000E+00 0.0000000E+00 -10577.67     210001\n" +
                        "  -26044.64     -3605.848     0.0000000E+00 0.0000000E+00 -12226.69     120001\n" +
                        "  -15436.67     -2248.344     0.0000000E+00 0.0000000E+00 -4676.896     030001\n" +
                        "  0.0000000E+00 0.0000000E+00  272.7692     -32.09090     0.0000000E+00 201001\n" +
                        "  0.0000000E+00 0.0000000E+00  950.8262     -282.8962     0.0000000E+00 111001\n" +
                        "  0.0000000E+00 0.0000000E+00  783.6416     -315.7195     0.0000000E+00 021001\n" +
                        "  -577.7177     -37.96516     0.0000000E+00 0.0000000E+00 -286.4188     102001\n" +
                        "  -802.1001     -58.68768     0.0000000E+00 0.0000000E+00 -457.6667     012001\n" +
                        "  0.0000000E+00 0.0000000E+00-0.2689461E-01 -9.779749     0.0000000E+00 003001\n" +
                        "  -5248.071     -310.9702     0.0000000E+00 0.0000000E+00 -211.1072     200200\n" +
                        "  -13797.86     -757.6883     0.0000000E+00 0.0000000E+00 -767.1857     110200\n" +
                        "  -9091.497     -475.4950     0.0000000E+00 0.0000000E+00 -649.3257     020200\n" +
                        "  0.0000000E+00 0.0000000E+00  1764.586     -64.95019     0.0000000E+00 101200\n" +
                        "  0.0000000E+00 0.0000000E+00  2302.480     -89.08432     0.0000000E+00 011200\n" +
                        "  -346.1354     -112.4521     0.0000000E+00 0.0000000E+00 -146.9110     002200\n" +
                        "  0.0000000E+00 0.0000000E+00  485.7032     -139.8407     0.0000000E+00 200101\n" +
                        "  0.0000000E+00 0.0000000E+00  1679.055     -577.3507     0.0000000E+00 110101\n" +
                        "  0.0000000E+00 0.0000000E+00  1376.970     -515.0961     0.0000000E+00 020101\n" +
                        "  -1237.050     -76.18630     0.0000000E+00 0.0000000E+00 -752.3237     101101\n" +
                        "  -1730.574     -118.2582     0.0000000E+00 0.0000000E+00 -1150.538     011101\n" +
                        "  0.0000000E+00 0.0000000E+00  1.198731     -51.75119     0.0000000E+00 002101\n" +
                        "  -925.1598     -31.86872     0.0000000E+00 0.0000000E+00 -466.5512     200002\n" +
                        "  -2887.116     -25.89070     0.0000000E+00 0.0000000E+00 -1466.103     110002\n" +
                        "  -2172.491      44.46450     0.0000000E+00 0.0000000E+00 -1166.489     020002\n" +
                        "  0.0000000E+00 0.0000000E+00  61.77508     -5.780446     0.0000000E+00 101002\n" +
                        "  0.0000000E+00 0.0000000E+00  116.7656    -0.5515484     0.0000000E+00 011002\n" +
                        "  -30.95574     -6.006117     0.0000000E+00 0.0000000E+00 -16.00300     002002\n" +
                        "  0.0000000E+00 0.0000000E+00  677.0376     -18.16719     0.0000000E+00 100300\n" +
                        "  0.0000000E+00 0.0000000E+00  885.0326     -22.83604     0.0000000E+00 010300\n" +
                        "  -267.4291     -83.82240     0.0000000E+00 0.0000000E+00 -116.2869     001300\n" +
                        "  -727.5387     -36.36990     0.0000000E+00 0.0000000E+00 -493.5882     100201\n" +
                        "  -1046.906     -71.48214     0.0000000E+00 0.0000000E+00 -739.2935     010201\n" +
                        "  0.0000000E+00 0.0000000E+00  14.72196     -72.55790     0.0000000E+00 001201\n" +
                        "  0.0000000E+00 0.0000000E+00  124.4747    -0.2079596     0.0000000E+00 100102\n" +
                        "  0.0000000E+00 0.0000000E+00  207.5103      9.836737     0.0000000E+00 010102\n" +
                        "  -91.48618     -25.61743     0.0000000E+00 0.0000000E+00 -42.20455     001102\n" +
                        "  -84.79062     -4.687141     0.0000000E+00 0.0000000E+00 -46.81222     100003\n" +
                        "  -151.3515     -15.52803     0.0000000E+00 0.0000000E+00 -79.79188     010003\n" +
                        "  0.0000000E+00 0.0000000E+00 0.7324325    -0.6063834     0.0000000E+00 001003\n" +
                        "  -84.66088     -28.48245     0.0000000E+00 0.0000000E+00 -37.47094     000400\n" +
                        "  0.0000000E+00 0.0000000E+00  17.64740     -30.52056     0.0000000E+00 000301\n" +
                        "  -69.43175     -23.37541     0.0000000E+00 0.0000000E+00 -30.70027     000202\n" +
                        "  0.0000000E+00 0.0000000E+00  3.110264     -1.306142     0.0000000E+00 000103\n" +
                        "  -2.951361    -0.4224939     0.0000000E+00 0.0000000E+00 -1.799808     000004\n" +
                        "   998971.2      80836.94     0.0000000E+00 0.0000000E+00 -400582.4     500000\n" +
                        "   6546745.      527304.3     0.0000000E+00 0.0000000E+00 -2601699.     410000\n" +
                        "  0.1716328E+08  1375898.     0.0000000E+00 0.0000000E+00 -6756428.     320000\n" +
                        "  0.2250563E+08  1796076.     0.0000000E+00 0.0000000E+00 -8770828.     230000\n" +
                        "  0.1476399E+08  1173566.     0.0000000E+00 0.0000000E+00 -5692233.     140000\n" +
                        "   3877370.      307231.3     0.0000000E+00 0.0000000E+00 -1477694.     050000\n" +
                        "  0.0000000E+00 0.0000000E+00 -34228.50      13770.80     0.0000000E+00 401000\n" +
                        "  0.0000000E+00 0.0000000E+00 -183447.6      71976.46     0.0000000E+00 311000\n" +
                        "  0.0000000E+00 0.0000000E+00 -368644.4      141075.2     0.0000000E+00 221000\n" +
                        "  0.0000000E+00 0.0000000E+00 -329385.8      123054.5     0.0000000E+00 131000\n" +
                        "  0.0000000E+00 0.0000000E+00 -110473.6      40356.98     0.0000000E+00 041000\n" +
                        "  -31883.13     -2949.408     0.0000000E+00 0.0000000E+00  47854.94     302000\n" +
                        "  -117852.2     -10918.00     0.0000000E+00 0.0000000E+00  189488.2     212000\n" +
                        "  -144356.5     -13402.26     0.0000000E+00 0.0000000E+00  250188.3     122000\n" +
                        "  -58511.54     -5448.681     0.0000000E+00 0.0000000E+00  110213.1     032000\n" +
                        "  0.0000000E+00 0.0000000E+00 -870.2503     -232.6184     0.0000000E+00 203000\n" +
                        "  0.0000000E+00 0.0000000E+00 -2414.828     -498.0525     0.0000000E+00 113000\n" +
                        "  0.0000000E+00 0.0000000E+00 -1679.746     -255.9035     0.0000000E+00 023000\n" +
                        "   2694.898      188.0512     0.0000000E+00 0.0000000E+00  848.0553     104000\n" +
                        "   3590.402      272.6556     0.0000000E+00 0.0000000E+00  1154.902     014000\n" +
                        "  0.0000000E+00 0.0000000E+00  4.333367      44.65249     0.0000000E+00 005000\n" +
                        "  0.0000000E+00 0.0000000E+00 -69784.83      16515.91     0.0000000E+00 400100\n" +
                        "  0.0000000E+00 0.0000000E+00 -370037.4      86255.97     0.0000000E+00 310100\n" +
                        "  0.0000000E+00 0.0000000E+00 -735972.8      169013.6     0.0000000E+00 220100\n" +
                        "  0.0000000E+00 0.0000000E+00 -651033.3      147432.9     0.0000000E+00 130100\n" +
                        "  0.0000000E+00 0.0000000E+00 -216220.0      48363.63     0.0000000E+00 040100\n" +
                        "  -56592.66     -5470.062     0.0000000E+00 0.0000000E+00  94282.80     301100\n" +
                        "  -208348.0     -20415.90     0.0000000E+00 0.0000000E+00  373900.1     211100\n" +
                        "  -253949.8     -25292.18     0.0000000E+00 0.0000000E+00  494540.1     121100\n" +
                        "  -102283.5     -10387.04     0.0000000E+00 0.0000000E+00  218284.7     031100\n" +
                        "  0.0000000E+00 0.0000000E+00 -2692.255     -489.8918     0.0000000E+00 202100\n" +
                        "  0.0000000E+00 0.0000000E+00 -7651.357     -983.1355     0.0000000E+00 112100\n" +
                        "  0.0000000E+00 0.0000000E+00 -5424.279     -434.3730     0.0000000E+00 022100\n" +
                        "   10944.06      766.2264     0.0000000E+00 0.0000000E+00  3923.664     103100\n" +
                        "   14574.15      1102.577     0.0000000E+00 0.0000000E+00  5295.920     013100\n" +
                        "  0.0000000E+00 0.0000000E+00 0.9771288      231.5672     0.0000000E+00 004100\n" +
                        "   115698.2      952.2676     0.0000000E+00 0.0000000E+00  7198.818     400001\n" +
                        "   511000.9     -10525.60     0.0000000E+00 0.0000000E+00  108986.4     310001\n" +
                        "   823000.4     -50694.58     0.0000000E+00 0.0000000E+00  355603.1     220001\n" +
                        "   566400.5     -70178.04     0.0000000E+00 0.0000000E+00  435655.3     130001\n" +
                        "   137632.9     -31444.21     0.0000000E+00 0.0000000E+00  184341.4     040001\n" +
                        "  0.0000000E+00 0.0000000E+00 -14424.72      206.5674     0.0000000E+00 301001\n" +
                        "  0.0000000E+00 0.0000000E+00 -58545.17     -1534.541     0.0000000E+00 211001\n" +
                        "  0.0000000E+00 0.0000000E+00 -79338.93     -5032.731     0.0000000E+00 121001\n" +
                        "  0.0000000E+00 0.0000000E+00 -35933.58     -3506.297     0.0000000E+00 031001\n" +
                        "   12089.14      582.2552     0.0000000E+00 0.0000000E+00  7570.695     202001\n" +
                        "   39085.18      1933.731     0.0000000E+00 0.0000000E+00  20273.80     112001\n" +
                        "   30550.00      1543.388     0.0000000E+00 0.0000000E+00  13654.80     022001\n" +
                        "  0.0000000E+00 0.0000000E+00 -597.4369      106.3730     0.0000000E+00 103001\n" +
                        "  0.0000000E+00 0.0000000E+00 -1093.403      152.6074     0.0000000E+00 013001\n" +
                        "   200.3881      40.60054     0.0000000E+00 0.0000000E+00  101.2734     004001\n" +
                        "  -27306.98     -3128.986     0.0000000E+00 0.0000000E+00  49573.02     300200\n" +
                        "  -94906.37     -10900.51     0.0000000E+00 0.0000000E+00  195588.9     210200\n" +
                        "  -107650.0     -12439.75     0.0000000E+00 0.0000000E+00  257527.0     120200\n" +
                        "  -39476.65     -4606.987     0.0000000E+00 0.0000000E+00  113226.2     030200\n" +
                        "  0.0000000E+00 0.0000000E+00 -3146.223     -381.3797     0.0000000E+00 201200\n" +
                        "  0.0000000E+00 0.0000000E+00 -9228.654     -564.2792     0.0000000E+00 111200\n" +
                        "  0.0000000E+00 0.0000000E+00 -6716.603     -53.17757     0.0000000E+00 021200\n" +
                        "   17220.53      1199.524     0.0000000E+00 0.0000000E+00  6829.381     102200\n" +
                        "   22946.89      1717.400     0.0000000E+00 0.0000000E+00  9221.009     012200\n" +
                        "  0.0000000E+00 0.0000000E+00 -66.88084      485.2354     0.0000000E+00 003200\n" +
                        "  0.0000000E+00 0.0000000E+00 -17661.72     -640.8115     0.0000000E+00 300101\n" +
                        "  0.0000000E+00 0.0000000E+00 -68835.37     -5137.750     0.0000000E+00 210101\n" +
                        "  0.0000000E+00 0.0000000E+00 -89802.21     -10097.65     0.0000000E+00 120101\n" +
                        "  0.0000000E+00 0.0000000E+00 -39269.60     -5867.977     0.0000000E+00 030101\n" +
                        "   31297.92      1447.097     0.0000000E+00 0.0000000E+00  15618.09     201101\n" +
                        "   96840.26      4636.210     0.0000000E+00 0.0000000E+00  42229.98     111101\n" +
                        "   73401.62      3644.495     0.0000000E+00 0.0000000E+00  28699.96     021101\n" +
                        "  0.0000000E+00 0.0000000E+00 -3255.705      364.7723     0.0000000E+00 102101\n" +
                        "  0.0000000E+00 0.0000000E+00 -5301.952      526.0534     0.0000000E+00 012101\n" +
                        "   947.0101      207.3737     0.0000000E+00 0.0000000E+00  463.4719     003101\n" +
                        "   17422.56      258.3382     0.0000000E+00 0.0000000E+00  9803.827     300002\n" +
                        "   78240.42      2133.306     0.0000000E+00 0.0000000E+00  44455.13     210002\n" +
                        "   120306.1      5024.776     0.0000000E+00 0.0000000E+00  65450.41     120002\n" +
                        "   62613.09      3526.244     0.0000000E+00 0.0000000E+00  31547.93     030002\n" +
                        "  0.0000000E+00 0.0000000E+00 -1698.978      28.35506     0.0000000E+00 201002\n" +
                        "  0.0000000E+00 0.0000000E+00 -5009.029      261.8074     0.0000000E+00 111002\n" +
                        "  0.0000000E+00 0.0000000E+00 -3733.865      360.4725     0.0000000E+00 021002\n" +
                        "   1732.270      99.15062     0.0000000E+00 0.0000000E+00  893.7594     102002\n" +
                        "   2714.461      165.7260     0.0000000E+00 0.0000000E+00  1435.038     012002\n" +
                        "  0.0000000E+00 0.0000000E+00 -14.40034      8.108087     0.0000000E+00 003002\n" +
                        "  0.0000000E+00 0.0000000E+00 -1400.046     -44.26738     0.0000000E+00 200300\n" +
                        "  0.0000000E+00 0.0000000E+00 -4334.558      140.8190     0.0000000E+00 110300\n" +
                        "  0.0000000E+00 0.0000000E+00 -3295.297      275.0288     0.0000000E+00 020300\n" +
                        "   12443.45      855.8833     0.0000000E+00 0.0000000E+00  5428.138     101300\n" +
                        "   16623.98      1223.965     0.0000000E+00 0.0000000E+00  7368.704     011300\n" +
                        "  0.0000000E+00 0.0000000E+00 -166.5280      532.5640     0.0000000E+00 002300\n" +
                        "   20272.71      921.4153     0.0000000E+00 0.0000000E+00  8844.300     200201\n" +
                        "   61379.36      2796.724     0.0000000E+00 0.0000000E+00  24380.29     110201\n" +
                        "   45742.94      2091.113     0.0000000E+00 0.0000000E+00  16935.55     020201\n" +
                        "  0.0000000E+00 0.0000000E+00 -5051.851      419.0312     0.0000000E+00 101201\n" +
                        "  0.0000000E+00 0.0000000E+00 -7807.410      599.6574     0.0000000E+00 011201\n" +
                        "   1711.105      378.7960     0.0000000E+00 0.0000000E+00  820.4646     002201\n" +
                        "  0.0000000E+00 0.0000000E+00 -2469.884      141.7137     0.0000000E+00 200102\n" +
                        "  0.0000000E+00 0.0000000E+00 -7476.878      712.9781     0.0000000E+00 110102\n" +
                        "  0.0000000E+00 0.0000000E+00 -5711.954      760.4934     0.0000000E+00 020102\n" +
                        "   4283.078      243.2279     0.0000000E+00 0.0000000E+00  2331.319     101102\n" +
                        "   6583.669      398.7707     0.0000000E+00 0.0000000E+00  3703.795     011102\n" +
                        "  0.0000000E+00 0.0000000E+00 -61.65059      60.38830     0.0000000E+00 002102\n" +
                        "   2600.258      72.39536     0.0000000E+00 0.0000000E+00  1450.723     200003\n" +
                        "   8456.514      189.6723     0.0000000E+00 0.0000000E+00  4655.243     110003\n" +
                        "   6773.820      91.91539     0.0000000E+00 0.0000000E+00  3733.054     020003\n" +
                        "  0.0000000E+00 0.0000000E+00 -115.9416      8.239392     0.0000000E+00 101003\n" +
                        "  0.0000000E+00 0.0000000E+00 -218.2831      8.819453     0.0000000E+00 011003\n" +
                        "   68.61763      7.794101     0.0000000E+00 0.0000000E+00  37.47870     002003\n" +
                        "   3506.459      233.1036     0.0000000E+00 0.0000000E+00  1682.628     100400\n" +
                        "   4715.504      339.2481     0.0000000E+00 0.0000000E+00  2303.639     010400\n" +
                        "  0.0000000E+00 0.0000000E+00 -160.0085      310.3251     0.0000000E+00 001400\n" +
                        "  0.0000000E+00 0.0000000E+00 -2479.063      152.3728     0.0000000E+00 100301\n" +
                        "  0.0000000E+00 0.0000000E+00 -3726.121      209.7860     0.0000000E+00 010301\n" +
                        "   1431.228      321.6454     0.0000000E+00 0.0000000E+00  678.1132     001301\n" +
                        "   2781.640      148.3604     0.0000000E+00 0.0000000E+00  1606.656     100202\n" +
                        "   4279.678      259.8458     0.0000000E+00 0.0000000E+00  2533.011     010202\n" +
                        "  0.0000000E+00 0.0000000E+00 -99.02519      109.0752     0.0000000E+00 001202\n" +
                        "  0.0000000E+00 0.0000000E+00 -247.3093      9.247815     0.0000000E+00 100103\n" +
                        "  0.0000000E+00 0.0000000E+00 -437.8108      4.868178     0.0000000E+00 010103\n" +
                        "   197.1247      33.24300     0.0000000E+00 0.0000000E+00  102.6660     001103\n" +
                        "   185.4687      7.868120     0.0000000E+00 0.0000000E+00  106.5061     100004\n" +
                        "   331.3759      21.31213     0.0000000E+00 0.0000000E+00  185.0425     010004\n" +
                        "  0.0000000E+00 0.0000000E+00 -1.741251     0.5019906     0.0000000E+00 001004\n" +
                        "  0.0000000E+00 0.0000000E+00 -59.03688      75.94995     0.0000000E+00 000500\n" +
                        "   478.2871      115.3442     0.0000000E+00 0.0000000E+00  225.0382     000401\n" +
                        "  0.0000000E+00 0.0000000E+00 -63.15792      57.29984     0.0000000E+00 000302\n" +
                        "   150.5451      32.47014     0.0000000E+00 0.0000000E+00  75.90477     000203\n" +
                        "  0.0000000E+00 0.0000000E+00 -5.659722      1.465925     0.0000000E+00 000104\n" +
                        "   5.200515     0.4742875     0.0000000E+00 0.0000000E+00  3.191733     000005"
        );
    }

    private CosyArbitraryOrder.CosyMapArbitraryOrder cosyMap右偏n1弯铁切片(){
        return CosyArbitraryOrder.readMap(
                "   21.83482      16.51886     0.0000000E+00 0.0000000E+00-0.9743222     100000\n" +
                        "   28.82283      21.85135     0.0000000E+00 0.0000000E+00 -1.229863     010000\n" +
                        "  0.0000000E+00 0.0000000E+00 -2.824333     -2.033919     0.0000000E+00 001000\n" +
                        "  0.0000000E+00 0.0000000E+00 -3.433080     -2.826368     0.0000000E+00 000100\n" +
                        "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00  1.000000     000010\n" +
                        "   1.228888     0.9743222     0.0000000E+00 0.0000000E+00 0.5030244     000001\n" +
                        "   101.7478      75.03617     0.0000000E+00 0.0000000E+00 -89.82764     200000\n" +
                        "   257.7280      189.4326     0.0000000E+00 0.0000000E+00 -237.7833     110000\n" +
                        "   162.6623      119.1278     0.0000000E+00 0.0000000E+00 -157.6634     020000\n" +
                        "  0.0000000E+00 0.0000000E+00  11.73873      15.29420     0.0000000E+00 101000\n" +
                        "  0.0000000E+00 0.0000000E+00  14.81751      19.30550     0.0000000E+00 011000\n" +
                        "  -12.18411     -9.660151     0.0000000E+00 0.0000000E+00 -2.222142     002000\n" +
                        "  0.0000000E+00 0.0000000E+00  6.818888      11.75403     0.0000000E+00 100100\n" +
                        "  0.0000000E+00 0.0000000E+00  8.607314      14.83683     0.0000000E+00 010100\n" +
                        "  -24.37823     -19.32824     0.0000000E+00 0.0000000E+00 -5.240951     001100\n" +
                        "  -12.94351     -2.198550     0.0000000E+00 0.0000000E+00 -9.739719     100001\n" +
                        "  -31.51781     -13.79327     0.0000000E+00 0.0000000E+00 -12.29366     010001\n" +
                        "  0.0000000E+00 0.0000000E+00-0.4553897     -1.901514     0.0000000E+00 001001\n" +
                        "  -13.29374     -10.53992     0.0000000E+00 0.0000000E+00 -3.529320     000200\n" +
                        "  0.0000000E+00 0.0000000E+00  1.943350    -0.4561550     0.0000000E+00 000101\n" +
                        "  -1.246704    -0.4932298     0.0000000E+00 0.0000000E+00-0.7157537     000002\n" +
                        "   3212.505      581.8500     0.0000000E+00 0.0000000E+00 -832.8004     300000\n" +
                        "   12710.59      2281.340     0.0000000E+00 0.0000000E+00 -3209.326     210000\n" +
                        "   16766.71      2983.637     0.0000000E+00 0.0000000E+00 -4117.249     120000\n" +
                        "   7385.633      1310.600     0.0000000E+00 0.0000000E+00 -1758.764     030000\n" +
                        "  0.0000000E+00 0.0000000E+00 -168.4409      149.6105     0.0000000E+00 201000\n" +
                        "  0.0000000E+00 0.0000000E+00 -450.3309      392.4987     0.0000000E+00 111000\n" +
                        "  0.0000000E+00 0.0000000E+00 -300.6848      257.5589     0.0000000E+00 021000\n" +
                        "   40.16804      1.634413     0.0000000E+00 0.0000000E+00  116.4781     102000\n" +
                        "   61.15523      8.225268     0.0000000E+00 0.0000000E+00  153.1019     012000\n" +
                        "  0.0000000E+00 0.0000000E+00  5.718149      12.65646     0.0000000E+00 003000\n" +
                        "  0.0000000E+00 0.0000000E+00 -282.8466      158.6574     0.0000000E+00 200100\n" +
                        "  0.0000000E+00 0.0000000E+00 -753.7674      415.0450     0.0000000E+00 110100\n" +
                        "  0.0000000E+00 0.0000000E+00 -503.3731      270.4788     0.0000000E+00 020100\n" +
                        "   102.2913     -2.227525     0.0000000E+00 0.0000000E+00  234.5405     101100\n" +
                        "   148.5018      7.038036     0.0000000E+00 0.0000000E+00  308.3549     011100\n" +
                        "  0.0000000E+00 0.0000000E+00  8.785585      35.31323     0.0000000E+00 002100\n" +
                        "   381.6382      2.867101     0.0000000E+00 0.0000000E+00  99.49667     200001\n" +
                        "   891.3679     -83.66197     0.0000000E+00 0.0000000E+00  387.2872     110001\n" +
                        "   519.2345     -109.4950     0.0000000E+00 0.0000000E+00  338.8245     020001\n" +
                        "  0.0000000E+00 0.0000000E+00 -47.55948    -0.4864679     0.0000000E+00 101001\n" +
                        "  0.0000000E+00 0.0000000E+00 -69.34298     -9.942394     0.0000000E+00 011001\n" +
                        "   15.27657      5.225678     0.0000000E+00 0.0000000E+00  6.171578     002001\n" +
                        "   64.55845     -5.856426     0.0000000E+00 0.0000000E+00  125.4424     100200\n" +
                        "   103.0520      5.624648     0.0000000E+00 0.0000000E+00  164.5993     010200\n" +
                        "  0.0000000E+00 0.0000000E+00-0.1815190      34.00187     0.0000000E+00 001200\n" +
                        "  0.0000000E+00 0.0000000E+00 -64.26343     -9.000078     0.0000000E+00 100101\n" +
                        "  0.0000000E+00 0.0000000E+00 -88.00835     -18.34491     0.0000000E+00 010101\n" +
                        "   45.48080      20.93164     0.0000000E+00 0.0000000E+00  15.72253     001101\n" +
                        "   37.22480      2.765013     0.0000000E+00 0.0000000E+00  20.60825     100002\n" +
                        "   68.17589      12.97156     0.0000000E+00 0.0000000E+00  33.27468     010002\n" +
                        "  0.0000000E+00 0.0000000E+00-0.3576535E-01 0.8829634     0.0000000E+00 001002\n" +
                        "  0.0000000E+00 0.0000000E+00 -5.115287      10.15366     0.0000000E+00 000300\n" +
                        "   32.13280      16.54284     0.0000000E+00 0.0000000E+00  11.42251     000201\n" +
                        "  0.0000000E+00 0.0000000E+00 -1.927705      1.102778     0.0000000E+00 000102\n" +
                        "   1.783692     0.4217728     0.0000000E+00 0.0000000E+00  1.084953     000003\n" +
                        "   41670.11      6174.108     0.0000000E+00 0.0000000E+00 -24955.60     400000\n" +
                        "   215799.4      32144.59     0.0000000E+00 0.0000000E+00 -131289.5     310000\n" +
                        "   418826.3      62744.00     0.0000000E+00 0.0000000E+00 -259050.5     220000\n" +
                        "   361141.6      54497.96     0.0000000E+00 0.0000000E+00 -227303.6     130000\n" +
                        "   116760.8      17794.60     0.0000000E+00 0.0000000E+00 -74869.27     040000\n" +
                        "  0.0000000E+00 0.0000000E+00  521.2705      1237.809     0.0000000E+00 301000\n" +
                        "  0.0000000E+00 0.0000000E+00  2196.679      4831.387     0.0000000E+00 211000\n" +
                        "  0.0000000E+00 0.0000000E+00  3078.725      6281.558     0.0000000E+00 121000\n" +
                        "  0.0000000E+00 0.0000000E+00  1441.685      2728.618     0.0000000E+00 031000\n" +
                        "  -4948.432     -261.1653     0.0000000E+00 0.0000000E+00  38.11632     202000\n" +
                        "  -13077.20     -689.5944     0.0000000E+00 0.0000000E+00 -17.41241     112000\n" +
                        "  -8644.154     -458.2815     0.0000000E+00 0.0000000E+00 -90.55227     022000\n" +
                        "  0.0000000E+00 0.0000000E+00  463.1515     -19.78766     0.0000000E+00 103000\n" +
                        "  0.0000000E+00 0.0000000E+00  603.6877     -27.88213     0.0000000E+00 013000\n" +
                        "  -50.07221     -20.12567     0.0000000E+00 0.0000000E+00 -20.96065     004000\n" +
                        "  0.0000000E+00 0.0000000E+00 -978.8080      1406.949     0.0000000E+00 300100\n" +
                        "  0.0000000E+00 0.0000000E+00 -3617.373      5479.441     0.0000000E+00 210100\n" +
                        "  0.0000000E+00 0.0000000E+00 -4420.523      7115.904     0.0000000E+00 120100\n" +
                        "  0.0000000E+00 0.0000000E+00 -1780.020      3087.458     0.0000000E+00 030100\n" +
                        "  -9836.562     -525.6955     0.0000000E+00 0.0000000E+00 -161.6237     201100\n" +
                        "  -26031.90     -1406.386     0.0000000E+00 0.0000000E+00 -647.1921     111100\n" +
                        "  -17243.30     -955.5608     0.0000000E+00 0.0000000E+00 -577.0450     021100\n" +
                        "  0.0000000E+00 0.0000000E+00  1565.546     -58.88555     0.0000000E+00 102100\n" +
                        "  0.0000000E+00 0.0000000E+00  2038.347     -86.43886     0.0000000E+00 012100\n" +
                        "  -212.8278     -77.04823     0.0000000E+00 0.0000000E+00 -88.09209     003100\n" +
                        "  -1546.737     -114.2369     0.0000000E+00 0.0000000E+00 -3033.712     300001\n" +
                        "  -12938.34     -1604.140     0.0000000E+00 0.0000000E+00 -10604.91     210001\n" +
                        "  -26075.70     -3612.234     0.0000000E+00 0.0000000E+00 -12263.37     120001\n" +
                        "  -15462.38     -2253.446     0.0000000E+00 0.0000000E+00 -4692.957     030001\n" +
                        "  0.0000000E+00 0.0000000E+00  272.8219     -32.01980     0.0000000E+00 201001\n" +
                        "  0.0000000E+00 0.0000000E+00  951.0957     -283.0185     0.0000000E+00 111001\n" +
                        "  0.0000000E+00 0.0000000E+00  784.0014     -316.0834     0.0000000E+00 021001\n" +
                        "  -578.2773     -38.01464     0.0000000E+00 0.0000000E+00 -286.4615     102001\n" +
                        "  -803.0310     -58.76138     0.0000000E+00 0.0000000E+00 -457.9119     012001\n" +
                        "  0.0000000E+00 0.0000000E+00-0.1663841E-01 -9.777394     0.0000000E+00 003001\n" +
                        "  -5259.177     -312.0416     0.0000000E+00 0.0000000E+00 -210.1267     200200\n" +
                        "  -13831.30     -760.6544     0.0000000E+00 0.0000000E+00 -765.0158     110200\n" +
                        "  -9116.286     -477.5393     0.0000000E+00 0.0000000E+00 -648.2575     020200\n" +
                        "  0.0000000E+00 0.0000000E+00  1765.756     -65.31186     0.0000000E+00 101200\n" +
                        "  0.0000000E+00 0.0000000E+00  2304.757     -89.57061     0.0000000E+00 011200\n" +
                        "  -346.2350     -112.6300     0.0000000E+00 0.0000000E+00 -147.0822     002200\n" +
                        "  0.0000000E+00 0.0000000E+00  485.8879     -139.9457     0.0000000E+00 200101\n" +
                        "  0.0000000E+00 0.0000000E+00  1680.141     -578.1569     0.0000000E+00 110101\n" +
                        "  0.0000000E+00 0.0000000E+00  1378.245     -516.0593     0.0000000E+00 020101\n" +
                        "  -1238.558     -76.30097     0.0000000E+00 0.0000000E+00 -752.7984     101101\n" +
                        "  -1733.024     -118.4393     0.0000000E+00 0.0000000E+00 -1151.688     011101\n" +
                        "  0.0000000E+00 0.0000000E+00  1.201337     -51.77155     0.0000000E+00 002101\n" +
                        "  -926.2123     -31.94019     0.0000000E+00 0.0000000E+00 -467.1861     200002\n" +
                        "  -2891.444     -26.06637     0.0000000E+00 0.0000000E+00 -1468.332     110002\n" +
                        "  -2176.501      44.42981     0.0000000E+00 0.0000000E+00 -1168.478     020002\n" +
                        "  0.0000000E+00 0.0000000E+00  61.75067     -5.787972     0.0000000E+00 101002\n" +
                        "  0.0000000E+00 0.0000000E+00  116.7726    -0.5583345     0.0000000E+00 011002\n" +
                        "  -30.97161     -6.010887     0.0000000E+00 0.0000000E+00 -16.01168     002002\n" +
                        "  0.0000000E+00 0.0000000E+00  677.8337     -18.29328     0.0000000E+00 100300\n" +
                        "  0.0000000E+00 0.0000000E+00  886.3620     -22.99701     0.0000000E+00 010300\n" +
                        "  -267.6117     -83.98204     0.0000000E+00 0.0000000E+00 -116.4288     001300\n" +
                        "  -728.5863     -36.43770     0.0000000E+00 0.0000000E+00 -494.1270     100201\n" +
                        "  -1048.604     -71.60370     0.0000000E+00 0.0000000E+00 -740.3596     010201\n" +
                        "  0.0000000E+00 0.0000000E+00  14.68541     -72.62567     0.0000000E+00 001201\n" +
                        "  0.0000000E+00 0.0000000E+00  124.5206    -0.2155067     0.0000000E+00 100102\n" +
                        "  0.0000000E+00 0.0000000E+00  207.6686      9.844711     0.0000000E+00 010102\n" +
                        "  -91.55274     -25.64679     0.0000000E+00 0.0000000E+00 -42.23754     001102\n" +
                        "  -84.87971     -4.693807     0.0000000E+00 0.0000000E+00 -46.85703     100003\n" +
                        "  -151.5384     -15.54762     0.0000000E+00 0.0000000E+00 -79.88757     010003\n" +
                        "  0.0000000E+00 0.0000000E+00 0.7317803    -0.6062357     0.0000000E+00 001003\n" +
                        "  -84.75060     -28.54178     0.0000000E+00 0.0000000E+00 -37.51822     000400\n" +
                        "  0.0000000E+00 0.0000000E+00  17.62278     -30.56656     0.0000000E+00 000301\n" +
                        "  -69.50111     -23.41049     0.0000000E+00 0.0000000E+00 -30.72904     000202\n" +
                        "  0.0000000E+00 0.0000000E+00  3.109575     -1.307302     0.0000000E+00 000103\n" +
                        "  -2.953646    -0.4228898     0.0000000E+00 0.0000000E+00 -1.801046     000004\n" +
                        "   1001296.      81071.76     0.0000000E+00 0.0000000E+00 -401499.5     500000\n" +
                        "   6564038.      529003.2     0.0000000E+00 0.0000000E+00 -2608494.     410000\n" +
                        "  0.1721401E+08  1380767.     0.0000000E+00 0.0000000E+00 -6776254.     320000\n" +
                        "  0.2257920E+08  1802999.     0.0000000E+00 0.0000000E+00 -8799400.     230000\n" +
                        "  0.1481688E+08  1178458.     0.0000000E+00 0.0000000E+00 -5712617.     140000\n" +
                        "   3892464.      308607.6     0.0000000E+00 0.0000000E+00 -1483464.     050000\n" +
                        "  0.0000000E+00 0.0000000E+00 -34219.46      13797.42     0.0000000E+00 401000\n" +
                        "  0.0000000E+00 0.0000000E+00 -183461.2      72138.39     0.0000000E+00 311000\n" +
                        "  0.0000000E+00 0.0000000E+00 -368795.7      141437.3     0.0000000E+00 221000\n" +
                        "  0.0000000E+00 0.0000000E+00 -329631.4      123409.0     0.0000000E+00 131000\n" +
                        "  0.0000000E+00 0.0000000E+00 -110592.8      40485.76     0.0000000E+00 041000\n" +
                        "  -31977.87     -2962.019     0.0000000E+00 0.0000000E+00  47946.06     302000\n" +
                        "  -118256.7     -10969.94     0.0000000E+00 0.0000000E+00  189906.2     212000\n" +
                        "  -144922.3     -13472.86     0.0000000E+00 0.0000000E+00  250815.5     122000\n" +
                        "  -58771.38     -5480.377     0.0000000E+00 0.0000000E+00  110522.4     032000\n" +
                        "  0.0000000E+00 0.0000000E+00 -871.3705     -234.2719     0.0000000E+00 203000\n" +
                        "  0.0000000E+00 0.0000000E+00 -2418.181     -502.4775     0.0000000E+00 113000\n" +
                        "  0.0000000E+00 0.0000000E+00 -1682.273     -258.8191     0.0000000E+00 023000\n" +
                        "   2698.028      188.3451     0.0000000E+00 0.0000000E+00  847.2230     104000\n" +
                        "   3595.613      273.1287     0.0000000E+00 0.0000000E+00  1154.198     014000\n" +
                        "  0.0000000E+00 0.0000000E+00  4.222468      44.64577     0.0000000E+00 005000\n" +
                        "  0.0000000E+00 0.0000000E+00 -69858.56      16557.06     0.0000000E+00 400100\n" +
                        "  0.0000000E+00 0.0000000E+00 -370546.0      86498.30     0.0000000E+00 310100\n" +
                        "  0.0000000E+00 0.0000000E+00 -737217.9      169542.0     0.0000000E+00 220100\n" +
                        "  0.0000000E+00 0.0000000E+00 -652340.7      147940.1     0.0000000E+00 130100\n" +
                        "  0.0000000E+00 0.0000000E+00 -216722.2      48544.99     0.0000000E+00 040100\n" +
                        "  -56803.29     -5496.069     0.0000000E+00 0.0000000E+00  94508.05     301100\n" +
                        "  -209226.3     -20522.21     0.0000000E+00 0.0000000E+00  374905.0     211100\n" +
                        "  -255154.5     -25435.88     0.0000000E+00 0.0000000E+00  496016.1     121100\n" +
                        "  -102827.6     -10451.32     0.0000000E+00 0.0000000E+00  219000.6     031100\n" +
                        "  0.0000000E+00 0.0000000E+00 -2694.464     -494.8377     0.0000000E+00 202100\n" +
                        "  0.0000000E+00 0.0000000E+00 -7658.650     -996.0650     0.0000000E+00 112100\n" +
                        "  0.0000000E+00 0.0000000E+00 -5430.295     -442.6991     0.0000000E+00 022100\n" +
                        "   10960.72      767.7429     0.0000000E+00 0.0000000E+00  3921.867     103100\n" +
                        "   14600.59      1104.968     0.0000000E+00 0.0000000E+00  5295.398     013100\n" +
                        "  0.0000000E+00 0.0000000E+00 0.4965075      231.6458     0.0000000E+00 004100\n" +
                        "   116041.2      963.6124     0.0000000E+00 0.0000000E+00  7169.403     400001\n" +
                        "   512781.8     -10506.62     0.0000000E+00 0.0000000E+00  108991.8     310001\n" +
                        "   826358.4     -50754.89     0.0000000E+00 0.0000000E+00  356017.6     220001\n" +
                        "   569120.2     -70331.43     0.0000000E+00 0.0000000E+00  436450.0     130001\n" +
                        "   138426.6     -31532.54     0.0000000E+00 0.0000000E+00  184770.3     040001\n" +
                        "  0.0000000E+00 0.0000000E+00 -14441.24      208.4494     0.0000000E+00 301001\n" +
                        "  0.0000000E+00 0.0000000E+00 -58635.47     -1531.281     0.0000000E+00 211001\n" +
                        "  0.0000000E+00 0.0000000E+00 -79491.92     -5035.760     0.0000000E+00 121001\n" +
                        "  0.0000000E+00 0.0000000E+00 -36016.27     -3511.619     0.0000000E+00 031001\n" +
                        "   12096.37      583.1580     0.0000000E+00 0.0000000E+00  7582.358     202001\n" +
                        "   39125.38      1937.843     0.0000000E+00 0.0000000E+00  20309.46     112001\n" +
                        "   30593.36      1547.389     0.0000000E+00 0.0000000E+00  13681.73     022001\n" +
                        "  0.0000000E+00 0.0000000E+00 -596.5369      106.4635     0.0000000E+00 103001\n" +
                        "  0.0000000E+00 0.0000000E+00 -1092.454      152.8365     0.0000000E+00 013001\n" +
                        "   200.5211      40.62988     0.0000000E+00 0.0000000E+00  101.3413     004001\n" +
                        "  -27424.99     -3143.676     0.0000000E+00 0.0000000E+00  49712.79     300200\n" +
                        "  -95383.01     -10958.70     0.0000000E+00 0.0000000E+00  196200.1     210200\n" +
                        "  -108280.6     -12515.42     0.0000000E+00 0.0000000E+00  258409.8     120200\n" +
                        "  -39749.80     -4639.266     0.0000000E+00 0.0000000E+00  113648.2     030200\n" +
                        "  0.0000000E+00 0.0000000E+00 -3147.691     -386.2808     0.0000000E+00 201200\n" +
                        "  0.0000000E+00 0.0000000E+00 -9234.641     -576.6583     0.0000000E+00 111200\n" +
                        "  0.0000000E+00 0.0000000E+00 -6722.325     -60.77900     0.0000000E+00 021200\n" +
                        "   17252.53      1202.393     0.0000000E+00 0.0000000E+00  6829.788     102200\n" +
                        "   22996.14      1721.840     0.0000000E+00 0.0000000E+00  9224.820     012200\n" +
                        "  0.0000000E+00 0.0000000E+00 -67.68021      485.6341     0.0000000E+00 003200\n" +
                        "  0.0000000E+00 0.0000000E+00 -17696.44     -640.2238     0.0000000E+00 300101\n" +
                        "  0.0000000E+00 0.0000000E+00 -68998.16     -5142.616     0.0000000E+00 210101\n" +
                        "  0.0000000E+00 0.0000000E+00 -90049.64     -10115.48     0.0000000E+00 120101\n" +
                        "  0.0000000E+00 0.0000000E+00 -39392.66     -5881.669     0.0000000E+00 030101\n" +
                        "   31333.96      1450.501     0.0000000E+00 0.0000000E+00  15646.69     201101\n" +
                        "   96989.45      4649.119     0.0000000E+00 0.0000000E+00  42316.07     111101\n" +
                        "   73541.55      3655.957     0.0000000E+00 0.0000000E+00  28764.39     021101\n" +
                        "  0.0000000E+00 0.0000000E+00 -3253.821      365.4331     0.0000000E+00 102101\n" +
                        "  0.0000000E+00 0.0000000E+00 -5301.374      527.2749     0.0000000E+00 012101\n" +
                        "   947.8128      207.6141     0.0000000E+00 0.0000000E+00  463.9589     003101\n" +
                        "   17459.76      258.9070     0.0000000E+00 0.0000000E+00  9820.283     300002\n" +
                        "   78416.57      2137.323     0.0000000E+00 0.0000000E+00  44546.46     210002\n" +
                        "   120593.2      5035.175     0.0000000E+00 0.0000000E+00  65608.44     120002\n" +
                        "   62772.64      3534.587     0.0000000E+00 0.0000000E+00  31635.07     030002\n" +
                        "  0.0000000E+00 0.0000000E+00 -1700.566      28.34098     0.0000000E+00 201002\n" +
                        "  0.0000000E+00 0.0000000E+00 -5014.696      261.9181     0.0000000E+00 111002\n" +
                        "  0.0000000E+00 0.0000000E+00 -3738.786      360.8339     0.0000000E+00 021002\n" +
                        "   1733.959      99.30213     0.0000000E+00 0.0000000E+00  894.5246     102002\n" +
                        "   2717.885      166.0054     0.0000000E+00 0.0000000E+00  1436.568     012002\n" +
                        "  0.0000000E+00 0.0000000E+00 -14.39542      8.105145     0.0000000E+00 003002\n" +
                        "  0.0000000E+00 0.0000000E+00 -1400.539     -45.81333     0.0000000E+00 200300\n" +
                        "  0.0000000E+00 0.0000000E+00 -4337.220      137.2884     0.0000000E+00 110300\n" +
                        "  0.0000000E+00 0.0000000E+00 -3298.208      273.1849     0.0000000E+00 020300\n" +
                        "   12470.38      858.2556     0.0000000E+00 0.0000000E+00  5431.274     101300\n" +
                        "   16664.62      1227.588     0.0000000E+00 0.0000000E+00  7375.533     011300\n" +
                        "  0.0000000E+00 0.0000000E+00 -167.1718      533.2407     0.0000000E+00 002300\n" +
                        "   20306.05      924.0734     0.0000000E+00 0.0000000E+00  8862.648     200201\n" +
                        "   61502.57      2806.008     0.0000000E+00 0.0000000E+00  24435.56     110201\n" +
                        "   45850.65      2098.856     0.0000000E+00 0.0000000E+00  16977.18     020201\n" +
                        "  0.0000000E+00 0.0000000E+00 -5052.432      420.1179     0.0000000E+00 101201\n" +
                        "  0.0000000E+00 0.0000000E+00 -7811.513      601.4711     0.0000000E+00 011201\n" +
                        "   1712.937      379.4014     0.0000000E+00 0.0000000E+00  821.5613     002201\n" +
                        "  0.0000000E+00 0.0000000E+00 -2473.258      141.8231     0.0000000E+00 200102\n" +
                        "  0.0000000E+00 0.0000000E+00 -7488.385      713.8872     0.0000000E+00 110102\n" +
                        "  0.0000000E+00 0.0000000E+00 -5721.770      761.8000     0.0000000E+00 020102\n" +
                        "   4288.803      243.6823     0.0000000E+00 0.0000000E+00  2333.877     101102\n" +
                        "   6594.274      399.5784     0.0000000E+00 0.0000000E+00  3708.795     011102\n" +
                        "  0.0000000E+00 0.0000000E+00 -61.66589      60.40570     0.0000000E+00 002102\n" +
                        "   2604.380      72.55024     0.0000000E+00 0.0000000E+00  1453.014     200003\n" +
                        "   8471.965      190.2178     0.0000000E+00 0.0000000E+00  4663.694     110003\n" +
                        "   6787.973      92.31410     0.0000000E+00 0.0000000E+00  3740.688     020003\n" +
                        "  0.0000000E+00 0.0000000E+00 -115.9457      8.245052     0.0000000E+00 101003\n" +
                        "  0.0000000E+00 0.0000000E+00 -218.3440      8.831385     0.0000000E+00 011003\n" +
                        "   68.67762      7.801873     0.0000000E+00 0.0000000E+00  37.50827     002003\n" +
                        "   3514.979      233.8325     0.0000000E+00 0.0000000E+00  1684.468     100400\n" +
                        "   4728.219      340.3566     0.0000000E+00 0.0000000E+00  2306.951     010400\n" +
                        "  0.0000000E+00 0.0000000E+00 -160.2600      310.8478     0.0000000E+00 001400\n" +
                        "  0.0000000E+00 0.0000000E+00 -2480.856      152.8896     0.0000000E+00 100301\n" +
                        "  0.0000000E+00 0.0000000E+00 -3730.236      210.5856     0.0000000E+00 010301\n" +
                        "   1433.095      322.2775     0.0000000E+00 0.0000000E+00  679.1546     001301\n" +
                        "   2786.232      148.6912     0.0000000E+00 0.0000000E+00  1608.888     100202\n" +
                        "   4287.819      260.4379     0.0000000E+00 0.0000000E+00  2537.227     010202\n" +
                        "  0.0000000E+00 0.0000000E+00 -99.05403      109.1664     0.0000000E+00 001202\n" +
                        "  0.0000000E+00 0.0000000E+00 -247.4581      9.264803     0.0000000E+00 100103\n" +
                        "  0.0000000E+00 0.0000000E+00 -438.2002      4.884973     0.0000000E+00 010103\n" +
                        "   197.3395      33.28513     0.0000000E+00 0.0000000E+00  102.7740     001103\n" +
                        "   185.7174      7.881518     0.0000000E+00 0.0000000E+00  106.6422     100004\n" +
                        "   331.8847      21.34593     0.0000000E+00 0.0000000E+00  185.3192     010004\n" +
                        "  0.0000000E+00 0.0000000E+00 -1.740115     0.5018281     0.0000000E+00 001004\n" +
                        "  0.0000000E+00 0.0000000E+00 -59.07854      76.10908     0.0000000E+00 000500\n" +
                        "   479.0211      115.5997     0.0000000E+00 0.0000000E+00  225.4114     000401\n" +
                        "  0.0000000E+00 0.0000000E+00 -63.17179      57.37883     0.0000000E+00 000302\n" +
                        "   150.7416      32.52185     0.0000000E+00 0.0000000E+00  75.99992     000203\n" +
                        "  0.0000000E+00 0.0000000E+00 -5.660397      1.467050     0.0000000E+00 000104\n" +
                        "   5.206131     0.4748323     0.0000000E+00 0.0000000E+00  3.194866     000005"
        );
    }

    private CosyArbitraryOrder.CosyMapArbitraryOrder cosyMap左偏n1弯铁(){
        return CosyArbitraryOrder.readMap(
                "   34.21466      24.13186     0.0000000E+00 0.0000000E+00  2.118981     100000\n" +
                        "   48.46882      34.21466     0.0000000E+00 0.0000000E+00  2.916527     010000\n" +
                        "  0.0000000E+00 0.0000000E+00-0.8251972     0.1725486     0.0000000E+00 001000\n" +
                        "  0.0000000E+00 0.0000000E+00 -1.849041    -0.8251972     0.0000000E+00 000100\n" +
                        "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00  1.000000     000010\n" +
                        "  -2.916527     -2.118981     0.0000000E+00 0.0000000E+00 0.5018838     000001\n" +
                        "  -260.6513     -176.8884     0.0000000E+00 0.0000000E+00 -204.4996     200000\n" +
                        "  -718.9358     -486.9319     0.0000000E+00 0.0000000E+00 -579.6105     110000\n" +
                        "  -494.7652     -334.3900     0.0000000E+00 0.0000000E+00 -410.9842     020000\n" +
                        "  0.0000000E+00 0.0000000E+00 -25.66988     -25.01555     0.0000000E+00 101000\n" +
                        "  0.0000000E+00 0.0000000E+00 -35.33156     -34.43095     0.0000000E+00 011000\n" +
                        "   17.25437      12.53603     0.0000000E+00 0.0000000E+00-0.3653826     002000\n" +
                        "  0.0000000E+00 0.0000000E+00 -22.54061     -25.66988     0.0000000E+00 100100\n" +
                        "  0.0000000E+00 0.0000000E+00 -31.02448     -35.33156     0.0000000E+00 010100\n" +
                        "   34.50874      25.07206     0.0000000E+00 0.0000000E+00-0.6333075     001100\n" +
                        "  -4.451824      6.685365     0.0000000E+00 0.0000000E+00  33.42126     100001\n" +
                        "  -30.33037     -7.469862     0.0000000E+00 0.0000000E+00  46.00042     010001\n" +
                        "  0.0000000E+00 0.0000000E+00-0.8286112    -0.7123018     0.0000000E+00 001001\n" +
                        "   19.86409      14.43211     0.0000000E+00 0.0000000E+00-0.8867287     000200\n" +
                        "  0.0000000E+00 0.0000000E+00 0.2924405    -0.8286112     0.0000000E+00 000101\n" +
                        "   2.642585     0.9526379     0.0000000E+00 0.0000000E+00 -1.896448     000002\n" +
                        "   11019.92      2244.387     0.0000000E+00 0.0000000E+00  3047.535     300000\n" +
                        "   46695.56      9427.952     0.0000000E+00 0.0000000E+00  12722.40     210000\n" +
                        "   65955.83      13200.31     0.0000000E+00 0.0000000E+00  17690.96     120000\n" +
                        "   31072.64      6173.698     0.0000000E+00 0.0000000E+00  8194.858     030000\n" +
                        "  0.0000000E+00 0.0000000E+00  259.4847      190.2838     0.0000000E+00 201000\n" +
                        "  0.0000000E+00 0.0000000E+00  726.5071      534.3816     0.0000000E+00 111000\n" +
                        "  0.0000000E+00 0.0000000E+00  508.1630      374.7344     0.0000000E+00 021000\n" +
                        "  -110.2797     -71.08580     0.0000000E+00 0.0000000E+00 -202.4154     102000\n" +
                        "  -144.3964     -92.26506     0.0000000E+00 0.0000000E+00 -286.7718     012000\n" +
                        "  0.0000000E+00 0.0000000E+00  1.360905      1.323998     0.0000000E+00 003000\n" +
                        "  0.0000000E+00 0.0000000E+00  1.387973      281.8162     0.0000000E+00 200100\n" +
                        "  0.0000000E+00 0.0000000E+00 -8.462541      789.8313     0.0000000E+00 110100\n" +
                        "  0.0000000E+00 0.0000000E+00 -15.45130      553.0158     0.0000000E+00 020100\n" +
                        "  -221.2864     -141.7176     0.0000000E+00 0.0000000E+00 -419.0394     101100\n" +
                        "  -294.3846     -187.1038     0.0000000E+00 0.0000000E+00 -593.3809     011100\n" +
                        "  0.0000000E+00 0.0000000E+00  3.876064      4.006950     0.0000000E+00 002100\n" +
                        "  -2257.660     -238.1584     0.0000000E+00 0.0000000E+00 -109.6671     200001\n" +
                        "  -6062.481     -439.3831     0.0000000E+00 0.0000000E+00 -3.840221     110001\n" +
                        "  -4071.756     -154.6549     0.0000000E+00 0.0000000E+00  215.8078     020001\n" +
                        "  0.0000000E+00 0.0000000E+00-0.8851413     -14.64867     0.0000000E+00 101001\n" +
                        "  0.0000000E+00 0.0000000E+00  15.44385     -3.808794     0.0000000E+00 011001\n" +
                        "  -13.26003     -4.135281     0.0000000E+00 0.0000000E+00  17.73942     002001\n" +
                        "  -118.1192     -84.00898     0.0000000E+00 0.0000000E+00 -247.1495     100200\n" +
                        "  -139.4938     -99.27481     0.0000000E+00 0.0000000E+00 -348.7065     010200\n" +
                        "  0.0000000E+00 0.0000000E+00  4.132078      4.238397     0.0000000E+00 001200\n" +
                        "  0.0000000E+00 0.0000000E+00  66.13571     -4.806931     0.0000000E+00 100101\n" +
                        "  0.0000000E+00 0.0000000E+00  107.6787      9.883456     0.0000000E+00 010101\n" +
                        "  -45.73905     -22.32022     0.0000000E+00 0.0000000E+00  35.33521     001101\n" +
                        "   199.7716      11.77604     0.0000000E+00 0.0000000E+00 -51.18852     100002\n" +
                        "   303.8634      24.32933     0.0000000E+00 0.0000000E+00 -95.08501     010002\n" +
                        "  0.0000000E+00 0.0000000E+00 0.9375084     0.2835883     0.0000000E+00 001002\n" +
                        "  0.0000000E+00 0.0000000E+00 0.6711981      1.647989     0.0000000E+00 000300\n" +
                        "  -36.78295     -19.63935     0.0000000E+00 0.0000000E+00  21.10937     000201\n" +
                        "  0.0000000E+00 0.0000000E+00 -1.565210      1.109691     0.0000000E+00 000102\n" +
                        "  -8.200824     -1.069062     0.0000000E+00 0.0000000E+00  3.722452     000003\n" +
                        "  -224674.2     -36106.89     0.0000000E+00 0.0000000E+00 -126791.4     400000\n" +
                        "  -1256068.     -201981.5     0.0000000E+00 0.0000000E+00 -715192.6     310000\n" +
                        "  -2632349.     -423599.7     0.0000000E+00 0.0000000E+00 -1512812.     220000\n" +
                        "  -2451181.     -394928.4     0.0000000E+00 0.0000000E+00 -1422436.     130000\n" +
                        "  -855784.7     -138168.6     0.0000000E+00 0.0000000E+00 -501709.8     040000\n" +
                        "  0.0000000E+00 0.0000000E+00 -10680.01     -1629.948     0.0000000E+00 301000\n" +
                        "  0.0000000E+00 0.0000000E+00 -44953.42     -6871.187     0.0000000E+00 211000\n" +
                        "  0.0000000E+00 0.0000000E+00 -63058.29     -9649.591     0.0000000E+00 121000\n" +
                        "  0.0000000E+00 0.0000000E+00 -29492.93     -4528.220     0.0000000E+00 031000\n" +
                        "   15012.54      2091.527     0.0000000E+00 0.0000000E+00  2621.267     202000\n" +
                        "   42437.72      5842.830     0.0000000E+00 0.0000000E+00  7184.574     112000\n" +
                        "   29994.55      4082.569     0.0000000E+00 0.0000000E+00  4913.653     022000\n" +
                        "  0.0000000E+00 0.0000000E+00  131.7396      117.2539     0.0000000E+00 103000\n" +
                        "  0.0000000E+00 0.0000000E+00  181.7618      161.1035     0.0000000E+00 013000\n" +
                        "   3.801333      4.085461     0.0000000E+00 0.0000000E+00 -52.52907     004000\n" +
                        "  0.0000000E+00 0.0000000E+00 -7096.821     -3087.109     0.0000000E+00 300100\n" +
                        "  0.0000000E+00 0.0000000E+00 -30006.02     -12962.44     0.0000000E+00 210100\n" +
                        "  0.0000000E+00 0.0000000E+00 -42305.21     -18146.48     0.0000000E+00 120100\n" +
                        "  0.0000000E+00 0.0000000E+00 -19901.49     -8483.511     0.0000000E+00 030100\n" +
                        "   30615.79      4128.220     0.0000000E+00 0.0000000E+00  5397.704     201100\n" +
                        "   86592.88      11578.26     0.0000000E+00 0.0000000E+00  14860.07     111100\n" +
                        "   61252.46      8134.577     0.0000000E+00 0.0000000E+00  10210.83     021100\n" +
                        "  0.0000000E+00 0.0000000E+00  93.83573      388.7886     0.0000000E+00 102100\n" +
                        "  0.0000000E+00 0.0000000E+00  121.7982      539.1759     0.0000000E+00 012100\n" +
                        "   14.38920      16.27652     0.0000000E+00 0.0000000E+00 -209.3637     003100\n" +
                        "   29534.68      4686.935     0.0000000E+00 0.0000000E+00  31106.51     300001\n" +
                        "   99867.45      15090.73     0.0000000E+00 0.0000000E+00  126173.9     210001\n" +
                        "   105237.2      14680.44     0.0000000E+00 0.0000000E+00  170539.2     120001\n" +
                        "   32594.96      3817.648     0.0000000E+00 0.0000000E+00  76836.05     030001\n" +
                        "  0.0000000E+00 0.0000000E+00  1298.177      243.4295     0.0000000E+00 201001\n" +
                        "  0.0000000E+00 0.0000000E+00  3267.737      428.4516     0.0000000E+00 111001\n" +
                        "  0.0000000E+00 0.0000000E+00  2030.292      122.0524     0.0000000E+00 021001\n" +
                        "  -2317.319     -188.5641     0.0000000E+00 0.0000000E+00  160.5312     102001\n" +
                        "  -3224.571     -225.3112     0.0000000E+00 0.0000000E+00  381.9152     012001\n" +
                        "  0.0000000E+00 0.0000000E+00 -6.682882     -4.102427     0.0000000E+00 003001\n" +
                        "   17800.58      2361.567     0.0000000E+00 0.0000000E+00  3024.272     200200\n" +
                        "   50088.60      6450.023     0.0000000E+00 0.0000000E+00  8111.935     110200\n" +
                        "   35265.89      4421.060     0.0000000E+00 0.0000000E+00  5417.027     020200\n" +
                        "  0.0000000E+00 0.0000000E+00 -222.1410      446.6732     0.0000000E+00 101200\n" +
                        "  0.0000000E+00 0.0000000E+00 -336.0942      610.1772     0.0000000E+00 011200\n" +
                        "   26.20614      23.99086     0.0000000E+00 0.0000000E+00 -328.9668     002200\n" +
                        "  0.0000000E+00 0.0000000E+00  1065.055      196.6139     0.0000000E+00 200101\n" +
                        "  0.0000000E+00 0.0000000E+00  3008.567      168.7042     0.0000000E+00 110101\n" +
                        "  0.0000000E+00 0.0000000E+00  2134.861     -149.6792     0.0000000E+00 020101\n" +
                        "  -4543.881     -296.1370     0.0000000E+00 0.0000000E+00  553.7866     101101\n" +
                        "  -6325.527     -343.1444     0.0000000E+00 0.0000000E+00  1098.101     011101\n" +
                        "  0.0000000E+00 0.0000000E+00  4.365942     -18.23355     0.0000000E+00 002101\n" +
                        "   1700.236     -70.70360     0.0000000E+00 0.0000000E+00 -3791.488     200002\n" +
                        "   7876.506     -26.32969     0.0000000E+00 0.0000000E+00 -10860.75     110002\n" +
                        "   7653.531      33.93428     0.0000000E+00 0.0000000E+00 -7893.346     020002\n" +
                        "  0.0000000E+00 0.0000000E+00 -18.67525     -4.183461     0.0000000E+00 101002\n" +
                        "  0.0000000E+00 0.0000000E+00 -35.28012     -8.264536     0.0000000E+00 011002\n" +
                        "   114.6791      10.52805     0.0000000E+00 0.0000000E+00 -38.05529     002002\n" +
                        "  0.0000000E+00 0.0000000E+00 -244.1693      168.1886     0.0000000E+00 100300\n" +
                        "  0.0000000E+00 0.0000000E+00 -358.7338      222.4317     0.0000000E+00 010300\n" +
                        "   41.61072      28.04064     0.0000000E+00 0.0000000E+00 -240.0658     001300\n" +
                        "  -2583.612     -129.5716     0.0000000E+00 0.0000000E+00  481.1652     100201\n" +
                        "  -3636.824     -158.1332     0.0000000E+00 0.0000000E+00  880.1977     010201\n" +
                        "  0.0000000E+00 0.0000000E+00  26.15730     -25.13457     0.0000000E+00 001201\n" +
                        "  0.0000000E+00 0.0000000E+00 -122.2475    -0.5280181     0.0000000E+00 100102\n" +
                        "  0.0000000E+00 0.0000000E+00 -226.2729     -9.358308     0.0000000E+00 010102\n" +
                        "   256.6039      36.23653     0.0000000E+00 0.0000000E+00 -94.96731     001102\n" +
                        "  -433.6390     -16.43809     0.0000000E+00 0.0000000E+00  299.1831     100003\n" +
                        "  -771.9746     -38.41238     0.0000000E+00 0.0000000E+00  476.4691     010003\n" +
                        "  0.0000000E+00 0.0000000E+00 -2.695018     0.1875664     0.0000000E+00 001003\n" +
                        "   27.46634      16.58956     0.0000000E+00 0.0000000E+00 -69.54153     000400\n" +
                        "  0.0000000E+00 0.0000000E+00  19.99252     -12.20870     0.0000000E+00 000301\n" +
                        "   168.8760      32.21110     0.0000000E+00 0.0000000E+00 -66.50103     000202\n" +
                        "  0.0000000E+00 0.0000000E+00  2.600615    -0.9357278     0.0000000E+00 000103\n" +
                        "   19.26663      1.406625     0.0000000E+00 0.0000000E+00 -10.94603     000004\n" +
                        "   7729774.      709274.9     0.0000000E+00 0.0000000E+00  3181268.     500000\n" +
                        "  0.5438575E+08  4968248.     0.0000000E+00 0.0000000E+00 0.2227966E+08 410000\n" +
                        "  0.1530561E+09 0.1391881E+08 0.0000000E+00 0.0000000E+00 0.6239899E+08 320000\n" +
                        "  0.2153832E+09 0.1949863E+08 0.0000000E+00 0.0000000E+00 0.8736543E+08 230000\n" +
                        "  0.1515679E+09 0.1366131E+08 0.0000000E+00 0.0000000E+00 0.6115278E+08 140000\n" +
                        "  0.4267427E+08  3830367.     0.0000000E+00 0.0000000E+00 0.1712057E+08 050000\n" +
                        "  0.0000000E+00 0.0000000E+00  235340.7      17605.86     0.0000000E+00 401000\n" +
                        "  0.0000000E+00 0.0000000E+00  1315973.      99146.50     0.0000000E+00 311000\n" +
                        "  0.0000000E+00 0.0000000E+00  2758874.      209299.4     0.0000000E+00 221000\n" +
                        "  0.0000000E+00 0.0000000E+00  2570311.      196505.8     0.0000000E+00 131000\n" +
                        "  0.0000000E+00 0.0000000E+00  897986.7      69305.29     0.0000000E+00 041000\n" +
                        "  -305951.7     -45533.68     0.0000000E+00 0.0000000E+00 -219863.8     302000\n" +
                        "  -1273854.     -189883.0     0.0000000E+00 0.0000000E+00 -931024.6     212000\n" +
                        "  -1766698.     -263847.6     0.0000000E+00 0.0000000E+00 -1314250.     122000\n" +
                        "  -816200.5     -122198.1     0.0000000E+00 0.0000000E+00 -618565.5     032000\n" +
                        "  0.0000000E+00 0.0000000E+00 -10454.86     -2043.883     0.0000000E+00 203000\n" +
                        "  0.0000000E+00 0.0000000E+00 -29138.88     -5684.802     0.0000000E+00 113000\n" +
                        "  0.0000000E+00 0.0000000E+00 -20299.54     -3953.374     0.0000000E+00 023000\n" +
                        "   7257.459      739.0623     0.0000000E+00 0.0000000E+00  601.3427     104000\n" +
                        "   10283.47      1044.603     0.0000000E+00 0.0000000E+00  781.0983     014000\n" +
                        "  0.0000000E+00 0.0000000E+00  24.16207      18.86306     0.0000000E+00 005000\n" +
                        "  0.0000000E+00 0.0000000E+00  72688.62      45809.12     0.0000000E+00 400100\n" +
                        "  0.0000000E+00 0.0000000E+00  396601.9      257157.4     0.0000000E+00 310100\n" +
                        "  0.0000000E+00 0.0000000E+00  810259.8      541342.8     0.0000000E+00 220100\n" +
                        "  0.0000000E+00 0.0000000E+00  734526.7      506784.8     0.0000000E+00 130100\n" +
                        "  0.0000000E+00 0.0000000E+00  249255.7      178126.5     0.0000000E+00 040100\n" +
                        "  -622365.4     -89659.50     0.0000000E+00 0.0000000E+00 -452413.3     301100\n" +
                        "  -2595891.     -374689.4     0.0000000E+00 0.0000000E+00 -1916697.     211100\n" +
                        "  -3607008.     -521840.5     0.0000000E+00 0.0000000E+00 -2707106.     121100\n" +
                        "  -1669719.     -242279.6     0.0000000E+00 0.0000000E+00 -1274888.     031100\n" +
                        "  0.0000000E+00 0.0000000E+00 -28665.01     -7049.694     0.0000000E+00 202100\n" +
                        "  0.0000000E+00 0.0000000E+00 -80108.06     -19619.30     0.0000000E+00 112100\n" +
                        "  0.0000000E+00 0.0000000E+00 -55966.72     -13648.52     0.0000000E+00 022100\n" +
                        "   29318.86      2941.628     0.0000000E+00 0.0000000E+00  2512.934     103100\n" +
                        "   41538.82      4163.758     0.0000000E+00 0.0000000E+00  3300.960     013100\n" +
                        "  0.0000000E+00 0.0000000E+00  41.53809      105.0714     0.0000000E+00 004100\n" +
                        "  -2136298.     -134097.2     0.0000000E+00 0.0000000E+00 -598266.9     400001\n" +
                        " -0.1147069E+08 -656429.5     0.0000000E+00 0.0000000E+00 -2985507.     310001\n" +
                        " -0.2306265E+08 -1180504.     0.0000000E+00 0.0000000E+00 -5489206.     220001\n" +
                        " -0.2057928E+08 -917551.5     0.0000000E+00 0.0000000E+00 -4381104.     130001\n" +
                        "  -6876866.     -256764.3     0.0000000E+00 0.0000000E+00 -1268451.     040001\n" +
                        "  0.0000000E+00 0.0000000E+00 -24575.34     -4213.977     0.0000000E+00 301001\n" +
                        "  0.0000000E+00 0.0000000E+00 -81310.98     -14440.45     0.0000000E+00 211001\n" +
                        "  0.0000000E+00 0.0000000E+00 -83022.72     -15613.94     0.0000000E+00 121001\n" +
                        "  0.0000000E+00 0.0000000E+00 -24283.66     -5108.354     0.0000000E+00 031001\n" +
                        "   15066.80      3587.654     0.0000000E+00 0.0000000E+00  46238.42     202001\n" +
                        "   18784.36      7058.962     0.0000000E+00 0.0000000E+00  127873.5     112001\n" +
                        "  -3611.041      2850.316     0.0000000E+00 0.0000000E+00  88538.05     022001\n" +
                        "  0.0000000E+00 0.0000000E+00  462.6969      145.1695     0.0000000E+00 103001\n" +
                        "  0.0000000E+00 0.0000000E+00  540.1190      129.2243     0.0000000E+00 013001\n" +
                        "  -621.3736     -53.43368     0.0000000E+00 0.0000000E+00  110.9526     004001\n" +
                        "  -355934.7     -50696.71     0.0000000E+00 0.0000000E+00 -263894.2     300200\n" +
                        "  -1467739.     -208526.0     0.0000000E+00 0.0000000E+00 -1113557.     210200\n" +
                        "  -2015266.     -285757.4     0.0000000E+00 0.0000000E+00 -1566764.     120200\n" +
                        "  -921300.4     -130481.8     0.0000000E+00 0.0000000E+00 -735173.9     030200\n" +
                        "  0.0000000E+00 0.0000000E+00 -27132.21     -8241.966     0.0000000E+00 201200\n" +
                        "  0.0000000E+00 0.0000000E+00 -75681.82     -22760.25     0.0000000E+00 111200\n" +
                        "  0.0000000E+00 0.0000000E+00 -52781.66     -15704.83     0.0000000E+00 021200\n" +
                        "   46590.57      4622.996     0.0000000E+00 0.0000000E+00  3910.593     102200\n" +
                        "   65950.19      6515.591     0.0000000E+00 0.0000000E+00  5056.945     012200\n" +
                        "  0.0000000E+00 0.0000000E+00 -64.89749      238.5662     0.0000000E+00 003200\n" +
                        "  0.0000000E+00 0.0000000E+00  26519.47     -6739.176     0.0000000E+00 300101\n" +
                        "  0.0000000E+00 0.0000000E+00  128848.6     -22199.87     0.0000000E+00 210101\n" +
                        "  0.0000000E+00 0.0000000E+00  205229.7     -22526.36     0.0000000E+00 120101\n" +
                        "  0.0000000E+00 0.0000000E+00  107616.5     -6494.791     0.0000000E+00 030101\n" +
                        "   13428.77      4558.243     0.0000000E+00 0.0000000E+00  91248.74     201101\n" +
                        "  -9901.880      6882.762     0.0000000E+00 0.0000000E+00  252203.4     111101\n" +
                        "  -41018.98      643.8694     0.0000000E+00 0.0000000E+00  174506.1     021101\n" +
                        "  0.0000000E+00 0.0000000E+00  1449.487      214.7859     0.0000000E+00 102101\n" +
                        "  0.0000000E+00 0.0000000E+00  1943.881      53.40881     0.0000000E+00 012101\n" +
                        "  -2481.755     -225.3881     0.0000000E+00 0.0000000E+00  553.4857     003101\n" +
                        "   283634.4      10036.08     0.0000000E+00 0.0000000E+00 -2055.084     300002\n" +
                        "   1166977.      36432.23     0.0000000E+00 0.0000000E+00 -74711.93     210002\n" +
                        "   1618507.      46164.53     0.0000000E+00 0.0000000E+00 -195753.3     120002\n" +
                        "   757401.1      20800.37     0.0000000E+00 0.0000000E+00 -133399.3     030002\n" +
                        "  0.0000000E+00 0.0000000E+00 -236.3140      154.3646     0.0000000E+00 201002\n" +
                        "  0.0000000E+00 0.0000000E+00 -2091.929      276.8834     0.0000000E+00 111002\n" +
                        "  0.0000000E+00 0.0000000E+00 -2333.975      169.2104     0.0000000E+00 021002\n" +
                        "   4124.035      126.4245     0.0000000E+00 0.0000000E+00 -4321.176     102002\n" +
                        "   7509.724      288.1007     0.0000000E+00 0.0000000E+00 -6377.053     012002\n" +
                        "  0.0000000E+00 0.0000000E+00  29.89463     -4.138698     0.0000000E+00 003002\n" +
                        "  0.0000000E+00 0.0000000E+00 -8485.475     -3278.043     0.0000000E+00 200300\n" +
                        "  0.0000000E+00 0.0000000E+00 -23823.22     -8833.934     0.0000000E+00 110300\n" +
                        "  0.0000000E+00 0.0000000E+00 -16734.17     -5939.336     0.0000000E+00 020300\n" +
                        "   34182.93      3303.869     0.0000000E+00 0.0000000E+00  2482.305     101300\n" +
                        "   48324.15      4622.856     0.0000000E+00 0.0000000E+00  3040.892     011300\n" +
                        "  0.0000000E+00 0.0000000E+00 -224.4134      279.2112     0.0000000E+00 002300\n" +
                        "  -2941.387      1351.286     0.0000000E+00 0.0000000E+00  51830.49     200201\n" +
                        "  -37946.87      461.3627     0.0000000E+00 0.0000000E+00  143665.8     110201\n" +
                        "  -47760.52     -1951.795     0.0000000E+00 0.0000000E+00  99874.92     020201\n" +
                        "  0.0000000E+00 0.0000000E+00  1910.876     -44.67025     0.0000000E+00 101201\n" +
                        "  0.0000000E+00 0.0000000E+00  2850.279     -337.5668     0.0000000E+00 011201\n" +
                        "  -3916.623     -371.9693     0.0000000E+00 0.0000000E+00  1054.344     002201\n" +
                        "  0.0000000E+00 0.0000000E+00 -6814.743      325.2137     0.0000000E+00 200102\n" +
                        "  0.0000000E+00 0.0000000E+00 -20755.52      921.4480     0.0000000E+00 110102\n" +
                        "  0.0000000E+00 0.0000000E+00 -15729.97      782.5071     0.0000000E+00 020102\n" +
                        "   10768.66      429.6795     0.0000000E+00 0.0000000E+00 -9010.474     101102\n" +
                        "   18518.59      781.8945     0.0000000E+00 0.0000000E+00 -13443.20     011102\n" +
                        "  0.0000000E+00 0.0000000E+00  38.51854      3.179140     0.0000000E+00 002102\n" +
                        "  -27737.69     -674.7293     0.0000000E+00 0.0000000E+00  9111.048     200003\n" +
                        "  -83566.91     -1943.008     0.0000000E+00 0.0000000E+00  31466.29     110003\n" +
                        "  -63787.46     -1415.991     0.0000000E+00 0.0000000E+00  26445.59     020003\n" +
                        "  0.0000000E+00 0.0000000E+00 -25.66552      4.917677     0.0000000E+00 101003\n" +
                        "  0.0000000E+00 0.0000000E+00 -21.50135      13.34403     0.0000000E+00 011003\n" +
                        "  -300.5435     -17.30387     0.0000000E+00 0.0000000E+00  182.4186     002003\n" +
                        "   9819.857      898.8106     0.0000000E+00 0.0000000E+00  449.8430     100400\n" +
                        "   13873.40      1249.238     0.0000000E+00 0.0000000E+00  439.0449     010400\n" +
                        "  0.0000000E+00 0.0000000E+00 -207.5802      169.3952     0.0000000E+00 001400\n" +
                        "  0.0000000E+00 0.0000000E+00  1097.140     -116.4346     0.0000000E+00 100301\n" +
                        "  0.0000000E+00 0.0000000E+00  1768.251     -255.2107     0.0000000E+00 010301\n" +
                        "  -2919.908     -306.6911     0.0000000E+00 0.0000000E+00  926.0454     001301\n" +
                        "   7658.116      310.5221     0.0000000E+00 0.0000000E+00 -5551.313     100202\n" +
                        "   12859.74      544.5946     0.0000000E+00 0.0000000E+00 -8424.273     010202\n" +
                        "  0.0000000E+00 0.0000000E+00 -31.79986      24.36622     0.0000000E+00 001202\n" +
                        "  0.0000000E+00 0.0000000E+00  490.0412     -10.80149     0.0000000E+00 100103\n" +
                        "  0.0000000E+00 0.0000000E+00  811.7680     -7.121079     0.0000000E+00 010103\n" +
                        "  -746.5735     -59.22159     0.0000000E+00 0.0000000E+00  417.2274     001103\n" +
                        "   1856.524      51.90168     0.0000000E+00 0.0000000E+00 -907.3581     100004\n" +
                        "   3053.751      97.42518     0.0000000E+00 0.0000000E+00 -1536.363     010004\n" +
                        "  0.0000000E+00 0.0000000E+00  7.169312    -0.6885744     0.0000000E+00 001004\n" +
                        "  0.0000000E+00 0.0000000E+00 -67.72334      42.96696     0.0000000E+00 000500\n" +
                        "  -892.0736     -115.7918     0.0000000E+00 0.0000000E+00  322.3581     000401\n" +
                        "  0.0000000E+00 0.0000000E+00 -53.62286      20.07390     0.0000000E+00 000302\n" +
                        "  -522.5820     -53.54036     0.0000000E+00 0.0000000E+00  278.1213     000203\n" +
                        "  0.0000000E+00 0.0000000E+00 -8.689901     0.7275827     0.0000000E+00 000104\n" +
                        "  -55.76167     -2.382990     0.0000000E+00 0.0000000E+00  30.15406     000005"
        );
    }

    private CosyArbitraryOrder.CosyMapArbitraryOrder cosyMap右偏n1负数弯铁(){
        return CosyArbitraryOrder.readMap(
                " -0.2600557     0.7058701     0.0000000E+00 0.0000000E+00 0.4820771E-01 100000\n" +
                        "  -1.320882    -0.2600557     0.0000000E+00 0.0000000E+00-0.8605605E-01 010000\n" +
                        "  0.0000000E+00 0.0000000E+00  44.15967      31.76542     0.0000000E+00 001000\n" +
                        "  0.0000000E+00 0.0000000E+00  61.35844      44.15967     0.0000000E+00 000100\n" +
                        "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00  1.000000     000010\n" +
                        "  0.8605605E-01-0.4820771E-01 0.0000000E+00 0.0000000E+00 0.5856989     000001\n" +
                        " -0.7552109    -0.1771761     0.0000000E+00 0.0000000E+00-0.9442773     200000\n" +
                        " -0.3396712     0.6325576     0.0000000E+00 0.0000000E+00 -1.419833     110000\n" +
                        "  0.3031752     0.9533776     0.0000000E+00 0.0000000E+00 -1.140245     020000\n" +
                        "  0.0000000E+00 0.0000000E+00 -9.810182     -7.069684     0.0000000E+00 101000\n" +
                        "  0.0000000E+00 0.0000000E+00  17.51225      12.62016     0.0000000E+00 011000\n" +
                        "  0.5091134    -0.2852000     0.0000000E+00 0.0000000E+00 -336.0388     002000\n" +
                        "  0.0000000E+00 0.0000000E+00 -13.61997     -9.810182     0.0000000E+00 100100\n" +
                        "  0.0000000E+00 0.0000000E+00  24.31314      17.51225     0.0000000E+00 010100\n" +
                        "   1.018227    -0.5704001     0.0000000E+00 0.0000000E+00 -934.7143     001100\n" +
                        "  -1.978966     -1.493347     0.0000000E+00 0.0000000E+00 0.3221924E-02 100001\n" +
                        "  -1.282377     -2.125322     0.0000000E+00 0.0000000E+00-0.5751487E-02 010001\n" +
                        "  0.0000000E+00 0.0000000E+00 -39.04057     -12.86383     0.0000000E+00 001001\n" +
                        "  0.4321100    -0.2420635     0.0000000E+00 0.0000000E+00 -650.3254     000200\n" +
                        "  0.0000000E+00 0.0000000E+00 -83.69898     -39.04057     0.0000000E+00 000101\n" +
                        "  0.1189370     0.1138733     0.0000000E+00 0.0000000E+00-0.5000095     000002\n" +
                        "   6.573665      6.075562     0.0000000E+00 0.0000000E+00 0.8809320E-01 300000\n" +
                        "   18.45084      18.63851     0.0000000E+00 0.0000000E+00-0.2953311     210000\n" +
                        "   18.96713      19.61168     0.0000000E+00 0.0000000E+00-0.3514488     120000\n" +
                        "   6.606856      7.626866     0.0000000E+00 0.0000000E+00 0.9260036E-01 030000\n" +
                        "  0.0000000E+00 0.0000000E+00  36.64979      17.88328     0.0000000E+00 201000\n" +
                        "  0.0000000E+00 0.0000000E+00  35.84249      23.19351     0.0000000E+00 111000\n" +
                        "  0.0000000E+00 0.0000000E+00  22.69261      9.716415     0.0000000E+00 021000\n" +
                        "   480.6380      137.7370     0.0000000E+00 0.0000000E+00  159.9366     102000\n" +
                        " -0.3649613      219.9501     0.0000000E+00 0.0000000E+00 -251.7548     012000\n" +
                        "  0.0000000E+00 0.0000000E+00  20587.13      2214.744     0.0000000E+00 003000\n" +
                        "  0.0000000E+00 0.0000000E+00  59.32823      30.87620     0.0000000E+00 200100\n" +
                        "  0.0000000E+00 0.0000000E+00  60.77386      40.09669     0.0000000E+00 110100\n" +
                        "  0.0000000E+00 0.0000000E+00  59.33994      33.49664     0.0000000E+00 020100\n" +
                        "   1336.881      383.8161     0.0000000E+00 0.0000000E+00  443.8582     101100\n" +
                        " -0.5256066E-01  612.7503     0.0000000E+00 0.0000000E+00 -699.8231     011100\n" +
                        "  0.0000000E+00 0.0000000E+00  85885.80      9250.857     0.0000000E+00 002100\n" +
                        " -0.6576395    -0.9090340     0.0000000E+00 0.0000000E+00  1.373747     200001\n" +
                        "  -1.012110     -1.339433     0.0000000E+00 0.0000000E+00  2.443054     110001\n" +
                        "  -1.488673     -1.421297     0.0000000E+00 0.0000000E+00  1.677530     020001\n" +
                        "  0.0000000E+00 0.0000000E+00  28.71429      18.45015     0.0000000E+00 101001\n" +
                        "  0.0000000E+00 0.0000000E+00  3.944501      9.105618     0.0000000E+00 011001\n" +
                        "  -32.04031     -8.762179     0.0000000E+00 0.0000000E+00  749.6785     002001\n" +
                        "   929.8433      267.5194     0.0000000E+00 0.0000000E+00  307.9592     100200\n" +
                        "  0.6318947      427.2330     0.0000000E+00 0.0000000E+00 -486.3054     010200\n" +
                        "  0.0000000E+00 0.0000000E+00  119442.5      12886.65     0.0000000E+00 001200\n" +
                        "  0.0000000E+00 0.0000000E+00  45.26431      29.50291     0.0000000E+00 100101\n" +
                        "  0.0000000E+00 0.0000000E+00 -6.972453      3.653959     0.0000000E+00 010101\n" +
                        "  -90.30172     -24.60490     0.0000000E+00 0.0000000E+00  2532.610     001101\n" +
                        "   1.776754     0.3832625     0.0000000E+00 0.0000000E+00-0.1292932     100002\n" +
                        "   2.638682      1.817024     0.0000000E+00 0.0000000E+00-0.3103673E-01 010002\n" +
                        "  0.0000000E+00 0.0000000E+00  38.96185      10.99371     0.0000000E+00 001002\n" +
                        "  0.0000000E+00 0.0000000E+00  55398.45      6004.178     0.0000000E+00 000300\n" +
                        "  -63.36366     -17.42860     0.0000000E+00 0.0000000E+00  2072.698     000201\n" +
                        "  0.0000000E+00 0.0000000E+00  101.1420      38.93492     0.0000000E+00 000102\n" +
                        " -0.1424338    -0.5355713E-01 0.0000000E+00 0.0000000E+00 0.4349974     000003\n" +
                        "  0.9948306      1.824074     0.0000000E+00 0.0000000E+00 -3.207785     400000\n" +
                        "   5.592599      6.563478     0.0000000E+00 0.0000000E+00 -8.990344     310000\n" +
                        "   10.47073      10.79663     0.0000000E+00 0.0000000E+00 -8.992389     220000\n" +
                        "   8.416929      8.737520     0.0000000E+00 0.0000000E+00 -4.007636     130000\n" +
                        "   3.472134      3.377595     0.0000000E+00 0.0000000E+00-0.9050894     040000\n" +
                        "  0.0000000E+00 0.0000000E+00 -86.30106     -60.76136     0.0000000E+00 301000\n" +
                        "  0.0000000E+00 0.0000000E+00 -189.2811     -161.1908     0.0000000E+00 211000\n" +
                        "  0.0000000E+00 0.0000000E+00 -182.4967     -151.2435     0.0000000E+00 121000\n" +
                        "  0.0000000E+00 0.0000000E+00 -73.84694     -51.56900     0.0000000E+00 031000\n" +
                        "  -265.9614      23.03839     0.0000000E+00 0.0000000E+00 -702.4949     202000\n" +
                        "   747.8200      55.21756     0.0000000E+00 0.0000000E+00 -434.0674     112000\n" +
                        "   518.3903      156.6547     0.0000000E+00 0.0000000E+00 -350.4902     022000\n" +
                        "  0.0000000E+00 0.0000000E+00 -15241.28     -2464.024     0.0000000E+00 103000\n" +
                        "  0.0000000E+00 0.0000000E+00  21899.89      818.2527     0.0000000E+00 013000\n" +
                        "  -1173.035     -946.7458     0.0000000E+00 0.0000000E+00 -277063.4     004000\n" +
                        "  0.0000000E+00 0.0000000E+00 -120.1122     -84.53798     0.0000000E+00 300100\n" +
                        "  0.0000000E+00 0.0000000E+00 -258.7845     -220.8303     0.0000000E+00 210100\n" +
                        "  0.0000000E+00 0.0000000E+00 -252.5141     -209.2836     0.0000000E+00 120100\n" +
                        "  0.0000000E+00 0.0000000E+00 -90.68266     -63.01210     0.0000000E+00 030100\n" +
                        "  -733.4293      69.51263     0.0000000E+00 0.0000000E+00 -2081.504     201100\n" +
                        "   2089.351      164.9353     0.0000000E+00 0.0000000E+00 -1375.117     111100\n" +
                        "   1447.967      441.4078     0.0000000E+00 0.0000000E+00 -1397.755     021100\n" +
                        "  0.0000000E+00 0.0000000E+00 -63543.38     -10286.76     0.0000000E+00 102100\n" +
                        "  0.0000000E+00 0.0000000E+00  91310.07      3431.205     0.0000000E+00 012100\n" +
                        "  -6357.655     -5209.714     0.0000000E+00 0.0000000E+00 -1541044.     003100\n" +
                        "  -7.841745     -1.368648     0.0000000E+00 0.0000000E+00 0.4551204     300001\n" +
                        "  -32.13051     -16.46628     0.0000000E+00 0.0000000E+00  2.127066     210001\n" +
                        "  -43.38218     -29.43339     0.0000000E+00 0.0000000E+00  3.373522     120001\n" +
                        "  -20.73970     -16.39270     0.0000000E+00 0.0000000E+00  1.035320     030001\n" +
                        "  0.0000000E+00 0.0000000E+00 -98.81619     -25.19670     0.0000000E+00 201001\n" +
                        "  0.0000000E+00 0.0000000E+00 -125.6492     -58.08908     0.0000000E+00 111001\n" +
                        "  0.0000000E+00 0.0000000E+00 -44.46196     -31.58770     0.0000000E+00 021001\n" +
                        "  -1885.996     -76.56270     0.0000000E+00 0.0000000E+00 -633.0652     102001\n" +
                        "  -1166.413     -346.8292     0.0000000E+00 0.0000000E+00  264.3123     012001\n" +
                        "  0.0000000E+00 0.0000000E+00 -58688.07     -5014.276     0.0000000E+00 003001\n" +
                        "  -505.7458      52.02019     0.0000000E+00 0.0000000E+00 -1536.135     200200\n" +
                        "   1458.233      122.0897     0.0000000E+00 0.0000000E+00 -1073.812     110200\n" +
                        "   1010.578      311.2052     0.0000000E+00 0.0000000E+00 -1267.177     020200\n" +
                        "  0.0000000E+00 0.0000000E+00 -88307.81     -14315.14     0.0000000E+00 101200\n" +
                        "  0.0000000E+00 0.0000000E+00  126902.3      4794.943     0.0000000E+00 011200\n" +
                        "  -12912.93     -10749.00     0.0000000E+00 0.0000000E+00 -3214408.     002200\n" +
                        "  0.0000000E+00 0.0000000E+00 -175.7515     -54.10092     0.0000000E+00 200101\n" +
                        "  0.0000000E+00 0.0000000E+00 -224.8730     -111.3468     0.0000000E+00 110101\n" +
                        "  0.0000000E+00 0.0000000E+00 -149.6249     -93.07702     0.0000000E+00 020101\n" +
                        "  -5886.354     -398.1582     0.0000000E+00 0.0000000E+00 -1953.567     101101\n" +
                        "  -3245.487     -1259.939     0.0000000E+00 0.0000000E+00  1082.483     011101\n" +
                        "  0.0000000E+00 0.0000000E+00 -285973.4     -25378.05     0.0000000E+00 002101\n" +
                        "   1.386746     0.6447928     0.0000000E+00 0.0000000E+00 -2.525419     200002\n" +
                        "   2.259780      1.672150     0.0000000E+00 0.0000000E+00 -5.600859     110002\n" +
                        "   2.333725      1.553800     0.0000000E+00 0.0000000E+00 -3.342364     020002\n" +
                        "  0.0000000E+00 0.0000000E+00 -31.03381     -16.45060     0.0000000E+00 101002\n" +
                        "  0.0000000E+00 0.0000000E+00 -25.72546     -19.84163     0.0000000E+00 011002\n" +
                        "   136.7729      7.059545     0.0000000E+00 0.0000000E+00 -1258.732     002002\n" +
                        "  0.0000000E+00 0.0000000E+00 -40913.15     -6644.168     0.0000000E+00 100300\n" +
                        "  0.0000000E+00 0.0000000E+00  58798.53      2240.080     0.0000000E+00 010300\n" +
                        "  -11647.83     -9855.806     0.0000000E+00 0.0000000E+00 -2980416.     001300\n" +
                        "  -4538.708     -406.0634     0.0000000E+00 0.0000000E+00 -1491.786     100201\n" +
                        "  -2259.272     -1083.537     0.0000000E+00 0.0000000E+00  993.8915     010201\n" +
                        "  0.0000000E+00 0.0000000E+00 -454900.5     -41517.10     0.0000000E+00 001201\n" +
                        "  0.0000000E+00 0.0000000E+00 -64.72453     -37.34708     0.0000000E+00 100102\n" +
                        "  0.0000000E+00 0.0000000E+00 -28.08331     -26.48844     0.0000000E+00 010102\n" +
                        "   424.3109      31.48764     0.0000000E+00 0.0000000E+00 -4818.248     001102\n" +
                        "  -1.466550    -0.9088289E-01 0.0000000E+00 0.0000000E+00 0.3354476     100003\n" +
                        "  -3.262269     -1.445727     0.0000000E+00 0.0000000E+00 0.2465195     010003\n" +
                        "  0.0000000E+00 0.0000000E+00 -40.38090     -10.39748     0.0000000E+00 001003\n" +
                        "  -3936.900     -3388.396     0.0000000E+00 0.0000000E+00 -1036597.     000400\n" +
                        "  0.0000000E+00 0.0000000E+00 -237463.7     -22182.41     0.0000000E+00 000301\n" +
                        "   325.9515      30.52320     0.0000000E+00 0.0000000E+00 -4415.849     000202\n" +
                        "  0.0000000E+00 0.0000000E+00 -117.6920     -40.15886     0.0000000E+00 000103\n" +
                        "  0.1348368     0.2556558E-01 0.0000000E+00 0.0000000E+00-0.3944071     000004\n" +
                        "   9.656912      2.307331     0.0000000E+00 0.0000000E+00-0.4017641     500000\n" +
                        "   38.17803      15.64747     0.0000000E+00 0.0000000E+00 -4.912046     410000\n" +
                        "   67.05196      38.28289     0.0000000E+00 0.0000000E+00 -15.71294     320000\n" +
                        "   76.16705      54.43496     0.0000000E+00 0.0000000E+00 -22.30434     230000\n" +
                        "   55.65038      45.48041     0.0000000E+00 0.0000000E+00 -15.49842     140000\n" +
                        "   18.86184      16.66821     0.0000000E+00 0.0000000E+00 -3.931545     050000\n" +
                        "  0.0000000E+00 0.0000000E+00  107.9299     -26.71790     0.0000000E+00 401000\n" +
                        "  0.0000000E+00 0.0000000E+00  147.9799     -134.7657     0.0000000E+00 311000\n" +
                        "  0.0000000E+00 0.0000000E+00 -110.7493     -249.8471     0.0000000E+00 221000\n" +
                        "  0.0000000E+00 0.0000000E+00 -273.0861     -189.2222     0.0000000E+00 131000\n" +
                        "  0.0000000E+00 0.0000000E+00 -135.7927     -53.37362     0.0000000E+00 041000\n" +
                        "   4330.986     -17.32952     0.0000000E+00 0.0000000E+00  1554.592     302000\n" +
                        "   10817.35      480.4172     0.0000000E+00 0.0000000E+00  2550.519     212000\n" +
                        "   11924.24      912.6808     0.0000000E+00 0.0000000E+00  2481.396     122000\n" +
                        "   5021.281      626.4243     0.0000000E+00 0.0000000E+00  1080.485     032000\n" +
                        "  0.0000000E+00 0.0000000E+00  56534.13      6167.387     0.0000000E+00 203000\n" +
                        "  0.0000000E+00 0.0000000E+00  35443.10      9221.059     0.0000000E+00 113000\n" +
                        "  0.0000000E+00 0.0000000E+00  33888.36      6451.408     0.0000000E+00 023000\n" +
                        "   408639.5      8035.232     0.0000000E+00 0.0000000E+00  269614.8     104000\n" +
                        "  -615.4679      31327.81     0.0000000E+00 0.0000000E+00 -398741.3     014000\n" +
                        "  0.0000000E+00 0.0000000E+00 0.1744342E+08  711248.6     0.0000000E+00 005000\n" +
                        "  0.0000000E+00 0.0000000E+00  169.5838     -24.64874     0.0000000E+00 400100\n" +
                        "  0.0000000E+00 0.0000000E+00  263.4084     -148.3145     0.0000000E+00 310100\n" +
                        "  0.0000000E+00 0.0000000E+00 -61.42800     -287.8286     0.0000000E+00 220100\n" +
                        "  0.0000000E+00 0.0000000E+00 -308.5305     -215.0706     0.0000000E+00 130100\n" +
                        "  0.0000000E+00 0.0000000E+00 -143.4608     -45.75517     0.0000000E+00 040100\n" +
                        "   12222.54      3.407227     0.0000000E+00 0.0000000E+00  4351.562     301100\n" +
                        "   30309.63      1483.628     0.0000000E+00 0.0000000E+00  7011.334     211100\n" +
                        "   33751.58      2814.916     0.0000000E+00 0.0000000E+00  6920.125     121100\n" +
                        "   13958.15      2017.171     0.0000000E+00 0.0000000E+00  2670.030     031100\n" +
                        "  0.0000000E+00 0.0000000E+00  247493.2      26978.55     0.0000000E+00 202100\n" +
                        "  0.0000000E+00 0.0000000E+00  163158.8      40079.06     0.0000000E+00 112100\n" +
                        "  0.0000000E+00 0.0000000E+00  180149.5      31070.67     0.0000000E+00 022100\n" +
                        "   2273268.      44916.66     0.0000000E+00 0.0000000E+00  1498853.     103100\n" +
                        "  -2515.413      174654.8     0.0000000E+00 0.0000000E+00 -2216839.     013100\n" +
                        "  0.0000000E+00 0.0000000E+00 0.1212823E+09  4950504.     0.0000000E+00 004100\n" +
                        "  -3.939267     -2.707336     0.0000000E+00 0.0000000E+00  11.24863     400001\n" +
                        "  -17.84379     -11.83151     0.0000000E+00 0.0000000E+00  45.65774     310001\n" +
                        "  -31.65896     -20.21823     0.0000000E+00 0.0000000E+00  67.24049     220001\n" +
                        "  -23.21314     -18.04001     0.0000000E+00 0.0000000E+00  44.91253     130001\n" +
                        "  -8.275515     -7.740140     0.0000000E+00 0.0000000E+00  11.79559     040001\n" +
                        "  0.0000000E+00 0.0000000E+00  152.3938      79.48404     0.0000000E+00 301001\n" +
                        "  0.0000000E+00 0.0000000E+00  432.2396      327.1391     0.0000000E+00 211001\n" +
                        "  0.0000000E+00 0.0000000E+00  464.7975      413.4304     0.0000000E+00 121001\n" +
                        "  0.0000000E+00 0.0000000E+00  239.4334      183.3819     0.0000000E+00 031001\n" +
                        "   768.9741     -58.49835     0.0000000E+00 0.0000000E+00  3244.885     202001\n" +
                        "  -2381.703      84.24339     0.0000000E+00 0.0000000E+00  3090.545     112001\n" +
                        "  -3034.100     -137.4040     0.0000000E+00 0.0000000E+00  1043.678     022001\n" +
                        "  0.0000000E+00 0.0000000E+00  69421.62      7403.786     0.0000000E+00 103001\n" +
                        "  0.0000000E+00 0.0000000E+00 -35274.69      616.4296     0.0000000E+00 013001\n" +
                        "  -23961.99      1878.913     0.0000000E+00 0.0000000E+00  1154742.     004001\n" +
                        "   8621.108      37.55291     0.0000000E+00 0.0000000E+00  3044.998     300200\n" +
                        "   21239.43      1141.942     0.0000000E+00 0.0000000E+00  4818.034     210200\n" +
                        "   23894.81      2168.123     0.0000000E+00 0.0000000E+00  4823.880     120200\n" +
                        "   9710.528      1604.934     0.0000000E+00 0.0000000E+00  1623.585     030200\n" +
                        "  0.0000000E+00 0.0000000E+00  360381.3      39272.72     0.0000000E+00 201200\n" +
                        "  0.0000000E+00 0.0000000E+00  248227.4      58018.47     0.0000000E+00 111200\n" +
                        "  0.0000000E+00 0.0000000E+00  304467.9      49023.33     0.0000000E+00 021200\n" +
                        "   4742596.      94188.54     0.0000000E+00 0.0000000E+00  3124717.     102200\n" +
                        "  -3049.584      365293.7     0.0000000E+00 0.0000000E+00 -4621794.     012200\n" +
                        "  0.0000000E+00 0.0000000E+00 0.3373184E+09 0.1378419E+08 0.0000000E+00 003200\n" +
                        "  0.0000000E+00 0.0000000E+00  268.6376      150.8983     0.0000000E+00 300101\n" +
                        "  0.0000000E+00 0.0000000E+00  716.7776      556.8164     0.0000000E+00 210101\n" +
                        "  0.0000000E+00 0.0000000E+00  772.8322      680.4407     0.0000000E+00 120101\n" +
                        "  0.0000000E+00 0.0000000E+00  358.8226      276.8699     0.0000000E+00 030101\n" +
                        "   2444.313     -206.6507     0.0000000E+00 0.0000000E+00  10340.75     201101\n" +
                        "  -7670.287      127.9247     0.0000000E+00 0.0000000E+00  9806.978     111101\n" +
                        "  -9180.022     -619.1458     0.0000000E+00 0.0000000E+00  5052.711     021101\n" +
                        "  0.0000000E+00 0.0000000E+00  318286.9      35673.14     0.0000000E+00 102101\n" +
                        "  0.0000000E+00 0.0000000E+00 -191921.8      817.8323     0.0000000E+00 012101\n" +
                        "  -130877.5      12896.35     0.0000000E+00 0.0000000E+00  7160981.     003101\n" +
                        "   9.592955    -0.5752630     0.0000000E+00 0.0000000E+00 -2.419925     300002\n" +
                        "   43.99780      10.34373     0.0000000E+00 0.0000000E+00 -8.239325     210002\n" +
                        "   66.10141      30.81284     0.0000000E+00 0.0000000E+00 -12.06509     120002\n" +
                        "   36.97828      23.09166     0.0000000E+00 0.0000000E+00 -5.411021     030002\n" +
                        "  0.0000000E+00 0.0000000E+00  209.2070      29.02878     0.0000000E+00 201002\n" +
                        "  0.0000000E+00 0.0000000E+00  380.5102      87.96376     0.0000000E+00 111002\n" +
                        "  0.0000000E+00 0.0000000E+00  148.0574      60.34649     0.0000000E+00 021002\n" +
                        "   3901.444     -44.00168     0.0000000E+00 0.0000000E+00  1191.711     102002\n" +
                        "   3810.707      349.2094     0.0000000E+00 0.0000000E+00  30.38843     012002\n" +
                        "  0.0000000E+00 0.0000000E+00  115572.1      8614.544     0.0000000E+00 003002\n" +
                        "  0.0000000E+00 0.0000000E+00  174599.2      19037.96     0.0000000E+00 200300\n" +
                        "  0.0000000E+00 0.0000000E+00  125011.9      27992.04     0.0000000E+00 110300\n" +
                        "  0.0000000E+00 0.0000000E+00  166212.0      25474.37     0.0000000E+00 020300\n" +
                        "   4398199.      87962.62     0.0000000E+00 0.0000000E+00  2895414.     101300\n" +
                        "  -509.2309      339944.1     0.0000000E+00 0.0000000E+00 -4282871.     011300\n" +
                        "  0.0000000E+00 0.0000000E+00 0.4691375E+09 0.1919591E+08 0.0000000E+00 002300\n" +
                        "   1907.979     -177.8598     0.0000000E+00 0.0000000E+00  8190.217     200201\n" +
                        "  -6065.688      7.168367     0.0000000E+00 0.0000000E+00  7776.241     110201\n" +
                        "  -6902.394     -600.5576     0.0000000E+00 0.0000000E+00  5293.552     020201\n" +
                        "  0.0000000E+00 0.0000000E+00  482429.4      56271.62     0.0000000E+00 101201\n" +
                        "  0.0000000E+00 0.0000000E+00 -329047.7     -1313.204     0.0000000E+00 011201\n" +
                        "  -268324.2      31884.92     0.0000000E+00 0.0000000E+00 0.1647643E+08 002201\n" +
                        "  0.0000000E+00 0.0000000E+00  392.7669      73.56667     0.0000000E+00 200102\n" +
                        "  0.0000000E+00 0.0000000E+00  673.5736      193.5795     0.0000000E+00 110102\n" +
                        "  0.0000000E+00 0.0000000E+00  376.9852      176.2883     0.0000000E+00 020102\n" +
                        "   13821.51      112.1802     0.0000000E+00 0.0000000E+00  4247.743     101102\n" +
                        "   12154.92      1645.383     0.0000000E+00 0.0000000E+00 -554.7770     011102\n" +
                        "  0.0000000E+00 0.0000000E+00  628728.7      49163.51     0.0000000E+00 002102\n" +
                        "  -2.357056    -0.4187189     0.0000000E+00 0.0000000E+00  3.701923     200003\n" +
                        "  -4.399650     -1.789508     0.0000000E+00 0.0000000E+00  10.20489     110003\n" +
                        "  -3.236181     -1.710872     0.0000000E+00 0.0000000E+00  6.627860     020003\n" +
                        "  0.0000000E+00 0.0000000E+00  20.56859      14.23185     0.0000000E+00 101003\n" +
                        "  0.0000000E+00 0.0000000E+00  34.82612      26.13628     0.0000000E+00 011003\n" +
                        "  -304.8830      1.037374     0.0000000E+00 0.0000000E+00  1876.273     002003\n" +
                        "   1530003.      30920.28     0.0000000E+00 0.0000000E+00  1006232.     100400\n" +
                        "   726.9025      118848.6     0.0000000E+00 0.0000000E+00 -1488502.     010400\n" +
                        "  0.0000000E+00 0.0000000E+00 0.3262942E+09 0.1337256E+08 0.0000000E+00 001400\n" +
                        "  0.0000000E+00 0.0000000E+00  242079.0      29193.49     0.0000000E+00 100301\n" +
                        "  0.0000000E+00 0.0000000E+00 -181312.5     -1756.471     0.0000000E+00 010301\n" +
                        "  -244783.1      34063.60     0.0000000E+00 0.0000000E+00 0.1670369E+08 001301\n" +
                        "   11889.50      303.5593     0.0000000E+00 0.0000000E+00  3659.456     100202\n" +
                        "   9537.780      1714.689     0.0000000E+00 0.0000000E+00 -949.7714     010202\n" +
                        "  0.0000000E+00 0.0000000E+00  1105581.      89774.30     0.0000000E+00 001202\n" +
                        "  0.0000000E+00 0.0000000E+00  63.49312      40.79473     0.0000000E+00 100103\n" +
                        "  0.0000000E+00 0.0000000E+00  57.15400      47.22687     0.0000000E+00 010103\n" +
                        "  -1061.677     -14.92079     0.0000000E+00 0.0000000E+00  7866.032     001103\n" +
                        "   1.247730    -0.4247168E-01 0.0000000E+00 0.0000000E+00-0.5500342     100004\n" +
                        "   3.581958      1.145171     0.0000000E+00 0.0000000E+00-0.6010474     010004\n" +
                        "  0.0000000E+00 0.0000000E+00  42.71228      10.24821     0.0000000E+00 001004\n" +
                        "  0.0000000E+00 0.0000000E+00 0.9080061E+08  3728826.     0.0000000E+00 000500\n" +
                        "  -83856.40      13363.87     0.0000000E+00 0.0000000E+00  6305266.     000401\n" +
                        "  0.0000000E+00 0.0000000E+00  632513.9      53003.85     0.0000000E+00 000302\n" +
                        "  -901.9275     -27.13114     0.0000000E+00 0.0000000E+00  7855.847     000203\n" +
                        "  0.0000000E+00 0.0000000E+00  134.6015      41.96554     0.0000000E+00 000104\n" +
                        " -0.1205991    -0.9168539E-02 0.0000000E+00 0.0000000E+00 0.3682734     000005"
        );
    }

    private CosyArbitraryOrder.CosyMapArbitraryOrder cosyMap左偏n1负数弯铁(){
        return CosyArbitraryOrder.readMap(
                "  -1.804579     -1.878998     0.0000000E+00 0.0000000E+00 0.2274887     100000\n" +
                        "  -1.200908     -1.804579     0.0000000E+00 0.0000000E+00 0.3395480     010000\n" +
                        "  0.0000000E+00 0.0000000E+00  5.238616      3.151350     0.0000000E+00 001000\n" +
                        "  0.0000000E+00 0.0000000E+00  8.391039      5.238616     0.0000000E+00 000100\n" +
                        "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00  1.000000     000010\n" +
                        " -0.3395480    -0.2274887     0.0000000E+00 0.0000000E+00 0.5301606     000001\n" +
                        "   1.254004     0.4612533     0.0000000E+00 0.0000000E+00 -1.239372     200000\n" +
                        "   3.141475      1.376927     0.0000000E+00 0.0000000E+00 -2.146909     110000\n" +
                        "   2.344471      1.328575     0.0000000E+00 0.0000000E+00 -1.306209     020000\n" +
                        "  0.0000000E+00 0.0000000E+00 -3.797048     -2.514083     0.0000000E+00 101000\n" +
                        "  0.0000000E+00 0.0000000E+00 -5.667447     -3.752501     0.0000000E+00 011000\n" +
                        " -0.8989034    -0.6022429     0.0000000E+00 0.0000000E+00 -3.401781     002000\n" +
                        "  0.0000000E+00 0.0000000E+00 -5.929774     -3.797048     0.0000000E+00 100100\n" +
                        "  0.0000000E+00 0.0000000E+00 -8.850737     -5.667447     0.0000000E+00 010100\n" +
                        "  -1.797807     -1.204486     0.0000000E+00 0.0000000E+00 -11.41688     001100\n" +
                        "   1.034461    -0.4390503     0.0000000E+00 0.0000000E+00-0.3479873     100001\n" +
                        "   2.136072     0.8975222     0.0000000E+00 0.0000000E+00-0.5194033     010001\n" +
                        "  0.0000000E+00 0.0000000E+00 -2.719710    -0.3373402     0.0000000E+00 001001\n" +
                        " -0.5950741    -0.3986848     0.0000000E+00 0.0000000E+00 -9.920888     000200\n" +
                        "  0.0000000E+00 0.0000000E+00 -8.143939     -2.719710     0.0000000E+00 000101\n" +
                        "  0.1923601    -0.2633801E-02 0.0000000E+00 0.0000000E+00-0.4620144     000002\n" +
                        "  -3.192021     0.8288008     0.0000000E+00 0.0000000E+00 0.8461535     300000\n" +
                        "  -8.981975      2.186050     0.0000000E+00 0.0000000E+00  3.091918     210000\n" +
                        "  -8.281136      2.462057     0.0000000E+00 0.0000000E+00  4.041519     120000\n" +
                        "  -3.556313    -0.4714067E-01 0.0000000E+00 0.0000000E+00  1.903653     030000\n" +
                        "  0.0000000E+00 0.0000000E+00  6.998145     0.1635663     0.0000000E+00 201000\n" +
                        "  0.0000000E+00 0.0000000E+00  13.76198     0.7073968     0.0000000E+00 111000\n" +
                        "  0.0000000E+00 0.0000000E+00  7.556628     0.9775495     0.0000000E+00 021000\n" +
                        "  -9.969656     0.9878167     0.0000000E+00 0.0000000E+00  4.584935     102000\n" +
                        "  -9.916388     0.3016957     0.0000000E+00 0.0000000E+00  7.483126     012000\n" +
                        "  0.0000000E+00 0.0000000E+00  20.68495      2.153831     0.0000000E+00 003000\n" +
                        "  0.0000000E+00 0.0000000E+00  12.13545     0.6576764     0.0000000E+00 200100\n" +
                        "  0.0000000E+00 0.0000000E+00  23.16096      1.583279     0.0000000E+00 110100\n" +
                        "  0.0000000E+00 0.0000000E+00  15.43746      3.504297     0.0000000E+00 020100\n" +
                        "  -33.17466      3.540082     0.0000000E+00 0.0000000E+00  15.66825     101100\n" +
                        "  -32.72636      1.454540     0.0000000E+00 0.0000000E+00  24.84534     011100\n" +
                        "  0.0000000E+00 0.0000000E+00  102.6339      10.03553     0.0000000E+00 002100\n" +
                        "  -1.336941     0.5506058     0.0000000E+00 0.0000000E+00  1.439682     200001\n" +
                        "  -4.421431     0.5119864     0.0000000E+00 0.0000000E+00  4.291850     110001\n" +
                        "  -3.977275    -0.4619316     0.0000000E+00 0.0000000E+00  3.257476     020001\n" +
                        "  0.0000000E+00 0.0000000E+00  3.867547     0.2543971     0.0000000E+00 101001\n" +
                        "  0.0000000E+00 0.0000000E+00  8.047561      2.483934     0.0000000E+00 011001\n" +
                        " -0.8332563     0.3655402E-01 0.0000000E+00 0.0000000E+00  5.490256     002001\n" +
                        "  -27.53078      3.254581     0.0000000E+00 0.0000000E+00  13.27821     100200\n" +
                        "  -27.99817     0.5438452     0.0000000E+00 0.0000000E+00  20.67072     010200\n" +
                        "  0.0000000E+00 0.0000000E+00  170.7545      16.16004     0.0000000E+00 001200\n" +
                        "  0.0000000E+00 0.0000000E+00  9.050944      2.332276     0.0000000E+00 100101\n" +
                        "  0.0000000E+00 0.0000000E+00  16.82803      6.573096     0.0000000E+00 010101\n" +
                        "  -2.264325     0.9355582     0.0000000E+00 0.0000000E+00  23.21075     001101\n" +
                        " -0.6624033     0.2782172     0.0000000E+00 0.0000000E+00 0.4211248     100002\n" +
                        "  -2.205418    -0.4146171     0.0000000E+00 0.0000000E+00 0.7720046     010002\n" +
                        "  0.0000000E+00 0.0000000E+00  2.289810     0.2457403     0.0000000E+00 001002\n" +
                        "  0.0000000E+00 0.0000000E+00  98.14097      10.74788     0.0000000E+00 000300\n" +
                        "  -2.250893     0.8983562     0.0000000E+00 0.0000000E+00  23.96537     000201\n" +
                        "  0.0000000E+00 0.0000000E+00  8.279481      2.196873     0.0000000E+00 000102\n" +
                        " -0.1348658     0.1283897E-01 0.0000000E+00 0.0000000E+00 0.4078950     000003\n" +
                        "   2.638210    -0.7915134     0.0000000E+00 0.0000000E+00 -2.256529     400000\n" +
                        "   13.38265     -2.730579     0.0000000E+00 0.0000000E+00 -8.791773     310000\n" +
                        "   26.04280     -3.637687     0.0000000E+00 0.0000000E+00 -12.77757     220000\n" +
                        "   23.46513     -1.859302     0.0000000E+00 0.0000000E+00 -9.738154     130000\n" +
                        "   8.645093     0.1518824     0.0000000E+00 0.0000000E+00 -3.616485     040000\n" +
                        "  0.0000000E+00 0.0000000E+00 -9.323196    -0.2636941     0.0000000E+00 301000\n" +
                        "  0.0000000E+00 0.0000000E+00 -33.45280     -1.236330     0.0000000E+00 211000\n" +
                        "  0.0000000E+00 0.0000000E+00 -40.11276    -0.8937156     0.0000000E+00 121000\n" +
                        "  0.0000000E+00 0.0000000E+00 -18.44301     -1.642790     0.0000000E+00 031000\n" +
                        "   14.53491     -1.938169     0.0000000E+00 0.0000000E+00 -18.09294     202000\n" +
                        "   39.32253     -4.935992     0.0000000E+00 0.0000000E+00 -37.79135     112000\n" +
                        "   26.81605     -2.686161     0.0000000E+00 0.0000000E+00 -21.20200     022000\n" +
                        "  0.0000000E+00 0.0000000E+00 -41.82264     -2.376395     0.0000000E+00 103000\n" +
                        "  0.0000000E+00 0.0000000E+00 -66.92442     -4.876004     0.0000000E+00 013000\n" +
                        "  -2.763150     0.7126392     0.0000000E+00 0.0000000E+00 -27.63247     004000\n" +
                        "  0.0000000E+00 0.0000000E+00 -15.29639    -0.7609268     0.0000000E+00 300100\n" +
                        "  0.0000000E+00 0.0000000E+00 -54.19295     -2.607985     0.0000000E+00 210100\n" +
                        "  0.0000000E+00 0.0000000E+00 -66.77282     -3.169023     0.0000000E+00 120100\n" +
                        "  0.0000000E+00 0.0000000E+00 -32.77713     -4.649131     0.0000000E+00 030100\n" +
                        "   50.48130     -7.057291     0.0000000E+00 0.0000000E+00 -60.53849     201100\n" +
                        "   133.7076     -16.98107     0.0000000E+00 0.0000000E+00 -124.8926     111100\n" +
                        "   89.13913     -9.609510     0.0000000E+00 0.0000000E+00 -73.50208     021100\n" +
                        "  0.0000000E+00 0.0000000E+00 -208.7658     -12.27371     0.0000000E+00 102100\n" +
                        "  0.0000000E+00 0.0000000E+00 -330.9775     -24.94747     0.0000000E+00 012100\n" +
                        "  -14.56516      3.983502     0.0000000E+00 0.0000000E+00 -182.3168     003100\n" +
                        "   3.639669    -0.7848466     0.0000000E+00 0.0000000E+00 -1.695563     300001\n" +
                        "   18.12818     -3.233609     0.0000000E+00 0.0000000E+00 -7.265218     210001\n" +
                        "   24.43056     -4.508377     0.0000000E+00 0.0000000E+00 -10.83996     120001\n" +
                        "   12.23013    -0.7722101     0.0000000E+00 0.0000000E+00 -5.928947     030001\n" +
                        "  0.0000000E+00 0.0000000E+00 -11.12806    -0.3636795     0.0000000E+00 201001\n" +
                        "  0.0000000E+00 0.0000000E+00 -32.03480    -0.9548043     0.0000000E+00 111001\n" +
                        "  0.0000000E+00 0.0000000E+00 -22.14466     -1.247994     0.0000000E+00 021001\n" +
                        "   17.11085     -1.564152     0.0000000E+00 0.0000000E+00 -11.73279     102001\n" +
                        "   25.06545     -1.678321     0.0000000E+00 0.0000000E+00 -20.25665     012001\n" +
                        "  0.0000000E+00 0.0000000E+00 -39.22937     -2.318983     0.0000000E+00 003001\n" +
                        "   43.31023     -6.357609     0.0000000E+00 0.0000000E+00 -50.58005     200200\n" +
                        "   114.0503     -14.17502     0.0000000E+00 0.0000000E+00 -104.4925     110200\n" +
                        "   76.08754     -7.378234     0.0000000E+00 0.0000000E+00 -65.32128     020200\n" +
                        "  0.0000000E+00 0.0000000E+00 -347.4883     -21.42725     0.0000000E+00 101200\n" +
                        "  0.0000000E+00 0.0000000E+00 -547.9833     -44.33291     0.0000000E+00 011200\n" +
                        "  -26.96247      8.180953     0.0000000E+00 0.0000000E+00 -452.8005     002200\n" +
                        "  0.0000000E+00 0.0000000E+00 -24.00929     -1.096517     0.0000000E+00 200101\n" +
                        "  0.0000000E+00 0.0000000E+00 -63.42521     -2.769300     0.0000000E+00 110101\n" +
                        "  0.0000000E+00 0.0000000E+00 -48.33560     -6.135843     0.0000000E+00 020101\n" +
                        "   71.22118     -6.988077     0.0000000E+00 0.0000000E+00 -46.00141     101101\n" +
                        "   97.15992     -6.533565     0.0000000E+00 0.0000000E+00 -77.77014     011101\n" +
                        "  0.0000000E+00 0.0000000E+00 -239.1001     -16.08255     0.0000000E+00 002101\n" +
                        "   1.354737    -0.4294799     0.0000000E+00 0.0000000E+00 -1.578333     200002\n" +
                        "   5.847387     -1.046749     0.0000000E+00 0.0000000E+00 -6.097485     110002\n" +
                        "   5.853647    -0.1805738     0.0000000E+00 0.0000000E+00 -5.550910     020002\n" +
                        "  0.0000000E+00 0.0000000E+00 -4.348853    -0.2051101     0.0000000E+00 101002\n" +
                        "  0.0000000E+00 0.0000000E+00 -10.41281     -2.039620     0.0000000E+00 011002\n" +
                        "   1.940300    -0.1345892     0.0000000E+00 0.0000000E+00 -7.645428     002002\n" +
                        "  0.0000000E+00 0.0000000E+00 -195.0201     -14.06518     0.0000000E+00 100300\n" +
                        "  0.0000000E+00 0.0000000E+00 -306.7427     -29.08397     0.0000000E+00 010300\n" +
                        "  -20.59258      6.628349     0.0000000E+00 0.0000000E+00 -505.4706     001300\n" +
                        "   70.89935     -7.577075     0.0000000E+00 0.0000000E+00 -44.22377     100201\n" +
                        "   94.15558     -5.125698     0.0000000E+00 0.0000000E+00 -73.53232     010201\n" +
                        "  0.0000000E+00 0.0000000E+00 -470.4475     -33.89602     0.0000000E+00 001201\n" +
                        "  0.0000000E+00 0.0000000E+00 -11.92051     -1.909755     0.0000000E+00 100102\n" +
                        "  0.0000000E+00 0.0000000E+00 -25.35028     -6.950495     0.0000000E+00 010102\n" +
                        "   7.295705     -1.138068     0.0000000E+00 0.0000000E+00 -36.70139     001102\n" +
                        "  0.5034812    -0.2159935     0.0000000E+00 0.0000000E+00-0.4574475     100003\n" +
                        "   2.193404     0.2036113     0.0000000E+00 0.0000000E+00 -1.004416     010003\n" +
                        "  0.0000000E+00 0.0000000E+00 -2.111209    -0.2050543     0.0000000E+00 001003\n" +
                        "  -4.862951      1.718210     0.0000000E+00 0.0000000E+00 -215.7925     000400\n" +
                        "  0.0000000E+00 0.0000000E+00 -308.5619     -26.10016     0.0000000E+00 000301\n" +
                        "   7.401434     -1.406862     0.0000000E+00 0.0000000E+00 -42.32073     000202\n" +
                        "  0.0000000E+00 0.0000000E+00 -8.552181     -1.947754     0.0000000E+00 000103\n" +
                        "  0.1027073    -0.1589260E-01 0.0000000E+00 0.0000000E+00-0.3693367     000004\n" +
                        "  -5.278285     0.9469319     0.0000000E+00 0.0000000E+00  2.210752     500000\n" +
                        "  -27.76976      4.329925     0.0000000E+00 0.0000000E+00  13.74267     410000\n" +
                        "  -57.14658      8.622598     0.0000000E+00 0.0000000E+00  34.32990     320000\n" +
                        "  -65.25681      10.08495     0.0000000E+00 0.0000000E+00  43.55423     230000\n" +
                        "  -43.25142      6.883573     0.0000000E+00 0.0000000E+00  28.68430     140000\n" +
                        "  -13.32991      1.538737     0.0000000E+00 0.0000000E+00  8.259814     050000\n" +
                        "  0.0000000E+00 0.0000000E+00  15.29747     0.1285202     0.0000000E+00 401000\n" +
                        "  0.0000000E+00 0.0000000E+00  66.42079      1.268612     0.0000000E+00 311000\n" +
                        "  0.0000000E+00 0.0000000E+00  110.0008      3.280743     0.0000000E+00 221000\n" +
                        "  0.0000000E+00 0.0000000E+00  91.60075      3.221520     0.0000000E+00 131000\n" +
                        "  0.0000000E+00 0.0000000E+00  32.95858      1.371668     0.0000000E+00 041000\n" +
                        "  -52.56901      3.650795     0.0000000E+00 0.0000000E+00  30.39067     302000\n" +
                        "  -169.1620      10.39041     0.0000000E+00 0.0000000E+00  110.9405     212000\n" +
                        "  -185.0886      11.68213     0.0000000E+00 0.0000000E+00  135.7882     122000\n" +
                        "  -76.59160      5.235841     0.0000000E+00 0.0000000E+00  58.75206     032000\n" +
                        "  0.0000000E+00 0.0000000E+00  128.2077      4.407878     0.0000000E+00 203000\n" +
                        "  0.0000000E+00 0.0000000E+00  290.1928      9.183494     0.0000000E+00 113000\n" +
                        "  0.0000000E+00 0.0000000E+00  180.8416      7.322502     0.0000000E+00 023000\n" +
                        "  -79.28765      2.660046     0.0000000E+00 0.0000000E+00  72.11528     104000\n" +
                        "  -76.75016      1.430800     0.0000000E+00 0.0000000E+00  116.0923     014000\n" +
                        "  0.0000000E+00 0.0000000E+00  170.4840      5.644409     0.0000000E+00 005000\n" +
                        "  0.0000000E+00 0.0000000E+00  26.35023     0.5902677     0.0000000E+00 400100\n" +
                        "  0.0000000E+00 0.0000000E+00  111.8838      3.198052     0.0000000E+00 310100\n" +
                        "  0.0000000E+00 0.0000000E+00  186.2607      6.966868     0.0000000E+00 220100\n" +
                        "  0.0000000E+00 0.0000000E+00  158.0213      6.903357     0.0000000E+00 130100\n" +
                        "  0.0000000E+00 0.0000000E+00  60.03143      4.448754     0.0000000E+00 040100\n" +
                        "  -175.5166      13.04128     0.0000000E+00 0.0000000E+00  102.7180     301100\n" +
                        "  -560.7086      36.65613     0.0000000E+00 0.0000000E+00  370.6759     211100\n" +
                        "  -622.5007      41.75911     0.0000000E+00 0.0000000E+00  455.0994     121100\n" +
                        "  -263.7334      18.42678     0.0000000E+00 0.0000000E+00  202.2670     031100\n" +
                        "  0.0000000E+00 0.0000000E+00  640.3796      22.13206     0.0000000E+00 202100\n" +
                        "  0.0000000E+00 0.0000000E+00  1435.013      46.14544     0.0000000E+00 112100\n" +
                        "  0.0000000E+00 0.0000000E+00  923.6957      40.24761     0.0000000E+00 022100\n" +
                        "  -525.9200      18.48202     0.0000000E+00 0.0000000E+00  481.3625     103100\n" +
                        "  -512.1827      10.55089     0.0000000E+00 0.0000000E+00  768.0212     013100\n" +
                        "  0.0000000E+00 0.0000000E+00  1405.490      47.18487     0.0000000E+00 004100\n" +
                        "  -4.155396      1.232765     0.0000000E+00 0.0000000E+00  4.242188     400001\n" +
                        "  -27.42010      6.325174     0.0000000E+00 0.0000000E+00  24.24757     310001\n" +
                        "  -65.46972      11.17172     0.0000000E+00 0.0000000E+00  46.73292     220001\n" +
                        "  -71.25515      8.301616     0.0000000E+00 0.0000000E+00  41.74578     130001\n" +
                        "  -30.62119      1.500336     0.0000000E+00 0.0000000E+00  16.38145     040001\n" +
                        "  0.0000000E+00 0.0000000E+00  17.47841     0.4234492     0.0000000E+00 301001\n" +
                        "  0.0000000E+00 0.0000000E+00  81.00422      2.884429     0.0000000E+00 211001\n" +
                        "  0.0000000E+00 0.0000000E+00  119.4819      3.820126     0.0000000E+00 121001\n" +
                        "  0.0000000E+00 0.0000000E+00  63.51451      3.879844     0.0000000E+00 031001\n" +
                        "  -40.81153      3.412404     0.0000000E+00 0.0000000E+00  46.79970     202001\n" +
                        "  -120.8111      10.74192     0.0000000E+00 0.0000000E+00  125.0711     112001\n" +
                        "  -91.97954      7.770226     0.0000000E+00 0.0000000E+00  83.71939     022001\n" +
                        "  0.0000000E+00 0.0000000E+00  106.2890      3.927917     0.0000000E+00 103001\n" +
                        "  0.0000000E+00 0.0000000E+00  190.2879      9.642739     0.0000000E+00 013001\n" +
                        "  -5.344026    -0.3892511     0.0000000E+00 0.0000000E+00  79.51709     004001\n" +
                        "  -145.9337      11.54161     0.0000000E+00 0.0000000E+00  86.26423     300200\n" +
                        "  -468.8916      33.25589     0.0000000E+00 0.0000000E+00  309.5080     210200\n" +
                        "  -532.5544      39.43202     0.0000000E+00 0.0000000E+00  383.5740     120200\n" +
                        "  -232.4246      16.26335     0.0000000E+00 0.0000000E+00  175.8650     030200\n" +
                        "  0.0000000E+00 0.0000000E+00  1067.525      37.68595     0.0000000E+00 201200\n" +
                        "  0.0000000E+00 0.0000000E+00  2375.285      78.21911     0.0000000E+00 111200\n" +
                        "  0.0000000E+00 0.0000000E+00  1580.638      73.23780     0.0000000E+00 021200\n" +
                        "  -1312.447      48.77083     0.0000000E+00 0.0000000E+00  1206.020     102200\n" +
                        "  -1291.034      29.83599     0.0000000E+00 0.0000000E+00  1909.753     012200\n" +
                        "  0.0000000E+00 0.0000000E+00  4648.445      158.8179     0.0000000E+00 003200\n" +
                        "  0.0000000E+00 0.0000000E+00  36.10744      1.424497     0.0000000E+00 300101\n" +
                        "  0.0000000E+00 0.0000000E+00  157.0320      7.190157     0.0000000E+00 210101\n" +
                        "  0.0000000E+00 0.0000000E+00  228.1740      10.66193     0.0000000E+00 120101\n" +
                        "  0.0000000E+00 0.0000000E+00  125.2324      12.44261     0.0000000E+00 030101\n" +
                        "  -158.2512      15.19742     0.0000000E+00 0.0000000E+00  182.4382     201101\n" +
                        "  -461.4564      44.65136     0.0000000E+00 0.0000000E+00  467.9603     111101\n" +
                        "  -344.6757      31.36983     0.0000000E+00 0.0000000E+00  318.1566     021101\n" +
                        "  0.0000000E+00 0.0000000E+00  618.3983      25.34072     0.0000000E+00 102101\n" +
                        "  0.0000000E+00 0.0000000E+00  1083.234      59.80121     0.0000000E+00 012101\n" +
                        "  -34.46369     -3.685728     0.0000000E+00 0.0000000E+00  604.4939     003101\n" +
                        "  -3.226132     0.8873344     0.0000000E+00 0.0000000E+00  2.423160     300002\n" +
                        "  -23.65228      4.476250     0.0000000E+00 0.0000000E+00  12.66238     210002\n" +
                        "  -42.17624      7.006067     0.0000000E+00 0.0000000E+00  21.19925     120002\n" +
                        "  -25.06320      2.177092     0.0000000E+00 0.0000000E+00  12.68341     030002\n" +
                        "  0.0000000E+00 0.0000000E+00  13.82673     0.4982053     0.0000000E+00 201002\n" +
                        "  0.0000000E+00 0.0000000E+00  50.72129      1.561465     0.0000000E+00 111002\n" +
                        "  0.0000000E+00 0.0000000E+00  42.14089      1.714462     0.0000000E+00 021002\n" +
                        "  -24.38794      1.967455     0.0000000E+00 0.0000000E+00  20.84820     102002\n" +
                        "  -43.68490      2.997584     0.0000000E+00 0.0000000E+00  39.28822     012002\n" +
                        "  0.0000000E+00 0.0000000E+00  58.72723      2.754263     0.0000000E+00 003002\n" +
                        "  0.0000000E+00 0.0000000E+00  597.8610      21.83616     0.0000000E+00 200300\n" +
                        "  0.0000000E+00 0.0000000E+00  1324.098      45.33987     0.0000000E+00 110300\n" +
                        "  0.0000000E+00 0.0000000E+00  914.7951      47.73352     0.0000000E+00 020300\n" +
                        "  -1471.031      58.94065     0.0000000E+00 0.0000000E+00  1349.169     101300\n" +
                        "  -1467.103      38.50039     0.0000000E+00 0.0000000E+00  2123.890     011300\n" +
                        "  0.0000000E+00 0.0000000E+00  7744.251      272.7179     0.0000000E+00 002300\n" +
                        "  -151.1719      16.20691     0.0000000E+00 0.0000000E+00  173.8834     200201\n" +
                        "  -437.2502      44.70507     0.0000000E+00 0.0000000E+00  435.0191     110201\n" +
                        "  -324.5613      29.46004     0.0000000E+00 0.0000000E+00  305.2131     020201\n" +
                        "  0.0000000E+00 0.0000000E+00  1175.900      52.90868     0.0000000E+00 101201\n" +
                        "  0.0000000E+00 0.0000000E+00  2026.855      123.1821     0.0000000E+00 011201\n" +
                        "  -91.49878     -10.30865     0.0000000E+00 0.0000000E+00  1697.274     002201\n" +
                        "  0.0000000E+00 0.0000000E+00  35.09554      1.588129     0.0000000E+00 200102\n" +
                        "  0.0000000E+00 0.0000000E+00  114.5592      4.611917     0.0000000E+00 110102\n" +
                        "  0.0000000E+00 0.0000000E+00  99.11788      8.871349     0.0000000E+00 020102\n" +
                        "  -115.2246      10.23284     0.0000000E+00 0.0000000E+00  91.06571     101102\n" +
                        "  -190.2077      13.34493     0.0000000E+00 0.0000000E+00  166.3042     011102\n" +
                        "  0.0000000E+00 0.0000000E+00  406.3546      22.06294     0.0000000E+00 002102\n" +
                        "  -1.180903     0.3996449     0.0000000E+00 0.0000000E+00  1.677746     200003\n" +
                        "  -6.805371      1.370413     0.0000000E+00 0.0000000E+00  7.754020     110003\n" +
                        "  -7.816964     0.7089012     0.0000000E+00 0.0000000E+00  8.098247     020003\n" +
                        "  0.0000000E+00 0.0000000E+00  4.750965     0.1993596     0.0000000E+00 101003\n" +
                        "  0.0000000E+00 0.0000000E+00  12.76731      1.849962     0.0000000E+00 011003\n" +
                        "  -2.873808     0.1999259     0.0000000E+00 0.0000000E+00  9.899988     002003\n" +
                        "  -629.0282      27.95658     0.0000000E+00 0.0000000E+00  570.8752     100400\n" +
                        "  -638.4972      18.64262     0.0000000E+00 0.0000000E+00  894.8288     010400\n" +
                        "  0.0000000E+00 0.0000000E+00  6526.859      240.9836     0.0000000E+00 001400\n" +
                        "  0.0000000E+00 0.0000000E+00  739.9614      38.70884     0.0000000E+00 100301\n" +
                        "  0.0000000E+00 0.0000000E+00  1261.166      89.72014     0.0000000E+00 010301\n" +
                        "  -115.8523     -9.944071     0.0000000E+00 0.0000000E+00  2107.430     001301\n" +
                        "  -128.9847      12.56247     0.0000000E+00 0.0000000E+00  96.46099     100202\n" +
                        "  -203.2783      13.09295     0.0000000E+00 0.0000000E+00  172.0783     010202\n" +
                        "  0.0000000E+00 0.0000000E+00  895.9373      53.67486     0.0000000E+00 001202\n" +
                        "  0.0000000E+00 0.0000000E+00  14.58257      1.757608     0.0000000E+00 100103\n" +
                        "  0.0000000E+00 0.0000000E+00  34.50322      7.288998     0.0000000E+00 010103\n" +
                        "  -12.82123      1.445361     0.0000000E+00 0.0000000E+00  52.03629     001103\n" +
                        " -0.3938031     0.1828546     0.0000000E+00 0.0000000E+00 0.4766518     100004\n" +
                        "  -2.155865    -0.8062327E-01 0.0000000E+00 0.0000000E+00  1.214336     010004\n" +
                        "  0.0000000E+00 0.0000000E+00  2.015150     0.1818176     0.0000000E+00 001004\n" +
                        "  0.0000000E+00 0.0000000E+00  2237.143      89.49347     0.0000000E+00 000500\n" +
                        "  -58.12795     -2.310130     0.0000000E+00 0.0000000E+00  986.1171     000401\n" +
                        "  0.0000000E+00 0.0000000E+00  647.4618      45.51240     0.0000000E+00 000302\n" +
                        "  -14.36106      1.990963     0.0000000E+00 0.0000000E+00  65.15964     000203\n" +
                        "  0.0000000E+00 0.0000000E+00  8.867736      1.800915     0.0000000E+00 000104\n" +
                        " -0.8099460E-01 0.1656817E-01 0.0000000E+00 0.0000000E+00 0.3406350     000005"
        );
    }

    private CosyArbitraryOrder.CosyMapArbitraryOrder cosyMap左偏n2正数弯铁(){
        return CosyArbitraryOrder.readMap(
                " -0.6808175     -1.087554     0.0000000E+00 0.0000000E+00 0.5104717     100000\n" +
                        "  0.4932974    -0.6808175     0.0000000E+00 0.0000000E+00 0.7889352     010000\n" +
                        "  0.0000000E+00 0.0000000E+00  1.000000     0.0000000E+00 0.0000000E+00 001000\n" +
                        "  0.0000000E+00 0.0000000E+00  2.967611      1.000000     0.0000000E+00 000100\n" +
                        "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00  1.000000     000010\n" +
                        " -0.7889352    -0.5104717     0.0000000E+00 0.0000000E+00 0.5151979     000001\n" +
                        "  -134.4270     -78.26390     0.0000000E+00 0.0000000E+00 -14.26770     200000\n" +
                        "  -390.7434     -241.9141     0.0000000E+00 0.0000000E+00 -34.84386     110000\n" +
                        "  -301.9474     -199.3250     0.0000000E+00 0.0000000E+00 -22.47066     020000\n" +
                        "  0.0000000E+00 0.0000000E+00  400.1946      258.9415     0.0000000E+00 101000\n" +
                        "  0.0000000E+00 0.0000000E+00  589.1222      400.1946     0.0000000E+00 011000\n" +
                        "   200.0973      129.4707     0.0000000E+00 0.0000000E+00  15.85686     002000\n" +
                        "  0.0000000E+00 0.0000000E+00  549.9117      368.2428     0.0000000E+00 100100\n" +
                        "  0.0000000E+00 0.0000000E+00  849.8899      598.4996     0.0000000E+00 010100\n" +
                        "   589.1222      400.1946     0.0000000E+00 0.0000000E+00  39.55826     001100\n" +
                        "   25.15380      18.46719     0.0000000E+00 0.0000000E+00 0.3234088     100001\n" +
                        "   47.78522      37.79875     0.0000000E+00 0.0000000E+00 0.4998291     010001\n" +
                        "  0.0000000E+00 0.0000000E+00 -39.55826     -31.71372     0.0000000E+00 001001\n" +
                        "   449.1977      318.8551     0.0000000E+00 0.0000000E+00  24.46564     000200\n" +
                        "  0.0000000E+00 0.0000000E+00 -68.46223     -54.55570     0.0000000E+00 000101\n" +
                        "  -2.524056     -2.524591     0.0000000E+00 0.0000000E+00-0.5663925     000002\n" +
                        "   5615.683      4215.763     0.0000000E+00 0.0000000E+00  149.0020     300000\n" +
                        "   24455.77      18750.55     0.0000000E+00 0.0000000E+00  608.8584     210000\n" +
                        "   35168.99      27429.98     0.0000000E+00 0.0000000E+00  833.7807     120000\n" +
                        "   16891.64      13374.15     0.0000000E+00 0.0000000E+00  393.3411     030000\n" +
                        "  0.0000000E+00 0.0000000E+00  3901.960      2983.609     0.0000000E+00 201000\n" +
                        "  0.0000000E+00 0.0000000E+00  18124.80      14631.85     0.0000000E+00 111000\n" +
                        "  0.0000000E+00 0.0000000E+00  16300.51      13461.91     0.0000000E+00 021000\n" +
                        "   12249.44      10062.00     0.0000000E+00 0.0000000E+00  357.7250     102000\n" +
                        "   10897.63      9042.305     0.0000000E+00 0.0000000E+00  254.4385     012000\n" +
                        "  0.0000000E+00 0.0000000E+00  10129.27      8108.321     0.0000000E+00 003000\n" +
                        "  0.0000000E+00 0.0000000E+00  298.1575     -21.60616     0.0000000E+00 200100\n" +
                        "  0.0000000E+00 0.0000000E+00  11294.21      8983.937     0.0000000E+00 110100\n" +
                        "  0.0000000E+00 0.0000000E+00  13468.14      11181.15     0.0000000E+00 020100\n" +
                        "   38807.52      32159.46     0.0000000E+00 0.0000000E+00  836.2198     101100\n" +
                        "   38079.85      31881.15     0.0000000E+00 0.0000000E+00  490.3190     011100\n" +
                        "  0.0000000E+00 0.0000000E+00  42181.58      34191.61     0.0000000E+00 002100\n" +
                        "  -1609.882     -1400.649     0.0000000E+00 0.0000000E+00 -36.30903     200001\n" +
                        "  -4203.238     -3744.115     0.0000000E+00 0.0000000E+00 -90.74462     110001\n" +
                        "  -2818.249     -2575.146     0.0000000E+00 0.0000000E+00 -69.59293     020001\n" +
                        "  0.0000000E+00 0.0000000E+00 -3124.602     -2514.727     0.0000000E+00 101001\n" +
                        "  0.0000000E+00 0.0000000E+00 -4468.936     -3551.633     0.0000000E+00 011001\n" +
                        "   899.1405      840.8708     0.0000000E+00 0.0000000E+00  54.52656     002001\n" +
                        "   28996.21      24211.62     0.0000000E+00 0.0000000E+00  457.8407     100200\n" +
                        "   30260.21      25529.79     0.0000000E+00 0.0000000E+00  142.4821     010200\n" +
                        "  0.0000000E+00 0.0000000E+00  58550.20      47997.62     0.0000000E+00 001200\n" +
                        "  0.0000000E+00 0.0000000E+00 -3941.434     -3105.639     0.0000000E+00 100101\n" +
                        "  0.0000000E+00 0.0000000E+00 -5903.895     -4606.734     0.0000000E+00 010101\n" +
                        "   1522.190      1579.550     0.0000000E+00 0.0000000E+00  157.0628     001101\n" +
                        "   91.07728      92.80301     0.0000000E+00 0.0000000E+00  5.604981     100002\n" +
                        "   117.6457      129.9657     0.0000000E+00 0.0000000E+00  12.32871     010002\n" +
                        "  0.0000000E+00 0.0000000E+00  132.7148      97.81162     0.0000000E+00 001002\n" +
                        "  0.0000000E+00 0.0000000E+00  27253.80      22577.29     0.0000000E+00 000300\n" +
                        "   393.8440      581.2443     0.0000000E+00 0.0000000E+00  128.4048     000201\n" +
                        "  0.0000000E+00 0.0000000E+00  233.7772      170.6151     0.0000000E+00 000102\n" +
                        " -0.7469318     -1.904807     0.0000000E+00 0.0000000E+00-0.2772324     000003\n" +
                        "  -226674.9     -186420.4     0.0000000E+00 0.0000000E+00 -4056.615     400000\n" +
                        "  -1217989.     -1008103.     0.0000000E+00 0.0000000E+00 -23237.66     310000\n" +
                        "  -2470508.     -2057460.     0.0000000E+00 0.0000000E+00 -52183.49     220000\n" +
                        "  -2240677.     -1877098.     0.0000000E+00 0.0000000E+00 -53843.76     130000\n" +
                        "  -767302.2     -646415.2     0.0000000E+00 0.0000000E+00 -21440.93     040000\n" +
                        "  0.0000000E+00 0.0000000E+00 -516059.0     -443793.8     0.0000000E+00 301000\n" +
                        "  0.0000000E+00 0.0000000E+00 -1888766.     -1629242.     0.0000000E+00 211000\n" +
                        "  0.0000000E+00 0.0000000E+00 -2310396.     -1998143.     0.0000000E+00 121000\n" +
                        "  0.0000000E+00 0.0000000E+00 -948242.5     -822168.1     0.0000000E+00 031000\n" +
                        "   1110205.      945855.4     0.0000000E+00 0.0000000E+00  5400.689     202000\n" +
                        "   2836634.      2428234.     0.0000000E+00 0.0000000E+00 -3961.618     112000\n" +
                        "   1858832.      1598685.     0.0000000E+00 0.0000000E+00 -11327.62     022000\n" +
                        "  0.0000000E+00 0.0000000E+00  1036015.      895551.3     0.0000000E+00 103000\n" +
                        "  0.0000000E+00 0.0000000E+00  1317013.      1145858.     0.0000000E+00 013000\n" +
                        "  -52835.68     -41940.58     0.0000000E+00 0.0000000E+00 -7738.587     004000\n" +
                        "  0.0000000E+00 0.0000000E+00 -591053.5     -511243.0     0.0000000E+00 300100\n" +
                        "  0.0000000E+00 0.0000000E+00 -2202747.     -1912609.     0.0000000E+00 210100\n" +
                        "  0.0000000E+00 0.0000000E+00 -2744645.     -2391157.     0.0000000E+00 120100\n" +
                        "  0.0000000E+00 0.0000000E+00 -1148092.     -1003525.     0.0000000E+00 030100\n" +
                        "   2715482.      2323786.     0.0000000E+00 0.0000000E+00  9913.181     201100\n" +
                        "   7006479.      6026479.     0.0000000E+00 0.0000000E+00 -32865.13     111100\n" +
                        "   4631632.      4003127.     0.0000000E+00 0.0000000E+00 -52694.33     021100\n" +
                        "  0.0000000E+00 0.0000000E+00  4051494.      3520657.     0.0000000E+00 102100\n" +
                        "  0.0000000E+00 0.0000000E+00  5155114.      4508072.     0.0000000E+00 012100\n" +
                        "  -200942.7     -154253.7     0.0000000E+00 0.0000000E+00 -50067.94     003100\n" +
                        "   52056.17      47746.19     0.0000000E+00 0.0000000E+00  2099.141     300001\n" +
                        "   202330.5      188802.8     0.0000000E+00 0.0000000E+00  10287.23     210001\n" +
                        "   260350.4      247191.2     0.0000000E+00 0.0000000E+00  16623.23     120001\n" +
                        "   111544.8      107816.6     0.0000000E+00 0.0000000E+00  9052.096     030001\n" +
                        "  0.0000000E+00 0.0000000E+00  32324.96      30664.96     0.0000000E+00 201001\n" +
                        "  0.0000000E+00 0.0000000E+00  58504.50      61690.22     0.0000000E+00 111001\n" +
                        "  0.0000000E+00 0.0000000E+00  20435.00      27362.66     0.0000000E+00 021001\n" +
                        "  -91805.12     -74919.40     0.0000000E+00 0.0000000E+00  6654.785     102001\n" +
                        "  -145684.7     -122942.2     0.0000000E+00 0.0000000E+00  6929.389     012001\n" +
                        "  0.0000000E+00 0.0000000E+00 -57721.29     -46806.39     0.0000000E+00 003001\n" +
                        "   1650796.      1417853.     0.0000000E+00 0.0000000E+00  6900.404     200200\n" +
                        "   4309693.      3722132.     0.0000000E+00 0.0000000E+00 -27908.60     110200\n" +
                        "   2880969.      2500898.     0.0000000E+00 0.0000000E+00 -44480.53     020200\n" +
                        "  0.0000000E+00 0.0000000E+00  5341598.      4665464.     0.0000000E+00 101200\n" +
                        "  0.0000000E+00 0.0000000E+00  6809691.      5984320.     0.0000000E+00 011200\n" +
                        "  -271851.1     -196214.4     0.0000000E+00 0.0000000E+00 -121478.6     002200\n" +
                        "  0.0000000E+00 0.0000000E+00  52070.09      46970.08     0.0000000E+00 200101\n" +
                        "  0.0000000E+00 0.0000000E+00  105368.1      101346.6     0.0000000E+00 110101\n" +
                        "  0.0000000E+00 0.0000000E+00  47539.26      50983.45     0.0000000E+00 020101\n" +
                        "  -266692.8     -215875.6     0.0000000E+00 0.0000000E+00  22405.56     101101\n" +
                        "  -412814.5     -345019.8     0.0000000E+00 0.0000000E+00  26296.38     011101\n" +
                        "  0.0000000E+00 0.0000000E+00 -251049.8     -203112.7     0.0000000E+00 002101\n" +
                        "  -3725.569     -4131.534     0.0000000E+00 0.0000000E+00 -628.8500     200002\n" +
                        "  -8225.971     -9538.554     0.0000000E+00 0.0000000E+00 -1885.959     110002\n" +
                        "  -4496.762     -5497.726     0.0000000E+00 0.0000000E+00 -1488.743     020002\n" +
                        "  0.0000000E+00 0.0000000E+00  5042.000      2855.980     0.0000000E+00 101002\n" +
                        "  0.0000000E+00 0.0000000E+00  8592.440      5209.051     0.0000000E+00 011002\n" +
                        "   3658.088      3694.836     0.0000000E+00 0.0000000E+00  112.1376     002002\n" +
                        "  0.0000000E+00 0.0000000E+00  2365476.      2075983.     0.0000000E+00 100300\n" +
                        "  0.0000000E+00 0.0000000E+00  3026402.      2671854.     0.0000000E+00 010300\n" +
                        "  -149506.7     -94426.09     0.0000000E+00 0.0000000E+00 -130738.5     001300\n" +
                        "  -196288.3     -158525.1     0.0000000E+00 0.0000000E+00  17585.81     100201\n" +
                        "  -298244.1     -247984.3     0.0000000E+00 0.0000000E+00  22479.14     010201\n" +
                        "  0.0000000E+00 0.0000000E+00 -368063.7     -298024.9     0.0000000E+00 001201\n" +
                        "  0.0000000E+00 0.0000000E+00  7417.238      4448.012     0.0000000E+00 100102\n" +
                        "  0.0000000E+00 0.0000000E+00  12714.44      7955.420     0.0000000E+00 010102\n" +
                        "   9320.018      9027.020     0.0000000E+00 0.0000000E+00 -186.8242     001102\n" +
                        "   73.89092      108.6279     0.0000000E+00 0.0000000E+00  50.06062     100003\n" +
                        "   63.13065      101.7995     0.0000000E+00 0.0000000E+00  81.85007     010003\n" +
                        "  0.0000000E+00 0.0000000E+00 -218.8225     -120.7657     0.0000000E+00 001003\n" +
                        "  -25406.37     -10162.10     0.0000000E+00 0.0000000E+00 -52752.41     000400\n" +
                        "  0.0000000E+00 0.0000000E+00 -181876.9     -147670.7     0.0000000E+00 000301\n" +
                        "   6665.412      6067.832     0.0000000E+00 0.0000000E+00 -515.3040     000202\n" +
                        "  0.0000000E+00 0.0000000E+00 -439.8003     -260.3198     0.0000000E+00 000103\n" +
                        "  -1.149242    -0.2576825     0.0000000E+00 0.0000000E+00 -1.870329     000004\n" +
                        "   6922531.      5916640.     0.0000000E+00 0.0000000E+00  200516.2     500000\n" +
                        "  0.4609740E+08 0.3954662E+08 0.0000000E+00 0.0000000E+00  1577693.     410000\n" +
                        "  0.1230504E+09 0.1059388E+09 0.0000000E+00 0.0000000E+00  4928427.     320000\n" +
                        "  0.1647197E+09 0.1422885E+09 0.0000000E+00 0.0000000E+00  7668991.     230000\n" +
                        "  0.1106560E+09 0.9588631E+08 0.0000000E+00 0.0000000E+00  5956951.     140000\n" +
                        "  0.2986919E+08 0.2595678E+08 0.0000000E+00 0.0000000E+00  1851611.     050000\n" +
                        "  0.0000000E+00 0.0000000E+00  5156869.      4450612.     0.0000000E+00 401000\n" +
                        "  0.0000000E+00 0.0000000E+00 0.2382215E+08 0.2045486E+08 0.0000000E+00 311000\n" +
                        "  0.0000000E+00 0.0000000E+00 0.4142133E+08 0.3537641E+08 0.0000000E+00 221000\n" +
                        "  0.0000000E+00 0.0000000E+00 0.3210729E+08 0.2726589E+08 0.0000000E+00 131000\n" +
                        "  0.0000000E+00 0.0000000E+00  9373197.      7913261.     0.0000000E+00 041000\n" +
                        "  -1614510.     -1161326.     0.0000000E+00 0.0000000E+00  200515.3     302000\n" +
                        "  -6918045.     -4875473.     0.0000000E+00 0.0000000E+00 -1151253.     212000\n" +
                        " -0.1009967E+08 -7096047.     0.0000000E+00 0.0000000E+00 -4128992.     122000\n" +
                        "  -5421242.     -3919629.     0.0000000E+00 0.0000000E+00 -3038230.     032000\n" +
                        "  0.0000000E+00 0.0000000E+00 0.2525858E+08 0.2280687E+08 0.0000000E+00 203000\n" +
                        "  0.0000000E+00 0.0000000E+00 0.6227488E+08 0.5656158E+08 0.0000000E+00 113000\n" +
                        "  0.0000000E+00 0.0000000E+00 0.3749576E+08 0.3422710E+08 0.0000000E+00 023000\n" +
                        "  0.1420426E+08 0.1280036E+08 0.0000000E+00 0.0000000E+00 -2217879.     104000\n" +
                        "  0.2167967E+08 0.1947555E+08 0.0000000E+00 0.0000000E+00 -2806831.     014000\n" +
                        "  0.0000000E+00 0.0000000E+00  6596591.      5990015.     0.0000000E+00 005000\n" +
                        "  0.0000000E+00 0.0000000E+00  7479149.      6560761.     0.0000000E+00 400100\n" +
                        "  0.0000000E+00 0.0000000E+00 0.3568470E+08 0.3122884E+08 0.0000000E+00 310100\n" +
                        "  0.0000000E+00 0.0000000E+00 0.6424605E+08 0.5609924E+08 0.0000000E+00 220100\n" +
                        "  0.0000000E+00 0.0000000E+00 0.5170562E+08 0.4505507E+08 0.0000000E+00 130100\n" +
                        "  0.0000000E+00 0.0000000E+00 0.1571308E+08 0.1366778E+08 0.0000000E+00 040100\n" +
                        "  -7667228.     -6298709.     0.0000000E+00 0.0000000E+00  1463136.     301100\n" +
                        " -0.2943685E+08-0.2357075E+08 0.0000000E+00 0.0000000E+00  754264.5     211100\n" +
                        " -0.3823266E+08-0.2995920E+08 0.0000000E+00 0.0000000E+00 -5894408.     121100\n" +
                        " -0.1789940E+08-0.1391642E+08 0.0000000E+00 0.0000000E+00 -5856051.     031100\n" +
                        "  0.0000000E+00 0.0000000E+00 0.1026728E+09 0.9280626E+08 0.0000000E+00 202100\n" +
                        "  0.0000000E+00 0.0000000E+00 0.2552094E+09 0.2319199E+09 0.0000000E+00 112100\n" +
                        "  0.0000000E+00 0.0000000E+00 0.1551774E+09 0.1416282E+09 0.0000000E+00 022100\n" +
                        "  0.7057675E+08 0.6393861E+08 0.0000000E+00 0.0000000E+00-0.1357830E+08 103100\n" +
                        "  0.1081242E+09 0.9754650E+08 0.0000000E+00 0.0000000E+00-0.1745032E+08 013100\n" +
                        "  0.0000000E+00 0.0000000E+00 0.4215124E+08 0.3839903E+08 0.0000000E+00 004100\n" +
                        "  -1723102.     -1634913.     0.0000000E+00 0.0000000E+00 -166877.1     400001\n" +
                        "  -8594009.     -8246506.     0.0000000E+00 0.0000000E+00 -984924.7     310001\n" +
                        " -0.1608627E+08-0.1561549E+08 0.0000000E+00 0.0000000E+00 -2194261.     220001\n" +
                        " -0.1338396E+08-0.1314748E+08 0.0000000E+00 0.0000000E+00 -2180829.     130001\n" +
                        "  -4178058.     -4153784.     0.0000000E+00 0.0000000E+00 -816388.9     040001\n" +
                        "  0.0000000E+00 0.0000000E+00  750474.4      428474.6     0.0000000E+00 301001\n" +
                        "  0.0000000E+00 0.0000000E+00  3764334.      2443940.     0.0000000E+00 211001\n" +
                        "  0.0000000E+00 0.0000000E+00  5876706.      4089404.     0.0000000E+00 121001\n" +
                        "  0.0000000E+00 0.0000000E+00  2932280.      2127621.     0.0000000E+00 031001\n" +
                        "  -772289.9     -193479.4     0.0000000E+00 0.0000000E+00  782381.4     202001\n" +
                        "  -3512311.     -1808015.     0.0000000E+00 0.0000000E+00  2315526.     112001\n" +
                        "  -2973412.     -1757787.     0.0000000E+00 0.0000000E+00  1762131.     022001\n" +
                        "  0.0000000E+00 0.0000000E+00 -3471804.     -2647731.     0.0000000E+00 103001\n" +
                        "  0.0000000E+00 0.0000000E+00 -4680879.     -3570134.     0.0000000E+00 013001\n" +
                        "  -1425969.     -1296281.     0.0000000E+00 0.0000000E+00  80342.02     004001\n" +
                        "  -7426030.     -6384274.     0.0000000E+00 0.0000000E+00  1509213.     300200\n" +
                        " -0.2785890E+08-0.2351833E+08 0.0000000E+00 0.0000000E+00  2805540.     210200\n" +
                        " -0.3498877E+08-0.2902107E+08 0.0000000E+00 0.0000000E+00 -736966.0     120200\n" +
                        " -0.1541856E+08-0.1262101E+08 0.0000000E+00 0.0000000E+00 -2465016.     030200\n" +
                        "  0.0000000E+00 0.0000000E+00 0.1366146E+09 0.1236050E+09 0.0000000E+00 201200\n" +
                        "  0.0000000E+00 0.0000000E+00 0.3429277E+09 0.3117677E+09 0.0000000E+00 111200\n" +
                        "  0.0000000E+00 0.0000000E+00 0.2108219E+09 0.1923558E+09 0.0000000E+00 021200\n" +
                        "  0.1315914E+09 0.1198305E+09 0.0000000E+00 0.0000000E+00-0.3084628E+08 102200\n" +
                        "  0.2026464E+09 0.1835525E+09 0.0000000E+00 0.0000000E+00-0.4013342E+08 012200\n" +
                        "  0.0000000E+00 0.0000000E+00 0.1083509E+09 0.9899451E+08 0.0000000E+00 003200\n" +
                        "  0.0000000E+00 0.0000000E+00  841548.9      457447.8     0.0000000E+00 300101\n" +
                        "  0.0000000E+00 0.0000000E+00  4249474.      2666104.     0.0000000E+00 210101\n" +
                        "  0.0000000E+00 0.0000000E+00  6708473.      4548964.     0.0000000E+00 120101\n" +
                        "  0.0000000E+00 0.0000000E+00  3394401.      2412390.     0.0000000E+00 030101\n" +
                        "  -3399441.     -1785988.     0.0000000E+00 0.0000000E+00  1861148.     201101\n" +
                        " -0.1290478E+08 -8175536.     0.0000000E+00 0.0000000E+00  5718809.     111101\n" +
                        " -0.1040816E+08 -7033150.     0.0000000E+00 0.0000000E+00  4482222.     021101\n" +
                        "  0.0000000E+00 0.0000000E+00-0.1632138E+08-0.1275380E+08 0.0000000E+00 102101\n" +
                        "  0.0000000E+00 0.0000000E+00-0.2187834E+08-0.1710046E+08 0.0000000E+00 012101\n" +
                        "  -7214899.     -6532742.     0.0000000E+00 0.0000000E+00  624547.4     003101\n" +
                        "   136688.9      148126.9     0.0000000E+00 0.0000000E+00  38484.19     300002\n" +
                        "   466651.4      515824.4     0.0000000E+00 0.0000000E+00  167934.6     210002\n" +
                        "   523935.1      589472.2     0.0000000E+00 0.0000000E+00  242664.8     120002\n" +
                        "   194765.5      221392.3     0.0000000E+00 0.0000000E+00  117072.4     030002\n" +
                        "  0.0000000E+00 0.0000000E+00 -77117.35     -51644.18     0.0000000E+00 201002\n" +
                        "  0.0000000E+00 0.0000000E+00 -212027.8     -157500.7     0.0000000E+00 111002\n" +
                        "  0.0000000E+00 0.0000000E+00 -134555.9     -107645.6     0.0000000E+00 021002\n" +
                        "   148221.1      82602.27     0.0000000E+00 0.0000000E+00 -98234.96     102002\n" +
                        "   226110.6      127505.8     0.0000000E+00 0.0000000E+00 -158138.9     012002\n" +
                        "  0.0000000E+00 0.0000000E+00  107625.4      64334.48     0.0000000E+00 003002\n" +
                        "  0.0000000E+00 0.0000000E+00 0.5952364E+08 0.5389883E+08 0.0000000E+00 200300\n" +
                        "  0.0000000E+00 0.0000000E+00 0.1511062E+09 0.1374098E+09 0.0000000E+00 110300\n" +
                        "  0.0000000E+00 0.0000000E+00 0.9402460E+08 0.8573778E+08 0.0000000E+00 020300\n" +
                        "  0.1092337E+09 0.9996817E+08 0.0000000E+00 0.0000000E+00-0.3095054E+08 101300\n" +
                        "  0.1693038E+09 0.1539103E+09 0.0000000E+00 0.0000000E+00-0.4069835E+08 011300\n" +
                        "  0.0000000E+00 0.0000000E+00 0.1400127E+09 0.1282473E+09 0.0000000E+00 002300\n" +
                        "  -2800121.     -1727327.     0.0000000E+00 0.0000000E+00  1072845.     200201\n" +
                        " -0.1004214E+08 -6890722.     0.0000000E+00 0.0000000E+00  3461655.     110201\n" +
                        "  -7997769.     -5737506.     0.0000000E+00 0.0000000E+00  2819594.     020201\n" +
                        "  0.0000000E+00 0.0000000E+00-0.2506604E+08-0.1992218E+08 0.0000000E+00 101201\n" +
                        "  0.0000000E+00 0.0000000E+00-0.3355232E+08-0.2667916E+08 0.0000000E+00 011201\n" +
                        " -0.1388756E+08-0.1251720E+08 0.0000000E+00 0.0000000E+00  1675448.     002201\n" +
                        "  0.0000000E+00 0.0000000E+00 -120691.7     -79350.23     0.0000000E+00 200102\n" +
                        "  0.0000000E+00 0.0000000E+00 -334718.5     -240317.0     0.0000000E+00 110102\n" +
                        "  0.0000000E+00 0.0000000E+00 -219483.0     -167421.0     0.0000000E+00 020102\n" +
                        "   590226.7      380495.6     0.0000000E+00 0.0000000E+00 -284067.4     101102\n" +
                        "   910087.3      598435.1     0.0000000E+00 0.0000000E+00 -454826.2     011102\n" +
                        "  0.0000000E+00 0.0000000E+00  580389.8      379024.8     0.0000000E+00 002102\n" +
                        "  -4858.982     -5352.181     0.0000000E+00 0.0000000E+00 -3648.924     200003\n" +
                        "  -10756.22     -10809.08     0.0000000E+00 0.0000000E+00 -9810.740     110003\n" +
                        "  -6591.130     -5473.987     0.0000000E+00 0.0000000E+00 -6667.522     020003\n" +
                        "  0.0000000E+00 0.0000000E+00 -5982.687     -2679.130     0.0000000E+00 101003\n" +
                        "  0.0000000E+00 0.0000000E+00 -11705.74     -6084.336     0.0000000E+00 011003\n" +
                        "  -3470.789     -1930.621     0.0000000E+00 0.0000000E+00  4331.114     002003\n" +
                        "  0.3410477E+08 0.3136037E+08 0.0000000E+00 0.0000000E+00-0.1159441E+08 100400\n" +
                        "  0.5326385E+08 0.4857408E+08 0.0000000E+00 0.0000000E+00-0.1539975E+08 010400\n" +
                        "  0.0000000E+00 0.0000000E+00 0.9094373E+08 0.8347437E+08 0.0000000E+00 001400\n" +
                        "  0.0000000E+00 0.0000000E+00-0.1262707E+08-0.1016056E+08 0.0000000E+00 100301\n" +
                        "  0.0000000E+00 0.0000000E+00-0.1693598E+08-0.1363342E+08 0.0000000E+00 010301\n" +
                        " -0.1203035E+08-0.1078677E+08 0.0000000E+00 0.0000000E+00  1915780.     001301\n" +
                        "   520327.2      356645.7     0.0000000E+00 0.0000000E+00 -205238.8     100202\n" +
                        "   809617.0      567721.1     0.0000000E+00 0.0000000E+00 -330123.4     010202\n" +
                        "  0.0000000E+00 0.0000000E+00  1006362.      691742.6     0.0000000E+00 001202\n" +
                        "  0.0000000E+00 0.0000000E+00 -10356.70     -5366.937     0.0000000E+00 100103\n" +
                        "  0.0000000E+00 0.0000000E+00 -19509.85     -10954.81     0.0000000E+00 010103\n" +
                        "  -16546.80     -11696.53     0.0000000E+00 0.0000000E+00  12585.55     001103\n" +
                        "   155.9084      80.99020     0.0000000E+00 0.0000000E+00  121.5076     100004\n" +
                        "   270.8451      94.72093     0.0000000E+00 0.0000000E+00  150.5655     010004\n" +
                        "  0.0000000E+00 0.0000000E+00  263.2618      132.9983     0.0000000E+00 001004\n" +
                        "  0.0000000E+00 0.0000000E+00 0.2375858E+08 0.2183998E+08 0.0000000E+00 000500\n" +
                        "  -3953226.     -3522912.     0.0000000E+00 0.0000000E+00  804090.1     000401\n" +
                        "  0.0000000E+00 0.0000000E+00  570259.7      405025.7     0.0000000E+00 000302\n" +
                        "  -16591.28     -12417.19     0.0000000E+00 0.0000000E+00  9710.151     000203\n" +
                        "  0.0000000E+00 0.0000000E+00  612.9357      334.1739     0.0000000E+00 000104\n" +
                        "  -6.208493     -1.821905     0.0000000E+00 0.0000000E+00-0.4478945     000005"
        );
    }

    private CosyArbitraryOrder.CosyMapArbitraryOrder cosyMap左偏n2负数弯铁(){
        return CosyArbitraryOrder.readMap(
                " -0.6808175     -1.087554     0.0000000E+00 0.0000000E+00 0.5104717     100000\n" +
                        "  0.4932974    -0.6808175     0.0000000E+00 0.0000000E+00 0.7889352     010000\n" +
                        "  0.0000000E+00 0.0000000E+00  1.000000     0.0000000E+00 0.0000000E+00 001000\n" +
                        "  0.0000000E+00 0.0000000E+00  2.967611      1.000000     0.0000000E+00 000100\n" +
                        "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00  1.000000     000010\n" +
                        " -0.7889352    -0.5104717     0.0000000E+00 0.0000000E+00 0.5151979     000001\n" +
                        "   162.4053      93.91668     0.0000000E+00 0.0000000E+00  16.39423     200000\n" +
                        "   470.2604      290.2969     0.0000000E+00 0.0000000E+00  40.90242     110000\n" +
                        "   363.3943      240.1948     0.0000000E+00 0.0000000E+00  25.59355     020000\n" +
                        "  0.0000000E+00 0.0000000E+00 -480.2336     -310.7297     0.0000000E+00 101000\n" +
                        "  0.0000000E+00 0.0000000E+00 -706.9466     -480.2336     0.0000000E+00 011000\n" +
                        "  -240.1168     -155.3649     0.0000000E+00 0.0000000E+00 -19.02823     002000\n" +
                        "  0.0000000E+00 0.0000000E+00 -661.9039     -441.8913     0.0000000E+00 100100\n" +
                        "  0.0000000E+00 0.0000000E+00 -1022.974     -718.1996     0.0000000E+00 010100\n" +
                        "  -706.9466     -480.2336     0.0000000E+00 0.0000000E+00 -47.46991     001100\n" +
                        "  -27.82166     -22.16063     0.0000000E+00 0.0000000E+00 -1.461822     100001\n" +
                        "  -55.02613     -44.02154     0.0000000E+00 0.0000000E+00 -2.259249     010001\n" +
                        "  0.0000000E+00 0.0000000E+00  47.46991      38.05646     0.0000000E+00 001001\n" +
                        "  -537.4841     -381.6212     0.0000000E+00 0.0000000E+00 -31.05976     000200\n" +
                        "  0.0000000E+00 0.0000000E+00  78.75269      65.46684     0.0000000E+00 000101\n" +
                        "   4.199520      3.225137     0.0000000E+00 0.0000000E+00-0.4739172     000002\n" +
                        "   7694.886      5900.813     0.0000000E+00 0.0000000E+00  335.0921     300000\n" +
                        "   33403.47      26203.78     0.0000000E+00 0.0000000E+00  1239.885     210000\n" +
                        "   47670.71      38096.58     0.0000000E+00 0.0000000E+00  1518.736     120000\n" +
                        "   22658.20      18410.14     0.0000000E+00 0.0000000E+00  606.4044     030000\n" +
                        "  0.0000000E+00 0.0000000E+00  6794.178      4801.195     0.0000000E+00 201000\n" +
                        "  0.0000000E+00 0.0000000E+00  30095.73      23016.21     0.0000000E+00 111000\n" +
                        "  0.0000000E+00 0.0000000E+00  26758.23      21119.67     0.0000000E+00 021000\n" +
                        "   18863.01      15203.99     0.0000000E+00 0.0000000E+00  353.9285     102000\n" +
                        "   17353.54      13932.49     0.0000000E+00 0.0000000E+00  398.8505     012000\n" +
                        "  0.0000000E+00 0.0000000E+00  14332.38      11504.95     0.0000000E+00 003000\n" +
                        "  0.0000000E+00 0.0000000E+00  2270.408      916.0799     0.0000000E+00 200100\n" +
                        "  0.0000000E+00 0.0000000E+00  22815.16      16647.22     0.0000000E+00 110100\n" +
                        "  0.0000000E+00 0.0000000E+00  24969.56      19454.40     0.0000000E+00 020100\n" +
                        "   59343.40      48430.60     0.0000000E+00 0.0000000E+00  1243.385     101100\n" +
                        "   59788.17      48823.84     0.0000000E+00 0.0000000E+00  1570.763     011100\n" +
                        "  0.0000000E+00 0.0000000E+00  59726.78      48474.60     0.0000000E+00 002100\n" +
                        "  -2312.843     -1909.796     0.0000000E+00 0.0000000E+00 -19.70377     200001\n" +
                        "  -6458.155     -5300.023     0.0000000E+00 0.0000000E+00 -52.58473     110001\n" +
                        "  -4733.892     -3871.992     0.0000000E+00 0.0000000E+00 -20.52759     020001\n" +
                        "  0.0000000E+00 0.0000000E+00 -4356.033     -3911.298     0.0000000E+00 101001\n" +
                        "  0.0000000E+00 0.0000000E+00 -5409.576     -4999.514     0.0000000E+00 011001\n" +
                        "   1469.829      1164.334     0.0000000E+00 0.0000000E+00 -29.34214     002001\n" +
                        "   44512.21      36667.88     0.0000000E+00 0.0000000E+00  1006.683     100200\n" +
                        "   47708.37      39406.04     0.0000000E+00 0.0000000E+00  1396.538     010200\n" +
                        "  0.0000000E+00 0.0000000E+00  82788.97      67871.98     0.0000000E+00 001200\n" +
                        "  0.0000000E+00 0.0000000E+00 -4860.957     -4495.780     0.0000000E+00 100101\n" +
                        "  0.0000000E+00 0.0000000E+00 -6045.290     -5803.056     0.0000000E+00 010101\n" +
                        "   3537.558      2679.726     0.0000000E+00 0.0000000E+00 -112.8447     001101\n" +
                        "   161.2185      129.7186     0.0000000E+00 0.0000000E+00 -2.677307     100002\n" +
                        "   309.2263      246.5993     0.0000000E+00 0.0000000E+00 -8.752866     010002\n" +
                        "  0.0000000E+00 0.0000000E+00  90.65413      104.9669     0.0000000E+00 001002\n" +
                        "  0.0000000E+00 0.0000000E+00  38378.86      31764.30     0.0000000E+00 000300\n" +
                        "   2202.632      1580.308     0.0000000E+00 0.0000000E+00 -112.7509     000201\n" +
                        "  0.0000000E+00 0.0000000E+00  74.96070      110.1901     0.0000000E+00 000102\n" +
                        "  -13.65427     -10.04373     0.0000000E+00 0.0000000E+00  1.427780     000003\n" +
                        "   344610.6      294256.4     0.0000000E+00 0.0000000E+00  5056.862     400000\n" +
                        "   1804802.      1553068.     0.0000000E+00 0.0000000E+00  20659.91     310000\n" +
                        "   3566324.      3092321.     0.0000000E+00 0.0000000E+00  26120.10     220000\n" +
                        "   3148033.      2749831.     0.0000000E+00 0.0000000E+00  7158.700     130000\n" +
                        "   1047646.      921684.3     0.0000000E+00 0.0000000E+00 -4125.247     040000\n" +
                        "  0.0000000E+00 0.0000000E+00  901224.4      766827.8     0.0000000E+00 301000\n" +
                        "  0.0000000E+00 0.0000000E+00  3192254.      2729080.     0.0000000E+00 211000\n" +
                        "  0.0000000E+00 0.0000000E+00  3742737.      3214768.     0.0000000E+00 121000\n" +
                        "  0.0000000E+00 0.0000000E+00  1457384.      1257996.     0.0000000E+00 031000\n" +
                        "  -1992046.     -1703846.     0.0000000E+00 0.0000000E+00 -41720.15     202000\n" +
                        "  -5067642.     -4354661.     0.0000000E+00 0.0000000E+00 -125958.8     112000\n" +
                        "  -3291244.     -2843738.     0.0000000E+00 0.0000000E+00 -90494.63     022000\n" +
                        "  0.0000000E+00 0.0000000E+00 -1915090.     -1626919.     0.0000000E+00 103000\n" +
                        "  0.0000000E+00 0.0000000E+00 -2472956.     -2108795.     0.0000000E+00 013000\n" +
                        "   76774.57      68434.81     0.0000000E+00 0.0000000E+00 -8730.601     004000\n" +
                        "  0.0000000E+00 0.0000000E+00  1096820.      933229.6     0.0000000E+00 300100\n" +
                        "  0.0000000E+00 0.0000000E+00  3977998.      3401689.     0.0000000E+00 210100\n" +
                        "  0.0000000E+00 0.0000000E+00  4780712.      4108197.     0.0000000E+00 120100\n" +
                        "  0.0000000E+00 0.0000000E+00  1911278.      1650741.     0.0000000E+00 030100\n" +
                        "  -4982619.     -4278014.     0.0000000E+00 0.0000000E+00 -103165.9     201100\n" +
                        " -0.1284034E+08-0.1107660E+08 0.0000000E+00 0.0000000E+00 -335668.4     111100\n" +
                        "  -8432673.     -7313686.     0.0000000E+00 0.0000000E+00 -255693.3     021100\n" +
                        "  0.0000000E+00 0.0000000E+00 -7524466.     -6425406.     0.0000000E+00 102100\n" +
                        "  0.0000000E+00 0.0000000E+00 -9754557.     -8359171.     0.0000000E+00 012100\n" +
                        "   252174.3      230220.5     0.0000000E+00 0.0000000E+00 -62761.89     003100\n" +
                        "  -78886.41     -66944.13     0.0000000E+00 0.0000000E+00  1363.791     300001\n" +
                        "  -340809.2     -288535.1     0.0000000E+00 0.0000000E+00  8211.492     210001\n" +
                        "  -490284.2     -414875.5     0.0000000E+00 0.0000000E+00  15215.26     120001\n" +
                        "  -236374.9     -200195.0     0.0000000E+00 0.0000000E+00  9269.403     030001\n" +
                        "  0.0000000E+00 0.0000000E+00 -32531.36     -25763.08     0.0000000E+00 201001\n" +
                        "  0.0000000E+00 0.0000000E+00 -78636.21     -53814.07     0.0000000E+00 111001\n" +
                        "  0.0000000E+00 0.0000000E+00 -60467.62     -38381.21     0.0000000E+00 021001\n" +
                        "   107122.0      105103.8     0.0000000E+00 0.0000000E+00  12217.01     102001\n" +
                        "   170946.0      163283.1     0.0000000E+00 0.0000000E+00  13540.85     012001\n" +
                        "  0.0000000E+00 0.0000000E+00  82952.64      80256.50     0.0000000E+00 003001\n" +
                        "  -3088230.     -2660939.     0.0000000E+00 0.0000000E+00 -59629.63     200200\n" +
                        "  -8076915.     -6993344.     0.0000000E+00 0.0000000E+00 -213448.0     110200\n" +
                        "  -5376323.     -4680300.     0.0000000E+00 0.0000000E+00 -173804.5     020200\n" +
                        "  0.0000000E+00 0.0000000E+00 -9945049.     -8535885.     0.0000000E+00 101200\n" +
                        "  0.0000000E+00 0.0000000E+00-0.1295180E+08-0.1115323E+08 0.0000000E+00 011200\n" +
                        "   237863.3      231581.7     0.0000000E+00 0.0000000E+00 -161674.5     002200\n" +
                        "  0.0000000E+00 0.0000000E+00 -60615.18     -52262.99     0.0000000E+00 200101\n" +
                        "  0.0000000E+00 0.0000000E+00 -153128.2     -121569.3     0.0000000E+00 110101\n" +
                        "  0.0000000E+00 0.0000000E+00 -114015.9     -85306.81     0.0000000E+00 020101\n" +
                        "   253894.6      259505.6     0.0000000E+00 0.0000000E+00  38905.34     101101\n" +
                        "   411368.8      405758.3     0.0000000E+00 0.0000000E+00  45913.69     011101\n" +
                        "  0.0000000E+00 0.0000000E+00  296374.8      293955.2     0.0000000E+00 002101\n" +
                        "   10577.79      8450.204     0.0000000E+00 0.0000000E+00 -837.9208     200002\n" +
                        "   31664.20      25414.31     0.0000000E+00 0.0000000E+00 -2562.486     110002\n" +
                        "   24481.70      19697.48     0.0000000E+00 0.0000000E+00 -2145.496     020002\n" +
                        "  0.0000000E+00 0.0000000E+00  2755.154      191.0669     0.0000000E+00 101002\n" +
                        "  0.0000000E+00 0.0000000E+00  7263.727      3632.543     0.0000000E+00 011002\n" +
                        "  -9539.641     -7880.013     0.0000000E+00 0.0000000E+00  249.0560     002002\n" +
                        "  0.0000000E+00 0.0000000E+00 -4412292.     -3805551.     0.0000000E+00 100300\n" +
                        "  0.0000000E+00 0.0000000E+00 -5780089.     -5000662.     0.0000000E+00 010300\n" +
                        "   11689.89      34143.08     0.0000000E+00 0.0000000E+00 -179995.5     001300\n" +
                        "   143793.2      154926.2     0.0000000E+00 0.0000000E+00  29374.70     100201\n" +
                        "   240242.0      246514.4     0.0000000E+00 0.0000000E+00  36367.17     010201\n" +
                        "  0.0000000E+00 0.0000000E+00  349659.0      356836.0     0.0000000E+00 001201\n" +
                        "  0.0000000E+00 0.0000000E+00  6948.118      3591.595     0.0000000E+00 100102\n" +
                        "  0.0000000E+00 0.0000000E+00  13650.08      8976.376     0.0000000E+00 010102\n" +
                        "  -25921.21     -21526.89     0.0000000E+00 0.0000000E+00  291.9312     001102\n" +
                        "  -575.4608     -442.8280     0.0000000E+00 0.0000000E+00  82.37786     100003\n" +
                        "  -1131.668     -863.4460     0.0000000E+00 0.0000000E+00  173.0422     010003\n" +
                        "  0.0000000E+00 0.0000000E+00 -220.7042     -160.4537     0.0000000E+00 001003\n" +
                        "  -52483.57     -37043.07     0.0000000E+00 0.0000000E+00 -73951.68     000400\n" +
                        "  0.0000000E+00 0.0000000E+00  136057.9      143610.1     0.0000000E+00 000301\n" +
                        "  -17623.41     -14648.73     0.0000000E+00 0.0000000E+00 -19.89475     000202\n" +
                        "  0.0000000E+00 0.0000000E+00 -361.1742     -301.8505     0.0000000E+00 000103\n" +
                        "   37.30861      25.70526     0.0000000E+00 0.0000000E+00 -8.129844     000004\n" +
                        "   9876319.      8915546.     0.0000000E+00 0.0000000E+00 -137548.1     500000\n" +
                        "  0.6369739E+08 0.5779359E+08 0.0000000E+00 0.0000000E+00 -1416980.     410000\n" +
                        "  0.1643096E+09 0.1498188E+09 0.0000000E+00 0.0000000E+00 -5191921.     320000\n" +
                        "  0.2120647E+09 0.1942927E+09 0.0000000E+00 0.0000000E+00 -8962596.     230000\n" +
                        "  0.1370291E+09 0.1261289E+09 0.0000000E+00 0.0000000E+00 -7470127.     140000\n" +
                        "  0.3548756E+08 0.3280928E+08 0.0000000E+00 0.0000000E+00 -2437944.     050000\n" +
                        "  0.0000000E+00 0.0000000E+00  853817.4      1047449.     0.0000000E+00 401000\n" +
                        "  0.0000000E+00 0.0000000E+00 -4654263.     -2659741.     0.0000000E+00 311000\n" +
                        "  0.0000000E+00 0.0000000E+00-0.2428813E+08-0.1880048E+08 0.0000000E+00 221000\n" +
                        "  0.0000000E+00 0.0000000E+00-0.3260595E+08-0.2667598E+08 0.0000000E+00 131000\n" +
                        "  0.0000000E+00 0.0000000E+00-0.1396217E+08-0.1170246E+08 0.0000000E+00 041000\n" +
                        "  0.1519081E+08 0.1308783E+08 0.0000000E+00 0.0000000E+00 -244915.3     302000\n" +
                        "  0.6644523E+08 0.5742206E+08 0.0000000E+00 0.0000000E+00  2584774.     212000\n" +
                        "  0.9588113E+08 0.8310668E+08 0.0000000E+00 0.0000000E+00  8160361.     122000\n" +
                        "  0.4514026E+08 0.3920237E+08 0.0000000E+00 0.0000000E+00  5795904.     032000\n" +
                        "  0.0000000E+00 0.0000000E+00 0.6999890E+08 0.6152011E+08 0.0000000E+00 203000\n" +
                        "  0.0000000E+00 0.0000000E+00 0.1840206E+09 0.1619683E+09 0.0000000E+00 113000\n" +
                        "  0.0000000E+00 0.0000000E+00 0.1189244E+09 0.1047840E+09 0.0000000E+00 023000\n" +
                        "  0.3156566E+08 0.2775053E+08 0.0000000E+00 0.0000000E+00  4346108.     104000\n" +
                        "  0.4622332E+08 0.4097278E+08 0.0000000E+00 0.0000000E+00  5561604.     014000\n" +
                        "  0.0000000E+00 0.0000000E+00 0.1596447E+08 0.1400318E+08 0.0000000E+00 005000\n" +
                        "  0.0000000E+00 0.0000000E+00  5262062.      4961783.     0.0000000E+00 400100\n" +
                        "  0.0000000E+00 0.0000000E+00 0.1571648E+08 0.1547489E+08 0.0000000E+00 310100\n" +
                        "  0.0000000E+00 0.0000000E+00 0.1077259E+08 0.1249166E+08 0.0000000E+00 220100\n" +
                        "  0.0000000E+00 0.0000000E+00 -6080274.     -2939767.     0.0000000E+00 130100\n" +
                        "  0.0000000E+00 0.0000000E+00 -6561490.     -5061297.     0.0000000E+00 040100\n" +
                        "  0.2730200E+08 0.2334698E+08 0.0000000E+00 0.0000000E+00 -2397571.     301100\n" +
                        "  0.1314325E+09 0.1132914E+09 0.0000000E+00 0.0000000E+00 -22165.99     211100\n" +
                        "  0.2041582E+09 0.1770820E+09 0.0000000E+00 0.0000000E+00 0.1288981E+08 121100\n" +
                        "  0.1017797E+09 0.8865534E+08 0.0000000E+00 0.0000000E+00 0.1174596E+08 031100\n" +
                        "  0.0000000E+00 0.0000000E+00 0.2781240E+09 0.2452640E+09 0.0000000E+00 202100\n" +
                        "  0.0000000E+00 0.0000000E+00 0.7360949E+09 0.6499900E+09 0.0000000E+00 112100\n" +
                        "  0.0000000E+00 0.0000000E+00 0.4789429E+09 0.4233084E+09 0.0000000E+00 022100\n" +
                        "  0.1638734E+09 0.1442572E+09 0.0000000E+00 0.0000000E+00 0.2608645E+08 103100\n" +
                        "  0.2396128E+09 0.2125915E+09 0.0000000E+00 0.0000000E+00 0.3382354E+08 013100\n" +
                        "  0.0000000E+00 0.0000000E+00 0.1038368E+09 0.9131137E+08 0.0000000E+00 004100\n" +
                        "  -2806931.     -2424166.     0.0000000E+00 0.0000000E+00  240377.6     400001\n" +
                        " -0.1543093E+08-0.1335286E+08 0.0000000E+00 0.0000000E+00  1442236.     310001\n" +
                        " -0.3192419E+08-0.2768424E+08 0.0000000E+00 0.0000000E+00  3285248.     220001\n" +
                        " -0.2944909E+08-0.2559437E+08 0.0000000E+00 0.0000000E+00  3354255.     130001\n" +
                        " -0.1022722E+08 -8906722.     0.0000000E+00 0.0000000E+00  1296506.     040001\n" +
                        "  0.0000000E+00 0.0000000E+00  1130559.      1449066.     0.0000000E+00 301001\n" +
                        "  0.0000000E+00 0.0000000E+00  2847277.      4146406.     0.0000000E+00 211001\n" +
                        "  0.0000000E+00 0.0000000E+00  2292452.      3960905.     0.0000000E+00 121001\n" +
                        "  0.0000000E+00 0.0000000E+00  619165.8      1299031.     0.0000000E+00 031001\n" +
                        "   453743.6     -583181.0     0.0000000E+00 0.0000000E+00 -1398346.     202001\n" +
                        "   2531330.     -328015.7     0.0000000E+00 0.0000000E+00 -3993182.     112001\n" +
                        "   3061767.      1028698.     0.0000000E+00 0.0000000E+00 -2933362.     022001\n" +
                        "  0.0000000E+00 0.0000000E+00 -5387450.     -5809320.     0.0000000E+00 103001\n" +
                        "  0.0000000E+00 0.0000000E+00 -4886265.     -5687454.     0.0000000E+00 013001\n" +
                        "  -2770156.     -2480624.     0.0000000E+00 0.0000000E+00 -142178.7     004001\n" +
                        "   9654332.      8019462.     0.0000000E+00 0.0000000E+00 -2651249.     300200\n" +
                        "  0.5676874E+08 0.4837320E+08 0.0000000E+00 0.0000000E+00 -4378839.     210200\n" +
                        "  0.9973285E+08 0.8618454E+08 0.0000000E+00 0.0000000E+00  2784213.     120200\n" +
                        "  0.5397983E+08 0.4703428E+08 0.0000000E+00 0.0000000E+00  5328868.     030200\n" +
                        "  0.0000000E+00 0.0000000E+00 0.3658303E+09 0.3235188E+09 0.0000000E+00 201200\n" +
                        "  0.0000000E+00 0.0000000E+00 0.9770310E+09 0.8650409E+09 0.0000000E+00 111200\n" +
                        "  0.0000000E+00 0.0000000E+00 0.6413837E+09 0.5682839E+09 0.0000000E+00 021200\n" +
                        "  0.3185482E+09 0.2807248E+09 0.0000000E+00 0.0000000E+00 0.5837370E+08 102200\n" +
                        "  0.4657916E+09 0.4135602E+09 0.0000000E+00 0.0000000E+00 0.7654979E+08 012200\n" +
                        "  0.0000000E+00 0.0000000E+00 0.2713026E+09 0.2391442E+09 0.0000000E+00 003200\n" +
                        "  0.0000000E+00 0.0000000E+00  187482.9      730714.1     0.0000000E+00 300101\n" +
                        "  0.0000000E+00 0.0000000E+00 -956899.5      1236957.     0.0000000E+00 210101\n" +
                        "  0.0000000E+00 0.0000000E+00 -2783221.      58468.72     0.0000000E+00 120101\n" +
                        "  0.0000000E+00 0.0000000E+00 -1623135.     -436494.5     0.0000000E+00 030101\n" +
                        "   3657477.      650657.6     0.0000000E+00 0.0000000E+00 -3376877.     201101\n" +
                        "  0.1229903E+08  4090286.     0.0000000E+00 0.0000000E+00 -9857090.     111101\n" +
                        "  0.1131151E+08  5533666.     0.0000000E+00 0.0000000E+00 -7359087.     021101\n" +
                        "  0.0000000E+00 0.0000000E+00-0.1791545E+08-0.2008287E+08 0.0000000E+00 102101\n" +
                        "  0.0000000E+00 0.0000000E+00-0.1513860E+08-0.1889808E+08 0.0000000E+00 012101\n" +
                        " -0.1427518E+08-0.1283843E+08 0.0000000E+00 0.0000000E+00 -977501.8     003101\n" +
                        "   361200.6      295350.0     0.0000000E+00 0.0000000E+00 -63268.50     300002\n" +
                        "   1644288.      1344499.     0.0000000E+00 0.0000000E+00 -297044.5     210002\n" +
                        "   2483155.      2030410.     0.0000000E+00 0.0000000E+00 -464233.0     120002\n" +
                        "   1252917.      1023636.     0.0000000E+00 0.0000000E+00 -245252.0     030002\n" +
                        "  0.0000000E+00 0.0000000E+00 -65128.64     -69952.79     0.0000000E+00 201002\n" +
                        "  0.0000000E+00 0.0000000E+00 -205846.2     -212239.8     0.0000000E+00 111002\n" +
                        "  0.0000000E+00 0.0000000E+00 -148314.4     -155082.7     0.0000000E+00 021002\n" +
                        "   24392.55      66440.20     0.0000000E+00 0.0000000E+00  121860.6     102002\n" +
                        "  -215759.4     -119597.0     0.0000000E+00 0.0000000E+00  204681.2     012002\n" +
                        "  0.0000000E+00 0.0000000E+00 -66655.59     -22045.91     0.0000000E+00 003002\n" +
                        "  0.0000000E+00 0.0000000E+00 0.1589721E+09 0.1409168E+09 0.0000000E+00 200300\n" +
                        "  0.0000000E+00 0.0000000E+00 0.4294006E+09 0.3810013E+09 0.0000000E+00 110300\n" +
                        "  0.0000000E+00 0.0000000E+00 0.2849841E+09 0.2529828E+09 0.0000000E+00 020300\n" +
                        "  0.2751789E+09 0.2427111E+09 0.0000000E+00 0.0000000E+00 0.5787683E+08 101300\n" +
                        "  0.4028739E+09 0.3578570E+09 0.0000000E+00 0.0000000E+00 0.7668343E+08 011300\n" +
                        "  0.0000000E+00 0.0000000E+00 0.3559003E+09 0.3143939E+09 0.0000000E+00 002300\n" +
                        "   4088001.      1953871.     0.0000000E+00 0.0000000E+00 -1989945.     200201\n" +
                        "  0.1210134E+08  6295087.     0.0000000E+00 0.0000000E+00 -5963979.     110201\n" +
                        "   9866873.      5794835.     0.0000000E+00 0.0000000E+00 -4545692.     020201\n" +
                        "  0.0000000E+00 0.0000000E+00-0.1935173E+08-0.2281827E+08 0.0000000E+00 101201\n" +
                        "  0.0000000E+00 0.0000000E+00-0.1465795E+08-0.2033665E+08 0.0000000E+00 011201\n" +
                        " -0.2749685E+08-0.2481898E+08 0.0000000E+00 0.0000000E+00 -2370020.     002201\n" +
                        "  0.0000000E+00 0.0000000E+00  10193.62     -15297.54     0.0000000E+00 200102\n" +
                        "  0.0000000E+00 0.0000000E+00 -4792.621     -66733.59     0.0000000E+00 110102\n" +
                        "  0.0000000E+00 0.0000000E+00 -4047.057     -50597.88     0.0000000E+00 020102\n" +
                        "  -8183.654      93963.27     0.0000000E+00 0.0000000E+00  303635.0     101102\n" +
                        "  -664731.8     -424844.6     0.0000000E+00 0.0000000E+00  511924.4     011102\n" +
                        "  0.0000000E+00 0.0000000E+00 -391906.0     -221873.8     0.0000000E+00 002102\n" +
                        "  -35819.94     -26701.77     0.0000000E+00 0.0000000E+00  9642.745     200003\n" +
                        "  -113344.4     -84973.53     0.0000000E+00 0.0000000E+00  29855.77     110003\n" +
                        "  -92296.57     -69199.28     0.0000000E+00 0.0000000E+00  24515.33     020003\n" +
                        "  0.0000000E+00 0.0000000E+00  8102.654      8806.598     0.0000000E+00 101003\n" +
                        "  0.0000000E+00 0.0000000E+00  5722.246      8609.792     0.0000000E+00 011003\n" +
                        "   25525.36      17784.97     0.0000000E+00 0.0000000E+00 -8454.741     002003\n" +
                        "  0.8925521E+08 0.7877032E+08 0.0000000E+00 0.0000000E+00 0.2147934E+08 100400\n" +
                        "  0.1309725E+09 0.1163509E+09 0.0000000E+00 0.0000000E+00 0.2874543E+08 010400\n" +
                        "  0.0000000E+00 0.0000000E+00 0.2344075E+09 0.2074634E+09 0.0000000E+00 001400\n" +
                        "  0.0000000E+00 0.0000000E+00 -6624047.     -8401323.     0.0000000E+00 100301\n" +
                        "  0.0000000E+00 0.0000000E+00 -4088295.     -6885288.     0.0000000E+00 010301\n" +
                        " -0.2350027E+08-0.2126860E+08 0.0000000E+00 0.0000000E+00 -2461807.     001301\n" +
                        "  -59157.48     -467.7312     0.0000000E+00 0.0000000E+00  183302.2     100202\n" +
                        "  -511342.6     -364826.0     0.0000000E+00 0.0000000E+00  313468.5     010202\n" +
                        "  0.0000000E+00 0.0000000E+00 -655252.4     -447412.1     0.0000000E+00 001202\n" +
                        "  0.0000000E+00 0.0000000E+00  3418.490      6701.933     0.0000000E+00 100103\n" +
                        "  0.0000000E+00 0.0000000E+00 -4626.054      2250.024     0.0000000E+00 010103\n" +
                        "   76372.94      54643.98     0.0000000E+00 0.0000000E+00 -21803.95     001103\n" +
                        "   1752.475      1203.388     0.0000000E+00 0.0000000E+00 -625.4165     100004\n" +
                        "   3568.269      2419.903     0.0000000E+00 0.0000000E+00 -1252.912     010004\n" +
                        "  0.0000000E+00 0.0000000E+00  138.4764      58.16622     0.0000000E+00 001004\n" +
                        "  0.0000000E+00 0.0000000E+00 0.6202209E+08 0.5497963E+08 0.0000000E+00 000500\n" +
                        "  -7533059.     -6827684.     0.0000000E+00 0.0000000E+00 -937285.0     000401\n" +
                        "  0.0000000E+00 0.0000000E+00 -338130.3     -257331.0     0.0000000E+00 000302\n" +
                        "   57259.85      41847.26     0.0000000E+00 0.0000000E+00 -14003.35     000203\n" +
                        "  0.0000000E+00 0.0000000E+00  452.8248      309.7903     0.0000000E+00 000104\n" +
                        "  -99.23931     -58.86795     0.0000000E+00 0.0000000E+00  38.95072     000005"
        );
    }


    final static double Bp = 2.43213; // 磁钢度

    final static double MM = 1e-3;
    final static double MRAD = 1e-3;

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
