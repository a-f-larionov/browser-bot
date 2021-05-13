//FIN
package taplinkbot.telegram.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import taplinkbot.service.Settings;
import taplinkbot.telegram.*;

@Component
@RequiredArgsConstructor
@CommandClass(name = "/weekends_allow")
public class WeekEndsAllow extends Command {

    private final Settings settings;

    @Override
    public String getDescription() {
        return "Разрешит работу бота в выходные";
    }

    @Override
    public Message run(Request msg) {

        settings.setAllowWeekEnds(msg.profile, true);

        return MessageBuilder.buildResult();
    }
}
