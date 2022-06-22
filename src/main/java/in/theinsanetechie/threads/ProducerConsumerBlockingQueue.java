package in.theinsanetechie.threads;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ProducerConsumerBlockingQueue {

    /**
     * A Queue that additionally supports operations that wait for the queue to become non-empty
     * when retrieving an element, and wait for space to become available in the queue when storing an element.
     *
     * A BlockingQueue does not accept null elements. Implementations throw NullPointerException on attempts to add,
     * put or offer a null. A null is used as a sentinel value to indicate failure of poll operations.
     *
     * A BlockingQueue may be capacity bounded. At any given time it may have a remainingCapacity beyond
     * which no additional elements can be put without blocking. A BlockingQueue without any intrinsic
     * capacity constraints always reports a remaining capacity of Integer.MAX_VALUE.
     * BlockingQueue implementations are designed to be used primarily for producer-consumer queues,
     * but additionally support the Collection interface. So, for example, it is possible to remove an
     * arbitrary element from a queue using remove(x). However, such operations are in general not performed
     * very efficiently, and are intended for only occasional use, such as when a queued message is cancelled.
     * BlockingQueue implementations are thread-safe. All queuing methods achieve their effects atomically
     * using internal locks or other forms of concurrency control. However, the bulk Collection operations addAll,
     * containsAll, retainAll and removeAll are not necessarily performed atomically unless specified otherwise
     * in an implementation. So it is possible, for example, for addAll(c) to fail (throwing an exception) after
     * adding only some of the elements in c.
     *
     * A BlockingQueue does not intrinsically support any kind of "close" or "shutdown" operation to indicate
     * that no more items will be added. The needs and usage of such features tend to be implementation-dependent.
     * For example, a common tactic is for producers to insert special end-of-stream or poison objects,
     * that are interpreted accordingly when taken by consumers.
     * Usage example, based on a typical producer-consumer scenario. Note that a BlockingQueue can safely
     * be used with multiple producers and multiple consumers.
     */
    private static final BlockingQueue<Integer> queue = new ArrayBlockingQueue<>(10);

    public static void main(String[] args) throws InterruptedException {

        Thread producerThread = new Thread(() -> {
            try {
                producer();
            } catch (InterruptedException ignored) {}
        });

        Thread consumerThread = new Thread(() -> {
            try {
                consumer();
            } catch (InterruptedException ignored) {}
        });

        producerThread.start();
        consumerThread.start();

        // exit (force quit) from infinite loop after 10 seconds
        Thread.sleep(10000);
        System.exit(0);
    }

    private static void producer() throws InterruptedException {
        Random random = new Random();

        while (true) { // infinite loop
            queue.put(random.nextInt(50)); // Inserts the specified element into this queue,
                                                 // waiting if necessary for space to become available.
        }
    }

    private static void consumer() throws InterruptedException {
        Random random = new Random();

        while (true) { // infinite loop
            Thread.sleep(10);

            if (random.nextInt(5) == 0) {
                Integer value = queue.take(); // Retrieves and removes the head of this queue,
                                             // waiting if necessary until an element becomes available.
                System.out.println("Taken value: " + value + "; Queue size: " + queue.size());
            }
        }
    }
}
