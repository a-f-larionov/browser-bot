package taplinkbot.telegram.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import taplinkbot.bot.Actions;
import taplinkbot.telegram.*;

@Component
@RequiredArgsConstructor
@TelegramCommand(name = "/set_number")
public class SetNumber extends Command {

    private final TelegramBot telegramBot;

    private final Actions actions;

    @Override
    public String getDescription() {
        return "Установит переданный номер";
    }

    /**
     * @param msg
     * @return
     * @todo telegramBot to informator.send("message here",request.chatId);
     */
    @Override
    public Message run(Request msg) {

        //@todo validation
        if (!msg.arg1.matches("^\\+7\\d{10}$")) {
            return MessageBuilder.buildFailed("Номер телефона должен быть в формате +71234567890, передано:'" + msg.arg1 + "'");
        }

        telegramBot.sendMessage("Начинаю смену номера:" + msg.arg1, msg.chatId);

        actions.setPhoneNumber(msg.arg1, msg.profile);

        return MessageBuilder.buildSuccess();
    }
}
