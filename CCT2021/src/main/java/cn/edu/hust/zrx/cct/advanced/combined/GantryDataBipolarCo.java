package cn.edu.hust.zrx.cct.advanced.combined;

import cn.edu.hust.zrx.cct.base.BaseUtils;

import static cn.edu.hust.zrx.cct.base.BaseUtils.Constant.MM;

/**
 * Description
 * GantryDataBipolarCo
 * 双极点坐标系下机架
 *
 * <p>
 * Data
 * 16:11
 *
 * @author zrx
 * @version 1.0
 */

public class GantryDataBipolarCo {
    public static class FirstBend {
        // 孔径
        double agCct12InnerSmallR = 35 * MM;
        double agCct12OuterSmallR = 45 * MM;
        double diCct12InnerSmallR = 55 * MM;
        double diCct12OuterSmallR = 65 * MM;

        // 匝数
        int agCct1WindingNumber = 30;
        int agCct2WindingNumber = 39;
        int diCct12WindingNumber = 71;

        // 角度
        public final double dipoleCct12Angle = 22.5;
        public final double agCctAngle1 = 9.782608695652174;
        public final double agCctAngle2 = 12.717391304347826;


        // 长度
        public double trajectoryBigRPart1 = 0.95;

        public double DL1 = 0.825;
        public double DL2 = 2.000;
        public double QS1_LEN = 0.250;
        public double QS2_LEN = 0.250;
        public double QS3_LEN = 0.163;
        public double GAP1 = 0.200;
        public double GAP2 = 0.200;
        public double GAP3 = 0.176;
        public double BETWEEN_CCT225 = GAP1 * 2 + QS1_LEN * 2 + GAP2 * 2 + QS2_LEN;
        public double CCT12_LENGTH = trajectoryBigRPart1 * BaseUtils.Converter.angleToRadian(dipoleCct12Angle);
        public double CCT12_LENGTH_PART1 = trajectoryBigRPart1 * BaseUtils.Converter.angleToRadian(agCctAngle1);
        public double CCT12_LENGTH_PART2 = trajectoryBigRPart1 * BaseUtils.Converter.angleToRadian(agCctAngle2);

        public static FirstBend getDefault() {
            return new FirstBend();
        }
    }

    public static class SecondBend {
        // QS 磁铁
        public double QS3_GRADIENT = -11.78; // -11.78 T/m

        // 注意，六极场梯度一直存在错误，实际值应该乘二。由陈庚发现于 2020年5月26日
        public double QS3_SECOND_GRADIENT = -87.25 * 2.0 / 2.0; // -87.25 * 2.0 / 2.0 T/m2

        public double QS3_APERTURE_MM = 60;

        // 偏转半径
        public double trajectoryBigRPart2 = 0.95;

        public double dipoleCct345BigR = 0.95;
        public double agCct345BigR = 0.95;

        // CCT 孔径 半径
        public double smallRGap = 15 * MM;
        public double innerestSmallR = 83 * MM;
        public double agCct345SmallRInner = innerestSmallR;
        public double agCct345SmallROuter = innerestSmallR + smallRGap * 1;
        public double dipoleCct345SmallRInner = innerestSmallR + smallRGap * 2;
        public double dipoleCct345SmallROuter = innerestSmallR + smallRGap * 3;

        // 匝数
        public int dipoleCct345WindingNumber = 128;
        public int agCctWindingNumber3 = 20;
        public int agCctWindingNumber4 = 44;
        public int agCctWindingNumber5 = 44;  // sum 158

        // cct 角度
        public double dipoleCct345Angle = 67.5;
        public double agCctAngle3 = 12.386;
        public double agCctAngle4 = 27.344;
        public double agCctAngle5 = 27.770;

        // 倾角 90 度表示不倾斜
        public double[] dipoleCct345TiltAngles = {30, 90, 90, 90};
        public double[] agCct345TiltAngles = {90, 30, 90, 90};


        // 长度
        public double DL2 = 2.000;
        public double QS3_LEN = 0.163;
        public double GAP3 = 0.176;
        public double CCT345_LENGTH = trajectoryBigRPart2 * BaseUtils.Converter.angleToRadian(dipoleCct345Angle);
        public double CCT345_LENGTH_PART3 = trajectoryBigRPart2 * BaseUtils.Converter.angleToRadian(agCctAngle3);
        public double CCT345_LENGTH_PART4 = trajectoryBigRPart2 * BaseUtils.Converter.angleToRadian(agCctAngle4);
        public double CCT345_LENGTH_PART5 = trajectoryBigRPart2 * BaseUtils.Converter.angleToRadian(agCctAngle5);

        // 电流
        public double dipoleCct345I0 = -9206;
        //        public double dipoleCct345IOuter = dipoleCct345IInner;
        // 调至6500A，则a1调至
        public double agCct345I0 = -7037;
//        public double agCct345IOuter = agCct345IInner;


        public int disperseNumberPerWinding = 360;


        public static SecondBend getDefault() {
            return new GantryDataBipolarCo.SecondBend();
        }
    }
}
