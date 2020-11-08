package oauth1.exception.missingdata;

import oauth1.exception.OAuthBackedRequestException;

public class MissingDataOAuthBackedRequestException extends OAuthBackedRequestException {
    public MissingDataOAuthBackedRequestException() {
        super("Response or required contained fields are null!");
    }
}
