package taplinkbot.telegram;

import org.springframework.stereotype.Component;
import taplinkbot.TapLinkBotException;
import taplinkbot.bot.Profiles;

@Component
public class Parser {

    public Message parse(String text, String chatId) throws TapLinkBotException {

        Message msg = new Message();

        msg.args = text.replace("@tap_link_bot", "")
                .replace("  ", " ")
                .replace("  ", " ")
                .replace("  ", " ")
                .replace("  ", " ")
                .split(" ");

        if (msg.args.length == 0) throw new TapLinkBotException("Неверная команда. см. /help");
        if (msg.args.length < 2)
            throw new TapLinkBotException("Не указан аргумент кабинета. /command [кабинет]." + taplinkbot.bot.Profiles.getValuesCommaString());

        msg.sourceText = text;
        msg.chatId = chatId;
        msg.cammand = msg.args[0];
        msg.profile = Profiles.findByName(msg.args[1]);

        if (msg.profile == null) throw new TapLinkBotException("Не удалось определить кабинет." +
                Profiles.getValuesCommaString()
        );

        return msg;
    }
}
