package cn.edu.hust.zrx.cct.base.line;

import cn.edu.hust.zrx.cct.advanced.SR;
import cn.edu.hust.zrx.cct.base.BaseUtils;
import cn.edu.hust.zrx.cct.base.point.Point2;
import cn.edu.hust.zrx.cct.base.point.Point3;
import cn.edu.hust.zrx.cct.base.python.Plot2d;
import cn.edu.hust.zrx.cct.base.python.Plot3d;
import cn.edu.hust.zrx.cct.base.vector.Vector2;
import cn.edu.hust.zrx.cct.base.vector.Vector3;

import java.util.List;
import java.util.stream.Collectors;


/**
 * 三维有向、定长线
 */

public interface Line3 {

    /**
     * @return 线长度
     */
    double getLength();

    /**
     * s 位置处的点
     *
     * @param s 位置
     * @return 所在位置的点
     */
    Point3 pointAt(double s);

    /**
     * s 位置的方向
     *
     * @param s 位置
     * @return s 位置处的方向
     */
    Vector3 directAt(double s);


    default List<Line2.Point3WithDistance> dispersePoint3sWithDistance(final double deltaLength) {
        int number = (int) (getLength() / deltaLength);
        return BaseUtils.Python.linspaceStream(0, getLength(), number)
                .mapToObj(s -> new Line2.Point3WithDistance(pointAt(s), s))
                .collect(Collectors.toList());
    }

    default List<Point3> disperse(final double step) {
        int number = (int) (getLength() / step);
        return BaseUtils.Python.linspaceStream(0, getLength(), number)
                .mapToObj(this::pointAt)
                .collect(Collectors.toList());
    }

    default Point3 rightHandSidePoint(double s, double d, Vector3 yDirect) {
        Point3 pointAtS = pointAt(s);
        Vector3 directAtS = directAt(s);


        return pointAtS.copy().moveSelf(
                directAtS.cross(yDirect).changeLengthSelf(d)
        );
    }

    default Line3 rightHandSideLine3(double rightHandOffset, Vector3 yDirect) {
        //辅助类
        class RightHandSideLine3 implements Line3 {
            private final Line3 origin;
            private final double rightHandOffset;
            private final Vector3 yDirect;

            public RightHandSideLine3(Line3 origin, double rightHandOffset, Vector3 yDirect) {
                this.origin = origin;
                this.rightHandOffset = rightHandOffset;
                this.yDirect = yDirect;
            }

            @Override
            public double getLength() {
                return origin.getLength();
            }

            @Override
            public Point3 pointAt(double s) {
                return origin.rightHandSidePoint(s, rightHandOffset, yDirect);
            }

            @Override
            public Vector3 directAt(double s) {
                return origin.directAt(s);
            }
        }
        //返回一条新的线
        return new RightHandSideLine3(this, rightHandOffset, yDirect);
    }

    default void plot3(final double step, String describe) {
        List<Point3> disperse = disperse(step);
        Plot3d.plot3(disperse, describe);
    }

    double MM = 1e-3;
    default void plot3(){
        plot3(MM, Plot2d.BLACK_LINE);
    }
}
