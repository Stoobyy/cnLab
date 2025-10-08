import java.io.*;
import java.net.*;
import java.util.*;

public class ftpc {
        public static void main(String[] args) throws IOException{
            try(Socket socket = new Socket("localhost", 5000))
            {
                System.out.println("Connected");
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                Scanner scanner = new Scanner(System.in);
                for(int i=0; i<2; i++)
                {
                    System.out.println(in.readLine());
                    String input = scanner.nextLine();
                    out.println(input);
                }

                String response;
                response = in.readLine();
                if(response.equals("Failed"))
                {
                    System.out.println(response);
                    in.readLine();
                    return;
                }
                System.out.println(response);
                in.readLine();
                String command;
                while (true) {
                    System.out.print("Enter Command: ");
                    command = scanner.nextLine();
                    out.println(command);
                    if(command.equals("EXIT"))
                    {
                        System.out.println("Exiting...");
                        break;
                    }
                    String[] parts = command.split(" ");
                    if(command.startsWith("WRITE"))
                    {
                        System.out.println("Enter file content (end with a single line containing EOF):");
                        String line;
                        while(!(line = scanner.nextLine()).equals("EOF"))
                        {
                            out.println(line);
                        }
                        out.println("EOF");
                        System.out.println(in.readLine());
                        in.readLine();
                    }
                    else if(command.startsWith("READ") || command.equals("LIST"))
                    {
                        System.out.println("Server:");
                        while(!(response = in.readLine()).equals("EOF"))
                        {
                            System.out.println(response);
                        }

                    }
                    else
                    {
                        System.out.println(in.readLine());
                        in.readLine();
                    }
                }
            } catch(Exception e)
            {
                System.out.println(e);
            }
        }
}
