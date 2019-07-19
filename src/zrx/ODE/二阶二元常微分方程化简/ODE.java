package zrx.ODE.二阶二元常微分方程化简;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;

public class ODE implements FirstOrderDifferentialEquations {
    private double[] hList;
    private double[] kList;
    private double ds;
    private double dp;//dp/p

    public ODE(double[] hList, double[] kList, double ds, double dp) {
        this.hList = hList;
        this.kList = kList;
        this.ds = ds;
        this.dp = dp;
    }

    public double getH(double s) {
        s*=1000.0;
        return hList[(int) (s / ds)];
    }

    public double getK(double s) {
        s*=1000.0;
        return kList[(int) (s / ds)];
    }

    @Override
    public int getDimension() {
        return 4;
    }

    @Override
    public void computeDerivatives(double s, double[] y, double[] yDot) throws MaxCountExceededException, DimensionMismatchException {
//        System.out.println("s = " + s);

        // y0 = x
        // y1 = x'
        // y2 = y
        // y3 = y'

        yDot[0] = y[1];
        yDot[1] = dp * getH(s) - (getH(s) * getH(s) + getK(s)) * y[0];

        yDot[2] = y[3];
        yDot[3] = getK(s) * y[2];
    }

    public static void main(String[] args) {

    }
}
