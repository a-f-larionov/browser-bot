
package browserbot.bots.taplink;

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
    public final static String urlSignIn = "https://taplink.ru/profile/auth/signin/";

    /**
     * URL для разлогинивония
     */
    public final static String urlLogout = "https://taplink.ru/api/auth/logout.json";

    /**
     * Страница свойств профиля.
     */
    public static String urlAccountSettings = "https://taplink.ru/profile/2988200/account/settings/";

    /**
     * xpath: поле логина на странцие авторизации
     */
    public final static String xpathSignInFieldLogin = "/html/body/div[1]/div[4]/section/div[2]/div/div[2]/form/div[1]/div/input";

    /**
     * xpath: поле пароль на странице авторизации
     */
    public final static String xpathSigInFieldPassword = "/html/body/div[1]/div[4]/section/div[2]/div/div[2]/form/div[2]/div[2]/input";

    /**
     * xpath: кнопка сабмит на странице авторизации
     */
    public final static String xpathSignInButtonSubmit = "/html/body/div[1]/div[4]/section/div[2]/div/div[2]/form/button";

    /**
     * xpath: иконка профиля
     */
    public static String xpathProfileIcon = "/html/body/div[1]/div[4]/div/div[2]/header/div/div[1]/div[2]/div/div/div[2]/img";

    /**
     * xpath: на странице профиля, поле емейл
     */
    public final static String xpathAccountSettingsEmailField = "//input[@type='email']";

    /**
     * текст: блока ватсап узнать цену
     */
    public final static String textWhatsUpPrice = "Узнать цену в WhatsApp";

    /**
     * часть текста блока Whatsup
     */
    public final static String textWhatsupHrefPostText = "%D0%97%D0%B4%D1%80%D0%B0%D0%B2%D1%81%D1%82%D0%B2%D1%83%D0%B9%D1%82%D0%B5%21%20%D0%AF%20%D1%85%D0%BE%D1%87%D1%83%20%D1%83%D0%B7%D0%BD%D0%B0%D1%82%D1%8C%20%D1%86%D0%B5%D0%BD%D1%83%20%D0%BD%D0%B0%20%D0%BF%D0%BE%D1%80%D1%82%D1%80%D0%B5%D1%82.";

    /**
     * xpath кнопкаи [Перейти в чат] на странице https://api.whatsapp.com/send?phone=79102790307&text=Здравствуйте%21%20Я%20хочу%20узнать%20цену%20на%20портрет.
     */
    public final static String xpathWhatsupApiPageMessage = "//div[contains(text(),'Здравствуйте! Я хочу узнать цену на портрет.')]";

    /**
     * xpath блока Узнать цену в WhatsUp
     **/
    public final static String xpathWhatsUpPrice = "//span[contains(text(),'Узнать цену в WhatsApp')]/..";

    /**
     * url: главная страница таплинка
     */
    public final static String urlTapLinkIndex = "https://taplink.ru/";

    /**
     * xpath: блок Whatsup в админке
     */
    public final static String xpathAdminWhatsUpBlock = "//span[contains(text(),'Узнать цену в WhatsApp')]/parent::a/parent::div";

    /**
     * xpath: поле номера телефона блока Whatsup
     */
    public final static String xpathAdminWhatsupPhoneElement = "//input[@type='tel' and @data-title='Страна']";

    /**
     * xpath: в админке кнопка сохранить номер
     */
    public final static String xpathAdminSaveButton = "/html/body/div[4]/div[2]/div/footer/div[2]/button";

    /**
     * url: страница профилей
     */
    public final static String urlProfilePages = "https://taplink.ru/profile/2988200/pages/";

    /**
     * xpath: страница профилей, попап меню
     */
    public final static String xpathProfilesPopUpMenu = "/html/body/div[1]/div[4]/div/div[2]/header/div/div[1]/div[2]/div/div/div[2]/img";

    /**
     * xpath: старница профилей, мои профили
     */
    public final static String xpathProfilesMyProfiles = "/html/body/div[1]/div[4]/div/div[2]/header/div/div[1]/div[2]/div/div/div[3]/div[1]/a";

    /**
     * текст: страница профилей, мои профили
     */
    public final static String textMyProfiles = "Мои профили";
}
