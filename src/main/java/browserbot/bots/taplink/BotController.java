//FIN
package browserbot.bots.taplink;

import browserbot.BrowserBotException;
import browserbot.bots.taplink.actions.AuthActions;
import browserbot.bots.taplink.actions.MultiPageActions;
import browserbot.bots.taplink.actions.PhoneNumberActions;
import browserbot.bots.taplink.actions.ProfileActions;
import browserbot.browser.Browser;
import browserbot.helpers.PhoneNumber;
import browserbot.services.LangService;
import browserbot.services.PhoneControlService;
import browserbot.services.TapLinkAccount;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Алгоритмы действий на сайте taplink.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class BotController {

    private final LangService lang;

    private final AuthActions authActions;

    private final PhoneNumberActions phoneNumberActions;

    private final ProfileActions profileActions;

    private final MultiPageActions multiPageActions;

    private final TapLinkAccount tapLinkAccount;

    private final PhoneControlService multiPageControl;

    private final Browser browser;

    /**
     * Установить номер телефона
     * 1 - Авторизуется
     * 2 - Выбирает профиль
     * 3 - Устонавливает номер
     * 4 - Проверяет мультистраницу
     *
     * @param phoneNumber номер телефона
     */
    synchronized public boolean setPhoneNumber(String phoneNumber, Profile profile) {

        if (!PhoneNumber.validate(phoneNumber)) {
            throw new BrowserBotException(lang.get("actions.phone_number_invalid", phoneNumber));
        }

        authActions.webLogin(tapLinkAccount.getLogin(), tapLinkAccount.getPassword());

        profileActions.changeTo(profile);

        phoneNumberActions.setPhoneNumber(phoneNumber);

        return multiPageActions.checkPhoneNumber(profile, phoneNumber);
    }

    /**
     * Проверяем оновную страницу.
     */
    synchronized public void multiPageControl(Profile profile) {

        String phoneNumber = getNumber(profile);

        multiPageControl.save(phoneNumber, profile);
    }

    /**
     * Получить номер с мультистраницы
     *
     * @param profile профиль
     * @return номер телефона
     */
    synchronized public String getNumber(Profile profile) {

        return multiPageActions.getNumber(profile);
    }
}
