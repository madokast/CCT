package zrx.Tools.python;

import zrx.Tools.Sleep;

import javax.swing.*;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Scanner;

public class ContourTest {
    private static OutputStream outputStream = null;
    private static InputStream errInputStream = null;
    private static InputStream inputStream = null;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()){
            final String[] split = scanner.nextLine().split(",");
            final double x = Double.valueOf(split[0]);
            final double y = Double.valueOf(split[1]);

            try{
                Runtime runtime = Runtime.getRuntime();
                Process process = runtime.exec(
                        "\"D:\\Program Files\\jdk-12.0.1\\bin\\java.exe\" " +
                                "\"-javaagent:D:\\Program Files\\JetBrains\\IntelliJ " +
                                "IDEA 2019.1.3\\lib\\idea_rt.jar=57309:D:\\Program Files\\" +
                                "JetBrains\\IntelliJ IDEA 2019.1.3\\bin\" -Dfile.encoding=UTF-8" +
                                " -classpath D:\\Documents\\GitHub\\CCT\\out\\production\\CCT;" +
                                "D:\\Documents\\GitHub\\CCT\\libs\\Jama-1.0.3.jar;D:\\Documents\\" +
                                "GitHub\\CCT\\libs\\commons-math3-3.6.1-tools.jar;D:\\Documents\\" +
                                "GitHub\\CCT\\libs\\commons-math3-3.6.1.jar zrx.Tools.python.Contour\n",
                        null,null);
                outputStream  = process.getOutputStream();
                inputStream  = process.getInputStream();

                outputStream.write((x+","+y).getBytes());
                outputStream.flush();
                outputStream.close();
                while(true){
                    if(inputStream.available()>0){
                        System.out.print(new String(
                                inputStream.readAllBytes()
                        ));
                        break;
                    }else {
                        Sleep.sleep(10);
                    }

                }
            }catch ( Exception e){e.printStackTrace();}
        }
    }

    private static void 作废(){
        new Thread(()->{
        try {
            Runtime runtime = Runtime.getRuntime();
            Process process = runtime.exec(
                    "\"D:\\Program Files\\jdk-12.0.1\\bin\\java.exe\" " +
                            "\"-javaagent:D:\\Program Files\\JetBrains\\IntelliJ " +
                            "IDEA 2019.1.3\\lib\\idea_rt.jar=57309:D:\\Program Files\\" +
                            "JetBrains\\IntelliJ IDEA 2019.1.3\\bin\" -Dfile.encoding=UTF-8" +
                            " -classpath D:\\Documents\\GitHub\\CCT\\out\\production\\CCT;" +
                            "D:\\Documents\\GitHub\\CCT\\libs\\Jama-1.0.3.jar;D:\\Documents\\" +
                            "GitHub\\CCT\\libs\\commons-math3-3.6.1-tools.jar;D:\\Documents\\" +
                            "GitHub\\CCT\\libs\\commons-math3-3.6.1.jar zrx.Tools.python.Contour\n",
                    null,null);
            outputStream  = process.getOutputStream();
            inputStream  = process.getInputStream();
            errInputStream = process.getErrorStream();
        }catch (Exception e){e.printStackTrace();}
    }).start();

        System.out.println("started");

        try {
            Thread.currentThread().sleep(1000);
        } catch (Exception e){e.printStackTrace();}

        System.out.println("sleet");

        try {
            outputStream.write("0,0\n".getBytes());
            outputStream.flush();
            outputStream.close();

            System.out.println("write");

            while(true){
                if(inputStream.available()>0){
                    final byte[] bytes = inputStream.readAllBytes();
                    System.out.println(new String(bytes));
                    break;
                }else{
                    if(errInputStream.available()>0) {
                        final byte[] bytes = errInputStream.readAllBytes();
                        System.out.println(new String(bytes));
                    }


                    try {
                        Thread.currentThread().sleep(100);
                    } catch (Exception e){e.printStackTrace();}
                }
            }

        }catch (Exception e){e.printStackTrace();}
    }

}
