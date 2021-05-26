package taplinkbot.browser;

import com.github.mike10004.xvfbmanager.Screenshot;
import com.github.mike10004.xvfbmanager.XvfbController;
import com.github.mike10004.xvfbmanager.XvfbManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebDriverFactory {

    private final Environment env;

    public ChromeDriver buildWebDriver() {

//        XvfbManager xvfbManager = new XvfbManager();
//
//        try (final XvfbController controller = xvfbManager.start(1)) {
//            controller.waitUntilReady();
//
//            Future<Process> processFuture = Executors.newSingleThreadExecutor().submit(new Callable<Process>() {
//                @Override
//                public Process call() throws Exception {
//                    ProcessBuilder pb = new ProcessBuilder();
//                    pb.environment().put("DISPLAY", controller.getDisplay());
//
//                    log.info("DISPLAY IS: " + controller.getDisplay());
//
//                    return pb.command("xclock").start();
//                }
//            });
//
//            Screenshot screenshot = controller.getScreenshooter().capture();
//            processFuture.cancel(true);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        System.setProperty("webdriver.chrome.driver",
                Objects.requireNonNull(env.getProperty("webdriver.path")));

        //System.setProperty("webdriver.chrome.logfile", "chromedriver.log");
        //System.setProperty("webdriver.chrome.verboseLogging", "true");

        ChromeOptions options = new ChromeOptions();

        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--start-maximized");
        options.addArguments("window-size=1000,2400");

        //  because a root
        if (env.getProperty("webdriver.--no-sandbox") != null &&
                env.getProperty("webdriver.--no-sandbox").equals("true")) {
            options.addArguments("--no-sandbox");
            //--disable-setuid-sandbox
        }

        if (env.getProperty("webdriver.--headless") != null &&
                env.getProperty("webdriver.--headless").equals("true")) {
            options.addArguments("--headless");
        }

        // Windows only
        if (env.getProperty("webdriver.--disable-gpu") != null &&
                env.getProperty("webdriver.--disable-gpu").equals("true")) {
            options.addArguments("--disable-gpu");
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

        if (false) capabilities.setCapability("UNHANDLED_PROMPT_BEHAVIOUR", "ignore");
        options.merge(capabilities);

        ChromeDriver chromeDriver = new ChromeDriver(options);
//        chromeDriver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
//        chromeDriver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        return chromeDriver;
    }
}
