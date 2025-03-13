import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;

public class SimpleWebServer {


    public static void main(String[] args) {
        int port = 9000;

        /* ServerSocket listening, accepting and management session */
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (true) {
                /* socket */
                Socket clientSocket = serverSocket.accept();
                /* Assign each thread for each request */
                new Thread(() -> {
                    handleRequest(clientSocket);
                }).start();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    private static void handleRequest(Socket clientSocket) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             OutputStream outputStream = clientSocket.getOutputStream();
        ) {
            /* Get method */
            String readLine = in.readLine();
            if (readLine == null) return;
            String[] requestParts = readLine.split(" ");
            if (requestParts.length < 2) throw new SocketException("Cannot get method and path");
            String method = requestParts[0];
            String path = requestParts[1];


            /* Get headers */
            Map<String, String> headers = new HashMap<>();
            String headerString;
            while ((headerString = in.readLine()) != null && !headerString.isEmpty()) {
                String[] splitHeaders = headerString.split(":");
                headers.put(splitHeaders[0].trim(), splitHeaders[1].trim());
            }

            /* Get content */
            StringBuilder bodyBuilder = new StringBuilder();
            while (in.ready()) {
                bodyBuilder.append((char) in.read());
            }
            String content = bodyBuilder.toString();
            System.out.println("Body : " + content);

            /* Handle content and response to server */



        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                clientSocket.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }
    }
}
