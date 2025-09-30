import java.net.*;
import java.util.Scanner;

public class chatServer {
    public static void main(String[] args) throws Exception {
        DatagramSocket socket = new DatagramSocket(5000);
        Scanner sc = new Scanner(System.in);

        Thread receiveThread = new Thread(() -> {
            try {
                byte[] buffer = new byte[1024];
                while (true) {
                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                    socket.receive(packet);
                    String msg = new String(packet.getData(), 0, packet.getLength());
                    if (msg.equalsIgnoreCase("exit")) break;
                    System.out.println("Client: " + msg);
                }
            } catch (Exception e) {}
        });

        Thread sendThread = new Thread(() -> {
            try {
                while (true) {
                    String reply = sc.nextLine();
                    byte[] data = reply.getBytes();
                    DatagramPacket packet = new DatagramPacket(data, data.length, InetAddress.getByName("localhost"), 6000);
                    socket.send(packet);
                    if (reply.equalsIgnoreCase("exit")) break;
                }
            } catch (Exception e) {}
        });

        receiveThread.start();
        sendThread.start();
    }
}
