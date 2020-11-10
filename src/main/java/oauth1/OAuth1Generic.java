package oauth1;

import com.google.api.client.auth.oauth.*;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpContent;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.javanet.NetHttpTransport;
import lombok.Data;
import lombok.Getter;
import oauth1.exception.*;
import oauth1.exception.missingdata.*;
import oauth1.lib.AuthorisedResult;
import oauth1.lib.BeginResult;
import oauth1.lib.RequestMethod;
import oauth1.strategy.TokenMapStrategy;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;

/**
 * An OAuth1.0 consumer class, using the HMAC signing strategy.
 * <br><br>
 * This is a generic consumer supporting both a callback and not a callback, and is not to be used directly.
 * <br>
 * Please use {@link OAuth1NoCallback} for an OAuth1.0 consumer not using a callback, and {@link OAuth1WithCallback} for an OAuth1.0 consumer using a callback.
 * <br><br>
 * Usage:
 * <br>
 * 1. Call {@link OAuth1Generic#begin()} to obtain a temporary token and a redirect URL if {@link OAuth1Generic#authoriseUrl} was provided (not null).
 * <br>
 * 2. On the front-end, redirect the user to the redirect URL obtained in (1), or compute the redirect URL from the temporary token obtained in (1).
 * <br>
 * 3. Once the user has granted access on the redirect URL (usually detected via the callback), call {@link OAuth1Generic#authorised(String, String)} with the temporary token obtained in (1), and the verifier (provided as a parameter to the callback, as parsed on the front-end), or null if no callback was used.
 * <br>
 * 4. If (3) succeeded, you now have an access token and an {@link OAuthParameters} object that can be used to make requests on the user's behalf. Using the {@link OAuthParameters} make a request to a given URL with a given HTTP request method and body via {@link OAuth1Generic#makeOAuthBackedRequest(String, OAuthParameters, RequestMethod, HttpContent)}.
 * @author Omar Tanner
 */
@Data
public class OAuth1Generic {
    protected @Nonnull String consumerKey;
    protected @Nonnull String consumerSecret;
    protected @Nonnull String requestTokenUrl;
    protected @Nullable String callbackUrl;
    protected @Nullable String authoriseUrl;
    protected @Nonnull String accessTokenUrl;
    protected @Nonnull TokenMapStrategy tokenMapStrategy;

    private OAuth1Generic() {

    }

    /**
     * @param consumerKey Required identifier portion of the client credentials (equivalent to a username). More information: client_id entry of the OAuthParameters table at https://www.iana.org/assignments/oauth-parameters/oauth-parameters.xhtml
     * @param consumerSecret Client-shared secret. More information: client_id entry of the OAuthParameters table at https://www.iana.org/assignments/oauth-parameters/oauth-parameters.xhtml
     * @param requestTokenUrl Encoded authorization server URL. More information: request_uri entry of the OAuthParameters table at https://www.iana.org/assignments/oauth-parameters/oauth-parameters.xhtml
     * @param callbackUrl Optional absolute URI back to which the server will redirect the resource owner when the Resource Owner Authorization step is completed or null for none. More information: redirect_uri entry of the OAuthParameters table at https://www.iana.org/assignments/oauth-parameters/oauth-parameters.xhtml
     * @param authoriseUrl Encoded user authorization URL, or null if only wish to receive the temporary token and build it themselves.
     * @param accessTokenUrl Encoded authorization server URL.
     * @param tokenMapStrategy Strategy to map temporary tokens to token secrets, and fetch the token secret for a temporary token.
     */
    protected OAuth1Generic(
            @Nonnull String consumerKey,
            @Nonnull String consumerSecret,
            @Nonnull String requestTokenUrl,
            @Nullable String callbackUrl,
            @Nullable String authoriseUrl,
            @Nonnull String accessTokenUrl,
            @Nonnull TokenMapStrategy tokenMapStrategy
    ) {
        this.consumerKey = consumerKey;
        this.consumerSecret = consumerSecret;
        this.requestTokenUrl = requestTokenUrl;
        this.callbackUrl = callbackUrl;
        this.authoriseUrl = authoriseUrl;
        this.accessTokenUrl = accessTokenUrl;
        this.tokenMapStrategy = tokenMapStrategy;
    }

    /**
     * Requests a temporary token and corresponding token secret from the server,
     * then maps the temporary token to the token secret
     * and returns the obtained temporary token along with the authenticate URL (redirect URL) if {@link OAuth1Generic#authoriseUrl} is non-null.
     *
     * @return Temporary token from the server and the authenticate URL (redirect URL) if if {@link OAuth1Generic#authoriseUrl} is non-null, otherwise null.
     * @throws GetTemporaryTokenException If the request to the server for the temporary token failed.
     * @throws TokenMapException          If mapping the obtained temporary token to the obtained token secret failed.
     */
    @Nonnull
    protected BeginResult begin() throws GetTemporaryTokenException, TokenMapException {
        // Build get temporary token request.
        OAuthHmacSigner signer = new OAuthHmacSigner();
        OAuthGetTemporaryToken getTemporaryToken = new OAuthGetTemporaryToken(requestTokenUrl);
        getTemporaryToken.callback = callbackUrl;
        signer.clientSharedSecret = consumerSecret;
        getTemporaryToken.consumerKey = consumerKey;
        getTemporaryToken.signer = signer;
        getTemporaryToken.transport = new NetHttpTransport();

        // Execute get temporary token request.
        OAuthCredentialsResponse temporaryTokenResponse;
        try {
            temporaryTokenResponse = getTemporaryToken.execute();
        } catch (IOException e) {
            throw new GetTemporaryTokenException(e);
        }
        if (temporaryTokenResponse == null || temporaryTokenResponse.token == null || temporaryTokenResponse.tokenSecret == null) {
            throw new MissingDataGetTemporaryTokenException();
        }

        // Map obtained temporary token to obtained token secret.
        String temporaryToken = temporaryTokenResponse.token;
        String tokenSecret = temporaryTokenResponse.tokenSecret;
        tokenMapStrategy.mapTemporaryTokenToTemporaryTokenSecret(temporaryToken, tokenSecret);

        // If authoriseURL is null then return the temporary token with an empty Authenticate URL.
        if (authoriseUrl == null) {
            return new BeginResult(temporaryToken, null);
        }

        // Build Authenticate URL, and return it with the temporary token.
        OAuthAuthorizeTemporaryTokenUrl authorizeTemporaryTokenUrl = new OAuthAuthorizeTemporaryTokenUrl(authoriseUrl);
        authorizeTemporaryTokenUrl.temporaryToken = temporaryToken;
        return new BeginResult(temporaryToken, authorizeTemporaryTokenUrl.build());
    }

    /**
     * Fetches the token secret corresponding to the temporary token,
     * then uses the obtained token secret along with the original temporary token and verifier to obtain an access token from the server.
     *
     * @param temporaryToken The temporary token obtained from {@link OAuth1Generic#begin()}
     * @param verifier       The verifier, if a callback is used, otherwise null.
     * @return Access token from the server, and an {@link OAuthParameters} object used to make OAuth-backed requests on behalf of the access token via {@link OAuth1Generic#makeOAuthBackedRequest(String, OAuthParameters, RequestMethod, HttpContent)}
     * @throws GetAccessTokenException If the request to the server for the access token failed.
     * @throws TokenMapException       If fetching the token secret corresponding to the given temporary token failed.
     */
    @Nonnull
    protected AuthorisedResult authorised(@Nonnull String temporaryToken, @Nullable String verifier) throws GetAccessTokenException, TokenMapException {
        // Fetch the temporary token secret for the given temporary token using the given strategy.
        String temporaryTokenSecret = tokenMapStrategy.fetchTemporaryTokenSecretForTemporaryToken(temporaryToken);
        if (temporaryTokenSecret == null) {
            throw new MissingDataTokenMapException();
        }

        // Build the get access token request.
        OAuthHmacSigner signer = new OAuthHmacSigner();
        signer.clientSharedSecret = consumerSecret;
        signer.tokenSharedSecret = temporaryTokenSecret;
        OAuthGetAccessToken getAccessToken = new OAuthGetAccessToken(accessTokenUrl);
        getAccessToken.signer = signer;
        getAccessToken.temporaryToken = temporaryToken;
        getAccessToken.transport = new NetHttpTransport();
        getAccessToken.consumerKey = consumerKey;
        getAccessToken.verifier = verifier;

        // Execute the get access token request.
        OAuthCredentialsResponse oAuthCredentialsResponse;
        try {
            oAuthCredentialsResponse = getAccessToken.execute();
        } catch (IOException e) {
            throw new GetAccessTokenException(e);
        }
        if (oAuthCredentialsResponse == null || oAuthCredentialsResponse.token == null) {
            throw new MissingDataGetAccessTokenException();
        }

        // Obtain the access token.
        String accessToken = oAuthCredentialsResponse.token;

        // Return the obtained access token, and an OAuthParameters object of the access token.
        return new AuthorisedResult(accessToken, buildOAuthParameters(accessToken, temporaryTokenSecret));
    }

    /**
     * Makes a request to the given URL wrapped with a user's OAuth credentials (which includes their access token) obtained from {@link OAuth1Generic#authorised(String, String)},
     * to make a request on a user's behalf.
     *
     * @param url The URL to make the request to.
     * @param oAuthParameters The OAuth credentials corresponding to a user (which includes their access token) obtained from {@link OAuth1Generic#authorised(String, String)}
     * @param requestMethod The HTTP request method (GET, PUT, etc.)
     * @param httpContent The content of the HTTP request body, if applicable (e.g. in a PUT request), otherwise null.
     *
     * @return The response from the server after making the request to the url on the behalf of the user. Note: use {@link HttpResponse#parseAsString()} or {@link HttpResponse#parseAs(Class)} to deserialize the response body.
     *
     * @throws OAuthBackedRequestException If building the request, or executing the request to the server, failed.
     */
    @Nonnull
    public static HttpResponse makeOAuthBackedRequest(@Nonnull String url, @Nonnull OAuthParameters oAuthParameters, @Nonnull RequestMethod requestMethod, @Nullable HttpContent httpContent) throws OAuthBackedRequestException {
        GenericUrl genericUrl = new GenericUrl(url);
        HttpRequestFactory requestFactory = new NetHttpTransport().createRequestFactory(oAuthParameters);
        HttpResponse httpResponse;
        try {
            httpResponse = requestFactory.buildRequest(requestMethod.getApiRepresentation(), genericUrl, httpContent).execute();
        }
        catch (IOException e) {
            throw new OAuthBackedRequestException(e);
        }
        if (httpResponse == null) {
            throw new MissingDataOAuthBackedRequestException();
        }
        return httpResponse;
    }

    @Nonnull
    private OAuthParameters buildOAuthParameters(@Nonnull String accessToken, @Nonnull String tokenSecret) {
        OAuthHmacSigner signer = new OAuthHmacSigner();
        signer.clientSharedSecret = consumerSecret;
        signer.tokenSharedSecret = tokenSecret;
        OAuthParameters oauthParameters = new OAuthParameters();
        signer.tokenSharedSecret = tokenSecret;
        oauthParameters.signer = signer;
        oauthParameters.consumerKey = consumerKey;
        oauthParameters.token = accessToken;
        oauthParameters.signatureMethod = "HMAC-SHA1";
        oauthParameters.version = "1.0";
        return oauthParameters;
    }
}
