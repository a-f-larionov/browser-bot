//FIN
package taplinkbot.telegram.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import taplinkbot.service.Settings;
import taplinkbot.telegram.*;

@Component
@RequiredArgsConstructor
@TelegramCommand(name = "/weekdays_disallow")
public class WeekDaysDisallow extends Command {

    private final Settings settings;

    @Override
    public String getDescription() {
        return "Запретит работу бота в будние";
    }

    @Override
    public Message run(Request msg) {

        settings.setAllowWeekDays(msg.profile, false);

        return MessageBuilder.buildSuccess();
    }
}
