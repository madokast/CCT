package zrx.DemoAndStudy.test;

import zrx.Tools.AngleToRadian;
import zrx.base.Constants;

import java.util.Map;

public class 计算0808 {
    public static void main(String[] args) {
        one();
        two();
        three();


    }

    private static void three() {
        double I1 = -6516.0;
        System.out.println(Constants.Miu0*I1/2.0/(Constants.ARC_LENGTH_OF_67p5_R1M)*241.0/Math.tan(AngleToRadian.to(30))/0.0535);
        System.out.println("------------------");
    }

    private static void two() {
        double R = Math.sqrt(1.0+0.0535*0.0535);
        double R2 = Math.sqrt(1.0+0.0635*0.0635);
        System.out.println(R);
        System.out.println(R2);
        System.out.println("--------------");
    }

    private static void one() {
        double a = 1.0/Math.tan(AngleToRadian.to(32.5)) * 235 * 7374;

        double b = a *Math.tan(AngleToRadian.to(30.0))/241.0;

        System.out.println(b);
        System.out.println("-------------------");
    }
}
