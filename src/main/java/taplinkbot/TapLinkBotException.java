//FIN
package taplinkbot;

/**
 * Исключения возникшие во время работы таблинкбота.
 * Их необходимо логировть в лог ошибок
 * Отправлять в ответ на действия через телеграм, веб интерфйс и другие.
 */
public class TapLinkBotException extends RuntimeException {

    public TapLinkBotException(String message) {
        super(message);
    }
}
