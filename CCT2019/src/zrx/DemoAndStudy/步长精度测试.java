package zrx.DemoAndStudy;

import zrx.CCT.ConcreteCCT.CurvedCCTAnalysis;
import zrx.CCT.ConcreteCCT.DiscreteCCT;
import zrx.CCT.Magnet;
import zrx.CCT.abstractCCT.CurvedCCT;
import zrx.Tools.AngleToRadian;
import zrx.Tools.Persistence;
import zrx.Tools.Timer;
import zrx.base.point.Vector2d;
import zrx.base.point.Vector3d;
import zrx.python.Plot2d;
import zrx.python.Plot3d;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 采用67.5度二极场CCT研究
 * 两层cct分别称为cct3 和 cct4
 */

public class 步长精度测试 {
    public static void main(String[] args) {
        mainExam();
//        read();
    }

    private static void read() {
        double[] step = {360,180,120,90,72,60,45,40,36,30,24,20,18,15,12,10,9,8,6,5,4,3,2,1,
                0.9,0.8,0.6,0.5,0.4,0.3,0.2,0.1,
                0.09,0.08,0.06,0.05,0.04,0.03/*0.02,0.01,0.005,0.001*/};
        List<Vector3d> ms = (ArrayList)Persistence.read("步长精度07091722");

        Vector2d[] v2s = new Vector2d[step.length];

        for (int i = 0; i < step.length; i++) {
            System.out.println(360.0/step[i]+"  "+ms.get(i).y);
            v2s[i] = Vector2d.getOne(360.0/step[i],ms.get(i).y);
        }

        Plot2d.plot2(Arrays.copyOfRange(v2s,10,v2s.length));

        Plot2d.showThread();
    }

    private static void mainExam() {
        //-----------------参数----------------------
        double R = 1.00;//m
        double r3 = 7.5/100;
        double r4 = 7.8/100;
        double I3 = 6180;
        double I4 = 6180;

        double tiltAngle3 = AngleToRadian.to(32.5);
        double tiltAngle4 = AngleToRadian.to(-32.5);

        int n3 = 235;
        int n4 = 235;

        double phi03 = AngleToRadian.to(67.5)/n3;
        double phi04 = AngleToRadian.to(67.5)/n4;

        double stepKsi = AngleToRadian.to(1.0);

        //-----------------建模----------------------
        CurvedCCT cct3 = CurvedCCTAnalysis.curvedCCTFactory(
                R, r3, n3, I3, 1, phi03, tiltAngle3, stepKsi);

        CurvedCCT cct4 = CurvedCCTAnalysis.curvedCCTFactory(
                R, r4, n4, I4, 1, phi04, tiltAngle4, stepKsi);

        DiscreteCCT dcct3 = CurvedCCTAnalysis.discrete(cct3);
        DiscreteCCT dcct4 = CurvedCCTAnalysis.discrete(cct4);

        dcct3.plot23d(Plot2d.RED_LINE);
        dcct4.plot23d(Plot2d.YELLOW_LINE);

        System.out.println("cct3 = " + cct3);
        System.out.println("cct4 = " + cct4);

        //-----------------轨迹----------------------
        Vector3d[] trajectory = Vector3d.vector2dTo3d(Vector2d.arc(Vector2d.getZero(),R,
                AngleToRadian.to(0.0-20),AngleToRadian.to(67.5+20),false,AngleToRadian.to(1.0)));

        Plot3d.plot3(trajectory, Plot2d.BLACK_LINE);

        //------------------中心---------------------
        System.out.println("-------------------------");
        Vector3d midPoint = Vector3d.vector2dTo3d(
                Vector2d.rayForPolarAngle(AngleToRadian.to(67.5/2.0)).changeLengthAndReturn(R)
        );
        System.out.println("Vector2d.rayForPolarAngle(67.5/2.0) = " + Vector2d.rayForPolarAngle(67.5 / 2.0));
        System.out.println("midPoint = " + midPoint);
        Plot3d.plotPoint(midPoint,Plot2d.BLACK_POINT);
        Vector3d m3 = Magnet.magnetAtPoint(dcct3.getPathInXYZ(),I3,midPoint);
        Vector3d m4 = Magnet.magnetAtPoint(dcct4.getPathInXYZ(),I4,midPoint);
        Vector3d m34 = Vector3d.add(m3,m4);
        System.out.println("m3 = " + m3);
        System.out.println("m4 = " + m4);
        System.out.println("m34 = " + m34);
        //本段CCT离散化成功，点数：84601
        //本段CCT离散化成功，点数：84601
        //cct3 = 这是个单层二极CCT，理想二极场为 -1.2158156606121233T
        //cct4 = 这是个单层二极CCT，理想二极场为 1.215815660612123T
        //-------------------------
        //Vector2d.rayForPolarAngle(67.5/2.0) = [-0.6912931286141081, 0.7225744323811342]
        //midPoint = [0.8314696123025452 0.5555702330196022 0.0]
        //m3 = [0.8620308322387913 -1.2714235534444251 1.2250633212391158]
        //m4 = [0.8441413944781037 -1.2819697853914267 -1.2247975822434416]
        //m34 = [1.706172226716895 -2.5533933388358516 2.657389956741696E-4]


        //------------------测试---------------------
        System.out.println("----------测试-----------");
        double[] step = {360,180,120,90,72,60,45,40,36,30,24,20,18,15,12,10,9,8,6,5,4,3,2,1,
                0.9,0.8,0.6,0.5,0.4,0.3,0.2,0.1,
                0.09,0.08,0.06,0.05,0.04,0.03/*0.02,0.01,0.005,0.001*/};
        //dell 设备 0.02 失败
        List<Vector3d> ms = new ArrayList<>();
        Timer.invoke();
        for(double s:step){
            System.out.println("-----------------------------------------");
            System.out.println("s = " + s);
            CurvedCCT cctTest = CurvedCCTAnalysis.curvedCCTFactory(
                    R, r3, 235, I3, 1, phi03, tiltAngle3, AngleToRadian.to(s));
            System.out.println(cctTest);
            DiscreteCCT dcctTest = CurvedCCTAnalysis.discrete(cctTest);
            Vector3d m = Magnet.magnetAtPoint(dcctTest.getPathInXYZ(),cctTest.getI(),midPoint);
            System.out.println("磁场 = " + m);
            ms.add(new Vector3d(m));
        }
        Timer.invoke();
        Persistence.write(ms,"步长精度07091722");




        Plot3d.setCube(0.6);
        Plot3d.showThread();
    }

}
