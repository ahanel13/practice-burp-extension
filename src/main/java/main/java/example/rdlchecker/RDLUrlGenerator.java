package main.java.example.rdlchecker;

import main.java.example.rdlchecker.parseRDLS.LinuxRDLParser;
import main.java.example.rdlchecker.parseRDLS.WindowsRDLParser;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

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

    public RDLUrlGenerator(String inputRDLFilePath, String outputFilePath) throws IOException {
        this.inputRDLFilePath = inputRDLFilePath;
        this.outputFilePath = outputFilePath;
        this.filePaths = new ArrayList<>();
        this.os = detectOS();
        readRDLInputFile();
    }

    private String detectOS() throws FileNotFoundException {
        Scanner scanner = new Scanner(new BufferedReader(new FileReader(inputRDLFilePath)));
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

    public void generateUrls(String domainName) throws IOException {
        PrintWriter writer = new PrintWriter(outputFilePath, StandardCharsets.UTF_8);
        Collections.sort(filePaths);
        for (String path : filePaths) {
            String url = "https://" + domainName + "/" + path.replaceAll("\\\\", "/");
            writer.println(url);
        }
        writer.close();
    }
}
