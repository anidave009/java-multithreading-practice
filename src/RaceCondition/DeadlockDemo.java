package RaceCondition;

public class DeadlockDemo {

    private static final Object lock1 = new Object();
    private static final Object lock2 = new Object();

    public static void main(String[] args) {

        Thread threadA = new Thread(() -> {
            synchronized (lock1) {                        // A grabs Lock1
                System.out.println("A: holding Lock1...");

                try { Thread.sleep(100); }               // small delay so B can grab Lock2
                catch (InterruptedException e) {}

                System.out.println("A: waiting for Lock2...");
                synchronized (lock2) {                   // A now waits for Lock2
                    System.out.println("A: got both locks!");
                }
            }
        });

        Thread threadB = new Thread(() -> {
            synchronized (lock2) {                        // B grabs Lock2
                System.out.println("B: holding Lock2...");

                try { Thread.sleep(100); }               // small delay so A can grab Lock1
                catch (InterruptedException e) {}

                System.out.println("B: waiting for Lock1...");
                synchronized (lock1) {                   // B now waits for Lock1
                    System.out.println("B: got both locks!");
                }
            }
        });

        threadA.start();
        threadB.start();
    }
}
