package zrx.Tools;

/**
 * 弧度角度转换
 */

public class AngleToRadian {
    public static double to(double angle){
        return angle*Math.PI/180.0;
    }

    public static double reverse(double radian){
        return radian/Math.PI*180.0;
    }

    public static void main(String[] args) {
        System.out.println(to(180.0));
        System.out.println("to(90.0) = " + to(90.0));

        System.out.println(reverse(Math.PI));
        System.out.println(reverse(Math.PI/2.0));
    }
}
