package cn.edu.hust.zrx.cct.base;

import cn.edu.hust.zrx.cct.Logger;
import cn.edu.hust.zrx.cct.base.line.Line2;
import cn.edu.hust.zrx.cct.base.point.Point2;
import cn.edu.hust.zrx.cct.base.point.Point3;
import cn.edu.hust.zrx.cct.base.vector.Vector3;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Description
 * 工具类
 * ---------------------------------
 * 2020年2月10日 新增方法
 * Converter
 * angleToRadian(double angle)
 * radianToAngle(double radian)
 * <p>
 * Python
 * linspace(double start,double end,int number)
 * <p>
 * Equal
 * isEqual(double a,double b,double error)
 * isEqual(double a,double b)
 *
 *
 * <p>
 * Data
 * 22:16
 *
 * @author zrx
 * @version 1.0
 */

public class BaseUtils {
    public static class Converter {
        /*-----------------角度/弧度---------------------*/

        /**
         * 角度转弧度制
         *
         * @param angle 角度
         * @return 弧度
         * @since 2020年2月10日
         */
        public static double angleToRadian(double angle) {
            return angle * Math.PI / 180.0;
        }

        /**
         * 弧度转角度
         *
         * @param radian 弧度
         * @return 角度
         * @since 2020年2月10日
         */
        public static double radianToAngle(double radian) {
            return radian / Math.PI * 180.0;
        }
    }

    public static class Python {
        /*-----------------linspace---------------------*/

        /**
         * 来自py numpy库的著名函数 linspace
         * 获得[start, end]上均匀分布的点，数目number个
         *
         * @param start  起始点 包含
         * @param end    终止点 包含
         * @param number 划分点数
         * @return 数组 双精度
         * @since 2020年2月10日
         */
        public static double[] linspace(double start, double end, int number) {
            // 新建数组，长度 number
            double[] doubles = new double[number];

            // 求间距
            double d = (end - start) / (number - 1);

            // 赋值
            for (int i = 0; i < number; i++) {
                doubles[i] = start + d * i;
            }

            // 最后一个数，重新赋值，减少计算误差
            doubles[number - 1] = end;
            return doubles;
        }

        public static DoubleStream linspaceStream(double start, double end, int number) {
            double[] linspace = linspace(start, end, number);
            return Arrays.stream(linspace);
        }

        public static DoubleStream disperse(double length, double footStep) {
            int number = (int) (length / footStep);
            return linspaceStream(0, length, number);
        }

    }

    public static class Equal {
        /**
         * 浮点数 a 和 b 是否相等，误差在 |error| 内
         *
         * @param a     浮点数 1
         * @param b     浮点数 2
         * @param error 误差
         * @return 是否相等
         * @since 2020年2月10日
         */
        public static boolean isEqual(double a, double b, double error) {
            return Math.abs(a - b) <= Math.abs(error);
        }

        /**
         * 浮点数 a 和 b 是否相等，误差在 |1e-8| 内
         *
         * @param a 浮点数 1
         * @param b 浮点数 2
         * @return 是否相等
         * @see BaseUtils::isEqual(double a,double b,double error)
         * @since 2020年2月10日
         */
        public static boolean isEqual(double a, double b) {
            return isEqual(a, b, 1e-8);
        }

        public static boolean isEqual(Vector3 a, Vector3 b) {
            if (!isEqual(a.x, b.x))
                return false;
            if (!isEqual(a.y, b.y))
                return false;
            if (!isEqual(a.z, b.z))
                return false;

            return true;
        }

        public static void requireTrue(boolean b, String msg) {
            if (!b)
                throw new RuntimeException(msg);
        }

        public static void requireFalse(boolean b, String msg) {
            requireTrue(!b, msg);
        }

        public static void requireTrue(boolean b, Object... objects) {
            requireTrue(b, Arrays.toString(objects));
        }

        public static void requireFalse(boolean b, Object... objects) {
            requireFalse(b, Arrays.toString(objects));
        }

        public static void requireNonzero(double a, String msg) {
            requireFalse(isEqual(a, 0.0), msg);
        }

        public static void requireNonzero(double a) {
            requireFalse(isEqual(a, 0.0), a + " = 0!");
        }

        public static void requireNonzero(Vector3 a) {
            requireNonzero(a.length());
        }
    }

    public static class StreamTools {

        /*---------------------------合并流，不是连接流-------------------------*/

        /**
         * 合并流 注意不是连接流
         * 输入多个流，如 [1, 2, 3, 4] 和 [5, 6, 7, 8]
         * 则返回流 [ [1,5], [2,6], [3,7], [4,8] ]
         *
         * @param streamList 要合并的流
         * @param <E>        流元素
         * @return 合并后的流
         */
        public static <E> Stream<List<E>> combine(final List<Stream<E>> streamList) {
            // List<Stream<E>> 中的 Stream<E> 转为 List<E>
            final List<List<E>> listListE = new ArrayList<>(streamList.size());
            streamList.forEach(stream -> listListE.add(stream.collect(Collectors.toList())));

            // 查看每个 List<E> 的长度知否一致
            // 不一致 则 log error
            // 取 min 作为新的流的长度
            IntSummaryStatistics intSummaryStatistics = listListE.stream().
                    mapToInt(List::size).summaryStatistics();
            int max = intSummaryStatistics.getMax();
            int min = intSummaryStatistics.getMin();
            if (max != min) {
                Logger.getLogger().error("BaseUtils.StreamTools::combine合并了不同长度的流");
                Logger.getLogger().error("流长度 max = " + max + "，min = " + min);
            }

            // 转为 Stream<List<E>>
            return StreamTools.fromZeroIncluding().to(min - 1)
                    .mapToObj(i -> {
                        final List<E> itemList = new ArrayList<>(streamList.size());
                        listListE.forEach(listE -> itemList.add(listE.get(i)));
                        return itemList;
                    });
        }

        /**
         * 合并流 注意不是连接流
         *
         * @param streams 要合并的流
         * @param <E>     流元素
         * @return 合并后的流
         * @see BaseUtils.StreamTools#combine(List)
         */
        @SafeVarargs
        public static <E> Stream<List<E>> combine(Stream<E>... streams) {
            return combine(Arrays.asList(streams));
        }

        /*------------------------------自然数流-------------------------*/

        /**
         * 产生一个[a, b]的 IntStream
         * 具体使用方法见下
         *
         * @param beginIncluding 起点包括
         * @return NaturalNumberStreamWithFrom
         */
        public static NaturalNumberStreamWithFrom from(int beginIncluding) {
            return new NaturalNumberStreamWithFrom(beginIncluding);
        }

        /**
         * 产生一个[0, b]的 IntStream
         *
         * @return NaturalNumberStreamWithFrom
         * @see StreamTools#from(int)
         */
        public static NaturalNumberStreamWithFrom fromZeroIncluding() {
            return from(0);
        }

        /**
         * 产生一个[1, b]的 IntStream
         *
         * @return NaturalNumberStreamWithFrom
         * @see StreamTools#from(int)
         */
        public static NaturalNumberStreamWithFrom fromOneIncluding() {
            return from(1);
        }

        /**
         * NaturalNumberStream 内部辅助类
         */
        public static class NaturalNumberStreamWithFrom {
            private int beginIncluding;

            public NaturalNumberStreamWithFrom(int beginIncluding) {
                this.beginIncluding = beginIncluding;
            }

            public IntStream to(int endIncluding) {
                Equal.requireTrue(endIncluding >= beginIncluding,
                        "自然数流构造失败" + endIncluding + "<" + beginIncluding);

                int[] arr = new int[endIncluding - beginIncluding + 1];
                for (int i = 0; i < arr.length; i++) {
                    arr[i] = beginIncluding + i;
                }

                return Arrays.stream(arr);
            }
        }

        /*-----------------------for-n-------------------------*/

        /**
         * 相当于 for(i=beginIncluding;i<endExcluding;i++)
         *
         * @param beginIncluding beginIncluding
         * @param endExcluding   endExcluding
         * @return IntStream
         */
        public static IntStream forN(int beginIncluding, int endExcluding) {
            return from(beginIncluding).to(endExcluding - 1);
        }

        /**
         * 相当于 for(i=0;i<endExcluding;i++)
         *
         * @param endExcluding endExcluding
         * @return IntStream
         */
        public static IntStream forZeroToN(int endExcluding) {
            return fromZeroIncluding().to(endExcluding - 1);
        }

        public static IntStream repeat(int times) {
            Equal.requireTrue(times >= 0, times);
            return forZeroToN(times);
        }
    }

    public static class Magnet {
        /**
         * 计算点 p0 到点 p1 电流为 I 的直导线在 p 点产生的磁场。
         * 使用 毕奥-萨伐尔定律。当p0 p1很近时，即为 dB，可用作数值积分 B = ∫dB
         * 还有十足的优化空间
         *
         * @param p0 直导线起点
         * @param p1 直导线终点
         * @param I  电流，从 p0 流向 p1
         * @param p  需要计算磁场的点
         * @return p 点的磁场
         */
        private static Vector3 dB(final Point3 p0, final Point3 p1, double I, final Point3 p) {
            Vector3 p01 = Vector3.subtract(p1, p0);
            Vector3 r = Vector3.subtract(p, p0);
            double rr = r.length();

            return Vector3.dot(Vector3.cross(p01, r),
                    I * 1e-7 / rr / rr / rr);
        }

        /**
         * 计算由 Vector3d[] windings 描述的线圈在点 p 产生的磁场
         * 电流方向为 v3s[i]->v3s[i+1] 电流大小为 I 。若 I 为负数，当人和上述方向相反
         * <p>
         * 注意这里不确认 Vector3d[] v3s 是否描述的是一段轨迹，函数仅仅调用 Magnet.dB 进行积分式的计算
         * 可以通过 Plot3d.plot3(v3s) 绘图哟~
         *
         * @param windings 线圈轨迹(不确认)
         * @param I        电流
         * @param p        需要求磁场的点
         * @return P 点的磁场
         */
        public static Vector3 magnetAtPoint(final List<Point3> windings, final double I, final Point3 p) {
            //用于累加磁场
            Vector3 B = Vector3.getZero();

            //获得段数
            int num = windings.size() - 1;

            for (int i = 0; i < num; i++) {
                B.addSelf(dB(windings.get(i), windings.get(i + 1), I, p));
            }

            return B;
        }

        /**
         * 计算多个线圈在 p 点的磁场
         *
         * @param windingsList 多个线圈
         * @param I            电流
         * @param p
         * @return
         * @see BaseUtils.Magnet#magnetAtPoint(List, double, Point3)
         */
        public static Vector3 magnetAtPoint(
                final Collection<List<Point3>> windingsList, final double I, final Point3 p) {
            Vector3 m = Vector3.getZero();
            for (List<Point3> windings : windingsList) {
                m.addSelf(magnetAtPoint(windings, I, p));
            }

            return m;
        }

        /**
         * 计算轨迹 trajectory 上磁场 Bz 分布
         *
         * @param windings    线圈轨迹
         * @param I           电流
         * @param trajectory  轨迹
         * @param deltaLength 轨迹离散分段长度
         * @return (s, Bz)
         */
        public static List<Point2> magnetBzAlongTrajectory(
                final List<Point3> windings, final double I, final Line2 trajectory, final double deltaLength) {
            return trajectory.dispersePoint3sWithDistance(deltaLength)
                    .stream()
                    .map(
                            point3WithDistance -> Point2.create(
                                    point3WithDistance.getDistance(),
                                    magnetAtPoint(windings, I, point3WithDistance.getPoint3()).z
                            )
                    ).collect(Collectors.toList());
        }


        /**
         * 计算轨迹 trajectory 右手侧 rightHandOffset 处的磁场 Bz 分布
         * 仅仅用于 梯度、六极场分析等
         *
         * @param windings        线圈轨迹
         * @param I               电流
         * @param trajectory      轨迹
         * @param deltaLength     轨迹离散分段长度
         * @param rightHandOffset 右手侧位置
         * @return (s, Bz)
         */
        private static List<Point2> magnetBzAlongTrajectory(
                final List<Point3> windings, final double I, final Line2 trajectory,
                final double deltaLength, final double rightHandOffset) {
            return magnetBzAlongTrajectory(windings, I, trajectory.rightHandSideLine2(rightHandOffset), deltaLength);
        }


        /**
         * 快速计算轨迹 trajectory 上磁场 Bz 方向的梯度分布
         * 因为是快速计算，所以只用于简单分析
         * 计算方法：梯度 = 好场区两端磁场Bz差 / 好场区宽度
         *
         * @param windings           线圈轨迹
         * @param I                  电流
         * @param trajectory         轨迹
         * @param deltaLength        轨迹离散分段长度
         * @param goodFieldAreaWidth 好场区宽度，实际计算中会使用好场区端点的磁场值
         * @return (s, 梯度)
         */
        public static List<Point2> magnetGradientAlongTrajectoryFast(
                final List<Point3> windings, final double I, final Line2 trajectory,
                final double deltaLength, final double goodFieldAreaWidth) {
            return StreamTools.combine(
                    magnetBzAlongTrajectory(windings, I, trajectory, deltaLength, -goodFieldAreaWidth).stream(),
                    magnetBzAlongTrajectory(windings, I, trajectory, deltaLength, goodFieldAreaWidth).stream()
            ).map(point2s ->
                    Point2.create(
                            point2s.get(0).x,
                            (point2s.get(0).y - point2s.get(1).y) / (2 * goodFieldAreaWidth)
                    )
            ).collect(Collectors.toList());
        }

    }

    public static class ArrayUtils {
        public static double[] repeat(double item, int times) {
            Equal.requireTrue(times >= 0, "ArrayUtils#repeat times应大于0", times);
            double[] ret = new double[times];
            Arrays.fill(ret, item);

            return ret;
        }
    }

    public static class ListUtils {
        public static <E> List<E> listListToList(List<List<E>> listList) {
            List<E> list = new ArrayList<>();
            for (List<E> es : listList) {
                list.addAll(es);
            }

            return list;
        }
    }

    public static class Switcher<Obj> {
        private final Obj[] objs;
        private int currentItem = 0;

        public Obj getAndSwitch() {
            Obj obj = objs[currentItem % objs.length];
            switching();
            return obj;
        }

        public int getSize() {
            return objs.length;
        }

        private void switching() {
            currentItem++;
        }

        @SuppressWarnings("all")
        public static <Integer> Switcher<Integer> creat(int times) {
            Integer[] objects = (Integer[]) StreamTools.forZeroToN(times - 1)
                    .boxed()
                    .toArray();

            Switcher<Integer> integerSwitcher = new Switcher<>(objects);

            return integerSwitcher;
        }

        @SafeVarargs
        public static <Obj> Switcher<Obj> creat(Obj... objs) {
            return new Switcher<>(objs);
        }

        private Switcher(Obj[] objs) {
            this.objs = objs;
        }
    }

    public static class Content {
        //容器，装两个任意对象，有泛型
        public static class BiContent<T1, T2> {
            private final T1 t1;
            private final T2 t2;

            public static <T1,T2> BiContent<T1,T2> create(T1 t1, T2 t2) {
                return new BiContent<>(t1, t2);
            }

            private BiContent(T1 t1, T2 t2) {
                this.t1 = t1;
                this.t2 = t2;
            }

            public T1 getT1() {
                return t1;
            }

            public T2 getT2() {
                return t2;
            }

            @Override
            public String toString() {
                return toStringWithInfo(t1.getClass().toString(),t2.getClass().toString());
            }

            public String toStringWithInfo(String infoT1,String infoT2){
                return "[" + infoT1 + " = " + t1.toString() + ", " + infoT2 + " = " + t2.toString() + "]";
            }
        }
    }

    public static class Timer{
        // 静态方法 每两次调用，打印中间时间
        private static long firstCallTime = -1;
        public static void printPeriodPerSecondCall(org.slf4j.Logger logger){
            long l = System.currentTimeMillis();
            if(firstCallTime==-1)
                firstCallTime = l;
            else {
                long period = l - firstCallTime;
                firstCallTime = -1;
                if(logger==null){
                    System.out.println("运行时间： "+period+"ms");
                }else {
                    logger.info("运行时间： {}ms",period);
                }
            }
        }

        // 实例方法，打印创建该实例后经过的时间
        public long initialTime;
        public Timer(){initialTime = System.currentTimeMillis();}
        public void printPeriodAfterInitial(org.slf4j.Logger logger){
            long period = System.currentTimeMillis() - initialTime;
            if(logger==null){
                System.out.println("计时后运行时间： "+period+"ms");
            }else {
                logger.info("计时后运行时间： {}ms",period);
            }
        }
    }

    public static class Async{
        private ExecutorService executorService;

        public Async() {
            int processors = Runtime.getRuntime().availableProcessors();
            Logger.getLogger().info("启动fixed线程池，线程数 = " + processors);;
            executorService =
                    Executors.newFixedThreadPool(processors);
        }

        public void execute(Runnable r){
            executorService.submit(r);
        }

        public void waitForAllFinish(long timeout, TimeUnit unit){
            try {
                executorService.shutdown();
                executorService.awaitTermination(timeout,unit);
            }catch (InterruptedException e){
                e.printStackTrace();
                System.exit(-1);
            }
        }
    }
}
