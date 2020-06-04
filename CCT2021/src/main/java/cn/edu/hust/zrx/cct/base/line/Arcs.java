package cn.edu.hust.zrx.cct.base.line;

import cn.edu.hust.zrx.cct.Logger;
import cn.edu.hust.zrx.cct.base.BaseUtils;
import cn.edu.hust.zrx.cct.base.point.Point2;
import cn.edu.hust.zrx.cct.base.point.Point3;
import cn.edu.hust.zrx.cct.base.vector.Vector2;
import cn.edu.hust.zrx.cct.base.vector.Vector3;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Description
 * Arcs
 * 圆弧工具类
 * <p>
 * Data
 * 11:49
 *
 * @author zrx
 * @version 1.0
 */

public class Arcs {
    /**
     * 获取三个点 p1 p2 p3 所在圆的圆心
     * @param p1 点1
     * @param p2 点2
     * @param p3 点3
     * @return 圆心
     */
    public static Point2 center(Point2 p1,Point2 p2,Point2 p3){
        Vector2 v12 = Vector2.from(p1).to(p2);
        Vector2 v13 = Vector2.from(p1).to(p3);

        // 判断三点是否有点相同
        if(v12.isZero()||v13.isZero()){
            Logger.getLogger().error("Arcs::center 三点有相同？");
        }

        // 判断三点是否共线
        if (Vector2.parallel(v12,v13)) {
            Logger.getLogger().error("Arcs::center 三点共线？");
        }

        Vector2 r1 = v12.rotateSelf(BaseUtils.Converter.angleToRadian(90));
        Vector2 r2 = v13.rotateSelf(BaseUtils.Converter.angleToRadian(90));


        Point2 c1 = Point2.center(p1, p2);
        Point2 c2 = Point2.center(p1, p3);

        double k2 =
                ((c2.x-c1.x)*r1.y
                        -
                        (c2.y-c1.y)*r1.x)/
                        (r2.y*r1.x-r2.x*r1.y);

        return c2.toVector2().add(r2.dot(k2)).toPoint2();

    }


    /**
     * 二维圆 散点
     * @param center 圆心
     * @param r 半径
     * @param number 离散点数
     * @return 离散圆
     */
    public static List<Point2> circle(Point2 center,double r,int number){
        if(r<=0)
            throw new IllegalArgumentException("半径应>0");

        return BaseUtils.Python.linspaceStream(0, 360., number)
                .map(BaseUtils.Converter::angleToRadian)
                .mapToObj(phi -> {
                    Vector2 start = Vector2.xDirect(r);
                    start.rotateSelf(phi);
                    return start.add(center.toVector2());
                })
                .map(Vector2::toPoint2)
                .collect(Collectors.toList());

    }

    /**
     * 三维圆
     * 注意圆心为原点
     * @param r 半径
     * @param direct 方向，即垂直于圆面的矢量方向
     * @param num 离散点数
     * @return 三维圆
     */
    private static List<Point3> circle3d(double r,Vector3 direct,int num){
        // 归一化
        Vector3 direct0 = direct.copy().changeLengthSelf(1);

        // 首先找到一个和 direct 垂直的矢量 rr
        Vector3 rr;
        for (;;){
            // 任意一个矢量
            Vector3 random = Vector3.random();

            // 不能和 direct 平行
            if(Vector3.parallel(random,direct))
                continue;

            rr = direct.cross(random);
            break;
        }

        // rr 即要找的圆上的一个点了
        rr.changeLengthSelf(r);

        return BaseUtils.Python.linspaceStream(0, 360, num)
                .map(BaseUtils.Converter::angleToRadian)
                .mapToObj(phi -> rr.dot(Math.cos(phi)).addSelf(direct0.cross(rr).dot(Math.sin(phi))))
                .map(Vector3::toPoint3)
                .collect(Collectors.toList());
    }

    public static List<Point3> circle3d(Point3 center,double r,Vector3 direct,int num){
        List<Point3> list = circle3d(r, direct, num);

        return Point3.move(list,center.toVector3());
    }
}
