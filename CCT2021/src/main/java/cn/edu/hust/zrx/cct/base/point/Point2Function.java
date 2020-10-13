package cn.edu.hust.zrx.cct.base.point;

import cn.edu.hust.zrx.cct.advanced.MathFunction;
import cn.edu.hust.zrx.cct.base.BaseUtils;
import cn.edu.hust.zrx.cct.base.vector.Vector3Function;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public interface Point2Function extends Function<Double, Point2> {
    @Override
    Point2 apply(Double s);

    static Point2Function create(MathFunction xf, MathFunction yf) {
        return s -> Point2.create(xf.apply(s), yf.apply(s));
    }

    default List<Point2> disperse(double startIn, double endIn, int number) {
        return BaseUtils.Python.linspaceStream(startIn, endIn, number)
                .mapToObj(this::apply).collect(Collectors.toList());
    }

    default Point3Function convert(Point2To3 point2To3) {
        return s -> point2To3.apply(this.apply(s));
    }
}
