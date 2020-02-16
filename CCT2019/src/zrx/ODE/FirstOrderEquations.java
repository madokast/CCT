package zrx.ODE;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import zrx.base.point.Vector3d;

public abstract class FirstOrderEquations implements FirstOrderDifferentialEquations {
    public FirstOrderEquations(){}

    abstract public double getH(double s);

    abstract public double getK(double s);

    abstract public double getDP();

    /**
     * 仅仅用于子类的测试
     * @param s 位置
     * @return 点
     */
    public Vector3d pointAtTrajectoryOfS(double s){ return null; }

    public void setDp(double dp){throw new UnsupportedOperationException();}

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

        System.out.println("s = " + s);

        final double h = getH(s);
        final double k = getK(s);

        yDot[0] = y[1];
        yDot[1] = getDP() * h - (h * h + k) * y[0];

        yDot[2] = y[3];
        yDot[3] = k * y[2];
    }
}
