//FIN
package taplinkbot.telegram.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import taplinkbot.services.Settings;
import taplinkbot.telegram.*;

@Component
@RequiredArgsConstructor
@CommandClass(name = "/weekdays_allow")
public class WeekDaysAllow extends Command {

    private final Settings settings;

    @Override
    public String getDescription() {
        return "Разрешит работу бота в будние";
    }

    @Override
    public Message run(Request msg) {

        settings.setAllowWeekDays(msg.profile, true);

        return MessageBuilder.buildResult("Выполненно");
    }
}
