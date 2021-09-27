package ui.pages.booking;

import logger.Log;
import org.openqa.selenium.By;
import ui.elements.SelectorProxy;
import ui.fragments.DatePicker;
import ui.pages.Page;

import java.time.LocalDate;


public class HomePage extends Page {

    private final SelectorProxy destinationInput = new SelectorProxy(By.cssSelector("input.sb-destination__input"));
    public final SelectorProxy searchButton = new SelectorProxy(By.cssSelector(".sb-searchbox__button"));
    public final SelectorProxy daterangeField = new SelectorProxy(By.cssSelector(".xp__dates"));

    @Override
    public HomePage render() {
        destinationInput.waitDisplayedOrElseThrow();
        Log.info("Home page loaded.");
        return this;
    }

    public void setDestination(String destination) {
        destinationInput.clear().sendKeys(destination);
    }


    public void setDateRange(LocalDate fromDate, LocalDate toDate) {
        DatePicker datePicker = openDatepicker();
        datePicker.setDate(fromDate);
        datePicker.setDate(toDate);
    }

    private DatePicker openDatepicker() {
        if (!DatePicker.isDisplayed()) {
            Log.info("clicking DateRange field");
            daterangeField.click();
        }
        return DatePicker.find();
    }

    public String getCheckInFieldValue() {

        String actual = daterangeField.getWebElement().findElement(
                By.cssSelector("div[data-mode='checkin'] div.sb-date-field__display"))
                .getText();
        Log.info("Actual CheckInFieldValue: [%s] ", actual);
        return actual.trim();
    }


    public String getCheckOutFieldValue() {

        String actual = daterangeField.getWebElement().findElement(
                By.cssSelector("div[data-mode='checkout'] div.sb-date-field__display"))
                .getText();
        Log.info("Actual CheckInFieldValue: [%s] ", actual);
        return actual.trim();
    }
}
