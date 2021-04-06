package taplinkbot.bot;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.stereotype.Component;
import taplinkbot.telegram.TelegramBot;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * @link https://geekflare.com/install-chromium-ubuntu-centos/
 */
@Component
public class DriverWrapper implements WebDriver {

    private final TelegramBot telegram;

    private RemoteWebDriver driver;

    private String lastHumanComment;

    /**
     * Настройка драйвера
     *
     * @todo try getBean ChromeDriver()
     */
    public DriverWrapper(TelegramBot telegram) {
        super();

        this.telegram = telegram;
        driver = getDriver();
    }

    private ChromeDriver getDriver() {
        System.setProperty("webdriver.chrome.driver", "/var/www/chromedriver/chromedriver");

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        options.addArguments("--disable-gpu");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");

        return new ChromeDriver(options);
    }

    @Override
    public void get(String s) {
        driver.get(s);
    }

    @Override
    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    @Override
    public String getTitle() {
        return driver.getTitle();
    }

    @Override
    public List<WebElement> findElements(By by) {
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
        try {
            return driver.findElement(by);
        } catch (NotFoundException e) {
            if (seconds <= 0) return findElement(by);
            try {
                Thread.sleep(3000);
            } catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
            }
            return waitElement(by, seconds - 3);
        }
    }

    @Override
    public WebElement findElement(By by) {
        try {
            return driver.findElement(by);
        } catch (NotFoundException e) {
            alert("Не удалось найти элемент. действие: " + lastHumanComment + ". Обратитесь к разработчику.");
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

    private void alert(String message) {
        telegram.alert(message, takeSreenshot());
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
        if (driver != null) driver.quit();
        driver = getDriver();
    }

    public String getHumanComment() {
        return lastHumanComment;
    }
}
