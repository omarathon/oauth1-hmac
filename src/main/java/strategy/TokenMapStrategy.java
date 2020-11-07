package strategy;

import exception.TokenMapException;

public interface TokenMapStrategy {
    void mapTemporaryTokenToTemporaryTokenSecret(String temporaryToken, String temporaryTokenSecret) throws TokenMapException;

    String getTemporaryTokenSecretForTemporaryToken(String temporaryToken) throws TokenMapException;
}
