package taplinkbot.schedulers;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import taplinkbot.bot.Cabinet2Actions;
import taplinkbot.bot.CanvasRuComActions;
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

    final private Logger log = LoggerFactory.getLogger(this.getClass());

    private final TelegramBot telegram;

    private final StateService stateService;

    private final Trigger trigger;

    private final ManagerRotator rotator;

    private final CanvasRuComActions canvasRuComActions;

    private final Cabinet2Actions cabinet2Actions;

    @Scheduled(cron = "0 * * * * 1-7")
    public void intervaled() {

        // onIdlePinger();

        onIdleCanvasRuCom();

        //onIdleCabinet2();
    }

    private void onIdlePinger() {

        stateService.setBotContext(BotContext.Ping);

        try {

            checkCanvas();

        } finally {
            stateService.setBotContext(null);
        }
    }

    private void onIdleCanvasRuCom() {

        stateService.setBotContext(BotContext.CanvasRuCom);

        Trigger.Conditions cond;
        try {
            cond = trigger.getConditions(Calendar.getInstance().getTimeInMillis());

            if (cond.isItTimeToChange) {

                System.out.println("Is it time to change!");

                Manager manager = rotator.getNextManager();

                trigger.updateLastTime();

                log.info("Установка менеджера(canvas.ru.com):" + manager.getDescription());

                setNewManager(manager, canvasRuComActions);

                log.info("Установка менеджера(canvas.ru.com):" + manager.getDescription());
            }
        } finally {
            stateService.setBotContext(null);
        }
    }


    private void onIdleCabinet2() {
        stateService.setBotContext(BotContext.Cabinet2);

        Trigger.Conditions cond;
        try {
            cond = trigger.getConditions(Calendar.getInstance().getTimeInMillis());

            if (cond.isItTimeToChange) {

                System.out.println("Is it time to change!");

                Manager manager = rotator.getNextManager();

                trigger.updateLastTime();

                log.info("Установка менеджера(cabinet2):" + manager.getDescription());

                String phone = cabinet2Actions.getNumber();

                log.info("phone number:" + phone);
                //setNewManager(manager, cabinet2Actions);

                manager = rotator.getPrevManager();

                log.info("Установка менеджера(cabinet2):" + manager.getDescription());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            stateService.setBotContext(null);
        }
    }

    private void setNewManager(Manager manager, CanvasRuComActions actions) {
        System.out.println("Scheduler setNewManager");
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

        canvasRuComActions.checkCanvas();

        System.out.println("pinger on idle");
    }
}

