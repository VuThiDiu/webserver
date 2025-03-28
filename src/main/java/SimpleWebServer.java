import service.RequestHandler;

public class SimpleWebServer {

    public static void main(String[] args) {
        RequestHandler requestHandler = new RequestHandler();
        requestHandler.listenRequest();
    }
}
