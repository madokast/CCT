package cn.edu.hust.zrx.cct.study;

import cn.edu.hust.zrx.cct.Logger;
import cn.edu.hust.zrx.cct.advanced.MathFunction;
import cn.edu.hust.zrx.cct.advanced.combined.GantryDataBipolarCo;
import cn.edu.hust.zrx.cct.advanced.combined.GantryDataBipolarCoUtils;
import cn.edu.hust.zrx.cct.base.BaseUtils;
import cn.edu.hust.zrx.cct.base.cct.Cct;
import cn.edu.hust.zrx.cct.base.cct.SoleLayerCct;
import cn.edu.hust.zrx.cct.base.line.Trajectory;
import cn.edu.hust.zrx.cct.base.magnet.Elements;
import cn.edu.hust.zrx.cct.base.particle.ParticleFactory;
import cn.edu.hust.zrx.cct.base.particle.ParticleRunner;
import cn.edu.hust.zrx.cct.base.particle.RunningParticle;
import cn.edu.hust.zrx.cct.base.point.Point3;
import cn.edu.hust.zrx.cct.base.vector.Vector2;
import cn.edu.hust.zrx.cct.base.vector.Vector3;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class C20201113GeneRunnerAllGantryTest {

    @Test
    void dipoleCct345_local_goodWindingMethod() {
        GantryDataBipolarCo.SecondBend secondBend = GantryDataBipolarCo.SecondBend.getDefault();
        Cct cct = Cct.getEmptyCct();

        final double bigR = secondBend.dipoleCct345BigR;
        final double innerSmallR = secondBend.dipoleCct345SmallRInner;
        final double outerSmall = secondBend.dipoleCct345SmallROuter;
        final double bendingAngle = secondBend.dipoleCct345Angle;
        final double bendingRadian = BaseUtils.Converter.angleToRadian(bendingAngle);
        final double[] tiltAngles = secondBend.dipoleCct345TiltAngles;
        final int windingNumber = secondBend.dipoleCct345WindingNumber;
        final double endKsi = windingNumber * Math.PI * 2;
        final double I0 = secondBend.dipoleCct345I0; // 2.355 T
        final double phi0 = bendingRadian / windingNumber;
        final int disperseNumberPerWinding = secondBend.disperseNumberPerWinding;

        MathFunction phiKsiFun = MathFunction.createPhiKsiFun(bigR, innerSmallR, phi0, tiltAngles);
        SoleLayerCct soleLayerCct = SoleLayerCct.createSoleLayerCct(bigR, innerSmallR, phiKsiFun, I0,
                0, endKsi, windingNumber * disperseNumberPerWinding);
        cct.addSoleLayerCct(soleLayerCct);

//        soleLayerCct.getWindings().forEach(System.out::println);


        MathFunction phiKsiFun1 = MathFunction.createPhiKsiFun(bigR, outerSmall, phi0, tiltAngles);
        phiKsiFun1 = phiKsiFun1.yAxisSymmetry();
        SoleLayerCct soleLayerCct1 = SoleLayerCct.createSoleLayerCct(bigR, outerSmall, phiKsiFun1, I0,
                0, -endKsi, windingNumber * disperseNumberPerWinding);
        cct.addSoleLayerCct(soleLayerCct1);

        soleLayerCct1.getWindings().forEach(System.out::println);


        Vector3 m = cct.magnetAt(Point3.origin());
        Vector3 m0 = soleLayerCct.magnetAt(Point3.origin());
        Vector3 m1 = soleLayerCct1.magnetAt(Point3.origin());

        Logger.getLogger().info("m = " + m);
        Logger.getLogger().info("m0 = " + m0);
        Logger.getLogger().info("m1 = " + m1);
    }

    @Test
    void agCct345_local_goodWindingMethod() {
        GantryDataBipolarCo.SecondBend secondBend = GantryDataBipolarCo.SecondBend.getDefault();
        Cct cct = Cct.getEmptyCct();

        final double bigR = secondBend.agCct345BigR;

        final double innerSmallR = secondBend.agCct345SmallRInner;
        final double outerSmallR = secondBend.agCct345SmallROuter;
        final double[] bendingAngles = {secondBend.agCctAngle3, secondBend.agCctAngle4, secondBend.agCctAngle5};
        BaseUtils.Equal.requireEqual(Arrays.stream(bendingAngles).sum(), 67.5);
        final double[] bendingRadians = BaseUtils.Converter.angleToRadian(bendingAngles);
        final double[] tiltAngles = secondBend.agCct345TiltAngles;
        final int[] windingNumbers = {secondBend.agCctWindingNumber3, secondBend.agCctWindingNumber4, secondBend.agCctWindingNumber5};
        final double[] endKsis = BaseUtils.ArrayUtils.multiple(windingNumbers, Math.PI * 2);
        final double I0 = secondBend.agCct345I0; // 2.355 T
        final double[] phi0 = BaseUtils.ArrayUtils.div(bendingRadians, windingNumbers);

        MathFunction phiKsiFunInner1 = MathFunction.createPhiKsiFun(bigR, innerSmallR, phi0[0], tiltAngles);
        SoleLayerCct soleLayerCctInner1 = SoleLayerCct.createSoleLayerCct(bigR, innerSmallR, phiKsiFunInner1, I0,
                0, endKsis[0], windingNumbers[0] * 360);
        cct.addSoleLayerCct(soleLayerCctInner1);

        Vector3 m_in_1 = soleLayerCctInner1.magnetAt(Point3.origin());
        Logger.getLogger().info("m_in_1 = " + m_in_1);


        MathFunction phiKsiFunInner2 = MathFunction.createPhiKsiFun(bigR, innerSmallR, phi0[1], BaseUtils.ArrayUtils.dot(tiltAngles, -1))
                .yAxisSymmetry().move(Vector2.create(0.0, bendingRadians[0] + phi0[0]));
        SoleLayerCct soleLayerCctInner2 = SoleLayerCct.createSoleLayerCct(bigR, innerSmallR, phiKsiFunInner2, I0,
                0, -endKsis[1], windingNumbers[1] * 360);
        cct.addSoleLayerCct(soleLayerCctInner2);

        Vector3 m_in2 = soleLayerCctInner2.magnetAt(Point3.origin());
        Logger.getLogger().info("m_in_2 = " + m_in2);
        //-3.351266998928599E-4, 6.797547549589101E-4, -2.2179458252074218E-4
        //-3.351266998928599E-4, 6.797547549589101E-4, -2.2179458252074218E-4

        MathFunction phiKsiFunInner3 = MathFunction.createPhiKsiFun(bigR, innerSmallR, phi0[2], tiltAngles)
                .move(Vector2.create(0, bendingRadians[0] + bendingRadians[1] + phi0[0] + phi0[1]));
        SoleLayerCct soleLayerCctInner3 = SoleLayerCct.createSoleLayerCct(bigR, innerSmallR, phiKsiFunInner3, I0,
                0, endKsis[2], windingNumbers[2] * 360);
        cct.addSoleLayerCct(soleLayerCctInner3);

        //6.1425297339177E-4, -4.3500564216640087E-4, -3.942973858333819E-4
        //6.142529733917701E-4, -4.350056421664014E-4, -3.942973858333819E-4
        Vector3 m_in3 = soleLayerCctInner3.magnetAt(Point3.origin());
        Logger.getLogger().info("m_in_3 = " + m_in3);


        MathFunction phiKsiFunOuter1 = MathFunction.createPhiKsiFun(bigR, outerSmallR, phi0[0], tiltAngles)
                .yAxisSymmetry();
        SoleLayerCct soleLayerCctOuter1 = SoleLayerCct.createSoleLayerCct(bigR, outerSmallR, phiKsiFunOuter1, I0,
                0, -endKsis[0], windingNumbers[0] * 360);
        cct.addSoleLayerCct(soleLayerCctOuter1);


        MathFunction phiKsiFunOuter2 = MathFunction.createPhiKsiFun(bigR, outerSmallR, phi0[1], BaseUtils.ArrayUtils.dot(tiltAngles, -1))
                .move(Vector2.create(0, bendingRadians[0] + phi0[0]));
        SoleLayerCct soleLayerCctOuter2 = SoleLayerCct.createSoleLayerCct(bigR, outerSmallR, phiKsiFunOuter2, I0,
                0, endKsis[1], windingNumbers[1] * 360);
        cct.addSoleLayerCct(soleLayerCctOuter2);
        //4.6851782278505737E-4, -9.503188433596936E-4, -1.654274319233047E-4
        //4.6851782278505846E-4, -9.503188433596932E-4, -1.654274319233049E-4


        MathFunction phiKsiFunOuter3 = MathFunction.createPhiKsiFun(bigR, outerSmallR, phi0[2], tiltAngles)
                .move(Vector2.create(0, bendingRadians[0] + bendingRadians[1] + phi0[0] + phi0[1]))
                .yAxisSymmetry();
        SoleLayerCct soleLayerCctOuter3 = SoleLayerCct.createSoleLayerCct(bigR, outerSmallR, phiKsiFunOuter3, I0,
                0, -endKsis[2], windingNumbers[2] * 360);
        cct.addSoleLayerCct(soleLayerCctOuter3);
        // -8.587814779358804E-4, 6.081774195203553E-4, -4.5113444265626744E-4
        //-8.587814779358805E-4, 6.081774195203557E-4, -4.511344426562672E-4


        Vector3 m_ou1 = soleLayerCctOuter1.magnetAt(Point3.origin());
        Vector3 m_ou2 = soleLayerCctOuter2.magnetAt(Point3.origin());
        Vector3 m_ou3 = soleLayerCctOuter3.magnetAt(Point3.origin());
        Logger.getLogger().info("m_ou1 = " + m_ou1);
        Logger.getLogger().info("m_ou2 = " + m_ou2);
        Logger.getLogger().info("m_ou3 = " + m_ou3);
    }

    @Test
    void all() {
        GantryDataBipolarCo.SecondBend secondBend = GantryDataBipolarCo.SecondBend.getDefault();

        Cct b = C20201113GeneRunnerAllGantry.dipoleCct345_local_goodWindingMethod(secondBend);

        Cct a = C20201113GeneRunnerAllGantry.agCct345_local_goodWindingMethod(secondBend);


        Vector3 m = Elements.of(a, b).magnetAt(Point3.create(0.95, 0.1, 0));


        Logger.getLogger().info("m = " + m);


    }

    @Test
    void particle_run() {
        BaseUtils.Timer.printPeriodPerSecondCall(Logger.getLogger());

        GantryDataBipolarCo.SecondBend secondBend = GantryDataBipolarCo.SecondBend.getDefault();

        Cct b = C20201113GeneRunnerAllGantry.dipoleCct345_local_goodWindingMethod(secondBend);

        Cct a = C20201113GeneRunnerAllGantry.agCct345_local_goodWindingMethod(secondBend);

        Elements e = Elements.of(a, b);

        Trajectory t = GantryDataBipolarCoUtils.getTrajectory2(GantryDataBipolarCo.FirstBend.getDefault(), secondBend);

        double len = secondBend.DL2 + secondBend.CCT345_LENGTH + secondBend.GAP3;

        Logger.getLogger().info("len = " + len);

        RunningParticle ip = ParticleFactory.createIdealProtonAtTrajectory(t, 215);

        Logger.getLogger().info("ip = " + ip);

        ParticleRunner.run(ip, e, len, 0.001);

        Logger.getLogger().info("ip = " + ip);

        BaseUtils.Timer.printPeriodPerSecondCall(Logger.getLogger());
    }
}