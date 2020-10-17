package cn.edu.hust.zrx.cct.base.point;

import cn.edu.hust.zrx.cct.base.vector.Vector3;
import cn.edu.hust.zrx.cct.base.vector.Vector3Function;

import javax.naming.OperationNotSupportedException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@FunctionalInterface
public interface Point2To3 extends Function<Point2, Point3> {
    Point3 convert(Point2 p);

    default Vector3 mainNormalDirection(Point2 p) {
        throw new RuntimeException(new OperationNotSupportedException());
    }

    default Vector3Function mainNormalDirection(Point2Function p2f) {
        return s -> mainNormalDirection(p2f.apply(s));
    }

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

    @Override
    default Point3 apply(Point2 point2) {
        return convert(point2);
    }
}
