import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.*;
import pages.HomePage;
import pages.LoginPage;

import java.time.Duration;

public class DeletePlaylist extends BaseTest {

    private LoginPage loginPage;
    private HomePage homePage;

    @BeforeMethod
    @Parameters({"baseUrl", "browser", "cloudUserName", "cloudAccessKey"})
    public void setup(String baseUrl,
                      @Optional("chrome") String browser,
                      @Optional("") String cloudUserName,
                      @Optional("") String cloudAccessKey) throws Exception {

        // Driver is already initialized in BaseTest's @BeforeMethod
        WebDriver driver = getDriver();

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.get(baseUrl);

        loginPage = new LoginPage(driver);
        homePage = new HomePage(driver);

        // Login using credentials stored in BaseTest
        loginPage.login(email, password);
    }

    @Test(groups = {"regression"})
    public void deletePlaylistTest() {
        String playlistName = "fart";

        homePage.deletePlaylist(playlistName);

        Assert.assertEquals(
                homePage.getSuccessBannerText(),
                "Deleted playlist \"" + playlistName + ".\""
        );
    }

    @AfterMethod
    public void teardownTest() {
        // Cleanup handled by BaseTest
        super.tearDown();
    }
}
