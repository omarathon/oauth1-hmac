package oauth1.lib;

import com.google.api.client.auth.oauth.OAuthParameters;

import javax.annotation.Nonnull;

public class AuthorisedResult {
    private @Nonnull String accessToken;
    private @Nonnull OAuthParameters oAuthParameters;

    public AuthorisedResult(@Nonnull String accessToken, @Nonnull OAuthParameters oAuthParameters) {
        this.accessToken = accessToken;
        this.oAuthParameters = oAuthParameters;
    }

    @Nonnull
    public String getAccessToken() {
        return accessToken;
    }

    @Nonnull
    public OAuthParameters getoAuthParameters() {
        return oAuthParameters;
    }
}
