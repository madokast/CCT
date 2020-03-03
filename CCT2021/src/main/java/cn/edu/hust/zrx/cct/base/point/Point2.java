package cn.edu.hust.zrx.cct.base.point;

import cn.edu.hust.zrx.cct.base.vector.Vector2;
import cn.edu.hust.zrx.cct.base.vector.Vector3;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.DoubleFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Description
 * 二维坐标的点
 * 即 p(x,y)
 * ---------------------------------
 * 2020年2月10日 新增方法
 * public Point2 copy()
 * public Point2 moveSelf(final Vector2 v)
 * public Vector2 toVector2()
 * private Point2(double x, double y)
 * public static Point2 create(double x, double y)
 * public static Point2 copyOf(final Point2 p)
 * public static Point2 origin()
 * public static Point2 createPointOnX(double y)
 * public static Point2 createPointOnY(double x)
 * -------------------------------------
 * 2020年2月11日 新增方法
 * public String toString()
 *
 * <p>
 * Data
 * 16:11
 *
 * @author zrx
 * @version 1.0
 */

public class Point2 implements Cloneable {
    public double x;
    public double y;

    /**
     * 返回实例的复制
     *
     * @return 实例的复制
     * @since 2020年2月10日
     */
    public Point2 copy() {
        return create(this.x, this.y);
    }


    /**
     * 点的移动
     *
     * @param v 描述移动矢量
     * @return 移动后的点
     * @since 2020年2月10日
     */
    public Point2 moveSelf(final Vector2 v) {
        this.x += v.x;
        this.y += v.y;

        return this;
    }

    /**
     * 矢量化
     *
     * @return 即返回以原点为起点，this为终点的矢量
     * @since 2020年2月10日
     */
    public Vector2 toVector2() {
        return Vector2.create(x, y);
    }


    public Point3 toPoint3(Point2To3 point2To3) {
        return point2To3.convert(this);
    }

    public Point3 toPoint3() {
        return Point2To3.getXY0ToXYZPoint2To3().convert(this);
    }


    /*----------------构造函数、简单工厂------------------*/

    /**
     * 构造函数，但是不应该直接使用
     * 使用里面的
     *
     * @since 2020年2月10日
     */
    private Point2(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * 工厂方法
     *
     * @param x 横坐标
     * @param y 纵坐标
     * @return (x, y)
     * @since 2020年2月10日
     */
    public static Point2 create(double x, double y) {
        return new Point2(x, y);
    }

    /**
     * 复制构造
     * 应使用 copy 更简单
     *
     * @param p 要被复制的点
     * @return 复制后的点
     * @since 2020年2月10日
     */
    @Deprecated
    public static Point2 copyOf(final Point2 p) {
        return new Point2(p.x, p.y);
    }

    /**
     * 原点
     *
     * @return 原点
     * @since 2020年2月10日
     */
    public static Point2 origin() {
        return create(0, 0);
    }

    public Vector3 toVector3() {
        return this.toPoint3().toVector3();
    }

    /**
     * x 轴上的点
     *
     * @param y 纵坐标
     * @return x 轴上的点
     * @since 2020年2月10日
     */
    public static Point2 createPointOnX(double y) {
        return create(0, y);
    }

    /**
     * y 轴上的点
     *
     * @param x 横坐标
     * @return y 轴上的点
     * @since 2020年2月10日
     */
    public static Point2 createPointOnY(double x) {
        return create(x, 0);
    }

    /**
     * @return toString
     * @since 2020年2月11日
     */
    @Override
    public String toString() {
        return "[" + x + ", " + y + "]";
    }

    /**
     * 转为字符串 x \t y 形式
     * 容易导入其他程序
     *
     * @return x + "/t" + y
     * @since 2020年2月11日
     */
    public String toStringForm() {
        return x + "\t" + y;
    }

    /**
     * 控制台打印 toStringForm()
     *
     * @since 2020年2月11日
     */
    public void printStringForm() {
        System.out.println(toStringForm());
    }

    /**
     * 利用 x 比较 Point2 大小
     *
     * @param p1 点1
     * @param p2 点2
     * @return Double.compare(p1.x, p2.x)
     */
    public static int compareInX(Point2 p1, Point2 p2) {
        return Double.compare(p1.x, p2.x);
    }

    /*----------------坐标变化------------------*/

    /**
     * 点 p 的变换
     * 原来的点 (x, y)
     * 变换后 (xFunctionFromXY(x, y), yFunctionFromXY(x, y))
     *
     * @param p               原来的点
     * @param xFunctionFromXY new x = xFunctionFromXY(old x, old y)
     * @param yFunctionFromXY new y = yFunctionFromXY(old x, old y)
     * @return 变换后的点
     */
    public static Point2 convert(final Point2 p,
                                 BiFunction<Double, Double, Double> xFunctionFromXY,
                                 BiFunction<Double, Double, Double> yFunctionFromXY) {
        return create(
                xFunctionFromXY.apply(p.x, p.y),
                yFunctionFromXY.apply(p.x, p.y)
        );
    }

    /**
     * @param ps              原来的点
     * @param xFunctionFromXY new x = xFunctionFromXY(old x, old y)
     * @param yFunctionFromXY new y = yFunctionFromXY(old x, old y)
     * @return 变换后的点
     * @see Point2#create(double, double)
     */
    public static List<Point2> convert(final List<Point2> ps,
                                       BiFunction<Double, Double, Double> xFunctionFromXY,
                                       BiFunction<Double, Double, Double> yFunctionFromXY) {
        return ps.stream()
                .map(p -> convert(p, xFunctionFromXY, yFunctionFromXY))
                .collect(Collectors.toList());
    }
}
