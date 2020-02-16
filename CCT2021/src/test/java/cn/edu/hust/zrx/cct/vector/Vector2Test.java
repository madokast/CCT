package cn.edu.hust.zrx.cct.vector;

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

@SpringBootTest
public class Vector2Test {

    @Test
    public void fromToTest(){
        Vector2 a = Vector2.from(Point2.create(2, 3))
                .to(Point2.create(4, 5));
        Vector2 b = Vector2.create(2, 2);
        Assert.isTrue(Vector2.isEqual(a,b),"fromToTest pass");
    }

}
