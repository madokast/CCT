package cn.edu.hust.zrx.cct.base.particle;

import javax.swing.*;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Description
 * 质子计算器
 * 默认单位
 * 质量 kg
 * 速度 m/s
 * 能量 J
 * 动量 kg m/s
 * <p>
 * Data
 * 10:19
 *
 * @author zrx
 * @version 1.0
 */

public class Protons {

    // --------------    单位转换   -------------------
    /**
     * 1 eV = 1.6021765700E-19 J
     */
    public static final double eV = 1.6021765700E-19;

    /**
     * 1 Mev = 1.6021765700E-13 J
     */
    public static final double MeV = 1000 * 1000 * eV;


    /**
     * 动量单位
     * 1 MeV/c = 5.3442857792E-22 kg m/s
     */
    public static final double MeV_PER_C = 5.3442857792E-22;


    //------------------------------------------------------------

    /**
     * 静止质量 kg
     */
    public static final double STATIC_MASS_KG = 1.67262164E-27;

    /**
     * 光速 m/s
     */
    public static final double LIGHT_SPEED_M_PER_S = 299792458.0;

    /**
     * 静止能量 = m0 * c ^ 2
     * 单位焦耳
     */
    public static final double STATIC_ENERGY_J = STATIC_MASS_KG * LIGHT_SPEED_M_PER_S * LIGHT_SPEED_M_PER_S;


    public static final double STATIC_ENERGY_eV = STATIC_ENERGY_J / eV;

    public static final double STATIC_ENERGY_MeV = STATIC_ENERGY_J / MeV;

    /**
     * 电荷量 库伦
     */
    private static final double CHARGE_QUANTITY = 1.6021766208e-19;


    /**
     * 计算总能量 MeV = 静止能量 + 动能
     *
     * @param kineticEnergy_MeV 动能 MeV 一般为 250 Mev
     * @return 总能量 MeV
     */
    public static double getTotalEnergy_MeV(double kineticEnergy_MeV) {
        return STATIC_ENERGY_MeV + kineticEnergy_MeV;
    }

    /**
     * 计算总能量 焦耳
     *
     * @param kineticEnergy_MeV 动能 MeV 一般为 250 Mev
     * @return 总能量 焦耳
     */
    public static double getTotalEnergy_J(double kineticEnergy_MeV) {
        return getTotalEnergy_MeV(kineticEnergy_MeV) * MeV;
    }

    /**
     * 计算动质量 kg = 动能 / (c^2)
     *
     * @param kineticEnergy_MeV 动能 MeV 一般为 250 Mev
     * @return 动质量 kg
     */
    public static double getRunMass_KG(double kineticEnergy_MeV) {
        return getTotalEnergy_J(kineticEnergy_MeV) / LIGHT_SPEED_M_PER_S / LIGHT_SPEED_M_PER_S;
    }

    /**
     * 计算速度 m/s = c * sqrt( 1 - (m0/m)^2 )
     *
     * @param kineticEnergy_MeV 动能 MeV 一般为 250 Mev
     * @return 速度 m/s
     */
    public static double getSpeed_M_PER_S(double kineticEnergy_MeV) {
        return LIGHT_SPEED_M_PER_S * Math.sqrt(
                1 - Math.pow(STATIC_MASS_KG / getRunMass_KG(kineticEnergy_MeV), 2)
        );
    }

    /**
     * 动量 kg m/s
     *
     * @param kineticEnergy_MeV 动能 MeV 一般为 250 Mev
     * @return 动量 kg m/s
     */
    public static double getMomentum_KG_M_PER_S(double kineticEnergy_MeV) {
        return getRunMass_KG(kineticEnergy_MeV) * getSpeed_M_PER_S(kineticEnergy_MeV);
    }

    /**
     * 动量 MeV/c
     *
     * @param kineticEnergy_MeV 动能 MeV 一般为 250 Mev
     * @return 动量 MeV/c
     */
    public static double getMomentum_MeV_PER_c(double kineticEnergy_MeV) {
        return getMomentum_KG_M_PER_S(kineticEnergy_MeV) / MeV_PER_C;
    }


    /**
     * 磁钢度 T/m
     *
     * @param kineticEnergy_MeV 动能 MeV 一般为 250 Mev
     * @return 磁钢度 T/m
     */
    public static double getMagneticStiffness(double kineticEnergy_MeV) {
        return getMomentum_KG_M_PER_S(kineticEnergy_MeV) / CHARGE_QUANTITY;
    }

    //  ------------------  动量分散相关  ----------------------

    /**
     * 已知 动量 kg m/s
     * 求 动能 MeV
     *
     * @param momentum_KG_M_PER_S 动量 kg m/s
     * @return 动能 MeV
     */
    public static double getKineticEnergy_MeV(double momentum_KG_M_PER_S) {

        // 求速度
        double speed = momentum_KG_M_PER_S / Math.sqrt((Math.pow(STATIC_MASS_KG, 2) + Math.pow(momentum_KG_M_PER_S / LIGHT_SPEED_M_PER_S, 2)));

        // 动质量
        double runMass = STATIC_MASS_KG / Math.sqrt(1 - Math.pow(speed / LIGHT_SPEED_M_PER_S, 2));

        // 总能量 J
        double totalEnergy = runMass * LIGHT_SPEED_M_PER_S * LIGHT_SPEED_M_PER_S;

        // 动能 J
        double k = totalEnergy - STATIC_ENERGY_J;

        return k / MeV;
    }


    public static double get动量分散后的动能(double 原动能_MeV, double 动量分散) {
        double 原动量 = getMomentum_KG_M_PER_S(原动能_MeV);

        double 新动量 = 原动量 * (1 + 动量分散);

        double 新动能 = getKineticEnergy_MeV(新动量);

        return 新动能;
    }

    /**
     * 上方法的英文版
     *
     * @param oldKineticEnergy_MeV 原动能_MeV
     * @param momentumDispersion   动量分散
     * @return 动量分散后的动能
     * @see Protons#get动量分散后的动能(double, double)
     */
    public static double getKineticEnergy_MeV_AfterMomentumDispersion(double oldKineticEnergy_MeV, double momentumDispersion) {
        return get动量分散后的动能(oldKineticEnergy_MeV, momentumDispersion);
    }

    /**
     * 动量分散 = 能量分散 * (Ek+E0)/(Ek+2E0)
     *
     * @param 动量分散 动量分散
     * @return 能量分散
     */
    public static double convert动量分散_TO_能量分散(double 动量分散, double 动能_MeV) {
        double k = (动能_MeV + STATIC_ENERGY_MeV) / (动能_MeV + 2 * STATIC_ENERGY_MeV);

        return 动量分散 / k;
    }

    /**
     * 上方法的英文版
     *
     * @param momentumDispersion 动量分散
     * @param kineticEnergy_MeV  动能_MeV
     * @return 能量分散
     */
    public static double convertMomentumDispersionToEnergyDispersion(double momentumDispersion, double kineticEnergy_MeV) {
        return convert动量分散_TO_能量分散(momentumDispersion, kineticEnergy_MeV);
    }


    /**
     * 报表
     *
     * @param kineticEnergy_MeV 动能 MeV 一般为 250 Mev
     * @return 报表
     */
    public static Map<String, Double> reportForm(double kineticEnergy_MeV) {
        LinkedHashMap<String, Double> map = new LinkedHashMap<>();


        map.put("静止质量/kg", STATIC_MASS_KG);
        map.put("光速/ms　　", LIGHT_SPEED_M_PER_S);
        map.put("静止能量/J　", STATIC_ENERGY_J);
        map.put("电荷量/C　　", CHARGE_QUANTITY);
        map.put("静止能量/eV", STATIC_ENERGY_eV);
        map.put("静止能量/MeV", STATIC_ENERGY_MeV);
        map.put("动能/MeV　　", kineticEnergy_MeV);
        map.put("总能量/MeV　", getTotalEnergy_MeV(kineticEnergy_MeV));
        map.put("总能量/J　", getTotalEnergy_J(kineticEnergy_MeV));
        map.put("动质量/kg　", getRunMass_KG(kineticEnergy_MeV));
        map.put("速度/ms　　　", getSpeed_M_PER_S(kineticEnergy_MeV));
        map.put("动量/kgms　　", getMomentum_KG_M_PER_S(kineticEnergy_MeV));
        map.put("动量/MeVc-1　　", getMomentum_MeV_PER_c(kineticEnergy_MeV));
        map.put("磁刚度/Tm　", getMagneticStiffness(kineticEnergy_MeV));

        return map;
    }

    public static String report(double kineticEnergy_MeV) {
        Map<String, Double> map = reportForm(kineticEnergy_MeV);
        StringBuilder stringBuilder = new StringBuilder();
        map.forEach((key, value) -> stringBuilder.append(String.format("%-20s\t%30s\n", key, value + "")));

        return stringBuilder.toString();
    }
}
