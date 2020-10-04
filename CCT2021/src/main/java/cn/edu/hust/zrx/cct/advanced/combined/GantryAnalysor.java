package cn.edu.hust.zrx.cct.advanced.combined;

import cn.edu.hust.zrx.cct.base.BaseUtils;
import cn.edu.hust.zrx.cct.base.cct.Cct;
import cn.edu.hust.zrx.cct.base.cct.CctFactory;
import cn.edu.hust.zrx.cct.base.cct.Elements;
import cn.edu.hust.zrx.cct.base.cct.MagnetAble;
import cn.edu.hust.zrx.cct.base.line.Arcs;
import cn.edu.hust.zrx.cct.base.line.Line2;
import cn.edu.hust.zrx.cct.base.line.TrajectoryFactory;
import cn.edu.hust.zrx.cct.base.opticsMagnet.qs.QsHardPlaneMagnet;
import cn.edu.hust.zrx.cct.base.point.Point2;
import cn.edu.hust.zrx.cct.base.vector.Vector2;

import static cn.edu.hust.zrx.cct.advanced.combined.GantryData.FirstPart;
import static cn.edu.hust.zrx.cct.advanced.combined.GantryData.SecondPart;

import static cn.edu.hust.zrx.cct.base.BaseUtils.Constant.MM;

/**
 * Description
 * 机架分析器
 * <p>
 * Data
 * 15:51
 *
 * @author zrx
 * @version 1.0
 */

@SuppressWarnings("all")
public class GantryAnalysor {
    /**
     * 获取默认的 FirstPart
     *
     * @return FirstPart
     */
    public static GantryData.FirstPart defaultFirstPart() {
        return new FirstPart();
    }

    /**
     * 获取默认的 SecondPart
     *
     * @return SecondPart
     */
    public static GantryData.SecondPart defaultSecondPart() {
        return new SecondPart();
    }

    /**
     * 获取 firstPart 相关的轨道，默认起点为原点
     *
     * @param firstPart firstPart
     * @return firstPart 相关的轨道
     */
    public static Line2 getTrajectoryFirstPart(FirstPart firstPart) {
        return TrajectoryFactory.setStartingPoint(Point2.origin())
                .setStraightLine(firstPart.DL1, Vector2.xDirect())
                .addArcLine(firstPart.trajectoryBigRPart1, false, firstPart.dipoleCct12Angle)
                .addStraitLine(firstPart.GAP1)//gap1
                .addStraitLine(firstPart.QS1_LEN)//QS1_LEN
                .addStraitLine(firstPart.GAP2)//GAP2
                .addStraitLine(firstPart.QS2_LEN)//QS2_LEN
                .addStraitLine(firstPart.GAP2)//GAP2
                .addStraitLine(firstPart.QS1_LEN)//QS1_LEN
                .addStraitLine(firstPart.GAP1)//gap1
                .addArcLine(firstPart.trajectoryBigRPart1, false, firstPart.dipoleCct12Angle)
                .addStraitLine(firstPart.DL1);
    }

    /**
     * 获取 secondPart 相关的轨道
     * 因为起点默认不是原点
     * 所以应传入原点信息
     *
     * @param startingPoint  原点
     * @param startingDirect 原点处方向
     * @param secondPart     secondPart
     * @return 获取 secondPart 相关的轨道
     */
    public static Line2 getTrajectorySecondPart(Point2 startingPoint, Vector2 startingDirect, SecondPart secondPart) {
        return TrajectoryFactory.setStartingPoint(startingPoint)
                .setStraightLine(secondPart.DL2, startingDirect)
                .addArcLine(secondPart.trajectoryBigRPart2, true, secondPart.dipoleCct345Angle)
                .addStraitLine(secondPart.GAP3 + secondPart.QS3_LEN + secondPart.GAP3)
                .addArcLine(secondPart.trajectoryBigRPart2, true, secondPart.dipoleCct345Angle)
                .addStraitLine(secondPart.DL2);
    }

    /**
     * 获取 firstPart 对应的磁场
     * 由此可用于磁场分析、粒子跟踪等
     * 2020年10月3日 验证通过
     *
     * @param firstPart firstPart
     * @return firstPart 对应的磁场
     */
    public static MagnetAble firstPartMagnetAble(FirstPart firstPart) {
        Elements elements = Elements.empty();
        Line2 trajectoryPart1 = getTrajectoryFirstPart(firstPart);

        // QS12
        {// List<QsHardPlaneMagnet> qs = getQs12();
            QsHardPlaneMagnet QS11 = QsHardPlaneMagnet.create(firstPart.QS1_LEN, firstPart.QS1_GRADIENT,
                    firstPart.QS1_SECOND_GRADIENT, firstPart.QS1_APERTURE_MM,
                    trajectoryPart1.pointAt(firstPart.DL1 + firstPart.CCT12_LENGTH + firstPart.GAP1),
                    trajectoryPart1.directAt(firstPart.DL1 + firstPart.CCT12_LENGTH + firstPart.GAP1));

            QsHardPlaneMagnet QS2 = QsHardPlaneMagnet.create(firstPart.QS2_LEN, firstPart.QS2_GRADIENT,
                    firstPart.QS2_SECOND_GRADIENT, firstPart.QS2_APERTURE_MM,
                    trajectoryPart1.pointAt(firstPart.DL1 + firstPart.CCT12_LENGTH +
                            firstPart.GAP1 + firstPart.QS1_LEN + firstPart.GAP2),
                    trajectoryPart1.directAt(firstPart.DL1 + firstPart.CCT12_LENGTH +
                            firstPart.GAP1 + firstPart.QS1_LEN + firstPart.GAP2));


            QsHardPlaneMagnet QS12 = QsHardPlaneMagnet.create(firstPart.QS1_LEN, firstPart.QS1_GRADIENT,
                    firstPart.QS1_SECOND_GRADIENT, firstPart.QS1_APERTURE_MM,
                    trajectoryPart1.pointAt(firstPart.DL1 + firstPart.CCT12_LENGTH +
                            firstPart.GAP1 + firstPart.QS1_LEN + firstPart.GAP2 + firstPart.QS2_LEN + firstPart.GAP2),
                    trajectoryPart1.directAt(firstPart.DL1 + firstPart.CCT12_LENGTH +
                            firstPart.GAP1 + firstPart.QS1_LEN + firstPart.GAP2 + firstPart.QS2_LEN + firstPart.GAP2));

            elements.addElement(QS11);
            elements.addElement(QS2);
            elements.addElement(QS12);
        }

        // cct12
        {
            Cct agCct12 = CctFactory.createAgCct(
                    firstPart.agCct12SmallRInner, firstPart.agCct12SmallROuter, firstPart.agCct12BigR,
                    new double[]{firstPart.agCctAngle1, firstPart.agCctAngle2},
                    new int[]{firstPart.agCctWindingNumber1, firstPart.agCctWindingNumber2},
                    firstPart.agCct12A0Inner, firstPart.agCct12A1Inner, firstPart.agCct12A2Inner, firstPart.agCct12IInner,
                    firstPart.agCct12A0Outer, firstPart.agCct12A1Outer, firstPart.agCct12A2Outer, firstPart.agCct12IOuter,
                    firstPart.numberPerWinding,
                    firstPart.agCct12StartingθInner, firstPart.agCct12StartingθOuter,
                    firstPart.agCct12StartingφInner, firstPart.agCct12StartingφOuter,
                    firstPart.agCct12DirectθInner, firstPart.agCct12DirectθOuter
            );

            Cct dipoleCct12 = CctFactory.createDipoleCctDetailed(
                    firstPart.dipoleCct12SmallRInner, firstPart.dipoleCct12SmallROuter, firstPart.dipoleCct12BigR,
                    firstPart.dipoleCct12Angle, firstPart.dipoleCct12WindingNumber,
                    firstPart.dipoleCct12A0Inner, firstPart.dipoleCct12A1Inner, firstPart.dipoleCct12A2Inner, firstPart.dipoleCct12IInner,
                    firstPart.dipoleCct12A0Outer, firstPart.dipoleCct12A1Outer, firstPart.dipoleCct12A2Outer, firstPart.dipoleCct12IOuter,
                    firstPart.numberPerWinding,
                    firstPart.dipoleCct12StartingθInner, firstPart.dipoleCct12StartingθOuter,
                    firstPart.dipoleCct12StartingφInner, firstPart.dipoleCct12StartingφOuter,
                    firstPart.dipoleCct12DirectθInner, firstPart.dipoleCct12DirectθOuter
            );

            Cct cct12_10 = CctFactory.combineCct(agCct12, dipoleCct12);

            Cct cct12_1 = CctFactory.positionInXYPlane(cct12_10, Point2.create(firstPart.DL1, firstPart.trajectoryBigRPart1), BaseUtils.Converter.angleToRadian(-90));

            Cct cct12_20 = cct12_1.deepCopy();

            // getCct12_2()
            Cct cct12_2 = CctFactory.symmetryInXYPlaneByLine(
                    cct12_20,
                    trajectoryPart1.pointAt(firstPart.DL1 + firstPart.CCT12_LENGTH +
                            firstPart.GAP1 + firstPart.QS1_LEN + firstPart.GAP2 + firstPart.QS2_LEN / 2),
                    trajectoryPart1.directAt(firstPart.DL1 + firstPart.CCT12_LENGTH +
                            firstPart.GAP1 + firstPart.QS1_LEN + firstPart.GAP2 + firstPart.QS2_LEN / 2)
                            .rotateSelf(Math.PI / 2)
            );

            elements.addElement(cct12_1);
            elements.addElement(cct12_2);
        }

        return elements;
    }

    /**
     * 获取 secondPart 对应的磁场
     * 2020年10月4日 验证通过
     *
     * @param startingPoint  原点
     * @param startingDirect 原点处方向
     * @param secondPart     secondPart
     * @return secondPart 对应的磁场
     */
    public static MagnetAble secondPartMagnetAble(Point2 startingPoint, Vector2 startingDirect, SecondPart secondPart) {
        Elements elements = Elements.empty();
        Line2 trajectoryPart2 = getTrajectorySecondPart(startingPoint, startingDirect, secondPart);
        // QS
        {
            QsHardPlaneMagnet QS3 = QsHardPlaneMagnet.create(secondPart.QS3_LEN, secondPart.QS3_GRADIENT,
                    secondPart.QS3_SECOND_GRADIENT, secondPart.QS3_APERTURE_MM,
                    trajectoryPart2.pointAt(secondPart.DL2 + secondPart.CCT345_LENGTH + secondPart.GAP3),
                    trajectoryPart2.directAt(secondPart.DL2 + secondPart.CCT345_LENGTH + secondPart.GAP3));

            elements.addElement(QS3);
        }

        // CCT
        {
//            Cct cct345_1 = getCct345_1();

            Point2 p1 = trajectoryPart2.pointAt(secondPart.DL2);
            Point2 p2 = trajectoryPart2.pointAt(secondPart.DL2 + secondPart.CCT345_LENGTH / 2);
            Point2 p3 = trajectoryPart2.pointAt(secondPart.DL2 + secondPart.CCT345_LENGTH);

            Point2 center3 = Arcs.center(p1, p2, p3);

            Cct dipoleCct345 = CctFactory.createDipoleCctDetailed(
                    secondPart.dipoleCct345SmallRInner, secondPart.dipoleCct345SmallROuter, secondPart.dipoleCct345BigR,
                    secondPart.dipoleCct345Angle, secondPart.dipoleCct345WindingNumber,
                    secondPart.dipoleCct345A0Inner, secondPart.dipoleCct345A1Inner,
                    secondPart.dipoleCct345A2Inner, secondPart.dipoleCct345IInner,
                    secondPart.dipoleCct345A0Outer, secondPart.dipoleCct345A1Outer,
                    secondPart.dipoleCct345A2Outer, secondPart.dipoleCct345IOuter,
                    secondPart.numberPerWinding,
                    secondPart.dipoleCct345StartingθInner, secondPart.dipoleCct345StartingθOuter,
                    secondPart.dipoleCct345StartingφInner, secondPart.dipoleCct345StartingφOuter,
                    secondPart.dipoleCct345DirectθInner, secondPart.dipoleCct345DirectθOuter
            );
            Cct agCct345 = CctFactory.createAgCct(
                    secondPart.agCct345SmallRInner, secondPart.agCct345SmallROuter, secondPart.agCct345BigR,
                    new double[]{secondPart.agCctAngle5, secondPart.agCctAngle4, secondPart.agCctAngle3},
                    new int[]{secondPart.agCctWindingNumber5, secondPart.agCctWindingNumber4, secondPart.agCctWindingNumber3},
                    BaseUtils.ArrayUtils.repeat(secondPart.agCct345A0Inner, 3),  // BaseUtils.ArrayUtils.repeat(secondPart.agCct345A0Inner, 3)
                    BaseUtils.ArrayUtils.repeat(secondPart.agCct345A1Inner, 3),
                    BaseUtils.ArrayUtils.repeat(secondPart.agCct345A2Inner, 3),
                    secondPart.agCct345IInner,
                    BaseUtils.ArrayUtils.repeat(secondPart.agCct345A0Outer, 3),
                    BaseUtils.ArrayUtils.repeat(secondPart.agCct345A1Outer, 3),
                    BaseUtils.ArrayUtils.repeat(secondPart.agCct345A2Outer, 3),
                    secondPart.agCct345IOuter,
                    secondPart.numberPerWinding,
                    secondPart.agCct345StartingθInner, secondPart.agCct345StartingθOuter,
                    secondPart.agCct345StartingφInner, secondPart.agCct345StartingφOuter,
                    secondPart.agCct345DirectθInner, secondPart.agCct345DirectθOuter
            );

            dipoleCct345 = CctFactory.positionInXYPlane(dipoleCct345, center3, BaseUtils.Converter.angleToRadian(180 - secondPart.dipoleCct345Angle - 45));
            agCct345 = CctFactory.positionInXYPlane(agCct345, center3, BaseUtils.Converter.angleToRadian(180 - secondPart.dipoleCct345Angle - 45));


            Cct cct345_1 = CctFactory.combineCct(dipoleCct345, agCct345);

            //-----------------------------------------------------------------------------


            Point2 p = trajectoryPart2.pointAt(secondPart.DL2 + secondPart.CCT345_LENGTH +
                    secondPart.GAP3 + secondPart.QS3_LEN / 2);
            Vector2 d = trajectoryPart2.directAt(secondPart.DL2 + secondPart.CCT345_LENGTH +
                    secondPart.GAP3 + secondPart.QS3_LEN / 2).rotateSelf(Math.PI / 2);


            Cct cct345_2 = CctFactory.symmetryInXYPlaneByLine(cct345_1.deepCopy(), p, d);


            elements.addElement(cct345_1);
            elements.addElement(cct345_2);
        }

        return elements;
    }
}
