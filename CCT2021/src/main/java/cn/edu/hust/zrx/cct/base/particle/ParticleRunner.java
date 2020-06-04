package cn.edu.hust.zrx.cct.base.particle;

import cn.edu.hust.zrx.cct.Logger;
import cn.edu.hust.zrx.cct.base.BaseUtils;
import cn.edu.hust.zrx.cct.base.cct.CctFactory;
import cn.edu.hust.zrx.cct.base.point.Point2;
import cn.edu.hust.zrx.cct.base.point.Point3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Description
 * ParticleRunner
 * 粒子运动指挥所
 * <p>
 * Data
 * 22:15
 *
 * @author zrx
 * @version 1.0
 */

public class ParticleRunner {
    public static List<Point3> run(
            RunningParticle particle,
            CctFactory.MagnetAble cct,
            double length, double footStep) {
        BaseUtils.Equal.requireTrue(length > footStep, "粒子运动距离应大于步长");

        int count = (int) (length / footStep) + 1;

        footStep = length / (count - 1);

        List<Point3> ret = new ArrayList<>(count);//提前开辟空间
        for (long i = 0; i < count - 1/*让粒子不要多走哪怕一步*/; i++) {
            Point3 position = particle.position.copy();
            ret.add(position);

            particle.runSelf(cct.magnetAt(position), footStep);
        }

        Point3 position = particle.position.copy();
        ret.add(position);

        return ret;

        //  ----------------------------------
        // |                                  |
        // |                                  |
        // |  下面代码在并行下存在巨大性能问题   |
        // |                                  |
        // |                                  |
        // |                                  |
        //  ----------------------------------
//        long count = BaseUtils.Python.disperse(length, footStep).count() + 1;
//        return BaseUtils.StreamTools.repeat((int) count)
//                .mapToObj(noUse -> {
//                    Point3 position = particle.position.copy();
//                    particle.runSelf(cct.magnetAt(position), footStep);
//                    return position;
//                }).collect(Collectors.toList());
    }

    public static List<List<Point3>> run(List<RunningParticle> particles,
                                         CctFactory.MagnetAble cct,
                                         double length, double footStep) {
        return particles
                .stream()
                .map(particle -> run(particle, cct, length, footStep))
                .collect(Collectors.toList());
    }

    public static List<List<Point3>> runThread(List<RunningParticle> particles,
                                               CctFactory.MagnetAble cct,
                                               double length, double footStep) {
        return particles
                .stream()
                .parallel()
//                .peek(particle->Logger.getLogger().info("{}",particle))
                .map(particle -> run(particle, cct, length, footStep))
                .collect(Collectors.toList());
    }

    /**
     * @param particle 粒子
     * @param cct      cct
     * @param length   长度
     * @param footStep 步长
     * @return bi(运行长度, 当地粒子全部信息)
     */
    public static List<BaseUtils.Content.BiContent<Double, RunningParticle>> runGetAllInfo(
            RunningParticle particle,
            CctFactory.MagnetAble cct,
            double length, double footStep) {
        BaseUtils.Equal.requireTrue(length > footStep, "粒子运动距离应大于步长");

        int count = (int) (length / footStep) + 1;
        footStep = length / (count - 1);

        List<BaseUtils.Content.BiContent<Double, RunningParticle>> ret = new ArrayList<>(count);//提前开辟空间
        for (long i = 0; i < count - 1/*让粒子不要多走哪怕一步*/; i++) {
            ret.add(BaseUtils.Content.BiContent.create(particle.distance, particle.copy()));
            particle.runSelf(cct.magnetAt(particle.position), footStep);
        }

        ret.add(BaseUtils.Content.BiContent.create(particle.distance, particle.copy()));

        return ret;
    }

    public static List<BaseUtils.Content.BiContent<Double, List<RunningParticle>>> runGetAllInfo(
            List<RunningParticle> particles,
            CctFactory.MagnetAble cct,
            double length, double footStep) {
        int particleNumber = particles.size();
        if (particleNumber <= 0)
            throw new IllegalArgumentException("粒子数目<=0");

        List<List<BaseUtils.Content.BiContent<Double, RunningParticle>>> collect = particles.stream()
                .map(particle -> runGetAllInfo(particle, cct, length, footStep))
                .collect(Collectors.toList());

        int footNumber = collect.get(0).size();
        Logger.getLogger().info("footNumber = " + footNumber);

        List<BaseUtils.Content.BiContent<Double, List<RunningParticle>>> ans = new ArrayList<>(footNumber);
        for (int i = 0; i < footNumber; i++) {
            ans.add(null);
        }
//        Collections.fill(ans, null);

        collect.forEach(list -> {
            for (int i = 0; i < list.size(); i++) {
                BaseUtils.Content.BiContent<Double, RunningParticle> doubleRunningParticleBiContent = list.get(i);

                Double distance = doubleRunningParticleBiContent.getT1();

                RunningParticle runningParticle = doubleRunningParticleBiContent.getT2();

                BaseUtils.Content.BiContent<Double, List<RunningParticle>> ansI = ans.get(i);

                if (ansI == null) {
                    BaseUtils.Content.BiContent<Double, List<RunningParticle>> biContent =
                            BaseUtils.Content.BiContent.create(distance, new ArrayList<>(particleNumber));

                    biContent.getT2().add(runningParticle);

                    ans.set(i, biContent);
                } else {

                    ansI.getT2().add(runningParticle);
                }
            }
        });

        return ans;
    }

    @Deprecated
    public static void justRun(
            RunningParticle particle,
            CctFactory.MagnetAble cct,
            double length, double footStep) {
        long count = BaseUtils.Python.disperse(length, footStep).count() + 1;
        BaseUtils.StreamTools.repeat((int) count)
                .forEach(noUse ->
                        particle.runSelf(cct.magnetAt(particle.position), footStep)
                );
    }

    @Deprecated
    public static void justRunThread(List<RunningParticle> particles,
                                     CctFactory.MagnetAble cct,
                                     double length, double footStep) {
        particles
                .stream()
                .parallel()
                .forEach(particle -> justRun(particle, cct, length, footStep));
    }

    @Deprecated
    public static void justRun(List<RunningParticle> particles,
                               CctFactory.MagnetAble cct,
                               double length, double footStep) {
        particles.forEach(particle -> justRun(particle, cct, length, footStep));
    }

    public static List<ParticleRunner.Point3WithDistance> runGetPoint3WithDistance(
            RunningParticle particle,
            CctFactory.MagnetAble cct,
            double length, double footStep
    ) {
        long count = BaseUtils.Python.disperse(length, footStep).count() + 1;
        return BaseUtils.StreamTools.repeat((int) count)
                .mapToObj(noUse -> {
                    Point3 position = particle.position.copy();
                    double distance = particle.distance;
                    particle.runSelf(cct.magnetAt(position), footStep);
                    return new Point3WithDistance(position, distance);
                }).collect(Collectors.toList());
    }

    public static class Point3WithDistance {
        private Point3 point3;
        private double distance;

        private Point3WithDistance(Point3 point3, double distance) {
            this.point3 = point3;
            this.distance = distance;
        }

        public Point3 getPoint3() {
            return point3;
        }

        public double getDistance() {
            return distance;
        }

        public Point2 getDistanceWithX() {
            return Point2.create(distance, point3.x);
        }

        public Point2 getDistanceWithY() {
            return Point2.create(distance, point3.y);
        }

        public Point2 getDistanceWithZ() {
            return Point2.create(distance, point3.z);
        }
    }
}
