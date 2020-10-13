package cn.edu.hust.zrx.cct.base.vector;

import cn.edu.hust.zrx.cct.advanced.MathFunction;
import cn.edu.hust.zrx.cct.base.point.Point3Function;

import java.util.function.Function;

/**
 * Description
 * Vector3Function
 * 人可以写出无与伦比的代码
 * <p>
 * Data
 * 22:37
 *
 * @author zrx
 * @version 1.0
 */

public interface Vector3Function extends Function<Double, Vector3> {


    @Override
    Vector3 apply(Double s);

    static Vector3Function cross(Vector3Function v1, Vector3Function v2) {
        return s -> v1.apply(s).cross(v2.apply(s));
    }

    default Vector3Function cross(Vector3Function v) {
        return cross(this, v);
    }

    default Vector3Function normal() {
        return s -> apply(s).normalSelf();
    }

    default Point3Function convertToPoint3Function() {
        return s -> apply(s).toPoint3();
    }

    default Vector3Function reverse() {
        return s -> apply(s).reverse();
    }

    default Vector3Function differential(final double delta) {
        return s -> apply(s + delta).subtract(apply(s)).dot(1 / delta);
    }

    static Vector3Function create(MathFunction xpFun, MathFunction ypFun, MathFunction zpFun) {
        return s -> Vector3.create(
                xpFun.apply(s),
                ypFun.apply(s),
                zpFun.apply(s)
        );
    }

    default Vector3Function dot(double k) {
        return s -> apply(s).dot(k);
    }
}
