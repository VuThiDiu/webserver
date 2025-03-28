package dto;

import lombok.Data;

import java.util.Map;

@Data
public class HttpRequest {
    private String method; // GET, POST, PUT, DELETE
    private String contextPath; // path
    private String version; // HTTP/1.1
    private String serverName; // domain name of server


    private Map<String, String> headers;
    private String content;
    private String session;



    /*private String contentType; //  Content type ( request _ response )
    private String connection; //  Keep-Alive or Close
    private String userAgent; //  Indentify the browser or client app

    private String accept; // Client Accept type response ( response )
    private String acceptLanguage; //  Accept language ( response )
    private String acceptEncoding; //  Accept encoding ( response )
    private String referer; */


    /* Different key between Accept and Content-Type:
    *  - accept: Tells the server what media types the client can accept in the response.
    *  - content-type: Tells the server what media type the client is sending in the request.
    * */

}
