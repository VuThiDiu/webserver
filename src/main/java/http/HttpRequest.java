package http;

import lombok.Data;

import java.util.Map;


@Data
public class HttpRequest {
    private String method;
    private String path;
    private Map<String, String> headers;
    private String content;
}
