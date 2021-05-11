//FIN
package taplinkbot.telegram;

public interface TelegramCommandInterface {

    String getDescription();

    Response run(Message msg);
}
