package cn.edu.hust.zrx.cct.base.cct;

import cn.edu.hust.zrx.cct.base.point.Point2;
import cn.edu.hust.zrx.cct.base.point.Point3;

import java.util.ArrayList;
import java.util.List;

/**
 * Description
 * 多个 CctLine2
 * <p>
 * Data
 * 20:52
 *
 * @author zrx
 * @version 1.0
 */

public class CctLine2s {
    protected List<CctLine2> cctLine2s;

    private CctLine2s(CctLine2 cctLine2) {
        this.cctLine2s = new ArrayList<>();
        this.cctLine2s.add(cctLine2);
    }


    protected static CctLine2s create(CctLine2 cctLine2) {
        return new CctLine2s(cctLine2);
    }

    protected void add(CctLine2 cctLine2) {
        this.cctLine2s.add(cctLine2);
    }

    public List<Point2> disperseToPoint2(final int numberPerWinding) {
        final List<Point2> point2s = new ArrayList<>();
        this.cctLine2s.forEach(cctLine2 ->
                point2s.addAll(cctLine2.disperseToPoint2(numberPerWinding))
        );

        return point2s;
    }

    public List<Point3> disperseToPoint3(final int numberPerWinding) {
        final List<Point3> point3s = new ArrayList<>();
        this.cctLine2s.forEach(cctLine2 ->
                point3s.addAll(cctLine2.disperseToPoint3(numberPerWinding))
        );

        return point3s;
    }
}
