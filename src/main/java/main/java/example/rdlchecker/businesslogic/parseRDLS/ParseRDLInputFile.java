package main.java.example.rdlchecker.businesslogic.parseRDLS;

import java.io.*;

public abstract class ParseRDLInputFile {
    protected File outputFile;
    protected String domainName;

    abstract void parseRDL(BufferedReader reader) throws IOException;
    abstract String getRDLFileName(String line);

    boolean validFileName(String fileName){
        if(fileName == null) return true;
        if(fileName.equals("./")) return true;
        if(fileName.equals(".")) return true;
        if(fileName.equals("..")) return true;
        if(fileName.equals("/..")) return true;
        return fileName.contains("/");
    }

    abstract void generateURLs(BufferedReader reader, String outputFilePath, String domain) throws IOException;

    void addToOutputFile(String fileName) throws IOException {
        PrintWriter writer;
        writer = new PrintWriter(new FileWriter(outputFile, true), true);
        String url = "https://" + domainName + "/" + fileName.replaceAll("\\\\", "/");
        writer.println(url);
        writer.close();
    }
}
