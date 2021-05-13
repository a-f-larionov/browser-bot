package taplinkbot.telegram.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import taplinkbot.service.Settings;
import taplinkbot.telegram.*;

@Component
@RequiredArgsConstructor
@CommandClass(name = "/set_manager_interval")
public class SetManagerInterval extends Command {

    private final Settings settings;

    @Override
    public String getDescription() {
        return "Установить интервал смены менеджеров";
    }

    @Override
    public Message run(Request msg) {

        long interval = Long.parseLong(msg.arg1);

        settings.setManagerInterval(msg.profile, interval);

        return MessageBuilder.buildResult();
    }
}
