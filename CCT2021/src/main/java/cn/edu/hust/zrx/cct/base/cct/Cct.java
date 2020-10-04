package cn.edu.hust.zrx.cct.base.cct;

import cn.edu.hust.zrx.cct.Logger;
import cn.edu.hust.zrx.cct.base.BaseUtils;
import cn.edu.hust.zrx.cct.base.point.Point2;
import cn.edu.hust.zrx.cct.base.point.Point3;
import cn.edu.hust.zrx.cct.base.python.Plot2d;
import cn.edu.hust.zrx.cct.base.python.PlotAble3d;
import cn.edu.hust.zrx.cct.base.vector.Vector3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 各种CCT
 */
public class Cct implements MagnetAble, PlotAble3d {
    public final List<SoleLayerCct> soleLayerCctList;

    /**
     * 空构造器
     * 使用 getEmptyCct
     */
    public Cct() {
        this.soleLayerCctList = new ArrayList<>();
    }

    private Cct(List<SoleLayerCct> soleLayerCctList) {
        this.soleLayerCctList = soleLayerCctList;
    }

    /**
     * 增加单层CCT
     *
     * @param soleLayerCct 单层CCT
     * @return CCT
     */
    public Cct addSoleLayerCct(final SoleLayerCct soleLayerCct) {
        this.soleLayerCctList.add(soleLayerCct);
        return this;
    }

    /**
     * 增加CCT
     *
     * @param cct CCT
     * @return CCT
     */
    public Cct addCct(Cct cct) {
        this.soleLayerCctList.addAll(cct.soleLayerCctList);
        return this;
    }

    public Cct deepCopy() {
        return new Cct(soleLayerCctList.stream().map(SoleLayerCct::deepCopy).collect(Collectors.toList()));
    }

    /**
     * 空工厂方法，获得空CCT
     *
     * @return 空工厂方法，获得空CCT
     */
    public static Cct getEmptyCct() {
        return new Cct();
    }

    /**
     * 取出某层，用于调试
     *
     * @param index 层数
     * @return 该层CCT
     */
    public SoleLayerCct get(int index) {
        return this.soleLayerCctList.get(index);
    }

    public List<SoleLayerCct> getSoleLayerCctList() {
        return soleLayerCctList;
    }

    @Override
    public String toString() {
        return "Cct{" +
                "soleLayerCctList.size=" + soleLayerCctList.size() +
                '}';
    }

    /**
     * 绘图
     * 先全部外层后内层
     *
     * @param describes 描述
     */
    public void plot3(String... describes) {
        if (describes == null || describes.length == 0) {
            describes = new String[soleLayerCctList.size()];
            Arrays.fill(describes, Plot2d.BLUE_LINE);
        }

        int length = describes.length;
        int size = soleLayerCctList.size();
        if (!Objects.equals(length, size)) {
            Logger.getLogger().error("CCT绘制，describes数目不对。");
            Logger.getLogger().error("describes.length = " + length);
            Logger.getLogger().error("soleLayerCctList.size() = " + size);
        }

        int plotTimes = Math.min(length, size);

        final String[] temps = describes; // 不这么做lambda报错

        BaseUtils.StreamTools.forZeroToN(plotTimes)
                .forEach(i -> this.soleLayerCctList.get(i).plot3(temps[i]));
    }

    @Override
    public void plot3d() {
        plot3(Plot2d.RED_LINE);
    }

    public int cctLayerNumber() {
        return soleLayerCctList.size();
    }

    /**
     * 磁场
     *
     * @param p 点
     * @return p点磁场
     */
    @Override
    public Vector3 magnetAt(final Point3 p) {
        final Vector3 m = Vector3.getZero();
        this.soleLayerCctList.forEach(
                soleLayerCct ->
                        m.addSelf(soleLayerCct.magnetAt(p))
        );

        return m;
    }

    public Cct rotateInXYPlane(Point2 point2, double phi) {
        Cct ret = new Cct();

        for (SoleLayerCct soleLayerCct : this.soleLayerCctList) {
            ret.addSoleLayerCct(soleLayerCct.rotateInXYPlane(point2, phi));
        }

        return ret;
    }

    public Cct move(Vector3 v) {
        Cct ret = new Cct();

        for (SoleLayerCct soleLayerCct : this.soleLayerCctList) {
            ret.addSoleLayerCct(soleLayerCct.move(v));
        }

        return ret;
    }
}
