package browserbot.browser;

import browserbot.BrowserBotException;
import browserbot.bots.taplink.Profile;
import browserbot.services.UrlProfiler;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.*;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Set;

/**
 * Обрётка над драйвером браузера.
 *
 * @link https://geekflare.com/install-chromium-ubuntu-centos/
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class Browser implements WebDriver {

    private final WebDriverFactory webDriverFactory;

    private final UrlProfiler urlProfiler;

    private final ScreenShooter screenShooter;

    private RemoteWebDriver driver;

    /**
     * Время по умолчанию для ожидания элементов на странице.
     * Секунды.
     */
    @Value("${webdriver.waitElementSeconds:300}")
    private int waitElementSeconds;

    /**
     * "Последний комментарий".
     * Будет выведен при ошибке, для дебага.
     * Например: клиентский код запросил элемент:
     * 1 comment("Нажатие кнопки входа")
     * 2 findElementBy();
     * Если элемент не будет найден, ошибка будет содержать текст последнего комментария
     * так быстрей устранить баг.
     */
    @Getter
    private String comment;

    public void setComment(String comment) {

        log.info(comment);

        this.comment = comment;
    }

    /**
     * Получим доступ к браузеру.
     */
    @PostConstruct
    public void init() {
        driver = webDriverFactory.buildWebDriver();
    }

    /**
     * Открыть страницу в браузере
     * Метод профилируется бином UrlProfiler
     *
     * @param url url страницы для открытия
     */
    @Override
    public void get(String url) {
        urlProfiler.start();

        log.info(url);

        driver.get(url);

        urlProfiler.finish(url);
    }

    /**
     * Получить текущий url.
     *
     * @return URL адресной строки
     */
    @Override
    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    /**
     * Заголовок страницы
     *
     * @return title страницы
     */
    @Override
    public String getTitle() {
        return driver.getTitle();
    }

    /**
     * Найти элементы
     *
     * @param by org.openqa.selenium.By
     */
    @Override
    public List<WebElement> findElements(By by) {
        return driver.findElements(by);
    }

    public boolean isElementPresent(By by) {
        return isElementPresent(by, waitElementSeconds);
    }

    /**
     * Проверяет наличие элемента.
     *
     * @param by      org.openqa.selenium.By
     * @param seconds время ожидания в секундах
     * @return WebElement или null если не присутствует
     */
    public boolean isElementPresent(By by, int seconds) {

        try {
            waitElement(by, seconds);

            return true;
        } catch (TimeoutException | NotFoundException e) {
            return false;
        }
    }

    /**
     * Ожидать элемент.
     * По умолчанию ждёт waitElementSeconds секунд.
     *
     * @param by org.openqa.selenium.By
     */
    public WebElement waitElement(By by) {
        return waitElement(by, waitElementSeconds);
    }

    /**
     * Ждёт элемент.
     *
     * @param by      селектор элемента.
     * @param seconds ожидание в секундах.
     */
    public WebElement waitElement(By by, int seconds) {

        WebDriverWait wait = new WebDriverWait(driver, seconds);
        wait.ignoring(ElementClickInterceptedException.class);
        wait.until(ExpectedConditions.visibilityOfElementLocated(by));

        //@todo, windows is mazimazed ...
        JavascriptExecutor js = driver;
        driver.manage().window().maximize();
        js.executeScript("window.scrollBy(0,1000)");

        return findElement(by);
    }

    /**
     * Ищет элемент, если не найден выбросит исключение BrowserException
     *
     * @param by org.openqa.selenium.By
     * @return WebElement
     */
    @Override
    public WebElement findElement(By by) {

        log.info(by.toString());

        try {
            return driver.findElement(by);

        } catch (NotFoundException e) {
            throw new BrowserBotException(
                    "Не удалось найти элемент." +
                            " Обратитесь к разработчику." +
                            " Элемент:" + " `" + by + "`");
        }
    }

    /**
     * Возвращает HTML страницы
     *
     * @return html страницы
     */
    @Override
    public String getPageSource() {
        return driver.getPageSource();
    }

    /**
     * Закрывает страницу
     */
    @Override
    public void close() {
        driver.close();
    }

    /**
     * Закрывает браузер
     */
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

    /**
     * Делает скриншот текущей страницы, возвращает url на него.
     *
     * @return url на скриншот
     */
    @SneakyThrows
    public String takeScreenshot() {
        return screenShooter.takeScreenshot(driver);
    }

    /**
     * Проверка бага обрыва связи с браузером.
     *
     * @return true - в случае перезапуска браузера, иначе false
     */
    public boolean testBugErrConnectionClosed() {
        try {
            get(Profile.Canvas.getMultiPageUrl());
            return false;

        } catch (Exception e) {

            if (e.getMessage().equals("unknown error: net::ERR_CONNECTION_CLOSED")) {

                fixBugErrConnectionClosed();
                return true;

            } else {
                throw e;
            }
        }
    }

    /**
     * Перезагрузка браузера.
     * В случае его не доступности.
     * Это избавлет от проблемы "unknown error: net::ERR_CONNECTION_CLOSED".
     *
     * @todo auto restorn on cathc Exception!
     */
    public void fixBugErrConnectionClosed() {

        resetBrowser();
    }

    public void resetBrowser() {

        if (driver != null) driver.quit();

        driver = webDriverFactory.buildWebDriver();

        log.info("Web Driver перезапущен.");
    }

    public void waitElementDisappear(String xpathAdminSaveButton) {
        //@Todo isNotPresent\waitForGoway?
        log.info(takeScreenshot());
        try {
            Thread.sleep(60 * 1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info(takeScreenshot());
    }
}