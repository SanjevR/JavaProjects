/**
 * @author Sanjev Rajaram
 * @since	Dec/09/2024
 * 
 * 
 * */

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class PackageAdd {

	 public static void main(String[] args) throws Exception {
		 PackageAdd packageAdd = new PackageAdd();
		 packageAdd.getAllFile();
	   }
	   public void getAllFile() {
	       String sourceRoot = "C:/Sanjev/Source Code/productrelease";
	       String targetRoot = "C:/Sanjev/productrelease-NEW";

/**
 * For the List below classFilesToInspect you can add as many files in your path as you want.
 * I previously had about 400 files in my computer to put to the test.
 * I reduced it to the two for the purposes of putting this on Github.
 * 
 * */
	       List<String> classFilesToInspect = new ArrayList<>();
	       classFilesToInspect.add("/Source/java/FreeThrow.java");
               classFilesToInspect.add("/Source/java/AroundTheWorld.java");
               classFilesToInspect.add("/Source/java/LayUp.java");
		

	       for (String relativePath : classFilesToInspect) {
	           String inputFile = sourceRoot + relativePath;
	           String targetFile = targetRoot + relativePath;
	           try {
	               List<String> inputLines = Files.readAllLines(Paths.get(inputFile));
	               // Check if the source file contains a line starting with "package com."
	               boolean packageLineExists = false;
	               for (String line : inputLines) {
	                   if (line.trim().startsWith("package com.")) {
	                       packageLineExists = true;
	                       break;
	                   }
	               }
	               if (packageLineExists) {
	                   System.out.println("Package line exists in source file, skipping: " + inputFile);
	                   continue; // Skip to the next file
	               }
	               // Read the target file to find the package line
	               List<String> targetLines = Files.readAllLines(Paths.get(targetFile));
	               String packageLine = null;
	               for (String line : targetLines) {
	                   if (line.trim().startsWith("package com.")) {
	                       packageLine = line;
	                       break;
	                   }
	               }
	               if (packageLine != null) {
	                   // Insert the package line at the beginning of the source file
	                   inputLines.add(0, packageLine);
	                   Files.write(Paths.get(inputFile), inputLines);
	                   System.out.println("Inserted package line into: " + inputFile);
	               } else {
	                   System.out.println("Package line not found in target file: " + targetFile);
	               }
	           } catch (IOException e) {
	               System.out.println("An error occurred while processing file: " + inputFile);
	               e.printStackTrace();
	           }
	       }
	   }

}
