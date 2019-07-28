package zrx.Tools;

import zrx.base.Constants;
import zrx.base.Vector2d;
import zrx.base.Vector3d;

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

    public static final boolean isEqual(Vector3d a, Vector3d b){
        return isEqual(a.length(),b.length());
    }

    public static final boolean isEqual(Vector2d a, Vector2d b){
        return isEqual(a.length(),b.length());
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
