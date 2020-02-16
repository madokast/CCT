package cn.edu.hust.zrx.cct.base.cct;

import cn.edu.hust.zrx.cct.Logger;
import cn.edu.hust.zrx.cct.base.BaseUtils;
import cn.edu.hust.zrx.cct.base.line.Line2;
import cn.edu.hust.zrx.cct.base.line.Trajectory;
import cn.edu.hust.zrx.cct.base.point.Point2;
import cn.edu.hust.zrx.cct.base.point.Point2To3;
import cn.edu.hust.zrx.cct.base.point.Point3;
import cn.edu.hust.zrx.cct.base.python.Plot3d;
import cn.edu.hust.zrx.cct.base.vector.Vector3;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Description
 * 正真可用的CCT工厂
 * <p>
 * Data
 * 17:20
 *
 * @author zrx
 * @version 1.0
 */

public class CctFactory {

    /**
     * 构造 二极CCT
     * cct 中 soleLayerCctList
     * 0 -- 内层CCT
     * 1 -- 外层CCT
     *
     * @param smallRInner      内层线圈半径
     * @param smallROuter      外层线圈半径
     * @param bigR             轴半径 / 粒子轨道半径
     * @param angle            角度
     * @param windingNumber    匝数
     * @param a0BipolarInner   内层二极场系数
     * @param a1QuadrupleInner 内层四极场系数
     * @param a2SextupleInner  内层六极场系数
     * @param IInner           内层电流
     * @param a0BipolarOuter   外层二极场系数
     * @param a1QuadrupleOuter 外层四极场系数
     * @param a2SextupleOuter  外层六极场系数
     * @param IOuter           外层电流
     * @param numberPerWinding 离散时，每匝离散份数
     * @return 二极CCT
     */
    public static Cct createDipoleCct(double smallRInner,
                                      double smallROuter,
                                      double bigR,
                                      double angle,
                                      int windingNumber,
                                      double a0BipolarInner,
                                      double a1QuadrupleInner,
                                      double a2SextupleInner,
                                      double IInner,
                                      double a0BipolarOuter,
                                      double a1QuadrupleOuter,
                                      double a2SextupleOuter,
                                      double IOuter,
                                      int numberPerWinding) {
        CctLine2s cctLine2sInner =
                CctLine2Factory.create(smallRInner, bigR, angle,
                        windingNumber, a0BipolarInner, a1QuadrupleInner, a2SextupleInner);

        CctLine2s cctLine2sOuter =
                CctLine2Factory.create(smallROuter, bigR, angle,
                        windingNumber, a0BipolarOuter, a1QuadrupleOuter, a2SextupleOuter);

        SoleLayerCct soleLayerCctInner = new SoleLayerCct(
                cctLine2sInner.disperseToPoint3(numberPerWinding), IInner);
        SoleLayerCct soleLayerCctOuter = new SoleLayerCct(
                cctLine2sOuter.disperseToPoint3(numberPerWinding), IOuter);

        return Cct.getEmptyCct().
                addSoleLayerCct(soleLayerCctInner).
                addSoleLayerCct(soleLayerCctOuter);
    }

    /**
     * 构造 AG-CCT
     *
     * @param smallRInner       内层线圈半径
     * @param smallROuter       外层线圈半径
     * @param bigR              轴半径 / 粒子轨道半径
     * @param angles            每段AG-CCT 角度
     * @param windingNumbers    每段AG-CCT 匝数s
     * @param a0BipolarInners   每段AG-CCT 内层二极场系数
     * @param a1QuadrupleInners 每段AG-CCT 内层四极场系数
     * @param a2SextupleInners  每段AG-CCT 内层六极场系数
     * @param IInner            内层电流
     * @param a0BipolarOuters   每段AG-CCT 外层二极场系数
     * @param a1QuadrupleOuters 每段AG-CCT 外层四极场系数
     * @param a2SextupleOuters  每段AG-CCT 外层六极场系数
     * @param IOuter            外层电流
     * @param numberPerWinding  离散时，每匝离散份数
     * @return AG-CCT
     */
    public static Cct createAgCct(double smallRInner,
                                  double smallROuter,
                                  double bigR,
                                  double[] angles,
                                  int[] windingNumbers,
                                  double[] a0BipolarInners,
                                  double[] a1QuadrupleInners,
                                  double[] a2SextupleInners,
                                  double IInner,
                                  double[] a0BipolarOuters,
                                  double[] a1QuadrupleOuters,
                                  double[] a2SextupleOuters,
                                  double IOuter,
                                  int numberPerWinding) {
        BaseUtils.Equal.requireTrue(angles.length == windingNumbers.length, "AG-CCT构建参数有误");
        BaseUtils.Equal.requireTrue(windingNumbers.length == a0BipolarInners.length, "AG-CCT构建参数有误");
        BaseUtils.Equal.requireTrue(a0BipolarInners.length == a1QuadrupleInners.length, "AG-CCT构建参数有误");
        BaseUtils.Equal.requireTrue(a1QuadrupleInners.length == a2SextupleInners.length, "AG-CCT构建参数有误");
        BaseUtils.Equal.requireTrue(a2SextupleInners.length == a0BipolarOuters.length, "AG-CCT构建参数有误");
        BaseUtils.Equal.requireTrue(a0BipolarOuters.length == a1QuadrupleOuters.length, "AG-CCT构建参数有误");
        BaseUtils.Equal.requireTrue(a2SextupleOuters.length == a0BipolarInners.length, "AG-CCT构建参数有误");

        Cct cct = Cct.getEmptyCct();

        CctLine2s agcctInner = CctLine2Factory.createAGCCT(smallRInner, bigR, angles, windingNumbers,
                a0BipolarInners, a1QuadrupleInners, a2SextupleInners);

        CctLine2s agcctOuter = CctLine2Factory.createAGCCT(smallROuter, bigR, angles, windingNumbers,
                a0BipolarOuters, a1QuadrupleOuters, a2SextupleOuters);

        for (CctLine2 cctLine2 : agcctOuter.cctLine2s) {
            cct.addSoleLayerCct(new SoleLayerCct(cctLine2.disperseToPoint3(numberPerWinding), IOuter));
        }

        for (CctLine2 cctLine2 : agcctInner.cctLine2s) {
            cct.addSoleLayerCct(new SoleLayerCct(cctLine2.disperseToPoint3(numberPerWinding), IInner));
        }

        return cct;
    }

    public static Cct createAgCct(double smallRInner,
                                  double smallROuter,
                                  double bigR,
                                  double[] angles,
                                  int[] windingNumbers,
                                  double a0BipolarInners,
                                  double a1QuadrupleInners,
                                  double a2SextupleInners,
                                  double IInner,
                                  double a0BipolarOuters,
                                  double a1QuadrupleOuters,
                                  double a2SextupleOuters,
                                  double IOuter,
                                  int numberPerWinding) {
        BaseUtils.Equal.requireTrue(angles.length == windingNumbers.length, "AG-CCT构建参数有误");
        int length = angles.length;
        return createAgCct(smallRInner, smallROuter, bigR, angles, windingNumbers,
                BaseUtils.ArrayUtils.repeat(a0BipolarInners, length),
                BaseUtils.ArrayUtils.repeat(a1QuadrupleInners, length),
                BaseUtils.ArrayUtils.repeat(a2SextupleInners, length),
                IInner,
                BaseUtils.ArrayUtils.repeat(a0BipolarOuters, length),
                BaseUtils.ArrayUtils.repeat(a1QuadrupleOuters, length),
                BaseUtils.ArrayUtils.repeat(a2SextupleOuters, length),
                IOuter,
                numberPerWinding);
    }

    /**
     * 单层CCT
     */
    public static class SoleLayerCct implements MagnetAble {
        private final List<Point3> windings;
        private final double I;

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
    }

    /**
     * 各种CCT
     */
    public static class Cct implements MagnetAble {
        final List<SoleLayerCct> soleLayerCctList;

        /**
         * 空构造器
         * 使用 getEmptyCct
         */
        private Cct() {
            this.soleLayerCctList = new ArrayList<>();
        }

        /**
         * 增加单层CCT
         *
         * @param soleLayerCct 单层CCT
         * @return CCT
         */
        private Cct addSoleLayerCct(final SoleLayerCct soleLayerCct) {
            this.soleLayerCctList.add(soleLayerCct);
            return this;
        }

        /**
         * 增加CCT
         *
         * @param cct CCT
         * @return CCT
         */
        private Cct addCct(Cct cct) {
            this.soleLayerCctList.addAll(cct.soleLayerCctList);
            return this;
        }

        /**
         * 空工厂方法，获得空CCT
         *
         * @return 空工厂方法，获得空CCT
         */
        private static Cct getEmptyCct() {
            return new Cct();
        }

        /**
         * 取出某层，用于调试
         *
         * @param index 层数
         * @return 该层CCT
         */
        public SoleLayerCct get(int index) {
            return this.soleLayerCctList.get(index);
        }

        /**
         * 绘图
         *
         * @param describes 描述
         */
        public void plot3(String... describes) {
            int length = describes.length;
            int size = soleLayerCctList.size();
            if (!Objects.equals(length, size)) {
                Logger.getLogger().error("CCT绘制，describes数目不对。");
                Logger.getLogger().error("describes.length = " + length);
                Logger.getLogger().error("soleLayerCctList.size() = " + size);
            }

            int plotTimes = Math.min(length, size);

            BaseUtils.StreamTools.forZeroToN(plotTimes)
                    .forEach(i -> this.soleLayerCctList.get(i).plot3(describes[i]));
        }

        /**
         * 磁场
         *
         * @param p 点
         * @return p点磁场
         */
        @Override
        public Vector3 magnetAt(Point3 p) {
            final Vector3 m = Vector3.getZero();
            this.soleLayerCctList.forEach(
                    soleLayerCct ->
                            m.addSelf(soleLayerCct.magnetAt(p))
            );

            return m;
        }

    }

    /**
     * 可以求磁场，接口： Vector3 magnetAt(Point3 p);
     */
    public static interface MagnetAble {
        Vector3 magnetAt(Point3 p);

        default List<Point2> magnetBzAlongTrajectory(
                final Line2 trajectory, final Point2To3 point2To3, final double deltaLength) {
            return trajectory.dispersePoint3sWithDistance(deltaLength, point2To3)
                    .stream()
                    .map(point3WithDistance -> Point2.create(
                            point3WithDistance.getDistance(),
                            this.magnetAt(point3WithDistance.getPoint3()).z
                    )).collect(Collectors.toList());
        }

        default List<Point2> magnetBzAlongTrajectory(
                final Line2 trajectory, final double deltaLength) {
            return magnetBzAlongTrajectory(trajectory, Point2To3.getXY0ToXYZPoint2To3(), deltaLength);
        }

        default List<Point3WithDistance> magnetAlongTrajectory(
                final Line2 trajectory, final Point2To3 point2To3, final double deltaLength) {
            return trajectory.dispersePoint3sWithDistance(deltaLength, point2To3)
                    .stream()
                    .map(point3WithDistance -> new CctFactory.MagnetAble.Point3WithDistance(
                            point3WithDistance.getDistance(),
                            this.magnetAt(point3WithDistance.getPoint3()).toPoint3()
                    )).collect(Collectors.toList());
        }

        default List<Point3WithDistance> magnetAlongTrajectory(
                final Line2 trajectory, final double deltaLength) {
            return trajectory.dispersePoint3sWithDistance(deltaLength)
                    .stream()
                    .map(point3WithDistance -> new CctFactory.MagnetAble.Point3WithDistance(
                            point3WithDistance.getDistance(),
                            this.magnetAt(point3WithDistance.getPoint3()).toPoint3()
                    )).collect(Collectors.toList());
        }

        default List<Point2> magnetGradientAlongTrajectoryFast(
                final Line2 trajectory, final double deltaLength, final double goodFieldAreaWidth) {
            final Line2 rightHandSideLine2 = trajectory.rightHandSideLine2(goodFieldAreaWidth);
            final Line2 leftHandSideLine2 = trajectory.rightHandSideLine2(-goodFieldAreaWidth);
            final double WIDTH = goodFieldAreaWidth * 2.;
            return trajectory.dispersePoint3sWithDistance(deltaLength)
                    .stream()
                    .map(point3WithDistance -> {
                        double distance = point3WithDistance.getDistance();
                        Point3 rightPoint = rightHandSideLine2.pointAt(distance).toPoint3();
                        Point3 leftPoint = leftHandSideLine2.pointAt(distance).toPoint3();
                        double rightBz = magnetAt(rightPoint).z;
                        double leftBz = magnetAt(leftPoint).z;
                        return Point2.create(distance, (leftBz - rightBz) / WIDTH);
                    }).collect(Collectors.toList());
        }

        default double magnetGradientAtTrajectoryOfDistanceFast(
                final Line2 trajectory, final double distance, final double goodFieldAreaWidth) {
            Point3 leftHandSidePoint = trajectory.leftHandSidePoint(distance, goodFieldAreaWidth).toPoint3();
            Point3 rightHandSidePoint = trajectory.rightHandSidePoint(distance, goodFieldAreaWidth).toPoint3();

            double leftBz = magnetAt(leftHandSidePoint).z;
            double rightBz = magnetAt(rightHandSidePoint).z;

            return (leftBz - rightBz) / (2 * goodFieldAreaWidth);
        }


        public static class Point3WithDistance {
            private double distance;
            private Point3 point3;

            public Point3WithDistance(double distance, Point3 point3) {
                this.point3 = point3;
                this.distance = distance;
            }

            public Point3 getPoint3() {
                return point3;
            }

            public double getDistance() {
                return distance;
            }

            public Point2 getDistanceWithX() {
                return Point2.create(distance, point3.x);
            }

            public Point2 getDistanceWithY() {
                return Point2.create(distance, point3.y);
            }

            public Point2 getDistanceWithZ() {
                return Point2.create(distance, point3.z);
            }
        }
    }
}
