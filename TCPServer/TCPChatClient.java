import java.io.*;
import java.net.*;
import java.util.Scanner;

public class TCPChatClient {
    public static void main(String[] args) throws Exception {
        Socket socket = new Socket("localhost", 5000);

        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        Scanner sc = new Scanner(System.in);

        String msg, reply;
        while (true) {
            System.out.print("Client: ");
            msg = sc.nextLine();
            out.println(msg);
            if (msg.equalsIgnoreCase("exit")) break;
            reply = in.readLine();
            if (reply.equalsIgnoreCase("exit")) break;
            System.out.println("Server: " + reply);
        }
        socket.close();
    }
}
