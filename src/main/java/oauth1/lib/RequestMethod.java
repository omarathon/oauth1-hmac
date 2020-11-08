package oauth1.lib;

import javax.annotation.Nonnull;

public enum RequestMethod {
    GET,
    POST,
    DELETE,
    PUT,
    PATCH,
    HEAD;

    // Get representation for com.google.api.client.http.HttpRequestFactory::buildRequest `requestMethod` parameter.
    @Nonnull
    public String getApiRepresentation() {
        // Turns out this works for Google API Client Library's current implementation.
        return toString();
    }
}
