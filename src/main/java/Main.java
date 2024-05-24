import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.Buffer;
import java.nio.charset.StandardCharsets;
public class Main {
    public static void main(String[] args) throws IOException {
        System.out.println("Logs from your program will appear here!");
        ServerSocket serverSocket = null;
        int port = 6379;
        serverSocket = new ServerSocket(port);
        serverSocket.setReuseAddress(true);
        while (true) {
            Socket clientSocket = serverSocket.accept();
//            handlePingCommand(clientSocket);
            new Thread(() -> {
                try {
                    handlePingCommand(clientSocket);
                } catch (Exception e) {
                    System.out.println("Exception: " + e.getMessage());
                }
            }).start();
        }
    }
    private static void handlePingCommand(Socket clientSocket) {
        OutputStream out = null;
        BufferedReader in = null;
        OutputStreamWriter writer = null;
        try {
            out = clientSocket.getOutputStream();
            writer = new OutputStreamWriter(out, StandardCharsets.UTF_8);
            in = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream()));
            String line = null;
            while ((line = in.readLine()) != null) {
                if (line.toUpperCase().contains("PING")) {
                    writer.write("+PONG\r\n");
                    writer.flush();
                }
            }
            System.out.println("closing the outputStram for client--");
        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        } finally {
            try {
                if(writer != null) {
                    writer.close();
                }
                if (in != null) {
                    in.close();
                    clientSocket.close();
                }
            } catch (IOException e) {
                System.out.println("IOException: " + e.getMessage());
            }
        }
    }
}