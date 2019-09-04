package zrx.Tools.python;

import zrx.Tools.PrintArray;

import javax.lang.model.SourceVersion;
import java.lang.reflect.Array;

/**
 * 数组反转
 */

public class ArrayReverse {
    public static <T> T[] reverse(T[] arr){
        final T[] ans = (T[])Array.newInstance(arr.getClass().componentType(), arr.length);
        for (int i = 0; i < ans.length; i++) {
            ans[i] = arr[arr.length-i-1];
        }

        return ans;
    }

    public static <T> T[] reverse(T[] arr,boolean doIt){
        if(doIt)
            return reverse(arr);
        else
            return arr;
    }

    public static void main(String[] args) {
        Integer[] arr = {1,2,3};
        final Integer[] ans = ArrayReverse.reverse(arr);
        PrintArray.print(arr);
        System.out.println("--------");
        PrintArray.print(ans);
    }
}
