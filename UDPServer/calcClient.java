import java.net.*;
import java.util.Scanner;

public class calcClient {
    public static void main(String[] args) throws Exception {
        DatagramSocket socket = new DatagramSocket();
        InetAddress address = InetAddress.getByName("localhost");
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter first number: ");
        int a = sc.nextInt();
        System.out.print("Enter second number: ");
        int b = sc.nextInt();
        System.out.print("Enter operation (+ - * /): ");
        char op = sc.next().charAt(0);

        String msg = a + " " + b + " " + op;
        byte[] sendData = msg.getBytes();
        socket.send(new DatagramPacket(sendData, sendData.length, address, 5000));

        byte[] buffer = new byte[1024];
        DatagramPacket receivePacket = new DatagramPacket(buffer, buffer.length);
        socket.receive(receivePacket);
        String reply = new String(receivePacket.getData(), 0, receivePacket.getLength());
        System.out.println(reply);

        socket.close();
    }
}
