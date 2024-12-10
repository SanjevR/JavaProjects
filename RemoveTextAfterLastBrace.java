/**
 * @author Sanjev Rajaram
 * @since	Dec/09/2024
 * 
 * 
 * */

import java.io.IOException;
import java.nio.file.*;
import java.util.*;
public class RemoveTextAfterLastBrace {
   public static void main(String[] args) {
       RemoveTextAfterLastBrace processor = new RemoveTextAfterLastBrace();
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
    		   "/Source/java/SwaggerSoBright.java",
   			"/Source/java/SanjevLovesSwedaSoMuch.java",
   			"/Source/java/NovemberBabies.java",
   			
       );
       for (String relativePath : classFilesToProcess) {
           String filePath = sourceRoot + relativePath;
           try {
               List<String> lines = Files.readAllLines(Paths.get(filePath));
               int lastBraceIndex = findLastClosingBraceIndex(lines);
               if (lastBraceIndex < lines.size() - 1) {
                   // Remove lines after the last closing brace
                   List<String> truncatedLines = lines.subList(0, lastBraceIndex + 1);
                   Files.write(Paths.get(filePath), truncatedLines);
                   System.out.println("Removed text after last closing brace in: " + filePath);
               } else {
                   System.out.println("No extra text found after last closing brace in: " + filePath);
               }
           } catch (IOException e) {
               System.out.println("An error occurred while processing file: " + filePath);
               e.printStackTrace();
           }
       }
   }
   private int findLastClosingBraceIndex(List<String> lines) {
       int braceBalance = 0;
       int lastBraceIndex = -1;
       boolean inSingleLineComment = false;
       boolean inMultiLineComment = false;
       boolean inSingleQuote = false;
       boolean inDoubleQuote = false;
       for (int i = 0; i < lines.size(); i++) {
           String line = lines.get(i);
           String processedLine = line;
           // Remove strings and comments from the line to avoid counting braces inside them
           processedLine = removeStringsAndComments(processedLine);
           for (char c : processedLine.toCharArray()) {
               if (c == '{') {
                   braceBalance++;
               } else if (c == '}') {
                   braceBalance--;
                   if (braceBalance == 0) {
                       lastBraceIndex = i;
                   }
               }
           }
       }
       // If the last brace index is found, return it; else return the last line index
       return lastBraceIndex != -1 ? lastBraceIndex : lines.size() - 1;
   }
   private String removeStringsAndComments(String line) {
       StringBuilder sb = new StringBuilder();
       boolean inSingleQuote = false;
       boolean inDoubleQuote = false;
       boolean inLineComment = false;
       boolean inBlockComment = false;
       char prevChar = '\0';
       for (int i = 0; i < line.length(); i++) {
           char c = line.charAt(i);
           if (inLineComment) {
               break; // Ignore the rest of the line
           } else if (inBlockComment) {
               if (prevChar == '*' && c == '/') {
                   inBlockComment = false;
               }
           } else if (inSingleQuote) {
               if (c == '\'' && prevChar != '\\') {
                   inSingleQuote = false;
               }
           } else if (inDoubleQuote) {
               if (c == '"' && prevChar != '\\') {
                   inDoubleQuote = false;
               }
           } else {
               if (prevChar == '/' && c == '/') {
                   inLineComment = true;
                   sb.setLength(sb.length() - 1); // Remove the previous '/'
               } else if (prevChar == '/' && c == '*') {
                   inBlockComment = true;
                   sb.setLength(sb.length() - 1); // Remove the previous '/'
               } else if (c == '\'') {
                   inSingleQuote = true;
               } else if (c == '"') {
                   inDoubleQuote = true;
               } else {
                   sb.append(c);
               }
           }
           prevChar = c;
       }
       return sb.toString();
   }
}