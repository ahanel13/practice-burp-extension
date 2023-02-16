package main.java.example.loggerinterface;

import burp.api.montoya.http.handler.*;

public class UiHttpHandler implements HttpHandler {
    private final TableModel tableModel;

    public UiHttpHandler(TableModel tableModel) {
        this.tableModel = tableModel;
    }

    @Override
    public RequestToBeSentAction handleHttpRequestToBeSent(HttpRequestToBeSent httpRequestToBeSent) {
        return RequestToBeSentAction.continueWith(httpRequestToBeSent);
    }

    @Override
    public ResponseReceivedAction handleHttpResponseReceived(HttpResponseReceived httpResponseReceived) {
        tableModel.add(httpResponseReceived);
        return ResponseReceivedAction.continueWith(httpResponseReceived);
    }
}
