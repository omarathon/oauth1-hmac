package example;

import com.google.api.client.auth.oauth.OAuthParameters;
import com.google.api.client.http.HttpResponse;
import oauth1.OAuth1NoCallback;
import oauth1.exception.GetAccessTokenException;
import oauth1.exception.GetTemporaryTokenException;
import oauth1.exception.OAuthBackedRequestException;
import oauth1.exception.TokenMapException;
import oauth1.lib.AuthorisedResult;
import oauth1.lib.BeginResult;
import oauth1.lib.RequestMethod;
import oauth1.strategy.preset.HashMapTokenMapStrategy;

import java.io.IOException;

public class Example {
    /**
     * Construct the OAuth1NoCallback instance. Usually, this would be done via injection, or a configuration bean in Spring.
     * Note: we use the preset HashMapTokenMapStrategy as the temporary token to token secret mapping strategy.
     *       It's recommended to use a custom implementation using a database.
     */
    public static final OAuth1NoCallback O_AUTH_1_NO_CALLBACK = new OAuth1NoCallback(
            "<CONSUMER_KEY>",
            "<CONSUMER_SECRET>",
            "<REQUEST_TEMPORARY_TOKEN_URL>",
            "<AUTHORISE_TEMPORARY_TOKEN_URL>",
            "<ACCESS_TOKEN_URL>",
            new HashMapTokenMapStrategy()
    );

    /**
     * This application:
     * 1. Obtains and displays a temporary token and redirect URL from the service provider.
     * 2. Waits for the user to verify the temporary token by polling until a key-press occurs.
     * 3. After a key-press occurs, obtains and displays an access token from the service provider using their temporary token.
     * 4. Makes a request on the behalf of the user to the resource server using their access token, and displays the result.
     *
     * The application assumes the entire process went smoothly, by throwing all checked exceptions.
     * In practise, the checked exceptions would be caught and handled appropriately.
     */
    public static void main(String[] args) throws IOException, OAuthBackedRequestException, GetAccessTokenException, GetTemporaryTokenException, TokenMapException {
        // Obtain a temporary token and redirect URL.
        BeginResult beginResult = begin();
        String temporaryToken = beginResult.getTemporaryToken();
        String redirectUrl = beginResult.getRedirectUrl();
        // Display the temporary token and redirect URL.
        System.out.println("Temporary Token: " + temporaryToken + ", Redirect URL: " + redirectUrl);

        /*
         Now, the user shall go to the redirect URL and verify the temporary token.
         After the user has verified the temporary token, press a key to obtain an access token and make a request on their behalf.
         */

        // Wait for key-press.
        System.out.println("Press a key once the temporary token has been verified by the user...");
        System.in.read();

        // Key-press occurred, so obtain an access token and OAuthParameters using their the temporary token.
        AuthorisedResult authorisedResult = authorised(beginResult.getTemporaryToken());
        String accessToken = authorisedResult.getAccessToken();
        OAuthParameters oAuthParameters = authorisedResult.getOAuthParameters();
        // Display their access token.
        System.out.println("Access Token: " + accessToken);

        /*
         Make a request on the user's behalf via the OAuthParameters returned with their access token.
         Note: the access token is all that is needed to make requests on their behalf,
               but the library provides out-of-the-box support for such requests using the OAuthParameters object.
               Here, we make a POST request with an empty request body.
         */
        HttpResponse response = OAuth1NoCallback.makeOAuthBackedRequest("<RESOURCE_SERVER_REQUEST_URL>", oAuthParameters, RequestMethod.POST, null);
        // Display the response in string format.
        System.out.println(response.parseAsString());
    }

    /**
     * The begin endpoint,
     * i.e. requesting a temporary token to obtain a redirect URL where the user shall go to verify the token.
     *
     * This endpoint assumes the entire process went smoothly, by throwing all checked exceptions.
     * In practise, the checked exceptions would be caught and handled appropriately.
     * If either a GetTemporaryTokenException or TokenMapException occurred, it can be assumed that a temporary token was unsuccessfully obtained.
     */
    public static BeginResult begin() throws GetTemporaryTokenException, TokenMapException {
        BeginResult beginResult = O_AUTH_1_NO_CALLBACK.begin();
        // Do something with the result, e.g. log the result.
        // For the sakes of this demo, we simply return the result.
        return beginResult;
    }

    /**
     * The authorised endpoint,
     * i.e. requesting an access token corresponding to a temporary token which was verified by the user.
     *
     * This endpoint assumes the entire process went smoothly, by throwing all checked exceptions.
     * In practise, the checked exceptions would be caught and handled appropriately.
     * If either a GetAccessTokenException or TokenMapException occurred, it can be assumed that an access token was unsuccessfully obtained.
     */
    public static AuthorisedResult authorised(String temporaryToken) throws GetAccessTokenException, TokenMapException {
        AuthorisedResult authorisedResult = O_AUTH_1_NO_CALLBACK.authorised(temporaryToken);
        // Do something with the result, e.g. store the user's access token, log the result, or make a request on their behalf.
        // For the sakes of this demo, we simply return the result.
        return authorisedResult;
    }
}
