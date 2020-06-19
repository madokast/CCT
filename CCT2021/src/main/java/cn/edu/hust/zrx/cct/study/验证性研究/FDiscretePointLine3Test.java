package cn.edu.hust.zrx.cct.study.验证性研究;

import cn.edu.hust.zrx.cct.Logger;
import cn.edu.hust.zrx.cct.base.BaseUtils;
import cn.edu.hust.zrx.cct.base.line.DiscretePointLine3;
import cn.edu.hust.zrx.cct.base.point.Point3;
import cn.edu.hust.zrx.cct.base.python.Plot3d;
import cn.edu.hust.zrx.cct.base.vector.Vector3;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Description
 * DiscretePointLine3 Test
 * <p>
 * Data
 * 16:57
 *
 * @author zrx
 * @version 1.0
 */

public class FDiscretePointLine3Test {

    public static void main(String[] args) {

        List<Point3> point3List = List.of(
                Point3.create(0, 0, 0),
                Point3.create(1, 0, 0),
                Point3.create(1, 1, 0)
        );

        DiscretePointLine3 line3 = DiscretePointLine3.create(point3List);

        double length = line3.getLength();
        Logger.getLogger().info("length = " + length);

        List<Point3> collect = BaseUtils.Python.linspaceStream(0, length, 20)
                .mapToObj(line3::pointAt).peek(System.out::println).collect(Collectors.toList());

        List<BaseUtils.Content.TriContent<Double, Point3, Vector3>> discreteLine = line3.getDiscreteLine();
        discreteLine.forEach(System.out::println);

        BaseUtils.Python.linspaceStream(0, length, 20)
                .mapToObj(line3::directAt).forEach(System.out::println);

        Plot3d.plot3(collect);

        Plot3d.showThread();
    }
}
