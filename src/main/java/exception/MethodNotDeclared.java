package exception;

public class MethodNotDeclared extends RuntimeException {
    public MethodNotDeclared(String message) {
        super(message);
    }
}
