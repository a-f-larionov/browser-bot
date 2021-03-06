package browserbot.telegram.commands;

import browserbot.browser.Browser;
import browserbot.telegram.*;
import browserbot.telegram.annotations.CommandClass;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@CommandClass(name = "/restart_browser")
public class RestartBrowser extends Command {

    private final Browser browser;

    @Override
    public String getDescription() {
        return "Выполняет перезагрузку бразуера";
    }

    @Override
    public Reponse run(Request msg) {

        browser.resetBrowser();

        return MessageBuilder.buildResult("Выполненно");
    }
}
