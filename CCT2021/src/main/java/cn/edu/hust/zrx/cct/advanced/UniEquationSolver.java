package cn.edu.hust.zrx.cct.advanced;

import cn.edu.hust.zrx.cct.Logger;
import cn.edu.hust.zrx.cct.base.point.Point2;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.solvers.BisectionSolver;
import org.apache.commons.math3.analysis.solvers.UnivariateSolver;

import java.util.function.Function;

/**
 * Description
 * 一元方程求解器
 * <p>
 * Data
 * 22:34
 *
 * @author zrx
 * @version 1.0
 */

public class UniEquationSolver {
    /**
     * 二分法找函数的零点，给定区间、最大迭代次数
     *
     * @param function 函数 y = f(x)
     * @param interval 区间
     * @param maxEval  最大迭代次数
     * @return 零点
     */
    public static double findZeroPoint(Function<Double, Double> function, Point2 interval, int maxEval) {
        try {
            UnivariateFunction univariateFunction = function::apply;
            UnivariateSolver solver = new BisectionSolver();
            return solver.solve(maxEval, univariateFunction, interval.x, interval.y);
        } catch (Exception e) {
            Logger.getLogger().info("方程求解失败");
            throw e;
        }
    }
}
