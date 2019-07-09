package zrx.CCT.ConcreteCCT;

import zrx.base.Vector3d;

import java.util.ArrayList;
import java.util.List;

/**
 * 把一层的CCT全部放进来
 * 多亏了接口 SingleLayerDiscreteCCTComponent 把
 * DiscreteCCT cct离散类，以及 ConnectionSegmentOfCCT cct连接段类 都放了进来
 */

public class SingleLayerDiscreteCCTs {
    public List<Vector3d[]> cctPathsList;
    public double I;

    public SingleLayerDiscreteCCTs(double I,SingleLayerDiscreteCCTComponent...ccts){
        this.I = I;
        this.cctPathsList = new ArrayList<>();
        for (int i = ccts.length - 1; i >= 0; i--) {
            cctPathsList.add(ccts[i].getPathInXYZ());
        }
    }
}
