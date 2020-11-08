package oauth1.exception;

public class OAuthBackedRequestException extends Exception {
    public OAuthBackedRequestException(String message) {
        super(message);
    }

    public OAuthBackedRequestException(Throwable cause) {
        super(cause);
    }
}
