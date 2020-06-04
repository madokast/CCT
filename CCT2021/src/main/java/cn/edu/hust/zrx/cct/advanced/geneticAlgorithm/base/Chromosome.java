package cn.edu.hust.zrx.cct.advanced.geneticAlgorithm.base;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.ToDoubleFunction;

/**
 * Description
 * Chromosome
 * 染色体
 * <p>
 * Data
 * 20:51
 *
 * @author zrx
 * @version 1.0
 */

public class Chromosome implements Comparable<Chromosome> {

    private static final Random RANDOM = new Random();

    private final List<Gene> genes;
    private final List<Integer> locations;
    private final List<Integer> lengths;
    private final BitList bitList;
    private final int size;
    private final FitnessFunction fitnessFunction;

    public Chromosome(List<Gene> genes, FitnessFunction fitnessFunction) {
        this.genes = genes;
        this.locations = new ArrayList<>(genes.size());
        this.lengths = new ArrayList<>(genes.size());

        int location = 0;

        for (Gene gene : genes) {

            locations.add(location);
            lengths.add(gene.getSize());

            location += gene.getSize();
        }
        this.bitList = new ByteBitList(location);
        this.size = bitList.size();
        this.fitnessFunction = fitnessFunction;
    }

    public static Chromosome create(FitnessFunction fitnessFunction, Gene... genes) {
        return new Chromosome(List.of(genes), fitnessFunction);
    }

    public int size(){
        return genes.size();
    }

    public double valueOf(int index) {
        Gene gene = genes.get(index);
        Integer location = locations.get(index);
        Integer length = lengths.get(index);

        int valueAlong = bitList.valueAlong(location, length);

        return gene.value(valueAlong);
    }

    public static void crossover(Chromosome c1, Chromosome c2) {
        BitList.crossover(c1.bitList, c2.bitList, RANDOM.nextInt(c1.bitList.size()));
    }

    public void mutate() {
        bitList.reverse(RANDOM.nextInt(size));
    }

    private Double fitness = null; // cache

    public double fitness() {
        if (fitness == null)
            fitness = fitnessFunction.fit(this);

        return fitness;
    }

    @Override
    public int compareTo(@NotNull Chromosome o) {
        return Double.compare(this.fitness(), o.fitness());
    }

    public Chromosome(Chromosome c) {
        this.genes = c.genes;
        this.locations = c.locations;
        this.lengths = c.lengths;
        this.bitList = c.bitList.copy();
        this.size = c.size;
        this.fitnessFunction = c.fitnessFunction;
    }

    public Chromosome copy() {
        return new Chromosome(this);
    }

    public Chromosome randomSelf() {
        for (int i = 0; i < size; i++) {
            this.bitList.set(i, RANDOM.nextInt(2));
        }

        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("染色体{");

        int s = genes.size();

        for (int i = 0; i < s; i++) {
            Gene gene = genes.get(i);
            Integer location = locations.get(i);
            Integer length = lengths.get(i);

            int valueAlong = bitList.valueAlong(location, length);

            double value = gene.value(valueAlong);

            sb.append(gene.getName()).append("=").append(value).append(" ,");
        }

        sb.deleteCharAt(sb.length() - 1);

        sb.append("} 适应度=").append(fitness());

        return sb.toString();
    }
}
