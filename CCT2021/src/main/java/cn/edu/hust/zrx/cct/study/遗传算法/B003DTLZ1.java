package cn.edu.hust.zrx.cct.study.遗传算法;

import cn.edu.hust.zrx.cct.base.point.Point3;
import cn.edu.hust.zrx.cct.base.python.Plot3d;
import io.jenetics.DoubleGene;
import io.jenetics.Mutator;
import io.jenetics.Phenotype;
import io.jenetics.TournamentSelector;
import io.jenetics.engine.Codecs;
import io.jenetics.engine.Engine;
import io.jenetics.engine.Problem;
import io.jenetics.ext.SimulatedBinaryCrossover;
import io.jenetics.ext.moea.MOEA;
import io.jenetics.ext.moea.NSGA2Selector;
import io.jenetics.ext.moea.Vec;
import io.jenetics.util.DoubleRange;
import io.jenetics.util.IntRange;

import static java.lang.Math.*;


import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Description
 * B003DTLZ1
 * <p>
 * Data
 * 9:22
 *
 * @author zrx
 * @version 1.0
 */

public class B003DTLZ1 {

    private static final int M = 4; // 目标数目

    private static final int K = 3; // 变量数目

    private static final int n = M + K - 1; //

    private static double[] fun(double[] x) {
        double g = 0.0;
        for (int i = M - K; i < M; i++) {
            g += pow(x[i] - 0.5, 2) - cos(20 * PI * (x[i] - 0.5));
        }

        g = 100.0 * (K + g);

        double[] f = new double[M];
        for (int i = 0; i < M; i++) {
            f[i] = 0.5 * (1.0 + g);
            for (int j = 0; j < M - i - 1; j++) {
                f[i] *= x[j];
            }
            if (i != 0) {
                f[i] *= 1 - x[M - i - 1];
            }
        }

        return f;
    }

    static Problem<double[], DoubleGene, Vec<double[]>> problem = Problem.of(
            v -> Vec.of(fun(v)),
            Codecs.ofVector(DoubleRange.of(0, 1.0), M)
    );

    static Engine<DoubleGene, Vec<double[]>> engine = Engine.builder(problem).populationSize(100)
            .alterers(
                    new SimulatedBinaryCrossover<>(1),
                    new Mutator<>(1.0 / M)
            ).offspringSelector(new TournamentSelector<>(5))
            .survivorsSelector(NSGA2Selector.ofVec())
            .minimizing().build();

    public static void main(String[] args) {
        List<Point3> point3s = engine.stream()
                .limit(2500)
                .collect(MOEA.toParetoSet(IntRange.of(1000, 1100)))
                .map(Phenotype::fitness)
                .asList()
                .stream()
                .map(Vec::data)
                .map(v -> Point3.create(v[0], v[1], v[2]))
                .collect(Collectors.toList());

        Plot3d.plot3(point3s);

        Plot3d.showThread();
    }

}
