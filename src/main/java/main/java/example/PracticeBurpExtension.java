package main.java.example;

import burp.api.montoya.BurpExtension;
import burp.api.montoya.MontoyaApi;
import burp.api.montoya.extension.Extension;
import burp.api.montoya.extension.ExtensionUnloadingHandler;
import burp.api.montoya.http.Http;
import burp.api.montoya.logging.Logging;
import burp.api.montoya.proxy.Proxy;
import burp.api.montoya.scanner.Scanner;
import burp.api.montoya.scanner.audit.AuditIssueHandler;
import burp.api.montoya.scanner.audit.issues.AuditIssue;
import main.java.example.eventlistener.EventListenerRequestHandler;
import main.java.example.eventlistener.EventListenerHttpHandler;
import main.java.example.eventlistener.EventListenerResponseHandler;
import main.java.example.basichttphandler.MyHttpHandler;
import main.java.example.basicproxyhandlers.MyProxyHttpRequestHandler;
import main.java.example.basicproxyhandlers.MyProxyHttpResponseHandler;
import main.java.example.loggerinterface.CreateTab;
import main.java.example.rdlchecker.userinterface.RDLTab;

public class PracticeBurpExtension implements BurpExtension {
    private Logging logging;
    private Http http;
    private Proxy proxy;
    private Extension extension;
    private Scanner scanner;


    @Override
    public void initialize(MontoyaApi api) {
        api.extension().setName("Practice Burp Extension");
        this.logging = api.logging();
        http = api.http();
        proxy = api.proxy();
        extension = api.extension();
        scanner = api.scanner();

        // https://github.com/PortSwigger/burp-extensions-montoya-api-examples/tree/main/helloworld
        helloWorld(this.logging);

        // https://github.com/PortSwigger/burp-extensions-montoya-api-examples/tree/main/httphandler
        registerHttpHandlers(api);

        // https://github.com/PortSwigger/burp-extensions-montoya-api-examples/tree/main/proxyhandler/src/main/java/example/proxyhandler
        registerBasicProxyHandlers(api);

        // https://github.com/PortSwigger/burp-extensions-montoya-api-examples/tree/main/eventlisteners/src/main/java/example/eventlisteners
        eventListeners(api);

        // https://github.com/PortSwigger/burp-extensions-montoya-api-examples/tree/main/customlogger
        customLogger(api);

        rdlTab(api);
    }

    private void customLogger(MontoyaApi api) {
        CreateTab tab = new CreateTab(api);
        logging.logToOutput("Practice Tab created");
    }

    private void rdlTab(MontoyaApi api){
        RDLTab RDLTab = new RDLTab(api);
        logging.logToOutput("RDL Tab Created");
    }

    private void eventListeners(MontoyaApi api) {
        // register the HTTP handler for event listeners
        http.registerHttpHandler(new EventListenerHttpHandler());

        // register new Proxy handlers
        proxy.registerRequestHandler(new EventListenerRequestHandler(api));
        proxy.registerResponseHandler(new EventListenerResponseHandler(api));

        // register a new Audit Issue handler
        scanner.registerAuditIssueHandler(new MyAuditIssueListenerHandler());

        // register a new extension unload handler (whatever that means)
        extension.registerUnloadingHandler(new MyExtensionUnloadHandler());

        api.logging().logToOutput("event listeners registered");
    }

    private void registerBasicProxyHandlers(MontoyaApi api) {
        //Register proxy handlers with Burp.
        api.proxy().registerRequestHandler(new MyProxyHttpRequestHandler());
        api.proxy().registerResponseHandler(new MyProxyHttpResponseHandler());

        // write a message to our output stream
        api.logging().logToOutput("proxy handlers registered");
    }

    void registerHttpHandlers(MontoyaApi api){
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

    private class MyAuditIssueListenerHandler implements AuditIssueHandler {
        @Override
        public void handleNewAuditIssue(AuditIssue auditIssue) {
            logging.logToOutput("New scan issue: " + auditIssue.name());
        }
    }

    private class MyExtensionUnloadHandler implements ExtensionUnloadingHandler {
        @Override
        public void extensionUnloaded() {
            logging.logToOutput("Extension was unloaded.");
        }
    }
}
