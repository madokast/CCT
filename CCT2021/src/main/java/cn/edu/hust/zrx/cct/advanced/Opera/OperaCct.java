package cn.edu.hust.zrx.cct.advanced.Opera;

import cn.edu.hust.zrx.cct.advanced.MathFunction;
import cn.edu.hust.zrx.cct.advanced.comsol.HexahedronModel;
import cn.edu.hust.zrx.cct.base.cct.EntitySoleLayerCct;
import cn.edu.hust.zrx.cct.base.point.Point2Function;
import cn.edu.hust.zrx.cct.base.point.Point2To3;
import cn.edu.hust.zrx.cct.base.point.Point3Function;
import cn.edu.hust.zrx.cct.base.vector.Vector3Function;

import static java.lang.Math.*;
import static java.lang.Math.PI;

/**
 * Description
 * OperaCct
 * <p>
 * Data
 * 17:04
 *
 * @author zrx
 * @version 1.0
 */

public class OperaCct {
    private final Point3Function centerCctPath3d;

    private final Point2Function centerCctPath2d;

    private final Point2To3.SimpleToroidalCoordinateSystemPoint2To3 simpleToroidalCoordinateSystemPoint2To3;

    private final double width;

    private final double depth;

    private final double currentDensity;

    private final double startTheta;

    private final double endTheta;

    private final int totalBrickNumber;

    public Brick8s createBrick8s() {
        // 切向
        Vector3Function tangentialDirection = centerCctPath3d.differential(1e-6).normal();

        // 主法向
        Vector3Function mainNormalDirection = simpleToroidalCoordinateSystemPoint2To3.mainNormalDirection(centerCctPath2d);

        // 副法线方向
        Vector3Function secondNormalDirection = tangentialDirection.cross(mainNormalDirection);

        Point3Function cctPath1 = centerCctPath3d.add(mainNormalDirection.dot(depth / 2)).add(secondNormalDirection.dot(width / 2));
        Point3Function cctPath2 = centerCctPath3d.add(mainNormalDirection.dot(depth / 2).reverse()).add(secondNormalDirection.dot(width / 2));
        Point3Function cctPath3 = centerCctPath3d.add(mainNormalDirection.dot(depth / 2).reverse()).add(secondNormalDirection.dot(width / 2).reverse());
        Point3Function cctPath4 = centerCctPath3d.add(mainNormalDirection.dot(depth / 2)).add(secondNormalDirection.dot(width / 2).reverse());

        return new Brick8s(
                cctPath1.disperse(startTheta, endTheta, totalBrickNumber),
                cctPath2.disperse(startTheta, endTheta, totalBrickNumber),
                cctPath3.disperse(startTheta, endTheta, totalBrickNumber),
                cctPath4.disperse(startTheta, endTheta, totalBrickNumber),
                currentDensity
        );
    }


    public OperaCct(Point3Function centerCctPath3d, Point2Function centerCctPath2d,
                    Point2To3.SimpleToroidalCoordinateSystemPoint2To3 simpleToroidalCoordinateSystemPoint2To3,
                    double width, double depth, double currentDensity, double startTheta, double endTheta, int totalBrickNumber) {
        this.centerCctPath3d = centerCctPath3d;
        this.centerCctPath2d = centerCctPath2d;
        this.simpleToroidalCoordinateSystemPoint2To3 = simpleToroidalCoordinateSystemPoint2To3;
        this.width = width;
        this.depth = depth;
        this.currentDensity = currentDensity;
        this.startTheta = startTheta;
        this.endTheta = endTheta;
        this.totalBrickNumber = totalBrickNumber;
    }

    public static OperaCct create(EntitySoleLayerCct entitySoleLayerCct){
        return new OperaCct(
                entitySoleLayerCct.centerCctPath3d,
                entitySoleLayerCct.centerCctPath2d,
                entitySoleLayerCct.simpleToroidalCoordinateSystemPoint2To3,
                entitySoleLayerCct.width,
                entitySoleLayerCct.depth,
                entitySoleLayerCct.currentDensity,
                entitySoleLayerCct.startTheta,
                entitySoleLayerCct.endTheta,
                entitySoleLayerCct.brickNumberPerWinding * entitySoleLayerCct.windNumber
        );
    }

}
