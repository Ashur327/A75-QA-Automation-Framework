import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.*;
import pages.HomePage;
import pages.LoginPage;
import java.net.MalformedURLException;
import java.time.Duration;

public class RenamePlaylist extends BaseTest {

    private static final ThreadLocal<WebDriver> threadDriver = new ThreadLocal<>();
    LoginPage loginPage;
    HomePage homePage;

    public static WebDriver getDriver() {
        return threadDriver.get();
    }

    @BeforeMethod
    @Parameters({"baseUrl", "browser", "cloudUserName", "cloudAccessKey"})
    public void setup(String baseUrl, String browser, String cloudUserName, String cloudAccessKey) throws MalformedURLException {
        // Initialize driver using BaseTest logic (local, grid, or cloud)
        threadDriver.set(pickBrowser(browser, cloudUserName, cloudAccessKey));

        getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        getDriver().get(baseUrl);

        // Initialize Page Objects with ThreadLocal driver
        loginPage = new LoginPage(getDriver());
        homePage = new HomePage(getDriver());

        // Login using BaseTest credentials
        loginPage.login(email, password);
    }

    // ✅ Add group for Jenkins smoke/regression control
    @Test(groups = {"regression"})
    public void renamePlaylistTest() {
        String oldName = "fart";
        String newName = "fart fart fart";

        homePage.createPlaylistIfNotExists(oldName);
        homePage.renamePlaylist(oldName, newName);

        Assert.assertEquals(
                homePage.getSuccessBannerText(),
                "Updated playlist \"" + newName + "\""
        );
    }

    @AfterMethod
    public void teardown() {
        if (getDriver() != null) {
            getDriver().quit();
            threadDriver.remove();
        }
    }
}
