package oauth1;

import com.google.api.client.auth.oauth.OAuthParameters;
import com.google.api.client.http.HttpContent;
import oauth1.exception.GetAccessTokenException;
import oauth1.exception.GetTemporaryTokenException;
import oauth1.exception.TokenMapException;
import oauth1.lib.AuthorisedResult;
import oauth1.lib.BeginResult;
import oauth1.lib.RequestMethod;
import oauth1.strategy.TokenMapStrategy;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * An OAuth1.0 consumer class, using the HMAC signing strategy.
 * <br><br>
 * This is a consumer supporting a callback.
 * <br><br>
 * Usage:
 * <br>
 * 1. Call {@link OAuth1WithCallback#begin()} to obtain a temporary token and a redirect URL if {@link OAuth1WithCallback#authoriseUrl} was provided (not null).
 * <br>
 * 2. On the front-end, redirect the user to the redirect URL obtained in (1), or compute the redirect URL from the temporary token obtained in (1).
 * <br>
 * 3. Once the user has granted access on the redirect URL (usually detected via the callback), call {@link OAuth1WithCallback#authorised(String, String)} with the temporary token obtained in (1), and the verifier (provided as a parameter to the callback, as parsed on the front-end).
 * <br>
 * 4. If (3) succeeded, you now have an access token and an {@link OAuthParameters} object that can be used to make requests on the user's behalf. Using the {@link OAuthParameters} make a request to a given URL with a given HTTP request method and body via {@link OAuth1WithCallback#makeOAuthBackedRequest(String, OAuthParameters, RequestMethod, HttpContent)}.
 * @author Omar Tanner
 */
public class OAuth1WithCallback extends OAuth1Generic {
    /**
     * @param consumerKey Required identifier portion of the client credentials (equivalent to a username). More information: client_id entry of the OAuthParameters table at https://www.iana.org/assignments/oauth-parameters/oauth-parameters.xhtml
     * @param consumerSecret Client-shared secret. More information: client_id entry of the OAuthParameters table at https://www.iana.org/assignments/oauth-parameters/oauth-parameters.xhtml
     * @param requestTokenUrl Encoded authorization server URL. More information: request_uri entry of the OAuthParameters table at https://www.iana.org/assignments/oauth-parameters/oauth-parameters.xhtml
     * @param callbackUrl Optional absolute URI back to which the server will redirect the resource owner when the Resource Owner Authorization step is completed or null for none. More information: redirect_uri entry of the OAuthParameters table at https://www.iana.org/assignments/oauth-parameters/oauth-parameters.xhtml
     * @param authoriseUrl Encoded user authorization URL, or null if only wish to receive the temporary token and build it themselves.
     * @param accessTokenUrl Encoded authorization server URL.
     * @param tokenMapStrategy Strategy to map temporary tokens to token secrets, and fetch the token secret for a temporary token.
     */
    public OAuth1WithCallback(
            @Nonnull String consumerKey,
            @Nonnull String consumerSecret,
            @Nonnull String requestTokenUrl,
            @Nonnull String callbackUrl,
            @Nullable String authoriseUrl,
            @Nonnull String accessTokenUrl,
            @Nonnull TokenMapStrategy tokenMapStrategy
    ) {
        super(
                consumerKey,
                consumerSecret,
                requestTokenUrl,
                callbackUrl,
                authoriseUrl,
                accessTokenUrl,
                tokenMapStrategy
        );
    }

    /**
     * Requests a temporary token and corresponding token secret from the server,
     * then maps the temporary token to the token secret
     * and returns the obtained temporary token along with the authenticate URL (redirect URL) if {@link OAuth1WithCallback#authoriseUrl} is non-null.
     *
     * @return Temporary token from the server and the authenticate URL (redirect URL) if if {@link OAuth1WithCallback#authoriseUrl} is non-null, otherwise null.
     * @throws GetTemporaryTokenException If the request to the server for the temporary token failed.
     * @throws TokenMapException          If mapping the obtained temporary token to the obtained token secret failed.
     */
    @Override
    @Nonnull
    public BeginResult begin() throws GetTemporaryTokenException, TokenMapException {
        return super.begin();
    }

    /**
     * Fetches the token secret corresponding to the temporary token,
     * then uses the obtained token secret along with the original temporary token and verifier to obtain an access token from the server.
     *
     * @param temporaryToken The temporary token obtained from {@link OAuth1WithCallback#begin()}
     * @param verifier       The verifier.
     * @return Access token from the server, and an {@link OAuthParameters} object used to make OAuth-backed requests on behalf of the access token via {@link OAuth1WithCallback#makeOAuthBackedRequest(String, OAuthParameters, RequestMethod, HttpContent)}
     * @throws GetAccessTokenException If the request to the server for the access token failed.
     * @throws TokenMapException       If fetching the token secret corresponding to the given temporary token failed.
     */
    @Override
    @Nonnull
    public AuthorisedResult authorised(@Nonnull String temporaryToken, @Nonnull String verifier) throws GetAccessTokenException, TokenMapException {
        return super.authorised(temporaryToken, verifier);
    }
}
