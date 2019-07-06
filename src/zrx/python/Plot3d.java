package zrx.python;


import zrx.base.Vector3d;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 使用方法，调用plot3画图，可以调用若干次
 * 最后调用show()展示图片
 */

public class Plot3d {
    private static boolean prepareHead = false;
    private static File pyFile;
    private static PrintWriter pyPrintWriter;

    /**
     * 3d画图，传入一个Vector3d数组，从[0]依次画图
     * @param rs Vector3d数组
     * @param describe python画图plot3() 中的描述符，如" ,'r+' "
     */
    public static void plot3(Vector3d[] rs,String describe) {
        int len = rs.length;
        List<Double> xs = new ArrayList<>(len);
        List<Double> ys = new ArrayList<>(len);
        List<Double> zs = new ArrayList<>(len);
        Arrays.stream(rs).forEach(r -> {
            xs.add(r.x);
            ys.add(r.y);
            zs.add(r.z);
        });

        polt3MakeFile(xs, ys, zs,describe);
    }

    public static void plot3(Vector3d[] rs){
        plot3(rs,"");
    }

    /**
     * 画一个点
     * @param p 点
     * @param describe python 画图描述符
     */
    public static void plotPoint(Vector3d p,String describe){
        plot3(new Vector3d[]{p},describe);
    }

    public static void plot3(List<Double> xs, List<Double> ys, List<Double> zs,String describe) {
        polt3MakeFile(xs, ys, zs,describe);
    }

    /**
     * 绘制一个盒子
     * 主要目的是 CCT 看起来不扭曲
     * @param box 盒子坐标  设定范围[xmin,xmax,ymin,ymax,zmin,zmax]
     */
    public static void setBoxSize(double[] box) {
        prepareHead();
        try {
            pyPrintWriter.println("xmin = "+box[0]);
            pyPrintWriter.println("xmax = "+box[1]);
            pyPrintWriter.println("ymin = "+box[2]);
            pyPrintWriter.println("ymax = "+box[3]);
            pyPrintWriter.println("zmin = "+box[4]);
            pyPrintWriter.println("zmax = "+box[5]);
            pyPrintWriter.println("");
            pyPrintWriter.println("ax.plot([xmin], [ymin], [zmin], 'w')");
            pyPrintWriter.println("ax.plot([xmax], [ymax], [zmax], 'w')");
            pyPrintWriter.println("");

        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    /**
     * 绘制一个正方体盒子
     * -size,size,-size,size,-size,size
     * @param size 半边长
     */
    public static void setCube(double size){
        setBoxSize(new double[]{-size,size,-size,size,-size,size});
    }

    private static void polt3MakeFile(List<Double> xs, List<Double> ys, List<Double> zs, String describe) {
        prepareHead();
        try {
            pyPrintWriter.println("x = " + printFomatPY(xs));
            pyPrintWriter.println("y = " + printFomatPY(ys));
            pyPrintWriter.println("z = " + printFomatPY(zs));
            pyPrintWriter.println("ax.plot(x, y, z"+describe+")");
            pyPrintWriter.println();

        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    private static void prepareHead() {
        if (!prepareHead) {
            try {
                pyFile = new File("./pyScript/plot3.py");
                pyFile.createNewFile();
                pyPrintWriter = new PrintWriter(new FileWriter(pyFile));

                //head
                pyPrintWriter.println("import matplotlib as mpl");
                pyPrintWriter.println("from mpl_toolkits.mplot3d import Axes3D");
                pyPrintWriter.println("import matplotlib.pyplot as plt");
                pyPrintWriter.println();
                pyPrintWriter.println("fig = plt.figure()");
                pyPrintWriter.println("ax = fig.gca(projection='3d')");
                pyPrintWriter.println();

            } catch (Exception e) {
                pyPrintWriter.close();
                e.printStackTrace();
                System.exit(-1);
            }
            prepareHead = true;
        }
    }

    public static void show() {
        if (!prepareHead)
            System.exit(-1);

        try {
            pyPrintWriter.println("ax.grid(False)");
            pyPrintWriter.println("plt.show()");
            pyPrintWriter.println();

            pyPrintWriter.close();

            File dir = pyFile.getParentFile();
            Runtime run = Runtime.getRuntime();
            Process exec = run.exec("python .\\plot3.py", null, dir);
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

    private static void demo1() {
        List<Double> xs = List.of(1.0, 2.0);
        List<Double> ys = List.of(1.0, 2.0);
        List<Double> zs = List.of(1.0, 2.0);
        polt3MakeFile(xs, ys, zs,"");
        xs = List.of(-1.0, 12.0);
        ys = List.of(-2.0, 22.0);
        zs = List.of(-3.0, 32.0);
        polt3MakeFile(xs, ys, zs,"");
        show();
    }

    private static void prinFomatPYtest() {
        List<Double> list = new ArrayList<>();
        list.add(1.0);
        list.add(1.0);
        System.out.println(printFomatPY(list));
    }

    private static void demo() throws Exception {
        File pyFile = new File("./pyScript/plot.py");
        pyFile.createNewFile();
        FileWriter pyFileWriter = new FileWriter(pyFile);
        pyFileWriter.write("print('hello jp')");
        pyFileWriter.close();

        File dir = pyFile.getParentFile();
        Runtime run = Runtime.getRuntime();
        Process exec = run.exec("python .\\plot.py", null, dir);
        exec.waitFor();
    }

}
