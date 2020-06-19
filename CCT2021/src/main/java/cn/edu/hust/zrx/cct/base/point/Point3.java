package cn.edu.hust.zrx.cct.base.point;

import cn.edu.hust.zrx.cct.base.vector.Vector2;
import cn.edu.hust.zrx.cct.base.vector.Vector3;

import java.util.ArrayList;
import java.util.List;

/**
 * Description
 * 三维点
 * <p>
 * Data
 * 21:18
 *
 * @author zrx
 * @version 1.0
 */

public class Point3 {
    public double x;
    public double y;
    public double z;


    private Point3(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public static Point3 create(double x, double y, double z) {
        return new Point3(x, y, z);
    }

    public static Point3 origin() {
        return create(0, 0, 0);
    }

    public static Point3 midPoint(Point3 p0, Point3 p1) {
        return create(
                (p0.x + p1.x) / 2,
                (p0.y + p1.y) / 2,
                (p0.z + p1.z) / 2
        );
    }

    public static double distance(Point3 c, Point3 p) {
        return Vector3.from(c).to(p).length();
    }

    public Point3 copy() {
        return create(this.x, this.y, this.z);
    }

    public Vector3 toVector3() {
        return Vector3.create(this.x, this.y, this.z);
    }

    public Point3 moveSelf(Vector3 v) {
        this.x += v.x;
        this.y += v.y;
        this.z += v.z;

        return this;
    }

    public static List<Point3> move(final List<Point3> point3s, Vector3 v) {
        List<Point3> ret = new ArrayList<>(point3s.size());

        for (Point3 point3 : point3s) {
            ret.add(point3.copy().moveSelf(v));
        }

        return ret;
    }

    public Point3 rotateSelfInXYPlane(Point2 center, double phi) {
        Point2 origin = this.toPoint2();
        Vector2 rotated = Vector2.from(center).to(origin).rotateSelf(phi);

        Vector2 add = center.toVector2().add(rotated);

        return Point3.create(add.x, add.y, this.z);
    }

    public static List<Point3> rotateInXYPlane(final List<Point3> point3s, Point2 center, double phi) {
        List<Point3> ret = new ArrayList<>(point3s.size());

        for (Point3 point3 : point3s) {
            ret.add(point3.rotateSelfInXYPlane(center, phi));
        }

        return ret;
    }

    @Override
    public String toString() {
        return List.of(x, y, z).toString();
    }

    public String toCadString(double unit) {
        return x / unit + "," + y / unit + "," + z / unit;
    }

    public Point2 toPoint2() {
        return Point2.create(x, y);
    }

}
