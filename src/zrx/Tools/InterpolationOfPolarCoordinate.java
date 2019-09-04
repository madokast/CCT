package zrx.Tools;

import Jama.Matrix;
import zrx.base.Constants;
import zrx.base.point.Vector2d;
import zrx.python.Plot2d;

import java.util.ArrayList;
import java.util.List;

/**
 * 极坐标下插值
 * 依赖包
 * Jama.Matrix
 * zrx.base.point.Vector2d
 */

public class InterpolationOfPolarCoordinate {
    /**
     * 极坐标形似的四点插值(虽说是极坐标，但是输入输出都是直角坐标系量)
     * 2019年7月8日 测试通过 <-- 胡言乱语
     * 过了几次调试才成功
     * <p>
     * r = a[0]θ^3 + a[1]θ^2 + a[2]θ^+ a[3]
     * 设定 极点 为 起点和终点的中点
     *
     * @param p0   起点
     * @param p1   中间点
     * @param p2   中间点
     * @param p3   终点
     * @param step 步长
     * @return 插值曲线
     */
    @Deprecated
    public static Vector2d[] interpolation4Point(Vector2d p0, Vector2d p1, Vector2d p2, Vector2d p3, double step) {
        //p0 p3的中点
        Vector2d mp = Vector2d.midpoint(p0, p3);

        //似乎过 θ = 0 / 2pi 交界线会出问题。那就整体旋转一下
        //把起点旋转到 θ = 0 下面求要旋转的角度phi0
        double phi0 = Vector2d.polarAngle(mp, p0);
        if (phi0 >= 0) {
            phi0 = -phi0 + Constants.DX;
        } else {
            phi0 = 2.0 * Math.PI - phi0 + Constants.DX;
        }

        //开始旋转 获得 rp0~rp3
        Vector2d rp0 = Vector2d.rotate(mp, p0, phi0);
        Vector2d rp1 = Vector2d.rotate(mp, p1, phi0);
        Vector2d rp2 = Vector2d.rotate(mp, p2, phi0);
        Vector2d rp3 = Vector2d.rotate(mp, p3, phi0);

//        //打印
//        Plot2d.plotPoint(rp0, Plot2d.YELLOW_POINT);
//        Plot2d.plotPoint(rp1, Plot2d.YELLOW_POINT);
//        Plot2d.plotPoint(rp2, Plot2d.YELLOW_POINT);
//        Plot2d.plotPoint(rp3, Plot2d.YELLOW_POINT);

//        Plot2d.plotPoint(p0, Plot2d.YELLOW_POINT);
//        Plot2d.plotPoint(p1, Plot2d.YELLOW_POINT);
//        Plot2d.plotPoint(p2, Plot2d.YELLOW_POINT);
//        Plot2d.plotPoint(p3, Plot2d.YELLOW_POINT);

        //得到四点的极坐标 pp0~pp3(r,φ)，以mp为极点
        Vector2d pp0 = Vector2d.convertToPolarCoordinates(mp, rp0);
        Vector2d pp1 = Vector2d.convertToPolarCoordinates(mp, rp1);
        Vector2d pp2 = Vector2d.convertToPolarCoordinates(mp, rp2);
        Vector2d pp3 = Vector2d.convertToPolarCoordinates(mp, rp3);

        //四点插值。待定系数法
        double[] arr = interpolation4Point(pp0, pp1, pp2, pp3);


        //起点终点 角度值
        double start = pp0.y;
        double end = pp3.y;

        //是逆时针还是顺时针？ 不是robust
        if (start < pp1.y) {
            //逆时钟
            step = Math.abs(step);
        } else {
            //顺时钟
            step = -Math.abs(step);
        }

        //得到轨迹
        List<Vector2d> list = new ArrayList<>();
        while (Math.abs(start - end) >= Math.abs(step)) {
            list.add(new Vector2d(cubicFunction(arr, start), start));
            start += step;
            if (start > 2.0 * Math.PI)
                start -= 2.0 * Math.PI;
        }

        //最后一点
        list.add(new Vector2d(cubicFunction(arr, end), end));
        //数组化
        Vector2d[] path = list.toArray(new Vector2d[]{});

        /**
         * 下面应该是先转换为直角坐标系，再旋转。晕死了
         * 回顾一下。
         *      首先是要插值的点 pn
         *      然后旋转phi0 得到 rpn
         *      然后变成极坐标 ppn
         *
         *      然后插值得到 path
         *
         *      所以先便会直角坐标系
         *      最后旋转回来
         */

        //变成直角坐标系
        path = Vector2d.convertFromPolarCoordinatesToCartesianCoordinates(mp, path);
        //旋转回去
        path = Vector2d.rotate(mp, path, -phi0);

        return path;
    }

    /**
     * 极坐标形似的二点插值(虽说是极坐标，但是输入输出都是直角坐标系量)
     * r = a[0]θ^3 + a[1]θ^2 + a[2]θ^+ a[3]
     * <p>
     * 极点为 p0 p1 中点
     *
     * @param p0   起点
     * @param v0   起点方向
     * @param p1   终点
     * @param v1   终点方向
     * @param step 步长 弧度
     * @return 插值路径
     */
    public static Vector2d[] interpolation2Point(Vector2d p0, Vector2d v0, Vector2d p1, Vector2d v1, double step) {
        return interpolation2Point(Vector2d.midpoint(p0, p1),p0,v0,p1,v1,step);
    }

    /**
     * 指定极点的赫米特形插值
     * 具体见  interpolation2Point
     * r = a[0]θ^3 + a[1]θ^2 + a[2]θ^+ a[3]
     * @param mid 中点
     * @param p0 起点
     * @param v0 起点方向
     * @param p1 终点
     * @param v1 终点方向
     * @param step 步长
     * @return 插值路径
     */
    public static Vector2d[] interpolation2Point(Vector2d mid,Vector2d p0, Vector2d v0, Vector2d p1, Vector2d v1, double step){
//        System.out.println("-------------------------");

        //p0 p1的中点
        Vector2d mp = mid;

        //把起点旋转到 θ = 0 下面求要旋转的角度phi0
        //上面这句话错误 2019年7月9日。不能仅考虑p0
        double phip0 = Vector2d.polarAngle(mp, p0);
        double phip1 = Vector2d.polarAngle(mp, p1);

        double phi0 = Math.min(phip0, phip1);
//        System.out.println("phi0 = " + phi0);

        phi0 = -phi0 + Constants.DX;

        //没用
//        if (phi0 >= 0) {
//            phi0 = -phi0 + Constants.DX;
//        } else {
//            phi0 = 2.0 * Math.PI - phi0 + Constants.DX;
//        }


//        System.out.println(Vector2d.polarAngle(mp,p0));

        //开始旋转 获得 rp0~rp1
        Vector2d rp0 = Vector2d.rotate(mp, p0, phi0);
        Vector2d rp1 = Vector2d.rotate(mp, p1, phi0);
        //得到极坐标 pp0~pp1(r,φ)，以mp为极点
        Vector2d pp0 = Vector2d.convertToPolarCoordinates(mp, rp0);
        Vector2d pp1 = Vector2d.convertToPolarCoordinates(mp, rp1);

        //方向也要旋转 rv0 rv1
        Vector2d rv0 = Vector2d.rotate(v0, phi0);
        Vector2d rv1 = Vector2d.rotate(v1, phi0);
        //方向对应的极角
        double av0 = rv0.polarAngle();
        double av1 = rv1.polarAngle();

//        System.out.println("av0 = " + av0);
//        System.out.println("av1 = " + av1);
//
//        Plot2d.plotPoint(rp0,Plot2d.RED_POINT);
//        Plot2d.plotVector(rp0,rv0,0.1);
//
//        Plot2d.plotPoint(rp1,Plot2d.RED_POINT);
//        Plot2d.plotVector(rp1,rv1,0.1);


//        System.out.println("av0 = " + av0);
//        System.out.println("av1 = " + av1);
////        av0 = 1.7497161350085566    99度
////        av1 = 4.87585552752088      279度

        //开始计算插值函数 r = a[0]θ^3 + a[1]θ^2 + a[2]θ^+ a[3]
        double[] arr = interpolation2Point(pp0, av0, pp1, av1);


        //起点终点 角度值
        double start = pp0.y;
        double end = pp1.y;

//        System.out.println("start = " + start);
//        System.out.println("end = " + end);

        //是逆时针还是顺时针？ 不是robust
        if (start < end) {
            //逆时钟
            step = Math.abs(step);
        } else {
            //顺时钟
            step = -Math.abs(step);
        }

//        System.out.println("step = " + step);

        //得到轨迹
        List<Vector2d> list = new ArrayList<>();
        while (Math.abs(start - end) >= Math.abs(step)) {
//            System.out.println("start = " + start);

            list.add(new Vector2d(cubicFunction(arr, start), start));
            start += step;
            if (start > 2.0 * Math.PI)
                start -= 2.0 * Math.PI;


        }

        //最后一点
        list.add(new Vector2d(cubicFunction(arr, end), end));
        //数组化
        Vector2d[] path = list.toArray(new Vector2d[]{});

        //变成直角坐标系
        path = Vector2d.convertFromPolarCoordinatesToCartesianCoordinates(mp, path);

//        Plot2d.plot2(path,Plot2d.YELLOW_POINT);


        //旋转回去
        path = Vector2d.rotate(mp, path, -phi0);

        return path;
    }

    /**
     * 四点插值，待定系数法
     * y = a[0]x^3 + a[1]x^2 + a[2]x^+ a[3]
     *
     * @param a0 点1
     * @param a1 点2
     * @param a2 点3
     * @param a3 点4
     * @return 插值系数
     */
    private static double[] interpolation4Point(Vector2d a0, Vector2d a1, Vector2d a2, Vector2d a3) {
        //(r,φ)
        //采用 Ax=b 求解。用到了 Jama 包

        //构建系数矩阵 A
        double[] a0n = {Math.pow(a0.y, 3), Math.pow(a0.y, 2), a0.y, 1};
        double[] a1n = {Math.pow(a1.y, 3), Math.pow(a1.y, 2), a1.y, 1};
        double[] a2n = {Math.pow(a2.y, 3), Math.pow(a2.y, 2), a2.y, 1};
        double[] a3n = {Math.pow(a3.y, 3), Math.pow(a3.y, 2), a3.y, 1};
        Matrix A = new Matrix(new double[][]{a0n, a1n, a2n, a3n});

        //构建列向量 b
        Matrix b = new Matrix(new double[][]{
                {a0.x}, {a1.x}, {a2.x}, {a3.x}
        });

//        System.out.println("A.print");
//        A.print(7,4);
//        b.print(7,4);

        //求解
        Matrix x = A.solve(b);

        return columnVectorToArray(x);
    }

    /**
     * 二点极坐标赫米特插值
     *
     *    发现重大bug 已修复
     *      2019年7月9日
     *
     *
     * @param a0  点0
     * @param th0 点0方向对应的极角
     * @param a1  点1
     * @param th1 点1方向对应的极角
     * @return 插值系数
     */
    private static double[] interpolation2Point(Vector2d a0, double th0, Vector2d a1, double th1) {
//        (r,φ)

        //构建系数矩阵 A
        double[] a0n = {Math.pow(a0.y, 3), Math.pow(a0.y, 2), a0.y, 1};
        double[] a1n = {Math.pow(a1.y, 3), Math.pow(a1.y, 2), a1.y, 1};
        double[] a2n = {3 * Math.pow(a0.y, 2), 2 * a0.y, 1, 0};
        double[] a3n = {3 * Math.pow(a1.y, 2), 2 * a1.y, 1, 0};
        Matrix A = new Matrix(new double[][]{a0n, a1n, a2n, a3n});

        // 下面删除的代码是胡说八道。留在这里，以警后人
        //构建列向量 b
//        Matrix b = new Matrix(new double[][]{
//                {a0.x}, {a1.x},
//                {a0.x*Math.sin(th0)*Math.cos(th0-a0.y)/(Math.pow(Math.sin(th0-a0.y),2))},
//                {/*就这里-->>*/+/*<<--加了一个负号*/a1.x*Math.sin(th1)*Math.cos(th1-a1.y)/(Math.pow(Math.sin(th1-a1.y),2))}
//                /*呜呜呜 到底是为什么呢？ 正数改成负数就对了*/
//                /*可是我无法解释。照道理是没有负号的*/
//        });

        Matrix b = new Matrix(new double[][]{
                {a0.x}, {a1.x},
                {a0.x*Math.sin(th0-a0.y)*Math.cos(th0-a0.y)/(Math.pow(Math.sin(th0-a0.y),2))},
                {a1.x*Math.sin(th1-a1.y)*Math.cos(th1-a1.y)/(Math.pow(Math.sin(th1-a1.y),2))}

        });

        //求解
        Matrix x = A.solve(b);

//        A.print(7,3);
//        b.print(7,3);
//        x.print(7,3);

        return columnVectorToArray(x);

    }

    private static double[] columnVectorToArray(Matrix x) {
        int r = x.getRowDimension();
//        System.out.println("r = " + r);

        double[] arr = new double[r];

        for (int i = 0; i < r; i++) {
            arr[i] = x.get(i, 0);
        }
        return arr;
    }

    /**
     * 三次方程
     * r = a[0]θ^3 + a[1]θ^2 + a[2]θ^+ a[3]
     *
     * @param arr 系数
     * @param x   自变量
     * @return 应变量 y
     */
    private static double cubicFunction(double[] arr, double x) {
        return arr[0] * x * x * x +
                arr[1] * x * x +
                arr[2] * x +
                arr[3];
    }

    public static void main(String[] args) {
//        测试1();
//        测试2();
        测试3();
    }

    private static void 测试3() {
        Vector2d p0 = new Vector2d(1, 0);
        Vector2d p1 = new Vector2d(-1, 0);
        Vector2d v0 = new Vector2d(-1, 1);
        Vector2d v1 = new Vector2d(1, -1);

        Plot2d.plotPoint(p0, Plot2d.RED_POINT);
        Plot2d.plotPoint(p1, Plot2d.RED_POINT);


        Plot2d.plotVector(p0,v0,0.2);
        Plot2d.plotVector(p1,v1,0.2);

        Vector2d[] path = interpolation2Point(p0,v0,p1,v1,Math.PI/180);
        Plot2d.plot2(path, Plot2d.BLUE_LINE);


        Plot2d.equal();
        Plot2d.showThread();
    }

    private static void 测试2() {
        Vector2d p0 = new Vector2d(2, 1);
        Vector2d p1 = new Vector2d(2.7, 1.8);
        Vector2d p2 = new Vector2d(3, 3);
        Vector2d p3 = new Vector2d(1.7, 4);

        Plot2d.plotPoint(p0, Plot2d.RED_POINT);
        Plot2d.plotPoint(p1, Plot2d.RED_POINT);
        Plot2d.plotPoint(p2, Plot2d.RED_POINT);
        Plot2d.plotPoint(p3, Plot2d.RED_POINT);

        Vector2d[] path = interpolation4Point(p0, p1, p2, p3, Math.PI / 180.0);
        Plot2d.plot2(path, Plot2d.BLUE_LINE);

        Plot2d.equal();
        Plot2d.showThread();
    }

    private static void 测试1() {
        Vector2d p0 = new Vector2d(2, 1);
        Vector2d p1 = new Vector2d(1, 2);
        Vector2d p2 = new Vector2d(-1, 2);
        Vector2d p3 = new Vector2d(-2, 1);

        Plot2d.plotPoint(p0, Plot2d.RED_POINT);
        Plot2d.plotPoint(p1, Plot2d.RED_POINT);
        Plot2d.plotPoint(p2, Plot2d.RED_POINT);
        Plot2d.plotPoint(p3, Plot2d.RED_POINT);

        Vector2d[] path = interpolation4Point(p0, p1, p2, p3, Math.PI / 180.0);
        Plot2d.plot2(path, Plot2d.BLUE_LINE);

        Plot2d.showThread();
    }
}
