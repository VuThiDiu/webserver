package dto;

import lombok.Data;

import java.util.Map;



//TODO: edit follow by standard of HTTP Request and Response


@Data
public class HttpRequest {
    private String method;
    private String path;
    private Map<String, String> headers;
    private String content;
}
