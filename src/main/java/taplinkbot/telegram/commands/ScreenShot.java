package taplinkbot.telegram.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import taplinkbot.browser.Browser;
import taplinkbot.telegram.*;

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
    public Message run(Request msg) {
        return MessageBuilder.buildResult(browser.takeScreenshot());
    }
}
