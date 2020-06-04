package cn.edu.hust.zrx.cct.advanced.geneticAlgorithm;

import cn.edu.hust.zrx.cct.Logger;
import cn.edu.hust.zrx.cct.advanced.geneticAlgorithm.base.Chromosome;
import cn.edu.hust.zrx.cct.advanced.geneticAlgorithm.base.FitnessFunction;
import cn.edu.hust.zrx.cct.advanced.geneticAlgorithm.base.Gene;
import cn.edu.hust.zrx.cct.advanced.geneticAlgorithm.base.Population;
import cn.edu.hust.zrx.cct.base.BaseUtils;

import java.util.function.ToDoubleFunction;

/**
 * Description
 * GeneticAlgorithm
 * 遗传算法
 * <p>
 * Data
 * 22:10
 *
 * @author zrx
 * @version 1.0
 */

public class GeneticAlgorithm {


    /**
     * 遗传算法
     *
     * @param populationSize  种群大小
     * @param maxGeneration   最大代数
     * @param keepRate        种群中最优个体，有多少直接进入下一代，比例
     * @param crossRate       杂交比例
     * @param mutateRate      变异比例
     * @param fitnessFunction 适应性函数 / 目标函数
     * @param genes           基因 / 变量
     * @return 最优个体
     */
    public static Chromosome solve(
            int populationSize,
            int maxGeneration,
            double keepRate,
            double crossRate,
            double mutateRate,
            ToDoubleFunction<double[]> fitnessFunction,
            Gene... genes
    ) {
        Chromosome origin = Chromosome.create(
                c -> {
                    int length = genes.length;
                    double[] arr = new double[length];

                    for (int i = 0; i < length; i++) {
                        arr[i] = c.valueOf(i);
                    }

                    return fitnessFunction.applyAsDouble(arr);
                },
                genes
        );

        Population initial = Population.initial(populationSize, origin);

        for (int i = 0; i < maxGeneration; i++) {
            initial = initial.nextPopulation(keepRate, crossRate, mutateRate);
            Logger.getLogger().info("代数 = " + (i + 1));

            Chromosome bestOne = initial.bestOne();

            Logger.getLogger().info("最优解 = " + bestOne);
        }

        return initial.bestOne();
    }

}
