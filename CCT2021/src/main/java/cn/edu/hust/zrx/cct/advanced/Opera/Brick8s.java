package cn.edu.hust.zrx.cct.advanced.Opera;

import cn.edu.hust.zrx.cct.base.point.Point3;

import java.util.List;

/**
 * Description
 * Brick8s
 * <p>
 * Data
 * 16:40
 *
 * @author zrx
 * @version 1.0
 */

public class Brick8s {
    private final List<Point3> line1;
    private final List<Point3> line2;
    private final List<Point3> line3;
    private final List<Point3> line4;

    // 电流密度
    private final double currentDensity;

    public Brick8s(List<Point3> line1, List<Point3> line2, List<Point3> line3, List<Point3> line4, double currentDensity) {
        this.line1 = line1;
        this.line2 = line2;
        this.line3 = line3;
        this.line4 = line4;
        this.currentDensity = currentDensity;
    }

    public void plot3(String des) {
        int size = line1.size();
        for (int i = 0; i < size - 1; i++) {
            new Brick8(
                    line1.get(i),
                    line2.get(i),
                    line3.get(i),
                    line4.get(i),
                    line1.get(i + 1),
                    line2.get(i + 1),
                    line3.get(i + 1),
                    line4.get(i + 1),
                    currentDensity
            ).plot3(des);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("CONDUCTOR\n");
        int size = line1.size();
        for (int i = 0; i < size - 1; i++) {
            sb.append(new Brick8(
                    line1.get(i),
                    line2.get(i),
                    line3.get(i),
                    line4.get(i),
                    line1.get(i + 1),
                    line2.get(i + 1),
                    line3.get(i + 1),
                    line4.get(i + 1),
                    currentDensity
            ).toString());
        }
        sb.append("QUIT\nEOF\n");

        return sb.toString();
    }

    public String toStringWithoutHeadTail() {
        StringBuilder sb = new StringBuilder();
        int size = line1.size();
        for (int i = 0; i < size - 1; i++) {
            sb.append(new Brick8(
                    line1.get(i),
                    line2.get(i),
                    line3.get(i),
                    line4.get(i),
                    line1.get(i + 1),
                    line2.get(i + 1),
                    line3.get(i + 1),
                    line4.get(i + 1),
                    currentDensity
            ).toString());
        }

        return sb.toString();
    }
}
