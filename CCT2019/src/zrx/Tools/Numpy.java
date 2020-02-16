package zrx.Tools;

import com.sun.source.tree.CompilationUnitTree;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * 哈哈哈
 */

public class Numpy {
    /**
     * 来自py numpy库的著名函数 linspace
     * 似乎值得优化
     * @param start 起始点 包含
     * @param end 终止点 包含
     * @param number 划分点数
     * @return 数组 双精度
     */
    public static double[] linspace(double start,double end,int number){
        double[] doubles = new double[number];
        double d = (end-start)/(number-1);
        for (int i = 0; i < number; i++) {
            doubles[i] = start+d*i;
        }

        return doubles;
    }

    public static double[] stepToEnd(double step,double end){
        int number = (int)(end/step);
        double[] ans = new double[number];
        for (int i = 0; i < ans.length; i++) {
            ans[i] = step*i;
        }

        return ans;
    }

    //测试
    public static void main(String[] args) {
//        Arrays.stream(linspace(1,1.3,4)).forEach(System.out::println);
//        Arrays.stream(linspace(0.0,99.0,100)).forEach(System.out::println);

//        final double[] linspace = linspace(-30.0 / 1000, 30.0 / 1000, 61);
//        PrintArray.print(linspace);

        final double[] stepToEnd = stepToEnd(1, 7.9);
        PrintArray.print(stepToEnd);
    }
}
