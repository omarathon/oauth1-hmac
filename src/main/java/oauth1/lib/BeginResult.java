package oauth1.lib;


import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BeginResult {
    private @Nonnull String temporaryToken;
    private @Nullable String redirectUrl;

    public BeginResult(@Nonnull String temporaryToken, @Nullable String redirectUrl) {
        this.temporaryToken = temporaryToken;
        this.redirectUrl = redirectUrl;
    }

    @Nonnull
    public String getTemporaryToken() {
        return temporaryToken;
    }

    @Nullable
    public String getRedirectUrl() {
        return redirectUrl;
    }
}
