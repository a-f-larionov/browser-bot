package taplinkbot.bot;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
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

    private final Profiles profiles;

    private final PhoneLoggerRepository phoneLoggerRep;

    private final AuthActions authActions;

    private final PhoneNumberActions phoneNumberActions;

    private final ProfileActions profileActions;

    private final MultiPageActions taplinkMultiPageActions;

    /**
     * Установить номер телефона
     * 1 - Авторизуется
     * 2 - Выбирает профиль
     * 3 - Устонавливает номер
     * 4 - Проверяет мультистраницу
     *
     * @param phoneNumber номер телефона
     */
    synchronized public void setPhoneNumber(String phoneNumber) throws Exception {

        Profile profile = profiles.current();

        if (!PhoneNumber.validate(phoneNumber)) {
            throw new BotException("Номер телефона должен быть в формате +71234567890, передано:'" + phoneNumber + "'");
        }

        authActions.webLogin(profile.getLogin(), profile.getPassword());

        profileActions.changeTo(profile);

        phoneNumberActions.setPhoneNumber(phoneNumber);

        taplinkMultiPageActions.checkPhoneNumber(phoneNumber);
    }

    /**
     * Контроль мульти страницы, номера телефона.
     *
     * @throws Exception ошибки бота и вебдрайвера
     */
    synchronized public void multiPageControl(Profile profile) throws Exception {

        String phoneNumber = getNumber(profile);

        phoneLoggerRep.save(
                new PhoneLogger(phoneNumber, profile)
        );
    }

    synchronized public String getNumber(Profile profile) throws Exception {

        return taplinkMultiPageActions.getNumber(profile);
    }
}
