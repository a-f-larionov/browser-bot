package taplinkbot.schedulers;

import taplinkbot.bot.CanvasRuComActions;
import taplinkbot.managers.Manager;
import taplinkbot.managers.ManagerRotator;
import taplinkbot.service.ManagerService;
import taplinkbot.schedulers.interavaled.Trigger;
import taplinkbot.service.StateService;
import taplinkbot.telegram.BotContext;
import taplinkbot.telegram.TelegramBot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Calendar;

@Component
public class Scheduler {

    final private Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private TelegramBot telegram;

    @Autowired
    private StateService stateService;

    @Autowired
    private Trigger trigger;

    @Autowired
    private ManagerRotator rotator;

    @Autowired
    private ManagerService managerService;

    @Autowired
    private CanvasRuComActions canvasRuComActions;

    @Scheduled(cron = "0 * * * * 1-7")
    public void intervaled() {

        onIdlePinger();

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

                setNewManager(manager);

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

                Manager manager = rotator.getCurrentManager();

                trigger.updateLastTime();

                log.info("Установка менеджера(cabinet2):" + manager.getDescription());

                setNewManager(manager);

                log.info("Установка менеджера(cabinet2):" + manager.getDescription());
            }
        } finally {
            stateService.setBotContext(null);
        }
    }

    private void setNewManager(Manager manager) {
        System.out.println("Scheduler setNewManager");
        if (!stateService.schedulerIsActive()) {
            telegram.info("Расписание выключено, действие отменено:     " + manager.getDescription());
            return;
        } else {
            telegram.info("Смена номера: " + stateService.getBotContext().stringNames[0] + " " + manager.getDescription());
            managerService.setNewManager(manager);
        }
    }

    private void checkCanvas() {
        if (!stateService.schedulerIsActive()) return;

        canvasRuComActions.checkCanvas();

        System.out.println("pinger on idle");
    }
}

