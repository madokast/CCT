package zrx.CCT.ConcreteCCT;

import zrx.CCT.abstractCCT.CurvedCCT;
import zrx.base.Vector2d;
import zrx.base.Vector3d;
import zrx.python.Plot2d;
import zrx.python.Plot3d;

/**
 * 对建模成功的cct分析获得有用的离散数据，如
 * ksi phi 坐标下轨迹
 * xyz 空间轨迹
 * 起点/起点方向
 * 终点/终点方向
 *
 * 是单层cct的主题，所以实现SingleLayerDiscreteCCTComponent接口
 */

public class DiscreteCCT implements SingleLayerDiscreteCCTComponent{
    public CurvedCCT sourceCCT;

    public Vector2d[] pathInKsiPhi;
    public Vector3d[] pathInXYZ;

    public Vector2d startPoint;
    public Vector2d startDirect;
    public Vector2d endPoint;
    public Vector2d endDirect;

    public DiscreteCCT(CurvedCCT sourceCCT,Vector2d[] pathInKsiPhi, Vector3d[] pathInXYZ,
                       Vector2d startPoint, Vector2d startDirect, Vector2d endPoint, Vector2d endDirect) {
        this.sourceCCT = sourceCCT;
        this.pathInKsiPhi = pathInKsiPhi;
        this.pathInXYZ = pathInXYZ;
        this.startPoint = startPoint;
        this.startDirect = startDirect;
        this.endPoint = endPoint;
        this.endDirect = endDirect;

//        System.out.println("本段CCT离散化成功，点数："+pathInKsiPhi.length);
    }

    /**
     * 画图
     * @param describe python画图描述符
     */
    public void plot23d(String describe){
        Plot2d.plot2(pathInKsiPhi,describe);
        Plot3d.plot3(pathInXYZ,describe);
    }

    public void plot2d(String describe){
        Plot2d.plot2(pathInKsiPhi,describe);
    }

    public void plot3d(String describe){
        Plot3d.plot3(pathInXYZ,describe);
    }




    //getter
    public CurvedCCT getSourceCCT() {
        return sourceCCT;
    }

    public Vector2d[] getPathInKsiPhi() {
        return pathInKsiPhi;
    }

    public Vector3d[] getPathInXYZ() {
        return pathInXYZ;
    }

    public Vector2d getStartPoint() {
        return startPoint;
    }

    public Vector2d getStartDirect() {
        return startDirect;
    }

    public Vector2d getEndPoint() {
        return endPoint;
    }

    public Vector2d getEndDirect() {
        return endDirect;
    }

    public void setSourceCCT(CurvedCCT sourceCCT) {
        this.sourceCCT = sourceCCT;
    }

    public void setPathInKsiPhi(Vector2d[] pathInKsiPhi) {
        this.pathInKsiPhi = pathInKsiPhi;
    }

    public void setPathInXYZ(Vector3d[] pathInXYZ) {
        this.pathInXYZ = pathInXYZ;
    }

    public void setStartPoint(Vector2d startPoint) {
        this.startPoint = startPoint;
    }

    public void setStartDirect(Vector2d startDirect) {
        this.startDirect = startDirect;
    }

    public void setEndPoint(Vector2d endPoint) {
        this.endPoint = endPoint;
    }

    public void setEndDirect(Vector2d endDirect) {
        this.endDirect = endDirect;
    }
}