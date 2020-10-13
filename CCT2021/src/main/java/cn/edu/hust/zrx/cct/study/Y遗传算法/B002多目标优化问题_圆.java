package cn.edu.hust.zrx.cct.study.Y遗传算法;

import cn.edu.hust.zrx.cct.base.BaseUtils;
import cn.edu.hust.zrx.cct.base.point.Point2;
import cn.edu.hust.zrx.cct.base.python.Plot2d;
import io.jenetics.DoubleGene;
import io.jenetics.Mutator;
import io.jenetics.Phenotype;
import io.jenetics.TournamentSelector;
import io.jenetics.engine.Codecs;
import io.jenetics.engine.Engine;
import io.jenetics.engine.Problem;
import io.jenetics.ext.SimulatedBinaryCrossover;
import io.jenetics.ext.moea.NSGA2Selector;
import io.jenetics.ext.moea.Vec;
import io.jenetics.util.DoubleRange;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.Math.*;

/**
 * Description
 * B002多目标优化问题
 * <p>
 * Data
 * 16:00
 *
 * @author zrx
 * @version 1.0
 */

public class B002多目标优化问题_圆 {
    public static void main(String[] args) {
        Problem<double[], DoubleGene, Vec<double[]>> problem = Problem.of(
                v -> Vec.of(
                        v[0] * cos(v[1]) + 1,
                        v[0] * sin(v[1]) + 1
                ),
                Codecs.ofVector(
                        DoubleRange.of(0, 3),
                        DoubleRange.of(0, 2 * PI)
                )
        );

        Engine<DoubleGene, Vec<double[]>> engine = Engine.builder(problem)
                .populationSize(100)
                .alterers(
                        new SimulatedBinaryCrossover<>(1),
                        new Mutator<>(0.5)
                ).offspringSelector(new TournamentSelector<>(5))
                .survivorsSelector(NSGA2Selector.ofVec())
                .minimizing()
                .build();

        BaseUtils.Switcher<String> switcher = BaseUtils.Switcher.creat(
                Plot2d.BLACK_POINT,
                Plot2d.RED_POINT,
                Plot2d.BLUE_POINT,
                Plot2d.BLACK_SMALL_POINT,
                Plot2d.YELLOW_POINT,
                Plot2d.YELLOW_SMALL_POINT
        );

        List<String> des = engine.stream()
                .limit(switcher.getSize() * 50 + 1000)
                .map(er -> {
                    long generation = er.generation();
                    if (generation>=1000 && generation % 50 == 0) {
                        List<Point2> point2List = new ArrayList<>(100);
                        for (Phenotype<DoubleGene, Vec<double[]>> doubleGeneVecPhenotype : er.population()) {
                            double[] v = doubleGeneVecPhenotype.fitness().data();
                            point2List.add(Point2.create(v[0], v[1]));
                        }

                        Plot2d.plot2(point2List, switcher.getAndSwitch());

                        System.out.println(point2List.size());

                        return er.generation();
                    } else {
                        return 0L;
                    }
                })
                .filter(g -> g != 0L)
                .map(String::valueOf)
                .collect(Collectors.toList());

        Plot2d.legend(18, des);

        Plot2d.showThread();
    }
}
