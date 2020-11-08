package oauth1.exception.missingdata;

import oauth1.exception.GetTemporaryTokenException;

public class MissingDataGetTemporaryTokenException extends GetTemporaryTokenException {
    public MissingDataGetTemporaryTokenException() {
        super("Response or required contained fields are null!");
    }
}
