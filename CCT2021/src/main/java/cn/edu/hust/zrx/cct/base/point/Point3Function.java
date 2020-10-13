package cn.edu.hust.zrx.cct.base.point;

import cn.edu.hust.zrx.cct.advanced.MathFunction;
import cn.edu.hust.zrx.cct.base.BaseUtils;
import cn.edu.hust.zrx.cct.base.line.Line2;
import cn.edu.hust.zrx.cct.base.vector.Vector3Function;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public interface Point3Function extends Function<Double, Point3> {
    @Override
    Point3 apply(Double s);

    static Point3Function create(MathFunction xf, MathFunction yf, MathFunction zf) {
        return s -> Point3.create(xf.apply(s), yf.apply(s), zf.apply(s));
    }

    default Point3Function add(Vector3Function v3f) {
        return s -> apply(s).copy().moveSelf(v3f.apply(s));
    }

    default Vector3Function convertToVector3Function() {
        return s -> apply(s).toVector3();
    }

    default List<Point3> disperse(double startIn, double endIn, int number) {
        return BaseUtils.Python.linspaceStream(startIn, endIn, number)
                .mapToObj(this::apply).collect(Collectors.toList());
    }

    default Vector3Function differential(double delta) {
        return s -> apply(s).to(apply(s + delta)).dot(1 / delta);
    }

    default Point3Function enlarge(double times){
        return s->apply(s).enlarge(times);
    }
}
