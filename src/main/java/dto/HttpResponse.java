package dto;


import lombok.Data;

@Data
public class HttpResponse {
    public String method;
    public String code;
    public String des;
    public String contentType;
    public String contentLength;
}
