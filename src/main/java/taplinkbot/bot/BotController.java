//FIN
package taplinkbot.bot;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import taplinkbot.TapLinkBotException;
import taplinkbot.browser.Browser;
import taplinkbot.components.LangComponent;
import taplinkbot.helpers.PhoneNumber;
import taplinkbot.services.PhoneControlService;
import taplinkbot.services.TapLinkAccount;

/**
 * Алгоритмы действий на сайте taplink.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class BotController {

    private final LangComponent lang;

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
            throw new TapLinkBotException(lang.get("actions.phone_number_invalid", phoneNumber));
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
     * @param profile
     * @return
     */
    synchronized public String getNumber(Profile profile) {

        return multiPageActions.getNumber(profile);
    }

    synchronized public boolean testBugErrConnectionClosed() {

        return browser.testBugErrConnectionClosed();
    }
}
