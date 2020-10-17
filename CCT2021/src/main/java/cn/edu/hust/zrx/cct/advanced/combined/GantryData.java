package cn.edu.hust.zrx.cct.advanced.combined;

import cn.edu.hust.zrx.cct.advanced.MathFunction;
import cn.edu.hust.zrx.cct.base.BaseUtils;
import cn.edu.hust.zrx.cct.base.cct.CctLine2Factory;
import cn.edu.hust.zrx.cct.base.cct.EntitySoleLayerCct;
import cn.edu.hust.zrx.cct.base.point.Point2Function;
import cn.edu.hust.zrx.cct.base.point.Point3Function;
import cn.edu.hust.zrx.cct.base.point.SimpleToroidalCoordinateSystemPoint2To3;

import java.util.ArrayList;
import java.util.List;

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

        //-------------------------------------------------------------------------------------------//
        public MathFunction innerDipoleCctPath2d() {
            //this.smallR = smallR;
            //        this.bigR = bigR;
            //        this.angle = angle;
            //        this.windingNumber = windingNumber;
            //        this.a0Bipolar = a0Bipolar;
            //        this.a1Quadruple = a1Quadruple;
            //        this.a2Sextuple = a2Sextuple;
            //
            //        this.startingθ = startingθ;
            //        this.startingφ = startingφ;
            //        this.directθ = directθ;
            //
            //        double radian = BaseUtils.Converter.angleToRadian(this.angle);
            //
            //        if (directθ) {
            //            this.n = 2 * Math.PI * this.windingNumber / radian;
            //        } else {
            //            this.n = -2 * Math.PI * this.windingNumber / radian;
            //        }

            final double radian = BaseUtils.Converter.angleToRadian(dipoleCct345Angle); // 67.5

            final double n;
            final double reversed;
            if (dipoleCct345DirectθInner) {
                n = 2 * Math.PI * dipoleCct345WindingNumber / radian;
                reversed = 1.;
            } else {
                n = -2 * Math.PI * dipoleCct345WindingNumber / radian;
                reversed = -1.;
            }


            return θ -> dipoleCct345StartingφInner
                    + (θ * reversed - dipoleCct345StartingθInner) / n
                    + dipoleCct345A0Inner * Math.sin(θ * reversed)
                    + dipoleCct345A1Inner * Math.sin(2 * θ * reversed)
                    + dipoleCct345A2Inner * Math.sin(3 * θ * reversed);
        }

        public MathFunction outerDipoleCctPath2d() {
            //
            //        if (directθ) {
            //            this.n = 2 * Math.PI * this.windingNumber / radian;
            //        } else {
            //            this.n = -2 * Math.PI * this.windingNumber / radian;
            //        }

            final double radian = BaseUtils.Converter.angleToRadian(dipoleCct345Angle); // 67.5

            final double n;
            final double reversed;
            if (dipoleCct345DirectθOuter) {
                n = 2 * Math.PI * dipoleCct345WindingNumber / radian;
                reversed = 1.;
            } else {
                n = -2 * Math.PI * dipoleCct345WindingNumber / radian;
                reversed = -1.;
            }

            return θ -> dipoleCct345StartingφOuter
                    + (θ * reversed - dipoleCct345StartingθOuter) / n
                    + dipoleCct345A0Outer * Math.sin(θ * reversed)
                    + dipoleCct345A1Outer * Math.sin(2 * θ * reversed)
                    + dipoleCct345A2Outer * Math.sin(3 * θ * reversed);
        }

        public Point3Function innerDipoleCctPath3d() {

            MathFunction innerDipoleCctPath2d = innerDipoleCctPath2d();

            Point2Function cctPath2d = Point2Function.create(MathFunction.identity(), innerDipoleCctPath2d);

            return cctPath2d.convert(SimpleToroidalCoordinateSystemPoint2To3
                    .create(dipoleCct345BigR, dipoleCct345SmallRInner));
        }

        public Point3Function outerDipoleCctPath3d() {

            MathFunction outerDipoleCctPath2d = outerDipoleCctPath2d();

            Point2Function cctPath2d = Point2Function.create(MathFunction.identity(), outerDipoleCctPath2d);

            return cctPath2d.convert(SimpleToroidalCoordinateSystemPoint2To3
                    .create(dipoleCct345BigR, dipoleCct345SmallROuter));
        }

        // 这也是一坨屎
        public List<EntitySoleLayerCct> innerAgCctEntitySoleLayerCcts(double width, double depth, int brickNumberPerWinding) {
            //        this.centerCctPath3d = centerCctPath3d;
            //        this.centerCctPath2d = centerCctPath2d;
            //        this.simpleToroidalCoordinateSystemPoint2To3 = simpleToroidalCoordinateSystemPoint2To3;
            //        this.width = width;
            //        this.depth = depth;
            //        this.currentDensity = currentDensity;
            //        this.startTheta = startTheta;
            //        this.endTheta = endTheta;
            //        this.brickNumberPerWinding = brickNumberPerWinding;
            //        this.windNumber = windNumber;

            //CctLine2 cct0 = new CctLine2(smallR, bigR, angles[0], windingNumbers[0],
            //                a0Bipolars[0], a1Quadruples[0], a2Sextuples[0],
            //                startingθ, startingφ, directθ);
            //        CctLine2s cctLine2s = CctLine2s.create(cct0);
            //
            //        for (int i = 1; i < angles.length; i++) {
            //            startingθ += (directθ ? 1 : -1) * windingNumbers[i - 1] * 2 * Math.PI;
            //            startingφ += windingNumbers[i - 1] * 2 * Math.PI
            //                    / Math.abs(cctLine2s.cctLine2s.get(cctLine2s.cctLine2s.size() - 1).getN());
            //            directθ = !directθ;
            //
            //            cctLine2s.add(
            //                    new CctLine2(smallR, bigR, angles[i], windingNumbers[i],
            //                            a0Bipolars[i], a1Quadruples[i], a2Sextuples[i],
            //                            startingθ, startingφ, directθ)
            //            );
            //        }
            //
            //        return cctLine2s;
            double[] angles = new double[]{agCctAngle3, agCctAngle4, agCctAngle5};
            int[] windingNumbers = new int[]{agCctWindingNumber3, agCctWindingNumber4, agCctWindingNumber5};
            double startingθ = agCct345StartingθInner;
            double startingφ = agCct345StartingφInner;
            boolean directθ = agCct345DirectθInner;

            double[] startingθs = new double[angles.length];
            double[] endingθs = new double[angles.length];

            //-----------------------------------------------------------------

            startingθs[0] = startingθ;
            double radian = BaseUtils.Converter.angleToRadian(angles[0]);
            double n;
            double reversed;
            if (directθ) {
                n = 2 * Math.PI * windingNumbers[0] / radian;
                reversed = 1.;
            } else {
                n = -2 * Math.PI * windingNumbers[0] / radian;
                reversed = -1.;
            }

//            MathFunction cct2d1 = θ -> startingφ
//                    + (θ * reversed - startingθ) / n
//                    + agCct345A0Inner * Math.sin(θ * reversed)
//                    + agCct345A1Inner * Math.sin(2 * θ * reversed)
//                    + agCct345A2Inner * Math.sin(3 * θ * reversed);

            MathFunction cct2d1 = CctLine2Factory.createCct2dPathFunction(
                    startingφ, startingθ, 1, n,
                    agCct345A0Inner, agCct345A1Inner, agCct345A2Inner
            );

            List<Point2Function> cct2dList = new ArrayList<>(angles.length);
            cct2dList.add(Point2Function.create(MathFunction.identity(), cct2d1));

            //--------------------------------------------------------------------
            for (int i = 1; i < angles.length; i++) {
                startingθ += (directθ ? 1 : -1) * windingNumbers[i - 1] * 2 * Math.PI;
                startingφ += windingNumbers[i - 1] * 2 * Math.PI / Math.abs(n);
                directθ = !directθ;

                startingθs[i] = startingθ;
                endingθs[i - 1] = startingθ;
                if (directθ) {
                    n = 2 * Math.PI * windingNumbers[0] / radian;
                    reversed = 1.;
                } else {
                    n = -2 * Math.PI * windingNumbers[0] / radian;
                    reversed = -1.;
                }

                MathFunction cct2d2AndSoOn = CctLine2Factory.createCct2dPathFunction(
                        startingφ, startingθ, 1, n,
                        agCct345A0Inner, agCct345A1Inner, agCct345A2Inner
                );

                cct2dList.add(Point2Function.create(MathFunction.identity(), cct2d2AndSoOn));
            }

            endingθs[angles.length - 1] = startingθs[angles.length - 1] + (directθ ? 1 : -1) * windingNumbers[angles.length - 1] * 2 * Math.PI;
            //----------------------------------------------------------------------------

            //MathFunction innerDipoleCctPath2d = innerDipoleCctPath2d();
            //
            //            Point2Function cctPath2d = Point2Function.create(MathFunction.identity(), innerDipoleCctPath2d);
            //
            //            return cctPath2d.convert(Point2To3.SimpleToroidalCoordinateSystemPoint2To3
            //                    .create(dipoleCct345BigR, dipoleCct345SmallRInner));


            SimpleToroidalCoordinateSystemPoint2To3 simpleToroidalCoordinateSystemPoint2To3 =
                    SimpleToroidalCoordinateSystemPoint2To3.create(dipoleCct345BigR, dipoleCct345SmallRInner);

            List<EntitySoleLayerCct> ret = new ArrayList<>(angles.length);

            for (int i = 0; i < angles.length; i++) {
                Point2Function centerCctPath2d = cct2dList.get(i);
                Point3Function centerCctPath3d = centerCctPath2d.convert(simpleToroidalCoordinateSystemPoint2To3);

                //OperaCct operaCct = new OperaCct(
                //                innerDipoleCctPath3d, innerDipoleCctPath2d,
                //                Point2To3.SimpleToroidalCoordinateSystemPoint2To3.create(secondPart.dipoleCct345BigR, secondPart.dipoleCct345SmallRInner),
                //                2 * MM, 8 * MM,
                //                secondPart.dipoleCct345IInner / (2 * MM * 8 * MM),
                //                0,
                //                2 * PI * secondPart.dipoleCct345WindingNumber,
                //                180 * secondPart.dipoleCct345WindingNumber
                //        );


                // double startTheta, double endTheta


                EntitySoleLayerCct entitySoleLayerCct = new EntitySoleLayerCct(
                        centerCctPath3d, centerCctPath2d, simpleToroidalCoordinateSystemPoint2To3,
                        width, depth, agCct345IInner / (width * depth),
                        startingθs[i], endingθs[i], brickNumberPerWinding, windingNumbers[i]
                );

                ret.add(entitySoleLayerCct);
            }


            return ret;
        }

        public List<EntitySoleLayerCct> outerAgCctEntitySoleLayerCcts(double width, double depth, int brickNumberPerWinding) {

            double[] angles = new double[]{agCctAngle3, agCctAngle4, agCctAngle5};
            int[] windingNumbers = new int[]{agCctWindingNumber3, agCctWindingNumber4, agCctWindingNumber5};
            double startingθ = agCct345StartingθOuter;
            double startingφ = agCct345StartingφOuter;
            boolean directθ = agCct345DirectθOuter;

            double[] startingθs = new double[angles.length];
            double[] endingθs = new double[angles.length];

            //-----------------------------------------------------------------

            startingθs[0] = startingθ;
            double radian = BaseUtils.Converter.angleToRadian(angles[0]);
            double n;
            double reversed;
            if (directθ) {
                n = 2 * Math.PI * windingNumbers[0] / radian;
                reversed = 1.;
            } else {
                n = -2 * Math.PI * windingNumbers[0] / radian;
                reversed = -1.;
            }

            MathFunction cct2d1 = CctLine2Factory.createCct2dPathFunction(
                    startingφ, startingθ, 1, n,
                    agCct345A0Outer, agCct345A1Outer, agCct345A2Outer
            );

            List<Point2Function> cct2dList = new ArrayList<>(angles.length);
            cct2dList.add(Point2Function.create(MathFunction.identity(), cct2d1));

            //--------------------------------------------------------------------
            for (int i = 1; i < angles.length; i++) {
                startingθ += (directθ ? 1 : -1) * windingNumbers[i - 1] * 2 * Math.PI;
                startingφ += windingNumbers[i - 1] * 2 * Math.PI / Math.abs(n);
                directθ = !directθ;

                startingθs[i] = startingθ;
                endingθs[i - 1] = startingθ;
                if (directθ) {
                    n = 2 * Math.PI * windingNumbers[0] / radian;
                    reversed = 1.;
                } else {
                    n = -2 * Math.PI * windingNumbers[0] / radian;
                    reversed = -1.;
                }

                MathFunction cct2d2AndSoOn = CctLine2Factory.createCct2dPathFunction(
                        startingφ, startingθ, 1, n,
                        agCct345A0Outer, agCct345A1Outer, agCct345A2Outer
                );

                cct2dList.add(Point2Function.create(MathFunction.identity(), cct2d2AndSoOn));
            }

            endingθs[angles.length - 1] = startingθs[angles.length - 1] + (directθ ? 1 : -1) * windingNumbers[angles.length - 1] * 2 * Math.PI;
            //----------------------------------------------------------------------------


            SimpleToroidalCoordinateSystemPoint2To3 simpleToroidalCoordinateSystemPoint2To3 =
                    SimpleToroidalCoordinateSystemPoint2To3.create(dipoleCct345BigR, dipoleCct345SmallROuter);

            List<EntitySoleLayerCct> ret = new ArrayList<>(angles.length);

            for (int i = 0; i < angles.length; i++) {
                Point2Function centerCctPath2d = cct2dList.get(i);
                Point3Function centerCctPath3d = centerCctPath2d.convert(simpleToroidalCoordinateSystemPoint2To3);


                EntitySoleLayerCct entitySoleLayerCct = new EntitySoleLayerCct(
                        centerCctPath3d, centerCctPath2d, simpleToroidalCoordinateSystemPoint2To3,
                        width, depth, agCct345IOuter / (width * depth),
                        startingθs[i], endingθs[i], brickNumberPerWinding, windingNumbers[i]
                );

                ret.add(entitySoleLayerCct);
            }


            return ret;
        }


        @Override
        public String toString() {
            return "SecondPart{" +
                    "QS3_GRADIENT=" + QS3_GRADIENT +
                    ", QS3_SECOND_GRADIENT=" + QS3_SECOND_GRADIENT +
                    ", QS3_APERTURE_MM=" + QS3_APERTURE_MM +
                    ", trajectoryBigRPart2=" + trajectoryBigRPart2 +
                    ", dipoleCct345BigR=" + dipoleCct345BigR +
                    ", agCct345BigR=" + agCct345BigR +
                    ", dipoleCct345StartingθInner=" + dipoleCct345StartingθInner +
                    ", dipoleCct345StartingθOuter=" + dipoleCct345StartingθOuter +
                    ", dipoleCct345StartingφInner=" + dipoleCct345StartingφInner +
                    ", dipoleCct345StartingφOuter=" + dipoleCct345StartingφOuter +
                    ", dipoleCct345DirectθInner=" + dipoleCct345DirectθInner +
                    ", dipoleCct345DirectθOuter=" + dipoleCct345DirectθOuter +
                    ", agCct345StartingθInner=" + agCct345StartingθInner +
                    ", agCct345StartingθOuter=" + agCct345StartingθOuter +
                    ", agCct345StartingφInner=" + agCct345StartingφInner +
                    ", agCct345StartingφOuter=" + agCct345StartingφOuter +
                    ", agCct345DirectθInner=" + agCct345DirectθInner +
                    ", agCct345DirectθOuter=" + agCct345DirectθOuter +
                    ", agCct345SmallRInner=" + agCct345SmallRInner +
                    ", agCct345SmallROuter=" + agCct345SmallROuter +
                    ", dipoleCct345SmallRInner=" + dipoleCct345SmallRInner +
                    ", dipoleCct345SmallROuter=" + dipoleCct345SmallROuter +
                    ", dipoleCct345A0Inner=" + dipoleCct345A0Inner +
                    ", dipoleCct345A0Outer=" + dipoleCct345A0Outer +
                    ", dipoleCct345A1Inner=" + dipoleCct345A1Inner +
                    ", dipoleCct345A1Outer=" + dipoleCct345A1Outer +
                    ", dipoleCct345A2Inner=" + dipoleCct345A2Inner +
                    ", dipoleCct345A2Outer=" + dipoleCct345A2Outer +
                    ", agCct345A0Inner=" + agCct345A0Inner +
                    ", agCct345A0Outer=" + agCct345A0Outer +
                    ", agCct345A1Inner=" + agCct345A1Inner +
                    ", agCct345A1Outer=" + agCct345A1Outer +
                    ", agCct345A2Inner=" + agCct345A2Inner +
                    ", agCct345A2Outer=" + agCct345A2Outer +
                    ", dipoleCct345WindingNumber=" + dipoleCct345WindingNumber +
                    ", agCctWindingNumber3=" + agCctWindingNumber3 +
                    ", agCctWindingNumber4=" + agCctWindingNumber4 +
                    ", agCctWindingNumber5=" + agCctWindingNumber5 +
                    ", dipoleCct345Angle=" + dipoleCct345Angle +
                    ", agCctAngle3=" + agCctAngle3 +
                    ", agCctAngle4=" + agCctAngle4 +
                    ", agCctAngle5=" + agCctAngle5 +
                    ", DL2=" + DL2 +
                    ", QS3_LEN=" + QS3_LEN +
                    ", GAP3=" + GAP3 +
                    ", CCT345_LENGTH=" + CCT345_LENGTH +
                    ", CCT345_LENGTH_PART3=" + CCT345_LENGTH_PART3 +
                    ", CCT345_LENGTH_PART4=" + CCT345_LENGTH_PART4 +
                    ", CCT345_LENGTH_PART5=" + CCT345_LENGTH_PART5 +
                    ", dipoleCct345IInner=" + dipoleCct345IInner +
                    ", dipoleCct345IOuter=" + dipoleCct345IOuter +
                    ", agCct345IInner=" + agCct345IInner +
                    ", agCct345IOuter=" + agCct345IOuter +
                    ", numberPerWinding=" + numberPerWinding +
                    '}';
        }
    }

}
