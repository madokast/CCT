package cn.edu.hust.zrx.cct.study.验证性研究;

import cn.edu.hust.zrx.cct.Logger;
import cn.edu.hust.zrx.cct.advanced.SR;
import cn.edu.hust.zrx.cct.base.BaseUtils;
import cn.edu.hust.zrx.cct.base.particle.PhaseSpaceParticle;
import cn.edu.hust.zrx.cct.base.particle.PhaseSpaceParticles;
import cn.edu.hust.zrx.cct.base.particle.Protons;

import java.util.List;

/**
 * Description
 * F3圆周粒子到COSY
 * <p>
 * Data
 * 9:39
 *
 * @author zrx
 * @version 1.0
 */

public class F3圆周粒子到COSY {

    private static final double MM = 0.001;

    public static void main(String[] args) {
        SR sr = new SR();
        boolean xPlane = true;
        double delta = 0.0;
        int number = 32;
        List<PhaseSpaceParticle> pp = PhaseSpaceParticles.phaseSpaceParticlesAlongPositiveEllipseInPlane(
                true, 3.5 * MM, 7.5 * MM, 0.1, number);

        pp.forEach(p -> {
            String sr1 = sr.sr(p, 1);
            System.out.println(sr1);
        });

        BaseUtils.Python.linspaceStream(-0.10, 0.10, 21)
                .forEach(dp -> {
                    double v = Protons.convert动量分散_TO_能量分散(dp, 250);
                    System.out.println(String.format("dp[%+4.3f]-dE[%+4.3f]",dp,v));
                });


    }
}
