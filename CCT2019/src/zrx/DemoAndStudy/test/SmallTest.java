package zrx.DemoAndStudy.test;

import zrx.Tools.PrintArray;
import zrx.base.point.Vector2d;

import java.util.ArrayList;
import java.util.List;

public class SmallTest {
    public static void main(String[] args) {
//        rayForPolarAngleTest();
        arrayLenth();
    }

    private static void arrayLenth() {
        List<Integer> list = new ArrayList<>(10);
        list.add(2);
        list.add(3);
        list.add(4);
        list.add(5);

        final Object[] toArray = list.toArray();
        System.out.println("toArray.length = " + toArray.length);

        PrintArray.print(toArray);
    }

    private static void rayForPolarAngleTest() {
        System.out.println(Vector2d.rayForPolarAngle(0.0));
        System.out.println(Vector2d.rayForPolarAngle(45.0));
        System.out.println(Vector2d.rayForPolarAngle(90.0));
    }

}
