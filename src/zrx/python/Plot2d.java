package zrx.python;

import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import zrx.Tools.ArrayMerge;
import zrx.Tools.Numpy;
import zrx.base.Vector2d;

public class Plot2d {
    private static boolean prepareHead = false;
    private static File pyFile;
    private static PrintWriter pyPrintWriter;

    //describe
    public static final String RED_POINT = ",'ro'";
    public static final String BLACK_POINT = ",'ko'";
    public static final String YELLOW_POINT = ",'yo'";
    public static final String YELLOW_SMALL_POINT = ",'y.'";
    public static final String GREEN_POINT = ",'go'";
    public static final String BLUE_POINT = ",'bo'";

    public static final String BLUE_ADD = ",'b+'";


    public static final String RED_LINE = ",'r'";
    public static final String BLACK_LINE = ",'k'";
    public static final String YELLOW_LINE = ",'y'";
    public static final String GREEN_LINE = ",'g'";
    public static final String BLUE_LINE = ",'b'";

    public static final String GREY_DASH = ",color='grey', linestyle='--'";
    public static final String GREY_LINE = ",color='grey', linestyle='-',linewidth = '1'";
    public static final String PINK_DASH = ",color='pink', linestyle='--'";
    public static final String RED_DASH = ",color='red', linestyle='--'";

    /**
     * 2d画图，传入一个Vector2d数组，从[0]依次画图
     *
     * @param rs       Vector2d数组
     * @param describe python画图plot() 中的描述符，如" ,'r+' "
     */
    public static void plot2(Vector2d[] rs, String describe) {
        int len = rs.length;
        List<Double> xs = new ArrayList<>(len);
        List<Double> ys = new ArrayList<>(len);
        Arrays.stream(rs).forEach(r -> {
            xs.add(r.x);
            ys.add(r.y);
        });

        polt2MakeFile(xs, ys, describe);
    }

    public static void plot2(Vector2d[] rs) {
        plot2(rs, "");
    }

    /**
     * 画一个点
     *
     * @param p        点
     * @param describe python 画图描述符
     */
    public static void plotPoint(Vector2d p, String describe) {
        plot2(new Vector2d[]{p}, describe);
    }

    public static void plotPoint(double x, double y, String describe) {
        plotPoint(Vector2d.getOne(x, y), describe);
    }

    public static void plotPoints(Vector2d[] ps, String describe) {
        for (Vector2d p : ps) {
            plotPoint(p, describe);
        }
    }

    public static void plot2Circle(Vector2d[] rs, String describe) {
        Vector2d[] circle = ArrayMerge.merge(rs,new Vector2d[]{rs[0]});
        plot2(circle,describe);
    }


    public static void plotFirstPoint(Vector2d[] vs, String describe) {
        plotPoint(vs[0], describe);
    }

    public static void plotLastPoint(Vector2d[] vs, String describe) {
        plotPoint(vs[vs.length - 1], describe);
    }

    /**
     * 画一个点
     *
     * @param p 点
     */
    public static void plotPoint(Vector2d p) {
        plot2(new Vector2d[]{p}, "");
    }

    /**
     * 画矢量
     * 画一个位于 p 的矢量，矢量方向为 v ，长度 len
     *
     * @param p   矢量原点
     * @param v   矢量方向
     * @param len 矢量长度
     */
    public static void plotVector(final Vector2d p, final Vector2d v, final double len) {
        Vector2d nv = Vector2d.normal(v);
        Vector2d q = Vector2d.add(p, Vector2d.dot(len, nv));
        plot2(new Vector2d[]{p, q}, ",'k'");
        plotPoint(q, ",'kv'");
    }

    /**
     * 画矢量
     * 画一个位于 原点 的矢量，矢量方向为 v ，长度 len
     *
     * @param v   矢量方向
     * @param len 矢量长度
     */
    public static void plotVector(final Vector2d v, final double len) {
        plotVector(Vector2d.getZeros(), v, len);
    }

    /**
     * 画矢量
     * 画一个位于 原点 的矢量，矢量方向为 v ，长度 v.len
     *
     * @param v 矢量方向
     */
    public static void plotVector(final Vector2d v) {
        plotVector(Vector2d.getZeros(), v, v.length());
    }

    /**
     * 连线画图
     *
     * @param describe 描述符
     * @param points   需要连起来的点
     */
    public static void plot2(String describe, Vector2d... points) {
        for (int i = 0; i < points.length - 1; i++) {
            plot2(new Vector2d[]{
                    points[i], points[i + 1]
            }, describe);
        }
    }

    /**
     * 连线画图。线为灰色点划线
     * 专用于硬边模型画图
     *
     * @param points 点
     */
    public static void plotGREY_DASH(Vector2d... points) {
        plot2(Plot2d.GREY_DASH, points);
    }

    public static void plotPINK_DASH(Vector2d... points) {
        plot2(Plot2d.PINK_DASH, points);
    }


    /**
     * 画平行于Y轴的线段
     *
     * @param x        横坐标值
     * @param yMax     纵坐标终点
     * @param yMin     纵坐标起点
     * @param describe py描述符
     */
    public static void plotYLine(double x, double yMax, double yMin, String describe) {
        plot2(
                new Vector2d[]{
                        Vector2d.getOne(x, yMax),
                        Vector2d.getOne(x, yMin)
                },
                describe
        );
    }

    /**
     * 画平行于X轴的线段
     *
     * @param y        纵坐标
     * @param xMax     横坐标终点
     * @param xMin     横坐标起点
     * @param describe py描述符
     */
    public static void plotXLine(double y, double xMax, double xMin, String describe) {
        plot2(
                new Vector2d[]{
                        Vector2d.getOne(xMax, y),
                        Vector2d.getOne(xMin, y)
                },
                describe
        );
    }

    public static void plot2(List<Double> xs, List<Double> ys, String describe) {
        polt2MakeFile(xs, ys, describe);
    }

    public static void plot2(double[] xs, double[] ys, String describe) {
        List<Double> xList = new ArrayList<>(xs.length);
        List<Double> yList = new ArrayList<>(xs.length);

        for (int i = 0; i < xs.length; i++) {
            xList.add(i, xs[i]);
            yList.add(i, ys[i]);
        }

        polt2MakeFile(xList, yList, describe);
    }

    /**
     * 绘制一个格子
     *
     * @param box 盒子坐标  设定范围[xmin,xmax,ymin,ymax]
     */
    public static void setBoxSize(double[] box) {
        prepareHead();
        try {
            pyPrintWriter.println("xmin = " + box[0]);
            pyPrintWriter.println("xmax = " + box[1]);
            pyPrintWriter.println("ymin = " + box[2]);
            pyPrintWriter.println("ymax = " + box[3]);
            pyPrintWriter.println("");
            pyPrintWriter.println("ax.plot([xmin], [ymin], 'w')");
            pyPrintWriter.println("ax.plot([xmax], [ymax], 'w')");
            pyPrintWriter.println("");

        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    /**
     * 绘制一个正方体格子
     * -size,size,-size,size
     *
     * @param size 半边长
     */
    public static void setCube(double size) {
        setBoxSize(new double[]{-size, size, -size, size});
    }

    /**
     * 设定横坐标范围
     *
     * @param min 最小值
     * @param max 最大值
     */
    public static void xlim(double min, double max) {
        prepareHead();
        try {
            pyPrintWriter.println("plt.xlim(" + min + ", " + max + ")");
            pyPrintWriter.println();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }


    private static void polt2MakeFile(List<Double> xs, List<Double> ys, String describe) {
        prepareHead();
        try {
            pyPrintWriter.println("x = " + printFomatPY(xs));
            pyPrintWriter.println("y = " + printFomatPY(ys));
            pyPrintWriter.println("plt.plot(x, y" + describe + ")");
            pyPrintWriter.println();

        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    private static void prepareHead() {
        if (!prepareHead) {
            try {
                pyFile = new File("./pyScript/plot2.py");
                pyFile.createNewFile();
                pyPrintWriter = new PrintWriter(new FileWriter(pyFile));

                //head
                pyPrintWriter.println("import matplotlib.pyplot as plt");
                pyPrintWriter.println();

            } catch (Exception e) {
                pyPrintWriter.close();
                e.printStackTrace();
                System.exit(-1);
            }
            prepareHead = true;
        }
    }

    public static void equal() {
        if(pyPrintWriter!=null)
            pyPrintWriter.println("plt.axis('equal')");
        else
            System.err.println("plot2d::equal called without any drawing");
    }

    public static void show() {
        if (!prepareHead) {
            System.err.println("2d绘图空");
            Thread.currentThread().stop();
        }

        try {

            pyPrintWriter.println("plt.show()");
            pyPrintWriter.println();

            pyPrintWriter.close();

            File dir = pyFile.getParentFile();
            Runtime run = Runtime.getRuntime();
            Process exec = run.exec("python .\\plot2.py", null, dir);
            InputStream inputStream = exec.getInputStream();
            InputStream errorStream = exec.getErrorStream();
            exec.waitFor();

            if (inputStream.available() > 0) {
                System.out.println(new String(inputStream.readAllBytes()));
            }
            if (errorStream.available() > 0) {
                System.out.println(new String(errorStream.readAllBytes()));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showThread() {
        new Thread(() -> show()).start();
    }

    private static String printFomatPY(List<Double> ls) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        sb.append(ls.get(0));
        for (int i = 1; i < ls.size(); i++) {
            sb.append(", " + ls.get(i));
        }
        sb.append("]");

        return sb.toString();
    }

    public static void plotCosyEngeFun(double[] arr, String describe) {
        final double[] xArr = Numpy.linspace(-3, 5, 100);
        final double[] yArr = new double[xArr.length];
        for (int i = 0; i < yArr.length; i++) {
            yArr[i] = 1.0 / (1 + Math.exp(arr[0] +
                    arr[1] * Math.pow(xArr[i], 1) +
                    arr[2] * Math.pow(xArr[i], 2) +
                    arr[3] * Math.pow(xArr[i], 3) +
                    arr[4] * Math.pow(xArr[i], 4) +
                    arr[5] * Math.pow(xArr[i], 5)
            ));
        }

        plot2(xArr, yArr, describe);
    }

    /**
     * 测试
     *
     * @param args
     */
    public static void main(String[] args) {

    }

    private static void demo1() {
        Vector2d p1 = new Vector2d(1, 3);
        Vector2d p2 = new Vector2d(2, 4);
        Vector2d p3 = new Vector2d(3, 5);

        plot2(new Vector2d[]{p1, p2, p3}, ",'r+'");

        Vector2d p4 = new Vector2d(3, -1);
        Vector2d p5 = new Vector2d(2, 2);
        Vector2d p6 = new Vector2d(1, 1);

        plot2(new Vector2d[]{p4, p5, p6}, ",'ko'");

        show();
    }

}
