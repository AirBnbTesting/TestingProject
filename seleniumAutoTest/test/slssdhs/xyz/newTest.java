package slssdhs.xyz;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.By;

public class newTest {
    public static void main(String[] args) {
        WebDriver driver = new ChromeDriver();
        driver.get("https://www.airbnb.ca");
        driver.manage().window().maximize();
        driver.manage().deleteAllCookies();
        WebElement Where = driver.findElement(By.xpath("//button[@data-index='1']"));
        Where.click();
        WebElement Month = driver.findElement(By.xpath("//button[@id='tab--tabs--1']"));
        Month.click();
        By locator = By.xpath("//span[contains(@style, 'top: 244.593px; left: 202.5px;')]");
        WebElement element = driver.findElement(locator);
        element.click();
    }
}
