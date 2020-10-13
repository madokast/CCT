package cn.edu.hust.zrx.cct.base.point;

import cn.edu.hust.zrx.cct.base.vector.Vector3;
import cn.edu.hust.zrx.cct.base.vector.Vector3Function;
import org.apache.commons.math3.util.FastMath;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@FunctionalInterface
public interface Point2To3 extends Function<Point2, Point3> {
    Point3 convert(Point2 p);

    default List<Point3> convert(List<Point2> point2s) {
        final List<Point3> point3s = new ArrayList<>(point2s.size());
        point2s.forEach(point2 -> point3s.add(convert(point2)));

        return point3s;
    }

    default Point3Function convert(Point2Function p2f) {
        return x -> this.convert(p2f.apply(x));
    }

    public static Point2To3 getXY0ToXYZPoint2To3() {
        return new XY0ToXYZPoint2To3();
    }

    public static class XY0ToXYZPoint2To3 implements Point2To3 {
        @Override
        public Point3 convert(Point2 p) {
            return Point3.create(p.x, p.y, 0);
        }
    }

    /**
     * 重要!!!!
     * 简单圆环坐标系
     */
    public static class SimpleToroidalCoordinateSystemPoint2To3 implements Point2To3 {
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
        public Vector3 mainNormalDirection(Point2 p) {
            double theta = p.x;
            double phi = p.y;

            // 找到 phi 对应的中轴线的位置
            Point3 center = Point3.create(bigR * Math.cos(phi), bigR * Math.sin(phi), 0);

            // p 的点
            Point3 facePoint3 = convert(p);

            return Vector3.from(center).to(facePoint3).normalSelf();
        }

        public Vector3Function mainNormalDirection(Point2Function p2f) {
            return s -> mainNormalDirection(p2f.apply(s));
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

    /**
     * 双极点圆环坐标系 博士论文，陈鹤鸣论文的基石
     * 2020年10月12日
     */
    public static class BipolarToroidalCoordinateSystemPoint2To3 implements Point2To3 {

        private final double a;

        private final double η;

        public BipolarToroidalCoordinateSystemPoint2To3(double a, double η) {
            this.a = a;
            this.η = η;
        }

        public static BipolarToroidalCoordinateSystemPoint2To3 create(double a, double η) {
            return new BipolarToroidalCoordinateSystemPoint2To3(a, η);
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
    }

    @Override
    default Point3 apply(Point2 point2) {
        return convert(point2);
    }
}
