package cn.edu.hust.zrx.cct.base.line;

import cn.edu.hust.zrx.cct.base.point.Point3;
import cn.edu.hust.zrx.cct.base.vector.Vector3;

/**
 * 三维有向、定长线
 */

public interface Line3 {

    /**
     * @return 线长度
     */
    double getLength();

    /**
     * s 位置处的点
     *
     * @param s 位置
     * @return 所在位置的点
     */
    Point3 pointAt(int s);

    /**
     * s 位置的方向
     *
     * @param s 位置
     * @return s 位置处的方向
     */
    Vector3 directAt(double s);
}
