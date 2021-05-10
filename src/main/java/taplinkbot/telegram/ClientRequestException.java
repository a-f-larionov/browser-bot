package taplinkbot.telegram;

public class ClientRequestException extends RuntimeException {

    ClientRequestException(String message) {
        super(message);
    }
}
