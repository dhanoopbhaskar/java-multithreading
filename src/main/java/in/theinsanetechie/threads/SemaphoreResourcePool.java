package in.theinsanetechie.threads;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Demonstrates the use of Semaphore for managing a limited pool of resources.
 * 
 * A Semaphore maintains a set of permits. Each acquire() blocks if necessary 
 * until a permit is available, and then takes it. Each release() adds a permit, 
 * potentially releasing a blocking acquirer.
 * 
 * In this example, we simulate a connection pool with limited connections 
 * where multiple threads need to acquire a connection before they can use it.
 */
public class SemaphoreResourcePool {

    static class ConnectionPool {
        private final Semaphore semaphore;
        private final int poolSize;
        private final AtomicInteger availableConnections;

        public ConnectionPool(int poolSize) {
            this.poolSize = poolSize;
            this.semaphore = new Semaphore(poolSize);
            this.availableConnections = new AtomicInteger(poolSize);
        }

        public void acquireConnection(String threadName) throws InterruptedException {
            System.out.println(threadName + " is waiting for a connection...");
            semaphore.acquire();
            availableConnections.decrementAndGet();
            System.out.println(threadName + " acquired a connection. Available: " + availableConnections.get());
        }

        public void releaseConnection(String threadName) {
            semaphore.release();
            availableConnections.incrementAndGet();
            System.out.println(threadName + " released a connection. Available: " + availableConnections.get());
        }
    }

    static class DatabaseWorker implements Runnable {
        private final ConnectionPool pool;
        private final String workerId;

        public DatabaseWorker(ConnectionPool pool, String workerId) {
            this.pool = pool;
            this.workerId = workerId;
        }

        @Override
        public void run() {
            try {
                pool.acquireConnection("Worker-" + workerId);
                
                // Simulate work being done with the connection
                System.out.println("Worker-" + workerId + " is executing a database query...");
                Thread.sleep(1000);
                
                System.out.println("Worker-" + workerId + " completed the query.");
                pool.releaseConnection("Worker-" + workerId);
            } catch (InterruptedException e) {
                System.err.println("Worker-" + workerId + " was interrupted!");
                Thread.currentThread().interrupt();
            }
        }
    }

    public static void main(String[] args) {
        // Create a pool with 3 connections
        ConnectionPool connectionPool = new ConnectionPool(3);
        
        // Create a thread pool to manage workers
        ExecutorService executorService = Executors.newFixedThreadPool(6);

        System.out.println("Starting Semaphore Resource Pool Demo...");
        System.out.println("Connection Pool Size: 3");
        System.out.println("Number of Workers: 6\n");

        // Submit 6 workers to use only 3 connections
        for (int i = 1; i <= 6; i++) {
            executorService.submit(new DatabaseWorker(connectionPool, String.valueOf(i)));
        }

        executorService.shutdown();

        try {
            if (executorService.awaitTermination(5, TimeUnit.MINUTES)) {
                System.out.println("\nAll workers completed successfully!");
            } else {
                System.out.println("\nWorkers did not complete within the timeout period.");
            }
        } catch (InterruptedException e) {
            System.err.println("Main thread was interrupted!");
            Thread.currentThread().interrupt();
        }
    }
}
