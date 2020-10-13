package cn.edu.hust.zrx.cct.advanced.Opera;

import cn.edu.hust.zrx.cct.base.point.Point3;
import cn.edu.hust.zrx.cct.base.python.Plot3d;

/**
 * Description
 * opera 的 8点 brick
 * <p>
 * Data
 * 16:30
 *
 * @author zrx
 * @version 1.0
 */

public class Brick8 {
    private final Point3 frontFacePoint1;
    private final Point3 frontFacePoint2;
    private final Point3 frontFacePoint3;
    private final Point3 frontFacePoint4;

    private final Point3 backFacePoint1;
    private final Point3 backFacePoint2;
    private final Point3 backFacePoint3;
    private final Point3 backFacePoint4;

    private final double currentDensity;

    public Brick8(Point3 frontFacePoint1, Point3 frontFacePoint2, Point3 frontFacePoint3, Point3 frontFacePoint4,
                  Point3 backFacePoint1, Point3 backFacePoint2, Point3 backFacePoint3, Point3 backFacePoint4,
                  double currentDensity) {
        this.frontFacePoint1 = frontFacePoint1;
        this.frontFacePoint2 = frontFacePoint2;
        this.frontFacePoint3 = frontFacePoint3;
        this.frontFacePoint4 = frontFacePoint4;
        this.backFacePoint1 = backFacePoint1;
        this.backFacePoint2 = backFacePoint2;
        this.backFacePoint3 = backFacePoint3;
        this.backFacePoint4 = backFacePoint4;
        this.currentDensity = currentDensity;
    }

    public void plot3(String des) {
        Plot3d.plot3(new Point3[]{
                frontFacePoint1,
                frontFacePoint2,
                frontFacePoint3,
                frontFacePoint4,

                backFacePoint1,
                backFacePoint2,
                backFacePoint3,
                backFacePoint4
        }, des);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("DEFINE BR8\n");
        sb.append("0.0 0.0 0.0 0.0 0.0\n");
        sb.append("0.0 0.0 0.0\n");
        sb.append("0.0 0.0 0.0\n");

        sb.append(String.format("%e %e %e\n", frontFacePoint1.x, frontFacePoint1.y, frontFacePoint1.z));
        sb.append(String.format("%e %e %e\n", frontFacePoint2.x, frontFacePoint2.y, frontFacePoint2.z));
        sb.append(String.format("%e %e %e\n", frontFacePoint3.x, frontFacePoint3.y, frontFacePoint3.z));
        sb.append(String.format("%e %e %e\n", frontFacePoint4.x, frontFacePoint4.y, frontFacePoint4.z));

        sb.append(String.format("%e %e %e\n", backFacePoint1.x, backFacePoint1.y, backFacePoint1.z));
        sb.append(String.format("%e %e %e\n", backFacePoint2.x, backFacePoint2.y, backFacePoint2.z));
        sb.append(String.format("%e %e %e\n", backFacePoint3.x, backFacePoint3.y, backFacePoint3.z));
        sb.append(String.format("%e %e %e\n", backFacePoint4.x, backFacePoint4.y, backFacePoint4.z));

        sb.append(currentDensity + " 1  'layer1'\n");
        sb.append("0 0 0\n");
        sb.append("1.0\n");

        return sb.toString();
    }
}
