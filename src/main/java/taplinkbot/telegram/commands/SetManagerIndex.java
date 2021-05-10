package taplinkbot.telegram.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import taplinkbot.managers.ManagerRotator;
import taplinkbot.telegram.Command;
import taplinkbot.telegram.CommandInterface;
import taplinkbot.telegram.Message;
import taplinkbot.telegram.Response;

@Component
@Command(name = "set_manager_index")
@RequiredArgsConstructor
public class SetManagerIndex implements CommandInterface {

    private final ManagerRotator managerRotator;

    @Override
    public Response run(Message msg) throws Exception {
        switch (msg.arg1) {
            case "0":
                managerRotator.setIndex(0);
                break;
            case "1":
                managerRotator.setIndex(1);
                break;
            case "2":
                managerRotator.setIndex(2);
                break;

        }
        return new Response("Выполненно");
    }
}
