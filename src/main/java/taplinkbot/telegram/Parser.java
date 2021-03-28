package taplinkbot.telegram;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Parser {

    public Message parse(String text, String chatId) {

        Message msg = new Message();

        msg.rawText = text;
        msg.chatId = chatId;

        text = text.replace("@tap_link_bot", "");
        text = text.replace("  ", " ");
        text = text.replace("  ", " ");
        text = text.replace("  ", " ");

        msg.args = text.split(" ");

        System.out.println("text" + text + ",len:" + msg.args.length);

        if (msg.args.length > 0) System.out.println(msg.args[0]);
        if (msg.args.length > 1) System.out.println(msg.args[1]);
        if (msg.args.length > 2) System.out.println(msg.args[2]);

        return msg;
    }
}
