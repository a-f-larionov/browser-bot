package taplinkbot.telegram.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import taplinkbot.bot.Actions;
import taplinkbot.telegram.*;

@Component
@RequiredArgsConstructor
@CommandClass(name = "/set_number")
public class SetNumber extends Command {

    private final TelegramBot telegramBot;

    private final Actions actions;

    @Override
    public String getDescription() {
        return "Установит переданный номер";
    }

    /**
     * @param req
     * @return
     * @todo telegramBot to informator.send("message here",request.initiatorChatId);
     */
    @Override
    public Message run(Request req) {

        //@todo validation
        if (!req.arg1.matches("^\\+7\\d{10}$")) {
            return MessageBuilder.buildAlert("Номер телефона должен быть в формате +71234567890, передано:'" + req.arg1 + "'");
        }

        telegramBot.notify(req, MessageBuilder.buildInfo("Начинаю смену номера:" + req.arg1));

        actions.setPhoneNumber(req.arg1, req.profile);

        return MessageBuilder.buildResult();
    }
}
