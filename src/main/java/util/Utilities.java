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


/**
 * Utility helper methods used across the application.
 *
 * <p>Provides:
 * <ul>
 *   <li>startup file cleanup for scraped CSVs</li>
 *   <li>date parsing/formatting utilities</li>
 *   <li>simple regex-based character checks</li>
 * </ul>
 *
 * <p>Note: All methods are static for convenience and global access.
 *
 * <p>Fields:
 * <ul>
 *   <li>{@code public static Logger logger} - a Log4j2 logger used to record errors and informational
 *       messages produced by the utility methods.</li>
 * </ul>
 *
 * Methods:
 *
 * <ul>
 *   <li>{@link #deleteFileAtInitialExecution()}
 *     <p>Deletes two resource files (if present) to prevent duplicate data on initial execution:
 *     {@code src/main/resources/scraped_entities.csv} and {@code src/main/resources/scraped_individuals.csv}.
 *     Any IOException encountered during deletion is caught and logged via {@code logger}.</p>
 *   </li>
 *
 *   <li>{@link #invertAndFormatDate(String)}
 *     <p>Parses an input date string using the pattern {@code M/d/yyyy} (for example, {@code 1/2/2023})
 *     and returns the same date formatted as {@code dd/MM/yyyy} (for example, {@code 02/01/2023}).</p>
 *     @param dateString the date string to parse; must match pattern {@code M/d/yyyy}
 *     @return the date formatted as {@code dd/MM/yyyy}
 *     @throws java.time.format.DateTimeParseException if the input cannot be parsed with the expected pattern
 *   </li>
 *
 *   <li>{@link #regexForSpecialChar(String)}
 *     <p>Checks the provided string for the presence of any character in the class used by the
 *     internal regular expression. The character class includes word characters (letters, digits,
 *     underscore), whitespace, and a range of punctuation characters such as:</p>
 *     <pre>
 *     $ + ; : ! @ # % & * ( ) _ = | &lt; &gt; ? { } [ ] ~ -
 *     </pre>
 *     <p>Returns {@code true} if any such character is found; returns {@code false} otherwise.</p>
 *     @param value the string to test (note: passing {@code null} will result in {@link NullPointerException})
 *     @return {@code true} if the input contains any matched character, {@code false} otherwise
 *   </li>
 * </ul>
 * 
 * @author banele mlamleli
 * @since 1.0
 */

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
