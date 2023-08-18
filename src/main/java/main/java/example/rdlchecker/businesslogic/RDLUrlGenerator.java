package main.java.example.rdlchecker.businesslogic;

import burp.api.montoya.logging.Logging;
import main.java.example.rdlchecker.businesslogic.parseRDLS.LinuxRDLParser;
import main.java.example.rdlchecker.businesslogic.parseRDLS.WindowsRDLParser;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/*
* Preferably the commands the app teams runs will change but this code will process the following commands automatically
*   Linux: (pwd;ls -laR)
* Windows: dir /s /a
*/
public class RDLUrlGenerator {
    private final String outputFilePath;
    private final String inputRDLFilePath;
    private final String os;
    private String domain;
    private List<String> filePaths;
    private final Logging logging;
    String border = new String(new char[50]).replace('\0', '=');
    public RDLUrlGenerator(String inputRDLFilePath, String outputFilePath, String domain) throws IOException {
        this.inputRDLFilePath = inputRDLFilePath;
        this.outputFilePath = outputFilePath;
        this.filePaths = new ArrayList<>();
        this.domain = domain;
        this.os = detectOS();
        this.logging = null;
    }

    public RDLUrlGenerator(String inputRDLFilePath, Logging logging) throws IOException {
        this.inputRDLFilePath = inputRDLFilePath;
        this.outputFilePath = inputRDLFilePath.replaceAll(".txt","").concat("_GeneratedURLs.txt");
        this.filePaths = new ArrayList<>();
        this.os = detectOS();
        this.logging = logging;
        logRDLProcessInfo();
        processRDLInputFile();
    }

    private void logRDLProcessInfo() {
        this.logging.logToOutput(border);
        this.logging.logToOutput("InputPath: " + this.inputRDLFilePath);
        this.logging.logToOutput("OutputPath: " + this.outputFilePath);
        this.logging.logToOutput("RDL OS: " + this.os);
    }

    private String detectOS() throws FileNotFoundException {
        FileInputStream fileInputStream = new FileInputStream(inputRDLFilePath);
        InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, StandardCharsets.UTF_8);
        Scanner scanner = new Scanner(new BufferedReader(inputStreamReader));
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (line.toLowerCase().contains("directory of")) {
                return "Windows";
            } else if (line.startsWith("/")) {
                return "Linux";
            }
        }
        scanner.close();
        return null;
    }

    private void processRDLInputFile() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(inputRDLFilePath));
        if (os.equals("Windows")) {
            WindowsRDLParser windowsRDLParser = new WindowsRDLParser();
            windowsRDLParser.generateURLs(reader, outputFilePath, domain);
        } else if (os.equals("Linux")) {
            LinuxRDLParser linuxRDLParser = new LinuxRDLParser();
            linuxRDLParser.generateURLs(reader, outputFilePath, domain);
        }
        reader.close();
    }

    public void generateUrls(){
        try {
            processRDLInputFile();
        } catch (IOException e) {
            logFailure();
        }
    }

    private void logFailure() {
        if(logging != null){
            logging.logToError("Something went wrong when processing RDL.");
        }
    }

    private void logSuccess() {
        if(logging != null){
            logging.logToOutput("RDL process completed.");
            logging.logToOutput(border);
        }
    }
}
