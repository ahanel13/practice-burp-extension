package main.java.example.httphandler;

import burp.api.montoya.MontoyaApi;
import burp.api.montoya.core.Annotations;
import burp.api.montoya.core.HighlightColor;
import burp.api.montoya.http.handler.*;
import burp.api.montoya.http.message.requests.HttpRequest;
import burp.api.montoya.logging.Logging;

import static burp.api.montoya.http.handler.RequestToBeSentAction.continueWith;
import static burp.api.montoya.http.handler.ResponseReceivedAction.continueWith;
import static burp.api.montoya.http.message.params.HttpParameter.urlParameter;


public class MyHttpHandler implements HttpHandler {
    private Logging logging;

    public MyHttpHandler(MontoyaApi api) {
        this.logging = api.logging();
    }

    @Override
    public RequestToBeSentAction handleHttpRequestToBeSent(HttpRequestToBeSent requestToBeSent) {
        Annotations annotations = requestToBeSent.annotations();

        // If the request is a POST, log the body and add notes.
        if (isPostRequest(requestToBeSent)){
            annotations = annotations.withNotes("Request was a POST");
            logging.logToOutput(requestToBeSent.bodyToString());
        }

        // Modify the request by adding url param.
        HttpRequest modifiedRequest = requestToBeSent.withAddedParameters(urlParameter("foo","bar"));

        // Return the modified request to burp with updated annotations.
        return continueWith(modifiedRequest, annotations);
    }

    @Override
    public ResponseReceivedAction handleHttpResponseReceived(HttpResponseReceived responseReceived) {
        Annotations annotations = responseReceived.annotations();

        // Highlight all responses where the request had a Content-Length header
        if (requestHadContentLengthHeader(responseReceived)){
            annotations = annotations.withHighlightColor(HighlightColor.BLUE);
        }

        return continueWith(responseReceived, annotations);
    }

    private boolean isPostRequest(HttpRequest httpRequest) {
        return httpRequest.method().equalsIgnoreCase("POST");
    }


    private boolean requestHadContentLengthHeader(HttpResponseReceived responseReceived) {
        return responseReceived.initiatingRequest().headers().stream().anyMatch(httpHeader -> httpHeader.name().equalsIgnoreCase("Content-Length"));
    }
}
