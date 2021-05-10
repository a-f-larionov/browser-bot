package taplinkbot.telegram.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import taplinkbot.service.StateService;
import taplinkbot.telegram.Command;
import taplinkbot.telegram.CommandInterface;
import taplinkbot.telegram.Message;
import taplinkbot.telegram.Response;

@Component
@Command(name = "/weekdays_allow")
@RequiredArgsConstructor
public class WeekDaysAllow implements CommandInterface {

    private final StateService stateService;

    @Override
    public Response run(Message msg) throws Exception {

        stateService.setAllowWeekDays(true);

        return new Response("Выполненно");
    }
}
