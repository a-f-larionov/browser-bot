package taplinkbot.browser;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.*;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
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

    private final PageLoadProfiler pageLoadProfiler;

    private final Environment env;

    private final Screenshoter screenshoter;

    private RemoteWebDriver driver;

    /**
     * Время по умолчанию для ожидания элементов на странице.
     * Секунды.
     */
    @Value("${webdriver.defaultTimeout:300}")
    public int DEFAULT_WAIT_TIME;

    /**
     * "Последний комментарий".
     * Будет выведен при ошибке, для дебага.
     * Например: клиентский код запросил элемент:
     * lastComment("Нажатие кнопки входа")
     * findElementBy();
     * если кнопка не будет найдена, ошибка будет содержать текст последнего комментария
     * так быстрей устранить баг.
     */
    @Getter
    @Setter
    private String comment;

    /**
     * Запустим браузер.
     */
    @PostConstruct
    public void init() {
        driver = webDriverFactory.buildWebDriver();
    }

    /**
     * Открыть страницу в браузере
     * Метод профилируется бином PageLoadProfiler
     *
     * @param url url страницы для открытия
     */
    @Override
    public void get(String url) {
        pageLoadProfiler.start();

        driver.get(url);

        pageLoadProfiler.finish(url);
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
     * @param by org.openqa.selenium
     * @return
     */
    @Override
    public List<WebElement> findElements(By by) {
        return driver.findElements(by);
    }

    /**
     * Ожидать элемент.
     * По умолчанию ждёт DEFAULT_WAIT_TIME секунд.
     *
     * @param by org.openqa.selenium.By
     * @return
     */
    public WebElement waitElement(By by) {
        return waitElement(by, DEFAULT_WAIT_TIME);
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
        } catch (TimeoutException | NotFoundException | BrowserException e) {
            return false;
        }
    }

    /**
     * Ждёт элемент.
     *
     * @param by      селектор элемента.
     * @param seconds ожидание в секундах, не точное.
     */
    public WebElement waitElement(By by, int seconds) {

        WebDriverWait wait = new WebDriverWait(driver, seconds);
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

            throw new BrowserException(
                    "Не удалось найти элемент. Обратитесь к разработчику. Элемент:" +
                            " `" + by.toString() + "`",
                    takeScreenshot(),
                    comment
            );
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

        return screenshoter.takeScreenshot(driver);
    }

    /**
     * Перезагрузка браузера.
     * В случае его не доступности.
     * Это избавлет от проблемы "unknown error: net::ERR_CONNECTION_CLOSED".
     */
    public void fixBugErrConnectionClosed() {

        if (driver != null) driver.quit();

        driver = webDriverFactory.buildWebDriver();
    }
}
