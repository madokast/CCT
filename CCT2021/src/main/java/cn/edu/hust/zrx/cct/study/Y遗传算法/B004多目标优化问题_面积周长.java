package cn.edu.hust.zrx.cct.study.Y遗传算法;

import cn.edu.hust.zrx.cct.base.point.Point2;
import cn.edu.hust.zrx.cct.base.python.Plot2d;
import io.jenetics.*;
import io.jenetics.engine.Codecs;
import io.jenetics.engine.Engine;
import io.jenetics.engine.Problem;
import io.jenetics.ext.SimulatedBinaryCrossover;
import io.jenetics.ext.moea.MOEA;
import io.jenetics.ext.moea.NSGA2Selector;
import io.jenetics.ext.moea.Vec;
import io.jenetics.util.DoubleRange;
import io.jenetics.util.IntRange;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Description
 * B004多目标优化问题_面积周长
 * <p>
 * Data
 * 10:11
 *
 * @author zrx
 * @version 1.0
 */

public class B004多目标优化问题_面积周长 {

    static double[] fun(double[] v) {
        // 边长
        double a = v[0];
        double b = v[1];

        // 面积和周长
        double area = a * b;
        double length = 2 * (a + b);

        return new double[]{area, length};
    }

    static final Problem<double[], DoubleGene, Vec<double[]>> PROBLEM = Problem.of(
            v -> Vec.of(fun(v)),
            Codecs.ofVector(DoubleRange.of(0, 1.), 2)
    );

    static Engine<DoubleGene, Vec<double[]>> engine = Engine.builder(PROBLEM).populationSize(100)
            .alterers(
                    new SimulatedBinaryCrossover<>(1),
                    new Mutator<>(1.0 / 2)
            ).offspringSelector(new TournamentSelector<>(5))
            .survivorsSelector(NSGA2Selector.ofVec())
            .maximizing().build();

    public static void main(String[] args) {
        List<Point2> point2s = engine.stream()
                .limit(25000)
                .peek(result->{
                    long generation = result.generation();

                    Phenotype<DoubleGene, Vec<double[]>> best = result.bestPhenotype();
                    double[] fitnessData = best.fitness().data();
                    double[] individual = best.genotype().chromosome().as(DoubleChromosome.class).toArray();

                    System.out.println("fitnessData = " + Arrays.toString(fitnessData));

                    System.out.println("individual = " + Arrays.toString(individual));
                })
                .collect(MOEA.toParetoSet(IntRange.of(1000, 1100)))
                .map(Phenotype::fitness)
                .asList()
                .stream()
                .map(Vec::data)
                .map(v -> Point2.create(v[0], v[1]))
                .collect(Collectors.toList());

        Plot2d.plot2(point2s, Plot2d.BLUE_POINT);

        Plot2d.showThread();
    }

}
