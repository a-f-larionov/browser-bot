package taplinkbot.telegram;

public class ResponseFactory {

    public static Response buildSuccessReponse() {
        return new Response("Успешно.");
    }

    public static Response buildSuccessReponse(String messsage) {
        return new Response(messsage);
    }

    public static Response buildFailedResponse(String message) {
        return new Response(message);
    }
}
