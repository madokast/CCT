package cn.edu.hust.zrx.cct.base.particle;

import cn.edu.hust.zrx.cct.base.BaseUtils;
import cn.edu.hust.zrx.cct.base.CoordinateSystem3d;
import cn.edu.hust.zrx.cct.base.line.Line2;
import cn.edu.hust.zrx.cct.base.line.Trajectory;
import cn.edu.hust.zrx.cct.base.point.Point2To3;
import cn.edu.hust.zrx.cct.base.point.Point3;
import cn.edu.hust.zrx.cct.base.vector.Vector3;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Description
 * 粒子工厂
 * 分清楚 动能 和 动量
 * <p>
 * 质子静止能量 938 MeV  不可变，测量值
 * 质子运动能量 250 MeV  可变，自己修改
 * 质子动量 729.134 MeV/c
 * <p>
 * 动量分散 是在 729.134 MeV/c 上变化
 *
 *
 * <p>
 * Data
 * 12:15
 *
 * @author zrx
 * @version 1.0
 */

public class ParticleFactory {
    private static final double ProtonChargeQuantity = 1.6021766208e-19;
    private static final double ProtonStaticMassKg = 1.672621898e-27;
    private static final double ProtonStaticMassMeV = 938.2720813;
    private static final double LightSpeed = 299792458.0;
    private static final double Kinetic250MeV = 250.0;

    /*--------------------------基本构造器-----------------------------------*/

    /**
     * 基本工厂方法，获得一个粒子
     *
     * @param position   位置
     * @param direct     速度方向
     * @param kineticMeV 动能 不是动量
     * @return 粒子
     */
    public static RunningParticle createProton(
            final Point3 position, final Vector3 direct, final double kineticMeV) {
        //计算速率
        double speed = LightSpeed * Math.sqrt(
                1 - Math.pow(ProtonStaticMassMeV / (ProtonStaticMassMeV + kineticMeV), 2)
        );
        //计算质量kg
        double runMass = ProtonStaticMassKg / Math.sqrt(1.0 -
                speed * speed / (LightSpeed * LightSpeed)
        );

        return new RunningParticle(position.copy(),
                direct.copy().changeLengthSelf(speed),
                runMass, ProtonChargeQuantity, speed, 0.0
        );
    }


    /**
     * 生成一个质子。动能250MeV
     * 只需要传入位置和速度方向即可
     *
     * @param position 位置
     * @param direct   速度方向
     * @return 粒子
     */
    protected static RunningParticle createProton250MeV(Point3 position, Vector3 direct) {
        return createProton(position, direct, Kinetic250MeV);
    }

    /*--------------------------理想粒子/参考粒子构造器(运用trajectory)-----------------------------------*/

    /**
     * 运用理想轨迹 trajectory 构造理想粒子
     * distance 默认值 0.0
     * point2To3 默认值 (x,y) -> (x,y,0)
     * kineticMeV 默认值 250.0
     *
     * @param trajectory 理想轨迹
     * @param distance   粒子所在轨迹上的位置
     *                   由此获得 trajectory.pointAt(distance)
     *                   trajectory.directAt(distance)
     *                   即得到粒子的位置和方向
     * @param point2To3  轨迹是二维的，转化为三维的方法
     * @param kineticMeV 动量，单位是MeV
     * @return 理想粒子
     */
    public static RunningParticle createIdealProtonAtTrajectory(
            Line2 trajectory, final double distance, Point2To3 point2To3, final double kineticMeV) {
        return createProton(trajectory.pointAt(distance).toPoint3(point2To3),
                trajectory.directAt(distance).toPoint2().toPoint3(point2To3).toVector3(),
                kineticMeV);
    }

    /**
     * @param trajectory 理想轨迹
     * @param distance   粒子所在轨迹上的位置
     *                   由此获得 trajectory.pointAt(distance)
     *                   trajectory.directAt(distance)
     *                   即得到粒子的位置和方向
     * @param kineticMeV 动量，单位是MeV
     * @return 理想粒子
     * @see ParticleFactory#createIdealProtonAtTrajectory(Line2, double, Point2To3, double)
     */
    public static RunningParticle createIdealProtonAtTrajectory(
            Line2 trajectory, final double distance, final double kineticMeV) {
        return createIdealProtonAtTrajectory(
                trajectory, distance, Point2To3.getXY0ToXYZPoint2To3(), kineticMeV
        );
    }

    /**
     * @param trajectory 理想轨迹
     * @param kineticMeV 动量，单位是MeV
     * @return 理想粒子
     * @see ParticleFactory#createIdealProtonAtTrajectory(Line2, double, Point2To3, double)
     */
    public static RunningParticle createIdealProtonAtTrajectory(
            Line2 trajectory, final double kineticMeV) {
        return createIdealProtonAtTrajectory(
                trajectory, 0.0, Point2To3.getXY0ToXYZPoint2To3(), kineticMeV
        );
    }

    /**
     * @param trajectory 理想轨迹
     * @param distance   粒子所在轨迹上的位置
     *                   由此获得 trajectory.pointAt(distance)
     *                   trajectory.directAt(distance)
     *                   即得到粒子的位置和方向
     * @param point2To3  轨迹是二维的，转化为三维的方法
     * @return 理想粒子
     * @see ParticleFactory#createIdealProtonAtTrajectory(Line2, double, Point2To3, double)
     */
    public static RunningParticle createIdealProtonAtTrajectory250MeV(
            Line2 trajectory, final double distance, Point2To3 point2To3) {
        return createProton(trajectory.pointAt(distance).toPoint3(point2To3),
                trajectory.directAt(distance).toPoint2().toPoint3(point2To3).toVector3(),
                Kinetic250MeV);
    }

    /**
     * @param trajectory 理想轨迹
     * @param distance   粒子所在轨迹上的位置
     *                   由此获得 trajectory.pointAt(distance)
     *                   trajectory.directAt(distance)
     *                   即得到粒子的位置和方向
     * @return 理想粒子
     * @see ParticleFactory#createIdealProtonAtTrajectory(Line2, double, Point2To3, double)
     */
    public static RunningParticle createIdealProtonAtTrajectory250MeV(
            Line2 trajectory, final double distance) {
        return createIdealProtonAtTrajectory(
                trajectory, distance, Point2To3.getXY0ToXYZPoint2To3(), Kinetic250MeV
        );
    }

    /**
     * @param trajectory 理想轨迹
     * @return 理想粒子
     * @see ParticleFactory#createIdealProtonAtTrajectory(Line2, double, Point2To3, double)
     */
    public static RunningParticle createIdealProtonAtTrajectory250MeV(Line2 trajectory) {
        return createIdealProtonAtTrajectory(
                trajectory, 0.0, Point2To3.getXY0ToXYZPoint2To3(), Kinetic250MeV
        );
    }


    /*---------------相空间粒子群构造器(利用理想粒子、相空间粒子坐标、理想粒子坐标系)----------------*/

    /**
     * 由相空间表示的粒子 phaseSpaceParticle 转为 实际三维坐标系中的RunningParticle
     * 需要知道 相空间粒子参数 phaseSpaceParticle x xp y yp z delta
     * 以及 当前理想粒子（处于相空间的中心）
     * 还有 理想粒子所在的自然坐标系 即z方向是理想粒子的方向
     *
     * @param idealParticle           理想粒子
     * @param naturalCoordinateSystem 粒子运动自然坐标系 即z方向是理想粒子的方向
     * @param phaseSpaceParticle      相空间表示的粒子（x xp y yp z delta）
     * @return 实际三维坐标系中的RunningParticle
     */
    public static RunningParticle createParticleFromPhaseSpaceParticle(
            final RunningParticle idealParticle,
            final CoordinateSystem3d naturalCoordinateSystem,
            final PhaseSpaceParticle phaseSpaceParticle) {
        RunningParticle copy = idealParticle.copy();

        copy.deploySelf(naturalCoordinateSystem, phaseSpaceParticle);

        return copy;
    }

    /**
     * 见 createParticleFromPhaseSpaceParticle 的注释
     *
     * @param idealParticle           理想粒子
     * @param naturalCoordinateSystem 粒子运动自然坐标系 即z方向是理想粒子的方向
     * @param phaseSpaceParticles     相空间表示的粒子（x xp y yp z delta）
     * @return 实际三维坐标系中的RunningParticles
     * @see ParticleFactory#createParticleFromPhaseSpaceParticle(RunningParticle, CoordinateSystem3d, PhaseSpaceParticle)
     */
    public static List<RunningParticle> createParticlesFromPhaseSpaceParticle(
            final RunningParticle idealParticle,
            final CoordinateSystem3d naturalCoordinateSystem,
            final List<PhaseSpaceParticle> phaseSpaceParticles) {
        return phaseSpaceParticles
                .stream()
                .map(phaseSpaceParticle ->
                        createParticleFromPhaseSpaceParticle(idealParticle, naturalCoordinateSystem, phaseSpaceParticle))
                .collect(Collectors.toList());
    }

    public static List<RunningParticle> copy(List<RunningParticle> particles) {
        return particles.stream().map(RunningParticle::copy).collect(Collectors.toList());
    }
}

