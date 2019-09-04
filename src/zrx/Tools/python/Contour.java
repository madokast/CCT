package zrx.Tools.python;

import zrx.CCT.ConcreteCCT.AllCCTs;
import zrx.DemoAndStudy.小研究.六十七点五新测试0804_2;
import zrx.Tools.Numpy;
import zrx.base.point.Vector3d;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;

/*
"D:\Program Files\jdk-12.0.1\bin\java.exe" "-javaagent:D:\Program Files\JetBrains\IntelliJ IDEA 2019.1.3\lib\idea_rt.jar=57309:D:\Program Files\JetBrains\IntelliJ IDEA 2019.1.3\bin" -Dfile.encoding=UTF-8 -classpath D:\Documents\GitHub\CCT\out\production\CCT;D:\Documents\GitHub\CCT\libs\Jama-1.0.3.jar;D:\Documents\GitHub\CCT\libs\commons-math3-3.6.1-tools.jar;D:\Documents\GitHub\CCT\libs\commons-math3-3.6.1.jar zrx.Tools.python.Contour
* */

public class Contour {
    public static void main(String[] args)throws Exception{
        final AllCCTs allCCTsFor675 = 六十七点五新测试0804_2.getAllCCTsFor675();
        final Vector3d midpoint3d = 六十七点五新测试0804_2.getMidpoint3d();
        PrintWriter printWriter = new PrintWriter(new FileOutputStream(new File("pyScript/contour.txt")));

        final double[] linspace = Numpy.linspace(-50, 50, 20);
        for (int i = 0; i < linspace.length; i++) {
            for (int j = 0; j < linspace.length; j++) {
                double x = linspace[i]/1000.0;
                double y = linspace[j]/1000.0;

                final Vector3d point = Vector3d.move(Vector3d.move(
                        Vector3d.copyOne(midpoint3d),
                        Vector3d.copyOne(midpoint3d), x),Vector3d.getZDirect(),y);
                printWriter.println(x*1000+","+y*1000+","+allCCTsFor675.magnet(point).length());
            }
            System.out.print(".");
        }
        System.out.println();

        printWriter.flush();
        printWriter.close();
        System.out.println("OK");

//        Runtime.getRuntime().exec("python D:\\Documents\\GitHub\\CCT\\pyScript\\contour.py").waitFor();

//        System.out.println("OK OK");

    }
}
