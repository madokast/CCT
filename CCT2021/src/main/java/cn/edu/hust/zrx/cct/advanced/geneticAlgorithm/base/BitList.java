package cn.edu.hust.zrx.cct.advanced.geneticAlgorithm.base;

import java.util.logging.Level;

/**
 * Description
 * BitList
 * bit数组，用于存放比特
 * <p>
 * Data
 * 18:37
 *
 * @author zrx
 * @version 1.0
 */

public interface BitList {

    int size();

    int get(int index);

    void set(int index, int value);

    BitList copy();

    /**
     * 将 offset 开始的 length 长度的 bit 子数组以int形式表示出来
     *
     * @param offset 首位偏移
     * @param length 应小于等于31位
     * @return offset 开始的 length 长度的 bit 子数组以 int 形式表示出来
     */
    default int valueAlong(int offset, int length) {
        int val = get(offset);

        for (int i = offset + 1; i < offset + length; i++) {
            val = (val << 1) + get(i);
        }

        return val;
    }

    /**
     * 子数组
     *
     * @param offset 相当于原数组偏移
     * @param length 长度
     * @return 子数组
     */
    BitList get(int offset, int length);

    /**
     * 将 this 的 offset 后 target.size 长度的部分，设为 target
     * 示例
     * this = 0111 1010 01
     * offset = 2
     * target = 000
     * 则方法执行后
     * this = 0100 0010 01
     *
     * @param offset this 偏移
     * @param target 目标值
     */
    default void set(int offset, BitList target) {
        for (int i = offset; i < offset + target.size(); i++) {
            set(i, target.get(i - offset));
        }
    }

    /**
     * 翻转 index 处的值 0<->1
     *
     * @param index 索引
     */
    default void reverse(int index) {
        set(index, 1 - get(index));
    }

    /**
     * 单点杂交
     *
     * @param l1     染色体 1
     * @param l2     染色体 2
     * @param offset 该位置及之前的基因交换
     */
    static void crossover(BitList l1, BitList l2, int offset) {
        BitList sub1 = l1.get(0, offset+1);
        BitList sub2 = l2.get(0, offset+1);

        l1.set(0, sub2);
        l2.set(0, sub1);
    }

    /**
     * @return 以01字符串打印BitList
     */
    String toString();
}
