package zrx.base.line2d;

import zrx.base.point.Vector2d;

/**
 * 线抽象类
 * 包括直线和弧线等
 * <p>
 * 注意：这个线，属于有方向的线
 * <p>
 * 2019年9月2日
 */

public interface Line {
    /**
     * 线长度
     *
     * @return 长度
     */
    double getLength();

    /**
     * 位于s处的点，它右手边d处的点
     * <p>
     * 1    5    10     15
     * -----------------@------>
     * |    2
     * *    4
     * <p>
     * 如上图，一条直线，s=15，d=4 ,即点 @ 右手边4 距离处的点 *
     *
     * @param s s位置
     * @param d d距离远处
     * @return 点
     */
    Vector2d rightHandSidePoint(double s, double d);

    /**
     * 返回S位置的点
     *
     * @param s 位置
     * @return s位置的点
     */
    default Vector2d pointAt(double s) {
        return rightHandSidePoint(s, 0);
    }

    /**
     * 说明见rightHandSidePoint 方法
     *
     * @param s s位置
     * @param d d距离远处
     * @return 点
     */
    default Vector2d leftHandSidePoint(double s, double d) {
        return rightHandSidePoint(s, -d);
    }
}
