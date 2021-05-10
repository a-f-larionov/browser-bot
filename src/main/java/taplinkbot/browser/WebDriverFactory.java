package taplinkbot.browser;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebDriverFactory {

    private final Environment env;

    public ChromeDriver buildWebDriver() {

        System.setProperty("webdriver.chrome.driver", Objects.requireNonNull(env.getProperty("webdriver.path")));

        log.info(env.getProperty("webdriver.path"));
        log.info(env.getProperty("webdriver.path"));
        log.info(env.getProperty("webdriver.path"));
        log.info(env.getProperty("webdriver.path"));
        log.info(env.getProperty("webdriver.path"));

        ChromeOptions options = new ChromeOptions();

        if (!env.getProperty("webdriver.guiEnabled").equals("true")) {
            options.addArguments("--headless");
            options.addArguments("--disable-gpu");
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
        }


        if (false) {
            //@todo need to check it
            // options to prevent TIMEOUTS
            options.addArguments("start-maximized"); //https://stackoverflow.com/a/26283818/1689770
            options.addArguments("enable-automation"); //https://stackoverflow.com/a/43840128/1689770
            //options.addArguments("--no-sandbox"); //https://stackoverflow.com/a/50725918/1689770
            options.addArguments("--disable-infobars"); //https://stackoverflow.com/a/43840128/1689770
            options.addArguments("--disable-dev-shm-usage"); //https://stackoverflow.com/a/50725918/1689770
            options.addArguments("--disable-browser-side-navigation"); //https://stackoverflow.com/a/49123152/1689770
            //options.addArguments("--disable-gpu"); //https://stackoverflow.com/questions/51959986/how-to-solve-selenium-chromedriver-timed-out-receiving-message-from-renderer-exc
            options.addArguments("--disable-features=VizDisplayCompositor"); //https://stackoverflow.com/questions/55373625/getting-timed-out-receiving-message-from-renderer-600-000-when-we-execute-selen
        }

        DesiredCapabilities capabilities = new DesiredCapabilities();

        capabilities.setCapability("UNHANDLED_PROMPT_BEHAVIOUR", "ignore");
        options.merge(capabilities);

        ChromeDriver chromeDriver = new ChromeDriver(options);
//        chromeDriver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
//        chromeDriver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        return chromeDriver;
    }
}
