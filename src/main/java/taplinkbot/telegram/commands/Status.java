//FIN
package taplinkbot.telegram.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import taplinkbot.service.Settings;
import taplinkbot.telegram.*;

@Component
@RequiredArgsConstructor
@TelegramCommand(name = "/status")
public class Status implements TelegramCommandInterface {

    private final Settings settings;

    @Override
    public String getDescription() {
        return "Выведет настройки бота";
    }

    @Override
    public Response run(Message msg) {
        StringBuilder builder = new StringBuilder();

        builder.append("Расписание: \t");
        builder.append(settings.schedulerIsActive() ? "включено" : "выключено");

        builder.append("\r\n");

        builder.append("В будние дни: \t\t\t");
        builder.append(settings.allowWeekDays() ? "да" : "нет");

        builder.append("\r\n");

        builder.append("В быходные дни: \t\t\t");
        builder.append(settings.allowWeekEnds() ? "да" : "нет");

        return ResponseFactory.buildSuccessReponse(builder.toString());
    }
}
