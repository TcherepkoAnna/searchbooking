package ui.elements;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;


public class WebElementProxy extends AbstractProxy {
    private WebElement webElement;

    public WebElementProxy(WebElement webElement) {
        this.webElement = webElement;
        this.name = webElement.getTagName();
    }

    public WebElementProxy(WebElement webElement, String name) {
        this.webElement = webElement;
        this.name = name;
    }

    @Override
    public WebElement getWebElement() {
        return webElement;
    }

    public void waitDisplayedOrElseThrow() {
        waitDisplayedOrElseThrow(defaultTimeoutSec, "element was not displayed: " + (name == null ? webElement.getTagName() : name));
    }

    public void waitDisplayedOrElseThrow(int timeoutSec, String errorMsg) {
        try {
            new WebDriverWait(driver(), timeoutSec).until(ExpectedConditions.visibilityOf(webElement));
        } catch (TimeoutException | NoSuchElementException e) {
            throw new RuntimeException(errorMsg);
        }
    }

    public void waitHiddenOrElseThrow(int timeoutSec, String errorMsg) {
        try {
            new WebDriverWait(driver(), timeoutSec).until(ExpectedConditions.invisibilityOf(webElement));
        } catch (NoSuchElementException e) {
            //consider absent elements as hidden
        } catch (TimeoutException e) {
            throw new RuntimeException(errorMsg);
        }
    }

    public void waitHiddenOrElseThrow(int timeoutSec) {
        waitHiddenOrElseThrow(timeoutSec, "element still visible: " + (name == null ? webElement.getTagName() : name));
    }
}
