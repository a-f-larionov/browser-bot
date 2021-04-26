package taplinkbot.bot;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import taplinkbot.entities.PageLoads;
import taplinkbot.repositories.PageLoadsRepository;
import taplinkbot.service.StateService;
import taplinkbot.telegram.BotContext;
import taplinkbot.telegram.TelegramBot;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * @link https://geekflare.com/install-chromium-ubuntu-centos/
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DriverWrapper implements WebDriver {

    private final Environment env;

    private final TelegramBot telegram;

    private final PageLoadsRepository pageLoadsRepository;

    private final StateService stateService;

    private RemoteWebDriver driver;

    private String lastHumanComment;


    /**
     * Настройка драйвера
     *
     * @todo try getBean ChromeDriver()
     */

    @PostConstruct
    public void init() {

        driver = getDriver();

        log.info("DriverWrapper PostConstructor");
    }

    private ChromeDriver getDriver() {

        assert env.getProperty("driver.path") != null;

        System.setProperty("webdriver.chrome.driver", Objects.requireNonNull(env.getProperty("driver.path")));

        ChromeOptions options = new ChromeOptions();

        options.addArguments("--headless");
        options.addArguments("--disable-gpu");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");

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

    @Override
    public void get(String url) {

        assert driver != null;

        long start = System.currentTimeMillis();
        driver.get(url);
        long finish = System.currentTimeMillis();


        BotContext botContext = stateService.getBotContext();

        pageLoadsRepository.save(new PageLoads(url, finish - start, botContext));
    }

    @Override
    public String getCurrentUrl() {
        assert driver != null;
        return driver.getCurrentUrl();
    }

    @Override
    public String getTitle() {
        assert driver != null;
        return driver.getTitle();
    }

    @Override
    public List<WebElement> findElements(By by) {
        assert driver != null;
        return driver.findElements(by);
    }


    public WebElement waitElement(By by) {
        return waitElement(by, 300);
    }

    /**
     * Ждёт элемент.
     * Кривой метод, но пока так
     *
     * @param by      селектор элемента.
     * @param seconds ожидание в секундах, не точное.
     * @todo применить WebDriverWait
     */
    public WebElement waitElement(By by, int seconds) {
        assert driver != null;

        try {
            return driver.findElement(by);
        } catch (NotFoundException e) {
            if (seconds <= 0) return findElement(by);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
            }
            return waitElement(by, seconds - 1);
        }
    }

    @Override
    public WebElement findElement(By by) {
        try {
            return driver.findElement(by);
        } catch (NotFoundException e) {

            if (skipOneAlert) {
                skipOneAlert = false;
            } else {
                telegram.alert("Не удалось найти элемент. Действие: " + lastHumanComment
                                + ". Обратитесь к разработчику. " + by.toString(),
                        takeSreenshot()
                );
            }

            throw e;
        }
    }

    @Override
    public String getPageSource() {
        return driver.getPageSource();
    }

    @Override
    public void close() {
        driver.close();
    }

    @Override
    public void quit() {
        driver.quit();
    }

    @Override
    public Set<String> getWindowHandles() {
        return driver.getWindowHandles();
    }

    @Override
    public String getWindowHandle() {
        return driver.getWindowHandle();
    }

    @Override
    public TargetLocator switchTo() {
        return driver.switchTo();
    }

    @Override
    public Navigation navigate() {
        return driver.navigate();
    }

    @Override
    public Options manage() {
        return driver.manage();
    }

    public void humanComment(String comment) {
        lastHumanComment = comment;
    }

    public String takeSreenshot() {

        String filesPath, url, fileName;
        TakesScreenshot screenshot = (TakesScreenshot) driver;
        File source = screenshot.getScreenshotAs(OutputType.FILE);
        filesPath = "/var/www/files/";
        fileName = "botscreen_" + System.currentTimeMillis() + ".png";
        url = "http://51.15.85.52/" + fileName;

        File dest = new File(filesPath + fileName);
        try {
            FileUtils.copyFile(source, dest);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return url;
    }

    public void reset() {
        log.info("driver reseted");

        if (driver != null) driver.quit();

      //  killChrome();

        driver = getDriver();
    }

    private void killChrome() {

        log.info("KILL CHROME");

        try {
            Process pkill_chrome = Runtime.getRuntime().exec("pkill chrome");


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getHumanComment() {
        return lastHumanComment;
    }

    private boolean skipOneAlert = false;

    public void skipOneAlert() {
        skipOneAlert = true;
    }
}
