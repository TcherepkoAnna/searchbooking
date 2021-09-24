import config.Config;
import logger.Log;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import selenium.DriverFactory;

import java.util.concurrent.TimeUnit;

public class TestBase {

    protected static WebDriver driver = null;
    private Config config = new Config();

    @BeforeSuite
    public void initialize() {
        try {
//            switch (config.getPreferredBrowser()) {
//                case "firefox":
//                    FirefoxOptions options = new FirefoxOptions();
//                    driver = new FirefoxDriver(options);
//                    break;
//                default:
//                    // chrome
//                    driver = new ChromeDriver();
//            }
            driver = DriverFactory.getInstance();

            driver.manage().window().maximize();
            driver.manage().timeouts().implicitlyWait(config.getWaitTime(), TimeUnit.SECONDS);

        } catch (
                WebDriverException exception) {
            Log.error("Exception occurred: " + exception.getMessage());
        }

    }

    @AfterSuite
    public void teardown() {
        DriverFactory.closeService();
//        driver.quit();
    }

}
