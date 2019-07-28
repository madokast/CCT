package zrx.base;

import Jama.Matrix;

public class Matrix66 {
    private Matrix matrix;

    @Deprecated
    public Matrix66() {
    }

    public Matrix66(double[][] m) {
        this.matrix = new Matrix(m);
    }

    public Matrix66(Matrix m){
        this.matrix=m;
    }

    public void print(){
        this.matrix.print(10,6);
    }

    public double value(){
        return this.matrix.det();
    }

    public static Matrix66 times(Matrix66 A,Matrix66 B){
        return new Matrix66(
                A.matrix.times(B.matrix)
        );
    }

    public static void main(String[] args) {
        final Matrix66 A = new Matrix66(new double[][]{
                {1, 0},
                {0, 4}
        });

        final Matrix66 B = new Matrix66(new double[][]{
                {2, 0},
                {0, 3}
        });

        System.out.println("A.value() = " + A.value());
        System.out.println("A.value() = " + B.value());
    }
}
