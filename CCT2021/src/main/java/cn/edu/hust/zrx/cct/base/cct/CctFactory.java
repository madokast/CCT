package cn.edu.hust.zrx.cct.base.cct;

import cn.edu.hust.zrx.cct.Logger;
import cn.edu.hust.zrx.cct.advanced.PolynomialFitter;
import cn.edu.hust.zrx.cct.base.BaseUtils;
import cn.edu.hust.zrx.cct.base.line.Line2;
import cn.edu.hust.zrx.cct.base.line.Trajectory;
import cn.edu.hust.zrx.cct.base.point.Point2;
import cn.edu.hust.zrx.cct.base.point.Point2To3;
import cn.edu.hust.zrx.cct.base.point.Point3;
import cn.edu.hust.zrx.cct.base.python.Plot2d;
import cn.edu.hust.zrx.cct.base.python.Plot3d;
import cn.edu.hust.zrx.cct.base.vector.Vector2;
import cn.edu.hust.zrx.cct.base.vector.Vector3;
import org.apache.commons.math3.fitting.PolynomialCurveFitter;

import java.io.File;
import java.io.FileWriter;
import java.rmi.server.ExportException;
import java.util.*;
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

    @SuppressWarnings("all")
    public static Cct createDipoleCctDetailed(
            double smallRInner, double smallROuter, double bigR, double angle, int windingNumber,
            double a0BipolarInner, double a1QuadrupleInner, double a2SextupleInner, double IInner,
            double a0BipolarOuter, double a1QuadrupleOuter, double a2SextupleOuter, double IOuter,
            int numberPerWinding,
            double startingθInner, double startingθOuter,
            double startingφInner, double startingφOuter,
            boolean directθInner, boolean directθOuter
    ) {
        CctLine2s cctLine2sInner =
                CctLine2Factory.create(smallRInner, bigR, angle,
                        windingNumber, a0BipolarInner, a1QuadrupleInner, a2SextupleInner, startingθInner, startingφInner, directθInner);

        CctLine2s cctLine2sOuter =
                CctLine2Factory.create(smallROuter, bigR, angle,
                        windingNumber, a0BipolarOuter, a1QuadrupleOuter, a2SextupleOuter, startingθOuter, startingφOuter, directθOuter);

        SoleLayerCct soleLayerCctInner = new SoleLayerCct(
                cctLine2sInner.disperseToPoint3(numberPerWinding), IInner);
        SoleLayerCct soleLayerCctOuter = new SoleLayerCct(
                cctLine2sOuter.disperseToPoint3(numberPerWinding), IOuter);

        return Cct.getEmptyCct().
                addSoleLayerCct(soleLayerCctInner).
                addSoleLayerCct(soleLayerCctOuter);
    }

    @SuppressWarnings("all")
    public static Cct createAgCct(
            double smallRInner, double smallROuter, double bigR, double[] angles, int[] windingNumbers,
            double a0BipolarInners, double a1QuadrupleInners, double a2SextupleInners, double IInner,
            double a0BipolarOuters, double a1QuadrupleOuters, double a2SextupleOuters, double IOuter,
            int numberPerWinding,
            double startingθInner, double startingθOuter,
            double startingφInner, double startingφOuter,
            boolean directθInner, boolean directθOuter
    ) {
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
                numberPerWinding, startingθInner, startingθOuter, startingφInner, startingφOuter, directθInner, directθOuter);
    }


    public static Cct combineCct(Cct... ccts) {

        Cct emptyCct = Cct.getEmptyCct();

        for (Cct cct : ccts) {
            emptyCct.addCct(cct);
        }

        return emptyCct;
    }

    public static Cct rotateInXYPlane(final Cct originCct, Point2 center, double phi) {
        return originCct.rotateInXYPlane(center, phi);
    }

    public static Cct move(final Cct originCct, Vector3 v) {
        return originCct.move(v);
    }

    @Deprecated
    public static Cct positionInXYPlane(final Cct originCct, double trajectoryR, Point2 startingPoint, Vector2 direct) {
        // 原先CCT 圆心 0,0,0  方向 y 方向
        Vector2 yDirect = Vector2.yDirect();

        Vector2 newCenter = startingPoint.toVector2().add(direct.copy().changeLengthSelf(trajectoryR).rotateSelf(BaseUtils.Converter.angleToRadian(90)));

        Cct move = originCct.move(newCenter.toVector3());

        return move.rotateInXYPlane(newCenter.toPoint2(), yDirect.angleTo(direct));

    }

    public static Cct positionInXYPlane(final Cct originCct, Point2 center, double rotateFromYDirectRadius) {
        Cct move = originCct.move(center.toVector3());
        return move.rotateInXYPlane(center, rotateFromYDirectRadius);
    }

    /**
     * 获得 关于 originCct 对称的 CCT
     * 注意对称只发生在 XY 平面
     * 利用 XY 平面的直线作为对称线
     *
     * @param originCct       原CCT
     * @param point2AtLine    对称线上任意点
     * @param directOfTheLine 对称线方向
     * @return 关于 originCct 对称的 CCT
     */
    public static Cct symmetryInXYPlaneByLine(final Cct originCct, Point2 point2AtLine, Vector2 directOfTheLine) {
        Cct emptyCct = Cct.getEmptyCct();
        for (SoleLayerCct soleLayerCct : originCct.getSoleLayerCctList()) {

            List<Point3> windings = soleLayerCct.windings;
            double I = soleLayerCct.I;

            windings = windings.stream()
                    .map(point3 -> {
                        double z = point3.z;
                        Point2 point2 = point3.toPoint2();

                        Point2 symmetryByLine = Point2.symmetryByLine(point2, point2AtLine, directOfTheLine);

                        return symmetryByLine.toPoint3(z);
                    })
                    .collect(Collectors.toList());

            emptyCct.addSoleLayerCct(new SoleLayerCct(windings, -I));
        }

        return emptyCct;
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

        private SoleLayerCct rotateInXYPlane(Point2 point2, double phi) {
            //private final List<Point3> windings;
            //        private final double I;

            List<Point3> rotateInXYPlane = Point3.rotateInXYPlane(this.getWindings(), point2, phi);

            return new SoleLayerCct(rotateInXYPlane, I);
        }

        private SoleLayerCct move(Vector3 v) {
            List<Point3> move = Point3.move(this.getWindings(), v);

            return new SoleLayerCct(move, I);
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
        public Cct addSoleLayerCct(final SoleLayerCct soleLayerCct) {
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
        public static Cct getEmptyCct() {
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

        public List<SoleLayerCct> getSoleLayerCctList() {
            return soleLayerCctList;
        }

        @Override
        public String toString() {
            return "Cct{" +
                    "soleLayerCctList.size=" + soleLayerCctList.size() +
                    '}';
        }

        /**
         * 绘图
         * 先全部外层后内层
         *
         * @param describes 描述
         */
        public void plot3(String... describes) {
            if (describes == null || describes.length == 0) {
                describes = new String[soleLayerCctList.size()];
                Arrays.fill(describes, Plot2d.BLUE_LINE);
            }

            int length = describes.length;
            int size = soleLayerCctList.size();
            if (!Objects.equals(length, size)) {
                Logger.getLogger().error("CCT绘制，describes数目不对。");
                Logger.getLogger().error("describes.length = " + length);
                Logger.getLogger().error("soleLayerCctList.size() = " + size);
            }

            int plotTimes = Math.min(length, size);

            final String[] temps = describes; // 不这么做lambda报错

            BaseUtils.StreamTools.forZeroToN(plotTimes)
                    .forEach(i -> this.soleLayerCctList.get(i).plot3(temps[i]));
        }

        public int cctLayerNumber() {
            return soleLayerCctList.size();
        }

        /**
         * 磁场
         *
         * @param p 点
         * @return p点磁场
         */
        @Override
        public Vector3 magnetAt(final Point3 p) {
            final Vector3 m = Vector3.getZero();
            this.soleLayerCctList.forEach(
                    soleLayerCct ->
                            m.addSelf(soleLayerCct.magnetAt(p))
            );

            return m;
        }

        private Cct rotateInXYPlane(Point2 point2, double phi) {
            Cct ret = new Cct();

            for (SoleLayerCct soleLayerCct : this.soleLayerCctList) {
                ret.addSoleLayerCct(soleLayerCct.rotateInXYPlane(point2, phi));
            }

            return ret;
        }

        private Cct move(Vector3 v) {
            Cct ret = new Cct();

            for (SoleLayerCct soleLayerCct : this.soleLayerCctList) {
                ret.addSoleLayerCct(soleLayerCct.move(v));
            }

            return ret;
        }
    }

    /**
     * 可以求磁场，接口： Vector3 magnetAt(Point3 p);
     */
    public static interface MagnetAble {

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

        default List<Point2> magnetBzAlongTrajectory(final Line2 trajectory) {
            return magnetBzAlongTrajectory(trajectory, MM);
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


        // 多级场 List<BaseUtils.Content.BiContent<Double, double[]>> -> List<List<Point2>>
        default List<List<Point2>> multiplePoleMagnetAlongTrajectoryBreak(
                final Line2 trajectory, final double deltaLength, final double goodFieldAreaWidth,
                final int order, final int dotNumber
        ) {

            List<List<Point2>> ret  = new ArrayList<>();

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

    public static class Elements implements MagnetAble {
        private List<MagnetAble> elements;

        private Elements() {
            this.elements = new ArrayList<>();
        }

        public static Elements empty() {
            return new Elements();
        }

        public void addElement(MagnetAble m) {
            this.elements.add(m);
        }

        @Override
        public Vector3 magnetAt(Point3 p) {
            Vector3 magnet = elements.get(0).magnetAt(p);

            for (int i = 1; i < elements.size(); i++) {
                magnet.addSelf(elements.get(i).magnetAt(p));
            }

            return magnet;
        }
    }

    /**
     * 硬边模型下 磁场分布
     * 想不到还有这个函数
     *
     * @param xStart          起点 (xStart,0)
     * @param magnetAndLength 磁场强度 和 其长度
     * @return 绘图关键点
     */
    public static List<Point2> scoffMagnetDistribution(double xStart, Point2... magnetAndLength) {
        List<Point2> ret = new ArrayList<>();
        for (Point2 point2 : magnetAndLength) {
            double magnet = point2.x;
            double length = point2.y;

            ret.add(Point2.create(xStart, magnet));
            ret.add(Point2.create(xStart + length, magnet));

            xStart += length;
        }

        return ret;
    }


    //  ------------       以下方法全部过失，因为无法指定内外两层CCT的绕线方向


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
    @Deprecated// 无法构建绕线相反的CCT
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
        return createDipoleCct(smallRInner, smallROuter,
                bigR, angle, windingNumber,
                a0BipolarInner, a1QuadrupleInner, a2SextupleInner, IInner,
                a0BipolarOuter, a1QuadrupleOuter, a2SextupleOuter, IOuter,
                numberPerWinding,
                0, 0, 0, 0, true);

        // 2020年4月14日 重构
//        CctLine2s cctLine2sInner =
//                CctLine2Factory.create(smallRInner, bigR, angle,
//                        windingNumber, a0BipolarInner, a1QuadrupleInner, a2SextupleInner);
//
//        CctLine2s cctLine2sOuter =
//                CctLine2Factory.create(smallROuter, bigR, angle,
//                        windingNumber, a0BipolarOuter, a1QuadrupleOuter, a2SextupleOuter);
//
//        SoleLayerCct soleLayerCctInner = new SoleLayerCct(
//                cctLine2sInner.disperseToPoint3(numberPerWinding), IInner);
//        SoleLayerCct soleLayerCctOuter = new SoleLayerCct(
//                cctLine2sOuter.disperseToPoint3(numberPerWinding), IOuter);
//
//        return Cct.getEmptyCct().
//                addSoleLayerCct(soleLayerCctInner).
//                addSoleLayerCct(soleLayerCctOuter);
    }

    @SuppressWarnings("all")
    @Deprecated// 无法构建绕线相反的CCT
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
                                      int numberPerWinding,
                                      double startingθInner,
                                      double startingθOuter,
                                      double startingφInner,
                                      double startingφOuter,
                                      boolean directθ) {
        CctLine2s cctLine2sInner =
                CctLine2Factory.create(smallRInner, bigR, angle,
                        windingNumber, a0BipolarInner, a1QuadrupleInner, a2SextupleInner, startingθInner, startingφInner, directθ);

        CctLine2s cctLine2sOuter =
                CctLine2Factory.create(smallROuter, bigR, angle,
                        windingNumber, a0BipolarOuter, a1QuadrupleOuter, a2SextupleOuter, startingθOuter, startingφOuter, directθ);

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
    @Deprecated// 无法构建绕线相反的CCT
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

    @SuppressWarnings("all")
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
                                  int numberPerWinding,
                                  double startingθInner,
                                  double startingθOuter,
                                  double startingφInner,
                                  double startingφOuter,
                                  boolean directθInner,
                                  boolean directθOuter) {
        BaseUtils.Equal.requireTrue(angles.length == windingNumbers.length, "AG-CCT构建参数有误");
        BaseUtils.Equal.requireTrue(windingNumbers.length == a0BipolarInners.length, "AG-CCT构建参数有误");
        BaseUtils.Equal.requireTrue(a0BipolarInners.length == a1QuadrupleInners.length, "AG-CCT构建参数有误");
        BaseUtils.Equal.requireTrue(a1QuadrupleInners.length == a2SextupleInners.length, "AG-CCT构建参数有误");
        BaseUtils.Equal.requireTrue(a2SextupleInners.length == a0BipolarOuters.length, "AG-CCT构建参数有误");
        BaseUtils.Equal.requireTrue(a0BipolarOuters.length == a1QuadrupleOuters.length, "AG-CCT构建参数有误");
        BaseUtils.Equal.requireTrue(a2SextupleOuters.length == a0BipolarInners.length, "AG-CCT构建参数有误");

        Cct cct = Cct.getEmptyCct();

        CctLine2s agcctInner = CctLine2Factory.createAGCCT(smallRInner, bigR, angles, windingNumbers,
                a0BipolarInners, a1QuadrupleInners, a2SextupleInners, startingθInner, startingφInner, directθInner);

        CctLine2s agcctOuter = CctLine2Factory.createAGCCT(smallROuter, bigR, angles, windingNumbers,
                a0BipolarOuters, a1QuadrupleOuters, a2SextupleOuters, startingθOuter, startingφOuter, directθOuter);

        for (CctLine2 cctLine2 : agcctOuter.cctLine2s) {
            cct.addSoleLayerCct(new SoleLayerCct(cctLine2.disperseToPoint3(numberPerWinding), IOuter));
        }

        for (CctLine2 cctLine2 : agcctInner.cctLine2s) {
            cct.addSoleLayerCct(new SoleLayerCct(cctLine2.disperseToPoint3(numberPerWinding), IInner));
        }

        return cct;
    }

    @Deprecated// 无法构建绕线相反的CCT
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
     * 创建单层CCT，这个主要用于基础性研究
     *
     * @param smallR           CCT半孔径
     * @param bigR             弯曲半径、即理想粒子的半径
     * @param angle            弯曲角度。典型值66.7度
     * @param windingNumber    匝数
     * @param a0Bipolar        a0 产生二极场
     * @param a1Quadruple      a1 产生四极场
     * @param a2Sextuple       a2 产生六极场
     * @param I                电流
     * @param numberPerWinding 每匝分段
     * @return 单层CCT
     */
    @Deprecated // 无法指定绕线方向
    public static SoleLayerCct createSoleLaterCct(double smallR,
                                                  double bigR,
                                                  double angle,
                                                  int windingNumber,
                                                  double a0Bipolar,
                                                  double a1Quadruple,
                                                  double a2Sextuple,
                                                  double I,
                                                  int numberPerWinding) {
        CctLine2s cctLine2 =
                CctLine2Factory.create(smallR, bigR, angle,
                        windingNumber, a0Bipolar, a1Quadruple, a2Sextuple);

        return new SoleLayerCct(
                cctLine2.disperseToPoint3(numberPerWinding), I);
    }

    /**
     * 创建单层CCT，这个主要用于基础性研究。比上个函数更深入底层
     *
     * @param smallR           CCT半孔径
     * @param bigR             弯曲半径、即理想粒子的半径
     * @param angle            弯曲角度。典型值66.7度
     * @param windingNumber    匝数
     * @param a0Bipolar        a0 产生二极场
     * @param a1Quadruple      a1 产生四极场
     * @param a2Sextuple       a2 产生六极场
     * @param I                电流
     * @param numberPerWinding 每匝分段
     * @param startingθ        起始 theta 类似于直线cct中的z
     * @param startingφ        起始 φ 绕线点
     * @param directθ          绕线方法，z 正方向？还是负方向
     * @return 单层CCT
     */
    public static SoleLayerCct createSoleLaterCct(double smallR,
                                                  double bigR,
                                                  double angle,
                                                  int windingNumber,
                                                  double a0Bipolar,
                                                  double a1Quadruple,
                                                  double a2Sextuple,
                                                  double I,
                                                  int numberPerWinding,
                                                  double startingθ,
                                                  double startingφ,
                                                  boolean directθ) {
        CctLine2s cctLine2 =
                CctLine2s.create(
                        new CctLine2(smallR, bigR, angle, windingNumber,
                                a0Bipolar, a1Quadruple, a2Sextuple,
                                startingθ, startingφ, directθ)
                );

        return new SoleLayerCct(
                cctLine2.disperseToPoint3(numberPerWinding), I);
    }
}
