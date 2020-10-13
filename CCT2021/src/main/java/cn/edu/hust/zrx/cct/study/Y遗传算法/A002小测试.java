package cn.edu.hust.zrx.cct.study.Y遗传算法;

import cn.edu.hust.zrx.cct.Logger;
import cn.edu.hust.zrx.cct.advanced.geneticAlgorithm.base.Chromosome;
import cn.edu.hust.zrx.cct.advanced.geneticAlgorithm.base.FitnessFunction;
import cn.edu.hust.zrx.cct.advanced.geneticAlgorithm.base.Gene;
import cn.edu.hust.zrx.cct.advanced.geneticAlgorithm.base.Population;
import cn.edu.hust.zrx.cct.base.BaseUtils;
import cn.edu.hust.zrx.cct.base.annotation.Run;
import cn.edu.hust.zrx.cct.base.point.Point2;
import cn.edu.hust.zrx.cct.base.python.Plot2d;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.lang.Math.log;

/**
 * Description
 * A002小测试
 * <p>
 * Data
 * 20:27
 *
 * @author zrx
 * @version 1.0
 */

public class A002小测试 {
    @Run
    public void 测试() {
        // 定义基因，每个基因代表一个变量
        // 4 个参数，变量名，上界，下届，分辨率
        Gene gene_x1 = Gene.create("x1", 6, 0, 0.00001);
        Gene gene_x2 = Gene.create("x2", 6, 0, 0.00001);

        // 定义目标函数
        FitnessFunction fitnessFunction = chromosome -> {
            double x1 = chromosome.valueOf(0);
            double x2 = chromosome.valueOf(1);

            if (26 - Math.pow(x1 - 5, 2) - x2 * x2 < 0) return 0;

            if (20 - 4 * x1 - x2 < 0) return 0;

            return log(1 / Math.abs(
                    Math.pow(x1 * x1 + x2 - 11, 2) +
                            Math.pow(x1 + x2 * x2 - 7, 2)
            ));
        };

        // 构建染色体
        Chromosome chromosome = Chromosome.create(fitnessFunction, gene_x1, gene_x2);

        // 初始化种群
        Population initial = Population.initial(10, chromosome);

        List<Point2> bestList = new ArrayList<>();
        List<Point2> avgList = new ArrayList<>();

        // 迭代
        for (int i = 0; i < 10000; i++) {

            Chromosome bestOne = initial.bestOne();

            double fitness = bestOne.fitness();

            double averageFitness = initial.averageFitness();

            avgList.add(Point2.create(i, averageFitness));

            bestList.add(Point2.create(i, fitness));

            initial = initial.nextPopulation(0.2, 0.9, 0.8);
        }

        Chromosome ans = initial.bestOne();
        double x1 = ans.valueOf(0);
        double x2 = ans.valueOf(1);

        Logger.getLogger().info("x1 = " + x1);
        Logger.getLogger().info("x2 = " + x2);

        Plot2d.plot2(bestList);
        Plot2d.plot2(avgList);

        Plot2d.showThread();
    }


    public static void main(String[] args) throws Exception {
        BaseUtils.Timer.printPeriodPerSecondCall(Logger.getLogger());
        StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[1];
        Logger.getLogger().info("{}", stackTraceElement.getClassName());
        Class<?> currentClass = Class.forName(stackTraceElement.getClassName());

        Constructor<?> constructor = currentClass.getConstructor();
        Object newInstance = constructor.newInstance();

        Class<Run> runAnnotation = Run.class;

        Method[] methods = currentClass.getMethods();
        Arrays.stream(methods)
                .filter(method -> method.isAnnotationPresent(runAnnotation))
                .filter(method -> method.getAnnotation(runAnnotation).validate())
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
}
