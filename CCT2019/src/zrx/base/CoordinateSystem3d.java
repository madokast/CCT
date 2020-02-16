package zrx.base;

import zrx.Tools.Equal;
import zrx.base.point.Vector3d;

/**
 * 三维直角坐标系
 * 以CCT建模时的全局坐标系为基
 */

public class CoordinateSystem3d {
    public Vector3d xDirect;
    public Vector3d yDirect;
    public Vector3d zDirect;

    /**
     * 空构造器
     * CCT建模时的全局坐标系
     */
    public CoordinateSystem3d() {
        this.xDirect = Vector3d.getXDirect();
        this.yDirect = Vector3d.getYDirect();
        this.zDirect = Vector3d.getZDirect();
    }

    public CoordinateSystem3d(Vector3d xDirect, Vector3d yDirect, Vector3d zDirect) {
        Equal.requireNonzero(xDirect.length());
        Equal.requireNonzero(yDirect.length());
        Equal.requireNonzero(zDirect.length());

        //是不是右手系
        if (!Equal.isEqual(Vector3d.copyOne(zDirect).setLengthAndReturn(1.0),
                Vector3d.corss(xDirect, yDirect).setLengthAndReturn(1.0)))
            System.err.println("CoordinateSystem3d:请注意建立的坐标系不是右手系");


        this.xDirect = Vector3d.copyOne(xDirect).setLengthAndReturn(1.0);
        this.yDirect = Vector3d.copyOne(yDirect).setLengthAndReturn(1.0);
        this.zDirect = Vector3d.copyOne(zDirect).setLengthAndReturn(1.0);
    }

    /**
     * 返回点p在this坐标系的坐标
     *
     * @param p 点p，用全局坐标表示
     * @return 点p在this坐标系的坐标
     */
    public Vector3d coordinate(Vector3d p) {
        return new Vector3d(
                Vector3d.dot(p, xDirect),
                Vector3d.dot(p, yDirect),
                Vector3d.dot(p, zDirect)
        );
    }

    /**
     * 已知yDirect zDirect 按照右手系建立坐标系
     *
     * @param yDirect y-坐标轴方向
     * @param zDirect z-坐标轴方向
     * @return 坐标系
     */
    public static CoordinateSystem3d getOneByYZ(Vector3d yDirect, Vector3d zDirect) {
        Vector3d xDirect = Vector3d.corss(yDirect, zDirect);
        return new CoordinateSystem3d(xDirect, yDirect, zDirect);
    }

    @Override
    public String toString() {
        return "CoordinateSystem3d{" +
                "xDirect=" + xDirect +
                ", yDirect=" + yDirect +
                ", zDirect=" + zDirect +
                '}';
    }

    public static void main(String[] args) {
//        测试1();
        测试2();
    }

    private static void 测试2() {
        final CoordinateSystem3d coordinateSystem3d = getOneByYZ(Vector3d.getZDirect(), Vector3d.getYDirect());
        System.out.println("coordinateSystem3d = " + coordinateSystem3d);
    }

    private static void 测试1() {
        final CoordinateSystem3d system3d = new CoordinateSystem3d(Vector3d.getXDirect(), Vector3d.getYDirect(), Vector3d.getZDirect());
        System.out.println(system3d);

        final CoordinateSystem3d system3d1 = new CoordinateSystem3d(
                Vector3d.getXDirect(-1),
                Vector3d.getZDirect(),
                Vector3d.getYDirect()
        );
        System.out.println(system3d1);
    }
}
