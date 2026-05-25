import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

//i coded this on my own , lets'go.
public class WaitNotifyDemo {
    private static Object lock = new Object();
    public static void main(String[] args) throws InterruptedException {

        Thread t1=new Thread(){
            @Override
            public void run() {
                synchronized (lock){
                    for(int i=1;i<15;i=i+2){
                    lock.notify();
                        try {
                            System.out.println("Thread 1 : "+i);
                            lock.wait();
                        }catch (InterruptedException e){
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        };

        Thread t2=new Thread(){
            @Override
                public void run() {
                synchronized (lock){
                    for(int i=2;i<15;i=i+2){
                    lock.notify();
                        try {
                          System.out.println("Thread 2 : "+i);
                          lock.wait();
                        }catch (InterruptedException e){
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        };

        t1.start();
        java.lang.Thread.sleep(50);
        t2.start();
        t1.join();
        System.out.println("T1 Executing done");
        t2.join();
        System.out.println("Done executing both ");
    }
}


// a better version
//public class WaitNotifyDemo {
//
//    private static final Object lock = new Object();
//
//    public static void main(String[] args) throws InterruptedException {
//
//        Thread t1 = new Thread() {
//            @Override
//            public void run() {
//
//                synchronized (lock) {
//
//                    for (int i = 1; i < 15; i += 2) {
//
//                        System.out.println("Thread 1 : " + i);
//
//                        lock.notify();
//
//                        if (i < 13) {
//                            try {
//                                lock.wait();
//                            } catch (InterruptedException e) {
//                                throw new RuntimeException(e);
//                            }
//                        }
//                    }
//                }
//            }
//        };
//
//        Thread t2 = new Thread() {
//            @Override
//            public void run() {
//
//                synchronized (lock) {
//
//                    for (int i = 2; i < 15; i += 2) {
//
//                        System.out.println("Thread 2 : " + i);
//
//                        lock.notify();
//
//                        if (i < 14) {
//                            try {
//                                lock.wait();
//                            } catch (InterruptedException e) {
//                                throw new RuntimeException(e);
//                            }
//                        }
//                    }
//                }
//            }
//        };
//
//        t1.start();
//        Thread.sleep(50);
//        t2.start();
//
//        t1.join();
//        t2.join();
//
//        System.out.println("Done executing both");
//    }
//}
//synchronized(lock) cannot be directly inside class body like that
//@Override must be inside anonymous class body
//run() must belong to Thread subclass or Runnable implementation
//Thread creation
//Race condition
//synchronized
//wait/notify
//Producer-consumer
//ExecutorService
//Callable/
//Future
//        ConcurrentHashMap
//CompletableFuture
//Deadlock + locks