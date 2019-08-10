package zrx.beam;

/**
 * 粒子类.
 * 这不就是观察者模式吗?
 * 算了,没那么复杂
 */

import com.sun.source.tree.CompilationUnitTree;
import zrx.Tools.Equal;
import zrx.base.CoordinateSystem3d;
import zrx.base.Vector3d;
import zrx.python.Plot3d;

import javax.naming.OperationNotSupportedException;

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
     *
     * @param m        磁场
     * @param footStep 步长
     */
    public void runSelf(Vector3d m, double footStep) {
        //计算受力 qvb
        Vector3d f = Vector3d.dot(this.e, Vector3d.corss(this.velocity, m));
        //计算加速度 a = f/m
        Vector3d a = Vector3d.dot(1 / this.runMass, f);
        //计算运动时间
        double t = footStep / this.speed;
        //位置变化
        this.position.addSelf(Vector3d.dot(t, this.velocity));
        //速度变化
        this.velocity.addSelf(Vector3d.dot(t, a));
        //运动距离
        this.distance += footStep;
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

    /**
     * 粒子部署。
     * 一般接受理想粒子，然后部署之.
     * 坐标化，美妙
     *
     * @param cs     坐标系
     * @param x      deltaX
     * @param xc     x'
     * @param y      deltaY
     * @param yc     y'
     * @param deltaP 动量分散大小
     */
    public void deploy(CoordinateSystem3d cs, double x, double xc, double y, double yc, double deltaP) {
        if (!Equal.isEqual(deltaP, 0.0)) {
            System.err.println("部署有动量分散的粒子，暂时不支持");
            throw new RuntimeException("部署有动量分散的粒子，暂时不支持");
        }

        position.addSelf(Vector3d.dot(cs.xDirect,x));
        position.addSelf(Vector3d.dot(cs.yDirect,y));

        velocity.addSelf(Vector3d.dot(cs.xDirect,xc * speed));
        velocity.addSelf(Vector3d.dot(cs.yDirect,yc * speed));
    }

    /**
     * 以参考粒子 referredParticle 为参考下 this 粒子的相空间坐标
     * @param referredParticle 参考粒子
     * @return this 粒子的相空间坐标
     */
    public PhaseSpaceParticle phaseSpaceParticle(CoordinateSystem3d cs ,RunningParticle referredParticle){
        //TODO
        return null;
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
}
