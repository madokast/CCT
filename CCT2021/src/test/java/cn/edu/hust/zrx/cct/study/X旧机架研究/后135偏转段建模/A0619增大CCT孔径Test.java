package cn.edu.hust.zrx.cct.study.X旧机架研究.后135偏转段建模;

import cn.edu.hust.zrx.cct.base.point.Point2;
import cn.edu.hust.zrx.cct.base.python.Plot2d;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

class A0619增大CCT孔径Test {

    public static void main(String[] args) {
        A0619增大CCT孔径 a0619增大CCT孔径 = new A0619增大CCT孔径();

        double[][] doubles = a0619增大CCT孔径.sphericalUniformDistribution球面(200, 2);

        List<Point2> collect = Arrays.stream(doubles).map(d -> Point2.create(d[0], d[1])).collect(Collectors.toList());

        Plot2d.plot2(collect,Plot2d.BLACK_POINT);

        Plot2d.showThread();
    }
}