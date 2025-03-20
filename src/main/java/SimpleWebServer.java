import service.ReceiverRequest;

public class SimpleWebServer {

    public static void main(String[] args) {
        ReceiverRequest request = new ReceiverRequest();
        request.listenRequest();
    }
}
