import java.net.*;

public class calcServer {
    public static void main(String[] args) throws Exception {
        DatagramSocket socket = new DatagramSocket(5000);
        byte[] buffer = new byte[1024];

        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        socket.receive(packet);
        String data = new String(packet.getData(), 0, packet.getLength());

        String[] parts = data.split(" ");
        int a = Integer.parseInt(parts[0]);
        int b = Integer.parseInt(parts[1]);
        char op = parts[2].charAt(0);

        int result = 0;
        switch(op) {
            case '+': result = a + b; break;
            case '-': result = a - b; break;
            case '*': result = a * b; break;
            case '/': result = b != 0 ? a / b : 0; break;
        }

        String resStr = "Result: " + result;
        byte[] sendData = resStr.getBytes();
        socket.send(new DatagramPacket(sendData, sendData.length, packet.getAddress(), packet.getPort()));

        socket.close();
    }
}
