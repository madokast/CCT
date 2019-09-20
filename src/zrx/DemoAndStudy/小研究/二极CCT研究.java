package zrx.DemoAndStudy.小研究;

import org.apache.commons.math3.ode.FirstOrderIntegrator;
import org.apache.commons.math3.ode.nonstiff.DormandPrince853Integrator;
import zrx.CCT.AnalyseCCT;
import zrx.CCT.ConcreteCCT.AllCCTs;
import zrx.CCT.ConcreteCCT.CurvedCCTAnalysis;
import zrx.CCT.ConcreteCCT.DiscreteCCT;
import zrx.CCT.ConcreteCCT.SingleLayerDiscreteCCTs;
import zrx.CCT.abstractCCT.CurvedCCT;
import zrx.ODE.FirstOrderEquations;
import zrx.Tools.*;
import zrx.base.point.Vector2d;
import zrx.base.point.Vector3d;
import zrx.beam.tracking.ParticleFactory;
import zrx.beam.tracking.RunningParticle;
import zrx.python.Plot2d;
import zrx.python.Plot3d;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.DoubleFunction;

/**
 * 研究时间 2019年7月19日
 * 目的：建立简单的弯曲二级铁CCT
 * 研究二级铁CCT是否在 极点 a 点，没有四极场
 * 利用赵泽峰的ODE方法，求出这个CCY的传输矩阵。
 * 虽然赵泽峰同志的代码令人发指，但是我还是看懂了的
 * ~~~喵
 * <p>
 * 重现博士论文 第九章
 */

@SuppressWarnings("all")
public class 二极CCT研究 {
    //CCT参数
    private static final double R = 1.0;//大半径
    private static final double ANGLEangle = 67.5;
    private static final double ANGLE = AngleToRadian.to(ANGLEangle);

    //倾斜角30度，没有问题，参见国内博士论文
    private static final double tiltAngle3 = AngleToRadian.to(30.0);
    private static final double tiltAngle4 = AngleToRadian.to(-30.0);

    //匝数235，问题不大，和几篇文献比较后没有大问题
    //由此计算得到平均匝间距 5.013mm
    private static final int N34 = 235;

    //线槽尺寸 英文为 channel 2.0——宽度 6——径向深度
    private static final double 线槽尺寸 = 2.0 / 6.0;

    //关于CCT厚度间距的问题
    //   内层|* *|  |* *|
    //         |      |
    //        66mm   75mm
    //宽度|* *|设为6，即线的宽度，channel，然后中心处间距9mm
    private static final double r3mid = 73.0 / 1000;
    private static final double r4mid = 82.0 / 1000;
    private static final double Rbore = 40.0 / 1000;

    //电流
    private static final double I3 = -5576;
    private static final double I4 = -I3;

    //四极场补偿
    private static final double QuadGradient = -1.848;

    //类 w 量
    private static double phi034 = ANGLE / N34;

    //磁刚度
    private static final double Bp = 2.432128299585206;
    //外沿角度
    private static final double EDGEangle = 60.0;//度
    private static final double EDGE = AngleToRadian.to(EDGEangle);//度

    //步长
    private static double stepKsi = AngleToRadian.to(1.0);

    //好场区+-30mm
    private static double GoodFieldErea = 30.0 / 1000;


    public static void main(String[] args) {
        Timer.invoke();
        final SingleLayerDiscreteCCTs cct3 = cct3();
        final SingleLayerDiscreteCCTs cct4 = cct4();
        final AllCCTs allCCTs = new AllCCTs(cct3, cct4);

        //轨迹trajectory全圆弧，要废弃。trajectoryNEW好东西
        final Vector3d[] trajectory = Vector3d.vector2dTo3d(
                Vector2d.arc(Vector2d.getZero(), R/*Math.sqrt(R*R-r3mid*r4mid*/,
                        -EDGE, ANGLE + EDGE, false, stepKsi));
        final Vector3d midpoint3d = Vector3d.vector2dTo3d(Vector2d.rayForPolarAngle(ANGLE / 2.0));
        final Vector3d[] trajectoryNEW = ArrayMerge.merge(
                Vector3d.interpolation(Vector3d.getOne(1, -1, 0), Vector3d.getOne(1, 0, 0), 500),
                Vector3d.vector2dTo3d(
                        Vector2d.arc(Vector2d.getZero(), R,
                                0.0, ANGLE, false, stepKsi)),
                Vector3d.interpolation(Vector3d.getOne(Math.cos(AngleToRadian.to(67.5)),
                        Math.sin(AngleToRadian.to(67.5)), 0),
                        Vector3d.getOne(-Math.cos(AngleToRadian.to(22.5)),
                                Math.sin(AngleToRadian.to(22.5)), 0),
                        1.0, 500)
        );
//        Plot3d.plot3(trajectory, Plot2d.BLACK_LINE);
        Plot3d.plot3(trajectoryNEW, Plot2d.BLACK_LINE);

        //角度 自变量
        List<Double> angList = new ArrayList<>(trajectory.length);
        for (int i = 0; i < trajectory.length; i++) {
            angList.add(AngleToRadian.reverse(Math.atan2(trajectory[i].y, trajectory[i].x)));
        }

        //径向磁场分布
        if (false) {
            //旧式 写一堆代码
            if (false) {
                final double maxLen = R + Rbore;
                final double minLen = R - Rbore;
                final double delta = 0.5 / 1000;
                List<Double> xList = new ArrayList<>();
                List<Double> BzList = new ArrayList<>();

                double currentLen = minLen;
                while (currentLen < maxLen) {
                    xList.add(currentLen - R);

                    midpoint3d.setLength(currentLen);
                    BzList.add(allCCTs.magnet(midpoint3d).z);

                    currentLen += delta;
                }

                //画图
//            Plot2d.plot2(xList, BzList, Plot2d.BLACK_LINE);

                //拟合
                final double[] fit = Fit.fit(xList, BzList, 2);
                PrintArray.print(fit);
            }
            //新式 新的API
            if (true) {
                //磁场分布~
                final double maxLen = R + Rbore;
                final double minLen = R - Rbore;
                final int number = 11;

                final Vector2d[] xBz = AnalyseCCT.magnetZeAlongLine(
                        allCCTs, -Rbore, Rbore,
                        midpoint3d.setLengthAndReturn(minLen),
                        midpoint3d.setLengthAndReturn(maxLen),
                        number
                );

                Plot2d.plot2(xBz, Plot2d.BLACK_LINE);

                final double[] fit = Fit.fit(xBz, 2);
                PrintArray.print(fit);
            }
        }
        //基本磁场计算
        if (true) {
            //繁琐版
            if (false) {
                //磁场计算
                List<Double> bzList = new ArrayList<>(trajectory.length);
                for (int i = 0; i < trajectory.length; i++) {
                    bzList.add(
                            allCCTs.magnet(trajectory[i]).z
                    );
                }

                Plot2d.plot2(angList, bzList, Plot2d.BLACK_LINE);

                //硬边模型
                Plot2d.plotGREY_DASH(
                        Vector2d.getOne(-EDGEangle, 0.0),
                        Vector2d.getOne(0.0, 0.0),
                        Vector2d.getOne(0.0, -2.43),
                        Vector2d.getOne(ANGLEangle, -2.43),
                        Vector2d.getOne(ANGLEangle, 0.0),
                        Vector2d.getOne(EDGEangle + ANGLEangle, 0.0)
                );
            }

            //new api
            if (true) {
                //XY单独处理
//                Plot2d.plot2(ListToArray.doubleListToArray(angList),
//                        AnalyseCCT.magnetZeAlongTrajectory(allCCTs,trajectory),Plot2d.BLACK_LINE);
//                //硬板模型
//                Plot2d.plotGREY_DASH(
//                        Vector2d.getByStartAndEnd(-EDGEangle, 0.0),
//                        Vector2d.getByStartAndEnd(0.0, 0.0),
//                        Vector2d.getByStartAndEnd(0.0, -2.43),
//                        Vector2d.getByStartAndEnd(ANGLEangle, -2.43),
//                        Vector2d.getByStartAndEnd(ANGLEangle, 0.0),
//                        Vector2d.getByStartAndEnd(EDGEangle + ANGLEangle, 0.0)
//                );


                //轨迹-s处理绘图
                final Vector2d[] orinalData = Vector2d.convict(
                        Vector2d.walk(AnalyseCCT.magnetZeAlongTrajectoryWithS(allCCTs, trajectory),
                                Vector2d.getOne(-1, 0)),
                        x -> (-x / 0.08), y -> (-y / 2.43));
                final Vector2d[] data = Vector2d.convict(
                        orinalData, x -> x,
                        new DoubleFunction<Double>() {
                            @Override
                            public Double apply(double value) {
                                if (value > 1.0) value = 1.0;
                                if (value < 0.0) value = 0.0;
                                return value;
                            }
                        }
                );

                //辅助线
                if (true) {
                    Plot2d.plotGREY_DASH(
                            Vector2d.getOne(-3, 1.1),
                            Vector2d.getOne(-3, -0.1)
                    );
                    Plot2d.plotGREY_DASH(
                            Vector2d.getOne(5, 1.1),
                            Vector2d.getOne(5, -0.1)
                    );
                    Plot2d.plotPINK_DASH(
                            Vector2d.getOne(0.0, 1.1),
                            Vector2d.getOne(0.0, -0.1)
                    );

                    Plot2d.plotGREY_DASH(
                            Vector2d.getOne(-4, 0.0),
                            Vector2d.getOne(6, 0.0)
                    );
                    Plot2d.plotGREY_DASH(
                            Vector2d.getOne(-4, 1.0),
                            Vector2d.getOne(6, 1.0)
                    );
                }

                //拟合enge函数
                final double[] engeFunction = Fit.fitEngeFunction(data);
//                PrintArray.print(data);
                System.out.println("-----");
                PrintArray.print(engeFunction);
                System.out.println("-----");
                Plot2d.plot2(orinalData,
                        Plot2d.BLACK_LINE);


                Plot2d.plotCosyEngeFun(engeFunction, Plot2d.RED_LINE);

            }
        }
        //粒子追踪。测试成功
        if (false) {
            if (false) {
                //粒子生成
                RunningParticle particle = ParticleFactory.getProton(
                        Vector3d.getOne(1, -0.5, 0),
                        Vector3d.getOne(0, 1, 0), 250
                );
                //运动长度
                final double LENGTH = 2.0;
                //步长
                final double STEP = 0.001;

                //记录轨迹的数组
                final ArrayList<Vector3d> particleTraj = new ArrayList<>((int) (LENGTH / STEP));

                //运动
                while (particle.distance < LENGTH) {
                    particleTraj.add(new Vector3d(particle.position));
                    particle.runSelf(allCCTs.magnet(particle.position), STEP);
//                System.out.println("particle.position = " + particle.position);
                }

                //画轨迹
                Plot3d.plot3(Vector3d.listToArray(particleTraj), Plot2d.YELLOW_LINE);
            }
            //new api
            if (true) {
                //粒子生成
                RunningParticle particle = ParticleFactory.getProton(
                        Vector3d.getOne(1, -0.5, 0),
                        Vector3d.getOne(0, 1, 0), 250
                );

                final Vector3d[] traj = AnalyseCCT.particleRunning(allCCTs, particle, 2.0, 0.001);
                Plot3d.plot3(traj, Plot2d.YELLOW_LINE);
            }
        }
        //高阶场拟合.轨迹平移法
        if (false) {
            //[0][1][2][3]分别是二极场 四极场 六级场 八级场
            final int N = 0;

            //好场区+-30mm
            //对中心轨迹移动
            //位移方向
            //平移
            final Vector3d[] innerTraj = Vector3d.move(trajectory, midpoint3d, -GoodFieldErea);
            final Vector3d[] outterTraj = Vector3d.move(trajectory, midpoint3d, GoodFieldErea);
            //画图确认
            Plot3d.plot3(innerTraj, Plot2d.YELLOW_LINE);
            Plot3d.plot3(outterTraj, Plot2d.YELLOW_LINE);

            //磁场拟合
            List<Double> bznList = new ArrayList<>(trajectory.length);
            for (int i = 0; i < trajectory.length; i++) {
                angList.add(
                        AngleToRadian.reverse(Math.atan2(trajectory[i].y, trajectory[i].x)
                        ));
                bznList.add(
                        allCCTs.magneticComponent0123(innerTraj[i], outterTraj[i])[N]
                );
            }

            //画图
            Plot2d.plot2(angList, bznList, Plot2d.RED_LINE);
        }
        //高阶场拟合.轨迹外推法
        if (false) {
            //内线和外线
            final Vector3d[] innerTrajectory = new Vector3d[trajectory.length];
            final Vector3d[] outerTrajectory = new Vector3d[trajectory.length];

            for (int i = 0; i < trajectory.length - 1; i++) {
                innerTrajectory[i] = Vector3d.add(trajectory[i],
                        Vector3d.vector2dTo3d(
                                Vector2d.subtract(trajectory[i + 1].projectToXY(), trajectory[i].projectToXY())
                                        .rotateSelfAndReturn(Math.PI / 2)
                                        .changeLengthAndReturn(GoodFieldErea)
                        )
                );
            }
            innerTrajectory[innerTrajectory.length - 1] = Vector3d.add(trajectory[trajectory.length - 1],
                    Vector3d.vector2dTo3d(
                            Vector2d.subtract(trajectory[trajectory.length - 1].projectToXY(),
                                    trajectory[trajectory.length - 2].projectToXY())
                                    .rotateSelfAndReturn(Math.PI / 2)
                                    .changeLengthAndReturn(GoodFieldErea)
                    )
            );


            for (int i = 0; i < trajectory.length - 1; i++) {
                outerTrajectory[i] = Vector3d.add(trajectory[i],
                        Vector3d.vector2dTo3d(
                                Vector2d.subtract(trajectory[i + 1].projectToXY(), trajectory[i].projectToXY())
                                        .rotateSelfAndReturn(-Math.PI / 2)
                                        .changeLengthAndReturn(GoodFieldErea)
                        )
                );
            }
            outerTrajectory[outerTrajectory.length - 1] = Vector3d.add(trajectory[trajectory.length - 1],
                    Vector3d.vector2dTo3d(
                            Vector2d.subtract(trajectory[trajectory.length - 1].projectToXY(),
                                    trajectory[trajectory.length - 2].projectToXY())
                                    .rotateSelfAndReturn(-Math.PI / 2)
                                    .changeLengthAndReturn(GoodFieldErea)
                    )
            );


            Plot3d.plot3(innerTrajectory, Plot2d.YELLOW_LINE);
            Plot3d.plot3(outerTrajectory, Plot2d.RED_LINE);

            Plot3d.plotLastPoint(innerTrajectory, Plot2d.BLACK_POINT);
            Plot3d.plotLastPoint(outerTrajectory, Plot2d.BLACK_POINT);
            Plot3d.plotLastPoint(trajectory, Plot2d.BLACK_POINT);

            //拟合~~
            List<Double> hList = new ArrayList<>(trajectory.length);
            List<Double> kList = new ArrayList<>(trajectory.length);
            List<Double> lList = new ArrayList<>(trajectory.length);

            for (int i = 0; i < trajectory.length; i++) {
                final double[] doubles = allCCTs.magneticComponent0123(innerTrajectory[i], outerTrajectory[i]);
                hList.add(i, doubles[0]);
                kList.add(i, doubles[1]);
                lList.add(i, doubles[2]);
            }

            //画图
            Plot2d.plot2(angList, hList, Plot2d.YELLOW_LINE);

        }
        //传输矩阵计算
        if (false) {
            /**
             * 实现它，就可以掌控雷电
             */
            final FirstOrderEquations ode00 = new FirstOrderEquations() {
                private final double deltaS = 1.0 / 1000.0 / 100.0;
                private double dp;

                /**
                 * 轨道上s处的点
                 * @param s 位置
                 * @return 点
                 */
                public Vector3d pointAtTrajectoryOfS(double s) {
//                    return Vector3d.vector2dTo3d(Vector2d.pointFromArcToXY(R, -EDGE, s));
                    if (s <= 1) {
                        return Vector3d.vector2dTo3d(Vector2d.getOne(1.0, s - 1.0));
                    } else if (s <= 1 + 67.5 / 180.0 * Math.PI) {
                        s -= 1.0;
                        return Vector3d.vector2dTo3d(Vector2d.pointFromArcToXY(R, 0.0, s));
                    } else {
                        s -= 1 + 67.5 / 180.0 * Math.PI;
                        return Vector3d.vector2dTo3d(Vector2d.walk(
                                Vector2d.getOne(Math.cos(AngleToRadian.to(67.5)),
                                        Math.sin(AngleToRadian.to(67.5))),
                                Vector2d.getOne(-Math.cos(AngleToRadian.to(22.5)),
                                        Math.sin(AngleToRadian.to(22.5))),
                                s
                        ));
                    }
                }

                /**
                 * 轨道s处的切线
                 * @param s 位置
                 * @return 切向量
                 */
                private Vector3d directAtTrajectoryOfS(double s) {
                    final Vector3d currentPoint = pointAtTrajectoryOfS(s);
                    final Vector3d pointAhead = pointAtTrajectoryOfS(s + deltaS);
                    return Vector3d.subtract(pointAhead, currentPoint);
                }

                /**
                 * 插值
                 * @param s 位置
                 * @return 插值值
                 */
                private double[] fit(double s) {
                    final Vector3d currentPoint = pointAtTrajectoryOfS(s);
                    final Vector3d direct = directAtTrajectoryOfS(s);
                    final Vector3d point1 = Vector3d.add(currentPoint,
                            Vector3d.vector2dTo3d(
                                    direct.projectToXY().
                                            rotateSelfAndReturn(Math.PI / 2.0).
                                            changeLengthAndReturn(GoodFieldErea)
                            )
                    );
                    final Vector3d point2 = Vector3d.add(currentPoint,
                            Vector3d.vector2dTo3d(
                                    direct.projectToXY().
                                            rotateSelfAndReturn(-Math.PI / 2.0).
                                            changeLengthAndReturn(GoodFieldErea)
                            )
                    );
                    return allCCTs.magneticComponent01(point1, point2);

                }

                @Override
                public double getH(double s) {
                    return fit(s)[0];
                }

                @Override
                public double getK(double s) {
                    return fit(s)[1];
                }

                /**
                 * @return 动量分散
                 */
                @Override
                public double getDP() {
                    return dp;
                }

                @Override
                public void setDp(double dp) {
                    this.dp = dp;
                }
            };
            ode00.setDp(0.0);
            final FirstOrderEquations ode10 = new FirstOrderEquations() {
                private final double deltaS = 1.0 / 1000.0 / 100.0;
                private double dp;

                /**
                 * 轨道上s处的点
                 * @param s 位置
                 * @return 点
                 */
                public Vector3d pointAtTrajectoryOfS(double s) {
//                    return Vector3d.vector2dTo3d(Vector2d.pointFromArcToXY(R, -EDGE, s));
                    if (s <= 1) {
                        return Vector3d.vector2dTo3d(Vector2d.getOne(1.0, s - 1.0));
                    } else if (s <= 1 + 67.5 / 180.0 * Math.PI) {
                        s -= 1.0;
                        return Vector3d.vector2dTo3d(Vector2d.pointFromArcToXY(R, 0.0, s));
                    } else {
                        s -= 1 + 67.5 / 180.0 * Math.PI;
                        return Vector3d.vector2dTo3d(Vector2d.walk(
                                Vector2d.getOne(Math.cos(AngleToRadian.to(67.5)),
                                        Math.sin(AngleToRadian.to(67.5))),
                                Vector2d.getOne(-Math.cos(AngleToRadian.to(22.5)),
                                        Math.sin(AngleToRadian.to(22.5))),
                                s
                        ));
                    }
                }

                /**
                 * 轨道s处的切线
                 * @param s 位置
                 * @return 切向量
                 */
                private Vector3d directAtTrajectoryOfS(double s) {
                    final Vector3d currentPoint = pointAtTrajectoryOfS(s);
                    final Vector3d pointAhead = pointAtTrajectoryOfS(s + deltaS);
                    return Vector3d.subtract(pointAhead, currentPoint);
                }

                /**
                 * 插值
                 * @param s 位置
                 * @return 插值值
                 */
                private double[] fit(double s) {
                    final Vector3d currentPoint = pointAtTrajectoryOfS(s);
                    final Vector3d direct = directAtTrajectoryOfS(s);
                    final Vector3d point1 = Vector3d.add(currentPoint,
                            Vector3d.vector2dTo3d(
                                    direct.projectToXY().
                                            rotateSelfAndReturn(Math.PI / 2.0).
                                            changeLengthAndReturn(GoodFieldErea)
                            )
                    );
                    final Vector3d point2 = Vector3d.add(currentPoint,
                            Vector3d.vector2dTo3d(
                                    direct.projectToXY().
                                            rotateSelfAndReturn(-Math.PI / 2.0).
                                            changeLengthAndReturn(GoodFieldErea)
                            )
                    );
                    return allCCTs.magneticComponent01(point1, point2);

                }

                @Override
                public double getH(double s) {
                    return fit(s)[0];
                }

                @Override
                public double getK(double s) {
                    return fit(s)[1];
                }

                /**
                 * @return 动量分散
                 */
                @Override
                public double getDP() {
                    return dp;
                }

                @Override
                public void setDp(double dp) {
                    this.dp = dp;
                }
            };
            ode10.setDp(1.0);

            final double sStart = 0.0;
            final double sEnd = 2 + 67.5 / 180.0 * Math.PI;

            //pointAtTrajectoryOfS测试
            if (false) {
                System.out.println("sEnd = " + sEnd);
                double SCurrent = sStart;
                while (SCurrent < sEnd) {
                    Plot3d.plotPoint(ode00.pointAtTrajectoryOfS(SCurrent), Plot2d.YELLOW_POINT);
                    SCurrent += 1.0 / 10;
                    System.out.println("SCurrent = " + SCurrent);
                }
            }

            final Map<String, Double> matrix = new HashMap<>();


            final Thread thread1121 = new Thread(() -> {
                double[] y0 = new double[]{1.0, 0, 0, 0};
                final FirstOrderIntegrator integrator = new DormandPrince853Integrator(
                        1.0e-5, 0.1, 1.0e-3, 1.0e-3);
                integrator.integrate(ode00, sStart, y0, sEnd, y0);
                synchronized (matrix) {
                    matrix.put("r11", y0[0]);
                    matrix.put("r21", y0[1]);
                }
            });

            final Thread thread1222 = new Thread(() -> {
                double[] y0 = new double[]{0.0, 1.0, 0, 0};
                final FirstOrderIntegrator integrator = new DormandPrince853Integrator(
                        1.0e-5, 0.1, 1.0e-3, 1.0e-3);
                integrator.integrate(ode00, sStart, y0, sEnd, y0);
                synchronized (matrix) {
                    matrix.put("r12", y0[0]);
                    matrix.put("r22", y0[1]);
                }
            });

            final Thread thread3343 = new Thread(() -> {
                double[] y0 = new double[]{0.0, 0.0, 1.0, 0};
                final FirstOrderIntegrator integrator = new DormandPrince853Integrator(
                        1.0e-5, 0.1, 1.0e-3, 1.0e-3);
                integrator.integrate(ode00, sStart, y0, sEnd, y0);
                synchronized (matrix) {
                    matrix.put("r33", y0[2]);
                    matrix.put("r43", y0[3]);
                }
            });

            final Thread thread3444 = new Thread(() -> {
                double[] y0 = new double[]{0.0, 0.0, 0.0, 1.0};
                final FirstOrderIntegrator integrator = new DormandPrince853Integrator(
                        1.0e-5, 0.1, 1.0e-3, 1.0e-3);
                integrator.integrate(ode00, sStart, y0, sEnd, y0);
                synchronized (matrix) {
                    matrix.put("r34", y0[2]);
                    matrix.put("r44", y0[3]);
                }
            });

            final Thread thread1626 = new Thread(() -> {
                double[] y0 = new double[]{0.0, 0.0, 0.0, 0.0};
                final FirstOrderIntegrator integrator = new DormandPrince853Integrator(
                        1.0e-5, 0.1, 1.0e-3, 1.0e-3);
                integrator.integrate(ode10, sStart, y0, sEnd, y0);
                synchronized (matrix) {
                    matrix.put("r16", y0[0]);
                    matrix.put("r26", y0[1]);
                }
            });

            thread1121.start();
            thread1222.start();
            thread3343.start();
            thread3444.start();
            thread1626.start();

            try {
                thread1121.join();
                thread1222.join();
                thread3343.join();
                thread3444.join();
                thread1626.join();
            }catch (Exception e){e.printStackTrace();}



            System.out.println(matrix);


//            double[] y0 = {1.0, 0, 0, 0}; //r11 r21
//            double[] y0 = {0.0, 1.0, 0, 0}; //r12 r22
//            double[] y0 = {0.0, 0.0, 1.0, 0}; //r33 r43
//            y0 = new double[]{0.0, 0.0, 0.0, 1.0}; //r33 r43
//            double[] y0 = {0.0, 0.0, 0.0, 0.0}; //r33 r43

//            integrator.integrate(ode, sStart, y0, sEnd, y0);
//            PrintArray.print(y0);

        }
        //积分场。理想值-2.862776306
        if (false) {
            double inteB = 0.0;
            for (int i = 0; i < trajectoryNEW.length - 1; i++) {
                double deltaLen = Vector3d.subtract(trajectoryNEW[i], trajectoryNEW[i + 1]).length();
                inteB += allCCTs.magnet(trajectoryNEW[i]).z * deltaLen;
            }
            System.out.println("deltaLen = " + inteB);
            //  -2.847205450874661
        }
        //积分场均匀度。轨道平移法
        if (false) {
            //11个点平移看看
            final int num = 11;
            //平移距离
            final double[] moveLens = Numpy.linspace(-GoodFieldErea, GoodFieldErea, num);
            //答案存放低
            Vector2d[] result = new Vector2d[num];

            for (int i = 0; i < num; i++) {
                double moveLen = moveLens[i];
                Vector3d[] trajectoryCurrent = Vector3d.move(trajectoryNEW, midpoint3d, moveLen);
//                Plot3d.plot3(trajectoryCurrent, Plot2d.BLACK_LINE);

                //开始计算
                double inteB = 0.0;
                for (int j = 0; j < trajectoryCurrent.length - 1; j++) {
                    double deltaLen = Vector3d.subtract(trajectoryCurrent[j], trajectoryCurrent[j + 1]).length();
                    inteB += allCCTs.magnet(trajectoryCurrent[j]).z * deltaLen;
                }

                result[i] = Vector2d.getOne(moveLen, inteB);
            }

            PrintArray.print(result);
            Plot2d.plot2(result, Plot2d.BLACK_LINE);
        }
        //0726 理想磁场传输矩阵计算，用以验证
        if (false) {
            //理想磁场
            FirstOrderEquations ode = new FirstOrderEquations() {
                @Override
                public double getH(double s) {
                    if (s <= 1) {
                        return 0.0;
                    } else if (s <= 1 + 67.5 / 180.0 * Math.PI) {
                        return 1.00;
                    } else {
                        return 0.0;
                    }
                }

                @Override
                public double getK(double s) {
                    return 0.0;
                }

                @Override
                public double getDP() {
                    return 1.0;
                }
            };
            final double sStart = 0.0;
            final double sEnd = 2 + 67.5 / 180.0 * Math.PI;
//            double[] y0 = {1.0, 0, 0, 0}; //r11 r21
//            double[] y0 = {0.0, 1.0, 0, 0}; //r12 r22
//            double[] y0 = {0.0, 0.0, 1.0, 0}; //r33 r43
//            double[] y0 = {0.0, 0.0, 0.0, 1.0}; //r33 r43
            double[] y0 = {0.0, 0.0, 0.0, 0.0}; //r33 r43
            FirstOrderIntegrator integrator = new DormandPrince853Integrator(
                    1.0e-5, 0.1, 1.0e-3, 1.0e-3);
            integrator.integrate(ode, sStart, y0, sEnd, y0);
            PrintArray.print(y0);
        }


        //画图
        cct3.Plot3d(Plot2d.RED_LINE);
//        cct4.Plot3d(Plot2d.BLUE_LINE);

        Plot3d.setCube(0.5);
//        Plot3d.showThread();
        Plot2d.showThread();
        Timer.invoke();

    }

    private static SingleLayerDiscreteCCTs cct3() {
        CurvedCCT cct3 = CurvedCCTAnalysis.curvedCCTFactory(
                R, r3mid, N34, I3, 1, phi034, tiltAngle3, stepKsi);
        DiscreteCCT dcct3 = CurvedCCTAnalysis.discreteWithFixDipole(cct3, QuadGradient / 2.0);
        return new SingleLayerDiscreteCCTs(I3, dcct3);
    }

    private static SingleLayerDiscreteCCTs cct4() {
        CurvedCCT cct4 = CurvedCCTAnalysis.curvedCCTFactory(
                R, r4mid, N34, I4, 1, phi034, tiltAngle4, stepKsi);
        DiscreteCCT dcct4 = CurvedCCTAnalysis.discreteWithFixDipole(cct4, QuadGradient / 2.0);
        return new SingleLayerDiscreteCCTs(I4, dcct4);
    }
}
