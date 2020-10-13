package cn.edu.hust.zrx.cct.study.Z验证性研究;


import cn.edu.hust.zrx.cct.Logger;
import cn.edu.hust.zrx.cct.advanced.MathFunction;
import cn.edu.hust.zrx.cct.advanced.comsol.HexahedronModel;
import cn.edu.hust.zrx.cct.base.BaseUtils;
import cn.edu.hust.zrx.cct.base.cct.SoleLayerCct;
import cn.edu.hust.zrx.cct.base.line.Trajectory;
import cn.edu.hust.zrx.cct.base.line.TrajectoryFactory;
import cn.edu.hust.zrx.cct.base.point.Point2;
import cn.edu.hust.zrx.cct.base.point.Point3;
import cn.edu.hust.zrx.cct.base.point.Point3Function;
import cn.edu.hust.zrx.cct.base.python.Plot2d;
import cn.edu.hust.zrx.cct.base.python.Plot3d;
import cn.edu.hust.zrx.cct.base.vector.Vector2;
import cn.edu.hust.zrx.cct.base.vector.Vector3Function;

import java.util.List;
import java.util.stream.Collectors;

import static cn.edu.hust.zrx.cct.base.BaseUtils.Constant.*;
import static java.lang.Math.*;

/**
 * Description
 * G01李纯漪直线机架研究
 * <p>
 * Data
 * 17:44
 *
 * @author zrx
 * @version 1.0
 */

public class G01李纯漪直线机架研究 {

    int windingNumber = 8;

    int numberPerWinding = 90;

    double tiltAngle = 30.;
    double tilt = BaseUtils.Converter.angleToRadian(tiltAngle);

    double distancePerWinding = 0.6 * CM;

    double gap = distancePerWinding;

    double smallR = 4 * CM;

    double depth = 0.5 * CM;

    double width = 0.2 * CM;

    public static void main(String[] args) {
        new G01李纯漪直线机架研究().test();
//        new G01李纯漪直线机架研究().test2();
//        new G01李纯漪直线机架研究().test3();

    }

    private void test3() {
        // cct 路径
        MathFunction xFun = theta -> smallR * cos(theta);
        MathFunction yFun = theta -> gap * theta / (2 * PI) + smallR * (1 / tan(tilt)) * sin(theta);
        MathFunction zFun = theta -> smallR * sin(theta);

        Point3Function cctPath = Point3Function.create(xFun, yFun, zFun);

        // 切向
        Vector3Function tangentialDirection = cctPath.differential(1e-6).normal();

        // 主法向
        Vector3Function mainNormalDirection = Vector3Function.create(Math::cos, x -> 0., Math::sin).normal();

        // 副法线方向
        Vector3Function secondNormalDirection = tangentialDirection.cross(mainNormalDirection);

        Point3Function cctPath1 = cctPath.add(mainNormalDirection.dot(depth / 2)).add(secondNormalDirection.dot(width / 2));
        Point3Function cctPath2 = cctPath.add(mainNormalDirection.dot(depth / 2).reverse()).add(secondNormalDirection.dot(width / 2));
        Point3Function cctPath3 = cctPath.add(mainNormalDirection.dot(depth / 2).reverse()).add(secondNormalDirection.dot(width / 2).reverse());
        Point3Function cctPath4 = cctPath.add(mainNormalDirection.dot(depth / 2)).add(secondNormalDirection.dot(width / 2).reverse());

        HexahedronModel straightCct = HexahedronModel.createFirst("straightCct",
                cctPath1.disperse(0, windingNumber * 2 * PI, numberPerWinding * 8),
                cctPath2.disperse(0, windingNumber * 2 * PI, numberPerWinding * 8),
                cctPath3.disperse(0, windingNumber * 2 * PI, numberPerWinding * 8),
                cctPath4.disperse(0, windingNumber * 2 * PI, numberPerWinding * 8)
        );


        String[] strings = straightCct.hexNames();

        String names = BaseUtils.JavaCoder.create("hexNames", strings);
        Logger.getLogger().info("names = " + names);

        double[][][] arr = straightCct.dataDoubleArrArrArr();
        String sa = BaseUtils.JavaCoder.create("arr",arr);
        Logger.getLogger().info("sa = " + sa);
    }

    private void test2() {
        // 直线 CCT
        MathFunction xFun = theta -> smallR * cos(theta);
        MathFunction yFun = theta -> gap * theta / (2 * PI) + smallR * (1 / tan(tilt)) * sin(theta);
        MathFunction zFun = theta -> smallR * sin(theta);

        List<Point3> winding = BaseUtils.Python.linspaceStream(0, 8 * 2 * PI, 360 * 8)
                .mapToObj(theta -> Point3.create(xFun.apply(theta), yFun.apply(theta), zFun.apply(theta)))
                .collect(Collectors.toList());


        MathFunction xpFun = xFun.differential(1e-6);
        MathFunction ypFun = yFun.differential(1e-6);
        MathFunction zpFun = zFun.differential(1e-6);

        // 切向   (xp,yp,zp)
        Vector3Function cutVF = Vector3Function.create(xpFun, ypFun, zpFun).normal();
        // 主法向 (cost,0,sint)
        Vector3Function pVFMain = Vector3Function.create(Math::cos, x -> 1., Math::sin).normal();
        // 副法线方向
        Vector3Function pvF2 = cutVF.cross(pVFMain).normal();


        HexahedronModel straightCct = HexahedronModel.create(
                "straightCct", 1, 1,
                Point3.create(
                        theta -> smallR * cos(theta),
                        theta -> gap * theta / (2 * PI) + smallR * (1 / tan(tilt)) * sin(theta),
                        theta -> smallR * sin(theta),
                        BaseUtils.Python.linspaceStream(0, windingNumber * 2 * PI, numberPerWinding * 8)
                ), Point3.create(
                        theta -> smallR * cos(theta),
                        theta -> gap * theta / (2 * PI) + smallR * (1 / tan(tilt)) * sin(theta),
                        theta -> smallR * sin(theta),
                        BaseUtils.Python.linspaceStream(0, windingNumber * 2 * PI, numberPerWinding * 8)
                ), Point3.create(
                        theta -> smallR * cos(theta),
                        theta -> gap * theta / (2 * PI) + smallR * (1 / tan(tilt)) * sin(theta),
                        theta -> smallR * sin(theta),
                        BaseUtils.Python.linspaceStream(0, windingNumber * 2 * PI, numberPerWinding * 8)
                ), Point3.create(
                        theta -> smallR * cos(theta),
                        theta -> gap * theta / (2 * PI) + smallR * (1 / tan(tilt)) * sin(theta),
                        theta -> smallR * sin(theta),
                        BaseUtils.Python.linspaceStream(0, windingNumber * 2 * PI, numberPerWinding * 8)
                )
        );


    }

    public void test() {
        // 直线 CCT
        MathFunction xFun = theta -> smallR * cos(theta);
        MathFunction yFun = theta -> gap * theta / (2 * PI) + smallR * (1 / tan(tilt)) * sin(theta);
        MathFunction zFun = theta -> smallR * sin(theta);

        List<Point3> winding = BaseUtils.Python.linspaceStream(0, 8 * 2 * PI, 360 * 8)
                .mapToObj(theta -> Point3.create(xFun.apply(theta), yFun.apply(theta), zFun.apply(theta)))
                .collect(Collectors.toList());

        Plot3d.plot3(winding);

        Plot3d.showThread();

        SoleLayerCct soleLayerCct = new SoleLayerCct(winding, 7000);

        Trajectory trajectory = TrajectoryFactory.setStartingPoint(0, -0.1).setStraightLine(0.25, Vector2.yDirect());

        List<Point2> b = trajectory.dispersePoint3sWithDistance(MM)
                .stream().map(pd -> Point2.create(pd.getDistance(), soleLayerCct.magnetAt(pd.getPoint3()).length()))
                .collect(Collectors.toList());

        Plot2d.plot2(b);

        Plot2d.showThread();
    }

}
