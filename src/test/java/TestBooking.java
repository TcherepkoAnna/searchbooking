import config.Config;
import org.apache.log4j.Logger;
import org.testng.annotations.Test;
import ui.pages.booking.HomePage;

public class TestBooking extends TestBase {

    private Config config = new Config();

    @Test
    public void testSearch() {

        driver.get(config.getBookingUrl());
        HomePage homePage = new HomePage();
        homePage.render();
        System.out.println(homePage.getTitle());
    }

}
