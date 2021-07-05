package browserbot.telegram.commands;

import browserbot.telegram.annotations.CommandClass;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import browserbot.services.Settings;
import browserbot.telegram.*;

@Component
@RequiredArgsConstructor
@Slf4j
@CommandClass(name = "/start")
public class Start extends Command {

    private final Settings settings;

    private final CommandExecutor commandExecutor;

    @Override
    public String getDescription() {
        return "Включает расписание";
    }

    @Override
    public Reponse run(Request msg) {

        settings.schedulerSetActive(msg.profile, true);

        //@todo execute
        Request request = new Request();
        request.profile = msg.profile;
        request.initiatorChatId = msg.initiatorChatId;
        request.command = "/status";

        //@todo
        commandExecutor.execute(request);

        return MessageBuilder.buildResult("Расписание запущено.");
    }
}

