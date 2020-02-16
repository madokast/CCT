package cn.edu.hust.zrx.cct;

import cn.edu.hust.zrx.cct.base.BaseUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * Description
 * BaseUtilsTest
 * <p>
 * Data
 * 9:52
 *
 * @author zrx
 * @version 1.0
 */

@SpringBootTest
public class BaseUtilsTest {
    @Test
    public void streamCombineTest(){
        BaseUtils.StreamTools.combine(List.of(
                BaseUtils.StreamTools.from(1).to(4).boxed(),
                BaseUtils.StreamTools.from(5).to(8).boxed()
        )).forEach(System.out::println);
    }

    @Test
    public void forN(){
        BaseUtils.StreamTools.forZeroToN(10).forEach(System.out::println);
    }

    @Test
    public void repeat(){
        BaseUtils.StreamTools.repeat(5).forEach(System.out::println);
    }
}
