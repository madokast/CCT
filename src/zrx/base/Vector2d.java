package zrx.base;

import zrx.Tools.QuadraticEquationOfOneVariable;
import zrx.python.Plod2d;

public class Vector2d {
    public double x;
    public double y;

    public Vector2d(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * no robust!!
     * no robust!!
     * no robust!!
     * no robust!!
     * 函数不一定能找到解
     * <p>
     * 尝试寻找对两点的圆插值，得到圆的半径 r
     * 此插值中存在两个相外切的同半径圆，半径r
     * 圆0 过点 p0 ，且圆心在方向向量 v0 法向上
     * 圆1 过点 p1 ，且圆心在方向向量 v1 法向上
     * 具体圆心在 v0 的左侧还是右侧，由 clockwise0 和 clockwise1 确定
     * 例如，若 clockwise0 = true ，则让 v0 顺时针旋转90°，得到法线 n0 ，这样圆心在 v0 右侧 ( 目光向着 v0 正方向，此时的右侧)
     * 例如，若 clockwise0 = false ，则让 v0 逆时针旋转90°，得到法线 n0 ，这样圆心在 v0 左侧 ( 目光向着 v0 正方向，此时的左侧)
     * <p>
     * !!!注意，此函数不一定能找到解，尤其是 clockwise0 和 clockwise1 设置错误的时候
     * <p>
     * 图形化描述见 下 喵喵喵~~
     * <p><img src = "docs/CircularInterpolation.JPG" /><p/>
     *
     * @param p0         点0
     * @param v0         点0的方向，插值圆会和此方向相切
     * @param clockwise0 true 代表 圆心0在 v0 右侧，否则是左侧
     * @param p1         点1
     * @param v1         点1的方向，插值圆会和此方向相切
     * @param clockwise1 true 代表 圆心0在 v1 右侧，否则是左侧
     * @return 圆半径r
     */
    private static double solveCircularInterpolationRadius(Vector2d p0, Vector2d v0, boolean clockwise0, Vector2d p1, Vector2d v1, boolean clockwise1) {
        //旋转角度
        double phi0 = (clockwise0) ? -Math.PI / 2.0 : Math.PI / 2.0;
        double phi1 = (clockwise1) ? -Math.PI / 2.0 : Math.PI / 2.0;

        //旋转与归一化
        Vector2d n0 = Vector2d.rotate(p0, phi0).normalSlefAndReturn();
        Vector2d n1 = Vector2d.rotate(p1, phi1).normalSlefAndReturn();

        double[] solves = QuadraticEquationOfOneVariable.solve(
                0,
                0,
                0
        );//TODO


        return 0.0;
    }

    /**
     * 正则化 矢量长度归一
     */
    public final void normalSlef() {
        double len = length();
        double nx = this.x / len;
        double ny = this.y / len;

        this.x = nx;
        this.y = ny;
    }

    public final Vector2d normalSlefAndReturn() {
        this.normalSlef();
        return this;
    }

    public static Vector2d normal(final Vector2d v) {
        double len = v.length();
        double nx = v.x / len;
        double ny = v.y / len;

        return new Vector2d(nx, ny);
    }

    /**
     * 二维矢量长度
     *
     * @return 长度
     */
    public final double length() {
        return Math.sqrt(x * x + y * y);
    }

    /**
     * 矢量旋转。phi>0 ，逆时针旋转
     *
     * @param v   原矢量
     * @param phi 旋转弧度
     * @return 旋转后矢量
     */
    public static Vector2d rotate(final Vector2d v, final double phi) {
        return matmul(v, rotateR(phi));
    }

    /**
     * 旋转矩阵
     *
     * @param phi 旋转弧度
     * @return double[2][2]
     */
    private static double[][] rotateR(final double phi) {
        return new double[][]{
                {Math.cos(phi), -Math.sin(phi)},
                {Math.sin(phi), Math.cos(phi)}
        };
    }

    /**
     * 矩阵*矢量 A*v
     *
     * @param v 矢量
     * @param A 矩阵
     * @return 结果
     */
    public static Vector2d matmul(final Vector2d v, final double[][] A) {
        return new Vector2d(
                A[0][0] * v.x + A[0][1] * v.y,
                A[1][0] * v.x + A[1][1] * v.y
        );
    }

    public static Vector2d add(final Vector2d a, final Vector2d b) {
        return new Vector2d(a.x + b.x, a.y + b.y);
    }

    public static Vector2d dot(final double t, final Vector2d a) {
        return new Vector2d(a.x * t, a.y * t);
    }

    /**
     * 获得 原点 / 零矢量
     *
     * @return new Vector3d(0.0, 0.0)
     */
    public static Vector2d getZeros() {
        return new Vector2d(0.0, 0.0);
    }

    public static void main(String[] args) {
        适量旋转();

    }

    private static void 适量旋转() {
        Vector2d p1 = new Vector2d(1, 2);
        Vector2d v1 = new Vector2d(3.0, 1.0);
        Plod2d.plotVector(p1, v1, 1);

        for (int i = 1; i < 10; i++) {
            Vector2d vi = Vector2d.rotate(v1, Math.PI / 180 * 10 * i);
            Plod2d.plotVector(p1, vi, 1);
            //2019年7月7日 太美了
        }

        Plod2d.showThread();
    }
}
