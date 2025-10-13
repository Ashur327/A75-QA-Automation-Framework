import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.LoginPage;

import java.time.Duration;

public class RenamePlaylist {

    WebDriver driver;
    LoginPage loginPage;
    HomePage homePage;

    @BeforeMethod
    @Parameters("baseUrl")
    public void setup(String baseUrl) {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--disable-notifications");

        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        driver.get(baseUrl);

        loginPage = new LoginPage(driver);
        homePage = new HomePage(driver);

        // Login first
        loginPage.login("ashur.yonan@testpro.io", "eUZgLpQa");
    }

    @Test
    public void renamePlaylistTest() {
        String oldName = "fart";
        String newName = "fart fart fart";

        homePage.createPlaylistIfNotExists(oldName);
        homePage.renamePlaylist(oldName, newName);

        Assert.assertEquals(homePage.getSuccessBannerText(),
                "Updated playlist \"" + newName + "\"");
    }

    @AfterMethod
    public void teardown() {
        if (driver != null) driver.quit();
    }
}
