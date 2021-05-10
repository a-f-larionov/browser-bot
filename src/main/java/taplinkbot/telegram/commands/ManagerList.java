package taplinkbot.telegram.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import taplinkbot.entities.Manager;
import taplinkbot.managers.ManagerRotator;
import taplinkbot.telegram.Command;
import taplinkbot.telegram.CommandInterface;
import taplinkbot.telegram.Message;
import taplinkbot.telegram.Response;

@Component
@Command(name = "/manager_list")
@RequiredArgsConstructor
public class ManagerList implements CommandInterface {

    private final ManagerRotator managerRotator;

    @Override
    public Response run(Message msg) throws Exception {

        String message;
        message = "";

        Manager[] manager = managerRotator.getList();

        for (int i = 0; i < manager.length; i++) {
            message += i + " - " + manager[i].getDescription() + "\r\n";
        }

        return new Response(message);
    }
}
