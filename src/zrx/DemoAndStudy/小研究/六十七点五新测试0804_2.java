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
import zrx.base.CoordinateSystem3d;
import zrx.base.Vector2d;
import zrx.base.Vector3d;
import zrx.beam.ParticleFactory;
import zrx.beam.PhaseSpaceParticle;
import zrx.beam.RunningParticle;
import zrx.python.Plot2d;
import zrx.python.Plot3d;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.*;

/**
 * layer1 1   AG-CCT
 * layer1 2
 * layer1 3   二极CCT
 * layer1 4
 * <p>
 * 2019年8月9日
 * 二极场已经调到最优。主要关注积分场和中心点的四极场补偿
 * 积分场 —— 靠调节电流 private static final double I3 = -5449.9;
 * 四极场补偿 —— private static final double QuadGradient = -1.843;
 */

@SuppressWarnings("all")
public class 六十七点五新测试0804_2 {
    private static final double MM = 1e-3;//毫米
    private static final double MRAD = MM;//毫米

    //各项参数
    private static final double R_beamline = 1.00;//束流偏转半径
    private static double R_CCT = 1.00254;//CCT偏转半径 避开AG-CCT轴向上的二极场
    private static final double ANGLE_675 = 67.5;
    private static final double ANGLERadian_675 = AngleToRadian.to(ANGLE_675);

    //倾斜角30度，没有问题，参见国内博士论文
    private static final double tiltAngle1 = AngleToRadian.to(30.0);
    private static final double tiltAngle2 = AngleToRadian.to(-30.0);
    private static final double tiltAngle3 = AngleToRadian.to(30.0);
    private static final double tiltAngle4 = AngleToRadian.to(-30.0);

    //二极CCT匝数235~左右，问题不大，和几篇文献比较后没有大问题。由此计算得到平均匝间距 5.013mm
    private static final int N34 = 241;

    //AG-CCT 角度 9度，12.9度，23.8度，12.9度，9度
    private static final double ANGLE_OUT = 8.973;
    private static final double ANGLE_MID = 12.8778;
    private static final double ANGLE_IN = ANGLE_675 - (ANGLE_OUT + ANGLE_MID) * 2;//==23.7984
    //匝数 初始值 32 46 85
    private static int N12_OUT = 27;//  8.973/67.5*241 = 31.904
    private static int N12_MID = 39;//  12.8778/67.5*241 = 45.9785155555556
    private static int N12_IN = 109;//  23.7984/67.5*241 = 84.9691022222222

    //CCT厚度，层间缝隙，每层的内外径。从内到外 1 2 3 4
    //厚度 7mm 6mm 间隙3mm
    private static final double THICKNESS12 = 7 * MM;//12层 厚度 AG-CCT 7mm
    private static final double THICKNESS34 = 6 * MM;//34层 厚度 2-CCT 6mm
    private static final double GAP_LAYER = 3 * MM;//间隔缝隙 3mm
    //层1 内外径和建模半径——因为建模是单根线
    private static final double RADIUS_IN1 = 50 * MM;//1层 内径 50mm
    private static final double RADIUS_OUT1 = RADIUS_IN1 + THICKNESS12;//1层 外径 57mm
    private static final double RADIUS1 = (RADIUS_IN1 + RADIUS_OUT1) / 2.0; // 0.0535
    //层2
    private static final double RADIUS_IN2 = RADIUS_OUT1 + GAP_LAYER;//2层 内径 60mm
    private static final double RADIUS_OUT2 = RADIUS_IN2 + THICKNESS12;//2层 外径 67mm
    private static final double RADIUS2 = (RADIUS_IN2 + RADIUS_OUT2) / 2.0; // 0.0635
    //层3
    private static final double RADIUS_IN3 = RADIUS_OUT2 + GAP_LAYER;//3层 内径 70mm
    private static final double RADIUS_OUT3 = RADIUS_IN3 + THICKNESS34;//3层 外径 76mm
    private static final double RADIUS3 = (RADIUS_IN3 + RADIUS_OUT3) / 2.0;
    //层4
    private static final double RADIUS_IN4 = RADIUS_OUT3 + GAP_LAYER;//4层 内径 79mm
    private static final double RADIUS_OUT4 = RADIUS_IN4 + THICKNESS34;//4层 外径 85mm
    private static final double RADIUS4 = (RADIUS_IN4 + RADIUS_OUT4) / 2.0;

    //电流
    private static double I1 = 5574.6;
    private static final double I2 = -I1;
    private static double I3 = -5450.5;
    private static final double I4 = -I3;

    //四极场补偿——用于二极CCT
    private static final double QuadGradient = -1.843;

    //类 w 量
    private static double PHI0 = ANGLERadian_675 / N34;

    //磁刚度
    private static final double Bp = 2.432128299585206;
    //外沿角度
    private static final double EDGE_ANGLE = 60.0;//度
    private static final double EDGE_RADIAN = AngleToRadian.to(EDGE_ANGLE);//度

    //步长
    private static double STEP_KSI = AngleToRadian.to(1.0);

    //好场区+-30mm
    private static double GOOD_FIELD_AREA = 30.0 * MM;

    //辅助量。中点 全圆弧轨迹 两段直线延长轨迹 角度值的自变量
    private static Vector3d midpoint3d = null;
    private static Vector3d[] trajectory = null;
    private static Vector3d[] trajectoryNEW = null;
//    private static double[] trajectoryNEWLength = null;
    private static List<Double> angList = null;

    //辅助量初始化
    static {
        //轨迹和中点
        midpoint3d = Vector3d.vector2dTo3d(
                Vector2d.rayForPolarAngle(ANGLERadian_675 / 2.0).changeLengthAndReturn(R_beamline));
        trajectory = Vector3d.vector2dTo3d(
                Vector2d.arc(Vector2d.getZeros(), R_beamline,
                        -EDGE_RADIAN, ANGLERadian_675 + EDGE_RADIAN, false, STEP_KSI));
        trajectoryNEW = ArrayMerge.merge(
                Vector3d.interpolation(Vector3d.getOne(1, -1, 0), Vector3d.getOne(1, 0, 0), 500),
                Vector3d.vector2dTo3d(
                        Vector2d.arc(Vector2d.getZeros(), R_beamline,
                                0.0, ANGLERadian_675, false, STEP_KSI)),
                Vector3d.interpolation(Vector3d.getOne(Math.cos(ANGLERadian_675),
                        Math.sin(ANGLERadian_675), 0),
                        Vector3d.getOne(-Math.cos(AngleToRadian.to(22.5)),
                                Math.sin(AngleToRadian.to(22.5)), 0),
                        1.0, 500)
        );
        //角度 自变量
        angList = new ArrayList<>(trajectory.length);
        for (int i = 0; i < trajectory.length; i++) {
            angList.add(AngleToRadian.reverse(Math.atan2(trajectory[i].y, trajectory[i].x)));
        }
    }


    public static void main(String[] args) throws Exception {
        final AllCCTs allCCTs = getAllCCTsFor675();

//        Plot3d.plot3(trajectory, Plot2d.BLACK_LINE);
        Plot3d.plot3(trajectoryNEW, Plot2d.BLACK_LINE);
//        Plot3d.plot3(Vector3d.move(trajectoryNEW,midpoint3d,R_CCT-R_beamline),Plot2d.RED_LINE);

        //轴向基本磁场计算
//        zDirectMagnetBasic(allCCTs);
        //轴向各极场分布
//        zDirectComponent0123(allCCTs, 0,Plot2d.BLACK_LINE);
//        zDirectComponent0123(allCCTs, 1, Plot2d.RED_LINE);
        //trajectoryNEW轨道上二极场积分场
//        IntegralFieldNEW(allCCTs);
        //trajectory轨道上二极场积分场
//        IntegralField(allCCTs);
        //中点处各极场分量
//        FieldDistributionAMidPoint(allCCTs);
        //R_CCT 和 R_beamline 关系——得出R_CCT=1.00254 2019年8月8日
//        changeTheR_CCT();
        //传输矩阵
//        transportMatrix(allCCTs);
        //单粒子跟踪
//        particleTracking(allCCTs,250);
        //相椭圆跟踪
        ellipseTracking(allCCTs);
//        尝试优化1();


//        allCCTs.plot3d();
        Plot3d.setCube(1.0);
        Plot3d.removeAxis();
//        Plot3d.setCenter(Vector3d.getOne(1,0,0),2.0);
        Plot3d.showThread();
        Plot2d.equal();
        Plot2d.showThread();
    }

    private static void 尝试优化1() throws  Exception{
        //总结一下用于粗调的物理量——共四个

        //电流
        //private static double I1 = 5574.6;
        //private static double I3 = -5450.5;
        //匝数 初始值 32 46 85
        //private static int N12_OUT = 32;//  8.973/67.5*241 = 31.904
        //private static int N12_MID = 46;//  12.8778/67.5*241 = 45.9785155555556
        //private static int N12_IN = 85;//  23.7984/67.5*241 = 84.9691022222222
        final File file = new File("n12-1-120-opt.txt");
        file.createNewFile();
        System.setOut(new PrintStream(new FileOutputStream(file)));

        System.out.println("<table border=\"1px\">");

        for (int n12out = 1; n12out < 120; n12out++) {
            for (int n12mid = 1; n12mid < 120; n12mid++) {
//                for (int i = 0; i < 3; i++) {

                N12_OUT = n12out;
                N12_MID = n12mid;
                N12_IN = N34 - 2 * (N12_OUT + N12_MID);
                if (N12_IN <= 0)
                    continue;
                final AllCCTs allCCTsFor675 = getAllCCTsFor675();
                System.err.println("正在打印"+new Date().toString());
                System.out.print("<tr>");

                System.out.print("<td>" + N12_OUT + "</td>");
                System.out.print("<td>" + N12_MID + "</td>");
                System.out.print("<td>" + N12_IN + "</td>");
                System.out.print("<td>" + 225 + "</td>");
                System.out.print("<td>" + particleTracking(allCCTsFor675, 225) + "</td>");
                System.out.print("<td>" + 250 + "</td>");
                System.out.print("<td>" + particleTracking(allCCTsFor675, 250) + "</td>");
                System.out.print("<td>" + 275 + "</td>");
                System.out.print("<td>" + particleTracking(allCCTsFor675, 275) + "</td>");

//                    switch (i){
//                        case 0:
//                            System.out.print("<td>"+225+"</td>");
//                            System.out.print("<td>"+particleTracking(allCCTsFor675,225)+"</td>");
//                            break;
//                        case 1:
//                            System.out.print("<td>"+250+"</td>");
//                            System.out.print("<td>"+particleTracking(allCCTsFor675,250)+"</td>");
//                            break;
//                        case 2:
//                            System.out.print("<td>"+275+"</td>");
//                            System.out.print("<td>"+particleTracking(allCCTsFor675,275)+"</td>");
//                            break;
//                    }


                System.out.println("</tr>");
//                }

            }
        }

        System.out.println("</table>");


        //<table border="1px">
        //	<tr>
        //		<td>1</td>
        //		<td>2</td>
        //		<td>3</td>
        //		<td>4</td>
        //		<td>5</td>
        //	</tr>
        //</table>
    }

    private static void ellipseTracking(AllCCTs allCCTs) throws Exception {
        //回顾相椭圆的知识
        //  相椭圆方程
        //    gama*x^2+2alph*y*y'+beta*y'^2=epsi
        //  参数关系
        //    beta*gama-alph^2=1

        //椭圆参数
        final double beta = 3.5 / 7.5;
        final double gama = 7.5 / 3.5;
        final double alpha = Math.sqrt(Math.abs(beta * gama - 1.0));
        final double epsi = 3.5 * 7.5 * MM * MRAD;
        // 1-x 2-y
        final int flag = 1;
        //生成粒子数目
        final int number = 12;
        //粒子局部坐标系
        final CoordinateSystem3d startCoordinateSystem3d = CoordinateSystem3d.getOneByYZ(
                Vector3d.getZDirect(), Vector3d.getYDirect());
        //每个粒子的运动长度
        final double LENGTH = 1.0 * 2 + 67.5 / 180.0 * Math.PI;
        //步长
        final double STEP = 1.0 * MM;
        //threads
        List<Thread> threads = new ArrayList<>(number + 1);


        //首先得到2维的相椭圆圆周分布点
        final Vector2d[] ellipsePoints = Vector2d.ellipsePoints(gama, 2 * alpha, beta, epsi, number);
//        Plot2d.plot2(points,Plot2d.YELLOW_POINT);

        //参考粒子
        RunningParticle referredParticle = ParticleFactory.getProton(
                Vector3d.getOne(1, -1, 0),//position
                Vector3d.getOne(0, 1, 0),//direction
//                250
                225
        );
        //复制参考粒子。复制后再修改为待研究的粒子
        final RunningParticle[] particles = ParticleFactory.copy(referredParticle, number);

        switch (flag) {
            case 1:
                for (int i = 0; i < number; i++) {
                    particles[i].deploy(startCoordinateSystem3d, ellipsePoints[i].x, ellipsePoints[i].y, 0, 0, 0);
//                    Plot3d.plot3Particle(particles[i], 0.1);
                }
                break;
            case 2:
                for (int i = 0; i < number; i++) {
                    particles[i].deploy(
                            startCoordinateSystem3d, 0,0, ellipsePoints[i].x, ellipsePoints[i].y, 0);
//                    Plot3d.plot3Particle(particles[i], 0.1);
                }
                break;
        }

        //初始相椭圆
        PhaseSpaceParticle.plot2dXXCParticlesInPhaseSpace(
                particles, referredParticle, startCoordinateSystem3d, Plot2d.YELLOW_POINT);

        //运动参考粒子
        threads.add(
                referredParticle.runInAllCCTsThread(allCCTs, LENGTH, STEP, 100, Plot2d.RED_DASH));
        //运动粒子s
        threads.addAll(ParticleFactory.runInAllCCTsThread(
                particles, allCCTs, LENGTH, STEP, 100, Plot2d.YELLOW_LINE)
        );

        //join
        threads.forEach(t -> {
            try {
                t.join();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        //出口粒子坐标系
        final CoordinateSystem3d endCoordinateSystem3d = CoordinateSystem3d.getOneByYZ(
                Vector3d.getZDirect(), referredParticle.velocity);
        System.out.println("endCoordinateSystem3d = " + endCoordinateSystem3d);

        //出口相椭圆
        PhaseSpaceParticle.plot2dXXCParticlesInPhaseSpace(
                particles, referredParticle, endCoordinateSystem3d, Plot2d.RED_POINT);


    }

    private static double particleTracking(AllCCTs allCCTs, double kineticMeV) {
        //粒子生成
        RunningParticle particle = ParticleFactory.getProton(
                Vector3d.getOne(1, -1, 0),//position
                Vector3d.getOne(0, 1, 0),//direction
                kineticMeV
        );
        //运动长度
        final double LENGTH = 1.0 * 2 + 67.5 / 180.0 * Math.PI;
        //步长
        final double STEP = 1.0 * MM;
        //画图
        final int number = 1000000;

        //记录初始方向
        final Vector2d startDierct = Vector2d.copyOne(Vector3d.vector3dTo2d(particle.velocity));

        //记录积分场
        double inteB = 0.0;

        int i = 0;
        List<Vector3d> traj = new ArrayList<>((int)(LENGTH/STEP)+100);
        while (particle.distance < LENGTH) {
            //画图
//            if (i++ % number == 0) {
//                particle.plot3self(Plot2d.YELLOW_SMALL_POINT);
//            }
            traj.add(Vector3d.copyOne(particle.position));

            //计算磁场
            Vector3d magnet = allCCTs.magnet(particle.position);
            //积分场
            inteB += magnet.z * STEP;

            //粒子运动
            particle.runSelf(magnet, STEP);
        }

        Plot3d.plot3(traj.toArray(Vector3d[]::new),Plot2d.RED_DASH);

        //最后粒子的方向
        final Vector2d endDierct = Vector2d.copyOne(Vector3d.vector3dTo2d(particle.velocity));

//        System.out.println("粒子最后运动方向：" + particle.velocity.setLengthAndReturn(1.0));
//        System.out.println("粒子偏转角度：" + AngleToRadian.reverse(Vector2d.angle(startDierct, endDierct)));
        return AngleToRadian.reverse(Vector2d.angle(startDierct, endDierct));
//        System.out.println("粒子轨迹上感受的积分场：" + inteB);
//        System.out.println("出口粒子z坐标(理论值0.0)：" + particle.position.z);

    }

    private static void transportMatrix(AllCCTs allCCTs) {
        //成神
        FirstOrderEquations ode00 = new FirstOrderEquations() {
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
                    return Vector3d.vector2dTo3d(Vector2d.pointFromArcToXY(R_beamline, 0.0, s));
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
                                        changeLengthAndReturn(GOOD_FIELD_AREA)
                        )
                );
                final Vector3d point2 = Vector3d.add(currentPoint,
                        Vector3d.vector2dTo3d(
                                direct.projectToXY().
                                        rotateSelfAndReturn(-Math.PI / 2.0).
                                        changeLengthAndReturn(GOOD_FIELD_AREA)
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
        FirstOrderEquations ode10 = new FirstOrderEquations() {
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
                    return Vector3d.vector2dTo3d(Vector2d.pointFromArcToXY(R_beamline, 0.0, s));
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
                                        changeLengthAndReturn(GOOD_FIELD_AREA)
                        )
                );
                final Vector3d point2 = Vector3d.add(currentPoint,
                        Vector3d.vector2dTo3d(
                                direct.projectToXY().
                                        rotateSelfAndReturn(-Math.PI / 2.0).
                                        changeLengthAndReturn(GOOD_FIELD_AREA)
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
        } catch (Exception e) {
            e.printStackTrace();
        }


        System.out.println(matrix);

//        FirstOrderIntegrator integrator = new DormandPrince853Integrator(
//                1.0e-5, 0.1, 1.0e-3, 1.0e-3);
//
//        Map<String, Double> matrix = new HashMap<>();
//        ode.setDp(0.0);
//        double[] y0 = new double[]{1.0, 0, 0, 0};
//        integrator.integrate(ode, sStart, y0, sEnd, y0);
//        matrix.put("r11", y0[0]);
//        matrix.put("r21", y0[1]);
//
//
//        y0 = new double[]{0.0, 1.0, 0, 0};
//        integrator.integrate(ode, sStart, y0, sEnd, y0);
//        matrix.put("r12", y0[0]);
//        matrix.put("r22", y0[1]);
//
//        y0 = new double[]{0.0, 0.0, 1.0, 0};
//        integrator.integrate(ode, sStart, y0, sEnd, y0);
//        matrix.put("r33", y0[2]);
//        matrix.put("r43", y0[3]);
//
//        y0 = new double[]{0.0, 0.0, 0.0, 1.0};
//        integrator.integrate(ode, sStart, y0, sEnd, y0);
//        matrix.put("r34", y0[2]);
//        matrix.put("r44", y0[3]);
//
//        ode.setDp(1.0);
//        y0 = new double[]{0.0, 0.0, 0.0, 0.0};
//        integrator.integrate(ode, sStart, y0, sEnd, y0);
//        matrix.put("r16", y0[0]);
//        matrix.put("r26", y0[1]);
//
//        System.out.println(matrix);
    }

    private static void changeTheR_CCT() {
        double inner = R_beamline - 0.01;
        double outer = R_beamline + 0.01;
        int number = 50;
        final double[] linspace = Numpy.linspace(inner, outer, number);
        PrintArray.print(linspace);
        List<Vector2d> plotV = new ArrayList<>(linspace.length);
        for (double r : linspace) {
            R_CCT = r;
            final AllCCTs allCCTsFor675 = getAllCCTsFor675();
            final double mag = FieldDistributionAMidPoint(allCCTsFor675)[0];
            final Vector2d plotPoint = Vector2d.getOne(r, mag);
            plotV.add(plotPoint);
            Plot2d.plotPoint(plotPoint, Plot2d.RED_POINT);
        }

        Plot2d.plot2(plotV.toArray(Vector2d[]::new), Plot2d.BLACK_LINE);
    }

    private static Vector2d[] zDirectComponent0123(AllCCTs allCCTs, int degree, String describe) {
        double inner = R_beamline - GOOD_FIELD_AREA / 100.0;
        double outer = R_beamline + GOOD_FIELD_AREA / 100.0;

        double[] componentList = new double[trajectory.length];
        for (int i = 0; i < trajectory.length; i++) {
            final Vector3d innerP = Vector3d.getOne(Vector2d.getOneByLength(Vector3d.vector3dTo2d(trajectory[i]), inner));
            final Vector3d outerP = Vector3d.getOne(Vector2d.getOneByLength(Vector3d.vector3dTo2d(trajectory[i]), outer));

            componentList[i] = allCCTs.magneticComponent0123(innerP, outerP)[degree];
        }

        final Vector2d[] ans = Vector2d.arrayToVector2ds(ListToArray.doubleListToArray(angList), componentList);
        Plot2d.plot2(ans, describe);

        //画理想四极场分布
        if (degree == 1) {
            double g = 42.97;
            Plot2d.plotGREY_DASH(
                    Vector2d.getOne(-EDGE_ANGLE, 0.0),
                    Vector2d.getOne(0.0, 0.0),
                    Vector2d.getOne(0.0, g),
                    Vector2d.getOne(ANGLE_675 * (double) N12_OUT / (double) N34, g),
                    Vector2d.getOne(ANGLE_675 * (double) N12_OUT / (double) N34, -g),
                    Vector2d.getOne(ANGLE_675 * (double) (N12_OUT + N12_MID) / (double) N34, -g),
                    Vector2d.getOne(ANGLE_675 * (double) (N12_OUT + N12_MID) / (double) N34, g),
                    Vector2d.getOne(ANGLE_675 * (double) (N12_OUT + N12_MID + N12_IN) / (double) N34, g),
                    Vector2d.getOne(ANGLE_675 * (double) (N12_OUT + N12_MID + N12_IN) / (double) N34, -g),
                    Vector2d.getOne(ANGLE_675 * (double) (N12_OUT + N12_MID * 2 + N12_IN) / (double) N34, -g),
                    Vector2d.getOne(ANGLE_675 * (double) (N12_OUT + N12_MID * 2 + N12_IN) / (double) N34, g),
                    Vector2d.getOne(ANGLE_675 * (double) (N12_OUT * 2 + N12_MID * 2 + N12_IN) / (double) N34, g),
                    Vector2d.getOne(ANGLE_675 * (double) (N12_OUT * 2 + N12_MID * 2 + N12_IN) / (double) N34, 0),
                    Vector2d.getOne(ANGLE_675 * (double) (N12_OUT * 2 + N12_MID * 2 + N12_IN) / (double) N34 + EDGE_ANGLE, 0)
            );
        }


        return ans;
    }

    private static double[] FieldDistributionAMidPoint(AllCCTs allCCTs) {
        double inner = R_beamline - GOOD_FIELD_AREA;
        double outer = R_beamline + GOOD_FIELD_AREA;

        Vector3d innerPoint = Vector3d.getOne(midpoint3d, inner);
        Vector3d outerPoint = Vector3d.getOne(midpoint3d, outer);

        final double[] magneticComponent0123 = allCCTs.magneticComponent0123(innerPoint, outerPoint);
        System.out.println("中点处各极场分量：");
        PrintArray.print(magneticComponent0123);

        Plot2d.plot2(Numpy.linspace(inner, outer, 20)
                , AnalyseCCT.magnetZeAlongTrajectory(
                        allCCTs,
                        Vector3d.interpolation(innerPoint, outerPoint, 20)), Plot2d.BLACK_LINE
        );

        return magneticComponent0123;
    }

    private static double IntegralFieldNEW(AllCCTs allCCTs) {
        double inteB = 0.0;
        for (int i = 0; i < trajectoryNEW.length - 1; i++) {
            double deltaLen = Vector3d.subtract(trajectoryNEW[i], trajectoryNEW[i + 1]).length();
            inteB += allCCTs.magnet(trajectoryNEW[i]).z * deltaLen;
        }
        System.out.println("trajectoryNEW 积分场 = " + inteB);

        return inteB;
    }

    private static double IntegralField(AllCCTs allCCTs) {
        double inteB = 0.0;
        for (int i = 0; i < trajectory.length - 1; i++) {
            double deltaLen = Vector3d.subtract(trajectory[i], trajectory[i + 1]).length();
            inteB += allCCTs.magnet(trajectory[i]).z * deltaLen;
        }
        System.out.println("trajectory积分场 = " + inteB);

        return inteB;
    }

    private static void zDirectMagnetBasic(AllCCTs allCCTs) {
        //XY单独处理
        Plot2d.plot2(ListToArray.doubleListToArray(angList),
                AnalyseCCT.magnetZeAlongTrajectory(allCCTs, trajectory), Plot2d.BLACK_LINE);
        //硬板模型
//                Plot2d.plotGREY_DASH(
//                        Vector2d.getOne(-EDGE_ANGLE, 0.0),
//                        Vector2d.getOne(0.0, 0.0),
//                        Vector2d.getOne(0.0, -2.43),
//                        Vector2d.getOne(ANGLE_675, -2.43),
//                        Vector2d.getOne(ANGLE_675, 0.0),
//                        Vector2d.getOne(EDGE_ANGLE + ANGLE_675, 0.0)
//                );
    }

    private static AllCCTs getAllCCTsFor675() {
        final AllCCTs allCCTs = new AllCCTs();
        allCCTs.add(cct1());
        allCCTs.add(cct2());
        allCCTs.add(cct3());
        allCCTs.add(cct4());

//        System.out.println("allCCTs.size() = " + allCCTs.size());

        return allCCTs;
    }

    private static SingleLayerDiscreteCCTs cct1() {
        //冗余写法
        if (false) {
            final CurvedCCT cct11 = CurvedCCTAnalysis.curvedCCTFactory(
                    R_CCT, RADIUS1, N12_OUT, I1, 2.0, PHI0, tiltAngle1, STEP_KSI
            );
            final DiscreteCCT discreteCCT11 = CurvedCCTAnalysis.discrete(cct11);

            final CurvedCCT cct12 = CurvedCCT.reverseWinding(cct11);
            cct12.setN(N12_MID);
            final DiscreteCCT discreteCCT12 = CurvedCCTAnalysis.discrete(cct12);

            final CurvedCCT cct13 = CurvedCCT.reverseWinding(cct12);
            cct13.setN(N12_IN);
            final DiscreteCCT discreteCCT13 = CurvedCCTAnalysis.discrete(cct13);

            final CurvedCCT cct14 = CurvedCCT.reverseWinding(cct13);
            cct14.setN(N12_MID);
            final DiscreteCCT discreteCCT14 = CurvedCCTAnalysis.discrete(cct14);

            final CurvedCCT cct15 = CurvedCCT.reverseWinding(cct14);
            cct15.setN(N12_OUT);
            final DiscreteCCT discreteCCT15 = CurvedCCTAnalysis.discrete(cct15);

            return new SingleLayerDiscreteCCTs(I1, discreteCCT11, discreteCCT12, discreteCCT13, discreteCCT14, discreteCCT15);
        }

        //新的api
        if (true) {
            final CurvedCCT cct11 = CurvedCCTAnalysis.curvedCCTFactory(
                    R_CCT, RADIUS1, N12_OUT, I1, 2.0, PHI0, tiltAngle1, STEP_KSI
            );

//            System.out.println("cct11.getA() = " + cct11.getA());

            final DiscreteCCT[] discreteCCTS = CurvedCCTAnalysis.curvedCCTFactoryForAG_CCT(cct11,
                    N12_MID, N12_IN, N12_MID, N12_OUT);
            return new SingleLayerDiscreteCCTs(I1, discreteCCTS);
        }

        //参考资料
        if (false) {
            ////---------------------------11-----------------------
            //        CurvedCCT cct11 = CurvedCCTAnalysis.curvedCCTFactory(
            //                R, r1, n12_15, I1, 2, phi012, tiltAngle1, stepKsi);
            //        DiscreteCCT dcct1 = CurvedCCTAnalysis.discrete(cct11);
            //        //---------------------------12-----------------------
            //        CurvedCCT cct12 = CurvedCCT.reverseWinding(cct11);
            //        cct12.setN(n12_24);
            //        DiscreteCCT dcct2 = CurvedCCTAnalysis.discrete(cct12);
            //        //---------------------------13-----------------------
            //        CurvedCCT cct13 = CurvedCCT.reverseWinding(cct12);
            //        cct13.setN(n12_3);
            //        DiscreteCCT dcct3 = CurvedCCTAnalysis.discrete(cct13);
            //        //---------------------------14-----------------------
            //        CurvedCCT cct14 = CurvedCCT.reverseWinding(cct13);
            //        cct14.setN(n12_24);
            //        DiscreteCCT dcct4 = CurvedCCTAnalysis.discrete(cct14);
            //        //---------------------------14-----------------------
            //        CurvedCCT cct15 = CurvedCCT.reverseWinding(cct14);
            //        cct15.setN(n12_15);
            //        DiscreteCCT dcct5 = CurvedCCTAnalysis.discrete(cct15);
            //
            //        //---------------------------11&12连接-----------------------
            //        ConnectionSegmentOfCCT connect12 = CurvedCCTAnalysis.connect(dcct1, dcct2,
            //                0.2, Vector2d.getOne(0, 0.001),
            //                0.3, Math.PI / 180.0 * 10, Math.PI / 18000);
            //        //---------------------------12&13连接/ 34连接 45连接-----------------------
            //        ConnectionSegmentOfCCT connect23 = CurvedCCTAnalysis.connect(dcct2, dcct3,
            //                0.2, Vector2d.getOne(0, -0.002),
            //                0.3, Math.PI / 180.0 * 30.0, Math.PI / 18000);
            //        ConnectionSegmentOfCCT connect34 = CurvedCCTAnalysis.connect(dcct3, dcct4,
            //                0.2, Vector2d.getOne(0, 0.001),
            //                0.3, Math.PI / 180.0 * 10, Math.PI / 18000);
            //        ConnectionSegmentOfCCT connect45 = CurvedCCTAnalysis.connect(dcct4, dcct5,
            //                0.2, Vector2d.getOne(0, -0.002),
            //                0.3, Math.PI / 180.0 * 30.0, Math.PI / 18000);
            //
            //        SingleLayerDiscreteCCTs singleLayerDiscreteCCTs = new SingleLayerDiscreteCCTs(I1,
            //                dcct1, connect12, dcct2, connect23, dcct3, connect34, dcct4, connect45, dcct5);
            //        return singleLayerDiscreteCCTs;
        }

        return null;
    }

    private static SingleLayerDiscreteCCTs cct2() {
        final CurvedCCT cct21 = CurvedCCTAnalysis.curvedCCTFactory(
                R_CCT, RADIUS2, N12_OUT, I2, 2.0, PHI0, tiltAngle2, STEP_KSI
        );

        final DiscreteCCT[] discreteCCTS = CurvedCCTAnalysis.curvedCCTFactoryForAG_CCT(cct21,
                N12_MID, N12_IN, N12_MID, N12_OUT);
        return new SingleLayerDiscreteCCTs(I2, discreteCCTS);
    }

    private static SingleLayerDiscreteCCTs cct3() {
        CurvedCCT cct3 = CurvedCCTAnalysis.curvedCCTFactory(
                R_CCT, RADIUS3, N34, I3, 1, PHI0, tiltAngle3, STEP_KSI);
        DiscreteCCT dcct3 = CurvedCCTAnalysis.discreteWithFixDipole(cct3, QuadGradient / 2.0);
        return new SingleLayerDiscreteCCTs(I3, dcct3);
    }

    private static SingleLayerDiscreteCCTs cct4() {
        CurvedCCT cct4 = CurvedCCTAnalysis.curvedCCTFactory(
                R_CCT, RADIUS4, N34, I4, 1, PHI0, tiltAngle4, STEP_KSI);
        DiscreteCCT dcct4 = CurvedCCTAnalysis.discreteWithFixDipole(cct4, QuadGradient / 2.0);
        return new SingleLayerDiscreteCCTs(I4, dcct4);
    }
}
