package cn.edu.hust.zrx.cct.base.line;

import cn.edu.hust.zrx.cct.Logger;
import cn.edu.hust.zrx.cct.advanced.BiBiMathFunction;
import cn.edu.hust.zrx.cct.base.BaseUtils;
import cn.edu.hust.zrx.cct.base.point.Point2;
import cn.edu.hust.zrx.cct.base.point.Point3;
import cn.edu.hust.zrx.cct.base.vector.Vector2;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Description
 * 流线
 * 自慢するコーダです
 * 使用实例：
 * <pre>
 *         @run(value = 3,code = "六极场01")
 *     public void test3(){
 *
 *
 *         for (int i = -5; i <=5; i++) {
 *             if(i==0)
 *                 continue;//break
 *             for (int j = -5; j <=5 ; j++) {
 *                 if(j==0)
 *                     continue;
 *
 *                 List<Point2> list = StreamLineFactory.create(
 *                         pp -> Point2.convert(pp, (x, y) -> 2*x*y, (x, y) -> x*x-y*y),
 *                         Point2.create(i,j),
 *                         MM,
 *                         5, -5, 5, -5
 *                 );
 *
 *                 Plot2d.plot2(list,Plot2d.BLACK_LINE);
 *             }
 *         }
 *
 *         Plot2d.equal();
 *         Plot2d.showThread();
 *     }
 * </pre>
 * <p>
 * Data
 * 22:32
 *
 * @author zrx
 * @version 1.0
 */

public class StreamLineFactory {

    /**
     * 画矢量图，流形式
     * 仅仅画一个流
     * 现在是用于 四极场 六极场分析
     *
     * @param bbf        (x,y) -> (u,v)
     * @param startPoint 起点，虽说是起点，但是会自动前后走动。主要是为了防止零场强点
     * @param footStep   步长
     * @param xMax       x方向能走到的最大值
     * @param xMin       x方向能走到的最小值
     * @param yMax       y方向能走到的最大值
     * @param yMin       y方向能走到的最小值
     * @return 这条流线
     */
    @SuppressWarnings("unchecked")
    public static List<Point2> create(BiBiMathFunction bbf, Point2 startPoint, double footStep, double xMax, double xMin, double yMax, double yMin) {
        Logger.getLogger().info("StreamLineFactory:startPoint = {}", startPoint);


        Deque<Point2> deque = new LinkedList<>();
        deque.add(startPoint);

        long maxSteps = (long) (2 * Math.PI * (xMax - xMin + yMax - yMin) / 2 / 2 / footStep);

        Point2 p = startPoint.copy();

        // go ahead
        for (; ; ) {
            Vector2 uv = bbf.fun(p).toVector2();

            if (uv.length() < 1e-8) {
                break;
            }

            uv.changeLengthSelf(footStep);


            deque.addLast(p.moveSelf(uv).copy());

            if (deque.size() > maxSteps) {
                break;
            }

            if (p.x > xMax)
                break;
            if (p.x < xMin)
                break;
            if (p.y > yMax)
                break;
            if (p.y < yMin)
                break;
        }

        // go back
        p = startPoint.copy();
        for (; ; ) {
            Vector2 uv = bbf.fun(p).toVector2();

            if (uv.length() < 1e-8)
                break;

            uv.changeLengthSelf(footStep);

            deque.addFirst(p.moveSelf(uv.reverseSelf()).copy());

            if (deque.size() > maxSteps * 2)
                break;

            if (p.x > xMax)
                break;
            if (p.x < xMin)
                break;
            if (p.y > yMax)
                break;
            if (p.y < yMin)
                break;
        }

        return (List<Point2>) deque;

    }

}
