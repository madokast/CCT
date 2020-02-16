package cn.edu.hust.zrx.cct.base.line;

import cn.edu.hust.zrx.cct.base.BaseUtils;
import cn.edu.hust.zrx.cct.base.point.Point2;
import cn.edu.hust.zrx.cct.base.point.Point2To3;
import cn.edu.hust.zrx.cct.base.point.Point3;
import cn.edu.hust.zrx.cct.base.python.Plot2d;
import cn.edu.hust.zrx.cct.base.python.Plot3d;
import cn.edu.hust.zrx.cct.base.vector.Vector2;

import javax.sound.sampled.Line;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 有向、有长度的二维平面线(曲线)
 * 包括： 线段、圆弧、二维CCT的几何描述
 * -----------------------
 * 2020年2月10日 新增方法
 * 线长度
 * double getLength();
 * 返回S位置的点
 * Point2 pointAt(double s);
 * s位置处，曲线的方向
 * Vector2 directAt(double s);
 * 线段s处，左手(右手)侧，d处的点
 * default Point2 rightHandSidePoint(double s, double d)
 * default Point2 leftHandSidePoint(double s, double d)
 * 线段起点/终点的位置/方向
 * default Point2 pointAtStart()
 * default Point2 pointAtEnd()
 * default Vector2 directAtStart()
 * default Vector2 directAtEnd()
 * ---------------------------------------------------
 * 2020年2月11日 新增方法
 * default String describe()
 * ---------------------------------------------------
 *
 * @author zrx
 * @version 1.0
 * @since 2020年2月10日
 */

public interface Line2 {
    /**
     * 线长度
     *
     * @return 长度
     * @since 2020年2月10日
     */
    double getLength();

    /**
     * 返回S位置的点
     *
     * @param s 位置
     * @return s位置的点
     * @since 2020年2月10日
     */
    Point2 pointAt(double s);

    /**
     * s位置处，曲线的方向
     *
     * @param s 位置
     * @return s位置出曲线方向
     * @since 2020年2月10日
     */
    Vector2 directAt(double s);


    /*------------------------------default方法--------------------*/
    /*------------------------------左右手方法--------------------*/

    /**
     * 位于s处的点，它右手边d处的点
     * <p>
     * 1    5    10     15
     * -----------------@------>
     * |    2
     * *    4
     * <p>
     * 如上图，一条直线，s=15，d=4 ,即点 @ 右手边4 距离处的点 *
     *
     * @param s s位置
     * @param d d距离远处
     * @return 点
     * @since 2020年2月10日
     */
    default Point2 rightHandSidePoint(double s, double d) {
        Point2 pointAtS = pointAt(s);
        Vector2 directAtS = directAt(s);

        return pointAtS.copy().moveSelf(
                directAtS.copy().rotateSelf(-Math.PI / 2).changeLengthSelf(d)
        );

//        这里没有copy 改变了pointAtS 和 directAtS 出了问题
//        return pointAtS.moveSelf(
//                directAtS.rotateSelf(-Math.PI / 2).changeLengthSelf(d)
//        );
    }

    /**
     * 位于s处的点，它左手边d处的点
     * 说明见rightHandSidePoint 方法
     *
     * @param s s位置
     * @param d d距离远处
     * @return 点
     * @since 2020年2月10日
     */
    default Point2 leftHandSidePoint(double s, double d) {
        return rightHandSidePoint(s, -d);
    }

    /**
     * 线 this 右手处 rightHandOffset 的一条线
     *
     * @param rightHandOffset 右手处距离
     * @return 线 this 右手处 rightHandOffset 的一条线
     */
    default Line2 rightHandSideLine2(double rightHandOffset) {
        //辅助类
        class RightHandSideLine2 implements Line2 {
            private final Line2 origin;
            private final double rightHandOffset;

            public RightHandSideLine2(Line2 origin, double rightHandOffset) {
                this.origin = origin;
                this.rightHandOffset = rightHandOffset;
            }

            @Override
            public double getLength() {
                return origin.getLength();
            }

            @Override
            public Point2 pointAt(double s) {
                return origin.rightHandSidePoint(s, rightHandOffset);
            }

            @Override
            public Vector2 directAt(double s) {
                return origin.directAt(s);
            }
        }
        //返回一条新的线
        return new RightHandSideLine2(this, rightHandOffset);
    }

    /*------------------------------端点性质--------------------*/

    /**
     * 起点
     *
     * @return 起点
     * @since 2020年2月10日
     */
    default Point2 pointAtStart() {
        return pointAt(0);
    }

    /**
     * 终点
     *
     * @return 终点
     * @since 2020年2月10日
     */
    default Point2 pointAtEnd() {
        return pointAt(getLength());
    }

    /**
     * 起点处方向
     *
     * @return 起点处方向
     * @since 2020年2月10日
     */
    default Vector2 directAtStart() {
        return directAt(0);
    }

    /**
     * 终点处方向
     *
     * @return 终点处方向
     * @since 2020年2月10日
     */
    default Vector2 directAtEnd() {
        return directAt(getLength());
    }

    /*------------------------------二维/三维离散--------------------*/

    /**
     * 二维离散轨迹 带有长度
     *
     * @param deltaLength 离散每段长度 / 每个离散点的间距
     * @return (s, point2)
     */
    default List<Point2WithDistance> dispersePoint2sWithDistance(double deltaLength) {
        int number = (int) (getLength() / deltaLength);
        return BaseUtils.Python.linspaceStream(0, getLength(), number)
                .mapToObj(s -> new Point2WithDistance(pointAt(s), s))
                .collect(Collectors.toList());
    }

    /**
     * 三维离散轨迹 带有长度
     *
     * @param deltaLength 离散每段长度 / 每个离散点的间距
     * @param point2To3   轨迹转为三维的方法
     * @return (s, point3)
     */
    default List<Point3WithDistance> dispersePoint3sWithDistance(final double deltaLength, final Point2To3 point2To3) {
        return dispersePoint2sWithDistance(deltaLength)
                .stream()
                .map(point2WithDistance -> point2WithDistance.toPoint3WithDistance(point2To3))
                .collect(Collectors.toList());
    }

    /**
     * 三维离散轨迹 带有长度
     * 默认轨迹(x, y) 直接转为三维(x, y, 0)
     *
     * @param deltaLength 离散每段长度 / 每个离散点的间距
     * @return (s, point3)
     */
    default List<Point3WithDistance> dispersePoint3sWithDistance(final double deltaLength) {
        return dispersePoint3sWithDistance(deltaLength, Point2To3.getXY0ToXYZPoint2To3());
    }

    /**
     * 二维离散轨迹 不带长度
     *
     * @param deltaLength 离散每段长度 / 每个离散点的间距
     * @return List<Point2>
     */
    default List<Point2> dispersePoint2s(final double deltaLength) {
        int number = (int) (getLength() / deltaLength);
        return BaseUtils.Python.linspaceStream(0, getLength(), number)
                .mapToObj(this::pointAt)
                .collect(Collectors.toList());
    }

    /**
     * 三维离散轨迹 不带有长度
     *
     * @param deltaLength 离散每段长度 / 每个离散点的间距
     * @param point2To3   轨迹转为三维的方法
     * @return List<Point3>
     */
    default List<Point3> dispersePoint3s(final double deltaLength, final Point2To3 point2To3) {
        return dispersePoint2s(deltaLength)
                .stream()
                .map(point2To3::convert)
                .collect(Collectors.toList());
    }

    /**
     * 三维离散轨迹 不带有长度
     * 按照 (x, y) 直接转为三维(x, y, 0)
     *
     * @param deltaLength 离散每段长度 / 每个离散点的间距
     * @return List<Point3>
     */
    default List<Point3> dispersePoint3s(final double deltaLength) {
        return dispersePoint3s(deltaLength, Point2To3.getXY0ToXYZPoint2To3());
    }

    /**
     * 离散轨迹辅助类
     */
    public static class Point2WithDistance {
        final Point2 point2;
        final double distance;

        public Point2WithDistance(Point2 point2, double distance) {
            this.point2 = point2;
            this.distance = distance;
        }

        public Point3WithDistance toPoint3WithDistance(Point2To3 point2To3) {
            return new Point3WithDistance(
                    point2To3.convert(point2), distance
            );
        }

        public Point2 getPoint2() {
            return point2;
        }

        public double getDistance() {
            return distance;
        }
    }

    /**
     * 离散轨迹辅助类
     */
    public static class Point3WithDistance {
        final Point3 point3;
        final double distance;

        public Point3WithDistance(Point3 point3, double distance) {
            this.point3 = point3;
            this.distance = distance;
        }

        public Point3 getPoint3() {
            return point3;
        }

        public double getDistance() {
            return distance;
        }
    }

    /*------------------------------python画图--------------------*/

    /**
     * plot2d 画图
     *
     * @param deltaLength 离散每段长度 / 每个离散点的间距 / 确定画图精度
     * @param describe    画图描述
     */
    default void plot2d(final double deltaLength, String describe) {
        Plot2d.plot2(dispersePoint2s(deltaLength), describe);
    }

    /**
     * plot3d 画图
     * 按照 (x, y) 直接转为三维(x, y, 0)
     *
     * @param deltaLength 离散每段长度 / 每个离散点的间距 / 确定画图精度
     * @param point2To3   轨迹转为三维的方法
     * @param describe    画图描述
     */
    default void plot3d(final double deltaLength, final Point2To3 point2To3, String describe) {
        Plot3d.plot3(dispersePoint3s(deltaLength, point2To3), describe);
    }

    /**
     * plot3d 画图
     *
     * @param deltaLength 离散每段长度 / 每个离散点的间距 / 确定画图精度
     * @param describe    画图描述
     */
    default void plot3d(final double deltaLength, String describe) {
        Plot3d.plot3(dispersePoint3s(deltaLength), describe);
    }

    default void plot3d(final double deltaLength){
        plot3d(deltaLength,Plot2d.BLACK_LINE);
    }

    default void plot3d(){
        plot3d(1*MM);
    }


    default String describe() {
        return "[起点" + pointAt(0) + ", 起点方向" + directAt(0) + ", 长度" + getLength() + "]";
    }

    public static final double MM = 1e-3;
}
