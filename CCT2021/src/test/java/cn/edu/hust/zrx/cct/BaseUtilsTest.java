package cn.edu.hust.zrx.cct;

import cn.edu.hust.zrx.cct.base.BaseUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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


    @Test
    public void mapTest(){
        BaseUtils.Content.BiContent<String, Integer> bi = BaseUtils.Content.BiContent.create("123", 321);

        BaseUtils.Content.BiContent<String, String> bi2 = BaseUtils.Content.BiContent.map(bi, s -> s + s, i -> String.valueOf(i + 5));

        Logger.getLogger().info("bi2 = " + bi2);
    }

    @Test
    public void mapTest2(){
        List<BaseUtils.Content.BiContent<Double, String>> bis = BaseUtils.Python.linspaceStream(0, 10, 10).mapToObj(
                v -> BaseUtils.Content.BiContent.create(v, v + "")
        ).collect(Collectors.toList());

        List<BaseUtils.Content.BiContent<Integer, Double>> map = BaseUtils.Content.BiContent.map(bis, v -> (int) (v * 10), s -> Double.parseDouble(s) * 2);

        map.forEach(System.out::println);
    }
}
