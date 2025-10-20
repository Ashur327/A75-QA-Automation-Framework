import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.*;
import pages.LoginPage;

import java.time.Duration;

public class LoginTests extends BaseTest {

    private LoginPage loginPage;

    @BeforeMethod
    @Parameters({"baseUrl", "browser", "cloudUserName", "cloudAccessKey"})
    public void setupTest(String baseUrl,
                          @Optional("chrome") String browser,
                          @Optional("") String cloudUserName,
                          @Optional("") String cloudAccessKey) throws Exception {

        // BaseTest's @BeforeMethod already runs automatically
        WebDriver driver = getDriver();

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.get(baseUrl);

        loginPage = new LoginPage(driver);
        Assert.assertEquals(driver.getCurrentUrl(), baseUrl);
    }

    @Test(groups = {"smoke"})
    @Parameters({"baseUrl"})
    public void validLoginTest(String baseUrl) {
        // Use credentials from BaseTest
        loginPage.login(email, password);
        Assert.assertEquals(getDriver().getCurrentUrl(), baseUrl + "#!/home");
    }

    @AfterMethod
    public void teardownTest() {
        // Cleanup handled by BaseTest
        super.tearDown();
    }
}
