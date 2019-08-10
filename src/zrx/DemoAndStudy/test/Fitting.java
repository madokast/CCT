package zrx.DemoAndStudy.test;

import org.apache.commons.math3.fitting.PolynomialCurveFitter;
import org.apache.commons.math3.fitting.WeightedObservedPoint;
import org.apache.commons.math3.fitting.WeightedObservedPoints;
import zrx.Tools.PrintArray;

/**
 * 多项式拟合。
 * 轮子轮子~~
 */


public class Fitting {

    //让我测试一下
    public static void main(String[] args) {
        //测试数据
        double[] xs = {1, 3, 4, 5, 6, 7, 8, 9, 10};
        double[] ys = {10, 5, 4, 2, 1, 1, 2, 3, 4};
        WeightedObservedPoints points = new WeightedObservedPoints();
        for (int i = 0; i < xs.length; i++) {
            points.add(xs[i],ys[i]);
        }

        PolynomialCurveFitter fitter = PolynomialCurveFitter.create(2);
        final double[] results = fitter.fit(points.toList());
        System.out.println("results.length = " + results.length);
        PrintArray.print(results);
        //results.length = 3
        //13.45966386554622
        //-3.6053093964858682
        //0.26757066462948825
        //从0次项开始

        //啊哈哈哈完美。和书本答案一致
    }
}
