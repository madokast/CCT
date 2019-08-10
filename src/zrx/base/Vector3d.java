package zrx.base;

import zrx.Tools.Equal;
import zrx.Tools.Numpy;
import zrx.Tools.PrintArray;
import zrx.python.Plot3d;

import java.io.Serializable;
import java.rmi.ServerError;
import java.util.List;
import java.util.SplittableRandom;

/**
 * 三维矢量
 */

public class Vector3d implements Serializable {
    public double x;
    public double y;
    public double z;
    private static final Long serialVersionUID = 201907091716L;

    public final void selfMatmul(double[][] r) {
        double xx = r[0][0] * x + r[0][1] * y + r[0][2] * z;
        double yy = r[1][0] * x + r[1][1] * y + r[1][2] * z;
        double zz = r[2][0] * x + r[2][1] * y + r[2][2] * z;

        this.x = xx;
        this.y = yy;
        this.z = zz;
    }

    public final static Vector3d corss(Vector3d a, Vector3d b) {
        return new Vector3d(a.y * b.z - a.z * b.y,
                -a.x * b.z + a.z * b.x,
                a.x * b.y - a.y * b.x);
    }

    public final static Vector3d add(Vector3d p0, Vector3d p1) {
        return new Vector3d(p0.x + p1.x, p0.y + p1.y, p0.z + p1.z);
    }

    public final static Vector3d subtract(Vector3d p0, Vector3d p1) {
        return new Vector3d(p0.x - p1.x, p0.y - p1.y, p0.z - p1.z);
    }

    public final static Vector3d dot(Vector3d p, double a) {
        return new Vector3d(p.x * a, p.y * a, p.z * a);
    }

    public final static Vector3d dot(double a, Vector3d p) {
        return new Vector3d(p.x * a, p.y * a, p.z * a);
    }

    public final static double dot(Vector3d a, Vector3d b) {
        return a.x * b.x +
                a.y * b.y +
                a.z * b.z;
    }

    public final void addSelf(Vector3d p) {
        x += p.x;
        y += p.y;
        z += p.z;
    }

    public final void subtractSelf(Vector3d p) {
        x -= p.x;
        y -= p.y;
        z -= p.z;
    }

    public final double length() {
        return Math.sqrt(x * x + y * y + z * z);
    }

    public final void normal() {
        double len = length();
        double nx = this.x / len;
        double ny = this.y / len;
        double nz = this.z / len;

        this.x = nx;
        this.y = ny;
        this.z = nz;
    }

    /**
     * 判断两条线是否共面
     *
     * @param a 线1
     * @param b 线2
     * @return 是否共面
     */
    public static final boolean isCoplane(Vector3d a, Vector3d b) {
        a.normal();
        b.normal();
        Vector3d n = Vector3d.corss(a, b);
        n.normal();

        double an = Vector3d.dot(a, n);
        double bn = Vector3d.dot(b, n);

        if (an < Constants.DX && bn < Constants.DX) {
            return true;
        } else
            return false;
    }

    /**
     * 返回空间中两条不相交的直线，它们最近距离时所对应的两个点
     * 其实就是求空间中两条直线的交点，但是因为精度误差，不可能完美相交
     * 若返回到两个点距离过大，那么这两条直线确实不相交呢
     * 若这两条线平行？鬼知道会得到什么
     * <p>
     * 也许测试通过了，忐忑不安 2019年7月5日
     *
     * @param line0 直线0
     * @param line1 直线1
     * @return 返回一个Vector3d数组，数组元素只有两个，p[0]∈line0， p[1]∈line1
     */
    public static final Vector3d[/*2 ONLY*/] nearestTwoPointsOnTheirLinesRespectively(final Line line0, final Line line1) {
        Vector3d p0 = line0.pointOnThisLine;
        Vector3d p1 = line1.pointOnThisLine;

        Vector3d v0 = line0.directOfThisLine;
        Vector3d v1 = line1.directOfThisLine;

        double[][] A = new double[][]{
                {Vector3d.dot(v0, v0), -Vector3d.dot(v0, v1)},
                {-Vector3d.dot(v0, v1), Vector3d.dot(v1, v1)}
        };
        double[] b = new double[]{
                Vector3d.dot(p1, v0) - Vector3d.dot(p0, v0),
                Vector3d.dot(p0, v1) - Vector3d.dot(p1, v1)
        };

        double[] nm = linearEquationsWithTwoUnknowns(A, b);

        Vector3d[] ps = new Vector3d[]{
                Vector3d.add(p0, Vector3d.dot(nm[0], v0)),
                Vector3d.add(p1, Vector3d.dot(nm[1], v1))
        };

        return ps;
    }

    /**
     * 两直线最短距离
     * <a href="https://baike.baidu.com/item/%E5%BC%82%E9%9D%A2%E7%9B%B4%E7%BA%BF%E7%9A%84%E8%B7%9D%E7%A6%BB/2074683?fr=aladdin">
     * 两直线最短距离
     * <a/>
     * 忐忑不安
     *
     * @param line0 直线1
     * @param line1 直线2
     * @return 最短距离
     */
    public static double nearestDistanceOfTwoLines(Line line0, Line line1) {
        Vector3d p0 = line0.pointOnThisLine;
        Vector3d p1 = line1.pointOnThisLine;
        Vector3d r01 = Vector3d.subtract(p1, p0);

        Vector3d v0 = line0.directOfThisLine;
        Vector3d v1 = line1.directOfThisLine;

        return Math.abs(
                Vector3d.dot(Vector3d.corss(v0, v1), r01)
        ) /
                Math.abs(
                        Vector3d.corss(v0, v1).length()
                );
    }

    /**
     * 解二元一次方程组。行列式形式 Ax=b
     * [a00 a01] [x0]  =  [b0]
     * [a10 a11] [x1]     [b1]
     * 感觉比较robust了 2019年7月5日 测试通过
     *
     * @param A 系数矩阵A
     * @param b 列向量b
     * @return 方程的解x
     */
    private static double[] linearEquationsWithTwoUnknowns(final double[][] A, final double[] b) {
        if (Math.abs(A[0][0]) < Constants.DX && Math.abs(A[1][0]) < Constants.DX) {
            throw new ArithmeticException("/ by zero");
        }
        double[] x = new double[2];

        if (Math.abs(A[0][0]) < Math.abs(A[1][0])) {
            x[1] = (b[0] - b[1] / A[1][0] * A[0][0]) /
                    (A[0][1] - A[1][1] / A[1][0] * A[0][0]);
            x[0] = (b[1] - A[1][1] * x[1]) / A[1][0];
        } else {
            x[1] = (b[1] - b[0] / A[0][0] * A[1][0]) /
                    (A[1][1] - A[0][1] / A[0][0] * A[1][0]);
            x[0] = (b[0] - A[0][1] * x[1]) / A[0][0];
        }

        return x;
    }

    /**
     * 一条直线。具体含义 代码已自解释
     */
    public static class Line {
        public final Vector3d pointOnThisLine;
        public final Vector3d directOfThisLine;

        public Line(Vector3d pointOnThisLine, Vector3d directOfThisLine) {
            this.pointOnThisLine = pointOnThisLine;
            this.directOfThisLine = directOfThisLine;
        }

        public Vector3d[] pathForPlot3(double len) {
            int num = 50;
            double start = 0;
            double end = len / directOfThisLine.length() / 2.0;
            Vector3d[] ps = new Vector3d[2 * num];
            double[] ds = Numpy.linspace(start, end, num);
            for (int i = 0; i < num; i++) {
                ps[i] = Vector3d.add(pointOnThisLine, Vector3d.dot(ds[i], directOfThisLine));
            }
            for (int i = num; i < 2 * num; i++) {
                ps[i] = Vector3d.add(pointOnThisLine, Vector3d.dot(-ds[i - num], directOfThisLine));
            }

            return ps;
        }
    }

    @Deprecated
    public Vector3d() {
        this.x = 0;
        this.y = 0;
        this.z = 0;
    }

    public Vector3d(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3d(Vector3d p) {
        this.x = p.x;
        this.y = p.y;
        this.z = p.z;
    }

    /**
     * 获得 原点 / 零矢量
     *
     * @return new Vector3d(0.0, 0.0, 0.0)
     */
    public static Vector3d getZeros() {
        return new Vector3d(0.0, 0.0, 0.0);
    }

    /**
     * 二维点变成三维 Z==0.0
     *
     * @param v2 二维
     * @return 三维
     */
    public static Vector3d vector2dTo3d(Vector2d v2) {
        return new Vector3d(v2.x, v2.y, 0.0);
    }

    public static Vector3d vector2dTo3d(Vector2d v2,Vector2dTo3d vector2dTo3d)
    {
        return vector2dTo3d.functioon(v2);
    }

    public static Vector3d[] vector2dTo3d(Vector2d[] v2s) {
        Vector3d[] v3s = new Vector3d[v2s.length];
        for (int i = 0; i < v3s.length; i++) {
            v3s[i] = vector2dTo3d(v2s[i]);
        }

        return v3s;
    }

    public static Vector3d[] vector2dTo3d(Vector2d[] v2s,Vector2dTo3d vector2dTo3d) {
        Vector3d[] v3s = new Vector3d[v2s.length];
        for (int i = 0; i < v3s.length; i++) {
            v3s[i] = vector2dTo3d(v2s[i],vector2dTo3d);
        }

        return v3s;
    }

    public static Vector2d vector3dTo2d(Vector3d v3) {
        return Vector2d.getOne(v3.x, v3.y);
    }

    public void setLength(double length) {
        this.normal();
        this.x = this.x * length;
        this.y = this.y * length;
        this.z = this.z * length;
    }

    public Vector3d setLengthAndReturn(double length){
        this.setLength(length);
        return this;
    }

    /**
     * 2019年7月16日 出现重大bug
     * 大bug!!!
     * @param sourcePoint 原点
     * @param direct 方向
     * @param length 长度
     * @return
     */
    public static Vector3d move(Vector3d sourcePoint, Vector3d direct, double length) {
        /**
         * 发现了重大bug。length=0，0，0 的大 bug
         */
        if(Equal.isEqual(length,0.0))
            return sourcePoint;

        direct.setLength(Math.abs(length));

        if(length>0){
            return Vector3d.add(sourcePoint,direct);
        }
        else if(length<0){
            return Vector3d.subtract(sourcePoint,direct);
        }
        else{
            System.err.println("有问题!public static Vector3d move");
            return sourcePoint;
        }


    }

    public static Vector3d[] move(final Vector3d[] sourcePoints,final Vector3d direct, double length){
        Vector3d[] vector3ds = new Vector3d[sourcePoints.length];
        for (int i = 0; i < vector3ds.length; i++) {
            vector3ds[i] = move(sourcePoints[i],direct,length);
        }

        return vector3ds;
    }

    /**
     * lisy变数组
     * @param v3s List<Vector3d>
     * @return Vector3d[]
     */
    public static Vector3d[] listToArray(List<Vector3d> v3s){
        final Vector3d[] vector3ds = new Vector3d[v3s.size()];
        for (int i = 0; i < vector3ds.length; i++) {
            vector3ds[i]=v3s.get(i);
        }

        return vector3ds;
    }

    /**
     * 均匀插值。
     * 如传入(0,0,0)、(1,0,0)、3
     * 返回{(0,0,0)、(1/2,0,0)、(1,0,0)}
     * @param start 起点
     * @param end 终点
     * @param num 数目
     * @return 插值数组
     */
    public static Vector3d[] interpolation(Vector3d start,Vector3d end,int num){
        if(num<3){
            System.err.println("error in Vector3d[] interpolation");
            System.err.println("插值数目过少");
            System.exit(-1);
        }
        final double[] xs = Numpy.linspace(start.x, end.x, num);
        final double[] ys = Numpy.linspace(start.y, end.y, num);
        final double[] zs = Numpy.linspace(start.z, end.z, num);

        return triArrayToVector3ds(xs,ys,zs);
    }

    /**
     * 均匀插值。这里专用于直线轨道的生成
     * @param start 起点
     * @param direct 方向
     * @param length 长度
     * @param num 离散点数
     * @return 离散轨道
     */
    public static Vector3d[] interpolation(Vector3d start,Vector3d direct,double length,int num){
        final Vector3d end = Vector3d.add(start,
                Vector3d.dot(length,direct.setLengthAndReturn(1.0))
        );

        return interpolation(start,end,num);
    }

    /**
     * 单独数组得到Vector3d[]
     * @param xs xs
     * @param ys yz
     * @param zs zs
     * @return Vector3d[]
     */
    public static Vector3d[] triArrayToVector3ds(double[] xs,double[] ys,double[] zs){
        int len = Math.min(zs.length,
                Math.min(xs.length,ys.length));

        Vector3d[] v3s = new Vector3d[len];
        for (int i = 0; i < len; i++) {
            v3s[i]=Vector3d.getOne(xs[i],ys[i],zs[i]);
        }

        return v3s;
    }

    public static boolean isParallel(Vector3d v1,Vector3d v2){
        Equal.requireNonzero(v1.length());
        Equal.requireNonzero(v2.length());

        Vector3d vv1 = copyOne(v1);
        Vector3d vv2 = copyOne(v2);

        vv1.setLength(1.0);
        vv2.setLength(1.0);

//        if(Equal.isEqual(getZeros(),Vector3d.add(vv1,vv2)))
//            return true;
//
//        if(Equal.isEqual(getZeros(),Vector3d.subtract(vv1,vv2)))
//            return true;
//
//        return false;

        //我要是写成它提示的那样子，就不好懂了吧

        return Equal.isEqual(getZeros(),Vector3d.add(vv1,vv2))||Equal.isEqual(getZeros(),Vector3d.subtract(vv1,vv2));
    }

    public static Vector3d getOne(double x,double y,double z){
        return new Vector3d(x,y,z);
    }

    public static Vector3d getOne(double x,double y){
        return getOne(x,y,0);
    }

    public static Vector3d getOne(Vector2d vector2d){
        return getOne(vector2d.x,vector2d.y,0);
    }

    public static Vector3d getOne(final Vector3d vct,double length){
        return new Vector3d(vct).setLengthAndReturn(length);
    }

    public static Vector3d copyOne(Vector3d v){
        return getOne(v.x,v.y,v.z);
    }

    public static Vector3d getXDirect(double x){
        return getOne(x,0,0);
    }

    public static Vector3d getXDirect(){
        return getXDirect(1);
    }

    public static Vector3d getYDirect(double y){
        return getOne(0,y,0);
    }

    public static Vector3d getYDirect(){
        return getYDirect(1);
    }

    public static Vector3d getZDirect(double z){
        return getOne(0,0,z);
    }

    public static Vector3d getZDirect(){
        return getZDirect(1);
    }

    /**
     * 投影到xy平面
     * 即(x,y,z)->(x,y,0)
     * @return Vector2d
     */
    public Vector2d projectToXY(){
        return Vector2d.getOne(this.x,this.y);
    }

    @Override
    public String toString() {
        return "[" + x + ' ' + y + ' ' + z + ']';
    }

    /**
     * 测试
     *
     * @param args
     */
    public static void main(String[] args) {
//        test1();
//        test2();
//        test3();
//        test4();
        平行测试();
    }

    private static void 平行测试() {
        final Vector3d one = Vector3d.getOne(1, 1, 1);
        final Vector3d one1 = Vector3d.getOne(-2, -2, -2);
        final Vector3d one2 = Vector3d.getOne(3, 3, 3);

        System.out.println("isParallel(one,one1) = " + isParallel(one, one1));

        System.out.println("isParallel(one,one2) = " + isParallel(one, one2));

        System.out.println("isParallel(getXDirect(),getYDirect()) = " + isParallel(getXDirect(), getYDirect()));

        isParallel(getXDirect(),getZeros());
    }

    private static void test4() {
        final Vector3d[] interpolation = interpolation(
                Vector3d.getZeros(),
                Vector3d.getOne(1,2,3),
                5
        );

        PrintArray.print(interpolation);
    }

    private static void test2() {
        double[][] A = new double[][]{{7, 2}, {3, 4}};
        double[] b = new double[]{5, 6};
        double[] x = linearEquationsWithTwoUnknowns(A, b);

        System.out.println("x[0] = " + x[0]);
        System.out.println("x[1] = " + x[1]);

        System.out.println("---------------");

        Vector3d p0 = new Vector3d(1, 2, 3);
        Vector3d v0 = new Vector3d(5.2, 4.7, 8.2);
        Line line1 = new Line(p0, v0);

        Vector3d p1 = new Vector3d(3, 2, 1);
        Vector3d v1 = new Vector3d(1.7, -5.1, 3.3);
        Line line2 = new Line(p1, v1);

        Vector3d[] pp = nearestTwoPointsOnTheirLinesRespectively(line1, line2);
        System.out.println("pp[0] = " + pp[0]);
        System.out.println("pp[1] = " + pp[1]);
        System.out.println("距离——");
        System.out.println(Vector3d.subtract(pp[0], pp[1]).length());

        System.out.println("额外计算的距离——");
        System.out.println(nearestDistanceOfTwoLines(line1, line2));
        System.out.println(nearestDistanceOfTwoLines(line2, line1));

        Plot3d.plot3(line1.pathForPlot3(12), "");
        Plot3d.plot3(line2.pathForPlot3(12), "");

        Plot3d.plot3(pp, "");
        Plot3d.show();

        //pp[0] = [0.7676194053106598 1.789963693261558 2.6335536776052715]
        //pp[1] = [3.113939959591246 1.6581801212262626 1.2211775686183006]
        //距离——
        //2.741786520963132
        //额外计算的距离——
        //2.7417865209631316
        //2.7417865209631316
    }

    private static void test1() {
        Vector3d p1 = new Vector3d(1, 1, 1);
        Vector3d p2 = new Vector3d(1, 2, 3);
        System.out.println("p2 = " + p2);
        System.out.println(p1.length());


        System.out.println(add(p1, p2));
        System.out.println(subtract(p1, p2));

        p2.subtractSelf(p1);
        System.out.println(p2);

        System.out.println("-----------------");
        System.out.println("p2 = " + p2);
        double[][] doubles = {{1, 2, 3}, {2, 3, 4}, {3, 4, 5}};
        p2.selfMatmul(doubles);
        System.out.println("p2 = " + p2);
        System.out.println("-----------------");

        System.out.println(p1);
        p1.normal();
        System.out.println(p1);
        System.out.println(1 / Math.sqrt(3));
    }
}
