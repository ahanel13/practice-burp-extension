package main.java.example.rdlchecker;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import static org.junit.Assert.*;

public class RDLUrlGeneratorTest {
    String linuxRDL = "src/test/resources/rdls/linux-rdl.txt";
    String linux_generated_urls = "src/test/resources/rdls/output/linux_generated_urls";
    String windowsRDL = "src/test/resources/rdls/windows-rdl.txt";
    String windows_generated_urls = "src/test/resources/rdls/output/windows_generated_urls";
    String domainName = "example.com";
    @Test
    public void generateUrls() throws IOException {
        removeOldGeneratedFiles();
        generateUrlsForWindows();
        generateUrlsForLinux();
        assertTrue(compareFiles(linux_generated_urls, windows_generated_urls));
    }

    private boolean compareFiles(String linux_generated_urls, String windows_generated_urls) throws IOException {
        ArrayList<String> lines1 = readLines(linux_generated_urls);
        ArrayList<String> lines2 = readLines(windows_generated_urls);
        Collections.sort(lines1);
        Collections.sort(lines2);
        if (!lines1.equals(lines2)) {
            System.out.println("The following lines are different between the files:");
            int i = 0;
            while (i < lines1.size() && i < lines2.size() && lines1.get(i).equals(lines2.get(i))) {
                i++;
            }
            if (i < lines1.size()) {
                System.out.println(linux_generated_urls + " line " + (i+1) + ": " + lines1.get(i));
            }
            if (i < lines2.size()) {
                System.out.println(windows_generated_urls + " line " + (i+1) + ": " + lines2.get(i));
            }
            return false;
        }
        return true;
    }

    private static ArrayList<String> readLines(String filePath) throws IOException {
        ArrayList<String> lines = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String line;
        while ((line = reader.readLine()) != null) {
            lines.add(line);
        }
        reader.close();
        return lines;
    }

    private void removeOldGeneratedFiles() {
        // Delete Linux file if it exists
        File linuxFile = new File(linux_generated_urls);
        if (linuxFile.exists() && linuxFile.delete()) {
            System.out.println("Linux file deleted successfully.");
        } else {
            System.out.println("Linux file does not exist.");
        }

        // Delete Windows file if it exists
        File windowsFile = new File(windows_generated_urls);
        if (windowsFile.exists() && windowsFile.delete()) {
            System.out.println("Windows file deleted successfully.");
        } else {
            System.out.println("Windows file does not exist.");
        }
    }

    private void generateUrlsForLinux() throws IOException {
        RDLUrlGenerator rdlUrlGenerator = new RDLUrlGenerator(linuxRDL, linux_generated_urls);
        rdlUrlGenerator.generateUrls(domainName);
    }

    private void generateUrlsForWindows() throws IOException {
        RDLUrlGenerator rdlUrlGenerator = new RDLUrlGenerator(windowsRDL, windows_generated_urls);
        rdlUrlGenerator.generateUrls(domainName);
    }
}