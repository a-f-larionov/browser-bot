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

    /**
     * текст: блока ватсап узнать цену
     */
    public static String textWhatsUpPrice = "Узнать цену в WhatsApp";

    /**
     * часть текста блока Whatsup
     */
    public static String textWhatsupHrefPostText = "%D0%97%D0%B4%D1%80%D0%B0%D0%B2%D1%81%D1%82%D0%B2%D1%83%D0%B9%D1%82%D0%B5%21%20%D0%AF%20%D1%85%D0%BE%D1%87%D1%83%20%D1%83%D0%B7%D0%BD%D0%B0%D1%82%D1%8C%20%D1%86%D0%B5%D0%BD%D1%83%20%D0%BD%D0%B0%20%D0%BF%D0%BE%D1%80%D1%82%D1%80%D0%B5%D1%82.";

    /**
     * xpath кнопкаи [Перейти в чат] на странице https://api.whatsapp.com/send?phone=79102790307&text=Здравствуйте%21%20Я%20хочу%20узнать%20цену%20на%20портрет.
     */
    public static String xpathApiWhatsUpComChatButton = "//div/a[contains(text(), 'Перейти в чат')]";

    /**
     * xpath блока Узнать цену в WhatsUp
     **/
    public static String xpathWhatsUpPrice = "//span[contains(text(),'Узнать цену в WhatsApp')]/..";
}
