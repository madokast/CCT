package zrx.CCT;

import zrx.base.Vector3d;
import zrx.python.Plot3d;

/**
 * 分立具体的CCT
 * 路径
 * 电流
 */

public class NoLinkedCCT {
    private Vector3d[] path;
    private double I;

    /**
     * 平移CCT
     * @param direct 方向
     * @param length 长度
     */
    public void move(Vector3d direct,double length){
        this.path = Vector3d.move(this.path,direct,length);
    }


    /**
     * 计算p点磁场
     * @param p 点
     * @return 磁场
     */
    public Vector3d magnetAtPoint(Vector3d p){
        return Magnet.magnetAtPoint(path,I,p);
    }

    public void plot3(String describe){
        Plot3d.plot3(path,describe);
    }

    @Deprecated
    public NoLinkedCCT() { }

    public NoLinkedCCT(Vector3d[] path, double i) {
        this.path = path;
        I = i;
    }


    public Vector3d[] getPath() {
        return path;
    }

    public double getI() {
        return I;
    }
}
