package zrx.Tools;

import java.util.Arrays;

public class PrintArray {
    public static void print(double[] arr){
        Arrays.stream(arr).forEach(System.out::println);
    }
}
