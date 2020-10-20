package cn.edu.hust.zrx.cct.base.point;

import cn.edu.hust.zrx.cct.base.vector.Vector3;

/**
 * 重要!!!!
 * 简单圆环坐标系
 */
public class SimpleToroidalCoordinateSystemPoint2To3 implements Point2To3 {
    private final double bigR;
    private final double smallR;

    public SimpleToroidalCoordinateSystemPoint2To3(double bigR, double smallR) {
        this.bigR = bigR;
        this.smallR = smallR;
    }

    public static SimpleToroidalCoordinateSystemPoint2To3 create(double bigR, double smallR) {
        return new SimpleToroidalCoordinateSystemPoint2To3(bigR, smallR);
    }

    /**
     * 点 p (theta, phi) 的主法线方向
     *
     * @param p (theta, phi)
     * @return 主法线方向
     */
    @Override
    public Vector3 mainNormalDirection(Point2 p) {
        double theta = p.x;
        double phi = p.y;

        // 找到 phi 对应的中轴线的位置
        Point3 center = Point3.create(bigR * Math.cos(phi), bigR * Math.sin(phi), 0);

        // p 的点
        Point3 facePoint3 = convert(p);

        return Vector3.from(center).to(facePoint3).normalSelf();
    }

    /**
     * refer Superconducting toroidal combined-function magnet for a compact ion
     * beam cancer therapy gantry
     *
     * @return 二维 theta phi 到三维映射
     */
    @Override
    public Point3 convert(Point2 p) {
        return Point3.create(
                (bigR + smallR * Math.cos(p.x)) * Math.cos(p.y),
                (bigR + smallR * Math.cos(p.x)) * Math.sin(p.y),
                smallR * Math.sin(p.x)
        );
    }
}
