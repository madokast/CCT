package cn.edu.hust.zrx.cct.base.line;

import cn.edu.hust.zrx.cct.base.point.Point2;
import cn.edu.hust.zrx.cct.base.vector.Vector2;

/**
 * Description
 * 直线段
 * 起点
 * 起点、方向和长度三个个自由度
 * 不应该直接访问这个类
 * ------------------------------
 * 2020年2月10日 新增方法
 * 构造器
 * public StraightLine(double length, Vector2 direct, Point2 startingPoint)
 * 线长度
 * double getLength();
 * 返回S位置的点
 * Point2 pointAt(double s);
 * s位置处，曲线的方向
 * Vector2 directAt(double s);
 * ---------------------------------------------------
 * 2020年2月11日 新增方法
 * public String toString()
 * <p>
 * Data
 * 21:50
 *
 * @author zrx
 * @version 1.0
 * @since 2020年2月10日
 */

public class StraightLine implements Line2 {

    private double length;
    private Vector2 direct;
    private Point2 startingPoint;

    /**
     * 直线段的构造器
     *
     * @param length        长度
     * @param direct        方向
     * @param startingPoint 起点
     * @since 2020年2月10日
     */
    protected StraightLine(double length, Vector2 direct, Point2 startingPoint) {
        this.length = length;
        this.direct = direct;
        this.startingPoint = startingPoint;
    }

    /**
     * length
     *
     * @return length
     * @since 2020年2月10日
     */
    @Override
    public double getLength() {
        return length;
    }

    /**
     * point at s
     *
     * @param s 位置
     * @return point at s
     * @since 2020年2月10日
     */
    @Override
    public Point2 pointAt(double s) {
        return startingPoint.copy().
                moveSelf(direct.copy().changeLengthSelf(s));
    }

    /**
     * direct at s
     *
     * @param s 位置
     * @return direct at s
     * @since 2020年2月10日
     */
    @Override
    public Vector2 directAt(double s) {
        return direct;
    }

    /**
     *
     * @return toString
     * @since 2020年2月11日
     */
    @Override
    public String toString() {
        return "直线段" + describe();
    }
}
