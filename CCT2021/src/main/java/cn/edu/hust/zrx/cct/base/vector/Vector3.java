package cn.edu.hust.zrx.cct.base.vector;

import cn.edu.hust.zrx.cct.base.BaseUtils;
import cn.edu.hust.zrx.cct.base.point.Point2;
import cn.edu.hust.zrx.cct.base.point.Point3;

import java.util.Arrays;
import java.util.List;

/**
 * Description
 * 三维矢量
 * <p>
 * Data
 * 21:42
 *
 * @author zrx
 * @version 1.0
 */

public class Vector3 {
    public double x;
    public double y;
    public double z;

    public double length() {
        return Math.sqrt(
                x * x + y * y + z * z
        );
    }

    public Vector3 changeLengthSelf(double length) {
        normalSelf();
        this.x *= length;
        this.y *= length;
        this.z *= length;

        return this;
    }

    public final Vector3 normalSelf() {
        double len = length();
        double nx = this.x / len;
        double ny = this.y / len;
        double nz = this.z / len;

        this.x = nx;
        this.y = ny;
        this.z = nz;

        return this;
    }

    public static Vector3 cross(Vector3 a, Vector3 b) {
        return create(a.y * b.z - a.z * b.y,
                -a.x * b.z + a.z * b.x,
                a.x * b.y - a.y * b.x);
    }

    public Vector3 cross(Vector3 b) {
        return cross(this, b);
    }

    public static Vector3 dot(Vector3 p, double a) {
        return create(p.x * a, p.y * a, p.z * a);
    }

    public static Vector3 dot(double a, Vector3 p) {
        return create(p.x * a, p.y * a, p.z * a);
    }

    public static double dot(Vector3 a, Vector3 b) {
        return a.x * b.x +
                a.y * b.y +
                a.z * b.z;
    }

    public double dot(Vector3 b) {
        return dot(this, b);
    }

    public Vector3 dot(double a) {
        return dot(a, this);
    }

    public static Vector3 subtract(final Point3 a, final Point3 b) {
        return create(
                a.x - b.x,
                a.y - b.y,
                a.z - b.z
        );
    }

    public static Vector3 subtract(final Vector3 a, final Vector3 b) {
        return create(
                a.x - b.x,
                a.y - b.y,
                a.z - b.z
        );
    }


    public Vector3 subtract(final Vector3 b) {
        return subtract(this, b);
    }


    /**
     * a b 间夹角，角度
     *
     * @param a 矢量 a
     * @param b 矢量 b
     * @return 夹角，单位: 角度
     */
    public static double angleIncluding(final Vector3 a, final Vector3 b) {
        return BaseUtils.Converter.radianToAngle(angleIncludingRadian(a, b));
    }

    /**
     * a b 间夹角，弧度
     *
     * @param a 矢量 a
     * @param b 矢量 b
     * @return 夹角，单位: 弧度
     */
    public static double angleIncludingRadian(final Vector3 a, final Vector3 b) {
        double cosTheta = Math.abs(dot(a, b)) / (a.length() * b.length());
        return Math.acos(cosTheta);
    }

    /**
     * this 和 a 的夹角，度数
     *
     * @param a 矢量 a
     * @return 夹角，单位: 角度
     */
    public double angleIncludingTo(final Vector3 a) {
        return angleIncluding(this, a);
    }

    private Vector3(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public static Vector3 getZero() {
        return create(0, 0, 0);
    }

    public static Vector3 create(double x, double y, double z) {
        return new Vector3(x, y, z);
    }

    public static Vector3 getXDirect() {
        return create(1, 0, 0);
    }

    public static Vector3 getYDirect() {
        return create(0, 1, 0);
    }

    public static Vector3 getZDirect() {
        return create(0, 0, 1);
    }

    public Vector3 copy() {
        return create(this.x, this.y, this.z);
    }

    public Point3 toPoint3() {
        return Point3.create(x, y, z);
    }

    public Vector3 addSelf(Vector3 b) {
        this.x += b.x;
        this.y += b.y;
        this.z += b.z;

        return this;
    }

    public static Vector3From from(Point3 source) {
        return new Vector3From(source);
    }

    public static class Vector3From {
        private Point3 source;

        private Vector3From(final Point3 source) {
            this.source = source;
        }

        public Vector3 to(Point3 destination) {
            return Vector3.subtract(destination, source);
        }
    }

    @Override
    public String toString() {
        return Arrays.toString(List.of(x, y, z).toArray());
    }
}
