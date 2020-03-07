package com.tstu.reviewdomparser.config;

import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

@Configuration
public class ApplicationConfig {

    @Bean
    public FirefoxOptions getDriverOptions() {
        //System.setProperty("webdriver.gecko.driver", System.getProperty("user.dir") + "path");
        //FirefoxDriverManager.firefoxdriver().setup();
        //FirefoxProfile profile = new FirefoxProfile(new File(findFile("firefox-profile")));
        FirefoxOptions options = new FirefoxOptions();
        options.setHeadless(true);
        //options.setHeadless(true);
        //options.setCapability("marionette", false);
        //options.setProfile(profile);
        return options;
    }

    @Bean
    @Primary
    public MutableCapabilities getBrowserCapabilities(FirefoxOptions firefoxOptions) {
        return new MutableCapabilities(firefoxOptions);
    }

    @Bean
    //@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.TARGET_CLASS)
    public WebDriver getWebDriver(MutableCapabilities mutableCapabilities, @Value("${services.selenium}") String driverUrl)  throws MalformedURLException {
        RemoteWebDriver webDriver = new RemoteWebDriver(new URL(driverUrl), mutableCapabilities);
        webDriver.manage().timeouts().pageLoadTimeout(100, TimeUnit.MINUTES);
        webDriver.manage().timeouts().setScriptTimeout(100, TimeUnit.MINUTES);
        webDriver.manage().timeouts().implicitlyWait(100, TimeUnit.MINUTES);
        //webDriver.manage().timeouts().implicitlyWait(1, TimeUnit.MINUTES);
        //webDriver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
        //webDriver.manage().timeouts().implicitlyWait()
        //driver.set(webDriver);
        /*String driverFiler = findFile("chromedriver");
        ChromeDriverService service = new ChromeDriverService.Builder()
                .usingDriverExecutable(new File(driverFiler))
                .build();
        ChromeOptions options = new ChromeOptions();
        options.setHeadless(true);*/
        return webDriver;
        //return new FirefoxDriver(firefoxOptions);
        //return new ChromeDriver(service, options);
    }

    private String findFile(String fileName) {
        ClassLoader classLoader = getClass().getClassLoader();
        URL url = classLoader.getResource(fileName);
        return url.getFile();
    }
}
