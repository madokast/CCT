package cn.edu.hust.zrx.cct.advanced;

import cn.edu.hust.zrx.cct.base.BaseUtils;
import org.apache.commons.math3.ode.FirstOrderIntegrator;
import org.apache.commons.math3.ode.nonstiff.DormandPrince853Integrator;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * Description
 * FirstOrderTransportMatrixSolver
 * <p>
 * Data
 * 13:25
 *
 * @author zrx
 * @version 1.0
 */

public class FirstOrderTransportMatrixSolver {
    private FirstOrderTransportMatrixSolver() {
    }

    /**
     * @return 初始化
     */
    public static FirstOrderTransportMatrixSolver build() {
        return new FirstOrderTransportMatrixSolver();
    }


    /**
     * @param hFunction h(s) 二极场
     * @param KFunction k(s) 四极场
     * @return 中间类
     */
    public FirstOrderTransportMatrixSolverMagnetInfo setHKFunction(
            Function<Double, Double> hFunction,
            Function<Double, Double> KFunction
    ) {
        FirstOrderTransportMatrixSolverMagnetInfo firstOrderTransportMatrixSolverMagnetInfo =
                new FirstOrderTransportMatrixSolverMagnetInfo();
        firstOrderTransportMatrixSolverMagnetInfo.sethFunction(hFunction);
        firstOrderTransportMatrixSolverMagnetInfo.setkFunction(KFunction);
        return firstOrderTransportMatrixSolverMagnetInfo;
    }


    public class FirstOrderTransportMatrixSolverMagnetInfo {

        private Function<Double, Double> hFunction;
        private Function<Double, Double> kFunction;

        public Function<Double, Double> gethFunction() {
            return hFunction;
        }

        public void sethFunction(Function<Double, Double> hFunction) {
            this.hFunction = hFunction;
        }

        public Function<Double, Double> getkFunction() {
            return kFunction;
        }

        public void setkFunction(Function<Double, Double> kFunction) {
            this.kFunction = kFunction;
        }

        /**
         * @param logger 是否开启日志
         * @return 中间类
         */
        public FirstOrderTransportMatrixSolverMagnetInfoLogger isLogger(boolean logger) {
            FirstOrderTransportMatrixSolverMagnetInfoLogger firstOrderTransportMatrixSolverMagnetInfoLogger
                    = new FirstOrderTransportMatrixSolverMagnetInfoLogger();
            firstOrderTransportMatrixSolverMagnetInfoLogger.setLogger(logger);
            return firstOrderTransportMatrixSolverMagnetInfoLogger;
        }

        public class FirstOrderTransportMatrixSolverMagnetInfoLogger {
            private boolean isLogger;

            public boolean isLogger() {
                return isLogger;
            }

            public void setLogger(boolean logger) {
                isLogger = logger;
            }

            public FirstOrderTransportMatrixSolverMagnetInfoLoggerODE initiate(
                    double sStart, double sEnd, double minStep, double maxStep,
                    double scaleAbsoluteTolerance, double scaleRelativeTolerance,
                    BaseUtils.Async async) {
                return new FirstOrderTransportMatrixSolverMagnetInfoLoggerODE(
                        sStart, sEnd, minStep, maxStep,
                        scaleAbsoluteTolerance, scaleRelativeTolerance, async);
            }

            public class FirstOrderTransportMatrixSolverMagnetInfoLoggerODE {
                //        final double sStart = 0.0;
                //        final double sEnd = trajectory.getLength();
                //        Logger.getLogger().info("sEnd = " + sEnd);
                //
                //        final double minStep = 1e-10;
                //        final double maxStep = 0.1;
                //        final double scaleAbsoluteTolerance = 1e-6;
                //        final double scaleRelativeTolerance = 1e-6;

                private double sStart;
                private double sEnd;

                private double minStep;
                private double maxStep;
                private double scaleAbsoluteTolerance;
                private double scaleRelativeTolerance;

                private BaseUtils.Async async;

                public FirstOrderTransportMatrixSolverMagnetInfoLoggerODE(
                        double sStart, double sEnd, double minStep, double maxStep,
                        double scaleAbsoluteTolerance, double scaleRelativeTolerance,
                        BaseUtils.Async async) {
                    this.sStart = sStart;
                    this.sEnd = sEnd;
                    this.minStep = minStep;
                    this.maxStep = maxStep;
                    this.scaleAbsoluteTolerance = scaleAbsoluteTolerance;
                    this.scaleRelativeTolerance = scaleRelativeTolerance;
                    this.async = async;
                }

                public Map<String, Double> solve() {
                    boolean isLogger = FirstOrderTransportMatrixSolverMagnetInfoLogger.this.isLogger;
                    Function<Double, Double> hFunction = FirstOrderTransportMatrixSolverMagnetInfo.this.gethFunction();
                    Function<Double, Double> kFunction = FirstOrderTransportMatrixSolverMagnetInfo.this.getkFunction();

                    FirstOrderTransportMatrixEquation firstOrderTransportMatrixEquation00
                            = new FirstOrderTransportMatrixEquation() {
                        @Override
                        public HKdP getHKdp(double s) {
                            return HKdP.valueOf(
                                    hFunction.apply(s),
                                    kFunction.apply(s),
                                    0.0
                            );
                        }

                        @Override
                        public boolean loggerDistance() {
                            return isLogger;
                        }
                    };

                    FirstOrderTransportMatrixEquation firstOrderTransportMatrixEquation10
                            = new FirstOrderTransportMatrixEquation() {
                        @Override
                        public HKdP getHKdp(double s) {
                            return HKdP.valueOf(
                                    hFunction.apply(s),
                                    kFunction.apply(s),
                                    1.0
                            );
                        }

                        @Override
                        public boolean loggerDistance() {
                            return isLogger;
                        }
                    };

                    Map<String, Double> matrix = new ConcurrentHashMap<>();

                    async.execute(() -> {
                        double[] y0 = new double[]{1.0, 0, 0, 0};
                        final FirstOrderIntegrator integrator = new DormandPrince853Integrator(
                                minStep, maxStep, scaleAbsoluteTolerance, scaleRelativeTolerance);
                        integrator.integrate(firstOrderTransportMatrixEquation00, sStart, y0, sEnd, y0);
                        matrix.put("r11", y0[0]);
                        matrix.put("r21", y0[1]);
                    });

                    async.execute(() -> {
                        double[] y0 = new double[]{0.0, 1.0, 0, 0};
                        final FirstOrderIntegrator integrator = new DormandPrince853Integrator(
                                minStep, maxStep, scaleAbsoluteTolerance, scaleRelativeTolerance);
                        integrator.integrate(firstOrderTransportMatrixEquation00, sStart, y0, sEnd, y0);
                        matrix.put("r12", y0[0]);
                        matrix.put("r22", y0[1]);
                    });

                    async.execute(() -> {
                        double[] y0 = new double[]{0.0, 0.0, 1.0, 0};
                        final FirstOrderIntegrator integrator = new DormandPrince853Integrator(
                                minStep, maxStep, scaleAbsoluteTolerance, scaleRelativeTolerance);
                        integrator.integrate(firstOrderTransportMatrixEquation00, sStart, y0, sEnd, y0);
                        matrix.put("r33", y0[2]);
                        matrix.put("r43", y0[3]);
                    });

                    async.execute(() -> {
                        double[] y0 = new double[]{0.0, 0.0, 0.0, 1.0};
                        final FirstOrderIntegrator integrator = new DormandPrince853Integrator(
                                minStep, maxStep, scaleAbsoluteTolerance, scaleRelativeTolerance);
                        integrator.integrate(firstOrderTransportMatrixEquation00, sStart, y0, sEnd, y0);
                        matrix.put("r34", y0[2]);
                        matrix.put("r44", y0[3]);
                    });

                    async.execute(() -> {
                        double[] y0 = new double[]{0.0, 0.0, 0.0, 0.0};
                        final FirstOrderIntegrator integrator = new DormandPrince853Integrator(
                                minStep, maxStep, scaleAbsoluteTolerance, scaleRelativeTolerance);
                        integrator.integrate(firstOrderTransportMatrixEquation10, sStart, y0, sEnd, y0);
                        matrix.put("r16", y0[0]);
                        matrix.put("r26", y0[1]);
                    });


                    async.waitForAllFinish(1, TimeUnit.HOURS);

                    matrix.put("r55", 1.);
                    matrix.put("r66", 1.);

                    return matrix;
                }


                public String solveAndFormMatrix() {
                    Map<String, Double> solve = solve();
                    StringBuilder stringBuilder = new StringBuilder("\n");
                    for (int i = 1; i <= 6; i++) {
                        for (int j = 1; j <= 6; j++) {
                            String key = "r" + i + j;
                            Double value = solve.getOrDefault(key, 0.);
                            stringBuilder.append(String.format("%+5.3f", value)).append("  ");
                        }
                        stringBuilder.append("\n");
                    }

                    return stringBuilder.toString();
                }

                public TransportCode.TransMatrix solveToMatrix() {
                    Map<String, Double> solve = solve();
                    double[][] r = new double[7][7];
                    for (int i = 1; i <= 6; i++) {
                        for (int j = 1; j <= 6; j++) {
                            String key = "r" + i + j;
                            Double value = solve.getOrDefault(key, 0.);
                            r[i][j] = value;
                        }
                    }

                    return TransportCode.TransMatrix.create(r);
                }
            }
        }
    }
}

