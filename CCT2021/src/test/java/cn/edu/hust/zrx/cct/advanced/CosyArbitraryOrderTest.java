package cn.edu.hust.zrx.cct.advanced;

import cn.edu.hust.zrx.cct.base.particle.PhaseSpaceParticle;
import cn.edu.hust.zrx.cct.base.particle.PhaseSpaceParticles;
import cn.edu.hust.zrx.cct.study.C20201113GeneRunnerAllGantry;
import org.junit.jupiter.api.Test;

import java.util.List;

import static cn.edu.hust.zrx.cct.base.BaseUtils.Constant.MM;
import static org.junit.jupiter.api.Assertions.*;

class CosyArbitraryOrderTest {

    @Test
    void readMap() {
        List<PhaseSpaceParticle> pp = PhaseSpaceParticles.phaseSpaceParticlesAlongPositiveEllipseInPlane(
                false, 3.5 * MM, 7.5 * MM, 0.1, 5
        );

        pp.forEach(System.out::println);

        CosyArbitraryOrder.CosyMapArbitraryOrder map = C20201113GeneRunnerAllGantry.COSY_MAP1021.all.map;

        List<PhaseSpaceParticle> apply = map.apply(pp, 5);

        System.out.println("--------------");

        apply.forEach(System.out::println);


    }
}