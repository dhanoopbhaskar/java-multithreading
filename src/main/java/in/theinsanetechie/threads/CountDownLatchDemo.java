package in.theinsanetechie.threads;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CountDownLatchDemo {

    public static void main(String[] args) {
        CountDownLatch countDownLatch = new CountDownLatch(3);
        ExecutorService executorService = Executors.newFixedThreadPool(3);

        for (int i = 0; i < 3; i++) {
            executorService.submit(new CountDownWorker(countDownLatch));
        }

        executorService.shutdown();

        try {
            /**
             * Causes the current thread to wait until the latch has counted down to zero,
             * unless the thread is interrupted.
             *
             * If the current count is zero then this method returns immediately.
             * If the current count is greater than zero then the current thread becomes disabled for
             * thread scheduling purposes and lies dormant until one of two things happen:
             *
             * 1. The count reaches zero due to invocations of the countDown method; or
             * 2. Some other thread interrupts the current thread.
             *
             * If the current thread:
             * 1. has its interrupted status set on entry to this method; or
             * 2. is interrupted while waiting,
             *
             * then InterruptedException is thrown and the current thread's interrupted status is cleared.
             */
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Completed.");
    }
}

class CountDownWorker implements Runnable {

    private CountDownLatch countDownLatch;

    public CountDownWorker(CountDownLatch countDownLatch) {
        this.countDownLatch = countDownLatch;
    }

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + " Started!");

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            System.out.println(Thread.currentThread().getName() + " has been interrupted!");
        }

        countDownLatch.countDown();
        System.out.println(Thread.currentThread().getName() + " Ended!");
    }
}