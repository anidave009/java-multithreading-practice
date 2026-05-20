package RaceCondition;

import java.util.concurrent.atomic.AtomicInteger;

public class AtomicIntegerDemo {
    private static AtomicInteger counter = new AtomicInteger(0);

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread() {
            @Override
            public void run() {
                for (int i = 0; i < 10000000; i++) {
                    counter.incrementAndGet();
                }
                System.out.println("t1 done");
            }
        };

        Thread t2 = new Thread() {
            @Override
            public void run() {
                for (int i = 0; i < 10000000; i++) {
                    counter.incrementAndGet();
                }
                System.out.println("t2 done");
            }
        };

        t1.start();
        t2.start();
        t1.join();
        t2.join();

        System.out.println("count = " + counter.get());
    }
}
