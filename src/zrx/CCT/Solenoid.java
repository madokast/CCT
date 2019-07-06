package zrx.CCT;

import zrx.base.Constants;
import zrx.base.Vector3d;

public class Solenoid extends CCT {
    private double r;
    private double w;
    private int n;
    private double I;
    private double stepTheta;
    private Vector3d p;
    private double azim;
    private double[][] azimR;
    // 即 n*2*pi/stepTheta 为了防止每次计算，所以作为属性，只计算一次
    private int totalNumber;
    // 开始点，弧度制，为了以后的需要。
    private double startPoint;

    /**
     * 直螺线管建模
     * :param r: 半径
     * :param w: 相邻导线的距离 (轴向)
     * :param n: 总匝数 n
     * :param I: 电流
     * :param stepTheta: θ步长，影响磁场计算精度
     */
    public Solenoid(double r, double w, int n, double i, double stepTheta) {
        this.r = r;
        this.w = w;
        this.n = n;
        I = i;
        this.stepTheta = stepTheta;
        this.p = new Vector3d(0.0, 0.0, 0.0);
        this.azim = 0.0;
        this.azimR = new double[][]{
                {Math.cos(azim), 0.0, Math.sin(azim)},
                {0.0, 1.0, 0.0},
                {-Math.sin(azim), 0.0, Math.cos(azim)}
        };
        this.totalNumber = getPointPerCircle() * n;
        this.startPoint = 0.0;
    }

    public void setPosition(Vector3d p) {
        this.p = p;
    }

    public void setAzimuth(double azim) {
        this.azim = azim;
        this.azimR = new double[][]{
                {Math.cos(azim), 0.0, Math.sin(azim)},
                {0.0, 1.0, 0.0},
                {-Math.sin(azim), 0.0, Math.cos(azim)}
        };
    }

    public void setStartPoint(double startPoint) {
        this.startPoint = startPoint;
    }

    @Override
    public double getStartTheta() {
        return startPoint;
    }

    @Override
    public int getN() {
        return this.n;
    }

    @Override
    public void setN(int N) {
        this.n = N;
        this.totalNumber = getPointPerCircle() * this.n;
    }

    @Override
    public double getStep() {
        return this.stepTheta;
    }

    @Override
    public void setStep(double step) {
        this.stepTheta = step;
    }

    @Override
    public int getTotalNumber() {
        return totalNumber;
    }

    @Override
    public double getI() {
        return this.I;
    }

    @Override
    public Vector3d point(double th) {
        Vector3d p0 = new Vector3d(-r * Math.cos(th),
                r * Math.sin(th),
                w / (2 * Math.PI) * th);
        p0.selfMatmul(azimR);
        return p0;
    }

    public static Solenoid demoInstance() {
        double r = 0.1;
        double w = 0.01;
        int n = 400;
        double I = 1.0;
        double step = Math.PI / 180.0;
        Solenoid solenoid = new Solenoid(r, w, n, I, step);
        return solenoid;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Solenoid{" +
                "r=" + r +
                ", w=" + w +
                ", n=" + n +
                ", I=" + I +
                ", stepTheta=" + stepTheta +
                ", p=" + p +
                ", azim=" + azim +
                '}');
        sb.append("\n对应的无限长re螺线管的理论磁场Bz = " + Constants.Miu0 * I * n / w);

        return sb.toString();
    }
}
