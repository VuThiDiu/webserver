package service;

import config.ApplicationConfig;
import constants.ResCode;
import controller.HomeController;
import dto.HttpRequest;
import exception.MethodNotDeclared;
import utils.Template;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class ReceiverRequest {
    private ExecutorService executorService;
    private ApplicationConfig applicationConfig;
    private Dispatcher dispatcher;

    public ReceiverRequest() {
        applicationConfig = ApplicationConfig.getInstance();
        executorService = Executors.newFixedThreadPool(applicationConfig.getThreads());
        dispatcher = Dispatcher.getInstance();

        // TODO: automatically scan all controllers in the package
        dispatcher.setController(new HomeController());
    }


    public void listenRequest() {
        /* ServerSocket listening, accepting and management session */
        try (ServerSocket serverSocket = new ServerSocket(applicationConfig.getPort())) {
            while (true) {
                /* socket : accept() waiting util client accept the connection*/
                Socket clientSocket = serverSocket.accept();
                /* Assign each thread for each request */
                executorService.execute(() -> handleRequest(clientSocket));
            }
        } catch (Exception ex) {
        }
    }

    private void handleRequest(Socket clientSocket) {
        try (InputStreamReader inputStreamReader = new InputStreamReader(clientSocket.getInputStream());
             BufferedReader inputStream = new BufferedReader(inputStreamReader);
             OutputStream outputStream = clientSocket.getOutputStream()) {
            /*Build http request*/
            RequestProcessor requestProcessor = RequestProcessor.getInstance();
            HttpRequest httpRequest = requestProcessor.formatClientRequest(inputStream);

            /*Build http response for Get request*/

            Object responseData = dispatcher.execute(httpRequest.getMethod(), httpRequest.getPath(), httpRequest.getContent());

            /*Format response by contentType*/
            String accept = httpRequest.getHeaders().get("Accept");
            String contentType = httpRequest.getHeaders().get("Content-Type");
            if (accept != null && !"*/*".equals(accept)) contentType = accept;

            outputStream.write(formatResponse(ResCode.OK, responseData, contentType).getBytes());
            outputStream.flush();
        } catch (MethodNotDeclared e) {
            // TODO: Response 404
            e.printStackTrace();
        } catch (Exception ex) {
            // TODO: Response 500
            ex.printStackTrace();
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                // TODO : response 404
                e.printStackTrace();
            }
        }
    }

    private static String formatResponse(ResCode rescode, Object responseBody, String contentType) {
        contentType = contentType == null ? "text/plain" : contentType;
        return Template.buildResponse(rescode, responseBody.toString(), contentType);
    }

}
