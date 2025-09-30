import java.util.Random;

public class StopAndWait {
    public static void main(String[] args) {
        Random rand = new Random();
        int totalFrames = 5; // number of frames to send
        int seq = 0;         // sequence number (0/1)

        for (int i = 0; i < totalFrames; i++) {
            boolean acked = false;

            while (!acked) {
                System.out.println("Sender: Sending frame " + seq);

                int r = rand.nextInt(100); // 0–99
                int event;
                if (r < 20) event = 1;       // 20% frame lost
                else if (r < 40) event = 2;  // 20% ACK lost
                else if (r < 50) event = 3;  // 10% ACK delayed
                else event = 0;              // 50% success

                if (event == 0) {
                    System.out.println("Receiver: Frame " + seq + " received, ACK " + seq + " sent");
                    System.out.println("Sender: ACK " + seq + " received\n");
                    acked = true; // success → move on
                }
                else if (event == 1) {
                    System.out.println("Frame " + seq + " lost!");
                    System.out.println("Sender: Timeout → retransmitting frame " + seq);
                }
                else if (event == 2) {
                    System.out.println("Receiver: Frame " + seq + " received, but ACK lost!");
                    System.out.println("Sender: Timeout → retransmitting frame " + seq);
                }
                else {
                    System.out.println("Receiver: Frame " + seq + " received, ACK delayed!");
                    System.out.println("Sender: Timeout → retransmitting frame " + seq);
                    System.out.println("Receiver: Duplicate discarded, ACK already sent");
                    System.out.println("Sender: Finally receives delayed ACK " + seq + "\n");
                    acked = true; // delayed ACK eventually received
                }
            }

            seq = 1 - seq; // toggle sequence number
        }

        System.out.println("All frames sent successfully!");
    }
}
