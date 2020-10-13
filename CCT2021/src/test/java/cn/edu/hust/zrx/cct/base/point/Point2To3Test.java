package cn.edu.hust.zrx.cct.base.point;

import cn.edu.hust.zrx.cct.Logger;
import cn.edu.hust.zrx.cct.advanced.combined.GantryAnalysor;
import cn.edu.hust.zrx.cct.advanced.combined.GantryData;
import cn.edu.hust.zrx.cct.base.BaseUtils;
import cn.edu.hust.zrx.cct.base.cct.SoleLayerCct;
import cn.edu.hust.zrx.cct.base.line.Trajectory;
import cn.edu.hust.zrx.cct.base.line.TrajectoryFactory;
import cn.edu.hust.zrx.cct.base.python.Plot2d;
import cn.edu.hust.zrx.cct.base.vector.Vector2;

import java.util.List;

import static java.lang.Math.PI;

class Point2To3Test {
    public static void main(String[] args) {
        b();

    }

    public static void b() {
        GantryData.SecondPart secondPart = GantryAnalysor.defaultSecondPart();

        Point3Function innerDipoleCctPath3d = secondPart.innerDipoleCctPath3d();
        Point3Function outerDipoleCctPath3d = secondPart.outerDipoleCctPath3d();

        Point2Function innerDipoleCctPath2d = secondPart.innerDipoleCctPath2d().convertToPoint2Function();
        Point2Function outerDipoleCctPath2d = secondPart.outerDipoleCctPath2d().convertToPoint2Function();

//        OperaCct operaCct = new OperaCct(
//                innerDipoleCctPath3d, innerDipoleCctPath2d,
//                Point2To3.SimpleToroidalCoordinateSystemPoint2To3.create(secondPart.dipoleCct345BigR, secondPart.dipoleCct345SmallRInner),
//                2 * MM, 8 * MM,
//                secondPart.dipoleCct345IInner / (0.2 * 0.8),
//                0,
//                2 * PI * secondPart.dipoleCct345WindingNumber,
//                180 * secondPart.dipoleCct345WindingNumber
//        );

//        Plot3d.plot3(innerDipoleCctPath3d.disperse(0, 201 * 2 * PI, 201 * 360));
//


        SoleLayerCct soleLayerCct =  SoleLayerCct.create(innerDipoleCctPath3d.disperse(0, 201 * 2 * PI, 201 * 360),secondPart.dipoleCct345IInner);
        Trajectory trajectory = TrajectoryFactory.setStartingPoint(0.95, 0).setStraightLine(1e-10, Vector2.yDirect())
                .addArcLine(0.95, false, 67.5);
        List<Point2> bz = soleLayerCct.magnetBzAlongTrajectory(
                trajectory
        );

        Point2 point2 = trajectory.pointAtEnd();
        Logger.getLogger().info("point2 = " + point2);

        Plot2d.plot2(bz);

        Plot2d.showThread();

//        trajectory.plot3d();
//
//        Plot3d.showThread();
    }

    public static void point() {
        GantryData.SecondPart secondPart = GantryAnalysor.defaultSecondPart();
        Point2To3.SimpleToroidalCoordinateSystemPoint2To3 simpleToroidalCoordinateSystemPoint2To3 =
                Point2To3.SimpleToroidalCoordinateSystemPoint2To3.create(secondPart.dipoleCct345BigR, secondPart.dipoleCct345SmallRInner);

        Point2 p0 = Point2.create(0, 0);
        Point2 p1 = Point2.create(2 * Math.PI, BaseUtils.Converter.angleToRadian(67.5));

        Point3 p30 = simpleToroidalCoordinateSystemPoint2To3.convert(p0);
        Point3 p31 = simpleToroidalCoordinateSystemPoint2To3.convert(p1);

        Logger.getLogger().info("p30 = " + p30);

        Logger.getLogger().info("p31 = " + p31);
    }
}