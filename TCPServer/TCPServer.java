import java.io.*;
import java.net.*;

public class TCPServer {
    public static void main(String[] args) throws Exception {
        ServerSocket serverSocket = new ServerSocket(5000);
        System.out.println("Server is listening on port 5000");
        Socket socket = serverSocket.accept();
        System.out.println("Client connected!");
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

        int a = Integer.parseInt(in.readLine());
        int b = Integer.parseInt(in.readLine());
        String op = in.readLine();

        float result = 0;
        switch(op) {
            case "+": result = a + b; break;
            case "-": result = a - b; break;
            case "*": result = a * b; break;
            case "/": result = b != 0 ? a / b : 0; break;
        }

        out.println("Result: " + result);
        socket.close();
        serverSocket.close();
    }
}
