package oauth1.strategy;

import oauth1.exception.TokenMapException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * An interface which provides a strategy to persistently map temporary tokens to temporary token secrets.
 */
public interface TokenMapStrategy {
    /**
     * Persistently map the given temporary token to the given temporary token secret.
     * <br>
     * This mapping should persist for the maximal permissible duration of the OAuth handshake procedure (e.g. 5 mins).
     * <br>
     * To be safe, the implementation would use a database and persist the mapping for a long duration, or permanently.
     * @param temporaryToken The temporary token, i.e. the "key" of the mapping. In the future, we will fetch from the map this key, and return its corresponding temporary token secret.
     * @param temporaryTokenSecret The temporary token secret, i.e. the "value" of the mapping. In the future, this value will be returned from the map after fetching its corresponding temporary token.
     * @throws TokenMapException If the mapping failed.
     */
    void mapTemporaryTokenToTemporaryTokenSecret(@Nonnull String temporaryToken, @Nonnull String temporaryTokenSecret) throws TokenMapException;

    /**
     * Retrieve the value of a temporary token secret mapped to the given temporary token, which was mapped in the past via {@link TokenMapStrategy#mapTemporaryTokenToTemporaryTokenSecret(String, String)}.
     * @param temporaryToken The temporary token, i.e. the "key" which we would like to retrieve the value of in the map.
     * @return The temporary token secret corresponding to the given temporary token, if such a mapping exists, otherwise null.
     * @throws TokenMapException If the lookup failed.
     */
    @Nullable
    String fetchTemporaryTokenSecretForTemporaryToken(@Nonnull String temporaryToken) throws TokenMapException;
}
