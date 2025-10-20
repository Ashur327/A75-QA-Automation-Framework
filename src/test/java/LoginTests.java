import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.*;
import pages.LoginPage;

import java.time.Duration;

public class LoginTests extends BaseTest {

    private LoginPage loginPage;

    // Hardcode credentials to avoid parameter issues
    private final String email = "ashur.yonan@testpro.io";
    private final String password = "BgAIWKi4DxG6";

    @BeforeMethod
    @Parameters({"baseUrl", "browser", "cloudUserName", "cloudAccessKey"})
    public void setupTest(String baseUrl,
                          @Optional("chrome") String browser,
                          @Optional("") String cloudUserName,
                          @Optional("") String cloudAccessKey) throws Exception {

        // BaseTest sets up the driver
        super.setup(browser, cloudUserName, cloudAccessKey);

        WebDriver driver = getDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.get(baseUrl);

        loginPage = new LoginPage(driver);
        Assert.assertEquals(driver.getCurrentUrl(), baseUrl);
    }

    @Test(groups = {"smoke"})
    @Parameters({"baseUrl"})
    public void validLoginTest(String baseUrl) {
        WebDriver driver = getDriver();

        // Perform login
        loginPage.login(email, password);

        // Wait for URL to contain expected home path
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.urlContains("#!/home"));

        String currentUrl = driver.getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("#!/home"),
                "Expected URL to contain '#!/home', but got: " + currentUrl);
    }

    @AfterMethod
    public void teardownTest() {
        super.tearDown();
    }
}
