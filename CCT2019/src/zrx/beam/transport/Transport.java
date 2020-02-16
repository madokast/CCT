package zrx.beam.transport;

import Jama.Matrix;
import zrx.base.point.Vector2d;
import zrx.python.Plot2d;

import java.awt.*;
import java.lang.management.PlatformLoggingMXBean;
import java.util.*;
import java.util.function.DoubleUnaryOperator;
import java.util.zip.DeflaterOutputStream;

public final class Transport {
    private Transport() {
    }

    /**
     * 利用twiss参数以及 发射度 动量分散 获得 5*5 sigma 矩阵
     *
     * @param betaX Y   twiss参数
     * @param gammaX Y  twiss参数
     * @param epsi   发射度
     * @param deltaP 动量分散
     * @return sigma矩阵
     */
    public static Matrix getSigmaMatrixByTwiss(
            final double betaX, final double gammaX,
            final double betaY, final double gammaY,
            final double epsi, final double deltaP) {
        //计算alpha
        final double alphaX = Math.sqrt(Math.abs(betaX * gammaX - 1.0));
        final double alphaY = Math.sqrt(Math.abs(betaY * gammaY - 1.0));

        //计算sigmaXY
        double sigma11 = betaX * epsi;
        double sigma12 = -alphaX * epsi;
        double sigma21 = sigma12;
        double sigma22 = gammaX * epsi;

        double sigma33 = betaY * epsi;
        double sigma34 = -alphaY * epsi;
        double sigma43 = sigma34;
        double sigma44 = gammaY * epsi;

        double sigma55 = deltaP*deltaP;


        double[] sigma1x = new double[]{sigma11,sigma12,0,0,0};
        double[] sigma2x = new double[]{sigma21,sigma22,0,0,0};
        double[] sigma3x = new double[]{0,0,sigma33,sigma34,0};
        double[] sigma4x = new double[]{0,0,sigma43,sigma44,0};
        double[] sigma5x = new double[]{0,0,0,0,sigma55};

        return new Matrix(new double[][]{
                sigma1x,sigma2x,sigma3x,sigma4x,sigma5x
        });
    }


    public static Matrix getTransportMatrixByMap(Map<String,Double> map){
        final Double r11 = map.get("r11");
        final Double r12 = map.get("r12");
        final Double r21 = map.get("r21");
        final Double r22 = map.get("r22");

        final Double r33 = map.get("r33");
        final Double r34 = map.get("r34");
        final Double r43 = map.get("r43");
        final Double r44 = map.get("r44");

        final Double r16 = map.get("r16");
        final Double r26 = map.get("r26");

        double[] r1x = new double[]{r11,r12,0,0,r16};
        double[] r2x = new double[]{r21,r22,0,0,r26};
        double[] r3x = new double[]{0,0,r33,r34,0};
        double[] r4x = new double[]{0,0,r43,r44,0};
        double[] r5x = new double[]{0,0,0,0,1};

        return new Matrix(new double[][]{
                r1x,r2x,r3x,r4x,r5x
        });
    }

    /**
     *
     * @param trans
     * @param sigma
     * @return
     */
    public static Matrix transport(Matrix trans,Matrix sigma){
        // R a Rt



        return (trans.times(sigma)).times((trans.transpose()));
    }

    public static void plot2dEllipseXXC(Matrix sigma,String describe){
        final double s11 = sigma.get(0, 0);
        final double s12 = sigma.get(0, 1);
        final double s21 = sigma.get(1, 0);
        final double s22 = sigma.get(1, 1);

        double epsi = Math.sqrt(s11*s22-s12*s21);
        double beta = s11/epsi;
        double gamma = s22/epsi;
        double alpha = -s12/epsi;

        final Vector2d[] vector2ds = Vector2d.ellipsePoints(gamma, 2 * alpha, beta, epsi, 1000);
        Plot2d.plot2(vector2ds,describe);

    }

    public static void plot2dEllipseYYC(Matrix sigma,String describe){
        final double s33 = sigma.get(2, 2);
        final double s34 = sigma.get(2, 3);
        final double s43 = sigma.get(3, 2);
        final double s44 = sigma.get(3, 3);

        double epsi = Math.sqrt(s33*s44-s43*s34);
        double beta = s33/epsi;
        double gamma = s44/epsi;
        double alpha = Math.sqrt(Math.abs(beta * gamma - 1.0));

        final Vector2d[] vector2ds = Vector2d.ellipsePoints(gamma, 2 * alpha, beta, epsi, 1000);
        Plot2d.plot2(vector2ds,describe);

    }

    @Deprecated
    public static double[][][] secondMatrixReadFromCosy(){
        double[][][] T = new double[5][][];
        for (int i = 0; i < T.length; i++) {
            T[i] = new double[6][];
        }
        for (int i = 0; i < T.length; i++) {
            for (int j = 0; j < T[i].length; j++) {
                T[i][j] = new double[6];
            }
        }

        return null;
    }



    public static void main(String[] args) {
        final Matrix sigmaMatrix = getSigmaMatrixByTwiss(3.5 / 7.5, 7.5 / 3.5,
                3.5 / 7.5, 7.5 / 3.5,
                3.5 * 7.5 * 1e-6 / 100000000, 0.0);
        sigmaMatrix.print(20,12);
        System.out.println("-----------------");

        Map<String,Double> map = new HashMap<>();
        map.put("r11",5.1974);
        map.put("r12",14.3268);
        map.put("r21",1.4652);
        map.put("r22",4.2310);

        map.put("r33",116.6857);
        map.put("r34",207.7455);
        map.put("r43",39.3126);
        map.put("r44",91.2305);

        map.put("r16",0.6107);
        map.put("r26",0.2100);

        final Matrix transportMatrix = getTransportMatrixByMap(map);

        transportMatrix.print(20,12);
        System.out.println("-------------------");

        final Matrix sigma1 = transport(transportMatrix, sigmaMatrix);
        sigma1.print(20,12);


        plot2dEllipseXXC(sigmaMatrix,Plot2d.YELLOW_LINE);
        plot2dEllipseXXC(sigma1,Plot2d.BLACK_LINE);
//        plot2dEllipseYYC(sigma1,Plot2d.YELLOW_LINE);
        Plot2d.equal();
        Plot2d.showThread();
    }
}
