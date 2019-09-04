package zrx.Tools;

/**
 * 方差
 */

public class Variance {
    public static double variance(double... doubles) {
        double temp = 0.0;
        double avg = Avg.avg(doubles);
        for (double v : doubles) {
            temp += Math.pow(v - avg, 2);
        }

        return temp / doubles.length;
    }

    public static void main(String[] args) {
        System.out.println(variance(1, 2, 3, 4, 5));
    }
}
