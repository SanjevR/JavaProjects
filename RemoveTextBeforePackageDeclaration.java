/**
 * @author Sanjev Rajaram
 * @since	Dec/09/2024
 * 
 * 
 * */
import java.io.IOException;
import java.nio.file.*;
import java.util.*;
public class RemoveTextBeforePackageDeclaration {
   public static void main(String[] args) {
       RemoveTextBeforePackageDeclaration processor = new RemoveTextBeforePackageDeclaration();
       processor.processFiles();
   }
   public void processFiles() {
       String sourceRoot = "C:/Sanjev/Source Code/productrelease";

/**
 * For the List below classFilesToProcess you can add as many files in your path as you want.
 * I previously had about 400 files in my computer to put to the test.
 * I reduced it to the two for the purposes of putting this on Github.
 * 
 * */
       List<String> classFilesToProcess = Arrays.asList(
    		   "/Source/java/BarclaysCenter.java",
   			"/Source/java/TheNets.java",
   			"/Source/java/NepoBabySwag.java",
       );
       for (String relativePath : classFilesToProcess) {
           String filePath = sourceRoot + relativePath;
           try {
               List<String> lines = Files.readAllLines(Paths.get(filePath));
               int packageIndex = findPackageDeclarationIndex(lines);
               if (packageIndex > 0) {
                   // Remove lines before the package declaration
                   List<String> truncatedLines = lines.subList(packageIndex, lines.size());
                   Files.write(Paths.get(filePath), truncatedLines);
                   System.out.println("Removed text before package declaration in: " + filePath);
               } else {
                   System.out.println("Package declaration not found or already at the beginning in: " + filePath);
               }
           } catch (IOException e) {
               System.out.println("An error occurred while processing file: " + filePath);
               e.printStackTrace();
           }
       }
   }
   private int findPackageDeclarationIndex(List<String> lines) {
       for (int i = 0; i < lines.size(); i++) {
           String line = lines.get(i).trim();
           if (line.startsWith("package ")) {
               return i;
           }
       }
       // If package declaration not found, return 0 to avoid removing content
       return 0;
   }
}