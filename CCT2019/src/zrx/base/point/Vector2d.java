package zrx.base.point;

import Jama.Matrix;
import zrx.Tools.*;
import zrx.base.Constants;
import zrx.python.Plot2d;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.DoubleFunction;
import java.util.function.Predicate;

public class Vector2d implements Serializable {
    public double x;
    public double y;

    public Vector2d(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vector2d(Vector2d copy) {
        this.x = copy.x;
        this.y = copy.y;
    }

    /**
     * 尝试寻找对两点的圆插值，得到圆的半径 r
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
     * とくいなコード
     * <p>
     * 2019年7月7日 16点12分 测试结束，基本满足要求
     * 本代码主要用于 CCT 连接
     *
     * @param p0         点0
     * @param v0         点0的方向，插值圆会和此方向相切
     * @param clockwise0 true 代表 圆心0在 v0 右侧，否则是左侧
     * @param p1         点1
     * @param v1         点1的方向，插值圆会和此方向相切
     * @param clockwise1 true 代表 圆心0在 v1 右侧，否则是左侧
     * @return 圆半径r
     */
    private static double circularInterpolationRadius(Vector2d p0, Vector2d v0, boolean clockwise0, Vector2d p1, Vector2d v1, boolean clockwise1) {
        //旋转角度
        double phi0 = (clockwise0) ? -Math.PI / 2.0 : Math.PI / 2.0;
        double phi1 = (clockwise1) ? -Math.PI / 2.0 : Math.PI / 2.0;

        //旋转与归一化
        Vector2d n0 = Vector2d.rotate(v0, phi0).normalSelfAndReturn();
        Vector2d n1 = Vector2d.rotate(v1, phi1).normalSelfAndReturn();

        double[] solves = QuadraticEquationOfOneVariable.solve(
                Vector2d.dot(n0, n0) + Vector2d.dot(n1, n1) - 2 * Vector2d.dot(n0, n1) - 4,
                2 * (Vector2d.dot(p0, n0) - Vector2d.dot(p0, n1) - Vector2d.dot(p1, n0) + Vector2d.dot(p1, n1)),
                Vector2d.dot(p0, p0) + Vector2d.dot(p1, p1) - 2 * Vector2d.dot(p0, p1)
        );

//        System.out.println("circularInterpolationRadius solves[0] = " + solves[0]);
//        System.out.println("circularInterpolationRadius solves[1] = " + solves[1]);

        return solves[0];
    }

    /**
     * 圆插值算法，获得两个外切圆的交点
     * 详细描述见 circularInterpolationRadius() 的注释
     * 此算法同样不保证结果正确。因为只不过是找到圆心，然后找两个圆心的中点而已
     * 因此此代码不会报错，但是结果可能不是想要的
     * <p>
     * 图形化描述见
     * <p><img src = "docs/CircularInterpolation.JPG""/><p/>
     *
     * @param p0         点0
     * @param v0         点0方向矢量
     * @param clockwise0 true 代表 圆心0在 v0 右侧，否则是左侧
     * @param p1         点1
     * @param v1         点1的方向，插值圆会和此方向相切
     * @param clockwise1 true 代表 圆心0在 v1 右侧，否则是左侧
     * @param r          外切圆半径，由 circularInterpolationRadius() 获得
     * @return 两个外切圆的交点
     */
    private static Vector2d circularInterpolationCouplingPoint(Vector2d p0, Vector2d v0, boolean clockwise0, Vector2d p1, Vector2d v1, boolean clockwise1, double r) {
        //旋转角度
        double phi0 = (clockwise0) ? -Math.PI / 2.0 : Math.PI / 2.0;
        double phi1 = (clockwise1) ? -Math.PI / 2.0 : Math.PI / 2.0;

        //旋转与归一化
        Vector2d n0 = Vector2d.rotate(v0, phi0).normalSelfAndReturn();
        Vector2d n1 = Vector2d.rotate(v1, phi1).normalSelfAndReturn();

//        Plot2d.plotVector(p0, n0, 1);
//        Plot2d.plotVector(p1, n1, 1);

        //得到圆心
        Vector2d c0 = Vector2d.add(p0, Vector2d.dot(r, n0));
        Vector2d c1 = Vector2d.add(p1, Vector2d.dot(r, n1));

//        Plot2d.plotPoint(c0, ",'k+'");
//        Plot2d.plotPoint(c1, ",'k+'");

//        System.out.println("c0 = " + c0);

        return new Vector2d(
                (c0.x + c1.x) / 2.0,
                (c0.y + c1.y) / 2.0
        );
    }

    /**
     * 圆插值算法，得到两个圆心
     * Vector2d[0] 属于p0的圆的圆心
     * Vector2d[1] 属于p1的圆的圆心
     * <p>
     * 具体见 <p><img src = "docs/CircularInterpolation.JPG" /><p/>
     *
     * @param p0         起点
     * @param v0         起点方向
     * @param clockwise0 起点所属圆上的轨迹旋转方向
     * @param p1         终点
     * @param v1         终点方向
     * @param clockwise1 终点点所属圆上的轨迹旋转方向
     * @param r          圆半径
     * @return 两个圆心
     */
    private static Vector2d[/*TWO ONLY*/] circularInterpolationCenters(Vector2d p0, Vector2d v0, boolean clockwise0, Vector2d p1, Vector2d v1, boolean clockwise1, double r) {
        //旋转角度
        double phi0 = (clockwise0) ? -Math.PI / 2.0 : Math.PI / 2.0;
        double phi1 = (clockwise1) ? -Math.PI / 2.0 : Math.PI / 2.0;

        //旋转与归一化
        Vector2d n0 = Vector2d.rotate(v0, phi0).normalSelfAndReturn();
        Vector2d n1 = Vector2d.rotate(v1, phi1).normalSelfAndReturn();

//        Plot2d.plotVector(p0, n0, 1);
//        Plot2d.plotVector(p1, n1, 1);

        //得到圆心
        Vector2d c0 = Vector2d.add(p0, Vector2d.dot(r, n0));
        Vector2d c1 = Vector2d.add(p1, Vector2d.dot(r, n1));

        return new Vector2d[]{c0, c1};
    }

    /**
     * 圆插值算法 no robust
     * 具体参考 <p><img src = "docs/CircularInterpolation.JPG" /><p/>
     * <p>
     * 返回 Vector2d[][2] 两条轨迹
     * Vector2d[][0] 轨迹，从 p0 出发 到达 coupling point
     * Vector2d[][1] 轨迹，从 coupling point 出发 到达 p1
     * <p>
     * 注：coupling point 即两个外切圆的交点
     *
     * @param p0         起点
     * @param v0         起点方向
     * @param clockwise0 绕第一个圆是顺时针还是逆时针
     * @param p1         终点
     * @param v1         终点方向
     * @param clockwise1 绕第二个圆是顺时针还是逆时针
     * @param step       步长 弧度
     * @return 两条轨迹/路径
     */
    public static Vector2d[][/*TWO ONLY*/] circularInterpolationPart(Vector2d p0, Vector2d v0, boolean clockwise0, Vector2d p1, Vector2d v1, boolean clockwise1, double step) {
        //圆半径
        double r = circularInterpolationRadius(p0, v0, clockwise0, p1, v1, clockwise1);

        //两个圆心
        Vector2d[] cs = circularInterpolationCenters(p0, v0, clockwise0, p1, v1, clockwise1, r);
        Vector2d c0 = cs[0];
        Vector2d c1 = cs[1];

        //coupling point 两个外切圆的交点
        Vector2d cp = circularInterpolationCouplingPoint(p0, v0, clockwise0, p1, v1, clockwise1, r);

        //下面获得轨迹 0 ，即一段弧
        //起点 终点
        double start0 = polarAngle(Vector2d.subtract(p0, c0));
        double end0 = polarAngle(Vector2d.subtract(cp, c0));
        Vector2d[] path0 = arc(c0, r, start0, end0, clockwise0, step);

        //下面获得轨迹 1 ，即一段弧
        //起点 终点
        double start1 = polarAngle(Vector2d.subtract(cp, c1));
        double end1 = polarAngle(Vector2d.subtract(p1, c1));
        Vector2d[] path1 = arc(c1, r, start1, end1, clockwise1, step);

        return new Vector2d[][]{path0, path1};
    }

    /**
     * 详见 circularInterpolationPart()
     * 仅仅是把两段合并起来了而已
     *
     * @param p0         起点
     * @param v0         起点反向
     * @param clockwise0 第一段 顺时针还是逆时针旋转
     * @param p1         终点
     * @param v1         终点方向
     * @param clockwise1 第二段 顺时针还是逆时针旋转
     * @param step       步长
     * @return 合并起来的插值轨迹
     */
    public static Vector2d[] circularInterpolation(Vector2d p0, Vector2d v0, boolean clockwise0, Vector2d p1, Vector2d v1, boolean clockwise1, double step) {
        Vector2d[][] vss = circularInterpolationPart(p0, v0, clockwise0, p1, v1, clockwise1, step);
        return ArrayMerge.merge(vss[0], vss[1]);
    }

    /**
     * 返回一段弧轨迹 Vector2d[]
     * 可以看底下的测试代码，那里有画图哦
     * 不知道是不是robust
     *
     * <img src = "docs/arc,JPG" />
     *
     * @param c         圆心
     * @param r         半径
     * @param start     开始处的极角 [0, 2pi)
     * @param end       结束处的极角 [0, 2pi)
     * @param clockwise 顺时针还是逆时针 不同的选择会产生优弧和劣弧
     * @param step      步长 弧度制。注意最后一点的步长不一定为 step ，因为必须等于 end
     * @return 弧轨迹
     */
    public static Vector2d[] arc(Vector2d c, double r, double start, double end, boolean clockwise, double step) {
        //思考...关键应该是，若 phi 减到了0 则加上 2pi
        //若 phi 加到了2pi 则减去 2pi
        //再思考。我可以写个宽泛的 rayForPolarAngle(phi) 对里面的 phi 要求扩展，不需要在[0, 2pi)

        //设置真正的步长
        if (clockwise) {
            //减少
            step = -Math.abs(step);
            while (start < end)
                start += 2 * Math.PI;
        } else {
            //增加
            step = Math.abs(step);
            while (start > end)
                start -= 2 * Math.PI;
        }

        //开始获得路径数据
        List<Vector2d> path = new ArrayList<>();
        while (Math.abs(start - end) >= Math.abs(step)) {
            Vector2d p = rayForPolarAngle(start);
            path.add(Vector2d.add(c, Vector2d.dot(r, p)));
            start += step;
        }

        Vector2d p = rayForPolarAngle(end);
        path.add(Vector2d.add(c, Vector2d.dot(r, p)));

        return path.toArray(new Vector2d[]{});
    }

    /**
     * 求矢量 v 的极角，弧度制。范围 [0, 2pi)
     *
     * @param v 矢量
     * @return 极角
     */
    public static double polarAngle(Vector2d v) {
        double atan2 = Math.atan2(v.y, v.x);
        return (atan2 > 0) ? atan2 : atan2 + Math.PI * 2.0;

//        if (v.length() < Constants.DX) {
//            throw new ArithmeticException("Zero vector non-exists polar angle");
//        }
//
//        double x = v.x;
//        double y = v.y;
//
//        if (Math.abs(x) < Constants.DX) {
//            if (y > 0)
//                return Math.PI / 2.0;
//            else if ((y < 0))
//                return 3.0 * Math.PI / 2.0;
//
//            throw new ArithmeticException("Zero vector non-exists polar angle");
//        }
//
//        double phi = Math.atan(y / x);
//
//        if (x >= 0 && y >= 0) {
//            return phi;
//        } else if (x <= 0 && y >= 0) {
//            return Math.PI + phi;
//        } else if (x <= 0 && y <= 0) {
//            return Math.PI + phi;
//        } else if (x >= 0 && y <= 0) {
//            return 2 * Math.PI + phi;
//        }
//
//        System.out.println("x = " + x);
//        System.out.println("y = " + y);
//        throw new ArithmeticException("what happened in polarAngle?!");

    }

    /**
     * 求矢量 v 的极角，弧度制。范围 [0, 2pi)
     *
     * @return 求矢量 v 的极角
     */
    public double polarAngle() {
        return Vector2d.polarAngle(this);
    }

    /**
     * 相对与center的极角
     *
     * @param center 中点 参考点 观测点
     * @param p      要求极角的点
     * @return 极角
     */
    public static double polarAngle(final Vector2d center, final Vector2d p) {
        return polarAngle(Vector2d.subtract(p, center));
    }

    /**
     * 返回单位矢量，其极角为 phi
     * 测试中有画图
     * 2019年7月7日 测试通过
     *
     * @param phi 极角
     * @return 单位矢量
     */
    public static Vector2d rayForPolarAngle(double phi) {
        //规范到 [0, 2pi)
        phi = phi - 2.0 * Math.PI * ((int) (phi / (2.0 * Math.PI)));

        //特殊情况。90° 270°
        if (Equal.isEqual(phi, Math.PI / 2.0))
            return new Vector2d(0, 1);
        if (Equal.isEqual(phi, 3.0 * Math.PI / 2.0))
            return new Vector2d(0, -1);

        //一般情况
        double t = Math.tan(phi);
        if (phi < Math.PI / 2)
            return Vector2d.normal(new Vector2d(1, t));
        else if (phi < Math.PI)
            return Vector2d.normal(new Vector2d(-1, -t));
        else if (phi < 3.0 * Math.PI / 2.0)
            return Vector2d.normal(new Vector2d(-1, -t));
        else if (phi < 2.0 * Math.PI)
            return Vector2d.normal(new Vector2d(1, t));

        throw new ArithmeticException("what happened in rayForPolarAngle?!");

    }

    /**
     * 极坐标到XY坐标系
     *
     * @param phi 弧度 0-2pi
     * @param r   长度
     * @return 点于直角坐标系
     */
    public static Vector2d pointFromPolarToXY(double phi, double r) {
        return rayForPolarAngle(phi).changeLengthAndReturn(r);
    }

    /**
     * 弧坐标系 TO 直角坐标系。
     * 首先你身处XY坐标系中，有一个圆心在原点的圆，半径r
     * 同时建立极坐标系(p,a)，极点和XY坐标系原点一致。
     * 例如XY坐标系点(0,r)即及坐标系中的(r,pi/2)
     * 你此时此刻处于极坐标系中(r,phi)位置!
     * 且，你只能在上述的圆上行走，即你的行动范围在极坐标系中是一维的，即(r,任意)
     * 并规定，你逆时针行走，phi增加。
     * 这时你行走了s，也就是长度为s的弧长，于是本函数返回你当前所处位置的XY直角坐标
     * <p>
     * 本函数用于COSY enge函数
     *
     * @param r   弧的半径
     * @param phi 弧坐标系的原点对应的角度-弧度制
     * @param s   从原点行走的距离。正数代表逆时针
     * @return 点于XY直角坐标
     */
    public static Vector2d pointFromArcToXY(double r, double phi, double s) {
        //求弧长s对应的弧度
        double sa = s / r;
        return pointFromPolarToXY(phi + sa, r);
    }

    /**
     * 正则化 矢量长度归一
     */
    public final void normalSelf() {
        double len = length();
        double nx = this.x / len;
        double ny = this.y / len;

        this.x = nx;
        this.y = ny;
    }

    public final Vector2d normalSelfAndReturn() {
        this.normalSelf();
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
     * 矢量旋转
     *
     * @param center 中心
     * @param v      要旋转的矢量
     * @param phi    角度
     * @return 旋转后
     */
    public static Vector2d rotate(final Vector2d center, final Vector2d v, final double phi) {
        Vector2d rp = Vector2d.subtract(v, center);
        Vector2d rotRp = rotate(rp, phi);

        return Vector2d.add(rotRp, center);
    }

    public void rotateSelf(final double phi) {
        Vector2d v = rotate(this, phi);

        this.x = v.x;
        this.y = v.y;
    }

    public Vector2d rotateSelfAndReturn(final double phi) {
        this.rotateSelf(phi);

        return this;
    }

    /**
     * 整体旋转
     *
     * @param vs  要旋转的点组
     * @param phi 旋转角度
     * @return 旋转后的点组
     */
    public static Vector2d[] rotate(final Vector2d[] vs, final double phi) {
        Vector2d[] ans = new Vector2d[vs.length];
        for (int i = 0; i < ans.length; i++) {
            ans[i] = rotate(vs[i], phi);
        }

        return ans;
    }

    /**
     * 整体旋转
     *
     * @param center 旋转中心
     * @param vs     要旋转的点组
     * @param phi    旋转角度
     * @return 旋转后的点组
     */
    public static Vector2d[] rotate(final Vector2d center, final Vector2d[] vs, final double phi) {
        Vector2d[] ans = new Vector2d[vs.length];
        for (int i = 0; i < ans.length; i++) {
            Vector2d rp = Vector2d.subtract(vs[i], center);
            Vector2d rotRp = rotate(rp, phi);

            ans[i] = Vector2d.add(rotRp, center);
        }

        return ans;
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

    public static double dot(final Vector2d a, final Vector2d b) {
        return a.x * b.x +
                a.y * b.y;
    }

    /**
     * 走几步 来来来 走几步
     * 从 from 点出发，走几步length，方向 direct
     *
     * @param from   起点
     * @param direct 方向
     * @param length 长度
     * @return 走到的位置
     */
    public static Vector2d walk(final Vector2d from, final Vector2d direct, double length) {
        Vector2d nd = Vector2d.normal(direct);
        return Vector2d.add(from, dot(length, nd));
    }

    public static Vector2d[] walk(final Vector2d[] from, final Vector2d direct, double length) {
        Vector2d[] afterWalk = new Vector2d[from.length];
        for (int i = 0; i < afterWalk.length; i++) {
            afterWalk[i] = walk(from[i], direct, length);
        }

        return afterWalk;
    }

    public static Vector2d walk(final Vector2d from, final Vector2d direct) {
        return Vector2d.walk(from, direct, direct.length());
    }

    public static Vector2d[] walk(final Vector2d[] from, final Vector2d direct) {
        return Vector2d.walk(from, direct, direct.length());
    }

    public Vector2d walkSelf(final Vector2d direct, double length) {
        Vector2d p = Vector2d.walk(this, direct, length);
        this.x = p.x;
        this.y = p.y;
        return this;
    }

    public Vector2d walkSelf(final Vector2d direct) {
        Vector2d p = Vector2d.walk(this, direct, direct.length());
        this.x = p.x;
        this.y = p.y;
        return this;
    }

    public Vector2d walkToXSelf(double length) {
        Vector2d p = Vector2d.walk(this, new Vector2d(1, 0), length);
        this.x = p.x;
        this.y = p.y;
        return this;
    }


    public Vector2d walkToYSelf(double length) {
        Vector2d p = Vector2d.walk(this, new Vector2d(0, 1), length);
        this.x = p.x;
        this.y = p.y;
        return this;
    }

    /**
     * 改变矢量长度
     *
     * @param length 长度
     * @return 返回原矢量
     */
    public Vector2d changeLengthAndReturn(double length) {
        Vector2d p = Vector2d.walk(Vector2d.getZero(), this, length);
        this.x = p.x;
        this.y = p.y;
        return this;
    }

    public void reverseSelf() {
        x = -x;
        y = -y;
    }

    public Vector2d reverseSelfAndReturn() {
        this.reverseSelf();
        return this;
    }

    public static Vector2d reverse(final Vector2d a) {
        return new Vector2d(-a.x, -a.y);
    }

    public static Vector2d subtract(final Vector2d a, final Vector2d b) {
        return Vector2d.add(a, Vector2d.reverse(b));
    }

    public static Vector2d midpoint(final Vector2d a, final Vector2d b) {
        return new Vector2d((a.x + b.x) / 2.0, (a.y + b.y) / 2.0);
    }

    /**
     * XY 直角坐标转化为 极坐标(r,φ)
     *
     * @param pole 极点
     * @param p    待转化点
     * @return P点的极坐标(r, φ)
     */
    public static Vector2d convertToPolarCoordinates(final Vector2d pole, final Vector2d p) {
        //p点相对坐标
        Vector2d rp = Vector2d.subtract(p, pole);

        //分别求(r,φ)
        double r = rp.length();
        double phi = rp.polarAngle();

        return new Vector2d(r, phi);
    }

    /**
     * 极坐标转化为直角坐标系
     *
     * @param pole 极点 (x,y)
     * @param p    要转换的点(r,φ)
     * @return 转换后的点(x, y)
     */
    public static Vector2d convertFromPolarCoordinatesToCartesianCoordinates(final Vector2d pole, final Vector2d p) {
        double rx = p.x * Math.cos(p.y);
        double ry = p.x * Math.sin(p.y);

        return new Vector2d(pole.x + rx, pole.y + ry);
    }

    /**
     * 大量点从极坐标转化为直角坐标系
     *
     * @param pole 极点 (x,y)
     * @param ps   要转换的点数组(r,φ)
     * @return 转换后的点数组(x, y)
     */
    public static Vector2d[] convertFromPolarCoordinatesToCartesianCoordinates(final Vector2d pole, final Vector2d[] ps) {
        Vector2d[] ans = new Vector2d[ps.length];
        for (int i = 0; i < ans.length; i++) {
            ans[i] = convertFromPolarCoordinatesToCartesianCoordinates(pole, ps[i]);
        }

        return ans;
    }

    /**
     * 坐标变换
     * 原来的点(x，y)
     * 返回点(xfun(x),yfun(y))
     *
     * @param origin 原来的点
     * @param xFun   x变换函数
     * @param yFun   y变换函数
     * @return 新的点
     */
    public static Vector2d convict(Vector2d origin, DoubleFunction<Double> xFun, DoubleFunction<Double> yFun) {
        return getOne(xFun.apply(origin.x), yFun.apply(origin.y));
    }

    public static Vector2d[] convict(Vector2d[] origin, DoubleFunction<Double> xFun, DoubleFunction<Double> yFun) {
        Vector2d[] ans = new Vector2d[origin.length];
        for (int i = 0; i < ans.length; i++) {
            ans[i] = convict(origin[i], xFun, yFun);
        }
        return ans;
    }

    /**
     * 可能有bug!!
     * 计算从向量1到向量2的角度。
     * 若向量2在向量1的逆时针方向，则返回正数
     *
     * @param v1 向量1
     * @param v2 向量2
     * @return 弧度制的角
     */
    public static double angle(Vector2d v1, Vector2d v2) {
        final double angleV1 = polarAngle(v1);
        final double angleV2 = polarAngle(v2);

        return Math.abs(angleV2 - angleV1);
    }

    /**
     * Ax^2+Bxy+Cy^2=D 椭返回圆圆周上均匀分布number个点
     *
     * @param A      椭圆方程参数
     * @param B      椭圆方程参数
     * @param C      椭圆方程参数
     * @param D      椭圆方程参数
     * @param number 点数
     * @return 点
     */
    public static Vector2d[] ellipsePoints(double A, double B, double C, double D, int number) {
        final Vector2d[] points = new Vector2d[number];
        final double circumference = ellipseCircumference(A, B, C, D);
        for (int i = 0; i < number; i++) {
//            points[i] = ellipsePointTheta(A, B, C, D, 2.0 * Math.PI / number * i);
            points[i] = ellipseWalkPoint(A, B, C, D, circumference / number * i);
        }

        return points;
    }

    /**
     * 椭圆拟合。Ax2 + Bxy + Cy2 = D
     * double[0] A
     * double[1] B
     * double[2] C
     * double[3] D
     * <p>
     * 相椭圆方程
     * gama*x^2+2alpha*y*y'+beta*y'^2=epsi
     * 参数关系
     * beta*gama-alpha^2=1
     * <p>
     * <p>
     * 2019年8月22日——测试通过
     *
     * @param vs 椭圆上的点
     * @return 拟合结果 double[0123]
     */
    public static double[] fitEllipse(Vector2d[] vs) {
        double[][] AMatrixDoubless = new double[vs.length][];
        double[][] YMatixDoubless = new double[vs.length][];
        for (int i = 0; i < AMatrixDoubless.length; i++) {
            AMatrixDoubless[i] = new double[]{
                    vs[i].x * vs[i].x,
                    vs[i].x * vs[i].y,
                    vs[i].y * vs[i].y,
            };

            YMatixDoubless[i] = new double[]{1.0};
        }

        //下面的方法，见数值分析。解矛盾方程组。


        final Matrix AMatrix = new Matrix(AMatrixDoubless);
        final Matrix YMatrix = new Matrix(YMatixDoubless);

        final Matrix AMatrixT = AMatrix.transpose();

        final Matrix AMatrixTA = AMatrixT.times(AMatrix);

        final Matrix solveMatrix = AMatrixTA.solve(AMatrixT.times(YMatrix));

        final double A = solveMatrix.get(0, 0);
        final double B = solveMatrix.get(1, 0);
        final double C = solveMatrix.get(2, 0);

        //归一化为twiss
        double temp = Math.sqrt(Math.abs(A * C - (B / 2.0)));
        double[] twiss = new double[]{
                A / temp, B / 2.0 / temp, C / temp, 1 / temp
        };

        System.out.println("gama:" + twiss[0]);
        System.out.println("alpha:" + twiss[1]);
        System.out.println("beta:" + twiss[2]);
        System.out.println("epsi:" + twiss[3]);

        return twiss;
    }

    /**
     * 原点出发，方向th弧度的射线和椭圆Ax^2+Bxy+Cy^2=D的交点
     * 吃老本 2019年8月10日
     *
     * @param A     椭圆方程参数
     * @param B     椭圆方程参数
     * @param C     椭圆方程参数
     * @param D     椭圆方程参数
     * @param theta 方向th弧度
     * @return 交点
     */
    private static Vector2d ellipsePointTheta(double A, double B, double C, double D, double theta) {
        double pi = Math.PI;
        Vector2d d = getZero();


        while (theta < 0)
            theta += 2 * pi;
        while (theta > 2 * pi)
            theta -= 2 * pi;//将弧度th限定在0~2pi

        if (Math.abs(theta) < Constants.ESP_0 || Math.abs(theta - 2 * pi) < Constants.ESP_0) {
            d.x = Math.sqrt(D / A);
            d.y = 0;
        }
        if (Math.abs(theta - pi) < Constants.ESP_0) {
            d.x = -Math.sqrt(D / A);
            d.y = 0;
        }
        //临界问题
        double t = 0.0;
        if (theta > 0 && theta < pi) {
            t = 1 / Math.tan(theta);
            d.y = Math.sqrt(D / (A * t * t + B * t + C));
            d.x = t * d.y;
            //printf("\ntest\n");
            //printf_divct(d);
        }
        if (theta > pi && theta < 2 * pi) {
            theta -= pi;
            t = 1 / Math.tan(theta);
            d.y = -Math.sqrt(D / (A * t * t + B * t + C));
            d.x = t * d.y;
        }
        //射线——负号问题，象限问题

        return d;

        //while (th < 0)
        //        th += 2 * pi;
        //    while (th > 2 * pi)
        //        th -= 2 * pi;//将弧度th限定在0~2pi
        //
        //    if (fabs(th) < eps_0 || fabs(th - 2 * pi)<eps_0) {
        //        d.x = sqrt(D / A);
        //        d.y = 0;
        //    }
        //    if (fabs(th - pi) < eps_0) {
        //        d.x = -sqrt(D / A);
        //        d.y = 0;
        //    }
        //    //临界问题
        //    double t;
        //    if (th > 0 && th < pi) {
        //        t = 1 / tan(th);
        //        d.y = sqrt(D / (A * t*t + B * t + C));
        //        d.x = t * d.y;
        //        //printf("\ntest\n");
        //        //printf_divct(d);
        //    }
        //    if (th > pi&&th < 2 * pi) {
        //        th -= pi;
        //        t = 1 / tan(th);
        //        d.y = -sqrt(D / (A * t*t + B * t + C));
        //        d.x = t * d.y;
        //    }
        //    //射线——负号问题，象限问题
        //
        //    return d;
    }

    /**
     * 暴力法计算椭圆Ax^2+Bxy+Cy^2=D周长
     *
     * @param A 椭圆方程参数
     * @param B 椭圆方程参数
     * @param C 椭圆方程参数
     * @param D 椭圆方程参数
     * @return 周长
     */
    private static double ellipseCircumference(double A, double B, double C, double D) {
        int num = 3600 * 4;
        double c = 0.0;
        for (int i = 0; i < num; i++) {
            c += Vector2d.subtract(ellipsePointTheta(A, B, C, D, 2.0 * Math.PI / num * i),
                    ellipsePointTheta(A, B, C, D, 2.0 * Math.PI / num * (i + 1))).length();
        }

        return c;
    }

    /**
     * 在椭圆Ax^2+Bxy+Cy^2=D 上行走length
     * 返回此时的点
     * 规定起点：椭圆与X轴正方向的交点
     * 规定行走方向：逆时针
     *
     * @param A      椭圆方程参数
     * @param B      椭圆方程参数
     * @param C      椭圆方程参数
     * @param D      椭圆方程参数
     * @param length 行走长度
     * @return 终点
     */
    private static Vector2d ellipseWalkPoint(double A, double B, double C, double D, double length) {
        double stepTheta = AngleToRadian.to(0.05);
        double theta = 0.0;
        while (length > 0.0) {
            length -= Vector2d.subtract(ellipsePointTheta(A, B, C, D, theta),
                    ellipsePointTheta(A, B, C, D, theta + stepTheta)).length();
            theta += stepTheta;
        }

        return ellipsePointTheta(A, B, C, D, theta);
    }

    /**
     * 筛选
     *
     * @param vs        源数据
     * @param predicate 测试
     * @return 筛选后
     */
    public static Vector2d[] select(Vector2d[] vs, Predicate<Vector2d> predicate) {
        List<Vector2d> ans = new ArrayList<>();
        for (Vector2d v : vs) {
            if (predicate.test(v)) {
                ans.add(v);
            }
        }

        return ans.toArray(Vector2d[]::new);
    }

    /**
     * 强制单调变化
     * 按照x
     *
     * @param vs         源数据
     * @param isIncrease 递增还是递减
     * @return 单调
     */
    public static Vector2d[] forceMonotony(Vector2d[] vs, boolean isIncrease) {
        Vector2d[] ans = new Vector2d[vs.length];
        ans[0] = Vector2d.copyOne(vs[0]);
        for (int i = 1; i < vs.length; i++) {
            if (isIncrease) {
                if (vs[i].y >= ans[i - 1].y) {
                    ans[i] = Vector2d.copyOne(vs[i]);
                } else {
                    ans[i] = Vector2d.getOne(vs[i].x, ans[i - 1].y);
                }
            } else {
                if (vs[i].y <= ans[i - 1].y) {
                    ans[i] = Vector2d.copyOne(vs[i]);
                } else {
                    ans[i] = Vector2d.getOne(vs[i].x, ans[i - 1].y);
                }
            }
        }

        return ans;
    }

    /**
     * 获得 原点 / 零矢量
     *
     * @return new Vector3d(0.0, 0.0)
     */
    public static Vector2d getZero() {
        return new Vector2d(0.0, 0.0);
    }

    public static Vector2d getXDirect() {
        return new Vector2d(1.0, 0.0);
    }

    public static Vector2d getYDirect() {
        return new Vector2d(0.0, 1.0);
    }

    public static Vector2d getOne(double x, double y) {
        return new Vector2d(x, y);
    }

    /**
     * 复制一个。复制和被复制的之间不存在关系
     *
     * @param origin 被复制
     * @return 复制的
     */
    public static Vector2d copyOne(Vector2d origin) {
        return getOne(origin.x, origin.y);
    }

    public static Vector2d getOneByLength(Vector2d origin, double length) {
        return Vector2d.getOne(origin.x, origin.y).changeLengthAndReturn(length);
    }

    public static Vector2d getOne(String s) {
        //s = [-0.03, -2.7829744825132186]
        final String substring = s.substring(1, s.length() - 1);
        final String[] split = substring.split(", ");
        double x = 0.0;
        double y = 0.0;
        try {
            x = Double.valueOf(split[0]);
            y = Double.valueOf(split[1]);
        } catch (Exception e) {
            System.err.println("error in Vector2d getByStartAndEnd(String s)");
        }

        return getOne(x, y);
    }

    public static Vector2d[] getOnes(String ss) {
        final String[] split = ss.split("\n");
        Vector2d[] vs = new Vector2d[split.length];

        for (int i = 0; i < vs.length; i++) {
            vs[i] = getOne(split[i]);
        }

        return vs;
    }

    /**
     * 数组 xArr yArr 变Vector2d[]
     *
     * @param xArr 数组
     * @param yArr 数组
     * @return Vector2d[]
     */
    public static Vector2d[] getOnes(double[] xArr, double[] yArr) {
        Equal.requireEqual(xArr.length, yArr.length);
        final Vector2d[] ans = new Vector2d[xArr.length];
        for (int i = 0; i < xArr.length; i++) {
            ans[i] = Vector2d.getOne(xArr[i], yArr[i]);
        }

        return ans;
    }

    public static Vector2d[] listToVector2ds(List<Double> xList, List<Double> yList) {
        final Vector2d[] vector2ds = new Vector2d[xList.size()];
        for (int i = 0; i < vector2ds.length; i++) {
            vector2ds[i] = getOne(xList.get(i), yList.get(i));
        }

        return vector2ds;
    }

    public static Vector2d[] arrayToVector2ds(double[] xs, double[] ys) {
        final Vector2d[] vector2ds = new Vector2d[xs.length];
        for (int i = 0; i < xs.length; i++) {
            vector2ds[i] = getOne(xs[i], ys[i]);
        }

        return vector2ds;

    }


    @Override
    public String toString() {
        return "[" +
                x +
                ", " + y +
                ']';
    }

    /**
     * 研究利器
     *
     * @param vs
     */
    public static void toTableHtml(Vector2d[] vs) {
        System.out.println("<table border=\"1px\">");

        for (Vector2d v : vs) {
            System.out.print("<tr>");
            System.out.print("<td>" + v.x + "</td>");
            System.out.print("<td>" + v.y + "</td>");
            System.out.print("<tr>");
        }

        System.out.println("</table>");


        // System.out.println("<table border=\"1px\">");
        //
        //        for (int n12out = 1; n12out < 120; n12out++) {
        //            for (int n12mid = 1; n12mid < 120; n12mid++) {
        ////                for (int i = 0; i < 3; i++) {
        //                System.out.print("<tr>");
        //
        //                System.out.print("<td>" + N12_OUT + "</td>");
        //                System.out.print("<td>" + N12_MID + "</td>");
        //
        //
        //                System.out.println("</tr>");
        ////                }
        //
        //            }
        //        }
        //
        //        System.out.println("</table>");
    }

    /**
     * 测试
     */
    public static void main(String[] args) {
//        矢量旋转();
//        求极角();
//        solveCircularInterpolationRadius测试1();
//        solveCircularInterpolationRadius测试2();
//        solveCircularInterpolationRadius测试3();
//        getOneTest();

//        椭圆周长();

//        极角规范化();
//        极角得到矢量();
//        弧路径();

//        selectTest();

        forceMonotonyTest();
    }

    private static void forceMonotonyTest() {
        Vector2d[] vs = new Vector2d[]{
                Vector2d.getOne(1, 3),
                Vector2d.getOne(2, 4),
                Vector2d.getOne(3, 5),
                Vector2d.getOne(4, 4),
                Vector2d.getOne(5, 3),
                Vector2d.getOne(6, 2)
        };

        final Vector2d[] monotony = forceMonotony(vs, false);
        PrintArray.print(monotony);
    }

    private static void selectTest() {
        Vector2d[] vs = new Vector2d[]{
                Vector2d.getOne(1, 3),
                Vector2d.getOne(2, 4),
                Vector2d.getOne(3, 5),
                Vector2d.getOne(4, 6),
                Vector2d.getOne(5, 7),
                Vector2d.getOne(6, 8)
        };

        final Vector2d[] ans = select(vs, v -> v.x > 2);
        PrintArray.print(ans);
    }

    private static void 椭圆周长() {
        final double circumference = ellipseCircumference(1, 0, 1, 1);
        System.out.println("circumference = " + circumference);
        System.out.println("Math.PI*2.0 = " + Math.PI * 2.0);
    }

    private static void getOneTest() {
//        System.out.println("getByStartAndEnd(\"[-0.03, -2.7829744825132186]\") = " + getByStartAndEnd("[-0.03, -2.7829744825132186]"));
//        Vector2d[] vs = new Vector2d[11];
//        vs[0] = getByStartAndEnd("[-0.03, -2.7829744825132186]");
//        vs[1] = getByStartAndEnd("[-0.03, -2.7829744825132186]");


        Vector2d[] vs = getOnes(
                "[-0.03, -2.7829744825132186]\n" +
                        "[-0.024, -2.7989782662699856]\n" +
                        "[-0.018, -2.814945183996857]\n" +
                        "[-0.011999999999999997, -2.8308777747658103]\n" +
                        "[-0.005999999999999998, -2.8467788462519814]\n" +
                        "[0.0, -2.862651332162382]\n" +
                        "[0.006000000000000005, -2.878498174152434]\n" +
                        "[0.012000000000000004, -2.8943222126056773]\n" +
                        "[0.018000000000000002, -2.910126072226271]\n" +
                        "[0.024, -2.925912027641135]\n" +
                        "[0.03, -2.9416818309749]"
        );

        Plot2d.plot2(vs, Plot2d.BLACK_LINE);
        for (int i = 0; i < vs.length; i++) {
            Plot2d.plotPoint(vs[i], Plot2d.RED_POINT);
        }

        Plot2d.showThread();
    }

    private static void 弧路径() {
//        arc(Vector2d c, double r, double start, double end, boolean clockwise, double step)
        Vector2d c1 = new Vector2d(1, 1);
        Vector2d[] arc1 = arc(c1, 2, 0.1, 1.8, false, Math.PI / 180);
        Plot2d.plot2(arc1, Plot2d.BLUE_LINE);
        Plot2d.plotPoint(c1, Plot2d.RED_POINT);

        Vector2d[] arc2 = arc(c1, 2, 0.1, 1.8, true, Math.PI / 180);
        Plot2d.plot2(arc2, Plot2d.YELLOW_LINE);

        Vector2d c2 = new Vector2d(-1, -1);
        Vector2d[] arc3 = arc(c2, 4, -1.1, 1.8, false, Math.PI / 180);
        Plot2d.plot2(arc3, Plot2d.GREEN_LINE);
        Plot2d.plotPoint(c2, Plot2d.BLACK_POINT);

        Vector2d[] arc4 = arc(c2, 4, -1.2, 1.9, true, Math.PI / 180);
        Plot2d.plot2(arc4, Plot2d.RED_LINE);

        Plot2d.showThread();
    }

    private static void 极角得到矢量() {
        for (int i = 0; i < 20; i++) {
            Plot2d.plotVector(rayForPolarAngle(i * 15 * Math.PI / 180));
        }

        Plot2d.showThread();
    }

    private static void solveCircularInterpolationRadius测试3() {
        Vector2d p0 = new Vector2d(2, 2);
        Vector2d v0 = new Vector2d(-0.1, -1);
        Vector2d p1 = new Vector2d(1.5, 5);
        Vector2d v1 = new Vector2d(0.2, 1);

        Plot2d.plotVector(p0, v0, 1);
        Plot2d.plotVector(p1, v1, 1);

//        circularInterpolationRadius(p0, v0, true, p1, v1, false);

        Vector2d[][] arcs = circularInterpolationPart(p0, v0, true, p1, v1, false, Math.PI / 180);
        Vector2d[] arc0 = arcs[0];
        Vector2d[] arc1 = arcs[1];

        Plot2d.plot2(arc0, Plot2d.YELLOW_LINE);
        Plot2d.plot2(arc1, Plot2d.RED_LINE);
        Plot2d.showThread();

        //circularInterpolationRadius solves[0] = -1.453332935541214
        //circularInterpolationRadius solves[1] = 1.5949753527049453

//        circularInterpolationRadius(p0,v0,false,p1,v1,false);
        //circularInterpolationRadius solves[0] = -2.451802584248656
        //circularInterpolationRadius solves[1] = 395.3425842748199
    }

    private static void solveCircularInterpolationRadius测试2() {
        Vector2d p0 = new Vector2d(2, 1);
        Vector2d v0 = new Vector2d(0.1, 1);
        Vector2d p1 = new Vector2d(2, 5);
        Vector2d v1 = new Vector2d(-0.2, -1);

        Plot2d.plotVector(p0, v0, 1);
        Plot2d.plotVector(p1, v1, 1);


        double radius = circularInterpolationRadius(p0, v0, true, p1, v1, false);
        System.out.println("radius = " + radius);
        Vector2d couplingPoint = circularInterpolationCouplingPoint(p0, v0, true, p1, v1, false, radius);
        System.out.println("couplingPoint = " + couplingPoint);

        Plot2d.plotPoint(couplingPoint, Plot2d.BLUE_POINT);

        //见证奇迹的时刻
        Vector2d[][] arcs = circularInterpolationPart(p0, v0, true, p1, v1, false, Math.PI / 180);
        Vector2d[] arc0 = arcs[0];
        Vector2d[] arc1 = arcs[1];

        Plot2d.plot2(arc0, Plot2d.YELLOW_LINE);
        Plot2d.plot2(arc1, Plot2d.RED_LINE);

        Plot2d.showThread();
    }

    private static void solveCircularInterpolationRadius测试1() {
        Vector2d p0 = Vector2d.getZero();
        Vector2d v0 = new Vector2d(0, 1);
        Vector2d p1 = new Vector2d(0, 3);
        Vector2d v1 = new Vector2d(0, -1);

        Plot2d.plotVector(p0, v0, 1);
        Plot2d.plotVector(p1, v1, 1);

        Plot2d.showThread();

        circularInterpolationRadius(p0, v0, true, p1, v1, false);
    }

    private static void 矢量旋转() {
        Vector2d p1 = new Vector2d(1, 2);
        Vector2d v1 = new Vector2d(3.0, 1.0);
        Plot2d.plotVector(p1, v1, 1);

        for (int i = 1; i < 10; i++) {
            Vector2d vi = Vector2d.rotate(v1, Math.PI / 180 * 10 * i);
            Plot2d.plotVector(p1, vi, 1);
            //2019年7月7日 太美了
        }

        Plot2d.showThread();
    }

    private static void 求极角() {
        Vector2d v = new Vector2d(1, 0);
        for (int i = 0; i < 30; i++) {
            Plot2d.plotVector(v);
            System.out.println("Vector2d.polarAngle(v)/Math.PI*180 = " + Vector2d.polarAngle(v) / Math.PI * 180);
            v = Vector2d.rotate(v, Math.PI / 180 * 15);
        }

        Plot2d.showThread();
    }

    private static void 极角规范化() {
        double phi = -Math.PI / 180 * 3600;

        while (phi < Math.PI / 180 * 3600) {
            System.out.println((phi - 2.0 * Math.PI * ((int) (phi / (2.0 * Math.PI)))) / Math.PI * 180);
            phi++;
        }

        //2019年7月7日 应该没问题
    }
}
