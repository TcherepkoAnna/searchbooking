import config.Config;
import logger.Log;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import selenium.DriverFactory;
import ui.fragments.NavigationBarHeader;
import ui.pages.booking.HomePage;

public class TestBase {

    protected static WebDriver driver = null;
    private Config config = new Config();

    @BeforeSuite
    public void initialize() {
        try {

            driver = DriverFactory.getInstance();
            driver.manage().window().maximize();

        } catch (
                WebDriverException exception) {
            Log.error("Exception occurred: " + exception.getMessage());
        }

        openHomepage();
        setLangAndCurrency();
    }

    private void openHomepage() {
        driver.get(config.getBookingUrl());
        HomePage homePage = new HomePage();
        homePage.render();
        Log.info("Page title: " + homePage.getTitle());
    }

    public void setLangAndCurrency() {
        NavigationBarHeader navBar = new NavigationBarHeader();
        navBar.setLanguage("English (US)");
        navBar.setCurrency("U.S. Dollar");
    }

    @AfterSuite
    public void teardown() {
        DriverFactory.closeService();
    }

}
