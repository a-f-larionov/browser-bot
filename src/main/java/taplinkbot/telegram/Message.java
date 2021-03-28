package taplinkbot.telegram;

public class Message {

    public String rawText;

    public String chatId;

    public String[] args;

    public final static String noArgumentValue = "нет аргумента";

    public static String[] getPadArgs(Message message) {
        String[] args = new String[3];

        args[0] = noArgumentValue;
        args[1] = noArgumentValue;
        args[2] = noArgumentValue;

        if (message.args.length > 0) args[0] = message.args[0];
        if (message.args.length > 1) args[0] = message.args[1];
        if (message.args.length > 2) args[0] = message.args[2];

        return args;
    }
}
