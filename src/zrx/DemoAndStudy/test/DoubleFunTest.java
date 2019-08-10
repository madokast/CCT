package zrx.DemoAndStudy.test;

import java.util.function.DoubleFunction;

public class DoubleFunTest {
    public static double fun(double x, DoubleFunction<Double> doubleFun){
        return doubleFun.apply(x);
    }

    public static void main(String[] args) {
        double result = fun(fun(10, x->x*x),xx->{
                        if(xx>50)
                            xx-=50;
                        return xx;
                }
                );

        System.out.println("result = " + result);
    }
}
