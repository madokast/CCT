package zrx.beam;

import zrx.base.Vector3d;

import java.util.function.DoubleUnaryOperator;

/**
 * 粒子工厂
 */

public class ParticleFactory {
    public static final double ProtonChargeQuantity = 1.6021766208e-19;
    public static final double ProtonStaticMassKg =1.672621898e-27;
    public static final double ProtonStaticMassMeV=938.2720813;
    public static final double LightSpeed = 299792458.0;
    public static final double Kinetic250MeV = 250.0;

    /**
     * 生成一个质子。动能250MeV
     * 只需要传入位置和速度方向即可
     * @param position 位置
     * @param direct 速度方向
     * @return 粒子
     */
    public static RunningParticle getProton250MeV(Vector3d position, Vector3d direct){
        //(Vector3d position, Vector3d velocity, double runMass, double e, double speed, double distance)

        return getProton(position,direct,Kinetic250MeV);

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

    public static RunningParticle getProton(Vector3d position, Vector3d direct,double kineticMeV){
        //计算速率
        double speed = LightSpeed*Math.sqrt(
                1-Math.pow(ProtonStaticMassMeV/(ProtonStaticMassMeV+kineticMeV), 2)
        );
        //计算质量kg
        double runMass = ProtonStaticMassKg/ Math.sqrt(1.0-
                speed*speed / (LightSpeed*LightSpeed)
        );

        return new RunningParticle(position,
                direct.setLengthAndReturn(speed),
                runMass,ProtonChargeQuantity,speed,0.0
        );
    }
}
