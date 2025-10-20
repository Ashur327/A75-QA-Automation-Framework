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

        super.setup(browser, cloudUserName, cloudAccessKey);
        WebDriver driver = getDriver();

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.get(baseUrl);

        loginPage = new LoginPage(driver);

        // Hardcoded login credentials
        loginPage.login("ashur.yonan@testpro.io", "BgAIWKi4DxG6");
    }

    @Test(groups = {"smoke"})
    public void validLoginTest() {
        // After login, check home URL
        Assert.assertEquals(getDriver().getCurrentUrl(), "https://qa.koel.app/#!/home");
    }

    @AfterMethod
    public void teardownTest() {
        super.tearDown();
    }
}
