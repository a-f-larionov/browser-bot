//FIN
package taplinkbot.telegram;

/**
 * Фабрика ответов на команды в телеграмме.
 */
public class MessageBuilder {

    public static Message buildSuccess() {
        return new Message("Успешно.", true);
    }

    public static Message buildSuccess(String messsage) {
        return new Message(messsage, true);
    }

    public static Message buildFailed(String message) {
        return new Message(message, false);
    }

    public static Message buildFailed(String message, Exception e) {
        return new Message(message, false, e);
    }
}
