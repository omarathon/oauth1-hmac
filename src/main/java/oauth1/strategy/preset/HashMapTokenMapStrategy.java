package oauth1.strategy.preset;

import oauth1.exception.TokenMapException;
import oauth1.strategy.TokenMapStrategy;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

/**
 * A preset implementation of {@link TokenMapStrategy}, simply using a {@link HashMap}.
 * <br>
 * In practise, it's highly recommended to use a database instead, i.e. a custom implementation of {@link TokenMapStrategy}.
 */
public class HashMapTokenMapStrategy implements TokenMapStrategy {
    /**
     * The map which persistently maps temporary tokens to temporary token secrets.
     */
    public static final Map<String, String> TEMPORARY_TOKEN_TOKEN_SECRET_MAP = new HashMap<String, String>();

    /**
     * Uses {@link Map#put(Object, Object)} to map the given temporary token to the given temporary token secret.
     */
    public void mapTemporaryTokenToTemporaryTokenSecret(@Nonnull String temporaryToken, @Nonnull String temporaryTokenSecret) throws TokenMapException {
        TEMPORARY_TOKEN_TOKEN_SECRET_MAP.put(temporaryToken, temporaryTokenSecret);
    }

    /**
     * Uses {@link Map#get(Object)} to obtain the temporary token secret mapped to the given temporary token.
     */
    @Nullable
    public String fetchTemporaryTokenSecretForTemporaryToken(@Nonnull String temporaryToken) throws TokenMapException {
        return TEMPORARY_TOKEN_TOKEN_SECRET_MAP.get(temporaryToken);
    }
}
