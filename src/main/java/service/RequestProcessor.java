package service;

import dto.HttpRequest;
import exception.IncorrectFormatRequestException;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class RequestProcessor {

    /* Singleton pattern */
    private static RequestProcessor instance;

    public static synchronized RequestProcessor getInstance() {
        if (instance == null) {
            instance = new RequestProcessor();
        }
        return instance;
    }

    private RequestProcessor() {
    }


    public HttpRequest formatClientRequest(BufferedReader bufferedReader) throws IOException {
        HttpRequest httpRequest = new HttpRequest();

        processGetMethodAndPath(httpRequest, bufferedReader);
        processGetHeaders(httpRequest, bufferedReader);
        processGetContent(httpRequest, bufferedReader);

        return httpRequest;
    }


    public void processGetMethodAndPath(HttpRequest request, BufferedReader in) throws IOException {
        String str = in.readLine();
        if (str == null) return;

        String[] requestParts = str.split(" ");
        if (requestParts.length < 2) throw new IncorrectFormatRequestException("Invalid Method or Path in request");

        String method = requestParts[0];
        String path = requestParts[1];
        request.setMethod(method);
        request.setContextPath(path);
    }


    public void processGetHeaders(HttpRequest request, BufferedReader in) throws IOException {
        Map<String, String> headers = new HashMap<>();
        String headerString;
        while ((headerString = in.readLine()) != null && !headerString.isEmpty()) {
            String[] splitHeaders = headerString.split(":");
            headers.put(splitHeaders[0].trim(), splitHeaders[1].trim());
        }
        request.setHeaders(headers);
    }

    public void processGetContent(HttpRequest request, BufferedReader in) throws IOException {
        /*if content in line is very large -> out of memory*/
        /*can use readLine() to read all line content of request */
        StringBuilder bodyBuilder = new StringBuilder();
        while (in.ready()) {
            bodyBuilder.append((char) in.read());
        }
        request.setContent(bodyBuilder.toString());
    }
}
