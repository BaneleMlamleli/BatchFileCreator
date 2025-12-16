# Batch File Creator (BFC)

This is a small Java application that creates and generates a correctly formatted batch file with test data. I created this because I'm simply lazy to manually create these batch files.

## Project design

This project is split into three main parts.

- Front-end: Using JSwing application for GIU
- Scraping the [OFAC](https://sanctionssearch.ofac.treas.gov/) Sanctions List website using Selenium with Java
- Storing the Sanctions list test data into SQLite database

## Tech-stack for the project

- ![Apache NetBeans IDE](./src/main/java/icons/netbeans.png) IDE 28
- ![Java 25](./src/main/java/icons/java.png) Java 25
- ![SQLite](./src/main/java/icons/sqlite.png) SQLite
- ![Maven](./src/main/java/icons/Maven.png) Maven
- ![Selenium 4](./src/main/java/icons/selenium.png) Selenium 4
- ![Git-flow](./src/main/java/icons/git.png) Git

## Execution

There are two ways one can run this project

- NetBeans 
    - Launch the jar file
    - Run it directly by clicking the **Run Project** button
- Selenium (**N.B.,** This will only be executed once as it creates test data and writes it into the database)
    - You can run the method **scrapeWebsite** by clicking on the *Run* icon next to the method
    - Run from testng.xml with the command **mvn test -DsuiteXmlFile=testng.xml**
        - **N.B.,** Depending on which party type you need, change the parameter in the testng.xml file. Expected parameter '*Entity*' or '*Individual*' 
    - Run using maven command **mvn test**

## Icons

Icons used are from [flaticon](https://www.flaticon.com/)

- [excel](https://www.flaticon.com/free-icons/excel) icon
- [file type](https://www.flaticon.com/free-icons/file-type) icon
- [csv file](https://www.flaticon.com/free-icons/csv-file) icon
- [batch processing](https://www.flaticon.com/free-icons/batch-processing) icon
