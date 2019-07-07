package zrx.CCT;

import zrx.CCT.CCT;
import zrx.python.Plot3d;


/**
 * 和CCT有关的plot
 * 新建一个类 减少耦合~~~
 * <p>
 * plot 各种东西后
 * CCTPlot.setCube(1.5);
 * CCTPlot.show();
 */

public class CCTPlot extends Plot3d {
    public static final String DescribeForStartPoint = ",'r+'";
    public static final String DescribeForEndPoint = ",'k+'";

    //以下全部作废 2019年7月7日

    @Deprecated
    public static void plotCCT(CCT cct, String describe) {
        Plot3d.plot3(cct.pathForPlot3(), describe);
    }

    @Deprecated
    public static void plotCCT(CCT cct) {
        Plot3d.plot3(cct.pathForPlot3(), "");
    }

    @Deprecated
    public static void plotStartPoint(CCT cct, String describe) {
        Plot3d.plotPoint(cct.getStartPoint(), describe);
    }

    @Deprecated
    public static void plotEndPoint(CCT cct, String describe) {
        Plot3d.plotPoint(cct.getEndPoint(), describe);
    }

    @Deprecated
    public static void plotStartAndEndPoint(CCT cct, String describeStart, String describeEnd) {
        plotStartPoint(cct, describeStart);
        plotEndPoint(cct, describeEnd);
    }

    @Deprecated
    public static void plotStartAndEndPoint(CCT cct) {
        plotStartAndEndPoint(cct, DescribeForStartPoint, DescribeForEndPoint);
    }


    public static void tellMeTheStartAndEndPoint() {
        System.out.println("起始点为红色+号：" + DescribeForStartPoint);
        System.out.println("终止点为黑色+号：" + DescribeForEndPoint);
    }
}

