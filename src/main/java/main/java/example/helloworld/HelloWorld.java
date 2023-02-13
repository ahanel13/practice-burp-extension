package main.java.example.helloworld;

import burp.api.montoya.BurpExtension;
import burp.api.montoya.MontoyaApi;
import burp.api.montoya.logging.Logging;

public class HelloWorld implements BurpExtension {
    @Override
    public void initialize(MontoyaApi montoyaApi) {
        // set extension name
        montoyaApi.extension().setName("Hello world extension");

        Logging logger = montoyaApi.logging();

        // write a message to our output stream
        logger.logToOutput("Hello output.");

        // write a message to our error stream
        logger.logToError("Hello error.");

        //write a message to the Burp alerts tab
        logger.raiseInfoEvent("Hello info event");
        logger.raiseDebugEvent("Hello debug event");
        logger.raiseErrorEvent("Hello error event");
        logger.raiseCriticalEvent("Hello critical event");

        // throw an exception that will appear in our error stream
        throw new RuntimeException("Hello exception");
    }
}
