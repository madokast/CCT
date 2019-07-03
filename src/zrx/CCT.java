package zrx;

import com.sun.source.tree.CompilationUnitTree;
import zrx.base.Vector3d;

import java.util.function.DoubleBinaryOperator;

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
                I * 1e-7 * rr * rr * rr);
    }

    /**
     * 获得线圈匝数
     *
     * @return 匝数
     */
    public abstract int getN();

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
    public abstract int getPointPerCircle();

    public abstract double getI();

    /**
     * 获得线圈 th 时位置
     *
     * @param th 决定线圈位置的唯一自变量
     * @return 线圈位置
     */
    public abstract Vector3d point(double th);

    /**
     * 计算 p 点磁场
     *
     * @param p 磁场
     * @return p 点磁场
     */
    public Vector3d magnet(Vector3d p) {
        Vector3d B = new Vector3d(0, 0, 0);
        double I = getI();
        double step = getStep();
        int number = getN() * getPointPerCircle();

        Vector3d p0 = point(0.0);
        Vector3d p1 = null;
        for (int i = 1; i <= number; i++) {
            p1 = point(i * step);
            B.addSelf(dB(p0, p1, I, p));
            p0 = p1;
        }

        return B;
    }
}