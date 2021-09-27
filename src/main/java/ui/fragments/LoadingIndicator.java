package ui.fragments;

import org.openqa.selenium.By;
import ui.elements.SelectorProxy;


public class LoadingIndicator {
    private SelectorProxy loadIndicator = new SelectorProxy(By.cssSelector("div.sr-usp-overlay"));

    public void waitLoading() {
        loadIndicator.waitHiddenOrElseThrow();
    }

    public void waitLoading(int time) {
        loadIndicator.waitHiddenOrElseThrow(time);
    }
}
