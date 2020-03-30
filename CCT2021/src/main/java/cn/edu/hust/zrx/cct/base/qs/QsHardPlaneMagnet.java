package cn.edu.hust.zrx.cct.base.qs;

import cn.edu.hust.zrx.cct.Logger;
import cn.edu.hust.zrx.cct.advanced.MathFunction;
import cn.edu.hust.zrx.cct.base.BaseUtils;
import cn.edu.hust.zrx.cct.base.CoordinateSystem3d;
import cn.edu.hust.zrx.cct.base.cct.CctFactory;
import cn.edu.hust.zrx.cct.base.point.Point2;
import cn.edu.hust.zrx.cct.base.point.Point3;
import cn.edu.hust.zrx.cct.base.vector.Vector2;
import cn.edu.hust.zrx.cct.base.vector.Vector3;

/**
 * Description
 * QS 磁铁
 * 硬边模型
 * <p>
 * Data
 * 20:16
 *
 * @author zrx
 * @version 1.0
 */

public class QsHardPlaneMagnet implements CctFactory.MagnetAble {
    //硬边模型长度 m
    private double length_m;
    //梯度 四极场
    private double gradient_T_per_m;
    //二阶梯度 六极场
    private double second_gradient_T_per_m2;
    // 半孔径 mm
    private double aperture_radius_mm;

    //位置，以 磁铁入口处、中心点作为 位置
    //                  ------------------------------------------
    //                  |                                        |
    // 这里就是位置点  ->|                                        |
    //                  |                                        |
    //                  ------------------------------------------
    private Point2 location;

    //朝向，以入口指向出口的方向作为朝向
    //也是理想粒子的运动方向
    //如下图磁铁内的矢量
    //                  ------------------------------------------
    //                  |                                        |
    // 这里就是位置点  ->|              ------ >>                 |
    //                  |                                        |
    //                  ------------------------------------------
    private Vector2 direct; // 已归一化

    @Override
    public Vector3 magnetAt(Point3 p) {
        double k = projectToAxisK(p.toPoint2());

        if (k < 0 || k > length_m) {
            // 在 QS 铁之外
            return Vector3.getZero();
        } else {
            //在 QS 之内
            Point2 project2 = projectToAxisPoint(p.toPoint2());
            Point3 project3 = project2.toPoint3();
            Vector3 relative = Vector3.from(project3).to(p);
            double y = relative.z;
            double x;
            switch (isOnTheAxisRight(p.toPoint2())){
                case 0 -> x=0;
                case 1-> x = Vector2.create(relative.x, relative.y).length();
                case -1 -> x=-Vector2.create(relative.x, relative.y).length();
                default -> throw new RuntimeException("isOnTheAxisRight");
            }

            double By_quad = -gradient_T_per_m * x;
            double Bx_quad = -gradient_T_per_m * y;

            double By_sext = second_gradient_T_per_m2 * x * x;
            double Bx_sext = second_gradient_T_per_m2 * y * y;

            Vector3 By = Vector3.getZDirect().dot(By_quad + By_sext);
            Vector3 Bxy = direct.copy().rotateSelf(-Math.PI / 2).toVector3().dot(Bx_quad + Bx_sext);

            return By.addSelf(Bxy);
        }
    }


    /**
     * 二维点 P 投影到 QS 磁铁 中轴线上 点p1
     * p1 的表示方法如下：
     * p1 = this.location + k * this.direct
     * 因此用 k 表示 点 p1
     * 即返回 k
     * 例如： k = 0 表示 投影点即 this.location
     *
     * @param p 二维点
     * @return 投影点的 k 值，解释见上
     */
    public double projectToAxisK(Point2 p) {
        // p1 = this.location + k * this.direct
        // 求 k 使得 |p-p1| -> min
        // 计算可知 k = direct · (p - location)

        return direct.dot(Vector2.subtract(p.toVector2(), this.location.copy().toVector2()));
    }

    /**
     * p 投影到 QS 磁铁 中轴线上 点p1
     *
     * @param p 投影点
     * @return p1
     */
    public Point2 projectToAxisPoint(Point2 p) {
        return this.location.toVector2().add(this.direct.dot(projectToAxisK(p))).toPoint2();
    }

    /**
     * 二维点 p 到 QS 磁铁 中轴线 的距离
     *
     * @param p 二维点
     * @return 距离
     */
    public double distanceToAxis(Point2 p) {
        Point2 project = projectToAxisPoint(p);
        return Point2.distance(p, project);
    }

    /**
     * 查看二维的 点p 在 QS 磁铁中轴线的左边还是右边，还是在轴线上
     *
     * @param p 二维点
     * @return 1 for right, -1 for left and 0 for on the axis
     */
    public int isOnTheAxisRight(Point2 p) {
        if (isOnAxis(p))
            return 0;

        Vector2 r = Vector2.from(location).to(p);
        Vector2 right = direct.copy().rotateSelf(-Math.PI / 2);
        if (r.dot(right) > 0)
            return 1;
        else
            return -1;
    }

    /**
     * 二维点p是否在QS中轴线上
     *
     * @param p 二维点
     * @return 是否在QS中轴线上
     */
    public boolean isOnAxis(Point2 p) {
        return distanceToAxis(p) <= 1e-8;
    }


    public QsHardPlaneMagnet(double length_m, double gradient_T_per_m,
                             double second_gradient_T_per_m2, double aperture_radius_mm,
                             Point2 location, Vector2 direct) {
        this.length_m = length_m;
        this.gradient_T_per_m = gradient_T_per_m;
        this.second_gradient_T_per_m2 = second_gradient_T_per_m2;
        this.aperture_radius_mm = aperture_radius_mm;
        this.location = location;
        this.direct = direct.copy().normalSelf();
    }

    public static QsHardPlaneMagnet create(double length_m, double gradient_T_per_m,
                                           double second_gradient_T_per_m2, double aperture_radius_mm,
                                           Point2 location, Vector2 direct) {
        return new QsHardPlaneMagnet(length_m, gradient_T_per_m, second_gradient_T_per_m2, aperture_radius_mm, location, direct);
    }

}
