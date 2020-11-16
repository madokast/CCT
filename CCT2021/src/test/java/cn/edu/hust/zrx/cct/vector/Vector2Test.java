package cn.edu.hust.zrx.cct.vector;

import cn.edu.hust.zrx.cct.Logger;
import cn.edu.hust.zrx.cct.base.point.Point2;
import cn.edu.hust.zrx.cct.base.vector.Vector2;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

/**
 * Description
 * Vector2Test
 * <p>
 * Data
 * 15:41
 *
 * @author zrx
 * @version 1.0
 */


public class Vector2Test {

    @Test
    public void fromToTest(){
        Vector2 a = Vector2.from(Point2.create(2, 3))
                .to(Point2.create(4, 5));
        Vector2 b = Vector2.create(2, 2);
        Assert.isTrue(Vector2.isEqual(a,b),"fromToTest pass");
    }

    @Test
    public void testRotate(){
        Vector2 v = Vector2.create(2, 3);
        Vector2 r1 = Vector2.rotate(v, 0.1);
        Vector2 r2 = Vector2.rotate(v, 0.2);
        Vector2 r3 = Vector2.rotate(v, -0.1);
        Vector2 r4 = Vector2.rotate(v, 1.0);

        Logger.getLogger().info("r1 = " + r1);
        Logger.getLogger().info("r2 = " + r2);
        Logger.getLogger().info("r3 = " + r3);
        Logger.getLogger().info("r4 = " + r4);

    }

}
