package oauth1.lib;


import lombok.Data;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@Data
public class BeginResult {
    private @Nonnull String temporaryToken;
    private @Nullable String redirectUrl;

    public BeginResult(@Nonnull String temporaryToken, @Nullable String redirectUrl) {
        this.temporaryToken = temporaryToken;
        this.redirectUrl = redirectUrl;
    }
}
