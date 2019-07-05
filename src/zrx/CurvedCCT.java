package zrx;

import zrx.base.Vector3d;

/**
 * 弯曲CCT
 */

public class CurvedCCT extends CCT{
    /**
     *:param a: 双极坐标系 极点位置
     *:param eta: 即η 确定了线圈半径，位置
     *:param phi0: 和线圈间距有关的量
     *:param n: 匝数
     *:param I: 电流
     *:param tiltAngle: 倾斜角
     *:param nth: 1：二极场，2：四极场
     *:param stepKsi: 步长
     * @return
     */
    private double a;
    private double eta;
    private double phi0;
    private int n;
    private double I;
    private double tiltAngle;
    private double nth;
    private double stepKsi;

    private int totalNumber; //即 n*2*pi/stepTheta 为了防止每次计算，所以作为属性，只计算一次
    private double startPoint; // 绕线开始点，弧度制，为了以后的需要。

    private double R;
    private double r;


    public CurvedCCT(double a, double eta, double phi0, int n, double i, double tiltAngle, double nth, double stepKsi) {
        this.a = a;
        this.eta = eta;
        this.phi0 = phi0;
        this.n = n;
        I = i;
        this.tiltAngle = tiltAngle;
        this.nth = nth;
        this.stepKsi = stepKsi;

        // 弯曲CCT半径 注意不是线圈半径
        this.R = this.a/Math.tanh(this.eta);
        // 线圈半径
        this.r = this.a/Math.sinh(this.eta);

        this.totalNumber = getPointPerCircle()*n;
        this.startPoint = 0.0;

    }

    public void setStartPoint(double startPoint) {
        this.startPoint = startPoint;
    }

    @Override
    public int getN() {
        return this.n;
    }

    @Override
    public double getStep() {
        return this.stepKsi;
    }

    @Override
    public int getPointPerCircle() {
        return (int)Math.round(2*Math.PI/this.stepKsi);
    }

    @Override
    public int getTotalNumber() {
        return totalNumber;
    }

    @Override
    public double getStartPoint() {
        return startPoint;
    }

    @Override
    public double getI() {
        return I;
    }

    @Override
    public Vector3d point(double ksi) {
        //返回ξ位置 在笛卡尔坐标系中的坐标
        //ksi，即ξ，是弯曲CCT中的自变量
        //把线圈看作一条路径，ξ确定当前处于线圈在何处
        //:param ksi: 弯曲CCT自变量 决定了当前位置
        //:return: 笛卡尔坐标系中位置

        // 首先确定双极坐标系(dx, dy)中位置
        double k = Math.cosh(this.eta) - Math.cos(ksi);
        double dx = this.a * Math.sinh(this.eta) / k;
        double dy = this.a * Math.sin(ksi) / k;

        // 确定φ
        double cn = 1.0 / (Math.tan(this.tiltAngle) * this.nth * Math.sinh(this.eta));
        double phi = cn * Math.sin(this.nth * ksi) + this.phi0 * ksi / (2.0 * Math.PI);

        // 变换到xyz直角坐标系
        double x = Math.cos(phi) * dx;
        double y = Math.sin(phi) * dx;
        double z = dy;

        return new Vector3d(x,y,z);
    }
}
