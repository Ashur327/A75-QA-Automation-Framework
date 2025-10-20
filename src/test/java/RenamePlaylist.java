import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.*;
import pages.HomePage;
import pages.LoginPage;

import java.time.Duration;

public class RenamePlaylist extends BaseTest {

    private LoginPage loginPage;
    private HomePage homePage;

    @BeforeMethod
    @Parameters({"baseUrl", "browser", "cloudUserName", "cloudAccessKey"})
    public void setupTest(String baseUrl,
                          @Optional("chrome") String browser,
                          @Optional("") String cloudUserName,
                          @Optional("") String cloudAccessKey) throws Exception {

        super.setup(browser, cloudUserName, cloudAccessKey);
        WebDriver driver = getDriver();

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        driver.get(baseUrl);

        loginPage = new LoginPage(driver);
        homePage = new HomePage(driver);

        // Hardcoded login
        loginPage.login("ashur.yonan@testpro.io", "BgAIWKi4DxG6");
    }

    @Test(groups = {"regression"})
    public void renamePlaylistTest() {
        String oldName = "fart";
        String newName = "fart fart fart";

        homePage.createPlaylistIfNotExists(oldName);
        homePage.renamePlaylist(oldName, newName);

        Assert.assertEquals(homePage.getSuccessBannerText(), "Updated playlist \"" + newName + "\"");
    }

    @AfterMethod
    public void teardownTest() {
        super.tearDown();
    }
}
