package zrx.DemoAndStudy.小研究;

import zrx.CCT.ConcreteCCT.CurvedCCTAnalysis;
import zrx.CCT.ConcreteCCT.DiscreteCCT;
import zrx.CCT.NoLinkedCCT;
import zrx.CCT.abstractCCT.CurvedCCT;
import zrx.Tools.AngleToRadian;
import zrx.base.point.Vector2d;
import zrx.base.point.Vector3d;
import zrx.python.Plot2d;

import java.util.ArrayList;
import java.util.List;

public class 四级CCT的二极场 {
    private static final double ANGLE = 30;

    private static double R = 1;
    private static final double r1 = 66.0/1000;
    private static final double r2 = 69.0/1000;
    private static final int n = 100;
    private static final double I = 5000;
    private static final double phi0 = AngleToRadian.to(ANGLE)/n;
    private static final double tiltAngle1 = AngleToRadian.to(30.0);
    private static final double tiltAngle2 = AngleToRadian.to(-30.0);
    private static final double stepKsi = AngleToRadian.to(1.0);

    private static final double EDGE_ANG = 30;


    public static void main(String[] args) {
        List<Vector2d> list2dRB2 = new ArrayList<>();


        while(R<5.0){
            CurvedCCT cct1 = CurvedCCTAnalysis.curvedCCTFactory(
                    R, r1, n, I, 2, phi0, tiltAngle1, stepKsi);
            DiscreteCCT dcct1 = CurvedCCTAnalysis.discrete(cct1);
            System.out.println(R+"  "+cct1);

            CurvedCCT cct2 = CurvedCCTAnalysis.curvedCCTFactory(
                    R, r2, n, -I, 2, phi0, tiltAngle2, stepKsi);
            DiscreteCCT dcct2 = CurvedCCTAnalysis.discrete(cct2);

            final NoLinkedCCT noLinkedCCT1 = new NoLinkedCCT(dcct1.pathInXYZ, cct1.getI());
            final NoLinkedCCT noLinkedCCT2 = new NoLinkedCCT(dcct2.pathInXYZ, cct2.getI());

            noLinkedCCT1.plot3(Plot2d.BLUE_LINE);
            noLinkedCCT2.plot3(Plot2d.RED_LINE);

            //计算二极场
            //轨迹
            Vector3d[] traj = Vector3d.vector2dTo3d(Vector2d.arc(
                    Vector2d.getZero(),R,AngleToRadian.to(-EDGE_ANG),AngleToRadian.to(ANGLE+EDGE_ANG),false,stepKsi
            ));
            Vector3d[] path001 = Vector3d.move(traj,Vector3d.vector2dTo3d(Vector2d.rayForPolarAngle(ANGLE/2)),0.01);

            List<Double> bzList = new ArrayList<>(traj.length);
            List<Double> gList = new ArrayList<>(traj.length);

            for (int i = 0; i < traj.length; i++) {
                double Bz = noLinkedCCT1.magnetAtPoint(traj[i]).z+noLinkedCCT2.magnetAtPoint(traj[i]).z;
                double Bz001 = noLinkedCCT1.magnetAtPoint(path001[i]).z+noLinkedCCT2.magnetAtPoint(path001[i]).z;

                bzList.add(Bz);
                gList.add((Bz001-Bz)/0.01);
            }

            System.out.println("中点Bz："+bzList.get(bzList.size()/2));
            System.out.println("G:"+ gList.get(gList.size()/2));
            list2dRB2.add(Vector2d.getOne(R,bzList.get(bzList.size()/2)));
            R+=0.5;
        }

        System.out.println("list2dRB2 = " + list2dRB2);

        for (int i = 0; i < list2dRB2.size(); i++) {
            final double R = list2dRB2.get(i).x;
            final double B = list2dRB2.get(i).y;

            System.out.println(B*R*R);
        }




    }
}
