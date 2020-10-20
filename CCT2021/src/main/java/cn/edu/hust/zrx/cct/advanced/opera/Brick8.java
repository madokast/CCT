package cn.edu.hust.zrx.cct.advanced.opera;

import cn.edu.hust.zrx.cct.base.BaseUtils;
import cn.edu.hust.zrx.cct.base.cct.MagnetAble;
import cn.edu.hust.zrx.cct.base.point.Point3;
import cn.edu.hust.zrx.cct.base.python.Plot2d;
import cn.edu.hust.zrx.cct.base.python.Plot3d;
import cn.edu.hust.zrx.cct.base.python.PlotAble3d;
import cn.edu.hust.zrx.cct.base.vector.Vector3;

import javax.swing.text.Utilities;

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

public class Brick8 implements PlotAble3d, MagnetAble {
    private final Point3 frontFacePoint1;
    private final Point3 frontFacePoint2;
    private final Point3 frontFacePoint3;
    private final Point3 frontFacePoint4;

    private final Point3 backFacePoint1;
    private final Point3 backFacePoint2;
    private final Point3 backFacePoint3;
    private final Point3 backFacePoint4;

    private final double currentDensity;

    private volatile Point3 backFaceCenter = null;
    private volatile Point3 frontFaceCenter = null;
    private volatile double I0 = 0.0;

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
    public Vector3 magnetAt(Point3 p) {
        if (I0 == 0) {
            synchronized (this) {
                if (I0 == 0) {
                    backFaceCenter = Point3.average(backFacePoint1, backFacePoint2, backFacePoint3, backFacePoint4);
                    frontFaceCenter = Point3.average(frontFacePoint1, frontFacePoint2, frontFacePoint3, frontFacePoint4);
                    double width = Point3.distance(backFacePoint1, backFacePoint2);
                    double height = Point3.distance(backFacePoint1, backFacePoint3);
                    I0 = width * height * currentDensity;
                }
            }
        }


        //        /**
        //         * 计算点 p0 到点 p1 电流为 I 的直导线在 p 点产生的磁场。
        //         * 使用 毕奥-萨伐尔定律。当p0 p1很近时，即为 dB，可用作数值积分 B = ∫dB
        //         * 还有十足的优化空间
        //         * <p>
        //         * 2020年4月17日 修改代码
        //         * r 应该是 p0 p1 中点到 p
        //         *
        //         * @param p0 直导线起点
        //         * @param p1 直导线终点
        //         * @param I  电流，从 p0 流向 p1
        //         * @param p  需要计算磁场的点
        //         * @return p 点的磁场
        //         */
        return BaseUtils.Magnet.dB(
                backFaceCenter, frontFaceCenter, I0, p
        );
    }

    @Override
    public void plot3d() {
        plot3(Plot2d.RED_LINE);
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

    public String toString(String label) {
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

        sb.append(currentDensity + " 1  '" + label + "  '\n");
        sb.append("0 0 0\n");
        sb.append("1.0\n");

        return sb.toString();
    }
}
