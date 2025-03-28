package constants;


import lombok.Getter;

@Getter
public enum HttpMethod {
    GET("GET"),
    POST("POST"),
    PUT("PUT"),
    DELETE("DELETE");

    HttpMethod(String method) {
        this.method = method;
    }

    private final String method;
}
