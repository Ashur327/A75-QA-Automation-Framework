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

public class DeletePlaylist {

    WebDriver driver;
    LoginPage loginPage;
    HomePage homePage;

    @BeforeMethod
    @Parameters("baseUrl")
    public void setup(String baseUrl) throws MalformedURLException {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-notifications");

        // connect to Selenium Grid
        driver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.get(baseUrl);

        loginPage = new LoginPage(driver);
        homePage = new HomePage(driver);

        loginPage.login("ashur.yonan@testpro.io", "eUZgLpQa");
    }

    @Test
    public void deletePlaylistTest() {
        String playlistName = "fart";

        homePage.deletePlaylist(playlistName);

        Assert.assertEquals(homePage.getSuccessBannerText(),
                "Deleted playlist \"" + playlistName + ".\"");
    }

    @AfterMethod
    public void teardown() {
        if (driver != null) driver.quit();
    }
}
