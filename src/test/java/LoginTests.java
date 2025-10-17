import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.*;
import pages.LoginPage;

import java.net.MalformedURLException;
import java.time.Duration;

public class LoginTests extends BaseTest {

    private static final ThreadLocal<WebDriver> threadDriver = new ThreadLocal<>();
    LoginPage loginPage;

    public static WebDriver getDriver() {
        return threadDriver.get();
    }

    @BeforeMethod
    @Parameters({"baseUrl", "browser", "cloudUserName", "cloudAccessKey"})
    public void setup(String baseUrl, String browser, String cloudUserName, String cloudAccessKey) throws MalformedURLException {
        // Get driver from BaseTest (local, grid, or cloud)
        threadDriver.set(pickBrowser(browser, cloudUserName, cloudAccessKey));

        getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        getDriver().get(baseUrl);

        loginPage = new LoginPage(getDriver());
        Assert.assertEquals(getDriver().getCurrentUrl(), baseUrl);
    }

    @Test
    @Parameters({"email", "password", "baseUrl"})
    public void validLoginTest(String email, String password, String baseUrl) {
        loginPage.login(email, password);
        Assert.assertEquals(getDriver().getCurrentUrl(), baseUrl + "#!/home");
    }

    @AfterMethod
    public void teardown() {
        if (getDriver() != null) {
            getDriver().quit();
            threadDriver.remove();
        }
    }
}
