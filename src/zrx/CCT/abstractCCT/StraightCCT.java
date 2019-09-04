package zrx.CCT.abstractCCT;

import zrx.base.Constants;
import zrx.base.point.Vector3d;

/**
 * 直线CCT
 */

public class StraightCCT extends CCT {
    //CCT 建模
    //:param r: 半径
    //:param w: 相邻导线的距离 (轴向)
    //:param n: 总匝数 n
    //:param I: 电流
    //:param stepTheta: θ步长，影响磁场计算精度
    //:param tiltAngle: 倾斜角，见文档
    //:param nth: 绕线方式 n=1 二极场 / n=2 四机场
    private double r;
    private double w;
    private int n;
    private double I;
    private double tiltAngle;
    private int nth;
    private double stepTheta;

    private Vector3d p;//位置
    private double azim;//方位角 这里固定以 y 为旋转轴，azim=0 表示z轴正方向，azim>0 表示往x轴的正向旋转
    private double[][] azimR;// 旋转矩阵
    private int totalNumber;// 即 n*2*pi/stepTheta 为了防止每次计算，所以作为属性，只计算一次
    private double startPoint;// 开始点，弧度制，为了以后的需要。

    public StraightCCT(double r, double w, int n, double I, double tiltAngle, int nth, double stepTheta) {
        this.r = r;
        this.w = w;
        this.n = n;
        this.I = I;
        this.tiltAngle = tiltAngle;
        this.nth = nth;
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

    public void setP(Vector3d p) {
        this.p = p;
    }

    public void setAzim(double azim) {
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
    public int getN() {
        return n;
    }

    @Override
    public void setN(int N) {
        this.n = N;
        this.totalNumber = getPointPerCircle() * this.n;
    }

    @Override
    public double getStep() {
        return stepTheta;
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
    public double getStartTheta() {
        return startPoint;
    }

    @Override
    public double getI() {
        return I;
    }

    @Override
    public Vector3d point(double th) {
        /**
         计算θ位置对应的点坐标
         注意坐标为笛卡尔直角坐标系
         当元件位置处于坐标轴原点且方位角为0时：
         螺线管轴向为 z 轴，从z=0开始正向生长
         θ=0 时，x=-r y=0 ，朝向 z 轴正方向，随 θ 增大，线圈顺时针转动
         具体可以参考文档AGCCT
         :param theta: 唯一的一个自变量θ
         :return: θ位置对应的点坐标
         */

        th = th + startPoint;

        Vector3d p0 = new Vector3d(-r * Math.cos(th), r * Math.sin(th),
                r / Math.tan(tiltAngle) / nth * Math.sin(nth * th) + (w / (2 * Math.PI) * th)
        );
        p0.selfMatmul(azimR);
        p0.addSelf(p);
        return p0;
    }

    public void printTheoreticalValues() {
        if (nth == 1) {
            System.out.println("这应该也许是个二极场CCT吧");
            System.out.println("理论By = " + (-Constants.Miu0 * I / 2.0 / w / Math.tan(tiltAngle)));
        } else if (nth == 2) {
            System.out.println("这应该也许是个四极场CCT吧");
            System.out.println("理论梯度G = " + (-Constants.Miu0 * I / 2.0 / w / Math.tan(tiltAngle) / r));
        }

        System.out.println("理论Bz = " + (Constants.Miu0 * I / this.w));
    }
}
