//FIN
package browserbot.telegram.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import browserbot.services.Settings;
import browserbot.telegram.*;

@Component
@RequiredArgsConstructor
@CommandClass(name = "/weekends_disallow")
public class WeekEndsDisallow extends Command {

    private final Settings settings;

    @Override
    public String getDescription() {
        return "Запретит работу бота в выходные";
    }

    /**
     * Запретить работу бота в выходные.
     */
    @Override
    public Reponse run(Request msg) {

        settings.setAllowWeekEnds(msg.profile, false);

        return MessageBuilder.buildResult();
    }
}