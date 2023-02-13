package main.java.example;

import burp.api.montoya.BurpExtension;
import burp.api.montoya.MontoyaApi;
import burp.api.montoya.logging.Logging;
import main.java.example.httphandler.MyHttpHandler;

public class PracticeBurpExtension implements BurpExtension {
    @Override
    public void initialize(MontoyaApi api) {
        api.extension().setName("Practice Burp Extension");
        Logging logging = api.logging();

        // https://github.com/PortSwigger/burp-extensions-montoya-api-examples/tree/main/helloworld
        helloWorld(logging);

        // https://github.com/PortSwigger/burp-extensions-montoya-api-examples/tree/main/httphandler
        initializeHttpHandlers(api);
    }

    void initializeHttpHandlers(MontoyaApi api){
        //register out http handler with burp.
        api.http().registerHttpHandler(new MyHttpHandler(api));

        // write a message to our output stream
        api.logging().logToOutput("http handler registered");
    }

    void helloWorld(Logging logging){

        // write a message to our output stream
        logging.logToOutput("Hello output.");

        // write a message to our error stream
        logging.logToError("Hello error.");

        //write a message to the Burp alerts tab
        logging.raiseInfoEvent("Hello info event");
        logging.raiseDebugEvent("Hello debug event");
        logging.raiseErrorEvent("Hello error event");
        logging.raiseCriticalEvent("Hello critical event");

        // throw an exception that will appear in our error stream (this stops the extension from continuing)
        // throw new RuntimeException("Hello exception");
    }
}