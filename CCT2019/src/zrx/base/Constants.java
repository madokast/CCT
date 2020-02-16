package zrx.base;

public class Constants {
    public static final double Miu0 = 4.0 * Math.PI * 1e-7;
    public static final double DX = 1e-8;
    public static final double ESP_0 = 1e-8;

    //in my computer it is 4*2+1=9
    public static final int MAXTHREADS = Runtime.getRuntime().availableProcessors() *2 +1;

    public static final double ARC_LENGTH_OF_67p5_R1M = 67.5 * Math.PI / 180.0;
}
