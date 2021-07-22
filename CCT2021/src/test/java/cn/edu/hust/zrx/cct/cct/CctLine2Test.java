package cn.edu.hust.zrx.cct.cct;

import cn.edu.hust.zrx.cct.base.BaseUtils;
import cn.edu.hust.zrx.cct.base.cct.CctLine2;
import cn.edu.hust.zrx.cct.base.cct.CctLine2Factory;
import cn.edu.hust.zrx.cct.base.cct.CctLine2s;
import cn.edu.hust.zrx.cct.base.line.Line2;
import cn.edu.hust.zrx.cct.base.line.StraightLine;
import cn.edu.hust.zrx.cct.base.line.Trajectory;
import cn.edu.hust.zrx.cct.base.line.TrajectoryFactory;
import cn.edu.hust.zrx.cct.base.point.Point2;
import cn.edu.hust.zrx.cct.base.point.Point2To3;
import cn.edu.hust.zrx.cct.base.point.Point3;
import cn.edu.hust.zrx.cct.base.python.Plot2d;
import cn.edu.hust.zrx.cct.base.python.Plot3d;
import cn.edu.hust.zrx.cct.base.vector.Vector2;

import java.lang.reflect.Constructor;
import java.util.List;

/**
 * Description
 * CctLine2Test
 * <p>
 * Data
 * 19:57
 *
 * @author zrx
 * @version 1.0
 */

//@SpringBootTest
public class CctLine2Test {

    //@Test
    public void straightLineTest()throws Exception{
        Class<StraightLine> straightLineClass = StraightLine.class;
        Constructor<StraightLine> declaredConstructor = straightLineClass.getDeclaredConstructor(double.class, Vector2.class, Point2.class);
        declaredConstructor.setAccessible(true);
        StraightLine straightLine = declaredConstructor.newInstance(1, Vector2.create(0, 1), Point2.origin());

        straightLine.plot2d(0.01, Plot2d.BLACK_LINE);

        for (int i = 0; i < 20; i++) {
            System.out.println("straightLine.rightHandSidePoint(0,0.01*i) = " + straightLine.rightHandSidePoint(0, 0.01 * i));

            Plot2d.plotPoint(straightLine.rightHandSidePoint(0,0.01*i),Plot2d.YELLOW_POINT);
        }

        for (int i = 0; i < 20; i++) {
            System.out.println("straightLine.rightHandSidePoint(0.1*i,0.01) = " + straightLine.rightHandSidePoint(0.1 * i, 0.01));
            Plot2d.plotPoint(straightLine.rightHandSidePoint(0.1*i,0.01),Plot2d.RED_POINT);
        }

        Plot2d.show();

    }

    //@Test
    public void line2Test(){
        Line2 trajectory = TrajectoryFactory.setStartingPoint(Point2.create(0.75, -1))
                .setStraightLine(1.0, Vector2.yDirect())
                .addArcLine(0.75, false, 60.0)
                .addStraitLine(1.0);

        trajectory.plot2d(1e-2,Plot2d.BLACK_LINE);
        trajectory.rightHandSideLine2(-5e-3).plot2d(1e-2,Plot2d.BLUE_LINE);
        trajectory.rightHandSideLine2(5e-3).plot2d(1e-2,Plot2d.YELLOW_LINE);

//        final double delta = 0.001;
//        final int number = (int)(trajectory.getLength()/delta);
//        final double goodAreaWidth = 2e-3;
//
//        List<Point2> point2s = BaseUtils.StreamTools.fromZeroIncluding().to(number)
//                .mapToDouble(i -> delta * i)
//                .mapToObj(d -> trajectory.rightHandSidePoint(d, goodAreaWidth))
//                .collect(Collectors.toList());
//        Plot2d.plot2(point2s,Plot2d.YELLOW_LINE);

        Plot2d.show();
    }

    //@Test
    public void test04() {
        CctLine2s agcct = CctLine2Factory.createAGCCT(
                60e-3, 0.75,
                new double[]{25, 10, 25},
                new int[]{50, 20, 50},
                0, 0.02, 0
        );

        List<Point3> agcctDisperse = agcct.disperseToPoint3(360);

        Line2 trajectory = TrajectoryFactory.setStartingPoint(Point2.create(0.75, -1))
                .setStraightLine(1.0, Vector2.yDirect())
                .addArcLine(0.75, false, 60.0)
                .addStraitLine(1.0);

        List<Point2> gradientAlongTrajectory = BaseUtils.Magnet.magnetGradientAlongTrajectoryFast(
                agcctDisperse, 1000, trajectory, 1e-2, 10e-3);

//        final double I = 1000;
//        final double delta = 0.001;
//        final int number = (int)(trajectory.getLength()/delta);
//        final double goodAreaWidth = 2e-3;
//
//        List<Point2> gradientAlongTrajectory = BaseUtils.StreamTools.from(0).to(number)
//                .mapToDouble(i->i*delta)
//                .mapToObj(s->{
//                    Point2 leftHandSidePoint = trajectory.leftHandSidePoint(s, goodAreaWidth);
//                    Point2 rightHandSidePoint = trajectory.rightHandSidePoint(s, goodAreaWidth);
//                    double zLeft = BaseUtils.Magnet.magnetAtPoint(agcctDisperse, I, leftHandSidePoint.toPoint3()).z;
//                    double zRight = BaseUtils.Magnet.magnetAtPoint(agcctDisperse, I, rightHandSidePoint.toPoint3()).z;
//                    return Point2.create(s,(zLeft-zRight)/(2*goodAreaWidth));
//                }).collect(Collectors.toList());
//
//        final double goodAreaWidth2 = 1e-3;
//        List<Point2> gradientAlongTrajectory2 = BaseUtils.StreamTools.from(0).to(number)
//                .mapToDouble(i->i*delta)
//                .mapToObj(s->{
//                    Point2 leftHandSidePoint = trajectory.leftHandSidePoint(s, goodAreaWidth2);
//                    Point2 rightHandSidePoint = trajectory.rightHandSidePoint(s, goodAreaWidth2);
//                    double zLeft = BaseUtils.Magnet.magnetAtPoint(agcctDisperse, I, leftHandSidePoint.toPoint3()).z;
//                    double zRight = BaseUtils.Magnet.magnetAtPoint(agcctDisperse, I, rightHandSidePoint.toPoint3()).z;
//                    return Point2.create(s,(zLeft-zRight)/(2*goodAreaWidth2));
//                }).collect(Collectors.toList());


        Plot2d.plot2(gradientAlongTrajectory, Plot2d.YELLOW_LINE);
//        Plot2d.plot2(gradientAlongTrajectory2, Plot2d.RED_LINE);
        Plot2d.showThread();

        Plot3d.plot3(agcctDisperse, Plot2d.YELLOW_LINE);
        Plot3d.plot3(trajectory.dispersePoint3s(1e-2), Plot2d.BLACK_LINE);
        Plot3d.setCube(0.8);
        Plot3d.show();
    }


    //@Test
    public void test03() {
//        CctLine2s agcct = CctLine2Factory.createAGCCT(
//                60e-3, 0.75,
//                new double[]{25, 10, 25},
//                new int[]{50, 20, 50},
//                0, 0.02, 0
//        );

        CctLine2s cct = CctLine2Factory.create(60e-3, 0.75,
                67.5, 135,
                0.1, 0, 0);

        List<Point3> cctDisperse = cct.disperseToPoint3(360);

        Line2 trajectory = TrajectoryFactory.setStartingPoint(Point2.create(0.75, -1))
                .setStraightLine(1.0, Vector2.yDirect())
                .addArcLine(0.75, false, 67.5)
                .addStraitLine(1.0);

//        List<Point2> magnetDistribution = trajectory.dispersePoint3sWithDistance(
//                1e-2, Point2To3.getXY0ToXYZPoint2To3())
//                .stream()
//                .map(point3WithDistance ->
//                        Point2.create(
//                                point3WithDistance.getDistance(),
//                                BaseUtils.Magnet.magnetAtPoint(
//                                        cctDisperse, 1000, point3WithDistance.getPoint3()).z
//                        )
//                )
//                .collect(Collectors.toList());

        List<Point2> magnetDistribution = BaseUtils.Magnet.magnetBzAlongTrajectory(cctDisperse, 1000, trajectory, 1e-2);

        Plot2d.plot2(magnetDistribution, Plot2d.YELLOW_LINE);
        Plot2d.show();


//        Plot3d.plot3(cctDisperse, Plot2d.YELLOW_LINE);
//        Plot3d.plot3(trajectory.dispersePoint3s(1e-2), Plot2d.BLACK_LINE);
//        Plot3d.setCube(0.8);
//        Plot3d.show();

    }

    //@Test
    public void test02() {
        CctLine2s agcct = CctLine2Factory.createAGCCT(
                60e-3, 0.75,
                new double[]{25, 10, 25},
                new int[]{50, 20, 50},
                0, 0.02, 0
        );

        Plot2d.plot2(agcct.disperseToPoint2(360), Plot2d.BLUE_LINE);

        Plot2d.showThread();

        Plot3d.plot3(agcct.disperseToPoint3(360), Plot2d.YELLOW_LINE);
        Plot3d.setCube(1);
        Plot3d.showThread();
    }

    //@Test
    public void test01() {
//        CctLine2 cctLine2 = new CctLine2(60e-3, 0.75,
//                25.0, 50, 0, 0.02, 0,
//                0, 0, true);
//
//        List<Point2> point2s = cctLine2.disperseToPoint2(360);
//        Plot2d.plot2(point2s,Plot2d.BLUE_LINE);
//
//        CctLine2 cctLine21 = new CctLine2(60e-3, 0.75,
//                10, 20, 0, 0.02, 0,
//                50*2*Math.PI, 50*2*Math.PI/720, false);
//        List<Point2> point2s1 = cctLine21.disperseToPoint2(360);
//        Plot2d.plot2(point2s1,Plot2d.RED_LINE);
//
//        CctLine2 cctLine22 = new CctLine2(60e-3, 0.75,
//                25.0, 50, 0, 0.02, 0,
//                30*2*Math.PI, 70*2*Math.PI/720, true);
//        List<Point2> point2s2 = cctLine22.disperseToPoint2(360);
//        Plot2d.plot2(point2s2,Plot2d.BLUE_LINE);
//
//        Plot2d.showThread();
    }
}
