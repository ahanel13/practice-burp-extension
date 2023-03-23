package main.java.example.rdlchecker.businesslogic.parseRDLS;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class WindowsRDLParser implements ParseRDLInputFile{
    private final List<String> filePaths = new ArrayList<>();

    @Override
    public void parseRDL(Scanner scanner) {
        String commandExecutionLocation = null;
        String currentDirectory = null;

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();

            if (line.toLowerCase().contains("directory of")) {
                currentDirectory = line.substring(line.indexOf("Directory of") + 12).trim();
                currentDirectory = removeDriveLetter(currentDirectory);
                if(commandExecutionLocation == null){
                    commandExecutionLocation = currentDirectory;
                }
            } else {
                String fileName = getRDLFileName(line);
                if (validFileName(fileName)) continue;

                if (currentDirectory != null) {
                    if(currentDirectory.equals(commandExecutionLocation)){
                        filePaths.add(fileName);
                    } else {
                        currentDirectory = currentDirectory.replace(commandExecutionLocation,"");
                        currentDirectory = currentDirectory.replaceFirst("^\\\\","");
                        filePaths.add(currentDirectory + "\\" + fileName);
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
    public List<String> generateURLs(Scanner scanner) {
        parseRDL(scanner);
        return filePaths;
    }
}
