//FIN
package taplinkbot.telegram;

import lombok.Data;
import lombok.Getter;

/**
 * Ответ может содержать текст,
 * в случае ошибки еще и Exception этой ошибки.
 */
@Data
public class Message {

    @Getter
    private final boolean isSuccess;

    private final String message;

    private Exception exception;

    public Message(String message, boolean isSuccess) {

        this.message = message;
        this.isSuccess = isSuccess;
    }

    public Message(String message, boolean isSuccess, Exception exception) {

        this(message, isSuccess);

        this.exception = exception;
    }

    /**
     * Описание ответа.
     *
     * @return
     */
    public String getDescription() {
        if (exception != null) {
            return message + " " + exception.getMessage();
        } else {
            return message;
        }
    }
}
