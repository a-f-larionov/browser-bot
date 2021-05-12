//FIN
package taplinkbot.telegram.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import taplinkbot.service.Settings;
import taplinkbot.telegram.*;

@Component
@RequiredArgsConstructor
@TelegramCommand(name = "/stop")
public class Stop implements CommandInterface {

    private final Settings settings;

    @Override
    public String getDescription() {
        return "Выключит расписание";
    }

    @Override
    public Response run(Message msg) {

        settings.schedulerSetActive(msg.profile, false);

        return ResponseFactory.buildSuccessReponse("Расписание выключенно");
    }
}
