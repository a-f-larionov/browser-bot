package taplinkbot.service;

import org.springframework.stereotype.Service;
import taplinkbot.bot.Semaphore;
import taplinkbot.bot.CanvasRuComActions;
import taplinkbot.managers.Manager;
import taplinkbot.telegram.TelegramBot;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class ManagerService {

    @Autowired
    private TelegramBot telegram;

    @Autowired
    private Semaphore semaphore;

    @Autowired
    private CanvasRuComActions actions;

    public void setNewManager(Manager manager) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss d m");
        String date = simpleDateFormat.format(new Date());
        System.out.println(date + manager.getComment() + " " + manager.getPhone());

        if (!semaphore.isntLocked()) {
            telegram.info("Заблокирован бот, менеджер не сменяется:" + manager.getDescription());
            return;
        }

        actions.authAndUpdatePhone(manager.getPhone(), false, true);
    }
}
