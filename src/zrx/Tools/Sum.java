package zrx.Tools;

public class Sum {
    public static double get(double...arr){
        double sum = 0.0;
        for (int i = 0; i < arr.length; i++) {
            sum+=arr[i];
        }
        return sum;
    }

    public static void main(String[] args) {
        System.out.println(get(1, 2, 3));
    }
}
