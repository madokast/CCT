package cn.edu.hust.zrx.cct.base.point;

import cn.edu.hust.zrx.cct.base.vector.Vector3;

import java.util.List;

/**
 * Description
 * 三维点
 * <p>
 * Data
 * 21:18
 *
 * @author zrx
 * @version 1.0
 */

public class Point3 {
    public double x;
    public double y;
    public double z;



    private Point3(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public static Point3 create(double x, double y, double z){
        return new Point3(x,y,z);
    }

    public Point3 copy(){
        return create(this.x,this.y,this.z);
    }

    public Vector3 toVector3(){
        return Vector3.create(this.x,this.y,this.z);
    }

    public Point3 moveSelf(Vector3 v){
        this.x+=v.x;
        this.y+=v.y;
        this.z+=v.z;

        return this;
    }

    @Override
    public String toString() {
        return List.of(x,y,z).toString();
    }
}
