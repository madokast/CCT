package cn.edu.hust.zrx.cct.base.cct;

import cn.edu.hust.zrx.cct.base.point.Point3;
import cn.edu.hust.zrx.cct.base.python.PlotAble3d;
import cn.edu.hust.zrx.cct.base.vector.Vector3;

import java.util.ArrayList;
import java.util.List;

/**
 * Description
 * Elements
 * <p>
 * Data
 * 20:44
 *
 * @author zrx
 * @version 1.0
 */
public class Elements implements MagnetAble, PlotAble3d {
    private List<MagnetAble> elements;

    private Elements() {
        this.elements = new ArrayList<>();
    }

    public static Elements empty() {
        return new Elements();
    }

    public static Elements of() {
        return empty();
    }

    public static Elements of(MagnetAble m) {
        Elements e = empty();
        e.addElement(m);
        return e;
    }

    public static Elements of(MagnetAble m1, MagnetAble m2) {
        Elements e = empty();
        e.addElement(m1);
        e.addElement(m2);
        return e;
    }

    public static Elements of(MagnetAble... magnetAbles) {
        Elements e = empty();
        for (MagnetAble magnetAble : magnetAbles) {
            e.addElement(magnetAble);
        }
        return e;
    }

    public Elements addElement(MagnetAble m) {
        this.elements.add(m);
        return this;
    }

    @Override
    public Vector3 magnetAt(Point3 p) {
        Vector3 magnet = elements.get(0).magnetAt(p);

        for (int i = 1; i < elements.size(); i++) {
            magnet.addSelf(elements.get(i).magnetAt(p));
        }

        return magnet;
    }

    @Override
    public void plot3d() {
        elements.stream()
                .filter(magnetAble -> magnetAble instanceof PlotAble3d)
                .map(magnetAble -> (PlotAble3d) magnetAble)
                .forEach(PlotAble3d::plot3d);
    }
}
