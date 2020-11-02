package cn.edu.hust.zrx.cct.study.V双极点坐标系机架;

import cn.edu.hust.zrx.cct.Logger;
import cn.edu.hust.zrx.cct.advanced.MathFunction;
import cn.edu.hust.zrx.cct.advanced.opera.Brick8s;
import cn.edu.hust.zrx.cct.advanced.opera.Brick8sSet;
import cn.edu.hust.zrx.cct.advanced.opera.OperaCct;
import cn.edu.hust.zrx.cct.base.BaseUtils;
import cn.edu.hust.zrx.cct.base.annotation.Run;
import cn.edu.hust.zrx.cct.base.cct.Cct;
import cn.edu.hust.zrx.cct.base.cct.SoleLayerCct;
import cn.edu.hust.zrx.cct.base.line.Trajectory;
import cn.edu.hust.zrx.cct.base.line.TrajectoryFactory;
import cn.edu.hust.zrx.cct.base.point.*;
import cn.edu.hust.zrx.cct.base.point.BipolarToroidalCoordinateSystemPoint2To3;
import cn.edu.hust.zrx.cct.base.python.Plot2d;
import cn.edu.hust.zrx.cct.base.python.Plot3d;
import cn.edu.hust.zrx.cct.base.vector.Vector2;
import org.apache.commons.math3.util.FastMath;

import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import static cn.edu.hust.zrx.cct.base.BaseUtils.Constant.MM;

/**
 * Description
 * 2020年10月19日 设计完成
 * <p>
 * Data
 * 10:14
 *
 * @author zrx
 * @version 1.0
 */

public class B20201019双极点坐标系新机架设计_分析原CCT6T以上怎么就降低了呢 {

    private static final int numberPerWinding = 60;

    private static class B20201012Utils {
        public static SoleLayerCct createSoleLayerCct(
                final double bigR,
                final double smallR,
                final MathFunction phiKsiFun,
                final double I,
                final double startKsi,
                final double endKsi,
                final int totalDisperseNumber
        ) {

            final double a = Math.sqrt(bigR * bigR - smallR * smallR);

            final double eta = 0.5 * Math.log((bigR + a) / (bigR - a));
            final double eta_1 = FastMath.asinh(Math.sqrt((bigR * bigR) / (smallR * smallR) - 1)); // 另一种计算 eta 方法

            BaseUtils.Equal.requireEqual(eta, eta_1, "两种计算 η 的方法发生了冲突？难以置信");

            Point2Function path2P2F = Point2Function.create(MathFunction.identity(), phiKsiFun);

            BipolarToroidalCoordinateSystemPoint2To3 bipolarToroidalCoordinateSystemPoint2To3 =
                    new BipolarToroidalCoordinateSystemPoint2To3(a, eta);

            Point3Function path3P3F = bipolarToroidalCoordinateSystemPoint2To3.convert(path2P2F);

            List<Point3> dispersedPath3 = path3P3F.disperse(startKsi, endKsi, totalDisperseNumber);

            SoleLayerCct soleLayerCct = SoleLayerCct.create(dispersedPath3, I);

            return soleLayerCct;
        }

        public static MathFunction createPhiKsiFun(
                final double bigR,
                final double smallR, // 用于计算 eta
                final double phi0, // 一匝线圈转过的弧度， 如 159匝 50度线圈，则 phi0 = ang2rad(50./159.)
                final double... tiltAngles // 各级倾斜角
        ) {
            final double a = Math.sqrt(bigR * bigR - smallR * smallR);

            final double eta = 0.5 * Math.log((bigR + a) / (bigR - a));
            final double eta_1 = FastMath.asinh(Math.sqrt((bigR * bigR) / (smallR * smallR) - 1)); // 另一种计算 eta 方法

            BaseUtils.Equal.requireEqual(eta, eta_1, "两种计算 η 的方法发生了冲突？难以置信");

            return ksi -> {
                double phi = phi0 * ksi / (2 * Math.PI);
                for (int i = 0; i < tiltAngles.length; i++) {
                    if (BaseUtils.Equal.isEqual(tiltAngles[i], 90.)) continue;
                    phi += (1 / Math.tan(BaseUtils.Converter.angleToRadian(tiltAngles[i]))) // cot(tilt)
                            /
                            ((i + 1) * Math.sinh(eta)) // n*sh(eta)
                            *
                            Math.sin((i + 1) * ksi);
                }
                return phi;
            };
        }

        public static BipolarToroidalCoordinateSystemPoint2To3 createBipolarToroidalCoordinateSystemPoint2To3(
                final double bigR,
                final double smallR
        ) {
            final double a = Math.sqrt(bigR * bigR - smallR * smallR);

            final double eta = 0.5 * Math.log((bigR + a) / (bigR - a));
            final double eta_1 = FastMath.asinh(Math.sqrt((bigR * bigR) / (smallR * smallR) - 1)); // 另一种计算 eta 方法

            BaseUtils.Equal.requireEqual(eta, eta_1, "两种计算 η 的方法发生了冲突？难以置信");

            return new BipolarToroidalCoordinateSystemPoint2To3(a, eta);
        }

        public static Brick8s createBrick8s(
                final double I,
                final int totalDisperseNumber,
                final double slotDepth, // 放线圈的槽的深度 如 8mm
                final double slotWidth, // 放线圈的槽的宽度 如 2mm
                final MathFunction phiKsiFun,
                final double startKsi,
                final double endKsi,
                final BipolarToroidalCoordinateSystemPoint2To3 bipolarToroidalCoordinateSystemPoint2To3
        ) {
            Point2Function path2P2F = Point2Function.create(MathFunction.identity(), phiKsiFun);
            Point3Function path3P3F = bipolarToroidalCoordinateSystemPoint2To3.convert(path2P2F);


            OperaCct operaCct = new OperaCct(
                    path3P3F, path2P2F,
                    bipolarToroidalCoordinateSystemPoint2To3,
                    slotWidth, slotDepth,
                    I / (slotWidth * slotDepth),
                    startKsi,
                    endKsi,
                    totalDisperseNumber
            );


            return operaCct.createBrick8s();

        }
    }

    @Run(10)
    public Brick8sSet operaOrigin() throws IOException {
        Brick8sSet brick8sSet = Brick8sSet.empty();
        Cct cct = Cct.getEmptyCct();

        final double GAP = 10 * MM;
        final double inner = 99 * MM;

        {
            final double bigR = 0.95;
            final double innerSmallR = inner + GAP * 2;
            final double outerSmall = inner + GAP * 3;
            final double bendingAngle = 67.5;
            final double bendingRadian = BaseUtils.Converter.angleToRadian(bendingAngle);
            final double tiltAngle = 30;
            final double tiltRadian = BaseUtils.Converter.angleToRadian(tiltAngle);
            final int windingNumber = 201;
            final double endKsi = windingNumber * Math.PI * 2;
            final double I0 = 6530; // 2.355 T
            final double phi0 = bendingRadian / windingNumber;
            final double slotDepth = 8 * MM;
            final double slotWidth = 2 * MM;

            MathFunction phiKsiFun = B20201012Utils.createPhiKsiFun(bigR, innerSmallR, phi0, tiltAngle);
            SoleLayerCct soleLayerCct = B20201012Utils.createSoleLayerCct(bigR, innerSmallR, phiKsiFun, I0,
                    0, endKsi, windingNumber * 360);
            cct.addSoleLayerCct(soleLayerCct);


            MathFunction phiKsiFun1 = B20201012Utils.createPhiKsiFun(bigR, outerSmall, phi0, tiltAngle);
            phiKsiFun1 = phiKsiFun1/*.yAxisSymmetry()*/.move(Vector2.xDirect(Math.PI));
            SoleLayerCct soleLayerCct1 = B20201012Utils.createSoleLayerCct(bigR, outerSmall, phiKsiFun1, -I0,
                    Math.PI, Math.PI + endKsi, windingNumber * 360);
            cct.addSoleLayerCct(soleLayerCct1);

//                final double I,
//                final int totalDisperseNumber,
//                final double slotDepth, // 放线圈的槽的深度 如 8mm
//                final double slotWidth, // 放线圈的槽的宽度 如 2mm
//                final MathFunction phiKsiFun,
//                final double startKsi,
//                final double endKsi,
//                final BipolarToroidalCoordinateSystemPoint2To3 bipolarToroidalCoordinateSystemPoint2To3
            brick8sSet.add(
                    B20201012Utils.createBrick8s(
                            I0, windingNumber * numberPerWinding, slotDepth, slotWidth,
                            phiKsiFun, 0, endKsi,
                            B20201012Utils.createBipolarToroidalCoordinateSystemPoint2To3(bigR, innerSmallR)
                    ),
                    "bicctin"
            );

            brick8sSet.add(
                    B20201012Utils.createBrick8s(
                            -I0, windingNumber * numberPerWinding, slotDepth, slotWidth,
                            phiKsiFun1, Math.PI, Math.PI + endKsi,
                            B20201012Utils.createBipolarToroidalCoordinateSystemPoint2To3(bigR, innerSmallR)
                    ),
                    "bicctout"
            );
        }

//        brick8sSet.clear();

        {
            final double bigR = 0.95;
            final double innerSmallR = inner;
            final double outerSmallR = inner + GAP;
            final double[] bendingAngles = {12.386, 27.344, 27.770};
            BaseUtils.Equal.requireEqual(Arrays.stream(bendingAngles).sum(), 67.5);
            final double[] bendingRadians = BaseUtils.Converter.angleToRadian(bendingAngles);
            final double tiltAngle = 28;
            final double tiltRadian = BaseUtils.Converter.angleToRadian(tiltAngle);
            final int[] windingNumbers = {29, 64, 65};
            final double[] endKsis = BaseUtils.ArrayUtils.multiple(windingNumbers, Math.PI * 2);
            final double I0 = -6500; // 2.355 T
            final double[] phi0 = BaseUtils.ArrayUtils.div(bendingRadians, windingNumbers);
            final double slotDepth = 8 * MM;
            final double slotWidth = 2 * MM;

            MathFunction phiKsiFunInner1 = B20201012Utils.createPhiKsiFun(bigR, innerSmallR, phi0[0], 90, tiltAngle);
            SoleLayerCct soleLayerCctInner1 = B20201012Utils.createSoleLayerCct(bigR, innerSmallR, phiKsiFunInner1, I0,
                    0, endKsis[0], windingNumbers[0] * 360);
            cct.addSoleLayerCct(soleLayerCctInner1);

            MathFunction phiKsiFunInner2 = B20201012Utils.createPhiKsiFun(bigR, innerSmallR, phi0[1], 90, -tiltAngle)
                    .yAxisSymmetry().move(Vector2.create(endKsis[0], bendingRadians[0] + phi0[0]));
            SoleLayerCct soleLayerCctInner2 = B20201012Utils.createSoleLayerCct(bigR, innerSmallR, phiKsiFunInner2, I0,
                    endKsis[0], endKsis[0] - endKsis[1], windingNumbers[1] * 360);
            cct.addSoleLayerCct(soleLayerCctInner2);

            MathFunction phiKsiFunInner3 = B20201012Utils.createPhiKsiFun(bigR, innerSmallR, phi0[2], 90, tiltAngle)
                    .move(Vector2.create(endKsis[0] - endKsis[1],
                            bendingRadians[0] + bendingRadians[1] + phi0[0] + phi0[1]));
            SoleLayerCct soleLayerCctInner3 = B20201012Utils.createSoleLayerCct(bigR, innerSmallR, phiKsiFunInner3, I0,
                    endKsis[0] - endKsis[1], endKsis[0] - endKsis[1] + endKsis[2], windingNumbers[2] * 360);
            cct.addSoleLayerCct(soleLayerCctInner3);

            brick8sSet.add(
                    B20201012Utils.createBrick8s(
                            I0, windingNumbers[0] * numberPerWinding, slotDepth, slotWidth,
                            phiKsiFunInner1, 0, endKsis[0],
                            B20201012Utils.createBipolarToroidalCoordinateSystemPoint2To3(bigR, innerSmallR)
                    ), "agcctin1"
            );

            brick8sSet.add(
                    B20201012Utils.createBrick8s(
                            I0, windingNumbers[1] * numberPerWinding, slotDepth, slotWidth,
                            phiKsiFunInner2, endKsis[0], endKsis[0] - endKsis[1],
                            B20201012Utils.createBipolarToroidalCoordinateSystemPoint2To3(bigR, innerSmallR)
                    ), "agcctin2"
            );

            brick8sSet.add(
                    B20201012Utils.createBrick8s(
                            I0, windingNumbers[2] * numberPerWinding, slotDepth, slotWidth,
                            phiKsiFunInner3,
                            endKsis[0] - endKsis[1],
                            bendingRadians[0] + bendingRadians[1] + phi0[0] + phi0[1],
                            B20201012Utils.createBipolarToroidalCoordinateSystemPoint2To3(bigR, innerSmallR)
                    ), "agcctin3"
            );


            MathFunction phiKsiFunOuter1 = B20201012Utils.createPhiKsiFun(bigR, outerSmallR, phi0[0], 90, tiltAngle)
                    .move(Vector2.xDirect(Math.PI));
            SoleLayerCct soleLayerCctOuter1 = B20201012Utils.createSoleLayerCct(bigR, outerSmallR, phiKsiFunOuter1, I0,
                    Math.PI, Math.PI + endKsis[0], windingNumbers[0] * 360);
            cct.addSoleLayerCct(soleLayerCctOuter1);
            MathFunction phiKsiFunOuter2 = B20201012Utils.createPhiKsiFun(bigR, outerSmallR, phi0[1], 90, -tiltAngle)
                    .yAxisSymmetry().move(Vector2.create(endKsis[0], bendingRadians[0] + phi0[0]))
                    .move(Vector2.xDirect(Math.PI));
            SoleLayerCct soleLayerCctOuter2 = B20201012Utils.createSoleLayerCct(bigR, outerSmallR, phiKsiFunOuter2, I0,
                    endKsis[0] + Math.PI, endKsis[0] - endKsis[1] + Math.PI, windingNumbers[1] * 360);
            cct.addSoleLayerCct(soleLayerCctOuter2);
            MathFunction phiKsiFunOuter3 = B20201012Utils.createPhiKsiFun(bigR, outerSmallR, phi0[2], 90, tiltAngle)
                    .move(Vector2.create(endKsis[0] - endKsis[1],
                            bendingRadians[0] + bendingRadians[1] + phi0[0] + phi0[1]))
                    .move(Vector2.xDirect(Math.PI));
            SoleLayerCct soleLayerCctOuter3 = B20201012Utils.createSoleLayerCct(bigR, outerSmallR, phiKsiFunOuter3, I0,
                    endKsis[0] - endKsis[1] + Math.PI, endKsis[0] - endKsis[1] + endKsis[2] + Math.PI, windingNumbers[2] * 360);
            cct.addSoleLayerCct(soleLayerCctOuter3);

            brick8sSet.add(
                    B20201012Utils.createBrick8s(
                            I0, windingNumbers[0] * numberPerWinding, slotDepth, slotWidth,
                            phiKsiFunOuter1,
                            Math.PI,
                            Math.PI + endKsis[0],
                            B20201012Utils.createBipolarToroidalCoordinateSystemPoint2To3(bigR, outerSmallR)
                    ), "agcctou1"
            );

            brick8sSet.add(
                    B20201012Utils.createBrick8s(
                            I0, windingNumbers[1] * numberPerWinding, slotDepth, slotWidth,
                            phiKsiFunOuter2,
                            endKsis[0] + Math.PI,
                            endKsis[0] - endKsis[1] + Math.PI,
                            B20201012Utils.createBipolarToroidalCoordinateSystemPoint2To3(bigR, outerSmallR)
                    ), "agcctou2"
            );

            brick8sSet.add(
                    B20201012Utils.createBrick8s(
                            I0, windingNumbers[2] * numberPerWinding, slotDepth, slotWidth,
                            phiKsiFunOuter3,
                            endKsis[0] - endKsis[1] + Math.PI,
                            endKsis[0] - endKsis[1] + endKsis[2] + Math.PI,
                            B20201012Utils.createBipolarToroidalCoordinateSystemPoint2To3(bigR, outerSmallR)
                    ), "agcctou3"
            );

        }

        FileOutputStream fileOutputStream = new FileOutputStream("bipoleCoCCT-origin2.txt");
        fileOutputStream.write(brick8sSet.toString().getBytes());
        fileOutputStream.flush();
        fileOutputStream.close();


//        Trajectory trajectory = TrajectoryFactory.setStartingPoint(0.95, -1)
//                .setStraightLine(1, Vector2.yDirect())
//                .addArcLine(0.95, false, 67.5)
//                .addStraitLine(1);

//        List<Point2> bz2 = cct.magnetBzAlongTrajectory(trajectory);
//        List<Point2> g = cct.magnetGradientAlongTrajectoryFast(trajectory, 10 * MM, 10 * MM);

//        Plot2d.plot2(bz2);
//        Plot2d.plot2(g);

//        Plot2d.showThread();

        return brick8sSet;
    }

    @Run(11)
    public Brick8sSet operaSmallRAndGap() throws IOException {
        Brick8sSet brick8sSet = Brick8sSet.empty();
        Cct cct = Cct.getEmptyCct();

        final double GAP = 15 * MM;
        final double inner = 83 * MM;

        {
            final double bigR = 0.95;
            final double innerSmallR = inner + GAP * 2;
            final double outerSmall = inner + GAP * 3;
            final double bendingAngle = 67.5;
            final double bendingRadian = BaseUtils.Converter.angleToRadian(bendingAngle);
            final double tiltAngle = 30;
            final double tiltRadian = BaseUtils.Converter.angleToRadian(tiltAngle);
            final int windingNumber = 201;
            final double endKsi = windingNumber * Math.PI * 2;
            final double I0 = 6530; // 2.355 T
            final double phi0 = bendingRadian / windingNumber;
            final double slotDepth = 8 * MM;
            final double slotWidth = 2 * MM;

            MathFunction phiKsiFun = B20201012Utils.createPhiKsiFun(bigR, innerSmallR, phi0, tiltAngle);
            SoleLayerCct soleLayerCct = B20201012Utils.createSoleLayerCct(bigR, innerSmallR, phiKsiFun, I0,
                    0, endKsi, windingNumber * 360);
            cct.addSoleLayerCct(soleLayerCct);


            MathFunction phiKsiFun1 = B20201012Utils.createPhiKsiFun(bigR, outerSmall, phi0, tiltAngle);
            phiKsiFun1 = phiKsiFun1/*.yAxisSymmetry()*/.move(Vector2.xDirect(Math.PI));
            SoleLayerCct soleLayerCct1 = B20201012Utils.createSoleLayerCct(bigR, outerSmall, phiKsiFun1, -I0,
                    Math.PI, Math.PI + endKsi, windingNumber * 360);
            cct.addSoleLayerCct(soleLayerCct1);

//                final double I,
//                final int totalDisperseNumber,
//                final double slotDepth, // 放线圈的槽的深度 如 8mm
//                final double slotWidth, // 放线圈的槽的宽度 如 2mm
//                final MathFunction phiKsiFun,
//                final double startKsi,
//                final double endKsi,
//                final BipolarToroidalCoordinateSystemPoint2To3 bipolarToroidalCoordinateSystemPoint2To3
            brick8sSet.add(
                    B20201012Utils.createBrick8s(
                            I0, windingNumber * numberPerWinding, slotDepth, slotWidth,
                            phiKsiFun, 0, endKsi,
                            B20201012Utils.createBipolarToroidalCoordinateSystemPoint2To3(bigR, innerSmallR)
                    ),
                    "bicctin"
            );

            brick8sSet.add(
                    B20201012Utils.createBrick8s(
                            -I0, windingNumber * numberPerWinding, slotDepth, slotWidth,
                            phiKsiFun1, Math.PI, Math.PI + endKsi,
                            B20201012Utils.createBipolarToroidalCoordinateSystemPoint2To3(bigR, innerSmallR)
                    ),
                    "bicctout"
            );
        }

//        brick8sSet.clear();

        {
            final double bigR = 0.95;
            final double innerSmallR = inner;
            final double outerSmallR = inner + GAP;
            final double[] bendingAngles = {12.386, 27.344, 27.770};
            BaseUtils.Equal.requireEqual(Arrays.stream(bendingAngles).sum(), 67.5);
            final double[] bendingRadians = BaseUtils.Converter.angleToRadian(bendingAngles);
            final double tiltAngle = 28;
            final double tiltRadian = BaseUtils.Converter.angleToRadian(tiltAngle);
            final int[] windingNumbers = {29, 64, 65};
            final double[] endKsis = BaseUtils.ArrayUtils.multiple(windingNumbers, Math.PI * 2);
            final double I0 = -6500; // 2.355 T
            final double[] phi0 = BaseUtils.ArrayUtils.div(bendingRadians, windingNumbers);
            final double slotDepth = 8 * MM;
            final double slotWidth = 2 * MM;

            MathFunction phiKsiFunInner1 = B20201012Utils.createPhiKsiFun(bigR, innerSmallR, phi0[0], 90, tiltAngle);
            SoleLayerCct soleLayerCctInner1 = B20201012Utils.createSoleLayerCct(bigR, innerSmallR, phiKsiFunInner1, I0,
                    0, endKsis[0], windingNumbers[0] * 360);
            cct.addSoleLayerCct(soleLayerCctInner1);

            MathFunction phiKsiFunInner2 = B20201012Utils.createPhiKsiFun(bigR, innerSmallR, phi0[1], 90, -tiltAngle)
                    .yAxisSymmetry().move(Vector2.create(endKsis[0], bendingRadians[0] + phi0[0]));
            SoleLayerCct soleLayerCctInner2 = B20201012Utils.createSoleLayerCct(bigR, innerSmallR, phiKsiFunInner2, I0,
                    endKsis[0], endKsis[0] - endKsis[1], windingNumbers[1] * 360);
            cct.addSoleLayerCct(soleLayerCctInner2);

            MathFunction phiKsiFunInner3 = B20201012Utils.createPhiKsiFun(bigR, innerSmallR, phi0[2], 90, tiltAngle)
                    .move(Vector2.create(endKsis[0] - endKsis[1],
                            bendingRadians[0] + bendingRadians[1] + phi0[0] + phi0[1]));
            SoleLayerCct soleLayerCctInner3 = B20201012Utils.createSoleLayerCct(bigR, innerSmallR, phiKsiFunInner3, I0,
                    endKsis[0] - endKsis[1], endKsis[0] - endKsis[1] + endKsis[2], windingNumbers[2] * 360);
            cct.addSoleLayerCct(soleLayerCctInner3);

            brick8sSet.add(
                    B20201012Utils.createBrick8s(
                            I0, windingNumbers[0] * numberPerWinding, slotDepth, slotWidth,
                            phiKsiFunInner1, 0, endKsis[0],
                            B20201012Utils.createBipolarToroidalCoordinateSystemPoint2To3(bigR, innerSmallR)
                    ), "agcctin1"
            );

            brick8sSet.add(
                    B20201012Utils.createBrick8s(
                            I0, windingNumbers[1] * numberPerWinding, slotDepth, slotWidth,
                            phiKsiFunInner2, endKsis[0], endKsis[0] - endKsis[1],
                            B20201012Utils.createBipolarToroidalCoordinateSystemPoint2To3(bigR, innerSmallR)
                    ), "agcctin2"
            );

            brick8sSet.add(
                    B20201012Utils.createBrick8s(
                            I0, windingNumbers[2] * numberPerWinding, slotDepth, slotWidth,
                            phiKsiFunInner3,
                            endKsis[0] - endKsis[1],
                            bendingRadians[0] + bendingRadians[1] + phi0[0] + phi0[1],
                            B20201012Utils.createBipolarToroidalCoordinateSystemPoint2To3(bigR, innerSmallR)
                    ), "agcctin3"
            );


            MathFunction phiKsiFunOuter1 = B20201012Utils.createPhiKsiFun(bigR, outerSmallR, phi0[0], 90, tiltAngle)
                    .move(Vector2.xDirect(Math.PI));
            SoleLayerCct soleLayerCctOuter1 = B20201012Utils.createSoleLayerCct(bigR, outerSmallR, phiKsiFunOuter1, I0,
                    Math.PI, Math.PI + endKsis[0], windingNumbers[0] * 360);
            cct.addSoleLayerCct(soleLayerCctOuter1);
            MathFunction phiKsiFunOuter2 = B20201012Utils.createPhiKsiFun(bigR, outerSmallR, phi0[1], 90, -tiltAngle)
                    .yAxisSymmetry().move(Vector2.create(endKsis[0], bendingRadians[0] + phi0[0]))
                    .move(Vector2.xDirect(Math.PI));
            SoleLayerCct soleLayerCctOuter2 = B20201012Utils.createSoleLayerCct(bigR, outerSmallR, phiKsiFunOuter2, I0,
                    endKsis[0] + Math.PI, endKsis[0] - endKsis[1] + Math.PI, windingNumbers[1] * 360);
            cct.addSoleLayerCct(soleLayerCctOuter2);
            MathFunction phiKsiFunOuter3 = B20201012Utils.createPhiKsiFun(bigR, outerSmallR, phi0[2], 90, tiltAngle)
                    .move(Vector2.create(endKsis[0] - endKsis[1],
                            bendingRadians[0] + bendingRadians[1] + phi0[0] + phi0[1]))
                    .move(Vector2.xDirect(Math.PI));
            SoleLayerCct soleLayerCctOuter3 = B20201012Utils.createSoleLayerCct(bigR, outerSmallR, phiKsiFunOuter3, I0,
                    endKsis[0] - endKsis[1] + Math.PI, endKsis[0] - endKsis[1] + endKsis[2] + Math.PI, windingNumbers[2] * 360);
            cct.addSoleLayerCct(soleLayerCctOuter3);

            brick8sSet.add(
                    B20201012Utils.createBrick8s(
                            I0, windingNumbers[0] * numberPerWinding, slotDepth, slotWidth,
                            phiKsiFunOuter1,
                            Math.PI,
                            Math.PI + endKsis[0],
                            B20201012Utils.createBipolarToroidalCoordinateSystemPoint2To3(bigR, outerSmallR)
                    ), "agcctou1"
            );

            brick8sSet.add(
                    B20201012Utils.createBrick8s(
                            I0, windingNumbers[1] * numberPerWinding, slotDepth, slotWidth,
                            phiKsiFunOuter2,
                            endKsis[0] + Math.PI,
                            endKsis[0] - endKsis[1] + Math.PI,
                            B20201012Utils.createBipolarToroidalCoordinateSystemPoint2To3(bigR, outerSmallR)
                    ), "agcctou2"
            );

            brick8sSet.add(
                    B20201012Utils.createBrick8s(
                            I0, windingNumbers[2] * numberPerWinding, slotDepth, slotWidth,
                            phiKsiFunOuter3,
                            endKsis[0] - endKsis[1] + Math.PI,
                            endKsis[0] - endKsis[1] + endKsis[2] + Math.PI,
                            B20201012Utils.createBipolarToroidalCoordinateSystemPoint2To3(bigR, outerSmallR)
                    ), "agcctou3"
            );

        }

        FileOutputStream fileOutputStream = new FileOutputStream("bipoleCoCCT-operaSmallRAndGap.txt");
        fileOutputStream.write(brick8sSet.toString().getBytes());
        fileOutputStream.flush();
        fileOutputStream.close();


//        Trajectory trajectory = TrajectoryFactory.setStartingPoint(0.95, -1)
//                .setStraightLine(1, Vector2.yDirect())
//                .addArcLine(0.95, false, 67.5)
//                .addStraitLine(1);

//        List<Point2> bz2 = cct.magnetBzAlongTrajectory(trajectory);
//        List<Point2> g = cct.magnetGradientAlongTrajectoryFast(trajectory, 10 * MM, 10 * MM);

//        Plot2d.plot2(bz2);
//        Plot2d.plot2(g);

//        Plot2d.showThread();

        return brick8sSet;
    }

    @Run(12)
    public Brick8sSet operaSlot() throws IOException {
        Brick8sSet brick8sSet = Brick8sSet.empty();
        Cct cct = Cct.getEmptyCct();

        final double GAP = 15 * MM;
        final double inner = 83 * MM;

        {
            final double bigR = 0.95;
            final double innerSmallR = inner + GAP * 2;
            final double outerSmall = inner + GAP * 3;
            final double bendingAngle = 67.5;
            final double bendingRadian = BaseUtils.Converter.angleToRadian(bendingAngle);
            final double tiltAngle = 30;
            final double tiltRadian = BaseUtils.Converter.angleToRadian(tiltAngle);
            final int windingNumber = 201;
            final double endKsi = windingNumber * Math.PI * 2;
            final double I0 = 6530; // 2.355 T
            final double phi0 = bendingRadian / windingNumber;
            final double slotDepth = 12 * MM;
            final double slotWidth = 2 * MM;

            MathFunction phiKsiFun = B20201012Utils.createPhiKsiFun(bigR, innerSmallR, phi0, tiltAngle);
            SoleLayerCct soleLayerCct = B20201012Utils.createSoleLayerCct(bigR, innerSmallR, phiKsiFun, I0,
                    0, endKsi, windingNumber * 360);
            cct.addSoleLayerCct(soleLayerCct);


            MathFunction phiKsiFun1 = B20201012Utils.createPhiKsiFun(bigR, outerSmall, phi0, tiltAngle);
            phiKsiFun1 = phiKsiFun1/*.yAxisSymmetry()*/.move(Vector2.xDirect(Math.PI));
            SoleLayerCct soleLayerCct1 = B20201012Utils.createSoleLayerCct(bigR, outerSmall, phiKsiFun1, -I0,
                    Math.PI, Math.PI + endKsi, windingNumber * 360);
            cct.addSoleLayerCct(soleLayerCct1);

//                final double I,
//                final int totalDisperseNumber,
//                final double slotDepth, // 放线圈的槽的深度 如 8mm
//                final double slotWidth, // 放线圈的槽的宽度 如 2mm
//                final MathFunction phiKsiFun,
//                final double startKsi,
//                final double endKsi,
//                final BipolarToroidalCoordinateSystemPoint2To3 bipolarToroidalCoordinateSystemPoint2To3
            brick8sSet.add(
                    B20201012Utils.createBrick8s(
                            I0, windingNumber * numberPerWinding, slotDepth, slotWidth,
                            phiKsiFun, 0, endKsi,
                            B20201012Utils.createBipolarToroidalCoordinateSystemPoint2To3(bigR, innerSmallR)
                    ),
                    "bicctin"
            );

            brick8sSet.add(
                    B20201012Utils.createBrick8s(
                            -I0, windingNumber * numberPerWinding, slotDepth, slotWidth,
                            phiKsiFun1, Math.PI, Math.PI + endKsi,
                            B20201012Utils.createBipolarToroidalCoordinateSystemPoint2To3(bigR, innerSmallR)
                    ),
                    "bicctout"
            );
        }

//        brick8sSet.clear();

        {
            final double bigR = 0.95;
            final double innerSmallR = inner;
            final double outerSmallR = inner + GAP;
            final double[] bendingAngles = {12.386, 27.344, 27.770};
            BaseUtils.Equal.requireEqual(Arrays.stream(bendingAngles).sum(), 67.5);
            final double[] bendingRadians = BaseUtils.Converter.angleToRadian(bendingAngles);
            final double tiltAngle = 28;
            final double tiltRadian = BaseUtils.Converter.angleToRadian(tiltAngle);
            final int[] windingNumbers = {29, 64, 65};
            final double[] endKsis = BaseUtils.ArrayUtils.multiple(windingNumbers, Math.PI * 2);
            final double I0 = -6500; // 2.355 T
            final double[] phi0 = BaseUtils.ArrayUtils.div(bendingRadians, windingNumbers);
            final double slotDepth = 12 * MM;
            final double slotWidth = 2 * MM;

            MathFunction phiKsiFunInner1 = B20201012Utils.createPhiKsiFun(bigR, innerSmallR, phi0[0], 90, tiltAngle);
            SoleLayerCct soleLayerCctInner1 = B20201012Utils.createSoleLayerCct(bigR, innerSmallR, phiKsiFunInner1, I0,
                    0, endKsis[0], windingNumbers[0] * 360);
            cct.addSoleLayerCct(soleLayerCctInner1);

            MathFunction phiKsiFunInner2 = B20201012Utils.createPhiKsiFun(bigR, innerSmallR, phi0[1], 90, -tiltAngle)
                    .yAxisSymmetry().move(Vector2.create(endKsis[0], bendingRadians[0] + phi0[0]));
            SoleLayerCct soleLayerCctInner2 = B20201012Utils.createSoleLayerCct(bigR, innerSmallR, phiKsiFunInner2, I0,
                    endKsis[0], endKsis[0] - endKsis[1], windingNumbers[1] * 360);
            cct.addSoleLayerCct(soleLayerCctInner2);

            MathFunction phiKsiFunInner3 = B20201012Utils.createPhiKsiFun(bigR, innerSmallR, phi0[2], 90, tiltAngle)
                    .move(Vector2.create(endKsis[0] - endKsis[1],
                            bendingRadians[0] + bendingRadians[1] + phi0[0] + phi0[1]));
            SoleLayerCct soleLayerCctInner3 = B20201012Utils.createSoleLayerCct(bigR, innerSmallR, phiKsiFunInner3, I0,
                    endKsis[0] - endKsis[1], endKsis[0] - endKsis[1] + endKsis[2], windingNumbers[2] * 360);
            cct.addSoleLayerCct(soleLayerCctInner3);

            brick8sSet.add(
                    B20201012Utils.createBrick8s(
                            I0, windingNumbers[0] * numberPerWinding, slotDepth, slotWidth,
                            phiKsiFunInner1, 0, endKsis[0],
                            B20201012Utils.createBipolarToroidalCoordinateSystemPoint2To3(bigR, innerSmallR)
                    ), "agcctin1"
            );

            brick8sSet.add(
                    B20201012Utils.createBrick8s(
                            I0, windingNumbers[1] * numberPerWinding, slotDepth, slotWidth,
                            phiKsiFunInner2, endKsis[0], endKsis[0] - endKsis[1],
                            B20201012Utils.createBipolarToroidalCoordinateSystemPoint2To3(bigR, innerSmallR)
                    ), "agcctin2"
            );

            brick8sSet.add(
                    B20201012Utils.createBrick8s(
                            I0, windingNumbers[2] * numberPerWinding, slotDepth, slotWidth,
                            phiKsiFunInner3,
                            endKsis[0] - endKsis[1],
                            bendingRadians[0] + bendingRadians[1] + phi0[0] + phi0[1],
                            B20201012Utils.createBipolarToroidalCoordinateSystemPoint2To3(bigR, innerSmallR)
                    ), "agcctin3"
            );


            MathFunction phiKsiFunOuter1 = B20201012Utils.createPhiKsiFun(bigR, outerSmallR, phi0[0], 90, tiltAngle)
                    .move(Vector2.xDirect(Math.PI));
            SoleLayerCct soleLayerCctOuter1 = B20201012Utils.createSoleLayerCct(bigR, outerSmallR, phiKsiFunOuter1, I0,
                    Math.PI, Math.PI + endKsis[0], windingNumbers[0] * 360);
            cct.addSoleLayerCct(soleLayerCctOuter1);
            MathFunction phiKsiFunOuter2 = B20201012Utils.createPhiKsiFun(bigR, outerSmallR, phi0[1], 90, -tiltAngle)
                    .yAxisSymmetry().move(Vector2.create(endKsis[0], bendingRadians[0] + phi0[0]))
                    .move(Vector2.xDirect(Math.PI));
            SoleLayerCct soleLayerCctOuter2 = B20201012Utils.createSoleLayerCct(bigR, outerSmallR, phiKsiFunOuter2, I0,
                    endKsis[0] + Math.PI, endKsis[0] - endKsis[1] + Math.PI, windingNumbers[1] * 360);
            cct.addSoleLayerCct(soleLayerCctOuter2);
            MathFunction phiKsiFunOuter3 = B20201012Utils.createPhiKsiFun(bigR, outerSmallR, phi0[2], 90, tiltAngle)
                    .move(Vector2.create(endKsis[0] - endKsis[1],
                            bendingRadians[0] + bendingRadians[1] + phi0[0] + phi0[1]))
                    .move(Vector2.xDirect(Math.PI));
            SoleLayerCct soleLayerCctOuter3 = B20201012Utils.createSoleLayerCct(bigR, outerSmallR, phiKsiFunOuter3, I0,
                    endKsis[0] - endKsis[1] + Math.PI, endKsis[0] - endKsis[1] + endKsis[2] + Math.PI, windingNumbers[2] * 360);
            cct.addSoleLayerCct(soleLayerCctOuter3);

            brick8sSet.add(
                    B20201012Utils.createBrick8s(
                            I0, windingNumbers[0] * numberPerWinding, slotDepth, slotWidth,
                            phiKsiFunOuter1,
                            Math.PI,
                            Math.PI + endKsis[0],
                            B20201012Utils.createBipolarToroidalCoordinateSystemPoint2To3(bigR, outerSmallR)
                    ), "agcctou1"
            );

            brick8sSet.add(
                    B20201012Utils.createBrick8s(
                            I0, windingNumbers[1] * numberPerWinding, slotDepth, slotWidth,
                            phiKsiFunOuter2,
                            endKsis[0] + Math.PI,
                            endKsis[0] - endKsis[1] + Math.PI,
                            B20201012Utils.createBipolarToroidalCoordinateSystemPoint2To3(bigR, outerSmallR)
                    ), "agcctou2"
            );

            brick8sSet.add(
                    B20201012Utils.createBrick8s(
                            I0, windingNumbers[2] * numberPerWinding, slotDepth, slotWidth,
                            phiKsiFunOuter3,
                            endKsis[0] - endKsis[1] + Math.PI,
                            endKsis[0] - endKsis[1] + endKsis[2] + Math.PI,
                            B20201012Utils.createBipolarToroidalCoordinateSystemPoint2To3(bigR, outerSmallR)
                    ), "agcctou3"
            );

        }

        FileOutputStream fileOutputStream = new FileOutputStream("bipoleCoCCT-operaSlot.txt");
        fileOutputStream.write(brick8sSet.toString().getBytes());
        fileOutputStream.flush();
        fileOutputStream.close();


//        Trajectory trajectory = TrajectoryFactory.setStartingPoint(0.95, -1)
//                .setStraightLine(1, Vector2.yDirect())
//                .addArcLine(0.95, false, 67.5)
//                .addStraitLine(1);

//        List<Point2> bz2 = cct.magnetBzAlongTrajectory(trajectory);
//        List<Point2> g = cct.magnetGradientAlongTrajectoryFast(trajectory, 10 * MM, 10 * MM);

//        Plot2d.plot2(bz2);
//        Plot2d.plot2(g);

//        Plot2d.showThread();

        return brick8sSet;
    }

    @Run(13)
    public Brick8sSet operaMagnet() throws IOException {
        Brick8sSet brick8sSet = Brick8sSet.empty();
        Cct cct = Cct.getEmptyCct();

        final double GAP = 15 * MM;
        final double inner = 83 * MM;

        {
            final double bigR = 0.95;
            final double innerSmallR = inner + GAP * 2;
            final double outerSmall = inner + GAP * 3;
            final double bendingAngle = 67.5;
            final double bendingRadian = BaseUtils.Converter.angleToRadian(bendingAngle);
            final double tiltAngle = 30;
            final double tiltRadian = BaseUtils.Converter.angleToRadian(tiltAngle);
            final int windingNumber = 201;
            final double endKsi = windingNumber * Math.PI * 2;
            final double I0 = 6530 / 2.558 * 2.355; // 2.355 T
            final double phi0 = bendingRadian / windingNumber;
            final double slotDepth = 12 * MM;
            final double slotWidth = 2 * MM;

            MathFunction phiKsiFun = B20201012Utils.createPhiKsiFun(bigR, innerSmallR, phi0, tiltAngle);
            SoleLayerCct soleLayerCct = B20201012Utils.createSoleLayerCct(bigR, innerSmallR, phiKsiFun, I0,
                    0, endKsi, windingNumber * 360);
            cct.addSoleLayerCct(soleLayerCct);


            MathFunction phiKsiFun1 = B20201012Utils.createPhiKsiFun(bigR, outerSmall, phi0, tiltAngle);
            phiKsiFun1 = phiKsiFun1/*.yAxisSymmetry()*/.move(Vector2.xDirect(Math.PI));
            SoleLayerCct soleLayerCct1 = B20201012Utils.createSoleLayerCct(bigR, outerSmall, phiKsiFun1, -I0,
                    Math.PI, Math.PI + endKsi, windingNumber * 360);
            cct.addSoleLayerCct(soleLayerCct1);

//                final double I,
//                final int totalDisperseNumber,
//                final double slotDepth, // 放线圈的槽的深度 如 8mm
//                final double slotWidth, // 放线圈的槽的宽度 如 2mm
//                final MathFunction phiKsiFun,
//                final double startKsi,
//                final double endKsi,
//                final BipolarToroidalCoordinateSystemPoint2To3 bipolarToroidalCoordinateSystemPoint2To3
            brick8sSet.add(
                    B20201012Utils.createBrick8s(
                            I0, windingNumber * numberPerWinding, slotDepth, slotWidth,
                            phiKsiFun, 0, endKsi,
                            B20201012Utils.createBipolarToroidalCoordinateSystemPoint2To3(bigR, innerSmallR)
                    ),
                    "bicctin"
            );

            brick8sSet.add(
                    B20201012Utils.createBrick8s(
                            -I0, windingNumber * numberPerWinding, slotDepth, slotWidth,
                            phiKsiFun1, Math.PI, Math.PI + endKsi,
                            B20201012Utils.createBipolarToroidalCoordinateSystemPoint2To3(bigR, innerSmallR)
                    ),
                    "bicctout"
            );
        }

//        brick8sSet.clear();

        {
            final double bigR = 0.95;
            final double innerSmallR = inner;
            final double outerSmallR = inner + GAP;
            final double[] bendingAngles = {12.386, 27.344, 27.770};
            BaseUtils.Equal.requireEqual(Arrays.stream(bendingAngles).sum(), 67.5);
            final double[] bendingRadians = BaseUtils.Converter.angleToRadian(bendingAngles);
            final double tiltAngle = 28;
            final double tiltRadian = BaseUtils.Converter.angleToRadian(tiltAngle);
            final int[] windingNumbers = {29, 64, 65};
            final double[] endKsis = BaseUtils.ArrayUtils.multiple(windingNumbers, Math.PI * 2);
            final double I0 = -6500. / 19 * 17; // 2.355 T
            final double[] phi0 = BaseUtils.ArrayUtils.div(bendingRadians, windingNumbers);
            final double slotDepth = 12 * MM;
            final double slotWidth = 2 * MM;

            MathFunction phiKsiFunInner1 = B20201012Utils.createPhiKsiFun(bigR, innerSmallR, phi0[0], 90, tiltAngle);
            SoleLayerCct soleLayerCctInner1 = B20201012Utils.createSoleLayerCct(bigR, innerSmallR, phiKsiFunInner1, I0,
                    0, endKsis[0], windingNumbers[0] * 360);
            cct.addSoleLayerCct(soleLayerCctInner1);

            MathFunction phiKsiFunInner2 = B20201012Utils.createPhiKsiFun(bigR, innerSmallR, phi0[1], 90, -tiltAngle)
                    .yAxisSymmetry().move(Vector2.create(endKsis[0], bendingRadians[0] + phi0[0]));
            SoleLayerCct soleLayerCctInner2 = B20201012Utils.createSoleLayerCct(bigR, innerSmallR, phiKsiFunInner2, I0,
                    endKsis[0], endKsis[0] - endKsis[1], windingNumbers[1] * 360);
            cct.addSoleLayerCct(soleLayerCctInner2);

            MathFunction phiKsiFunInner3 = B20201012Utils.createPhiKsiFun(bigR, innerSmallR, phi0[2], 90, tiltAngle)
                    .move(Vector2.create(endKsis[0] - endKsis[1],
                            bendingRadians[0] + bendingRadians[1] + phi0[0] + phi0[1]));
            SoleLayerCct soleLayerCctInner3 = B20201012Utils.createSoleLayerCct(bigR, innerSmallR, phiKsiFunInner3, I0,
                    endKsis[0] - endKsis[1], endKsis[0] - endKsis[1] + endKsis[2], windingNumbers[2] * 360);
            cct.addSoleLayerCct(soleLayerCctInner3);

            brick8sSet.add(
                    B20201012Utils.createBrick8s(
                            I0, windingNumbers[0] * numberPerWinding, slotDepth, slotWidth,
                            phiKsiFunInner1, 0, endKsis[0],
                            B20201012Utils.createBipolarToroidalCoordinateSystemPoint2To3(bigR, innerSmallR)
                    ), "agcctin1"
            );

            brick8sSet.add(
                    B20201012Utils.createBrick8s(
                            I0, windingNumbers[1] * numberPerWinding, slotDepth, slotWidth,
                            phiKsiFunInner2, endKsis[0], endKsis[0] - endKsis[1],
                            B20201012Utils.createBipolarToroidalCoordinateSystemPoint2To3(bigR, innerSmallR)
                    ), "agcctin2"
            );

            brick8sSet.add(
                    B20201012Utils.createBrick8s(
                            I0, windingNumbers[2] * numberPerWinding, slotDepth, slotWidth,
                            phiKsiFunInner3,
                            endKsis[0] - endKsis[1],
                            bendingRadians[0] + bendingRadians[1] + phi0[0] + phi0[1],
                            B20201012Utils.createBipolarToroidalCoordinateSystemPoint2To3(bigR, innerSmallR)
                    ), "agcctin3"
            );


            MathFunction phiKsiFunOuter1 = B20201012Utils.createPhiKsiFun(bigR, outerSmallR, phi0[0], 90, tiltAngle)
                    .move(Vector2.xDirect(Math.PI));
            SoleLayerCct soleLayerCctOuter1 = B20201012Utils.createSoleLayerCct(bigR, outerSmallR, phiKsiFunOuter1, I0,
                    Math.PI, Math.PI + endKsis[0], windingNumbers[0] * 360);
            cct.addSoleLayerCct(soleLayerCctOuter1);
            MathFunction phiKsiFunOuter2 = B20201012Utils.createPhiKsiFun(bigR, outerSmallR, phi0[1], 90, -tiltAngle)
                    .yAxisSymmetry().move(Vector2.create(endKsis[0], bendingRadians[0] + phi0[0]))
                    .move(Vector2.xDirect(Math.PI));
            SoleLayerCct soleLayerCctOuter2 = B20201012Utils.createSoleLayerCct(bigR, outerSmallR, phiKsiFunOuter2, I0,
                    endKsis[0] + Math.PI, endKsis[0] - endKsis[1] + Math.PI, windingNumbers[1] * 360);
            cct.addSoleLayerCct(soleLayerCctOuter2);
            MathFunction phiKsiFunOuter3 = B20201012Utils.createPhiKsiFun(bigR, outerSmallR, phi0[2], 90, tiltAngle)
                    .move(Vector2.create(endKsis[0] - endKsis[1],
                            bendingRadians[0] + bendingRadians[1] + phi0[0] + phi0[1]))
                    .move(Vector2.xDirect(Math.PI));
            SoleLayerCct soleLayerCctOuter3 = B20201012Utils.createSoleLayerCct(bigR, outerSmallR, phiKsiFunOuter3, I0,
                    endKsis[0] - endKsis[1] + Math.PI, endKsis[0] - endKsis[1] + endKsis[2] + Math.PI, windingNumbers[2] * 360);
            cct.addSoleLayerCct(soleLayerCctOuter3);

            brick8sSet.add(
                    B20201012Utils.createBrick8s(
                            I0, windingNumbers[0] * numberPerWinding, slotDepth, slotWidth,
                            phiKsiFunOuter1,
                            Math.PI,
                            Math.PI + endKsis[0],
                            B20201012Utils.createBipolarToroidalCoordinateSystemPoint2To3(bigR, outerSmallR)
                    ), "agcctou1"
            );

            brick8sSet.add(
                    B20201012Utils.createBrick8s(
                            I0, windingNumbers[1] * numberPerWinding, slotDepth, slotWidth,
                            phiKsiFunOuter2,
                            endKsis[0] + Math.PI,
                            endKsis[0] - endKsis[1] + Math.PI,
                            B20201012Utils.createBipolarToroidalCoordinateSystemPoint2To3(bigR, outerSmallR)
                    ), "agcctou2"
            );

            brick8sSet.add(
                    B20201012Utils.createBrick8s(
                            I0, windingNumbers[2] * numberPerWinding, slotDepth, slotWidth,
                            phiKsiFunOuter3,
                            endKsis[0] - endKsis[1] + Math.PI,
                            endKsis[0] - endKsis[1] + endKsis[2] + Math.PI,
                            B20201012Utils.createBipolarToroidalCoordinateSystemPoint2To3(bigR, outerSmallR)
                    ), "agcctou3"
            );

        }

        FileOutputStream fileOutputStream = new FileOutputStream("bipoleCoCCT-operaMagnet.txt");
        fileOutputStream.write(brick8sSet.toString().getBytes());
        fileOutputStream.flush();
        fileOutputStream.close();


//        Trajectory trajectory = TrajectoryFactory.setStartingPoint(0.95, -1)
//                .setStraightLine(1, Vector2.yDirect())
//                .addArcLine(0.95, false, 67.5)
//                .addStraitLine(1);

//        List<Point2> bz2 = cct.magnetBzAlongTrajectory(trajectory);
//        List<Point2> g = cct.magnetGradientAlongTrajectoryFast(trajectory, 10 * MM, 10 * MM);

//        Plot2d.plot2(bz2);
//        Plot2d.plot2(g);

//        Plot2d.showThread();

        return brick8sSet;
    }

    @Run(14)
    public Brick8sSet operaTiltAngle() throws IOException {
        Brick8sSet brick8sSet = Brick8sSet.empty();
        Cct cct = Cct.getEmptyCct();

        final double GAP = 15 * MM;
        final double inner = 83 * MM;

        {
            final double bigR = 0.95;
            final double innerSmallR = inner + GAP * 2;
            final double outerSmall = inner + GAP * 3;
            final double bendingAngle = 67.5;
            final double bendingRadian = BaseUtils.Converter.angleToRadian(bendingAngle);
            final double tiltAngle = 30;
            final double tiltRadian = BaseUtils.Converter.angleToRadian(tiltAngle);
            final int windingNumber = 201;
            final double endKsi = windingNumber * Math.PI * 2;
            final double I0 = 6530 / 2.558 * 2.355; // 2.355 T
            final double phi0 = bendingRadian / windingNumber;
            final double slotDepth = 12 * MM;
            final double slotWidth = 2 * MM;

            MathFunction phiKsiFun = B20201012Utils.createPhiKsiFun(bigR, innerSmallR, phi0, tiltAngle);
            SoleLayerCct soleLayerCct = B20201012Utils.createSoleLayerCct(bigR, innerSmallR, phiKsiFun, I0,
                    0, endKsi, windingNumber * 360);
            cct.addSoleLayerCct(soleLayerCct);


            MathFunction phiKsiFun1 = B20201012Utils.createPhiKsiFun(bigR, outerSmall, phi0, tiltAngle);
            phiKsiFun1 = phiKsiFun1/*.yAxisSymmetry()*/.move(Vector2.xDirect(Math.PI));
            SoleLayerCct soleLayerCct1 = B20201012Utils.createSoleLayerCct(bigR, outerSmall, phiKsiFun1, -I0,
                    Math.PI, Math.PI + endKsi, windingNumber * 360);
            cct.addSoleLayerCct(soleLayerCct1);

//                final double I,
//                final int totalDisperseNumber,
//                final double slotDepth, // 放线圈的槽的深度 如 8mm
//                final double slotWidth, // 放线圈的槽的宽度 如 2mm
//                final MathFunction phiKsiFun,
//                final double startKsi,
//                final double endKsi,
//                final BipolarToroidalCoordinateSystemPoint2To3 bipolarToroidalCoordinateSystemPoint2To3
            brick8sSet.add(
                    B20201012Utils.createBrick8s(
                            I0, windingNumber * numberPerWinding, slotDepth, slotWidth,
                            phiKsiFun, 0, endKsi,
                            B20201012Utils.createBipolarToroidalCoordinateSystemPoint2To3(bigR, innerSmallR)
                    ),
                    "bicctin"
            );

            brick8sSet.add(
                    B20201012Utils.createBrick8s(
                            -I0, windingNumber * numberPerWinding, slotDepth, slotWidth,
                            phiKsiFun1, Math.PI, Math.PI + endKsi,
                            B20201012Utils.createBipolarToroidalCoordinateSystemPoint2To3(bigR, innerSmallR)
                    ),
                    "bicctout"
            );
        }

//        brick8sSet.clear();

        {
            final double bigR = 0.95;
            final double innerSmallR = inner;
            final double outerSmallR = inner + GAP;
            final double[] bendingAngles = {12.386, 27.344, 27.770};
            BaseUtils.Equal.requireEqual(Arrays.stream(bendingAngles).sum(), 67.5);
            final double[] bendingRadians = BaseUtils.Converter.angleToRadian(bendingAngles);
            final double tiltAngle = 30;
            final double tiltRadian = BaseUtils.Converter.angleToRadian(tiltAngle);
            final int[] windingNumbers = {29, 64, 65};
            final double[] endKsis = BaseUtils.ArrayUtils.multiple(windingNumbers, Math.PI * 2);
            final double I0 = -6500. / 19 * 17 / 30 *28; // 2.355 T
            final double[] phi0 = BaseUtils.ArrayUtils.div(bendingRadians, windingNumbers);
            final double slotDepth = 12 * MM;
            final double slotWidth = 2 * MM;

            MathFunction phiKsiFunInner1 = B20201012Utils.createPhiKsiFun(bigR, innerSmallR, phi0[0], 90, tiltAngle);
            SoleLayerCct soleLayerCctInner1 = B20201012Utils.createSoleLayerCct(bigR, innerSmallR, phiKsiFunInner1, I0,
                    0, endKsis[0], windingNumbers[0] * 360);
            cct.addSoleLayerCct(soleLayerCctInner1);

            MathFunction phiKsiFunInner2 = B20201012Utils.createPhiKsiFun(bigR, innerSmallR, phi0[1], 90, -tiltAngle)
                    .yAxisSymmetry().move(Vector2.create(endKsis[0], bendingRadians[0] + phi0[0]));
            SoleLayerCct soleLayerCctInner2 = B20201012Utils.createSoleLayerCct(bigR, innerSmallR, phiKsiFunInner2, I0,
                    endKsis[0], endKsis[0] - endKsis[1], windingNumbers[1] * 360);
            cct.addSoleLayerCct(soleLayerCctInner2);

            MathFunction phiKsiFunInner3 = B20201012Utils.createPhiKsiFun(bigR, innerSmallR, phi0[2], 90, tiltAngle)
                    .move(Vector2.create(endKsis[0] - endKsis[1],
                            bendingRadians[0] + bendingRadians[1] + phi0[0] + phi0[1]));
            SoleLayerCct soleLayerCctInner3 = B20201012Utils.createSoleLayerCct(bigR, innerSmallR, phiKsiFunInner3, I0,
                    endKsis[0] - endKsis[1], endKsis[0] - endKsis[1] + endKsis[2], windingNumbers[2] * 360);
            cct.addSoleLayerCct(soleLayerCctInner3);

            brick8sSet.add(
                    B20201012Utils.createBrick8s(
                            I0, windingNumbers[0] * numberPerWinding, slotDepth, slotWidth,
                            phiKsiFunInner1, 0, endKsis[0],
                            B20201012Utils.createBipolarToroidalCoordinateSystemPoint2To3(bigR, innerSmallR)
                    ), "agcctin1"
            );

            brick8sSet.add(
                    B20201012Utils.createBrick8s(
                            I0, windingNumbers[1] * numberPerWinding, slotDepth, slotWidth,
                            phiKsiFunInner2, endKsis[0], endKsis[0] - endKsis[1],
                            B20201012Utils.createBipolarToroidalCoordinateSystemPoint2To3(bigR, innerSmallR)
                    ), "agcctin2"
            );

            brick8sSet.add(
                    B20201012Utils.createBrick8s(
                            I0, windingNumbers[2] * numberPerWinding, slotDepth, slotWidth,
                            phiKsiFunInner3,
                            endKsis[0] - endKsis[1],
                            bendingRadians[0] + bendingRadians[1] + phi0[0] + phi0[1],
                            B20201012Utils.createBipolarToroidalCoordinateSystemPoint2To3(bigR, innerSmallR)
                    ), "agcctin3"
            );


            MathFunction phiKsiFunOuter1 = B20201012Utils.createPhiKsiFun(bigR, outerSmallR, phi0[0], 90, tiltAngle)
                    .move(Vector2.xDirect(Math.PI));
            SoleLayerCct soleLayerCctOuter1 = B20201012Utils.createSoleLayerCct(bigR, outerSmallR, phiKsiFunOuter1, I0,
                    Math.PI, Math.PI + endKsis[0], windingNumbers[0] * 360);
            cct.addSoleLayerCct(soleLayerCctOuter1);
            MathFunction phiKsiFunOuter2 = B20201012Utils.createPhiKsiFun(bigR, outerSmallR, phi0[1], 90, -tiltAngle)
                    .yAxisSymmetry().move(Vector2.create(endKsis[0], bendingRadians[0] + phi0[0]))
                    .move(Vector2.xDirect(Math.PI));
            SoleLayerCct soleLayerCctOuter2 = B20201012Utils.createSoleLayerCct(bigR, outerSmallR, phiKsiFunOuter2, I0,
                    endKsis[0] + Math.PI, endKsis[0] - endKsis[1] + Math.PI, windingNumbers[1] * 360);
            cct.addSoleLayerCct(soleLayerCctOuter2);
            MathFunction phiKsiFunOuter3 = B20201012Utils.createPhiKsiFun(bigR, outerSmallR, phi0[2], 90, tiltAngle)
                    .move(Vector2.create(endKsis[0] - endKsis[1],
                            bendingRadians[0] + bendingRadians[1] + phi0[0] + phi0[1]))
                    .move(Vector2.xDirect(Math.PI));
            SoleLayerCct soleLayerCctOuter3 = B20201012Utils.createSoleLayerCct(bigR, outerSmallR, phiKsiFunOuter3, I0,
                    endKsis[0] - endKsis[1] + Math.PI, endKsis[0] - endKsis[1] + endKsis[2] + Math.PI, windingNumbers[2] * 360);
            cct.addSoleLayerCct(soleLayerCctOuter3);

            brick8sSet.add(
                    B20201012Utils.createBrick8s(
                            I0, windingNumbers[0] * numberPerWinding, slotDepth, slotWidth,
                            phiKsiFunOuter1,
                            Math.PI,
                            Math.PI + endKsis[0],
                            B20201012Utils.createBipolarToroidalCoordinateSystemPoint2To3(bigR, outerSmallR)
                    ), "agcctou1"
            );

            brick8sSet.add(
                    B20201012Utils.createBrick8s(
                            I0, windingNumbers[1] * numberPerWinding, slotDepth, slotWidth,
                            phiKsiFunOuter2,
                            endKsis[0] + Math.PI,
                            endKsis[0] - endKsis[1] + Math.PI,
                            B20201012Utils.createBipolarToroidalCoordinateSystemPoint2To3(bigR, outerSmallR)
                    ), "agcctou2"
            );

            brick8sSet.add(
                    B20201012Utils.createBrick8s(
                            I0, windingNumbers[2] * numberPerWinding, slotDepth, slotWidth,
                            phiKsiFunOuter3,
                            endKsis[0] - endKsis[1] + Math.PI,
                            endKsis[0] - endKsis[1] + endKsis[2] + Math.PI,
                            B20201012Utils.createBipolarToroidalCoordinateSystemPoint2To3(bigR, outerSmallR)
                    ), "agcctou3"
            );

        }

        FileOutputStream fileOutputStream = new FileOutputStream("bipoleCoCCT-operaTiltAngle.txt");
        fileOutputStream.write(brick8sSet.toString().getBytes());
        fileOutputStream.flush();
        fileOutputStream.close();


//        Trajectory trajectory = TrajectoryFactory.setStartingPoint(0.95, -1)
//                .setStraightLine(1, Vector2.yDirect())
//                .addArcLine(0.95, false, 67.5)
//                .addStraitLine(1);

//        List<Point2> bz2 = cct.magnetBzAlongTrajectory(trajectory);
//        List<Point2> g = cct.magnetGradientAlongTrajectoryFast(trajectory, 10 * MM, 10 * MM);

//        Plot2d.plot2(bz2);
//        Plot2d.plot2(g);

//        Plot2d.showThread();

        return brick8sSet;
    }

    @Run(15) // 和 B20201012双极点坐标系新机架设计_最终结果 一致 略
    public Brick8sSet operaWindingNumber() throws IOException {
        Trajectory trajectory = TrajectoryFactory.setStartingPoint(0.95, -1)
                .setStraightLine(1, Vector2.yDirect())
                .addArcLine(0.95, false, 67.5)
                .addStraitLine(1);


        Brick8sSet brick8sSet = Brick8sSet.empty();
        Cct cct = Cct.getEmptyCct();

        final double GAP = 15 * MM;
        final double inner = 83 * MM;

        {
            final double bigR = 0.95;
            final double innerSmallR = inner + GAP * 2; // 83+30
            final double outerSmall = inner + GAP * 3; // 83+45
            final double bendingAngle = 67.5;
            final double bendingRadian = BaseUtils.Converter.angleToRadian(bendingAngle);
            final double tiltAngle = 30;
            final double tiltRadian = BaseUtils.Converter.angleToRadian(tiltAngle);
            final int windingNumber = 201;
            final double endKsi = windingNumber * Math.PI * 2;
            final double I0 = 6530 / 2.558 * 2.355; // 2.355 T
            final double phi0 = bendingRadian / windingNumber;
            final double slotDepth = 12 * MM;
            final double slotWidth = 2 * MM;

            MathFunction phiKsiFun = B20201012Utils.createPhiKsiFun(bigR, innerSmallR, phi0, tiltAngle);
            SoleLayerCct soleLayerCct = B20201012Utils.createSoleLayerCct(bigR, innerSmallR, phiKsiFun, I0,
                    0, endKsi, windingNumber * 360);
            cct.addSoleLayerCct(soleLayerCct);


            MathFunction phiKsiFun1 = B20201012Utils.createPhiKsiFun(bigR, outerSmall, phi0, tiltAngle);
            phiKsiFun1 = phiKsiFun1/*.yAxisSymmetry()*/.move(Vector2.xDirect(Math.PI));
            SoleLayerCct soleLayerCct1 = B20201012Utils.createSoleLayerCct(bigR, outerSmall, phiKsiFun1, -I0,
                    Math.PI, Math.PI + endKsi, windingNumber * 360);
            cct.addSoleLayerCct(soleLayerCct1);


            brick8sSet.add(
                    B20201012Utils.createBrick8s(
                            I0, windingNumber * numberPerWinding, slotDepth, slotWidth,
                            phiKsiFun, 0, endKsi,
                            B20201012Utils.createBipolarToroidalCoordinateSystemPoint2To3(bigR, innerSmallR)
                    ),
                    "bicctin"
            );

            brick8sSet.add(
                    B20201012Utils.createBrick8s(
                            -I0, windingNumber * numberPerWinding, slotDepth, slotWidth,
                            phiKsiFun1, Math.PI, Math.PI + endKsi,
                            B20201012Utils.createBipolarToroidalCoordinateSystemPoint2To3(bigR, outerSmall)
                    ),
                    "bicctout"
            );
        }

//        brick8sSet.clear();

        {
            final double bigR = 0.95;
            final double innerSmallR = inner;
            final double outerSmallR = inner + GAP;
            final double[] bendingAngles = {12.386, 27.344, 27.770};
            BaseUtils.Equal.requireEqual(Arrays.stream(bendingAngles).sum(), 67.5);
            final double[] bendingRadians = BaseUtils.Converter.angleToRadian(bendingAngles);
            final double tiltAngle = 30;
            final double tiltRadian = BaseUtils.Converter.angleToRadian(tiltAngle);
            final int[] windingNumbers = {29, 64, 65};
            final double[] endKsis = BaseUtils.ArrayUtils.multiple(windingNumbers, Math.PI * 2);
            final double I0 = -6500. / 19 * 17 / 30 *28; // 2.355 T
            final double[] phi0 = BaseUtils.ArrayUtils.div(bendingRadians, windingNumbers);
            final double slotDepth = 12 * MM;
            final double slotWidth = 2 * MM;

            MathFunction phiKsiFunInner1 = B20201012Utils.createPhiKsiFun(bigR, innerSmallR, phi0[0], 90, tiltAngle);
            SoleLayerCct soleLayerCctInner1 = B20201012Utils.createSoleLayerCct(bigR, innerSmallR, phiKsiFunInner1, I0,
                    0, endKsis[0], windingNumbers[0] * 360);
            cct.addSoleLayerCct(soleLayerCctInner1);

            MathFunction phiKsiFunInner2 = B20201012Utils.createPhiKsiFun(bigR, innerSmallR, phi0[1], 90, -tiltAngle)
                    .yAxisSymmetry().move(Vector2.create(endKsis[0], bendingRadians[0] + phi0[0]));
            SoleLayerCct soleLayerCctInner2 = B20201012Utils.createSoleLayerCct(bigR, innerSmallR, phiKsiFunInner2, I0,
                    endKsis[0], endKsis[0] - endKsis[1], windingNumbers[1] * 360);
            cct.addSoleLayerCct(soleLayerCctInner2);

            MathFunction phiKsiFunInner3 = B20201012Utils.createPhiKsiFun(bigR, innerSmallR, phi0[2], 90, tiltAngle)
                    .move(Vector2.create(endKsis[0] - endKsis[1],
                            bendingRadians[0] + bendingRadians[1] + phi0[0] + phi0[1]));
            SoleLayerCct soleLayerCctInner3 = B20201012Utils.createSoleLayerCct(bigR, innerSmallR, phiKsiFunInner3, I0,
                    endKsis[0] - endKsis[1], endKsis[0] - endKsis[1] + endKsis[2], windingNumbers[2] * 360);
            cct.addSoleLayerCct(soleLayerCctInner3);
            soleLayerCctInner3.plot3(Plot2d.BLUE_LINE);

            brick8sSet.add(
                    B20201012Utils.createBrick8s(
                            I0, windingNumbers[0] * numberPerWinding, slotDepth, slotWidth,
                            phiKsiFunInner1, 0, endKsis[0],
                            B20201012Utils.createBipolarToroidalCoordinateSystemPoint2To3(bigR, innerSmallR)
                    ), "agcctin1"
            );

            brick8sSet.add(
                    B20201012Utils.createBrick8s(
                            I0, windingNumbers[1] * numberPerWinding, slotDepth, slotWidth,
                            phiKsiFunInner2, endKsis[0], endKsis[0] - endKsis[1],
                            B20201012Utils.createBipolarToroidalCoordinateSystemPoint2To3(bigR, innerSmallR)
                    ), "agcctin2"
            );

            brick8sSet.add(
                    B20201012Utils.createBrick8s(
                            I0, windingNumbers[2] * numberPerWinding, slotDepth, slotWidth,
                            phiKsiFunInner3,
                            endKsis[0] - endKsis[1],
                            bendingRadians[0] + bendingRadians[1] + phi0[0] + phi0[1],
                            B20201012Utils.createBipolarToroidalCoordinateSystemPoint2To3(bigR, innerSmallR)
                    ), "agcctin3"
            );


            MathFunction phiKsiFunOuter1 = B20201012Utils.createPhiKsiFun(bigR, outerSmallR, phi0[0], 90, -tiltAngle)
                    .move(Vector2.xDirect(Math.PI));
            SoleLayerCct soleLayerCctOuter1 = B20201012Utils.createSoleLayerCct(bigR, outerSmallR, phiKsiFunOuter1, -I0,
                    Math.PI, Math.PI + endKsis[0], windingNumbers[0] * 360);
            cct.addSoleLayerCct(soleLayerCctOuter1);

            MathFunction phiKsiFunOuter2 = B20201012Utils.createPhiKsiFun(bigR, outerSmallR, phi0[1], 90, tiltAngle)
                    .yAxisSymmetry().move(Vector2.create(endKsis[0], bendingRadians[0] + phi0[0]))
                    .move(Vector2.xDirect(Math.PI));
            SoleLayerCct soleLayerCctOuter2 = B20201012Utils.createSoleLayerCct(bigR, outerSmallR, phiKsiFunOuter2, -I0,
                    endKsis[0] + Math.PI, endKsis[0] - endKsis[1] + Math.PI, windingNumbers[1] * 360);
            cct.addSoleLayerCct(soleLayerCctOuter2);
            MathFunction phiKsiFunOuter3 = B20201012Utils.createPhiKsiFun(bigR, outerSmallR, phi0[2], 90, -tiltAngle)
                    .move(Vector2.create(endKsis[0] - endKsis[1],
                            bendingRadians[0] + bendingRadians[1] + phi0[0] + phi0[1]))
                    .move(Vector2.xDirect(Math.PI));
            SoleLayerCct soleLayerCctOuter3 = B20201012Utils.createSoleLayerCct(bigR, outerSmallR, phiKsiFunOuter3, -I0,
                    endKsis[0] - endKsis[1] + Math.PI, endKsis[0] - endKsis[1] + endKsis[2] + Math.PI, windingNumbers[2] * 360);
            cct.addSoleLayerCct(soleLayerCctOuter3);
            soleLayerCctOuter3.plot3(Plot2d.RED_LINE);
            Plot3d.showThread();

            brick8sSet.add(
                    B20201012Utils.createBrick8s(
                            -I0, windingNumbers[0] * numberPerWinding, slotDepth, slotWidth,
                            phiKsiFunOuter1,
                            Math.PI,
                            Math.PI + endKsis[0],
                            B20201012Utils.createBipolarToroidalCoordinateSystemPoint2To3(bigR, outerSmallR)
                    ), "agcctou1"
            );

            brick8sSet.add(
                    B20201012Utils.createBrick8s(
                            -I0, windingNumbers[1] * numberPerWinding, slotDepth, slotWidth,
                            phiKsiFunOuter2,
                            endKsis[0] + Math.PI,
                            endKsis[0] - endKsis[1] + Math.PI,
                            B20201012Utils.createBipolarToroidalCoordinateSystemPoint2To3(bigR, outerSmallR)
                    ), "agcctou2"
            );

            brick8sSet.add(
                    B20201012Utils.createBrick8s(
                            -I0, windingNumbers[2] * numberPerWinding, slotDepth, slotWidth,
                            phiKsiFunOuter3,
                            endKsis[0] - endKsis[1] + Math.PI,
                            endKsis[0] - endKsis[1] + endKsis[2] + Math.PI,
                            B20201012Utils.createBipolarToroidalCoordinateSystemPoint2To3(bigR, outerSmallR)
                    ), "agcctou3"
            );

        }

        FileOutputStream fileOutputStream = new FileOutputStream("bipoleCoCCT-final.txt");
        fileOutputStream.write(brick8sSet.toString().getBytes());
        fileOutputStream.flush();
        fileOutputStream.close();

        return brick8sSet;
    }

    @Run(-11)
    public void 倾角和电流关系() {
        {
            System.out.println("二极");
            double k = 9206 * cot(BaseUtils.Converter.angleToRadian(30));
            Logger.getLogger().info("k = " + k);

            double cot25 = cot(BaseUtils.Converter.angleToRadian(25));
            double cot35 = cot(BaseUtils.Converter.angleToRadian(35));

            double I25 = k / cot25;
            double I35 = k / cot35;

            Logger.getLogger().info("I25 = " + I25);
            Logger.getLogger().info("I35 = " + I35);
        }

        {
            System.out.println("四极");
            double k = 7037 * cot(BaseUtils.Converter.angleToRadian(30));
            Logger.getLogger().info("k = " + k);

            double cot25 = cot(BaseUtils.Converter.angleToRadian(25));
            double cot35 = cot(BaseUtils.Converter.angleToRadian(35));

            double I25 = k / cot25;
            double I35 = k / cot35;

            Logger.getLogger().info("I25 = " + I25);
            Logger.getLogger().info("I35 = " + I35);
        }
    }

    private double cot(double r) {
        return 1 / Math.tan(r);
    }

    public static void main(String[] args) throws Exception {
        BaseUtils.Timer.printPeriodPerSecondCall(Logger.getLogger());
        StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[1];
        Logger.getLogger().info("{}", stackTraceElement.getClassName());
        Class<?> currentClass = Class.forName(stackTraceElement.getClassName());

        Constructor<?> constructor = currentClass.getConstructor();
        Object newInstance = constructor.newInstance();

        Class<Run> runAnnotation = Run.class;

        Method[] methods = currentClass.getMethods();
        Arrays.stream(methods)
                .filter(method -> method.isAnnotationPresent(runAnnotation))
                .filter(method -> method.getAnnotation(runAnnotation).validate())
                .sorted((m1, m2) -> Integer.compare(
                        m2.getAnnotation(runAnnotation).value(),
                        m1.getAnnotation(runAnnotation).value()
                ))
                .limit(1)
                .forEach(method -> {
                    Logger.getLogger().info("运行{}", method.toString());
                    try {
                        method.invoke(newInstance);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                });

        BaseUtils.Timer.printPeriodPerSecondCall(Logger.getLogger());
    }
}
