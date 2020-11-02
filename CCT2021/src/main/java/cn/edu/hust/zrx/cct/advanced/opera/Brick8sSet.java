package cn.edu.hust.zrx.cct.advanced.opera;

import cn.edu.hust.zrx.cct.base.magnet.MagnetAble;
import cn.edu.hust.zrx.cct.base.point.Point3;
import cn.edu.hust.zrx.cct.base.vector.Vector3;

import java.util.ArrayList;
import java.util.List;

/**
 * Description
 * 多个 Brick8s
 * <p>
 * Data
 * 16:42
 *
 * @author zrx
 * @version 1.0
 */

public class Brick8sSet implements MagnetAble {

    List<Brick8s> set = new ArrayList<>();
    List<String> labels = new ArrayList<>();

    public static Brick8sSet empty() {
        return new Brick8sSet();
    }

    public Brick8sSet add(Brick8s brick8s) {
        this.set.add(brick8s);
        this.labels.add(null);
        return this;
    }

    public Brick8sSet add(Brick8s brick8s, String label) {
        this.set.add(brick8s);
        this.labels.add(label);
        return this;
    }

    public void clear() {
        set.clear();
        labels.clear();
    }

    @Override
    public Vector3 magnetAt(Point3 p) {
        return set.stream().map(bs -> bs.magnetAt(p)).reduce(Vector3.getZero(), (v1, v2) -> Vector3.add(v1, v2));
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("CONDUCTOR\n");

        for (int i = 0; i < set.size(); i++) {
            Brick8s brick8s = set.get(i);
            String label = labels.get(i);
            if (label == null) {
                sb.append(brick8s.toStringWithoutHeadTail());
            } else {
                sb.append(brick8s.toStringWithoutHeadTail(label));
            }
        }

        sb.append("QUIT\nEOF\n");

        return sb.toString();
    }

}
