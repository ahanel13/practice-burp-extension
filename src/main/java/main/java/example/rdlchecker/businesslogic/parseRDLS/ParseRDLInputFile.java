package main.java.example.rdlchecker.businesslogic.parseRDLS;

import java.util.List;
import java.util.Scanner;

public interface ParseRDLInputFile {
    void parseRDL(Scanner scanner);
    String getRDLFileName(String line);

    default boolean validFileName(String fileName){
        if(fileName == null) return true;
        if(fileName.equals("./")) return true;
        if(fileName.equals(".")) return true;
        if(fileName.equals("..")) return true;
        if(fileName.equals("/..")) return true;
        return fileName.contains("/");
    }

    List<String> generateURLs(Scanner scanner);
}
