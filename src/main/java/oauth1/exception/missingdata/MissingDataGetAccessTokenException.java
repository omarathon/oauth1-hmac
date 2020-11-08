package oauth1.exception.missingdata;

import oauth1.exception.GetAccessTokenException;

public class MissingDataGetAccessTokenException extends GetAccessTokenException {
    public MissingDataGetAccessTokenException() {
        super("Response or required contained fields are null!");
    }
}
