package zrx.beam;

import zrx.CCT.ConcreteCCT.AllCCTs;
import zrx.base.Vector3d;

import java.util.ArrayList;
import java.util.List;

/**
 * 粒子工厂
 */

public class ParticleFactory {
    public static final double ProtonChargeQuantity = 1.6021766208e-19;
    public static final double ProtonStaticMassKg = 1.672621898e-27;
    public static final double ProtonStaticMassMeV = 938.2720813;
    public static final double LightSpeed = 299792458.0;
    public static final double Kinetic250MeV = 250.0;

    /**
     * 生成一个质子。动能250MeV
     * 只需要传入位置和速度方向即可
     *
     * @param position 位置
     * @param direct   速度方向
     * @return 粒子
     */
    public static RunningParticle getProton250MeV(Vector3d position, Vector3d direct) {
        //(Vector3d position, Vector3d velocity, double runMass, double e, double speed, double distance)

        return getProton(position, direct, Kinetic250MeV);

//        //计算速率
//        double speed = LightSpeed*Math.sqrt(
//                1-Math.pow(ProtonStaticMassMeV/(ProtonStaticMassMeV+Kinetic250MeV), 2)
//        );
//        //计算质量kg
//        double runMass = ProtonStaticMassKg/ Math.sqrt(1.0-
//                speed*speed / (LightSpeed*LightSpeed)
//        );
//
//        return new RunningParticle(position,
//                direct.setLengthAndReturn(speed),
//                runMass,ProtonChargeQuantity,speed,0.0
//                );
    }

    public static RunningParticle getProton(Vector3d position, Vector3d direct, double kineticMeV) {
        //计算速率
        double speed = LightSpeed * Math.sqrt(
                1 - Math.pow(ProtonStaticMassMeV / (ProtonStaticMassMeV + kineticMeV), 2)
        );
        //计算质量kg
        double runMass = ProtonStaticMassKg / Math.sqrt(1.0 -
                speed * speed / (LightSpeed * LightSpeed)
        );

        return new RunningParticle(position,
                direct.setLengthAndReturn(speed),
                runMass, ProtonChargeQuantity, speed, 0.0
        );
    }

    public static List<Thread> runInAllCCTsThread(
            RunningParticle[] runningParticles, AllCCTs allCCTs,
            double length, double step, int plotInterval, String describe){
        final ArrayList<Thread> threads = new ArrayList<>(runningParticles.length);
        for (int i = 0; i < runningParticles.length; i++) {
            final Thread runInAllCCTsThread = runningParticles[i].runInAllCCTsThread(allCCTs, length, step, plotInterval, describe);
            threads.add(runInAllCCTsThread);
        }

        return threads;
    }

    public static RunningParticle copy(RunningParticle particle) {
        //Vector3d position, Vector3d velocity, double runMass, double e, double speed, double distance
        return new RunningParticle(
                Vector3d.copyOne(particle.position),
                Vector3d.copyOne(particle.velocity),
                particle.runMass,
                particle.e,
                particle.speed,
                particle.distance
        );
    }

    /**
     * 工具类。已知质子的速度，求其动能
     *
     * @param speed 速度
     * @return 动量
     */
    public static double speedToKineticMeVProton(double speed) {
        double temp = Math.sqrt(1 - Math.pow(speed / LightSpeed, 2));

        return ProtonStaticMassMeV * (1.0 / temp - 1.0);

        //double speed = LightSpeed*Math.sqrt(
        //                1-Math.pow(ProtonStaticMassMeV/(ProtonStaticMassMeV+kineticMeV), 2)
        //        );
    }

    /**
     * 工具方法。
     * 已知质子动能MeV 求其速度
     *
     * @param kineticMeV 动能MeV
     * @return 速度
     */
    public static double kineticMeVToSpeedProton(double kineticMeV) {
        return LightSpeed * Math.sqrt(
                1 - Math.pow(ProtonStaticMassMeV / (ProtonStaticMassMeV + kineticMeV), 2));
    }

    /**
     * 已知质子速度求动质量
     *
     * @param speed 速度
     * @return 动质量
     */
    public static double speedToRunMassProton(double speed) {
        return ProtonStaticMassKg / Math.sqrt(1.0 -
                speed * speed / (LightSpeed * LightSpeed));
    }

    /**
     * 已知质子的标量动量，求其速度
     *
     * @param scalarMomentum 标量动量
     * @return 速度
     */
    public static double scalarMomentumToSpeedProton(double scalarMomentum) {
        double temp = Math.pow(ProtonStaticMassKg / scalarMomentum, 2) + Math.pow(1.0 / LightSpeed, 2);
        return 1.0 / Math.sqrt(temp);
    }

    /**
     * 已知质子的标量动量，求动质量
     *
     * @param scalarMomentum 标量动量
     * @return 动质量
     */
    public static double scalarMomentumToRunMassProton(double scalarMomentum) {
        final double speed = scalarMomentumToSpeedProton(scalarMomentum);
        return speedToRunMassProton(speed);
    }


    public static RunningParticle[] copy(RunningParticle particle, int number) {
        RunningParticle[] particles = new RunningParticle[number];
        for (int i = 0; i < number; i++) {
            particles[i] = copy(particle);
        }

        return particles;
    }

    //测试
    public static void main(String[] args) {
        final RunningParticle proton250MeV = getProton250MeV(Vector3d.getZeros(), Vector3d.getZDirect());
        System.out.println(proton250MeV);

        System.out.println("------------------");

        System.out.println(proton250MeV.speed);

        final double KineticMeV = speedToKineticMeVProton(proton250MeV.speed);
        System.out.println(KineticMeV);

        System.out.println(kineticMeVToSpeedProton(250.0));

        System.out.println("------------------");
        final double scalarMomentum = proton250MeV.getScalarMomentum();
        System.out.println("标量动量：" + scalarMomentum);
        System.out.println(scalarMomentumToSpeedProton(scalarMomentum));

        System.out.println("-----------");
        System.out.println(speedToRunMassProton(proton250MeV.speed));
        System.out.println(scalarMomentumToRunMassProton(scalarMomentum));
    }

}
