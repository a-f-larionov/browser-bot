package browserbot;

/**
 * Исключения возникшие во время работы таблинкбота.
 * Их необходимо логировть в лог ошибок
 * Отправлять в ответ на действия через телеграм, веб интерфейс и другие.
 */
public class BrowserBotException extends RuntimeException {

    public BrowserBotException(String message) {
        super(message);
    }
}
