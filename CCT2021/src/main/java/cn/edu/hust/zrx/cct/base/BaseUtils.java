package cn.edu.hust.zrx.cct.base;

import cn.edu.hust.zrx.cct.Logger;
import cn.edu.hust.zrx.cct.base.line.Line2;
import cn.edu.hust.zrx.cct.base.point.Point2;
import cn.edu.hust.zrx.cct.base.point.Point3;
import cn.edu.hust.zrx.cct.base.vector.Vector2;
import cn.edu.hust.zrx.cct.base.vector.Vector3;

import java.io.*;
import java.lang.reflect.Array;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.IntToDoubleFunction;
import java.util.function.Predicate;
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

        public static double[] angleToRadian(double[] angles) {
            return Arrays.stream(angles).map(Converter::angleToRadian).toArray();
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
         * ## 2020年6月10日 重构 支持 start > end 的情况
         *
         * @param start  起始点 包含
         * @param end    终止点 包含
         * @param number 划分点数
         * @return 数组 双精度
         * @since 2020年2月10日
         */
        public static double[] linspace(double start, double end, int number) {
//            if (start - end > 1e-8)
//                throw new IllegalArgumentException("start(" + start + ")>end(" + end + ")!!");

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

        // 依赖 linspace(double start, double end, int number)
        public static Point3[] linspace(Point3 start, Point3 end, int number) {
            double[] xs = linspace(start.x, end.x, number);
            double[] ys = linspace(start.y, end.y, number);
            double[] zs = linspace(start.z, end.z, number);

            Point3[] ps = new Point3[number];

            for (int i = 0; i < ps.length; i++) {
                ps[i] = Point3.create(xs[i], ys[i], zs[i]);
            }

            return ps;
        }

        // 依赖 linspace(double start, double end, int number)
        public static DoubleStream linspaceStream(double start, double end, int number) {
            double[] linspace = linspace(start, end, number);
            return Arrays.stream(linspace);
        }

        // 依赖 linspace(double start, double end, int number)
        public static Stream<Point3> linspaceStream(Point3 start, Point3 end, int number) {
            Point3[] linspace = linspace(start, end, number);
            return Arrays.stream(linspace);
        }

        // 依赖 linspace(double start, double end, int number)
        public static DoubleStream disperse(double length, double footStep) {
            int number = (int) (length / footStep);
            return linspaceStream(0, length, number);
        }


        public static <E> List<E> linspaceFunction(Function<Double, E> fun, double start, double end, int number) {
            return linspaceStream(start, end, number).mapToObj(fun::apply).collect(Collectors.toList());
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
            return Math.abs(a - b) <= Math.abs(error) || Math.max(Math.abs(a / b), Math.abs(b / a)) <= Math.abs(1 + error);
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

        public static boolean isEqual(int a, int b) {
            return a == b;
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

        public static void requireEqual(double a, double b, String msg) {
            if (!isEqual(a, b))
                throw new RuntimeException(msg);
        }

        public static void requireEqual(int a, int b, String msg) {
            if (!isEqual(a, b))
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

        public static void requireNonzero(int a, String msg) {
            requireFalse(isEqual(a, 0), msg);
        }

        public static void requireNonzero(double a) {
            requireFalse(isEqual(a, 0.0), a + " = 0!");
        }

        public static void requireNonzero(Vector3 a) {
            requireNonzero(a.length());
        }

        public static void requireEqual(int a, int b) {
            if (!isEqual(a, b))
                throw new RuntimeException(a + "不等于" + b);
        }

        public static void requireEqual(Object a, Object b, String msg) {
            if (a instanceof Object[] && b instanceof Object[]) {
                if (!Arrays.deepEquals((Object[]) a, (Object[]) b)) {
                    throw new RuntimeException(msg);
                }
            } else if (!Objects.equals(a, b)) {
                throw new RuntimeException(msg);
            }
        }

        public static void requireEqual(Object a, Object b) {
            requireEqual(a, b, a + "不等于" + b);
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

        public static class Average {
            double sum = 0;
            int num = 0;

            public static Average create(){
                return new Average();
            }

            public void add(double val) {
                sum += val;
                num++;
            }

            public double average() {
                return sum / num;
            }
        }
    }

    public static class Magnet {
        /**
         * 计算点 p0 到点 p1 电流为 I 的直导线在 p 点产生的磁场。
         * 使用 毕奥-萨伐尔定律。当p0 p1很近时，即为 dB，可用作数值积分 B = ∫dB
         * 还有十足的优化空间
         * <p>
         * 2020年4月17日 修改代码
         * r 应该是 p0 p1 中点到 p
         *
         * @param p0 直导线起点
         * @param p1 直导线终点
         * @param I  电流，从 p0 流向 p1
         * @param p  需要计算磁场的点
         * @return p 点的磁场
         */
        public static Vector3 dB(final Point3 p0, final Point3 p1, double I, final Point3 p) {
            Vector3 p01 = Vector3.subtract(p1, p0);
            Vector3 r = Vector3.subtract(p, Point3.midPoint(p0, p1));
//            Vector3 r = Vector3.subtract(p, p0);
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
            if (Equal.isEqual(I, 0.0, 1e-8)) {
                //Logger.getLogger().warn("magnetAtPoint:存在零电流的线圈，该磁场直接设为0，请确认");
                return Vector3.getZero();
            }

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

        @SuppressWarnings("unchecked")
        public static <T> T[] repeat(T item, int times) {
            Equal.requireTrue(times >= 0, "ArrayUtils#repeat times应大于0", times);
            T[] ret = (T[]) Array.newInstance(item.getClass(), times);
            Arrays.fill(ret, item);

            return ret;
        }

        @SuppressWarnings("unchecked")
        public static <T> T[] repeat(int times, T... items) {
            Equal.requireTrue(times >= 0, "ArrayUtils#repeat times应大于0", times);
            int itemsLen = items.length;
            Equal.requireNonzero(itemsLen, "T...items 不能为0");

            return StreamTools.repeat(times)
                    .mapToObj(ig -> items)
                    .flatMap(Stream::of)
                    .collect(Collectors.toList())
                    .toArray((T[]) Array.newInstance(items[0].getClass(), times * itemsLen));
        }

        public static double[] add(double[] arr0, double[] arr1) {
            Equal.requireEqual(arr0.length, arr1.length);

            double[] ret = new double[arr0.length];

            for (int i = 0; i < ret.length; i++) {
                ret[i] = arr0[i] + arr1[i];
            }

            return ret;
        }

//        public static double[] multiple(double[] arr, double a) {
//            double[] ret = new double[arr.length];
//
//            for (int i = 0; i < ret.length; i++) {
//                ret[i] = arr[i] * a;
//            }
//
//            return ret;
//        }

        public static double[] apply(double[] arr, Function<Double, Double> fun) {
            double[] ret = new double[arr.length];

            for (int i = 0; i < ret.length; i++) {
                ret[i] = fun.apply(arr[i]);
            }

            return ret;
        }

        public static <T> T[] append(T[] arr, T... appending) {
            T[] ret = (T[]) Array.newInstance(arr.getClass().componentType(), arr.length + appending.length);

            System.arraycopy(arr, 0, ret, 0, arr.length);

            System.arraycopy(appending, 0, ret, arr.length, appending.length);

            return ret;
        }

        /**
         * [1,2,3] / 2 = [0.5, 1.0, 1.5]
         *
         * @param dividends 被除数
         * @param divisor   除数
         * @return 商
         */
        public static double[] div(double[] dividends, double divisor) {
            return Arrays.stream(dividends).map(d -> d / divisor).toArray();
        }

        // 见上
        public static double[] div(double[] dividends, double[] divisors) {
            Equal.requireEqual(divisors.length, dividends.length);
            double[] ret = new double[dividends.length];
            for (int i = 0; i < dividends.length; i++) {
                ret[i] = dividends[i] / divisors[i];
            }
            return ret;
        }

        public static double[] div(double[] dividends, int[] divisors) {
            return div(dividends, asDoubleArr(divisors));
        }

        public static double[] asDoubleArr(int[] divisors) {
            return Arrays.stream(divisors).mapToDouble(Double::valueOf).toArray();
        }

        public static double[] multiple(double[] arr, double fact) {
            return Arrays.stream(arr).map(a -> a * fact).toArray();
        }

        public static double[] multiple(int[] arr, double fact) {
            return Arrays.stream(arr).mapToDouble(a -> a * fact).toArray();
        }

        public static double[] dot(double[] arr, double fact) {
            return multiple(arr, fact);
        }
    }

    public static class ListUtils {

        public static <T> T sum(List<T> list, BiFunction<T, T, T> sumFunction) {
            if (list == null || list.size() == 0) {
                throw new IllegalArgumentException("list不能为空");
            }

            T ret = list.get(0);

            for (int i = 1; i < list.size(); i++) {
                T t = list.get(i);

                ret = sumFunction.apply(ret, t);
            }

            return ret;
        }

        public static <T> T averageSum(List<T> list, BiFunction<T, T, T> sumFunction, BiFunction<T, Double, T> multipleFunction) {
            if (list == null || list.size() < 2) {
                throw new IllegalArgumentException("list长度不能小于2");
            }

            List<T> avgs = new ArrayList<>();

            for (int i = 0; i < list.size() - 1; i++) {
                T t0 = list.get(i);
                T t1 = list.get(i + 1);


                T sum = sumFunction.apply(t0, t1);

                T avg = multipleFunction.apply(sum, 0.5);

                avgs.add(avg);
            }

            return sum(avgs, sumFunction);

        }


        public static <T extends Comparable<? super T>> T max(List<T> numberList) {
            if (numberList == null || numberList.size() == 0) {
                throw new IllegalArgumentException("numberList不能为空");
            }

            if (numberList.size() == 1) {
                return numberList.get(0);
            }

            numberList.sort(Comparator.naturalOrder());

            return numberList.get(numberList.size() - 1);
        }

        public static <T> T[] combine(T[] arr1, T[] arr2) {
            List<T> arr11 = List.of(arr1);
            List<T> arr21 = List.of(arr2);

            List<T> arr = new ArrayList<>(arr11);
            arr.addAll(arr21);

            return arr.toArray(arr1);
        }

        public static <E> List<E> listListToList(List<List<E>> listList) {
            List<E> list = new ArrayList<>();
            for (List<E> es : listList) {
                list.addAll(es);
            }

            return list;
        }

        /**
         * 输入 List<List<E>> 如
         * 1 2 3 4
         * 5 6 7 8
         * 9 0 1 2
         * 返回 List<List<E>>，如下
         * 1 5 9
         * 2 6 0
         * 3 7 1
         * 4 8 2
         *
         * @param listList List<List<E>>
         * @param <E>      element
         * @return List<List < E>>
         */
        public static <E> List<List<E>> listList2ListList(List<List<E>> listList) {
            // 要求 数据长度都相同
            int length = listList.get(0).size(); // 4
            for (List<E> es : listList) {
                if (es.size() != length)
                    throw new RuntimeException("要求 数据长度都相同");
            }

            int size = listList.size(); // 3

            List<List<E>> ret = new ArrayList<>(length);//4
            for (int i = 0; i < length; i++) {
                ret.add(new ArrayList<>(size));
            }

            for (int i = 0; i < size; i++) {// 3
                for (int j = 0; j < length; j++) {//4
                    ret.get(j).add(listList.get(i).get(j));
                }
            }

            return ret;
        }

        public static void store(Collection<? extends Serializable> collection, String name) throws IOException {

            File file = new File("./data/" + name + ".obj");
            if (!file.createNewFile()) {
                Logger.getLogger().error("文件{}创建失败", file);
                return;
            }


            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(file));

            objectOutputStream.writeObject(collection);

            objectOutputStream.flush();

            objectOutputStream.close();

            Logger.getLogger().info("文件{}写入成功", file);
        }

        public static Object load(String name) throws IOException, ClassNotFoundException {
            File file = new File("./data/" + name + ".obj");

            if (!file.exists()) {
                Logger.getLogger().error("文件{}不存在", file);
                return null;
            }

            ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(file));

            Object o = objectInputStream.readObject();

            return o;
        }

        public static <T> List<T> loadList(String name, T element) throws IOException, ClassNotFoundException {
            Object o = load(name);

            return (List<T>) o;
        }

        /**
         * 创建list
         * 解决 List.of 不可变问题
         *
         * @param t   第一个值
         * @param <T> 泛型
         * @return 可变list
         */
        public static <T> List<T> createListWithFirstElementIs(T t) {
            List<T> list = new ArrayList<>();
            list.add(t);

            return list;
        }

        @SafeVarargs
        public static <E> List<E> append(List<E> list, E... es) {
            ArrayList<E> ret = new ArrayList<>(list);
            ret.addAll(Arrays.asList(es));

            return ret;
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

        public void reset() {
            currentItem = 0;
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

        public Obj[] getObjs() {
            return objs;
        }

        public int getCurrentItem() {
            return currentItem;
        }

        public void setCurrentItem(int currentItem) {
            this.currentItem = currentItem;
        }
    }

    public static class Content {
        //容器，装两个任意对象，有泛型
        public static class BiContent<T1, T2> implements Serializable {
            private T1 t1;
            private T2 t2;

            public static <T1, T2> BiContent<T1, T2> create(T1 t1, T2 t2) {
                return new BiContent<>(t1, t2);
            }

            private BiContent(T1 t1, T2 t2) {
                this.t1 = t1;
                this.t2 = t2;
            }

            public static <T1, T2> BiContent<List<T1>, List<T2>> collect(Stream<BiContent<T1, T2>> biContentStream) {
                List<T1> t1List = new ArrayList<>();
                List<T2> t2List = new ArrayList<>();

                biContentStream.forEach(bi -> {
                    t1List.add(bi.t1);
                    t2List.add(bi.t2);
                });

                return create(t1List, t2List);
            }

            public static <T1, T2> List<BiContent<T1, T2>> createList(List<T1> t1List, List<T2> t2List) {
                int s1 = t1List.size();
                int s2 = t2List.size();

                int s = Math.min(s1, s2);

                List<BiContent<T1, T2>> ans = new ArrayList<>(s1);

                for (int i = 0; i < s; i++) {
                    T1 t1 = t1List.get(i);
                    T2 t2 = t2List.get(i);

                    ans.add(create(t1, t2));
                }

                return ans;
            }

            // 2020年9月17日
            public static <T1, T2, R1, R2> BiContent<T1, T2> map(BiContent<R1, R2> origin,
                                                                 Function<R1, T1> map0, Function<R2, T2> map1) {
                return BiContent.create(
                        map0.apply(origin.getT1()),
                        map1.apply(origin.getT2())
                );
            }

            public static <T1, T2, R1, R2> List<BiContent<T1, T2>> map(List<BiContent<R1, R2>> origin,
                                                                       Function<R1, T1> map0, Function<R2, T2> map1) {
                return origin.stream().map(bi -> map(bi, map0, map1)).collect(Collectors.toList());
            }

            public T1 getT1() {
                return t1;
            }

            public T2 getT2() {
                return t2;
            }

            public void setT1(T1 t1) {
                this.t1 = t1;
            }

            public void setT2(T2 t2) {
                this.t2 = t2;
            }

            public void setT2If(Predicate<T2> p, T2 newVal) {
                if (p.test(this.t2)) {
                    this.t2 = newVal;
                }
            }

            @Override
            public String toString() {
                return toStringWithInfo(t1.getClass().getName(), t2.getClass().getName());
            }

            public String toStringWithInfo(String infoT1, String infoT2) {
                return "[" + infoT1 + " = " + t1.toString() + ", " + infoT2 + " = " + t2.toString() + "]";
            }

            public static Point2 doubleDoubleBiContentToPoint2(BiContent<Double, Double> doubleDoubleBiContent) {
                return Point2.create(
                        doubleDoubleBiContent.getT1(),
                        doubleDoubleBiContent.getT2()
                );
            }
        }

        public static class TriContent<T1, T2, T3> {
            private T1 t1;
            private T2 t2;
            private T3 t3;

            private TriContent(T1 t1, T2 t2, T3 t3) {
                this.t1 = t1;
                this.t2 = t2;
                this.t3 = t3;
            }

            public static <T1, T2, T3> TriContent<T1, T2, T3> create(T1 t1, T2 t2, T3 t3) {
                return new TriContent<>(t1, t2, t3);
            }

            public T1 getT1() {
                return t1;
            }

            public void setT1(T1 t1) {
                this.t1 = t1;
            }

            public T2 getT2() {
                return t2;
            }

            public void setT2(T2 t2) {
                this.t2 = t2;
            }

            public T3 getT3() {
                return t3;
            }

            public void setT3(T3 t3) {
                this.t3 = t3;
            }

            @Override
            public String toString() {
                return "TriContent{" +
                        "t1=" + t1 +
                        ", t2=" + t2 +
                        ", t3=" + t3 +
                        '}';
            }
        }
    }

    public static class Timer {
        // 静态方法 每两次调用，打印中间时间
        private static long firstCallTime = -1;

        public static void printPeriodPerSecondCall(org.slf4j.Logger logger) {
            long l = System.currentTimeMillis();
            if (firstCallTime == -1)
                firstCallTime = l;
            else {
                long period = l - firstCallTime;
                firstCallTime = -1;
                if (logger == null) {
                    System.out.println("运行时间： " + period + "ms");
                } else {
                    logger.info("运行时间： {}ms", period);
                }
            }
        }

        // 实例方法，打印创建该实例后经过的时间
        public long initialTime;

        public Timer() {
            initialTime = System.currentTimeMillis();
        }

        public void printPeriodAfterInitial(org.slf4j.Logger logger) {
            long period = System.currentTimeMillis() - initialTime;
            if (logger == null) {
                System.out.println("计时后运行时间： " + period + "ms");
            } else {
                logger.info("计时后运行时间： {}ms", period);
            }
        }

        public void printPeriodAfterInitial(org.slf4j.Logger logger, String msg) {
            long period = System.currentTimeMillis() - initialTime;
            if (logger == null) {
                System.out.println("[" + msg + "]计时后运行时间： " + period + "ms");
            } else {
                logger.info("[{}]计时后运行时间： {}ms", msg, period);
            }
        }
    }

    public static class Async {
        private ExecutorService executorService;

        public Async() {
            int processors = Runtime.getRuntime().availableProcessors();
            Logger.getLogger().info("启动fixed线程池，线程数 = " + processors);

            executorService =
                    Executors.newFixedThreadPool(processors);
        }

        public void execute(Runnable r) {
            executorService.submit(r);
        }

        public <T> Future<T> submit(Callable<T> callable) {
            return executorService.submit(callable);
        }

        public void waitForAllFinish(long timeout, TimeUnit unit) {
            try {
                executorService.shutdown();
                executorService.awaitTermination(timeout, unit);
            } catch (InterruptedException e) {
                e.printStackTrace();
                System.exit(-1);
            }
        }
    }

    public static class Geometry {
        private static final double ESP_0 = 1e-8;

        // 几何相关工具

        /**
         * Ax^2+Bxy+Cy^2=D 椭返回圆圆周上均匀分布number个点
         *
         * @param A      椭圆方程参数
         * @param B      椭圆方程参数
         * @param C      椭圆方程参数
         * @param D      椭圆方程参数
         * @param number 点数
         * @return 点
         */
        public static List<Point2> ellipsePoints(double A, double B, double C, double D, int number) {
            List<Point2> list = new ArrayList<>(number);
            final double circumference = ellipseCircumference(A, B, C, D);
            for (int i = 0; i < number; i++) {
                list.add(ellipseWalkPoint(A, B, C, D, circumference / number * i));
            }

            return list;
        }

        /**
         * 暴力法计算椭圆Ax^2+Bxy+Cy^2=D周长
         *
         * @param A 椭圆方程参数
         * @param B 椭圆方程参数
         * @param C 椭圆方程参数
         * @param D 椭圆方程参数
         * @return 周长
         */
        private static double ellipseCircumference(double A, double B, double C, double D) {
            int num = 3600 * 4;
            double c = 0.0;
            for (int i = 0; i < num; i++) {
                c += Vector2.
                        from(ellipsePointTheta(A, B, C, D, 2.0 * Math.PI / num * i))
                        .to(ellipsePointTheta(A, B, C, D, 2.0 * Math.PI / num * (i + 1)))
                        .length();
            }

            return c;
        }

        /**
         * 原点出发，方向th弧度的射线和椭圆Ax^2+Bxy+Cy^2=D的交点
         * 吃老本 2019年8月10日
         *
         * @param A     椭圆方程参数
         * @param B     椭圆方程参数
         * @param C     椭圆方程参数
         * @param D     椭圆方程参数
         * @param theta 方向th弧度
         * @return 交点
         */
        private static Point2 ellipsePointTheta(double A, double B, double C, double D, double theta) {
            double pi = Math.PI;
            Point2 d = Point2.origin();


            while (theta < 0)
                theta += 2 * pi;
            while (theta > 2 * pi)
                theta -= 2 * pi;//将弧度th限定在0~2pi

            if (Math.abs(theta) < ESP_0 || Math.abs(theta - 2 * pi) < ESP_0) {
                d.x = Math.sqrt(D / A);
                d.y = 0;
            }
            if (Math.abs(theta - pi) < ESP_0) {
                d.x = -Math.sqrt(D / A);
                d.y = 0;
            }
            //临界问题
            double t = 0.0;
            if (theta > 0 && theta < pi) {
                t = 1 / Math.tan(theta);
                d.y = Math.sqrt(D / (A * t * t + B * t + C));
                d.x = t * d.y;
                //printf("\ntest\n");
                //printf_divct(d);
            }
            if (theta > pi && theta < 2 * pi) {
                theta -= pi;
                t = 1 / Math.tan(theta);
                d.y = -Math.sqrt(D / (A * t * t + B * t + C));
                d.x = t * d.y;
            }
            //射线——负号问题，象限问题

            return d;
        }

        /**
         * 在椭圆Ax^2+Bxy+Cy^2=D 上行走length
         * 返回此时的点
         * 规定起点：椭圆与X轴正方向的交点
         * 规定行走方向：逆时针
         *
         * @param A      椭圆方程参数
         * @param B      椭圆方程参数
         * @param C      椭圆方程参数
         * @param D      椭圆方程参数
         * @param length 行走长度
         * @return 终点
         */
        private static Point2 ellipseWalkPoint(double A, double B, double C, double D, double length) {
            double stepTheta = Converter.angleToRadian(0.05);
            double theta = 0.0;
            while (length > 0.0) {
                length -= Vector2
                        .from(ellipsePointTheta(A, B, C, D, theta))
                        .to(ellipsePointTheta(A, B, C, D, theta + stepTheta))
                        .length();

                theta += stepTheta;
            }

            return ellipsePointTheta(A, B, C, D, theta);
        }

        /**
         * 单位圆（极坐标）
         * 返回：极坐标(r=1,phi=phi)的点的直角坐标(x,y)
         * 核心方法
         *
         * @param phi 极坐标phi
         * @return 单位圆上的一点
         */
        public static Point2 unitCircle(double phi) {
            double x = Math.cos(phi);
            double y = Math.sin(phi);

            return Point2.create(x, y);
        }
    }

    public static class Constant {
        public static final double LIGHT_SPEED = 299792458.0;

        public static final double M = 1.;

        public static final double MM = 1e-3 * M;

        public static final double CM = 1e-2 * M;


        public static final double PRESENT = 1e-2;

        public static final double A = 1.;

        public static final double kA = 1e3 * A;

        public static final double RAD = 1.;

        public static final double MRAD = MM * RAD;
    }

    public static class Doubles {

        /**
         * 保留 num 位小数
         * 存在bug
         *
         * @param target 浮点数
         * @param num    保留小数位数
         * @return 浮点数的字符串
         */
        public static String reservedDecimal(double target, int num) {
            if (num < 0) {
                throw new IllegalArgumentException("num必须大于0，但是 num = " + num);
            }
            if (num == 0) {
                return ((long) target) + "";
            }

            String str = String.format("%.16f", target);
            int indexOf = str.indexOf(".");

            int length = indexOf + num + 1;


            if (str.length() < length) {
                return str + "0".repeat(length - str.length());
            } else {
                return str.substring(0, length);
            }

        }

        public static double max(Collection<Double> doubles) {
            return doubles.stream().mapToDouble(Double::doubleValue).max().orElseThrow();
        }

        public static double min(Collection<Double> doubles) {
            return doubles.stream().mapToDouble(Double::doubleValue).min().orElseThrow();
        }
    }

    public static class Statistics {

        public static double max(List<Double> doubleList) {
            Objects.requireNonNull(doubleList);
            Equal.requireTrue(doubleList.size() > 0, "doubleList 长度需大于0");

            return doubleList.stream().mapToDouble(Double::doubleValue).max().orElseThrow(RuntimeException::new);
        }


        public static double min(List<Double> doubleList) {
            Objects.requireNonNull(doubleList);
            Equal.requireTrue(doubleList.size() > 0, "doubleList 长度需大于0");

            return doubleList.stream().mapToDouble(Double::doubleValue).min().orElseThrow(RuntimeException::new);
        }

        public static double average(List<Double> doubleList) {
            Objects.requireNonNull(doubleList);
            Equal.requireTrue(doubleList.size() > 0, "doubleList 长度需大于0");

            return doubleList.stream().mapToDouble(Double::doubleValue).average().orElseThrow(RuntimeException::new);
        }

        /**
         * 求 abs((max - min) / average)
         *
         * @param doubleList doubles
         * @return abs(( max - min) / average)
         */
        public static double undulate(List<Double> doubleList) {
            Objects.requireNonNull(doubleList);
            Equal.requireTrue(doubleList.size() > 0, "doubleList 长度需大于0");

            if (doubleList.size() == 1)
                return 0;
            else {
                DoubleSummaryStatistics doubleSummaryStatistics =
                        doubleList.stream().mapToDouble(Double::doubleValue).summaryStatistics();
                double max = doubleSummaryStatistics.getMax();
                double min = doubleSummaryStatistics.getMin();

                double average = doubleSummaryStatistics.getAverage();

                if (
                        (
                                Equal.isEqual(max, 0, 1e-5) &&
                                        Equal.isEqual(min, 0, 1e-5)
                        )
                                || Equal.isEqual(average, 0, 1e-5)
                )
                    return 0.0;

                return Math.abs((max - min) / average);
            }
        }
    }

    public static class JavaCoder {
        public static String create(String name, String[] data) {
            StringBuilder sb = new StringBuilder();
            sb.append("String[] ").append(name).append(" = new String[]{");

            for (int i = 0; i < data.length - 1; i++) {
                sb.append('"').append(data[i]).append('"').append(',');
                if (i % 20 == 0) {
                    sb.append('\n');
                }
            }

            sb.append('"').append(data[data.length - 1]).append('"');


            sb.append("};");
            return sb.toString();
        }

        public static String create(double[] arr) {
            return "new double[]{" + Arrays.stream(arr).mapToObj(String::valueOf).collect(Collectors.joining(",")) + "}";
        }

        public static String create(double[][] arr) {
            return "new double[][]{" + Arrays.stream(arr).map(JavaCoder::create).collect(Collectors.joining(",\n")) + "}";
        }

        public static String create(String name, double[][][] arr) {
            StringBuilder sb = new StringBuilder();
            int length = arr.length;
            sb.append("double[][][] ").append(name).append(" = new double[").append(length).append("][][];");
            for (int i = 0; i < arr.length; i++) {
                sb.append(name).append("[").append(i).append("]=").append(create(arr[i])).append(";");
            }

            return sb.toString();


            //return "new double[][][]{" + Arrays.stream(arr).map(JavaCoder::create).collect(Collectors.joining(",\n")) + "}";
        }
    }
}
