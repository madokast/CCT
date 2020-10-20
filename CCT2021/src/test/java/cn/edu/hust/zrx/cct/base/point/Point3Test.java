package cn.edu.hust.zrx.cct.base.point;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Point3Test {
    @Test
    public void testAdd() {
        Point3 p1 = Point3.create(1, 1, 1);

        Point3 p2 = Point3.create(1, 2, 3);

        Point3 add = Point3.add(p1, p2);

        System.out.println("add = " + add);
    }

    @Test
    public void averageTest() {
        Point3 p1 = Point3.create(1, 1, 1);

        Point3 p2 = Point3.create(1, 2, 3);

        Point3 p3 = Point3.create(5, 5, 5);

        Point3 average = Point3.average(p1, p2, p3);

        System.out.println("average = " + average);
    }

}