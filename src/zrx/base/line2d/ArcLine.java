package zrx.base.line2d;

import zrx.Tools.AngleToRadian;
import zrx.base.point.Vector2d;
import zrx.python.Plot2d;

/**
 * 弧线类
 * <p>
 * 测试通过——2019年9月2日
 */

public class ArcLine implements Line {
    //不用new
    private ArcLine() {
    }

    private double length;
    private Vector2d center;
    private double r;
    private double startPhi;
    private double endPhi;
    private boolean clockwise;

    //工厂方法
    public static ArcLine getOne(Vector2d c, double r, double start, double end, boolean clockwise) {
        //Vector2d.arc(
        //      Vector2d.getByStartAndEnd(-rsina, rcosa),
        //      R_beamline,
        //      2.0 * a, a, true, STEP_TRAJ
        //)

        final ArcLine arcLine = new ArcLine();
        arcLine.center = c;
        arcLine.r = r;
        arcLine.startPhi = start;
        arcLine.endPhi = end;
        arcLine.clockwise = clockwise;

        if (clockwise) {
            arcLine.length = (start - end) * r;
        } else {
            arcLine.length = (end - start) * r;
        }

        return arcLine;
    }

    @Override
    public double getLength() {
        return length;
    }

    @Override
    public Vector2d rightHandSidePoint(double s, double d) {
        //获得极点，以center为极心
        double phi;
        if (clockwise)
            phi = startPhi - s / r;
        else
            phi = startPhi + s / r;

        Vector2d sPointAtPolarCoordinate = Vector2d.getOne(r, phi);

        //获得点s
        Vector2d sPointXY = Vector2d.convertFromPolarCoordinatesToCartesianCoordinates(
                center,
                sPointAtPolarCoordinate
        );

        //获得s处方向
        Vector2d sDirect = Vector2d.getOne(-Math.sin(phi), Math.cos(phi));
        //获取s处右手方向
        Vector2d sRightHandDirect = sDirect.rotateSelfAndReturn(-AngleToRadian.to(90.0));

        //移动s
        sPointXY.walkSelf(sRightHandDirect, d);

        //返回
        return sPointXY;
    }

    ////////////////////////////测试/////////////////
    public static void main(String[] args) {
        final Line arcLine = getOne(
                Vector2d.getOne(1, 2),
                1.1,
                0.2,
                1.2,
                false
        );

        System.out.println("arcLine.getLength() = " + arcLine.getLength());


        for (int i = 0; i < 40; i++) {
            Plot2d.plotPoint(arcLine.pointAt(i / 10.0), Plot2d.BLACK_POINT);
            Plot2d.plotPoint(arcLine.leftHandSidePoint(1.1, i / 10.0), Plot2d.RED_POINT);
        }

        Plot2d.plotPoint(arcLine.pointAt(-1), Plot2d.YELLOW_POINT);
        Plot2d.plotPoint(arcLine.leftHandSidePoint(-1, -0.2), Plot2d.YELLOW_POINT);
        Plot2d.plotPoint(arcLine.leftHandSidePoint(-1, +0.2), Plot2d.YELLOW_POINT);

        Plot2d.equal();
        Plot2d.showThread();
    }
}
