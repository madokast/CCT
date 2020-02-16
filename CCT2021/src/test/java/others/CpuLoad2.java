package others;

import cn.edu.hust.zrx.cct.Logger;
import cn.edu.hust.zrx.cct.base.BaseUtils;
import oshi.json.SystemInfo;
import oshi.json.hardware.CentralProcessor;
import oshi.json.hardware.HardwareAbstractionLayer;

import java.util.Arrays;

/**
 * Description
 * CpuLoad2
 * <p>
 * Data
 * 11:28
 *
 * @author zrx
 * @version 1.0
 */

public class CpuLoad2 {
    private final SystemInfo systemInfo = new SystemInfo();

    public double getCpuRare() {
        HardwareAbstractionLayer hal = systemInfo.getHardware();
        CentralProcessor processor = hal.getProcessor();
        return processor.getSystemCpuLoadBetweenTicks();
    }

    public static void main(String[] args) {
//        for (int i = 0; i < 4; i++) {
//            Thread thread = new Thread(() -> {
//                while (true) {
//                    double[] linspace = BaseUtils.Python.linspace(0.1, 1.0, 1000);
//                    double sum = Arrays.stream(linspace).sum();
//                    if (sum > 1000)
//                        Logger.getLogger().info(">1000");
//                }
//            });
//
//            thread.setDaemon(true);
//            thread.start();
//            Logger.getLogger().info("i = {}",i);
//        }

        CpuLoad2 cpuLoad2 = new CpuLoad2();
        BaseUtils.StreamTools.repeat(10)
                .forEach(i -> {
                    Logger.getLogger().info("cpuLoad2.getCpuRare() = " + cpuLoad2.getCpuRare());
                    try {
                        Thread.sleep(1000);
                    }catch (Exception ignored){}
                });
    }
}
