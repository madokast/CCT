package cn.edu.hust.zrx.cct.advanced.comsol;

import cn.edu.hust.zrx.cct.Logger;
import cn.edu.hust.zrx.cct.base.BaseUtils;
import cn.edu.hust.zrx.cct.base.point.Point3;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class HexahedronModelTest {


    public static void main(String[] args) {
        List<Point3> l0 = BaseUtils.Python.linspaceStream(Point3.create(0, 0, 0), Point3.create(-1,-1,10), 10).collect(Collectors.toList());
        List<Point3> l1 = BaseUtils.Python.linspaceStream(Point3.create(1, 0, 0), Point3.create(2,-1,10), 10).collect(Collectors.toList());
        List<Point3> l2 = BaseUtils.Python.linspaceStream(Point3.create(1, 1, 0), Point3.create(2,2,10), 10).collect(Collectors.toList());
        List<Point3> l3 = BaseUtils.Python.linspaceStream(Point3.create(0, 1, 0), Point3.create(-1,2,10), 10).collect(Collectors.toList());

        HexahedronModel test = HexahedronModel.create("CCT",1,1, l0, l1, l2, l3);

        Logger.getLogger().info("test = \n" + test);
    }

}