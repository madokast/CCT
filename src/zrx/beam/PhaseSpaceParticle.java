package zrx.beam;

import zrx.Tools.PrintArray;
import zrx.base.CoordinateSystem3d;
import zrx.base.point.Vector2d;
import zrx.python.Plot2d;

public class PhaseSpaceParticle {
    double x;
    double xc;
    double y;
    double yc;
    double deltaP;

    public PhaseSpaceParticle() {
    }

    public PhaseSpaceParticle(double x, double xc, double y, double yc, double deltaP) {
        this.x = x;
        this.xc = xc;
        this.y = y;
        this.yc = yc;
        this.deltaP = deltaP;
    }

    public void plot2dXXC(String describe) {
        Plot2d.plotPoint(x, xc, describe);
    }

    public void plot2dYYC(String describe) {
        Plot2d.plotPoint(y, yc, describe);
    }

    /**
     * 这才是神代码。
     * <p>
     * 输入粒子群坐标等。输入参考粒子。输入参考粒子坐标系。
     * 返回以参考粒子为中心的相空间XX'粒子坐标。
     *
     * @param particles        所考查的粒子s
     * @param referredParticle 参考粒子
     * @param cs               参考粒子坐标系
     * @return 以参考粒子为中心的相空间XX'粒子坐标
     */
    public static Vector2d[] particlesInPhaseSpaceXXC(
            RunningParticle[] particles, RunningParticle referredParticle, CoordinateSystem3d cs) {
        Vector2d[] ps = new Vector2d[particles.length];
        for (int i = 0; i < particles.length; i++) {
            ps[i] = particles[i].phaseSpaceParticle(cs, referredParticle).getXXCAsVector2d();
        }

        return ps;
    }

    /**
     * 见上方法。
     *
     * @param particles        所考查的粒子s
     * @param referredParticle 参考粒子
     * @param cs               参考粒子坐标系
     * @return 以参考粒子为中心的相空间YY'粒子坐标
     */
    public static Vector2d[] particlesInPhaseSpaceYYC(
            RunningParticle[] particles, RunningParticle referredParticle, CoordinateSystem3d cs) {
        Vector2d[] ps = new Vector2d[particles.length];
        for (int i = 0; i < particles.length; i++) {
            ps[i] = particles[i].phaseSpaceParticle(cs, referredParticle).getYYCAsVector2d();
        }

        return ps;
    }

    /**
     * 神封装。
     * 传入一堆粒子 RunningParticle[] particles
     * 传入一个参考粒子 referredParticle
     * 传入参考粒子所处的坐标系 CoordinateSystem3d cs
     * 最后是python画图描述符
     * 就可以直接画出相椭圆
     * <p>
     * 封装个鬼鬼。功能不独立!
     *
     * @param particles        一堆粒子
     * @param referredParticle 一个参考粒子
     * @param cs               参考粒子所处的坐标系
     * @param describe         python画图描述符
     */
    @Deprecated
    public static void plot2dXXCParticlesInPhaseSpace(
            RunningParticle[] particles, RunningParticle referredParticle, CoordinateSystem3d cs, String describe) {
        Vector2d[] ps = particlesInPhaseSpaceXXC(particles, referredParticle, cs);
        Vector2d[] ps1000 = Vector2d.convict(ps,x->1000*x,y->y*1000);

        System.out.println("相椭圆拟合");
        PrintArray.print(Vector2d.fitEllipse(ps));

        Plot2d.plotPoints(ps1000, describe);
        Plot2d.plot2Circle(ps1000, Plot2d.BLACK_LINE);

        //for (RunningParticle particle : particles) {
        //            final PhaseSpaceParticle phaseSpaceParticle = particle.phaseSpaceParticle(
        //                    endCoordinateSystem3d, referredParticle);
        //            phaseSpaceParticle.plot2dXXC(Plot2d.RED_POINT);
        //        }
    }

    /**
     * 见 plot2dXXCParticlesInPhaseSpace 的解释
     *
     * @param particles        一堆粒子
     * @param referredParticle 一个参考粒子
     * @param cs               参考粒子所处的坐标系
     * @param describe         python画图描述符
     */
    @Deprecated
    public static void plot2dYYCParticlesInPhaseSpace(
            RunningParticle[] particles, RunningParticle referredParticle, CoordinateSystem3d cs, String describe) {
        Vector2d[] ps = particlesInPhaseSpaceYYC(particles, referredParticle, cs);

        Plot2d.plotPoints(ps, describe);
        Plot2d.plot2Circle(ps, Plot2d.BLACK_LINE);
    }

    public Vector2d getXXCAsVector2d() {
        return Vector2d.getOne(x, xc);
    }

    public Vector2d getYYCAsVector2d() {
        return Vector2d.getOne(y, yc);
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setXc(double xc) {
        this.xc = xc;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setYc(double yc) {
        this.yc = yc;
    }

    public void setDeltaP(double deltaP) {
        this.deltaP = deltaP;
    }

    public double getX() {
        return x;
    }

    public double getXc() {
        return xc;
    }

    public double getY() {
        return y;
    }

    public double getYc() {
        return yc;
    }

    public double getDeltaP() {
        return deltaP;
    }

    @Override
    public String toString() {
        return "PhaseSpaceParticle{" +
                "x=" + x +
                ", xc=" + xc +
                ", y=" + y +
                ", yc=" + yc +
                ", deltaP=" + deltaP +
                '}';
    }
}
