package config;

import logger.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Properties;

public class Config {

    private Properties properties = new Properties();
    private static final String propertiePath = "src\\main\\resources\\config.properties";


    private static final String URL_BOOKING = "url_booking";
    private static final String WEBDRIVER_CHROME_PATH_WIN = "webdriver.path.chrome.win";
    private static final String WEBDRIVER_FIREFOX_PATH_WIN = "webdriver.path.firefox.win";
    private static final String WEBDRIVER_CHROME_PATH_LINUX = "webdriver.path.chrome.linux";
    private static final String WEBDRIVER_FIREFOX_PATH_LINUX = "webdriver.path.firefox.linux";
    private static final String PREFERRED_BROWSER = "preferred_browser";


    public Config() {

        try (FileInputStream input = new FileInputStream(new File(propertiePath));
             InputStreamReader reader = new InputStreamReader(input, Charset.forName("UTF-8"))) {
            // load a properties file
            properties.load(reader);

        } catch (IOException ex) {
            Log.error(ex.getMessage());
        }
    }


    public String getBookingUrl() {
        return properties.getProperty(URL_BOOKING);
    }

    public String getPreferredBrowser() {
        return properties.getProperty(PREFERRED_BROWSER);
    }

    public String webdriverChromePathWin() {
        return properties.getProperty(WEBDRIVER_CHROME_PATH_WIN);
    }

    public String webdriverFirefoxPathWin() {
        return properties.getProperty(WEBDRIVER_FIREFOX_PATH_WIN);
    }

    public String webdriverChromePathLinux() {
        return properties.getProperty(WEBDRIVER_CHROME_PATH_LINUX);
    }

    public String webdriverFirefoxPathLinux() {
        return properties.getProperty(WEBDRIVER_FIREFOX_PATH_LINUX);
    }
}
