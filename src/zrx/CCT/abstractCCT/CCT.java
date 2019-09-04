package zrx.CCT.abstractCCT;

import zrx.base.Constants;
import zrx.base.point.Vector3d;

/**
 * CCT 抽象类
 * 注意线圈 = 路径，是一个从起点出发到达终点的动态过程
 * 线圈位置 即绕制 th 弧度时，当前所处位置
 */

public abstract class CCT {
    /**
     * 计算点 p0 到点 p1 电流为 I 的直导线在 p 点产生的磁场。
     * 使用 毕奥-萨伐尔定律。当p0 p1很近时，即为 dB，可用作数值积分 B = ∫dB
     * 还有十足的优化空间
     *
     * @param p0 直导线起点
     * @param p1 直导线终点
     * @param I  电流，从 p0 流向 p1
     * @param p  需要计算磁场的点
     * @return p 点的磁场
     */
    final public static Vector3d dB(final Vector3d p0, final Vector3d p1, double I, final Vector3d p) {
        Vector3d p01 = Vector3d.subtract(p1, p0);
        Vector3d r = Vector3d.subtract(p, p0);
        double rr = r.length();

        return Vector3d.dot(Vector3d.corss(p01, r),
                I * 1e-7 / rr / rr / rr);
    }

    /**
     * 获得线圈匝数
     *
     * @return 匝数
     */
    public abstract int getN();

    /**
     * 设置线圈匝数
     *
     * @param N 匝数
     */
    public abstract void setN(int N);

    /**
     * 获得步长，步长决定计算磁场时的精度
     * 单位 弧度
     *
     * @return 步长
     */
    public abstract double getStep();

    /**
     * 线圈一圈上分为多少个点。即2pi/step
     * 但是为了防止double强制转为int出现问题
     * 所以要求实现类明确计算出这个值
     *
     * @return 线圈一圈上分为多少个点
     */
    public int getPointPerCircle(){
        return (int) Math.round(2 * Math.PI / Math.abs(getStep()));
    }

    /**
     * 就是 return getN()*getPointPerCircle();
     * 但是不希望每次都计算
     * 务必第二次以后的调用，免去计算过程!
     *
     * @return getN()*getPointPerCircle()
     */
    public abstract int getTotalNumber();

    /**
     * 起点位置的 Theta
     * 很重要的量
     * 还好及时重构
     * 在AG-CCT中万分重要
     *
     * @return 起点的θ
     */
    public abstract double getStartTheta();

    /**
     * 获得结束点的 Theta
     * 用于此cct结束，准备接下一段cct
     * 专用于 AG-CCT 建模
     *
     * @return 终点的θ
     */
    public double getEndTheta() {
        return getStartTheta() + getStep() * getTotalNumber();
    }

    /**
     * 获得电流
     *
     * @return 电流
     */
    public abstract double getI();

    /**
     * 获得线圈 th 时位置
     *
     * @param th 决定线圈位置的唯一自变量
     * @return 线圈位置
     */
    public abstract Vector3d point(double th);

    /**
     * 设置步长
     * 步长 > 0 时，目光方向和线圈轴向平行，则此时线圈逆时针缠绕
     * 1. 改变计算精度
     * 2. 可以用于CCT运动方向的反向 即setStep(-getStep())。
     * 请使用reverse()
     *
     * @param step 步长
     */
    public abstract void setStep(double step);

    /**
     * 改变step的符号
     * 这会导致线圈绕制方向 逆时针/顺时针 的切换
     */
    @Deprecated
    public void reverse() {
        setStep(-getStep());
    }

    /**
     * 计算 p 点磁场
     *
     * @param p 磁场
     * @return p 点磁场
     */
    @Deprecated
    public Vector3d magnetDeprecated(Vector3d p) {
        Vector3d B = new Vector3d(0, 0, 0);
        double I = getI();
        double step = getStep();
        int number = getTotalNumber();

        Vector3d p0 = point(0.0 + getStartTheta());
        Vector3d p1 = null;


        for (int i = 1; i <= number; i++) {
            p1 = point(i * step + getStartTheta());
            B.addSelf(dB(p0, p1, I, p));
            p0 = p1;
        }

        return B;
    }

    /**
     * 获得CCT上从起点到终点的一系列点，用于画图
     * 点间距为 step
     *
     * @return 一系列点
     */
    @Deprecated
    public Vector3d[] pathForPlot3() {
        int number = getTotalNumber();
        double step = getStep();
        Vector3d[] vs = new Vector3d[number + 1];

        for (int i = 0; i <= number; i++) {
            vs[i] = point(i * step + getStartTheta());
        }

        return vs;
    }

    /**
     * 起点
     *
     * @return 起始点
     */
    @Deprecated
    public Vector3d getStartPoint() {
        return point(0.0 + getStartTheta());
    }

    /**
     * 起点方向 已正则归一化
     *
     * @return 起点方向
     */
    @Deprecated
    public Vector3d getStartDirect() {
        Vector3d p0 = point(0.0 + getStartTheta());
        Vector3d p1 = point(0.0 + getStartTheta() + Math.PI / 180.0 * Constants.DX);

        p1.subtractSelf(p0);
        p1.normal();
        return p1;
    }

    /**
     * 终点方向 已正则归一化
     *
     * @return 终点方向
     */
    @Deprecated
    public Vector3d getEndDirect() {
        Vector3d p0 = point(getTotalNumber() * getStep() + getStartTheta());
        Vector3d p1 = point(getTotalNumber() * getStep() + getStartTheta() + Math.PI / 180.0 * Constants.DX);

        p1.subtractSelf(p0);
        p1.normal();
        return p1;
    }

    /**
     * 终点
     *
     * @return 线圈终点
     */
    @Deprecated
    public Vector3d getEndPoint() {
        return point(getTotalNumber() * getStep() + getStartTheta());
    }


}
