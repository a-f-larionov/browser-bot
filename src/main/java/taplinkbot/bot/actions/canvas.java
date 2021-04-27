package taplinkbot.bot.actions;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import taplinkbot.browser.DriverWrapper;
import taplinkbot.bot.Profile;
import taplinkbot.browser.Semaphore;
import taplinkbot.repositories.PhoneLoggerRepository;
import taplinkbot.service.StateService;
import taplinkbot.telegram.TelegramBot;

/**
 * Действия специфичные для профиля CanvasRuCom
 */
@Component
public class Canvas extends Common {

    public Canvas(Semaphore semaphore, TelegramBot telegram, DriverWrapper wrapper, StateService stateService, PhoneLoggerRepository phoneLoggerRepository, Environment env) {
        super(semaphore, telegram, wrapper, stateService, phoneLoggerRepository, env);
    }

    @Override
    protected Profile getProfile() {
        return Profile.Canvas;
    }
}
