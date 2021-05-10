package taplinkbot.telegram.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import taplinkbot.service.StateService;
import taplinkbot.telegram.Command;
import taplinkbot.telegram.CommandInterface;
import taplinkbot.telegram.Message;
import taplinkbot.telegram.Response;

@Component
@Command(name = "/stop")
@RequiredArgsConstructor
public class Stop implements CommandInterface {

    private final StateService stateService;

    @Override
    public Response run(Message msg) {

        stateService.schedulerSetActive(false);

        return new Response("Расписание выключенно");
    }
}
