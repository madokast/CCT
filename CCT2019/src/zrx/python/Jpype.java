package zrx.python;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.PrintWriter;

@SuppressWarnings("all")
public class Jpype {
    private static File JVMDll = null;
    private static File pyFile = new File("./pyScript/contour.py");
    private static PrintWriter pyWriter = null;

    static {
//        final String java_home = System.getenv("JAVA_HOME");
//        JVMDll = new File(java_home, "bin\\server\\jvm.dll");
//        JVMDll = new File("C:\\Program Files (x86)\\Java\\jdk1.8.0_221\\jre\\bin\\client\\jvm.dll");
        JVMDll = new File("C:\\Program Files (x86)\\Java\\jdk1.8.0_221\\jre\\bin\\server\\jvm.dll");
        try {
            pyFile.createNewFile();
            pyWriter = new PrintWriter(new FileOutputStream(pyFile));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void run() {
        try {
            pyWriter.println();

            pyWriter.close();

            File dir = pyFile.getParentFile();
            Runtime run = Runtime.getRuntime();
            Process exec = run.exec("python " + pyFile.getAbsolutePath(), null, dir);
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

    public static void test() {
        pyWriter.println("from jpype import *");
        pyWriter.println("import os.path  ");
        pyWriter.println("startJVM(\"" + JVMDll.getAbsolutePath() + "\", \"-ea\",convertStrings=False) ");
        pyWriter.println("java.lang.System.out.println(\"hello jpype\") ");
        pyWriter.println("shutdownJVM()");
    }


    public static void main(String[] args) {
        test();
        run();
    }
}
