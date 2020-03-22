package cn.edu.hust.zrx.cct.advanced;

import cn.edu.hust.zrx.cct.Logger;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;

/**
 * 我的代码真的是厉害
 * 实现这个代码就可以计算 一阶传输矩阵 ODE 方法
 */

public abstract class FirstOrderTransportMatrixEquation implements FirstOrderDifferentialEquations {
    public FirstOrderTransportMatrixEquation(){}

    abstract public HKdP getHKdp(double s);

    abstract public boolean loggerDistance();

    @Override
    public int getDimension() {
        return 4;
    }

    @Override
    public void computeDerivatives(double s, double[] y, double[] yDot) throws MaxCountExceededException, DimensionMismatchException {
        // y0 = x
        // y1 = x'
        // y2 = y
        // y3 = y'

        if(loggerDistance())
            Logger.getLogger().info("s = " + s);

        HKdP hKdp = getHKdp(s);

        final double h = hKdp.h;
        final double k = hKdp.k;

        yDot[0] = y[1];
        yDot[1] = hKdp.dp * h - (h * h + k) * y[0];

        yDot[2] = y[3];
        yDot[3] = k * y[2];
    }

    public static class HKdP{
        private double h;
        private double k;
        private double dp;

        private HKdP(double h, double k, double dp) {
            this.h = h;
            this.k = k;
            this.dp = dp;
        }

        public static HKdP valueOf(double h, double k, double dp){
            return new HKdP(h,k,dp);
        }
    }
}
