package cn.edu.hust.zrx.cct.advanced;

import cn.edu.hust.zrx.cct.base.point.Point2;
import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.FastFourierTransformer;
import org.apache.commons.math3.transform.TransformType;

import java.util.Arrays;

/**
 * Description
 * 快速傅里叶变换
 * <p>
 * Data
 * 16:08
 *
 * @author zrx
 * @version 1.0
 */

public class FFT {
    private static final FastFourierTransformer FFT = new FastFourierTransformer(DftNormalization.STANDARD);

    /**
     * 返回的实际上是复数
     * point2.len 是幅值
     * atan2(y,x) 是相位
     *
     * @param data 数据
     * @return 复数
     */
    public static Point2[] forward(double[] data) {
        Complex[] transformed = FFT.transform(data, TransformType.FORWARD);


        return Arrays.stream(transformed).map(
                c -> Point2.create(c.getReal(), c.getImaginary())
        ).toArray(Point2[]::new);
    }

    public static Point2[] forwardToAmplitude(double[] data) {
        Point2[] forward = forward(data);

        Point2[] ret = new Point2[forward.length];

        for (int i = 0; i < forward.length; i++) {
            ret[i] = Point2.create(i, forward[i].distanceToOrigin());
        }

        return ret;
    }
}
