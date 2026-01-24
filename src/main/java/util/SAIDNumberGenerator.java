package util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Random;

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
