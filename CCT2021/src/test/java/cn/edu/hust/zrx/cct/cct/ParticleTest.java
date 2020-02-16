package cn.edu.hust.zrx.cct.cct;

import cn.edu.hust.zrx.cct.Logger;
import cn.edu.hust.zrx.cct.base.BaseUtils;
import cn.edu.hust.zrx.cct.base.cct.CctFactory;
import cn.edu.hust.zrx.cct.base.cct.CctLine2Factory;
import cn.edu.hust.zrx.cct.base.cct.CctLine2s;
import cn.edu.hust.zrx.cct.base.line.Line2;
import cn.edu.hust.zrx.cct.base.line.TrajectoryFactory;
import cn.edu.hust.zrx.cct.base.particle.ParticleFactory;
import cn.edu.hust.zrx.cct.base.particle.RunningParticle;
import cn.edu.hust.zrx.cct.base.point.Point2;
import cn.edu.hust.zrx.cct.base.point.Point3;
import cn.edu.hust.zrx.cct.base.python.Plot2d;
import cn.edu.hust.zrx.cct.base.python.Plot3d;
import cn.edu.hust.zrx.cct.base.vector.Vector2;
import cn.edu.hust.zrx.cct.base.vector.Vector3;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * Description
 * ParticleTest
 * <p>
 * Data
 * 16:49
 *
 * @author zrx
 * @version 1.0
 */
@SpringBootTest
public class ParticleTest {
    //@Test
    public void test02(){
        CctFactory.Cct dipoleCct = CctFactory.createDipoleCct(
                60e-3, 75e-3, 0.75, 67.5, 135,
                -0.168, 0, 0, 5000,
                0.168, 0, 0, -5000,
                360);

        dipoleCct.plot3(Plot2d.RED_LINE,Plot2d.YELLOW_LINE);
        Plot3d.setCube(0.8);

        Line2 trajectory = TrajectoryFactory.setStartingPoint(Point2.create(0.75, -1))
                .setStraightLine(1.0, Vector2.yDirect())
                .addArcLine(0.75, false, 67.5)
                .addStraitLine(1.0);
        trajectory.plot3d(0.01,Plot2d.BLACK_LINE);

        Vector3 magnetAtMid = dipoleCct.magnetAt(trajectory.pointAt(trajectory.getLength() / 2).toPoint3());
        Logger.getLogger().info("magnetAtMid = {}",magnetAtMid.toString());

        List<Point2> magnetBzAlongTrajectory = dipoleCct.magnetBzAlongTrajectory(trajectory, 0.01);
        Plot2d.plot2(magnetBzAlongTrajectory,Plot2d.BLACK_LINE);

        Plot2d.showThread();
        Plot3d.show();
    }


    //@Test
    public void test01(){
        CctLine2s cct = CctLine2Factory.create(60e-3, 0.75,
                67.5, 135,
                -0.1, 0, 0);

        List<Point3> cctDisperse = cct.disperseToPoint3(360);

        Line2 trajectory = TrajectoryFactory.setStartingPoint(Point2.create(0.75, -1))
                .setStraightLine(1.0, Vector2.yDirect())
                .addArcLine(0.75, false, 67.5)
                .addStraitLine(1.0);

        final double I = 25000;
        List<Point2> magnetBzAlongTrajectory = BaseUtils.Magnet.magnetBzAlongTrajectory(cctDisperse, I, trajectory, 1e-2);
        Plot2d.plot2(magnetBzAlongTrajectory,Plot2d.BLACK_LINE);

        trajectory.plot3d(0.01,Plot2d.BLACK_LINE);
        Plot2d.show();

        //构造粒子
        RunningParticle idealProton =
                ParticleFactory.createIdealProtonAtTrajectory250MeV(trajectory);
        idealProton.plot3self(Plot2d.RED_POINT);

        final double footStep = 1e-3;
        final int times = (int)(trajectory.getLength()/footStep);
        final double plotStep = 0.1;
        final int skip = (int)(plotStep/footStep);

        BaseUtils.StreamTools.repeat(times)
                .mapToObj(i->idealProton.runSelf(
                        BaseUtils.Magnet.magnetAtPoint(cctDisperse,I,idealProton.position),
                        footStep
                )).skip(skip)
                .forEach(p->p.plot3self(Plot2d.YELLOW_SMALL_POINT));


        Plot3d.show();

    }
}
