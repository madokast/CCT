package cn.edu.hust.zrx.cct.advanced.combined;

import cn.edu.hust.zrx.cct.base.BaseUtils;

import static cn.edu.hust.zrx.cct.base.BaseUtils.Constant.MM;

/**
 * Description
 * gantry pojo
 * <p>
 * Data
 * 17:10
 *
 * @author zrx
 * @version 1.0
 */

@SuppressWarnings("all")
public class GantryData {
    public static class FirstPart {

        public double QS1_GRADIENT = 28.33; // T/m
        public double QS2_GRADIENT = -12.12; // -12.12 T/m

        // 注意，六极场梯度一直存在错误，实际值应该乘二。由陈庚发现于 2020年5月26日
        public double QS1_SECOND_GRADIENT = -140.44 * 2.0 / 2.0; // -140.44 * 2.0 / 2.0 T/m2
        public double QS2_SECOND_GRADIENT = 316.22 * 2.0 / 2.0; // T/m2

        public double QS1_APERTURE_MM = 30;
        public double QS2_APERTURE_MM = 30;


        // 偏转半径
        public double trajectoryBigRPart1 = 0.95;

        public double dipoleCct12BigR = 0.95;
        public double agCct12BigR = 0.95 + 0.1875 * MM;

        // 初始绕线位置
        public double dipoleCct12StartingθInner = 0.0;
        public double dipoleCct12StartingθOuter = 0.0;
        public double dipoleCct12StartingφInner = 0.0;
        public double dipoleCct12StartingφOuter = 0.0;
        public boolean dipoleCct12DirectθInner = true;
        public boolean dipoleCct12DirectθOuter = false;

        public double agCct12StartingθInner = 0.0;
        public double agCct12StartingθOuter = 0.0;
        public double agCct12StartingφInner = 0.0;
        public double agCct12StartingφOuter = 0.0;
        public boolean agCct12DirectθInner = false;
        public boolean agCct12DirectθOuter = true;

        // CCT孔径
        public double agCct12SmallRInner = 23.5 * MM;
        public double agCct12SmallROuter = 33.5 * MM;
        public double dipoleCct12SmallRInner = 43 * MM;
        public double dipoleCct12SmallROuter = 52 * MM;

        // a0 a1 a2
        public double dipoleCct12A0Inner = -dipoleCct12SmallRInner * Math.sqrt(3) / dipoleCct12BigR; // 2020年9月4日 调整后倾斜角为30度整
        public double dipoleCct12A0Outer = dipoleCct12SmallROuter * Math.sqrt(3) / dipoleCct12BigR;
        public double dipoleCct12A1Inner = Math.pow(dipoleCct12SmallRInner, 2) * 0.25;
        public double dipoleCct12A1Outer = -Math.pow(dipoleCct12SmallROuter, 2) * 0.25;
        public double dipoleCct12A2Inner = 0.0;
        public double dipoleCct12A2Outer = 0.0;

        public double agCct12A0Inner = 0.0;
        public double agCct12A0Outer = 0.0;
        public double agCct12A1Inner = -Math.pow(agCct12SmallRInner, 2) * Math.sqrt(3) * 20;
        public double agCct12A1Outer = Math.pow(agCct12SmallROuter, 2) * Math.sqrt(3) * 20;
        public double agCct12A2Inner = 0.0;
        public double agCct12A2Outer = 0.0;

        // 匝数
        public int dipoleCct12WindingNumber = 71;
        public int agCctWindingNumber1 = 30;
        public int agCctWindingNumber2 = 39; // sum = 69

        // CCT角度
        public double dipoleCct12Angle = 22.5;
        public double agCctAngle1 = 22.5 * agCctWindingNumber1 / (agCctWindingNumber1 + agCctWindingNumber2);//9.782608695652174
        public double agCctAngle2 = 22.5 * agCctWindingNumber2 / (agCctWindingNumber1 + agCctWindingNumber2);//12.717391304347826

        // 长度
        public double DL1 = 0.825;
        public double QS1_LEN = 0.250;
        public double QS2_LEN = 0.250;
        public double GAP1 = 0.200;
        public double GAP2 = 0.200;
        public double BETWEEN_CCT225 = GAP1 * 2 + QS1_LEN * 2 + GAP2 * 2 + QS2_LEN;
        public double CCT12_LENGTH = trajectoryBigRPart1 * BaseUtils.Converter.angleToRadian(dipoleCct12Angle);
        public double CCT12_LENGTH_PART1 = trajectoryBigRPart1 * BaseUtils.Converter.angleToRadian(agCctAngle1);
        public double CCT12_LENGTH_PART2 = trajectoryBigRPart1 * BaseUtils.Converter.angleToRadian(agCctAngle2);

        // 电流
        public double dipoleCct12IInner = 6191.87824; // 求解获得 2020年9月5日
        public double dipoleCct12IOuter = dipoleCct12IInner;
        public double agCct12IInner = -3319.43418579895;// 观察图像活得 2020年9月5日
        public double agCct12IOuter = agCct12IInner;

        // CCT每匝分段
        public int numberPerWinding = 360;
    }

    public static class SecondPart {
        public double QS3_GRADIENT = -11.78; // -11.78 T/m

        // 注意，六极场梯度一直存在错误，实际值应该乘二。由陈庚发现于 2020年5月26日
        public double QS3_SECOND_GRADIENT = -87.25 * 2.0 / 2.0; // -87.25 * 2.0 / 2.0 T/m2

        public double QS3_APERTURE_MM = 60;

        // 偏转半径
        public double trajectoryBigRPart2 = 0.95;

        public double dipoleCct345BigR = 0.95;
        public double agCct345BigR = 0.95; // + 1.12 * MM

        // 初始绕线位置
        public double dipoleCct345StartingθInner = 0.0;
        public double dipoleCct345StartingθOuter = 0.0;
        public double dipoleCct345StartingφInner = 0.0;
        public double dipoleCct345StartingφOuter = 0.0;
        public boolean dipoleCct345DirectθInner = true;
        public boolean dipoleCct345DirectθOuter = false;
        public double agCct345StartingθInner = 0.0;
        public double agCct345StartingθOuter = 0.0;
        public double agCct345StartingφInner = 0.0;
        public double agCct345StartingφOuter = 0.0;
        public boolean agCct345DirectθInner = false;
        public boolean agCct345DirectθOuter = true;

        // CCT孔径
        public double agCct345SmallRInner = 23.5 * MM + 40 * MM;
        public double agCct345SmallROuter = 33.5 * MM + 40 * MM;
        public double dipoleCct345SmallRInner = 43 * MM + 40 * MM;
        public double dipoleCct345SmallROuter = 52 * MM + 40 * MM;

        public double dipoleCct345A0Inner = -dipoleCct345SmallRInner * Math.sqrt(3) / dipoleCct345BigR; // 2020年9月4日 调整后倾斜角为30度整
        public double dipoleCct345A0Outer = dipoleCct345SmallROuter * Math.sqrt(3) / dipoleCct345BigR;
        public double dipoleCct345A1Inner = Math.pow(dipoleCct345SmallRInner, 2) * 0.25;
        public double dipoleCct345A1Outer = -Math.pow(dipoleCct345SmallROuter, 2) * 0.25;
        public double dipoleCct345A2Inner = 0.0;
        public double dipoleCct345A2Outer = 0.0;

        // 数组
        public double agCct345A0Inner = 0.;
        public double agCct345A0Outer = 0.;
        public double agCct345A1Inner = -Math.pow(agCct345SmallRInner, 2) * Math.sqrt(3) * 20 * 0.19;
        public double agCct345A1Outer = Math.pow(agCct345SmallROuter, 2) * Math.sqrt(3) * 20 * 0.19;
        public double agCct345A2Inner = 0.;
        public double agCct345A2Outer = 0.;

        // 匝数
        public int dipoleCct345WindingNumber = 201;
        public int agCctWindingNumber3 = 29;
        public int agCctWindingNumber4 = 64;
        public int agCctWindingNumber5 = 65;  // sum 158

        // cct 角度
        public double dipoleCct345Angle = 67.5;
        public double agCctAngle3 = 67.5 * agCctWindingNumber3 / (agCctWindingNumber3 + agCctWindingNumber4 + agCctWindingNumber5); //12.389
        public double agCctAngle4 = 67.5 * agCctWindingNumber4 / (agCctWindingNumber3 + agCctWindingNumber4 + agCctWindingNumber5); //27.3418
        public double agCctAngle5 = 67.5 * agCctWindingNumber5 / (agCctWindingNumber3 + agCctWindingNumber4 + agCctWindingNumber5); //27.7690

        // 长度
        public double DL2 = 2.000;
        public double QS3_LEN = 0.163;
        public double GAP3 = 0.176;
        public double CCT345_LENGTH = trajectoryBigRPart2 * BaseUtils.Converter.angleToRadian(dipoleCct345Angle);
        public double CCT345_LENGTH_PART3 = trajectoryBigRPart2 * BaseUtils.Converter.angleToRadian(agCctAngle3);
        public double CCT345_LENGTH_PART4 = trajectoryBigRPart2 * BaseUtils.Converter.angleToRadian(agCctAngle4);
        public double CCT345_LENGTH_PART5 = trajectoryBigRPart2 * BaseUtils.Converter.angleToRadian(agCctAngle5);

        // 电流
        public double dipoleCct345IInner = -6529.971375582991; // 2020年9月9日 -6738.987300872428 。2020年9月11日 改为 -6529.971375582991
        public double dipoleCct345IOuter = dipoleCct345IInner;
        // 调至6500A，则a1调至
        public double agCct345IInner = 8171; // 9799 // 2020年6月11日 *0.995 = 7811. 2020年9月11日 改为 8171
        public double agCct345IOuter = agCct345IInner;

        // CCT每匝分段
        public int numberPerWinding = 360;
    }
}
