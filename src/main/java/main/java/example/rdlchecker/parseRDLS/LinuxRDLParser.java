package main.java.example.rdlchecker.parseRDLS;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class LinuxRDLParser implements ParseRDLInputFile{
    private final List<String> filePaths = new ArrayList<>();

    @Override
    public void parseRDL(Scanner scanner) {
        String pwd = "";
        String commandExecutionLocation = null;
        String fileName;

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if(presentWorkingDir(line)){
                pwd = getPWD(line);
                if(commandExecutionLocation == null){
                    commandExecutionLocation = pwd;
                    pwd = null;
                }
                continue;
            }
            else {
                fileName = getRDLFileName(line);
                if(validFileName(fileName)) continue;
            }

            if(pwd != null){
                filePaths.add(pwd.concat("/").concat(fileName));
            } else {
                filePaths.add(fileName);
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
    public List<String> generateURLs(Scanner scanner) {
        parseRDL(scanner);
        return filePaths;
    }
}
