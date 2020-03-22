package cn.edu.hust.zrx.cct.base.particle;

import cn.edu.hust.zrx.cct.base.BaseUtils;
import cn.edu.hust.zrx.cct.base.CoordinateSystem3d;
import cn.edu.hust.zrx.cct.base.point.Point2;
import cn.edu.hust.zrx.cct.base.point.Point3;
import cn.edu.hust.zrx.cct.base.vector.Vector2;
import cn.edu.hust.zrx.cct.base.vector.Vector3;

import java.util.List;
import java.util.Vector;
import java.util.stream.Collectors;

/**
 * Description
 * PhaseSpaceParticle 的工具类
 * <p>
 * Data
 * 17:12
 *
 * @author zrx
 * @version 1.0
 */

public class PhaseSpaceParticles {

    public static PhaseSpaceParticle create(double x, double xp, double y, double yp, double z, double dpp) {
        return new PhaseSpaceParticle(x, xp, y, yp, z, dpp);
    }

    /**
     * 获取分布于 x xp 平面上 正相椭圆上的 PhaseSpaceParticles
     * 注意是 正相椭圆
     *
     * @param xMax   相椭圆参数 x 最大值
     * @param xpMax  相椭圆参数 xp 最大值
     * @param delta  动量分散
     * @param number 粒子数目
     * @return PhaseSpaceParticles
     */
    public static List<PhaseSpaceParticle> phaseSpaceParticlesAlongPositiveEllipseInXXpPlane(
            double xMax, double xpMax, double delta, int number) {
        //Ax^2+Bxy+Cy^2=D 椭返回圆圆周上均匀分布number个点
        final double A = 1 / Math.pow(xMax, 2);
        final double B = 0;
        final double C = 1 / Math.pow(xpMax, 2);
        final double D = 1;

        return BaseUtils.Geometry.ellipsePoints(A, B, C, D, number)
                .stream()
                .map(p -> new PhaseSpaceParticle(p.x, p.y, 0, 0, 0, delta))
                .collect(Collectors.toList());
    }

    /**
     * 获取分布于 y yp 平面上 正相椭圆上的 PhaseSpaceParticles
     * 注意是 正相椭圆
     *
     * @param yMax   相椭圆参数 y 最大值
     * @param ypMax  相椭圆参数 yp 最大值
     * @param delta  动量分散
     * @param number 粒子数目
     * @return PhaseSpaceParticles
     */
    public static List<PhaseSpaceParticle> phaseSpaceParticlesAlongPositiveEllipseInYYpPlane(
            double yMax, double ypMax, double delta, int number) {
        //Ax^2+Bxy+Cy^2=D 椭返回圆圆周上均匀分布number个点
        final double A = 1 / Math.pow(yMax, 2);
        final double B = 0;
        final double C = 1 / Math.pow(ypMax, 2);
        final double D = 1;

        return BaseUtils.Geometry.ellipsePoints(A, B, C, D, number)
                .stream()
                .map(p -> new PhaseSpaceParticle(0, 0, p.x, p.y, 0, delta))
                .collect(Collectors.toList());
    }

    public static List<PhaseSpaceParticle> phaseSpaceParticlesAlongPositiveEllipseInPlane(
            boolean xxPlane, double xMax, double xpMax, double delta, int number) {
        return xxPlane ? phaseSpaceParticlesAlongPositiveEllipseInXXpPlane(xMax, xpMax, delta, number) :
                phaseSpaceParticlesAlongPositiveEllipseInYYpPlane(xMax, xpMax, delta, number);
    }

    // 以下四个方法是低维化投影

    public static Point2 projectionToXXpPlane(PhaseSpaceParticle phaseSpaceParticle) {
        return phaseSpaceParticle.projectionToXXpPlane();
    }

    public static List<Point2> projectionToXXpPlane(List<PhaseSpaceParticle> phaseSpaceParticles) {
        return phaseSpaceParticles
                .stream()
                .map(PhaseSpaceParticles::projectionToXXpPlane)
                .collect(Collectors.toList());
    }

    public static Point2 projectionToYYpPlane(PhaseSpaceParticle phaseSpaceParticle) {
        return phaseSpaceParticle.projectionToYYpPlane();
    }

    public static List<Point2> projectionToYYpPlane(List<PhaseSpaceParticle> phaseSpaceParticles) {
        return phaseSpaceParticles
                .stream()
                .map(PhaseSpaceParticles::projectionToYYpPlane)
                .collect(Collectors.toList());
    }

    public static List<Point2> projectionToPlane(boolean xxpPlane, List<PhaseSpaceParticle> phaseSpaceParticles) {
        return xxpPlane ?
                projectionToXXpPlane(phaseSpaceParticles) :
                projectionToYYpPlane(phaseSpaceParticles);
    }

    public static Point2 projectionToPlane(boolean xxpPlane, PhaseSpaceParticle phaseSpaceParticle) {
        return xxpPlane ? projectionToXXpPlane(phaseSpaceParticle) :
                projectionToYYpPlane(phaseSpaceParticle);
    }

    // 反函数

    /**
     * 从 三维实际粒子 runningParticle 到 相空间粒子
     * 需要知道 理想粒子 还有 粒子运动所在的自然坐标系
     *
     * @param idealParticle           理想粒子
     * @param naturalCoordinateSystem 粒子运动自然坐标系 即z方向是理想粒子的方向
     * @param runningParticle         需要求 PhaseSpaceParticle 的 runningParticle
     * @return PhaseSpaceParticle
     */
    public static PhaseSpaceParticle phaseSpaceParticleFromRunningParticle(
            RunningParticle idealParticle,
            CoordinateSystem3d naturalCoordinateSystem,
            RunningParticle runningParticle) {
        // x y z
        Point3 idealParticlePosition = idealParticle.getPosition().copy();
        Point3 runningParticlePosition = runningParticle.getPosition().copy();
        Vector3 subPosition = Vector3.from(idealParticlePosition).to(runningParticlePosition);
        double x = naturalCoordinateSystem.xDirect.dot(subPosition);
        double y = naturalCoordinateSystem.yDirect.dot(subPosition);
        double z = naturalCoordinateSystem.zDirect.dot(subPosition);

        // xp yp
        Vector3 idealParticleVelocity = idealParticle.getVelocity().copy();
        Vector3 runningParticleVelocity = runningParticle.getVelocity().copy();
        double speed = idealParticle.getSpeed();
        Vector3 subVelocity = runningParticleVelocity.subtract(idealParticleVelocity);
        double xp = naturalCoordinateSystem.xDirect.dot(subVelocity) / speed;
        double yp = naturalCoordinateSystem.yDirect.dot(subVelocity) / speed;

        // delta
        double idealParticleScalarMomentum = idealParticle.computeScalarMomentum();
        double runningParticleScalarMomentum = runningParticle.computeScalarMomentum();
        double delta = (runningParticleScalarMomentum - idealParticleScalarMomentum) / idealParticleScalarMomentum;

        return new PhaseSpaceParticle(x, xp, y, yp, z, delta);
    }

    /**
     * 见 phaseSpaceParticleFromRunningParticle
     *
     * @param idealParticle           理想粒子
     * @param naturalCoordinateSystem 粒子运动自然坐标系 即z方向是理想粒子的方向
     * @param runningParticles        需要求 PhaseSpaceParticle 的 runningParticle
     * @return PhaseSpaceParticle
     * @see PhaseSpaceParticles#phaseSpaceParticleFromRunningParticle(RunningParticle, CoordinateSystem3d, RunningParticle)
     */
    public static List<PhaseSpaceParticle> phaseSpaceParticlesFromRunningParticles(
            RunningParticle idealParticle,
            CoordinateSystem3d naturalCoordinateSystem,
            List<RunningParticle> runningParticles) {
        return runningParticles
                .stream()
                .map(runningParticle ->
                        phaseSpaceParticleFromRunningParticle(idealParticle, naturalCoordinateSystem, runningParticle))
                .collect(Collectors.toList());
    }

    public static PhaseSpaceParticle convertDeltaFromMomentumDispersionToEnergyDispersion(PhaseSpaceParticle phaseSpaceParticle, double centerKineticEnergy_MeV) {
        PhaseSpaceParticle copy = phaseSpaceParticle.copy();

        double deltaMomentumDispersion = copy.getDelta();

        double deltaEnergyDispersion = Protons.convertMomentumDispersionToEnergyDispersion(
                deltaMomentumDispersion, centerKineticEnergy_MeV);

        copy.setDelta(deltaEnergyDispersion);

        return copy;

    }

    public static List<PhaseSpaceParticle> convertDeltaFromMomentumDispersionToEnergyDispersion(List<PhaseSpaceParticle> phaseSpaceParticles, double centerKineticEnergy_MeV) {
        return phaseSpaceParticles.stream()
                .map(p -> convertDeltaFromMomentumDispersionToEnergyDispersion(p, centerKineticEnergy_MeV))
                .collect(Collectors.toList());
    }

}
