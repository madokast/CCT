package zrx.DemoAndStudy;

import Jama.Matrix;
import org.apache.commons.math3.analysis.function.Min;
import org.apache.commons.math3.fitting.PolynomialCurveFitter;
import org.apache.commons.math3.fitting.WeightedObservedPoint;
import org.apache.commons.math3.fitting.WeightedObservedPoints;
import zrx.CCT.ConcreteCCT.ConnectionSegmentOfCCT;
import zrx.CCT.ConcreteCCT.CurvedCCTAnalysis;
import zrx.CCT.ConcreteCCT.DiscreteCCT;
import zrx.CCT.ConcreteCCT.SingleLayerDiscreteCCTs;
import zrx.CCT.Magnet;
import zrx.CCT.abstractCCT.CurvedCCT;
import zrx.Tools.*;
import zrx.base.Vector2d;
import zrx.base.Vector3d;
import zrx.beam.ParticleFactory;
import zrx.beam.RunningParticle;
import zrx.python.Plot2d;
import zrx.python.Plot3d;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;

import static zrx.Tools.Persistence.read;

@SuppressWarnings("all")
public class 六十七点五CCT {
    //倾角：32.5度，半径：6.6cm，6.9cm，7.5cm，7.8cm，四极电流：5600A，二极电流：6180A
    //匝间距：0.005m
    //总匝数：235
    //9度，12.9度，23.8度按比例给就行了
    //四极：2×7mm，二极：2×6mm，超导体电流密度用上面给的电流除以截面积

    //CCT半径
    static double R = 1.00;//m
    static double angCCT = 67.5;

    //从内到外 1 2 3 4层。12-AG-CCT 34-Dipole
    static double r1 = 6.6 / 100;
    static double r2 = 6.9 / 100;
    static double r3 = 7.5 / 100;
    static double r4 = 7.8 / 100;

    //电流
    static double I1 = 7374;
    static double I2 = -7374;
    static double I3 = -6111;
    static double I4 = 6111;

    //倾角
    static double tiltAngle1 = AngleToRadian.to(32.5);
    static double tiltAngle2 = AngleToRadian.to(-32.5);
    static double tiltAngle3 = AngleToRadian.to(32.5);
    static double tiltAngle4 = AngleToRadian.to(-32.5);

    //匝数 12层为AG-CCT，15/24/3为段号，即12345。9度，12.9度，23.8度，12.9度，9度
    static int n12_15 = 31;
    static int n12_24 = 45;
    static int n12_3 = 83;
    static int n34 = 235;

    //绕一周phi变化量，相当于直线CCT的w
    static double phi012 = AngleToRadian.to(angCCT) / (n12_15 * 2 + n12_24 * 2 + n12_3);
    static double phi034 = AngleToRadian.to(angCCT) / n34;

    //步长
    static double stepKsi = AngleToRadian.to(1.0);

    //对二级CCT的四极场补偿
    static double compensateQuadFoeDipole = -1.873;

    //磁刚度
    static final double Bp = 2.432128299585206;

    //边缘角度
    static final double EDGE = 40;//度

    public static void main(String[] args) {
        final SingleLayerDiscreteCCTs ccts1 = cct1();
        final SingleLayerDiscreteCCTs ccts2 = cct2();
        final SingleLayerDiscreteCCTs ccts3 = cct3();
        final SingleLayerDiscreteCCTs ccts4 = cct4();

        Vector3d[] ccts1Path = ccts1.joinAll3d();
        Vector3d[] ccts2Path = ccts2.joinAll3d();
        Vector3d[] ccts3Path = ccts3.joinAll3d();
        Vector3d[] ccts4Path = ccts4.joinAll3d();

        ccts1.Plot3d();
//        ccts2.Plot3d();
//        ccts3.Plot3d();
//        ccts4.Plot3d();

        Timer.invoke();

        //理想轨道
        final Vector3d[] trajectory = Vector3d.vector2dTo3d(Vector2d.arc(Vector2d.getZeros(), R,
                AngleToRadian.to(-EDGE), AngleToRadian.to(angCCT + EDGE),
                false, AngleToRadian.to(0.5)));
//        Plot3d.plot3(trajectory, Plot2d.BLACK_LINE);

        //位移方向
        Vector2d midpoint = Vector2d.rayForPolarAngle(AngleToRadian.to(67.5 / 2));
        Vector3d midpoint3d = Vector3d.vector2dTo3d(midpoint);
        Plot3d.plotPoint(Vector3d.getZeros(), Plot2d.BLACK_POINT);
        Plot3d.plot3(
                new Vector3d[]{
                        Vector3d.getZeros(),
                        Vector3d.vector2dTo3d(Vector2d.rayForPolarAngle(AngleToRadian.to(0.0)))
                }, Plot2d.BLACK_LINE
        );
        Plot3d.plot3(
                new Vector3d[]{
                        Vector3d.getZeros(),
                        Vector3d.vector2dTo3d(Vector2d.rayForPolarAngle(AngleToRadian.to(67.5)))
                }, Plot2d.BLACK_LINE
        );

        //拟合法。更多的点~
        if (false) {
            //--------------------------参数设定--------------------------------
            //每个点上划分的点数
            final int NUM = 61;
            //位移，以理想轨道 trajectory 为基础
            final double[] displace = Numpy.linspace(-30.0 / 1000, 30.0 / 1000, NUM);
            ;//位移
            //拟合次数
            final int DEGREE = 3;
            //打印图像选择   2-归一化二极场/ 4-归一化四极场 / 6-归一化六极场/ ALL-所有归一化极场 / 2T-二极场/4Tm-梯度
            // 只应修改#处的值
            final String[] plotDegrees = {"2", "4", "6", "8", "ALL", "2T", "4Tm"};
            final String plotDegree = plotDegrees[/*#*/4/*#*/];

            //--------------------------开始计算和拟合---------------------------
            //存放角度的数组
            List<Double> angleList = new ArrayList<>(trajectory.length);
            //数组的数组。每个List存放一个偏差位移下的磁场分布
            List<Double>[] magnetLists = new ArrayList[NUM];//list's list
            //存放拟合结果。注意归一化
            List<Double>[] fittingLists = new ArrayList[DEGREE + 1];

            //先填充角度数组
            for (int i = 0; i < trajectory.length; i++) {
                angleList.add(i, AngleToRadian.reverse(Math.atan2(trajectory[i].y, trajectory[i].x)));
            }

            //不同位移轨迹下记录磁场。这是个双层循环
            for (int iNUM = 0; iNUM < NUM; iNUM++) {
                //初始化数组
                magnetLists[iNUM] = new ArrayList<>(trajectory.length);
                //移动轨道，有需要画图
                Vector3d[] currentTrajectory = Vector3d.move(trajectory, midpoint3d, displace[iNUM]);
                Plot3d.plot3(currentTrajectory, Plot2d.BLACK_LINE);

                //记录磁场
                for (int iTrajectory = 0; iTrajectory < trajectory.length; iTrajectory++) {
                    //计算磁场
                    double m1z = Magnet.magnetAtPoint(ccts1Path, I1, currentTrajectory[iTrajectory]).z;
                    double m2z = Magnet.magnetAtPoint(ccts2Path, I2, currentTrajectory[iTrajectory]).z;
                    double m3z = Magnet.magnetAtPoint(ccts3Path, I3, currentTrajectory[iTrajectory]).z;
                    double m4z = Magnet.magnetAtPoint(ccts4Path, I4, currentTrajectory[iTrajectory]).z;
                    magnetLists[iNUM].add(iTrajectory, m1z + m2z + m3z + m4z);
                }
            }
            System.out.println("磁场记录完成，开始拟合。一共需要拟合次数" + trajectory.length);

            //记录完成。开始拟合
            //先初始化数组
            for (int iDegree = 0; iDegree < DEGREE + 1; iDegree++) {
                fittingLists[iDegree] = new ArrayList<>(trajectory.length);
            }

            //开始拟合
            for (int iTrajectory = 0; iTrajectory < trajectory.length; iTrajectory++) {
                //拟合所用的轮子。感谢apache的math库
                WeightedObservedPoints points = new WeightedObservedPoints();
                PolynomialCurveFitter fitter = PolynomialCurveFitter.create(DEGREE);

                //放入数据
                for (int iNum = 0; iNum < NUM; iNum++) {
//                    System.out.println("放入数据"+iNum);
                    points.add(displace[iNum], magnetLists[iNum].get(iTrajectory));
//                    System.out.println(Vector2d.getOne(displace[iNum], magnetLists[iNum].get(iTrajectory)));
                }

                //开始拟合
                double[] fit = fitter.fit(points.toList());
                //结果放入拟合数组中
                for (int iDegree = 0; iDegree < DEGREE + 1; iDegree++) {
//                    System.out.println("结果放入拟合数组中" + iDegree);
                    //iDegree = 0 归一化二极场
                    //iDegree = 1 归一化四极场
                    //iDegree = 2 归一化六极场
                    fittingLists[iDegree].add(iTrajectory,
                            /*归一化*/
                            fit[iDegree] * Factorial.get(iDegree) / Bp
                    );
                }

            }
            System.out.println("拟合结束");


            //拟合结束。打印!
            switch (plotDegree) {
                //{"2", "4", "6", "ALL", "2T", "4Tm"};
                case "2":
                    Plot2d.plot2(angleList, fittingLists[0], Plot2d.BLACK_LINE);
                    break;
                case "4":
                    Plot2d.plot2(angleList, fittingLists[1], Plot2d.BLACK_LINE);
                    break;
                case "6":
                    Plot2d.plot2(angleList, fittingLists[2], Plot2d.BLACK_LINE);
                    break;
                case "8":
                    Plot2d.plot2(angleList, fittingLists[3], Plot2d.BLACK_LINE);
                case "2T":
                    //不支持
                    break;
                case "4Tm":
                    //不支持
                    break;
                case "ALL":
                default:
                    for (int iDegree = 0; iDegree < DEGREE + 1; iDegree++) {
                        Plot2d.plot2(angleList, fittingLists[iDegree], Plot2d.BLACK_LINE);
                    }
            }

        }

        //好场区画图
        if (false) {
            double x = R - 6.5 / 100;
            List<Double> xList = new ArrayList<>(trajectory.length);
            List<Double> gList = new ArrayList<>(trajectory.length);
            List<Double> List2 = new ArrayList<>(trajectory.length);
            while (x < R + 6.5 / 100) {
                final Vector3d m01 = Magnet.magnetAtPoint(ccts1Path, I1, midpoint3d.setLengthAndReturn(x));
                final Vector3d m02 = Magnet.magnetAtPoint(ccts2Path, I2, midpoint3d.setLengthAndReturn(x));
                final Vector3d m03 = Magnet.magnetAtPoint(ccts3Path, I3, midpoint3d.setLengthAndReturn(x));
                final Vector3d m04 = Magnet.magnetAtPoint(ccts4Path, I4, midpoint3d.setLengthAndReturn(x));

                Plot3d.plotPoint(midpoint3d, Plot2d.BLACK_POINT);

                double m2 = m03.z + m04.z;
                double m4 = m01.z + m02.z;

                xList.add((x - 1.00) * 1000);
                gList.add(m4);
                List2.add(m2);
                x += 0.1 / 100;
            }

            Plot2d.plot2(xList, List2, Plot2d.BLACK_LINE);
            Plot2d.plot2(xList, gList, Plot2d.RED_LINE);

            //二极场理想值
            Vector2d[] scoff2 = new Vector2d[]{
                    Vector2d.getOne(-60, 2.43),
                    Vector2d.getOne(60, 2.43)
            };
            //四极场理想值
            Vector2d[] scoff4 = new Vector2d[]{
                    Vector2d.getOne(-60, -2.58),
                    Vector2d.getOne(0, 0),
                    Vector2d.getOne(60, 2.58)
            };
            Plot2d.plot2(scoff2, Plot2d.GREY_DASH);
            Plot2d.plot2(scoff4, Plot2d.PINK_DASH);
        }

        //简单计算
        if (false) {
            //移动
            Vector3d[] midArc3d001 = Vector3d.move(trajectory, midpoint3d, 0.01);
//            Plot3d.plot3(midArc3d001, Plot2d.YELLOW_LINE);

            //计算磁场
            List<Double> angleList = new ArrayList<>(trajectory.length);
            List<Double> gList = new ArrayList<>(trajectory.length);
            List<Double> gList2 = new ArrayList<>(trajectory.length);
            List<Double> gList3 = new ArrayList<>(trajectory.length);
            for (int i = 0; i < trajectory.length; i++) {
                final Vector3d m01 = Magnet.magnetAtPoint(ccts1Path, I1, trajectory[i]);
                final Vector3d m02 = Magnet.magnetAtPoint(ccts2Path, I2, trajectory[i]);
                final Vector3d m03 = Magnet.magnetAtPoint(ccts3Path, I3, trajectory[i]);
                final Vector3d m04 = Magnet.magnetAtPoint(ccts4Path, I4, trajectory[i]);
//            m02.setLength(0.0);
                final Vector3d m0011 = Magnet.magnetAtPoint(ccts1Path, I1, midArc3d001[i]);
                final Vector3d m0012 = Magnet.magnetAtPoint(ccts2Path, I2, midArc3d001[i]);
                final Vector3d m0013 = Magnet.magnetAtPoint(ccts3Path, I3, midArc3d001[i]);
                final Vector3d m0014 = Magnet.magnetAtPoint(ccts4Path, I4, midArc3d001[i]);
//            m0012.setLength(0.0);

                double m000 = m01.z + m02.z + m03.z + m04.z;
                double m0001 = m0011.z + m0012.z + m0013.z + m0014.z;

                double m20 = m03.z + m04.z;
                double m21 = m0013.z + m0014.z;

                final Vector3d m0 = Vector3d.add(m01, m02);
                final Vector3d m001 = Vector3d.add(m0011, m0012);
                angleList.add(i, AngleToRadian.reverse(Math.atan2(trajectory[i].y, trajectory[i].x)));
                gList.add(i,
//                        -(m0.z - m001.z) / 0.01
                        m0.z
                );
                gList2.add(i,
//                        -(m000 - m0001) / 0.01
                m000
                );
                gList3.add(i,
//                        -(m20 - m21) / 0.01
                        m20
                );

            }

//            Plot2d.plot2(angleList, gList, Plot2d.RED_LINE);//4
            Plot2d.plot2(angleList, gList2, Plot2d.BLACK_LINE);//4+2
//            Plot2d.plot2(angleList, gList3, Plot2d.YELLOW_LINE);//2
            //四级场硬边模型
            Vector2d[] scoff4 = new Vector2d[]{
                    Vector2d.getOne(-EDGE, 0),
                    Vector2d.getOne(0, 0),
                    Vector2d.getOne(0, 43),
                    Vector2d.getOne(9, 43),

                    Vector2d.getOne(9, -43),
                    Vector2d.getOne(9 + 12.9, -43),

                    Vector2d.getOne(9 + 12.9, 43),
                    Vector2d.getOne(9 + 12.9 + 23.8, 43),

                    Vector2d.getOne(9 + 12.9 + 23.8, -43),
                    Vector2d.getOne(9 + 12.9 + 23.8 + 12.9, -43),

                    Vector2d.getOne(9 + 12.9 + 23.8 + 12.9, 43),
                    Vector2d.getOne(9 + 12.9 + 23.8 + 12.9 + 9, 43),

                    Vector2d.getOne(9 + 12.9 + 23.8 + 12.9 + 9, 0),
                    Vector2d.getOne(67.5 + EDGE, 0.0)
            };
            //二极场硬边模型
            Vector2d[] scoff2 = new Vector2d[]{
                    Vector2d.getOne(-EDGE, 0),
                    Vector2d.getOne(0, 0),

                    Vector2d.getOne(0, 2.43),
                    Vector2d.getOne(67.5, 2.43),

                    Vector2d.getOne(67.5, 0.0),
                    Vector2d.getOne(67.5 + EDGE, 0.0),


            };

            //二极CCT的四极分量 -1.84
            Vector2d[] quadInDipole184 = new Vector2d[]{
                    Vector2d.getOne(-EDGE, -1.84),
                    Vector2d.getOne(67.5 + EDGE, -1.84),
            };

            Plot2d.plot2(scoff2, Plot2d.GREY_DASH);
//            Plot2d.plot2(quadInDipole184,Plot2d.BLUE_LINE);

            Plot2d.plot2(new Vector2d[]{
                    Vector2d.getOne(-EDGE, 0), Vector2d.getOne(67.5 + EDGE, 0)
            }, Plot2d.GREY_LINE);
        }

        //束流跟踪
        if (false) {
            //粒子生成
            RunningParticle particle = ParticleFactory.getProton(
                    Vector3d.getOne(1, -0.5, 0),
                    Vector3d.getOne(0, 1, 0),225
            );
            //运动长度
            final double LENGTH = 2.0;
            //步长
            final double STEP = 0.001;
            while (particle.distance < LENGTH) {
                //画图
                particle.plot3self(Plot2d.YELLOW_SMALL_POINT);

                //计算磁场
                final Vector3d m01 = Magnet.magnetAtPoint(ccts1Path, I1, particle.position);
                final Vector3d m02 = Magnet.magnetAtPoint(ccts2Path, I2, particle.position);
                final Vector3d m03 = Magnet.magnetAtPoint(ccts3Path, I3, particle.position);
                final Vector3d m04 = Magnet.magnetAtPoint(ccts4Path, I4, particle.position);

                m01.addSelf(m02);
                m01.addSelf(m03);
                m01.addSelf(m04);

                particle.runSelf(m01,STEP);
            }

        }

        //积分场均匀度
        if(false){

        }

        //边缘场Enge函数.以入口为例
        if(false){
            //拟合方程 ln(1/F-1)=a1+a2(z/D)+a3(z/D)^2+a4(z/D)^3+a5(z/D)^4+a6(z/D)^5
            //二极场
            if(true){
                //cosy对enge函数的要求
                final double D = 0.08;//m 孔直径80mm
                final double MIN = -5.0*D;
                final double MAX = 3.0*D;//enge范围

                final double STEP = 0.01;//m

                List<Double> xList = new ArrayList<>();
                List<Double> bList = new ArrayList<>();

                double start = MIN;
                while(start<MAX){
                    //得到需要的点。即入口处MIN 到 MAX那一段
                    Vector3d point = Vector3d.vector2dTo3d(
                            Vector2d.pointFromArcToXY(R,0.0,start)
                    );
                    //点画出来
                    Plot3d.plotPoint(point,Plot2d.YELLOW_SMALL_POINT);
                    //求磁场
                    final Vector3d m01 = Magnet.magnetAtPoint(ccts1Path, I1, point);
                    final Vector3d m02 = Magnet.magnetAtPoint(ccts2Path, I2, point);
                    final Vector3d m03 = Magnet.magnetAtPoint(ccts3Path, I3, point);
                    final Vector3d m04 = Magnet.magnetAtPoint(ccts4Path, I4, point);
                    final double bz = Sum.get(m01.z, m02.z, m03.z, m04.z);

                    xList.add(start);
                    bList.add(bz);

                    start+=STEP;
                }

                //开始处理数据并归一化
                //z  首先需要倒过来。z要归一化到-5 3
                List<Double> zD_List = new ArrayList<>(xList.size());
                for (int i = 0; i < xList.size(); i++) {
                    zD_List.add(
                            -xList.get(xList.size()-1-i)/D
                    );
                }
                //bz->F->ln(1/F-1)
                //ln(1/F-1)=a1+a2(z/D)+a3(z/D)^2+a4(z/D)^3+a5(z/D)^4+a6(z/D)^5
                List<Double> fList = new ArrayList<>();
                List<Double> ln1Y1_List = new ArrayList<>(xList.size());
                final double B_MAX = bList.stream().mapToDouble(e->(double)e).min().getAsDouble();//流有点恶心啊
                for (int i = 0; i < xList.size(); i++) {
                    fList.add(
                            bList.get(xList.size()-1-i)/B_MAX
                    );
//                    ln1Y1_List.add(
//                            Math.log()
//                    );
                }
                //画图试试
                Plot2d.plot2(zD_List,fList,Plot2d.BLACK_LINE);
                Plot2d.plotXLine(1.0,-4,6,Plot2d.GREY_DASH);
                Plot2d.plotXLine(0.0,-4,6,Plot2d.GREY_DASH);

                Plot2d.xlim(-4,6);





                //




            }
        }

        Timer.invoke();
//        Plot2d.xlim(-EDGE, 67.5 + EDGE);
//        Plot3d.setCenter(Vector3d.getOne(1,0,0),2.0);
        Plot3d.setCube(1.0);
        Plot3d.showThread();
        Plot2d.showThread();


    }

    private static void 测试() {
        final Object ob2 = read("5cctBY-01234-2");
        final Object ob3 = read("5cctBY-01234-3");

        List<Vector2d> oblist2 = (List<Vector2d>) ob2;
        List<Vector2d> oblist3 = (List<Vector2d>) ob3;

        Vector2d[] v2s2 = oblist2.toArray(Vector2d[]::new);
        Vector2d[] v2s3 = oblist3.toArray(Vector2d[]::new);

        System.out.println(oblist2);
        System.out.println("--------------");
        System.out.println(oblist3);
    }

    private static void 四极场() {
        double dr = 1.0 / 100;
        final Object ob2 = read("5cctBY-01234-0");
        final Object ob3 = read("5cctBY-01234-4");

        List<Vector2d> oblist2 = (List<Vector2d>) ob2;
        List<Vector2d> oblist3 = (List<Vector2d>) ob3;

        Vector2d[] v2s2 = oblist2.toArray(Vector2d[]::new);
        Vector2d[] v2s3 = oblist3.toArray(Vector2d[]::new);

        List<Vector2d> ploe4 = new ArrayList<>();

        for (int i = 0; i < v2s2.length; i++) {
            double a = v2s2[i].x;

            double m2 = v2s2[i].y;
            double m3 = v2s3[i].y;
            System.out.println("m2 = " + m2);
            System.out.println("m3 = " + m3);

            ploe4.add(Vector2d.getOne(a, (m2 - m3) / dr * 4.0));
        }
        Persistence.write(ploe4, "pole4");
    }

    private static void 后处理() {
        double dr = 1.0 / 100;
        final Object ob0 = read("5cctBY-01234-0");
        final Object ob1 = read("5cctBY-01234-1");
        final Object ob2 = read("5cctBY-01234-2");
        final Object ob3 = read("5cctBY-01234-3");
        final Object ob4 = read("5cctBY-01234-4");
        List<Vector2d> oblist0 = (List<Vector2d>) ob0;
        List<Vector2d> oblist1 = (List<Vector2d>) ob1;
        List<Vector2d> oblist2 = (List<Vector2d>) ob2;
        List<Vector2d> oblist3 = (List<Vector2d>) ob3;
        List<Vector2d> oblist4 = (List<Vector2d>) ob4;

        //提取数据
        Vector2d[] v2s0 = oblist0.toArray(Vector2d[]::new);//角度 磁场
        Vector2d[] v2s1 = oblist1.toArray(Vector2d[]::new);
        Vector2d[] v2s2 = oblist2.toArray(Vector2d[]::new);
        Vector2d[] v2s3 = oblist3.toArray(Vector2d[]::new);
        Vector2d[] v2s4 = oblist4.toArray(Vector2d[]::new);

        //存放拟合结果
        List<Vector2d> ploe2 = new ArrayList<>();
        List<Vector2d> ploe4 = new ArrayList<>();
        List<Vector2d> ploe6 = new ArrayList<>();
//        List<Vector2d> ploe8 = new ArrayList<>();

        //自变量 X
        double r0 = R + dr * (-2);
        double r1 = R + dr * (-1);
        double r2 = R + dr * (-0);
        double r3 = R + dr * (1);
        double r4 = R + dr * (2);


        for (int i = 0; i < v2s0.length; i++) {
            double a = v2s0[i].x;

            //应变量 Y
            double m0 = v2s0[i].y;
            double m1 = v2s1[i].y;
            double m2 = v2s2[i].y;
            double m3 = v2s3[i].y;
            double m4 = v2s4[i].y;

//            double[] A0 = {1,r0,Math.pow(r0,2),Math.pow(r0,3)};
            double[] A0 = {1, r0, Math.pow(r0, 2)};
            double[] A1 = {1, r1, Math.pow(r1, 2)};
            double[] A2 = {1, r2, Math.pow(r2, 2)};
            double[] A3 = {1, r3, Math.pow(r3, 2)};
            double[] A4 = {1, r4, Math.pow(r4, 2)};

            Matrix A = new Matrix(new double[][]{A0, A1, A2, A3, A4});
            Matrix AT = A.transpose();

            Matrix Y = new Matrix(new double[][]{
                    {m0}, {m1}, {m2}, {m3}, {m4}
            });
            Matrix ATY = AT.times(Y);

            Matrix ATA = AT.times(A);

            Matrix X = ATA.solve(ATY);
            X.print(7, 4);

            ploe2.add(Vector2d.getOne(a, X.get(0, 0)));//x(0,0) 0次项目
            ploe4.add(Vector2d.getOne(a, X.get(1, 0)));
            ploe6.add(Vector2d.getOne(a, X.get(2, 0)));
//            ploe8.add(Vector2d.getOne(a,X.get(3,0)));
        }

        System.out.println("ploe2.size() = " + ploe2.size());

        Persistence.write(ploe2, "pole2");
        Persistence.write(ploe4, "pole4");
        Persistence.write(ploe6, "pole6");
//        Persistence.write(ploe8,"pole8");


    }

    /**
     * @return 第一层 AG-CCT
     */
    private static SingleLayerDiscreteCCTs cct1() {
        //---------------------------11-----------------------
        CurvedCCT cct11 = CurvedCCTAnalysis.curvedCCTFactory(
                R, r1, n12_15, I1, 2, phi012, tiltAngle1, stepKsi);
        DiscreteCCT dcct1 = CurvedCCTAnalysis.discrete(cct11);
        //---------------------------12-----------------------
        CurvedCCT cct12 = CurvedCCT.reverseWinding(cct11);
        cct12.setN(n12_24);
        DiscreteCCT dcct2 = CurvedCCTAnalysis.discrete(cct12);
        //---------------------------13-----------------------
        CurvedCCT cct13 = CurvedCCT.reverseWinding(cct12);
        cct13.setN(n12_3);
        DiscreteCCT dcct3 = CurvedCCTAnalysis.discrete(cct13);
        //---------------------------14-----------------------
        CurvedCCT cct14 = CurvedCCT.reverseWinding(cct13);
        cct14.setN(n12_24);
        DiscreteCCT dcct4 = CurvedCCTAnalysis.discrete(cct14);
        //---------------------------14-----------------------
        CurvedCCT cct15 = CurvedCCT.reverseWinding(cct14);
        cct15.setN(n12_15);
        DiscreteCCT dcct5 = CurvedCCTAnalysis.discrete(cct15);

        //---------------------------11&12连接-----------------------
        ConnectionSegmentOfCCT connect12 = CurvedCCTAnalysis.connect(dcct1, dcct2,
                0.2, Vector2d.getOne(0, 0.001),
                0.3, Math.PI / 180.0 * 10, Math.PI / 18000);
        //---------------------------12&13连接/ 34连接 45连接-----------------------
        ConnectionSegmentOfCCT connect23 = CurvedCCTAnalysis.connect(dcct2, dcct3,
                0.2, Vector2d.getOne(0, -0.002),
                0.3, Math.PI / 180.0 * 30.0, Math.PI / 18000);
        ConnectionSegmentOfCCT connect34 = CurvedCCTAnalysis.connect(dcct3, dcct4,
                0.2, Vector2d.getOne(0, 0.001),
                0.3, Math.PI / 180.0 * 10, Math.PI / 18000);
        ConnectionSegmentOfCCT connect45 = CurvedCCTAnalysis.connect(dcct4, dcct5,
                0.2, Vector2d.getOne(0, -0.002),
                0.3, Math.PI / 180.0 * 30.0, Math.PI / 18000);

        SingleLayerDiscreteCCTs singleLayerDiscreteCCTs = new SingleLayerDiscreteCCTs(I1,
                dcct1, connect12, dcct2, connect23, dcct3, connect34, dcct4, connect45, dcct5);
        return singleLayerDiscreteCCTs;
    }

    /**
     * @return 第二层 AG-CCT
     */
    private static SingleLayerDiscreteCCTs cct2() {
        //---------------------------11-----------------------
        CurvedCCT cct21 = CurvedCCTAnalysis.curvedCCTFactory(
                R, r2, n12_15, I2, 2, phi012, tiltAngle2, stepKsi);
        DiscreteCCT dcct1 = CurvedCCTAnalysis.discrete(cct21);
        System.out.println("cct21 = " + cct21);
        //---------------------------12-----------------------
        CurvedCCT cct22 = CurvedCCT.reverseWinding(cct21);
        cct22.setN(n12_24);
        DiscreteCCT dcct2 = CurvedCCTAnalysis.discrete(cct22);
        //---------------------------13-----------------------
        CurvedCCT cct23 = CurvedCCT.reverseWinding(cct22);
        cct23.setN(n12_3);
        DiscreteCCT dcct3 = CurvedCCTAnalysis.discrete(cct23);
        //---------------------------14-----------------------
        CurvedCCT cct24 = CurvedCCT.reverseWinding(cct23);
        cct24.setN(n12_24);
        DiscreteCCT dcct4 = CurvedCCTAnalysis.discrete(cct24);
        //---------------------------14-----------------------
        CurvedCCT cct25 = CurvedCCT.reverseWinding(cct24);
        cct25.setN(n12_15);
        DiscreteCCT dcct5 = CurvedCCTAnalysis.discrete(cct25);

        //---------------------------11&12连接-----------------------
        ConnectionSegmentOfCCT connect12 = CurvedCCTAnalysis.connect(dcct1, dcct2,
                0.2, Vector2d.getOne(0, 0.001),
                0.3, Math.PI / 180.0 * 10, Math.PI / 18000);
        //---------------------------12&13连接/ 34连接 45连接-----------------------
        ConnectionSegmentOfCCT connect23 = CurvedCCTAnalysis.connect(dcct2, dcct3,
                0.2, Vector2d.getOne(0, -0.002),
                0.3, Math.PI / 180.0 * 30.0, Math.PI / 18000);
        ConnectionSegmentOfCCT connect34 = CurvedCCTAnalysis.connect(dcct3, dcct4,
                0.2, Vector2d.getOne(0, 0.001),
                0.3, Math.PI / 180.0 * 10, Math.PI / 18000);
        ConnectionSegmentOfCCT connect45 = CurvedCCTAnalysis.connect(dcct4, dcct5,
                0.2, Vector2d.getOne(0, -0.002),
                0.3, Math.PI / 180.0 * 30.0, Math.PI / 18000);

        SingleLayerDiscreteCCTs singleLayerDiscreteCCTs = new SingleLayerDiscreteCCTs(I2,
                dcct1, connect12, dcct2, connect23, dcct3, connect34, dcct4, connect45, dcct5);
        return singleLayerDiscreteCCTs;

    }

    private static SingleLayerDiscreteCCTs cct3() {
        CurvedCCT cct3 = CurvedCCTAnalysis.curvedCCTFactory(
                R, r3, n34, I3, 1, phi034, tiltAngle3, stepKsi);
        DiscreteCCT dcct3 = CurvedCCTAnalysis.discreteWithFixDipole(cct3, compensateQuadFoeDipole / 2.0);
        return new SingleLayerDiscreteCCTs(I3, dcct3);
    }

    private static SingleLayerDiscreteCCTs cct4() {
        CurvedCCT cct4 = CurvedCCTAnalysis.curvedCCTFactory(
                R, r4, n34, I4, 1, phi034, tiltAngle4, stepKsi);
        System.out.println("cct4 = " + cct4);
        DiscreteCCT dcct4 = CurvedCCTAnalysis.discreteWithFixDipole(cct4, compensateQuadFoeDipole / 2.0);
        return new SingleLayerDiscreteCCTs(I4, dcct4);
    }

    private static void a动画测试(SingleLayerDiscreteCCTs singleLayerDiscreteCCTs) {
        final List<Vector2d[]> cctPaths2dList = singleLayerDiscreteCCTs.cctPaths2dList;
        final List<Vector3d[]> cctPaths3dList = singleLayerDiscreteCCTs.cctPaths3dList;
        Vector2d[] joinAll = new Vector2d[]{};
        Vector3d[] joinAll3d = new Vector3d[]{};
        for (int i = 0; i < cctPaths2dList.size(); i++) {
            joinAll = ArrayMerge.merge(joinAll, cctPaths2dList.get(i));
            joinAll3d = ArrayMerge.merge(joinAll3d, cctPaths3dList.get(i));
        }

        System.out.println("joinAll.length = " + joinAll.length);


        Plot2d.plot2(joinAll);
        Plot3d.plot3(joinAll3d);
//        singleLayerDiscreteCCTs.Plot2d();
        Plot2d.showThread();
        Plot3d.setCube(0.5);
        Plot3d.showThread();
    }
}
