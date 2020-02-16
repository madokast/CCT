package cn.edu.hust.zrx.cct.base.cct;

import cn.edu.hust.zrx.cct.Logger;
import cn.edu.hust.zrx.cct.base.BaseUtils;
import cn.edu.hust.zrx.cct.base.point.Point2;
import cn.edu.hust.zrx.cct.base.point.Point2To3;
import cn.edu.hust.zrx.cct.base.point.Point3;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Description
 * 二维 CCT 线段
 * 实际上仅仅是描述 φ = f(θ) 关系
 * 详见简单圆环坐标系下CCT的描述方法
 * 文献 Superconducting toroidal combined-function magnet for a compact ion beam cancer therapy gantry
 * 来源 www.sciencedirect.com/science/article/pii/S0168900211017098
 * <p>
 * 简单圆环坐标系：
 * 一个弯曲的圆环
 * 圆环孔径/半径R（e.g. 60mm）
 * 圆环轴线的半径R0（即理想轨迹半径 e.g. 0.75m）
 * φ toroidal angle 圆环角，相当于直线圆环中的 Z
 * θ poloidal angle 极角，即每 2pi 绕圆环一周
 * <p>
 * φ = f(θ) = θ/n + Σ a(i)*sin( (i+1)*θ )
 * 其中：
 * n - 决定匝间距
 * a(0) 二极场
 * a(1) 四极场
 * a(2) 六极场
 *
 * <p>
 * Data
 * 17:33
 *
 * @author zrx
 * @version 1.0
 * @since 2020年2月10日
 */

public class CctLine2 {
    private final double smallR;
    private final double bigR;
    private final double angle;
    private final int windingNumber;
    private final double n;
    private final double a0Bipolar;
    private final double a1Quadruple;
    private final double a2Sextuple;

    private final double startingθ;
    private final double startingφ;
    private final boolean directθ;

    /**
     * 构造器
     *
     * @param smallR        小 r CCT 半径/孔径
     * @param bigR          大 R CCT 偏转半径
     * @param angle         CCT 角度，如 67.5 度 CCT，angle = 67.5
     * @param windingNumber 匝数
     * @param a0Bipolar     二极场系数
     * @param a1Quadruple   四极场系数
     * @param a2Sextuple    六极场系数
     * @since 2020年2月10日
     */
    public CctLine2(double smallR,
                    double bigR,
                    double angle,
                    int windingNumber,
                    double a0Bipolar,
                    double a1Quadruple,
                    double a2Sextuple,
                    double startingθ,
                    double startingφ,
                    boolean directθ) {
        this.smallR = smallR;
        this.bigR = bigR;
        this.angle = angle;
        this.windingNumber = windingNumber;
        this.a0Bipolar = a0Bipolar;
        this.a1Quadruple = a1Quadruple;
        this.a2Sextuple = a2Sextuple;

        this.startingθ = startingθ;
        this.startingφ = startingφ;
        this.directθ = directθ;

        double radian = BaseUtils.Converter.angleToRadian(this.angle);

        if (directθ) {
            this.n = 2 * Math.PI * this.windingNumber / radian;
        } else {
            this.n = -2 * Math.PI * this.windingNumber / radian;
        }

//        Logger.getLogger().info(this.toString());

    }

    /**
     * φ = f(θ) = θ/n + Σ a(i)*sin( (i+1)*θ )
     *
     * @param θ poloidal angle 极角，即每 2pi 绕圆环一周
     * @return (θ, φ)
     */
    private Point2 point2At(double θ) {
        double φ = startingφ
                + (θ - startingθ) / n
                + a0Bipolar * Math.sin(θ)
                + a1Quadruple * Math.sin(2 * θ)
                + a2Sextuple * Math.sin(3 * θ);

        return Point2.create(θ, φ);
    }

    /**
     * 离散
     * φ = f(θ) = θ/n + Σ a(i)*sin( (i+1)*θ )
     *
     * @param numberPerWinding 每匝分点数，如 360 表示 每 1度 分一个点，一匝 360 点
     * @return 离散后Point2[]，注意这还只是二维的，需要转换到三维
     */
    protected List<Point2> disperseToPoint2(int numberPerWinding) {
        final int totalNumber = numberPerWinding * windingNumber;
        final double Δθ = 2 * Math.PI / numberPerWinding;
//        Logger.getLogger().info("disperseToPoint2...run");
//        Logger.getLogger().info("totalNumber = " + totalNumber);
//        Logger.getLogger().info("Δθ = " + Δθ);

        return BaseUtils.StreamTools
                .fromZeroIncluding()
                .to(totalNumber)
                .mapToDouble(i ->
                        startingθ + (directθ ? 1 : -1) * ((double) i * Δθ)
                )
                .mapToObj(this::point2At)
                .collect(Collectors.toList());
    }

    public List<Point3> disperseToPoint3(int numberPerWinding){
        List<Point2> point2s = disperseToPoint2(numberPerWinding);

        return new Point2To3.SimpleToroidalCoordinateSystemPoint2To3(bigR,smallR)
                .convert(point2s);
    }


    @Override
    public String toString() {
        return "CctLine2{" +
                "smallR=" + smallR +
                ", bigR=" + bigR +
                ", angle=" + angle +
                ", windingNumber=" + windingNumber +
                ", n=" + n +
                ", a0Bipolar=" + a0Bipolar +
                ", a1Quadruple=" + a1Quadruple +
                ", a2Sextuple=" + a2Sextuple +
                ", startingθ=" + startingθ +
                ", startingφ=" + startingφ +
                ", directθ=" + directθ +
                '}';
    }

    public double getSmallR() {
        return smallR;
    }

    public double getBigR() {
        return bigR;
    }

    public double getAngle() {
        return angle;
    }

    public int getWindingNumber() {
        return windingNumber;
    }

    public double getN() {
        return n;
    }

    public double getA0Bipolar() {
        return a0Bipolar;
    }

    public double getA1Quadruple() {
        return a1Quadruple;
    }

    public double getA2Sextuple() {
        return a2Sextuple;
    }

    public double getStartingθ() {
        return startingθ;
    }

    public double getStartingφ() {
        return startingφ;
    }

    public boolean isDirectθ() {
        return directθ;
    }
}
