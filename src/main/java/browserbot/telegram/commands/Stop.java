//FIN
package browserbot.telegram.commands;

import browserbot.telegram.annotations.CommandClass;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import browserbot.services.Settings;
import browserbot.telegram.*;

@Component
@RequiredArgsConstructor
@CommandClass(name = "/stop")
public class Stop extends Command {

    private final Settings settings;

    @Override
    public String getDescription() {
        return "Выключит расписание";
    }

    @Override
    public Reponse run(Request msg) {

        settings.schedulerSetActive(msg.profile, false);

        return MessageBuilder.buildResult("Расписание выключенно");
    }
}
