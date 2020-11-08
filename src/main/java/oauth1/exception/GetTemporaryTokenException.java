package oauth1.exception;

public class GetTemporaryTokenException extends Exception {
    public GetTemporaryTokenException(String message) {
        super(message);
    }

    public GetTemporaryTokenException(Throwable cause) {
        super(cause);
    }
}
