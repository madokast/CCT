package cn.edu.hust.zrx.cct.base.cct;

import cn.edu.hust.zrx.cct.Logger;
import cn.edu.hust.zrx.cct.advanced.MathFunction;

/**
 * Description
 * CctLine2Factory
 * <p>
 * Data
 * 20:50
 *
 * @author zrx
 * @version 1.0
 */

public class CctLine2Factory {
//    @Deprecated // 屎也要吃啊
    public static MathFunction createCct2dPathFunction(double startingφ, double startingθ, double reversed,
                                                       double n, double a0Inner, double a1Inner, double a2Inner) {
        Logger.getLogger().info("startingφ{},startingθ{},reversed{}",startingφ,startingθ,reversed);

        return θ -> startingφ
                + ((θ - startingθ) * reversed) / n // 这里肯定是加号
                + a0Inner * Math.sin(θ * reversed)
                + a1Inner * Math.sin(2 * θ * reversed)
                + a2Inner * Math.sin(3 * θ * reversed);
    }


    /**
     * 非交变CCT构造工厂
     *
     * @param smallR        小 r CCT 半径/孔径
     * @param bigR          大 R CCT 偏转半径
     * @param angle         CCT 角度，如 67.5 度 CCT，angle = 67.5
     * @param windingNumber 匝数
     * @param a0Bipolar     二极场系数
     * @param a1Quadruple   四极场系数
     * @param a2Sextuple    六极场系数
     * @return 非交变CCT
     */
    public static CctLine2s create(double smallR,
                                   double bigR,
                                   double angle,
                                   int windingNumber,
                                   double a0Bipolar,
                                   double a1Quadruple,
                                   double a2Sextuple) {
        return CctLine2s.create(
                new CctLine2(smallR, bigR, angle, windingNumber,
                        a0Bipolar, a1Quadruple, a2Sextuple,
                        0.0, 0.0, true)
        );
    }

    /**
     * 非交变CCT构造工厂
     * 可以控制初始绕线位置、绕线点位置、绕线方向
     * φ = f(θ) = θ/n + Σ a(i)*sin( (i+1)*θ )
     *
     * @param smallR        小 r CCT 半径/孔径
     * @param bigR          大 R CCT 偏转半径
     * @param angle         CCT 角度，如 67.5 度 CCT，angle = 67.5
     * @param windingNumber 匝数
     * @param a0Bipolar     二极场系数
     * @param a1Quadruple   四极场系数
     * @param a2Sextuple    六极场系数
     * @param startingθ     初始绕线位置，即直线 CCT 中的 θ ,周期 2pi 函数
     * @param startingφ     类似直线 CCT 的 Z
     * @param directθ       绕线方向，true代表逆时针
     * @return
     */
    @SuppressWarnings("all")
    public static CctLine2s create(double smallR,
                                   double bigR,
                                   double angle,
                                   int windingNumber,
                                   double a0Bipolar,
                                   double a1Quadruple,
                                   double a2Sextuple,
                                   double startingθ,
                                   double startingφ,
                                   boolean directθ) {
        return CctLine2s.create(
                new CctLine2(smallR, bigR, angle, windingNumber,
                        a0Bipolar, a1Quadruple, a2Sextuple,
                        startingθ, startingφ, directθ)
        );
    }

    /**
     * 交变CCT构造工厂
     *
     * @param smallR         小 r CCT 半径/孔径
     * @param bigR           大 R CCT 偏转半径
     * @param angles         AG-CCT每段的角度
     * @param windingNumbers AG-CCT每段的匝数
     * @param a0Bipolar      二极场系数
     * @param a1Quadruple    四极场系数
     * @param a2Sextuple     六极场系数
     * @return 交变CCT
     */
    @SuppressWarnings("all")
    @Deprecated// 无法构建绕线相反的CCT
    public static CctLine2s createAGCCT(double smallR,
                                        double bigR,
                                        double[] angles,
                                        int[] windingNumbers,
                                        double a0Bipolar,
                                        double a1Quadruple,
                                        double a2Sextuple) {
        double startingθ = 0.0;
        double startingφ = 0.0;
        boolean directθ = true;

        CctLine2 cct0 = new CctLine2(smallR, bigR, angles[0], windingNumbers[0],
                a0Bipolar, a1Quadruple, a2Sextuple,
                startingθ, startingφ, directθ);
        CctLine2s cctLine2s = CctLine2s.create(cct0);

        for (int i = 1; i < angles.length; i++) {
            startingθ += (directθ ? 1 : -1) * windingNumbers[i - 1] * 2 * Math.PI;
            startingφ += windingNumbers[i - 1] * 2 * Math.PI
                    / Math.abs(cctLine2s.cctLine2s.get(cctLine2s.cctLine2s.size() - 1).getN());
            directθ = !directθ;

            cctLine2s.add(
                    new CctLine2(smallR, bigR, angles[i], windingNumbers[i],
                            a0Bipolar, a1Quadruple, a2Sextuple,
                            startingθ, startingφ, directθ)
            );
        }

        return cctLine2s;
    }

    @SuppressWarnings("all")
    public static CctLine2s createAGCCT(double smallR,
                                        double bigR,
                                        double[] angles,
                                        int[] windingNumbers,
                                        double a0Bipolar,
                                        double a1Quadruple,
                                        double a2Sextuple,
                                        double startingθ,
                                        double startingφ,
                                        boolean directθ) {

        CctLine2 cct0 = new CctLine2(smallR, bigR, angles[0], windingNumbers[0],
                a0Bipolar, a1Quadruple, a2Sextuple,
                startingθ, startingφ, directθ);
        CctLine2s cctLine2s = CctLine2s.create(cct0);

        for (int i = 1; i < angles.length; i++) {
            startingθ += (directθ ? 1 : -1) * windingNumbers[i - 1] * 2 * Math.PI;
            startingφ += windingNumbers[i - 1] * 2 * Math.PI
                    / Math.abs(cctLine2s.cctLine2s.get(cctLine2s.cctLine2s.size() - 1).getN());
            directθ = !directθ;

            cctLine2s.add(
                    new CctLine2(smallR, bigR, angles[i], windingNumbers[i],
                            a0Bipolar, a1Quadruple, a2Sextuple,
                            startingθ, startingφ, directθ)
            );
        }

        return cctLine2s;
    }

    /**
     * 交变CCT构造工厂
     *
     * @param smallR         小 r CCT 半径/孔径
     * @param bigR           大 R CCT 偏转半径
     * @param angles         AG-CCT每段的角度
     * @param windingNumbers AG-CCT每段的匝数
     * @param a0Bipolars     每段二极场系数
     * @param a1Quadruples   每段四极场系数
     * @param a2Sextuples    每段六极场系数
     * @return 交变CCT
     */
    @SuppressWarnings("all")
    @Deprecated// 无法构建绕线相反的CCT
    protected static CctLine2s createAGCCT(double smallR,
                                           double bigR,
                                           double[] angles,
                                           int[] windingNumbers,
                                           double[] a0Bipolars,
                                           double[] a1Quadruples,
                                           double[] a2Sextuples) {
        double startingθ = 0.0;
        double startingφ = 0.0;
        boolean directθ = true;

        CctLine2 cct0 = new CctLine2(smallR, bigR, angles[0], windingNumbers[0],
                a0Bipolars[0], a1Quadruples[0], a2Sextuples[0],
                startingθ, startingφ, directθ);
        CctLine2s cctLine2s = CctLine2s.create(cct0);

        for (int i = 1; i < angles.length; i++) {
            startingθ += (directθ ? 1 : -1) * windingNumbers[i - 1] * 2 * Math.PI;
            startingφ += windingNumbers[i - 1] * 2 * Math.PI
                    / Math.abs(cctLine2s.cctLine2s.get(cctLine2s.cctLine2s.size() - 1).getN());
            directθ = !directθ;

            cctLine2s.add(
                    new CctLine2(smallR, bigR, angles[i], windingNumbers[i],
                            a0Bipolars[i], a1Quadruples[i], a2Sextuples[i],
                            startingθ, startingφ, directθ)
            );
        }

        return cctLine2s;
    }

    @SuppressWarnings("all")
    protected static CctLine2s createAGCCT(double smallR,
                                           double bigR,
                                           double[] angles,
                                           int[] windingNumbers,
                                           double[] a0Bipolars,
                                           double[] a1Quadruples,
                                           double[] a2Sextuples,
                                           double startingθ,
                                           double startingφ,
                                           boolean directθ) {

        CctLine2 cct0 = new CctLine2(smallR, bigR, angles[0], windingNumbers[0],
                a0Bipolars[0], a1Quadruples[0], a2Sextuples[0],
                startingθ, startingφ, directθ);
        CctLine2s cctLine2s = CctLine2s.create(cct0);

        for (int i = 1; i < angles.length; i++) {
            startingθ += (directθ ? 1 : -1) * windingNumbers[i - 1] * 2 * Math.PI;
            startingφ += windingNumbers[i - 1] * 2 * Math.PI
                    / Math.abs(cctLine2s.cctLine2s.get(cctLine2s.cctLine2s.size() - 1).getN());
            directθ = !directθ;

            cctLine2s.add(
                    new CctLine2(smallR, bigR, angles[i], windingNumbers[i],
                            a0Bipolars[i], a1Quadruples[i], a2Sextuples[i],
                            startingθ, startingφ, directθ)
            );
        }

        return cctLine2s;
    }


}
