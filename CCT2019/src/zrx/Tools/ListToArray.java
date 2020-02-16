package zrx.Tools;

import java.util.List;

public class ListToArray {
    public static double[] doubleListToArray(List<Double> doubles){
        final double[] result = new double[doubles.size()];
        for (int i = 0; i < result.length; i++) {
            result[i] = doubles.get(i);
        }

        return result;
    }
}
