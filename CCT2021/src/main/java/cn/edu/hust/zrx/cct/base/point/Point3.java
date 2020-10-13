package cn.edu.hust.zrx.cct.base.point;

import cn.edu.hust.zrx.cct.advanced.MathFunction;
import cn.edu.hust.zrx.cct.base.BaseUtils;
import cn.edu.hust.zrx.cct.base.vector.Vector2;
import cn.edu.hust.zrx.cct.base.vector.Vector3;
import cn.edu.hust.zrx.cct.base.vector.Vector3Function;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.Stream;

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

    /**
     * 美妙
     *
     * @param xFun         函数
     * @param yFun         函数
     * @param zFun         函数
     * @param doubleStream 流
     * @return List<Point3>
     */
    public static List<Point3> create(MathFunction xFun, MathFunction yFun, MathFunction zFun, DoubleStream doubleStream) {
        return doubleStream.mapToObj(theta -> Point3.create(xFun.apply(theta), yFun.apply(theta), zFun.apply(theta)))
                .collect(Collectors.toList());
    }

    public static List<Point3> create(Vector3Function vector3Function, DoubleStream doubleStream) {
        return doubleStream.mapToObj(vector3Function::apply).map(Vector3::toPoint3).collect(Collectors.toList());
    }

    public static List<Point3> create(Point3Function point3Function, DoubleStream doubleStream) {
        return doubleStream.mapToObj(point3Function::apply).collect(Collectors.toList());
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

    /**
     * 返回 v3 = this -> destination
     *
     * @param destination 目的地
     * @return v3 = this -> destination
     */
    public Vector3 to(Point3 destination) {
        return Vector3.from(this).to(destination);
    }

    public Point3 enlarge(double times) {
        return create(
                x * times,
                y * times,
                z * times
        );
    }
}
