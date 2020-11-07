package strategy.preset;

import exception.TokenMapException;
import strategy.TokenMapStrategy;

import java.util.HashMap;
import java.util.Map;

public class HashMapTokenMapStrategy implements TokenMapStrategy {
    private static Map<String, String> temporaryTokenTokenSecretMap = new HashMap<String, String>();

    public void mapTemporaryTokenToTemporaryTokenSecret(String temporaryToken, String temporaryTokenSecret) throws TokenMapException {
        temporaryTokenTokenSecretMap.put(temporaryToken, temporaryTokenSecret);
    }
}
