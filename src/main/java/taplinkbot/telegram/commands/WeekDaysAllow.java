//FIN
package taplinkbot.telegram.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import taplinkbot.service.Settings;
import taplinkbot.telegram.*;

@Component
@RequiredArgsConstructor
@TelegramCommand(name = "/weekdays_allow")
public class WeekDaysAllow extends Command {

    private final Settings settings;

    @Override
    public String getDescription() {
        return "Разрешит работу бота в будние";
    }

    @Override
    public Response run(Message msg) {

        settings.setAllowWeekDays(msg.profile, true);

        return ResponseFactory.buildSuccessResponse("Выполненно");
    }
}
