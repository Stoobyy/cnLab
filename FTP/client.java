import java.io.*;
import java.net.*;
import java.util.Scanner;


public class client {
    public static void main(String[] args) {
        String server = "localhost"; // Change to server IP if needed
        int port = 4410; // FTP port

        try (Socket socket = new Socket(server, port);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             Scanner scanner = new Scanner(System.in)) {

            String serverResponse;

            // --- Authentication ---
            for (int i = 0; i < 2; i++) {
                serverResponse = in.readLine();
                System.out.println("Server: " + serverResponse);
                String input = scanner.nextLine();
                out.println(input);
            }
            serverResponse = in.readLine();
            System.out.println("Server: " + serverResponse);

            // Exit if authentication failed
            if (serverResponse.toLowerCase().contains("failed")) {
                // consume the trailing EOF
                in.readLine();
                System.out.println("Exiting due to failed authentication.");
                return;
            }

            // consume trailing EOF after success message
            in.readLine();

            // --- Command loop ---
            String command;
            while (true) {
                System.out.print("Enter command (UPLOAD <file> / DOWNLOAD <file> / LIST / EDIT <file> / EXIT): ");
                command = scanner.nextLine().trim();
                out.println(command);

                if (command.equalsIgnoreCase("EXIT")) break;

                if (command.startsWith("UPLOAD")) {
                    String[] parts = command.split(" ");
                    if (parts.length >= 2) {
                        uploadFile(out, parts[1]);
                        out.println("EOF");
                    }
                } else if (command.startsWith("DOWNLOAD")) {
                    String[] parts = command.split(" ");
                    if (parts.length >= 2) {
                        downloadFile(in, parts[1]);
                        continue; // handled inside
                    }
                } else if (command.startsWith("EDIT")) {
                    String[] parts = command.split(" ");
                    if (parts.length >= 2) {
                        editFile(in, out, scanner, parts[1]);
                        continue; // handled inside
                    }
                }

                while (!(serverResponse = in.readLine()).equals("EOF")) {
                    System.out.println("Server: " + serverResponse);
                }
            }
        } catch (IOException e) {
            System.err.println("Connection error: " + e.getMessage());
        }
    }

    private static void uploadFile(PrintWriter out, String filename) {
        try (BufferedReader fileReader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = fileReader.readLine()) != null) {
                out.println(line);
            }
            System.out.println("File " + filename + " uploaded.");
        } catch (IOException e) {
            System.out.println("Error uploading file: " + e.getMessage());
        }
    }

    private static void downloadFile(BufferedReader in, String filename) throws IOException {
        try (FileWriter fw = new FileWriter(filename)) {
            String line;
            while (!(line = in.readLine()).equals("EOF")) {
                fw.write(line + "\n");
            }
            System.out.println("File downloaded and saved as client_" + filename);
        }
    }

    private static void editFile(BufferedReader in, PrintWriter out, Scanner scanner, String filename) throws IOException {
        String line;

        // --- Step 1: Download and save file locally ---
        try (FileWriter fw = new FileWriter("client_edit_" + filename)) {
            while (!(line = in.readLine()).equals("EOF")) {
                fw.write(line + "\n");
                System.out.println(line); // Print file contents to screen
            }
        }
        System.out.println("File downloaded for editing as client_edit_" + filename);

        // --- Step 2: User edits the content ---
        System.out.println("Enter new content for " + filename + " (end with 'EOF'):");
        while (!(line = scanner.nextLine()).equals("EOF")) {
            out.println(line);
        }
        out.println("EOF");

        // --- Step 3: Server confirmation ---
        while (!(line = in.readLine()).equals("EOF")) {
            System.out.println("Server: " + line);
        }
    }
}

