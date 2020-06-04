package cn.edu.hust.zrx.cct.base.line;

import cn.edu.hust.zrx.cct.base.BaseUtils;
import cn.edu.hust.zrx.cct.base.point.Point2;
import cn.edu.hust.zrx.cct.base.vector.Vector2;

/**
 * Description
 * 圆弧
 * 借助极坐标的思想来描述圆弧
 * 基础属性： 圆弧的半径 radius、圆弧的圆心 center
 * 起点描述：极坐标 phi 值
 * 弧长：len = radius * totalPhi
 * <p>
 * ----------------------------------------------------
 * <p>
 * 起点、圆心、半径、旋转方向、角度 五个自由度
 * 或者
 * 起点、起点处方向、半径、旋转方向、角度 五个自由度
 * 详见下图（顺时针旋转）
 * *
 * *
 * *
 * *
 * startDirect ↑ *
 * startPoint -> *                * <- center
 * [----------------] = radius
 * <p>
 * ---------------------------------------------------
 * 2020年2月10日 新增方法
 * 构造器
 * 起点、起点方向、半径、顺时针与否、旋转角度
 * protected ArcLine(final Point2 startingPoint,
 * final Vector2 startingDirect,
 * final double radius,
 * final boolean clockwise,
 * final double totalAngle)
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
 * 21:56
 *
 * @author zrx
 * @version 1.0
 * @since 2020年2月10日
 */

public class ArcLine implements Line2 {
    private double startingPhi;
    private Point2 center;
    private double radius;
    private double totalPhi;
    private boolean clockwise;


    /**
     * 单位圆（极坐标）
     * 返回：极坐标(r=1,phi=phi)的点的直角坐标(x,y)
     * 核心方法
     *
     * @param phi 极坐标phi
     * @return 单位圆上的一点
     */
    private Point2 unitCircle(double phi) {
        double x = Math.cos(phi);
        double y = Math.sin(phi);

        return Point2.create(x, y);
    }


    /**
     * 构造器
     *
     * @param startingPoint  起点
     * @param startingDirect 起点方向
     * @param radius         半径
     * @param clockwise      顺时针与否
     * @param totalAngle     总旋转度数
     */
    public ArcLine(final Point2 startingPoint,
                      final Vector2 startingDirect,
                      final double radius,
                      final boolean clockwise,
                      final double totalAngle) {

        this.radius = radius;
        this.clockwise = clockwise;

        if (clockwise) {
            this.center = startingPoint.copy().moveSelf(
                    startingDirect.copy().
                            rotateSelf(BaseUtils.Converter.angleToRadian(-90)).changeLengthSelf(radius)
            );
        } else {
            this.center = startingPoint.copy().moveSelf(
                    startingDirect.copy().
                            rotateSelf(BaseUtils.Converter.angleToRadian(90)).changeLengthSelf(radius)
            );
        }

        this.startingPhi = Vector2.subtract(startingPoint.toVector2(), center.toVector2()).angleToXAxis();

        this.totalPhi = BaseUtils.Converter.angleToRadian(totalAngle);
    }


    /**
     * Length
     *
     * @return Length
     * @since 2020年2月10日
     */
    @Override
    public double getLength() {
        return radius * totalPhi;
    }

    /**
     * pointAt s
     *
     * @param s 位置
     * @return pointAt s
     * @since 2020年2月10日
     */
    @Override
    public Point2 pointAt(double s) {
        double phi = s / radius;
        double currentPhi;
        if (clockwise) {
            currentPhi = startingPhi - phi;
        } else {
            currentPhi = startingPhi + phi;
        }

        Point2 unitCircle = unitCircle(currentPhi);

        return unitCircle.toVector2().changeLengthSelf(radius)
                .toPoint2().moveSelf(center.toVector2());
    }

    /**
     * directAt s
     *
     * @param s 位置
     * @return directAt s
     * @since 2020年2月10日
     */
    @Override
    public Vector2 directAt(double s) {
        double phi = s / radius;
        double currentPhi;
        if (clockwise) {
            currentPhi = startingPhi - phi;
        } else {
            currentPhi = startingPhi + phi;
        }

        Point2 unitCircle = unitCircle(currentPhi);

        if (clockwise) {
            return unitCircle.toVector2().rotateSelf(BaseUtils.Converter.angleToRadian(-90));
        } else {
            return unitCircle.toVector2().rotateSelf(BaseUtils.Converter.angleToRadian(90));
        }
    }

    /**
     * @return toString
     * @since 2020年2月11日
     */
    @Override
    public String toString() {
        return "弧线段" + describe();
    }
}
