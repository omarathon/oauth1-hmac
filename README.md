# OAuth1-HMAC

OAuth1-HMAC is a simple library implementing the [OAuth1.0 protocol](https://en.wikipedia.org/wiki/OAuth) using the [HMAC signing strategy](https://en.wikipedia.org/wiki/HMAC).

This library wraps [Google's API Client Library for Java](https://developers.google.com/api-client-library/java) (`google-api-client`) which [implements the underlying OAuth1.0 functionality](https://developers.google.com/api-client-library/java/google-oauth-java-client) ([JavaDoc](https://googleapis.dev/java/google-oauth-client/1.25.0/com/google/api/client/auth/oauth/package-summary.html)).

Please note that the OAuth1.0 implementation in `google-api-client` is in Beta, and is thus subject to change. Its implementation has been tested and verified working for version `1.30.10`. The version of the `google-api-client` dependency shall only be bumped if the OAuth1.0 implementation remains unchanged.
 
 **If your project uses a conflicting version of `google-api-client`, then usage of this library is highly unrecommended.**