package zrx.DemoAndStudy.test;

import zrx.Tools.PrintArray;

import java.util.Scanner;

public class Test0811 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int i = 0;
        while(scanner.hasNext()){
            if(i%3==0){
                System.out.println();
            }
            i++;
            final String nextLine = scanner.nextLine();
            System.out.print(nextLine+"\t");

        }
    }
}
