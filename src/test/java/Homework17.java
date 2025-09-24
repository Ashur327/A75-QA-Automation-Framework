import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.Duration;

public class Homework17 extends BaseTest {
    @Test
    public void addSongToPlaylist() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--disable-notifications");

        WebDriver driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // go to URL
        driver.get("https://qa.koel.app/");

        // log in
        driver.findElement(By.cssSelector("input[type='email']")).sendKeys("ashur.yonan@testpro.io");
        driver.findElement(By.cssSelector("input[type='password']")).sendKeys("eUZgLpQa");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        // search for song
        WebElement searchBox = wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector("input[type='search']")));
        searchBox.sendKeys("midnight");
        searchBox.sendKeys(Keys.ENTER);

        // click "View All"
        driver.findElement(By.cssSelector("button[data-test='view-all-songs-btn']")).click();

        // right-click the song row
        WebElement firstResult = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//tr[contains(@class,'song-item')]/td[text()='Midnight in Mississippi']/..")));
        Actions actions = new Actions(driver);
        actions.contextClick(firstResult).perform();

        // hover over "Add To"
        WebElement addToParent = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//li[contains(@class,'has-sub') and contains(.,'Add To')]")));
        actions.moveToElement(addToParent).perform();

        // click playlist "fart" via JavaScript, since it's inaccessible otherwise
        String js = "let parent = arguments[0];" +
                "let items = parent.querySelectorAll('ul li');" +
                "items.forEach(i => { if(i.innerText.trim() === 'fart'){ i.click(); }});";
        ((JavascriptExecutor) driver).executeScript(js, addToParent);

        // wait for success message
        WebElement successMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("div.success.show")));
        String actualMessage = successMessage.getText();
        String expectedMessage = "Added 1 song into \"fart.\"";

        Assert.assertEquals(actualMessage, expectedMessage);

        driver.quit();
    }
}
