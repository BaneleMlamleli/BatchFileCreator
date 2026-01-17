package util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ReadAndWriteToFile {

    public static Logger logger = LogManager.getLogger(new Object() {
    }.getClass().getName());

    public static void deleteFileAtInitialExecution(){
        // Always delete file at initial execution to prevent duplication
        try {
            Path entitiesFileToDelete = Paths.get(System.getProperty("user.dir"), "src", "main", "resources", "scraped_entities.csv");
            if (Files.exists(entitiesFileToDelete)) {
                Files.delete(entitiesFileToDelete);
                System.out.println("Deleted existing scraped_entities.csv");
            }

            Path individualsFileToDelete = Paths.get(System.getProperty("user.dir"), "src", "main", "resources", "scraped_individuals.csv");
            if (Files.exists(individualsFileToDelete)) {
                Files.delete(individualsFileToDelete);
                System.out.println("Deleted existing scraped_individuals.csv");
            }
            
        } catch (IOException e) {
            logger.error("'" + e.getMessage() + "' in method '" + new Object() {
            }.getClass().getEnclosingMethod().getName() + "'");
        }
    }

}
