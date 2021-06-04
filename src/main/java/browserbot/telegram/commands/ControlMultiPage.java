package browserbot.telegram.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import browserbot.bots.taplink.BotController;
import browserbot.telegram.*;

@Component
@RequiredArgsConstructor
@CommandClass(name = "/control_multi_pag")
public class ControlMultiPage extends Command {

    private final BotController botController;

    @Override
    public String getDescription() {
        return "Проверяет доступность страницы, номер и логирует его.";
    }

    @Override
    public Reponse run(Request msg) {

        botController.multiPageControl(msg.profile);

        return MessageBuilder.buildResult();
    }
}
