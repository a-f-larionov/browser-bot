package taplinkbot.telegram.commands;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import taplinkbot.service.StateService;
import taplinkbot.telegram.*;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
@Command(name = "/start")
@RequiredArgsConstructor
@Slf4j
public class Start implements CommandInterface {

    private final StateService stateService;

    private final Commands commands;

    @Override
    public Response run(Message msg) throws Exception {

        if (msg.arg1.equals(Message.noArgumentValue)) {

            stateService.schedulerSetActive(true);

            //@todo execute
            Message message = new Message();
            message.profile = msg.profile;
            message.chatId = msg.chatId;
            message.cammand = "/status";

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

                    stateService.schedulerSetActive(true);
                });

                return new Response("Расписание будет запущено, черзе" + minutes + " минут(у,ы)");

            } catch (NumberFormatException e) {

                return new Response("Не верный аргумент, " +
                        "должно быть целлое число минут. Передано `" + msg.arg1 + "`");
            }
        }
    }
}
