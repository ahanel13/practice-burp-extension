package main.java.example.eventlistener;

import burp.api.montoya.http.handler.*;

public class EventListenerHttpHandler implements HttpHandler {
    @Override
    public RequestToBeSentAction handleHttpRequestToBeSent(HttpRequestToBeSent httpRequestToBeSent) {
        return null;
    }

    @Override
    public ResponseReceivedAction handleHttpResponseReceived(HttpResponseReceived httpResponseReceived) {
        return null;
    }
}
