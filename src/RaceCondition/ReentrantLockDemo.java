package RaceCondition;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockDemo {
    static ReentrantLock lock = new ReentrantLock();
    static ReentrantLock fairLock = new ReentrantLock(true);
    static int balance=1000;

static void demo1_simpleLock() throws InterruptedException {
    System.out.println("\n===== 1. lock() — waits forever =====");
    balance=1000;

    Thread A=new Thread(()->{
       lock.lock();
       try{
           System.out.println("Thread A → got lock, withdrawing 200, sleeping 2 sec...");
           Thread.sleep(2000);
           balance -= 200;
           System.out.println("Thread A → done. Balance = " + balance);
       }catch (InterruptedException e){
           Thread.currentThread().interrupt();
       }finally {
           lock.unlock();
           System.out.println("Thread A -> lock released");
       }
    });

    Thread B = new Thread(() -> {
        System.out.println("Thread B → waiting for lock...");
        lock.lock();   // waits here until A releases
        try {
            System.out.println("Thread B → got lock finally! withdrawing 300");
            balance -= 300;
            System.out.println("Thread B → done. Balance = " + balance);
        } finally {
            lock.unlock();
            System.out.println("Thread B → lock released");
        }
    });

    A.start();
    Thread.sleep(100);
    B.start();
    A.join();
    B.join();
}

static void demo2_tryLock() throws InterruptedException {
    System.out.println("\n===== 2. tryLock() — no wait, instant skip =====");
    balance=1000;

    Thread A=new Thread(()->{
        lock.lock();
        try {
            System.out.println("Thread A → got lock, working for 2 sec...");
            Thread.sleep(2000);
            balance -= 200;
            System.out.println("Thread A → done. Balance = " + balance);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            lock.unlock();
            System.out.println("Thread A → lock released");
        }
    });

    Thread B=new Thread(()->{
        if(lock.tryLock()){
            try{
                System.out.println("Thread B -> got lock! withdrawing 300");
                balance -= 300;
                System.out.println("Thread B → done. Balance = " + balance);
            }finally {
                lock.unlock();
            }
        }else{
            System.out.println("Thread B → lock busy, skipping. Will try later.");
        }
    });
    A.start();
    Thread.sleep(100);
    B.start();
    A.join();
    B.join();
}

static void demo3_tryLockTimeout()  throws InterruptedException {
    System.out.println("\n===== 3. tryLock(timeout) — waits a bit then gives up =====");
    balance = 1000;

    Thread A = new Thread(() -> {
        lock.lock();
        try {
            System.out.println("Thread A → got lock, working for 3 sec...");
            Thread.sleep(3000);
            balance -= 200;
            System.out.println("Thread A → done. Balance = " + balance);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            lock.unlock();
            System.out.println("Thread A → lock released");
        }
    });

    Thread B = new Thread(() -> {
        try {
            System.out.println("Thread B → trying lock, will wait max 1 sec...");
            if (lock.tryLock(1, TimeUnit.SECONDS)) {  // A holds for 3 sec, B waits only 1
                try {
                    System.out.println("Thread B → got lock! withdrawing 300");
                    balance -= 300;
                } finally {
                    lock.unlock();
                }
            } else {
                System.out.println("Thread B → waited 1 sec, lock still busy. Giving up.");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    });

    A.start();
    Thread.sleep(100);
    B.start();
    A.join();
    B.join();
}

static void demo4_fairLock() throws InterruptedException {
    System.out.println("\n===== 4. Fair Lock — first come first served =====");
    balance = 1000;
    Thread A = new Thread(() -> {
        fairLock.lock();
        try {
            System.out.println("Thread A → got fair lock, working...");
            Thread.sleep(500);
            balance -= 100;
            System.out.println("Thread A → done. Balance = " + balance);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            fairLock.unlock();
        }
    }, "Thread-A");

    Thread B = new Thread(() -> {
        fairLock.lock();
        try {
            System.out.println("Thread B → got fair lock, working...");
            Thread.sleep(500);
            balance -= 200;
            System.out.println("Thread B → done. Balance = " + balance);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            fairLock.unlock();
        }
    }, "Thread-B");

    Thread C = new Thread(() -> {
        fairLock.lock();
        try {
            System.out.println("Thread C → got fair lock, working...");
            Thread.sleep(500);
            balance -= 300;
            System.out.println("Thread C → done. Balance = " + balance);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            fairLock.unlock();
        }
    }, "Thread-C");

    // start in order A → B → C
    A.start();
    Thread.sleep(50);
    B.start();
    Thread.sleep(50);
    C.start();
    A.join(); B.join(); C.join();
    System.out.println("Fair lock: A → B → C served in order they arrived");

}
public static void main(String[] args) throws InterruptedException {
//    demo1_simpleLock();
//    demo2_tryLock();
//    demo3_tryLockTimeout();
//demo4_fairLock();
}
}
