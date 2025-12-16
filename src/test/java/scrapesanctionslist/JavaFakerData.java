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
            int yearOfBirth = (int) (Math.random() * 2008) + 1900;
            int monthOfBirth = (int) (Math.random() * 12) + 1;
            int dateOfBirth = (int) (Math.random() * 30) + 1;
            LocalDate dob = LocalDate.of(yearOfBirth, monthOfBirth, dateOfBirth);
            System.out.println("SA ID Number: " + SAIDNumberGenerator.generateSAID(LocalDate.of(yearOfBirth, monthOfBirth, dateOfBirth), gender[(int) (Math.random() * 1) + 0], true) + ", D.O.B: " + dob);
        }
    }

    @Test
    public void entityTestData(){}

}
