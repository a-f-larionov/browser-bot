package taplinkbot.bot;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import taplinkbot.repositories.PhoneLoggerRepository;
import taplinkbot.service.StateService;
import taplinkbot.telegram.TelegramBot;

@Component
public class LadyArtActions extends CommonActions {

    public LadyArtActions(Semaphore semaphore, TelegramBot telegram, DriverWrapper wrapper, StateService stateService, PhoneLoggerRepository phoneLoggerRepository, Environment env) {
        super(semaphore, telegram, wrapper, stateService, phoneLoggerRepository, env);
    }

    @Override
    protected Profile getProfile() {
        return Profile.LadyArt;
    }

    @Override
    protected String getPageUrl() {
        return "https://lady-art.art/";
    }
}
