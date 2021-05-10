package taplinkbot.telegram.commands;

import org.springframework.stereotype.Component;
import taplinkbot.telegram.Command;
import taplinkbot.telegram.CommandInterface;
import taplinkbot.telegram.Message;
import taplinkbot.telegram.Response;

@Component
@Command(name = "/help")
public class Help implements CommandInterface {

    @Override
    public Response run(Message msg) {
        return new Response("/help - подсказка\r\n" +

                "/start - Запустить работу расписания /start\r\n" +
                "/stop - Остановить работу расписания /stop\r\n" +
                "/status - Узнать состояние бота /status\r\n" +

                "/set_number - Установить номер /set_number +71234567890\r\n" +

                "/everyday_allow - работать все дни\r\n" +
                "/everyday_disallow - работать не все дни\r\n" +

                "/weekends_allow - работать все выходные дни\r\n" +
                "/weekends_disallow - не работать в выходные дни\r\n"
        );
    }
}
