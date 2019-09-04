package zrx.Tools;

public class RunAllAndJoin {
    public static void run(Runnable...runnables){
        final Thread[] threads = new Thread[runnables.length];
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(runnables[i]);
            threads[i].start();
        }

        try {
            for (int i = 0; i < threads.length; i++) {
                threads[i].join();
            }
        }catch (Exception e){e.printStackTrace();}
    }

    public static void main(String[] args) {
        run(
                ()-> {
                    try {
                        Thread.currentThread().sleep(1000);
                    }catch (Exception e){e.printStackTrace();}
                },
                ()-> System.out.println("123"),
                ()-> System.out.println("abc"),
                ()-> System.out.println("efd"),
                ()-> System.out.println("eqw")
        );

        System.out.println("over----------------");
    }
}
