package taplinkbot.telegram;

import lombok.Data;

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
        exception.printStackTrace();
    }

    public String getDescription() {
        if (exception != null) {
            return message + " " + exception.getMessage();
        } else {
            return message;
        }
    }
}
