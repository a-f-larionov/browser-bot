//FIN
package taplinkbot.telegram.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import taplinkbot.service.Settings;
import taplinkbot.telegram.*;

@Component
@TelegramCommand(name = "/weekdays_allow")
@RequiredArgsConstructor
public class WeekDaysAllow implements CommandInterface {

    private final Settings settings;

    @Override
    public String getDescription() {
        return "Разрешит работу бота в будние";
    }

    @Override
    public Response run(Message msg) {

        settings.setAllowWeekDays(true);

        return ResponseFactory.buildSuccessReponse("Выполненно");
    }
}
