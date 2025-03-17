package service;

import exception.IncorrectFormatRequestException;
import http.HttpRequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


// comment: Neu da la class thi phai la N
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


    public HttpRequest fromClientRequest(BufferedReader bufferedReader) throws IOException {
        HttpRequest httpRequest = new HttpRequest();

        getMethodAndPath(httpRequest, bufferedReader);
        getHeaders(httpRequest, bufferedReader);
        getContent(httpRequest, bufferedReader);

        return httpRequest;
    }


    public void getMethodAndPath(HttpRequest request, BufferedReader in) throws IOException {
        String str = in.readLine();
        if (str == null) return;
        String[] requestParts = str.split(" ");
        if (requestParts.length < 2) throw new IncorrectFormatRequestException("Invalid Method or Path in request");
        String method = requestParts[0];
        String path = requestParts[1];
        request.setMethod(method);
        request.setPath(path);
    }

    public void getHeaders(HttpRequest request, BufferedReader in) throws IOException {
        Map<String, String> headers = new HashMap<>();
        String headerString;
        while ((headerString = in.readLine()) != null && !headerString.isEmpty()) {
            String[] splitHeaders = headerString.split(":");
            headers.put(splitHeaders[0].trim(), splitHeaders[1].trim());
        }
        request.setHeaders(headers);
    }

    public void getContent(HttpRequest request, BufferedReader in) throws IOException {
        StringBuilder bodyBuilder = new StringBuilder();
        while (in.ready()) {
            bodyBuilder.append((char) in.read());
        }
        request.setContent(bodyBuilder.toString());
    }
}
