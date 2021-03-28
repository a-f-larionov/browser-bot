package taplinkbot.service;

import org.springframework.stereotype.Service;
import taplinkbot.bot.BotSemaphore;
import taplinkbot.bot.CanvasRuComActions;
import taplinkbot.managers.Manager;
import taplinkbot.telegram.TelegramBot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class ManagerService {

    @Autowired
    private TelegramBot telegram;

    @Autowired
    private BotSemaphore semaphore;

    @Autowired
    private CanvasRuComActions actions;

    public void setNewManager(Manager manager) {

        String pattern = " HH:mm:ss d m";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String date = simpleDateFormat.format(new Date());
        System.out.println(date + manager.getComment() + " " + manager.getPhone());

        if (!semaphore.isntLocked()) {
            telegram.info("Заблокирован бот, менеджер не сменяется:" + manager.getDescription());
            return;
        }

        actions.authAndUpdatePhone(manager.getPhone(), false, true);
    }
}
