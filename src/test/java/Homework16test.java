import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.Duration;

public class Homework16test extends BaseTest {
    @Test
    public void registrationNavigation() {

        // Added ChromeOptions argument to fix websocket error
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");

        WebDriver driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        String baseUrl = "https://qa.koel.app/";
        driver.get(baseUrl);

        // find and click the registration link
        driver.findElement(By.linkText("Registration / Forgot password")).click();

        // verify redirect to the registration page
        Assert.assertTrue(
                driver.getCurrentUrl().endsWith("/registration"),
                "User was not redirected to the Registration page."
        );
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        driver.quit();
    }
}
