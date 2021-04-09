package taplinkbot.schedulers;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import taplinkbot.bot.CommonActions;
import taplinkbot.bot.LadyArtActions;
import taplinkbot.bot.CanvasActions;
import taplinkbot.managers.Manager;
import taplinkbot.managers.ManagerRotator;
import taplinkbot.schedulers.interavaled.Trigger;
import taplinkbot.service.StateService;
import taplinkbot.telegram.BotContext;
import taplinkbot.telegram.TelegramBot;

import java.util.Calendar;

@Component
@Scope(scopeName = ConfigurableBeanFactory.SCOPE_SINGLETON)
@RequiredArgsConstructor
public class Scheduler {

    private static final Logger log = LoggerFactory.getLogger(Scheduler.class);

    private final TelegramBot telegram;

    private final StateService stateService;

    private final Trigger trigger;

    private final ManagerRotator rotator;

    private final CanvasActions canvasActions;

    private final LadyArtActions ladyArtActions;

    @Scheduled(cron = "0 * * * * 1-7")
    public void intervaled() {

        if (stateService.getBotContext() != null) {
            log.info("skip because bot context busy");
            return;
        }

        onIdlePinger();

        onIdleCanvas();

        onIdleLadyArt();
    }

    private void onIdlePinger() {

        stateService.setBotContext(BotContext.Ping);

        try {

            checkCanvas();

        } finally {
            stateService.setBotContext(null);
        }
    }

    private void onIdleCanvas() {

        stateService.setBotContext(BotContext.Canvas);

        Trigger.Conditions cond;
        try {
            cond = trigger.getConditions(Calendar.getInstance().getTimeInMillis());

            if (cond.isItTimeToChange) {

                log.info("Is it time to change!");

                Manager manager = rotator.getNextManager();

                trigger.updateLastTime();

                log.info("Установка менеджера(" + stateService.getBotContext().name + "):" + manager.getDescription());

                setNewManager(manager, canvasActions);

                log.info("Установка менеджера(" + stateService.getBotContext().name + "):" + manager.getDescription());
            }
        } finally {
            stateService.setBotContext(null);
        }
    }

    private void onIdleLadyArt() {
        stateService.setBotContext(BotContext.LadyArt);

        Trigger.Conditions cond;
        try {
            cond = trigger.getConditions(Calendar.getInstance().getTimeInMillis());

            if (cond.isItTimeToChange) {

                log.info("Is it time to change!");

                Manager manager = rotator.getNextManager();

                trigger.updateLastTime();

                log.info("Установка менеджера(" + stateService.getBotContext().name + "):" + manager.getDescription());

                String phone = ladyArtActions.getNumber();

                setNewManager(manager, ladyArtActions);

                log.info("Установка менеджера(" + stateService.getBotContext().name + "):" + manager.getDescription());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            stateService.setBotContext(null);
        }
    }

    private void setNewManager(Manager manager, CommonActions actions) {
        log.info("Scheduler setNewManager");
        if (!stateService.schedulerIsActive()) {
            telegram.info("Расписание выключено, действие отменено: " + manager.getDescription() + stateService.getBotContext().name);
            return;
        } else {
            telegram.info("Смена номера: " + stateService.getBotContext().name + " " + manager.getDescription());
            actions.authAndUpdatePhone(manager.getPhone(), false, true);
        }
    }


    private void checkCanvas() {
        if (!stateService.schedulerIsActive()) return;

        canvasActions.checkCanvas();

        log.info("pinger on idle");
    }
}

