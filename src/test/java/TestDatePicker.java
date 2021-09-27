import logger.Log;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import ui.pages.booking.HomePage;
import utils.DateUtils;

import java.time.LocalDate;

public class TestDatePicker extends TestBase {

    private HomePage homePage = new HomePage();

    private String format_MMMM_yyyy_d = "MMMM yyyy, d";
    private String format_E_MMM_d = "E, MMM d";


    @DataProvider(name = "date-range")
    public Object[][] dateRange() {
        return new Object[][]{
                {"October 2021, 30", "November 2021, 30"},
                {"December 2021, 30", "January 2022, 1"},
                {"December 2021, 31", "February 2022, 2"},
                {"March 2022, 3", "August 2022, 9"},
                {"August 2022, 30", "September 2022, 30"}};
    }

    @Test(dataProvider = "date-range")
    public void testSettingPickerValues(String desiredFrom, String desiredTo) {

        Log.info("Setting DateRange: [%s - %s] ", desiredFrom, desiredTo);
        homePage.setDateRange(DateUtils.parse(desiredFrom, format_MMMM_yyyy_d), DateUtils.parse(desiredTo, format_MMMM_yyyy_d));

        Log.info("Check DateRange values were set correctly ");
        String expectedFrom = DateUtils.convertToformat(desiredFrom, format_MMMM_yyyy_d, format_E_MMM_d);
        String expectedTo = DateUtils.convertToformat(desiredTo, format_MMMM_yyyy_d, format_E_MMM_d);
//      get Actual Values
        String actualFrom = homePage.getCheckInFieldValue().replace("Sept", "Sep");
        String actualTo = homePage.getCheckOutFieldValue().replace("Sept", "Sep");
//        compare
        Assert.assertEquals(actualFrom, expectedFrom, String.format(" Actual FROM values is [%s] ", actualFrom));
        Assert.assertEquals(actualTo, expectedTo, String.format(" Actual TO values is [%s] ", expectedTo));
    }


    @Test
    public void testEarlierDateNotSet() {

        openHomepage();
        LocalDate today = LocalDate.now();
        LocalDate desiredFromYesterday = today.getDayOfYear() != 1 ? LocalDate.ofYearDay(today.getYear(), today.getDayOfYear() - 1)
                : LocalDate.of(today.getYear() - 1, 12, 31);

        Log.info("Setting earlier date: [%s], today [%s] ", desiredFromYesterday, today);
        homePage.setDate(desiredFromYesterday);
        String actualFrom = homePage.getCheckInFieldValue();
        Assert.assertEquals(actualFrom, "Check-in", String.format(" Actual FROM values is [%s] ", actualFrom));

    }

}
