package cn.edu.hust.zrx.cct.base.magnet;

import cn.edu.hust.zrx.cct.Logger;
import cn.edu.hust.zrx.cct.advanced.PolynomialFitter;
import cn.edu.hust.zrx.cct.base.BaseUtils;
import cn.edu.hust.zrx.cct.base.line.Line2;
import cn.edu.hust.zrx.cct.base.line.Line3;
import cn.edu.hust.zrx.cct.base.point.Point2;
import cn.edu.hust.zrx.cct.base.point.Point2To3;
import cn.edu.hust.zrx.cct.base.point.Point3;
import cn.edu.hust.zrx.cct.base.vector.Vector3;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 可以求磁场，接口： Vector3 magnetAt(Point3 p);
 * 这是本程序中写得最好的一个类
 * 2020年10月28日 —— 感叹道
 */

@FunctionalInterface
public interface MagnetAble {

    double MM = 1e-3;

    Vector3 magnetAt(Point3 p);

    default double magnetBzAt(Point3 p) {
        return magnetAt(p).z;
    }

    default double magnetBzAlongTrajectoryAt(final Line2 trajectory, double s) {
        return magnetBzAt(trajectory.pointAt(s).toPoint3());
    }

    default List<Point2> magnetBzAlongTrajectory(
            final Line2 trajectory, final Point2To3 point2To3, final double deltaLength) {
        return trajectory.dispersePoint3sWithDistance(deltaLength, point2To3)
                .stream()
                .map(point3WithDistance -> Point2.create(
                        point3WithDistance.getDistance(),
                        this.magnetAt(point3WithDistance.getPoint3()).z
                )).collect(Collectors.toList());
    }

    default List<Point2> magnetBzAlongTrajectoryParallel(
            final Line2 trajectory, final Point2To3 point2To3, final double deltaLength) {
        return trajectory.dispersePoint3sWithDistance(deltaLength, point2To3)
                .stream()
                .parallel()
                .map(point3WithDistance -> Point2.create(
                        point3WithDistance.getDistance(),
                        this.magnetAt(point3WithDistance.getPoint3()).z
                ))
                .collect(Collectors.toList())
                .stream()
                .sequential()
                .sorted(Comparator.comparingDouble(Point2::getX))
                .collect(Collectors.toList());
    }

    default List<Point2> magnetBzAlongTrajectory(
            final Line2 trajectory, final double deltaLength) {
        return magnetBzAlongTrajectory(trajectory, Point2To3.getXY0ToXYZPoint2To3(), deltaLength);
    }

    default List<Point2> magnetBzAlongTrajectoryParallel(
            final Line2 trajectory, final double deltaLength) {
        return magnetBzAlongTrajectoryParallel(trajectory, Point2To3.getXY0ToXYZPoint2To3(), deltaLength);
    }

    default List<Point3WithDistance> magnetAlongTrajectory(
            final Line2 trajectory, final Point2To3 point2To3, final double deltaLength) {
        return trajectory.dispersePoint3sWithDistance(deltaLength, point2To3)
                .stream()
                .map(point3WithDistance -> new Point3WithDistance(
                        point3WithDistance.getDistance(),
                        this.magnetAt(point3WithDistance.getPoint3()).toPoint3()
                )).collect(Collectors.toList());
    }

    default List<Point2> magnetBzAlongTrajectory(final Line2 trajectory) {
        return magnetBzAlongTrajectory(trajectory, MM);
    }

    default List<Point2> magnetBzAlongTrajectoryParallel(final Line2 trajectory) {
        return magnetBzAlongTrajectoryParallel(trajectory, MM);
    }

    default List<Point3WithDistance> magnetAlongTrajectory(
            final Line2 trajectory, final double deltaLength) {
        return trajectory.dispersePoint3sWithDistance(deltaLength)
                .stream()
                .map(point3WithDistance -> new Point3WithDistance(
                        point3WithDistance.getDistance(),
                        this.magnetAt(point3WithDistance.getPoint3()).toPoint3()
                )).collect(Collectors.toList());
    }

    default List<Point3WithDistance> magnetAlongTrajectory(final Line2 trajectory) {
        return magnetAlongTrajectory(trajectory, 10 * MM);
    }

    /**
     * 积分场
     *
     * @param trajectory  积分所在轨迹
     * @param deltaLength delta d 长度
     * @return 积分值
     */
    default double integrationField(final Line2 trajectory, final double deltaLength) {
        List<Point2> list = magnetBzAlongTrajectory(trajectory, deltaLength);
        if (list.size() < 2) {
            throw new IllegalArgumentException("deltaLength太大，无法积分");
        }

        final double delta = list.get(1).x - list.get(0).x;

        double ret = 0;

        for (int i = 0; i < list.size() - 1; i++) {
            double y0 = list.get(i).y;
            double y1 = list.get(i + 1).y;

            ret += (y0 + y1) / 2;
        }

        return ret * delta;
    }

    /**
     * 研究轨道左手侧磁场。2020年2月21日
     * 详见2月中旬的研究——理想粒子为什么脱离了中平面
     * 验证通过
     *
     * @param trajectory  轨道
     * @param deltaLength 分段
     * @return 轨道左手侧磁场（s, m）
     */
    default List<Point2> leftHandMagnetAlongTrajectory(final Line2 trajectory, final double deltaLength) {
        return magnetAlongTrajectory(trajectory, deltaLength)
                .stream()
                .map(point3WithDistance -> {
                    Vector3 vector3 = point3WithDistance.getPoint3().toVector3();
                    double distance = point3WithDistance.getDistance();

                    Vector3 left = trajectory.directAt(distance)
                            .rotateSelf(BaseUtils.Converter.angleToRadian(90))
                            .toVector3()
                            .normalSelf();

                    return Point2.create(distance, vector3.dot(left));
                }).collect(Collectors.toList());
    }

    default List<Point2> leftHandMagnetAlongTrajectory(final Line2 trajectory) {
        return leftHandMagnetAlongTrajectory(trajectory, MM);
    }


    default double magnetGradientAlongTrajectoryAt(
            final Line2 trajectory, final double goodFieldAreaWidth, final double s) {
        final Line2 rightHandSideLine2 = trajectory.rightHandSideLine2(goodFieldAreaWidth);
        final Line2 leftHandSideLine2 = trajectory.rightHandSideLine2(-goodFieldAreaWidth);
        final double WIDTH = goodFieldAreaWidth * 2.;

        Point3 rightPoint = rightHandSideLine2.pointAt(s).toPoint3();
        Point3 leftPoint = leftHandSideLine2.pointAt(s).toPoint3();

        double rightBz = magnetAt(rightPoint).z;
        double leftBz = magnetAt(leftPoint).z;

        return (leftBz - rightBz) / WIDTH;
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

    default List<Point2> magnetGradientAlongTrajectoryFast(final Line2 trajectory) {
        return magnetGradientAlongTrajectoryFast(trajectory, 10 * MM, 10 * MM);
    }

    default List<Point2> magnetGradientAlongTrajectory(
            final Line2 trajectory, final double deltaLength, final double goodFieldAreaWidth) {
        int dotNumber = 16;

        PolynomialFitter fitter = PolynomialFitter.build(1);

        List<Double> xList = BaseUtils.Python.linspaceStream(-goodFieldAreaWidth, goodFieldAreaWidth, dotNumber)
                .boxed().collect(Collectors.toList());
        ;

        final Line2 rightHandSideLine2 = trajectory.rightHandSideLine2(goodFieldAreaWidth);
        final Line2 leftHandSideLine2 = trajectory.rightHandSideLine2(-goodFieldAreaWidth);

        return trajectory.dispersePoint3sWithDistance(deltaLength)
                .stream()
                .map(point3WithDistance -> {
                    double distance = point3WithDistance.getDistance();
                    Point3 rightPoint = rightHandSideLine2.pointAt(distance).toPoint3();
                    Point3 leftPoint = leftHandSideLine2.pointAt(distance).toPoint3();

                    List<Double> bzList = BaseUtils.Python.linspaceStream(rightPoint, leftPoint, dotNumber)
                            .mapToDouble(p -> magnetAt(p).z)
                            .boxed()
                            .collect(Collectors.toList());
                    List<Point2> fitted = Point2.create(xList, bzList);

                    return Point2.create(distance, fitter.fit(fitted)[1]);
                }).collect(Collectors.toList());
    }

    // 六极场
    default List<Point2> magnetSecondGradientAlongTrajectory(
            final Line2 trajectory, final double deltaLength, final double goodFieldAreaWidth) {
        int dotNumber = 16;

        PolynomialFitter fitter = PolynomialFitter.build(2);

        List<Double> xList = BaseUtils.Python.linspaceStream(-goodFieldAreaWidth, goodFieldAreaWidth, dotNumber)
                .boxed().collect(Collectors.toList());


        final Line2 rightHandSideLine2 = trajectory.rightHandSideLine2(goodFieldAreaWidth);
        final Line2 leftHandSideLine2 = trajectory.rightHandSideLine2(-goodFieldAreaWidth);

        return trajectory.dispersePoint3sWithDistance(deltaLength)
                .stream()
                .map(point3WithDistance -> {
                    double distance = point3WithDistance.getDistance();
                    Point3 rightPoint = rightHandSideLine2.pointAt(distance).toPoint3();
                    Point3 leftPoint = leftHandSideLine2.pointAt(distance).toPoint3();

                    List<Double> bzList = BaseUtils.Python.linspaceStream(rightPoint, leftPoint, dotNumber)
                            .mapToDouble(p -> magnetAt(p).z)
                            .boxed()
                            .collect(Collectors.toList());
                    List<Point2> fitted = Point2.create(xList, bzList);

                    return Point2.create(distance, fitter.fit(fitted)[2]);
                }).collect(Collectors.toList());
    }

    default List<Point2> magnetSecondGradientAlongTrajectory(final Line2 trajectory) {
        return magnetSecondGradientAlongTrajectory(trajectory, 10 * MM, 10 * MM);
    }

    // 多级场
    default List<BaseUtils.Content.BiContent<Double, double[]>> multiplePoleMagnetAlongTrajectory(
            final Line2 trajectory, final double deltaLength, final double goodFieldAreaWidth,
            final int order, final int dotNumber
    ) {
        PolynomialFitter fitter = PolynomialFitter.build(order);

        List<Double> xList = BaseUtils.Python.linspaceStream(-goodFieldAreaWidth, goodFieldAreaWidth, dotNumber)
                .boxed().collect(Collectors.toList());

        final Line2 rightHandSideLine2 = trajectory.rightHandSideLine2(goodFieldAreaWidth);
        final Line2 leftHandSideLine2 = trajectory.rightHandSideLine2(-goodFieldAreaWidth);

        return trajectory.dispersePoint3sWithDistance(deltaLength)
                .stream()
                .parallel()
                .map(point3WithDistance -> {
                    double distance = point3WithDistance.getDistance();
                    Point3 rightPoint = rightHandSideLine2.pointAt(distance).toPoint3();
                    Point3 leftPoint = leftHandSideLine2.pointAt(distance).toPoint3();

                    List<Double> bzList = BaseUtils.Python.linspaceStream(rightPoint, leftPoint, dotNumber)
                            .mapToDouble(p -> magnetAt(p).z)
                            .boxed()
                            .collect(Collectors.toList());
                    List<Point2> fitted = Point2.create(xList, bzList);

                    return BaseUtils.Content.BiContent.create(distance, fitter.fit(fitted));
                }).collect(Collectors.toList())
                .stream()
                .sorted(Comparator.comparingDouble(BaseUtils.Content.BiContent::getT1))
                .collect(Collectors.toList());
    }

    default List<BaseUtils.Content.BiContent<Double, double[]>> multiplePoleMagnetAlongTrajectory(
            final Line3 trajectory, final double deltaLength, final double goodFieldAreaWidth,
            final int order, final int dotNumber
    ) {
        PolynomialFitter fitter = PolynomialFitter.build(order);

        List<Double> xList = BaseUtils.Python.linspaceStream(-goodFieldAreaWidth, goodFieldAreaWidth, dotNumber)
                .boxed().collect(Collectors.toList());


        final Line3 rightHandSideLine3 = trajectory.rightHandSideLine3(goodFieldAreaWidth, Vector3.getZDirect());
        final Line3 leftHandSideLine3 = trajectory.rightHandSideLine3(-goodFieldAreaWidth, Vector3.getZDirect());

        return trajectory.dispersePoint3sWithDistance(deltaLength)
                .stream()
                .parallel()
                .map(point3WithDistance -> {
                    double distance = point3WithDistance.getDistance();
                    Point3 rightPoint = rightHandSideLine3.pointAt(distance);
                    Point3 leftPoint = leftHandSideLine3.pointAt(distance);

                    List<Double> bzList = BaseUtils.Python.linspaceStream(rightPoint, leftPoint, dotNumber)
                            .mapToDouble(p -> magnetAt(p).z)
                            .boxed()
                            .collect(Collectors.toList());
                    List<Point2> fitted = Point2.create(xList, bzList);

                    return BaseUtils.Content.BiContent.create(distance, fitter.fit(fitted));
                }).collect(Collectors.toList())
                .stream()
                .sorted(Comparator.comparingDouble(BaseUtils.Content.BiContent::getT1))
                .collect(Collectors.toList());
    }


    // 多级场 List<BaseUtils.Content.BiContent<Double, double[]>> -> List<List<Point2>>
    default List<List<Point2>> multiplePoleMagnetAlongTrajectoryBreak(
            final Line2 trajectory, final double deltaLength, final double goodFieldAreaWidth,
            final int order, final int dotNumber
    ) {

        List<List<Point2>> ret = new ArrayList<>();

        List<BaseUtils.Content.BiContent<Double, double[]>> biContents =
                multiplePoleMagnetAlongTrajectory(trajectory, deltaLength, goodFieldAreaWidth, order, dotNumber);

        for (int i = 0; i <= order; i++) {
            int finalI = i;
            List<Point2> collect = biContents.stream().map(bi -> {
                Double d = bi.getT1();
                double[] doubles = bi.getT2();

                return Point2.create(d, doubles[finalI]);
            }).collect(Collectors.toList());

            ret.add(collect);
        }

        return ret;
    }

    default List<List<Point2>> multiplePoleMagnetAlongTrajectoryBreak(
            final Line3 trajectory, final double deltaLength, final double goodFieldAreaWidth,
            final int order, final int dotNumber
    ) {

        List<List<Point2>> ret = new ArrayList<>();

        List<BaseUtils.Content.BiContent<Double, double[]>> biContents =
                multiplePoleMagnetAlongTrajectory(trajectory, deltaLength, goodFieldAreaWidth, order, dotNumber);

        for (int i = 0; i <= order; i++) {
            int finalI = i;
            List<Point2> collect = biContents.stream().map(bi -> {
                Double d = bi.getT1();
                double[] doubles = bi.getT2();

                return Point2.create(d, doubles[finalI]);
            }).collect(Collectors.toList());

            ret.add(collect);
        }

        return ret;
    }

    default double magnetGradientAtTrajectoryOfDistanceFast(
            final Line2 trajectory, final double distance, final double goodFieldAreaWidth) {
        Point3 leftHandSidePoint = trajectory.leftHandSidePoint(distance, goodFieldAreaWidth).toPoint3();
        Point3 rightHandSidePoint = trajectory.rightHandSidePoint(distance, goodFieldAreaWidth).toPoint3();

        double leftBz = magnetAt(leftHandSidePoint).z;
        double rightBz = magnetAt(rightHandSidePoint).z;

        return (leftBz - rightBz) / (2 * goodFieldAreaWidth);
    }

    default List<String> sliceToCosyScript(
            final double Bp, final double aperture,
            final Line2 trajectory, final double goodFieldAreaWidth,
            final double minStepLength, final double tolerance
    ) {
        List<String> answer = new ArrayList<>();

        List<BaseUtils.Content.BiContent<Double, double[]>> multiplePoleMagnetAlongTrajectory =
                multiplePoleMagnetAlongTrajectory(trajectory, minStepLength, goodFieldAreaWidth, 2, 6);

        int size = multiplePoleMagnetAlongTrajectory.size();

        final double STEP = multiplePoleMagnetAlongTrajectory.get(1).getT1()
                - multiplePoleMagnetAlongTrajectory.get(0).getT1();

        int i = 0;

        double totalLength = 0;

        while (i < size - 1) {
            double[] multi0 = multiplePoleMagnetAlongTrajectory.get(i).getT2();

            double B0 = multi0[0];
            if (Math.abs(Bp / B0) > 50) B0 = 0;
            double T0 = multi0[1];
            if (Math.abs(T0) < 0.1) T0 = 0;
            double L0 = multi0[2];
            if (Math.abs(L0) < 1.) L0 = 0;

            List<Double> Bs = BaseUtils.ListUtils.createListWithFirstElementIs(B0);
            List<Double> Ts = BaseUtils.ListUtils.createListWithFirstElementIs(T0);
            List<Double> Ls = BaseUtils.ListUtils.createListWithFirstElementIs(L0);

            int j;

            for (j = i + 1; j < size - 1; j++) {
                double[] multi = multiplePoleMagnetAlongTrajectory.get(j).getT2();
                double B = multi[0];
                if (Math.abs(Bp / B) > 50) B = 0;
                double T = multi[1];
                if (Math.abs(T) < 0.1) T = 0;
                double L = multi[2];
                if (Math.abs(L) < 1.) L = 0;

                Bs.add(B);
                Ts.add(T);
                Ls.add(L);

//                    if ((Math.abs((B0 - B) / B0) > tolerance)
//                            || (Math.abs((T0 - T) / T0) > tolerance)
//                            || (Math.abs((L0 - L) / L0) > tolerance)
//                    ) {
//                        break;
//                    }

                if (// list 长度至少为2
                        BaseUtils.Statistics.undulate(Bs) > tolerance ||
                                BaseUtils.Statistics.undulate(Ts) > tolerance ||
                                BaseUtils.Statistics.undulate(Ls) > tolerance
                ) {
                    break;
                }
            }

            B0 = BaseUtils.Statistics.average(Bs);
            T0 = BaseUtils.Statistics.average(Ts);
            L0 = BaseUtils.Statistics.average(Ls);


            double length = STEP * (j - i);

            {
                double len = multiplePoleMagnetAlongTrajectory.get(j).getT1()
                        - multiplePoleMagnetAlongTrajectory.get(i).getT1();
                BaseUtils.Equal.isEqual(len, length, 1e-10);
            }


            totalLength += length;
            i = j;


            double r = Bp / B0; // 半径 有正负

            double angle = BaseUtils.Converter.radianToAngle(length / Math.abs(r)); // 角度

            String changeDirect = r < 0 ? "CB ;" : ""; // 换方向

            double n1 = -r / B0 * T0;

            /**
             * ///////////////////  ///////////////////
             * 加上了奇怪的负号
             */
            double n2 = -r * r / B0 * L0;

            double b2 = T0 * aperture;

            double b3 = L0 * aperture * aperture;

            String cosyScript = null;

            if (Math.abs(r) > 50) {
                cosyScript = String.format(
                        "M5 %e %e %e 0 0 0 %e ;", length, b2, b3, aperture
                );
            } else {
                cosyScript = String.format("%s MS %e %e %e %e %e 0 0 0 ; %s", changeDirect,
                        Math.abs(r), angle, aperture, n1, n2, changeDirect);
            }

            //Logger.getLogger().debug("[{}]{}", totalLength, cosyScript);

            answer.add(cosyScript);
        }

        Logger.getLogger().info("size() = " + answer.size());

        return answer;
    }

    default List<String> sliceToCosyScript(
            final double Bp, final double aperture,
            final Line3 trajectory, final double goodFieldAreaWidth,
            final double minStepLength, final double maxStepLength, final double tolerance
    ) {
        List<String> answer = new ArrayList<>();

        List<BaseUtils.Content.BiContent<Double, double[]>> multiplePoleMagnetAlongTrajectory =
                multiplePoleMagnetAlongTrajectory(trajectory, minStepLength, goodFieldAreaWidth, 2, 6);

        int size = multiplePoleMagnetAlongTrajectory.size();

        final double STEP = multiplePoleMagnetAlongTrajectory.get(1).getT1()
                - multiplePoleMagnetAlongTrajectory.get(0).getT1();

        int i = 0;

        double totalLength = 0;

        while (i < size - 1) {
            double[] multi0 = multiplePoleMagnetAlongTrajectory.get(i).getT2();

            double B0 = multi0[0];
            if (Math.abs(Bp / B0) > 50) B0 = 0;
            double T0 = multi0[1];
            if (Math.abs(T0) < 0.1) T0 = 0;
            double L0 = multi0[2];
            if (Math.abs(L0) < 1.) L0 = 0;

            List<Double> Bs = BaseUtils.ListUtils.createListWithFirstElementIs(B0);
            List<Double> Ts = BaseUtils.ListUtils.createListWithFirstElementIs(T0);
            List<Double> Ls = BaseUtils.ListUtils.createListWithFirstElementIs(L0);

            int j;

            for (j = i + 1; j < size - 1; j++) {
                double[] multi = multiplePoleMagnetAlongTrajectory.get(j).getT2();
                double B = multi[0];
                if (Math.abs(Bp / B) > 50) B = 0;
                double T = multi[1];
                if (Math.abs(T) < 0.1) T = 0;
                double L = multi[2];
                if (Math.abs(L) < 1.) L = 0;

                Bs.add(B);
                Ts.add(T);
                Ls.add(L);

//                    if ((Math.abs((B0 - B) / B0) > tolerance)
//                            || (Math.abs((T0 - T) / T0) > tolerance)
//                            || (Math.abs((L0 - L) / L0) > tolerance)
//                    ) {
//                        break;
//                    }

                if (// list 长度至少为2
                        BaseUtils.Statistics.undulate(Bs) > tolerance ||
                                BaseUtils.Statistics.undulate(Ts) > tolerance ||
                                BaseUtils.Statistics.undulate(Ls) > tolerance ||
                                STEP * (j - i) > maxStepLength
                ) {
                    break;
                }
            }

            B0 = BaseUtils.Statistics.average(Bs);
            T0 = BaseUtils.Statistics.average(Ts);
            L0 = BaseUtils.Statistics.average(Ls);


            double length = STEP * (j - i);

            {
                double len = multiplePoleMagnetAlongTrajectory.get(j).getT1()
                        - multiplePoleMagnetAlongTrajectory.get(i).getT1();
                BaseUtils.Equal.isEqual(len, length, 1e-10);
            }


            totalLength += length;
            i = j;


            double r = Bp / B0; // 半径 有正负

            double angle = BaseUtils.Converter.radianToAngle(length / Math.abs(r)); // 角度

            String changeDirect = r < 0 ? "CB ;" : ""; // 换方向

            double n1 = -r / B0 * T0;

            /**
             * ///////////////////  ///////////////////
             * 加上了奇怪的负号
             */
            double n2 = -r * r / B0 * L0;

            double b2 = T0 * aperture;

            double b3 = L0 * aperture * aperture;

            String cosyScript = null;


            //--------------------
            //  半径从50m改为10m  |
            //  2020年8月24日     |
            //--------------------
            if (Math.abs(r) > 10) {
                cosyScript = String.format(
                        "M5 %e %e %e 0 0 0 %e ;", length, b2, b3, aperture
                );
            } else {
                cosyScript = String.format("%s MS %e %e %e %e %e 0 0 0 ; %s", changeDirect,
                        Math.abs(r), angle, aperture, n1, n2, changeDirect);
            }

            //Logger.getLogger().debug("[{}]{}", totalLength, cosyScript);

            answer.add(cosyScript);


        }

        Logger.getLogger().info("size() = " + answer.size());

        return answer;
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

        public Point2 getBmodWithDistance() {
            return Point2.create(distance, point3.toVector3().length());
        }
    }
}
