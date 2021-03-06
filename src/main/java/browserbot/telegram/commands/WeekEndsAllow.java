
package browserbot.telegram.commands;

import browserbot.telegram.annotations.CommandClass;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import browserbot.services.Settings;
import browserbot.telegram.*;

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
    public Reponse run(Request msg) {

        settings.setAllowWeekEnds(msg.profile, true);

        return MessageBuilder.buildResult();
    }
}
