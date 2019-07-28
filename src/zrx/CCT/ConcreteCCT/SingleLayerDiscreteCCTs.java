package zrx.CCT.ConcreteCCT;

import zrx.CCT.Magnet;
import zrx.Tools.ArrayMerge;
import zrx.base.Vector2d;
import zrx.base.Vector3d;
import zrx.python.Plot2d;
import zrx.python.Plot3d;

import java.util.ArrayList;
import java.util.List;

/**
 * 把一层的CCT全部放进来
 * 多亏了接口 SingleLayerDiscreteCCTComponent 把
 * DiscreteCCT cct离散类，以及 ConnectionSegmentOfCCT cct连接段类 都放了进来
 */

public class SingleLayerDiscreteCCTs {
    public List<Vector3d[]> cctPaths3dList;
    public List<Vector2d[]> cctPaths2dList;
    public double I;

    public SingleLayerDiscreteCCTs(double I,SingleLayerDiscreteCCTComponent...ccts){
        this.I = I;
        this.cctPaths3dList = new ArrayList<>();
        this.cctPaths2dList = new ArrayList<>();
        for (int i = 0; i < ccts.length; i++) {
            cctPaths3dList.add(ccts[i].getPathInXYZ());
            cctPaths2dList.add(ccts[i].getPathInKsiPhi());
        }
    }

    /**
     * 写一个磁场计算类
     * @return 计算磁场
     */
    public Vector3d magnet(Vector3d point){
        final Vector3d B = Vector3d.getZeros();
        for (int i = 0; i < cctPaths3dList.size(); i++) {
            B.addSelf(Magnet.magnetAtPoint(cctPaths3dList.get(i),I,point));
        }

        return B;
    }

    @Deprecated
    /**
     * 不要使用这个方法了
     */
    public Vector3d[] joinAll3d(){
        Vector3d[] joinAll3d = new Vector3d[]{};
        for (int i = 0; i < cctPaths2dList.size(); i++) {
            joinAll3d = ArrayMerge.merge(joinAll3d, cctPaths3dList.get(i));
        }

        return joinAll3d;
    }

    public void Plot2d(){
        //   0     1       2      3         4      5        6      7       8
        //dcct1,connect12,dcct2,connect23,dcct3,connect34,dcct4,connect45,dcct5
        String des = null;
        for (int i = 0; i < cctPaths2dList.size(); i++) {
            switch (i){
                case 0: case 4: case 8:
                    des = Plot2d.BLUE_LINE;
                    break;
                case 1: case 3: case 5: case 7:
                    des = Plot2d.BLACK_LINE;
                    break;
                case 2: case 6:
                    des = Plot2d.RED_LINE;
                    break;
            }

            Plot2d.plot2(cctPaths2dList.get(i),des);
        }

    }

    public void Plot3d(){
        String des = null;
        for (int i = 0; i < cctPaths3dList.size(); i++) {
            switch (i){
                case 0: case 4: case 8:
                    des = Plot2d.BLUE_LINE;
                    break;
                case 1: case 3: case 5: case 7:
                    des = Plot2d.BLACK_LINE;
                    break;
                case 2: case 6:
                    des = Plot2d.RED_LINE;
                    break;
            }
            Plot3d.plot3(cctPaths3dList.get(i),des);
        }
    }

    public void Plot3d(String describe){
        for (int i = 0; i < cctPaths3dList.size(); i++) {
            Plot3d.plot3(cctPaths3dList.get(i),describe);
        }
    }

    public void quickShow(){
        Plot2d();
        Plot3d();
        Plot2d.showThread();
        Plot3d.setCube(0.5);
        Plot3d.showThread();
    }
}
