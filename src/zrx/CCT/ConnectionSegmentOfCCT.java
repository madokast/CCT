package zrx.CCT;

import zrx.CCT.abstractCCT.CurvedCCT;
import zrx.base.Vector2d;
import zrx.base.Vector3d;
import zrx.python.Plot2d;
import zrx.python.Plot3d;

/**
 * cct的连接部分
 */

public class ConnectionSegmentOfCCT {
    public Vector2d[] pathInKsiPhi;
    public Vector3d[] pathInXYZ;

    public Vector2d startPoint;
    public Vector2d startDirect;
    public Vector2d endPoint;
    public Vector2d endDirect;

    public Vector2d intermediaryPoint;
    public Vector2d intermediaryPointDirect;

    public Vector2d polePoint;

    public CurvedCCT fromCCT;
    public CurvedCCT toCCT;

    /**
     * cct的连接部分
     *
     * @param pathInKsiPhi            ksi/phi 二维轨迹
     * @param pathInXYZ               xyz坐标系 三维轨迹
     * @param startPoint              起点
     * @param startDirect             起点方向
     * @param endPoint                终点
     * @param endDirect               终点方向
     * @param intermediaryPoint       中间外凸点
     * @param intermediaryPointDirect 中间外凸点方向
     * @param polePoint               极点
     * @param fromCCT                 连接的CCT 前面那个
     * @param toCCT                   连接的CCT 后面那个
     */
    public ConnectionSegmentOfCCT(Vector2d[] pathInKsiPhi, Vector3d[] pathInXYZ,
                                  Vector2d startPoint, Vector2d startDirect, Vector2d endPoint,
                                  Vector2d endDirect, Vector2d intermediaryPoint,
                                  Vector2d intermediaryPointDirect, Vector2d polePoint,
                                  CurvedCCT fromCCT, CurvedCCT toCCT) {
        this.pathInKsiPhi = pathInKsiPhi;
        this.pathInXYZ = pathInXYZ;
        this.startPoint = startPoint;
        this.startDirect = startDirect;
        this.endPoint = endPoint;
        this.endDirect = endDirect;
        this.intermediaryPoint = intermediaryPoint;
        this.intermediaryPointDirect = intermediaryPointDirect;
        this.polePoint = polePoint;
        this.fromCCT = fromCCT;
        this.toCCT = toCCT;

        System.out.println("CCT连接段建模并离散化成功，点数："+pathInKsiPhi.length);
    }

    public void plot23d(String describe) {
        Plot2d.plot2(pathInKsiPhi, describe);
        Plot3d.plot3(pathInXYZ, describe);
    }

    public void plotKeyPoint(String describe){
        Plot2d.plotPoint(intermediaryPoint,describe);
        Plot2d.plotPoint(startPoint,describe);
        Plot2d.plotPoint(endPoint,describe);
        Plot2d.plotVector(intermediaryPoint,intermediaryPointDirect,0.1);
        Plot2d.plotPoint(polePoint,describe);
    }


    //getter
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

    public Vector2d getIntermediaryPoint() {
        return intermediaryPoint;
    }

    public Vector2d getIntermediaryPointDirect() {
        return intermediaryPointDirect;
    }

    public Vector2d getPolePoint() {
        return polePoint;
    }

    public CurvedCCT getFromCCT() {
        return fromCCT;
    }

    public CurvedCCT getToCCT() {
        return toCCT;
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

    public void setIntermediaryPoint(Vector2d intermediaryPoint) {
        this.intermediaryPoint = intermediaryPoint;
    }

    public void setIntermediaryPointDirect(Vector2d intermediaryPointDirect) {
        this.intermediaryPointDirect = intermediaryPointDirect;
    }

    public void setPolePoint(Vector2d polePoint) {
        this.polePoint = polePoint;
    }

    public void setFromCCT(CurvedCCT fromCCT) {
        this.fromCCT = fromCCT;
    }

    public void setToCCT(CurvedCCT toCCT) {
        this.toCCT = toCCT;
    }
}
