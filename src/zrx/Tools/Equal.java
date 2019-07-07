package zrx.Tools;

import zrx.base.Constants;

/**
 * 比较两个浮点数是否相等
 */

public class Equal {
    public static final boolean isEqual(double a, double b) {
        if (Math.abs(a - b) < Constants.DX)
            return true;
        else
            return false;
    }

    public static void main(String[] args) {
        System.out.println(isEqual(0, 0));
        System.out.println(isEqual(0, 1));
        System.out.println(isEqual(1, 1));
        //true
        //false
        //true
    }
}
