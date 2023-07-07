package slssdhs.xyz;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@RunWith(Parameterized.class)
public class AirbnbTest {
    private WebDriver driver;

    private static Object[][] inputData = null;
    private String LocationInput;

    private int YearInput;
    private int CheckInMonthInput;
    private int CheckInDayInput;

    private int CheckOutMonthInput;
    private int CheckOutDayInput;

    private int AdultsInput;
    private int ChildrenInput;
    private int InfantsInput;
    private int PetsInput;
    private String ExpectedResult;
    final LocalDate TODAY = LocalDate.now();

    public AirbnbTest(String locationInput, int yearInput, int checkInMonthInput, int checkInDayInput, int checkOutMonthInput, int checkOutDayInput, int adultsInput, int childrenInput, int infantsInput, int petsInput, String expectedResult) {
        LocationInput = locationInput;
        YearInput = yearInput;
        CheckInMonthInput = checkInMonthInput;
        CheckInDayInput = checkInDayInput;
        CheckOutMonthInput = checkOutMonthInput;
        CheckOutDayInput = checkOutDayInput;
        AdultsInput = adultsInput;
        ChildrenInput = childrenInput;
        InfantsInput = infantsInput;
        PetsInput = petsInput;
        ExpectedResult = expectedResult;
    }

    @Before
    public void setUp(){
        driver = new ChromeDriver();
    }

    @Parameterized.Parameters
    public static Object[][] data() {
//        if (inputData == null ) {
//            inputData = InputFileReader.readFile("input.txt");
//        }
//        return inputData;
       return new Object[][] { { "Ottawa", 2023,7,51,7,26,1,1,0,0, "Ottawa" },{ "San Francisco", 2023,7,3,8,5,1,0,0,0,"San Francisco" } };
    }

    @Test
    public void TestSearch() throws InterruptedException {
        driver.get("https://www.airbnb.ca");
        driver.manage().window().maximize();
        driver.manage().deleteAllCookies();
        WebElement location_input_button = driver.findElement(By.xpath("//button[@data-index='0']"));
        location_input_button.click();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id='bigsearch-query-location-input']")));
        WebElement location_input = driver.findElement(By.xpath("//input[@id='bigsearch-query-location-input']"));
        location_input.sendKeys(LocationInput);
        location_input.sendKeys(Keys.ENTER);
        String checkin_xpath = xpathConstructor(YearInput, CheckInMonthInput, CheckInDayInput);
        WebElement checkin_input = driver.findElement(By.xpath(checkin_xpath));
        checkin_input.click();
        String checkout_xpath = xpathConstructor(YearInput, CheckOutMonthInput, CheckOutDayInput);
        WebElement checkout_input = driver.findElement(By.xpath(checkout_xpath));
        checkout_input.click();
        WebElement WhoButton = driver.findElement(By.xpath("//div[@data-testid='structured-search-input-field-guests-button']"));
        WhoButton.click();
        WebElement AdultsButton = driver.findElement(By.xpath("//button[@data-testid='stepper-adults-increase-button']"));
        clickMultipleTimes(AdultsButton, AdultsInput);
        WebElement ChildrenButton = driver.findElement(By.xpath("//button[@data-testid='stepper-children-increase-button']"));
        clickMultipleTimes(ChildrenButton, ChildrenInput);
        WebElement InfantsButton = driver.findElement(By.xpath("//button[@data-testid='stepper-infants-increase-button']"));
        clickMultipleTimes(InfantsButton, InfantsInput);
        WebElement PetsButton = driver.findElement(By.xpath("//button[@data-testid='stepper-pets-increase-button']"));
        clickMultipleTimes(PetsButton, PetsInput);
        WebElement SearchButton = driver.findElement(By.xpath("//button[@data-testid='structured-search-input-search-button']"));
        SearchButton.click();
        wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//h1[@elementtiming='LCP-target']/span"), "Search results"));
        WebElement Result = driver.findElement(By.xpath("//h1[@elementtiming='LCP-target']/span"));
        String ResultText = Result.getText();
        assert (ResultText.contains(ExpectedResult));


    }


    private String xpathConstructor(int year, int month, int day) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d, EEEE, MMMM yyyy", Locale.ENGLISH);
        LocalDate date = LocalDate.of(year, month, day);
        String date_string = "";
        if (date.isEqual(TODAY)) {
            date_string = "Today";
        } else {
            date_string = date.format(formatter);
        }
        return "//td[contains(@aria-label,'" + date_string + "')]";
    }

    public void clickMultipleTimes(WebElement element, int times) {
        for (int i = 0; i < times; i++) {
            element.click();
        }
    }

    @After
    public void tearDown() throws InterruptedException {
        Thread.sleep(10000);
        driver.quit();
    }
}
