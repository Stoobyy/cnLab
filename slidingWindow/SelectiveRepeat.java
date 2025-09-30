import java.util.Random;

public class SelectiveRepeat {
    public static void main(String[] args) {
        Random rand = new Random();
        int totalFrames = 12;    // total frames to send
        int windowSize = 4;      // window size
        int nextNewFrame = 0;    // next fresh frame index

        int[] window = new int[windowSize];
        int windowCount = 0;

        // fill first window
        while (windowCount < windowSize && nextNewFrame < totalFrames) {
            window[windowCount++] = nextNewFrame++;
        }

        while (windowCount > 0) {
            System.out.print("\nSending packets: ");
            for (int i = 0; i < windowCount; i++) {
                System.out.print(window[i] + " ");
            }
            System.out.println();

            int[] unacked = new int[windowSize];
            int unackedCount = 0;

            for (int i = 0; i < windowCount; i++) {
                int frame = window[i];
                int r = rand.nextInt(100);

                if (r < 20) { // 20% frame lost
                    System.out.println("Packet " + frame + " lost → will retry");
                    unacked[unackedCount++] = frame;
                }
                else if (r < 40) { // 20% ACK lost
                    System.out.println("Packet " + frame + " received, ACK lost → will retry");
                    unacked[unackedCount++] = frame;
                }
                else if (r < 50) { // 10% delayed ACK
                    System.out.println("Packet " + frame + " received, ACK delayed → will retry");
                    unacked[unackedCount++] = frame;
                }
                else { // success
                    System.out.println("Packet " + frame + " received, ACK received");
                }
            }

            // build next window: unacked first
            windowCount = 0;
            for (int i = 0; i < unackedCount; i++) {
                window[windowCount++] = unacked[i];
            }
            while (windowCount < windowSize && nextNewFrame < totalFrames) {
                window[windowCount++] = nextNewFrame++;
            }
        }

        System.out.println("\nAll packets sent successfully!");
    }
}
