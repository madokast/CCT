package cn.edu.hust.zrx.cct.advanced.combined;

import cn.edu.hust.zrx.cct.base.BaseUtils;

import static cn.edu.hust.zrx.cct.base.BaseUtils.Constant.MM;

/**
 * Description
 * GantryDataBipolarCo
 * 双极点坐标系下机架
 *
 * <p>
 * Data
 * 16:11
 *
 * @author zrx
 * @version 1.0
 */

public class GantryDataBipolarCo {
    public static class FirstBend {
        // 孔径
        double agCct12InnerSmallR = 35 * MM;
        double agCct12OuterSmallR = 45 * MM;
        double diCct12InnerSmallR = 55 * MM;
        double diCct12OuterSmallR = 65 * MM;

        // 匝数
        int agCct1WindingNumber = 30;
        int agCct2WindingNumber = 39;
        int diCct12WindingNumber = 71;

        // 角度
        double diCct12BendingAngle = 22.5;
        double agCct1BendingAngle = diCct12BendingAngle * agCct1WindingNumber / (agCct1WindingNumber + agCct2WindingNumber);
        double agCct2BendingAngle = diCct12BendingAngle * agCct2WindingNumber / (agCct1WindingNumber + agCct2WindingNumber);
    }
}
