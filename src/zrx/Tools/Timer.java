package zrx.Tools;

public class Timer {
    private static long s;
    private static boolean isCalled=false;
    public static void invoke(){
        if(isCalled){
            long t = System.currentTimeMillis()-s;
            System.out.println("运行时间 "+String.format("%.3f",(double)(t)/1000.0)+"s");
            isCalled=false;
        }else {
            s=System.currentTimeMillis();
            isCalled=true;
        }
    }
}
