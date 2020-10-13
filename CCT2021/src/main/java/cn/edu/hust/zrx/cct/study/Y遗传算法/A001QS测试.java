package cn.edu.hust.zrx.cct.study.Y遗传算法;

import cn.edu.hust.zrx.cct.Logger;
import cn.edu.hust.zrx.cct.advanced.geneticAlgorithm.base.Chromosome;
import cn.edu.hust.zrx.cct.advanced.geneticAlgorithm.base.FitnessFunction;
import cn.edu.hust.zrx.cct.advanced.geneticAlgorithm.base.Gene;
import cn.edu.hust.zrx.cct.advanced.geneticAlgorithm.base.Population;
import cn.edu.hust.zrx.cct.base.BaseUtils;
import cn.edu.hust.zrx.cct.base.point.Point2;
import cn.edu.hust.zrx.cct.base.python.Plot2d;
import cn.hutool.core.io.file.FileWriter;

import java.io.*;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.lang.Math.sin;

/**
 * Description
 * A001QS测试
 * <p>
 * Data
 * 22:12
 *
 * @author zrx
 * @version 1.0
 */

public class A001QS测试 {

    @run
    public void 测试() {
        // 求 f(x, y) 21.5 + x * sin(4 * Math.PI * x) + y * sin(20 * Math.PI * y)
        // 在  -5<x<15, 4<y<6 上的最大值

        // 定义基因，每个基因代表一个变量
        // 4 个参数，变量名，上界，下届，分辨率
        Gene gene_x = Gene.create("x", 15, -5, 0.001);
        Gene gene_y = Gene.create("y", 6, 4, 0.001);

        // 定义目标函数
        FitnessFunction fitnessFunction = chromosome -> {
            double x = chromosome.valueOf(0);
            double y = chromosome.valueOf(1);

            return 21.5 + x * sin(4 * Math.PI * x) + y * sin(20 * Math.PI * y);
        };

        // 构建染色体
        Chromosome chromosome = Chromosome.create(fitnessFunction, gene_x, gene_y);

        // 初始化种群
        Population initial = Population.initial(2000, chromosome);

        List<Point2> bestList = new ArrayList<>();
        List<Point2> avgList = new ArrayList<>();

        // 迭代
        for (int i = 0; i < 50; i++) {

            Chromosome bestOne = initial.bestOne();
            double fitness = bestOne.fitness();

            double averageFitness = initial.averageFitness();

            avgList.add(Point2.create(i, averageFitness));

            bestList.add(Point2.create(i, fitness));

            initial = initial.nextPopulation(0.2, 1, 1);
        }

        Plot2d.plot2(bestList);
        Plot2d.plot2(avgList);

        Plot2d.showThread();

    }

    @run(1)
    public void cosy调用() throws IOException {
        Runtime runtime = Runtime.getRuntime();
        Logger.getLogger().info("runtime = " + runtime);

        Process exec = runtime.exec("cosy.bat");

        InputStream inputStream = exec.getInputStream();

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

        String s;
        while ((s = bufferedReader.readLine()) != null)
            Logger.getLogger().info("s = " + s);
    }

    @run(2)
    public void writeTest() throws IOException {
        double[] doubles = {
                1, 1.17939375,
                0.249984258,
                0.150038117,
                0.120406873,
                0.129701836,
                6.99923741,
                5.50076259,
                15.0,
                0.749996288,
                -0.481454424,
                -0.0797442511,
                0.0564069116,
                2.299980589264395,
                0.24918341,
                0.199955992,
                3.12455553,
                20.8669494,
                19.50849507,
                -5.45165598,
                -0.143741562,
                -0.227124004,
        };

        List<double[]> in = List.of(doubles);

        writeInputFile(new File("./cosy/input.txt"), in);

        cosy调用();
    }

    private void writeInputFile(File file, List<double[]> in) throws IOException {
        FileWriter fileWriter = FileWriter.create(file);

        BufferedWriter writer = fileWriter.getWriter(false);

        StringBuilder sb = new StringBuilder();
        sb.append('\n');
        sb.append(in.size()).append('\n');

        for (double[] doubles : in) {
            for (double aDouble : doubles) {
                sb.append(aDouble).append('\n');
            }
        }

        writer.write(sb.toString());

        writer.flush();

        writer.close();


        /**
         *
         275
         1
         1.17939375
         0.249984258
         0.150038117
         0.120406873
         0.129701836
         6.99923741
         5.50076259
         15.0
         0.749996288
         -0.481454424
         -0.0797442511
         0.0564069116
         2.299980589264395
         0.24918341
         0.199955992
         3.12455553
         20.8669494
         19.50849507
         -5.45165598
         -0.143741562
         -0.227124004
         */
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
