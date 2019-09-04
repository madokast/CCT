package zrx.CCT;

import zrx.CCT.ConcreteCCT.AllCCTs;
import zrx.Tools.Numpy;
import zrx.base.point.Vector2d;
import zrx.base.point.Vector3d;
import zrx.beam.RunningParticle;

import java.util.ArrayList;

/**
 * 专用于各种CCT分析 工具类函数
 * 只需要指定少数参数即可
 * <p>
 * AllCCTs——核心
 */

public class AnalyseCCT {
    /**
     * 计算直线上磁场Bz分布情况。
     * 例如传入(cct,10 ,12 ,(0,0,0), (0,2,0), 3)
     * 即把(10, 12)补充为3个点，作为应变量(一般是角度、路程等量)
     * 而((0,0,0), (0,2,0))这两个端点，同样补充至3个点，分别计算每个点的磁场分布Bz
     * 则返回(10,Bz0) ,(11,Bz1), (12,Bz2)
     * 其中Bz1 为点 (0,1,0) 处的磁场
     *
     * @param allCCTs    完整的CCT数据（感谢封装）
     * @param xStart     应变量起点
     * @param xEnd      应变量终点
     * @param pointStart 起点
     * @param pointEnd   终点
     * @param number     扩充点数，另见Numpy.linspace()(
     * @return Vector2d数组，每个元素为(x(i), Bz(i))，长度number
     */
    public static Vector2d[/*number*/] magnetZeAlongLine(AllCCTs allCCTs, double xStart, double xEnd,
                                                         Vector3d pointStart, Vector3d pointEnd, int number) {
        final double[] xs = Numpy.linspace(xStart, xEnd, number);
        final Vector3d[] points = Vector3d.interpolation(pointStart, pointEnd, number);

        final Vector2d[] results2D = new Vector2d[number];

        for (int i = 0; i < number; i++) {
            results2D[i] = Vector2d.getOne(xs[i],allCCTs.magnet(points[i]).z);
        }

        return results2D;
    }

    public static Vector2d[] magnetZeAlongTrajectoryWithS(AllCCTs allCCTs,Vector3d[] trajectory){
        final Vector2d[] results = new Vector2d[trajectory.length];
        double s = 0.0;

        for (int i = 0; i < results.length-1; i++) {
            results[i] = Vector2d.getOne(s,allCCTs.magnet(trajectory[i]).z);
            s+=Vector3d.subtract(trajectory[i+1],trajectory[i]).length();
        }

        results[results.length-1] = Vector2d.getOne(s,allCCTs.magnet(trajectory[results.length-1]).z);

        return results;
    }

    public static double[] magnetZeAlongTrajectory(AllCCTs allCCTs,Vector3d[] trajectory){
        final double[] results = new double[trajectory.length];
        for (int i = 0; i < results.length; i++) {
            results[i] = allCCTs.magnet(trajectory[i]).z;
        }

        return results;
    }

    /**
     * 粒子运动获得轨迹
     * @param allCCTs CCT
     * @param particle 粒子
     * @param length 运动长度
     * @param step 步长
     * @return 轨迹
     */
    public static Vector3d[] particleRunning(AllCCTs allCCTs, RunningParticle particle,double length,double step){
        ////记录轨迹的数组
        //            final ArrayList<Vector3d> particleTraj = new ArrayList<>((int) (LENGTH / STEP));
        //
        //            //运动
        //            while (particle.distance < LENGTH) {
        //                particleTraj.add(new Vector3d(particle.position));
        //                particle.runSelf(allCCTs.magnet(particle.position), STEP);
        ////                System.out.println("particle.position = " + particle.position);
        //            }
        //
        //            //画轨迹
        //            Plot3d.plot3(Vector3d.listToArray(particleTraj), Plot2d.YELLOW_LINE);
        final ArrayList<Vector3d> particleTraj = new ArrayList<>((int) (length / step));

        while (particle.distance < length) {
            particleTraj.add(new Vector3d(particle.position));
            particle.runSelf(allCCTs.magnet(particle.position), step);
        }

        return Vector3d.listToArray(particleTraj);
    }
}
