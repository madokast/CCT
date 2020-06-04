package cn.edu.hust.zrx.cct.advanced.geneticAlgorithm.base;

import cn.edu.hust.zrx.cct.base.point.Point2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Description
 * Population
 * <p>
 * Data
 * 21:09
 *
 * @author zrx
 * @version 1.0
 */

public class Population {
    private static final Random RANDOM = new Random();

    private final List<Chromosome> chromosomes;

    private Population(int size) {
        this.chromosomes = new ArrayList<>(size);
    }

    public static Population initial(int size, Chromosome origin) {
        Population population = new Population(size);

        for (int i = 0; i < size; i++) {
            population.add(origin.copy().randomSelf());
        }

        return population;
    }

    public List<Chromosome> getChromosomes() {
        return chromosomes;
    }

    public void add(Chromosome c) {
        this.chromosomes.add(c);
    }

    public Population nextPopulation(double keepRate, double crossRate, double mutateRate) {
        int size = chromosomes.size();

        Population next = new Population(size);

        Collections.sort(chromosomes);
        Collections.reverse(chromosomes);

        chromosomes.stream().limit((int) (keepRate * size)).forEach(next::add);

        while (next.chromosomes.size() < size) {
            Chromosome p = chromosomes.get(RANDOM.nextInt(size)).copy();
            Chromosome m = chromosomes.get(RANDOM.nextInt(size)).copy();

            if (Math.random() <= crossRate) {
                Chromosome.crossover(p, m);
            }

            if (Math.random() <= mutateRate) {
                p.mutate();
                m.mutate();
            }

            next.add(p);
            next.add(m);
        }

        return next;
    }

    public List<Point2> project(int i, int j) {
        return chromosomes.stream()
                .map(c -> {
                    double v = c.valueOf(i);
                    double v1 = c.valueOf(j);

                    return Point2.create(v, v1);
                })
                .collect(Collectors.toList());
    }

    public Chromosome bestOne() {
        Collections.sort(chromosomes);


        return chromosomes.get(chromosomes.size() - 1);
    }

    public double averageFitness(){
        return chromosomes.stream().mapToDouble(Chromosome::fitness).average().orElse(0);
    }

    public int size() {
        return chromosomes.size();
    }
}
