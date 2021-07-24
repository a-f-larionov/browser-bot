
package browserbot.telegram;

import lombok.Data;
import lombok.Getter;

/**
 * Ответ может содержать текст,
 * в случае ошибки еще и Exception этой ошибки.
 */
@Data
public class Reponse {

    public enum Type {
        INFO,
        ALERT,
        RESULT
    }

    @Getter
    private final Type type;

    private final String message;

    private Exception exception;

    public Reponse(String message, Type type) {

        this.message = message;
        this.type = type;
    }

    public Reponse(String message, Type type, Exception exception) {

        this(message, type);

        this.exception = exception;
    }

    /**
     * Описание ответа.
     */
    public String getDescription() {
        if (exception != null) {
            return message + "\r\nОшибка: " + exception.getMessage();
        } else {
            return message;
        }
    }
}
