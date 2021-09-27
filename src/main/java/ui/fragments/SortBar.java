package ui.fragments;

import logger.Log;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import ui.elements.SelectorProxy;

import java.util.List;
import java.util.stream.Collectors;

import static utils.Utils.getUnique;
import static utils.Utils.normalize;

public class SortBar {

    private By sortBarItem = By.cssSelector("div[data-sort-bar-container='sort-bar'] > ul > li:not(li[data-id='dropdown'])");
    private SelectorProxy meatballs = new SelectorProxy(By.cssSelector("div[data-sort-bar-container='sort-bar'] li[data-id='dropdown']"));
    private SelectorProxy sortBar = new SelectorProxy(By.cssSelector("div[data-sort-bar-container='sort-bar']"));
    private SelectorProxy dropdown = new SelectorProxy(By.cssSelector("div[data-sort-bar-container='sort-bar'] li[data-id='dropdown'] ul[role='menu']"));


    public void setSorting(String sortBy) {
        List<Tab> found = getVisibleTabs();
        Log.info("Visible sorting options found:\n " + found);
        found = found.stream()
                .filter(t -> normalize(t.title).equals(normalize(sortBy)))
                .collect(Collectors.toList());
        if (found.size() == 1) {
            Log.info("Sorting option found in visible tabs -> selecting it ");
            found.get(0).getLink().click();
        } else if (found.size() == 0) {
            Log.info("Sorting option NOT found in visible tabs -> searching it in dropdown ");
            meatballs.click();
            Assert.assertTrue(dropdown.isDisplayed());
            found = sortBar.getWebElement().findElements(By.cssSelector("ul[role='menu'] > li"))
                    .stream().map(Tab::new)
                    .collect(Collectors.toList());
            Log.info("Dropdown list options found:\n " + found);
            getUnique(found.stream()
                    .filter(t -> normalize(t.title).equals(normalize(sortBy)))
                    .collect(Collectors.toList()))
                    .getLink().click();
        } else {
            throw new IllegalStateException("too many elements found: " + found.size());
        }

    }

    private List<Tab> getVisibleTabs() {
        return sortBar.getWebElement().findElements(sortBarItem)
                .stream().map(Tab::new)
                .filter(Tab::isVisibleInBar)
                .collect(Collectors.toList());
    }


    public class Tab {
        private WebElement we;
        private String title;


        public Tab(WebElement we) {
            this.we = we;
            this.title = getTitle();
        }

        private String getTitle() {
            return we.getText();
        }

        private boolean isVisibleInBar() {
            return getLink().getAttribute("tabindex").equals("0");
        }

        private WebElement getLink() {
            return getUnique(we.findElements(By.cssSelector("a[tabindex]")));
        }

        @Override
        public String toString() {
            return getTitle();
        }
    }
}
