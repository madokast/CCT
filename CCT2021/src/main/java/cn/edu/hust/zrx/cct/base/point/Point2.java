package cn.edu.hust.zrx.cct.base.point;

import cn.edu.hust.zrx.cct.base.vector.Vector2;
import cn.edu.hust.zrx.cct.base.vector.Vector3;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.ArrayList;
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

public class Point2 implements Cloneable, Serializable, Comparable<Point2> {
    public double x;
    public double y;

    public static double distance(Point2 p1, Point2 p2) {
        return Vector2.from(p1).to(p2).length();
    }

    /**
     * 两点的中点
     *
     * @param p1 点1
     * @param p2 点2
     * @return 中点
     */
    public static Point2 center(Point2 p1, Point2 p2) {
        return create(
                (p1.x + p2.x) / 2,
                (p1.y + p2.y) / 2
        );
    }

    /**
     * 把点的 y 值相加
     *
     * @param p1 点1
     * @param p2 点2
     * @return (p1.x, p1.y + p2.y)
     */
    public static Point2 addOnY(Point2 p1, Point2 p2) {
        // 不检查 p1.x==p2.x
        return Point2.create(p1.x, p1.y + p2.y);
    }

    public static Point2 addOnY(Point2... point2s) {
        double sum = 0;
        for (Point2 point2 : point2s) {
            sum += point2.y;
        }

        return Point2.create(point2s[0].x, sum);
    }

    public static List<Point2> addOnY(List<Point2> ps1, List<Point2> ps2) {
        // 不检查 两个数组长度是否一致
        List<Point2> ret = new ArrayList<>(ps1.size());

        for (int i = 0; i < ps1.size(); i++) {
            ret.add(
                    addOnY(ps1.get(i), ps2.get(i))
            );
        }

        return ret;
    }

    public static List<Point2> addOnY(List<List<Point2>> pss) {
        List<Point2> ret = pss.get(0);

        for (int i = 1; i < pss.size(); i++) {
            for (int j = 0; j < pss.get(i).size(); j++) {
                Point2 addPoint = pss.get(i).get(j);
                Point2 origin = ret.get(j);

                ret.set(j, addOnY(addPoint, origin));
            }
        }

        return ret;
    }

    /**
     * 均值 中心点
     *
     * @param point2s point2s
     * @return 均值 中心点
     */
    public static Point2 average(List<Point2> point2s) {
        double x = point2s.stream().mapToDouble(Point2::getX).average().orElseGet(() -> {
            throw new RuntimeException("Point2::average");
        });

        double y = point2s.stream().mapToDouble(Point2::getY).average().orElseGet(() -> {
            throw new RuntimeException("Point2::average");
        });

        return create(x, y);
    }

    public static Point2 midPoint(Point2 p1, Point2 p2) {
        return create(
                (p1.x + p2.x) / 2,
                (p1.y + p2.y) / 2
        );
    }

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

    public Point3 toPoint3(double z) {
        return new Point2To3() {
            @Override
            public Point3 convert(Point2 p) {
                return Point3.create(p.x, p.y, z);
            }
        }.apply(this);
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

    public static List<Point2> create(List<Double> xs, List<Double> ys) {
        int xLen = xs.size();
        int yLen = ys.size();

        int len = Math.min(xLen, yLen);

        List<Point2> list = new ArrayList<>(len);

        for (int i = 0; i < len; i++) {
            list.add(create(xs.get(i), ys.get(i)));
        }

        return list;
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

    public static int compareInY(Point2 p1, Point2 p2) {
        return Double.compare(p1.y, p2.y);
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
        if (xFunctionFromXY == null)
            xFunctionFromXY = (x, y) -> x;
        if (yFunctionFromXY == null)
            yFunctionFromXY = (x, y) -> y;


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

    public static List<Point2> convert(final List<Point2> ps,
                                       Function<Double, Double> xFunctionFromX,
                                       Function<Double, Double> yFunctionFromY) {
        final Function<Double, Double> xFun = xFunctionFromX == null ? x -> x : xFunctionFromX;
        final Function<Double, Double> yFun = yFunctionFromY == null ? y -> y : yFunctionFromY;


        return convert(ps, (x, y) -> xFun.apply(x), (x, y) -> yFun.apply(y));
    }

    public static List<Point2> convert(final List<Point2> ps,
                                       double xScale,
                                       double yScale) {
        return convert(ps, x -> x * xScale, y -> y * yScale);
    }

    public static Point2 convert(final Point2 p,
                                 double xScale,
                                 double yScale) {
        return convert(p, (x, y) -> x * xScale, (x, y) -> y * yScale);
    }


    /**
     * 点 thisPoint 关于直线对称的点
     * 直线由 point2AtLine 和 directOfTheLine 确定
     *
     * @param thisPoint       用于对称的点
     * @param point2AtLine    直线上任意一点
     * @param directOfTheLine 直线的方向
     * @return 点 thisPoint 关于直线对称的点
     */
    public static Point2 symmetryByLine(final Point2 thisPoint, final Point2 point2AtLine, final Vector2 directOfTheLine) {
        Vector2 directOfTheLineNormal = directOfTheLine.copy().normalSelf();

        Vector2 sp = Vector2.from(point2AtLine).to(thisPoint);

        double projectLength = sp.dot(directOfTheLineNormal);

        Vector2 diagonal = directOfTheLineNormal.dot(2 * projectLength); // 对角线

        Vector2 spp = diagonal.add(sp.reverseSelf());

        return point2AtLine.copy().moveSelf(spp);
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    /**
     * 以 x 作为比较基准
     *
     * @param o 比较对象
     * @return Double.compare(this.x, o.x)
     */
    @Override
    public int compareTo(@NotNull Point2 o) {
        return Double.compare(this.x, o.x);
    }
}
