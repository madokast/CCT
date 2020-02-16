package zrx.CCT.ConcreteCCT;

import zrx.base.point.Vector2d;
import zrx.base.point.Vector3d;

/**
 * 为了计算的方便，需要把 DiscreteCCT cct离散类，以及 ConnectionSegmentOfCCT cct连接段类组合起来
 * 两者都有 getPathInXYZ() 方法 正好去实现这个接口
 * 此类主要是为了 SingleLayerDiscreteCCTs 类的构造器的方便
 */


public interface SingleLayerDiscreteCCTComponent {
    Vector3d[] getPathInXYZ();
    Vector2d[] getPathInKsiPhi();
}
