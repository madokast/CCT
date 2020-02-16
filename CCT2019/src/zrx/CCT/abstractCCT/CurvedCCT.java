package zrx.CCT.abstractCCT;

import zrx.Tools.Equal;
import zrx.base.Constants;
import zrx.base.point.Vector2d;
import zrx.base.point.Vector3d;

/**
 * 弯曲CCT
 */

@SuppressWarnings("all")
public class CurvedCCT extends CCT {
    /**
     * :param a: 双极坐标系 极点位置
     * :param eta: 即η 确定了线圈半径，位置
     * :param phi0: 和线圈间距有关的量
     * :param n: 匝数
     * :param I: 电流
     * :param tiltAngle: 倾斜角
     * :param nth: 1：二极场，2：四极场
     * :param stepKsi: 步长
     *
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

    //以下为辅助量
    private int totalNumber; //即 n*2*pi/stepTheta 为了防止每次计算，所以作为属性，只计算一次
    private double startKsi; // 绕线开始点ksi0，弧度制，为了以后的需要。
    private double startPhi; // 注意这个值和φ0的区别。 φ0为绕制一周时，phi变化量。startPhi为起始点的phi值
    private int antiClockwise; // 取值1/-1  若为 1 表示绕线方向为逆时针。
    // 所谓逆时针。指的是目光和线圈轴向生长方向平行，此时线圈的绕制方向

    private double cn;//辅助量

    private double R; // 弯曲CCT半径 注意不是线圈半径
    private double r; // 线圈半径

    /**
     * 复制cct 返回新的cct。和原cct一样
     *
     * @param cct 原cct
     * @return 复制值
     */
    private static CurvedCCT copy(final CurvedCCT cct) {
        CurvedCCT copied = new CurvedCCT();
        copied.a = cct.a;
        copied.eta = cct.eta;
        copied.phi0 = cct.phi0;
        copied.n = cct.n;
        copied.I = cct.I;
        copied.tiltAngle = cct.tiltAngle;
        copied.nth = cct.nth;
        copied.stepKsi = cct.stepKsi;

        copied.totalNumber = cct.totalNumber;
        copied.startKsi = cct.startKsi;
        copied.startPhi = cct.startPhi;
        copied.antiClockwise = cct.antiClockwise;

        copied.cn = cct.cn;

        copied.R = cct.R;
        copied.r = cct.r;

        return copied;
    }

    private CurvedCCT() {
    }

    public double getA() {
        return a;
    }

    public double getEta() {
        return eta;
    }

    public double getNth() {
        return nth;
    }

    public double getStepKsi() {
        return stepKsi;
    }

    public double getStartKsi() {
        return startKsi;
    }

    public int getAntiClockwise() {
        return antiClockwise;
    }

    public double getCn() {
        return cn;
    }

    public double getR() {
        return R;
    }

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
        this.R = this.a / Math.tanh(this.eta);
        // 线圈半径
        this.r = this.a / Math.sinh(this.eta);
        //辅助量
        this.cn = 1.0 / (Math.tan(this.tiltAngle) * this.nth * Math.sinh(this.eta));
        this.antiClockwise = (this.stepKsi > 0) ? 1 : -1;

        this.totalNumber = getPointPerCircle() * n;
        this.startKsi = 0.0;
        this.startPhi = 0.0; //注意这个值和φ0的区别。 φ0为绕制一周时，phi变化量。

    }

    @Deprecated
    public void setStartKsi(double startPoint) {
        this.startKsi = startPoint;
        this.cn = 1.0 / (Math.tan(this.tiltAngle) * this.nth * Math.sinh(this.eta));
        this.startPhi = cn * Math.sin(this.nth * startKsi) + this.phi0 * startKsi / (2.0 * Math.PI);
    }

    public void setStartPhi(double startPhi) {
        this.startPhi = startPhi;
    }

    public double getStartPhi() {
        return startPhi;
    }

    /**
     * 优秀函数
     *
     * @return 结束点的phi值
     */
    public double getEndPhi() {
//        double phi = this.startPhi + dphi * this.antiClockwise;
//        double dphi = cn * Math.sin(this.nth * dksi) + this.phi0 * dksi / (2.0 * Math.PI);

        // ksi 相对变化量
        double dksi = getTotalNumber() * getStep();
        double dphi = cn * Math.sin(this.nth * dksi) + this.phi0 * dksi / (2.0 * Math.PI);

        return this.startPhi + dphi * this.antiClockwise;
    }

    @Override
    public int getN() {
        return this.n;
    }

    @Override
    public void setN(int N) {
        this.n = N;
        this.totalNumber = getPointPerCircle() * this.n;
    }

    @Override
    public double getStep() {
        return this.stepKsi;
    }

    @Override
    @Deprecated
    public void setStep(double stepKsi) {
        this.stepKsi = stepKsi;
        this.antiClockwise = (this.stepKsi > 0) ? 1 : -1;
    }

    @Override
    public int getTotalNumber() {
        return totalNumber;
    }

    /**
     * 为什么是 θ 而不是 ξ？
     * 好问题，因为直线cci中，使用的直圆柱坐标系中，用的是θ
     * 因此在直线CCT 和 直线螺线管中都是这个名字
     * 在弯曲cct中，使用的圆环坐标系(弯曲圆柱坐标系)，使用的是ξ
     * 随意吧，都差不多，描述缠绕角度而已呢
     *
     * @return θ 或 ξ
     */
    @Override
    public double getStartTheta() {
        return startKsi;
    }

    @Override
    public double getI() {
        return I;
    }

    /**
     * 作废!
     * 被 二维=三维 变换替代
     *
     * @param ksi 自变量
     * @return ksi对应的三维xyz空间的点
     */
    @Override
    @Deprecated
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

        // 相对于开始点ξ的变化量。注意有正负。
        double dksi = ksi - this.startKsi;

        // 确定φ   dphi 为phi变化量
        // 注意里面的 antiClockwise 若为逆时针(antiClockwise==1)，且ksi>0，是默认情况。
        // 当二者有一个不默认情况时，线圈的轴向生长方向反向，相当于往回绕线
        // 当二者都不是默认值时，线圈绕制方向为顺时针，但是在轴向上依旧往前生长。
        double dphi = this.cn * Math.sin(this.nth * dksi) + this.phi0 * dksi / (2.0 * Math.PI);
        double phi = this.startPhi + dphi * this.antiClockwise;

        // 变换到xyz直角坐标系
        double x = Math.cos(phi) * dx;
        double y = Math.sin(phi) * dx;
        double z = dy;

        return new Vector3d(x, y, z);
    }

    @Deprecated
    public void setTiltAngle(double tiltAngle) {
        this.tiltAngle = tiltAngle;
        //辅助量
        // 重大bug发现!
        // 2019年7月6日
        this.cn = 1.0 / (Math.tan(this.tiltAngle) * this.nth * Math.sinh(this.eta));
    }

    public double getTiltAngle() {
        return this.tiltAngle;
    }

    public void setPhi0(double phi0) {
        this.phi0 = phi0;
    }

    public double getPhi0() {
        return phi0;
    }

    /**
     * 将 CCT 反向绕线延长
     * 专用于AG-CCT建模
     * 注意是在传入的cct上修改
     * 一般般 能用——不 是优秀代码
     *
     * @param cct 原来的 CCT
     */
    public static CurvedCCT reverseWinding(final CurvedCCT cct) {
        CurvedCCT newCct = CurvedCCT.copy(cct);

        double endPhi = newCct.getEndPhi();
        double endKsi = newCct.getEndTheta();
        double step = -newCct.stepKsi;
        double tiltAngle = -newCct.tiltAngle;
        double phi0 = -newCct.phi0;

        newCct.startKsi = endKsi;

        //这个代码写得非常差 2019年7月6日
        //应该先判断当前CCT的phi增长方向，然后确定是增加还是减少
        //2019年7月7日
        //step 正 phi 正 -> 右上方行走
        //step 负 phi 正 -> 左下方行走
        //step 负 phi 负 -> 左上方行走
        //step 正 phi 负 -> 右下方行走
        newCct.setStartPhi(endPhi + Math.abs(newCct.phi0));

        newCct.stepKsi = step;
        newCct.tiltAngle = tiltAngle;
        newCct.phi0 = phi0;

        return newCct;


//        cct.setStartKsi(cct.getEndTheta());
//        cct.setStartPhi(endPhi + cct.getPhi0());
//        cct.setStep(-cct.getStep());
//        cct.setTiltAngle(-cct.getTiltAngle());
    }


    /**
     * 历史将要改写。2019年7月6日 今日清晨加上前一天晚上，在寝室思考时的突然发现!!
     * 画出弯曲 CCT 在(ξ,φ)坐标系下的图形!!
     * 这里获得坐标数据
     *
     * @return (ξ, φ)坐标系下 弯曲cct路径
     */
    public Vector2d[] pointsOnKsiPhiCoordinateSystem() {
        Vector2d[] vs = new Vector2d[totalNumber + 1];

        for (int i = 0; i < totalNumber + 1; i++) {
            //相对变化量
            double dksi = i * stepKsi;
            double dphi = cn * Math.sin(this.nth * dksi) + this.phi0 * dksi / (2.0 * Math.PI);
            //绝对量
            double ksi = dksi + this.startKsi;
            double phi = this.startPhi + dphi * this.antiClockwise;
            vs[i] = new Vector2d(ksi, phi);
        }

        return vs;
    }

    /**
     * 对二极场CCT经行四极场修正。函数主要功能见 pointsOnKsiPhiCoordinateSystem
     *
     * @param B4 加入的四极场分量
     * @return (ξ, φ)坐标系下 弯曲cct路径
     */
    public Vector2d[] pointsOnKsiPhiCoordinateSystemWithFixAtDipole(double B4) {
        if (!Equal.isEqual(this.nth, 1.0)) {
            System.err.println("不允许对非二级CCT经行四极场修正");
        }

        //计算这个CCT的二极场大小
        double w = this.phi0 * this.R;//w-绕一周前进量
        double B2 = -Constants.Miu0 * this.I / (2.0 * w * Math.tan(this.tiltAngle));

        Vector2d[] vs = new Vector2d[totalNumber + 1];

        for (int i = 0; i < totalNumber + 1; i++) {
            //相对变化量
            double dksi = i * stepKsi;
            double dphi = cn * Math.sin(this.nth * dksi) -/*增加项*/
                    (B4 / (2.0 * B2 * Math.sinh(this.eta))) * this.cn * Math.sin(2.0 * dksi)
                    + this.phi0 * dksi / (2.0 * Math.PI);
            //绝对量
            double ksi = dksi + this.startKsi;
            double phi = this.startPhi + dphi * this.antiClockwise;
            vs[i] = new Vector2d(ksi, phi);
        }

        return vs;
    }

    /**
     * 坐标变换 (ξ,φ) to (x, y, z)
     *
     * @param v2 圆环坐标系 坐标 (ξ,φ)
     * @return 空间直角坐标系 坐标 (x, y, z)
     */
    public Vector3d coordinateSystemTransformateFromKsiPhiToXYZ(Vector2d v2) {
        //提取ksi phi
        double ksi = v2.x;
        double phi = v2.y;

        // 首先确定双极坐标系(dx, dy)中位置
        double k = Math.cosh(this.eta) - Math.cos(ksi);
        double dx = this.a * Math.sinh(this.eta) / k;
        double dy = this.a * Math.sin(ksi) / k;

        // 变换到xyz直角坐标系
        double x = Math.cos(phi) * dx;
        double y = Math.sin(phi) * dx;
        double z = dy;

        return new Vector3d(x, y, z);
    }

    /**
     * 坐标变换 (ξ,φ) to (x, y, z)
     *
     * @param v2s 圆环坐标系 坐标 (ξ,φ)
     * @return 空间直角坐标系 坐标 (x, y, z)
     */
    public Vector3d[] coordinateSystemTransformateFromKsiPhiToXYZ(Vector2d[] v2s) {
        int len = v2s.length;
        Vector3d[] v3s = new Vector3d[len];
        for (int i = 0; i < len; i++) {
            v3s[i] = coordinateSystemTransformateFromKsiPhiToXYZ(v2s[i]);
        }

        return v3s;
    }


    /**
     * 作废。耦合过深
     * 坐标变换 (ξ,φ) to (x, y, z)
     * 用于验证
     */
    @Deprecated
    public Vector3d[] coordinateSystemTransformateFromKsiPhiToXYZ_OLD(Vector2d[] v2s) {
        int len = v2s.length;
        Vector3d[] v3s = new Vector3d[len];

        for (int i = 0; i < len; i++) {
            double ksi = v2s[i].x;
            double phi = v2s[i].y;

            // 首先确定双极坐标系(dx, dy)中位置
            double k = Math.cosh(this.eta) - Math.cos(ksi);
            double dx = this.a * Math.sinh(this.eta) / k;
            double dy = this.a * Math.sin(ksi) / k;

            // 变换到xyz直角坐标系
            double x = Math.cos(phi) * dx;
            double y = Math.sin(phi) * dx;
            double z = dy;

            v3s[i] = new Vector3d(x, y, z);
        }

        return v3s;
    }

    /**
     * @return 得到(ξ, φ)坐标系中的CCT开始点
     */
    public Vector2d getStartPointInKsiPhiCoordinateSystem() {
        return new Vector2d(getStartTheta(), getStartPhi());
    }

    /**
     * @return 得到(ξ, φ)坐标系中的CCT结束点
     */
    public Vector2d getEndPointInKsiPhiCoordinateSystem() {
        return new Vector2d(getEndTheta(), getEndPhi());
    }

    public Vector2d getStartDirectInKsiPhiCoordinateSystem() {
        //求导
        double dksi = 1.0;
//        double dphi = cn * Math.sin(this.nth * dksi) + this.phi0 * dksi / (2.0 * Math.PI);
        double dphi = this.cn * this.nth * Math.cos(this.nth * getStartTheta()) + this.phi0 / (2.0 * Math.PI);

        Vector2d d = new Vector2d(dksi, dphi).normalSelfAndReturn();

        if (this.stepKsi > 0)
            return d;
        else
            return d.reverseSelfAndReturn();
    }

    public Vector2d getEndDirectInKsiPhiCoordinateSystem() {
        //求导
        double dksi = 1.0;
//        double dphi = cn * Math.sin(this.nth * dksi) + this.phi0 * dksi / (2.0 * Math.PI);
        double dphi = this.cn * this.nth * Math.cos(this.nth * getEndTheta()) + this.phi0 / (2.0 * Math.PI);

        Vector2d d = new Vector2d(dksi, dphi).normalSelfAndReturn();

        if (this.stepKsi > 0)
            return d;
        else
            return d.reverseSelfAndReturn();
    }

    @Override
    public String toString() {
        double w = this.phi0 * this.R;
        if (Equal.isEqual(nth, 1.0)) {
            double B = -Constants.Miu0 * this.I / (2.0 * w * Math.tan(this.tiltAngle));
            return "这是个单层二极CCT，理想二极场为 " + B + "T";
        } else if (Equal.isEqual(nth, 2.0)) {
            double B2 = -Constants.Miu0 * this.I / (2.0 * w * Math.tan(this.tiltAngle) * this.r);
            return "这是个单四极CCT，理想梯度为 " + B2 + "T";
        }

        return "不知道什么东西";
    }
}
