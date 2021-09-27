package ui.fragments;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import selenium.DriverFactory;
import ui.elements.WebElementProxy;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static utils.Utils.getUnique;
import static utils.Utils.stripNonDigits;

public class DivTable {

    private By rowCtnr = By.cssSelector("div.sr_item, div[data-testid='property-card']");

    public DivTable() {
    }


    public List<DivTable.Row> getTableRows() {
        List<WebElement> rows = DriverFactory.getInstance().findElements(rowCtnr);
        return rows.stream().map(Row::new).collect(Collectors.toList());
    }


    public class Row {

        private WebElement we;
        By title = By.cssSelector("h3 span:nth-child(1), div[data-testid='title']");
        By price = By.cssSelector("div.bui-price-display__value, div[data-testid='price-and-discounted-price']");

        Row(WebElement we) {
            this.we = we;
        }

        public String getTitle() {
            return getUnique(we.findElements(title)).getText();
        }

        public String getPrice() {
            return stripNonDigits(getUnique(we.findElements(price)).getAttribute("innerText")).trim();
        }

        public String getCurrency() {
            return stripPrice(getUnique(we.findElements(price)).getAttribute("innerText")).trim();
        }

        private String stripPrice(String input) {
            StringBuilder result= new StringBuilder();
            Pattern p = Pattern.compile("([^0-9|,|.|\\s])+");
            Matcher m = p.matcher(input);
            while(m.find()) {
                result.append(m.group());
            }
            return result.toString();
        }

        public Row scrollIntoView() {
            new WebElementProxy(we).scrollIntoView();
            return this;
        }
    }
}
