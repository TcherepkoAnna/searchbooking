package ui.elements;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * Basic proxy for webElement.
 * Needs to add automatic wait and logging for actions.
 * Also protects from random element selection in case of multiple elements matched by selector
 */
public class SelectorProxy extends AbstractProxy {
    protected final By selector;
    protected final int defaultTimeoutMilliSec = 30 * 1000;
    protected final int defaultTimeoutSec = defaultTimeoutMilliSec / 1000;
    protected String name; // used into autologging. If null - autologging is off. If not provided -selector used as name.

    public SelectorProxy(By selector) {
        this(selector, selector.toString());
    }

    public SelectorProxy(By selector, String name) {
        this.selector = selector;
        this.name = name;
    }

    public WebElement getWebElement() {
        new WebDriverWait(driver(), defaultTimeoutSec).until(ExpectedConditions.presenceOfElementLocated(selector));

        List<WebElement> elements = driver().findElements(selector);
        if (elements.size() != 1) {
            throw new WebDriverException("Elements found by selector: " + elements.size() + ", Selector: " + selector.toString());
        }
        return elements.get(0);
    }

    public boolean isDisplayed() {
        try {
            return driver().findElement(selector).isDisplayed();
        } catch (NoSuchElementException e) {
            return false; // consider case when element not present as element not displayed
        }
    }

    public void waitDisplayedOrElseThrow() {
        waitDisplayedOrElseThrow(defaultTimeoutMilliSec, "element was not displayed: " + (name == null ? selector.toString() : name));
    }

    public void waitDisplayedOrElseThrow(int timeoutMilliSec, String errorMsg) {
        int timeoutSec = Math.max(1, timeoutMilliSec / 1000);
        try {
            new WebDriverWait(driver(), timeoutSec).until(ExpectedConditions.visibilityOfElementLocated(selector));
        } catch (TimeoutException | NoSuchElementException e) {
            throw new RuntimeException(errorMsg);
        }
    }

    public SelectorProxy waitDisplayed(int timeoutMilliSec) {
        int timeoutSec = Math.max(1, timeoutMilliSec / 1000);
        try {
            new WebDriverWait(driver(), timeoutSec).until(ExpectedConditions.visibilityOfElementLocated(selector));
        } catch (TimeoutException | NoSuchElementException e) {
            //NOP
        }
        return this;
    }

    public void waitHiddenOrElseThrow(int timeoutMilliSec, String errorMsg) {
        int timeoutSec = Math.max(1, timeoutMilliSec / 1000);
        try {
            new WebDriverWait(driver(), timeoutSec).until(ExpectedConditions.invisibilityOfElementLocated(selector));
        } catch (NoSuchElementException e) {
            //consider absent elements as hidden
        } catch (TimeoutException e) {
            throw new RuntimeException(errorMsg);
        }
    }

    public void waitHiddenOrElseThrow() {
        waitHiddenOrElseThrow(defaultTimeoutMilliSec);
    }

    public void waitHiddenOrElseThrow(int timeoutMilliSec) {
        waitHiddenOrElseThrow(timeoutMilliSec, "element still visible: " + (name == null ? selector.toString() : name));
    }

    public SelectorProxy clear() {
        log("clear");
        new WebDriverWait(driver(), defaultTimeoutSec).until(ExpectedConditions.elementToBeClickable(selector));
        getWebElement().clear();
        return this;
    }

    public SelectorProxy click() {
        log("click");
        new WebDriverWait(driver(), defaultTimeoutSec).until(ExpectedConditions.elementToBeClickable(selector));
        getWebElement().click();
        return this;
    }

    public SelectorProxy doubleClick() {
        log("double click");
        new WebDriverWait(driver(), defaultTimeoutSec).until(ExpectedConditions.elementToBeClickable(selector));
        new Actions(driver()).doubleClick(getWebElement()).perform();
        return this;
    }

    public SelectorProxy sendKeys(CharSequence... keysToSend) {
        log("send keys: " + Arrays.toString(keysToSend));
        new WebDriverWait(driver(), defaultTimeoutSec).until(ExpectedConditions.visibilityOfElementLocated(selector));
        getWebElement().sendKeys(keysToSend);
        return this;
    }

    public By getSelector() {
        return selector;
    }
}
