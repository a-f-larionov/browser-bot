package taplinkbot.bot;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import taplinkbot.browser.Browser;
import taplinkbot.telegram.TelegramBot;

/**
 * @todo move mini actions, and group actions
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class AuthActions {

    protected final TelegramBot telegram;

    protected final Browser browser;

    protected final Profiles profiles;

    private final Environment env;

    private WebElement we;

    public void webLogin(String login, String password) {

        String url = "https://taplink.ru/profile/auth/signin/";

        // Если уже авторизованы, ничего не делаем
        if (checkIsAuthorized(login)) return;

        //@todo logout method here!
        browser.fixBugErrConnectionClosed();

        //@todo format string?
        browser.setComment("Открытие страницы для авторизации: " + url);
        browser.get(url);


        log.info("1");


        enterLogin(login);

        log.info("2");
        enterPassword(password);

        log.info("3");
        authSubmit();
        log.info("df1");
        if (!checkIsAuthorized(login)) {
            log.info("df2");
            throw new BotException("Не удалось авторизоваться.");
        }
    }

    private void enterLogin(String login) {

        browser.setComment("Обращение к полю ввода логина.");
        we = browser.findElement(By.xpath("/html/body/div[1]/div[4]/section/div[2]/div/div[2]/form/div[1]/div/input"));

        browser.setComment("Ввод логина.");
        we.sendKeys(login);
    }

    private void enterPassword(String password) {

        browser.setComment("Обращение к поле ввода пароля");
        we = browser.findElement(By.xpath("/html/body/div[1]/div[4]/section/div[2]/div/div[2]/form/div[2]/div[2]/input"));

        browser.setComment("Ввод пароля");
        we.sendKeys(password);
    }

    private void authSubmit() {

        browser.setComment("Обращение к кнопки авторизации");
        we = browser.findElement(By.xpath("/html/body/div[1]/div[4]/section/div[2]/div/div[2]/form/button"));

        browser.setComment("Нажатие кнопки авторизации");
        we.click();

        browser.setComment("Проверка начилия иконки профиля(проверка авторизации)");

        browser.waitElement(By.xpath("/html/body/div[1]/div[4]/div/div[2]/header/div/div[1]/div[2]/div/div/div[2]/img"));

        try {
            //@todo for what?
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private boolean checkIsAuthorized(String login) {
        //@todo cabinet?

        //@Todo data provider
        String url = "https://taplink.ru/profile/2988200/account/settings/";

        browser.setComment("Открытие страницы:" + url);
        browser.get(url);

        String xpath = "/html/body/div[1]/div[4]/div/div[3]/div/div[1]/div[2]/div/div/div/div[1]/div/p/div/div/div/div/input";

        if (!browser.isElementPresent(By.xpath(xpath), 5)) {
            return false;
        }

        we = browser.waitElement(By.xpath(xpath), 5);

        String value = we.getAttribute("value");

        return login.equals(value);
    }
}
