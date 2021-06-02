//FIN
package taplinkbot.bot;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.stereotype.Component;
import taplinkbot.TapLinkBotException;
import taplinkbot.browser.Browser;
import taplinkbot.components.LangComponent;

/**
 * Действия авторизии.
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class AuthActions {

    private final Browser browser;

    private final LangComponent lang;

    private WebElement we;

    public void webLogin(String login, String password) {

        String url = DataProvider.urlSignIn;

        // Если уже авторизованы, ничего не делаем
        if (checkIsAuthorized(login)) return;

        logout();

        browser.setComment(lang.get("actions.auth.openAuthPage", url));
        browser.get(url);

        enterLogin(login);

        enterPassword(password);

        authSubmit();

        if (!checkIsAuthorized(login)) {
            throw new TapLinkBotException(lang.get("actions.auth.auth_failed"));
        }
    }

    /**
     * Выполняет выход.
     */
    private void logout() {

        browser.setComment(lang.get("actions.auth.do_logout"));
        browser.get(DataProvider.urlLogout);
    }

    /**
     * Ввести логин
     *
     * @param login логин
     */
    private void enterLogin(String login) {

        browser.setComment(lang.get("actions.auth.field_login_access"));
        we = browser.findElement(By.xpath(DataProvider.xpathSignInFieldLogin));

        browser.setComment(lang.get("actions.auth.field_login_enter"));
        we.sendKeys(login);
    }

    /**
     * Ввод пароля
     *
     * @param password пароль
     */
    private void enterPassword(String password) {

        browser.setComment(lang.get("actions.auth.field_password_access"));
        we = browser.findElement(By.xpath(DataProvider.xpathSigInFieldPassword));

        browser.setComment(lang.get("actions.auth.field_password_enter"));
        we.sendKeys(password);
    }

    /**
     * Нажатие кнопки submit формы авторизации
     */
    private void authSubmit() {

        browser.setComment(lang.get("actions.auth.button_submit_access"));
        we = browser.findElement(By.xpath(DataProvider.xpathSignInButtonSubmit));

        browser.setComment(lang.get("actions.auth.button_submit_click"));
        we.click();

        browser.setComment(lang.get("actions.auth.check_profile_icon"));
        browser.waitElement(By.xpath(DataProvider.xpathProfileIcon));
    }

    /**
     * Проверка авторизации
     *
     * @param login логин
     * @return boolean true - если авторизация под запрашиваемым логином, иначе - false
     */
    private boolean checkIsAuthorized(String login) {

        String url = DataProvider.urlAccountSettings;

        browser.setComment(lang.get("actions.get_url", url));
        browser.get(url);

        if (!browser.isElementPresent(By.xpath(DataProvider.xpathAccountSettingsEmailField))) {
            return false;
        }

        we = browser.waitElement(By.xpath(DataProvider.xpathAccountSettingsEmailField));

        return login.equals(we.getAttribute("value"));
    }
}
