import com.google.api.client.auth.oauth.*;
import com.google.api.client.http.javanet.NetHttpTransport;
import exception.GetTemporaryTokenException;
import exception.TokenMapException;
import strategy.TokenMapStrategy;

import java.io.IOException;

public class OAuth1 {
    private String consumerKey;
    private String consumerSecret;
    private String requestTokenUrl;
    private String callbackUrl;
    private String authoriseUrl;
    private String accessTokenUrl;
    private TokenMapStrategy tokenMapStrategy;

    public OAuth1(String consumerKey, String consumerSecret, String requestTokenUrl, String callbackUrl, String authoriseUrl, String accessTokenUrl, TokenMapStrategy tokenMapStrategy) {
        this.consumerKey = consumerKey;
        this.consumerSecret = consumerSecret;
        this.requestTokenUrl = requestTokenUrl;
        this.callbackUrl = callbackUrl;
        this.authoriseUrl = authoriseUrl;
        this.accessTokenUrl = accessTokenUrl;
        this.tokenMapStrategy = tokenMapStrategy;
    }

    public String getRedirectUrl() throws GetTemporaryTokenException, TokenMapException {
        // Build get temporary token request.
        OAuthHmacSigner signer = new OAuthHmacSigner();
        OAuthGetTemporaryToken getTemporaryToken = new OAuthGetTemporaryToken(requestTokenUrl);
        getTemporaryToken.callback = callbackUrl;
        signer.clientSharedSecret = consumerSecret;
        getTemporaryToken.consumerKey = consumerKey;
        getTemporaryToken.transport = new NetHttpTransport();

        // Execute get temporary token request.
        OAuthCredentialsResponse temporaryTokenResponse;
        try {
            temporaryTokenResponse = getTemporaryToken.execute();
        }
        catch (IOException e) {
            throw new GetTemporaryTokenException(e);
        }

        // Map obtained temporary token to obtained token secret.
        String temporaryToken = temporaryTokenResponse.token;
        String tokenSecret = temporaryTokenResponse.tokenSecret;
        tokenMapStrategy.mapTemporaryTokenToTemporaryTokenSecret(temporaryToken, tokenSecret);

        // Build Authenticate URL and return it.
        OAuthAuthorizeTemporaryTokenUrl authorizeTemporaryTokenUrl = new OAuthAuthorizeTemporaryTokenUrl(authoriseUrl);
        authorizeTemporaryTokenUrl.temporaryToken = temporaryToken;
        return authorizeTemporaryTokenUrl.build();
    }


}
