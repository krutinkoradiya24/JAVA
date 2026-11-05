// Unsynchronized counter
class UnsynchronizedCounter {
    private int count = 0;
    public void increment() {
        count++;
    }
    public int getCount() {
        return count;
    }
}
// Synchronized thread-safe counter
class SynchronizedCounter {
    private int count = 0;
    public synchronized void increment() {
        count++;
    }
    public int getCount() {
        return count;
    }
}
public class CounterRace {
    public static void main(String[] args) throws InterruptedException {
        int numberOfThreads = 10;
        int incrementsPerThread = 10000;
        int expected = numberOfThreads * incrementsPerThread;
        // 1. Without synchronization (race condition)
        UnsynchronizedCounter unsafeCounter = new UnsynchronizedCounter();
        Thread[] unsafeThreads = new Thread[numberOfThreads];
        for (int i = 0; i < numberOfThreads; i++) {
            unsafeThreads[i] = new Thread(() -> {
                for (int j = 0; j < incrementsPerThread; j++) {
                    unsafeCounter.increment();
                }
            });
            unsafeThreads[i].start();
        }
        for (Thread t : unsafeThreads) {
            t.join();
        }
        System.out.println("Without Synchronization:");
        System.out.println("Expected: " + expected);
        System.out.println("Actual:   " + unsafeCounter.getCount());
        // 2. With synchronization (correct total)
        SynchronizedCounter safeCounter = new SynchronizedCounter();
        Thread[] safeThreads = new Thread[numberOfThreads];
        for (int i = 0; i < numberOfThreads; i++) {
            safeThreads[i] = new Thread(() -> {
                for (int j = 0; j < incrementsPerThread; j++) {
                    safeCounter.increment();
                }
            });
            safeThreads[i].start();
        }
        for (Thread t : safeThreads) {
            t.join();
        }
        System.out.println("\nWith Synchronization:");
        System.out.println("Expected: " + expected);
        System.out.println("Actual:   " + safeCounter.getCount());
    }
}
