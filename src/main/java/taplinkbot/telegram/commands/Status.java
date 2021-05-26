//FIN
package taplinkbot.telegram.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import taplinkbot.schedulers.Trigger;
import taplinkbot.service.Settings;
import taplinkbot.telegram.*;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

@Component
@RequiredArgsConstructor
@CommandClass(name = "/status")
public class Status extends Command {

    private final Settings settings;

    private final Trigger trigger;

    @Override
    public String getDescription() {
        return "Выведет настройки бота";
    }

    @Override
    public Message run(Request msg) {
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

        builder.append("Интервал: ");
        builder.append(settings.getManagerInterval(msg.profile));

        builder.append("\r\n");

        builder.append(trigger.isItTimeToChange(msg.profile) ? "скоро сработает" : "не сработает");
        builder.append(trigger.getConditions(msg.profile).toString());

        builder.append("\r\n");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm");
        //DateTimeFormatter customFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy 'at' hh:mma z");

        builder.append("След срабатывание:" + trigger.getNext(msg.profile));

        return MessageBuilder.buildResult(builder.toString());
    }
}
