package browserbot.telegram.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import browserbot.browser.Browser;
import browserbot.telegram.*;

@Component
@RequiredArgsConstructor
@CommandClass(name = "/screenshot")
public class ScreenShot extends Command {

    private final Browser browser;

    @Override
    public String getDescription() {
        return "Возвращает ссылку на скриншот";
    }

    @Override
    public Reponse run(Request msg) {
        return MessageBuilder.buildResult(browser.takeScreenshot());
    }
}
