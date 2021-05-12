package taplinkbot.telegram;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import taplinkbot.TapLinkBotException;
import taplinkbot.bot.Profiles;

@Component
@RequiredArgsConstructor
@Slf4j
public class Router {

    private final Parser parser;

    private final Accessor accessor;

    private final Profiles profiles;

    private Commands commands;

    /**
     * Обработка сообщения.
     *
     * @param message сообщение от пользователя телеграмм
     * @param chatId  id чата откуда отправлено сообщение
     * @todo вынести проверку прав паттерны?
     * @todo вынести парсинг команды паттерны?
     */
    public String processMessage(String message, String chatId) {

        Response response;

        try {
            Message msg = parser.parse(message, chatId);

            if (!accessor.check(msg)) throw new TapLinkBotException("Нет доступа");

            String[] args = Message.getFilledArgs(msg);

            profiles.set(msg.profile);

            //response = processCommand(msg.cammand, args[2], args[3], msg.chatId);
            msg.arg1 = args[2];
            msg.arg2 = args[3];
            response = processCommand(msg);

        } catch (TapLinkBotException e) {

            log.info("Ошибка запроса пользователя." + e.getMessage() + " chatId:" + chatId);
            response = new Response("Невозможно обработать команду", e);

        } finally {

            profiles.clear();
        }

        return response.getDescription();
    }

    private Response processCommand(Message msg) {

        try {

            Response response = commands.execute(msg);

            return response;

        } catch (Exception e) {
            e.printStackTrace();
            return new Response("Ошибка выполнения команды", e);
        }
    }

    public void setCommands(Commands commands) {
        this.commands = commands;
    }
}
