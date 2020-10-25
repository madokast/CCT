package cn.edu.hust.zrx.cct.base.python;

import cn.edu.hust.zrx.cct.Logger;
import cn.edu.hust.zrx.cct.advanced.MathFunction;
import cn.edu.hust.zrx.cct.base.BaseUtils;
import cn.edu.hust.zrx.cct.base.point.Point2;

import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.PrintWriter;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.function.DoubleFunction;
import java.util.function.Function;
import java.util.stream.Collectors;


public class Plot2d {
    private static boolean prepareHead = false;
    private static File pyFile;
    private static PrintWriter pyPrintWriter;

    //describe
    public static final String RED_POINT = ",'ro'";
    public static final String BLACK_POINT = ",'ko'";
    public static final String YELLOW_POINT = ",'yo'";
    public static final String YELLOW_SMALL_POINT = ",'y.'";
    public static final String BLACK_SMALL_POINT = ",'k.'";
    public static final String GREEN_POINT = ",'go'";
    public static final String BLUE_POINT = ",'bo'";

    public static final String BLUE_ADD = ",'b+'";


    public static final String RED_LINE = ",'r'";
    public static final String BLACK_LINE = ",'k'";
    public static final String YELLOW_LINE = ",'y'";
    public static final String GREEN_LINE = ",'g'";
    public static final String BLUE_LINE = ",'b'";
    public static final String WHITE_LINE = ",'w'";
    public static final String TRANSPARENCY_LINE = ",'w',alpha=0";

    public static final String BLACK_UP_TRI = ",'k^'";
    public static final String RED_UP_TRI = ",'r^'";
    public static final String YELLOW_UP_TRI = ",'y^'";
    public static final String GREEN_UP_TRI = ",'g^'";
    public static final String BLUE_UP_TRI = ",'b^'";

    public static final String GREY_LINE = ",color='grey', linestyle='-',linewidth = '1'";


    public static final String GREY_DASH = ",color='grey', linestyle='--'";
    public static final String PINK_DASH = ",color='pink', linestyle='--'";
    public static final String RED_DASH = ",color='red', linestyle='--'";
    public static final String BLACK_DASH = ",color='black', linestyle='--'";
    public static final String BLUE_DASH = ",color='blue', linestyle='--'";
    public static final String YELLOW_DASH = ",color='yellow', linestyle='--'";

    public static final String GREY_DOt_DASH = ",color='grey', linestyle='-.'";
    public static final String PINK_DOt_DASH = ",color='pink', linestyle='-.'";
    public static final String RED_DOt_DASH = ",color='red', linestyle='-.'";
    public static final String BLACK_DOt_DASH = ",color='black', linestyle='-.'";
    public static final String BLUE_DOt_DASH = ",color='blue', linestyle='-.'";


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

    public static synchronized void plot2(List<Point2> rs, String describe) {
        Point2[] point2s = new Point2[rs.size()];
        for (int i = 0; i < point2s.length; i++) {
            point2s[i] = rs.get(i);
        }

        plot2(point2s, describe);
    }

    public static void plot2(List<Double> xList, List<Double> yList) {
        plot2(xList, yList, BLUE_LINE);
    }

    public static void plot2(List<Double> xList, List<Double> yList, String describe) {
        Point2[] point2s = new Point2[xList.size()];
        for (int i = 0; i < point2s.length; i++) {
            point2s[i] = Point2.create(xList.get(i), yList.get(i));
        }

        plot2(point2s, describe);
    }


    // 画长方形
    public static void plotBox(double minX, double maxX, double minY, double maxY, String describe) {
        List<Point2> box = List.of(
                Point2.create(minX, minY),
                Point2.create(minX, maxY),
                Point2.create(maxX, maxY),
                Point2.create(maxX, minY),
                Point2.create(minX, minY)
        );

        plot2(box, describe);
    }

    /**
     * 画条形，条形的底边在x轴上
     *
     * @param xCenter 底边中心的 x 值
     * @param width   宽度
     * @param height  高度
     */
    public static void plotBox(double xCenter, double width, double height, String describe) {
        plotBox(
                xCenter - width / 2,
                xCenter + width / 2,
                0,
                height,
                describe
        );
    }


    /**
     * 谐波分析
     * 本质是画条形，条形的底边在x轴上
     *
     * @param array        谐波数组
     * @param describe     画图描述
     * @param centerOffset 条形图中心 和 整点的偏差
     * @param width        条形宽度
     */
    public static void harmonicAnalysis(double[] array, String describe, double centerOffset, double width) {
        for (int i = 0; i < array.length; i++) {
            double h = array[i];

            plotBox(i + centerOffset, width, h, describe);
        }
    }

    public static void harmonicAnalysisWithOut0(double[] array, String describe, double centerOffset, double width) {
        for (int i = 1; i < array.length; i++) {
            double h = array[i];

            plotBox(i + centerOffset, width, h, describe);
        }
    }


    public static void plot2circle(List<Point2> rs, String describe) {
        List<Point2> list = new ArrayList<>(rs);
        list.add(list.get(0));

        plot2(list, describe);
    }


    public static void plot2(List<Point2> rs) {
        plot2(rs, "");
    }

    public static void plot2(Function<Double, Double> function, Point2 interval, int number, String describe) {
        List<Point2> collect = BaseUtils.Python.linspaceStream(interval.x, interval.y, number)
                .mapToObj(x -> Point2.create(x, function.apply(x)))
                .collect(Collectors.toList());
        plot2(collect, describe);
    }

    public static void plot2(double[] xs, double[] ys, String describe) {
        int len = Math.min(xs.length, ys.length);
        List<Point2> point2s = new ArrayList<>(len);

        for (int i = 0; i < len; i++) {
            point2s.add(Point2.create(xs[i], ys[i]));
        }

        plot2(point2s, describe);
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
     * 画多个点
     *
     * @param point2s  点
     * @param describe python 画图描述符
     */
    @Deprecated // 直接用 plot2 即可
    public static void plotPoints(List<Point2> point2s, String describe) {
        point2s.forEach(point2 -> plotPoint(point2, describe));
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

    public static void ylim(double min, double max) {
        prepareHead();
        try {
            pyPrintWriter.println("plt.ylim(" + min + ", " + max + ")");
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
     * @param xs       横坐标值 多个
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
    public static void plotYLine(double x, final List<Point2>... point2sList) {
        List<Point2> point2s = new ArrayList<>();
        for (List<Point2> s : point2sList) {
            point2s.addAll(s);
        }
        plotYLine(x, point2s);
    }

    /**
     * 画平行于Y轴的线段(多条) 自适应模式
     *
     * @param xs      横坐标值 多个
     * @param point2s 已绘制的数据
     */
    public static void plotYLines(List<Double> xs, List<Point2> point2s) {
        Logger.getLogger().info("xs = " + xs);
        xs.forEach(x -> plotYLine(x, point2s));
    }

    @SafeVarargs
    public static void plotYLines(List<Double> xs, final List<Point2>... point2sList) {
        xs.forEach(x -> plotYLine(x, point2sList));
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
        //
//        font2 = {'family' : 'Times New Roman',
//                'weight' : 'normal',
//                'size'   : 30,

        if (pyPrintWriter != null)
            pyPrintWriter.println("plt.xlabel(\"\"\"" + label + "\"\"\")");
        else
            System.err.println("plot2d::xLabel called without any drawing");
    }

    public static void xLabel(String label, int size) {
        if (pyPrintWriter != null) {
            pyPrintWriter.println("fontXLabel = {'family' : 'Times New Roman',");
            pyPrintWriter.println("'weight' : 'normal',");
            pyPrintWriter.println("'size'   : " + size + ",");
            pyPrintWriter.println("}");
            pyPrintWriter.println("plt.xlabel('" + label + "',fontXLabel)");
        } else
            System.err.println("plot2d::xLabel called without any drawing");
    }

    public static void yLabel(String label) {
        if (pyPrintWriter != null)
            pyPrintWriter.println("plt.ylabel(\"\"\"" + label + "\"\"\")");
        else
            System.err.println("plot2d::xLabel called without any drawing");
    }

    public static void yLabel(String label, int size) {
        if (pyPrintWriter != null) {
            pyPrintWriter.println("fontYLabel = {'family' : 'Times New Roman',");
            pyPrintWriter.println("'weight' : 'normal',");
            pyPrintWriter.println("'size'   : " + size + ",");
            pyPrintWriter.println("}");
            pyPrintWriter.println("plt.ylabel('" + label + "',fontYLabel)");
        } else
            System.err.println("plot2d::xLabel called without any drawing");
    }

    public static void title(String title) {
        if (pyPrintWriter != null)
            pyPrintWriter.println("plt.title(\"\"\"" + title + "\"\"\")");
        else
            System.err.println("plot2d::xLabel called without any drawing");
    }

    public static void title(String title, int size) {
        if (pyPrintWriter != null) {
            pyPrintWriter.println("fontTitle = {'family' : 'Microsoft YaHei',");
            pyPrintWriter.println("'weight' : 'normal',");
            pyPrintWriter.println("'size'   : " + size + ",");
            pyPrintWriter.println("}");
            pyPrintWriter.println("plt.title('" + title + "',fontTitle)");
        } else
            System.err.println("plot2d::xLabel called without any drawing");
    }

    public static void axisFontSize(int size) {
        if (pyPrintWriter != null) {
            pyPrintWriter.println("plt.xticks(fontproperties = 'Times New Roman', size = " + size + ")");
            pyPrintWriter.println("plt.yticks(fontproperties = 'Times New Roman', size = " + size + ")");
        } else
            System.err.println("plot2d::xLabel called without any drawing");
    }

    public static void legend(int size, String... labels) {
//        plt.legend(labels=['up', 'down'])
        if (pyPrintWriter != null) {
            StringBuilder sb = new StringBuilder();
            sb.append("plt.legend(labels=[");
            for (int i = 0; i < labels.length - 1; i++) {
                sb.append("'").append(labels[i]).append("', ");
            }
            sb.append("'").append(labels[labels.length - 1]).append("'], ");
            sb.append("prop={'family' : 'Times New Roman', 'size'   : " + size + "})");
            pyPrintWriter.println(sb.toString());
        } else {
            System.err.println("plot2d::xLabel called without any drawing");
        }
    }

    public static void legend(int size, List<String> labels) {
        legend(size, labels.toArray(String[]::new));
    }

    public static void grid() {
        if (pyPrintWriter != null) {
            pyPrintWriter.println("plt.grid()");
        } else
            System.err.println("plot2d::xLabel called without any drawing");
    }

    public static void info(String xLabel, String yLabel, String title) {
        xLabel(xLabel);
        yLabel(yLabel);
        title(title);
    }

    public static void info(String xLabel, String yLabel, String title, int size) {
        xLabel(xLabel, size);
        yLabel(yLabel, size);
        title(title, size);
        axisFontSize(size);
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
