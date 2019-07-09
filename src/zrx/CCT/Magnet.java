package zrx.CCT;

import zrx.base.Vector3d;

import java.util.logging.Level;

public class Magnet {
    /**
     * 计算由 Vector3d[] v3s 描述的轨迹线圈在点 p 产生的磁场
     * 电流方向为 v3s[i]->v3s[i+1] 电流大小为 I 。若 I 为负数，当人和上述方向相反
     * <p>
     * 注意这里不确认 Vector3d[] v3s 是否描述的是一段轨迹，函数仅仅调用 Magnet.dB 进行积分式的计算
     * 可以通过 Plot3d.plot3(v3s) 绘图哟~
     *
     * @param v3s 轨迹(不确认)
     * @param I   电流
     * @param p   需要求磁场的点
     * @return P 点的磁场
     */
    final public static Vector3d magnetAtPoint(final Vector3d[] v3s, final double I, final Vector3d p) {
        //用于累加磁场
        Vector3d B = Vector3d.getZeros();

        //获得段数
        int num = v3s.length - 1;

        for (int i = 0; i < num; i++) {
            B.addSelf(dB(v3s[i], v3s[i + 1], I, p));
        }

        return B;
    }


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
    final private static Vector3d dB(final Vector3d p0, final Vector3d p1, double I, final Vector3d p) {
        Vector3d p01 = Vector3d.subtract(p1, p0);
        Vector3d r = Vector3d.subtract(p, p0);
        double rr = r.length();

        return Vector3d.dot(Vector3d.corss(p01, r),
                I * 1e-7 / rr / rr / rr);
    }

    //以下为一些封装方法
}
