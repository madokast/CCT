package cn.edu.hust.zrx.cct.advanced.combined;

import cn.edu.hust.zrx.cct.base.line.Trajectory;
import cn.edu.hust.zrx.cct.base.line.TrajectoryFactory;
import cn.edu.hust.zrx.cct.base.point.Point2;
import cn.edu.hust.zrx.cct.base.vector.Vector2;

/**
 * Description
 * GantryDataBipolarCoUtils
 * <p>
 * Data
 * 16:42
 *
 * @author zrx
 * @version 1.0
 */

public class GantryDataBipolarCoUtils {

    public static Trajectory getTrajectory1(GantryDataBipolarCo.FirstBend firstBend) {
        return TrajectoryFactory.setStartingPoint(Point2.origin())
                .setStraightLine(firstBend.DL1, Vector2.xDirect())
                .addArcLine(firstBend.trajectoryBigRPart1, false, firstBend.dipoleCct12Angle)
                .addStraitLine(firstBend.GAP1)//gap1
                .addStraitLine(firstBend.QS1_LEN)//QS1_LEN
                .addStraitLine(firstBend.GAP2)//GAP2
                .addStraitLine(firstBend.QS2_LEN)//QS2_LEN
                .addStraitLine(firstBend.GAP2)//GAP2
                .addStraitLine(firstBend.QS1_LEN)//QS1_LEN
                .addStraitLine(firstBend.GAP1)//gap1
                .addArcLine(firstBend.trajectoryBigRPart1, false, firstBend.dipoleCct12Angle)
                .addStraitLine(firstBend.DL1);
    }

    public static Trajectory getTrajectory2(GantryDataBipolarCo.FirstBend firstBend,
                                     GantryDataBipolarCo.SecondBend secondBend) {
        Trajectory trajectory1 = getTrajectory1(firstBend);


        return TrajectoryFactory.setStartingPoint(trajectory1.pointAtEnd())
                .setStraightLine(secondBend.DL2, trajectory1.directAtEnd())
                .addArcLine(secondBend.trajectoryBigRPart2, true, secondBend.dipoleCct345Angle)
                .addStraitLine(secondBend.GAP3 + secondBend.QS3_LEN + secondBend.GAP3)
                .addArcLine(secondBend.trajectoryBigRPart2, true, secondBend.dipoleCct345Angle)
                .addStraitLine(secondBend.DL2);
    }

    public static Trajectory getTrajectory2(){
        return GantryDataBipolarCoUtils.getTrajectory2(GantryDataBipolarCo.FirstBend.getDefault(),
                GantryDataBipolarCo.SecondBend.getDefault());
    }

}
