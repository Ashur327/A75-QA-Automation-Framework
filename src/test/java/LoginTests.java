import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import pages.LoginPage;

import java.time.Duration;

public class LoginTests {

    @Test
    @Parameters("baseUrl")
    public void validLoginTest(String baseUrl) {

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");

        WebDriver driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        driver.get(baseUrl);
        Assert.assertEquals(driver.getCurrentUrl(), baseUrl);

        // Use LoginPage POM to perform login
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login(); // uses the email/password defined in POM

        //assert successful login by checking URL
        Assert.assertEquals(driver.getCurrentUrl(), baseUrl + "#!/home");

        driver.quit();
    }
}
