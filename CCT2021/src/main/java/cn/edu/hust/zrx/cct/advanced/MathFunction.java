package cn.edu.hust.zrx.cct.advanced;

import cn.edu.hust.zrx.cct.base.point.Point2;
import cn.edu.hust.zrx.cct.base.point.Point2Function;
import cn.edu.hust.zrx.cct.base.vector.Vector2;

import java.util.function.Function;

/**
 * Description
 * 数学函数
 * <p>
 * Data
 * 9:33
 *
 * @author zrx
 * @version 1.0
 */

public interface MathFunction extends Function<Double, Double> {

    @Override
    Double apply(Double aDouble);

    default Function<Double, Double> toDoubleDoubleFunction() {
        return this;
    }

    /**
     * 获得关于 x 轴对称的函数
     *
     * @return 关于 x 轴对称的函数
     */
    default MathFunction xAxisSymmetry() {
        return x -> -apply(x);
    }

    default MathFunction move(final Vector2 v) {
        return x -> apply(x - v.x) + v.y;
    }

    /**
     * 获得关于 y 轴对称的函数
     *
     * @return 关于 y 轴对称的函数
     */
    default MathFunction yAxisSymmetry() {
        return x -> apply(-x);
    }

    /**
     * 关于 x = x0 这条线对称的函数
     *
     * @param x0 x0
     * @return 关于 x = x0 这条线对称的函数
     */
    default MathFunction yAxisSymmetry(double x0) {
        return this.yAxisSymmetry().move(Vector2.yDirect(2 * x0));
    }

    static Function<Double, Double> add(Function<Double, Double> fun1, Function<Double, Double> fun2) {
        return x -> fun1.apply(x) + fun2.apply(x);
    }

    /**
     * 微分
     *
     * @return 微分
     */
    default MathFunction differential(final double delta) {
        return x -> (apply(x + delta) - apply(x)) / delta;
    }

    /**
     * 多项式函数
     * 如 y = 10 * x^4 + 2 * x^2 - x + 100
     * 参数数组，数组下标 i 即 对比 i 阶系数
     *
     * @param coefficients 系数
     * @return 函数
     */
    public static Function<Double, Double> createPolynomialFunction(double[] coefficients) {
        return x -> {
            double y = 0;
            y += coefficients[0];
            for (int i = 1; i < coefficients.length; i++) {
                y += coefficients[i] * Math.pow(x, i);
            }
            return y;
        };
    }


    /**
     * COSY ENGE function
     * F(z) = 1 / (1+exp(a1+a2(z/D)^1+...+a6(z/D)^5))
     *
     * @param D  is the full aperture
     * @param a1 a1
     * @return F(z)
     */
    public static Function<Double, Double> createEngeFuntion(double D, double a1, double a2,
                                                             double a3, double a4, double a5, double a6) {
        return z -> 1.0 / (1 + Math.exp(
                a1
                        + a2 * Math.pow(z / D, 1)
                        + a3 * Math.pow(z / D, 2)
                        + a4 * Math.pow(z / D, 3)
                        + a5 * Math.pow(z / D, 4)
                        + a6 * Math.pow(z / D, 5)
        ));

    }


    static MathFunction identity() {
        return x -> x;
    }


    default Point2Function convertToPoint2Function() {
        return x -> Point2.create(x, apply(x));
    }
}
