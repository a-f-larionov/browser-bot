//FIN
package taplinkbot.bot;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Предоставляет данные для работы с taplink.ru
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DataProvider {

    /**
     * Страница авторизации
     */
    public static String urlSignIn = "https://taplink.ru/profile/auth/signin/";

    /**
     * URL для разлогинивония
     */
    public static String urlLogout = "https://taplink.ru/api/auth/logout.json";

    /**
     * Страница свойств профиля.
     */
    public static String urlAccountSettings = "https://taplink.ru/profile/2988200/account/settings/";

    /**
     * xpath: поле логина на странцие авторизации
     */
    public static String xpathSignInFieldLogin = "/html/body/div[1]/div[4]/section/div[2]/div/div[2]/form/div[1]/div/input";

    /**
     * xpath: поле пароль на странице авторизации
     */
    public static String xpathSigInFieldPassword = "/html/body/div[1]/div[4]/section/div[2]/div/div[2]/form/div[2]/div[2]/input";

    /**
     * xpath: кнопка сабмит на странице авторизации
     */
    public static String xpathSignInButtonSubmit = "/html/body/div[1]/div[4]/section/div[2]/div/div[2]/form/button";

    /**
     * xpath: иконка профиля
     */
    public static String xpathProfileIcon = "/html/body/div[1]/div[4]/div/div[2]/header/div/div[1]/div[2]/div/div/div[2]/img";

    /**
     * xpath: на странице профиля, поле емейл
     */
    public static String xpathAccountSettingsEmailField = "//input[@type='email']";
}
