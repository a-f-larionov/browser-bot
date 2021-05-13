//FIN
package taplinkbot.telegram.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import taplinkbot.service.Settings;
import taplinkbot.telegram.*;

@Component
@RequiredArgsConstructor
@TelegramCommand(name = "/stop")
public class Stop extends Command {

    private final Settings settings;

    @Override
    public String getDescription() {
        return "Выключит расписание";
    }

    @Override
    public Message run(Request msg) {

        settings.schedulerSetActive(msg.profile, false);

        return MessageBuilder.buildSuccess("Расписание выключенно");
    }
}
