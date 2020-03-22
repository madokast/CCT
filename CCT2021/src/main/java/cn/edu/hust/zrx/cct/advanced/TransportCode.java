package cn.edu.hust.zrx.cct.advanced;

import cn.edu.hust.zrx.cct.base.particle.PhaseSpaceParticle;
import cn.edu.hust.zrx.cct.base.particle.PhaseSpaceParticles;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Description
 * TransportCode
 * 著名程序 transport
 * <p>
 * Data
 * 15:02
 *
 * @author zrx
 * @version 1.0
 */

public class TransportCode {

    private static class Particle {
        double[] p;

        private Particle() {
        }

        private static Particle getFromPhaseSpaceParticle(PhaseSpaceParticle ps) {
            Particle particle = new Particle();

            double[] p0 = new double[7];

            p0[1] = ps.getX();
            p0[2] = ps.getXp();
            p0[3] = ps.getY();
            p0[4] = ps.getYp();
            p0[5] = ps.getZ();
            p0[6] = ps.getDelta();

            particle.p = p0;

            return particle;
        }

        private static PhaseSpaceParticle getPhaseSpaceParticleFromParticle(Particle p) {
            return PhaseSpaceParticles.create(
                    p.p[1],
                    p.p[2],
                    p.p[3],
                    p.p[4],
                    p.p[5],
                    p.p[6]
            );
        }

        private static Particle createIdealParticle() {
            Particle particle = new Particle();
            particle.p = new double[7];
            Arrays.fill(particle.p, 0.0);

            return particle;
        }
    }

    public static class TransMatrix{
        private double[][] R;

        private TransMatrix(){}

        public static TransMatrix create(double[][] r){
            TransMatrix transMatrix = new TransMatrix();
            transMatrix.R = r;
            return transMatrix;
        }

        public PhaseSpaceParticle apply(PhaseSpaceParticle p0){
            Particle particle0 = Particle.getFromPhaseSpaceParticle(p0);
            double[] x0 = particle0.p;
            double[][] r = this.R;

            Particle idealParticle = Particle.createIdealParticle();
            double[] x = idealParticle.p;

            for (int i = 1; i < r.length; i++) {
                for (int j = 1; j < r[i].length; j++) {
                    x[i] += r[i][j] * x0[j];
                }
            }

            return Particle.getPhaseSpaceParticleFromParticle(idealParticle);
        }

        public List<PhaseSpaceParticle> apply(List<PhaseSpaceParticle> ps){
            return ps.stream()
                    .map(this::apply)
                    .collect(Collectors.toList());
        }

        @Override
        public String toString() {
            StringBuilder stringBuilder = new StringBuilder("\n");
            stringBuilder.append("---  R  ---\n");
            for (int i = 1; i < R.length; i++) {
                for (int j = 1; j < R[i].length; j++) {
                    stringBuilder.append(String.format("%+5.3f",R[i][j])).append("\t");
                }
                stringBuilder.append("\n");
            }

            return stringBuilder.toString();
        }
    }
}
