
package browserbot.telegram;

/**
 * Фабрика ответов на команды в телеграмме.
 */
public class MessageBuilder {

    public static Reponse buildInfo(String message) {
        return new Reponse(message, Reponse.Type.INFO);
    }

    public static Reponse buildResult() {
        return new Reponse("Успешно.", Reponse.Type.RESULT);
    }

    public static Reponse buildResult(String message) {
        return new Reponse(message, Reponse.Type.RESULT);
    }

    public static Reponse buildAlert(String message) {
        return new Reponse(message, Reponse.Type.ALERT);
    }

    public static Reponse buildAlert(String message, Exception e) {

        return new Reponse(message, Reponse.Type.ALERT, e);
    }
}
