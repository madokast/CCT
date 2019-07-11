package zrx.Tools;


import zrx.base.Vector2d;
import zrx.base.Vector3d;
import zrx.python.Plot2d;

import java.io.*;
import java.lang.Object;
import java.lang.invoke.VarHandle;
import java.util.ArrayList;
import java.util.List;

public class Persistence {
    /**
     * 写对象
     * @param o 对象
     * @param fileName 文件名 无后缀
     */
    public static void write(Object o,String fileName){
        File file = new File("./"+fileName+".dat");
        try (
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
                ){
            objectOutputStream.writeObject(o);
            objectOutputStream.flush();
            System.out.println("持久化完成，"+o.getClass()+"的实例写入到了："+file.getCanonicalFile());
        }catch (Exception e){e.printStackTrace(); System.exit(-1);}
    }

    /**
     * 读对象
     * @param fileName 文件名 无后缀
     * @return 读取到的对象
     */
    public static Object read(String fileName){
        Object o = null;

        File file = new File("./"+fileName+".dat");
        try(
                FileInputStream fileInputStream = new FileInputStream(file);
                ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)
        ){
            o = objectInputStream.readObject();
        }catch (Exception e){
            e.printStackTrace();
            System.exit(-1);
        }

        if(o!=null)
            System.out.println("读取成功");

        return o;
    }

    //测试
    public static void main(String[] args) {
        final Object ob = read("pole4");
        List<Vector2d> oblist = (List<Vector2d>)ob;
        System.out.println("oblist.size() = " + oblist.size());
        Plot2d.plot2(oblist.toArray(Vector2d[]::new),Plot2d.RED_POINT);
        Plot2d.showThread();
    }
}
