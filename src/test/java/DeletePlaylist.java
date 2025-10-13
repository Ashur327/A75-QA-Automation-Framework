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

public class DeletePlaylist {

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
