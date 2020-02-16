package zrx.Tools;

/**
 * 均值
 */

public class Avg {
    public static double avg(double...doubles){
        return Sum.get(doubles)/doubles.length;
    }

    public static void main(String[] args) {
        System.out.println("avg(1,2,3,4,5) = " + avg(1, 2, 3, 4, 5));
    }
}
