//FIN
package browserbot.telegram.commands;

import browserbot.entities.Manager;
import browserbot.repositories.ManagerRepository;
import browserbot.telegram.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@CommandClass(name = "/manager_list")
public class ManagerList extends Command {

    private final ManagerRepository managerRepository;

    @Override
    public String getDescription() {
        return "Выведет список менеджеров.";
    }

    @Override
    public Reponse run(Request msg) {

        StringBuilder builder = new StringBuilder();

        List<Manager> all = managerRepository.findAll();

        all.forEach((Manager m) -> {
            builder.append(m.getId());
            builder.append(" - ");
            builder.append(m.getDescription());
            builder.append("\r\n");
        });

        return MessageBuilder.buildResult(builder.toString());
    }
}