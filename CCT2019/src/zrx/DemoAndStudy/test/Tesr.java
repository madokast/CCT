package zrx.DemoAndStudy.test;

import zrx.Tools.AngleToRadian;

import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Tesr {
    public static void main(String[] args) {
//        test1();
//        test2();

        test3();
    }

    private static void test3() {
        double[] sList = {0,2,4,6,8,10};
        double ds = 2.0;
        double[] hList = {9,80,7,60,5,40};

        for (double i = 0; i < 10; i+=0.1) {
            int si = (int)(i/ds);
            System.out.println(i+" "+hList[si]);
        }

    }

    private static void test2() {
        List<Double> doubles = new ArrayList<>();
        doubles.add(1.1);
        doubles.add(1.2);
        doubles.add(1.3);

        final double max = doubles.stream().mapToDouble(Double::valueOf).max().getAsDouble();
        System.out.println(max);
    }

    private static void test1() {
        final double v1 = Math.atan2(1, 0);
        System.out.println(AngleToRadian.reverse(v1));

//        final double v2 = Math.atan2(0.0003865067963522151, -0.0009229051020618051);
        final double v2 = Math.atan2(0.0003075691996695662, -0.0009522223247022399);
        System.out.println(AngleToRadian.reverse(v2));

        System.out.println(AngleToRadian.reverse(v2)-AngleToRadian.reverse(v1));
    }


}
