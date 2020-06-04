package cn.edu.hust.zrx.cct.advanced.geneticAlgorithm.base;

import cn.edu.hust.zrx.cct.Logger;
import cn.edu.hust.zrx.cct.base.BaseUtils;

/**
 * Description
 * Gene 基因 即一个变量
 * <p>
 * Data
 * 20:38
 *
 * @author zrx
 * @version 1.0
 */

public class Gene {
    private final String name;
    private final double max;
    private final double min;
    private final int size;
    private final int partNumber;
    private final double[] disperse;

    /**
     * @param name 名字
     * @param max  最大值
     * @param min  最小值
     * @param size 碱基对数目 即分段为 2^basePair段，表示精度
     */
    public Gene(String name, double max, double min, int size) {
        this.name = name;
        this.max = max;
        this.min = min;
        this.size = size;

        this.partNumber = (1 << size);

        disperse = BaseUtils.Python.linspace(max, min, partNumber);
    }

    public static Gene create(String name, double max, double min, int disperseNumber) {
        if (max <= min) {
            throw new IllegalArgumentException("max<=min");
        }

        if (disperseNumber < 0) {
            throw new IllegalArgumentException("disperseNumber<0?");
        }


        int size = 0;
        while (disperseNumber > 0) {
            disperseNumber >>= 1;
            size += 1;
        }

        Gene gene = new Gene(name, max, min, size);

        Logger.getLogger().info("创建 " + gene);

        return gene;
    }


    public static Gene create(String name, double max, double min, double resolution){
        int disperseNumber = (int)((max-min)/resolution);

        return create(name,max,min,disperseNumber);
    }

    @Override
    public String toString() {
        return "基因[" + name + ", 最大值" + max + ", 最小值" + min + ", 碱基对" + size + ", 即分段数" + partNumber + ", 精度为" + ((max - min) / partNumber)+
        "]";
    }

    public double value(int index) {
        return disperse[index];
    }

    public double getMax() {
        return max;
    }

    public double getMin() {
        return min;
    }

    public int getSize() {
        return size;
    }

    public int getPartNumber() {
        return partNumber;
    }

    public double[] getDisperse() {
        return disperse;
    }

    public String getName() {
        return name;
    }
}
