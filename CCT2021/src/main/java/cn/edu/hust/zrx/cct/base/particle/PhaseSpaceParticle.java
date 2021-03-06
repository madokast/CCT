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

    public static PhaseSpaceParticle create(double x, double xp, double y, double yp, double z, double delta) {
        return new PhaseSpaceParticle(x, xp, y, yp, z, delta);
    }

    public static PhaseSpaceParticle origin() {
        return new PhaseSpaceParticle(0,0,0,0,0,0);
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

    public PhaseSpaceParticle copy() {
        return new PhaseSpaceParticle(
                this.x,
                this.xp,
                this.y,
                this.yp,
                this.z,
                this.delta
        );
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

    public String toExcelMapComputer() {
        return String.format("%+e\t%+e\t%+e\t%+e", x, xp, y, yp);
    }


    //    private double x;
    //    private double xp;
    //    private double y;
    //    private double yp;
    //    private double z;
    //    private double delta;

    public static enum VARIABLE {
        X, XP, Y, YP, Z, Dp
    }

    public double get(VARIABLE variable) {
        return switch (variable) {
            case X -> x;
            case XP -> xp;
            case Y -> y;
            case YP -> yp;
            case Z -> z;
            case Dp -> delta;
        };
    }

    public void set(VARIABLE variable, double value) {
        switch (variable) {
            case X -> x = value;
            case XP -> xp = value;
            case Y -> y = value;
            case YP -> yp = value;
            case Z -> z = value;
            case Dp -> delta = value;
        }
    }
}
