package cn.edu.hust.zrx.cct.base.vector;

import cn.edu.hust.zrx.cct.base.point.Point2;

/**
 * Description
 * 二维向量
 * p = (x, y)
 * ------------------------
 * 2020年2月10日 新增方法
 * public static Vector2 create(double x, double y)
 * public Vector2 copy()
 * public Point2 toPoint2()
 * public static Vector2 xDirect(double length)
 * public static Vector2 yDirect(double length)
 * public static Vector2 xDirect()
 * public static Vector2 yDirect()
 * from p1 to p2 构造方法 Vector2.from(Point2.create(2, 3)).to(Point2.create(4, 5))
 * public Vector2 changeLengthSelf(double length)
 * public final Vector2 normalSelf()
 * public final Vector2 normal()
 * public double length()
 * public static Vector2 subtract(final Vector2 subtracted, final Vector2 subtraction)
 * public Vector2 subtractSelf(final Vector2 b)
 * public Vector2 subtract(final Vector2 b)
 * public static Vector2 matmul(final Vector2 v, final double[][] A)
 * public static Vector2 rotate(final Vector2 v, final double phi)
 * public Vector2 rotateSelf(final double phi)
 * public double angleToXAxis()
 * public static boolean isEqual(final Vector2 a, final Vector2 b, double error)
 * public static boolean isEqual(final Vector2 a, final Vector2 b)
 * ----------------------------------------------
 * 2020年2月11日 新增方法
 * public String toString()
 * <p>
 * Data
 * 20:08
 *
 * @author zrx
 * @version 1.0
 */

public class Vector2 {
    public double x;
    public double y;

    /*----------------简单变换------------------*/

    /**
     * 改变矢量长度
     * 如果长度小于零，则等于反向，然后再 changeLengthSelf(-length)
     *
     * @param length 长度
     * @return 改变长度后的矢量
     * @since 2020年2月10日
     */
    public Vector2 changeLengthSelf(double length) {
        this.normalSelf();

        this.x *= length;
        this.y *= length;

        return this;
    }

    /**
     * 自身正则化 矢量长度归一
     *
     * @return 归一化矢量
     * @since 2020年2月10日
     */
    public final Vector2 normalSelf() {
        double len = length();
        double nx = this.x / len;
        double ny = this.y / len;

        this.x = nx;
        this.y = ny;

        return this;
    }

    /**
     * 正则化 矢量长度归一
     * 返回新的矢量，原矢量不受影响
     *
     * @return 归一化矢量
     * @since 2020年2月10日
     */
    public final Vector2 normal() {
        return this.copy().normalSelf();
    }


    /*----------------简单运算------------------*/

    /**
     * 矢量长度
     *
     * @return 矢量长度
     * @since 2020年2月10日
     */
    public double length() {
        return Math.sqrt(x * x + y * y);
    }

    /**
     * 减法
     * a-b
     *
     * @param a 被减数
     * @param b 减数
     * @return a-b
     * @since 2020年2月10日
     */
    public static Vector2 subtract(final Vector2 a, final Vector2 b) {
        return create(
                a.x - b.x,
                a.y - b.y
        );
    }

    /**
     * 减法
     * this 自身减去 b，this 改变
     *
     * @param b 减数
     * @return this-b 后的 this
     * @since 2020年2月10日
     */
    public Vector2 subtractSelf(final Vector2 b) {
        this.x -= b.x;
        this.y -= b.y;

        return this;
    }

    /**
     * 减法
     * this 减去 b，this 不改变
     *
     * @param b 减数
     * @return this-b，this 不改变
     * @since 2020年2月10日
     */
    public Vector2 subtract(final Vector2 b) {
        return Vector2.subtract(this, b);
    }

    /**
     * 计算 A*v
     * 这个方法主要用于旋转矩阵
     *
     * @param v 矢量
     * @param A 2*2矩阵
     * @return A*v
     * @since 2020年2月10日
     */
    public static Vector2 matmul(final Vector2 v, final double[][] A) {
        return create(
                A[0][0] * v.x + A[0][1] * v.y,
                A[1][0] * v.x + A[1][1] * v.y
        );
    }


    /**
     * 旋转矩阵
     *
     * @param phi 旋转弧度
     * @return double[2][2]
     * @since 2020年2月10日
     */
    private static double[][] rotateR(final double phi) {
        return new double[][]{
                {Math.cos(phi), -Math.sin(phi)},
                {Math.sin(phi), Math.cos(phi)}
        };
    }

    /**
     * 矢量旋转。phi>0 ，逆时针旋转
     *
     * @param v   原矢量
     * @param phi 旋转弧度
     * @return 旋转后矢量
     * @since 2020年2月10日
     */
    public static Vector2 rotate(final Vector2 v, final double phi) {
        return matmul(v, rotateR(phi));
    }

    /**
     * 矢量旋转。phi>0 ，逆时针旋转
     *
     * @param phi 旋转弧度
     * @return 旋转后的矢量
     * @since 2020年2月10日
     */
    public Vector2 rotateSelf(final double phi) {
        Vector2 v = rotate(this, phi);

        this.x = v.x;
        this.y = v.y;

        return this;
    }

    /**
     * 矢量 this 和 x 轴的夹角
     * 具体计算方法同极坐标的 θ 角
     *
     * @return 矢量 this 和 x 轴的夹角
     * @since 2020年2月10日
     */
    public double angleToXAxis() {
        return Math.atan2(y, x);
    }



    /*----------------构造函数、简单工厂------------------*/

    /**
     * 免去直接构造
     *
     * @since 2020年2月10日
     */
    private Vector2(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * 简单工厂
     *
     * @return (x, y)
     * @since 2020年2月10日
     */
    public static Vector2 create(double x, double y) {
        return new Vector2(x, y);
    }

    /**
     * 复制实例
     *
     * @return 实例的复制
     * @since 2020年2月10日
     */
    public Vector2 copy() {
        return create(this.x, this.y);
    }

    /**
     * 转化为点
     *
     * @return 转化的点
     * @since 2020年2月10日
     */
    public Point2 toPoint2() {
        return Point2.create(this.x, this.y);
    }

    public Vector3 toVector3(){
        return Vector3.create(this.x,this.y,0);
    }

    /**
     * x 方向矢量
     *
     * @param length 长度
     * @return x 方向矢量
     * @since 2020年2月10日
     */
    public static Vector2 xDirect(double length) {
        return create(length, 0);
    }

    /**
     * x 方向单位矢量
     *
     * @return x 方向单位矢量
     * @since 2020年2月10日
     */
    public static Vector2 xDirect() {
        return xDirect(1.0);
    }

    /**
     * y 方向矢量
     *
     * @param length 长度
     * @return y 方向矢量
     * @since 2020年2月10日
     */
    public static Vector2 yDirect(double length) {
        return create(0, length);
    }

    /**
     * y 方向单位矢量
     *
     * @return y 方向单位矢量
     * @since 2020年2月10日
     */
    public static Vector2 yDirect() {
        return yDirect(1.0);
    }

    /**
     * 配合内部类 Vector2From::to 使用
     * 使用方法示例：
     * Vector2 a = Vector2.from(Point2.create(2, 3))
     * .to(Point2.create(4, 5));
     *
     * @param source 起点
     * @return 中间类 Vector2From
     * @since 2020年2月10日
     */
    public static Vector2From from(Point2 source) {
        return new Vector2From(source);
    }

    /**
     * @param x0 source.x
     * @param y0 source.y
     * @return 中间类 Vector2From
     * @see Vector2#from(Point2 source)
     * @since 2020年2月10日
     */
    public static Vector2From from(double x0, double y0) {
        return new Vector2From(Point2.create(x0, y0));
    }

    /**
     * 复制矢量
     * 使用实例方法 copy
     *
     * @param v 源矢量
     * @return 复制
     * @since 2020年2月10日
     */
    @Deprecated
    public static Vector2 copyOf(Vector2 v) {
        return new Vector2(v.x, v.y);
    }

    /**
     * 辅助类，仅用于方法 from()，使用方法见 from() 注释
     *
     * @since 2020年2月10日
     */
    public static class Vector2From {
        private Point2 source;

        private Vector2From(final Point2 source) {
            this.source = source;
        }

        public Vector2 to(Point2 destination) {
            return Vector2.subtract(destination.toVector2(), source.toVector2());
        }

        public Vector2 to(double x1, double y1) {
            return this.to(Point2.create(x1, y1));
        }
    }

    /**
     * 判断两个矢量是否相等，误差 error
     * 具体判断方法：|a-b|<error
     * 两个矢量做差，判断其长度小于 error
     *
     * @param a     矢量 a
     * @param b     矢量 b
     * @param error 误差
     * @return 是否相等
     * @since 2020年2月10日
     */
    public static boolean isEqual(final Vector2 a, final Vector2 b, double error) {
        return Vector2.subtract(a, b).length() < Math.abs(error);
    }

    /**
     * 见 isEqual(final Vector2 a,final Vector2 b,double error)
     * 误差 error = 1e-8
     *
     * @param a 矢量 a
     * @param b 矢量 b
     * @return 是否相等
     * @since 2020年2月10日
     */
    public static boolean isEqual(final Vector2 a, final Vector2 b) {
        return isEqual(a, b, 1e-8);
    }

    /**
     * @return toString
     * @since 2020年2月11日
     */
    @Override
    public String toString() {
        return "[" + x + ", " + y + "]";
    }
}
