package cn.edu.hust.zrx.cct.advanced;

import cn.edu.hust.zrx.cct.base.point.Point2;
import org.apache.commons.math3.fitting.PolynomialCurveFitter;
import org.apache.commons.math3.fitting.WeightedObservedPoints;

import java.util.List;

/**
 * Description
 * 多项式拟合
 * <p>
 * Data
 * 9:44
 *
 * @author zrx
 * @version 1.0
 */

public class PolynomialFitter {
    private int order;

    private PolynomialFitter(){}

    /**
     * 构造器
     * @param order 系数
     */
    private PolynomialFitter(int order) {
        this.order = order;
    }

    public static PolynomialFitter build(int order){
        return new PolynomialFitter(order);
    }

    public double[] fit(List<Point2> point2s){
        WeightedObservedPoints weightedObservedPoints = new WeightedObservedPoints();
        for (Point2 p : point2s) {
            weightedObservedPoints.add(p.x,p.y);
        }

        return PolynomialCurveFitter.create(order).fit(weightedObservedPoints.toList());
    }



    //        //测试数据
    //        double[] xs = {1, 3, 4, 5, 6, 7, 8, 9, 10};
    //        double[] ys = {10, 5, 4, 2, 1, 1, 2, 3, 4};
    //        WeightedObservedPoints points = new WeightedObservedPoints();
    //        for (int i = 0; i < xs.length; i++) {
    //            points.add(xs[i], ys[i]);
    //        }
    //
    //        PolynomialCurveFitter fitter = PolynomialCurveFitter.create(2);
    //        final double[] results = fitter.fit(points.toList());
}
