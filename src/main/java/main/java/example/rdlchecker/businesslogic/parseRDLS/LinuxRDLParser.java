package main.java.example.rdlchecker.businesslogic.parseRDLS;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class LinuxRDLParser extends ParseRDLInputFile {
    private final List<String> filePaths = new ArrayList<>();

    @Override
    public void parseRDL(BufferedReader reader) throws IOException {
        String pwd = "";
        String commandExecutionLocation = null;
        String fileName;

        String line;
        while ((line = reader.readLine()) != null) {
            if (presentWorkingDir(line)) {
                pwd = getPWD(line);
                if (commandExecutionLocation == null) {
                    commandExecutionLocation = pwd;
                    pwd = null;
                }
                continue;
            } else {
                fileName = getRDLFileName(line);
                if (validFileName(fileName)) continue;
            }

            if (pwd != null) {
                addToOutputFile(pwd.concat("/").concat(fileName));
            } else {
                addToOutputFile(fileName);
            }
        }
    }

    private String getPWD(String line) {
        String pwd = setPWD(line);
        return pwd.replaceFirst(":$","");
    }

    private String setPWD(String line) {
        line = line.replaceFirst("^\\./","");
        line = line.replaceFirst("^/","");
        return line;
    }

    private boolean presentWorkingDir(String line) {
        return line.startsWith("./") || line.startsWith("/");
    }

    @Override
    public String getRDLFileName(String line) {
        String[] splitLine = line.split("\\s+", 9);

        if (splitLine.length < 9) {
            return null;
        }

        String fileName = splitLine[8];

        if (fileName.contains("->")) {
            return null;
        }

        return fileName.replaceAll("’","'");
    }

    @Override
    public void generateURLs(BufferedReader scanner, String outputFilePath, String domain) throws IOException {
        this.outputFile = new File(outputFilePath);
        this.domainName = domain;
        parseRDL(scanner);
    }
}
