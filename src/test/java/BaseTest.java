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

    private static final ThreadLocal<WebDriver> threadDriver = new ThreadLocal<>();

    public static WebDriver getDriver() {
        return threadDriver.get();
    }

    @Parameters({"browser", "cloudUserName", "cloudAccessKey"})
    @BeforeMethod(alwaysRun = true)
    public void setup(@Optional("chrome") String browser,
                      @Optional("") String cloudUserName,
                      @Optional("") String cloudAccessKey) throws MalformedURLException {

        WebDriver driver = pickBrowser(browser, cloudUserName, cloudAccessKey);
        threadDriver.set(driver);
        driver.manage().window().maximize();
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        WebDriver driver = getDriver();
        if (driver != null) {
            driver.quit();
            threadDriver.remove();
        }
    }

    private WebDriver pickBrowser(String browser, String cloudUserName, String cloudAccessKey) throws MalformedURLException {
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
            default:
                WebDriverManager.chromedriver().setup();
                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.addArguments("--remote-allow-origins=*");
                return new ChromeDriver(chromeOptions);
        }
    }

    private WebDriver lambdaTest(String cloudUserName, String cloudAccessKey) throws MalformedURLException {
        String hubURL = "https://hub.lambdatest.com/wd/hub";

        ChromeOptions browserOptions = new ChromeOptions();
        browserOptions.setPlatformName("Windows 10");
        browserOptions.setBrowserVersion("141.0");

        HashMap<String, Object> ltOptions = new HashMap<>();
        ltOptions.put("username", cloudUserName);
        ltOptions.put("accessKey", cloudAccessKey);
        ltOptions.put("project", "QA Automation");
        ltOptions.put("selenium_version", "4.0.0");
        ltOptions.put("w3c", true);

        browserOptions.setCapability("LT:Options", ltOptions);

        return new RemoteWebDriver(new URL(hubURL), browserOptions);
    }
}
