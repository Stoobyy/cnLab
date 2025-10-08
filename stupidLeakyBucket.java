//Processes have individual file sizes and dynamic fill rates based on process, constant leak rate
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class stupidLeakyBucket {

    private static final int BUCKET_CAPACITY = 8; // Max bucket capacity
    private static final int LEAK_RATE = 3;       // Leak rate per second

    // Each file has a specific size
    private int[] files = {10, 8, 12, 5}; // p1, p2, p3, p4

    private int bucket = 0;
    private int discarded = 0;

    private final Lock lock = new ReentrantLock();

    // Pattern: how many packets (bytes) arrive from each file
    private static final int[] FILL_PATTERN = {5, 3, 2, 1}; // repeat

    public void leak() {
        while (true) {
            try {
                Thread.sleep(3000);
                lock.lock();

                if (bucket > 0) {
                    int leaked = Math.min(bucket, LEAK_RATE);
                    bucket -= leaked;
                    System.out.println("Leaked " + leaked + " bytes, bucket = " + bucket);
                }

                // Stop when all files done and bucket empty
                if (allFilesDone() && bucket == 0) {
                    System.out.println("\n✅ All files transmitted completely!");
                    System.out.println("Total overflowed = " + discarded + " bytes");
                    break;
                }

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                lock.unlock();
            }
        }
    }

    public void fill() {
        int second = 0;

        while (true) {
            try {
                Thread.sleep(1000);
                lock.lock();

                int fileIndex = second % 4;  // rotate p1→p2→p3→p4
                int toFill = FILL_PATTERN[fileIndex];

                if (files[fileIndex] > 0) {
                    int actualFill = Math.min(toFill, files[fileIndex]);

                    if (bucket + actualFill <= BUCKET_CAPACITY) {
                        bucket += actualFill;
                        files[fileIndex] -= actualFill;
                        System.out.println("Filled " + actualFill + " bytes from p" + (fileIndex + 1)
                                + " | bucket = " + bucket
                                + " | remaining p" + (fileIndex + 1) + " = " + files[fileIndex]);
                    } else {
                        discarded += actualFill;
                        System.out.println("❌ Bucket overflow! Overflow " + actualFill
                                + " bytes from p" + (fileIndex + 1)
                                + " | total overflowed (not discarded) = " + discarded);
                    }
                }

                second++;

                // stop filling once all files done
                if (allFilesDone()) {
                    System.out.println("\nAll packets loaded into bucket. Waiting for leak to finish...");
                    break;
                }

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                lock.unlock();
            }
        }
    }

    private boolean allFilesDone() {
        for (int f : files)
            if (f > 0)
                return false;
        return true;
    }

    public static void main(String[] args) {
        stupidLeakyBucket lb = new stupidLeakyBucket();

        Thread leakThread = new Thread(lb::leak);
        Thread fillThread = new Thread(lb::fill);

        leakThread.start();
        fillThread.start();
    }
}
