package ExecutorService;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExecutorServiceDemo {

    public static void main(String[] args) {

    ExecutorService executor=  Executors.newFixedThreadPool(3);

     for(int i = 1; i <= 5; i++) {
            int taskId = i;
         executor.execute(new Runnable() {
             @Override
             public void run() {
                 System.out.println("Task " + taskId + " running on " + Thread.currentThread().getName());
             }
         });
        }

     for(int i=6;i<10;i++){
         int taskId = i;
         executor.execute(()->{
             System.out.println("Task " + taskId + " running on " + Thread.currentThread().getName());
         });
     }

     executor.shutdown();
        }
}
