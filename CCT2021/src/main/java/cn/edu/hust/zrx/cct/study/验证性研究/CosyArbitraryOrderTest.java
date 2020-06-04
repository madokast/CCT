package cn.edu.hust.zrx.cct.study.验证性研究;

import cn.edu.hust.zrx.cct.advanced.COSY;
import cn.edu.hust.zrx.cct.base.particle.PhaseSpaceParticle;

/**
 * Description
 * CosyArbitraryOrderTest
 * <p>
 * Data
 * 22:20
 *
 * @author zrx
 * @version 1.0
 */
@SuppressWarnings("all")
public class CosyArbitraryOrderTest {

    private static final int ITEM_LENGTH = "-0.0000000E+00".length();

    public static void main(String[] args) {
        test01();
    }

    private static void test01(){
        PhaseSpaceParticle pp = PhaseSpaceParticle.create(-8.1118E-04, -2.9127E-03, 2.2512E-04, -3.8987E-03, 0, 2.5055E-01);

        double x = 0;
        double xp = 0;
        double y = 0;
        double yp = 0;

        String map = getMap();
        String[] contributionLinesString = map.split("\n");
        for (String contributionLineString : contributionLinesString) {
            String xContributionString = contributionLineString.substring(0, ITEM_LENGTH+1);
            String xpContributionString = contributionLineString.substring(ITEM_LENGTH+1, ITEM_LENGTH * 2+1);
            String yContributionString = contributionLineString.substring(ITEM_LENGTH * 2+1, ITEM_LENGTH * 3+1);
            String ypContributionString = contributionLineString.substring(ITEM_LENGTH * 3+1, ITEM_LENGTH * 4+1);

            String contributionDescribing = contributionLineString.substring(ITEM_LENGTH * 5 + 2);
            double contributionBy = contributionBy(contributionDescribing, pp);

            double xContribution = Double.parseDouble(xContributionString);
            x+=xContribution* contributionBy;

            double xpContribution = Double.parseDouble(xpContributionString);
            xp+=xpContribution* contributionBy;

            double yContribution = Double.parseDouble(yContributionString);
            y+=yContribution* contributionBy;

            double ypContribution = Double.parseDouble(ypContributionString);
            yp+=ypContribution*contributionBy;
        }

        PhaseSpaceParticle p = PhaseSpaceParticle.create(x, xp, y, yp, pp.getZ(), pp.getDelta());

        System.out.println("p = " + p);

        //-----------------------
        COSY.TransMatrix transMatrix = COSY.importMatrix(getMap());
        PhaseSpaceParticle apply = transMatrix.apply(pp);
        System.out.println("apply = " + apply);
    }

    private static double contributionBy(String contributionDescribing, PhaseSpaceParticle pp) {
        double by = 1;

        double x = pp.getX();
        double xp = pp.getXp();
        double y = pp.getY();
        double yp = pp.getYp();
        double delta = pp.getDelta();


        int byX = contributionDescribing.charAt(0) - '0';
        by*=Math.pow(x,byX);

        int byXP = contributionDescribing.charAt(1) - '0';
        by*=Math.pow(xp,byXP);

        int byY = contributionDescribing.charAt(2) - '0';
        by*=Math.pow(y,byY);

        int byYP = contributionDescribing.charAt(3) - '0';
        by*=Math.pow(yp,byYP);

        int byDelta = contributionDescribing.charAt(5) - '0';
        by*=Math.pow(delta,byDelta);

        return by;
    }


    private static String getMap() {
        return "   1.000000     0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00 100000\n" +
                "   1.200000      1.000000     0.0000000E+00 0.0000000E+00 0.0000000E+00 010000\n" +
                "  0.0000000E+00 0.0000000E+00  1.000000     0.0000000E+00 0.0000000E+00 001000\n" +
                "  0.0000000E+00 0.0000000E+00  1.200000      1.000000     0.0000000E+00 000100\n" +
                "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00  1.000000     000010\n" +
                "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00 0.2336092     000001\n" +
                "  -493.3950     -822.3250     0.0000000E+00 0.0000000E+00 0.0000000E+00 200000\n" +
                "  -586.5919     -986.7900     0.0000000E+00 0.0000000E+00 0.0000000E+00 110000\n" +
                "  -175.9776     -298.7781     0.0000000E+00 0.0000000E+00-0.3352685     020000\n" +
                "  0.0000000E+00 0.0000000E+00  986.7900      1644.650     0.0000000E+00 101000\n" +
                "  0.0000000E+00 0.0000000E+00  586.5919      986.7900     0.0000000E+00 011000\n" +
                "   493.3950      822.3250     0.0000000E+00 0.0000000E+00 0.0000000E+00 002000\n" +
                "  0.0000000E+00 0.0000000E+00  586.5919      986.7900     0.0000000E+00 100100\n" +
                "  0.0000000E+00 0.0000000E+00  351.9551      597.5562     0.0000000E+00 010100\n" +
                "   586.5919      986.7900     0.0000000E+00 0.0000000E+00 0.0000000E+00 001100\n" +
                " -0.6705370     0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00 010001\n" +
                "   175.9776      298.7781     0.0000000E+00 0.0000000E+00-0.3352685     000200\n" +
                "  0.0000000E+00 0.0000000E+00-0.6705370     0.0000000E+00 0.0000000E+00 000101\n" +
                "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00-0.1958045     000002";
    }
}
