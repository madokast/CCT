package zrx.Tools;

import javax.crypto.AEADBadTagException;
import java.lang.reflect.Array;
import java.util.Arrays;

/**
 * 数组合并的究极黑科技
 * 研究源码好多年，终于派上用场了
 */

public class ArrayMerge {
    public static final <T> T[] merge(T[] arr1,T[] arr2){
        int len1 = arr1.length;
        int len2 = arr2.length;
        T[] arr = (T[])Array.newInstance(arr1.getClass().componentType(), len1 + len2);

        for (int i = 0; i < len1; i++) {
            arr[i]=arr1[i];
        }
        for (int i = 0; i < len2; i++) {
            arr[len1+i]=arr2[i];
        }

        return arr;
    }

    public static <T> T[] merge(T[]...arrs){
        T[] ans = arrs[0];
        for (int i = 1; i < arrs.length; i++) {
            ans = merge(ans,arrs[i]);
        }

        return ans;
    }

    public static void main(String[] args) {
        Integer[] arr1 = {1,2,3};
        Integer[] arr2 = {8,9,10};
        Integer[] arr3 = {20,21,22};

        Integer[] arr = merge(arr1,arr2,arr3);

        Arrays.stream(arr).forEach(System.out::println);
    }
}
