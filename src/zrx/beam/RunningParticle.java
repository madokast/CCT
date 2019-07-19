package zrx.beam;

/**
 * 粒子类.
 * 这不就是观察者模式吗?
 * 算了,没那么复杂
 */

import zrx.base.Vector3d;
import zrx.python.Plot3d;

public class RunningParticle {
    //position
    public Vector3d position;
    //velocity
    public Vector3d velocity;
    //running mass
    public double runMass;
    //chargeQuantity
    public double e;
    //speed
    public double speed;
    //distance
    public double distance;

    /**
     * 粒子在指定磁场下,运动footStep长度
     * @param m 磁场
     * @param footStep 步长
     */
    public void runSelf(Vector3d m,double footStep){
        //计算受力 qvb
        Vector3d f = Vector3d.dot(this.e,Vector3d.corss(this.velocity,m));
        //计算加速度 a = f/m
        Vector3d a = Vector3d.dot(1/this.runMass,f);
        //计算运动时间
        double t = footStep/this.speed;
        //位置变化
        this.position.addSelf(Vector3d.dot(t,this.velocity));
        //速度变化
        this.velocity.addSelf(Vector3d.dot(t,a));
        //运动距离
        this.distance+=footStep;
    }

    public RunningParticle() {
    }

    public RunningParticle(Vector3d position, Vector3d velocity, double runMass, double e, double speed, double distance) {
        this.position = position;
        this.velocity = velocity;
        this.runMass = runMass;
        this.e = e;
        this.speed = speed;
        this.distance = distance;
    }

    @Override
    public String toString() {
        return "RunningParticle{" +
                "position=" + position +
                ", velocity=" + velocity +
                ", runMass=" + runMass +
                ", e=" + e +
                ", speed=" + speed +
                ", distance=" + distance +
                '}';
    }

    /**
     * 画图
     * @param describe 描述符
     */
    public void plot3self(String describe){
        Plot3d.plotPoint(position,describe);
    }
}
