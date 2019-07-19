package zrx.base;

public class Constants {
    public static final double Miu0 = 4.0 * Math.PI * 1e-7;
    public static final double DX = 1e-8;

    //in my computer it is 4*2+1=9
    public static final int MAXTHREADS = Runtime.getRuntime().availableProcessors() *2 +1;
}
