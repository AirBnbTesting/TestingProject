package slssdhs.xyz;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.Duration;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Locale;



@RunWith(Parameterized.class)
public class NewAirbnbTest {
    private static Object[][] inputData = null;
    final LocalDate TODAY = LocalDate.now();
    private static WebDriver driver;
    private static String locationInput;
    private static int yearInput;
    private static int checkInMonthInput;
    private static int checkInDayInput;
    private static int checkOutMonthInput;
    private static int checkOutDayInput;
    private static int adultsInput;
    private static int childrenInput;
    private static int infantsInput;
    private static int petsInput;
    private  String expectedResult;



    private static int CurrentTestIndex = 0;

    @SuppressWarnings("ConstantConditions")
    @Parameterized.Parameters
    public static Object[][] data() {
        int dataSource = 1; // 0: use hard-coded data; 1: use data from input.txt
        if (dataSource == 0) {
            return new Object[][]{{"Ottawa", 2023, 7, 17, 8, 10, 2, 6, 4, 5, "Ottawa"}};
        } else if (dataSource == 1) {
            if (inputData == null) {
                inputData = InputFileReader.readFile("input.txt");
            }
        }
        return inputData;

    }

    public NewAirbnbTest(String locationInput, int yearInput, int checkInMonthInput, int checkInDayInput, int checkOutMonthInput, int checkOutDayInput, int adultsInput, int childrenInput, int infantsInput, int petsInput, String expectedResult) {
        this.locationInput = locationInput;
        this.yearInput = yearInput;
        this.checkInMonthInput = checkInMonthInput;
        this.checkInDayInput = checkInDayInput;
        this.checkOutMonthInput = checkOutMonthInput;
        this.checkOutDayInput = checkOutDayInput;
        this.adultsInput = adultsInput;
        this.childrenInput = childrenInput;
        this.infantsInput = infantsInput;
        this.petsInput = petsInput;
        this.expectedResult = expectedResult;
    }

    @SuppressWarnings("ConstantConditions")
    @Before
    public void setUp() {
        CurrentTestIndex++;
        ChromeOptions options = new ChromeOptions();
        boolean headlessEnabled = true; // set to true to enable headless mode
        if (headlessEnabled) {
            options.addArguments("--headless");
            Dimension windowsSize = new Dimension(1920, 1080);
            options.addArguments("--window-size=" + windowsSize.getWidth() + "," + windowsSize.getHeight());
            driver = new ChromeDriver(options);
        } else {
            driver = new ChromeDriver();
        }
        driver.get("https://www.airbnb.ca");
        driver.manage().window().maximize();
        driver.manage().deleteAllCookies();
    }

    @SuppressWarnings("ConstantConditions")
    @After
    public void tearDown() throws InterruptedException {
        boolean ObservationEnabled = false; // set to true to have a timeout before closing the browser, so we can observe what happened
        if (ObservationEnabled) {
            Thread.sleep(5000);
        }
        driver.quit();
    }

    @Test
    public void testSearch() throws InterruptedException {
        try {
            if (checkInMonthInput > checkOutMonthInput) {
                throw new IllegalArgumentException("Check-in month cannot be after check-out month");
            } else if (checkInMonthInput == checkOutMonthInput && checkInDayInput > checkOutDayInput) {
                throw new IllegalArgumentException("Check-in day cannot be after check-out day");
            }
            HomePage homePage = new HomePage(driver);
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.visibilityOfAllElements(homePage.locationInputButton));
            wait.until(ExpectedConditions.elementToBeClickable(homePage.locationInputButton));
            homePage.setLocationInput(locationInput);
            xpathDateInfo checkinInfo = xpathConstructor(yearInput, checkInMonthInput, checkInDayInput);
            String checkinXpath = checkinInfo.getXpath();
            int monthDifference = checkinInfo.getClicks();
            if (monthDifference > 1) {
                clickMultipleTimes(homePage.NextMonthButton, monthDifference - 1);
            }

            By checkInDateOptionLocator = By.xpath(checkinXpath);
            wait.until(ExpectedConditions.visibilityOfElementLocated(checkInDateOptionLocator));
            wait.until(ExpectedConditions.elementToBeClickable(checkInDateOptionLocator));
            WebElement checkinInput = driver.findElement(By.xpath(checkinXpath));
            checkinInput.click();
            xpathDateInfo checkoutInfo = xpathConstructor(yearInput, checkOutMonthInput, checkOutDayInput);
            String checkoutXpath = checkoutInfo.getXpath();
            int monthDifference2 = checkoutInfo.getClicks();
            clickMultipleTimes(homePage.NextMonthButton, monthDifference == 0 ? monthDifference2 - monthDifference - 1 : monthDifference2 - monthDifference);
            By checkOutDateOptionLocator = By.xpath(checkoutXpath);
            wait.until(ExpectedConditions.visibilityOfElementLocated(checkOutDateOptionLocator));
            wait.until(ExpectedConditions.elementToBeClickable(checkOutDateOptionLocator));
            WebElement checkoutInput = driver.findElement(By.xpath(checkoutXpath));
            checkoutInput.click();
            homePage.setGuests(adultsInput, childrenInput, infantsInput, petsInput);
            homePage.clickSearchButton();
            SearchResultsPage searchResultsPage = new SearchResultsPage(driver);
            wait.until(ExpectedConditions.textToBePresentInElement(searchResultsPage.result, "Search results"));
            String resultText = searchResultsPage.getResultText();
            assert (resultText.contains(expectedResult)) : "Test case failed: result doesn't contains required location\n" + resultText;
        }catch (Exception e){
            System.out.println("Test case failed: " + CurrentTestIndex);
            captureScreenshotAndError(driver, e);
            System.out.println(locationInput + " " + yearInput + " " + checkInMonthInput + " " + checkInDayInput + " " + checkOutMonthInput + " " + checkOutDayInput + " " + adultsInput + " " + childrenInput + " " + infantsInput + " " + petsInput);
            throw new TestFailedException(e.getMessage(), e);
        }
    }

    public static class TestFailedException extends RuntimeException {
        public TestFailedException() {
            super();
        }

        public TestFailedException(String message) {
            super(message);
        }

        public TestFailedException(String message, Throwable cause) {
            super(message, cause);
        }
    }
    private static void captureScreenshotAndError(WebDriver driver, Exception e) {
        // save screenshot to specific location
        File screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        Path savePath = Path.of("ErrorRecord", String.valueOf(CurrentTestIndex));
        Path screenshotsPath = savePath.resolve("failure.png");
        Path textPath = savePath.resolve("failure.txt");
        try {
            Files.createDirectories(screenshotsPath.getParent()); // 创建目录（如果不存在）
            Files.copy(screenshotFile.toPath(), screenshotsPath, StandardCopyOption.REPLACE_EXISTING);
            Files.writeString(textPath, "Test case failed: "+ e + "\n" + CurrentTestIndex + "\n" + locationInput + " " + yearInput + " " + checkInMonthInput + " " + checkInDayInput + " " + checkOutMonthInput + " " + checkOutDayInput + " " + adultsInput + " " + childrenInput + " " + infantsInput + " " + petsInput);
            System.out.println("Screenshot&text saved to: " + savePath.toAbsolutePath());
        } catch (IOException ee) {
            ee.printStackTrace();
        }
    }

    private xpathDateInfo xpathConstructor(int year, int month, int day) {
        xpathDateInfo xpathDateInfo = new xpathDateInfo();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d, EEEE, MMMM yyyy", Locale.ENGLISH);
        LocalDate date = LocalDate.of(year, month, day);
        String dateString = "";
        if (date.isBefore(TODAY)) {
            throw new IllegalArgumentException("Date cannot be before today");
        } else if (date.isEqual(TODAY)) {
            dateString = "Today";
            xpathDateInfo.setXpath("Today");
            xpathDateInfo.setClicks(0);
        } else {
            dateString = date.format(formatter);
            int monthDifference = calculateAbsoluteMonthDifference(TODAY, date);
            xpathDateInfo.setXpath("//td[contains(@aria-label,'" + dateString + "')]");
            xpathDateInfo.setClicks(monthDifference);
        }
        return xpathDateInfo;
    }


    public static int calculateAbsoluteMonthDifference(LocalDate date1, LocalDate date2) {
        YearMonth yearMonth1 = YearMonth.from(date1);
        YearMonth yearMonth2 = YearMonth.from(date2);

        // cal months between two dates
        return Math.abs((yearMonth2.getYear() - yearMonth1.getYear()) * 12
                + (yearMonth2.getMonthValue() - yearMonth1.getMonthValue()));
    }

    public static class HomePage {
        private final WebDriver driver;

        @FindBy(xpath = "//button[@data-index='0']")
        private WebElement locationInputButton;

        @FindBy(xpath = "//input[@id='bigsearch-query-location-input']")
        private WebElement locationInput;

        @FindBy(xpath = "//div[@data-testid='structured-search-input-field-guests-button']")
        private WebElement whoButton;

        @FindBy(xpath = "//button[@data-testid='stepper-adults-increase-button']")
        private WebElement adultsButton;

        @FindBy(xpath = "//button[@data-testid='stepper-children-increase-button']")
        private WebElement childrenButton;

        @FindBy(xpath = "//button[@data-testid='stepper-infants-increase-button']")
        private WebElement infantsButton;

        @FindBy(xpath = "//button[@data-testid='stepper-pets-increase-button']")
        private WebElement petsButton;

        @FindBy(xpath = "//button[@data-testid='structured-search-input-search-button']")
        private WebElement searchButton;

        @FindBy(xpath = "//button[@aria-label='Move forward to switch to the next month.']")
        private WebElement NextMonthButton;

        public HomePage(WebDriver driver) {
            this.driver = driver;
            PageFactory.initElements(driver, this);
        }

        public void setLocationInput(String location) {
            locationInputButton.click();
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.visibilityOf(locationInput));
            locationInput.sendKeys(location);
            locationInput.sendKeys(Keys.ENTER);
        }

        public void setGuests(int adults, int children, int infants, int pets) throws InterruptedException {
            whoButton.click();
            clickMultipleTimes(adultsButton, adults);
            clickMultipleTimes(childrenButton, children);
            clickMultipleTimes(infantsButton, infants);
            clickMultipleTimes(petsButton, pets);
        }

        public void clickSearchButton() {
            searchButton.click();
        }


    }

    public static void clickMultipleTimes(WebElement element, int times) throws InterruptedException {
        for (int i = 0; i < times; i++) {
            element.click();
            Thread.sleep(250);
        }
    }

    public static class xpathDateInfo {
        private String xpath;
        private int clicks;

        public xpathDateInfo(String xpath, int clicks) {
            this.xpath = xpath;
            this.clicks = clicks;
        }

        public xpathDateInfo() {

        }

        public void setXpath(String xpath) {
            this.xpath = xpath;
        }

        public void setClicks(int clicks) {
            this.clicks = clicks;
        }


        public String getXpath() {
            return xpath;
        }

        public int getClicks() {
            return clicks;
        }
    }

    public static class SearchResultsPage {
        private final WebDriver driver;

        @FindBy(xpath = "//h1[@elementtiming='LCP-target']/span")
        private WebElement result;

        public SearchResultsPage(WebDriver driver) {
            this.driver = driver;
            PageFactory.initElements(driver, this);
        }

        public String getResultText() {
            return result.getText();
        }
    }



}
