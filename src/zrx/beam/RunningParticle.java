package zrx.beam;

/**
 * 粒子类.
 * 这不就是观察者模式吗?
 * 算了,没那么复杂
 */

import zrx.CCT.ConcreteCCT.AllCCTs;
import zrx.base.CoordinateSystem3d;
import zrx.base.point.Vector3d;
import zrx.python.Plot3d;

import java.util.ArrayList;

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
     * 粒子在指定磁场下,运动footStep长度.
     * 本来这是一个高级函数，但是现在已经是低级生物了。
     * ——2019年8月11日
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
        this.position = Vector3d.copyOne(position);
        this.velocity = Vector3d.copyOne(velocity);
        this.runMass = runMass;
        this.e = e;
        this.speed = speed;
        this.distance = distance;
    }

    /**
     * 粒子部署。
     * 一般接受理想粒子，然后部署之.
     * 坐标化，美妙
     * 真是太美了
     *
     * @param cs     坐标系
     * @param x      deltaX
     * @param xc     x'
     * @param y      deltaY
     * @param yc     y'
     * @param deltaP 动量分散大小
     */
    public void deploy(CoordinateSystem3d cs, double x, double xc, double y, double yc, double deltaP) {
        //支持动量分散，大进步
//        if (!Equal.isEqual(deltaP, 0.0)) {
//            System.err.println("部署有动量分散的粒子，暂时不支持");
//            throw new RuntimeException("部署有动量分散的粒子，暂时不支持");
//        }

        //位置改变
        position.addSelf(Vector3d.dot(cs.xDirect, x));
        position.addSelf(Vector3d.dot(cs.yDirect, y));

        //部署的标量动量
        final double deployedScalarMomentum = this.getScalarMomentum() * (1.00 + deltaP);
        //神来之笔 改变标量动量
        this.changeScalarMomentum(deployedScalarMomentum);

        //速度变化x' y'
        velocity.addSelf(Vector3d.dot(cs.xDirect, xc * speed));
        velocity.addSelf(Vector3d.dot(cs.yDirect, yc * speed));
    }

    /**
     * 以参考粒子 referredParticle 为参考下 this 粒子的相空间坐标
     *
     * @param referredParticle 参考粒子
     * @return this 粒子的相空间坐标
     */
    public PhaseSpaceParticle phaseSpaceParticle(CoordinateSystem3d cs, RunningParticle referredParticle) {
        final Vector3d relativePositionGlobal = Vector3d.subtract(this.position, referredParticle.position);
        final Vector3d relativeVelocityGlobal = Vector3d.subtract(this.velocity, referredParticle.velocity);
        final double referredSpeed = referredParticle.speed;

        //double x, double xc, double y, double yc, double deltaP
        return new PhaseSpaceParticle(
                Vector3d.dot(relativePositionGlobal, cs.xDirect),
                Vector3d.dot(relativeVelocityGlobal, cs.xDirect) / referredSpeed,
                Vector3d.dot(relativePositionGlobal, cs.yDirect),
                Vector3d.dot(relativeVelocityGlobal, cs.yDirect) / referredSpeed,
                this.getScalarMomentum() - referredParticle.getScalarMomentum() / referredParticle.getScalarMomentum()
        );
    }

    /**
     * 标量动量
     *
     * @return 获得标量动量
     */
    public double getScalarMomentum() {
        return this.speed * this.runMass;
    }

    /**
     * 改变粒子的标量动量。
     * 注意!!真正改变的是粒子的速度和动质量
     *
     * @param scalarMomentum 标量动量
     */
    public void changeScalarMomentum(double scalarMomentum) {
        //获取当前速度
        final Vector3d currentVelocity = Vector3d.copyOne(this.velocity);

        this.runMass = ParticleFactory.scalarMomentumToRunMassProton(scalarMomentum);
        this.speed = ParticleFactory.scalarMomentumToSpeedProton(scalarMomentum);
        this.velocity = currentVelocity.setLengthAndReturn(this.speed);
    }


    /**
     * 粒子运动于AllCCTs
     * 记住跑完后this就改变了
     *
     * @param allCCTs      全部CCT，全部磁场来源
     * @param length       总运动长度
     * @param step         步长
     * @param plotInterval 每隔一个点画图？
     * @param describe     画图描述符
     */
    public void runInAllCCTs(AllCCTs allCCTs, double length, double step, int plotInterval, String describe) {
        int i = 0;
        java.util.List<Vector3d> traj = new ArrayList<>((int)(length/step)+10);
        while (this.distance < length) {
            if (i++ % plotInterval == 0) {
//                this.plot3self(describe);
            }
            traj.add(Vector3d.copyOne(this.position));
            //计算磁场
            Vector3d magnet = allCCTs.magnet(this.position);
            //粒子运动
            this.runSelf(magnet, step);
        }
        System.out.println("粒子之一运动完");
        Plot3d.plot3(traj.toArray(Vector3d[]::new),describe);

        //while (particle.distance < LENGTH) {
        //            //画图
        //            if(i++%100==0){
        //                particle.plot3self(Plot2d.YELLOW_SMALL_POINT);
        //            }
        //            //计算磁场
        //            Vector3d magnet = allCCTs.magnet(particle.position);
        //            //积分场
        //            inteB+=magnet.z*STEP;
        //
        //            //粒子运动
        //            particle.runSelf(magnet,STEP);
        //        }
    }

    /**
     * 同上。只不过新建线程运行之
     *
     * @param allCCTs      同上
     * @param length       同上
     * @param step         同上
     * @param plotInterval 同上
     * @param describe     同上
     */
    public Thread runInAllCCTsThread(AllCCTs allCCTs, double length, double step, int plotInterval, String describe) {
        final Thread thread = new Thread(
                () -> runInAllCCTs(allCCTs, length, step, plotInterval, describe)
        );
        thread.start();
        return thread;
    }

    /**
     * 粒子当前方位和XS平面所成的角度
     * @return 弧度角
     */
    public double relativeDirectionXS(){
        return 0.0;
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
