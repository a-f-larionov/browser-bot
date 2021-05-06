package taplinkbot.bot;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import taplinkbot.browser.DriverWrapper;
import taplinkbot.entities.PhoneLogger;
import taplinkbot.helpers.PhoneNumber;
import taplinkbot.repositories.PhoneLoggerRepository;
import taplinkbot.service.StateService;
import taplinkbot.telegram.TelegramBot;

/**
 * Алгоритмы действий на сайте taplink.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class Actions {

    protected final TelegramBot telegram;

    protected final DriverWrapper browser;

    protected final StateService stateService;

    protected final BotContexts botContexts;

    protected final PhoneLoggerRepository phoneLoggerRep;

    protected final AuthActions authActions;

    protected final PhoneNumberActions phoneNumberActions;

    protected final ProfileActions profileActions;

    private final MultiPageActions taplinkMultiPageActions;

    protected final Environment env;

    protected final DataProvider dataProvider;

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

        if (!PhoneNumber.validate(phoneNumber)) {
            throw new BotException("Номер телефона должен быть в формате +71234567890, передано:'" + phoneNumber + "'");
        }

        authActions.signin(
                botContexts.current().getLogin(),
                botContexts.current().getPassword()
        );

        profileActions.change(botContexts.current());

        phoneNumberActions.setPhoneNumber(phoneNumber);

        taplinkMultiPageActions.checkPhoneNumber(phoneNumber);
    }

    /**
     * Контроль мульти страницы, номера телефона.
     *
     * @throws Exception
     */
    synchronized public void multiPageControl() throws Exception {

        String phoneNumber = getNumber();

        phoneLoggerRep.save(
                new PhoneLogger(phoneNumber, botContexts.current())
        );
    }

    synchronized public String getNumber() throws Exception {

        return taplinkMultiPageActions.getNumber();
    }
}
