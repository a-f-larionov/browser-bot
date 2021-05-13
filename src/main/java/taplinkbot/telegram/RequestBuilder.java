package taplinkbot.telegram;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RequestBuilder {

    private final Parser parser;

    public Request buildFromTelegramMessage(String message, String chatId) {

        Request req;

        req = parser.parse(message);
        req.chatId = chatId;

        return req;
    }
}
