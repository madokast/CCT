package zrx.CCT.ConcreteCCT;

import org.apache.commons.math3.util.FastMath;
import zrx.CCT.abstractCCT.CurvedCCT;
import zrx.Tools.ArrayMerge;
import zrx.Tools.InterpolationOfPolarCoordinate;
import zrx.base.point.Vector2d;
import zrx.base.point.Vector3d;

/**
 * 弯曲 CCT 分析
 * 用于获得大量分析数据
 * <p>
 * 这个工厂的产物只有两个，CCT 和 cct连接段。他们都是 SingleLayerDiscreteCCTComponent接口的实现类
 */

public class CurvedCCTAnalysis {
    /**
     * CCT 工厂
     * 不要再使用 CurvedCCT 中的构造器了
     * 因为他那边用的是 极点a 和 eta 的构造方式。反人类
     * 这里不免吐槽一下，java 居然没有内置 asinh() 函数，还得外调库
     * org.apache.commons.math3.util.FastMath 好东西啊好东西
     *
     * @param R         CCT 半径
     * @param r         线圈半径
     * @param n         匝数
     * @param I         电流
     * @param nth       1-二极CCT / 2-四极CCT
     * @param phi0      线圈绕一周CCT角度变化量
     * @param tiltAngle 倾斜角
     * @param stepKsi   步长，弧度
     * @return CCT 成品
     */
    public static CurvedCCT curvedCCTFactory(double R, double r, int n, double I, double nth, double phi0, double tiltAngle, double stepKsi) {
        double a = Math.sqrt(R * R - r * r);
        double eta0 = FastMath.asinh(Math.sqrt(Math.pow((R / r), 2) - 1));

        return new CurvedCCT(a, eta0, phi0, n, I, tiltAngle, nth, stepKsi);
    }

    /**
     * 获得离散化的CCT数据，主要用于 cct连接 画图 磁场计算
     *
     * @param cct 抽象的、没有实质的弯曲cct
     * @return 离散cct
     */
    public static DiscreteCCT discrete(final CurvedCCT cct) {
        final Vector2d[] pathInKsiPhi = cct.pointsOnKsiPhiCoordinateSystem();
        final Vector3d[] pathInXYZ = cct.coordinateSystemTransformateFromKsiPhiToXYZ(pathInKsiPhi);

        final Vector2d startPoint = cct.getStartPointInKsiPhiCoordinateSystem();
        final Vector2d startDirect = cct.getStartDirectInKsiPhiCoordinateSystem();
        final Vector2d endPoint = cct.getEndPointInKsiPhiCoordinateSystem();
        final Vector2d endDirect = cct.getEndDirectInKsiPhiCoordinateSystem();

        return new DiscreteCCT(cct, pathInKsiPhi, pathInXYZ, startPoint,
                startDirect, endPoint, endDirect);
    }

    /**
     * 二极场CCT 带有四极场 补偿的 DiscreteCCT
     *
     * @param cct      抽象的、没有实质的弯曲cct
     * @param gradient 需要补偿的四极场梯度
     * @return 离散cct
     */
    public static DiscreteCCT discreteWithFixDipole(final CurvedCCT cct, double gradient) {
        final Vector2d[] pathInKsiPhi = cct.pointsOnKsiPhiCoordinateSystemWithFixAtDipole(gradient);
        final Vector3d[] pathInXYZ = cct.coordinateSystemTransformateFromKsiPhiToXYZ(pathInKsiPhi);

        final Vector2d startPoint = cct.getStartPointInKsiPhiCoordinateSystem();
        final Vector2d startDirect = cct.getStartDirectInKsiPhiCoordinateSystem();
        final Vector2d endPoint = cct.getEndPointInKsiPhiCoordinateSystem();
        final Vector2d endDirect = cct.getEndDirectInKsiPhiCoordinateSystem();

        return new DiscreteCCT(cct, pathInKsiPhi, pathInXYZ, startPoint,
                startDirect, endPoint, endDirect);
    }

    /**
     * 构造CCT连接段
     * 非常需要耐心的调参。我能力不行，实在无法实现智能化。哎，留待后来人吧
     * 的确能力不够，但是至少连接段是平滑过渡的，即和两头的CCT即相接、而且接点处切线连续
     *
     * @param formCCT                  起点cct
     * @param toCCT                    终点cct
     * @param polePointInWardLength    对插值极点初步移动，即向内部移动，传入移动距离，方向为 toCCT.startDirect
     * @param polePointAdjust          对插值极点的进一步微调调整，传入一个有长度的方向，作为调整方法。一般需要微调
     * @param outWardLength            对中间外凸点位置调整，默认为连接点的中点，不向外移动(默认值绝对不行)
     * @param outWardDirectRotateFromY 对中间外凸点的方向调整(旋转角度)，默认为y轴/φ轴正向，不旋转，这个默认值可以为0
     * @param stepKsi                  插值步长，推荐2pi/1800
     * @return 连接段
     */
    public static ConnectionSegmentOfCCT connect(DiscreteCCT formCCT, DiscreteCCT toCCT, double polePointInWardLength, Vector2d polePointAdjust,
                                                 double outWardLength, double outWardDirectRotateFromY, double stepKsi) {
        //中点 mid
        final Vector2d midPoint = Vector2d.midpoint(formCCT.endPoint, toCCT.startPoint);

        //极坐标系的极点，由从中点移动 polePointAdjust 得到
        final Vector2d polePoint = Vector2d.walk(midPoint, toCCT.startDirect, polePointInWardLength).walkSelf(polePointAdjust);

        //连接段中，新增一个中间外凸点 intermediary 外凸距离 outWardLength
        final Vector2d intermediaryPoint = Vector2d.walk(midPoint, formCCT.endDirect, outWardLength);
        //中间外凸点的方向 默认Y正方向 提供旋转
        final Vector2d intermediaryPointDirect = Vector2d.getYDirect().rotateSelfAndReturn(outWardDirectRotateFromY);

        //插值第一段
        final Vector2d[] connectionSegment2d1 = InterpolationOfPolarCoordinate.interpolation2Point(
                polePoint, formCCT.endPoint, formCCT.endDirect, intermediaryPoint, intermediaryPointDirect, stepKsi);
        final Vector3d[] connectionSegment3d1 = formCCT.sourceCCT.
                coordinateSystemTransformateFromKsiPhiToXYZ(connectionSegment2d1);

//        Plot2d.plot2(connectionSegment2d1,Plot2d.YELLOW_LINE);

        //插值第二段
        final Vector2d[] connectionSegment2d2 = InterpolationOfPolarCoordinate.interpolation2Point(
                polePoint, intermediaryPoint, intermediaryPointDirect, toCCT.startPoint, toCCT.startDirect, stepKsi);
        final Vector3d[] connectionSegment3d2 = formCCT.sourceCCT.
                coordinateSystemTransformateFromKsiPhiToXYZ(connectionSegment2d2);

        //两段合并
        final Vector2d[] connectionSegment2d = ArrayMerge.merge(connectionSegment2d1, connectionSegment2d2);
        final Vector3d[] connectionSegment3d = ArrayMerge.merge(connectionSegment3d1, connectionSegment3d2);

        //恭喜差值完成。你就等着调参吧
        return new ConnectionSegmentOfCCT(connectionSegment2d, connectionSegment3d,
                formCCT.endPoint, formCCT.endDirect,
                toCCT.startPoint, toCCT.startDirect,
                intermediaryPoint, intermediaryPointDirect,
                polePoint, formCCT.sourceCCT, toCCT.sourceCCT);

    }


    /**
     * 用于快速构造AG-CCT
     *
     * @param cct1 ag-cct的第一段，由curvedCCTFactory 构成
     * @param Ns   每段cct的匝数，注意从第二段开始
     * @return DiscreteCCT数组，注意第一段也包含进去了
     */
    public static DiscreteCCT[] curvedCCTFactoryForAG_CCT(CurvedCCT cct1, int... Ns) {
        //加一段用于包含cct1
        final DiscreteCCT[] discreteCCTS = new DiscreteCCT[Ns.length + 1];
        discreteCCTS[0] = discrete(cct1);


        CurvedCCT previousCCT = cct1;
        for (int i = 0; i < Ns.length; i++) {
            CurvedCCT currentCCT = CurvedCCT.reverseWinding(previousCCT);
            currentCCT.setN(Ns[i]);
            discreteCCTS[i + 1] = discrete(currentCCT);

            previousCCT = currentCCT;

            //final CurvedCCT cct12 = CurvedCCT.reverseWinding(cct11);
            //cct12.setN(N12_MID);
            //final DiscreteCCT discreteCCT12 = CurvedCCTAnalysis.discrete(cct12);
        }

        return discreteCCTS;
    }
}

//        Vector2d cct12MidPoint = Vector2d.midpoint(cct1EndPoint,cct2StartPoint);
//        Vector2d cct12AddPoint = Vector2d.walk(cct12MidPoint,cct1EndDirect,0.3);
//        Vector2d cct12AddDirect = Vector2d.rotate(new Vector2d(0,1),0.0);
//        Vector2d mp = Vector2d.walk(cct12MidPoint,cct1EndDirect,-0.5).walkToYSelf(0.02);
//
//
//        Vector2d[] connectionSegment2d1 = InterpolationOfPolarCoordinate.interpolation2Point(
//                mp,
//                cct1EndPoint,cct1EndDirect,cct12AddPoint,cct12AddDirect,Math.PI/18000.0
//        );
//        Vector3d[] connectionSegment3d1 = cct.coordinateSystemTransformateFromKsiPhiToXYZ(connectionSegment2d1);
//
//        Plot2d.plot2(connectionSegment2d1, Plot2d.BLACK_LINE);
//        Plot3d.plot3(connectionSegment3d1, Plot2d.BLACK_LINE);
//
//        Vector2d[] connectionSegment2d2 = InterpolationOfPolarCoordinate.interpolation2Point(
//                mp,
//                cct12AddPoint,cct12AddDirect.reverseSelfAndReturn(),cct2StartPoint,cct2StartDirect.reverseSelfAndReturn(),Math.PI/18000.0
//        );
//        Vector3d[] connectionSegment3d2 = cct.coordinateSystemTransformateFromKsiPhiToXYZ(connectionSegment2d2);
//
//        Plot2d.plot2(connectionSegment2d2, Plot2d.GREEN_LINE);
//        Plot3d.plot3(connectionSegment3d2, Plot2d.GREEN_LINE);