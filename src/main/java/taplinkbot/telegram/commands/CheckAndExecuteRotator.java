package taplinkbot.telegram.commands;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import taplinkbot.bot.Actions;
import taplinkbot.browser.Browser;
import taplinkbot.entities.Manager;
import taplinkbot.managers.ManagerRotator;
import taplinkbot.schedulers.Trigger;
import taplinkbot.telegram.*;

@Component
@RequiredArgsConstructor
@Slf4j
@CommandClass(name = "/check_and_execute_rotator")
public class CheckAndExecuteRotator extends Command {

    private final ManagerRotator rotator;

    private final Actions actions;

    private final Trigger trigger;

    private final TelegramBot telegram;

    private final Browser browser;

    @Override
    public String getDescription() {
        return "Если настало время, выполнит смену менеджера.";
    }

    @Override
    public Message run(Request req) {

        if (!trigger.isItTimeToChange(req.profile)) {
            return MessageBuilder.buildResult("Рано");
        }

        Manager manager = rotator.getNextManager(req.profile);

        log.info("Установка менеджера(" + req.profile.name + "):" + manager.getDescription());

        telegram.notify(req, MessageBuilder.buildInfo("Начинаю снену номера: " + req.profile.name + " " + manager.getDescription()));

        if (actions.setPhoneNumber(manager.getPhone(), req.profile)) {

            telegram.notify(req, MessageBuilder.buildInfo(
                    "Номер на странице " +
                            browser.getCurrentUrl() +
                            " соответствует:" +
                            manager.getPhone() +
                            " " + browser.takeScreenshot()
            ));

            trigger.updateLastTime(req.profile);

            browser.resetBrowser();

            return MessageBuilder.buildResult();

        } else {

            return MessageBuilder.buildAlert("Номер не удалось сменить");
        }
    }
}
