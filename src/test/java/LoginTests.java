import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.*;
import pages.LoginPage;

import java.net.MalformedURLException;
import java.time.Duration;

public class LoginTests extends BaseTest {

    LoginPage loginPage;

    @BeforeMethod
    @Parameters({"baseUrl", "browser", "cloudUserName", "cloudAccessKey"})
    public void setup(String baseUrl, String browser, String cloudUserName, String cloudAccessKey) throws MalformedURLException {
        // Initialize ThreadLocal driver via BaseTest
        initDriver(browser, cloudUserName, cloudAccessKey);

        getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        getDriver().get(baseUrl);

        loginPage = new LoginPage(getDriver());
        Assert.assertEquals(getDriver().getCurrentUrl(), baseUrl);
    }

    @Test(groups = {"smoke"})
    @Parameters({"email", "password", "baseUrl"})
    public void validLoginTest(String email, String password, String baseUrl) {
        loginPage.login(email, password);
        Assert.assertEquals(getDriver().getCurrentUrl(), baseUrl + "#!/home");
    }

    @AfterMethod
    public void teardown() {
        // Handled by BaseTest
        super.tearDown();
    }
}
