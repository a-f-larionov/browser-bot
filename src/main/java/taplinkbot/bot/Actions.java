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

    private final PhoneLoggerRepository phoneLoggerRep;

    private final AuthActions authActions;

    private final PhoneNumberActions phoneNumberActions;

    private final ProfileActions profileActions;

    private final MultiPageActions taplinkMultiPageActions;

    private final Profiles profiles;

    private final TaplinkAcсount taplinkAcсount;

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

        profiles.set(profile);

        //@Todo message from Hibernate validator
        if (!PhoneNumber.validate(phoneNumber)) {
            throw new BotException("Номер телефона должен быть в формате +71234567890, передано:'" + phoneNumber + "'");
        }

        authActions.webLogin(taplinkAcсount.getLogin(), taplinkAcсount.getPassword());

        profileActions.changeTo(profile);

        try {
            phoneNumberActions.setPhoneNumber(phoneNumber);

            taplinkMultiPageActions.checkPhoneNumber(phoneNumber);
        }catch (Exception e){
            //@Todo use BotExceptions
            e.printStackTrace();
        }
    }

    /**
     * Проверяем оновную страницу.
     *
     * @throws Exception ошибки бота и вебдрайвера
     */
    synchronized public void multiPageControl(Profile profile) throws BotException {

        String phoneNumber = getNumber(profile);

        phoneLoggerRep.save(new PhoneLogger(phoneNumber, profile));
    }

    synchronized public String getNumber(Profile profile) throws BotException {

        return taplinkMultiPageActions.getNumber(profile);
    }
}
