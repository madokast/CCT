package zrx.python;

import zrx.CCT.CCT;


/**
 * 和CCT有关的plot
 * 新建一个类 减少耦合~~~
 * <p>
 * plot 各种东西后
 * CCTPlot.setCube(1.5);
 * CCTPlot.show();
 */

public class CCTPlot extends Plot3d {
    public static void plotCCT(CCT cct, String describe) {
        Plot3d.plot3(cct.pathForPlot3(), describe);
    }

    public static void plotCCT(CCT cct) {
        Plot3d.plot3(cct.pathForPlot3(), "");
    }

    public static void plotStartPoint(CCT cct, String describe) {
        Plot3d.plotPoint(cct.getStartPoint(), describe);
    }

    public static void plotEndPoint(CCT cct, String describe) {
        Plot3d.plotPoint(cct.getEndPoint(), describe);
    }

    public static void plotStartAndEndPoint(CCT cct, String describeStart, String describeEnd) {
        plotStartPoint(cct, describeStart);
        plotEndPoint(cct, describeEnd);
    }

    private static final String DescribeForStartPoint = ",'r+'";
    private static final String DescribeForEndPoint = ",'k+'";

    public static void plotStartAndEndPoint(CCT cct) {
        plotStartAndEndPoint(cct, DescribeForStartPoint, DescribeForEndPoint);
    }


    public static void tellMeTheStartAndEndPoint() {
        System.out.println("起始点为红色+号：" + DescribeForStartPoint);
        System.out.println("终止点为黑色+号：" + DescribeForEndPoint);
    }
}

