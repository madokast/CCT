package cn.edu.hust.zrx.cct.base.particle;

import cn.edu.hust.zrx.cct.base.BaseUtils;
import cn.edu.hust.zrx.cct.base.CoordinateSystem3d;
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
    //running mass 相对论质量 relativistic mass
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

    public CoordinateSystem3d computeNaturalCoordinateSystem(Vector3 yDirect) {
        Vector3 zDirect = velocity.copy();

        return CoordinateSystem3d.getOneByYZ(yDirect, zDirect);
    }

    public CoordinateSystem3d computeNaturalCoordinateSystem() {
        return computeNaturalCoordinateSystem(Vector3.getZDirect());
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
        BaseUtils.Equal.requireTrue(Math.abs(speed - velocity.length()) < 1e-4 ||
                        Math.abs((speed - velocity.length()) / speed) < 1e-4,
                "建立RunningParticle，velocity.len!=speed (" + velocity.length() + " / " + speed + ")");

        System.out.println("abs(v-s)/s = " + Math.abs(speed - velocity.length()) / speed);

        this.position = position.copy();
        this.velocity = velocity.copy();
        this.runMass = runMass;
        this.e = e;
        this.speed = speed;
        this.distance = distance;
    }

    /**
     * 复制实例
     *
     * @return 复制后的粒子
     */
    public RunningParticle copy() {
        return new RunningParticle(
                this.position.copy(),
                this.velocity.copy(),
                this.runMass,
                this.e,
                this.speed,
                this.distance
        );
    }

    /**
     * 标量动量
     *
     * @return 获得标量动量
     */
    protected double computeScalarMomentum() {
        return this.speed * this.runMass;
    }

    /**
     * 改变粒子的标量动量。
     * 注意!!真正改变的是粒子的速度和动质量
     *
     * @param scalarMomentum 标量动量
     */
    protected void changeScalarMomentum(double scalarMomentum) {
//        Logger.getLogger().info("computeScalarMomentum() = " + computeScalarMomentum());
//        Logger.getLogger().info("scalarMomentum = " + scalarMomentum);

        // 先求 静止质量
        // mr = m0 / sqrt( 1- v2/c2 )
        double lightSpeed = BaseUtils.Constant.LIGHT_SPEED;
        double m0 = runMass * Math.sqrt(1 - Math.pow(speed, 2) / Math.pow(lightSpeed, 2));
//        Logger.getLogger().info("m0 = " + m0);

        // 求新的速率
        double newSpeed = scalarMomentum / Math.sqrt((Math.pow(m0, 2) + Math.pow(scalarMomentum / lightSpeed, 2)));

//        Logger.getLogger().info("speed = " + speed);
//        Logger.getLogger().info("newSpeed = " + newSpeed);

        // 求新的动质量
        double newRunMass = m0 / Math.sqrt(1 - Math.pow(newSpeed / lightSpeed, 2));
//        Logger.getLogger().info("runMass = " + runMass);
//        Logger.getLogger().info("newRunMass = " + newRunMass);

        // 求新的速度
        Vector3 newVelocity = velocity.copy().normalSelf().changeLengthSelf(newSpeed);
//        Logger.getLogger().info("velocity = " + velocity);
//        Logger.getLogger().info("newVelocity = " + newVelocity);

        // 写入
        this.runMass = newRunMass;
        this.speed = newSpeed;
        this.velocity = newVelocity;

        // 验证
        BaseUtils.Equal.requireEqual(scalarMomentum, this.computeScalarMomentum(),
                "致命错误，changeScalarMomentum()方法代码错误");
        BaseUtils.Equal.requireEqual(speed, velocity.length(),
                "致命错误，changeScalarMomentum方法代码错误");
    }

    /**
     * 粒子部署。
     * 一般接受理想粒子，然后部署之.
     * 坐标化，美妙
     * 真是太美了
     *
     * @param naturalCoordinateSystem 粒子运动自然坐标系 即z方向是理想粒子的方向
     * @param phaseSpaceParticle      相空间坐标
     */
    protected void deploySelf(CoordinateSystem3d naturalCoordinateSystem, PhaseSpaceParticle phaseSpaceParticle) {
        double x = phaseSpaceParticle.getX();
        double xp = phaseSpaceParticle.getXp();
        double y = phaseSpaceParticle.getY();
        double yp = phaseSpaceParticle.getYp();
        double z = phaseSpaceParticle.getZ();
        double delta = phaseSpaceParticle.getDelta();

        //位置改变
        position.moveSelf(Vector3.dot(naturalCoordinateSystem.xDirect, x));
        position.moveSelf(Vector3.dot(naturalCoordinateSystem.yDirect, y));
        position.moveSelf(Vector3.dot(naturalCoordinateSystem.zDirect, z));


        //部署的标量动量
        final double deployedScalarMomentum = this.computeScalarMomentum() * (1.00 + delta);
        //神来之笔 改变标量动量
        this.changeScalarMomentum(deployedScalarMomentum);

        // 速度变化x' y'
        // 此处有 bud 啊
        velocity.addSelf(Vector3.dot(naturalCoordinateSystem.xDirect, xp * speed));
        velocity.addSelf(Vector3.dot(naturalCoordinateSystem.yDirect, yp * speed));

        // 解决 bug 干脆再加一个这个
        this.changeScalarMomentum(deployedScalarMomentum);
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
