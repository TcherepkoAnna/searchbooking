package selenium;

import config.Config;
import logger.Log;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriverLogLevel;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.GeckoDriverService;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.service.DriverService;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;


public class DriverFactory {
    private static WebDriver instance;
    private static DriverService service;
    private static Config config = new Config();


    public static synchronized WebDriver getInstance() {
        if (instance == null) {
            initDriver();
        }
        return instance;
    }


    public static synchronized void closeDriver() {
        try {
            if (instance != null) {
                instance.quit();
                instance = null;
            }
        } catch (Throwable t) {
            instance = null;
        }
    }

    public static synchronized void closeService() {
        closeDriver();
        try {
            if (service != null) {
                service.stop();
                service = null;
            }
        } catch (Throwable t) {
            service = null;
        }
    }

    private static void initService() {
        String webdriverPathChrome;
        String webdriverPathFirefox;
        String os = System.getProperty("os.name");
        Log.info(">>>SYSTEM_TYPE: %s <<<", os);
        if (os.toLowerCase().startsWith("windows")) {
            webdriverPathChrome = config.webdriverChromePathWin();
            webdriverPathFirefox = config.webdriverFirefoxPathWin();
        } else {
            webdriverPathChrome = config.webdriverChromePathLinux();
            webdriverPathFirefox = config.webdriverFirefoxPathLinux();

        }

        switch (config.getPreferredBrowser()) {
            case "chrome":
                service = new ChromeDriverService.Builder()
                        .usingDriverExecutable(new File(webdriverPathChrome))
                        .usingAnyFreePort()
                        .build();
                try {
                    service.start();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                break;
            case "firefox":
                service = new GeckoDriverService.Builder()
                        .usingDriverExecutable(new File(webdriverPathFirefox))
                        .usingAnyFreePort()
                        .build();
                try {
                    service.start();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                break;
            default:
                throw new IllegalStateException("driver not supported: " + config.getPreferredBrowser());
        }
    }

    private static void initDriver() {
        if (service == null) {
            initService();
        }
        switch (config.getPreferredBrowser()) {
            case "chrome":
                instance = getChromeDriver();
                break;
            case "firefox":
                instance = getFirefoxDriver();
                break;
            default:
                throw new IllegalStateException("driver not supported: " + config.getPreferredBrowser());
        }
        Log.debug("\n>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>\n" +
                        ">>Browser type: %s\n" +
                        ">>Browser resolution: %s\n" +
                        ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>",
                config.getPreferredBrowser(),
                instance.manage().window().getSize()
        );
    }

    private static WebDriver getChromeDriver() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("test-type"); //ignore cert errors
        options.addArguments("--disable-popup-blocking");
        options.addArguments("--dns-prefetch-disable");
        options.addArguments("--no-sandbox");
        options.addArguments("window-size=1920,1080");
        return new RemoteWebDriver(service.getUrl(), options);
    }

    private static WebDriver getFirefoxDriver() {
        FirefoxOptions firefoxOptions = new FirefoxOptions();
        firefoxOptions.setCapability("acceptInsecureCerts", true); //ignore cert errors
        firefoxOptions.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
        firefoxOptions.setLogLevel(FirefoxDriverLogLevel.fromLevel(Level.OFF));
        WebDriver firefoxDriver = new RemoteWebDriver(service.getUrl(), firefoxOptions);
        firefoxDriver.manage().window().setSize(new Dimension(1920, 1080));
        return firefoxDriver;
    }
}
