import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.*;
import pages.HomePage;
import pages.LoginPage;

import java.net.MalformedURLException;
import java.time.Duration;

public class DeletePlaylist extends BaseTest {

    LoginPage loginPage;
    HomePage homePage;

    @BeforeMethod
    @Parameters({"baseUrl", "browser", "cloudUserName", "cloudAccessKey"})
    public void setup(String baseUrl, String browser, String cloudUserName, String cloudAccessKey) throws MalformedURLException {
        // Initialize ThreadLocal driver via BaseTest
        initDriver(browser, cloudUserName, cloudAccessKey);

        getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        getDriver().get(baseUrl);

        loginPage = new LoginPage(getDriver());
        homePage = new HomePage(getDriver());

        // Login using BaseTest credentials
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
    public void teardown() {
        // Handled by BaseTest
        super.tearDown();
    }
}
