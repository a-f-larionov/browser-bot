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

    private final Commands commands;

    @Override
    public String getDescription() {
        return "Включит расписание";
    }

    @Override
    public Response run(Message msg) {

        if (msg.arg1.equals(Message.noArgumentValue)) {

            settings.schedulerSetActive(msg.profile, true);

            //@todo execute
            Message message = new Message();
            message.profile = msg.profile;
            message.chatId = msg.chatId;
            message.cammand = "/status";

            //Commands.execute(CommandClass.class, profile, chatId);
            commands.execute(message);

            return new Response("Расписание запущено");

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

                return new Response("Расписание будет запущено, черзе" + minutes + " минут(у,ы)");

            } catch (NumberFormatException e) {

                return new Response("Не верный аргумент, " +
                        "должно быть целлое число минут. Передано `" + msg.arg1 + "`");
            }
        }
    }
}

