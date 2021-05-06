package taplinkbot.telegram;

public interface Command {

    public void execute();

    public String getAnswer();
}
