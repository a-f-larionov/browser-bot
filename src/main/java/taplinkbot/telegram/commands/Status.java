//FIN
package taplinkbot.telegram.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import taplinkbot.bot.Profile;
import taplinkbot.schedulers.Trigger;
import taplinkbot.service.Settings;
import taplinkbot.telegram.*;

@Component
@RequiredArgsConstructor
@TelegramCommand(name = "/status")
public class Status implements CommandInterface {

    private final Settings settings;

    private final Trigger trigger;

    @Override
    public String getDescription() {
        return "Выведет настройки бота";
    }

    @Override
    public Response run(Message msg) {
        StringBuilder builder = new StringBuilder();

        builder.append("Расписание: \t");
        builder.append(settings.schedulerIsActive(msg.profile) ? "включено" : "выключено");

        builder.append("\r\n");

        builder.append("В будние дни: \t\t\t");
        builder.append(settings.allowWeekDays(msg.profile) ? "да" : "нет");

        builder.append("\r\n");

        builder.append("В быходные дни: \t\t\t");
        builder.append(settings.allowWeekEnds(msg.profile) ? "да" : "нет");

        builder.append("\r\n");
        builder.append(trigger.isItTimeToChange(Profile.Canvas) ? "скоро сработает" : "не сработает");
        builder.append(trigger.getConditions(Profile.Canvas).toString());

        return ResponseFactory.buildSuccessReponse(builder.toString());
    }
}
