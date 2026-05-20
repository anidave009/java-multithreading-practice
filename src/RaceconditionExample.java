public class RaceconditionExample {
    private static int counter=0;
    public static void main(String[] args) throws InterruptedException {
        Runnable runnable1 = ()->{
            for(int i=0;i<10000000;i++){
                counter++;
            }
        };

        Runnable runnable2 = ()->{
            for(int i=0;i<10000000;i++){
                counter++;
            }
        };

        Thread thread1 = new Thread(runnable1);
        Thread thread2 = new Thread(runnable2);

        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();
        System.out.println("count : " +counter);
        //the expected count should be 2000000 but it is random each time due to race condition.
        //it is due to context switch and other thread comes in between the incomplete operation of other thread(read -> add -> write)
        //and does not gets the updated value and this happens multiple times and thus the difference in result.
    }
}
