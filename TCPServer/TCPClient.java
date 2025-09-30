import java.io.*;
import java.net.*;
import java.util.Scanner;

public class TCPClient {
    public static void main(String[] args) throws Exception {
        Socket socket = new Socket("localhost", 5000);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter first number: ");
        out.println(sc.nextInt());
        System.out.print("Enter second number: ");
        out.println(sc.nextInt());
        System.out.print("Enter operation (+ - * /): ");
        out.println(sc.next().charAt(0));

        String reply = in.readLine();
        System.out.println(reply);
        socket.close();
    }
}
