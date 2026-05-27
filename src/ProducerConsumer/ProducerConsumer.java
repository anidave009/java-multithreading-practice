package ProducerConsumer;

import java.util.LinkedList;
import java.util.Queue;

public class ProducerConsumer {

    static final int MAX_SIZE = 5;          // buffer max capacity
    static Queue<Integer> buffer = new LinkedList<>();
    static final Object LOCK = new Object(); // shared lock object

    // ─────────────────────────────────────────
    // PRODUCER — makes items, puts in buffer
    // ─────────────────────────────────────────
    static class Producer extends Thread {
        public void run() {
            int item = 1;
            while (true) {
                synchronized (LOCK) {

                    // buffer full → wait for consumer to take something
                    while (buffer.size() == MAX_SIZE) {
                        try {
                            System.out.println("Producer → buffer FULL, waiting...");
                            LOCK.wait();  // releases lock, goes to sleep
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            return;
                        }
                    }

                    // buffer has space → add item
                    buffer.add(item);
                    System.out.println("Producer → produced item: " + item
                            + "  | buffer size: " + buffer.size());
                    item++;
                    LOCK.notifyAll(); // wake up consumer if it was waiting
                }

                try { Thread.sleep(500); } // produce every 0.5 sec
                catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;}
            }
        }
    }

    // ─────────────────────────────────────────
    // CONSUMER — takes items from buffer
    // ─────────────────────────────────────────
    static class Consumer extends Thread {
        public void run() {
            while (true) {
                synchronized (LOCK) {

                    // buffer empty → wait for producer to add something
                    while (buffer.isEmpty()) {
                        try {
                            System.out.println("Consumer → buffer EMPTY, waiting...");
                            LOCK.wait();  // releases lock, goes to sleep
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            return;
                        }
                    }

                    // buffer has items → take one
                    int item = buffer.poll();
                    System.out.println("Consumer → consumed item: " + item
                            + "  | buffer size: " + buffer.size());

                    LOCK.notifyAll(); // wake up producer if it was waiting
                }

                try { Thread.sleep(1500); } // consume every 1.5 sec (slower than producer)
                catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;}
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== Producer Consumer Demo ===");
        System.out.println("Buffer max size: " + MAX_SIZE);
        System.out.println("Producer speed : every 0.5 sec");
        System.out.println("Consumer speed : every 1.5 sec");
        System.out.println("(Producer is faster → buffer will fill up → producer will wait)\n");

        Producer producer = new Producer();
        Consumer consumer = new Consumer();

        producer.start();
        Thread.sleep(100);
        consumer.start();

        // run for 6 seconds then stop
        try { Thread.sleep(6000); }
        catch (InterruptedException e) { e.printStackTrace(); }

        producer.interrupt();
        consumer.interrupt();
        System.out.println("\n=== Demo ended ===");
    }
}

//Main Problems in Producer-Consumer
//1. Consumer tries to consume when nothing exists
//2. Producer keeps producing when buffer is full
//3. Race condition : Two consumers try removing simultaneously.
//4. Data inconsistency:Producer is adding while consumer is removing simultaneously.