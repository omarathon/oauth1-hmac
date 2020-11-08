package oauth1.exception;

public class TokenMapException extends Exception {
    public TokenMapException(String message) {
        super(message);
    }

    public TokenMapException(Throwable cause) {
        super(cause);
    }
}
