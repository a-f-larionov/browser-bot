package browserbot.telegram.commands;

import browserbot.entities.Manager;
import browserbot.services.ManagerRotator;
import browserbot.telegram.*;
import browserbot.telegram.annotations.CommandClass;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@CommandClass(name = "/manager_skip")
public class SkipManager extends Command {

    private final ManagerRotator rotator;

    @Override
    public String getDescription() {
        return "Пропустить одного менеджера";
    }

    @Override
    public Reponse run(Request msg) {

        Manager manager = rotator.getCurrentManager(msg.profile);

        rotator.setNextManager(msg.profile, manager.getId());

        return MessageBuilder.buildResult(rotator.getCurrentManager(msg.profile).getDescription());
    }
}
