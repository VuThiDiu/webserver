import config.ApplicationConfig;
import constants.HttpMethod;
import constants.ResCode;
import dto.HttpRequest;
import service.RequestProcessor;
import utils.Template;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


//TODO: split code and refactor code into another class

public class SimpleWebServer {
    public SimpleWebServer() {
    }


    //TODO: split code
    public static void main(String[] args) {
        ApplicationConfig applicationConfig = ApplicationConfig.getInstance();
        ExecutorService executorService = Executors.newFixedThreadPool(applicationConfig.getThreads());

        /* ServerSocket listening, accepting and management session */
        try (ServerSocket serverSocket = new ServerSocket(applicationConfig.getPort())) {
            while (true) {
                /* socket : accept() waiting util client accept the connection*/
                Socket clientSocket = serverSocket.accept();
                /* Assign each thread for each request */
                executorService.execute(() -> handleRequest(clientSocket));
            }
        } catch (Exception ex) {
//            ex.printStackTrace(); // TODO: reformat into logger
        }
    }


    private static void handleRequest(Socket clientSocket) {
        try (InputStreamReader inputStreamReader = new InputStreamReader(clientSocket.getInputStream());
             BufferedReader inputStream = new BufferedReader(inputStreamReader);
             OutputStream outputStream = clientSocket.getOutputStream()) {
            /*Build http request*/
            RequestProcessor requestProcessor = RequestProcessor.getInstance();
            HttpRequest httpRequest = requestProcessor.formatClientRequest(inputStream);

            /*Build http response for Get request*/

            // TODO: handle response by method and path by structure of each method ( refer dispatcher )
            String response = responseExample(httpRequest.getMethod(), httpRequest.getPath());
            outputStream.write(response.getBytes());
            outputStream.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }


    private static String responseExample(String method, String path) {
        ResCode resCode = ResCode.BAD_REQUEST;
        String content = "";
        if (HttpMethod.GET.equals(method)) {
            if ("/".equals(path)) {
                resCode = ResCode.OK;
                content = "Hello world!";
            } else {
                resCode = ResCode.NOT_FOUND;
                content = "404 Not found!";
            }
        }

        return Template.buildResponse(resCode, content);
    }
}
