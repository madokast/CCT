package cn.edu.hust.zrx.cct.base.opticsMagnet;

import cn.edu.hust.zrx.cct.base.cct.MagnetAble;
import cn.edu.hust.zrx.cct.base.point.Point3;
import cn.edu.hust.zrx.cct.base.vector.Vector3;

/**
 * Description
 * NoMagnet
 * <p>
 * Data
 * 19:50
 *
 * @author zrx
 * @version 1.0
 */

public class NoMagnet implements MagnetAble {
    @Override
    public Vector3 magnetAt(Point3 p) {
        return Vector3.getZero();
    }

    public static NoMagnet create(){
        return new NoMagnet();
    }
}
