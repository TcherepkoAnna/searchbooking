package ui.fragments;

import logger.Log;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import selenium.DriverFactory;
import ui.elements.SelectorProxy;
import utils.DateUtils;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;

import static utils.Utils.getUnique;

public class DatePicker {

    private static SelectorProxy root = new SelectorProxy(By.cssSelector(".bui-calendar"));
    private SelectorProxy shevronPrev = new SelectorProxy(By.cssSelector(".bui-calendar__control--prev"));
    private SelectorProxy shevronNext = new SelectorProxy(By.cssSelector(".bui-calendar__control--next"));

    private WebElement we;

    private DatePicker(WebElement we) {
        this.we = we;
    }

    public static DatePicker find() {
        if (isDisplayed()) {
            return new DatePicker(root.getWebElement());
        } else throw new IllegalStateException("no elements found");
    }

    public static boolean isDisplayed() {
        boolean state = root.isDisplayed();
        Log.info("datePicker is " + (state ? "" : "NOT") + " open");
        return state;
    }


    public void setDate(LocalDate dateToSet) {
        Log.info("Selecting date: [%s]", dateToSet);
        PickerMonthTable month = getPickerMonthTable(dateToSet);
        month.setDay(dateToSet.getDayOfMonth());

    }

    private PickerMonthTable getPickerMonthTable(LocalDate dateToSet) {
        YearMonth ymDesired = YearMonth.from(dateToSet);
        Log.info("Looking for desired month-year table: [%s]", ymDesired);

        //get current 2 tables
        List<PickerMonthTable> all = getPickerMonthTables();

        //if desired month is not here
        while (!(all.get(0).yearMonth.equals(ymDesired) || all.get(1).yearMonth.equals(ymDesired))) {
            // move calendar forward or back
            if (ymDesired.isAfter(all.get(1).yearMonth)) {
                if (!shevronNext.isDisplayed()) {
                    throw new IllegalArgumentException(String.format(" Required date [%s] is not possible. Latest available month-year: " + all.get(1).yearMonth));
                }
                Log.info("Moving forward in datepicker calendar");
                shevronNext.click();
                if (shevronNext.isDisplayed()) shevronNext.click();
            } else {
                if (!shevronPrev.isDisplayed()) {
                    throw new IllegalArgumentException(String.format(" Required date [%s] is not possible. Earliest available month-year: [%s] ", dateToSet, all.get(0).yearMonth));
                }
                Log.info("Moving back in datepicker calendar");
                shevronPrev.click();
                if (shevronPrev.isDisplayed()) shevronPrev.click();
            }
            all = getPickerMonthTables();
        }
        //select desired month table
        return all.get(0).yearMonth.equals(ymDesired) ? all.get(0) : all.get(1);

    }


    private List<PickerMonthTable> getPickerMonthTables() {
        By selector = By.cssSelector(".bui-calendar__wrapper");
        List<WebElement> elements = DriverFactory.getInstance().findElements(selector);
        return elements.stream().map(PickerMonthTable::new).collect(Collectors.toList());
    }

    private class PickerMonthTable {
        final YearMonth yearMonth;
        final WebElement we;

        PickerMonthTable(WebElement we) {
            this.we = we;
            this.yearMonth = headerDate();
        }

        public String header() {
            By header = By.cssSelector(".bui-calendar__month");
            return we.findElement(header).getText();
        }

        public YearMonth headerDate() {
            return DateUtils.parseYearMonth(header(), "MMMM yyyy");
        }

        public void setDay(int dayToSet) {
            By table = By.cssSelector("table.bui-calendar__dates");
            PickerDay checkBox = getUnique(we.findElement(table)
                    .findElements(By.cssSelector("tbody td[role='gridcell']"))
                    .stream().map(PickerDay::new)
                    .filter(d -> d.day == dayToSet)
                    .collect(Collectors.toList()));
            checkBox.click();
        }

    }

    class PickerDay {
        final WebElement we;
        final int day;
        final String fullDate;

        public PickerDay(WebElement webElement) {
            this.we = webElement;
            this.day = getDayOfMonth();
            this.fullDate = getFullDate();
        }

        private String getFullDate() {
            return we.getAttribute("data-date");
        }

        private int getDayOfMonth() {
            return Integer.parseInt(we.getText());
        }

        public void click() {
            By dayCheckbox = By.cssSelector("span[role='checkbox']");
            Log.info("Clicking cell [%s] ", day);
            we.findElement(dayCheckbox).click();
        }
    }


}
