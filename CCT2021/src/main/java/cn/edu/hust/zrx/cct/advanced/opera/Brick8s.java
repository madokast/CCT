package cn.edu.hust.zrx.cct.advanced.opera;

import cn.edu.hust.zrx.cct.base.cct.MagnetAble;
import cn.edu.hust.zrx.cct.base.point.Point3;
import cn.edu.hust.zrx.cct.base.python.Plot2d;
import cn.edu.hust.zrx.cct.base.python.PlotAble3d;
import cn.edu.hust.zrx.cct.base.vector.Vector3;

import java.util.ArrayList;
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

public class Brick8s implements PlotAble3d, MagnetAble {
    private final List<Point3> line1;
    private final List<Point3> line2;
    private final List<Point3> line3;
    private final List<Point3> line4;

    // 电流密度
    private final double currentDensity;

    private volatile List<Brick8> brick8List = null;

    public Brick8s(List<Point3> line1, List<Point3> line2, List<Point3> line3, List<Point3> line4, double currentDensity) {
        this.line1 = line1;
        this.line2 = line2;
        this.line3 = line3;
        this.line4 = line4;
        this.currentDensity = currentDensity;
    }

    @Override
    public Vector3 magnetAt(Point3 p) {
        if (brick8List == null) {
            synchronized (this) {
                if (brick8List == null) {
                    int size = line1.size();
                    brick8List = new ArrayList<>(size);
                    for (int i = 0; i < size - 1; i++) {
                        Brick8 b = new Brick8(
                                line1.get(i),
                                line2.get(i),
                                line3.get(i),
                                line4.get(i),
                                line1.get(i + 1),
                                line2.get(i + 1),
                                line3.get(i + 1),
                                line4.get(i + 1),
                                currentDensity
                        );
                        brick8List.add(b);
                    }
                }
            }
        }

        return brick8List.stream().map(b -> b.magnetAt(p)).reduce(Vector3.getZero(), (v1, v2) -> Vector3.add(v1, v2));
    }

    @Override
    public void plot3d() {
        plot3(Plot2d.RED_LINE);
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

    public String toString(String label) {
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
            ).toString(label));
        }
        sb.append("QUIT\nEOF\n");

        return sb.toString();
    }

    public String toStringWithoutHeadTail(String label) {
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
            ).toString(label));
        }

        return sb.toString();
    }
}
