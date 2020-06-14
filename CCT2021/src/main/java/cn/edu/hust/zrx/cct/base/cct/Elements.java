package cn.edu.hust.zrx.cct.base.cct;

import cn.edu.hust.zrx.cct.base.point.Point3;
import cn.edu.hust.zrx.cct.base.vector.Vector3;

import java.util.ArrayList;
import java.util.List;

/**
 * Description
 * TODO
 * <p>
 * Data
 * 20:44
 *
 * @author zrx
 * @version 1.0
 */
public class Elements implements MagnetAble {
    private List<MagnetAble> elements;

    private Elements() {
        this.elements = new ArrayList<>();
    }

    public static Elements empty() {
        return new Elements();
    }

    public void addElement(MagnetAble m) {
        this.elements.add(m);
    }

    @Override
    public Vector3 magnetAt(Point3 p) {
        Vector3 magnet = elements.get(0).magnetAt(p);

        for (int i = 1; i < elements.size(); i++) {
            magnet.addSelf(elements.get(i).magnetAt(p));
        }

        return magnet;
    }
}
