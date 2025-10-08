import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class leakyBucket {

    private static final int BUCKET_CAPACITY = 30; // Maximum capacity of the bucket
    private static final int LEAK_RATE = 5;        // Leak rate per second
    private static final int FILL_RATE = 10;       // Fill rate per second
    private static final int FILE_SIZE = 100;      // Total file size in bytes

    private int bucket = 0;                        // Current amount of data in bucket
    private int remainingFile = FILE_SIZE;         // Bytes left to send
    private int discarded = 0;                     // Total discarded bytes due to overflow

    private final Lock lock = new ReentrantLock();

    public void leak() {
        while (true) {
            try {
                Thread.sleep(1000);
                lock.lock();

                if (bucket > 0) {
                    int leaked = Math.min(bucket, LEAK_RATE);
                    bucket -= leaked;
                    System.out.println("Leaked " + leaked + " bytes, bucket = " + bucket);
                }

                // Exit condition: file finished sending AND bucket is empty
                if (remainingFile <= 0 && bucket == 0) {
                    System.out.println("\nFile fully transmitted and emptied.");
                    System.out.println("Total discarded = " + discarded + " bytes");
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
        while (true) {
            try {
                Thread.sleep(1000);
                lock.lock();

                if (remainingFile > 0) {
                    int toFill = Math.min(FILL_RATE, remainingFile);

                    if (bucket + toFill <= BUCKET_CAPACITY) {
                        remainingFile -= toFill;
                        bucket += toFill;
                        System.out.println("Filled " + toFill + " bytes, bucket = " + bucket +
                                           ", remaining file = " + remainingFile);
                    } else {
                        discarded += toFill;
                        System.out.println("Bucket overflow! Discarded " + toFill +
                                           " bytes, total discarded = " + discarded);
                    }
                }

                // Stop filling once file has been fully pushed into the bucket
                if (remainingFile <= 0) {
                    break;
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                lock.unlock();
            }
        }
    }

    public static void main(String[] args) {
        leakyBucket lb = new leakyBucket();

        Thread leakThread = new Thread(lb::leak);
        Thread fillThread = new Thread(lb::fill);

        leakThread.start();
        fillThread.start();
    }
}
