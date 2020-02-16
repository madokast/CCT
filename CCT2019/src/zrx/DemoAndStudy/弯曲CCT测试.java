package zrx.DemoAndStudy;

import zrx.CCT.ConcreteCCT.CurvedCCTAnalysis;
import zrx.CCT.ConcreteCCT.DiscreteCCT;
import zrx.CCT.Magnet;
import zrx.CCT.abstractCCT.CurvedCCT;
import zrx.Tools.AngleToRadian;
import zrx.base.point.Vector2d;
import zrx.base.point.Vector3d;
import zrx.python.Plot2d;
import zrx.python.Plot3d;

import java.util.ArrayList;
import java.util.List;

public class 弯曲CCT测试 {
    public static void main(String[] args) {
        /**
         *  半径1m
         *  绕线半径0.03m
         *  匝数40
         *  电流100A
         *  四极场CCT
         *  绕一匝 1读——半径上对应 w = pi/180 m 推出梯度：G = -0.41569219381653094T
         *      ——40度，因此，中心点为20度
         *  倾角30度
         *
         *  测试没有问题
         */


        CurvedCCT cct1 = CurvedCCTAnalysis.curvedCCTFactory(
                1.0, 0.03, 40, 10000, 2, Math.PI / 180, Math.PI / 6, Math.PI / 180);
        DiscreteCCT dcct1 = CurvedCCTAnalysis.discrete(cct1);

        System.out.println(cct1);
        dcct1.plot3d(Plot2d.BLACK_LINE);

        if(false){
            //中心点
            Vector2d vector2d20 = Vector2d.rayForPolarAngle(AngleToRadian.to(20));
            Vector3d vector3d20 = Vector3d.vector2dTo3d(vector2d20);
            //中心点移动
            final Vector3d vector3d101 = Vector3d.vector2dTo3d(vector2d20.changeLengthAndReturn(1.01));
            final Vector3d vector3d099 = Vector3d.vector2dTo3d(vector2d20.changeLengthAndReturn(0.99));

            Plot3d.plotPoint(vector3d20,Plot2d.RED_POINT);
            Plot3d.plotPoint(vector3d101,Plot2d.BLUE_POINT);
            Plot3d.plotPoint(vector3d099,Plot2d.YELLOW_POINT);

            //算磁场
            System.out.println(Magnet.magnetAtPoint(dcct1.pathInXYZ, cct1.getI(), vector3d101));
            System.out.println(Magnet.magnetAtPoint(dcct1.pathInXYZ, cct1.getI(), vector3d20));
            System.out.println(Magnet.magnetAtPoint(dcct1.pathInXYZ, cct1.getI(), vector3d099));
        }

        //下面开始正式测试
        //理想轨迹
        Vector2d[] midArc = Vector2d.arc(Vector2d.getZero(), cct1.getR(),
                AngleToRadian.to(-20), AngleToRadian.to(40 + 20), false,
                AngleToRadian.to(1));
        Vector3d[] midArc3d = Vector3d.vector2dTo3d(midArc);
        Plot3d.plot3(midArc3d,Plot2d.RED_LINE);

        //位移方向
        Vector2d midpoint = Vector2d.rayForPolarAngle(AngleToRadian.to(20));
        Vector3d midpoint3d = Vector3d.vector2dTo3d(midpoint);

        //移动测试 测试通过2019年7月14日
        Vector3d[] midArc3d001 = Vector3d.move(midArc3d,midpoint3d,0.01);
        Plot3d.plot3(midArc3d001,Plot2d.YELLOW_LINE);

        //开始计算磁场 测试通过
        List<Double> angleList = new ArrayList<>(midArc.length);
        List<Double> gList = new ArrayList<>(midArc.length);
        for (int i = 0; i < midArc.length; i++) {
            final Vector3d m0 = Magnet.magnetAtPoint(dcct1.pathInXYZ, cct1.getI(), midArc3d[i]);
            final Vector3d m001 = Magnet.magnetAtPoint(dcct1.pathInXYZ, cct1.getI(), midArc3d001[i]);
            angleList.add(i,Math.atan2(midArc[i].y,midArc[i].x));
            gList.add(i,
                    (m0.z-m001.z)/0.01
                    );
        }

        Plot2d.plot2(angleList,gList,Plot2d.BLACK_LINE);



        Plot2d.showThread();
        Plot3d.setCube(1.0);
        Plot3d.showThread();
    }


}
//这是个单四极CCT，理想梯度为 -20.784609690826546T
//[0.24631784715875246 -0.6660046415791575 0.21983465973123803]

//0.20462990118701366
//20.462990118701367

//[0.24542801570169664 -0.6740209847301235 0.015204758544224378]

//0.21082822243924967
//21.082822243924967

//[0.24449523031251771 -0.6822044518624606 -0.1956234638950253]
