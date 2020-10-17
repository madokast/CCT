package cn.edu.hust.zrx.cct.base.point;

import cn.edu.hust.zrx.cct.base.vector.Vector3;
import org.apache.commons.math3.util.FastMath;

/**
 * 双极点圆环坐标系 博士论文，陈鹤鸣论文的基石
 * 2020年10月12日
 */
public class BipolarToroidalCoordinateSystemPoint2To3 implements Point2To3 {

    private final double a;

    private final double η;

    private final double bigR;

    private final double smallR;

    public BipolarToroidalCoordinateSystemPoint2To3(double a, double η) {
        this.a = a;
        this.η = η;

        this.bigR = Math.sqrt(
                a * a
                        /
                        (
                                1 -
                                        1 /
                                                Math.pow(Math.cosh(η), 2)
                        )
        );

        this.smallR = bigR / Math.cosh(η);
    }

    public static BipolarToroidalCoordinateSystemPoint2To3 createByBigRAnDSmallR(double bigR, double smallR) {
        double a = Math.sqrt(bigR * bigR - smallR * smallR);

        double eta = FastMath.acosh(bigR / smallR);

        return new BipolarToroidalCoordinateSystemPoint2To3(a, eta);
    }

    public static cn.edu.hust.zrx.cct.base.point.BipolarToroidalCoordinateSystemPoint2To3 createByAAndEta(double a, double η) {
        return new cn.edu.hust.zrx.cct.base.point.BipolarToroidalCoordinateSystemPoint2To3(a, η);
    }

    @Override
    public Vector3 mainNormalDirection(Point2 p) {
        double ksi = p.x;
        double phi = p.y;

        //找到 phi 对应的中轴线的位置
        Point3 center = Point3.create(bigR * Math.cos(phi), bigR * Math.sin(phi), 0);

        // p 的点
        Point3 facePoint3 = convert(p);

        return Vector3.from(center).to(facePoint3).normalSelf();
    }

    /**
     * 双极点圆环坐标系转换
     * ξ 和 φ 点转为 x y z
     *
     * @param p (ξ, φ)
     * @return x y z 点
     */
    @Override
    public Point3 convert(Point2 p) {
        final double ξ = p.x;
        final double φ = p.y;

        final double temp = a / (Math.cosh(η) - Math.cos(ξ));

        return Point3.create(
                temp * Math.sinh(η) * Math.cos(φ),
                temp * Math.sinh(η) * Math.sin(φ),
                temp * Math.sin(ξ)
        );
    }

    @Override
    public String toString() {
        return "BipolarToroidalCoordinateSystemPoint2To3{" +
                "a=" + a +
                ", η=" + η +
                ", bigR=" + bigR +
                ", smallR=" + smallR +
                '}';
    }
}
