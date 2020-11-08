package oauth1.exception;

public class GetAccessTokenException extends Exception {
    public GetAccessTokenException(String message) {
        super(message);
    }

    public GetAccessTokenException(Throwable cause) {
        super(cause);
    }
}
