package cn.edu.hust.zrx.cct.advanced.geneticAlgorithm.base;

/**
 * Description
 * FitnessFunction
 * 适应度函数
 * <p>
 * Data
 * 21:37
 *
 * @author zrx
 * @version 1.0
 */

@FunctionalInterface
public interface FitnessFunction {
    double fit(Chromosome c);
}
