package oauth1.exception.missingdata;

import oauth1.exception.TokenMapException;

public class MissingDataTokenMapException extends TokenMapException {
    public MissingDataTokenMapException() {
        super("Fetched token secret for the given temporary token is null!");
    }
}
