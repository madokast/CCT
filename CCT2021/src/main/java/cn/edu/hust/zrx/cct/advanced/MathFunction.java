package cn.edu.hust.zrx.cct.advanced;

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

public class MathFunction {

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

}
