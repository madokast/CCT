package others;

import cn.edu.hust.zrx.cct.Logger;
import cn.edu.hust.zrx.cct.base.BaseUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;

/**
 * Description
 * CpuLoad
 * <p>
 * Data
 * 11:08
 *
 * @author zrx
 * @version 1.0
 */

@SpringBootTest
public class CpuLoad {

    public static void main(String[] args) {
        new CpuLoad().getProcessCpuLoadTest();
    }

//    @Test
    public void getProcessCpuLoadTest(){
        BaseUtils.StreamTools.repeat(10)
                .forEach(i->{
                   try {
                       Logger.getLogger().info("getProcessCpuLoad() = " + getProcessCpuLoad());
                       Thread.sleep(1000);

                   }catch (Exception e){
                       e.printStackTrace();
                   }
                });
    }

    public static double getProcessCpuLoad() throws Exception {

        MBeanServer mbs    = ManagementFactory.getPlatformMBeanServer();
        ObjectName name    = ObjectName.getInstance("java.lang:type=OperatingSystem");
        AttributeList list = mbs.getAttributes(name, new String[]{ "ProcessCpuLoad" });

        if (list.isEmpty())     return Double.NaN;

        Attribute att = (Attribute)list.get(0);
        Double value  = (Double)att.getValue();

        // usually takes a couple of seconds before we get real values
        if (value == -1.0)      return Double.NaN;
        // returns a percentage value with 1 decimal point precision
        return ((int)(value * 1000) / 10.0);
    }
}


