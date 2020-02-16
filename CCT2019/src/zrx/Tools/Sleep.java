package zrx.Tools;

public class Sleep {
    public static void sleep(int s){
        try {
            Thread.currentThread().sleep(s*1000);
        }catch (Exception e){}
    }
}
