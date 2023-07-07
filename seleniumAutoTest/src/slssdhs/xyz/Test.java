package slssdhs.xyz;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class Test {
    public static void main(String[] args) throws InterruptedException {
        WebDriver driver = new ChromeDriver();
        driver.get("https://www.airbnb.ca");
        driver.manage().window().maximize();
        driver.manage().deleteAllCookies();
        Thread.sleep(2000);
        WebElement Where = driver.findElement(By.name("query"));
        if (Where != null) {
            System.out.println("Find!");
            Where.sendKeys("Ottawa");
        } else {
            System.out.println("Not Find!");
        }
        driver.quit();

    }
}
