import java.net.*;
import java.util.Scanner;

public class chatClient {
    public static void main(String[] args) throws Exception {
        DatagramSocket socket = new DatagramSocket(6000);
        Scanner sc = new Scanner(System.in);

        Thread receiveThread = new Thread(() -> {
            try {
                byte[] buffer = new byte[1024];
                while (true) {
                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                    socket.receive(packet);
                    String msg = new String(packet.getData(), 0, packet.getLength());
                    if (msg.equalsIgnoreCase("exit")) break;
                    System.out.println("Server: " + msg);
                }
            } catch (Exception e) {}
        });

        Thread sendThread = new Thread(() -> {
            try {
                while (true) {
                    String msg = sc.nextLine();
                    byte[] data = msg.getBytes();
                    DatagramPacket packet = new DatagramPacket(data, data.length, InetAddress.getByName("localhost"), 5000);
                    socket.send(packet);
                    if (msg.equalsIgnoreCase("exit")) break;
                }
            } catch (Exception e) {}
        });

        receiveThread.start();
        sendThread.start();
    }
}
