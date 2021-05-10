package taplinkbot.telegram;

public interface CommandInterface {

    Response run(Message msg) throws Exception;
}
