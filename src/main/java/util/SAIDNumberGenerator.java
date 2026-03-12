package util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Random;

/**
 * Utility class for generating South African ID numbers (SA ID).
 *
 * <p>The generated ID follows the structure: YYMMDD SSSS C A Z
 * <ul>
 *   <li>YYMMDD - date of birth formatted as two-digit year, month and day</li>
 *   <li>SSSS   - four-digit gender sequence (0000–4999 for female, 5000–9999 for male)</li>
 *   <li>C      - citizenship indicator (0 = citizen, 1 = non-citizen)</li>
 *   <li>A      - fixed race digit (set to 8 in this implementation)</li>
 *   <li>Z      - single Luhn check digit computed over the preceding 12 digits</li>
 * </ul>
 *
 * <p>The class is stateless and can be used concurrently. Randomness is used to
 * generate the gender sequence portion of the ID.
 *
 * @author banele mlamleli
 * @since 1.0
 */
 
/**
 * Generates a 13-digit South African ID number string composed of:
 * YYMMDD + SSSS + C + A + Z.
 *
 * - dateOfBirth: formatted as "yyMMdd".
 * - isMale: when true, SSSS is in the range 5000–9999; when false, 0000–4999.
 * - isCitizen: true maps to C = 0, false maps to C = 1.
 * - A is fixed to 8 in this implementation.
 * - Z is the Luhn check digit computed from the preceding 12 digits.
 *
 * @param dateOfBirth the person's date of birth (must not be null)
 * @param isMale true to generate a male sequence, false for female
 * @param isCitizen true for South African citizen (C = 0), false otherwise (C = 1)
 * @return a 13-character numeric SA ID string
 * @throws NullPointerException if dateOfBirth is null
 */
 
/**
 * Calculates the Luhn check digit for a given numeric partial ID string.
 *
 * <p>Expected input is the first 12 numeric digits of the ID (YYMMDDSSSSC A).
 * The method applies the Luhn algorithm (doubling every second digit from the
 * right and subtracting 9 when the doubled value exceeds 9) and returns the
 * single check digit (0–9) needed to make the total sum a multiple of 10.
 *
 * @param partialId a numeric string containing the first 12 digits of the ID
 * @return the Luhn check digit (0–9)
 * @throws NumberFormatException if partialId contains non-digit characters
 */

public class SAIDNumberGenerator {

    public static String generateSAID(LocalDate dateOfBirth, boolean isMale, boolean isCitizen) {
        // 1. YYMMDD (Date of Birth)
        String yyMMDD = dateOfBirth.format(DateTimeFormatter.ofPattern("yyMMdd"));

        // 2. SSSS (Gender)
        Random random = new Random();
        int genderBase = isMale ? 5000 : 0;
        int genderSequence = genderBase + random.nextInt(5000);
        String ssss = String.format("%04d", genderSequence);

        // 3. C (Citizenship)
        int citizenship = isCitizen ? 0 : 1;

        // 4. A (Race, always 8 now)
        int race = 8;

        // Combine the first 12 digits
        String partialId = yyMMDD + ssss + citizenship + race;

        // 5. Z (Checksum - Luhn algorithm)
        int checksum = generateLuhnDigit(partialId);

        return partialId + checksum;
    }

    /**
     * Calculates the Luhn check digit for a given 12-digit number string.
     */
    private static int generateLuhnDigit(String partialId) {
        int sum = 0;
        boolean alternate = true;

        for (int i = partialId.length() - 1; i >= 0; i--) {
            int digit = Integer.parseInt(partialId.substring(i, i + 1));

            if (alternate) {
                digit *= 2;
                if (digit > 9) {
                    digit = (digit % 10) + 1;
                }
            }
            sum += digit;
            alternate = !alternate;
        }

        // The check digit is the amount needed to make the sum a multiple of 10
        int checksum = 10 - (sum % 10);
        if (checksum == 10) {
            checksum = 0;
        }
        return checksum;
    }
}
