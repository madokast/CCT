package cn.edu.hust.zrx.cct.base.line;

import cn.edu.hust.zrx.cct.base.BaseUtils;
import cn.edu.hust.zrx.cct.base.point.Point2;
import cn.edu.hust.zrx.cct.base.vector.Vector2;

/**
 * Description
 * Circle
 * 二维圆
 * <p>
 * Data
 * 11:58
 *
 * @author zrx
 * @version 1.0
 */

public class Circle {

    private final Point2 center;

    private final double r;

    public static Circle create(Point2 center, double r) {
        BaseUtils.Equal.requireTrue(r > 0, "半径应大于0");
        return new Circle(center, r);
    }

    private Circle(Point2 center, double r) {
        this.center = center;
        this.r = r;
    }

    /**
     * 从圆心 center 到 point2OutOfCenter 引出一条射线
     * 这个射线和 this 圆的交点
     *
     * @param point2OutOfCenter 圆心外一点
     * @return 射线和 this 圆的交点
     */
    public Point2 intersectionPoint(Point2 point2OutOfCenter) {
        Vector2 direct = Vector2.from(center).to(point2OutOfCenter);

        BaseUtils.Equal.requireTrue(direct.length() > 1e-6, "point2OutOfCenter 必须是圆心外一点");

        return center.copy().moveSelf(direct.changeLengthSelf(r));
    }

    /**
     * 点 p 到圆的距离
     * 即点 p 到圆心的距离 - r
     * 负数表示点 p 在园内
     * 整数表述点 p 在园外
     *
     * @param p 任意一点
     * @return 距离
     */
    public double distance(Point2 p) {
        double distance = Point2.distance(p, center);
        return distance - r;
    }
}
