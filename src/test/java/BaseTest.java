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

    // ThreadLocal driver for parallel-safe tests
    private static final ThreadLocal<WebDriver> threadDriver = new ThreadLocal<>();

    // Credentials
    protected String email;
    protected String password;

    /** Get the current thread's driver */
    protected static WebDriver getDriver() {
        return threadDriver.get();
    }

    /** Initialize the driver before each test */
    @Parameters({"email", "password", "browser", "cloudUserName", "cloudAccessKey"})
    @BeforeMethod(alwaysRun = true)
    public void setup(@Optional("") String email,
                      @Optional("") String password,
                      @Optional("chrome") String browser,
                      @Optional("") String cloudUserName,
                      @Optional("") String cloudAccessKey) throws MalformedURLException {

        this.email = email;
        this.password = password;

        WebDriver driver = pickBrowser(browser, cloudUserName, cloudAccessKey);
        threadDriver.set(driver);

        driver.manage().window().maximize();
    }

    /** Quit and remove driver after each test */
    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        WebDriver driver = getDriver();
        if (driver != null) {
            driver.quit();
            threadDriver.remove();
        }
    }

    /** Pick a browser or cloud driver */
    protected WebDriver pickBrowser(String browser, String cloudUserName, String cloudAccessKey) throws MalformedURLException {
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
                throw new IllegalArgumentException("Unsupported browser: " + browser);
        }
    }

    /** LambdaTest remote driver setup */
    protected WebDriver lambdaTest(String cloudUserName, String cloudAccessKey) throws MalformedURLException {
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
