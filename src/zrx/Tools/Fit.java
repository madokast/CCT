package zrx.Tools;

import org.apache.commons.math3.fitting.PolynomialCurveFitter;
import org.apache.commons.math3.fitting.WeightedObservedPoints;
import zrx.base.point.Vector2d;

import java.util.ArrayList;
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

    public static double[] fit(Vector2d[] vs, int degree) {
        final double[] xArr = new double[vs.length];
        final double[] yArr = new double[vs.length];

        for (int i = 0; i < xArr.length; i++) {
            xArr[i] = vs[i].x;
            yArr[i] = vs[i].y;
        }

        return fit(xArr, yArr, degree);
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

    public static double[] fit(List<Vector2d> data, int degree) {
        final double[] xArr = new double[data.size()];
        final double[] yArr = new double[data.size()];
        for (int i = 0; i < xArr.length; i++) {
            xArr[i] = data.get(i).x;
            yArr[i] = data.get(i).y;
        }

        return fit(xArr, yArr, degree);

    }

    /**
     * COSY enge函数拟合
     * 原方程 F(z) = 1 / (1 + exp(a1+a2*(z/D)+...+a6(z/D)^5) )
     * 传入的参数为 z/D 和 Fz
     * 其中z/D 在 [-3, 5]范围
     * Fz 在 [0,1]范围
     *
     * @param data 参数 (z/D,F)
     * @return arr数组 a0 到 a5 注意和原方程相差1 ，原方程ax 为 返回值的 a[x-1]
     */
    public static double[] fitEngeFunction(Vector2d[] data) {
        //筛选合法数据
        //并且 变换方程
        //ln(1/F-1) = a1 + a2(z/D) + ...
        final ArrayList<Vector2d> vector2dList = new ArrayList<>();
        for (int i = 0; i < data.length; i++) {

            if(data[i].y>0.0&&data[i].y<1.0){
                vector2dList.add(
                        Vector2d.convict(data[i], x -> x, y -> Math.log(1 / y - 1))
                );
            }

        }

//        PrintArray.print(vector2dList.toArray());

        return fit(vector2dList, 5);
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
