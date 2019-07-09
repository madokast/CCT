package zrx.DemoAndStudy;

import zrx.CCT.CCTPlot;
import zrx.CCT.ConnectionSegmentOfCCT;
import zrx.CCT.CurvedCCTAnalysis;
import zrx.CCT.DiscreteCCT;
import zrx.CCT.abstractCCT.CurvedCCT;
import zrx.base.Vector2d;
import zrx.python.Plot2d;
import zrx.python.Plot3d;

public class 五段AGCCT {
    public static void main(String[] args) {
        CCT_quadrupole_5part();
    }

    /**
     * 《恭喜 cct连接问题终于告一段落了》
     *      从7月5日周五晚上开始，到7月9日周二，历时近4天。
     *      从三维连接开始，猜测连接段的共面问题，编写了 Vector3d.isCoplane()、
     * Vector3d.nearestTwoPointsOnTheirLinesRespectively()、Vector3d.nearestDistanceOfTwoLines()
     * 等一系列函数，可以看到这些函数都是在Vector3d类中写的，因为当时猜测这是一个三位问题。结果发现最
     * 开始的共面猜测失败了。这似乎意味着，三维插值问题不能平面化，需要写三维插值算法。这对我来说几乎
     * 不可能完成。
     *      好在，周五深夜11点多，我在整理思路，画图的时候发现，“CCT连接问题”并不是三维问题，这是因
     * 为CCT是缠绕在弯曲圆柱上的，因此理所当然的插值，也要在这个曲面内进行。可是曲面的插值是怎样的呢？
     * 我又开始了冥思苦想，甚至还在知乎提问，“曲面上的两点赫米特插值要怎么做？”。
     *      没想到，回到寝室，突然灵光一闪，虽然CCT实际是一个三维模型，但是建模中确实首先得到两个自由
     * 度的二维曲线(ξ,φ)，然后通过一些变换得到三维曲线的。这意味着，“CCT连接问题”的插值虽然最终产
     * 物是空间中的曲线，但是插值可以在二维的(ξ,φ)坐标系下完成。
     * 以上的发现是整个问题的转折点。
     *      虽然方法找到了，但是实际做起来并不简单，一开始我的画图没有使用Plot2d.equal()，导致看到的
     * 图像失真，由此选择了圆插值算法，经过长时间的研究，编写了Vector2d.circularInterpolation()函数，
     * 以及相关的十多个函数，美名其曰“圆插值算法”，docs文件夹里还有相关的图片，当时觉得真好看，似乎
     * 一切都成了。结果插值失败。
     *      不过好在这个过程中，我重新优化了CCT的建模过程，不再是直接得到三维模型，而是先生产二维模型，
     * 然后在此基础上再得到三维模型，基于此我还把CCT建模的大部分函数都@Deprecated了。
     *      接下来，开始研究新的插值算法，我发现学校学的插值，都是在直角坐标系中经行的，这无法完成多值
     * 函数的插值，例如圆插值。因此我把重点放在了极坐标系上，极坐标系的完美特征，可以插值出各种曲线，而
     * 原本在直角坐标系中是多值函数，难以直接插值。
     *      这次的方向没有问题，函数很快写了出来，可惜一开始用的是两点插值，点数不够，因此在二维曲线中，
     * 我看到插值结果非常好，但是变换到了三维中，十分的突兀。(这里忘记留图了，应该留一张看看的)。因此我
     * 引入了中间点概念，其实就是在中间再找一个点，这样插值终于完成了。期间主要遇到的问题是，直角坐标到
     * 极点坐标系的变换，插值方向的选择，如果插值经过了(0/2pi/x轴)怎么办的问题。还有就是插值需要求解矩
     * 阵方程(线性方程组)，对于二元方程，我写了自己的算法，而多元的，我实在是不想自己造轮子了，多谢网上
     * 找到的Jama包，完成了解方程问题。还有我想说，Java居然不自带asinh()函数，害我苦苦寻找，弄了apache
     * 下的math3包。
     *      昨晚加上今早，我把CCT建模继续封装了一层，原本的抽象类CCT基本弃而不用，实现的CurvedCCT类也太
     * 基础了，因此在上面封装了CurvedCCTAnalysis类，利用里面的工厂方法生成CCT，利用discrete()离散CCT，
     * 获得二维和三维的轨迹(现在生成三维CCT轨迹，必须先生成二维)。然后CurvedCCT.reverseWinding()方法让
     * CCT反向，以及CurvedCCTAnalysis.connect()反向连接两个CCT。可惜这个方向智能化程度非常低，需要自己
     * 手动调节许多参数，不过插值连接算法本来就是多解的。若太智能化反而缺失了自由度(胡说八道)。
     *      最后，颇为自满的python画图方法，让我的CCT每次都能立刻看出图像，没有它们，我的插值是不可能完
     * 成的。
     *      本来还想在组会上大发其言的。算了，太累了，没必要说了。
     *
     *      还有，要感谢一只一只陪我的喵喵。
     *
     *      2019年7月9日   madokast
     */
    private static void CCT_quadrupole_5part() {
        //-------------------------------cct1-----------------------------------//
        //建立第一段CCT
        CurvedCCT cct1 = CurvedCCTAnalysis.curvedCCTFactory(
                1.0, 0.03, 35, 100, 2, Math.PI / 360, Math.PI / 6, Math.PI / 180);
        //离散化 画图
        DiscreteCCT dcct1 = CurvedCCTAnalysis.discrete(cct1);
        dcct1.plot23d(Plot2d.RED_LINE);

        //--------------------------------cct2----------------------------------//
        //反向。改变匝数
        CurvedCCT cct2 = CurvedCCT.reverseWinding(cct1);
        cct2.setN(20);
        //离散化 画图
        DiscreteCCT dcct2 = CurvedCCTAnalysis.discrete(cct2);
        dcct2.plot23d(Plot2d.BLUE_LINE);

        //--------------------------------cct12连接----------------------------------//
        //连接CCT12 并画图
        ConnectionSegmentOfCCT connect12 = CurvedCCTAnalysis.connect(dcct1, dcct2,
                0.3, Vector2d.getOne(0, 0.001),
                0.5, Math.PI / 180.0 * 10, Math.PI / 18000);
        connect12.plot23d(Plot2d.BLACK_LINE);

        //--------------------------------cct3----------------------------------//
        //反向。改变匝数
        CurvedCCT cct3 = CurvedCCT.reverseWinding(cct2);
        cct3.setN(35);
        //离散化 画图
        DiscreteCCT dcct3 = CurvedCCTAnalysis.discrete(cct3);
        dcct3.plot23d(Plot2d.RED_LINE);

        //--------------------------------cct23连接----------------------------------//
        //连接CCT12 并画图
        ConnectionSegmentOfCCT connect23 = CurvedCCTAnalysis.connect(dcct2, dcct3,
                0.3, Vector2d.getOne(0, -0.002),
                0.5, Math.PI / 180.0 * 30.0, Math.PI / 18000);
        connect23.plot23d(Plot2d.BLACK_LINE);

        //--------------------------------cct4----------------------------------//
        //反向。改变匝数
        CurvedCCT cct4 = CurvedCCT.reverseWinding(cct3);
        cct4.setN(20);
        //离散化 画图
        DiscreteCCT dcct4 = CurvedCCTAnalysis.discrete(cct4);
        dcct4.plot23d(Plot2d.BLUE_LINE);

        //--------------------------------cct34连接----------------------------------//
        //连接CCT12 并画图
        ConnectionSegmentOfCCT connect34 = CurvedCCTAnalysis.connect(dcct3, dcct4,
                0.3, Vector2d.getOne(0, 0.001),
                0.5, Math.PI / 180.0 * 10, Math.PI / 18000);
        connect34.plot23d(Plot2d.BLACK_LINE);

        //--------------------------------cct5----------------------------------//
        //反向。改变匝数
        CurvedCCT cct5 = CurvedCCT.reverseWinding(cct4);
        cct5.setN(35);
        //离散化 画图
        DiscreteCCT dcct5 = CurvedCCTAnalysis.discrete(cct5);
        dcct5.plot23d(Plot2d.RED_LINE);

        //--------------------------------cct45连接----------------------------------//
        //连接CCT12 并画图
        ConnectionSegmentOfCCT connect45 = CurvedCCTAnalysis.connect(dcct4, dcct5,
                0.3, Vector2d.getOne(0, -0.002),
                0.5, Math.PI / 180.0 * 30.0, Math.PI / 18000);
        connect45.plot23d(Plot2d.BLACK_LINE);


        //展示图片
        Plot2d.showThread();
        Plot2d.equal();
        CCTPlot.setCube(1.5);
        Plot3d.showThread();
    }
}
