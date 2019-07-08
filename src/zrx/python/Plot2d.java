package zrx.python;

import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import zrx.base.Vector2d;

public class Plot2d {
    private static boolean prepareHead = false;
    private static File pyFile;
    private static PrintWriter pyPrintWriter;

    //describe
    public static final String RED_POINT = ",'ro'";
    public static final String BLACK_POINT = ",'ko'";
    public static final String YELLOW_POINT = ",'yo'";
    public static final String GREEN_POINT = ",'go'";
    public static final String BLUE_POINT = ",'bo'";

    public static final String BLUE_ADD = ",'b+'";


    public static final String RED_LINE = ",'r'";
    public static final String BLACK_LINE = ",'k'";
    public static final String YELLOW_LINE = ",'y'";
    public static final String GREEN_LINE = ",'g'";
    public static final String BLUE_LINE = ",'b'";

    /**
     * 2d画图，传入一个Vector2d数组，从[0]依次画图
     * @param rs Vector2d数组
     * @param describe python画图plot() 中的描述符，如" ,'r+' "
     */
    public static void plot2(Vector2d[] rs,String describe) {
        int len = rs.length;
        List<Double> xs = new ArrayList<>(len);
        List<Double> ys = new ArrayList<>(len);
        Arrays.stream(rs).forEach(r -> {
            xs.add(r.x);
            ys.add(r.y);
        });

        polt2MakeFile(xs, ys,describe);
    }

    public static void plot2(Vector2d[] rs){
        plot2(rs,"");
    }

    /**
     * 画一个点
     * @param p 点
     * @param describe python 画图描述符
     */
    public static void plotPoint(Vector2d p,String describe){
        plot2(new Vector2d[]{p},describe);
    }

    public static void plotFirstPoint(Vector2d[] vs,String describe){
        plotPoint(vs[0],describe);
    }

    public static void plotLastPoint(Vector2d[] vs,String describe){
        plotPoint(vs[vs.length-1],describe);
    }

    /**
     * 画一个点
     * @param p 点
     */
    public static void plotPoint(Vector2d p){
        plot2(new Vector2d[]{p},"");
    }

    /**
     * 画矢量
     * 画一个位于 p 的矢量，矢量方向为 v ，长度 len
     * @param p 矢量原点
     * @param v 矢量方向
     * @param len 矢量长度
     */
    public static void plotVector(final Vector2d p,final Vector2d v,final double len){
        Vector2d nv = Vector2d.normal(v);
        Vector2d q = Vector2d.add(p,Vector2d.dot(len,nv));
        plot2(new Vector2d[]{p,q},",'k'");
        plotPoint(q,",'kv'");
    }

    /**
     * 画矢量
     * 画一个位于 原点 的矢量，矢量方向为 v ，长度 len
     * @param v 矢量方向
     * @param len 矢量长度
     */
    public static void plotVector(final Vector2d v,final double len){
        plotVector(Vector2d.getZeros(),v,len);
    }

    /**
     * 画矢量
     * 画一个位于 原点 的矢量，矢量方向为 v ，长度 v.len
     * @param v 矢量方向
     */
    public static void plotVector(final Vector2d v){
        plotVector(Vector2d.getZeros(),v,v.length());
    }

    public static void plot2(List<Double> xs, List<Double> ys,String describe) {
        polt2MakeFile(xs, ys,describe);
    }

    /**
     * 绘制一个格子
     * @param box 盒子坐标  设定范围[xmin,xmax,ymin,ymax]
     */
    public static void setBoxSize(double[] box) {
        prepareHead();
        try {
            pyPrintWriter.println("xmin = "+box[0]);
            pyPrintWriter.println("xmax = "+box[1]);
            pyPrintWriter.println("ymin = "+box[2]);
            pyPrintWriter.println("ymax = "+box[3]);
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
     * @param size 半边长
     */
    public static void setCube(double size){
        setBoxSize(new double[]{-size,size,-size,size});
    }


    private static void polt2MakeFile(List<Double> xs, List<Double> ys, String describe) {
        prepareHead();
        try {
            pyPrintWriter.println("x = " + printFomatPY(xs));
            pyPrintWriter.println("y = " + printFomatPY(ys));
            pyPrintWriter.println("plt.plot(x, y"+describe+")");
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

    public static void equal(){
        pyPrintWriter.println("plt.axis('equal')");
    }

    public static void show() {
        if (!prepareHead)
            System.exit(-1);

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
            System.exit(-1);
        }
    }

    public static void showThread(){
        new Thread(()->show()).start();
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

    /**
     * 测试
     * @param args
     */
    public static void main(String[] args) {

    }

    private static void demo1(){
        Vector2d p1 = new Vector2d(1,3);
        Vector2d p2 = new Vector2d(2,4);
        Vector2d p3 = new Vector2d(3,5);

        plot2(new Vector2d[]{p1,p2,p3},",'r+'");

        Vector2d p4 = new Vector2d(3,-1);
        Vector2d p5 = new Vector2d(2,2);
        Vector2d p6 = new Vector2d(1,1);

        plot2(new Vector2d[]{p4,p5,p6},",'ko'");

        show();
    }

}
