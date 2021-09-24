package ui.pages;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import selenium.DriverFactory;


public abstract class Page {
    protected final int defaultTimeoutSec = 30;

    public String getTitle() {
        return driver().getTitle();
    }

    public void refresh() {
        driver().navigate().refresh();
    }

    public Page render() {
        return this;
    }

    public void waitJsComplete(){
        waitJsComplete(defaultTimeoutSec);
    }

    public void waitJsComplete(int timeOutSec){
        ExpectedCondition<Boolean> pageLoadCondition = driver -> ((JavascriptExecutor)driver).executeScript("return document.readyState").equals("complete");
        WebDriverWait wait = new WebDriverWait(driver(), timeOutSec);
        wait.until(pageLoadCondition);
    }

    protected WebDriver driver(){
        return DriverFactory.getInstance();
    }
}
