package oauth1.lib;

import com.google.api.client.auth.oauth.OAuthParameters;
import lombok.Data;

import javax.annotation.Nonnull;

@Data
public class AuthorisedResult {
    private @Nonnull String accessToken;
    private @Nonnull OAuthParameters oAuthParameters;

    public AuthorisedResult(@Nonnull String accessToken, @Nonnull OAuthParameters oAuthParameters) {
        this.accessToken = accessToken;
        this.oAuthParameters = oAuthParameters;
    }
}
