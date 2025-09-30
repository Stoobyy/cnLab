import java.io.*;
import java.net.*;

public class server {
    public static void main(String[] args) {
        int port = 4410; // FTP default port

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("FTP Server is listening on port " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected: " + clientSocket.getInetAddress());

                // Handle client in a new thread
                new ClientHandler(clientSocket).start();
            }
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}

class ClientHandler extends Thread {
    private Socket socket;
    private static final String USERNAME = "admin";
    private static final String PASSWORD = "1234";

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            // --- Simple Auth ---
            out.println("Enter username:");
            String user = in.readLine();
            out.println("Enter password:");
            String pass = in.readLine();

            if (!USERNAME.equals(user) || !PASSWORD.equals(pass)) {
                out.println("Authentication failed. Closing connection.");
                out.println("EOF");
                socket.close();
                return;
            }
            out.println("Authentication successful! You can use UPLOAD, DOWNLOAD, LIST, EDIT, EXIT");
            out.println("EOF");

            // --- Command loop ---
            String command;
            while ((command = in.readLine()) != null) {
                System.out.println("Received: " + command);

                if (command.startsWith("UPLOAD")) {
                    uploadFile(in, out, command.split(" ")[1]);
                } else if (command.startsWith("DOWNLOAD")) {
                    downloadFile(out, command.split(" ")[1]);
                } else if (command.equalsIgnoreCase("LIST")) {
                    listFiles(out);
                } else if (command.startsWith("EDIT")) {
                    editFile(in, out, command.split(" ")[1]);
                } else if (command.equalsIgnoreCase("EXIT")) {
                    out.println("Goodbye!");
                    out.println("EOF");
                    break;
                } else {
                    out.println("Invalid Command");
                    out.println("EOF");
                }
            }
        } catch (IOException e) {
            System.err.println("Client disconnected: " + e.getMessage());
        } finally {
            try { socket.close(); } catch (IOException e) { e.printStackTrace(); }
        }
    }

    private void listFiles(PrintWriter out) {
        File dir = new File(".");
        File[] files = dir.listFiles();
        if (files != null && files.length > 0) {
            out.println("Files on server:");
            for (File f : files) {
                if (f.isFile()) {
                    out.println(f.getName());
                }
            }
        } else {
            out.println("No files found.");
        }
        out.println("EOF"); // end of list
    }

    private void uploadFile(BufferedReader in, PrintWriter out, String filename) throws IOException {
        try (FileWriter fw = new FileWriter(filename)) {
            String line;
            while (!(line = in.readLine()).equals("EOF")) {
                fw.write(line + "\n");
            }
            System.out.println("File " + filename + " uploaded successfully.");
        }
        out.println("File " + filename + " uploaded successfully.");
        out.println("EOF");
    }

    private void downloadFile(PrintWriter out, String filename) throws IOException {
        File file = new File(filename);
        if (file.exists()) {
            try (BufferedReader fileReader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = fileReader.readLine()) != null) {
                    out.println(line);
                }
            }
            out.println("EOF"); // Indicate end of file transmission
            System.out.println("File " + filename + " downloaded successfully.");
        } else {
            out.println("File not found");
            out.println("EOF");
        }
    }

    private void editFile(BufferedReader in, PrintWriter out, String filename) throws IOException {
        File file = new File(filename);

        // Send current content
        if (file.exists()) {
            try (BufferedReader fileReader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = fileReader.readLine()) != null) {
                    out.println(line);
                }
            }
        } else {
            out.println("File not found, creating new file.");
        }
        out.println("EOF");

        // Receive new content
        try (FileWriter fw = new FileWriter(file)) {
            String line;
            while (!(line = in.readLine()).equals("EOF")) {
                fw.write(line + "\n");
            }
        }
        out.println("File " + filename + " updated successfully.");
        out.println("EOF");
        System.out.println("File " + filename + " edited.");
    }
}

