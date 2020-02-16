package cn.edu.hust.zrx.cct.base.particle;

import cn.edu.hust.zrx.cct.base.point.Point3;
import cn.edu.hust.zrx.cct.base.python.Plot3d;
import cn.edu.hust.zrx.cct.base.vector.Vector3;

/**
 * Description
 * 运动粒子
 * 只有麻烦的构造器
 * 以及粒子在磁场中走一步
 * 外加一个画图方法
 * <p>
 * Data
 * 12:15
 *
 * @author zrx
 * @version 1.0
 */

public class RunningParticle {
    //position
    public Point3 position;
    //velocity
    public Vector3 velocity;
    //running mass
    public double runMass;
    //chargeQuantity
    public double e;
    //speed
    public double speed;
    //distance
    public double distance;

    /**
     * 粒子在指定磁场下,运动footStep长度.
     * 本来这是一个高级函数，但是现在已经是低级生物了。
     * ——2019年8月11日
     *
     * @param magnet   磁场
     * @param footStep 步长
     */
    public RunningParticle runSelf(Vector3 magnet, double footStep) {
        //计算受力 qvb
        Vector3 f = Vector3.dot(this.e, Vector3.cross(this.velocity, magnet));
        //计算加速度 a = f/m
        Vector3 a = Vector3.dot(1 / this.runMass, f);
        //计算运动时间
        double t = footStep / this.speed;
        //位置变化
        this.position.moveSelf(Vector3.dot(t, this.velocity));
        //速度变化
        this.velocity.addSelf(Vector3.dot(t, a));
        //运动距离
        this.distance += footStep;

        return this;
    }

    /**
     * 构造器
     *
     * @param position 粒子位置
     * @param velocity 速度方向
     * @param runMass  运动质量
     * @param e        电荷量
     * @param speed    速率
     * @param distance 运动距离
     */
    protected RunningParticle(Point3 position, Vector3 velocity,
                              double runMass, double e, double speed, double distance) {
        this.position = position.copy();
        this.velocity = velocity.copy();
        this.runMass = runMass;
        this.e = e;
        this.speed = speed;
        this.distance = distance;
    }

    /**
     * 复制实例
     * @return 复制后的粒子
     */
    protected RunningParticle copy(){
        return new RunningParticle(
                this.position.copy(),
                this.velocity.copy(),
                this.runMass,
                this.e,
                this.speed,
                this.distance
        );
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
     *
     * @param describe 描述符
     */
    public void plot3self(String describe) {
        Plot3d.plotPoint(position, describe);
    }

    public Point3 getPosition() {
        return position;
    }

    public Vector3 getVelocity() {
        return velocity;
    }

    public double getRunMass() {
        return runMass;
    }

    public double getE() {
        return e;
    }

    public double getSpeed() {
        return speed;
    }

    public double getDistance() {
        return distance;
    }
}
