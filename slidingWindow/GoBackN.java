import java.util.Random;

public class GoBackN {
    public static void main(String[] args) {
        Random rand = new Random();
        int totalFrames = 12;   // total frames
        int windowSize = 4;     // window size
        int base = 0;           // base of window

        while (base < totalFrames) {
            int end = Math.min(base + windowSize - 1, totalFrames - 1);
            System.out.println("\nSending packets from " + base + " to " + end);

            boolean error = false;
            int nextBase = base;

            for (int i = base; i <= end; i++) {
                int r = rand.nextInt(100); // probability check
                if (r < 20) { // 20% frame lost
                    System.out.println("Packet " + i + " lost → resending from " + i);
                    nextBase = i;
                    error = true;
                    break;
                }
                else if (r < 25) { // 20% ACK lost
                    System.out.println("Packet " + i + " received, ACK lost → resending from " + i);
                    nextBase = i;
                    error = true;
                    break;
                }
                else if (r < 35) { // 10% ACK delayed
                    System.out.println("Packet " + i + " received, ACK delayed → resending from " + i);
                    nextBase = i;
                    error = true;
                    break;
                }
                else { // success
                    System.out.println("Packet " + i + " received, ACK received");
                    nextBase = i + 1;
                }
            }

            base = nextBase;
        }

        System.out.println("\nAll packets sent successfully!");
    }
}
