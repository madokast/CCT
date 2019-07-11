package zrx.DemoAndStudy;

import Jama.Matrix;
import zrx.CCT.ConcreteCCT.ConnectionSegmentOfCCT;
import zrx.CCT.ConcreteCCT.CurvedCCTAnalysis;
import zrx.CCT.ConcreteCCT.DiscreteCCT;
import zrx.CCT.ConcreteCCT.SingleLayerDiscreteCCTs;
import zrx.CCT.Magnet;
import zrx.CCT.abstractCCT.CurvedCCT;
import zrx.Tools.AngleToRadian;
import zrx.Tools.ArrayMerge;
import zrx.Tools.Persistence;
import zrx.Tools.Timer;
import zrx.base.Vector2d;
import zrx.base.Vector3d;
import zrx.python.Plot2d;
import zrx.python.Plot3d;

import java.lang.invoke.VarHandle;
import java.util.ArrayList;
import java.util.List;
import java.util.SplittableRandom;
import java.util.TooManyListenersException;

import static zrx.Tools.Persistence.read;

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
    static double I1 = -5600;
    static double I2 = 5600;
    static double I3 = -6180;
    static double I4 = 6180;

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

    static double phi012 = AngleToRadian.to(angCCT) / (n12_15 * 2 + n12_24 * 2 + n12_3);
    static double phi034 = AngleToRadian.to(angCCT) / n34;

    static double stepKsi = AngleToRadian.to(1.0);

    public static void main(String[] args) {
        if(false){
            Timer.invoke();
//            测试();
//            后处理();
            四极场();
            Timer.invoke();
            System.exit(0);
        }


        final SingleLayerDiscreteCCTs ccts1 = cct1();
        final SingleLayerDiscreteCCTs ccts2 = cct2();
        final SingleLayerDiscreteCCTs ccts3 = cct3();
        final SingleLayerDiscreteCCTs ccts4 = cct4();

        Vector3d[] ccts1Path = ccts1.joinAll3d();
        Vector3d[] ccts2Path = ccts2.joinAll3d();
        Vector3d[] ccts3Path = ccts3.joinAll3d();
        Vector3d[] ccts4Path = ccts4.joinAll3d();

        ccts1.Plot3d();
        ccts2.Plot3d();
        ccts3.Plot3d();
        ccts4.Plot3d();

        Timer.invoke();

        for (int i = 0; i < 0; i++) {
            //6.6 / 100
            double dr = 1.0/100 *(i-2) ;

            Vector3d[] trajectory = Vector3d.vector2dTo3d(Vector2d.arc(Vector2d.getZeros(), R + dr,
                    AngleToRadian.to(-20), AngleToRadian.to(angCCT+20), false, AngleToRadian.to(0.5)));
            Plot3d.plot3(trajectory, Plot2d.BLACK_LINE);
            List<Vector2d> listABY = new ArrayList<>();
            for (Vector3d p : trajectory) {
                Vector3d m1 = Magnet.magnetAtPoint(ccts1Path, ccts1.I, p);
                Vector3d m2 = Magnet.magnetAtPoint(ccts2Path, ccts2.I, p);
                Vector3d m3 = Magnet.magnetAtPoint(ccts3Path, ccts3.I, p);
                Vector3d m4 = Magnet.magnetAtPoint(ccts4Path, ccts4.I, p);
                double a = Math.atan2(p.y, p.x);
                double mz = m1.z + m2.z + m3.z + m4.z;
                listABY.add(Vector2d.getOne(AngleToRadian.reverse(a), -mz));
            }
            Persistence.write(listABY, "5cctBY-01234-"+i);
        }


        Timer.invoke();
        Plot3d.setCube(0.5);
        Plot3d.showThread();


    }

    private static void 测试() {
        final Object ob2 = read("5cctBY-01234-2");
        final Object ob3 = read("5cctBY-01234-3");

        List<Vector2d> oblist2 = (List<Vector2d>)ob2;
        List<Vector2d> oblist3 = (List<Vector2d>)ob3;

        Vector2d[] v2s2 = oblist2.toArray(Vector2d[]::new);
        Vector2d[] v2s3 = oblist3.toArray(Vector2d[]::new);

        System.out.println(oblist2);
        System.out.println("--------------");
        System.out.println(oblist3);
    }

    private static void 四极场() {
        double dr = 1.0/100;
        final Object ob2 = read("5cctBY-01234-0");
        final Object ob3 = read("5cctBY-01234-4");

        List<Vector2d> oblist2 = (List<Vector2d>)ob2;
        List<Vector2d> oblist3 = (List<Vector2d>)ob3;

        Vector2d[] v2s2 = oblist2.toArray(Vector2d[]::new);
        Vector2d[] v2s3 = oblist3.toArray(Vector2d[]::new);

        List<Vector2d> ploe4 = new ArrayList<>();

        for (int i = 0; i < v2s2.length; i++){
            double a = v2s2[i].x;

            double m2 = v2s2[i].y;
            double m3 = v2s3[i].y;
            System.out.println("m2 = " + m2);
            System.out.println("m3 = " + m3);

            ploe4.add(Vector2d.getOne(a,(m2-m3)/dr*4.0));
        }
        Persistence.write(ploe4,"pole4");
    }

    private static void 后处理() {
        double dr = 1.0/100;
        final Object ob0 = read("5cctBY-01234-0");
        final Object ob1 = read("5cctBY-01234-1");
        final Object ob2 = read("5cctBY-01234-2");
        final Object ob3 = read("5cctBY-01234-3");
        final Object ob4 = read("5cctBY-01234-4");
        List<Vector2d> oblist0 = (List<Vector2d>)ob0;
        List<Vector2d> oblist1 = (List<Vector2d>)ob1;
        List<Vector2d> oblist2 = (List<Vector2d>)ob2;
        List<Vector2d> oblist3 = (List<Vector2d>)ob3;
        List<Vector2d> oblist4 = (List<Vector2d>)ob4;

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
        double r0 = R + dr *(-2);
        double r1 = R + dr *(-1);
        double r2 = R + dr *(-0);
        double r3 = R + dr *(1);
        double r4 = R + dr *(2);


        for (int i = 0; i < v2s0.length; i++) {
            double a = v2s0[i].x;

            //应变量 Y
            double m0 = v2s0[i].y;
            double m1 = v2s1[i].y;
            double m2 = v2s2[i].y;
            double m3 = v2s3[i].y;
            double m4 = v2s4[i].y;

//            double[] A0 = {1,r0,Math.pow(r0,2),Math.pow(r0,3)};
            double[] A0 = {1,r0,Math.pow(r0,2)};
            double[] A1 = {1,r1,Math.pow(r1,2)};
            double[] A2 = {1,r2,Math.pow(r2,2)};
            double[] A3 = {1,r3,Math.pow(r3,2)};
            double[] A4 = {1,r4,Math.pow(r4,2)};

            Matrix A = new Matrix(new double[][]{ A0,A1,A2,A3,A4 });
            Matrix AT = A.transpose();

            Matrix Y = new Matrix(new double[][]{
                    {m0},{m1},{m2},{m3},{m4}
            });
            Matrix ATY = AT.times(Y);

            Matrix ATA = AT.times(A);

            Matrix X = ATA.solve(ATY);
            X.print(7,4);

            ploe2.add(Vector2d.getOne(a,X.get(0,0)));//x(0,0) 0次项目
            ploe4.add(Vector2d.getOne(a,X.get(1,0)));
            ploe6.add(Vector2d.getOne(a,X.get(2,0)));
//            ploe8.add(Vector2d.getOne(a,X.get(3,0)));
        }

        System.out.println("ploe2.size() = " + ploe2.size());

        Persistence.write(ploe2,"pole2");
        Persistence.write(ploe4,"pole4");
        Persistence.write(ploe6,"pole6");
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
                R, r2, n12_24, I2, 2, phi012, tiltAngle2, stepKsi);
        DiscreteCCT dcct1 = CurvedCCTAnalysis.discrete(cct21);
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
        DiscreteCCT dcct3 = CurvedCCTAnalysis.discrete(cct3);
        return new SingleLayerDiscreteCCTs(I3, dcct3);
    }

    private static SingleLayerDiscreteCCTs cct4() {
        CurvedCCT cct4 = CurvedCCTAnalysis.curvedCCTFactory(
                R, r4, n34, I4, 1, phi034, tiltAngle4, stepKsi);
        System.out.println("cct4 = " + cct4);
        DiscreteCCT dcct4 = CurvedCCTAnalysis.discrete(cct4);
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
