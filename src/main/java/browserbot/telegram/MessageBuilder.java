//FIN
package browserbot.telegram;

/**
 * Фабрика ответов на команды в телеграмме.
 */
public class MessageBuilder {

    public static Message buildInfo(String message) {
        return new Message(message, Message.Type.INFO);
    }

    public static Message buildResult() {
        return new Message("Успешно.", Message.Type.RESULT);
    }

    public static Message buildResult(String message) {
        return new Message(message, Message.Type.RESULT);
    }

    public static Message buildAlert(String message) {
        return new Message(message, Message.Type.ALERT);
    }

    public static Message buildAlert(String message, Exception e) {

        return new Message(message, Message.Type.ALERT, e);
    }
}
