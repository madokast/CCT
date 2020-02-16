package zrx.CCT.ConcreteCCT;

import zrx.Tools.Fit;
import zrx.Tools.Numpy;
import zrx.base.point.Vector3d;

import java.util.ArrayList;
import java.util.List;

/**
 * 全部单层CCT丢一起的计算
 */

@SuppressWarnings("all")
public class AllCCTs {
    private List<SingleLayerDiscreteCCTs> ccTs;

    public AllCCTs(SingleLayerDiscreteCCTs... ccTs) {
        this.ccTs = new ArrayList<>(ccTs.length);
        for (int i = 0; i < ccTs.length; i++) {
            this.ccTs.add(i, ccTs[i]);
        }
    }

    public AllCCTs add(SingleLayerDiscreteCCTs... ccTs) {
        for (int i = 0; i < ccTs.length; i++) {
            this.ccTs.add(i, ccTs[i]);
        }

        return this;
    }

    public AllCCTs addAll(AllCCTs allCCTs){
        for (int i = 0; i < allCCTs.ccTs.size(); i++) {
            this.add(allCCTs.get(i));
        }

        return this;
    }

    /**
     * 一股脑的磁场计算
     *
     * @param point 点
     * @return 磁场
     */
    public Vector3d magnet(Vector3d point) {
        final Vector3d B = Vector3d.getZeros();
        for (int i = 0; i < ccTs.size(); i++) {
            B.addSelf(ccTs.get(i).magnet(point));
        }

        return B;
    }

    /**
     * 计算磁场分量 3次
     * 从起点到终点的磁场分布，做出Bz的拟合
     *
     * @param start 起点
     * @param end   终点
     * @return [0][1][2][3]分别是二极场 四极场 六级场 八级场
     */
    public double[] magneticComponent0123(Vector3d start, Vector3d end) {
        final int NUM = 20;
        final int DEGREE = 3;

        //分点
        final Vector3d[] v3inters = Vector3d.interpolation(start, end, NUM);

        //起点和终点的长度 以及得到的自变量sList
        final double length = Vector3d.subtract(start, end).length();
        final double[] sList = Numpy.linspace(-length / 2.0, length / 2.0, NUM);

        //计算磁场
        final double[] bzList = new double[NUM];
        for (int i = 0; i < v3inters.length; i++) {
            bzList[i] = magnet(v3inters[i]).z;
        }

        //对数据 sList 和 bzList 拟合
        return Fit.fit(sList, bzList, DEGREE);
    }

    //赵泽峰版本
    public double[] magneticComponent012_ZhaoZeFengXYS(Vector3d start, Vector3d end){
        final int NUM = 20;
        final int DEGREE = 2;

        //分点
        final Vector3d[] v3inters = Vector3d.interpolation(start, end, NUM);

        //起点和终点的长度 以及得到的自变量sList
        final double length = Vector3d.subtract(start, end).length();
        final double[] sList = Numpy.linspace(-length / 2.0, length / 2.0, NUM);

        //计算磁场
        final double[] bzList = new double[NUM];
        for (int i = 0; i < v3inters.length; i++) {
            bzList[i] = - magnet(v3inters[i]).z;//这里是负数
        }

        //对数据 sList 和 bzList 拟合
        return Fit.fit(sList, bzList, DEGREE);
    }

    /**
     * 计算磁场分量 1次
     * 从起点到终点的磁场分布，做出Bz的拟合
     *
     * @param start 起点
     * @param end   终点
     * @return [0][1]分别是二极场 四极场
     */
    public double[] magneticComponent01(Vector3d start, Vector3d end) {
        final int NUM = 20;
        final int DEGREE = 1;

        //分点
        final Vector3d[] v3inters = Vector3d.interpolation(start, end, NUM);

        //起点和终点的长度 以及得到的自变量sList
        final double length = Vector3d.subtract(start, end).length();
        final double[] sList = Numpy.linspace(0, length, NUM);

        //计算磁场
        final double[] bzList = new double[NUM];
        for (int i = 0; i < v3inters.length; i++) {
            bzList[i] = magnet(v3inters[i]).z;
        }

        //对数据 sList 和 bzList 拟合
        return Fit.fit(sList, bzList, DEGREE);
    }

    public void plot3d() {
        ccTs.stream().forEach(SingleLayerDiscreteCCTs::Plot3d);
    }

    public int size() {
        return ccTs.size();
    }

    public SingleLayerDiscreteCCTs get(int i) {
        return ccTs.get(i);
    }

    /**
     * 复制AllCCTs，并绕原点，在XY轴上旋转
     *
     * @param allCCTs    带复制的CCTs
     * @param polarAngle 旋转，极角
     * @return 复制后且旋转了的CCTs
     */
    public static AllCCTs copyAndRotate(AllCCTs allCCTs, double polarAngle) {
        //private List<SingleLayerDiscreteCCTs> ccTs;

        final AllCCTs ans = new AllCCTs();
        for (int i = 0; i < allCCTs.ccTs.size(); i++) {
            ans.add(
                    SingleLayerDiscreteCCTs.copyAndRotate(allCCTs.ccTs.get(i), polarAngle)
            );
        }

        return ans;
    }

    /**
     * 复制并移动CCTs
     * @param allCCTs 原ccts
     * @param direct 方向
     * @param length 长度
     * @return 复制并移动
     */
    public static AllCCTs copyAndMove(AllCCTs allCCTs,Vector3d direct,double length){

        final AllCCTs ans = new AllCCTs();
        for (int i = 0; i < allCCTs.ccTs.size(); i++) {
            ans.add(
                    SingleLayerDiscreteCCTs.copyAndMove(allCCTs.ccTs.get(i), direct,length)
            );
        }

        return ans;
    }
}
