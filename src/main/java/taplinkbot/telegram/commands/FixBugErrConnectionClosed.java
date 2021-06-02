package taplinkbot.telegram.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import taplinkbot.bot.BotController;
import taplinkbot.telegram.*;

@Component
@RequiredArgsConstructor
@CommandClass(name = "/fix_bug_err_connection_closed")
public class FixBugErrConnectionClosed extends Command {

    private final BotController botController;

    @Override
    public String getDescription() {
        return "Проверяет что есть связь с браузереом и если её нет, перезагружает браузер.";
    }

    @Override
    public Message run(Request msg) {

        botController.testBugErrConnectionClosed();

        return MessageBuilder.buildResult();
    }
}
