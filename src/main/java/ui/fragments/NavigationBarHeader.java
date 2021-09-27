package ui.fragments;

import logger.Log;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import ui.elements.SelectorProxy;
import ui.elements.WebElementProxy;

import java.util.List;
import java.util.stream.Collectors;

import static utils.Utils.normalize;

public class NavigationBarHeader {

    private By root = By.cssSelector("header nav.bui-header__bar");
    private SelectorProxy dialog = new SelectorProxy(By.cssSelector("div.bui-modal__slot[role='dialog']"));

    private SelectorProxy currencyButton = new SelectorProxy(By.cssSelector("button[data-modal-header-async-type='currencyDesktop']"));
    private SelectorProxy languageButton = new SelectorProxy(By.cssSelector("button[data-modal-id='language-selection']"));


    public void setLanguage(String optionToChoose) {
        languageButton.click();
        processModalOfOptions(optionToChoose);
    }

    public void setCurrency(String optionToChoose) {
        currencyButton.click();
        processModalOfOptions(optionToChoose);
    }

    private void processModalOfOptions(String optionToChoose) {
        new SelectorProxy(By.cssSelector("div.bui-modal__header")).waitDisplayedOrElseThrow();

        List<WebElement> found = dialog.getWebElement().findElements(By.cssSelector("a.bui-list-item"));
        Log.info("Looking for option [%s]", optionToChoose);

        found = found.stream()
                .filter(op -> normalize(op.getAttribute("innerText")).contains(normalize(optionToChoose)))
                .collect(Collectors.toList());
        if (found.size() == 0) {
            throw new IllegalStateException(String.format(" No option containing [%s] was found. ", optionToChoose));
        } else {
            new WebElementProxy(found.get(0)).scrollIntoView().click();
        }
        dialog.waitHiddenOrElseThrow(3);
    }


}
