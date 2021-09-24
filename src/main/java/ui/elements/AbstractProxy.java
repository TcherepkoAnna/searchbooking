package ui.elements;

import logger.Log;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import selenium.DriverFactory;

import java.util.Arrays;

/**
 * <p>
 * Basic proxy for webElement.
 * Needs to add automatic wait and logging for actions.
 * Also protects from random element selection in case of multiple elements matched by selector
 */
public abstract class AbstractProxy {
    protected final int defaultTimeoutSec = 30;
    protected String name; // used into autologging. If null - autologging is off. If not provided -selector used as name.

    public abstract WebElement getWebElement();

    public boolean isDisplayed() {
        try {
            return getWebElement().isDisplayed();
        } catch (NoSuchElementException e) {
            return false; // consider case when element not present as element not displayed
        }
    }

    public abstract void waitDisplayedOrElseThrow();

    public abstract void waitDisplayedOrElseThrow(int timeoutSec, String errorMsg);

    public abstract void waitHiddenOrElseThrow(int timeoutSec, String errorMsg);

    public void waitHiddenOrElseThrow() {
        waitHiddenOrElseThrow(defaultTimeoutSec);
    }

    public abstract void waitHiddenOrElseThrow(int timeoutSec);

    public AbstractProxy clear() {
        log("clear");
        getWebElement().clear();
        return this;
    }

    public AbstractProxy click() {
        log("click");
        getWebElement().click();
        return this;
    }

    public AbstractProxy doubleClick() {
        log("double click");
        new Actions(driver()).doubleClick(getWebElement()).perform();
        return this;
    }

    public AbstractProxy sendKeys(CharSequence... keysToSend) {
        log("send keys: " + Arrays.toString(keysToSend));
        getWebElement().sendKeys(keysToSend);
        return this;
    }

    public AbstractProxy hover() {
        log("mouse over");
        new Actions(driver()).moveToElement(getWebElement()).perform();
        return this;
    }

    public AbstractProxy scrollCenterHorizontal() {
        ((JavascriptExecutor) DriverFactory.getInstance()).executeScript("arguments[0].scrollIntoView({inline: 'center'});", getWebElement());
        return this;
    }

    public AbstractProxy scrollCenterVertical() {
        ((JavascriptExecutor) DriverFactory.getInstance()).executeScript("arguments[0].scrollIntoView({block: 'center'});", getWebElement());
        return this;
    }

    public AbstractProxy scrollIntoView() {
        ((JavascriptExecutor) DriverFactory.getInstance()).executeScript("arguments[0].scrollIntoView(true);", getWebElement());
        return this;
    }

    public AbstractProxy scrollIntoView(String arg) {
        ((JavascriptExecutor) DriverFactory.getInstance()).executeScript(String.format("arguments[0].scrollIntoView(%s);", arg), getWebElement());
        return this;
    }


    /**
     * @return element text INCLUDING hidden/invisible parts
     */
    public String text() {
        return getWebElement().getAttribute("textContent");
    }

    /**
     * @return only visible text
     */
    public String visibleText() {
        return getWebElement().getText();
    }


    protected void log(String action) {
        if (name != null) {
            Log.info(">>[%s]>> %s", name, action);
        }
    }

    protected WebDriver driver() {
        return DriverFactory.getInstance();
    }
}
