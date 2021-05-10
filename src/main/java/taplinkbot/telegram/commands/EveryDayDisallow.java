package taplinkbot.telegram.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import taplinkbot.service.StateService;
import taplinkbot.telegram.Command;
import taplinkbot.telegram.CommandInterface;
import taplinkbot.telegram.Message;
import taplinkbot.telegram.Response;

@Component
@Command(name = "/every_day_disallow")
@RequiredArgsConstructor
public class EveryDayDisallow implements CommandInterface {

    private final StateService stateService;

    @Override
    public Response run(Message msg) throws Exception {

        stateService.setIsEveryDay(false);

        return new Response("Выполненно");
    }
}