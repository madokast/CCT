package cn.edu.hust.zrx.cct.base.cct;

import cn.edu.hust.zrx.cct.base.BaseUtils;
import cn.edu.hust.zrx.cct.base.point.Point2;
import cn.edu.hust.zrx.cct.base.point.Point3;
import cn.edu.hust.zrx.cct.base.vector.Vector2;
import cn.edu.hust.zrx.cct.base.vector.Vector3;

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

    /**
     * 创建弯转的二极CCT 包络绕线方向和绕线起点
     * @param smallRInner 内层CCT 小半径 就是半孔径 单位 m
     * @param smallROuter 外层CCT 小半径
     * @param bigR 大半径 CCT中轴线的偏转半径 单位 m
     * @param angle 偏转角度 角度制
     * @param windingNumber 匝数
     * @param a0BipolarInner 内层CCT的绕线方程 参数 a0
     * @param a1QuadrupleInner 内层CCT的绕线方程 参数 a1
     * @param a2SextupleInner 内层CCT的绕线方程 参数 a2
     * @param IInner 内层CCT电流 安培
     * @param a0BipolarOuter 外层CCT的绕线方程 参数 a0
     * @param a1QuadrupleOuter 外层CCT的绕线方程 参数 a1
     * @param a2SextupleOuter 外层CCT的绕线方程 参数 a2
     * @param IOuter 外层CCT电流 安培
     * @param numberPerWinding 每匝线圈 分解段数 越大则计算结果越准确
     * @param startingθInner CCT路径方程由
     * @param startingθOuter 外层初始绕线位置，即直线 CCT 中的 θ ,周期 2pi 函数
     * @param startingφInner 类似直线 CCT 的 Z
     * @param startingφOuter 类似直线 CCT 的 Z
     * @param directθInner 绕线方向，true代表逆时针
     * @param directθOuter 绕线方向，true代表逆时针
     * @return 弯转的二极CCT
     */
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

    /**
     * @see CctFactory#createDipoleCctDetailed(double, double, double,
     * double, int, double, double, double, double, double, double,
     * double, double, int, double, double, double, double, boolean,
     * boolean)
     *
     */
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

        return move.rotateInXYPlane(newCenter.toPoint2(), yDirect.angleToInRadian(direct));

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
