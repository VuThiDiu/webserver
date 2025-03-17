import config.ApplicationConfig;
import constants.MethodConstants;
import constants.ResCode;
import http.HttpRequest;
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

public class SimpleWebServer {


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
            ex.printStackTrace();
        }
    }


    private static void handleRequest(Socket clientSocket) {
        try (BufferedReader inputStream = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             OutputStream outputStream = clientSocket.getOutputStream()) {
            /*Build http request*/
            RequestProcessor requestProcessor = RequestProcessor.getInstance();
            HttpRequest httpRequest = requestProcessor.fromClientRequest(inputStream);

            /*Build http response for Get request*/
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
        if (MethodConstants.GET.equals(method)) {
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
