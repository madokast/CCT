package cn.edu.hust.zrx.cct;

import cn.edu.hust.zrx.cct.base.BaseUtils;
import cn.edu.hust.zrx.cct.base.cct.SoleLayerCct;
import cn.edu.hust.zrx.cct.base.particle.ParticleFactory;
import cn.edu.hust.zrx.cct.base.particle.ParticleRunner;
import cn.edu.hust.zrx.cct.base.particle.RunningParticle;
import cn.edu.hust.zrx.cct.base.point.Point3;
import cn.edu.hust.zrx.cct.base.python.Plot2d;
import cn.edu.hust.zrx.cct.base.python.Plot3d;
import cn.edu.hust.zrx.cct.base.vector.Vector3;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

import static cn.edu.hust.zrx.cct.base.BaseUtils.Constant.MM;
import static cn.edu.hust.zrx.cct.base.BaseUtils.Magnet.dB;

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


public class BaseUtilsTest {
    @Test
    public void streamCombineTest() {
        BaseUtils.StreamTools.combine(List.of(
                BaseUtils.StreamTools.from(1).to(4).boxed(),
                BaseUtils.StreamTools.from(5).to(8).boxed()
        )).forEach(System.out::println);
    }

    @Test
    public void forN() {
        BaseUtils.StreamTools.forZeroToN(10).forEach(System.out::println);
    }

    @Test
    public void repeat() {
        BaseUtils.StreamTools.repeat(5).forEach(System.out::println);
    }


    @Test
    public void mapTest() {
        BaseUtils.Content.BiContent<String, Integer> bi = BaseUtils.Content.BiContent.create("123", 321);

        BaseUtils.Content.BiContent<String, String> bi2 = BaseUtils.Content.BiContent.map(bi, s -> s + s, i -> String.valueOf(i + 5));

        Logger.getLogger().info("bi2 = " + bi2);
    }

    @Test
    public void mapTest2() {
        List<BaseUtils.Content.BiContent<Double, String>> bis = BaseUtils.Python.linspaceStream(0, 10, 10).mapToObj(
                v -> BaseUtils.Content.BiContent.create(v, v + "")
        ).collect(Collectors.toList());

        List<BaseUtils.Content.BiContent<Integer, Double>> map = BaseUtils.Content.BiContent.map(bis, v -> (int) (v * 10), s -> Double.parseDouble(s) * 2);

        map.forEach(System.out::println);
    }

    @Test
    public void repeatTest() {
        BaseUtils.Equal.requireEqual(
                new String[]{"a", "a", "a"},
                BaseUtils.ArrayUtils.repeat(3, "a")
        );

        BaseUtils.Equal.requireEqual(
                new String[]{"a", "b", "a", "b"},
                BaseUtils.ArrayUtils.repeat(2, "a", "b")
        );
    }

    @Test
    public void testBD() {
        Point3 p0 = Point3.create(1, 1, 1);
        Point3 p1 = Point3.create(1.1, 1.2, 1.3);
        Point3 p = Point3.create(0.0, 0.0, 0.0);

        // I * 1e-7

        Vector3 m = dB(p0, p1, 1e7, p);
        System.out.println("m = " + m);
    }

    @Test
    public void testMagnetAtPoint() {
        long s = System.currentTimeMillis();

        float r = 0.01f;
        float length = 0.1f;
        int n = 20;
        int pas = 360;

        List<Point3> line = BaseUtils.Python.linspaceStream(0, n * 2 * Math.PI, n * pas)
                .mapToObj(t -> Point3.create(r * Math.cos(t), r * Math.sin(t), t / (n * 2 * Math.PI) * length))
                .collect(Collectors.toList());

        Point3 p = Point3.origin();

        Vector3 m = BaseUtils.Magnet.magnetAtPoint(line, 10000f, p);

        System.out.println("m = " + m);

        System.out.println("time = " + (System.currentTimeMillis() - s));

    }

    @Test
    public void testRunt() {
        long s = System.currentTimeMillis();

        float r = 0.01f;
        float length = 0.1f;
        int n = 20;
        int pas = 360;

        float current = 10000f;

        List<Point3> line = BaseUtils.Python.linspaceStream(0, n * 2 * Math.PI, n * pas)
                .mapToObj(t -> Point3.create(r * Math.cos(t), r * Math.sin(t), t / (n * 2 * Math.PI) * length))
                .collect(Collectors.toList());


        RunningParticle proton = ParticleFactory.createProton(
                Point3.origin(),
                Vector3.getZDirect(),
                250
        );

        System.out.println("proton = " + proton);

        SoleLayerCct cct = SoleLayerCct.create(line, current);

        //[0.016659991558021645, 9.641212628421269E-4, 1.2504328150541577]
        Vector3 m0 = cct.magnetAt(Point3.origin());
        Logger.getLogger().info("m0 = " + m0);

        List<Point3> tr = ParticleRunner.run(proton, cct, 1, MM);

        System.out.println("proton = " + proton);


        System.out.println("time = " + (System.currentTimeMillis() - s));

        Plot3d.plot3(tr, Plot2d.BLACK_LINE);

        cct.plot3d();

        Plot3d.showThread();

    }

    @Test
    public void testFloatDouble() {
        float f = 0.0f;
        double d = 0.0;
        double dd = 0.0;

        for (int i = 0; i < 2; i++) {
            double t = Math.sin(i) * Math.pow(10, i % 10 - 5);
            f += (float) t;
            d += (float) t;
            dd += t;
        }

        System.out.println("f = " + f);
        System.out.println("d = " + d);
        System.out.println("dd = " + dd);
    }

    @Test
    public void test_sin_sum() {
        int number = 1000;

        float sum = 0.0f;
        int i;
        for (i = 0; i < number; i++) {
            sum += (float) Math.sin(((float) i) / 180.0f * Math.PI);
        }

        Logger.getLogger().info("sum = " + sum);
    }

    @Test
    public void testRunt_sin_table() {
        float[] sinTable = new float[360];
        for (int i = 0; i < 360; i++) {
            sinTable[i] = (float) Math.sin(i / 180.0 * Math.PI);
        }


        long s = System.currentTimeMillis();

        float r = 0.01f;
        float length = 0.1f;
        int n = 20;
        int pas = 360;

        float current = 10000f;

        Point3 p = Point3.origin();
        Vector3 m = Vector3.getZero();

        int DELTA_DEG = 1;
        int TOTAL_DEG = 360 * n;
        int pre_deg = 0;
        int cur_deg = DELTA_DEG;

        Point3 pre_point = Point3.create(r * sinTable[pre_deg + 90], r * sinTable[pre_deg], length * pre_deg / 360.0 / n);

        while (cur_deg <= TOTAL_DEG) {
            Point3 cur_point = Point3.create(r * sinTable[(cur_deg + 90) % 360],
                    r * sinTable[cur_deg % 360], length * cur_deg / 360.0 / n);

            Vector3 db = dB(pre_point, cur_point, current, p);
            m.addSelf(db);

            pre_point = cur_point;

            cur_deg += DELTA_DEG;
        }

        // [0.01665999041053552, 9.641210474562111E-4, 1.250432803174737]
        Logger.getLogger().info("m = " + m);

    }
}
