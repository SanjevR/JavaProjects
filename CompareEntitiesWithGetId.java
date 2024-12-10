/**
 * @author Sanjev Rajaram
 * @since	Dec/09/2024
 * 
 * 
 * */

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
public class CompareEntitiesWithGetId {
   public static void main(String[] args) throws Exception {
       CompareEntitiesWithGetId search = new CompareEntitiesWithGetId();
       search.searchForGetId();
   }
   public void searchForGetId() {
       // Define source and target roots
       String sourceRoot = "\"C:/Sanjev/productrelease-NEW\"";
       String targetRoot = "C:/Sanjev/Source Code/productrelease"; 
       // 1. Get all entity classes from source
       List<Path> sourceEntities = getAllEntityClasses(Paths.get(sourceRoot));
       // 2. Get all entity classes from target
       List<Path> targetEntities = getAllEntityClasses(Paths.get(targetRoot));
       // 3. Filter source entities that contain getId() line as specified
       List<Path> sourceEntitiesWithGetId = filterClassesContainingGetId(sourceEntities);
       // 4. Filter target entities that contain getId() line as specified
       List<Path> targetEntitiesWithGetId = filterClassesContainingGetId(targetEntities);
       // Convert target files to a set of names for quick lookup
       List<String> targetClassNames = targetEntities.stream()
               .map(this::relativeClassName)
               .collect(Collectors.toList());
       List<String> targetClassNamesWithGetId = targetEntitiesWithGetId.stream()
               .map(this::relativeClassName)
               .collect(Collectors.toList());
       // Find classes that are present in both source and target
       // but have getId in source and do not have getId in target.
       List<String> problematicClasses = sourceEntitiesWithGetId.stream()
               .map(this::relativeClassName)
               // Keep only those that appear in target
               .filter(targetClassNames::contains)
               // Keep only those that do NOT have getId in target
               .filter(className -> !targetClassNamesWithGetId.contains(className))
               .collect(Collectors.toList());
       // Print the results
       System.out.println("Classes that have getId() in source but not in target:");
       for (String className : problematicClasses) {
           System.out.println(" - " + className);
       }
   }
   /**
    * Retrieves all Java entity classes from a given directory.
    * In this context, we'll assume all .java files under the root are "entity classes."
    */
   private List<Path> getAllEntityClasses(Path rootDir) {
       try (Stream<Path> walk = Files.walk(rootDir)) {
           return walk.filter(Files::isRegularFile)
                   .filter(p -> p.toString().endsWith(".java"))
                   .collect(Collectors.toList());
       } catch (IOException e) {
           e.printStackTrace();
           return new ArrayList<>();
       }
   }
   private List<Path> filterClassesContainingGetId(List<Path> classes) {
       List<Path> result = new ArrayList<>();
       // Regex to match a line that starts with public and ends with getId() {
       // possibly with modifiers and return type in between
       String regex = "^\\s*public.*getId\\(\\)\\s*\\{\\s*$";
       for (Path classFile : classes) {
           try {
               List<String> lines = Files.readAllLines(classFile);
               // Check if any line matches the specified pattern
               boolean containsGetId = lines.stream().anyMatch(line -> line.matches(regex));
               if (containsGetId) {
                   result.add(classFile);
               }
           } catch (IOException e) {
               System.out.println("Error reading file: " + classFile.toString());
               e.printStackTrace();
           }
       }
       return result;
   }
   private String relativeClassName(Path fullPath) {
       // For simplicity, we return the file name.
       return fullPath.getFileName().toString();
   }
}