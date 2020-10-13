package cn.edu.hust.zrx.cct.advanced.combined;

import cn.edu.hust.zrx.cct.advanced.CctUtils;
import cn.edu.hust.zrx.cct.base.BaseUtils;
import cn.edu.hust.zrx.cct.base.cct.MagnetAble;
import cn.edu.hust.zrx.cct.base.line.Line2;
import cn.edu.hust.zrx.cct.base.point.Point2;
import cn.edu.hust.zrx.cct.base.python.Plot2d;
import cn.edu.hust.zrx.cct.study.W廖益诚机架.A0906廖益诚机架后偏转段建模;

import java.util.List;

import static cn.edu.hust.zrx.cct.base.BaseUtils.Constant.PRESENT;

class GantryTest {
    public static void main(String[] args) {
//        test1();
        test2();
    }

    private static void test2() {
        {
            GantryData.FirstPart firstPart = GantryAnalysor.defaultFirstPart();
            Line2 trajectoryFirstPart = GantryAnalysor.getTrajectoryFirstPart(firstPart);

            GantryData.SecondPart secondPart = GantryAnalysor.defaultSecondPart();


            MagnetAble magnetAble = GantryAnalysor.secondPartMagnetAble(
                    trajectoryFirstPart.pointAtEnd(),trajectoryFirstPart.directAtEnd(),secondPart);

            Line2 trajectorySecondPart = GantryAnalysor.getTrajectorySecondPart(
                    trajectoryFirstPart.pointAtEnd(),trajectoryFirstPart.directAtEnd(),secondPart);

//            List<Point2> list1 = CctUtils.trackingIdealParticle(trajectorySecondPart, trajectorySecondPart.getLength(), magnetAble, true);
//
//            Plot2d.plot2(list1,Plot2d.GREEN_LINE);

            List<List<Point2>> lists = magnetAble.multiplePoleMagnetAlongTrajectoryBreak(trajectorySecondPart, 1e-3, 10e-3, 1, 4);

            Plot2d.plot2(lists.get(0));

            Plot2d.showThread();
        }

    }

    private static void test1() {
        GantryData.FirstPart firstPart = GantryAnalysor.defaultFirstPart();

        MagnetAble magnetAble = GantryAnalysor.firstPartMagnetAble(firstPart);

        Line2 trajectoryFirstPart = GantryAnalysor.getTrajectoryFirstPart(firstPart);

//        List<Point2> y = CctUtils.trackingIdealParticle(trajectoryFirstPart, trajectoryFirstPart.getLength(), magnetAble, false);
//
//        // 0.6mm   -0.8mm
////        Plot2d.plot2(mm2mmm(x));
//
//        // 0mm  -0.7mm
//        Plot2d.plot2(mm2mmm(y));
//
//        Plot2d.showThread();

        CctUtils.analysePhaseEllipseAndPlot(
                trajectoryFirstPart.getLength(),
                true, 0 * PRESENT, 16,
                true, 1,
                magnetAble, trajectoryFirstPart,
                512, 5,
                List.of(BaseUtils.Content.BiContent.create("Optics", A0906廖益诚机架后偏转段建模.COSY_MAP.part1Optics.map))
        );
    }


}