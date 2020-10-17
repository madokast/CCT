package cn.edu.hust.zrx.cct.base.point;

/**
 * Description
 * XY0ToXYZPoint2To3
 * <p>
 * Data
 * 21:01
 *
 * @author zrx
 * @version 1.0
 */
public class XY0ToXYZPoint2To3 implements Point2To3 {
    @Override
    public Point3 convert(Point2 p) {
        return Point3.create(p.x, p.y, 0);
    }
}
