//FIN
package taplinkbot.telegram.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import taplinkbot.services.Settings;
import taplinkbot.telegram.*;

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
    public Message run(Request msg) {

        settings.schedulerSetActive(msg.profile, false);

        return MessageBuilder.buildResult("Расписание выключенно");
    }
}
