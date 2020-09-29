package cn.edu.hust.zrx.cct.study.遗传算法;

import cn.edu.hust.zrx.cct.Logger;
import io.jenetics.BitChromosome;
import io.jenetics.BitGene;
import io.jenetics.DoubleChromosome;
import io.jenetics.Genotype;
import io.jenetics.engine.Engine;
import io.jenetics.engine.EvolutionResult;
import io.jenetics.engine.Limits;

import java.util.stream.Collectors;

/**
 * Description
 * 学习 jenetics
 * <p>
 * Data
 * 11:01
 *
 * @author zrx
 * @version 1.0
 */

public class B001JeneticsHelloWorld {

    // fitness function
    static int fit(final Genotype<BitGene> gt) {
        // BitGene 就是一个基因，装一个 bit，值只有 0 or 1

        // Gene<A, G extends Gene<A, G>> 是一个接口，表示一个基因，A 是基因表示的值，即解码后的值。G 是 Gene 的实现类

        // bit count 返回 BitChromosome 中 bit 位==1 的数目
        return gt.chromosome().as(BitChromosome.class).bitCount();
    }

    public static void main(String[] args) {

        Genotype<BitGene> gtf = Genotype.of(BitChromosome.of(10, 0.1));

        Engine<BitGene, Integer> engine = Engine.builder(B001JeneticsHelloWorld::fit, gtf)
                .build();

        Genotype<BitGene> result = engine.stream()
                .limit(Limits.bySteadyFitness(2))
                .limit(100)
                .peek(evolutionResult -> {
                    Integer bestFitness = evolutionResult.bestFitness();
                    System.out.println("bestFitness = " + bestFitness);
                })
                .collect(EvolutionResult.toBestGenotype());

        Logger.getLogger().info("result = " + result);

    }
}
