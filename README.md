# OAuth1-HMAC

OAuth1-HMAC is a simple library implementing the [OAuth1.0 protocol](https://en.wikipedia.org/wiki/OAuth) as a consumer, using the [HMAC signing strategy](https://en.wikipedia.org/wiki/HMAC).

This library wraps the [Google OAuth Client Library for Java](https://developers.google.com/api-client-library/java/google-oauth-java-client) (`google-oauth-client` | [GitHub](https://github.com/googleapis/google-oauth-java-client)) which implements the underlying OAuth1.0 functionality ([JavaDoc](https://googleapis.dev/java/google-oauth-client/1.25.0/com/google/api/client/auth/oauth/package-summary.html)).

Please note that the OAuth1.0 implementation in `google-oauth-client` is in Beta, and is thus subject to change. Its implementation has been tested and verified working for version `1.31.0`. For higher versions, it exhibits buggy behaviour, thus the version of the `google-oauth-client` dependency shall only be bumped if the OAuth1.0 implementation remains unchanged and works.
 
**If your project uses a conflicting version of `google-oauth-client`** (such a dependency may come transitively from `google-oauth1-client`) then **usage of this library is highly unrecommended**.
 
## Usage

### Primary API


### Example
 
One may find an example application (which does not use a callback URL) in [Example.java](src/test/java/example/Example.java).
 
This simple example:
1. Obtains and displays a temporary token and redirect URL from the service provider.
1. Waits for the user to verify the temporary token by polling until a key-press occurs.
1. After a key-press occurs, obtains and displays an access token from the service provider using their temporary token.
1. Makes a request on the behalf of the user to the resource server using their access token, and displays the result.

For an understanding of the terms used in this example, please refer to the following diagram of the OAuth1.0 flow:
![OAuth1.0 Flow](https://support.smartbear.com/readyapi/docs/_images/requests/auth/types/oauth1/about-flow.png)

To use a callback URL, use an instance of [OAuth1WithCallback](src/main/java/oauth1/OAuth1WithCallback.java) instead of [OAuth1NoCallback](src/main/java/oauth1/OAuth1NoCallback.java).
 
## Documentation
 
The latest version of OAuth1-HMAC has a JavaDoc [here](https://omarathon.github.io/oauth1-hmac/).
