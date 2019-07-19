package zrx.Tools;

import java.util.Arrays;
import java.util.Objects;

public class PrintArray {
    public static void print(double[] arr){
        Arrays.stream(arr).forEach(System.out::println);
    }

    public static void print(Object[] arr){
        Arrays.stream(arr).forEach(System.out::println);
    }
}
