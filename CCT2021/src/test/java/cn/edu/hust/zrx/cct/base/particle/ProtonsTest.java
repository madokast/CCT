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

}