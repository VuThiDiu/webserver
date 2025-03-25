package exception;

public class InvalidSession extends  RuntimeException {
    public InvalidSession(String message) {
        super(message);
    }
}
