package main.java.example.httphandler;

import burp.api.montoya.BurpExtension;
import burp.api.montoya.MontoyaApi;
import burp.api.montoya.logging.Logging;

public class HttpHandlerExample implements BurpExtension {
    @Override
    public void initialize(MontoyaApi api) {
        api.extension().setName("HTTP Handler Example");

        Logging logger = api.logging();

        //register out http handler with burp.
        api.http().registerHttpHandler(new MyHttpHandler(api));

        // write a message to our output stream
        logger.logToOutput("http handler registered");
    }
}
