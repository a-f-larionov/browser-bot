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
public class TapLinkAuthActions {

    protected final TelegramBot telegram;

    protected final DriverWrapper browser;

    protected final BotContexts botContexts;

    private final Environment env;

    private WebElement we;

    public void authorize() {

        try {

            //@todo check if autorized - do not repeat it! or reset

            //@todo this account priznak
            if (checkIsAuthorized()) return;

            browser.reset();

            String url = "https://taplink.ru/profile/auth/signin/";
            browser.humanComment("Открытие страницы:" + url);
            browser.get(url);

            enterLogin();

            enterPassword();

            authSubmit();

        } catch (Exception e) {

            telegram.alert("Авторизация не удалась.", browser.takeSreenshot());
            throw e;
        }
    }

    private void enterLogin() {
        browser.humanComment("Обращение к полю ввода логина");

        we = browser.findElement(By.xpath("/html/body/div[1]/div[4]/section/div[2]/div/div[2]/form/div[1]/div/input"));

        assert we != null;

        browser.humanComment("Ввод логина");
        we.sendKeys(getLogin());
    }

    private void enterPassword() {
        browser.humanComment("Обращение к поле ввода пароля");
        we = browser.findElement(By.xpath("/html/body/div[1]/div[4]/section/div[2]/div/div[2]/form/div[2]/div[2]/input"));

        assert we != null;

        browser.humanComment("Ввод пароля");
        we.sendKeys(getPassword());
    }

    private void authSubmit() {
        browser.humanComment("Обращение к кнопки авторизации");
        we = browser.findElement(By.xpath("/html/body/div[1]/div[4]/section/div[2]/div/div[2]/form/button"));

        assert we != null;

        browser.humanComment("Нажатие кнопки авторизации");
        we.click();

        browser.humanComment("Проверка начилия иконки профиля(проверка авторизации)");
        browser.waitElement(By.xpath("/html/body/div[1]/div[4]/div/div[2]/header/div/div[1]/div[2]/div/div/div[2]/img"));

        checkIsAuthorized();

        try {
            //@todo for what?
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private boolean checkIsAuthorized() {
        //@todo cabinet?

        String url = "https://taplink.ru/profile/2988200/account/settings/";
        browser.humanComment("Открытие страницы:" + url);
        browser.get(url);

        System.out.println(browser.takeSreenshot());

        try {
            browser.skipOneAlert();
            we = browser.waitElement(By.xpath("/html/body/div[1]/div[4]/div/div[3]/div/div[1]/div[2]/div/div/div/div[1]/div/p/div/div/div/div/input"), 5);
        } catch (Exception e) {
            return false;
        }

        String value = we.getAttribute("value");
        log.info("авторизованно:" + value);

        return getLogin().equals(value);
    }


    private String getLogin() {
        return getProp("username");
    }

    private String getPassword() {
        return getProp("password");
    }

    private String getProp(String name) {
        String propName = "taplink." +
                botContexts.getCurrent().name
                + "." + name;
        String prop = env.getProperty(propName);

        log.info("prop " + propName + " " + prop);

        return prop;
    }

}
