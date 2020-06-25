package cn.edu.hust.zrx.cct.advanced;


import cn.edu.hust.zrx.cct.Logger;
import cn.edu.hust.zrx.cct.base.particle.PhaseSpaceParticle;

/**
 * Description
 * SR   -1.750000e-03   -6.495191e-03    0.000000e+00    0.000000e+00  0    8.948052e-02 0 0 2 ;
 * 球面均匀分布
 * <p>
 * Data
 * 2020/1/1 10:44
 *
 * @author zrx
 * @version 1.0
 */

public class SR {
    private static final int NUMBER = 700;
    private static final double X_MAX = 3.5e-3;
    private static final double X1_MAX = 7.5e-3;
    private static final double DE_MAX = 0.0895;

    public static void main(String[] args) {
        final SR sr = new SR();
//        final Point3[] point3s = sr.sphericalUniformDistribution(100);
//        printPoint3s(point3s);
        sr.printSRy(
                50, 5e-3, 5.6e-3, 0.1432, 2);
//        sr.printSRy(700,3.5e-3,7.5e-3,8.94/100,2);
    }

    public void printSRx(int number, double xMax, double x1Max, double dE, int color) {
        final Point3[] point3s = sphericalUniformDistribution(number);

        double max = 0;

        for (Point3 point3 : point3s) {

            double x126 = point3.y * x1Max * point3.z * dE;
            if (Math.abs(x126) > max) {
                max = Math.abs(x126);
            }
            //Logger.getLogger().info("max = " + max);

            System.out.println(
                    sr(
                            point3.x * xMax,
                            point3.y * x1Max,
                            0,
                            0,
                            point3.z * dE,
                            color
                    )
            );
        }
    }

    public void printSRy(int number, double xMax, double x1Max, double dE, int color) {
        final Point3[] point3s = sphericalUniformDistribution(number);
        for (Point3 point3 : point3s) {
            System.out.println(
                    sr(
                            0,
                            0,
                            point3.x * xMax,
                            point3.y * x1Max,
                            point3.z * dE,
                            2
                    )
            );
        }
    }

    public String sr(double x, double x1, double y, double y1, double dE, int color) {
        return String.format("SR  %e  %e  %e  %e  0  %e  0  0  %d ;",
                x, x1, y, y1, dE, color);
    }

    public String sr(PhaseSpaceParticle p, int color) {
        return sr(
                p.getX(),
                p.getXp(),
                p.getY(),
                p.getYp(),
                p.getDelta(),
                color
        );
    }

    public static Point3[] sphericalUniformDistribution(int number) {
        int num = 0;
        Point3[] point3s = new Point3[number];
        while (true) {
            double x1 = Math.random() * 2 - 1;
            double x2 = Math.random() * 2 - 1;

            if (x1 * x1 + x2 * x2 >= 1)
                continue;

            point3s[num] = new Point3(
                    2 * x1 * Math.sqrt(1 - x1 * x1 - x2 * x2),
                    2 * x2 * Math.sqrt(1 - x1 * x1 - x2 * x2),
                    1 - 2 * (x1 * x1 + x2 * x2)
            );

            num++;

            if (num == number)
                break;
        }

        return point3s;
    }

    public static void printPoint3s(Point3[] point3s) {
        for (Point3 point3 : point3s) {
            System.out.println(point3);
        }
    }

    public static class Point3 {
        public double x;
        public double y;
        public double z;

        public Point3(double x, double y, double z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        @Override
        public String toString() {
            return x + "  " + y + "  " + z;
        }
    }
}
