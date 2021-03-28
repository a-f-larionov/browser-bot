package taplinkbot.schedulers;

import taplinkbot.managers.Manager;
import taplinkbot.managers.ManagerRotator;
import taplinkbot.service.ManagerService;
import taplinkbot.schedulers.interavaled.IntervaledTrigger;
import taplinkbot.service.StateService;
import taplinkbot.telegram.TelegramBot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Calendar;

@Component
public class CanvasScheduler {

    final private Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private TelegramBot telegram;

    @Autowired
    private StateService stateService;

    @Autowired
    private IntervaledTrigger trigger;

    @Autowired
    private ManagerRotator rotator;

    @Autowired
    private ManagerService managerService;

    @Scheduled(cron = "0 * * * * 1-7")
    public void intervaled() {

        Calendar c = Calendar.getInstance();
        IntervaledTrigger.Conditions cond;
        cond = trigger.getConditions(Calendar.getInstance().getTimeInMillis());

        if (cond.isItTimeToChange) {

            System.out.println("Is it time to change!");

            Manager manager = rotator.getNextManager();

            trigger.updateLastTime();

            log.info("Установка нового менеджера:" + manager.getDescription());

            setNewManager(manager);
        }
    }

    /*
    @Scheduled(cron = "0 0  00,03,06,09,12,15,18,21 * * 1-4")
    @Scheduled(cron = "0 0  00,03,06,09,12,15,18    * * 5")
    public void weekdayA() {
        setNewManager(Manager.WeekdayA);
    }

    @Scheduled(cron = "0 0  01,04,07,10,13,16,19,22 * * 1-4")
    @Scheduled(cron = "0 0  01,04,07,10,13,16       * * 5")
    public void weekdayB() {
        setNewManager(Manager.WeekdayB);
    }

    @Scheduled(cron = "0 0  02,05,08,11,14,17,20,23 * * 1-4")
    @Scheduled(cron = "0 0  02,05,08,11,14,17       * * 5")
    public void weekdayC() {
        setNewManager(Manager.WeekdayC);
    }
*/

    public boolean isActive() {
        return stateService.schedulerIsActive();
    }

    public void start() {
        stateService.schedulerSetActive(true);
    }

    public void stop() {
        stateService.schedulerSetActive(false);
    }

    private void setNewManager(Manager manager) {
        System.out.println("Scheduler setNewManager");
        if (!stateService.schedulerIsActive()) {
            telegram.info("Расписание выключено, действие отменено:     " + manager.getDescription());
            return;
        } else {
            telegram.info("Смена номера по расписанию: " + manager.getDescription());
            managerService.setNewManager(manager);
        }
    }
}

