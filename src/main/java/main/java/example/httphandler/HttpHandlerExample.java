package main.java.example.httphandler;

import burp.api.montoya.BurpExtension;
import burp.api.montoya.MontoyaApi;

public class HttpHandlerExample implements BurpExtension {
    @Override
    public void initialize(MontoyaApi api) {
        api.extension().setName("HTTP Handler Example");

        //register out http handler with burp.
        api.http().registerHttpHandler(new MyHttpHandler(api));
    }
}
