package RaceCondition;

import org.w3c.dom.css.Counter;

public class Synchronization {
    private static int counter = 0;

    //acquire lock when any thread is working on it .
    private static synchronized void increment(){
        counter++;
    }
    public static void main(String[] args) throws InterruptedException {
        Thread t1=new Thread(){
            @Override
            public void run() {
                for(int i=0;i<10000000;i++){
                    increment();
                }
                System.out.println("t1 done");
            }
        };
        Thread t2=new Thread(){
            @Override
            public void run() {
                for(int i=0;i<10000000;i++){
                    increment();
                }
                System.out.println("t2 done");
            }
        };

             t1.start();
             t2.start();
             t1.join();
             t2.join();
             System.out.println("count = "+counter);
         //now one thread acquires lock and releases lock only when the operation is
        //done , so other thread sees only updated value
    }
}

//synchronised can be method or block of code , but you can pass 3 things in it .
//object , class itself , or object lock.

// synchronized can be used in following ways

//1. synchronized (this) — lock on current object instance
//basic meaning of this is if we are making an instance and 2 threads try to access
//the same object then it cannot , it can acccess different instance.

//public class Counter {
//    private int count = 0;
//
//    public void increment() {
//        synchronized (this) {  // "this" = current Counter object
//            count++;
//        }
//    }
//}

//Counter c1 = new Counter();
//Counter c2 = new Counter();
//
//t1 → c1.increment()  →  locks c1
//t2 → c2.increment()  →  locks c2   ← different object, no blocking!
//
//t1 → c1.increment()  →  locks c1
//t2 → c1.increment()  →  BLOCKED    ← same object, blocks!



//2.synchronized (anyObject) — lock on dedicated object
//every object has a hidden lock for it

//public class Main {
//    private static int counter = 0;
//    private static final Object lock = new Object(); // dedicated lock
//
//    public void increment() {
//        synchronized (lock) {  // lock on this specific object
//            counter++;
//        }
//    }
//}

//t1 → increment() → grabs lock object's hidden lock
//t2 → increment() → BLOCKED, same lock object
//
//most explicit and clean way
//you clearly see WHAT is being used as lock

//3.synchronized (Main.class) — lock on the class itself

//public class Main {
//    private static int counter = 0;  // static = belongs to class, not object
//
//    public void increment() {
//        synchronized (Main.class) {  // lock on class itself
//            counter++;
//        }
//    }
//}
//Main m1 = new Main();
//Main m2 = new Main();
//
//t1 → m1.increment()  →  locks Main.class
//t2 → m2.increment()  →  BLOCKED ← even different instances, still blocks!

//why?  →  counter is static, belongs to Main.class not m1 or m2
//          so lock must also be on Main.class, not on m1 or m2
//
//good when → protecting static shared variables