package cn.edu.hust.zrx.cct.base.line;

import cn.edu.hust.zrx.cct.Logger;
import cn.edu.hust.zrx.cct.base.BaseUtils;
import cn.edu.hust.zrx.cct.base.point.Point3;
import cn.edu.hust.zrx.cct.base.vector.Vector3;
import cn.hutool.log.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

/**
 * Description
 * DiscretePointLine3
 * 2020年6月18日
 * 测试通过
 * <p>
 * Data
 * 16:34
 *
 * @author zrx
 * @version 1.0
 */

public class DiscretePointLine3 implements Line3 {
    private final double length;

    // 每段长度、起点、方向
    private final List<BaseUtils.Content.TriContent<Double, Point3, Vector3>> discreteLine;

    public DiscretePointLine3(List<Point3> point3List) {

        this.discreteLine = new ArrayList<>(point3List.size() - 1);

        double totalLen = 0;

        for (int i = 1; i < point3List.size(); i++) {
            Point3 c = point3List.get(i);
            Point3 p = point3List.get(i - 1);

            Vector3 pc = Vector3.from(p).to(c);
            double partLen = pc.length();

            this.discreteLine.add(
                    BaseUtils.Content.TriContent.create(partLen, p, pc)
            );

            totalLen += partLen;
        }


        this.length = totalLen;
    }

    public static DiscretePointLine3 create(List<Point3> point3List) {
        return new DiscretePointLine3(point3List);
    }


    @Override
    public double getLength() {
        return length;
    }

    @Override
    public Point3 pointAt(double s) {
        if (s > length) {
            Logger.getLogger().error("s[{}]>len[{}]", s, length);
        }

        int index = 0;
        BaseUtils.Content.TriContent<Double, Point3, Vector3> part = discreteLine.get(0);
        for (; ; ) {
            if (index == discreteLine.size()) break;

            part = discreteLine.get(index);

            if (s < part.getT1()) break;

            s -= part.getT1();
            index++;
        }

        Point3 p = part.getT2();
        Vector3 v = part.getT3();

        return p.copy().moveSelf(v.copy().changeLengthSelf(s));

    }

    @Override
    public Vector3 directAt(double s) {
        if (s > length) {
            Logger.getLogger().error("s[{}]>len[{}]", s, length);
        }

        int index = 0;
        BaseUtils.Content.TriContent<Double, Point3, Vector3> part = discreteLine.get(0);

        for (; ; ) {
            if (index == discreteLine.size()) break;

            part = discreteLine.get(index);

            if (s < part.getT1()) break;

            s -= part.getT1();
            index++;
        }

        return part.getT3().copy();

    }

    public List<BaseUtils.Content.TriContent<Double, Point3, Vector3>> getDiscreteLine() {
        return discreteLine;
    }
}
