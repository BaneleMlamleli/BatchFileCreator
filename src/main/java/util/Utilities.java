package util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Utilities {

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

    public static String invertAndFormatDate(String dateString){
        DateTimeFormatter in = DateTimeFormatter.ofPattern("M/d/yyyy");
        LocalDate parsed = LocalDate.parse(dateString, in);
        DateTimeFormatter out = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String dateFormatted = parsed.format(out);
        return dateFormatted;
    }

    public static boolean regexForSpecialChar(String value){
        Pattern p = Pattern.compile("[\\w{0}$\\s+;:!@#$%&*()_+=|<>?{}\\\\[\\\\]~-]");
        Matcher m = p.matcher(value);
        return m.find(); // Returns true if a special character is found
    }

}
