package cn.edu.hust.zrx.cct.advanced.geneticAlgorithm.base;

import javax.naming.OperationNotSupportedException;
import java.util.Arrays;

/**
 * Description
 * ByteBitList
 * 底层是byte数组
 * <p>
 * Data
 * 18:59
 *
 * @author zrx
 * @version 1.0
 */

public class ByteBitList implements BitList {
    private static final int BUCKET_LENGTH = Byte.SIZE;

    private final int size;

    private byte[] data;

    public ByteBitList(int size) {
        this.size = size;
        data = new byte[size / BUCKET_LENGTH + 1];
    }

    public ByteBitList(String binaryString) {
        this(binaryString.length());

        for (int i = 0; i < binaryString.length(); i++) {
            set(i, binaryString.charAt(i) - '0');
        }
    }

    private ByteBitList(ByteBitList list) {
        this.size = list.size;
        this.data = Arrays.copyOf(list.data, list.data.length);
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public BitList copy() {
        return new ByteBitList(this);
    }

    @Override
    public BitList get(int offset, int length) {
        BitList ret = new ByteBitList(length);

        for (int i = offset; i < offset + length; i++) {
            ret.set(i - offset, get(i));
        }

        return ret;
    }

    public void set(int index, int value) {


        int arrayIndex = 0;
        while (index >= BUCKET_LENGTH) {
            index -= BUCKET_LENGTH;
            arrayIndex++;
        }

        if (value == 1) {
            data[arrayIndex] |= (filters[BUCKET_LENGTH - index - 1]);
        } else if (value == 0) {
            data[arrayIndex] &= (~filters[BUCKET_LENGTH - index - 1]);
        } else
            throw new IllegalArgumentException("value 只能是0/1");

    }

    public int get(int index) {
        int arrayIndex = 0;
        while (index >= BUCKET_LENGTH) {
            index -= BUCKET_LENGTH;
            arrayIndex++;
        }

        return (data[arrayIndex] & filters[BUCKET_LENGTH - index - 1]) == 0 ? 0 : 1;
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < size; i++) {
            sb.append(get(i));

            if (i % 4 == 3)
                sb.append(" ");
        }

        return sb.toString();
    }

    private static byte[] filters = new byte[BUCKET_LENGTH];
    private static byte[] leftMasks = new byte[BUCKET_LENGTH];
    private static byte[] rightMasks = new byte[BUCKET_LENGTH];

    static {
        // filters
        // 0 -> 0000 0001
        // 1 -> 0000 0010

        // leftMasks
        // 0 -> 1000 0000
        // 1 -> 1100 0000

        // rightMasks
        // 0 -> 0000 0001
        // 1 -> 0000 0011

        for (int i = 0; i < BUCKET_LENGTH; i++) {
            filters[i] = (byte) (1 << i);
        }

        leftMasks[0] = filters[BUCKET_LENGTH - 1]; // 1000 0000

        rightMasks[0] = filters[0]; // 0000 0001

        for (int i = 1; i < BUCKET_LENGTH; i++) {
            leftMasks[i] = (byte) ((leftMasks[i - 1] >>> 1) + filters[BUCKET_LENGTH - 1]);
            rightMasks[i] = (byte) ((rightMasks[i - 1] << 1) + filters[0]);
        }
    }
}
