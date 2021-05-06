package taplinkbot.schedulers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriverException;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import taplinkbot.bot.Actions;
import taplinkbot.bot.Context;
import taplinkbot.bot.BotContexts;
import taplinkbot.browser.DriverWrapper;
import taplinkbot.entities.Manager;
import taplinkbot.managers.ManagerRotator;
import taplinkbot.service.StateService;
import taplinkbot.telegram.TelegramBot;

@Component
@Scope(scopeName = ConfigurableBeanFactory.SCOPE_SINGLETON)
@RequiredArgsConstructor
@Slf4j
public class Scheduler {

    private final TelegramBot telegram;

    private final StateService stateService;

    private final BotContexts botContexts;

    private final Trigger trigger;

    private final ManagerRotator rotator;

    private final Actions actions;

    private final DriverWrapper browser;

    @Scheduled(cron = "0 * * * * 1-7")
    public void intervaled() {

        try {
            onIdleBrowserReachable();

            onIdleManagerChange(Context.Canvas);

            onIdleManagerChange(Context.LadyArt);

            onIdlePinger(Context.Canvas);

            onIdlePinger(Context.LadyArt);

        } catch (Exception e) {
            //@Todo create annotation @BotExceptionHandler
            telegram.alert(e.getMessage());
        }
    }

    private void onIdleBrowserReachable() {

        try {
            botContexts.setCurrent(Context.Canvas);

            checkPage();

        } catch (Exception e) {
            if (e instanceof WebDriverException) {
                log.debug(e.getMessage());
            }
            if (e.getMessage().equals("unknown error: net::ERR_CONNECTION_CLOSED")) {
                browser.reset();
                log.info("Web Driver перезапущен.");
            }
            e.printStackTrace();
        } finally {

            botContexts.setCurrent(null);
        }

    }

    private void onIdlePinger(Context botContext) throws Exception {

        botContexts.setCurrent(botContext);

        checkPage();
    }

    private void onIdleManagerChange(Context botContext) throws Exception {

        if (trigger.isItTimeToChange()) {

            botContexts.setCurrent(botContext);

            Manager manager = rotator.getNextManager();

            log.info("Установка менеджера(" + botContexts.current().name + "):" + manager.getDescription());

            setNewManager(manager);

            trigger.updateLastTime();
        }
    }

    private void setNewManager(Manager manager) throws Exception {

        telegram.info("Смена номера: " + botContexts.current().name + " " + manager.getDescription());

        actions.setPhoneNumber(manager.getPhone());
    }

    private void checkPage() throws Exception {

        actions.multiPageControl();
    }
}

