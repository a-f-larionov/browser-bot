//FIN
package taplinkbot.telegram.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import taplinkbot.service.Settings;
import taplinkbot.telegram.*;

@Component
@RequiredArgsConstructor
@TelegramCommand(name = "/weekends_disallow")
public class WeekEndsDisallow implements CommandInterface {

    private final Settings settings;

    @Override
    public String getDescription() {
        return "Запретит работу бота в выходные";
    }

    /**
     * Запретить работу бота в выходные.
     */
    @Override
    public Response run(Message msg) {

        settings.setAllowWeekEnds(msg.profile, false);

        return ResponseFactory.buildSuccessReponse();
    }
}