package main.java.example.rdlchecker.businesslogic.parseRDLS;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;

public class WindowsRDLParser extends ParseRDLInputFile {
    @Override
    public void parseRDL(BufferedReader reader) throws IOException {
        String commandExecutionLocation = null;
        String currentDirectory = null;

        String line;
        while ((line = reader.readLine()) != null) {
            if (line.toLowerCase().contains("directory of")) {
                currentDirectory = line.substring(line.indexOf("Directory of") + 12).trim();
                currentDirectory = removeDriveLetter(currentDirectory);
                if (commandExecutionLocation == null) {
                    commandExecutionLocation = currentDirectory;
                }
            } else {
                String fileName = getRDLFileName(line);
                if (validFileName(fileName)) continue;

                if (currentDirectory != null) {
                    if (currentDirectory.equals(commandExecutionLocation)) {
                        addToOutputFile(fileName);
                    } else {
                        currentDirectory = currentDirectory.replace(commandExecutionLocation, "");
                        currentDirectory = currentDirectory.replaceFirst("^\\\\", "");
                        addToOutputFile(currentDirectory + "\\" + fileName);
                    }
                }
            }
        }
    }

    private String removeDriveLetter(String path) {
        return path.replaceFirst("^[a-zA-Z]:", "");
    }

    @Override
    public String getRDLFileName(String line) {
        String[] splitLine = line.split("\\s+",5);
        if (splitLine.length < 5) {
            return null;
        }

        String fileName = splitLine[splitLine.length - 1];

        // Exclude directories, junctions, and lines containing square brackets
        if ("<junction>".equalsIgnoreCase(splitLine[3]) || "File(s)".equalsIgnoreCase(splitLine[2]) || "Dir(s)".equalsIgnoreCase(splitLine[2])) {
            return null;
        }

        return fileName.replaceAll("\\?",":");
    }

    @Override
    public void generateURLs(BufferedReader scanner, String outputFilePath, String domain) throws IOException {
        this.outputFile = new File(outputFilePath);
        this.domainName = domain;
        parseRDL(scanner);
    }
}
