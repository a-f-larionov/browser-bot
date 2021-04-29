package taplinkbot.schedulers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriverException;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import taplinkbot.bot.actions.Canvas;
import taplinkbot.bot.actions.Common;
import taplinkbot.bot.actions.LadyArt;
import taplinkbot.browser.DriverWrapper;
import taplinkbot.entities.Manager;
import taplinkbot.managers.ManagerRotator;
import taplinkbot.service.StateService;
import taplinkbot.telegram.BotContext;
import taplinkbot.telegram.TelegramBot;

@Component
@Scope(scopeName = ConfigurableBeanFactory.SCOPE_SINGLETON)
@RequiredArgsConstructor
@Slf4j
public class Scheduler {

    private final TelegramBot telegram;

    private final StateService stateService;

    private final Trigger trigger;

    private final ManagerRotator rotator;

    private final Canvas canvasActions;

    private final LadyArt ladyArtActions;

    private final DriverWrapper browser;

    @Scheduled(cron = "0 * * * * 1-7")

    public void intervaled() {

        if (stateService.isContextBusy()) {
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

            stateService.setBotContext(BotContext.Canvas);

            checkCanvas();

        } catch (Exception e) {
            if (e instanceof WebDriverException) {
                log.info(e.getMessage());
            }
            log.info("exception___");
            log.info(e.getMessage());
            if (e.getMessage().equals("unknown error: net::ERR_CONNECTION_CLOSED")) {
                //@todo
                browser.reset();
                log.info("driver reseted");
            }
            e.printStackTrace();
            //telegram.alert("Чтото пошло не так. не удалось проверить страницу" + stateService.getBotContext());
        } finally {
            stateService.setBotContext(null);
        }

    }

    private void onIdlePinger(BotContext botContext) {

        try {

            stateService.setBotContext(botContext);

            if (botContext == BotContext.Canvas) {
                checkCanvas();
            }

            if (botContext == BotContext.LadyArt) {
                checkLadyArt();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            stateService.setBotContext(null);
        }

    }

    private void onIdleManagerChange(BotContext botContext) {

        stateService.setBotContext(botContext);

        try {
            if (trigger.isItTimeToChange()) {

                Manager manager = rotator.getNextManager();

                trigger.updateLastTime();

                log.info("Установка менеджера(" + stateService.getBotContext().name + "):" + manager.getDescription());

                if (botContext == BotContext.Canvas)
                    setNewManager(manager, canvasActions);

                if (botContext == BotContext.LadyArt)
                    setNewManager(manager, ladyArtActions);
            }
        } finally {

            stateService.setBotContext(null);
        }
    }

    private void setNewManager(Manager manager, Common actions) {

        if (!stateService.schedulerIsActive()) {
            telegram.info("Расписание выключено, действие отменено: " + manager.getDescription() + stateService.getBotContext().name);
            return;
        }

        telegram.info("Смена номера: " + stateService.getBotContext().name + " " + manager.getDescription());
        actions.authAndUpdatePhone(manager.getPhone(), false, true);
    }


    private void checkCanvas() throws Exception {
        if (!stateService.schedulerIsActive()) return;

        canvasActions.checkPage();

        log.info("Pinger on idle. check canvas");
    }

    private void checkLadyArt() throws Exception {
        if (!stateService.schedulerIsActive()) return;

        ladyArtActions.checkPage();

        log.info("Pinger on idle. check lady art");
    }
}

