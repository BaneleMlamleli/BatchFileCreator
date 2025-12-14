package scrapesanctionslist;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

/**
 *
 * @author banele
 *
 * STEPS:
 * 1. Open the browser on the home page
 * 2. Type: Select your party type (Entity or Individual)
 * 3. Country: Select the country (193 countries in total)
 * 4. Click on 'Search' button
 * 5. Loop through the list of parties displayed per country
 * 6. Read data for each party
 * 7. Record data in the Database for each party type
 *  - 2 tables: Entity and Individual
 */

public class SanctionsListData {

    WebDriver driver;

    @SuppressWarnings("null")
    @Test
    @Parameters({"partyType"})
    public void scrapeWebsite(@Optional("Individual") String partyType){
        System.out.println("start of method");
        driver = new FirefoxDriver();
        driver.get("https://sanctionssearch.ofac.treas.gov/");
        driver.manage().window().maximize();
        
        Select selectType = new Select(driver.findElement(By.id("ctl00_MainContent_ddlType")));
        Select selectCountry = new Select(driver.findElement(By.id("ctl00_MainContent_ddlCountry")));

        selectType.selectByValue(partyType);
        
        System.out.println("Total countries: " + selectCountry.getOptions().size());
        
        int allCountries = selectCountry.getOptions().size();

        for (int a = 1; a < allCountries; a++) { // starting at 1 because index 0 will select the 'All' option which not what we need
           
            new WebDriverWait(driver, Duration.ofSeconds(10))
            .until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.id("ctl00_MainContent_ddlCountry")));
            
            Select selectCountries = new Select(driver.findElement(By.id("ctl00_MainContent_ddlCountry")));
            selectCountries.selectByIndex(a); // select country

            // Click on the 'Search' button
            new WebDriverWait(driver, Duration.ofSeconds(10))
                    .until(ExpectedConditions.elementToBeClickable(By.id("ctl00_MainContent_btnSearch")))
                    .click();

            By linksLocator = By.xpath("//table[@id='gvSearchResults']//tr//td//a");
            int size = driver.findElements(linksLocator).size();

            for (int i = 0; i < size; i++) {
                boolean clicked = false;
                int attempts = 0;
                while (!clicked && attempts < 3) {
                    try {
                        List<WebElement> currentLinks = driver.findElements(linksLocator);
                        WebElement link = currentLinks.get(i);

                        new WebDriverWait(driver, Duration.ofSeconds(10))
                            .until(ExpectedConditions.elementToBeClickable(link)).click();

                        clicked = true;
                    } catch (org.openqa.selenium.StaleElementReferenceException ex) {
                        attempts++;
                        // small pause before retrying
                        try { Thread.sleep(200); } catch (InterruptedException ignored) {}
                    }
                }

                if (!clicked) {
                    System.out.println("Skipping index " + i + " after retries due to staleness.");
                    continue;
                }

                switch (partyType) {
                    case "Entity":
                        new WebDriverWait(driver, Duration.ofSeconds(10))
                            .until(ExpectedConditions.visibilityOfElementLocated(By.id("ctl00_MainContent_lblNameOther")));
                        String entityName = driver.findElement(By.id("ctl00_MainContent_lblNameOther")).getText();
                        System.out.println("Entity Name: " + entityName);

                        try {
                            String idType = driver.findElement(By.xpath("//table[@id='ctl00_MainContent_gvIdentification']//tr[2]//td[1]")).getText();
                            String id = driver.findElement(By.xpath("//table[@id='ctl00_MainContent_gvIdentification']//tr[2]//td[2]")).getText();
                            String entityCountry = driver.findElement(By.xpath("//table[@id='ctl00_MainContent_gvIdentification']//tr[2]//td[3]")).getText();

                            System.out.println("Type: " + idType);
                            System.out.println("Id: " + id);
                            System.out.println("Country: " + entityCountry);
                        } catch (Exception e) {
                            System.out.println("TABLE FOR IDENTIFICATION INFORMATION IS NOT IN THE DOM AT ALL!");
                            e.getMessage();
                        }

                        try {
                            String address = driver.findElement(By.xpath("//div[@id='ctl00_MainContent_pnlAddress']//table//tr[2]//td[1]")).getText();
                            String city = driver.findElement(By.xpath("//div[@id='ctl00_MainContent_pnlAddress']//table//tr[2]//td[2]")).getText();
                            String stateOrProvince = driver.findElement(By.xpath("//div[@id='ctl00_MainContent_pnlAddress']//table//tr[2]//td[3]")).getText();
                            String postalCode = driver.findElement(By.xpath("//div[@id='ctl00_MainContent_pnlAddress']//table//tr[2]//td[4]")).getText();
                            String addressCountry = driver.findElement(By.xpath("//div[@id='ctl00_MainContent_pnlAddress']//table//tr[2]//td[5]")).getText();

                            System.out.println("Address: " + address);
                            System.out.println("City: " + city);
                            System.out.println("State/Province: " + stateOrProvince);
                            System.out.println("Postal code: " + postalCode);
                            System.out.println("Address country: " + addressCountry);
                        } catch (Exception e) {
                            System.out.println("TABLE FOR ADDRESS INFORMATION IS NOT IN THE DOM AT ALL!");
                            // e.getMessage();
                            continue;
                        }
                        System.out.println("************************************************************************"); break;
                    case "Individual":
                        new WebDriverWait(driver, Duration.ofSeconds(10))
                            .until(ExpectedConditions.visibilityOfElementLocated(By.id("ctl00_MainContent_lblFirstName")));

                        String firstname = driver.findElement(By.id("ctl00_MainContent_lblFirstName")).getText();
                        String lastname = driver.findElement(By.id("ctl00_MainContent_lblLastName")).getText();
                        String dob = driver.findElement(By.id("ctl00_MainContent_lblDOB")).getText();
                        String nationality = driver.findElement(By.id("ctl00_MainContent_lblNationality")).getText();
                        String citizenship = driver.findElement(By.id("ctl00_MainContent_lblCitizenship")).getText();
                        System.out.println("Firstname: " + firstname);
                        System.out.println("Lastname: " + lastname);
                        System.out.println("D.O.B: " + dob);
                        System.out.println("Nationality: " + nationality);
                        System.out.println("Citizenship: " + citizenship);

                        try {
                            String idType = driver.findElement(By.xpath("//table[@id='ctl00_MainContent_gvIdentification']//tr[2]//td[1]")).getText();
                            String id = driver.findElement(By.xpath("")).getText();
                            String entityCountry = driver.findElement(By.xpath("")).getText();
                            System.out.println("Type: " + idType);
                            System.out.println("Id: " + id);
                            System.out.println("Country: " + entityCountry);
                        } catch (Exception e) {
                            System.out.println("TABLE FOR IDENTIFICATION INFORMATION IS NOT IN THE DOM AT ALL!");
                            e.getMessage();
                        }

                        try {
                            String address = driver.findElement(By.xpath("//div[@id='ctl00_MainContent_pnlAddress']//table//tr[2]//td[1]")).getText();
                            String city = driver.findElement(By.xpath("//div[@id='ctl00_MainContent_pnlAddress']//table//tr[2]//td[2]")).getText();
                            String stateOrProvince = driver.findElement(By.xpath("//div[@id='ctl00_MainContent_pnlAddress']//table//tr[2]//td[3]")).getText();
                            String postalCode = driver.findElement(By.xpath("//div[@id='ctl00_MainContent_pnlAddress']//table//tr[2]//td[4]")).getText();
                            String addressCountry = driver.findElement(By.xpath("//div[@id='ctl00_MainContent_pnlAddress']//table//tr[2]//td[5]")).getText();

                            System.out.println("Address: " + address);
                            System.out.println("City: " + city);
                            System.out.println("State/Province: " + stateOrProvince);
                            System.out.println("Postal code: " + postalCode);
                            System.out.println("Address country: " + addressCountry);
                        } catch (Exception e) {
                            System.out.println("TABLE FOR ADDRESS INFORMATION IS NOT IN THE DOM AT ALL!");
                            // e.getMessage();
                            continue;
                        }
                        System.out.println("************************************************************************"); break;
                    default:
                        System.out.println("Error. Only 'Entity' and 'Individual' are expected options"); break;
                }

                // go back and wait until results table is present again
                new WebDriverWait(driver, Duration.ofSeconds(10))
                    .until(ExpectedConditions.elementToBeClickable(By.id("ctl00_MainContent_btnBack")))
                    .click();

                new WebDriverWait(driver, Duration.ofSeconds(10))
                    .until(ExpectedConditions.presenceOfElementLocated(By.id("gvSearchResults")));
            }
        }

        System.out.println("end of method");
    }

}
