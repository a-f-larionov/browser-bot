package taplinkbot.telegram.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import taplinkbot.bot.Actions;
import taplinkbot.telegram.*;

@Component
@RequiredArgsConstructor
@CommandClass(name = "/control_multi_pag")
public class ControlMultiPage extends Command {

    private final Actions actions;

    @Override
    public String getDescription() {
        return "Проверяет доступность страницы, номер и логирует его.";
    }

    @Override
    public Message run(Request msg) {

        actions.multiPageControl(msg.profile);

        return MessageBuilder.buildResult();
    }
}
