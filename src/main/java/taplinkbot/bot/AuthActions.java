package taplinkbot.bot;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import taplinkbot.browser.DriverWrapper;
import taplinkbot.telegram.TelegramBot;

/**
 * @todo move mini actions, and group actions
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class AuthActions {

    protected final TelegramBot telegram;

    protected final DriverWrapper browser;

    protected final BotContexts botContexts;

    private final Environment env;

    private WebElement we;

    public void signin(String login, String password) {

        String url = "https://taplink.ru/profile/auth/signin/";

        // Если уже авторизованы, ничего не делаем
        if (checkIsAuthorized(login)) return;

        browser.reset();

        //@todo format string?
        browser.comment("Открытие страницы для авторизации: " + url);
        browser.get(url);

        enterLogin(login);

        enterPassword(password);

        authSubmit();

        checkIsAuthorized(login);
    }

    private void enterLogin(String login) {

        browser.comment("Обращение к полю ввода логина.");
        we = browser.findElement(By.xpath("/html/body/div[1]/div[4]/section/div[2]/div/div[2]/form/div[1]/div/input"));

        browser.comment("Ввод логина.");
        we.sendKeys(login);
    }

    private void enterPassword(String password) {

        browser.comment("Обращение к поле ввода пароля");
        we = browser.findElement(By.xpath("/html/body/div[1]/div[4]/section/div[2]/div/div[2]/form/div[2]/div[2]/input"));

        browser.comment("Ввод пароля");
        we.sendKeys(password);
    }

    private void authSubmit() {

        browser.comment("Обращение к кнопки авторизации");
        we = browser.findElement(By.xpath("/html/body/div[1]/div[4]/section/div[2]/div/div[2]/form/button"));

        browser.comment("Нажатие кнопки авторизации");
        we.click();

        browser.comment("Проверка начилия иконки профиля(проверка авторизации)");
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

        browser.comment("Открытие страницы:" + url);
        browser.get(url);

        try {
            //@todo strange construction
            browser.skipOneAlert();
            we = browser.waitElement(By.xpath("/html/body/div[1]/div[4]/div/div[3]/div/div[1]/div[2]/div/div/div/div[1]/div/p/div/div/div/div/input"), 5);
        } catch (Exception e) {
            return false;
        }

        String value = we.getAttribute("value");
        log.info("авторизованно:" + value);

        return login.equals(value);
    }
}
