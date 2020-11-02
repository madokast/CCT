package cn.edu.hust.zrx.cct.base.magnet;

import cn.edu.hust.zrx.cct.base.point.Point3;
import cn.edu.hust.zrx.cct.base.vector.Vector3;

/**
 * Description
 * FarIgnoreMagnet
 * 可以根据距离忽视的磁铁
 * <p>
 * Data
 * 2020年10月29日
 *
 * @author zrx
 * @version 1.0
 */

public class FarIgnoreMagnet implements MagnetAble {

    private final MagnetAble magnetAble;

    private final Point3 center;

    private final double ignoreDistance;

    private final Vector3 M0 = Vector3.getZero();

    @Override
    public Vector3 magnetAt(Point3 p) {
        double x = Math.abs(p.x - center.x);
        if (x > ignoreDistance) return M0;

        double y = Math.abs(p.y - center.y);
        if (y > ignoreDistance) return M0;


        double z = Math.abs(p.z - center.z);
        if (z > ignoreDistance) return M0;

        if (Math.sqrt(x * x + y * y + z * z) > ignoreDistance) return M0;
        else return magnetAble.magnetAt(p);
    }

    public FarIgnoreMagnet(MagnetAble magnetAble, Point3 center, double ignoreDistance) {
        this.magnetAble = magnetAble;
        this.center = center;
        this.ignoreDistance = ignoreDistance;
    }

    public static FarIgnoreMagnet create(MagnetAble magnetAble, Point3 center, double ignoreDistance) {
        return new FarIgnoreMagnet(magnetAble, center, ignoreDistance);
    }
}
