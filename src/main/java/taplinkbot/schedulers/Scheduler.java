package taplinkbot.schedulers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriverException;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import taplinkbot.bot.Actions;
import taplinkbot.bot.BotContext;
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

        if (botContexts.isBusy()) {
            log.info("skip because bot context busy");
            return;
        }

        onIdleBrowserReacheble();

        onIdleManagerChange(BotContext.Canvas);

        onIdleManagerChange(BotContext.LadyArt);

        onIdlePinger(BotContext.Canvas);

        onIdlePinger(BotContext.LadyArt);
    }

    private void onIdleBrowserReacheble() {

        try {

            botContexts.setCurrent(BotContext.Canvas);

            checkPage();

        } catch (Exception e) {
            if (e instanceof WebDriverException) {
                log.info(e.getMessage());
            }
            if (e.getMessage().equals("unknown error: net::ERR_CONNECTION_CLOSED")) {
                browser.reset();
                log.info("driver reseted");
            }
            e.printStackTrace();
        } finally {

            botContexts.setCurrent(null);
        }

    }

    private void onIdlePinger(BotContext botContext) {

        try {

            botContexts.setCurrent(botContext);

            checkPage();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            botContexts.setCurrent(null);
        }

    }

    private void onIdleManagerChange(BotContext botContext) {

        botContexts.setCurrent(botContext);

        try {
            if (trigger.isItTimeToChange()) {

                Manager manager = rotator.getNextManager();

                trigger.updateLastTime();

                log.info("Установка менеджера(" + botContexts.getCurrent().name + "):" + manager.getDescription());

                setNewManager(manager);
            }
        } finally {

            botContexts.setCurrent(null);
        }
    }

    private void setNewManager(Manager manager) {

        if (!stateService.schedulerIsActive()) {
            telegram.info("Расписание выключено, действие отменено: " +
                    manager.getDescription() + botContexts.getCurrent().name);
            return;
        }

        telegram.info("Смена номера: " + botContexts.getCurrent().name + " " + manager.getDescription());

        actions.authAndUpdatePhone(manager.getPhone(), false, true);
    }

    private void checkPage() throws Exception {

        if (!stateService.schedulerIsActive()) return;

        actions.testMultiPage();
    }
}

