package cn.edu.hust.zrx.cct.base;


import cn.edu.hust.zrx.cct.Logger;
import cn.edu.hust.zrx.cct.base.point.Point3;
import cn.edu.hust.zrx.cct.base.vector.Vector3;

/**
 * 三维直角坐标系
 * 以CCT建模时的全局坐标系为基
 */

public class CoordinateSystem3d {
    public Vector3 xDirect;
    public Vector3 yDirect;
    public Vector3 zDirect;

    /**
     * 空构造器
     * CCT建模时的全局坐标系
     */
    public CoordinateSystem3d() {
        this.xDirect = Vector3.getXDirect();
        this.yDirect = Vector3.getYDirect();
        this.zDirect = Vector3.getZDirect();
    }

    public CoordinateSystem3d(final Vector3 xDirect, final Vector3 yDirect, final Vector3 zDirect) {
        BaseUtils.Equal.requireNonzero(xDirect);
        BaseUtils.Equal.requireNonzero(yDirect);
        BaseUtils.Equal.requireNonzero(zDirect);

        //是不是右手系
        if (!BaseUtils.Equal.isEqual(zDirect.copy().changeLengthSelf(1.0),
                Vector3.cross(xDirect, yDirect).changeLengthSelf(1.0)))
            Logger.getLogger().error("CoordinateSystem3d:请注意建立的坐标系不是右手系");


        this.xDirect = xDirect.copy().normalSelf();
        this.yDirect = yDirect.copy().normalSelf();
        this.zDirect = zDirect.copy().normalSelf();
    }

    /**
     * 返回点p在this坐标系的坐标
     *
     * @param p 点p，用全局坐标表示
     * @return 点p在this坐标系的坐标
     */
    public Vector3 coordinate(Point3 p) {
        return Vector3.create(
                Vector3.dot(p.toVector3(), xDirect),
                Vector3.dot(p.toVector3(), yDirect),
                Vector3.dot(p.toVector3(), zDirect)
        );
    }


    /**
     * 点 p 是在 this 坐标系中的点
     * 返回他在全局坐标系中的坐标
     * @param p this 坐标系中的点
     * @return 在全局坐标系中的坐标
     */
    public Point3 coordinateReverse(Point3 p){
        return null;
    }

    /**
     * 已知yDirect zDirect 按照右手系建立坐标系
     *
     * @param yDirect y-坐标轴方向
     * @param zDirect z-坐标轴方向
     * @return 坐标系
     */
    public static CoordinateSystem3d getOneByYZ(Vector3 yDirect, Vector3 zDirect) {
        Vector3 xDirect = Vector3.cross(yDirect, zDirect);
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
}
