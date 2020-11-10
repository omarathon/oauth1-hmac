# OAuth1-HMAC

OAuth1-HMAC is a simple library implementing the [OAuth1.0 protocol](https://en.wikipedia.org/wiki/OAuth) as a consumer, using the [HMAC signing strategy](https://en.wikipedia.org/wiki/HMAC).

This library wraps the [Google OAuth Client Library for Java](https://developers.google.com/api-client-library/java/google-oauth-java-client) (`google-oauth-client` | [GitHub](https://github.com/googleapis/google-oauth-java-client)) which implements the underlying OAuth1.0 functionality ([JavaDoc](https://googleapis.dev/java/google-oauth-client/1.25.0/com/google/api/client/auth/oauth/package-summary.html)).

Please note that the OAuth1.0 implementation in `google-oauth-client` is in Beta, and is thus subject to change. Its implementation has been tested and verified working for version `1.31.0`. For higher versions, it exhibits buggy behaviour, thus the version of the `google-oauth-client` dependency shall only be bumped if the OAuth1.0 implementation remains unchanged and works.
 
**If your project uses a conflicting version of `google-oauth-client`** (such a dependency may come transitively from `google-api-client`) then **usage of this library is highly unrecommended**.
 
## Usage

### Primary API

#### Main Instance

Usage of this library involves constructing either an [OAuth1WithCallback](src/main/java/oauth1/OAuth1WithCallback.java) ([JavaDoc](https://omarathon.github.io/oauth1-hmac/oauth1/OAuth1WithCallback.html)) instance, or an [OAuth1NoCallback](src/main/java/oauth1/OAuth1NoCallback.java) ([JavaDoc](https://omarathon.github.io/oauth1-hmac/oauth1/OAuth1NoCallback.html)) instance, depending on whether you're using a callback or not, respectively (a callback is the URL the user is redirected to after verifying their temporary token).

If using Spring, it's recommended to construct such an instance as a Configuration bean, and inject it via autowiring.

Both OAuth1WithCallback and OAuth1NoCallback require the following parameters to its constructor:
* `consumerKey::String` - Required identifier portion of the client credentials (equivalent to a username). More information: client_id entry of the OAuthParameters table [here](https://www.iana.org/assignments/oauth-parameters/oauth-parameters.xhtml)
* `consumerSecret::String` -  Client-shared secret. More information: client_id entry of the OAuthParameters table [here](https://www.iana.org/assignments/oauth-parameters/oauth-parameters.xhtml)
* `requestTokenUrl::String` -  Encoded authorization server URL. More information: request_uri entry of the OAuthParameters table [here](https://www.iana.org/assignments/oauth-parameters/oauth-parameters.xhtml)
* `authoriseUrl::String` -  Encoded user authorization URL the user visits to verify their temporary token, or null if only wish to receive the temporary token and build it yourself
* `accessTokenUrl::String` -  Encoded authorization server URL, for requesting an access token from the service provider
* `tokenMapStrategy::TokenMapStrategy` -  Strategy to map temporary tokens to token secrets, and fetch the token secret for a temporary token

and OAuth1WithCallback requires an additional parameter:
* `callbackUrl::String` -  Absolute URI back to which the server will redirect the resource owner when the Resource Owner Authorization step has completed. More information: redirect_uri entry of the OAuthParameters table [here](https://www.iana.org/assignments/oauth-parameters/oauth-parameters.xhtml)

##### Example Instance

Using the [University of Warwick's OAuth provider](https://warwick.ac.uk/services/its/servicessupport/web/sign-on/help/oauth/apis/) as an example, one may construct an OAuth1WithCallback instance with the following values:
* `consumerKey::String` - `"<YOUR_CONSUMER_KEY_FROM_WARWICK>"`
* `consumerSecret::String` - `"<YOUR_CONSUMER_SECRET_FROM_WARWICK>"`
* `requestTokenUrl::String` - `"https://websignon.warwick.ac.uk/oauth/requestToken"`
* `callbackUrl::String` - `<YOUR_WEBSITE_LOGIN_URL>`
* `authoriseUrl::String` - `"https://websignon.warwick.ac.uk/oauth/authorise"`
* `accessTokenUrl::String` - `"https://websignon.warwick.ac.uk/oauth/accessToken"`
* `tokenMapStrategy::TokenMapStrategy` - `new HashMapTokenMapStrategy()`

#### TokenMapStrategy

On constructing either OAuth1WithCallback or OAuth1NoCallback, an instance of [TokenMapStrategy](src/main/java/oauth1/strategy/TokenMapStrategy.java) ([JavaDoc](https://omarathon.github.io/oauth1-hmac/oauth1/strategy/TokenMapStrategy.html)) must be provided:

```java
void mapTemporaryTokenToTemporaryTokenSecret(@Nonnull String temporaryToken, @Nonnull String temporaryTokenSecret) throws TokenMapException
    
@Nullable
String fetchTemporaryTokenSecretForTemporaryToken(@Nonnull String temporaryToken) throws TokenMapException
```

where:
 * `mapTemporaryTokenToTemporaryTokenSecret` maps the given `temporaryToken` to the given `temporaryTokenSecret` persistently for at least the longest permissible duration of an OAuth1.0 handshake
 * `fetchTemporaryTokenSecretForTemporaryToken` retrieves the value of `temporaryTokenSecret` for the given `temporaryToken` from the map, and returns null if it does not exist

OAuth1-HMAC provides a preset implementation [HashMapTokenMapStrategy](src/main/java/oauth1/strategy/preset/HashMapTokenMapStrategy.java) ([JavaDoc](https://omarathon.github.io/oauth1-hmac/oauth1/strategy/preset/HashMapTokenMapStrategy.html)), which uses an in-memory static `HashMap` to persist the temporary token to token secret mapping. This could be used for prototyping / non-production purposes, but in production it's recommended to use a database.

#### Main Instance API

Both OAuth1WithCallback and OAuth1NoCallback provide the following instance methods:

* `begin` - Requests a temporary token and corresponding token secret from the service provider, then maps the temporary token to the token secret via the TokenMapStrategy, and returns the obtained temporary token along with the authenticate URL (redirect URL) if `authoriseUrl` is non-null
* `authorised` - Fetches the token secret corresponding to the temporary token via the TokenMapStrategy, then uses the obtained token secret along with the original temporary token (and verifier if using a callback, which is parsed on the front-end as a parameter to the callback URL) to obtain an access token from the service provider

and they both provide the following static method, which is used to make requests to the resource server on the user's behalf after a successful OAuth1.0 handshake:

* `makeOAuthBackedRequest` - Makes a request to the given URL wrapped with a user's OAuth credentials (which includes their access token) obtained from `authorised`


## Example
 
One may find an example application (which does not use a callback URL) in [Example.java](src/test/java/example/Example.java).

To use a callback URL, use an instance of [OAuth1WithCallback](src/main/java/oauth1/OAuth1WithCallback.java) instead of [OAuth1NoCallback](src/main/java/oauth1/OAuth1NoCallback.java).
 
This simple example:
1. Obtains and displays a temporary token and redirect URL from the service provider.
1. Waits for the user to verify the temporary token by polling until a key-press occurs.
1. After a key-press occurs, obtains and displays an access token from the service provider using their temporary token.
1. Makes a request on the behalf of the user to the resource server using their access token, and displays the result.

For an understanding of the terms used in this example, please refer to the following diagram of the OAuth1.0 flow:
![OAuth1.0 Flow](https://support.smartbear.com/readyapi/docs/_images/requests/auth/types/oauth1/about-flow.png)
 
## Documentation
 
The latest version of OAuth1-HMAC has a JavaDoc [here](https://omarathon.github.io/oauth1-hmac/).

## Support

Please contact Omar Tanner via Discord (omarathon#2226) if you require assistance with this library.
