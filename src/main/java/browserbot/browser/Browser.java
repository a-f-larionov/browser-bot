//FIN
package browserbot.browser;

import browserbot.BrowserBotException;
import browserbot.bots.taplink.Profile;
import browserbot.services.LangService;
import browserbot.services.UrlProfiler;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
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

    private final LangService lang;

    private RemoteWebDriver driver;

    /**
     * Время по умолчанию для ожидания элементов на странице.
     * Секунды.
     */
    @Value("${webdriver.waitElementSeconds:300}")
    private int waitElementSeconds;

    /**
     * "Описание действия".
     * Будет выведен при ошибке, для дебага.
     * Например: клиентский код запросил элемент:
     * 1 actionComment("Нажатие кнопки входа")
     * 2 findElementBy();
     * Если элемент не будет найден, ошибка будет содержать текст последнего комментария
     * так быстрей устранить баг.
     */
    @Getter
    private String actionComment;

    /**
     * Устанавливает комментарий к действия.
     * Это помогает дебажить
     *
     * @param actionComment
     */
    public void setActionComment(String actionComment) {

        log.info(actionComment);

        this.actionComment = actionComment;
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

    /**
     * Существует ли элемент?
     *
     * @param by
     * @return true - если да, иначе - false
     */
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

        try {
            return driver.findElement(by);

        } catch (NotFoundException e) {
            throw new BrowserBotException(
                    lang.get("actions.cantfind_element", by.toString()));
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

                resetBrowser();

                return true;

            } else {
                throw e;
            }
        }
    }

    /**
     * Ожидает исчезновение элемента.
     *
     * @param by Элемент ожидающий исчезновения
     */
    public void waitElementDisappear(By by) {

        while (isElementPresent(by, 1)) {
            // nothing to do, just waiting
            log.info("wait element: " + by.toString());
        }
    }

    /**
     * Перезагрузка браузера.
     */
    public void resetBrowser() {

        if (driver != null) driver.quit();

        driver = webDriverFactory.buildWebDriver();

        log.info("Web Driver перезапущен.");
    }

    public Object executeScript(String script, Object... args) {
        return driver.executeScript(script, args);
    }
}