package taplinkbot.telegram;

public interface Command {

    void execute();

    String getAnswer();
}
