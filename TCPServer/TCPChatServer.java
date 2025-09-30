import java.io.*;
import java.net.*;
import java.util.Scanner;

public class TCPChatServer {
    public static void main(String[] args) throws Exception {
        ServerSocket serverSocket = new ServerSocket(5000);
        Socket socket = serverSocket.accept();

        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        Scanner sc = new Scanner(System.in);

        String msg, reply;
        while (true) {
            msg = in.readLine();
            if (msg.equalsIgnoreCase("exit")) break;
            System.out.println("Client: " + msg);
            System.out.print("Server: ");
            reply = sc.nextLine();
            out.println(reply);
            if (reply.equalsIgnoreCase("exit")) break;
        }
        socket.close();
        serverSocket.close();
    }
}
