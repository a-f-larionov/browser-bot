package taplinkbot.bot;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import taplinkbot.repositories.PhoneLoggerRepository;
import taplinkbot.service.StateService;
import taplinkbot.telegram.TelegramBot;

@Component
public class CanvasActions extends CommonActions {

    public CanvasActions(Semaphore semaphore, TelegramBot telegram, DriverWrapper wrapper, StateService stateService, PhoneLoggerRepository phoneLoggerRepository, Environment env) {
        super(semaphore, telegram, wrapper, stateService, phoneLoggerRepository, env);
    }

    @Override
    protected Profile getProfile() {
        return Profile.Canvas;
    }

    @Override
    protected String getPageUrl() {
        return "https://canvas.ru.com/";
    }
}
