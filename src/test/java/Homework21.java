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
import org.openqa.selenium.interactions.Actions;

public class Homework21 {

    @Test
    @Parameters("baseUrl")
    public void renamePlaylist(String baseUrl){
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--disable-notifications");

        WebDriver driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
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

            // wait for the creation banner to disappear before moving on
            wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("div.success.show")));
        }

        // re-fetch it now that it exists
        fartLinks = driver.findElements(
                By.xpath("//li[contains(@class,'playlist')]//a[normalize-space()='fart']")
        );

        // select the playlist
        WebElement playlist = fartLinks.get(0); // first match

        Actions actions = new Actions(driver);

        // Right-click the playlist
        actions.contextClick(playlist).perform();

        // Wait for pop up to appear, then click edit
        WebDriverWait wait2 = new WebDriverWait(driver, Duration.ofSeconds(5));
        WebElement editOption = wait2.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//li[contains(@data-testid,'playlist-context-menu-edit')]")
        ));
        editOption.click();


        // Wait for the input to be visible and enabled
        WebElement nameInput = wait.until(driver2 -> {
            WebElement el = driver2.findElement(By.xpath("//input[@data-testid='inline-playlist-name-input']"));
            return (el.isDisplayed() && el.isEnabled()) ? el : null;
        });

        // Use Actions to clear, then type into the input
        nameInput.click();
        nameInput.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        nameInput.sendKeys(Keys.BACK_SPACE);
        Actions typeAction = new Actions(driver);
        typeAction.moveToElement(nameInput).click().sendKeys("fart fart fart").sendKeys(Keys.ENTER).perform();

        // wait specifically for the confirmation banner
        wait.until(ExpectedConditions.textToBePresentInElementLocated(
                By.cssSelector("div.success.show"),
                "Updated playlist \"fart fart fart.\""
        ));

        WebElement successBanner = driver.findElement(By.cssSelector("div.success.show"));

        // assert the success banner text matches
        Assert.assertEquals(successBanner.getText(),
                "Updated playlist \"fart fart fart.\""
        );

        driver.quit();
    }
}
