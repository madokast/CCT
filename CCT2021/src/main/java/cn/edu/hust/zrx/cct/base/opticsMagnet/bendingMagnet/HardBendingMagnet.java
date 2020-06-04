package cn.edu.hust.zrx.cct.base.opticsMagnet.bendingMagnet;

import cn.edu.hust.zrx.cct.Logger;
import cn.edu.hust.zrx.cct.base.BaseUtils;
import cn.edu.hust.zrx.cct.base.cct.CctFactory;
import cn.edu.hust.zrx.cct.base.line.ArcLine;
import cn.edu.hust.zrx.cct.base.point.Point2;
import cn.edu.hust.zrx.cct.base.point.Point3;
import cn.edu.hust.zrx.cct.base.python.Plot2d;
import cn.edu.hust.zrx.cct.base.vector.Vector2;
import cn.edu.hust.zrx.cct.base.vector.Vector3;

import java.util.List;

/**
 * Description
 * HardBendingMagnet
 * 硬边弯铁
 * <p>
 * Data
 * 11:44
 *
 * @author zrx
 * @version 1.0
 */

public class HardBendingMagnet implements CctFactory.MagnetAble {

    /**
     * 起点。二维
     */
    private final Point2 startingPoint;

    /**
     * 起点方向。二维
     */
    private final Vector2 startingDirect;

    /**
     * 是否是右偏转
     */
    private final boolean rightBending;

    /**
     * 角度
     */
    private final double angle;

    /**
     * 弧度
     */
    private final double radian;

    /**
     * 半径
     */
    private final double r;

    /**
     * 磁钢度
     */
    private final double Bp;

    /**
     * 二极场（有符号）
     * 符号：By其实是磁场 z 方向投影，因此 正数 表示 指向 z 正方向
     */
    private final double By;

    /**
     * 四极场系数
     */
    private final double n1;

    /**
     * 四极场梯度
     * T = -n1 * By / r
     */
    private final double gradient;

    /**
     * 六极场系数
     */
    private final double n2;

    /**
     * 六极场梯度
     * L = n2 * By / (r^2)
     */
    private final double secondGradient;

    /**
     * 圆心
     */
    private final Point2 center;

    /**
     * 终点 出口位置
     */
    private final Point2 endingPoint;

    /**
     * 圆心到起点的矢量
     */
    private final Vector2 center2StartingPoint;

    /**
     * 圆心到终点的矢量
     */
    private final Vector2 center2EndingPoint;

    public static HardBendingMagnet create(Point2 startingPoint, Vector2 startingDirect,
                                           boolean rightBending, double angle, double r, double Bp, double n1, double n2) {
        BaseUtils.Equal.requireTrue(angle > 0, "角度应为正值");
        BaseUtils.Equal.requireTrue(r > 0, "半径应为正值");
        BaseUtils.Equal.requireTrue(Bp > 0, "磁钢度应为正值");


        return new HardBendingMagnet(startingPoint, startingDirect, rightBending, angle, r, Bp, n1, n2);
    }

    private HardBendingMagnet(Point2 startingPoint, Vector2 startingDirect,
                              boolean rightBending, double angle, double r, double Bp, double n1, double n2) {
        this.startingPoint = startingPoint;
        this.startingDirect = startingDirect;
        this.rightBending = rightBending;
        this.angle = angle;
        this.r = r;
        this.Bp = Bp;
        this.n1 = n1;
        this.n2 = n2;

        if (rightBending) {
            this.By = this.Bp / r;
        } else {
            this.By = -this.Bp / r;
        }

        this.gradient = -n1 * By / r;

        /**
         * |||||||||||||||||| |||||||||||||||||||||||
         * 奇怪的加了个负号
         */
        this.secondGradient = - n2 * By / (r * r);

        this.radian = BaseUtils.Converter.angleToRadian(angle);

        if (rightBending) {
            Vector2 right = startingDirect.copy().rotateSelf(BaseUtils.Converter.angleToRadian(-90)).changeLengthSelf(r);
            this.center = startingPoint.copy().moveSelf(right);
        } else {
            Vector2 right = startingDirect.copy().rotateSelf(BaseUtils.Converter.angleToRadian(90)).changeLengthSelf(r);
            this.center = startingPoint.copy().moveSelf(right);
        }

        this.center2StartingPoint = Vector2.from(center).to(startingPoint);

        if (rightBending) {
            this.center2EndingPoint = this.center2StartingPoint.copy().rotateSelf(BaseUtils.Converter.angleToRadian(-angle));
        } else {
            this.center2EndingPoint = this.center2StartingPoint.copy().rotateSelf(BaseUtils.Converter.angleToRadian(angle));
        }

        this.endingPoint = center.copy().moveSelf(center2EndingPoint);
    }


    @Override
    public Vector3 magnetAt(Point3 p) {
        Point2 p2 = p.toPoint2();

        if (isInTheMagnet(p2)) {
            double y = p.z;
            double x = distance2centerLine(p2);

            Vector2 x_direct = Vector2.from(center).to(p2).changeLengthSelf(1);


            double By_quad = gradient * x;
            double Bx_quad = gradient * y;

            double By_sext = secondGradient * (x * x - y * y);

            double Bx_sext = secondGradient * (2 * x * y);


            Vector3 By = Vector3.getZDirect().dot(By_quad + By_sext);
            Vector3 Bxy = x_direct.toVector3().dot(Bx_quad + Bx_sext);

            return By.addSelf(Bxy).addSelf(Vector3.getZDirect().dot(this.By));

        } else
            return Vector3.getZero();
    }

    /**
     * 点 p 是否在磁铁内
     *
     * @param p 任意点 p
     * @return 是否在磁铁内
     */
    public boolean isInTheMagnet(Point2 p) {
        Vector2 center2P = Vector2.from(center).to(p);

        double angle2Starting = center2P.angleTo(center2StartingPoint);
        double angle2Ending = center2P.angleTo(center2EndingPoint);

//        Logger.getLogger().info("angle2Starting = " + angle2Starting);
//        Logger.getLogger().info("angle2Ending = " + angle2Ending);
//        Logger.getLogger().info("radian = " + radian);

        return Math.abs((angle2Ending + angle2Starting) - radian) <= 1e-6; // 误差千分之1度以下
    }


    /**
     * 点 p 到磁铁中轴线的距离
     * 如果在圆弧外，则返回正数
     * 在圆弧外，则返回负数
     * 此方法不判断点 p 是否在磁铁内部
     *
     * @param p 任一点
     * @return 点 p 到磁铁中轴线的距离
     */
    public double distance2centerLine(Point2 p) {
        double distance2center = Point2.distance(p, center);

        return distance2center - r;
    }


    public void plot2(String describing) {
        ArcLine arcLine = new ArcLine(startingPoint, startingDirect, r, rightBending, angle);

        arcLine.plot2d(1e-3, describing);

        List<Point2> center2s = List.of(this.center, startingPoint);

        List<Point2> center2e = List.of(this.center, endingPoint);

        Plot2d.plot2(center2s, describing);
        Plot2d.plot2(center2e, describing);
    }
}
