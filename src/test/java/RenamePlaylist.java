import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;
import org.testng.annotations.*;
import pages.HomePage;
import pages.LoginPage;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

public class RenamePlaylist {

    WebDriver driver;
    LoginPage loginPage;
    HomePage homePage;

    @BeforeMethod
    @Parameters("baseUrl")
    public void setup(String baseUrl) throws MalformedURLException {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-notifications");

        // connect to Grid hub
        driver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        driver.get(baseUrl);

        loginPage = new LoginPage(driver);
        homePage = new HomePage(driver);

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
