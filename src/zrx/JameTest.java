package zrx;

import Jama.Matrix;

public class JameTest {
    public static void main(String[] args) {
//        初始化();
        解方程();

    }

    private static void 解方程() {
        double[][] a = new double[][]{
                {1.0,0.0},
                {0.0,2.0}
        };

        double[][] bs = new double[][]{
                {2.0},
                {3.0}
        };

        Matrix A = new Matrix(a);
        Matrix b = new Matrix(bs);

        Matrix x = A.solve(b);

        x.print(5,2);
    }

    private static void 初始化() {
            double[][] a = new double[][]{
                    {1.1,2.2},
                    {3.3,4.4}
            };

            Matrix matrix = new Matrix(a);
            matrix.print(5,2);
    }


}
