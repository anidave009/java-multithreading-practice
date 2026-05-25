public class ThreadStates {
    public static void main(String[] args) throws InterruptedException {
        Runnable r=new Runnable() {
            @Override
            public void run() {
                System.out.println("Hello");
            }
        };
        Thread t1=new Thread(r);
        t1.start();
    }
}
