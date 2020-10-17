package cn.edu.hust.zrx.cct.base.cct;

import cn.edu.hust.zrx.cct.base.point.Point2Function;
import cn.edu.hust.zrx.cct.base.point.Point3Function;
import cn.edu.hust.zrx.cct.base.point.SimpleToroidalCoordinateSystemPoint2To3;

/**
 * Description
 * EntitySoleLayerCct
 * <p>
 * 实体CCT，包括线槽等
 *
 * <p>
 * Data
 * 14:08
 *
 * @author zrx
 * @version 1.0
 */

public class EntitySoleLayerCct {
    //Point3Function centerCctPath3d, Point2Function centerCctPath2d,
    //                    Point2To3.SimpleToroidalCoordinateSystemPoint2To3 simpleToroidalCoordinateSystemPoint2To3,
    //                    double width, double depth, double currentDensity, double startTheta, double endTheta, int totalBrickNumber

    public Point3Function centerCctPath3d;

    public Point2Function centerCctPath2d;

    public SimpleToroidalCoordinateSystemPoint2To3 simpleToroidalCoordinateSystemPoint2To3;

    public double width;

    public double depth;

    public double currentDensity;

    public double startTheta;

    public double endTheta;

    // 每匝分成几段
    public int brickNumberPerWinding;

    // 一共几匝
    public int windNumber;

    public EntitySoleLayerCct(Point3Function centerCctPath3d,
                              Point2Function centerCctPath2d,
                              SimpleToroidalCoordinateSystemPoint2To3 simpleToroidalCoordinateSystemPoint2To3,
                              double width, double depth, double currentDensity, double startTheta, double endTheta,
                              int brickNumberPerWinding, int windNumber) {
        this.centerCctPath3d = centerCctPath3d;
        this.centerCctPath2d = centerCctPath2d;
        this.simpleToroidalCoordinateSystemPoint2To3 = simpleToroidalCoordinateSystemPoint2To3;
        this.width = width;
        this.depth = depth;
        this.currentDensity = currentDensity;
        this.startTheta = startTheta;
        this.endTheta = endTheta;
        this.brickNumberPerWinding = brickNumberPerWinding;
        this.windNumber = windNumber;
    }

    public Point3Function getCenterCctPath3d() {
        return centerCctPath3d;
    }

    public Point2Function getCenterCctPath2d() {
        return centerCctPath2d;
    }

    public SimpleToroidalCoordinateSystemPoint2To3 getSimpleToroidalCoordinateSystemPoint2To3() {
        return simpleToroidalCoordinateSystemPoint2To3;
    }

    public double getWidth() {
        return width;
    }

    public double getDepth() {
        return depth;
    }

    public double getCurrentDensity() {
        return currentDensity;
    }

    public double getStartTheta() {
        return startTheta;
    }

    public double getEndTheta() {
        return endTheta;
    }

    public int getBrickNumberPerWinding() {
        return brickNumberPerWinding;
    }

    public int getWindNumber() {
        return windNumber;
    }

    public void setCenterCctPath3d(Point3Function centerCctPath3d) {
        this.centerCctPath3d = centerCctPath3d;
    }

    public void setCenterCctPath2d(Point2Function centerCctPath2d) {
        this.centerCctPath2d = centerCctPath2d;
    }

    public void setSimpleToroidalCoordinateSystemPoint2To3(SimpleToroidalCoordinateSystemPoint2To3 simpleToroidalCoordinateSystemPoint2To3) {
        this.simpleToroidalCoordinateSystemPoint2To3 = simpleToroidalCoordinateSystemPoint2To3;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public void setDepth(double depth) {
        this.depth = depth;
    }

    public void setCurrentDensity(double currentDensity) {
        this.currentDensity = currentDensity;
    }

    public void setStartTheta(double startTheta) {
        this.startTheta = startTheta;
    }

    public void setEndTheta(double endTheta) {
        this.endTheta = endTheta;
    }

    public void setBrickNumberPerWinding(int brickNumberPerWinding) {
        this.brickNumberPerWinding = brickNumberPerWinding;
    }

    public void setWindNumber(int windNumber) {
        this.windNumber = windNumber;
    }

    @Override
    public String toString() {
        return "EntitySoleLayerCct{" +
                "centerCctPath3d=" + centerCctPath3d +
                ", centerCctPath2d=" + centerCctPath2d +
                ", simpleToroidalCoordinateSystemPoint2To3=" + simpleToroidalCoordinateSystemPoint2To3 +
                ", width=" + width +
                ", depth=" + depth +
                ", currentDensity=" + currentDensity +
                ", startTheta=" + startTheta +
                ", endTheta=" + endTheta +
                ", brickNumberPerWinding=" + brickNumberPerWinding +
                ", windNumber=" + windNumber +
                '}';
    }
}
