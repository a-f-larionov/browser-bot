package taplinkbot.telegram.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import taplinkbot.bot.Actions;
import taplinkbot.telegram.*;

@Component
@Command(name = "/set_number")
@RequiredArgsConstructor
public class SetNumber implements CommandInterface {

    private final TelegramBot telegramBot;

    private final Actions actions;

    @Override
    public Response run(Message msg) throws Exception {

        if (!msg.arg1.matches("^\\+7\\d{10}$")) {
            return new Response("Номер телефона должен быть в формате +71234567890, передано:'" + msg.arg1 + "'");
        }

        telegramBot.sendMessage("Начинаю смену номера:" + msg.arg1, msg.chatId);

        actions.setPhoneNumber(msg.arg1, msg.profile);

        return new Response("Команда выполненна");
    }
}
