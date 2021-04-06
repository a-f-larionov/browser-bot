package taplinkbot.bot;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import taplinkbot.repositories.PhoneLoggerRepository;
import taplinkbot.service.StateService;
import taplinkbot.telegram.TelegramBot;

@Component
public class CanvasRuComActions extends CommonActions {

    public CanvasRuComActions(Semaphore semaphore, TelegramBot telegram, DriverWrapper wrapper, StateService stateService, PhoneLoggerRepository phoneLoggerRepository, Environment env) {
        super(semaphore, telegram, wrapper, stateService, phoneLoggerRepository, env);
    }
}
