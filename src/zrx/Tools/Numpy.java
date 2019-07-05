package zrx.Tools;

import java.util.Arrays;

/**
 * 哈哈哈
 */

public class Numpy {
    public static double[] linspace(double start,double end,int number){
        double[] doubles = new double[number];
        double d = (end-start)/(number-1);
        for (int i = 0; i < number; i++) {
            doubles[i] = start+d*i;
        }

        return doubles;
    }

    //测试
    public static void main(String[] args) {
        Arrays.stream(linspace(1,1.3,4)).forEach(System.out::println);
        Arrays.stream(linspace(0.0,99.0,100)).forEach(System.out::println);
    }
}
