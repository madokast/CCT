package zrx.base.line2d;

import zrx.Tools.AngleToRadian;
import zrx.Tools.Numpy;
import zrx.base.point.Vector2d;
import zrx.base.point.Vector2dTo3d;
import zrx.base.point.Vector3d;
import zrx.python.Plot2d;
import zrx.python.Plot3d;

import java.util.ArrayList;
import java.util.List;

/**
 * 二维轨迹类
 * 轨迹由曲线构成
 * <p>
 * 测试通过 2019年9月2日
 */

public class Trajectory implements Line {
    private List<Line> lineList;
    private double length;

    public Trajectory() {
        this.lineList = new ArrayList<>();
        this.length = 0.0;
    }

    public Trajectory add(Line line) {
        this.lineList.add(line);
        this.length += line.getLength();

        return this;
    }

    @Override
    public double getLength() {
        return length;
    }

    @Override
    public Vector2d rightHandSidePoint(double s, double d) {
        //确定在哪一段Line上
        int i = 0;
        while (s > this.lineList.get(i).getLength()) {
            //如果已是最后一个。放弃
            if (i == this.lineList.size() - 1) {
                break;
            }
            s -= this.lineList.get(i).getLength();

            i++;
        }
        //确认完毕，s在i上

        return this.lineList.get(i).rightHandSidePoint(s, d);
    }

    @Deprecated
    public Vector2d[] discrete(int number){
        Vector2d[] ans  = new Vector2d[number];

        double[] sArr = Numpy.linspace(0,this.getLength(),number);

        for (int i = 0; i < number; i++) {
            ans[i] = this.pointAt(sArr[i]);
        }

        return ans;
    }

    public Vector3d[] discreteBySArr(double[] sArr , Vector2dTo3d vector2dTo3d){
        final Vector3d[] ans = new Vector3d[sArr.length];
        for (int i = 0; i < sArr.length; i++) {
            ans[i] = vector2dTo3d.functioon(
                    pointAt(sArr[i])
            );
        }

        return ans;
    }

    public void Plot2d(double step,String describe){
        double[] sArr = Numpy.stepToEnd(step,getLength());
        Vector2d[] v2ds = new Vector2d[sArr.length];
        for (int i = 0; i < sArr.length; i++) {
            v2ds[i] = pointAt(sArr[i]);
        }

        Plot2d.plot2(v2ds,describe);
    }

    public void Plot3d(double step,Vector2dTo3d vector2dTo3d,String describe){
        double[] sArr = Numpy.stepToEnd(step,getLength());
        Vector3d[] v3ds = new Vector3d[sArr.length];
        for (int i = 0; i < sArr.length; i++) {
            v3ds[i] =vector2dTo3d.functioon( pointAt(sArr[i]));
        }

        Plot3d.plot3(v3ds,describe);
    }

    ////////////////////////////测试/////////////////
    public static void main(String[] args) {
        final Trajectory trajectory = new Trajectory();
        trajectory.add(StraightLine.getByStartAndEnd(
                Vector2d.getOne(0, 0),
                Vector2d.getOne(1, 2)
        )).add(ArcLine.getOne(
                Vector2d.getOne(1, 0),
                2.0,
                AngleToRadian.to(90),
                AngleToRadian.to(0.0),
                true
        ));

        System.out.println("trajectory.getLength() = " + trajectory.getLength());

//        for (double s : Numpy.linspace(-1, trajectory.getLength() + 1, 100)) {
//            Plot2d.plotPoint(trajectory.pointAt(s), Plot2d.BLACK_POINT);
//            Plot2d.plotPoint(trajectory.rightHandSidePoint(s, Math.sin(s)), Plot2d.YELLOW_POINT);
//            Plot2d.plotPoint(trajectory.leftHandSidePoint(s, Math.cos(s)), Plot2d.RED_POINT);
//        }

        Plot2d.plot2(trajectory.discrete(20),Plot2d.YELLOW_LINE);

        Plot2d.equal();
        Plot2d.showThread();
    }
}
