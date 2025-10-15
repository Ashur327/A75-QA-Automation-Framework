import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import pages.LoginPage;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

public class LoginTests {

    WebDriver driver;
    LoginPage loginPage;

    @BeforeMethod
    @Parameters({"baseUrl", "browser"})
    public void setup(String baseUrl, String browser) throws MalformedURLException {
        // Configure desired capabilities for the chosen browser
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setBrowserName(browser); // "chrome", "firefox", "edge", etc.

        // Connect to Selenium Grid Hub
        driver = new RemoteWebDriver(new URL("http://<hub-ip>:4444/wd/hub"), caps);

        // Optional implicit wait
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        // Open the application URL
        driver.get(baseUrl);

        // Initialize Page Object (Page Factory)
        loginPage = new LoginPage(driver);

        // Optional check that the page loaded correctly
        Assert.assertEquals(driver.getCurrentUrl(), baseUrl);
    }

    @Test
    @Parameters({"email", "password", "baseUrl"})
    public void validLoginTest(String email, String password, String baseUrl) {
        // Perform login using Page Factory POM
        loginPage.login(email, password);

        // Assert successful login
        Assert.assertEquals(driver.getCurrentUrl(), baseUrl + "#!/home");
    }

    @AfterMethod
    public void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
