package scrapesanctionslist;

import java.time.LocalDate;
import org.testng.annotations.Test;
import com.github.javafaker.Faker;

import util.SAIDNumberGenerator;


public class JavaFakerData {

    @Test
    public void individualTestData(){

        Faker faker = new Faker();

        for (int i = 0; i < 5; i++) {

            boolean gender[] = {true, false};
            
            long startEpoch = LocalDate.now().minusYears(100).toEpochDay();
            long endEpoch = LocalDate.now().minusYears(18).toEpochDay();
            long randomEpochDay = java.util.concurrent.ThreadLocalRandom.current().nextLong(startEpoch, endEpoch + 1);
            LocalDate randomDob = LocalDate.ofEpochDay(randomEpochDay);
            int yearOfBirth = randomDob.getYear();
            int monthOfBirth = randomDob.getMonthValue();
            int dateOfBirth = randomDob.getDayOfMonth();

            LocalDate dob = LocalDate.of(yearOfBirth, monthOfBirth, dateOfBirth);
            System.out.println("SA ID Number: " + SAIDNumberGenerator.generateSAID(LocalDate.of(yearOfBirth, monthOfBirth, dateOfBirth), gender[(int) (Math.random() * 1) + 0], true) + ", D.O.B: " + dob);
        }
    }

    @Test
    public void entityTestData(){}

}
