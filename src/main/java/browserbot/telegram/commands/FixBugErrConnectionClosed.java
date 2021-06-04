package browserbot.telegram.commands;

import browserbot.browser.Browser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import browserbot.telegram.*;

@Component
@RequiredArgsConstructor
@CommandClass(name = "/fix_bug_err_connection_closed")
public class FixBugErrConnectionClosed extends Command {

    private final Browser browser;

    @Override
    public String getDescription() {
        return "Проверяет что есть связь с браузереом и если её нет, перезагружает браузер.";
    }

    @Override
    public Reponse run(Request msg) {

        browser.testBugErrConnectionClosed();

        return MessageBuilder.buildResult();
    }
}
