package cn.edu.hust.zrx.cct.study;

import cn.edu.hust.zrx.cct.Logger;
import cn.edu.hust.zrx.cct.advanced.combined.GantryDataBipolarCo;
import cn.edu.hust.zrx.cct.advanced.combined.GantryDataBipolarCoUtils;
import cn.edu.hust.zrx.cct.base.line.Trajectory;
import cn.edu.hust.zrx.cct.base.particle.ParticleFactory;
import cn.edu.hust.zrx.cct.base.particle.PhaseSpaceParticle;
import cn.edu.hust.zrx.cct.base.particle.PhaseSpaceParticles;
import cn.edu.hust.zrx.cct.base.particle.RunningParticle;
import cn.edu.hust.zrx.cct.base.point.Point2;
import cn.edu.hust.zrx.cct.base.python.Plot2d;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class C20201112GeneRunnerCCTTest {

    @Test
    void particlesAtEntry() {
        List<RunningParticle> rp = C20201112GeneRunnerCCT.particlesAtEntry(GantryDataBipolarCo.SecondBend.getDefault());

        Trajectory t = GantryDataBipolarCoUtils.getTrajectory1(GantryDataBipolarCo.FirstBend.getDefault());

        RunningParticle ip = ParticleFactory.createIdealProtonAtTrajectory(t, t.getLength(), 215);

        Logger.getLogger().info("rp = " + rp.stream().map(Objects::toString).collect(Collectors.joining("\n")));

        List<PhaseSpaceParticle> pp = PhaseSpaceParticles.phaseSpaceParticlesFromRunningParticles(
                ip, ip.computeNaturalCoordinateSystem(), rp);

        Logger.getLogger().info("pp = " + pp.stream().map(Objects::toString).collect(Collectors.joining("\n")));

        List<Point2> px = PhaseSpaceParticles.projectionToXXpPlane(pp);
        List<Point2> py = PhaseSpaceParticles.projectionToYYpPlane(pp);

        Plot2d.plot2(px,Plot2d.BLACK_POINT);
        Plot2d.plot2(py,Plot2d.RED_POINT);

        Plot2d.equal();

        Plot2d.show();
    }
}