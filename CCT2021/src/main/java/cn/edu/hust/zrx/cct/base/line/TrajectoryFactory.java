package cn.edu.hust.zrx.cct.base.line;

import cn.edu.hust.zrx.cct.base.point.Point2;
import cn.edu.hust.zrx.cct.base.vector.Vector2;

/**
 * Description
 * 理想轨迹工厂类
 * 使用方法
 * //轨迹的建立方法，类似于我写的matLab 机架绘图
 * //使用 TrajectoryFactory 工厂类
 * // 第一步： 确定起点 setStartingPoint 传入 Point2
 * // 第二步： 总是设定线段(轨迹的第一段总是线段) setStraightLine 传入 length 和 direct (Vector2)
 * // 之后得到的就是一个 Trajectory
 * // Trajectory 可以往后不断地增加 setStraightLine / addArcLine
 * <p>
 * // 关于 setStraightLine 后接线段，只需要给定长度
 * // 关于 addArcLine 后接圆弧，给定 半径、顺时针与否、角度
 * <p>
 * <p>
 * Trajectory trajectory =
 * TrajectoryFactory.setStartingPoint(Point2.origin())
 * .setStraightLine(1, Vector2.xDirect())
 * .addArcLine(0.75, false, 22.5)
 * .addStraitLine(1)
 * .addArcLine(0.75, false, 22.5)
 * .addStraitLine(3)
 * .addArcLine(0.75, true, 67.5)
 * .addStraitLine(0.5)
 * .addArcLine(0.75, true, 67.5)
 * .addStraitLine(3);
 * ---------------------------------------------
 * 2020年2月10日 新增方法
 * 工厂方法，具体使用见类注释
 * public static TrajectoryFactoryWithStartingPoint setStartingPoint(Point2 startingPoint)
 * 轨迹从原定开始
 * public static TrajectoryFactoryWithStartingPoint fromOrigin()
 * 内部辅助类
 * public static class TrajectoryFactoryWithStartingPoint
 * <p>
 * Data
 * 22:39
 *
 * @author zrx
 * @version 1.0
 */

public class TrajectoryFactory {
    /**
     * 工厂方法，具体使用见类注释
     *
     * @param startingPoint 起点
     * @return TrajectoryFactoryWithStartingPoint
     * @since 2020年2月10日
     */
    public static TrajectoryFactoryWithStartingPoint setStartingPoint(Point2 startingPoint) {
        return new TrajectoryFactoryWithStartingPoint(startingPoint);
    }

    public static TrajectoryFactoryWithStartingPoint setStartingPoint(double x,double y) {
        return new TrajectoryFactoryWithStartingPoint(Point2.create(x,y));
    }

    /**
     * 轨迹从原定开始
     *
     * @return TrajectoryFactoryWithStartingPoint
     * @since 2020年2月10日
     */
    public static TrajectoryFactoryWithStartingPoint fromOrigin() {
        return setStartingPoint(Point2.origin());
    }

    /**
     * 内部辅助类
     *
     * @since 2020年2月10日
     */
    public static class TrajectoryFactoryWithStartingPoint {
        Point2 startingPoint;

        TrajectoryFactoryWithStartingPoint(Point2 startingPoint) {
            this.startingPoint = startingPoint;
        }

        public Trajectory setStraightLine(double length, Vector2 direct) {
            StraightLine straightLine = new StraightLine(length, direct, startingPoint);

            return new Trajectory(straightLine);
        }
    }
}
