package zrx.DemoAndStudy;

// * layer1 1   AG-CCT
// * layer1 2
// * layer1 3   二极CCT
// * layer1 4

import Jama.Matrix;
import org.apache.commons.math3.ode.FirstOrderIntegrator;
import org.apache.commons.math3.ode.nonstiff.DormandPrince853Integrator;
import zrx.CCT.CCTUtils;
import zrx.CCT.ConcreteCCT.AllCCTs;
import zrx.CCT.ConcreteCCT.CurvedCCTAnalysis;
import zrx.CCT.ConcreteCCT.DiscreteCCT;
import zrx.CCT.ConcreteCCT.SingleLayerDiscreteCCTs;
import zrx.CCT.abstractCCT.CurvedCCT;
import zrx.ODE.FirstOrderEquations;
import zrx.Tools.*;
import zrx.base.CoordinateSystem3d;
import zrx.base.line2d.ArcLine;
import zrx.base.line2d.StraightLine;
import zrx.base.line2d.Trajectory;
import zrx.base.point.Vector2d;
import zrx.base.point.Vector2dTo3d;
import zrx.base.point.Vector3d;
import zrx.beam.tracking.*;
import zrx.beam.transport.Transport;
import zrx.python.Plot2d;
import zrx.python.Plot3d;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("all")
public class 六十七点五两个0821 {
    private static final double MM = 1e-3;//毫米
    private static final double MRAD = MM;//毫米

    private static final double DISTANCE = 0.3;//两个cct中间的漂移段

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
    private static int N12_OUT = 32;//  8.973/67.5*241 = 31.904
    private static int N12_MID = 46;//  12.8778/67.5*241 = 45.9785155555556
    private static int N12_IN = 85;//  23.7984/67.5*241 = 84.9691022222222

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
    private static double I1 = 5650;//5649.603
    private static double I2 = -I1;//AG-CCT
    private static double I3 = 5600;//5462.356
    private static double I4 = -I3;//2-CCT

    //四极场补偿——用于二极CCT
    private static double QuadGradient = 1.843;

    //类 w 量
    private static double PHI0 = ANGLERadian_675 / N34;

    //磁刚度
    private static final double Bp = 2.432128299585206;

    //外沿角度
    private static final double EDGE_ANGLE = 60.0;//度
    private static final double EDGE_RADIAN = AngleToRadian.to(EDGE_ANGLE);//度

    //步长
    private static double STEP_KSI = AngleToRadian.to(1.0);
    private static double STEP_TRAJ = 10 * MM;

    //cct前后偏移段
    /**
     * 数据来自线性光学
     * 3. 0.4 ;
     * 5. 0.27 2.33267879 30. /QG5/ ;
     * ( -10. 1. 1. 25 0.05 /FITG/ ;    )
     * <p>
     * <p>
     * 3. 0.48 ;
     * 5. 0.27 -0.314107536 30. /QG6/ ;
     * <p>
     * 3. 0.55  ;
     * --------------------------------------
     * 3. 0.2 /SCAs/;
     * 3. 0.7 /SCAe/;
     * 3. 0.2 /NOZs/;
     * 3. 0.8 /NOZe/;
     * 3. 0.5 /ISOC/;
     */
    private static final double DRIFTIN = (0.4 / 2 + 0.27 + 0.48 + 0.27) + 0.55;
    private static final double DRIFTOUT = (0.2 + 0.7 + 0.2 + 0.8) + 0.5;

    //好场区+-30mm
    private static double GOOD_FIELD_AREA = 30.0 * MM;

    //辅助量。中点 全圆弧轨迹 两段直线延长轨迹 角度值的自变量
    private static Vector3d midpoint3d = null;
    //    private static Vector3d[] trajectory = null;
    private static Vector3d[] trajectoryNEW = null;
    //    private static double[] trajectoryNEWLength = null;
//    private static List<Double> angList = null;
    private static double[] sList = null;
    private static Vector3d startPointParticle = null;//粒子运动开始点
    private static Vector3d startPointDirect = null;//粒子运动方向
    private static final double LENGTH = DRIFTIN + ANGLERadian_675 + DISTANCE + ANGLERadian_675 + DRIFTOUT;//理想轨迹总长度

//    private static final Vector3d IealEndDirect = Vector3d.getYDirect(-1);
    private static RunningParticle IdealEndParticle;
    private static CoordinateSystem3d IdealEndParticleCoordinate;

    //全新的辅助量，代替 trajectoryNEW 。使用trajectory类 2019年9月4日
    private static Trajectory trajectory2d = new Trajectory();
    private static Vector2dTo3d trajectory2dTo3d = v2 -> Vector3d.vector2dTo3d(v2);//厉害厉害

    //辅助量初始化
    static {
        midpoint3d = Vector3d.vector2dTo3d(
                Vector2d.rayForPolarAngle(ANGLERadian_675 / 2.0).changeLengthAndReturn(R_beamline));

        //轨迹和中点
//        midpoint3d = Vector3d.vector2dTo3d(
//                Vector2d.rayForPolarAngle(ANGLERadian_675 / 2.0).changeLengthAndReturn(R_beamline));
//        trajectory = Vector3d.vector2dTo3d(
//                Vector2d.arc(Vector2d.getZero(), R_beamline,
//                        -EDGE_RADIAN, ANGLERadian_675 + EDGE_RADIAN, false, STEP_KSI));
//        trajectoryNEW = ArrayMerge.merge(
//                Vector3d.interpolation(Vector3d.getByStartAndEnd(1, -1, 0), Vector3d.getByStartAndEnd(1, 0, 0), 500),
//                Vector3d.vector2dTo3d(
//                        Vector2d.arc(Vector2d.getZero(), R_beamline,
//                                0.0, ANGLERadian_675, false, STEP_KSI)),
//                Vector3d.interpolation(Vector3d.getByStartAndEnd(Math.cos(ANGLERadian_675),
//                        Math.sin(ANGLERadian_675), 0),
//                        Vector3d.getByStartAndEnd(-Math.cos(AngleToRadian.to(22.5)),
//                                Math.sin(AngleToRadian.to(22.5)), 0),
//                        1.0, 500)
//        );

        double a = ANGLERadian_675;
        double b = Math.PI - a * 2;
        double r = DISTANCE;

        double sina = Math.sin(a);
        double cosa = Math.cos(a);
        double rsina = r * sina;
        double rcosa = r * cosa;
        double sin2a = Math.sin(2.0 * a);
        double cos2a = Math.cos(2.0 * a);

        double sinb = Math.sin(b);
        double cosb = Math.cos(b);

        startPointParticle = Vector3d.getOne(cos2a - rsina - DRIFTIN * sinb, sin2a + rcosa - DRIFTIN * cosb);
        startPointDirect = Vector3d.subtract(Vector3d.getOne(cos2a - rsina, sin2a + rcosa), startPointParticle);


        trajectory2d.add(
                //第一段线段。CCT前漂移段
                StraightLine.getByStartAndDirectAndLength(
                        startPointParticle.projectToXY(), startPointDirect.projectToXY(), DRIFTIN)
        ).add(
                //第二段。第一个CCT圆弧
                ArcLine.getOne(
                        Vector2d.getOne(-rsina, rcosa),//圆心
                        R_beamline,//半径
                        2.0 * a, a, true
                )
        ).add(
                //第三段，中间的漂移段
                StraightLine.getByStartAndEnd(
                        Vector2d.getOne(cosa - rsina, sina + rcosa),
                        Vector2d.getOne(cosa, sina)
                )
        ).add(
                //第四段。第二个CCT
                ArcLine.getOne(
                        Vector2d.getZero(),//圆心
                        R_beamline,//半径
                        a, 0.0, true
                )
        ).add(
                //最后一段。出口漂移段
                StraightLine.getByStartAndDirectAndLength(
                        Vector2d.getOne(1.0, 0.0),
                        Vector2d.getYDirect().reverseSelfAndReturn(),
                        DRIFTOUT
                )
        );


        trajectoryNEW = ArrayMerge.merge(
                Vector3d.interpolation(
                        Vector3d.copyOne(startPointParticle),
                        Vector3d.getOne(cos2a - rsina, sin2a + rcosa),
                        (int) (DRIFTIN / STEP_TRAJ)
                ),
                Vector3d.vector2dTo3d(
                        Vector2d.arc(
                                Vector2d.getOne(-rsina, rcosa),
                                R_beamline,
                                2.0 * a, a, true, STEP_TRAJ
                        )
                ),
                Vector3d.interpolation(
                        Vector3d.getOne(cosa - rsina, sina + rcosa),
                        Vector3d.getOne(cosa, sina),
                        (int) (DISTANCE / STEP_TRAJ)
                ),
                Vector3d.vector2dTo3d(
                        Vector2d.arc(
                                Vector2d.getZero(),
                                R_beamline,
                                a, 0.0, true, STEP_TRAJ
                        )
                ),
                Vector3d.interpolation(
                        Vector3d.getOne(1.0, 0.0),
                        Vector3d.getOne(1.0, -DRIFTOUT),
                        (int) (DRIFTOUT / STEP_TRAJ)
                )
        );

        sList = new double[trajectoryNEW.length];
        sList[0] = 0.0;
        for (int i = 1; i < trajectoryNEW.length; i++) {
            sList[i] = sList[i - 1] +
                    Vector3d.subtract(
                            trajectoryNEW[i - 1],
                            trajectoryNEW[i]
                    ).length();
        }


        //角度 自变量
//        angList = new ArrayList<>(trajectory.length);
//        for (int i = 0; i < trajectory.length; i++) {
//            angList.add(AngleToRadian.reverse(Math.atan2(trajectory[i].y, trajectory[i].x)));
//        }

        IdealEndParticle = ParticleFactory.getProton250MeV(
                trajectory2dTo3d.functioon(trajectory2d.pointAt(trajectory2d.getLength())),
                trajectory2dTo3d.functioon(trajectory2d.endDirect())
        );

        IdealEndParticleCoordinate = CoordinateSystem3d.getOneByYZ(
                Vector3d.getOne(0,0,-1),
                IdealEndParticle.velocity
        );
    }

    public static void main(String[] args) {
        AllCCTs allCCTs = getAllCCTsFor675();
//        Plot3d.plot3(trajectoryNEW, Plot2d.BLACK_LINE);

        //Trajectory 类测试。测试通过
        trajectory2d.Plot3d(2 * MM, trajectory2dTo3d, Plot2d.RED_LINE);

        //简单磁场分布
//        Plot2d.plot2(CCTUtils.magneticDistributionZ(allCCTs, trajectoryNEW, sList), Plot2d.BLACK_LINE);
        //多级场分布
//        Plot2d.plot2(CCTUtils.magneticComponent0123(allCCTs, GOOD_FIELD_AREA, 0, trajectoryNEW, sList), Plot2d.BLACK_LINE);
//        Plot2d.plotPoints(CCTUtils.magneticComponent0123(allCCTs, GOOD_FIELD_AREA, 1, trajectoryNEW, sList), Plot2d.RED_LINE);
//        Plot2d.xLabel("s/m");
//        Plot2d.yLabel("G/Tm-1");

        //截面磁场分布
//        Plot2d.plot2(CCTUtils.magneticDistributionZ(allCCTs,Vector3d.interpolation(
//                Vector3d.copyOne(midpoint3d).setLengthAndReturn(R_beamline-GOOD_FIELD_AREA),
//                Vector3d.copyOne(midpoint3d).setLengthAndReturn(R_beamline+GOOD_FIELD_AREA),50
//        ),-GOOD_FIELD_AREA));

        //单粒子运动。生成粒子，开始运动
//        RunningParticle proton250MeV = ParticleFactory.getProton250MeV(startPointParticle, startPointDirect);
//        proton250MeV.runInAllCCTs(allCCTs, LENGTH, STEP_TRAJ, 10, Plot2d.RED_LINE);
//        proton250MeV.phaseSpaceParticle(IdealEndParticleCoordinate,IdealEndParticle);



        //enge函数拟合
//        engeFunction(allCCTs);//bug很多

        //多粒子运动，相椭圆分析
//        ellipseTracking(allCCTs);

        //传输矩阵
        transportMatrix(allCCTs);

        //COSY切片
//        final double[][] component0123Array = CCTUtils.magneticComponent0123Array(allCCTs, GOOD_FIELD_AREA, trajectoryNEW);
//        CCTUtils.component0123ArrayTOCosyCode(component0123Array, sList, Bp);


        //输出多级场分量 h k l
//        CCTUtils.getHKL(allCCTs,Bp,trajectory2d,trajectory2dTo3d,GOOD_FIELD_AREA,
//                new File("docs/fileOut/0904_135_hks/shkl10mm.txt"));


//        allCCTs.plot3d();
        Plot3d.setCube(1.0);
        Plot3d.removeAxis();
        Plot3d.setCenter(Vector3d.getOne(1, 0, 0), 2.0);
        Plot3d.showThread();
        Plot2d.equal();
        Plot2d.showThread();
    }

    private static void trajectory2dTest() {
        trajectory2d.Plot2d(2 * MM, Plot2d.BLACK_LINE);
        double s = 0.0;
        List<Vector2d> vector2ds = new ArrayList<>();
        while (s < trajectory2d.getLength()) {
            vector2ds.add(trajectory2d.rightHandSidePoint(s, 20 * MM));
            s += 1 * MM;
        }
        Plot2d.plot2(vector2ds.toArray(Vector2d[]::new), Plot2d.YELLOW_LINE);
    }

    private static void transportMatrix(AllCCTs allCCTs) {
        FirstOrderEquations ode00 = new FirstOrderEquations() {
            private double dp;


            private double[] fit(double s) {
                Vector3d startPoint = trajectory2dTo3d.functioon(trajectory2d.leftHandSidePoint(s, GOOD_FIELD_AREA));
                Vector3d endPoint = trajectory2dTo3d.functioon(trajectory2d.rightHandSidePoint(s, GOOD_FIELD_AREA));

                return allCCTs.magneticComponent012_ZhaoZeFengXYS(startPoint, endPoint);

            }

            @Override
            public double getH(double s) {
                return fit(s)[0]/Bp;
            }

            @Override
            public double getK(double s) {
                return fit(s)[1]/Bp;
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
            private double dp;

            private double[] fit(double s) {
                Vector3d startPoint = trajectory2dTo3d.functioon(trajectory2d.leftHandSidePoint(s, GOOD_FIELD_AREA));
                Vector3d endPoint = trajectory2dTo3d.functioon(trajectory2d.rightHandSidePoint(s, GOOD_FIELD_AREA));

                return allCCTs.magneticComponent012_ZhaoZeFengXYS(startPoint, endPoint);

            }

            @Override
            public double getH(double s) {
                return fit(s)[0]/Bp;
            }

            @Override
            public double getK(double s) {
                return fit(s)[1]/Bp;
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
        final double sEnd = trajectory2d.getLength()*0.0 + DRIFTIN +ANGLERadian_675 ;
        final double minStep = 1e-6;
        final double maxStep = 0.1;
        final double scalAbsoluteTolerance = 1e-4;
        final double scalRelativeTolerance = 1e-4;

        final Map<String, Double> matrix = new HashMap<>();


        final Thread thread1121 = new Thread(() -> {
            double[] y0 = new double[]{1.0, 0, 0, 0};
            final FirstOrderIntegrator integrator = new DormandPrince853Integrator(
                    minStep, maxStep, scalAbsoluteTolerance, scalRelativeTolerance);
            integrator.integrate(ode00, sStart, y0, sEnd, y0);
            synchronized (matrix) {
                matrix.put("r11", y0[0]);
                matrix.put("r21", y0[1]);
            }
        });

        final Thread thread1222 = new Thread(() -> {
            double[] y0 = new double[]{0.0, 1.0, 0, 0};
            final FirstOrderIntegrator integrator = new DormandPrince853Integrator(
                    minStep, maxStep, scalAbsoluteTolerance, scalRelativeTolerance);
            integrator.integrate(ode00, sStart, y0, sEnd, y0);
            synchronized (matrix) {
                matrix.put("r12", y0[0]);
                matrix.put("r22", y0[1]);
            }
        });

        final Thread thread3343 = new Thread(() -> {
            double[] y0 = new double[]{0.0, 0.0, 1.0, 0};
            final FirstOrderIntegrator integrator = new DormandPrince853Integrator(
                    minStep, maxStep, scalAbsoluteTolerance, scalRelativeTolerance);
            integrator.integrate(ode00, sStart, y0, sEnd, y0);
            synchronized (matrix) {
                matrix.put("r33", y0[2]);
                matrix.put("r43", y0[3]);
            }
        });

        final Thread thread3444 = new Thread(() -> {
            double[] y0 = new double[]{0.0, 0.0, 0.0, 1.0};
            final FirstOrderIntegrator integrator = new DormandPrince853Integrator(
                    minStep, maxStep, scalAbsoluteTolerance, scalRelativeTolerance);
            integrator.integrate(ode00, sStart, y0, sEnd, y0);
            synchronized (matrix) {
                matrix.put("r34", y0[2]);
                matrix.put("r44", y0[3]);
            }
        });

        final Thread thread1626 = new Thread(() -> {
            double[] y0 = new double[]{0.0, 0.0, 0.0, 0.0};
            final FirstOrderIntegrator integrator = new DormandPrince853Integrator(
                    minStep, maxStep, scalAbsoluteTolerance, scalRelativeTolerance);
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


        //——————————————————————————————————————
        final Matrix sigmaMatrix = Transport.getSigmaMatrixByTwiss(3.5 / 7.5, 7.5 / 3.5,
                3.5 / 7.5, 7.5 / 3.5,
                3.5 * 7.5 * 1e-6 / 100000000, 0.0);
        final Matrix transportMatrix = Transport.getTransportMatrixByMap(matrix);
        final Matrix sigma2 = Transport.transport(transportMatrix, sigmaMatrix);
        Transport.plot2dEllipseXXC(sigmaMatrix,Plot2d.YELLOW_LINE);
        Transport.plot2dEllipseXXC(sigma2,Plot2d.BLACK_LINE);


    }

    private static void ellipseTracking(AllCCTs allCCTs) {
        //注意，这里涉及粒子在不同坐标系中的表示，一为CCT直角坐标系，即粒子运动时用到的坐标系。
        //当设计相空间时，首先“建立/获取”一个坐标系，这个坐标系的原点是参考粒子，z方向指向运动方向，y方向垂直水平面，
        // x方向由xz经过右手系规则得到。利用这个坐标系和参考粒子的CCT坐标，就可以自由的切换待研究粒子的坐标。

        //—————————下面是一段完美的史诗—————————

        //得到参考粒子
        RunningParticle referredParticle = ParticleFactory.getProton250MeV(startPointParticle, startPointDirect);
        System.out.println("referredParticle = " + referredParticle);

        //参考粒子所处的轨道坐标系
        CoordinateSystem3d startCoordinateSystem3d = CoordinateSystem3d.getOneByYZ(
                Vector3d.getZDirect(-1), referredParticle.velocity);

        //由参考粒子和相椭圆参数得到需要研究的其他粒子
        RunningParticle[] studiedParticles = ParticleFactory.getEllipseParticles(referredParticle,
                startCoordinateSystem3d,//轨道坐标系
                PhaseSpaceDirect.X,//相椭圆位于XX'
                96,//生成粒子数目
                3.5 / 7.5,//beta
                7.5 / 3.5,//gamma
                3.5 * 7.5 * MM * MRAD,//epsi
                0.0);//动量分散

        //画出studiedParticles在相空间的图像，用于验证
        PhaseSpaceParticle.plot2dXXCParticlesInPhaseSpace(
                studiedParticles, referredParticle, startCoordinateSystem3d, Plot2d.YELLOW_POINT);

        //运动所有的粒子。会自动画三维轨迹图
        new BeamRun().addParticle(referredParticle, Plot2d.RED_LINE)
                .addParticles(studiedParticles, Plot2d.YELLOW_LINE)
                .setMotionParameters(allCCTs, LENGTH
                        , STEP_KSI, 100)
                .startThread();

        //————!!!!注意!!!!粒子运动完后referredParicle和studiedParticles都不再在起点，而是停留在终点，等待研究————
        System.out.println("referredParticle = " + referredParticle);

        //出口粒子坐标系
        final CoordinateSystem3d endCoordinateSystem3d = CoordinateSystem3d.getOneByYZ(
                Vector3d.getZDirect(-1), referredParticle.velocity);


        //绘制出口相椭圆
        PhaseSpaceParticle.plot2dXXCParticlesInPhaseSpace(
                studiedParticles, referredParticle, endCoordinateSystem3d, Plot2d.RED_POINT);

    }

    private static void engeFunction(AllCCTs allCCTs) {
        //二极场
        if (false) {
//        //二极场，第一段
//            final double[] engeFunction = CCTUtils.engeFunction(allCCTs, trajectoryNEW, sList,
//                    2.54, 1.77, 80 * MM, true,0);
//            Plot2d.plotCosyEngeFun(engeFunction,
//                    2.54,1.77,80*MM,true,Plot2d.RED_LINE);
//            PrintArray.print(engeFunction);


//        //二极场，第二段
//        final double[] engeFunction = CCTUtils.engeFunction(allCCTs, trajectoryNEW, sList,
//                2.54, 1.77+ANGLERadian_675, 80 * MM, false,0);
//        Plot2d.plotCosyEngeFun(engeFunction,
//                2.54,1.77+ANGLERadian_675,80*MM,false,Plot2d.RED_LINE);
//        PrintArray.print(engeFunction);

//        //二极场，第三段
//        final double[] engeFunction = CCTUtils.engeFunction(allCCTs, trajectoryNEW, sList,
//                2.54, 1.77+ANGLERadian_675+DISTANCE, 80 * MM, true,0);
//        Plot2d.plotCosyEngeFun(engeFunction,
//                2.54,1.77+ANGLERadian_675+DISTANCE,80*MM,true,Plot2d.RED_LINE);
//        PrintArray.print(engeFunction);

            //二极场，第四段
//        final double[] engeFunction = CCTUtils.engeFunction(allCCTs, trajectoryNEW, sList,
//                2.55, 1.77+ANGLERadian_675+DISTANCE+ANGLERadian_675, 80 * MM, false,0);
//        Plot2d.plotCosyEngeFun(engeFunction,
//                2.55,1.77+ANGLERadian_675+DISTANCE+ANGLERadian_675,80*MM,false,Plot2d.RED_LINE);
//        PrintArray.print(engeFunction);
        }

        //四极场
        if (true) {
            final double[] engeFunction = CCTUtils.engeFunction(allCCTs, trajectoryNEW, sList,
                    43.4, 1.77 + ANGLERadian_675 + DISTANCE + ANGLERadian_675, 80 * MM, true, 1);
            PrintArray.print(engeFunction);
            //ANGLERadian_675+DISTANCE+ANGLERadian_675
            Plot2d.plotCosyEngeFun(engeFunction,
                    43.4, 1.77 + ANGLERadian_675 + DISTANCE + ANGLERadian_675, 80 * MM, true, Plot2d.RED_LINE);
            PrintArray.print(engeFunction);
        }

    }

    private static AllCCTs getAllCCTsFor675() {
        final AllCCTs allCCTs = new AllCCTs();
        allCCTs.add(cct1());
        allCCTs.add(cct2());
        allCCTs.add(cct3());
        allCCTs.add(cct4());


        allCCTs.addAll(
                AllCCTs.copyAndMove(
                        AllCCTs.copyAndRotate(allCCTs, ANGLERadian_675),
                        Vector3d.vector2dTo3d(Vector2d.getYDirect().rotateSelfAndReturn(ANGLERadian_675)),
                        DISTANCE
                )
        );


        System.out.println("allCCTs.size() = " + allCCTs.size());

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
            //                0.2, Vector2d.getByStartAndEnd(0, 0.001),
            //                0.3, Math.PI / 180.0 * 10, Math.PI / 18000);
            //        //---------------------------12&13连接/ 34连接 45连接-----------------------
            //        ConnectionSegmentOfCCT connect23 = CurvedCCTAnalysis.connect(dcct2, dcct3,
            //                0.2, Vector2d.getByStartAndEnd(0, -0.002),
            //                0.3, Math.PI / 180.0 * 30.0, Math.PI / 18000);
            //        ConnectionSegmentOfCCT connect34 = CurvedCCTAnalysis.connect(dcct3, dcct4,
            //                0.2, Vector2d.getByStartAndEnd(0, 0.001),
            //                0.3, Math.PI / 180.0 * 10, Math.PI / 18000);
            //        ConnectionSegmentOfCCT connect45 = CurvedCCTAnalysis.connect(dcct4, dcct5,
            //                0.2, Vector2d.getByStartAndEnd(0, -0.002),
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

    private static Vector3d pointAtTrajectoryOfS(double s) {
        double a = ANGLERadian_675;
        double b = Math.PI - a * 2;
        double r = DISTANCE;

        double sina = Math.sin(a);
        double cosa = Math.cos(a);
        double rsina = r * sina;
        double rcosa = r * cosa;
        double sin2a = Math.sin(2.0 * a);
        double cos2a = Math.cos(2.0 * a);

        double sinb = Math.sin(b);
        double cosb = Math.cos(b);

        //startPointParticle = Vector3d.getByStartAndEnd(cos2a - rsina - DRIFTIN * sinb, sin2a + rcosa - DRIFTIN * cosb);
        // startPointDirect = Vector3d.subtract(Vector3d.getByStartAndEnd(cos2a - rsina, sin2a + rcosa), startPointParticle);

        if (s <= DRIFTIN) {
            Vector3d driftInEnd = Vector3d.getOne(cos2a - rsina, sin2a + rcosa);
            Vector3d direct = Vector3d.subtract(driftInEnd, startPointParticle);
            return Vector3d.move(
                    Vector3d.copyOne(startPointParticle), Vector3d.copyOne(startPointDirect), s
            );
        } else if (s <= DRIFTIN + ANGLERadian_675) {
            s -= DRIFTIN;
            return Vector3d.vector2dTo3d(
                    Vector2d.getOne(Math.cos(a + s) - rsina, Math.sin(a + s) + rcosa)
            );
        } else if (s <= DRIFTIN + ANGLERadian_675 + DISTANCE) {
            s -= DRIFTIN + ANGLERadian_675;
            Vector3d arc1End = Vector3d.vector2dTo3d(Vector2d.getOne(cosa - rsina, sina + rcosa));
            Vector3d arc2Start = Vector3d.vector2dTo3d(Vector2d.getOne(cosa, sina));
            return Vector3d.move(
                    Vector3d.copyOne(arc1End),
                    Vector3d.subtract(arc2Start, arc1End), s
            );
        } else if (s <= DRIFTIN + ANGLERadian_675 + DISTANCE + ANGLERadian_675) {
            s -= DRIFTIN + ANGLERadian_675 + DISTANCE;
            return Vector3d.vector2dTo3d(
                    Vector2d.getOne(Math.cos(s), Math.sin(s))
            );
        } else {
            s -= DRIFTIN + ANGLERadian_675 + DISTANCE + ANGLERadian_675;
            return Vector3d.vector2dTo3d(Vector2d.getOne(1.0, -s));
        }
    }
}
