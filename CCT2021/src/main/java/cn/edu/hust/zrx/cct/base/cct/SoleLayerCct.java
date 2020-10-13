package cn.edu.hust.zrx.cct.base.cct;

import cn.edu.hust.zrx.cct.Logger;
import cn.edu.hust.zrx.cct.base.BaseUtils;
import cn.edu.hust.zrx.cct.base.point.Point2;
import cn.edu.hust.zrx.cct.base.point.Point3;
import cn.edu.hust.zrx.cct.base.python.Plot2d;
import cn.edu.hust.zrx.cct.base.python.Plot3d;
import cn.edu.hust.zrx.cct.base.python.PlotAble3d;
import cn.edu.hust.zrx.cct.base.vector.Vector3;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 单层CCT
 */
public class SoleLayerCct implements MagnetAble, PlotAble3d {
    public final List<Point3> windings;
    public final double I;

    /**
     * 构造器
     *
     * @param windings 线圈
     * @param i        电流
     */
    public SoleLayerCct(List<Point3> windings, double i) {
        this.windings = windings;
        I = i;
    }

    public static SoleLayerCct create(List<Point3> windings, double I) {
        return new SoleLayerCct(windings, I);
    }

    public SoleLayerCct deepCopy() {
        return new SoleLayerCct(windings.stream().map(Point3::copy).collect(Collectors.toList()), I);
    }

    @Override
    public void plot3d() {
        plot3(Plot2d.RED_LINE);
    }

    /**
     * 绘制CCT
     *
     * @param describe 描述
     */
    public void plot3(String describe) {
        Plot3d.plot3(windings, describe);
    }

    /**
     * CCT在p点的磁场
     *
     * @param p 点
     * @return 磁场
     */
    @Override
    public Vector3 magnetAt(Point3 p) {
        return BaseUtils.Magnet.magnetAtPoint(windings, I, p);
    }


    // getter for debug

    public List<Point3> getWindings() {
        return windings;
    }

    public double getI() {
        return I;
    }

    public Cct toCct() {
        Cct cct = new Cct();
        cct.addSoleLayerCct(this);
        return cct;
    }

    public void printToCad(String fileName) {
        File file = new File("./" + fileName + ".txt");
        String canonicalPath = null;

        try {
            canonicalPath = file.getCanonicalPath();
        } catch (Exception e) {
            Logger.getLogger().error("获取文件绝对路径失败");
            e.printStackTrace();
        }

        if (file.exists()) {
            Logger.getLogger().info("文件{}存在，删除之", canonicalPath);
            boolean delete = file.delete();
            if (!delete) {
                Logger.getLogger().error("文件{}删除失败!", canonicalPath);
                System.exit(-1);
            }
        }

        try {
            boolean newFile = file.createNewFile();
            if (!newFile) {
                Logger.getLogger().error("文件{}创建失败", canonicalPath);
                throw new Exception("文件创建失败");
            }
        } catch (Exception e) {
            Logger.getLogger().error("文件{}创建异常!", canonicalPath);
            System.exit(-1);
        }

        //文件创建成功

        try {
            FileWriter fileWriter = new FileWriter(file);
            for (Point3 winding : windings) {
                fileWriter.write(winding.toCadString(1. / 1000.) + "\r\n");
            }
            fileWriter.flush();
            fileWriter.close();
        } catch (Exception e) {
            Logger.getLogger().error("文件{}写入失败", canonicalPath);
            System.exit(-1);
        }

        Logger.getLogger().info("文件{}写入成功!", canonicalPath);
    }

    public SoleLayerCct rotateInXYPlane(Point2 point2, double phi) {
        //private final List<Point3> windings;
        //        private final double I;

        List<Point3> rotateInXYPlane = Point3.rotateInXYPlane(this.getWindings(), point2, phi);

        return new SoleLayerCct(rotateInXYPlane, I);
    }

    public SoleLayerCct move(Vector3 v) {
        List<Point3> move = Point3.move(this.getWindings(), v);

        return new SoleLayerCct(move, I);
    }
}
