package taplinkbot.telegram;

import org.springframework.stereotype.Component;
import taplinkbot.bot.BotContexts;

@Component
public class Parser {

    public Message parse(String text, String chatId) throws ClientRequestException {

        Message msg = new Message();

        msg.args = text.replace("@tap_link_bot", "")
                .replace("  ", " ")
                .replace("  ", " ")
                .replace("  ", " ")
                .replace("  ", " ")
                .split(" ");

        if (msg.args.length == 0) throw new ClientRequestException("Неверная команда. см. /help");
        if (msg.args.length < 2)
            throw new ClientRequestException("Не указан аргумент кабинета. /command [кабинет]." + taplinkbot.bot.BotContexts.getValuesCommaString());

        msg.sourceText = text;
        msg.chatId = chatId;
        msg.cammand = msg.args[0];
        msg.botContext = BotContexts.getByString(msg.args[1]);

        if (msg.botContext == null) throw new ClientRequestException("Не удалось определить кабинет." +

                BotContexts.getValuesCommaString()
        );

        return msg;
    }
}
