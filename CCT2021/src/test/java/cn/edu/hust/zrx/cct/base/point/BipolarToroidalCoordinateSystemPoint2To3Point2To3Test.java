package cn.edu.hust.zrx.cct.base.point;

import cn.edu.hust.zrx.cct.advanced.MathFunction;
import cn.edu.hust.zrx.cct.base.BaseUtils;
import cn.edu.hust.zrx.cct.base.python.Plot2d;
import cn.edu.hust.zrx.cct.base.python.Plot3d;
import org.apache.commons.math3.util.FastMath;

class BipolarToroidalCoordinateSystemPoint2To3Point2To3Test {

    public static void main(String[] args) {
        test2();
        test3();
    }

    private static void test3() {
        double R = 0.95;
        double r = 60 * BaseUtils.Constant.MM;

        BipolarToroidalCoordinateSystemPoint2To3 byBigRAnDSmallR = BipolarToroidalCoordinateSystemPoint2To3.createByBigRAnDSmallR(R, r);

        System.out.println("byBigRAnDSmallR = " + byBigRAnDSmallR);
    }

    static void test2() {
        double R = 0.95;
        double r = 60 * BaseUtils.Constant.MM;

        double a = Math.sqrt(R * R - r * r);
        System.out.println("a = " + a);

        double eta = FastMath.acosh(R / r);
        System.out.println("eta = " + eta);

        double reR = Math.sqrt(
                a * a
                        /
                        (
                                1 -
                                        1 /
                                                Math.pow(Math.cosh(eta), 2)
                        )
        );
        System.out.println("reR = " + reR);
    }

    public static void test1() {
        double R = 0.95;
        double r = 60 * BaseUtils.Constant.MM;

        double a = Math.sqrt(R * R - r - r);
        System.out.println("a = " + a);

        double eta = FastMath.acosh(R / r);
        System.out.println("eta = " + eta);

        Point2To3 point2To3 = new BipolarToroidalCoordinateSystemPoint2To3(a, eta);

        MathFunction path2MathFunction = ksi -> (1 / Math.tan(BaseUtils.Converter.angleToRadian(30))) / (1 * Math.sinh(eta))
                * Math.sin(1 * ksi) + BaseUtils.Converter.angleToRadian(3) * ksi / (2 * Math.PI);

        Point2Function path2Point2Function = Point2Function.create(MathFunction.identity(), path2MathFunction);

        Point3Function path3 = point2To3.convert(path2Point2Function);

        Plot3d.plot(path3, 0, 20 * 2 * Math.PI, BaseUtils.Constant.MM, Plot2d.BLUE_LINE);

        Plot3d.showThread();
    }
}