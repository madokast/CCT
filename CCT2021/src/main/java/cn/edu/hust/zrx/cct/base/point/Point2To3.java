package cn.edu.hust.zrx.cct.base.point;

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

    public static Point2To3 getXY0ToXYZPoint2To3() {
        return new XY0ToXYZPoint2To3();
    }

    public static class XY0ToXYZPoint2To3 implements Point2To3 {
        @Override
        public Point3 convert(Point2 p) {
            return Point3.create(p.x, p.y, 0);
        }
    }

    public static class SimpleToroidalCoordinateSystemPoint2To3 implements Point2To3 {
        private final double bigR;
        private final double smallR;

        public SimpleToroidalCoordinateSystemPoint2To3(double bigR, double smallR) {
            this.bigR = bigR;
            this.smallR = smallR;
        }

        @Override
        public Point3 convert(Point2 p) {
            return Point3.create(
                    (bigR + smallR * Math.cos(p.x)) * Math.cos(p.y),
                    (bigR + smallR * Math.cos(p.x)) * Math.sin(p.y),
                    smallR * Math.sin(p.x)
            );
        }
    }

    @Override
    default Point3 apply(Point2 point2) {
        return convert(point2);
    }
}
