package zrx.DemoAndStudy.test;

import zrx.base.Vector2d;

public class SmallTest {
    public static void main(String[] args) {
        rayForPolarAngleTest();
    }

    private static void rayForPolarAngleTest() {
        System.out.println(Vector2d.rayForPolarAngle(0.0));
        System.out.println(Vector2d.rayForPolarAngle(45.0));
        System.out.println(Vector2d.rayForPolarAngle(90.0));
    }

}
