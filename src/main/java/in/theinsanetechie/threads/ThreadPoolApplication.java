package in.theinsanetechie.threads;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ThreadPoolApplication {

    public static void main(String[] args) {
        /**
         * Created 2 threads
         */
        ExecutorService executorService = Executors.newFixedThreadPool(2);

        for (int i = 0; i < 5; i++) {
            executorService.submit(new Worker(i));
        }

        System.out.println("Tasks have been submitted");

        /**
         * Initiates an orderly shutdown in which previously submitted tasks are executed,
         * but no new tasks will be accepted.
         *
         * Invocation has no additional effect if already shut down.
         *
         * This method does not wait for previously submitted tasks to complete execution.
         * Use awaitTermination to do that.
         */
        executorService.shutdown();

        //executorService.submit(new Worker(10)); // RejectedExecutionException

        try {
            executorService.awaitTermination(1, TimeUnit.DAYS);
        } catch (InterruptedException e) {
            System.out.println("awaitTermination() has been interrupted!");
        }

        System.out.println("Tasks have been completed");
    }
}

class Worker implements Runnable {

    private int workerId;

    public Worker(int workerId) {
        this.workerId = workerId;
    }

    @Override
    public void run() {
        System.out.println("Starting: " + workerId);

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            System.out.println(Thread.currentThread().getName() + "(" + workerId + ") has been interrupted!");
        }

        System.out.println("Completed: " + workerId);
    }
}