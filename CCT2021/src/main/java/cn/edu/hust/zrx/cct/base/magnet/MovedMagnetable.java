package cn.edu.hust.zrx.cct.base.magnet;

import cn.edu.hust.zrx.cct.base.point.Point3;
import cn.edu.hust.zrx.cct.base.vector.Vector3;

/**
 * Description
 * MovedMagnetable
 * 果然我写过这么神奇的类~
 * 2020年8月25日
 * <p>
 * Data
 * 22:33
 *
 * @author zrx
 * @version 1.0
 */

public class MovedMagnetable implements MagnetAble {

    private final Vector3 pointMoving;

    private final MagnetAble magnetAble;

    private MovedMagnetable(Vector3 direct, double length, MagnetAble magnetAble) {
        this.magnetAble = magnetAble;
        this.pointMoving = direct.copy().changeLengthSelf(-length);
    }

    public static MovedMagnetable create(MagnetAble magnetAble, Vector3 direct, double length) {
        return new MovedMagnetable(direct, length, magnetAble);
    }

    public static MovedMagnetable create(MagnetAble magnetAble, Vector3 movingVector) {
        return new MovedMagnetable(movingVector, movingVector.length(), magnetAble);
    }

    @Override
    public Vector3 magnetAt(Point3 p) {
        return magnetAble.magnetAt(p.copy().moveSelf(pointMoving));
    }
}
