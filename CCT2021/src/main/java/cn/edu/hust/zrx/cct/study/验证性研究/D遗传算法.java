package cn.edu.hust.zrx.cct.study.验证性研究;

import cn.edu.hust.zrx.cct.Logger;
import cn.edu.hust.zrx.cct.advanced.geneticAlgorithm.base.BitList;
import cn.edu.hust.zrx.cct.advanced.geneticAlgorithm.base.ByteBitList;
import cn.edu.hust.zrx.cct.advanced.geneticAlgorithm.base.FitnessFunction;
import cn.edu.hust.zrx.cct.advanced.geneticAlgorithm.base.Gene;
import cn.edu.hust.zrx.cct.base.BaseUtils;
import cn.edu.hust.zrx.cct.base.point.Point2;
import cn.edu.hust.zrx.cct.base.python.Plot2d;
import org.apache.commons.math3.genetics.*;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.BitSet;
import java.util.List;

/**
 * Description
 * D遗传算法
 * 失败
 * <p>
 * Data
 * 16:37
 *
 * @author zrx
 * @version 1.0
 */
@SuppressWarnings("all")
public class D遗传算法 {

    @run(1)
    public void ByteBitListTest1() {
        BitList list = new ByteBitList("000111000111010101");

        Logger.getLogger().info("list = " + list);

        // 0001 1100 0111 0101 01
        // 0001 1100 0111 0100 01
    }

    @run(2)
    public void sizeTest() {
        for (int i = 1; i < 10; i++) {
            BitList bitList = new ByteBitList(i);
            Logger.getLogger().info("bitList.size() = " + bitList.size());

            Logger.getLogger().info("bitList = " + bitList);
        }
    }

    @run(3)
    public void getTest() {
        BitList bitList = new ByteBitList("0100111101");

        Logger.getLogger().info("bitList = " + bitList);

        for (int i = 0; i < bitList.size(); i++) {
            Logger.getLogger().info("bitList.get(" + i + ") = " + bitList.get(i));
        }
    }

    @run(6)
    public void setTest() {
        BitList bitList = new ByteBitList(6);

        Logger.getLogger().info("bitList = " + bitList);

        bitList.set(0, 1);
        bitList.set(1, 1);
        bitList.set(2, 1);

        Logger.getLogger().info("bitList = " + bitList);

        bitList.set(0, new ByteBitList("111111"));

        Logger.getLogger().info("bitList = " + bitList);

        bitList.set(2, 0);
        bitList.set(3, 0);

        Logger.getLogger().info("bitList = " + bitList);

    }

    @run(5)
    public void setTest1() {
        BitList bitList = new ByteBitList(1);

        Logger.getLogger().info("bitList = " + bitList);

        bitList.set(0, 1);

        int i = bitList.get(0);

        Logger.getLogger().info("i = " + i);

        Logger.getLogger().info("bitList = " + bitList);
    }

    @run(7)
    public void valueTest() {
        BitList bitList = new ByteBitList("110011");

        for (int i = 0; i < 4; i++) {
            BitList sub = bitList.get(i, 2);
            Logger.getLogger().info("sub = " + sub);
            Logger.getLogger().info("sub.valueAlong(0,sub.size()) = " + sub.valueAlong(0, sub.size()));

            int valueAlong = bitList.valueAlong(i, 2);
            Logger.getLogger().info("valueAlong = " + valueAlong);
        }
    }

    @run(8)
    public void reverseTest() {
        BitList bitList = new ByteBitList("110011");

        Logger.getLogger().info("bitList = " + bitList);

        bitList.reverse(3);

        Logger.getLogger().info("bitList = " + bitList);
    }

    @run(9)
    public void crossTest() {
        BitList l1 = new ByteBitList("110011");
        BitList l2 = new ByteBitList("001100");

        Logger.getLogger().info("l1 = " + l1);
        Logger.getLogger().info("l2 = " + l2);

        BitList.crossover(l1, l2, 1);

        Logger.getLogger().info("l1 = " + l1);
        Logger.getLogger().info("l2 = " + l2);
    }

    @run(11)
    public void 就在这一课了() {
        /**
         *  x1*x1 + x2*x2 = 0
         */


        Gene x = Gene.create("x", 2, -2, 16);
        Gene y = Gene.create("y", 3, -3, 16);

        Logger.getLogger().info("x = " + x);
        Logger.getLogger().info("y = " + y);


        cn.edu.hust.zrx.cct.advanced.geneticAlgorithm.base.Chromosome chromosome = cn.edu.hust.zrx.cct.advanced.geneticAlgorithm.base.Chromosome.create(
                (FitnessFunction) c -> {
                    double xx = c.valueOf(0);
                    double yy = c.valueOf(1);

                    return Math.abs(1 / (xx * xx + yy * yy + 1e-10));
                },
                x, y
        );

        Logger.getLogger().info("chromosome = " + chromosome);

        cn.edu.hust.zrx.cct.advanced.geneticAlgorithm.base.Population initial =
                cn.edu.hust.zrx.cct.advanced.geneticAlgorithm.base.Population.initial(20, chromosome);


        for (int i = 0; i < 5000; i++) {

            if (i % 100 == 0) {
                Logger.getLogger().info("initial.size() = " + initial.size());

                List<Point2> project = initial.project(0, 1);
                Plot2d.plot2(project, switcher.getAndSwitch());


                cn.edu.hust.zrx.cct.advanced.geneticAlgorithm.base.Chromosome bestOne = initial.bestOne();
                Logger.getLogger().info("bestOne = " + bestOne);

            }

            initial = initial.nextPopulation(0.2, 1, 1);
        }

        List<Point2> project = initial.project(0, 1);
        Plot2d.plot2(project, switcher.getAndSwitch());

        Plot2d.equal();

        Plot2d.showThread();

    }


    @run(12)
    public void 学习() {
//        Plot2d.plot2(x-> x*Math.sin(4* Math.PI*x),Point2.create(-5,15),1000,Plot2d.BLACK_LINE);
//        Plot2d.plot2(y-> y*Math.sin(20* Math.PI*y),Point2.create(4,6),1000,Plot2d.BLACK_LINE);

//        Plot2d.showThread();


        Gene x = Gene.create("x", 15, -5, 60000);
        Gene y = Gene.create("y", 6, 4, 60000);

        Logger.getLogger().info("x = " + x);
        Logger.getLogger().info("y = " + y);


        cn.edu.hust.zrx.cct.advanced.geneticAlgorithm.base.Chromosome chromosome =
                cn.edu.hust.zrx.cct.advanced.geneticAlgorithm.base.Chromosome.create(
                        (FitnessFunction) fitness -> {
                            double xx = fitness.valueOf(0);
                            double yy = fitness.valueOf(1);
                            return 21.5 + xx * Math.sin(4 * Math.PI * xx) + yy * Math.sin(20 * Math.PI * yy);
                        }, x, y
                );

        Logger.getLogger().info("chromosome = " + chromosome);

        cn.edu.hust.zrx.cct.advanced.geneticAlgorithm.base.Population initial =
                cn.edu.hust.zrx.cct.advanced.geneticAlgorithm.base.Population.initial(2000, chromosome);

        for (int i = 0; i < 100 * 100 * 100; i++) {

            if (i % 100 * 100 * 10 == 0) {
                Logger.getLogger().info("initial.size() = " + initial.size());


                cn.edu.hust.zrx.cct.advanced.geneticAlgorithm.base.Chromosome bestOne = initial.bestOne();
                Logger.getLogger().info("bestOne = " + bestOne);

            }

            initial = initial.nextPopulation(0.01, 1, 1);
        }

        cn.edu.hust.zrx.cct.advanced.geneticAlgorithm.base.Chromosome bestOne = initial.bestOne();

        Logger.getLogger().info("bestOne = " + bestOne);

    }

    BaseUtils.Switcher<String> switcher = BaseUtils.Switcher.creat(
            Plot2d.BLACK_POINT,
            Plot2d.YELLOW_POINT,
            Plot2d.RED_POINT,
            Plot2d.BLUE_POINT,
            Plot2d.GREEN_POINT
    );


    public void 垃圾() {
        GeneticAlgorithm ga = new GeneticAlgorithm(
                new OnePointCrossover<Integer>(),
                1,
                new RandomKeyMutation(),
                0.10,
                new TournamentSelection(5)
        );

        // initial population
        Population initial = getInitialPopulation();

        // stopping condition
        StoppingCondition stopCond = new FixedGenerationCount(100);

        // run the algorithm
        Population finalPopulation = ga.evolve(initial, stopCond);

        // best chromosome from the final population
        Chromosome bestFinal = finalPopulation.getFittestChromosome();

        Logger.getLogger().info("bestFinal = " + bestFinal.toString());
    }

    private static Population getInitialPopulation() {
        //     * @param chromosomes list of chromosomes in the population
        //     * @param populationLimit maximal size of the population
        //     * @param elitismRate how many best chromosomes will be directly transferred to the next generation [in %]


        return new ElitisticListPopulation(
                List.of(
                        new MyRandomKey(List.of(Math.random())),
                        new MyRandomKey(List.of(Math.random())),
                        new MyRandomKey(List.of(Math.random())),
                        new MyRandomKey(List.of(Math.random())),
                        new MyRandomKey(List.of(Math.random())),
                        new MyRandomKey(List.of(Math.random())),
                        new MyRandomKey(List.of(Math.random())),
                        new MyRandomKey(List.of(Math.random())),
                        new MyRandomKey(List.of(Math.random())),
                        new MyRandomKey(List.of(Math.random())),
                        new MyRandomKey(List.of(Math.random())),
                        new MyRandomKey(List.of(Math.random()))
                ), 20, 0.2
        );
    }

    private static class MyRandomKey extends RandomKey<Double> {
        public MyRandomKey(List<Double> representation) throws InvalidRepresentationException {
            super(representation);
        }

        @Override
        public AbstractListChromosome<Double> newFixedLengthChromosome(List<Double> chromosomeRepresentation) {
            return new MyRandomKey(chromosomeRepresentation);
        }

        @Override
        public double fitness() {
            List<Double> representation = getRepresentation();

            Double x1 = representation.get(0);

            return x1 * x1;
        }

        @Override
        public String toString() {
            return getRepresentation().toString();
        }
    }


    public static void main(String[] args) throws Exception {
        BaseUtils.Timer.printPeriodPerSecondCall(Logger.getLogger());
        StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[1];
        Logger.getLogger().info("{}", stackTraceElement.getClassName());
        Class<?> currentClass = Class.forName(stackTraceElement.getClassName());

        Constructor<?> constructor = currentClass.getConstructor();
        Object newInstance = constructor.newInstance();

        Class<run> runAnnotation = run.class;

        Method[] methods = currentClass.getMethods();
        Arrays.stream(methods)
                .filter(method -> method.isAnnotationPresent(runAnnotation))
                .sorted((m1, m2) -> Integer.compare(
                        m2.getAnnotation(runAnnotation).value(),
                        m1.getAnnotation(runAnnotation).value()
                ))
                .limit(1)
                .forEach(method -> {
                    Logger.getLogger().info("运行{}", method.toString());
                    try {
                        method.invoke(newInstance);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                });

        BaseUtils.Timer.printPeriodPerSecondCall(Logger.getLogger());
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    private @interface run {
        int value() default 0;

        String code() default "";
    }

}
