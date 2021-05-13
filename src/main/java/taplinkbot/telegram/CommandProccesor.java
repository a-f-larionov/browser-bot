package taplinkbot.telegram;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CommandProccesor {

    private final RequestBuilder requestBuilder;

    private CommandExecutor commandExecutor;

    /**
     * Обработка сообщения.
     *
     * @param message сообщение от пользователя телеграмм
     * @param chatId  id чата откуда отправлено сообщение
     * @todo вынести проверку прав паттерны?
     * @todo вынести парсинг команды паттерны?
     */
    public void processTelegramMessage(String message, String chatId) {

        Request request = requestBuilder.buildFromTelegramMessage(message, chatId);

        commandExecutor.execute(request);
    }

    public void setCommandExecutor(CommandExecutor commandExecutor) {
        this.commandExecutor = commandExecutor;
    }
}
