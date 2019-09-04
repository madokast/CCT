package zrx.CCT;

import zrx.CCT.ConcreteCCT.AllCCTs;
import zrx.Tools.*;
import zrx.Tools.python.ArrayReverse;
import zrx.base.line2d.Trajectory;
import zrx.base.point.Vector2d;
import zrx.base.point.Vector2dTo3d;
import zrx.base.point.Vector3d;

import java.io.File;
import java.io.FileWriter;
import java.util.function.DoubleFunction;

/**
 * CCT 分析工具
 */

public class CCTUtils {
    private CCTUtils() {
    }

    /**
     * cct磁场分布于给定的点集Vector3d[] trajectory
     * 返回z方向的磁场分布，配套double[] xArray里面的自变量
     * <p>
     * 一般Vector3d[] trajectory即理想轨迹
     * double[] xArray即路程
     * <p>
     * 没有画图，自己拿到返回值自己画图
     *
     * @param allCCTs    cct
     * @param trajectory 点集，将会计算每个点的磁场
     * @param xArray     自变量，将上述点集和一个标量集映射
     * @return 分布distribution
     */
    public static Vector2d[] magneticDistributionZ(AllCCTs allCCTs, Vector3d[] trajectory, double[] xArray) {
        Equal.requireEqual(trajectory.length, xArray.length);

        final Vector2d[] distribution = new Vector2d[trajectory.length];
        for (int i = 0; i < trajectory.length; i++) {
            distribution[i] = Vector2d.getOne(
                    xArray[i],
                    allCCTs.magnet(trajectory[i]).z
            );
        }

        return distribution;
    }

    //上函数的无xArray版本，xArray由trajectory路程计算出，并加上offset
    public static Vector2d[] magneticDistributionZ(AllCCTs allCCTs, Vector3d[] trajectory, double offset) {
        double[] xArray = new double[trajectory.length];
        xArray[0] = offset;
        for (int i = 1; i < trajectory.length; i++) {
            xArray[i] = xArray[i - 1] + Vector3d.subtract(trajectory[i], trajectory[i - 1]).length();
        }

        return magneticDistributionZ(allCCTs, trajectory, xArray);
    }

    /**
     * 多级场分布。
     * !!注意：trajectory只能分布于XY平面
     *
     * @param allCCTs      cct
     * @param halfGoodArea 半好场区
     * @param degree       0-二极场 1-四极场，等等
     * @param trajectory   轨迹
     * @param xArray       自变量，仅仅用于返回，好画图
     * @return 多级场分布
     */
    public static Vector2d[] magneticComponent0123(AllCCTs allCCTs, double halfGoodArea, int degree, Vector3d[] trajectory, double[] xArray) {
        final double[][] component0123Array = magneticComponent0123Array(allCCTs, halfGoodArea, trajectory);
        final Vector2d[] ans = new Vector2d[component0123Array.length];
        for (int i = 0; i < component0123Array.length; i++) {
            ans[i] = Vector2d.getOne(xArray[i], component0123Array[i][degree]);
        }

        return ans;


        // if 中为 旧实现  . 2019年8月29日 测试基本通过
//        if(false){
//            Equal.requireEqual(trajectory.length, xArray.length);
//
//            final Vector2d[] components = new Vector2d[trajectory.length - 1];
//            Vector2d directPrevious = null;
//            for (int i = 0; i < trajectory.length - 1; i++) {
//                Vector2d direct = Vector3d.vector3dTo2d(Vector3d.subtract(trajectory[i + 1], trajectory[i]));
//
//                if (Equal.isEqual(direct, Vector2d.getZero())) {
//                    direct = directPrevious;
//                } else {
//                    directPrevious = direct;
//                }
//
//                Vector2d tangentAntiClock = Vector2d.copyOne(direct).rotateSelfAndReturn(AngleToRadian.to(90));
//                Vector2d tangentClock = Vector2d.copyOne(direct).rotateSelfAndReturn(AngleToRadian.to(-90));
//
//                Vector3d antiClockPoint = Vector3d.move(
//                        Vector3d.copyOne(trajectory[i]), Vector3d.vector2dTo3d(tangentAntiClock), halfGoodArea
//                );
//
//                Vector3d clockPoint = Vector3d.move(
////                    Vector3d.copyOne(trajectory[i]), Vector3d.vector2dTo3d(tangentClock), halfGoodArea
//                        Vector3d.copyOne(trajectory[i]), Vector3d.vector2dTo3d(tangentAntiClock), -halfGoodArea
//                );
//
//                components[i] = Vector2d.getByStartAndEnd(
//                        xArray[i],
//                        allCCTs.magneticComponent0123(clockPoint, antiClockPoint)[degree]
//                );
//
//            }
//
//
//            return components;
//        }

        // end if

        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////

//        double inner = R_beamline - GOOD_FIELD_AREA / 100.0;
//        double outer = R_beamline + GOOD_FIELD_AREA / 100.0;
//
//        double[] componentList = new double[trajectory.length];
//        for (int i = 0; i < trajectory.length; i++) {
//            final Vector3d innerP = Vector3d.getByStartAndEnd(Vector2d.getOneByLength(Vector3d.vector3dTo2d(trajectory[i]), inner));
//            final Vector3d outerP = Vector3d.getByStartAndEnd(Vector2d.getOneByLength(Vector3d.vector3dTo2d(trajectory[i]), outer));
//
//            componentList[i] = allCCTs.magneticComponent0123(innerP, outerP)[degree];
//        }
//
//        final Vector2d[] ans = Vector2d.arrayToVector2ds(ListToArray.doubleListToArray(angList), componentList);
    }

    public static double[][] magneticComponent0123Array(AllCCTs allCCTs, double halfGoodArea, Vector3d[] trajectory) {
        double[][] components = new double[trajectory.length - 1][];
        Vector2d directPrevious = null;
        for (int i = 0; i < components.length; i++) {
            Vector2d direct = Vector3d.vector3dTo2d(Vector3d.subtract(trajectory[i + 1], trajectory[i]));

            if (Equal.isEqual(direct, Vector2d.getZero())) {
                direct = directPrevious;
            } else {
                directPrevious = direct;
            }

            Vector2d tangentAntiClock = Vector2d.copyOne(direct).rotateSelfAndReturn(AngleToRadian.to(90));
            Vector2d tangentClock = Vector2d.copyOne(direct).rotateSelfAndReturn(AngleToRadian.to(-90));

            Vector3d antiClockPoint = Vector3d.move(
                    Vector3d.copyOne(trajectory[i]), Vector3d.vector2dTo3d(tangentAntiClock), halfGoodArea
            );

            Vector3d clockPoint = Vector3d.move(
                    //                    Vector3d.copyOne(trajectory[i]), Vector3d.vector2dTo3d(tangentClock), halfGoodArea
                    Vector3d.copyOne(trajectory[i]), Vector3d.vector2dTo3d(tangentAntiClock), -halfGoodArea
            );

            components[i] = allCCTs.magneticComponent0123(clockPoint, antiClockPoint);
        }

        return components;


        //Equal.requireEqual(trajectory.length, xArray.length);
        //
        //        final Vector2d[] components = new Vector2d[trajectory.length - 1];
        //        Vector2d directPrevious = null;
        //        for (int i = 0; i < trajectory.length - 1; i++) {
        //            Vector2d direct = Vector3d.vector3dTo2d(Vector3d.subtract(trajectory[i + 1], trajectory[i]));
        //
        //            if (Equal.isEqual(direct, Vector2d.getZero())) {
        //                direct = directPrevious;
        //            } else {
        //                directPrevious = direct;
        //            }
        //
        //            Vector2d tangentAntiClock = Vector2d.copyOne(direct).rotateSelfAndReturn(AngleToRadian.to(90));
        //            Vector2d tangentClock = Vector2d.copyOne(direct).rotateSelfAndReturn(AngleToRadian.to(-90));
        //
        //            Vector3d antiClockPoint = Vector3d.move(
        //                    Vector3d.copyOne(trajectory[i]), Vector3d.vector2dTo3d(tangentAntiClock), halfGoodArea
        //            );
        //
        //            Vector3d clockPoint = Vector3d.move(
        ////                    Vector3d.copyOne(trajectory[i]), Vector3d.vector2dTo3d(tangentClock), halfGoodArea
        //                    Vector3d.copyOne(trajectory[i]), Vector3d.vector2dTo3d(tangentAntiClock), -halfGoodArea
        //            );
        //
        //            components[i] = Vector2d.getByStartAndEnd(
        //                    xArray[i],
        //                    allCCTs.magneticComponent0123(clockPoint, antiClockPoint)[degree]
        //            );
        //
        //        }
        //
        //
        //        return components;


    }

    public static void component0123ArrayTOCosyCode(double[][] components, double[] xArray, final double Bp) {
        Equal.requireEqual(components.length, xArray.length - 1);
        System.out.println("components.length = " + components.length);

        double B = 0.0; // 二极场大小
        double n = 0.0; // 归一化梯度 n = 梯度/磁刚度
        double L = 0.0; // 元件长度
        double r = 0.0; // 偏转半径
        double Ra = 0.0; // 偏转角度

        for (int i = 0; i < components.length; i++) {
            L = xArray[i + 1] - xArray[i];

            B = components[i][0];
            n = -components[i][1] / Bp;

            r = Bp / B;
            Ra = AngleToRadian.reverse(L / r);


            if (r < 0) {
                //CB ; MS R1 ABS(CD13Ag) HA_B1 CD2n 0 0 0 0; CB ; {/CD1/}
                System.out.println("{" + xArray[i] + "} CB ; MS " + Math.abs(r) + "*ri " + Math.abs(Ra) + " " + 0.04 + " " + n + "*ni 0 0 0 0 ; CB ;");
            } else {
                ////MS r Ra  halfA n 0 0 0 0;
                System.out.println("{" + xArray[i] + "} MS " + r + "*ri " + Ra + " " + 0.04 + " " + n + "*ni 0 0 0 0 ;");
            }

        }

    }

    public static double[] magneticComponent0123(AllCCTs allCCTs, double halfGoodArea, Vector3d startPoint, Vector3d endPoint) {
        return allCCTs.magneticComponent0123(startPoint, endPoint);
    }

    /**
     * 获取enge函数。大工程
     *
     * @param allCCTs             全部cct
     * @param trajectory          轨道，计算其上的磁场
     * @param xArray              自变量，一般时路程
     * @param maxValue            磁场最大值，如2.43，用于归一化
     * @param sharpCutoffLocation 硬边模型位置，用于归一化中的x移动
     * @param fullAperture        孔径，用于归一化。-3到5倍孔径
     * @param needRervse          是否需要反转，如果是出口边缘函数，则和归一化的情况一致，就不需要
     * @param degree              0-二极场 1-四极场
     * @return engeFun系数
     */
    public static double[] engeFunction(AllCCTs allCCTs, Vector3d[] trajectory, double[] xArray, final double maxValue,
                                        double sharpCutoffLocation, final double fullAperture, boolean needRervse, int degree) {
        System.err.println("警告：engeFunction函数可能很难实现你想要的功能");


        final Vector2d[] original = magneticComponent0123(allCCTs, 30.0 / 1000, degree, trajectory, xArray);


        final Vector2d[] convicted = Vector2d.convict(original,
                new DoubleFunction<Double>() {
                    @Override
                    public Double apply(double x) {
                        return (x - sharpCutoffLocation) / fullAperture;
                    }
                },
                new DoubleFunction<Double>() {
                    @Override
                    public Double apply(double y) {
                        y = y / maxValue;


                        if (y > 1.0) return 1.0;
                        else if (y < 0) return 0.0;
                        else return y;
                    }
                }
        );

        PrintArray.print(convicted);
        System.out.println("-------");

        final Vector2d[] selected = Vector2d.select(convicted, v -> v.x >= -3.0 && v.x <= 3);

//        final Vector2d[] monotony1 = Vector2d.forceMonotony(selected, true);

//        final Vector2d[] monotony1 = Vector2d.forceMonotony(selected, needRervse);

//        PrintArray.print(monotony1);
//        System.out.println("-------");

        final Vector2d[] reversed = ArrayReverse.reverse(selected, true);


        PrintArray.print(reversed);
        System.out.println("-------");

        final Vector2d[] monotony = Vector2d.forceMonotony(reversed, true);

        PrintArray.print(monotony);
        System.out.println("-------");


//        final Vector2d[] reversed1 = ArrayReverse.reverse(monotony, true);

//        PrintArray.print(monotony);
//        System.out.println("-------");

        final double[] engeFunction = Fit.fitEngeFunction(monotony);

        return engeFunction;
//        return null;
    }

    public static void COSYPart(AllCCTs allCCTs, Vector3d[] trajectory) {
        //TODO cosy切片分析
    }

    public static void getHKL(AllCCTs allCCTs, Trajectory trajectory, Vector2dTo3d vector2dTo3d, double goodField, File outFile) {
        //输出文件格式
        //距离/mm h k l \r\n
        //每个量之间用空格隔开

        final double STEP  = 2.0 / 1000; //2mm

        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(outFile,false);//重写
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }

        final double[] stepToEnd = Numpy.stepToEnd(STEP, trajectory.getLength());
        for (int i = 0; i < stepToEnd.length; i++) {
            double s = stepToEnd[i];
            final double[] component0123 = allCCTs.magneticComponent0123(
                    vector2dTo3d.functioon(trajectory.rightHandSidePoint(s, goodField)),
                    vector2dTo3d.functioon(trajectory.leftHandSidePoint(s, goodField)));
            double h = 
        }


    }
}
