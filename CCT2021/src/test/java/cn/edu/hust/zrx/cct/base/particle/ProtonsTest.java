package cn.edu.hust.zrx.cct.base.particle;

import cn.edu.hust.zrx.cct.Logger;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProtonsTest {

    @Test
    public void 磁钢度(){
        double magneticStiffness = Protons.getMagneticStiffness(250);

        Logger.getLogger().info("magneticStiffness = " + magneticStiffness);
    }

    @Test
    public void test_STATIC_ENERGY_J(){
        Logger.getLogger().info("Protons.STATIC_ENERGY_J = " + Protons.STATIC_ENERGY_J);
    }

    @Test
    public void test_STATIC_ENERGY_eV(){
        Logger.getLogger().info("Protons.STATIC_ENERGY_eV = " + Protons.STATIC_ENERGY_eV);
    }

    @Test
    public void test_STATIC_ENERGY_MeV(){
        Logger.getLogger().info("Protons.STATIC_ENERGY_MeV = " + Protons.STATIC_ENERGY_MeV);
    }

    //get动量分散后的动能

    @Test
    public void test_get动量分散后的动能(){
        Logger.getLogger().info("Protons.get动量分散后的动能 = " + Protons.get动量分散后的动能(1000,0.01));
    }

    @Test
    public void test_convertMomentumDispersionToEnergyDispersion(){
        Logger.getLogger().info("Protons.convertMomentumDispersionToEnergyDispersion = " +
                Protons.convertMomentumDispersionToEnergyDispersion(
                0.01,250));
    }

    @Test
    void testConvert能量分散_TO_动量分散() {
        double dp = 0.05;
        double dE = Protons.convert动量分散_TO_能量分散(dp, 250);
        Logger.getLogger().info("dE = " + dE);

        double dp2 = Protons.convert能量分散_TO_动量分散(dE, 250);
        Logger.getLogger().info("dp2 = " + dp2);
    }
}