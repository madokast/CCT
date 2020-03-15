package cn.edu.hust.zrx.cct.base.particle;

import cn.edu.hust.zrx.cct.base.BaseUtils;
import cn.edu.hust.zrx.cct.base.point.Point2;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Description
 * 相空间坐标系
 * <p>
 * Data
 * 16:38
 *
 * @author zrx
 * @version 1.0
 */

public class PhaseSpaceParticle {
    private double x;
    private double xp;
    private double y;
    private double yp;
    private double z;
    private double delta;

    // 低维化

    /**
     * @return 投影到 x xp 平面
     */
    public Point2 projectionToXXpPlane() {
        return Point2.create(x, xp);
    }

    /**
     * @return 投影到 y yp 平面
     */
    public Point2 projectionToYYpPlane() {
        return Point2.create(y, yp);
    }

    // 构造器 getter 等

    public PhaseSpaceParticle(double x, double xp, double y, double yp, double z, double delta) {
        this.x = x;
        this.xp = xp;
        this.y = y;
        this.yp = yp;
        this.z = z;
        this.delta = delta;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getXp() {
        return xp;
    }

    public void setXp(double xp) {
        this.xp = xp;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getYp() {
        return yp;
    }

    public void setYp(double yp) {
        this.yp = yp;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public double getDelta() {
        return delta;
    }

    public void setDelta(double delta) {
        this.delta = delta;
    }

    @Override
    public String toString() {
        return "PhaseSpaceCoordinateSystem6d{" +
                "x=" + x +
                ", xp=" + xp +
                ", y=" + y +
                ", yp=" + yp +
                ", z=" + z +
                ", delta=" + delta +
                '}';
    }
}
