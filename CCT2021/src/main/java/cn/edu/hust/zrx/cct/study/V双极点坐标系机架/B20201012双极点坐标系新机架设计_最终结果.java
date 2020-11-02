package cn.edu.hust.zrx.cct.study.V双极点坐标系机架;

import cn.edu.hust.zrx.cct.Logger;
import cn.edu.hust.zrx.cct.advanced.CctUtils;
import cn.edu.hust.zrx.cct.advanced.MathFunction;
import cn.edu.hust.zrx.cct.advanced.PolynomialFitter;
import cn.edu.hust.zrx.cct.advanced.opera.Brick8s;
import cn.edu.hust.zrx.cct.advanced.opera.Brick8sSet;
import cn.edu.hust.zrx.cct.advanced.opera.OperaCct;
import cn.edu.hust.zrx.cct.base.BaseUtils;
import cn.edu.hust.zrx.cct.base.annotation.Run;
import cn.edu.hust.zrx.cct.base.cct.Cct;
import cn.edu.hust.zrx.cct.base.magnet.Elements;
import cn.edu.hust.zrx.cct.base.cct.SoleLayerCct;
import cn.edu.hust.zrx.cct.base.line.Trajectory;
import cn.edu.hust.zrx.cct.base.line.TrajectoryFactory;
import cn.edu.hust.zrx.cct.base.point.*;
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
import java.util.function.Function;

import static cn.edu.hust.zrx.cct.base.BaseUtils.Constant.MM;
import static cn.edu.hust.zrx.cct.base.BaseUtils.Constant.kA;

/**
 * Description
 * B20201012双极点坐标系新机架设计
 * CCT 孔径从 63 开始
 * <p>
 * Data
 * 10:14
 *
 * @author zrx
 * @version 1.0
 */

public class B20201012双极点坐标系新机架设计_最终结果 {

    private static final int numberPerWinding = 30;

    @Run
    public void 双极点坐标系() {
        // 再次推到一下圆心


        final double a = 1;
        final double eta = 0.1;

        final Point2 F1 = Point2.create(a, 0);
        final Point2 F2 = Point2.create(-a, 0);
        Vector2 xDirect = Vector2.xDirect();

        MathFunction xf = ksi -> a * Math.sinh(eta) / (
                Math.cosh(eta) - Math.cos(ksi)
        );


        MathFunction yf = ksi -> a * Math.sin(ksi) / (
                Math.cosh(eta) - Math.cos(ksi)
        );

        Point2Function p2f = Point2Function.create(xf, yf);

        Plot2d.plot2(p2f.disperse(0, Math.PI, 10000));

        Plot2d.showThread();

        p2f.disperse(0, Math.PI, 50)
                .forEach(p2 -> {
                    Vector2 F1P = Vector2.from(F1).to(p2);
                    Vector2 F2P = Vector2.from(F2).to(p2);

                    Logger.getLogger().info("eta = {}", Math.log(F1P.length() / F2P.length()));

                    double t1 = xDirect.angleToInRadian(F1P);
                    double t2 = xDirect.angleToInRadian(F2P);

                    Logger.getLogger().info("ksi = " + BaseUtils.Converter.radianToAngle(t1 - t2));
                });
    }

    @Run(1)
    public void LBNL二极CCT建模() {
        final double bigR = 900 * MM;
        final double smallR = 150 * MM;
        final double bendingAngle = 50;
        final double bendingRadian = BaseUtils.Converter.angleToRadian(bendingAngle);
        final double tiltAngle = 31.5; // 待定
        final double tiltRadian = BaseUtils.Converter.angleToRadian(tiltAngle);
        final int windingNumber = 159;
        final double endKsi = windingNumber * Math.PI * 2;
        final double I0 = 922 * 6;
        final double phi0 = bendingRadian / windingNumber;

        MathFunction phiKsiFun = B20201012Utils.createPhiKsiFun(bigR, smallR, phi0, tiltAngle);
        SoleLayerCct soleLayerCct = B20201012Utils.createSoleLayerCct(bigR, smallR, phiKsiFun, I0,
                0, endKsi, windingNumber * 360);


        MathFunction phiKsiFun1 = B20201012Utils.createPhiKsiFun(bigR, smallR, phi0, tiltAngle);
        phiKsiFun1 = phiKsiFun1/*.yAxisSymmetry()*/.move(Vector2.xDirect(Math.PI));
        SoleLayerCct soleLayerCct1 = B20201012Utils.createSoleLayerCct(bigR, smallR, phiKsiFun1, -I0,
                Math.PI, Math.PI + endKsi, windingNumber * 360);


        Trajectory trajectory = TrajectoryFactory.setStartingPoint(900 * MM, -1)
                .setStraightLine(1, Vector2.yDirect())
                .addArcLine(900 * MM, false, 50)
                .addStraitLine(1);

        List<Point2> bz = soleLayerCct.magnetBzAlongTrajectory(trajectory);
        List<Point2> bz1 = soleLayerCct1.magnetBzAlongTrajectory(trajectory);

        soleLayerCct1.plot3(Plot2d.BLUE_LINE);
        soleLayerCct.plot3(Plot2d.RED_LINE);
        trajectory.plot3d();
        Plot3d.showThread();

        Plot2d.plot2(bz);
        Plot2d.plot2(bz1);

        Plot2d.showThread();
    }

    @Run(2)
    public Elements er后偏转段675二极CCT() {
        final double bigR = 0.95;
        final double innerSmallR = 102 * MM;
        final double outerSmall = 117 * MM;
        final double bendingAngle = 67.5;
        final double bendingRadian = BaseUtils.Converter.angleToRadian(bendingAngle);
        final double tiltAngle = 30;
        final double tiltRadian = BaseUtils.Converter.angleToRadian(tiltAngle);
        final int windingNumber = 128;
        final double endKsi = windingNumber * Math.PI * 2;
        final double I0 = 10 * kA / 2.558 * 2.355; // 2.355 T
        final double phi0 = bendingRadian / windingNumber;

        MathFunction phiKsiFun = B20201012Utils.createPhiKsiFun(bigR, innerSmallR, phi0, tiltAngle);
        SoleLayerCct soleLayerCct = B20201012Utils.createSoleLayerCct(bigR, innerSmallR, phiKsiFun, I0,
                0, endKsi, windingNumber * 360);


        MathFunction phiKsiFun1 = B20201012Utils.createPhiKsiFun(bigR, outerSmall, phi0, tiltAngle);
        phiKsiFun1 = phiKsiFun1/*.yAxisSymmetry()*/.move(Vector2.xDirect(Math.PI));
        SoleLayerCct soleLayerCct1 = B20201012Utils.createSoleLayerCct(bigR, outerSmall, phiKsiFun1, -I0,
                Math.PI, Math.PI + endKsi, windingNumber * 360);


        Trajectory trajectory = TrajectoryFactory.setStartingPoint(bigR, -1)
                .setStraightLine(1, Vector2.yDirect())
                .addArcLine(bigR, false, bendingAngle)
                .addStraitLine(1);

//        List<Point2> bz = soleLayerCct.magnetBzAlongTrajectory(trajectory);
//        List<Point2> bz1 = soleLayerCct1.magnetBzAlongTrajectory(trajectory);
//
//        soleLayerCct1.plot3(Plot2d.BLUE_LINE);
//        soleLayerCct.plot3(Plot2d.RED_LINE);
//        trajectory.plot3d();
//        Plot3d.showThread();
//
//        Plot2d.plot2(bz);
//        Plot2d.plot2(bz1);
//        Plot2d.plot2(Point2.addOnY(bz, bz1));
//
//        Plot2d.showThread();

        Elements elements = Elements.empty();
        elements.addElement(soleLayerCct);
        elements.addElement(soleLayerCct1);
        return elements;
    }

    @Run(3)
    public Elements si后偏转段675四极CCT() {
        final double bigR = 0.95;
        final double innerSmallR = 72 * MM;
        final double outerSmallR = 87 * MM;
        final double[] bendingAngles = {12.386, 27.344, 27.770};
        BaseUtils.Equal.requireEqual(Arrays.stream(bendingAngles).sum(), 67.5);
        final double[] bendingRadians = BaseUtils.Converter.angleToRadian(bendingAngles);
        final double tiltAngle = 30;
        final double tiltRadian = BaseUtils.Converter.angleToRadian(tiltAngle);
        final int[] windingNumbers = {20, 44, 44};
        final double[] endKsis = BaseUtils.ArrayUtils.multiple(windingNumbers, Math.PI * 2);
        final double I0 = -10 * kA / 27 * 19; // 2.355 T
        final double[] phi0 = BaseUtils.ArrayUtils.div(bendingRadians, windingNumbers);

        MathFunction phiKsiFunInner1 = B20201012Utils.createPhiKsiFun(bigR, innerSmallR, phi0[0], 90, tiltAngle);
        SoleLayerCct soleLayerCctInner1 = B20201012Utils.createSoleLayerCct(bigR, innerSmallR, phiKsiFunInner1, I0,
                0, endKsis[0], windingNumbers[0] * 360);

        MathFunction phiKsiFunInner2 = B20201012Utils.createPhiKsiFun(bigR, innerSmallR, phi0[1], 90, -tiltAngle)
                .yAxisSymmetry().move(Vector2.create(endKsis[0], bendingRadians[0] + phi0[0]));
        SoleLayerCct soleLayerCctInner2 = B20201012Utils.createSoleLayerCct(bigR, innerSmallR, phiKsiFunInner2, I0,
                endKsis[0], endKsis[0] - endKsis[1], windingNumbers[1] * 360);

        MathFunction phiKsiFunInner3 = B20201012Utils.createPhiKsiFun(bigR, innerSmallR, phi0[2], 90, tiltAngle)
                .move(Vector2.create(endKsis[0] - endKsis[1],
                        bendingRadians[0] + bendingRadians[1] + phi0[0] + phi0[1]));
        SoleLayerCct soleLayerCctInner3 = B20201012Utils.createSoleLayerCct(bigR, innerSmallR, phiKsiFunInner3, I0,
                endKsis[0] - endKsis[1], endKsis[0] - endKsis[1] + endKsis[2], windingNumbers[2] * 360);

//        soleLayerCctInner1.plot3(Plot2d.YELLOW_LINE);
//        soleLayerCctInner2.plot3(Plot2d.RED_LINE);
//        soleLayerCctInner3.plot3(Plot2d.BLUE_LINE);
//        Plot3d.showThread();


        MathFunction phiKsiFunOuter1 = B20201012Utils.createPhiKsiFun(bigR, outerSmallR, phi0[0], 90, tiltAngle)
                .move(Vector2.xDirect(Math.PI));
        SoleLayerCct soleLayerCctOuter1 = B20201012Utils.createSoleLayerCct(bigR, outerSmallR, phiKsiFunOuter1, I0,
                Math.PI, Math.PI + endKsis[0], windingNumbers[0] * 360);
        MathFunction phiKsiFunOuter2 = B20201012Utils.createPhiKsiFun(bigR, outerSmallR, phi0[1], 90, -tiltAngle)
                .yAxisSymmetry().move(Vector2.create(endKsis[0], bendingRadians[0] + phi0[0]))
                .move(Vector2.xDirect(Math.PI));
        SoleLayerCct soleLayerCctOuter2 = B20201012Utils.createSoleLayerCct(bigR, outerSmallR, phiKsiFunOuter2, I0,
                endKsis[0] + Math.PI, endKsis[0] - endKsis[1] + Math.PI, windingNumbers[1] * 360);

        MathFunction phiKsiFunOuter3 = B20201012Utils.createPhiKsiFun(bigR, outerSmallR, phi0[2], 90, tiltAngle)
                .move(Vector2.create(endKsis[0] - endKsis[1],
                        bendingRadians[0] + bendingRadians[1] + phi0[0] + phi0[1]))
                .move(Vector2.xDirect(Math.PI));
        SoleLayerCct soleLayerCctOuter3 = B20201012Utils.createSoleLayerCct(bigR, outerSmallR, phiKsiFunOuter3, I0,
                endKsis[0] - endKsis[1] + Math.PI, endKsis[0] - endKsis[1] + endKsis[2] + Math.PI, windingNumbers[2] * 360);

//        soleLayerCctOuter1.plot3(Plot2d.YELLOW_LINE);
//        soleLayerCctOuter2.plot3(Plot2d.RED_LINE);
//        soleLayerCctOuter3.plot3(Plot2d.BLUE_LINE);
//        Plot3d.showThread();

        Elements e = Elements.empty();
        e.addElement(soleLayerCctInner1);
        e.addElement(soleLayerCctInner2);
        e.addElement(soleLayerCctInner3);
        e.addElement(soleLayerCctOuter1);
        e.addElement(soleLayerCctOuter2);
        e.addElement(soleLayerCctOuter3);

        return e;
    }

    @Run(4)
    public void 磁场分布() {
        final double bigR = 0.95;
        final double innerSmallR = 102 * MM;
        final double outerSmall = 117 * MM;
        final double bendingAngle = 67.5;
        final double bendingRadian = BaseUtils.Converter.angleToRadian(bendingAngle);
        final double tiltAngle = 30;
        final double tiltRadian = BaseUtils.Converter.angleToRadian(tiltAngle);
        final int windingNumber = 128;
        final double endKsi = windingNumber * Math.PI * 2;
        final double I0 = 10 * kA / 2.558 * 2.355; // 2.355 T
        final double phi0 = bendingRadian / windingNumber;

        Elements elements = (si后偏转段675四极CCT()).addElement(er后偏转段675二极CCT());

        Trajectory trajectory = TrajectoryFactory.setStartingPoint(bigR, -1)
                .setStraightLine(1, Vector2.yDirect())
                .addArcLine(bigR, false, bendingAngle)
                .addStraitLine(1);


        List<Point2> bz = elements.magnetBzAlongTrajectory(trajectory);

        List<Point2> g = elements.magnetGradientAlongTrajectoryFast(trajectory, 50 * MM, 10 * MM);

        Plot2d.plot2(g);
        Plot2d.plot2(bz);

        Plot2d.showThread();
    }

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
    public Brick8sSet opera() throws IOException {
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
            final int windingNumber = 128;
            final double endKsi = windingNumber * Math.PI * 2;
            final double I0 = 9206; // 2.355 T
            final double phi0 = bendingRadian / windingNumber;
            final double slotDepth = 12 * MM;
            final double slotWidth = 2 * MM;
            Logger.getLogger().info("phi0 = " + phi0);

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
            final int[] windingNumbers = {20, 44, 44};
            final double[] endKsis = BaseUtils.ArrayUtils.multiple(windingNumbers, Math.PI * 2);
            final double I0 = -7037; // 2.355 T
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

        FileOutputStream fileOutputStream = new FileOutputStream("bipoleCoCCT-final.txt");
        fileOutputStream.write(brick8sSet.toString().getBytes());
        fileOutputStream.flush();
        fileOutputStream.close();


        Trajectory trajectory = TrajectoryFactory.setStartingPoint(0.95, -1)
                .setStraightLine(1, Vector2.yDirect())
                .addArcLine(0.95, false, 67.5)
                .addStraitLine(1);

//        List<Point2> bz2 = cct.magnetBzAlongTrajectory(trajectory);
//        List<Point2> g = cct.magnetGradientAlongTrajectoryFast(trajectory, 10 * MM, 10 * MM);

//        Plot2d.plot2(bz2);
//        Plot2d.plot2(g);

        CctUtils.bxByBzBmdAlong(cct,trajectory);

//        Plot2d.showThread();

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

    @Run(-100)
    public void 超导线材() {
        PolynomialFitter fitter = PolynomialFitter.build(2);
        double[] fit = fitter.fit(List.of(
                Point2.create(4, 770),
                Point2.create(5, 630),
                Point2.create(6, 480),
                Point2.create(7, 240),
                Point2.create(8, 210),
                Point2.create(9, 80)
        ));

        Plot2d.plot2(List.of(
                Point2.create(4, 770),
                Point2.create(5, 630),
                Point2.create(6, 480),
                Point2.create(7, 240),
                Point2.create(8, 210),
                Point2.create(9, 80)
        ), Plot2d.BLACK_SMALL_POINT);

        Plot2d.plotPoint(Point2.origin(), Plot2d.WHITE_LINE);

        Function<Double, Double> polynomialFunction = MathFunction.createPolynomialFunction(fit);

        {
            Plot2d.plot2(List.of(
                    Point2.origin(),
                    Point2.create(4.60, 418)
            ), Plot2d.RED_LINE);

            Plot2d.plotPoint(Point2.create(4.60, 418), Plot2d.RED_POINT);

            Plot2d.plot2(List.of(
                    Point2.origin(),
                    Point2.create(4.60 / 418 * 511, 511)
            ), Plot2d.RED_DASH);
        }

        {
            Plot2d.plot2(List.of(
                    Point2.origin(),
                    Point2.create(4.85, 320)
            ), Plot2d.RED_LINE);

            Plot2d.plotPoint(Point2.create(4.85, 320), Plot2d.RED_POINT);

            Plot2d.plot2(List.of(
                    Point2.origin(),
                    Point2.create(4.85 / 320 * 412, 412)
            ), Plot2d.RED_DASH);
        }

        Plot2d.plot2(polynomialFunction, Point2.create(3, 10), 100, Plot2d.GREY_DASH);

        Plot2d.info("B/T", "I/A", "线圈最大工作点及电流裕度", 18);

        Plot2d.xlim(0, 10);
        Plot2d.ylim(0, 1000);

        Plot2d.showThread();
    }

    @Run(101)
    public void 超导线材2() {
        PolynomialFitter fitter = PolynomialFitter.build(2);
        double[] fit = fitter.fit(List.of(
                Point2.create(4, 990),
                Point2.create(5, 732),
                Point2.create(6, 558),
                Point2.create(7, 391),
                Point2.create(8, 247),
                Point2.create(9, 100)
        ));

        Plot2d.plot2(List.of(
                Point2.create(4, 990),
                Point2.create(5, 732),
                Point2.create(6, 558),
                Point2.create(7, 391),
                Point2.create(8, 247),
                Point2.create(9, 100)
        ), Plot2d.BLACK_SMALL_POINT);

        Plot2d.plotPoint(Point2.origin(), Plot2d.WHITE_LINE);

        Function<Double, Double> polynomialFunction = MathFunction.createPolynomialFunction(fit);

        double dipoleMax = 4.09;
        double agcctMax = 4.36;
        double dipoleSingleI = 9206. / 20;
        double agcctSingleI = 7037./ 20;

        Logger.getLogger().info("dipoleSingleI = " + dipoleSingleI);
        Logger.getLogger().info("agcctSingleI = " + agcctSingleI);

        {
            Plot2d.plot2(List.of(
                    Point2.origin(),
                    Point2.create(dipoleMax, dipoleSingleI)
            ), Plot2d.RED_LINE);

            Plot2d.plotPoint(Point2.create(dipoleMax, dipoleSingleI), Plot2d.RED_POINT);

            Plot2d.plot2(List.of(
                    Point2.origin(),
                    Point2.create(dipoleMax / dipoleSingleI * 620, 620)
            ), Plot2d.RED_DASH);
        }

        {
            Plot2d.plot2(List.of(
                    Point2.origin(),
                    Point2.create(agcctMax, agcctSingleI)
            ), Plot2d.RED_LINE);

            Plot2d.plotPoint(Point2.create(agcctMax, agcctSingleI), Plot2d.RED_POINT);

            Plot2d.plot2(List.of(
                    Point2.origin(),
                    Point2.create(agcctMax / agcctSingleI * 505, 505)
            ), Plot2d.RED_DASH);
        }

        Plot2d.plot2(polynomialFunction, Point2.create(3, 10), 100, Plot2d.GREY_DASH);

        Plot2d.info("B/T", "I/A", "线圈最大工作点及电流裕度", 18);

        Plot2d.xlim(0, 10);
        Plot2d.ylim(0, 1000);

        Plot2d.showThread();
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
