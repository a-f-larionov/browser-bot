package taplinkbot.telegram.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import taplinkbot.service.StateService;
import taplinkbot.telegram.Command;
import taplinkbot.telegram.CommandInterface;
import taplinkbot.telegram.Message;
import taplinkbot.telegram.Response;

@Component
@Command(name = "/status")
@RequiredArgsConstructor
public class Status implements CommandInterface {

    private final StateService stateService;

    @Override
    public Response run(Message msg) {
        String message;

        //@todo multilanguage mechanizm
        message = "\r\nРасписание: \t" + (stateService.schedulerIsActive() ? "включено" : "выключено");
        message += "\r\nВО ВСЕ ДНИ без исключения: \t" + (stateService.allowEveryDay() ? "да" : "нет");
        message += "\r\nВ будние дни: \t\t\t" + (stateService.allowWeekDays() ? "да" : "нет");
        message += "\r\nВ быходные дни: \t\t\t" + (stateService.allowWeekEnds() ? "да" : "нет");

        return new Response(message);
    }
}
