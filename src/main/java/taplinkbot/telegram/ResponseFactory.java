//FIN
package taplinkbot.telegram;

/**
 * Фабрика ответов на команды в телеграмме.
 */
public class ResponseFactory {

    public static Response buildSuccessResponse() {
        return new Response("Успешно.");
    }

    public static Response buildSuccessResponse(String messsage) {
        return new Response(messsage);
    }

    public static Response buildFailedResponse(String message) {
        return new Response(message);
    }
}
