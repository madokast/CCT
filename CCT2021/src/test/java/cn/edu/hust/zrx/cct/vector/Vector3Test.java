package cn.edu.hust.zrx.cct.vector;

import cn.edu.hust.zrx.cct.Logger;
import cn.edu.hust.zrx.cct.base.BaseUtils;
import cn.edu.hust.zrx.cct.base.vector.Vector2;
import cn.edu.hust.zrx.cct.base.vector.Vector3;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

/**
 * Description
 * TODO
 * <p>
 * Data
 * 18:03
 *
 * @author zrx
 * @version 1.0
 */

@SpringBootTest
public class Vector3Test {
    @Test
    public void angleIncludingTest(){
        Vector3 xDirect = Vector3.getXDirect();
        Vector3 yDirect = Vector3.getYDirect();

        double ans90 = Vector3.angleIncluding(xDirect, yDirect);
        Logger.getLogger().info("ans90 = {}",ans90);
        BaseUtils.Equal.requireTrue(
                BaseUtils.Equal.isEqual(ans90,90.0),ans90
        );
    }

    @Test
    public void angleIncludingTest2(){
        Vector3 xDirect = Vector3.getXDirect();

        BaseUtils.Python.linspaceStream(0,89,20).forEach(angle->{
            double r = BaseUtils.Converter.angleToRadian(angle);
            Vector2 xDirect1 = Vector2.xDirect(10);
            xDirect1.rotateSelf(r);
            double angleIncluding = Vector3.angleIncluding(xDirect, xDirect1.toPoint2().toPoint3().toVector3());
            Logger.getLogger().info("ang:{}/{}",angleIncluding,angle);
            BaseUtils.Equal.requireTrue(
                    BaseUtils.Equal.isEqual(angle,angleIncluding),angle,angleIncluding
            );
        });
    }
}
