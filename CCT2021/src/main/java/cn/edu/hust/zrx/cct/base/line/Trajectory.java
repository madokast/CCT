package cn.edu.hust.zrx.cct.base.line;

import cn.edu.hust.zrx.cct.Logger;
import cn.edu.hust.zrx.cct.base.BaseUtils;
import cn.edu.hust.zrx.cct.base.point.Point2;
import cn.edu.hust.zrx.cct.base.vector.Vector2;
import com.google.common.primitives.Ints;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Description
 * 理想轨迹
 * 由直线+圆弧组成
 * -------------------------
 * 2020年2月10日 新增方法
 * 轨迹后接直线段
 * public Trajectory addStraitLine(double length)
 * 轨迹后接弧线段
 * public Trajectory addArcLine(double radius, boolean clockwise, double angle)
 * 构造器
 * protected Trajectory(Line2 line)
 * 线长度
 * double getLength();
 * 返回S位置的点
 * Point2 pointAt(double s);
 * s位置处，曲线的方向
 * Vector2 directAt(double s);
 * ---------------------------------------------------
 * 2020年2月11日 新增方法
 * public String toString()
 *
 * <p>
 * Data
 * 21:47
 *
 * @author zrx
 * @version 1.0
 */

public class Trajectory implements Line2 {
    // Trajectory 由 Line2 组成
    private List<Line2> trajectoryList = new ArrayList<>();
    // 总长度
    private double length;

    /**
     * 构造器 不应该直接访问
     *
     * @param line 第一条线
     */
    protected Trajectory(Line2 line) {
        trajectoryList.add(line);
        length = line.getLength();
    }

    /**
     * 增加直线
     * 即按照上一条线延长
     *
     * @param length 直线长度
     * @return 延长后的轨迹
     */
    public Trajectory addStraitLine(double length) {
        Line2 lastLine = trajectoryList.get(trajectoryList.size() - 1);
        Point2 startPoint = lastLine.pointAtEnd();
        Vector2 startDirect = lastLine.directAtEnd();
        StraightLine straightLine = new StraightLine(length, startDirect, startPoint);

        trajectoryList.add(straightLine);
        this.length += straightLine.getLength();
        return this;
    }

    /**
     * 增加弧线
     * 即按照上一条线的段增加
     *
     * @param radius    半径
     * @param clockwise 旋转方向
     * @param angle     角度
     * @return 延长后的轨迹
     */
    public Trajectory addArcLine(double radius, boolean clockwise, double angle) {
        Line2 lastLine = trajectoryList.get(trajectoryList.size() - 1);
        Point2 startPoint = lastLine.pointAtEnd();
        Vector2 startDirect = lastLine.directAtEnd();
        ArcLine arcLine = new ArcLine(startPoint, startDirect, radius, clockwise, angle);

        trajectoryList.add(arcLine);
        this.length += arcLine.getLength();
        return this;
    }

    /**
     * 长度
     *
     * @return 长度
     * @since 2020年2月10日
     */
    @Override
    public double getLength() {
        return length;
    }

    /**
     * 返回S位置的点
     *
     * @param s 位置
     * @return s位置的点
     * @since 2020年2月10日
     */
    @Override
    public Point2 pointAt(double s) {
        for (Line2 line : trajectoryList) {
            if (s <= line.getLength()) {
                return line.pointAt(s);
            } else {
                s -= line.getLength();
            }
        }

        Logger.getLogger().error("Trajectory::pointAt");
        return trajectoryList.get(trajectoryList.size() - 1).pointAt(s);
    }

    /**
     * s位置处，曲线的方向
     *
     * @param s 位置
     * @return s位置出曲线方向
     * @since 2020年2月10日
     */
    @Override
    public Vector2 directAt(double s) {
        for (Line2 line : trajectoryList) {
            if (s <= line.getLength()) {
                return line.directAt(s).copy();
            } else {
                s -= line.getLength();
            }
        }
        Logger.getLogger().error("Trajectory::directAt");
        return trajectoryList.get(trajectoryList.size() - 1).directAt(s).copy();
    }

    /**
     * trajectoryList.toString()
     *
     * @return trajectoryList.toString()
     * @since 2020年2月10日
     */
    @Override
    public String toString() {
        return this.describe();
    }

    /**
     * @return 详细信息
     */
    public String detailInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append("轨迹信息：")
                .append(this.describe())
                .append("\n").
                append("共").append(this.trajectoryList.size()).append("部分组成");
        Point2 point2 = Point2.origin();

        this.trajectoryList.forEach(
                line2 -> {
                    point2.x += line2.getLength();
                    sb.append(line2.toString()).append(" --当前长度：").append(point2.x).append("\n");
                }
        );

        return sb.toString();
    }

    /**
     * 所有连接点的位置
     * 现在用于画图时的辅助线 2020年2月13日
     *
     * @return List<Double>
     */
    public List<Double> couplingPointLocation() {
        return IntStream.range(0, this.trajectoryList.size() - 1)
                .mapToObj(i ->
                        (Double) (this.trajectoryList.stream().limit(i + 1)
                                .mapToDouble(Line2::getLength)
                                .sum())
                ).collect(Collectors.toList());

    }
}
