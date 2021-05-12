//FIN
package taplinkbot.telegram;

import lombok.Data;

/**
 * Ответ может содержать текст,
 * в случае ошибки еще и Exception этой ошибки.
 */
@Data
public class Response {

    private final String message;

    private Exception exception;

    public Response(String message) {
        this.message = message;
    }

    public Response(String message, Exception exception) {

        this.message = message;
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
