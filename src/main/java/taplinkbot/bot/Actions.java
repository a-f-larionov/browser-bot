package taplinkbot.bot;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import taplinkbot.TapLinkBotException;
import taplinkbot.browser.Browser;
import taplinkbot.entities.PhoneLogger;
import taplinkbot.helpers.PhoneNumber;
import taplinkbot.repositories.PhoneLoggerRepository;

/**
 * Алгоритмы действий на сайте taplink.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class Actions {

    private final PhoneLoggerRepository phoneLoggerRep;

    private final AuthActions authActions;

    private final PhoneNumberActions phoneNumberActions;

    private final ProfileActions profileActions;

    private final MultiPageActions taplinkMultiPageActions;

    private final TapLinkAccount tapLinkAccount;

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
    synchronized public void setPhoneNumber(String phoneNumber, Profile profile) {

        //@Todo message from Hibernate validator
        if (!PhoneNumber.validate(phoneNumber)) {
            throw new TapLinkBotException("Номер телефона должен быть в формате +71234567890, передано:'" + phoneNumber + "'");
        }

        authActions.webLogin(tapLinkAccount.getLogin(), tapLinkAccount.getPassword());

        profileActions.changeTo(profile);

        try {
            phoneNumberActions.setPhoneNumber(phoneNumber);

            taplinkMultiPageActions.checkPhoneNumber(profile, phoneNumber);
        } catch (Exception e) {
            //@Todo use BotExceptions
            e.printStackTrace();
        }
    }

    /**
     * Проверяем оновную страницу.
     */
    synchronized public void multiPageControl(Profile profile) {

        String phoneNumber = getNumber(profile);

        phoneLoggerRep.save(new PhoneLogger(phoneNumber, profile));
    }

    synchronized public String getNumber(Profile profile) {

        return taplinkMultiPageActions.getNumber(profile);
    }

    synchronized public void testBugErrConnectionClosed() {

        browser.testBugErrConnectionClosed();
    }
}
