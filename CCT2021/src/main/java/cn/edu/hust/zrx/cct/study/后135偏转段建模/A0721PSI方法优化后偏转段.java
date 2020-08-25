package cn.edu.hust.zrx.cct.study.后135偏转段建模;

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
import cn.edu.hust.zrx.cct.base.python.Plot3d;
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
 * A0721PSI方法优化后偏转段
 * <p>
 * Data
 * 21:11
 *
 * @author zrx
 * @version 1.0
 */

public class A0721PSI方法优化后偏转段 {

    @run(1)
    public void 后偏转段CCT0前后1米椭圆() {

        Cct cct345_1 = getCct345_1();

        //TrajectoryFactory.setStartingPoint(trajectoryPart1.pointAtEnd())
        //                .setStraightLine(DL2, trajectoryPart1.directAtEnd())
        //                .addArcLine(trajectoryBigRPart2, true, dipoleCct345Angle)
        //                .addStraitLine(GAP3 + QS3_LEN + GAP3)
        //                .addArcLine(trajectoryBigRPart2, true, dipoleCct345Angle)
        //                .addStraitLine(DL2);
        Trajectory trajectoryPart2 = getTrajectoryPart2();

        Vector2 d0 = trajectoryPart2.directAt(1.4);
        Point2 p0 = trajectoryPart2.pointAt(1.4);

        Trajectory trajectory = TrajectoryFactory.setStartingPoint(p0)
                .setStraightLine(1.0, d0)
                .addArcLine(trajectoryBigRPart2, true, dipoleCct345Angle)
                .addStraitLine(1.0);

//        trajectory.plot3d();
//        cct345_1.plot3();
//
//        Plot3d.showThread();


        phase相椭圆画图(trajectory.getLength(), false, 0.00, 32,
                true, 1, cct345_1,
                trajectory, 512, 5,
                List.of(
                        BaseUtils.Content.BiContent.create("cosy", Maps.后偏转段CCT0前后1米.map)
                )
        );

    }

    @run(-2)
    public void 后偏转段CCT0前后1包络() {

        Cct cct345_1 = getCct345_1();


        Trajectory trajectoryPart2 = getTrajectoryPart2();

        Vector2 d0 = trajectoryPart2.directAt(1.4);
        Point2 p0 = trajectoryPart2.pointAt(1.4);

        Trajectory trajectory = TrajectoryFactory.setStartingPoint(p0)
                .setStraightLine(1.0, d0)
                .addArcLine(trajectoryBigRPart2, true, dipoleCct345Angle)
                .addStraitLine(1.0);


        List<List<Point2>> lists = track多粒子(32, 0.05, trajectory, trajectory.getLength(), cct345_1, false);

        lists.forEach(Plot2d::plot2);

        Plot2d.showThread();

    }

    private enum Maps {

        后偏转段CCT0前后1米;

        static {
            后偏转段CCT0前后1米.map = CosyArbitraryOrder.readMap(
                    "  -1.623753     -2.314735     0.0000000E+00 0.0000000E+00-0.6086743     100000\n" +
                            "  -1.889581     -3.309543     0.0000000E+00 0.0000000E+00-0.9199559     010000\n" +
                            "  0.0000000E+00 0.0000000E+00-0.6056581    -0.8326336     0.0000000E+00 001000\n" +
                            "  0.0000000E+00 0.0000000E+00  1.847102     0.8882228     0.0000000E+00 000100\n" +
                            "  0.0000000E+00 0.0000000E+00 0.0000000E+00 0.0000000E+00  1.000000     000010\n" +
                            "  0.3436418     0.1150208     0.0000000E+00 0.0000000E+00 0.4983761     000001\n" +
                            "  -2.773268     -2.252022     0.0000000E+00 0.0000000E+00 -1.637359     200000\n" +
                            "  -12.39203     -11.60185     0.0000000E+00 0.0000000E+00 -5.207046     110000\n" +
                            "  -12.64511     -12.14804     0.0000000E+00 0.0000000E+00 -4.468781     020000\n" +
                            "  0.0000000E+00 0.0000000E+00 -7.582589     -5.107612     0.0000000E+00 101000\n" +
                            "  0.0000000E+00 0.0000000E+00 -10.87782     -7.114725     0.0000000E+00 011000\n" +
                            " -0.8126374    -0.1669061     0.0000000E+00 0.0000000E+00-0.4116962     002000\n" +
                            "  0.0000000E+00 0.0000000E+00 -6.410598     -4.356298     0.0000000E+00 100100\n" +
                            "  0.0000000E+00 0.0000000E+00 -7.750473     -4.909721     0.0000000E+00 010100\n" +
                            " -0.5497040     0.8787257     0.0000000E+00 0.0000000E+00 0.3390456     001100\n" +
                            " -0.1730312     -1.703109     0.0000000E+00 0.0000000E+00 0.2530558     100001\n" +
                            "  0.6798751    -0.6600618     0.0000000E+00 0.0000000E+00 0.8021417     010001\n" +
                            "  0.0000000E+00 0.0000000E+00  1.670141     0.8172833     0.0000000E+00 001001\n" +
                            "  0.4489747      1.364433     0.0000000E+00 0.0000000E+00-0.5011185     000200\n" +
                            "  0.0000000E+00 0.0000000E+00  1.160715      1.552529     0.0000000E+00 000101\n" +
                            " -0.1256535     0.7288792E-01 0.0000000E+00 0.0000000E+00-0.4305713     000002\n" +
                            "  -8.277949    -0.4122981     0.0000000E+00 0.0000000E+00 -3.310619     300000\n" +
                            "  -47.15637     -12.78965     0.0000000E+00 0.0000000E+00 -22.32563     210000\n" +
                            "  -85.68644     -34.49853     0.0000000E+00 0.0000000E+00 -43.96547     120000\n" +
                            "  -50.29397     -25.28955     0.0000000E+00 0.0000000E+00 -27.18542     030000\n" +
                            "  0.0000000E+00 0.0000000E+00 -13.52184     -8.206529     0.0000000E+00 201000\n" +
                            "  0.0000000E+00 0.0000000E+00 -50.43417     -31.52856     0.0000000E+00 111000\n" +
                            "  0.0000000E+00 0.0000000E+00 -44.28252     -28.33633     0.0000000E+00 021000\n" +
                            "  -1.232532     0.4317126     0.0000000E+00 0.0000000E+00 -4.026448     102000\n" +
                            " -0.5934303      1.176024     0.0000000E+00 0.0000000E+00 -5.516581     012000\n" +
                            "  0.0000000E+00 0.0000000E+00 -5.629048     -3.873640     0.0000000E+00 003000\n" +
                            "  0.0000000E+00 0.0000000E+00 -11.36606     -9.950665     0.0000000E+00 200100\n" +
                            "  0.0000000E+00 0.0000000E+00 -39.22550     -32.69401     0.0000000E+00 110100\n" +
                            "  0.0000000E+00 0.0000000E+00 -33.42941     -27.34629     0.0000000E+00 020100\n" +
                            "   1.883009      2.213299     0.0000000E+00 0.0000000E+00-0.4683041E-01 101100\n" +
                            "   6.660377      5.685537     0.0000000E+00 0.0000000E+00  1.147078     011100\n" +
                            "  0.0000000E+00 0.0000000E+00 -14.35148     -11.71570     0.0000000E+00 002100\n" +
                            " -0.7190078E-01 -2.378831     0.0000000E+00 0.0000000E+00-0.4127860E-01 200001\n" +
                            "   7.613409     -2.010961     0.0000000E+00 0.0000000E+00  3.148928     110001\n" +
                            "   15.40793      6.014787     0.0000000E+00 0.0000000E+00  5.567830     020001\n" +
                            "  0.0000000E+00 0.0000000E+00  5.095699      1.078627     0.0000000E+00 101001\n" +
                            "  0.0000000E+00 0.0000000E+00  13.23337      5.502567     0.0000000E+00 011001\n" +
                            "   1.118270     0.3727395     0.0000000E+00 0.0000000E+00  1.139736     002001\n" +
                            "   1.570939      3.926049     0.0000000E+00 0.0000000E+00  3.736166     100200\n" +
                            "   4.382247      6.204219     0.0000000E+00 0.0000000E+00  4.710711     010200\n" +
                            "  0.0000000E+00 0.0000000E+00 -17.81088     -13.21678     0.0000000E+00 001200\n" +
                            "  0.0000000E+00 0.0000000E+00  10.37052      5.429736     0.0000000E+00 100101\n" +
                            "  0.0000000E+00 0.0000000E+00  18.03353      10.08844     0.0000000E+00 010101\n" +
                            "   2.738930      1.195821     0.0000000E+00 0.0000000E+00 0.4615742     001101\n" +
                            "  0.6557231     0.8101713     0.0000000E+00 0.0000000E+00-0.8026854E-01 100002\n" +
                            "  0.5838499      1.222052     0.0000000E+00 0.0000000E+00-0.5793204     010002\n" +
                            "  0.0000000E+00 0.0000000E+00 -1.465307    -0.3965939     0.0000000E+00 001002\n" +
                            "  0.0000000E+00 0.0000000E+00 -6.890473     -6.003457     0.0000000E+00 000300\n" +
                            "  0.8794123    -0.1803912     0.0000000E+00 0.0000000E+00 0.1465458     000201\n" +
                            "  0.0000000E+00 0.0000000E+00 -2.421142     -1.552995     0.0000000E+00 000102\n" +
                            "  0.7676515E-01-0.4175706E-01 0.0000000E+00 0.0000000E+00 0.3720152     000003\n" +
                            "  -13.66068      6.758755     0.0000000E+00 0.0000000E+00 -8.696300     400000\n" +
                            "  -141.7867      21.59412     0.0000000E+00 0.0000000E+00 -74.75084     310000\n" +
                            "  -451.6838      3.270362     0.0000000E+00 0.0000000E+00 -223.9268     220000\n" +
                            "  -583.0741     -46.75480     0.0000000E+00 0.0000000E+00 -285.3815     130000\n" +
                            "  -271.7774     -42.49136     0.0000000E+00 0.0000000E+00 -133.0940     040000\n" +
                            "  0.0000000E+00 0.0000000E+00 -28.58354     -7.029463     0.0000000E+00 301000\n" +
                            "  0.0000000E+00 0.0000000E+00 -178.0455     -62.55247     0.0000000E+00 211000\n" +
                            "  0.0000000E+00 0.0000000E+00 -335.7682     -137.3899     0.0000000E+00 121000\n" +
                            "  0.0000000E+00 0.0000000E+00 -203.8398     -91.71190     0.0000000E+00 031000\n" +
                            "  -10.24320      2.596222     0.0000000E+00 0.0000000E+00 -15.72681     202000\n" +
                            "  -33.81801      3.156892     0.0000000E+00 0.0000000E+00 -49.89204     112000\n" +
                            "  -29.38857     -1.702162     0.0000000E+00 0.0000000E+00 -39.49519     022000\n" +
                            "  0.0000000E+00 0.0000000E+00 -24.95955     -12.42596     0.0000000E+00 103000\n" +
                            "  0.0000000E+00 0.0000000E+00 -34.11050     -16.78011     0.0000000E+00 013000\n" +
                            "  -3.133979     -1.968322     0.0000000E+00 0.0000000E+00 -3.068885     004000\n" +
                            "  0.0000000E+00 0.0000000E+00 -21.48057     -10.88915     0.0000000E+00 300100\n" +
                            "  0.0000000E+00 0.0000000E+00 -118.3347     -75.66580     0.0000000E+00 210100\n" +
                            "  0.0000000E+00 0.0000000E+00 -212.1107     -154.1967     0.0000000E+00 120100\n" +
                            "  0.0000000E+00 0.0000000E+00 -126.9605     -98.97257     0.0000000E+00 030100\n" +
                            "   20.59085      8.710196     0.0000000E+00 0.0000000E+00 -10.51261     201100\n" +
                            "   65.87887      20.90597     0.0000000E+00 0.0000000E+00 -18.30166     111100\n" +
                            "   46.92017      10.20484     0.0000000E+00 0.0000000E+00 -5.993418     021100\n" +
                            "  0.0000000E+00 0.0000000E+00 -38.49202     -32.62699     0.0000000E+00 102100\n" +
                            "  0.0000000E+00 0.0000000E+00 -45.69407     -40.52617     0.0000000E+00 012100\n" +
                            "  -13.23931     -9.345806     0.0000000E+00 0.0000000E+00 -6.823600     003100\n" +
                            "  -4.744327     -3.936138     0.0000000E+00 0.0000000E+00-0.5011042E-01 300001\n" +
                            "   11.09104     -13.70026     0.0000000E+00 0.0000000E+00  15.88820     210001\n" +
                            "   80.85626      1.260510     0.0000000E+00 0.0000000E+00  58.79234     120001\n" +
                            "   78.83280      18.68190     0.0000000E+00 0.0000000E+00  52.18966     030001\n" +
                            "  0.0000000E+00 0.0000000E+00  6.677236     -2.215362     0.0000000E+00 201001\n" +
                            "  0.0000000E+00 0.0000000E+00  56.22280      13.51362     0.0000000E+00 111001\n" +
                            "  0.0000000E+00 0.0000000E+00  75.43522      30.06796     0.0000000E+00 021001\n" +
                            "   1.381262     -2.522591     0.0000000E+00 0.0000000E+00  9.226475     102001\n" +
                            "  0.9308951     -4.022466     0.0000000E+00 0.0000000E+00  15.70073     012001\n" +
                            "  0.0000000E+00 0.0000000E+00  9.495014      3.745527     0.0000000E+00 003001\n" +
                            "   29.22760      8.645307     0.0000000E+00 0.0000000E+00  3.780429     200200\n" +
                            "   81.06667      23.82483     0.0000000E+00 0.0000000E+00  21.97810     110200\n" +
                            "   49.91024      12.75223     0.0000000E+00 0.0000000E+00  22.55278     020200\n" +
                            "  0.0000000E+00 0.0000000E+00 -39.01469     -31.95807     0.0000000E+00 101200\n" +
                            "  0.0000000E+00 0.0000000E+00 -53.61549     -41.20963     0.0000000E+00 011200\n" +
                            "  -23.97162     -16.84423     0.0000000E+00 0.0000000E+00 -6.488187     002200\n" +
                            "  0.0000000E+00 0.0000000E+00  18.59950      5.325659     0.0000000E+00 200101\n" +
                            "  0.0000000E+00 0.0000000E+00  86.65812      39.92494     0.0000000E+00 110101\n" +
                            "  0.0000000E+00 0.0000000E+00  93.68775      52.01060     0.0000000E+00 020101\n" +
                            "  -2.590657     -4.056532     0.0000000E+00 0.0000000E+00  13.08195     101101\n" +
                            "  -13.15291     -9.433419     0.0000000E+00 0.0000000E+00  14.95396     011101\n" +
                            "  0.0000000E+00 0.0000000E+00  30.94796      18.31603     0.0000000E+00 002101\n" +
                            "   1.791105      1.004359     0.0000000E+00 0.0000000E+00 0.9633466     200002\n" +
                            " -0.6138355      3.354481     0.0000000E+00 0.0000000E+00 0.3494982     110002\n" +
                            "  -13.03032     -2.778224     0.0000000E+00 0.0000000E+00 -4.114203     020002\n" +
                            "  0.0000000E+00 0.0000000E+00 -3.163317     0.2183381     0.0000000E+00 101002\n" +
                            "  0.0000000E+00 0.0000000E+00 -12.55505     -3.217371     0.0000000E+00 011002\n" +
                            "  -1.197070    -0.3435709E-01 0.0000000E+00 0.0000000E+00 -1.970279     002002\n" +
                            "  0.0000000E+00 0.0000000E+00 -29.39768     -16.03591     0.0000000E+00 100300\n" +
                            "  0.0000000E+00 0.0000000E+00 -40.58278     -22.27666     0.0000000E+00 010300\n" +
                            "  -21.01585     -14.29057     0.0000000E+00 0.0000000E+00-0.2025000     001300\n" +
                            "  -6.509905     -3.438964     0.0000000E+00 0.0000000E+00 -1.597235     100201\n" +
                            "  -16.49497     -9.482918     0.0000000E+00 0.0000000E+00 -5.467730     010201\n" +
                            "  0.0000000E+00 0.0000000E+00  42.58478      26.77820     0.0000000E+00 001201\n" +
                            "  0.0000000E+00 0.0000000E+00 -11.56789     -3.867598     0.0000000E+00 100102\n" +
                            "  0.0000000E+00 0.0000000E+00 -25.78268     -10.92959     0.0000000E+00 010102\n" +
                            "  -4.421442     -1.169783     0.0000000E+00 0.0000000E+00 -2.675841     001102\n" +
                            " -0.8035210    -0.5007542     0.0000000E+00 0.0000000E+00-0.2186941E-01 100003\n" +
                            "  -1.568902     -1.305154     0.0000000E+00 0.0000000E+00 0.3284710     010003\n" +
                            "  0.0000000E+00 0.0000000E+00  1.353482     0.2848699     0.0000000E+00 001003\n" +
                            "  -6.110056     -4.141099     0.0000000E+00 0.0000000E+00  1.146121     000400\n" +
                            "  0.0000000E+00 0.0000000E+00  23.55815      14.73074     0.0000000E+00 000301\n" +
                            "  -2.656064    -0.6469026     0.0000000E+00 0.0000000E+00-0.4791406     000202\n" +
                            "  0.0000000E+00 0.0000000E+00  3.187220      1.486375     0.0000000E+00 000103\n" +
                            " -0.6434417E-01 0.2696139E-01 0.0000000E+00 0.0000000E+00-0.3335171     000004\n" +
                            "  -37.28654      10.84787     0.0000000E+00 0.0000000E+00 -16.00993     500000\n" +
                            "  -468.0473      65.26197     0.0000000E+00 0.0000000E+00 -217.4010     410000\n" +
                            "  -2052.894      128.8519     0.0000000E+00 0.0000000E+00 -967.3482     320000\n" +
                            "  -4171.305      63.44557     0.0000000E+00 0.0000000E+00 -1959.164     230000\n" +
                            "  -4051.396     -88.74176     0.0000000E+00 0.0000000E+00 -1886.898     140000\n" +
                            "  -1531.552     -87.65758     0.0000000E+00 0.0000000E+00 -707.5631     050000\n" +
                            "  0.0000000E+00 0.0000000E+00 -66.54883    -0.6720233     0.0000000E+00 401000\n" +
                            "  0.0000000E+00 0.0000000E+00 -604.4373     -77.67865     0.0000000E+00 311000\n" +
                            "  0.0000000E+00 0.0000000E+00 -1819.713     -348.3475     0.0000000E+00 221000\n" +
                            "  0.0000000E+00 0.0000000E+00 -2293.078     -538.2711     0.0000000E+00 131000\n" +
                            "  0.0000000E+00 0.0000000E+00 -1054.746     -287.2074     0.0000000E+00 041000\n" +
                            "  -69.33147     0.3227693     0.0000000E+00 0.0000000E+00 -54.14628     302000\n" +
                            "  -350.3483     -7.328767     0.0000000E+00 0.0000000E+00 -292.8817     212000\n" +
                            "  -583.7875     -27.70532     0.0000000E+00 0.0000000E+00 -506.4750     122000\n" +
                            "  -317.7277     -21.42195     0.0000000E+00 0.0000000E+00 -285.1524     032000\n" +
                            "  0.0000000E+00 0.0000000E+00 -105.6509     -29.77177     0.0000000E+00 203000\n" +
                            "  0.0000000E+00 0.0000000E+00 -331.1658     -100.5466     0.0000000E+00 113000\n" +
                            "  0.0000000E+00 0.0000000E+00 -261.2943     -85.58196     0.0000000E+00 023000\n" +
                            "  -11.26976      1.322543     0.0000000E+00 0.0000000E+00 -28.90279     104000\n" +
                            "  -13.61567      1.547823     0.0000000E+00 0.0000000E+00 -39.63738     014000\n" +
                            "  0.0000000E+00 0.0000000E+00 -16.71152     -7.973936     0.0000000E+00 005000\n" +
                            "  0.0000000E+00 0.0000000E+00 -46.50179     -4.959287     0.0000000E+00 400100\n" +
                            "  0.0000000E+00 0.0000000E+00 -393.3654     -102.0626     0.0000000E+00 310100\n" +
                            "  0.0000000E+00 0.0000000E+00 -1123.329     -402.4337     0.0000000E+00 220100\n" +
                            "  0.0000000E+00 0.0000000E+00 -1362.693     -597.2103     0.0000000E+00 130100\n" +
                            "  0.0000000E+00 0.0000000E+00 -613.0413     -313.3573     0.0000000E+00 040100\n" +
                            "  -6.379779      3.798302     0.0000000E+00 0.0000000E+00 -24.86221     301100\n" +
                            "   58.78163     -2.524014     0.0000000E+00 0.0000000E+00 -111.0602     211100\n" +
                            "   199.2024     -34.44068     0.0000000E+00 0.0000000E+00 -159.6637     121100\n" +
                            "   158.9514     -25.40601     0.0000000E+00 0.0000000E+00 -71.74247     031100\n" +
                            "  0.0000000E+00 0.0000000E+00 -128.6600     -63.69565     0.0000000E+00 202100\n" +
                            "  0.0000000E+00 0.0000000E+00 -347.5387     -194.8714     0.0000000E+00 112100\n" +
                            "  0.0000000E+00 0.0000000E+00 -257.9701     -165.4921     0.0000000E+00 022100\n" +
                            "  -26.70049     -2.977350     0.0000000E+00 0.0000000E+00 -78.56843     103100\n" +
                            "  -25.98344     -3.505287     0.0000000E+00 0.0000000E+00 -101.8235     013100\n" +
                            "  0.0000000E+00 0.0000000E+00 -58.01825     -36.96547     0.0000000E+00 004100\n" +
                            "  -21.78751     -8.236603     0.0000000E+00 0.0000000E+00 -5.904086     400001\n" +
                            "  -38.50164     -67.06227     0.0000000E+00 0.0000000E+00  23.12417     310001\n" +
                            "   267.0372     -146.3690     0.0000000E+00 0.0000000E+00  246.7123     220001\n" +
                            "   758.2426     -94.76214     0.0000000E+00 0.0000000E+00  511.4102     130001\n" +
                            "   530.2809      12.92029     0.0000000E+00 0.0000000E+00  322.7892     040001\n" +
                            "  0.0000000E+00 0.0000000E+00  6.608137     -9.770295     0.0000000E+00 301001\n" +
                            "  0.0000000E+00 0.0000000E+00  182.9074     0.5104398     0.0000000E+00 211001\n" +
                            "  0.0000000E+00 0.0000000E+00  567.3391      110.6805     0.0000000E+00 121001\n" +
                            "  0.0000000E+00 0.0000000E+00  466.7316      133.9861     0.0000000E+00 031001\n" +
                            "   23.43852     -6.677633     0.0000000E+00 0.0000000E+00  30.21730     202001\n" +
                            "   95.12902     -18.04163     0.0000000E+00 0.0000000E+00  128.0770     112001\n" +
                            "   98.92940     -5.945630     0.0000000E+00 0.0000000E+00  126.5117     022001\n" +
                            "  0.0000000E+00 0.0000000E+00  55.67828      14.14966     0.0000000E+00 103001\n" +
                            "  0.0000000E+00 0.0000000E+00  93.47076      27.08239     0.0000000E+00 013001\n" +
                            "   5.428181     0.8740335     0.0000000E+00 0.0000000E+00  9.899212     004001\n" +
                            "   66.22865      8.760585     0.0000000E+00 0.0000000E+00  24.95395     300200\n" +
                            "   380.3902      32.87815     0.0000000E+00 0.0000000E+00  124.4588     210200\n" +
                            "   657.7592      30.42475     0.0000000E+00 0.0000000E+00  200.9062     120200\n" +
                            "   365.7354      7.808083     0.0000000E+00 0.0000000E+00  108.5589     030200\n" +
                            "  0.0000000E+00 0.0000000E+00 -26.92157     -34.47891     0.0000000E+00 201200\n" +
                            "  0.0000000E+00 0.0000000E+00 -90.63013     -98.54606     0.0000000E+00 111200\n" +
                            "  0.0000000E+00 0.0000000E+00 -116.9194     -103.6270     0.0000000E+00 021200\n" +
                            "  -42.94064     -15.38213     0.0000000E+00 0.0000000E+00 -121.5611     102200\n" +
                            "  -42.59375     -18.17921     0.0000000E+00 0.0000000E+00 -162.9470     012200\n" +
                            "  0.0000000E+00 0.0000000E+00 -99.49936     -72.82347     0.0000000E+00 003200\n" +
                            "  0.0000000E+00 0.0000000E+00  32.57931     -2.044671     0.0000000E+00 300101\n" +
                            "  0.0000000E+00 0.0000000E+00  278.6512      61.69763     0.0000000E+00 210101\n" +
                            "  0.0000000E+00 0.0000000E+00  649.4510      241.9927     0.0000000E+00 120101\n" +
                            "  0.0000000E+00 0.0000000E+00  466.5434      220.8939     0.0000000E+00 030101\n" +
                            "   19.96476     -18.47680     0.0000000E+00 0.0000000E+00  54.33428     201101\n" +
                            "   15.33809     -49.46745     0.0000000E+00 0.0000000E+00  164.7419     111101\n" +
                            "  -1.568150     -20.78403     0.0000000E+00 0.0000000E+00  122.2338     021101\n" +
                            "  0.0000000E+00 0.0000000E+00  131.3429      63.81746     0.0000000E+00 102101\n" +
                            "  0.0000000E+00 0.0000000E+00  182.7631      98.65368     0.0000000E+00 012101\n" +
                            "   29.33244      11.14425     0.0000000E+00 0.0000000E+00  31.67292     003101\n" +
                            "   10.18798      1.493037     0.0000000E+00 0.0000000E+00  3.334628     300002\n" +
                            "   33.99746      14.38244     0.0000000E+00 0.0000000E+00  2.371606     210002\n" +
                            "  -15.48267      16.00537     0.0000000E+00 0.0000000E+00 -42.53988     120002\n" +
                            "  -66.39784     -6.776339     0.0000000E+00 0.0000000E+00 -61.58999     030002\n" +
                            "  0.0000000E+00 0.0000000E+00  2.105451      4.334467     0.0000000E+00 201002\n" +
                            "  0.0000000E+00 0.0000000E+00 -38.04158     0.5892189     0.0000000E+00 111002\n" +
                            "  0.0000000E+00 0.0000000E+00 -85.85462     -23.28884     0.0000000E+00 021002\n" +
                            "  -1.008983      3.819581     0.0000000E+00 0.0000000E+00 -13.38640     102002\n" +
                            "  0.3646469      7.941824     0.0000000E+00 0.0000000E+00 -27.22268     012002\n" +
                            "  0.0000000E+00 0.0000000E+00 -14.32216     -4.213239     0.0000000E+00 003002\n" +
                            "  0.0000000E+00 0.0000000E+00 -11.90949     -7.887400     0.0000000E+00 200300\n" +
                            "  0.0000000E+00 0.0000000E+00 -81.22219     -30.90919     0.0000000E+00 110300\n" +
                            "  0.0000000E+00 0.0000000E+00 -113.5680     -46.52855     0.0000000E+00 020300\n" +
                            "  -38.68586     -17.39990     0.0000000E+00 0.0000000E+00 -93.50347     101300\n" +
                            "  -35.63521     -19.54570     0.0000000E+00 0.0000000E+00 -121.6196     011300\n" +
                            "  0.0000000E+00 0.0000000E+00 -103.8690     -81.81958     0.0000000E+00 002300\n" +
                            "  -32.30193     -14.20938     0.0000000E+00 0.0000000E+00  7.667042     200201\n" +
                            "  -146.6572     -44.75178     0.0000000E+00 0.0000000E+00 -9.876297     110201\n" +
                            "  -117.3073     -22.16771     0.0000000E+00 0.0000000E+00 -33.11969     020201\n" +
                            "  0.0000000E+00 0.0000000E+00  101.4065      81.03437     0.0000000E+00 101201\n" +
                            "  0.0000000E+00 0.0000000E+00  148.2376      116.7984     0.0000000E+00 011201\n" +
                            "   63.58855      30.58550     0.0000000E+00 0.0000000E+00  41.77197     002201\n" +
                            "  0.0000000E+00 0.0000000E+00 -16.05728     0.3853712     0.0000000E+00 200102\n" +
                            "  0.0000000E+00 0.0000000E+00 -112.7962     -29.72984     0.0000000E+00 110102\n" +
                            "  0.0000000E+00 0.0000000E+00 -157.4083     -63.37638     0.0000000E+00 020102\n" +
                            " -0.6768150      9.916750     0.0000000E+00 0.0000000E+00 -32.46106     101102\n" +
                            "   16.50187      20.91214     0.0000000E+00 0.0000000E+00 -48.70486     011102\n" +
                            "  0.0000000E+00 0.0000000E+00 -54.79222     -25.55690     0.0000000E+00 002102\n" +
                            "  -2.010486    -0.3382549     0.0000000E+00 0.0000000E+00 -1.482332     200003\n" +
                            "  -4.041383     -2.633785     0.0000000E+00 0.0000000E+00 -3.866275     110003\n" +
                            "   9.547269      1.702435     0.0000000E+00 0.0000000E+00  1.162527     020003\n" +
                            "  0.0000000E+00 0.0000000E+00  1.712057    -0.5455678     0.0000000E+00 101003\n" +
                            "  0.0000000E+00 0.0000000E+00  10.65156      1.625643     0.0000000E+00 011003\n" +
                            "   1.286890    -0.1139596     0.0000000E+00 0.0000000E+00  2.858169     002003\n" +
                            "  -12.71206     -5.610308     0.0000000E+00 0.0000000E+00 -17.36428     100400\n" +
                            "  -7.402116     -4.346635     0.0000000E+00 0.0000000E+00 -20.64077     010400\n" +
                            "  0.0000000E+00 0.0000000E+00 -75.80973     -54.20856     0.0000000E+00 001400\n" +
                            "  0.0000000E+00 0.0000000E+00  63.49237      41.35087     0.0000000E+00 100301\n" +
                            "  0.0000000E+00 0.0000000E+00  108.2306      64.11881     0.0000000E+00 010301\n" +
                            "   67.29107      33.49141     0.0000000E+00 0.0000000E+00  21.01154     001301\n" +
                            "   7.940403      6.337301     0.0000000E+00 0.0000000E+00 -8.516353     100202\n" +
                            "   30.37643      17.00398     0.0000000E+00 0.0000000E+00 -5.567198     010202\n" +
                            "  0.0000000E+00 0.0000000E+00 -76.83466     -43.97510     0.0000000E+00 001202\n" +
                            "  0.0000000E+00 0.0000000E+00  11.35432      2.651202     0.0000000E+00 100103\n" +
                            "  0.0000000E+00 0.0000000E+00  30.50431      10.11807     0.0000000E+00 010103\n" +
                            "   6.057839     0.8305106     0.0000000E+00 0.0000000E+00  6.003951     001103\n" +
                            "  0.8335877     0.3607478     0.0000000E+00 0.0000000E+00 0.7761450E-01 100004\n" +
                            "   2.365720      1.325225     0.0000000E+00 0.0000000E+00-0.8455231E-01 010004\n" +
                            "  0.0000000E+00 0.0000000E+00 -1.308682    -0.2509868     0.0000000E+00 001004\n" +
                            "  0.0000000E+00 0.0000000E+00 -28.65181     -17.45339     0.0000000E+00 000500\n" +
                            "   25.93174      12.65986     0.0000000E+00 0.0000000E+00-0.2932409     000401\n" +
                            "  0.0000000E+00 0.0000000E+00 -47.11252     -26.69169     0.0000000E+00 000302\n" +
                            "   4.872777      1.060837     0.0000000E+00 0.0000000E+00  1.924310     000203\n" +
                            "  0.0000000E+00 0.0000000E+00 -3.783232     -1.461521     0.0000000E+00 000104\n" +
                            "  0.6690912E-01-0.1783390E-01 0.0000000E+00 0.0000000E+00 0.3088518     000005"
            );
        }


        private CosyArbitraryOrder.CosyMapArbitraryOrder map;

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

    private final double Bp = 2.43213; // 磁钢度

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
