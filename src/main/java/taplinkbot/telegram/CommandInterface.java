//FIN
package taplinkbot.telegram;

public interface CommandInterface {

    String getDescription();

    Response run(Message msg);
}
