package zrx;

import zrx.Tools.Numpy;
import zrx.Tools.Timer;
import zrx.base.Vector3d;
import zrx.python.Plot;

public class Test {
    public static void main(String[] args) {
        CurvedCCT cct = new CurvedCCT(1, 3, 5e-2, 30, 100, Math.PI / 6.0, 2, Math.PI / 180.0);
        Plot.plot3(cct.pathForPlot3());
        Plot.show();
    }

    private static void 弯曲CCT验收(){
        CurvedCCT cct = new CurvedCCT(1, 3, 5e-2, 30, 100, Math.PI / 6.0, 2, Math.PI / 180.0);
        System.out.println(cct.magnet(new Vector3d(1, 1, 1)));

        //py
        //10800
        //[ 4.44534923e-06  8.75442240e-06 -1.87388197e-06]

        //java
        //jj = 10800
        //[4.445349227022009E-6 8.75442240123138E-6 -1.8738819729176823E-6]
    }

    private static void 直线CCT验收2(){
        Timer.invoke();
        int[] steps = {30, 90, 120, 150, 180, 240, 360, 3600};
        for (int step : steps) {
            StraightCCT cct = new StraightCCT(25e-3, 6.96e-3, 75, 10000.0, Math.PI / 9.0, 1, Math.PI / step);
            cct.printTheoreticalValues();
            double[] nz = Numpy.linspace(0.25,0.251,3);
            for(double i : nz){
                System.out.println(cct.magnet(new Vector3d(0.0, 0.0, i)));
            }
        }
        Timer.invoke();

        // py 结果
        //.....
        //[-1.06035990e-03  2.49131772e+00 -1.79637258e+00]
        //540000
        //540000
        //[-1.06063519e-03  2.49131217e+00 -1.79637789e+00]
        //540000
        //540000
        //[-1.06090965e-03  2.49130689e+00 -1.79638295e+00]
        //运行时间 398.318s

        //java 结果
        //...
        //[-0.0010603599042505278 2.4913177160675217 -1.7963725751309338]
        //jj = 540000
        //[-0.001060635193060712 2.4913121722988816 -1.796377887532746]
        //jj = 540000
        //[-0.0010609096519886665 2.4913068893205232 -1.7963829492884515]
        //运行时间 0.409s
    }

    private static void 直线CCT验收() {
        StraightCCT straightCCT = new StraightCCT(25e-3, 6.96e-3, 75, 10000.0, Math.PI / 9.0, 1, Math.PI / 180);
        System.out.println(straightCCT.point(0.0));
        System.out.println(straightCCT.point(1.0));
        System.out.println(straightCCT.point(2.0));

        //py计算结果
        //[-0.025  0.     0.   ]
        //[-0.01350756  0.02103677  0.05890578]
        //[0.01040367 0.02273244 0.06467229]

        //java计算结果
        //[-0.025 0.0 0.0]
        //[-0.013507557646703494 0.021036774620197415 0.05890578165106808]
        //[0.01040367091367856 0.022732435670642044 0.064672290502133]

        StraightCCT cct = new StraightCCT(25e-3, 6.96e-3, 75, 10000.0, Math.PI / 9.0, 1, Math.PI / 180);
        cct.printTheoreticalValues();
        System.out.println(cct.point(20));
        System.out.println(cct.magnet(new Vector3d(0.0, 0.0, 0.0)));

        //java
        //这应该也许是个二极场CCT吧
        //理论By = -2.4803031255351966
        //理论Bz = 1.8055130193044788
        //[-0.0102020515453348 0.022823631268190692 0.08486177961770422]
        //jj = 27000
        //[-0.032163679068191675 1.2423964658731057 -0.9020006578940977]

        //python
        //[-0.01020205  0.02282363  0.08486178]
        //27000
        //[-0.03216368  1.24239647 -0.90200066]


    }

    private static void 螺线管验收() {
        CCT cct = Solenoid.demoInstance();
        System.out.println("cct = " + cct);
//        System.out.println(CCT.dB(new Vector3d(0,0,0),new Vector3d(1,1,1),1.0,new Vector3d(0.9,0.9,0)));
//        System.out.println("cct.magnet(new Vector3d(0.0,0.0,0.0)) = " + cct.magnet(new Vector3d(0.0, 0.0, 0.0)));
        Timer.invoke();
        System.out.println(cct.magnet(new Vector3d(0, 0, 0)));
        System.out.println(cct.magnet(new Vector3d(0, 0, 0.1)));
        System.out.println(cct.magnet(new Vector3d(0, 0, 0.2)));
        System.out.println(cct.magnet(new Vector3d(0, 0, 0.3)));
        System.out.println(cct.magnet(new Vector3d(0, 0, 0.4)));
        Timer.invoke();
    }
}
