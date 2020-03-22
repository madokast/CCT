package cn.edu.hust.zrx.cct.advanced;

import cn.edu.hust.zrx.cct.Logger;
import cn.edu.hust.zrx.cct.base.BaseUtils;
import cn.edu.hust.zrx.cct.base.particle.PhaseSpaceParticle;
import cn.edu.hust.zrx.cct.base.particle.PhaseSpaceParticles;
import cn.edu.hust.zrx.cct.base.particle.Protons;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Description
 * TODO
 * <p>
 * Data
 * 14:27
 *
 * @author zrx
 * @version 1.0
 */

public class COSY {

    /**
     * 计算 x=Rx0 + Tx0
     * 粒子 p0 在矩阵 matrix 下运动的结果
     *
     * @param matrix 传输矩阵 R 和 T
     * @param p0     初始状态
     * @return 粒子 p0 在矩阵 matrix 下运动的结果
     */
    public static PhaseSpaceParticle transport(TransMatrix matrix, PhaseSpaceParticle p0) {
        Particle particle0 = Particle.getFromPhaseSpaceParticle(p0);
        double[] x0 = particle0.p;
        double[][][] t = matrix.T;
        double[][] r = matrix.R;

        Particle idealParticle = Particle.createIdealParticle();
        double[] x = idealParticle.p;

        for (int i = 1; i < r.length; i++) {
            for (int j = 1; j < r[i].length; j++) {
//                Logger.getLogger().info("r[{}][{}] * x0[{}] = {}*{} =  {}", i, j, j, r[i][j], x0[j], r[i][j] * x0[j]);
                x[i] += r[i][j] * x0[j];
            }
        }

        for (int i = 1; i < t.length; i++) {
            for (int j = 1; j < t[i].length; j++) {
                for (int k = 1; k < t[i][j].length; k++) {
                    x[i] += t[i][j][k] * x0[j] * x0[k];
                }
            }
        }

        return Particle.getPhaseSpaceParticleFromParticle(idealParticle);
    }

    // 粒子 Particle = (x,x',y,y',z,dp)
    private static class Particle {
        double[] p;

        private Particle() {
        }

        private static Particle getFromPhaseSpaceParticle(PhaseSpaceParticle ps) {
            Particle particle = new Particle();

            double[] p0 = new double[7];

            p0[1] = ps.getX();
            p0[2] = ps.getXp();
            p0[3] = ps.getY();
            p0[4] = ps.getYp();
            p0[5] = ps.getZ();
            p0[6] = ps.getDelta();

            particle.p = p0;

            return particle;
        }

        private static PhaseSpaceParticle getPhaseSpaceParticleFromParticle(Particle p) {
            return PhaseSpaceParticles.create(
                    p.p[1],
                    p.p[2],
                    p.p[3],
                    p.p[4],
                    p.p[5],
                    p.p[6]
            );
        }

        private static Particle createIdealParticle() {
            Particle particle = new Particle();
            particle.p = new double[7];
            Arrays.fill(particle.p, 0.0);

            return particle;
        }
    }


    /**
     * 由 COSY MAP 得到传输矩阵
     *
     * @param matrixString
     * @return
     */
    public static TransMatrix importMatrix(String matrixString) {
        TransMatrix matrix = TransMatrix.initiate();
        double[][] r = matrix.R;
        double[][][] t = matrix.T;

        String[] lines = matrixString.split("\n");
        for (String line : lines) {
            //0.1027073     0.8598542     0.0000000E+00 0.0000000E+00-0.1064929     100000

            String s1 = line.substring(0, "  -5.227584    ".length());
            String s2 = line.substring("  -5.227584    ".length(), "  -5.227584    -0.3088457    ".length());
            String s3 = line.substring("  -5.227584    -0.3088457    ".length(),
                    "  -5.227584    -0.3088457     0.0000000E+00".length());
            String s4 = line.substring("  -5.227584    -0.3088457     0.0000000E+00".length(),
                    "  -5.227584    -0.3088457     0.0000000E+00 0.0000000E+00".length());
            String s5 = line.substring("  -5.227584    -0.3088457     0.0000000E+00 0.0000000E+00".length(),
                    "  -5.227584    -0.3088457     0.0000000E+00 0.0000000E+00 -2.650967     ".length());
            String s6 = line.substring("  -5.227584    -0.3088457     0.0000000E+00 0.0000000E+00 -2.650967     ".length());

            Double[] values = new Double[]{
                    null,//0 不应该使用
                    Double.parseDouble(s1),//1
                    Double.parseDouble(s2),//2
                    Double.parseDouble(s3),//3
                    Double.parseDouble(s4),//4
                    0.0,//5 需要使用
                    Double.parseDouble(s5),//6
            };

            if (getOrderFromOOOOOO(s6) == 1) {
                //一阶
                int index = getIndexFromFirstOrderOOOOOO(s6);
                for (int i = 1; i < values.length; i++) {
                    r[i][index] = values[i];
                }
            } else if (getOrderFromOOOOOO(s6) == 2) {
                //二阶
                BaseUtils.Content.BiContent<Integer, Integer> indexFromSecondOrderOOOOOO = getIndexFromSecondOrderOOOOOO(s6);
                Integer j = indexFromSecondOrderOOOOOO.getT1();
                Integer k = indexFromSecondOrderOOOOOO.getT2();
                for (int i = 1; i < values.length; i++) {
                    t[i][j][k] = values[i];
                }
            } else {
                Logger.getLogger().error("{}阶数不支持，跳过", s6);
            }

        }

        return matrix;
    }

    public static class TransMatrix {
        double[][] R;
        double[][][] T;

        public PhaseSpaceParticle apply(PhaseSpaceParticle p0) {
            return COSY.transport(this, p0);
        }

        public List<PhaseSpaceParticle> apply(List<PhaseSpaceParticle> p0s) {
            return p0s.stream()
                    .map(this::apply)
                    .collect(Collectors.toList());
        }


        private TransMatrix() {
        }

        private static TransMatrix initiate() {
            double[][] r = new double[7][7];
            double[][][] t = new double[7][7][7];

            TransMatrix transMatrix = new TransMatrix();
            transMatrix.R = r;
            transMatrix.T = t;

            return transMatrix;
        }

        @Override
        public String toString() {
            StringBuilder stringBuilder = new StringBuilder("\n--- R ---\n");

            for (int i = 1; i < R.length; i++) {
                for (int j = 1; j < R[i].length; j++) {
                    stringBuilder.append(String.format("%+8.4f", R[i][j])).append("  ");
                }
                stringBuilder.append("\n");
            }

            stringBuilder.append("--- T ---\n");
            for (int i = 1; i < T.length; i++) {
                for (int j = 1; j < T[i].length; j++) {
                    if (!allZero(T[i][j])) {
                        for (int k = 1; k < T[i][j].length; k++) {
                            stringBuilder.append(String.format("%+10.4f", T[i][j][k])).append("  ");
                        }
                        stringBuilder.append("T").append(i).append(j).append('x').append("\n");
                    }
                }
            }

            stringBuilder.append("二阶传输矩阵中，未显示的行，表示全0");

            return stringBuilder.toString();
        }

        public double getR(int i, int j) {
            return this.R[i][j];
        }

        public double getT(int i, int j, int k) {
            return this.T[i][j][k];
        }
    }

    /**
     * 数组全0？
     *
     * @param arr 数组
     * @return 全零？
     */
    private static boolean allZero(double[] arr) {
        for (double v : arr) {
            if (Math.abs(v) >= 1e-8)
                return false;
        }

        return true;
    }

    /**
     * 查看COSY 输出 的 110000 这样的东西
     * 若 sum = 1 表示一阶 R矩阵
     * 若 sum = 2 表示二阶 T矩阵
     *
     * @param oooooo COSY 输出 的 110000 这样的东西
     * @return 阶数
     */
    private static int getOrderFromOOOOOO(String oooooo) {
        int count = 0;
        int length = oooooo.length();
        for (int i = 0; i < length; i++) {
            char charAt = oooooo.charAt(i);

            count += (charAt - '0');
        }

        return count;
    }

    /**
     * 当 oooooo 表示一阶时，如 010000
     * 返回下标数字 ，示例中为2
     *
     * @param oooooo COSY 输出 的 110000 这样的东西
     * @return 下标数字
     */
    private static int getIndexFromFirstOrderOOOOOO(String oooooo) {
        if (getOrderFromOOOOOO(oooooo) != 1)
            throw new IllegalArgumentException(oooooo + " 不是一阶!");

        int indexOf = oooooo.indexOf("1");

        return indexOf + 1;
    }

    /**
     * 当 oooooo 表示e二阶时，如 010100
     * 返回下标数字 ，示例中为 2 4
     *
     * @param oooooo COSY 输出 的 110000 这样的东西
     * @return 下标数字
     */
    private static BaseUtils.Content.BiContent<Integer, Integer> getIndexFromSecondOrderOOOOOO(String oooooo) {
        if (getOrderFromOOOOOO(oooooo) != 2)
            throw new IllegalArgumentException(oooooo + " 不是二阶!");

        int i = oooooo.indexOf("2");

        if (i == -1) {
            int i1 = oooooo.indexOf("1");
            int i2 = oooooo.lastIndexOf("1");

            return BaseUtils.Content.BiContent.create(i1 + 1, i2 + 1);
        } else
            return BaseUtils.Content.BiContent.create(i + 1, i + 1);
    }

    /**
     * 非常重要的方法
     * 把 cosy 中的 r16 单位是 m/能量分散-1
     * 转为 transport 中的 r16
     * 单位是 mm/动量分散-100%
     *
     * @param r16InCosy         cosy 中的 r16 单位是 m/能量分散-1
     * @param kineticEnergy_MeV 动能 一般 250MeV
     * @return transport 中的 r16 单位是 mm/动量分散-100%
     */
    public static double convertR16InCosyToTransportPronTonOnly(double r16InCosy, double kineticEnergy_MeV) {
        // 动量分散 = 能量分散 * (Ek+E0)/(Ek+2E0)
        double k = (kineticEnergy_MeV + Protons.STATIC_ENERGY_MeV) / (kineticEnergy_MeV + 2 * Protons.STATIC_ENERGY_MeV);

        double r16_m_per_1 = r16InCosy / k;

        double r16_mm_per_1 = r16_m_per_1*1000;

        double r16_mm_per_present = r16_mm_per_1/100;

        return r16_mm_per_present;
    }

}
