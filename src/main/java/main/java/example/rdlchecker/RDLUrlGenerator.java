package main.java.example.rdlchecker;

import burp.api.montoya.logging.Logging;
import main.java.example.rdlchecker.parseRDLS.LinuxRDLParser;
import main.java.example.rdlchecker.parseRDLS.WindowsRDLParser;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
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
    private List<String> filePaths;
    private final Logging logging;
    String border = new String(new char[50]).replace('\0', '=');
    public RDLUrlGenerator(String inputRDLFilePath, String outputFilePath) throws IOException {
        this.inputRDLFilePath = inputRDLFilePath;
        this.outputFilePath = outputFilePath;
        this.filePaths = new ArrayList<>();
        this.os = detectOS();
        this.logging = null;
        readRDLInputFile();
    }

    public RDLUrlGenerator(String inputRDLFilePath, Logging logging) throws IOException {
        this.inputRDLFilePath = inputRDLFilePath;
        this.outputFilePath = inputRDLFilePath.replaceAll(".txt","").concat("_GeneratedURLs.txt");
        this.filePaths = new ArrayList<>();
        this.os = detectOS();
        this.logging = logging;
        logRDLProcessInfo();
        readRDLInputFile();
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

    private void readRDLInputFile() throws IOException {
        Scanner scanner = new Scanner(new BufferedReader(new FileReader(inputRDLFilePath)));
        if(os.equals("Windows")){
            WindowsRDLParser windowsRDLParser = new WindowsRDLParser();
            filePaths = windowsRDLParser.generateURLs(scanner);
        } else if (os.equals("Linux")) {
            LinuxRDLParser linuxRDLParser = new LinuxRDLParser();
            filePaths = linuxRDLParser.generateURLs(scanner);
        }
        scanner.close();
    }

    public void generateUrls(String domainName){
        PrintWriter writer;
        try {
            writer = new PrintWriter(outputFilePath, StandardCharsets.UTF_8);
            Collections.sort(filePaths);
            for (String path : filePaths) {
                String url = "https://" + domainName + "/" + path.replaceAll("\\\\", "/");
                writer.println(url);
            }
            writer.close();
            logSuccess();
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
