import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import java.time.Duration;
import org.openqa.selenium.WebElement;
import java.util.List;
import org.openqa.selenium.Keys;

public class Homework20 {

    @Test
    @Parameters("baseUrl")
    public void deletePlaylist(String baseUrl){
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--disable-notifications");

        WebDriver driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.get(baseUrl);

        // input credentials
        driver.findElement(By.cssSelector("input[type='email']")).sendKeys("ashur.yonan@testpro.io");
        driver.findElement(By.cssSelector("input[type='password']")).sendKeys("eUZgLpQa");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        // check if playlist exists
        List<WebElement> fartLinks = driver.findElements(
                By.xpath("//li[contains(@class,'playlist')]//a[normalize-space()='fart']")
        );

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        if (fartLinks.isEmpty()) {
            // create playlist if it doesn't exist
            driver.findElement(By.cssSelector("i[title='Create a new playlist']")).click();
            driver.findElement(By.cssSelector("li[data-testid='playlist-context-menu-create-simple']")).click();
            driver.findElement(By.cssSelector("input[placeholder='↵ to save']"))
                    .sendKeys("fart", Keys.ENTER);

            // wait for the creation banner to disappear before deleting
            wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("div.success.show")));
        } else {
            // click it if it already existed
            fartLinks.get(0).click();
        }

        // delete the playlist
        driver.findElement(By.cssSelector("button[title='Delete this playlist']")).click();

        // wait specifically for the deletion banner text
        wait.until(ExpectedConditions.textToBePresentInElementLocated(
                By.cssSelector("div.success.show"),
                "Deleted playlist \"fart.\""
        ));
        WebElement successBanner = driver.findElement(By.cssSelector("div.success.show"));

        // assert the banner text
        Assert.assertEquals(successBanner.getText(), "Deleted playlist \"fart.\"");


        driver.quit();
    }
}
