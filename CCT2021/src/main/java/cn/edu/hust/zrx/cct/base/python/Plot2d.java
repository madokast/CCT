package cn.edu.hust.zrx.cct.base.python;

import cn.edu.hust.zrx.cct.base.BaseUtils;
import cn.edu.hust.zrx.cct.base.point.Point2;

import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.function.DoubleFunction;


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

    public static final String GREY_LINE = ",color='grey', linestyle='-',linewidth = '1'";
    public static final String GREY_DASH = ",color='grey', linestyle='--'";
    public static final String PINK_DASH = ",color='pink', linestyle='--'";
    public static final String RED_DASH = ",color='red', linestyle='--'";
    public static final String BLACK_DASH = ",color='black', linestyle='--'";
    public static final String BLUE_DASH = ",color='blue', linestyle='--'";

    /**
     * 2d画图，传入一个Vector2d数组，从[0]依次画图
     *
     * @param rs       Vector2d数组
     * @param describe python画图plot() 中的描述符，如" ,'r+' "
     */
    public static void plot2(Point2[] rs, String describe) {
        int len = rs.length;
        List<Double> xs = new ArrayList<>(len);
        List<Double> ys = new ArrayList<>(len);
        Arrays.stream(rs).forEach(r -> {
            xs.add(r.x);
            ys.add(r.y);
        });

        polt2MakeFile(xs, ys, describe);
    }

    public static void plot2(Point2[] rs) {
        plot2(rs, "");
    }

    public static void plot2(List<Point2> rs, String describe) {
        Point2[] point2s = new Point2[rs.size()];
        for (int i = 0; i < point2s.length; i++) {
            point2s[i] = rs.get(i);
        }

        plot2(point2s, describe);
    }

    public static void plot2(List<Point2> rs) {
        plot2(rs, "");
    }

    /**
     * 画一个点
     *
     * @param p        点
     * @param describe python 画图描述符
     */
    public static void plotPoint(Point2 p, String describe) {
        plot2(new Point2[]{p}, describe);
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
            pyPrintWriter.println("plt.plot([xmin], [ymin], 'w')");
            pyPrintWriter.println("plt.plot([xmax], [ymax], 'w')");
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
                new Point2[]{
                        Point2.create(x, yMax),
                        Point2.create(x, yMin)
                },
                describe
        );
    }

    /**
     * 画平行于Y轴的线段，多个
     *
     * @param xs        横坐标值 多个
     * @param yMax     纵坐标终点
     * @param yMin     纵坐标起点
     * @param describe py描述符
     */
    public static void plotYLines(List<Double> xs, double yMax, double yMin, String describe) {
        xs.forEach(x -> plotYLine(x, yMax, yMin, describe));
    }

    /**
     * 画平行于Y轴的线段 自适应模式
     *
     * @param x       横坐标值
     * @param point2s 已绘制的数据
     */
    public static void plotYLine(double x, List<Point2> point2s) {
        DoubleSummaryStatistics doubleSummaryStatistics =
                point2s.stream().mapToDouble(p -> p.y).summaryStatistics();

        double max = doubleSummaryStatistics.getMax();
        double min = doubleSummaryStatistics.getMin();

        double length = max - min;
        double p10 = length / 10;

        max += p10;
        min -= p10;


        plotYLine(x, max, min, GREY_DASH);
    }

    @SafeVarargs
    public static void plotYLine(double x, final List<Point2>... point2sList){
        List<Point2> point2s = new ArrayList<>();
        for (List<Point2> s : point2sList) {
            point2s.addAll(s);
        }
        plotYLine(x,point2s);
    }

    /**
     * 画平行于Y轴的线段(多条) 自适应模式
     *
     * @param xs      横坐标值 多个
     * @param point2s 已绘制的数据
     */
    public static void plotYLines(List<Double> xs, List<Point2> point2s) {
        xs.forEach(x -> plotYLine(x, point2s));
    }

    @SafeVarargs
    public static void plotYLines(List<Double> xs,final List<Point2>... point2sList){
        xs.forEach(x->plotYLine(x,point2sList));
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
                new Point2[]{
                        Point2.create(xMax, y),
                        Point2.create(xMin, y)
                },
                describe
        );
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
//                System.out.println("new File(\".\").getAbsolutePath() = " + new File(".").getAbsolutePath());

                pyFile = new File("./pyScript/plot2.py");
                pyFile.createNewFile();
                pyPrintWriter = new PrintWriter(new FileWriter(pyFile));

                //head
                pyPrintWriter.println("import matplotlib.pyplot as plt");
                pyPrintWriter.println("plt.rcParams['font.sans-serif']=['SimHei'] #用来正常显示中文标签");
                pyPrintWriter.println("plt.rcParams['axes.unicode_minus']=False #用来正常显示负号");
                pyPrintWriter.println();

            } catch (Exception e) {
                e.printStackTrace();
                pyPrintWriter.close();
                System.exit(-1);
            }
            prepareHead = true;
        }
    }

    public static void equal() {
        if (pyPrintWriter != null)
            pyPrintWriter.println("plt.axis('equal')");
        else
            System.err.println("plot2d::equal called without any drawing");
    }

    public static void xLabel(String label) {
        if (pyPrintWriter != null)
            pyPrintWriter.println("plt.xlabel(\"\"\"" + label + "\"\"\")");
        else
            System.err.println("plot2d::xLabel called without any drawing");
    }

    public static void yLabel(String label) {
        if (pyPrintWriter != null)
            pyPrintWriter.println("plt.ylabel(\"\"\"" + label + "\"\"\")");
        else
            System.err.println("plot2d::xLabel called without any drawing");
    }

    public static void title(String title) {
        if (pyPrintWriter != null)
            pyPrintWriter.println("plt.title(\"\"\"" + title + "\"\"\")");
        else
            System.err.println("plot2d::xLabel called without any drawing");
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
        new Thread(Plot2d::show).start();
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
}