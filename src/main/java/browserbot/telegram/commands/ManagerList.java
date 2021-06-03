package browserbot.telegram.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import browserbot.entities.Manager;
import browserbot.services.ManagerRotator;
import browserbot.telegram.*;

@Component
@CommandClass(name = "/manager_list")
@RequiredArgsConstructor
public class ManagerList extends Command {

    private final ManagerRotator managerRotator;

    @Override
    public String getDescription() {
        return "Выведет список менеджеров.";
    }

    @Override
    //@todo StringBuilder
    public Message run(Request msg) {

        StringBuilder builder = new StringBuilder();

        Manager[] manager = managerRotator.getManagers();

        for (int i = 0; i < manager.length; i++) {

            builder.append(i);
            builder.append(" - ");
            builder.append(manager[i].getDescription());
            builder.append("\r\n");
        }

        return MessageBuilder.buildResult(builder.toString());
    }
}
