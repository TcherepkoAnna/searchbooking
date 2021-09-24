package ui.pages.booking;

import logger.Log;
import org.openqa.selenium.By;
import ui.elements.SelectorProxy;
import ui.pages.Page;


public class HomePage extends Page {

    private final SelectorProxy destinationInput = new SelectorProxy(By.cssSelector("input.sb-destination__input"), null);


    @Override
    public HomePage render() {
        destinationInput.waitDisplayedOrElseThrow();
        Log.info("Home page loaded.");
        return this;
    }
}
