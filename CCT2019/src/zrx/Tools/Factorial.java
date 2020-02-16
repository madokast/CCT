package zrx.Tools;

public class Factorial {
    public static double get(int n){
        if(n<=1)
            return 1;
        else
            return n*get(n-1);
    }

    public static void main(String[] args) {
        for (int i = 0; i < 5; i++) {
            System.out.println("get(i) = " + get(i));
        }
    }
}
