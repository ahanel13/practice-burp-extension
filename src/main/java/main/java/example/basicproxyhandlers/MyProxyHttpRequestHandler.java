package main.java.example.basicproxyhandlers;

import burp.api.montoya.proxy.http.InterceptedRequest;
import burp.api.montoya.proxy.http.ProxyRequestHandler;
import burp.api.montoya.proxy.http.ProxyRequestReceivedAction;
import burp.api.montoya.proxy.http.ProxyRequestToBeSentAction;

import static burp.api.montoya.core.HighlightColor.RED;
import static burp.api.montoya.http.message.ContentType.JSON;

public class MyProxyHttpRequestHandler implements ProxyRequestHandler {
    @Override
    public ProxyRequestReceivedAction handleRequestReceived(InterceptedRequest interceptedRequest) {
        // DROP requests with "analytics" in the url
        if (interceptedRequest.url().contains("analytics")){
            return ProxyRequestReceivedAction.drop();
        }

        // Intercept requests with "user" in the body
        if (interceptedRequest.body().toString().contains("user")){
            return ProxyRequestReceivedAction.intercept(interceptedRequest);
        }

        //Intercept any request with foo in the url
        if (interceptedRequest.url().contains("foo")) {
            return ProxyRequestReceivedAction.intercept(interceptedRequest);
        }

        //If the content type is json, highlight the request and follow burp rules for interception
        if (interceptedRequest.contentType() == JSON) {
            return ProxyRequestReceivedAction.continueWith(interceptedRequest, interceptedRequest.annotations().withHighlightColor(RED));
        }

        // Do not intercept any other requests
        return ProxyRequestReceivedAction.continueWith(interceptedRequest);
    }

    @Override
    public ProxyRequestToBeSentAction handleRequestToBeSent(InterceptedRequest interceptedRequest) {
        // Don't do anything with requests that have been modified by the user, continue as normal
        return ProxyRequestToBeSentAction.continueWith(interceptedRequest);
    }
}
