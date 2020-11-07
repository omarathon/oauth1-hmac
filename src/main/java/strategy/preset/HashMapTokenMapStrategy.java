package strategy.preset;

import exception.TokenMapException;
import strategy.TokenMapStrategy;

import java.util.HashMap;
import java.util.Map;

public class HashMapTokenMapStrategy implements TokenMapStrategy {
    public static final Map<String, String> TEMPORARY_TOKEN_TOKEN_SECRET_MAP = new HashMap<String, String>();

    public void mapTemporaryTokenToTemporaryTokenSecret(String temporaryToken, String temporaryTokenSecret) throws TokenMapException {
        TEMPORARY_TOKEN_TOKEN_SECRET_MAP.put(temporaryToken, temporaryTokenSecret);
    }

    public String getTemporaryTokenSecretForTemporaryToken(String temporaryToken) throws TokenMapException {
        return TEMPORARY_TOKEN_TOKEN_SECRET_MAP.get(temporaryToken);
    }
}
