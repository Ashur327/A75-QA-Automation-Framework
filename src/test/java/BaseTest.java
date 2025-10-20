import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

public class BaseTest {

    protected WebDriver driver;
    protected String email;
    protected String password;

    @Parameters({"baseUrl", "email", "password", "cloudUserName", "cloudAccessKey", "browser"})
    @BeforeMethod
    public void setUp(String baseUrl, String email, String password,
                      String cloudUserName, String cloudAccessKey,
                      @Optional("chrome") String browser) throws MalformedURLException {

        this.email = email;
        this.password = password;

        driver = pickBrowser(browser, cloudUserName, cloudAccessKey);

        driver.manage().window().maximize();
        driver.get(baseUrl);
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    public WebDriver pickBrowser(String browser, String cloudUserName, String cloudAccessKey) throws MalformedURLException {
        switch (browser.toLowerCase()) {
            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                return new FirefoxDriver();

            case "edge":
                WebDriverManager.edgedriver().setup();
                return new EdgeDriver();

            case "cloud":
                return lambdaTest(cloudUserName, cloudAccessKey);

            case "chrome":
                WebDriverManager.chromedriver().setup();
                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.addArguments("--remote-allow-origins=*");
                return new ChromeDriver(chromeOptions);

            default:
                throw new IllegalArgumentException("Unknown browser: " + browser);
        }
    }

    public WebDriver lambdaTest(String cloudUserName, String cloudAccessKey) throws MalformedURLException {
        String hubURL = "https://hub.lambdatest.com/wd/hub";

        ChromeOptions browserOptions = new ChromeOptions();
        browserOptions.setPlatformName("Windows 10");
        browserOptions.setBrowserVersion("141.0");

        HashMap<String, Object> ltOptions = new HashMap<>();
        ltOptions.put("username", cloudUserName);
        ltOptions.put("accessKey", cloudAccessKey);
        ltOptions.put("project", "Untitled");
        ltOptions.put("selenium_version", "4.0.0");
        ltOptions.put("w3c", true);

        browserOptions.setCapability("LT:Options", ltOptions);

        return new RemoteWebDriver(new URL(hubURL), browserOptions);
    }
}
