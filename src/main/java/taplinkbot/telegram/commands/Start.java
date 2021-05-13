package taplinkbot.telegram.commands;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import taplinkbot.service.Settings;
import taplinkbot.telegram.*;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
@RequiredArgsConstructor
@Slf4j
@TelegramCommand(name = "/start")
public class Start extends Command {

    private final Settings settings;

    private final CommandExecutor commandExecutor;

    @Override
    public String getDescription() {
        return "Включит расписание";
    }

    @Override
    public Message run(Request msg) {

        if (msg.arg1.equals(Request.noArgumentValue)) {

            settings.schedulerSetActive(msg.profile, true);

            //@todo execute
            Request request = new Request();
            request.profile = msg.profile;
            request.chatId = msg.chatId;
            request.command = "/status";

            //Commands.execute(CommandClass.class, profile, chatId);
            commandExecutor.execute(request);

            return MessageBuilder.buildSuccess("Расписание запущено.");

        } else {

            try {
                long minutes = Long.parseLong(msg.arg1);
                log.info(Long.toString(minutes));
                log.info("argument" + msg.arg1);

                ExecutorService service = Executors.newCachedThreadPool();

                service.submit(() -> {
                    try {
                        Thread.sleep(minutes * 60 * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    //@Todo
                    //telegram.info("Расписание запущено по таймаута. Запрос минут назад: " + minutes);

                    settings.schedulerSetActive(msg.profile, true);
                });

                return MessageBuilder.buildFailed("Расписание будет запущено, черзе" + minutes + " минут(у,ы)");

            } catch (NumberFormatException e) {

                return MessageBuilder.buildFailed("Не верный аргумент, " +
                        "должно быть целлое число минут. Передано `" + msg.arg1 + "`");
            }
        }
    }
}

