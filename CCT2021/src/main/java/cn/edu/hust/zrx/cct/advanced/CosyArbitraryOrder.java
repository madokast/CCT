package cn.edu.hust.zrx.cct.advanced;

import cn.edu.hust.zrx.cct.base.particle.PhaseSpaceParticle;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Description
 * 新的 COSY MAP 计算器
 * 可以支持任意阶数
 * <p>
 * Data
 * 22:18
 *
 * @author zrx
 * @version 1.0
 */

public class CosyArbitraryOrder {

    private CosyArbitraryOrder() {
    }

    public static CosyMapArbitraryOrder readMap(String mapString) {
        return new CosyMapArbitraryOrder(mapString);
    }


    //任意阶数的MAP
    public static class CosyMapArbitraryOrder {
        String map;

        public CosyMapArbitraryOrder(String map) {
            this.map = map;
        }

        private static final int ITEM_LENGTH = "-0.0000000E+00".length();

        public PhaseSpaceParticle apply(final PhaseSpaceParticle p0) {
            double x = 0;
            double xp = 0;
            double y = 0;
            double yp = 0;

            String[] contributionLinesString = map.split("\n");
            for (String contributionLineString : contributionLinesString) {
                String xContributionString = contributionLineString.substring(0, ITEM_LENGTH + 1);
                String xpContributionString = contributionLineString.substring(ITEM_LENGTH + 1, ITEM_LENGTH * 2 + 1);
                String yContributionString = contributionLineString.substring(ITEM_LENGTH * 2 + 1, ITEM_LENGTH * 3 + 1);
                String ypContributionString = contributionLineString.substring(ITEM_LENGTH * 3 + 1, ITEM_LENGTH * 4 + 1);

                String contributionDescribing = contributionLineString.substring(ITEM_LENGTH * 5 + 2);
                double contributionBy = contributionBy(contributionDescribing, p0);

                double xContribution = Double.parseDouble(xContributionString);
                x += xContribution * contributionBy;

                double xpContribution = Double.parseDouble(xpContributionString);
                xp += xpContribution * contributionBy;

                double yContribution = Double.parseDouble(yContributionString);
                y += yContribution * contributionBy;

                double ypContribution = Double.parseDouble(ypContributionString);
                yp += ypContribution * contributionBy;

//                System.out.println(
//                        String.format("Order[%s]x[%2.4e]xp[%2.4e]y[%2.4e]yp[%2.4e]",
//                                contributionDescribing,xContribution * contributionBy,
//                                xpContribution * contributionBy,yContribution * contributionBy,
//                                ypContribution * contributionBy
//                        )
//                );

                System.out.println(
                        String.format("%s\t%2.4e\t%2.4e\t%2.4e\t%2.4e",
                                contributionDescribing,xContribution * contributionBy,
                                xpContribution * contributionBy,yContribution * contributionBy,
                                ypContribution * contributionBy
                        )
                );
            }

            return PhaseSpaceParticle.create(x, xp, y, yp, p0.getZ(), p0.getDelta());
        }

        public PhaseSpaceParticle apply(final PhaseSpaceParticle p0, int order) {
            double x = 0;
            double xp = 0;
            double y = 0;
            double yp = 0;

            String[] contributionLinesString = map.split("\n");
            for (String contributionLineString : contributionLinesString) {
                String xContributionString = contributionLineString.substring(0, ITEM_LENGTH + 1);
                String xpContributionString = contributionLineString.substring(ITEM_LENGTH + 1, ITEM_LENGTH * 2 + 1);
                String yContributionString = contributionLineString.substring(ITEM_LENGTH * 2 + 1, ITEM_LENGTH * 3 + 1);
                String ypContributionString = contributionLineString.substring(ITEM_LENGTH * 3 + 1, ITEM_LENGTH * 4 + 1);

                String contributionDescribing = contributionLineString.substring(ITEM_LENGTH * 5 + 2);

                int thisOrder = order(contributionDescribing);
                if (thisOrder > order)
                    break;

                double contributionBy = contributionBy(contributionDescribing, p0);

                double xContribution = Double.parseDouble(xContributionString);
                x += xContribution * contributionBy;

                double xpContribution = Double.parseDouble(xpContributionString);
                xp += xpContribution * contributionBy;

                double yContribution = Double.parseDouble(yContributionString);
                y += yContribution * contributionBy;

                double ypContribution = Double.parseDouble(ypContributionString);
                yp += ypContribution * contributionBy;
            }

            return PhaseSpaceParticle.create(x, xp, y, yp, p0.getZ(), p0.getDelta());
        }

        private int order(String contributionDescribing) {
            int order = 0;
            for (int i = 0; i <= 5; i++) {
                if (i == 4)
                    continue;

                order += (contributionDescribing.charAt(i) - '0');
            }

            return order;
        }

        private double contributionBy(String contributionDescribing, PhaseSpaceParticle pp) {
            double by = 1;

            double x = pp.getX();
            double xp = pp.getXp();
            double y = pp.getY();
            double yp = pp.getYp();
            double delta = pp.getDelta();


            int byX = contributionDescribing.charAt(0) - '0';
            by *= Math.pow(x, byX);

            int byXP = contributionDescribing.charAt(1) - '0';
            by *= Math.pow(xp, byXP);

            int byY = contributionDescribing.charAt(2) - '0';
            by *= Math.pow(y, byY);

            int byYP = contributionDescribing.charAt(3) - '0';
            by *= Math.pow(yp, byYP);

            int byDelta = contributionDescribing.charAt(5) - '0';
            by *= Math.pow(delta, byDelta);

            return by;
        }


        public List<PhaseSpaceParticle> apply(final List<PhaseSpaceParticle> p0s) {
            return p0s.stream()
                    .map(this::apply)
                    .collect(Collectors.toList());
        }

        public List<PhaseSpaceParticle> apply(final List<PhaseSpaceParticle> p0s, int order) {
            return p0s.stream()
                    .map(p -> this.apply(p, order))
                    .collect(Collectors.toList());
        }

        public String getMap() {
            return map;
        }

        public double getR(int i,int j){
            String rEnd = "000001\n";

            int indexOf = map.indexOf(rEnd);

            return COSY.importMatrix(map.substring(0, indexOf + rEnd.length())).getR(i, j);
        }
    }
}
