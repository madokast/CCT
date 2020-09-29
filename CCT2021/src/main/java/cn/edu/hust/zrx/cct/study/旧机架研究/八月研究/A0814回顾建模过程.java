package cn.edu.hust.zrx.cct.study.旧机架研究.八月研究;

import cn.edu.hust.zrx.cct.Logger;
import cn.edu.hust.zrx.cct.advanced.COSY;
import cn.edu.hust.zrx.cct.advanced.CosyArbitraryOrder;
import cn.edu.hust.zrx.cct.advanced.PolynomialFitter;
import cn.edu.hust.zrx.cct.advanced.SR;
import cn.edu.hust.zrx.cct.base.BaseUtils;
import cn.edu.hust.zrx.cct.base.cct.Cct;
import cn.edu.hust.zrx.cct.base.cct.CctFactory;
import cn.edu.hust.zrx.cct.base.cct.Elements;
import cn.edu.hust.zrx.cct.base.cct.MagnetAble;
import cn.edu.hust.zrx.cct.base.line.Arcs;
import cn.edu.hust.zrx.cct.base.line.Line2;
import cn.edu.hust.zrx.cct.base.line.Trajectory;
import cn.edu.hust.zrx.cct.base.line.TrajectoryFactory;
import cn.edu.hust.zrx.cct.base.opticsMagnet.qs.QsHardPlaneMagnet;
import cn.edu.hust.zrx.cct.base.particle.*;
import cn.edu.hust.zrx.cct.base.point.Point2;
import cn.edu.hust.zrx.cct.base.point.Point3;
import cn.edu.hust.zrx.cct.base.python.Plot2d;
import cn.edu.hust.zrx.cct.base.vector.Vector2;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Description
 * A0814回顾建模过程
 * <p>
 * Data
 * 16:58
 *
 * @author zrx
 * @version 1.0
 */

public class A0814回顾建模过程 {

    @run(1)
    public void 前偏转段回顾() {
        Trajectory trajectoryPart1 = getTrajectoryPart1();

        Elements elementsOfAllPart1 = getElementsOfAllPart1();

        phase相椭圆画图(trajectoryPart1.getLength(), false, 5 * PRESENT, 32, false,
                1, elementsOfAllPart1, trajectoryPart1, 512, 5, List.of(
                        BaseUtils.Content.BiContent.create("optic", COSY_MAP.optics前偏转段.map)
                ));
    }


    @run(2)
    public void 廖益诚0814最新结果的贡献度查看() {
        CosyArbitraryOrder.CosyMapArbitraryOrder map = CosyArbitraryOrder.readMap(
                "  0.2971867      1.732245     0.0000000E+00 0.0000000E+00 0.2360767E-02 100000\n" +
                        " -0.5362395     0.2392488     0.0000000E+00 0.0000000E+00-0.3330065E-02 010000\n" +
                        "  0.0000000E+00 0.0000000E+00 0.7546332     0.4637937     0.0000000E+00 001000\n" +
                        "  0.0000000E+00 0.0000000E+00-0.5211197      1.004870     0.0000000E+00 000100\n" +
                        "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00  1.000000     000010\n" +
                        "  0.2762855E-03-0.6333300E-02 0.0000000E+00 0.0000000E+00  1.902852     000001\n" +
                        "   5.854377      10.58362     0.0000000E+00 0.0000000E+00 -3.814481     200000\n" +
                        "   9.236384      6.217213     0.0000000E+00 0.0000000E+00 -1.065555     110000\n" +
                        "  0.4947589     -6.443124     0.0000000E+00 0.0000000E+00-0.3399430     020000\n" +
                        "  0.0000000E+00 0.0000000E+00  32.16588      3.275162     0.0000000E+00 101000\n" +
                        "  0.0000000E+00 0.0000000E+00  16.22980     -1.919457     0.0000000E+00 011000\n" +
                        "   4.670968      6.285173     0.0000000E+00 0.0000000E+00 -3.336912     002000\n" +
                        "  0.0000000E+00 0.0000000E+00  20.74088     -32.34655     0.0000000E+00 100100\n" +
                        "  0.0000000E+00 0.0000000E+00  8.218834     -15.23484     0.0000000E+00 010100\n" +
                        "   22.79734      18.37673     0.0000000E+00 0.0000000E+00 -1.909498     001100\n" +
                        "  -4.468416    -0.1057482     0.0000000E+00 0.0000000E+00-0.2235510     100001\n" +
                        " -0.8065394    -0.9130790     0.0000000E+00 0.0000000E+00-0.3184573E-01 010001\n" +
                        "  0.0000000E+00 0.0000000E+00 -5.064261      5.814336     0.0000000E+00 001001\n" +
                        "   1.116081    -0.1998761     0.0000000E+00 0.0000000E+00 -2.437588     000200\n" +
                        "  0.0000000E+00 0.0000000E+00 -4.762518    -0.1985973     0.0000000E+00 000101\n" +
                        " -0.5627843E-01 0.4136959E-03 0.0000000E+00 0.0000000E+00 -1.825521     000002\n" +
                        "   13.94834     -40.63324     0.0000000E+00 0.0000000E+00 -25.60111     300000\n" +
                        "  -135.6757     -191.6463     0.0000000E+00 0.0000000E+00 -2.024810     210000\n" +
                        "  -188.6751     -355.6329     0.0000000E+00 0.0000000E+00  39.54614     120000\n" +
                        "  -83.80640     -83.56217     0.0000000E+00 0.0000000E+00  2.417036     030000\n" +
                        "  0.0000000E+00 0.0000000E+00  1315.882      110.3402     0.0000000E+00 201000\n" +
                        "  0.0000000E+00 0.0000000E+00  743.9829      216.3132     0.0000000E+00 111000\n" +
                        "  0.0000000E+00 0.0000000E+00 0.5749086      25.47969     0.0000000E+00 021000\n" +
                        "   321.7705      187.2028     0.0000000E+00 0.0000000E+00 -45.90345     102000\n" +
                        "   47.60153      67.24746     0.0000000E+00 0.0000000E+00 -39.52336     012000\n" +
                        "  0.0000000E+00 0.0000000E+00  149.6319     -31.67593     0.0000000E+00 003000\n" +
                        "  0.0000000E+00 0.0000000E+00  1894.606      804.7632     0.0000000E+00 200100\n" +
                        "  0.0000000E+00 0.0000000E+00  621.4937      569.8673     0.0000000E+00 110100\n" +
                        "  0.0000000E+00 0.0000000E+00 -38.82641      264.5253     0.0000000E+00 020100\n" +
                        "   861.8667      367.3029     0.0000000E+00 0.0000000E+00 -128.5634     101100\n" +
                        "   231.2940      98.55317     0.0000000E+00 0.0000000E+00  5.645543     011100\n" +
                        "  0.0000000E+00 0.0000000E+00  451.9291     -102.1195     0.0000000E+00 002100\n" +
                        "   16.38194      40.60204     0.0000000E+00 0.0000000E+00  20.36115     200001\n" +
                        "   22.71643      28.23161     0.0000000E+00 0.0000000E+00  11.69063     110001\n" +
                        "   19.35767     -13.08257     0.0000000E+00 0.0000000E+00  5.106043     020001\n" +
                        "  0.0000000E+00 0.0000000E+00  11.16138     -208.8809     0.0000000E+00 101001\n" +
                        "  0.0000000E+00 0.0000000E+00 -20.12782     -21.07212     0.0000000E+00 011001\n" +
                        "   96.76209      40.37603     0.0000000E+00 0.0000000E+00  20.11617     002001\n" +
                        "   1373.639      276.7134     0.0000000E+00 0.0000000E+00  108.9200     100200\n" +
                        "   262.5044      93.64166     0.0000000E+00 0.0000000E+00  36.99811     010200\n" +
                        "  0.0000000E+00 0.0000000E+00  270.4344     -343.9959     0.0000000E+00 001200\n" +
                        "  0.0000000E+00 0.0000000E+00  6.697815      64.02547     0.0000000E+00 100101\n" +
                        "  0.0000000E+00 0.0000000E+00 -11.87736      11.52448     0.0000000E+00 010101\n" +
                        "  -101.3256     -24.86821     0.0000000E+00 0.0000000E+00  7.752027     001101\n" +
                        "   12.72434      2.016913     0.0000000E+00 0.0000000E+00 0.1222921     100002\n" +
                        "   5.621248      5.440431     0.0000000E+00 0.0000000E+00-0.3050003     010002\n" +
                        "  0.0000000E+00 0.0000000E+00  3.657991     -24.52442     0.0000000E+00 001002\n" +
                        "  0.0000000E+00 0.0000000E+00  224.1088      119.0272     0.0000000E+00 000300\n" +
                        "  -42.67019     -53.46481     0.0000000E+00 0.0000000E+00  28.37855     000201\n" +
                        "  0.0000000E+00 0.0000000E+00  31.82411     -6.403703     0.0000000E+00 000102\n" +
                        " -0.3957007E-01-0.2593019     0.0000000E+00 0.0000000E+00  1.794877     000003\n" +
                        "   44.84095     -1797.048     0.0000000E+00 0.0000000E+00 -447.6715     400000\n" +
                        "  -1685.713     -3776.756     0.0000000E+00 0.0000000E+00  212.0602     310000\n" +
                        "  -2550.126     -2888.169     0.0000000E+00 0.0000000E+00  1156.855     220000\n" +
                        "   33.52991      382.5937     0.0000000E+00 0.0000000E+00  327.7694     130000\n" +
                        "   513.0507      1102.696     0.0000000E+00 0.0000000E+00 -107.5196     040000\n" +
                        "  0.0000000E+00 0.0000000E+00  24775.58      11325.01     0.0000000E+00 301000\n" +
                        "  0.0000000E+00 0.0000000E+00  11112.51      6154.569     0.0000000E+00 211000\n" +
                        "  0.0000000E+00 0.0000000E+00 -11510.35      877.4225     0.0000000E+00 121000\n" +
                        "  0.0000000E+00 0.0000000E+00 -3172.761     -974.0487     0.0000000E+00 031000\n" +
                        "   519.0335     -374.4315     0.0000000E+00 0.0000000E+00 -2385.171     202000\n" +
                        "  -2619.502     -1089.290     0.0000000E+00 0.0000000E+00 -1033.648     112000\n" +
                        "  -2594.031     -784.5380     0.0000000E+00 0.0000000E+00 -298.9987     022000\n" +
                        "  0.0000000E+00 0.0000000E+00  9207.584      2482.181     0.0000000E+00 103000\n" +
                        "  0.0000000E+00 0.0000000E+00  3216.273      585.1286     0.0000000E+00 013000\n" +
                        "   47.53482      137.1307     0.0000000E+00 0.0000000E+00 -304.8806     004000\n" +
                        "  0.0000000E+00 0.0000000E+00  21106.38      5516.733     0.0000000E+00 300100\n" +
                        "  0.0000000E+00 0.0000000E+00 -4929.566     -194.5490     0.0000000E+00 210100\n" +
                        "  0.0000000E+00 0.0000000E+00 -21349.11     -5579.756     0.0000000E+00 120100\n" +
                        "  0.0000000E+00 0.0000000E+00 -2912.989     -2194.064     0.0000000E+00 030100\n" +
                        "   7143.131      1344.078     0.0000000E+00 0.0000000E+00 -5403.779     201100\n" +
                        "  -16390.84     -3429.516     0.0000000E+00 0.0000000E+00 -3211.872     111100\n" +
                        "  -7083.211     -2681.065     0.0000000E+00 0.0000000E+00  83.84625     021100\n" +
                        "  0.0000000E+00 0.0000000E+00  34551.55      7703.180     0.0000000E+00 102100\n" +
                        "  0.0000000E+00 0.0000000E+00  9349.943      5137.605     0.0000000E+00 012100\n" +
                        "   4603.039      1841.270     0.0000000E+00 0.0000000E+00 -1142.223     003100\n" +
                        "  -1287.809     -68.91592     0.0000000E+00 0.0000000E+00  13.63310     300001\n" +
                        "  -684.2679     -535.0095     0.0000000E+00 0.0000000E+00  16.26667     210001\n" +
                        "   69.22461     -517.3563     0.0000000E+00 0.0000000E+00 -158.3187     120001\n" +
                        "  -68.78800     -305.7537     0.0000000E+00 0.0000000E+00 -24.41890     030001\n" +
                        "  0.0000000E+00 0.0000000E+00  5115.757      5190.162     0.0000000E+00 201001\n" +
                        "  0.0000000E+00 0.0000000E+00  524.6986      732.4345     0.0000000E+00 111001\n" +
                        "  0.0000000E+00 0.0000000E+00 -619.6607      1152.302     0.0000000E+00 021001\n" +
                        "   2534.023      925.5889     0.0000000E+00 0.0000000E+00 -249.1666     102001\n" +
                        "   2017.917      250.3786     0.0000000E+00 0.0000000E+00  359.2516     012001\n" +
                        "  0.0000000E+00 0.0000000E+00  96.89819     -31.56984     0.0000000E+00 003001\n" +
                        "  -17250.10     -4412.250     0.0000000E+00 0.0000000E+00 -6440.921     200200\n" +
                        "  -30202.48     -6132.503     0.0000000E+00 0.0000000E+00 -4379.233     110200\n" +
                        "  -10288.86     -1175.057     0.0000000E+00 0.0000000E+00 -1313.342     020200\n" +
                        "  0.0000000E+00 0.0000000E+00  25935.42      17192.62     0.0000000E+00 101200\n" +
                        "  0.0000000E+00 0.0000000E+00  3300.654      4189.871     0.0000000E+00 011200\n" +
                        "   10643.44      4412.615     0.0000000E+00 0.0000000E+00 -1265.941     002200\n" +
                        "  0.0000000E+00 0.0000000E+00 -4567.199     -1517.567     0.0000000E+00 200101\n" +
                        "  0.0000000E+00 0.0000000E+00 -5229.588     -2403.432     0.0000000E+00 110101\n" +
                        "  0.0000000E+00 0.0000000E+00 -1449.079     -399.2864     0.0000000E+00 020101\n" +
                        "   12778.34      2870.178     0.0000000E+00 0.0000000E+00  1595.880     101101\n" +
                        "   2162.774      809.4576     0.0000000E+00 0.0000000E+00  241.7520     011101\n" +
                        "  0.0000000E+00 0.0000000E+00 -1449.542     -1351.127     0.0000000E+00 002101\n" +
                        "  -72.08316      17.29986     0.0000000E+00 0.0000000E+00 -38.83304     200002\n" +
                        "  -76.23285     -111.5171     0.0000000E+00 0.0000000E+00 -26.98545     110002\n" +
                        "  -24.27480      36.27886     0.0000000E+00 0.0000000E+00 -17.22477     020002\n" +
                        "  0.0000000E+00 0.0000000E+00  96.80915      1405.698     0.0000000E+00 101002\n" +
                        "  0.0000000E+00 0.0000000E+00 -3.211429      147.2611     0.0000000E+00 011002\n" +
                        "  -895.3036     -156.2330     0.0000000E+00 0.0000000E+00 -80.68318     002002\n" +
                        "  0.0000000E+00 0.0000000E+00 -5145.941     -4778.644     0.0000000E+00 100300\n" +
                        "  0.0000000E+00 0.0000000E+00 -3062.052     -4356.590     0.0000000E+00 010300\n" +
                        "   13410.69      4924.942     0.0000000E+00 0.0000000E+00  6.978039     001300\n" +
                        "  -1759.573     -716.6799     0.0000000E+00 0.0000000E+00 -113.9649     100201\n" +
                        "  -1850.813     -691.9049     0.0000000E+00 0.0000000E+00 -443.7866     010201\n" +
                        "  0.0000000E+00 0.0000000E+00 -204.7971      3397.701     0.0000000E+00 001201\n" +
                        "  0.0000000E+00 0.0000000E+00  219.2276      731.3947     0.0000000E+00 100102\n" +
                        "  0.0000000E+00 0.0000000E+00  63.42533     -81.39290     0.0000000E+00 010102\n" +
                        "  -148.6466     -229.9644     0.0000000E+00 0.0000000E+00  194.7347     001102\n" +
                        "  -13.24725      14.59838     0.0000000E+00 0.0000000E+00  2.001462     100003\n" +
                        "  -19.40368     -15.35568     0.0000000E+00 0.0000000E+00  2.892113     010003\n" +
                        "  0.0000000E+00 0.0000000E+00  134.5498      101.9241     0.0000000E+00 001003\n" +
                        "   3283.286      2039.323     0.0000000E+00 0.0000000E+00 -219.4974     000400\n" +
                        "  0.0000000E+00 0.0000000E+00 -796.9905      2433.485     0.0000000E+00 000301\n" +
                        "   681.1614      166.6819     0.0000000E+00 0.0000000E+00  14.85400     000202\n" +
                        "  0.0000000E+00 0.0000000E+00 -31.58650      88.99851     0.0000000E+00 000103\n" +
                        "   1.083901      1.453635     0.0000000E+00 0.0000000E+00 -2.027167     000004\n" +
                        "   62421.69      7506.514     0.0000000E+00 0.0000000E+00 -938.0856     500000\n" +
                        "   78746.38      24225.61     0.0000000E+00 0.0000000E+00  9886.541     410000\n" +
                        "   53306.63      65930.04     0.0000000E+00 0.0000000E+00  23263.02     320000\n" +
                        "   56309.63      73439.96     0.0000000E+00 0.0000000E+00  2738.322     230000\n" +
                        "   29442.83      33943.40     0.0000000E+00 0.0000000E+00 -7977.976     140000\n" +
                        "   1783.719      3464.380     0.0000000E+00 0.0000000E+00 -1767.125     050000\n" +
                        "  0.0000000E+00 0.0000000E+00  155111.8      24151.78     0.0000000E+00 401000\n" +
                        "  0.0000000E+00 0.0000000E+00 -84284.76     -21775.42     0.0000000E+00 311000\n" +
                        "  0.0000000E+00 0.0000000E+00 -627408.1     -215235.9     0.0000000E+00 221000\n" +
                        "  0.0000000E+00 0.0000000E+00 -229226.5     -85002.83     0.0000000E+00 131000\n" +
                        "  0.0000000E+00 0.0000000E+00  9246.518     -12602.01     0.0000000E+00 041000\n" +
                        "  -70829.55     -45012.93     0.0000000E+00 0.0000000E+00 -40249.15     302000\n" +
                        "  -137675.4     -54679.17     0.0000000E+00 0.0000000E+00 -19833.82     212000\n" +
                        "  -56148.12     -23311.42     0.0000000E+00 0.0000000E+00  22807.10     122000\n" +
                        "  -2797.627      6121.517     0.0000000E+00 0.0000000E+00  1573.743     032000\n" +
                        "  0.0000000E+00 0.0000000E+00  287145.7      64074.37     0.0000000E+00 203000\n" +
                        "  0.0000000E+00 0.0000000E+00  147868.1      52256.21     0.0000000E+00 113000\n" +
                        "  0.0000000E+00 0.0000000E+00 -8247.894     -10846.94     0.0000000E+00 023000\n" +
                        "  -9026.172     -4079.293     0.0000000E+00 0.0000000E+00 -10517.25     104000\n" +
                        "  -24407.28     -3324.691     0.0000000E+00 0.0000000E+00 -5867.502     014000\n" +
                        "  0.0000000E+00 0.0000000E+00  15450.02      2339.668     0.0000000E+00 005000\n" +
                        "  0.0000000E+00 0.0000000E+00  306535.9      68596.99     0.0000000E+00 400100\n" +
                        "  0.0000000E+00 0.0000000E+00 -267670.4     -106817.1     0.0000000E+00 310100\n" +
                        "  0.0000000E+00 0.0000000E+00 -801391.7     -311687.3     0.0000000E+00 220100\n" +
                        "  0.0000000E+00 0.0000000E+00 -127911.2     -96451.00     0.0000000E+00 130100\n" +
                        "  0.0000000E+00 0.0000000E+00  40861.54     -74.10140     0.0000000E+00 040100\n" +
                        "  -291955.1     -104206.6     0.0000000E+00 0.0000000E+00 -113109.4     301100\n" +
                        "  -251813.6     -90290.86     0.0000000E+00 0.0000000E+00  11120.17     211100\n" +
                        "  -99381.33     -39771.65     0.0000000E+00 0.0000000E+00  82419.27     121100\n" +
                        "   65512.38      3293.192     0.0000000E+00 0.0000000E+00  21465.65     031100\n" +
                        "  0.0000000E+00 0.0000000E+00  1123520.      316654.0     0.0000000E+00 202100\n" +
                        "  0.0000000E+00 0.0000000E+00  529079.7      76978.32     0.0000000E+00 112100\n" +
                        "  0.0000000E+00 0.0000000E+00 -58301.62     -14974.99     0.0000000E+00 022100\n" +
                        "  -11349.53      8543.084     0.0000000E+00 0.0000000E+00 -76571.19     103100\n" +
                        "  -41588.45     -5317.947     0.0000000E+00 0.0000000E+00 -26749.13     013100\n" +
                        "  0.0000000E+00 0.0000000E+00  106980.5      24463.60     0.0000000E+00 004100\n" +
                        "  -5336.301     -1937.846     0.0000000E+00 0.0000000E+00 -788.0005     400001\n" +
                        "  -2433.498     -1638.574     0.0000000E+00 0.0000000E+00 -2423.930     310001\n" +
                        "   17774.31     -3208.171     0.0000000E+00 0.0000000E+00 -4498.507     220001\n" +
                        "   7739.083      1985.754     0.0000000E+00 0.0000000E+00 -1761.103     130001\n" +
                        "  -581.2454     -226.0858     0.0000000E+00 0.0000000E+00  399.2428     040001\n" +
                        "  0.0000000E+00 0.0000000E+00 -12460.91     -44777.20     0.0000000E+00 301001\n" +
                        "  0.0000000E+00 0.0000000E+00 -17079.37     -3410.890     0.0000000E+00 211001\n" +
                        "  0.0000000E+00 0.0000000E+00 -56483.48     -33314.94     0.0000000E+00 121001\n" +
                        "  0.0000000E+00 0.0000000E+00 -7778.444      1257.977     0.0000000E+00 031001\n" +
                        "   36727.72      11905.48     0.0000000E+00 0.0000000E+00 -10379.18     202001\n" +
                        "  -31561.12      2759.028     0.0000000E+00 0.0000000E+00 -12605.11     112001\n" +
                        "  -2169.945     -8270.541     0.0000000E+00 0.0000000E+00  4032.727     022001\n" +
                        "  0.0000000E+00 0.0000000E+00  57244.09     -6460.777     0.0000000E+00 103001\n" +
                        "  0.0000000E+00 0.0000000E+00  6204.181      5778.606     0.0000000E+00 013001\n" +
                        "   29659.89      7355.321     0.0000000E+00 0.0000000E+00  573.2177     004001\n" +
                        "   242266.7      96798.47     0.0000000E+00 0.0000000E+00  27341.40     300200\n" +
                        "   289732.8      110017.0     0.0000000E+00 0.0000000E+00  118743.9     210200\n" +
                        "   300316.8      83077.11     0.0000000E+00 0.0000000E+00  99422.93     120200\n" +
                        "   147103.0      23205.48     0.0000000E+00 0.0000000E+00  25838.52     030200\n" +
                        "  0.0000000E+00 0.0000000E+00  864474.7      2056.166     0.0000000E+00 201200\n" +
                        "  0.0000000E+00 0.0000000E+00  392830.8      26954.16     0.0000000E+00 111200\n" +
                        "  0.0000000E+00 0.0000000E+00 -18062.69     -46250.89     0.0000000E+00 021200\n" +
                        "   4370.904      27399.71     0.0000000E+00 0.0000000E+00 -144172.4     102200\n" +
                        "  -183796.8     -29408.93     0.0000000E+00 0.0000000E+00 -45030.71     012200\n" +
                        "  0.0000000E+00 0.0000000E+00  208311.4      46963.18     0.0000000E+00 003200\n" +
                        "  0.0000000E+00 0.0000000E+00  80438.54     -8552.058     0.0000000E+00 300101\n" +
                        "  0.0000000E+00 0.0000000E+00  50973.00      12721.89     0.0000000E+00 210101\n" +
                        "  0.0000000E+00 0.0000000E+00  29699.10     -4538.471     0.0000000E+00 120101\n" +
                        "  0.0000000E+00 0.0000000E+00  18237.39      8884.724     0.0000000E+00 030101\n" +
                        "  -206979.2     -33331.69     0.0000000E+00 0.0000000E+00 -62832.53     201101\n" +
                        "  -144087.9     -25815.63     0.0000000E+00 0.0000000E+00 -25106.63     111101\n" +
                        "  -59006.49     -3793.364     0.0000000E+00 0.0000000E+00 -6365.207     021101\n" +
                        "  0.0000000E+00 0.0000000E+00 -7879.797      35492.71     0.0000000E+00 102101\n" +
                        "  0.0000000E+00 0.0000000E+00 -50206.54     -25345.32     0.0000000E+00 012101\n" +
                        "   90323.42      30535.53     0.0000000E+00 0.0000000E+00  2366.978     003101\n" +
                        "   646.7852     -538.0338     0.0000000E+00 0.0000000E+00 -75.58449     300002\n" +
                        "   1447.512      1040.836     0.0000000E+00 0.0000000E+00 -17.50040     210002\n" +
                        "  -433.6604     -714.5227     0.0000000E+00 0.0000000E+00  87.38510     120002\n" +
                        "   506.5041      959.4771     0.0000000E+00 0.0000000E+00 -72.38464     030002\n" +
                        "  0.0000000E+00 0.0000000E+00 -53433.01     -41192.95     0.0000000E+00 201002\n" +
                        "  0.0000000E+00 0.0000000E+00 -30405.47     -14878.46     0.0000000E+00 111002\n" +
                        "  0.0000000E+00 0.0000000E+00 -4174.701     -6478.601     0.0000000E+00 021002\n" +
                        "   21940.97      3246.063     0.0000000E+00 0.0000000E+00  5648.164     102002\n" +
                        "  -9381.249      1569.050     0.0000000E+00 0.0000000E+00 -2251.977     012002\n" +
                        "  0.0000000E+00 0.0000000E+00 -3759.400     -2753.914     0.0000000E+00 003002\n" +
                        "  0.0000000E+00 0.0000000E+00  688575.8      236080.3     0.0000000E+00 200300\n" +
                        "  0.0000000E+00 0.0000000E+00  611390.6      294179.0     0.0000000E+00 110300\n" +
                        "  0.0000000E+00 0.0000000E+00  142771.5      83551.90     0.0000000E+00 020300\n" +
                        "  -300152.2     -10059.57     0.0000000E+00 0.0000000E+00 -140636.0     101300\n" +
                        "  -245398.4     -60962.03     0.0000000E+00 0.0000000E+00 -28636.75     011300\n" +
                        "  0.0000000E+00 0.0000000E+00  102835.0     -5921.698     0.0000000E+00 002300\n" +
                        "   142566.8      46724.16     0.0000000E+00 0.0000000E+00  13184.94     200201\n" +
                        "   110538.9      39198.57     0.0000000E+00 0.0000000E+00  22997.54     110201\n" +
                        "   22293.51      17983.24     0.0000000E+00 0.0000000E+00  6026.949     020201\n" +
                        "  0.0000000E+00 0.0000000E+00 -313562.6     -200952.8     0.0000000E+00 101201\n" +
                        "  0.0000000E+00 0.0000000E+00 -96236.04     -106995.3     0.0000000E+00 011201\n" +
                        "   117831.0      34742.75     0.0000000E+00 0.0000000E+00  7078.894     002201\n" +
                        "  0.0000000E+00 0.0000000E+00 -142.2119     -17165.70     0.0000000E+00 200102\n" +
                        "  0.0000000E+00 0.0000000E+00  14970.26      4800.715     0.0000000E+00 110102\n" +
                        "  0.0000000E+00 0.0000000E+00  4387.072     -3724.454     0.0000000E+00 020102\n" +
                        "  -82884.39     -17869.51     0.0000000E+00 0.0000000E+00 -11379.56     101102\n" +
                        "  -39709.39     -12266.52     0.0000000E+00 0.0000000E+00 -5613.509     011102\n" +
                        "  0.0000000E+00 0.0000000E+00  2949.725      18654.95     0.0000000E+00 002102\n" +
                        "  -121.7068     -250.8208     0.0000000E+00 0.0000000E+00  64.53259     200003\n" +
                        "   286.1406      417.9042     0.0000000E+00 0.0000000E+00  77.88092     110003\n" +
                        "  -140.7532     -200.9574     0.0000000E+00 0.0000000E+00  61.49563     020003\n" +
                        "  0.0000000E+00 0.0000000E+00  180.6181     -4536.950     0.0000000E+00 101003\n" +
                        "  0.0000000E+00 0.0000000E+00  866.2154     -1269.306     0.0000000E+00 011003\n" +
                        "   4675.244      233.5219     0.0000000E+00 0.0000000E+00  992.7054     002003\n" +
                        "   189676.4      78695.58     0.0000000E+00 0.0000000E+00  1009.663     100400\n" +
                        "   39744.80     -10058.69     0.0000000E+00 0.0000000E+00  18072.88     010400\n" +
                        "  0.0000000E+00 0.0000000E+00 -38658.20     -114908.0     0.0000000E+00 001400\n" +
                        "  0.0000000E+00 0.0000000E+00 -45557.95     -87207.27     0.0000000E+00 100301\n" +
                        "  0.0000000E+00 0.0000000E+00  18397.02     -29817.60     0.0000000E+00 010301\n" +
                        "  -1293.872      19106.57     0.0000000E+00 0.0000000E+00 -12118.80     001301\n" +
                        "  -36095.39     -8000.585     0.0000000E+00 0.0000000E+00 -4833.766     100202\n" +
                        "  -6758.031     -3818.868     0.0000000E+00 0.0000000E+00 -684.9365     010202\n" +
                        "  0.0000000E+00 0.0000000E+00 -15254.81      13082.22     0.0000000E+00 001202\n" +
                        "  0.0000000E+00 0.0000000E+00 -1130.391     -4654.794     0.0000000E+00 100103\n" +
                        "  0.0000000E+00 0.0000000E+00 -450.8669     -278.4051     0.0000000E+00 010103\n" +
                        "   8754.202      2712.429     0.0000000E+00 0.0000000E+00 -775.6776     001103\n" +
                        "  -18.37063     -74.87388     0.0000000E+00 0.0000000E+00 -8.690181     100004\n" +
                        "   66.84911      40.45469     0.0000000E+00 0.0000000E+00 -13.76847     010004\n" +
                        "  0.0000000E+00 0.0000000E+00 -742.9964     -175.4774     0.0000000E+00 001004\n" +
                        "  0.0000000E+00 0.0000000E+00  1392.075     -40855.70     0.0000000E+00 000500\n" +
                        "  -32916.10      6299.795     0.0000000E+00 0.0000000E+00 -2330.820     000401\n" +
                        "  0.0000000E+00 0.0000000E+00 -4720.049     -4447.474     0.0000000E+00 000302\n" +
                        "  -1079.798      333.9335     0.0000000E+00 0.0000000E+00 -452.2590     000203\n" +
                        "  0.0000000E+00 0.0000000E+00 -252.5509     -301.8055     0.0000000E+00 000104\n" +
                        "  -5.728315     -5.054783     0.0000000E+00 0.0000000E+00  2.976489     000005"
        );

        PhaseSpaceParticle p = PhaseSpaceParticle.create(
                3.5 * MM, 7.5 * MRAD,
                3.5 * MM, 7.5 * MRAD,
                0, 9.0 * PRESENT
        );

        PhaseSpaceParticles.convertDeltaFromMomentumDispersionToEnergyDispersion(p, 250);

        PhaseSpaceParticle apply = map.apply(p);

        Logger.getLogger().info("apply = " + apply);

    }

    // ------------------- method ----------------
    private void phase相椭圆画图(
            double distance, boolean xPlane, double delta, int numberParticleForTrack,
            boolean moveToCenter, double scaleForParticle,
            MagnetAble magnetAble, Trajectory trajectory,
            int numberParticleForCosyMap, int cosyOrder,
            List<BaseUtils.Content.BiContent<String, CosyArbitraryOrder.CosyMapArbitraryOrder>> mapInfo
    ) {
        CosyArbitraryOrder.CosyMapArbitraryOrder[] maps = mapInfo.stream().map(BaseUtils.Content.
                BiContent::getT2).collect(Collectors.toList()).toArray(CosyArbitraryOrder.CosyMapArbitraryOrder[]::new);

        List<List<Point2>> lists = phase相椭圆研究(distance, xPlane, delta, numberParticleForTrack, moveToCenter,
                scaleForParticle, magnetAble, trajectory, numberParticleForCosyMap, cosyOrder, maps);

        List<Point2> list = lists.get(0);
        Plot2d.plot2(list, Plot2d.BLACK_POINT);

        for (int i = 1; i < lists.size(); i++) {
            List<Point2> list1 = lists.get(i);
            Plot2d.plot2(list1, switcher.getAndSwitch());
        }

        if (xPlane)
            Plot2d.info("x/mm", "xp/mrad", "x/xp dp=" + delta / PRESENT + "%, s=" + distance + "m", 18);
        else
            Plot2d.info("y/mm", "yp/mrad", "y/yp dp=" + delta / PRESENT + "%, s=" + distance + "m", 18);

        List<String> info = mapInfo.stream().map(BaseUtils.Content.BiContent::getT1).collect(Collectors.toList());
        info.add(0, "Track");

        Plot2d.legend(18, info.toArray(String[]::new));

        Plot2d.equal();

        Plot2d.showThread();
    }

    private void phase相椭圆画图(
            double distance, boolean xPlane, double delta, int numberParticleForTrack,
            boolean moveToCenter, double scaleForParticle,
            List<BaseUtils.Content.BiContent<String, MagnetAble>> magnetAblenfo, Trajectory trajectory,
            int numberParticleForCosyMap, int cosyOrder,
            List<BaseUtils.Content.BiContent<String, CosyArbitraryOrder.CosyMapArbitraryOrder>> mapInfo, List<String> describes
    ) {
        List<CosyArbitraryOrder.CosyMapArbitraryOrder> maps = mapInfo.stream().map(BaseUtils.Content.
                BiContent::getT2).collect(Collectors.toList());

        List<MagnetAble> magnetAbles = magnetAblenfo.stream().map(BaseUtils.Content.BiContent::getT2).collect(Collectors.toList());

        List<List<Point2>> lists = phase相椭圆研究(distance, xPlane, delta, numberParticleForTrack, moveToCenter,
                scaleForParticle, magnetAbles, trajectory, numberParticleForCosyMap, cosyOrder, maps);

        for (int i = 0; i < lists.size(); i++) {
            List<Point2> list1 = lists.get(i);
            Plot2d.plot2circle(list1, describes.get(i));
        }

        if (xPlane)
            Plot2d.info("x/mm", "xp/mrad", "x/xp dp=" + delta / PRESENT + "%, s=" + distance + "m", 18);
        else
            Plot2d.info("y/mm", "yp/mrad", "y/yp dp=" + delta / PRESENT + "%, s=" + distance + "m", 18);

        Stream<String> trakcinfo = magnetAblenfo.stream().map(BaseUtils.Content.BiContent::getT1).collect(Collectors.toList()).stream();
        Stream<String> mapinfo = mapInfo.stream().map(BaseUtils.Content.BiContent::getT1).collect(Collectors.toList()).stream();

        String[] le = Stream.concat(trakcinfo, mapinfo).collect(Collectors.toList()).toArray(String[]::new);

        Plot2d.legend(18, le);

        Plot2d.equal();

        Plot2d.showThread();
    }

    private List<List<Point2>> phase相椭圆研究(
            double distance, boolean xPlane, double delta, int numberParticleForTrack,
            boolean moveToCenter, double scaleForParticle,
            MagnetAble magnetAble, Trajectory trajectory,
            int numberParticleForCosyMap, int cosyOrder, CosyArbitraryOrder.CosyMapArbitraryOrder... maps
    ) {
        List<List<Point2>> ret = new ArrayList<>(1 + maps.length);

        ret.add(
                tracking相椭圆(distance, xPlane, delta, numberParticleForTrack, moveToCenter, scaleForParticle, magnetAble, trajectory)
        );

        for (CosyArbitraryOrder.CosyMapArbitraryOrder map : maps) {
            ret.add(
                    cosy相椭圆(xPlane, delta, numberParticleForCosyMap, cosyOrder, moveToCenter, scaleForParticle, map)
            );
        }

        return ret;
    }

    private List<List<Point2>> phase相椭圆研究(
            double distance, boolean xPlane, double delta, int numberParticleForTrack,
            boolean moveToCenter, double scaleForParticle,
            List<MagnetAble> magnetAbles, Trajectory trajectory,
            int numberParticleForCosyMap, int cosyOrder, List<CosyArbitraryOrder.CosyMapArbitraryOrder> maps
    ) {
        List<List<Point2>> ret = new ArrayList<>(magnetAbles.size() + maps.size());


        for (MagnetAble magnetAble : magnetAbles) {
            ret.add(
                    tracking相椭圆(distance, xPlane, delta, numberParticleForTrack, moveToCenter, scaleForParticle, magnetAble, trajectory)
            );
        }


        for (CosyArbitraryOrder.CosyMapArbitraryOrder map : maps) {
            ret.add(
                    cosy相椭圆(xPlane, delta, numberParticleForCosyMap, cosyOrder, moveToCenter, scaleForParticle, map)
            );
        }

        return ret;
    }


    private List<Point2> cosy相椭圆(
            boolean xPlane, double delta, int number, int order,
            boolean moveToCenter, double scaleForParticle,
            CosyArbitraryOrder.CosyMapArbitraryOrder map
    ) {

        /*----------------COSY-----------------*/

        // 相空间点
        List<PhaseSpaceParticle> pp = PhaseSpaceParticles.phaseSpaceParticlesAlongPositiveEllipseInPlane(
                xPlane, 3.5 * MM * scaleForParticle, 7.5 * MM * scaleForParticle, delta, number);

        // 转为COSY能量分散
        pp = PhaseSpaceParticles.convertDeltaFromMomentumDispersionToEnergyDispersion(pp, 250);

        // 运行
        List<PhaseSpaceParticle> ppEnd = map.apply(pp, order);

        // 投影
        List<Point2> projectionToPlaneCOSY = PhaseSpaceParticles.projectionToPlane(xPlane, ppEnd);

        if (moveToCenter) {
            Vector2 average = Point2.average(projectionToPlaneCOSY).toVector2().reverseSelf();

            projectionToPlaneCOSY.forEach(point2 -> point2.moveSelf(average));
        }

        // 改单位
        return Point2.convert(projectionToPlaneCOSY, 1 / MM, 1 / MRAD);
    }

    private List<Point2> tracking相椭圆(
            double distance, boolean xPlane, double delta, int number,
            boolean moveToCenter, double scaleForParticle,
            MagnetAble magnetAble, Trajectory trajectory) {

        RunningParticle ip = ParticleFactory.createIdealProtonAtTrajectory250MeV(trajectory);

        List<PhaseSpaceParticle> pp = PhaseSpaceParticles.phaseSpaceParticlesAlongPositiveEllipseInPlane(
                xPlane, 3.5 * MM * scaleForParticle, 7.5 * MM * scaleForParticle, delta, number);

        List<RunningParticle> ps = ParticleFactory.createParticlesFromPhaseSpaceParticle(ip, ip.computeNaturalCoordinateSystem(), pp);

        if (distance > MM) {
            ParticleRunner.runThread(ps, magnetAble, distance, MM);
            //ParticleRunner.run(ip, magnetAble, distance, MM);
        }

        RunningParticle ipEnd = ParticleFactory.createIdealProtonAtTrajectory250MeV(trajectory, distance);

        List<PhaseSpaceParticle> ppEnd = PhaseSpaceParticles.phaseSpaceParticlesFromRunningParticles(
                ipEnd, ipEnd.computeNaturalCoordinateSystem(), ps);

        List<Point2> projectionToPlaneCOSY = PhaseSpaceParticles.projectionToPlane(xPlane, ppEnd);

        if (moveToCenter) {
            Vector2 average = Point2.average(projectionToPlaneCOSY).toVector2().reverseSelf();

            projectionToPlaneCOSY.forEach(point2 -> point2.moveSelf(average));
        }

        // 改单位
        return Point2.convert(projectionToPlaneCOSY, 1 / MM, 1 / MRAD);
    }


    private List<Point2> trackingIdealParticle(
            Line2 trajectory, double distance, MagnetAble magnetAble, boolean xPlane) {
        RunningParticle ip = ParticleFactory.createIdealProtonAtTrajectory250MeV(trajectory);

        return ParticleRunner.runGetPoint3WithDistance(ip, magnetAble, distance, MM)
                .stream()
                .map(point3WithDistance -> {

                    if (xPlane) {
                        double d = point3WithDistance.getDistance();

                        Point2 p = point3WithDistance.getPoint3().toPoint2();

                        Point2 o = trajectory.pointAt(d);
                        Vector2 x = trajectory.directAt(d).rotateSelf(Math.PI / 2);

                        return Point2.create(d, Vector2.from(o).to(p).dot(x));


                    } else {
                        return point3WithDistance.getDistanceWithZ();
                    }
                }).collect(Collectors.toList());
    }

    private List<Point3> trackingIdealParticle(Line2 trajectory, double distance, MagnetAble magnetAble) {
        RunningParticle ip = ParticleFactory.createIdealProtonAtTrajectory250MeV(trajectory);
        return ParticleRunner.run(ip, magnetAble, distance, MM);
    }

    private List<List<Point2>> track多粒子(int number, double delta, Trajectory trajectory, double distance,
                                        MagnetAble magnetAble, boolean xPlane) {
        RunningParticle ip = ParticleFactory.createIdealProtonAtTrajectory250MeV(trajectory);

//        List<PhaseSpaceParticle> pp = PhaseSpaceParticles.phaseSpaceParticlesAlongPositiveEllipseInPlane(
//                xPlane, 3.5 * MM, 7.5 * MM, delta, number);
//        List<RunningParticle> ps = ParticleFactory.createParticlesFromPhaseSpaceParticle(ip, ip.computeNaturalCoordinateSystem(), pp);


        List<RunningParticle> ps = Arrays.stream(SR.sphericalUniformDistribution(number)).map(point3 -> {

            PhaseSpaceParticle psp = PhaseSpaceParticle.create(
                    xPlane ? 3.5 * MM * point3.x : 0,
                    xPlane ? 7.5 * MM * point3.y : 0,
                    xPlane ? 0 : 3.5 * MM * point3.x,
                    xPlane ? 0 : 7.5 * MM * point3.y,
                    0,
                    delta * point3.z
            );

            return ParticleFactory.createParticleFromPhaseSpaceParticle(ip, ip.computeNaturalCoordinateSystem(), psp);
        }).collect(Collectors.toList());


        return ps.stream().parallel().map(p ->
                ParticleRunner.runGetPoint3WithDistance(p, magnetAble, distance, MM)
                        .stream()
                        .map(point3WithDistance -> {
                            if (xPlane) {
                                double d = point3WithDistance.getDistance();

                                Point2 p2 = point3WithDistance.getPoint3().toPoint2();

                                Point2 o = trajectory.pointAt(d);
                                Vector2 x = trajectory.directAt(d).rotateSelf(Math.PI / 2);

                                return Point2.create(d, Vector2.from(o).to(p2).dot(x));
                            } else {
                                return point3WithDistance.getDistanceWithZ();
                            }
                        })
                        .collect(Collectors.toList())
        ).collect(Collectors.toList());
    }

    // 单位 mm/%
    private List<Point2> track色散(Trajectory trajectory, MagnetAble magnetAble) {
        double length = trajectory.getLength() + 0.1;

        List<RunningParticle> particles = BaseUtils.Python.linspaceStream(-5 * PRESENT, 5 * PRESENT, 2)
                .mapToObj(delta -> {
                    double k = Protons.getKineticEnergy_MeV_AfterMomentumDispersion(250., delta);
                    RunningParticle ipk = ParticleFactory.createIdealProtonAtTrajectory(trajectory, k);
                    return ipk;
                })
                .collect(Collectors.toList());

        PolynomialFitter fitter = PolynomialFitter.build(1);

        List<Point2> distance_R16 = IntStream.range(0, (int) (length / MM) + 1)
                .mapToObj(i -> {
                    double distance = particles.get(0).getDistance();
                    RunningParticle ip = ParticleFactory.createIdealProtonAtTrajectory250MeV(trajectory, distance);
                    List<Point2> delta_x = particles
                            .stream()
                            .map(p -> PhaseSpaceParticles.phaseSpaceParticleFromRunningParticle(ip, ip.computeNaturalCoordinateSystem(), p))
                            .map(pp -> Point2.create(pp.getDelta(), pp.getX()))
                            .sorted(Comparator.comparingDouble(Point2::getX))
                            .collect(Collectors.toList());
                    double[] fit = fitter.fit(delta_x);


                    particles.forEach(p -> p.runSelf(magnetAble.magnetAt(p.position), MM));

                    return Point2.create(distance, fit[1]);
                })
                .map(p -> Point2.convert(p, 1, 1 / MM * PRESENT))
                .collect(Collectors.toList());

        return distance_R16;
    }

    // 单位 mm/%
    private List<Point2> cosy色散(List<BaseUtils.Content.BiContent<Double,
            CosyArbitraryOrder.CosyMapArbitraryOrder>> distance_cosyMap) {
        return distance_cosyMap.stream()
                .map(bi -> {
                    Double d = bi.getT1();
                    CosyArbitraryOrder.CosyMapArbitraryOrder m = bi.getT2();

                    double r16Cosy = m.getR(1, 6);

                    double r16Trans = COSY.convertR16InCosyToTransportPronTonOnly(r16Cosy, 250);

                    return Point2.create(d, r16Trans);
                })
                .collect(Collectors.toList());
    }


    /**
     * 神方法
     *
     * @param dependentVariable   应变量
     * @param independentVariable 自变量
     * @param xMin                自变量最小值
     * @param xMax                自变量最大值
     * @param number              分点数目
     * @param trajectory          轨迹
     * @param distance            粒子运动距离
     * @param magnetAble          磁场
     * @return 函数关系 y=f(x)
     */
    private List<Point2> trackFunction(
            PhaseSpaceParticle.VARIABLE dependentVariable,
            PhaseSpaceParticle.VARIABLE independentVariable,
            double xMin, double xMax, int number,
            Trajectory trajectory,
            double distance,
            MagnetAble magnetAble
    ) {
        RunningParticle ip = ParticleFactory.createIdealProtonAtTrajectory250MeV(trajectory);
        RunningParticle ipEnd = ParticleFactory.createIdealProtonAtTrajectory250MeV(trajectory, distance);
        PhaseSpaceParticle origin = PhaseSpaceParticle.origin();

        return BaseUtils.Python.linspaceStream(xMin, xMax, number)
                .mapToObj(x -> {
                    PhaseSpaceParticle p = origin.copy();
                    p.set(independentVariable, x);
                    RunningParticle pp = ParticleFactory.createParticleFromPhaseSpaceParticle(ip,
                            ip.computeNaturalCoordinateSystem(), p);
                    return BaseUtils.Content.BiContent.create(x, pp);
                })
                .collect(Collectors.toList())
                .stream().parallel()
                .map(bi -> {
                    Double x = bi.getT1();
                    RunningParticle p = bi.getT2();
                    ParticleRunner.run(p, magnetAble, distance, MM);
                    PhaseSpaceParticle ppEnd = PhaseSpaceParticles.phaseSpaceParticleFromRunningParticle(
                            ipEnd, ipEnd.computeNaturalCoordinateSystem(), p);
                    double y = ppEnd.get(dependentVariable);
                    return Point2.create(x, y);
                })
                .collect(Collectors.toList())
                .stream()
                .sorted(Point2::compareTo)
                .collect(Collectors.toList());
    }

    private List<Point2> mm2mmm(List<Point2> mm) {
        return Point2.convert(mm, 1, 1 / MM);
    }

    // ---------- element --------------

    private static Trajectory getTrajectoryPart2() {
        Trajectory trajectoryPart1 = getTrajectoryPart1();

        return TrajectoryFactory.setStartingPoint(trajectoryPart1.pointAtEnd())
                .setStraightLine(DL2, trajectoryPart1.directAtEnd())
                .addArcLine(trajectoryBigRPart2, true, dipoleCct345Angle)
                .addStraitLine(GAP3 + QS3_LEN + GAP3)
                .addArcLine(trajectoryBigRPart2, true, dipoleCct345Angle)
                .addStraitLine(DL2);
    }

    private static Trajectory getTrajectoryPart1() {
        return TrajectoryFactory.setStartingPoint(Point2.origin())
                .setStraightLine(DL1, Vector2.xDirect())
                .addArcLine(trajectoryBigRPart1, false, dipoleCct12Angle)
                .addStraitLine(GAP1)//gap1
                .addStraitLine(QS1_LEN)//QS1_LEN
                .addStraitLine(GAP2)//GAP2
                .addStraitLine(QS2_LEN)//QS2_LEN
                .addStraitLine(GAP2)//GAP2
                .addStraitLine(QS1_LEN)//QS1_LEN
                .addStraitLine(GAP1)//gap1
                .addArcLine(trajectoryBigRPart1, false, dipoleCct12Angle)
                .addStraitLine(DL1);

    }

    private static Trajectory getTestTrajectory(double BigR, double angle, double driftLength) {
        return TrajectoryFactory.setStartingPoint(BigR, -driftLength)
                .setStraightLine(driftLength, Vector2.yDirect())
                .addArcLine(BigR, false, angle)
                .addStraitLine(driftLength);
    }

    private Cct getCct12_1() {
        Cct agCct12 = createAgCct12();
        Cct dipoleCct12 = createDipoleCct12();

        Cct cct = CctFactory.combineCct(agCct12, dipoleCct12);

        return CctFactory.positionInXYPlane(cct, Point2.create(DL1, trajectoryBigRPart1), BaseUtils.Converter.angleToRadian(-90));
    }

    private Cct getCct12_2() {
        Cct cct12_1 = getCct12_1();

        Trajectory trajectoryPart1 = getTrajectoryPart1();
        return CctFactory.symmetryInXYPlaneByLine(
                cct12_1,
                trajectoryPart1.pointAt(DL1 + CCT12_LENGTH + GAP1 + QS1_LEN + GAP2 + QS2_LEN / 2),
                trajectoryPart1.directAt(DL1 + CCT12_LENGTH + GAP1 + QS1_LEN + GAP2 + QS2_LEN / 2).rotateSelf(Math.PI / 2)
        );
    }

    private Cct getCct345_1() {
        Trajectory trajectoryPart2 = getTrajectoryPart2();

        Point2 p1 = trajectoryPart2.pointAt(DL2);
        Point2 p2 = trajectoryPart2.pointAt(DL2 + CCT345_LENGTH / 2);
        Point2 p3 = trajectoryPart2.pointAt(DL2 + CCT345_LENGTH);

        Point2 center3 = Arcs.center(p1, p2, p3);

        Cct dipoleCct345 = createDipoleCct345();
        Cct agCct345 = createAgCct345();


        dipoleCct345 = CctFactory.positionInXYPlane(dipoleCct345, center3, BaseUtils.Converter.angleToRadian(180 - dipoleCct345Angle - 45));
        agCct345 = CctFactory.positionInXYPlane(agCct345, center3, BaseUtils.Converter.angleToRadian(180 - dipoleCct345Angle - 45));


        return CctFactory.combineCct(dipoleCct345, agCct345);
    }

    private Cct getCct345_2() {
        Trajectory trajectoryPart2 = getTrajectoryPart2();

        Point2 p = trajectoryPart2.pointAt(DL2 + CCT345_LENGTH + GAP3 + QS3_LEN / 2);
        Vector2 d = trajectoryPart2.directAt(DL2 + CCT345_LENGTH + GAP3 + QS3_LEN / 2).rotateSelf(Math.PI / 2);

        Cct cct345_1 = getCct345_1();

        return CctFactory.symmetryInXYPlaneByLine(cct345_1, p, d);
    }

    private Elements getElementsOfAllPart1() {
        List<QsHardPlaneMagnet> qs = getQs12();
        Cct allCctIn45 = CctFactory.combineCct(getCct12_1(), getCct12_2());

        Elements elements = Elements.empty();
        qs.forEach(elements::addElement);
        allCctIn45.getSoleLayerCctList().forEach(elements::addElement);

        return elements;
    }

    private MagnetAble getElementsOfAllPart2() {
        Trajectory trajectoryPart2 = getTrajectoryPart2();

        Cct cct345_1 = getCct345_1();
        Cct cct345_2 = getCct345_2();

        QsHardPlaneMagnet QS3 = getQs3();

        Elements elements = Elements.empty();

        elements.addElement(cct345_1);
        elements.addElement(cct345_2);
        elements.addElement(QS3);

        return elements;
    }

    private List<QsHardPlaneMagnet> getQs12() {
        Trajectory trajectoryPart1 = getTrajectoryPart1();

        QsHardPlaneMagnet QS11 = QsHardPlaneMagnet.create(QS1_LEN, QS1_GRADIENT, QS1_SECOND_GRADIENT, QS1_APERTURE_MM,
                trajectoryPart1.pointAt(DL1 + CCT12_LENGTH + GAP1), trajectoryPart1.directAt(DL1 + CCT12_LENGTH + GAP1));

        QsHardPlaneMagnet QS2 = QsHardPlaneMagnet.create(QS2_LEN, QS2_GRADIENT, QS2_SECOND_GRADIENT, QS2_APERTURE_MM,
                trajectoryPart1.pointAt(DL1 + CCT12_LENGTH + GAP1 + QS1_LEN + GAP2),
                trajectoryPart1.directAt(DL1 + CCT12_LENGTH + GAP1 + QS1_LEN + GAP2));


        QsHardPlaneMagnet QS12 = QsHardPlaneMagnet.create(QS1_LEN, QS1_GRADIENT, QS1_SECOND_GRADIENT, QS1_APERTURE_MM,
                trajectoryPart1.pointAt(DL1 + CCT12_LENGTH + GAP1 + QS1_LEN + GAP2 + QS2_LEN + GAP2),
                trajectoryPart1.directAt(DL1 + CCT12_LENGTH + GAP1 + QS1_LEN + GAP2 + QS2_LEN + GAP2));

        return List.of(QS11, QS2, QS12);
    }

    private QsHardPlaneMagnet getQs3() {
        Trajectory trajectoryPart2 = getTrajectoryPart2();

        return QsHardPlaneMagnet.create(QS3_LEN, QS3_GRADIENT, QS3_SECOND_GRADIENT, QS3_APERTURE_MM,
                trajectoryPart2.pointAt(DL2 + CCT345_LENGTH + GAP3),
                trajectoryPart2.directAt(DL2 + CCT345_LENGTH + GAP3));
    }

    private Cct createDipoleCct345() {
        return createDipoleCct(
                dipoleCct345SmallRInner, dipoleCct345SmallROuter, dipoleCct345BigR,
                dipoleCct345Angle, dipoleCct345WindingNumber,
                dipoleCct345A0Inner, dipoleCct345A1Inner, dipoleCct345A2Inner, dipoleCct345IInner,
                dipoleCct345A0Outer, dipoleCct345A1Outer, dipoleCct345A2Outer, dipoleCct345IOuter,
                numberPerWinding,
                dipoleCct345StartingθInner, dipoleCct345StartingθOuter,
                dipoleCct345StartingφInner, dipoleCct345StartingφOuter,
                dipoleCct345DirectθInner, dipoleCct345DirectθOuter
        );
    }

    private Cct createDipoleCct12() {
        return createDipoleCct(
                dipoleCct12SmallRInner, dipoleCct12SmallROuter, dipoleCct12BigR,
                dipoleCct12Angle, dipoleCct12WindingNumber,
                dipoleCct12A0Inner, dipoleCct12A1Inner, dipoleCct12A2Inner, dipoleCct12IInner,
                dipoleCct12A0Outer, dipoleCct12A1Outer, dipoleCct12A2Outer, dipoleCct12IOuter,
                numberPerWinding,
                dipoleCct12StartingθInner, dipoleCct12StartingθOuter,
                dipoleCct12StartingφInner, dipoleCct12StartingφOuter,
                dipoleCct12DirectθInner, dipoleCct12DirectθOuter
        );
    }

    private Cct createAgCct12() {
        return createAgCct(
                agCct12SmallRInner, agCct12SmallROuter, agCct12BigR,
                new double[]{agCctAngle1, agCctAngle2},
                new int[]{agCctWindingNumber1, agCctWindingNumber2},
                agCct12A0Inner, agCct12A1Inner, agCct12A2Inner, agCct12IInner,
                agCct12A0Outer, agCct12A1Outer, agCct12A2Outer, agCct12IOuter,
                numberPerWinding,
                agCct12StartingθInner, agCct12StartingθOuter,
                agCct12StartingφInner, agCct12StartingφOuter,
                agCct12DirectθInner, agCct12DirectθOuter
        );
    }

    private Cct createAgCct345() {
        return createAgCct(
                agCct345SmallRInner, agCct345SmallROuter, agCct345BigR,
                new double[]{agCctAngle5, agCctAngle4, agCctAngle3},
                new int[]{agCctWindingNumber5, agCctWindingNumber4, agCctWindingNumber3},
                agCct345A0Inners, agCct345A1Inners, agCct345A2Inners, agCct345IInner,
                agCct345A0Outers, agCct345A1Outers, agCct345A2Outers, agCct345IOuter,
                numberPerWinding,
                agCct345StartingθInner, agCct345StartingθOuter,
                agCct345StartingφInner, agCct345StartingφOuter,
                agCct345DirectθInner, agCct345DirectθOuter
        );
    }

    private Cct createDipoleCct(
            double smallRInner, double smallROuter, double bigR, double angle, int windingNumber,
            double a0BipolarInner, double a1QuadrupleInner, double a2SextupleInner, double IInner,
            double a0BipolarOuter, double a1QuadrupleOuter, double a2SextupleOuter, double IOuter,
            int numberPerWinding,
            double startingθInner, double startingθOuter,
            double startingφInner, double startingφOuter,
            boolean directθInner, boolean directθOuter
    ) {
        return CctFactory.createDipoleCctDetailed(
                smallRInner, smallROuter, bigR, angle, windingNumber,
                a0BipolarInner, a1QuadrupleInner, a2SextupleInner, IInner,
                a0BipolarOuter, a1QuadrupleOuter, a2SextupleOuter, IOuter,
                numberPerWinding,
                startingθInner, startingθOuter,
                startingφInner, startingφOuter,
                directθInner, directθOuter
        );
    }

    private Cct createAgCct(
            double smallRInner, double smallROuter, double bigR, double[] angles, int[] windingNumbers,
            double a0BipolarInners, double a1QuadrupleInners, double a2SextupleInners, double IInner,
            double a0BipolarOuters, double a1QuadrupleOuters, double a2SextupleOuters, double IOuter,
            int numberPerWinding,
            double startingθInner, double startingθOuter,
            double startingφInner, double startingφOuter,
            boolean directθInner, boolean directθOuter
    ) {
//        double smallRInner, double smallROuter, double bigR, double[] angles, int[] windingNumbers,
//        double a0BipolarInners, double a1QuadrupleInners, double a2SextupleInners, double IInner,
//        double a0BipolarOuters, double a1QuadrupleOuters, double a2SextupleOuters, double IOuter,
//        int numberPerWinding,
//        double startingθInner, double startingθOuter,
//        double startingφInner, double startingφOuter,
//        boolean directθInner, boolean directθOuter
        return CctFactory.createAgCct(
                smallRInner, smallROuter, bigR, angles, windingNumbers,
                a0BipolarInners, a1QuadrupleInners, a2SextupleInners, IInner,
                a0BipolarOuters, a1QuadrupleOuters, a2SextupleOuters, IOuter,
                numberPerWinding,
                startingθInner, startingθOuter,
                startingφInner, startingφOuter,
                directθInner, directθOuter
        );
    }

    private Cct createAgCct(
            double smallRInner, double smallROuter, double bigR, double[] angles, int[] windingNumbers,
            double[] a0BipolarInners, double[] a1QuadrupleInners, double[] a2SextupleInners, double IInner,
            double[] a0BipolarOuters, double[] a1QuadrupleOuters, double[] a2SextupleOuters, double IOuter,
            int numberPerWinding,
            double startingθInner, double startingθOuter,
            double startingφInner, double startingφOuter,
            boolean directθInner, boolean directθOuter
    ) {
        return CctFactory.createAgCct(
                smallRInner, smallROuter, bigR, angles, windingNumbers,
                a0BipolarInners, a1QuadrupleInners, a2SextupleInners, IInner,
                a0BipolarOuters, a1QuadrupleOuters, a2SextupleOuters, IOuter,
                numberPerWinding,
                startingθInner, startingθOuter,
                startingφInner, startingφOuter,
                directθInner, directθOuter
        );
    }

    //----------cosy------------------------
    enum COSY_MAP {
        optics前偏转段;

        CosyArbitraryOrder.CosyMapArbitraryOrder map;

        static {
            /* COSY 脚本
            {cosy script for HUST SC Gantry DownStream, B. Qin, 2019-12-25}

INCLUDE 'COSY' ;
PROCEDURE RUN ;

VARIABLE I 1 ; {number of iteration} VARIABLE refresh 1 ; {print picture per refresh}
VARIABLE DL1 1 ; VARIABLE DL2 1 ;
VARIABLE CCT12_APER 1 ; VARIABLE CCT345_APER 1 ; {half aperture}
VARIABLE CCT1_ANG 1 ; VARIABLE CCT2_ANG 1 ; VARIABLE CCT3_ANG 1 ;
VARIABLE CCT12_ANG_MIN 1 ; VARIABLE CCT345_ANG_MIN 1 ;
VARIABLE CCT4_ANG 1 ; VARIABLE CCT5_ANG 1 ;{angle}
VARIABLE CCT12_n 1 ; VARIABLE CCT345_n 1 ;
VARIABLE CCT12_R 1 ; VARIABLE CCT345_R 1 ;
VARIABLE QS1_LEN 1 ; VARIABLE QS1_Q 1 ; VARIABLE QS1_S 1 ; VARIABLE QS1_APER 1 ;
VARIABLE QS2_LEN 1 ; VARIABLE QS2_Q 1 ; VARIABLE QS2_S 1 ; VARIABLE QS2_APER 1 ;
VARIABLE QS3_LEN 1 ; VARIABLE QS3_Q 1 ; VARIABLE QS3_S 1 ; VARIABLE QS3_APER 1 ;
VARIABLE GAP1 1 ; VARIABLE GAP2 1 ; VARIABLE GAP3 1 ;

VARIABLE OBJ_ANG45 1 ; VARIABLE OBJ_ANG135 1 ;
VARIABLE OBJ_R16_1 1 ; VARIABLE OBJ_R16_2 1;
VARIABLE OBJ_R26_1 1 ; VARIABLE OBJ_R26_2 1;
VARIABLE OBJ_R11_1 1 ; VARIABLE OBJ_R11_2 1;
VARIABLE OBJ_R12_1 1 ; VARIABLE OBJ_R12_2 1;
VARIABLE OBJ_R21_1 1 ; VARIABLE OBJ_R21_2 1;
VARIABLE OBJ_R33_1 1 ; VARIABLE OBJ_R33_2 1;
VARIABLE OBJ_R34_1 1 ; VARIABLE OBJ_R34_2 1;
VARIABLE OBJ_R43_1 1 ; VARIABLE OBJ_R43_2 1;
VARIABLE OBJ_T126_1 1 ; VARIABLE OBJ_T126_2 1;
VARIABLE OBJ_T226_1 1 ; VARIABLE OBJ_T226_2 1;
VARIABLE OBJ_T346_1 1 ; VARIABLE OBJ_T346_2 1;
VARIABLE OBJ_T446_1 1 ; VARIABLE OBJ_T446_2 1;
VARIABLE OBJ_SIGMA11_1 1 ; VARIABLE OBJ_SIGMA11_2 1 ;
VARIABLE OBJ_SIGMA33_1 1 ; VARIABLE OBJ_SIGMA33_2 1 ;
VARIABLE OBJ_ALIGN 1 ;


FUNCTION SQUA X ; SQUA := X * X ; ENDFUNCTION ;


I := 0 ; {number of iteration}
refresh := 1 ; {print picture per refresh}

{--------------PARAMETER SET--------------------}
CCT12_R := 0.75 ;
CCT345_R := 0.75 ;

CCT12_APER := 0.020 ;
CCT345_APER := 0.060 ;
QS1_APER := 0.030 ;
QS2_APER := 0.030 ;
QS3_APER := 0.060 ;

DL1 := 1.1759 ;
DL2 := 2.40 ;
QS1_LEN := 0.2;
QS2_LEN := 0.2;
QS3_LEN := 0.2;
GAP1 := 0.15 ;
GAP2 := 0.15 ;
GAP3:=0.2585751675857585;

CCT12_ANG_MIN := 5 ;
CCT345_ANG_MIN := 8 ;

CCT1_ANG:= 4.116696651312341;
CCT2_ANG:= 8.383304669429050;
CCT3_ANG:=0.3926893224425800E-005;
CCT4_ANG:= 21.99845340255313;
CCT5_ANG:= 21.50155751013952;

CCT12_n:=-13.63885007227421;
CCT345_n:=-4.216451945875646;

QS1_Q:= 1.596245173043834;
QS2_Q:=-1.133857472311528;
QS3_Q:=0.4569;

QS1_S:=-0.1924;
QS2_S:=0.6062;
QS3_S:=0.1470;

{--------------PARAMETER END--------------------}

{ FIT {GAP3} {CCT1_ANG CCT2_ANG CCT12_n QS1_Q QS2_Q} QS2_S {CCT3_ANG CCT4_ANG CCT5_ANG CCT345_n QS3_Q} QS3_S QS1_S; }

I := I+1 ; IF I-INT(I/refresh)*refresh=0; WRITE 6 I ; ENDIF ;

OBJ_ANG45 := (ABS(CCT1_ANG)+CCT12_ANG_MIN + ABS(CCT2_ANG))+CCT12_ANG_MIN - 22.5 ;
OBJ_ANG135 := (ABS(CCT3_ANG)+CCT345_ANG_MIN + ABS(CCT4_ANG)+CCT345_ANG_MIN + ABS(CCT5_ANG)+CCT345_ANG_MIN) - 67.5 ;

OV 5 3 0 ; {order 1, phase space MSm 3, # of parameters 0}
PTY 0.0 ; {picture type 0.0 for straight-line and nonzore for lab}
RPP 250 ; {particle type = proton, kinetic energy = 250MeV}
SB 3.5E-3 7.5E-3 0    3.5E-3 7.5E-3 0    0 0 0 0 0;
{<x><x'><r12>       <y><y'><r34>     <t'><dE/E><r56><dm/m><z'>}
{x=2.0mm x'=3.0mr y=2.0mm y'=3.0mm z=0.0mm DP/p=5.0% momentum=729.134GeV/c}
CR ;

ENVEL ;

UM ;    {sets map to unity} BP ;

{-------------------------BEAMLINE START-------------------------}
{----FIRST BENDING----}
DL ABS(DL1) ;

CB ; MS CCT12_R ABS(CCT1_ANG)+CCT12_ANG_MIN CCT12_APER CCT12_n 0 0 0 0; CB ;
CB ; MS CCT12_R ABS(CCT2_ANG)+CCT12_ANG_MIN CCT12_APER -CCT12_n 0 0 0 0; CB ;
{ MS CCT12_R ABS(CCT1_ANG)+CCT12_ANG_MIN CCT12_APER CCT12_n 0 0 0 0; }
{ MS CCT12_R ABS(CCT2_ANG)+CCT12_ANG_MIN CCT12_APER -CCT12_n 0 0 0 0; }

{ PM 6; }

DL ABS(GAP1);

M5 ABS(QS1_LEN) QS1_Q QS1_S 0 0 0 QS1_APER;
DL ABS(GAP2);
M5 ABS(QS2_LEN) QS2_Q QS2_S 0 0 0 QS2_APER;
DL ABS(GAP2);
M5 ABS(QS1_LEN) QS1_Q QS1_S 0 0 0 QS1_APER;

DL ABS(GAP1);
CB ; MS CCT12_R ABS(CCT2_ANG)+CCT12_ANG_MIN CCT12_APER -CCT12_n 0 0 0 0; CB ;
CB ; MS CCT12_R ABS(CCT1_ANG)+CCT12_ANG_MIN CCT12_APER CCT12_n 0 0 0 0; CB ;
{ MS CCT12_R ABS(CCT2_ANG)+CCT12_ANG_MIN CCT12_APER -CCT12_n 0 0 0 0; }
{ MS CCT12_R ABS(CCT1_ANG)+CCT12_ANG_MIN CCT12_APER CCT12_n 0 0 0 0; }
DL ABS(DL1) ;

OBJ_R16_1 := ABS(ME(1,6)) ;    OBJ_R26_1 := ABS(ME(2,6)) ;
OBJ_R12_1 := ME(1,2) + 0.7586535E-02;        OBJ_R34_1 := ME(3,4)/1 - 0.1290455E-01;
OBJ_R11_1 := ABS(ME(1,1))- 1.000222 ; OBJ_R33_1 := ABS(ME(3,3))- 0.9990903;
OBJ_R21_1 := ME(2,1) *1 - 0.7586535E-02;       OBJ_R43_1 := ME(4,3)*1 - 0.7120463 ;
OBJ_T126_1 := ABS(ME(1,26)) ;  OBJ_T346_1 := ABS(ME(3,46)) ;
OBJ_SIGMA11_1 := ABS(SIGMA(1,1)-SQUA(3.5E-3)) * 100 ;
OBJ_SIGMA33_1 := ABS(SIGMA(3,3)-SQUA(3.5E-3)) * 100 ;

PM 6 ;

{----SECOND BENDING----}
DL ABS(DL2) ;

MS CCT345_R ABS(CCT3_ANG)+CCT345_ANG_MIN CCT345_APER CCT345_n 0 0 0 0;
MS CCT345_R ABS(CCT4_ANG)+CCT345_ANG_MIN CCT345_APER -CCT345_n 0 0 0 0;
OBJ_T126_1 := ABS(ME(1,26)) ;
MS CCT345_R ABS(CCT5_ANG)+CCT345_ANG_MIN CCT345_APER CCT345_n 0 0 0 0;
DL ABS(GAP3);

M5 ABS(QS3_LEN) QS3_Q QS3_S 0 0 0 QS3_APER;

DL ABS(GAP3);
MS CCT345_R ABS(CCT5_ANG)+CCT345_ANG_MIN CCT345_APER CCT345_n 0 0 0 0;
MS CCT345_R ABS(CCT4_ANG)+CCT345_ANG_MIN CCT345_APER -CCT345_n 0 0 0 0;
MS CCT345_R ABS(CCT3_ANG)+CCT345_ANG_MIN CCT345_APER CCT345_n 0 0 0 0;

DL ABS(DL2) ;

OBJ_R16_2 := ABS(ME(1,6)) ;    OBJ_R26_2 := ABS(ME(2,6)) ;
OBJ_R12_2 := ME(1,2) *1 ;     OBJ_R34_2 := ME(3,4) *1 ;
OBJ_R21_2 := ME(2,1) *1 ;       OBJ_R43_2 := ME(4,3)*1 ;
OBJ_R11_2 := ABS(ME(1,1))- 1 ; OBJ_R33_2 := ABS(ME(3,3))- 1;
OBJ_T126_2 := ABS(ME(1,26)) ;  OBJ_T346_2 := ABS(ME(3,46)) ;
OBJ_T226_2 := ABS(ME(2,26)) ;  OBJ_T446_2 := ABS(ME(4,46)) ;
OBJ_SIGMA11_2 := ABS(SIGMA(1,1)-SQUA(3.5E-3)) * 200 ;
OBJ_SIGMA33_2 := ABS(SIGMA(3,3)-SQUA(3.5E-3)) * 200 ;

OBJ_ALIGN := (GAP1*2+GAP2*2+QS1_LEN*2+QS2_LEN)*SIN(22.5/180*3.14159)+(DL1+DL2)*SIN(45/180*3.14159)-(GAP3*2+QS3_LEN)*SIN(22.5/180*3.14159)-DL2-0.75*(SQRT(2)-1);

{-------------------------BEAMLINE  END-------------------------}

IF I-INT(I/refresh)*refresh=0; EP ; {end the picture} PG -1 -2 ; {print to screen}
  WRITE 6 '' '' '' '' 'END FITTING' 'number of iteration' I ;
  WRITE 6 'OBJ_ANG45:='&ST(OBJ_ANG45)&';' ;
  WRITE 6 'OBJ_ANG135:='&ST(OBJ_ANG135)&';' ;
  WRITE 6 'OBJ_R16_1:='&ST(OBJ_R16_1)&';' ;
  WRITE 6 'OBJ_R26_1:='&ST(OBJ_R26_1)&';' ;
  WRITE 6 'OBJ_R12_1:='&ST(OBJ_R12_1)&';' ;
  WRITE 6 'OBJ_R34_1:='&ST(OBJ_R34_1)&';' ;
  WRITE 6 'OBJ_R11_1:='&ST(OBJ_R11_1)&';' ;
  WRITE 6 'OBJ_R33_1:='&ST(OBJ_R33_1)&';' ;
  WRITE 6 'OBJ_R21_1:='&ST(OBJ_R21_1)&';' ;
  WRITE 6 'OBJ_R43_1:='&ST(OBJ_R43_1)&';' ;
  WRITE 6 'OBJ_R16_2:='&ST(OBJ_R16_2)&';' ;
  WRITE 6 'OBJ_R26_2:='&ST(OBJ_R26_2)&';' ;
  WRITE 6 'OBJ_R12_2:='&ST(OBJ_R12_2)&';' ;
  WRITE 6 'OBJ_R34_2:='&ST(OBJ_R34_2)&';' ;
  WRITE 6 'OBJ_R11_2:='&ST(OBJ_R11_2)&';' ;
  WRITE 6 'OBJ_R33_2:='&ST(OBJ_R33_2)&';' ;
  WRITE 6 'OBJ_T126_2:='&ST(OBJ_T126_2)&';' ;
  WRITE 6 'OBJ_T346_2:='&ST(OBJ_T346_2)&';' ;
  WRITE 6 'OBJ_T226_2:='&ST(OBJ_T226_2)&';' ;
  WRITE 6 'OBJ_T446_2:='&ST(OBJ_T446_2)&';' ;
  WRITE 6 'OBJ_R21_2:='&ST(OBJ_R21_2)&';' ;
  WRITE 6 'OBJ_R43_2:='&ST(OBJ_R43_2)&';' ;
  WRITE 6 'OBJ_ALIGN:='&ST(OBJ_ALIGN)&';' ;
ENDIF ;

{ ENDFIT 1E-3 200000 4 {OBJ_ANG45} OBJ_ANG135 {OBJ_R16_1 OBJ_R26_1 OBJ_R12_1 OBJ_R34_1 OBJ_R11_1 OBJ_R33_1 OBJ_R16_2 OBJ_R26_2} OBJ_T126_1 {OBJ_T346_1} {OBJ_R12_2 OBJ_R34_2} {OBJ_R21_2 OBJ_R43_2} {OBJ_R11_2 OBJ_R33_2} OBJ_T126_2 OBJ_T346_2 {OBJ_T226_2 OBJ_T446_2} OBJ_SIGMA11_2 OBJ_SIGMA33_2 {OBJ_SIGMA11_1 OBJ_SIGMA33_1} ; }

WRITE 6 '' '' '' '' 'END FITTING' 'number of iteration' I ;
{ WRITE 6 'GAP3:='&ST(GAP3)&';' ; }
WRITE 6 '' ;
WRITE 6 'CCT1_ANG:='&ST(CCT1_ANG)&';' ;
WRITE 6 'CCT2_ANG:='&ST(CCT2_ANG)&';' ;
WRITE 6 'CCT3_ANG:='&ST(CCT3_ANG)&';' ;
WRITE 6 'CCT4_ANG:='&ST(CCT4_ANG)&';' ;
WRITE 6 'CCT5_ANG:='&ST(CCT5_ANG)&';' ;
WRITE 6 '' ;
WRITE 6 'CCT12_n:='&ST(CCT12_n)&';' ;
WRITE 6 'CCT345_n:='&ST(CCT345_n)&';' ;
WRITE 6 '' ;
WRITE 6 'QS1_Q:='&ST(QS1_Q)&';' ;
WRITE 6 'QS2_Q:='&ST(QS2_Q)&';' ;
WRITE 6 'QS3_Q:='&ST(QS3_Q)&';' ;
WRITE 6 '' ;
WRITE 6 'QS1_S:='&ST(QS1_S)&';' ;
WRITE 6 'QS2_S:='&ST(QS2_S)&';' ;
WRITE 6 'QS3_S:='&ST(QS3_S)&';' ;
WRITE 6 '' ;
WRITE 6 'SIGMA(1,1)='&ST(SIGMA(1,1))&';' ;
WRITE 6 'SIGMA(3,3)='&ST(SIGMA(3,3))&';' ;
{ PM 6 ; }

ENDPROCEDURE ;
RUN ; END ;

             */
            optics前偏转段.map = CosyArbitraryOrder.readMap(
                    "  0.9964840      1.427547     0.0000000E+00 0.0000000E+00 0.8509655E-02 100000\n" +
                            " -0.4917276E-02 0.9964840     0.0000000E+00 0.0000000E+00-0.2095900E-04 010000\n" +
                            "  0.0000000E+00 0.0000000E+00-0.9950191    -0.8864910     0.0000000E+00 001000\n" +
                            "  0.0000000E+00 0.0000000E+00 0.1120935E-01-0.9950191     0.0000000E+00 000100\n" +
                            "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00  1.000000     000010\n" +
                            "  0.2095900E-04-0.8509655E-02 0.0000000E+00 0.0000000E+00 0.8032067     000001\n" +
                            "   18.82739      6.329920     0.0000000E+00 0.0000000E+00-0.4462137     200000\n" +
                            "   26.30636    -0.3118077E-01 0.0000000E+00 0.0000000E+00 0.3002196     110000\n" +
                            " -0.3239586E-01 -13.19951     0.0000000E+00 0.0000000E+00 0.1293451     020000\n" +
                            "  0.0000000E+00 0.0000000E+00 -47.27493     -17.81102     0.0000000E+00 101000\n" +
                            "  0.0000000E+00 0.0000000E+00 -11.93402     0.4386797E-01 0.0000000E+00 011000\n" +
                            "   5.352314     -4.468277     0.0000000E+00 0.0000000E+00 0.7542168     002000\n" +
                            "  0.0000000E+00 0.0000000E+00 -87.04167     -30.07234     0.0000000E+00 100100\n" +
                            "  0.0000000E+00 0.0000000E+00 0.2143808      12.12452     0.0000000E+00 010100\n" +
                            "   12.06462     -30.12220     0.0000000E+00 0.0000000E+00  5.687943     001100\n" +
                            "  0.7012846E-01  1.317867     0.0000000E+00 0.0000000E+00-0.2106103     100001\n" +
                            "  0.2592570     0.2947757     0.0000000E+00 0.0000000E+00 0.5187265E-03 010001\n" +
                            "  0.0000000E+00 0.0000000E+00 -5.573975     -3.541390     0.0000000E+00 001001\n" +
                            "  0.3977022E-01 -43.56902     0.0000000E+00 0.0000000E+00  6.335985     000200\n" +
                            "  0.0000000E+00 0.0000000E+00 -12.67261     -5.676521     0.0000000E+00 000101\n" +
                            " -0.1363192E-02 0.1040371     0.0000000E+00 0.0000000E+00-0.7095060     000002\n" +
                            "  -85.12732     -87.89620     0.0000000E+00 0.0000000E+00 -6.760498     300000\n" +
                            "  -780.6498     -526.2756     0.0000000E+00 0.0000000E+00 -9.827625     210000\n" +
                            "  -1489.826     -852.6873     0.0000000E+00 0.0000000E+00 -1.745046     120000\n" +
                            "  -775.2802     -380.3378     0.0000000E+00 0.0000000E+00 -5.030989     030000\n" +
                            "  0.0000000E+00 0.0000000E+00 -59.34564     -80.50890     0.0000000E+00 201000\n" +
                            "  0.0000000E+00 0.0000000E+00  579.8377      154.1054     0.0000000E+00 111000\n" +
                            "  0.0000000E+00 0.0000000E+00  744.5749      271.9807     0.0000000E+00 021000\n" +
                            "  -191.0297     -9.226310     0.0000000E+00 0.0000000E+00 -31.48790     102000\n" +
                            "  -248.9678      49.79603     0.0000000E+00 0.0000000E+00 -32.83909     012000\n" +
                            "  0.0000000E+00 0.0000000E+00 -322.7786     -148.9836     0.0000000E+00 003000\n" +
                            "  0.0000000E+00 0.0000000E+00 -125.5991     -180.9245     0.0000000E+00 200100\n" +
                            "  0.0000000E+00 0.0000000E+00  1226.261      303.2377     0.0000000E+00 110100\n" +
                            "  0.0000000E+00 0.0000000E+00  1660.361      586.1963     0.0000000E+00 020100\n" +
                            "  -1010.832     -147.8197     0.0000000E+00 0.0000000E+00 -62.61143     101100\n" +
                            "  -1317.014      60.18591     0.0000000E+00 0.0000000E+00 -99.47147     011100\n" +
                            "  0.0000000E+00 0.0000000E+00 -1871.144     -909.3902     0.0000000E+00 002100\n" +
                            "   31.53683      14.43764     0.0000000E+00 0.0000000E+00 0.2632580     200001\n" +
                            "   31.49717      8.853845     0.0000000E+00 0.0000000E+00 0.5880710     110001\n" +
                            "   11.59950     -2.162066     0.0000000E+00 0.0000000E+00  2.358774     020001\n" +
                            "  0.0000000E+00 0.0000000E+00 -104.3699     -69.71969     0.0000000E+00 101001\n" +
                            "  0.0000000E+00 0.0000000E+00  10.17780     -4.462861     0.0000000E+00 011001\n" +
                            "   11.93281     -21.31898     0.0000000E+00 0.0000000E+00  3.627550     002001\n" +
                            "  -1236.580     -252.0784     0.0000000E+00 0.0000000E+00 0.8920943     100200\n" +
                            "  -1660.194     -87.95044     0.0000000E+00 0.0000000E+00 -88.97716     010200\n" +
                            "  0.0000000E+00 0.0000000E+00 -3747.842     -1926.039     0.0000000E+00 001200\n" +
                            "  0.0000000E+00 0.0000000E+00 -141.4219     -119.3131     0.0000000E+00 100101\n" +
                            "  0.0000000E+00 0.0000000E+00 -25.12902     -31.03125     0.0000000E+00 010101\n" +
                            "   47.32767     -61.91976     0.0000000E+00 0.0000000E+00  12.78880     001101\n" +
                            "   3.065793     0.4488007     0.0000000E+00 0.0000000E+00 0.3329702     100002\n" +
                            "   2.223042     0.4388128     0.0000000E+00 0.0000000E+00-0.2456073E-01 010002\n" +
                            "  0.0000000E+00 0.0000000E+00 -14.00873     -7.829662     0.0000000E+00 001002\n" +
                            "  0.0000000E+00 0.0000000E+00 -2615.133     -1424.765     0.0000000E+00 000300\n" +
                            "   77.92375     -13.22572     0.0000000E+00 0.0000000E+00  15.83178     000201\n" +
                            "  0.0000000E+00 0.0000000E+00 -14.99228     -12.56426     0.0000000E+00 000102\n" +
                            "  0.4273311E-02-0.1026127     0.0000000E+00 0.0000000E+00 0.6438708     000003\n" +
                            "  -988.2586     -144.9184     0.0000000E+00 0.0000000E+00 -33.71432     400000\n" +
                            "  -5102.509     -354.0919     0.0000000E+00 0.0000000E+00  239.8950     310000\n" +
                            "  -5967.983      2067.207     0.0000000E+00 0.0000000E+00  831.1023     220000\n" +
                            "   2857.595      6342.473     0.0000000E+00 0.0000000E+00  684.0151     130000\n" +
                            "   5129.457      4321.480     0.0000000E+00 0.0000000E+00  102.4164     040000\n" +
                            "  0.0000000E+00 0.0000000E+00  3189.634      1809.613     0.0000000E+00 301000\n" +
                            "  0.0000000E+00 0.0000000E+00  15522.38      8969.014     0.0000000E+00 211000\n" +
                            "  0.0000000E+00 0.0000000E+00  19427.00      11864.64     0.0000000E+00 121000\n" +
                            "  0.0000000E+00 0.0000000E+00  4098.431      3321.072     0.0000000E+00 031000\n" +
                            "   2598.102      2007.515     0.0000000E+00 0.0000000E+00 -688.2156     202000\n" +
                            "   12893.01      6840.745     0.0000000E+00 0.0000000E+00 -107.5533     112000\n" +
                            "   11084.95      4892.492     0.0000000E+00 0.0000000E+00  363.2696     022000\n" +
                            "  0.0000000E+00 0.0000000E+00  2406.079      1327.079     0.0000000E+00 103000\n" +
                            "  0.0000000E+00 0.0000000E+00  2027.152      1213.092     0.0000000E+00 013000\n" +
                            "   1619.865      1097.309     0.0000000E+00 0.0000000E+00 -185.8271     004000\n" +
                            "  0.0000000E+00 0.0000000E+00  6459.750      3643.806     0.0000000E+00 300100\n" +
                            "  0.0000000E+00 0.0000000E+00  33238.01      18958.35     0.0000000E+00 210100\n" +
                            "  0.0000000E+00 0.0000000E+00  44174.40      26295.42     0.0000000E+00 120100\n" +
                            "  0.0000000E+00 0.0000000E+00  11876.86      8355.897     0.0000000E+00 030100\n" +
                            "   8531.243      7813.287     0.0000000E+00 0.0000000E+00 -2584.617     201100\n" +
                            "   52199.91      28885.43     0.0000000E+00 0.0000000E+00 -411.1203     111100\n" +
                            "   47637.32      21981.06     0.0000000E+00 0.0000000E+00  790.3988     021100\n" +
                            "  0.0000000E+00 0.0000000E+00  20110.18      11655.17     0.0000000E+00 102100\n" +
                            "  0.0000000E+00 0.0000000E+00  16043.13      10142.71     0.0000000E+00 012100\n" +
                            "   15811.75      9484.424     0.0000000E+00 0.0000000E+00 -1002.745     003100\n" +
                            "  -327.9768     -194.0059     0.0000000E+00 0.0000000E+00 -27.98015     300001\n" +
                            "  -1071.967     -707.4767     0.0000000E+00 0.0000000E+00 -25.99944     210001\n" +
                            "  -667.4609     -633.4351     0.0000000E+00 0.0000000E+00  29.52836     120001\n" +
                            "   152.6346     -167.6475     0.0000000E+00 0.0000000E+00  50.90065     030001\n" +
                            "  0.0000000E+00 0.0000000E+00  489.6067      146.6705     0.0000000E+00 201001\n" +
                            "  0.0000000E+00 0.0000000E+00  2503.705      1431.457     0.0000000E+00 111001\n" +
                            "  0.0000000E+00 0.0000000E+00  2427.708      1537.387     0.0000000E+00 021001\n" +
                            "  -1470.333     -434.7131     0.0000000E+00 0.0000000E+00 -227.8012     102001\n" +
                            "  -1424.086     -400.3807     0.0000000E+00 0.0000000E+00 -82.72358     012001\n" +
                            "  0.0000000E+00 0.0000000E+00 -519.8724     -445.2298     0.0000000E+00 003001\n" +
                            "   6207.730      7275.314     0.0000000E+00 0.0000000E+00 -2436.605     200200\n" +
                            "   51774.29      29681.22     0.0000000E+00 0.0000000E+00 -525.6504     110200\n" +
                            "   49751.75      23797.30     0.0000000E+00 0.0000000E+00 -261.5314     020200\n" +
                            "  0.0000000E+00 0.0000000E+00  54062.68      32258.89     0.0000000E+00 101200\n" +
                            "  0.0000000E+00 0.0000000E+00  40025.98      26106.53     0.0000000E+00 011200\n" +
                            "   56722.29      30970.65     0.0000000E+00 0.0000000E+00 -1832.580     002200\n" +
                            "  0.0000000E+00 0.0000000E+00  901.5780      384.4138     0.0000000E+00 200101\n" +
                            "  0.0000000E+00 0.0000000E+00  3623.505      2535.945     0.0000000E+00 110101\n" +
                            "  0.0000000E+00 0.0000000E+00  2978.061      2548.309     0.0000000E+00 020101\n" +
                            "  -5543.701     -1910.756     0.0000000E+00 0.0000000E+00 -900.7510     101101\n" +
                            "  -4926.471     -2019.866     0.0000000E+00 0.0000000E+00 -266.0787     011101\n" +
                            "  0.0000000E+00 0.0000000E+00 -1242.839     -1712.620     0.0000000E+00 002101\n" +
                            "  -60.90836     -20.67737     0.0000000E+00 0.0000000E+00 -5.331238     200002\n" +
                            "  -169.4071     -67.17743     0.0000000E+00 0.0000000E+00 -7.621608     110002\n" +
                            "  -108.7372     -24.15192     0.0000000E+00 0.0000000E+00 -9.223707     020002\n" +
                            "  0.0000000E+00 0.0000000E+00  77.92211     -15.99628     0.0000000E+00 101002\n" +
                            "  0.0000000E+00 0.0000000E+00 -21.56670     -13.79002     0.0000000E+00 011002\n" +
                            "  -2.176233      21.97157     0.0000000E+00 0.0000000E+00 -20.47896     002002\n" +
                            "  0.0000000E+00 0.0000000E+00  46604.53      28373.24     0.0000000E+00 100300\n" +
                            "  0.0000000E+00 0.0000000E+00  31405.70      20886.32     0.0000000E+00 010300\n" +
                            "   89129.94      45249.87     0.0000000E+00 0.0000000E+00 -1162.047     001300\n" +
                            "  -4930.865     -1942.906     0.0000000E+00 0.0000000E+00 -1015.544     100201\n" +
                            "  -3475.738     -2185.196     0.0000000E+00 0.0000000E+00 -114.9164     010201\n" +
                            "  0.0000000E+00 0.0000000E+00  1786.354     -1088.943     0.0000000E+00 001201\n" +
                            "  0.0000000E+00 0.0000000E+00  218.4217      56.41572     0.0000000E+00 100102\n" +
                            "  0.0000000E+00 0.0000000E+00  31.88740      1.965913     0.0000000E+00 010102\n" +
                            "  -45.23487      141.8816     0.0000000E+00 0.0000000E+00 -84.44508     001102\n" +
                            "  -2.137252      1.014195     0.0000000E+00 0.0000000E+00-0.7583947     100003\n" +
                            "  -3.994676     -1.471476     0.0000000E+00 0.0000000E+00 0.8553562E-01 010003\n" +
                            "  0.0000000E+00 0.0000000E+00  18.49343      1.327210     0.0000000E+00 001003\n" +
                            "   51908.87      24891.13     0.0000000E+00 0.0000000E+00 -38.88291     000400\n" +
                            "  0.0000000E+00 0.0000000E+00  4775.736      1373.465     0.0000000E+00 000301\n" +
                            "  -176.1586      128.4034     0.0000000E+00 0.0000000E+00 -116.0137     000202\n" +
                            "  0.0000000E+00 0.0000000E+00  40.52519      14.82079     0.0000000E+00 000103\n" +
                            "  0.1243206     0.2131167     0.0000000E+00 0.0000000E+00-0.6236264     000004\n" +
                            "   3212.271      3666.892     0.0000000E+00 0.0000000E+00  961.3873     500000\n" +
                            "   18454.58      15114.00     0.0000000E+00 0.0000000E+00  8054.366     410000\n" +
                            "   76129.41      31924.52     0.0000000E+00 0.0000000E+00  17791.08     320000\n" +
                            "   160354.5      41424.55     0.0000000E+00 0.0000000E+00  10312.11     230000\n" +
                            "   136525.0      21529.00     0.0000000E+00 0.0000000E+00 -5024.624     140000\n" +
                            "   31556.30     -1595.542     0.0000000E+00 0.0000000E+00 -4553.644     050000\n" +
                            "  0.0000000E+00 0.0000000E+00  10885.19      7558.049     0.0000000E+00 401000\n" +
                            "  0.0000000E+00 0.0000000E+00  13890.57      10151.07     0.0000000E+00 311000\n" +
                            "  0.0000000E+00 0.0000000E+00 -114642.3     -64451.41     0.0000000E+00 221000\n" +
                            "  0.0000000E+00 0.0000000E+00 -277321.1     -157616.3     0.0000000E+00 131000\n" +
                            "  0.0000000E+00 0.0000000E+00 -159356.8     -90622.07     0.0000000E+00 041000\n" +
                            "   27381.91     -844.9799     0.0000000E+00 0.0000000E+00  3566.105     302000\n" +
                            "   20726.79     -48713.19     0.0000000E+00 0.0000000E+00  25190.87     212000\n" +
                            "  -131132.7     -143234.8     0.0000000E+00 0.0000000E+00  32237.54     122000\n" +
                            "  -149777.7     -105245.5     0.0000000E+00 0.0000000E+00  7004.990     032000\n" +
                            "  0.0000000E+00 0.0000000E+00 -52889.68     -29361.33     0.0000000E+00 203000\n" +
                            "  0.0000000E+00 0.0000000E+00 -147793.0     -85014.94     0.0000000E+00 113000\n" +
                            "  0.0000000E+00 0.0000000E+00 -77925.59     -46400.78     0.0000000E+00 023000\n" +
                            "  -34830.56     -21432.04     0.0000000E+00 0.0000000E+00 -7604.154     104000\n" +
                            "  -63594.39     -38997.78     0.0000000E+00 0.0000000E+00 -1363.057     014000\n" +
                            "  0.0000000E+00 0.0000000E+00  16992.96      10138.25     0.0000000E+00 005000\n" +
                            "  0.0000000E+00 0.0000000E+00  25643.21      16912.30     0.0000000E+00 400100\n" +
                            "  0.0000000E+00 0.0000000E+00  54967.64      34289.86     0.0000000E+00 310100\n" +
                            "  0.0000000E+00 0.0000000E+00 -164659.7     -95711.38     0.0000000E+00 220100\n" +
                            "  0.0000000E+00 0.0000000E+00 -495390.1     -287001.3     0.0000000E+00 130100\n" +
                            "  0.0000000E+00 0.0000000E+00 -301391.6     -174852.9     0.0000000E+00 040100\n" +
                            "   118239.1      157.8692     0.0000000E+00 0.0000000E+00  14588.39     301100\n" +
                            "   144671.9     -177305.5     0.0000000E+00 0.0000000E+00  92164.74     211100\n" +
                            "  -416181.0     -548875.4     0.0000000E+00 0.0000000E+00  108999.3     121100\n" +
                            "  -558300.1     -414167.3     0.0000000E+00 0.0000000E+00  16052.11     031100\n" +
                            "  0.0000000E+00 0.0000000E+00 -368122.2     -216776.4     0.0000000E+00 202100\n" +
                            "  0.0000000E+00 0.0000000E+00 -1028884.     -607895.2     0.0000000E+00 112100\n" +
                            "  0.0000000E+00 0.0000000E+00 -586748.1     -353908.8     0.0000000E+00 022100\n" +
                            "  -320231.5     -191630.0     0.0000000E+00 0.0000000E+00 -59661.46     103100\n" +
                            "  -596526.2     -352458.1     0.0000000E+00 0.0000000E+00 -8615.654     013100\n" +
                            "  0.0000000E+00 0.0000000E+00  162069.9      98037.41     0.0000000E+00 004100\n" +
                            "   1409.759      1039.526     0.0000000E+00 0.0000000E+00  217.1347     400001\n" +
                            "   9999.093      5304.353     0.0000000E+00 0.0000000E+00  1412.277     310001\n" +
                            "   24843.56      10055.15     0.0000000E+00 0.0000000E+00  2617.508     220001\n" +
                            "   18298.00      5193.319     0.0000000E+00 0.0000000E+00  1879.185     130001\n" +
                            "  -230.6955     -2054.124     0.0000000E+00 0.0000000E+00  639.2554     040001\n" +
                            "  0.0000000E+00 0.0000000E+00  8571.667      7237.890     0.0000000E+00 301001\n" +
                            "  0.0000000E+00 0.0000000E+00  26392.54      23689.41     0.0000000E+00 211001\n" +
                            "  0.0000000E+00 0.0000000E+00  19448.43      20410.03     0.0000000E+00 121001\n" +
                            "  0.0000000E+00 0.0000000E+00  4071.828      3130.459     0.0000000E+00 031001\n" +
                            "   16724.31      9343.510     0.0000000E+00 0.0000000E+00  311.8775     202001\n" +
                            "   54321.07      29909.91     0.0000000E+00 0.0000000E+00  5619.036     112001\n" +
                            "   35818.73      20202.34     0.0000000E+00 0.0000000E+00  4153.121     022001\n" +
                            "  0.0000000E+00 0.0000000E+00  10530.42      8231.623     0.0000000E+00 103001\n" +
                            "  0.0000000E+00 0.0000000E+00 -1859.215      1215.478     0.0000000E+00 013001\n" +
                            "   8817.550      5064.945     0.0000000E+00 0.0000000E+00 -1117.052     004001\n" +
                            "   127449.5      4544.471     0.0000000E+00 0.0000000E+00  15038.04     300200\n" +
                            "   202916.9     -159213.2     0.0000000E+00 0.0000000E+00  81420.28     210200\n" +
                            "  -317475.1     -525216.1     0.0000000E+00 0.0000000E+00  82058.73     120200\n" +
                            "  -526457.5     -406842.7     0.0000000E+00 0.0000000E+00 -3319.059     030200\n" +
                            "  0.0000000E+00 0.0000000E+00 -840105.7     -513809.9     0.0000000E+00 201200\n" +
                            "  0.0000000E+00 0.0000000E+00 -2350942.     -1410907.     0.0000000E+00 111200\n" +
                            "  0.0000000E+00 0.0000000E+00 -1444423.     -877521.5     0.0000000E+00 021200\n" +
                            "  -1060119.     -630041.0     0.0000000E+00 0.0000000E+00 -180301.8     102200\n" +
                            "  -2035883.     -1179620.     0.0000000E+00 0.0000000E+00 -22671.21     012200\n" +
                            "  0.0000000E+00 0.0000000E+00  612939.8      375706.7     0.0000000E+00 003200\n" +
                            "  0.0000000E+00 0.0000000E+00  12525.70      11693.61     0.0000000E+00 300101\n" +
                            "  0.0000000E+00 0.0000000E+00  25164.59      32634.50     0.0000000E+00 210101\n" +
                            "  0.0000000E+00 0.0000000E+00 -3028.462      19429.68     0.0000000E+00 120101\n" +
                            "  0.0000000E+00 0.0000000E+00 -7981.457     -1192.547     0.0000000E+00 030101\n" +
                            "   61407.15      31279.93     0.0000000E+00 0.0000000E+00  4077.293     201101\n" +
                            "   175121.6      97067.70     0.0000000E+00 0.0000000E+00  22849.20     111101\n" +
                            "   100490.0      60709.06     0.0000000E+00 0.0000000E+00  16238.59     021101\n" +
                            "  0.0000000E+00 0.0000000E+00  49878.47      41672.40     0.0000000E+00 102101\n" +
                            "  0.0000000E+00 0.0000000E+00 -29110.59     -4153.174     0.0000000E+00 012101\n" +
                            "   67219.82      38594.05     0.0000000E+00 0.0000000E+00 -8430.621     003101\n" +
                            "   279.1872      133.1781     0.0000000E+00 0.0000000E+00  55.90081     300002\n" +
                            "   1910.850      1104.390     0.0000000E+00 0.0000000E+00  177.5441     210002\n" +
                            "   3724.824      2228.724     0.0000000E+00 0.0000000E+00  149.6620     120002\n" +
                            "   2176.112      1549.378     0.0000000E+00 0.0000000E+00 -49.74898     030002\n" +
                            "  0.0000000E+00 0.0000000E+00  846.8523      1008.958     0.0000000E+00 201002\n" +
                            "  0.0000000E+00 0.0000000E+00  79.24668      1729.828     0.0000000E+00 111002\n" +
                            "  0.0000000E+00 0.0000000E+00 -2918.687     -38.67415     0.0000000E+00 021002\n" +
                            "  -132.1631     -761.0048     0.0000000E+00 0.0000000E+00 -267.4464     102002\n" +
                            "   1773.297     -318.9611     0.0000000E+00 0.0000000E+00  210.9160     012002\n" +
                            "  0.0000000E+00 0.0000000E+00  2093.698      1052.638     0.0000000E+00 003002\n" +
                            "  0.0000000E+00 0.0000000E+00 -632577.7     -396850.4     0.0000000E+00 200300\n" +
                            "  0.0000000E+00 0.0000000E+00 -1772318.     -1074352.     0.0000000E+00 110300\n" +
                            "  0.0000000E+00 0.0000000E+00 -1175291.     -719709.8     0.0000000E+00 020300\n" +
                            "  -1512605.     -905111.1     0.0000000E+00 0.0000000E+00 -250507.9     101300\n" +
                            "  -3018747.     -1733345.     0.0000000E+00 0.0000000E+00 -31255.73     011300\n" +
                            "  0.0000000E+00 0.0000000E+00  1138177.      707205.6     0.0000000E+00 002300\n" +
                            "   62107.20      27296.03     0.0000000E+00 0.0000000E+00  7036.888     200201\n" +
                            "   138473.5      75735.46     0.0000000E+00 0.0000000E+00  24270.88     110201\n" +
                            "   59456.00      38730.58     0.0000000E+00 0.0000000E+00  20106.61     020201\n" +
                            "  0.0000000E+00 0.0000000E+00  40855.52      48566.89     0.0000000E+00 101201\n" +
                            "  0.0000000E+00 0.0000000E+00 -108545.3     -41012.59     0.0000000E+00 011201\n" +
                            "   178189.0      106924.5     0.0000000E+00 0.0000000E+00 -25376.84     002201\n" +
                            "  0.0000000E+00 0.0000000E+00  1338.678      1640.685     0.0000000E+00 200102\n" +
                            "  0.0000000E+00 0.0000000E+00 -1306.670      1584.468     0.0000000E+00 110102\n" +
                            "  0.0000000E+00 0.0000000E+00 -6551.065     -2069.732     0.0000000E+00 020102\n" +
                            "   4325.760     -1422.164     0.0000000E+00 0.0000000E+00 -195.2051     101102\n" +
                            "   12321.47      1428.261     0.0000000E+00 0.0000000E+00  1249.403     011102\n" +
                            "  0.0000000E+00 0.0000000E+00  14123.82      8157.600     0.0000000E+00 002102\n" +
                            "   20.84792     -17.51973     0.0000000E+00 0.0000000E+00  9.133506     200003\n" +
                            "   133.2679      10.50293     0.0000000E+00 0.0000000E+00  17.49896     110003\n" +
                            "   111.5991     -26.61505     0.0000000E+00 0.0000000E+00  25.08871     020003\n" +
                            "  0.0000000E+00 0.0000000E+00  39.93366      111.2365     0.0000000E+00 101003\n" +
                            "  0.0000000E+00 0.0000000E+00  192.8423      105.0357     0.0000000E+00 011003\n" +
                            "  -133.7458     -37.52870     0.0000000E+00 0.0000000E+00 -21.50789     002003\n" +
                            "  -790157.4     -480609.7     0.0000000E+00 0.0000000E+00 -134940.5     100400\n" +
                            "  -1649371.     -943826.9     0.0000000E+00 0.0000000E+00 -18600.15     010400\n" +
                            "  0.0000000E+00 0.0000000E+00  1026971.      648182.9     0.0000000E+00 001400\n" +
                            "  0.0000000E+00 0.0000000E+00 -36799.42     -6234.521     0.0000000E+00 100301\n" +
                            "  0.0000000E+00 0.0000000E+00 -109420.2     -51648.27     0.0000000E+00 010301\n" +
                            "   184532.8      124440.4     0.0000000E+00 0.0000000E+00 -35795.19     001301\n" +
                            "   9199.393      431.1078     0.0000000E+00 0.0000000E+00  1060.689     100202\n" +
                            "   15730.14      4517.231     0.0000000E+00 0.0000000E+00  1091.099     010202\n" +
                            "  0.0000000E+00 0.0000000E+00  25653.59      16936.42     0.0000000E+00 001202\n" +
                            "  0.0000000E+00 0.0000000E+00 -78.45425      114.7943     0.0000000E+00 100103\n" +
                            "  0.0000000E+00 0.0000000E+00  311.8037      222.5586     0.0000000E+00 010103\n" +
                            "  -589.3735     -354.4308     0.0000000E+00 0.0000000E+00 -47.82400     001103\n" +
                            "  -2.389853     -4.488911     0.0000000E+00 0.0000000E+00  1.320703     100004\n" +
                            "   2.620467      1.533837     0.0000000E+00 0.0000000E+00-0.4379146     010004\n" +
                            "  0.0000000E+00 0.0000000E+00 -11.66715      10.06598     0.0000000E+00 001004\n" +
                            "  0.0000000E+00 0.0000000E+00  354529.3      228455.6     0.0000000E+00 000500\n" +
                            "   53011.41      48977.02     0.0000000E+00 0.0000000E+00 -19737.88     000401\n" +
                            "  0.0000000E+00 0.0000000E+00  9152.072      8017.687     0.0000000E+00 000302\n" +
                            "  -356.3105     -503.4281     0.0000000E+00 0.0000000E+00  102.4647     000203\n" +
                            "  0.0000000E+00 0.0000000E+00 -54.86278     -5.797522     0.0000000E+00 000104\n" +
                            " -0.3814892    -0.4168794     0.0000000E+00 0.0000000E+00 0.6492803     000005"
            );
        }
    }


    //--------------parameter------------------


    private final BaseUtils.Timer timer = new BaseUtils.Timer();

    private void logPastTime(String msg) {
        timer.printPeriodAfterInitial(Logger.getLogger(), msg);
    }

    BaseUtils.Switcher<String> switcher = BaseUtils.Switcher.creat(
            Plot2d.RED_LINE,
            Plot2d.YELLOW_LINE,
            Plot2d.BLUE_LINE,
            Plot2d.GREEN_LINE,
            Plot2d.RED_DASH,
            Plot2d.PINK_DASH,
            Plot2d.BLUE_DASH,
            Plot2d.BLACK_DASH,
            Plot2d.BLACK_LINE
    );

    // 磁钢度
    private final double Bp = 2.43213;

    // QS 123
    private double QS1_GRADIENT = 53.3; //T m-1
    private double QS2_GRADIENT = -37.7; //T m-1
    private double QS3_GRADIENT = 7.6; //T m-1


    // 注意，六极场梯度一直存在错误，实际值应该乘二。由陈庚发现 2020年5月26日
    private double QS1_SECOND_GRADIENT = -213.78 * 2.0 / 2.0; //T m-2
    private double QS2_SECOND_GRADIENT = 673.56 * 2.0 / 2.0; //T m-2
    private double QS3_SECOND_GRADIENT = 40.83 * 2.0 / 2.0; //T m-2
    private double QS1_APERTURE_MM = 30;
    private double QS2_APERTURE_MM = 30;
    private double QS3_APERTURE_MM = 60;

    // 偏转半径
    private static final double trajectoryBigRPart1 = 0.75;
    private static final double trajectoryBigRPart2 = 0.75;
    private final double dipoleCct12BigR = 0.75;
    private final double dipoleCct345BigR = 0.75;
    private double agCct12BigR = 750.2 * MM;
    private double agCct345BigR = 0.7512 * 0 + 0.75; //2020年5月28日

    // 初始绕线位置
    private double dipoleCct12StartingθInner = 0.0;
    private double dipoleCct12StartingθOuter = 0.0;
    private double dipoleCct12StartingφInner = 0.0;
    private double dipoleCct12StartingφOuter = 0.0;
    private boolean dipoleCct12DirectθInner = true;
    private boolean dipoleCct12DirectθOuter = false;
    private double agCct12StartingθInner = 0.0; // 起始绕线方向
    private double agCct12StartingθOuter = 0.0;
    private double agCct12StartingφInner = 0.0;
    private double agCct12StartingφOuter = 0.0;
    private boolean agCct12DirectθInner = false;
    private boolean agCct12DirectθOuter = true;

    private double dipoleCct345StartingθInner = 0.0;
    private double dipoleCct345StartingθOuter = 0.0;
    private double dipoleCct345StartingφInner = 0.0;
    private double dipoleCct345StartingφOuter = 0.0;
    private boolean dipoleCct345DirectθInner = true;
    private boolean dipoleCct345DirectθOuter = false;
    private double agCct345StartingθInner = 0.0; // 起始绕线方向
    private double agCct345StartingθOuter = 0.0;
    private double agCct345StartingφInner = 0.0;
    private double agCct345StartingφOuter = 0.0;
    private boolean agCct345DirectθInner = false;
    private boolean agCct345DirectθOuter = true;

    // CCT孔径
    private final double agCct12SmallRInner = 23.5 * MM;
    private final double agCct12SmallROuter = 33.5 * MM;
    private final double dipoleCct12SmallRInner = 43 * MM;
    private final double dipoleCct12SmallROuter = 52 * MM;

    private double agCct345SmallRInner = 23.5 * MM + 40 * MM;
    private double agCct345SmallROuter = 33.5 * MM + 40 * MM;
    private final double dipoleCct345SmallRInner = 43 * MM + 40 * MM;
    private final double dipoleCct345SmallROuter = 52 * MM + 40 * MM;

    // CCT角度
    private static final double dipoleCct12Angle = 22.5;
    private static final double agCctAngle1 = 9.1;
    private static final double agCctAngle2 = 13.4;

    private static final double dipoleCct345Angle = 67.5;
    private static final double agCctAngle3 = 8.0;
    private static final double agCctAngle4 = 30.0;
    private static final double agCctAngle5 = 29.5;


    // CCT长度
    private static final double DL1 = 1.1759;
    private static final double DL2 = 2.40;
    private static final double QS1_LEN = 0.2;
    private static final double QS2_LEN = 0.2;
    private static final double QS3_LEN = 0.2;
    private static final double GAP1 = 0.15;
    private static final double GAP2 = 0.15;
    private static final double GAP3 = 0.2585751675857585;
    private static final double BETWEEN_CCT225 = GAP1 * 2 + QS1_LEN * 2 + GAP2 * 2 + QS2_LEN;
    private static final double CCT12_LENGTH = trajectoryBigRPart1 * BaseUtils.Converter.angleToRadian(dipoleCct12Angle);
    private static final double CCT12_LENGTH_PART1 = trajectoryBigRPart1 * BaseUtils.Converter.angleToRadian(agCctAngle1);
    private static final double CCT12_LENGTH_PART2 = trajectoryBigRPart1 * BaseUtils.Converter.angleToRadian(agCctAngle2);
    private static final double CCT345_LENGTH = trajectoryBigRPart2 * BaseUtils.Converter.angleToRadian(dipoleCct345Angle);
    private static final double CCT345_LENGTH_PART3 = trajectoryBigRPart2 * BaseUtils.Converter.angleToRadian(agCctAngle3);
    private static final double CCT345_LENGTH_PART4 = trajectoryBigRPart2 * BaseUtils.Converter.angleToRadian(agCctAngle4);
    private static final double CCT345_LENGTH_PART5 = trajectoryBigRPart2 * BaseUtils.Converter.angleToRadian(agCctAngle5);


    // CCT a0 a1 a2
    private double dipoleCct12A0Inner = -dipoleCct12SmallRInner * Math.sqrt(3);
    private double dipoleCct12A0Outer = dipoleCct12SmallROuter * Math.sqrt(3);
    private double dipoleCct12A1Inner = Math.pow(dipoleCct12SmallRInner, 2) * 0.225;
    private double dipoleCct12A1Outer = -Math.pow(dipoleCct12SmallROuter, 2) * 0.225;
    private double dipoleCct12A2Inner = 0.0;
    private double dipoleCct12A2Outer = 0.0;

    private final double agCct12A0Inner = 0.0;
    private final double agCct12A0Outer = 0.0;
    private final double agCct12A1Inner = -Math.pow(agCct12SmallRInner, 2) * Math.sqrt(3) * 20;
    private final double agCct12A1Outer = Math.pow(agCct12SmallROuter, 2) * Math.sqrt(3) * 20;
    private final double agCct12A2Inner = 0.0;
    private final double agCct12A2Outer = 0.0;

    private double dipoleCct345A0Inner = -dipoleCct345SmallRInner * Math.sqrt(3);
    private double dipoleCct345A0Outer = dipoleCct345SmallROuter * Math.sqrt(3);
    private double dipoleCct345A1Inner = Math.pow(dipoleCct345SmallRInner, 2) * 0.250;
    private double dipoleCct345A1Outer = -Math.pow(dipoleCct345SmallROuter, 2) * 0.250;
    private double dipoleCct345A2Inner = 0.0;
    private double dipoleCct345A2Outer = 0.0;

    // 单值 作废
//    private double agCct345A0Inner = 0.0;
//    private double agCct345A0Outer = 0.0;
//    private double agCct345A1Inner = -Math.pow(agCct345SmallRInner, 2) * Math.sqrt(3) * 20 * 0.19;
//    private double agCct345A1Outer = Math.pow(agCct345SmallROuter, 2) * Math.sqrt(3) * 20 * 0.19;
//    private final double agCct345A2Inner = 0.0;
//    private final double agCct345A2Outer = 0.0;

    // 数组
    private double[] agCct345A0Inners = BaseUtils.ArrayUtils.repeat(0.0, 3);
    private double[] agCct345A0Outers = BaseUtils.ArrayUtils.repeat(0.0, 3);
    private double[] agCct345A1Inners = BaseUtils.ArrayUtils.repeat(-Math.pow(agCct345SmallRInner, 2) * Math.sqrt(3) * 20 * 0.19, 3);
    private double[] agCct345A1Outers = BaseUtils.ArrayUtils.repeat(Math.pow(agCct345SmallROuter, 2) * Math.sqrt(3) * 20 * 0.19, 3);
    private double[] agCct345A2Inners = BaseUtils.ArrayUtils.repeat(0.0, 3);
    private double[] agCct345A2Outers = BaseUtils.ArrayUtils.repeat(0.0, 3);

    // 匝数 5mm匝间距 则匝数 = 58.85
    private final int dipoleCct12WindingNumber = 59;
    private final int agCctWindingNumber1 = 23;
    private final int agCctWindingNumber2 = 34; // sum = 57

    private final int dipoleCct345WindingNumber = 174; // 原 180，让最小匝间距>5mm
    private final int agCctWindingNumber3 = 16;
    private final int agCctWindingNumber4 = 60;
    private final int agCctWindingNumber5 = 59;  // sum 135

    // 电流
    private double dipoleCct12IInner = 9.898121356964111 * kA; // 求解获得
    private double dipoleCct12IOuter = dipoleCct12IInner;
    private double agCct12IInner = -4618.911905272398;//* (1 - 0.010335570469798657);
    private double agCct12IOuter = agCct12IInner;


    private double dipoleCct345IInner = -10.017 * kA;
    private double dipoleCct345IOuter = dipoleCct345IInner;
    private double agCct345IInner = 4618.911905272398 / 45 * 18.14 / 0.19; // 9799 // 2020年6月11日 *0.995
    private double agCct345IOuter = agCct345IInner;


    // CCT每匝分段
    private final int numberPerWinding = 360;

    /*-----------------辅助量----------------------*/
    final static double MM = 1e-3;
    final static double MRAD = 1e-3; // m rad
    final static double kA = 1e3;
    final static double PRESENT = 0.01; // %
    final static double Gs = 1e-4; // 1T = 10000 Gs

    public static void main(String[] args) throws Exception {
        BaseUtils.Timer.printPeriodPerSecondCall(Logger.getLogger());
        StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[1];
        Logger.getLogger().info("{}", stackTraceElement.getClassName());
        Class<?> currentClass = Class.forName(stackTraceElement.getClassName());

        Constructor<?> constructor = currentClass.getConstructor();
        Object newInstance = constructor.newInstance();

        Class<run> runAnnotation = run.class;

        Method[] methods = currentClass.getMethods();
        Arrays.stream(methods)
                .filter(method -> method.isAnnotationPresent(runAnnotation))
                .sorted((m1, m2) -> Integer.compare(
                        m2.getAnnotation(runAnnotation).value(),
                        m1.getAnnotation(runAnnotation).value()
                ))
                .limit(1)
                .forEach(method -> {
                    Logger.getLogger().info("运行{}", method.toString());
                    try {
                        method.invoke(newInstance);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                });

        BaseUtils.Timer.printPeriodPerSecondCall(Logger.getLogger());
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    private @interface run {
        int value() default 0;

        String code() default "";
    }
}
