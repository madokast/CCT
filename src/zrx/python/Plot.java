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

public class Plot {
    private static boolean prepareHead=false;
    private static File pyFile;
    private static PrintWriter pyPrintWriter;

    public static void plot3(Vector3d[] rs){
        int len = rs.length;
        List<Double> xs = new ArrayList<>(len);
        List<Double> ys = new ArrayList<>(len);
        List<Double> zs = new ArrayList<>(len);
        Arrays.stream(rs).forEach(r->{
            xs.add(r.x);
            ys.add(r.y);
            zs.add(r.z);
        });

        polt3MakeFile(xs,ys,zs);
    }

    public static void plot3(List<Double> xs,List<Double> ys,List<Double> zs){
        polt3MakeFile(xs,ys,zs);
    }

    private static void polt3MakeFile(List<Double> xs,List<Double> ys,List<Double> zs){
        prepareHead();
        try {
            pyPrintWriter.println("x = "+printFomatPY(xs));
            pyPrintWriter.println("y = "+printFomatPY(ys));
            pyPrintWriter.println("z = "+printFomatPY(zs));
            pyPrintWriter.println("ax.plot(x, y, z, zdir='z')");
            pyPrintWriter.println();

        }catch (Exception e){
            e.printStackTrace();
            System.exit(-1);
        }
    }

    private static void prepareHead(){
        if(!prepareHead){
            try{
                pyFile = new File("./pyScript/plot.py");
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

            }catch (Exception e){
                pyPrintWriter.close();
                e.printStackTrace();
                System.exit(-1);
            }
            prepareHead=true;
        }
    }

    public static void show(){
        if(!prepareHead)
            System.exit(-1);

        try{
            pyPrintWriter.println("ax.grid(False)");
            pyPrintWriter.println("plt.show()");
            pyPrintWriter.println();

            pyPrintWriter.close();

            File dir = pyFile.getParentFile();
            Runtime run = Runtime.getRuntime();
            Process exec = run.exec("python .\\plot.py", null, dir);
            InputStream inputStream = exec.getInputStream();
            exec.waitFor();

            if(inputStream.available()>0){
                System.out.println(new String(inputStream.readAllBytes()));
            }

        }catch (Exception e){
            e.printStackTrace();
            System.exit(-1);
        }
    }

    private static String printFomatPY(List<Double> ls){
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        sb.append(ls.get(0));
        for (int i = 1; i < ls.size(); i++) {
            sb.append(", "+ls.get(i));
        }
        sb.append("]");

        return sb.toString();
    }

    public static void main(String[] args){

    }

    private static void demo1(){
        List<Double> xs = List.of(1.0,2.0);
        List<Double> ys = List.of(1.0,2.0);
        List<Double> zs = List.of(1.0,2.0);
        polt3MakeFile(xs,ys,zs);
        xs = List.of(-1.0,12.0);
        ys = List.of(-2.0,22.0);
        zs = List.of(-3.0,32.0);
        polt3MakeFile(xs,ys,zs);
        show();
    }

    private static void prinFomatPYtest(){
        List<Double> list = new ArrayList<>();
        list.add(1.0);
        list.add(1.0);
        System.out.println(printFomatPY(list));
    }

    private static void demo()throws  Exception{
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
