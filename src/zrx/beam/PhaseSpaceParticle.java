package zrx.beam;

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
