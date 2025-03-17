package exception;

public class IncorrectFormatRequestException extends  RuntimeException {
    public IncorrectFormatRequestException(String message) {
        super(message);
    }
}
