import logger.Log;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import ui.fragments.DivTable;
import ui.fragments.LoadingIndicator;
import ui.fragments.NavigationBarHeader;
import ui.fragments.SortBar;
import ui.pages.booking.HomePage;
import ui.pages.booking.SearchResults;
import utils.DateUtils;

import java.util.List;
import java.util.stream.Collectors;

import static utils.Utils.normalize;

public class TestSearchResults extends TestBase {

    private DivTable table = new DivTable();
    private SortBar sortBar = new SortBar();
    private HomePage homePage = new HomePage();
    private NavigationBarHeader navBar = new NavigationBarHeader();


    @Test
    public void testSortingByPrice() {
        String sortingMode = "Price (lowest first)";

        // set search parameters
        homePage.setDestination("Odessa");
        homePage.setDateRange(
                DateUtils.parse("September 2021, 30", "MMMM yyyy, d"),
                DateUtils.parse("October 2021, 30", "MMMM yyyy, d"));
        // execute search
        homePage.searchButton.click();

        // check url and table of options is displayed
        Assert.assertTrue(driver.getCurrentUrl().startsWith(SearchResults.resourceUrl));
        List<DivTable.Row> results = table.getTableRows();
        Assert.assertTrue(results.size() != 0);
        Log.info("Results on page: " + results.size());

        //sort table by price
        sortBar.setSorting(sortingMode);
        new LoadingIndicator().waitLoading();
        results = table.getTableRows();
        List<Integer> prices = results.stream()
                .map(r -> Integer.parseInt(r.scrollIntoView().getPrice()))
                .collect(Collectors.toList());

        // verify tables is sorted
        Assert.assertEquals(prices.stream().sorted().collect(Collectors.toList()), prices);
    }


    @DataProvider(name = "currenciesData")
    public Object[][] currenciesData() {
        return new Object[][]{
                {"Danish Krone", "DKK"}};
    }

    @Test(dataProvider = "currenciesData", dependsOnMethods = {"testSortingByPrice"})
    public void testChangeCurrency(String expectedCurrSetting, String expectedCurrSymbol) {
        navBar.setCurrency(expectedCurrSetting);
        List<DivTable.Row> results = table.getTableRows();
        results.forEach(r -> Assert.assertEquals(normalize(r.scrollIntoView().getCurrency()), normalize(expectedCurrSymbol)));

    }

}
