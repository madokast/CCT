package zrx.Tools;

import org.apache.commons.math3.fitting.PolynomialCurveFitter;
import org.apache.commons.math3.fitting.WeightedObservedPoints;

import java.util.List;

/**
 * 多项式拟合
 */

public class Fit {
    /**
     * 多项式拟合
     *
     * @param xs     自变量x
     * @param ys     应变量y
     * @param degree 拟合阶数
     * @return double[degree] [i]代表x^i项的系数
     */
    public static double[] fit(double[] xs, double[] ys, int degree) {
        //数据个数
        int num = Math.min(xs.length, ys.length);

        //拟合所用的轮子。感谢apache的math库
        WeightedObservedPoints points = new WeightedObservedPoints();
        PolynomialCurveFitter fitter = PolynomialCurveFitter.create(degree);

        //放入数据
        for (int i = 0; i < num; i++) {
            points.add(xs[i], ys[i]);
        }

        return fitter.fit(points.toList());
    }

    public static double[] fit(List<Double> xs, List<Double> ys, int degree) {
        int num = Math.min(xs.size(), ys.size());

        final double[] xArr = new double[num];
        final double[] yArr = new double[num];
        for (int i = 0; i < xArr.length; i++) {
            xArr[i] = xs.get(i);
            yArr[i] = ys.get(i);
        }

        return fit(xArr, yArr, degree);
    }

    /**
     * 测试
     *
     * @param args null
     */
    public static void main(String[] args) {
        final double[] xs = {0, 1, 2, 3};
        final double[] ys = {1, 2, 3, 4};

        final double[] fit = fit(xs, ys, 2);
        PrintArray.print(fit);
        //1.0
        //0.9999999999999999
        //2.8882734487668653E-17
    }
}
