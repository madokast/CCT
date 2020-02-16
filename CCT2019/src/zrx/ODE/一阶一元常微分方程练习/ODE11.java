package zrx.ODE.一阶一元常微分方程练习;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.FirstOrderIntegrator;
import org.apache.commons.math3.ode.nonstiff.DormandPrince853Integrator;
import zrx.Tools.PrintArray;


/**
 * 练习一阶一元常微分方程求解。
 * 函数 y^2 = 4t  即 y = 2sqrt(t)
 * 方程化为 y' = 2/y ,y0=0
 *
 * 成了，我要起飞了
 */

public class ODE11 implements FirstOrderDifferentialEquations {
    @Override
    public int getDimension() {
        //一元
        return 1;
    }

    @Override
    public void computeDerivatives(double t, double[] y, double[] yDot) throws MaxCountExceededException, DimensionMismatchException {
        if(t<=4){
            yDot[0] = 2.0 / y[0];
        }
        else {
            yDot[0] = 1.0 / y[0];
        }
    }

    //测试
    public static void main(String[] args) {
        FirstOrderIntegrator dp853 = new DormandPrince853Integrator(1.0e-8, 100.0, 1.0e-10, 1.0e-10);
        FirstOrderDifferentialEquations ode = new ODE11();
        double[] y0 = new double[]{2.0};
        double[] y1 = new double[]{0.0};
        dp853.integrate(ode,1.0,y0,16.0,y1);


        PrintArray.print(y0);
        PrintArray.print(y1);


    }
}
