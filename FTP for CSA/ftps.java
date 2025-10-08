import java.io.*;
import java.net.*;

public class ftps{
    public static void main(String[] args) throws IOException{
        try (ServerSocket serversocket = new ServerSocket(5000))
    {
        Socket socket = serversocket.accept();
        System.out.println("Client Connected");

        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

        out.println("Enter Username:");
        String user = in.readLine();
        out.println("Enter Password:");
        String pass = in.readLine();

        if (!(user.equals("admin") && pass.equals("123")))
        {
            out.println("Failed");
            out.println("EOF");
            return;
        }

        out.println("Authentication successful! You can use READ, WRITE, LIST, EXIT");
        out.println("EOF");

        String command;
        while(((command = in.readLine()) != null))
        {
            String[] parts = command.split(" ");

            if(command.startsWith("READ")){
                
                try
                {
                    File file = new File(parts[1]);
                    if(!file.exists()){
                        out.println("File does not exist");
                        out.println("EOF");
                        continue;
                    }
                    BufferedReader filereader = new BufferedReader(new FileReader(file));
                    String line;
                    while((line = filereader.readLine()) != null)
                    {
                        out.println(line);
                    }
                    out.println("EOF");
                    System.out.println("Sent");
                } catch(Exception e) { 
                    System.out.println(e); 
                }
            }

            else if(command.startsWith("WRITE"))
            {
                try {
                    File file = new File(parts[1]);
                    if(!file.exists()){
                        out.println("File does not exist, creating file");
                    }
                    FileWriter fw = new FileWriter(file);
                    String line;
                    while(!(line = in.readLine()).equals("EOF"))
                    {
                        fw.write(line + "\n");
                    }
                    out.println("Written File");
                    out.println("EOF");
                    System.out.println("Written file");
                    fw.close();
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
            else if (command.startsWith("LIST"))
            {
                try {
                    File file = new File(".");
                    File[] dir = file.listFiles();
                    if(dir != null && dir.length > 0)
                    {
                        for(File f: dir)
                        {
                            if(f.isFile())
                            {
                                out.println(f.getName());
                            }
                        }
                    } else {
                        out.println("No files");
                    }
                    out.println("EOF");

                } catch (Exception e) {
                    System.out.println(e);
                }
            }
            else if (command.equalsIgnoreCase("EXIT")) {
                out.println("Goodbye!");
                out.println("EOF");
                break;
            }
            else{
                out.println("Invalid Command");
                out.println("EOF");
            }
        }
    } catch (Exception e)
    {
        System.out.println(e);
    }
    }
}