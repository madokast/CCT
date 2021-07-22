package cn.edu.hust.zrx.cct.line;

import cn.edu.hust.zrx.cct.base.BaseUtils;
import cn.edu.hust.zrx.cct.base.line.Trajectory;
import cn.edu.hust.zrx.cct.base.line.TrajectoryFactory;
import cn.edu.hust.zrx.cct.base.point.Point2;
import cn.edu.hust.zrx.cct.base.vector.Vector2;
import org.junit.jupiter.api.Test;
//import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;

/**
 * Description
 * line包测试
 * <p>
 * Data
 * 23:06
 *
 * @author zrx
 * @version 1.0
 */

//@SpringBootTest
public class LineTests {
    @Test
    public void trajectoryTest(){
        //轨迹的建立方法，类似于我写的matLab 机架绘图
        //使用 TrajectoryFactory 工厂类
        // 第一步： 确定起点 setStartingPoint 传入 Point2
        // 第二步： 总是设定线段(轨迹的第一段总是线段) setStraightLine 传入 length 和 direct (Vector2)
        // 之后得到的就是一个 Trajectory
        // Trajectory 可以往后不断地增加 setStraightLine / addArcLine

        // 关于 setStraightLine 后接线段，只需要给定长度
        // 关于 addArcLine 后接圆弧，给定 半径、顺时针与否、角度


        Trajectory trajectory =
                TrajectoryFactory.fromOrigin()
                .setStraightLine(1, Vector2.xDirect())
                .addArcLine(0.75, false, 22.5)
                .addStraitLine(1)
                .addArcLine(0.75, false, 22.5)
                .addStraitLine(3)
                .addArcLine(0.75, true, 67.5)
                .addStraitLine(0.5)
                .addArcLine(0.75, true, 67.5)
                .addStraitLine(3);


        double length = trajectory.getLength();
        System.out.println("length = " + length);

        Arrays.stream(BaseUtils.Python.linspace(0, length, 1000))
                .mapToObj(trajectory::pointAt)
                .forEach(p->{
                    System.out.println(p.x+"\t"+p.y);
                });
    }

}
