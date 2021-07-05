package browserbot.telegram;

import browserbot.telegram.annotations.CommandClass;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RequestBuilder {

    private final Parser parser;

    public Request buildFromTelegramMessage(String message, String chatId) {

        Request req;

        req = parser.parse(message);
        req.initiatorChatId = chatId;

        return req;
    }

    public Request buildFromCommandClass(Class<? extends Command> commandClass) {

        Request req = new Request();

        req.command = commandClass.getAnnotation(CommandClass.class).name();

        return req;
    }
}
