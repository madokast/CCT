package zrx.base.line2d;

import zrx.Tools.AngleToRadian;
import zrx.Tools.Equal;
import zrx.base.point.Vector2d;
import zrx.python.Plot2d;

public class StraightLine implements Line {
    //不要new 提供工厂方法
    private StraightLine(){}

    private double length;
    private Vector2d startPoint;
    private Vector2d direct;
    private Vector2d rightHandDirect;

    public static StraightLine getByStartAndEnd(Vector2d startPoint, Vector2d endPoint){
        //两点不能重合
        double len = Vector2d.subtract(startPoint,endPoint).length();
        Equal.requireNonzero(len);

        final StraightLine straightLine = new StraightLine();
        straightLine.length = len;
        straightLine.startPoint = Vector2d.copyOne(startPoint);
        straightLine.direct = Vector2d.subtract(endPoint,startPoint);
        straightLine.rightHandDirect = Vector2d.rotate(straightLine.direct,-AngleToRadian.to(90));

        return straightLine;
    }

    public static StraightLine getByStartAndDirectAndLength(Vector2d startPoint, Vector2d direct, double length){
        return getByStartAndEnd(Vector2d.copyOne(startPoint),
                Vector2d.walk(Vector2d.copyOne(startPoint),direct,length)
                );
    }


    @Override
    public double getLength() {
        return this.length;
    }

    @Override
    public Vector2d rightHandSidePoint(double s, double d) {
        //找到s点
        Vector2d sPoint = Vector2d.walk(startPoint,direct,s);

        //移动s点
        sPoint.walkSelf(rightHandDirect,d);

        return sPoint;
    }

    ////////////////////////////测试/////////////////
    public static void main(String[] args) {
        final StraightLine straightLine = getByStartAndEnd(
                Vector2d.getOne(0,0),
                Vector2d.getOne(1,1)
        );

        System.out.println(straightLine.getLength());
        System.out.println(straightLine.pointAt(10));

        for (int i = 0; i < 10; i++) {
            Plot2d.plotPoint(straightLine.pointAt(i),Plot2d.BLACK_POINT);
            Plot2d.plotPoint(straightLine.leftHandSidePoint(5,i),Plot2d.RED_POINT);
        }

        Plot2d.plotPoint(straightLine.pointAt(-1),Plot2d.YELLOW_POINT);
        Plot2d.plotPoint(straightLine.leftHandSidePoint(-1,-2),Plot2d.YELLOW_POINT);

        Plot2d.equal();
        Plot2d.showThread();
    }

}
