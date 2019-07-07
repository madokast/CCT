package zrx.Tools;

import java.util.Arrays;

/**
 * 一元二次方程求解
 * 遇到复数抛出错误
 */

public class QuadraticEquationOfOneVariable {
    /**
     *  ax^2 + bx + c = 0
     * @param a 二次项系数
     * @param b 一次项系数
     * @param c 常数项
     * @return 方程解 double[2] 且[0]>=[1]
     */
    public static double[/*TWO ONLY*/] solve(double a, double b, double c) {
        double delta = b * b - 4 * a * c;
        if (delta < 0) {
            /**
             * 不支持复数
             */
            throw new ArithmeticException("Complex number roots unsupported for quadratic equation of one variable");
        }

        double x1 = (-b + Math.sqrt(delta)) / (2 * a);
        double x2 = (-b - Math.sqrt(delta)) / (2 * a);

        return new double[]{x1, x2};

    }

    /**
     * 测试
     * @param args null
     */
    public static void main(String[] args) {
        Arrays.stream(solve(1,-5,6)).forEach(System.out::println);
        Arrays.stream(solve(1,0,1)).forEach(System.out::println);
    }
}
